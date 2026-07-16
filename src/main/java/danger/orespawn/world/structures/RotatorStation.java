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
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootTable;

/**
 * Gold {@code GenericDungeon.makeRotatorStation}: thin crystal-stone pillar with two
 * stacked Rotator spawners and a chest on top (gold: Rotator eggs + CrystalCoal;
 * chest loot uses {@code crystal_coal}).
 * <p>
 * Gold {@code DungeonSpawnerBlock} type <b>3</b>. Origin is the station base center.
 */
public final class RotatorStation {
    /** Footprint (single-column station). */
    public static final int WIDTH = 1;

    /**
     * Top of structure relative to origin: gold chest at {@code y+8}.
     * Clear volume is {@code y . y+HEIGHT-1}.
     */
    public static final int HEIGHT = 9;

    /** Gold first crystal-stone block at {@code y+4}. */
    public static final int PILLAR_BASE = 4;

    public static final ResourceKey<LootTable> LOOT_TABLE = ResourceKey.create(
            Registries.LOOT_TABLE,
            ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "chests/rotator_station"));

    private RotatorStation() {}

    /**
     * Places a rotator station centered on {@code (x, y, z)}.
     *
     * @return true if written
     */
    public static boolean makeRotatorStation(
            WorldGenLevel level, RandomSource random, int x, int y, int z) {
        int minY = level.getMinBuildHeight();
        int maxY = level.getMaxBuildHeight();
        if (y < minY || y + HEIGHT >= maxY) {
            return false;
        }

        // Clear column first so underground stone never fills the pillar air gaps
        StructureHelper.clearBox(level, x, y, z, WIDTH, HEIGHT, WIDTH);

        BlockState crystalStone = ModBlocks.CRYSTAL_STONE.get().defaultBlockState();

        // Gold: crystal stone @ y+4, spawners @ y+5 / y+6, crystal stone @ y+7, chest @ y+8
        StructureHelper.set(level, x, y + PILLAR_BASE, z, crystalStone);

        placeSpawner(level, random, x, y + 5, z);
        placeSpawner(level, random, x, y + 6, z);

        StructureHelper.set(level, x, y + 7, z, crystalStone);

        BlockPos chestPos = new BlockPos(x, y + 8, z);
        BlockState chest = Blocks.CHEST.defaultBlockState().setValue(ChestBlock.FACING, Direction.NORTH);
        StructureHelper.set(level, chestPos, chest);
        RandomizableContainer.setBlockEntityLootTable(level, random, chestPos, LOOT_TABLE);

        return true;
    }

    private static void placeSpawner(WorldGenLevel level, RandomSource random, int x, int y, int z) {
        BlockPos pos = new BlockPos(x, y, z);
        StructureHelper.set(level, pos, Blocks.SPAWNER.defaultBlockState());
        if (level.getBlockEntity(pos) instanceof SpawnerBlockEntity spawner) {
            EntityType<?> type =
                    ModEntities.ROTATOR != null ? ModEntities.ROTATOR.get() : EntityType.BLAZE;
            spawner.setEntityId(type, random);
        }
    }
}
