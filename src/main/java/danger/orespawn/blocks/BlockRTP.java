package danger.orespawn.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;

/**
 * Gold {@code RTPBlock} (unlocalized {@code blockteleport}, Material.rock).
 * <p>
 * On player contact: search up to 1000 random nearby positions (~±16–24) for solid floor
 * + two air cells, then teleport there with smoke/explode FX.
 * <p>
 * Gold used {@code onEntityCollidedWithBlock}; modern solid cubes rarely fire
 * {@link #entityInside}, so {@link #stepOn} is also wired for walking on top.
 */
public class BlockRTP extends Block {
    public BlockRTP(BlockBehaviour.Properties properties) {
        super(properties);
    }

    public static BlockBehaviour.Properties defaultProps() {
        return BlockBehaviour.Properties.of()
                .mapColor(MapColor.STONE)
                .strength(1.5f, 6.0f)
                .requiresCorrectToolForDrops()
                .sound(SoundType.STONE);
    }

    /** Gold {@code onEntityCollidedWithBlock} / {@code func_149724_b}. */
    @Override
    protected void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if (level.isClientSide || !(entity instanceof Player player)) {
            return;
        }
        tryRandomTeleport(level, pos, player);
    }

    /** Practical path for solid pad: player walking on the block. */
    @Override
    public void stepOn(Level level, BlockPos pos, BlockState state, Entity entity) {
        if (!level.isClientSide && entity instanceof Player player) {
            tryRandomTeleport(level, pos, player);
        }
        super.stepOn(level, pos, state, entity);
    }

    /**
     * Gold search loop: 1000 tries; offset x/z by ±16 ± rand(8); y in [blockY-4, blockY+4];
     * require solid below and air at feet + head.
     */
    public static void tryRandomTeleport(Level level, BlockPos origin, Player player) {
        int par2 = origin.getX();
        int par3 = origin.getY();
        int par4 = origin.getZ();
        int x = par2;
        int y = par3;
        int z = par4;
        boolean found = false;

        for (int tries = 0; tries < 1000 && !found; tries++) {
            if (level.random.nextInt(2) == 0) {
                x = par2 + 16 + level.random.nextInt(8) - level.random.nextInt(8);
            } else {
                x = par2 - 16 + level.random.nextInt(8) - level.random.nextInt(8);
            }

            if (level.random.nextInt(2) == 0) {
                z = par4 + 16 + level.random.nextInt(8) - level.random.nextInt(8);
            } else {
                z = par4 - 16 + level.random.nextInt(8) - level.random.nextInt(8);
            }

            for (y = par3 - 4; y <= par3 + 4; y++) {
                BlockPos feet = new BlockPos(x, y, z);
                BlockPos below = feet.below();
                BlockPos head = feet.above();
                if (level.getBlockState(below).isFaceSturdy(level, below, Direction.UP)
                        && level.getBlockState(feet).isAir()
                        && level.getBlockState(head).isAir()) {
                    found = true;
                    break;
                }
            }
        }

        if (!found) {
            return;
        }

        double destX = x + 0.5;
        double destY = y;
        double destZ = z + 0.5;
        float yaw = player.getYRot();

        if (player instanceof ServerPlayer mp) {
            mp.teleportTo(destX, destY, destZ);
            mp.setYRot(yaw);
            mp.setXRot(0.0F);
        } else {
            player.moveTo(destX, destY, destZ, yaw, 0.0F);
        }

        if (level instanceof ServerLevel server) {
            for (int i = 0; i < 6; i++) {
                server.sendParticles(
                        ParticleTypes.SMOKE, destX, destY + 2.25, destZ, 1, 0.0, 0.0, 0.0, 0.0);
                server.sendParticles(
                        ParticleTypes.EXPLOSION, destX, destY + 2.25, destZ, 1, 0.0, 0.0, 0.0, 0.0);
                server.sendParticles(
                        ParticleTypes.DUST_PLUME, destX, destY + 2.25, destZ, 1, 0.0, 0.0, 0.0, 0.0);
            }
            // gold "random.explode" pitch 1.5
            server.playSound(
                    null,
                    destX,
                    destY,
                    destZ,
                    SoundEvents.GENERIC_EXPLODE.value(),
                    SoundSource.BLOCKS,
                    1.0F,
                    1.5F);
        }
    }
}
