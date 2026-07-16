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
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.storage.loot.LootTable;

/**
 * Gold {@code GenericDungeon.makeIgloo}: snow/ice circular shell (radius 6→1),
 * west oak door, Rat / Ghost / GhostSkelly spawners, one chest.
 * <p>
 * Gold {@code DungeonSpawnerBlock} type <b>20</b>.
 */
public final class Igloo {
    /** Outer ring radius (gold currad = 6). */
    public static final float RADIUS = 6.0F;
    /** Shell height above origin floor (gold y+1 . y+5). */
    public static final int HEIGHT = 6;

    public static final ResourceKey<LootTable> LOOT_TABLE = ResourceKey.create(
            Registries.LOOT_TABLE,
            ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "chests/igloo"));

    private Igloo() {}

    /**
     * Places an igloo centered at {@code (cposx, cposy, cposz)}.
     *
     * @return true if the structure was written
     */
    public static boolean makeIgloo(WorldGenLevel level, RandomSource random, int cposx, int cposy, int cposz) {
        int minY = level.getMinBuildHeight();
        int maxY = level.getMaxBuildHeight();
        if (cposy < minY || cposy + HEIGHT >= maxY) {
            return false;
        }

        BlockState snow = Blocks.SNOW_BLOCK.defaultBlockState();
        BlockState ice = Blocks.ICE.defaultBlockState();
        BlockState planks = Blocks.OAK_PLANKS.defaultBlockState();
        BlockState air = Blocks.AIR.defaultBlockState();

        // Clear full cylinder so interior is walkable underground / in terrain
        StructureHelper.clearCylinder(level, cposx, cposy, cposz, RADIUS + 0.5F, HEIGHT);

        // y+1,y+2,y+3: outer ring radius 6 — snow / ice / snow (gold)
        placeRing(level, cposx, cposy + 1, cposz, 6.0F, 5.0F, snow);
        placeRing(level, cposx, cposy + 2, cposz, 6.0F, 5.0F, ice);
        placeRing(level, cposx, cposy + 3, cposz, 6.0F, 5.0F, snow);

        // y+4: radius 5 ice
        placeRing(level, cposx, cposy + 4, cposz, 5.0F, 10.0F, ice);

        // y+5: stacked rings radius 4 snow, 3 ice, 2 snow, 1 ice (dome cap)
        placeRing(level, cposx, cposy + 5, cposz, 4.0F, 5.0F, snow);
        placeRing(level, cposx, cposy + 5, cposz, 3.0F, 10.0F, ice);
        placeRing(level, cposx, cposy + 5, cposz, 2.0F, 15.0F, snow);
        placeRing(level, cposx, cposy + 5, cposz, 1.0F, 15.0F, ice);

        // Hollow interior (keep ring walls) for y+1.y+4
        StructureHelper.hollowCylinder(level, cposx, cposy + 1, cposz, 5.0F, 4);

        // West door: plank threshold + oak door (gold ItemDoor facing 2 / north-ish)
        int doorX = Math.round(cposx - 6.0F + 0.5F);
        int doorZ = Math.round(cposz + 0.0F + 0.5F);
        StructureHelper.set(level, doorX, cposy, doorZ, planks);
        StructureHelper.set(level, doorX, cposy + 1, doorZ, air);
        StructureHelper.set(level, doorX, cposy + 2, doorZ, air);

        BlockState lowerDoor = Blocks.OAK_DOOR
                .defaultBlockState()
                .setValue(DoorBlock.FACING, Direction.EAST)
                .setValue(DoorBlock.HALF, DoubleBlockHalf.LOWER)
                .setValue(DoorBlock.OPEN, false);
        BlockState upperDoor = Blocks.OAK_DOOR
                .defaultBlockState()
                .setValue(DoorBlock.FACING, Direction.EAST)
                .setValue(DoorBlock.HALF, DoubleBlockHalf.UPPER)
                .setValue(DoorBlock.OPEN, false);
        StructureHelper.set(level, doorX, cposy + 1, doorZ, lowerDoor);
        StructureHelper.set(level, doorX, cposy + 2, doorZ, upperDoor);

        // Spawners (gold Rat / Ghost / Ghost Pumpkin Skelly)
        placeSpawner(level, random, cposx + 2, cposy + 1, cposz - 4, ModEntities.RAT.get());
        placeSpawner(level, random, cposx - 1, cposy + 1, cposz + 1, ModEntities.GHOST.get());
        placeSpawner(level, random, cposx + 3, cposy + 1, cposz + 4, ModEntities.GHOST_SKELLY.get());

        // Chest (gold facing meta 2 ≈ north)
        BlockPos chestPos = new BlockPos(cposx - 3, cposy + 1, cposz - 3);
        StructureHelper.set(
                level, chestPos, Blocks.CHEST.defaultBlockState().setValue(ChestBlock.FACING, Direction.NORTH));
        RandomizableContainer.setBlockEntityLootTable(level, random, chestPos, LOOT_TABLE);

        return true;
    }

    /** Places blocks along a circle at fixed Y (finite degree steps — no infinite loops). */
    private static void placeRing(
            WorldGenLevel level, int cx, int y, int cz, float radius, float stepDeg, BlockState state) {
        for (float deg = 0.0F; deg < 360.0F; deg += stepDeg) {
            float curx = (float) (radius * Math.cos(Math.toRadians(deg)));
            float curz = (float) (radius * Math.sin(Math.toRadians(deg)));
            StructureHelper.set(
                    level, (int) (cx + curx + 0.5F), y, (int) (cz + curz + 0.5F), state);
        }
    }

    private static void placeSpawner(
            WorldGenLevel level, RandomSource random, int x, int y, int z, EntityType<?> mob) {
        BlockPos pos = new BlockPos(x, y, z);
        StructureHelper.set(level, pos, Blocks.SPAWNER.defaultBlockState());
        if (level.getBlockEntity(pos) instanceof SpawnerBlockEntity spawner) {
            spawner.setEntityId(mob, random);
        }
    }
}
