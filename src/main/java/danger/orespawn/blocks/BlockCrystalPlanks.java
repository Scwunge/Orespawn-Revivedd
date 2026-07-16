package danger.orespawn.blocks;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.block.HalfTransparentBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;

/**
 * Gold {@code CrystalWood} — registry name {@code crystalplanks} → modern {@code crystal_planks}.
 * <p>
 * Stats: hardness {@code 1.5F}, resistance {@code 4.0F}.
 * {@code isOpaqueCube}/{@code renderAsNormalBlock} both {@code false} → non-opaque translucent crystal wood.
 * Extends {@link BlockCrystal} so crystal-torch attach logic ({@code instanceof BlockCrystal}) still matches.
 * <p>
 * Registration: replace stub {@code CRYSTAL_PLANKS} supplier in {@code ModBlocks}:
 * <pre>{@code
 * public static final DeferredBlock<Block> CRYSTAL_PLANKS = register(
 *     "crystal_planks",
 *     () -> new BlockCrystalPlanks(BlockCrystalPlanks.defaultProps()));
 * }</pre>
 * (Current stub uses {@code BlockCrystal} with wrong strength 2.0/3.0.)
 */
public class BlockCrystalPlanks extends BlockCrystal {
    public static final MapCodec<BlockCrystalPlanks> CODEC = simpleCodec(BlockCrystalPlanks::new);

    public BlockCrystalPlanks(BlockBehaviour.Properties properties) {
        super(properties);
    }

    /** Gold {@code new CrystalWood(., 1.5F, 4.0F)} + non-opaque. */
    public static BlockBehaviour.Properties defaultProps() {
        return BlockBehaviour.Properties.of()
                .mapColor(MapColor.COLOR_PURPLE)
                .strength(1.5F, 4.0F)
                .sound(SoundType.GLASS)
                .noOcclusion()
                .isViewBlocking((s, g, p) -> false)
                .isSuffocating((s, g, p) -> false);
    }

    @Override
    protected MapCodec<? extends HalfTransparentBlock> codec() {
        return CODEC;
    }
}
