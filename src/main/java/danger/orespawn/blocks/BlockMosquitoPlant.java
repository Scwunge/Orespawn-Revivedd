package danger.orespawn.blocks;

import danger.orespawn.entity.Mosquito;
import danger.orespawn.init.ModEntities;
import danger.orespawn.init.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.FarmBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

/**
 * Gold {@code BlockMosquitoPlant} — crop that spawns mosquitoes by day when grown.
 */
public class BlockMosquitoPlant extends CropBlock {
    public BlockMosquitoPlant(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
        return state.getBlock() instanceof FarmBlock
                || state.is(Blocks.GRASS_BLOCK)
                || state.is(Blocks.DIRT)
                || state.is(Blocks.COARSE_DIRT)
                || state.is(Blocks.PODZOL)
                || state.is(Blocks.ROOTED_DIRT)
                || state.is(Blocks.MUD);
    }

    @Override
    protected boolean isRandomlyTicking(BlockState state) {
        return true;
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        super.randomTick(state, level, pos, random);
        if (level.isClientSide) {
            return;
        }
        float radius = 50.0F;
        AABB aabb = new AABB(
                pos.getX() - radius, 0.0, pos.getZ() - radius,
                pos.getX() + radius, 200.0, pos.getZ() + radius);
        int count = level.getEntitiesOfClass(Mosquito.class, aabb).size();
        if (count <= 15) {
            int rate = this.getAge(state) & 7;
            rate = 6 - rate;
            if (rate <= 1 || random.nextInt(Math.max(1, rate)) == 0) {
                if (level.getBlockState(pos.above()).is(Blocks.AIR) && level.isDay()) {
                    Entity mosquito = ModEntities.MOSQUITO.get().create(level);
                    if (mosquito != null) {
                        mosquito.moveTo(pos.getX() + 0.5, pos.getY() + 1.0, pos.getZ() + 0.5, 0.0F, 0.0F);
                        level.addFreshEntity(mosquito);
                    }
                }
            }
        }
    }

    @Override
    protected ItemLike getBaseSeedId() {
        return ModItems.MOSQUITO_SEED.get();
    }
}
