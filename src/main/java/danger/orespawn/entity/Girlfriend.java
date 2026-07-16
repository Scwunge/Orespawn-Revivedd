package danger.orespawn.entity;

import danger.orespawn.OreSpawnMain;
import danger.orespawn.entity.tame.OreSpawnPet;
import danger.orespawn.init.ModItems;
import danger.orespawn.util.ai.FollowOwnerGoal;
import danger.orespawn.util.ai.WanderALotGoal;
import java.util.Optional;
import java.util.UUID;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
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
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.OpenDoorGoal;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RangedAttackGoal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.entity.projectile.Snowball;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

/**
 * Gold {@code Girlfriend} (EntityTameable + IRangedAttackMob) 1:1 best-effort for NeoForge 1.21.1.
 * <p>
 * Size 0.5×1.6 (valentines angry 2.5×8.0), speed 0.3, health 80 / 800, attack 8, XP 0.
 * Tame: red flower (POPPY) 1/3; skin cycle yellow flower; equip armor/weapons; heal food;
 * sit via diamond equip; voice ruby/amethyst; ranged arrows + melee with held item.
 * Skins: girlfriend0–40, bikini0–17, frogprincess / frogprincess2, girlfriendv.
 * Registry size/attrs set in {@code ModEntities}.
 */
