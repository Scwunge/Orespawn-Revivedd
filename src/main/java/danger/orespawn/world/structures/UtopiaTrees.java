package danger.orespawn.world.structures;

import danger.orespawn.init.ModBlocks;
import danger.orespawn.items.ItemMagicApple;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Gold Utopia surface trees from {@code OreSpawnWorld} / {@code Trees}:
 * WindTree, SkyTree, and the rare "Tree of Goodness" (ore Magic-Apple tree).
 */
public final class UtopiaTrees {
    private UtopiaTrees() {}

    /**
     * Gold {@code Trees.WindTree}: tall oak trunk (~40–47), one-sided windblown leaves.
     *
     * @param dir 0.3 horizontal wind direction
     */
    public static boolean makeWindTree(WorldGenLevel level, RandomSource random, int x, int y, int z, int dir) {
        if (dir < 0 || dir > 3) {
            return false;
        }
        BlockState ground = level.getBlockState(new BlockPos(x, y, z));
        if (!ground.is(Blocks.GRASS_BLOCK) && !ground.is(Blocks.DIRT)) {
            return false;
        }
        int dirx = 1;
        int dirz = 0;
        if (dir == 1) {
            dirx = -1;
            dirz = 0;
        } else if (dir == 2) {
            dirx = 0;
            dirz = 1;
        } else if (dir == 3) {
            dirx = 0;
            dirz = -1;
        }

        int height = random.nextInt(8) + 40;
        int maxY = level.getMaxBuildHeight();
        if (y + height + 2 >= maxY) {
            height = Math.max(12, maxY - y - 3);
        }

        BlockState log = Blocks.OAK_LOG.defaultBlockState();
        BlockState leaves = Blocks.OAK_LEAVES.defaultBlockState().setValue(LeavesBlock.PERSISTENT, true);

        for (int j = 0; j < height; j++) {
            set(level, x, j + y, z, log);
            if (j > height / 5) {
                setIfAir(level, x + dirx, j + y, z + dirz, leaves);
                if (j > height / 4 && j % 4 == 0) {
                    windBranch(level, x, j + y, z, height - j, dirx, dirz, leaves);
                }
            }
        }
        setIfAir(level, x, y + height, z, leaves);
        return true;
    }

    private static void windBranch(
            WorldGenLevel level, int x, int y, int z, int length, int dirx, int dirz, BlockState leaves) {
        int len = Math.min(length, 12);
        for (int i = 1; i < len; i++) {
            setIfAir(level, x + i * dirx, y, z + i * dirz, leaves);
            if (i % 2 == 0) {
                setIfAir(level, x + i * dirx + dirz, y, z + i * dirz + dirx, leaves);
                setIfAir(level, x + i * dirx - dirz, y, z + i * dirz - dirx, leaves);
            }
        }
    }

    /**
     * Gold {@code Trees.SkyTree}: tall sky-log tower with cross branches.
     * Height capped (~70–100) — gold 190+ froze chunk gen / teleports.
     */
    public static boolean makeSkyTree(WorldGenLevel level, RandomSource random, int x, int y, int z) {
        BlockState ground = level.getBlockState(new BlockPos(x, y, z));
        if (!ground.is(Blocks.GRASS_BLOCK) && !ground.is(Blocks.DIRT)) {
            return false;
        }
        // Performance: absolute top Y of trunk, not gold's 190+ (multi-second freezes)
        int trunkHeight = 70 + random.nextInt(31); // 70–100 blocks tall
        int height = y + trunkHeight;
        int maxY = level.getMaxBuildHeight();
        if (height >= maxY - 4) {
            height = maxY - 5;
        }
        if (height - y < 24) {
            return false;
        }

        int width = 12 + random.nextInt(8); // was 25–34 — shorter branches = less lag
        BlockState log = ModBlocks.SKY_TREE_LOG.get().defaultBlockState();
        BlockState leaves = Blocks.OAK_LEAVES.defaultBlockState().setValue(LeavesBlock.PERSISTENT, true);

        for (int j = y; j <= height; j++) {
            set(level, x, j, z, log);
        }
        setIfAir(level, x, height + 1, z, leaves);
        skyBranch(level, x, height, z, width, 1, 0, log, leaves);
        skyBranch(level, x, height, z, width, -1, 0, log, leaves);
        skyBranch(level, x, height, z, width, 0, 1, log, leaves);
        skyBranch(level, x, height, z, width, 0, -1, log, leaves);

        int mid = height - 5 - random.nextInt(4);
        int w2 = Math.max(4, width / 3);
        skyBranch(level, x, mid, z, w2, 1, 0, log, leaves);
        skyBranch(level, x, mid, z, w2, -1, 0, log, leaves);
        skyBranch(level, x, mid, z, w2, 0, 1, log, leaves);
        skyBranch(level, x, mid, z, w2, 0, -1, log, leaves);
        return true;
    }

