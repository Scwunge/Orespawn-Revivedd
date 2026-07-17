package me.scwunge.mods.portalgun.client;

import com.mojang.blaze3d.platform.InputConstants.Type;
import me.scwunge.mods.portalgun.item.PortalGunData;
import me.scwunge.mods.portalgun.item.PortalGunItem;
import me.scwunge.mods.portalgun.network.PortalGunActionPayload;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.EventBusSubscriber.Bus;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent.Post;
import net.neoforged.neoforge.client.settings.KeyConflictContext;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent.LeftClickEmpty;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent.RightClickItem;
import net.neoforged.neoforge.network.PacketDistributor;

public final class PortalGunKeys {
   public static final KeyMapping GRAB = new KeyMapping("key.orespawn.portal_gun.grab", KeyConflictContext.IN_GAME, Type.KEYSYM, 71, "key.categories.orespawn");
   public static final KeyMapping RESET = new KeyMapping("key.orespawn.portal_gun.reset", KeyConflictContext.IN_GAME, Type.KEYSYM, 82, "key.categories.orespawn");
   public static final KeyMapping SIZE_UP = new KeyMapping("key.orespawn.portal_gun.size_up", KeyConflictContext.IN_GAME, Type.KEYSYM, 93, "key.categories.orespawn");
   public static final KeyMapping SIZE_DOWN = new KeyMapping("key.orespawn.portal_gun.size_down", KeyConflictContext.IN_GAME, Type.KEYSYM, 91, "key.categories.orespawn");
   public static final KeyMapping WIDTH_UP = new KeyMapping("key.orespawn.portal_gun.width_up", KeyConflictContext.IN_GAME, Type.KEYSYM, 61, "key.categories.orespawn");
   public static final KeyMapping WIDTH_DOWN = new KeyMapping(
      "key.orespawn.portal_gun.width_down", KeyConflictContext.IN_GAME, Type.KEYSYM, 45, "key.categories.orespawn"
   );
   public static final KeyMapping CHANNEL = new KeyMapping("key.orespawn.portal_gun.channel", KeyConflictContext.IN_GAME, Type.KEYSYM, 67, "key.categories.orespawn");
   public static final KeyMapping GRAB_STRENGTH_UP = new KeyMapping(
      "key.orespawn.portal_gun.grab_strength_up", KeyConflictContext.IN_GAME, Type.KEYSYM, 86, "key.categories.orespawn"
   );
   public static final KeyMapping GRAB_STRENGTH_DOWN = new KeyMapping(
      "key.orespawn.portal_gun.grab_strength_down", KeyConflictContext.IN_GAME, Type.KEYSYM, 66, "key.categories.orespawn"
   );
   public static final KeyMapping RENAME_CHANNEL = new KeyMapping(
      "key.orespawn.portal_gun.rename_channel", KeyConflictContext.IN_GAME, Type.KEYSYM, 78, "key.categories.orespawn"
   );
   public static final KeyMapping ZOOM = new KeyMapping("key.orespawn.portal_gun.zoom", KeyConflictContext.IN_GAME, Type.KEYSYM, 90, "key.categories.orespawn");
   private static boolean holdingReset;
   private static boolean resetDidPartial;

   private PortalGunKeys() {
   }

   public static boolean isResetHeld() {
      return RESET.isDown() || holdingReset;
   }

   public static void markResetPartialFizzle() {
      resetDidPartial = true;
      holdingReset = true;
   }

   @EventBusSubscriber(
      modid = "orespawn",
      value = {Dist.CLIENT},
      bus = Bus.MOD
   )
   public static final class Register {
      private Register() {
      }

      @SubscribeEvent
      public static void registerKeys(RegisterKeyMappingsEvent event) {
         event.register(PortalGunKeys.GRAB);
         event.register(PortalGunKeys.RESET);
         event.register(PortalGunKeys.SIZE_UP);
         event.register(PortalGunKeys.SIZE_DOWN);
         event.register(PortalGunKeys.WIDTH_UP);
         event.register(PortalGunKeys.WIDTH_DOWN);
         event.register(PortalGunKeys.CHANNEL);
         event.register(PortalGunKeys.GRAB_STRENGTH_UP);
         event.register(PortalGunKeys.GRAB_STRENGTH_DOWN);
         event.register(PortalGunKeys.RENAME_CHANNEL);
         event.register(PortalGunKeys.ZOOM);
      }
   }

   @EventBusSubscriber(
      modid = "orespawn",
      value = {Dist.CLIENT}
   )
   public static final class Tick {
      private static int statusRefreshTicks;

      private Tick() {
      }

