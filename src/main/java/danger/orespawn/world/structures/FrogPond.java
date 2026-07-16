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
 * Gold {@code GenericDungeon.makeFrogPond}: 7×7 water floor, center water column,
 * lily pads on cardinals, Frog spawner at {@code y+2}.
 * <p>
 * Gold {@code DungeonSpawnerBlock} type <b>43</b> (calls with {@code y+1}).
 * Origin is pond center at water floor Y.
 */
public final class FrogPond {
    /** Gold half-extent: loops {@code -3.3} → 7×7. */
    public static final int HALF = 3;

    public static final int WIDTH = HALF * 2 + 1;

    /** Spawner / lily height above floor (gold places pad + spawner at y+2). */
    public static final int HEIGHT = 3;

    public static final ResourceKey<LootTable> LOOT_TABLE = ResourceKey.create(
            Registries.LOOT_TABLE,
            ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "chests/frog_pond"));

    private FrogPond() {}

    /**
     * Places a frog pond centered on {@code (x, y, z)}.
     *
     * @return true if written
     */
    public static boolean makeFrogPond(WorldGenLevel level, RandomSource random, int x, int y, int z) {
        int minY = level.getMinBuildHeight();
        int maxY = level.getMaxBuildHeight();
        if (y < minY || y + HEIGHT >= maxY) {
            return false;
        }

        // Clear volume so underground stone does not fill the pond
        StructureHelper.clearBox(level, x - HALF, y, z - HALF, WIDTH, HEIGHT, WIDTH);

        BlockState water = Blocks.WATER.defaultBlockState();
        BlockState lily = Blocks.LILY_PAD.defaultBlockState();

        // Gold: 7×7 water floor at y
        for (int i = -HALF; i <= HALF; i++) {
            for (int j = -HALF; j <= HALF; j++) {
                StructureHelper.set(level, x + i, y, z + j, water);
            }
        }

        // Gold: center water + cardinal water at y+1 (gold used still + flowing sources)
        StructureHelper.set(level, x, y + 1, z, water);
        StructureHelper.set(level, x - 1, y + 1, z, water);
        StructureHelper.set(level, x + 1, y + 1, z, water);
        StructureHelper.set(level, x, y + 1, z - 1, water);
        StructureHelper.set(level, x, y + 1, z + 1, water);

        // Gold: lily pads on cardinals at y+2
        StructureHelper.set(level, x - 1, y + 2, z, lily);
        StructureHelper.set(level, x + 1, y + 2, z, lily);
        StructureHelper.set(level, x, y + 2, z - 1, lily);
        StructureHelper.set(level, x, y + 2, z + 1, lily);

        // Gold: Frog spawner at (x, y+2, z) — placed after lilies so center stays spawner
        BlockPos spawnerPos = new BlockPos(x, y + 2, z);
        StructureHelper.set(level, spawnerPos, Blocks.SPAWNER.defaultBlockState());
        if (level.getBlockEntity(spawnerPos) instanceof SpawnerBlockEntity spawner) {
            // FROG is registered; gold name "Frog"
            EntityType<?> type = ModEntities.FROG != null ? ModEntities.FROG.get() : EntityType.TROPICAL_FISH;
            spawner.setEntityId(type, random);
        }

        return true;
    }
}
