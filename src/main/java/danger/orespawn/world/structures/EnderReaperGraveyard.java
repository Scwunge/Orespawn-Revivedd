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
 * Gold {@code GenericDungeon.makeEnderReaperGraveyard}: 11×13 end-stone pad with iron-bar
 * fence walls (y+1.y+4), four corner Ender Reaper spawners, eight graves (obsidian head/
 * foot + chest).
 * <p>
 * Gold {@code DungeonSpawnerBlock} type <b>18</b>. Origin is the SW corner of the pad.
 */
public final class EnderReaperGraveyard {
    /** Gold {@code width = 11} (X). */
    public static final int WIDTH = 11;

    /** Gold {@code length = 13} (Z). */
    public static final int LENGTH = 13;

    /** Fence walls y+1.y+4 plus foundation down 4. */
    public static final int HEIGHT = 5;

    /** Foundation depth below origin (gold fills air j=1.4 down with end stone). */
    public static final int FOUNDATION = 4;

    public static final ResourceKey<LootTable> LOOT_TABLE = ResourceKey.create(
            Registries.LOOT_TABLE,
            ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "chests/ender_reaper_graveyard"));

    private EnderReaperGraveyard() {}

    /**
     * Places the ender reaper graveyard with SW corner at {@code (cposx, cposy, cposz)}.
     *
     * @return true if written
     */
    public static boolean makeEnderReaperGraveyard(
            WorldGenLevel level, RandomSource random, int cposx, int cposy, int cposz) {
        int minY = level.getMinBuildHeight();
        int maxY = level.getMaxBuildHeight();
        if (cposy - FOUNDATION < minY || cposy + HEIGHT >= maxY) {
            return false;
        }

        // Clear pad + walls + foundation volume
        StructureHelper.clearBox(
                level,
                cposx,
                cposy - FOUNDATION,
                cposz,
                WIDTH,
                HEIGHT + FOUNDATION,
                LENGTH);

        BlockState endStone = Blocks.END_STONE.defaultBlockState();
        BlockState ironBars = Blocks.IRON_BARS.defaultBlockState();
        BlockState air = Blocks.AIR.defaultBlockState();
        BlockState obsidian = Blocks.OBSIDIAN.defaultBlockState();

        // Foundation under pad (gold only filled existing air; port always fills after clear)
        for (int j = 1; j <= FOUNDATION; j++) {
            for (int i = 0; i < WIDTH; i++) {
                for (int k = 0; k < LENGTH; k++) {
                    StructureHelper.set(level, cposx + i, cposy - j, cposz + k, endStone);
                }
            }
        }

        // Floor
        for (int i = 0; i < WIDTH; i++) {
            for (int k = 0; k < LENGTH; k++) {
                StructureHelper.set(level, cposx + i, cposy, cposz + k, endStone);
            }
        }

        // Iron bar walls y+1.y+4, air interior
        for (int j = 1; j < 5; j++) {
            for (int i = 0; i < WIDTH; i++) {
                for (int k = 0; k < LENGTH; k++) {
                    BlockState blk = air;
                    if (i == 0 || k == 0 || i == WIDTH - 1 || k == LENGTH - 1) {
                        blk = ironBars;
                    }
                    StructureHelper.set(level, cposx + i, cposy + j, cposz + k, blk);
                }
            }
        }

        // Four corner Ender Reaper spawners
        placeSpawner(level, random, cposx + 1, cposy + 1, cposz + 1);
        placeSpawner(level, random, cposx + WIDTH - 2, cposy + 1, cposz + LENGTH - 2);
        placeSpawner(level, random, cposx + 1, cposy + 1, cposz + LENGTH - 2);
        placeSpawner(level, random, cposx + WIDTH - 2, cposy + 1, cposz + 1);

        // Graves (gold makeAGrave offsets)
        makeAGrave(level, random, cposx, cposy, cposz, 1, 6, obsidian);
        makeAGrave(level, random, cposx, cposy, cposz, 3, 4, obsidian);
        makeAGrave(level, random, cposx, cposy, cposz, 5, 4, obsidian);
        makeAGrave(level, random, cposx, cposy, cposz, 7, 4, obsidian);
        makeAGrave(level, random, cposx, cposy, cposz, 3, 8, obsidian);
        makeAGrave(level, random, cposx, cposy, cposz, 5, 8, obsidian);
        makeAGrave(level, random, cposx, cposy, cposz, 7, 8, obsidian);
        makeAGrave(level, random, cposx, cposy, cposz, 9, 6, obsidian);

        return true;
    }

    /** Gold {@code makeAGrave}: headstone + footstone + chest with grave loot. */
    private static void makeAGrave(
            WorldGenLevel level,
            RandomSource random,
            int cposx,
            int cposy,
            int cposz,
            int xoff,
            int zoff,
            BlockState obsidian) {
        StructureHelper.set(level, cposx + xoff, cposy + 1, cposz + zoff - 1, obsidian);
        StructureHelper.set(level, cposx + xoff, cposy, cposz + zoff + 1, obsidian);

        BlockPos chestPos = new BlockPos(cposx + xoff, cposy, cposz + zoff);
        StructureHelper.set(
                level,
                chestPos,
                Blocks.CHEST.defaultBlockState().setValue(ChestBlock.FACING, Direction.SOUTH));
        RandomizableContainer.setBlockEntityLootTable(level, random, chestPos, LOOT_TABLE);
    }

    private static void placeSpawner(WorldGenLevel level, RandomSource random, int x, int y, int z) {
        BlockPos pos = new BlockPos(x, y, z);
        StructureHelper.set(level, pos, Blocks.SPAWNER.defaultBlockState());
        if (level.getBlockEntity(pos) instanceof SpawnerBlockEntity spawner) {
            spawner.setEntityId(ModEntities.ENDER_REAPER.get(), random);
        }
    }
}
