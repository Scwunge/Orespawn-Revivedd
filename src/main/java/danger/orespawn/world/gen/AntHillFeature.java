package danger.orespawn.world.gen;

import com.mojang.serialization.MapCodec;
import danger.orespawn.init.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

/**
 * Gold {@code AntHillGenerator}: if surface is grass, replace with {@code ant_block}.
 * Datapack {@code simple_block} cannot replace solid grass, so this feature is required.
 */
public class AntHillFeature extends Feature<NoneFeatureConfiguration> {
    public static final MapCodec<AntHillFeature> CODEC = MapCodec.unit(AntHillFeature::new);

    public AntHillFeature() {
        super(NoneFeatureConfiguration.CODEC);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        WorldGenLevel level = context.level();
        BlockPos origin = context.origin();
        BlockPos.MutableBlockPos cursor = origin.mutable();

        // Heightmap often lands on the top solid block (grass). Search a few steps down for grass.
        for (int i = 0; i < 6; i++) {
            BlockState state = level.getBlockState(cursor);
            if (state.is(Blocks.GRASS_BLOCK)) {
                level.setBlock(cursor, ModBlocks.ANT_BLOCK.get().defaultBlockState(), 2);
                return true;
            }
            // Already an ant nest — count as success so we don't spam
            if (state.is(ModBlocks.ANT_BLOCK.get())) {
                return true;
            }
            cursor.move(Direction.DOWN);
        }
        return false;
    }
}
