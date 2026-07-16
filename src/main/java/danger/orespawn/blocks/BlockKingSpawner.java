package danger.orespawn.blocks;

import danger.orespawn.entity.TheKing;
import danger.orespawn.init.ModEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * Gold {@code KingSpawnerBlock} / The King egg seed (unlocalized {@code kingspawner}).
 * <p>
 * <b>Does not</b> auto-spawn on place or random/scheduled tick. The player must
 * <b>punch/break</b> the egg to hatch {@link TheKing} at y+8 (guard mode 1).
 * Client: occasional firework particles.
 */
public class BlockKingSpawner extends Block {
    private static final VoxelShape SHAPE = Block.box(5.0, 0.0, 5.0, 11.0, 16.0, 11.0);

    /** Gold {@code OreSpawnMain.TheKingEnable}. Default 1 (spawn). */
    public static int TheKingEnable = 1;

    /** Guards recursive spawn when clearing after hatch. */
    private static final ThreadLocal<Boolean> CLEARING = ThreadLocal.withInitial(() -> false);

    public BlockKingSpawner(BlockBehaviour.Properties properties) {
        super(properties);
    }

    public static BlockBehaviour.Properties defaultProps() {
        return BlockBehaviour.Properties.of()
                .mapColor(MapColor.COLOR_YELLOW)
                .noCollission()
                // no randomTicks — must not hatch on its own
                .instabreak()
                .sound(SoundType.GRASS)
                .lightLevel(s -> 13)
                .pushReaction(PushReaction.DESTROY);
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        BlockPos below = pos.below();
        return level.getBlockState(below).isFaceSturdy(level, below, Direction.UP);
    }

    /**
     * Gold {@code breakBlock} path: hatching is tied to breaking the seed.
     * Punch / mining / explosion removes the block → spawn here.
     * No scheduled tick / random tick / neighbor auto-hatch.
     */
    @Override
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        if (!level.isClientSide
                && !CLEARING.get()
                && !state.is(newState.getBlock())
                && level instanceof ServerLevel server) {
            hatch(server, pos);
        }
        super.onRemove(state, level, pos, newState, movedByPiston);
    }

    /**
     * Spawn The King at (x, y+8, z) if enabled. Only called from break/{@link #onRemove}
     * — never from place or random/scheduled ticks.
     */
    public static void hatch(ServerLevel level, BlockPos pos) {
        if (level.isClientSide || CLEARING.get()) {
            return;
        }
        if (TheKingEnable == 0) {
            return;
        }
        CLEARING.set(true);
        try {
            spawnTheKing(level, pos.getX() + 0.5, pos.getY() + 8.0, pos.getZ() + 0.5);
            // Gold always cleared egg + block above
            if (level.getBlockState(pos).getBlock() instanceof BlockKingSpawner) {
                level.setBlock(pos, Blocks.AIR.defaultBlockState(), Block.UPDATE_ALL);
            }
            level.setBlock(pos.above(), Blocks.AIR.defaultBlockState(), Block.UPDATE_ALL);
        } finally {
            CLEARING.set(false);
        }
    }

    /** Gold {@code KingSpawnerBlock.spawnTheKing}. */
    public static Entity spawnTheKing(ServerLevel level, double x, double y, double z) {
        Entity entity = ModEntities.THE_KING.get().create(level);
        if (entity == null) {
            return null;
        }
        entity.moveTo(x, y, z, level.random.nextFloat() * 360.0F, 0.0F);
        if (entity instanceof Mob mob) {
            mob.finalizeSpawn(
                    level, level.getCurrentDifficultyAt(mob.blockPosition()), MobSpawnType.MOB_SUMMONED, null);
            mob.playAmbientSound();
        }
        if (entity instanceof TheKing king) {
            king.setGuardMode(1);
        }
        level.addFreshEntity(entity);
        return entity;
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (random.nextInt(20) == 1) {
            for (int j1 = 0; j1 < 20; j1++) {
                level.addParticle(
                        ParticleTypes.FIREWORK,
                        pos.getX() + random.nextFloat(),
                        pos.getY() + random.nextFloat(),
                        pos.getZ() + random.nextFloat(),
                        0.0,
                        0.0,
                        0.0);
            }
        }
    }
}
