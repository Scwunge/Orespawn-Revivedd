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
 * Gold {@code GenericDungeon.makeKyuubiDungeon}: sandstone surface shaft, stone vertical
 * drop into water sump, lava-lined tunnel into a large netherrack room with lava squares,
 * Kyuubi platform (nether-brick rings + lava), and obsidian Blaze tower.
 * <p>
 * Gold {@code DungeonSpawnerBlock} type <b>7</b>. Origin is the SW corner of the 5×5 shaft.
 */
public final class KyuubiDungeon {
    /** Surface shaft width (gold {@code width = 5}). */
    public static final int WIDTH = 5;

    /** Surface shaft height above origin (gold {@code height = 5}). */
    public static final int SHAFT_HEIGHT = 5;

    /** Drop depth from origin to room floor (gold {@code depth = 20}). */
    public static final int DEPTH = 20;

    /** Tunnel length east into the room (gold {@code length = 12}). */
    public static final int TUNNEL_LENGTH = 12;

    /** Nether room Z width (gold {@code rwidth = 30}). */
    public static final int ROOM_WIDTH = 30;

    /** Nether room height (gold {@code rheight = 18}). */
    public static final int ROOM_HEIGHT = 18;

    /** Nether room X length (gold {@code rlength = 20}). */
    public static final int ROOM_LENGTH = 20;

    /**
     * Full X span from shaft corner through tunnel into room:
     * {@code WIDTH + TUNNEL_LENGTH - 2 + ROOM_LENGTH}.
     */
    public static final int TOTAL_X = WIDTH + TUNNEL_LENGTH - 2 + ROOM_LENGTH;

    /** Room extends {@code ROOM_WIDTH/2} south of shaft Z. */
    public static final int ROOM_Z_SOUTH = ROOM_WIDTH / 2;

