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
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.OpenDoorGoal;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RangedAttackGoal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.animal.AbstractGolem;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.entity.projectile.Snowball;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
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
 * Gold {@code Boyfriend} (EntityTameable + IRangedAttackMob) 1:1 best-effort for NeoForge 1.21.1.
 * Size 0.5×1.6, speed 0.3, health 80, attack 8, XP 0, fire immune.
 * Skins boyfriend0–27 dry / swimshorts0–17 wet / FrogPrince skins; tame cooked beef (+peacock stand-in).
 * Registry size/attrs set in {@code ModEntities}.
 */
public class Boyfriend extends Monster implements RangedAttackMob {
    private static final EntityDataAccessor<Byte> PET_FLAGS =
            SynchedEntityData.defineId(Boyfriend.class, EntityDataSerializers.BYTE);
    private static final EntityDataAccessor<Optional<UUID>> OWNER =
            SynchedEntityData.defineId(Boyfriend.class, EntityDataSerializers.OPTIONAL_UUID);
    /** Gold datawatcher 20 — dry skin which_guy 0.27 */
    private static final EntityDataAccessor<Integer> DATA_WHICH_GUY =
            SynchedEntityData.defineId(Boyfriend.class, EntityDataSerializers.INT);
    /** Gold datawatcher 21 — voice pitch index 0.9 */
    private static final EntityDataAccessor<Integer> DATA_VOICE =
            SynchedEntityData.defineId(Boyfriend.class, EntityDataSerializers.INT);
    /** Gold datawatcher 22 — wet skin which_wet_guy 0.17 */
    private static final EntityDataAccessor<Integer> DATA_WET_GUY =
            SynchedEntityData.defineId(Boyfriend.class, EntityDataSerializers.INT);
    /** Gold datawatcher 23 — voice_enable 0/1 */
    private static final EntityDataAccessor<Integer> DATA_VOICE_ENABLE =
            SynchedEntityData.defineId(Boyfriend.class, EntityDataSerializers.INT);
    /** Gold datawatcher 24 — is_prince 0/1/2 */
    private static final EntityDataAccessor<Integer> DATA_IS_PRINCE =
            SynchedEntityData.defineId(Boyfriend.class, EntityDataSerializers.INT);

    public static final float GOLD_WIDTH = 0.5F;
    public static final float GOLD_HEIGHT = 1.6F;
    public static final double GOLD_HEALTH = 80.0;
    public static final double GOLD_SPEED = 0.3;
    public static final double GOLD_ATTACK = 8.0;
    public static final double GOLD_ARMOR = 8.0; // gold getTotalArmorValue floor
    public static final int GOLD_XP = 0;
    public static final double GOLD_FOLLOW = 100.0; // gold field_70174_ab

    /** Dry skins on disk: boyfriend0.png … boyfriend27.png */
    public static final int DRY_SKIN_COUNT = 28;
    /** Wet skins on disk: swimshorts0.png … swimshorts17.png */
    public static final int WET_SKIN_COUNT = 18;

    private static final ResourceLocation[] DRY_TEXTURES = buildTextures("boyfriend", DRY_SKIN_COUNT);
    private static final ResourceLocation[] WET_TEXTURES = buildTextures("swimshorts", WET_SKIN_COUNT);
    private static final ResourceLocation PRINCE_TEXTURE_1 =
            ResourceLocation.fromNamespaceAndPath("orespawn", "textures/entity/frogprince.png");
    private static final ResourceLocation PRINCE_TEXTURE_2 =
            ResourceLocation.fromNamespaceAndPath("orespawn", "textures/entity/frogprince2.png");

    private final float moveSpeed = 0.3F;

    /** Local mirrors of synched skin / voice (gold public which_guy). */
    public int which_guy = 0;
    public int which_wet_guy = 0;
    public int wet_count = 0;
    public int passenger = 0;

