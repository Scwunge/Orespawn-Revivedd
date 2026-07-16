package danger.orespawn.entity;

import danger.orespawn.OreSpawnMain;
import danger.orespawn.entity.tame.OreSpawnPet;
import danger.orespawn.init.ModEntities;
import danger.orespawn.util.ai.FollowOwnerGoal;
import danger.orespawn.util.ai.WanderALotGoal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.Difficulty;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.SmallFireball;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
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
 * Gold {@code ThePrincess} (EntityTameable) → NeoForge 1.21.1.
 * Size 0.75×1.25, speed 0.32, HP 400, atk attr 10 / melee 9, armor 14, XP 50, follow 1000.
 * Activity ground/fly, three-head extensions, power charge, fire/lightning/ice canon, pet grow flags.
 * Textures: {@code theprincesstexture.png} / {@code theprincesstexture2.png} (attack).
 */
public class ThePrincess extends Monster {
    private static final EntityDataAccessor<Integer> ACTIVITY =
            SynchedEntityData.defineId(ThePrincess.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> FIRE =
            SynchedEntityData.defineId(ThePrincess.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> ATTACKING =
            SynchedEntityData.defineId(ThePrincess.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> POWER =
            SynchedEntityData.defineId(ThePrincess.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Byte> PET_FLAGS =
            SynchedEntityData.defineId(ThePrincess.class, EntityDataSerializers.BYTE);
    private static final EntityDataAccessor<Optional<UUID>> OWNER =
            SynchedEntityData.defineId(ThePrincess.class, EntityDataSerializers.OPTIONAL_UUID);

    public static final float GOLD_WIDTH = 0.75F;
    public static final float GOLD_HEIGHT = 1.25F;
    public static final double GOLD_HEALTH = 400.0;
    public static final double GOLD_SPEED = 0.32;
    public static final double GOLD_ATTACK = 10.0;
    public static final double GOLD_ARMOR = 14.0;
    public static final int GOLD_XP = 50;
    public static final double GOLD_FOLLOW = 1000.0;

    private final float moveSpeed = 0.32F;

    @Nullable
    private BlockPos currentFlightTarget;
    /** Gold public activity (1=ground, 2=fly). */
    public int activity = 1;
    private int owner_flying = 0;
    private int syncit = 0;
    private int head1ext = 0;
    private int head2ext = 0;
    private int head3ext = 0;
    private int head1dir = 1;
    private int head2dir = 1;
    private int head3dir = 1;
    private int ok_to_grow = 0;
    private int kill_count = 0;
    private int fed_count = 0;
    private int day_count = 0;
    private int is_day = 0;
    private int attack_level = 1;
    private int ticker = 0;

    public ThePrincess(EntityType<? extends ThePrincess> type, Level level) {
        super(type, level);
        this.xpReward = GOLD_XP;
        this.setNoGravity(false);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, GOLD_HEALTH) // mygetMaxHealth
                .add(Attributes.MOVEMENT_SPEED, GOLD_SPEED)
                .add(Attributes.ATTACK_DAMAGE, GOLD_ATTACK)
                .add(Attributes.ARMOR, GOLD_ARMOR) // func_70658_aO
                .add(Attributes.FOLLOW_RANGE, GOLD_FOLLOW); // field_70174_ab
    }

    /** Gold was EntityTameable — never wipe on Peaceful. */
    @Override
    protected boolean shouldDespawnInPeaceful() {
        return false;
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        // gold: MyEntityAIFollowOwner 1.15 / 12 / 2
        this.goalSelector.addGoal(
                2,
                new FollowOwnerGoal(
                        this,
                        () -> OreSpawnPet.getOwnerUUID(this, OWNER),
                        () -> OreSpawnPet.isSitting(this, PET_FLAGS),
                        1.15,
                        12.0F,
                        2.0F));
        // gold: EntityAITempt beef
        this.goalSelector.addGoal(3, new TemptGoal(this, 1.25, Ingredient.of(Items.BEEF), false));
        this.goalSelector.addGoal(4, new LookAtPlayerGoal(this, LivingEntity.class, 6.0F));
        this.goalSelector.addGoal(5, new WanderALotGoal(this, 10, 0.75));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
        // gold EntityAIMoveIndoors — no direct 1.21 equivalent; skip
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(ACTIVITY, 1);
        builder.define(FIRE, 1);
        builder.define(ATTACKING, 0);
        builder.define(POWER, 1);
        builder.define(PET_FLAGS, (byte) 0);
        builder.define(OWNER, Optional.empty());
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("SpyroActivity", this.getActivity());
        tag.putInt("SpyroFire", this.getSpyroFire());
        tag.putInt("SpyroGrow", this.ok_to_grow);
        tag.putInt("SpyroKill", this.kill_count);
        tag.putInt("SpyroFed", this.fed_count);
        tag.putInt("SpyroDay", this.day_count);
        OreSpawnPet.addAdditionalSaveData(this, PET_FLAGS, OWNER, tag);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.contains("SpyroActivity")) {
            this.setActivity(tag.getInt("SpyroActivity"));
        }
        if (tag.contains("SpyroFire")) {
            this.setSpyroFire(tag.getInt("SpyroFire"));
        }
        this.ok_to_grow = tag.getInt("SpyroGrow");
        this.kill_count = tag.getInt("SpyroKill");
        this.fed_count = tag.getInt("SpyroFed");
        this.day_count = tag.getInt("SpyroDay");
        OreSpawnPet.readAdditionalSaveData(this, PET_FLAGS, OWNER, tag);
        if (OreSpawnPet.isTame(this, PET_FLAGS)) {
            this.setPersistenceRequired();
        }
    }

    @Override
    public boolean fireImmune() {
        return true; // field_70178_ae
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return false; // gold func_70692_ba = false
    }

    public int mygetMaxHealth() {
        return (int) GOLD_HEALTH;
    }

    public int getPower() {
        return this.entityData.get(POWER);
    }

    public void setPower(int par1) {
        this.entityData.set(POWER, par1);
    }

    public int getActivity() {
        int i = this.entityData.get(ACTIVITY);
        this.activity = i;
        return i;
    }

    public void setActivity(int par1) {
        this.activity = par1;
        this.entityData.set(ACTIVITY, par1);
    }

    public int getSpyroFire() {
        return this.entityData.get(FIRE);
    }

    public void setSpyroFire(int par1) {
        this.entityData.set(FIRE, par1);
    }

    public int getAttacking() {
        return this.entityData.get(ATTACKING);
    }

    public void setAttacking(int par1) {
        this.entityData.set(ATTACKING, par1);
    }

    public int getHead1Ext() {
        return this.head1ext;
    }

    public int getHead2Ext() {
        return this.head2ext;
    }

    public int getHead3Ext() {
        return this.head3ext;
    }

    public void set_ok_to_grow() {
        this.ok_to_grow = 1;
        this.kill_count = 0;
        this.fed_count = 0;
        this.day_count = 0;
    }

    public boolean isOreSpawnTame() {
        return OreSpawnPet.isTame(this, PET_FLAGS);
    }

    public boolean isOreSpawnSitting() {
        return OreSpawnPet.isSitting(this, PET_FLAGS);
    }

    private static boolean isFood(ItemStack stack) {
        // gold: any ItemFood
        return !stack.isEmpty() && stack.getFoodProperties(null) != null;
    }

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
    public void tick() {
        super.tick();

        // gold: noClip when flying
        if (this.getActivity() == 2) {
            this.noPhysics = true;
            this.setNoGravity(true);
        } else {
            this.noPhysics = false;
            this.setNoGravity(false);
        }

        // head extension idle wander
        if (this.random.nextInt(10) == 1) {
            int i = this.random.nextInt(3);
            if (i == 0) this.head1dir = 2;
            if (i == 1) this.head1dir = -2;
            if (i == 2) this.head1dir = 0;
        }
        if (this.random.nextInt(10) == 1) {
            int i = this.random.nextInt(3);
            if (i == 0) this.head2dir = 2;
            if (i == 1) this.head2dir = -2;
            if (i == 2) this.head2dir = 0;
        }
        if (this.random.nextInt(10) == 1) {
            int i = this.random.nextInt(3);
            if (i == 0) this.head3dir = 2;
            if (i == 1) this.head3dir = -2;
            if (i == 2) this.head3dir = 0;
        }
        this.head1ext = Mth.clamp(this.head1ext + this.head1dir, 0, 60);
        this.head2ext = Mth.clamp(this.head2ext + this.head2dir, 0, 60);
        this.head3ext = Mth.clamp(this.head3ext + this.head3dir, 0, 60);

        // gold: fireworks when power > 400 (client)
        if (this.level().isClientSide && this.getPower() > 400) {
            float f = 0.25F;
            if (this.random.nextInt(6) == 1) {
                for (int i = 0; i < 2; i++) {
                    this.level()
                            .addParticle(
                                    ParticleTypes.FIREWORK,
                                    this.getX() - f * Math.sin(Math.toRadians(this.getYRot())),
                                    this.getY() + 0.4,
                                    this.getZ() + f * Math.cos(Math.toRadians(this.getYRot())),
                                    (this.random.nextGaussian() - this.random.nextGaussian()) / 7.0
                                            + this.getDeltaMovement().x * 3.0,
                                    (this.random.nextGaussian() - this.random.nextGaussian()) / 7.0,
                                    (this.random.nextGaussian() - this.random.nextGaussian()) / 7.0
                                            + this.getDeltaMovement().z * 3.0);
                }
            }
        }

        if (this.getAttribute(Attributes.MOVEMENT_SPEED) != null) {
            this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(this.moveSpeed);
        }
        if (OreSpawnPet.isSitting(this, PET_FLAGS)) {
            this.getNavigation().stop();
        }
        if (this.isInWater()) {
            this.setDeltaMovement(this.getDeltaMovement().add(0.0, 0.07, 0.0));
        }
        if (this.currentFlightTarget == null) {
            this.currentFlightTarget = BlockPos.containing(this.getX(), this.getY(), this.getZ());
        }

        this.syncit++;
        if (this.syncit > 20) {
            this.syncit = 0;
            if (this.level().isClientSide) {
                this.getActivity();
            } else {
                int j = this.activity;
                this.setActivity(j);
            }
        }

        // gold: activity 2 damps Y motion
        if (this.activity == 2) {
            Vec3 m = this.getDeltaMovement();
            this.setDeltaMovement(m.x, m.y * 0.6, m.z);
        }
    }

    @Override
    protected void customServerAiStep() {
        // gold: when not flying, run super ground AI; when flying, skip path AI
        if (this.activity != 2) {
            super.customServerAiStep();
        }

        if (this.isDeadOrDying()) {
            return;
        }

        double xzoff = 1.5;
        double yoff = 1.0;

        if (this.random.nextInt(200) == 1) {
            this.setTarget(null);
        }

        this.ticker++;
        if (this.ticker % 10 == 0) {
            this.setPower(this.attack_level);
        }

        if (this.random.nextInt(200) == 1 && this.getHealth() < this.mygetMaxHealth()) {
            this.heal(1.0F);
        }

        // gold: auto-tame nearest player within 10
        if (!OreSpawnPet.isTame(this, PET_FLAGS)) {
            Player p = this.level().getNearestPlayer(this, 10.0);
            if (p != null) {
                OreSpawnPet.tame(this, PET_FLAGS, OWNER, p);
                this.heal(this.mygetMaxHealth() - this.getHealth());
            }
        }

        this.attack_level++;
        if (this.getAttacking() != 0) {
            this.attack_level += 4;
        }
        if (this.getSpyroFire() == 0) {
            this.attack_level = 0;
        }

        if (this.attack_level > 500) {
            if (this.getAttacking() != 0) {
                // gold: spawn 3 PurplePower — deferred entity; fireworks + BetterFireball stand-in
                int j = 3;
                for (int i = 0; i < j; i++) {
                    double cx = this.getX() - xzoff * Math.sin(Math.toRadians(this.getYRot()));
                    double cz = this.getZ() + xzoff * Math.cos(Math.toRadians(this.getYRot()));
                    Vec3 dir = new Vec3(this.getDeltaMovement().x * 3.0 + 0.01, 0.05, this.getDeltaMovement().z * 3.0);
                    BetterFireball bf = new BetterFireball(this.level(), this, dir.normalize());
                    bf.setPos(cx, this.getY() + yoff, cz);
                    bf.setSmall();
                    bf.setNotMe();
                    this.level().addFreshEntity(bf);
                    if (this.level() instanceof ServerLevel sl) {
                        sl.sendParticles(
                                ParticleTypes.FIREWORK,
                                cx,
                                this.getY() + yoff,
                                cz,
                                8,
                                0.2,
                                0.2,
                                0.2,
                                0.05);
                    }
                }
            } else {
                // flower / terrain blessing when not attacking
                if (this.level().getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING)) {
                    for (int m = 0; m < 5; m++) {
                        int i = this.random.nextInt(5) - this.random.nextInt(5);
                        int k = this.random.nextInt(5) - this.random.nextInt(5);
                        for (int j = -5; j < 5; j++) {
                            BlockPos pos = BlockPos.containing(this.getX() + i, this.getY() + j, this.getZ() + k);
                            BlockState bid = this.level().getBlockState(pos);
                            BlockPos above = pos.above();
                            BlockState aboveState = this.level().getBlockState(above);
                            if (bid.is(Blocks.GRASS_BLOCK) && aboveState.isAir()) {
                                this.level().setBlock(above, pickBlessingFlower(), 3);
                                break;
                            }
                            if (bid.is(Blocks.DIRT) && aboveState.isAir()) {
                                this.level().setBlock(pos, Blocks.GRASS_BLOCK.defaultBlockState(), 3);
                                break;
                            }
                            if (bid.is(Blocks.STONE) && aboveState.isAir()) {
                                this.level().setBlock(above, Blocks.DIRT.defaultBlockState(), 3);
                                break;
                            }
                            if (bid.is(Blocks.SAND) && aboveState.isAir()) {
                                if (this.random.nextInt(2) == 0) {
                                    this.level().setBlock(above, Blocks.CACTUS.defaultBlockState(), 3);
                                } else {
                                    this.level().setBlock(pos, Blocks.DIRT.defaultBlockState(), 3);
                                }
                                break;
                            }
                            if (bid.is(Blocks.LAVA) && aboveState.isAir()) {
                                this.level().setBlock(pos, Blocks.WATER.defaultBlockState(), 3);
                                break;
                            }
                            if (bid.getFluidState().is(net.minecraft.world.level.material.Fluids.LAVA)
                                    && aboveState.isAir()) {
                                this.level().setBlock(pos, Blocks.WATER.defaultBlockState(), 3);
                                break;
                            }
                            if (bid.isAir() && j > 0) {
                                break;
                            }
                        }
                    }
                }

                // spawn Butterfly / Bird nearby
                for (int m = 0; m < 2; m++) {
                    int i = this.random.nextInt(4) - this.random.nextInt(4);
                    int k = this.random.nextInt(4) - this.random.nextInt(4);
                    int j = 1 + this.random.nextInt(4);
                    BlockPos spawnPos = BlockPos.containing(this.getX() + i, this.getY() + j, this.getZ() + k);
                    if (this.level().getBlockState(spawnPos).isAir()) {
                        spawnCritter(spawnPos);
                    }
                }
            }
            this.attack_level = 1;
        }

        if (!OreSpawnPet.isSitting(this, PET_FLAGS)) {
            if (this.activity == 0) {
                this.setActivity(1);
            }

            if (this.random.nextInt(100) == 1) {
                if (this.random.nextInt(20) == 1) {
                    this.setActivity(2);
                } else {
                    this.setActivity(1);
                }
            }

            this.owner_flying = 0;
            if (OreSpawnPet.isTame(this, PET_FLAGS) && OreSpawnPet.getOwner(this, OWNER) != null) {
                LivingEntity e = OreSpawnPet.getOwner(this, OWNER);
                if (e instanceof Player player && player.getAbilities().flying) {
                    this.owner_flying = 1;
                    this.setActivity(2);
                }
            }

            if (this.activity == 1
                    && OreSpawnPet.isTame(this, PET_FLAGS)
                    && OreSpawnPet.getOwner(this, OWNER) != null) {
                LivingEntity e = OreSpawnPet.getOwner(this, OWNER);
                if (e != null && this.distanceToSqr(e) > 256.0) {
                    this.setActivity(2);
                }
            }

            this.do_movement();
        } else if (OreSpawnPet.isTame(this, PET_FLAGS) && OreSpawnPet.getOwner(this, OWNER) != null) {
            LivingEntity e = OreSpawnPet.getOwner(this, OWNER);
            if (e != null && this.distanceToSqr(e) > 256.0) {
                OreSpawnPet.setSitting(this, PET_FLAGS, false);
                this.setActivity(2);
            }
        }

        // day counter for grow system (gold tracks day transitions)
        if (this.is_day == 0) {
            this.is_day = 1;
            if (!this.level().isDay()) {
                this.is_day = -1;
            }
        } else {
            if (this.is_day == -1 && this.level().isDay()) {
                this.day_count++;
            }
            this.is_day = 1;
            if (!this.level().isDay()) {
                this.is_day = -1;
            }
        }
    }

    private BlockState pickBlessingFlower() {
        // gold: poppy/dandelion + OreSpawn flowers — vanilla stand-ins for missing crystal flowers
        int which = this.random.nextInt(8);
        return switch (which) {
            case 0 -> Blocks.POPPY.defaultBlockState();
            case 1 -> Blocks.DANDELION.defaultBlockState();
            case 2 -> Blocks.BLUE_ORCHID.defaultBlockState();
            case 3 -> Blocks.PINK_TULIP.defaultBlockState();
            case 4 -> Blocks.RED_TULIP.defaultBlockState();
            case 5 -> Blocks.ALLIUM.defaultBlockState();
            case 6 -> Blocks.CORNFLOWER.defaultBlockState();
            default -> Blocks.OXEYE_DAISY.defaultBlockState();
        };
    }

    private void spawnCritter(BlockPos pos) {
        EntityType<? extends Mob> type =
                this.random.nextInt(2) == 0 ? ModEntities.BUTTERFLY.get() : ModEntities.BIRD.get();
        Mob mob = type.create(this.level());
        if (mob != null) {
            mob.moveTo(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, this.random.nextFloat() * 360.0F, 0.0F);
            this.level().addFreshEntity(mob);
        }
    }

    private void do_movement() {
        int xdir = 1;
        int zdir = 1;
        int keep_trying = 10;
        int do_new = 0;
        double ox = 0.0;
        double oy = 0.0;
        double oz = 0.0;
        int has_owner = 0;
        double pi = 3.1415926545;
        LivingEntity e = null;

        if (this.currentFlightTarget == null) {
            do_new = 1;
            this.currentFlightTarget = BlockPos.containing(this.getX(), this.getY(), this.getZ());
        }

        if (this.activity == 2 && this.random.nextInt(300) == 0) {
            do_new = 1;
        }

        if (OreSpawnPet.isTame(this, PET_FLAGS) && OreSpawnPet.getOwner(this, OWNER) != null) {
            e = OreSpawnPet.getOwner(this, OWNER);
            if (e != null) {
                has_owner = 1;
                ox = e.getX();
                oy = e.getY() + 1.0;
                oz = e.getZ();
                if (this.distanceToSqr(e) > 100.0) {
                    do_new = 1;
                }
                if (this.owner_flying != 0 && this.distanceToSqr(e) > 36.0) {
                    do_new = 1;
                }
            }
        }

        if (this.random.nextInt(7) == 1 && this.level().getDifficulty() != Difficulty.PEACEFUL) {
            e = this.findSomethingToAttack();
            if (e != null) {
                if (OreSpawnPet.isTame(this, PET_FLAGS)
                        && this.getHealth() / this.mygetMaxHealth() < 0.25F) {
                    this.setActivity(2);
                    this.setAttacking(0);
                    do_new = 0;
                    this.currentFlightTarget = BlockPos.containing(
                            this.getX() + (this.getX() - e.getX()),
                            this.getY() + 1.0,
                            this.getZ() + (this.getZ() - e.getZ()));
                } else {
                    this.setActivity(2);
                    this.setAttacking(1);
                    this.currentFlightTarget =
                            BlockPos.containing(e.getX(), e.getY() + 1.0, e.getZ());
                    do_new = 0;
                    double reach = 3.0F + e.getBbWidth() / 2.0F;
                    if (this.distanceToSqr(e) < reach * reach) {
                        this.doHurtTarget(e);
                    } else if (this.distanceToSqr(e) > 25.0
                            && this.distanceToSqr(e) < 144.0
                            && !this.isInWater()
                            && this.getSpyroFire() != 0
                            && (this.random.nextInt(3) == 0 || this.random.nextInt(4) == 1)) {
                        int which = this.random.nextInt(3);
                        double rr = Math.atan2(e.getZ() - this.getZ(), e.getX() - this.getX());
                        double rhdir = Math.toRadians((this.getYRot() + 90.0F) % 360.0F);
                        double rdd = Math.abs(rr - rhdir) % (pi * 2.0);
                        if (rdd > pi) {
                            rdd -= pi * 2.0;
                        }
                        rdd = Math.abs(rdd);
                        if (rdd < 0.5) {
                            if (which == 0) {
                                this.firecanon(e);
                            } else if (which == 1) {
                                this.firecanonl(e);
                            } else {
                                this.firecanoni(e);
                            }
                        }
                    }
                }
            } else {
                this.setAttacking(0);
            }
        }

        if (this.activity != 1) {
            if (this.currentFlightTarget.distSqr(
                            BlockPos.containing(this.getX(), this.getY(), this.getZ()))
                    < 2.1F) {
                do_new = 1;
            }

            if (do_new != 0) {
                BlockState bid = Blocks.STONE.defaultBlockState();
                while (!bid.isAir() && keep_trying != 0) {
                    keep_trying--;
                    int gox = (int) this.getX();
                    int goy = (int) this.getY();
                    int goz = (int) this.getZ();
                    if (has_owner == 1) {
                        gox = (int) ox;
                        goy = (int) oy;
                        goz = (int) oz;
                        if (this.owner_flying == 0) {
                            zdir = this.random.nextInt(4) + 6;
                            xdir = this.random.nextInt(4) + 6;
                        } else {
                            zdir = this.random.nextInt(8);
                            xdir = this.random.nextInt(8);
                        }
                    } else {
                        zdir = this.random.nextInt(5) + 6;
                        xdir = this.random.nextInt(5) + 6;
                    }
                    if (this.random.nextInt(2) == 0) zdir = -zdir;
                    if (this.random.nextInt(2) == 0) xdir = -xdir;
                    this.currentFlightTarget = new BlockPos(
                            gox + xdir,
                            goy + (this.random.nextInt(6 + this.owner_flying * 2) - 2),
                            goz + zdir);
                    bid = this.level().getBlockState(this.currentFlightTarget);
                }
            }

            double speed_factor = 1.0;
            double var1 = this.currentFlightTarget.getX() + 0.5 - this.getX();
            double var3 = this.currentFlightTarget.getY() + 0.1 - this.getY();
            double var5 = this.currentFlightTarget.getZ() + 0.5 - this.getZ();
            if (this.owner_flying != 0) {
                speed_factor = 1.75;
                if (OreSpawnPet.isTame(this, PET_FLAGS) && OreSpawnPet.getOwner(this, OWNER) != null) {
                    e = OreSpawnPet.getOwner(this, OWNER);
                    if (e != null && this.distanceToSqr(e) > 49.0) {
                        speed_factor = 3.5;
                    }
                }
            }

            Vec3 m = this.getDeltaMovement();
            this.setDeltaMovement(
                    m.x + (Math.signum(var1) * 0.5 - m.x) * 0.15 * speed_factor,
                    m.y + (Math.signum(var3) * 0.7 - m.y) * 0.21 * speed_factor,
                    m.z + (Math.signum(var5) * 0.5 - m.z) * 0.15 * speed_factor);
            float var7 =
                    (float) (Math.atan2(this.getDeltaMovement().z, this.getDeltaMovement().x) * 180.0
                                    / Math.PI)
                            - 90.0F;
            float var8 = Mth.wrapDegrees(var7 - this.getYRot());
            this.setZza((float) (0.75 * speed_factor));
            this.setYRot(this.getYRot() + var8 / 3.0F);
        }
    }

    private boolean isSuitableTarget(LivingEntity par1EntityLiving) {
        if (this.level().getDifficulty() == Difficulty.PEACEFUL) {
            return false;
        }
        if (par1EntityLiving == null || par1EntityLiving == this || !par1EntityLiving.isAlive()) {
            return false;
        }
        if (OreSpawnPet.isOwnedBy(this, OWNER, par1EntityLiving)) {
            return false;
        }
        if (!this.hasLineOfSight(par1EntityLiving)) {
            return false;
        }
        // gold MyUtils.isRoyalty — Princess + Prince family
        if (par1EntityLiving instanceof ThePrincess || par1EntityLiving instanceof ThePrince) {
            return false;
        }
        if (par1EntityLiving instanceof Monster) {
            return true;
        }
        if (par1EntityLiving instanceof Mothra) {
            return true;
        }
        if (par1EntityLiving instanceof Dragonfly) {
            return true;
        }
        return par1EntityLiving instanceof Mosquito;
    }

    @Nullable
    private LivingEntity findSomethingToAttack() {
        if (OreSpawnMain.PlayNicely != 0) {
            return null;
        }
        List<LivingEntity> list = this.level().getEntitiesOfClass(
                LivingEntity.class, this.getBoundingBox().inflate(12.0, 6.0, 12.0));
        LivingEntity best = null;
        double bestDist = Double.MAX_VALUE;
        for (LivingEntity living : list) {
            if (this.isSuitableTarget(living)
                    && this.canSeeTarget(living.getX(), living.getY(), living.getZ())) {
                double d = this.distanceToSqr(living);
                if (d < bestDist) {
                    bestDist = d;
                    best = living;
                }
            }
        }
        return best;
    }

    /** Gold firecanon → BetterFireball (big or small 50%). */
    private void firecanon(LivingEntity e) {
        double yoff = 1.0;
        double xzoff = 3.0;
        double cx = this.getX() - xzoff * Math.sin(Math.toRadians(this.getYRot()));
        double cz = this.getZ() + xzoff * Math.cos(Math.toRadians(this.getYRot()));
        float r1 = 5.0F * (this.random.nextFloat() - this.random.nextFloat());
        float r2 = 3.0F * (this.random.nextFloat() - this.random.nextFloat());
        float r3 = 5.0F * (this.random.nextFloat() - this.random.nextFloat());
        Vec3 dir = new Vec3(
                e.getX() - cx + r1,
                e.getY() + e.getBbHeight() / 2.0F - (this.getY() + yoff) + r2,
                e.getZ() - cz + r3);
        BetterFireball bf = new BetterFireball(this.level(), this, dir);
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

    /** Gold firecanonl → ThunderBolt (deferred). Stand-in: SmallFireball. */
    private void firecanonl(LivingEntity e) {
        double yoff = 1.0;
        double xzoff = 3.0;
        double cx = this.getX() - xzoff * Math.sin(Math.toRadians(this.getYRot()));
        double cz = this.getZ() + xzoff * Math.cos(Math.toRadians(this.getYRot()));
        this.playSound(
                SoundEvents.ARROW_SHOOT,
                1.0F,
                1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
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

    /** Gold firecanoni → IceBall + setIceMaker(1) (deferred). Stand-in: SmallFireball. */
    private void firecanoni(LivingEntity e) {
        double yoff = 1.0;
        double xzoff = 3.0;
        double cx = this.getX() - xzoff * Math.sin(Math.toRadians(this.getYRot()));
        double cz = this.getZ() + xzoff * Math.cos(Math.toRadians(this.getYRot()));
        this.playSound(
                SoundEvents.ARROW_SHOOT,
                1.0F,
                1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
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

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (player.distanceToSqr(this) >= 16.0) {
            return InteractionResult.PASS;
        }

        // gold: diamond block instant tame + max grow flags
        if (stack.is(Blocks.DIAMOND_BLOCK.asItem())) {
            if (!this.level().isClientSide) {
                OreSpawnPet.tame(this, PET_FLAGS, OWNER, player);
                this.heal(this.mygetMaxHealth() - this.getHealth());
                this.ok_to_grow = 1;
                this.kill_count = 1000;
                this.fed_count = 1000;
                this.day_count = 1000;
            }
            if (!player.getAbilities().instabuild) {
                stack.shrink(1);
            }
            return InteractionResult.SUCCESS;
        }

        // gold: food heals nutrition*10 when owned
        if (OreSpawnPet.isTame(this, PET_FLAGS)
                && OreSpawnPet.isOwnedBy(this, OWNER, player)
                && isFood(stack)) {
            if (!this.level().isClientSide) {
                var food = stack.getFoodProperties(this);
                if (food != null && this.getHealth() < this.mygetMaxHealth()) {
                    this.heal(food.nutrition() * 10.0F);
                }
                this.level().broadcastEntityEvent(this, (byte) 7);
                this.fed_count++;
            }
            if (!player.getAbilities().instabuild) {
                stack.shrink(1);
            }
            return InteractionResult.SUCCESS;
        }

        if (OreSpawnPet.isTame(this, PET_FLAGS) && OreSpawnPet.isOwnedBy(this, OWNER, player)) {
            if (stack.is(Items.ICE)) {
                if (!this.level().isClientSide) {
                    this.level().broadcastEntityEvent(this, (byte) 6);
                    this.setSpyroFire(0);
                    player.displayClientMessage(Component.literal("Princess fireballs extinguished."), true);
                }
                if (!player.getAbilities().instabuild) {
                    stack.shrink(1);
                }
                return InteractionResult.SUCCESS;
            }
            if (stack.is(Items.FLINT_AND_STEEL)) {
                if (!this.level().isClientSide) {
                    this.level().broadcastEntityEvent(this, (byte) 6);
                    this.setSpyroFire(1);
                    player.displayClientMessage(Component.literal("Princess fireballs lit!"), true);
                }
                if (!player.getAbilities().instabuild) {
                    stack.shrink(1);
                }
                return InteractionResult.SUCCESS;
            }
            if (stack.is(Items.NAME_TAG)) {
                if (!this.level().isClientSide) {
                    this.setCustomName(stack.getHoverName());
                }
                if (!player.getAbilities().instabuild) {
                    stack.shrink(1);
                }
                return InteractionResult.SUCCESS;
            }
            // empty-hand / other: sit toggle
            if (!this.level().isClientSide) {
                if (!OreSpawnPet.isSitting(this, PET_FLAGS)) {
                    OreSpawnPet.setSitting(this, PET_FLAGS, true);
                    this.setActivity(1);
                } else {
                    OreSpawnPet.setSitting(this, PET_FLAGS, false);
                }
            }
            return InteractionResult.SUCCESS;
        }

        return InteractionResult.PASS;
    }

    @Override
    public void handleEntityEvent(byte id) {
        if (id == 7) {
            for (int i = 0; i < 7; i++) {
                this.level()
                        .addParticle(
                                ParticleTypes.HEART,
                                this.getRandomX(1.0),
                                this.getRandomY() + 0.5,
                                this.getRandomZ(1.0),
                                this.random.nextGaussian() * 0.02,
                                this.random.nextGaussian() * 0.02,
                                this.random.nextGaussian() * 0.02);
            }
        } else if (id == 6) {
            for (int i = 0; i < 7; i++) {
                this.level()
                        .addParticle(
                                ParticleTypes.SMOKE,
                                this.getRandomX(1.0),
                                this.getRandomY() + 0.5,
                                this.getRandomZ(1.0),
                                this.random.nextGaussian() * 0.02,
                                this.random.nextGaussian() * 0.02,
                                this.random.nextGaussian() * 0.02);
            }
        } else {
            super.handleEntityEvent(id);
        }
    }

    @Override
    protected SoundEvent getAmbientSound() {
        if (OreSpawnPet.isSitting(this, PET_FLAGS)) {
            return null;
        }
        //  gold: roar only when attacking — roar event may be unregistered; null until SoundsHandler
        return null;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return danger.orespawn.util.handlers.SoundsHandler.ENTITY_DUCK_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return danger.orespawn.util.handlers.SoundsHandler.ENTITY_CRYO_DEATH.get();
    }

    @Override
    protected float getSoundVolume() {
        return 0.6F;
    }

    @Override
    public float getVoicePitch() {
        return (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.5F;
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
    protected void dropCustomDeathLoot(ServerLevel level, DamageSource damageSource, boolean recentlyHit) {
        super.dropCustomDeathLoot(level, damageSource, recentlyHit);
        // gold: 1+rand(4) beef
        int var3 = 1 + this.random.nextInt(4);
        for (int var4 = 0; var4 < var3; var4++) {
            this.spawnAtLocation(new ItemStack(Items.BEEF));
        }
    }

    public float getAttackStrength(Entity par1Entity) {
        return 9.0F;
    }

    @Override
    public boolean doHurtTarget(Entity target) {
        if (OreSpawnPet.isOwnedBy(this, OWNER, target)) {
            return false;
        }
        boolean var4 = target.hurt(this.damageSources().mobAttack(this), this.getAttackStrength(target));
        if (var4 && target instanceof LivingEntity el) {
            if (el.getHealth() <= 0.0F) {
                this.kill_count++;
            }
        }
        return var4;
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if ("inWall".equals(source.getMsgId())) {
            return false;
        }
        if ("cactus".equals(source.getMsgId())) {
            return false;
        }
        boolean ret = super.hurt(source, amount);
        if (ret) {
            OreSpawnPet.setSitting(this, PET_FLAGS, false);
            this.setActivity(2);
        }
        return ret;
    }

    @Override
    public boolean checkSpawnRules(LevelAccessor level, MobSpawnType spawnType) {
        // gold getCanSpawnHere: return true
        return true;
    }
}
