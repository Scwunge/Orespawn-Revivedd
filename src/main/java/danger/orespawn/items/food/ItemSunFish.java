package danger.orespawn.items.food;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

/**
 * Gold {@code ItemSunFish} when registered as {@code MySunFish} / {@code sunfish}
 * ({@code new ItemSunFish(id, 6, 0.6F, false)}).
 * <p>
 * Gold {@code ItemSunFish} was shared by bacon, buttercandy, crystalapple, heart, cookedcrabmeat
 * via identity checks. This port class is <b>only</b> the sunfish identity:
 * Fire Resistance 6000 ticks. Use {@link ItemBacon} / {@link ItemButterCandy} for the others.
 * <p>
 * Food props: nutrition 6, saturationModifier 0.6F, alwaysEdible.
 */
public class ItemSunFish extends Item {
    /** Gold Potion.fireResistance duration for MySunFish. */
    public static final int FIRE_RESIST_TICKS = 6000;

    public ItemSunFish(Properties properties) {
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
