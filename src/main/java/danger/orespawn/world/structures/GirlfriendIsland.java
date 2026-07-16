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
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootTable;

/**
 * Gold {@code GenericDungeon.makeGirlfriendIsland}: small sand island pad (−5.5 X,
 * stepped Z half-width), stone underlay, oak palm canopy with Girlfriend / Boyfriend /
 * Gold Fish spawners and dual Damsel-loot chests.
 * <p>
 * Gold {@code DungeonSpawnerBlock} type <b>35</b>. Origin is island center at sand Y.
 */
public final class GirlfriendIsland {
    /** Gold X half-extent ({@code i = -5.5}). */
    public static final int HALF_X = 5;

    /** Full X/Z pad footprint. */
    public static final int WIDTH = HALF_X * 2 + 1;

    /** Gold tree canopy + trunk reaches y+4; foundation at y−1. */
    public static final int HEIGHT = 6;

    public static final ResourceKey<LootTable> LOOT_TABLE = ResourceKey.create(
            Registries.LOOT_TABLE,
            ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "chests/girlfriend_island"));

    private GirlfriendIsland() {}

    /**
     * Places the girlfriend island centered on {@code (cposx, cposy, cposz)}.
     *
     * @return true if written
     */
    public static boolean makeGirlfriendIsland(
            WorldGenLevel level, RandomSource random, int cposx, int cposy, int cposz) {
        int minY = level.getMinBuildHeight();
        int maxY = level.getMaxBuildHeight();
        if (cposy - 1 < minY || cposy + 4 >= maxY) {
            return false;
        }

        // Clear pad + tree volume
        StructureHelper.clearBox(
                level, cposx - HALF_X, cposy - 1, cposz - 3, WIDTH, HEIGHT, 7);

        BlockState sand = Blocks.SAND.defaultBlockState();
        BlockState stone = Blocks.STONE.defaultBlockState();
        BlockState leaves = Blocks.OAK_LEAVES
                .defaultBlockState()
                .setValue(LeavesBlock.PERSISTENT, true);
        BlockState log = Blocks.OAK_LOG.defaultBlockState();

        // Gold stepped sand island + stone under
        for (int i = -HALF_X; i <= HALF_X; i++) {
            int kHalf = 3;
            if (i == -5 || i == 5) {
                kHalf = 1;
            } else if (i == -4 || i == 4 || i == -3 || i == 3) {
                kHalf = 2;
            }
            for (int j = -kHalf; j <= kHalf; j++) {
                StructureHelper.set(level, cposx + i, cposy, cposz + j, sand);
                StructureHelper.set(level, cposx + i, cposy - 1, cposz + j, stone);
            }
        }

        // Gold 5×5 leaf canopy at y+3
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

        // Gold spawners in canopy (Girlfriend / Boyfriend / Gold Fish ×2)
        placeSpawner(level, random, cposx + 1, cposy + 3, cposz, girlfriend());
        placeSpawner(level, random, cposx - 1, cposy + 3, cposz, boyfriend());
        placeSpawner(level, random, cposx, cposy + 3, cposz + 1, goldFish());
        placeSpawner(level, random, cposx, cposy + 3, cposz - 1, goldFish());

        // Gold dual chests at y+1 near trunk
        placeChest(level, random, cposx, cposy + 1, cposz - 1, Direction.SOUTH);
        placeChest(level, random, cposx, cposy + 1, cposz + 1, Direction.NORTH);

        return true;
    }

    private static EntityType<?> girlfriend() {
        return ModEntities.GIRLFRIEND != null ? ModEntities.GIRLFRIEND.get() : EntityType.VILLAGER;
    }

    private static EntityType<?> boyfriend() {
        return ModEntities.BOYFRIEND != null ? ModEntities.BOYFRIEND.get() : EntityType.VILLAGER;
    }

    private static EntityType<?> goldFish() {
        return ModEntities.GOLD_FISH != null ? ModEntities.GOLD_FISH.get() : EntityType.TROPICAL_FISH;
    }

    private static void placeSpawner(
            WorldGenLevel level, RandomSource random, int x, int y, int z, EntityType<?> mob) {
        BlockPos pos = new BlockPos(x, y, z);
        StructureHelper.set(level, pos, Blocks.SPAWNER.defaultBlockState());
        if (level.getBlockEntity(pos) instanceof SpawnerBlockEntity spawner) {
            spawner.setEntityId(mob, random);
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
