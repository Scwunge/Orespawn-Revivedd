package danger.orespawn.world.gen;

import com.mojang.serialization.MapCodec;
import danger.orespawn.init.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

/**
 * Gold {@code PlantGenerator} — places mature butterfly / mosquito / firefly plants
 * in a small patch on grass/dirt (1% decorate chance handled by placed_feature rarity).
 */
public class InsectPlantFeature extends Feature<NoneFeatureConfiguration> {
    public static final MapCodec<InsectPlantFeature> CODEC = MapCodec.unit(InsectPlantFeature::new);

    public InsectPlantFeature() {
        super(NoneFeatureConfiguration.CODEC);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        WorldGenLevel level = context.level();
        BlockPos origin = context.origin();
        RandomSource random = context.random();

        Block cropBlock = switch (random.nextInt(3)) {
            case 0 -> ModBlocks.BUTTERFLY_PLANT.get();
            case 1 -> ModBlocks.MOSQUITO_PLANT.get();
            default -> ModBlocks.FIREFLY_PLANT.get();
        };

        if (!(cropBlock instanceof CropBlock crop)) {
            return false;
        }

        boolean placed = false;
        for (int i = 0; i < 12; i++) {
            BlockPos pos = origin.offset(
                    random.nextInt(8) - random.nextInt(8),
                    random.nextInt(4) - random.nextInt(4),
                    random.nextInt(8) - random.nextInt(8));
            if (pos.getY() >= level.getMaxBuildHeight() - 1) {
                continue;
            }
            BlockState below = level.getBlockState(pos.below());
            if (!level.isEmptyBlock(pos)) {
                continue;
            }
            if (!(below.is(Blocks.GRASS_BLOCK)
                    || below.is(Blocks.DIRT)
                    || below.is(Blocks.COARSE_DIRT)
                    || below.is(Blocks.PODZOL)
                    || below.is(Blocks.FARMLAND))) {
                continue;
            }
            BlockState mature = crop.getStateForAge(crop.getMaxAge());
            level.setBlock(pos, mature, 2);
            placed = true;
        }
        return placed;
    }
}
