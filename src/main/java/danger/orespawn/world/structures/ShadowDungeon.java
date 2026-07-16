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
 * Gold {@code GenericDungeon.makeShadowDungeon}: stepped inverted / upright dark shell
 * (total width 19, layer inset +1 each level). Gold used obsidian / bedrock / nether brick
 * with Ender Reaper / Nightmare spawners and shadow chests on odd mid layers.
 * <p>
 * Port materials: black wool / obsidian / nether brick mix (no bedrock). Spawners: Ghost /
 * PitchBlack (shadow-ish; gold Nightmare → PitchBlack, Ender Reaper retained as alternate).
 * <p>
 * Gold {@code DungeonSpawnerBlock} type <b>9</b>. Origin is the mid-level corner.
 */
public final class ShadowDungeon {
    /** Gold {@code totalwidth = 19}. */
    public static final int WIDTH = 19;

    /**
     * Layers per direction (width 19,17,.,1 → 10 steps). Structure spans
     * {@code y - (LAYERS-1)} . {@code y + (LAYERS-1)}.
     */
    public static final int LAYERS = 10;

    /** Vertical span from lowest to highest layer inclusive. */
    public static final int HEIGHT = LAYERS * 2 - 1;

    public static final ResourceKey<LootTable> LOOT_TABLE = ResourceKey.create(
            Registries.LOOT_TABLE,
            ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "chests/shadow_dungeon"));

    private ShadowDungeon() {}

    /**
     * Places a shadow dungeon with mid-level corner at {@code (cposx, cposy, cposz)}.
     *
     * @return true if written
     */
    public static boolean makeShadowDungeon(
            WorldGenLevel level, RandomSource random, int cposx, int cposy, int cposz) {
        int minY = level.getMinBuildHeight();
        int maxY = level.getMaxBuildHeight();
        int bottom = cposy - (LAYERS - 1);
        int top = cposy + (LAYERS - 1);
        if (bottom < minY || top >= maxY) {
            return false;
        }

        // Full AABB of both stepped shells (max footprint is WIDTH at mid layer)
        StructureHelper.clearBox(level, cposx, bottom, cposz, WIDTH, HEIGHT, WIDTH);

        // Downward inverted pyramid (y decreasing, inset +x/+z)
        int yoff = 0;
        int xoff = 0;
        int zoff = 0;
        for (int width = WIDTH; width > 0; width -= 2) {
            placeLayer(level, random, cposx + xoff, cposy - yoff, cposz + zoff, width, true, yoff);

            // Gold: spawners + chests only on mid bands width 9.15
            if (width <= 15 && width >= 9) {
                EntityType<?> mob = pickShadowMob(random, yoff);
                placeCornerSpawners(level, random, cposx + xoff, cposy - yoff, cposz + zoff, width, mob);
                if ((yoff & 1) != 0) {
                    fillShadowChests(level, random, cposx + xoff, cposy - yoff, cposz + zoff, width);
                }
            }

            xoff++;
            zoff++;
            yoff++;
        }

        // Upward pyramid (no cross nether-brick accents / no spawners — matches gold)
        yoff = 0;
        xoff = 0;
        zoff = 0;
        for (int width = WIDTH; width > 0; width -= 2) {
            placeLayer(level, random, cposx + xoff, cposy + yoff, cposz + zoff, width, false, yoff);
            xoff++;
            zoff++;
            yoff++;
        }

        return true;
    }

    /**
     * Hollow ring layer. Gold wall: even yoff → obsidian, odd → bedrock; nether-brick
     * cross on mid of each side (downward only). Port: dark mix, no bedrock.
     */
    private static void placeLayer(
            WorldGenLevel level,
            RandomSource random,
            int ox,
            int oy,
            int oz,
            int width,
            boolean downward,
            int yoff) {
        for (int i = 0; i < width; i++) {
            for (int k = 0; k < width; k++) {
                if (k != 0 && k != width - 1 && i != 0 && i != width - 1) {
                    StructureHelper.set(level, ox + i, oy, oz + k, Blocks.AIR.defaultBlockState());
                    continue;
                }
                BlockState blk = wallBlock(random, yoff);
                if (downward) {
                    int mid = width / 2;
                    if ((k >= mid - 1 && k <= mid + 1) || (i >= mid - 1 && i <= mid + 1)) {
                        blk = Blocks.NETHER_BRICKS.defaultBlockState();
                    }
                }
                StructureHelper.set(level, ox + i, oy, oz + k, blk);
            }
        }
    }

    /** Dark palette: black wool / obsidian / nether brick (gold even=obsidian, odd=bedrock). */
    private static BlockState wallBlock(RandomSource random, int yoff) {
        if ((yoff & 1) != 0) {
            // odd layers: mostly black wool (gold bedrock stand-in), some nether brick
            return random.nextInt(4) == 0
                    ? Blocks.NETHER_BRICKS.defaultBlockState()
                    : Blocks.BLACK_WOOL.defaultBlockState();
        }
        // even layers: obsidian with occasional black wool
        return random.nextInt(5) == 0
                ? Blocks.BLACK_WOOL.defaultBlockState()
                : Blocks.OBSIDIAN.defaultBlockState();
    }

    /**
     * Gold: odd yoff → "Ender Reaper", even → "Nightmare".
     * Port: Ender Reaper / PitchBlack with Ghost as dark-room spice.
     */
    private static EntityType<?> pickShadowMob(RandomSource random, int yoff) {
        if ((yoff & 1) != 0) {
            if (ModEntities.ENDER_REAPER != null && random.nextInt(3) != 0) {
                return ModEntities.ENDER_REAPER.get();
            }
            if (ModEntities.GHOST != null) {
                return ModEntities.GHOST.get();
            }
            return EntityType.ENDERMAN;
        }
        if (ModEntities.PITCH_BLACK != null && random.nextBoolean()) {
            return ModEntities.PITCH_BLACK.get();
        }
        if (ModEntities.GHOST != null) {
            return ModEntities.GHOST.get();
        }
        return EntityType.WITHER_SKELETON;
    }

    private static void placeCornerSpawners(
            WorldGenLevel level,
            RandomSource random,
            int ox,
            int oy,
            int oz,
            int width,
            EntityType<?> mob) {
        placeSpawner(level, random, ox + 1, oy, oz + 1, mob);
        placeSpawner(level, random, ox + width - 2, oy, oz + 1, mob);
        placeSpawner(level, random, ox + 1, oy, oz + width - 2, mob);
        placeSpawner(level, random, ox + width - 2, oy, oz + width - 2, mob);
    }

    private static void placeSpawner(
            WorldGenLevel level, RandomSource random, int x, int y, int z, EntityType<?> type) {
        BlockPos pos = new BlockPos(x, y, z);
        StructureHelper.set(level, pos, Blocks.SPAWNER.defaultBlockState());
        if (level.getBlockEntity(pos) instanceof SpawnerBlockEntity spawner) {
            spawner.setEntityId(type, random);
        }
    }

    /** Gold {@code fill_shadow_chests}: 4 wall-mid chests. */
    private static void fillShadowChests(
            WorldGenLevel level, RandomSource random, int ox, int oy, int oz, int width) {
        placeChest(level, random, ox + 1, oy, oz + width / 2, Direction.EAST);
        placeChest(level, random, ox + width - 2, oy, oz + width / 2, Direction.WEST);
        placeChest(level, random, ox + width / 2, oy, oz + 1, Direction.SOUTH);
        placeChest(level, random, ox + width / 2, oy, oz + width - 2, Direction.NORTH);
    }

    private static void placeChest(
            WorldGenLevel level, RandomSource random, int x, int y, int z, Direction facing) {
        BlockPos pos = new BlockPos(x, y, z);
        BlockState chest = Blocks.CHEST.defaultBlockState().setValue(ChestBlock.FACING, facing);
        StructureHelper.set(level, pos, chest);
        RandomizableContainer.setBlockEntityLootTable(level, random, pos, LOOT_TABLE);
    }
}
