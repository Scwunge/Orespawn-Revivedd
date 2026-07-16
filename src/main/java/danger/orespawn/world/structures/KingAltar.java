package danger.orespawn.world.structures;

import danger.orespawn.init.ModBlocks;
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
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootTable;

/**
 * Gold {@code GenericDungeon.makeKingAltar}: 51×51 grass pad, four quartz columns,
 * quartz roof, stepped center platform with chest (gold: TheKingEgg).
 * <p>
 * Port omits the full RLE mural wall (huge gold {@code king[]} table) — keeps
 * geometry, columns, roof, and center altar. Places {@link ModBlocks#KING_SPAWNER}
 * on the peak so the boss can still be summoned.
 * <p>
 * Gold {@code DungeonSpawnerBlock} type <b>31</b>.
 */
public final class KingAltar {
    public static final int WIDTH = 51;
    public static final int HEIGHT = 48;
    public static final int COLUMN_HEIGHT = 44;

    public static final ResourceKey<LootTable> LOOT_TABLE = ResourceKey.create(
            Registries.LOOT_TABLE,
            ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "chests/king_altar"));

    private KingAltar() {}

    public static boolean makeKingAltar(WorldGenLevel level, RandomSource random, int cposx, int cposy, int cposz) {
        int minY = level.getMinBuildHeight();
        int maxY = level.getMaxBuildHeight();
        if (cposy < minY + 2 || cposy + HEIGHT + 10 >= maxY) {
            return false;
        }

        BlockState quartz = Blocks.QUARTZ_BLOCK.defaultBlockState();
        BlockState gold = Blocks.GOLD_BLOCK.defaultBlockState();
        BlockState emerald = Blocks.EMERALD_BLOCK.defaultBlockState();
        BlockState grass = Blocks.GRASS_BLOCK.defaultBlockState();
        BlockState dirt = Blocks.DIRT.defaultBlockState();
        // Gold clears a padded volume above the pad
        StructureHelper.clearBox(level, cposx - 5, cposy, cposz - 5, WIDTH + 10, HEIGHT + 11, WIDTH + 10);

        // Grass pad + dirt fill below air/water pockets
        for (int i = 0; i < WIDTH; i++) {
            for (int k = 0; k < WIDTH; k++) {
                StructureHelper.set(level, cposx + i, cposy, cposz + k, grass);
                for (int v = 1; v < 10; v++) {
                    BlockPos below = new BlockPos(cposx + i, cposy - v, cposz + k);
                    BlockState under = level.getBlockState(below);
                    if (under.isAir() || under.liquid()) {
                        StructureHelper.set(level, below, dirt);
                    }
                }
            }
        }

        makeColumn(level, cposx + 1, cposy + 1, cposz + 1, quartz, gold, emerald);
        makeColumn(level, cposx + WIDTH - 8, cposy + 1, cposz + WIDTH - 8, quartz, gold, emerald);
        makeColumn(level, cposx + 1, cposy + 1, cposz + WIDTH - 8, quartz, gold, emerald);
        makeColumn(level, cposx + WIDTH - 8, cposy + 1, cposz + 1, quartz, gold, emerald);

        // Double roof slab
        int roofY = cposy + HEIGHT - 1;
        for (int i = 0; i < WIDTH; i++) {
            for (int k = 0; k < WIDTH; k++) {
                StructureHelper.set(level, cposx + i, roofY, cposz + k, quartz);
            }
        }
        for (int i = -1; i <= WIDTH; i++) {
            for (int k = -1; k <= WIDTH; k++) {
                StructureHelper.set(level, cposx + i, roofY + 1, cposz + k, quartz);
            }
        }

        // Simple rear mural wall (stone / quartz checker — stand-in for gold RLE art)
        int wallX = cposx + 4;
        int wallY = cposy + 10;
        int wallZ = cposz + 9;
        for (int dy = 0; dy < 33; dy++) {
            for (int dz = 0; dz < 33; dz++) {
                BlockState tile = ((dy + dz) & 1) == 0 ? Blocks.STONE.defaultBlockState() : quartz;
                StructureHelper.set(level, wallX, wallY + dy, wallZ + dz, tile);
            }
        }
        // Gold frame with diamond + torches at corners
        for (int dz = 0; dz < 33; dz++) {
            StructureHelper.set(level, wallX, wallY - 1, wallZ + dz, gold);
            StructureHelper.set(level, wallX, wallY + 33, wallZ + dz, gold);
        }
        for (int dy = -1; dy <= 33; dy++) {
            StructureHelper.set(level, wallX, wallY + dy, wallZ - 1, gold);
            StructureHelper.set(level, wallX, wallY + dy, wallZ + 33, gold);
        }

        makeCenterAltar(level, random, cposx + WIDTH / 2, cposy, cposz + WIDTH / 2, quartz, gold);
        return true;
    }

    private static void makeColumn(
            WorldGenLevel level,
            int cposx,
            int cposy,
            int cposz,
            BlockState quartz,
            BlockState gold,
            BlockState emerald) {
        // Base / cap 7×7
        for (int i = 0; i < 7; i++) {
            for (int k = 0; k < 7; k++) {
                StructureHelper.set(level, cposx + i, cposy, cposz + k, quartz);
                StructureHelper.set(level, cposx + i, cposy + COLUMN_HEIGHT + 1, cposz + k, quartz);
            }
        }
        int ox = cposx + 1;
        int oz = cposz + 1;
        int oy = cposy + 1;
        for (int j = 0; j < COLUMN_HEIGHT; j++) {
            for (int i = 0; i < 5; i++) {
                for (int k = 0; k < 5; k++) {
                    BlockState bid = Blocks.AIR.defaultBlockState();
                    if (i == 0 || k == 0 || i == 4 || k == 4) {
                        bid = quartz;
                        int mod = j % 4;
                        if (mod == 0 && (i == 2 || k == 2)) {
                            bid = gold;
                        } else if (mod == 1 && (i == 1 || k == 1 || i == 3 || k == 3)) {
                            bid = gold;
                        } else if (mod == 2) {
                            if (i == 1 || k == 1 || i == 3 || k == 3) {
                                bid = gold;
                            }
                            if (i == 2 || k == 2) {
                                bid = emerald;
                            }
                        } else if (mod == 3 && (i == 1 || k == 1 || i == 3 || k == 3)) {
                            bid = gold;
                        }
                    }
                    StructureHelper.set(level, ox + i, oy + j, oz + k, bid);
                }
            }
        }
    }

    private static void makeCenterAltar(
            WorldGenLevel level,
            RandomSource random,
            int cx,
            int cy,
            int cz,
            BlockState quartz,
            BlockState gold) {
        // Stepped cross platforms (gold shrinking discs)
        placeRect(level, cx, cy, cz, 10, 10, quartz);
        placeRect(level, cx, cy, cz, 6, 20, quartz);
        placeRect(level, cx, cy, cz, 20, 6, quartz);

        placeRect(level, cx, cy + 1, cz, 8, 8, quartz);
        placeRect(level, cx, cy + 1, cz, 4, 18, quartz);
        placeRect(level, cx, cy + 1, cz, 18, 4, quartz);

        placeRect(level, cx, cy + 2, cz, 7, 7, quartz);
        // corner torches on this tier
        StructureHelper.set(level, cx + 7, cy + 3, cz + 7, ModBlocks.CRYSTAL_TORCH.get().defaultBlockState());
        StructureHelper.set(level, cx + 7, cy + 3, cz - 7, ModBlocks.CRYSTAL_TORCH.get().defaultBlockState());
        StructureHelper.set(level, cx - 7, cy + 3, cz + 7, ModBlocks.CRYSTAL_TORCH.get().defaultBlockState());
        StructureHelper.set(level, cx - 7, cy + 3, cz - 7, ModBlocks.CRYSTAL_TORCH.get().defaultBlockState());

        placeRect(level, cx, cy + 2, cz, 3, 17, quartz);
        placeRect(level, cx, cy + 2, cz, 17, 3, quartz);

        placeRect(level, cx, cy + 3, cz, 6, 6, quartz);
        placeRect(level, cx, cy + 3, cz, 2, 16, quartz);
        placeRect(level, cx, cy + 3, cz, 16, 2, quartz);

        placeRect(level, cx, cy + 4, cz, 2, 2, quartz);
        StructureHelper.set(level, cx + 2, cy + 5, cz + 2, ModBlocks.CRYSTAL_TORCH.get().defaultBlockState());
        StructureHelper.set(level, cx + 2, cy + 5, cz - 2, ModBlocks.CRYSTAL_TORCH.get().defaultBlockState());
        StructureHelper.set(level, cx - 2, cy + 5, cz + 2, ModBlocks.CRYSTAL_TORCH.get().defaultBlockState());
        StructureHelper.set(level, cx - 2, cy + 5, cz - 2, ModBlocks.CRYSTAL_TORCH.get().defaultBlockState());

        // Peak: king spawner seed + loot chest (gold had TheKingEgg in chest)
        BlockPos peak = new BlockPos(cx, cy + 4, cz);
        StructureHelper.set(level, peak, ModBlocks.KING_SPAWNER.get().defaultBlockState());
        BlockPos chestPos = new BlockPos(cx, cy + 5, cz);
        StructureHelper.set(
                level, chestPos, Blocks.CHEST.defaultBlockState().setValue(ChestBlock.FACING, Direction.SOUTH));
        RandomizableContainer.setBlockEntityLootTable(level, random, chestPos, LOOT_TABLE);
    }

    private static void placeRect(
            WorldGenLevel level, int cx, int cy, int cz, int halfX, int halfZ, BlockState state) {
        for (int i = -halfX; i <= halfX; i++) {
            for (int k = -halfZ; k <= halfZ; k++) {
                StructureHelper.set(level, cx + i, cy, cz + k, state);
            }
        }
    }
}
