package danger.orespawn.entity;

import danger.orespawn.OreSpawnMain;
import danger.orespawn.entity.tame.OreSpawnPet;
import danger.orespawn.util.ai.FollowOwnerGoal;
import danger.orespawn.util.ai.WanderALotGoal;
import danger.orespawn.util.handlers.SoundsHandler;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
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
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

/**
 * Gold {@code Gazelle} (EntityTameable) for NeoForge 1.21.1.
 * Size 0.6×1.8, speed 0.3, health 15, attack 0, XP 5, follow 100.
 * Tame food: apple (50%). Forages carrots/potatoes/grass; buddy seek; sit/follow.
 * Registry size set in {@code ModEntities.GAZELLE} .
 */
public class Gazelle extends Animal {
    private static final EntityDataAccessor<Byte> PET_FLAGS =
            SynchedEntityData.defineId(Gazelle.class, EntityDataSerializers.BYTE);
    private static final EntityDataAccessor<Optional<UUID>> OWNER =
            SynchedEntityData.defineId(Gazelle.class, EntityDataSerializers.OPTIONAL_UUID);

    private final float moveSpeed = 0.3F;
    private int closest = 99999;
    private int tx;
    private int ty;
    private int tz;

    public Gazelle(EntityType<? extends Gazelle> type, Level level) {
        super(type, level);
        this.xpReward = 5;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Animal.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 15.0) // mygetMaxHealth()
                .add(Attributes.MOVEMENT_SPEED, 0.3)
                .add(Attributes.ATTACK_DAMAGE, 0.0)
                .add(Attributes.FOLLOW_RANGE, 100.0); // gold field_70174_ab = 100
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(PET_FLAGS, (byte) 0);
        builder.define(OWNER, Optional.empty());
    }

    @Override
    protected void registerGoals() {
        // gold: Swimming, Mate, FollowOwner, Avoid Mob, Tempt apple, Panic,
        // Avoid Player, WatchClosest, Wander, LookIdle, MoveIndoors (skip POI)
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new BreedGoal(this, 1.0));
        this.goalSelector.addGoal(
                2,
                new FollowOwnerGoal(
                        this,
                        () -> OreSpawnPet.getOwnerUUID(this, OWNER),
                        () -> OreSpawnPet.isSitting(this, PET_FLAGS),
                        2.0,
                        10.0F,
                        2.0F));
        this.goalSelector.addGoal(3, new AvoidEntityGoal<>(this, Monster.class, 8.0F, 1.0, 1.7F));
        this.goalSelector.addGoal(4, new TemptGoal(this, 1.2, Ingredient.of(Items.APPLE), false));
        this.goalSelector.addGoal(5, new PanicGoal(this, 1.5));
        this.goalSelector.addGoal(6, new AvoidEntityGoal<>(this, Player.class, 12.0F, 1.0, 2.0F));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(8, new WanderALotGoal(this, 10, 1.0));
        this.goalSelector.addGoal(9, new RandomLookAroundGoal(this));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        OreSpawnPet.addAdditionalSaveData(this, PET_FLAGS, OWNER, tag);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        OreSpawnPet.readAdditionalSaveData(this, PET_FLAGS, OWNER, tag);
        if (OreSpawnPet.isTame(this, PET_FLAGS)) {
            this.setPersistenceRequired();
        }
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
    }

    /** Gold fall: damage only after −3 blocks, capped at 2. */
    @Override
    protected void checkFallDamage(double y, boolean onGround, BlockState state, BlockPos pos) {
        if (onGround && this.fallDistance > 0.0F) {
            float i = Mth.ceil(this.fallDistance - 3.0F);
            if (i > 0.0F) {
                if (i > 3.0F) {
                    this.playSound(SoundEvents.GENERIC_BIG_FALL, 1.0F, 1.0F);
                } else {
                    this.playSound(SoundEvents.GENERIC_SMALL_FALL, 1.0F, 1.0F);
                }
                if (i > 2.0F) {
                    i = 2.0F;
                }
                this.hurt(this.damageSources().fall(), i);
            }
            this.resetFallDistance();
        } else {
            super.checkFallDamage(y, onGround, state, pos);
        }
    }

    @Override
    protected void customServerAiStep() {
        if (!this.isDeadOrDying()) {
            if (this.random.nextInt(200) == 1) {
                this.setLastHurtByMob(null);
            }

            if (!OreSpawnPet.isSitting(this, PET_FLAGS)) {
                // gold: forage when hurt 1/30 or random 1/750
                if (((this.random.nextInt(30) == 0 && this.getHealth() < this.mygetMaxHealth())
                                || this.random.nextInt(750) == 1)
                        && OreSpawnMain.PlayNicely == 0) {
                    this.closest = 99999;
                    this.tx = this.ty = this.tz = 0;
                    for (int i = 1; i < 11; i++) {
                        int j = i;
                        if (j > 2) {
                            j = 2;
                        }
                        if (this.scanIt((int) this.getX(), (int) this.getY() + 1, (int) this.getZ(), i, j, i)) {
                            break;
                        }
                        if (i >= 6) {
                            i++;
                        }
                    }
                    if (this.closest < 99999) {
                        this.getNavigation().moveTo(this.tx, this.ty, this.tz, 1.0);
                        if (this.closest < 12) {
                            if (this.level().getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING)) {
                                this.level().removeBlock(new BlockPos(this.tx, this.ty, this.tz), false);
                            }
                            this.heal(1.0F);
                            this.playSound(
                                    SoundEvents.PLAYER_BURP,
                                    1.0F,
                                    this.level().getRandom().nextFloat() * 0.2F + 0.9F);
                        }
                    }
                }

                // gold: find buddy 1/250
                if (this.random.nextInt(250) == 1) {
                    Gazelle buddy = this.findBuddy();
                    if (buddy != null) {
                        this.getNavigation().moveTo(buddy, 0.5);
                    }
                }
            }

            // gold: passive heal 1/250
            if (this.random.nextInt(250) == 0) {
                this.heal(1.0F);
            }
        }
        super.customServerAiStep();
    }

    /** Gold edible: strawberry (deferred), carrots, potatoes, tallgrass, double_plant. */
    private boolean isEdiblePlant(BlockState state) {
        return state.is(Blocks.CARROTS)
                || state.is(Blocks.POTATOES)
                || state.is(Blocks.SHORT_GRASS)
                || state.is(Blocks.TALL_GRASS)
                || state.is(Blocks.FERN)
                || state.is(Blocks.LARGE_FERN)
                || state.is(Blocks.SUNFLOWER)
                || state.is(Blocks.LILAC)
                || state.is(Blocks.ROSE_BUSH)
                || state.is(Blocks.PEONY);
    }

    private boolean scanIt(int x, int y, int z, int dx, int dy, int dz) {
        int found = 0;
        for (int i = -dy; i <= dy; i++) {
            for (int j = -dz; j <= dz; j++) {
                BlockState bid = this.level().getBlockState(new BlockPos(x + dx, y + i, z + j));
                if (this.isEdiblePlant(bid)) {
                    int d = dx * dx + j * j + i * i;
                    if (d < this.closest) {
                        this.closest = d;
                        this.tx = x + dx;
                        this.ty = y + i;
                        this.tz = z + j;
                        found++;
                    }
                }
                bid = this.level().getBlockState(new BlockPos(x - dx, y + i, z + j));
                if (this.isEdiblePlant(bid)) {
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
                BlockState bid = this.level().getBlockState(new BlockPos(x + xi, y + dy, z + j));
                if (this.isEdiblePlant(bid)) {
                    int d = dy * dy + j * j + xi * xi;
                    if (d < this.closest) {
                        this.closest = d;
                        this.tx = x + xi;
                        this.ty = y + dy;
                        this.tz = z + j;
                        found++;
                    }
                }
                bid = this.level().getBlockState(new BlockPos(x + xi, y - dy, z + j));
                if (this.isEdiblePlant(bid)) {
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
                BlockState bid = this.level().getBlockState(new BlockPos(x + xi, y + j, z + dz));
                if (this.isEdiblePlant(bid)) {
                    int d = dz * dz + j * j + xi * xi;
                    if (d < this.closest) {
                        this.closest = d;
                        this.tx = x + xi;
                        this.ty = y + j;
                        this.tz = z + dz;
                        found++;
                    }
                }
                bid = this.level().getBlockState(new BlockPos(x + xi, y + j, z - dz));
                if (this.isEdiblePlant(bid)) {
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

    @Nullable
    private Gazelle findBuddy() {
        List<Gazelle> list =
                this.level().getEntitiesOfClass(Gazelle.class, this.getBoundingBox().inflate(16.0, 6.0, 16.0));
        list.sort(Comparator.comparingDouble(this::distanceToSqr));
        for (Gazelle g : list) {
            if (g != this) {
                return g;
            }
        }
        return null;
    }

    public int mygetMaxHealth() {
        return 15;
    }

    public boolean isOreSpawnTame() {
        return OreSpawnPet.isTame(this, PET_FLAGS);
    }

    public boolean isOreSpawnSitting() {
        return OreSpawnPet.isSitting(this, PET_FLAGS);
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!stack.is(Items.APPLE)) {
            ItemStack food = OreSpawnPet.findFoodInHands(player, s -> s.is(Items.APPLE));
            if (!food.isEmpty()) {
                stack = food;
            }
        }

        // gold: apple tame / heal, distSq < 16
        if (stack.is(Items.APPLE) && this.distanceToSqr(player) < 16.0) {
            if (!OreSpawnPet.isTame(this, PET_FLAGS)) {
                if (!this.level().isClientSide) {
                    if (OreSpawnPet.tryTame(this, PET_FLAGS, OWNER, player, 2)) {
                        this.heal(this.mygetMaxHealth() - this.getHealth());
                    }
                }
            } else if (OreSpawnPet.isOwnedBy(this, OWNER, player)) {
                if (this.level().isClientSide) {
                    this.level().broadcastEntityEvent(this, (byte) 7);
                }
                if (this.mygetMaxHealth() > this.getHealth()) {
                    this.heal(this.mygetMaxHealth() - this.getHealth());
                }
            }
            if (!player.getAbilities().instabuild) {
                stack.shrink(1);
            }
            return InteractionResult.SUCCESS;
        }

        // gold: dead bush untame when owned
        if (OreSpawnPet.isTame(this, PET_FLAGS)
                && !stack.isEmpty()
                && stack.is(Blocks.DEAD_BUSH.asItem())
                && this.distanceToSqr(player) < 16.0
                && OreSpawnPet.isOwnedBy(this, OWNER, player)) {
            if (!this.level().isClientSide) {
                OreSpawnPet.setTame(this, PET_FLAGS, false);
                OreSpawnPet.setOwnerUUID(this, OWNER, null);
                OreSpawnPet.setSitting(this, PET_FLAGS, false);
                this.level().broadcastEntityEvent(this, (byte) 6);
            }
            if (!player.getAbilities().instabuild) {
                stack.shrink(1);
            }
            return InteractionResult.SUCCESS;
        }

        // gold: name tag when tamed + owned
        if (OreSpawnPet.isTame(this, PET_FLAGS)
                && !stack.isEmpty()
                && stack.is(Items.NAME_TAG)
                && this.distanceToSqr(player) < 16.0
                && OreSpawnPet.isOwnedBy(this, OWNER, player)) {
            this.setCustomName(stack.getHoverName());
            if (!player.getAbilities().instabuild) {
                stack.shrink(1);
            }
            return InteractionResult.SUCCESS;
        }

        // gold: owner empty-hand toggles sit
        if (OreSpawnPet.isTame(this, PET_FLAGS)
                && OreSpawnPet.isOwnedBy(this, OWNER, player)
                && this.distanceToSqr(player) < 16.0) {
            if (!this.level().isClientSide) {
                OreSpawnPet.setSitting(this, PET_FLAGS, !OreSpawnPet.isSitting(this, PET_FLAGS));
            }
            return InteractionResult.SUCCESS;
        }

        return super.mobInteract(player, hand);
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

    @Override
    protected SoundEvent getAmbientSound() {
        //  gold: always null (even when not sitting)
        return null;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        // gold: orespawn:scorpion_hit
        return SoundsHandler.ENTITY_SCORPION_HIT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        // gold: orespawn:cryo_death
        return SoundsHandler.ENTITY_CRYO_DEATH.get();
    }

    @Override
    protected float getSoundVolume() {
        return 0.4F;
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
        // gold: tamed → poppies (2 + 0.4); untamed → beef (getDropItem)
        if (OreSpawnPet.isTame(this, PET_FLAGS)) {
            int count = 2 + this.random.nextInt(5);
            for (int i = 0; i < count; i++) {
                this.spawnAtLocation(new ItemStack(Blocks.POPPY));
            }
        } else {
            this.spawnAtLocation(new ItemStack(Items.BEEF));
        }
    }

    /** Gold: tamed damage capped at 10. */
    @Override
    public boolean hurt(DamageSource source, float amount) {
        float p2 = amount;
        if (OreSpawnPet.isTame(this, PET_FLAGS) && p2 > 10.0F) {
            p2 = 10.0F;
        }
        return super.hurt(source, p2);
    }

    /**
     * Gold {@code getCanSpawnHere}: y in [50, 100]; ground dirt/grass/tallgrass.
     */
    @Override
    public boolean checkSpawnRules(LevelAccessor level, MobSpawnType spawnType) {
        if (this.getY() < 50.0 || this.getY() > 100.0) {
            return false;
        }
        BlockState below = level.getBlockState(BlockPos.containing(this.getX(), this.getY() - 1.0, this.getZ()));
        return below.is(Blocks.DIRT) || below.is(Blocks.GRASS_BLOCK) || below.is(Blocks.SHORT_GRASS) || below.is(Blocks.TALL_GRASS);
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        // gold canDespawn always false
        return false;
    }

    @Override
    public boolean isFood(ItemStack stack) {
        // gold isWheat: apple; isBreedingItem: CrystalApple → apple stand-in
        return stack.is(Items.APPLE);
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob other) {
        return (AgeableMob) this.getType().create(level);
    }
}
