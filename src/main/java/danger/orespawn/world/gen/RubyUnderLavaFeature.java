package danger.orespawn.world.gen;

import com.mojang.serialization.MapCodec;
import danger.orespawn.init.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

/**
 * Gold {@code OreSpawnWorld.generateRuby}: each try picks a column and y, then (only when
 * y ≤ 50) scans straight down for lava and turns the stone directly beneath it into one
 * ruby ore. Deepslate counts as stone so the deep lava level works like 1.7.10's y≈10 lava.
 * <p>
 * 1.7.10 flooded caves below y≈10 with lava; 1.21 has far less underground lava, so the placed
 * features try ~6x more columns than gold's 10–14 to keep a similar ruby yield per chunk.
 */
public class RubyUnderLavaFeature extends Feature<NoneFeatureConfiguration> {
    public static final MapCodec<RubyUnderLavaFeature> CODEC = MapCodec.unit(RubyUnderLavaFeature::new);

    /** Gold {@code Ruby_stats.maxdepth}. */
    private static final int MAX_START_Y = 50;

    public RubyUnderLavaFeature() {
        super(NoneFeatureConfiguration.CODEC);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        WorldGenLevel level = context.level();
        BlockPos origin = context.origin();
        if (origin.getY() > MAX_START_Y) {
            return false;
        }
        int bottom = level.getMinBuildHeight() + 5;
        BlockPos.MutableBlockPos cursor = origin.mutable();
        for (int y = origin.getY(); y > bottom; y--) {
            cursor.setY(y);
            if (level.getFluidState(cursor).is(FluidTags.LAVA)) {
                cursor.setY(y - 1);
                BlockState below = level.getBlockState(cursor);
                if (below.is(Blocks.STONE) || below.is(Blocks.DEEPSLATE)) {
                    level.setBlock(cursor, ModBlocks.RUBY_ORE.get().defaultBlockState(), 2);
                    return true;
                }
                // gold keeps scanning down when the block under the lava is not stone
            }
        }
        return false;
    }
}
