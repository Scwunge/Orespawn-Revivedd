package danger.orespawn.items.tools;

import danger.orespawn.entity.UltimateFishHook;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;

/**
 * 1.7.10 {@code UltimateFishingRod} — gold registers as
 * {@code new UltimateFishingRod(BaseItemID+304).setUnlocalizedName("ultimatefishingrod")}.
 * <p>
 * Gold combat:
 * <ul>
 *   <li>Max stack 1, durability <b>3000</b></li>
 *   <li>Auto-enchants Lure II (onCrafted + inventory tick if unenchanted)</li>
 *   <li>Right-click: cast / retrieve {@link UltimateFishHook}</li>
 *   <li>Full-3D item model flag in gold ({@code isFull3D})</li>
 * </ul>
 * Flat inventory icon: {@code textures/item/ultimatefishingrod.png}.
 */
public class UltimateFishingRod extends Item {
    /** Gold {@code setMaxDamage(3000)}. */
    public static final int ULTIMATE_FISHING_ROD_USES = 3000;
    /** Gold enchantability — fishing rods use low base; Lure is force-applied. */
    public static final int ULTIMATE_FISHING_ENCHANTABILITY = 1;

    public UltimateFishingRod(Properties properties) {
        super(properties);
    }

    @Override
    public void onCraftedBy(ItemStack stack, Level level, Player player) {
        applyGoldEnchants(stack, level);
        super.onCraftedBy(stack, level, player);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        // gold onUsingTick / craft: re-apply Lure 2 if missing
        applyGoldEnchants(stack, level);
        super.inventoryTick(stack, level, entity, slotId, isSelected);
    }

    private static void applyGoldEnchants(ItemStack stack, Level level) {
        // gold Enchantment.field_77347_r = Lure level 2
        ToolEnchantHelper.applyIfUnenchanted(stack, level, Enchantments.LURE, 2);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (player.fishing != null) {
            // retrieve existing hook (ultimate or otherwise)
            if (!level.isClientSide) {
                int dmg = player.fishing.retrieve(stack);
                ItemStack original = stack.copy();
                stack.hurtAndBreak(dmg, player, LivingEntity.getSlotForHand(hand));
                if (stack.isEmpty()) {
                    net.neoforged.neoforge.event.EventHooks.onPlayerDestroyItem(player, original, hand);
                }
            }
            level.playSound(
                    null,
                    player.getX(),
                    player.getY(),
                    player.getZ(),
                    SoundEvents.FISHING_BOBBER_RETRIEVE,
                    SoundSource.NEUTRAL,
                    1.0F,
                    0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F));
            player.gameEvent(GameEvent.ITEM_INTERACT_FINISH);
        } else {
            // gold: "random.bow" cast sound
            level.playSound(
                    null,
                    player.getX(),
                    player.getY(),
                    player.getZ(),
                    SoundEvents.FISHING_BOBBER_THROW,
                    SoundSource.NEUTRAL,
                    0.5F,
                    0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F));

            if (level instanceof ServerLevel server) {
                int lureTicks = (int) (EnchantmentHelper.getFishingTimeReduction(server, stack, player) * 20.0F);
                int luck = EnchantmentHelper.getFishingLuckBonus(server, stack, player);
                level.addFreshEntity(new UltimateFishHook(player, level, luck, lureTicks));
            }

            player.awardStat(Stats.ITEM_USED.get(this));
            player.gameEvent(GameEvent.ITEM_INTERACT_START);
        }

        // gold swingItem
        player.swing(hand, true);
        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
    }

    @Override
    public int getEnchantmentValue() {
        return ULTIMATE_FISHING_ENCHANTABILITY;
    }

    @Override
    public boolean canPerformAction(ItemStack stack, net.neoforged.neoforge.common.ItemAbility itemAbility) {
        return net.neoforged.neoforge.common.ItemAbilities.DEFAULT_FISHING_ROD_ACTIONS.contains(itemAbility);
    }
}
