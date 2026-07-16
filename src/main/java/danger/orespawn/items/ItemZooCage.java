package danger.orespawn.items;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Gold {@code ZooCage} — builds an iron-floor/roof glass-wall cage centered on the player.
 * Gold sizes: zoo2→3, zoo4→5, zoo6→9, zoo8→13, zoo10→17; half extent =
 * {@code cageSize / 2 + 1}. Stack 16; consume unless creative.
 */
public class ItemZooCage extends Item {
    public static final int MAX_STACK = 16;

    /** Gold constructor second arg (3 / 5 / 9 / 13 / 17). */
    private final int cageSize;

    public ItemZooCage(Properties properties, int cageSize) {
        super(properties);
        this.cageSize = cageSize;
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        Player player = context.getPlayer();
        if (player == null) {
            return InteractionResult.PASS;
        }

        int dirx = 0;
        int dirz = 0;
        if (player.getX() < 0) {
            dirx = -1;
        }
        if (player.getZ() < 0) {
            dirz = -1;
        }

        // gold: player block pos with 0.99*dir on negative coords; y = feet-1
        int x = (int) (player.getX() + 0.99 * dirx);
        int y = (int) player.getY() - 1;
        int z = (int) (player.getZ() + 0.99 * dirz);

        int width = this.cageSize / 2 + 1;
        int length = width;
        int height = width;

        level.playSound(
                null,
                player.getX(),
                player.getY(),
                player.getZ(),
                SoundEvents.GENERIC_EXPLODE.value(),
                SoundSource.PLAYERS,
                1.0F,
                1.5F);

        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        }

        BlockState iron = Blocks.IRON_BLOCK.defaultBlockState();
        BlockState glass = Blocks.GLASS.defaultBlockState();
        BlockState air = Blocks.AIR.defaultBlockState();

        for (int i = -width; i <= width; i++) {
            for (int j = -length; j <= length; j++) {
                for (int k = 0; k <= height + 1; k++) {
                    BlockPos pos = new BlockPos(x + i, y + k, z + j);
                    if (k == height + 1 || k == 0) {
                        level.setBlock(pos, iron, 2);
                    } else if (i != width && j != length && i != -width && j != -length) {
                        level.setBlock(pos, air, 2);
                    } else {
                        level.setBlock(pos, glass, 2);
                    }
                }
            }
        }

        if (!player.getAbilities().instabuild) {
            context.getItemInHand().shrink(1);
        }
        return InteractionResult.SUCCESS;
    }
}
