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
 * Gold {@code OreTitanium} — same glow/sparkle behaviour as uranium (light ~0.2).
 * Silver dust stands in for gold REDSTONE particles.
 */
public class BlockTitaniumOre extends Block {
    public static final MapCodec<BlockTitaniumOre> CODEC = simpleCodec(BlockTitaniumOre::new);

    private static final Vector3f SILVER_DUST_COLOR = Vec3.fromRGB24(0xC0C0C0).toVector3f();
    private static final DustParticleOptions SILVER_DUST = new DustParticleOptions(SILVER_DUST_COLOR, 1.0F);

    private boolean glowing = false;
    private int glowcount = 0;

    public BlockTitaniumOre(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends Block> codec() {
        return CODEC;
    }

    @Override
    public void stepOn(Level level, BlockPos pos, BlockState state, Entity entity) {
        this.glow(level, pos);
        super.stepOn(level, pos, state, entity);
    }

    @Override
    protected void attack(BlockState state, Level level, BlockPos pos, Player player) {
        this.glow(level, pos);
        super.attack(state, level, pos, player);
    }

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

    private void glow(Level level, BlockPos pos) {
        this.glowing = true;
        this.glowcount = 10;
        this.sparkle(level, pos);
    }

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

            if (x < pos.getX() || x > pos.getX() + 1
                    || y < 0.0 || y > pos.getY() + 1
                    || z < pos.getZ() || z > pos.getZ() + 1) {
                if (random.nextInt(3) == 2) {
                    level.addParticle(SILVER_DUST, x, y, z, 0.0, 0.0, 0.0);
                }
            }
        }
    }
}
