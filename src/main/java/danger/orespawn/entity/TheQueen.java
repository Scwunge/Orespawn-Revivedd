package danger.orespawn.entity;

import danger.orespawn.OreSpawnMain;
import danger.orespawn.init.ModEntities;
import danger.orespawn.init.ModItems;
import danger.orespawn.util.handlers.SoundsHandler;
import java.util.Comparator;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.SmallFireball;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.ClipContext;
import org.jetbrains.annotations.Nullable;

/**
 * Gold {@code TheQueen} (EntityMob) best-effort AI + 1:1 stats for NeoForge 1.21.1.
 * <p>
 * Gold defaults ({@code TheQueen_stats}): health <b>6000</b>, defense <b>225</b>, attack <b>21</b>.
 * Size 22×24 (PlayNicely: 5.5×6), speed 0.62, XP 25000, fire immune, noClip flyer.
 * <p>
 * Data: attacking(20), PlayNicely(21), mood/happy(22), power/attack_level(23).
 * Mood 0 = happy (flower power dump), 1 = mad (PurplePower dump deferred).
 * AI reconstructed from gold bytecode (Vineflower failed {@code func_70619_bc}) + TheKing parity.
 * <p>
 * Texture: {@code textures/entity/thequeentexture.png} / {@code thequeentexture2.png} when happy.
 * Registry size/attrs set in {@code ModEntities}.
 */
