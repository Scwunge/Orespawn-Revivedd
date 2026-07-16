package danger.orespawn.entity;

import danger.orespawn.OreSpawnMain;
import danger.orespawn.init.ModItems;
import danger.orespawn.util.ai.GoldStyleCombat;
import java.util.Comparator;
import java.util.List;
import net.minecraft.core.BlockPos;
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
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.entity.animal.Squid;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ServerLevelData;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

/**
 * Gold {@code Kraken} (EntityMob) 1:1 best-effort for NeoForge 1.21.1.
 * Size 4.0×15.0 (PlayNicely 1.333×5 via renderer /3 scale), speed 0.37,
 * health 1000, attack 10, armor 40, XP 500, follow 120, fireImmune.
 * Sky grab-and-lift boss; forces storms; may spawn reinforcements when low HP.
 * Texture: {@code textures/entity/kraken.png}.
 * Registry size/attrs set in {@code ModEntities}.
 */
public class Kraken extends Monster {
    private static final EntityDataAccessor<Byte> DATA_ATTACKING =
            SynchedEntityData.defineId(Kraken.class, EntityDataSerializers.BYTE);
    /** Gold datawatcher 21 — OreSpawnMain.PlayNicely mirror for client scale. */
    private static final EntityDataAccessor<Integer> DATA_PLAY_NICELY =
            SynchedEntityData.defineId(Kraken.class, EntityDataSerializers.INT);

    public static final float GOLD_WIDTH = 4.0F;
    public static final float GOLD_HEIGHT = 15.0F;
    public static final double GOLD_HEALTH = 1000.0;
    public static final double GOLD_SPEED = 0.37;
    public static final double GOLD_ATTACK = 10.0;
    public static final double GOLD_ARMOR = 40.0;
    public static final int GOLD_XP = 500;
    public static final double GOLD_FOLLOW = 120.0;

    private RenderInfo renderdata = new RenderInfo();
    @Nullable
    private BlockPos currentFlightTarget = null;
    @Nullable
    private LivingEntity caught = null;
    private int newtarget = 0;
    private int release = 0;
    private int weather_set = 10;
    private int long_enough = 3600;
    private int call_reinforcements = 0;
    private boolean hit_by_player = false;
    private int straight_down = 1;
    private int hurt_timer = 0;

    public Kraken(EntityType<? extends Kraken> type, Level level) {
        super(type, level);
        this.xpReward = GOLD_XP; // gold field_70728_aV = 500
        this.noCulling = true;
        this.renderdata = new RenderInfo();
        // gold field_70178_ae = true (fireImmune) — set via EntityType.Builder.fireImmune()
    }

