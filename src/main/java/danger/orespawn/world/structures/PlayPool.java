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
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootTable;

/**
 * Gold {@code GenericDungeon.makePlayPool}: elevated water strip with Attack Squid
 * spawners and dual chests (gold {@code SquidContentsList}).
 * <p>
 * Gold {@code DungeonSpawnerBlock} type <b>12</b>. Origin is the west end of the strip
 * at ground Y; structure sits at {@code y+16.y+18}.
 */
public final class PlayPool {
    /** Length of the spawner/water strip (gold {@code i = 0.3}). */
    public static final int STRIP = 4;

    /** Gold places content starting at {@code y+16}. */
    public static final int BASE_OFFSET = 16;

    /** Water top is at {@code y+18} relative to origin. */
    public static final int HEIGHT = 3;

    /** Flowing ends extend one block past each side of the strip. */
    public static final int PAD_X = 1;

    public static final ResourceKey<LootTable> LOOT_TABLE = ResourceKey.create(
            Registries.LOOT_TABLE,
            ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "chests/play_pool"));

    private PlayPool() {}

    /**
     * Places the play pool with west origin at {@code (x, y, z)}.
     *
     * @return true if written
     */
    public static boolean makePlayPool(WorldGenLevel level, RandomSource random, int x, int y, int z) {
        int minY = level.getMinBuildHeight();
        int maxY = level.getMaxBuildHeight();
        int baseY = y + BASE_OFFSET;
        if (baseY < minY || baseY + HEIGHT >= maxY) {
            return false;
        }

        // Clear strip volume (incl. flowing ends) so underground stone never fills it
        StructureHelper.clearBox(
                level, x - PAD_X, baseY, z, STRIP + PAD_X * 2, HEIGHT, 1);

        BlockState water = Blocks.WATER.defaultBlockState();
        BlockState air = Blocks.AIR.defaultBlockState();

        // Gold: 4 Attack Squid spawners at y+16
        for (int i = 0; i < STRIP; i++) {
            placeSpawner(level, random, x + i, baseY, z);
        }

        // Gold: dual chests at y+17 (i=1,2)
        placeChest(level, random, x + 1, baseY + 1, z, Direction.SOUTH);
        placeChest(level, random, x + 2, baseY + 1, z, Direction.SOUTH);

        // Gold: still water on strip at y+18; flowing sources at ends (-1 / +4)
        for (int i = 0; i < STRIP; i++) {
            StructureHelper.set(level, x + i, baseY + 2, z, water);
        }
        StructureHelper.set(level, x - 1, baseY + 2, z, water);
        StructureHelper.set(level, x + STRIP, baseY + 2, z, water);

        // Keep air around spawner level (belt-and-suspenders after clear)
        for (int i = 0; i < STRIP; i++) {
            // chests occupy y+1; leave air only where no chest
            if (i != 1 && i != 2) {
                StructureHelper.set(level, x + i, baseY + 1, z, air);
            }
        }

        return true;
    }

    private static void placeSpawner(WorldGenLevel level, RandomSource random, int x, int y, int z) {
        BlockPos pos = new BlockPos(x, y, z);
        StructureHelper.set(level, pos, Blocks.SPAWNER.defaultBlockState());
        if (level.getBlockEntity(pos) instanceof SpawnerBlockEntity spawner) {
            EntityType<?> type =
                    ModEntities.ATTACK_SQUID != null ? ModEntities.ATTACK_SQUID.get() : EntityType.SQUID;
            spawner.setEntityId(type, random);
        }
    }

    private static void placeChest(
            WorldGenLevel level, RandomSource random, int x, int y, int z, Direction facing) {
        BlockPos pos = new BlockPos(x, y, z);
        StructureHelper.set(
                level, pos, Blocks.CHEST.defaultBlockState().setValue(ChestBlock.FACING, facing));
        RandomizableContainer.setBlockEntityLootTable(level, random, pos, LOOT_TABLE);
    }
}
