package danger.orespawn.world.gen;

import com.mojang.serialization.MapCodec;
import danger.orespawn.world.structures.GenericDungeon;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

/**
 * Gold {@code StructureGenerator}: pick XZ + Y, carve {@link GenericDungeon} immediately.
 * <p>
 * Previous port scanned only 8 blocks for solid and returned false often (caves / open
 * volume) so overworld almost never got rooms. Gold never required solid ground.
 */
public class DungeonFeature extends Feature<NoneFeatureConfiguration> {
    public static final MapCodec<DungeonFeature> CODEC = MapCodec.unit(DungeonFeature::new);

    public DungeonFeature() {
        super(NoneFeatureConfiguration.CODEC);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        WorldGenLevel level = context.level();
        RandomSource random = context.random();
        BlockPos origin = context.origin();

        // Optional: snap floor down to solid if nearby (nicer rooms), else use origin Y like gold.
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
        if (y + GenericDungeon.HEIGHT >= maxY) {
            y = maxY - GenericDungeon.HEIGHT - 1;
        }
        if (y < minY) {
            return false;
        }

        // Gold passed chunk-local x/z as room origin; center the 12×12 on the sample point.
        int cposx = origin.getX() - GenericDungeon.WIDTH / 2;
        int cposz = origin.getZ() - GenericDungeon.WIDTH / 2;

        return GenericDungeon.makeDungeon(level, random, cposx, y, cposz);
    }
}
