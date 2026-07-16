package danger.orespawn.items;

import danger.orespawn.entity.DeadIrukandji;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

/**
 * Gold {@code ItemIrukandji} ({@code deadirukandji}) — stack 64, combat tab.
 * Right-click throws {@link DeadIrukandji} (100 dmg laser-ball variant) with bow SFX.
 */
public class ItemDeadIrukandji extends Item {
    public ItemDeadIrukandji(Properties properties) {
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
            DeadIrukandji proj = new DeadIrukandji(level, player);
            proj.launchFrom(player);
            level.addFreshEntity(proj);
        }

        if (!player.getAbilities().instabuild) {
            stack.shrink(1);
        }

        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
    }
}
