package me.scwunge.mods.portalgun.client.render;

import com.mojang.blaze3d.platform.GlStateManager.DestFactor;
import com.mojang.blaze3d.platform.GlStateManager.SourceFactor;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.PoseStack.Pose;
import com.mojang.blaze3d.vertex.VertexFormat.Mode;
import com.mojang.math.Axis;
import java.util.HashMap;
import java.util.Map;
import me.scwunge.mods.portalgun.PortalGunMod;
import me.scwunge.mods.portalgun.block.entity.PortalMasterBlockEntity;
import me.scwunge.mods.portalgun.client.ClientPortalStatus;
import me.scwunge.mods.portalgun.config.PortalGunConfig;
import me.scwunge.mods.portalgun.portal.PortalGunHelper;
import me.scwunge.mods.portalgun.portal.PortalSavedData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.MultiBufferSource.BufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider.Context;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class PortalMasterRenderer implements BlockEntityRenderer<PortalMasterBlockEntity> {
   private static final ResourceLocation TEX_OPEN = ResourceLocation.fromNamespaceAndPath("orespawn", "textures/block/portalopen.png");
   private static final ResourceLocation TEX_OUTLINE = ResourceLocation.fromNamespaceAndPath("orespawn", "textures/block/portaloutline.png");
   private static final ResourceLocation TEX_CLOSED = ResourceLocation.fromNamespaceAndPath("orespawn", "textures/block/portalclose.png");
   private static final float WALL_FLUSH = 0.49F;
   private static final float FLOOR_FLUSH = 0.47F;
   private static final double NEAR_RANGE_SQ = 576.0;
   private static final Map<Long, Long> LAST_PARTICLE_TICK = new HashMap<>();
   private static final int OVAL_SEGMENTS = 28;

   public PortalMasterRenderer(Context context) {
   }

   public void render(PortalMasterBlockEntity be, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
      Level level = be.getLevel();
      if (level != null && level.isClientSide) {
         if (be.isOrigin()) {
            if (PortalSeeThrough.allowPortalDisplay()) {
               try {
                  this.renderPortal(be, partialTick, poseStack, bufferSource, level);
               } catch (Throwable var9) {
                  PortalGunMod.LOGGER.warn("Portal BER failed at {}: {}", be.getBlockPos(), var9.toString());
               }
            }
         }
      }
   }

   private void renderPortal(PortalMasterBlockEntity be, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, Level level) {
      Direction facing = be.getFacing();
      int portalW = Math.max(1, be.getPortalWidth());
      int portalH = Math.max(1, be.getPortalHeight());
      int colour = be.getColour() & 16777215;
      if (colour == 0 || colour == 16777215) {
         int[] pair = PortalGunHelper.coloursForChannel(be.getUuid(), be.getChannelName());
         colour = (be.isTypeA() ? pair[0] : pair[1]) & 16777215;
      }

      float r = (float)(colour >> 16 & 0xFF) / 255.0F;
      float g = (float)(colour >> 8 & 0xFF) / 255.0F;
      float b = (float)(colour & 0xFF) / 255.0F;
      boolean fancy = false;
      boolean seeThrough = false;

      try {
         fancy = (Boolean)PortalGunConfig.FANCY_PORTALS.get();
         seeThrough = (Boolean)PortalGunConfig.SEE_THROUGH_PORTALS.get();
      } catch (Throwable var28) {
      }

      boolean nestedPass = PortalSeeThrough.isCapturing();
      boolean hasPair = be.hasPair() || hasLinkedPair(be);
      if (be.hasPair() && seeThrough && !nestedPass) {
         PortalSeeThrough.requestCapture(be);
      }

      poseStack.pushPose();
      Direction upDir = be.getUpDir();
      if (facing.getAxis().isVertical() && (upDir == null || !upDir.getAxis().isHorizontal())) {
         upDir = Direction.SOUTH;
      }

      orientFlushToWall(poseStack, facing, upDir, portalW, portalH);
      float openProg = be.getOpenProgress(partialTick);
      poseStack.scale(openProg, openProg, 1.0F);
      float sx = (float)portalW;
      float sy = (float)portalH;
      int light = 15728880;
      if (bufferSource instanceof BufferSource source) {
         source.endBatch();
      }

      float RING_INNER = 0.86F;
      float IMAGE_SCALE = 0.9F;
      int fboTex = 0;
      if (hasPair && seeThrough && !nestedPass) {
         fboTex = PortalSeeThrough.textureId(be.getBlockPos());
      }

      if (fboTex != 0) {
         drawFboEllipse(poseStack, fboTex, sx * 0.9F, sy * 0.9F);
      } else {
         float voidR = Math.max(0.12F, r * 0.35F);
         float voidG = Math.max(0.12F, g * 0.35F);
         float voidB = Math.max(0.2F, b * 0.55F);
         drawSolidEllipse(poseStack, sx * 0.9F, sy * 0.9F, voidR, voidG, voidB, 1.0F);
      }

      drawOvalRing(poseStack, bufferSource, TEX_OUTLINE, sx, sy, 0.86F, r, g, b, light);
      if (!nestedPass) {
         float time = ((float)level.getGameTime() + partialTick) * 0.05F;
         float openBoost = hasPair ? 1.0F : 0.55F;
         drawPortalGlow(poseStack, bufferSource, sx, sy, r, g, b, time, openBoost, fancy);
      }

      poseStack.popPose();
      if (!nestedPass) {
         Minecraft mc = Minecraft.getInstance();
         if (mc.player != null && !mc.isPaused()) {
            BlockPos pos = be.getBlockPos();
            if (!(mc.player.distanceToSqr(Vec3.atCenterOf(pos)) > 576.0)) {
               spawnFaceParticles(level, pos, facing, upDir, portalW, portalH, colour, hasPair);
            }
         }
      }
   }

   private static void drawPortalGlow(
      PoseStack poseStack, MultiBufferSource bufferSource, float scaleX, float scaleY, float r, float g, float b, float time, float openBoost, boolean fancy
   ) {
      if (bufferSource instanceof BufferSource source) {
         source.endBatch();
      }

      RenderSystem.enableDepthTest();
      RenderSystem.depthMask(false);
      RenderSystem.enableBlend();
      RenderSystem.blendFuncSeparate(SourceFactor.SRC_ALPHA, DestFactor.ONE, SourceFactor.ONE, DestFactor.ZERO);
      RenderSystem.disableCull();
      RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
      Matrix4f mat = poseStack.last().pose();
      float hx = 0.5F * scaleX;
      float hy = 0.5F * scaleY;
      float z = 0.016F;
      drawGlowAnnulus(mat, hx * 1.18F, hy * 1.18F, hx * 0.92F, hy * 0.92F, z, r, g, b, (int)(55.0F * openBoost));
      drawGlowAnnulus(
         mat,
         hx * 1.02F,
         hy * 1.02F,
         hx * 0.86F,
         hy * 0.86F,
         z + 0.001F,
         Math.min(1.0F, r * 1.25F),
         Math.min(1.0F, g * 1.25F),
         Math.min(1.0F, b * 1.25F),
         (int)(140.0F * openBoost)
      );
      drawGlowAnnulus(
         mat,
         hx * 0.94F,
         hy * 0.94F,
         hx * 0.88F,
         hy * 0.88F,
         z + 0.002F,
         Math.min(1.0F, r * 1.4F),
         Math.min(1.0F, g * 1.4F),
         Math.min(1.0F, b * 1.4F),
         (int)(180.0F * openBoost)
      );
      if (fancy) {
         Minecraft.getInstance().getTextureManager().getTexture(TEX_OPEN).setFilter(true, false);
         RenderSystem.setShaderTexture(0, TEX_OPEN);
         RenderSystem.setShader(GameRenderer::getPositionTexColorShader);
         float spin = time * 0.7F;
         float rev = -time * 1.1F;
         int a1 = (int)(160.0F * openBoost) & 0xFF;
         int a2 = (int)(120.0F * openBoost) & 0xFF;
         int ir = (int)(Math.min(1.0F, r * 1.2F) * 255.0F) & 0xFF;
         int ig = (int)(Math.min(1.0F, g * 1.2F) * 255.0F) & 0xFF;
         int ib = (int)(Math.min(1.0F, b * 1.2F) * 255.0F) & 0xFF;
         drawSpinningGlowDisc(mat, hx * 0.98F, hy * 0.98F, z + 0.003F, spin, ir, ig, ib, a1);
         drawSpinningGlowDisc(mat, hx * 0.9F, hy * 0.9F, z + 0.004F, rev, ir, ig, ib, a2);
      }

      RenderSystem.defaultBlendFunc();
      RenderSystem.depthMask(true);
      RenderSystem.enableCull();
      RenderSystem.disableBlend();
      RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
   }

   private static void drawGlowAnnulus(Matrix4f mat, float hxO, float hyO, float hxI, float hyI, float z, float r, float g, float b, int alpha) {
      int ir = (int)(r * 255.0F) & 0xFF;
      int ig = (int)(g * 255.0F) & 0xFF;
      int ib = (int)(b * 255.0F) & 0xFF;
      int a = Math.max(0, Math.min(255, alpha));
      RenderSystem.setShader(GameRenderer::getPositionColorShader);
      BufferBuilder buf = Tesselator.getInstance().begin(Mode.TRIANGLE_STRIP, DefaultVertexFormat.POSITION_COLOR);

      for (int i = 0; i <= 64; i++) {
         float ang = (float)((double)i * 0.09817477042468103);
         float cos = (float)Math.cos((double)ang);
         float sin = (float)Math.sin((double)ang);
         buf.addVertex(mat, cos * hxO, sin * hyO, z).setColor(ir, ig, ib, a);
         buf.addVertex(mat, cos * hxI, sin * hyI, z).setColor(ir, ig, ib, a / 3);
      }

      BufferUploader.drawWithShader(buf.buildOrThrow());
   }

   private static void drawSpinningGlowDisc(Matrix4f mat, float hx, float hy, float z, float rot, int r, int g, int b, int a) {
      float cr = (float)Math.cos((double)rot);
      float sr = (float)Math.sin((double)rot);
      BufferBuilder buf = Tesselator.getInstance().begin(Mode.TRIANGLE_FAN, DefaultVertexFormat.POSITION_TEX_COLOR);
      buf.addVertex(mat, 0.0F, 0.0F, z).setUv(0.5F, 0.5F).setColor(r, g, b, a / 4);

      for (int i = 0; i <= 64; i++) {
         float ang = (float)((double)i * 0.09817477042468103);
         float cos = (float)Math.cos((double)ang);
         float sin = (float)Math.sin((double)ang);
         float x = (cos * cr - sin * sr) * hx;
         float y = (cos * sr + sin * cr) * hy;
         float u = 0.5F + 0.5F * cos;
         float v = 0.5F + 0.5F * sin;
         buf.addVertex(mat, x, y, z).setUv(u, v).setColor(r, g, b, a);
      }

      BufferUploader.drawWithShader(buf.buildOrThrow());
   }

   private static void drawOvalRing(
      PoseStack poseStack,
      MultiBufferSource bufferSource,
      ResourceLocation texture,
      float scaleX,
      float scaleY,
      float innerFrac,
      float r,
      float g,
      float b,
      int light
   ) {
      if (bufferSource instanceof BufferSource source) {
         source.endBatch();
      }

      Minecraft.getInstance().getTextureManager().getTexture(texture).setFilter(false, false);
      RenderSystem.setShaderTexture(0, texture);
      RenderSystem.setShader(GameRenderer::getPositionTexColorShader);
      RenderSystem.enableDepthTest();
      RenderSystem.enableBlend();
      RenderSystem.defaultBlendFunc();
      RenderSystem.disableCull();
      RenderSystem.depthMask(true);
      RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
      Matrix4f mat = poseStack.last().pose();
      float hxO = 0.5F * scaleX;
      float hyO = 0.5F * scaleY;
      float hxI = hxO * innerFrac;
      float hyI = hyO * innerFrac;
      int ir = (int)(r * 255.0F) & 0xFF;
      int ig = (int)(g * 255.0F) & 0xFF;
      int ib = (int)(b * 255.0F) & 0xFF;
      drawOutlineAnnulus(mat, hxO, hyO, hxI, hyI, 0.01F, true, ir, ig, ib, 255);
      drawOutlineAnnulus(mat, hxO, hyO, hxI, hyI, -0.006F, false, ir, ig, ib, 255);
      RenderSystem.enableCull();
      RenderSystem.disableBlend();
   }

   private static void drawOutlineAnnulus(Matrix4f mat, float hxO, float hyO, float hxI, float hyI, float z, boolean front, int r, int g, int b, int a) {
      BufferBuilder buf = Tesselator.getInstance().begin(Mode.TRIANGLE_STRIP, DefaultVertexFormat.POSITION_TEX_COLOR);
      float uvIn = 0.5F * (hxI / Math.max(hxO, 1.0E-4F));
      if (front) {
         for (int i = 0; i <= 64; i++) {
            float ang = (float)((double)i * 0.09817477042468103);
            float cos = (float)Math.cos((double)ang);
            float sin = (float)Math.sin((double)ang);
            float uo = 0.5F + 0.5F * cos;
            float vo = 0.5F + 0.5F * sin;
            float ui = 0.5F + uvIn * cos;
            float vi = 0.5F + uvIn * sin;
            buf.addVertex(mat, cos * hxO, sin * hyO, z).setUv(uo, vo).setColor(r, g, b, a);
            buf.addVertex(mat, cos * hxI, sin * hyI, z).setUv(ui, vi).setColor(r, g, b, a);
         }
      } else {
         for (int i = 64; i >= 0; i--) {
            float ang = (float)((double)i * 0.09817477042468103);
            float cos = (float)Math.cos((double)ang);
            float sin = (float)Math.sin((double)ang);
            float uo = 0.5F + 0.5F * cos;
            float vo = 0.5F + 0.5F * sin;
            float ui = 0.5F + uvIn * cos;
            float vi = 0.5F + uvIn * sin;
            buf.addVertex(mat, cos * hxO, sin * hyO, z).setUv(uo, vo).setColor(r, g, b, a);
            buf.addVertex(mat, cos * hxI, sin * hyI, z).setUv(ui, vi).setColor(r, g, b, a);
         }
      }

      BufferUploader.drawWithShader(buf.buildOrThrow());
   }

   private static void drawFboEllipse(PoseStack poseStack, int textureId, float scaleX, float scaleY) {
      Minecraft.getInstance().renderBuffers().bufferSource().endBatch();
      RenderSystem.enableDepthTest();
      RenderSystem.enableBlend();
      RenderSystem.defaultBlendFunc();
      RenderSystem.disableCull();
      Matrix4f mat = poseStack.last().pose();
      float hx = 0.5F * scaleX;
      float hy = 0.5F * scaleY;
      float frontZ = 0.001F;
      float backZ = -0.008F;
      if (!PortalStencil.drawFboAperture(mat, textureId, hx, hy, frontZ, backZ)) {
         RenderSystem.setShader(GameRenderer::getPositionTexColorShader);
         RenderSystem.setShaderTexture(0, textureId);
         RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
         drawTexturedEllipse(mat, hx, hy, frontZ, true);
         drawTexturedEllipse(mat, hx, hy, backZ, false);
      }

      RenderSystem.enableCull();
      RenderSystem.disableBlend();
      RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
   }

   private static void drawTexturedEllipse(Matrix4f mat, float hx, float hy, float z, boolean front) {
      BufferBuilder buf = Tesselator.getInstance().begin(Mode.TRIANGLE_FAN, DefaultVertexFormat.POSITION_TEX_COLOR);
      float cu = 0.5F;
      float cv = 0.5F;
      buf.addVertex(mat, 0.0F, 0.0F, z).setUv(cu, cv).setColor(255, 255, 255, 255);
      if (front) {
         for (int i = 0; i <= 64; i++) {
            float ang = (float)((double)i * 0.09817477042468103);
            float cos = (float)Math.cos((double)ang);
            float sin = (float)Math.sin((double)ang);
            float x = cos * hx;
            float y = sin * hy;
            float u = 0.5F - 0.5F * cos;
            float v = 0.5F + 0.5F * sin;
            buf.addVertex(mat, x, y, z).setUv(u, v).setColor(255, 255, 255, 255);
         }
      } else {
         for (int i = 64; i >= 0; i--) {
            float ang = (float)((double)i * 0.09817477042468103);
            float cos = (float)Math.cos((double)ang);
            float sin = (float)Math.sin((double)ang);
            float x = cos * hx;
            float y = sin * hy;
            float u = 0.5F - 0.5F * cos;
            float v = 0.5F + 0.5F * sin;
            buf.addVertex(mat, x, y, z).setUv(u, v).setColor(255, 255, 255, 255);
         }
      }

      BufferUploader.drawWithShader(buf.buildOrThrow());
   }

   private static void drawSolidEllipse(PoseStack poseStack, float scaleX, float scaleY, float r, float g, float b, float a) {
      Minecraft.getInstance().renderBuffers().bufferSource().endBatch();
      RenderSystem.enableDepthTest();
      RenderSystem.enableBlend();
      RenderSystem.defaultBlendFunc();
      RenderSystem.disableCull();
      RenderSystem.setShader(GameRenderer::getPositionColorShader);
      RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
      Matrix4f mat = poseStack.last().pose();
      float hx = 0.5F * scaleX;
      float hy = 0.5F * scaleY;
      int ir = (int)(r * 255.0F) & 0xFF;
      int ig = (int)(g * 255.0F) & 0xFF;
      int ib = (int)(b * 255.0F) & 0xFF;
      int ia = (int)(a * 255.0F) & 0xFF;

      for (float z : new float[]{0.001F, -0.001F}) {
         boolean front = z > 0.0F;
         BufferBuilder buf = Tesselator.getInstance().begin(Mode.TRIANGLE_FAN, DefaultVertexFormat.POSITION_COLOR);
         buf.addVertex(mat, 0.0F, 0.0F, z).setColor(ir, ig, ib, ia);
         if (front) {
            for (int i = 0; i <= 64; i++) {
               float ang = (float)((double)i * 0.09817477042468103);
               buf.addVertex(mat, (float)Math.cos((double)ang) * hx, (float)Math.sin((double)ang) * hy, z).setColor(ir, ig, ib, ia);
            }
         } else {
            for (int i = 64; i >= 0; i--) {
               float ang = (float)((double)i * 0.09817477042468103);
               buf.addVertex(mat, (float)Math.cos((double)ang) * hx, (float)Math.sin((double)ang) * hy, z).setColor(ir, ig, ib, ia);
            }
         }

         BufferUploader.drawWithShader(buf.buildOrThrow());
      }

      RenderSystem.enableCull();
      RenderSystem.disableBlend();
   }

   private static void orientFlushToWall(PoseStack poseStack, Direction facing, Direction upDir, int portalW, int portalH) {
      poseStack.translate(0.5, 0.5, 0.5);
      Direction widthDir = PortalGunHelper.widthDirection(facing, upDir);
      Direction heightDir = PortalGunHelper.heightDirection(facing, upDir);
      int w0 = PortalGunHelper.widthStart(portalW);
      int h0 = PortalGunHelper.heightStart(facing, portalH);
      double midW = (double)w0 + (double)(portalW - 1) * 0.5;
      double midH = (double)h0 + (double)(portalH - 1) * 0.5;
      poseStack.translate(
         (double)widthDir.getStepX() * midW + (double)heightDir.getStepX() * midH,
         (double)widthDir.getStepY() * midW + (double)heightDir.getStepY() * midH,
         (double)widthDir.getStepZ() * midW + (double)heightDir.getStepZ() * midH
      );
      float flush = facing.getAxis().isVertical() ? 0.47F : 0.49F;
      poseStack.translate((float)(-facing.getStepX()) * flush, (float)(-facing.getStepY()) * flush, (float)(-facing.getStepZ()) * flush);
      float roomEps = facing.getAxis().isVertical() ? 0.02F : 0.005F;
      poseStack.translate((float)facing.getStepX() * roomEps, (float)facing.getStepY() * roomEps, (float)facing.getStepZ() * roomEps);
      applyFaceRotation(poseStack, facing, heightDir);
   }

   private static void applyFaceRotation(PoseStack poseStack, Direction facing, Direction heightDir) {
      switch (facing) {
         case SOUTH:
         default:
            break;
         case NORTH:
            poseStack.mulPose(Axis.YP.rotationDegrees(180.0F));
            break;
         case EAST:
            poseStack.mulPose(Axis.YP.rotationDegrees(90.0F));
            break;
         case WEST:
            poseStack.mulPose(Axis.YP.rotationDegrees(-90.0F));
            break;
         case UP:
            poseStack.mulPose(Axis.XP.rotationDegrees(-90.0F));
            poseStack.mulPose(Axis.ZP.rotationDegrees(floorRollDegrees(Direction.NORTH, heightDir)));
            break;
         case DOWN:
            poseStack.mulPose(Axis.XP.rotationDegrees(90.0F));
            poseStack.mulPose(Axis.ZP.rotationDegrees(floorRollDegrees(Direction.SOUTH, heightDir)));
      }
   }

   private static float floorRollDegrees(Direction fromLocalY, Direction desired) {
      Direction to = desired;
      if (desired == null || !desired.getAxis().isHorizontal()) {
         to = fromLocalY;
      }

      int from = fromLocalY.get2DDataValue();
      int dest = to.get2DDataValue();
      return (float)Math.floorMod(dest - from, 4) * 90.0F;
   }

   private static void drawQuad(
      PoseStack poseStack, VertexConsumer vc, float scaleX, float scaleY, float u0, float v0, float u1, float v1, float r, float g, float b, float a, int light
   ) {
      Pose pose = poseStack.last();
      Matrix4f mat = pose.pose();
      float hx = 0.5F * scaleX;
      float hy = 0.5F * scaleY;
      vertex(vc, mat, pose, -hx, -hy, 0.0F, u0, v1, r, g, b, a, light, 0.0F, 0.0F, 1.0F);
      vertex(vc, mat, pose, hx, -hy, 0.0F, u1, v1, r, g, b, a, light, 0.0F, 0.0F, 1.0F);
      vertex(vc, mat, pose, hx, hy, 0.0F, u1, v0, r, g, b, a, light, 0.0F, 0.0F, 1.0F);
      vertex(vc, mat, pose, -hx, hy, 0.0F, u0, v0, r, g, b, a, light, 0.0F, 0.0F, 1.0F);
      vertex(vc, mat, pose, -hx, hy, 0.0F, u0, v0, r, g, b, a, light, 0.0F, 0.0F, -1.0F);
      vertex(vc, mat, pose, hx, hy, 0.0F, u1, v0, r, g, b, a, light, 0.0F, 0.0F, -1.0F);
      vertex(vc, mat, pose, hx, -hy, 0.0F, u1, v1, r, g, b, a, light, 0.0F, 0.0F, -1.0F);
      vertex(vc, mat, pose, -hx, -hy, 0.0F, u0, v1, r, g, b, a, light, 0.0F, 0.0F, -1.0F);
   }

   private static void vertex(
      VertexConsumer vc,
      Matrix4f mat,
      Pose pose,
      float x,
      float y,
      float z,
      float u,
      float v,
      float r,
      float g,
      float b,
      float a,
      int light,
      float nx,
      float ny,
      float nz
   ) {
      vc.addVertex(mat, x, y, z).setColor(r, g, b, a).setUv(u, v).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(pose, nx, ny, nz);
   }

   private static boolean hasLinkedPair(PortalMasterBlockEntity be) {
      if (be.getLevel() instanceof ServerLevel server) {
         try {
            PortalSavedData data = PortalSavedData.get(server);
            PortalSavedData.PortalEntry entry = data.getEntry(be.getUuid(), be.getChannelName(), be.isTypeA());
            return entry == null ? false : data.findPair(entry.info) != null;
         } catch (Throwable var5) {
            return false;
         }
      } else {
         return ClientPortalStatus.hasBlue() && ClientPortalStatus.hasOrange();
      }
   }

   private static void spawnFaceParticles(
      Level level, BlockPos origin, Direction facing, Direction upDir, int portalW, int portalH, int colour, boolean hasPair
   ) {
      long key = origin.asLong();
      long gameTime = level.getGameTime();
      Long last = LAST_PARTICLE_TICK.get(key);
      // At most every 4 ticks — particles are pure client cost near portals
      if (last == null || gameTime - last >= 4L) {
         LAST_PARTICLE_TICK.put(key, gameTime);
         if (LAST_PARTICLE_TICK.size() > 512) {
            LAST_PARTICLE_TICK.clear();
            LAST_PARTICLE_TICK.put(key, gameTime);
         }

         RandomSource random = level.getRandom();
         int count = hasPair ? 2 : 1;
         Direction widthDir = PortalGunHelper.widthDirection(facing, upDir);
         Direction heightDir = PortalGunHelper.heightDirection(facing, upDir);
         int w0 = PortalGunHelper.widthStart(portalW);
         int h0 = PortalGunHelper.heightStart(facing, portalH);
         double midW = (double)w0 + (double)(portalW - 1) * 0.5;
         double midH = (double)h0 + (double)(portalH - 1) * 0.5;
         double cx = (double)origin.getX() + 0.5 + (double)widthDir.getStepX() * midW + (double)heightDir.getStepX() * midH;
         double cy = (double)origin.getY() + 0.5 + (double)widthDir.getStepY() * midW + (double)heightDir.getStepY() * midH;
         double cz = (double)origin.getZ() + 0.5 + (double)widthDir.getStepZ() * midW + (double)heightDir.getStepZ() * midH;
         double faceOffset = -0.45;
         double nx = (double)facing.getStepX();
         double ny = (double)facing.getStepY();
         double nz = (double)facing.getStepZ();
         Vector3f dustColor = Vec3.fromRGB24(colour).toVector3f();
         double spreadU = 0.35 * (double)portalW;
         double spreadV = 0.35 * (double)portalH;

         for (int i = 0; i < count; i++) {
            double u = (random.nextDouble() - 0.5) * 2.0 * spreadU;
            double v = (random.nextDouble() - 0.5) * 2.0 * spreadV;
            double x;
            double y;
            double z;
            switch (facing.getAxis()) {
               case X:
                  x = cx + nx * faceOffset;
                  y = cy + u;
                  z = cz + v;
                  break;
               case Y:
                  x = cx + u;
                  y = cy + ny * faceOffset;
                  z = cz + v;
                  break;
               default:
                  x = cx + u;
                  y = cy + v;
                  z = cz + nz * faceOffset;
            }

            double vx = (random.nextDouble() - 0.5) * 0.08 + nx * 0.03;
            double vy = (random.nextDouble() - 0.5) * 0.08 + ny * 0.03;
            double vz = (random.nextDouble() - 0.5) * 0.08 + nz * 0.03;
            switch (random.nextInt(3)) {
               case 0:
                  level.addParticle(new DustParticleOptions(dustColor, 0.9F), x, y, z, 0.0, 0.0, 0.0);
                  break;
               case 1:
                  level.addParticle(ParticleTypes.PORTAL, x, y, z, vx, vy, vz);
                  break;
               default:
                  level.addParticle(ParticleTypes.ENCHANT, x, y, z, vx, vy + 0.1, vz);
            }
         }
      }
   }

   public boolean shouldRenderOffScreen(PortalMasterBlockEntity be) {
      return true;
   }

   public int getViewDistance() {
      return 64;
   }

   public AABB getRenderBoundingBox(PortalMasterBlockEntity be) {
      BlockPos o = be.getBlockPos();
      int w = Math.max(1, be.getPortalWidth());
      int h = Math.max(1, be.getPortalHeight());
      Direction face = be.getFacing();
      Direction up = be.getUpDir();
      Direction widthDir = PortalGunHelper.widthDirection(face, up);
      Direction heightDir = PortalGunHelper.heightDirection(face, up);
      int w0 = PortalGunHelper.widthStart(w);
      int h0 = PortalGunHelper.heightStart(face, h);
      double minX = (double)o.getX();
      double minY = (double)o.getY();
      double minZ = (double)o.getZ();
      double maxX = (double)(o.getX() + 1);
      double maxY = (double)(o.getY() + 1);
      double maxZ = (double)(o.getZ() + 1);

      for (int dw = 0; dw < w; dw++) {
         for (int dh = 0; dh < h; dh++) {
            int ox = widthDir.getStepX() * (w0 + dw) + heightDir.getStepX() * (h0 + dh);
            int oy = widthDir.getStepY() * (w0 + dw) + heightDir.getStepY() * (h0 + dh);
            int oz = widthDir.getStepZ() * (w0 + dw) + heightDir.getStepZ() * (h0 + dh);
            minX = Math.min(minX, (double)(o.getX() + ox));
            minY = Math.min(minY, (double)(o.getY() + oy));
            minZ = Math.min(minZ, (double)(o.getZ() + oz));
            maxX = Math.max(maxX, (double)(o.getX() + ox + 1));
            maxY = Math.max(maxY, (double)(o.getY() + oy + 1));
            maxZ = Math.max(maxZ, (double)(o.getZ() + oz + 1));
         }
      }

      return new AABB(minX - 0.5, minY - 0.5, minZ - 0.5, maxX + 0.5, maxY + 0.5, maxZ + 0.5);
   }
}
