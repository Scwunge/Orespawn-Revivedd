package danger.orespawn.entity;

import danger.orespawn.init.ModItems;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.PlayerRideableJumping;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

/**
 * Gold {@code Elevator} (EntityLiving / Hoverboard) 1:1 best-effort for NeoForge 1.21.1.
 * Size 1.25×1.0, health 60, speed 1.33, attack 0. Rideable hover platform with boat-like physics.
 * Color variants 1–10 (Ultimate Sword cycles). Registry size/attrs set in {@code ModEntities}.
 */
public class Elevator extends PathfinderMob implements PlayerRideableJumping {
    private static final EntityDataAccessor<Integer> DATA_TIME_SINCE_HIT =
            SynchedEntityData.defineId(Elevator.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> DATA_FORWARD_DIRECTION =
            SynchedEntityData.defineId(Elevator.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Float> DATA_DAMAGE_TAKEN =
            SynchedEntityData.defineId(Elevator.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Integer> DATA_EXPLODING =
            SynchedEntityData.defineId(Elevator.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> DATA_COLOR =
            SynchedEntityData.defineId(Elevator.class, EntityDataSerializers.INT);
    /** Bird gyro bank (degrees) — client render. */
    private static final EntityDataAccessor<Float> DATA_FLIGHT_ROLL =
            SynchedEntityData.defineId(Elevator.class, EntityDataSerializers.FLOAT);
    /** Bird gyro pitch (degrees, continuous −180.180 for loops) — client render. */
    private static final EntityDataAccessor<Float> DATA_FLIGHT_PITCH =
            SynchedEntityData.defineId(Elevator.class, EntityDataSerializers.FLOAT);
    /** High-speed aerobatic / loop mode — client camera locks to board. */
    private static final EntityDataAccessor<Boolean> DATA_AEROBATIC =
            SynchedEntityData.defineId(Elevator.class, EntityDataSerializers.BOOLEAN);

    public static final float GOLD_WIDTH = 1.25F;
    /**
     * Gold hitbox height 1.0 — kept for physics. Visual deck is a flat 1px model at
     * entity origin; passenger attachment places feet on the deck (see
     * {@link #getPassengerAttachmentPoint}).
     */
    public static final float GOLD_HEIGHT = 1.0F;
    /**
     * Stand-on-deck Y: feet above entity origin so the thin board sits under the soles,
     * not through the ankles. (0.15 was too low; gold mounted offset was 0.5.)
     */
    public static final double PASSENGER_DECK_Y = 0.42;
    public static final double GOLD_HEALTH = 60.0;
    public static final double GOLD_SPEED = 1.33;
    public static final double GOLD_ATTACK = 0.0;
    public static final int GOLD_XP = 0;

    private static final ResourceLocation[] TEXTURES = new ResourceLocation[] {
        ResourceLocation.fromNamespaceAndPath("orespawn", "textures/entity/elevator1.png"),
        ResourceLocation.fromNamespaceAndPath("orespawn", "textures/entity/elevator2.png"),
        ResourceLocation.fromNamespaceAndPath("orespawn", "textures/entity/elevator3.png"),
        ResourceLocation.fromNamespaceAndPath("orespawn", "textures/entity/elevator4.png"),
        ResourceLocation.fromNamespaceAndPath("orespawn", "textures/entity/elevator5.png"),
        ResourceLocation.fromNamespaceAndPath("orespawn", "textures/entity/elevator6.png"),
        ResourceLocation.fromNamespaceAndPath("orespawn", "textures/entity/elevator7.png"),
        ResourceLocation.fromNamespaceAndPath("orespawn", "textures/entity/elevator8.png"),
        ResourceLocation.fromNamespaceAndPath("orespawn", "textures/entity/elevator9.png"),
        ResourceLocation.fromNamespaceAndPath("orespawn", "textures/entity/elevator10.png")
    };

    private int boatPosRotationIncrements;
    private double boatX;
    private double boatY;
    private double boatZ;
    private double boatYaw;
    private double boatPitch;
    private double velocityX;
    private double velocityY;
    private double velocityZ;
    private int explodingLocal = 0;
    private int colorLocal = 1;
    private int playing = 0;
    private boolean playerJumpPending = false;
    /** Cobblemon BirdBehaviour local ride velocity (Charizard-style). */
    private final HoverBirdFlight.State birdFlight = new HoverBirdFlight.State();
    /**
     * Client-only previous-tick bank/pitch for smooth multiplayer interpolation
     * (same pattern as {@code yRotO} / {@code xRotO}).
     */
    private float flightRollO;
    private float flightPitchO;
    /** Client-smoothed render angles — kills packet / look-chase mesh jitter. */
    private float renderPitchSmoothed;
    private float renderRollSmoothed;
    private boolean renderOrientSeeded;

    public Elevator(EntityType<? extends Elevator> type, Level level) {
        super(type, level);
        this.xpReward = GOLD_XP;
        this.setPersistenceRequired(); // gold func_110163_bv
    }

    public static AttributeSupplier.Builder createAttributes() {
        return PathfinderMob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, GOLD_HEALTH)
                .add(Attributes.MOVEMENT_SPEED, GOLD_SPEED)
                .add(Attributes.ATTACK_DAMAGE, GOLD_ATTACK)
                .add(Attributes.FOLLOW_RANGE, 16.0)
                .add(Attributes.STEP_HEIGHT, 1.0);
    }

    @Override
    protected void registerGoals() {
        // gold EntityLiving — no AI goals; motion is custom boat physics
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        // gold: 22 timeSinceHit, 23 forwardDirection, 24 damageTaken, 20 exploding, 21 color
        builder.define(DATA_TIME_SINCE_HIT, 0);
        builder.define(DATA_FORWARD_DIRECTION, 1);
        builder.define(DATA_DAMAGE_TAKEN, 0.0F);
        builder.define(DATA_EXPLODING, 0);
        builder.define(DATA_COLOR, 1);
        builder.define(DATA_FLIGHT_ROLL, 0.0F);
        builder.define(DATA_FLIGHT_PITCH, 0.0F);
        builder.define(DATA_AEROBATIC, false);
    }

    public ResourceLocation getTexture() {
        int c = this.getHoverColor();
        if (c < 1 || c > 10) {
            return TEXTURES[0];
        }
        return TEXTURES[c - 1];
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        // gold canDespawn false
        return false;
    }

    @Override
    public boolean isPushable() {
        // gold canBePushed true
        return true;
    }

    @Override
    public boolean isPickable() {
        // gold canBeCollidedWith: !isDead
        return !this.isRemoved();
    }

    @Override
    public boolean causeFallDamage(float fallDistance, float multiplier, DamageSource source) {
        // gold fall empty
        return false;
    }

    @Override
    protected void checkFallDamage(double y, boolean onGround, BlockState state, BlockPos pos) {
        // gold updateFallState empty
    }

    /** Rider Y offset so feet sit on the flat deck (not mid-hitbox / head). */
    public double getPassengersRidingOffset() {
        return PASSENGER_DECK_Y;
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

    /**
     * Local-space seat: deck under feet at entity origin (player entity origin = feet).
     * Not mid-hitbox — that put the board through the head.
     */
    @Override
    protected Vec3 getPassengerAttachmentPoint(
            Entity entity, net.minecraft.world.entity.EntityDimensions dimensions, float partialTick) {
        return new Vec3(0.0, this.getPassengersRidingOffset(), 0.0);
    }

    /**
     * Hard-glue rider to the deck every tick — gold {@code updateRiderPosition} /
     * Cobblemon {@code positionRider}. Player and hoverboard move as one unit:
     * feet locked to board center, shared velocity, no independent rider drift.
     */
    @Override
    protected void positionRider(Entity passenger, Entity.MoveFunction moveFunction) {
        if (!this.hasPassenger(passenger)) {
            return;
        }
        // Gold: passenger.setPosition(boardX, boardY + mountedYOffset + passengerYOffset, boardZ)
        // 1.21: attachment is deck under feet; subtract passenger VEHICLE attachment (usually 0).
        Vec3 attach = this.getPassengerAttachmentPoint(
                passenger, this.getDimensions(this.getPose()), 1.0F);
        Vec3 vehicleAtt = passenger.getVehicleAttachmentPoint(this);
        moveFunction.accept(
                passenger,
                this.getX() + attach.x - vehicleAtt.x,
                this.getY() + attach.y - vehicleAtt.y,
                this.getZ() + attach.z - vehicleAtt.z);

        // Move as one unit
        passenger.setDeltaMovement(this.getDeltaMovement());
        passenger.fallDistance = 0.0F;
        passenger.setOnGround(false);

        // Body faces board forward; camera freelook stays free for flight aim
        if (passenger instanceof LivingEntity living) {
            living.yBodyRot = this.getYRot();
        }
        if (passenger != this.getControllingPassenger()) {
            passenger.setYRot(this.getYRot());
            passenger.setYHeadRot(this.getYRot());
        }
    }

    /**
     * NeoForge {@code IEntityExtension#shouldRiderSit}: false = standing ride pose
     * (hoverboard is a flat platform, not a seat).
     */
    @Override
    public boolean shouldRiderSit() {
        return false;
    }

    /**
     * Server-authoritative flight for all clients. Returning false means the client
     * only interpolates network packets (no client-side free simulation), so the
     * board cannot drift off the rider. Input is still read from the controlling
     * passenger on the server in {@link #tickServer()}.
     */
    @Override
    public boolean isControlledByLocalInstance() {
        return false;
    }

    @Override
    public void onPassengerTurned(Entity passenger) {
        // Do not snap freelook (needed for bird mouse aim). Body locked in positionRider.
    }

    @Override
    protected void removePassenger(Entity passenger) {
        super.removePassenger(passenger);
        // Restore gravity after dismount (we disable it while glued to the board)
        if (passenger instanceof Player player) {
            player.setNoGravity(false);
        }
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        boolean fromPlayer = source.getEntity() instanceof Player;
        // gold: if rider present and not player damage → ignore
        if (this.isVehicle() && !fromPlayer) {
            return false;
        }
        if (source.getMsgId().equals("inWall")) {
            return false;
        }

        if (!this.level().isClientSide && !this.isRemoved()) {
            this.setForwardDirection(-this.getForwardDirection());
            this.setTimeSinceHit(10);
            this.setDamageTaken(this.getDamageTaken() + amount * 10.0F);
            this.markHurt();
            boolean creative =
                    source.getEntity() instanceof Player player && player.getAbilities().instabuild;
            if (creative || this.getDamageTaken() > 40.0F) {
                this.ejectPassengers();
                // gold drop MyElevator when not creative ModItems.ELEVATOR
                this.discard();
            }
            return true;
        }
        return true;
    }

    @Override
    public void handleEntityEvent(byte id) {
        // gold performHurtAnimation path via boat-style damage data
        if (id == 2) {
            this.setForwardDirection(-this.getForwardDirection());
            this.setTimeSinceHit(10);
            this.setDamageTaken(this.getDamageTaken() * 11.0F);
        }
        super.handleEntityEvent(id);
    }

    @Override
    public void lerpTo(double x, double y, double z, float yRot, float xRot, int steps) {
        // gold setPositionAndRotation2 boat-style
        // Extra steps while ridden = smoother client path (less positional jitter)
        if (this.isVehicle()) {
            this.boatPosRotationIncrements = Math.max(steps + 12, 15);
        } else {
            this.boatPosRotationIncrements = 6;
        }
        this.boatX = x;
        this.boatY = y;
        this.boatZ = z;
        this.boatYaw = yRot;
        this.boatPitch = xRot;
        this.setDeltaMovement(this.velocityX, this.velocityY, this.velocityZ);
    }

    @Override
    public void lerpMotion(double x, double y, double z) {
        this.velocityX = x;
        this.velocityY = y;
        this.velocityZ = z;
        this.setDeltaMovement(x, y, z);
    }

    @Override
    public void tick() {
        // Capture previous bank/pitch before network / physics update this tick
        // so remote clients can lerp smoothly (other players see fluid banking).
        if (this.level().isClientSide) {
            this.flightRollO = this.getFlightRoll();
            this.flightPitchO = this.getFlightPitch();
        }

        if (this.getTimeSinceHit() > 0) {
            this.setTimeSinceHit(this.getTimeSinceHit() - 1);
        }
        if (this.getDamageTaken() > 0.0F) {
            this.setDamageTaken(this.getDamageTaken() - 1.0F);
        }

        super.tick();

        if (this.isRemoved()) {
            return;
        }

        Vec3 dm = this.getDeltaMovement();
        double mx = dm.x;
        double my = dm.y;
        double mz = dm.z;
        double velocity = Math.sqrt(mx * mx + mz * mz);

        this.spawnTrailParticles(velocity, mx, my, mz);
        this.tickHoverSoundsAndExplode(velocity, mx, mz);
    }

    @Override
    public void aiStep() {
        // gold onLivingUpdate: super only when no rider; always extinguish
        if (!this.isVehicle()) {
            super.aiStep();
        }
        this.clearFire();

        if (this.isRemoved() || this.isDeadOrDying()) {
            return;
        }

        if (this.level().isClientSide) {
            this.tickClient();
        } else {
            this.tickServer();
        }
    }

    private void spawnTrailParticles(double velocity, double mx, double my, double mz) {
        if (velocity <= 0.15 || !this.isVehicle()) {
            return;
        }
        double d4 = Math.cos(Math.toRadians(this.getYRot() + 270.0F));
        double d5 = Math.sin(Math.toRadians(this.getYRot() + 270.0F));
        BlockState bid = Blocks.AIR.defaultBlockState();
        int depth = 1;
        for (; depth < 10; depth++) {
            bid = this.level().getBlockState(BlockPos.containing(this.getX(), this.getY() - depth, this.getZ()));
            if (!bid.isAir()) {
                break;
            }
        }
        for (int j = 0; j < 1.0 + velocity * 10.0; j++) {
            double d6 = this.random.nextFloat() * 2.0F - 1.0F;
            double d7 = (this.random.nextInt(2) * 2 - 1) * 0.7;
            if (this.random.nextBoolean()) {
                double d8 = this.getX() - d4 * d6 * 0.8 + d5 * d7;
                double d9 = this.getZ() - d5 * d6 * 0.8 - d4 * d7;
                this.level()
                        .addParticle(
                                this.random.nextBoolean() ? ParticleTypes.SMOKE : ParticleTypes.FIREWORK,
                                d8,
                                this.getY() - 0.25,
                                d9,
                                mx,
                                my,
                                mz);
            } else {
                double d8 = this.getX() + d4 + d5 * d6 * 0.7;
                double d9 = this.getZ() + d5 - d4 * d6 * 0.7;
                this.level()
                        .addParticle(
                                this.random.nextBoolean() ? ParticleTypes.SMOKE : ParticleTypes.FIREWORK,
                                d8,
                                this.getY() - 0.225,
                                d9,
                                mx,
                                my,
                                mz);
            }
            if (bid.is(Blocks.WATER)) {
                for (int k = 0; k < 5; k++) {
                    this.level()
                            .addParticle(
                                    ParticleTypes.SPLASH,
                                    this.getX() + this.random.nextFloat(),
                                    this.getY() - depth + 1.25,
                                    this.getZ() + this.random.nextFloat(),
                                    mx / 2.0,
                                    my + velocity,
                                    mz / 2.0);
                }
            }
        }
    }

    private void tickHoverSoundsAndExplode(double velocity, double mx, double mz) {
        if (this.playing > 0) {
            this.playing--;
        }
        if (this.isVehicle() && this.playing == 0 && this.random.nextInt(80) == 1) {
            // gold orespawn:hover 0.45; stand-in
            this.playSound(SoundEvents.ELYTRA_FLYING, 0.45F, 1.0F);
            this.playing = 55;
        }

        if (!this.level().isClientSide) {
            if (this.explodingLocal > 0) {
                this.explodingLocal--;
            }
            if (this.explodingLocal == 0 && velocity > 0.65 && this.random.nextInt(20000) == 1) {
                this.explodingLocal = 45;
                this.playing = 50;
            }
            this.setExploding(this.explodingLocal);
        } else {
            this.explodingLocal = this.getExploding();
        }

        if (this.getExploding() > 0 && this.isVehicle()) {
            if (this.random.nextInt(10) == 1) {
                this.playSound(SoundEvents.GENERIC_EXPLODE.value(), 0.55F, 0.75F + this.random.nextFloat());
            }
            for (int i = 0; i < 15; i++) {
                this.level()
                        .addParticle(
                                ParticleTypes.EXPLOSION,
                                this.getX() + (this.random.nextFloat() - this.random.nextFloat()) * 4.0F,
                                this.getY() + (this.random.nextFloat() - this.random.nextFloat()) * 4.0F,
                                this.getZ() + (this.random.nextFloat() - this.random.nextFloat()) * 4.0F,
                                mx,
                                0.0,
                                mz);
                this.level()
                        .addParticle(
                                ParticleTypes.EXPLOSION_EMITTER,
                                this.getX() + (this.random.nextFloat() - this.random.nextFloat()) * 2.0F,
                                this.getY() + (this.random.nextFloat() - this.random.nextFloat()) * 2.0F,
                                this.getZ() + (this.random.nextFloat() - this.random.nextFloat()) * 2.0F,
                                mx,
                                0.0,
                                mz);
                this.level()
                        .addParticle(
                                ParticleTypes.SMOKE,
                                this.getX() + (this.random.nextFloat() - this.random.nextFloat()) * 5.0F,
                                this.getY() + (this.random.nextFloat() - this.random.nextFloat()) * 5.0F,
                                this.getZ() + (this.random.nextFloat() - this.random.nextFloat()) * 5.0F,
                                mx,
                                0.0,
                                mz);
                this.level()
                        .addParticle(
                                ParticleTypes.LARGE_SMOKE,
                                this.getX() + (this.random.nextFloat() - this.random.nextFloat()) * 3.0F,
                                this.getY() + (this.random.nextFloat() - this.random.nextFloat()) * 3.0F,
                                this.getZ() + (this.random.nextFloat() - this.random.nextFloat()) * 3.0F,
                                mx,
                                0.0,
                                mz);
            }
        }
    }

    private void tickClient() {
        // ——— Ridden: position from packets; rotation from pilot look / smoothed data ———
        if (this.isVehicle()) {
            if (this.boatPosRotationIncrements > 0) {
                // Position only — interpolating yaw/pitch here fought look-chase and
                // made the board model rock back and forth.
                double d4 = this.getX() + (this.boatX - this.getX()) / this.boatPosRotationIncrements;
                double d5 = this.getY() + (this.boatY - this.getY()) / this.boatPosRotationIncrements;
                double d11 = this.getZ() + (this.boatZ - this.getZ()) / this.boatPosRotationIncrements;
                this.setPos(d4, d5, d11);
                this.boatPosRotationIncrements--;
            }

            Entity pilot = this.getControllingPassenger();
            if (pilot instanceof Player player && player.isLocalPlayer()) {
                // Local pilot: model yaw tracks look instantly (no server-lag rock)
                float ly = player.getYRot();
                this.setYRot(ly);
                this.yRotO = ly;
                this.yBodyRot = ly;
                this.yBodyRotO = ly;
                this.setXRot(player.getXRot() * 0.35F);
                this.xRotO = this.getXRot();
                // Smooth pitch/roll for mesh (A/D bank + look pitch)
                float targetPitch = Mth.clamp(player.getXRot(), -80.0F, 80.0F);
                float targetRoll = Math.abs(player.xxa) > 0.05F
                        ? Mth.clamp(-player.xxa * 40.0F, -55.0F, 55.0F)
                        : this.getFlightRoll();
                if (!this.renderOrientSeeded) {
                    this.renderPitchSmoothed = targetPitch;
                    this.renderRollSmoothed = targetRoll;
                    this.renderOrientSeeded = true;
                } else {
                    this.renderPitchSmoothed = Mth.lerp(0.35F, this.renderPitchSmoothed, targetPitch);
                    this.renderRollSmoothed = Mth.lerp(0.28F, this.renderRollSmoothed, targetRoll);
                }
            } else {
                // Remote: exponential smooth toward synched flight angles
                float tp = this.getFlightPitch();
                float tr = this.getFlightRoll();
                if (!this.renderOrientSeeded) {
                    this.renderPitchSmoothed = tp;
                    this.renderRollSmoothed = tr;
                    this.renderOrientSeeded = true;
                } else {
                    this.renderPitchSmoothed =
                            Mth.rotLerp(0.30F, this.renderPitchSmoothed, tp);
                    this.renderRollSmoothed = Mth.rotLerp(0.30F, this.renderRollSmoothed, tr);
                }
                // Keep body yaw stable with entity yRot (no extra boat yaw lerp)
                this.yBodyRot = this.getYRot();
                this.yBodyRotO = this.yBodyRot;
            }

            this.gluePassengers();
            return;
        }

        this.renderOrientSeeded = false;

        // ——— Unmounted client: gentle hover float (gold idle) ———
        double gh = 0.75;
        BlockState bid =
                this.level().getBlockState(BlockPos.containing(this.getX(), this.getY() - gh, this.getZ()));
        Vec3 dm = this.getDeltaMovement();
        if (!bid.isAir()) {
            this.setDeltaMovement(dm.x, dm.y + 0.06, dm.z);
            this.setPos(this.getX(), this.getY() + 0.07, this.getZ());
            this.boatY += 0.07;
        } else {
            this.setDeltaMovement(dm.x, dm.y - 0.003, dm.z);
        }

        if (this.boatPosRotationIncrements > 0) {
            double d4 = this.getX() + (this.boatX - this.getX()) / this.boatPosRotationIncrements;
            double d5 = this.getY() + (this.boatY - this.getY()) / this.boatPosRotationIncrements;
            double d11 = this.getZ() + (this.boatZ - this.getZ()) / this.boatPosRotationIncrements;
            this.setPos(d4, d5, d11);
            this.setXRot(
                    (float) (this.getXRot() + (this.boatPitch - this.getXRot()) / this.boatPosRotationIncrements));
            double d10 = Mth.wrapDegrees(this.boatYaw - this.getYRot());
            this.setYRot((float) (this.getYRot() + d10 / this.boatPosRotationIncrements));
            this.yBodyRot = this.getYRot();
            this.boatPosRotationIncrements--;
        } else {
            Vec3 dm2 = this.getDeltaMovement();
            this.setPos(this.getX() + dm2.x, this.getY() + dm2.y, this.getZ() + dm2.z);
            this.setDeltaMovement(dm2.x * 0.99, dm2.y * 0.95, dm2.z * 0.99);
        }
    }

    /** Force every passenger onto the deck (feet lock). */
    private void gluePassengers() {
        for (Entity passenger : this.getPassengers()) {
            this.positionRider(passenger, Entity::setPos);
        }
    }

    /**
     * Server physics. When ridden: Cobblemon <b>BirdBehaviour</b> (Charizard
     * {@code air/bird}) — mouse look = flight direction; W/S local-Z thrust;
     * pitch dive/glide; Space/Shift hover when slow; gravity 0; inertia 0.5.
     * See {@link HoverBirdFlight} and {@code BIRD_GYRO_SPEC.md}.
     */
    private void tickServer() {
        Vec3 dm = this.getDeltaMovement();
        double mx = dm.x;
        double my = dm.y;
        double mz = dm.z;
        double velocity = Math.sqrt(mx * mx + mz * mz);

        if (this.getControllingPassenger() instanceof Player pp) {
            // Cobblemon bird: gravity() = 0 while mounted — board + rider
            this.setNoGravity(true);
            pp.setNoGravity(true);
            this.fallDistance = 0.0F;
            pp.fallDistance = 0.0F;

            boolean jump = this.playerJumpPending;
            this.playerJumpPending = false;

            Vec3 motion = HoverBirdFlight.tick(
                    this, pp, this.birdFlight, jump, this.explodingLocal != 0);

            // Terrain skim: slight lift near ground (gold hoverboard feel)
            BlockState under = this.level()
                    .getBlockState(BlockPos.containing(this.getX(), this.getY() - 0.35, this.getZ()));
            if (!under.isAir() && motion.y < 0.02) {
                motion = new Vec3(motion.x, Math.max(motion.y, 0.04), motion.z);
            }

            mx = motion.x;
            my = motion.y;
            mz = motion.z;
            velocity = Math.sqrt(mx * mx + mz * mz);
        } else {
            this.setNoGravity(false);
            this.birdFlight.reset();
            this.setFlightRoll(0.0F);
            this.setFlightPitch(0.0F);
            this.setAerobatic(false);
            // ——— Unmounted: gentle hover float (gold idle) ———
            double gh = 0.75;
            BlockState bid =
                    this.level().getBlockState(BlockPos.containing(this.getX(), this.getY() - gh, this.getZ()));
            if (!bid.isAir()) {
                my += 0.06;
                this.setPos(this.getX(), this.getY() + 0.1, this.getZ());
            } else {
                my -= 0.01;
            }
            mx = 0.0;
            mz = 0.0;
        }

        this.setDeltaMovement(mx, my, mz);
        this.move(MoverType.SELF, this.getDeltaMovement());

        // After physics: hard-lock feet to deck so board + player never separate
        if (this.isVehicle()) {
            this.gluePassengers();
            for (Entity passenger : this.getPassengers()) {
                passenger.setDeltaMovement(this.getDeltaMovement());
                passenger.fallDistance = 0.0F;
                if (passenger instanceof Player p) {
                    p.setNoGravity(true);
                }
            }
        }

        if (this.horizontalCollision && velocity > 0.75 && this.isVehicle()) {
            this.discard();
            int p = this.random.nextInt(10);
            for (int k = 0; k < 6 + p; k++) {
                this.spawnAtLocation(new ItemStack(Items.STICK));
            }
            for (int k = 0; k < 2; k++) {
                this.spawnAtLocation(new ItemStack(Items.DIAMOND));
            }
            return;
        }

        Vec3 after = this.getDeltaMovement();
        // Ridden: light residual drag (local state already has bird z-drag);
        // unmounted: stronger settle
        if (this.isVehicle()) {
            this.setDeltaMovement(after.x * 0.998, after.y * 0.998, after.z * 0.998);
        } else {
            this.setDeltaMovement(after.x * 0.98, after.y * 0.94, after.z * 0.98);
        }

        // gold push nearby collidable (not rider / Girlfriend / Boyfriend)
        AABB box = this.getBoundingBox().inflate(0.25, 0.0, 0.25);
        List<Entity> list = this.level()
                .getEntities(
                        this,
                        box,
                        e -> e != this.getFirstPassenger()
                                && e.isPushable()
                                && !(e instanceof Girlfriend)
                                && !(e instanceof Boyfriend));
        for (Entity entity : list) {
            entity.push(this);
        }

        if (this.isVehicle() && this.getFirstPassenger() != null && !this.getFirstPassenger().isAlive()) {
            this.ejectPassengers();
        }
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
        tag.putInt("HoverColor", this.getHoverColor());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.colorLocal = tag.getInt("HoverColor");
        if (this.colorLocal < 1) {
            this.colorLocal = 1;
        }
        if (this.colorLocal > 10) {
            this.colorLocal = 10;
        }
        this.setHoverColor(this.colorLocal);
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        // gold UltimateSword within 16 → cycle color
        if (!stack.isEmpty()
                && stack.is(ModItems.ULTIMATE_SWORD.get())
                && this.distanceToSqr(player) < 16.0) {
            if (!this.level().isClientSide) {
                this.colorLocal = this.getHoverColor() + 1;
                if (this.colorLocal > 10) {
                    this.colorLocal = 1;
                }
                this.setHoverColor(this.colorLocal);
            }
            return InteractionResult.sidedSuccess(this.level().isClientSide);
        }

        if (this.isVehicle()
                && this.getControllingPassenger() instanceof Player other
                && other != player) {
            return InteractionResult.PASS;
        }

        if (!this.level().isClientSide) {
            player.startRiding(this);
        }
        return InteractionResult.sidedSuccess(this.level().isClientSide);
    }

    public void setDamageTaken(float f) {
        this.entityData.set(DATA_DAMAGE_TAKEN, f);
    }

    public float getDamageTaken() {
        return this.entityData.get(DATA_DAMAGE_TAKEN);
    }

    public void setTimeSinceHit(int par1) {
        this.entityData.set(DATA_TIME_SINCE_HIT, par1);
    }

    public int getTimeSinceHit() {
        return this.entityData.get(DATA_TIME_SINCE_HIT);
    }

    public void setForwardDirection(int par1) {
        this.entityData.set(DATA_FORWARD_DIRECTION, par1);
    }

    public int getForwardDirection() {
        return this.entityData.get(DATA_FORWARD_DIRECTION);
    }

    public void setExploding(int par1) {
        this.entityData.set(DATA_EXPLODING, par1);
    }

    public int getExploding() {
        return this.entityData.get(DATA_EXPLODING);
    }

    public void setHoverColor(int par1) {
        this.entityData.set(DATA_COLOR, par1);
        this.colorLocal = par1;
    }

    /** Gold {@code getColor} hover paint index 1–10. */
    public int getHoverColor() {
        return this.entityData.get(DATA_COLOR);
    }

    /**
     * Cobblemon bird bank/roll (degrees). Synched entity-data — all tracking
     * clients (including other players) receive this for multiplayer bank visuals.
     */
    public void setFlightRoll(float rollDeg) {
        // Fine quantize (0.1°) — coarse steps felt stepped/jittery on clients
        float q = Math.round(rollDeg * 10.0F) / 10.0F;
        if (Math.abs(Mth.wrapDegrees(this.entityData.get(DATA_FLIGHT_ROLL) - q)) > 0.05F) {
            this.entityData.set(DATA_FLIGHT_ROLL, q);
        }
    }

    public float getFlightRoll() {
        return this.entityData.get(DATA_FLIGHT_ROLL);
    }

    /**
     * Render roll. Local pilot uses client-smoothed value (no packet rock);
     * others lerp synched data.
     */
    public float getFlightRoll(float partialTick) {
        if (this.level().isClientSide && this.renderOrientSeeded) {
            return this.renderRollSmoothed;
        }
        return Mth.rotLerp(partialTick, this.flightRollO, this.getFlightRoll());
    }

    /** Cobblemon bird nose pitch (degrees) — synched for multiplayer. */
    public void setFlightPitch(float pitchDeg) {
        float q = Math.round(pitchDeg * 10.0F) / 10.0F;
        if (Math.abs(Mth.wrapDegrees(this.entityData.get(DATA_FLIGHT_PITCH) - q)) > 0.05F) {
            this.entityData.set(DATA_FLIGHT_PITCH, q);
        }
    }

    public float getFlightPitch() {
        return this.entityData.get(DATA_FLIGHT_PITCH);
    }

    /** Render pitch — client-smoothed when available. */
    public float getFlightPitch(float partialTick) {
        if (this.level().isClientSide && this.renderOrientSeeded) {
            return this.renderPitchSmoothed;
        }
        return Mth.rotLerp(partialTick, this.flightPitchO, this.getFlightPitch());
    }

    /**
     * Yaw used by the model renderer. Local pilot: follow look (matches camera).
     * Avoids entityYaw lag that made the board mesh swing opposite the player.
     */
    public float getRenderYaw(float partialTick) {
        if (this.level().isClientSide
                && this.getControllingPassenger() instanceof Player player
                && player.isLocalPlayer()) {
            return player.getYRot();
        }
        return Mth.rotLerp(partialTick, this.yRotO, this.getYRot());
    }

    public void setAerobatic(boolean aero) {
        if (this.entityData.get(DATA_AEROBATIC) != aero) {
            this.entityData.set(DATA_AEROBATIC, aero);
        }
    }

    /** True while high-speed loop / inverted flight is active. */
    public boolean isAerobatic() {
        return this.entityData.get(DATA_AEROBATIC);
    }

    @Override
    protected void dropCustomDeathLoot(ServerLevel level, DamageSource damageSource, boolean recentlyHit) {
        super.dropCustomDeathLoot(level, damageSource, recentlyHit);
        // gold MyElevator drop
        this.spawnAtLocation(new ItemStack(ModItems.ELEVATOR.get()));
    }
}
