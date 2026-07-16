package danger.orespawn.world.gen;

import com.mojang.serialization.MapCodec;
import danger.orespawn.world.structures.BasiliskMaze;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

/**
 * Worldgen wrapper for {@link BasiliskMaze}, mirroring {@link NightmareDungeonFeature}.
 * <p>
 * Register via {@code ModFeatures.FEATURES.register("basilisk_maze", BasiliskMazeFeature::new)}.
 * Optional configured/placed feature + biome modifier once registered.
 */
public class BasiliskMazeFeature extends Feature<NoneFeatureConfiguration> {
    public static final MapCodec<BasiliskMazeFeature> CODEC = MapCodec.unit(BasiliskMazeFeature::new);

    public BasiliskMazeFeature() {
        super(NoneFeatureConfiguration.CODEC);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        WorldGenLevel level = context.level();
        RandomSource random = context.random();
        BlockPos origin = context.origin();

        // Snap surface down to solid if nearby; entrance origin is surface Y like gold spawner.
        int y = origin.getY();
        BlockPos.MutableBlockPos cursor = origin.mutable();
        for (int i = 0; i < 16; i++) {
            BlockState state = level.getBlockState(cursor);
            if (!state.isAir() && state.getFluidState().isEmpty() && state.isCollisionShapeFullBlock(level, cursor)) {
                y = cursor.getY() + 1; // stand on solid like a surface spawner
                break;
            }
            cursor.move(Direction.DOWN);
        }

        int minY = level.getMinBuildHeight();
        int maxY = level.getMaxBuildHeight();

        // Ensure room for deepest dig (depth 29) + floor + boss height + entrance collar
        int worstFloor = y - (BasiliskMaze.MIN_DEPTH + BasiliskMaze.DEPTH_RANGE - 1) - 4;
        if (worstFloor < minY + 1) {
            y = minY + 1 + (BasiliskMaze.MIN_DEPTH + BasiliskMaze.DEPTH_RANGE - 1) + 4;
        }
        if (y + BasiliskMaze.ENTRANCE_WIDTH >= maxY) {
            y = maxY - BasiliskMaze.ENTRANCE_WIDTH - 1;
        }
        if (y < minY + BasiliskMaze.MIN_DEPTH + 5) {
            return false;
        }

        return BasiliskMaze.buildBasiliskMaze(level, random, origin.getX(), y, origin.getZ());
    }
}
