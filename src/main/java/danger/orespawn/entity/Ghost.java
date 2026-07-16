package danger.orespawn.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ambient.AmbientCreature;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

/**
 * Gold {@code Ghost} (EntityAmbientCreature). Size 0.5×1.5, speed 0.1, health 2, attack 0, XP 5.
 * No-clip flyer; drifts toward nearby players; ignores in-wall damage; night spawn.
 */
public class Ghost extends AmbientCreature {
    @Nullable
    private BlockPos currentFlightTarget;

    public Ghost(EntityType<? extends Ghost> type, Level level) {
        super(type, level);
        this.xpReward = 5; // gold field_70728_aV
        this.noPhysics = true; // gold field_70145_X
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 2.0) // mygetMaxHealth()
                .add(Attributes.MOVEMENT_SPEED, 0.1)
                .add(Attributes.ATTACK_DAMAGE, 0.0)
                .add(Attributes.FOLLOW_RANGE, 16.0);
    }

    public int mygetMaxHealth() {
        return 2;
    }

    @Override
    protected float getSoundVolume() {
        return 0.3F; // gold func_70599_aP
    }

    @Override
    public float getVoicePitch() {
        return 1.5F; // gold func_70647_i
    }

    @Override
    protected SoundEvent getAmbientSound() {
        //  gold: 50% orespawn:ghost_sound — ogg present; SoundsHandler wiring deferred
        return null;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return null;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return null;
    }

    /** Gold {@code canBePushed} false. */
    @Override
    public boolean isPushable() {
        return false;
    }

    /** Gold empty {@code collideWithEntity}. */
    @Override
    protected void doPush(Entity entity) {}

    /** Gold empty {@code collideWithNearbyEntities}. */
    @Override
    protected void pushEntities() {}

    @Override
    public void tick() {
        // gold: if noDespawnRequired → noClip false
        if (this.isPersistenceRequired()) {
            this.noPhysics = false;
        }
        super.tick();
        // gold: motionY *= 0.65
        Vec3 m = this.getDeltaMovement();
        this.setDeltaMovement(m.x, m.y * 0.65, m.z);
    }

    /**
     * Gold {@code updateAITasks}: pick flight target near player (or random air),
     * soft-steer velocity + yaw.
     */
    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
        if (this.isDeadOrDying()) {
            return;
        }
        if (this.currentFlightTarget == null) {
            this.currentFlightTarget = this.blockPosition();
        }

        if (this.random.nextInt(40) == 1
                || this.currentFlightTarget.distToCenterSqr(this.getX(), this.getY(), this.getZ()) < 2.0F) {
            Player target = this.level().getNearestPlayer(this, 16.0);
            if (target != null) {
                this.currentFlightTarget = BlockPos.containing(
                        target.getX() + this.random.nextInt(3) - this.random.nextInt(3),
                        target.getY() + 1.0,
                        target.getZ() + this.random.nextInt(3) - this.random.nextInt(3));
            } else {
                int i = 0;
                int j = 0;
                for (i = 0; i < 3; i++) {
                    if (this.level()
                            .getBlockState(BlockPos.containing(this.getX(), this.getY() + i, this.getZ()))
                            .isAir()) {
                        break;
                    }
                }
                for (j = -1; j >= -3; j--) {
                    if (!this.level()
                            .getBlockState(BlockPos.containing(this.getX(), this.getY() + j, this.getZ()))
                            .isAir()) {
                        break;
                    }
                }
                this.currentFlightTarget = BlockPos.containing(
                        this.getX() + this.random.nextInt(10) - this.random.nextInt(10),
                        this.getY() + i + j + this.random.nextInt(4) + 1,
                        this.getZ() + this.random.nextInt(10) - this.random.nextInt(10));
            }
        }

        double var1 = this.currentFlightTarget.getX() + 0.5 - this.getX();
        double var3 = this.currentFlightTarget.getY() + 0.1 - this.getY();
        double var5 = this.currentFlightTarget.getZ() + 0.5 - this.getZ();
        Vec3 m = this.getDeltaMovement();
        this.setDeltaMovement(
                m.x + (Math.signum(var1) * 0.1 - m.x) * 0.05,
                m.y + (Math.signum(var3) * 0.7 - m.y) * 0.1,
                m.z + (Math.signum(var5) * 0.1 - m.z) * 0.05);
        float var7 =
                (float) (Math.atan2(this.getDeltaMovement().z, this.getDeltaMovement().x) * 180.0 / Math.PI) - 90.0F;
        float var8 = Mth.wrapDegrees(var7 - this.getYRot());
        this.setZza(0.05F); // gold field_70701_bs
        this.setYRot(this.getYRot() + var8 / 6.0F);
    }

    @Override
    public boolean causeFallDamage(float fallDistance, float multiplier, DamageSource source) {
        return false;
    }

    @Override
    protected void checkFallDamage(double y, boolean onGround, BlockState state, BlockPos pos) {}

    /** Gold {@code canTriggerWalking} false. */
    @Override
    public boolean isIgnoringBlockTriggers() {
        return true;
    }

    // gold canBreatheUnderwater true — LivingEntity API not overridable the same way in 1.21.1; deferred

    /** Gold {@code canDespawn}: !noDespawnRequired. */
    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return !this.isPersistenceRequired();
    }

    /** Gold: ignore inWall damage. */
    @Override
    public boolean hurt(DamageSource source, float amount) {
        if ("inWall".equals(source.getMsgId()) || source == this.damageSources().inWall()) {
            return false;
        }
        return super.hurt(source, amount);
    }

    /**
     * Gold {@code getCanSpawnHere}: near Ghost spawner (deferred) OR night.
     */
    @Override
    public boolean checkSpawnRules(LevelAccessor level, MobSpawnType spawnType) {
        if (level instanceof Level lvl) {
            return !lvl.isDay();
        }
        return true;
    }
}
