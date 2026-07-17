package me.scwunge.mods.portalgun.client.render;

import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat.Mode;
import me.scwunge.mods.portalgun.PortalGunMod;
import me.scwunge.mods.portalgun.config.PortalGunConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;

public final class PortalStencil {
   public static final int STENCIL_REF = 1;
   private static final int OVAL_SEGMENTS = 48;
   private static boolean available;
   private static boolean failedPermanently;
   private static boolean loggedOk;

   private PortalStencil() {
   }

   public static boolean isActive() {
      try {
         if (!(Boolean)PortalGunConfig.USE_STENCIL_APERTURE.get()) {
            return false;
         }
      } catch (Throwable var1) {
      }

      return ensureEnabled();
   }

   public static boolean ensureEnabled() {
      if (available) {
         return true;
      } else if (failedPermanently) {
         return false;
      } else {
         try {
            Minecraft mc = Minecraft.getInstance();
            if (mc == null) {
               return false;
            }

            RenderTarget main = mc.getMainRenderTarget();
            if (main == null || main.width <= 0 || main.height <= 0) {
               return false;
            }

            if (!main.isStencilEnabled()) {
               main.enableStencil();
            }

            available = main.isStencilEnabled();
            if (available) {
               if (!loggedOk) {
                  loggedOk = true;
                  PortalGunMod.LOGGER.info("Portal stencil aperture enabled (main FBO stencil buffer)");
               }
            } else {
               failedPermanently = true;
               PortalGunMod.LOGGER.warn("Portal stencil aperture unavailable — falling back to ellipse mesh");
            }
         } catch (Throwable var2) {
            failedPermanently = true;
            PortalGunMod.LOGGER.warn("Portal stencil enable failed: {}", var2.toString());
         }

         return available;
      }
   }

   public static boolean drawFboAperture(Matrix4f mat, int textureId, float halfX, float halfY, float frontZ, float backZ) {
      if (!isActive()) {
         return false;
      } else {
         GL11.glEnable(2960);
         GlStateManager._colorMask(false, false, false, false);
         GlStateManager._depthMask(false);
         GlStateManager._stencilFunc(519, 1, 255);
         GlStateManager._stencilOp(7680, 7680, 7681);
         GlStateManager._stencilMask(255);
         GlStateManager._clearStencil(0);
         GlStateManager._clear(1024, Minecraft.ON_OSX);
         RenderSystem.setShader(GameRenderer::getPositionColorShader);
         RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
         drawSolidOval(mat, halfX, halfY, frontZ, true);
         GlStateManager._colorMask(true, true, true, true);
         GlStateManager._stencilMask(0);
         GlStateManager._stencilFunc(514, 1, 255);
         GlStateManager._stencilOp(7680, 7680, 7680);
         GlStateManager._depthMask(true);
         RenderSystem.setShader(GameRenderer::getPositionTexColorShader);
         RenderSystem.setShaderTexture(0, textureId);
         RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
         drawTexturedRect(mat, halfX, halfY, frontZ, true);
         drawTexturedRect(mat, halfX, halfY, backZ, false);
         GL11.glDisable(2960);
         GlStateManager._stencilMask(255);
         GlStateManager._stencilFunc(519, 0, 255);
         GlStateManager._stencilOp(7680, 7680, 7680);
         GlStateManager._colorMask(true, true, true, true);
         GlStateManager._depthMask(true);
         return true;
      }
   }

   private static void drawSolidOval(Matrix4f mat, float hx, float hy, float z, boolean front) {
      BufferBuilder buf = Tesselator.getInstance().begin(Mode.TRIANGLE_FAN, DefaultVertexFormat.POSITION_COLOR);
      buf.addVertex(mat, 0.0F, 0.0F, z).setColor(255, 255, 255, 255);
      if (front) {
         for (int i = 0; i <= 48; i++) {
            float ang = (float)((double)i * 0.1308996938995747);
            buf.addVertex(mat, (float)Math.cos((double)ang) * hx, (float)Math.sin((double)ang) * hy, z).setColor(255, 255, 255, 255);
         }
      } else {
         for (int i = 48; i >= 0; i--) {
            float ang = (float)((double)i * 0.1308996938995747);
            buf.addVertex(mat, (float)Math.cos((double)ang) * hx, (float)Math.sin((double)ang) * hy, z).setColor(255, 255, 255, 255);
         }
      }

      BufferUploader.drawWithShader(buf.buildOrThrow());
   }

   private static void drawTexturedRect(Matrix4f mat, float hx, float hy, float z, boolean front) {
      BufferBuilder buf = Tesselator.getInstance().begin(Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
      if (front) {
         buf.addVertex(mat, -hx, -hy, z).setUv(1.0F, 0.0F).setColor(255, 255, 255, 255);
         buf.addVertex(mat, hx, -hy, z).setUv(0.0F, 0.0F).setColor(255, 255, 255, 255);
         buf.addVertex(mat, hx, hy, z).setUv(0.0F, 1.0F).setColor(255, 255, 255, 255);
         buf.addVertex(mat, -hx, hy, z).setUv(1.0F, 1.0F).setColor(255, 255, 255, 255);
      } else {
         buf.addVertex(mat, -hx, hy, z).setUv(1.0F, 1.0F).setColor(255, 255, 255, 255);
         buf.addVertex(mat, hx, hy, z).setUv(0.0F, 1.0F).setColor(255, 255, 255, 255);
         buf.addVertex(mat, hx, -hy, z).setUv(0.0F, 0.0F).setColor(255, 255, 255, 255);
         buf.addVertex(mat, -hx, -hy, z).setUv(1.0F, 0.0F).setColor(255, 255, 255, 255);
      }

      BufferUploader.drawWithShader(buf.buildOrThrow());
   }
}
