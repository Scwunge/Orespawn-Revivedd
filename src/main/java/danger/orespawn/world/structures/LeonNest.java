package danger.orespawn.world.structures;

import danger.orespawn.init.ModEntities;
import danger.orespawn.util.Reference;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootTable;

/**
 * Gold {@code GenericDungeon.makeLeonNest}: hemispherical nest bowl (radius 10)
 * of mixed leaves/log/planks/dirt/cobble/lapis shell, air interior, air cleared
 * above, Leon spawner in the bowl.
 * <p>
 * Gold {@code DungeonSpawnerBlock} type <b>32</b>. Origin is the nest rim center.
 */
public final class LeonNest {
    /** Gold {@code rad = 10}. */
    public static final int RADIUS = 10;

    /** Gold clears {@code y+1.y+5} above the rim to air. */
    public static final int CLEAR_ABOVE = 5;

    /**
     * Spawner depth under rim: gold {@code y - (rad - 4)} → {@code y - 6}.
     */
    public static final int SPAWNER_DEPTH = RADIUS - 4;

    public static final ResourceKey<LootTable> LOOT_TABLE = ResourceKey.create(
            Registries.LOOT_TABLE,
            ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "chests/leon_nest"));

    private LeonNest() {}

    /**
     * Places a Leon nest centered on {@code (x, y, z)} (rim height).
     * Structure extends downward to {@code y - RADIUS}.
     *
     * @return true if written
     */
    public static boolean makeLeonNest(WorldGenLevel level, RandomSource random, int x, int y, int z) {
        int minY = level.getMinBuildHeight();
        int maxY = level.getMaxBuildHeight();
        int bottom = y - RADIUS;
        int top = y + CLEAR_ABOVE;
        if (bottom < minY || top >= maxY) {
            return false;
        }

        int size = RADIUS * 2 + 1;
        // Clear full AABB first so underground stone never remains in the bowl / air pad
        StructureHelper.clearBox(level, x - RADIUS, bottom, z - RADIUS, size, RADIUS + 1 + CLEAR_ABOVE, size);

        BlockState air = Blocks.AIR.defaultBlockState();

        // Gold: hemisphere shell downward from rim (j = 0.rad)
        for (int j = 0; j <= RADIUS; j++) {
            for (int i = -RADIUS; i <= RADIUS; i++) {
                for (int k = -RADIUS; k <= RADIUS; k++) {
                    int dist = (int) Math.sqrt((double) j * j + (double) i * i + (double) k * k);
                    if (dist > RADIUS) {
                        continue;
                    }
                    BlockState bid = air;
                    if (dist >= RADIUS - 2) {
                        bid = nestShellBlock(random);
                    }
                    StructureHelper.set(level, x + i, y - j, z + k, bid);
                }
            }
        }

        // Gold: clear air above rim (y+1.y+5) over full horizontal disk AABB
        for (int j = 1; j <= CLEAR_ABOVE; j++) {
            for (int i = -RADIUS; i <= RADIUS; i++) {
                for (int k = -RADIUS; k <= RADIUS; k++) {
                    StructureHelper.set(level, x + i, y + j, z + k, air);
                }
            }
        }

        // Gold: spawner at (x, y - (rad - 4), z) — Leonopteryx → LEON
        int sy = y - SPAWNER_DEPTH;
        BlockPos spawnerPos = new BlockPos(x, sy, z);
        StructureHelper.set(level, spawnerPos, Blocks.SPAWNER.defaultBlockState());
        if (level.getBlockEntity(spawnerPos) instanceof SpawnerBlockEntity spawner) {
            // LEON is registered; fallback hostile if ever absent at runtime
            EntityType<?> type = ModEntities.LEON != null ? ModEntities.LEON.get() : EntityType.ZOMBIE;
            spawner.setEntityId(type, random);
        }

        return true;
    }

    /** Gold which 0.5: leaves, log, planks, dirt, cobble, lapis. */
    private static BlockState nestShellBlock(RandomSource random) {
        return switch (random.nextInt(6)) {
            case 0 -> Blocks.OAK_LEAVES.defaultBlockState().setValue(LeavesBlock.PERSISTENT, true);
            case 1 -> Blocks.OAK_LOG.defaultBlockState();
            case 2 -> Blocks.OAK_PLANKS.defaultBlockState();
            case 3 -> Blocks.DIRT.defaultBlockState();
            case 4 -> Blocks.COBBLESTONE.defaultBlockState();
            default -> Blocks.LAPIS_BLOCK.defaultBlockState();
        };
    }
}
