package danger.orespawn.blocks;

import com.mojang.serialization.MapCodec;
import danger.orespawn.init.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * Gold {@code BlockCrystalPlant} — crystal tree saplings ({@code crystalsapling},
 * {@code crystalsapling2}, {@code crystalsapling3}). Reed-shaped single block that
 * randomly grows into one of three crystal tree shapes.
 * <p>
 * Registration (in {@code ModBlocks}):
 * <pre>{@code
 * public static final DeferredBlock<Block> CRYSTAL_SAPLING = register(
 *     "crystalsapling",
 *     () -> new BlockCrystalPlant(BlockCrystalPlant.defaultProps(), BlockCrystalPlant.TreeStyle.TALL));
 * public static final DeferredBlock<Block> CRYSTAL_SAPLING_2 = register(
 *     "crystalsapling2",
 *     () -> new BlockCrystalPlant(BlockCrystalPlant.defaultProps(), BlockCrystalPlant.TreeStyle.SCRAGGLY));
 * public static final DeferredBlock<Block> CRYSTAL_SAPLING_3 = register(
 *     "crystalsapling3",
 *     () -> new BlockCrystalPlant(BlockCrystalPlant.defaultProps(), BlockCrystalPlant.TreeStyle.TALL_BLUE));
 * }</pre>
 * Client: cutout render layer. Textures: {@code crystalsapling.png} / 2 / 3.
 * <p>
 * {@link BlockCrystalLeaves} drop lookup prefers {@code crystal_sapling[_2|_3]} then
 * falls back to {@code crystalsapling}; either registry id works if leaves stay as-is.
 */
public class BlockCrystalPlant extends BushBlock {
    public static final MapCodec<BlockCrystalPlant> CODEC =
            simpleCodec(p -> new BlockCrystalPlant(p, TreeStyle.TALL));

    /** Gold reed AABB: 0.375 half-width, full height. */
    private static final VoxelShape SHAPE = Block.box(2.0, 0.0, 2.0, 14.0, 16.0, 14.0);

    public enum TreeStyle {
        /** Gold MyCrystalPlant → TallCrystalTree + crystaltreeleaves */
        TALL,
        /** Gold MyCrystalPlant2 → ScragglyCrystalTreeWithBranches + leaves2 */
        SCRAGGLY,
        /** Gold MyCrystalPlant3 → TallCrystalTreeBlue + leaves3 */
        TALL_BLUE
    }

    private final TreeStyle style;

    public BlockCrystalPlant(BlockBehaviour.Properties properties, TreeStyle style) {
        super(properties);
        this.style = style;
    }

    public static BlockBehaviour.Properties defaultProps() {
        return BlockBehaviour.Properties.of()
                .mapColor(MapColor.COLOR_PURPLE)
                .noCollission()
                .instabreak()
                .sound(SoundType.GRASS)
                .offsetType(BlockBehaviour.OffsetType.XZ)
                .pushReaction(PushReaction.DESTROY)
                .randomTicks();
    }

    public TreeStyle getStyle() {
        return this.style;
    }

    @Override
    protected MapCodec<? extends BushBlock> codec() {
        return CODEC;
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        var offset = state.getOffset(level, pos);
        return SHAPE.move(offset.x, offset.y, offset.z);
    }

