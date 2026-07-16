package danger.orespawn.entity;

import danger.orespawn.OreSpawnMain;
import danger.orespawn.entity.tame.OreSpawnPet;
import danger.orespawn.init.ModItems;
import danger.orespawn.util.ai.FollowOwnerGoal;
import danger.orespawn.util.ai.WanderALotGoal;
import danger.orespawn.util.handlers.SoundsHandler;
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
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

/**
 * Gold {@code Stinky} (EntityTameable) ported for NeoForge 1.21.1.
 * Size 0.75×0.75, speed 0.3, health 100, attack 10, armor 6, XP 35, follow 1000.
 * Activity 1 ground / 2 fly; skin 0.18 (stinkytexture1.19); coal-ore forage; burp/fart drops.
 * Tame food: raw beef 50%. FollowOwner 1.15/12/2. Registry size set in {@code ModEntities}.
 */
public class Stinky extends Monster {
    private static final EntityDataAccessor<Byte> ACTIVITY =
            SynchedEntityData.defineId(Stinky.class, EntityDataSerializers.BYTE);
    private static final EntityDataAccessor<Byte> FIRE =
            SynchedEntityData.defineId(Stinky.class, EntityDataSerializers.BYTE);
    private static final EntityDataAccessor<Integer> SKIN =
            SynchedEntityData.defineId(Stinky.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Byte> PET_FLAGS =
            SynchedEntityData.defineId(Stinky.class, EntityDataSerializers.BYTE);
    private static final EntityDataAccessor<Optional<UUID>> OWNER =
            SynchedEntityData.defineId(Stinky.class, EntityDataSerializers.OPTIONAL_UUID);

    private final float moveSpeed = 0.3F;

    @Nullable
    private BlockPos currentFlightTarget;
    /** Gold public activity mirror (1=ground, 2=fly). */
    public int activity = 1;
    private int owner_flying = 0;
    private int skin_color = -1;
    private int syncit = 0;
    private int closest = 99999;
    private int tx = 0;
    private int ty = 0;
    private int tz = 0;

    public Stinky(EntityType<? extends Stinky> type, Level level) {
        super(type, level);
        this.xpReward = 35;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 100.0) // mygetMaxHealth()
                .add(Attributes.MOVEMENT_SPEED, 0.3)
                .add(Attributes.ATTACK_DAMAGE, 10.0)
                .add(Attributes.ARMOR, 6.0) // func_70658_aO
                .add(Attributes.FOLLOW_RANGE, 1000.0); // gold field_70174_ab
    }

    /** Gold was EntityTameable, not EntityMob — never wipe on Peaceful. */
    @Override
    protected boolean shouldDespawnInPeaceful() {
        return false;
    }

    @Override
    public boolean fireImmune() {
        // gold field_70178_ae = true
        return true;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(ACTIVITY, (byte) 1);
        builder.define(FIRE, (byte) 1);
        builder.define(SKIN, 0);
        builder.define(PET_FLAGS, (byte) 0);
        builder.define(OWNER, Optional.empty());
    }

    @Override
    protected void registerGoals() {
        // gold: swim, avoid mob, follow owner, tempt beef, panic, watch, wander, idle, indoors
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(2, new AvoidEntityGoal<>(this, Monster.class, 8.0F, 0.3, 0.4));
        this.goalSelector.addGoal(
                3,
                new FollowOwnerGoal(
                        this,
                        () -> OreSpawnPet.getOwnerUUID(this, OWNER),
                        () -> OreSpawnPet.isSitting(this, PET_FLAGS),
                        1.15,
                        12.0F,
                        2.0F));
        this.goalSelector.addGoal(4, new TemptGoal(this, 1.25, Ingredient.of(Items.BEEF), false));
        this.goalSelector.addGoal(5, new PanicGoal(this, 1.5));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(7, new WanderALotGoal(this, 10, 0.75));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("SpyroActivity", this.getActivity());
        tag.putInt("SpyroFire", this.getSpyroFire());
        tag.putInt("StinkySkin", this.getSkin());
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
        if (tag.contains("StinkySkin")) {
            this.setSkin(tag.getInt("StinkySkin"));
        }
        OreSpawnPet.readAdditionalSaveData(this, PET_FLAGS, OWNER, tag);
        if (OreSpawnPet.isTame(this, PET_FLAGS)) {
            this.setPersistenceRequired();
        }
    }

