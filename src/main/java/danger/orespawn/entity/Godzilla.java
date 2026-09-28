package danger.orespawn.entity;

import danger.orespawn.OreSpawnMain;
import danger.orespawn.init.ModItems;
import danger.orespawn.util.ai.GoldStyleCombat;
import danger.orespawn.util.ai.WanderALotGoal;
import java.util.Comparator;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.entity.monster.Spider;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

/**
 * Gold {@code Godzilla} / Mobzilla (EntityMob) 1:1 for NeoForge 1.21.1.
 * Size 9.9×25.0 (PlayNicely 2.475×6.25), speed 0.75, health 4000, attack 21, armor 175, XP 10000.
 * Fire immune; jump stomp, block crush, BetterFireball stream, lightning strike; RenderInfo arms/jaw.
 * Texture: {@code textures/entity/godzillatexture.png}. Registry size/attrs set in {@code ModEntities}.
 * Spawns {@link GodzillaHead} companion via registry name {@code orespawn:godzilla_head} once the entity type is registered.
 */
public class Godzilla extends Monster {
    private static final EntityDataAccessor<Byte> ATTACKING =
            SynchedEntityData.defineId(Godzilla.class, EntityDataSerializers.BYTE);
    private static final EntityDataAccessor<Integer> PLAY_NICELY =
            SynchedEntityData.defineId(Godzilla.class, EntityDataSerializers.INT);

    /** Gold Godzilla_stats defaults: health 4000, defense 175, attack 21. */
    public static final float GOLD_WIDTH = 9.9F;
    public static final float GOLD_HEIGHT = 25.0F;
    public static final float GOLD_WIDTH_PLAY_NICELY = 2.475F;
    public static final float GOLD_HEIGHT_PLAY_NICELY = 6.25F;
    public static final double GOLD_HEALTH = 4000.0;
    public static final double GOLD_SPEED = 0.75;
    public static final double GOLD_ATTACK = 21.0;
    public static final double GOLD_ARMOR = 175.0;
    public static final int GOLD_XP = 10000;
    public static final double GOLD_FOLLOW = 10000.0;

    /**
     * Gold {@code OreSpawnMain.godzilla_has_spawned}: blocks natural spawns while a Mobzilla is alive.
     * Stored as the game time a Mobzilla last ticked so the lock clears once it dies or unloads
     * (a plain static flag was never reset, so no second Mobzilla could ever spawn until restart).
     */
    private static long lastAliveGameTime = Long.MIN_VALUE;

    /** Ticks after the last living Mobzilla ticked before another may spawn naturally. */
    private static final long SPAWN_LOCK_TICKS = 1200L;

    private final float moveSpeed = 0.75F;
    private int hurtTimer;
    private int jumped;
    private int jumpTimer;
    private int ticker;
    private int streamCount = 8;
    private int headFound;
    private int largeUnknownDetected;
    private RenderInfo renderdata = new RenderInfo();
    private WanderALotGoal wander;

