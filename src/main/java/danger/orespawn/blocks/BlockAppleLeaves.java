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
import net.minecraft.world.item.Items;
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
 * Gold {@code BlockAppleLeaves} ({@code leaves_apple}).
 * Drops: apple 1/25, golden 1/500, enchanted 1/1000, magic apple 1/10000.
 * Night in crystal dim → transforms to scary leaves (gold DimensionID4).
 */
public class BlockAppleLeaves extends LeavesBlock {
    public static final MapCodec<BlockAppleLeaves> CODEC = simpleCodec(BlockAppleLeaves::new);

    public BlockAppleLeaves(BlockBehaviour.Properties properties) {
        super(properties);
    }

    /** Gold hardness 0.2, leaves sound, light opacity 1. */
    public static BlockBehaviour.Properties defaultProps() {
        return BlockBehaviour.Properties.of()
                .mapColor(MapColor.PLANT)
                .strength(0.2f)
                .randomTicks()
                .noOcclusion()
                .isSuffocating((s, g, p) -> false)
                .isViewBlocking((s, g, p) -> false)
                .isRedstoneConductor((s, g, p) -> false)
                .sound(SoundType.GRASS)
                .pushReaction(PushReaction.DESTROY);
    }

    @Override
    public MapCodec<? extends LeavesBlock> codec() {
        return CODEC;
    }

    /** Gold always ticked (fruit + night→scary transform). */
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
        int chance = 20;
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
                        BlockPos below = pos.below();
                        if (level.getBlockState(below).isAir() && random.nextInt(chance) == 3) {
                            for (ItemStack stack : rollFruit(random)) {
                                Block.popResource(level, below, stack);
                            }
                        }
                        // Gold: night (t > 12000) in crystal dim → scary leaves
                        long t = level.getDayTime() % 24000L;
                        if (t > 12000L) {
                            tryTransformScary(level, pos, state);
                        }
                        return;
                    }
                }
            }
        }
    }

    private static void tryTransformScary(ServerLevel level, BlockPos pos, BlockState appleState) {
        BuiltInRegistries.BLOCK.getOptional(
                        ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "scary_leaves"))
                .or(() -> BuiltInRegistries.BLOCK.getOptional(
                        ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "leaves_scary")))
                .ifPresent(scary -> {
                    BlockState next = scary.defaultBlockState();
                    if (next.hasProperty(DISTANCE)) {
                        next = next.setValue(DISTANCE, appleState.getValue(DISTANCE));
                    }
                    if (next.hasProperty(PERSISTENT)) {
                        next = next.setValue(PERSISTENT, appleState.getValue(PERSISTENT));
                    }
                    if (next.hasProperty(WATERLOGGED)) {
                        next = next.setValue(WATERLOGGED, appleState.getValue(WATERLOGGED));
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

    private static List<ItemStack> rollFruit(RandomSource random) {
        List<ItemStack> drops = new ArrayList<>();
        if (random.nextInt(25) == 1) {
            drops.add(new ItemStack(Items.APPLE));
        }
        if (random.nextInt(500) == 2) {
            drops.add(new ItemStack(Items.GOLDEN_APPLE));
        }
        if (random.nextInt(1000) == 3) {
            drops.add(new ItemStack(Items.ENCHANTED_GOLDEN_APPLE));
        }
        if (random.nextInt(10000) == 4) {
            BuiltInRegistries.ITEM.getOptional(
                            ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "magic_apple"))
                    .ifPresent(item -> drops.add(new ItemStack(item)));
        }
        return drops;
    }
}
