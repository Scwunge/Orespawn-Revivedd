package danger.orespawn.blocks;

import com.mojang.serialization.MapCodec;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * Gold {@code BlockExtremeTorch} — torch with light level 15 (full bright).
 * Uses {@code facing} (up + horizontal) to match gold blockstates.
 */
public class BlockExtremeTorch extends Block {
    public static final MapCodec<BlockExtremeTorch> CODEC = simpleCodec(BlockExtremeTorch::new);
    /** Matches gold assets: facing=up|north|south|east|west */
    public static final DirectionProperty FACING = DirectionProperty.create(
            "facing", d -> d != Direction.DOWN);

    private static final VoxelShape FLOOR_AABB = Block.box(6.0, 0.0, 6.0, 10.0, 10.0, 10.0);
    private static final VoxelShape NORTH_AABB = Block.box(5.5, 3.0, 11.0, 10.5, 13.0, 16.0);
    private static final VoxelShape SOUTH_AABB = Block.box(5.5, 3.0, 0.0, 10.5, 13.0, 5.0);
    private static final VoxelShape WEST_AABB = Block.box(11.0, 3.0, 5.5, 16.0, 13.0, 10.5);
    private static final VoxelShape EAST_AABB = Block.box(0.0, 3.0, 5.5, 5.0, 13.0, 10.5);

    public BlockExtremeTorch(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.UP));
    }

    @Override
    protected MapCodec<? extends Block> codec() {
        return CODEC;
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return switch (state.getValue(FACING)) {
            case NORTH -> NORTH_AABB;
            case SOUTH -> SOUTH_AABB;
            case WEST -> WEST_AABB;
            case EAST -> EAST_AABB;
            default -> FLOOR_AABB;
        };
    }

    @Override
    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        Direction facing = state.getValue(FACING);
        if (facing == Direction.UP) {
            return canSupportCenter(level, pos.below(), Direction.UP);
        }
        BlockPos attach = pos.relative(facing.getOpposite());
        return level.getBlockState(attach).isFaceSturdy(level, attach, facing);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        LevelReader level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        Direction clicked = context.getClickedFace();

        if (clicked.getAxis().isHorizontal()) {
            BlockState wall = this.defaultBlockState().setValue(FACING, clicked);
            if (wall.canSurvive(level, pos)) {
                return wall;
            }
        }

        for (Direction dir : context.getNearestLookingDirections()) {
            if (dir.getAxis().isHorizontal()) {
                BlockState wall = this.defaultBlockState().setValue(FACING, dir.getOpposite());
                if (wall.canSurvive(level, pos)) {
                    return wall;
                }
            }
        }

        BlockState floor = this.defaultBlockState().setValue(FACING, Direction.UP);
        return floor.canSurvive(level, pos) ? floor : null;
    }

    @Override
    protected BlockState updateShape(
            BlockState state,
            Direction direction,
            BlockState neighborState,
            LevelAccessor level,
            BlockPos pos,
            BlockPos neighborPos) {
        return state.canSurvive(level, pos) ? state : Blocks.AIR.defaultBlockState();
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        Direction facing = state.getValue(FACING);
        double x = pos.getX() + 0.5;
        double y = pos.getY() + 0.7;
        double z = pos.getZ() + 0.5;
        double yOff = 0.213;
        double hOff = 0.271;

        if (facing == Direction.EAST) {
            spawnParticles(level, x - hOff, y + yOff, z);
        } else if (facing == Direction.WEST) {
            spawnParticles(level, x + hOff, y + yOff, z);
        } else if (facing == Direction.SOUTH) {
            spawnParticles(level, x, y + yOff, z - hOff);
        } else if (facing == Direction.NORTH) {
            spawnParticles(level, x, y + yOff, z + hOff);
        } else {
            spawnParticles(level, x, y, z);
        }
    }

    private static void spawnParticles(Level level, double x, double y, double z) {
        // gold: SMOKE_NORMAL + FLAME + REDSTONE
        level.addParticle(ParticleTypes.SMOKE, x, y, z, 0.0, 0.0, 0.0);
        level.addParticle(ParticleTypes.FLAME, x, y, z, 0.0, 0.0, 0.0);
        level.addParticle(DustParticleOptions.REDSTONE, x, y, z, 0.0, 0.0, 0.0);
    }

    @Override
    protected BlockState rotate(BlockState state, Rotation rotation) {
        Direction facing = state.getValue(FACING);
        if (facing == Direction.UP) {
            return state;
        }
        return state.setValue(FACING, rotation.rotate(facing));
    }

    @Override
    protected BlockState mirror(BlockState state, Mirror mirror) {
        Direction facing = state.getValue(FACING);
        if (facing == Direction.UP) {
            return state;
        }
        return state.rotate(mirror.getRotation(facing));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }
}
