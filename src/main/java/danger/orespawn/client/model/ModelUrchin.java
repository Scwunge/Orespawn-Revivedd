package danger.orespawn.client.model;

import danger.orespawn.entity.Urchin;
import danger.orespawn.util.Reference;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

/**
 * Port of gold {@code ModelUrchin} (1.7.10 ModelBase, tex 128×128) → 1.21 HierarchicalModel.
 * Cubes / pivots / UV 1:1. Full gold {@code func_78088_a} foot walk + spike sway (attack amp).
 * wingspeed default 1.0 from ClientProxy registration.
 */
@OnlyIn(Dist.CLIENT)
public class ModelUrchin extends HierarchicalModel<Urchin> {
    /** Model layer location. */
    public static final ModelLayerLocation LAYER_LOCATION =
            new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "urchin"), "main");

    private final float wingspeed;
    private final ModelPart root;
    private final ModelPart if1;
    private final ModelPart if2;
    private final ModelPart if3;
    private final ModelPart if4;
    private final ModelPart of1;
    private final ModelPart of2;
    private final ModelPart of3;
    private final ModelPart of4;
    private final ModelPart center;
    private final ModelPart tis1;
    private final ModelPart tis2;
    private final ModelPart tis3;
    private final ModelPart tis4;
    private final ModelPart tos1;
    private final ModelPart tos2;
    private final ModelPart tos3;
    private final ModelPart tos4;

    public ModelUrchin(ModelPart root) {
        this(root, 1.0F);
    }

    public ModelUrchin(ModelPart root, float wingspeed) {
        this.root = root;
        this.wingspeed = wingspeed;
        this.if1 = root.getChild("if1");
        this.if2 = root.getChild("if2");
        this.if3 = root.getChild("if3");
        this.if4 = root.getChild("if4");
        this.of1 = root.getChild("of1");
        this.of2 = root.getChild("of2");
        this.of3 = root.getChild("of3");
        this.of4 = root.getChild("of4");
        this.center = root.getChild("center");
        this.tis1 = root.getChild("tis1");
        this.tis2 = root.getChild("tis2");
        this.tis3 = root.getChild("tis3");
        this.tis4 = root.getChild("tis4");
        this.tos1 = root.getChild("tos1");
        this.tos2 = root.getChild("tos2");
        this.tos3 = root.getChild("tos3");
        this.tos4 = root.getChild("tos4");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        // gold tex 128×128; cubes/pivots/base rotations 1:1
        root.addOrReplaceChild(
                "if1",
                CubeListBuilder.create().texOffs(0, 35).addBox(0.0F, 0.0F, 0.0F, 1.0F, 8.0F, 1.0F),
                PartPose.offsetAndRotation(0.0F, 16.0F, 0.0F, (float) (Math.PI / 12), 0.0F, 0.0F));
        root.addOrReplaceChild(
                "if2",
                CubeListBuilder.create().texOffs(5, 35).addBox(0.0F, 0.0F, 0.0F, 1.0F, 8.0F, 1.0F),
                PartPose.offsetAndRotation(0.0F, 16.0F, 0.0F, (float) (-Math.PI / 12), 0.0F, 0.0F));
        root.addOrReplaceChild(
                "if3",
                CubeListBuilder.create().texOffs(10, 35).addBox(0.0F, 0.0F, 0.0F, 1.0F, 8.0F, 1.0F),
                PartPose.offsetAndRotation(0.0F, 16.0F, 0.0F, 0.0F, 0.0F, (float) (Math.PI / 12)));
        root.addOrReplaceChild(
                "if4",
                CubeListBuilder.create().texOffs(15, 35).addBox(0.0F, 0.0F, 0.0F, 1.0F, 8.0F, 1.0F),
                PartPose.offsetAndRotation(0.0F, 16.0F, 0.0F, 0.0F, 0.0F, (float) (-Math.PI / 12)));
        root.addOrReplaceChild(
                "of1",
                CubeListBuilder.create().texOffs(0, 45).addBox(0.0F, 0.0F, 0.0F, 1.0F, 8.0F, 1.0F),
                PartPose.offsetAndRotation(2.0F, 16.0F, 0.0F, 0.0F, 0.0F, (float) (-Math.PI / 6)));
        root.addOrReplaceChild(
                "of2",
                CubeListBuilder.create().texOffs(5, 45).addBox(0.0F, 0.0F, 0.0F, 1.0F, 8.0F, 1.0F),
                PartPose.offsetAndRotation(-2.0F, 16.0F, 0.0F, 0.0F, 0.0F, (float) (Math.PI / 6)));
        root.addOrReplaceChild(
                "of3",
                CubeListBuilder.create().texOffs(10, 45).addBox(0.0F, 0.0F, 0.0F, 1.0F, 8.0F, 1.0F),
                PartPose.offsetAndRotation(0.0F, 16.0F, -2.0F, (float) (-Math.PI / 6), 0.0F, 0.0F));
        root.addOrReplaceChild(
                "of4",
                CubeListBuilder.create().texOffs(15, 45).addBox(0.0F, 0.0F, 0.0F, 1.0F, 8.0F, 1.0F),
                PartPose.offsetAndRotation(0.0F, 16.0F, 2.0F, (float) (Math.PI / 6), 0.0F, 0.0F));
        root.addOrReplaceChild(
                "center",
                CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, -30.0F, 0.0F, 1.0F, 30.0F, 1.0F),
                PartPose.offset(0.0F, 16.0F, 0.0F));
        root.addOrReplaceChild(
                "tis1",
                CubeListBuilder.create().texOffs(25, 0).addBox(0.0F, -25.0F, 0.0F, 1.0F, 25.0F, 1.0F),
                PartPose.offsetAndRotation(0.0F, 16.0F, 0.0F, (float) (Math.PI / 12), 0.0F, 0.0F));
        root.addOrReplaceChild(
                "tis2",
                CubeListBuilder.create().texOffs(30, 0).addBox(0.0F, -25.0F, 0.0F, 1.0F, 25.0F, 1.0F),
                PartPose.offsetAndRotation(0.0F, 16.0F, 0.0F, (float) (-Math.PI / 12), 0.0F, 0.0F));
        root.addOrReplaceChild(
                "tis3",
                CubeListBuilder.create().texOffs(35, 0).addBox(0.0F, -25.0F, 0.0F, 1.0F, 25.0F, 1.0F),
                PartPose.offsetAndRotation(0.0F, 16.0F, 0.0F, 0.0F, 0.0F, (float) (Math.PI / 12)));
        root.addOrReplaceChild(
                "tis4",
                CubeListBuilder.create().texOffs(40, 0).addBox(0.0F, -25.0F, 0.0F, 1.0F, 25.0F, 1.0F),
                PartPose.offsetAndRotation(0.0F, 16.0F, 0.0F, 0.0F, 0.0F, (float) (-Math.PI / 12)));
        root.addOrReplaceChild(
                "tos1",
                CubeListBuilder.create().texOffs(5, 0).addBox(0.0F, -20.0F, 0.0F, 1.0F, 20.0F, 1.0F),
                PartPose.offsetAndRotation(0.0F, 16.0F, 2.0F, (float) (-Math.PI / 6), 0.0F, 0.0F));
        root.addOrReplaceChild(
                "tos2",
                CubeListBuilder.create().texOffs(10, 0).addBox(-2.0F, -20.0F, 0.0F, 1.0F, 20.0F, 1.0F),
                PartPose.offsetAndRotation(0.0F, 16.0F, 0.0F, 0.0F, 0.0F, (float) (-Math.PI / 6)));
        root.addOrReplaceChild(
                "tos3",
                CubeListBuilder.create().texOffs(15, 0).addBox(0.0F, -20.0F, 0.0F, 1.0F, 20.0F, 1.0F),
                PartPose.offsetAndRotation(2.0F, 16.0F, 0.0F, 0.0F, 0.0F, (float) (Math.PI / 6)));
        root.addOrReplaceChild(
                "tos4",
                CubeListBuilder.create().texOffs(20, 0).addBox(0.0F, -20.0F, 0.0F, 1.0F, 20.0F, 1.0F),
                PartPose.offsetAndRotation(0.0F, 16.0F, -2.0F, (float) (Math.PI / 6), 0.0F, 0.0F));

        return LayerDefinition.create(mesh, 128, 128);
    }

    @Override
    public ModelPart root() {
        return this.root;
    }

    @Override
    public void setupAnim(
            Urchin entity,
            float limbSwing,
            float limbSwingAmount,
            float ageInTicks,
            float netHeadYaw,
            float headPitch) {
        // gold func_78088_a — f1=limbSwingAmount, f2=ageInTicks
        float newangle;
        float newangle1;
        float newangle2;
        float newangle3;
        float newangle4;
        if (limbSwingAmount > 0.1F) {
            newangle = Mth.cos(ageInTicks * 0.7F * this.wingspeed) * (float) Math.PI * 0.15F * limbSwingAmount;
            newangle1 = Mth.cos(ageInTicks * 1.7F * this.wingspeed) * (float) Math.PI * 0.15F * limbSwingAmount;
            newangle2 = Mth.cos(ageInTicks * 1.65F * this.wingspeed) * (float) Math.PI * 0.15F * limbSwingAmount;
            newangle3 = Mth.cos(ageInTicks * 1.75F * this.wingspeed) * (float) Math.PI * 0.15F * limbSwingAmount;
            newangle4 = Mth.cos(ageInTicks * 1.8F * this.wingspeed) * (float) Math.PI * 0.15F * limbSwingAmount;
        } else {
            newangle = 0.0F;
            newangle1 = 0.0F;
            newangle2 = 0.0F;
            newangle3 = 0.0F;
            newangle4 = 0.0F;
        }

        // foot walk (inner / outer)
        this.if1.xRot = 0.261F + newangle1;
        this.if2.xRot = -0.261F - newangle2;
        this.if3.xRot = newangle3;
        this.if4.xRot = -newangle4;
        // restore base zRot on if3/if4 (gold only overwrote xRot)
        this.if3.zRot = (float) (Math.PI / 12);
        this.if4.zRot = (float) (-Math.PI / 12);
        this.of1.zRot = -0.523F + newangle;
        this.of2.zRot = 0.523F - newangle;
        this.of3.xRot = -0.523F + newangle;
        this.of4.xRot = 0.523F - newangle;

        float newangle5;
        float newangle6;
        float newangle7;
        float newangle8;
        if (entity.getAttacking() != 0) {
            newangle = (float) (ageInTicks * 0.2F % (Math.PI * 2));
            newangle1 = Mth.cos(ageInTicks * 0.7F * this.wingspeed) * (float) Math.PI * 0.06F;
            newangle2 = Mth.cos(ageInTicks * 0.65F * this.wingspeed) * (float) Math.PI * 0.06F;
            newangle3 = Mth.cos(ageInTicks * 0.75F * this.wingspeed) * (float) Math.PI * 0.06F;
            newangle4 = Mth.cos(ageInTicks * 0.8F * this.wingspeed) * (float) Math.PI * 0.06F;
            newangle5 = Mth.cos(ageInTicks * 0.55F * this.wingspeed) * (float) Math.PI * 0.06F;
            newangle6 = Mth.cos(ageInTicks * 0.45F * this.wingspeed) * (float) Math.PI * 0.06F;
            newangle7 = Mth.cos(ageInTicks * 0.35F * this.wingspeed) * (float) Math.PI * 0.06F;
            newangle8 = Mth.cos(ageInTicks * 0.4F * this.wingspeed) * (float) Math.PI * 0.06F;
        } else {
            newangle = (float) (ageInTicks * 0.02F % (Math.PI * 2));
            newangle1 = Mth.cos(ageInTicks * 0.07F * this.wingspeed) * (float) Math.PI * 0.02F;
            newangle2 = Mth.cos(ageInTicks * 0.065F * this.wingspeed) * (float) Math.PI * 0.02F;
            newangle3 = Mth.cos(ageInTicks * 0.075F * this.wingspeed) * (float) Math.PI * 0.02F;
            newangle4 = Mth.cos(ageInTicks * 0.08F * this.wingspeed) * (float) Math.PI * 0.02F;
            newangle5 = Mth.cos(ageInTicks * 0.055F * this.wingspeed) * (float) Math.PI * 0.02F;
            newangle6 = Mth.cos(ageInTicks * 0.045F * this.wingspeed) * (float) Math.PI * 0.02F;
            newangle7 = Mth.cos(ageInTicks * 0.035F * this.wingspeed) * (float) Math.PI * 0.02F;
            newangle8 = Mth.cos(ageInTicks * 0.04F * this.wingspeed) * (float) Math.PI * 0.02F;
        }

        // center spin + spike sway
        this.center.yRot = newangle;
        this.tis1.xRot = 0.261F + newangle1;
        this.tis2.xRot = -0.261F + newangle2;
        this.tis3.xRot = newangle3;
        this.tis4.xRot = newangle4;
        this.tis1.zRot = newangle5;
        this.tis2.zRot = newangle6;
        this.tis3.zRot = 0.261F + newangle7;
        this.tis4.zRot = -0.261F + newangle8;
        this.tos1.xRot = -0.532F + newangle1;
        this.tos2.xRot = newangle7;
        this.tos3.xRot = newangle3;
        this.tos4.xRot = 0.532F + newangle5;
        this.tos1.zRot = newangle4;
        this.tos2.zRot = -0.523F + newangle6;
        this.tos3.zRot = 0.523F + newangle2;
        this.tos4.zRot = newangle8;
    }
}
