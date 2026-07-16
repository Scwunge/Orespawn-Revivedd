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
 * Gold {@code GenericDungeon.makeRedAntHangout}: 16×16 stone foundation + gravel/dirt pad
 * with MyRedAntBlock corners, live Robot Red Ant at center.
 * <p>
 * Port: dirt / ant theme — dirt pad with {@link ModBlocks#ANT_BLOCK} corner patches,
 * {@link ModEntities#RED_ANT} spawners (user request). Center spawner also covers gold's
 * Robot Red Ant via {@link ModEntities#ANT_ROBOT} when present.
 * <p>
 * Gold {@code DungeonSpawnerBlock} type <b>49</b>. Origin is the SW corner of the pad.
 */
public final class RedAntHangout {
    /** Gold loops {@code i/k = 0.15}. */
    public static final int WIDTH = 16;

    /**
     * Gold {@code j = -1 . 15}: foundation at -1, pad at 0, air 1.15.
     * Clear height = 17.
     */
    public static final int HEIGHT = 17;

    /** Gold air column above pad. */
    public static final int AIR_HEIGHT = 15;

    /** Gold corner ant-block band: {@code i/k < 3 || i/k > 12}. */
    public static final int ANT_CORNER = 3;

    public static final ResourceKey<LootTable> LOOT_TABLE = ResourceKey.create(
            Registries.LOOT_TABLE,
            ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "chests/red_ant_hangout"));

    private RedAntHangout() {}

    /**
     * Places a red-ant hangout with corner at {@code (cposx, cposy, cposz)} (pad Y).
     * Structure extends one block below origin for the stone foundation.
     *
     * @return true if written
     */
    public static boolean makeRedAntHangout(
            WorldGenLevel level, RandomSource random, int cposx, int cposy, int cposz) {
        int minY = level.getMinBuildHeight();
        int maxY = level.getMaxBuildHeight();
        int foundationY = cposy - 1;
        if (foundationY < minY || cposy + AIR_HEIGHT >= maxY) {
            return false;
        }

        StructureHelper.clearBox(level, cposx, foundationY, cposz, WIDTH, HEIGHT, WIDTH);

        BlockState stone = Blocks.STONE.defaultBlockState();
        BlockState dirt = Blocks.DIRT.defaultBlockState();
        BlockState gravel = Blocks.GRAVEL.defaultBlockState();
        BlockState antBlock = ModBlocks.ANT_BLOCK.get().defaultBlockState();
        BlockState coarse = Blocks.COARSE_DIRT.defaultBlockState();

        // Gold: j=-1 stone; j=0 gravel, ant-block in outer corner bands
        for (int i = 0; i < WIDTH; i++) {
            for (int k = 0; k < WIDTH; k++) {
                StructureHelper.set(level, cposx + i, foundationY, cposz + k, stone);

                boolean outerCorner =
                        (i < ANT_CORNER || i > WIDTH - 1 - ANT_CORNER)
                                && (k < ANT_CORNER || k > WIDTH - 1 - ANT_CORNER);
                BlockState pad;
                if (outerCorner) {
                    pad = antBlock; // gold MyRedAntBlock
                } else if (random.nextInt(3) == 0) {
                    pad = dirt; // dirt theme accent
                } else if (random.nextInt(5) == 0) {
                    pad = coarse;
                } else {
                    pad = gravel;
                }
                StructureHelper.set(level, cposx + i, cposy, cposz + k, pad);
            }
        }

        // Keep air column clear (already cleared; reaffirm interior walk space)
        StructureHelper.clearBox(level, cposx, cposy + 1, cposz, WIDTH, AIR_HEIGHT, WIDTH);

        // RED_ANT spawners at corner ant mounds (user request)
        placeSpawner(level, random, cposx + 1, cposy + 1, cposz + 1, redAnt());
        placeSpawner(level, random, cposx + WIDTH - 2, cposy + 1, cposz + 1, redAnt());
        placeSpawner(level, random, cposx + 1, cposy + 1, cposz + WIDTH - 2, redAnt());
        placeSpawner(level, random, cposx + WIDTH - 2, cposy + 1, cposz + WIDTH - 2, redAnt());

        // Gold center "Robot Red Ant" → ANT_ROBOT spawner when available, else more RED_ANT
        EntityType<?> center =
                ModEntities.ANT_ROBOT != null ? ModEntities.ANT_ROBOT.get() : redAnt();
        placeSpawner(level, random, cposx + WIDTH / 2, cposy + 1, cposz + WIDTH / 2, center);

        return true;
    }

    private static EntityType<?> redAnt() {
        return ModEntities.RED_ANT != null ? ModEntities.RED_ANT.get() : EntityType.SILVERFISH;
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
