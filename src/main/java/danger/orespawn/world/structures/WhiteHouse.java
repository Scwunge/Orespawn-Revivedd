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
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.storage.loot.LootTable;

/**
 * Gold {@code GenericDungeon.makeWhiteHouse}: dual quartz fountains, walkway, 25×25
 * quartz base/house, glass-pane walls, stepped sea-lantern roof with fence mast,
 * bedroom-style beds (quartz stairs + orange wool), four Criminal ({@code BandP})
 * spawners + chests at the rear.
 * <p>
 * Gold {@code DungeonSpawnerBlock} type <b>41</b>. Origin is the seed point used by
 * gold helpers (house base sits at origin −4 X / −6 Z; fountains at −5/−15 and +15/−15).
 */
public final class WhiteHouse {
    /** Gold base pad size ({@code makewhbase} 25×25). */
    public static final int BASE = 25;

    /** Wall shell size ({@code makewhwalls} 23×23). */
    public static final int WALL = 23;

    /** Roof peak relative Y: base roof at y+8, 13 steps → y+8+12. */
    public static final int ROOF_PEAK = 20;

    /** Full height including roof peak + torch. */
    public static final int HEIGHT = ROOF_PEAK + 2;

    /**
     * X span from leftmost fountain (origin−5) through base (origin−4 + 25) / right fountain
     * (origin+15+7) → roughly 28. Documented footprint for clear/centering.
     */
    public static final int WIDTH = 28;

    /**
     * Z span from fountain front (origin−15) through base rear (origin−6 + 25) → 34.
     */
    public static final int DEPTH = 34;

    /** Base pad offset from origin (gold {@code makewhbase(cposx-4, …, cposz-6)}). */
    public static final int BASE_OX = -4;
    public static final int BASE_OZ = -6;

