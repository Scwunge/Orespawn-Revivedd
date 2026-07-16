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
 * Gold {@code GenericDungeon.makeAlienWTFDungeon}: deep clay entry cube, spiral stone
 * stair shaft, four branching quartz/obsidian rooms with Alien / GammaMetroid (gold
 * {@code "WTF?"}) spawners and escalating chest counts by difficulty.
 * <p>
 * Gold {@code DungeonSpawnerBlock} type <b>10</b>. Origin is the surface seed; gold
 * immediately drops {@code depth - 3} levels before building.
 */
public final class AlienWTFDungeon {
    /** Gold entry cube {@code width = 5}. */
    public static final int ENTRY_WIDTH = 5;

    /** Gold entry cube {@code height = 5}. */
    public static final int ENTRY_HEIGHT = 5;

    /** Gold drop before build ({@code depth = 20}; floor at {@code y - (depth - 3)}). */
    public static final int DEPTH = 20;

    /** Y offset applied to origin (gold {@code cposy -= depth - 3}). */
    public static final int DROP = DEPTH - 3;

    /**
     * Rough half-extent of the four outer rooms (max makePart width 15 + corridors).
     * Used for clearBox sizing notes.
     */
    public static final int RADIUS = 22;

    /** Room interior height (gold makePart height args 5.8; walls use that + roof). */
    public static final int ROOM_MAX_HEIGHT = 9;

