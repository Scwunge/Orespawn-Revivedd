package danger.orespawn.entity;

import danger.orespawn.util.handlers.SoundsHandler;
import java.util.List;
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
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

/**
 * Gold {@code Mosquito} (EntityAmbientCreature). Size 0.2×0.2, speed 0.1, health 2, attack 0, XP 5.
 * Flies; 1/4 repaths seek nearest player head (blood) in inflate(10,6,10); attack attr stays 0.
 */
public class Mosquito extends AmbientCreature {
    @Nullable
    private BlockPos currentFlightTarget;

    public Mosquito(EntityType<? extends Mosquito> type, Level level) {
        super(type, level);
        this.xpReward = 5;
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
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        // gold: !isNoDespawnRequired / !func_104002_bU
        return !this.isPersistenceRequired();
    }

    @Override
    protected float getSoundVolume() {
        return 0.4F;
    }

    @Override
    public float getVoicePitch() {
        return 1.5F;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundsHandler.ENTITY_MOSQUITO_LIVING.get();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return null;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return null;
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    /** Gold {@code collideWithEntity} empty — no shove on contact. */
    @Override
    protected void doPush(Entity entity) {}

    @Override
    protected void pushEntities() {}

    @Override
    public void tick() {
        super.tick();
        // gold: motionY *= 0.6F each tick (flight damp)
        Vec3 m = this.getDeltaMovement();
        this.setDeltaMovement(m.x, m.y * 0.6F, m.z);
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
        // gold: repath every 1/20 or when within 3 of target
        if (this.random.nextInt(20) == 0
                || this.currentFlightTarget.distToCenterSqr(this.getX(), this.getY(), this.getZ()) < 3.0) {
            int keepTrying = 50;
            // gold: 1/4 chance seek player (blood) else random air cell
            if (this.random.nextInt(4) == 0) {
                Player target = this.findNearestPlayerInBloodRange();
                if (target != null) {
                    // gold: fly to player head (Y+2)
                    this.currentFlightTarget = BlockPos.containing(target.getX(), target.getY() + 2, target.getZ());
                } else {
                    this.pickAirTarget(keepTrying);
                }
            } else {
                this.pickAirTarget(keepTrying);
            }
        }
        double var1 = this.currentFlightTarget.getX() + 0.5 - this.getX();
        double var3 = this.currentFlightTarget.getY() + 0.1 - this.getY();
        double var5 = this.currentFlightTarget.getZ() + 0.5 - this.getZ();
        Vec3 m = this.getDeltaMovement();
        this.setDeltaMovement(
                m.x + (Math.signum(var1) * 0.5 - m.x) * 0.1F,
                m.y + (Math.signum(var3) * 0.7F - m.y) * 0.1F,
                m.z + (Math.signum(var5) * 0.5 - m.z) * 0.1F);
        float var7 = (float) (Math.atan2(this.getDeltaMovement().z, this.getDeltaMovement().x) * 180.0 / Math.PI) - 90.0F;
        float var8 = Mth.wrapDegrees(var7 - this.getYRot());
        this.setZza(0.3F);
        this.setYRot(this.getYRot() + var8);
    }

    /**
     * Gold {@code world.findNearestEntityWithinAABB(EntityPlayer, expand(10,6,10), this)}.
     * Seeks player head for "blood" — gold ATTACK_DAMAGE remains 0 (no contact damage).
     */
    @Nullable
    private Player findNearestPlayerInBloodRange() {
        AABB box = this.getBoundingBox().inflate(10.0, 6.0, 10.0);
        List<Player> players = this.level().getEntitiesOfClass(Player.class, box, Player::isAlive);
        Player nearest = null;
        double best = Double.MAX_VALUE;
        for (Player p : players) {
            double d = this.distanceToSqr(p);
            if (d < best) {
                best = d;
                nearest = p;
            }
        }
        return nearest;
    }

    /** Gold: keep_trying air cell pick (±6 xz, ±2–3 y, stop on air). */
    private void pickAirTarget(int keepTrying) {
        BlockState bid = Blocks.STONE.defaultBlockState();
        while (!bid.isAir() && keepTrying != 0) {
            keepTrying--;
            this.currentFlightTarget = BlockPos.containing(
                    this.getX() + this.random.nextInt(6) - this.random.nextInt(6),
                    this.getY() + this.random.nextInt(6) - 2,
                    this.getZ() + this.random.nextInt(6) - this.random.nextInt(6));
            bid = this.level().getBlockState(this.currentFlightTarget);
        }
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

    /**
     * Gold {@code getCanSpawnHere} always true (swamp biome weights elsewhere).
     */
    @Override
    public boolean checkSpawnRules(LevelAccessor level, MobSpawnType spawnType) {
        return true;
    }
}