    /** Gold canPlaceBlockOn: grass, dirt, farmland, CrystalGrass. */
    @Override
    protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
        return state.is(Blocks.GRASS_BLOCK)
                || state.is(Blocks.DIRT)
                || state.is(Blocks.COARSE_DIRT)
                || state.is(Blocks.PODZOL)
                || state.is(Blocks.FARMLAND)
                || state.is(Blocks.ROOTED_DIRT)
                || state.is(Blocks.MUD)
                || state.is(ModBlocks.CRYSTAL_GRASS.get());
    }

    @Override
    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        BlockState below = level.getBlockState(pos.below());
        return this.mayPlaceOn(below, level, pos.below());
    }

    @Override
    protected boolean isRandomlyTicking(BlockState state) {
        return true;
    }

    /** Gold updateTick: 1/5 chance remove self and grow tree. */
    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (!this.canSurvive(state, level, pos)) {
            level.destroyBlock(pos, true);
            return;
        }
        if (random.nextInt(5) != 1) {
            return;
        }
        level.setBlock(pos, Blocks.AIR.defaultBlockState(), Block.UPDATE_ALL);
        growTree(level, pos, random, resolveStyle());
    }

    /**
     * Grow a crystal tree of the given style at {@code origin} (sapling already cleared).
     * Used by sapling ticks and crystal-dim worldgen for immediate full trees.
     * Accepts {@link LevelAccessor} so worldgen regions work (not only {@link ServerLevel}).
     */
    public static void growTree(LevelAccessor level, BlockPos origin, RandomSource random, TreeStyle style) {
        switch (style != null ? style : TreeStyle.TALL) {
            case SCRAGGLY -> growScragglyTree(level, origin, random);
            case TALL_BLUE -> growTallBlueTree(level, origin, random);
            default -> growTallTree(level, origin, random);
        }
    }

    /** Random style pick for worldgen variety (tall / scraggly / blue). */
    public static TreeStyle randomStyle(RandomSource random) {
        return switch (random.nextInt(3)) {
            case 1 -> TreeStyle.SCRAGGLY;
            case 2 -> TreeStyle.TALL_BLUE;
            default -> TreeStyle.TALL;
        };
    }

    /**
     * Prefer constructor style; fall back to registry path so simpleCodec-loaded
     * instances still pick the right tree.
     */
    private TreeStyle resolveStyle() {
        if (this.style != null && this.style != TreeStyle.TALL) {
            return this.style;
        }
        String path = BuiltInRegistries.BLOCK.getKey(this).getPath();
        return switch (path) {
            case "crystalsapling2", "crystal_sapling_2" -> TreeStyle.SCRAGGLY;
            case "crystalsapling3", "crystal_sapling_3" -> TreeStyle.TALL_BLUE;
            default -> this.style != null ? this.style : TreeStyle.TALL;
        };
    }

    /** Gold animateTick: rare happyVillager sparkles. */
    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (random.nextInt(30) != 1) {
            return;
        }
        for (int i = 0; i < 10; i++) {
            level.addParticle(
                    ParticleTypes.HAPPY_VILLAGER,
                    pos.getX() + random.nextFloat(),
                    pos.getY() + random.nextFloat(),
                    pos.getZ() + random.nextFloat(),
                    0.0,
                    0.0,
                    0.0);
        }
    }

    // ——— Gold TallCrystalTree ———
    private static void growTallTree(LevelAccessor world, BlockPos origin, RandomSource random) {
        int x = origin.getX();
        int y = origin.getY();
        int z = origin.getZ();
        Block log = logBlock();
        Block leaves = leaves1();

        int trunk = 10 + random.nextInt(12);
        int total = trunk + random.nextInt(18);

        for (int k = 0; k < trunk; k++) {
            BlockState at = world.getBlockState(new BlockPos(x, y + k, z));
            if (k >= 1 && !isReplaceable(at, log, leaves)) {
                return;
            }
            setFast(world, x, y + k, z, log);
        }

        y += trunk - 1;
        for (int step = trunk; step < total; step++) {
            y++;
            BlockState at = world.getBlockState(new BlockPos(x, y, z));
            if (!isReplaceable(at, log, leaves)) {
                break;
            }
            setFast(world, x, y, z, log);
            if (step % 4 == 0) {
                for (int m = -1; m < 2; m++) {
                    for (int n = -1; n < 2; n++) {
                        if (random.nextInt(2) == 1
                                && world.getBlockState(new BlockPos(x + m, y, z + n)).isAir()) {
                            setFast(world, x + m, y, z + n, leaves);
                        }
                    }
                }
            }
        }

        y++;
        for (int m = -1; m < 2; m++) {
            for (int n = -1; n < 2; n++) {
                if (random.nextInt(2) == 1
                        && world.getBlockState(new BlockPos(x + m, y, z + n)).isAir()) {
                    setFast(world, x + m, y, z + n, log);
                }
            }
        }
        for (int m = -3; m < 4; m++) {
            for (int n = -3; n < 4; n++) {
                if (world.getBlockState(new BlockPos(x + m, y, z + n)).isAir()) {
                    setFast(world, x + m, y, z + n, leaves);
                }
            }
        }
        y++;
        for (int m = -1; m < 2; m++) {
            for (int n = -1; n < 2; n++) {
                if (world.getBlockState(new BlockPos(x + m, y, z + n)).isAir()) {
                    setFast(world, x + m, y, z + n, leaves);
                }
            }
        }
    }

    // ——— Gold ScragglyCrystalTreeWithBranches ———
    private static void growScragglyTree(LevelAccessor world, BlockPos origin, RandomSource random) {
        int x = origin.getX();
        int y = origin.getY();
        int z = origin.getZ();
        Block log = logBlock();
        Block leaves = leaves2();

        int base = 1 + random.nextInt(2);
        int total = base + random.nextInt(8);

        for (int k = 0; k < base; k++) {
            BlockState at = world.getBlockState(new BlockPos(x, y + k, z));
            if (k >= 1 && !isReplaceable(at, log, leaves)) {
                return;
            }
            setFast(world, x, y + k, z, log);
        }

        y += base - 1;
        for (int step = base; step < total; step++) {
            int ix = random.nextInt(2) - random.nextInt(2);
            int iz = random.nextInt(2) - random.nextInt(2);
            int iy = random.nextInt(4) > 0 ? 1 : 0;
            x += ix;
            z += iz;
            y += iy;
            BlockState at = world.getBlockState(new BlockPos(x, y, z));
            if (!isReplaceable(at, log, leaves)) {
                break;
            }
            setFast(world, x, y, z, log);
            if (random.nextInt(4) == 1) {
                makeScragglyBranch(
                        world,
                        x,
                        y,
                        z,
                        random.nextInt(1 + total - step),
                        random.nextInt(2) - random.nextInt(2),
                        random.nextInt(2) - random.nextInt(2),
                        random,
                        log,
                        leaves);
            }
            for (int m = -1; m < 2; m++) {
                for (int n = -1; n < 2; n++) {
                    if (random.nextInt(2) == 1
                            && world.getBlockState(new BlockPos(x + m, y, z + n)).isAir()) {
                        setFast(world, x + m, y, z + n, leaves);
                    }
                }
            }
            if (random.nextInt(2) == 1
                    && world.getBlockState(new BlockPos(x, y + 1, z)).isAir()) {
                setFast(world, x, y + 1, z, leaves);
            }
        }
    }

    private static void makeScragglyBranch(
            LevelAccessor world,
            int x,
            int y,
            int z,
            int len,
            int biasx,
            int biasz,
            RandomSource random,
            Block log,
            Block leaves) {
        for (int k = 0; k < len; k++) {
            int ix = random.nextInt(2) - random.nextInt(2) + biasx;
            int iz = random.nextInt(2) - random.nextInt(2) + biasz;
            if (ix > 1) {
                ix = 1;
            }
            if (ix < -1) {
                ix = -1;
            }
            if (iz > 1) {
                iz = 1;
            }
            if (iz < -1) {
                iz = -1;
            }
            int iy = random.nextInt(3) > 0 ? 1 : 0;
            x += ix;
            z += iz;
            y += iy;
            BlockState at = world.getBlockState(new BlockPos(x, y, z));
            if (!isReplaceable(at, log, leaves)) {
                return;
            }
            setFast(world, x, y, z, log);
            for (int m = -1; m < 2; m++) {
                for (int n = -1; n < 2; n++) {
                    if (random.nextInt(2) == 1
                            && world.getBlockState(new BlockPos(x + m, y, z + n)).isAir()) {
                        setFast(world, x + m, y, z + n, leaves);
                    }
                }
            }
            if (random.nextInt(2) == 1
                    && world.getBlockState(new BlockPos(x, y + 1, z)).isAir()) {
                setFast(world, x, y + 1, z, leaves);
            }
        }
    }

    // ——— Gold TallCrystalTreeBlue ———
    private static void growTallBlueTree(LevelAccessor world, BlockPos origin, RandomSource random) {
        int x = origin.getX();
        int y = origin.getY();
        int z = origin.getZ();
        Block log = logBlock();
        Block leaves = leaves3();

        int trunk = 5 + random.nextInt(6);
        int total = 2 + trunk + random.nextInt(12);

        for (int k = 0; k < trunk; k++) {
            BlockState at = world.getBlockState(new BlockPos(x, y + k, z));
            if (k >= 1 && !isReplaceable(at, log, leaves)) {
                return;
            }
            setFast(world, x, y + k, z, log);
        }

        y += trunk - 1;
        for (int step = trunk; step < total; step++) {
            y++;
            BlockState at = world.getBlockState(new BlockPos(x, y, z));
            if (!isReplaceable(at, log, leaves)) {
                break;
            }
            setFast(world, x, y, z, log);
            if (step % 3 == 0) {
                for (int m = -1; m < 2; m++) {
                    for (int n = -1; n < 2; n++) {
                        if (random.nextInt(2) == 1
                                && world.getBlockState(new BlockPos(x + m, y, z + n)).isAir()) {
                            setFast(world, x + m, y, z + n, leaves);
                        }
                    }
                }
            }
        }

        y++;
        for (int m = -1; m < 2; m++) {
            for (int n = -1; n < 2; n++) {
                if (random.nextInt(2) == 1
                        && world.getBlockState(new BlockPos(x + m, y, z + n)).isAir()) {
                    setFast(world, x + m, y, z + n, log);
                }
            }
        }
        for (int m = -3; m < 4; m++) {
            for (int n = -3; n < 4; n++) {
                if (world.getBlockState(new BlockPos(x + m, y, z + n)).isAir()) {
                    setFast(world, x + m, y, z + n, leaves);
                }
            }
        }
        y++;
        for (int m = -1; m < 2; m++) {
            for (int n = -1; n < 2; n++) {
                if (world.getBlockState(new BlockPos(x + m, y, z + n)).isAir()) {
                    setFast(world, x + m, y, z + n, leaves);
                }
            }
        }
    }

    private static boolean isReplaceable(BlockState state, Block log, Block leaves) {
        return state.isAir() || state.is(log) || state.is(leaves);
    }

    private static void setFast(LevelAccessor world, int x, int y, int z, Block block) {
        BlockState state = block.defaultBlockState();
        // Avoid instant leaf decay (default distance=7) until log distance ticks catch up.
        // Logs must be in #minecraft:logs so LeavesBlock distance refresh keeps them alive.
        if (state.hasProperty(LeavesBlock.DISTANCE)) {
            state = state.setValue(LeavesBlock.DISTANCE, 1);
        }
        if (state.hasProperty(LeavesBlock.PERSISTENT)) {
            state = state.setValue(LeavesBlock.PERSISTENT, false);
        }
        world.setBlock(new BlockPos(x, y, z), state, Block.UPDATE_CLIENTS);
    }

    private static Block logBlock() {
        return ModBlocks.CRYSTAL_TREE_LOG.get();
    }

    private static Block leaves1() {
        return ModBlocks.CRYSTAL_TREE_LEAVES.get();
    }

    private static Block leaves2() {
        return ModBlocks.CRYSTAL_TREE_LEAVES_2.get();
    }

    private static Block leaves3() {
        return ModBlocks.CRYSTAL_TREE_LEAVES_3.get();
    }
}
