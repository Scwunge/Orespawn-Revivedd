package me.scwunge.mods.portalgun.client;

import me.scwunge.mods.portalgun.item.PortalGunItem;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent.LoggingOut;
import net.neoforged.neoforge.client.event.ClientTickEvent.Post;
import net.neoforged.neoforge.client.event.ViewportEvent.ComputeFov;

@EventBusSubscriber(
   modid = "orespawn",
   value = {Dist.CLIENT}
)
public final class PortalGunZoom {
   private static final float ZOOM_MIN_FACTOR = 0.1F;
   private static final int ZOOM_TICKS = 5;
   private static boolean zoomWanted;
   private static int zoomCounter = -1;

   private PortalGunZoom() {
   }

   public static void toggle() {
      Minecraft mc = Minecraft.getInstance();
      if (mc.player != null && holdingGun(mc)) {
         zoomWanted = !zoomWanted;
         if (zoomWanted && zoomCounter < 0) {
            zoomCounter = 0;
         }
      }
   }

   public static boolean isZooming() {
      return zoomWanted || zoomCounter > 0;
   }

   public static float zoomAmount(float partialTick) {
      if (zoomCounter < 0) {
         return 0.0F;
      } else {
         float t;
         if (zoomWanted) {
            t = Mth.clamp(((float)zoomCounter + partialTick) / 5.0F, 0.0F, 1.0F);
         } else {
            t = Mth.clamp(((float)zoomCounter - partialTick) / 5.0F, 0.0F, 1.0F);
         }

         return (float)Math.sin(Math.toRadians(90.0 * (double)t));
      }
   }

   @SubscribeEvent
   public static void onClientTick(Post event) {
      Minecraft mc = Minecraft.getInstance();
      if (mc.player != null && !mc.isPaused()) {
         if (zoomWanted) {
            if (!holdingGun(mc)) {
               zoomWanted = false;
            } else if (zoomCounter < 5) {
               zoomCounter++;
            }
         } else if (zoomCounter > -1) {
            zoomCounter--;
            if (zoomCounter < 0) {
               zoomCounter = -1;
            }
         }
      }
   }

   @SubscribeEvent
   public static void onFov(ComputeFov event) {
      if (zoomCounter >= 0) {
         float amt = zoomAmount((float)event.getPartialTick());
         if (!(amt <= 1.0E-4F)) {
            double factor = 0.1F + 0.8999999985098839 * (1.0 - (double)amt);
            event.setFOV(event.getFOV() * factor);
         }
      }
   }

   @SubscribeEvent
   public static void onLogout(LoggingOut event) {
      zoomWanted = false;
      zoomCounter = -1;
   }

   private static boolean holdingGun(Minecraft mc) {
      ItemStack main = mc.player.getMainHandItem();
      ItemStack off = mc.player.getOffhandItem();
      return main.getItem() instanceof PortalGunItem || off.getItem() instanceof PortalGunItem;
   }
}
