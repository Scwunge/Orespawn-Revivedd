package danger.orespawn.world.structures;

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
 * Gold {@code GenericDungeon.makeEnderKnightDungeon} (bytecode-reconstructed): eastward
 * obsidian corridor with end-stone west wall panels, random shelves (chest / bookshelf /
 * cobweb), dual Ender Knight spawners mid-hall.
 * <p>
 * Gold vertical size is the {@code width=6} axis (Y), segment Z-depth is the varying
 * {@code height} local. Gold {@code DungeonSpawnerBlock} type <b>11</b>.
 * <p>
 * Origin is the west clear-pad SW corner (Y base / Z base of the entry clear).
 */
public final class EnderKnightDungeon {
    /**
     * Gold local {@code width = 6} — vertical span of the corridor (Y size).
     */
    public static final int HEIGHT = 6;

    /** Gold clear pad Z depth before floor segment. */
    public static final int ENTRY_DEPTH = 5;

    /** Hallway Z depth (gold height=9 segment). */
    public static final int HALL_DEPTH = 9;

    /** Room Z depth (gold height=7 segments). */
    public static final int ROOM_DEPTH = 7;

    /** End pad Z depth (gold height=5). */
    public static final int END_DEPTH = 5;

    /**
     * X columns from entry clear through end pad:
     * 4 clear + 1 floor + 1 room + 5 hall + 1 room + 1 end = 13.
     */
    public static final int LENGTH_X = 13;

    /**
     * Z span: hallway sits at {@code z-2} with depth 9 → {@code z-2 . z+6}.
     * Footprint depth for clearBox notes.
     */
    public static final int WIDTH_Z = 9;

    /** Alias for structure docs (max horizontal Z span). */
    public static final int WIDTH = WIDTH_Z;

