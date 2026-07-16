package danger.orespawn.items;

import danger.orespawn.entity.LaserBall;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

/**
 * Gold {@code ItemLaserBall} — registers as {@code laserball}, stack 64, combat tab.
 * Right-click throws {@link LaserBall} with gold launch velocity 1.5 and fireworks launch SFX.
 * <p>
 * Inventory icon: {@code textures/item/laserball.png}.
 */
public class ItemLaserBall extends Item {
    public ItemLaserBall(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        // gold: "fireworks.launch" 3.0F, 1.0F
        level.playSound(
                null,
                player.getX(),
                player.getY(),
                player.getZ(),
                SoundEvents.FIREWORK_ROCKET_LAUNCH,
                SoundSource.PLAYERS,
                3.0F,
                1.0F);

        if (!level.isClientSide) {
            LaserBall ball = new LaserBall(level, player);
            ball.launchFrom(player);
            level.addFreshEntity(ball);
        }

        if (!player.getAbilities().instabuild) {
            stack.shrink(1);
        }

        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
    }
}
