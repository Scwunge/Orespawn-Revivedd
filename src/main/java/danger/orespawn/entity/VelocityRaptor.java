package danger.orespawn.entity;

import danger.orespawn.OreSpawnMain;
import danger.orespawn.entity.tame.OreSpawnPet;
import danger.orespawn.util.ai.FollowOwnerGoal;
import danger.orespawn.util.ai.WanderALotGoal;
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
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
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

/**
 * Gold {@code VelocityRaptor} (EntityTameable) ported as Monster skeleton for NeoForge 1.21.1.
 * Size 0.5×0.6, speed 0.55, health 10/20 (untamed/tamed), attack 2, armor 0, XP 5.
 * Tame food: apple (50% = gold nextInt(2)==0); FollowOwner 1.5 / 10 / 2.
 * Special: cobweb releases pet. Forages grass/flowers/cobweb when not sitting (heal 2).
 * No combat AI in gold (panic only). No buddy limits in gold.
 */
public class VelocityRaptor extends Monster {
    private static final EntityDataAccessor<Byte> PET_FLAGS =
            SynchedEntityData.defineId(VelocityRaptor.class, EntityDataSerializers.BYTE);
    private static final EntityDataAccessor<Optional<UUID>> OWNER =
            SynchedEntityData.defineId(VelocityRaptor.class, EntityDataSerializers.OPTIONAL_UUID);

    private final float moveSpeed = 0.55F;
    private int closest = 99999;
    private int tx;
    private int ty;
    private int tz;

