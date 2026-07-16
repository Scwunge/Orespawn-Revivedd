package danger.orespawn.entity;

import danger.orespawn.OreSpawnMain;
import danger.orespawn.init.ModEntities;
import danger.orespawn.init.ModItems;
import java.util.Comparator;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.animal.horse.Horse;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.SmallFireball;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

/**
 * Gold {@code TheKing} (EntityMob) ported for NeoForge 1.21.1.
 * Size 22×24 (PlayNicely 5.5×6 via renderer /4), speed 0.62, health 7000, attack 21, armor 350,
 * XP 25000. Always-flying three-headed boss: fire/lightning/ice canons, KingHead hitbox companion,
 * end-sequence monologue, ramp-up attack/armor when low HP and few player hits.
 * Texture: {@code textures/entity/thekingtexture.png}.
 * Registry size/attrs set in {@code ModEntities}.
 */
public class TheKing extends Monster {
    private static final EntityDataAccessor<Byte> ATTACKING =
            SynchedEntityData.defineId(TheKing.class, EntityDataSerializers.BYTE);
    /** Gold datawatcher 21 — PlayNicely mirror for client scale. */
    private static final EntityDataAccessor<Integer> PLAY_NICELY =
            SynchedEntityData.defineId(TheKing.class, EntityDataSerializers.INT);
    /** Gold datawatcher 22 — isEnd flag for client fireworks. */
    private static final EntityDataAccessor<Integer> IS_END =
            SynchedEntityData.defineId(TheKing.class, EntityDataSerializers.INT);

    /** Gold TheKing_stats defaults: get_mobstats("TheKing", 7000, 350, 21). */
    public static final float GOLD_WIDTH = 22.0F;
    public static final float GOLD_HEIGHT = 24.0F;
    public static final float GOLD_WIDTH_PLAY_NICELY = 5.5F;
    public static final float GOLD_HEIGHT_PLAY_NICELY = 6.0F;
    public static final double GOLD_HEALTH = 7000.0;
    public static final double GOLD_SPEED = 0.62;
    public static final double GOLD_ATTACK = 21.0;
    public static final double GOLD_ARMOR = 350.0;
    public static final int GOLD_XP = 25000;
    public static final double GOLD_FOLLOW = 128.0;

    private double attdam = GOLD_ATTACK;
    private int hurt_timer = 0;
    private int homex = 0;
    private int homez = 0;
    private int stream_count = 0;
    private int stream_count_l = 0;
    private int stream_count_i = 0;
    private int ticker = 0;
    private int player_hit_count = 0;
    private int backoff_timer = 0;
    private int guard_mode = 0;
    private volatile int head_found = 0;
    private int wing_sound = 0;
    private int large_unknown_detected = 0;
    private int isEnd = 0;
    private int endCounter = 0;

    @Nullable
    private BlockPos currentFlightTarget = null;
    @Nullable
    private LivingEntity rt = null;

    public TheKing(EntityType<? extends TheKing> type, Level level) {
        super(type, level);
        this.xpReward = GOLD_XP;
        this.noCulling = true; // always render (gold shouldRender always true)
        this.noPhysics = true; // gold field_70145_X
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
        // gold: Swimming, LookIdle, HurtByTarget — flight AI is customServerAiStep
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(ATTACKING, (byte) 0);
        builder.define(PLAY_NICELY, OreSpawnMain.PlayNicely);
        builder.define(IS_END, 0);
    }

    @Override
    protected boolean shouldDespawnInPeaceful() {
        return false;
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return false; // gold canDespawn false
    }

    @Override
    public boolean fireImmune() {
        return true; // gold field_70178_ae
    }

    @Override
    public boolean isPushable() {
        return false; // gold canBePushed false
    }

    @Override
    protected void doPush(Entity entity) {
        // gold empty collideWithEntity
    }

    @Override
    public void push(Entity entity) {
        // gold no push
    }

    public int mygetMaxHealth() {
        return (int) GOLD_HEALTH;
    }

    public int getPlayNicely() {
        return this.entityData.get(PLAY_NICELY);
    }

    public final int getAttacking() {
        return this.entityData.get(ATTACKING) & 0xFF;
    }

    public final void setAttacking(int par1) {
        this.entityData.set(ATTACKING, (byte) par1);
    }

    public int getIsEnd() {
        return this.entityData.get(IS_END);
    }

    public void setGuardMode(int i) {
        this.guard_mode = i;
    }

    public void setFree() {
        this.isEnd = 1;
    }

    @Override
    protected float getSoundVolume() {
        return 1.35F;
    }

    @Override
    public float getVoicePitch() {
        return 1.0F;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        //  gold king_living
        return null;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        //  gold king_hit
        return null;
    }

