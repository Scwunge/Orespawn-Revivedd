package danger.orespawn.items;

import danger.orespawn.init.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Gold {@code ItemExperienceTreeSeed} — stack 1.
 * Gold places ExperiencePlant which later ticks into ExperienceTree.
 * Port grows the experience tree immediately (same end result).
 */
public class ItemExperienceTreeSeed extends Item {
    public static final int MAX_STACK = 1;

    public ItemExperienceTreeSeed(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        Player player = context.getPlayer();
        BlockPos pos = context.getClickedPos();
        BlockState base = level.getBlockState(pos);

        if (!base.is(Blocks.GRASS_BLOCK)
                && !base.is(Blocks.DIRT)
                && !base.is(Blocks.FARMLAND)
                && !base.is(Blocks.COARSE_DIRT)) {
            return InteractionResult.FAIL;
        }

        if (level.isClientSide) {
            for (int j = 0; j < 10; j++) {
                level.addParticle(
                        ParticleTypes.HAPPY_VILLAGER,
                        pos.getX() + level.random.nextFloat(),
                        pos.getY() + 1.0 + level.random.nextFloat(),
                        pos.getZ() + level.random.nextFloat(),
                        0.0,
                        0.0,
                        0.0);
            }
            level.playSound(player, pos, SoundEvents.EXPERIENCE_ORB_PICKUP, SoundSource.BLOCKS, 0.6F, 1.2F);
            return InteractionResult.SUCCESS;
        }

        if (level instanceof ServerLevel server) {
            growExperienceTree(server, pos.getX(), pos.getY(), pos.getZ(), server.getRandom());
        }

        if (player != null && !player.getAbilities().instabuild) {
            context.getItemInHand().shrink(1);
        }
        return InteractionResult.SUCCESS;
    }

    /**
     * Gold {@code Trees.ExperienceTree}: 2×2 oak trunk, mid/high branch crosses,
     * experience-leaf toppers.
     */
    public static void growExperienceTree(Level world, int x, int y, int z, RandomSource random) {
        BlockState base = world.getBlockState(new BlockPos(x, y, z));
        if (!base.is(Blocks.GRASS_BLOCK)
                && !base.is(Blocks.DIRT)
                && !base.is(Blocks.FARMLAND)
                && !base.is(Blocks.COARSE_DIRT)) {
            return;
        }

        BlockState log = Blocks.OAK_LOG.defaultBlockState();
        BlockState leaves = ModBlocks.EXPERIENCE_LEAVES
                .get()
                .defaultBlockState()
                .setValue(LeavesBlock.PERSISTENT, true);

        // Lower 2×2 trunk y+1.5
        for (int j = 1; j < 6; j++) {
            for (int i = 0; i < 2; i++) {
                for (int k = 0; k < 2; k++) {
                    set(world, x + i, y + j, z + k, log);
                }
            }
        }

        // Mid branches at y+6
        placeCross(world, x, y + 6, z, log, leaves, 4);
        placeCross(world, x + 1, y + 6, z, log, leaves, 4);
        placeCross(world, x, y + 6, z + 1, log, leaves, 4);
        placeCross(world, x + 1, y + 6, z + 1, log, leaves, 4);

        // Upper 2×2 trunk y+7.18
        for (int j = 7; j < 19; j++) {
            for (int i = 0; i < 2; i++) {
                for (int k = 0; k < 2; k++) {
                    set(world, x + i, y + j, z + k, log);
                }
            }
        }

        // Small branches at y+19
        placeCross(world, x, y + 19, z, log, leaves, 2);
        placeCross(world, x + 1, y + 19, z, log, leaves, 2);
        placeCross(world, x, y + 19, z + 1, log, leaves, 2);
        placeCross(world, x + 1, y + 19, z + 1, log, leaves, 2);

        int grow = 5 + random.nextInt(6);
        for (int j = 19; j < 19 + grow; j++) {
            for (int i = 0; i < 2; i++) {
                for (int k = 0; k < 2; k++) {
                    set(world, x + i, y + j, z + k, log);
                    makeLeaves(world, x + i, y + j, z + k, leaves);
                }
            }
        }
    }

    private static void placeCross(
            Level world, int x, int y, int z, BlockState log, BlockState leaves, int len) {
        for (int d = 1; d <= len; d++) {
            set(world, x + d, y, z, log);
            set(world, x - d, y, z, log);
            set(world, x, y, z + d, log);
            set(world, x, y, z - d, log);
        }
        makeLeaves(world, x + len, y, z, leaves);
        makeLeaves(world, x - len, y, z, leaves);
        makeLeaves(world, x, y, z + len, leaves);
        makeLeaves(world, x, y, z - len, leaves);
    }

    /** Gold make_leaves: 3×3×3 leaf blob around a log if air. */
    private static void makeLeaves(Level world, int x, int y, int z, BlockState leaves) {
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                for (int dz = -1; dz <= 1; dz++) {
                    BlockPos p = new BlockPos(x + dx, y + dy, z + dz);
                    if (world.getBlockState(p).isAir()) {
                        world.setBlock(p, leaves, 2);
                    }
                }
            }
        }
    }

    private static void set(Level world, int x, int y, int z, BlockState state) {
        world.setBlock(new BlockPos(x, y, z), state, 2);
    }
}
