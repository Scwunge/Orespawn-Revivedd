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
 * Gold {@code GenericDungeon.makeCloudSharkDungeon}: tiny netherrack pad (2 high),
 * four Cloud Shark spawners on cardinals, chest on top.
 * <p>
 * Gold {@code DungeonSpawnerBlock} type <b>14</b>. Origin is the pad center.
 */
public final class CloudSharkDungeon {
    /** Horizontal half-extent of the pad + spawners (±1). */
    public static final int RADIUS = 1;

    /** Full footprint width (3×3). */
    public static final int WIDTH = RADIUS * 2 + 1;

    /** Pad y and y-1 plus chest at y+1 → vertical span 3. */
    public static final int HEIGHT = 3;

    public static final ResourceKey<LootTable> LOOT_TABLE = ResourceKey.create(
            Registries.LOOT_TABLE,
            ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "chests/cloud_shark_dungeon"));

    private CloudSharkDungeon() {}

    /**
     * Places the cloud shark dungeon centered on {@code (cposx, cposy, cposz)}.
     *
     * @return true if written
     */
    public static boolean makeCloudSharkDungeon(
            WorldGenLevel level, RandomSource random, int cposx, int cposy, int cposz) {
        int minY = level.getMinBuildHeight();
        int maxY = level.getMaxBuildHeight();
        if (cposy - 1 < minY || cposy + 1 >= maxY) {
            return false;
        }

        StructureHelper.clearBox(
                level, cposx - RADIUS, cposy - 1, cposz - RADIUS, WIDTH, HEIGHT, WIDTH);

        BlockState netherrack = Blocks.NETHERRACK.defaultBlockState();
        StructureHelper.set(level, cposx, cposy, cposz, netherrack);
        StructureHelper.set(level, cposx, cposy - 1, cposz, netherrack);

        placeSpawner(level, random, cposx + 1, cposy, cposz);
        placeSpawner(level, random, cposx - 1, cposy, cposz);
        placeSpawner(level, random, cposx, cposy, cposz + 1);
        placeSpawner(level, random, cposx, cposy, cposz - 1);

        BlockPos chestPos = new BlockPos(cposx, cposy + 1, cposz);
        StructureHelper.set(
                level,
                chestPos,
                Blocks.CHEST.defaultBlockState().setValue(ChestBlock.FACING, Direction.SOUTH));
        RandomizableContainer.setBlockEntityLootTable(level, random, chestPos, LOOT_TABLE);

        return true;
    }

    private static void placeSpawner(WorldGenLevel level, RandomSource random, int x, int y, int z) {
        BlockPos pos = new BlockPos(x, y, z);
        StructureHelper.set(level, pos, Blocks.SPAWNER.defaultBlockState());
        if (level.getBlockEntity(pos) instanceof SpawnerBlockEntity spawner) {
            spawner.setEntityId(ModEntities.CLOUD_SHARK.get(), random);
        }
    }
}
