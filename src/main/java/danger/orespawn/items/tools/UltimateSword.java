package danger.orespawn.items.tools;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;

/**
 * Gold UltimateSword — Looting 6 + Unbreaking 6 when unenchanted
 * (MCP stable_39: field_185304_p LOOTING, field_185307_s UNBREAKING).
 * Plus previously specified 1.21 QoL: Sweeping Edge, Fire Aspect, Mending.
 */
public class UltimateSword extends SwordItem {
    public UltimateSword(Properties properties) {
        super(OrespawnToolMaterial.UltimateTools.tier, properties);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        ToolEnchantHelper.applyAllIfUnenchanted(
                stack,
                level,
                // gold
                new ToolEnchantHelper.EnchantSpec(Enchantments.LOOTING, 6),
                new ToolEnchantHelper.EnchantSpec(Enchantments.UNBREAKING, 6),
                // previously user-specified 1.21 QoL
                new ToolEnchantHelper.EnchantSpec(Enchantments.SWEEPING_EDGE, 3),
                new ToolEnchantHelper.EnchantSpec(Enchantments.FIRE_ASPECT, 2),
                new ToolEnchantHelper.EnchantSpec(Enchantments.MENDING, 1));
        super.inventoryTick(stack, level, entity, slotId, isSelected);
    }
}