public class TheQueen extends Monster {
    private static final EntityDataAccessor<Integer> DATA_ATTACKING =
            SynchedEntityData.defineId(TheQueen.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> DATA_PLAY_NICELY =
            SynchedEntityData.defineId(TheQueen.class, EntityDataSerializers.INT);
    /** Gold datawatcher 22 = mood (0 happy, 1 mad). */
    private static final EntityDataAccessor<Integer> DATA_MOOD =
            SynchedEntityData.defineId(TheQueen.class, EntityDataSerializers.INT);
    /** Gold datawatcher 23 = attack_level / power. */
    private static final EntityDataAccessor<Integer> DATA_POWER =
            SynchedEntityData.defineId(TheQueen.class, EntityDataSerializers.INT);

    public static final float GOLD_WIDTH = 22.0F;
    public static final float GOLD_HEIGHT = 24.0F;
    public static final float GOLD_WIDTH_NICE = 5.5F;
    public static final float GOLD_HEIGHT_NICE = 6.0F;
    public static final double GOLD_HEALTH = 6000.0;
    public static final double GOLD_SPEED = 0.62;
    public static final double GOLD_ATTACK = 21.0;
    public static final double GOLD_ARMOR = 225.0;
    public static final int GOLD_XP = 25000;
    public static final double GOLD_FOLLOW = 128.0;

    private double attdam = GOLD_ATTACK;
    private int hurt_timer = 0;
    private int homex = 0;
    private int homez = 0;
    private int stream_count = 0;
    private int stream_count_l = 0;
    private int ticker = 0;
    private int player_hit_count = 0;
    private int backoff_timer = 0;
    private int guard_mode = 0;
    private volatile int head_found = 0;
    private int wing_sound = 0;
    private int attack_level = 1;
    private int mood = 0;
    private int always_mad = 0;

    @Nullable
    private BlockPos currentFlightTarget;
    @Nullable
    private LivingEntity rt;
    @Nullable
    private LivingEntity ev;
    private float evh = 0.0F;

    public TheQueen(EntityType<? extends TheQueen> type, Level level) {
        super(type, level);
        this.xpReward = GOLD_XP;
        this.noCulling = true;
        this.setNoGravity(true);
        this.noPhysics = true;
        this.attdam = GOLD_ATTACK;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, GOLD_HEALTH)
                .add(Attributes.MOVEMENT_SPEED, GOLD_SPEED)
                .add(Attributes.ATTACK_DAMAGE, GOLD_ATTACK)
                .add(Attributes.ARMOR, GOLD_ARMOR)
                .add(Attributes.FOLLOW_RANGE, GOLD_FOLLOW)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1.0);
    }

    @Override
    protected void registerGoals() {
        // gold: Swimming, LookIdle, HurtByTarget
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_ATTACKING, 0);
        builder.define(DATA_PLAY_NICELY, OreSpawnMain.PlayNicely);
        builder.define(DATA_MOOD, 0);
        builder.define(DATA_POWER, 1);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("KingHomeX", this.homex);
        tag.putInt("KingHomeZ", this.homez);
        tag.putInt("GuardMode", this.guard_mode);
        tag.putInt("PlayerHits", this.player_hit_count);
        tag.putInt("MeanMode", this.always_mad);
        tag.putInt("QueenMood", this.mood);
        tag.putInt("QueenPower", this.attack_level);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.homex = tag.getInt("KingHomeX");
        this.homez = tag.getInt("KingHomeZ");
        this.guard_mode = tag.getInt("GuardMode");
        this.player_hit_count = tag.getInt("PlayerHits");
        this.always_mad = tag.getInt("MeanMode");
        if (tag.contains("QueenMood")) {
            this.mood = tag.getInt("QueenMood");
        }
        if (tag.contains("QueenPower")) {
            this.attack_level = tag.getInt("QueenPower");
            this.setPower(this.attack_level);
        }
    }

    @Override
    public boolean fireImmune() {
        return true;
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return false;
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    protected void doPush(Entity entity) {
        // gold func_82167_n empty
    }

    @Override
    public void push(Entity entity) {
        // gold no push
    }

    @Override
    protected void checkFallDamage(double y, boolean onGround, BlockState state, BlockPos pos) {
        // gold no fall
    }

    @Override
    public boolean causeFallDamage(float fallDistance, float multiplier, DamageSource source) {
        return false;
    }

    @Override
    public boolean canBeCollidedWith() {
        return true;
    }

    public int getPlayNicely() {
        return this.entityData.get(DATA_PLAY_NICELY);
    }

    public int getIsHappy() {
        return this.entityData.get(DATA_MOOD);
    }

    /** Gold isHappy: mood data == 0. */
    public boolean isHappy() {
        return this.getIsHappy() == 0;
    }

    public int getAttacking() {
        return this.entityData.get(DATA_ATTACKING);
    }

    public void setAttacking(int value) {
        this.entityData.set(DATA_ATTACKING, value);
    }

    public int getPower() {
        return this.entityData.get(DATA_POWER);
    }

    public void setPower(int value) {
        this.entityData.set(DATA_POWER, value);
    }

    public void setGuardMode(int i) {
        this.guard_mode = i;
    }

    public void setBadMood(int i) {
        this.always_mad = i;
    }

    public int mygetMaxHealth() {
        return (int) GOLD_HEALTH;
    }

    @Override
    public void tick() {
        if (this.getAttribute(Attributes.MOVEMENT_SPEED) != null) {
            this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(GOLD_SPEED);
        }
        if (this.getAttribute(Attributes.ATTACK_DAMAGE) != null) {
            this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(this.attdam);
        }
        if (this.getAttribute(Attributes.ARMOR) != null) {
            this.getAttribute(Attributes.ARMOR).setBaseValue(this.computeArmor());
        }

        super.tick();

        this.wing_sound++;
        if (this.wing_sound > 30) {
            if (!this.level().isClientSide) {
                this.playSound(SoundsHandler.ENTITY_MOTHRA_WINGS.get(), 1.75F, 0.75F);
            }
            this.wing_sound = 0;
        }

        this.noPhysics = true;
        this.setNoGravity(true);
        Vec3 m = this.getDeltaMovement();
        this.setDeltaMovement(m.x, m.y * 0.6, m.z);

        // enrage damage multipliers (gold tick, only if player_hit_count < 10)
        float hp = this.getHealth();
        float max = this.mygetMaxHealth();
        this.attdam = GOLD_ATTACK;
        if (this.player_hit_count < 10) {
            if (hp < max * 3 / 4) {
                this.attdam = GOLD_ATTACK * 20;
            }
            if (hp < max / 2) {
                this.attdam = GOLD_ATTACK * 100;
            }
            if (hp < max / 3) {
                this.attdam = GOLD_ATTACK * 500;
            }
            if (hp < max / 4) {
                this.attdam = GOLD_ATTACK * 1000;
            }
        }

        // client fireworks when power high
        if (this.level().isClientSide && this.getPower() > 800) {
            float f = 7.0F;
            if (this.random.nextInt(4) == 1) {
                for (int i = 0; i < 10; i++) {
                    double px = this.getX() - f * Math.sin(Math.toRadians(this.getYRot()));
                    double py = this.getY() + 14.0;
                    double pz = this.getZ() + f * Math.cos(Math.toRadians(this.getYRot()));
                    this.level()
                            .addParticle(
                                    ParticleTypes.FIREWORK,
                                    px,
                                    py,
                                    pz,
                                    (this.random.nextGaussian() - this.random.nextGaussian()) / 5.0
                                            + this.getDeltaMovement().x * 3.0,
                                    (this.random.nextGaussian() - this.random.nextGaussian()) / 5.0,
                                    (this.random.nextGaussian() - this.random.nextGaussian()) / 5.0
                                            + this.getDeltaMovement().z * 3.0);
                }
            }
        }
    }

    private double computeArmor() {
        float hp = this.getHealth();
        float max = this.mygetMaxHealth();
        if (this.player_hit_count < 10 && hp < max * 2 / 3) {
            return GOLD_ARMOR + 2;
        } else if (this.player_hit_count < 10 && hp < max / 2) {
            return GOLD_ARMOR + 3;
        } else if (this.player_hit_count < 10 && hp < max / 3) {
            return GOLD_ARMOR + 5;
        }
        return GOLD_ARMOR;
    }

    @Override
    protected void customServerAiStep() {
        if (this.isDeadOrDying()) {
            return;
        }
        super.customServerAiStep();

        // lock target health (gold anti-heal)
        if (this.ev != null) {
            if (this.distanceToSqr(this.ev) < 2000.0 && this.ev.isAlive()) {
                if (this.evh < this.ev.getHealth()) {
                    this.ev.setHealth(this.evh);
                } else {
                    this.evh = this.ev.getHealth();
                }
                if (this.evh <= 0.0F) {
                    this.ev.discard();
                }
            } else {
                this.ev = null;
                this.evh = 0.0F;
            }
        }

        // power dump when attack_level > 1000
        if (this.attack_level > 1000) {
            if (this.mood == 1) {
                // gold: spawn PurplePower ×15 (×45 if player_hit_count < 10) — deferred entity
                // stand-in: small fireball barrage
                int n = this.player_hit_count < 10 ? 45 : 15;
                double xzoff = 8.0;
                double yoff = 14.0;
                for (int i = 0; i < n; i++) {
                    double cx = this.getX() - xzoff * Math.sin(Math.toRadians(this.getYRot()));
                    double cz = this.getZ() + xzoff * Math.cos(Math.toRadians(this.getYRot()));
                    Vec3 dir = new Vec3(
                            this.getDeltaMovement().x * 3.0 + (this.random.nextDouble() - 0.5),
                            0.2 + this.random.nextDouble() * 0.3,
                            this.getDeltaMovement().z * 3.0 + (this.random.nextDouble() - 0.5));
                    SmallFireball fb = new SmallFireball(this.level(), this, dir);
                    fb.setPos(cx, this.getY() + yoff, cz);
                    this.level().addFreshEntity(fb);
                }
            } else if (this.level().getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING)) {
                this.doHappyTerrainPass();
            }
            this.attack_level = 1;
        }

        if (this.attack_level > 1) {
            this.attack_level--;
        }

        if (this.hurt_timer > 0) {
            this.hurt_timer--;
        }

        if (this.homex == 0 && this.homez == 0 || this.guard_mode == 0) {
            this.homex = (int) this.getX();
            this.homez = (int) this.getZ();
        }

        // calm if near full health
        if (this.getHealth() > this.mygetMaxHealth() - 2 && this.random.nextInt(500) == 1) {
            this.mood = 0;
        }
        if (this.always_mad != 0) {
            this.mood = 1;
        }
        // happy builds power passively
        if (this.mood == 0) {
            this.attack_level += 10;
        }

        this.ticker++;
        if (this.ticker > 30000) {
            this.ticker = 0;
        }
        if (this.ticker % 60 == 0) {
            this.stream_count = 10;
        }
        if (this.ticker % 70 == 0) {
            this.stream_count_l = 6;
        }
        if (this.ticker % 10 == 0) {
            this.entityData.set(DATA_PLAY_NICELY, OreSpawnMain.PlayNicely);
            this.entityData.set(DATA_MOOD, this.mood);
            this.setPower(this.attack_level);
        }

        if (this.backoff_timer > 0) {
            this.backoff_timer--;
        }

        int attrand = 5;
        if (this.player_hit_count < 10 && this.getHealth() < this.mygetMaxHealth() / 2) {
            attrand = 3;
        }

        this.noPhysics = true;

        if (this.currentFlightTarget == null) {
            this.currentFlightTarget = BlockPos.containing(this.getX(), this.getY(), this.getZ());
        }

        if (this.tooFarFromHome()
                || this.random.nextInt(200) == 0
                || this.currentFlightTarget.distSqr(
                                BlockPos.containing(this.getX(), this.getY(), this.getZ()))
                        < 9.1F) {
            this.pickWanderFlightTarget();
        } else if (this.random.nextInt(attrand) == 0) {
            this.doCombatAi();
        }

        // fly toward target
        double var1 = this.currentFlightTarget.getX() + 0.5 - this.getX();
        double var3 = this.currentFlightTarget.getY() + 0.1 - this.getY();
        double var5 = this.currentFlightTarget.getZ() + 0.5 - this.getZ();
        Vec3 m = this.getDeltaMovement();
        // gold: signum * 0.65 * blend 0.35 (xz), 0.69999 * 0.3 (y)
        this.setDeltaMovement(
                m.x + (Math.signum(var1) * 0.65 - m.x) * 0.35,
                m.y + (Math.signum(var3) * 0.69999 - m.y) * 0.3,
                m.z + (Math.signum(var5) * 0.65 - m.z) * 0.35);
        float var7 =
                (float) (Math.atan2(this.getDeltaMovement().z, this.getDeltaMovement().x) * 180.0
                                / Math.PI)
                        - 90.0F;
        float var8 = Mth.wrapDegrees(var7 - this.getYRot());
        this.setZza(0.75F);
        this.setYRot(this.getYRot() + var8 / 8.0F);

        if (this.random.nextInt(32) == 1 && this.getHealth() < this.mygetMaxHealth()) {
            this.heal(5.0F);
            if (this.player_hit_count < 10) {
                this.heal(50.0F);
            }
        }
        if (this.player_hit_count < 10 && this.getHealth() < 2000.0F) {
            this.heal(2000.0F - this.getHealth());
        }
    }

    /** Happy power dump: place flowers / transform nearby terrain (gold mobGriefing path). */
    private void doHappyTerrainPass() {
        for (int n = 0; n < 25; n++) {
            int dx = this.random.nextInt(25) - this.random.nextInt(25);
            int dz = this.random.nextInt(25) - this.random.nextInt(25);
            for (int dy = -20; dy < 20; dy++) {
                BlockPos pos = BlockPos.containing(this.getX() + dx, this.getY() + dy, this.getZ() + dz);
                BlockState state = this.level().getBlockState(pos);
                BlockPos above = pos.above();
                if (state.is(Blocks.GRASS_BLOCK) && this.level().getBlockState(above).isAir()) {
                    int which = this.random.nextInt(8);
                    if (which == 0) {
                        this.level().setBlock(above, Blocks.POPPY.defaultBlockState(), 3);
                    } else if (which == 1) {
                        this.level().setBlock(above, Blocks.DANDELION.defaultBlockState(), 3);
                    }
                    break;
                }
                if (state.is(Blocks.DIRT) && this.level().getBlockState(above).isAir()) {
                    this.level().setBlock(pos, Blocks.GRASS_BLOCK.defaultBlockState(), 3);
                    break;
                }
                if (state.is(Blocks.STONE) && this.level().getBlockState(above).isAir()) {
                    this.level().setBlock(pos, Blocks.DIRT.defaultBlockState(), 3);
                    break;
                }
                if (state.is(Blocks.SAND) && this.level().getBlockState(above).isAir()) {
                    this.level().setBlock(pos, Blocks.DIRT.defaultBlockState(), 3);
                    break;
                }
                if (state.is(Blocks.LAVA)) {
                    this.level().setBlock(pos, Blocks.WATER.defaultBlockState(), 3);
                    break;
                }
                if (state.is(Blocks.WATER) || state.is(Blocks.BUBBLE_COLUMN)) {
                    // leave water
                    break;
                }
            }
        }
    }

    private void pickWanderFlightTarget() {
        int zdir = this.random.nextInt(120);
        int xdir = this.random.nextInt(120);
        if (this.random.nextInt(2) == 0) {
            zdir = -zdir;
        }
        if (this.random.nextInt(2) == 0) {
            xdir = -xdir;
        }
        int dist = this.scanAltitudeBias(this.homex, this.homez);
        if ((int) (this.getY() + dist) > 230) {
            dist = 230 - (int) this.getY();
        }
        this.currentFlightTarget =
                new BlockPos(this.homex + xdir, (int) (this.getY() + dist), this.homez + zdir);
    }

    private int scanAltitudeBias(int cx, int cz) {
        int dist = 0;
        for (int i = -5; i <= 5; i += 5) {
            for (int j = -5; j <= 5; j += 5) {
                BlockPos base = new BlockPos(cx + j, (int) this.getY(), cz + i);
                if (!this.level().getBlockState(base).isAir()) {
                    for (int k = 1; k < 20; k++) {
                        dist++;
                        if (this.level().getBlockState(base.above(k)).isAir()) {
                            break;
                        }
                    }
                } else {
                    for (int k = 1; k < 20; k++) {
                        dist--;
                        if (!this.level().getBlockState(base.below(k)).isAir()) {
                            break;
                        }
                    }
                }
            }
        }
        return dist / 9 + 2;
    }

    private void doCombatAi() {
        LivingEntity e = this.rt;
        if (OreSpawnMain.PlayNicely != 0 || this.isHappy()) {
            e = null;
        }

        if (e != null && (e instanceof TheQueen || e instanceof QueenHead)) {
            this.rt = null;
            e = null;
        }

        if (e != null) {
            float d1 = (float) (e.getX() - this.homex);
            float d2 = (float) (e.getZ() - this.homez);
            d1 = (float) Math.sqrt(d1 * d1 + d2 * d2);
            if (!e.isAlive()
                    || this.random.nextInt(450) == 1
                    || (d1 > 128.0F && this.guard_mode == 1)) {
                e = null;
                this.rt = null;
            }
            if (e != null && !this.myCanSee(e)) {
                e = null;
            }
        }

        LivingEntity f = this.findSomethingToAttack();
        // gold: spawnCreature QueenHead when mad and head missing
        if (this.head_found == 0 && this.mood == 1) {
            QueenHead head = ModEntities.QUEEN_HEAD.get().create(this.level());
            if (head != null) {
                head.moveTo(this.getX(), this.getY() + 20.0, this.getZ(), this.getYRot(), 0.0F);
                this.level().addFreshEntity(head);
            }
        }

        if (e == null) {
            e = f;
        }

        if (e != null) {
            float size = e.getBbWidth() * e.getBbHeight();
            if (this.attack_level < 1000) {
                this.attack_level += 15;
                if (this.getHealth() < this.mygetMaxHealth() / 2) {
                    this.attack_level += 15;
                }
                if (size > 50.0F) {
                    this.attack_level += 15;
                }
                if (size > 100.0F) {
                    this.attack_level += 15;
                }
                if (size > 200.0F) {
                    this.attack_level += 25;
                }
            }

            this.setAttacking(1);

            if (this.backoff_timer == 0) {
                int dist = (int) (e.getY() + e.getBbHeight() / 2.0F + 1.0);
                if (dist > 230) {
                    dist = 230;
                }
                this.currentFlightTarget = new BlockPos((int) e.getX(), dist, (int) e.getZ());
                if (this.random.nextInt(50) == 1) {
                    this.backoff_timer = 90 + this.random.nextInt(90);
                }
            } else if (this.currentFlightTarget.distSqr(
                            BlockPos.containing(this.getX(), this.getY(), this.getZ()))
                    < 9.1F) {
                int zdir = this.random.nextInt(20) + 30;
                int xdir = this.random.nextInt(20) + 30;
                if (this.random.nextInt(2) == 0) {
                    zdir = -zdir;
                }
                if (this.random.nextInt(2) == 0) {
                    xdir = -xdir;
                }
                int dist = this.scanAltitudeBias((int) e.getX(), (int) e.getZ());
                if ((int) (this.getY() + dist) > 230) {
                    dist = 230 - (int) this.getY();
                }
                this.currentFlightTarget = new BlockPos(
                        (int) e.getX() + xdir, (int) (this.getY() + dist), (int) e.getZ() + zdir);
            }

            if (this.distanceToSqr(e) < 900.0) {
                if (this.random.nextInt(2) == 1) {
                    this.doJumpDamage(
                            this.getX(), this.getY(), this.getZ(), 15.0, GOLD_ATTACK / 4.0, 0);
                }
                this.doHurtTarget(e);
            }

            double dx = this.getX() + 20.0 * Math.sin(Math.toRadians(this.yBodyRot));
            double dz = this.getZ() - 20.0 * Math.cos(Math.toRadians(this.yBodyRot));
            if (this.random.nextInt(3) == 1) {
                this.doJumpDamage(dx, this.getY() + 10.0, dz, 15.0, GOLD_ATTACK / 2.0, 1);
            }

            if (this.getHorizontalDistanceSqToEntity(e) > 900.0) {
                int which = this.random.nextInt(2);
                if (which == 0) {
                    if (this.stream_count > 0) {
                        this.setAttacking(1);
                        if (this.aimConeOk(e)) {
                            this.firecanon(e);
                        }
                    }
                } else if (this.stream_count_l > 0) {
                    this.setAttacking(1);
                    if (this.aimConeOk(e)) {
                        this.firecanonl(e);
                    }
                }
            }
        } else {
            this.setAttacking(0);
            this.stream_count = 10;
            this.stream_count_l = 6;
        }
    }

    private boolean aimConeOk(LivingEntity e) {
        double rr = Math.atan2(e.getZ() - this.getZ(), e.getX() - this.getX());
        double rhdir = Math.toRadians((this.yBodyRot + 90.0F) % 360.0F);
        double pi = 3.1415926545;
        double rdd = Math.abs(rr - rhdir) % (pi * 2.0);
        if (rdd > pi) {
            rdd -= pi * 2.0;
        }
        rdd = Math.abs(rdd);
        return rdd < 0.5;
    }

    private boolean tooFarFromHome() {
        float d1 = (float) (this.getX() - this.homex);
        float d2 = (float) (this.getZ() - this.homez);
        d1 = (float) Math.sqrt(d1 * d1 + d2 * d2);
        return d1 > 120.0F;
    }

    private double getHorizontalDistanceSqToEntity(Entity e) {
        double d1 = e.getZ() - this.getZ();
        double d2 = e.getX() - this.getX();
        return d1 * d1 + d2 * d2;
    }

    /** Gold firecanon: ReallyBig fireball + 6 big/small. */
    private void firecanon(LivingEntity e) {
        double yoff = 14.0;
        double xzoff = 32.0;
        double cx = this.getX() - xzoff * Math.sin(Math.toRadians(this.getYRot()));
        double cz = this.getZ() + xzoff * Math.cos(Math.toRadians(this.getYRot()));
        if (this.stream_count > 0) {
            Vec3 dir = new Vec3(
                    e.getX() - cx,
                    e.getY() + e.getBbHeight() / 2.0F - (this.getY() + yoff),
                    e.getZ() - cz);
            BetterFireball bf = new BetterFireball(this.level(), this, dir);
            bf.setPos(cx, this.getY() + yoff, cz);
            bf.setReallyBig();
            this.playSound(
                    SoundEvents.TNT_PRIMED,
                    1.0F,
                    1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
            this.level().addFreshEntity(bf);

            for (int i = 0; i < 6; i++) {
                float r1 = 5.0F * (this.random.nextFloat() - this.random.nextFloat());
                float r2 = 3.0F * (this.random.nextFloat() - this.random.nextFloat());
                float r3 = 5.0F * (this.random.nextFloat() - this.random.nextFloat());
                Vec3 d2 = new Vec3(
                        e.getX() - cx + r1,
                        e.getY() + e.getBbHeight() / 2.0F - (this.getY() + yoff) + r2,
                        e.getZ() - cz + r3);
                bf = new BetterFireball(this.level(), this, d2);
                bf.setPos(cx, this.getY() + yoff, cz);
                bf.setBig();
                if (this.random.nextInt(2) == 1) {
                    bf.setSmall();
                }
                this.playSound(
                        SoundEvents.ARROW_SHOOT,
                        1.0F,
                        1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
                this.level().addFreshEntity(bf);
            }
            this.stream_count--;
        }
    }

    /** Gold firecanonl → ThunderBolt ×3 (deferred). Stand-in: SmallFireball. */
    private void firecanonl(LivingEntity e) {
        double yoff = 14.0;
        double xzoff = 32.0;
        double cx = this.getX() - xzoff * Math.sin(Math.toRadians(this.getYRot()));
        double cz = this.getZ() + xzoff * Math.cos(Math.toRadians(this.getYRot()));
        if (this.stream_count_l > 0) {
            this.playSound(
                    SoundEvents.ARROW_SHOOT,
                    1.0F,
                    1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
            for (int i = 0; i < 3; i++) {
                Vec3 dir = new Vec3(
                        e.getX() - cx,
                        e.getY() + 0.25 - (this.getY() + yoff),
                        e.getZ() - cz);
                SmallFireball lb = new SmallFireball(this.level(), this, dir.normalize().scale(3.0));
                lb.setPos(cx, this.getY() + yoff, cz);
                this.level().addFreshEntity(lb);
            }
            this.stream_count_l--;
        }
    }

    @Override
    public boolean doHurtTarget(Entity par1Entity) {
        if (par1Entity instanceof LivingEntity living && !this.level().isClientSide) {
            if (!living.isDeadOrDying()) {
                if (this.ev == living) {
                    if (this.evh < living.getHealth()) {
                        living.setHealth(this.evh);
                    }
                } else {
                    this.ev = living;
                }

                if (living.getBbWidth() * living.getBbHeight() > 30.0F) {
                    living.setHealth(living.getHealth() * 3.0F / 4.0F);
                    living.hurt(this.damageSources().mobAttack(this), (float) this.attdam);
                }

                this.evh = living.getHealth();
                if (this.evh <= 0.0F) {
                    this.ev.discard();
                }
            } else {
                this.ev = null;
                this.evh = 0.0F;
            }
        }

        if (par1Entity instanceof EnderDragon dr) {
            // gold damages random dragon part — simplified whole-dragon hit
            dr.hurt(this.damageSources().mobAttack(this), (float) this.attdam);
        }

        boolean var4 = par1Entity.hurt(this.damageSources().mobAttack(this), (float) this.attdam);
        if (var4) {
            double ks = 2.75;
            double inair = 0.2;
            float f3 = (float) Math.atan2(par1Entity.getZ() - this.getZ(), par1Entity.getX() - this.getX());
            inair += this.random.nextFloat() * 0.25F;
            if (!par1Entity.isAlive() || par1Entity instanceof Player) {
                inair *= 1.5;
            }
            par1Entity.push(Math.cos(f3) * ks, inair, Math.sin(f3) * ks);
        }
        return var4;
    }

    public boolean canSeeTarget(double pX, double pY, double pZ) {
        Vec3 from = new Vec3(this.getX(), this.getY() + 8.75, this.getZ());
        Vec3 to = new Vec3(pX, pY, pZ);
        return this.level()
                        .clip(new ClipContext(
                                from, to, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this))
                        .getType()
                == HitResult.Type.MISS;
    }

    /** Gold MyCanSee: step sample along aim from mouth. */
    public boolean myCanSee(LivingEntity e) {
        double xzoff = 10.0;
        int nblks = 20;
        double cx = this.getX() - xzoff * Math.sin(Math.toRadians(this.getYRot()));
        double cz = this.getZ() + xzoff * Math.cos(Math.toRadians(this.getYRot()));
        float startx = (float) cx;
        float starty = (float) (this.getY() + 14.0);
        float startz = (float) cz;
        float dx = (float) ((e.getX() - startx) / 20.0);
        float dy = (float) ((e.getY() + e.getBbHeight() / 2.0F - starty) / 20.0);
        float dz = (float) ((e.getZ() - startz) / 20.0);
        if (Math.abs(dx) > 1.0) {
            dy /= Math.abs(dx);
            dz /= Math.abs(dx);
            nblks = (int) (nblks * Math.abs(dx));
            if (dx > 1.0F) {
                dx = 1.0F;
            }
            if (dx < -1.0F) {
                dx = -1.0F;
            }
        }
        if (Math.abs(dy) > 1.0) {
            dx /= Math.abs(dy);
            dz /= Math.abs(dy);
            nblks = (int) (nblks * Math.abs(dy));
            if (dy > 1.0F) {
                dy = 1.0F;
            }
            if (dy < -1.0F) {
                dy = -1.0F;
            }
        }
        if (Math.abs(dz) > 1.0) {
            dy /= Math.abs(dz);
            dx /= Math.abs(dz);
            nblks = (int) (nblks * Math.abs(dz));
            if (dz > 1.0F) {
                dz = 1.0F;
            }
            if (dz < -1.0F) {
                dz = -1.0F;
            }
        }
        for (int i = 0; i < nblks; i++) {
            startx += dx;
            starty += dy;
            startz += dz;
            if (!this.level()
                    .getBlockState(BlockPos.containing(startx, starty, startz))
                    .isAir()) {
                return false;
            }
        }
        return true;
    }

    private boolean isRoyalty(LivingEntity e) {
        // gold MyUtils.isRoyalty
        return e instanceof TheQueen
                || e instanceof ThePrince
                || e instanceof ThePrincess
                || e instanceof ThePrinceTeen
                || e instanceof ThePrinceAdult
                || e instanceof Spyro;
    }

    private boolean isSuitableTarget(LivingEntity par1EntityLiving) {
        if (par1EntityLiving == null || par1EntityLiving == this || !par1EntityLiving.isAlive()) {
            return false;
        }
        if (par1EntityLiving instanceof QueenHead) {
            this.head_found = 1;
            return false;
        }
        if (this.isRoyalty(par1EntityLiving)) {
            return false;
        }
        float d1 = (float) (par1EntityLiving.getX() - this.homex);
        float d2 = (float) (par1EntityLiving.getZ() - this.homez);
        d1 = (float) Math.sqrt(d1 * d1 + d2 * d2);
        if (d1 > 144.0F) {
            return false;
        }
        if (!this.hasLineOfSight(par1EntityLiving)) {
            return false;
        }
        if (par1EntityLiving instanceof Player p) {
            return !p.getAbilities().instabuild && !p.isSpectator();
        }
        if (par1EntityLiving instanceof AbstractHorse) {
            return true;
        }
        if (par1EntityLiving instanceof Monster) {
            return true;
        }
        if (par1EntityLiving instanceof EnderDragon) {
            return true;
        }
        // gold isAttackableNonMob
        return true;
    }

    @Nullable
    private LivingEntity findSomethingToAttack() {
        if (OreSpawnMain.PlayNicely != 0 || this.isHappy()) {
            this.head_found = 1;
            return null;
        }
        List<LivingEntity> list = this.level()
                .getEntitiesOfClass(
                        LivingEntity.class, this.getBoundingBox().inflate(80.0, 60.0, 80.0));
        list.sort(Comparator.comparingDouble(this::distanceToSqr));
        LivingEntity ret = null;
        this.head_found = 0;
        for (LivingEntity var4 : list) {
            if (this.isSuitableTarget(var4) && ret == null) {
                ret = var4;
            }
            if (ret != null && this.head_found != 0) {
                break;
            }
        }
        return ret;
    }

    @Override
    public boolean hurt(DamageSource par1DamageSource, float par2) {
        if (this.hurt_timer > 0) {
            return false;
        }
        float dm = par2;
        if (dm > 750.0F) {
            dm = 750.0F;
        }
        if ("inWall".equals(par1DamageSource.getMsgId())
                || par1DamageSource.is(net.minecraft.tags.DamageTypeTags.IS_FALL)) {
            return false;
        }

        this.mood = 1;

        // explosion damage heals queen
        if (par1DamageSource.is(net.minecraft.tags.DamageTypeTags.IS_EXPLOSION)) {
            float s = this.getHealth() + par2 / 2.0F;
            if (s > this.getMaxHealth()) {
                s = this.getMaxHealth();
            }
            this.setHealth(s);
            return false;
        }

        Entity e = par1DamageSource.getEntity();
        if (e instanceof LivingEntity living) {
            // PurplePower deferred
            float s = living.getBbHeight() * living.getBbWidth();
            if (living instanceof Monster && s < 3.0F) {
                living.discard();
                return false;
            }
        }

        if ("cactus".equals(par1DamageSource.getMsgId())) {
            return false;
        }

        this.hurt_timer = 20;
        boolean ret = super.hurt(par1DamageSource, dm);
        if (e instanceof Player) {
            this.player_hit_count++;
        }
        if (e instanceof LivingEntity living
                && this.currentFlightTarget != null
                && !this.isRoyalty(living)) {
            this.rt = living;
            int dist = (int) living.getY();
            if (dist > 230) {
                dist = 230;
            }
            this.currentFlightTarget = new BlockPos((int) living.getX(), dist, (int) living.getZ());
        }
        return ret;
    }

    @Override
    public boolean checkSpawnRules(LevelAccessor level, MobSpawnType reason) {
        return true;
    }

    @Override
    public void thunderHit(ServerLevel level, net.minecraft.world.entity.LightningBolt lightning) {
        // gold immune
    }

    private void dropItemRand(Item index, int count) {
        ItemEntity item = new ItemEntity(
                this.level(),
                this.getX() + this.random.nextInt(20) - this.random.nextInt(20),
                this.getY() + 12.0,
                this.getZ() + this.random.nextInt(20) - this.random.nextInt(20),
                new ItemStack(index, count));
        this.level().addFreshEntity(item);
    }

    @Override
    protected void dropCustomDeathLoot(ServerLevel level, DamageSource damageSource, boolean recentlyHit) {
        // Always Queen Battle Axe (not Royal — King is the Royal drop source)
        this.dropItemRand(ModItems.QUEEN_BATTLE_AXE.get(), 1);
        this.dropItemRand(ModItems.THE_PRINCE_EGG.get(), 1);
        // gold: spawn The Princess
        if (!level.isClientSide) {
            EntityType<?> princessType = ModEntities.THE_PRINCESS.get();
            Entity p = princessType.create(level);
            if (p != null) {
                p.moveTo(this.getX(), this.getY() + 10.0, this.getZ(), this.getYRot(), 0.0F);
                level.addFreshEntity(p);
            }
        }
        for (int i = 0; i < 56; i++) {
            this.dropItemRand(ModItems.QUEEN_SCALE.get(), 1);
            this.dropItemRand(Items.BEEF, 1);
            this.dropItemRand(Items.BONE, 1);
            this.dropItemRand(Items.PORKCHOP, 1);
        }
    }

    @Nullable
    private LivingEntity doJumpDamage(
            double X, double Y, double Z, double dist, double damage, int knock) {
        AABB bb = new AABB(X - dist, Y - 10.0, Z - dist, X + dist, Y + 10.0, Z + dist);
        List<LivingEntity> list = this.level().getEntitiesOfClass(LivingEntity.class, bb);
        list.sort(Comparator.comparingDouble(this::distanceToSqr));
        for (LivingEntity var4 : list) {
            if (var4 != null
                    && var4 != this
                    && var4.isAlive()
                    && !this.isRoyalty(var4)
                    && !(var4 instanceof Ghost)
                    && !(var4 instanceof GhostSkelly)) {
                var4.hurt(this.damageSources().magic(), (float) damage / 2.0F);
                var4.hurt(this.damageSources().generic(), (float) damage / 2.0F);
                this.playSound(
                        SoundEvents.GENERIC_EXPLODE.value(),
                        0.65F,
                        1.0F + (this.random.nextFloat() - this.random.nextFloat()) * 0.5F);
                if (knock != 0) {
                    double ks = 2.75;
                    double inair = 0.65;
                    float f3 = (float) Math.atan2(var4.getZ() - this.getZ(), var4.getX() - this.getX());
                    var4.push(Math.cos(f3) * ks, inair, Math.sin(f3) * ks);
                }
            }
        }
        return null;
    }

    public static Entity spawnCreature(
            Level level, EntityType<? extends Mob> type, double x, double y, double z) {
        if (!(level instanceof ServerLevel server)) {
            return null;
        }
        Entity e = type.create(server);
        if (e != null) {
            e.moveTo(x, y, z, level.random.nextFloat() * 360.0F, 0.0F);
            if (e instanceof Mob mob) {
                mob.finalizeSpawn(
                        server,
                        server.getCurrentDifficultyAt(mob.blockPosition()),
                        MobSpawnType.MOB_SUMMONED,
                        null);
            }
            level.addFreshEntity(e);
        }
        return e;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        // gold orespawn:king_living
        return SoundEvents.ENDER_DRAGON_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        // gold orespawn:king_hit
        return SoundEvents.ENDER_DRAGON_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        // gold orespawn:trex_death
        if (SoundsHandler.ENTITY_TREX_DEATH != null) {
            return SoundsHandler.ENTITY_TREX_DEATH.get();
        }
        return SoundEvents.ENDER_DRAGON_DEATH;
    }

    @Override
    protected float getSoundVolume() {
        return 1.35F;
    }

    @Override
    public float getVoicePitch() {
        return 1.0F;
    }

    /** Gold default loot item yellow flower. */
    @Override
    protected void dropFromLootTable(DamageSource damageSource, boolean hitByPlayer) {
        super.dropFromLootTable(damageSource, hitByPlayer);
    }
}
