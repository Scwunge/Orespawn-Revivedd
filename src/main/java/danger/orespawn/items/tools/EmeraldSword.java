package danger.orespawn.items.tools;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;

/**
 * Gold: no auto-enchants.
 * 1.21 QoL: Unbreaking 3 + Mending 1 when unenchanted.
 */
public class EmeraldSword extends SwordItem {
    public EmeraldSword(Properties properties) {
        super(OrespawnToolMaterial.EmeraldTools.tier, properties);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        ToolEnchantHelper.applyAllIfUnenchanted(
                stack,
                level,
                new ToolEnchantHelper.EnchantSpec(Enchantments.UNBREAKING, 3),
                new ToolEnchantHelper.EnchantSpec(Enchantments.MENDING, 1));
        super.inventoryTick(stack, level, entity, slotId, isSelected);
    }
}
