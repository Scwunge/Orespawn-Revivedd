package danger.orespawn.items.food;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

/**
 * Butter candy — gold registered {@code MyButterCandy} as {@code ItemSunFish}
 * ({@code new ItemSunFish(id, 4, 0.5F, false).setUnlocalizedName("buttercandy")}).
 * <p>
 * Gold {@code ItemSunFish} MyButterCandy branch on finish eat:
 * <ul>
 *   <li>Speed 2000 ticks, amp 0 ({@code Potion.field_76424_c} / moveSpeed)</li>
 *   <li>Jump Boost 2000 ticks, amp 0 ({@code Potion.field_76430_j})</li>
 *   <li>Always edible (ItemSunFish ctor)</li>
 * </ul>
 * Food props: nutrition 4, saturationModifier 0.5F, alwaysEdible.
 * Registry name gold: {@code buttercandy}.
 */
public class ItemButterCandy extends Item {
    /** Gold potion duration for MyButterCandy effects. */
    public static final int EFFECT_TICKS = 2000;

    public ItemButterCandy(Properties properties) {
        super(properties);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity livingEntity) {
        ItemStack result = super.finishUsingItem(stack, level, livingEntity);
        if (!level.isClientSide) {
            livingEntity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, EFFECT_TICKS, 0));
            livingEntity.addEffect(new MobEffectInstance(MobEffects.JUMP, EFFECT_TICKS, 0));
        }
        return result;
    }
}