    public int mygetMaxHealth() {
        return 100;
    }

    public int getActivity() {
        int i = this.entityData.get(ACTIVITY);
        this.activity = i;
        return i;
    }

    public void setActivity(int par1) {
        this.activity = par1;
        this.entityData.set(ACTIVITY, (byte) par1);
    }

    public int getSpyroFire() {
        return this.entityData.get(FIRE);
    }

    public void setSpyroFire(int par1) {
        this.entityData.set(FIRE, (byte) par1);
    }

    public int getSkin() {
        int i = this.entityData.get(SKIN);
        this.skin_color = i;
        return i;
    }

    public void setSkin(int par1) {
        this.skin_color = par1;
        this.entityData.set(SKIN, par1);
    }

    public boolean isOreSpawnTame() {
        return OreSpawnPet.isTame(this, PET_FLAGS);
    }

    public boolean isOreSpawnSitting() {
        return OreSpawnPet.isSitting(this, PET_FLAGS);
    }

    private static boolean isTameFood(ItemStack stack) {
        // gold: Items.field_151082_bd raw beef
        return !stack.isEmpty() && stack.is(Items.BEEF);
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        // gold: babies false; tamed false; else !noDespawnRequired
        if (this.isBaby()) {
            return false;
        }
        if (OreSpawnPet.isTame(this, PET_FLAGS)) {
            return false;
        }
        return !this.isPersistenceRequired();
    }

    @Override
    protected SoundEvent getAmbientSound() {
        //  gold: null
        return null;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundsHandler.ENTITY_DUCK_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundsHandler.ENTITY_CRYO_DEATH.get();
    }

    @Override
    protected float getSoundVolume() {
        return 0.6F;
    }

    @Override
    public float getVoicePitch() {
        return this.isBaby()
                ? (this.random.nextFloat() - this.random.nextFloat()) * 0.1F + 1.5F
                : (this.random.nextFloat() - this.random.nextFloat()) * 0.1F + 1.0F;
    }

    @Override
    protected void dropCustomDeathLoot(ServerLevel level, DamageSource damageSource, boolean recentlyHit) {
        super.dropCustomDeathLoot(level, damageSource, recentlyHit);
        // gold: drops beef only when tamed (1.4 +1 roll)
        if (!OreSpawnPet.isTame(this, PET_FLAGS)) {
            return;
        }
        int var3 = this.random.nextInt(4) + 1;
        for (int i = 0; i < var3; i++) {
            this.spawnAtLocation(new ItemStack(Items.BEEF));
        }
    }

    @Override
    public boolean causeFallDamage(float fallDistance, float multiplier, DamageSource source) {
        return false;
    }

    @Override
    protected void checkFallDamage(double y, boolean onGround, BlockState state, BlockPos pos) {}

