package danger.orespawn.blocks;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;

/**
 * Gold {@code BlockCrystalTreeLog} ({@code crystaltreelog}) — pillar log, non-opaque,
 * sustains leaves / counts as wood.
 */
public class BlockCrystalTreeLog extends RotatedPillarBlock {
    public static final MapCodec<BlockCrystalTreeLog> CODEC = simpleCodec(BlockCrystalTreeLog::new);

    public BlockCrystalTreeLog(BlockBehaviour.Properties properties) {
        super(properties);
    }

    /** Gold: hardness 0.2, wood sound; isOpaqueCube/renderAsNormalBlock false. */
    public static BlockBehaviour.Properties defaultProps() {
        return BlockBehaviour.Properties.of()
                .mapColor(MapColor.COLOR_PURPLE)
                .strength(0.2f)
                .noOcclusion()
                .isViewBlocking((s, g, p) -> false)
                .isSuffocating((s, g, p) -> false)
                .sound(SoundType.WOOD);
    }

    @Override
    public MapCodec<? extends RotatedPillarBlock> codec() {
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
}
