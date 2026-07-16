package danger.orespawn.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * Gold {@code PortalBlock} extends {@code BlockPortal} but is a <b>full no-op</b>:
 * <ul>
 *   <li>{@code updateTick} empty</li>
 *   <li>{@code tryToCreatePortal} → false</li>
 *   <li>{@code onNeighborBlockChange} empty</li>
 *   <li>{@code onEntityCollidedWithBlock} empty</li>
 * </ul>
 * Port keeps a translucent portal-looking block with no teleport / frame logic so
 * creative placement and future dim-hook wiring have a registry target. Real dim travel
 * in gold used other systems (ant items, RTP, elevators) — not this block.
 */
public class BlockPortalOreSpawn extends Block {
    public BlockPortalOreSpawn(BlockBehaviour.Properties properties) {
        super(properties);
    }

    public static BlockBehaviour.Properties defaultProps() {
        return BlockBehaviour.Properties.of()
                .mapColor(MapColor.COLOR_PURPLE)
                .noCollission()
                .strength(-1.0F, 3_600_000.0F) // portal-like unbreakable
                .lightLevel(s -> 11)
                .sound(SoundType.GLASS)
                .noOcclusion()
                .isViewBlocking((s, g, p) -> false)
                .isSuffocating((s, g, p) -> false)
                .pushReaction(PushReaction.BLOCK);
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        // Thin portal slab stand-in (gold portal thickness ~0.125 on axis; full for simplicity)
        return Shapes.block();
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    /** Gold: no random tick work. */
    @Override
    protected boolean isRandomlyTicking(BlockState state) {
        return false;
    }

    /** Gold {@code onEntityCollidedWithBlock} empty — no teleport. */
    @Override
    protected void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        // intentionally empty (gold parity)
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        // gold PortalBlock had no particle override; vanilla portal particles omitted
    }
}
