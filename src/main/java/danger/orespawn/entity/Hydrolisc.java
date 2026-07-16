package danger.orespawn.entity;

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
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
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
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

/**
 * Gold {@code Hydrolisc} (EntityTameable) 1:1 for NeoForge 1.21.1.
 * Size 0.5×0.5, speed 0.25, health 100, attack 1, armor 10, XP 5.
 * Tame food: cod/fish (50%); release cobweb; sit toggle; name tag.
 * Seeks water when not sitting; heals owner from own HP when tamed.
 */
public class Hydrolisc extends Monster {
    private static final EntityDataAccessor<Byte> PET_FLAGS =
            SynchedEntityData.defineId(Hydrolisc.class, EntityDataSerializers.BYTE);
    private static final EntityDataAccessor<Optional<UUID>> OWNER =
            SynchedEntityData.defineId(Hydrolisc.class, EntityDataSerializers.OPTIONAL_UUID);

    private final float moveSpeed = 0.25F;
    private int closest = 99999;
    private int tx;
    private int ty;
    private int tz;

    public Hydrolisc(EntityType<? extends Hydrolisc> type, Level level) {
        super(type, level);
        this.xpReward = 5;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 100.0) // mygetMaxHealth()
                .add(Attributes.MOVEMENT_SPEED, 0.25)
                .add(Attributes.ATTACK_DAMAGE, 1.0)
                .add(Attributes.ARMOR, 10.0) // gold getTotalArmorValue 10
                .add(Attributes.FOLLOW_RANGE, 16.0); // gold field_70174_ab = 100 (tracking)
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
        // gold EntityAIMate skipped (still Monster skeleton)
        // gold: EntityAIAvoidEntity(EntityMob, 8, 1.0, 1.4)
        this.goalSelector.addGoal(2, new AvoidEntityGoal<>(this, Monster.class, 8.0F, 1.0, 1.4));
        // gold: MyEntityAIFollowOwner(this, 1.2F, 10.0F, 2.0F)
        this.goalSelector.addGoal(
                3,
                new FollowOwnerGoal(
                        this,
                        () -> OreSpawnPet.getOwnerUUID(this, OWNER),
                        () -> OreSpawnPet.isSitting(this, PET_FLAGS),
                        1.2,
                        10.0F,
                        2.0F));
        // gold: EntityAITempt(fish)
        this.goalSelector.addGoal(4, new TemptGoal(this, 1.25, Ingredient.of(Items.COD), false));
        this.goalSelector.addGoal(5, new PanicGoal(this, 1.5));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 6.0F));
        // gold: MyEntityAIWander 1.0
        this.goalSelector.addGoal(7, new WanderALotGoal(this, 10, 1.0));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        // gold EntityAIMoveIndoors skipped
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        // gold canDespawn: false
        return false;
    }

    @Override
    public void tick() {
        if (this.getAttribute(Attributes.MOVEMENT_SPEED) != null) {
            this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(this.moveSpeed);
        }
        if (OreSpawnPet.isSitting(this, PET_FLAGS)) {
            this.getNavigation().stop();
        }
        // gold canBreatheUnderwater=true — LivingEntity method is final in 1.21; restore air each tick
        this.setAirSupply(this.getMaxAirSupply());
        super.tick();
    }

    /** Gold {@code livingUpdate}: buoyant in water (motionY += 0.04). */
    @Override
    public void aiStep() {
        super.aiStep();
        if (this.isInWater()) {
            Vec3 m = this.getDeltaMovement();
            this.setDeltaMovement(m.x, m.y + 0.04, m.z);
        }
    }

    public int mygetMaxHealth() {
        return 100;
    }

    /** Gold {@code getHydroHealth} for model feather speed. */
    public int getHydroHealth() {
        return (int) this.getHealth();
    }

    public boolean isTame() {
        return OreSpawnPet.isTame(this, PET_FLAGS);
    }

    public boolean isSitting() {
        return OreSpawnPet.isSitting(this, PET_FLAGS);
    }

    /** Gold isWheat / breed food: fish; breed item CrystalApple deferred → golden apple stand-in. */
    public boolean isFood(ItemStack stack) {
        return !stack.isEmpty() && (stack.is(Items.COD) || stack.is(Items.GOLDEN_APPLE));
    }

    /**
     * Gold {@code updateAITick}: clear target 1/200; water seek when not sitting;
     * tamed owner heal (owner heal 1 / self hurt 1) when hydro health &gt; 20.
     */
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
        if (!OreSpawnPet.isSitting(this, PET_FLAGS)
                && ((this.random.nextInt(20) == 0 && this.getHydroHealth() < this.mygetMaxHealth())
                        || this.random.nextInt(100) == 0)) {
            this.closest = 99999;
            this.tx = this.ty = this.tz = 0;
            for (int i = 1; i < 11; i++) {
                int j = i;
                if (j > 4) {
                    j = 4;
                }
                if (this.scanWater((int) this.getX(), (int) this.getY() - 1, (int) this.getZ(), i, j, i)) {
                    break;
                }
                if (i >= 5) {
                    i++;
                }
            }
            if (this.closest < 99999) {
                this.getNavigation().moveTo(this.tx, this.ty - 1, this.tz, 1.0);
                if (this.isInWater()) {
                    this.heal(1.0F);
                    this.playSound(
                            SoundEvents.GENERIC_SPLASH,
                            1.0F,
                            this.level().getRandom().nextFloat() * 0.2F + 0.9F);
                }
            }
        }
        // gold: 1/10 when tamed, heal owner if owner hurt and hydro health > 20
        if (this.random.nextInt(10) == 0 && OreSpawnPet.isTame(this, PET_FLAGS)) {
            LivingEntity owner = OreSpawnPet.getOwner(this, OWNER);
            if (owner != null
                    && owner.getHealth() < owner.getMaxHealth()
                    && this.getHydroHealth() > 20) {
                owner.heal(1.0F);
                this.heal(-1.0F);
            }
        }
    }

    private boolean isWaterBlock(BlockState state) {
        return state.getFluidState().is(FluidTags.WATER);
    }

    /** Gold {@code scan_it} for water / flowing water. */
    private boolean scanWater(int x, int y, int z, int dx, int dy, int dz) {
        int found = 0;
        for (int i = -dy; i <= dy; i++) {
            for (int j = -dz; j <= dz; j++) {
                BlockState bid = this.level().getBlockState(new BlockPos(x + dx, y + i, z + j));
                if (this.isWaterBlock(bid)) {
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
                if (this.isWaterBlock(bid)) {
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
                if (this.isWaterBlock(bid)) {
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
                if (this.isWaterBlock(bid)) {
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
                if (this.isWaterBlock(bid)) {
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
                if (this.isWaterBlock(bid)) {
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

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!stack.is(Items.COD)) {
            ItemStack food = OreSpawnPet.findFoodInHands(player, s -> s.is(Items.COD));
            if (!food.isEmpty()) {
                stack = food;
            }
        }

        // gold distanceSq < 16
        if (player.distanceToSqr(this) > 16.0) {
            return InteractionResult.PASS;
        }

        // gold: fish tame / heal
        if (stack.is(Items.COD)) {
            if (!OreSpawnPet.isTame(this, PET_FLAGS)) {
                if (!this.level().isClientSide) {
                    if (OreSpawnPet.tryTameWithFeedback(
                            this,
                            PET_FLAGS,
                            OWNER,
                            player,
                            2,
                            "Hydrolisc is now yours!",
                            "Hydrolisc ignores the fish… try again!")) {
                        this.heal(this.getMaxHealth() - this.getHealth());
                    }
                }
            } else if (OreSpawnPet.isOwnedBy(this, OWNER, player)) {
                if (this.level().isClientSide) {
                    this.level().broadcastEntityEvent(this, (byte) 7);
                }
                if (this.getMaxHealth() > this.getHealth()) {
                    this.heal(this.getMaxHealth() - this.getHealth());
                }
            }
            if (!player.getAbilities().instabuild) {
                stack.shrink(1);
            }
            return InteractionResult.SUCCESS;
        }

        if (OreSpawnPet.isTame(this, PET_FLAGS) && OreSpawnPet.isOwnedBy(this, OWNER, player)) {
            // gold: cobweb (Blocks.web) releases pet
            if (stack.is(Items.COBWEB)) {
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
            // gold: name tag
            if (stack.is(Items.NAME_TAG)) {
                if (!this.level().isClientSide) {
                    this.setCustomName(stack.getHoverName());
                }
                if (!player.getAbilities().instabuild) {
                    stack.shrink(1);
                }
                return InteractionResult.SUCCESS;
            }
            // gold: empty hand / other → toggle sit (owner only)
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
        // gold orespawn:cryo_hurt
        return SoundsHandler.ENTITY_CRYO_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        // gold orespawn:cryo_death
        return SoundsHandler.ENTITY_CRYO_DEATH.get();
    }

    @Override
    protected float getSoundVolume() {
        return 0.4F;
    }

    @Override
    public float getVoicePitch() {
        // gold: baby 1.5-ish, adult 1.0 base ±0.1 — no age → adult pitch
        return (this.random.nextFloat() - this.random.nextFloat()) * 0.1F + 1.0F;
    }

    @Override
    protected void dropCustomDeathLoot(ServerLevel level, DamageSource damageSource, boolean recentlyHit) {
        super.dropCustomDeathLoot(level, damageSource, recentlyHit);
        // gold: only when tamed, 2+nextInt(5) fish
        if (OreSpawnPet.isTame(this, PET_FLAGS)) {
            int n = 2 + this.random.nextInt(5);
            for (int i = 0; i < n; i++) {
                this.spawnAtLocation(new ItemStack(Items.COD));
            }
        }
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        // gold: cap incoming at 10
        float p2 = amount;
        if (p2 > 10.0F) {
            p2 = 10.0F;
        }
        return super.hurt(source, p2);
    }

    /**
     * Gold {@code fall}: ceil(dist-3), big/small sound, damage capped at 2.
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
}