    public static final ResourceKey<LootTable> LOOT_TABLE = ResourceKey.create(
            Registries.LOOT_TABLE,
            ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "chests/alien_wtf_dungeon"));

    private AlienWTFDungeon() {}

    /**
     * Places the alien/WTF dungeon. Origin is the surface seed point; build floor is
     * {@code cposy - DROP}.
     *
     * @return true if written
     */
    public static boolean makeAlienWTFDungeon(
            WorldGenLevel level, RandomSource random, int cposx, int cposy, int cposz) {
        int minY = level.getMinBuildHeight();
        int maxY = level.getMaxBuildHeight();
        int baseY = cposy - DROP;
        if (baseY < minY || baseY + ROOM_MAX_HEIGHT + DEPTH >= maxY) {
            return false;
        }

        // Clear full hub + room footprint (finite bounds)
        StructureHelper.clearBox(
                level,
                cposx - RADIUS,
                baseY,
                cposz - RADIUS,
                RADIUS * 2 + 1,
                ROOM_MAX_HEIGHT + DEPTH,
                RADIUS * 2 + 1);

        BlockState clay = Blocks.CLAY.defaultBlockState();
        BlockState air = Blocks.AIR.defaultBlockState();
        BlockState stone = Blocks.STONE.defaultBlockState();

        int ox = cposx;
        int oy = baseY;
        int oz = cposz;

        // Entry clay cube (gold: cpos ± 2)
        for (int i = 0; i < ENTRY_WIDTH; i++) {
            for (int j = 0; j < ENTRY_HEIGHT; j++) {
                for (int k = 0; k < ENTRY_WIDTH; k++) {
                    boolean shell = i == 0
                            || j == 0
                            || k == 0
                            || i == ENTRY_WIDTH - 1
                            || j == ENTRY_HEIGHT - 1
                            || k == ENTRY_WIDTH - 1;
                    StructureHelper.set(
                            level, ox + i - 2, oy + j, oz + k - 2, shell ? clay : air);
                }
            }
        }

        // Stair shaft origin nudged -1,-1 (gold cposx--; cposz--)
        int sx = ox - 1;
        int sz = oz - 1;
        int step = 0;
        for (int j = 3; j < DEPTH; j++) {
            for (int i = 0; i < 4; i++) {
                for (int k = 0; k < 4; k++) {
                    BlockState blk = air;
                    if (i == 0 || k == 0 || i == 3 || k == 3) {
                        blk = clay;
                    }
                    StructureHelper.set(level, sx + i, oy + j, sz + k, blk);
                }
            }
            // Spiral stone step
            int stepX;
            int stepZ;
            switch (step) {
                case 0 -> {
                    stepX = 1;
                    stepZ = 1;
                }
                case 1 -> {
                    stepX = 2;
                    stepZ = 1;
                }
                case 2 -> {
                    stepX = 2;
                    stepZ = 2;
                }
                default -> {
                    stepX = 1;
                    stepZ = 2;
                }
            }
            StructureHelper.set(level, sx + stepX, oy + j, sz + stepZ, stone);
            if (++step > 3) {
                step = 0;
            }
        }

        // Hub working origin after gold ++cposx / ++cposz (back to surface seed)
        int hx = ox;
        int hz = oz;

        // North room + corridor (difficulty 1)
        makePart(level, random, hx, oy, hz + 7, 9, 5, 1, 1, 1);
        int xwidth = 3;
        int zwidth = 6;
        for (int i = 0; i < xwidth; i++) {
            for (int k = 0; k < zwidth; k++) {
                for (int j = 0; j < 4; j++) {
                    BlockState blk = air;
                    if (j == 0 || j == 3 || i == 0 || i == xwidth - 1) {
                        blk = clay;
                    }
                    StructureHelper.set(level, hx + i, oy + j, hz + k + 2, blk);
                }
            }
        }

        // East room + corridor (difficulty 2)
        makePart(level, random, hx + 7, oy, hz, 11, 6, 1, -1, 2);
        int ex = 6;
        int ez = 3;
        for (int i = 0; i < ex; i++) {
            for (int k = 0; k < ez; k++) {
                for (int j = 0; j < 4; j++) {
                    BlockState blk = air;
                    if (j == 0 || j == 3 || k == 0 || k == ez - 1) {
                        blk = clay;
                    }
                    StructureHelper.set(level, hx + i + 2, oy + j, hz - k, blk);
                }
            }
        }

        // West room + corridor (difficulty 3)
        makePart(level, random, hx - 7, oy, hz, 13, 7, -1, 1, 3);
        for (int i = 0; i < ex; i++) {
            for (int k = 0; k < ez; k++) {
                for (int j = 0; j < 4; j++) {
                    BlockState blk = air;
                    if (j == 0 || j == 3 || k == 0 || k == ez - 1) {
                        blk = clay;
                    }
                    StructureHelper.set(level, hx - i - 2, oy + j, hz + k, blk);
                }
            }
        }

        // South room + corridor (difficulty 4)
        makePart(level, random, hx, oy, hz - 7, 15, 8, -1, -1, 4);
        int sxw = 3;
        int szw = 6;
        for (int i = 0; i < sxw; i++) {
            for (int k = 0; k < szw; k++) {
                for (int j = 0; j < 4; j++) {
                    BlockState blk = air;
                    if (j == 0 || j == 3 || i == 0 || i == sxw - 1) {
                        blk = clay;
                    }
                    StructureHelper.set(level, hx - i, oy + j, hz - k - 2, blk);
                }
            }
        }

        return true;
    }

    /**
     * Gold {@code makePart}: hollow room with quartz floor cross + obsidian shell,
     * {@code difficulty} pairs of Alien/GammaMetroid spawners, 1.4 chests.
     */
    private static void makePart(
            WorldGenLevel level,
            RandomSource random,
            int cposx,
            int cposy,
            int cposz,
            int width,
            int height,
            int dx,
            int dz,
            int difficulty) {
        BlockState air = Blocks.AIR.defaultBlockState();
        BlockState quartz = Blocks.QUARTZ_BLOCK.defaultBlockState();
        BlockState obsidian = Blocks.OBSIDIAN.defaultBlockState();

        // Clear volume along directed axes
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                for (int k = 0; k < width; k++) {
                    StructureHelper.set(
                            level, cposx + i * dx, cposy + j, cposz + k * dz, air);
                }
            }
        }

        // Floor: quartz with obsidian cross
        for (int i = 0; i < width; i++) {
            for (int k = 0; k < width; k++) {
                BlockState blk = quartz;
                if (i == width / 2 || k == width / 2) {
                    blk = obsidian;
                }
                StructureHelper.set(level, cposx + i * dx, cposy, cposz + k * dz, blk);
            }
        }

        // Ceiling
        for (int i = 0; i < width; i++) {
            for (int k = 0; k < width; k++) {
                StructureHelper.set(
                        level, cposx + i * dx, cposy + height, cposz + k * dz, obsidian);
            }
        }

        // Walls along X
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                StructureHelper.set(level, cposx + i * dx, cposy + j, cposz, obsidian);
                StructureHelper.set(
                        level, cposx + i * dx, cposy + j, cposz + (width - 1) * dz, obsidian);
            }
        }

        // Walls along Z
        for (int k = 0; k < width; k++) {
            for (int j = 0; j < height; j++) {
                StructureHelper.set(level, cposx, cposy + j, cposz + k * dz, obsidian);
                StructureHelper.set(
                        level, cposx + (width - 1) * dx, cposy + j, cposz + k * dz, obsidian);
            }
        }

        // Spawner columns (pairs per difficulty tier)
        for (int j = 0; j < difficulty; j++) {
            placeAlienSpawner(
                    level,
                    random,
                    cposx + dx * width / 2,
                    cposy + j + 2,
                    cposz + dz * width / 2);
            placeAlienSpawner(
                    level,
                    random,
                    cposx + dx * width / 2 + dx,
                    cposy + j + 2,
                    cposz + dz * width / 2 + dz);
        }

        // Chests scale with difficulty
        placeChest(
                level,
                random,
                cposx + width * dx / 2,
                cposy + 1,
                cposz + dz,
                Direction.SOUTH);
        if (difficulty > 1) {
            placeChest(
                    level,
                    random,
                    cposx + width * dx / 2,
                    cposy + 1,
                    cposz + (width - 2) * dz,
                    Direction.NORTH);
        }
        if (difficulty > 2) {
            placeChest(
                    level,
                    random,
                    cposx + dx,
                    cposy + 1,
                    cposz + width / 2 * dz,
                    Direction.EAST);
        }
        if (difficulty > 3) {
            placeChest(
                    level,
                    random,
                    cposx + (width - 2) * dx,
                    cposy + 1,
                    cposz + width / 2 * dz,
                    Direction.WEST);
        }
    }

    /** Gold 50/50 Alien / "WTF?" → {@link ModEntities#ALIEN} / {@link ModEntities#GAMMAMETROID}. */
    private static void placeAlienSpawner(
            WorldGenLevel level, RandomSource random, int x, int y, int z) {
        BlockPos pos = new BlockPos(x, y, z);
        StructureHelper.set(level, pos, Blocks.SPAWNER.defaultBlockState());
        if (level.getBlockEntity(pos) instanceof SpawnerBlockEntity spawner) {
            EntityType<?> type = random.nextBoolean()
                    ? ModEntities.ALIEN.get()
                    : ModEntities.GAMMAMETROID.get();
            spawner.setEntityId(type, random);
        }
    }

    private static void placeChest(
            WorldGenLevel level, RandomSource random, int x, int y, int z, Direction facing) {
        BlockPos pos = new BlockPos(x, y, z);
        BlockState chest = Blocks.CHEST.defaultBlockState().setValue(ChestBlock.FACING, facing);
        StructureHelper.set(level, pos, chest);
        RandomizableContainer.setBlockEntityLootTable(level, random, pos, LOOT_TABLE);
    }
}
