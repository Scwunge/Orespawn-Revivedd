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
 * Gold {@code GenericDungeon.makeWaterDragonLair} — cylindrical bedrock/diamond ring
 * walls, sand floor, center tree, Water Dragon spawners, chest.
 * Gold dungeon spawner type <b>13</b>.
 */
public final class WaterDragonLair {
    public static final float RADIUS = 10.0F;
    public static final int HEIGHT = 8;

    public static final ResourceKey<LootTable> LOOT_TABLE = ResourceKey.create(
            Registries.LOOT_TABLE,
            ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "chests/water_dragon_lair"));

    private WaterDragonLair() {}

    public static boolean makeWaterDragonLair(
            WorldGenLevel level, RandomSource random, int cposx, int cposy, int cposz) {
        int minY = level.getMinBuildHeight();
        int maxY = level.getMaxBuildHeight();
        if (cposy - 1 < minY || cposy + HEIGHT >= maxY) {
            return false;
        }

        StructureHelper.clearCylinder(level, cposx, cposy, cposz, RADIUS + 1.0F, HEIGHT + 1);

        BlockState bedrock = Blocks.BEDROCK.defaultBlockState();
        BlockState diamond = Blocks.DIAMOND_BLOCK.defaultBlockState();
        BlockState gold = Blocks.GOLD_BLOCK.defaultBlockState();
        BlockState netherrack = Blocks.NETHERRACK.defaultBlockState();
        BlockState sand = Blocks.SAND.defaultBlockState();
        BlockState stone = Blocks.STONE.defaultBlockState();
        BlockState leaves = Blocks.OAK_LEAVES.defaultBlockState()
                .setValue(net.minecraft.world.level.block.LeavesBlock.PERSISTENT, true);
        BlockState log = Blocks.OAK_LOG.defaultBlockState();
        BlockState air = Blocks.AIR.defaultBlockState();

        // Roof disc y+7
        for (float currad = 0.0F; currad < RADIUS; currad += 0.33F) {
            for (float curdeg = 0.0F; curdeg < 360.0F; curdeg += 5.0F) {
                float curx = (float) (currad * Math.cos(Math.toRadians(curdeg)));
                float curz = (float) (currad * Math.sin(Math.toRadians(curdeg)));
                BlockState blk = bedrock;
                if (currad > 5.0F && currad < 6.0F) {
                    blk = diamond;
                }
                StructureHelper.set(
                        level,
                        (int) (cposx + curx + 0.5F),
                        cposy + 7,
                        (int) (cposz + curz + 0.5F),
                        blk);
            }
        }
        // Diamond cross on roof
        for (int i = 1; i < 10; i++) {
            StructureHelper.set(level, cposx + i, cposy + 7, cposz, diamond);
            StructureHelper.set(level, cposx - i, cposy + 7, cposz, diamond);
            StructureHelper.set(level, cposx, cposy + 7, cposz + i, diamond);
            StructureHelper.set(level, cposx, cposy + 7, cposz - i, diamond);
        }
        StructureHelper.set(level, cposx, cposy + 7, cposz, air);
        StructureHelper.set(level, cposx + 1, cposy + 7, cposz, netherrack);
        StructureHelper.set(level, cposx - 1, cposy + 7, cposz, netherrack);
        StructureHelper.set(level, cposx, cposy + 7, cposz + 1, netherrack);
        StructureHelper.set(level, cposx, cposy + 7, cposz - 1, netherrack);

        // Ring walls y+1.6
        for (float curdeg = 0.0F; curdeg < 360.0F; curdeg += 5.0F) {
            float curx = (float) (RADIUS * Math.cos(Math.toRadians(curdeg)));
            float curz = (float) (RADIUS * Math.sin(Math.toRadians(curdeg)));
            int wx = (int) (cposx + curx + 0.5F);
            int wz = (int) (cposz + curz + 0.5F);
            StructureHelper.set(level, wx, cposy + 1, wz, netherrack);
            // Gold mixed gold_block with water-dragon egg-ore; use gold block stand-in
            StructureHelper.set(level, wx, cposy + 2, wz, gold);
            StructureHelper.set(level, wx, cposy + 3, wz, gold);
            StructureHelper.set(level, wx, cposy + 4, wz, netherrack);
            StructureHelper.set(level, wx, cposy + 5, wz, bedrock);
            StructureHelper.set(level, wx, cposy + 6, wz, bedrock);
        }

        // Sand floor + stone pad
        for (int i = -3; i <= 3; i++) {
            for (int j = -3; j <= 3; j++) {
                StructureHelper.set(level, cposx + i, cposy, cposz + j, sand);
                StructureHelper.set(level, cposx + i, cposy - 1, cposz + j, stone);
            }
        }
        for (int i = -2; i <= 2; i++) {
            for (int j = -2; j <= 2; j++) {
                StructureHelper.set(level, cposx + i, cposy + 3, cposz + j, leaves);
            }
        }
        StructureHelper.set(level, cposx, cposy + 4, cposz, leaves);
        StructureHelper.set(level, cposx, cposy + 3, cposz, log);
        StructureHelper.set(level, cposx, cposy + 2, cposz, log);
        StructureHelper.set(level, cposx, cposy + 1, cposz, log);
        StructureHelper.set(level, cposx + 1, cposy + 3, cposz + 1, log);
        StructureHelper.set(level, cposx - 1, cposy + 3, cposz - 1, log);
        StructureHelper.set(level, cposx + 1, cposy + 3, cposz - 1, log);
        StructureHelper.set(level, cposx - 1, cposy + 3, cposz + 1, log);

        placeSpawner(level, random, cposx + 1, cposy + 3, cposz);
        placeSpawner(level, random, cposx - 1, cposy + 3, cposz);
        placeSpawner(level, random, cposx, cposy + 3, cposz + 1);
        placeSpawner(level, random, cposx, cposy + 3, cposz - 1);

        BlockPos chestPos = new BlockPos(cposx, cposy + 1, cposz - 1);
        StructureHelper.set(
                level, chestPos, Blocks.CHEST.defaultBlockState().setValue(ChestBlock.FACING, Direction.SOUTH));
        RandomizableContainer.setBlockEntityLootTable(level, random, chestPos, LOOT_TABLE);
        return true;
    }

    private static void placeSpawner(WorldGenLevel level, RandomSource random, int x, int y, int z) {
        BlockPos pos = new BlockPos(x, y, z);
        StructureHelper.set(level, pos, Blocks.SPAWNER.defaultBlockState());
        if (level.getBlockEntity(pos) instanceof SpawnerBlockEntity spawner) {
            spawner.setEntityId(ModEntities.WATER_DRAGON.get(), random);
        }
    }
}
