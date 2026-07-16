package danger.orespawn.world.structures;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Shared structure placement helpers.
 * <p>
 * Always clear volumes to air before placing walls underground — otherwise solid
 * stone/ore remains inside hollow shells (cylinder towers, rings, etc.).
 * Uses {@link Block#UPDATE_ALL} so clients see changes in survival placement.
 */
public final class StructureHelper {
    /** Notify clients + neighbors — required for player-placed / item-placed structures. */
    public static final int FLAGS = Block.UPDATE_ALL;

    private StructureHelper() {}

    public static void set(WorldGenLevel level, int x, int y, int z, BlockState state) {
        int minY = level.getMinBuildHeight();
        int maxY = level.getMaxBuildHeight();
        if (y < minY || y >= maxY) {
            return;
        }
        level.setBlock(new BlockPos(x, y, z), state, FLAGS);
    }

    public static void set(WorldGenLevel level, BlockPos pos, BlockState state) {
        int y = pos.getY();
        int minY = level.getMinBuildHeight();
        int maxY = level.getMaxBuildHeight();
        if (y < minY || y >= maxY) {
            return;
        }
        level.setBlock(pos, state, FLAGS);
    }

    /** Axis-aligned box: {@code width × height × depth} from origin corner (exclusive max). */
    public static void clearBox(
            WorldGenLevel level, int x0, int y0, int z0, int width, int height, int depth) {
        BlockState air = Blocks.AIR.defaultBlockState();
        for (int dx = 0; dx < width; dx++) {
            for (int dy = 0; dy < height; dy++) {
                for (int dz = 0; dz < depth; dz++) {
                    set(level, x0 + dx, y0 + dy, z0 + dz, air);
                }
            }
        }
    }

    /**
     * Hollow only the interior of a shell (keeps floor/ceiling/walls).
     * Call after walls so underground stone never remains walkable-space.
     * Interior is {@code (1.width-2) × (1.height-2) × (1.depth-2)}.
     */
    public static void hollowInterior(
            WorldGenLevel level, int x0, int y0, int z0, int width, int height, int depth) {
        if (width < 3 || height < 3 || depth < 3) {
            return;
        }
        BlockState air = Blocks.AIR.defaultBlockState();
        for (int dx = 1; dx < width - 1; dx++) {
            for (int dy = 1; dy < height - 1; dy++) {
                for (int dz = 1; dz < depth - 1; dz++) {
                    set(level, x0 + dx, y0 + dy, z0 + dz, air);
                }
            }
        }
    }

    /**
     * Solid cylinder volume (inclusive radius) from {@code y0} for {@code height} levels.
     * Clears stone/ore that would otherwise remain inside ring-wall structures.
     */
    public static void clearCylinder(
            WorldGenLevel level, int cx, int y0, int cz, float radius, int height) {
        BlockState air = Blocks.AIR.defaultBlockState();
        int r = (int) Math.ceil(radius);
        int r2 = r * r;
        for (int dy = 0; dy < height; dy++) {
            int y = y0 + dy;
            for (int dx = -r; dx <= r; dx++) {
                for (int dz = -r; dz <= r; dz++) {
                    if (dx * dx + dz * dz <= r2) {
                        set(level, cx + dx, y, cz + dz, air);
                    }
                }
            }
        }
    }

    /**
     * Clear interior of a cylinder wall (radius exclusive of outer ring cells).
     * Keeps a 1-block ring at {@code radius} for walls; floors at caller discretion.
     */
    public static void hollowCylinder(
            WorldGenLevel level, int cx, int y0, int cz, float innerRadius, int height) {
        BlockState air = Blocks.AIR.defaultBlockState();
        int r = (int) Math.floor(innerRadius);
        if (r < 1) {
            return;
        }
        int r2 = r * r;
        for (int dy = 0; dy < height; dy++) {
            int y = y0 + dy;
            for (int dx = -r; dx <= r; dx++) {
                for (int dz = -r; dz <= r; dz++) {
                    if (dx * dx + dz * dz <= r2) {
                        set(level, cx + dx, y, cz + dz, air);
                    }
                }
            }
        }
    }
}
