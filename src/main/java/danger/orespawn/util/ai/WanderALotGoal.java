package danger.orespawn.util.ai;

import java.util.EnumSet;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.phys.Vec3;

/**
 * Port of gold {@code MyEntityAIWanderALot} — wanders with custom xz range, 1/30 chance per tick.
 */
public class WanderALotGoal extends Goal {
    private final PathfinderMob entity;
    private double xPosition;
    private double yPosition;
    private double zPosition;
    private final double speed;
    private int xzRange = 10;
    private int busy = 0;

    public WanderALotGoal(PathfinderMob entity, int xzRange, double speed) {
        this.entity = entity;
        this.xzRange = xzRange;
        this.speed = speed;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE));
    }

    public void setBusy(int busy) {
        this.busy = busy;
    }

    @Override
    public boolean canUse() {
        if (this.busy != 0) {
            return false;
        }
        if (this.entity.getRandom().nextInt(30) != 0) {
            return false;
        }
        if (this.entity instanceof TamableAnimal tamable && tamable.isOrderedToSit()) {
            return false;
        }
        Vec3 pos = DefaultRandomPos.getPos(this.entity, this.xzRange, 7);
        if (pos == null) {
            return false;
        }
        this.xPosition = pos.x;
        this.yPosition = pos.y;
        this.zPosition = pos.z;
        return true;
    }

    @Override
    public boolean canContinueToUse() {
        return !this.entity.getNavigation().isDone();
    }

    @Override
    public void start() {
        this.entity.getNavigation().moveTo(this.xPosition, this.yPosition, this.zPosition, this.speed);
    }
}
