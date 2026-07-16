package danger.orespawn.entity;

import danger.orespawn.OreSpawnMain;
import danger.orespawn.entity.tame.OreSpawnPet;
import danger.orespawn.util.ai.FollowOwnerGoal;
import danger.orespawn.util.ai.WanderALotGoal;
import danger.orespawn.util.handlers.SoundsHandler;
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
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
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
 * Gold {@code GammaMetroid} (EntityTameable) ported as Monster + OreSpawnPet for NeoForge 1.21.1.
 * Size 1.5×1.5, speed 0.15, health 100, attack 10, armor 0, XP 20.
 * Tame food: iron_ingot (1/3), FollowOwner 2.0/10/2; when tamed, combat AI finds no targets.
 * Untamed: hunts non-Monster living (not creative players). Forages stone when not sitting (heal 1).
 * Gold has no sit toggle on empty hand and does not consume iron.
 */
public class GammaMetroid extends Monster {
    private static final EntityDataAccessor<Byte> PET_FLAGS =
            SynchedEntityData.defineId(GammaMetroid.class, EntityDataSerializers.BYTE);
    private static final EntityDataAccessor<Optional<UUID>> OWNER =
            SynchedEntityData.defineId(GammaMetroid.class, EntityDataSerializers.OPTIONAL_UUID);

    private final float moveSpeed = 0.15F;
    private int closest = 99999;
    private int tx;
    private int ty;
    private int tz;

