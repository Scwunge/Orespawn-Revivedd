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
 * Gold {@code GenericDungeon.makeRubberDuckyPond}: small tower of Rubber Ducky spawners
 * + dual chests + glass + water column, above a 12×11 sand-bordered water pond centered
 * on the origin with air cleared above the water surface.
 * <p>
 * Gold {@code DungeonSpawnerBlock} type <b>40</b>. Origin is pond center / tower base.
 */
public final class RubberDuckyPond {
    /** Gold pond loop {@code i = 0.11} → 12 wide (centered with −5). */
    public static final int POND_WIDTH = 12;

    /** Gold pond loop {@code k = 0.10} → 11 deep (centered with −5). */
    public static final int POND_DEPTH = 11;

    /** Half-offsets gold used: {@code cposx + i - 5}, {@code cposz + k - 5}. */
    public static final int POND_OFF = 5;

    /**
     * Alias for bounding / centering docs (max horizontal extent of pond).
     */
    public static final int WIDTH = POND_WIDTH;

    /** Top of duck tower relative to origin: gold spawners at {@code y+6}. */
    public static final int HEIGHT = 7;

    public static final ResourceKey<LootTable> LOOT_TABLE = ResourceKey.create(
            Registries.LOOT_TABLE,
            ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "chests/rubber_ducky_pond"));

    private RubberDuckyPond() {}

    /**
     * Places a rubber-ducky pond centered on {@code (cposx, cposy, cposz)}.
     *
     * @return true if written
     */
    public static boolean makeRubberDuckyPond(
            WorldGenLevel level, RandomSource random, int cposx, int cposy, int cposz) {
        int minY = level.getMinBuildHeight();
        int maxY = level.getMaxBuildHeight();
        if (cposy < minY || cposy + HEIGHT >= maxY) {
            return false;
        }

        // Clear pond AABB + tower column so stone never fills the water / air
        StructureHelper.clearBox(
                level,
                cposx - POND_OFF,
                cposy,
                cposz - POND_OFF,
                POND_WIDTH,
                HEIGHT,
                POND_DEPTH);

        BlockState water = Blocks.WATER.defaultBlockState();
        BlockState sand = Blocks.SAND.defaultBlockState();
        BlockState glass = Blocks.GLASS.defaultBlockState();
        BlockState air = Blocks.AIR.defaultBlockState();

        // Gold pond: 12×11 water with sand border at y; air at y+1 and y+2
        for (int i = 0; i < POND_WIDTH; i++) {
            for (int k = 0; k < POND_DEPTH; k++) {
                boolean border = i == 0 || k == 0 || i == POND_WIDTH - 1 || k == POND_DEPTH - 1;
                StructureHelper.set(
                        level,
                        cposx + i - POND_OFF,
                        cposy,
                        cposz + k - POND_OFF,
                        border ? sand : water);
                StructureHelper.set(level, cposx + i - POND_OFF, cposy + 1, cposz + k - POND_OFF, air);
                StructureHelper.set(level, cposx + i - POND_OFF, cposy + 2, cposz + k - POND_OFF, air);
            }
        }

        // Gold tower water at y+3 (two sources + sides)
        StructureHelper.set(level, cposx, cposy + 3, cposz, water);
        StructureHelper.set(level, cposx + 1, cposy + 3, cposz, water);
        StructureHelper.set(level, cposx - 1, cposy + 3, cposz, water);
        StructureHelper.set(level, cposx + 2, cposy + 3, cposz, water);

        // Gold glass at y+4
        StructureHelper.set(level, cposx, cposy + 4, cposz, glass);
        StructureHelper.set(level, cposx + 1, cposy + 4, cposz, glass);

        // Gold dual chests at y+5
        placeChest(level, random, cposx, cposy + 5, cposz, Direction.NORTH);
        placeChest(level, random, cposx + 1, cposy + 5, cposz, Direction.NORTH);

        // Gold Rubber Ducky spawners at y+6 (two blocks)
        if (ModEntities.RUBBER_DUCKY != null) {
            placeSpawner(level, random, cposx, cposy + 6, cposz, ModEntities.RUBBER_DUCKY.get());
            placeSpawner(level, random, cposx + 1, cposy + 6, cposz, ModEntities.RUBBER_DUCKY.get());
        } else {
            // Fallback if entity ever absent
            placeSpawner(level, random, cposx, cposy + 6, cposz, EntityType.CHICKEN);
            placeSpawner(level, random, cposx + 1, cposy + 6, cposz, EntityType.CHICKEN);
        }

        return true;
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
            WorldGenLevel level, RandomSource random, int x, int y, int z, Direction facing) {
        BlockPos pos = new BlockPos(x, y, z);
        BlockState chest = Blocks.CHEST.defaultBlockState().setValue(ChestBlock.FACING, facing);
        StructureHelper.set(level, pos, chest);
        RandomizableContainer.setBlockEntityLootTable(level, random, pos, LOOT_TABLE);
    }
}
