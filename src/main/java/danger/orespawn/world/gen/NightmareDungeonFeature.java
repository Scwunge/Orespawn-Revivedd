package danger.orespawn.world.gen;

import com.mojang.serialization.MapCodec;
import danger.orespawn.world.structures.NightmareDungeon;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

/**
 * Worldgen wrapper for {@link NightmareDungeon}, mirroring {@link DungeonFeature}.
 * <p>
 * Register via {@code ModFeatures.FEATURES.register("nightmare_dungeon", NightmareDungeonFeature::new)}.
 */
public class NightmareDungeonFeature extends Feature<NoneFeatureConfiguration> {
    public static final MapCodec<NightmareDungeonFeature> CODEC = MapCodec.unit(NightmareDungeonFeature::new);

    public NightmareDungeonFeature() {
        super(NoneFeatureConfiguration.CODEC);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        WorldGenLevel level = context.level();
        RandomSource random = context.random();
        BlockPos origin = context.origin();

        // Snap floor down to solid if nearby; else use origin Y.
        int y = origin.getY();
        BlockPos.MutableBlockPos cursor = origin.mutable();
        for (int i = 0; i < 16; i++) {
            BlockState state = level.getBlockState(cursor);
            if (!state.isAir() && state.getFluidState().isEmpty() && state.isCollisionShapeFullBlock(level, cursor)) {
                y = cursor.getY();
                break;
            }
            cursor.move(Direction.DOWN);
        }

        int minY = level.getMinBuildHeight();
        int maxY = level.getMaxBuildHeight();
        if (y < minY + 1) {
            y = minY + 1;
        }
        if (y + NightmareDungeon.HEIGHT >= maxY) {
            y = maxY - NightmareDungeon.HEIGHT - 1;
        }
        if (y < minY) {
            return false;
        }

        // Center the 25×25 on the sample point.
        int cposx = origin.getX() - NightmareDungeon.WIDTH / 2;
        int cposz = origin.getZ() - NightmareDungeon.WIDTH / 2;

        return NightmareDungeon.makeDungeon(level, random, cposx, y, cposz);
    }
}
