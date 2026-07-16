package danger.orespawn.util;

import com.mojang.logging.LogUtils;
import danger.orespawn.init.ModDimensions;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

/**
 * Gold {@code Teleport.teleportToDimension} for NeoForge 1.21.1.
 * Returns {@code null} on success, else a short error string.
 * <p>
 * Islands (Aetherial noise) often has void at origin — we spiral-search for solid
 * ground and only build a small emergency pad if nothing is found.
 */
public final class Teleport {
    private static final Logger LOGGER = LogUtils.getLogger();

    private Teleport() {}

    @Nullable
    public static String teleportToDimension(
            ServerPlayer player, ResourceKey<Level> dimension, double x, double y, double z) {
        MinecraftServer server = player.server;
        if (server == null) {
            return "no server";
        }

        if (dimension != Level.OVERWORLD
                && dimension != Level.NETHER
                && dimension != Level.END
                && server.registryAccess()
                        .registryOrThrow(Registries.LEVEL_STEM)
                        .getOptional(ResourceKey.create(Registries.LEVEL_STEM, dimension.location()))
                        .isEmpty()) {
            return "dimension not in registry — create a NEW world after datapack fix";
        }

        ServerLevel target = server.getLevel(dimension);
        if (target == null) {
            return "level not loaded (server has no ServerLevel for " + dimension.location() + ")";
        }

        int originX = (int) Math.floor(x);
        int originZ = (int) Math.floor(z);
        BlockPos safe = findSafeLanding(target, originX, originZ);
        double destX = safe.getX() + 0.5;
        double destY = safe.getY();
        double destZ = safe.getZ() + 0.5;

        try {
            // Touch nearby chunks first so client view distance can catch up without
            // waiting on a single overloaded gen thread (Utopia structures are deferred).
            preloadRadius(target, safe.getX() >> 4, safe.getZ() >> 4, 2);

            player.teleportTo(target, destX, destY, destZ, player.getYRot(), player.getXRot());
            player.resetFallDistance();
            player.setDeltaMovement(0.0, 0.0, 0.0);
            // Keep shared spawn on solid ground for future joins / respawn
            if (target.dimension().equals(ModDimensions.ISLANDS)
                    || needsSharedSpawnFix(target, safe)) {
                target.setDefaultSpawnPos(safe, 0.0F);
            }
            return null;
        } catch (Exception e) {
            LOGGER.error("Teleport to {} failed", dimension.location(), e);
            return "teleport exception: " + e.getClass().getSimpleName() + " — see log";
        }
    }

    /** Soft-touch chunks around dest (radius in chunks). Failures ignored. */
    private static void preloadRadius(ServerLevel level, int cx, int cz, int radius) {
        for (int dx = -radius; dx <= radius; dx++) {
            for (int dz = -radius; dz <= radius; dz++) {
                try {
                    level.getChunk(cx + dx, cz + dz);
                } catch (Exception ignored) {
                    // worldgen issues must not cancel teleport
                }
            }
        }
    }

    /**
     * Find feet position (air with solid below). Spiral from origin; emergency pad last.
     */
    public static BlockPos findSafeLanding(ServerLevel level, int originX, int originZ) {
        // Prefer current column first
        BlockPos local = tryColumn(level, originX, originZ);
        if (local != null) {
            return local;
        }

        // Expanding ring search (Aetherial islands can be sparse near 0,0)
        int[] radii = {8, 16, 32, 48, 64, 96, 128, 192, 256};
        for (int radius : radii) {
            int step = Math.max(4, radius / 8);
            for (int dx = -radius; dx <= radius; dx += step) {
                for (int dz = -radius; dz <= radius; dz += step) {
                    // Only check ring edge for speed (plus cross)
                    boolean edge = Math.abs(dx) == radius || Math.abs(dz) == radius
                            || dx == 0
                            || dz == 0;
                    if (!edge && radius > 16) {
                        continue;
                    }
                    BlockPos found = tryColumn(level, originX + dx, originZ + dz);
                    if (found != null) {
                        return found;
                    }
                }
            }
        }

        LOGGER.warn(
                "No solid ground near {},{} in {} — placing emergency pad",
                originX,
                originZ,
                level.dimension().location());
        return placeEmergencyPad(level, originX, originZ);
    }

