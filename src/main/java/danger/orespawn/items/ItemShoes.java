package danger.orespawn.items;

import danger.orespawn.entity.Shoes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

/**
 * Gold {@code ItemShoes} — throwable shoe projectile with fixed shoe type id.
 * <p>
 * Gold registrations (stack 64, materials tab):
 * <ul>
 *   <li>MyItemShoes → {@code redheels}, shoeId <b>2</b></li>
 *   <li>MyItemShoes_1 → {@code blackheels}, shoeId <b>3</b></li>
 *   <li>MyItemShoes_2 → {@code slippers}, shoeId <b>4</b></li>
 *   <li>MyItemShoes_3 → {@code boots}, shoeId <b>5</b></li>
 *   <li>MyItemGameController → {@code gamecontroller}, shoeId <b>6</b></li>
 * </ul>
 * Right-click throws {@link Shoes} with gold launch velocity 1.5 and bow SFX.
 * <p>
 * Inventory icons: {@code textures/item/redheels.png} etc.
 */
public class ItemShoes extends Item {
    /** Gold {@code my_id} passed to {@link Shoes#Shoes(Level, LivingEntity, int)}. */
    private final int shoeId;

    public ItemShoes(Properties properties, int shoeId) {
        super(properties);
        this.shoeId = shoeId;
    }

    public int getShoeId() {
        return this.shoeId;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        // gold: "random.bow" 0.5F, 0.4F / (rand * 0.4F + 0.8F)
        level.playSound(
                null,
                player.getX(),
                player.getY(),
                player.getZ(),
                SoundEvents.ARROW_SHOOT,
                SoundSource.PLAYERS,
                0.5F,
                0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F));

        if (!level.isClientSide) {
            Shoes shoes = new Shoes(level, player, this.shoeId);
            shoes.launchFrom(player);
            level.addFreshEntity(shoes);
        }

        if (!player.getAbilities().instabuild) {
            stack.shrink(1);
        }

        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
    }
}
