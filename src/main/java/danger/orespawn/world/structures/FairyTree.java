package danger.orespawn.world.structures;

import danger.orespawn.init.ModBlocks;
import danger.orespawn.init.ModEntities;
import danger.orespawn.util.Reference;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.RandomizableContainer;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootTable;

/**
 * Gold {@code Trees.FairyTree}: 2×2 crystal-log trunk, eight crystal branches with
 * leaves, Fairy spawner west of base, crystal-loot chest east of base.
 * <p>
 * Gold {@code DungeonSpawnerBlock} type <b>0</b> calls this structure
 * ({@code OreSpawnMain.OreSpawnTrees.FairyTree}).
 * <p>
 * All growth lengths are gold-finite ({@code 4+rand(4)} / {@code 5+rand(5)}); no
 * recursive growth — structure stays bounded like gold.
 */
public final class FairyTree {
    /**
     * Conservative max Y span above origin for bounds checks.
     * Gold: trunk to {@code y+5}, upper trunk up to {@code +5+rand(5)} from j=6
     * (max y+14), branches climb at most one grow segment (max 7) from y+6 → ~y+13,
     * leaves +1. Pad for safety.
     */
    public static final int MAX_HEIGHT = 20;

    /**
     * Approximate half-radius for docs: branch segments sum to ≤ ~25 horizontal +
     * leaf pad ±2.
     */
    public static final int MAX_RADIUS = 30;

