package danger.orespawn.entity;

import danger.orespawn.OreSpawnMain;
import danger.orespawn.util.ai.GoldStyleCombat;
import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
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
 * Gold {@code LurkingTerror} (EntityMob) 1:1 for NeoForge 1.21.1.
 * Size 1.75×1.25, speed 0.25, health 30, attack attr 6 / melee 5, armor 5, XP 20.
 * Flying day hunter (no AI goals — pure customServerAiStep flight).
 */
public class LurkingTerror extends Monster {
    private static final EntityDataAccessor<Byte> DATA_ATTACKING =
            SynchedEntityData.defineId(LurkingTerror.class, EntityDataSerializers.BYTE);

    @Nullable
    private BlockPos currentFlightTarget;
    /** Gold client animation state for {@code ModelLurkingTerror}. */
    private RenderInfo renderdata = new RenderInfo();

    public LurkingTerror(EntityType<? extends LurkingTerror> type, Level level) {
        super(type, level);
        this.xpReward = 20;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 30.0) // LurkingTerror_stats.health default
                .add(Attributes.MOVEMENT_SPEED, 0.25)
                .add(Attributes.ATTACK_DAMAGE, 6.0) // LurkingTerror_stats.attack (melee uses 5)
                .add(Attributes.ARMOR, 5.0) // LurkingTerror_stats.defense
                .add(Attributes.FOLLOW_RANGE, 12.0); // gold field_70174_ab = 5, scan 12
    }

    public RenderInfo getRenderInfo() {
        if (this.renderdata == null) {
            this.renderdata = new RenderInfo();
        }
        return this.renderdata;
    }

    /** Gold {@code setRenderInfo} — copy fields (model mutates then writes back). */
    public void setRenderInfo(RenderInfo r) {
        if (r == null) {
            return;
        }
        if (this.renderdata == null) {
            this.renderdata = new RenderInfo();
        }
        this.renderdata.rf1 = r.rf1;
        this.renderdata.rf2 = r.rf2;
        this.renderdata.rf3 = r.rf3;
        this.renderdata.rf4 = r.rf4;
        this.renderdata.ri1 = r.ri1;
        this.renderdata.ri2 = r.ri2;
        this.renderdata.ri3 = r.ri3;
        this.renderdata.ri4 = r.ri4;
    }

    public int getAttacking() {
        return this.entityData.get(DATA_ATTACKING);
    }

    public void setAttacking(int value) {
        this.entityData.set(DATA_ATTACKING, (byte) value);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_ATTACKING, (byte) 0);
        if (this.renderdata == null) {
            this.renderdata = new RenderInfo();
        }
        this.renderdata.rf1 = 0.0F;
        this.renderdata.rf2 = 0.0F;
        this.renderdata.rf3 = 0.0F;
        this.renderdata.rf4 = 0.0F;
        this.renderdata.ri1 = 0;
        this.renderdata.ri2 = 0;
        this.renderdata.ri3 = 0;
        this.renderdata.ri4 = 0;
    }

    @Override
    protected void registerGoals() {
        // gold: no AI goals — pure updateAITasks flight/combat
    }

    /**
     * Gold {@code canDespawn}: if noDespawnRequired → false; else only when not attacking.
     */
    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        if (this.isPersistenceRequired()) {
            return false;
        }
        return this.getAttacking() == 0;
    }

    public int mygetMaxHealth() {
        return 30;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        //  gold orespawn:lurkinghorror_living
        return null;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        //  gold orespawn:lurkinghorror_hit
        return null;
    }

    @Override
    protected SoundEvent getDeathSound() {
        //  gold orespawn:lurkinghorror_dead
        return null;
    }

    @Override
    protected float getSoundVolume() {
        return 0.55F;
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
     * Gold {@code attackEntityAsMob}: fixed 5.0F damage (not attribute 6).
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

        // gold: repath if nextInt(120)==0 OR within 2.1 of target; else 1/9 hunt
        if (this.random.nextInt(120) == 0
                || this.currentFlightTarget.distToCenterSqr(this.getX(), this.getY(), this.getZ()) < 2.1F) {
            int keepTrying = 50;
            BlockState bid = Blocks.STONE.defaultBlockState();
            while (!bid.isAir() && keepTrying != 0) {
                keepTrying--;
                // gold: nextInt(10)+2 (wider wander than TerribleTerror's 5+5)
                int zdir = this.random.nextInt(10) + 2;
                int xdir = this.random.nextInt(10) + 2;
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
                this.setAttacking(1);
                this.currentFlightTarget = BlockPos.containing(e.getX(), e.getY() + 1.0, e.getZ());
                if (this.distanceToSqr(e) < 6.0) {
                    this.doHurtTarget(e);
                }
            } else {
                this.setAttacking(0);
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
        // gold: skip self-type + nightmare allies / unported flyers when present
        if (target instanceof LurkingTerror) {
            return false;
        }
        if (target instanceof TerribleTerror) {
            return false;
        }
        if (target instanceof CreepingHorror) {
            return false;
        }
        if (target instanceof Mothra) {
            return false;
        }
        if (target instanceof CloudShark) {
            return false;
        }
        if (target instanceof Bee) {
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
        // gold also skipped RockBase, EnderReaper, LeafMonster, Rotator, Triffid,
        // PitchBlack, Dragon, Island, IslandToo — unported types omitted
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
        // gold getDropItem: nextInt(3) → beef / flint / feather
        int i = this.random.nextInt(3);
        if (i == 0) {
            this.spawnAtLocation(new ItemStack(Items.BEEF));
        } else if (i == 1) {
            this.spawnAtLocation(new ItemStack(Items.FLINT));
        } else {
            this.spawnAtLocation(new ItemStack(Items.FEATHER));
        }
    }

    /**
     * Gold {@code getCanSpawnHere}: light + daytime; y ≥ 10; 50% random; no peer in 32×16×32.
     * Spawner-name + DimensionID6 checks simplified away.
     */
    @Override
    public boolean checkSpawnRules(LevelAccessor level, MobSpawnType spawnType) {
        if (!super.checkSpawnRules(level, spawnType)) {
            return false;
        }
        // gold requires isDaytime
        if (level instanceof Level lvl && !lvl.isDay()) {
            return false;
        }
        if (this.random.nextInt(2) != 1) {
            return false;
        }
        // gold: nearest other LurkingTerror in expand(32,16,32) blocks spawn
        if (!level
                .getEntitiesOfClass(
                        LurkingTerror.class, this.getBoundingBox().inflate(32.0, 16.0, 32.0), e -> e != this)
                .isEmpty()) {
            return false;
        }
        return !(this.getY() < 10.0);
    }
}
