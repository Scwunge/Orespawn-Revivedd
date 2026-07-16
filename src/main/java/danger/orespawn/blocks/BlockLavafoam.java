package danger.orespawn.blocks;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * Gold {@code Lavafoam} ({@code lavafoam}) — hardness/resistance 5, slipperiness 1.1,
 * directional launch on entity collision; XP when broken in Nether.
 */
public class BlockLavafoam extends Block {
    public static final MapCodec<BlockLavafoam> CODEC = simpleCodec(BlockLavafoam::new);

    /** Gold shrink 0.0125 on sides for collision. */
    private static final VoxelShape COLLISION = Block.box(0.2, 0.0, 0.2, 15.8, 16.0, 15.8);

    public BlockLavafoam(BlockBehaviour.Properties properties) {
        super(properties);
    }

    /** Gold: hardness 5, resistance 5, slipperiness 1.1, randomTicks. */
    public static BlockBehaviour.Properties defaultProps() {
        return BlockBehaviour.Properties.of()
                .mapColor(MapColor.COLOR_ORANGE)
                .strength(5.0f, 5.0f)
                .requiresCorrectToolForDrops()
                .friction(1.1f)
                .randomTicks()
                .sound(SoundType.STONE);
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
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (random.nextInt(20) == 0) {
            sparkle(level, pos, random);
        }
    }

    private static void sparkle(Level level, BlockPos pos, RandomSource random) {
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
                int which = random.nextInt(10);
                if (which == 1) {
                    level.addParticle(ParticleTypes.SMOKE, x, y, z, 0.0, 0.0, 0.0);
                } else if (which == 2) {
                    level.addParticle(DustParticleOptions.REDSTONE, x, y, z, 0.0, 0.0, 0.0);
                }
            }
        }
    }

    @Override
    protected void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if (!(entity instanceof LivingEntity)) {
            return;
        }
        double pi = Math.PI;
        double pi2 = pi / 2.0;
        double pi4 = pi / 4.0;
        double d = Math.atan2(entity.getX() - (pos.getX() + 0.5), entity.getZ() - (pos.getZ() + 0.5));
        if (d < 0.0) {
            d += pi * 2.0;
        }
        Vec3 motion = entity.getDeltaMovement();
        double mx = motion.x;
        double mz = motion.z;
        if (d > pi2 - pi4 && d < pi2 + pi4) {
            mx = 0.45;
            mz *= 1.35;
        } else if (d > pi - pi4 && d < pi + pi4) {
            mz = -0.45;
            mx *= 1.35;
        } else if (d > pi + pi2 - pi4 && d < pi + pi2 + pi4) {
            mx = -0.45;
            mz *= 1.35;
        } else {
            mz = 0.45;
            mx *= 1.35;
        }
        entity.setDeltaMovement(mx, motion.y, mz);
        double speed = Math.sqrt(mx * mx + mz * mz);
        if (speed > 1.0) {
            // gold DamageSource.generic
            entity.hurt(level.damageSources().generic(), (float) speed);
        }
    }

    @Override
    protected void spawnAfterBreak(BlockState state, ServerLevel level, BlockPos pos, ItemStack tool, boolean dropXp) {
        super.spawnAfterBreak(state, level, pos, tool, dropXp);
        // Gold: only when dimension is Nether (-1)
        if (dropXp && level.dimensionType().ultraWarm()) {
            int xp = 5 + level.random.nextInt(5) + level.random.nextInt(5);
            popExperience(level, pos, xp);
        }
    }
}
