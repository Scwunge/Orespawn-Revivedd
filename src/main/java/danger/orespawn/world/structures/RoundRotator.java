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
 * Gold {@code GenericDungeon.makeRoundRotator}: vertical wheel (circle in X–Y plane)
 * of bedrock (outer r=6) + CrystalPink (inner r=2) around hub at {@code y+6}, with
 * Rotator and Dungeon Beast spawners and a center chest (CrystalBattleTower vortex loot).
 * <p>
 * Port: outer ring uses obsidian (avoids bedrock spam); inner uses
 * {@link ModBlocks#CRYSTAL_PINK_BLOCK}; hub accents use {@link ModBlocks#CRYSTAL_COAL}
 * (gold CrystalCoal).
 * <p>
 * Gold {@code DungeonSpawnerBlock} type <b>45</b> (called with {@code y+1}).
 * Origin is the hub center at base Y; wheel hub sits at {@code y + HUB_Y}.
 */
public final class RoundRotator {
    /** Gold outer ring radius. */
    public static final float OUTER_RADIUS = 6.0F;

    /** Gold inner pink ring radius. */
    public static final float INNER_RADIUS = 2.0F;

    /** Gold hub offset: all geometry is around {@code cposy + 6}. */
    public static final int HUB_Y = 6;

    /**
     * Horizontal/vertical footprint of the wheel (2*outer + 1 margin).
     * Wheel lies in the X–Y plane at fixed Z.
     */
    public static final int WIDTH = (int) (OUTER_RADIUS * 2) + 3;

    /** From origin up through hub+outer radius. */
    public static final int HEIGHT = HUB_Y + (int) OUTER_RADIUS + 2;

    public static final ResourceKey<LootTable> LOOT_TABLE = ResourceKey.create(
            Registries.LOOT_TABLE,
            ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "chests/round_rotator"));

    private RoundRotator() {}

    /**
     * Places a round rotator wheel centered on {@code (cposx, cposy, cposz)}.
     *
     * @return true if written
     */
    public static boolean makeRoundRotator(
            WorldGenLevel level, RandomSource random, int cposx, int cposy, int cposz) {
        int minY = level.getMinBuildHeight();
        int maxY = level.getMaxBuildHeight();
        int hubY = cposy + HUB_Y;
        int half = (int) Math.ceil(OUTER_RADIUS) + 1;
        if (cposy < minY || hubY + half >= maxY || hubY - half < minY) {
            return false;
        }

        // Clear AABB covering the vertical wheel + small Z thickness
        StructureHelper.clearBox(
                level, cposx - half, hubY - half, cposz - 1, half * 2 + 1, half * 2 + 1, 3);

        BlockState outerRing = Blocks.OBSIDIAN.defaultBlockState(); // gold bedrock → obsidian
        BlockState innerRing = ModBlocks.CRYSTAL_PINK_BLOCK.get().defaultBlockState();
        BlockState hubAccent = ModBlocks.CRYSTAL_COAL.get().defaultBlockState();

        // Gold: outer ring radius 6, degree step 5°, circle in X–Y at z
        for (float curdeg = 0.0F; curdeg < 360.0F; curdeg += 5.0F) {
            float curx = (float) (OUTER_RADIUS * Math.cos(Math.toRadians(curdeg)));
            float cury = (float) (OUTER_RADIUS * Math.sin(Math.toRadians(curdeg)));
            StructureHelper.set(
                    level,
                    (int) (cposx + curx + 0.5F),
                    (int) (hubY + cury + 0.5F),
                    cposz,
                    outerRing);
        }

        // Gold: inner pink crystal ring radius 2
        for (float curdeg = 0.0F; curdeg < 360.0F; curdeg += 5.0F) {
            float curx = (float) (INNER_RADIUS * Math.cos(Math.toRadians(curdeg)));
            float cury = (float) (INNER_RADIUS * Math.sin(Math.toRadians(curdeg)));
            StructureHelper.set(
                    level,
                    (int) (cposx + curx + 0.5F),
                    (int) (hubY + cury + 0.5F),
                    cposz,
                    innerRing);
        }

        // Gold Rotator spawners around hub
        placeSpawner(level, random, cposx + 1, hubY + 1, cposz, rotator());
        placeSpawner(level, random, cposx - 1, hubY - 1, cposz, rotator());
        placeSpawner(level, random, cposx + 1, hubY - 1, cposz, rotator());
        placeSpawner(level, random, cposx - 1, hubY + 1, cposz, rotator());

        // Gold Dungeon Beast spawners on outer cross
        placeSpawner(level, random, cposx + 5, hubY, cposz, dungeonBeast());
        placeSpawner(level, random, cposx - 5, hubY, cposz, dungeonBeast());
        placeSpawner(level, random, cposx, hubY - 5, cposz, dungeonBeast());
        placeSpawner(level, random, cposx, hubY + 5, cposz, dungeonBeast());

        // Gold CrystalCoal accents at hub cardinals
        StructureHelper.set(level, cposx + 1, hubY, cposz, hubAccent);
        StructureHelper.set(level, cposx - 1, hubY, cposz, hubAccent);
        StructureHelper.set(level, cposx, hubY + 1, cposz, hubAccent);
        StructureHelper.set(level, cposx, hubY - 1, cposz, hubAccent);

        // Gold center chest (vortex loot list)
        BlockPos chestPos = new BlockPos(cposx, hubY, cposz);
        BlockState chest = Blocks.CHEST.defaultBlockState().setValue(ChestBlock.FACING, Direction.NORTH);
        StructureHelper.set(level, chestPos, chest);
        RandomizableContainer.setBlockEntityLootTable(level, random, chestPos, LOOT_TABLE);

        return true;
    }

    private static EntityType<?> rotator() {
        return ModEntities.ROTATOR != null ? ModEntities.ROTATOR.get() : EntityType.BLAZE;
    }

    private static EntityType<?> dungeonBeast() {
        return ModEntities.DUNGEON_BEAST != null
                ? ModEntities.DUNGEON_BEAST.get()
                : EntityType.ZOMBIE;
    }

    private static void placeSpawner(
            WorldGenLevel level, RandomSource random, int x, int y, int z, EntityType<?> type) {
        BlockPos pos = new BlockPos(x, y, z);
        StructureHelper.set(level, pos, Blocks.SPAWNER.defaultBlockState());
        if (level.getBlockEntity(pos) instanceof SpawnerBlockEntity spawner) {
            spawner.setEntityId(type, random);
        }
    }
}
