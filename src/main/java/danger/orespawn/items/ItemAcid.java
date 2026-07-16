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
 * Gold {@code ItemAcid} — registers as {@code acid} ({@code MyAcid}), combat tab.
 * <p>
 * Gold: stack 64; right-click throws gold {@code Acid} (extends {@code LaserBall} with
 * {@code setAcid()}). Port spawns {@link LaserBall} + {@link LaserBall#setAcid()} (no
 * separate Acid entity type yet). Bow SFX at volume 3.0; consume unless creative.
 * <p>
 * Inventory icon: {@code textures/item/acid.png}. Register item and dispenser behavior.
 */
public class ItemAcid extends Item {
    /** Gold max stack size. */
    public static final int MAX_STACK = 64;

    public ItemAcid(Properties properties) {
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
            // gold: new Acid(world, player) → LaserBall + setAcid()
            LaserBall ball = new LaserBall(level, player);
            ball.setAcid();
            ball.launchFrom(player);
            level.addFreshEntity(ball);
        }

        if (!player.getAbilities().instabuild) {
            stack.shrink(1);
        }

        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
    }
}
