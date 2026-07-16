package danger.orespawn.items.tools;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;

/**
 * Ultimate pickaxe auto-enchants: Efficiency 5 + Fortune 5 (gold) + Unbreaking 3 + Mending.
 * No Silk Touch — player can add that separately if they want ore blocks.
 */
public class UltimatePickaxe extends PickaxeItem {
    public UltimatePickaxe(Properties properties) {
        super(OrespawnToolMaterial.UltimateTools.tier, properties);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        ToolEnchantHelper.applyAllIfUnenchanted(
                stack,
                level,
                new ToolEnchantHelper.EnchantSpec(Enchantments.EFFICIENCY, 5),
                new ToolEnchantHelper.EnchantSpec(Enchantments.FORTUNE, 5),
                new ToolEnchantHelper.EnchantSpec(Enchantments.UNBREAKING, 3),
                new ToolEnchantHelper.EnchantSpec(Enchantments.MENDING, 1));
        super.inventoryTick(stack, level, entity, slotId, isSelected);
    }
}