    public Godzilla(EntityType<? extends Godzilla> type, Level level) {
        super(type, level);
        this.xpReward = GOLD_XP;
        // Mesh is huge vs hitbox height — keep cull generous
        this.noCulling = true;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, GOLD_HEALTH)
                .add(Attributes.MOVEMENT_SPEED, GOLD_SPEED)
                .add(Attributes.ATTACK_DAMAGE, GOLD_ATTACK)
                .add(Attributes.ARMOR, GOLD_ARMOR)
                // Gold listed 10000; 1.21 pathfinding at that range stalls the server. Cap for ticks.add(Attributes.FOLLOW_RANGE, 64.0)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1.0);
    }

    @Override
    protected void registerGoals() {
        // gold: Swimming, MoveThroughVillage(1.0,false) skipped, WanderALot(15,1),
        // WatchClosest(Living,50) ~ LookAtPlayer 50, LookIdle, HurtByTarget
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.wander = new WanderALotGoal(this, 15, 1.0);
        this.goalSelector.addGoal(2, this.wander);
        this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 50.0F));
        this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(ATTACKING, (byte) 0);
        builder.define(PLAY_NICELY, OreSpawnMain.PlayNicely);
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

    public int getPlayNicely() {
        return this.entityData.get(PLAY_NICELY);
    }

    public int getAttacking() {
        return this.entityData.get(ATTACKING) & 0xFF;
    }

    public void setAttacking(int value) {
        this.entityData.set(ATTACKING, (byte) value);
    }

    public int mygetMaxHealth() {
        return (int) GOLD_HEALTH;
    }

    /** Gold field_70178_ae isImmuneToFire. */
    @Override
    public boolean fireImmune() {
        return true;
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        // gold canDespawn: isNoDespawnRequired ? false : PlayNicely != 0
        if (this.isPersistenceRequired()) {
            return false;
        }
        return OreSpawnMain.PlayNicely != 0;
    }

    @Override
    public void tick() {
        if (this.getAttribute(Attributes.MOVEMENT_SPEED) != null) {
            this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(this.moveSpeed);
        }
        super.tick();
        if (this.onGround()) {
            // gold: when onGround clear path — keep pathfinder active via goals
        }
    }

    /** Gold jump: yaw normalize, motionY += 0.45, posY += 0.5, forward impulse. */
    @Override
    public void jumpFromGround() {
        float yaw = this.getYRot();
        while (yaw < 0.0F) {
            yaw += 360.0F;
        }
        while (yaw > 360.0F) {
            yaw -= 360.0F;
        }
        float body = this.yBodyRot;
        while (body < 0.0F) {
            body += 360.0F;
        }
        while (body > 360.0F) {
            body -= 360.0F;
        }
        this.setYRot(yaw);
        this.yBodyRot = body;

        Vec3 motion = this.getDeltaMovement();
        float f = 0.2F + Math.abs(this.random.nextFloat() * 0.45F);
        double dx = f * Math.cos(Math.toRadians(body + 90.0F));
        double dz = f * Math.sin(Math.toRadians(body + 90.0F));
        this.setDeltaMovement(motion.x + dx, motion.y + 0.45, motion.z + dz);
        this.setPos(this.getX(), this.getY() + 0.5, this.getZ());
        this.setOnGround(false);
        this.getNavigation().stop();
    }

    protected void jumpAtEntity(LivingEntity e) {
        Vec3 motion = this.getDeltaMovement();
        double d1 = e.getX() - this.getX();
        double d2 = e.getZ() - this.getZ();
        float d = (float) Math.atan2(d2, d1);
        float f2 = (float) (d * 180.0 / Math.PI) - 90.0F;
        this.setYRot(f2);
        double dist = Math.sqrt(d1 * d1 + d2 * d2);
        this.setDeltaMovement(
                motion.x + dist * 0.05 * Math.cos(d),
                motion.y + 1.25,
                motion.z + dist * 0.05 * Math.sin(d));
        this.setPos(this.getX(), this.getY() + 1.55, this.getZ());
        this.setOnGround(false);
        this.getNavigation().stop();
    }

    private double getHorizontalDistanceSqToEntity(Entity e) {
        double d1 = e.getZ() - this.getZ();
        double d2 = e.getX() - this.getX();
        return d1 * d1 + d2 * d2;
    }

    /** Gold {@code MygetDistanceSqToEntity}: compress Y within 0.20 to 0, >20 subtract 10. */
    public double myGetDistanceSqToEntity(Entity par1Entity) {
        double d0 = this.getX() - par1Entity.getX();
        double d1 = par1Entity.getY() - this.getY();
        double d2 = this.getZ() - par1Entity.getZ();
        if (d1 > 0.0 && d1 < 20.0) {
            d1 = 0.0;
        }
        if (d1 > 20.0) {
            d1 -= 10.0;
        }
        return d0 * d0 + d1 * d1 + d2 * d2;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        //  gold 1/5 orespawn:godzilla_living
        return null;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        //  gold orespawn:alo_hurt
        return null;
    }

    @Override
    protected SoundEvent getDeathSound() {
        //  gold orespawn:godzilla_death
        return null;
    }

    @Override
    protected float getSoundVolume() {
        return 1.65F;
    }

    @Override
    public float getVoicePitch() {
        return 1.1F;
    }

    /** Gold ignore fall. */
    @Override
    public boolean causeFallDamage(float fallDistance, float multiplier, DamageSource source) {
        return false;
    }

    @Override
    protected void checkFallDamage(double y, boolean onGround, BlockState state, BlockPos pos) {
        // gold empty checkFallDamage
    }

    private ItemStack dropItemRand(Item index, int count) {
        ItemStack stack = new ItemStack(index, count);
        ItemEntity item = new ItemEntity(
                this.level(),
                this.getX() + this.random.nextInt(10) - this.random.nextInt(10),
                this.getY() + 4.0 + this.random.nextInt(10),
                this.getZ() + this.random.nextInt(10) - this.random.nextInt(10),
                stack);
        this.level().addFreshEntity(item);
        return stack;
    }

    private ItemStack dropItemRandAt(Item index, int count, double dx, double dz) {
        ItemStack stack = new ItemStack(index, count);
        ItemEntity item = new ItemEntity(
                this.level(),
                dx + this.random.nextInt(10) - this.random.nextInt(10),
                this.getY() + 4.0 + this.random.nextInt(6),
                dz + this.random.nextInt(10) - this.random.nextInt(10),
                stack);
        this.level().addFreshEntity(item);
        return stack;
    }

    @Override
    protected void dropCustomDeathLoot(ServerLevel level, DamageSource damageSource, boolean recentlyHit) {
        // gold dropFewItems only — no vanilla loot table junk
        // gold: item_frame x1
        this.dropItemRand(Items.ITEM_FRAME, 1);
        // gold MyGodzillaScale 50+rand(30)
        int var5 = 50 + this.random.nextInt(30);
        for (int i = 0; i < var5; i++) {
            this.dropItemRand(ModItems.GODZILLA_SCALE.get(), 1);
        }
        // gold rotten flesh 100+rand(160)
        var5 = 100 + this.random.nextInt(160);
        for (int i = 0; i < var5; i++) {
            this.dropItemRand(Items.ROTTEN_FLESH, 1);
        }
        // gold bones 50+rand(60)
        var5 = 50 + this.random.nextInt(60);
        for (int i = 0; i < var5; i++) {
            this.dropItemRand(Items.BONE, 1);
        }
        // gold 25+rand(15) rolls of gear table (subset ported)
        int rolls = 25 + this.random.nextInt(15);
        for (int i = 0; i < rolls; i++) {
            int pick = this.random.nextInt(80);
            switch (pick) {
                case 0 -> this.dropItemRand(ModItems.ULTIMATE_SWORD.get(), 1);
                case 1 -> this.dropItemRand(Items.DIAMOND, 1);
                case 2 -> this.dropItemRand(Items.DIAMOND_BLOCK, 1);
                case 3 -> this.dropItemRand(Items.IRON_SWORD, 1);
                case 4 -> this.dropItemRand(Items.IRON_SHOVEL, 1);
                case 5 -> this.dropItemRand(Items.IRON_PICKAXE, 1);
                case 6 -> this.dropItemRand(Items.IRON_AXE, 1);
                case 7 -> this.dropItemRand(Items.IRON_HOE, 1);
                case 8 -> this.dropItemRand(Items.IRON_HELMET, 1);
                case 9 -> this.dropItemRand(Items.IRON_CHESTPLATE, 1);
                case 10 -> this.dropItemRand(Items.IRON_LEGGINGS, 1);
                case 11 -> this.dropItemRand(Items.IRON_BOOTS, 1);
                case 12 -> this.dropItemRand(ModItems.ULTIMATE_BOW.get(), 1);
                case 13 -> this.dropItemRand(ModItems.ULTIMATE_AXE.get(), 1);
                case 14 -> this.dropItemRand(Items.IRON_INGOT, 1);
                case 15 -> this.dropItemRand(ModItems.ULTIMATE_PICKAXE.get(), 1);
                case 16 -> this.dropItemRand(Items.GOLDEN_SWORD, 1);
                case 25 -> this.dropItemRand(ModItems.ULTIMATE_SHOVEL.get(), 1);
                case 26 -> this.dropItemRand(Items.GOLD_BLOCK, 1);
                case 27 -> this.dropItemRand(Items.GOLD_NUGGET, 1);
                case 28 -> this.dropItemRand(Items.GOLD_INGOT, 1);
                case 29 -> this.dropItemRand(Items.EMERALD, 1);
                default -> {
                }
            }
        }
    }

    /**
     * Gold {@code updateAITasks}: ticker, jump stomp, block crush front+body, hunt, firecanon,
     * lightning, heal.
     */
    @Override
    protected void customServerAiStep() {
        if (this.isDeadOrDying()) {
            return;
        }
        if (this.level().isClientSide) {
            return;
        }

        this.entityData.set(PLAY_NICELY, OreSpawnMain.PlayNicely);
        super.customServerAiStep();

        this.ticker++;
        if (this.ticker > 30000) {
            this.ticker = 0;
        }
        if (this.ticker % 100 == 0) {
            this.streamCount = 8;
        }
        if (this.hurtTimer > 0) {
            this.hurtTimer--;
        }
        if (this.jumpTimer > 0) {
            this.jumpTimer--;
        }

        lastAliveGameTime = this.level().getGameTime();

        if (this.random.nextInt(200) == 0) {
            this.setTarget(null);
        }

        // armor: large_unknown_detected → 25 else 175
        if (this.getAttribute(Attributes.ARMOR) != null) {
            this.getAttribute(Attributes.ARMOR)
                    .setBaseValue(this.largeUnknownDetected != 0 ? 25.0 : GOLD_ARMOR);
        }

        if (OreSpawnMain.PlayNicely == 0) {
            double my = this.getDeltaMovement().y;
            if (my < -0.95) {
                this.jumped = 1;
            }
            if (my < -1.5) {
                this.jumped = 2;
            }
            if (this.jumped != 0 && my > -0.1) {
                double df = this.jumped == 2 ? 1.5 : 1.0;
                this.doJumpDamage(this.getX(), this.getY(), this.getZ(), 10.0, GOLD_ATTACK * df, 0);
                this.doJumpDamage(this.getX(), this.getY(), this.getZ(), 15.0, GOLD_ATTACK / 2 * df, 0);
                this.doJumpDamage(this.getX(), this.getY(), this.getZ(), 25.0, GOLD_ATTACK / 4 * df, 0);
                this.jumped = 0;
            }
        }

        // Gold crushed every tick at two radii — ~2k setBlock/tick freezes 1.21. One pass/tick, thinner ring.
        int var23 = this.getAttacking() != 0 ? 10 : 8;
        if (OreSpawnMain.PlayNicely == 0) {
            if ((this.ticker & 1) == 0) {
                int k = -3 + this.ticker % 30;
                this.crushBlocksAround((int) this.getX(), (int) this.getY() + k, (int) this.getZ(), var23, false);
            } else {
                double dx = this.getX() + 16.0 * Math.sin(Math.toRadians(this.yBodyRot));
                double dz = this.getZ() - 16.0 * Math.cos(Math.toRadians(this.yBodyRot));
                int k = -3 + this.ticker % 12;
                this.crushBlocksAround((int) dx, (int) this.getY() + k, (int) dz, var23, true);
                if (k == 0) {
                    this.doJumpDamage(dx, this.getY(), dz, 15.0, GOLD_ATTACK / 2, 1);
                }
            }
        }

        // Target search is expensive (64×40×64 LivingEntity + LoS); gold rolled 1/5, we also skip if busy.
        int huntChance = Math.max(1, 8 - this.largeUnknownDetected * 2);
        if (this.random.nextInt(huntChance) == 1) {
            LivingEntity e = this.getTarget();
            if (OreSpawnMain.PlayNicely != 0) {
                e = null;
            }
            if (e != null) {
                if (!e.isAlive()) {
                    this.setTarget(null);
                    e = null;
                } else if (e instanceof Godzilla || e instanceof GodzillaHead) {
                    this.setTarget(null);
                    e = null;
                }
            }
            if (e == null) {
                e = this.findSomethingToAttack();
                // gold: spawn MobzillaHead when head_found==0
                if (this.headFound == 0) {
                    this.spawnGodzillaHead();
                }
            }
            if (e != null) {
                if (this.wander != null) {
                    this.wander.setBusy(1);
                }
                this.getLookControl().setLookAt(e, 10.0F, 10.0F);
                if (this.random.nextInt(65) == 1 && this.myGetDistanceSqToEntity(e) > 300.0) {
                    this.doLightningAttack(e);
                } else if (this.random.nextInt(20 - this.largeUnknownDetected * 5) == 1 && this.jumpTimer == 0) {
                    this.jumpAtEntity(e);
                    this.jumpTimer = 30;
                } else if (this.myGetDistanceSqToEntity(e)
                        < 300.0F + (e.getBbWidth() / 2.0F) * (e.getBbWidth() / 2.0F)) {
                    this.setAttacking(1);
                    this.getNavigation().moveTo(e, 1.0);
                    if (this.random.nextInt(4 - this.largeUnknownDetected) == 0
                            || this.random.nextInt(3 - this.largeUnknownDetected) == 1) {
                        this.doHurtTarget(e);
                    }
                } else {
                    this.getNavigation().moveTo(e, 1.0);
                    if (this.getHorizontalDistanceSqToEntity(e) > 625.0) {
                        if (this.streamCount > 0) {
                            this.setAttacking(1);
                            double rr = Math.atan2(e.getZ() - this.getZ(), e.getX() - this.getX());
                            double rhdir = Math.toRadians((this.yBodyRot + 90.0F) % 360.0F);
                            double pi = 3.1415926545;
                            double rdd = Math.abs(rr - rhdir) % (pi * 2.0);
                            if (rdd > pi) {
                                rdd -= pi * 2.0;
                            }
                            rdd = Math.abs(rdd);
                            if (rdd < 0.5) {
                                this.firecanon(e);
                            }
                        } else {
                            this.setAttacking(0);
                        }
                    } else {
                        this.setAttacking(0);
                    }
                }
            } else {
                this.setAttacking(0);
                if (this.wander != null) {
                    this.wander.setBusy(0);
                }
                this.streamCount = 8;
            }
        }

        if (this.random.nextInt(35) == 1 && this.getHealth() < this.mygetMaxHealth()) {
            this.heal(5.0F);
        }
    }

    /**
     * Crush one horizontal ring of blocks. Uses client-only block flags so bulk grief
     * does not run full neighbor/light updates (main 1.21 tick stall).
     */
    private void crushBlocksAround(int cx, int cy, int cz, int range, boolean frontDrop) {
        if (!this.level().getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING)) {
            return;
        }
        if (!(this.level() instanceof ServerLevel)) {
            return;
        }
        // 2 | 16 = notify clients, skip neighbor / observer storms
        final int flags = 2 | 16;
        for (int i = -range; i <= range; i++) {
            for (int j = -range; j <= range; j++) {
                BlockPos pos = new BlockPos(cx + i, cy, cz + j);
                BlockState state = this.level().getBlockState(pos);
                if (state.isAir()) {
                    continue;
                }
                Block bid = state.getBlock();
                if (this.isCrushable(state)) {
                    this.level().setBlock(pos, Blocks.AIR.defaultBlockState(), flags);
                    // rarer drops — item entities are another tick cost
                    if (this.random.nextInt(40) == 1) {
                        Item drop = bid.asItem();
                        if (drop != Items.AIR) {
                            if (frontDrop) {
                                this.dropItemRandAt(drop, 1, cx, cz);
                            } else {
                                this.dropItemRand(drop, 1);
                            }
                        }
                    }
                } else if (bid == Blocks.GRASS_BLOCK || bid == Blocks.FARMLAND) {
                    this.level().setBlock(pos, Blocks.DIRT.defaultBlockState(), flags);
                }
            }
        }
    }

    private boolean isCrushable(BlockState state) {
        if (state == null || state.isAir()) {
            return false;
        }
        Block bid = state.getBlock();
        // gold: leave grass/dirt/stone/farmland/water/lava/obsidian/bedrock/ores/orespawn gems
        if (bid == Blocks.GRASS_BLOCK
                || bid == Blocks.DIRT
                || bid == Blocks.STONE
                || bid == Blocks.FARMLAND
                || bid == Blocks.WATER
                || bid == Blocks.LAVA
                || bid == Blocks.BEDROCK
                || bid == Blocks.OBSIDIAN
                || bid == Blocks.SAND
                || bid == Blocks.GRAVEL
                || bid == Blocks.GOLD_BLOCK
                || bid == Blocks.DIAMOND_BLOCK
                || bid == Blocks.EMERALD_BLOCK
                || bid == Blocks.IRON_BLOCK
                || bid == Blocks.END_STONE
                || bid == Blocks.END_PORTAL_FRAME) {
            return false;
        }
        // Avoid getDestroySpeed every cell (expensive); skip unbreakable / very hard
        if (state.getDestroySpeed(this.level(), BlockPos.ZERO) < 0.0F) {
            return false;
        }
        return !state.is(Blocks.BEDROCK);
    }

    @Nullable
    private LivingEntity doJumpDamage(double X, double Y, double Z, double dist, double damage, int knock) {
        AABB bb = new AABB(X - dist, Y - 10.0, Z - dist, X + dist, Y + 10.0, Z + dist);
        List<LivingEntity> list = this.level().getEntitiesOfClass(LivingEntity.class, bb);
        list.sort(Comparator.comparingDouble(this::distanceToSqr));
        for (LivingEntity var4 : list) {
            if (var4 == null || var4 == this || !var4.isAlive()) {
                continue;
            }
            if (var4 instanceof Godzilla
                    || var4 instanceof GodzillaHead
                    || var4 instanceof Ghost
                    || var4 instanceof GhostSkelly) {
                continue;
            }
            // gold: DamageSource.magic half + generic half
            var4.hurt(this.damageSources().magic(), (float) damage / 2.0F);
            var4.hurt(this.damageSources().generic(), (float) damage / 2.0F);
            this.level()
                    .playSound(
                            null,
                            var4.getX(),
                            var4.getY(),
                            var4.getZ(),
                            SoundEvents.GENERIC_EXPLODE.value(),
                            this.getSoundSource(),
                            0.85F,
                            1.0F + (this.random.nextFloat() - this.random.nextFloat()) * 0.5F);
            if (knock != 0) {
                double ks = 3.5;
                double inair = 0.75;
                float f3 = (float) Math.atan2(var4.getZ() - this.getZ(), var4.getX() - this.getX());
                var4.push(Math.cos(f3) * ks, inair, Math.sin(f3) * ks);
            }
        }
        return null;
    }

    private boolean isSuitableTarget(LivingEntity par1EntityLiving) {
        if (par1EntityLiving == null || par1EntityLiving == this || !par1EntityLiving.isAlive()) {
            return false;
        }
        if (!this.hasLineOfSight(par1EntityLiving)) {
            return false;
        }
        if (par1EntityLiving instanceof Godzilla || par1EntityLiving instanceof GodzillaHead) {
            return false;
        }
        if (par1EntityLiving instanceof Creeper
                || par1EntityLiving instanceof Zombie
                || par1EntityLiving instanceof Spider
                || par1EntityLiving instanceof Skeleton) {
            return false;
        }
        if (par1EntityLiving instanceof Ghost || par1EntityLiving instanceof GhostSkelly) {
            return false;
        }
        if (par1EntityLiving instanceof Player p) {
            if (p.isSpectator() || p.getAbilities().instabuild) {
                return false;
            }
        }
        return true;
    }

    private boolean isVillagerTarget(LivingEntity par1EntityLiving) {
        if (par1EntityLiving == null || par1EntityLiving == this || !par1EntityLiving.isAlive()) {
            return false;
        }
        if (!this.hasLineOfSight(par1EntityLiving)) {
            return false;
        }
        return par1EntityLiving instanceof Villager;
    }

    @Nullable
    private LivingEntity findSomethingToAttack() {
        if (OreSpawnMain.PlayNicely != 0) {
            this.headFound = 1;
            return null;
        }
        // Smaller query than gold 64×40×64 — still huge, but less chunk entity iteration
        List<LivingEntity> var5 =
                this.level()
                        .getEntitiesOfClass(
                                LivingEntity.class, this.getBoundingBox().inflate(48.0, 24.0, 48.0));
        var5.sort(Comparator.comparingDouble(this::distanceToSqr));
        this.headFound = 0;
        LivingEntity ret = null;
        int vf = 0;
        int checked = 0;
        for (LivingEntity var4 : var5) {
            if (var4 instanceof GodzillaHead) {
                this.headFound = 1;
            }
            // Cap LoS checks (isSuitableTarget / isVillagerTarget call hasLineOfSight)
            if (checked > 24 && ret != null) {
                break;
            }
            checked++;
            if (vf == 0 && this.isVillagerTarget(var4)) {
                ret = var4;
                vf = 1;
            }
            if (ret == null && vf == 0 && this.isSuitableTarget(var4)) {
                ret = var4;
            }
        }
        return ret;
    }

    /**
     * Gold {@code spawnCreature(., "MobzillaHead", .)} — looks up registered
     * {@code orespawn:godzilla_head}. No-op until the entity type exists.
     */
    @Nullable
    private Entity spawnGodzillaHead() {
        ResourceLocation id = ResourceLocation.fromNamespaceAndPath("orespawn", "godzilla_head");
        EntityType<?> type = BuiltInRegistries.ENTITY_TYPE.getOptional(id).orElse(null);
        if (type == null) {
            return null;
        }
        Entity var8 = type.create(this.level());
        if (var8 != null) {
            var8.moveTo(this.getX(), this.getY() + 20.0, this.getZ(), this.random.nextFloat() * 360.0F, 0.0F);
            this.level().addFreshEntity(var8);
        }
        return var8;
    }

    private void firecanon(LivingEntity e) {
        double yoff = 19.0;
        double xzoff = 22.0;
        double cx = this.getX() - xzoff * Math.sin(Math.toRadians(this.getYRot()));
        double cz = this.getZ() + xzoff * Math.cos(Math.toRadians(this.getYRot()));
        if (this.streamCount <= 0) {
            return;
        }
        Vec3 aim = new Vec3(
                e.getX() - cx,
                e.getY() + e.getBbHeight() / 2.0F - (this.getY() + yoff),
                e.getZ() - cz);
        BetterFireball bf = new BetterFireball(this.level(), this, aim);
        bf.setPos(cx, this.getY() + yoff, cz);
        bf.setBig();
        this.level()
                .playSound(
                        null,
                        this.getX(),
                        this.getY(),
                        this.getZ(),
                        SoundEvents.TNT_PRIMED,
                        this.getSoundSource(),
                        1.0F,
                        1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
        this.level().addFreshEntity(bf);

        for (int i = 0; i < 5; i++) {
            float r1 = 5.0F * (this.random.nextFloat() - this.random.nextFloat());
            float r2 = 3.0F * (this.random.nextFloat() - this.random.nextFloat());
            float r3 = 5.0F * (this.random.nextFloat() - this.random.nextFloat());
            Vec3 aim2 = new Vec3(
                    e.getX() - cx + r1,
                    e.getY() + e.getBbHeight() / 2.0F - (this.getY() + yoff) + r2,
                    e.getZ() - cz + r3);
            BetterFireball bf2 = new BetterFireball(this.level(), this, aim2);
            bf2.setPos(cx, this.getY() + yoff, cz);
            if (this.random.nextInt(2) == 1) {
                bf2.setSmall();
            }
            this.level()
                    .playSound(
                            null,
                            this.getX(),
                            this.getY(),
                            this.getZ(),
                            SoundEvents.ARROW_SHOOT,
                            this.getSoundSource(),
                            1.0F,
                            1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
            this.level().addFreshEntity(bf2);
        }
        this.streamCount--;
    }

    private void doLightningAttack(LivingEntity e) {
        if (e == null) {
            return;
        }
        e.hurt(this.damageSources().mobAttack(this), 100.0F);
        e.igniteForSeconds(5);
        if (this.level() instanceof ServerLevel server) {
            for (int var3 = 0; var3 < 20; var3++) {
                server.sendParticles(
                        ParticleTypes.SMOKE,
                        e.getX() + this.random.nextFloat() - this.random.nextFloat(),
                        e.getY() + this.random.nextFloat() - this.random.nextFloat(),
                        e.getZ() + this.random.nextFloat(),
                        1,
                        0.0,
                        0.0,
                        0.0,
                        0.0);
                server.sendParticles(
                        ParticleTypes.LARGE_SMOKE,
                        e.getX() + this.random.nextFloat() - this.random.nextFloat(),
                        e.getY() + this.random.nextFloat() - this.random.nextFloat(),
                        e.getZ() + this.random.nextFloat() - this.random.nextFloat(),
                        1,
                        0.0,
                        0.0,
                        0.0,
                        0.0);
                server.sendParticles(
                        ParticleTypes.FIREWORK,
                        e.getX(),
                        e.getY(),
                        e.getZ(),
                        1,
                        this.random.nextGaussian(),
                        this.random.nextGaussian(),
                        this.random.nextGaussian(),
                        0.0);
            }
            server.playSound(
                    null,
                    e.getX(),
                    e.getY(),
                    e.getZ(),
                    SoundEvents.GENERIC_EXPLODE.value(),
                    this.getSoundSource(),
                    0.5F,
                    1.0F + (this.random.nextFloat() - this.random.nextFloat()) * 0.5F);
            if (server.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING)) {
                server.explode(this, e.getX(), e.getY(), e.getZ(), 3.0F, Level.ExplosionInteraction.MOB);
            } else {
                server.explode(this, e.getX(), e.getY(), e.getZ(), 3.0F, Level.ExplosionInteraction.NONE);
            }
            LightningBolt bolt = EntityType.LIGHTNING_BOLT.create(server);
            if (bolt != null) {
                bolt.moveTo(e.getX(), e.getY() + 1.0, e.getZ());
                server.addFreshEntity(bolt);
            }
            LightningBolt selfBolt = EntityType.LIGHTNING_BOLT.create(server);
            if (selfBolt != null) {
                selfBolt.moveTo(this.getX(), this.getY() + 15.0, this.getZ());
                server.addFreshEntity(selfBolt);
            }
        }
    }

    @Override
    public boolean doHurtTarget(Entity par1Entity) {
        if (par1Entity instanceof LivingEntity living) {
            float s = living.getBbHeight() * living.getBbWidth();
            if (s > 30.0F
                    && !(par1Entity instanceof Godzilla)
                    && !(par1Entity instanceof GodzillaHead)
                    && !(par1Entity instanceof PitchBlack)
                    && !(par1Entity instanceof IronGolem)
                    && !(par1Entity instanceof Kraken)) {
                // gold: MyUtils.isRoyalty deferred
                living.setHealth(living.getHealth() / 2.0F);
                living.hurt(this.damageSources().mobAttack(this), (float) (GOLD_ATTACK * 10.0F));
                this.largeUnknownDetected = 1;
            }
        }

        if (par1Entity instanceof EnderDragon) {
            // gold multi-part dragon damage simplified
            par1Entity.hurt(this.damageSources().mobAttack(this), (float) (GOLD_ATTACK / 2.0F));
        }

        if (!GoldStyleCombat.dealAttributeDamage(this, par1Entity)) {
            return false;
        }

        if (par1Entity instanceof LivingEntity) {
            double ks = 3.2;
            double inair = 0.3;
            float f3 = (float) Math.atan2(par1Entity.getZ() - this.getZ(), par1Entity.getX() - this.getX());
            if (!par1Entity.isAlive() || par1Entity instanceof Player) {
                inair *= 2.0;
            }
            par1Entity.push(Math.cos(f3) * ks, inair, Math.sin(f3) * ks);
        }
        return true;
    }

    @Override
    public boolean hurt(DamageSource par1DamageSource, float par2) {
        if (this.hurtTimer > 0) {
            return false;
        }
        float dm = par2;
        if (dm > 750.0F) {
            dm = 750.0F;
        }
        Entity e = par1DamageSource.getEntity();
        if (e instanceof LivingEntity enl) {
            float s = enl.getBbHeight() * enl.getBbWidth();
            if (s > 30.0F
                    && !(enl instanceof Godzilla)
                    && !(enl instanceof GodzillaHead)
                    && !(enl instanceof PitchBlack)
                    && !(enl instanceof IronGolem)
                    && !(enl instanceof Kraken)) {
                dm /= 10.0F;
                this.hurtTimer = 50;
                this.largeUnknownDetected = 1;
            }
        }
        boolean ret = false;
        if (!"cactus".equals(par1DamageSource.getMsgId())) {
            ret = super.hurt(par1DamageSource, dm);
            this.hurtTimer = 20;
            e = par1DamageSource.getEntity();
            if (e instanceof LivingEntity living
                    && !(e instanceof Godzilla)
                    && !(e instanceof GodzillaHead)) {
                this.setTarget(living);
                this.setLastHurtByMob(living);
                this.getNavigation().moveTo(living, 1.2);
            }
        }
        return ret;
    }

    @Override
    public void thunderHit(ServerLevel level, LightningBolt lightning) {
        // gold immune to lightning
    }

    /**
     * Gold {@code getCanSpawnHere}: dark, night, y≥50, headroom air 5.14 over ±8, no peer 64×16,
     * random 1/40, refreshes the alive lock.
     */
    @Override
    public boolean checkSpawnRules(LevelAccessor level, MobSpawnType spawnType) {
        if (!super.checkSpawnRules(level, spawnType)) {
            return false;
        }
        if (level instanceof Level lvl && lvl.isDay()) {
            return false;
        }
        if (this.getY() < 50.0) {
            return false;
        }
        if (level instanceof Level lvl) {
            long sinceAlive = lvl.getGameTime() - lastAliveGameTime;
            if (sinceAlive >= 0 && sinceAlive < SPAWN_LOCK_TICKS) {
                return false;
            }
        }
        if (this.random.nextInt(40) != 1) {
            return false;
        }
        BlockPos origin = this.blockPosition();
        for (int k = -8; k <= 8; k++) {
            for (int j = -8; j <= 8; j++) {
                for (int i = 5; i < 15; i++) {
                    BlockState bid = level.getBlockState(origin.offset(j, i, k));
                    if (!bid.isAir()) {
                        return false;
                    }
                }
            }
        }
        if (!level.getEntitiesOfClass(
                        Godzilla.class, this.getBoundingBox().inflate(64.0, 16.0, 64.0), e -> e != this)
                .isEmpty()) {
            return false;
        }
        if (level instanceof Level lvl && !lvl.isClientSide()) {
            lastAliveGameTime = lvl.getGameTime();
        }
        return true;
    }

    @Override
    public AABB getBoundingBoxForCulling() {
        // Model cubes extend ~200+ units; scale 2.0 → keep huge cull box
        return this.getBoundingBox().inflate(40.0, 30.0, 40.0);
    }
}
