package danger.orespawn.items.food;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

/**
 * Gold {@code ItemGenericFish} — used for greenfish / bluefish / pinkfish / rockfish /
 * woodfish / greyfish with per-item nutrition/sat from registration.
 * <p>
 * Gold behavior:
 * <ul>
 *   <li>Not always edible</li>
 *   <li>On finish eat: 25% chance Hunger 20 ticks (gold {@code nextInt(4) == 1})</li>
 * </ul>
 * Per-variant food props (nutrition/sat only — class handles hunger chance).
 * <pre>
 * greenfish  3 / 0.5F
 * bluefish   4 / 0.4F
 * pinkfish   4 / 0.6F
 * rockfish   3 / 0.7F
 * woodfish   5 / 0.7F
 * greyfish   5 / 0.5F
 * </pre>
 */
public class ItemGenericFish extends Item {
    /** Gold Potion.hunger duration. */
    public static final int HUNGER_TICKS = 20;

    public ItemGenericFish(Properties properties) {
        super(properties);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity livingEntity) {
        ItemStack result = super.finishUsingItem(stack, level, livingEntity);
        // gold: nextInt(4) == 1 → 25% hunger
        if (!level.isClientSide && level.random.nextInt(4) == 1) {
            livingEntity.addEffect(new MobEffectInstance(MobEffects.HUNGER, HUNGER_TICKS, 0));
        }
        return result;
    }
}
