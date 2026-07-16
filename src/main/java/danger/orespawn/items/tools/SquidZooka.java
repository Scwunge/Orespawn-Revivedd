package danger.orespawn.items.tools;

import danger.orespawn.entity.AttackSquid;
import danger.orespawn.init.ModEntities;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

/**
 * 1.7.10 {@code ItemSquidZooka} — gold registers as
 * {@code new ItemSquidZooka(BaseItemID+317).setUnlocalizedName("squidzookasmall")}
 * with dedicated {@code ModelSquidZooka}/{@code RenderSquidZooka} (IItemRenderer).
 * <p>
 * Gold behavior:
 * <ul>
 *   <li>Max stack 1, durability <b>100</b></li>
 *   <li>Right-click: spawn {@link AttackSquid}, {@link AttackSquid#setWasShot()}, launch at 3.6F
 *       along look with ±0.05 jitter</li>
 *   <li>Spawn offset: xzoff 2.5, yoff 1.65, yaw+15°</li>
 *   <li>Sound {@code random.explode} 0.5/0.5; swing; recoil 0.45 xz / 0.1 y; damage 1</li>
 *   <li>Refuses to fire when remaining durability ≤ 1</li>
 * </ul>
 * Inventory icon: {@code squidzookasmall}; in-hand texture: {@code squidzookatexture.png}.
 */
public class SquidZooka extends Item {
    /** Gold {@code setMaxDamage(100)}. */
    public static final int SQUID_ZOOKA_USES = 100;
    /** Gold launch speed {@code f = 3.6F}. */
    public static final float LAUNCH_SPEED = 3.6F;
    /** Gold xz spawn offset. */
    public static final double XZ_OFFSET = 2.5;
    /** Gold y spawn offset. */
    public static final double Y_OFFSET = 1.65;

    public SquidZooka(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        // gold: if maxDamage - damage <= 1, do not fire
        if (stack.isDamageableItem() && stack.getMaxDamage() - stack.getDamageValue() <= 1) {
            return InteractionResultHolder.fail(stack);
        }

        level.playSound(
                null,
                player.getX(),
                player.getY(),
                player.getZ(),
                SoundEvents.GENERIC_EXPLODE.value(),
                SoundSource.PLAYERS,
                0.5F,
                0.5F);

        if (!level.isClientSide) {
            float yaw = player.getYRot();
            double x = player.getX() - XZ_OFFSET * Math.sin(Math.toRadians(yaw + 15.0F));
            double y = player.getY() + Y_OFFSET;
            double z = player.getZ() + XZ_OFFSET * Math.cos(Math.toRadians(yaw + 15.0F));

            AttackSquid squid = ModEntities.ATTACK_SQUID.get().create(level);
            if (squid != null) {
                squid.moveTo(x, y, z, level.random.nextFloat() * 360.0F, 0.0F);
                squid.setWasShot();

                // gold motion from rotationYaw / rotationPitch * f
                float pitch = player.getXRot();
                double mx = -Mth.sin(yaw * ((float) Math.PI / 180.0F))
                        * Mth.cos(pitch * ((float) Math.PI / 180.0F))
                        * LAUNCH_SPEED;
                double mz = Mth.cos(yaw * ((float) Math.PI / 180.0F))
                        * Mth.cos(pitch * ((float) Math.PI / 180.0F))
                        * LAUNCH_SPEED;
                double my = -Mth.sin(pitch * ((float) Math.PI / 180.0F)) * LAUNCH_SPEED;

                mx += (level.random.nextFloat() - level.random.nextFloat()) * 0.05;
                my += (level.random.nextFloat() - level.random.nextFloat()) * 0.05;
                mz += (level.random.nextFloat() - level.random.nextFloat()) * 0.05;
                squid.setDeltaMovement(mx, my, mz);
                squid.hasImpulse = true;

                level.addFreshEntity(squid);
            }

            stack.hurtAndBreak(1, player, LivingEntity.getSlotForHand(hand));
        }

        // gold: swingItem + addVelocity recoil
        player.swing(hand, true);
        double recoilYaw = player.getYRot() - 90.0F;
        player.push(
                Math.cos(Math.toRadians(recoilYaw)) * 0.45,
                0.1,
                Math.sin(Math.toRadians(recoilYaw)) * 0.45);

        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
    }
}