    @Override
    public boolean isIgnoringBlockTriggers() {
        // gold func_145773_az = false
        return false;
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

    private void dropItemFront(Item index, int par1) {
        float f = 0.75F + Math.abs(this.random.nextFloat() * 0.75F);
        double yaw = Math.toRadians(this.yBodyRot);
        ItemEntity var3 = new ItemEntity(
                this.level(),
                this.getX() - f * Math.sin(yaw),
                this.getY() + 0.9,
                this.getZ() + f * Math.cos(yaw),
                new ItemStack(index, par1));
        this.level().addFreshEntity(var3);
    }

    private void dropItemRear(Item index, int par1) {
        float f = 0.55F + Math.abs(this.random.nextFloat() * 0.55F);
        double yaw = Math.toRadians(this.yBodyRot);
        ItemEntity var3 = new ItemEntity(
                this.level(),
                this.getX() + f * Math.sin(yaw),
                this.getY() + 0.25,
                this.getZ() - f * Math.cos(yaw),
                new ItemStack(index, par1));
        this.level().addFreshEntity(var3);
    }

    /**
     * Gold skin-indexed rear drop (fart loot). MCP fields from gold 1.7.10:
     * 0 blaze_powder, 1 rotten_flesh, 2 melon_seeds, 3 uranium, 4 wheat, 5 brick,
     * 6 torch, 7 emerald, 8 gold_ingot, 9 leaves, 10 titanium, 11 MyAppleSeed,
     * 12 diamond, 13 sand, 14 cobble, 15 bone, 16 string, 17 cherry seed, 18 peach seed.
     * Seeds → apple stand-in until plant-seed items registered.
     */
    private void dropFartLoot() {
        switch (this.skin_color) {
            case 0 -> this.dropItemRear(Items.BLAZE_POWDER, 1); // field_151065_br
            case 1 -> this.dropItemRear(Items.ROTTEN_FLESH, 1); // field_151078_bh
            case 2 -> this.dropItemRear(Items.MELON_SEEDS, 1); // field_151081_bc
            case 3 -> this.dropItemRear(ModItems.URANIUM_NUGGET.get(), 1);
            case 4 -> this.dropItemRear(Items.WHEAT, 1); // field_151015_O
            case 5 -> this.dropItemRear(Items.BRICK, 1); // field_151118_aC
            case 6 -> this.dropItemRear(Items.TORCH, 1); // Blocks.torch
            case 7 -> this.dropItemRear(Items.EMERALD, 1); // field_151166_bC
            case 8 -> this.dropItemRear(Items.GOLD_INGOT, 1); // field_151043_k
            case 9 -> this.dropItemRear(Items.OAK_LEAVES, 1); // Blocks.leaves
            case 10 -> this.dropItemRear(ModItems.TITANIUM_NUGGET.get(), 1);
            case 11 -> this.dropItemRear(Items.APPLE, 1); // MyAppleSeed stand-in
            case 12 -> this.dropItemRear(Items.DIAMOND, 1); // field_151045_i
            case 13 -> this.dropItemRear(Items.SAND, 1); // Blocks.sand
            case 14 -> this.dropItemRear(Items.COBBLESTONE, 1); // Blocks.cobblestone
            case 15 -> this.dropItemRear(Items.BONE, 1); // field_151103_aS
            case 16 -> this.dropItemRear(Items.STRING, 1); // field_151007_F
            case 17 -> this.dropItemRear(Items.APPLE, 1); // MyCherrySeed stand-in
            case 18 -> this.dropItemRear(Items.APPLE, 1); // MyPeachSeed stand-in
            default -> {}
        }
    }

    private SoundEvent pickFartSound() {
        return switch (this.random.nextInt(9)) {
            case 0 -> SoundsHandler.ENTITY_STINKBUG_FART1.get();
            case 1 -> SoundsHandler.ENTITY_STINKBUG_FART2.get();
            case 2 -> SoundsHandler.ENTITY_STINKBUG_FART3.get();
            case 3 -> SoundsHandler.ENTITY_STINKBUG_FART4.get();
            case 4 -> SoundsHandler.ENTITY_STINKBUG_FART5.get();
            case 5 -> SoundsHandler.ENTITY_STINKBUG_FART6.get();
            case 6 -> SoundsHandler.ENTITY_STINKBUG_FART7.get();
            case 7 -> SoundsHandler.ENTITY_STINKBUG_FART8.get();
            default -> SoundsHandler.ENTITY_STINKBUG_FART9.get();
        };
    }

    @Override
    public void tick() {
        if (this.getAttribute(Attributes.MOVEMENT_SPEED) != null) {
            this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(this.moveSpeed);
        }
        if (OreSpawnPet.isSitting(this, PET_FLAGS)) {
            this.getNavigation().stop();
        }
        super.tick();

        // gold func_70071_h_: burp coal / fart loot
        if (!this.level().isClientSide && this.random.nextInt(1750) == 1) {
            this.playSound(SoundEvents.PLAYER_BURP, 1.0F, 1.0F);
            this.dropItemFront(Items.COAL, 1);
        }
        if (!this.level().isClientSide && this.random.nextInt(2000) == 2) {
            this.playSound(this.pickFartSound(), 1.0F, 1.5F);
            this.dropFartLoot();
        }

        // gold livingUpdate (func_70636_d) bits
        if (this.isInWater()) {
            this.setDeltaMovement(this.getDeltaMovement().add(0.0, 0.07, 0.0));
        }

        if (!this.level().isClientSide && this.random.nextInt(2000) == 1) {
            this.setSkin(this.random.nextInt(19));
        }

        if (this.currentFlightTarget == null) {
            this.currentFlightTarget = BlockPos.containing(this.getX(), this.getY(), this.getZ());
        }

        if (this.skin_color < 0) {
            this.setSkin(this.random.nextInt(19));
        }

        this.syncit++;
        if (this.syncit > 20) {
            this.syncit = 0;
            if (this.level().isClientSide) {
                this.getActivity();
                this.getSkin();
            } else {
                this.setActivity(this.activity);
                this.setSkin(this.skin_color);
            }
        }

        // gold: activity 2 damps vertical motion
        if (this.activity == 2) {
            Vec3 m = this.getDeltaMovement();
            this.setDeltaMovement(m.x, m.y * 0.6, m.z);
        }

        if (!this.level().isClientSide && !OreSpawnPet.isSitting(this, PET_FLAGS)) {
            this.do_movement();
        }
    }

    /** Gold scan_it: coal ore shells. */
    private boolean scan_it(int x, int y, int z, int dx, int dy, int dz) {
        int found = 0;

        for (int i = -dy; i <= dy; i++) {
            for (int j = -dz; j <= dz; j++) {
                if (this.isCoalOre(this.level().getBlockState(new BlockPos(x + dx, y + i, z + j)))) {
                    int d = dx * dx + j * j + i * i;
                    if (d < this.closest) {
                        this.closest = d;
                        this.tx = x + dx;
                        this.ty = y + i;
                        this.tz = z + j;
                        found++;
                    }
                }
                if (this.isCoalOre(this.level().getBlockState(new BlockPos(x - dx, y + i, z + j)))) {
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

        for (int i = -dx; i <= dx; i++) {
            for (int j = -dz; j <= dz; j++) {
                if (this.isCoalOre(this.level().getBlockState(new BlockPos(x + i, y + dy, z + j)))) {
                    int d = dy * dy + j * j + i * i;
                    if (d < this.closest) {
                        this.closest = d;
                        this.tx = x + i;
                        this.ty = y + dy;
                        this.tz = z + j;
                        found++;
                    }
                }
                if (this.isCoalOre(this.level().getBlockState(new BlockPos(x + i, y - dy, z + j)))) {
                    int d = dy * dy + j * j + i * i;
                    if (d < this.closest) {
                        this.closest = d;
                        this.tx = x + i;
                        this.ty = y - dy;
                        this.tz = z + j;
                        found++;
                    }
                }
            }
        }

        for (int i = -dx; i <= dx; i++) {
            for (int j = -dy; j <= dy; j++) {
                if (this.isCoalOre(this.level().getBlockState(new BlockPos(x + i, y + j, z + dz)))) {
                    int d = dz * dz + j * j + i * i;
                    if (d < this.closest) {
                        this.closest = d;
                        this.tx = x + i;
                        this.ty = y + j;
                        this.tz = z + dz;
                        found++;
                    }
                }
                if (this.isCoalOre(this.level().getBlockState(new BlockPos(x + i, y + j, z - dz)))) {
                    int d = dz * dz + j * j + i * i;
                    if (d < this.closest) {
                        this.closest = d;
                        this.tx = x + i;
                        this.ty = y + j;
                        this.tz = z - dz;
                        found++;
                    }
                }
            }
        }

        return found != 0;
    }

    private boolean isCoalOre(BlockState state) {
        return state.is(Blocks.COAL_ORE) || state.is(Blocks.DEEPSLATE_COAL_ORE);
    }

    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
        if (this.isDeadOrDying()) {
            return;
        }

        if (this.random.nextInt(200) == 1) {
            this.setTarget(null);
            this.setLastHurtByMob(null);
        }

        // gold: when activity==2 skip super path AI goals pathing only — goals still run via super above

        if (this.random.nextInt(100) == 1 && this.getHealth() < this.mygetMaxHealth()) {
            this.heal(1.0F);
        }

        if (OreSpawnPet.isSitting(this, PET_FLAGS)) {
            return;
        }

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
            LivingEntity owner = OreSpawnPet.getOwner(this, OWNER);
            if (owner instanceof Player player && player.getAbilities().flying) {
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
    }

    private void do_movement() {
        int xdir = 1;
        int zdir = 1;
        int keep_trying = 50;
        int do_new = 0;
        double ox = 0.0;
        double oy = 0.0;
        double oz = 0.0;
        int has_owner = 0;
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
                oy = e.getY();
                oz = e.getZ();
                if (this.distanceToSqr(e) > 100.0) {
                    do_new = 1;
                }
                if (this.owner_flying != 0 && this.distanceToSqr(e) > 36.0) {
                    do_new = 1;
                }
            }
        }

        // gold combat nextInt(7)==1
        if (this.random.nextInt(7) == 1 && this.level().getDifficulty() != Difficulty.PEACEFUL) {
            e = this.findSomethingToAttack();
            if (e != null) {
                if (OreSpawnPet.isTame(this, PET_FLAGS)
                        && this.getHealth() / this.mygetMaxHealth() < 0.25F) {
                    this.setActivity(2);
                    do_new = 0;
                    this.currentFlightTarget = BlockPos.containing(
                            this.getX() + (this.getX() - e.getX()),
                            this.getY() + 1.0,
                            this.getZ() + (this.getZ() - e.getZ()));
                } else {
                    this.setActivity(2);
                    this.currentFlightTarget =
                            BlockPos.containing(e.getX(), e.getY() + 1.0, e.getZ());
                    do_new = 0;
                    double reach = 3.0F + e.getBbWidth() / 2.0F;
                    if (this.distanceToSqr(e) < reach * reach) {
                        this.doHurtTarget(e);
                    }
                }
            }
        }

        if (this.activity == 1) {
            // ground: coal ore forage
            if (this.random.nextInt(50) == 0 && OreSpawnMain.PlayNicely == 0) {
                this.closest = 99999;
                this.tx = this.ty = this.tz = 0;
                for (int i = 1; i < 9; i++) {
                    int j = i;
                    if (j > 2) {
                        j = 2;
                    }
                    if (this.scan_it((int) this.getX(), (int) this.getY() + 1, (int) this.getZ(), i, j, i)) {
                        break;
                    }
                    if (i >= 4) {
                        i++;
                    }
                }
                if (this.closest < 99999) {
                    this.getNavigation().moveTo(this.tx, this.ty, this.tz, 1.25);
                    if (this.closest < 12) {
                        this.level().setBlock(new BlockPos(this.tx, this.ty, this.tz), Blocks.AIR.defaultBlockState(), 2);
                        this.heal(1.0F);
                        this.playSound(
                                SoundEvents.PLAYER_BURP,
                                0.5F,
                                this.random.nextFloat() * 0.2F + 1.5F);
                    }
                }
            }
        } else {
            // flight steering
            if (this.currentFlightTarget.distToCenterSqr(this.getX(), this.getY(), this.getZ()) < 2.1F) {
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

                    if (this.random.nextInt(2) == 0) {
                        zdir = -zdir;
                    }
                    if (this.random.nextInt(2) == 0) {
                        xdir = -xdir;
                    }

                    this.currentFlightTarget = new BlockPos(
                            gox + xdir,
                            goy + this.random.nextInt(6 + this.owner_flying * 2) - 2,
                            goz + zdir);
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
            float var7 = (float) (Math.atan2(this.getDeltaMovement().z, this.getDeltaMovement().x) * 180.0 / Math.PI)
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
        // gold: Mothra OR EntityMob
        return par1EntityLiving instanceof Mothra || par1EntityLiving instanceof Monster;
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

    private int findBuddies() {
        List<Stinky> list =
                this.level().getEntitiesOfClass(Stinky.class, this.getBoundingBox().inflate(20.0, 10.0, 20.0));
        return list.size();
    }

    @Override
    public boolean doHurtTarget(Entity target) {
        if (OreSpawnPet.isOwnedBy(this, OWNER, target)) {
            return false;
        }
        // gold getAttackStrength = 10
        return target.hurt(this.damageSources().mobAttack(this), 10.0F);
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        // gold: ignore cactus; else hurt + unsit + activity 2
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
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        // Prefer hand that holds tame food (main or offhand), matching Spyro pet pattern
        if (!isTameFood(stack)) {
            ItemStack other = OreSpawnPet.findFoodInHands(player, Stinky::isTameFood);
            if (!other.isEmpty()) {
                stack = other;
            }
        }

        // gold: distanceSq < 16
        if (player.distanceToSqr(this) >= 16.0) {
            return InteractionResult.PASS;
        }

        // gold: beef tame 50%
        if (isTameFood(stack)) {
            if (!OreSpawnPet.isTame(this, PET_FLAGS)) {
                if (!this.level().isClientSide) {
                    if (OreSpawnPet.tryTameWithFeedback(
                            this,
                            PET_FLAGS,
                            OWNER,
                            player,
                            2,
                            "Stinky is now yours!",
                            "Stinky sniffs the beef… try again!")) {
                        this.heal(this.mygetMaxHealth() - this.getHealth());
                    }
                }
            } else if (OreSpawnPet.isOwnedBy(this, OWNER, player)) {
                if (!this.level().isClientSide && this.getHealth() < this.mygetMaxHealth()) {
                    this.heal(this.mygetMaxHealth() - this.getHealth());
                    this.level().broadcastEntityEvent(this, (byte) 7);
                } else if (this.level().isClientSide) {
                    this.level().broadcastEntityEvent(this, (byte) 7);
                }
            }
            if (!player.getAbilities().instabuild) {
                stack.shrink(1);
            }
            return InteractionResult.SUCCESS;
        }

        // gold: Item.getItemFromBlock(Blocks.web) = cobweb untame
        if (OreSpawnPet.isTame(this, PET_FLAGS)
                && OreSpawnPet.isOwnedBy(this, OWNER, player)
                && stack.is(Items.COBWEB)) {
            if (!this.level().isClientSide) {
                OreSpawnPet.setTame(this, PET_FLAGS, false);
                OreSpawnPet.setOwnerUUID(this, OWNER, null);
                OreSpawnPet.setSitting(this, PET_FLAGS, false);
                this.setHealth(this.mygetMaxHealth());
                this.level().broadcastEntityEvent(this, (byte) 6);
                player.displayClientMessage(Component.literal("Stinky was released."), true);
            }
            if (!player.getAbilities().instabuild) {
                stack.shrink(1);
            }
            return InteractionResult.SUCCESS;
        }

        // gold: empty hand / other → sit toggle when owned
        if (OreSpawnPet.isTame(this, PET_FLAGS) && OreSpawnPet.isOwnedBy(this, OWNER, player)) {
            if (!this.level().isClientSide) {
                boolean nowSitting = !OreSpawnPet.isSitting(this, PET_FLAGS);
                OreSpawnPet.setSitting(this, PET_FLAGS, nowSitting);
                if (nowSitting) {
                    this.setActivity(1);
                    this.getNavigation().stop();
                }
                player.displayClientMessage(
                        Component.literal(nowSitting ? "Stinky is sitting." : "Stinky is following."),
                        true);
            }
            return InteractionResult.SUCCESS;
        }

        return InteractionResult.PASS;
    }

    @Override
    public void handleEntityEvent(byte id) {
        if (id == 7) {
            for (int i = 0; i < 7; i++) {
                double dx = this.random.nextGaussian() * 0.02;
                double dy = this.random.nextGaussian() * 0.02;
                double dz = this.random.nextGaussian() * 0.02;
                this.level()
                        .addParticle(
                                ParticleTypes.HEART,
                                this.getRandomX(1.0),
                                this.getRandomY() + 0.5,
                                this.getRandomZ(1.0),
                                dx,
                                dy,
                                dz);
            }
        } else if (id == 6) {
            for (int i = 0; i < 7; i++) {
                double dx = this.random.nextGaussian() * 0.02;
                double dy = this.random.nextGaussian() * 0.02;
                double dz = this.random.nextGaussian() * 0.02;
                this.level()
                        .addParticle(
                                ParticleTypes.SMOKE,
                                this.getRandomX(1.0),
                                this.getRandomY() + 0.5,
                                this.getRandomZ(1.0),
                                dx,
                                dy,
                                dz);
            }
        } else {
            super.handleEntityEvent(id);
        }
    }

    /** Gold {@code getCanSpawnHere}: daytime and buddies ≤ 2. */
    @Override
    public boolean checkSpawnRules(LevelAccessor level, MobSpawnType spawnType) {
        if (level instanceof Level lvl && !lvl.isDay()) {
            return false;
        }
        return this.findBuddies() <= 2;
    }
}
