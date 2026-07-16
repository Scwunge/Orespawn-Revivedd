package danger.orespawn.blocks;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

/**
 * Gold {@code OreUranium} — stone ore with lightLevel ~0.2 and short glow/sparkle
 * when walked on, attacked, or used. Green dust stands in for gold REDSTONE particles.
 * <p>
 * Glow state is block-instance fields (gold shared singleton behaviour).
 */
public class BlockUraniumOre extends Block {
    public static final MapCodec<BlockUraniumOre> CODEC = simpleCodec(BlockUraniumOre::new);

    /** Green dust (uranium tint); gold used EnumParticleTypes.REDSTONE. */
    private static final Vector3f GREEN_DUST_COLOR = Vec3.fromRGB24(0x33FF33).toVector3f();
    private static final DustParticleOptions GREEN_DUST = new DustParticleOptions(GREEN_DUST_COLOR, 1.0F);

    /** Gold: private boolean glowing / int glowcount on the Block singleton. */
    private boolean glowing = false;
    private int glowcount = 0;

    public BlockUraniumOre(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends Block> codec() {
        return CODEC;
    }

    /** Gold {@code func_176199_a} (onEntityWalk). */
    @Override
    public void stepOn(Level level, BlockPos pos, BlockState state, Entity entity) {
        this.glow(level, pos);
        super.stepOn(level, pos, state, entity);
    }

    /** Gold {@code func_180649_a} (onBlockClicked). */
    @Override
    protected void attack(BlockState state, Level level, BlockPos pos, Player player) {
        this.glow(level, pos);
        super.attack(state, level, pos, player);
    }

    /** Gold {@code func_180639_a} (onBlockActivated). */
    @Override
    protected ItemInteractionResult useItemOn(
            ItemStack stack,
            BlockState state,
            Level level,
            BlockPos pos,
            Player player,
            InteractionHand hand,
            BlockHitResult hitResult) {
        this.glow(level, pos);
        return ItemInteractionResult.SUCCESS;
    }

    /** Gold {@code func_180655_c} (randomDisplayTick / animateTick). */
    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (this.glowing) {
            this.sparkle(level, pos);
            if (this.glowcount > 0) {
                this.glowcount--;
            } else {
                this.glowing = false;
            }
        }
    }

    /** Gold: glowing=true, glowcount=10, immediate sparkle. */
    private void glow(Level level, BlockPos pos) {
        this.glowing = true;
        this.glowcount = 10;
        this.sparkle(level, pos);
    }

    /**
     * Gold sparkle: sample each face; if neighbour is not opaque, emit dust just outside
     * the face. Gold spawned REDSTONE with 1/3 chance; we use green DUST the same way.
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

            // gold: only spawn when sample is outside the block volume; 1/3 chance
            if (x < pos.getX() || x > pos.getX() + 1
                    || y < 0.0 || y > pos.getY() + 1
                    || z < pos.getZ() || z > pos.getZ() + 1) {
                if (random.nextInt(3) == 2) {
                    level.addParticle(GREEN_DUST, x, y, z, 0.0, 0.0, 0.0);
                }
            }
        }
    }
}
