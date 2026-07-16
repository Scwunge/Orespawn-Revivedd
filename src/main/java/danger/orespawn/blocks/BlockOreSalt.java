package danger.orespawn.blocks;

import com.mojang.serialization.MapCodec;
import danger.orespawn.entity.Ant;
import net.minecraft.core.BlockPos;
import net.minecraft.world.damagesource.DamageSources;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;

/**
 * Gold {@code OreSalt} ({@code oresalt}) — hardness 5 / resistance 2.
 * Damages {@link Ant} (and subclasses) on walk / inside (cactus-style 5.0).
 */
public class BlockOreSalt extends Block {
    public static final MapCodec<BlockOreSalt> CODEC = simpleCodec(BlockOreSalt::new);

    public BlockOreSalt(BlockBehaviour.Properties properties) {
        super(properties);
    }

    /** Gold: hardness 5.0F, resistance 2.0F, stone material. */
    public static BlockBehaviour.Properties defaultProps() {
        return BlockBehaviour.Properties.of()
                .mapColor(MapColor.SAND)
                .strength(5.0f, 2.0f)
                .requiresCorrectToolForDrops()
                .sound(SoundType.STONE);
    }

    @Override
    protected MapCodec<? extends Block> codec() {
        return CODEC;
    }

    @Override
    public void stepOn(Level level, BlockPos pos, BlockState state, Entity entity) {
        hurtAnt(level, entity);
        super.stepOn(level, pos, state, entity);
    }

    @Override
    protected void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        hurtAnt(level, entity);
    }

    private static void hurtAnt(Level level, Entity entity) {
        if (entity instanceof Ant) {
            DamageSources sources = level.damageSources();
            entity.hurt(sources.cactus(), 5.0F);
        }
    }
}
