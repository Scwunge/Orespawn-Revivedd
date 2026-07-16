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
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.PlayerRideableJumping;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.SmallFireball;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

/**
 * Gold {@code Dragon} (EntityTameable) ported for NeoForge 1.21.1.
 * Size 1.5x1.25, speed 0.32, health 200, attack 35, armor 14, XP 100.
 * Dual activity ground/flight, rideable, fireball combat (type 0 fire / type 1 ice-water stand-ins).
 * Texture: dragon.png / whitedragon.png. ClientProxy ModelDragon(0.65F), shadow 1.25, scale 1.0.
 * Registry size/attrs set in {@code ModEntities}.
 */
public class Dragon extends Monster implements PlayerRideableJumping {
    private static final EntityDataAccessor<Byte> DATA_ATTACKING =
            SynchedEntityData.defineId(Dragon.class, EntityDataSerializers.BYTE);
    private static final EntityDataAccessor<Byte> DATA_ACTIVITY =
            SynchedEntityData.defineId(Dragon.class, EntityDataSerializers.BYTE);
    private static final EntityDataAccessor<Integer> DATA_DRAGON_TYPE =
            SynchedEntityData.defineId(Dragon.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> DATA_DRAGON_FIRE =
            SynchedEntityData.defineId(Dragon.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Byte> PET_FLAGS =
            SynchedEntityData.defineId(Dragon.class, EntityDataSerializers.BYTE);
    private static final EntityDataAccessor<Optional<UUID>> OWNER =
            SynchedEntityData.defineId(Dragon.class, EntityDataSerializers.OPTIONAL_UUID);

    public static final float GOLD_WIDTH = 1.5F;
    public static final float GOLD_HEIGHT = 1.25F;
    public static final double GOLD_HEALTH = 200.0;
    public static final double GOLD_SPEED = 0.32;
    public static final double GOLD_ATTACK = 35.0;
    public static final double GOLD_ARMOR = 14.0;
    public static final int GOLD_XP = 100;
    public static final double GOLD_FOLLOW = 48.0;

    private final float moveSpeed = 0.32F;
    private RenderInfo renderdata = new RenderInfo();
    private int hurtTimer = 0;
    private int wingSound = 0;
    @Nullable
    private BlockPos currentFlightTarget = null;
    private boolean targetInSight = false;
    private int ownerFlying = 0;
    private int flyaway = 0;
    private int stuckCount = 0;
    private int lastX = 0;
    private int lastZ = 0;
    private int unstickTimer = 0;
    private int fireballTicker = 0;
    private float deltasmooth = 0.0F;
    private int dragontype = 0;
    private int closest = 99999;
    private int tx = 0;
    private int ty = 0;
    private int tz = 0;
    private boolean playerJumpPending = false;

    public Dragon(EntityType<? extends Dragon> type, Level level) {
        super(type, level);
        this.xpReward = GOLD_XP;
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

    /** Gold was EntityTameable — never wipe on Peaceful. */
    @Override
    protected boolean shouldDespawnInPeaceful() {
        return false;
    }

    @Override
    protected void registerGoals() {
        // gold: Swimming, FollowOwner(1.1,12,2), Tempt beef 1.25, Wander 0.75, Watch Player 9, LookIdle
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
        this.goalSelector.addGoal(2, new TemptGoal(this, 1.25, Ingredient.of(Items.BEEF), false));
        this.goalSelector.addGoal(3, new WanderALotGoal(this, 10, 0.75));
        this.goalSelector.addGoal(4, new LookAtPlayerGoal(this, Player.class, 9.0F));
        this.goalSelector.addGoal(5, new RandomLookAroundGoal(this));
        if (OreSpawnMain.PlayNicely == 0) {
            this.targetSelector.addGoal(
                    1, new NearestAttackableTargetGoal<>(this, Monster.class, true, false));
        }
        this.targetSelector.addGoal(2, new HurtByTargetGoal(this));
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_ATTACKING, (byte) 0);
        builder.define(DATA_ACTIVITY, (byte) 0);
        builder.define(DATA_DRAGON_TYPE, 0);
        builder.define(DATA_DRAGON_FIRE, 1); // gold data 24 default 1
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

    public final int getAttacking() {
        return this.entityData.get(DATA_ATTACKING) & 0xFF;
    }

    public final void setAttacking(int value) {
        if (!this.level().isClientSide) {
            this.entityData.set(DATA_ATTACKING, (byte) value);
        }
    }

    public final int getActivity() {
        return this.entityData.get(DATA_ACTIVITY) & 0xFF;
    }

    public final void setActivity(int value) {
        if (!this.level().isClientSide) {
            this.entityData.set(DATA_ACTIVITY, (byte) value);
        }
    }

    public int getDragonFire() {
        return this.entityData.get(DATA_DRAGON_FIRE);
    }

    public void setDragonFire(int par1) {
        if (!this.level().isClientSide) {
            this.entityData.set(DATA_DRAGON_FIRE, par1);
        }
    }

    public void setDragonType(int par1) {
        this.dragontype = par1;
        this.entityData.set(DATA_DRAGON_TYPE, par1);
    }

    public int getDragonType() {
        return this.entityData.get(DATA_DRAGON_TYPE);
    }

    public int mygetMaxHealth() {
        return (int) GOLD_HEALTH;
    }

    public int getDragonHealth() {
        return (int) this.getHealth();
    }

    public boolean isOreSpawnTame() {
        return OreSpawnPet.isTame(this, PET_FLAGS);
    }

    public boolean isOreSpawnSitting() {
        return OreSpawnPet.isSitting(this, PET_FLAGS);
    }

    /** Gold isSitting for model anim. */
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

    public boolean isOwnedBy(Entity other) {
        return OreSpawnPet.isOwnedBy(this, OWNER, other);
    }

    @Override
    public boolean fireImmune() {
        return true; // gold field_70178_ae
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        if (this.isPersistenceRequired()) {
            return false;
        }
        if (this.isVehicle()) {
            return false;
        }
        return !this.isOreSpawnTame();
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
        // gold empty
    }

    @Override
    public void jumpFromGround() {
        super.jumpFromGround();
        // gold: motionY += 0.25
        Vec3 m = this.getDeltaMovement();
        this.setDeltaMovement(m.x, m.y + 0.25, m.z);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        // gold: null if sitting; roar if attacking && no rider; else null
        if (this.isOreSpawnSitting()) {
            return null;
        }
        if (this.getAttacking() == 1 && !this.isVehicle()) {
            return SoundEvents.ENDER_DRAGON_GROWL; // stand-in for orespawn:roar
        }
        return null;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        // gold orespawn:alo_hurt
        return SoundEvents.GENERIC_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        // gold orespawn:alo_death
        return SoundEvents.GENERIC_DEATH;
    }

    @Override
    protected float getSoundVolume() {
        return 0.6F;
    }

    @Override
    public float getVoicePitch() {
        return 0.75F;
    }

    /** Gold mountedYOffset: 1.3 */
    public double getPassengersRidingOffset() {
        return 1.3;
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
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        OreSpawnPet.addAdditionalSaveData(this, PET_FLAGS, OWNER, tag);
        tag.putInt("DragonAttacking", this.getAttacking());
        tag.putInt("DragonActivity", this.getActivity());
        tag.putInt("DragonFire", this.getDragonFire());
        tag.putInt("DragonType", this.getDragonType());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        OreSpawnPet.readAdditionalSaveData(this, PET_FLAGS, OWNER, tag);
        this.setAttacking(tag.getInt("DragonAttacking"));
        this.setActivity(tag.getInt("DragonActivity"));
        this.setDragonFire(tag.getInt("DragonFire"));
        this.dragontype = tag.getInt("DragonType");
        this.setDragonType(this.dragontype);
        if (this.isOreSpawnTame()) {
            this.setPersistenceRequired();
        }
    }

    @Override
    public void tick() {
        if (this.getAttribute(Attributes.MOVEMENT_SPEED) != null) {
            this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(this.moveSpeed);
        }
        if (this.isOreSpawnSitting() && !this.isVehicle()) {
            this.getNavigation().stop();
        }
        super.tick();

        if (this.hurtTimer > 0) {
            this.hurtTimer--;
        }

        if (this.getActivity() == 1) {
            this.wingSound++;
            if (this.wingSound > 20) {
                if (!this.level().isClientSide) {
                    // gold orespawn:MothraWings 0.5
                    this.playSound(SoundEvents.ENDER_DRAGON_FLAP, 0.5F, 1.0F);
                }
                this.wingSound = 0;
            }
        }

        if (this.isInWater()) {
            Vec3 m = this.getDeltaMovement();
            this.setDeltaMovement(m.x, m.y + 0.07, m.z);
        }

        if (!this.level().isClientSide) {
            if (this.getActivity() == 0
                    && this.isOreSpawnTame()
                    && this.getOwner() != null
                    && !this.isOreSpawnSitting()) {
                LivingEntity e = this.getOwner();
                if (e != null && this.distanceToSqr(e) > 144.0) {
                    this.setActivity(1);
                }
            }
        }
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
                this.flyWithRider();
            } else {
                this.flyWithoutRider();
            }
        }

        this.alwaysDo();

        if (this.isVehicle() && this.getFirstPassenger() != null && !this.getFirstPassenger().isAlive()) {
            this.ejectPassengers();
        }
    }

    /** Gold always_do — heal, owner flying, activity toggles, lava seek. */
    private void alwaysDo() {
        if (this.level().isClientSide) {
            return;
        }

        if (this.random.nextInt(250) == 1 && this.getHealth() < this.mygetMaxHealth()) {
            this.heal(2.0F);
        }

        if (!this.isOreSpawnSitting()) {
            this.ownerFlying = 0;
            if (this.isOreSpawnTame()
                    && this.getOwner() != null
                    && !this.isVehicle()
                    && !this.isOreSpawnSitting()) {
                if (this.getOwner() instanceof Player pl && pl.getAbilities().flying) {
                    this.ownerFlying = 1;
                    this.setActivity(1);
                }
            }

            if (this.isOreSpawnTame() && this.getOwner() != null && !this.isOreSpawnSitting()) {
                LivingEntity pl = this.getOwner();
                if (pl != null && this.distanceToSqr(pl) > 400.0) {
                    this.setActivity(1);
                }
            }

            if (this.random.nextInt(50) == 1
                    && !this.isOreSpawnSitting()
                    && !this.targetInSight
                    && !this.isVehicle()) {
                if (this.random.nextInt(15) == 1) {
                    this.setActivity(1);
                } else {
                    this.setActivity(0);
                }
            }

            // gold lava seek when not targeting / not ridden
            if (this.random.nextInt(25) == 0 && !this.targetInSight && !this.isVehicle()) {
                this.closest = 99999;
                this.tx = this.ty = this.tz = 0;
                for (int i = 1; i < 11; i++) {
                    int j = i;
                    if (j > 4) {
                        j = 4;
                    }
                    if (this.scanLava((int) this.getX(), (int) this.getY() - 1, (int) this.getZ(), i, j, i)) {
                        break;
                    }
                    if (i >= 6) {
                        i++;
                    }
                }
                if (this.closest < 99999) {
                    this.setActivity(0);
                    this.getNavigation().moveTo(this.tx, this.ty - 1, this.tz, 1.0);
                    if (this.isInLava()) {
                        this.heal(1.0F);
                        this.playSound(
                                SoundEvents.GENERIC_SPLASH, 1.0F, this.random.nextFloat() * 0.2F + 0.9F);
                    }
                }
            }
        }

        // gold updateAITasks-ish: ground activity may hunt → fly
        if (!this.isOreSpawnSitting()
                && this.getActivity() == 0
                && !this.isVehicle()
                && this.level().getDifficulty() != Difficulty.PEACEFUL
                && this.random.nextInt(10) == 1) {
            LivingEntity e = this.findSomethingToAttack();
            if (e != null) {
                this.setActivity(1);
            }
        }
    }

    /** Gold scan_it for lava. */
    private boolean scanLava(int x, int y, int z, int dx, int dy, int dz) {
        int found = 0;
        for (int i = -dy; i <= dy; i++) {
            for (int j = -dz; j <= dz; j++) {
                if (this.tryLavaCell(x + dx, y + i, z + j, dx * dx + j * j + i * i)) {
                    found++;
                }
                if (this.tryLavaCell(x - dx, y + i, z + j, dx * dx + j * j + i * i)) {
                    found++;
                }
            }
        }
        for (int var12 = -dx; var12 <= dx; var12++) {
            for (int j = -dz; j <= dz; j++) {
                if (this.tryLavaCell(x + var12, y + dy, z + j, dy * dy + j * j + var12 * var12)) {
                    found++;
                }
                if (this.tryLavaCell(x + var12, y - dy, z + j, dy * dy + j * j + var12 * var12)) {
                    found++;
                }
            }
        }
        for (int var13 = -dx; var13 <= dx; var13++) {
            for (int j = -dy; j <= dy; j++) {
                if (this.tryLavaCell(x + var13, y + j, z + dz, dz * dz + j * j + var13 * var13)) {
                    found++;
                }
                if (this.tryLavaCell(x + var13, y + j, z - dz, dz * dz + j * j + var13 * var13)) {
                    found++;
                }
            }
        }
        return found != 0;
    }

    private boolean tryLavaCell(int x, int y, int z, int d) {
        BlockState st = this.level().getBlockState(new BlockPos(x, y, z));
        if (st.is(Blocks.LAVA)) {
            if (d < this.closest) {
                this.closest = d;
                this.tx = x;
                this.ty = y;
                this.tz = z;
            }
            return true;
        }
        return false;
    }

    /** Gold fly_with_rider combat pulse. */
    private void flyWithRider() {
        if (this.isDeadOrDying() || this.isOreSpawnSitting() || this.level().isClientSide) {
            return;
        }
        if (this.random.nextInt(7) != 1 || this.level().getDifficulty() == Difficulty.PEACEFUL) {
            return;
        }
        if (this.random.nextInt(250) == 0) {
            this.setTarget(null);
        }
        LivingEntity e = this.getTarget();
        if (e != null && !e.isAlive()) {
            this.setTarget(null);
            e = null;
        }
        if (e == null) {
            e = this.findSomethingToAttack();
        }
        if (e != null) {
            this.setAttacking(1);
            float reach = 7.0F + e.getBbWidth() / 2.0F;
            if (this.distanceToSqr(e) < (double) (reach * reach)) {
                this.doHurtTarget(e);
            }
        } else {
            this.setAttacking(0);
        }
    }

    /** Gold fly_without_rider autonomous flight + ranged. */
    private void flyWithoutRider() {
        if (this.level().isClientSide || this.isOreSpawnSitting() || this.isVehicle()) {
            return;
        }

        int doNew = 0;
        if (this.currentFlightTarget == null) {
            doNew = 1;
            this.currentFlightTarget = this.blockPosition();
        }

        if (this.unstickTimer > 0) {
            this.unstickTimer--;
        }

        if (this.lastX == (int) this.getX() && this.lastZ == (int) this.getZ()) {
            this.stuckCount++;
            if (this.stuckCount > 50) {
                this.stuckCount = 0;
                this.unstickTimer = 100;
                this.targetInSight = false;
                this.setAttacking(0);
                doNew = 1;
            }
        } else {
            this.stuckCount = 0;
            this.lastX = (int) this.getX();
            this.lastZ = (int) this.getZ();
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

        double ox = 0.0;
        double oy = 0.0;
        double oz = 0.0;
        int hasOwner = 0;
        int tooFar = 0;
        if (this.isOreSpawnTame() && this.getOwner() != null) {
            LivingEntity e = this.getOwner();
            hasOwner = 1;
            ox = e.getX();
            oy = e.getY();
            oz = e.getZ();
            if (this.distanceToSqr(e) > 144.0) {
                tooFar = 1;
                this.targetInSight = false;
                this.setAttacking(0);
                this.flyaway = 0;
                doNew = 1;
            }
        }

        if (this.flyaway > 0) {
            this.flyaway--;
        }

        if (tooFar == 0
                && this.unstickTimer == 0
                && this.flyaway == 0
                && this.level().getDifficulty() != Difficulty.PEACEFUL
                && this.random.nextInt(9) == 1) {
            LivingEntity e = this.findSomethingToAttack();
            if (e != null) {
                if (this.isOreSpawnTame() && this.getHealth() / this.mygetMaxHealth() < 0.25F) {
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
                    this.currentFlightTarget =
                            BlockPos.containing(e.getX(), e.getY() + 1.0, e.getZ());
                    doNew = 0;
                    float reach = 5.0F + e.getBbWidth() / 2.0F;
                    if (this.distanceToSqr(e) < (double) (reach * reach)) {
                        this.doHurtTarget(e);
                        this.flyaway = 5 + this.random.nextInt(10);
                        doNew = 1;
                    } else if (this.distanceToSqr(e) < 256.0
                            && !this.isInWater()
                            && this.getDragonFire() >= 1) {
                        this.shootAtTarget(e);
                    }
                }
            } else {
                this.targetInSight = false;
                this.flyaway = 0;
                this.setAttacking(0);
            }
        }

        if (this.currentFlightTarget != null
                && this.currentFlightTarget.distToCenterSqr(this.getX(), this.getY(), this.getZ())
                        < 2.1F) {
            doNew = 1;
        }

        if ((doNew != 0 && !this.targetInSight) || (doNew != 0 && this.flyaway != 0)) {
            int keepTrying = 50;
            BlockState bid = Blocks.STONE.defaultBlockState();
            while (!bid.isAir() && keepTrying != 0) {
                keepTrying--;
                int gox = (int) this.getX();
                int goy = (int) this.getY();
                int goz = (int) this.getZ();
                int xdir;
                int zdir;
                if (hasOwner == 1 && this.unstickTimer == 0) {
                    gox = (int) ox;
                    goy = (int) oy;
                    goz = (int) oz;
                    if (this.ownerFlying == 0) {
                        zdir = this.random.nextInt(10) + 4;
                        xdir = this.random.nextInt(10) + 4;
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
                this.currentFlightTarget = new BlockPos(
                        gox + xdir,
                        goy + this.random.nextInt(9 + this.ownerFlying * 2) - 4,
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

        double velocity = Math.sqrt(dm.x * dm.x + dm.z * dm.z);
        double obstruction = 0.0;
        int dist = 2 + (int) (velocity * 4.0);
        for (int k = 1; k < dist; k++) {
            for (int i = 1; i < dist * 2; i++) {
                double dx = i * Math.cos(Math.toRadians(this.getYRot() + 90.0F));
                double dz = i * Math.sin(Math.toRadians(this.getYRot() + 90.0F));
                BlockState b = this.level()
                        .getBlockState(BlockPos.containing(this.getX() + dx, this.getY() - k, this.getZ() + dz));
                if (!b.isAir()) {
                    obstruction += 0.05;
                }
            }
        }
        my += obstruction * 0.05;
        this.setPos(this.getX(), this.getY() + obstruction * 0.05, this.getZ());

        double speedFactor = 0.5;
        if (this.currentFlightTarget == null) {
            this.currentFlightTarget = this.blockPosition();
        }
        double var1 = this.currentFlightTarget.getX() + 0.5 - this.getX();
        double var3 = this.currentFlightTarget.getY() + 0.1 - this.getY();
        double var5 = this.currentFlightTarget.getZ() + 0.5 - this.getZ();
        if (this.ownerFlying != 0) {
            speedFactor = 1.75;
            if (this.isOreSpawnTame() && this.getOwner() != null) {
                LivingEntity e = this.getOwner();
                if (this.distanceToSqr(e) > 49.0) {
                    speedFactor = 3.5;
                }
            }
        }

        double mx = dm.x + (Math.signum(var1) - dm.x) * 0.15 * speedFactor;
        my = my + (Math.signum(var3) - my) * 0.21 * speedFactor;
        double mz = dm.z + (Math.signum(var5) - dm.z) * 0.15 * speedFactor;
        float var7 = (float) (Math.atan2(mz, mx) * 180.0 / Math.PI) - 90.0F;
        float var8 = Mth.wrapDegrees(var7 - this.getYRot());
        this.setZza((float) (0.75 * speedFactor));
        this.setYRot(this.getYRot() + var8 / 4.0F);
        this.setDeltaMovement(mx, my, mz);
        this.move(MoverType.SELF, this.getDeltaMovement());
    }

    /**
     * Gold autonomous shoot: type0 SmallFireball / BetterFireball; type1 WaterBall / IceBall
     * (stand-in SmallFireball when Ice/Water not ported).
     */
    private void shootAtTarget(LivingEntity e) {
        double yoff = 1.25;
        double xzoff = 2.25;
        double cx = this.getX() - xzoff * Math.sin(Math.toRadians(this.getYRot()));
        double cz = this.getZ() + xzoff * Math.cos(Math.toRadians(this.getYRot()));
        Vec3 dir = new Vec3(
                e.getX() - cx,
                e.getY() + e.getBbHeight() / 2.0F - (this.getY() + yoff),
                e.getZ() - cz);

        if (this.getDragonType() == 0) {
            if (this.getDragonFire() == 1) {
                SmallFireball sf = new SmallFireball(this.level(), this, dir.normalize());
                sf.setPos(cx, this.getY() + yoff, cz);
                this.playSound(
                        SoundEvents.ARROW_SHOOT,
                        0.75F,
                        1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
                this.level().addFreshEntity(sf);
            } else {
                BetterFireball bf = new BetterFireball(this.level(), this, dir);
                bf.setPos(cx, this.getY() + yoff, cz);
                this.playSound(
                        SoundEvents.TNT_PRIMED,
                        1.0F,
                        1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
                this.level().addFreshEntity(bf);
            }
        } else {
            // gold WaterBall / IceBall deferred — SmallFireball stand-in
            SmallFireball ball = new SmallFireball(this.level(), this, dir.normalize());
            ball.setPos(cx, this.getY() + yoff, cz);
            if (this.getDragonFire() == 1) {
                this.playSound(
                        SoundEvents.ARROW_SHOOT,
                        0.75F,
                        1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
            } else {
                this.playSound(
                        SoundEvents.TNT_PRIMED,
                        1.0F,
                        1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
            }
            this.level().addFreshEntity(ball);
        }
    }

    /** Gold onLivingUpdate boat-style flight when activity!=0 and rider present. */
    private void driveFromRider(Player player) {
        Vec3 dm = this.getDeltaMovement();
        double mx = Mth.clamp(dm.x, -2.0, 2.0);
        double mz = Mth.clamp(dm.z, -2.0, 2.0);
        double my = dm.y;

        double velocity = Math.sqrt(mx * mx + mz * mz);
        double gh = 1.25;
        BlockState below =
                this.level().getBlockState(BlockPos.containing(this.getX(), this.getY() - gh, this.getZ()));
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
            // gold OreSpawnMain.flyup_keystate
            this.playerJumpPending = false;
            my += 0.03;
            my += velocity * 0.036;
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

        // gold rider fireballs via moveStrafing (xxa)
        if (this.fireballTicker == 0) {
            this.riderShoot(player);
        }

        this.setDeltaMovement(mx, my, mz);
        this.move(MoverType.SELF, this.getDeltaMovement());
        Vec3 after = this.getDeltaMovement();
        this.setDeltaMovement(after.x * 0.985, after.y * 0.94, after.z * 0.985);

        AABB box = this.getBoundingBox().inflate(2.25, 2.0, 2.25);
        List<Entity> list =
                this.level().getEntities(this, box, e -> e != player && e.isAlive() && e.isPushable());
        for (Entity listEntity : list) {
            listEntity.push(this);
        }
    }

    /** Gold rider fire/water/ice via player.xxa (strafe). */
    private void riderShoot(Player player) {
        double xzoff = 4.0;
        double yoff = -0.25;
        float strafe = player.xxa;
        if (Math.abs(strafe) < 0.001F) {
            return;
        }

        if (this.getDragonType() == 0) {
            if (strafe > 0.001F) {
                Vec3 aim = aimFromPlayer(player);
                BetterFireball bf = new BetterFireball(this.level(), this, aim);
                bf.setNotMe();
                bf.setSmall();
                double cx = this.getX() - xzoff * Math.sin(Math.toRadians(this.getYRot()));
                double cz = this.getZ() + xzoff * Math.cos(Math.toRadians(this.getYRot()));
                bf.setPos(cx, this.getY() + yoff, cz);
                bf.setDeltaMovement(this.getDeltaMovement());
                this.playSound(
                        SoundEvents.ARROW_SHOOT,
                        0.75F,
                        1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
                this.level().addFreshEntity(bf);
                this.fireballTicker = 10;
            } else if (strafe < -0.001F) {
                Vec3 aim = aimFromPlayer(player);
                BetterFireball bf = new BetterFireball(this.level(), this, aim);
                bf.setNotMe();
                double cx = this.getX() - xzoff * Math.sin(Math.toRadians(this.getYRot()));
                double cz = this.getZ() + xzoff * Math.cos(Math.toRadians(this.getYRot()));
                bf.setPos(cx, this.getY() + yoff, cz);
                bf.setDeltaMovement(this.getDeltaMovement());
                this.playSound(
                        SoundEvents.TNT_PRIMED,
                        1.0F,
                        1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
                this.level().addFreshEntity(bf);
                this.fireballTicker = 20;
            }
        } else {
            // white dragon: WaterBall / IceBall stand-in SmallFireball
            if (strafe > 0.001F || strafe < -0.001F) {
                Vec3 aim = aimFromPlayer(player);
                double cx = this.getX() - xzoff * Math.sin(Math.toRadians(this.getYRot()));
                double cz = this.getZ() + xzoff * Math.cos(Math.toRadians(this.getYRot()));
                SmallFireball ball = new SmallFireball(this.level(), this, aim.normalize());
                ball.setPos(cx, this.getY() + yoff, cz);
                if (strafe > 0.001F) {
                    this.playSound(
                            SoundEvents.ARROW_SHOOT,
                            0.75F,
                            1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
                    this.fireballTicker = 5;
                } else {
                    this.playSound(
                            SoundEvents.FIREWORK_ROCKET_LAUNCH,
                            0.75F,
                            1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
                    this.fireballTicker = 15;
                }
                this.level().addFreshEntity(ball);
            }
        }
    }

    private static Vec3 aimFromPlayer(Player player) {
        double cx = Math.cos(Math.toRadians(player.yHeadRot + 90.0F));
        double cz = Math.sin(Math.toRadians(player.yHeadRot + 90.0F));
        double cy = -Math.sin(Math.toRadians(player.getXRot()));
        return new Vec3(cx, cy, cz);
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
    protected void customServerAiStep() {
        // gold updateAITasks only when no rider
        if (!this.level().isClientSide && !this.isVehicle()) {
            super.customServerAiStep();
        }
    }

    @Override
    public boolean doHurtTarget(Entity target) {
        // gold: 35 dmg, Kraken *2, ks=1.75 inair=0.1
        double ks = 1.75;
        double inair = 0.1;
        float iskraken = 1.0F;
        if (target instanceof LivingEntity living) {
            if (living.getBbHeight() > 8.0F && living.getBbWidth() > 4.0F) {
                iskraken = 2.0F; // gold Kraken
            }
            boolean ret = living.hurt(this.damageSources().mobAttack(this), iskraken * 35.0F);
            float f3 = (float) Math.atan2(target.getZ() - this.getZ(), target.getX() - this.getX());
            if (!target.isAlive() || target instanceof Player) {
                inair *= 2.0;
            }
            target.push(Math.cos(f3) * ks, inair, Math.sin(f3) * ks);
            return ret;
        }
        return true;
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (this.hurtTimer > 0) {
            return false;
        }
        // gold ignore cactus / inFire / onFire / lava / inWall
        if (source == this.damageSources().cactus()
                || source == this.damageSources().inFire()
                || source == this.damageSources().onFire()
                || source == this.damageSources().lava()
                || source == this.damageSources().inWall()) {
            return false;
        }

        if (!this.level().isClientSide) {
            OreSpawnPet.setSitting(this, PET_FLAGS, false);
            this.setActivity(1);
        }

        Entity e = source.getEntity();
        if (e == null) {
            e = source.getDirectEntity();
        }
        if (e instanceof BetterFireball && this.getDragonType() == 0) {
            e.discard();
            return false;
        }
        if (e instanceof SmallFireball && this.getDragonType() == 0) {
            e.discard();
            return false;
        }
        // gold IceBall / WaterBall when type!=0 — not ported
        if (e instanceof Dragon || e instanceof Spyro) {
            return false;
        }

        boolean ret = super.hurt(source, amount);
        this.hurtTimer = 20;
        if (e instanceof LivingEntity living && !this.level().isClientSide) {
            if (this.isOreSpawnTame() && e instanceof Player) {
                return false;
            }
            this.setTarget(living);
            this.setLastHurtByMob(living);
            this.getNavigation().moveTo(living, 1.2);
            ret = true;
        }
        return ret;
    }

    private boolean isSuitableTarget(LivingEntity target) {
        if (this.level().getDifficulty() == Difficulty.PEACEFUL) {
            return false;
        }
        if (target == null || target == this || !target.isAlive()) {
            return false;
        }
        if (!this.hasLineOfSight(target)) {
            return false;
        }
        // gold ignore list
        if (target instanceof LurkingTerror
                || target instanceof EnderReaper
                || target instanceof TerribleTerror
                || target instanceof LeafMonster
                || target instanceof CreepingHorror
                || target instanceof Triffid) {
            return false;
        }
        if (target instanceof Monster) {
            return true;
        }
        if (target instanceof Mothra) {
            return true;
        }
        // gold Kraken — large body stand-in
        if (target.getBbHeight() > 8.0F && target.getBbWidth() > 4.0F) {
            return true;
        }
        // gold never targets players via isSuitableTarget
        return false;
    }

    @Nullable
    private LivingEntity findSomethingToAttack() {
        if (OreSpawnMain.PlayNicely != 0) {
            return null;
        }
        AABB box = this.getBoundingBox().inflate(20.0, 20.0, 20.0);
        List<LivingEntity> list =
                this.level().getEntitiesOfClass(LivingEntity.class, box, this::isSuitableTarget);
        LivingEntity best = null;
        double bestD = Double.MAX_VALUE;
        for (LivingEntity living : list) {
            double d = this.distanceToSqr(living);
            if (d < bestD) {
                bestD = d;
                best = living;
            }
        }
        return best;
    }

    public boolean canSeeTarget(double pX, double pY, double pZ) {
        Vec3 from = new Vec3(this.getX(), this.getY() + 0.75, this.getZ());
        Vec3 to = new Vec3(pX, pY, pZ);
        return this.level()
                        .clip(new ClipContext(
                                from, to, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this))
                        .getType()
                == HitResult.Type.MISS;
    }

    @Override
    public boolean checkSpawnRules(LevelAccessor level, MobSpawnType spawnType) {
        // gold: day only; peer Dragon inflate 16,6,16 none; DimensionID4 always else y>=50
        if (level instanceof Level lvl && !lvl.isDay()) {
            return false;
        }
        List<Dragon> peers = level.getEntitiesOfClass(
                Dragon.class, this.getBoundingBox().inflate(16.0, 6.0, 16.0), e -> e != this);
        if (!peers.isEmpty()) {
            return false;
        }
        // gold DimensionID4 special — if not mining dim, require y >= 50
        return !(this.getY() < 50.0);
    }

    private void playTameEffect(boolean happy) {
        for (int i = 0; i < 20; i++) {
            double d0 = this.random.nextGaussian() * 0.08;
            double d1 = this.random.nextGaussian() * 0.08;
            double d2 = this.random.nextGaussian() * 0.08;
            this.level()
                    .addParticle(
                            happy ? ParticleTypes.HEART : ParticleTypes.SMOKE,
                            this.getX() + (this.random.nextFloat() - this.random.nextFloat()) * 2.5F,
                            this.getY() + 0.5 + this.random.nextFloat() * 1.5,
                            this.getZ() + (this.random.nextFloat() - this.random.nextFloat()) * 2.5F,
                            d0,
                            d1,
                            d2);
        }
    }

    @Override
    public void handleEntityEvent(byte id) {
        if (id == 7) {
            this.playTameEffect(true);
        } else if (id == 6) {
            this.playTameEffect(false);
        } else {
            super.handleEntityEvent(id);
        }
    }

    /**
     * Gold processInteract: beef 1/5 tame; empty mount; beef heal; dead_bush release;
     * snow extinguish fire; flint fire1; gunpowder fire2; snowball white; coal red;
     * diamond → Spyro; name_tag; any item sit toggle.
     */
    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (!this.isOreSpawnTame()) {
            if (!stack.isEmpty() && stack.is(Items.BEEF) && this.distanceToSqr(player) < 25.0) {
                if (!this.level().isClientSide) {
                    // gold nextInt(5)==1 → 20%
                    if (OreSpawnPet.tryTame(this, PET_FLAGS, OWNER, player, 5)) {
                        this.heal(this.mygetMaxHealth() - this.getHealth());
                        this.playTameEffect(true);
                    } else {
                        this.playTameEffect(false);
                    }
                }
                if (!player.getAbilities().instabuild) {
                    stack.shrink(1);
                }
                return InteractionResult.sidedSuccess(this.level().isClientSide);
            }
            return InteractionResult.PASS;
        }

        if (!this.isOwnedBy(player)) {
            return InteractionResult.PASS;
        }

        // empty hand → mount
        if (stack.isEmpty() && this.distanceToSqr(player) < 16.0) {
            if (!this.level().isClientSide) {
                player.startRiding(this);
                this.setActivity(1);
                OreSpawnPet.setSitting(this, PET_FLAGS, false);
            }
            return InteractionResult.sidedSuccess(this.level().isClientSide);
        }

        // beef heal
        if (!stack.isEmpty() && stack.is(Items.BEEF) && this.distanceToSqr(player) < 25.0) {
            if (this.level().isClientSide) {
                this.playTameEffect(true);
            }
            if (this.mygetMaxHealth() > this.getHealth()) {
                this.heal(this.mygetMaxHealth() - this.getHealth());
            }
            if (!player.getAbilities().instabuild) {
                stack.shrink(1);
            }
            return InteractionResult.sidedSuccess(this.level().isClientSide);
        }

        // dead_bush release
        if (!stack.isEmpty() && stack.is(Blocks.DEAD_BUSH.asItem()) && this.distanceToSqr(player) < 25.0) {
            if (!this.level().isClientSide) {
                OreSpawnPet.setTame(this, PET_FLAGS, false);
                OreSpawnPet.setOwnerUUID(this, OWNER, null);
                OreSpawnPet.setSitting(this, PET_FLAGS, false);
                this.level().broadcastEntityEvent(this, (byte) 6);
            }
            this.playTameEffect(false);
            if (!player.getAbilities().instabuild) {
                stack.shrink(1);
            }
            return InteractionResult.sidedSuccess(this.level().isClientSide);
        }

        // snow_block extinguish fire (gold snow)
        if (!stack.isEmpty()
                && stack.is(Blocks.SNOW_BLOCK.asItem())
                && this.distanceToSqr(player) < 25.0
                && this.isOwnedBy(player)) {
            if (!this.level().isClientSide) {
                this.playTameEffect(true);
                this.level().broadcastEntityEvent(this, (byte) 6);
                this.setDragonFire(0);
                player.displayClientMessage(Component.literal("Dragon fireballs extinguished."), true);
            }
            if (!player.getAbilities().instabuild) {
                stack.shrink(1);
            }
            return InteractionResult.sidedSuccess(this.level().isClientSide);
        }

        // flint fireballs lit
        if (!stack.isEmpty()
                && stack.is(Items.FLINT)
                && this.distanceToSqr(player) < 25.0
                && this.isOwnedBy(player)) {
            if (!this.level().isClientSide) {
                this.playTameEffect(true);
                this.level().broadcastEntityEvent(this, (byte) 6);
                this.setDragonFire(1);
                player.displayClientMessage(Component.literal("Dragon fireballs lit!"), true);
            }
            if (!player.getAbilities().instabuild) {
                stack.shrink(1);
            }
            return InteractionResult.sidedSuccess(this.level().isClientSide);
        }

        // gunpowder supercharge
        if (!stack.isEmpty()
                && stack.is(Items.GUNPOWDER)
                && this.distanceToSqr(player) < 25.0
                && this.isOwnedBy(player)
                && this.getDragonFire() > 0) {
            if (!this.level().isClientSide) {
                this.playTameEffect(true);
                this.level().broadcastEntityEvent(this, (byte) 6);
                this.setDragonFire(2);
                player.displayClientMessage(Component.literal("Dragon fireballs supercharged!"), true);
            }
            if (!player.getAbilities().instabuild) {
                stack.shrink(1);
            }
            return InteractionResult.sidedSuccess(this.level().isClientSide);
        }

        // snowball → white dragon
        if (!stack.isEmpty() && stack.is(Items.SNOWBALL) && this.distanceToSqr(player) < 25.0) {
            if (this.level().isClientSide) {
                this.playTameEffect(true);
            }
            this.dragontype = 1;
            this.setDragonType(1);
            if (!player.getAbilities().instabuild) {
                stack.shrink(1);
            }
            return InteractionResult.sidedSuccess(this.level().isClientSide);
        }

        // coal → red/fire dragon
        if (!stack.isEmpty() && stack.is(Items.COAL) && this.distanceToSqr(player) < 25.0) {
            if (this.level().isClientSide) {
                this.playTameEffect(true);
            }
            this.dragontype = 0;
            this.setDragonType(0);
            if (!player.getAbilities().instabuild) {
                stack.shrink(1);
            }
            return InteractionResult.sidedSuccess(this.level().isClientSide);
        }

        // diamond → shrink to Spyro (Baby Dragon)
        if (!stack.isEmpty()
                && stack.is(Items.DIAMOND)
                && this.distanceToSqr(player) < 25.0
                && this.isOwnedBy(player)
                && !this.level().isClientSide) {
            if (this.level() instanceof ServerLevel server) {
                this.spawnSpyroFromDiamond(server, player);
            }
            if (!player.getAbilities().instabuild) {
                stack.shrink(1);
            }
            return InteractionResult.sidedSuccess(this.level().isClientSide);
        }

        // name_tag
        if (!stack.isEmpty()
                && stack.is(Items.NAME_TAG)
                && this.distanceToSqr(player) < 25.0
                && this.isOwnedBy(player)) {
            this.setCustomName(stack.getHoverName());
            if (!player.getAbilities().instabuild) {
                stack.shrink(1);
            }
            return InteractionResult.sidedSuccess(this.level().isClientSide);
        }

        // any item → sit toggle
        if (!stack.isEmpty() && this.distanceToSqr(player) < 25.0 && !this.isVehicle()) {
            if (!this.isOreSpawnSitting()) {
                OreSpawnPet.setSitting(this, PET_FLAGS, true);
                this.setActivity(0);
            } else {
                OreSpawnPet.setSitting(this, PET_FLAGS, false);
                this.setActivity(0);
            }
            return InteractionResult.sidedSuccess(this.level().isClientSide);
        }

        return InteractionResult.PASS;
    }

    /** Gold diamond → Baby Dragon (Spyro), keep owner if tamed. */
    private void spawnSpyroFromDiamond(ServerLevel server, Player player) {
        Spyro baby = ModEntities.SPYRO.get().create(server);
        if (baby == null) {
            return;
        }
        baby.moveTo(this.getX(), this.getY(), this.getZ(), this.getYRot(), 0.0F);
        if (this.isOreSpawnTame()) {
            // transfer OreSpawn pet flags via NBT keys Spyro.readAdditionalSaveData understands
            CompoundTag t = new CompoundTag();
            baby.saveWithoutId(t);
            t.putBoolean("OreSpawnTame", true);
            t.putBoolean("OreSpawnSitting", false);
            t.putUUID("OreSpawnOwner", player.getUUID());
            baby.load(t);
            baby.setPersistenceRequired();
        }
        server.addFreshEntity(baby);
        this.discard();
    }

    @Override
    protected void dropCustomDeathLoot(ServerLevel level, DamageSource damageSource, boolean recentlyHit) {
        super.dropCustomDeathLoot(level, damageSource, recentlyHit);
        // gold: beef 1+r6
        int i = 1 + this.random.nextInt(6);
        for (int n = 0; n < i; n++) {
            this.spawnAtLocation(new ItemStack(Items.BEEF));
        }
    }
}