    public GammaMetroid(EntityType<? extends GammaMetroid> type, Level level) {
        super(type, level);
        this.xpReward = 20;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 100.0) // mygetMaxHealth()
                .add(Attributes.MOVEMENT_SPEED, 0.15)
                .add(Attributes.ATTACK_DAMAGE, 10.0)
                .add(Attributes.ARMOR, 0.0)
                .add(Attributes.FOLLOW_RANGE, 16.0);
    }

    /** Gold was EntityTameable, not EntityMob — never wipe on Peaceful. */
    @Override
    protected boolean shouldDespawnInPeaceful() {
        return false;
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        // gold: MyEntityAIFollowOwner(this, 2.0F, 10.0F, 2.0F)
        this.goalSelector.addGoal(
                2,
                new FollowOwnerGoal(
                        this,
                        () -> OreSpawnPet.getOwnerUUID(this, OWNER),
                        () -> OreSpawnPet.isSitting(this, PET_FLAGS),
                        2.0,
                        10.0F,
                        2.0F));
        // gold: EntityAITempt(iron_ingot)
        this.goalSelector.addGoal(3, new TemptGoal(this, 1.2, Ingredient.of(Items.IRON_INGOT), false));
        this.goalSelector.addGoal(4, new WanderALotGoal(this, 16, 1.0));
        this.goalSelector.addGoal(5, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
        // gold: EntityAIHurtByTarget — still registered; custom combat ignores targets when tamed
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(PET_FLAGS, (byte) 0);
        builder.define(OWNER, Optional.empty());
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

    public boolean isTame() {
        return OreSpawnPet.isTame(this, PET_FLAGS);
    }

    public boolean isSitting() {
        return OreSpawnPet.isSitting(this, PET_FLAGS);
    }

    @Nullable
    public UUID getOwnerUUID() {
        return OreSpawnPet.getOwnerUUID(this, OWNER);
    }

    @Nullable
    public LivingEntity getOwner() {
        return OreSpawnPet.getOwner(this, OWNER);
    }

    /** Gold breed food: iron_ingot. */
    public boolean isFood(ItemStack stack) {
        return !stack.isEmpty() && stack.is(Items.IRON_INGOT);
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        // gold: if tamed → false; else !isNoDespawnRequired
        if (this.isTame()) {
            return false;
        }
        return !this.isPersistenceRequired();
    }

    /**
     * Gold {@code processInteract}: iron_ingot within distance 25, not tamed, server-side,
     * random 1/3 success ({@code nextInt(3) == 0}) → tame + heal full; else fail particles.
     * Gold does not consume the iron or toggle sit.
     */
    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!stack.is(Items.IRON_INGOT)) {
            ItemStack food = OreSpawnPet.findFoodInHands(player, s -> s.is(Items.IRON_INGOT));
            if (!food.isEmpty()) {
                stack = food;
            }
        }
        if (stack.is(Items.IRON_INGOT) && !this.isTame() && player.distanceTo(this) < 25.0F) {
            if (!this.level().isClientSide) {
                if (OreSpawnPet.tryTameWithFeedback(
                        this,
                        PET_FLAGS,
                        OWNER,
                        player,
                        3,
                        "WTF? is now yours!",
                        "WTF? ignores the iron… try again!")) {
                    this.setHealth(this.mygetMaxHealth());
                }
            }
            // gold does not consume iron
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
    public void tick() {
        if (this.getAttribute(Attributes.MOVEMENT_SPEED) != null) {
            this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(this.moveSpeed);
        }
        // sit/follow polish: stop pathing while sitting
        if (OreSpawnPet.isSitting(this, PET_FLAGS)) {
            this.getNavigation().stop();
        }
        super.tick();
        // gold forage runs in tick when not sitting (stone eat)
        if (!this.level().isClientSide && !this.isDeadOrDying() && !OreSpawnPet.isSitting(this, PET_FLAGS)) {
            this.tryForageStone();
        }
    }

    /** Gold livingUpdate stone forage: heal 1 when eating stone under mobGriefing; PlayNicely==0. */
    private void tryForageStone() {
        if (OreSpawnMain.PlayNicely != 0) {
            return;
        }
        if (!((this.random.nextInt(20) == 0 && this.getHealth() < this.mygetMaxHealth())
                || this.random.nextInt(100) == 0)) {
            return;
        }
        this.closest = 99999;
        this.tx = this.ty = this.tz = 0;
        for (int i = 1; i < 6; i++) {
            int j = i;
            if (j > 2) {
                j = 2;
            }
            if (this.scanIt((int) this.getX(), (int) this.getY() + 1, (int) this.getZ(), i, j, i)) {
                break;
            }
            if (i >= 4) {
                i++;
            }
        }
        if (this.closest < 99999) {
            this.getNavigation().moveTo(this.tx, this.ty, this.tz, 1.0);
            if (this.closest < 12) {
                BlockPos pos = new BlockPos(this.tx, this.ty, this.tz);
                if (this.level().getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING) && this.isEffectiveAi()) {
                    this.level().removeBlock(pos, false);
                    this.heal(1.0F);
                    this.playSound(
                            SoundEvents.GENERIC_EAT,
                            0.5F,
                            this.level().getRandom().nextFloat() * 0.2F + 1.5F);
                }
            }
        }
    }

    private boolean isEdibleStone(BlockState state) {
        // gold: Blocks.STONE only
        return state.is(Blocks.STONE);
    }

    private boolean scanIt(int x, int y, int z, int dx, int dy, int dz) {
        int found = 0;
        for (int i = -dy; i <= dy; i++) {
            for (int j = -dz; j <= dz; j++) {
                BlockState bid = this.level().getBlockState(new BlockPos(x + dx, y + i, z + j));
                if (this.isEdibleStone(bid)) {
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
                if (this.isEdibleStone(bid)) {
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
                if (this.isEdibleStone(bid)) {
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
                if (this.isEdibleStone(bid)) {
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
                if (this.isEdibleStone(bid)) {
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
                if (this.isEdibleStone(bid)) {
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

    public int mygetMaxHealth() {
        return 100;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return this.random.nextInt(5) == 1 ? SoundsHandler.ENTITY_GAMMAMETROID_LIVING.get() : null;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        // entity/gammametroid/hurt.ogg present (gold used ENTITY_DUCK_HURT stand-in)
        return SoundsHandler.ENTITY_GAMMAMETROID_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundsHandler.ENTITY_ALOSAURUS_DEATH.get();
    }

    @Override
    protected float getSoundVolume() {
        return 1.5F;
    }

    @Override
    public float getVoicePitch() {
        return 1.0F;
    }

    @Override
    protected void dropCustomDeathLoot(ServerLevel level, DamageSource damageSource, boolean recentlyHit) {
        super.dropCustomDeathLoot(level, damageSource, recentlyHit);
        // gold: 5+rand(10) gold_nugget, 6+rand(10) iron_ingot
        int n = 5 + this.random.nextInt(10);
        for (int i = 0; i < n; i++) {
            this.spawnAtLocation(new ItemStack(Items.GOLD_NUGGET));
        }
        n = 6 + this.random.nextInt(10);
        for (int i = 0; i < n; i++) {
            this.spawnAtLocation(new ItemStack(Items.IRON_INGOT));
        }
    }

    @Override
    public boolean doHurtTarget(Entity target) {
        // never attack owner
        if (OreSpawnPet.isOwnedBy(this, OWNER, target)) {
            return false;
        }
        // gold: fixed 10 damage
        return target.hurt(this.damageSources().mobAttack(this), 10.0F);
    }

    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
        if (this.isDeadOrDying()) {
            return;
        }
        if (this.level().getDifficulty() == net.minecraft.world.Difficulty.PEACEFUL) {
            return;
        }
        // gold: combat only when untamed (isSuitableTarget returns false when tamed)
        if (this.random.nextInt(5) != 0) {
            return;
        }
        LivingEntity e = this.findSomethingToAttack();
        if (e != null) {
            this.getLookControl().setLookAt(e, 10.0F, 10.0F);
            // gold: distance <= 9 (non-squared getDistance)
            if (this.distanceTo(e) <= 9.0F) {
                if (this.random.nextInt(4) == 0 || this.random.nextInt(5) == 1) {
                    this.doHurtTarget(e);
                }
            } else {
                this.getNavigation().moveTo(e, 1.25);
            }
        }
    }

    private boolean isSuitableTarget(LivingEntity target) {
        if (this.level().getDifficulty() == net.minecraft.world.Difficulty.PEACEFUL) {
            return false;
        }
        if (target == null || target == this || !target.isAlive()) {
            return false;
        }
        if (target instanceof GammaMetroid || target instanceof Monster) {
            return false;
        }
        // gold: if tamed → false for all combat targets (includes owner)
        if (this.isTame()) {
            return false;
        }
        if (OreSpawnPet.isOwnedBy(this, OWNER, target)) {
            return false;
        }
        if (!this.hasLineOfSight(target)) {
            return false;
        }
        if (target instanceof Player player) {
            return !player.isSpectator() && !player.getAbilities().instabuild;
        }
        return true;
    }

    @Nullable
    private LivingEntity findSomethingToAttack() {
        // gold: PlayNicely != 0 → null
        if (OreSpawnMain.PlayNicely != 0) {
            return null;
        }
        return danger.orespawn.util.ai.GoldStyleCombat.findTarget(this, 10.0, 3.0, this::isSuitableTarget);
    }

    /**
     * Gold {@code getCanSpawnHere}: y ≤ 50 only (no light gate in gold).
     */
    @Override
    public boolean checkSpawnRules(LevelAccessor level, MobSpawnType spawnType) {
        return !(this.getY() > 50.0);
    }
}
