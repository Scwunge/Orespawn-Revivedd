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
 * Gold {@code GenericDungeon.makeEnormousCastle}: 28×28 iron-bar shell on stone pad,
 * bedrock roof, Extreme Torches, corner Terrible Terror stacks, center Emperor Scorpion
 * stacks, west quartz platform + stair ramp, and up to six stacked {@code buildLevel}
 * towers (Cloud Shark → Mothra) based on random difficulty 1.6.
 * <p>
 * Gold {@code DungeonSpawnerBlock} type <b>2</b>. Origin is min-corner of the main
 * 28×28 shell (platform extends west to about X−37; fence pad −4.width+3).
 * <p>
 * Helpers {@code buildLevel} / {@code addLevelDecorations} / {@code fill_chests} are
 * inlined as private methods (gold instance methods on {@code GenericDungeon}).
 */
public final class EnormousCastle {
    /** Gold {@code width = 28}. */
    public static final int WIDTH = 28;

    /** Gold base shell height {@code height = 16}. */
    public static final int BASE_HEIGHT = 16;

    /** Gold west platform width {@code platformwidth = 11}. */
    public static final int PLATFORM_WIDTH = 11;

    /**
     * Max stacked tower top relative to origin when level=6:
     * 16 + 10+10+9+9+8+16 = 78, plus decor (~+3) → 81.
     */
    public static final int HEIGHT = 81;

    /**
     * Full X clear: stairs end near −37, shell fence to width+3.
     * width = 37 + WIDTH + 3 + 1 ≈ 69.
     */
    public static final int FOOTPRINT_X = 69;

    /** Clear origin X offset from cposx (stairs west). */
    public static final int CLEAR_OX = -37;

    /** Z clear: fence −4 . width+3 → depth 36. */
    public static final int FOOTPRINT_Z = 36;

    public static final int CLEAR_OZ = -4;

