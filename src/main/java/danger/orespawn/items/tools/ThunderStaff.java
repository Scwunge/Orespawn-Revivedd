package danger.orespawn.items.tools;

import danger.orespawn.entity.ThunderBolt;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

/**
 * 1.7.10 {@code ItemThunderStaff} — gold registers as
 * {@code new ItemThunderStaff(BaseItemID+240).setUnlocalizedName("thunderstaff")}.
 * <p>
 * Gold behavior:
 * <ul>
 *   <li>Max stack 1, durability <b>50</b></li>
 *   <li>Right-click: fire {@link ThunderBolt} via {@link ThunderBolt#launchFromStaff}
 *       (spawn offset + aim velocity ×3)</li>
 *   <li>Refuses to fire when remaining durability ≤ 1 (never fully breaks from use)</li>
 *   <li>Swing arm, recoil push (0.5 xz, 0.15 y), damage staff by 1</li>
 *   <li>During thunderstorm: repair 1 damage every 50 ticks while damaged</li>
 * </ul>
 * Flat inventory icon: {@code textures/item/thunderstaff.png}. No 3D IItemRenderer in gold.
 */
public class ThunderStaff extends Item {
    /** Gold {@code setMaxDamage(50)}. */
    public static final int THUNDER_STAFF_USES = 50;
    /** Gold repair ticker interval while thundering. */
    public static final int REPAIR_INTERVAL_TICKS = 50;

    public ThunderStaff(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        // gold: if maxDamage - damage <= 1, do not fire
        if (stack.isDamageableItem() && stack.getMaxDamage() - stack.getDamageValue() <= 1) {
            return InteractionResultHolder.fail(stack);
        }

        if (!level.isClientSide) {
            ThunderBolt bolt = new ThunderBolt(level, player);
            bolt.launchFromStaff(player);
            level.addFreshEntity(bolt);

            stack.hurtAndBreak(1, player, LivingEntity.getSlotForHand(hand));
        }

        // gold: swingItem + addVelocity recoil
        player.swing(hand, true);
        double yaw = player.getYRot() - 90.0F;
        player.push(
                Math.cos(Math.toRadians(yaw)) * 0.5,
                0.15,
                Math.sin(Math.toRadians(yaw)) * 0.5);

        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        // gold: if isRaining && isThundering, every 50 ticks reduce damage by 1
        // (use entity tickCount so shared Item singleton does not share a broken ticker)
        if (!level.isClientSide
                && level.isRaining()
                && level.isThundering()
                && stack.isDamaged()
                && entity.tickCount % REPAIR_INTERVAL_TICKS == 0) {
            stack.setDamageValue(Math.max(0, stack.getDamageValue() - 1));
        }
        super.inventoryTick(stack, level, entity, slotId, isSelected);
    }
}
