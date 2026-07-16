package danger.orespawn.items;

import danger.orespawn.entity.WaterBall;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

/**
 * Gold {@code ItemWaterBall} — registers as {@code waterball}, stack 64, combat tab.
 * Right-click throws {@link WaterBall} with gold launch velocity 1.5 and bow SFX.
 * <p>
 * Inventory icon: {@code textures/item/waterball.png}.
 */
public class ItemWaterBall extends Item {
    public ItemWaterBall(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        // gold: "random.bow" 0.5F, 0.4F / (rand * 0.4F + 0.8F)
        level.playSound(
                null,
                player.getX(),
                player.getY(),
                player.getZ(),
                SoundEvents.ARROW_SHOOT,
                SoundSource.PLAYERS,
                0.5F,
                0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F));

        if (!level.isClientSide) {
            WaterBall ball = new WaterBall(level, player);
            ball.launchFrom(player);
            level.addFreshEntity(ball);
        }

        if (!player.getAbilities().instabuild) {
            stack.shrink(1);
        }

        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
    }
}
