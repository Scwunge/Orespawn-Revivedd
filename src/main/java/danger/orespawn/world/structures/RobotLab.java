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
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.storage.loot.LootTable;

/**
 * Gold {@code GenericDungeon.makeRobotLab} (+ private helpers makerobomain / makerobopillar /
 * makerobotower / makeroboaltar / makeroborailway / makeroboassemblyline / makerobotreasureroom):
 * quartz entry hall (10×20×5) into 30×30 main lab with tower, altar, railway, assembly line,
 * and treasure room. Spawners: Robo-Sniper (ROBOT5), Robo-Pounder (ROBOT2), Robo-Warrior (ROBOT4).
 * <p>
 * Gold {@code DungeonSpawnerBlock} type <b>30</b>. Origin is SW corner of the entry hall floor.
 */
public final class RobotLab {
    /** Gold entry hall width. */
    public static final int WIDTH = 10;

    /** Gold entry hall length (Z). */
    public static final int LENGTH = 20;

    /** Gold entry hall height (j = 0.height inclusive → 6 layers). */
    public static final int HEIGHT = 5;

    /** Gold makerobomain footprint. */
    public static final int MAIN_WIDTH = 30;

    public static final int MAIN_LENGTH = 30;

    public static final int MAIN_HEIGHT = 9;

    /**
     * Main hall X offset from entry origin ({@code cposx -= 10} in gold makerobomain).
     */
    public static final int MAIN_X_OFFSET = -10;

    /**
     * Main hall Z attach: gold {@code makerobomain(., cposz + length - 1)}.
     */
    public static final int MAIN_Z_ATTACH = LENGTH - 1;

    /** Tower base size (gold 12×12). */
    public static final int TOWER_SIZE = 12;

    /** Tower shaft top relative to tower base Y (gold j &lt; 35). */
    public static final int TOWER_TOP = 35;

