package me.scwunge.mods.portalgun.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

public class ModelPortalGun {
   private final ModelPart root;

   public ModelPortalGun(ModelPart root) {
      this.root = root;
   }

   public static LayerDefinition createBodyLayer() {
      MeshDefinition mesh = new MeshDefinition();
      PartDefinition root = mesh.getRoot();
      PartDefinition p_claw2 = root.addOrReplaceChild(
         "claw2",
         CubeListBuilder.create().texOffs(2, 16).addBox(-0.5F, 0.0F, -1.0F, 1.0F, 1.0F, 2.0F),
         PartPose.offsetAndRotation(9.0F, 20.0F, 5.0F, (float) (-Math.PI / 4), 3.141593F, 3.141593F)
      );
      PartDefinition p_claw3 = root.addOrReplaceChild(
         "claw3",
         CubeListBuilder.create().texOffs(2, 16).addBox(-0.5F, 0.0F, -1.0F, 1.0F, 1.0F, 2.0F),
         PartPose.offsetAndRotation(9.0F, 20.0F, -5.0F, (float) (Math.PI / 4), 3.141593F, 3.141593F)
      );
      PartDefinition p_block = root.addOrReplaceChild(
         "block", CubeListBuilder.create().texOffs(0, 35).addBox(-8.0F, -5.0F, -4.5F, 16.0F, 10.0F, 9.0F), PartPose.offset(-12.0F, 14.0F, 0.0F)
      );
      PartDefinition p_block2 = root.addOrReplaceChild(
         "block2", CubeListBuilder.create().texOffs(1, 36).addBox(-8.0F, -0.5F, -4.0F, 16.0F, 1.0F, 8.0F), PartPose.offset(-12.0F, 8.5F, 0.0F)
      );
      PartDefinition p_block3 = root.addOrReplaceChild(
         "block3", CubeListBuilder.create().texOffs(2, 37).addBox(-8.0F, -0.5F, -3.5F, 16.0F, 1.0F, 7.0F), PartPose.offset(-12.0F, 19.5F, 0.0F)
      );
      PartDefinition p_block4 = root.addOrReplaceChild(
         "block4", CubeListBuilder.create().texOffs(3, 38).addBox(-8.0F, -0.5F, -3.0F, 16.0F, 1.0F, 6.0F), PartPose.offset(-12.0F, 7.5F, 0.0F)
      );
      PartDefinition p_block5 = root.addOrReplaceChild(
         "block5", CubeListBuilder.create().texOffs(3, 38).addBox(-8.0F, -0.5F, -3.0F, 16.0F, 1.0F, 6.0F), PartPose.offset(-12.0F, 20.5F, 0.0F)
      );
      PartDefinition p_edgeA = root.addOrReplaceChild(
         "edgeA", CubeListBuilder.create().texOffs(0, 14).addBox(-1.0F, 0.0F, -3.5F, 2.0F, 1.0F, 4.0F), PartPose.offset(-3.0F, 10.15F, 1.5F)
      );
      PartDefinition p_edgeB = root.addOrReplaceChild(
         "edgeB",
         CubeListBuilder.create().texOffs(1, 15).addBox(-1.0F, -0.5F, -3.0F, 2.0F, 1.0F, 3.0F),
         PartPose.offsetAndRotation(-3.0F, 10.5F, -1.5F, (float) (Math.PI / 4), 0.0F, 0.0F)
      );
      PartDefinition p_edgeC = root.addOrReplaceChild(
         "edgeC",
         CubeListBuilder.create().texOffs(1, 15).addBox(-1.0F, -0.5F, -3.0F, 2.0F, 1.0F, 3.0F),
         PartPose.offsetAndRotation(-3.000002F, 12.62132F, -3.621318F, 1.570796F, 0.0F, 0.0F)
      );
      PartDefinition p_edgeD = root.addOrReplaceChild(
         "edgeD",
         CubeListBuilder.create().texOffs(1, 15).addBox(-1.0F, -0.5F, -3.0F, 2.0F, 1.0F, 3.0F),
         PartPose.offsetAndRotation(-3.0F, 15.62132F, -3.621317F, 0.7853983F, -3.141593F, -3.141593F)
      );
      PartDefinition p_edgeE = root.addOrReplaceChild(
         "edgeE",
         CubeListBuilder.create().texOffs(1, 15).addBox(-1.0F, -0.5F, -3.0F, 2.0F, 1.0F, 3.0F),
         PartPose.offsetAndRotation(-3.0F, 17.74264F, -1.499998F, -2.6E-7F, -3.141593F, -3.141593F)
      );
      PartDefinition p_edgeF = root.addOrReplaceChild(
         "edgeF",
         CubeListBuilder.create().texOffs(1, 15).addBox(-1.0F, -0.5F, -3.0F, 2.0F, 1.0F, 3.0F),
         PartPose.offsetAndRotation(-3.000002F, 17.74264F, 1.500001F, -0.7853986F, -3.141593F, -3.141593F)
      );
      PartDefinition p_edgeG = root.addOrReplaceChild(
         "edgeG",
         CubeListBuilder.create().texOffs(1, 15).addBox(-1.0F, -0.5F, -3.0F, 2.0F, 1.0F, 3.0F),
         PartPose.offsetAndRotation(-3.0F, 15.62132F, 3.621321F, -1.570796F, 0.0F, 0.0F)
      );
      PartDefinition p_edgeH = root.addOrReplaceChild(
         "edgeH",
         CubeListBuilder.create().texOffs(1, 15).addBox(-1.0F, -0.5F, -3.0F, 2.0F, 1.0F, 3.0F),
         PartPose.offsetAndRotation(-2.999998F, 12.62132F, 3.621319F, -0.7853976F, 0.0F, 0.0F)
      );
      PartDefinition p_claw1 = root.addOrReplaceChild(
         "claw1", CubeListBuilder.create().texOffs(2, 16).addBox(-0.5F, 0.0F, -2.5F, 1.0F, 1.0F, 2.0F), PartPose.offset(9.0F, 9.0F, 1.5F)
      );
      PartDefinition p_claw1A = root.addOrReplaceChild(
         "claw1A",
         CubeListBuilder.create().texOffs(15, 10).addBox(-0.5F, -6.5F, -0.5F, 1.0F, 6.0F, 1.0F),
         PartPose.offsetAndRotation(9.0F, 9.5F, 0.0F, 0.0F, 0.0F, 0.6981316F)
      );
      PartDefinition p_claw1B = p_claw1A.addOrReplaceChild(
         "claw1B",
         CubeListBuilder.create().texOffs(15, 10).addBox(-0.5F, -7.5F, -0.5F, 1.0F, 8.0F, 1.0F),
         PartPose.offsetAndRotation(0.0F, -6.5F, 0.0F, 0.0F, 0.0F, 0.87266463F)
      );
      PartDefinition p_claw1C = p_claw1B.addOrReplaceChild(
         "claw1C",
         CubeListBuilder.create().texOffs(13, 12).addBox(-0.5F, -2.5F, -1.0F, 1.0F, 3.0F, 2.0F),
         PartPose.offsetAndRotation(0.0F, -7.5F, 0.0F, 0.0F, 0.0F, (float) (Math.PI * 2.0 / 9.0))
      );
      PartDefinition p_frontShell1 = root.addOrReplaceChild(
         "frontShell1",
         CubeListBuilder.create().texOffs(0, 21).addBox(-6.0F, -1.0F, -2.5F, 12.0F, 1.0F, 5.0F),
         PartPose.offsetAndRotation(8.0F, 20.0F, 0.0F, 0.0F, 0.0F, -3.141593F)
      );
      PartDefinition p_frontShell6 = root.addOrReplaceChild(
         "frontShell6",
         CubeListBuilder.create().texOffs(0, 21).addBox(-6.0F, -1.0F, 0.0F, 12.0F, 1.0F, 5.0F),
         PartPose.offsetAndRotation(8.0F, 21.0F, -2.5F, 0.7853979F, 3.141593F, -1.2E-7F)
      );
      PartDefinition p_frontShell7 = root.addOrReplaceChild(
         "frontShell7",
         CubeListBuilder.create().texOffs(2, 24).addBox(-5.0F, -1.0F, 0.0F, 10.0F, 1.0F, 2.0F),
         PartPose.offsetAndRotation(8.0F, 17.46447F, -6.035534F, 1.570796F, 3.141593F, 0.0F)
      );
      PartDefinition p_frontShell8 = root.addOrReplaceChild(
         "frontShell8",
         CubeListBuilder.create().texOffs(3, 25).addBox(-4.0F, -1.0F, 0.0F, 8.0F, 1.0F, 1.0F),
         PartPose.offsetAndRotation(8.0F, 15.46447F, -6.035534F, 1.570796F, 3.141593F, 0.0F)
      );
      PartDefinition p_frontShell9 = root.addOrReplaceChild(
         "frontShell9",
         CubeListBuilder.create().texOffs(3, 25).addBox(-3.0F, -1.0F, 0.0F, 6.0F, 1.0F, 1.0F),
         PartPose.offsetAndRotation(7.999999F, 14.46447F, -6.035534F, 1.570796F, 3.141593F, 0.0F)
      );
      PartDefinition p_frontShell2 = root.addOrReplaceChild(
         "frontShell2",
         CubeListBuilder.create().texOffs(0, 27).addBox(-6.0F, -1.0F, 0.0F, 12.0F, 1.0F, 5.0F),
         PartPose.offsetAndRotation(8.0F, 21.0F, 2.5F, (float) (-Math.PI * 7.0 / 4.0), 0.0F, 0.0F)
      );
      PartDefinition p_frontShell3 = root.addOrReplaceChild(
         "frontShell3",
         CubeListBuilder.create().texOffs(2, 24).addBox(-5.0F, -1.0F, 0.0F, 10.0F, 1.0F, 2.0F),
         PartPose.offsetAndRotation(8.0F, 17.46447F, 6.035533F, 1.570796F, 6.283185F, 0.0F)
      );
      PartDefinition p_frontShell4 = root.addOrReplaceChild(
         "frontShell4",
         CubeListBuilder.create().texOffs(3, 25).addBox(-4.0F, -1.0F, 0.0F, 8.0F, 1.0F, 1.0F),
         PartPose.offsetAndRotation(8.0F, 15.46447F, 6.035533F, 1.570796F, -6.283185F, 0.0F)
      );
      PartDefinition p_frontShell5 = root.addOrReplaceChild(
         "frontShell5",
         CubeListBuilder.create().texOffs(3, 25).addBox(-3.0F, -1.0F, 0.0F, 6.0F, 1.0F, 1.0F),
         PartPose.offsetAndRotation(8.000001F, 14.46447F, 6.035532F, 1.570796F, -6.283185F, 0.0F)
      );
      PartDefinition p_muzzle3A = root.addOrReplaceChild(
         "muzzle3A", CubeListBuilder.create().texOffs(0, 14).addBox(-1.0F, 0.0F, -3.5F, 2.0F, 1.0F, 4.0F), PartPose.offset(9.0F, 9.5F, 1.5F)
      );
      PartDefinition p_muzzle3H = root.addOrReplaceChild(
         "muzzle3H",
         CubeListBuilder.create().texOffs(0, 14).addBox(-1.0F, -1.0F, -3.5F, 2.0F, 1.0F, 4.0F),
         PartPose.offsetAndRotation(9.000002F, 12.62132F, 3.621319F, -0.7853973F, 0.0F, 0.0F)
      );
      PartDefinition p_muzzle3G = root.addOrReplaceChild(
         "muzzle3G",
         CubeListBuilder.create().texOffs(0, 14).addBox(-8.0F, -1.0F, -3.5F, 16.0F, 1.0F, 4.0F),
         PartPose.offsetAndRotation(2.0F, 15.62132F, 3.621321F, -1.570796F, 0.0F, 0.0F)
      );
      PartDefinition p_muzzle3C = root.addOrReplaceChild(
         "muzzle3C",
         CubeListBuilder.create().texOffs(0, 14).addBox(-8.0F, -1.0F, -3.5F, 16.0F, 1.0F, 4.0F),
         PartPose.offsetAndRotation(2.0F, 12.62133F, -3.621317F, 1.570796F, 0.0F, 0.0F)
      );
      PartDefinition p_muzzle3D = root.addOrReplaceChild(
         "muzzle3D",
         CubeListBuilder.create().texOffs(0, 14).addBox(-8.0F, -1.0F, -3.5F, 16.0F, 1.0F, 4.0F),
         PartPose.offsetAndRotation(2.000002F, 15.62133F, -3.621318F, 0.7853978F, -3.141593F, -3.141593F)
      );
      PartDefinition p_muzzle3E = root.addOrReplaceChild(
         "muzzle3E",
         CubeListBuilder.create().texOffs(0, 14).addBox(-8.0F, -1.0F, -3.5F, 16.0F, 1.0F, 4.0F),
         PartPose.offsetAndRotation(2.0F, 17.74265F, -1.5F, -1.7E-7F, -3.141593F, -3.141593F)
      );
      PartDefinition p_muzzle3F = root.addOrReplaceChild(
         "muzzle3F",
         CubeListBuilder.create().texOffs(0, 14).addBox(-8.0F, -1.0F, -3.5F, 16.0F, 1.0F, 4.0F),
         PartPose.offsetAndRotation(2.0F, 17.74265F, 1.5F, -0.7853987F, -3.141593F, -3.141593F)
      );
      PartDefinition p_muzzle3B = root.addOrReplaceChild(
         "muzzle3B",
         CubeListBuilder.create().texOffs(0, 14).addBox(-1.0F, -1.0F, -3.5F, 2.0F, 1.0F, 4.0F),
         PartPose.offsetAndRotation(9.0F, 10.5F, -1.5F, (float) (Math.PI / 4), 0.0F, 0.0F)
      );
      PartDefinition p_muzzle2A = root.addOrReplaceChild(
         "muzzle2A", CubeListBuilder.create().texOffs(1, 15).addBox(-1.0F, 0.0F, -3.0F, 2.0F, 1.0F, 3.0F), PartPose.offset(11.0F, 10.0F, 1.5F)
      );
      PartDefinition p_muzzle2B = root.addOrReplaceChild(
         "muzzle2B",
         CubeListBuilder.create().texOffs(1, 15).addBox(-1.0F, -0.5F, -3.0F, 2.0F, 1.0F, 3.0F),
         PartPose.offsetAndRotation(11.0F, 10.5F, -1.5F, (float) (Math.PI / 4), 0.0F, 0.0F)
      );
      PartDefinition p_muzzle2C = root.addOrReplaceChild(
         "muzzle2C",
         CubeListBuilder.create().texOffs(1, 15).addBox(-1.0F, -0.5F, -3.0F, 2.0F, 1.0F, 3.0F),
         PartPose.offsetAndRotation(11.0F, 12.62132F, -3.621318F, 1.570796F, 0.0F, 0.0F)
      );
      PartDefinition p_muzzle2D = root.addOrReplaceChild(
         "muzzle2D",
         CubeListBuilder.create().texOffs(1, 15).addBox(-1.0F, -0.5F, -3.0F, 2.0F, 1.0F, 3.0F),
         PartPose.offsetAndRotation(11.0F, 15.62132F, -3.621317F, 0.7853983F, -3.141593F, -3.141593F)
      );
      PartDefinition p_muzzle2E = root.addOrReplaceChild(
         "muzzle2E",
         CubeListBuilder.create().texOffs(1, 15).addBox(-1.0F, -0.5F, -3.0F, 2.0F, 1.0F, 3.0F),
         PartPose.offsetAndRotation(11.0F, 17.74264F, -1.499998F, -2.6E-7F, -3.141593F, -3.141593F)
      );
      PartDefinition p_muzzle2F = root.addOrReplaceChild(
         "muzzle2F",
         CubeListBuilder.create().texOffs(1, 15).addBox(-1.0F, -0.5F, -3.0F, 2.0F, 1.0F, 3.0F),
         PartPose.offsetAndRotation(11.0F, 17.74264F, 1.500001F, -0.7853986F, -3.141593F, -3.141593F)
      );
      PartDefinition p_muzzle2G = root.addOrReplaceChild(
         "muzzle2G",
         CubeListBuilder.create().texOffs(1, 15).addBox(-1.0F, -0.5F, -3.0F, 2.0F, 1.0F, 3.0F),
         PartPose.offsetAndRotation(11.0F, 15.62132F, 3.621321F, -1.570796F, 0.0F, 0.0F)
      );
      PartDefinition p_muzzle2H = root.addOrReplaceChild(
         "muzzle2H",
         CubeListBuilder.create().texOffs(1, 15).addBox(-1.0F, -0.5F, -3.0F, 2.0F, 1.0F, 3.0F),
         PartPose.offsetAndRotation(11.0F, 12.62132F, 3.621319F, -0.7853976F, 0.0F, 0.0F)
      );
      PartDefinition p_muzzle1A = root.addOrReplaceChild(
         "muzzle1A", CubeListBuilder.create().texOffs(0, 10).addBox(-4.5F, 0.0F, -3.0F, 9.0F, 1.0F, 3.0F), PartPose.offset(12.5F, 10.5F, 1.5F)
      );
      PartDefinition p_muzzle1B = root.addOrReplaceChild(
         "muzzle1B",
         CubeListBuilder.create().texOffs(0, 10).addBox(-4.5F, 0.0F, -3.0F, 9.0F, 1.0F, 3.0F),
         PartPose.offsetAndRotation(12.5F, 10.5F, -1.5F, (float) (Math.PI / 4), 0.0F, 0.0F)
      );
      PartDefinition p_muzzle1C = root.addOrReplaceChild(
         "muzzle1C",
         CubeListBuilder.create().texOffs(0, 10).addBox(-4.5F, 0.0F, -3.0F, 9.0F, 1.0F, 3.0F),
         PartPose.offsetAndRotation(12.5F, 12.62132F, -3.62132F, 1.570796F, 0.0F, 0.0F)
      );
      PartDefinition p_muzzle1D = root.addOrReplaceChild(
         "muzzle1D",
         CubeListBuilder.create().texOffs(0, 10).addBox(-4.5F, 0.0F, -3.0F, 9.0F, 1.0F, 3.0F),
         PartPose.offsetAndRotation(12.5F, 15.62132F, -3.62132F, 0.7853979F, -3.141593F, -3.141593F)
      );
      PartDefinition p_muzzle1E = root.addOrReplaceChild(
         "muzzle1E",
         CubeListBuilder.create().texOffs(0, 10).addBox(-4.5F, 0.0F, -3.0F, 9.0F, 1.0F, 3.0F),
         PartPose.offsetAndRotation(12.5F, 17.74264F, -1.499999F, -3.2E-7F, -3.141593F, -3.141593F)
      );
      PartDefinition p_muzzle1F = root.addOrReplaceChild(
         "muzzle1F",
         CubeListBuilder.create().texOffs(0, 10).addBox(-4.5F, 0.0F, -3.0F, 9.0F, 1.0F, 3.0F),
         PartPose.offsetAndRotation(12.5F, 17.74264F, 1.500001F, -0.7853984F, -3.141593F, -3.141593F)
      );
      PartDefinition p_muzzle1G = root.addOrReplaceChild(
         "muzzle1G",
         CubeListBuilder.create().texOffs(0, 10).addBox(-4.5F, 0.0F, -3.0F, 9.0F, 1.0F, 3.0F),
         PartPose.offsetAndRotation(12.5F, 15.62132F, 3.62132F, -1.570796F, -6.283185F, 0.0F)
      );
      PartDefinition p_muzzle1H = root.addOrReplaceChild(
         "muzzle1H",
         CubeListBuilder.create().texOffs(0, 10).addBox(-4.5F, 0.0F, -3.0F, 9.0F, 1.0F, 3.0F),
         PartPose.offsetAndRotation(12.5F, 12.62132F, 3.621318F, -0.7853974F, 0.0F, 0.0F)
      );
      PartDefinition p_core = root.addOrReplaceChild(
         "core", CubeListBuilder.create().texOffs(0, 0).addBox(-7.0F, -1.5F, -1.5F, 18.0F, 3.0F, 3.0F), PartPose.offset(1.0F, 14.0F, 0.0F)
      );
      PartDefinition p_innerBlock = root.addOrReplaceChild(
         "innerBlock", CubeListBuilder.create().texOffs(3, 38).addBox(-4.5F, 0.0F, -3.0F, 2.0F, 5.0F, 6.0F), PartPose.offset(13.5F, 11.6F, 0.0F)
      );
      PartDefinition p_tube1 = root.addOrReplaceChild(
         "tube1", CubeListBuilder.create().texOffs(0, 6).addBox(-7.0F, 0.0F, -3.0F, 14.0F, 1.0F, 3.0F), PartPose.offset(1.0F, 10.5F, 1.5F)
      );
      PartDefinition p_tube2 = root.addOrReplaceChild(
         "tube2",
         CubeListBuilder.create().texOffs(0, 6).addBox(-7.0F, 0.0F, -3.0F, 14.0F, 1.0F, 3.0F),
         PartPose.offsetAndRotation(1.0F, 10.5F, -1.5F, (float) (Math.PI / 4), 0.0F, 0.0F)
      );
      PartDefinition p_tube3 = root.addOrReplaceChild(
         "tube3",
         CubeListBuilder.create().texOffs(0, 6).addBox(-7.0F, 0.0F, -3.0F, 14.0F, 1.0F, 3.0F),
         PartPose.offsetAndRotation(1.0F, 12.62132F, -3.62132F, 1.570796F, 0.0F, 0.0F)
      );
      PartDefinition p_tube4 = root.addOrReplaceChild(
         "tube4",
         CubeListBuilder.create().texOffs(0, 6).addBox(-7.0F, 0.0F, -3.0F, 14.0F, 1.0F, 3.0F),
         PartPose.offsetAndRotation(1.0F, 15.62132F, -3.62132F, 0.7853979F, -3.141593F, -3.141593F)
      );
      PartDefinition p_tube5 = root.addOrReplaceChild(
         "tube5",
         CubeListBuilder.create().texOffs(0, 6).addBox(-7.0F, 0.0F, -3.0F, 14.0F, 1.0F, 3.0F),
         PartPose.offsetAndRotation(1.0F, 17.74264F, -1.499999F, -3.2E-7F, -3.141593F, -3.141593F)
      );
      PartDefinition p_tube6 = root.addOrReplaceChild(
         "tube6",
         CubeListBuilder.create().texOffs(0, 6).addBox(-7.0F, 0.0F, -3.0F, 14.0F, 1.0F, 3.0F),
         PartPose.offsetAndRotation(1.0F, 17.74264F, 1.500001F, -0.7853984F, -3.141593F, -3.141593F)
      );
      PartDefinition p_tube7 = root.addOrReplaceChild(
         "tube7",
         CubeListBuilder.create().texOffs(0, 6).addBox(-7.0F, 0.0F, -3.0F, 14.0F, 1.0F, 3.0F),
         PartPose.offsetAndRotation(1.0F, 15.62132F, 3.62132F, -1.570796F, -6.283185F, 0.0F)
      );
      PartDefinition p_tube8 = root.addOrReplaceChild(
         "tube8",
         CubeListBuilder.create().texOffs(0, 6).addBox(-7.0F, 0.0F, -3.0F, 14.0F, 1.0F, 3.0F),
         PartPose.offsetAndRotation(1.0F, 12.62132F, 3.621318F, -0.7853974F, 0.0F, 0.0F)
      );
      PartDefinition p_whiteBlock1 = root.addOrReplaceChild(
         "whiteBlock1", CubeListBuilder.create().texOffs(44, 3).addBox(-8.0F, -3.5F, -6.5F, 16.0F, 7.0F, 13.0F), PartPose.offset(-13.0F, 12.45F, 0.0F)
      );
      PartDefinition p_whiteBlock9 = root.addOrReplaceChild(
         "whiteBlock9", CubeListBuilder.create().texOffs(89, 1).addBox(-0.5F, -0.5F, -4.5F, 1.0F, 1.0F, 9.0F), PartPose.offset(-21.5F, 8.45F, 0.0F)
      );
      PartDefinition p_whiteBlock8 = root.addOrReplaceChild(
         "whiteBlock8", CubeListBuilder.create().texOffs(98, 28).addBox(-0.5F, -3.5F, -4.5F, 1.0F, 7.0F, 9.0F), PartPose.offset(-22.5F, 12.45F, 0.0F)
      );
      PartDefinition p_whiteBlock7 = root.addOrReplaceChild(
         "whiteBlock7", CubeListBuilder.create().texOffs(103, 7).addBox(-0.5F, -4.0F, -5.5F, 1.0F, 8.0F, 11.0F), PartPose.offset(-21.5F, 12.95F, 0.0F)
      );
      PartDefinition p_whiteBlock6 = root.addOrReplaceChild(
         "whiteBlock6", CubeListBuilder.create().texOffs(50, 37).addBox(-8.0F, -0.5F, -3.5F, 16.0F, 1.0F, 7.0F), PartPose.offset(-13.0F, 6.450001F, 0.0F)
      );
      PartDefinition p_whiteBlock4 = root.addOrReplaceChild(
         "whiteBlock4", CubeListBuilder.create().texOffs(49, 45).addBox(-8.0F, -0.5F, -4.5F, 16.0F, 1.0F, 9.0F), PartPose.offset(-13.0F, 7.450001F, 0.0F)
      );
      PartDefinition p_whiteBlock2 = root.addOrReplaceChild(
         "whiteBlock2", CubeListBuilder.create().texOffs(46, 13).addBox(-8.0F, -0.5F, -5.5F, 16.0F, 1.0F, 11.0F), PartPose.offset(-13.0F, 8.45F, 0.0F)
      );
      PartDefinition p_whiteBlock3 = root.addOrReplaceChild(
         "whiteBlock3", CubeListBuilder.create().texOffs(44, 3).addBox(-7.0F, -0.5F, -6.5F, 14.0F, 1.0F, 13.0F), PartPose.offset(-14.0F, 16.45F, 0.0F)
      );
      PartDefinition p_whiteBlock5 = root.addOrReplaceChild(
         "whiteBlock5", CubeListBuilder.create().texOffs(49, 55).addBox(-6.0F, -0.5F, -6.5F, 12.0F, 1.0F, 13.0F), PartPose.offset(-15.0F, 17.45F, 0.0F)
      );
      PartDefinition p_whiteBlock10 = root.addOrReplaceChild(
         "whiteBlock10", CubeListBuilder.create().texOffs(44, 3).addBox(-4.5F, -0.5F, -6.5F, 9.0F, 1.0F, 13.0F), PartPose.offset(-15.0F, 18.45F, 0.0F)
      );
      PartDefinition p_indicator = root.addOrReplaceChild(
         "indicator", CubeListBuilder.create().texOffs(56, 32).addBox(-1.0F, -0.5F, -1.0F, 2.0F, 1.0F, 2.0F), PartPose.offset(-10.84F, 6.35F, 0.0F)
      );
      return LayerDefinition.create(mesh, 128, 128);
   }