    @Override
    protected SoundEvent getDeathSound() {
        //  gold trex_death
        return null;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("KingHomeX", this.homex);
        tag.putInt("KingHomeZ", this.homez);
        tag.putInt("GuardMode", this.guard_mode);
        tag.putInt("PlayerHits", this.player_hit_count);
        tag.putInt("IsEnd", this.isEnd);
        tag.putInt("EndCounter", this.endCounter);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.homex = tag.getInt("KingHomeX");
        this.homez = tag.getInt("KingHomeZ");
        this.guard_mode = tag.getInt("GuardMode");
        this.player_hit_count = tag.getInt("PlayerHits");
        this.isEnd = tag.getInt("IsEnd");
        this.endCounter = tag.getInt("EndCounter");
    }

    private void dropItemRand(Item index, int count) {
        if (index == null || count <= 0) {
            return;
        }
        double x = this.getX() + this.random.nextInt(20) - this.random.nextInt(20);
        double y = this.getY() + 12.0;
        double z = this.getZ() + this.random.nextInt(20) - this.random.nextInt(20);
        ItemEntity item = new ItemEntity(this.level(), x, y, z, new ItemStack(index, count));
        this.level().addFreshEntity(item);
    }

    /**
     * Gold dumps 150 random registry items + 150 block-items. In 1.21 the registry
     * includes command blocks / barriers / etc. that 1.7 never had — skip those.
     */
    private static boolean isSafeKingLootItem(Item item) {
        if (item == null || item == Items.AIR) {
            return false;
        }
        if (item == Items.COMMAND_BLOCK
                || item == Items.CHAIN_COMMAND_BLOCK
                || item == Items.REPEATING_COMMAND_BLOCK
                || item == Items.COMMAND_BLOCK_MINECART
                || item == Items.BARRIER
                || item == Items.STRUCTURE_BLOCK
                || item == Items.STRUCTURE_VOID
                || item == Items.JIGSAW
                || item == Items.LIGHT
                || item == Items.DEBUG_STICK
                || item == Items.KNOWLEDGE_BOOK
                || item == Items.BEDROCK
                || item == Items.END_PORTAL_FRAME
                || item == Items.SPAWNER
                || item == Items.TRIAL_SPAWNER
                || item == Items.VAULT
                || item == Items.REINFORCED_DEEPSLATE
                || item == Items.BUDDING_AMETHYST) {
            return false;
        }
        // BlockItem of unbreakable / admin blocks
        Block b = Block.byItem(item);
        if (b != Blocks.AIR) {
            if (b == Blocks.COMMAND_BLOCK
                    || b == Blocks.CHAIN_COMMAND_BLOCK
                    || b == Blocks.REPEATING_COMMAND_BLOCK
                    || b == Blocks.BARRIER
                    || b == Blocks.STRUCTURE_BLOCK
                    || b == Blocks.STRUCTURE_VOID
                    || b == Blocks.JIGSAW
                    || b == Blocks.LIGHT
                    || b == Blocks.BEDROCK
                    || b == Blocks.END_PORTAL
                    || b == Blocks.NETHER_PORTAL
                    || b == Blocks.END_GATEWAY
                    || b == Blocks.MOVING_PISTON) {
                return false;
            }
        }
        return true;
    }

    @Override
    protected void dropCustomDeathLoot(ServerLevel level, DamageSource damageSource, boolean recentlyHit) {
        // gold dropFewItems only (no vanilla entity loot table)

        // gold: spawn The Prince above body
        spawnByName("the_prince", this.getX(), this.getY() + 10.0, this.getZ());

        // gold full Royal set + MyRoyal
        this.dropItemRand(ModItems.ROYAL_CHESTPLATE.get(), 1);
        this.dropItemRand(ModItems.ROYAL_HELMET.get(), 1);
        this.dropItemRand(ModItems.ROYAL_LEGGINGS.get(), 1);
        this.dropItemRand(ModItems.ROYAL_BOOTS.get(), 1);
        this.dropItemRand(ModItems.ROYAL.get(), 1);

        // gold: 150 random items (registry dump) — filtered for 1.21 admin junk
        List<Item> items = BuiltInRegistries.ITEM.stream().filter(TheKing::isSafeKingLootItem).toList();
        if (!items.isEmpty()) {
            for (int j = 0; j < 150; j++) {
                this.dropItemRand(items.get(this.random.nextInt(items.size())), 1);
            }
        }

        // gold: 150 random block-as-item drops — same safety filter
        List<Item> blockItems =
                BuiltInRegistries.BLOCK.stream()
                        .map(Block::asItem)
                        .filter(TheKing::isSafeKingLootItem)
                        .toList();
        if (!blockItems.isEmpty()) {
            for (int j = 0; j < 150; j++) {
                this.dropItemRand(blockItems.get(this.random.nextInt(blockItems.size())), 1);
            }
        }
    }