    public static final ResourceKey<LootTable> LOOT_TABLE = ResourceKey.create(
            Registries.LOOT_TABLE,
            ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "chests/white_house"));

    private WhiteHouse() {}

    /**
     * Places the white house complex centered on gold origin {@code (cposx, cposy, cposz)}.
     *
     * @return true if written
     */
    public static boolean makeWhiteHouse(
            WorldGenLevel level, RandomSource random, int cposx, int cposy, int cposz) {
        int minY = level.getMinBuildHeight();
        int maxY = level.getMaxBuildHeight();
        if (cposy < minY || cposy + HEIGHT >= maxY) {
            return false;
        }

        // Clear full complex AABB (fountains + walkway + house + roof)
        // X: -5 . 22 (28), Z: -15 . 19 (34), Y: 0 . HEIGHT
        StructureHelper.clearBox(level, cposx - 5, cposy, cposz - 15, WIDTH, HEIGHT + 1, DEPTH);

        makeFountain(level, cposx - 5, cposy, cposz - 15);
        makeFountain(level, cposx + 15, cposy, cposz - 15);
        makeWalkway(level, cposx + 7, cposy, cposz - 15);
        makeWhBase(level, cposx - 4, cposy, cposz - 6);
        makeWhWalls(level, cposx - 3, cposy + 2, cposz - 5);
        makeWhRoof(level, cposx - 4, cposy, cposz - 6);
        makeWhInterior(level, random, cposx - 1, cposy + 2, cposz - 3);

        return true;
    }

    /** Gold {@code makefountain}: 7×5 quartz basin with water + center pillar. */
    private static void makeFountain(WorldGenLevel level, int cposx, int cposy, int cposz) {
        BlockState quartz = Blocks.QUARTZ_BLOCK.defaultBlockState();
        BlockState water = Blocks.WATER.defaultBlockState();
        BlockState air = Blocks.AIR.defaultBlockState();
        // Gold field_150426_aN → netherrack (matches other ports)
        BlockState netherrack = Blocks.NETHERRACK.defaultBlockState();

        for (int i = 0; i < 7; i++) {
            for (int k = 0; k < 5; k++) {
                for (int j = 0; j < 15; j++) {
                    BlockState bid = water;
                    if (i == 0 || k == 0 || i == 6 || k == 4) {
                        bid = quartz;
                    }
                    if (j == 0) {
                        bid = quartz;
                    }
                    if (j == 1 && i == 3 && k == 2) {
                        bid = netherrack;
                    }
                    if (j > 1) {
                        bid = air;
                        if (j <= 4 && i == 3 && k == 2) {
                            bid = quartz;
                        }
                    }
                    StructureHelper.set(level, cposx + i, cposy + j, cposz + k, bid);
                }
            }
        }
        // Gold water spout at pillar top (still + flowing stand-ins → water)
        StructureHelper.set(level, cposx + 3, cposy + 5, cposz + 2, water);
        StructureHelper.set(level, cposx + 2, cposy + 5, cposz + 2, water);
        StructureHelper.set(level, cposx + 4, cposy + 5, cposz + 2, water);
    }

    /** Gold {@code makewalkway}: 3×10 quartz path with raised rear pad. */
    private static void makeWalkway(WorldGenLevel level, int cposx, int cposy, int cposz) {
        BlockState quartz = Blocks.QUARTZ_BLOCK.defaultBlockState();
        BlockState air = Blocks.AIR.defaultBlockState();
        for (int i = 0; i < 3; i++) {
            for (int k = 0; k < 10; k++) {
                for (int j = 0; j < 15; j++) {
                    BlockState bid = quartz;
                    if (j == 1) {
                        bid = air;
                        if (k > 6) {
                            bid = quartz;
                        }
                    }
                    if (j > 1) {
                        bid = air;
                    }
                    StructureHelper.set(level, cposx + i, cposy + j, cposz + k, bid);
                }
            }
        }
    }

    /** Gold {@code makewhbase}: 25×25 quartz pad, crystal-torch corners. */
    private static void makeWhBase(WorldGenLevel level, int cposx, int cposy, int cposz) {
        BlockState quartz = Blocks.QUARTZ_BLOCK.defaultBlockState();
        BlockState crystalTorch = ModBlocks.CRYSTAL_TORCH.get().defaultBlockState();
        for (int i = 0; i < BASE; i++) {
            for (int k = 0; k < BASE; k++) {
                StructureHelper.set(level, cposx + i, cposy + 1, cposz + k, quartz);
                if ((i == 0 || i == 24) && (k == 0 || k == 24)) {
                    StructureHelper.set(level, cposx + i, cposy + 2, cposz + k, crystalTorch);
                }
            }
        }
        for (int i = 1; i < 24; i++) {
            for (int k = 1; k < 24; k++) {
                StructureHelper.set(level, cposx + i, cposy + 2, cposz + k, quartz);
            }
        }
    }

    /** Gold {@code makewhwalls}: 23×23×6 quartz shell with glass-pane windows. */
    private static void makeWhWalls(WorldGenLevel level, int cposx, int cposy, int cposz) {
        BlockState quartz = Blocks.QUARTZ_BLOCK.defaultBlockState();
        BlockState pane = Blocks.GLASS_PANE.defaultBlockState();
        BlockState air = Blocks.AIR.defaultBlockState();
        BlockState torch = Blocks.TORCH.defaultBlockState();

        for (int i = 0; i < WALL; i++) {
            for (int k = 0; k < WALL; k++) {
                for (int j = 0; j < 6; j++) {
                    BlockState bid = air;
                    if (i == 0 || k == 0 || i == 22 || k == 22) {
                        bid = quartz;
                    }
                    if (j != 0 && bid != air) {
                        if (k == 22) {
                            if ((j & 1) == 1) {
                                if ((i & 1) == 0 || (k & 1) == 0) {
                                    bid = pane;
                                }
                            } else if ((i & 1) == 1 || (k & 1) == 1) {
                                bid = pane;
                            }
                        } else if (k != 0) {
                            if ((j & 1) == 1) {
                                if (i == 2 || k == 2 || i == 20 || k == 20) {
                                    bid = pane;
                                }
                            } else if (i == 1 || k == 1 || i == 21 || k == 21) {
                                bid = pane;
                            }
                            if (j > 0 && j < 5 && k > 7 && k < 15) {
                                bid = pane;
                            }
                        } else if ((j & 1) == 1) {
                            if (i == 2 || k == 2 || i == 20 || k == 20) {
                                bid = pane;
                            }
                        } else if (i == 1 || k == 1 || i == 21 || k == 21) {
                            bid = pane;
                        }
                    }
                    StructureHelper.set(level, cposx + i, cposy + j, cposz + k, bid);
                }
            }
        }

        // Gold iron door on −Z face at x+11, torch outside
        StructureHelper.set(level, cposx + 11, cposy, cposz, air);
        StructureHelper.set(level, cposx + 11, cposy + 1, cposz, air);
        placeIronDoor(level, cposx + 11, cposy, cposz, Direction.SOUTH);
        StructureHelper.set(level, cposx + 12, cposy + 1, cposz - 1, torch);
    }

    /** Gold {@code makewhroof}: 13 stepped quartz frames, sea-lantern accents, fence mast. */
    private static void makeWhRoof(WorldGenLevel level, int cposx, int cposy, int cposz) {
        BlockState quartz = Blocks.QUARTZ_BLOCK.defaultBlockState();
        BlockState lantern = Blocks.SEA_LANTERN.defaultBlockState();
        BlockState air = Blocks.AIR.defaultBlockState();
        BlockState fence = Blocks.OAK_FENCE.defaultBlockState();
        BlockState crystalTorch = ModBlocks.CRYSTAL_TORCH.get().defaultBlockState();

        for (int j = 0; j < 13; j++) {
            int span = 25 - 2 * j;
            for (int i = 0; i < span; i++) {
                for (int k = 0; k < span; k++) {
                    BlockState bid = air;
                    if (i == 0 || k == 0 || i == 24 - 2 * j || k == 24 - 2 * j) {
                        bid = quartz;
                    }
                    if (j == 0 && bid != air && ((i + k) & 1) == 1) {
                        bid = lantern;
                    }
                    if (j == 12) {
                        bid = lantern;
                    }
                    StructureHelper.set(
                            level, cposx + i + j, cposy + 8 + j, cposz + k + j, bid);
                    if ((i == 0 || i == 24 - 2 * j) && (k == 0 || k == 24 - 2 * j)) {
                        StructureHelper.set(
                                level,
                                cposx + i + j,
                                cposy + 8 + j + 1,
                                cposz + k + j,
                                crystalTorch);
                    }
                }
            }
        }

        // Gold fence mast center
        for (int dy = 0; dy <= 11; dy++) {
            StructureHelper.set(level, cposx + 12, cposy + 8 + dy, cposz + 12, fence);
        }
        StructureHelper.set(level, cposx + 11, cposy + 8, cposz + 12, fence);
        StructureHelper.set(level, cposx + 13, cposy + 8, cposz + 12, fence);
        StructureHelper.set(level, cposx + 12, cposy + 8, cposz + 11, fence);
        StructureHelper.set(level, cposx + 12, cposy + 8, cposz + 13, fence);
        StructureHelper.set(level, cposx + 11, cposy + 9, cposz + 12, crystalTorch);
        StructureHelper.set(level, cposx + 13, cposy + 9, cposz + 12, crystalTorch);
        StructureHelper.set(level, cposx + 12, cposy + 9, cposz + 11, crystalTorch);
        StructureHelper.set(level, cposx + 12, cposy + 9, cposz + 13, crystalTorch);
    }

    /**
     * Gold {@code makewhinterior}: six “beds” (quartz stairs + orange wool) and four
     * Criminal (BandP) spawner+chest pairs along the rear wall.
     */
    private static void makeWhInterior(
            WorldGenLevel level, RandomSource random, int cposx, int cposy, int cposz) {
        BlockState orangeWool = Blocks.ORANGE_WOOL.defaultBlockState();
        // Gold quartz stairs meta 3 → NORTH, meta 2 → SOUTH
        BlockState stairN = Blocks.QUARTZ_STAIRS
                .defaultBlockState()
                .setValue(StairBlock.FACING, Direction.NORTH);
        BlockState stairS = Blocks.QUARTZ_STAIRS
                .defaultBlockState()
                .setValue(StairBlock.FACING, Direction.SOUTH);

        placeBedRow(level, cposx, cposy, cposz + 1, 0, stairN, orangeWool, stairS);
        placeBedRow(level, cposx, cposy, cposz + 1, 11, stairN, orangeWool, stairS);
        placeBedRow(level, cposx, cposy, cposz + 7, 0, stairN, orangeWool, stairS);
        placeBedRow(level, cposx, cposy, cposz + 7, 11, stairN, orangeWool, stairS);
        placeBedRow(level, cposx, cposy, cposz + 13, 0, stairN, orangeWool, stairS);
        placeBedRow(level, cposx, cposy, cposz + 13, 11, stairN, orangeWool, stairS);

        // Gold rear Criminal spawners + chests at zoff=18, x = 2/6/12/16
        int rearZ = 18;
        placeCriminalChest(level, random, cposx + 2, cposy, cposz + rearZ);
        placeCriminalChest(level, random, cposx + 6, cposy, cposz + rearZ);
        placeCriminalChest(level, random, cposx + 12, cposy, cposz + rearZ);
        placeCriminalChest(level, random, cposx + 16, cposy, cposz + rearZ);
    }

    private static void placeBedRow(
            WorldGenLevel level,
            int cposx,
            int cposy,
            int z,
            int xOff,
            BlockState stairN,
            BlockState wool,
            BlockState stairS) {
        for (int i = 0; i < 8; i++) {
            StructureHelper.set(level, cposx + xOff + i, cposy, z, stairN);
            StructureHelper.set(level, cposx + xOff + i, cposy, z + 1, wool);
            StructureHelper.set(level, cposx + xOff + i, cposy, z + 2, wool);
            StructureHelper.set(level, cposx + xOff + i, cposy, z + 3, stairS);
        }
    }

    private static void placeCriminalChest(
            WorldGenLevel level, RandomSource random, int x, int y, int z) {
        placeSpawner(level, random, x, y + 1, z, criminal());
        placeChest(level, random, x, y, z, Direction.SOUTH);
    }

    private static EntityType<?> criminal() {
        // Gold spawner name "Criminal" → BandP
        return ModEntities.BANDP != null ? ModEntities.BANDP.get() : EntityType.VILLAGER;
    }

    private static void placeIronDoor(WorldGenLevel level, int x, int y, int z, Direction facing) {
        BlockState lower = Blocks.IRON_DOOR
                .defaultBlockState()
                .setValue(DoorBlock.FACING, facing)
                .setValue(DoorBlock.HALF, DoubleBlockHalf.LOWER)
                .setValue(DoorBlock.OPEN, false);
        BlockState upper = Blocks.IRON_DOOR
                .defaultBlockState()
                .setValue(DoorBlock.FACING, facing)
                .setValue(DoorBlock.HALF, DoubleBlockHalf.UPPER)
                .setValue(DoorBlock.OPEN, false);
        StructureHelper.set(level, x, y, z, lower);
        StructureHelper.set(level, x, y + 1, z, upper);
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