    public static AttributeSupplier.Builder createAttributes() {
        // gold Kraken_stats defaults: health 1000, defense 40, attack 10
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, GOLD_HEALTH)
                .add(Attributes.MOVEMENT_SPEED, GOLD_SPEED)
                .add(Attributes.ATTACK_DAMAGE, GOLD_ATTACK)
                .add(Attributes.ARMOR, GOLD_ARMOR)
                .add(Attributes.FOLLOW_RANGE, GOLD_FOLLOW);
    }

    @Override
    protected void registerGoals() {
        // gold: LookIdle + HurtByTarget only (flight AI in customServerAiStep)
        this.goalSelector.addGoal(1, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(2, new LookAtPlayerGoal(this, Player.class, 16.0F));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_ATTACKING, (byte) 0);
        builder.define(DATA_PLAY_NICELY, OreSpawnMain.PlayNicely);
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

    public int getPlayNicely() {
        return this.entityData.get(DATA_PLAY_NICELY);
    }

    public int mygetMaxHealth() {
        return (int) GOLD_HEALTH;
    }

    public int getKrakenHealth() {
        return (int) this.getHealth();
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

    public final void setAttacking(int par1) {
        this.entityData.set(DATA_ATTACKING, (byte) par1);
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        // gold canDespawn
        if (this.isPersistenceRequired()) {
            return false;
        }
        if (this.long_enough <= 0) {
            return true;
        }
        if (this.getY() > 150.0 && this.getHealth() < this.mygetMaxHealth() / 2.0F) {
            return true;
        }
        if (this.getY() > 180.0 && this.long_enough <= 0) {
            this.discard();
            return true;
        }
        return false;
    }

    @Override
    public void aiStep() {
        super.aiStep();
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.isDeadOrDying()) {
            if (this.currentFlightTarget == null) {
                this.currentFlightTarget =
                        BlockPos.containing(this.getX(), this.getY() - 10.0, this.getZ());
            } else if (this.getY() < this.currentFlightTarget.getY()) {
                Vec3 m = this.getDeltaMovement();
                this.setDeltaMovement(m.x, m.y * 0.72, m.z);
            } else {
                Vec3 m = this.getDeltaMovement();
                this.setDeltaMovement(m.x, m.y * 0.5, m.z);
            }

            // gold weather force every ~100t when PlayNicely==0
            if (this.weather_set > 0 && OreSpawnMain.PlayNicely == 0) {
                this.weather_set--;
                if (this.weather_set == 0 && !this.level().isClientSide) {
                    if (this.level() instanceof ServerLevel server) {
                        if (server.getLevelData() instanceof ServerLevelData worldinfo) {
                            if (!server.isRaining()) {
                                worldinfo.setRaining(true);
                                worldinfo.setThundering(true);
                                worldinfo.setRainTime(300);
                                worldinfo.setThunderTime(300);
                            } else {
                                worldinfo.setRainTime(300);
                                worldinfo.setThunderTime(300);
                            }
                        }
                    }
                    this.weather_set = 100;
                }
            }
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("LongEnough", this.long_enough);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.contains("LongEnough")) {
            this.long_enough = tag.getInt("LongEnough");
        }
    }

    @Override
    protected SoundEvent getAmbientSound() {
        // gold: 1/5 orespawn:kraken_living
        if (this.random.nextInt(5) == 0) {
            return SoundEvents.ELDER_GUARDIAN_AMBIENT; // stand-in until SoundsHandler wires kraken_living
        }
        return null;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return null;
    }

    @Override
    protected SoundEvent getDeathSound() {
        // gold orespawn:alo_death
        return SoundEvents.GENERIC_DEATH;
    }

    @Override
    protected float getSoundVolume() {
        return 2.0F;
    }

    @Override
    public float getVoicePitch() {
        return 1.0F;
    }

    @Override
    protected void dropCustomDeathLoot(ServerLevel level, DamageSource damageSource, boolean recentlyHit) {
        // gold: KrakenTooth, nether_star, 120+r160 dyes, random gear rolls
        this.spawnAtLocation(new ItemStack(ModItems.KRAKEN_TOOTH.get()));
        this.spawnAtLocation(new ItemStack(Items.NETHER_STAR));
        int dyes = 120 + this.random.nextInt(160);
        for (int i = 0; i < dyes; i++) {
            this.spawnAtLocation(new ItemStack(Items.BLACK_DYE));
        }
        int rolls = 5 + this.random.nextInt(10);
        for (int r = 0; r < rolls; r++) {
            dropRandomLoot(level);
        }
    }

    /** Gold drop table cases 0–52 simplified with available ModItems / vanilla. */
    private void dropRandomLoot(ServerLevel level) {
        int var3 = this.random.nextInt(53);
        switch (var3) {
            case 0 -> this.spawnAtLocation(new ItemStack(ModItems.ULTIMATE_SWORD.get()));
            case 1 -> this.spawnAtLocation(new ItemStack(Items.DIAMOND));
            case 2 -> this.spawnAtLocation(new ItemStack(Blocks.DIAMOND_BLOCK));
            case 3 -> this.spawnAtLocation(new ItemStack(Items.DIAMOND_SWORD));
            case 4 -> this.spawnAtLocation(new ItemStack(Items.DIAMOND_SHOVEL));
            case 5 -> this.spawnAtLocation(new ItemStack(Items.DIAMOND_PICKAXE));
            case 6 -> this.spawnAtLocation(new ItemStack(Items.DIAMOND_AXE));
            case 7 -> this.spawnAtLocation(new ItemStack(Items.DIAMOND_HOE));
            case 8 -> this.spawnAtLocation(new ItemStack(Items.DIAMOND_HELMET));
            case 9 -> this.spawnAtLocation(new ItemStack(Items.DIAMOND_CHESTPLATE));
            case 10 -> this.spawnAtLocation(new ItemStack(Items.DIAMOND_LEGGINGS));
            case 11 -> this.spawnAtLocation(new ItemStack(Items.DIAMOND_BOOTS));
            case 12 -> this.spawnAtLocation(new ItemStack(ModItems.ULTIMATE_BOW.get()));
            case 13 -> this.spawnAtLocation(new ItemStack(ModItems.ULTIMATE_AXE.get()));
            case 14 -> this.spawnAtLocation(new ItemStack(Items.IRON_INGOT));
            case 15 -> this.spawnAtLocation(new ItemStack(ModItems.ULTIMATE_PICKAXE.get()));
            case 16 -> this.spawnAtLocation(new ItemStack(Items.IRON_SWORD));
            case 17 -> this.spawnAtLocation(new ItemStack(Items.IRON_SHOVEL));
            case 18 -> this.spawnAtLocation(new ItemStack(Items.IRON_PICKAXE));
            case 19 -> this.spawnAtLocation(new ItemStack(Items.IRON_AXE));
            case 20 -> this.spawnAtLocation(new ItemStack(Items.IRON_HOE));
            case 21 -> this.spawnAtLocation(new ItemStack(Items.IRON_HELMET));
            case 22 -> this.spawnAtLocation(new ItemStack(Items.IRON_CHESTPLATE));
            case 23 -> this.spawnAtLocation(new ItemStack(Items.IRON_LEGGINGS));
            case 24 -> this.spawnAtLocation(new ItemStack(Items.IRON_BOOTS));
            case 25 -> this.spawnAtLocation(new ItemStack(ModItems.ULTIMATE_SHOVEL.get()));
            case 26 -> this.spawnAtLocation(new ItemStack(Blocks.GOLD_BLOCK));
            case 27 -> this.spawnAtLocation(new ItemStack(Items.GOLD_NUGGET));
            case 28 -> this.spawnAtLocation(new ItemStack(Items.GOLD_INGOT));
            case 29 -> this.spawnAtLocation(new ItemStack(Items.EMERALD));
            case 30 -> this.spawnAtLocation(new ItemStack(Items.GOLDEN_SWORD));
            case 31 -> this.spawnAtLocation(new ItemStack(Items.GOLDEN_SHOVEL));
            case 32 -> this.spawnAtLocation(new ItemStack(Items.GOLDEN_PICKAXE));
            case 33 -> this.spawnAtLocation(new ItemStack(Items.GOLDEN_AXE));
            case 34 -> this.spawnAtLocation(new ItemStack(Items.GOLDEN_HOE));
            case 35 -> this.spawnAtLocation(new ItemStack(Items.GOLDEN_HELMET));
            case 36 -> this.spawnAtLocation(new ItemStack(Items.GOLDEN_CHESTPLATE));
            case 37 -> this.spawnAtLocation(new ItemStack(Items.GOLDEN_LEGGINGS));
            case 38 -> this.spawnAtLocation(new ItemStack(Items.GOLDEN_BOOTS));
            case 39 -> this.spawnAtLocation(new ItemStack(Items.GOLDEN_APPLE));
            case 40 -> this.spawnAtLocation(new ItemStack(Blocks.EMERALD_BLOCK));
            case 41 -> this.spawnAtLocation(new ItemStack(Items.ENCHANTED_GOLDEN_APPLE));
            case 42 -> this.spawnAtLocation(new ItemStack(ModItems.EXPERIENCE_SWORD.get()));
            case 47 -> this.spawnAtLocation(new ItemStack(ModItems.AMETHYST_SWORD.get()));
            case 48 -> this.spawnAtLocation(new ItemStack(ModItems.AMETHYST_SHOVEL.get()));
            case 49 -> this.spawnAtLocation(new ItemStack(ModItems.AMETHYST_PICKAXE.get()));
            case 50 -> this.spawnAtLocation(new ItemStack(ModItems.AMETHYST_AXE.get()));
            case 51 -> this.spawnAtLocation(new ItemStack(ModItems.AMETHYST_HOE.get()));
            case 52 -> this.spawnAtLocation(new ItemStack(ModItems.AMETHYST.get(), 4));
            default -> {
            }
        }
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    protected void doPush(Entity entity) {}

    @Override
    public boolean causeFallDamage(float fallDistance, float multiplier, DamageSource source) {
        return false; // gold fall empty
    }

    @Override
    protected void checkFallDamage(double y, boolean onGround, BlockState state, BlockPos pos) {
        // gold no fall / walk damage
    }

    /** Gold canSeeTarget — ray from eyes y+0.75. */
    public boolean canSeeTarget(double pX, double pY, double pZ) {
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
        if (this.isDeadOrDying()) {
            return;
        }
        super.customServerAiStep();

        if (this.hurt_timer > 0) {
            this.hurt_timer--;
        }
        if (this.long_enough > 0) {
            this.long_enough--;
        }

        // gold sync PlayNicely each tick
        if (!this.level().isClientSide) {
            this.entityData.set(DATA_PLAY_NICELY, OreSpawnMain.PlayNicely);
        }

        // gold random lightning under body
        if (this.random.nextInt(400) == 1
                && OreSpawnMain.PlayNicely == 0
                && this.level() instanceof ServerLevel server) {
            LightningBolt bolt = EntityType.LIGHTNING_BOLT.create(server);
            if (bolt != null) {
                bolt.moveTo(this.getX(), this.getY() - 16.0, this.getZ());
                server.addFreshEntity(bolt);
            }
        }

        if (this.currentFlightTarget == null) {
            this.currentFlightTarget = BlockPos.containing(this.getX(), this.getY(), this.getZ());
        }

        if (this.newtarget != 0
                || this.random.nextInt(250) == 1
                || this.currentFlightTarget.distToCenterSqr(this.getX(), this.getY(), this.getZ()) < 9.1F) {
            this.newtarget = 0;

            int ground_dist;
            for (ground_dist = 0; ground_dist < 31; ground_dist++) {
                BlockState bid = this.level()
                        .getBlockState(BlockPos.containing(this.getX(), this.getY() - ground_dist, this.getZ()));
                if (!bid.isAir()) {
                    this.straight_down = 0;
                    break;
                }
            }
            ground_dist = 20 - ground_dist;

            int keep_trying = 50;
            BlockState bid = Blocks.STONE.defaultBlockState();
            int xdir = 1;
            int zdir = 1;
            while (!bid.isAir() && keep_trying != 0) {
                keep_trying--;
                zdir = this.random.nextInt(6) + 12;
                xdir = this.random.nextInt(6) + 12;
                if (this.random.nextInt(2) == 0) {
                    zdir = -zdir;
                }
                if (this.random.nextInt(2) == 0) {
                    xdir = -xdir;
                }
                if (this.straight_down != 0) {
                    xdir = 0;
                    zdir = 0;
                }
                this.currentFlightTarget = BlockPos.containing(
                        this.getX() + xdir,
                        this.getY() + ground_dist + this.random.nextInt(9) - 6,
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

            // flee upward when time up or low HP
            if (this.long_enough <= 0
                    || (this.getY() < 200.0 && this.getHealth() < this.mygetMaxHealth() / 4.0F)) {
                this.currentFlightTarget = this.currentFlightTarget.above(30);
                if (this.hit_by_player
                        && this.call_reinforcements == 0
                        && this.getHealth() < this.mygetMaxHealth() / 8.0F
                        && this.getY() > 130.0) {
                    this.call_reinforcements = 1;
                    // gold: spawnCreature "The Kraken" x10 at y=170 — KRAKEN entity type must be registered first
                    // Deferred: cannot spawn self type without ModEntities.KRAKEN; leave note until KRAKEN registration is used here.
                }
            }
        } else if (this.caught == null && this.random.nextInt(8) == 1 && OreSpawnMain.PlayNicely == 0) {
            Player target = this.level().getNearestPlayer(this, 25.0);
            // gold also expand Y 40 — approximate with larger box check below if needed
            if (target != null) {
                if (!target.getAbilities().instabuild && !target.isSpectator()) {
                    if (this.hasLineOfSight(target)
                            && Math.abs(target.getY() - this.getY()) < 40.0) {
                        this.currentFlightTarget =
                                BlockPos.containing(target.getX(), target.getY() + 15, target.getZ());
                        this.attackWithSomething(target);
                    }
                } else {
                    target = null;
                }
            }
            if (target == null && this.random.nextInt(2) == 0) {
                LivingEntity e = this.findSomethingToAttack();
                if (e != null) {
                    this.currentFlightTarget = BlockPos.containing(e.getX(), e.getY() + 15, e.getZ());
                    this.attackWithSomething(e);
                }
            }
        }

        // hold victim and lift to y~200
        if (this.caught != null) {
            if (this.caught.isAlive()) {
                this.currentFlightTarget = BlockPos.containing(this.getX(), 200, this.getZ());
                if (this.getY() > 190.0) {
                    this.release = 1;
                }
                Vec3 km = this.getDeltaMovement();
                this.caught.setDeltaMovement(km.x, km.y, km.z);
                this.caught.setPos(this.getX(), this.caught.getY(), this.getZ());
                if (this.getY() - this.caught.getY() > 16.0) {
                    Vec3 cm = this.caught.getDeltaMovement();
                    this.caught.setDeltaMovement(cm.x, cm.y + 0.25, cm.z);
                }
                this.caught.setPos(this.getX(), this.getY() - 15.0, this.getZ());
                this.caught.setYRot(this.getYRot());
                if (this.random.nextInt(50) == 1) {
                    this.doHurtTarget(this.caught);
                }
                if (this.release != 0 || this.random.nextInt(250) == 1) {
                    this.caught = null;
                    this.newtarget = 1;
                    this.release = 0;
                    this.setAttacking(0);
                }
            } else {
                this.caught = null;
                this.newtarget = 1;
                this.release = 0;
                this.setAttacking(0);
            }
        }

        // flight velocity toward target
        double var1 = this.currentFlightTarget.getX() + 0.3 - this.getX();
        double var3 = this.currentFlightTarget.getY() + 0.1 - this.getY();
        double var5 = this.currentFlightTarget.getZ() + 0.3 - this.getZ();
        Vec3 m = this.getDeltaMovement();
        this.setDeltaMovement(
                m.x + (Math.signum(var1) * 0.45 - m.x) * 0.15,
                m.y + (Math.signum(var3) * 0.70999 - m.y) * 0.202,
                m.z + (Math.signum(var5) * 0.45 - m.z) * 0.15);
        float var7 = (float) (Math.atan2(this.getDeltaMovement().z, this.getDeltaMovement().x) * 180.0 / Math.PI)
                - 90.0F;
        float var8 = Mth.wrapDegrees(var7 - this.getYRot());
        this.setZza(0.4F);
        if (Math.abs(this.getDeltaMovement().x) + Math.abs(this.getDeltaMovement().z) < 0.15) {
            var8 = 0.0F;
        }
        this.setYRot(this.getYRot() + var8 / 5.0F);

        // obstruction climb
        double obstruction_factor = 0.0;
        int dist = 10;
        for (int k = -20; k < 18; k += 2) {
            for (int i = 1; i < dist; i += 2) {
                double dx = i * Math.cos(Math.toRadians(this.getYRot() + 90.0F));
                double dz = i * Math.sin(Math.toRadians(this.getYRot() + 90.0F));
                BlockState block = this.level()
                        .getBlockState(BlockPos.containing(this.getX() + dx, this.getY() + k, this.getZ() + dz));
                if (!block.isAir()) {
                    obstruction_factor += 0.1;
                }
            }
        }
        Vec3 m2 = this.getDeltaMovement();
        this.setDeltaMovement(m2.x, m2.y + obstruction_factor * 0.08, m2.z);
        this.setPos(this.getX(), this.getY() + obstruction_factor * 0.08, this.getZ());

        if (this.getY() > 256.0 && !this.isPersistenceRequired()) {
            this.discard();
        }
    }

    private void attackWithSomething(LivingEntity par1) {
        if (this.caught == null) {
            double dist = (this.getX() - par1.getX()) * (this.getX() - par1.getX());
            dist += (this.getZ() - par1.getZ()) * (this.getZ() - par1.getZ());
            dist += (this.getY() - par1.getY() - 15.0) * (this.getY() - par1.getY() - 15.0);
            if (dist < 30.0) {
                this.caught = par1;
                this.release = 0;
                this.setAttacking(1);
            }
        }
    }

    private boolean isSuitableTarget(LivingEntity par1EntityLiving) {
        if (par1EntityLiving == null || par1EntityLiving == this || !par1EntityLiving.isAlive()) {
            return false;
        }
        // gold MyUtils.isIgnoreable deferred
        if (!this.hasLineOfSight(par1EntityLiving)) {
            return false;
        }
        if (par1EntityLiving instanceof Player p) {
            if (p.getAbilities().instabuild || p.isSpectator()) {
                return false;
            }
            return !p.getAbilities().flying; // gold field_75100_b flying
        }
        if (!par1EntityLiving.onGround() && !par1EntityLiving.isInWater()) {
            return false;
        }
        if (par1EntityLiving instanceof Squid) {
            return false;
        }
        if (par1EntityLiving instanceof AttackSquid) {
            return false;
        }
        if (par1EntityLiving instanceof Kraken) {
            return false;
        }
        if (par1EntityLiving instanceof Spyro) {
            return false;
        }
        if (par1EntityLiving instanceof Dragon dragon) {
            return !dragon.isVehicle();
        }
        if (par1EntityLiving instanceof Cephadrome ceph) {
            return !ceph.isVehicle();
        }
        if (par1EntityLiving instanceof Leon leon) {
            return !leon.isVehicle();
        }
        if (par1EntityLiving instanceof ThePrinceTeen teen) {
            return !teen.isVehicle();
        }
        if (par1EntityLiving instanceof ThePrinceAdult adult) {
            return !adult.isVehicle();
        }
        if (par1EntityLiving instanceof Chicken) {
            return false;
        }
        if (par1EntityLiving instanceof Chipmunk) {
            return false;
        }
        if (par1EntityLiving instanceof StinkBug) {
            return false;
        }
        if (par1EntityLiving instanceof Mothra) {
            return false;
        }
        return true;
    }

    @Nullable
    private LivingEntity findSomethingToAttack() {
        if (OreSpawnMain.PlayNicely != 0) {
            return null;
        }
        AABB box = this.getBoundingBox().inflate(20.0, 40.0, 20.0);
        List<LivingEntity> list = this.level().getEntitiesOfClass(LivingEntity.class, box);
        list.sort(Comparator.comparingDouble(this::distanceToSqr));
        for (LivingEntity e : list) {
            if (this.isSuitableTarget(e)) {
                return e;
            }
        }
        return null;
    }

    @Override
    public void thunderHit(ServerLevel level, LightningBolt lightning) {
        // gold immune to lightning
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        Entity e = source.getEntity();
        if (this.currentFlightTarget != null
                && e instanceof Player
                && this.getHealth() > this.mygetMaxHealth() / 4.0F) {
            this.hit_by_player = true;
            this.currentFlightTarget = BlockPos.containing(e.getX(), e.getY() + 15, e.getZ());
        }
        if (this.hurt_timer > 0) {
            return false;
        }
        this.hurt_timer = 30;
        boolean ret = super.hurt(source, amount);
        if (this.random.nextInt(2) == 1) {
            this.release = 1;
        }
        return ret;
    }

    @Override
    public boolean doHurtTarget(Entity target) {
        return GoldStyleCombat.dealAttributeDamage(this, target);
    }

    /**
     * Gold {@code getCanSpawnHere}: y ≥ 50, headroom air/grass clear.
     */
    @Override
    public boolean checkSpawnRules(LevelAccessor level, MobSpawnType spawnType) {
        if (this.getY() < 50.0) {
            return false;
        }
        for (int k = -1; k < 2; k++) {
            for (int j = -1; j < 1; j++) {
                for (int i = 1; i < 6; i++) {
                    BlockState bid = level.getBlockState(
                            BlockPos.containing(this.getX() + j, this.getY() + i, this.getZ() + k));
                    if (!bid.isAir() && !bid.is(Blocks.SHORT_GRASS) && !bid.is(Blocks.TALL_GRASS)) {
                        return false;
                    }
                }
            }
        }
        if (spawnType == MobSpawnType.SPAWNER
                || spawnType == MobSpawnType.SPAWN_EGG
                || spawnType == MobSpawnType.COMMAND) {
            return true;
        }
        return super.checkSpawnRules(level, spawnType);
    }
}
