package danger.orespawn.world;

import danger.orespawn.init.ModDimensions;
import danger.orespawn.util.Reference;
import danger.orespawn.world.structures.KingAltar;
import danger.orespawn.world.structures.QueenAltar;
import danger.orespawn.world.structures.UtopiaTrees;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.levelgen.Heightmap;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.level.ChunkEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

/**
 * Gold Utopia surface structures — <b>deferred</b> so teleports do not freeze.
 * <p>
 * Chunk load only enqueues work. At most one structure per server tick is placed.
 * Sky trees are height-capped (gold 190+ froze chunk gen / limited view distance).
 */
@EventBusSubscriber(modid = Reference.MOD_ID)
public final class UtopiaDimEvents {
    private UtopiaDimEvents() {}

    private static final int MAX_QUEUE = 48;
    /** Structures placed per server tick (keep 1 — teleports load many chunks at once). */
    private static final int GEN_BUDGET_PER_TICK = 1;

    private static final Queue<QueuedChunk> QUEUE = new ConcurrentLinkedQueue<>();
    private static int recentlyPlaced;

    private record QueuedChunk(ResourceKey<Level> dimension, ChunkPos pos) {}

    @SubscribeEvent
    public static void onChunkLoad(ChunkEvent.Load event) {
        if (!(event.getLevel() instanceof ServerLevel level)) {
            return;
        }
        if (level.dimension() != ModDimensions.UTOPIA) {
            return;
        }
        if (!event.isNewChunk()) {
            return;
        }
        if (!(event.getChunk() instanceof LevelChunk chunk)) {
            return;
        }
        // Do NOT generate here — blocks chunk pipeline and freezes teleports
        if (QUEUE.size() >= MAX_QUEUE) {
            return;
        }
        QUEUE.offer(new QueuedChunk(level.dimension(), chunk.getPos()));
    }

    @SubscribeEvent
    public static void onServerTick(ServerTickEvent.Post event) {
        if (QUEUE.isEmpty()) {
            return;
        }
        if (recentlyPlaced > 0) {
            recentlyPlaced--;
        }

        int budget = GEN_BUDGET_PER_TICK;
        while (budget > 0 && !QUEUE.isEmpty()) {
            QueuedChunk job = QUEUE.poll();
            if (job == null) {
                break;
            }
            ServerLevel level = event.getServer().getLevel(job.dimension());
            if (level == null || level.dimension() != ModDimensions.UTOPIA) {
                continue;
            }
            // Chunk may have unloaded
            if (!level.hasChunk(job.pos().x, job.pos().z)) {
                continue;
            }
            try {
                if (generateForChunk(level, job.pos())) {
                    budget--;
                }
            } catch (Exception e) {
                // Never let structure spam crash the server mid-teleport
                Reference.MOD_ID.length(); // keep util import used if logger not available
                danger.orespawn.OreSpawnMain.LOGGER.warn(
                        "Utopia structure gen failed at chunk {},{}: {}",
                        job.pos().x,
                        job.pos().z,
                        e.toString());
            }
        }
    }

    /**
     * @return true if something substantial was placed (consumes tick budget)
     */
    private static boolean generateForChunk(ServerLevel level, ChunkPos pos) {
        RandomSource random = level.getRandom();
        int chunkX = pos.getMinBlockX();
        int chunkZ = pos.getMinBlockZ();

        // Skip if global cooldown (after big structure)
        if (recentlyPlaced > 10 && random.nextInt(3) != 0) {
            return false;
        }

        if (tryHugeTree(level, random, chunkX, chunkZ)) {
            recentlyPlaced = 30;
            return true;
        }
        if (tryWindOrSky(level, random, chunkX, chunkZ)) {
            recentlyPlaced = 15;
            return true;
        }
        if (recentlyPlaced == 0 && tryKingOrQueenAltar(level, random, chunkX, chunkZ)) {
            recentlyPlaced = 60;
            return true;
        }
        return false;
    }

