package me.scwunge.mods.portalgun.client;

import me.scwunge.mods.portalgun.config.PortalGunConfig;
import me.scwunge.mods.portalgun.item.PortalGunItem;
import me.scwunge.mods.portalgun.portal.info.ChannelIndicator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent.LoggingOut;
import net.neoforged.neoforge.client.event.RenderGuiEvent.Post;

@EventBusSubscriber(
   modid = "orespawn",
   value = {Dist.CLIENT}
)
public final class PortalGunHud {
   private static final ResourceLocation TEX_EMPTY_L = ResourceLocation.fromNamespaceAndPath("orespawn", "textures/overlay/lempty.png");
   private static final ResourceLocation TEX_FULL_L = ResourceLocation.fromNamespaceAndPath("orespawn", "textures/overlay/lfull.png");
   private static final ResourceLocation TEX_EMPTY_R = ResourceLocation.fromNamespaceAndPath("orespawn", "textures/overlay/rempty.png");
   private static final ResourceLocation TEX_FULL_R = ResourceLocation.fromNamespaceAndPath("orespawn", "textures/overlay/rfull.png");
   private static final int TEX_SIZE = 256;

   private PortalGunHud() {
   }

   @SubscribeEvent
   public static void onRenderGui(Post event) {
      Minecraft mc = Minecraft.getInstance();
      if (mc.player != null && !mc.options.hideGui && mc.screen == null) {
         if (holdingGun(mc)) {
            int indicatorPct = 30;

            try {
               indicatorPct = (Integer)PortalGunConfig.INDICATOR_SIZE.get();
            } catch (Throwable var16) {
            }

            if (indicatorPct > 0) {
               GuiGraphics g = event.getGuiGraphics();
               int sw = mc.getWindow().getGuiScaledWidth();
               int sh = mc.getWindow().getGuiScaledHeight();
               int size = Mth.clamp(Math.min(sw, sh) * indicatorPct / 100, 16, 128);
               int x = (sw - size) / 2;
               int y = (sh - size) / 2;
               ChannelIndicator ind = ClientChannelIndicators.localHeld();
               boolean hasA = ind.portalAAvailable || ClientPortalStatus.hasBlue();
               boolean hasB = ind.portalBAvailable || ClientPortalStatus.hasOrange();
               ResourceLocation left = hasA ? TEX_FULL_L : TEX_EMPTY_L;
               ResourceLocation right = hasB ? TEX_FULL_R : TEX_EMPTY_R;
               int colA = ind.info.colourA != -1 ? ind.info.colourA : 360703;
               int colB = ind.info.colourB != -1 ? ind.info.colourB : 16751110;
               blitOverlayTinted(g, left, x, hasA ? y - 1 : y, size, colA);
               blitOverlayTinted(g, right, x, y, size, colB);
            }
         }
      }
   }

   private static void blitOverlayTinted(GuiGraphics g, ResourceLocation tex, int x, int y, int size, int rgb) {
      float r = (float)(rgb >> 16 & 0xFF) / 255.0F;
      float gr = (float)(rgb >> 8 & 0xFF) / 255.0F;
      float b = (float)(rgb & 0xFF) / 255.0F;
      g.setColor(r, gr, b, 1.0F);
      g.blit(tex, x, y, size, size, 0.0F, 0.0F, 256, 256, 256, 256);
      g.setColor(1.0F, 1.0F, 1.0F, 1.0F);
   }

   private static boolean holdingGun(Minecraft mc) {
      ItemStack main = mc.player.getMainHandItem();
      ItemStack off = mc.player.getOffhandItem();
      return main.getItem() instanceof PortalGunItem || off.getItem() instanceof PortalGunItem;
   }

   @SubscribeEvent
   public static void onLoggingOut(LoggingOut event) {
      ClientPortalStatus.clear();
      ClientChannelIndicators.clear();
   }
}
