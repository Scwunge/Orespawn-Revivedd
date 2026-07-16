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
 * Gold {@code GenericDungeon.makeBeeHive}: 10×30 wood/log shaft hanging downward
 * from origin, 4 Bee spawners, wall chests every 2 levels.
 * <p>
 * Gold {@code DungeonSpawnerBlock} type <b>4</b>.
 */
public final class BeeHive {
    public static final int WIDTH = 10;
    public static final int HEIGHT = 30;

    public static final ResourceKey<LootTable> LOOT_TABLE = ResourceKey.create(
            Registries.LOOT_TABLE,
            ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "chests/bee_hive"));

    private BeeHive() {}

    /**
     * Origin is the top of the hive; structure extends downward.
     *
     * @return true if written
     */
    public static boolean makeBeeHive(WorldGenLevel level, RandomSource random, int cposx, int cposy, int cposz) {
        int minY = level.getMinBuildHeight();
        int maxY = level.getMaxBuildHeight();
        int bottom = cposy - HEIGHT;
        if (bottom < minY + 1 || cposy >= maxY) {
            return false;
        }

        // Clear hanging volume + top few layers (gold clears y.y-4 first)
        StructureHelper.clearBox(level, cposx, bottom, cposz, WIDTH, HEIGHT + 1, WIDTH);

        BlockState oakLog = Blocks.OAK_LOG.defaultBlockState();
        BlockState oakPlanks = Blocks.OAK_PLANKS.defaultBlockState();

        // Floor at bottom of shaft
        for (int i = 0; i < WIDTH; i++) {
            for (int k = 0; k < WIDTH; k++) {
                StructureHelper.set(level, cposx + i, bottom, cposz + k, oakLog);
            }
        }

        // Walls + hollow interior from bottom+1 up to cposy-1
        for (int j = 1; j < HEIGHT; j++) {
            int y = cposy - j;
            for (int i = 0; i < WIDTH; i++) {
                for (int k = 0; k < WIDTH; k++) {
                    if (i != 0 && k != 0 && i != WIDTH - 1 && k != WIDTH - 1) {
                        StructureHelper.set(level, cposx + i, y, cposz + k, Blocks.AIR.defaultBlockState());
                    } else {
                        // gold: alternate log / planks on odd levels
                        BlockState wall = (j & 1) == 1 ? oakPlanks : oakLog;
                        StructureHelper.set(level, cposx + i, y, cposz + k, wall);
                    }
                }
            }
        }

        // 4 Bee spawners spaced down the shaft
        int midX = cposx + WIDTH / 2;
        int midZ = cposz + WIDTH / 2;
        for (int s = 0; s < 4; s++) {
            int sy = cposy - 2 - s * (HEIGHT / 4);
            if (sy <= bottom) {
                continue;
            }
            BlockPos spawnerPos = new BlockPos(midX, sy, midZ);
            StructureHelper.set(level, spawnerPos, Blocks.SPAWNER.defaultBlockState());
            if (level.getBlockEntity(spawnerPos) instanceof SpawnerBlockEntity spawner) {
                spawner.setEntityId(ModEntities.BEE.get(), random);
            }
        }

        // Wall chests every 2 levels
        for (int j = 2; j < HEIGHT - 1; j += 2) {
            int cy = cposy - j;
            if (cy <= bottom) {
                continue;
            }
            placeChest(level, random, cposx + 1, cy, cposz + WIDTH / 2, Direction.EAST);
            placeChest(level, random, cposx + WIDTH - 2, cy, cposz + WIDTH / 2, Direction.WEST);
            placeChest(level, random, cposx + WIDTH / 2, cy, cposz + 1, Direction.SOUTH);
            placeChest(level, random, cposx + WIDTH / 2, cy, cposz + WIDTH - 2, Direction.NORTH);
        }

        return true;
    }

    private static void placeChest(
            WorldGenLevel level, RandomSource random, int x, int y, int z, Direction facing) {
        BlockPos pos = new BlockPos(x, y, z);
        BlockState chest = Blocks.CHEST.defaultBlockState().setValue(ChestBlock.FACING, facing);
        StructureHelper.set(level, pos, chest);
        RandomizableContainer.setBlockEntityLootTable(level, random, pos, LOOT_TABLE);
    }
}
