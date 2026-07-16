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
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootTable;

/**
 * Gold {@code GenericDungeon.makeEnderCastle}: 22×22 bedrock shell on obsidian pad,
 * corner columns with ladders, mid mezzanine, lava roof pool with ender-chest spire,
 * wall spawn-egg stand-ins → spawners, CaveFisher / Ender Reaper / Ender Knight spawners,
 * dual side chests ({@code EnderCastleContentsList}).
 * <p>
 * Gold {@code DungeonSpawnerBlock} type <b>27</b>. Origin is min-corner of the main shell
 * (pad extends −3.width+3 around it).
 * <p>
 * Block map: {@code field_150343_Z} → obsidian, {@code field_150357_h} → bedrock,
 * {@code field_150411_aY} → iron bars, {@code MyEnderPearlBlock} → purpur,
 * {@code MyEyeOfEnderBlock} → end stone, gold spawn-egg blocks → spawners.
 */
public final class EnderCastle {
    /** Gold {@code width = 22} (main shell X/Z inclusive 0.width). */
    public static final int WIDTH = 22;

    /** Gold {@code height = 12} (main shell walls). */
    public static final int WALL_HEIGHT = 12;

    /** Column / pad overhang beyond shell edges. */
    public static final int PAD = 3;

    /**
     * Full horizontal clear extent including pad: {@code WIDTH + 1 + 2*PAD}.
     * (loops −PAD.WIDTH+PAD inclusive → size WIDTH + 2*PAD + 1)
     */
    public static final int FOOTPRINT = WIDTH + 2 * PAD + 1;

    /**
     * Peak content Y above origin: column tops at {@code WALL_HEIGHT + 3}, beacon at 15.
     */
    public static final int HEIGHT = WALL_HEIGHT + 4;

