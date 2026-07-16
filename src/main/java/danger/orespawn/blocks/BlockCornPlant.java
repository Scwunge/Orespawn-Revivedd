package danger.orespawn.blocks;

import com.mojang.serialization.MapCodec;
import danger.orespawn.init.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.FarmBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * Gold {@code BlockCornPlant} — multi-stage stalk crop (stage 0–15).
 * Simplified growth: advances stage and can stack upward; drops corn at stage ≥ 3.
 * Full TileEntityPlant age/phase logic can be layered later.
 */
public class BlockCornPlant extends BushBlock {
    public static final MapCodec<BlockCornPlant> CODEC = simpleCodec(BlockCornPlant::new);
    public static final IntegerProperty STAGE = IntegerProperty.create("stage", 0, 15);
    private static final VoxelShape SHAPE = Block.box(2.0, 0.0, 2.0, 14.0, 16.0, 14.0);
    private static final int MAX_HEIGHT = 8;

    public BlockCornPlant(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(STAGE, 0));
    }

    @Override
    public MapCodec<BlockCornPlant> codec() {
        return CODEC;
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    protected VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return Shapes.empty();
    }

    @Override
    protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
        return state.is(this)
                || state.getBlock() instanceof FarmBlock
                || state.is(Blocks.GRASS_BLOCK)
                || state.is(Blocks.DIRT)
                || state.is(Blocks.COARSE_DIRT)
                || state.is(Blocks.PODZOL)
                || state.is(Blocks.ROOTED_DIRT)
                || state.is(Blocks.MUD);
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

    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (!level.isAreaLoaded(pos, 1)) {
            return;
        }
        if (!this.canSurvive(state, level, pos)) {
            level.destroyBlock(pos, true);
            return;
        }

        int stage = state.getValue(STAGE);
        boolean airAbove = level.getBlockState(pos.above()).isAir();

        int height = 0;
        for (int i = 0; i < MAX_HEIGHT && level.getBlockState(pos.below(i)).is(this); i++) {
            height++;
        }

        if (random.nextInt(3) != 0) {
            return;
        }

        if (airAbove && height < MAX_HEIGHT) {
            if (stage < 3) {
                level.setBlock(pos, state.setValue(STAGE, stage + 1), Block.UPDATE_ALL);
            } else if (stage >= 2 && random.nextInt(2) == 0) {
                level.setBlock(pos.above(), this.defaultBlockState().setValue(STAGE, 0), Block.UPDATE_ALL);
                if (stage < 15) {
                    level.setBlock(pos, state.setValue(STAGE, Math.min(15, stage + 1)), Block.UPDATE_ALL);
                }
            } else if (stage < 15) {
                level.setBlock(pos, state.setValue(STAGE, stage + 1), Block.UPDATE_ALL);
            }
        } else if (stage < 3) {
            level.setBlock(pos, state.setValue(STAGE, stage + 1), Block.UPDATE_ALL);
        } else if (stage < 15 && random.nextInt(4) == 0) {
            level.setBlock(pos, state.setValue(STAGE, stage + 1), Block.UPDATE_ALL);
        }
    }

    @Override
    public ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state) {
        return new ItemStack(this.getBaseSeedId());
    }

    protected ItemLike getBaseSeedId() {
        return ModItems.CORN.get();
    }

    /** Gold: drop corn only when stage == 3 (mature cob). */
    public boolean isMature(BlockState state) {
        return state.getValue(STAGE) >= 3;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(STAGE);
    }

    @Override
    public boolean isFlammable(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return true;
    }
}
