package danger.orespawn.world.structures;

import danger.orespawn.init.ModBlocks;
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
 * Gold {@code GenericDungeon.makeCrystalHauntedHouse}: small crystal-plank house
 * (half-extents width/length 3, wall height 3 + roof), crystal-stone floor, glass
 * top wall ring, door on +X, furniture on +Z wall, three stacked center spawners.
 * <p>
 * Gold {@code DungeonSpawnerBlock} type <b>25</b> calls this structure.
 */
public final class CrystalHauntedHouse {
    /** Half-extent on X/Z (gold {@code width}/{@code length} = 3 → footprint 7×7). */
    public static final int HALF = 3;
    /** Wall interior height (gold {@code height} = 3); roof sits at {@code height + 1}. */
    public static final int HEIGHT = 3;
    /** Total Y span including floor (k=0) through roof (k=height+1). */
    public static final int TOTAL_HEIGHT = HEIGHT + 2;

    public static final ResourceKey<LootTable> LOOT_TABLE = ResourceKey.create(
            Registries.LOOT_TABLE,
            ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "chests/crystal_haunted_house"));

    private CrystalHauntedHouse() {}

    /**
     * Places a crystal haunted house centered at {@code (x, y, z)}.
     * Gold used fixed orientation: door on +X, furniture along +Z interior wall.
     *
     * @return true if the structure was written
     */
    public static boolean makeCrystalHauntedHouse(WorldGenLevel level, RandomSource random, int x, int y, int z) {
        int minY = level.getMinBuildHeight();
        int maxY = level.getMaxBuildHeight();
        if (y < minY || y + TOTAL_HEIGHT - 1 >= maxY) {
            return false;
        }

        // Gold fixed orientation: var22=1, deltaz=0 → door on +X wall at j==0
        final int doorDir = 1;
        final int deltaZ = 0;
        // Furniture facing meta 2 = north (gold setBlockMetadataWithNotify meta var28=2)
        final Direction furnitureFacing = Direction.NORTH;

        BlockState crystalStone = ModBlocks.CRYSTAL_STONE.get().defaultBlockState();
        BlockState crystalPlanks = ModBlocks.CRYSTAL_PLANKS.get().defaultBlockState();
        BlockState glass = Blocks.GLASS.defaultBlockState();
        BlockState air = Blocks.AIR.defaultBlockState();

        // Pre-clear footprint so underground stone cannot remain inside the shell
        StructureHelper.clearBox(level, x - HALF, y, z - HALF, HALF * 2 + 1, TOTAL_HEIGHT, HALF * 2 + 1);

        // Shell: i,j in [-HALF.HALF], k in [0.HEIGHT+1]
        for (int i = -HALF; i <= HALF; i++) {
            for (int j = -HALF; j <= HALF; j++) {
                for (int k = 0; k <= HEIGHT + 1; k++) {
                    if (k == HEIGHT + 1) {
                        // Roof planks
                        StructureHelper.set(level, x + i, y + k, z + j, crystalPlanks);
                    } else if (k == 0) {
                        // Floor crystal stone
                        StructureHelper.set(level, x + i, y + k, z + j, crystalStone);
                    } else if (i != HALF && j != HALF && i != -HALF && j != -HALF) {
                        // Interior air
                        StructureHelper.set(level, x + i, y + k, z + j, air);
                    } else if (k == HEIGHT) {
                        // Top wall ring: glass
                        StructureHelper.set(level, x + i, y + k, z + j, glass);
                    } else if ((k == 1 || k == 2) && i == doorDir * HALF && j == deltaZ * HALF) {
                        // Door opening (2 blocks tall) on +X wall at z-center
                        StructureHelper.set(level, x + i, y + k, z + j, air);
                    } else {
                        // Walls: crystal planks
                        StructureHelper.set(level, x + i, y + k, z + j, crystalPlanks);
                    }
                }
            }
        }

        // Furniture along +Z interior wall (gold: j = length - 1 = 2, k = 1)
        int furnY = y + 1;
        int furnJ = HALF - 1; // length - 1

        // Furnace at var23=2 → (x+2, y+1, z+2), facing north
        BlockPos furnacePos = new BlockPos(x + 2 * doorDir + furnJ * deltaZ, furnY, z + 2 * deltaZ + furnJ * doorDir);
        BlockState furnaceState = ModBlocks.CRYSTAL_FURNACE.get().defaultBlockState()
                .setValue(AbstractFurnaceBlock.FACING, furnitureFacing);
        StructureHelper.set(level, furnacePos, furnaceState);

        // Workbench at var23=1 → (x+1, y+1, z+2)
        BlockPos workbenchPos = new BlockPos(x + 1 * doorDir + furnJ * deltaZ, furnY, z + 1 * deltaZ + furnJ * doorDir);
        StructureHelper.set(level, workbenchPos, ModBlocks.CRYSTAL_WORKBENCH.get().defaultBlockState());

        // Chest at var23=0 → (x+0, y+1, z+2), facing north
        BlockPos chestPos = new BlockPos(x + 0 * doorDir + furnJ * deltaZ, furnY, z + 0 * deltaZ + furnJ * doorDir);
        BlockState chestState = Blocks.CHEST.defaultBlockState().setValue(ChestBlock.FACING, furnitureFacing);
        StructureHelper.set(level, chestPos, chestState);
        RandomizableContainer.setBlockEntityLootTable(level, random, chestPos, LOOT_TABLE);

        // Stacked center spawners (gold: Rat @ y+1, Ghost @ y+2, Ghost Pumpkin Skelly @ y+3)
        placeSpawner(level, random, new BlockPos(x, y + 1, z), ModEntities.RAT.get());
        placeSpawner(level, random, new BlockPos(x, y + 2, z), ModEntities.GHOST.get());
        placeSpawner(level, random, new BlockPos(x, y + 3, z), ModEntities.GHOST_SKELLY.get());

        return true;
    }

    private static void placeSpawner(
            WorldGenLevel level, RandomSource random, BlockPos pos, net.minecraft.world.entity.EntityType<?> type) {
        StructureHelper.set(level, pos, Blocks.SPAWNER.defaultBlockState());
        if (level.getBlockEntity(pos) instanceof SpawnerBlockEntity spawner) {
            spawner.setEntityId(type, random);
        }
    }
}
