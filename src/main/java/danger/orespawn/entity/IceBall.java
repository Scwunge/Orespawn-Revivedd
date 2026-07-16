package danger.orespawn.entity;

import danger.orespawn.init.ModEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

/**
 * Gold {@code IceBall} extends {@code LaserBall} with iceball mode + optional ice maker
 * (places ice near impact). Royalty hits are ignored (discard only).
 * <p>
 * Registry: {@code ice_ball}.
 */
public class IceBall extends LaserBall {
    private int myIndex = 84;
    private int iceMaker = 0;

    public IceBall(EntityType<? extends IceBall> type, Level level) {
        super(castType(type), level);
        this.setIceBall();
    }

    public IceBall(Level level, LivingEntity thrower) {
        super(ModEntities.ICE_BALL.get(), thrower, level);
        this.setIceBall();
    }

    public IceBall(Level level, double x, double y, double z) {
        super(ModEntities.ICE_BALL.get(), x, y, z, level);
        this.setIceBall();
    }

    @SuppressWarnings("unchecked")
    private static EntityType<? extends LaserBall> castType(EntityType<? extends IceBall> type) {
        return (EntityType<? extends LaserBall>) (EntityType<?>) type;
    }

    public int getIceBallIndex() {
        return this.myIndex;
    }

    public void setIceMaker(int i) {
        this.iceMaker = i;
    }

    public int getIceMaker() {
        return this.iceMaker;
    }

    @Override
    protected void onHit(HitResult result) {
        if (this.level().isClientSide) {
            return;
        }

        if (result.getType() == HitResult.Type.ENTITY) {
            Entity hit = ((EntityHitResult) result).getEntity();
            if (isRoyalty(hit)) {
                this.discard();
                return;
            }
        }

        // gold: super.onImpact then iceMaker place + kill
        super.onHit(result);

        if (this.iceMaker != 0 && !this.isRemoved()) {
            // super may have discarded — place ice before fully gone if still here
        }
        if (this.iceMaker != 0) {
            Vec3 at = result.getLocation();
            for (int i = 0; i < 5; i++) {
                int x = this.random.nextInt(4);
                if (this.random.nextInt(2) == 1) {
                    x = -x;
                }
                int y = this.random.nextInt(4);
                if (this.random.nextInt(2) == 1) {
                    y = -y;
                }
                int z = this.random.nextInt(4);
                if (this.random.nextInt(2) == 1) {
                    z = -z;
                }
                BlockPos pos = BlockPos.containing(at.x + x, at.y + y, at.z + z);
                if (this.level().getBlockState(pos).isAir()
                        || this.level().getBlockState(pos).canBeReplaced()) {
                    this.level().setBlockAndUpdate(pos, Blocks.ICE.defaultBlockState());
                }
            }
        }

        if (!this.isRemoved()) {
            this.discard();
        }
    }

    /** Gold {@code MyUtils.isRoyalty}. */
    private static boolean isRoyalty(Entity e) {
        if (!(e instanceof LivingEntity)) {
            return false;
        }
        return e instanceof ThePrince
                || e instanceof ThePrinceTeen
                || e instanceof ThePrinceAdult
                || e instanceof ThePrincess
                || e instanceof TheKing
                || e instanceof KingHead
                || e instanceof TheQueen;
        // PurplePower / QueenHead deferred if absent
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("IceMaker", this.iceMaker);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.iceMaker = tag.getInt("IceMaker");
        this.setIceBall();
    }
}
