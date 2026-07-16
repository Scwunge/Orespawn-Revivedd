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
import net.minecraft.world.level.block.AbstractFurnaceBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootTable;

/**
 * Gold {@code GenericDungeon.makeHauntedHouse}: small wood/cobble house
 * (half-extents 3, wall height 3 + roof), door on +X, furniture on +Z wall,
 * center Rat spawner.
 * <p>
 * Gold {@code DungeonSpawnerBlock} type <b>5</b>. Crystal variant is
 * {@link CrystalHauntedHouse} (type 25).
 */
public final class HauntedHouse {
    public static final int HALF = 3;
    public static final int HEIGHT = 3;
    public static final int TOTAL_HEIGHT = HEIGHT + 2;

    public static final ResourceKey<LootTable> LOOT_TABLE = ResourceKey.create(
            Registries.LOOT_TABLE,
            ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "chests/haunted_house"));

    private HauntedHouse() {}

    public static boolean makeHauntedHouse(WorldGenLevel level, RandomSource random, int x, int y, int z) {
        int minY = level.getMinBuildHeight();
        int maxY = level.getMaxBuildHeight();
        if (y < minY || y + TOTAL_HEIGHT - 1 >= maxY) {
            return false;
        }

        final int doorDir = 1;
        final int deltaZ = 0;
        final Direction furnitureFacing = Direction.NORTH;

        BlockState planks = Blocks.OAK_PLANKS.defaultBlockState();
        BlockState cobble = Blocks.COBBLESTONE.defaultBlockState();
        BlockState glass = Blocks.GLASS.defaultBlockState();
        BlockState air = Blocks.AIR.defaultBlockState();

        // Pre-clear so underground solid does not fill the interior
        StructureHelper.clearBox(level, x - HALF, y, z - HALF, HALF * 2 + 1, TOTAL_HEIGHT, HALF * 2 + 1);

        for (int i = -HALF; i <= HALF; i++) {
            for (int j = -HALF; j <= HALF; j++) {
                for (int k = 0; k <= HEIGHT + 1; k++) {
                    if (k == HEIGHT + 1) {
                        StructureHelper.set(level, x + i, y + k, z + j, planks);
                    } else if (k == 0) {
                        StructureHelper.set(level, x + i, y + k, z + j, cobble);
                    } else if (i != HALF && j != HALF && i != -HALF && j != -HALF) {
                        StructureHelper.set(level, x + i, y + k, z + j, air);
                    } else if (k == HEIGHT) {
                        StructureHelper.set(level, x + i, y + k, z + j, glass);
                    } else if ((k == 1 || k == 2) && i == doorDir * HALF && j == deltaZ * HALF) {
                        StructureHelper.set(level, x + i, y + k, z + j, air);
                    } else {
                        StructureHelper.set(level, x + i, y + k, z + j, planks);
                    }
                }
            }
        }

        int furnY = y + 1;
        int furnJ = HALF - 1;

        BlockPos furnacePos = new BlockPos(x + 2 * doorDir + furnJ * deltaZ, furnY, z + 2 * deltaZ + furnJ * doorDir);
        StructureHelper.set(
                level,
                furnacePos,
                Blocks.FURNACE.defaultBlockState().setValue(AbstractFurnaceBlock.FACING, furnitureFacing));

        BlockPos craftPos = new BlockPos(x + 1 * doorDir + furnJ * deltaZ, furnY, z + 1 * deltaZ + furnJ * doorDir);
        StructureHelper.set(level, craftPos, Blocks.CRAFTING_TABLE.defaultBlockState());

        BlockPos chestPos = new BlockPos(x + 0 * doorDir + furnJ * deltaZ, furnY, z + 0 * deltaZ + furnJ * doorDir);
        StructureHelper.set(
                level, chestPos, Blocks.CHEST.defaultBlockState().setValue(ChestBlock.FACING, furnitureFacing));
        RandomizableContainer.setBlockEntityLootTable(level, random, chestPos, LOOT_TABLE);

        BlockPos spawnerPos = new BlockPos(x, y + 1, z);
        StructureHelper.set(level, spawnerPos, Blocks.SPAWNER.defaultBlockState());
        if (level.getBlockEntity(spawnerPos) instanceof SpawnerBlockEntity spawner) {
            spawner.setEntityId(ModEntities.RAT.get(), random);
        }

        return true;
    }
}