    public static final ResourceKey<LootTable> LOOT_TABLE = ResourceKey.create(
            Registries.LOOT_TABLE,
            ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "chests/ender_castle"));

    private EnderCastle() {}

    /**
     * Places the ender castle with min-corner origin at {@code (cposx, cposy, cposz)}.
     *
     * @return true if written
     */
    public static boolean makeEnderCastle(
            WorldGenLevel level, RandomSource random, int cposx, int cposy, int cposz) {
        int minY = level.getMinBuildHeight();
        int maxY = level.getMaxBuildHeight();
        if (cposy < minY || cposy + HEIGHT >= maxY) {
            return false;
        }

        // Clear pad + shell + column tops
        StructureHelper.clearBox(
                level,
                cposx - PAD,
                cposy,
                cposz - PAD,
                FOOTPRINT,
                HEIGHT + 1,
                FOOTPRINT);

        BlockState air = Blocks.AIR.defaultBlockState();
        // Gold field_150343_Z → obsidian (1.7.10 SRG)
        BlockState obsidian = Blocks.OBSIDIAN.defaultBlockState();
        BlockState bedrock = Blocks.BEDROCK.defaultBlockState();
        BlockState ironBars = Blocks.IRON_BARS.defaultBlockState();
        BlockState lava = Blocks.LAVA.defaultBlockState();
        BlockState torch = Blocks.TORCH.defaultBlockState();
        BlockState beacon = Blocks.BEACON.defaultBlockState();
        // Gold MyEnderPearlBlock / MyEyeOfEnderBlock
        BlockState enderPearl = ModBlocks.ENDER_PEARL_BLOCK.get().defaultBlockState();
        BlockState eyeOfEnder = ModBlocks.EYE_OF_ENDER_BLOCK.get().defaultBlockState();

        int width = WIDTH;
        int height = WALL_HEIGHT;

        // Gold: obsidian pad −3.width+3 at y+0, iron-bar rim at y+1
        for (int i = -PAD; i <= width + PAD; i++) {
            for (int k = -PAD; k <= width + PAD; k++) {
                for (int j = 0; j <= 1; j++) {
                    BlockState bid = air;
                    if (j == 0) {
                        bid = obsidian;
                    }
                    if (j == 1
                            && (i == -PAD
                                    || i == width + PAD
                                    || k == width + PAD
                                    || k == -PAD)) {
                        bid = ironBars;
                    }
                    StructureHelper.set(level, cposx + i, cposy + j, cposz + k, bid);
                }
            }
        }

        // Gold: main shell walls 0.width, y 1.height (bedrock), crenels + spawn stand-ins
        for (int i = 0; i <= width; i++) {
            for (int k = 0; k <= width; k++) {
                for (int j = 1; j <= height; j++) {
                    BlockState bid = air;
                    boolean edge = i == 0 || i == width || k == width || k == 0;
                    if (edge) {
                        bid = bedrock;
                    }
                    if (j == height && bid == bedrock && ((i + k) & 1) == 0) {
                        bid = air;
                    }
                    if (j == height - 2 && bid == bedrock && ((i + k) & 1) == 0) {
                        // Gold spawn-egg blocks → spawners with matching entities
                        placeWallSpawnStandIn(level, random, cposx + i, cposy + j, cposz + k);
                        continue;
                    }
                    if (j == 7 && bid == bedrock && ((i + k) & 1) != 0) {
                        bid = eyeOfEnder;
                    }
                    StructureHelper.set(level, cposx + i, cposy + j, cposz + k, bid);
                }
            }
        }

        // Gold: outer gallery ring −1.width+1 at y 1.height-1 (mezz / upper)
        for (int i = -1; i <= width + 1; i++) {
            for (int k = -1; k <= width + 1; k++) {
                for (int j = 1; j <= height - 1; j++) {
                    BlockState bid = air;
                    if (j == 6 || j > 8) {
                        if (i == -1 || i == width + 1 || k == width + 1 || k == -1) {
                            bid = bedrock;
                        }
                        if (j == 6 && bid != air && random.nextInt(2) == 1) {
                            StructureHelper.set(
                                    level, cposx + i, cposy + j - 1, cposz + k, enderPearl);
                            if (random.nextInt(3) == 1) {
                                StructureHelper.set(
                                        level, cposx + i, cposy + j - 2, cposz + k, enderPearl);
                            }
                        }
                    }
                    if (j == 7) {
                        if (i == -1 || i == width + 1 || k == width + 1 || k == -1) {
                            bid = bedrock;
                        }
                        if (bid == bedrock && ((i + k) & 1) == 0) {
                            bid = air;
                        }
                    }
                    if (bid != air) {
                        StructureHelper.set(level, cposx + i, cposy + j, cposz + k, bid);
                    }
                }
            }
        }

        // Gold corner columns (makeAColumn)
        makeAColumn(level, cposx - 2, cposy, cposz - 2, height + 1, 0);
        makeAColumn(level, cposx + width - 2, cposy, cposz - 2, height + 1, 1);
        makeAColumn(level, cposx - 2, cposy, cposz + width - 2, height + 1, 2);
        makeAColumn(level, cposx + width - 2, cposy, cposz + width - 2, height + 1, 3);

        // Gold mezzanine floor at y+8 (obsidian with bedrock diagonals/cross)
        int j = 8;
        for (int i = 1; i <= width - 1; i++) {
            for (int k = 1; k <= width - 1; k++) {
                BlockState bid = obsidian;
                if (i == width / 2 || k == width / 2 || i == k || i == width - k) {
                    bid = bedrock;
                }
                StructureHelper.set(level, cposx + i, cposy + j, cposz + k, bid);
            }
        }

        // Gold lava pool + rim on roof y+9
        int roof = 9;
        for (int i = -2; i <= 2; i++) {
            for (int k = -2; k <= 2; k++) {
                StructureHelper.set(
                        level, cposx + i + width / 2, cposy + roof, cposz + k + width / 2, lava);
            }
        }
        for (int m = -1; m <= 1; m++) {
            StructureHelper.set(
                    level, cposx + width / 2 + m, cposy + roof, cposz + width / 2 + 3, bedrock);
            StructureHelper.set(
                    level, cposx + width / 2 + m, cposy + roof, cposz + width / 2 - 3, bedrock);
            StructureHelper.set(
                    level, cposx + width / 2 + 3, cposy + roof, cposz + width / 2 + m, bedrock);
            StructureHelper.set(
                    level, cposx + width / 2 - 3, cposy + roof, cposz + width / 2 + m, bedrock);
        }
        StructureHelper.set(
                level, cposx + width / 2 - 2, cposy + roof, cposz + width / 2 - 2, bedrock);
        StructureHelper.set(
                level, cposx + width / 2 + 2, cposy + roof, cposz + width / 2 + 2, bedrock);
        StructureHelper.set(
                level, cposx + width / 2 - 2, cposy + roof, cposz + width / 2 + 2, bedrock);
        StructureHelper.set(
                level, cposx + width / 2 + 2, cposy + roof, cposz + width / 2 - 2, bedrock);
        StructureHelper.set(level, cposx + width / 2, cposy + roof, cposz + width / 2, bedrock);

        // Gold center spire: ender chest, obsidian, bedrock cross, torches, beacon
        StructureHelper.set(
                level,
                cposx + width / 2,
                cposy + roof + 1,
                cposz + width / 2,
                Blocks.ENDER_CHEST.defaultBlockState());
        StructureHelper.set(
                level, cposx + width / 2, cposy + roof + 2, cposz + width / 2, obsidian);
        StructureHelper.set(
                level, cposx + width / 2, cposy + roof + 3, cposz + width / 2, bedrock);
        StructureHelper.set(
                level, cposx + width / 2 - 1, cposy + roof + 3, cposz + width / 2, bedrock);
        StructureHelper.set(
                level, cposx + width / 2 + 1, cposy + roof + 3, cposz + width / 2, bedrock);
        StructureHelper.set(
                level, cposx + width / 2, cposy + roof + 3, cposz + width / 2 - 1, bedrock);
        StructureHelper.set(
                level, cposx + width / 2, cposy + roof + 3, cposz + width / 2 + 1, bedrock);
        StructureHelper.set(
                level, cposx + width / 2 - 1, cposy + roof + 4, cposz + width / 2, torch);
        StructureHelper.set(
                level, cposx + width / 2 + 1, cposy + roof + 4, cposz + width / 2, torch);
        StructureHelper.set(
                level, cposx + width / 2, cposy + roof + 4, cposz + width / 2 - 1, torch);
        StructureHelper.set(
                level, cposx + width / 2, cposy + roof + 4, cposz + width / 2 + 1, torch);
        StructureHelper.set(
                level, cposx + width / 2, cposy + roof + 4, cposz + width / 2, bedrock);
        StructureHelper.set(
                level, cposx + width / 2, cposy + roof + 5, cposz + width / 2, bedrock);
        StructureHelper.set(
                level, cposx + width / 2, cposy + roof + 6, cposz + width / 2, beacon);

        // Gold roof-corner Reaper/Knight stacks (±5 from center)
        placeReaperKnightStack(
                level, random, cposx + width / 2 + 5, cposy + roof, cposz + width / 2 + 5);
        placeReaperKnightStack(
                level, random, cposx + width / 2 - 5, cposy + roof, cposz + width / 2 + 5);
        placeReaperKnightStack(
                level, random, cposx + width / 2 + 5, cposy + roof, cposz + width / 2 - 5);
        placeReaperKnightStack(
                level, random, cposx + width / 2 - 5, cposy + roof, cposz + width / 2 - 5);

        // Gold mezzanine gallery floor at y+4 with iron-bar parapet
        int mez = 4;
        for (int i = 1; i <= width - 1; i++) {
            for (int k = 1; k <= width - 1; k++) {
                BlockState bid = air;
                if (i <= 5 || k <= 5 || i >= width - 5 || k >= width - 5) {
                    bid = bedrock;
                }
                if (bid != air) {
                    StructureHelper.set(level, cposx + i, cposy + mez, cposz + k, bid);
                }
                if (i == 5 && k >= 5 && k <= width - 5) {
                    placeBarsStack(level, cposx + i, cposy + mez, cposz + k, ironBars);
                }
                if (i == width - 5 && k >= 5 && k <= width - 5) {
                    placeBarsStack(level, cposx + i, cposy + mez, cposz + k, ironBars);
                }
                if (k == 5 && i >= 5 && i <= width - 5) {
                    placeBarsStack(level, cposx + i, cposy + mez, cposz + k, ironBars);
                }
                if (k == width - 5 && i >= 5 && i <= width - 5) {
                    placeBarsStack(level, cposx + i, cposy + mez, cposz + k, ironBars);
                }
            }
        }

        // Gold stairs up from floor toward +X mezz opening
        int midZ = width / 2;
        StructureHelper.set(level, cposx + width - 6, cposy + 3, cposz + midZ - 1, bedrock);
        StructureHelper.set(level, cposx + width - 6, cposy + 3, cposz + midZ, bedrock);
        StructureHelper.set(level, cposx + width - 6, cposy + 3, cposz + midZ + 1, bedrock);
        StructureHelper.set(level, cposx + width - 7, cposy + 2, cposz + midZ - 1, bedrock);
        StructureHelper.set(level, cposx + width - 7, cposy + 2, cposz + midZ, bedrock);
        StructureHelper.set(level, cposx + width - 7, cposy + 2, cposz + midZ + 1, bedrock);
        StructureHelper.set(level, cposx + width - 8, cposy + 1, cposz + midZ - 1, bedrock);
        StructureHelper.set(level, cposx + width - 8, cposy + 1, cposz + midZ, bedrock);
        StructureHelper.set(level, cposx + width - 8, cposy + 1, cposz + midZ + 1, bedrock);
        // Opening through parapet
        for (int dy = 1; dy <= 3; dy++) {
            for (int dz = -1; dz <= 1; dz++) {
                StructureHelper.set(
                        level, cposx + width - 5, cposy + mez + dy, cposz + midZ + dz, air);
            }
        }

        // Gold ground center Reaper/Knight
        placeSpawner(level, random, cposx + width / 2, cposy + 1, cposz + width / 2, enderReaper());
        placeSpawner(level, random, cposx + width / 2, cposy + 2, cposz + width / 2, enderKnight());

        // Gold CaveFisher + chests on mezzanine y+5 walls
        int chestY = 5;
        placeSpawner(level, random, cposx + 1, cposy + chestY, cposz + width / 2 - 1, caveFisher());
        placeSpawner(level, random, cposx + 1, cposy + chestY, cposz + width / 2 + 1, caveFisher());
        placeChest(level, random, cposx + 1, cposy + chestY, cposz + width / 2, Direction.NORTH);

        placeSpawner(level, random, cposx + width / 2 - 1, cposy + chestY, cposz + 1, caveFisher());
        placeSpawner(level, random, cposx + width / 2 + 1, cposy + chestY, cposz + 1, caveFisher());
        placeChest(level, random, cposx + width / 2, cposy + chestY, cposz + 1, Direction.SOUTH);

        placeSpawner(
                level, random, cposx + width / 2 - 1, cposy + chestY, cposz + width - 1, caveFisher());
        placeSpawner(
                level, random, cposx + width / 2 + 1, cposy + chestY, cposz + width - 1, caveFisher());
        placeChest(
                level, random, cposx + width / 2, cposy + chestY, cposz + width - 1, Direction.WEST);

        return true;
    }

    /** Gold {@code makeAColumn}: 5×5 obsidian tower with ladder spiral + door gaps. */
    private static void makeAColumn(
            WorldGenLevel level, int cposx, int cposy, int cposz, int height, int dir) {
        int width = 4;
        int halfwidth = 2;
        int step = dir;
        // Gold field_150343_Z → obsidian
        BlockState obsidian = Blocks.OBSIDIAN.defaultBlockState();
        BlockState air = Blocks.AIR.defaultBlockState();
        BlockState ironBars = Blocks.IRON_BARS.defaultBlockState();
        BlockState ladder = Blocks.LADDER
                .defaultBlockState()
                .setValue(LadderBlock.FACING, Direction.NORTH);

        // Cap roof at height+2 / crenel at height+3
        for (int i = -2; i <= width + 2; i++) {
            for (int k = -2; k <= width + 2; k++) {
                StructureHelper.set(level, cposx + i, cposy + height + 2, cposz + k, obsidian);
            }
        }
        for (int i = -2; i <= width + 2; i++) {
            for (int k = -2; k <= width + 2; k++) {
                BlockState bid = air;
                if (i == -2 || i == width + 2 || k == width + 2 || k == -2) {
                    bid = obsidian;
                }
                if (bid != air && ((i + k) & 1) == 0) {
                    bid = air;
                }
                StructureHelper.set(level, cposx + i, cposy + height + 3, cposz + k, bid);
            }
        }

        for (int i = 0; i <= width; i++) {
            for (int k = 0; k <= width; k++) {
                for (int j = 1; j <= height + 2; j++) {
                    BlockState bid = air;
                    if (i == 0 || i == width || k == width || k == 0) {
                        bid = obsidian;
                    }
                    if ((j % 3 == 0 || j % 3 == 1)
                            && j != height + 2
                            && bid == obsidian
                            && (i == halfwidth || k == halfwidth)) {
                        bid = ironBars;
                    }
                    StructureHelper.set(level, cposx + i, cposy + j, cposz + k, bid);
                }
            }
        }

        // Door gaps by corner dir (gold dirs 0.3)
        if (dir == 0) {
            clearDoor(level, cposx + width, cposy, cposz + width, width, air);
            clearDoorHigh(level, cposx + width, cposy, cposz + width, width, air);
        }
        if (dir == 1) {
            for (int j = 1; j <= 2; j++) {
                StructureHelper.set(level, cposx, cposy + j, cposz + width, air);
                StructureHelper.set(level, cposx + 1, cposy + j, cposz + width, air);
                StructureHelper.set(level, cposx, cposy + j, cposz + width - 1, air);
            }
            for (int j = 9; j <= 10; j++) {
                StructureHelper.set(level, cposx, cposy + j, cposz + width, air);
                StructureHelper.set(level, cposx + 1, cposy + j, cposz + width, air);
                StructureHelper.set(level, cposx, cposy + j, cposz + width - 1, air);
            }
            if (++step > 3) {
                step = 0;
            }
        }
        if (dir == 2) {
            for (int j = 1; j <= 2; j++) {
                StructureHelper.set(level, cposx + width, cposy + j, cposz, air);
                StructureHelper.set(level, cposx + width - 1, cposy + j, cposz, air);
                StructureHelper.set(level, cposx + width, cposy + j, cposz + 1, air);
            }
            for (int j = 9; j <= 10; j++) {
                StructureHelper.set(level, cposx + width, cposy + j, cposz, air);
                StructureHelper.set(level, cposx + width - 1, cposy + j, cposz, air);
                StructureHelper.set(level, cposx + width, cposy + j, cposz + 1, air);
            }
            if (++step > 3) {
                step = 0;
            }
            if (++step > 3) {
                step = 0;
            }
        }
        if (dir == 3) {
            for (int j = 1; j <= 2; j++) {
                StructureHelper.set(level, cposx, cposy + j, cposz, air);
                StructureHelper.set(level, cposx + 1, cposy + j, cposz, air);
                StructureHelper.set(level, cposx, cposy + j, cposz + 1, air);
            }
            for (int j = 9; j <= 10; j++) {
                StructureHelper.set(level, cposx, cposy + j, cposz, air);
                StructureHelper.set(level, cposx + 1, cposy + j, cposz, air);
                StructureHelper.set(level, cposx, cposy + j, cposz + 1, air);
            }
            if (++step > 3) {
                step = 0;
            }
            if (++step > 3) {
                step = 0;
            }
        }

        // Gold ladder spiral (step cycles corners of 1/3)
        for (int j = 1; j <= height + 2; j++) {
            int lx;
            int lz;
            if (step == 0) {
                lx = 1;
                lz = 1;
                ladder = Blocks.LADDER.defaultBlockState().setValue(LadderBlock.FACING, Direction.SOUTH);
            } else if (step == 1) {
                lx = 1;
                lz = 3;
                ladder = Blocks.LADDER.defaultBlockState().setValue(LadderBlock.FACING, Direction.EAST);
            } else if (step == 2) {
                lx = 3;
                lz = 3;
                ladder = Blocks.LADDER.defaultBlockState().setValue(LadderBlock.FACING, Direction.NORTH);
            } else {
                lx = 3;
                lz = 1;
                ladder = Blocks.LADDER.defaultBlockState().setValue(LadderBlock.FACING, Direction.WEST);
            }
            if (++step > 3) {
                step = 0;
            }
            StructureHelper.set(level, cposx + lx, cposy + j, cposz + lz, ladder);
        }
    }

    private static void clearDoor(
            WorldGenLevel level, int x, int y, int z, int width, BlockState air) {
        for (int j = 1; j <= 2; j++) {
            StructureHelper.set(level, x, y + j, z, air);
            StructureHelper.set(level, x - 1, y + j, z, air);
            StructureHelper.set(level, x, y + j, z - 1, air);
        }
    }

    private static void clearDoorHigh(
            WorldGenLevel level, int x, int y, int z, int width, BlockState air) {
        for (int j = 9; j <= 10; j++) {
            StructureHelper.set(level, x, y + j, z, air);
            StructureHelper.set(level, x - 1, y + j, z, air);
            StructureHelper.set(level, x, y + j, z - 1, air);
        }
    }

    private static void placeBarsStack(
            WorldGenLevel level, int x, int baseY, int z, BlockState bars) {
        StructureHelper.set(level, x, baseY + 1, z, bars);
        StructureHelper.set(level, x, baseY + 2, z, bars);
        StructureHelper.set(level, x, baseY + 3, z, bars);
    }

    private static void placeReaperKnightStack(
            WorldGenLevel level, RandomSource random, int x, int y, int z) {
        placeSpawner(level, random, x, y, z, enderReaper());
        placeSpawner(level, random, x, y + 1, z, enderKnight());
    }

    /**
     * Gold wall spawn-egg blocks (EnderKnight / EnderReaper / Enderman / EnderDragon).
     * Port: vanilla spawners with matching entity types.
     */
    private static void placeWallSpawnStandIn(
            WorldGenLevel level, RandomSource random, int x, int y, int z) {
        int which = random.nextInt(4);
        EntityType<?> mob = switch (which) {
            case 0 -> enderKnight();
            case 1 -> enderReaper();
            case 2 -> EntityType.ENDERMAN;
            default -> EntityType.ENDER_DRAGON;
        };
        placeSpawner(level, random, x, y, z, mob);
    }

    private static EntityType<?> enderKnight() {
        return ModEntities.ENDER_KNIGHT != null
                ? ModEntities.ENDER_KNIGHT.get()
                : EntityType.ENDERMAN;
    }

    private static EntityType<?> enderReaper() {
        return ModEntities.ENDER_REAPER != null
                ? ModEntities.ENDER_REAPER.get()
                : EntityType.ENDERMAN;
    }

    private static EntityType<?> caveFisher() {
        return ModEntities.CAVEFISHER != null
                ? ModEntities.CAVEFISHER.get()
                : EntityType.CAVE_SPIDER;
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
