package danger.orespawn.entity;

import danger.orespawn.OreSpawnMain;
import danger.orespawn.init.ModItems;
import danger.orespawn.util.ai.GoldStyleCombat;
import danger.orespawn.util.ai.WanderALotGoal;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

/**
 * Gold {@code PitchBlack} / Nightmare (EntityMob) 1:1 for NeoForge 1.21.1.
 * Variable scale 0.5/1/2/3/4; size 2.5×3.5 × scale; health 250×scale; attack 10×scale;
 * armor 30+2×scale; speed 0.2 + 0.1×scale; XP 100×scale. Ground + flight activity modes.
 */
public class PitchBlack extends Monster {
    private static final EntityDataAccessor<Byte> DATA_ATTACKING =
            SynchedEntityData.defineId(PitchBlack.class, EntityDataSerializers.BYTE);
    private static final EntityDataAccessor<Byte> DATA_ACTIVITY =
            SynchedEntityData.defineId(PitchBlack.class, EntityDataSerializers.BYTE);
    /** Gold datawatcher 22: scale × 10 as int. */
    private static final EntityDataAccessor<Integer> DATA_SCALE =
            SynchedEntityData.defineId(PitchBlack.class, EntityDataSerializers.INT);

    /** Gold PitchBlack_stats defaults: health 250, defense 30, attack 10. */
    public static final int STAT_HEALTH = 250;
    public static final int STAT_DEFENSE = 30;
    public static final int STAT_ATTACK = 10;

    private float myMoveSpeed = 0.2F;
    private int damageTicker = 0;
    private int wingSound = 0;
    @Nullable
    private BlockPos currentFlightTarget;
    private RenderInfo renderdata = new RenderInfo();
    private boolean scaleInitialized = false;

    public PitchBlack(EntityType<? extends PitchBlack> type, Level level) {
        super(type, level);
        this.xpReward = 200;
        // Large winged mesh — reduce frustum pop when scale > 1
        this.noCulling = true;
    }

