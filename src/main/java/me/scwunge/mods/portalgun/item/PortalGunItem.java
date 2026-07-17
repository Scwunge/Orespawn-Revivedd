package me.scwunge.mods.portalgun.item;

import java.util.List;
import me.scwunge.mods.portalgun.client.ClientGrabStatus;
import me.scwunge.mods.portalgun.entity.PortalProjectile;
import me.scwunge.mods.portalgun.init.ModSounds;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.item.Item.TooltipContext;
import net.minecraft.world.level.Level;

public class PortalGunItem extends Item {
   public PortalGunItem(Properties properties) {
      super(properties);
   }

   public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
      ItemStack stack = player.getItemInHand(hand);
      boolean orange = player.isShiftKeyDown();
      return !tryFire(player, stack, orange) ? InteractionResultHolder.fail(stack) : InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
   }

   public static boolean tryFire(Player player, ItemStack stack, boolean orange) {
      if (stack.getItem() instanceof PortalGunItem gun) {
         if (player.getCooldowns().isOnCooldown(gun)) {
            return false;
         } else {
            PortalGunData.ensureDefaults(stack);
            Level level = player.level();
            if (!level.isClientSide) {
               PortalGunData.stampPersonalOwner(stack, player);
               String ownerKey = PortalGunData.portalOwnerKey(stack, player);
               PortalProjectile proj = new PortalProjectile(level, player);
               proj.setOwner(player);
               proj.setPortalOwnerKey(ownerKey);
               proj.setOrange(orange);
               proj.setPortalSize(PortalGunData.width(stack), PortalGunData.height(stack));
               proj.setChannel(PortalGunData.channel(stack));
               proj.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 2.5F, 0.0F);
               level.addFreshEntity(proj);
               PortalGunData.setLastOrange(stack, orange);
               level.playSound(
                  null,
                  player.getX(),
                  player.getY(),
                  player.getZ(),
                  orange ? (SoundEvent)ModSounds.GUN_FIRE_RED.get() : (SoundEvent)ModSounds.GUN_FIRE_BLUE.get(),
                  SoundSource.PLAYERS,
                  0.8F,
                  1.0F
               );
            } else {
               ClientGrabStatus.pulseFire();
            }

            player.getCooldowns().addCooldown(gun, 4);
            return true;
         }
      } else {
         return false;
      }
   }

   public static boolean tryFireSwap(Player player, ItemStack stack) {
      PortalGunData.ensureDefaults(stack);
      return tryFire(player, stack, !PortalGunData.lastOrange(stack));
   }

   public void inventoryTick(ItemStack stack, Level level, Entity entity, int slot, boolean selected) {
      if (!level.isClientSide && entity instanceof Player player && (selected || slot == 40)) {
         PortalGunData.stampPersonalOwner(stack, player);
      }
   }

   public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tip, TooltipFlag flag) {
      PortalGunData.ensureDefaults(stack);
      if (PortalGunData.isGlobal(stack)) {
         tip.add(Component.translatable("orespawn.info.global"));
      } else {
         String name = PortalGunData.ownerName(stack);
         if (!name.isEmpty()) {
            tip.add(Component.translatable("orespawn.info.owner", new Object[]{name}));
         } else {
            tip.add(Component.translatable("orespawn.info.newPortalGun"));
         }
      }

      tip.add(Component.translatable("orespawn.info.channel", new Object[]{PortalGunData.channel(stack)}));
      tip.add(Component.translatable("orespawn.info.size", new Object[]{PortalGunData.width(stack), PortalGunData.height(stack)}));
      int s = PortalGunData.grabStrength(stack);
      int tier = Math.min(4, Math.max(1, s));
      tip.add(Component.translatable("orespawn.info.grabStrength" + tier));
      tip.add(
         Component.translatable(
            "orespawn.info.last",
            new Object[]{PortalGunData.lastOrange(stack) ? Component.translatable("orespawn.colour.orange") : Component.translatable("orespawn.colour.blue")}
         )
      );
      tip.add(Component.translatable("orespawn.tip.fire"));
      tip.add(Component.translatable("orespawn.tip.grab"));
      tip.add(Component.translatable("orespawn.tip.reset"));
   }
}