    public static final ResourceKey<LootTable> LOOT_TABLE = ResourceKey.create(
            Registries.LOOT_TABLE,
            ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "chests/enormous_castle"));

    public static final ResourceKey<LootTable> LOOT_TOP = ResourceKey.create(
            Registries.LOOT_TABLE,
            ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "chests/enormous_castle_top"));

    private EnormousCastle() {}

    /**
     * Places the enormous castle with min-corner origin at {@code (cposx, cposy, cposz)}.
     *
     * @return true if written
     */
    public static boolean makeEnormousCastle(
            WorldGenLevel level, RandomSource random, int cposx, int cposy, int cposz) {
        int minY = level.getMinBuildHeight();
        int maxY = level.getMaxBuildHeight();
        // Worms at y-1; tower up to HEIGHT
        if (cposy - 1 < minY || cposy + HEIGHT >= maxY) {
            return false;
        }

        int width = WIDTH;
        int height = BASE_HEIGHT;

        // Gold difficulty 1.6 with bias toward higher when rolled low
        int difficulty = 1 + random.nextInt(6);
        if (difficulty <= 3 && random.nextInt(3) != 1) {
            difficulty += 3;
        }

        // Clear full AABB including upper tower stack and west stairs
        StructureHelper.clearBox(
                level,
                cposx + CLEAR_OX,
                cposy - 1,
                cposz + CLEAR_OZ,
                FOOTPRINT_X,
                HEIGHT + 2,
                FOOTPRINT_Z);

        BlockState air = Blocks.AIR.defaultBlockState();
        BlockState stone = Blocks.STONE.defaultBlockState();
        BlockState bedrock = Blocks.BEDROCK.defaultBlockState();
        BlockState ironBars = Blocks.IRON_BARS.defaultBlockState();
        BlockState fence = Blocks.OAK_FENCE.defaultBlockState();
        BlockState quartz = Blocks.QUARTZ_BLOCK.defaultBlockState();
        BlockState netherBrick = Blocks.NETHER_BRICKS.defaultBlockState();
        BlockState fire = Blocks.FIRE.defaultBlockState();
        BlockState extremeTorch = ModBlocks.EXTREME_TORCH.get().defaultBlockState();

        // Gold air clear interior already done via clearBox

        // Gold stone floor
        for (int i = 0; i < width; i++) {
            for (int k = 0; k < width; k++) {
                StructureHelper.set(level, cposx + i, cposy, cposz + k, stone);
            }
        }

        // Gold bedrock roof
        for (int i = 0; i < width; i++) {
            for (int k = 0; k < width; k++) {
                StructureHelper.set(level, cposx + i, cposy + height, cposz + k, bedrock);
            }
        }

        // Gold iron-bar walls
        for (int i = 0; i < width; i++) {
            for (int j = 1; j < height; j++) {
                StructureHelper.set(level, cposx + i, cposy + j, cposz, ironBars);
                StructureHelper.set(level, cposx + i, cposy + j, cposz + width - 1, ironBars);
            }
        }
        for (int k = 0; k < width; k++) {
            for (int j = 1; j < height; j++) {
                StructureHelper.set(level, cposx, cposy + j, cposz + k, ironBars);
                StructureHelper.set(level, cposx + width - 1, cposy + j, cposz + k, ironBars);
            }
        }

        // Gold Extreme Torches at inner corners
        StructureHelper.set(level, cposx + 1, cposy + 1, cposz + 1, extremeTorch);
        StructureHelper.set(level, cposx + 1, cposy + 1, cposz + width - 2, extremeTorch);
        StructureHelper.set(level, cposx + width - 2, cposy + 1, cposz + 1, extremeTorch);
        StructureHelper.set(level, cposx + width - 2, cposy + 1, cposz + width - 2, extremeTorch);

        // Gold outer stone apron + oak fence rim (−4.width+3)
        for (int i = -4; i < width + 4; i++) {
            for (int k = -4; k < width + 4; k++) {
                if (i < 0 || k < 0 || i >= width || k >= width) {
                    StructureHelper.set(level, cposx + i, cposy, cposz + k, stone);
                }
                if (i == -4 || k == -4 || i == width + 3 || k == width + 3) {
                    StructureHelper.set(level, cposx + i, cposy + 1, cposz + k, fence);
                }
            }
        }

        // Gold 4-high Terrible Terror stacks at outer corners
        for (int j = 0; j < 4; j++) {
            placeSpawner(
                    level, random, cposx - 3, cposy + 1 + j, cposz - 3, terribleTerror());
            placeSpawner(
                    level,
                    random,
                    cposx - 3,
                    cposy + 1 + j,
                    cposz + width + 2,
                    terribleTerror());
            placeSpawner(
                    level,
                    random,
                    cposx + width + 2,
                    cposy + 1 + j,
                    cposz - 3,
                    terribleTerror());
            placeSpawner(
                    level,
                    random,
                    cposx + width + 2,
                    cposy + 1 + j,
                    cposz + width + 2,
                    terribleTerror());
        }

        // Gold center Emperor Scorpion stack y+2.4
        placeSpawner(
                level, random, cposx + width / 2, cposy + 2, cposz + width / 2, emperorScorpion());
        placeSpawner(
                level, random, cposx + width / 2, cposy + 3, cposz + width / 2, emperorScorpion());
        placeSpawner(
                level, random, cposx + width / 2, cposy + 4, cposz + width / 2, emperorScorpion());

        // Gold stacked levels above roof
        int var31 = height;
        buildLevel(
                level,
                random,
                cposx + 1,
                cposy + var31,
                cposz + 1,
                width - 2,
                10,
                4,
                cloudShark(),
                1,
                -1,
                5,
                1,
                difficulty);
        var31 += 10;
        if (difficulty >= 2) {
            buildLevel(
                    level,
                    random,
                    cposx + 1,
                    cposy + var31,
                    cposz + 1,
                    width - 2,
                    10,
                    4,
                    lurkingTerror(),
                    0,
                    0,
                    4,
                    2,
                    difficulty);
        }
        var31 += 10;
        if (difficulty >= 3) {
            buildLevel(
                    level,
                    random,
                    cposx + 2,
                    cposy + var31,
                    cposz + 2,
                    width - 4,
                    9,
                    4,
                    rotator(),
                    1,
                    1,
                    4,
                    3,
                    difficulty);
        }
        var31 += 9;
        if (difficulty >= 4) {
            buildLevel(
                    level,
                    random,
                    cposx + 2,
                    cposy + var31,
                    cposz + 2,
                    width - 4,
                    9,
                    3,
                    bee(),
                    0,
                    0,
                    4,
                    4,
                    difficulty);
        }
        var31 += 9;
        if (difficulty >= 5) {
            buildLevel(
                    level,
                    random,
                    cposx + 3,
                    cposy + var31,
                    cposz + 3,
                    width - 6,
                    8,
                    3,
                    mantis(),
                    1,
                    1,
                    4,
                    5,
                    difficulty);
        }
        var31 += 8;
        if (difficulty >= 6) {
            buildLevel(
                    level,
                    random,
                    cposx + 3,
                    cposy + var31,
                    cposz + 3,
                    width - 6,
                    16,
                    3,
                    mothra(),
                    0,
                    0,
                    3,
                    6,
                    difficulty);
        }
        var31 += 16;

        // Gold west quartz platform at roof height
        int platformY = height;
        for (int i = 0; i < PLATFORM_WIDTH; i++) {
            for (int k = -(PLATFORM_WIDTH / 2); k <= PLATFORM_WIDTH / 2; k++) {
                StructureHelper.set(
                        level,
                        cposx + i - 20,
                        cposy + platformY,
                        cposz + k + width / 2,
                        quartz);
                boolean rim =
                        i == 0
                                || i == PLATFORM_WIDTH - 1
                                || k == -(PLATFORM_WIDTH / 2)
                                || k == PLATFORM_WIDTH / 2;
                if (rim && (i != 0 || k < -1 || k > 1)) {
                    StructureHelper.set(
                            level,
                            cposx + i - 20,
                            cposy + platformY + 1,
                            cposz + k + width / 2,
                            fence);
                }
            }
        }

        // Gold bridge −10.−3 toward shell
        for (int i = -10; i <= -3; i++) {
            for (int k = -2; k < 3; k++) {
                if (i != -3 && i != -10) {
                    StructureHelper.set(
                            level, cposx + i, cposy + platformY, cposz + k + width / 2, quartz);
                    if (k == -2 || k == 2) {
                        StructureHelper.set(
                                level,
                                cposx + i,
                                cposy + platformY + 1,
                                cposz + k + width / 2,
                                fence);
                    }
                } else if (k != -2 && k != 2) {
                    StructureHelper.set(
                            level, cposx + i, cposy + platformY + 1, cposz + k + width / 2, air);
                } else {
                    StructureHelper.set(
                            level,
                            cposx + i,
                            cposy + platformY + 1,
                            cposz + k + width / 2,
                            netherBrick);
                    StructureHelper.set(
                            level,
                            cposx + i,
                            cposy + platformY + 2,
                            cposz + k + width / 2,
                            netherBrick);
                    StructureHelper.set(
                            level, cposx + i, cposy + platformY + 3, cposz + k + width / 2, fire);
                }
            }
        }

        // Gold stair ramp from roof west down to ground
        int sx = -21;
        for (int y = height; y >= 0; y--) {
            for (int k = -2; k < 3; k++) {
                for (int t = 0; t < 6; t++) {
                    StructureHelper.set(
                            level, cposx + sx, cposy + y + t + 1, cposz + k + width / 2, air);
                }
                if (y == 0) {
                    if (k != -2 && k != 2) {
                        StructureHelper.set(
                                level, cposx + sx, cposy + 1, cposz + k + width / 2, air);
                    } else {
                        StructureHelper.set(
                                level, cposx + sx, cposy + 1, cposz + k + width / 2, netherBrick);
                        StructureHelper.set(
                                level, cposx + sx, cposy + 2, cposz + k + width / 2, netherBrick);
                        StructureHelper.set(
                                level, cposx + sx, cposy + 3, cposz + k + width / 2, fire);
                    }
                } else {
                    StructureHelper.set(
                            level, cposx + sx, cposy + y, cposz + k + width / 2, quartz);
                    if (k == -2 || k == 2) {
                        StructureHelper.set(
                                level, cposx + sx, cposy + y + 1, cposz + k + width / 2, fence);
                    }
                }
            }
            sx--;
        }

        // Gold level-6 buried Large Worm spawners around site (100 tries, outer ring)
        if (difficulty >= 6) {
            int span = width * 3;
            for (int tries = 0; tries < 100; tries++) {
                int rx = random.nextInt(span);
                int rz = random.nextInt(span);
                if (rx < span / 4 || rx > span * 3 / 4 || rz < span / 4 || rz > span * 3 / 4) {
                    rx -= span / 2;
                    rz -= span / 2;
                    placeSpawner(
                            level,
                            random,
                            cposx + rx + width / 2,
                            cposy - 1,
                            cposz + rz + width / 2,
                            largeWorm());
                }
            }
        }

        return true;
    }

    /**
     * Gold {@code buildLevel}: square bedrock box with stone apron, fence rim, stair
     * notch, corner critter stacks, and {@code addLevelDecorations}.
     */
    private static void buildLevel(
            WorldGenLevel level,
            RandomSource random,
            int cposx,
            int cposy,
            int cposz,
            int width,
            int height,
            int pw,
            EntityType<?> critter,
            int stepside,
            int stepoff,
            int holelen,
            int decor,
            int difficulty) {
        BlockState air = Blocks.AIR.defaultBlockState();
        BlockState stone = Blocks.STONE.defaultBlockState();
        BlockState bedrock = Blocks.BEDROCK.defaultBlockState();
        BlockState gold = Blocks.GOLD_BLOCK.defaultBlockState();
        BlockState fence = Blocks.OAK_FENCE.defaultBlockState();

        // Interior air (already cleared globally; re-assert shell volume)
        for (int i = -pw; i < width + pw; i++) {
            for (int j = 1; j < height; j++) {
                for (int k = -pw; k < width + pw; k++) {
                    StructureHelper.set(level, cposx + i, cposy + j, cposz + k, air);
                }
            }
        }

        // Floor / roof bedrock
        for (int i = 0; i < width; i++) {
            for (int k = 0; k < width; k++) {
                StructureHelper.set(level, cposx + i, cposy, cposz + k, bedrock);
                StructureHelper.set(level, cposx + i, cposy + height, cposz + k, bedrock);
            }
        }

        // Z walls bedrock
        for (int i = 0; i < width; i++) {
            for (int j = 1; j < height; j++) {
                StructureHelper.set(level, cposx + i, cposy + j, cposz, bedrock);
                StructureHelper.set(level, cposx + i, cposy + j, cposz + width - 1, bedrock);
            }
        }

        // X walls bedrock with gold-block corners
        for (int k = 0; k < width; k++) {
            for (int j = 1; j < height; j++) {
                BlockState blk = bedrock;
                if (k == 0 || k == width - 1) {
                    blk = gold;
                }
                StructureHelper.set(level, cposx, cposy + j, cposz + k, blk);
                StructureHelper.set(level, cposx + width - 1, cposy + j, cposz + k, blk);
            }
        }

        // Stone apron + fence rim
        for (int i = -pw; i < width + pw; i++) {
            for (int k = -pw; k < width + pw; k++) {
                if (i < 0 || k < 0 || i >= width || k >= width) {
                    StructureHelper.set(level, cposx + i, cposy, cposz + k, stone);
                }
                if (i == -pw || k == -pw || i == width + (pw - 1) || k == width + (pw - 1)) {
                    StructureHelper.set(level, cposx + i, cposy + 1, cposz + k, fence);
                }
            }
        }

        // Stair step blocks
        int stepX = -(height / 2) + width / 2;
        for (int j = 1; j < height; j++) {
            if (stepside != 0) {
                StructureHelper.set(level, cposx + stepX, cposy + j, cposz - 1, stone);
            } else {
                StructureHelper.set(level, cposx + stepX, cposy + j, cposz + width, stone);
            }
            stepX++;
        }

        // Floor hole for vertical access
        if (stepoff >= 0) {
            int holeZ;
            if (stepside == 0) {
                holeZ = -1 - stepoff;
            } else {
                holeZ = width + stepoff;
            }
            int holeX = width / 2;
            for (int l = 0; l < holelen; l++) {
                StructureHelper.set(level, cposx + holeX + l, cposy, cposz + holeZ, air);
            }
        }

        // Corner critter stacks
        for (int j = 0; j < 4; j++) {
            placeSpawner(
                    level,
                    random,
                    cposx - (pw - 1),
                    cposy + j + 1,
                    cposz - (pw - 1),
                    critter);
            placeSpawner(
                    level,
                    random,
                    cposx - (pw - 1),
                    cposy + j + 1,
                    cposz + width + (pw - 2),
                    critter);
            placeSpawner(
                    level,
                    random,
                    cposx + width + (pw - 2),
                    cposy + j + 1,
                    cposz - (pw - 1),
                    critter);
            placeSpawner(
                    level,
                    random,
                    cposx + width + (pw - 2),
                    cposy + j + 1,
                    cposz + width + (pw - 2),
                    critter);
        }

        addLevelDecorations(level, random, cposx, cposy, cposz, width, height, decor, difficulty);
    }

    /** Gold {@code addLevelDecorations}: center bosses, pillars, RTP, chests by decor tier. */
    private static void addLevelDecorations(
            WorldGenLevel level,
            RandomSource random,
            int cposx,
            int cposy,
            int cposz,
            int width,
            int height,
            int decor,
            int difficulty) {
        BlockState air = Blocks.AIR.defaultBlockState();
        BlockState bedrock = Blocks.BEDROCK.defaultBlockState();
        BlockState dirt = Blocks.DIRT.defaultBlockState();
        BlockState netherBrick = Blocks.NETHER_BRICKS.defaultBlockState();
        BlockState fire = Blocks.FIRE.defaultBlockState();
        BlockState rtp = ModBlocks.BLOCK_TELEPORT.get().defaultBlockState();

        int reward = 1;
        EntityType<?> critter = alosaurus();

        if (decor == 6) {
            // Gold nether-brick/fire corners, Nightmare roof, dirt floor, Large Worm center
            StructureHelper.set(level, cposx, cposy + height, cposz, netherBrick);
            StructureHelper.set(level, cposx, cposy + height + 1, cposz, fire);
            StructureHelper.set(level, cposx, cposy + height, cposz + width - 1, netherBrick);
            StructureHelper.set(level, cposx, cposy + height + 1, cposz + width - 1, fire);
            StructureHelper.set(level, cposx + width - 1, cposy + height, cposz, netherBrick);
            StructureHelper.set(level, cposx + width - 1, cposy + height + 1, cposz, fire);
            StructureHelper.set(
                    level, cposx + width - 1, cposy + height, cposz + width - 1, netherBrick);
            StructureHelper.set(
                    level, cposx + width - 1, cposy + height + 1, cposz + width - 1, fire);
            StructureHelper.set(
                    level, cposx + width / 2, cposy + height, cposz + width / 2, air);
            placeSpawner(
                    level,
                    random,
                    cposx + width / 2 - 1,
                    cposy + height + 2,
                    cposz + width / 2,
                    nightmare());
            placeSpawner(
                    level,
                    random,
                    cposx + width / 2 + 1,
                    cposy + height + 2,
                    cposz + width / 2,
                    nightmare());
            placeSpawner(
                    level,
                    random,
                    cposx + width / 2,
                    cposy + height + 2,
                    cposz + width / 2 - 1,
                    nightmare());
            placeSpawner(
                    level,
                    random,
                    cposx + width / 2,
                    cposy + height + 2,
                    cposz + width / 2 + 1,
                    nightmare());
            for (int i = 1; i < width - 1; i++) {
                for (int j = 1; j < 5; j++) {
                    for (int k = 1; k < width - 1; k++) {
                        StructureHelper.set(level, cposx + i, cposy + j, cposz + k, dirt);
                    }
                }
            }
            placeSpawner(
                    level, random, cposx + width / 2, cposy + 2, cposz + width / 2, largeWorm());
            placeSpawner(
                    level, random, cposx + width / 2, cposy + 3, cposz + width / 2, largeWorm());
            placeSpawner(
                    level, random, cposx + width / 2, cposy + 4, cposz + width / 2, largeWorm());
            for (int j = 0; j < 10; j++) {
                StructureHelper.set(level, cposx + 1, cposy + j, cposz + 1, air);
            }
            fillChests(level, random, cposx, cposy + 4, cposz, width, height, decor, 1);
            return;
        }

        if (decor == 5) {
            if (difficulty == 5) {
                critter = alosaurus();
                reward = 1;
            }
            if (difficulty == 6) {
                critter = trex();
                reward = 2;
            }
            placeCenterPillarBoss(level, random, cposx, cposy, cposz, width, height, critter, air);
            StructureHelper.set(level, cposx + width - 2, cposy, cposz + width - 2, air);
            StructureHelper.set(level, cposx + 1, cposy + height, cposz + 1, air);
            fillChests(level, random, cposx, cposy, cposz, width, height, decor, reward);
            return;
        }

        if (decor == 4) {
            if (difficulty == 4) {
                critter = alosaurus();
                reward = 1;
            }
            if (difficulty == 5) {
                critter = trex();
                reward = 2;
            }
            if (difficulty == 6) {
                critter = basilisk();
                reward = 3;
            }
            placeCenterPillarBoss(level, random, cposx, cposy, cposz, width, height, critter, air);
            StructureHelper.set(level, cposx + 1, cposy, cposz + 1, air);
            StructureHelper.set(level, cposx + width - 2, cposy + height, cposz + width - 2, air);
            fillChests(level, random, cposx, cposy, cposz, width, height, decor, reward);
            return;
        }

        if (decor == 3) {
            if (difficulty == 3) {
                critter = alosaurus();
                reward = 1;
            }
            if (difficulty == 4) {
                critter = trex();
                reward = 2;
            }
            if (difficulty == 5) {
                critter = basilisk();
                reward = 3;
            }
            if (difficulty == 6) {
                critter = hercules();
                reward = 4;
            }
            placeCenterPillarBoss(level, random, cposx, cposy, cposz, width, height, critter, air);
            StructureHelper.set(level, cposx + width - 2, cposy, cposz + width - 2, air);
            StructureHelper.set(level, cposx + 1, cposy + height, cposz + 1, air);
            fillChests(level, random, cposx, cposy, cposz, width, height, decor, reward);
            return;
        }

        if (decor == 2) {
            if (difficulty == 2) {
                critter = alosaurus();
                reward = 1;
            }
            if (difficulty == 3) {
                critter = trex();
                reward = 2;
            }
            if (difficulty == 4) {
                critter = basilisk();
                reward = 3;
            }
            if (difficulty == 5) {
                critter = hercules();
                reward = 4;
            }
            if (difficulty == 6) {
                critter = jumpyBug();
                reward = 5;
            }
            placeCenterPillarBoss(level, random, cposx, cposy, cposz, width, height, critter, air);
            StructureHelper.set(level, cposx + 1, cposy, cposz + 1, air);
            StructureHelper.set(level, cposx + width - 2, cposy + height, cposz + width - 2, air);
            fillChests(level, random, cposx, cposy, cposz, width, height, decor, reward);
            return;
        }

        if (decor == 1) {
            if (difficulty == 1) {
                critter = alosaurus();
            }
            if (difficulty == 2) {
                critter = trex();
            }
            if (difficulty == 3) {
                critter = basilisk();
            }
            if (difficulty == 4) {
                critter = hercules();
            }
            if (difficulty == 5) {
                critter = jumpyBug();
            }
            if (difficulty == 6) {
                critter = hammerhead();
            }
            reward = difficulty;
            placeCenterPillarBoss(level, random, cposx, cposy, cposz, width, height, critter, air);
            // Gold MyRTPBlock corners
            StructureHelper.set(
                    level, cposx + width / 2 - 1, cposy + 1, cposz + width / 2 - 1, rtp);
            StructureHelper.set(
                    level, cposx + width / 2 + 1, cposy + 1, cposz + width / 2 + 1, rtp);
            StructureHelper.set(
                    level, cposx + width / 2 + 1, cposy + 1, cposz + width / 2 - 1, rtp);
            StructureHelper.set(
                    level, cposx + width / 2 - 1, cposy + 1, cposz + width / 2 + 1, rtp);
            StructureHelper.set(level, cposx + 1, cposy + height, cposz + 1, air);
            fillChests(level, random, cposx, cposy, cposz, width, height, decor, reward);
        }
    }

    private static void placeCenterPillarBoss(
            WorldGenLevel level,
            RandomSource random,
            int cposx,
            int cposy,
            int cposz,
            int width,
            int height,
            EntityType<?> critter,
            BlockState air) {
        BlockState bedrock = Blocks.BEDROCK.defaultBlockState();
        placeSpawner(level, random, cposx + width / 2, cposy + 2, cposz + width / 2, critter);
        placeSpawner(level, random, cposx + width / 2, cposy + 3, cposz + width / 2, critter);
        for (int j = 1; j < 5; j++) {
            StructureHelper.set(
                    level, cposx + width / 2 - 1, cposy + j, cposz + width / 2, bedrock);
            StructureHelper.set(
                    level, cposx + width / 2 + 1, cposy + j, cposz + width / 2, bedrock);
            StructureHelper.set(
                    level, cposx + width / 2, cposy + j, cposz + width / 2 - 1, bedrock);
            StructureHelper.set(
                    level, cposx + width / 2, cposy + j, cposz + width / 2 + 1, bedrock);
        }
    }

    /** Gold {@code fill_chests}: four wall chests; reward 6 uses top loot table. */
    private static void fillChests(
            WorldGenLevel level,
            RandomSource random,
            int cposx,
            int cposy,
            int cposz,
            int width,
            int height,
            int decor,
            int reward) {
        ResourceKey<LootTable> loot = reward >= 6 ? LOOT_TOP : LOOT_TABLE;
        placeChest(level, random, cposx + 1, cposy + 1, cposz + width / 2, Direction.EAST, loot);
        placeChest(
                level, random, cposx + width - 2, cposy + 1, cposz + width / 2, Direction.WEST, loot);
        placeChest(level, random, cposx + width / 2, cposy + 1, cposz + 1, Direction.SOUTH, loot);
        placeChest(
                level,
                random,
                cposx + width / 2,
                cposy + 1,
                cposz + width - 2,
                Direction.NORTH,
                loot);
    }

    // ——— entity resolvers (gold spawner name → ModEntities) ———

    private static EntityType<?> terribleTerror() {
        return ModEntities.TERRIBLE_TERROR != null
                ? ModEntities.TERRIBLE_TERROR.get()
                : EntityType.BLAZE;
    }

    private static EntityType<?> emperorScorpion() {
        return ModEntities.EMPEROR_SCORPION != null
                ? ModEntities.EMPEROR_SCORPION.get()
                : EntityType.CAVE_SPIDER;
    }

    private static EntityType<?> cloudShark() {
        return ModEntities.CLOUD_SHARK != null
                ? ModEntities.CLOUD_SHARK.get()
                : EntityType.PHANTOM;
    }

    private static EntityType<?> lurkingTerror() {
        return ModEntities.LURKING_TERROR != null
                ? ModEntities.LURKING_TERROR.get()
                : EntityType.PHANTOM;
    }

    private static EntityType<?> rotator() {
        return ModEntities.ROTATOR != null ? ModEntities.ROTATOR.get() : EntityType.SHULKER;
    }

    private static EntityType<?> bee() {
        return ModEntities.BEE != null ? ModEntities.BEE.get() : EntityType.BEE;
    }

    private static EntityType<?> mantis() {
        return ModEntities.MANTIS != null ? ModEntities.MANTIS.get() : EntityType.SPIDER;
    }

    private static EntityType<?> mothra() {
        return ModEntities.MOTHRA != null ? ModEntities.MOTHRA.get() : EntityType.PHANTOM;
    }

    private static EntityType<?> largeWorm() {
        return ModEntities.LARGE_WORM != null
                ? ModEntities.LARGE_WORM.get()
                : EntityType.SILVERFISH;
    }

    private static EntityType<?> nightmare() {
        // Gold "Nightmare" → PitchBlack
        return ModEntities.PITCH_BLACK != null
                ? ModEntities.PITCH_BLACK.get()
                : EntityType.WITHER_SKELETON;
    }

    private static EntityType<?> alosaurus() {
        return ModEntities.ALOSAURUS != null
                ? ModEntities.ALOSAURUS.get()
                : EntityType.ZOMBIE;
    }

    private static EntityType<?> trex() {
        return ModEntities.TREX != null ? ModEntities.TREX.get() : EntityType.RAVAGER;
    }

    private static EntityType<?> basilisk() {
        return ModEntities.BASILISK != null ? ModEntities.BASILISK.get() : EntityType.RAVAGER;
    }

    private static EntityType<?> hercules() {
        return ModEntities.HERCULES_BEETLE != null
                ? ModEntities.HERCULES_BEETLE.get()
                : EntityType.RAVAGER;
    }

    private static EntityType<?> jumpyBug() {
        // Gold "Jumpy Bug" → TrooperBug
        return ModEntities.TROOPER_BUG != null
                ? ModEntities.TROOPER_BUG.get()
                : EntityType.SPIDER;
    }

    private static EntityType<?> hammerhead() {
        return ModEntities.HAMMERHEAD != null
                ? ModEntities.HAMMERHEAD.get()
                : EntityType.DOLPHIN;
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
            WorldGenLevel level,
            RandomSource random,
            int x,
            int y,
            int z,
            Direction facing,
            ResourceKey<LootTable> loot) {
        BlockPos pos = new BlockPos(x, y, z);
        StructureHelper.set(
                level, pos, Blocks.CHEST.defaultBlockState().setValue(ChestBlock.FACING, facing));
        RandomizableContainer.setBlockEntityLootTable(level, random, pos, loot);
    }
}
