package danger.orespawn.entity;

import danger.orespawn.entity.tame.OreSpawnPet;
import danger.orespawn.util.ai.FollowOwnerGoal;
import danger.orespawn.util.ai.WanderALotGoal;
import danger.orespawn.util.handlers.SoundsHandler;
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
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.PlayerRideableJumping;
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
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

/**
 * Gold {@code Ostrich} (EntityCannonFodder / EntityTameable) for NeoForge 1.21.1.
 * Size 0.85×2.1, speed 0.38, health 25, attack 6, XP 10, follow 100.
 * Tame food: apple (50%). Rideable (empty-hand mount). Sit toggle with any item when owned.
 * Cannon-fodder hat activation stubbed for model parity ({@code get_is_activated}).
 * Registry size set in {@code ModEntities.OSTRICH} .
 */
public class Ostrich extends Animal implements PlayerRideableJumping {
    private static final EntityDataAccessor<Byte> PET_FLAGS =
            SynchedEntityData.defineId(Ostrich.class, EntityDataSerializers.BYTE);
    private static final EntityDataAccessor<Optional<UUID>> OWNER =
            SynchedEntityData.defineId(Ostrich.class, EntityDataSerializers.OPTIONAL_UUID);
    /** Gold EntityCannonFodder datawatcher 20. */
    private static final EntityDataAccessor<Integer> IS_ACTIVATED =
            SynchedEntityData.defineId(Ostrich.class, EntityDataSerializers.INT);
    /** Gold EntityCannonFodder datawatcher 21. */
    private static final EntityDataAccessor<Integer> HAT_COLOR =
            SynchedEntityData.defineId(Ostrich.class, EntityDataSerializers.INT);

    private final float moveSpeed = 0.38F;
    private RenderInfo renderdata = new RenderInfo();
    /** Gold rider accel smoother. */
    private float deltasmooth = 0.0F;
    /** Gold jump cooldown when flyup/jump held. */
    private int didjump = 0;
    private boolean playerJumpPending;

    public Ostrich(EntityType<? extends Ostrich> type, Level level) {
        super(type, level);
        this.xpReward = 10;
        this.renderdata = new RenderInfo();
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Animal.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 25.0) // mygetMaxHealth()
                .add(Attributes.MOVEMENT_SPEED, 0.38)
                .add(Attributes.ATTACK_DAMAGE, 6.0)
                .add(Attributes.FOLLOW_RANGE, 100.0); // gold field_70174_ab = 100
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

    /** Gold EntityCannonFodder {@code get_is_activated}. */
    public int get_is_activated() {
        return this.entityData.get(IS_ACTIVATED);
    }

    public void set_is_activated(int v) {
        this.entityData.set(IS_ACTIVATED, v);
    }

    /** Gold EntityCannonFodder {@code getHatColor}. */
    public int getHatColor() {
        return this.entityData.get(HAT_COLOR);
    }

    public void setHatColor(int v) {
        this.entityData.set(HAT_COLOR, v);
    }

    public boolean isOreSpawnTame() {
        return OreSpawnPet.isTame(this, PET_FLAGS);
    }

    public boolean isOreSpawnSitting() {
        return OreSpawnPet.isSitting(this, PET_FLAGS);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(PET_FLAGS, (byte) 0);
        builder.define(OWNER, Optional.empty());
        builder.define(IS_ACTIVATED, 0);
        builder.define(HAT_COLOR, 0);
    }