    private int auto_heal = 200;
    private int force_sync = 50;
    private int fight_sound_ticker = 0;
    private int taunt_sound_ticker = 0;
    private int had_target = 0;
    private int voice = 0;
    private int voice_enable = 1;
    private int is_prince = 0;
    private int attackAnimTicks = 0;

    private static ResourceLocation[] buildTextures(String prefix, int count) {
        ResourceLocation[] out = new ResourceLocation[count];
        for (int i = 0; i < count; i++) {
            out[i] = ResourceLocation.fromNamespaceAndPath(
                    "orespawn", "textures/entity/" + prefix + i + ".png");
        }
        return out;
    }

    public Boyfriend(EntityType<? extends Boyfriend> type, Level level) {
        super(type, level);
        this.xpReward = GOLD_XP;
        this.which_guy = this.random.nextInt(DRY_SKIN_COUNT);
        this.voice = this.random.nextInt(10);
        this.which_wet_guy = this.random.nextInt(WET_SKIN_COUNT);
        if (this.getNavigation() instanceof GroundPathNavigation gpn) {
            gpn.setCanOpenDoors(true);
            gpn.setCanPassDoors(true);
        }
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, GOLD_HEALTH)
                .add(Attributes.MOVEMENT_SPEED, GOLD_SPEED)
                .add(Attributes.ATTACK_DAMAGE, GOLD_ATTACK)
                .add(Attributes.ARMOR, GOLD_ARMOR)
                .add(Attributes.FOLLOW_RANGE, GOLD_FOLLOW)
                .add(Attributes.STEP_HEIGHT, 1.0);
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
        builder.define(PET_FLAGS, (byte) 0);
        builder.define(OWNER, Optional.empty());
        builder.define(DATA_WHICH_GUY, this.which_guy);
        builder.define(DATA_VOICE, this.voice);
        builder.define(DATA_WET_GUY, this.which_wet_guy);
        builder.define(DATA_VOICE_ENABLE, 1);
        builder.define(DATA_IS_PRINCE, 0);
        this.auto_heal = 200;
        this.force_sync = 50;
        this.fight_sound_ticker = 0;
        this.taunt_sound_ticker = 0;
        this.had_target = 0;
        this.voice_enable = 1;
        this.is_prince = 0;
        this.wet_count = 0;
    }

    @Override
    protected void registerGoals() {
        // gold: FollowOwner 1.4/12/1.5, Tempt cooked_beef, ArrowAttack 1.25/20/10,
        // Swimming, Panic 1.5, WatchClosest player 6, Wander 0.75, LookIdle, OpenDoor, MoveIndoors
        this.goalSelector.addGoal(
                1,
                new FollowOwnerGoal(
                        this,
                        () -> OreSpawnPet.getOwnerUUID(this, OWNER),
                        () -> OreSpawnPet.isSitting(this, PET_FLAGS),
                        1.4,
                        12.0F,
                        1.5F));
        this.goalSelector.addGoal(
                2, new TemptGoal(this, 1.25, Ingredient.of(Items.COOKED_BEEF), false));
        this.goalSelector.addGoal(4, new RangedAttackGoal(this, 1.25, 20, 10.0F));
        this.goalSelector.addGoal(5, new FloatGoal(this));
        this.goalSelector.addGoal(6, new PanicGoal(this, 1.5));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(8, new WanderALotGoal(this, 10, 0.75));
        this.goalSelector.addGoal(9, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(10, new OpenDoorGoal(this, true));
        // gold EntityAIMoveIndoors — villager-specific; deferred

        // gold targets when PlayNicely==0: Creeper 20, Living/IMob 15, Jealousy Boyfriend (deferred)
        if (OreSpawnMain.PlayNicely == 0) {
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
                                    && !(living instanceof Boyfriend)
                                    && !(living instanceof AbstractGolem)));
        }
        // gold MyEntityAIJealousy(Boyfriend) deferred — no JealousyGoal in util.ai yet
    }

    public int mygetMaxHealth() {
        return (int) GOLD_HEALTH;
    }

    public int getBoyfriendHealth() {
        return (int) this.getHealth();
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

    public void setPrince(int par1) {
        this.is_prince = par1;
        if (!this.level().isClientSide) {
            this.entityData.set(DATA_IS_PRINCE, par1);
        }
    }

    public int getPrince() {
        return this.entityData.get(DATA_IS_PRINCE);
    }

    public int getTameSkin() {
        return this.entityData.get(DATA_WHICH_GUY);
    }

    public void setTameSkin(int par1) {
        int clamped = Mth.clamp(par1, 0, DRY_SKIN_COUNT - 1);
        this.entityData.set(DATA_WHICH_GUY, clamped);
        this.which_guy = clamped;
    }

    public int getWetTameSkin() {
        return this.entityData.get(DATA_WET_GUY);
    }

    public void setWetTameSkin(int par1) {
        int clamped = Mth.clamp(par1, 0, WET_SKIN_COUNT - 1);
        this.entityData.set(DATA_WET_GUY, clamped);
        this.which_wet_guy = clamped;
    }

    public int getVoice() {
        return this.entityData.get(DATA_VOICE);
    }

    public int getVoiceEnable() {
        return this.entityData.get(DATA_VOICE_ENABLE);
    }

    /**
     * Gold {@code getTexture()} — dry skins / prince / wet swimshorts.
     * Paths under {@code textures/entity/}.
     */
    public ResourceLocation getTexture() {
        if (this.wet_count <= 0) {
            int prince = this.getPrince();
            if (prince == 1) {
                return PRINCE_TEXTURE_1;
            }
            if (prince == 2) {
                return PRINCE_TEXTURE_2;
            }
            int t = this.getTameSkin();
            if (t >= 0 && t < DRY_TEXTURES.length) {
                return DRY_TEXTURES[t];
            }
            return DRY_TEXTURES[0];
        }
        int temp = this.getWetTameSkin();
        if (temp >= 0 && temp < WET_TEXTURES.length) {
            return WET_TEXTURES[temp];
        }
        return WET_TEXTURES[0];
    }

    public ItemStack getCurrentEquippedItem() {
        return this.getItemBySlot(EquipmentSlot.MAINHAND);
    }

    /** Gold isWheat / tempt food: cooked beef or MyPeacock (cooked chicken stand-in). */
    public boolean isWheat(ItemStack stack) {
        return !stack.isEmpty() && (stack.is(Items.COOKED_BEEF) || stack.is(Items.COOKED_CHICKEN));
    }

    /**
     * Gold {@code OreSpawnMain.bro_mode} — field not on port OreSpawnMain yet; default 0.
     * May be promoted to {@code OreSpawnMain.bro_mode}.
     */
    private static int broMode() {
        return 0;
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        // gold canDespawn always false
        return false;
    }

    // gold canBreatheUnderwater=true — LivingEntity method is final in 1.21; air forced in tick

    @Override
    public void tick() {
        if (this.getAttribute(Attributes.MOVEMENT_SPEED) != null) {
            this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(this.moveSpeed);
        }
        // gold canBreatheUnderwater
        this.setAirSupply(this.getMaxAirSupply());
        if (OreSpawnPet.isSitting(this, PET_FLAGS)) {
            this.getNavigation().stop();
        }
        super.tick();

        this.passenger = 0;
        // gold: ride alongside owner on Elevator — Elevator entity not ported; deferred

        if (this.attackAnimTicks > 0) {
            this.attackAnimTicks--;
        }
    }

    @Override
    public void aiStep() {
        this.updateSwingTime();
        super.aiStep();

        // gold: isInWater || isInLava
        if (this.isInWater() || this.isInLava()) {
            this.wet_count = 500;
        } else if (this.wet_count > 0) {
            this.wet_count--;
        }

        this.auto_heal--;
        if (this.auto_heal <= 0) {
            if (this.mygetMaxHealth() > this.getBoyfriendHealth()) {
                this.heal(1.0F);
            }
            this.auto_heal = 150;
        }

        this.force_sync--;
        if (this.force_sync <= 0) {
            this.force_sync = 20;
            if (!this.level().isClientSide) {
                this.entityData.set(DATA_VOICE, this.voice);
                this.entityData.set(DATA_VOICE_ENABLE, this.voice_enable);
                this.entityData.set(DATA_IS_PRINCE, this.is_prince);
                // re-assert sitting flag (gold force setSitting(isSitting))
                OreSpawnPet.setSitting(this, PET_FLAGS, OreSpawnPet.isSitting(this, PET_FLAGS));
            } else {
                this.voice = this.getVoice();
                this.voice_enable = this.getVoiceEnable();
                this.is_prince = this.getPrince();
                this.which_guy = this.getTameSkin();
                this.which_wet_guy = this.getWetTameSkin();
            }
        }
    }

    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
        // gold updateAITick melee when holding item + target
        ItemStack stack = this.getCurrentEquippedItem();
        LivingEntity victim = this.getTarget();
        if (OreSpawnMain.PlayNicely != 0) {
            victim = null;
        }

        if (this.random.nextInt(100) == 1) {
            this.setLastHurtByMob(null);
        }

        if (!stack.isEmpty() && !OreSpawnPet.isSitting(this, PET_FLAGS)) {
            if (victim != null) {
                float dist = this.distanceTo(victim);
                boolean berthaReach = stack.is(ModItems.BERTHA.get()) && dist < 10.0F;
                if (dist < 4.0F || berthaReach) {
                    this.attackAnimTicks--;
                    if (this.attackAnimTicks <= 0) {
                        this.attackAnimTicks = 25;
                        this.swing(InteractionHand.MAIN_HAND);
                        this.attackTargetEntityWithCurrentItem(victim);
                        this.fight_sound_ticker--;
                        if (this.fight_sound_ticker <= 0) {
                            if (this.voice_enable != 0) {
                                // gold orespawn:b_fight
                                this.playSound(SoundEvents.PLAYER_ATTACK_STRONG, 0.5F, this.getVoicePitch());
                            }
                            this.fight_sound_ticker = 3;
                        }
                        this.had_target = 1;
                    }
                } else if (dist < 7.0F && !isUltimateBow(stack)) {
                    this.taunt_sound_ticker--;
                    if (this.taunt_sound_ticker <= 0) {
                        if (this.voice_enable != 0) {
                            // gold orespawn:b_taunt
                            this.playSound(SoundEvents.VILLAGER_NO, 0.5F, this.getVoicePitch());
                        }
                        this.taunt_sound_ticker = 300;
                    }
                    this.getNavigation().moveTo(victim, 1.25);
                }
            } else {
                this.fight_sound_ticker = 0;
                this.attackAnimTicks = 0;
                if (this.had_target != 0) {
                    this.had_target = 0;
                    if (this.voice_enable != 0) {
                        // gold orespawn:b_woohoo
                        this.playSound(SoundEvents.PLAYER_LEVELUP, 0.4F, this.getVoicePitch());
                    }
                }
            }
        }
    }

    private static boolean isUltimateBow(ItemStack stack) {
        // gold OreSpawnMain.MyUltimateBow — not registered yet
        return false;
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
        // gold potion strength/weakness omitted (effect API differs)
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
        // gold fire aspect on held item — vanilla enchantment helpers deferred for simplicity
    }

    @Override
    public void performRangedAttack(LivingEntity target, float distanceFactor) {
        // gold attackEntityWithRangedAttack / func_82196_d
        if (this.swinging) {
            return;
        }
        ItemStack it = this.getCurrentEquippedItem();
        if (isUltimateBow(it) && this.level() instanceof ServerLevel server) {
            // UltimateArrow deferred — vanilla Arrow stand-in
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
            it.hurtAndBreak(1, this, EquipmentSlot.MAINHAND);
        } else {
            // gold Shoes projectile — Snowball stand-in until Shoes entity ported
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
        return super.hurt(source, p2);
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
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack var2 = player.getItemInHand(hand);
        if (var2.isEmpty()) {
            ItemStack off = player.getOffhandItem();
            if (!off.isEmpty() && hand == InteractionHand.MAIN_HAND) {
                // prefer main; empty-hand path below
            }
        }
        double distSq = player.distanceToSqr(this);
        if (distSq >= 16.0) {
            return InteractionResult.PASS;
        }

        // gold: cooked beef / MyPeacock tame+heal
        if (!var2.isEmpty() && this.isWheat(var2)) {
            if (!OreSpawnPet.isTame(this, PET_FLAGS)) {
                if (!this.level().isClientSide) {
                    if (this.random.nextInt(3) == 0 || player.getAbilities().instabuild) {
                        OreSpawnPet.tame(this, PET_FLAGS, OWNER, player);
                        this.heal(this.mygetMaxHealth() - this.getHealth());
                    } else {
                        this.level().broadcastEntityEvent(this, (byte) 6);
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
                var2.shrink(1);
            }
            return InteractionResult.SUCCESS;
        }

        // gold: dead_bush release
        if (OreSpawnPet.isTame(this, PET_FLAGS)
                && !var2.isEmpty()
                && var2.is(Blocks.DEAD_BUSH.asItem())
                && OreSpawnPet.isOwnedBy(this, OWNER, player)) {
            if (!this.level().isClientSide) {
                OreSpawnPet.setTame(this, PET_FLAGS, false);
                OreSpawnPet.setOwnerUUID(this, OWNER, null);
                OreSpawnPet.setSitting(this, PET_FLAGS, false);
                this.level().broadcastEntityEvent(this, (byte) 6);
            }
            if (!player.getAbilities().instabuild) {
                var2.shrink(1);
            }
            return InteractionResult.SUCCESS;
        }

        // gold: MyRuby → mute voice
        if (OreSpawnPet.isTame(this, PET_FLAGS)
                && !var2.isEmpty()
                && var2.is(ModItems.RUBY.get())
                && OreSpawnPet.isOwnedBy(this, OWNER, player)) {
            if (!this.level().isClientSide) {
                this.voice_enable = 0;
                this.entityData.set(DATA_VOICE_ENABLE, 0);
                this.level().broadcastEntityEvent(this, (byte) 7);
            }
            if (!player.getAbilities().instabuild) {
                var2.shrink(1);
            }
            return InteractionResult.SUCCESS;
        }

        // gold: MyAmethyst → enable voice
        if (OreSpawnPet.isTame(this, PET_FLAGS)
                && !var2.isEmpty()
                && var2.is(ModItems.AMETHYST.get())
                && OreSpawnPet.isOwnedBy(this, OWNER, player)) {
            if (!this.level().isClientSide) {
                this.voice_enable = 1;
                this.entityData.set(DATA_VOICE_ENABLE, 1);
                this.level().broadcastEntityEvent(this, (byte) 7);
            }
            if (!player.getAbilities().instabuild) {
                var2.shrink(1);
            }
            return InteractionResult.SUCCESS;
        }

        // gold: feather / MyPeacockFeather → cycle skin
        if (OreSpawnPet.isTame(this, PET_FLAGS)
                && !var2.isEmpty()
                && var2.is(Items.FEATHER)
                && OreSpawnPet.isOwnedBy(this, OWNER, player)) {
            if (!this.level().isClientSide) {
                if (this.wet_count <= 0 && !this.isInWater()) {
                    this.which_guy++;
                    if (this.which_guy >= DRY_SKIN_COUNT) {
                        this.which_guy = 0;
                    }
                    this.setTameSkin(this.which_guy);
                    this.level().broadcastEntityEvent(this, (byte) 7);
                } else {
                    this.which_wet_guy++;
                    if (this.which_wet_guy >= WET_SKIN_COUNT) {
                        this.which_wet_guy = 0;
                    }
                    this.setWetTameSkin(this.which_wet_guy);
                    this.level().broadcastEntityEvent(this, (byte) 7);
                    if (this.isInWater()) {
                        this.wet_count = 500;
                    }
                }
            }
            if (!player.getAbilities().instabuild) {
                var2.shrink(1);
            }
            return InteractionResult.SUCCESS;
        }

        // gold: owned + holding item → food heal or equip swap (before diamond_block rebind)
        if (OreSpawnPet.isTame(this, PET_FLAGS)
                && !var2.isEmpty()
                && OreSpawnPet.isOwnedBy(this, OWNER, player)
                && !var2.is(Items.NAME_TAG)) {
            FoodProperties food = var2.getFoodProperties(this);
            if (food != null) {
                if (!this.level().isClientSide) {
                    if (this.mygetMaxHealth() > this.getHealth()) {
                        this.heal(food.nutrition() * 5.0F);
                    }
                    this.level().broadcastEntityEvent(this, (byte) 7);
                }
                if (!player.getAbilities().instabuild) {
                    var2.shrink(1);
                }
            } else {
                if (!this.level().isClientSide) {
                    this.level().broadcastEntityEvent(this, (byte) 7);
                }
                ItemStack held = this.getCurrentEquippedItem();
                // give player item to main hand slot of boyfriend
                ItemStack give = var2.copyWithCount(1);
                this.setItemSlot(EquipmentSlot.MAINHAND, give);
                if (var2.is(Items.DIAMOND)) {
                    OreSpawnPet.setSitting(this, PET_FLAGS, true);
                } else {
                    OreSpawnPet.setSitting(this, PET_FLAGS, false);
                }
                if (!held.isEmpty()) {
                    player.setItemInHand(hand, held);
                } else {
                    if (!player.getAbilities().instabuild) {
                        var2.shrink(1);
                    }
                    // gold OreSpawn armor auto-slot
                    Item itm = give.getItem();
                    if (itm instanceof ArmorItem armor && isOreSpawnArmorPiece(give)) {
                        EquipmentSlot slot = armor.getType().getSlot();
                        ItemStack previous = this.getItemBySlot(slot);
                        this.setItemSlot(slot, give);
                        this.setItemSlot(EquipmentSlot.MAINHAND, previous);
                    }
                }
            }
            return InteractionResult.SUCCESS;
        }

        // gold: diamond_block re-bind owner when already tamed (no isOwner check; owned hits equip above)
        if (OreSpawnPet.isTame(this, PET_FLAGS)
                && !var2.isEmpty()
                && var2.is(Blocks.DIAMOND_BLOCK.asItem())) {
            OreSpawnPet.setSitting(this, PET_FLAGS, false);
            if (!this.level().isClientSide) {
                OreSpawnPet.tame(this, PET_FLAGS, OWNER, player);
            }
            if (!player.getAbilities().instabuild) {
                var2.shrink(1);
            }
            return InteractionResult.SUCCESS;
        }

        // gold: name tag
        if (OreSpawnPet.isTame(this, PET_FLAGS)
                && !var2.isEmpty()
                && var2.is(Items.NAME_TAG)
                && OreSpawnPet.isOwnedBy(this, OWNER, player)) {
            this.setCustomName(var2.getHoverName());
            if (!player.getAbilities().instabuild) {
                var2.shrink(1);
            }
            return InteractionResult.SUCCESS;
        }

        // gold: empty hand owned — take gear or report health
        if (OreSpawnPet.isTame(this, PET_FLAGS)
                && var2.isEmpty()
                && OreSpawnPet.isOwnedBy(this, OWNER, player)) {
            EquipmentSlot[] order = {
                EquipmentSlot.MAINHAND,
                EquipmentSlot.FEET,
                EquipmentSlot.LEGS,
                EquipmentSlot.CHEST,
                EquipmentSlot.HEAD
            };
            ItemStack found = ItemStack.EMPTY;
            EquipmentSlot foundSlot = null;
            for (EquipmentSlot slot : order) {
                ItemStack s = this.getItemBySlot(slot);
                if (!s.isEmpty()) {
                    found = s;
                    foundSlot = slot;
                    break;
                }
            }
            if (!found.isEmpty() && foundSlot != null) {
                player.setItemInHand(hand, found);
                this.setItemSlot(foundSlot, ItemStack.EMPTY);
                OreSpawnPet.setSitting(this, PET_FLAGS, false);
                if (!this.level().isClientSide) {
                    this.level().broadcastEntityEvent(this, (byte) 6);
                }
            } else if (!this.level().isClientSide) {
                OreSpawnPet.setSitting(this, PET_FLAGS, false);
                this.level().broadcastEntityEvent(this, (byte) 7);
                player.displayClientMessage(
                        Component.literal(
                                String.format(
                                        "I have %d health. Thanks for asking!",
                                        this.getBoyfriendHealth())),
                        true);
            }
            return InteractionResult.SUCCESS;
        }

        return InteractionResult.PASS;
    }

    private static boolean isOreSpawnArmorPiece(ItemStack stack) {
        Item itm = stack.getItem();
        return itm == ModItems.EMERALD_HELMET.get()
                || itm == ModItems.EMERALD_CHESTPLATE.get()
                || itm == ModItems.EMERALD_LEGGINGS.get()
                || itm == ModItems.EMERALD_BOOTS.get()
                || itm == ModItems.AMETHYST_HELMET.get()
                || itm == ModItems.AMETHYST_CHESTPLATE.get()
                || itm == ModItems.AMETHYST_LEGGINGS.get()
                || itm == ModItems.AMETHYST_BOOTS.get()
                || itm == ModItems.ULTIMATE_HELMET.get()
                || itm == ModItems.ULTIMATE_CHESTPLATE.get()
                || itm == ModItems.ULTIMATE_LEGGINGS.get()
                || itm == ModItems.ULTIMATE_BOOTS.get();
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
        // gold NBT keys GirlType / WetGirlType / GirlVoice… (shared with Girlfriend schema)
        tag.putInt("GirlType", this.getTameSkin());
        tag.putInt("WetGirlType", this.getWetTameSkin());
        tag.putInt("GirlVoice", this.entityData.get(DATA_VOICE));
        tag.putInt("GirlVoiceEnable", this.entityData.get(DATA_VOICE_ENABLE));
        tag.putInt("IsPrince", this.entityData.get(DATA_IS_PRINCE));
        OreSpawnPet.addAdditionalSaveData(this, PET_FLAGS, OWNER, tag);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.contains("GirlType")) {
            this.setTameSkin(tag.getInt("GirlType"));
        }
        if (tag.contains("WetGirlType")) {
            this.setWetTameSkin(tag.getInt("WetGirlType"));
        }
        if (tag.contains("GirlVoice")) {
            this.voice = tag.getInt("GirlVoice");
            this.entityData.set(DATA_VOICE, this.voice);
        }
        if (tag.contains("GirlVoiceEnable")) {
            this.voice_enable = tag.getInt("GirlVoiceEnable");
            this.entityData.set(DATA_VOICE_ENABLE, this.voice_enable);
        }
        if (tag.contains("IsPrince")) {
            this.is_prince = tag.getInt("IsPrince");
            this.entityData.set(DATA_IS_PRINCE, this.is_prince);
        }
        OreSpawnPet.readAdditionalSaveData(this, PET_FLAGS, OWNER, tag);
        if (OreSpawnPet.isTame(this, PET_FLAGS)) {
            this.setPersistenceRequired();
        }
    }

    @Override
    protected SoundEvent getAmbientSound() {
        // gold getLivingSound — complex conditions; stand-in villager until SoundsHandler wires b_*
        if (OreSpawnPet.isSitting(this, PET_FLAGS) || this.voice_enable == 0) {
            return null;
        }
        if (broMode() != 0 && this.random.nextInt(2) == 1) {
            return null;
        }
        if (this.random.nextInt(11) != 1) {
            return null;
        }
        if (this.getTarget() != null) {
            return null;
        }
        if (this.isInWater()) {
            return SoundEvents.DOLPHIN_AMBIENT_WATER; // stand-in b_water
        }
        if (this.random.nextInt(4) != 0) {
            if (this.getY() < 60.0) {
                return null;
            }
            if (this.level().isThundering()) {
                return SoundEvents.LIGHTNING_BOLT_THUNDER; // stand-in b_thunder
            }
            if (this.level().isRaining()) {
                return SoundEvents.WEATHER_RAIN; // stand-in b_rain
            }
            if (!this.level().isDay()
                    && this.level()
                            .canSeeSky(BlockPos.containing(this.getX(), this.getY(), this.getZ()))) {
                if (this.random.nextInt(3) == 0) {
                    return SoundEvents.AMBIENT_CAVE.value(); // stand-in b_dark
                }
                return null;
            }
        }
        if (OreSpawnPet.isTame(this, PET_FLAGS)) {
            if (this.mygetMaxHealth() > this.getHealth()) {
                return SoundEvents.VILLAGER_HURT; // stand-in b_hurt
            }
            // bro_mode: bb_happy else b_happy
            return SoundEvents.VILLAGER_YES;
        }
        return null;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        if (this.voice_enable == 0) {
            return null;
        }
        if (broMode() != 0 && this.random.nextInt(2) == 1) {
            return null;
        }
        // gold orespawn:b_ow
        return SoundEvents.PLAYER_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        if (broMode() != 0) {
            return null;
        }
        // gold: b_death_boyfriend if tamed else b_death_single
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
        int v = this.getVoice();
        return (v - 5) * 0.02F + 1.0F;
    }

    @Override
    protected void dropCustomDeathLoot(ServerLevel level, DamageSource damageSource, boolean recentlyHit) {
        super.dropCustomDeathLoot(level, damageSource, recentlyHit);
        // gold: tamed → 2.6 red flower (poppy)
        if (OreSpawnPet.isTame(this, PET_FLAGS)) {
            int var3 = this.random.nextInt(5) + 2;
            for (int i = 0; i < var3; i++) {
                this.spawnAtLocation(new ItemStack(Items.POPPY));
            }
        }
        // gold MyItemGameController × (10.35) — not registered; deferred
        // gold drop equipped items when tamed
        if (OreSpawnPet.isTame(this, PET_FLAGS)) {
            for (EquipmentSlot slot : EquipmentSlot.values()) {
                ItemStack stack = this.getItemBySlot(slot);
                if (!stack.isEmpty()) {
                    this.spawnAtLocation(stack.copy());
                    this.setItemSlot(slot, ItemStack.EMPTY);
                }
            }
        }
    }

    @Override
    public boolean checkSpawnRules(LevelAccessor level, MobSpawnType spawnType) {
        // gold: allow near Boyfriend spawner
        BlockPos origin = this.blockPosition();
        for (int k = -3; k < 3; k++) {
            for (int j = -3; j < 3; j++) {
                for (int i = 0; i < 5; i++) {
                    BlockPos p = origin.offset(j, i, k);
                    if (level.getBlockState(p).is(Blocks.SPAWNER)) {
                        BlockEntity be = level.getBlockEntity(p);
                        if (be instanceof SpawnerBlockEntity) {
                            // gold entity name "Boyfriend"
                            return true;
                        }
                    }
                }
            }
        }
        return super.checkSpawnRules(level, spawnType);
    }

    @Override
    public boolean isAlliedTo(Entity other) {
        if (other == this) {
            return true;
        }
        if (OreSpawnPet.isTame(this, PET_FLAGS) && other instanceof Player player) {
            return OreSpawnPet.isOwnedBy(this, OWNER, player);
        }
        return super.isAlliedTo(other);
    }
}
