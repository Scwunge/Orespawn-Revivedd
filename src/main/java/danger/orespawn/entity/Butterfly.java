package danger.orespawn.entity;

import danger.orespawn.init.ModDimensions;
import danger.orespawn.util.AntDimensionPortal;
import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

/**
 * Gold {@code Butterfly} (EntityCreature + IAnimals). Size 0.4×0.4, speed 0.1, health 1. Passive flyer.
 */
public class Butterfly extends PathfinderMob {
    private static final EntityDataAccessor<Integer> DATA_TYPE =
            SynchedEntityData.defineId(Butterfly.class, EntityDataSerializers.INT);

    @Nullable
    private BlockPos spawnPosition;
    /** Local mirror; authoritative value is {@link #DATA_TYPE} for client textures. */
    public int butterflyType = 1;

    public Butterfly(EntityType<? extends Butterfly> type, Level level) {
        super(type, level);
        if (!level.isClientSide) {
            this.setButterflyType(this.random.nextInt(4) + 1);
        }
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_TYPE, 1);
    }

    public void setButterflyType(int type) {
        this.butterflyType = type;
        this.entityData.set(DATA_TYPE, type);
    }

    public int getButterflyType() {
        return this.entityData.get(DATA_TYPE);
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> key) {
        super.onSyncedDataUpdated(key);
        if (DATA_TYPE.equals(key)) {
            this.butterflyType = this.entityData.get(DATA_TYPE);
        }
    }

    public static AttributeSupplier.Builder createAttributes() {
        return PathfinderMob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 1.0)
                .add(Attributes.MOVEMENT_SPEED, 0.1)
                .add(Attributes.FOLLOW_RANGE, 16.0);
    }

    @Override
    public void tick() {
        super.tick();
        Vec3 m = this.getDeltaMovement();
        this.setDeltaMovement(m.x, m.y * 0.6F, m.z);
        // Gold World.func_175657_ab() = getSkylightSubtracted / sky darken (high at night).
        // Was wrongly mapped to block brightness (high by day) → spawn eggs vanished outdoors.
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

    /** 1.7.10: empty-hand butterfly toggles Chaos dimension. */
    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        if (player == null) {
            return InteractionResult.FAIL;
        }
        if (!(player instanceof ServerPlayer)) {
            return InteractionResult.PASS;
        }
        ItemStack stack = player.getItemInHand(hand);
        if (!stack.isEmpty() && stack.getCount() <= 0) {
            player.setItemInHand(hand, ItemStack.EMPTY);
            stack = ItemStack.EMPTY;
        }
        if (stack.isEmpty()) {
            AntDimensionPortal.tryToggle(
                    player, stack, ModDimensions.CHAOS, "Warped to the Chaos Dimension.");
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }
}
