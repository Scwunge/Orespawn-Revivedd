package danger.orespawn.items.armor;

import danger.orespawn.items.tools.ToolEnchantHelper;
import danger.orespawn.items.tools.ToolEnchantHelper.EnchantSpec;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;

/**
 * Gold {@code ArmorBase} — real {@link ArmorItem} with gold material / slot.
 *
 * <p>Ultimate armor auto-applies gold 1.12 {@code ItemEnchantments} map once when unenchanted
 * (MCP stable_39 field names), plus 1.21 QoL Mending and piece-specific Soul Speed / Swift Sneak.
 * Emerald / Moth / Amethyst materials have empty gold enchant maps — no auto-enchant.
 */
public class ArmorBase extends ArmorItem {
    public ArmorBase(Holder<ArmorMaterial> material, Type type, Item.Properties properties) {
        super(material, type, properties);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        if (this.getMaterial().is(OrespawnArmorMaterial.ULTIMATE.getKey())) {
            List<EnchantSpec> specs = new ArrayList<>();
            // Gold UltimateArmor map — OrespawnArmorMaterial (MCP stable_39):
            // field_185298_f RESPIRATION 2, field_185299_g AQUA_AFFINITY 2,
            // field_180310_c PROTECTION 5, field_77329_d FIRE_PROTECTION 5,
            // field_180308_g PROJECTILE_PROTECTION 5, field_185307_s UNBREAKING 5,
            // field_180309_e FEATHER_FALLING 3
            specs.add(new EnchantSpec(Enchantments.RESPIRATION, 2));
            specs.add(new EnchantSpec(Enchantments.AQUA_AFFINITY, 2));
            specs.add(new EnchantSpec(Enchantments.PROTECTION, 5));
            specs.add(new EnchantSpec(Enchantments.FIRE_PROTECTION, 5));
            specs.add(new EnchantSpec(Enchantments.PROJECTILE_PROTECTION, 5));
            specs.add(new EnchantSpec(Enchantments.UNBREAKING, 5));
            specs.add(new EnchantSpec(Enchantments.FEATHER_FALLING, 3));
            // 1.21 QoL (not in gold map; Unbreaking already gold Unbreaking 5)
            specs.add(new EnchantSpec(Enchantments.MENDING, 1));
            // Piece-specific post-1.12 enchants
            if (this.getType() == Type.BOOTS) {
                specs.add(new EnchantSpec(Enchantments.SOUL_SPEED, 3));
            }
            if (this.getType() == Type.LEGGINGS) {
                specs.add(new EnchantSpec(Enchantments.SWIFT_SNEAK, 3));
            }
            ToolEnchantHelper.applyAllIfUnenchanted(stack, level, specs.toArray(EnchantSpec[]::new));
        }
        super.inventoryTick(stack, level, entity, slotId, isSelected);
    }
}
