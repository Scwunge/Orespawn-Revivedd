package danger.orespawn.blocks;

import com.mojang.serialization.MapCodec;
import danger.orespawn.entity.Ant;
import danger.orespawn.entity.PurplePower;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Creeper;
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
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * Gold {@code CreeperRepellent} (extends BlockTorch) — light 0.8F (~12).
 * <p>
 * Client: smoke + flame + reddust particles (torch-like).
 * Server scheduled tick every 10: push Creepers, Ants (all subtypes), and PurplePower
 * (except purple type 10) within AABB 40×20×40 centered on the block.
 */
public class BlockCreeperRepellent extends Block {
    public static final MapCodec<BlockCreeperRepellent> CODEC = simpleCodec(BlockCreeperRepellent::new);
    /** Matches extreme_torch assets: facing=up|north|south|east|west */
    public static final DirectionProperty FACING = DirectionProperty.create(
            "facing", d -> d != Direction.DOWN);

    private static final int TICK_INTERVAL = 10;

    private static final VoxelShape FLOOR_AABB = Block.box(6.0, 0.0, 6.0, 10.0, 10.0, 10.0);
    private static final VoxelShape NORTH_AABB = Block.box(5.5, 3.0, 11.0, 10.5, 13.0, 16.0);
    private static final VoxelShape SOUTH_AABB = Block.box(5.5, 3.0, 0.0, 10.5, 13.0, 5.0);
    private static final VoxelShape WEST_AABB = Block.box(11.0, 3.0, 5.5, 16.0, 13.0, 10.5);
    private static final VoxelShape EAST_AABB = Block.box(0.0, 3.0, 5.5, 5.0, 13.0, 10.5);

    public BlockCreeperRepellent(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.UP));
    }

    /** Gold: {@code setLightLevel(0.8F)} → ~12/15. */
    public static BlockBehaviour.Properties defaultProps() {
        return BlockBehaviour.Properties.of()
                .mapColor(MapColor.COLOR_LIGHT_GREEN)
                .noCollission()
                .instabreak()
                .lightLevel(s -> 12)
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
        if (!state.canSurvive(level, pos)) {
            return Blocks.AIR.defaultBlockState();
        }
        // gold neighbor change: reschedule tick
        if (level instanceof ServerLevel server) {
            server.scheduleTick(pos, this, TICK_INTERVAL);
        }
        return state;
    }

    @Override
    protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
        super.onPlace(state, level, pos, oldState, movedByPiston);
        if (!level.isClientSide && level instanceof ServerLevel server) {
            server.scheduleTick(pos, this, TICK_INTERVAL);
        }
    }

    @Override
    protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        this.findSomethingToRepel(level, pos);
        level.scheduleTick(pos, this, TICK_INTERVAL);
    }

    /**
     * Gold AABB: (x-20, y-10, z-20) → (x+20, y+10, z+20).
     * Pushes Creeper, EntityAnt (all ant types), PurplePower (not type 10).
     */
    private void findSomethingToRepel(ServerLevel level, BlockPos pos) {
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();
        AABB bb = new AABB(x - 20.0, y - 10.0, z - 20.0, x + 20.0, y + 10.0, z + 20.0);
        List<LivingEntity> entities = level.getEntitiesOfClass(LivingEntity.class, bb);

        for (LivingEntity entity : entities) {
            if (entity instanceof Creeper || entity instanceof Ant) {
                pushAway(entity, x, y, z, 0.0);
                continue;
            }
            if (entity instanceof PurplePower purple) {
                // gold skips type 10 (King stream); use continue so other targets still get pushed
                if (purple.getPurpleType() == 10) {
                    continue;
                }
                pushAway(entity, x, y, z, 0.0);
            }
        }
    }

    /**
     * Gold push: force = clamp(20 - dist, 0, 20) * 0.4; horizontal via atan2(dx, dz).
     *
     * @param yOffset gold Kraken uses 15; creeper/ant use 0
     */
    static void pushAway(LivingEntity entity, int blockX, int blockY, int blockZ, double yOffset) {
        double d1 = entity.getX() - blockX;
        double d2 = entity.getY() - yOffset - blockY;
        double d3 = entity.getZ() - blockZ;
        double f = Math.sqrt(d1 * d1 + d2 * d2 + d3 * d3);
        f = 20.0 - f;
        if (f > 20.0) {
            f = 20.0;
        }
        if (f < 0.0) {
            f = 0.0;
        }
        f *= 0.4;
        double d = Math.atan2(entity.getX() - blockX, entity.getZ() - blockZ);
        Vec3 motion = entity.getDeltaMovement();
        entity.setDeltaMovement(
                motion.x + f * Math.sin(d),
                motion.y,
                motion.z + f * Math.cos(d));
        entity.hasImpulse = true;
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        Direction facing = state.getValue(FACING);
        double px = pos.getX() + 0.5;
        double py = pos.getY() + 0.7;
        double pz = pos.getZ() + 0.5;
        // gold wall y-offset 0.413, horizontal 0.271; floor y+0.21
        double yOff = 0.413;
        double hOff = 0.271;

        if (facing == Direction.EAST) {
            spawnParticles(level, px - hOff, py + yOff, pz);
        } else if (facing == Direction.WEST) {
            spawnParticles(level, px + hOff, py + yOff, pz);
        } else if (facing == Direction.SOUTH) {
            spawnParticles(level, px, py + yOff, pz - hOff);
        } else if (facing == Direction.NORTH) {
            spawnParticles(level, px, py + yOff, pz + hOff);
        } else {
            spawnParticles(level, px, py + 0.21, pz);
        }
    }

    private static void spawnParticles(Level level, double x, double y, double z) {
        // gold: smoke + flame + reddust
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
