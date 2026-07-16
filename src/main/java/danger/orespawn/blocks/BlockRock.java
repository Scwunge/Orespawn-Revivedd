package danger.orespawn.blocks;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;

/**
 * Gold {@code RockBlock} — simple stone terrain block.
 * Hardness 2.0 / resistance 1.0 (gold constructor).
 * <p>
 * Gold registration of this class is incomplete in the decompile; exclusive class
 * kept for 1:1 parity. Suggested registry id: {@code rock_block}.
 */
public class BlockRock extends Block {
    public static final MapCodec<BlockRock> CODEC = simpleCodec(BlockRock::new);

    public BlockRock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    /** Gold: hardness 2.0F, resistance 1.0F, rock material. */
    public static BlockBehaviour.Properties defaultProps() {
        return BlockBehaviour.Properties.of()
                .mapColor(MapColor.STONE)
                .strength(2.0f, 1.0f)
                .requiresCorrectToolForDrops()
                .sound(SoundType.STONE);
    }

    @Override
    protected MapCodec<? extends Block> codec() {
        return CODEC;
    }
}
