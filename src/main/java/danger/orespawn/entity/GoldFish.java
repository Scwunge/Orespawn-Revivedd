package danger.orespawn.entity;

import danger.orespawn.init.ModItems;
import danger.orespawn.util.handlers.SoundsHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
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
 * Gold {@code GoldFish} (EntityAnimal). Size 0.75×0.5, speed 0.22, health 6, attack 1, XP 5.
 * Sky flyer (prefers y 120–140); no breed offspring. Flight AI from gold updateAITasks.
 * canBreatheUnderwater=true in gold — LivingEntity method final; air forced each tick.
 */
public class GoldFish extends Animal {
    @Nullable
    private BlockPos currentFlightTarget;

    public GoldFish(EntityType<? extends GoldFish> type, Level level) {
        super(type, level);
        this.xpReward = 5;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Animal.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 6.0) // mygetMaxHealth()
                .add(Attributes.MOVEMENT_SPEED, 0.22)
                .add(Attributes.ATTACK_DAMAGE, 1.0)
                .add(Attributes.FOLLOW_RANGE, 5.0); // gold field_70174_ab = 5
    }

    public int mygetMaxHealth() {
        return 6;
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        // gold: if noDespawnRequired → false; else !isDaytime
        if (this.isPersistenceRequired()) {
            return false;
        }
        if (this.level() instanceof Level lvl) {
            return !lvl.isDay();
        }
        return true;
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
        // gold: "splash"
        return SoundEvents.GENERIC_SPLASH;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        // gold: "splash"
        return SoundEvents.GENERIC_SPLASH;
    }

    @Override
    protected SoundEvent getDeathSound() {
        // gold: orespawn:little_splat
        return SoundsHandler.LITTLE_SPLAT.get();
    }

    @Override
    public boolean isPushable() {
        return true;
    }

    /** Gold {@code collideWithEntity} empty. */
    @Override
    protected void doPush(Entity entity) {}

    @Override
    public void tick() {
        super.tick();
        // gold canBreatheUnderwater — restore air
        this.setAirSupply(this.getMaxAirSupply());
        // gold livingUpdate: motionY *= 0.6
        Vec3 m = this.getDeltaMovement();
        this.setDeltaMovement(m.x, m.y * 0.6, m.z);
    }

    private boolean canSeeTarget(double pX, double pY, double pZ) {
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

        int updown = 0;
        if ((int) this.getY() < 120) {
            updown = 2;
        }
        if ((int) this.getY() > 140) {
            updown = -2;
        }

        if (this.random.nextInt(300) == 0
                || this.currentFlightTarget.distToCenterSqr(this.getX(), this.getY(), this.getZ()) < 2.1F) {
            int keepTrying = 50;
            BlockState bid = Blocks.STONE.defaultBlockState();
            while (!bid.isAir() && keepTrying != 0) {
                keepTrying--;
                int zdir = this.random.nextInt(5) + 5;
                int xdir = this.random.nextInt(5) + 5;
                if (this.random.nextInt(2) == 0) {
                    zdir = -zdir;
                }
                if (this.random.nextInt(2) == 0) {
                    xdir = -xdir;
                }
                this.currentFlightTarget = BlockPos.containing(
                        this.getX() + xdir,
                        this.getY() + this.random.nextInt(11) - 5 + updown,
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
    protected void checkFallDamage(double y, boolean onGround, BlockState state, BlockPos pos) {}

    @Override
    public boolean isIgnoringBlockTriggers() {
        // gold func_145773_az = false (not ignoring)
        return false;
    }

    @Override
    public boolean checkSpawnRules(LevelAccessor level, MobSpawnType spawnType) {
        // gold getCanSpawnHere: return true
        return true;
    }

    @Override
    protected void dropCustomDeathLoot(ServerLevel level, DamageSource damageSource, boolean recentlyHit) {
        super.dropCustomDeathLoot(level, damageSource, recentlyHit);
        // gold: nextInt(3) → gold block / uranium nugget / titanium nugget
        int i = this.random.nextInt(3);
        if (i == 0) {
            this.spawnAtLocation(new ItemStack(Items.GOLD_BLOCK));
        } else if (i == 1) {
            this.spawnAtLocation(new ItemStack(ModItems.URANIUM_NUGGET.get()));
        } else {
            this.spawnAtLocation(new ItemStack(ModItems.TITANIUM_NUGGET.get()));
        }
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return false;
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob other) {
        //  gold createChild: null
        return null;
    }
}
