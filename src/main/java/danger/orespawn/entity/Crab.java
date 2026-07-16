package danger.orespawn.entity;

import danger.orespawn.OreSpawnMain;
import danger.orespawn.util.ai.GoldStyleCombat;
import danger.orespawn.util.ai.WanderALotGoal;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
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
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

/**
 * Gold {@code Crab} (EntityMob) 1:1 for NeoForge 1.21.1.
 * Registry name {@code crab}; texture {@code robotcrabtexture.png}.
 * Variable scale (0.25 / 0.5 / 1.0); health from gold PitchBlack_stats (250) × scale;
 * attack Crab_stats (24)×scale; defense Crab_stats (16)+2×scale; XP 400×scale.
 * Water-seeker; proximity combat with knockback.
 */
public class Crab extends Monster {
    private static final EntityDataAccessor<Byte> ATTACKING =
            SynchedEntityData.defineId(Crab.class, EntityDataSerializers.BYTE);
    /** Gold datawatcher 21: scale × 100 as int. */
    private static final EntityDataAccessor<Integer> CRAB_SCALE =
            SynchedEntityData.defineId(Crab.class, EntityDataSerializers.INT);

    /** Gold Crab_stats defaults. */
    private static final int CRAB_ATTACK = 24;
    private static final int CRAB_DEFENSE = 16;
    /** Gold mygetMaxHealth uses PitchBlack_stats.health (default 250). */
    private static final int PITCH_BLACK_HEALTH = 250;

    private float moveSpeed = 0.55F;
    private int hurtTimer = 0;
    private int closest = 99999;
    private int tx = 0;
    private int ty = 0;
    private int tz = 0;
    private boolean scaleInitialized = false;

    public Crab(EntityType<? extends Crab> type, Level level) {
        super(type, level);
        this.xpReward = 150;
    }

