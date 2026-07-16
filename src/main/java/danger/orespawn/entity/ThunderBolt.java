package danger.orespawn.entity;

import danger.orespawn.init.ModEntities;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.neoforged.neoforge.event.EventHooks;

/**
 * Gold {@code ThunderBolt} (EntityThrowable). Hit: 20 thrown + 20 mob damage (40 total),
 * ignite 1s, smoke/spark FX, explode power 3, lightning at impact. Royalty ignored.
 * Thunder staff multiplies launch velocity ×3.
 * <p>
 * Registry: {@code thunder_bolt}.
 */
public class ThunderBolt extends ThrowableProjectile {

    public ThunderBolt(EntityType<? extends ThunderBolt> type, Level level) {
        super(type, level);
    }

    public ThunderBolt(Level level, LivingEntity thrower) {
        super(ModEntities.THUNDER_BOLT.get(), thrower, level);
    }

    public ThunderBolt(Level level, double x, double y, double z) {
        super(ModEntities.THUNDER_BOLT.get(), x, y, z, level);
    }

    /** Gold ItemThunderStaff: position offset + motion ×3. */
    public void launchFromStaff(LivingEntity thrower) {
        double xzoff = 1.0;
        double yoff = 1.55;
        float yaw = thrower.getYRot();
        this.setPos(
                thrower.getX() - xzoff * Math.sin(Math.toRadians(yaw + 45.0F)),
                thrower.getY() + yoff,
                thrower.getZ() + xzoff * Math.cos(Math.toRadians(yaw + 45.0F)));
        this.setYRot(yaw);
        this.setXRot(thrower.getXRot());
        this.shootFromRotation(thrower, thrower.getXRot(), thrower.getYRot(), 0.0F, 1.5F, 1.0F);
        this.setDeltaMovement(this.getDeltaMovement().scale(3.0));
        this.hasImpulse = true;
    }

    /** Mob / general launch at aim. */
    public void launchFrom(LivingEntity thrower, float velocity) {
        this.shootFromRotation(thrower, thrower.getXRot(), thrower.getYRot(), 0.0F, velocity, 1.0F);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {}

    @Override
    protected double getDefaultGravity() {
        return 0.03;
    }

    @Override
    protected void onHit(HitResult result) {
        if (result.getType() == HitResult.Type.ENTITY) {
            Entity hit = ((EntityHitResult) result).getEntity();
            if (isRoyalty(hit)) {
                if (!this.level().isClientSide) {
                    this.discard();
                }
                return;
            }
            if (!this.level().isClientSide) {
                float half = 20.0F; // gold var2=40 → half each source
                Entity owner = this.getOwner();
                hit.hurt(this.damageSources().thrown(this, owner), half);
                if (owner instanceof LivingEntity livingOwner) {
                    hit.hurt(this.damageSources().mobAttack(livingOwner), half);
                } else {
                    hit.hurt(this.damageSources().magic(), half);
                }
                hit.igniteForSeconds(1);
            }
        }

        int mx = 20;
        for (int i = 0; i < mx; i++) {
            this.level().addParticle(
                    ParticleTypes.SMOKE,
                    this.getX() + this.random.nextFloat() - this.random.nextFloat(),
                    this.getY() + this.random.nextFloat() - this.random.nextFloat(),
                    this.getZ() + this.random.nextFloat(),
                    0.0,
                    0.0,
                    0.0);
            this.level().addParticle(
                    ParticleTypes.LARGE_SMOKE,
                    this.getX() + this.random.nextFloat() - this.random.nextFloat(),
                    this.getY() + this.random.nextFloat() - this.random.nextFloat(),
                    this.getZ() + this.random.nextFloat() - this.random.nextFloat(),
                    0.0,
                    0.0,
                    0.0);
            this.level().addParticle(
                    ParticleTypes.FIREWORK,
                    this.getX(),
                    this.getY(),
                    this.getZ(),
                    this.random.nextGaussian(),
                    this.random.nextGaussian(),
                    this.random.nextGaussian());
        }

        this.playSound(
                SoundEvents.GENERIC_EXPLODE.value(),
                0.5F,
                1.0F + (this.random.nextFloat() - this.random.nextFloat()) * 0.5F);

        if (!this.level().isClientSide && this.level() instanceof ServerLevel server) {
            boolean grief = server.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING);
            if (this.getOwner() != null) {
                grief = EventHooks.canEntityGrief(server, this.getOwner());
            }
            server.explode(
                    this,
                    this.getX(),
                    this.getY(),
                    this.getZ(),
                    3.0F,
                    grief ? Level.ExplosionInteraction.MOB : Level.ExplosionInteraction.NONE);

            // gold: EntityLightningBolt at (x, y+1, z)
            LightningBolt bolt = EntityType.LIGHTNING_BOLT.create(server);
            if (bolt != null) {
                bolt.moveTo(this.getX(), this.getY() + 1.0, this.getZ());
                server.addFreshEntity(bolt);
            }

            this.discard();
        }
    }

    /** Gold {@code MyUtils.isRoyalty}. */
    private static boolean isRoyalty(Entity e) {
        if (!(e instanceof LivingEntity)) {
            return false;
        }
        return e instanceof ThePrince
                || e instanceof ThePrinceTeen
                || e instanceof ThePrinceAdult
                || e instanceof ThePrincess
                || e instanceof TheKing
                || e instanceof KingHead
                || e instanceof TheQueen;
    }

    @Override
    public void tick() {
        super.tick();
        for (int i = 0; i < 4; i++) {
            this.level().addParticle(
                    ParticleTypes.FIREWORK,
                    this.getX(),
                    this.getY(),
                    this.getZ(),
                    this.random.nextGaussian() / 10.0,
                    this.random.nextGaussian() / 10.0,
                    this.random.nextGaussian() / 10.0);
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
    }
}
