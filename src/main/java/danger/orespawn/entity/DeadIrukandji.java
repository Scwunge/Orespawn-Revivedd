package danger.orespawn.entity;

import danger.orespawn.init.ModEntities;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

/**
 * Gold {@code DeadIrukandji} extends {@code LaserBall} with irukandji mode (100 damage, acid flag).
 * Sprite index 86. Thrown by gold {@code ItemIrukandji} ({@code deadirukandji}).
 * <p>
 * Registry: {@code dead_irukandji}.
 */
public class DeadIrukandji extends LaserBall {
    private int myIndex = 86;

    public DeadIrukandji(EntityType<? extends DeadIrukandji> type, Level level) {
        super(castType(type), level);
        this.setIrukandji();
    }

    public DeadIrukandji(Level level, LivingEntity thrower) {
        super(ModEntities.DEAD_IRUKANDJI.get(), thrower, level);
        this.setIrukandji();
    }

    public DeadIrukandji(Level level, double x, double y, double z) {
        super(ModEntities.DEAD_IRUKANDJI.get(), x, y, z, level);
        this.setIrukandji();
    }

    @SuppressWarnings("unchecked")
    private static EntityType<? extends LaserBall> castType(EntityType<? extends DeadIrukandji> type) {
        return (EntityType<? extends LaserBall>) (EntityType<?>) type;
    }

    public int getIrukandjiIndex() {
        return this.myIndex;
    }

    @Override
    public void readAdditionalSaveData(net.minecraft.nbt.CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.setIrukandji();
    }
}