   public void renderBase(PoseStack pose, VertexConsumer vc, int light, int overlay) {
      for (String n : new String[]{
         "block",
         "block2",
         "block3",
         "block4",
         "block5",
         "whiteBlock1",
         "whiteBlock9",
         "whiteBlock8",
         "whiteBlock7",
         "whiteBlock6",
         "whiteBlock4",
         "whiteBlock2",
         "whiteBlock3",
         "whiteBlock5",
         "whiteBlock10"
      }) {
         this.root.getChild(n).render(pose, vc, light, overlay);
      }
   }

   public void renderBarrel(PoseStack pose, VertexConsumer vc, int light, int overlay) {
      for (String n : new String[]{
         "claw2",
         "claw3",
         "edgeA",
         "edgeB",
         "edgeC",
         "edgeD",
         "edgeE",
         "edgeF",
         "edgeG",
         "edgeH",
         "innerBlock",
         "claw1",
         "frontShell1",
         "frontShell6",
         "frontShell7",
         "frontShell8",
         "frontShell9",
         "frontShell2",
         "frontShell3",
         "frontShell4",
         "frontShell5",
         "muzzle3A",
         "muzzle3H",
         "muzzle3G",
         "muzzle3C",
         "muzzle3D",
         "muzzle3E",
         "muzzle3F",
         "muzzle3B",
         "muzzle2A",
         "muzzle2B",
         "muzzle2C",
         "muzzle2D",
         "muzzle2E",
         "muzzle2F",
         "muzzle2G",
         "muzzle2H",
         "muzzle1A",
         "muzzle1B",
         "muzzle1C",
         "muzzle1D",
         "muzzle1E",
         "muzzle1F",
         "muzzle1G",
         "muzzle1H",
         "tube3",
         "tube4",
         "tube5",
         "tube6",
         "tube7"
      }) {
         this.root.getChild(n).render(pose, vc, light, overlay);
      }
   }

