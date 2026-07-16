package danger.orespawn.entity;

import danger.orespawn.init.ModItems;

import danger.orespawn.OreSpawnMain;
import danger.orespawn.entity.tame.OreSpawnPet;
import danger.orespawn.util.ai.FollowOwnerGoal;
import danger.orespawn.util.ai.WanderALotGoal;
import java.util.Comparator;
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
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.PlayerRideableJumping;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.SmallFireball;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

/**
 * Gold {@code ThePrinceTeen} (EntityTameable) 1:1 best-effort for NeoForge 1.21.1.
 * Size 3.25Ã—4.25, speed 0.32, health 1500, attack 50, armor 18, XP 300, follow 1000.
 * Three-headed teen dragon: ground/flight activity, rideable, fireball/bolt combat, apple heal,
 * diamond-block instant tame, ice/flint fire toggle. Head extension data for model anims.
 * Texture: {@code textures/entity/princeteentexture.png}.
 * Registry size/attrs set in {@code ModEntities}.
 */
public class ThePrinceTeen extends Monster implements PlayerRideableJumping {
    private static final EntityDataAccessor<Byte> DATA_ATTACKING =
            SynchedEntityData.defineId(ThePrinceTeen.class, EntityDataSerializers.BYTE);
    private static final EntityDataAccessor<Byte> DATA_ACTIVITY =
            SynchedEntityData.defineId(ThePrinceTeen.class, EntityDataSerializers.BYTE);
    private static final EntityDataAccessor<Byte> DATA_FIRE =
            SynchedEntityData.defineId(ThePrinceTeen.class, EntityDataSerializers.BYTE);
    private static final EntityDataAccessor<Integer> DATA_HEAD1 =
            SynchedEntityData.defineId(ThePrinceTeen.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> DATA_HEAD2 =
            SynchedEntityData.defineId(ThePrinceTeen.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> DATA_HEAD3 =
            SynchedEntityData.defineId(ThePrinceTeen.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Byte> PET_FLAGS =
            SynchedEntityData.defineId(ThePrinceTeen.class, EntityDataSerializers.BYTE);
    private static final EntityDataAccessor<Optional<UUID>> OWNER =
            SynchedEntityData.defineId(ThePrinceTeen.class, EntityDataSerializers.OPTIONAL_UUID);

    public static final float GOLD_WIDTH = 3.25F;
    public static final float GOLD_HEIGHT = 4.25F;
    public static final double GOLD_HEALTH = 1500.0;
    public static final double GOLD_SPEED = 0.32;
    public static final double GOLD_ATTACK = 50.0;
    public static final double GOLD_ARMOR = 18.0;
    public static final int GOLD_XP = 300;
    public static final double GOLD_FOLLOW = 1000.0;

    private final float moveSpeed = 0.32F;
    private int hurtTimer = 0;
    private int wingSound = 0;
    private int ownerFlying = 0;
    private int flyaway = 0;
    private int whichAttack = 0;
    private int fireballTicker = 0;
    private int head1ext = 0;
    private int head2ext = 0;
    private int head3ext = 0;
    private int head1dir = 1;
    private int head2dir = 1;
    private int head3dir = 1;
    private int killCount = 0;
    private int dayCount = 0;
    private int isDay = 0;
    private boolean targetInSight = false;
    private float deltasmooth = 0.0F;
    private boolean playerJumpPending = false;
    @Nullable
    private BlockPos currentFlightTarget;
    private RenderInfo renderdata = new RenderInfo();

    public ThePrinceTeen(EntityType<? extends ThePrinceTeen> type, Level level) {
        super(type, level);
        this.xpReward = GOLD_XP;
        this.noCulling = true;
        this.renderdata = new RenderInfo();
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

    /** Gold was EntityTameable â€” never wipe on Peaceful. */
    @Override
    protected boolean shouldDespawnInPeaceful() {
        return false;
    }

    @Override
    public boolean fireImmune() {
        return true; // gold field_70178_ae
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_ATTACKING, (byte) 0);
        builder.define(DATA_ACTIVITY, (byte) 0);
        builder.define(DATA_FIRE, (byte) 1);
        builder.define(DATA_HEAD1, 0);
        builder.define(DATA_HEAD2, 0);
        builder.define(DATA_HEAD3, 0);
        builder.define(PET_FLAGS, (byte) 0);
        builder.define(OWNER, Optional.empty());
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

    @Override
    protected void registerGoals() {
        // gold: Swimming, FollowOwner 1.1/12/2, Tempt apple, WanderALot, WatchClosest Living 9, LookIdle
        // MoveIndoors skipped; NearestAttackableTarget IMob when PlayNicely==0; HurtByTarget
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(
                1,
                new FollowOwnerGoal(
                        this,
                        () -> OreSpawnPet.getOwnerUUID(this, OWNER),
                        () -> OreSpawnPet.isSitting(this, PET_FLAGS),
                        1.1,
                        12.0F,
                        2.0F));
        this.goalSelector.addGoal(2, new TemptGoal(this, 1.25, Ingredient.of(Items.APPLE), false));
        this.goalSelector.addGoal(3, new WanderALotGoal(this, 16, 0.75));
        this.goalSelector.addGoal(4, new LookAtPlayerGoal(this, LivingEntity.class, 9.0F));
        this.goalSelector.addGoal(5, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(2, new HurtByTargetGoal(this));
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

    public int mygetMaxHealth() {
        return (int) GOLD_HEALTH;
    }

    public int getThePrinceTeenHealth() {
        return (int) this.getHealth();
    }

    public final int getAttacking() {
        return this.entityData.get(DATA_ATTACKING);
    }

    public final void setAttacking(int value) {
        if (!this.level().isClientSide) {
            this.entityData.set(DATA_ATTACKING, (byte) value);
        }
    }

    public final int getActivity() {
        return this.entityData.get(DATA_ACTIVITY);
    }

    public final void setActivity(int value) {
        if (!this.level().isClientSide) {
            this.entityData.set(DATA_ACTIVITY, (byte) value);
        }
    }

    public int getThePrinceTeenFire() {
        return this.entityData.get(DATA_FIRE);
    }

    public void setThePrinceTeenFire(int value) {
        if (!this.level().isClientSide) {
            this.entityData.set(DATA_FIRE, (byte) value);
        }
    }

    public int getHead1Ext() {
        return this.entityData.get(DATA_HEAD1);
    }

    public int getHead2Ext() {
        return this.entityData.get(DATA_HEAD2);
    }

    public int getHead3Ext() {
        return this.entityData.get(DATA_HEAD3);
    }

    public void setHead1Ext(int v) {
        if (!this.level().isClientSide) {
            this.entityData.set(DATA_HEAD1, v);
        }
    }

    public void setHead2Ext(int v) {
        if (!this.level().isClientSide) {
            this.entityData.set(DATA_HEAD2, v);
        }
    }

    public void setHead3Ext(int v) {
        if (!this.level().isClientSide) {
            this.entityData.set(DATA_HEAD3, v);
        }
    }

    public boolean isOreSpawnTame() {
        return OreSpawnPet.isTame(this, PET_FLAGS);
    }

    public boolean isOreSpawnSitting() {
        return OreSpawnPet.isSitting(this, PET_FLAGS);
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        if (this.isPersistenceRequired()) {
            return false;
        }
        if (this.isVehicle()) {
            return false;
        }
        return !OreSpawnPet.isTame(this, PET_FLAGS);
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    public boolean causeFallDamage(float fallDistance, float multiplier, DamageSource source) {
        return false;
    }

    @Override
    protected void checkFallDamage(double y, boolean onGround, BlockState state, BlockPos pos) {
        // gold no fall / walk damage
    }

    @Override
    public void jumpFromGround() {
        super.jumpFromGround();
        Vec3 m = this.getDeltaMovement();
        this.setDeltaMovement(m.x, m.y + 0.25, m.z);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        // gold: roar when activity==1 and no rider; null when sitting
        if (OreSpawnPet.isSitting(this, PET_FLAGS)) {
            return null;
        }
        if (this.getActivity() == 1 && !this.isVehicle()) {
            return SoundEvents.ENDER_DRAGON_GROWL; // stand-in for orespawn:roar
        }
        return null;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.GENERIC_HURT; // gold orespawn:alo_hurt
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.GENERIC_DEATH; // gold orespawn:alo_death
    }

    @Override
    protected float getSoundVolume() {
        return 0.6F;
    }

    @Override
    public float getVoicePitch() {
        return 0.75F;
    }

    /** Gold mountedYOffset: 2.75 */
    public double getPassengersRidingOffset() {
        return 2.75;
    }

    @Override
    protected boolean canAddPassenger(Entity passenger) {
        return this.getPassengers().isEmpty();
    }

    @Nullable
    @Override
    public LivingEntity getControllingPassenger() {
        Entity p = this.getFirstPassenger();
        return p instanceof LivingEntity living ? living : null;
    }

    @Override
    protected Vec3 getPassengerAttachmentPoint(
            Entity entity, net.minecraft.world.entity.EntityDimensions dimensions, float partialTick) {
        // gold updateRiderPosition: f=0.65 along yaw
        float f = 0.65F;
        double yaw = Math.toRadians(this.getYRot());
        return new Vec3(-f * Math.sin(yaw), this.getPassengersRidingOffset(), f * Math.cos(yaw));
    }

    @Override
    public boolean isControlledByLocalInstance() {
        return this.getControllingPassenger() instanceof Player || super.isControlledByLocalInstance();
    }

    @Override
    public void onPlayerJump(int jumpPower) {
        this.playerJumpPending = true;
    }

    @Override
    public boolean canJump() {
        return true;
    }

    @Override
    public void handleStartJump(int jumpPower) {
        this.playerJumpPending = true;
    }

    @Override
    public void handleStopJump() {
        // no-op
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("ThePrinceTeenAttacking", this.getAttacking());
        tag.putInt("ThePrinceTeenActivity", this.getActivity());
        tag.putInt("ThePrinceTeenFire", this.getThePrinceTeenFire());
        tag.putInt("SpyroKill", this.killCount);
        tag.putInt("SpyroDay", this.dayCount);
        OreSpawnPet.addAdditionalSaveData(this, PET_FLAGS, OWNER, tag);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.setAttacking(tag.getInt("ThePrinceTeenAttacking"));
        this.setActivity(tag.getInt("ThePrinceTeenActivity"));
        this.setThePrinceTeenFire(tag.getInt("ThePrinceTeenFire"));
        this.killCount = tag.getInt("SpyroKill");
        this.dayCount = tag.getInt("SpyroDay");
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
        super.tick();

        // gold: noPhysics when activity==1
        this.noPhysics = this.getActivity() != 0;

        if (!this.level().isClientSide) {
            this.tickHeadExtensions();
        }

        if (this.hurtTimer > 0) {
            this.hurtTimer--;
        }

        if (this.getActivity() == 1) {
            this.wingSound++;
            if (this.wingSound > 20) {
                if (!this.level().isClientSide) {
                    this.playSound(SoundEvents.ENDER_DRAGON_FLAP, 0.5F, 1.0F); // gold MothraWings
                }
                this.wingSound = 0;
            }
        }

        if (this.isInWater()) {
            Vec3 m = this.getDeltaMovement();
            this.setDeltaMovement(m.x, m.y + 0.07, m.z);
        }

        if (!this.level().isClientSide
                && this.getActivity() == 0
                && OreSpawnPet.isTame(this, PET_FLAGS)
                && OreSpawnPet.getOwner(this, OWNER) != null
                && !OreSpawnPet.isSitting(this, PET_FLAGS)) {
            LivingEntity e = OreSpawnPet.getOwner(this, OWNER);
            if (e != null && this.distanceToSqr(e) > 400.0) {
                this.setActivity(1);
            }
        }
    }

    private void tickHeadExtensions() {
        if (this.random.nextInt(10) == 1) {
            int i = this.random.nextInt(3);
            this.head1dir = i == 0 ? 2 : (i == 1 ? -2 : 0);
        }
        if (this.random.nextInt(10) == 1) {
            int i = this.random.nextInt(3);
            this.head2dir = i == 0 ? 2 : (i == 1 ? -2 : 0);
        }
        if (this.random.nextInt(10) == 1) {
            int i = this.random.nextInt(3);
            this.head3dir = i == 0 ? 2 : (i == 1 ? -2 : 0);
        }
        this.head1ext = Mth.clamp(this.head1ext + this.head1dir, 0, 60);
        this.head2ext = Mth.clamp(this.head2ext + this.head2dir, 0, 60);
        this.head3ext = Mth.clamp(this.head3ext + this.head3dir, 0, 60);
        this.setHead1Ext(this.head1ext);
        this.setHead2Ext(this.head2ext);
        this.setHead3Ext(this.head3ext);
    }

    @Override
    public void aiStep() {
        if (this.getActivity() == 0) {
            super.aiStep();
        } else if (this.isDeadOrDying()) {
            super.aiStep();
            return;
        }

        if (this.isDeadOrDying()) {
            return;
        }

        if (this.level().isClientSide) {
            return;
        }

        if (this.getActivity() != 0) {
            if (this.fireballTicker > 0) {
                this.fireballTicker--;
            }
            if (this.getControllingPassenger() instanceof Player player) {
                this.driveFromRider(player);
            } else {
                this.flyWithoutRider();
            }
        }

        this.alwaysDo();

        if (this.isVehicle() && this.getFirstPassenger() != null && !this.getFirstPassenger().isAlive()) {
            this.ejectPassengers();
        }
    }

    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
        if (!OreSpawnPet.isSitting(this, PET_FLAGS)
                && this.getActivity() == 0
                && !this.isVehicle()
                && this.level().getDifficulty() != Difficulty.PEACEFUL
                && this.random.nextInt(10) == 1) {
            LivingEntity e = this.findSomethingToAttack();
            if (e != null) {
                this.setActivity(1);
            } else {
                this.setAttacking(0);
            }
        }

        // gold evolve â†’ ThePrinceAdult when kill_count>25 && day_count>10 â€” deferred (no Adult yet)
        // counters still tracked for NBT parity / future wave

        if (this.isDay == 0) {
            this.isDay = this.level().isDay() ? 1 : -1;
        } else {
            if (this.isDay == -1 && this.level().isDay()) {
                this.dayCount++;
            }
            this.isDay = this.level().isDay() ? 1 : -1;
        }
    }

    private void alwaysDo() {
        if (this.random.nextInt(250) == 1 && this.getHealth() < this.mygetMaxHealth()) {
            this.heal(2.0F);
        }
        if (this.random.nextInt(250) == 0) {
            this.setTarget(null);
        }
        if (!OreSpawnPet.isSitting(this, PET_FLAGS)) {
            this.ownerFlying = 0;
            if (OreSpawnPet.isTame(this, PET_FLAGS)
                    && OreSpawnPet.getOwner(this, OWNER) != null
                    && !this.isVehicle()
                    && !OreSpawnPet.isSitting(this, PET_FLAGS)) {
                LivingEntity owner = OreSpawnPet.getOwner(this, OWNER);
                if (owner instanceof Player player && player.getAbilities().flying) {
                    this.ownerFlying = 1;
                    this.setActivity(1);
                }
            }
            if (this.random.nextInt(50) == 1
                    && !OreSpawnPet.isSitting(this, PET_FLAGS)
                    && !this.targetInSight
                    && !this.isVehicle()) {
                if (this.random.nextInt(15) == 1) {
                    this.setActivity(1);
                } else {
                    this.setActivity(0);
                }
            }
        }
    }

    private void flyWithRider() {
        if (this.isDeadOrDying() || OreSpawnPet.isSitting(this, PET_FLAGS) || this.level().isClientSide) {
            return;
        }
        if (this.random.nextInt(5) == 1 && this.level().getDifficulty() != Difficulty.PEACEFUL) {
            LivingEntity e = this.findSomethingToAttack();
            if (e != null) {
                this.setAttacking(1);
                float reach = 8.0F + e.getBbWidth() / 2.0F;
                if (this.distanceToSqr(e) < reach * reach) {
                    this.doHurtTarget(e);
                } else if (this.distanceToSqr(e) > 100.0
                        && this.distanceToSqr(e) < 625.0
                        && !this.isInWater()
                        && this.getThePrinceTeenFire() != 0) {
                    this.shootSomething(e.getX(), e.getY(), e.getZ());
                }
            } else {
                this.setAttacking(0);
            }
        }
    }

    private void flyWithoutRider() {
        int doNew = 0;
        double ox = 0.0;
        double oy = 0.0;
        double oz = 0.0;
        int hasOwner = 0;
        int tooFar = 0;
        LivingEntity e;

        if (this.currentFlightTarget == null) {
            doNew = 1;
            this.currentFlightTarget = this.blockPosition();
        }

        if (OreSpawnPet.isTame(this, PET_FLAGS) && OreSpawnPet.getOwner(this, OWNER) != null) {
            e = OreSpawnPet.getOwner(this, OWNER);
            if (e != null) {
                hasOwner = 1;
                ox = e.getX();
                oy = e.getY();
                oz = e.getZ();
                if (this.distanceToSqr(e) > 400.0) {
                    tooFar = 1;
                    this.targetInSight = false;
                    this.setAttacking(0);
                    OreSpawnPet.setSitting(this, PET_FLAGS, false);
                    this.flyaway = 0;
                    doNew = 1;
                }
            }
        }

        if (OreSpawnPet.isSitting(this, PET_FLAGS)) {
            return;
        }

        Vec3 dm = this.getDeltaMovement();
        double my = dm.y;
        if (this.getY() < this.currentFlightTarget.getY() + 2.0) {
            my *= 0.7;
        } else if (this.getY() > this.currentFlightTarget.getY() - 2.0) {
            my *= 0.5;
        } else {
            my *= 0.61;
        }

        if (this.random.nextInt(300) == 1) {
            doNew = 1;
        }
        if (this.flyaway > 0) {
            this.flyaway--;
        }

        if (tooFar == 0
                && this.flyaway == 0
                && this.level().getDifficulty() != Difficulty.PEACEFUL
                && this.random.nextInt(7) == 1) {
            e = this.getTarget();
            if (e != null && !e.isAlive()) {
                this.setTarget(null);
                e = null;
            }
            if (e == null) {
                e = this.findSomethingToAttack();
            }
            if (e != null) {
                if (OreSpawnPet.isTame(this, PET_FLAGS) && this.getHealth() / this.mygetMaxHealth() < 0.25F) {
                    this.setActivity(1);
                    this.setAttacking(0);
                    this.targetInSight = false;
                    doNew = 0;
                    this.currentFlightTarget = BlockPos.containing(
                            this.getX() + (this.getX() - e.getX()),
                            this.getY() + 1.0,
                            this.getZ() + (this.getZ() - e.getZ()));
                } else {
                    this.setActivity(1);
                    this.setAttacking(1);
                    this.targetInSight = true;
                    this.currentFlightTarget = BlockPos.containing(e.getX(), e.getY() + 1.0, e.getZ());
                    doNew = 0;
                    float reach = 8.0F + e.getBbWidth() / 2.0F;
                    if (this.distanceToSqr(e) < reach * reach) {
                        this.doHurtTarget(e);
                        this.flyaway = 5 + this.random.nextInt(15);
                        doNew = 1;
                    } else if (this.distanceToSqr(e) < 400.0
                            && !this.isInWater()
                            && this.getThePrinceTeenFire() != 0
                            && this.random.nextInt(2) == 1) {
                        this.shootSomething(e.getX(), e.getY(), e.getZ());
                    }
                }
            } else {
                this.targetInSight = false;
                this.flyaway = 0;
                this.setAttacking(0);
            }
        }

        if (this.currentFlightTarget.distToCenterSqr(this.getX(), this.getY(), this.getZ()) < 2.1) {
            doNew = 1;
        }

        if ((doNew != 0 && !this.targetInSight) || (doNew != 0 && this.flyaway != 0)) {
            int keepTrying = 10;
            while (keepTrying != 0) {
                keepTrying--;
                int gox = (int) this.getX();
                int goy = (int) this.getY();
                int goz = (int) this.getZ();
                int zdir;
                int xdir;
                if (hasOwner == 1) {
                    gox = (int) ox;
                    goy = (int) oy;
                    goz = (int) oz;
                    if (this.ownerFlying == 0) {
                        zdir = this.random.nextInt(14) + 5;
                        xdir = this.random.nextInt(14) + 5;
                    } else {
                        zdir = this.random.nextInt(6);
                        xdir = this.random.nextInt(6);
                    }
                } else {
                    zdir = this.random.nextInt(10) + 16;
                    xdir = this.random.nextInt(10) + 16;
                }
                if (this.random.nextInt(2) == 1) {
                    zdir = -zdir;
                }
                if (this.random.nextInt(2) == 1) {
                    xdir = -xdir;
                }
                BlockPos dest = new BlockPos(
                        gox + xdir,
                        goy + this.random.nextInt(9 + this.ownerFlying * 2) - 4,
                        goz + zdir);
                if (this.level().getBlockState(dest).isAir()
                        && this.canSeeTarget(dest.getX(), dest.getY(), dest.getZ())) {
                    this.currentFlightTarget = dest;
                    break;
                }
            }
        }

        double velocity = Math.sqrt(dm.x * dm.x + dm.z * dm.z);
        double obstruction = 0.0;
        int dist = 2 + (int) (velocity * 4.0);
        for (int k = 1; k < dist; k++) {
            for (int i = 1; i < dist * 2; i++) {
                double dx = i * Math.cos(Math.toRadians(this.getYRot() + 90.0F));
                double dz = i * Math.sin(Math.toRadians(this.getYRot() + 90.0F));
                BlockState bid = this.level()
                        .getBlockState(BlockPos.containing(this.getX() + dx, this.getY() - k, this.getZ() + dz));
                if (!bid.isAir()) {
                    obstruction += 0.05;
                }
            }
        }
        my += obstruction * 0.05;
        this.setPos(this.getX(), this.getY() + obstruction * 0.05, this.getZ());

        double speedFactor = 0.5;
        double var1 = this.currentFlightTarget.getX() + 0.5 - this.getX();
        double var3 = this.currentFlightTarget.getY() + 0.1 - this.getY();
        double var5 = this.currentFlightTarget.getZ() + 0.5 - this.getZ();
        if (this.ownerFlying != 0) {
            speedFactor = 1.75;
            if (OreSpawnPet.isTame(this, PET_FLAGS) && OreSpawnPet.getOwner(this, OWNER) != null) {
                e = OreSpawnPet.getOwner(this, OWNER);
                if (e != null && this.distanceToSqr(e) > 64.0) {
                    speedFactor = 3.5;
                }
            }
        }

        double mx = dm.x + (Math.signum(var1) - dm.x) * 0.15 * speedFactor;
        my = my + (Math.signum(var3) - my) * 0.21 * speedFactor;
        double mz = dm.z + (Math.signum(var5) - dm.z) * 0.15 * speedFactor;
        float var7 = (float) (Math.atan2(mz, mx) * 180.0 / Math.PI) - 90.0F;
        float var8 = Mth.wrapDegrees(var7 - this.getYRot());
        this.setYRot(this.getYRot() + var8 / 4.0F);
        this.setDeltaMovement(mx, my, mz);
        this.move(MoverType.SELF, this.getDeltaMovement());
    }

    private void driveFromRider(Player player) {
        Vec3 dm = this.getDeltaMovement();
        double mx = Mth.clamp(dm.x, -2.0, 2.0);
        double mz = Mth.clamp(dm.z, -2.0, 2.0);
        double my = dm.y;

        double velocity = Math.sqrt(mx * mx + mz * mz);
        double gh = 1.25;
        BlockState below = this.level().getBlockState(BlockPos.containing(this.getX(), this.getY() - gh, this.getZ()));
        if (!below.isAir()) {
            my += 0.03;
            this.setPos(this.getX(), this.getY() + 0.1, this.getZ());
        } else {
            my -= 0.018;
        }

        double obstruction = 0.0;
        int span = 3 + (int) (velocity * 7.0);
        for (int k = 1; k < span; k++) {
            for (int i = 1; i < span * 2; i++) {
                double dx = i * Math.cos(Math.toRadians(this.getYRot() + 90.0F));
                double dz = i * Math.sin(Math.toRadians(this.getYRot() + 90.0F));
                BlockState bid = this.level()
                        .getBlockState(BlockPos.containing(this.getX() + dx, this.getY() - k, this.getZ() + dz));
                if (!bid.isAir()) {
                    obstruction += 0.05;
                }
            }
        }
        my += obstruction * 0.07;
        this.setPos(this.getX(), this.getY() + obstruction * 0.07, this.getZ());
        if (my > 2.0) {
            my = 2.0;
        }

        double d4 = player.getYRot() % 360.0;
        while (d4 < 0.0) {
            d4 += 360.0;
        }
        double d5 = this.getYRot() % 360.0;
        while (d5 < 0.0) {
            d5 += 360.0;
        }
        double relativeG = (d4 - d5) % 180.0;
        while (relativeG < 0.0) {
            relativeG += 180.0;
        }
        if (relativeG > 90.0) {
            relativeG -= 180.0;
        }
        if (velocity > 0.01) {
            double soft = Math.abs(1.85 - velocity);
            soft = Mth.clamp(soft, 0.01, 0.9);
            this.setYRot(player.getYRot() + (float) (relativeG * soft));
        } else {
            this.setYRot(player.getYRot());
        }
        this.setXRot(2.0F * (float) velocity);
        this.yBodyRot = this.getYRot();
        this.yHeadRot = this.getYRot();

        if (this.playerJumpPending) {
            this.playerJumpPending = false;
            my += 0.035;
            my += velocity * 0.046;
        }

        double maxSpeed = 0.95;
        double newVelocity = Math.sqrt(mx * mx + mz * mz);
        double rhm = Math.atan2(mz, mx);
        double rhdir = Math.toRadians((player.getYRot() + 90.0F) % 360.0F);
        double rdv = Math.abs(rhm - rhdir) % (Math.PI * 2.0);
        if (rdv > Math.PI) {
            rdv -= Math.PI * 2.0;
        }
        rdv = Math.abs(rdv);
        if (Math.abs(newVelocity) < 0.01) {
            rdv = 0.0;
        }
        if (rdv > 1.5) {
            newVelocity = -newVelocity;
        }

        float im = player.zza;
        double deltav = 0.0;
        if (Math.abs(im) > 0.001F) {
            if (im > 0.0F) {
                deltav = 0.025;
                if (maxSpeed > 1.0) {
                    deltav += 0.05;
                }
                if (this.deltasmooth < 0.0F) {
                    this.deltasmooth = 0.0F;
                }
                this.deltasmooth = (float) (this.deltasmooth + deltav / 10.0);
                if (this.deltasmooth > deltav) {
                    this.deltasmooth = (float) deltav;
                }
            } else {
                maxSpeed = 0.35;
                deltav = -0.02;
                if (this.deltasmooth > 0.0F) {
                    this.deltasmooth = 0.0F;
                }
                this.deltasmooth = (float) (this.deltasmooth + deltav / 10.0);
                if (this.deltasmooth < deltav) {
                    this.deltasmooth = (float) deltav;
                }
            }
            newVelocity += this.deltasmooth;
            if (newVelocity >= 0.0) {
                if (newVelocity > maxSpeed) {
                    newVelocity = maxSpeed;
                }
                mx = Math.cos(Math.toRadians(this.getYRot() + 90.0F)) * newVelocity;
                mz = Math.sin(Math.toRadians(this.getYRot() + 90.0F)) * newVelocity;
            } else {
                if (newVelocity < -maxSpeed) {
                    newVelocity = -maxSpeed;
                }
                double nv = -newVelocity;
                mx = Math.cos(Math.toRadians(this.getYRot() + 270.0F)) * nv;
                mz = Math.sin(Math.toRadians(this.getYRot() + 270.0F)) * nv;
            }
        } else if (newVelocity >= 0.0) {
            mx = Math.cos(Math.toRadians(this.getYRot() + 90.0F)) * newVelocity;
            mz = Math.sin(Math.toRadians(this.getYRot() + 90.0F)) * newVelocity;
        } else {
            mx = Math.cos(Math.toRadians(this.getYRot() + 270.0F)) * (newVelocity * -1.0);
            mz = Math.sin(Math.toRadians(this.getYRot() + 270.0F)) * (newVelocity * -1.0);
        }

        // gold: rider strafe shoots fire / ice / bolt cycle â€” IceBall/ThunderBolt deferred â†’ BetterFireball/SmallFireball
        if (this.fireballTicker == 0 && (player.xxa < -0.001F || player.xxa > 0.001F)) {
            this.fireRiderShot(player);
            this.fireballTicker = 10;
        }

        this.setDeltaMovement(mx, my, mz);
        this.move(MoverType.SELF, this.getDeltaMovement());
        Vec3 after = this.getDeltaMovement();
        this.setDeltaMovement(after.x * 0.985, after.y * 0.94, after.z * 0.985);

        AABB box = this.getBoundingBox().inflate(3.25, 4.0, 3.25);
        List<Entity> list = this.level().getEntities(this, box, ent -> ent != player && ent.isAlive() && ent.isPushable());
        for (Entity listEntity : list) {
            listEntity.push(this);
        }

        this.flyWithRider();
    }

    private void fireRiderShot(Player player) {
        double yoff = 1.5;
        double xzoff = 7.5;
        this.whichAttack++;
        if (this.whichAttack > 2) {
            this.whichAttack = 0;
        }
        if (this.whichAttack == 0) {
            yoff += this.getHead1Ext() * 0.04F;
            double cx = this.getX() - xzoff * Math.sin(Math.toRadians(this.getYRot() - 10.0F));
            double cz = this.getZ() + xzoff * Math.cos(Math.toRadians(this.getYRot() - 10.0F));
            double aimX = Math.cos(Math.toRadians(player.yHeadRot + 90.0F));
            double aimZ = Math.sin(Math.toRadians(player.yHeadRot + 90.0F));
            double aimY = -Math.sin(Math.toRadians(player.getXRot()));
            BetterFireball bf = new BetterFireball(this.level(), this, new Vec3(aimX, aimY, aimZ));
            bf.setNotMe();
            bf.setPos(cx, this.getY() + yoff, cz);
            bf.setDeltaMovement(this.getDeltaMovement());
            this.playSound(SoundEvents.TNT_PRIMED, 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
            this.level().addFreshEntity(bf);
        } else if (this.whichAttack == 1) {
            // gold IceBall â†’ SmallFireball stand-in until IceBall ported
            yoff += this.getHead3Ext() * 0.04F;
            double cx = this.getX() - xzoff * Math.sin(Math.toRadians(this.getYRot() + 10.0F));
            double cz = this.getZ() + xzoff * Math.cos(Math.toRadians(this.getYRot() + 10.0F));
            double var3 = Math.cos(Math.toRadians(player.getYRot() + 90.0F));
            double var5 = -Math.sin(Math.toRadians(player.getXRot()));
            double var77 = Math.sin(Math.toRadians(player.getYRot() + 90.0F));
            SmallFireball ball = new SmallFireball(this.level(), this, new Vec3(var3, var5, var77));
            ball.setPos(cx, this.getY() + yoff, cz);
            ball.setDeltaMovement(ball.getDeltaMovement().scale(2.0));
            this.playSound(SoundEvents.FIREWORK_ROCKET_LAUNCH, 0.75F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
            this.level().addFreshEntity(ball);
        } else {
            // gold ThunderBolt â†’ BetterFireball stand-in
            yoff += this.getHead2Ext() * 0.04F;
            double cx = this.getX() - xzoff * Math.sin(Math.toRadians(this.getYRot()));
            double cz = this.getZ() + xzoff * Math.cos(Math.toRadians(this.getYRot()));
            double aimX = Math.cos(Math.toRadians(player.yHeadRot + 90.0F));
            double aimZ = Math.sin(Math.toRadians(player.yHeadRot + 90.0F));
            double aimY = -Math.sin(Math.toRadians(player.getXRot()));
            BetterFireball lb = new BetterFireball(this.level(), this, new Vec3(aimX, aimY, aimZ));
            lb.setNotMe();
            lb.setPos(cx, this.getY() + yoff, cz);
            lb.setDeltaMovement(this.getDeltaMovement().scale(3.0));
            this.playSound(SoundEvents.ARROW_SHOOT, 0.75F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
            this.level().addFreshEntity(lb);
        }
    }

    private void shootSomething(double x, double y, double z) {
        int which = this.random.nextInt(3);
        double rr = Math.atan2(z - this.getZ(), x - this.getX());
        double rhdir = Math.toRadians((this.getYRot() + 90.0F) % 360.0F);
        double rdd = Math.abs(rr - rhdir) % (Math.PI * 2.0);
        if (rdd > Math.PI) {
            rdd -= Math.PI * 2.0;
        }
        rdd = Math.abs(rdd);
        if (rdd >= 0.5) {
            return;
        }
        if (which == 0) {
            this.fireCanon(x, y, z);
        } else if (which == 1) {
            this.fireCanonL(x, y, z);
        } else {
            this.fireCanonI(x, y, z);
        }
    }

    private void fireCanon(double x, double y, double z) {
        double yoff = 3.5;
        double xzoff = 6.0;
        double cx = this.getX() - xzoff * Math.sin(Math.toRadians(this.getYRot()));
        double cz = this.getZ() + xzoff * Math.cos(Math.toRadians(this.getYRot()));
        float r1 = 5.0F * (this.random.nextFloat() - this.random.nextFloat());
        float r2 = 3.0F * (this.random.nextFloat() - this.random.nextFloat());
        float r3 = 5.0F * (this.random.nextFloat() - this.random.nextFloat());
        BetterFireball bf = new BetterFireball(
                this.level(),
                this,
                new Vec3(x - cx + r1, y + 0.25 - (this.getY() + yoff) + r2, z - cz + r3));
        bf.setPos(cx, this.getY() + yoff, cz);
        bf.setBig();
        this.playSound(SoundEvents.ARROW_SHOOT, 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
        this.level().addFreshEntity(bf);
    }

    private void fireCanonL(double x, double y, double z) {
        // gold ThunderBolt stand-in
        double yoff = 3.5;
        double xzoff = 6.0;
        double cx = this.getX() - xzoff * Math.sin(Math.toRadians(this.getYRot()));
        double cz = this.getZ() + xzoff * Math.cos(Math.toRadians(this.getYRot()));
        float r1 = 5.0F * (this.random.nextFloat() - this.random.nextFloat());
        float r2 = 3.0F * (this.random.nextFloat() - this.random.nextFloat());
        float r3 = 5.0F * (this.random.nextFloat() - this.random.nextFloat());
        BetterFireball lb = new BetterFireball(
                this.level(), this, new Vec3(x - cx + r1, y + 0.25 - (this.getY() + yoff) + r2, z - cz + r3));
        lb.setNotMe();
        lb.setPos(cx, this.getY() + yoff, cz);
        lb.setDeltaMovement(lb.getDeltaMovement().scale(3.0));
        this.playSound(SoundEvents.ARROW_SHOOT, 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
        this.level().addFreshEntity(lb);
    }

    private void fireCanonI(double x, double y, double z) {
        // gold IceBall stand-in
        double yoff = 3.5;
        double xzoff = 6.0;
        double cx = this.getX() - xzoff * Math.sin(Math.toRadians(this.getYRot()));
        double cz = this.getZ() + xzoff * Math.cos(Math.toRadians(this.getYRot()));
        float r1 = 5.0F * (this.random.nextFloat() - this.random.nextFloat());
        float r2 = 3.0F * (this.random.nextFloat() - this.random.nextFloat());
        float r3 = 5.0F * (this.random.nextFloat() - this.random.nextFloat());
        SmallFireball ball = new SmallFireball(
                this.level(), this, new Vec3(x - cx + r1, y + 0.25 - (this.getY() + yoff) + r2, z - cz + r3));
        ball.setPos(cx, this.getY() + yoff, cz);
        this.playSound(SoundEvents.ARROW_SHOOT, 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
        this.level().addFreshEntity(ball);
    }

    private boolean isSuitableTarget(LivingEntity target) {
        if (this.level().getDifficulty() == Difficulty.PEACEFUL) {
            return false;
        }
        if (target == null || target == this || !target.isAlive()) {
            return false;
        }
        if (!this.getSensing().hasLineOfSight(target)) {
            return false;
        }
        // gold MyUtils.isRoyalty â€” skip other royalty family when ported
        if (target instanceof ThePrinceTeen) {
            return false;
        }
        if (target instanceof Spyro) {
            return false;
        }
        if (target instanceof Monster || target instanceof Enemy) {
            return true;
        }
        if (target instanceof Mothra) {
            return true;
        }
        // Kraken deferred
        if (target instanceof Leon leon) {
            return !leon.isOreSpawnTame();
        }
        if (target instanceof WaterDragon wd) {
            return !wd.isOreSpawnTame();
        }
        if (target instanceof GammaMetroid gm) {
            return !gm.isTame();
        }
        return false;
    }

    @Nullable
    private LivingEntity findSomethingToAttack() {
        if (OreSpawnMain.PlayNicely != 0) {
            return null;
        }
        AABB box = this.getBoundingBox().inflate(25.0, 20.0, 25.0);
        List<LivingEntity> list = this.level().getEntitiesOfClass(LivingEntity.class, box, this::isSuitableTarget);
        list.sort(Comparator.comparingDouble(this::distanceToSqr));
        return list.isEmpty() ? null : list.get(0);
    }

    public boolean canSeeTarget(double pX, double pY, double pZ) {
        Vec3 from = new Vec3(this.getX(), this.getY() + 0.75, this.getZ());
        Vec3 to = new Vec3(pX, pY, pZ);
        return this.level()
                        .clip(new ClipContext(from, to, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this))
                        .getType()
                == HitResult.Type.MISS;
    }

    @Override
    public boolean doHurtTarget(Entity target) {
        // gold: DamageSource.causeMobDamage 45 * iskraken, knockback ks=1.75 inair=0.1
        if (target instanceof LivingEntity living) {
            float isKraken = 1.0F; // Kraken class deferred
            float dmg = isKraken * 45.0F;
            living.hurt(this.damageSources().mobAttack(this), dmg);
            float f3 = (float) Math.atan2(living.getZ() - this.getZ(), living.getX() - this.getX());
            double inair = 0.1;
            if (!living.isAlive() || living instanceof Player) {
                inair *= 2.0;
            }
            double ks = 1.75;
            living.push(Math.cos(f3) * ks, inair, Math.sin(f3) * ks);
            if (living instanceof Mob && !living.isAlive()) {
                this.killCount++;
            }
            return true;
        }
        return super.doHurtTarget(target);
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (this.hurtTimer > 0) {
            return false;
        }
        if (source.is(DamageTypes.CACTUS)
                || source.is(DamageTypes.IN_FIRE)
                || source.is(DamageTypes.ON_FIRE)
                || source.is(DamageTypes.LAVA)
                || source.is(DamageTypes.IN_WALL)) {
            return false;
        }

        OreSpawnPet.setSitting(this, PET_FLAGS, false);
        this.setActivity(1);

        Entity e = source.getDirectEntity();
        if (e == null) {
            e = source.getEntity();
        }
        if (e instanceof BetterFireball || e instanceof SmallFireball) {
            e.discard();
            return false;
        }
        if (e instanceof ThePrinceTeen || e instanceof Spyro) {
            return false;
        }

        boolean ret = super.hurt(source, amount);
        this.hurtTimer = 20;
        if (e instanceof LivingEntity living) {
            if (OreSpawnPet.isTame(this, PET_FLAGS) && living instanceof Player) {
                return false;
            }
            this.setTarget(living);
            this.getNavigation().moveTo(living, 1.2);
            ret = true;
        }
        return ret;
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        // gold: diamond block instant tame + force grow counters, distSq < 25
        if (stack.is(Blocks.DIAMOND_BLOCK.asItem()) && this.distanceToSqr(player) < 25.0) {
            if (!this.level().isClientSide) {
                OreSpawnPet.setTame(this, PET_FLAGS, true);
                OreSpawnPet.setOwnerUUID(this, OWNER, player.getUUID());
                OreSpawnPet.setSitting(this, PET_FLAGS, false);
                this.setPersistenceRequired();
                this.level().broadcastEntityEvent(this, (byte) 7);
                this.heal(this.mygetMaxHealth() - this.getHealth());
                this.killCount = 1000;
                this.dayCount = 1000;
            }
            if (!player.getAbilities().instabuild) {
                stack.shrink(1);
            }
            return InteractionResult.SUCCESS;
        }

        if (OreSpawnPet.isTame(this, PET_FLAGS)) {
            if (!OreSpawnPet.isOwnedBy(this, OWNER, player)) {
                return InteractionResult.PASS;
            }

            // empty hand mount
            if (stack.isEmpty() && this.distanceToSqr(player) < 25.0) {
                if (!this.level().isClientSide) {
                    player.startRiding(this);
                    this.setActivity(1);
                    OreSpawnPet.setSitting(this, PET_FLAGS, false);
                }
                return InteractionResult.SUCCESS;
            }

            // apple full heal
            if (stack.is(Items.APPLE) && this.distanceToSqr(player) < 25.0) {
                if (this.level().isClientSide) {
                    this.level().broadcastEntityEvent(this, (byte) 7);
                }
                if (this.mygetMaxHealth() > this.getHealth()) {
                    this.heal(this.mygetMaxHealth() - this.getHealth());
                }
                if (!player.getAbilities().instabuild) {
                    stack.shrink(1);
                }
                return InteractionResult.SUCCESS;
            }

            // any food heal * 10
            FoodProperties food = stack.getFoodProperties(this);
            if (food != null && this.distanceToSqr(player) < 25.0) {
                if (!this.level().isClientSide) {
                    if (this.mygetMaxHealth() > this.getHealth()) {
                        this.heal(food.nutrition() * 10.0F);
                    }
                    this.level().broadcastEntityEvent(this, (byte) 7);
                }
                if (!player.getAbilities().instabuild) {
                    stack.shrink(1);
                }
                return InteractionResult.SUCCESS;
            }

            // ice extinguish fireballs
            if (stack.is(Blocks.ICE.asItem()) && this.distanceToSqr(player) < 25.0) {
                if (!this.level().isClientSide) {
                    this.level().broadcastEntityEvent(this, (byte) 6);
                    this.setThePrinceTeenFire(0);
                    player.displayClientMessage(Component.literal("Fireballs extinguished."), true);
                }
                if (!player.getAbilities().instabuild) {
                    stack.shrink(1);
                }
                return InteractionResult.SUCCESS;
            }

            // flint and steel light fireballs
            if (stack.is(Items.FLINT_AND_STEEL) && this.distanceToSqr(player) < 25.0) {
                if (!this.level().isClientSide) {
                    this.level().broadcastEntityEvent(this, (byte) 6);
                    this.setThePrinceTeenFire(1);
                    player.displayClientMessage(Component.literal("Fireballs lit!"), true);
                }
                if (!player.getAbilities().instabuild) {
                    stack.shrink(1);
                }
                return InteractionResult.SUCCESS;
            }

            // diamond â†’ devolve ThePrince deferred
            if (stack.is(Items.DIAMOND) && this.distanceToSqr(player) < 25.0 && !this.level().isClientSide) {
                // ThePrince not yet ported â€” keep diamond, message only
                player.displayClientMessage(Component.literal("The Prince (younger form) is not ported yet."), true);
                return InteractionResult.SUCCESS;
            }

            // name tag
            if (stack.is(Items.NAME_TAG) && this.distanceToSqr(player) < 25.0) {
                if (!this.level().isClientSide) {
                    this.setCustomName(stack.getHoverName());
                }
                if (!player.getAbilities().instabuild) {
                    stack.shrink(1);
                }
                return InteractionResult.SUCCESS;
            }

            // other item: toggle sit within 16
            if (!stack.isEmpty() && this.distanceToSqr(player) < 16.0) {
                if (!this.level().isClientSide) {
                    boolean now = !OreSpawnPet.isSitting(this, PET_FLAGS);
                    OreSpawnPet.setSitting(this, PET_FLAGS, now);
                    this.setActivity(0);
                }
                return InteractionResult.SUCCESS;
            }
        }

        return InteractionResult.PASS;
    }

    @Override
    public void handleEntityEvent(byte id) {
        if (id == 7) {
            for (int i = 0; i < 20; i++) {
                double d0 = this.random.nextGaussian() * 0.08;
                double d1 = this.random.nextGaussian() * 0.08;
                double d2 = this.random.nextGaussian() * 0.08;
                this.level()
                        .addParticle(
                                ParticleTypes.HEART,
                                this.getX() + (this.random.nextFloat() - this.random.nextFloat()) * 2.5F,
                                this.getY() + 0.5 + this.random.nextFloat() * 1.5,
                                this.getZ() + (this.random.nextFloat() - this.random.nextFloat()) * 2.5F,
                                d0,
                                d1,
                                d2);
            }
        } else if (id == 6) {
            for (int i = 0; i < 20; i++) {
                double d0 = this.random.nextGaussian() * 0.08;
                double d1 = this.random.nextGaussian() * 0.08;
                double d2 = this.random.nextGaussian() * 0.08;
                this.level()
                        .addParticle(
                                ParticleTypes.SMOKE,
                                this.getX() + (this.random.nextFloat() - this.random.nextFloat()) * 2.5F,
                                this.getY() + 0.5 + this.random.nextFloat() * 1.5,
                                this.getZ() + (this.random.nextFloat() - this.random.nextFloat()) * 2.5F,
                                d0,
                                d1,
                                d2);
            }
        } else {
            super.handleEntityEvent(id);
        }
    }

    @Override
    protected void dropCustomDeathLoot(ServerLevel level, DamageSource source, boolean recentlyHit) {
        super.dropCustomDeathLoot(level, source, recentlyHit);
        // gold: drop ThePrinceEgg
        this.spawnAtLocation(new ItemStack(ModItems.THE_PRINCE_EGG.get()));
    }

    /** Gold getCanSpawnHere false â€” never natural spawn. */
    public static boolean checkSpawnRules(
            EntityType<ThePrinceTeen> type,
            net.minecraft.world.level.LevelAccessor level,
            net.minecraft.world.entity.MobSpawnType spawnType,
            BlockPos pos,
            net.minecraft.util.RandomSource random) {
        return false;
    }
}
