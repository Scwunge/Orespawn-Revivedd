package danger.orespawn.items.food;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

/**
 * Gold {@code MyLove} ({@code ItemSunFish} identity, unlocalized {@code heart}).
 * Nutrition 8 / sat 0.95. Effects: Regen IV, Resistance III, Fire Resist III,
 * Absorption II, Speed I, Jump Boost I (gold durations).
 * <p>
 * Texture: {@code textures/item/heart.png}. Register as {@code heart}.
 */
public class ItemLove extends Item {
    public static final int REGEN_TICKS = 6000;
    public static final int RESIST_TICKS = 6000;
    public static final int FIRE_TICKS = 6000;
    public static final int ABSORB_TICKS = 6000;
    public static final int SPEED_TICKS = 5000;
    public static final int JUMP_TICKS = 5000;

    public ItemLove(Properties properties) {
        super(properties);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity livingEntity) {
        ItemStack result = super.finishUsingItem(stack, level, livingEntity);
        if (!level.isClientSide) {
            livingEntity.addEffect(new MobEffectInstance(MobEffects.REGENERATION, REGEN_TICKS, 3));
            livingEntity.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, RESIST_TICKS, 2));
            livingEntity.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, FIRE_TICKS, 2));
            livingEntity.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, ABSORB_TICKS, 1));
            livingEntity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, SPEED_TICKS, 0));
            livingEntity.addEffect(new MobEffectInstance(MobEffects.JUMP, JUMP_TICKS, 0));
        }
        return result;
    }
}
