package danger.orespawn.items;

import danger.orespawn.entity.LaserBall;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

/**
 * Gold {@code ItemRayGun} — registers as {@code RayGun} ({@code MyRayGun}), combat tab.
 * <p>
 * Gold behavior:
 * <ul>
 *   <li>Max stack 1, durability <b>50</b></li>
 *   <li>Right-click: refuse when remaining durability ≤ 1</li>
 *   <li>Play fireworks.launch (3.5F, 0.5F)</li>
 *   <li>Server: spawn {@link LaserBall} with {@link LaserBall#setSpecial()}, gold offset
 *       (xzoff 1.0, yoff 1.55, yaw+45), velocity ×3</li>
 *   <li>Swing arm, recoil push (1.5 xz, 0.3 y), damage item by 1</li>
 * </ul>
 * Inventory icon: {@code textures/item/raygun.png}. Register in {@code ModItems}.
 */
public class ItemRayGun extends Item {
    /** Gold {@code setMaxDamage(50)}. */
    public static final int RAYGUN_USES = 50;

    public ItemRayGun(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        // gold: if maxDamage - damage <= 1, do not fire
        if (stack.isDamageableItem() && stack.getMaxDamage() - stack.getDamageValue() <= 1) {
            return InteractionResultHolder.fail(stack);
        }

        // gold: "fireworks.launch" 3.5F, 0.5F
        level.playSound(
                null,
                player.getX(),
                player.getY(),
                player.getZ(),
                SoundEvents.FIREWORK_ROCKET_LAUNCH,
                SoundSource.PLAYERS,
                3.5F,
                0.5F);

        if (!level.isClientSide) {
            // gold ItemRayGun: LaserBall + setSpecial + setPositionAndRotation + motion * 3
            // (mirrors ThunderBolt#launchFromStaff offsets / scale)
            LaserBall lb = new LaserBall(level, player);
            lb.setSpecial();
            double xzoff = 1.0;
            double yoff = 1.55;
            float yaw = player.getYRot();
            lb.setPos(
                    player.getX() - xzoff * Math.sin(Math.toRadians(yaw + 45.0F)),
                    player.getY() + yoff,
                    player.getZ() + xzoff * Math.cos(Math.toRadians(yaw + 45.0F)));
            lb.setYRot(yaw);
            lb.setXRot(player.getXRot());
            lb.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 1.5F, 1.0F);
            lb.setDeltaMovement(lb.getDeltaMovement().scale(3.0));
            lb.hasImpulse = true;
            level.addFreshEntity(lb);

            stack.hurtAndBreak(1, player, LivingEntity.getSlotForHand(hand));
        }

        // gold: swingItem + addVelocity recoil (cos/sin yaw-90 * 1.5, 0.3 y)
        player.swing(hand, true);
        double recoilYaw = player.getYRot() - 90.0F;
        player.push(
                Math.cos(Math.toRadians(recoilYaw)) * 1.5,
                0.3,
                Math.sin(Math.toRadians(recoilYaw)) * 1.5);

        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
    }
}
