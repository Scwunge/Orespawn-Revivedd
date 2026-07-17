package me.scwunge.mods.portalgun.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.RenderType;

public class ModelPortalProjectile extends Model {
   private static final float PI4 = (float) (Math.PI / 4);
   private final ModelPart inner1;
   private final ModelPart inner2;
   private final ModelPart inner3;
   private final ModelPart outer1;
   private final ModelPart outer2;
   private final ModelPart outer3;

   public ModelPortalProjectile(ModelPart root) {
      super(RenderType::entityCutoutNoCull);
      this.inner1 = root.getChild("inner1");
      this.inner2 = root.getChild("inner2");
      this.inner3 = root.getChild("inner3");
      this.outer1 = root.getChild("outer1");
      this.outer2 = root.getChild("outer2");
      this.outer3 = root.getChild("outer3");
   }

   public static LayerDefinition createBodyLayer() {
      MeshDefinition mesh = new MeshDefinition();
      PartDefinition root = mesh.getRoot();
      root.addOrReplaceChild("inner1", CubeListBuilder.create().texOffs(0, 0).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F), PartPose.ZERO);
      root.addOrReplaceChild(
         "inner2",
         CubeListBuilder.create().texOffs(0, 0).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F),
         PartPose.rotation(0.0F, (float) (Math.PI / 4), (float) (Math.PI / 4))
      );
      root.addOrReplaceChild(
         "inner3",
         CubeListBuilder.create().texOffs(0, 0).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F),
         PartPose.rotation((float) (Math.PI / 4), 0.0F, (float) (Math.PI / 4))
      );
      root.addOrReplaceChild("outer1", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F), PartPose.ZERO);
      root.addOrReplaceChild(
         "outer2",
         CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F),
         PartPose.rotation((float) (Math.PI / 4), 0.0F, (float) (Math.PI / 4))
      );
      root.addOrReplaceChild(
         "outer3",
         CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F),
         PartPose.rotation(0.0F, (float) (Math.PI / 4), (float) (Math.PI / 4))
      );
      return LayerDefinition.create(mesh, 64, 32);
   }

   public void setupAnim(float ageInTicks) {
      float t = ageInTicks / 2.0F;
      this.inner1.xRot = t * 1.2F;
      this.inner1.yRot = t * 1.4F;
      this.inner1.zRot = t * 1.1F;
      this.inner2.xRot = t * 0.8F;
      this.inner2.yRot = (float) (Math.PI / 4) + t * 0.4F;
      this.inner2.zRot = (float) (Math.PI / 4) + t * 0.6F;
      this.inner3.xRot = (float) (Math.PI / 4) + t * 1.5F;
      this.inner3.yRot = t * 1.2F;
      this.inner3.zRot = (float) (Math.PI / 4) + t * 1.3F;
      this.outer1.xRot = t * 0.7F;
      this.outer1.yRot = t * 0.5F;
      this.outer1.zRot = t * 0.6F;
      this.outer2.xRot = (float) (Math.PI / 4) + t * 0.9F;
      this.outer2.yRot = t * 1.0F;
      this.outer2.zRot = (float) (Math.PI / 4) + t * 0.4F;
      this.outer3.xRot = t * 0.3F;
      this.outer3.yRot = (float) (Math.PI / 4) + t * 0.1F;
      this.outer3.zRot = (float) (Math.PI / 4) + t * 0.4F;
   }

   public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, int color) {
      this.inner1.render(poseStack, buffer, packedLight, packedOverlay, color);
      this.inner2.render(poseStack, buffer, packedLight, packedOverlay, color);
      this.inner3.render(poseStack, buffer, packedLight, packedOverlay, color);
      this.outer1.render(poseStack, buffer, packedLight, packedOverlay, color);
      this.outer2.render(poseStack, buffer, packedLight, packedOverlay, color);
      this.outer3.render(poseStack, buffer, packedLight, packedOverlay, color);
   }
}
