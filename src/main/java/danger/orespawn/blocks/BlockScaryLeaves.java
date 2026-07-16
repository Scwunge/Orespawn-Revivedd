package danger.orespawn.blocks;

import com.mojang.serialization.MapCodec;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.level.storage.loot.LootParams;
import danger.orespawn.util.Reference;

/**
 * Gold {@code BlockScaryLeaves} — used for leaves_scary, leaves_cherry, leaves_peach.
 * Daytime → apple leaves (scary variant only). Fruit drops for cherry/peach by registry path.
 */
public class BlockScaryLeaves extends LeavesBlock {
    public static final MapCodec<BlockScaryLeaves> CODEC = simpleCodec(BlockScaryLeaves::new);

    public BlockScaryLeaves(BlockBehaviour.Properties properties) {
        super(properties);
    }

    /** Gold scary hardness 0.2 (cherry/peach 0.15). */
    public static BlockBehaviour.Properties defaultProps() {
        return BlockBehaviour.Properties.of()
                .mapColor(MapColor.COLOR_BLACK)
                .strength(0.2f)
                .randomTicks()
                .noOcclusion()
                .isSuffocating((s, g, p) -> false)
                .isViewBlocking((s, g, p) -> false)
                .isRedstoneConductor((s, g, p) -> false)
                .sound(SoundType.GRASS)
                .pushReaction(PushReaction.DESTROY);
    }

    public static BlockBehaviour.Properties fruitTreeProps() {
        return defaultProps().mapColor(MapColor.PLANT).strength(0.15f);
    }

    @Override
    public MapCodec<? extends LeavesBlock> codec() {
        return CODEC;
    }

    /** Gold always ticked (fruit + day→apple transform). */
    @Override
    protected boolean isRandomlyTicking(BlockState state) {
        return true;
    }

    @Override
    protected List<ItemStack> getDrops(BlockState state, LootParams.Builder params) {
        return rollFruit(params.getLevel().getRandom());
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        super.randomTick(state, level, pos, random);
        if (level.isClientSide) {
            return;
        }
        int range = 2;
        if (!level.hasChunksAt(pos.offset(-range, -range, -range), pos.offset(range, range, range))) {
            return;
        }
        for (int dx = -range; dx <= range; dx++) {
            for (int dy = -range; dy <= 0; dy++) {
                for (int dz = -range; dz <= range; dz++) {
                    if (Math.abs(dx) + Math.abs(dy) + Math.abs(dz) > 3) {
                        continue;
                    }
                    BlockPos support = pos.offset(dx, dy, dz);
                    BlockState supportState = level.getBlockState(support);
                    if (isLeafSupport(supportState, level, support)) {
                        String path = BuiltInRegistries.BLOCK.getKey(this).getPath();
                        boolean isScary = path.contains("scary");
                        // Gold: daytime (t < 12000) scary → apple leaves
                        long t = level.getDayTime() % 24000L;
                        if (isScary && t < 12000L) {
                            tryTransformApple(level, pos, state);
                        }
                        BlockPos below = pos.below();
                        if (level.getBlockState(below).isAir() && random.nextInt(20) == 3) {
                            for (ItemStack stack : rollFruit(random)) {
                                Block.popResource(level, below, stack);
                            }
                        }
                        return;
                    }
                }
            }
        }
    }

    private static void tryTransformApple(ServerLevel level, BlockPos pos, BlockState scaryState) {
        BuiltInRegistries.BLOCK.getOptional(
                        ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "apple_leaves"))
                .or(() -> BuiltInRegistries.BLOCK.getOptional(
                        ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "leaves_apple")))
                .ifPresent(apple -> {
                    BlockState next = apple.defaultBlockState();
                    if (next.hasProperty(DISTANCE)) {
                        next = next.setValue(DISTANCE, scaryState.getValue(DISTANCE));
                    }
                    if (next.hasProperty(PERSISTENT)) {
                        next = next.setValue(PERSISTENT, scaryState.getValue(PERSISTENT));
                    }
                    if (next.hasProperty(WATERLOGGED)) {
                        next = next.setValue(WATERLOGGED, scaryState.getValue(WATERLOGGED));
                    }
                    level.setBlock(pos, next, 3);
                });
    }

    private static boolean isLeafSupport(BlockState state, BlockGetter level, BlockPos pos) {
        Block b = state.getBlock();
        return state.is(BlockTags.LOGS)
                || b instanceof BlockCrystalTreeLog
                || b instanceof BlockSkyTreeLog
                || b instanceof BlockDuplicatorLog;
    }

    private List<ItemStack> rollFruit(RandomSource random) {
        List<ItemStack> drops = new ArrayList<>();
        String path = BuiltInRegistries.BLOCK.getKey(this).getPath();
        // Gold: 1/25 cherry or peach; scary leaves drop nothing extra
        // Gold unlocalized names: cherries / peach
        if (path.contains("cherry") && random.nextInt(25) == 1) {
            BuiltInRegistries.ITEM.getOptional(
                            ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "cherries"))
                    .ifPresent(item -> drops.add(new ItemStack(item)));
        } else if (path.contains("peach") && random.nextInt(25) == 1) {
            BuiltInRegistries.ITEM.getOptional(
                            ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "peach"))
                    .ifPresent(item -> drops.add(new ItemStack(item)));
        }
        return drops;
    }
}