    public static final ResourceKey<LootTable> LOOT_TABLE = ResourceKey.create(
            Registries.LOOT_TABLE,
            ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "chests/ender_knight_dungeon"));

    private EnderKnightDungeon() {}

    /**
     * Places the ender knight dungeon starting at {@code (cposx, cposy, cposz)}.
     *
     * @return true if written
     */
    public static boolean makeEnderKnightDungeon(
            WorldGenLevel level, RandomSource random, int cposx, int cposy, int cposz) {
        int minY = level.getMinBuildHeight();
        int maxY = level.getMaxBuildHeight();
        if (cposy < minY || cposy + HEIGHT >= maxY) {
            return false;
        }

        // Clear full corridor AABB (Z starts 2 west of origin for hall inset)
        StructureHelper.clearBox(
                level, cposx, cposy, cposz - 2, LENGTH_X, HEIGHT, WIDTH_Z);

        int x = cposx;
        int y = cposy;
        int z = cposz;

        // 1) Clear 4 entry slices (5 high × 5 deep) stepping +X
        for (int step = 0; step < 4; step++) {
            for (int k = 0; k < ENTRY_DEPTH; k++) {
                for (int i = 0; i < 5; i++) {
                    StructureHelper.set(
                            level, x, y + i, z + k, Blocks.AIR.defaultBlockState());
                }
            }
            x++;
        }

        // 2) Obsidian floor pad with air doorway (k=2, i=1.3)
        placeSolidSlice(level, x, y, z, HEIGHT, ENTRY_DEPTH, true);
        x++;
        z--;

        // 3) First room (depth 7) with shelves
        placeWallSlice(level, random, x, y, z, HEIGHT, ROOM_DEPTH, true);
        z--;

        // 4) Hallway 5 layers (depth 9); spawners on middle layer at k=4
        for (int layer = 0; layer < 5; layer++) {
            x++;
            placeWallSlice(level, random, x, y, z, HEIGHT, HALL_DEPTH, true);
            if (layer == 2) {
                int k = 4;
                placeSpawner(level, random, x, y + 2, z + k);
                placeSpawner(level, random, x, y + 3, z + k);
            }
        }

        // 5) Second room
        z++;
        x++;
        placeWallSlice(level, random, x, y, z, HEIGHT, ROOM_DEPTH, true);

        // 6) Solid end pad
        z++;
        x++;
        placeSolidSlice(level, x, y, z, HEIGHT, END_DEPTH, false);

        return true;
    }

    /**
     * Solid or doorway pad. Gold floor: all obsidian except optional mid air hole.
     */
    private static void placeSolidSlice(
            WorldGenLevel level,
            int x,
            int y,
            int z,
            int heightY,
            int depthZ,
            boolean doorway) {
        BlockState obsidian = Blocks.OBSIDIAN.defaultBlockState();
        BlockState air = Blocks.AIR.defaultBlockState();
        for (int k = 0; k < depthZ; k++) {
            for (int i = 0; i < heightY; i++) {
                BlockState blk = obsidian;
                if (doorway && k == 2 && i >= 1 && i <= 3) {
                    blk = air;
                }
                StructureHelper.set(level, x, y + i, z + k, blk);
            }
        }
    }

    /**
     * Hollow corridor slice: obsidian shell, end-stone on west (i==0) mid-Y panels.
     * Shelves on k = 1,2,depth-3,depth-2.
     */
    private static void placeWallSlice(
            WorldGenLevel level,
            RandomSource random,
            int x,
            int y,
            int z,
            int heightY,
            int depthZ,
            boolean shelves) {
        BlockState air = Blocks.AIR.defaultBlockState();
        BlockState obsidian = Blocks.OBSIDIAN.defaultBlockState();
        BlockState endStone = Blocks.END_STONE.defaultBlockState();

        for (int k = 0; k < depthZ; k++) {
            for (int i = 0; i < heightY; i++) {
                BlockState blk = air;
                if (i == 0 || i == heightY - 1) {
                    blk = obsidian;
                }
                if (i == 0 && k > 0 && k < depthZ - 1) {
                    blk = endStone;
                }
                if (k == 0 || k == depthZ - 1) {
                    blk = obsidian;
                }
                StructureHelper.set(level, x, y + i, z + k, blk);
            }

            if (shelves
                    && (k == 1
                            || k == 2
                            || k == depthZ - 3
                            || k == depthZ - 2)) {
                makeShelves(level, random, x, y + 1, z + k);
            }
        }
    }

    /**
     * Gold {@code makeShelves}: 25% chest, 25% bookshelf stack, 25% cobweb stack, 25% empty.
     */
    private static void makeShelves(
            WorldGenLevel level, RandomSource random, int x, int y, int z) {
        int roll = random.nextInt(4);
        if (roll == 0) {
            placeChest(level, random, x, y, z, Direction.EAST);
        } else if (roll == 1) {
            BlockState bookshelf = Blocks.BOOKSHELF.defaultBlockState();
            int h = 1 + random.nextInt(4);
            for (int j = 0; j < h; j++) {
                StructureHelper.set(level, x, y + j, z, bookshelf);
            }
        } else if (roll == 2) {
            BlockState web = Blocks.COBWEB.defaultBlockState();
            int h = 1 + random.nextInt(4);
            for (int j = 0; j < h; j++) {
                StructureHelper.set(level, x, y + j, z, web);
            }
        }
        // roll == 3: empty
    }

    private static void placeSpawner(WorldGenLevel level, RandomSource random, int x, int y, int z) {
        BlockPos pos = new BlockPos(x, y, z);
        StructureHelper.set(level, pos, Blocks.SPAWNER.defaultBlockState());
        if (level.getBlockEntity(pos) instanceof SpawnerBlockEntity spawner) {
            spawner.setEntityId(ModEntities.ENDER_KNIGHT.get(), random);
        }
    }

    private static void placeChest(
            WorldGenLevel level, RandomSource random, int x, int y, int z, Direction facing) {
        BlockPos pos = new BlockPos(x, y, z);
        BlockState chest = Blocks.CHEST.defaultBlockState().setValue(ChestBlock.FACING, facing);
        StructureHelper.set(level, pos, chest);
        RandomizableContainer.setBlockEntityLootTable(level, random, pos, LOOT_TABLE);
    }
}
