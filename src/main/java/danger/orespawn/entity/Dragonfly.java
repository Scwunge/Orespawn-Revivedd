package danger.orespawn.entity;

import danger.orespawn.OreSpawnMain;
import danger.orespawn.init.ModItems;
import danger.orespawn.util.handlers.SoundsHandler;
import java.util.Comparator;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

/**
 * Gold {@code Dragonfly} (EntityAnimal). Size 1.5×0.5, speed 0.33, health 10, attack 2, XP 5.
 * Flies; hunts Bird/Butterfly when not peaceful. Spawn: y≥50 + daytime.
 */
public class Dragonfly extends Animal {
    @Nullable
    private BlockPos currentFlightTarget;

    public Dragonfly(EntityType<? extends Dragonfly> type, Level level) {
        super(type, level);
        this.xpReward = 5;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Animal.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 10.0) // mygetMaxHealth()
                .add(Attributes.MOVEMENT_SPEED, 0.33)
                .add(Attributes.ATTACK_DAMAGE, 2.0)
                .add(Attributes.FOLLOW_RANGE, 16.0);
    }

    public int mygetMaxHealth() {
        return 10;
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return !this.isPersistenceRequired();
    }

    @Override
    protected float getSoundVolume() {
        return 0.25F;
    }

    @Override
    public float getVoicePitch() {
        return 1.0F;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundsHandler.ENTITY_DRAGONFLY_LIVING.get();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundsHandler.ENTITY_DRAGONFLY_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        // entity/dragonfly/dragonfly_death.ogg present (gold had no death holder)
        return SoundsHandler.ENTITY_DRAGONFLY_DEATH.get();
    }

    /** Gold {@code collideWithEntity} empty — no shove on contact. */
    @Override
    protected void doPush(Entity entity) {}

    @Override
    public void tick() {
        super.tick();
        Vec3 m = this.getDeltaMovement();
        this.setDeltaMovement(m.x, m.y * 0.6, m.z);
    }

    @Override
    public boolean doHurtTarget(Entity target) {
        // gold: fixed 2.0 damage
        return target.hurt(this.damageSources().mobAttack(this), 2.0F);
    }

    private boolean canSeeTarget(double pX, double pY, double pZ) {
        return this.level().clip(new net.minecraft.world.level.ClipContext(
                new Vec3(this.getX(), this.getY() + 0.25, this.getZ()),
                new Vec3(pX, pY, pZ),
                net.minecraft.world.level.ClipContext.Block.COLLIDER,
                net.minecraft.world.level.ClipContext.Fluid.NONE,
                this)).getType() == net.minecraft.world.phys.HitResult.Type.MISS;
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
                        this.getX() + xdir, this.getY() + this.random.nextInt(5) - 2, this.getZ() + zdir);
                bid = this.level().getBlockState(this.currentFlightTarget);
                if (bid.isAir()
                        && !this.canSeeTarget(
                                this.currentFlightTarget.getX(),
                                this.currentFlightTarget.getY(),
                                this.currentFlightTarget.getZ())) {
                    bid = Blocks.STONE.defaultBlockState();
                }
            }
        } else if (this.random.nextInt(12) == 0 && this.level().getDifficulty() != Difficulty.PEACEFUL) {
            LivingEntity e = this.findSomethingToAttack();
            if (e != null) {
                this.currentFlightTarget = BlockPos.containing(e.getX(), e.getY() + 1.0, e.getZ());
                if (this.distanceToSqr(e) < 6.0) {
                    this.doHurtTarget(e);
                }
            }
        }
        double var1 = this.currentFlightTarget.getX() + 0.5 - this.getX();
        // gold bug: uses getX for Y and Z terms — keep 1:1
        double var3 = this.currentFlightTarget.getX() + 0.1 - this.getY();
        double var5 = this.currentFlightTarget.getX() + 0.5 - this.getZ();
        Vec3 m = this.getDeltaMovement();
        this.setDeltaMovement(
                m.x + (Math.signum(var1) * 0.5 - m.x) * 0.30000000149011613,
                m.y + (Math.signum(var3) * 0.7F - m.y) * 0.20000000149011612,
                m.z + (Math.signum(var5) * 0.5 - m.z) * 0.30000000149011613);
        float var7 = (float) (Math.atan2(this.getDeltaMovement().z, this.getDeltaMovement().x) * 180.0 / Math.PI) - 90.0F;
        float var8 = Mth.wrapDegrees(var7 - this.getYRot());
        this.setZza(1.0F);
        this.setYRot(this.getYRot() + var8 / 4.0F);
    }

    @Override
    public boolean causeFallDamage(float fallDistance, float multiplier, DamageSource source) {
        return false;
    }

    @Override
    protected void checkFallDamage(double y, boolean onGround, BlockState state, BlockPos pos) {}

    @Override
    public boolean hurt(DamageSource source, float amount) {
        boolean ret = super.hurt(source, amount);
        Entity e = source.getEntity();
        if (e != null && this.currentFlightTarget != null) {
            this.currentFlightTarget = BlockPos.containing(e.getX(), e.getY(), e.getZ());
        }
        return ret;
    }

    private boolean isSuitableTarget(LivingEntity target) {
        if (this.level().getDifficulty() == Difficulty.PEACEFUL) {
            return false;
        }
        if (target == null || target == this || !target.isAlive()) {
            return false;
        }
        if (!this.hasLineOfSight(target)) {
            return false;
        }
        return target instanceof Butterfly || target instanceof Bird;
    }

    @Nullable
    private LivingEntity findSomethingToAttack() {
        // gold: PlayNicely != 0 → null
        if (OreSpawnMain.PlayNicely != 0) {
            return null;
        }
        List<LivingEntity> list = this.level().getEntitiesOfClass(
                LivingEntity.class, this.getBoundingBox().inflate(10.0, 6.0, 10.0));
        // gold GenericTargetSorter — nearest first
        list.sort(Comparator.comparingDouble(this::distanceToSqr));
        for (LivingEntity living : list) {
            if (this.isSuitableTarget(living)) {
                return living;
            }
        }
        return null;
    }

    /**
     * Gold {@code getCanSpawnHere}: y &lt; 50 → false; else world.isDaytime().
     */
    @Override
    public boolean checkSpawnRules(LevelAccessor level, MobSpawnType spawnType) {
        if (this.getY() < 50.0) {
            return false;
        }
        if (level instanceof Level lvl) {
            return lvl.isDay();
        }
        return true;
    }

    @Override
    protected void dropCustomDeathLoot(ServerLevel level, DamageSource damageSource, boolean recentlyHit) {
        super.dropCustomDeathLoot(level, damageSource, recentlyHit);
        int i = this.random.nextInt(6);
        if (i == 0) {
            this.spawnAtLocation(new ItemStack(Items.GLOWSTONE_DUST));
        } else if (i == 1) {
            this.spawnAtLocation(new ItemStack(ModItems.URANIUM_NUGGET.get()));
        } else if (i == 2) {
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
        return null;
    }
}
