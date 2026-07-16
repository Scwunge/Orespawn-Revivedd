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
 * Gold {@code GenericDungeon.makeStinkyHouse}: oak fence yard with cobweb clutter,
 * ruined plank house, Stink Bug + Stinky spawners, center chest.
 * <p>
 * Gold {@code DungeonSpawnerBlock} type <b>39</b>. Origin is house min-corner (pad Y);
 * yard extends to {@code x-5.x+yardlength}, {@code z-4.z+yardwidth}.
 */
public final class StinkyHouse {
    /** Gold house {@code width = 9} (Z extent of house). */
    public static final int HOUSE_WIDTH = 9;

    /** Gold house {@code length = 12} (X extent of house). */
    public static final int HOUSE_LENGTH = 12;

    /** Gold {@code yardwidth = 16}. */
    public static final int YARD_WIDTH = 16;

    /** Gold {@code yardlength = 24}. */
    public static final int YARD_LENGTH = 24;

    /** Yard X offset: gold places yard at {@code cposx + i - 5}. */
    public static final int YARD_OFF_X = 5;

    /** Yard Z offset: gold places yard at {@code cposz + k - 4}. */
    public static final int YARD_OFF_Z = 4;

    /**
     * Primary footprint for centering / docs: yard X span ({@code yardlength + 1}).
     */
    public static final int WIDTH = YARD_LENGTH + 1;

    /** Gold house wall height {@code height = 2} plus floor at y+1 → content y+1.y+3. */
    public static final int HOUSE_HEIGHT = 2;

    /** Total Y from origin: house j 0.height at y+1 → max y+3. */
    public static final int HEIGHT = HOUSE_HEIGHT + 2;

    public static final ResourceKey<LootTable> LOOT_TABLE = ResourceKey.create(
            Registries.LOOT_TABLE,
            ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "chests/stinky_house"));

    private StinkyHouse() {}

    /**
     * Places the stinky house with house min-corner origin at {@code (cposx, cposy, cposz)}.
     *
     * @return true if written
     */
    public static boolean makeStinkyHouse(
            WorldGenLevel level, RandomSource random, int cposx, int cposy, int cposz) {
        int minY = level.getMinBuildHeight();
        int maxY = level.getMaxBuildHeight();
        if (cposy + 1 < minY || cposy + HEIGHT >= maxY) {
            return false;
        }

        int yardMinX = cposx - YARD_OFF_X;
        int yardMinZ = cposz - YARD_OFF_Z;
        int yardSpanX = YARD_LENGTH + 1;
        int yardSpanZ = YARD_WIDTH + 1;

        // Clear yard + house volume (y+1 . y+HEIGHT)
        StructureHelper.clearBox(level, yardMinX, cposy + 1, yardMinZ, yardSpanX, HEIGHT, yardSpanZ);

        BlockState fence = Blocks.OAK_FENCE.defaultBlockState();
        BlockState air = Blocks.AIR.defaultBlockState();
        BlockState web = Blocks.COBWEB.defaultBlockState();
        BlockState planks = Blocks.OAK_PLANKS.defaultBlockState();
        BlockState pane = Blocks.GLASS_PANE.defaultBlockState();

        // Gold yard fence / cobweb at y+1
        for (int i = 0; i <= YARD_LENGTH; i++) {
            for (int k = 0; k <= YARD_WIDTH; k++) {
                BlockState bid = air;
                if (i == 0 || i == YARD_LENGTH || k == 0 || k == YARD_WIDTH) {
                    bid = fence;
                }
                if (bid.getBlock() == Blocks.OAK_FENCE && random.nextInt(3) == 1) {
                    bid = air;
                }
                if (bid.getBlock() == Blocks.AIR && random.nextInt(10) == 1) {
                    bid = web;
                }
                if (bid.getBlock() != Blocks.AIR) {
                    StructureHelper.set(level, cposx + i - YARD_OFF_X, cposy + 1, cposz + k - YARD_OFF_Z, bid);
                }
            }
        }

        // Gold ruined house at y+1.y+1+height
        for (int i = 0; i <= HOUSE_LENGTH; i++) {
            for (int k = 0; k <= HOUSE_WIDTH; k++) {
                for (int j = 0; j <= HOUSE_HEIGHT; j++) {
                    BlockState bid = air;
                    if (i == 0 || i == HOUSE_LENGTH || k == 0 || k == HOUSE_WIDTH) {
                        bid = planks;
                    }
                    // Windows on mid wall ring
                    if (bid.getBlock() == Blocks.OAK_PLANKS
                            && j == 1
                            && (i == 1 || i == HOUSE_LENGTH - 1 || k == 1 || k == HOUSE_WIDTH - 1)) {
                        bid = pane;
                    }
                    if (j == HOUSE_HEIGHT) {
                        bid = planks;
                    }
                    // Gold random holes
                    if (random.nextInt(10) == 1) {
                        bid = air;
                    }
                    // Front doorway (j 0.1, i=0, k near width/2)
                    if ((j == 0 || j == 1)
                            && i == 0
                            && (k == HOUSE_WIDTH / 2 || k == HOUSE_WIDTH / 2 + 1)) {
                        bid = air;
                    }
                    StructureHelper.set(level, cposx + i, cposy + j + 1, cposz + k, bid);
                }
            }
        }

        // Gold Stink Bug + Stinky spawners
        placeSpawner(level, random, cposx + 2, cposy + 1, cposz + 2, stinkBug());
        placeSpawner(
                level,
                random,
                cposx + HOUSE_LENGTH - 2,
                cposy + 1,
                cposz + HOUSE_WIDTH - 2,
                stinky());

        // Gold center chest
        placeChest(
                level,
                random,
                cposx + HOUSE_LENGTH / 2,
                cposy + 1,
                cposz + HOUSE_WIDTH / 2,
                Direction.SOUTH);

        return true;
    }

    private static EntityType<?> stinkBug() {
        return ModEntities.STINKBUG != null ? ModEntities.STINKBUG.get() : EntityType.SILVERFISH;
    }

    private static EntityType<?> stinky() {
        return ModEntities.STINKY != null ? ModEntities.STINKY.get() : EntityType.WOLF;
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