    @Override
    public void tick() {
        super.tick();
        this.wing_sound++;
        if (this.wing_sound > 30) {
            if (!this.level().isClientSide) {
                // gold orespawn:MothraWings 1.75 / 0.75
                this.playSound(SoundEvents.ENDER_DRAGON_FLAP, 1.75F, 0.75F);
            }
            this.wing_sound = 0;
        }

        this.noPhysics = true;
        Vec3 m = this.getDeltaMovement();
        this.setDeltaMovement(m.x, m.y * 0.6, m.z);

        // ramp attack damage by health bands when few player hits
        if (this.player_hit_count < 10 && this.getHealth() < this.mygetMaxHealth() * 2 / 3.0F) {
            this.attdam = GOLD_ATTACK * 2;
        }
        if (this.player_hit_count < 10 && this.getHealth() < this.mygetMaxHealth() / 2.0F) {
            this.attdam = GOLD_ATTACK * 4;
        }
        if (this.player_hit_count < 10 && this.getHealth() < this.mygetMaxHealth() / 4.0F) {
            this.attdam = GOLD_ATTACK * 8;
        }
        if (this.player_hit_count < 10 && this.getHealth() < this.mygetMaxHealth() / 8.0F) {
            this.attdam = GOLD_ATTACK * 16;
        }

        // sync armor dynamically
        if (this.getAttribute(Attributes.ARMOR) != null) {
            this.getAttribute(Attributes.ARMOR).setBaseValue(this.getTotalArmorValue());
        }
        if (this.getAttribute(Attributes.ATTACK_DAMAGE) != null) {
            this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(this.attdam);
        }

        if (this.level().isClientSide) {
            float f = 7.0F;
            this.isEnd = this.entityData.get(IS_END);
            if (this.isEnd != 0 && this.random.nextInt(3) == 1) {
                for (int i = 0; i < 10; i++) {
                    this.level()
                            .addParticle(
                                    ParticleTypes.FIREWORK,
                                    this.getX() - f * Math.sin(Math.toRadians(this.getYRot())),
                                    this.getY() + 14.0,
                                    this.getZ() + f * Math.cos(Math.toRadians(this.getYRot())),
                                    (this.random.nextGaussian() - this.random.nextGaussian()) / 4.0
                                            + this.getDeltaMovement().x * 6.0,
                                    (this.random.nextGaussian() - this.random.nextGaussian()) / 4.0,
                                    (this.random.nextGaussian() - this.random.nextGaussian()) / 4.0
                                            + this.getDeltaMovement().z * 6.0);
                }
            }
        }
    }

