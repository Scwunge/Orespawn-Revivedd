package danger.orespawn.entity;

import danger.orespawn.init.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

/**
 * Gold {@code CliffRacer} (EntityAnimal). Size 0.75×0.5, speed 0.33, health 5, attack 1, XP 5,
 * follow range 5. Passive flyer; random flight waypoints. Registry size set in {@code ModEntities}.
 */
public class CliffRacer extends Animal {
    @Nullable
    private BlockPos currentFlightTarget;

    public CliffRacer(EntityType<? extends CliffRacer> type, Level level) {
        super(type, level);
        this.xpReward = 5;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Animal.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 5.0) // mygetMaxHealth()
                .add(Attributes.MOVEMENT_SPEED, 0.33)
                .add(Attributes.ATTACK_DAMAGE, 1.0)
                .add(Attributes.FOLLOW_RANGE, 5.0); // gold field_70174_ab = 5
    }

    public int mygetMaxHealth() {
        return 5;
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        // gold: !isNoDespawnRequired
        return !this.isPersistenceRequired();
    }

    @Override
    protected float getSoundVolume() {
        return 0.45F;
    }

    @Override
    public float getVoicePitch() {
        return 1.0F;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        //  gold: orespawn:cliffracer — ogg present; SoundsHandler deferred
        return null;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        //  gold: null
        return null;
    }

    @Override
    protected SoundEvent getDeathSound() {
        //  gold: null
        return null;
    }

    @Override
    public boolean isPushable() {
        // gold: canBePushed true
        return true;
    }

    /** Gold {@code collideWithEntity} empty. */
    @Override
    protected void doPush(Entity entity) {}

    @Override
    public void tick() {
        super.tick();
        // gold livingUpdate: motionY *= 0.6
        Vec3 m = this.getDeltaMovement();
        this.setDeltaMovement(m.x, m.y * 0.6, m.z);
    }

    private boolean canSeeTarget(double pX, double pY, double pZ) {
        // gold: eye at y+0.75
        return this.level()
                        .clip(new ClipContext(
                                new Vec3(this.getX(), this.getY() + 0.75, this.getZ()),
                                new Vec3(pX, pY, pZ),
                                ClipContext.Block.COLLIDER,
                                ClipContext.Fluid.NONE,
                                this))
                        .getType()
                == HitResult.Type.MISS;
    }

    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
        if (this.isDeadOrDying()) {
            return;
        }
        if (this.currentFlightTarget == null) {
            this.currentFlightTarget = this.blockPosition();
        }

        // gold: new waypoint 1/300 or distSq < 2.1
        if (this.random.nextInt(300) == 0
                || this.currentFlightTarget.distToCenterSqr(this.getX(), this.getY(), this.getZ()) < 2.1F) {
            int keepTrying = 50;
            BlockState bid = Blocks.STONE.defaultBlockState();
            while (!bid.isAir() && keepTrying != 0) {
                keepTrying--;
                // gold: nextInt(10)+5 (wider than Dragonfly/GoldFish 5+5)
                int zdir = this.random.nextInt(10) + 5;
                int xdir = this.random.nextInt(10) + 5;
                if (this.random.nextInt(2) == 0) {
                    zdir = -zdir;
                }
                if (this.random.nextInt(2) == 0) {
                    xdir = -xdir;
                }
                this.currentFlightTarget = BlockPos.containing(
                        this.getX() + xdir,
                        this.getY() + this.random.nextInt(11) - 5,
                        this.getZ() + zdir);
                bid = this.level().getBlockState(this.currentFlightTarget);
                if (bid.isAir()
                        && !this.canSeeTarget(
                                this.currentFlightTarget.getX(),
                                this.currentFlightTarget.getY(),
                                this.currentFlightTarget.getZ())) {
                    bid = Blocks.STONE.defaultBlockState();
                }
            }
        }

        // gold flight velocity (signum 0.4 xz / 0.7 y)
        double var1 = this.currentFlightTarget.getX() + 0.4 - this.getX();
        double var3 = this.currentFlightTarget.getY() + 0.1 - this.getY();
        double var5 = this.currentFlightTarget.getZ() + 0.4 - this.getZ();
        Vec3 m = this.getDeltaMovement();
        this.setDeltaMovement(
                m.x + (Math.signum(var1) * 0.4 - m.x) * 0.3,
                m.y + (Math.signum(var3) * 0.7 - m.y) * 0.2,
                m.z + (Math.signum(var5) * 0.4 - m.z) * 0.3);
        float var7 =
                (float) (Math.atan2(this.getDeltaMovement().z, this.getDeltaMovement().x) * 180.0 / Math.PI) - 90.0F;
        float var8 = Mth.wrapDegrees(var7 - this.getYRot());
        this.setZza(0.75F);
        this.setYRot(this.getYRot() + var8 / 6.0F);
    }

    @Override
    public boolean causeFallDamage(float fallDistance, float multiplier, DamageSource source) {
        return false;
    }

    @Override
    protected void checkFallDamage(double y, boolean onGround, BlockState state, BlockPos pos) {
        // gold empty
    }

    @Override
    public boolean isIgnoringBlockTriggers() {
        // gold func_145773_az = false
        return false;
    }

    /** Gold {@code getCanSpawnHere}: y ≥ 50. */
    @Override
    public boolean checkSpawnRules(LevelAccessor level, MobSpawnType spawnType) {
        return !(this.getY() < 50.0);
    }

    @Override
    protected void dropCustomDeathLoot(ServerLevel level, DamageSource damageSource, boolean recentlyHit) {
        super.dropCustomDeathLoot(level, damageSource, recentlyHit);
        // gold getDropItem: nextInt(8) → bone / uranium / titanium / null
        int i = this.random.nextInt(8);
        if (i == 0) {
            this.spawnAtLocation(new ItemStack(Items.BONE));
        } else if (i == 1) {
            this.spawnAtLocation(new ItemStack(ModItems.URANIUM_NUGGET.get()));
        } else if (i == 2) {
            this.spawnAtLocation(new ItemStack(ModItems.TITANIUM_NUGGET.get()));
        }
    }

    @Override
    public boolean isFood(ItemStack stack) {
        // gold createChild null — not breedable
        return false;
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob other) {
        //  gold createChild: null
        return null;
    }
}
