package danger.orespawn.items.tools;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;

/**
 * 1.7.10 {@code PoisonSword} — emerald-tier, auto Sharpness 1;
 * on hit applies poison / wither / weakness (gold Potion effects).
 */
public class PoisonSword extends SwordItem {
    public PoisonSword(Properties properties) {
        super(OrespawnToolMaterial.EmeraldTools.tier, properties);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        ToolEnchantHelper.applyAllIfUnenchanted(
                stack, level, new ToolEnchantHelper.EnchantSpec(Enchantments.SHARPNESS, 1));
        super.inventoryTick(stack, level, entity, slotId, isSelected);
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (target != null && !target.level().isClientSide) {
            int d = 10 + target.getRandom().nextInt(10);
            target.addEffect(new MobEffectInstance(MobEffects.POISON, d * 20, 0));
            d = 10 + target.getRandom().nextInt(10);
            target.addEffect(new MobEffectInstance(MobEffects.WITHER, d * 20, 0));
            d = 10 + target.getRandom().nextInt(10);
            target.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, d * 20, 0));
        }
        return super.hurtEnemy(stack, target, attacker);
    }
}
