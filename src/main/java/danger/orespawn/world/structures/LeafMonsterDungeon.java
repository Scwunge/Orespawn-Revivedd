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
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.LadderBlock;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootTable;

/**
 * Gold {@code GenericDungeon.makeLeafMonsterDungeon}: oak log tower with ladder
 * shaft, leaf canopy room, four Leaf Monster spawners, dual chests.
 * <p>
 * Gold {@code DungeonSpawnerBlock} type <b>15</b>. Origin is the SW corner of the
 * 4×4 tower footprint.
 */
public final class LeafMonsterDungeon {
    /** Tower footprint (gold {@code 0.3}). */
    public static final int TOWER = 4;

    /** Canopy half-extent beyond tower: loops {@code -3.6}. */
    public static final int CANOPY_MIN = -3;

    public static final int CANOPY_MAX = 6;

    /** Footprint width of canopy AABB. */
    public static final int WIDTH = CANOPY_MAX - CANOPY_MIN + 1;

    /** Foundation dig depth under tower. */
    public static final int FOUNDATION = 4;

    /** Highest block is leaf tip at {@code y+16}. */
    public static final int HEIGHT = 17;

    public static final ResourceKey<LootTable> LOOT_TABLE = ResourceKey.create(
            Registries.LOOT_TABLE,
            ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "chests/leaf_monster_dungeon"));

    private LeafMonsterDungeon() {}

    /**
     * Places the leaf monster dungeon with tower corner at {@code (cposx, cposy, cposz)}.
     *
     * @return true if written
     */
    public static boolean makeLeafMonsterDungeon(
            WorldGenLevel level, RandomSource random, int cposx, int cposy, int cposz) {
        int minY = level.getMinBuildHeight();
        int maxY = level.getMaxBuildHeight();
        if (cposy - FOUNDATION < minY || cposy + HEIGHT >= maxY) {
            return false;
        }

        BlockState air = Blocks.AIR.defaultBlockState();
        BlockState log = Blocks.OAK_LOG.defaultBlockState();
        BlockState leaves = Blocks.OAK_LEAVES.defaultBlockState().setValue(LeavesBlock.PERSISTENT, true);
        // gold ladder meta 2 = facing north
        BlockState ladder =
                Blocks.LADDER.defaultBlockState().setValue(LadderBlock.FACING, Direction.NORTH);

        // Clear full canopy + tower + porch volume (incl. foundation under tower)
        StructureHelper.clearBox(
                level,
                cposx + CANOPY_MIN,
                cposy - FOUNDATION,
                cposz + CANOPY_MIN,
                WIDTH,
                HEIGHT + FOUNDATION,
                WIDTH);

        // Gold: clear porch/approach air box (i=-2.5, k=-3.1, j=0.3)
        for (int i = -2; i < 6; i++) {
            for (int k = -3; k < 2; k++) {
                for (int j = 0; j < 4; j++) {
                    StructureHelper.set(level, cposx + i, cposy + j, cposz + k, air);
                }
            }
        }

        // Gold: fill foundation under 4×4 with logs if was air/tallgrass (cleared → always log)
        for (int i = 0; i < TOWER; i++) {
            for (int k = 0; k < TOWER; k++) {
                for (int j = -1; j > -5; j--) {
                    StructureHelper.set(level, cposx + i, cposy + j, cposz + k, log);
                }
            }
        }

        // Gold: 4×4 tower shell j=0.9 with door/ladder shaft cutouts
        for (int i = 0; i < TOWER; i++) {
            for (int k = 0; k < TOWER; k++) {
                for (int j = 0; j < 10; j++) {
                    // gold: skip placement on ladder column (k==2, i==1|2) — ladders later
                    if (k == 2 && (i == 1 || i == 2)) {
                        continue;
                    }
                    BlockState blk = log;
                    // door front at low levels: air when j<2 && k in {0,1} && i in {1,2}
                    if (j < 2 && (k == 0 || k == 1) && (i == 1 || i == 2)) {
                        blk = air;
                    }
                    // open corridor k==1 for i==1|2
                    if (k == 1 && (i == 1 || i == 2)) {
                        blk = air;
                    }
                    StructureHelper.set(level, cposx + i, cposy + j, cposz + k, blk);
                }
            }
        }

        // Gold: ladders on k==2, i==1|2 for j=0.9 (meta 2 → NORTH)
        for (int i = 1; i <= 2; i++) {
            for (int j = 0; j < 10; j++) {
                StructureHelper.set(level, cposx + i, cposy + j, cposz + 2, ladder);
            }
        }

        // Gold: porch leaves
        StructureHelper.set(level, cposx + 1, cposy + 2, cposz - 1, leaves);
        StructureHelper.set(level, cposx + 2, cposy + 2, cposz - 1, leaves);

        // Gold: canopy floor at j=9 outside tower
        for (int i = CANOPY_MIN; i <= CANOPY_MAX; i++) {
            for (int k = CANOPY_MIN; k <= CANOPY_MAX; k++) {
                if (i >= 0 && i <= 3 && k >= 0 && k <= 3) {
                    continue;
                }
                BlockState blk = log;
                if (i == CANOPY_MIN || i == CANOPY_MAX || k == CANOPY_MIN || k == CANOPY_MAX) {
                    blk = leaves;
                }
                StructureHelper.set(level, cposx + i, cposy + 9, cposz + k, blk);
            }
        }

        // Gold: leaf walls j=10.12
        for (int i = CANOPY_MIN; i <= CANOPY_MAX; i++) {
            for (int k = CANOPY_MIN; k <= CANOPY_MAX; k++) {
                for (int j = 10; j < 13; j++) {
                    BlockState blk = air;
                    if (i == CANOPY_MIN || i == CANOPY_MAX || k == CANOPY_MIN || k == CANOPY_MAX) {
                        blk = leaves;
                    }
                    StructureHelper.set(level, cposx + i, cposy + j, cposz + k, blk);
                }
            }
        }

        // Gold: j=13 log/leaf step-in
        for (int i = -2; i < 6; i++) {
            for (int k = -2; k < 6; k++) {
                BlockState blk = air;
                if (i == -2 || i == 5 || k == -2 || k == 5) {
                    blk = log;
                }
                if (i == -1 || i == 4 || k == -1 || k == 4) {
                    blk = leaves;
                }
                StructureHelper.set(level, cposx + i, cposy + 13, cposz + k, blk);
            }
        }

        // Gold: j=14 leaf cap
        for (int i = -1; i < 5; i++) {
            for (int k = -1; k < 5; k++) {
                StructureHelper.set(level, cposx + i, cposy + 14, cposz + k, leaves);
            }
        }

        // Gold: j=15 log 4×4
        for (int i = 0; i < TOWER; i++) {
            for (int k = 0; k < TOWER; k++) {
                StructureHelper.set(level, cposx + i, cposy + 15, cposz + k, log);
            }
        }

        // Gold: j=16 leaf tip 2×2
        for (int i = 1; i < 3; i++) {
            for (int k = 1; k < 3; k++) {
                StructureHelper.set(level, cposx + i, cposy + 16, cposz + k, leaves);
            }
        }

        // Gold: 4 Leaf Monster spawners in canopy corners at y+10
        placeSpawner(level, random, cposx - 2, cposy + 10, cposz - 2);
        placeSpawner(level, random, cposx + 5, cposy + 10, cposz + 5);
        placeSpawner(level, random, cposx - 2, cposy + 10, cposz + 5);
        placeSpawner(level, random, cposx + 5, cposy + 10, cposz - 2);

        // Gold: dual chests at (1,10,5) and (2,10,5)
        placeChest(level, random, cposx + 1, cposy + 10, cposz + 5, Direction.NORTH);
        placeChest(level, random, cposx + 2, cposy + 10, cposz + 5, Direction.NORTH);

        return true;
    }

    private static void placeSpawner(WorldGenLevel level, RandomSource random, int x, int y, int z) {
        BlockPos pos = new BlockPos(x, y, z);
        StructureHelper.set(level, pos, Blocks.SPAWNER.defaultBlockState());
        if (level.getBlockEntity(pos) instanceof SpawnerBlockEntity spawner) {
            EntityType<?> type =
                    ModEntities.LEAF_MONSTER != null ? ModEntities.LEAF_MONSTER.get() : EntityType.ZOMBIE;
            spawner.setEntityId(type, random);
        }
    }

    private static void placeChest(
            WorldGenLevel level, RandomSource random, int x, int y, int z, Direction facing) {
        BlockPos pos = new BlockPos(x, y, z);
        StructureHelper.set(
                level, pos, Blocks.CHEST.defaultBlockState().setValue(ChestBlock.FACING, facing));
        RandomizableContainer.setBlockEntityLootTable(level, random, pos, LOOT_TABLE);
    }
}
