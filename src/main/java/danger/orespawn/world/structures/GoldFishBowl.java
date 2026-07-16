package danger.orespawn.world.structures;

import danger.orespawn.init.ModEntities;
import danger.orespawn.util.Reference;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootTable;

/**
 * Gold {@code GenericDungeon.makeGoldFishBowl}: glass aquarium (sand floor, water mid,
 * glass walls, open air upper, glass lid) with a Gold Fish spawner in the water column.
 * <p>
 * Gold {@code DungeonSpawnerBlock} type <b>17</b>. Origin is the min-corner of the
 * 5×5 glass pad at {@code y+1} (bowl extends to {@code x/z -1.5}).
 */
public final class GoldFishBowl {
    /** Gold floor pad is 5×5 ({@code i/k 0.4}). */
    public static final int INNER = 5;

    /** Outer glass footprint: {@code -1.5} → 7. */
    public static final int OUTER = 7;

    /** Outer min offset from origin (gold loops {@code -1}). */
    public static final int OUTER_MIN = -1;

    /** Structure spans {@code y+1 . y+8}. */
    public static final int HEIGHT = 8;

    public static final ResourceKey<LootTable> LOOT_TABLE = ResourceKey.create(
            Registries.LOOT_TABLE,
            ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "chests/gold_fish_bowl"));

    private GoldFishBowl() {}

    /**
     * Places the goldfish bowl with pad origin at {@code (x, y, z)}.
     *
     * @return true if written
     */
    public static boolean makeGoldFishBowl(WorldGenLevel level, RandomSource random, int x, int y, int z) {
        int minY = level.getMinBuildHeight();
        int maxY = level.getMaxBuildHeight();
        if (y + 1 < minY || y + HEIGHT >= maxY) {
            return false;
        }

        // Clear full outer volume (incl. wall ring) so stone never remains in the bowl
        StructureHelper.clearBox(level, x + OUTER_MIN, y + 1, z + OUTER_MIN, OUTER, HEIGHT, OUTER);

        BlockState glass = Blocks.GLASS.defaultBlockState();
        BlockState sand = Blocks.SAND.defaultBlockState();
        BlockState water = Blocks.WATER.defaultBlockState();
        BlockState glow = Blocks.GLOWSTONE.defaultBlockState();
        BlockState air = Blocks.AIR.defaultBlockState();

        // Gold j=1: 5×5 glass pad (floor plate under sand)
        for (int i = 0; i < INNER; i++) {
            for (int k = 0; k < INNER; k++) {
                StructureHelper.set(level, x + i, y + 1, z + k, glass);
            }
        }

        // Gold j=2: sand interior, glass rim on -1/5 edges
        for (int i = -1; i < 6; i++) {
            for (int k = -1; k < 6; k++) {
                boolean rim = i == -1 || k == -1 || i == 5 || k == 5;
                StructureHelper.set(level, x + i, y + 2, z + k, rim ? glass : sand);
            }
        }

        // Gold j=3: water interior, glass rim + 4 glowstone “fish lights”
        for (int i = -1; i < 6; i++) {
            for (int k = -1; k < 6; k++) {
                boolean rim = i == -1 || k == -1 || i == 5 || k == 5;
                StructureHelper.set(level, x + i, y + 3, z + k, rim ? glass : water);
            }
        }
        // gold glowstone corners of the water layer (0,0) (4,4) (0,4) (4,0)
        StructureHelper.set(level, x + 0, y + 3, z + 0, glow);
        StructureHelper.set(level, x + 4, y + 3, z + 4, glow);
        StructureHelper.set(level, x + 0, y + 3, z + 4, glow);
        StructureHelper.set(level, x + 4, y + 3, z + 0, glow);

        // Gold j=4: second water layer + glass rim
        for (int i = -1; i < 6; i++) {
            for (int k = -1; k < 6; k++) {
                boolean rim = i == -1 || k == -1 || i == 5 || k == 5;
                StructureHelper.set(level, x + i, y + 4, z + k, rim ? glass : water);
            }
        }

        // Gold j=5.7: air interior, glass walls
        for (int j = 5; j < 8; j++) {
            for (int i = -1; i < 6; i++) {
                for (int k = -1; k < 6; k++) {
                    boolean rim = i == -1 || k == -1 || i == 5 || k == 5;
                    StructureHelper.set(level, x + i, y + j, z + k, rim ? glass : air);
                }
            }
        }

        // Gold j=8: 5×5 glass lid
        for (int i = 0; i < INNER; i++) {
            for (int k = 0; k < INNER; k++) {
                StructureHelper.set(level, x + i, y + 8, z + k, glass);
            }
        }

        // Gold: Gold Fish spawner at center (2,6,2)
        BlockPos spawnerPos = new BlockPos(x + 2, y + 6, z + 2);
        StructureHelper.set(level, spawnerPos, Blocks.SPAWNER.defaultBlockState());
        if (level.getBlockEntity(spawnerPos) instanceof SpawnerBlockEntity spawner) {
            EntityType<?> type =
                    ModEntities.GOLD_FISH != null ? ModEntities.GOLD_FISH.get() : EntityType.TROPICAL_FISH;
            spawner.setEntityId(type, random);
        }

        return true;
    }
}
