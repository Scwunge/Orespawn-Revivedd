package danger.orespawn.entity;

import danger.orespawn.OreSpawnMain;
import danger.orespawn.util.ai.GoldStyleCombat;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
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
 * Gold {@code TerribleTerror} (EntityMob) 1:1 for NeoForge 1.21.1.
 * Size 1.0×0.75, speed 0.1, health 10, attack attr 3 / melee 5, armor 5, XP 10.
 * Flying night hunter (no AI goals — pure customServerAiStep flight).
 */
public class TerribleTerror extends Monster {
    @Nullable
    private BlockPos currentFlightTarget;

    public TerribleTerror(EntityType<? extends TerribleTerror> type, Level level) {
        super(type, level);
        this.xpReward = 10;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 10.0) // TerribleTerror_stats.health
                .add(Attributes.MOVEMENT_SPEED, 0.1)
                .add(Attributes.ATTACK_DAMAGE, 3.0) // TerribleTerror_stats.attack (melee uses 5)
                .add(Attributes.ARMOR, 5.0) // TerribleTerror_stats.defense
                .add(Attributes.FOLLOW_RANGE, 12.0); // gold field_70174_ab = 5, scan 12
    }

    @Override
    protected void registerGoals() {
        // gold: no AI goals — pure updateAITasks flight/combat
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        // gold canDespawn: if noDespawnRequired → false; else isDaytime
        if (this.isPersistenceRequired()) {
            return false;
        }
        return this.level().isDay();
    }

    public int mygetMaxHealth() {
        return 10;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        //  gold orespawn:terribleterror_living
        return null;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        //  gold orespawn:terribleterror_hit
        return null;
    }

    @Override
    protected SoundEvent getDeathSound() {
        //  gold orespawn:terribleterror_dead
        return null;
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
    public void tick() {
        super.tick();
        // gold: motionY *= 0.6 each tick
        Vec3 m = this.getDeltaMovement();
        this.setDeltaMovement(m.x, m.y * 0.6, m.z);
    }

    /**
     * Gold {@code attackEntityAsMob}: fixed 5.0F damage (not attribute 3).
     */
    @Override
    public boolean doHurtTarget(Entity target) {
        return target.hurt(this.damageSources().mobAttack(this), 5.0F);
    }

    /** Gold canSeeTarget — ray from y+0.75 to waypoint; MISS means clear. */
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
            this.currentFlightTarget = BlockPos.containing(this.getX(), this.getY(), this.getZ());
        }

        // gold: repath if nextInt(100)==0 OR within 2.1 of target; else 1/9 hunt
        if (this.random.nextInt(100) == 0
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
                        this.getY() + this.random.nextInt(5) - 2,
                        this.getZ() + zdir);
                bid = this.level().getBlockState(this.currentFlightTarget);
                // gold: air but cannot see → treat as solid, retry
                if (bid.isAir()
                        && !this.canSeeTarget(
                                this.currentFlightTarget.getX(),
                                this.currentFlightTarget.getY(),
                                this.currentFlightTarget.getZ())) {
                    bid = Blocks.STONE.defaultBlockState();
                }
            }
        } else if (this.random.nextInt(9) == 0) {
            LivingEntity e = this.findSomethingToAttack();
            if (e != null) {
                this.currentFlightTarget = BlockPos.containing(e.getX(), e.getY() + 1.0, e.getZ());
                if (this.distanceToSqr(e) < 6.0) {
                    this.doHurtTarget(e);
                }
            }
        }

        double var1 = this.currentFlightTarget.getX() + 0.4 - this.getX();
        double var3 = this.currentFlightTarget.getY() + 0.1 - this.getY();
        double var5 = this.currentFlightTarget.getZ() + 0.4 - this.getZ();
        Vec3 m = this.getDeltaMovement();
        // gold flight lerp formulas
        this.setDeltaMovement(
                m.x + (Math.signum(var1) * 0.4 - m.x) * 0.30000000149011613,
                m.y + (Math.signum(var3) * 0.7F - m.y) * 0.20000000149011612,
                m.z + (Math.signum(var5) * 0.4 - m.z) * 0.30000000149011613);
        float var7 = (float) (Math.atan2(this.getDeltaMovement().z, this.getDeltaMovement().x) * (180.0 / Math.PI))
                - 90.0F;
        float var8 = Mth.wrapDegrees(var7 - this.getYRot());
        this.setZza(0.75F);
        this.setYRot(this.getYRot() + var8 / 4.0F);
    }

    /** Gold {@code canTriggerWalking} true (default). */
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
        if (target == null || target == this || !target.isAlive()) {
            return false;
        }
        if (!this.hasLineOfSight(target)) {
            return false;
        }
        // gold: skip self-type + several unported flyers/allies
        if (target instanceof TerribleTerror) {
            return false;
        }
        if (target instanceof CreepingHorror) {
            return false;
        }
        if (target instanceof Mothra) {
            return false;
        }
        if (target instanceof Mantis) {
            return false;
        }
        if (target instanceof Butterfly) {
            return false;
        }
        if (target instanceof Firefly) {
            return false;
        }
        if (target instanceof Player player) {
            if (player.isSpectator() || player.getAbilities().instabuild) {
                return false;
            }
        }
        return true;
    }

    @Nullable
    private LivingEntity findSomethingToAttack() {
        // gold: PlayNicely != 0 → null; box expand(12, 8, 12)
        if (OreSpawnMain.PlayNicely != 0) {
            return null;
        }
        return GoldStyleCombat.findTarget(this, 12.0, 8.0, this::isSuitableTarget);
    }

    @Override
    protected void dropCustomDeathLoot(ServerLevel level, DamageSource damageSource, boolean recentlyHit) {
        super.dropCustomDeathLoot(level, damageSource, recentlyHit);
        // gold getDropItem: nextInt(3) → rotten_flesh / emerald / feather
        int i = this.random.nextInt(3);
        if (i == 0) {
            this.spawnAtLocation(new ItemStack(Items.ROTTEN_FLESH));
        } else if (i == 1) {
            this.spawnAtLocation(new ItemStack(Items.EMERALD));
        } else {
            this.spawnAtLocation(new ItemStack(Items.FEATHER));
        }
    }

    /**
     * Gold {@code getCanSpawnHere}: light + night; y ≤ 40 (spawner name check simplified away).
     */
    @Override
    public boolean checkSpawnRules(LevelAccessor level, MobSpawnType spawnType) {
        if (!super.checkSpawnRules(level, spawnType)) {
            return false;
        }
        if (level instanceof Level lvl && lvl.isDay()) {
            return false;
        }
        return !(this.getY() > 40.0);
    }
}
