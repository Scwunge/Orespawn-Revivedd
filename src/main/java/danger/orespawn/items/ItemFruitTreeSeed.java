package danger.orespawn.items;

import danger.orespawn.init.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Gold {@code ItemAppleSeed} shared by apple / cherry / peach tree seeds.
 * On grass/dirt/farmland: grows a custom fruit tree (oak log trunk + cross beams + leaf canopy).
 */
public class ItemFruitTreeSeed extends Item {
    public static final int MAX_STACK = 16;

    public enum Kind {
        APPLE,
        CHERRY,
        PEACH
    }

    private final Kind kind;

    public ItemFruitTreeSeed(Properties properties, Kind kind) {
        super(properties);
        this.kind = kind;
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        Player player = context.getPlayer();
        BlockPos pos = context.getClickedPos();
        BlockState base = level.getBlockState(pos);

        if (!base.is(Blocks.GRASS_BLOCK) && !base.is(Blocks.DIRT) && !base.is(Blocks.FARMLAND) && !base.is(Blocks.COARSE_DIRT)) {
            return InteractionResult.FAIL;
        }

        if (level.isClientSide) {
            level.playSound(player, pos, SoundEvents.GRASS_PLACE, SoundSource.BLOCKS, 1.0F, 1.0F);
            return InteractionResult.SUCCESS;
        }

        makeTree(level, pos.getX(), pos.getY(), pos.getZ(), leafBlock());

        if (player != null && !player.getAbilities().instabuild) {
            context.getItemInHand().shrink(1);
        }
        return InteractionResult.SUCCESS;
    }

    private Block leafBlock() {
        return switch (this.kind) {
            case CHERRY -> ModBlocks.CHERRY_LEAVES.get();
            case PEACH -> ModBlocks.PEACH_LEAVES.get();
            default -> ModBlocks.APPLE_LEAVES.get();
        };
    }

    /**
     * Gold {@code ItemAppleSeed.makeTree} — trunk + mid/high cross beams + leaf cloud.
     * Heights/widths vary by fruit kind.
     */
    public static void makeTree(Level world, int x, int y, int z, Block leafBlock) {
        BlockState base = world.getBlockState(new BlockPos(x, y, z));
        if (!base.is(Blocks.GRASS_BLOCK)
                && !base.is(Blocks.DIRT)
                && !base.is(Blocks.FARMLAND)
                && !base.is(Blocks.COARSE_DIRT)) {
            return;
        }

        int h1 = 12;
        int h2 = 6;
        int h3 = 9;
        int h4 = 6;
        int h5 = 14;
        int w1 = 5;
        int w2 = 3;

        if (leafBlock == ModBlocks.PEACH_LEAVES.get()) {
            h1 = 10;
            h2 = 5;
            h3 = 7;
            h4 = 5;
            h5 = 12;
            w1 = 4;
            w2 = 2;
        } else if (leafBlock == ModBlocks.CHERRY_LEAVES.get()) {
            h1 = 8;
            h2 = 3;
            h3 = 5;
            h4 = 3;
            h5 = 10;
            w1 = 3;
            w2 = 1;
        }

        BlockState log = Blocks.OAK_LOG.defaultBlockState();
        BlockState leaves = leafBlock.defaultBlockState();
        // Keep fruit leaves from decaying away from logs awkwardly
        if (leaves.hasProperty(net.minecraft.world.level.block.LeavesBlock.PERSISTENT)) {
            leaves = leaves.setValue(net.minecraft.world.level.block.LeavesBlock.PERSISTENT, true);
        }

        for (int j = 1; j < h1; j++) {
            world.setBlock(new BlockPos(x, y + j, z), log, 2);
        }

        for (int var19 = 1; var19 < w1; var19++) {
            setLog(world, x + var19, y + h2, z, log);
            setLog(world, x - var19, y + h2, z, log);
            setLog(world, x, y + h2, z + var19, log);
            setLog(world, x, y + h2, z - var19, log);
        }

        for (int var23 = 1; var23 < w2; var23++) {
            setLog(world, x + var23, y + h3, z, log);
            setLog(world, x - var23, y + h3, z, log);
            setLog(world, x, y + h3, z + var23, log);
            setLog(world, x, y + h3, z - var23, log);
        }

        boolean appleLeaves = leafBlock == ModBlocks.APPLE_LEAVES.get();
        for (int i = h4; i < h5; i++) {
            int width = 6;
            if (i > 8) {
                width = 5;
            }
            if (i > 10) {
                width = 4;
            }
            if (!appleLeaves) {
                width--;
            }
            for (int var27 = -width; var27 <= width; var27++) {
                for (int k = -width; k <= width; k++) {
                    BlockPos p = new BlockPos(x + k, y + i, z + var27);
                    if (world.getBlockState(p).isAir()) {
                        world.setBlock(p, leaves, 2);
                    }
                }
            }
        }
    }

    private static void setLog(Level world, int x, int y, int z, BlockState log) {
        world.setBlock(new BlockPos(x, y, z), log, 2);
    }
}
