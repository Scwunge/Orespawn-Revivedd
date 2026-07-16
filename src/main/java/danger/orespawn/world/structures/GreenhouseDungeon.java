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
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.storage.loot.LootTable;

/**
 * Gold {@code GenericDungeon.makeGreenhouseDungeon}: glass greenhouse (23×15×7)
 * with grass/water floor, farmland + random crops/flowers (vanilla + OreSpawn plants),
 * stone-brick roof with glass skylines / glowstone accents, iron double door on −Z,
 * dual Triffid spawners and chest on the roof center.
 * <p>
 * Gold {@code DungeonSpawnerBlock} type <b>36</b>. Origin is min-corner (SW) of the shell.
 */
public final class GreenhouseDungeon {
    /** Gold {@code length = 23} (X extent of shell). Primary WIDTH for centering on X. */
    public static final int WIDTH = 23;

    /** Gold {@code length} alias. */
    public static final int LENGTH = WIDTH;

    /** Gold {@code width = 15} (Z extent of shell). */
    public static final int DEPTH = 15;

    /** Gold wall/roof height {@code height = 7}. */
    public static final int WALL_HEIGHT = 7;

    /** Gold clears air from {@code height . height+6}. */
    public static final int ROOF_CLEAR = 6;

    /** Total Y span from floor origin including roof clear + spawner stack. */
    public static final int HEIGHT = WALL_HEIGHT + ROOF_CLEAR + 1;

