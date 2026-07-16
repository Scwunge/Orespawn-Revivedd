package danger.orespawn.blocks;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
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
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * Gold {@code BlockPizza} — cake-like edible block.
 * <p>
 * Gold metadata bites {@code 0–5} (6 slices). Each eat restores hunger
 * {@code (4, 0.2F)} when the player can eat; last bite removes the block.
 * Hitbox shrinks from the west as bites increase (height 4/16 like gold
 * {@code 0.25F}, not full cake height). Right-click and left-click both eat.
 * <p>
 * Registry name gold: {@code pizza}. Place via {@code ItemPizza} (stack 1).
 */
public class BlockPizza extends Block {
    public static final MapCodec<BlockPizza> CODEC = simpleCodec(BlockPizza::new);

    /** Gold removes the block when meta would become {@code >= 6} → max stored bite is 5. */
    public static final int MAX_BITES = 5;
    public static final IntegerProperty BITES = IntegerProperty.create("bites", 0, MAX_BITES);

    /** Gold food: {@code foodStats.addStats(4, 0.2F)}. */
    public static final int NUTRITION = 4;
    public static final float SATURATION_MODIFIER = 0.2F;

    /**
     * Gold bounds: {@code f1 = (1 + bites * 2) / 16}, height {@code 0.25F} (4 px).
     * Collision used {@code f2 - f} (~3 px); outline used full height 4.
     */
    private static final VoxelShape[] SHAPE_BY_BITE = new VoxelShape[] {
            Block.box(1.0, 0.0, 1.0, 15.0, 4.0, 15.0),
            Block.box(3.0, 0.0, 1.0, 15.0, 4.0, 15.0),
            Block.box(5.0, 0.0, 1.0, 15.0, 4.0, 15.0),
            Block.box(7.0, 0.0, 1.0, 15.0, 4.0, 15.0),
            Block.box(9.0, 0.0, 1.0, 15.0, 4.0, 15.0),
            Block.box(11.0, 0.0, 1.0, 15.0, 4.0, 15.0),
    };

    public BlockPizza(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(BITES, 0));
    }

    /** Gold: material cake, non-opaque, soft break, quantityDropped=0. */
    public static BlockBehaviour.Properties defaultProps() {
        return BlockBehaviour.Properties.of()
                .mapColor(MapColor.COLOR_ORANGE)
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
    public MapCodec<BlockPizza> codec() {
        return CODEC;
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE_BY_BITE[state.getValue(BITES)];
    }

    @Override
    protected InteractionResult useWithoutItem(
            BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
        if (level.isClientSide) {
            // Client predicts; server validates hunger.
            if (eatSlice(level, pos, state, player).consumesAction()) {
                return InteractionResult.SUCCESS;
            }
            if (player.getItemInHand(player.getUsedItemHand()).isEmpty()) {
                return InteractionResult.CONSUME;
            }
            return InteractionResult.PASS;
        }
        return eatSlice(level, pos, state, player);
    }

    /**
     * Gold {@code onBlockClicked} — left-click also takes a slice.
     */
    @Override
    protected void attack(BlockState state, Level level, BlockPos pos, Player player) {
        if (!level.isClientSide) {
            eatSlice(level, pos, state, player);
        }
    }

    /**
     * Gold {@code eatPizzaSlice}: if {@code canEat(false)}, add food and advance bites.
     */
    public static InteractionResult eatSlice(LevelAccessor level, BlockPos pos, BlockState state, Player player) {
        if (!player.canEat(false)) {
            return InteractionResult.PASS;
        }
        player.getFoodData().eat(NUTRITION, SATURATION_MODIFIER);
        int bites = state.getValue(BITES);
        if (bites < MAX_BITES) {
            level.setBlock(pos, state.setValue(BITES, bites + 1), Block.UPDATE_ALL);
        } else {
            level.removeBlock(pos, false);
        }
        return InteractionResult.SUCCESS;
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

    /** Gold: opaque cube below. */
    @Override
    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        return level.getBlockState(pos.below()).isSolidRender(level, pos.below());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(BITES);
    }

    /** Gold {@code quantityDropped = 0} — no survival drops. */
    @Override
    public ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state) {
        return new ItemStack(this);
    }

    @Override
    protected boolean isPathfindable(BlockState state, PathComputationType type) {
        return false;
    }
}
