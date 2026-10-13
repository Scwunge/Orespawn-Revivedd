package danger.orespawn.blocks;

import java.util.function.Supplier;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

/**
 * Gold {@code AntBlock} / {@code CrystalAntBlock} — an ant nest. On a random tick, if it is not
 * raining and the block above is air, it releases 2–7 ants of its kind (Ant Nest, Red Ant Nest,
 * Rainbow Ant Nest, Unstable Ant Nest, Termite Nest, Crystal Termite Nest).
 */
public class BlockAnt extends Block {
    /** Port lag guard: stop releasing ants once this many of the kind are nearby (gold had no cap). */
    private static final int MAX_NEARBY = 20;

    private final Supplier<? extends EntityType<? extends Mob>> ant;

    public BlockAnt(BlockBehaviour.Properties properties, Supplier<? extends EntityType<? extends Mob>> ant) {
        super(properties);
        this.ant = ant;
    }

    @Override
    protected boolean isRandomlyTicking(BlockState state) {
        return true;
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        super.randomTick(state, level, pos, random);
        if (level.isRaining() || !level.getBlockState(pos.above()).is(Blocks.AIR)) {
            return;
        }

        EntityType<? extends Mob> type = this.ant.get();
        int radius = 16;
        AABB area = new AABB(pos).inflate(radius, 100.0, radius);
        if (level.getEntities(type, area, e -> true).size() > MAX_NEARBY) {
            return;
        }

        int howmany = random.nextInt(6) + 2;
        for (int i = 0; i < howmany; i++) {
            Mob mob = type.create(level);
            if (mob == null) {
                continue;
            }
            mob.moveTo(pos.getX() + 0.5, pos.getY() + 1.01, pos.getZ() + 0.5, random.nextFloat() * 360.0F, 0.0F);
            level.addFreshEntity(mob);
            mob.playAmbientSound();
        }
    }
}
