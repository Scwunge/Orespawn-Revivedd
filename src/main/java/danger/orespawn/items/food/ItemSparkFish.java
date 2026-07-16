package danger.orespawn.items.food;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

/**
 * Gold {@code ItemSparkFish} — registers as {@code sparkfish}
 * ({@code new ItemSparkFish(id, 1, 0.2F, false)}).
 * <p>
 * Gold behavior:
 * <ul>
 *   <li>{@code setAlwaysEdible()}</li>
 *   <li>On finish eat: Fire Resistance 100 ticks, amplifier 0</li>
 * </ul>
 * Food props: nutrition 1, saturationModifier 0.2F, alwaysEdible.
 */
public class ItemSparkFish extends Item {
    /** Gold Potion.fireResistance duration. */
    public static final int FIRE_RESIST_TICKS = 100;

    public ItemSparkFish(Properties properties) {
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