    public VelocityRaptor(EntityType<? extends VelocityRaptor> type, Level level) {
        super(type, level);
        this.xpReward = 5;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 10.0) // mygetMaxHealth() untamed
                .add(Attributes.MOVEMENT_SPEED, 0.55)
                .add(Attributes.ATTACK_DAMAGE, 2.0)
                .add(Attributes.ARMOR, 0.0)
                .add(Attributes.FOLLOW_RANGE, 16.0);
    }

    /** Gold was EntityTameable, not EntityMob — never wipe on Peaceful. */
    @Override
    protected boolean shouldDespawnInPeaceful() {
        return false;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(PET_FLAGS, (byte) 0);
        builder.define(OWNER, Optional.empty());
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        // gold: MyEntityAIFollowOwner(this, 1.5F, 10.0F, 2.0F)
        this.goalSelector.addGoal(
                2,
                new FollowOwnerGoal(
                        this,
                        () -> OreSpawnPet.getOwnerUUID(this, OWNER),
                        () -> OreSpawnPet.isSitting(this, PET_FLAGS),
                        1.5,
                        10.0F,
                        2.0F));
        // gold: EntityAITempt(apple)
        this.goalSelector.addGoal(4, new TemptGoal(this, 1.25, Ingredient.of(Items.APPLE), false));
        this.goalSelector.addGoal(5, new PanicGoal(this, 1.6));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 6.0F));
        // gold: MyEntityAIWander speed 0.9
        this.goalSelector.addGoal(7, new WanderALotGoal(this, 10, 0.9));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        if (OreSpawnPet.isTame(this, PET_FLAGS)) {
            return false;
        }
        return !this.isPersistenceRequired();
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

    /** Gold {@code livingUpdate}: clear target + forage plants for heal. */
    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
        if (this.isDeadOrDying()) {
            return;
        }
        // gold: nextInt(200)==1 → clear attack target
        if (this.random.nextInt(200) == 1) {
            this.setTarget(null);
            this.setLastHurtByMob(null);
        }
        // gold: forage when not sitting; (1/20 when hurt OR 1/250) && PlayNicely==0
        if (!OreSpawnPet.isSitting(this, PET_FLAGS)
                && OreSpawnMain.PlayNicely == 0
                && ((this.random.nextInt(20) == 0 && this.getHealth() < this.mygetMaxHealth())
                        || this.random.nextInt(250) == 0)) {
            this.closest = 99999;
            this.tx = this.ty = this.tz = 0;
            for (int i = 1; i < 10; i++) {
                int j = i;
                if (j > 2) {
                    j = 2;
                }
                if (this.scanIt((int) this.getX(), (int) this.getY() + 1, (int) this.getZ(), i, j, i)) {
                    break;
                }
                if (i >= 5) {
                    i++;
                }
            }
            if (this.closest < 99999) {
                this.getNavigation().moveTo(this.tx, this.ty, this.tz, 1.0);
                if (this.closest < 12) {
                    if (this.level().getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING)) {
                        this.level().removeBlock(new BlockPos(this.tx, this.ty, this.tz), false);
                    }
                    this.heal(2.0F);
                    this.playSound(
                            SoundEvents.GENERIC_EAT,
                            0.5F,
                            this.level().getRandom().nextFloat() * 0.2F + 1.5F);
                }
            }
        }
    }

    /** Gold edible: tallgrass, yellow/red flower, web, double_plant. */
    private boolean isEdiblePlant(BlockState state) {
        return state.is(Blocks.SHORT_GRASS)
                || state.is(Blocks.TALL_GRASS)
                || state.is(Blocks.FERN)
                || state.is(Blocks.LARGE_FERN)
                || state.is(Blocks.DANDELION)
                || state.is(Blocks.POPPY)
                || state.is(BlockTags.SMALL_FLOWERS)
                || state.is(Blocks.COBWEB)
                || state.is(Blocks.DEAD_BUSH)
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

    /** Gold {@code mygetMaxHealth()} — 20 tamed / 10 untamed. */
    public int mygetMaxHealth() {
        return OreSpawnPet.isTame(this, PET_FLAGS) ? 20 : 10;
    }

    private void applyTamedMaxHealth() {
        if (this.getAttribute(Attributes.MAX_HEALTH) != null) {
            this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(this.mygetMaxHealth());
        }
    }

    public boolean isTame() {
        return OreSpawnPet.isTame(this, PET_FLAGS);
    }

    public boolean isSitting() {
        return OreSpawnPet.isSitting(this, PET_FLAGS);
    }

    /** Gold breed food: apple ({@code isWheat} / {@code func_70877_b}). */
    public boolean isFood(ItemStack stack) {
        return !stack.isEmpty() && stack.is(Items.APPLE);
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

        // gold: distanceSq < 16; relax for tiny hitbox
        if (player.distanceToSqr(this) > 36.0) {
            return InteractionResult.PASS;
        }

        // apple: try tame / heal
        if (stack.is(Items.APPLE)) {
            if (!OreSpawnPet.isTame(this, PET_FLAGS)) {
                if (!this.level().isClientSide) {
                    if (OreSpawnPet.tryTameWithFeedback(
                            this,
                            PET_FLAGS,
                            OWNER,
                            player,
                            2,
                            "Velocity Raptor is now yours!",
                            "Velocity Raptor darts away… try again!")) {
                        this.applyTamedMaxHealth();
                        this.heal(this.getMaxHealth() - this.getHealth());
                    }
                }
            } else {
                // tamed: heal to full (gold does not require owner for apple heal)
                if (!this.level().isClientSide && this.getHealth() < this.getMaxHealth()) {
                    this.heal(this.getMaxHealth() - this.getHealth());
                    this.level().broadcastEntityEvent(this, (byte) 7);
                }
            }
            if (!player.getAbilities().instabuild) {
                stack.shrink(1);
            }
            return InteractionResult.SUCCESS;
        }

        if (OreSpawnPet.isTame(this, PET_FLAGS)) {
            // gold: cobweb releases pet
            if (stack.is(Items.COBWEB)) {
                if (!this.level().isClientSide) {
                    OreSpawnPet.setTame(this, PET_FLAGS, false);
                    OreSpawnPet.setOwnerUUID(this, OWNER, null);
                    OreSpawnPet.setSitting(this, PET_FLAGS, false);
                    this.applyTamedMaxHealth();
                    this.setHealth(this.getMaxHealth());
                    this.level().broadcastEntityEvent(this, (byte) 6);
                }
                if (!player.getAbilities().instabuild) {
                    stack.shrink(1);
                }
                return InteractionResult.SUCCESS;
            }

            // gold: name tag sets custom name
            if (stack.is(Items.NAME_TAG)) {
                if (!this.level().isClientSide) {
                    this.setCustomName(stack.getHoverName());
                }
                if (!player.getAbilities().instabuild) {
                    stack.shrink(1);
                }
                return InteractionResult.SUCCESS;
            }

            // gold: otherwise toggle sit when tamed (no owner check in gold)
            if (!this.level().isClientSide) {
                OreSpawnPet.setSitting(this, PET_FLAGS, !OreSpawnPet.isSitting(this, PET_FLAGS));
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

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        OreSpawnPet.addAdditionalSaveData(this, PET_FLAGS, OWNER, tag);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        OreSpawnPet.readAdditionalSaveData(this, PET_FLAGS, OWNER, tag);
        this.applyTamedMaxHealth();
        if (OreSpawnPet.isTame(this, PET_FLAGS)) {
            this.setPersistenceRequired();
        }
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return null;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return danger.orespawn.util.handlers.SoundsHandler.ENTITY_CRYO_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return danger.orespawn.util.handlers.SoundsHandler.ENTITY_CRYO_DEATH.get();
    }

    @Override
    protected float getSoundVolume() {
        return 0.4F;
    }

    @Override
    public float getVoicePitch() {
        return 1.0F + (this.random.nextFloat() - this.random.nextFloat()) * 0.1F;
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        // gold: cap incoming damage at 10
        float capped = Math.min(amount, 10.0F);
        boolean ret = super.hurt(source, capped);
        if (ret && OreSpawnPet.isTame(this, PET_FLAGS)) {
            OreSpawnPet.setSitting(this, PET_FLAGS, false);
        }
        return ret;
    }

    /**
     * Gold {@code fall}: ceil(dist-3), cap damage at 2 (soft landing).
     */
    @Override
    public boolean causeFallDamage(float fallDistance, float multiplier, DamageSource source) {
        float i = Mth.ceil(fallDistance - 3.0F);
        if (i > 0.0F) {
            if (i > 3.0F) {
                this.playSound(SoundEvents.GENERIC_BIG_FALL, 1.0F, 1.0F);
            } else {
                this.playSound(SoundEvents.GENERIC_SMALL_FALL, 1.0F, 1.0F);
            }
            if (i > 2.0F) {
                i = 2.0F;
            }
            this.hurt(source, i);
            return true;
        }
        return false;
    }

    /**
     * Gold {@code getCanSpawnHere}: y &lt; 50 → false; else world.isDaytime().
     */
    @Override
    public boolean checkSpawnRules(LevelAccessor level, MobSpawnType spawnType) {
        if (this.getY() < 50.0) {
            return false;
        }
        if (level instanceof Level lvl) {
            return lvl.isDay();
        }
        return true;
    }

    @Override
    protected void dropCustomDeathLoot(ServerLevel level, DamageSource damageSource, boolean recentlyHit) {
        super.dropCustomDeathLoot(level, damageSource, recentlyHit);
        // gold: only drops when tamed — 2 + rand(5) poppy
        if (!OreSpawnPet.isTame(this, PET_FLAGS)) {
            return;
        }
        int count = 2 + this.random.nextInt(5);
        for (int i = 0; i < count; i++) {
            this.spawnAtLocation(new ItemStack(Blocks.POPPY));
        }
    }
}
