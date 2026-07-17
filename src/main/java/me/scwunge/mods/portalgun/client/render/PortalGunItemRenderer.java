package me.scwunge.mods.portalgun.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import me.scwunge.mods.portalgun.client.ClientChannelIndicators;
import me.scwunge.mods.portalgun.client.ClientGrabStatus;
import me.scwunge.mods.portalgun.client.ClientPortalStatus;
import me.scwunge.mods.portalgun.client.model.ModelPortalGun;
import me.scwunge.mods.portalgun.item.PortalGunData;
import me.scwunge.mods.portalgun.portal.PortalGunHelper;
import me.scwunge.mods.portalgun.portal.info.ChannelIndicator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.MultiBufferSource.BufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class PortalGunItemRenderer extends BlockEntityWithoutLevelRenderer {
   private static final ResourceLocation TEX = ResourceLocation.fromNamespaceAndPath("orespawn", "textures/model/pg.png");
   private static final ResourceLocation TEX_GLOW = ResourceLocation.fromNamespaceAndPath("orespawn", "textures/model/pg_glow.png");
   private static final float SCALE_TP = 0.35F;
   private static final float SCALE_FP_X = 0.32F;
   private static final float SCALE_FP_Y = 0.5F;
   private static final float SCALE_FP_Z = 0.5F;
   private static final float SCALE_GUI = 0.35F;
   private static final float SCALE_GROUND = 0.13F;
   private static final float SCALE_FIXED = 0.35F;
   private final ModelPortalGun model = new ModelPortalGun(ModelPortalGun.createBodyLayer().bakeRoot());

   public PortalGunItemRenderer(EntityModelSet models) {
      super(Minecraft.getInstance().getBlockEntityRenderDispatcher(), models);
   }

   public void renderByItem(ItemStack stack, ItemDisplayContext context, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
      poseStack.pushPose();
      boolean firstPerson = context.firstPerson();
      boolean left = context == ItemDisplayContext.FIRST_PERSON_LEFT_HAND || context == ItemDisplayContext.THIRD_PERSON_LEFT_HAND;
      boolean hand = isHeldInHand(context);
      poseStack.translate(0.5F, 0.5F, 0.5F);
      applyItemTransform(poseStack, context);
      poseStack.translate(0.55F, -0.7F, 0.0F);
      if (hand) {
         poseStack.mulPose(Axis.XP.rotationDegrees(180.0F));
      }

      float claw = firstPerson ? ClientGrabStatus.clawOpen() : 0.0F;
      float kick = firstPerson ? ClientGrabStatus.getFireKick() : 0.0F;
      float fireProg = Mth.clamp(kick, 0.0F, 1.0F);
      float clawProg = Mth.clamp(Math.max(claw, fireProg * 0.85F), 0.0F, 1.0F);
      boolean firingPortal = !ClientGrabStatus.isHolding() && fireProg > 0.05F;
      if (firstPerson && fireProg > 0.0F) {
         float kickAmt = (float)Math.sin((double)fireProg * Math.PI) * 0.12F;
         poseStack.translate(-kickAmt * 0.4F, 0.0F, 0.0F);
         poseStack.mulPose(Axis.ZP.rotationDegrees(-8.0F * kickAmt * (left ? -1.0F : 1.0F)));
         poseStack.mulPose(Axis.YP.rotationDegrees(-5.0F * kickAmt * (left ? -1.0F : 1.0F)));
      }

      int overlay = packedOverlay == 0 ? OverlayTexture.NO_OVERLAY : packedOverlay;
      VertexConsumer solid = ItemRenderer.getFoilBuffer(buffer, RenderType.entityCutoutNoCull(TEX), false, stack.hasFoil());
      this.model.renderBase(poseStack, solid, packedLight, overlay);
      poseStack.pushPose();
      if (firstPerson && fireProg > 0.0F) {
         float pull = (float)Math.sin((double)fireProg * Math.PI) * 0.35F;
         poseStack.translate(-pull * 0.6F, -pull * 0.1F, 0.0F);
      }

      this.model.renderBarrel(poseStack, solid, packedLight, overlay);
      this.model.renderClaw(poseStack, solid, packedLight, overlay, clawProg, firingPortal);
      poseStack.popPose();
      this.model.renderTube(poseStack, solid, packedLight, overlay);
      if (buffer instanceof BufferSource source) {
         source.endBatch(RenderType.entityCutoutNoCull(TEX));
      }

      int rgb = glowColour(stack);
      int glowArgb = 0xFF000000 | rgb & 16777215;
      int fullBright = 15728880;
      VertexConsumer glow = buffer.getBuffer(RenderType.eyes(TEX_GLOW));
      poseStack.pushPose();
      if (firstPerson && fireProg > 0.0F) {
         float pull = (float)Math.sin((double)fireProg * Math.PI) * 0.35F;
         poseStack.translate(-pull * 0.6F, -pull * 0.1F, 0.0F);
      }

      this.model.renderCore(poseStack, glow, fullBright, OverlayTexture.NO_OVERLAY, glowArgb);
      poseStack.popPose();
      this.model.renderIndicator(poseStack, glow, fullBright, OverlayTexture.NO_OVERLAY, glowArgb);
      poseStack.popPose();
   }

   private static void applyItemTransform(PoseStack pose, ItemDisplayContext ctx) {
      if (ctx == ItemDisplayContext.THIRD_PERSON_LEFT_HAND || ctx == ItemDisplayContext.THIRD_PERSON_RIGHT_HAND) {
         pose.translate(0.0F, 0.4F, 0.0F);
         pose.mulPose(Axis.YP.rotationDegrees(90.0F));
         pose.scale(0.35F, 0.35F, 0.35F);
      } else if (ctx == ItemDisplayContext.FIRST_PERSON_LEFT_HAND) {
         pose.translate(-0.07F, 0.48F, 0.06F);
         pose.mulPose(Axis.XP.rotationDegrees(17.6F));
         pose.mulPose(Axis.YP.rotationDegrees(-270.0F));
         pose.mulPose(Axis.ZP.rotationDegrees(-8.0F));
         pose.scale(0.32F, 0.5F, 0.5F);
      } else if (ctx == ItemDisplayContext.FIRST_PERSON_RIGHT_HAND) {
         pose.translate(-0.03F, 0.48F, 0.06F);
         pose.mulPose(Axis.XP.rotationDegrees(3.8F));
         pose.mulPose(Axis.YP.rotationDegrees(-95.9F));
         pose.mulPose(Axis.ZP.rotationDegrees(-7.0F));
         pose.mulPose(Axis.YP.rotationDegrees(180.0F));
         pose.scale(0.32F, 0.5F, 0.5F);
      } else if (ctx == ItemDisplayContext.GUI) {
         pose.translate(0.08F, 0.05F, -0.25F);
         pose.mulPose(Axis.XP.rotationDegrees(37.0F));
         pose.mulPose(Axis.YP.rotationDegrees(-56.0F));
         pose.mulPose(Axis.ZP.rotationDegrees(1.0F));
         pose.mulPose(Axis.XP.rotationDegrees(180.0F));
         pose.scale(0.35F, 0.35F, 0.35F);
      } else if (ctx == ItemDisplayContext.GROUND) {
         pose.translate(0.5F, 0.5F, 0.5F);
         pose.translate(0.08F, 0.28F, 0.0F);
         pose.mulPose(Axis.ZP.rotationDegrees(-30.0F));
         pose.scale(0.13F, 0.13F, 0.13F);
      } else if (ctx == ItemDisplayContext.FIXED) {
         pose.translate(0.5F, 0.5F, 0.5F);
         pose.translate(0.0F, 0.2F, -0.25F);
         pose.mulPose(Axis.XP.rotationDegrees(37.0F));
         pose.mulPose(Axis.YP.rotationDegrees(-56.0F));
         pose.mulPose(Axis.ZP.rotationDegrees(1.0F));
         pose.scale(0.35F, 0.35F, 0.35F);
      } else {
         pose.translate(0.5F, 0.5F, 0.5F);
         pose.scale(0.35F, 0.35F, 0.35F);
      }
   }

   private static int glowColour(ItemStack stack) {
      ChannelIndicator ind = null;
      Minecraft mc = Minecraft.getInstance();
      String owner = PortalGunData.ownerUuid(stack);
      if (owner.isEmpty() && mc.player != null) {
         owner = mc.player.getUUID().toString();
      }

      if (!owner.isEmpty()) {
         ind = ClientChannelIndicators.get(owner, PortalGunData.channel(stack));
      }

      boolean lastOrange = PortalGunData.lastOrange(stack);
      if (ind != null && ind.info.colourA != -1) {
         boolean hasA = ind.portalAAvailable || ClientPortalStatus.hasBlue();
         boolean hasB = ind.portalBAvailable || ClientPortalStatus.hasOrange();
         if (hasA && !hasB) {
            return ind.info.colourA;
         } else if (hasB && !hasA) {
            return ind.info.colourB;
         } else {
            return lastOrange ? ind.info.colourB : ind.info.colourA;
         }
      } else {
         int[] cols = PortalGunHelper.coloursForChannel(owner, PortalGunData.channel(stack));
         if (ClientPortalStatus.hasBlue() && !ClientPortalStatus.hasOrange()) {
            return cols[0];
         } else if (ClientPortalStatus.hasOrange() && !ClientPortalStatus.hasBlue()) {
            return cols[1];
         } else {
            return lastOrange ? cols[1] : cols[0];
         }
      }
   }

   private static boolean isHeldInHand(ItemDisplayContext ctx) {
      return ctx == ItemDisplayContext.FIRST_PERSON_RIGHT_HAND
         || ctx == ItemDisplayContext.FIRST_PERSON_LEFT_HAND
         || ctx == ItemDisplayContext.THIRD_PERSON_RIGHT_HAND
         || ctx == ItemDisplayContext.THIRD_PERSON_LEFT_HAND;
   }
}