    public static final ResourceKey<LootTable> LOOT_TABLE = ResourceKey.create(
            Registries.LOOT_TABLE,
            ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "chests/greenhouse_dungeon"));

    private GreenhouseDungeon() {}

    /**
     * Places the greenhouse with min-corner origin at {@code (cposx, cposy, cposz)}.
     *
     * @return true if written
     */
    public static boolean makeGreenhouseDungeon(
            WorldGenLevel level, RandomSource random, int cposx, int cposy, int cposz) {
        int minY = level.getMinBuildHeight();
        int maxY = level.getMaxBuildHeight();
        if (cposy < minY || cposy + HEIGHT >= maxY) {
            return false;
        }

        // Clear shell + roof air pad
        StructureHelper.clearBox(level, cposx, cposy, cposz, LENGTH, HEIGHT, DEPTH);

        BlockState glass = Blocks.GLASS.defaultBlockState();
        BlockState stoneBrick = Blocks.STONE_BRICKS.defaultBlockState();
        BlockState glow = Blocks.GLOWSTONE.defaultBlockState();
        BlockState grass = Blocks.GRASS_BLOCK.defaultBlockState();
        BlockState water = Blocks.WATER.defaultBlockState();
        BlockState air = Blocks.AIR.defaultBlockState();
        BlockState farmland = Blocks.FARMLAND.defaultBlockState();
        BlockState stone = Blocks.STONE.defaultBlockState();
        BlockState torch = Blocks.TORCH.defaultBlockState();

        for (int i = 0; i < LENGTH; i++) {
            for (int k = 0; k < DEPTH; k++) {
                for (int j = 0; j < WALL_HEIGHT; j++) {
                    BlockState blk = air;
                    boolean edge = i == 0 || k == 0 || i == LENGTH - 1 || k == DEPTH - 1;

                    if (edge) {
                        blk = glass;
                    }

                    if (j == WALL_HEIGHT - 1) {
                        // Gold roof: stonebrick (field_150339_S → stone brick stand-in),
                        // glow every 4, glass bands on k%4==1
                        blk = stoneBrick;
                        if (i % 4 == 3 && k % 4 == 3) {
                            blk = glow;
                        }
                        if (k % 4 == 1) {
                            blk = glass;
                        }
                    }

                    if (j == 0) {
                        blk = grass;
                        if (!edge && i % 3 == 2) {
                            blk = water;
                        }
                    }

                    if (j == 1 && !edge && i % 3 != 2 && random.nextInt(3) != 1) {
                        // Gold: farmland under plant, plant on j=1
                        StructureHelper.set(level, cposx + i, cposy + j - 1, cposz + k, farmland);
                        blk = pickPlant(random);
                    }

                    StructureHelper.set(level, cposx + i, cposy + j, cposz + k, blk);
                }
            }
        }

        // Gold roof air pad already cleared; re-assert air above roof
        for (int i = 0; i < LENGTH; i++) {
            for (int k = 0; k < DEPTH; k++) {
                for (int j = WALL_HEIGHT; j <= WALL_HEIGHT + ROOF_CLEAR; j++) {
                    StructureHelper.set(level, cposx + i, cposy + j, cposz + k, air);
                }
            }
        }

        // Gold iron double door on −Z face near gold width/2 (15/2 → x+7 / x+6)
        int doorX0 = DEPTH / 2; // 7
        int doorX1 = DEPTH / 2 - 1; // 6
        StructureHelper.set(level, cposx + doorX0, cposy + 1, cposz, air);
        StructureHelper.set(level, cposx + doorX0, cposy + 2, cposz, air);
        StructureHelper.set(level, cposx + doorX1, cposy + 1, cposz, air);
        StructureHelper.set(level, cposx + doorX1, cposy + 2, cposz, air);
        placeIronDoor(level, cposx + doorX0, cposy + 1, cposz, Direction.SOUTH);
        placeIronDoor(level, cposx + doorX1, cposy + 1, cposz, Direction.SOUTH);
        StructureHelper.set(level, cposx + doorX0 + 1, cposy + 2, cposz, stone);
        StructureHelper.set(level, cposx + doorX1 - 1, cposy + 2, cposz, stone);
        // Gold torch meta 4 ≈ facing south of wall (on stone outside)
        StructureHelper.set(level, cposx + doorX1 - 1, cposy + 2, cposz - 1, torch);
        StructureHelper.set(level, cposx + doorX0 + 1, cposy + 2, cposz - 1, torch);

        // Gold roof center: chest at height, Triffid spawners at height+1/+2
        // gold: var18 = length/2, k = width/2
        int cx = cposx + LENGTH / 2;
        int cz = cposz + DEPTH / 2;
        placeChest(level, random, cx, cposy + WALL_HEIGHT, cz, Direction.SOUTH);
        placeSpawner(level, random, cx, cposy + WALL_HEIGHT + 1, cz, triffid());
        placeSpawner(level, random, cx, cposy + WALL_HEIGHT + 2, cz, triffid());

        return true;
    }

    private static BlockState pickPlant(RandomSource random) {
        int t = random.nextInt(20);
        return switch (t) {
            case 0 -> Blocks.DANDELION.defaultBlockState();
            case 1 -> Blocks.POPPY.defaultBlockState();
            case 2 -> Blocks.BROWN_MUSHROOM.defaultBlockState();
            case 3 -> Blocks.RED_MUSHROOM.defaultBlockState();
            case 4 -> Blocks.WHEAT.defaultBlockState();
            case 5 -> Blocks.CARROTS.defaultBlockState();
            case 6 -> Blocks.POTATOES.defaultBlockState();
            case 7 -> Blocks.SUGAR_CANE.defaultBlockState();
            case 9 -> ModBlocks.CORN_PLANT.get().defaultBlockState();
            case 10 -> ModBlocks.TOMATO_PLANT.get().defaultBlockState();
            case 11 -> ModBlocks.STRAWBERRY_PLANT.get().defaultBlockState();
            case 12 -> ModBlocks.BUTTERFLY_PLANT.get().defaultBlockState();
            case 13 -> ModBlocks.MOTH_PLANT.get().defaultBlockState();
            case 14 -> ModBlocks.RADISH_PLANT.get().defaultBlockState();
            case 15 -> ModBlocks.LETTUCE_PLANT.get().defaultBlockState();
            case 16 -> ModBlocks.FLOWER_PINK.get().defaultBlockState();
            case 17 -> ModBlocks.FLOWER_BLUE.get().defaultBlockState();
            case 18 -> ModBlocks.QUINOA_PLANT.get().defaultBlockState();
            case 19 -> ModBlocks.RICE_PLANT.get().defaultBlockState();
            default -> Blocks.AIR.defaultBlockState(); // t==8 gold left air
        };
    }

    private static EntityType<?> triffid() {
        return ModEntities.TRIFFID != null ? ModEntities.TRIFFID.get() : EntityType.ZOMBIE;
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
