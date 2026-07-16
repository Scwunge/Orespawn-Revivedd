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
import net.minecraft.world.level.block.LadderBlock;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.storage.loot.LootTable;

/**
 * Gold {@code GenericDungeon.makeIncaPyramid} (+ makepoolalter / makeincagraves / makeincagrave):
 * stepped stone/cobble/lapis base (41×31×10), four approach stairways, upper temple (21×11×9),
 * pool altars, Molenoid spawner, creeper-repellent posts, trapdoor/ladder shaft, grave rows.
 * <p>
 * Gold {@code DungeonSpawnerBlock} type <b>29</b>. Origin is SW corner of the base pad
 * (stairs extend −{@code BASE_HEIGHT} outside each side).
 * <p>
 * Stair fill-down loops are capped at {@code BASE_HEIGHT} steps (gold {@code while (var >= 0)}
 * breaks on non-air; finite because max height is 10).
 */
public final class IncaPyramid {
    /** Gold upper temple width. */
    public static final int WIDTH = 21;

    /** Gold upper temple depth (Z). */
    public static final int DEPTH = 11;

    /** Gold upper temple height. */
    public static final int HEIGHT = 9;

    /** Gold stepped base width. */
    public static final int BASE_WIDTH = 41;

    /** Gold stepped base depth. */
    public static final int BASE_DEPTH = 31;

    /** Gold stepped base height (also stair run length). */
    public static final int BASE_HEIGHT = 10;

    /**
     * Full clear width: base + stair overhangs (−BASE_HEIGHT . +BASE_WIDTH+BASE_HEIGHT).
     */
    public static final int FOOTPRINT_X = BASE_WIDTH + BASE_HEIGHT * 2;

    public static final int FOOTPRINT_Z = BASE_DEPTH + BASE_HEIGHT * 2;

    /** Total vertical: base + temple + decorative slab rim. */
    public static final int TOTAL_HEIGHT = BASE_HEIGHT + HEIGHT + 2;

