package danger.orespawn.items.food;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

/**
 * Gold {@code ItemFireFish} — registers as {@code firefish}
 * ({@code new ItemFireFish(id, 4, 0.6F, false)}).
 * <p>
 * Gold behavior:
 * <ul>
 *   <li>{@code setAlwaysEdible()} ({@code func_77848_i})</li>
 *   <li>On finish eat: Fire Resistance 1200 ticks, amplifier 0</li>
 * </ul>
 * Food props: nutrition 4, saturationModifier 0.6F, alwaysEdible.
 * Effect is applied here (gold {@code onFoodEaten}).
 */
public class ItemFireFish extends Item {
    /** Gold Potion.fireResistance duration. */
    public static final int FIRE_RESIST_TICKS = 1200;

    public ItemFireFish(Properties properties) {
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
