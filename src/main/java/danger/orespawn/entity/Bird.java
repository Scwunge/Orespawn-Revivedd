package danger.orespawn.entity;

import danger.orespawn.util.handlers.SoundsHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

/**
 * Gold {@code Bird} (EntityCreature + IAnimals). Size 0.4×0.4, speed 0.1, health 2. Passive flyer.
 * Despawns when sky darken &gt; 7 (night); birdType 0.5 selects texture.
 */
public class Bird extends PathfinderMob {
    private static final EntityDataAccessor<Integer> DATA_BIRD_TYPE =
            SynchedEntityData.defineId(Bird.class, EntityDataSerializers.INT);

    @Nullable
    private BlockPos spawnPosition;
    /** Local mirror of {@link #DATA_BIRD_TYPE} (gold birdType 0.5). */
    public int birdType;

    public Bird(EntityType<? extends Bird> type, Level level) {
        super(type, level);
        if (!level.isClientSide) {
            this.setBirdType(this.random.nextInt(6));
        }
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_BIRD_TYPE, 0);
    }

    public void setBirdType(int type) {
        this.birdType = type;
        this.entityData.set(DATA_BIRD_TYPE, type);
    }

    public int getBirdType() {
        return this.entityData.get(DATA_BIRD_TYPE);
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> key) {
        super.onSyncedDataUpdated(key);
        if (DATA_BIRD_TYPE.equals(key)) {
            this.birdType = this.entityData.get(DATA_BIRD_TYPE);
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

    public static AttributeSupplier.Builder createAttributes() {
        return PathfinderMob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 2.0)
                .add(Attributes.MOVEMENT_SPEED, 0.1)
                .add(Attributes.FOLLOW_RANGE, 16.0);
    }

    @Override
    public void tick() {
        super.tick();
        Vec3 m = this.getDeltaMovement();
        this.setDeltaMovement(m.x, m.y * 0.6F, m.z);
        // gold: World.func_175657_ab() = sky darken (high at night)
        if (!this.level().isClientSide && this.level().getSkyDarken() > 7) {
            this.discard();
        }
    }

    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
        if (this.spawnPosition != null
                && (!this.level().isEmptyBlock(this.spawnPosition) || this.spawnPosition.getY() < 1)) {
            this.spawnPosition = null;
        }
        if (this.spawnPosition == null
                || this.random.nextInt(30) == 0
                || this.spawnPosition.distToCenterSqr(this.getX(), this.getY(), this.getZ()) < 4.0) {
            this.spawnPosition = BlockPos.containing(
                    this.getX() + this.random.nextInt(7) - this.random.nextInt(7),
                    this.getY() + this.random.nextInt(6) - 2,
                    this.getZ() + this.random.nextInt(7) - this.random.nextInt(7));
        }
        double d0 = this.spawnPosition.getX() + 0.5 - this.getX();
        double d1 = this.spawnPosition.getY() + 0.1 - this.getY();
        double d2 = this.spawnPosition.getZ() + 0.5 - this.getZ();
        Vec3 m = this.getDeltaMovement();
        this.setDeltaMovement(
                m.x + (Math.signum(d0) * 0.5 - m.x) * 0.1F,
                m.y + (Math.signum(d1) * 0.7F - m.y) * 0.1F,
                m.z + (Math.signum(d2) * 0.5 - m.z) * 0.1F);
        float f = (float) (Mth.atan2(this.getDeltaMovement().z, this.getDeltaMovement().x) * (180.0 / Math.PI)) - 90.0F;
        float f1 = Mth.wrapDegrees(f - this.getYRot());
        this.setYRot(this.getYRot() + f1);
        this.setZza(0.5F);
    }

    @Override
    public boolean causeFallDamage(float fallDistance, float multiplier, DamageSource source) {
        return false;
    }

    @Override
    protected void checkFallDamage(double y, boolean onGround, BlockState state, BlockPos pos) {}

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
        // gold: 1/4 chance to pick birds1.23; else null
        if (this.random.nextInt(4) != 0) {
            return null;
        }
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

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        //  gold: no hurt override (null)
        return null;
    }

    @Override
    protected SoundEvent getDeathSound() {
        //  gold: no death override (null)
        return null;
    }
}