    public static final ResourceKey<LootTable> LOOT_TABLE = ResourceKey.create(
            Registries.LOOT_TABLE,
            ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "chests/robot_lab"));

    private RobotLab() {}

    /**
     * Places the robot lab with entry-hall SW corner at {@code (cposx, cposy, cposz)}.
     *
     * @return true if written
     */
    public static boolean makeRobotLab(
            WorldGenLevel level, RandomSource random, int cposx, int cposy, int cposz) {
        int minY = level.getMinBuildHeight();
        int maxY = level.getMaxBuildHeight();
        // Entry + main roof + tower shaft
        if (cposy < minY || cposy + MAIN_HEIGHT + TOWER_TOP >= maxY) {
            return false;
        }

        // Clear full lab volume (entry, main, tower, exterior torches at z-1)
        int clearX0 = cposx + MAIN_X_OFFSET - 1;
        int clearZ0 = cposz - 1;
        int clearW = MAIN_WIDTH + 2;
        int clearD = MAIN_Z_ATTACH + MAIN_LENGTH + 2;
        int clearH = MAIN_HEIGHT + TOWER_TOP + 1;
        StructureHelper.clearBox(level, clearX0, cposy, clearZ0, clearW, clearH, clearD);

        BlockState air = Blocks.AIR.defaultBlockState();
        BlockState quartz = Blocks.QUARTZ_BLOCK.defaultBlockState();
        BlockState gold = Blocks.GOLD_BLOCK.defaultBlockState();

        // Gold entry hall: j=0.height, i=0.width-1, k=0.length-1
        for (int j = 0; j <= HEIGHT; j++) {
            for (int i = 0; i < WIDTH; i++) {
                for (int k = 0; k < LENGTH; k++) {
                    BlockState bid = air;
                    if (i == 0 || k == 0 || i == WIDTH - 1 || k == LENGTH - 1) {
                        bid = quartz;
                    }
                    if (j == 0) {
                        bid = quartz;
                        if (i == WIDTH / 2 || i == WIDTH / 2 - 1) {
                            bid = gold;
                        }
                    }
                    if (j == HEIGHT) {
                        bid = quartz;
                        if (i == 0 || k == 0 || i == WIDTH - 1 || k == LENGTH - 1) {
                            bid = air;
                        }
                    }
                    StructureHelper.set(level, cposx + i, cposy + j, cposz + k, bid);
                }
            }
        }

        // Door openings (gold clears air then places iron doors, facing meta 3 = north)
        StructureHelper.set(level, cposx + WIDTH / 2, cposy + 1, cposz, air);
        StructureHelper.set(level, cposx + WIDTH / 2, cposy + 2, cposz, air);
        StructureHelper.set(level, cposx + WIDTH / 2 - 1, cposy + 1, cposz, air);
        StructureHelper.set(level, cposx + WIDTH / 2 - 1, cposy + 2, cposz, air);
        placeIronDoor(level, cposx + WIDTH / 2, cposy + 1, cposz, Direction.NORTH);
        placeIronDoor(level, cposx + WIDTH / 2 - 1, cposy + 1, cposz, Direction.NORTH);

        // Exterior wall torches (gold torch meta 4 = facing north)
        StructureHelper.set(
                level,
                cposx + WIDTH / 2 - 2,
                cposy + 2,
                cposz - 1,
                Blocks.WALL_TORCH.defaultBlockState()
                        .setValue(net.minecraft.world.level.block.WallTorchBlock.FACING, Direction.NORTH));
        StructureHelper.set(
                level,
                cposx + WIDTH / 2 + 1,
                cposy + 2,
                cposz - 1,
                Blocks.WALL_TORCH.defaultBlockState()
                        .setValue(net.minecraft.world.level.block.WallTorchBlock.FACING, Direction.NORTH));

        makeRoboMain(level, random, cposx, cposy, cposz + MAIN_Z_ATTACH);

        makeRoboPillar(level, random, cposx, cposy, cposz + LENGTH / 3, 0);
        makeRoboPillar(level, random, cposx, cposy, cposz + LENGTH * 2 / 3, 0);
        makeRoboPillar(level, random, cposx, cposy, cposz + (LENGTH - 1), 0);
        makeRoboPillar(level, random, cposx + WIDTH - 1, cposy, cposz + LENGTH / 3, 1);
        makeRoboPillar(level, random, cposx + WIDTH - 1, cposy, cposz + LENGTH * 2 / 3, 1);
        makeRoboPillar(level, random, cposx + WIDTH - 1, cposy, cposz + (LENGTH - 1), 1);

        return true;
    }

    /** Gold {@code makerobopillar}: 3×3×5 quartz column with redstone accents + Robo-Sniper. */
    private static void makeRoboPillar(
            WorldGenLevel level, RandomSource random, int cposx, int cposy, int cposz, int dir) {
        BlockState quartz = Blocks.QUARTZ_BLOCK.defaultBlockState();
        BlockState redstone = Blocks.REDSTONE_BLOCK.defaultBlockState();

        for (int j = 0; j < 5; j++) {
            for (int i = -1; i < 2; i++) {
                for (int k = -1; k < 2; k++) {
                    BlockState bid = quartz;
                    if (j == 2 || j == 3) {
                        if (k == 0 && (i == -1 || i == 1)) {
                            bid = redstone;
                        }
                        if (i == 0 && (k == -1 || k == 1)) {
                            bid = redstone;
                        }
                    }
                    StructureHelper.set(level, cposx + i, cposy + j, cposz + k, bid);
                }
            }
        }

        if (dir == 0) {
            placeSpawner(level, random, cposx + 1, cposy + 1, cposz, robotSniper());
        }
        if (dir == 1) {
            placeSpawner(level, random, cposx - 1, cposy + 1, cposz, robotSniper());
        }
    }

    /** Gold {@code makerobomain}: 30×30×9 hall + sub-features. Shifts origin X by −10. */
    private static void makeRoboMain(
            WorldGenLevel level, RandomSource random, int cposx, int cposy, int cposz) {
        BlockState air = Blocks.AIR.defaultBlockState();
        BlockState quartz = Blocks.QUARTZ_BLOCK.defaultBlockState();
        BlockState gold = Blocks.GOLD_BLOCK.defaultBlockState();

        cposx += MAIN_X_OFFSET;

        for (int j = 0; j <= MAIN_HEIGHT; j++) {
            for (int i = 0; i < MAIN_WIDTH; i++) {
                for (int k = 0; k < MAIN_LENGTH; k++) {
                    BlockState bid = air;
                    if (i == 0 || k == 0 || i == MAIN_WIDTH - 1 || k == MAIN_LENGTH - 1) {
                        bid = quartz;
                    }
                    if (j == 0) {
                        bid = quartz;
                        if (i == MAIN_WIDTH / 2 || i == MAIN_WIDTH / 2 - 1) {
                            bid = gold;
                        }
                    }
                    if (j == MAIN_HEIGHT) {
                        bid = quartz;
                        if (i == 0 || k == 0 || i == MAIN_WIDTH - 1 || k == MAIN_LENGTH - 1) {
                            bid = air;
                        }
                    }
                    // Gold entry aperture from corridor into main (k==0, i in middle third)
                    if ((j == 1 || j == 2 || j == 3)
                            && k == 0
                            && i >= MAIN_WIDTH / 3
                            && i < MAIN_WIDTH * 2 / 3) {
                        bid = air;
                    }
                    StructureHelper.set(level, cposx + i, cposy + j, cposz + k, bid);
                }
            }
        }

        makeRoboAltar(level, random, cposx + MAIN_WIDTH / 2 - 4, cposy, cposz + 6);
        makeRoboRailway(level, cposx + 3, cposy, cposz + 10);
        makeRoboAssemblyLine(level, cposx + MAIN_WIDTH - 4, cposy, cposz + 4);
        makeRoboTreasureRoom(level, random, cposx + 9, cposy, cposz + 18);
        makeRoboTower(
                level,
                random,
                cposx + MAIN_WIDTH / 2 - 6,
                cposy + MAIN_HEIGHT,
                cposz + MAIN_LENGTH / 2 - 6);
    }

    /** Gold {@code makerobotower}: 12×12 platform + 4 pillars + stepped quartz shaft. */
    private static void makeRoboTower(
            WorldGenLevel level, RandomSource random, int cposx, int cposy, int cposz) {
        BlockState air = Blocks.AIR.defaultBlockState();
        BlockState quartz = Blocks.QUARTZ_BLOCK.defaultBlockState();
        BlockState bars = Blocks.IRON_BARS.defaultBlockState();
        BlockState redstone = Blocks.REDSTONE_BLOCK.defaultBlockState();

        for (int j = 0; j < 2; j++) {
            for (int i = 0; i < TOWER_SIZE; i++) {
                for (int k = 0; k < TOWER_SIZE; k++) {
                    BlockState bid = air;
                    if (j == 1) {
                        if (i == 0 || k == 0 || i == 11 || k == 11) {
                            bid = bars;
                        }
                        if (i == 0 && (k == 0 || k == 11)) {
                            bid = redstone;
                        }
                        if (i == 11 && (k == 0 || k == 11)) {
                            bid = redstone;
                        }
                    }
                    if (j == 0) {
                        bid = quartz;
                    }
                    StructureHelper.set(level, cposx + i, cposy + j, cposz + k, bid);
                }
            }
        }

        makeRoboPillar(level, random, cposx + 4, cposy + 1, cposz + 4, 1);
        makeRoboPillar(level, random, cposx + 7, cposy + 1, cposz + 7, 0);
        makeRoboPillar(level, random, cposx + 4, cposy + 1, cposz + 7, 1);
        makeRoboPillar(level, random, cposx + 7, cposy + 1, cposz + 4, 0);

        // Gold shaft j=5.34 (finite)
        for (int j = 5; j < TOWER_TOP; j++) {
            for (int i = 0; i < 2; i++) {
                for (int k = 0; k < 3; k++) {
                    BlockState bid = air;
                    if (j < 15) {
                        bid = quartz;
                    } else if (j < 25) {
                        bid = quartz;
                        if (k == 2) {
                            bid = bars;
                        }
                    } else {
                        bid = quartz;
                        if (k == 1) {
                            bid = bars;
                        }
                        if (k == 2) {
                            bid = air;
                        }
                    }
                    StructureHelper.set(level, cposx + i + 5, cposy + j, cposz + k + 5, bid);
                }
            }
        }
    }

    /** Gold {@code makeroboaltar}: gold pad + quartz step + 2 Robo-Pounder spawners. */
    private static void makeRoboAltar(
            WorldGenLevel level, RandomSource random, int cposx, int cposy, int cposz) {
        BlockState gold = Blocks.GOLD_BLOCK.defaultBlockState();
        BlockState quartz = Blocks.QUARTZ_BLOCK.defaultBlockState();
        BlockState redstone = Blocks.REDSTONE_BLOCK.defaultBlockState();
        BlockState torch = Blocks.TORCH.defaultBlockState();

        for (int i = 0; i < 8; i++) {
            for (int k = 0; k < 8; k++) {
                StructureHelper.set(level, cposx + i, cposy, cposz + k, gold);
            }
        }
        for (int i = 0; i < 6; i++) {
            for (int k = 0; k < 6; k++) {
                StructureHelper.set(level, cposx + i + 1, cposy + 1, cposz + k + 1, quartz);
            }
        }

        StructureHelper.set(level, cposx + 2, cposy + 1, cposz + 2, redstone);
        StructureHelper.set(level, cposx + 2, cposy + 2, cposz + 2, torch);
        StructureHelper.set(level, cposx + 5, cposy + 1, cposz + 5, redstone);
        StructureHelper.set(level, cposx + 5, cposy + 2, cposz + 5, torch);
        StructureHelper.set(level, cposx + 5, cposy + 1, cposz + 2, redstone);
        StructureHelper.set(level, cposx + 5, cposy + 2, cposz + 2, torch);
        StructureHelper.set(level, cposx + 2, cposy + 1, cposz + 5, redstone);
        StructureHelper.set(level, cposx + 2, cposy + 2, cposz + 5, torch);

        placeSpawner(level, random, cposx + 3, cposy + 2, cposz + 3, robotPounder());
        placeSpawner(level, random, cposx + 4, cposy + 2, cposz + 4, robotPounder());
    }

    /** Gold {@code makeroborailway}: dual rail lines with powered segments. */
    private static void makeRoboRailway(WorldGenLevel level, int cposx, int cposy, int cposz) {
        BlockState rail = Blocks.RAIL.defaultBlockState();
        BlockState powered = Blocks.POWERED_RAIL.defaultBlockState();
        BlockState redTorch = Blocks.REDSTONE_TORCH.defaultBlockState();

        // Gold layout (z 0.12) — rails on x+0 and x+3, powered crossings + redstone torches
        int[] railZ = {0, 1, 3, 4, 5, 7, 8, 9, 11, 12};
        for (int z : railZ) {
            StructureHelper.set(level, cposx + 0, cposy + 1, cposz + z, rail);
            StructureHelper.set(level, cposx + 3, cposy + 1, cposz + z, rail);
        }
        int[] poweredZ = {2, 6, 10};
        for (int z : poweredZ) {
            StructureHelper.set(level, cposx + 0, cposy + 1, cposz + z, powered);
            StructureHelper.set(level, cposx + 3, cposy + 1, cposz + z, powered);
            StructureHelper.set(level, cposx + 1, cposy + 1, cposz + z, redTorch);
            StructureHelper.set(level, cposx + 2, cposy + 1, cposz + z, redTorch);
        }
    }

    /** Gold {@code makeroboassemblyline}: quartz counters, stairs, dispensers, white carpet. */
    private static void makeRoboAssemblyLine(WorldGenLevel level, int cposx, int cposy, int cposz) {
        BlockState quartz = Blocks.QUARTZ_BLOCK.defaultBlockState();
        BlockState stairs = Blocks.QUARTZ_STAIRS
                .defaultBlockState()
                .setValue(StairBlock.FACING, Direction.WEST);
        BlockState dispenser = Blocks.DISPENSER
                .defaultBlockState()
                .setValue(net.minecraft.world.level.block.DispenserBlock.FACING, Direction.SOUTH);
        BlockState carpet = Blocks.WHITE_CARPET.defaultBlockState();
        BlockState redTorch = Blocks.REDSTONE_TORCH.defaultBlockState();

        for (int k = 0; k < 24; k++) {
            if (k % 3 == 1) {
                StructureHelper.set(level, cposx - 2, cposy + 1, cposz + k, stairs);
                StructureHelper.set(level, cposx, cposy + 2, cposz + k, dispenser);
                StructureHelper.set(level, cposx, cposy + 3, cposz + k, carpet);
            }
            if (k % 3 == 0) {
                // Gold redstone torch meta 13 → standing redstone torch
                StructureHelper.set(level, cposx, cposy + 2, cposz + k, redTorch);
            }
            StructureHelper.set(level, cposx, cposy + 1, cposz + k, quartz);
            StructureHelper.set(level, cposx + 1, cposy + 1, cposz + k, quartz);
        }
    }

    /** Gold {@code makerobotreasureroom}: walled room, Robo-Warrior + dual chests. */
    private static void makeRoboTreasureRoom(
            WorldGenLevel level, RandomSource random, int cposx, int cposy, int cposz) {
        BlockState air = Blocks.AIR.defaultBlockState();
        BlockState quartz = Blocks.QUARTZ_BLOCK.defaultBlockState();
        BlockState bars = Blocks.IRON_BARS.defaultBlockState();

        for (int j = 1; j < 7; j++) {
            for (int i = 0; i < 12; i++) {
                for (int k = 0; k < 8; k++) {
                    BlockState bid = air;
                    if (i == 0 || k == 0 || i == 11 || k == 7) {
                        bid = quartz;
                    }
                    if (j == 2 && i == 11) {
                        bid = bars;
                    }
                    if (j == 3 && !bid.isAir()) {
                        bid = bars;
                    }
                    if ((j == 1 || j == 2 || j == 3) && k == 0 && (i == 1 || i == 2)) {
                        bid = air;
                    }
                    StructureHelper.set(level, cposx + i, cposy + j, cposz + k, bid);
                }
            }
        }

        placeSpawner(level, random, cposx + 10, cposy + 1, cposz + 1, robotWarrior());
        // Gold chest meta 2 → facing north
        placeChest(level, random, cposx + 8, cposy + 1, cposz + 1, Direction.NORTH);
        placeChest(level, random, cposx + 6, cposy + 1, cposz + 1, Direction.NORTH);
    }

    /** Gold "Robo-Sniper" → {@link ModEntities#ROBOT5}. */
    private static EntityType<?> robotSniper() {
        return ModEntities.ROBOT5 != null ? ModEntities.ROBOT5.get() : EntityType.SKELETON;
    }

    /** Gold "Robo-Pounder" → {@link ModEntities#ROBOT2}. */
    private static EntityType<?> robotPounder() {
        return ModEntities.ROBOT2 != null ? ModEntities.ROBOT2.get() : EntityType.IRON_GOLEM;
    }

    /** Gold "Robo-Warrior" → {@link ModEntities#ROBOT4}. */
    private static EntityType<?> robotWarrior() {
        return ModEntities.ROBOT4 != null ? ModEntities.ROBOT4.get() : EntityType.SKELETON;
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
