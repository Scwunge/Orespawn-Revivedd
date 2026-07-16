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
 * Gold {@code GenericDungeon.makeSmallBeeHive}: 7×21 hanging wood hive (smaller than
 * {@link BeeHive}), platform at 2/3 height with hanging planks, short tower above,
 * west doorway, 3 stacked Bee spawners, one center chest.
 * <p>
 * Gold {@code DungeonSpawnerBlock} type <b>8</b>.
 */
public final class SmallBeeHive {
    public static final int WIDTH = 7;
    public static final int HEIGHT = 21;

    public static final ResourceKey<LootTable> LOOT_TABLE = ResourceKey.create(
            Registries.LOOT_TABLE,
            ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "chests/small_bee_hive"));

    private SmallBeeHive() {}

    /**
     * Places a small bee hive with corner at {@code (cposx, cposy, cposz)}.
     * Gold builds upward from origin (platform at {@code height * 2/3}).
     *
     * @return true if the structure was written
     */
    public static boolean makeSmallBeeHive(
            WorldGenLevel level, RandomSource random, int cposx, int cposy, int cposz) {
        int minY = level.getMinBuildHeight();
        int maxY = level.getMaxBuildHeight();
        int platformY = HEIGHT * 2 / 3; // 14
        if (cposy < minY + 1 || cposy + HEIGHT + 2 >= maxY) {
            return false;
        }

        BlockState air = Blocks.AIR.defaultBlockState();
        BlockState oakLog = Blocks.OAK_LOG.defaultBlockState();
        BlockState oakPlanks = Blocks.OAK_PLANKS.defaultBlockState();

        // Clear top canopy volume (gold clears y+[height*2/3 . height) with ±3 margin)
        StructureHelper.clearBox(
                level, cposx - 3, cposy + platformY, cposz - 3, WIDTH + 6, HEIGHT - platformY + 2, WIDTH + 6);
        // Clear hanging volume below platform
        StructureHelper.clearBox(level, cposx, cposy, cposz, WIDTH, platformY + 1, WIDTH);

        // Platform floor + hanging planks (gold field_150360_v log / field_150341_Y planks)
        for (int i = 0; i < WIDTH; i++) {
            for (int k = 0; k < WIDTH; k++) {
                StructureHelper.set(level, cposx + i, cposy + platformY, cposz + k, oakLog);

                int hang = random.nextInt(HEIGHT / 3);
                hang *= 2;
                hang -= Math.abs(i - WIDTH / 2);
                hang -= Math.abs(k - WIDTH / 2);
                if (hang < 1) {
                    hang = 1;
                }
                // Center column hangs full platform depth
                if (i == WIDTH / 2 && k == WIDTH / 2) {
                    hang = platformY;
                }
                for (int h = 0; h < hang; h++) {
                    StructureHelper.set(
                            level, cposx + i, cposy + platformY - h, cposz + k, oakPlanks);
                }
            }
        }

        // Tower rising from platform: alternating inner width / outer width rings
        int j = platformY;
        int ringPairs = HEIGHT / 6; // finite
        for (int blk = 0; blk < ringPairs; blk++) {
            j++;
            // Inner 7×7 walls
            for (int i = 0; i < WIDTH; i++) {
                for (int k = 0; k < WIDTH; k++) {
                    if (k != 0 && i != 0 && k != WIDTH - 1 && i != WIDTH - 1) {
                        StructureHelper.set(level, cposx + i, cposy + j, cposz + k, air);
                    } else {
                        StructureHelper.set(level, cposx + i, cposy + j, cposz + k, oakLog);
                    }
                }
            }

            j++;
            // Outer 9×9 walls (margin 1)
            for (int i = -1; i < WIDTH + 1; i++) {
                for (int k = -1; k < WIDTH + 1; k++) {
                    if (k != -1 && i != -1 && k != WIDTH && i != WIDTH) {
                        StructureHelper.set(level, cposx + i, cposy + j, cposz + k, air);
                    } else {
                        StructureHelper.set(level, cposx + i, cposy + j, cposz + k, oakLog);
                    }
                }
            }
        }

        // Cap roof
        j++;
        for (int i = 0; i < WIDTH; i++) {
            for (int k = 0; k < WIDTH; k++) {
                StructureHelper.set(level, cposx + i, cposy + j, cposz + k, oakLog);
            }
        }

        // West doorway opening at platform+1 (gold -1.0 x, 2.3 z, 3 tall)
        int doorBase = platformY + 1;
        for (int dx = -1; dx < 1; dx++) {
            for (int dz = 2; dz < 4; dz++) {
                StructureHelper.set(level, cposx + dx, cposy + doorBase, cposz + dz, air);
                StructureHelper.set(level, cposx + dx, cposy + doorBase + 1, cposz + dz, air);
                StructureHelper.set(level, cposx + dx, cposy + doorBase + 2, cposz + dz, air);
            }
        }

        // 3 stacked Bee spawners (gold cposx+1, doorBase+0.2, cposz+1)
        for (int s = 0; s < 3; s++) {
            BlockPos spawnerPos = new BlockPos(cposx + 1, cposy + doorBase + s, cposz + 1);
            StructureHelper.set(level, spawnerPos, Blocks.SPAWNER.defaultBlockState());
            if (level.getBlockEntity(spawnerPos) instanceof SpawnerBlockEntity spawner) {
                spawner.setEntityId(ModEntities.BEE.get(), random);
            }
        }

        // Center chest on platform floor+1
        BlockPos chestPos = new BlockPos(cposx + WIDTH / 2, cposy + doorBase, cposz + WIDTH / 2);
        StructureHelper.set(
                level, chestPos, Blocks.CHEST.defaultBlockState().setValue(ChestBlock.FACING, Direction.WEST));
        RandomizableContainer.setBlockEntityLootTable(level, random, chestPos, LOOT_TABLE);

        return true;
    }
}
