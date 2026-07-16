package danger.orespawn.blocks;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

/**
 * Gold {@code BlockTitanium} (storage block) — metal block with light ~0.2 and
 * occasional face sparkle. Silver dust stands in for gold FLAME/SMOKE/REDSTONE.
 * <p>
 * Note: gold used lightLevel 0.5F; port uses ~0.2 (3/15) per task parity with uranium.
 */
public class BlockTitaniumMetal extends Block {
    public static final MapCodec<BlockTitaniumMetal> CODEC = simpleCodec(BlockTitaniumMetal::new);

    /** Silver/grey dust (titanium tint); gold cycled FLAME / SMOKE_NORMAL / REDSTONE. */
    private static final Vector3f SILVER_DUST_COLOR = Vec3.fromRGB24(0xC0C0C0).toVector3f();
    private static final DustParticleOptions SILVER_DUST = new DustParticleOptions(SILVER_DUST_COLOR, 1.0F);

    public BlockTitaniumMetal(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends Block> codec() {
        return CODEC;
    }

    /** Gold {@code func_180655_c} — 1/20 chance per client tick to sparkle. */
    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (random.nextInt(20) == 0) {
            this.sparkle(level, pos);
        }
    }

    /**
     * Gold sparkle: sample each face; if neighbour is not opaque, emit dust just
     * outside the face. Gold always spawned one of FLAME/SMOKE/REDSTONE when the
     * sample sat outside the block volume; we always emit silver DUST the same way.
     */
    private void sparkle(Level level, BlockPos pos) {
        RandomSource random = level.random;
        double edge = 0.0625;

        for (Direction direction : Direction.values()) {
            BlockPos neighbour = pos.relative(direction);
            if (level.getBlockState(neighbour).isSolidRender(level, neighbour)) {
                continue;
            }

            double x = pos.getX() + random.nextFloat();
            double y = pos.getY() + random.nextFloat();
            double z = pos.getZ() + random.nextFloat();

            if (direction == Direction.UP) {
                y = pos.getY() + 1 + edge;
            } else if (direction == Direction.DOWN) {
                y = pos.getY() - edge;
            } else if (direction == Direction.SOUTH) {
                z = pos.getZ() + 1 + edge;
            } else if (direction == Direction.NORTH) {
                z = pos.getZ() - edge;
            } else if (direction == Direction.EAST) {
                x = pos.getX() + 1 + edge;
            } else if (direction == Direction.WEST) {
                x = pos.getX() - edge;
            }

            // gold: only spawn when sample is outside the block volume
            if (x < pos.getX() || x > pos.getX() + 1
                    || y < 0.0 || y > pos.getY() + 1
                    || z < pos.getZ() || z > pos.getZ() + 1) {
                level.addParticle(SILVER_DUST, x, y, z, 0.0, 0.0, 0.0);
            }
        }
    }
}