    @Override
    protected void registerGoals() {
        // gold: Swimming, Mate, FollowOwner, Avoid Mob, Tempt apple, Panic,
        // WatchClosest player/living, Wander, LookIdle, MoveIndoors (skip POI)
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
        this.goalSelector.addGoal(3, new AvoidEntityGoal<>(this, Monster.class, 8.0F, 1.0, 1.9F));
        this.goalSelector.addGoal(4, new TemptGoal(this, 1.2, Ingredient.of(Items.APPLE), false));
        this.goalSelector.addGoal(5, new PanicGoal(this, 1.5));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, LivingEntity.class, 5.0F));
        this.goalSelector.addGoal(8, new WanderALotGoal(this, 10, 1.0));
        this.goalSelector.addGoal(9, new RandomLookAroundGoal(this));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        OreSpawnPet.addAdditionalSaveData(this, PET_FLAGS, OWNER, tag);
        tag.putInt("IsActivated", this.get_is_activated());
        tag.putInt("HatColor", this.getHatColor());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        OreSpawnPet.readAdditionalSaveData(this, PET_FLAGS, OWNER, tag);
        this.set_is_activated(tag.getInt("IsActivated"));
        this.setHatColor(tag.getInt("HatColor"));
        if (OreSpawnPet.isTame(this, PET_FLAGS)) {
            this.setPersistenceRequired();
        }
    }

    @Override
    public void tick() {
        if (this.getAttribute(Attributes.MOVEMENT_SPEED) != null) {
            this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(this.moveSpeed);
        }
        if (OreSpawnPet.isSitting(this, PET_FLAGS) && !this.isVehicle()) {
            this.getNavigation().stop();
        }
        super.tick();
        if (this.didjump > 0) {
            this.didjump--;
        }
    }

    @Override
    protected void customServerAiStep() {
        // gold updateAITasks: when no rider
        if (!this.isDeadOrDying() && !this.isVehicle()) {
            if (this.random.nextInt(200) == 1) {
                this.setLastHurtByMob(null);
            }
            if (this.random.nextInt(250) == 0) {
                this.heal(1.0F);
            }
        }
        super.customServerAiStep();
    }

    public int mygetMaxHealth() {
        return 25;
    }

    /** Gold: ignore cactus; still apply other damage. */
    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (source.is(DamageTypes.CACTUS)) {
            return false;
        }
        return super.hurt(source, amount);
    }

    /** Gold empty fall handlers — no fall damage. */
    @Override
    protected void checkFallDamage(double y, boolean onGround, BlockState state, BlockPos pos) {
        if (onGround) {
            this.resetFallDistance();
        }
    }

    @Override
    public boolean causeFallDamage(float fallDistance, float multiplier, DamageSource source) {
        // gold: no fall damage
        return false;
    }

    /** Gold jump: +0.25 y velocity. */
    @Override
    public void jumpFromGround() {
        Vec3 v = this.getDeltaMovement();
        this.setDeltaMovement(v.x, v.y + 0.25, v.z);
        super.jumpFromGround();
    }

    // --- riding (gold livingUpdate boat physics, simplified for 1.21) ---

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
    protected Vec3 getPassengerAttachmentPoint(Entity entity, net.minecraft.world.entity.EntityDimensions dimensions, float partialTick) {
        // gold mountedYOffset 1.4; lateral f=-0.15 along yaw
        float f = -0.15F;
        double yaw = Math.toRadians(this.getYRot());
        return new Vec3(-f * Math.sin(yaw), 1.4, f * Math.cos(yaw));
    }

    @Override
    public boolean isControlledByLocalInstance() {
        return this.getControllingPassenger() instanceof Player || super.isControlledByLocalInstance();
    }

    @Override
    public void travel(Vec3 travelVector) {
        if (!this.isAlive()) {
            super.travel(travelVector);
            return;
        }
        if (this.isVehicle() && this.getControllingPassenger() instanceof Player player) {
            this.getNavigation().stop();

            // gold: clamp horizontal velocities
            Vec3 dm = this.getDeltaMovement();
            double mx = Mth.clamp(dm.x, -2.0, 2.0);
            double mz = Mth.clamp(dm.z, -2.0, 2.0);
            double my = dm.y;

            double velocity = Math.sqrt(mx * mx + mz * mz);

            // gold obstruction climb: blocks ahead raise y slightly
            double obstruction = 0.0;
            int dist = 1 + (int) (velocity * 10.0);
            for (int k = 0; k < dist; k++) {
                for (int i = 1; i < dist * 2; i++) {
                    double dx = i * Math.cos(Math.toRadians(this.getYRot() + 90.0F));
                    double dz = i * Math.sin(Math.toRadians(this.getYRot() + 90.0F));
                    BlockPos bp = BlockPos.containing(this.getX() + dx, this.getY() - 1.0 + k, this.getZ() + dz);
                    if (!this.level().getBlockState(bp).isAir()) {
                        obstruction += 0.075;
                    }
                }
            }
            my += obstruction;
            if (my > 4.0) {
                my = 4.0;
            }
            if (obstruction > 0.0) {
                this.setPos(this.getX(), this.getY() + obstruction, this.getZ());
            }

            // gold: relative yaw toward rider look
            double riderYaw = player.getYRot() % 360.0;
            while (riderYaw < 0.0) {
                riderYaw += 360.0;
            }
            double bodyYaw = this.getYRot() % 360.0;
            while (bodyYaw < 0.0) {
                bodyYaw += 360.0;
            }
            double relativeG = (riderYaw - bodyYaw) % 180.0;
            while (relativeG < 0.0) {
                relativeG += 180.0;
            }
            if (relativeG > 90.0) {
                relativeG -= 180.0;
            }
            if (velocity > 0.01) {
                double d4 = 1.85 - velocity;
                d4 = Math.abs(d4);
                if (d4 < 0.01) {
                    d4 = 0.01;
                }
                if (d4 > 0.9) {
                    d4 = 0.9;
                }
                this.setYRot(player.getYRot() + (float) (relativeG * d4));
            } else {
                this.setYRot(player.getYRot());
            }
            this.setXRot(2.0F * (float) velocity);
            this.yRotO = this.getYRot();
            this.yBodyRot = this.getYRot();
            this.yHeadRot = this.getYRot();

            // gold jump: flyup_keystate → PlayerRideableJumping onPlayerJump
            if (this.playerJumpPending) {
                this.playerJumpPending = false;
                if (this.didjump == 0) {
                    my += 1.0;
                    my += velocity * 6.0;
                    this.didjump = 20;
                }
            }

            float im = player.zza;
            double maxSpeed = 0.75;
            double newVelocity = velocity;
            double deltav = 0.0;
            if (Math.abs(im) > 0.001F) {
                if (im > 0.0F) {
                    deltav = 0.045;
                    if (this.deltasmooth < 0.0F) {
                        this.deltasmooth = 0.0F;
                    }
                    this.deltasmooth = (float) (this.deltasmooth + deltav / 10.0);
                    if (this.deltasmooth > deltav) {
                        this.deltasmooth = (float) deltav;
                    }
                } else {
                    maxSpeed = 0.25;
                    deltav = -0.03;
                    if (this.deltasmooth > 0.0F) {
                        this.deltasmooth = 0.0F;
                    }
                    this.deltasmooth = (float) (this.deltasmooth + deltav / 10.0);
                    if (this.deltasmooth < deltav) {
                        this.deltasmooth = (float) deltav;
                    }
                }
                newVelocity += this.deltasmooth;
            }

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

            // gold friction / gravity after move
            this.setDeltaMovement(mx, my, mz);
            this.move(net.minecraft.world.entity.MoverType.SELF, this.getDeltaMovement());
            Vec3 after = this.getDeltaMovement();
            this.setDeltaMovement(after.x * 0.95, (after.y - 0.25) * 0.85, after.z * 0.95);
            this.calculateEntityAnimation(false);
            return;
        }
        super.travel(travelVector);
    }

    // PlayerRideableJumping
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

        // gold: hat activation — leather helmet color 1, golden helmet color 3
        if (!stack.isEmpty()
                && this.distanceToSqr(player) < 16.0
                && (stack.is(Items.LEATHER_HELMET) || stack.is(Items.GOLDEN_HELMET))) {
            if (!this.level().isClientSide) {
                this.setHatColor(stack.is(Items.GOLDEN_HELMET) ? 3 : 1);
                if (this.get_is_activated() == 0) {
                    this.set_is_activated(1);
                }
                OreSpawnPet.setTame(this, PET_FLAGS, true);
                OreSpawnPet.setOwnerUUID(this, OWNER, player.getUUID());
                OreSpawnPet.setSitting(this, PET_FLAGS, false);
                this.setPersistenceRequired();
                this.heal(this.mygetMaxHealth() - this.getHealth());
                this.level().broadcastEntityEvent(this, (byte) 7);
            }
            if (!player.getAbilities().instabuild) {
                stack.shrink(1);
            }
            return InteractionResult.SUCCESS;
        }

        // gold: any item when tamed+owned toggles sit (only on sand/gravel/dirt/farmland/grass)
        if (!stack.isEmpty()
                && OreSpawnPet.isTame(this, PET_FLAGS)
                && OreSpawnPet.isOwnedBy(this, OWNER, player)
                && this.distanceToSqr(player) < 16.0) {
            if (!this.level().isClientSide) {
                if (!OreSpawnPet.isSitting(this, PET_FLAGS)) {
                    BlockState below =
                            this.level().getBlockState(BlockPos.containing(this.getX(), this.getY() - 1.0, this.getZ()));
                    if (below.is(Blocks.SAND)
                            || below.is(Blocks.RED_SAND)
                            || below.is(Blocks.GRAVEL)
                            || below.is(Blocks.DIRT)
                            || below.is(Blocks.COARSE_DIRT)
                            || below.is(Blocks.FARMLAND)
                            || below.is(Blocks.GRASS_BLOCK)) {
                        OreSpawnPet.setSitting(this, PET_FLAGS, true);
                    }
                } else {
                    OreSpawnPet.setSitting(this, PET_FLAGS, false);
                }
            }
            return InteractionResult.SUCCESS;
        }

        // gold: empty hand mount (always, within 16)
        if (stack.isEmpty() && this.distanceToSqr(player) < 16.0) {
            if (!this.level().isClientSide) {
                player.startRiding(this);
                OreSpawnPet.setSitting(this, PET_FLAGS, false);
            }
            return InteractionResult.SUCCESS;
        }

        return super.mobInteract(player, hand);
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
    protected SoundEvent getAmbientSound() {
        //  gold always null
        return null;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        // gold: orespawn:cryo_hurt
        return SoundsHandler.ENTITY_CRYO_HURT.get();
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
        // gold: tamed → poppies (2 + 0.4); untamed → feather
        if (OreSpawnPet.isTame(this, PET_FLAGS)) {
            int count = 2 + this.random.nextInt(5);
            for (int i = 0; i < count; i++) {
                this.spawnAtLocation(new ItemStack(Blocks.POPPY));
            }
        } else {
            this.spawnAtLocation(new ItemStack(Items.FEATHER));
        }
    }

    /**
     * Gold {@code getCanSpawnHere}: y ≥ 50, daytime, 1/4 chance, no other Ostrich in 16×6×16.
     */
    @Override
    public boolean checkSpawnRules(LevelAccessor level, MobSpawnType spawnType) {
        if (this.getY() < 50.0) {
            return false;
        }
        if (level instanceof Level lvl && !lvl.isDay()) {
            return false;
        }
        if (this.random.nextInt(4) != 1) {
            return false;
        }
        List<Ostrich> near =
                level.getEntitiesOfClass(Ostrich.class, this.getBoundingBox().inflate(16.0, 6.0, 16.0));
        for (Ostrich o : near) {
            if (o != this) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        // gold: baby → setNoDespawn + false; rider → false; tamed → false; else !isNoDespawnRequired
        if (this.isBaby()) {
            this.setPersistenceRequired();
            return false;
        }
        if (this.isVehicle()) {
            return false;
        }
        if (OreSpawnPet.isTame(this, PET_FLAGS)) {
            return false;
        }
        return !this.isPersistenceRequired();
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
