package danger.orespawn.world.structures;

import danger.orespawn.init.ModBlocks;
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
 * Gold {@code GenericDungeon.makeCephadromeAltar}: stepped cobble/stone-brick platform
 * with sea-lantern pillars, Extreme Torches on the outer posts, and a center Eye-of-Ender
 * stand-in block. Port adds a {@link ModEntities#CEPHADROME} spawner on the peak (gold
 * had no spawner/chest — structure-only altar).
 * <p>
 * Gold {@code DungeonSpawnerBlock} type <b>34</b>. Origin is altar center at base Y.
 */
public final class CephadromeAltar {
    /** Gold base pad half-extent {@code width = length = 4}. */
    public static final int HALF = 4;

    /** Full base pad size. */
    public static final int WIDTH = HALF * 2 + 1;

    /** Layers y+0 . y+4 (torches on outer corners). */
    public static final int HEIGHT = 5;

    /** Gold had no loot; reserved for future use. */
    public static final ResourceKey<LootTable> LOOT_TABLE = ResourceKey.create(
            Registries.LOOT_TABLE,
            ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "chests/cephadrome_altar"));

    private CephadromeAltar() {}

    /**
     * Places the Cephadrome altar centered on {@code (cposx, cposy, cposz)}.
     *
     * @return true if written
     */
    public static boolean makeCephadromeAltar(
            WorldGenLevel level, RandomSource random, int cposx, int cposy, int cposz) {
        int minY = level.getMinBuildHeight();
        int maxY = level.getMaxBuildHeight();
        if (cposy < minY || cposy + HEIGHT >= maxY) {
            return false;
        }

        StructureHelper.clearBox(level, cposx - HALF, cposy, cposz - HALF, WIDTH, HEIGHT, WIDTH);

        BlockState cobble = Blocks.COBBLESTONE.defaultBlockState();
        BlockState brick = Blocks.STONE_BRICKS.defaultBlockState();
        BlockState air = Blocks.AIR.defaultBlockState();
        BlockState lantern = Blocks.SEA_LANTERN.defaultBlockState();
        BlockState torch = ModBlocks.EXTREME_TORCH.get().defaultBlockState();
        // Gold MyEyeOfEnderBlock
        BlockState eye = ModBlocks.EYE_OF_ENDER_BLOCK.get().defaultBlockState();

        // Gold y+0: full 9×9 cobble pad
        for (int i = -HALF; i <= HALF; i++) {
            for (int k = -HALF; k <= HALF; k++) {
                StructureHelper.set(level, cposx + i, cposy, cposz + k, cobble);
            }
        }

        // Gold y+1: 7×7 cobble with stone-brick cross + corner posts (half 3)
        placeLayerCross(level, cposx, cposy + 1, cposz, 3, cobble, brick);

        // Gold y+2: air with corner stone-brick posts (half 3), then 5×5 raised cross (half 2)
        for (int i = -3; i <= 3; i++) {
            for (int k = -3; k <= 3; k++) {
                BlockState bid = air;
                if ((k == -3 || k == 3) && (i == -3 || i == 3)) {
                    bid = brick;
                }
                StructureHelper.set(level, cposx + i, cposy + 2, cposz + k, bid);
            }
        }
        placeLayerCross(level, cposx, cposy + 2, cposz, 2, cobble, brick);

        // Gold y+3: corner sea lanterns (half 3), then 3×3 cobble with center eye (half 1)
        for (int i = -3; i <= 3; i++) {
            for (int k = -3; k <= 3; k++) {
                BlockState bid = air;
                if ((k == -3 || k == 3) && (i == -3 || i == 3)) {
                    bid = lantern;
                }
                StructureHelper.set(level, cposx + i, cposy + 3, cposz + k, bid);
            }
        }
        for (int i = -1; i <= 1; i++) {
            for (int k = -1; k <= 1; k++) {
                BlockState bid = cobble;
                if (k == 0 && i == 0) {
                    bid = eye;
                }
                if ((k == -1 || k == 1) && (i == -1 || i == 1)) {
                    bid = lantern;
                }
                StructureHelper.set(level, cposx + i, cposy + 3, cposz + k, bid);
            }
        }

        // Gold y+4: extreme torches on outer corners of half-3 frame
        for (int i = -3; i <= 3; i++) {
            for (int k = -3; k <= 3; k++) {
                BlockState bid = air;
                if ((k == -3 || k == 3) && (i == -3 || i == 3)) {
                    bid = torch;
                }
                StructureHelper.set(level, cposx + i, cposy + 4, cposz + k, bid);
            }
        }

        // Port: Cephadrome spawner on peak center (gold structure-only; expects entity spawn)
        placeSpawner(level, random, cposx, cposy + 4, cposz, cephadrome());

        return true;
    }

    private static void placeLayerCross(
            WorldGenLevel level,
            int cx,
            int y,
            int cz,
            int half,
            BlockState fill,
            BlockState accent) {
        for (int i = -half; i <= half; i++) {
            for (int k = -half; k <= half; k++) {
                BlockState bid = fill;
                if (k == 0 || i == 0) {
                    bid = accent;
                }
                if ((k == -half || k == half) && (i == -half || i == half)) {
                    bid = accent;
                }
                StructureHelper.set(level, cx + i, y, cz + k, bid);
            }
        }
    }

    private static EntityType<?> cephadrome() {
        return ModEntities.CEPHADROME != null ? ModEntities.CEPHADROME.get() : EntityType.PHANTOM;
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