    @Override
    public boolean doHurtTarget(Entity par1Entity) {
        if (par1Entity instanceof LivingEntity living) {
            float s = living.getBbHeight() * living.getBbWidth();
            if (s > 30.0F
                    && !this.isRoyalty(living)
                    && !(living instanceof PitchBlack)
                    /* Godzilla / GodzillaHead / Kraken deferred */) {
                living.setHealth(living.getHealth() / 2.0F);
                living.hurt(this.damageSources().mobAttack(this), (float) this.attdam * 10.0F);
                this.large_unknown_detected = 1;
            }
        }

        if (par1Entity instanceof EnderDragon dr) {
            // gold damages random dragon part — simplified whole-dragon hit
            dr.hurt(this.damageSources().mobAttack(this), (float) this.attdam);
        }

        boolean var4 = par1Entity.hurt(this.damageSources().mobAttack(this), (float) this.attdam);
        if (var4) {
            double ks = 3.3;
            double inair = 0.25;
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
        // gold rayTrace blocks false from eye+8.75
        return this.level()
                        .clip(new net.minecraft.world.level.ClipContext(
                                new Vec3(this.getX(), this.getY() + 8.75, this.getZ()),
                                new Vec3(pX, pY, pZ),
                                net.minecraft.world.level.ClipContext.Block.COLLIDER,
                                net.minecraft.world.level.ClipContext.Fluid.NONE,
                                this))
                        .getType()
                == net.minecraft.world.phys.HitResult.Type.MISS;
    }

    private boolean tooFarFromHome() {
        float d1 = (float) (this.getX() - this.homex);
        float d2 = (float) (this.getZ() - this.homez);
        d1 = (float) Math.sqrt(d1 * d1 + d2 * d2);
        return d1 > 120.0F;
    }

    private void msgToPlayers(String s) {
        List<Player> var5 =
                this.level().getEntitiesOfClass(Player.class, this.getBoundingBox().inflate(80.0, 64.0, 80.0));
        var5.sort(Comparator.comparingDouble(this::distanceToSqr));
        for (Player p : var5) {
            p.displayClientMessage(Component.literal(s), false);
        }
    }

    @Nullable
    private Player findNearestPlayer() {
        List<Player> var5 =
                this.level().getEntitiesOfClass(Player.class, this.getBoundingBox().inflate(80.0, 64.0, 80.0));
        var5.sort(Comparator.comparingDouble(this::distanceToSqr));
        return var5.isEmpty() ? null : var5.get(0);
    }

    @Override
    protected void customServerAiStep() {
        int xdir = 1;
        int zdir = 1;
        int attrand = 5;
        LivingEntity e;
        LivingEntity f;
        double rr;
        double rhdir;
        double rdd;
        double pi = 3.1415926545;
        double var1;
        double var3;
        double var5;
        float var7;
        float var8;
        Player p;

        if (this.isDeadOrDying()) {
            return;
        }

        super.customServerAiStep();
        this.entityData.set(IS_END, this.isEnd);
        this.entityData.set(PLAY_NICELY, OreSpawnMain.PlayNicely);

        if (this.isEnd == 1) {
            this.endCounter++;
            this.noPhysics = true;
            this.setDeltaMovement(Vec3.ZERO);
            this.hurt_timer = 10;
            if (!this.isDeadOrDying()) {
                p = this.findNearestPlayer();
                if (p != null) {
                    this.getLookControl().setLookAt(p, 10.0F, 10.0F);
                    p.setDeltaMovement(Vec3.ZERO);
                    double dd0 = this.getX() - p.getX();
                    double dd1 = this.getZ() - p.getZ();
                    float f2 = (float) (Math.atan2(dd1, dd0) * 180.0 / Math.PI) - 90.0F;
                    p.setYRot(f2);
                    p.setHealth(1.0F);
                }

                if (this.endCounter == 10) {
                    this.msgToPlayers(
                            "The King: Enough of this charade. I am done. You have shown me what I wanted to know.");
                } else if (this.endCounter == 80) {
                    this.msgToPlayers(
                            "The King: That's right my little pet. It has all been a game. You never killed me. You can't.");
                } else if (this.endCounter == 160) {
                    this.msgToPlayers(
                            "The King: I am the one. The only. The many. I exist within both space and time. Everywhere and always.");
                } else if (this.endCounter == 240) {
                    this.msgToPlayers(
                            "The King: I used you to learn your ways, and I have reached my conclusion on your species.");
                } else if (this.endCounter == 300) {
                    this.msgToPlayers("The King: You have 10 seconds to run.");
                } else if (this.endCounter == 320) {
                    this.msgToPlayers("9.");
                } else if (this.endCounter == 340) {
                    this.msgToPlayers("8.");
                } else if (this.endCounter == 360) {
                    this.msgToPlayers("7.");
                } else if (this.endCounter == 380) {
                    this.msgToPlayers("6.");
                } else if (this.endCounter == 400) {
                    this.msgToPlayers("5.");
                } else if (this.endCounter == 420) {
                    this.msgToPlayers("4.");
                } else if (this.endCounter == 440) {
                    this.msgToPlayers("3.");
                } else if (this.endCounter == 460) {
                    this.msgToPlayers("2.");
                } else if (this.endCounter == 480) {
                    this.msgToPlayers("1.");
                } else if (this.endCounter == 500) {
                    this.msgToPlayers("The King: Prepare to die!");
                    this.isEnd = 2;
                }
            }
            return;
        }

        if (this.isEnd == 2) {
            this.hurt_timer = 10;
            this.player_hit_count = 0;
            this.stream_count = 10;
            this.stream_count_l = 10;
            this.stream_count_i = 10;
            attrand = 3;
            this.guard_mode = 0;
            this.large_unknown_detected = 1;
            if (this.backoff_timer > 0) {
                this.backoff_timer--;
            }
        }

        if (this.hurt_timer > 0) {
            this.hurt_timer--;
        }

        if (this.homex == 0 && this.homez == 0 || this.guard_mode == 0) {
            this.homex = (int) this.getX();
            this.homez = (int) this.getZ();
        }

        this.ticker++;
        if (this.ticker > 30000) {
            this.ticker = 0;
        }
        if (this.ticker % 80 == 0) {
            this.stream_count = 10;
        }
        if (this.ticker % 90 == 0) {
            this.stream_count_l = 5;
        }
        if (this.ticker % 70 == 0) {
            this.stream_count_i = 8;
        }

        if (this.backoff_timer > 0) {
            this.backoff_timer--;
        }

        if (this.player_hit_count < 10 && this.getHealth() < this.mygetMaxHealth() / 2.0F) {
            attrand = 3;
        }

        this.noPhysics = true;
        if (this.currentFlightTarget == null) {
            this.currentFlightTarget = BlockPos.containing(this.getX(), this.getY(), this.getZ());
        }

        if (this.tooFarFromHome()
                || this.random.nextInt(200) == 0
                || this.currentFlightTarget.distToCenterSqr(this.getX(), this.getY(), this.getZ()) < 9.1) {
            zdir = this.random.nextInt(120);
            xdir = this.random.nextInt(120);
            if (this.random.nextInt(2) == 0) {
                zdir = -zdir;
            }
            if (this.random.nextInt(2) == 0) {
                xdir = -xdir;
            }

            int dist = 0;
            for (int i = -5; i <= 5; i += 5) {
                for (int j = -5; j <= 5; j += 5) {
                    BlockPos probe = new BlockPos(this.homex + j, (int) this.getY(), this.homez + i);
                    BlockState bid = this.level().getBlockState(probe);
                    if (!bid.isAir()) {
                        for (int k = 1; k < 20; k++) {
                            bid = this.level().getBlockState(probe.above(k));
                            dist++;
                            if (bid.isAir()) {
                                break;
                            }
                        }
                    } else {
                        for (int k = 1; k < 20; k++) {
                            bid = this.level().getBlockState(probe.below(k));
                            dist--;
                            if (!bid.isAir()) {
                                break;
                            }
                        }
                    }
                }
            }

            dist = dist / 9 + 2;
            if ((int) (this.getY() + dist) > 230) {
                dist = 230 - (int) this.getY();
            }
            this.currentFlightTarget =
                    new BlockPos(this.homex + xdir, (int) (this.getY() + dist), this.homez + zdir);
        } else if (this.random.nextInt(attrand) == 0) {
            e = this.rt;
            if (OreSpawnMain.PlayNicely != 0) {
                e = null;
            }
            if (e instanceof TheKing || e instanceof KingHead) {
                this.rt = null;
                e = null;
            }
            if (e != null) {
                float d1 = (float) (e.getX() - this.homex);
                float d2 = (float) (e.getZ() - this.homez);
                d1 = (float) Math.sqrt(d1 * d1 + d2 * d2);
                if (!e.isAlive() || this.random.nextInt(250) == 1 || d1 > 128.0F && this.guard_mode == 1) {
                    e = null;
                    this.rt = null;
                }
                if (e != null && !this.MyCanSee(e)) {
                    e = null;
                }
            }

            f = this.findSomethingToAttack();
            if (this.head_found == 0) {
                spawnByName("king_head", this.getX(), this.getY() + 20.0, this.getZ());
            }

            if (e == null) {
                e = f;
            }

            if (e != null) {
                this.setAttacking(1);
                if (this.backoff_timer == 0) {
                    int dist = (int) (e.getY() + e.getBbHeight() / 2.0F + 1.0);
                    if (dist > 230) {
                        dist = 230;
                    }
                    this.currentFlightTarget = new BlockPos((int) e.getX(), dist, (int) e.getZ());
                    if (this.random.nextInt(70) == 1) {
                        this.backoff_timer = 80 + this.random.nextInt(80);
                    }
                } else if (this.currentFlightTarget.distToCenterSqr(this.getX(), this.getY(), this.getZ())
                        < 9.1) {
                    zdir = this.random.nextInt(20) + 30;
                    xdir = this.random.nextInt(20) + 30;
                    if (this.random.nextInt(2) == 0) {
                        zdir = -zdir;
                    }
                    if (this.random.nextInt(2) == 0) {
                        xdir = -xdir;
                    }

                    int dist = 0;
                    for (int i = -5; i <= 5; i += 5) {
                        for (int j = -5; j <= 5; j += 5) {
                            BlockPos probe =
                                    new BlockPos((int) e.getX() + j, (int) this.getY(), (int) e.getZ() + i);
                            BlockState bid = this.level().getBlockState(probe);
                            if (!bid.isAir()) {
                                for (int k = 1; k < 20; k++) {
                                    bid = this.level().getBlockState(probe.above(k));
                                    dist++;
                                    if (bid.isAir()) {
                                        break;
                                    }
                                }
                            } else {
                                for (int k = 1; k < 20; k++) {
                                    bid = this.level().getBlockState(probe.below(k));
                                    dist--;
                                    if (!bid.isAir()) {
                                        break;
                                    }
                                }
                            }
                        }
                    }

                    dist = dist / 9 + 2;
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
                    int which = this.random.nextInt(3);
                    if (which == 0) {
                        if (this.stream_count > 0) {
                            this.setAttacking(1);
                            rr = Math.atan2(e.getZ() - this.getZ(), e.getX() - this.getX());
                            rhdir = Math.toRadians((this.yBodyRot + 90.0F) % 360.0F);
                            rdd = Math.abs(rr - rhdir) % (pi * 2.0);
                            if (rdd > pi) {
                                rdd -= pi * 2.0;
                            }
                            rdd = Math.abs(rdd);
                            if (rdd < 0.5) {
                                this.firecanon(e);
                            }
                        }
                    } else if (which == 1) {
                        if (this.stream_count_l > 0) {
                            this.setAttacking(1);
                            rr = Math.atan2(e.getZ() - this.getZ(), e.getX() - this.getX());
                            rhdir = Math.toRadians((this.yBodyRot + 90.0F) % 360.0F);
                            rdd = Math.abs(rr - rhdir) % (pi * 2.0);
                            if (rdd > pi) {
                                rdd -= pi * 2.0;
                            }
                            rdd = Math.abs(rdd);
                            if (rdd < 0.5) {
                                this.firecanonl(e);
                            }
                        }
                    } else if (this.stream_count_i > 0) {
                        this.setAttacking(1);
                        rr = Math.atan2(e.getZ() - this.getZ(), e.getX() - this.getX());
                        rhdir = Math.toRadians((this.yBodyRot + 90.0F) % 360.0F);
                        rdd = Math.abs(rr - rhdir) % (pi * 2.0);
                        if (rdd > pi) {
                            rdd -= pi * 2.0;
                        }
                        rdd = Math.abs(rdd);
                        if (rdd < 0.5) {
                            this.firecanoni(e);
                        }
                    }
                }
            } else {
                this.setAttacking(0);
                this.stream_count = 10;
                this.stream_count_l = 5;
                this.stream_count_i = 8;
            }
        }

        // gold isEnd==2 purple power stream while attacking — PurplePower deferred; fireworks stand-in via isEnd
        if (this.getAttacking() != 0 && this.isEnd == 2) {
            // deferred: spawn PurplePower type 10; client already shows fireworks when isEnd != 0
        }

        var1 = this.currentFlightTarget.getX() + 0.5 - this.getX();
        var3 = this.currentFlightTarget.getY() + 0.1 - this.getY();
        var5 = this.currentFlightTarget.getZ() + 0.5 - this.getZ();
        Vec3 dm = this.getDeltaMovement();
        double nx = dm.x + (Math.signum(var1) * 0.7 - dm.x) * 0.35;
        double ny = dm.y + (Math.signum(var3) * 0.69999 - dm.y) * 0.3;
        double nz = dm.z + (Math.signum(var5) * 0.7 - dm.z) * 0.35;
        this.setDeltaMovement(nx, ny, nz);
        var7 = (float) (Math.atan2(nz, nx) * 180.0 / Math.PI) - 90.0F;
        var8 = Mth.wrapDegrees(var7 - this.getYRot());
        this.zza = 1.0F;
        this.setYRot(this.getYRot() + var8 / 8.0F);

        if (this.random.nextInt(30) == 1 && this.getHealth() < this.mygetMaxHealth()) {
            this.heal(5.0F);
            if (this.large_unknown_detected != 0) {
                this.heal(200.0F);
            }
        }

        if (this.player_hit_count < 10 && this.getHealth() < 2000.0F) {
            this.heal(2000.0F - this.getHealth());
        }
    }

    private double getHorizontalDistanceSqToEntity(Entity e) {
        double d1 = e.getZ() - this.getZ();
        double d2 = e.getX() - this.getX();
        return d1 * d1 + d2 * d2;
    }

    /** Gold firecanon → BetterFireball stream (really-big + 6 big/small). */
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
                Vec3 dir2 = new Vec3(
                        e.getX() - cx + r1,
                        e.getY() + e.getBbHeight() / 2.0F - (this.getY() + yoff) + r2,
                        e.getZ() - cz + r3);
                bf = new BetterFireball(this.level(), this, dir2);
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

    /** Gold firecanonl → ThunderBolt (deferred). Stand-in: SmallFireball ×3. */
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
                float r1 = 5.0F * (this.random.nextFloat() - this.random.nextFloat());
                float r2 = 3.0F * (this.random.nextFloat() - this.random.nextFloat());
                float r3 = 5.0F * (this.random.nextFloat() - this.random.nextFloat());
                Vec3 dir = new Vec3(
                        e.getX() - cx + r1,
                        e.getY() + 0.25 - (this.getY() + yoff) + r2,
                        e.getZ() - cz + r3);
                SmallFireball lb = new SmallFireball(this.level(), this, dir.normalize().scale(3.0));
                lb.setPos(cx, this.getY() + yoff, cz);
                this.level().addFreshEntity(lb);
            }
            this.stream_count_l--;
        }
    }

    /** Gold firecanoni → IceBall setIceMaker(1) (deferred). Stand-in: SmallFireball ×5. */
    private void firecanoni(LivingEntity e) {
        double yoff = 14.0;
        double xzoff = 32.0;
        double cx = this.getX() - xzoff * Math.sin(Math.toRadians(this.getYRot()));
        double cz = this.getZ() + xzoff * Math.cos(Math.toRadians(this.getYRot()));
        if (this.stream_count_i > 0) {
            this.playSound(
                    SoundEvents.ARROW_SHOOT,
                    1.0F,
                    1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
            for (int i = 0; i < 5; i++) {
                float r1 = 5.0F * (this.random.nextFloat() - this.random.nextFloat());
                float r2 = 3.0F * (this.random.nextFloat() - this.random.nextFloat());
                float r3 = 5.0F * (this.random.nextFloat() - this.random.nextFloat());
                Vec3 dir = new Vec3(
                        e.getX() - cx + r1,
                        e.getY() + 0.25 - (this.getY() + yoff) + r2,
                        e.getZ() - cz + r3);
                SmallFireball lb = new SmallFireball(this.level(), this, dir.normalize().scale(3.0));
                lb.setPos(cx, this.getY() + yoff, cz);
                this.level().addFreshEntity(lb);
            }
            this.stream_count_i--;
        }
    }

    @Override
    public boolean causeFallDamage(float fallDistance, float multiplier, DamageSource source) {
        return false;
    }

    @Override
    protected void checkFallDamage(double y, boolean onGround, BlockState state, BlockPos pos) {
        // gold empty
    }

    @Override
    public boolean canBeLeashed() {
        return false;
    }

    @Override
    public boolean hurt(DamageSource par1DamageSource, float par2) {
        boolean ret = false;
        float dm = par2;
        if (this.hurt_timer > 0) {
            return false;
        }

        if (dm > 750.0F) {
            dm = 750.0F;
        }

        // gold "inWall" immune
        if (par1DamageSource == this.damageSources().inWall()) {
            return false;
        }

        Entity e = par1DamageSource.getEntity();
        if (e instanceof LivingEntity enl) {
            float s = enl.getBbHeight() * enl.getBbWidth();
            if (s > 30.0F
                    && !this.isRoyalty(enl)
                    && !(enl instanceof PitchBlack)
                    /* Godzilla / Kraken deferred */) {
                dm /= 10.0F;
                this.hurt_timer = 50;
                this.large_unknown_detected = 1;
            }

            if (e instanceof Monster && s < 3.0F) {
                e.discard();
                return false;
            }
        }

        // gold cactus ignored for actual damage application path. actually gold skips cactus entirely for super
        if (!"cactus".equals(par1DamageSource.getMsgId())) {
            this.hurt_timer = 20;
            ret = super.hurt(par1DamageSource, dm);
            if (e instanceof Player) {
                this.player_hit_count++;
            }
            if (e instanceof LivingEntity living
                    && this.currentFlightTarget != null
                    && !this.isRoyalty(e)) {
                this.rt = living;
                int dist = (int) e.getY();
                if (dist > 230) {
                    dist = 230;
                }
                this.currentFlightTarget = new BlockPos((int) e.getX(), dist, (int) e.getZ());
            }
        }

        return ret;
    }

    @Override
    public boolean checkSpawnRules(net.minecraft.world.level.LevelAccessor level, net.minecraft.world.entity.MobSpawnType spawnType) {
        return true; // gold getCanSpawnHere true
    }

    /** Gold getTotalArmorValue — defense bands. */
    public int getTotalArmorValue() {
        if (this.large_unknown_detected != 0) {
            return 25;
        } else if (this.player_hit_count < 10 && this.getHealth() < this.mygetMaxHealth() * 2 / 3.0F) {
            return (int) GOLD_ARMOR + 1;
        } else if (this.player_hit_count < 10 && this.getHealth() < this.mygetMaxHealth() / 2.0F) {
            return (int) GOLD_ARMOR + 2;
        } else if (this.player_hit_count < 10 && this.getHealth() < this.mygetMaxHealth() / 4.0F) {
            return (int) GOLD_ARMOR + 3;
        } else {
            return (int) GOLD_ARMOR;
        }
    }

    @Override
    public void thunderHit(ServerLevel level, net.minecraft.world.entity.LightningBolt lightning) {
        // gold immune to lightning
    }

    public boolean MyCanSee(LivingEntity e) {
        double xzoff = 22.0;
        int nblks = 20;
        double cx = this.getX() - xzoff * Math.sin(Math.toRadians(this.getYRot()));
        double cz = this.getZ() + xzoff * Math.cos(Math.toRadians(this.getYRot()));
        float startx = (float) cx;
        float starty = (float) (this.getY() + this.getBbHeight() * 7.0F / 8.0F);
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
            BlockState bid = this.level().getBlockState(BlockPos.containing(startx, starty, startz));
            // gold allows water, leaves, vines, air
            if (!bid.isAir()
                    && !bid.is(Blocks.WATER)
                    && !bid.is(Blocks.LAVA)
                    && !bid.getBlock().toString().contains("leaves")
                    && !bid.is(Blocks.VINE)) {
                // simplified: solid non-liquid blocks block LOS unless leaves/vine
                if (bid.canOcclude()) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean isRoyalty(Entity e) {
        // gold MyUtils.isRoyalty — Prince tree + King family
        return e instanceof TheKing
                || e instanceof KingHead
                || e instanceof ThePrince
                || e instanceof ThePrinceTeen
                || e instanceof ThePrinceAdult
                || e instanceof ThePrincess;
    }

    private boolean isSuitableTarget(LivingEntity par1EntityLiving) {
        if (par1EntityLiving == null) {
            return false;
        }
        if (par1EntityLiving == this) {
            return false;
        }
        if (!par1EntityLiving.isAlive()) {
            return false;
        }
        if (par1EntityLiving instanceof KingHead) {
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

        // gold MyUtils.isIgnoreable deferred

        if (this.isEnd == 2) {
            if (par1EntityLiving instanceof Player p) {
                return !p.isCreative() && !p.isSpectator();
            }
            // Girlfriend / Boyfriend deferred
            if (par1EntityLiving instanceof Villager) {
                return true;
            }
        }

        if (!this.MyCanSee(par1EntityLiving)) {
            return false;
        } else if (par1EntityLiving instanceof Player p) {
            return !p.isCreative() && !p.isSpectator();
        } else if (par1EntityLiving instanceof Horse) {
            return true;
        } else if (par1EntityLiving instanceof Monster) {
            return true;
        } else if (par1EntityLiving instanceof EnderDragon) {
            return true;
        } else {
            // gold MyUtils.isAttackableNonMob — animals etc.
            return par1EntityLiving instanceof net.minecraft.world.entity.animal.Animal;
        }
    }

    @Nullable
    private LivingEntity findSomethingToAttack() {
        if (OreSpawnMain.PlayNicely != 0) {
            this.head_found = 1;
            return null;
        }

        if (this.isEnd == 2) {
            List<Player> var5p =
                    this.level().getEntitiesOfClass(Player.class, this.getBoundingBox().inflate(80.0, 64.0, 80.0));
            var5p.sort(Comparator.comparingDouble(this::distanceToSqr));
            this.head_found = 1;
            for (Player var4p : var5p) {
                if (this.isSuitableTarget(var4p)) {
                    return var4p;
                }
            }
        }

        List<LivingEntity> var5 = this.level()
                .getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(80.0, 64.0, 80.0));
        var5.sort(Comparator.comparingDouble(this::distanceToSqr));
        LivingEntity ret = null;
        this.head_found = 0;

        for (LivingEntity var4 : var5) {
            if (this.isSuitableTarget(var4) && ret == null) {
                ret = var4;
            }
            if (ret != null && this.head_found != 0) {
                break;
            }
        }
        return ret;
    }

    /**
     * Spawn by registry name so KingHead works once {@code orespawn:king_head} is registered,
     * and ThePrince via known DeferredHolder. No shared ModEntities edit required from this wave.
     */
    @Nullable
    private Entity spawnByName(String path, double x, double y, double z) {
        EntityType<?> type = null;
        if ("the_prince".equals(path)) {
            type = ModEntities.THE_PRINCE.get();
        } else {
            ResourceLocation id = ResourceLocation.fromNamespaceAndPath("orespawn", path);
            type = BuiltInRegistries.ENTITY_TYPE.getOptional(id).orElse(null);
        }
        if (type == null) {
            return null;
        }
        Entity var8 = type.create(this.level());
        if (var8 != null) {
            var8.moveTo(x, y, z, this.random.nextFloat() * 360.0F, 0.0F);
            this.level().addFreshEntity(var8);
        }
        return var8;
    }

    @Nullable
    private LivingEntity doJumpDamage(double X, double Y, double Z, double dist, double damage, int knock) {
        AABB bb = new AABB(X - dist, Y - 10.0, Z - dist, X + dist, Y + 10.0, Z + dist);
        List<LivingEntity> var5x = this.level().getEntitiesOfClass(LivingEntity.class, bb);
        var5x.sort(Comparator.comparingDouble(this::distanceToSqr));
        for (LivingEntity var4 : var5x) {
            if (var4 != null
                    && var4 != this
                    && var4.isAlive()
                    && !this.isRoyalty(var4)
                    && !(var4 instanceof Ghost)
                    && !(var4 instanceof GhostSkelly)) {
                // gold half generic + half fall damage
                var4.hurt(this.damageSources().magic(), (float) damage / 2.0F);
                var4.hurt(this.damageSources().fall(), (float) damage / 2.0F);
                this.level()
                        .playSound(
                                null,
                                var4.blockPosition(),
                                SoundEvents.GENERIC_EXPLODE.value(),
                                var4.getSoundSource(),
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
}
