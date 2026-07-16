package danger.orespawn.blocks;

import com.mojang.serialization.MapCodec;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
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
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * Gold {@code BlockCrystalTorch} — torch with light {@code 0.99F} (~15) and
 * fireworksSpark + flame particles. Can attach to crystal terrain (non-solid in gold).
 */
public class BlockCrystalTorch extends Block {
    public static final MapCodec<BlockCrystalTorch> CODEC = simpleCodec(BlockCrystalTorch::new);
    /** Matches extreme_torch assets: facing=up|north|south|east|west */
    public static final DirectionProperty FACING = DirectionProperty.create(
            "facing", d -> d != Direction.DOWN);

    private static final VoxelShape FLOOR_AABB = Block.box(6.0, 0.0, 6.0, 10.0, 10.0, 10.0);
    private static final VoxelShape NORTH_AABB = Block.box(5.5, 3.0, 11.0, 10.5, 13.0, 16.0);
    private static final VoxelShape SOUTH_AABB = Block.box(5.5, 3.0, 0.0, 10.5, 13.0, 5.0);
    private static final VoxelShape WEST_AABB = Block.box(11.0, 3.0, 5.5, 16.0, 13.0, 10.5);
    private static final VoxelShape EAST_AABB = Block.box(0.0, 3.0, 5.5, 5.0, 13.0, 10.5);

    public BlockCrystalTorch(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.UP));
    }

    /** Gold: setLightLevel(0.99F) → full bright. */
    public static BlockBehaviour.Properties defaultProps() {
        return BlockBehaviour.Properties.of()
                .mapColor(MapColor.COLOR_PINK)
                .noCollission()
                .instabreak()
                .lightLevel(s -> 15)
                .sound(SoundType.WOOD)
                .pushReaction(PushReaction.DESTROY);
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

    /** Gold: solid side OR crystal block (CrystalStone/Grass/Log/Planks). */
    private static boolean isCrystalSupport(BlockState state) {
        Block b = state.getBlock();
        return b instanceof BlockCrystal
                || b instanceof BlockCrystalTreeLog
                || b instanceof BlockCrystalOre;
    }

    private static boolean canAttach(LevelReader level, BlockPos attachPos, Direction towardTorch) {
        BlockState attach = level.getBlockState(attachPos);
        if (isCrystalSupport(attach)) {
            return true;
        }
        return attach.isFaceSturdy(level, attachPos, towardTorch);
    }

    private static boolean canPlaceOnTop(LevelReader level, BlockPos below) {
        BlockState state = level.getBlockState(below);
        if (isCrystalSupport(state)) {
            return true;
        }
        return canSupportCenter(level, below, Direction.UP);
    }

    @Override
    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        Direction facing = state.getValue(FACING);
        if (facing == Direction.UP) {
            return canPlaceOnTop(level, pos.below());
        }
        return canAttach(level, pos.relative(facing.getOpposite()), facing);
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
        // Gold: only 1/4 ticks
        if (random.nextInt(4) != 1) {
            return;
        }
        Direction facing = state.getValue(FACING);
        double x = pos.getX() + 0.5;
        double y = pos.getY() + 0.7;
        double z = pos.getZ() + 0.5;
        double yOff = 0.213;
        double hOff = 0.271;

        if (facing == Direction.EAST) {
            spawnParticles(level, random, x - hOff, y + yOff, z);
        } else if (facing == Direction.WEST) {
            spawnParticles(level, random, x + hOff, y + yOff, z);
        } else if (facing == Direction.SOUTH) {
            spawnParticles(level, random, x, y + yOff, z - hOff);
        } else if (facing == Direction.NORTH) {
            spawnParticles(level, random, x, y + yOff, z + hOff);
        } else {
            spawnParticles(level, random, x, y, z);
        }
    }

    private static void spawnParticles(Level level, RandomSource random, double x, double y, double z) {
        // gold: fireworksSpark + flame with small velocity jitter
        level.addParticle(
                ParticleTypes.FIREWORK,
                x, y, z,
                (random.nextFloat() - random.nextFloat()) / 8.0,
                random.nextFloat() / 8.0,
                (random.nextFloat() - random.nextFloat()) / 8.0);
        level.addParticle(
                ParticleTypes.FLAME,
                x, y, z,
                (random.nextFloat() - random.nextFloat()) / 60.0,
                random.nextFloat() / 10.0,
                (random.nextFloat() - random.nextFloat()) / 60.0);
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
