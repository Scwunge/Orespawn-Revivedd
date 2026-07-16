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
 * Gold {@code GenericDungeon.makeRainbow}: elevated white terracotta skywalk with
 * water wells, hollow platforms at y+26.29, and a rainbow terracotta arch at y+30
 * with Cloud Shark spawners and dual chests.
 * <p>
 * Gold {@code DungeonSpawnerBlock} type <b>46</b>. Origin is arch/walk center at ground Y
 * (content floats at {@code y + PLATFORM_Y} / {@code y + ARCH_Y}).
 * <p>
 * Size-capped: gold geometry is already finite and modest (half-extent 14, arch m≤10);
 * constants document bounds. No truncation needed.
 */
public final class RainbowDungeon {
    /** Gold high walk half-extent ({@code var35 = 12} → X −12.11). */
    public static final int WALK_HALF = 12;

    /** Gold platform layers half-extent max ({@code var35 = 14}). */
    public static final int PLATFORM_HALF = 14;

    /** Full X footprint for clear bounds. */
    public static final int WIDTH = PLATFORM_HALF * 2;

    /** Gold skywalk Y offset. */
    public static final int PLATFORM_Y = 35;

    /** Gold lower hollow platform Y. */
    public static final int BASE_Y = 26;

    /** Gold rainbow arch base Y. */
    public static final int ARCH_Y = 30;

    /** Gold arch max rise {@code m < 11} → peak at ARCH_Y + 10. */
    public static final int ARCH_MAX_M = 10;

    /** Peak relative to origin Y. */
    public static final int HEIGHT = ARCH_Y + ARCH_MAX_M + 1;

    /**
     * Gold {@code blkcolors = {14,1,4,5,3,11,10,6}} → red, orange, yellow, lime,
     * light blue, blue, purple, pink terracotta.
     */
    private static final BlockState[] RAINBOW = {
        Blocks.RED_TERRACOTTA.defaultBlockState(),
        Blocks.ORANGE_TERRACOTTA.defaultBlockState(),
        Blocks.YELLOW_TERRACOTTA.defaultBlockState(),
        Blocks.LIME_TERRACOTTA.defaultBlockState(),
        Blocks.LIGHT_BLUE_TERRACOTTA.defaultBlockState(),
        Blocks.BLUE_TERRACOTTA.defaultBlockState(),
        Blocks.PURPLE_TERRACOTTA.defaultBlockState(),
        Blocks.PINK_TERRACOTTA.defaultBlockState()
    };

    public static final ResourceKey<LootTable> LOOT_TABLE = ResourceKey.create(
            Registries.LOOT_TABLE,
            ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "chests/rainbow_dungeon"));

    private RainbowDungeon() {}

    /**
     * Places the rainbow dungeon centered on {@code (cposx, cposy, cposz)}.
     *
     * @return true if written
     */
    public static boolean makeRainbow(
            WorldGenLevel level, RandomSource random, int cposx, int cposy, int cposz) {
        int minY = level.getMinBuildHeight();
        int maxY = level.getMaxBuildHeight();
        if (cposy + BASE_Y < minY || cposy + HEIGHT >= maxY) {
            return false;
        }

        // Clear full elevated AABB (platform stack + arch)
        int clearH = HEIGHT - BASE_Y + 1;
        StructureHelper.clearBox(
                level,
                cposx - PLATFORM_HALF,
                cposy + BASE_Y,
                cposz - 3,
                WIDTH,
                clearH,
                7);

        BlockState white = Blocks.WHITE_TERRACOTTA.defaultBlockState();
        BlockState air = Blocks.AIR.defaultBlockState();
        BlockState water = Blocks.WATER.defaultBlockState();

        // Gold y+35 solid white walk (−12.11 X, −1.1 Z)
        int j = PLATFORM_Y;
        for (int i = -WALK_HALF; i < WALK_HALF; i++) {
            for (int k = -1; k <= 1; k++) {
                StructureHelper.set(level, cposx + i, cposy + j, cposz + k, white);
            }
        }
        // Gold water wells every 3 blocks with under-block water
        for (int i = -WALK_HALF + 1; i < WALK_HALF; i += 3) {
            StructureHelper.set(level, cposx + i, cposy + j, cposz, water);
            StructureHelper.set(level, cposx + i, cposy + j - 1, cposz, water);
        }

        // Gold hollow frames at y+26 (half 13×2), y+27 (14×3), y+28 (13×2)
        placeHollowFrame(level, cposx, cposy + 26, cposz, 13, 2, white, air);
        placeHollowFrame(level, cposx, cposy + 27, cposz, 14, 3, white, air);
        placeHollowFrame(level, cposx, cposy + 28, cposz, 13, 2, white, air);

        // Gold solid white strip at y+29 (−12.11, −1.1)
        for (int i = -WALK_HALF; i < WALK_HALF; i++) {
            for (int k = -1; k <= 1; k++) {
                StructureHelper.set(level, cposx + i, cposy + 29, cposz + k, white);
            }
        }

        // Gold rainbow arches m = 3.10
        int archBase = ARCH_Y;
        for (int m = 3; m < 11; m++) {
            BlockState color = RAINBOW[m - 3];
            for (int up = 0; up < m; up++) {
                StructureHelper.set(level, cposx + m, cposy + archBase + up, cposz, color);
                StructureHelper.set(level, cposx - (m + 1), cposy + archBase + up, cposz, color);
            }
            for (int x = -(m + 1); x <= m; x++) {
                StructureHelper.set(level, cposx + x, cposy + archBase + m, cposz, color);
            }
        }

        // Gold Cloud Shark spawners at arch base ± sides, stacked 3 high
        EntityType<?> shark = cloudShark();
        for (int dy = 0; dy < 3; dy++) {
            placeSpawner(level, random, cposx + 2, cposy + archBase + dy, cposz, shark);
            placeSpawner(level, random, cposx - 3, cposy + archBase + dy, cposz, shark);
        }

        // Gold dual chests at arch base
        placeChest(level, random, cposx, cposy + archBase, cposz, Direction.NORTH);
        placeChest(level, random, cposx - 1, cposy + archBase, cposz, Direction.NORTH);

        return true;
    }

    /**
     * Places a hollow rectangle frame: rim of {@code wool} at half-extents
     * {@code halfX} (loops −halfX . halfX-1) and {@code halfZ} (−halfZ.halfZ).
     */
    private static void placeHollowFrame(
            WorldGenLevel level,
            int cx,
            int y,
            int cz,
            int halfX,
            int halfZ,
            BlockState rim,
            BlockState air) {
        for (int i = -halfX; i < halfX; i++) {
            for (int k = -halfZ; k <= halfZ; k++) {
                BlockState blk = air;
                if (i == -halfX || i == halfX - 1) {
                    blk = rim;
                }
                if (k == -halfZ || k == halfZ) {
                    blk = rim;
                }
                StructureHelper.set(level, cx + i, y, cz + k, blk);
            }
        }
    }

    private static EntityType<?> cloudShark() {
        return ModEntities.CLOUD_SHARK != null ? ModEntities.CLOUD_SHARK.get() : EntityType.PHANTOM;
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