    /**
     * @return feet BlockPos (player stands here) or null if column is void/unsafe
     */
    @Nullable
    private static BlockPos tryColumn(ServerLevel level, int x, int z) {
        try {
            level.getChunk(x >> 4, z >> 4);

            int surface = level.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, x, z);
            if (isStandableSurface(level, x, surface, z)) {
                return new BlockPos(x, surface, z);
            }

            // Manual scan from high — islands can sit mid-air with empty heightmap edge cases
            int maxY = Math.min(level.getMaxBuildHeight() - 2, 320);
            int minY = level.getMinBuildHeight() + 1;
            for (int y = maxY; y > minY; y--) {
                if (isStandableSurface(level, x, y, z)) {
                    return new BlockPos(x, y, z);
                }
            }
        } catch (Exception e) {
            LOGGER.debug("Column probe {},{} failed: {}", x, z, e.toString());
        }
        return null;
    }

    /** Feet at (x,y,z): that cell air/replaceable, head free, block below solid (not fluid). */
    private static boolean isStandableSurface(ServerLevel level, int x, int y, int z) {
        if (y <= level.getMinBuildHeight() || y >= level.getMaxBuildHeight() - 1) {
            return false;
        }
        BlockPos feet = new BlockPos(x, y, z);
        BlockPos head = feet.above();
        BlockPos below = feet.below();
        BlockState feetState = level.getBlockState(feet);
        BlockState headState = level.getBlockState(head);
        BlockState belowState = level.getBlockState(below);

        if (!feetState.isAir() && !feetState.canBeReplaced()) {
            return false;
        }
        if (!headState.isAir() && !headState.canBeReplaced()) {
            return false;
        }
        if (belowState.isAir() || belowState.canBeReplaced()) {
            return false;
        }
        if (!belowState.getFluidState().isEmpty()) {
            return false;
        }
        // Must actually block motion (not carpet-only edge cases)
        return belowState.isCollisionShapeFullBlock(level, below)
                || belowState.isSolidRender(level, below)
                || belowState.is(Blocks.GRASS_BLOCK)
                || belowState.is(Blocks.DIRT)
                || belowState.is(Blocks.STONE)
                || belowState.is(Blocks.SAND)
                || belowState.is(Blocks.GRAVEL)
                || belowState.is(Blocks.PODZOL)
                || belowState.is(Blocks.MYCELIUM)
                || belowState.is(Blocks.SNOW_BLOCK)
                || belowState.is(Blocks.NETHERRACK)
                || belowState.is(Blocks.END_STONE);
    }

    private static BlockPos placeEmergencyPad(ServerLevel level, int x, int z) {
        // Float mid-range where Aetherial islands often sit
        int y = Math.max(level.getSeaLevel() + 16, 80);
        y = Math.min(y, level.getMaxBuildHeight() - 8);
        level.getChunk(x >> 4, z >> 4);

        BlockState grass = Blocks.GRASS_BLOCK.defaultBlockState();
        BlockState dirt = Blocks.DIRT.defaultBlockState();
        for (int dx = -2; dx <= 2; dx++) {
            for (int dz = -2; dz <= 2; dz++) {
                BlockPos dirtPos = new BlockPos(x + dx, y - 2, z + dz);
                BlockPos grassPos = new BlockPos(x + dx, y - 1, z + dz);
                level.setBlock(dirtPos, dirt, 3);
                level.setBlock(grassPos, grass, 3);
                // Clear pad air
                for (int dy = 0; dy <= 2; dy++) {
                    BlockPos air = new BlockPos(x + dx, y + dy, z + dz);
                    if (!level.getBlockState(air).isAir()) {
                        level.setBlock(air, Blocks.AIR.defaultBlockState(), 3);
                    }
                }
            }
        }
        return new BlockPos(x, y, z);
    }

    private static boolean needsSharedSpawnFix(ServerLevel level, BlockPos safe) {
        BlockPos spawn = level.getSharedSpawnPos();
        return !isStandableSurface(level, spawn.getX(), spawn.getY(), spawn.getZ());
    }
}
