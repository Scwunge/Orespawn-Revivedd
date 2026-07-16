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
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootTable;

/**
 * Gold {@code GenericDungeon.makeMiniDungeon}: 10×7×10 iron-bar shell with cobble
 * floor/corners, grass stepped roof, west stair ramp, corner pillars with spawners,
 * interior Terrible Terror / Butterfly / Lurking Terror spawners, one chest.
 * <p>
 * Gold {@code DungeonSpawnerBlock} type <b>16</b>.
 */
public final class MiniDungeon {
    /** Main room footprint (gold loop i/k 0.9). */
    public static final int WIDTH = 10;
    /** Main shell height (gold j 0.6). */
    public static final int HEIGHT = 7;
    /** Max Y offset used (corner pillars + top spawners reach y+11). */
    public static final int TOTAL_HEIGHT = 12;
    /** Stair ramp extends this many blocks west of origin. */
    public static final int STAIR_EXTENT = 6;

    public static final ResourceKey<LootTable> LOOT_TABLE = ResourceKey.create(
            Registries.LOOT_TABLE,
            ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "chests/mini_dungeon"));

    private MiniDungeon() {}

    /**
     * Places a mini dungeon with corner at {@code (cposx, cposy, cposz)}.
     *
     * @return true if the structure was written
     */
    public static boolean makeDungeon(WorldGenLevel level, RandomSource random, int cposx, int cposy, int cposz) {
        int minY = level.getMinBuildHeight();
        int maxY = level.getMaxBuildHeight();
        if (cposy < minY || cposy + TOTAL_HEIGHT >= maxY) {
            return false;
        }

        BlockState air = Blocks.AIR.defaultBlockState();
        BlockState cobble = Blocks.COBBLESTONE.defaultBlockState();
        BlockState ironBars = Blocks.IRON_BARS.defaultBlockState();
        BlockState grass = Blocks.GRASS_BLOCK.defaultBlockState();
        BlockState planks = Blocks.OAK_PLANKS.defaultBlockState();
        BlockState fence = Blocks.OAK_FENCE.defaultBlockState();
        BlockState torch = Blocks.TORCH.defaultBlockState();

        // Clear main volume + stair corridor west of room (underground stone must go first)
        StructureHelper.clearBox(level, cposx, cposy, cposz, WIDTH, TOTAL_HEIGHT, WIDTH);
        StructureHelper.clearBox(
                level, cposx - STAIR_EXTENT, cposy, cposz, STAIR_EXTENT, HEIGHT + 2, WIDTH);

        // Main 10×7×10 shell (gold lines 3666–3700)
        for (int i = 0; i < WIDTH; i++) {
            for (int k = 0; k < WIDTH; k++) {
                for (int j = 0; j < HEIGHT; j++) {
                    BlockState blk = air;
                    boolean edge = i == 0 || k == 0 || i == WIDTH - 1 || k == WIDTH - 1;
                    if (edge) {
                        blk = ironBars;
                    }
                    // Cobble corners
                    if ((i == 0 || i == WIDTH - 1) && (k == 0 || k == WIDTH - 1)) {
                        blk = cobble;
                    }
                    // Floor
                    if (j == 0) {
                        blk = cobble;
                    }
                    // Top wall ring on edges
                    if (j == HEIGHT - 1 && edge) {
                        blk = cobble;
                    }
                    StructureHelper.set(level, cposx + i, cposy + j, cposz + k, blk);
                }
            }
        }

        // Grass ring at y+7 (inset 1)
        for (int i = 1; i < WIDTH - 1; i++) {
            for (int k = 1; k < WIDTH - 1; k++) {
                BlockState blk = air;
                if (i == 1 || i == WIDTH - 2 || k == 1 || k == WIDTH - 2) {
                    blk = grass;
                }
                StructureHelper.set(level, cposx + i, cposy + 7, cposz + k, blk);
            }
        }

        // Grass ring at y+8 (inset 2)
        for (int i = 2; i < WIDTH - 2; i++) {
            for (int k = 2; k < WIDTH - 2; k++) {
                BlockState blk = air;
                if (i == 2 || i == WIDTH - 3 || k == 2 || k == WIDTH - 3) {
                    blk = grass;
                }
                StructureHelper.set(level, cposx + i, cposy + 8, cposz + k, blk);
            }
        }

        // West stair ramp (gold x from -6, rises 6 steps; planks + fence rails + torches)
        int sx = -STAIR_EXTENT;
        int sy = 1;
        int sz = 3;
        for (int m = 0; m < STAIR_EXTENT; m++) {
            StructureHelper.set(level, cposx + sx, cposy + sy, cposz + sz, planks);
            StructureHelper.set(level, cposx + sx, cposy + sy, cposz + sz + 1, planks);
            StructureHelper.set(level, cposx + sx, cposy + sy, cposz + sz + 2, planks);
            StructureHelper.set(level, cposx + sx, cposy + sy, cposz + sz + 3, planks);
            StructureHelper.set(level, cposx + sx, cposy + sy + 1, cposz + sz, fence);
            StructureHelper.set(level, cposx + sx, cposy + sy + 1, cposz + sz + 3, fence);
            StructureHelper.set(level, cposx + sx, cposy + sy + 2, cposz + sz, torch);
            StructureHelper.set(level, cposx + sx, cposy + sy + 2, cposz + sz + 3, torch);
            sx++;
            sy++;
        }

        // Butterfly spawners on grass ring rim at y+9 (gold 3.6 edges)
        for (int i = 3; i < 7; i++) {
            for (int k = 3; k < 7; k++) {
                if (i == 3 || i == 6 || k == 3 || k == 6) {
                    placeSpawner(level, random, cposx + i, cposy + 9, cposz + k, ModEntities.BUTTERFLY.get());
                }
            }
        }

        // Corner cobble pillars y+7.10 with top spawners (gold)
        placeCornerPillar(level, random, cposx + 0, cposy, cposz + 0, ModEntities.TERRIBLE_TERROR.get());
        placeCornerPillar(level, random, cposx + 9, cposy, cposz + 9, ModEntities.BUTTERFLY.get());
        placeCornerPillar(level, random, cposx + 0, cposy, cposz + 9, ModEntities.TERRIBLE_TERROR.get());
        placeCornerPillar(level, random, cposx + 9, cposy, cposz + 0, ModEntities.BUTTERFLY.get());

        // Interior floor spawners (gold y+1)
        placeSpawner(level, random, cposx + 1, cposy + 1, cposz + 1, ModEntities.TERRIBLE_TERROR.get());
        placeSpawner(level, random, cposx + 8, cposy + 1, cposz + 8, ModEntities.TERRIBLE_TERROR.get());
        placeSpawner(level, random, cposx + 8, cposy + 1, cposz + 1, ModEntities.BUTTERFLY.get());
        placeSpawner(level, random, cposx + 1, cposy + 1, cposz + 8, ModEntities.BUTTERFLY.get());
        placeSpawner(level, random, cposx + 4, cposy + 1, cposz + 4, ModEntities.LURKING_TERROR.get());
        placeSpawner(level, random, cposx + 5, cposy + 1, cposz + 5, ModEntities.LURKING_TERROR.get());

        // Single chest (gold cposx+3, y+1, cposz+3)
        BlockPos chestPos = new BlockPos(cposx + 3, cposy + 1, cposz + 3);
        StructureHelper.set(
                level, chestPos, Blocks.CHEST.defaultBlockState().setValue(ChestBlock.FACING, Direction.SOUTH));
        RandomizableContainer.setBlockEntityLootTable(level, random, chestPos, LOOT_TABLE);

        return true;
    }

    private static void placeCornerPillar(
            WorldGenLevel level, RandomSource random, int x, int cposy, int z, EntityType<?> mob) {
        BlockState cobble = Blocks.COBBLESTONE.defaultBlockState();
        for (int j = 7; j < 11; j++) {
            StructureHelper.set(level, x, cposy + j, z, cobble);
        }
        placeSpawner(level, random, x, cposy + 11, z, mob);
    }

    private static void placeSpawner(
            WorldGenLevel level, RandomSource random, int x, int y, int z, EntityType<?> mob) {
        BlockPos pos = new BlockPos(x, y, z);
        StructureHelper.set(level, pos, Blocks.SPAWNER.defaultBlockState());
        if (level.getBlockEntity(pos) instanceof SpawnerBlockEntity spawner) {
            spawner.setEntityId(mob, random);
        }
    }
}
