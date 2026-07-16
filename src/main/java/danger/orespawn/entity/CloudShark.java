package danger.orespawn.entity;

import danger.orespawn.OreSpawnMain;
import danger.orespawn.util.handlers.SoundsHandler;
import java.util.Comparator;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
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
 * Gold {@code CloudShark} (EntityMob). Size 1.0×0.75, speed 0.3, health 15, attack 6, armor 5, XP 5
 * (CloudShark_stats defaults: health 15, attack 6, defense 5).
 * Sky flyer (y 120–140 band); hunts small flyers / GoldFish / players. Flight AI from gold updateAITasks.
 */
public class CloudShark extends Monster {
    @Nullable
    private BlockPos currentFlightTarget;

    public CloudShark(EntityType<? extends CloudShark> type, Level level) {
        super(type, level);
        this.xpReward = 5;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 15.0) // CloudShark_stats.health default
                .add(Attributes.MOVEMENT_SPEED, 0.3)
                .add(Attributes.ATTACK_DAMAGE, 6.0) // CloudShark_stats.attack default
                .add(Attributes.ARMOR, 5.0) // CloudShark_stats.defense default
                .add(Attributes.FOLLOW_RANGE, 5.0); // gold field_70174_ab = 5
    }

    public int mygetMaxHealth() {
        return 15;
    }

    @Override
    public boolean doHurtTarget(Entity target) {
        float f = (float) this.getAttributeValue(Attributes.ATTACK_DAMAGE);
        return target.hurt(this.damageSources().mobAttack(this), f);
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
        return 0.25F;
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
        // gold: orespawn:little_splat
        return SoundsHandler.LITTLE_SPLAT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        // gold: orespawn:big_splat
        return SoundsHandler.BIG_SPLAT.get();
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
                        this.getY() + this.random.nextInt(5) - 2 + updown,
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

        // gold: nextInt(9)==2 hunt
        if (this.random.nextInt(9) == 2) {
            LivingEntity e = this.findSomethingToAttack();
            if (e != null) {
                this.currentFlightTarget = BlockPos.containing(e.getX(), e.getY(), e.getZ());
                if (this.distanceToSqr(e) < 9.0) {
                    this.doHurtTarget(e);
                }
            }
        }

        double var1 = this.currentFlightTarget.getX() + 0.5 - this.getX();
        double var3 = this.currentFlightTarget.getY() + 0.1 - this.getY();
        double var5 = this.currentFlightTarget.getZ() + 0.5 - this.getZ();
        Vec3 m = this.getDeltaMovement();
        this.setDeltaMovement(
                m.x + (Math.signum(var1) * 0.5 - m.x) * 0.30000000149011613,
                m.y + (Math.signum(var3) * 0.7F - m.y) * 0.20000000149011612,
                m.z + (Math.signum(var5) * 0.5 - m.z) * 0.30000000149011613);
        float var7 =
                (float) (Math.atan2(this.getDeltaMovement().z, this.getDeltaMovement().x) * 180.0 / Math.PI) - 90.0F;
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
    public boolean isIgnoringBlockTriggers() {
        return false;
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        boolean ret = super.hurt(source, amount);
        Entity e = source.getEntity();
        if (e != null && this.currentFlightTarget != null) {
            this.currentFlightTarget = BlockPos.containing(e.getX(), e.getY(), e.getZ());
        }
        return ret;
    }

    @Override
    public boolean checkSpawnRules(LevelAccessor level, MobSpawnType spawnType) {
        // gold getCanSpawnHere: return true
        return true;
    }

    private boolean isSuitableTarget(LivingEntity target) {
        if (target == null || target == this || !target.isAlive()) {
            return false;
        }
        if (!this.hasLineOfSight(target)) {
            return false;
        }
        // gold: skip RockBase / EntityAnt (not in scope / ant friendly)
        if (target instanceof Ant
                || target instanceof RedAnt
                || target instanceof RainbowAnt
                || target instanceof UnstableAnt
                || target instanceof Termite) {
            return false;
        }
        if (target instanceof Butterfly) {
            return true;
        }
        // gold Cockateil → port Bird stand-in for small flyer prey
        if (target instanceof Bird) {
            return true;
        }
        if (target instanceof Mosquito) {
            return true;
        }
        if (target instanceof Firefly) {
            return true;
        }
        if (target instanceof Player p) {
            return !p.getAbilities().instabuild;
        }
        // gold: GoldFish or CliffRacer (CliffRacer not ported)
        return target instanceof GoldFish;
    }

    @Nullable
    private LivingEntity findSomethingToAttack() {
        // gold: PlayNicely != 0 → null
        if (OreSpawnMain.PlayNicely != 0) {
            return null;
        }
        List<LivingEntity> list = this.level().getEntitiesOfClass(
                LivingEntity.class, this.getBoundingBox().inflate(12.0, 10.0, 12.0));
        // gold GenericTargetSorter — nearest first
        list.sort(Comparator.comparingDouble(this::distanceToSqr));
        for (LivingEntity living : list) {
            if (this.isSuitableTarget(living)) {
                return living;
            }
        }
        return null;
    }

    @Override
    protected void dropCustomDeathLoot(ServerLevel level, DamageSource damageSource, boolean recentlyHit) {
        super.dropCustomDeathLoot(level, damageSource, recentlyHit);
        // gold: nextInt(3) → bone / string / gunpowder
        int i = this.random.nextInt(3);
        if (i == 0) {
            this.spawnAtLocation(new ItemStack(Items.BONE));
        } else if (i == 1) {
            this.spawnAtLocation(new ItemStack(Items.STRING));
        } else {
            this.spawnAtLocation(new ItemStack(Items.GUNPOWDER));
        }
    }
}
