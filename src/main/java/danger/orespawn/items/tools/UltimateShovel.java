package danger.orespawn.items.tools;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;

/**
 * Gold UltimateShovel — Efficiency 5 when unenchanted.
 * Plus Silk Touch, Unbreaking 3, Mending (no Fortune — conflicts with Silk Touch).
 */
public class UltimateShovel extends ShovelItem {
    public UltimateShovel(Properties properties) {
        super(OrespawnToolMaterial.UltimateTools.tier, properties);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        ToolEnchantHelper.applyAllIfUnenchanted(
                stack,
                level,
                new ToolEnchantHelper.EnchantSpec(Enchantments.EFFICIENCY, 5),
                new ToolEnchantHelper.EnchantSpec(Enchantments.SILK_TOUCH, 1),
                new ToolEnchantHelper.EnchantSpec(Enchantments.UNBREAKING, 3),
                new ToolEnchantHelper.EnchantSpec(Enchantments.MENDING, 1));
        super.inventoryTick(stack, level, entity, slotId, isSelected);
    }
}