public class Girlfriend extends Monster implements RangedAttackMob {
    private static final EntityDataAccessor<Byte> PET_FLAGS =
            SynchedEntityData.defineId(Girlfriend.class, EntityDataSerializers.BYTE);
    private static final EntityDataAccessor<Optional<UUID>> OWNER =
            SynchedEntityData.defineId(Girlfriend.class, EntityDataSerializers.OPTIONAL_UUID);
    private static final EntityDataAccessor<Integer> DATA_GIRL =
            SynchedEntityData.defineId(Girlfriend.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> DATA_WET_GIRL =
            SynchedEntityData.defineId(Girlfriend.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> DATA_VOICE =
            SynchedEntityData.defineId(Girlfriend.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> DATA_VOICE_ENABLE =
            SynchedEntityData.defineId(Girlfriend.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> DATA_PRINCESS =
            SynchedEntityData.defineId(Girlfriend.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> DATA_FEELING_BETTER =
            SynchedEntityData.defineId(Girlfriend.class, EntityDataSerializers.INT);

    public static final float GOLD_WIDTH = 0.5F;
    public static final float GOLD_HEIGHT = 1.6F;
    public static final float GOLD_VALENTINE_WIDTH = 2.5F;
    public static final float GOLD_VALENTINE_HEIGHT = 8.0F;
    public static final double GOLD_HEALTH = 80.0;
    public static final double GOLD_HEALTH_VALENTINE = 800.0;
    public static final double GOLD_SPEED = 0.3;
    public static final double GOLD_ATTACK = 8.0;
    public static final int GOLD_XP = 0;

    private static final ResourceLocation[] DRY_TEXTURES = new ResourceLocation[41];
    private static final ResourceLocation[] WET_TEXTURES = new ResourceLocation[18];
    private static final ResourceLocation VALENTINE_TEXTURE =
            ResourceLocation.fromNamespaceAndPath("orespawn", "textures/entity/girlfriendv.png");
    private static final ResourceLocation PRINCESS_TEXTURE_1 =
            ResourceLocation.fromNamespaceAndPath("orespawn", "textures/entity/frogprincess.png");
    private static final ResourceLocation PRINCESS_TEXTURE_2 =
            ResourceLocation.fromNamespaceAndPath("orespawn", "textures/entity/frogprincess2.png");

    static {
        for (int i = 0; i <= 40; i++) {
            DRY_TEXTURES[i] = ResourceLocation.fromNamespaceAndPath(
                    "orespawn", "textures/entity/girlfriend" + i + ".png");
        }
        for (int i = 0; i <= 17; i++) {
            WET_TEXTURES[i] = ResourceLocation.fromNamespaceAndPath(
                    "orespawn", "textures/entity/bikini" + i + ".png");
        }
    }

    /**
     * Gold {@code OreSpawnMain.valentines_day}. Field not yet on OreSpawnMain — default 0.
     * May move onto OreSpawnMain later; entity reads via {@link #getValentinesDay()}.
     */
    public static int VALENTINES_DAY = 0;

    private final float moveSpeed = 0.3F;
    public int which_girl = 0;
    public int which_wet_girl = 0;
    public int wet_count = 0;
    public int feelingBetter = 0;
    public int passenger = 0;
    private int voice = 0;
    private int voice_enable = 1;
    private int is_princess = 0;
    private int auto_heal = 200;
    private int force_sync = 50;
    private int fight_sound_ticker = 0;
    private int taunt_sound_ticker = 0;
    private int had_target = 0;
    private int attackCooldown = 0;

    public Girlfriend(EntityType<? extends Girlfriend> type, Level level) {
        super(type, level);
        this.xpReward = GOLD_XP;
        this.which_girl = this.random.nextInt(41);
        this.which_wet_girl = this.random.nextInt(18);
        this.voice = this.random.nextInt(10);
        // defineSynchedData ran during super with 0 defaults — push roll results
        this.entityData.set(DATA_GIRL, this.which_girl);
        this.entityData.set(DATA_WET_GIRL, this.which_wet_girl);
        this.entityData.set(DATA_VOICE, this.voice);
        this.entityData.set(DATA_VOICE_ENABLE, this.voice_enable);
        this.entityData.set(DATA_PRINCESS, this.is_princess);
        this.entityData.set(DATA_FEELING_BETTER, this.feelingBetter);
        if (this.getNavigation() instanceof GroundPathNavigation ground) {
            ground.setCanOpenDoors(true);
            ground.setCanPassDoors(true);
        }
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, GOLD_HEALTH)
                .add(Attributes.MOVEMENT_SPEED, GOLD_SPEED)
                .add(Attributes.ATTACK_DAMAGE, GOLD_ATTACK)
                .add(Attributes.ARMOR, 8.0)
                .add(Attributes.FOLLOW_RANGE, 100.0) // gold field_70174_ab tracking-ish
                .add(Attributes.STEP_HEIGHT, 1.0);
    }

    /** Gold was EntityTameable — never wipe on Peaceful when tamed; always false like other pets. */
    @Override
    protected boolean shouldDespawnInPeaceful() {
        return false;
    }

    @Override
    public boolean fireImmune() {
        // gold field_70178_ae = true
        return true;
    }

    private static int getValentinesDay() {
        return VALENTINES_DAY;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(PET_FLAGS, (byte) 0);
        builder.define(OWNER, Optional.empty());
        builder.define(DATA_GIRL, this.which_girl);
        builder.define(DATA_WET_GIRL, this.which_wet_girl);
        builder.define(DATA_VOICE, this.voice);
        builder.define(DATA_VOICE_ENABLE, this.voice_enable);
        builder.define(DATA_PRINCESS, this.is_princess);
        builder.define(DATA_FEELING_BETTER, this.feelingBetter);
    }

    @Override
    protected void registerGoals() {
        // gold: FollowOwner 1.4 / 12 / 1.5, Tempt red_flower, Dance (stub), ArrowAttack,
        // Swimming, Panic, Watch Player, Wander, LookIdle, OpenDoor, MoveIndoors (skip POI)
        this.goalSelector.addGoal(
                1,
                new FollowOwnerGoal(
                        this,
                        () -> OreSpawnPet.getOwnerUUID(this, OWNER),
                        () -> OreSpawnPet.isSitting(this, PET_FLAGS),
                        1.4,
                        12.0F,
                        1.5F));
        this.goalSelector.addGoal(2, new TemptGoal(this, 1.25, Ingredient.of(Blocks.POPPY), false));
        // gold MyEntityAIDance — util/ai dance goal not present; deferred
        // gold EntityAIArrowAttack — RangedAttackGoal (not RangedBowAttackGoal: no bow required)
        this.goalSelector.addGoal(4, new RangedAttackGoal(this, 1.25, 20, 10.0F));
        this.goalSelector.addGoal(5, new FloatGoal(this));
        this.goalSelector.addGoal(6, new PanicGoal(this, 1.5));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(8, new WanderALotGoal(this, 10, 0.75));
        this.goalSelector.addGoal(9, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(10, new OpenDoorGoal(this, true));
        // gold EntityAIMoveIndoors — no direct 1.21 equivalent; skipped

        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        // gold MyValentineTarget Player/Boyfriend when valentines — target players; Boyfriend class ok to reference
        if (getValentinesDay() != 0) {
            this.targetSelector.addGoal(
                    1,
                    new NearestAttackableTargetGoal<>(
                            this,
                            Player.class,
                            10,
                            true,
                            true,
                            living -> this.getFeelingBetter() == 0));
            this.targetSelector.addGoal(
                    1,
                    new NearestAttackableTargetGoal<>(
                            this,
                            Boyfriend.class,
                            10,
                            true,
                            true,
                            living -> this.getFeelingBetter() == 0));
        }
        if (OreSpawnMain.PlayNicely == 0) {
            // match Boyfriend target predicate style (Predicate<LivingEntity>)
            this.targetSelector.addGoal(
                    2, new NearestAttackableTargetGoal<>(this, Creeper.class, true));
            this.targetSelector.addGoal(
                    3,
                    new NearestAttackableTargetGoal<>(
                            this,
                            Mob.class,
                            15,
                            true,
                            true,
                            living -> living instanceof Enemy
                                    && !(living instanceof Girlfriend)
                                    && !(living instanceof Boyfriend)));
            // gold MyEntityAIJealousy vs other Girlfriends (best-effort nearest target)
            this.targetSelector.addGoal(
                    4, new NearestAttackableTargetGoal<>(this, Girlfriend.class, true));
        }
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> key) {
        if (DATA_FEELING_BETTER.equals(key)) {
            this.feelingBetter = this.entityData.get(DATA_FEELING_BETTER);
            this.refreshDimensions();
            this.applyMaxHealthForMode();
        }
        super.onSyncedDataUpdated(key);
    }

    @Override
    public EntityDimensions getDefaultDimensions(Pose pose) {
        if (getValentinesDay() != 0 && this.getFeelingBetter() == 0) {
            return EntityDimensions.scalable(GOLD_VALENTINE_WIDTH, GOLD_VALENTINE_HEIGHT);
        }
        return EntityDimensions.scalable(GOLD_WIDTH, GOLD_HEIGHT);
    }

    private void applyMaxHealthForMode() {
        if (this.getAttribute(Attributes.MAX_HEALTH) != null) {
            double max = this.mygetMaxHealth();
            this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(max);
            if (this.getHealth() > max) {
                this.setHealth((float) max);
            }
        }
    }

    public int mygetMaxHealth() {
        return getValentinesDay() != 0 && this.getFeelingBetter() == 0
                ? (int) GOLD_HEALTH_VALENTINE
                : (int) GOLD_HEALTH;
    }

    public int getGirlfriendHealth() {
        return (int) this.getHealth();
    }

    public int getTameSkin() {
        return this.entityData.get(DATA_GIRL);
    }

    public void setTameSkin(int skin) {
        this.which_girl = skin;
        if (!this.level().isClientSide) {
            this.entityData.set(DATA_GIRL, skin);
        }
    }

    public int getWetTameSkin() {
        return this.entityData.get(DATA_WET_GIRL);
    }

    public void setWetTameSkin(int skin) {
        this.which_wet_girl = skin;
        if (!this.level().isClientSide) {
            this.entityData.set(DATA_WET_GIRL, skin);
        }
    }

    public int getVoice() {
        return this.entityData.get(DATA_VOICE);
    }

    public int getVoiceEnable() {
        return this.entityData.get(DATA_VOICE_ENABLE);
    }

    public int getPrincess() {
        return this.entityData.get(DATA_PRINCESS);
    }

    public void setPrincess(int par1) {
        this.is_princess = par1;
        if (!this.level().isClientSide) {
            this.entityData.set(DATA_PRINCESS, par1);
        }
    }

    public int getFeelingBetter() {
        return this.entityData.get(DATA_FEELING_BETTER);
    }

    public void setFeelingBetter(int value) {
        this.feelingBetter = value;
        if (!this.level().isClientSide) {
            this.entityData.set(DATA_FEELING_BETTER, value, true);
        }
        this.refreshDimensions();
        this.applyMaxHealthForMode();
    }

    public boolean isOreSpawnTame() {
        return OreSpawnPet.isTame(this, PET_FLAGS);
    }

    public boolean isOreSpawnSitting() {
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

    public boolean isOwnedBy(Entity other) {
        return OreSpawnPet.isOwnedBy(this, OWNER, other);
    }

    public ItemStack getCurrentEquippedItem() {
        return this.getMainHandItem();
    }

    /** Gold texture picker — used by GirlfriendRenderer. */
    public ResourceLocation getTexture() {
        if (getValentinesDay() != 0 && this.getFeelingBetter() == 0) {
            return VALENTINE_TEXTURE;
        }
        if (this.wet_count <= 0) {
            int princess = this.getPrincess();
            if (princess == 1) {
                return PRINCESS_TEXTURE_1;
            }
            if (princess == 2) {
                return PRINCESS_TEXTURE_2;
            }
            int t = this.getTameSkin();
            if (t < 0 || t > 40) {
                t = 0;
            }
            return DRY_TEXTURES[t];
        }
        int w = this.getWetTameSkin();
        if (w < 0 || w > 17) {
            w = 0;
        }
        return WET_TEXTURES[w];
    }

    @Override
    public int getArmorValue() {
        int i = super.getArmorValue();
        // gold: floor 8, cap 23 from worn armor
        if (i < 8) {
            i = 8;
        }
        if (i > 23) {
            i = 23;
        }
        return i;
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
        this.passenger = 0;
        // gold Elevator passenger pose — Elevator entity deferred
    }

    @Override
    public void aiStep() {
        this.updateSwingTime();
        super.aiStep();

        if (this.isInWaterRainOrBubble() || this.isInWater()) {
            this.wet_count = 500;
        } else if (this.wet_count > 0) {
            this.wet_count--;
        }

        this.auto_heal--;
        if (this.auto_heal <= 0) {
            if (this.mygetMaxHealth() > this.getGirlfriendHealth()) {
                this.heal(1.0F);
            }
            this.auto_heal = 100;
        }

        this.force_sync--;
        if (this.force_sync <= 0) {
            this.force_sync = 20;
            if (!this.level().isClientSide) {
                this.entityData.set(DATA_VOICE, this.voice);
                this.entityData.set(DATA_VOICE_ENABLE, this.voice_enable);
                this.entityData.set(DATA_PRINCESS, this.is_princess);
                this.entityData.set(DATA_FEELING_BETTER, this.feelingBetter);
            } else {
                this.voice = this.getVoice();
                this.voice_enable = this.getVoiceEnable();
                int nowFeeling = this.getFeelingBetter();
                if (nowFeeling != this.feelingBetter && nowFeeling != 0) {
                    this.feelingBetter = nowFeeling;
                    this.refreshDimensions();
                }
            }
        }
    }

    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
        if (this.isDeadOrDying()) {
            return;
        }

        ItemStack stack = this.getCurrentEquippedItem();
        Entity victim = this.getTarget();
        if (OreSpawnMain.PlayNicely != 0) {
            victim = null;
        }

        if (this.random.nextInt(100) == 1) {
            this.setLastHurtByMob(null);
        }
        if (this.random.nextInt(200) == 1) {
            this.setTarget(null);
        }

        if (this.attackCooldown > 0) {
            this.attackCooldown--;
        }

        if (!stack.isEmpty() && !OreSpawnPet.isSitting(this, PET_FLAGS) && victim instanceof LivingEntity living) {
            float dist = this.distanceTo(living);
            boolean berthaReach = stack.is(ModItems.BERTHA.get()) && dist < 10.0F;
            if (dist < 4.0F || berthaReach) {
                if (this.attackCooldown <= 0) {
                    this.attackCooldown = 25;
                    this.swing(InteractionHand.MAIN_HAND);
                    this.attackTargetEntityWithCurrentItem(living);
                    this.fight_sound_ticker--;
                    if (this.fight_sound_ticker <= 0) {
                        if (this.voice_enable != 0) {
                            // gold orespawn:o_fight — sound holder deferred
                            this.playSound(SoundEvents.PLAYER_ATTACK_STRONG, 0.5F, this.getVoicePitch());
                        }
                        this.fight_sound_ticker = 3;
                    }
                    this.had_target = 1;
                }
            } else if (dist < 7.0F) {
                this.taunt_sound_ticker--;
                if (this.taunt_sound_ticker <= 0) {
                    if (this.voice_enable != 0) {
                        // gold orespawn:o_taunt
                        this.playSound(SoundEvents.VILLAGER_NO, 0.5F, this.getVoicePitch());
                    }
                    this.taunt_sound_ticker = 300;
                }
                this.getNavigation().moveTo(living, 1.25);
            }
        } else {
            this.fight_sound_ticker = 0;
            this.attackCooldown = 0;
            if (this.had_target != 0) {
                this.had_target = 0;
                if (this.voice_enable != 0) {
                    // gold orespawn:o_woohoo
                    this.playSound(SoundEvents.VILLAGER_YES, 0.4F, this.getVoicePitch());
                }
            }
        }
    }

    /**
     * Gold {@code attackTargetEntityWithCurrentItem} — melee with held item + attack attr.
     */
    public void attackTargetEntityWithCurrentItem(Entity target) {
        ItemStack stack = this.getCurrentEquippedItem();
        if (stack.isEmpty()) {
            return;
        }
        float strength =
                this.getAttribute(Attributes.ATTACK_DAMAGE) != null
                        ? (float) this.getAttributeValue(Attributes.ATTACK_DAMAGE)
                        : (float) GOLD_ATTACK;
        boolean crit = this.fallDistance > 0.0F
                && !this.onGround()
                && !this.onClimbable()
                && !this.isInWater()
                && this.getVehicle() == null
                && target instanceof LivingEntity;
        if (crit) {
            strength += this.random.nextInt((int) (strength / 2.0F) + 2);
        }
        boolean hit = target.hurt(this.damageSources().mobAttack(this), strength);
        if (hit && this.isSprinting()) {
            target.push(
                    -Mth.sin(this.getYRot() * ((float) Math.PI / 180.0F)) * 0.5F,
                    0.1,
                    Mth.cos(this.getYRot() * ((float) Math.PI / 180.0F)) * 0.5F);
            this.setDeltaMovement(this.getDeltaMovement().multiply(0.6, 1.0, 0.6));
            this.setSprinting(false);
        }
    }

    @Override
    public void performRangedAttack(LivingEntity target, float distanceFactor) {
        // gold attackEntityWithRangedAttack — UltimateArrow / Shoes; stand-ins like Boyfriend
        if (this.swinging || OreSpawnPet.isSitting(this, PET_FLAGS)) {
            return;
        }
        ItemStack it = this.getMainHandItem();
        // gold UltimateBow → Arrow; else Shoes → Snowball stand-in
        if (!it.isEmpty() && it.is(Items.BOW) && this.level() instanceof ServerLevel server) {
            Arrow arrow = new Arrow(this.level(), this, new ItemStack(Items.ARROW), it);
            double dx = target.getX() - this.getX();
            double dy = target.getY(0.33333334F) - arrow.getY();
            double dz = target.getZ() - this.getZ();
            double horiz = Math.sqrt(dx * dx + dz * dz);
            arrow.shoot(dx, dy + horiz * 0.20000000298023224, dz, 2.0F, 10.0F);
            if (this.random.nextInt(4) == 1) {
                arrow.setCritArrow(true);
            }
            arrow.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
            this.playSound(
                    SoundEvents.ARROW_SHOOT,
                    1.0F,
                    1.0F / (this.random.nextFloat() * 0.4F + 1.2F) + 0.5F);
            server.addFreshEntity(arrow);
        } else {
            Snowball shoe = new Snowball(this.level(), this);
            double dx = target.getX() - this.getX();
            double dy = target.getY(0.33333334F) - shoe.getY();
            double dz = target.getZ() - this.getZ();
            double horiz = Math.sqrt(dx * dx + dz * dz) * 0.2F;
            shoe.shoot(dx, dy + horiz, dz, 1.8F, 4.0F);
            this.playSound(
                    SoundEvents.ARROW_SHOOT,
                    0.75F,
                    1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
            this.level().addFreshEntity(shoe);
        }
        this.swing(InteractionHand.MAIN_HAND);
    }

    private boolean isTameFlower(ItemStack stack) {
        return !stack.isEmpty() && stack.is(Blocks.POPPY.asItem());
    }

    private boolean isSkinFlower(ItemStack stack) {
        return !stack.isEmpty() && stack.is(Blocks.DANDELION.asItem());
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        double distSq = this.distanceToSqr(player);
        if (distSq > 16.0) {
            return InteractionResult.PASS;
        }

        // gold: red_flower / CrystalFlowerRed tame or heal
        if (isTameFlower(stack)) {
            if (!OreSpawnPet.isTame(this, PET_FLAGS)) {
                if (!this.level().isClientSide) {
                    if (OreSpawnPet.tryTame(this, PET_FLAGS, OWNER, player, 3)) {
                        this.heal(this.mygetMaxHealth() - this.getHealth());
                        player.displayClientMessage(Component.literal("Girlfriend is now yours!"), true);
                    } else {
                        player.displayClientMessage(Component.literal("Girlfriend is not ready… try again!"), true);
                    }
                }
            } else if (OreSpawnPet.isOwnedBy(this, OWNER, player)) {
                if (!this.level().isClientSide) {
                    this.level().broadcastEntityEvent(this, (byte) 7);
                    if (this.getHealth() < this.mygetMaxHealth()) {
                        this.heal(this.mygetMaxHealth() - this.getHealth());
                    }
                }
            }
            if (!player.getAbilities().instabuild) {
                stack.shrink(1);
            }
            return InteractionResult.SUCCESS;
        }

        // gold: dead_bush untame (owner)
        if (OreSpawnPet.isTame(this, PET_FLAGS)
                && !stack.isEmpty()
                && stack.is(Blocks.DEAD_BUSH.asItem())
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

        // gold: MyRuby disables voice
        if (OreSpawnPet.isTame(this, PET_FLAGS)
                && !stack.isEmpty()
                && stack.is(ModItems.RUBY.get())
                && OreSpawnPet.isOwnedBy(this, OWNER, player)) {
            if (!this.level().isClientSide) {
                this.voice_enable = 0;
                this.entityData.set(DATA_VOICE_ENABLE, 0);
                this.level().broadcastEntityEvent(this, (byte) 7);
            }
            if (!player.getAbilities().instabuild) {
                stack.shrink(1);
            }
            return InteractionResult.SUCCESS;
        }

        // gold: MyAmethyst enables voice
        if (OreSpawnPet.isTame(this, PET_FLAGS)
                && !stack.isEmpty()
                && stack.is(ModItems.AMETHYST.get())
                && OreSpawnPet.isOwnedBy(this, OWNER, player)) {
            if (!this.level().isClientSide) {
                this.voice_enable = 1;
                this.entityData.set(DATA_VOICE_ENABLE, 1);
                this.level().broadcastEntityEvent(this, (byte) 7);
            }
            if (!player.getAbilities().instabuild) {
                stack.shrink(1);
            }
            return InteractionResult.SUCCESS;
        }

        // gold: yellow flower / CrystalFlowerYellow skin cycle
        if (OreSpawnPet.isTame(this, PET_FLAGS)
                && isSkinFlower(stack)
                && OreSpawnPet.isOwnedBy(this, OWNER, player)) {
            if (!this.level().isClientSide) {
                if (this.wet_count <= 0 && !this.isInWaterOrBubble()) {
                    this.which_girl++;
                    if (this.which_girl > 40) {
                        this.which_girl = 0;
                    }
                    this.setTameSkin(this.which_girl);
                    this.level().broadcastEntityEvent(this, (byte) 7);
                } else {
                    this.which_wet_girl++;
                    if (this.which_wet_girl > 17) {
                        this.which_wet_girl = 0;
                    }
                    this.setWetTameSkin(this.which_wet_girl);
                    this.level().broadcastEntityEvent(this, (byte) 7);
                    if (this.isInWaterOrBubble()) {
                        this.wet_count = 500;
                    }
                }
            }
            if (!player.getAbilities().instabuild) {
                stack.shrink(1);
            }
            return InteractionResult.SUCCESS;
        }

        // gold: diamond_block re-claim ownership
        if (OreSpawnPet.isTame(this, PET_FLAGS)
                && !stack.isEmpty()
                && stack.is(Blocks.DIAMOND_BLOCK.asItem())) {
            if (!this.level().isClientSide) {
                OreSpawnPet.tame(this, PET_FLAGS, OWNER, player);
                OreSpawnPet.setSitting(this, PET_FLAGS, false);
                this.level().broadcastEntityEvent(this, (byte) 7);
            }
            if (!player.getAbilities().instabuild) {
                stack.shrink(1);
            }
            return InteractionResult.SUCCESS;
        }

        // gold: name tag
        if (OreSpawnPet.isTame(this, PET_FLAGS)
                && !stack.isEmpty()
                && stack.is(Items.NAME_TAG)
                && OreSpawnPet.isOwnedBy(this, OWNER, player)) {
            this.setCustomName(stack.getHoverName());
            if (!player.getAbilities().instabuild) {
                stack.shrink(1);
            }
            return InteractionResult.SUCCESS;
        }

        // gold: tamed + owned + holding item → food heal or equip
        if (OreSpawnPet.isTame(this, PET_FLAGS)
                && !stack.isEmpty()
                && OreSpawnPet.isOwnedBy(this, OWNER, player)) {
            var food = stack.getFoodProperties(this);
            if (food != null) {
                if (!this.level().isClientSide) {
                    if (this.getHealth() < this.mygetMaxHealth()) {
                        this.heal(food.nutrition() * 5.0F);
                    }
                    this.level().broadcastEntityEvent(this, (byte) 7);
                }
                if (!player.getAbilities().instabuild) {
                    stack.shrink(1);
                }
                return InteractionResult.SUCCESS;
            }

            // equip / swap main hand or armor
            if (!this.level().isClientSide) {
                this.level().broadcastEntityEvent(this, (byte) 7);
            }
            ItemStack held = this.getMainHandItem();
            ItemStack toGive = stack.copyWithCount(1);
            EquipmentSlot slot = this.getEquipmentSlotForItem(toGive);
            if (slot != EquipmentSlot.MAINHAND && slot != EquipmentSlot.OFFHAND) {
                ItemStack previous = this.getItemBySlot(slot);
                this.setItemSlot(slot, toGive);
                if (!player.getAbilities().instabuild) {
                    stack.shrink(1);
                }
                if (!previous.isEmpty()) {
                    if (stack.isEmpty()) {
                        player.setItemInHand(hand, previous);
                    } else if (!player.getInventory().add(previous)) {
                        player.drop(previous, false);
                    }
                }
            } else {
                this.setItemSlot(EquipmentSlot.MAINHAND, toGive);
                // gold: diamond in hand → sit
                if (toGive.is(Items.DIAMOND)) {
                    OreSpawnPet.setSitting(this, PET_FLAGS, true);
                } else {
                    OreSpawnPet.setSitting(this, PET_FLAGS, false);
                }
                if (!player.getAbilities().instabuild) {
                    stack.shrink(1);
                }
                if (!held.isEmpty()) {
                    if (stack.isEmpty()) {
                        player.setItemInHand(hand, held);
                    } else if (!player.getInventory().add(held)) {
                        player.drop(held, false);
                    }
                }
            }
            return InteractionResult.SUCCESS;
        }

        // gold: empty hand owner → take gear or health message (not sit toggle)
        if (OreSpawnPet.isTame(this, PET_FLAGS)
                && stack.isEmpty()
                && OreSpawnPet.isOwnedBy(this, OWNER, player)) {
            EquipmentSlot[] order = {
                EquipmentSlot.MAINHAND,
                EquipmentSlot.FEET,
                EquipmentSlot.LEGS,
                EquipmentSlot.CHEST,
                EquipmentSlot.HEAD
            };
            for (EquipmentSlot slot : order) {
                ItemStack gear = this.getItemBySlot(slot);
                if (!gear.isEmpty()) {
                    player.setItemInHand(hand, gear);
                    this.setItemSlot(slot, ItemStack.EMPTY);
                    OreSpawnPet.setSitting(this, PET_FLAGS, false);
                    if (!this.level().isClientSide) {
                        this.level().broadcastEntityEvent(this, (byte) 6);
                    }
                    return InteractionResult.SUCCESS;
                }
            }
            if (!this.level().isClientSide) {
                OreSpawnPet.setSitting(this, PET_FLAGS, false);
                this.level().broadcastEntityEvent(this, (byte) 7);
                player.displayClientMessage(
                        Component.literal(String.format(
                                "I have %d health. Thank you for asking! xoxo",
                                this.getGirlfriendHealth())),
                        false);
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
        tag.putInt("GirlType", this.getTameSkin());
        tag.putInt("WetGirlType", this.getWetTameSkin());
        tag.putInt("GirlVoice", this.getVoice());
        tag.putInt("GirlVoiceEnable", this.getVoiceEnable());
        tag.putInt("IsPrincess", this.getPrincess());
        tag.putInt("feelingBetter", this.getFeelingBetter());
        tag.putInt("wet_count", this.wet_count);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        OreSpawnPet.readAdditionalSaveData(this, PET_FLAGS, OWNER, tag);
        this.which_girl = tag.getInt("GirlType");
        this.setTameSkin(this.which_girl);
        this.which_wet_girl = tag.getInt("WetGirlType");
        this.setWetTameSkin(this.which_wet_girl);
        this.voice = tag.getInt("GirlVoice");
        this.entityData.set(DATA_VOICE, this.voice);
        this.voice_enable = tag.getInt("GirlVoiceEnable");
        this.entityData.set(DATA_VOICE_ENABLE, this.voice_enable);
        this.is_princess = tag.getInt("IsPrincess");
        this.entityData.set(DATA_PRINCESS, this.is_princess);
        this.feelingBetter = tag.getInt("feelingBetter");
        this.entityData.set(DATA_FEELING_BETTER, this.feelingBetter);
        this.wet_count = tag.getInt("wet_count");
        if (OreSpawnPet.isTame(this, PET_FLAGS)) {
            this.setPersistenceRequired();
        }
        if (getValentinesDay() != 0 && this.feelingBetter != 0) {
            this.refreshDimensions();
        }
        this.applyMaxHealthForMode();
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        if (OreSpawnPet.isTame(this, PET_FLAGS)) {
            return false;
        }
        return !this.isPersistenceRequired();
    }

    @Override
    public boolean causeFallDamage(float fallDistance, float multiplier, DamageSource source) {
        // gold fall: damage min(ceil(dist-3), 3)
        float i = Mth.ceil(fallDistance - 3.0F);
        if (i > 0.0F) {
            if (i > 3.0F) {
                this.playSound(SoundEvents.PLAYER_BIG_FALL, 1.0F, 1.0F);
                i = 3.0F;
            } else {
                this.playSound(SoundEvents.PLAYER_SMALL_FALL, 1.0F, 1.0F);
            }
            this.hurt(this.damageSources().fall(), i);
            return true;
        }
        return false;
    }

    @Override
    protected void checkFallDamage(double y, boolean onGround, BlockState state, BlockPos pos) {
        // custom fall handled in causeFallDamage
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        // gold: ignore cactus; cap damage at 10
        if ("cactus".equals(source.getMsgId())) {
            return false;
        }
        float p2 = amount;
        if (p2 > 10.0F) {
            p2 = 10.0F;
        }
        if ("inWall".equals(source.getMsgId()) && getValentinesDay() != 0) {
            return false;
        }
        // gold: valentines RoseSword calm — MyRoseSword / MyLove not ported; deferred
        return super.hurt(source, p2);
    }

    @Override
    public boolean canAttack(LivingEntity target) {
        if (OreSpawnPet.isOwnedBy(this, OWNER, target)) {
            return false;
        }
        if (target instanceof Girlfriend other
                && OreSpawnPet.isTame(this, PET_FLAGS)
                && OreSpawnPet.isTame(other, PET_FLAGS)
                && this.getOwnerUUID() != null
                && this.getOwnerUUID().equals(other.getOwnerUUID())) {
            // still allow jealousy vs same-owner girlfriends (gold)
        }
        if (target instanceof Player
                && OreSpawnPet.isTame(this, PET_FLAGS)
                && !(getValentinesDay() != 0 && this.getFeelingBetter() == 0)) {
            return false;
        }
        return super.canAttack(target);
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        if (OreSpawnPet.isSitting(this, PET_FLAGS) || this.getVoiceEnable() == 0) {
            return null;
        }
        // gold dance silence — dance AI deferred
        if (this.random.nextInt(11) != 1) {
            return null;
        }
        if (this.getTarget() != null) {
            return null;
        }
        if (this.isInWaterOrBubble()) {
            return SoundEvents.GENERIC_SPLASH; // gold orespawn:o_water
        }
        // gold o_happy / o_hurt / weather lines deferred to SoundsHandler
        if (!OreSpawnPet.isTame(this, PET_FLAGS)) {
            return null;
        }
        if (this.mygetMaxHealth() > this.getHealth()
                || (getValentinesDay() != 0 && this.getFeelingBetter() == 0)) {
            return SoundEvents.VILLAGER_HURT; // stand-in o_hurt
        }
        return SoundEvents.VILLAGER_AMBIENT; // stand-in o_happy
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return this.getVoiceEnable() == 0 ? null : SoundEvents.PLAYER_HURT; // gold o_ow
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return OreSpawnPet.isTame(this, PET_FLAGS)
                ? SoundEvents.PLAYER_DEATH
                : SoundEvents.VILLAGER_DEATH;
    }

    @Override
    protected float getSoundVolume() {
        return 0.3F;
    }

    @Override
    public float getVoicePitch() {
        // gold: (voice - 5) * 0.02F + 1.0F
        return (this.getVoice() - 5) * 0.02F + 1.0F;
    }

    @Override
    protected void dropCustomDeathLoot(ServerLevel level, DamageSource damageSource, boolean recentlyHit) {
        super.dropCustomDeathLoot(level, damageSource, recentlyHit);
        // gold: tamed drops 2+r5 poppies; shoe items deferred → extra poppies
        if (OreSpawnPet.isTame(this, PET_FLAGS)) {
            int n = 2 + this.random.nextInt(5);
            for (int i = 0; i < n; i++) {
                this.spawnAtLocation(new ItemStack(Blocks.POPPY));
            }
        }
        // gold shoe piles (MyItemShoes*) — not registered; skip
        if (OreSpawnPet.isTame(this, PET_FLAGS)) {
            for (EquipmentSlot slot : EquipmentSlot.values()) {
                if (slot.getType() == EquipmentSlot.Type.HUMANOID_ARMOR
                        || slot == EquipmentSlot.MAINHAND
                        || slot == EquipmentSlot.OFFHAND) {
                    ItemStack gear = this.getItemBySlot(slot);
                    if (!gear.isEmpty()) {
                        this.spawnAtLocation(gear.copy());
                        this.setItemSlot(slot, ItemStack.EMPTY);
                    }
                }
            }
        }
    }

    /** Gold getCanSpawnHere: near Girlfriend spawner or super. */
    @Override
    public boolean checkSpawnRules(LevelAccessor level, MobSpawnType spawnType) {
        BlockPos origin = this.blockPosition();
        for (int k = -3; k < 3; k++) {
            for (int j = -3; j < 3; j++) {
                for (int i = 0; i < 5; i++) {
                    BlockPos p = origin.offset(j, i, k);
                    BlockState state = level.getBlockState(p);
                    if (state.is(Blocks.SPAWNER)) {
                        BlockEntity be = level.getBlockEntity(p);
                        if (be instanceof SpawnerBlockEntity) {
                            // gold string "Girlfriend" — allow near any spawner in scan box
                            return true;
                        }
                    }
                }
            }
        }
        return super.checkSpawnRules(level, spawnType);
    }

    /** NEVER override canBreatheUnderwater — FINAL on LivingEntity in 1.21.1. Gold returned true. */
}