   public void renderCore(PoseStack pose, VertexConsumer vc, int light, int overlay) {
      this.renderCore(pose, vc, light, overlay, -1);
   }

   public void renderCore(PoseStack pose, VertexConsumer vc, int light, int overlay, int argb) {
      pose.pushPose();
      pose.translate(0.6875, 0.875, 0.0);
      pose.scale(0.99F, 0.7F, 0.7F);
      pose.translate(-0.6875, -0.875, 0.0);
      this.root.getChild("core").render(pose, vc, light, overlay, argb);
      pose.popPose();
      this.root.getChild("core").render(pose, vc, light, overlay, argb);
   }

   public void renderIndicator(PoseStack pose, VertexConsumer vc, int light, int overlay) {
      this.renderIndicator(pose, vc, light, overlay, -1);
   }

   public void renderIndicator(PoseStack pose, VertexConsumer vc, int light, int overlay, int argb) {
      pose.pushPose();
      pose.translate(-0.6775, 0.396875, 0.0);
      pose.scale(0.8F, 0.99F, 0.8F);
      pose.translate(0.6775, -0.396875, 0.0);
      this.root.getChild("indicator").render(pose, vc, light, overlay, argb);
      pose.popPose();
      this.root.getChild("indicator").render(pose, vc, light, overlay, argb);
   }

