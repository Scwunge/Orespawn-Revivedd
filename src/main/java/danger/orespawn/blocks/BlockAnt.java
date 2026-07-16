package danger.orespawn.blocks;

import danger.orespawn.entity.RedAnt;
import danger.orespawn.init.ModEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

/**
 * Gold {@code BlockAnt} (registry {@code ant_block}) — nest that spawns red ants by day.
 */
public class BlockAnt extends Block {
    public BlockAnt(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    protected boolean isRandomlyTicking(BlockState state) {
        return true;
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (level.isClientSide) {
            return;
        }
        super.randomTick(state, level, pos, random);
        if (!level.getBlockState(pos.above()).is(Blocks.AIR) || !level.isDay()) {
            return;
        }

        int radius = 16;
        AABB aabb = new AABB(
                pos.getX() - radius, 0.0, pos.getZ() - radius,
                pos.getX() + radius, 200.0, pos.getZ() + radius);
        int antCount = level.getEntitiesOfClass(RedAnt.class, aabb).size();
        if (antCount > 20) {
            return;
        }

        int howmany = random.nextInt(6) + 2;
        for (int i = 0; i < howmany; i++) {
            Entity ant = ModEntities.RED_ANT.get().create(level);
            if (ant != null) {
                ant.moveTo(
                        pos.getX() + 0.5,
                        pos.getY() + 1.0,
                        pos.getZ() + 0.5,
                        random.nextFloat() * 360.0F,
                        0.0F);
                level.addFreshEntity(ant);
                if (ant instanceof Mob mob) {
                    mob.playAmbientSound();
                }
            }
        }
    }
}
