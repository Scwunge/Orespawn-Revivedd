package danger.orespawn.world.gen;

import com.mojang.serialization.MapCodec;
import danger.orespawn.blocks.BlockCornPlant;
import danger.orespawn.init.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

/**
 * Gold {@code CornPlantGenerator} — places short mature corn stalks on grass/dirt.
 * (Gold stacked multi-block corn via TileEntityPlant; we place stage-3 base + up to 2 stages above.)
 */
public class CornPlantFeature extends Feature<NoneFeatureConfiguration> {
    public static final MapCodec<CornPlantFeature> CODEC = MapCodec.unit(CornPlantFeature::new);

    public CornPlantFeature() {
        super(NoneFeatureConfiguration.CODEC);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        WorldGenLevel level = context.level();
        BlockPos origin = context.origin();
        RandomSource random = context.random();
        BlockCornPlant plant = (BlockCornPlant) ModBlocks.CORN_PLANT.get();
        BlockState mature = plant.defaultBlockState().setValue(BlockCornPlant.STAGE, 3);

        boolean placed = false;
        for (int i = 0; i < 32; i++) {
            BlockPos base = origin.offset(
                    random.nextInt(8) - random.nextInt(8),
                    random.nextInt(4) - random.nextInt(4),
                    random.nextInt(8) - random.nextInt(8));
            if (base.getY() >= level.getMaxBuildHeight() - 3) {
                continue;
            }
            if (!level.isEmptyBlock(base)) {
                continue;
            }
            BlockState below = level.getBlockState(base.below());
            if (!(below.is(Blocks.GRASS_BLOCK)
                    || below.is(Blocks.DIRT)
                    || below.is(Blocks.COARSE_DIRT)
                    || below.is(Blocks.PODZOL)
                    || below.is(Blocks.FARMLAND))) {
                continue;
            }

            int height = 1 + random.nextInt(3);
            for (int h = 0; h < height; h++) {
                BlockPos pos = base.above(h);
                if (!level.isEmptyBlock(pos) && !level.getBlockState(pos).is(Blocks.SHORT_GRASS)
                        && !level.getBlockState(pos).is(Blocks.TALL_GRASS)) {
                    break;
                }
                level.setBlock(pos, mature, 2);
                placed = true;
            }
        }
        return placed;
    }
}
