package danger.orespawn.items.tools;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;

/** Applies gold on-inventory auto-enchantments once when the stack has none. */
public final class ToolEnchantHelper {
    private ToolEnchantHelper() {}

    public static void applyIfUnenchanted(ItemStack stack, Level level, ResourceKey<Enchantment> key, int levelValue) {
        if (level.isClientSide() || stack.isEmpty() || stack.isEnchanted()) {
            return;
        }
        Holder.Reference<Enchantment> holder =
                level.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(key);
        stack.enchant(holder, levelValue);
    }

    @SafeVarargs
    public static void applyAllIfUnenchanted(ItemStack stack, Level level, EnchantSpec... specs) {
        if (level.isClientSide() || stack.isEmpty() || stack.isEnchanted()) {
            return;
        }
        for (EnchantSpec spec : specs) {
            Holder.Reference<Enchantment> holder =
                    level.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(spec.key());
            stack.enchant(holder, spec.level());
        }
    }

    public record EnchantSpec(ResourceKey<Enchantment> key, int level) {}
}
