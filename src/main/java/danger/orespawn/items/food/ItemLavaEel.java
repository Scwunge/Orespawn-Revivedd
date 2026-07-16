package danger.orespawn.items.food;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

/**
 * Gold {@code ItemLavaEel} — registers as {@code lavaeel}
 * ({@code new ItemLavaEel(id, 2, 0.6F, false)}).
 * <p>
 * Gold behavior:
 * <ul>
 *   <li>{@code setAlwaysEdible()}</li>
 *   <li>On finish eat: Fire Resistance 600 ticks, amplifier 0</li>
 * </ul>
 * Food props: nutrition 2, saturationModifier 0.6F, alwaysEdible.
 */
public class ItemLavaEel extends Item {
    /** Gold Potion.fireResistance duration. */
    public static final int FIRE_RESIST_TICKS = 600;

    public ItemLavaEel(Properties properties) {
        super(properties);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity livingEntity) {
        ItemStack result = super.finishUsingItem(stack, level, livingEntity);
        if (!level.isClientSide) {
            livingEntity.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, FIRE_RESIST_TICKS, 0));
        }
        return result;
    }
}