    public static final ResourceKey<LootTable> LOOT_TABLE = ResourceKey.create(
            Registries.LOOT_TABLE,
            ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "chests/fairy_tree"));

    private FairyTree() {}

    /**
     * Places a Fairy Tree rooted at {@code (x, y, z)} (gold trunk starts at y+1).
     *
     * @return true if the structure was written
     */
    public static boolean makeFairyTree(WorldGenLevel level, RandomSource random, int x, int y, int z) {
        int minY = level.getMinBuildHeight();
        int maxY = level.getMaxBuildHeight();
        if (y < minY || y + MAX_HEIGHT >= maxY) {
            return false;
        }

        BlockState log = ModBlocks.CRYSTAL_TREE_LOG.get().defaultBlockState();

        // Gold: 2×2 trunk for j = 1.5
        for (int j = 1; j < 6; j++) {
            for (int i = 0; i < 2; i++) {
                for (int k = 0; k < 2; k++) {
                    setFast(level, x + i, y + j, z + k, log);
                }
            }
        }

        // Gold: eight crystal branches at y+5 and y+6 on the 2×2 trunk corners
        growCrystalBranch(level, random, x, y + 5, z, 0, 1, 1, 1, -1);
        growCrystalBranch(level, random, x + 1, y + 5, z, 1, 0, 1, -1, -1);
        growCrystalBranch(level, random, x, y + 5, z + 1, -1, 0, -1, 1, -1);
        growCrystalBranch(level, random, x + 1, y + 5, z + 1, 0, -1, -1, -1, -1);
        growCrystalBranch(level, random, x, y + 6, z, 0, 1, -1, 1, -1);
        growCrystalBranch(level, random, x + 1, y + 6, z, 1, 0, 1, 1, -1);
        growCrystalBranch(level, random, x, y + 6, z + 1, -1, 0, -1, -1, -1);
        growCrystalBranch(level, random, x + 1, y + 6, z + 1, 0, -1, 1, -1, -1);

        // Gold: upper trunk grow = 5 + nextInt(5), j from 6 inclusive
        int grow = 5 + random.nextInt(5);
        for (int j = 6; j < 6 + grow; j++) {
            for (int i = 0; i < 2; i++) {
                for (int k = 0; k < 2; k++) {
                    setFast(level, x + i, y + j, z + k, log);
                    makeCrystalLeaves(level, x + i, y + j, z + k);
                }
            }
        }

        // Gold: Fairy spawner at (x-1, y+1, z)
        placeSpawner(level, random, new BlockPos(x - 1, y + 1, z), ModEntities.FAIRY.get());

        // Gold: chest at (x+2, y+1, z) with CrystalChestContentsList (1 + nextInt(5) items)
        BlockPos chestPos = new BlockPos(x + 2, y + 1, z);
        BlockState chestState = Blocks.CHEST.defaultBlockState().setValue(ChestBlock.FACING, Direction.WEST);
        level.setBlock(chestPos, chestState, 2);
        RandomizableContainer.setBlockEntityLootTable(level, random, chestPos, LOOT_TABLE);

        return true;
    }

    /**
     * Gold {@code Trees.make_crystal_leaves}: ±2 X/Z, Y and Y+1, air only →
     * {@code MyCrystalLeaves3}.
     */
    private static void makeCrystalLeaves(WorldGenLevel level, int x, int y, int z) {
        BlockState leaves = ModBlocks.CRYSTAL_TREE_LEAVES_3.get().defaultBlockState();
        if (leaves.hasProperty(net.minecraft.world.level.block.LeavesBlock.DISTANCE)) {
            leaves = leaves.setValue(net.minecraft.world.level.block.LeavesBlock.DISTANCE, 1);
        }
        if (leaves.hasProperty(net.minecraft.world.level.block.LeavesBlock.PERSISTENT)) {
            leaves = leaves.setValue(net.minecraft.world.level.block.LeavesBlock.PERSISTENT, false);
        }
        for (int l1 = -2; l1 <= 2; l1++) {
            for (int l2 = -2; l2 <= 2; l2++) {
                for (int l3 = 0; l3 <= 1; l3++) {
                    BlockPos pos = new BlockPos(x + l1, y + l3, z + l2);
                    if (level.getBlockState(pos).isAir()) {
                        level.setBlock(pos, leaves, 2);
                    }
                }
            }
        }
    }

    /**
     * Gold {@code Trees.grow_crystal_branch}: five finite grow loops (no recursion).
     * Gold {@code LessLag} shrink is omitted (default lag mode 0).
     */
    private static void growCrystalBranch(
            WorldGenLevel level,
            RandomSource random,
            int x,
            int y,
            int z,
            int xdir,
            int zdir,
            int xxdir,
            int zzdir,
            int ydir) {
        BlockState log = ModBlocks.CRYSTAL_TREE_LOG.get().defaultBlockState();

        int i = x;
        int j = y;
        int k = z;
        int i2 = 0;
        int k2 = 0;

        // Segment 1: climb diagonally — grow 4+rand(4)
        int grow = 4 + random.nextInt(4);
        for (int n = 0; n < grow; n++) {
            setFast(level, i, j, k, log);
            makeCrystalLeaves(level, i, j, k);
            j++;
            i += xdir;
            k += zdir;
            i2 = i;
            k2 = k;
        }

        // Segment 2: continue primary direction flat — grow 5+rand(5)
        grow = 5 + random.nextInt(5);
        for (int n = 0; n < grow; n++) {
            setFast(level, i, j, k, log);
            makeCrystalLeaves(level, i, j, k);
            i += xdir;
            k += zdir;
        }

        // Segment 3: secondary fork from saved (i2,k2) at current height — grow 5+rand(5)
        grow = 5 + random.nextInt(5);
        for (int n = 0; n < grow; n++) {
            setFast(level, i2, j, k2, log);
            makeCrystalLeaves(level, i2, j, k2);
            i2 += xxdir;
            k2 += zzdir;
        }

        // Segment 4: primary tip with ydir (gold --j then loop)
        int j2 = --j;
        grow = 4 + random.nextInt(4);
        for (int n = 0; n < grow; n++) {
            setFast(level, i, j, k, log);
            makeCrystalLeaves(level, i, j, k);
            i += xdir;
            k += zdir;
            j += ydir;
        }

        // Segment 5: secondary tip with ydir
        grow = 4 + random.nextInt(4);
        for (int n = 0; n < grow; n++) {
            setFast(level, i2, j2, k2, log);
            makeCrystalLeaves(level, i2, j2, k2);
            i2 += xxdir;
            k2 += zzdir;
            j2 += ydir;
        }
    }

    private static void setFast(WorldGenLevel level, int x, int y, int z, BlockState state) {
        int minY = level.getMinBuildHeight();
        int maxY = level.getMaxBuildHeight();
        if (y < minY || y >= maxY) {
            return;
        }
        level.setBlock(new BlockPos(x, y, z), state, 2);
    }

    private static void placeSpawner(
            WorldGenLevel level, RandomSource random, BlockPos pos, net.minecraft.world.entity.EntityType<?> type) {
        level.setBlock(pos, Blocks.SPAWNER.defaultBlockState(), 2);
        if (level.getBlockEntity(pos) instanceof SpawnerBlockEntity spawner) {
            spawner.setEntityId(type, random);
        }
    }
}
