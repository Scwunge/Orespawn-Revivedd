package danger.orespawn.items.tools;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;

/**
 * Gold UltimateAxe — Efficiency 5 when unenchanted.
 * Plus 1.21 QoL: Fortune (leaves/apples), Unbreaking, Mending.
 */
public class UltimateAxe extends AxeItem {
    public UltimateAxe(Properties properties) {
        super(OrespawnToolMaterial.UltimateTools.tier, properties);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        ToolEnchantHelper.applyAllIfUnenchanted(
                stack,
                level,
                // gold
                new ToolEnchantHelper.EnchantSpec(Enchantments.EFFICIENCY, 5),
                // 1.21 QoL
                new ToolEnchantHelper.EnchantSpec(Enchantments.FORTUNE, 3),
                new ToolEnchantHelper.EnchantSpec(Enchantments.UNBREAKING, 3),
                new ToolEnchantHelper.EnchantSpec(Enchantments.MENDING, 1));
        super.inventoryTick(stack, level, entity, slotId, isSelected);
    }
}
