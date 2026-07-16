package danger.orespawn.items.tools;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;

/** 1.7.10 {@code NightmareSword} — auto Sharpness / Knockback / Fire Aspect. */
public class NightmareSword extends SwordItem {
    public NightmareSword(Properties properties) {
        super(OrespawnToolMaterial.NightmareTools.tier, properties);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        ToolEnchantHelper.applyAllIfUnenchanted(
                stack,
                level,
                new ToolEnchantHelper.EnchantSpec(Enchantments.SHARPNESS, 1),
                new ToolEnchantHelper.EnchantSpec(Enchantments.KNOCKBACK, 3),
                new ToolEnchantHelper.EnchantSpec(Enchantments.FIRE_ASPECT, 1));
        super.inventoryTick(stack, level, entity, slotId, isSelected);
    }
}
