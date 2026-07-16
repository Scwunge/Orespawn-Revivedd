package danger.orespawn.items;

import danger.orespawn.entity.IrukandjiArrow;
import danger.orespawn.init.ModItems;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;

/**
 * Gold {@code SkateBow} — durability 300, stack 1, enchantability 50.
 * Draws like a bow; on release fires {@link IrukandjiArrow} using Irukandji arrows as ammo
 * (creative free-fire). Charge curve: charge/20, then (f²+2f)/3, min 0.1, max 1.75.
 * 5% crit; damages bow by 1.
 */
public class ItemSkateBow extends Item {
    public static final int SKATE_BOW_USES = 300;
    public static final int ENCHANTABILITY = 50;
    public static final int MAX_USE_DURATION = 9000;

    public ItemSkateBow(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        boolean hasAmmo = player.getAbilities().instabuild || hasIrukandjiArrow(player);
        if (!hasAmmo) {
            return InteractionResultHolder.fail(stack);
        }
        player.startUsingItem(hand);
        return InteractionResultHolder.consume(stack);
    }

    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity living, int timeLeft) {
        if (!(living instanceof Player player)) {
            return;
        }

        int used = this.getUseDuration(stack, living) - timeLeft;
        boolean creative = player.getAbilities().instabuild;
        if (!creative && !hasIrukandjiArrow(player)) {
            return;
        }

        float f = used / 20.0F;
        f = (f * f + f * 2.0F) / 3.0F;
        if (f < 0.1F) {
            return;
        }
        if (f > 1.75F) {
            f = 1.75F;
        }

        if (!level.isClientSide && level instanceof ServerLevel server) {
            ItemStack ammo = new ItemStack(ModItems.IRUKANDJI_ARROW.get());
            IrukandjiArrow arrow = new IrukandjiArrow(level, player, ammo, stack);
            arrow.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, f * 3.0F, 1.0F);

            // gold nextInt(20) == 1 → crit
            if (level.random.nextInt(20) == 1) {
                arrow.setCritArrow(true);
            }

            EnchantmentHelper.onProjectileSpawned(server, stack, arrow, item -> {});

            arrow.pickup = creative ? AbstractArrow.Pickup.CREATIVE_ONLY : AbstractArrow.Pickup.ALLOWED;
            server.addFreshEntity(arrow);

            if (!creative) {
                consumeIrukandjiArrow(player);
            }
            stack.hurtAndBreak(1, player, LivingEntity.getSlotForHand(player.getUsedItemHand()));
        }

        level.playSound(
                null,
                player.getX(),
                player.getY(),
                player.getZ(),
                SoundEvents.ARROW_SHOOT,
                SoundSource.PLAYERS,
                1.0F,
                1.0F / (level.getRandom().nextFloat() * 0.4F + 1.2F) + 0.5F);
        player.awardStat(Stats.ITEM_USED.get(this));
    }

    private static boolean hasIrukandjiArrow(Player player) {
        for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
            ItemStack s = player.getInventory().getItem(i);
            if (s.is(ModItems.IRUKANDJI_ARROW.get())) {
                return true;
            }
        }
        return false;
    }

    private static void consumeIrukandjiArrow(Player player) {
        for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
            ItemStack s = player.getInventory().getItem(i);
            if (s.is(ModItems.IRUKANDJI_ARROW.get())) {
                s.shrink(1);
                return;
            }
        }
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return MAX_USE_DURATION;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.BOW;
    }

    @Override
    public int getEnchantmentValue() {
        return ENCHANTABILITY;
    }
}
