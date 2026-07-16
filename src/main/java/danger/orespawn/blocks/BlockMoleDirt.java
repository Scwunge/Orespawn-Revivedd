package danger.orespawn.blocks;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * Gold {@code MoleDirtBlock} ({@code moledirt}) — temporary mole mound dirt.
 * Random tick removes self; short collision; slows entities ×0.3 on contact.
 */
public class BlockMoleDirt extends Block {
    public static final MapCodec<BlockMoleDirt> CODEC = simpleCodec(BlockMoleDirt::new);

    /** Gold: height reduced by 0.125 (1/8 block). */
    private static final VoxelShape COLLISION = Block.box(0.0, 0.0, 0.0, 16.0, 14.0, 16.0);

    public BlockMoleDirt(BlockBehaviour.Properties properties) {
        super(properties);
    }

    /** Gold: hardness 0.6, gravel sound, randomTicks. */
    public static BlockBehaviour.Properties defaultProps() {
        return BlockBehaviour.Properties.of()
                .mapColor(MapColor.DIRT)
                .strength(0.6f)
                .randomTicks()
                .sound(SoundType.GRAVEL);
    }

    @Override
    protected MapCodec<? extends Block> codec() {
        return CODEC;
    }

    @Override
    protected VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return COLLISION;
    }

    @Override
    protected boolean isRandomlyTicking(BlockState state) {
        return true;
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (!level.isClientSide) {
            // Gold: set to air on tick
            level.setBlock(pos, Blocks.AIR.defaultBlockState(), 2);
        }
    }

    @Override
    protected void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        Vec3 motion = entity.getDeltaMovement();
        entity.setDeltaMovement(motion.x * 0.3, motion.y, motion.z * 0.3);
    }

    @Override
    public void stepOn(Level level, BlockPos pos, BlockState state, Entity entity) {
        Vec3 motion = entity.getDeltaMovement();
        entity.setDeltaMovement(motion.x * 0.3, motion.y, motion.z * 0.3);
        super.stepOn(level, pos, state, entity);
    }
}
