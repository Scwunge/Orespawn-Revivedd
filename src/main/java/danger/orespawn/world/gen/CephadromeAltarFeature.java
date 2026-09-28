package danger.orespawn.world.gen;

import com.mojang.serialization.MapCodec;
import danger.orespawn.world.structures.CephadromeAltar;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

/**
 * Worldgen wrapper for {@link CephadromeAltar} (gold dungeon spawner type 34).
 * <p>
 * Placed on the surface (heightmap placement): only on dry, solid ground above sea level so
 * the 9×9 pad never floats over water or lands in a cave.
 */
public class CephadromeAltarFeature extends Feature<NoneFeatureConfiguration> {
    public static final MapCodec<CephadromeAltarFeature> CODEC = MapCodec.unit(CephadromeAltarFeature::new);

    public CephadromeAltarFeature() {
        super(NoneFeatureConfiguration.CODEC);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        WorldGenLevel level = context.level();
        BlockPos origin = context.origin();
        if (origin.getY() <= level.getSeaLevel()) {
            return false;
        }

        // Every pad corner must sit on solid, dry ground (skips cliffs, rivers, trees).
        int half = CephadromeAltar.HALF;
        for (int dx = -half; dx <= half; dx += half) {
            for (int dz = -half; dz <= half; dz += half) {
                BlockPos ground = origin.offset(dx, -1, dz);
                BlockState state = level.getBlockState(ground);
                if (!state.getFluidState().isEmpty()
                        || state.is(BlockTags.LOGS)
                        || !state.isCollisionShapeFullBlock(level, ground)) {
                    return false;
                }
            }
        }

        return CephadromeAltar.makeCephadromeAltar(
                level, context.random(), origin.getX(), origin.getY(), origin.getZ());
    }
}