    private static boolean tryHugeTree(ServerLevel level, RandomSource random, int chunkX, int chunkZ) {
        // Gold ~1/50; keep moderate
        if (random.nextInt(45) != 0) {
            return false;
        }
        int posX = 4 + chunkX + random.nextInt(8);
        int posZ = 4 + chunkZ + random.nextInt(8);
        BlockPos grass = findGrass(level, posX, posZ);
        if (grass == null) {
            return false;
        }
        if (random.nextInt(100) == 0) {
            return UtopiaTrees.makeGoodnessTree(level, random, grass.getX(), grass.getY(), grass.getZ());
        }
        return UtopiaTrees.makeHugeOakTree(level, random, grass.getX(), grass.getY(), grass.getZ());
    }

    private static boolean tryWindOrSky(ServerLevel level, RandomSource random, int chunkX, int chunkZ) {
        // Gold ~1/30 — rarer than before to reduce load
        if (random.nextInt(28) != 0) {
            return false;
        }
        int posX = 3 + chunkX + random.nextInt(10);
        int posZ = 3 + chunkZ + random.nextInt(10);
        BlockPos grass = findGrass(level, posX, posZ);
        if (grass == null) {
            return false;
        }
        // Prefer wind (cheap); sky less often (still tall)
        if (random.nextInt(4) == 0) {
            return UtopiaTrees.makeSkyTree(level, random, grass.getX(), grass.getY(), grass.getZ());
        }
        return UtopiaTrees.makeWindTree(
                level, random, grass.getX(), grass.getY(), grass.getZ(), random.nextInt(4));
    }

    private static boolean tryKingOrQueenAltar(
            ServerLevel level, RandomSource random, int chunkX, int chunkZ) {
        // Gold 1/2000; keep rare so teleports don't queue altar spam
        if (random.nextInt(1800) != 1) {
            return false;
        }
        int posX = 3 + chunkX + random.nextInt(10);
        int posZ = 3 + chunkZ + random.nextInt(10);
        BlockPos grass = findGrass(level, posX, posZ);
        if (grass == null) {
            return false;
        }
        if (!hasClearance(level, grass, 48, 24)) {
            return false;
        }
        int ox = grass.getX() - KingAltar.WIDTH / 2;
        int oy = grass.getY();
        int oz = grass.getZ() - KingAltar.WIDTH / 2;
        if (random.nextBoolean()) {
            return KingAltar.makeKingAltar(level, random, ox, oy, oz);
        }
        return QueenAltar.makeQueenAltar(level, random, ox, oy, oz);
    }

    private static BlockPos findGrass(ServerLevel level, int x, int z) {
        try {
            level.getChunk(x >> 4, z >> 4);
            int surface = level.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, x, z);
            if (surface > level.getMinBuildHeight() + 2 && surface < level.getMaxBuildHeight()) {
                BlockPos ground = new BlockPos(x, surface - 1, z);
                if (level.getBlockState(ground).is(Blocks.GRASS_BLOCK)) {
                    return ground;
                }
            }
            // Fallback scan (bounded)
            int maxY = Math.min(level.getMaxBuildHeight() - 2, 160);
            int minY = Math.max(level.getMinBuildHeight() + 2, 40);
            for (int y = maxY; y > minY; y--) {
                BlockPos above = new BlockPos(x, y, z);
                BlockPos ground = above.below();
                if (level.getBlockState(above).isAir() && level.getBlockState(ground).is(Blocks.GRASS_BLOCK)) {
                    return ground;
                }
            }
        } catch (Exception ignored) {
            // chunk not ready
        }
        return null;
    }

    private static boolean hasClearance(ServerLevel level, BlockPos grass, int up, int radius) {
        int gx = grass.getX();
        int gy = grass.getY();
        int gz = grass.getZ();
        // Sparse sample only
        for (int dy = 8; dy <= up; dy += 8) {
            for (int dx = -radius; dx <= radius; dx += 12) {
                for (int dz = -radius; dz <= radius; dz += 12) {
                    BlockPos p = new BlockPos(gx + dx, gy + dy, gz + dz);
                    if (p.getY() >= level.getMaxBuildHeight()) {
                        return false;
                    }
                    if (!level.getBlockState(p).isAir() && !level.getBlockState(p).canBeReplaced()) {
                        return false;
                    }
                }
            }
        }
        return true;
    }
}
