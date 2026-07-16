package danger.orespawn.world.gen;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

/**
 * Gold {@code LiquidGenerator} — 50/50 water or lava source at surface air above solid
 * (mining biome grass-decorate path, ~10% of attempts).
 */
public class LiquidSurfaceFeature extends Feature<NoneFeatureConfiguration> {
    public static final MapCodec<LiquidSurfaceFeature> CODEC = MapCodec.unit(LiquidSurfaceFeature::new);

    public LiquidSurfaceFeature() {
        super(NoneFeatureConfiguration.CODEC);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        WorldGenLevel level = context.level();
        BlockPos position = context.origin();
        RandomSource random = context.random();

        // gold: !isNether && isAir(position)
        if (level.dimensionType().ultraWarm()) {
            return false;
        }
        if (!level.isEmptyBlock(position)) {
            return false;
        }
        // need solid underfoot so liquid sits on surface
        if (!level.getBlockState(position.below()).isSolid()) {
            return false;
        }

        if (random.nextBoolean()) {
            level.setBlock(position, Blocks.WATER.defaultBlockState(), 2);
        } else {
            level.setBlock(position, Blocks.LAVA.defaultBlockState(), 2);
        }
        return true;
    }
}