    public static final ResourceKey<LootTable> LOOT_TABLE = ResourceKey.create(
            Registries.LOOT_TABLE,
            ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "chests/kyuubi_dungeon"));

    public static final ResourceKey<LootTable> BLAZE_LOOT_TABLE = ResourceKey.create(
            Registries.LOOT_TABLE,
            ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "chests/kyuubi_dungeon_blaze"));

    private KyuubiDungeon() {}

    /**
     * Places the kyuubi dungeon with shaft SW corner at {@code (cposx, cposy, cposz)}.
     *
     * @return true if written
     */
    public static boolean makeKyuubiDungeon(
            WorldGenLevel level, RandomSource random, int cposx, int cposy, int cposz) {
        int minY = level.getMinBuildHeight();
        int maxY = level.getMaxBuildHeight();
        int bottom = cposy - (DEPTH + 2);
        int top = cposy + SHAFT_HEIGHT;
        int roomZ0 = cposz - ROOM_Z_SOUTH;
        if (bottom < minY || top >= maxY) {
            return false;
        }

        // Clear full AABB (shaft + drop + tunnel + room)
        StructureHelper.clearBox(
                level,
                cposx,
                bottom,
                roomZ0,
                TOTAL_X,
                top - bottom + 1,
                ROOM_WIDTH);

        BlockState air = Blocks.AIR.defaultBlockState();
        BlockState sandstone = Blocks.SANDSTONE.defaultBlockState();
        BlockState stone = Blocks.STONE.defaultBlockState();
        BlockState water = Blocks.WATER.defaultBlockState();
        BlockState netherrack = Blocks.NETHERRACK.defaultBlockState();
        BlockState lava = Blocks.LAVA.defaultBlockState();
        BlockState fire = Blocks.FIRE.defaultBlockState();

        // Gold: clear a few layers above origin for surface access
        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < 5; j++) {
                for (int k = 0; k < WIDTH; k++) {
                    StructureHelper.set(level, cposx + i, cposy - j, cposz + k, air);
                }
            }
        }

        // Sandstone roof at y+height with center hole
        for (int i = 0; i < WIDTH; i++) {
            for (int k = 0; k < WIDTH; k++) {
                StructureHelper.set(level, cposx + i, cposy + SHAFT_HEIGHT, cposz + k, sandstone);
            }
        }
        StructureHelper.set(
                level, cposx + WIDTH / 2, cposy + SHAFT_HEIGHT, cposz + WIDTH / 2, air);

        // Sandstone surface shaft walls y.y+height-1
        for (int i = 0; i < WIDTH; i++) {
            for (int k = 0; k < WIDTH; k++) {
                for (int j = 0; j < SHAFT_HEIGHT; j++) {
                    if (k != 0 && k != WIDTH - 1 && i != 0 && i != WIDTH - 1) {
                        StructureHelper.set(level, cposx + i, cposy + j, cposz + k, air);
                    } else {
                        StructureHelper.set(level, cposx + i, cposy + j, cposz + k, sandstone);
                    }
                }
            }
        }

        // Stone drop shaft y-1 . y-(depth-1)
        for (int i = 0; i < WIDTH; i++) {
            for (int k = 0; k < WIDTH; k++) {
                for (int j = -1; j > -DEPTH; j--) {
                    if (k != 0 && k != WIDTH - 1 && i != 0 && i != WIDTH - 1) {
                        StructureHelper.set(level, cposx + i, cposy + j, cposz + k, air);
                    } else {
                        StructureHelper.set(level, cposx + i, cposy + j, cposz + k, stone);
                    }
                }
            }
        }

        // Water sump interior at y-depth and y-(depth+1)
        for (int i = 1; i < WIDTH - 1; i++) {
            for (int k = 1; k < WIDTH - 1; k++) {
                for (int j = -DEPTH; j > -(DEPTH + 2); j--) {
                    StructureHelper.set(level, cposx + i, cposy + j, cposz + k, water);
                }
            }
        }
        // Stone floor under water
        for (int i = 1; i < WIDTH - 1; i++) {
            for (int k = 1; k < WIDTH - 1; k++) {
                StructureHelper.set(level, cposx + i, cposy - (DEPTH + 2), cposz + k, stone);
            }
        }

        // Netherrack room shell
        int rx = cposx + WIDTH + TUNNEL_LENGTH - 2;
        int rz = cposz - ROOM_WIDTH / 2;
        int ry = cposy - DEPTH;
        for (int i = 0; i < ROOM_LENGTH; i++) {
            for (int k = 0; k < ROOM_WIDTH; k++) {
                for (int j = 0; j < ROOM_HEIGHT; j++) {
                    boolean shell = k == 0
                            || k == ROOM_WIDTH - 1
                            || j == 0
                            || j == ROOM_HEIGHT - 1
                            || i == 0
                            || i == ROOM_LENGTH - 1;
                    StructureHelper.set(
                            level,
                            rx + i,
                            ry + j,
                            rz + k,
                            shell ? netherrack : air);
                }
            }
        }

        // East tunnel from shaft into room (lava mid-walls, stone floor/ceiling)
        int tx = cposx + WIDTH - 1;
        int tz = cposz;
        int ty = cposy - DEPTH;
        for (int i = 0; i < TUNNEL_LENGTH; i++) {
            for (int k = 0; k < WIDTH; k++) {
                for (int j = 0; j < WIDTH; j++) {
                    if (k != 0 && k != WIDTH - 1 && j != 0 && j != WIDTH - 1) {
                        StructureHelper.set(level, tx + i, ty + j, tz + k, air);
                    } else {
                        BlockState blk = stone;
                        if (j > 0 && j < WIDTH - 1) {
                            blk = lava;
                        }
                        StructureHelper.set(level, tx + i, ty + j, tz + k, blk);
                    }
                }
            }
        }

        // Room floor decorations (y was pre-incremented in gold after room base)
        int floorY = ry + 1;
        addLavaSquare(level, rx + 2, floorY, rz + 2);
        addLavaSquare(level, rx + 4, floorY, rz + 6);
        addLavaSquare(level, rx + 12, floorY, rz + 10);
        addLavaSquare(level, rx + 6, floorY, rz + 15);
        addLavaSquare(level, rx + 3, floorY, rz + 22);

        addKyuubi(
                level,
                random,
                rx + ROOM_LENGTH / 4,
                floorY,
                rz + ROOM_WIDTH * 3 / 4 - 3);
        addBlaze(
                level,
                random,
                rx + ROOM_LENGTH * 2 / 3 - 3,
                floorY,
                rz + ROOM_WIDTH / 4 - 2);

        StructureHelper.set(level, rx + 7, floorY, rz + 1, fire);
        StructureHelper.set(level, rx + 5, floorY, rz + 9, fire);
        StructureHelper.set(level, rx + 2, floorY, rz + 12, fire);
        StructureHelper.set(level, rx + 16, floorY, rz + 18, fire);
        StructureHelper.set(level, rx + 2, floorY, rz + 27, fire);
        StructureHelper.set(level, rx + 18, floorY, rz + 28, fire);

        return true;
    }

    /** Gold {@code addlavasquare}: netherrack cross with center lava. */
    private static void addLavaSquare(WorldGenLevel level, int x, int y, int z) {
        BlockState netherrack = Blocks.NETHERRACK.defaultBlockState();
        BlockState lava = Blocks.LAVA.defaultBlockState();
        StructureHelper.set(level, x - 1, y, z, netherrack);
        StructureHelper.set(level, x + 1, y, z, netherrack);
        StructureHelper.set(level, x, y, z + 1, netherrack);
        StructureHelper.set(level, x, y, z - 1, netherrack);
        StructureHelper.set(level, x, y, z, lava);
    }

    /** Gold {@code addkyuubi}: 9×9 then 7×7 nether-brick rings over lava, 3 spawners + chest. */
    private static void addKyuubi(WorldGenLevel level, RandomSource random, int x, int y, int z) {
        int width = 9;
        BlockState lava = Blocks.LAVA.defaultBlockState();
        BlockState netherBrick = Blocks.NETHER_BRICKS.defaultBlockState();

        for (int i = 0; i < width; i++) {
            for (int k = 0; k < width; k++) {
                if (k != 0 && k != width - 1 && i != 0 && i != width - 1) {
                    StructureHelper.set(level, x + i, y, z + k, lava);
                } else {
                    StructureHelper.set(level, x + i, y, z + k, netherBrick);
                }
            }
        }

        int inner = 7;
        for (int i = 0; i < inner; i++) {
            for (int k = 0; k < inner; k++) {
                if (k != 0 && k != inner - 1 && i != 0 && i != inner - 1) {
                    StructureHelper.set(level, x + i + 1, y + 1, z + k + 1, lava);
                } else {
                    StructureHelper.set(level, x + i + 1, y + 1, z + k + 1, netherBrick);
                }
            }
        }

        for (int j = 0; j < 3; j++) {
            placeSpawner(level, random, x + 4, y + j + 2, z + 4, ModEntities.KYUUBI.get());
        }

        placeChest(level, random, x + 4, y + 5, z + 4, Direction.NORTH, LOOT_TABLE);
    }

    /** Gold {@code addblaze}: stepped obsidian tower, Blaze spawners, 4 chests. */
    private static void addBlaze(WorldGenLevel level, RandomSource random, int x, int y, int z) {
        int width = 7;
        int height = 4;
        int xx = x;
        int yy = y;
        int zz = z;
        BlockState obsidian = Blocks.OBSIDIAN.defaultBlockState();

        for (int i = 0; i < width; i++) {
            for (int k = 0; k < width; k++) {
                for (int j = 0; j < height; j++) {
                    StructureHelper.set(level, xx + i, yy + j, zz + k, obsidian);
                }
            }
        }

        xx++;
        yy += height;
        zz++;
        int w2 = 5;
        int h2 = 1;
        for (int i = 0; i < w2; i++) {
            for (int k = 0; k < w2; k++) {
                for (int j = 0; j < h2; j++) {
                    StructureHelper.set(level, xx + i, yy + j, zz + k, obsidian);
                }
            }
        }

        xx++;
        yy += h2;
        zz++;
        int w3 = 3;
        int h3 = 6;
        for (int i = 0; i < w3; i++) {
            for (int k = 0; k < w3; k++) {
                for (int j = 0; j < h3; j++) {
                    StructureHelper.set(level, xx + i, yy + j, zz + k, obsidian);
                }
            }
        }

        xx++;
        yy += h3;
        zz++;
        int w4 = 1;
        int h4 = 5;
        for (int i = 0; i < w4; i++) {
            for (int k = 0; k < w4; k++) {
                for (int j = 0; j < h4; j++) {
                    StructureHelper.set(level, xx + i, yy + j, zz + k, obsidian);
                }
            }
        }

        // Gold: 2 layers of 4 Blaze spawners around the tip
        for (int j = 0; j < 2; j++) {
            int sy = yy + h4 + j - 3;
            placeSpawner(level, random, xx - 1, sy, zz, EntityType.BLAZE);
            placeSpawner(level, random, xx + 1, sy, zz, EntityType.BLAZE);
            placeSpawner(level, random, xx, sy, zz - 1, EntityType.BLAZE);
            placeSpawner(level, random, xx, sy, zz + 1, EntityType.BLAZE);
        }

        placeChest(level, random, x, y + 4, z + 3, Direction.WEST, BLAZE_LOOT_TABLE);
        placeChest(level, random, x + 3, y + 4, z, Direction.NORTH, BLAZE_LOOT_TABLE);
        placeChest(level, random, x + 3, y + 4, z + 6, Direction.SOUTH, BLAZE_LOOT_TABLE);
        placeChest(level, random, x + 6, y + 4, z + 3, Direction.EAST, BLAZE_LOOT_TABLE);
    }

    private static void placeSpawner(
            WorldGenLevel level, RandomSource random, int x, int y, int z, EntityType<?> type) {
        BlockPos pos = new BlockPos(x, y, z);
        StructureHelper.set(level, pos, Blocks.SPAWNER.defaultBlockState());
        if (level.getBlockEntity(pos) instanceof SpawnerBlockEntity spawner) {
            spawner.setEntityId(type, random);
        }
    }

    private static void placeChest(
            WorldGenLevel level,
            RandomSource random,
            int x,
            int y,
            int z,
            Direction facing,
            ResourceKey<LootTable> loot) {
        BlockPos pos = new BlockPos(x, y, z);
        BlockState chest = Blocks.CHEST.defaultBlockState().setValue(ChestBlock.FACING, facing);
        StructureHelper.set(level, pos, chest);
        RandomizableContainer.setBlockEntityLootTable(level, random, pos, loot);
    }
}
