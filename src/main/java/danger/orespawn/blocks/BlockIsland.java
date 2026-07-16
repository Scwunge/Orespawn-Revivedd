package danger.orespawn.blocks;

import danger.orespawn.entity.Island;
import danger.orespawn.entity.IslandToo;
import danger.orespawn.init.ModEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * Gold {@code IslandBlock} (extends BlockReed) — seed for floating islands.
 * <p>
 * Gold {@code OreSpawnWorld.addIslands} places this on grass in Dimension-Islands.
 * On random tick: spawn 1–3 {@link Island}/{@link IslandToo} high in the air, then remove self.
 * <p>
 * Gold size factor default 2 → max height roll 55 (12 + nextInt(55)).
 */
public class BlockIsland extends Block {
    private static final VoxelShape SHAPE = Block.box(5.0, 0.0, 5.0, 11.0, 16.0, 11.0);

    /** Gold OreSpawnMain.IslandSizeFactor default 2. */
    public static int IslandSizeFactor = 2;

    public BlockIsland(BlockBehaviour.Properties properties) {
        super(properties);
    }

    public static BlockBehaviour.Properties defaultProps() {
        return BlockBehaviour.Properties.of()
                .mapColor(net.minecraft.world.level.material.MapColor.PLANT)
                .noCollission()
                .randomTicks()
                .instabreak()
                .sound(SoundType.GRASS)
                .lightLevel(s -> 13) // gold setLightLevel(0.9F) ≈ 13/15
                .pushReaction(net.minecraft.world.level.material.PushReaction.DESTROY);
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
    protected boolean isRandomlyTicking(BlockState state) {
        return true;
    }

    @Override
    protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
        super.onPlace(state, level, pos, oldState, movedByPiston);
        // Modern random ticks are slow; schedule a first tick soon so islands appear after gen
        if (!level.isClientSide && level instanceof ServerLevel server) {
            server.scheduleTick(pos, this, 40 + level.random.nextInt(80));
        }
    }

    @Override
    protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        this.trySpawnIslands(level, pos, random);
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        this.trySpawnIslands(level, pos, random);
    }

    /**
     * Gold {@code IslandBlock.updateTick}: n = 1+r3 attempts; for each, height = 12+r(m);
     * if air cube clear → 1/25 Island else IslandToo; then remove seed (+ block above).
     */
    private void trySpawnIslands(ServerLevel level, BlockPos pos, RandomSource random) {
        if (level.isClientSide) {
            return;
        }

        int n = 1 + random.nextInt(3);
        int m = 64;
        if (IslandSizeFactor == 2) {
            m = 55;
        } else if (IslandSizeFactor == 1) {
            m = 45;
        }

        for (int i = 0; i < n; i++) {
            int height = 12 + random.nextInt(Math.max(1, m));
            if (!isAirCubeClear(level, pos.getX(), pos.getY() + height, pos.getZ())) {
                continue;
            }
            boolean light = random.nextInt(25) == 1;
            spawnIslandEntity(level, light, pos.getX() + 0.5, pos.getY() + height, pos.getZ() + 0.5, random);
        }

        // gold: set air at seed and seed+1
        level.setBlock(pos, Blocks.AIR.defaultBlockState(), 2);
        level.setBlock(pos.above(), Blocks.AIR.defaultBlockState(), 2);
    }

    private static boolean isAirCubeClear(ServerLevel level, int x, int y, int z) {
        for (int k = -10; k <= 10; k++) {
            for (int j = -10; j <= 10; j++) {
                if (!level.getBlockState(new BlockPos(x + j, y, z + k)).isAir()) {
                    return false;
                }
            }
        }
        return true;
    }

    public static void spawnIslandEntity(
            ServerLevel level, boolean lightIsland, double x, double y, double z, RandomSource random) {
        Entity entity = lightIsland
                ? ModEntities.ISLAND.get().create(level)
                : ModEntities.ISLAND_TOO.get().create(level);
        if (entity == null) {
            return;
        }
        entity.moveTo(x, y, z, random.nextFloat() * 360.0F, 0.0F);
        if (entity instanceof Mob mob) {
            mob.finalizeSpawn(level, level.getCurrentDifficultyAt(mob.blockPosition()), MobSpawnType.CHUNK_GENERATION, null);
        }
        level.addFreshEntity(entity);
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        // gold: occasional happyVillager particles
        if (random.nextInt(20) == 1) {
            for (int j1 = 0; j1 < 20; j1++) {
                level.addParticle(
                        ParticleTypes.HAPPY_VILLAGER,
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
