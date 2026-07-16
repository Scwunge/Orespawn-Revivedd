package danger.orespawn.util.ai;

import java.util.EnumSet;
import java.util.UUID;
import java.util.function.Supplier;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.level.pathfinder.PathType;
import org.jetbrains.annotations.Nullable;

/**
 * Gold {@code MyEntityAIFollowOwner} — simplified 1.21 port for non-TamableAnimal pets
 * that store an owner UUID (see {@link danger.orespawn.entity.tame.OreSpawnPet}).
 */
public class FollowOwnerGoal extends Goal {
    private final Mob pet;
    private final Supplier<@Nullable UUID> ownerId;
    private final Supplier<Boolean> isSitting;
    private final double speedModifier;
    private final float startDistance;
    private final float stopDistance;
    private LivingEntity owner;
    private int timeToRecalcPath;
    private float oldWaterCost;

    /**
     * @param speedModifier gold path speed (e.g. 1.15)
     * @param startDistance gold maxDist — start following when farther than this
     * @param stopDistance gold minDist — stop when closer than this
     */
    public FollowOwnerGoal(
            Mob pet,
            Supplier<@Nullable UUID> ownerId,
            Supplier<Boolean> isSitting,
            double speedModifier,
            float startDistance,
            float stopDistance) {
        this.pet = pet;
        this.ownerId = ownerId;
        this.isSitting = isSitting;
        this.speedModifier = speedModifier;
        this.startDistance = startDistance;
        this.stopDistance = stopDistance;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        UUID id = this.ownerId.get();
        if (id == null || Boolean.TRUE.equals(this.isSitting.get())) {
            return false;
        }
        LivingEntity owner = this.pet.level().getPlayerByUUID(id);
        if (owner == null || owner.isSpectator()) {
            return false;
        }
        if (this.pet.distanceToSqr(owner) < this.startDistance * this.startDistance) {
            return false;
        }
        this.owner = owner;
        return true;
    }

    @Override
    public boolean canContinueToUse() {
        if (Boolean.TRUE.equals(this.isSitting.get()) || this.owner == null) {
            return false;
        }
        PathNavigation nav = this.pet.getNavigation();
        if (nav.isDone()) {
            return false;
        }
        return this.pet.distanceToSqr(this.owner) > this.stopDistance * this.stopDistance;
    }

    @Override
    public void start() {
        this.timeToRecalcPath = 0;
        this.oldWaterCost = this.pet.getPathfindingMalus(PathType.WATER);
        this.pet.setPathfindingMalus(PathType.WATER, 0.0F);
    }

    @Override
    public void stop() {
        this.owner = null;
        this.pet.getNavigation().stop();
        this.pet.setPathfindingMalus(PathType.WATER, this.oldWaterCost);
    }

    @Override
    public void tick() {
        if (this.owner == null) {
            return;
        }
        this.pet.getLookControl().setLookAt(this.owner, 10.0F, this.pet.getMaxHeadXRot());
        if (--this.timeToRecalcPath <= 0) {
            this.timeToRecalcPath = 10;
            if (!this.pet.isLeashed() && !this.pet.isPassenger()) {
                if (this.pet.distanceToSqr(this.owner) >= 144.0) {
                    this.teleportToOwner();
                } else {
                    this.pet.getNavigation().moveTo(this.owner, this.speedModifier);
                }
            }
        }
    }

    private void teleportToOwner() {
        if (this.owner == null) {
            return;
        }
        BlockPos around = this.owner.blockPosition();
        for (int dx = -2; dx <= 2; dx++) {
            for (int dz = -2; dz <= 2; dz++) {
                if (Math.abs(dx) < 2 && Math.abs(dz) < 2) {
                    continue;
                }
                int x = around.getX() + dx;
                int z = around.getZ() + dz;
                int y = around.getY();
                if (this.canTeleportTo(x, y, z)) {
                    this.pet.moveTo(x + 0.5, y, z + 0.5, this.pet.getYRot(), this.pet.getXRot());
                    this.pet.getNavigation().stop();
                    return;
                }
            }
        }
    }

    private boolean canTeleportTo(int x, int y, int z) {
        var level = this.pet.level();
        var below = new BlockPos(x, y - 1, z);
        var feet = new BlockPos(x, y, z);
        var head = new BlockPos(x, y + 1, z);
        return level.getBlockState(below).isSolidRender(level, below)
                && level.getBlockState(feet).isAir()
                && level.getBlockState(head).isAir();
    }
}
