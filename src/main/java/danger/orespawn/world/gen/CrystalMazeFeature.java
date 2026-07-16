package danger.orespawn.world.gen;

import com.mojang.serialization.MapCodec;
import danger.orespawn.world.structures.CrystalMaze;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

/**
 * Worldgen wrapper for {@link CrystalMaze}, mirroring {@link NightmareDungeonFeature}.
 * <p>
 * Gold places this once per chunk in Crystal dim via {@code ChunkProviderOreSpawn5} at
 * {@code (chunkX*16, 25, chunkZ*16)}. This feature snaps X/Z to chunk origin and uses
 * {@link CrystalMaze#DEFAULT_Y} (25) so placement matches gold.
 * <p>
 * Register via:
 * <pre>{@code
 * // ModFeatures.java
 * import danger.orespawn.world.gen.CrystalMazeFeature;
 *
 * public static final DeferredHolder<Feature<?>, Feature<NoneFeatureConfiguration>> CRYSTAL_MAZE =
 *         FEATURES.register("crystal_maze", CrystalMazeFeature::new);
 * }</pre>
 * Then configured/placed feature + biome modifier for the Crystal dimension (every chunk / high
 * chance), or call {@link CrystalMaze#buildCrystalMaze} from a crystal-dim chunk generator.
 */
public class CrystalMazeFeature extends Feature<NoneFeatureConfiguration> {
    public static final MapCodec<CrystalMazeFeature> CODEC = MapCodec.unit(CrystalMazeFeature::new);

    public CrystalMazeFeature() {
        super(NoneFeatureConfiguration.CODEC);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        WorldGenLevel level = context.level();
        RandomSource random = context.random();
        BlockPos origin = context.origin();

        // Gold: par1*16, 25, par2*16 — align to chunk origin, fixed Y
        int x = origin.getX() & ~15;
        int z = origin.getZ() & ~15;
        int y = CrystalMaze.DEFAULT_Y;

        int minY = level.getMinBuildHeight();
        int maxY = level.getMaxBuildHeight();
        if (y - 1 < minY) {
            y = minY + 1;
        }
        if (y + 3 >= maxY) {
            y = maxY - 4;
        }
        if (y - 1 < minY || y + 3 >= maxY) {
            return false;
        }

        return CrystalMaze.buildCrystalMaze(level, random, x, y, z);
    }
}
