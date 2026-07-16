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
 * Gold {@code GenericDungeon.makeCrystalBattleTower}: cylindrical multi-floor tower
 * (radius 10) of {@link ModBlocks#CRYSTAL_STONE} with solid floors every 5 levels,
 * door gaps on intermediate levels, and a crystal-ore crown at the top.
 * <p>
 * Five combat floors (relative Y 1 / 6 / 11 / 16 / 21), each with a center loot chest
 * and two stacked spawners:
 * <ul>
 *   <li>Y+1 — Rat ×2 + {@code chests/crystal_battle_tower_rat}</li>
 *   <li>Y+6 — DungeonBeast ×2 + {@code chests/crystal_battle_tower_dungeon_beast}</li>
 *   <li>Y+11 — Urchin ×2 + {@code chests/crystal_battle_tower_urchin}</li>
 *   <li>Y+16 — Rotator ×2 + {@code chests/crystal_battle_tower_rotator}</li>
 *   <li>Y+21 — Vortex ×2 + {@code chests/crystal_battle_tower_vortex}</li>
 * </ul>
 * Gold DungeonSpawnerBlock type 33. Origin is tower center base.
 */
public final class CrystalBattleTower {
    /** Gold wall/floor radius. */
    public static final float RADIUS = 10.0F;
    /** Solid crystal body height j = 0.20 inclusive. */
    public static final int BODY_HEIGHT = 21;
    /** Crown ring levels j = 21.22 (CrystalCrystal stand-in). */
    public static final int CROWN_TOP = 22;
    /** Total vertical span from origin (0.CROWN_TOP). */
    public static final int TOTAL_HEIGHT = CROWN_TOP + 1;

    public static final ResourceKey<LootTable> LOOT_RAT = loot("chests/crystal_battle_tower_rat");
    public static final ResourceKey<LootTable> LOOT_DUNGEON_BEAST = loot("chests/crystal_battle_tower_dungeon_beast");
    public static final ResourceKey<LootTable> LOOT_URCHIN = loot("chests/crystal_battle_tower_urchin");
    public static final ResourceKey<LootTable> LOOT_ROTATOR = loot("chests/crystal_battle_tower_rotator");
    public static final ResourceKey<LootTable> LOOT_VORTEX = loot("chests/crystal_battle_tower_vortex");

    /** Primary loot key (rat floor) for callers that want a single default. */
    public static final ResourceKey<LootTable> LOOT_TABLE = LOOT_RAT;

    private CrystalBattleTower() {}

    private static ResourceKey<LootTable> loot(String path) {
        return ResourceKey.create(
                Registries.LOOT_TABLE, ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, path));
    }

    /**
     * Gold {@code makeCrystalBattleTower(world, cposx, cposy, cposz)}.
     * Origin is the tower center at base floor Y.
     *
     * @return true if the structure was written
     */
    public static boolean makeCrystalBattleTower(
            WorldGenLevel level, RandomSource random, int cposx, int cposy, int cposz) {
        int minY = level.getMinBuildHeight();
        int maxY = level.getMaxBuildHeight();
        if (cposy < minY || cposy + CROWN_TOP >= maxY) {
            return false;
        }

        BlockState crystalStone = ModBlocks.CRYSTAL_STONE.get().defaultBlockState();
        // Gold OreSpawnMain.CrystalCrystal — no 1:1 registry; CRYSTAL_ORE is the crystal ore stand-in
        BlockState crystalCrown = ModBlocks.CRYSTAL_ORE.get().defaultBlockState();
        BlockState air = Blocks.AIR.defaultBlockState();

        // Gold only painted walls/floors; underground leftover stone filled the cylinder.
        // Clear full column first so interiors are walkable.
        StructureHelper.clearCylinder(level, cposx, cposy, cposz, RADIUS + 1.0F, TOTAL_HEIGHT);

        // Body: j = 0.20 — solid floors every 5 levels, ring walls otherwise
        for (int j = 0; j <= 20; j++) {
            if (j % 5 == 0) {
                // Solid disc floor
                for (float currad = 0.0F; currad < RADIUS; currad += 0.33F) {
                    for (float curdeg = 0.0F; curdeg < 360.0F; curdeg += 5.0F) {
                        float curx = (float) (currad * Math.cos(Math.toRadians(curdeg)));
                        float curz = (float) (currad * Math.sin(Math.toRadians(curdeg)));
                        setFast(
                                level,
                                (int) (cposx + curx + 0.5F),
                                cposy + j,
                                (int) (cposz + curz + 0.5F),
                                crystalStone);
                    }
                }
            } else {
                // Re-hollow this ring level so leftover stone cannot remain mid-floor
                StructureHelper.hollowCylinder(level, cposx, cposy + j, cposz, RADIUS - 0.5F, 1);
                // Outer ring wall; door gap near deg 0 when j%5 in 1.3
                float currad = RADIUS;
                for (float curdeg = 0.0F; curdeg < 360.0F; curdeg += 5.0F) {
                    float curx = (float) (currad * Math.cos(Math.toRadians(curdeg)));
                    float curz = (float) (currad * Math.sin(Math.toRadians(curdeg)));
                    BlockState blk = crystalStone;
                    int mod = j % 5;
                    if (mod >= 1 && mod <= 3 && (curdeg < 10.0F || curdeg > 350.0F)) {
                        blk = air;
                    }
                    setFast(
                            level,
                            (int) (cposx + curx + 0.5F),
                            cposy + j,
                            (int) (cposz + curz + 0.5F),
                            blk);
                }
            }
        }

        // Crown rings j = 21.22
        for (int j = 21; j <= 22; j++) {
            float currad = RADIUS;
            for (float curdeg = 0.0F; curdeg < 360.0F; curdeg += 5.0F) {
                float curx = (float) (currad * Math.cos(Math.toRadians(curdeg)));
                float curz = (float) (currad * Math.sin(Math.toRadians(curdeg)));
                setFast(
                        level,
                        (int) (cposx + curx + 0.5F),
                        cposy + j,
                        (int) (cposz + curz + 0.5F),
                        crystalCrown);
            }
        }

        // Floor contents (gold relative Y offsets)
        placeFloor(level, random, cposx, cposy, cposz, 1, ModEntities.RAT.get(), LOOT_RAT);
        placeFloor(level, random, cposx, cposy, cposz, 6, ModEntities.DUNGEON_BEAST.get(), LOOT_DUNGEON_BEAST);
        placeFloor(level, random, cposx, cposy, cposz, 11, ModEntities.URCHIN.get(), LOOT_URCHIN);
        placeFloor(level, random, cposx, cposy, cposz, 16, ModEntities.ROTATOR.get(), LOOT_ROTATOR);
        placeFloor(level, random, cposx, cposy, cposz, 21, ModEntities.VORTEX.get(), LOOT_VORTEX);

        return true;
    }

    /**
     * Gold: chest at (cpos, floorY), two spawners stacked at floorY+1 and floorY+2.
     */
    private static void placeFloor(
            WorldGenLevel level,
            RandomSource random,
            int cx,
            int cy,
            int cz,
            int floorY,
            EntityType<?> mob,
            ResourceKey<LootTable> loot) {
        int baseY = cy + floorY;

        placeSpawner(level, random, cx, baseY + 1, cz, mob);
        placeSpawner(level, random, cx, baseY + 2, cz, mob);

        BlockPos chestPos = new BlockPos(cx, baseY, cz);
        BlockState chestState = Blocks.CHEST.defaultBlockState().setValue(ChestBlock.FACING, Direction.SOUTH);
        StructureHelper.set(level, chestPos, chestState);
        RandomizableContainer.setBlockEntityLootTable(level, random, chestPos, loot);
    }

    private static void placeSpawner(
            WorldGenLevel level, RandomSource random, int x, int y, int z, EntityType<?> type) {
        BlockPos pos = new BlockPos(x, y, z);
        StructureHelper.set(level, pos, Blocks.SPAWNER.defaultBlockState());
        if (level.getBlockEntity(pos) instanceof SpawnerBlockEntity spawner) {
            spawner.setEntityId(type, random);
        }
    }

    private static void setFast(WorldGenLevel level, int x, int y, int z, BlockState state) {
        StructureHelper.set(level, x, y, z, state);
    }
}
