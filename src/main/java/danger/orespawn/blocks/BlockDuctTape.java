package danger.orespawn.blocks;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * Gold {@code BlockDuctTape} — cake-shaped repair block (cloth material).
 * <p>
 * Gold metadata bites {@code 0–5} (6 uses). Right-click / left-click while holding a
 * <b>single</b> damaged damageable item repairs {@code maxDamage / 6} (min 1) and
 * advances bites; last bite removes the block. Does not restore hunger.
 * <p>
 * Registry name gold: {@code ducttape}. Place via {@code ItemDuctTape} (stack 1).
 */
public class BlockDuctTape extends Block {
    public static final MapCodec<BlockDuctTape> CODEC = simpleCodec(BlockDuctTape::new);

    /** Gold removes when meta would become {@code >= 6}. */
    public static final int MAX_BITES = 5;
    public static final IntegerProperty BITES = IntegerProperty.create("bites", 0, MAX_BITES);

    /** Same footprint as gold pizza / cake slices, height 4 px. */
    private static final VoxelShape[] SHAPE_BY_BITE = new VoxelShape[] {
            Block.box(1.0, 0.0, 1.0, 15.0, 4.0, 15.0),
            Block.box(3.0, 0.0, 1.0, 15.0, 4.0, 15.0),
            Block.box(5.0, 0.0, 1.0, 15.0, 4.0, 15.0),
            Block.box(7.0, 0.0, 1.0, 15.0, 4.0, 15.0),
            Block.box(9.0, 0.0, 1.0, 15.0, 4.0, 15.0),
            Block.box(11.0, 0.0, 1.0, 15.0, 4.0, 15.0),
    };

    public BlockDuctTape(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(BITES, 0));
    }

    /** Gold: material cloth, non-opaque, no drops. */
    public static BlockBehaviour.Properties defaultProps() {
        return BlockBehaviour.Properties.of()
                .mapColor(MapColor.COLOR_GRAY)
                .forceSolidOn()
                .strength(0.5F)
                .sound(SoundType.WOOL)
                .pushReaction(PushReaction.DESTROY)
                .noOcclusion()
                .noLootTable()
                .isViewBlocking((s, g, p) -> false)
                .isSuffocating((s, g, p) -> false);
    }

    @Override
    public MapCodec<BlockDuctTape> codec() {
        return CODEC;
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE_BY_BITE[state.getValue(BITES)];
    }

    @Override
    protected ItemInteractionResult useItemOn(
            ItemStack stack,
            BlockState state,
            Level level,
            BlockPos pos,
            Player player,
            InteractionHand hand,
            BlockHitResult hit) {
        if (tryRepair(level, pos, state, player, stack)) {
            return ItemInteractionResult.sidedSuccess(level.isClientSide);
        }
        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    @Override
    protected InteractionResult useWithoutItem(
            BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
        ItemStack held = player.getMainHandItem();
        if (tryRepair(level, pos, state, player, held)) {
            return InteractionResult.sidedSuccess(level.isClientSide);
        }
        return InteractionResult.PASS;
    }

    /** Gold {@code onBlockClicked} — left-click also consumes a tape slice if repair works. */
    @Override
    protected void attack(BlockState state, Level level, BlockPos pos, Player player) {
        if (!level.isClientSide) {
            tryRepair(level, pos, state, player, player.getMainHandItem());
        }
    }

    /**
     * Gold {@code eatDuctTapeSlice}:
     * held stack count == 1, maxDamage &gt; 0, current damage &gt; 0 →
     * reduce damage by {@code max(1, maxDamage/6)}, advance bites.
     *
     * @return true if a slice was used
     */
    public static boolean tryRepair(
            LevelAccessor level, BlockPos pos, BlockState state, Player player, ItemStack stack) {
        if (player == null || stack.isEmpty() || stack.getCount() != 1) {
            return false;
        }
        if (!stack.isDamageableItem()) {
            return false;
        }
        int maxDamage = stack.getMaxDamage();
        if (maxDamage <= 0) {
            return false;
        }
        int damage = stack.getDamageValue();
        if (damage <= 0) {
            return false;
        }

        int repair = maxDamage / 6;
        if (repair < 1) {
            repair = 1;
        }
        stack.setDamageValue(Math.max(0, damage - repair));

        if (level.isClientSide()) {
            return true;
        }

        int bites = state.getValue(BITES);
        if (bites < MAX_BITES) {
            level.setBlock(pos, state.setValue(BITES, bites + 1), Block.UPDATE_ALL);
        } else {
            level.removeBlock(pos, false);
        }
        return true;
    }

    @Override
    protected BlockState updateShape(
            BlockState state,
            Direction direction,
            BlockState neighborState,
            LevelAccessor level,
            BlockPos pos,
            BlockPos neighborPos) {
        return direction == Direction.DOWN && !state.canSurvive(level, pos)
                ? Blocks.AIR.defaultBlockState()
                : super.updateShape(state, direction, neighborState, level, pos, neighborPos);
    }

    /** Gold: solid material below ({@code isSolid}). */
    @Override
    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        BlockPos below = pos.below();
        return level.getBlockState(below).isFaceSturdy(level, below, Direction.UP);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(BITES);
    }

    /** Gold drops nothing; pick block returns duct tape item (via BlockItem mapping). */
    @Override
    public ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state) {
        return new ItemStack(this);
    }

    @Override
    protected boolean isPathfindable(BlockState state, PathComputationType type) {
        return false;
    }
}
