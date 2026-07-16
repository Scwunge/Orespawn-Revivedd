package danger.orespawn.items;

import danger.orespawn.entity.IceBall;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

/**
 * Gold {@code ItemIceBall} — registers as {@code iceball}, stack 64, combat tab.
 * Right-click throws {@link IceBall} with gold launch velocity 1.5 (via
 * {@link danger.orespawn.entity.LaserBall#launchFrom}) and bow SFX at volume 3.0.
 * <p>
 * Inventory icon: {@code textures/item/iceball.png}.
 */
public class ItemIceBall extends Item {
    public ItemIceBall(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        // gold: "random.bow" 3.0F, 1.0F
        level.playSound(
                null,
                player.getX(),
                player.getY(),
                player.getZ(),
                SoundEvents.ARROW_SHOOT,
                SoundSource.PLAYERS,
                3.0F,
                1.0F);

        if (!level.isClientSide) {
            IceBall ball = new IceBall(level, player);
            ball.launchFrom(player);
            level.addFreshEntity(ball);
        }

        if (!player.getAbilities().instabuild) {
            stack.shrink(1);
        }

        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
    }
}
