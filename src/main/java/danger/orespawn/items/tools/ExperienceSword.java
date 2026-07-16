package danger.orespawn.items.tools;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;

/**
 * 1.7.10 {@code ExperienceSword} — emerald-tier; auto Sharpness 2 + Looting 3
 * (gold field_77338_j / field_77347_r). Full armor-XP tick later with experience armor.
 */
public class ExperienceSword extends SwordItem {
    public ExperienceSword(Properties properties) {
        super(OrespawnToolMaterial.EmeraldTools.tier, properties);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        ToolEnchantHelper.applyAllIfUnenchanted(
                stack,
                level,
                new ToolEnchantHelper.EnchantSpec(Enchantments.SHARPNESS, 2),
                new ToolEnchantHelper.EnchantSpec(Enchantments.LOOTING, 3));
        super.inventoryTick(stack, level, entity, slotId, isSelected);
    }
}