    public static final ResourceKey<LootTable> LOOT_TABLE = ResourceKey.create(
            Registries.LOOT_TABLE,
            ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "chests/inca_pyramid"));

    private IncaPyramid() {}

    /**
     * Places the Inca pyramid with base SW corner at {@code (cposx, cposy, cposz)}.
     *
     * @return true if written
     */
    public static boolean makeIncaPyramid(
            WorldGenLevel level, RandomSource random, int cposx, int cposy, int cposz) {
        int minY = level.getMinBuildHeight();
        int maxY = level.getMaxBuildHeight();
        if (cposy < minY || cposy + TOTAL_HEIGHT >= maxY) {
            return false;
        }

        // Clear base + stair overhangs + temple volume
        StructureHelper.clearBox(
                level,
                cposx - BASE_HEIGHT,
                cposy,
                cposz - BASE_HEIGHT,
                FOOTPRINT_X,
                TOTAL_HEIGHT,
                FOOTPRINT_Z);

        BlockState air = Blocks.AIR.defaultBlockState();
        BlockState stone = Blocks.STONE.defaultBlockState();
        BlockState cobble = Blocks.COBBLESTONE.defaultBlockState();
        BlockState lapis = Blocks.LAPIS_BLOCK.defaultBlockState();
        BlockState stoneBricks = Blocks.STONE_BRICKS.defaultBlockState();
        BlockState torch = Blocks.TORCH.defaultBlockState();
        BlockState wallTorchN = Blocks.WALL_TORCH
                .defaultBlockState()
                .setValue(net.minecraft.world.level.block.WallTorchBlock.FACING, Direction.NORTH);
        BlockState wallTorchS = Blocks.WALL_TORCH
                .defaultBlockState()
                .setValue(net.minecraft.world.level.block.WallTorchBlock.FACING, Direction.SOUTH);
        BlockState slab = Blocks.SMOOTH_STONE_SLAB.defaultBlockState();
        BlockState fence = Blocks.OAK_FENCE.defaultBlockState();
        BlockState netherBricks = Blocks.NETHER_BRICKS.defaultBlockState();

        // Gold stepped base j=0.baseheight-1
        for (int j = 0; j < BASE_HEIGHT; j++) {
            int layerW = BASE_WIDTH - j * 2;
            int layerD = BASE_DEPTH - j * 2;
            for (int i = 0; i < layerW; i++) {
                for (int k = 0; k < layerD; k++) {
                    BlockState bid = air;
                    if (i == 0 || k == 0 || i == layerW - 1 || k == layerD - 1) {
                        bid = stone;
                        if (random.nextInt(2) == 0) {
                            bid = cobble;
                        }
                        if (random.nextInt(4) == 0) {
                            bid = lapis;
                        }
                    }
                    if (j == 0) {
                        bid = stoneBricks;
                    }
                    // Gold wall torch on near-Z wall when j%3==2 (meta 3 = facing south in 1.7)
                    if (k == 1 && j % 3 == 2 && i != 0 && i != layerW - 1) {
                        StructureHelper.set(
                                level, cposx + i + j, cposy + j, cposz + k + j, wallTorchS);
                    } else {
                        StructureHelper.set(level, cposx + i + j, cposy + j, cposz + k + j, bid);
                    }
                    // Far-Z wall torch (meta 4 = facing north) one block inward
                    if (k == layerD - 1 && j % 3 == 2 && i != 0 && i != layerW - 1) {
                        StructureHelper.set(
                                level,
                                cposx + i + j,
                                cposy + j,
                                cposz + k + j - 1,
                                wallTorchN);
                    }
                }
            }
        }

        // Four approach stairways (finite: m < baseheight*2-1; fill-down capped)
        placeApproachStairsWest(level, cposx, cposy, cposz, stone, stoneBricks, slab, torch);
        placeApproachStairsEast(level, cposx, cposy, cposz, stone, stoneBricks, slab, torch);
        placeApproachStairsNorth(level, cposx, cposy, cposz, stone, stoneBricks, slab, torch);
        placeApproachStairsSouth(level, cposx, cposy, cposz, stone, stoneBricks, slab, torch);

        // Gold shifts origin to temple SW after base
        int tx = cposx + BASE_HEIGHT;
        int ty = cposy + BASE_HEIGHT;
        int tz = cposz + BASE_HEIGHT;

        for (int j = 0; j < HEIGHT; j++) {
            for (int i = 0; i < WIDTH; i++) {
                for (int k = 0; k < DEPTH; k++) {
                    BlockState bid = air;
                    if (i == 0 || k == 0 || i == WIDTH - 1 || k == DEPTH - 1) {
                        bid = stone;
                        if (random.nextInt(2) == 0) {
                            bid = cobble;
                        }
                        if (random.nextInt(4) == 0) {
                            bid = lapis;
                        }
                    }
                    if (j == 0 || j == HEIGHT - 1) {
                        bid = stoneBricks;
                    }
                    if (j == 1 || j == 2 || j == 3) {
                        if ((k == 0 || k == DEPTH - 1)
                                && i >= WIDTH / 2 - 1
                                && i <= WIDTH / 2 + 1) {
                            bid = (j == 3) ? fence : air;
                        }
                        if ((i == 0 || i == WIDTH - 1)
                                && k >= DEPTH / 2 - 1
                                && k <= DEPTH / 2 + 1) {
                            bid = (j == 3) ? fence : air;
                        }
                    }
                    if ((j == HEIGHT - 3 || j == HEIGHT - 2) && (i + k) % 2 == 1) {
                        if (j == HEIGHT - 3) {
                            if (!bid.isAir()) {
                                bid = netherBricks;
                            }
                        } else {
                            bid = air;
                        }
                    }
                    StructureHelper.set(level, tx + i, ty + j, tz + k, bid);
                }
            }
        }

        // Gold decorative slab rim at temple roof
        int rimY = ty + HEIGHT;
        for (int i = -1; i <= WIDTH; i++) {
            for (int k = -1; k <= DEPTH; k++) {
                if ((i == -1 || k == -1 || i == WIDTH || k == DEPTH) && ((i + k) & 1) == 1) {
                    StructureHelper.set(level, tx + i, rimY, tz + k, slab);
                }
            }
        }

        makePoolAltar(level, tx + 1, ty, tz + 1);
        makePoolAltar(level, tx + WIDTH - 2, ty, tz + DEPTH - 2);
        makePoolAltar(level, tx + 1, ty, tz + DEPTH - 2);
        makePoolAltar(level, tx + WIDTH - 2, ty, tz + 1);
        makePoolAltar(level, tx + WIDTH / 2, ty, tz + DEPTH / 2);

        // Gold CreeperRepellent on center pool corners
        BlockState repellent = ModBlocks.CREEPER_REPELLENT.get().defaultBlockState();
        StructureHelper.set(level, tx + WIDTH / 2 - 1, ty + 2, tz + DEPTH / 2 - 1, repellent);
        StructureHelper.set(level, tx + WIDTH / 2 + 1, ty + 2, tz + DEPTH / 2 + 1, repellent);
        StructureHelper.set(level, tx + WIDTH / 2 - 1, ty + 2, tz + DEPTH / 2 + 1, repellent);
        StructureHelper.set(level, tx + WIDTH / 2 + 1, ty + 2, tz + DEPTH / 2 - 1, repellent);

        placeSpawner(level, random, tx + WIDTH / 2 - 2, ty + 1, tz + DEPTH / 2, molenoid());

        // Gold trapdoor meta 3 + ladder shaft down into base
        int shaftX = tx + WIDTH / 2 + 2;
        int shaftZ = tz + DEPTH / 2;
        StructureHelper.set(
                level,
                shaftX,
                ty + 1,
                shaftZ,
                Blocks.OAK_TRAPDOOR
                        .defaultBlockState()
                        .setValue(TrapDoorBlock.FACING, Direction.SOUTH)
                        .setValue(TrapDoorBlock.HALF, Half.TOP)
                        .setValue(TrapDoorBlock.OPEN, false));
        StructureHelper.set(level, shaftX, ty, shaftZ, air);
        BlockState ladder = Blocks.LADDER
                .defaultBlockState()
                .setValue(LadderBlock.FACING, Direction.NORTH);
        for (int j = 1; j < BASE_HEIGHT; j++) {
            StructureHelper.set(level, shaftX, ty - j, shaftZ + 1, cobble);
            StructureHelper.set(level, shaftX, ty - j, shaftZ, ladder);
        }

        makeIncaGraves(level, random, cposx, cposy, cposz, BASE_WIDTH, BASE_DEPTH);

        return true;
    }

    /** Gold {@code makepoolalter}: 3×3 cobble with center water. */
    private static void makePoolAltar(WorldGenLevel level, int cposx, int cposy, int cposz) {
        BlockState cobble = Blocks.COBBLESTONE.defaultBlockState();
        for (int i = -1; i <= 1; i++) {
            for (int k = -1; k <= 1; k++) {
                StructureHelper.set(level, cposx + i, cposy + 1, cposz + k, cobble);
            }
        }
        StructureHelper.set(level, cposx, cposy + 1, cposz, Blocks.WATER.defaultBlockState());
    }

    /** Gold {@code makeincagraves}: four rows of graves on base pad. */
    private static void makeIncaGraves(
            WorldGenLevel level,
            RandomSource random,
            int cposx,
            int cposy,
            int cposz,
            int width,
            int depth) {
        for (int i = 5; i < width - 5; i += 6) {
            makeIncaGrave(level, random, cposx + i, cposy, cposz + 5, 1);
        }
        for (int i = 5; i < width - 5; i += 6) {
            makeIncaGrave(level, random, cposx + i, cposy, cposz + 10, 1);
        }
        for (int i = 5; i < width - 5; i += 6) {
            makeIncaGrave(level, random, cposx + i, cposy, cposz + 20, 3);
        }
        for (int i = 5; i < width - 5; i += 6) {
            makeIncaGrave(level, random, cposx + i, cposy, cposz + 25, 3);
        }
    }

    /**
     * Gold {@code makeincagrave}: grass/flower sides, stone/slab body, optional Ghost, chest.
     *
     * @param dir gold 1 = +Z body, 3 = −Z body
     */
    private static void makeIncaGrave(
            WorldGenLevel level, RandomSource random, int cposx, int cposy, int cposz, int dir) {
        BlockState grass = Blocks.GRASS_BLOCK.defaultBlockState();
        BlockState poppy = Blocks.POPPY.defaultBlockState();
        BlockState dandelion = Blocks.DANDELION.defaultBlockState();
        BlockState stone = Blocks.STONE.defaultBlockState();
        BlockState slab = Blocks.SMOOTH_STONE_SLAB.defaultBlockState();

        if (dir == 1) {
            StructureHelper.set(level, cposx - 1, cposy, cposz, grass);
            StructureHelper.set(level, cposx - 1, cposy + 1, cposz, poppy);
            StructureHelper.set(level, cposx - 1, cposy, cposz + 1, grass);
            StructureHelper.set(level, cposx - 1, cposy + 1, cposz + 1, dandelion);
            StructureHelper.set(level, cposx - 1, cposy, cposz + 2, grass);
            StructureHelper.set(level, cposx - 1, cposy + 1, cposz + 2, poppy);
            StructureHelper.set(level, cposx + 1, cposy, cposz, grass);
            StructureHelper.set(level, cposx + 1, cposy + 1, cposz, poppy);
            StructureHelper.set(level, cposx + 1, cposy, cposz + 1, grass);
            StructureHelper.set(level, cposx + 1, cposy + 1, cposz + 1, dandelion);
            StructureHelper.set(level, cposx + 1, cposy, cposz + 2, grass);
            StructureHelper.set(level, cposx + 1, cposy + 1, cposz + 2, poppy);
            StructureHelper.set(level, cposx, cposy + 1, cposz, stone);
            StructureHelper.set(level, cposx, cposy + 1, cposz + 1, slab);
            StructureHelper.set(level, cposx, cposy + 1, cposz + 2, slab);
            if (random.nextInt(3) == 1) {
                placeSpawner(level, random, cposx, cposy + 2, cposz, ghost());
            }
            // Gold chest meta 2 → facing north
            placeChest(level, random, cposx, cposy + 1, cposz - 1, Direction.NORTH);
        }

        if (dir == 3) {
            StructureHelper.set(level, cposx - 1, cposy, cposz, grass);
            StructureHelper.set(level, cposx - 1, cposy + 1, cposz, poppy);
            StructureHelper.set(level, cposx - 1, cposy, cposz - 1, grass);
            StructureHelper.set(level, cposx - 1, cposy + 1, cposz - 1, dandelion);
            StructureHelper.set(level, cposx - 1, cposy, cposz - 2, grass);
            StructureHelper.set(level, cposx - 1, cposy + 1, cposz - 2, poppy);
            StructureHelper.set(level, cposx + 1, cposy, cposz, grass);
            StructureHelper.set(level, cposx + 1, cposy + 1, cposz, poppy);
            StructureHelper.set(level, cposx + 1, cposy, cposz - 1, grass);
            StructureHelper.set(level, cposx + 1, cposy + 1, cposz - 1, dandelion);
            StructureHelper.set(level, cposx + 1, cposy, cposz - 2, grass);
            StructureHelper.set(level, cposx + 1, cposy + 1, cposz - 2, poppy);
            StructureHelper.set(level, cposx, cposy + 1, cposz, stone);
            StructureHelper.set(level, cposx, cposy + 1, cposz - 1, slab);
            StructureHelper.set(level, cposx, cposy + 1, cposz - 2, slab);
            if (random.nextInt(3) == 1) {
                placeSpawner(level, random, cposx, cposy + 2, cposz, ghost());
            }
            placeChest(level, random, cposx, cposy + 1, cposz + 1, Direction.SOUTH);
        }
    }

    private static void placeApproachStairsWest(
            WorldGenLevel level,
            int cposx,
            int cposy,
            int cposz,
            BlockState stone,
            BlockState stoneBricks,
            BlockState slab,
            BlockState torch) {
        for (int m = 0; m < BASE_HEIGHT * 2 - 1; m++) {
            int i = -BASE_HEIGHT + m;
            for (int p = -2; p <= 2; p++) {
                int k = BASE_DEPTH / 2 + p;
                int yOff = m / 2;
                if (p >= -1 && p <= 1) {
                    if (m % 2 == 1) {
                        if (isAir(level, cposx + i, cposy + yOff + 1, cposz + k)) {
                            StructureHelper.set(
                                    level, cposx + i, cposy + yOff + 1, cposz + k, slab);
                        }
                    }
                } else {
                    if (isAir(level, cposx + i, cposy + yOff + 1, cposz + k)) {
                        StructureHelper.set(
                                level, cposx + i, cposy + yOff + 1, cposz + k, stoneBricks);
                        if (m == 0 || m == BASE_HEIGHT * 2 - 2) {
                            StructureHelper.set(
                                    level, cposx + i, cposy + yOff + 2, cposz + k, torch);
                        }
                    }
                }
                // Finite fill-down: at most BASE_HEIGHT steps
                int fill = yOff;
                int guard = 0;
                while (fill >= 0 && guard < BASE_HEIGHT + 1) {
                    if (!isAir(level, cposx + i, cposy + fill, cposz + k)) {
                        break;
                    }
                    StructureHelper.set(level, cposx + i, cposy + fill, cposz + k, stone);
                    fill--;
                    guard++;
                }
            }
        }
    }

    private static void placeApproachStairsEast(
            WorldGenLevel level,
            int cposx,
            int cposy,
            int cposz,
            BlockState stone,
            BlockState stoneBricks,
            BlockState slab,
            BlockState torch) {
        for (int m = 0; m < BASE_HEIGHT * 2 - 1; m++) {
            int i = BASE_WIDTH + BASE_HEIGHT - m - 1;
            for (int p = -2; p <= 2; p++) {
                int k = BASE_DEPTH / 2 + p;
                int yOff = m / 2;
                if (p >= -1 && p <= 1) {
                    if (m % 2 == 1) {
                        if (isAir(level, cposx + i, cposy + yOff + 1, cposz + k)) {
                            StructureHelper.set(
                                    level, cposx + i, cposy + yOff + 1, cposz + k, slab);
                        }
                    }
                } else {
                    if (isAir(level, cposx + i, cposy + yOff + 1, cposz + k)) {
                        StructureHelper.set(
                                level, cposx + i, cposy + yOff + 1, cposz + k, stoneBricks);
                        if (m == 0 || m == BASE_HEIGHT * 2 - 2) {
                            StructureHelper.set(
                                    level, cposx + i, cposy + yOff + 2, cposz + k, torch);
                        }
                    }
                }
                int fill = yOff;
                int guard = 0;
                while (fill >= 0 && guard < BASE_HEIGHT + 1) {
                    if (!isAir(level, cposx + i, cposy + fill, cposz + k)) {
                        break;
                    }
                    StructureHelper.set(level, cposx + i, cposy + fill, cposz + k, stone);
                    fill--;
                    guard++;
                }
            }
        }
    }

    private static void placeApproachStairsNorth(
            WorldGenLevel level,
            int cposx,
            int cposy,
            int cposz,
            BlockState stone,
            BlockState stoneBricks,
            BlockState slab,
            BlockState torch) {
        for (int m = 0; m < BASE_HEIGHT * 2 - 1; m++) {
            int k = -BASE_HEIGHT + m;
            for (int p = -2; p <= 2; p++) {
                int i = BASE_WIDTH / 2 + p;
                int yOff = m / 2;
                if (p >= -1 && p <= 1) {
                    if (m % 2 == 1) {
                        if (isAir(level, cposx + i, cposy + yOff + 1, cposz + k)) {
                            StructureHelper.set(
                                    level, cposx + i, cposy + yOff + 1, cposz + k, slab);
                        }
                    }
                } else {
                    if (isAir(level, cposx + i, cposy + yOff + 1, cposz + k)) {
                        StructureHelper.set(
                                level, cposx + i, cposy + yOff + 1, cposz + k, stoneBricks);
                        if (m == 0 || m == BASE_HEIGHT * 2 - 2) {
                            StructureHelper.set(
                                    level, cposx + i, cposy + yOff + 2, cposz + k, torch);
                        }
                    }
                }
                int fill = yOff;
                int guard = 0;
                while (fill >= 0 && guard < BASE_HEIGHT + 1) {
                    if (!isAir(level, cposx + i, cposy + fill, cposz + k)) {
                        break;
                    }
                    StructureHelper.set(level, cposx + i, cposy + fill, cposz + k, stone);
                    fill--;
                    guard++;
                }
            }
        }
    }

    private static void placeApproachStairsSouth(
            WorldGenLevel level,
            int cposx,
            int cposy,
            int cposz,
            BlockState stone,
            BlockState stoneBricks,
            BlockState slab,
            BlockState torch) {
        for (int m = 0; m < BASE_HEIGHT * 2 - 1; m++) {
            int k = BASE_DEPTH + BASE_HEIGHT - m - 1;
            for (int p = -2; p <= 2; p++) {
                int i = BASE_WIDTH / 2 + p;
                int yOff = m / 2;
                if (p >= -1 && p <= 1) {
                    if (m % 2 == 1) {
                        if (isAir(level, cposx + i, cposy + yOff + 1, cposz + k)) {
                            StructureHelper.set(
                                    level, cposx + i, cposy + yOff + 1, cposz + k, slab);
                        }
                    }
                } else {
                    if (isAir(level, cposx + i, cposy + yOff + 1, cposz + k)) {
                        StructureHelper.set(
                                level, cposx + i, cposy + yOff + 1, cposz + k, stoneBricks);
                        if (m == 0 || m == BASE_HEIGHT * 2 - 2) {
                            StructureHelper.set(
                                    level, cposx + i, cposy + yOff + 2, cposz + k, torch);
                        }
                    }
                }
                int fill = yOff;
                int guard = 0;
                while (fill >= 0 && guard < BASE_HEIGHT + 1) {
                    if (!isAir(level, cposx + i, cposy + fill, cposz + k)) {
                        break;
                    }
                    StructureHelper.set(level, cposx + i, cposy + fill, cposz + k, stone);
                    fill--;
                    guard++;
                }
            }
        }
    }

    private static boolean isAir(WorldGenLevel level, int x, int y, int z) {
        int minY = level.getMinBuildHeight();
        int maxY = level.getMaxBuildHeight();
        if (y < minY || y >= maxY) {
            return false;
        }
        return level.getBlockState(new BlockPos(x, y, z)).isAir();
    }

    private static EntityType<?> molenoid() {
        return ModEntities.MOLENOID != null ? ModEntities.MOLENOID.get() : EntityType.SILVERFISH;
    }

    private static EntityType<?> ghost() {
        return ModEntities.GHOST != null ? ModEntities.GHOST.get() : EntityType.VEX;
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
