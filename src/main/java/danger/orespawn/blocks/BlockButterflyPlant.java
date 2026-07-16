package danger.orespawn.blocks;

import danger.orespawn.entity.Butterfly;
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
 * Gold {@code BlockButterflyPlant} — crop that spawns butterflies by day when grown.
 */
public class BlockButterflyPlant extends CropBlock {
    public BlockButterflyPlant(BlockBehaviour.Properties properties) {
        super(properties);
    }

    /** Allow grass/dirt so wild PlantGenerator patches survive (gold placed on surface). */
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
        int count = level.getEntitiesOfClass(Butterfly.class, aabb).size();
        if (count <= 15) {
            int rate = this.getAge(state) & 7;
            rate = 7 - rate;
            if (rate <= 1 || random.nextInt(rate) == 0) {
                if (level.getBlockState(pos.above()).is(Blocks.AIR) && level.isDay()) {
                    Entity butterfly = ModEntities.BUTTERFLY.get().create(level);
                    if (butterfly != null) {
                        butterfly.moveTo(pos.getX() + 0.5, pos.getY() + 1.0, pos.getZ() + 0.5, 0.0F, 0.0F);
                        level.addFreshEntity(butterfly);
                    }
                }
            }
        }
    }

    @Override
    protected ItemLike getBaseSeedId() {
        return ModItems.BUTTERFLY_SEED.get();
    }
}
