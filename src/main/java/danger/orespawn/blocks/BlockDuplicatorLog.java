package danger.orespawn.blocks;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;

/**
 * Gold {@code BlockDuplicatorLog} ({@code duplicatortreelog}) — random-tick log that
 * grew the duplicator tree when {@code enableduplicatortree != 0}.
 * <p>
 * Tree growth body lives in gold {@code OreSpawnTrees.DuplicatorTree}; worldgen/config later. Class provides wood semantics + randomTicks.
 */
public class BlockDuplicatorLog extends Block {
    public static final MapCodec<BlockDuplicatorLog> CODEC = simpleCodec(BlockDuplicatorLog::new);

    /** Gold OreSpawnMain.enableduplicatortree — may be toggled from config. */
    public static boolean ENABLE_DUPLICATOR_TREE = true;

    public BlockDuplicatorLog(BlockBehaviour.Properties properties) {
        super(properties);
    }

    /** Gold: hardness 0.2, wood, randomTicks true. */
    public static BlockBehaviour.Properties defaultProps() {
        return BlockBehaviour.Properties.of()
                .mapColor(MapColor.WOOD)
                .strength(0.2f)
                .randomTicks()
                .sound(SoundType.WOOD);
    }

    @Override
    protected MapCodec<? extends Block> codec() {
        return CODEC;
    }

    @Override
    protected boolean isRandomlyTicking(BlockState state) {
        return true;
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (level.isClientSide || !ENABLE_DUPLICATOR_TREE) {
            return;
        }
        // Gold: OreSpawnMain.OreSpawnTrees.DuplicatorTree(world, x, y, z)
        // Deferred until tree generator is ported; keep tick hook for future tree growth.
    }

    @Override
    public boolean isFlammable(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return true;
    }

    @Override
    public int getFlammability(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return 5;
    }

    @Override
    public int getFireSpreadSpeed(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return 5;
    }
}
