package danger.orespawn.items.food;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

/**
 * Cooked bacon — gold registered {@code MyBacon} as {@code ItemSunFish}
 * ({@code new ItemSunFish(id, 14, 1.5F, false).setUnlocalizedName("cookedbacon")}).
 * <p>
 * Gold {@code ItemSunFish} MyBacon branch on finish eat:
 * <ul>
 *   <li>Regeneration 2000 ticks, amp 0 ({@code Potion.field_76428_l})</li>
 *   <li>Strength / damageBoost 2000 ticks, amp 0 ({@code Potion.field_76420_g})</li>
 *   <li>Always edible (ItemSunFish ctor)</li>
 * </ul>
 * Food props: nutrition 14, saturationModifier 1.5F, alwaysEdible.
 * Registry name gold: {@code cookedbacon}.
 */
public class ItemBacon extends Item {
    /** Gold potion duration for MyBacon effects. */
    public static final int EFFECT_TICKS = 2000;

    public ItemBacon(Properties properties) {
        super(properties);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity livingEntity) {
        ItemStack result = super.finishUsingItem(stack, level, livingEntity);
        if (!level.isClientSide) {
            livingEntity.addEffect(new MobEffectInstance(MobEffects.REGENERATION, EFFECT_TICKS, 0));
            livingEntity.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, EFFECT_TICKS, 0));
        }
        return result;
    }
}
