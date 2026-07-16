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
 * Gold {@code GenericDungeon.makeBouncyCastle}: lavafoam box shell (half-extents 4),
 * red terracotta corner pillars, doorway on −Z, Silverfish / Rat / Scorpion spawners
 * on three walls, one chest.
 * <p>
 * Gold {@code DungeonSpawnerBlock} type <b>26</b>. Origin is structure center at floor Y.
 * <p>
 * Theme note: gold materials are lavafoam + red stained clay (not wool/slime); the
 * “funhouse” bounce feel comes from lavafoam physics in gold OreSpawn.
 */
public final class BouncyCastle {
    /** Gold {@code width = length = 4} (half-extent; loops {@code -4.4}). */
    public static final int HALF = 4;

    /** Full X/Z footprint including walls. */
    public static final int WIDTH = HALF * 2 + 1;

    /** Gold wall height {@code k = 0.height-1} with {@code height = 5}. */
    public static final int HEIGHT = 5;

    public static final ResourceKey<LootTable> LOOT_TABLE = ResourceKey.create(
            Registries.LOOT_TABLE,
            ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "chests/bouncy_castle"));

    private BouncyCastle() {}

    /**
     * Places the bouncy castle centered on {@code (cposx, cposy, cposz)}.
     *
     * @return true if written
     */
    public static boolean makeBouncyCastle(
            WorldGenLevel level, RandomSource random, int cposx, int cposy, int cposz) {
        int minY = level.getMinBuildHeight();
        int maxY = level.getMaxBuildHeight();
        if (cposy < minY || cposy + HEIGHT >= maxY) {
            return false;
        }

        // Clear full shell so underground stone never remains inside
        StructureHelper.clearBox(
                level, cposx - HALF, cposy, cposz - HALF, WIDTH, HEIGHT, WIDTH);

        BlockState lavafoam = ModBlocks.LAVAFOAM.get().defaultBlockState();
        BlockState redCorner = Blocks.RED_TERRACOTTA.defaultBlockState();
        BlockState air = Blocks.AIR.defaultBlockState();

        // Gold: shell floor/ceiling/walls; air interior; doorway on −Z wall
        for (int i = -HALF; i <= HALF; i++) {
            for (int j = -HALF; j <= HALF; j++) {
                for (int k = 0; k < HEIGHT; k++) {
                    BlockState bid = air;
                    if (k == HEIGHT - 1 || k == 0) {
                        bid = lavafoam;
                    }
                    if (i == -HALF || i == HALF) {
                        bid = lavafoam;
                    }
                    if (j == -HALF || j == HALF) {
                        bid = lavafoam;
                    }
                    // Gold corners: stained clay meta 14 (red)
                    if ((i == -HALF || i == HALF) && (j == -HALF || j == HALF)) {
                        bid = redCorner;
                    }
                    // Doorway: k 1.2 at i=0, j=-length
                    if ((k == 1 || k == 2) && i == 0 && j == -HALF) {
                        bid = air;
                    }
                    StructureHelper.set(level, cposx + i, cposy + k, cposz + j, bid);
                }
            }
        }

        // Gold spawners along +Z wall (length-1) and ±X walls at y+3
        placeSpawner(level, random, cposx - 1, cposy + 3, cposz + HALF - 1, EntityType.SILVERFISH);
        placeSpawner(level, random, cposx, cposy + 3, cposz + HALF - 1, rat());
        placeSpawner(level, random, cposx + 1, cposy + 3, cposz + HALF - 1, scorpion());

        placeSpawner(level, random, cposx + HALF - 1, cposy + 3, cposz - 1, EntityType.SILVERFISH);
        placeSpawner(level, random, cposx + HALF - 1, cposy + 3, cposz, rat());
        placeSpawner(level, random, cposx + HALF - 1, cposy + 3, cposz + 1, scorpion());

        placeSpawner(level, random, cposx - HALF + 1, cposy + 3, cposz - 1, EntityType.SILVERFISH);
        placeSpawner(level, random, cposx - HALF + 1, cposy + 3, cposz, rat());
        placeSpawner(level, random, cposx - HALF + 1, cposy + 3, cposz + 1, scorpion());

        // Gold chest at +X,+Z corner y+3 (meta 2 ≈ north)
        placeChest(level, random, cposx + HALF - 1, cposy + 3, cposz + HALF - 1, Direction.NORTH);

        return true;
    }

    private static EntityType<?> rat() {
        return ModEntities.RAT != null ? ModEntities.RAT.get() : EntityType.SILVERFISH;
    }

    private static EntityType<?> scorpion() {
        return ModEntities.SCORPION != null ? ModEntities.SCORPION.get() : EntityType.CAVE_SPIDER;
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