    public static AttributeSupplier.Builder createAttributes() {
        // Base at scale 1.0; tick/scale apply multipliers.
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, PITCH_BLACK_HEALTH)
                .add(Attributes.MOVEMENT_SPEED, 0.55)
                .add(Attributes.ATTACK_DAMAGE, CRAB_ATTACK)
                .add(Attributes.ARMOR, CRAB_DEFENSE)
                .add(Attributes.FOLLOW_RANGE, 30.0); // gold field_70174_ab base 30, then 10*scale
    }

    @Override
    protected void registerGoals() {
        // gold: Swimming, WanderALot(16,1), WatchClosest(Player,10), WatchClosest(Living,8), LookIdle, HurtByTarget
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new WanderALotGoal(this, 16, 1.0));
        this.goalSelector.addGoal(2, new LookAtPlayerGoal(this, Player.class, 10.0F));
        this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, LivingEntity.class, 8.0F));
        this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(ATTACKING, (byte) 0);
        builder.define(CRAB_SCALE, 25); // default 0.25F
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> key) {
        if (CRAB_SCALE.equals(key)) {
            this.refreshDimensions();
            this.applyScaleStats();
        }
        super.onSyncedDataUpdated(key);
    }

    @Override
    public EntityDimensions getDefaultDimensions(Pose pose) {
        float s = this.getCrabScale();
        // gold tick: setSize(2.5 * scale, 3.5 * scale)
        return EntityDimensions.scalable(2.5F * s, 3.5F * s);
    }

    /** Gold getCrabScale: datawatcher int / 100. */
    public float getCrabScale() {
        int i = this.entityData.get(CRAB_SCALE);
        if (i <= 0) {
            return 0.25F;
        }
        return i / 100.0F;
    }

    public void setCrabScale(float scale) {
        float f = scale * 100.0F;
        int i = (int) f;
        if (i < 1) {
            i = 1;
        }
        this.entityData.set(CRAB_SCALE, i);
        this.xpReward = (int) (400.0F * scale);
        this.applyScaleStats();
        this.refreshDimensions();
    }

    private void applyScaleStats() {
        float s = this.getCrabScale();
        if (this.getAttribute(Attributes.MAX_HEALTH) != null) {
            double max = PITCH_BLACK_HEALTH * s;
            this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(max);
            if (this.getHealth() > max) {
                this.setHealth((float) max);
            }
        }
        if (this.getAttribute(Attributes.ATTACK_DAMAGE) != null) {
            this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(CRAB_ATTACK * s);
        }
        if (this.getAttribute(Attributes.ARMOR) != null) {
            this.getAttribute(Attributes.ARMOR).setBaseValue(CRAB_DEFENSE + (int) (2.0F * s));
        }
        if (this.getAttribute(Attributes.FOLLOW_RANGE) != null) {
            this.getAttribute(Attributes.FOLLOW_RANGE).setBaseValue(10.0F * s);
        }
    }

    private void initRandomScaleIfNeeded() {
        if (this.scaleInitialized || this.level().isClientSide) {
            return;
        }
        this.scaleInitialized = true;
        float t = 0.25F;
        if (this.random.nextInt(4) == 1) {
            t = 0.5F;
        }
        if (this.random.nextInt(8) == 2) {
            t = 1.0F;
        }
        this.setCrabScale(t);
        if (this.getHealth() < this.getMaxHealth()) {
            this.setHealth(this.getMaxHealth());
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putFloat("Fscale", this.getCrabScale());
        tag.putBoolean("ScaleInit", this.scaleInitialized);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.contains("Fscale")) {
            this.scaleInitialized = true;
            this.setCrabScale(tag.getFloat("Fscale"));
        } else {
            this.scaleInitialized = tag.getBoolean("ScaleInit");
        }
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return !this.isPersistenceRequired();
    }

    // gold canBreatheUnderwater true — LivingEntity.canBreatheUnderwater is final in 1.21; air forced in tick if needed


    @Override
    public void tick() {
        this.initRandomScaleIfNeeded();
        // gold: water speed 0.95 else 0.55, then * scale
        if (this.isInWater()) {
            this.moveSpeed = 0.95F;
        } else {
            this.moveSpeed = 0.55F;
        }
        if (this.getAttribute(Attributes.MOVEMENT_SPEED) != null) {
            this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(this.moveSpeed * this.getCrabScale());
        }
        super.tick();
    }

    public int mygetMaxHealth() {
        return (int) (PITCH_BLACK_HEALTH * this.getCrabScale());
    }

    public int getCrabHealth() {
        return (int) this.getHealth();
    }

    public int getAttacking() {
        return this.entityData.get(ATTACKING);
    }

    public void setAttacking(int value) {
        this.entityData.set(ATTACKING, (byte) value);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        //  gold null
        return null;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        //  gold orespawn:leaves_hit — via SoundsHandler when registered
        return null;
    }

    @Override
    protected SoundEvent getDeathSound() {
        //  gold null
        return null;
    }

    @Override
    protected float getSoundVolume() {
        return 0.75F;
    }

    @Override
    public float getVoicePitch() {
        // gold: 2.0F - 0.3F * (1.0F / scale)
        return 2.0F - 0.3F * (1.0F / Math.max(0.01F, this.getCrabScale()));
    }

    @Override
    protected void dropCustomDeathLoot(ServerLevel level, DamageSource damageSource, boolean recentlyHit) {
        super.dropCustomDeathLoot(level, damageSource, recentlyHit);
        // gold: (4+rand8)*scale raw crab meat — item not registered this wave; drop cod as stand-in food
        int n = 4 + this.random.nextInt(8);
        n = (int) (n * this.getCrabScale());
        if (n < 1) {
            n = 1;
        }
        for (int i = 0; i < n; i++) {
            // gold OreSpawnMain.MyRawCrabMeat — deferred until ModItems.RAW_CRAB_MEAT
            this.spawnAtLocation(new ItemStack(Items.COD));
        }
    }

    @Override
    public boolean doHurtTarget(Entity target) {
        // gold: fixed Crab_stats.attack * scale (attribute already scaled)
        float dmg = CRAB_ATTACK * this.getCrabScale();
        boolean hit = target.hurt(this.damageSources().mobAttack(this), dmg);
        if (hit && target instanceof LivingEntity) {
            double ks = 1.15 * this.getCrabScale();
            double inair = 0.48 * this.getCrabScale();
            float f3 = (float) Math.atan2(target.getZ() - this.getZ(), target.getX() - this.getX());
            if (!target.isAlive() || target instanceof Player) {
                inair *= 2.0;
            }
            target.push(Math.cos(f3) * ks, inair, Math.sin(f3) * ks);
        }
        return hit;
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if ("cactus".equals(source.getMsgId())) {
            return false;
        }
        boolean ret = false;
        if (this.hurtTimer <= 0) {
            ret = super.hurt(source, amount);
            this.hurtTimer = 8;
        }
        Entity e = source.getEntity();
        if (e instanceof LivingEntity living) {
            if (living instanceof Crab) {
                return false;
            }
            this.setTarget(living);
            this.getNavigation().moveTo(living, 1.2);
        }
        return ret;
    }

    /** Gold {@code scan_it} for water / flowing water. */
    private boolean scanIt(int x, int y, int z, int dx, int dy, int dz) {
        int found = 0;

        for (int i = -dy; i <= dy; i++) {
            for (int j = -dz; j <= dz; j++) {
                if (isWater(new BlockPos(x + dx, y + i, z + j))) {
                    int d = dx * dx + j * j + i * i;
                    if (d < this.closest) {
                        this.closest = d;
                        this.tx = x + dx;
                        this.ty = y + i;
                        this.tz = z + j;
                        found++;
                    }
                }
                if (isWater(new BlockPos(x - dx, y + i, z + j))) {
                    int d = dx * dx + j * j + i * i;
                    if (d < this.closest) {
                        this.closest = d;
                        this.tx = x - dx;
                        this.ty = y + i;
                        this.tz = z + j;
                        found++;
                    }
                }
            }
        }

        for (int xi = -dx; xi <= dx; xi++) {
            for (int j = -dz; j <= dz; j++) {
                if (isWater(new BlockPos(x + xi, y + dy, z + j))) {
                    int d = dy * dy + j * j + xi * xi;
                    if (d < this.closest) {
                        this.closest = d;
                        this.tx = x + xi;
                        this.ty = y + dy;
                        this.tz = z + j;
                        found++;
                    }
                }
                if (isWater(new BlockPos(x + xi, y - dy, z + j))) {
                    int d = dy * dy + j * j + xi * xi;
                    if (d < this.closest) {
                        this.closest = d;
                        this.tx = x + xi;
                        this.ty = y - dy;
                        this.tz = z + j;
                        found++;
                    }
                }
            }
        }

        for (int xi = -dx; xi <= dx; xi++) {
            for (int j = -dy; j <= dy; j++) {
                if (isWater(new BlockPos(x + xi, y + j, z + dz))) {
                    int d = dz * dz + j * j + xi * xi;
                    if (d < this.closest) {
                        this.closest = d;
                        this.tx = x + xi;
                        this.ty = y + j;
                        this.tz = z + dz;
                        found++;
                    }
                }
                if (isWater(new BlockPos(x + xi, y + j, z - dz))) {
                    int d = dz * dz + j * j + xi * xi;
                    if (d < this.closest) {
                        this.closest = d;
                        this.tx = x + xi;
                        this.ty = y + j;
                        this.tz = z - dz;
                        found++;
                    }
                }
            }
        }

        return found != 0;
    }

    private boolean isWater(BlockPos pos) {
        BlockState state = this.level().getBlockState(pos);
        return state.is(Blocks.WATER);
    }

    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
        if (this.isDeadOrDying()) {
            return;
        }
        if (this.hurtTimer > 0) {
            this.hurtTimer--;
        }

        // gold: if not in water, 1/25 water hunt
        if (!this.isInWater() && this.random.nextInt(25) == 0) {
            this.closest = 99999;
            this.tx = this.ty = this.tz = 0;
            for (int i = 1; i < 12; i++) {
                int j = i;
                if (j > 10) {
                    j = 10;
                }
                if (this.scanIt((int) this.getX(), (int) this.getY() - 1, (int) this.getZ(), i, j, i)) {
                    break;
                }
                if (i >= 5) {
                    i++;
                }
            }
            if (this.closest < 99999) {
                this.getNavigation().moveTo(this.tx, this.ty - 1, this.tz, 1.33);
            } else {
                if (this.random.nextInt(100) == 1) {
                    // gold heal(-1.0F * scale) — damage self when stranded
                    this.hurt(this.damageSources().generic(), 1.0F * this.getCrabScale());
                }
                if (this.getHealth() <= 0.0F) {
                    this.discard();
                    return;
                }
            }
        }

        // gold combat 1/5
        if (this.random.nextInt(5) == 1) {
            if (this.random.nextInt(100) == 1) {
                this.setTarget(null);
            }
            LivingEntity e = this.getTarget();
            if (e != null && !e.isAlive()) {
                this.setTarget(null);
                e = null;
            }
            if (e == null) {
                e = this.findSomethingToAttack();
            }
            if (e != null) {
                this.getLookControl().setLookAt(e, 10.0F, 10.0F);
                // gold reach: (6 + width/2)^2 * scale — gold multiplies the product of squares oddly;
                // interpret as distSq < (6+w/2)^2 * scale for practical parity with large crab
                double reach = 6.0 + e.getBbWidth() / 2.0;
                if (this.distanceToSqr(e) < reach * reach * this.getCrabScale()) {
                    this.setAttacking(1);
                    if (this.random.nextInt(4) == 0 || this.random.nextInt(5) == 1) {
                        this.doHurtTarget(e);
                        // gold scorpion_attack / scorpion_living at 0.75/1.5
                    }
                } else {
                    this.getNavigation().moveTo(e, 1.0);
                }
            } else {
                this.setAttacking(0);
            }
        }

        // gold heal in water 1/120 when damaged
        if (this.random.nextInt(120) == 1
                && this.isInWater()
                && this.getHealth() < this.mygetMaxHealth()) {
            this.playSound(SoundEvents.GENERIC_SPLASH, 1.5F, this.random.nextFloat() * 0.2F + 0.9F);
            this.heal(4.0F * this.getCrabScale());
        }
    }

    /**
     * Gold isSuitableTarget: players (not creative), EntityMob yes, Crab no,
     * Lizard/RubberDucky/Villager/Girlfriend/Boyfriend, isAttackableNonMob.
     */
    private boolean isSuitableTarget(LivingEntity target) {
        if (target == null || target == this || !target.isAlive()) {
            return false;
        }
        if (!this.hasLineOfSight(target)) {
            return false;
        }
        if (target instanceof Crab) {
            return false;
        }
        if (target instanceof Player player) {
            return !player.isSpectator() && !player.getAbilities().instabuild;
        }
        if (target instanceof Monster) {
            return true;
        }
        // gold Lizard / Girlfriend / Boyfriend not ported
        if (target instanceof RubberDucky) {
            return true;
        }
        if (target instanceof Villager) {
            return true;
        }
        // gold MyUtils.isAttackableNonMob — animals etc.
        return target instanceof Animal;
    }

    @Nullable
    private LivingEntity findSomethingToAttack() {
        if (OreSpawnMain.PlayNicely != 0) {
            return null;
        }
        LivingEntity current = this.getTarget();
        if (current != null && current.isAlive()) {
            return current;
        }
        this.setTarget(null);
        // gold box expand(16, 6, 16)
        return GoldStyleCombat.findTarget(this, 16.0, 6.0, this::isSuitableTarget);
    }

    private int findBuddies() {
        return this.level()
                .getEntitiesOfClass(Crab.class, this.getBoundingBox().inflate(24.0, 8.0, 24.0))
                .size();
    }

    /**
     * Gold getCanSpawnHere: spawner "Crab" → scale 0.35; else y≥50, daytime;
     * DimensionID5 1/40 and buddies≤3.
     */
    @Override
    public boolean checkSpawnRules(LevelAccessor level, MobSpawnType spawnType) {
        // spawner proximity
        BlockPos origin = this.blockPosition();
        for (int k = -3; k < 3; k++) {
            for (int j = -3; j < 3; j++) {
                for (int i = 0; i < 5; i++) {
                    BlockPos p = origin.offset(j, i, k);
                    if (level.getBlockState(p).is(Blocks.SPAWNER)) {
                        BlockEntity be = level.getBlockEntity(p);
                        if (be instanceof SpawnerBlockEntity) {
                            // gold sets scale 0.35 and returns true for spawner "Crab"
                            this.scaleInitialized = true;
                            this.setCrabScale(0.35F);
                            return true;
                        }
                    }
                }
            }
        }
        if (this.getY() < 50.0) {
            return false;
        }
        if (level instanceof Level lvl && !lvl.isDay()) {
            return false;
        }
        // gold DimensionID5 special density — deferred to dimension-id check
        if (this.findBuddies() > 3) {
            // only enforced on gold dim5; keep soft cap elsewhere as mild density control
        }
        return super.checkSpawnRules(level, spawnType);
    }
}
