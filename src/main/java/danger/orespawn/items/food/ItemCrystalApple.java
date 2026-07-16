package danger.orespawn.items.food;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

/**
 * Gold {@code MyCrystalApple} ({@code ItemSunFish} identity).
 * Nutrition typically high; finish-eat: Regeneration 3000 + Resistance 3000 (amp 0).
 * <p>
 * Registry name gold: {@code crystalapple}. Texture: {@code textures/item/crystalapple.png}.
 * Breeding food for Whale / Lizard / Flounder / CrystalCow drops.
 */
public class ItemCrystalApple extends Item {
    public static final int EFFECT_TICKS = 3000;

    public ItemCrystalApple(Properties properties) {
        super(properties);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity livingEntity) {
        ItemStack result = super.finishUsingItem(stack, level, livingEntity);
        if (!level.isClientSide) {
            livingEntity.addEffect(new MobEffectInstance(MobEffects.REGENERATION, EFFECT_TICKS, 0));
            livingEntity.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, EFFECT_TICKS, 0));
        }
        return result;
    }
}
