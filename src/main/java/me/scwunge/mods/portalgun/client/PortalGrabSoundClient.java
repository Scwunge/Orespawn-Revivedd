package me.scwunge.mods.portalgun.client;

import me.scwunge.mods.portalgun.client.sound.PortalGunGrabSound;
import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent.LoggingOut;
import net.neoforged.neoforge.client.event.ClientTickEvent.Post;

@EventBusSubscriber(
   modid = "orespawn",
   value = {Dist.CLIENT}
)
public final class PortalGrabSoundClient {
   private static PortalGunGrabSound active;
   private static boolean wasHolding;

   private PortalGrabSoundClient() {
   }

   @SubscribeEvent
   public static void onClientTick(Post event) {
      Minecraft mc = Minecraft.getInstance();
      if (mc.player != null && mc.level != null) {
         boolean holding = ClientGrabStatus.isHolding();
         if (holding && !wasHolding) {
            start();
         } else if (!holding && wasHolding) {
            stop();
         } else if (holding && (active == null || active.isStopped())) {
            start();
         }

         wasHolding = holding;
      } else {
         stop();
         wasHolding = false;
      }
   }

   @SubscribeEvent
   public static void onLogout(LoggingOut event) {
      stop();
      wasHolding = false;
      ClientGrabStatus.clear();
   }

   private static void start() {
      stop();
      Minecraft mc = Minecraft.getInstance();
      if (mc.getSoundManager() != null) {
         active = new PortalGunGrabSound();
         mc.getSoundManager().play(active);
      }
   }

   private static void stop() {
      if (active != null) {
         Minecraft mc = Minecraft.getInstance();
         if (mc.getSoundManager() != null) {
            mc.getSoundManager().stop(active);
         }

         active = null;
      }
   }
}