    private static void skyBranch(
            WorldGenLevel level,
            int x,
            int y,
            int z,
            int length,
            int dirx,
            int dirz,
            BlockState log,
            BlockState leaves) {
        for (int i = 1; i < length; i++) {
            int bx = x + i * dirx;
            int bz = z + i * dirz;
            set(level, bx, y, bz, log);
            setIfAir(level, bx, y + 1, bz, leaves);
            setIfAir(level, bx + dirz, y, bz + dirx, leaves);
            setIfAir(level, bx - dirz, y, bz - dirx, leaves);
        }
        setIfAir(level, x + length * dirx, y, z + length * dirz, leaves);
    }

    /**
     * Community "Tree of Goodness" — gold {@code addHugeTree} when {@code rand_treetype == 0}:
     * gold/emerald/diamond or lapis/ruby/amethyst square tower.
     */
    public static boolean makeGoodnessTree(ServerLevel level, RandomSource random, int x, int y, int z) {
        BlockState ground = level.getBlockState(new BlockPos(x, y, z));
        if (!ground.is(Blocks.GRASS_BLOCK) && !ground.is(Blocks.DIRT)) {
            return false;
        }
        // Origin at trunk base above ground (gold places on grass y-1)
        int trunkY = y + 1;
        if (random.nextBoolean()) {
            // Gold + emerald "leaves" + diamond stairs
            ItemMagicApple.growMaterialSquareTree(
                    level,
                    random,
                    x,
                    trunkY,
                    z,
                    Blocks.GOLD_BLOCK.defaultBlockState(),
                    Blocks.EMERALD_BLOCK.defaultBlockState(),
                    Blocks.DIAMOND_BLOCK.defaultBlockState());
        } else {
            // Lapis + ruby + amethyst
            ItemMagicApple.growMaterialSquareTree(
                    level,
                    random,
                    x,
                    trunkY,
                    z,
                    Blocks.LAPIS_BLOCK.defaultBlockState(),
                    ModBlocks.RUBY_BLOCK.get().defaultBlockState(),
                    ModBlocks.AMETHYST_BLOCK.get().defaultBlockState());
        }
        return true;
    }

    /** Normal huge hollow tree (oak + apple leaves) for Utopia surface. */
    public static boolean makeHugeOakTree(ServerLevel level, RandomSource random, int x, int y, int z) {
        BlockState ground = level.getBlockState(new BlockPos(x, y, z));
        if (!ground.is(Blocks.GRASS_BLOCK) && !ground.is(Blocks.DIRT)) {
            return false;
        }
        ItemMagicApple.growBigSquareTree(level, random, x, y + 1, z);
        return true;
    }

    private static void set(WorldGenLevel level, int x, int y, int z, BlockState state) {
        if (y < level.getMinBuildHeight() || y >= level.getMaxBuildHeight()) {
            return;
        }
        level.setBlock(new BlockPos(x, y, z), state, 2);
    }

    private static void setIfAir(WorldGenLevel level, int x, int y, int z, BlockState state) {
        if (y < level.getMinBuildHeight() || y >= level.getMaxBuildHeight()) {
            return;
        }
        BlockPos pos = new BlockPos(x, y, z);
        BlockState cur = level.getBlockState(pos);
        if (cur.isAir() || cur.canBeReplaced() || cur.is(Blocks.SHORT_GRASS) || cur.is(Blocks.TALL_GRASS)) {
            level.setBlock(pos, state, 2);
        }
    }
}
