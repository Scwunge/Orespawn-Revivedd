package me.scwunge.mods.portalgun.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import me.scwunge.mods.portalgun.PortalGunMod;
import me.scwunge.mods.portalgun.client.model.ModelPortalProjectile;
import me.scwunge.mods.portalgun.entity.PortalProjectile;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class PortalProjectileRenderer extends EntityRenderer<PortalProjectile> {
   private static final ResourceLocation TEX = ResourceLocation.fromNamespaceAndPath("orespawn", "textures/model/pg_glow.png");
   private final ModelPortalProjectile model = new ModelPortalProjectile(ModelPortalProjectile.createBodyLayer().bakeRoot());

   public PortalProjectileRenderer(Context ctx) {
      super(ctx);
      this.shadowRadius = 0.15F;
   }

   public void render(PortalProjectile entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
      try {
         poseStack.pushPose();
         float yRot = Mth.lerp(partialTick, entity.yRotO, entity.getYRot());
         float xRot = Mth.lerp(partialTick, entity.xRotO, entity.getXRot());
         poseStack.mulPose(Axis.YP.rotationDegrees(yRot - 90.0F));
         poseStack.mulPose(Axis.ZP.rotationDegrees(xRot));
         float scale = 0.35F;
         poseStack.scale(scale, scale, scale);
         float age = (float)entity.tickCount + partialTick;
         this.model.setupAnim(age);
         int rgb = entity.getTrailColour();
         float r = (float)(rgb >> 16 & 0xFF) / 255.0F;
         float g = (float)(rgb >> 8 & 0xFF) / 255.0F;
         float b = (float)(rgb & 0xFF) / 255.0F;
         VertexConsumer vc = buffer.getBuffer(RenderType.entityCutoutNoCull(TEX));
         int argb = 0xFF000000 | rgb & 16777215;
         this.model.renderToBuffer(poseStack, vc, packedLight, OverlayTexture.NO_OVERLAY, argb);
         poseStack.scale(1.25F, 1.25F, 1.25F);
         int soft = -1728053248 | rgb & 16777215;
         VertexConsumer glow = buffer.getBuffer(RenderType.entityTranslucent(TEX));
         this.model.renderToBuffer(poseStack, glow, 15728880, OverlayTexture.NO_OVERLAY, soft);
         poseStack.popPose();
         super.render(entity, entityYaw, partialTick, poseStack, buffer, packedLight);
      } catch (Throwable var20) {
         PortalGunMod.LOGGER.warn("Portal projectile render failed: {}", var20.toString());

         try {
            poseStack.popPose();
         } catch (Throwable var19) {
         }
      }
   }

   public ResourceLocation getTextureLocation(PortalProjectile entity) {
      return TEX;
   }
}