    public static AttributeSupplier.Builder createAttributes() {
        // Base at scale 1.0; tick/scale apply multipliers.
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, STAT_HEALTH)
                .add(Attributes.MOVEMENT_SPEED, 0.3) // MyMoveSpeed 0.2 + 0.1 * scale1
                .add(Attributes.ATTACK_DAMAGE, STAT_ATTACK)
                .add(Attributes.ARMOR, STAT_DEFENSE + 2)
                .add(Attributes.FOLLOW_RANGE, 25.0);
    }

    @Override
    protected void registerGoals() {
        // gold: Swimming, MoveThroughVillage(skipped), WanderALot(16,1), WatchClosest, LookIdle
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(2, new WanderALotGoal(this, 16, 1.0));
        this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 10.0F));
        this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_ATTACKING, (byte) 0);
        builder.define(DATA_ACTIVITY, (byte) 0);
        builder.define(DATA_SCALE, 5); // default 0.5F
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
    public void onSyncedDataUpdated(EntityDataAccessor<?> key) {
        if (DATA_SCALE.equals(key)) {
            this.refreshDimensions();
            this.applyScaleStats();
        }
        super.onSyncedDataUpdated(key);
    }

    @Override
    public EntityDimensions getDefaultDimensions(Pose pose) {
        float s = this.getPitchBlackScale();
        // gold tick / entityInit: setSize(2.5 * scale, 3.5 * scale)
        return EntityDimensions.scalable(2.5F * s, 3.5F * s);
    }

    public RenderInfo getRenderInfo() {
        if (this.renderdata == null) {
            this.renderdata = new RenderInfo();
        }
        return this.renderdata;
    }

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

    public final int getAttacking() {
        return this.entityData.get(DATA_ATTACKING);
    }

    public final void setAttacking(int value) {
        this.entityData.set(DATA_ATTACKING, (byte) value);
    }

    public final int getActivity() {
        return this.entityData.get(DATA_ACTIVITY);
    }

    public final void setActivity(int value) {
        this.entityData.set(DATA_ACTIVITY, (byte) value);
    }

    /** Gold getPitchBlackScale: datawatcher int / 10. */
    public float getPitchBlackScale() {
        int i = this.entityData.get(DATA_SCALE);
        if (i <= 0) {
            return 0.5F;
        }
        return i / 10.0F;
    }

    public void setPitchBlackScale(float scale) {
        float f = scale * 10.0001F;
        int i = (int) f;
        if (i < 1) {
            i = 1;
        }
        this.entityData.set(DATA_SCALE, i);
        this.xpReward = (int) (100.0F * scale);
        this.applyScaleStats();
        this.refreshDimensions();
    }

    private void applyScaleStats() {
        float s = this.getPitchBlackScale();
        if (this.getAttribute(Attributes.MAX_HEALTH) != null) {
            double max = STAT_HEALTH * s;
            this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(max);
            if (this.getHealth() > max) {
                this.setHealth((float) max);
            }
        }
        if (this.getAttribute(Attributes.ATTACK_DAMAGE) != null) {
            this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(STAT_ATTACK * s);
        }
        if (this.getAttribute(Attributes.ARMOR) != null) {
            // gold getTotalArmorValue: defense + (int)(2 * scale)
            this.getAttribute(Attributes.ARMOR).setBaseValue(STAT_DEFENSE + (int) (2.0F * s));
        }
        if (this.getAttribute(Attributes.FOLLOW_RANGE) != null) {
            this.getAttribute(Attributes.FOLLOW_RANGE).setBaseValue(25.0F * s);
        }
        if (this.getAttribute(Attributes.MOVEMENT_SPEED) != null) {
            this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(this.myMoveSpeed + 0.1F * s);
        }
    }

    private void initRandomScaleIfNeeded() {
        if (this.scaleInitialized || this.level().isClientSide) {
            return;
        }
        this.scaleInitialized = true;
        float t = 0.5F;
        if (this.random.nextInt(4) == 1) {
            t = 1.0F;
        }
        if (this.random.nextInt(8) == 2) {
            t = 2.0F;
        }
        if (this.random.nextInt(32) == 3) {
            t = 3.0F;
        }
        if (this.random.nextInt(64) == 4) {
            t = 4.0F;
        }
        // gold NightmareSize config 1.5 forces scale; not wired this wave (default 0 = random)
        this.setPitchBlackScale(t);
        if (this.getHealth() < this.getMaxHealth()) {
            this.setHealth(this.getMaxHealth());
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putFloat("Fscale", this.getPitchBlackScale());
        tag.putBoolean("ScaleInit", this.scaleInitialized);
        tag.putByte("Activity", (byte) this.getActivity());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.contains("Fscale")) {
            this.scaleInitialized = true;
            this.setPitchBlackScale(tag.getFloat("Fscale"));
        } else {
            this.scaleInitialized = tag.getBoolean("ScaleInit");
        }
        if (tag.contains("Activity")) {
            this.setActivity(tag.getByte("Activity"));
        }
    }

    /**
     * Gold canDespawn: if noDespawnRequired → false; else only when daytime.
     */
    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        if (this.isPersistenceRequired()) {
            return false;
        }
        return this.level().isDay();
    }

    public int mygetMaxHealth() {
        return (int) (STAT_HEALTH * this.getPitchBlackScale());
    }

    @Override
    protected SoundEvent getAmbientSound() {
        // gold: only 1/5 chance pitchblack_living
        if (this.random.nextInt(5) != 2) {
            return null;
        }
        return null;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        //  gold orespawn:pitchblack_hit
        return null;
    }

    @Override
    protected SoundEvent getDeathSound() {
        //  gold orespawn:pitchblack_dead
        return null;
    }

    @Override
    protected float getSoundVolume() {
        return 0.75F;
    }

    @Override
    public float getVoicePitch() {
        // gold: 1.0F - 0.7F * (4.0F / scale)
        float s = Math.max(0.01F, this.getPitchBlackScale());
        return 1.0F - 0.7F * (4.0F / s);
    }

    @Override
    public void tick() {
        this.initRandomScaleIfNeeded();
        this.myMoveSpeed = 0.2F;
        if (this.getAttribute(Attributes.MOVEMENT_SPEED) != null) {
            this.getAttribute(Attributes.MOVEMENT_SPEED)
                    .setBaseValue(this.myMoveSpeed + 0.1F * this.getPitchBlackScale());
        }
        super.tick();
        this.refreshDimensions();

        this.wingSound++;
        if (this.wingSound > 20) {
            if (!this.level().isClientSide) {
                // gold orespawn:MothraWings
                this.playSound(net.minecraft.sounds.SoundEvents.ENDER_DRAGON_FLAP, 1.0F, 1.0F);
            }
            this.wingSound = 0;
        }

        // gold: motionY *= 0.6 each tick
        Vec3 m = this.getDeltaMovement();
        this.setDeltaMovement(m.x, m.y * 0.6, m.z);

        if (!this.level().isClientSide && this.random.nextInt(250) == 1) {
            this.heal(1.0F + this.getPitchBlackScale());
            if (this.random.nextInt(5) == 0) {
                BlockState bid = Blocks.AIR.defaultBlockState();
                if (this.getY() > 10.0) {
                    for (int i = 0; i < 10; i++) {
                        bid = this.level()
                                .getBlockState(BlockPos.containing(this.getX(), this.getY() - i, this.getZ()));
                        if (!bid.isAir()) {
                            break;
                        }
                    }
                } else {
                    bid = Blocks.STONE.defaultBlockState();
                }
                if (!bid.isAir()) {
                    LivingEntity e = this.findSomethingToAttack();
                    if (e == null) {
                        this.setActivity(0);
                    }
                }
            } else {
                this.setActivity(1);
                this.getNavigation().stop();
            }
        }

        if (this.getActivity() == 0 && this.random.nextInt(10) == 1) {
            LivingEntity e = this.findSomethingToAttack();
            if (e != null) {
                this.setActivity(1);
                this.getNavigation().stop();
            }
        }
    }

    @Override
    public boolean doHurtTarget(Entity target) {
        float dmg = STAT_ATTACK * this.getPitchBlackScale();
        boolean var4;
        if (target instanceof EnderDragon) {
            // gold damages dragon parts; 1.21 uses body hurt
            var4 = target.hurt(this.damageSources().mobAttack(this), dmg);
        } else {
            var4 = target.hurt(this.damageSources().mobAttack(this), dmg);
            if (var4 && target instanceof LivingEntity) {
                double ks = 1.15 * this.getPitchBlackScale();
                double inair = 0.08 * this.getPitchBlackScale();
                float f3 = (float) Math.atan2(target.getZ() - this.getZ(), target.getX() - this.getX());
                if (!target.isAlive() || target instanceof Player) {
                    inair *= 2.0;
                }
                target.push(Math.cos(f3) * ks, inair, Math.sin(f3) * ks);
            }
        }
        return var4;
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
        if (this.damageTicker > 0) {
            this.damageTicker--;
        }

        // gold: activity 0 → super ground AI; activity != 0 → flight
        if (this.getActivity() == 0) {
            super.customServerAiStep();
            return;
        }

        if (this.isDeadOrDying()) {
            return;
        }

        if (this.currentFlightTarget == null) {
            this.currentFlightTarget = BlockPos.containing(this.getX(), this.getY(), this.getZ());
        }

        // gold: repath if nextInt(150)==0 OR within 2.1; else 1/8 hunt
        if (this.random.nextInt(150) == 0
                || this.currentFlightTarget.distToCenterSqr(this.getX(), this.getY(), this.getZ()) < 2.1F) {
            int keepTrying = 50;
            BlockState bid = Blocks.STONE.defaultBlockState();
            while (!bid.isAir() && keepTrying > 0) {
                keepTrying--;
                int zdir = this.random.nextInt(20) + 5 * (int) this.getPitchBlackScale();
                int xdir = this.random.nextInt(20) + 5 * (int) this.getPitchBlackScale();
                if (this.random.nextInt(2) == 0) {
                    zdir = -zdir;
                }
                if (this.random.nextInt(2) == 0) {
                    xdir = -xdir;
                }
                this.currentFlightTarget = BlockPos.containing(
                        this.getX() + xdir,
                        this.getY() + this.random.nextInt(11) - 5,
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
        } else if (this.random.nextInt(8) == 0) {
            LivingEntity e = this.findSomethingToAttack();
            if (e != null) {
                double d1 = 5.0 + e.getBbWidth() / 2.0;
                d1 += this.getPitchBlackScale();
                d1 *= d1;
                this.setAttacking(1);
                // gold: EnderDragon / Godzilla / GodzillaHead min d1=100
                if (e instanceof EnderDragon && d1 < 100.0) {
                    d1 = 100.0;
                }
                this.currentFlightTarget = BlockPos.containing(e.getX(), e.getY() + 2.0, e.getZ());
                if (this.distanceToSqr(e) < d1) {
                    this.doHurtTarget(e);
                }
            } else {
                this.setAttacking(0);
            }
        }

        double var1 = this.currentFlightTarget.getX() + 0.4 - this.getX();
        double var3 = this.currentFlightTarget.getY() + 0.1 - this.getY();
        double var5 = this.currentFlightTarget.getZ() + 0.4 - this.getZ();
        double myspeed = 0.5F + this.getPitchBlackScale() / 10.0F;
        Vec3 m = this.getDeltaMovement();
        this.setDeltaMovement(
                m.x + (Math.signum(var1) * myspeed - m.x) * 0.33,
                m.y + (Math.signum(var3) * 0.7F - m.y) * 0.20000000149011612,
                m.z + (Math.signum(var5) * myspeed - m.z) * 0.33);
        float var7 = (float) (Math.atan2(this.getDeltaMovement().z, this.getDeltaMovement().x) * (180.0 / Math.PI))
                - 90.0F;
        float var8 = Mth.wrapDegrees(var7 - this.getYRot());
        this.setZza(0.1F + (float) myspeed);
        this.setYRot(this.getYRot() + var8 / 5.0F);
    }

    @Override
    public boolean causeFallDamage(float fallDistance, float multiplier, DamageSource source) {
        return false;
    }

    @Override
    protected void checkFallDamage(double y, boolean onGround, BlockState state, BlockPos pos) {}

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (this.damageTicker > 0) {
            return false;
        }
        this.damageTicker = 20;
        boolean ret = super.hurt(source, amount);
        Entity e = source.getEntity();
        if (e != null) {
            this.currentFlightTarget = BlockPos.containing(e.getX(), e.getY() + 2.0, e.getZ());
        }
        this.setActivity(1);
        this.getNavigation().stop();
        return ret;
    }

    private boolean isSuitableTarget(LivingEntity target) {
        if (target == null || target == this || !target.isAlive()) {
            return false;
        }
        if (!this.hasLineOfSight(target)) {
            return false;
        }
        // gold MyUtils.isIgnoreable deferred
        if (target instanceof PitchBlack) {
            return false;
        }
        if (target instanceof EnderReaper) {
            return false;
        }
        if (target instanceof LeafMonster) {
            return false;
        }
        if (target instanceof TerribleTerror) {
            return false;
        }
        if (target instanceof LurkingTerror) {
            return false;
        }
        if (target instanceof CreepingHorror) {
            return false;
        }
        // gold Island, IslandToo, Triffid — unported
        if (target instanceof Player player) {
            if (player.isSpectator() || player.getAbilities().instabuild) {
                return false;
            }
        }
        return true;
    }

    @Nullable
    private LivingEntity findSomethingToAttack() {
        if (OreSpawnMain.PlayNicely != 0) {
            return null;
        }
        double d1 = 16.0 + this.getPitchBlackScale() * 6.0F;
        double d2 = 10.0 + this.getPitchBlackScale() * 4.0F;
        return GoldStyleCombat.findTarget(this, d1, d2, this::isSuitableTarget);
    }

    @Override
    protected void dropCustomDeathLoot(ServerLevel level, DamageSource damageSource, boolean recentlyHit) {
        super.dropCustomDeathLoot(level, damageSource, recentlyHit);
        float s = this.getPitchBlackScale();
        int i = 3 + this.random.nextInt(2 + (int) (5.0F * s));
        for (int var4 = 0; var4 < i; var4++) {
            this.spawnAtLocation(new ItemStack(Items.ROTTEN_FLESH));
            int j = this.random.nextInt(10);
            if (j == 0) {
                this.spawnAtLocation(new ItemStack(Items.FLINT));
            }
            if (j == 1) {
                this.spawnAtLocation(new ItemStack(Items.STRING));
            }
            if (j == 2) {
                this.spawnAtLocation(new ItemStack(Items.FEATHER));
            }
            if (j == 3) {
                this.spawnAtLocation(new ItemStack(Items.BEEF));
            }
        }
        // gold MyNightmareScale
        this.spawnAtLocation(new ItemStack(ModItems.NIGHTMARE_SCALE.get()));
        this.spawnAtLocation(new ItemStack(Items.QUARTZ));
        // gold ZooKeeper × (2+scale+rand)
        i = 2 + (int) s + this.random.nextInt(2 + (int) (5.0F * s));
        for (int var7 = 0; var7 < i; var7++) {
            this.spawnAtLocation(new ItemStack(ModItems.ZOO_KEEPER.get()));
        }
    }

    /**
     * Gold getCanSpawnHere: night + light; spawner "Nightmare" cap scale; DimensionID6 spacing;
     * large scales need air volume.
     */
    @Override
    public boolean checkSpawnRules(LevelAccessor level, MobSpawnType spawnType) {
        // gold spawner name "Nightmare" nearby → cap scale ≤ 1
        for (int k = -3; k < 3; k++) {
            for (int j = -3; j < 3; j++) {
                for (int i = 0; i < 5; i++) {
                    BlockPos pos = BlockPos.containing(this.getX() + j, this.getY() + i, this.getZ() + k);
                    BlockState st = level.getBlockState(pos);
                    if (st.is(Blocks.SPAWNER)) {
                        BlockEntity be = level.getBlockEntity(pos);
                        if (be instanceof SpawnerBlockEntity) {
                            float t = this.getPitchBlackScale();
                            if (t > 1.0F) {
                                t = 1.0F;
                            }
                            this.setPitchBlackScale(t);
                            this.scaleInitialized = true;
                            return true;
                        }
                    }
                }
            }
        }

        if (!super.checkSpawnRules(level, spawnType)) {
            return false;
        }
        // gold requires !isDaytime
        if (level instanceof Level lvl && lvl.isDay()) {
            return false;
        }

        // gold DimensionID6 peer spacing — simplified to any dimension
        List<PitchBlack> peers = level.getEntitiesOfClass(
                PitchBlack.class, this.getBoundingBox().inflate(16.0, 16.0, 16.0), e -> e != this);
        if (!peers.isEmpty()) {
            return false;
        }

        if (this.getPitchBlackScale() < 1.1F) {
            return true;
        }

        int ix = 1;
        if (this.getPitchBlackScale() > 3.1F) {
            ix = 2;
        }
        int iy = ix * 3;
        for (int var13 = -ix; var13 <= ix; var13++) {
            for (int j = -ix; j <= ix; j++) {
                for (int i = 1; i <= iy; i++) {
                    BlockState bid = level.getBlockState(
                            BlockPos.containing(this.getX() + j, this.getY() + i, this.getZ() + var13));
                    if (!bid.isAir()) {
                        return false;
                    }
                }
            }
        }
        return true;
    }
}
