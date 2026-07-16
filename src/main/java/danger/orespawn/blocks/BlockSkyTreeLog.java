package danger.orespawn.blocks;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;

/**
 * Gold {@code BlockSkyTreeLog} ({@code skytreelog}) — breaking one node cascades through
 * connected sky-log voxels (gold {@code breakRecursor}, max recursion 1000).
 */
public class BlockSkyTreeLog extends Block {
    public static final MapCodec<BlockSkyTreeLog> CODEC = simpleCodec(BlockSkyTreeLog::new);

    public BlockSkyTreeLog(BlockBehaviour.Properties properties) {
        super(properties);
    }

    /** Gold: hardness 0.2, wood sound. */
    public static BlockBehaviour.Properties defaultProps() {
        return BlockBehaviour.Properties.of()
                .mapColor(MapColor.WOOD)
                .strength(0.2f)
                .sound(SoundType.WOOD);
    }

    @Override
    protected MapCodec<? extends Block> codec() {
        return CODEC;
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

    @Override
    public BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        if (!level.isClientSide) {
            boolean drop = !player.isCreative();
            // Gold breakBlock: clear self-handled by super; cascade neighbors + drop them
            breakRecursor(level, pos, pos, 0, drop);
        }
        return super.playerWillDestroy(level, pos, state, player);
    }

    /**
     * Gold {@code breakRecursor} — BFS-ish 3³ shell walk with recursion cap 1000.
     * Neighbour logs are air-set + optionally dropped.
     */
    private void breakRecursor(Level level, BlockPos cur, BlockPos from, int recursion, boolean drop) {
        if (recursion > 1000) {
            return;
        }
        int r = 1;
        for (int dx = -r; dx <= r; dx++) {
            for (int dy = -r; dy <= r; dy++) {
                for (int dz = -r; dz <= r; dz++) {
                    if (dx == 0 && dy == 0 && dz == 0) {
                        continue;
                    }
                    BlockPos n = cur.offset(dx, dy, dz);
                    if (n.equals(from)) {
                        continue;
                    }
                    // gold: after recursion>0, skip cells still inside previous shell
                    if (recursion > 0
                            && n.getX() >= from.getX() - r && n.getX() <= from.getX() + r
                            && n.getY() >= from.getY() - r && n.getY() <= from.getY() + r
                            && n.getZ() >= from.getZ() - r && n.getZ() <= from.getZ() + r) {
                        continue;
                    }
                    BlockState ns = level.getBlockState(n);
                    if (ns.is(this)) {
                        if (drop) {
                            Block.dropResources(ns, level, n);
                        }
                        level.setBlock(n, Blocks.AIR.defaultBlockState(), 2);
                        breakRecursor(level, n, cur, recursion + 1, drop);
                    }
                }
            }
        }
    }
}
