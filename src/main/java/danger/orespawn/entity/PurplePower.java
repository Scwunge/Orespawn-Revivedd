package danger.orespawn.entity;

import danger.orespawn.OreSpawnMain;
import java.util.Comparator;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

/**
 * Gold {@code PurplePower} (EntityLiving). Size 0.75×0.75, speed 0.25, health 1000,
 * attack 500, armor 25, XP 35. Fire-immune no-clip flyer; purple_type 0–3 / 10 variants.
 * <p>
 * Registry: {@code purple_power}.
 */
public class PurplePower extends Monster {
    private static final EntityDataAccessor<Integer> DATA_PURPLE_TYPE =
            SynchedEntityData.defineId(PurplePower.class, EntityDataSerializers.INT);

    @Nullable
    private BlockPos currentFlightTarget;
    private int purpleType = 0;

    public PurplePower(EntityType<? extends PurplePower> type, Level level) {
        super(type, level);
        this.xpReward = 35; // gold field_70728_aV
        this.noPhysics = true; // gold field_70145_X
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 1000.0) // mygetMaxHealth()
                .add(Attributes.MOVEMENT_SPEED, 0.25)
                .add(Attributes.ATTACK_DAMAGE, 500.0)
                .add(Attributes.ARMOR, 25.0) // gold getTotalArmorValue 25
                .add(Attributes.FOLLOW_RANGE, 25.0); // gold field_70174_ab = 25
    }

    public int mygetMaxHealth() {
        return 1000;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_PURPLE_TYPE, 0);
    }

    public void setPurpleType(int type) {
        this.purpleType = type;
        if (!this.level().isClientSide) {
            this.entityData.set(DATA_PURPLE_TYPE, type);
        }
    }

    public int getPurpleType() {
        return this.entityData.get(DATA_PURPLE_TYPE);
    }

    @Override
    protected void registerGoals() {
        // gold: no AI goals — pure updateAITasks flight/combat
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        // gold canDespawn false
        return false;
    }

    @Override
    public boolean fireImmune() {
        return true; // gold field_70178_ae
    }

    @Override
    protected float getSoundVolume() {
        return 0.75F;
    }

    @Override
    public float getVoicePitch() {
        return 1.0F;
    }

    @Override
    protected SoundEvent getAmbientSound() {
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

    @Override
    public void tick() {
        super.tick();
        // gold: motionY *= 0.6
        Vec3 m = this.getDeltaMovement();
        this.setDeltaMovement(m.x, m.y * 0.6, m.z);

        if (this.level().isClientSide) {
            if (this.getPurpleType() == 0) {
                if (this.random.nextInt(4) == 1) {
                    this.level()
                            .addParticle(
                                    ParticleTypes.FIREWORK,
                                    this.getX(),
                                    this.getY() + 1.25,
                                    this.getZ(),
                                    (this.random.nextFloat() - this.random.nextFloat()) / 2.0F,
                                    (this.random.nextFloat() - this.random.nextFloat()) / 2.0F,
                                    (this.random.nextFloat() - this.random.nextFloat()) / 2.0F);
                }
            } else if (this.random.nextInt(6) == 1) {
                this.level()
                        .addParticle(
                                ParticleTypes.FIREWORK,
                                this.getX(),
                                this.getY() + 0.65F,
                                this.getZ(),
                                (this.random.nextFloat() - this.random.nextFloat()) / 5.0F,
                                (this.random.nextFloat() - this.random.nextFloat()) / 5.0F,
                                (this.random.nextFloat() - this.random.nextFloat()) / 5.0F);
            }
            this.purpleType = this.getPurpleType();
        } else {
            this.setPurpleType(this.purpleType);
        }

        // gold: 1/2500 server expire; type 10 explodes
        if (!this.level().isClientSide && this.random.nextInt(2500) == 1) {
            if (this.getPurpleType() == 10) {
                boolean grief = this.level().getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING);
                this.level()
                        .explode(
                                null,
                                this.getX(),
                                this.getY() + 0.25,
                                this.getZ(),
                                9.1F,
                                true,
                                grief ? Level.ExplosionInteraction.MOB : Level.ExplosionInteraction.NONE);
            }
            this.discard();
        }
    }

    private boolean canSeeTarget(double pX, double pY, double pZ) {
        return this.level()
                        .clip(new ClipContext(
                                new Vec3(this.getX(), this.getY() + 0.55, this.getZ()),
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

        // gold: repath if nextInt(300)==0 OR within 2.1 — else sometimes hunt
        if (this.random.nextInt(300) != 0
                && !(this.currentFlightTarget.distToCenterSqr(this.getX(), this.getY(), this.getZ()) < 2.1F)) {
            if (this.random.nextInt(7) == 2 && this.level().getDifficulty() != Difficulty.PEACEFUL) {
                LivingEntity e = this.findSomethingToAttack();
                if (e != null) {
                    this.currentFlightTarget =
                            BlockPos.containing(e.getX(), e.getY() + e.getBbHeight() / 2.0F, e.getZ());
                    float reach = 4.0F + e.getBbWidth() / 2.0F;
                    if (this.distanceToSqr(e) < reach * reach) {
                        this.doHurtTarget(e);
                        this.discard();
                    }
                }
            }
        } else {
            int keepTrying = 50;
            BlockState bid = Blocks.STONE.defaultBlockState();
            while (!bid.isAir() && keepTrying != 0) {
                keepTrying--;
                int zdir = this.random.nextInt(10) + 8;
                int xdir = this.random.nextInt(10) + 8;
                if (this.random.nextInt(2) == 0) {
                    zdir = -zdir;
                }
                if (this.random.nextInt(2) == 0) {
                    xdir = -xdir;
                }
                this.currentFlightTarget = BlockPos.containing(
                        this.getX() + xdir,
                        this.getY() + this.random.nextInt(20) - 10,
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

        if (this.level().getDifficulty() == Difficulty.PEACEFUL) {
            this.discard();
            return;
        }

        double var1 = this.currentFlightTarget.getX() + 0.5 - this.getX();
        double var3 = this.currentFlightTarget.getY() + 0.1 - this.getY();
        double var5 = this.currentFlightTarget.getZ() + 0.5 - this.getZ();
        Vec3 m = this.getDeltaMovement();
        this.setDeltaMovement(
                m.x + (Math.signum(var1) * 0.4 - m.x) * 0.2,
                m.y + (Math.signum(var3) * 0.7F - m.y) * 0.20000000149011612,
                m.z + (Math.signum(var5) * 0.4 - m.z) * 0.2);
        float var7 =
                (float) (Math.atan2(this.getDeltaMovement().z, this.getDeltaMovement().x) * 180.0 / Math.PI) - 90.0F;
        float var8 = Mth.wrapDegrees(var7 - this.getYRot());
        this.setZza(0.75F);
        this.setYRot(this.getYRot() + var8 / 4.0F);
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

    @Override
    public boolean hurt(DamageSource source, float amount) {
        Entity e = source.getEntity();
        if (e instanceof AbstractArrow) {
            return false;
        }
        float dm = amount;
        if (dm > 10.0F) {
            dm = 10.0F;
        }
        boolean ret = super.hurt(source, dm);
        if (e != null && this.currentFlightTarget != null) {
            this.currentFlightTarget =
                    BlockPos.containing(e.getX(), e.getY() + e.getBbHeight() / 2.0F, e.getZ());
        }
        return ret;
    }

    @Override
    public boolean checkSpawnRules(LevelAccessor level, MobSpawnType spawnType) {
        // gold getCanSpawnHere true
        return true;
    }

    private boolean isSuitableTarget(LivingEntity target) {
        if (this.level().getDifficulty() == Difficulty.PEACEFUL) {
            return false;
        }
        if (target == null || target == this || !target.isAlive()) {
            return false;
        }
        // gold MyUtils.isIgnoreable deferred
        if (!this.hasLineOfSight(target)) {
            return false;
        }
        if (target instanceof Player p) {
            if (p.getAbilities().instabuild) {
                return false;
            }
            // gold: players only if type <= 0 OR type == 10
            return this.getPurpleType() <= 0 || this.getPurpleType() == 10;
        }
        if (this.getPurpleType() != 0 && this.getPurpleType() != 10 && target instanceof TamableAnimal tame) {
            if (tame.isTame()) {
                return false;
            }
        }
        return !isRoyalty(target);
    }

    private static boolean isRoyalty(Entity e) {
        return e instanceof ThePrince
                || e instanceof ThePrinceTeen
                || e instanceof ThePrinceAdult
                || e instanceof ThePrincess
                || e instanceof TheKing
                || e instanceof KingHead
                || e instanceof TheQueen
                || e instanceof QueenHead
                || e instanceof PurplePower;
    }

    @Nullable
    private LivingEntity findSomethingToAttack() {
        if (OreSpawnMain.PlayNicely != 0) {
            return null;
        }
        List<LivingEntity> list = this.level()
                .getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(32.0, 24.0, 32.0));
        list.sort(Comparator.comparingDouble(this::distanceToSqr));
        for (LivingEntity living : list) {
            if (this.isSuitableTarget(living)) {
                return living;
            }
        }
        return null;
    }

    @Override
    public boolean doHurtTarget(Entity target) {
        if (!(target instanceof LivingEntity e)) {
            return false;
        }
        boolean var4;
        if (this.getPurpleType() != 0 && this.getPurpleType() != 10) {
            e.setHealth(e.getHealth() * 15.0F / 16.0F);
            var4 = e.hurt(this.damageSources().mobAttack(this), 5.0F);
            if (this.getPurpleType() == 1) {
                e.igniteForSeconds(10);
            }
            if (this.getPurpleType() == 2) {
                e.addEffect(new MobEffectInstance(MobEffects.POISON, 50, 0));
            }
            if (this.getPurpleType() == 3) {
                e.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 50, 0));
            }
        } else {
            e.setHealth(e.getHealth() / 4.0F - 1.0F);
            var4 = e.hurt(this.damageSources().mobAttack(this), e.getMaxHealth() / 8.0F);
            if (this.getPurpleType() == 10) {
                boolean grief = this.level().getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING);
                this.level()
                        .explode(
                                null,
                                e.getX(),
                                e.getY() - 0.25,
                                e.getZ(),
                                9.1F,
                                true,
                                grief ? Level.ExplosionInteraction.MOB : Level.ExplosionInteraction.NONE);
            }
        }
        return var4;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("PurpleType", this.purpleType);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.purpleType = tag.getInt("PurpleType");
        this.setPurpleType(this.purpleType);
    }
}
