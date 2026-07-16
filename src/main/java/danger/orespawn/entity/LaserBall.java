package danger.orespawn.entity;

import danger.orespawn.init.ModEntities;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.neoforged.neoforge.event.EventHooks;

/**
 * Gold {@code LaserBall} (EntityThrowable). Base damage 16 + fire; variants:
 * special (more FX / explode), iceball (no fire, explode), acid (no FX, bug immunities),
 * irukandji (100 dmg). Lifetime 200 ticks.
 * <p>
 * Registry: {@code laser_ball}.
 */
public class LaserBall extends ThrowableProjectile {
    private float myRotation = 0.0F;
    /** Gold item-render index (sprite sheet slot). */
    private int myIndex = 81;
    private int isSpecial = 0;
    private int isIceball = 0;
    private int isAcid = 0;
    private int isIrukandji = 0;
    private int ticksAlive = 0;

    public LaserBall(EntityType<? extends LaserBall> type, Level level) {
        super(type, level);
    }

    public LaserBall(Level level, LivingEntity thrower) {
        super(ModEntities.LASER_BALL.get(), thrower, level);
    }

    public LaserBall(Level level, double x, double y, double z) {
        super(ModEntities.LASER_BALL.get(), x, y, z, level);
    }

    /** Package/subclass ctor with explicit EntityType (IceBall). */
    protected LaserBall(EntityType<? extends LaserBall> type, LivingEntity thrower, Level level) {
        super(type, thrower, level);
    }

    protected LaserBall(EntityType<? extends LaserBall> type, double x, double y, double z, Level level) {
        super(type, x, y, z, level);
    }

    public void launchFrom(LivingEntity thrower) {
        this.shootFromRotation(thrower, thrower.getXRot(), thrower.getYRot(), 0.0F, 1.5F, 1.0F);
    }

    public int getLaserBallIndex() {
        return this.myIndex;
    }

    public void setSpecial() {
        this.isSpecial = 1;
    }

    public void setIceBall() {
        this.isIceball = 1;
    }

    public void setAcid() {
        this.isAcid = 1;
    }

    public void setIrukandji() {
        this.isIrukandji = 1;
        this.isAcid = 1;
    }

    public boolean isIceBallMode() {
        return this.isIceball != 0;
    }

    public boolean isAcidMode() {
        return this.isAcid != 0;
    }

    public boolean isSpecialMode() {
        return this.isSpecial != 0;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {}

    @Override
    protected double getDefaultGravity() {
        return 0.03;
    }

    @Override
    protected void onHit(HitResult result) {
        if (this.level().isClientSide) {
            return;
        }

        if (result.getType() == HitResult.Type.ENTITY) {
            if (!this.handleEntityHit(((EntityHitResult) result).getEntity())) {
                return;
            }
        } else if (this.isIrukandji != 0) {
            // gold: drop MyIrukandji on block miss — item deferred
        }

        if (this.isAcid == 0) {
            spawnImpactFx();
            this.playSound(
                    SoundEvents.GENERIC_EXPLODE.value(),
                    0.5F,
                    1.0F + (this.random.nextFloat() - this.random.nextFloat()) * 0.5F);
            if (this.isSpecial != 0 || this.isIceball != 0) {
                boolean grief = this.level().getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING);
                if (this.getOwner() != null) {
                    grief = EventHooks.canEntityGrief(this.level(), this.getOwner());
                }
                this.level().explode(
                        this,
                        this.getX(),
                        this.getY(),
                        this.getZ(),
                        3.0F,
                        grief ? Level.ExplosionInteraction.MOB : Level.ExplosionInteraction.NONE);
            }
        }

        this.discard();
    }

    /**
     * @return false if hit should abort without discard (gold early return keeps ball alive only when
     *         discarded inside branch — gold always dies via final kill; early returns call setDead)
     */
    protected boolean handleEntityHit(Entity hit) {
        if (this.isIrukandji != 0) {
            hit.hurt(this.damageSources().thrown(this, this.getOwner()), 100.0F);
            this.discard();
            return false;
        }

        if (this.isAcid != 0) {
            if (hit instanceof TrooperBug || hit instanceof SpitBug) {
                this.discard();
                return false;
            }
        }

        if (this.isIceball == 0 && this.isAcid == 0) {
            if (hit instanceof Robot2
                    || hit instanceof Robot3
                    || hit instanceof Robot4
                    || hit instanceof Robot5
                    || hit instanceof GiantRobot) {
                this.discard();
                return false;
            }
        }

        if (hit instanceof Dragon dragon && this.isAcid == 0) {
            if (dragon.getFirstPassenger() != null) {
                this.discard();
                return false;
            }
            if (dragon.getDragonType() != 0 && this.isIceball != 0) {
                this.discard();
                return false;
            }
        }

        if (hit instanceof Player player && this.isAcid == 0) {
            if (player.getVehicle() != null) {
                this.discard();
                return false;
            }
        }

        float dmg = 16.0F;
        hit.hurt(this.damageSources().thrown(this, this.getOwner()), dmg);
        if (this.isIceball == 0) {
            hit.igniteForSeconds(1);
        }
        return true;
    }

    private void spawnImpactFx() {
        int mx = this.isSpecial != 0 ? 20 : 10;
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
    }

    @Override
    public void tick() {
        this.ticksAlive++;
        if (this.ticksAlive > 200) {
            this.discard();
            return;
        }

        super.tick();

        this.myRotation += 50.0F;
        while (this.myRotation > 360.0F) {
            this.myRotation -= 360.0F;
        }
        this.setXRot(this.myRotation);
        this.xRotO = this.myRotation;

        if (this.isAcid == 0) {
            int mx = 4;
            if (this.isSpecial != 0) {
                mx = 10;
            }
            if (this.isIceball != 0 && this.isSpecial == 0) {
                mx = 2;
            }
            for (int i = 0; i < mx; i++) {
                this.level().addParticle(
                        ParticleTypes.FIREWORK,
                        this.getX(),
                        this.getY(),
                        this.getZ(),
                        this.random.nextGaussian() / 2.0,
                        this.random.nextGaussian() / 2.0,
                        this.random.nextGaussian() / 2.0);
                if (this.isIceball == 0) {
                    // gold "reddust" trail
                    this.level().addParticle(
                            DustParticleOptions.REDSTONE,
                            this.getX(),
                            this.getY(),
                            this.getZ(),
                            this.random.nextGaussian() / 10.0,
                            this.random.nextGaussian() / 10.0,
                            this.random.nextGaussian() / 10.0);
                }
            }
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("Special", this.isSpecial);
        tag.putInt("Iceball", this.isIceball);
        tag.putInt("Acid", this.isAcid);
        tag.putInt("Irukandji", this.isIrukandji);
        tag.putInt("TicksAlive", this.ticksAlive);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.isSpecial = tag.getInt("Special");
        this.isIceball = tag.getInt("Iceball");
        this.isAcid = tag.getInt("Acid");
        this.isIrukandji = tag.getInt("Irukandji");
        this.ticksAlive = tag.getInt("TicksAlive");
    }
}
