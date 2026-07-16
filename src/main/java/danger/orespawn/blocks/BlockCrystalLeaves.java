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
 * Gold {@code BlockCrystalLeaves} — crystaltreeleaves / 2 / 3.
 * Proper leaves props + noOcclusion (crystal translucency).
 * Drop rates (gold): 1/100 crystal apple, 1/50 matching sapling.
 */
public class BlockCrystalLeaves extends LeavesBlock {
    public static final MapCodec<BlockCrystalLeaves> CODEC = simpleCodec(BlockCrystalLeaves::new);

    public BlockCrystalLeaves(BlockBehaviour.Properties properties) {
        super(properties);
    }

    /** Gold hardness 0.2, grass sound, light opacity 1 → translucent leaves. */
    public static BlockBehaviour.Properties defaultProps() {
        return BlockBehaviour.Properties.of()
                .mapColor(MapColor.COLOR_PURPLE)
                .strength(0.2f)
                .randomTicks()
                .noOcclusion()
                .isSuffocating((s, g, p) -> false)
                .isViewBlocking((s, g, p) -> false)
                .isRedstoneConductor((s, g, p) -> false)
                .sound(SoundType.GRASS)
                .pushReaction(PushReaction.DESTROY);
    }

    /** Gold crystaltreeleaves2/3 hardness 0.25. */
    public static BlockBehaviour.Properties defaultPropsHarder() {
        return defaultProps().strength(0.25f);
    }

    @Override
    public MapCodec<? extends LeavesBlock> codec() {
        return CODEC;
    }

    /** Gold always ticked for fruit drops, not only at decay distance. */
    @Override
    protected boolean isRandomlyTicking(BlockState state) {
        return true;
    }

    @Override
    protected float getShadeBrightness(BlockState state, BlockGetter level, BlockPos pos) {
        return 1.0F;
    }

    @Override
    protected boolean propagatesSkylightDown(BlockState state, BlockGetter level, BlockPos pos) {
        return true;
    }

    @Override
    protected List<ItemStack> getDrops(BlockState state, LootParams.Builder params) {
        RandomSource random = params.getLevel().getRandom();
        return rollFruit(random);
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
                        return;
                    }
                }
            }
        }
    }

    private static boolean isLeafSupport(BlockState state, BlockGetter level, BlockPos pos) {
        // 1.21: leaf distance uses #logs; exclusive logs should be in the #logs tag
        Block b = state.getBlock();
        return state.is(BlockTags.LOGS)
                || b instanceof BlockCrystalTreeLog
                || b instanceof BlockSkyTreeLog
                || b instanceof BlockDuplicatorLog;
    }

    private List<ItemStack> rollFruit(RandomSource random) {
        List<ItemStack> drops = new ArrayList<>();
        if (random.nextInt(100) == 1) {
            itemStack("crystal_apple").ifPresent(drops::add);
        }
        if (random.nextInt(50) == 1) {
            String path = BuiltInRegistries.BLOCK.getKey(this).getPath();
            String sapling = switch (path) {
                case "crystal_tree_leaves_2", "crystal_leaves_2", "crystaltreeleaves2" -> "crystal_sapling_2";
                case "crystal_tree_leaves_3", "crystal_leaves_3", "crystaltreeleaves3" -> "crystal_sapling_3";
                default -> "crystal_sapling";
            };
            if (itemStack(sapling).isEmpty()) {
                itemStack("crystalsapling").ifPresent(drops::add);
            } else {
                itemStack(sapling).ifPresent(drops::add);
            }
        }
        return drops;
    }

    private static java.util.Optional<ItemStack> itemStack(String path) {
        ResourceLocation id = ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, path);
        return BuiltInRegistries.ITEM.getOptional(id).map(ItemStack::new);
    }
}