      @SubscribeEvent
      public static void onClientTick(Post event) {
         Minecraft mc = Minecraft.getInstance();
         ClientGrabStatus.tick();
         if (mc.player != null && mc.level != null && mc.screen == null) {
            if (!holdingGun(mc)) {
               statusRefreshTicks = 0;
               PortalGunKeys.holdingReset = false;
               PortalGunKeys.resetDidPartial = false;
            } else {
               if (++statusRefreshTicks >= 40) {
                  statusRefreshTicks = 0;
                  PacketDistributor.sendToServer(new PortalGunActionPayload(10), new CustomPacketPayload[0]);
               }

               while (PortalGunKeys.GRAB.consumeClick()) {
                  PacketDistributor.sendToServer(new PortalGunActionPayload(0), new CustomPacketPayload[0]);
               }

               if (PortalGunKeys.RESET.isDown()) {
                  PortalGunKeys.holdingReset = true;
               } else if (PortalGunKeys.holdingReset) {
                  if (!PortalGunKeys.resetDidPartial) {
                     PacketDistributor.sendToServer(new PortalGunActionPayload(1), new CustomPacketPayload[0]);
                  }

                  PortalGunKeys.holdingReset = false;
                  PortalGunKeys.resetDidPartial = false;
               }

               while (PortalGunKeys.RESET.consumeClick()) {
               }

               while (PortalGunKeys.SIZE_UP.consumeClick()) {
                  PacketDistributor.sendToServer(new PortalGunActionPayload(2), new CustomPacketPayload[0]);
               }

               while (PortalGunKeys.SIZE_DOWN.consumeClick()) {
                  PacketDistributor.sendToServer(new PortalGunActionPayload(3), new CustomPacketPayload[0]);
               }

               while (PortalGunKeys.WIDTH_UP.consumeClick()) {
                  PacketDistributor.sendToServer(new PortalGunActionPayload(8), new CustomPacketPayload[0]);
               }

               while (PortalGunKeys.WIDTH_DOWN.consumeClick()) {
                  PacketDistributor.sendToServer(new PortalGunActionPayload(9), new CustomPacketPayload[0]);
               }

               while (PortalGunKeys.CHANNEL.consumeClick()) {
                  PacketDistributor.sendToServer(new PortalGunActionPayload(4), new CustomPacketPayload[0]);
               }

               while (PortalGunKeys.GRAB_STRENGTH_UP.consumeClick()) {
                  PacketDistributor.sendToServer(new PortalGunActionPayload(5), new CustomPacketPayload[0]);
               }

               while (PortalGunKeys.GRAB_STRENGTH_DOWN.consumeClick()) {
                  PacketDistributor.sendToServer(new PortalGunActionPayload(6), new CustomPacketPayload[0]);
               }

               while (PortalGunKeys.RENAME_CHANNEL.consumeClick()) {
                  ItemStack gun = heldGun(mc);
                  if (!gun.isEmpty()) {
                     String ch = PortalGunData.channel(gun);
                     mc.setScreen(new PortalChannelScreen(ch));
                  }
               }

               while (PortalGunKeys.ZOOM.consumeClick()) {
                  PortalGunZoom.toggle();
               }
            }
         }
      }

      @SubscribeEvent
      public static void onLeftClickEmpty(LeftClickEmpty event) {
         if (event.getItemStack().getItem() instanceof PortalGunItem) {
            if (PortalGunKeys.isResetHeld()) {
               PortalGunKeys.markResetPartialFizzle();
               PacketDistributor.sendToServer(new PortalGunActionPayload(11), new CustomPacketPayload[0]);
            } else {
               PacketDistributor.sendToServer(new PortalGunActionPayload(7), new CustomPacketPayload[0]);
            }
         }
      }

      @SubscribeEvent
      public static void onRightClickItem(RightClickItem event) {
         if (event.getItemStack().getItem() instanceof PortalGunItem) {
            if (PortalGunKeys.isResetHeld()) {
               PortalGunKeys.markResetPartialFizzle();
               PacketDistributor.sendToServer(new PortalGunActionPayload(12), new CustomPacketPayload[0]);
               event.setCanceled(true);
               event.setCancellationResult(InteractionResult.SUCCESS);
            }
         }
      }

      private static boolean holdingGun(Minecraft mc) {
         return !heldGun(mc).isEmpty();
      }

      private static ItemStack heldGun(Minecraft mc) {
         ItemStack main = mc.player.getMainHandItem();
         if (main.getItem() instanceof PortalGunItem) {
            return main;
         } else {
            ItemStack off = mc.player.getOffhandItem();
            return off.getItem() instanceof PortalGunItem ? off : ItemStack.EMPTY;
         }
      }
   }
}

