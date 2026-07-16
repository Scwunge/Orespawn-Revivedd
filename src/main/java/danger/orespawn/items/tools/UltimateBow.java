package danger.orespawn.items.tools;

import danger.orespawn.entity.UltimateArrow;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;

/**
 * 1.7.10 {@code UltimateBow} — gold registers as
 * {@code new UltimateBow(BaseItemID+303).setUnlocalizedName("ultimatebow")}.
 * <p>
 * Gold combat (extends Item, not ItemBow):
 * <ul>
 *   <li>Max stack 1, durability <b>1000</b>, enchantability <b>50</b></li>
 *   <li>Auto-enchants: Power 5, Flame 3, Punch 2, Infinity 1
 *       (onCrafted + inventory / use re-apply if unenchanted)</li>
 *   <li>Right-click starts bow draw ({@code EnumAction.bow}); max use 9000</li>
 *   <li>On release: always fire {@link UltimateArrow} at velocity <b>3.0F</b>
 *       (no charge curve; no ammo check — Infinity is always present)</li>
 *   <li>25% crit; pickup {@code CREATIVE_ONLY}; damages bow by 1</li>
 *   <li>Arrow base damage = {@link UltimateArrow#UltimateBowDamage} (config default 10)</li>
 * </ul>
 * Flat inventory icon: {@code textures/item/ultimatebow.png} (or {@code ultimate_bow.png}).
 * No 3D IItemRenderer in gold.
 */
public class UltimateBow extends Item {
    /** Gold {@code setMaxDamage(1000)}. */
    public static final int ULTIMATE_BOW_USES = 1000;
    /** Gold {@code getItemEnchantability()} = 50. */
    public static final int ULTIMATE_BOW_ENCHANTABILITY = 50;
    /** Gold max use duration {@code func_77626_a} = 9000. */
    public static final int MAX_USE_DURATION = 9000;
    /** Gold UltimateArrow constructor velocity. */
    public static final float SHOOT_VELOCITY = 3.0F;

    public UltimateBow(Properties properties) {
        super(properties);
    }

    @Override
    public void onCraftedBy(ItemStack stack, Level level, Player player) {
        applyGoldEnchants(stack, level);
        super.onCraftedBy(stack, level, player);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        // gold onUsingTick: re-apply kit if Infinity missing / unenchanted
        applyGoldEnchants(stack, level);
        super.inventoryTick(stack, level, entity, slotId, isSelected);
    }

    private static void applyGoldEnchants(ItemStack stack, Level level) {
        // Power 5, Flame 3, Punch 2, Infinity 1
        ToolEnchantHelper.applyAllIfUnenchanted(
                stack,
                level,
                new ToolEnchantHelper.EnchantSpec(Enchantments.POWER, 5),
                new ToolEnchantHelper.EnchantSpec(Enchantments.FLAME, 3),
                new ToolEnchantHelper.EnchantSpec(Enchantments.PUNCH, 2),
                new ToolEnchantHelper.EnchantSpec(Enchantments.INFINITY, 1));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        player.startUsingItem(hand);
        return InteractionResultHolder.consume(stack);
    }

    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity living, int timeLeft) {
        if (!(living instanceof Player player)) {
            return;
        }

        // gold: fire on any release with fixed velocity 3.0F (no charge gate)
        if (!level.isClientSide && level instanceof ServerLevel server) {
            UltimateArrow arrow = new UltimateArrow(level, player, new ItemStack(Items.ARROW), stack);
            arrow.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, SHOOT_VELOCITY, 1.0F);

            // gold nextInt(4) == 1 → crit
            if (level.random.nextInt(4) == 1) {
                arrow.setCritArrow(true);
            }

            // Power / Punch / Flame from weapon enchants (gold applied Punch + Flame manually)
            EnchantmentHelper.onProjectileSpawned(server, stack, arrow, item -> {});

            // gold field_70251_a = 2 → CREATIVE_ONLY
            arrow.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;

            server.addFreshEntity(arrow);
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
        return ULTIMATE_BOW_ENCHANTABILITY;
    }
}
