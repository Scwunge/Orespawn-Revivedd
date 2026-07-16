package danger.orespawn.entity;

import danger.orespawn.init.ModBlocks;
import danger.orespawn.init.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

/**
 * Gold {@code Moth} (EntityCreature + IAnimals). Size 0.5×0.5, speed 0.1, health 1, attack 0.
 * Night flyer; attracted to torches / extreme_torch; spawn air + night + y≥50.
 */
public class Moth extends PathfinderMob {
    private static final EntityDataAccessor<Integer> DATA_MOTH_TYPE =
            SynchedEntityData.defineId(Moth.class, EntityDataSerializers.INT);

    @Nullable
    private BlockPos currentFlightTarget;
    /** Local mirror of {@link #DATA_MOTH_TYPE} (gold moth_type 0.3). */
    public int mothType;
    private int closest = 99999;
    private int tx;
    private int ty;
    private int tz;

    public Moth(EntityType<? extends Moth> type, Level level) {
        super(type, level);
        if (!level.isClientSide) {
            this.setMothType(this.random.nextInt(4));
        }
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_MOTH_TYPE, 0);
    }

    public void setMothType(int type) {
        this.mothType = type;
        this.entityData.set(DATA_MOTH_TYPE, type);
    }

    public int getMothType() {
        return this.entityData.get(DATA_MOTH_TYPE);
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> key) {
        super.onSyncedDataUpdated(key);
        if (DATA_MOTH_TYPE.equals(key)) {
            this.mothType = this.entityData.get(DATA_MOTH_TYPE);
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("MothType", this.getMothType());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.contains("MothType")) {
            this.setMothType(tag.getInt("MothType"));
        }
    }

    public static AttributeSupplier.Builder createAttributes() {
        return PathfinderMob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 1.0)
                .add(Attributes.MOVEMENT_SPEED, 0.1)
                .add(Attributes.ATTACK_DAMAGE, 0.0)
                .add(Attributes.FOLLOW_RANGE, 16.0);
    }

    /** Gold {@code collideWithEntity} empty. */
    @Override
    protected void doPush(Entity entity) {}

    @Override
    public void tick() {
        super.tick();
        // gold: motionY *= 0.6
        Vec3 m = this.getDeltaMovement();
        this.setDeltaMovement(m.x, m.y * 0.6, m.z);
    }

    private boolean isLightSource(BlockState state) {
        // gold: Blocks.TORCH + ModBlocks.EXTREME_TORCH (wall/soul = modern torch family)
        return state.is(Blocks.TORCH)
                || state.is(Blocks.WALL_TORCH)
                || state.is(Blocks.SOUL_TORCH)
                || state.is(Blocks.SOUL_WALL_TORCH)
                || state.is(ModBlocks.EXTREME_TORCH.get());
    }

    /** Gold {@code scan_it} — shell search for nearest torch / extreme_torch. */
    private boolean scanIt(int x, int y, int z, int dx, int dy, int dz) {
        int found = 0;
        for (int i = -dy; i <= dy; i++) {
            for (int j = -dz; j <= dz; j++) {
                BlockState bid = this.level().getBlockState(new BlockPos(x + dx, y + i, z + j));
                if (this.isLightSource(bid)) {
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
                if (this.isLightSource(bid)) {
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
                if (this.isLightSource(bid)) {
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
                if (this.isLightSource(bid)) {
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
                if (this.isLightSource(bid)) {
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
                if (this.isLightSource(bid)) {
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
    protected void customServerAiStep() {
        super.customServerAiStep();
        if (this.isDeadOrDying()) {
            return;
        }
        if (this.currentFlightTarget == null) {
            this.currentFlightTarget = this.blockPosition();
        }
        // gold: repath 1/100 or dist < 4 → random air; else night torch hunt 1/10
        if (this.random.nextInt(100) == 0
                || this.currentFlightTarget.distToCenterSqr(this.getX(), this.getY(), this.getZ()) < 4.0) {
            int keepTrying = 25;
            BlockState bid = Blocks.STONE.defaultBlockState();
            while (!bid.isAir() && keepTrying != 0) {
                keepTrying--;
                this.currentFlightTarget = BlockPos.containing(
                        this.getX() + this.random.nextInt(10) - this.random.nextInt(10),
                        this.getY() + this.random.nextInt(6) - 2,
                        this.getZ() + this.random.nextInt(10) - this.random.nextInt(10));
                bid = this.level().getBlockState(this.currentFlightTarget);
            }
        } else if (!this.level().isDay() && this.random.nextInt(10) == 0) {
            this.closest = 99999;
            this.tx = this.ty = this.tz = 0;
            for (int i = 2; i < 15 && !this.scanIt((int) this.getX(), (int) this.getY(), (int) this.getZ(), i, i, i); i++) {
                if (i >= 6) {
                    i++;
                }
            }
            if (this.closest < 99999) {
                this.currentFlightTarget = new BlockPos(this.tx, this.ty + 1, this.tz);
            }
        }
        double var1 = this.currentFlightTarget.getX() + 0.5 - this.getX();
        double var3 = this.currentFlightTarget.getY() + 0.1 - this.getY();
        double var5 = this.currentFlightTarget.getZ() + 0.5 - this.getZ();
        Vec3 m = this.getDeltaMovement();
        this.setDeltaMovement(
                m.x + (Math.signum(var1) * 0.5 - m.x) * 0.1F,
                m.y + (Math.signum(var3) * 0.68 - m.y) * 0.1F,
                m.z + (Math.signum(var5) * 0.5 - m.z) * 0.1F);
        float var7 = (float) (Math.atan2(this.getDeltaMovement().z, this.getDeltaMovement().x) * 180.0 / Math.PI) - 90.0F;
        float var8 = Mth.wrapDegrees(var7 - this.getYRot());
        this.setZza(0.75F);
        this.setYRot(this.getYRot() + var8);
    }

    @Override
    public boolean causeFallDamage(float fallDistance, float multiplier, DamageSource source) {
        return false;
    }

    @Override
    protected void checkFallDamage(double y, boolean onGround, BlockState state, BlockPos pos) {}

    /** Gold {@code canTriggerWalking} was only on Mosquito; moth still ignores ground triggers. */
    @Override
    public boolean isIgnoringBlockTriggers() {
        return true;
    }

    /**
     * Gold {@code getCanSpawnHere}: air block, not day, y ≥ 50.
     */
    @Override
    public boolean checkSpawnRules(LevelAccessor level, MobSpawnType spawnType) {
        if (!level.getBlockState(this.blockPosition()).isAir()) {
            return false;
        }
        if (level instanceof Level lvl && lvl.isDay()) {
            return false;
        }
        return !(this.getY() < 50.0);
    }

    /**
     * Gold had moth_scale item but no entity drop hook; keep survival drop so moth armor is craftable.
     */
    @Override
    protected void dropCustomDeathLoot(ServerLevel level, DamageSource damageSource, boolean recentlyHit) {
        super.dropCustomDeathLoot(level, damageSource, recentlyHit);
        if (this.random.nextInt(3) == 0) {
            this.spawnAtLocation(new ItemStack(ModItems.MOTH_SCALE.get()));
        }
    }
}
