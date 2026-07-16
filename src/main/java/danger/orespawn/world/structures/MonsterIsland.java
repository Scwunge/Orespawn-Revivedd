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
 * Gold {@code GenericDungeon.makeMonsterIsland}: small sand island pad (−5.5 X, stepped Z
 * half-width), stone underlay, oak palm canopy with four Sea Viper / Sea Monster spawners and
 * dual chests.
 * <p>
 * Gold {@code DungeonSpawnerBlock} type <b>37</b>. Origin is island center at sand Y.
 */
public final class MonsterIsland {
    /** Gold X half-extent ({@code i = -5.5}). */
    public static final int HALF_X = 5;

    /** Full X footprint (and max Z pad width). */
    public static final int WIDTH = HALF_X * 2 + 1;

    /** Gold tree canopy + trunk reaches y+4; foundation at y−1. */
    public static final int HEIGHT = 6;

    public static final ResourceKey<LootTable> LOOT_TABLE = ResourceKey.create(
            Registries.LOOT_TABLE,
            ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "chests/monster_island"));

    private MonsterIsland() {}

    /**
     * Places the monster island centered on {@code (cposx, cposy, cposz)}.
     *
     * @return true if written
     */
    public static boolean makeMonsterIsland(
            WorldGenLevel level, RandomSource random, int cposx, int cposy, int cposz) {
        int minY = level.getMinBuildHeight();
        int maxY = level.getMaxBuildHeight();
        if (cposy - 1 < minY || cposy + 4 >= maxY) {
            return false;
        }

        StructureHelper.clearBox(level, cposx - HALF_X, cposy - 1, cposz - 3, WIDTH, HEIGHT, 7);

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

        // Gold: 50/50 Sea Viper / Sea Monster for all four canopy spawners
        EntityType<?> monster = random.nextInt(2) == 0 ? seaMonster() : seaViper();
        placeSpawner(level, random, cposx + 1, cposy + 3, cposz, monster);
        placeSpawner(level, random, cposx - 1, cposy + 3, cposz, monster);
        placeSpawner(level, random, cposx, cposy + 3, cposz + 1, monster);
        placeSpawner(level, random, cposx, cposy + 3, cposz - 1, monster);

        placeChest(level, random, cposx, cposy + 1, cposz - 1, Direction.SOUTH);
        placeChest(level, random, cposx, cposy + 1, cposz + 1, Direction.NORTH);

        return true;
    }

    private static EntityType<?> seaViper() {
        return ModEntities.SEA_VIPER != null ? ModEntities.SEA_VIPER.get() : EntityType.GUARDIAN;
    }

    private static EntityType<?> seaMonster() {
        return ModEntities.SEA_MONSTER != null ? ModEntities.SEA_MONSTER.get() : EntityType.GUARDIAN;
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