   public void renderClaw(PoseStack pose, VertexConsumer vc, int light, int overlay, float prog, boolean firingPortal) {
      ModelPart claw1A = this.root.getChild("claw1A");
      ModelPart claw1B = claw1A.getChild("claw1B");
      ModelPart claw1C = claw1B.getChild("claw1C");
      float p = firingPortal ? -prog : prog;
      claw1A.zRot = 0.6981316F + -0.3F * p;
      claw1B.zRot = 0.87266463F + -0.15F * prog;
      claw1C.zRot = (float) (Math.PI * 2.0 / 9.0) + 0.9F * prog;
      claw1A.render(pose, vc, light, overlay);
      pose.pushPose();
      pose.translate(0.0, 0.96875, 0.0);
      pose.mulPose(Axis.XP.rotationDegrees(135.0F));
      pose.translate(0.0, -0.96875, 0.0);
      claw1A.render(pose, vc, light, overlay);
      pose.popPose();
      pose.pushPose();
      pose.translate(0.0, 0.96875, 0.0);
      pose.mulPose(Axis.XP.rotationDegrees(-135.0F));
      pose.translate(0.0, -0.96875, 0.0);
      claw1A.render(pose, vc, light, overlay);
      pose.popPose();
   }

   public void renderTube(PoseStack pose, VertexConsumer vc, int light, int overlay) {
      this.root.getChild("tube1").render(pose, vc, light, overlay);
      this.root.getChild("tube2").render(pose, vc, light, overlay);
      this.root.getChild("tube8").render(pose, vc, light, overlay);
   }

   public void renderAll(PoseStack pose, VertexConsumer vc, int light, int overlay) {
      this.renderBase(pose, vc, light, overlay);
      this.renderBarrel(pose, vc, light, overlay);
      this.renderTube(pose, vc, light, overlay);
      this.renderCore(pose, vc, light, overlay);
      this.renderIndicator(pose, vc, light, overlay);
      this.renderClaw(pose, vc, light, overlay, 0.0F, false);
   }
}
