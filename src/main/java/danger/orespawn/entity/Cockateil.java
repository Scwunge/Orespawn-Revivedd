package danger.orespawn.entity;

import danger.orespawn.init.ModItems;
import danger.orespawn.util.handlers.SoundsHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

/**
 * Gold {@code Cockateil} (EntityAnimal). Size 0.5×0.5, speed 0.33, health 2, attack 1, XP 2,
 * follow range 2. Passive flyer with birdtype 0.5 → bird1.bird6 textures.
 * Registry size set in {@code ModEntities}.
 */
public class Cockateil extends Animal {
    private static final EntityDataAccessor<Integer> DATA_BIRD_TYPE =
            SynchedEntityData.defineId(Cockateil.class, EntityDataSerializers.INT);

    @Nullable
    private BlockPos currentFlightTarget;
    /** Local mirror of synched bird type (gold birdtype 0.5). */
    public int birdtype;
    private boolean killedByPlayer = false;
    private int stuck_count = 0;
    private int lastX = 0;
    private int lastZ = 0;
    private int flyup = 0;

    public Cockateil(EntityType<? extends Cockateil> type, Level level) {
        super(type, level);
        this.xpReward = 2;
        if (!level.isClientSide) {
            this.setBirdType(this.random.nextInt(6));
        }
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Animal.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 2.0) // mygetMaxHealth()
                .add(Attributes.MOVEMENT_SPEED, 0.33)
                .add(Attributes.ATTACK_DAMAGE, 1.0)
                .add(Attributes.FOLLOW_RANGE, 2.0); // gold field_70174_ab = 2
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_BIRD_TYPE, 0);
    }

    public void setBirdType(int par1) {
        this.birdtype = par1;
        this.entityData.set(DATA_BIRD_TYPE, par1);
    }

    public int getBirdType() {
        return this.entityData.get(DATA_BIRD_TYPE);
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> key) {
        super.onSyncedDataUpdated(key);
        if (DATA_BIRD_TYPE.equals(key)) {
            this.birdtype = this.entityData.get(DATA_BIRD_TYPE);
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("BirdType", this.getBirdType());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.contains("BirdType")) {
            this.setBirdType(tag.getInt("BirdType"));
        }
    }

    public int mygetMaxHealth() {
        return 2;
    }

    public void setFlyUp() {
        this.flyup = 2;
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        // gold: !isNoDespawnRequired
        return !this.isPersistenceRequired();
    }

    @Override
    protected float getSoundVolume() {
        return 0.55F;
    }

    @Override
    public float getVoicePitch() {
        return 1.0F;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        // gold: daytime + not raining → orespawn:birds
        if (this.level().isDay() && !this.level().isRaining()) {
            return switch (this.random.nextInt(23) + 1) {
                case 1 -> SoundsHandler.ENTITY_BIRD_BIRD1.get();
                case 2 -> SoundsHandler.ENTITY_BIRD_BIRD2.get();
                case 3 -> SoundsHandler.ENTITY_BIRD_BIRD3.get();
                case 4 -> SoundsHandler.ENTITY_BIRD_BIRD4.get();
                case 5 -> SoundsHandler.ENTITY_BIRD_BIRD5.get();
                case 6 -> SoundsHandler.ENTITY_BIRD_BIRD6.get();
                case 7 -> SoundsHandler.ENTITY_BIRD_BIRD7.get();
                case 8 -> SoundsHandler.ENTITY_BIRD_BIRD8.get();
                case 9 -> SoundsHandler.ENTITY_BIRD_BIRD9.get();
                case 10 -> SoundsHandler.ENTITY_BIRD_BIRD10.get();
                case 11 -> SoundsHandler.ENTITY_BIRD_BIRD11.get();
                case 12 -> SoundsHandler.ENTITY_BIRD_BIRD12.get();
                case 13 -> SoundsHandler.ENTITY_BIRD_BIRD13.get();
                case 14 -> SoundsHandler.ENTITY_BIRD_BIRD14.get();
                case 15 -> SoundsHandler.ENTITY_BIRD_BIRD15.get();
                case 16 -> SoundsHandler.ENTITY_BIRD_BIRD16.get();
                case 17 -> SoundsHandler.ENTITY_BIRD_BIRD17.get();
                case 18 -> SoundsHandler.ENTITY_BIRD_BIRD18.get();
                case 19 -> SoundsHandler.ENTITY_BIRD_BIRD19.get();
                case 20 -> SoundsHandler.ENTITY_BIRD_BIRD20.get();
                case 21 -> SoundsHandler.ENTITY_BIRD_BIRD21.get();
                case 22 -> SoundsHandler.ENTITY_BIRD_BIRD22.get();
                default -> SoundsHandler.ENTITY_BIRD_BIRD23.get();
            };
        }
        return null;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundsHandler.ENTITY_DUCK_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundsHandler.ENTITY_DUCK_HURT.get();
    }

    @Override
    public boolean isPushable() {
        // gold canBePushed true
        return true;
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        Entity e = source.getEntity();
        if (e instanceof Player) {
            this.killedByPlayer = true;
        }
        return super.hurt(source, amount);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.currentFlightTarget == null) {
            this.currentFlightTarget = BlockPos.containing(this.getX(), this.getY(), this.getZ());
        }
        // gold: damp y velocity relative to flight target height
        Vec3 m = this.getDeltaMovement();
        if (this.getY() < this.currentFlightTarget.getY()) {
            this.setDeltaMovement(m.x, m.y * 0.7, m.z);
        } else {
            this.setDeltaMovement(m.x, m.y * 0.5, m.z);
        }
    }

    private boolean canSeeTarget(double pX, double pY, double pZ) {
        return this.level()
                        .clip(new ClipContext(
                                new Vec3(this.getX(), this.getY() + 0.75, this.getZ()),
                                new Vec3(pX, pY, pZ),
                                ClipContext.Block.COLLIDER,
                                ClipContext.Fluid.NONE,
                                this))
                        .getType()
                == HitResult.Type.MISS;
    }

    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
        if (this.isDeadOrDying()) {
            return;
        }

        int stayup = 0;
        // gold: DimensionID4 stayup = 2 — dim-4 constant deferred; stayup remains 0

        if (this.lastX == (int) this.getX() && this.lastZ == (int) this.getZ()) {
            this.stuck_count++;
        } else {
            this.stuck_count = 0;
            this.lastX = (int) this.getX();
            this.lastZ = (int) this.getZ();
        }

        if (this.currentFlightTarget == null) {
            this.currentFlightTarget = BlockPos.containing(this.getX(), this.getY(), this.getZ());
        }

        if (this.stuck_count > 40
                || this.random.nextInt(250) == 0
                || this.currentFlightTarget.distToCenterSqr(this.getX(), this.getY(), this.getZ()) < 4.1F) {
            BlockState bid = Blocks.STONE.defaultBlockState();
            int keep_trying = 35;
            this.stuck_count = 0;
            while (!bid.isAir() && keep_trying != 0) {
                keep_trying--;
                int zdir = this.random.nextInt(8) + 5 - this.flyup * 2;
                int xdir = this.random.nextInt(8) + 5 - this.flyup * 2;
                if (this.random.nextInt(2) == 0) {
                    zdir = -zdir;
                }
                if (this.random.nextInt(2) == 0) {
                    xdir = -xdir;
                }
                this.currentFlightTarget = BlockPos.containing(
                        this.getX() + xdir,
                        this.getY() + this.random.nextInt(9 + stayup) - 5 + this.flyup,
                        this.getZ() + zdir);
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

        double var1 = this.currentFlightTarget.getX() + 0.3 - this.getX();
        double var3 = this.currentFlightTarget.getY() + 0.1 - this.getY();
        double var5 = this.currentFlightTarget.getZ() + 0.3 - this.getZ();
        Vec3 m = this.getDeltaMovement();
        this.setDeltaMovement(
                m.x + (Math.signum(var1) * 0.3 - m.x) * 0.25,
                m.y + (Math.signum(var3) * 0.699999 - m.y) * 0.200000001,
                m.z + (Math.signum(var5) * 0.3 - m.z) * 0.25);
        float var7 =
                (float) (Math.atan2(this.getDeltaMovement().z, this.getDeltaMovement().x) * 180.0 / Math.PI) - 90.0F;
        float var8 = Mth.wrapDegrees(var7 - this.getYRot());
        this.setZza(0.8F);
        this.setYRot(this.getYRot() + var8 / 3.0F);
    }

    @Override
    public boolean causeFallDamage(float fallDistance, float multiplier, DamageSource source) {
        return false;
    }

    @Override
    protected void checkFallDamage(double y, boolean onGround, BlockState state, BlockPos pos) {}

    @Override
    public boolean isIgnoringBlockTriggers() {
        // gold func_145773_az = false
        return false;
    }

    /** Gold {@code getCanSpawnHere}: daytime; y ≥ 50 (dim4 always-spawn deferred). */
    @Override
    public boolean checkSpawnRules(LevelAccessor level, MobSpawnType spawnType) {
        if (level instanceof Level lvl && !lvl.isDay()) {
            return false;
        }
        return !(this.getY() < 50.0);
    }

    @Override
    protected void dropCustomDeathLoot(ServerLevel level, DamageSource damageSource, boolean recentlyHit) {
        super.dropCustomDeathLoot(level, damageSource, recentlyHit);
        // gold: birdtype==5 && killedByPlayer && 1/3 → MyRuby else feather
        this.birdtype = this.getBirdType();
        if (this.birdtype == 5 && this.killedByPlayer && this.random.nextInt(3) == 1) {
            this.spawnAtLocation(new ItemStack(ModItems.RUBY.get()));
        } else {
            this.spawnAtLocation(new ItemStack(Items.FEATHER));
        }
    }

    @Override
    public boolean isFood(ItemStack stack) {
        // gold createChild null — not breedable
        return false;
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob other) {
        //  gold createChild: null
        return null;
    }
}
