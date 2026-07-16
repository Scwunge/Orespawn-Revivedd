package danger.orespawn.blocks;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.HalfTransparentBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;

/**
 * Gold {@code CrystalGrass} — solid walkable ground with crystal-fleck textures that use
 * alpha (looks glassy/translucent, not flat solid color).
 * <p>
 * Gold: {@code Material.ground}, hardness 0.6 / resist 2.0; {@code isOpaqueCube} only true
 * in the crystal dimension. Port uses {@link HalfTransparentBlock} + translucent render
 * layer so gold PNGs with transparent flecks display correctly (filling alpha made
 * solid-color “plastic” grass).
 */
public class BlockCrystalGrass extends HalfTransparentBlock {
    public static final MapCodec<BlockCrystalGrass> CODEC = simpleCodec(BlockCrystalGrass::new);

    public BlockCrystalGrass(BlockBehaviour.Properties properties) {
        super(properties);
    }

    public static BlockBehaviour.Properties defaultProps() {
        return BlockBehaviour.Properties.of()
                .mapColor(MapColor.GRASS)
                .strength(0.6f, 2.0f)
                .sound(SoundType.GRASS)
                // glass-like light through flecks (gold non-opaque outside crystal dim)
                .noOcclusion()
                .isViewBlocking((s, g, p) -> false)
                .isSuffocating((s, g, p) -> false);
    }

    @Override
    protected MapCodec<? extends HalfTransparentBlock> codec() {
        return CODEC;
    }

    @Override
    protected boolean skipRendering(BlockState state, BlockState adjacent, Direction side) {
        return adjacent.is(this) || super.skipRendering(state, adjacent, side);
    }

    @Override
    protected float getShadeBrightness(BlockState state, BlockGetter level, BlockPos pos) {
        return 1.0F;
    }

    @Override
    protected boolean propagatesSkylightDown(BlockState state, BlockGetter level, BlockPos pos) {
        return true;
    }
}
