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
 * Gold {@code GenericDungeon.makeMantisHive}: inverted stepped shell (width 13→1),
 * gold-ore / emerald-ore ring walls, wall chests on mid layers, triple Mantis spawners
 * at the tip.
 * <p>
 * Gold {@code DungeonSpawnerBlock} type <b>6</b>. Origin is the top SW corner of the
 * full-width layer.
 */
public final class MantisHive {
    /** Gold {@code width = 13}. */
    public static final int WIDTH = 13;

    /**
     * Layers down (width 13,11,.,1 → 7 steps). Lowest layer at {@code y - (LAYERS-1)}.
     */
    public static final int LAYERS = (WIDTH + 1) / 2;

    /** Vertical span of the stepped shell inclusive. */
    public static final int HEIGHT = LAYERS;

    public static final ResourceKey<LootTable> LOOT_TABLE = ResourceKey.create(
            Registries.LOOT_TABLE,
            ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "chests/mantis_hive"));

    private MantisHive() {}

    /**
     * Places a mantis hive with top-layer SW corner at {@code (cposx, cposy, cposz)}.
     *
     * @return true if written
     */
    public static boolean makeMantisHive(
            WorldGenLevel level, RandomSource random, int cposx, int cposy, int cposz) {
        int minY = level.getMinBuildHeight();
        int maxY = level.getMaxBuildHeight();
        int bottom = cposy - (LAYERS - 1);
        // Spawners sit up to tip+6 which reaches cposy; gold also cleared 20 up for air
        if (bottom < minY || cposy + 6 >= maxY) {
            return false;
        }

        // Full AABB of stepped shell + small tip-spawner column
        StructureHelper.clearBox(level, cposx, bottom, cposz, WIDTH, HEIGHT + 6, WIDTH);

        int width = WIDTH;
        int yoff = 0;
        int xoff = 0;
        int zoff = 0;

        // Gold even yoff → gold_ore; odd → emerald_ore
        while (width > 0) {
            BlockState wall = (yoff & 1) != 0
                    ? Blocks.EMERALD_ORE.defaultBlockState()
                    : Blocks.GOLD_ORE.defaultBlockState();
            for (int i = 0; i < width; i++) {
                for (int k = 0; k < width; k++) {
                    if (k != 0 && k != width - 1 && i != 0 && i != width - 1) {
                        StructureHelper.set(
                                level,
                                cposx + i + xoff,
                                cposy - yoff,
                                cposz + k + zoff,
                                Blocks.AIR.defaultBlockState());
                    } else {
                        StructureHelper.set(
                                level, cposx + i + xoff, cposy - yoff, cposz + k + zoff, wall);
                    }
                }
            }

            // Chests on mid bands width 7.11
            if (width <= 11 && width >= 7) {
                fillMantisChests(level, random, cposx + xoff, cposy - yoff, cposz + zoff, width);
            }

            xoff++;
            zoff++;
            yoff++;
            width -= 2;
        }

        // Gold: after loop xoff/zoff/yoff overshot by one — step back for tip center
        xoff--;
        zoff--;
        yoff--;

        // Triple Mantis spawners stacked above tip center (gold z uses yoff)
        for (int j = 4; j < 7; j++) {
            placeSpawner(level, random, cposx + xoff, cposy + j - yoff, cposz + yoff);
        }

        return true;
    }

    private static void fillMantisChests(
            WorldGenLevel level, RandomSource random, int ox, int oy, int oz, int width) {
        placeChest(level, random, ox + 1, oy, oz + width / 2, Direction.EAST);
        placeChest(level, random, ox + width - 2, oy, oz + width / 2, Direction.WEST);
        placeChest(level, random, ox + width / 2, oy, oz + 1, Direction.SOUTH);
        placeChest(level, random, ox + width / 2, oy, oz + width - 2, Direction.NORTH);
    }

    private static void placeSpawner(WorldGenLevel level, RandomSource random, int x, int y, int z) {
        BlockPos pos = new BlockPos(x, y, z);
        StructureHelper.set(level, pos, Blocks.SPAWNER.defaultBlockState());
        if (level.getBlockEntity(pos) instanceof SpawnerBlockEntity spawner) {
            spawner.setEntityId(ModEntities.MANTIS.get(), random);
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
