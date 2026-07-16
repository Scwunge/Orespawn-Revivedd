package danger.orespawn.client.model;

import danger.orespawn.entity.AttackSquid;
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
 * Port of gold {@code ModelAttackSquid} (1.7.10 ModelBase, tex 64×32) → 1.21 HierarchicalModel.
 * Cubes / pivots / UV 1:1. Full gold {@code func_78088_a} tentacle + body bob/yaw.
 * wingspeed default 1.0 from ClientProxy registration.
 */
@OnlyIn(Dist.CLIENT)
public class ModelAttackSquid extends HierarchicalModel<AttackSquid> {
    /** Model layer location. */
    public static final ModelLayerLocation LAYER_LOCATION =
            new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "attack_squid"), "main");

    private final float wingspeed;
    private final ModelPart root;
    private final ModelPart tent1;
    private final ModelPart tent2;
    private final ModelPart tent3;
    private final ModelPart tent4;
    private final ModelPart tent5;
    private final ModelPart tent6;
    private final ModelPart tent7;
    private final ModelPart body;
    private final ModelPart tent8;

    public ModelAttackSquid(ModelPart root) {
        this(root, 1.0F);
    }

    public ModelAttackSquid(ModelPart root, float wingspeed) {
        this.root = root;
        this.wingspeed = wingspeed;
        this.tent1 = root.getChild("tent1");
        this.tent2 = root.getChild("tent2");
        this.tent3 = root.getChild("tent3");
        this.tent4 = root.getChild("tent4");
        this.tent5 = root.getChild("tent5");
        this.tent6 = root.getChild("tent6");
        this.tent7 = root.getChild("tent7");
        this.body = root.getChild("body");
        this.tent8 = root.getChild("tent8");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        // gold tex 64×32; cubes/pivots/base rotations 1:1
        root.addOrReplaceChild(
                "tent1",
                CubeListBuilder.create().texOffs(0, 18).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 9.0F, 2.0F),
                PartPose.offsetAndRotation(5.0F, 15.0F, -1.0F, -0.9250245F, -1.745329F, 0.0F));
        root.addOrReplaceChild(
                "tent2",
                CubeListBuilder.create().texOffs(0, 18).addBox(-8.0F, -1.0F, -1.0F, 8.0F, 2.0F, 2.0F),
                PartPose.offsetAndRotation(-2.0F, 15.0F, -3.0F, -0.1745329F, -0.6632251F, -0.2443461F));
        root.addOrReplaceChild(
                "tent3",
                CubeListBuilder.create().texOffs(0, 18).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 10.0F, 2.0F),
                PartPose.offsetAndRotation(1.0F, 15.0F, -4.0F, -1.134464F, 0.3316126F, 0.0F));
        root.addOrReplaceChild(
                "tent4",
                CubeListBuilder.create().texOffs(0, 18).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 10.0F, 2.0F),
                PartPose.offsetAndRotation(-3.0F, 15.0F, -1.0F, 0.5585054F, -1.692969F, 0.0F));
        root.addOrReplaceChild(
                "tent5",
                CubeListBuilder.create().texOffs(0, 18).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 10.0F, 2.0F),
                PartPose.offsetAndRotation(1.0F, 15.0F, 3.0F, 0.5410521F, 0.2268928F, 0.0F));
        root.addOrReplaceChild(
                "tent6",
                CubeListBuilder.create().texOffs(0, 18).addBox(-1.0F, -1.0F, 0.0F, 2.0F, 2.0F, 8.0F),
                PartPose.offsetAndRotation(-2.0F, 15.0F, 2.0F, -0.418879F, -0.6806784F, 0.0F));
        root.addOrReplaceChild(
                "tent7",
                CubeListBuilder.create().texOffs(0, 18).addBox(0.0F, -1.0F, -1.0F, 8.0F, 2.0F, 2.0F),
                PartPose.offsetAndRotation(3.0F, 15.0F, 1.0F, -0.1919862F, -0.6632251F, 0.418879F));
        root.addOrReplaceChild(
                "body",
                CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -10.0F, -4.0F, 8.0F, 10.0F, 8.0F),
                PartPose.offsetAndRotation(1.0F, 16.0F, -1.0F, -0.1919862F, -0.6806784F, 0.0F));
        root.addOrReplaceChild(
                "tent8",
                CubeListBuilder.create().texOffs(0, 18).addBox(-1.0F, -1.0F, -8.0F, 2.0F, 2.0F, 8.0F),
                PartPose.offsetAndRotation(3.0F, 15.0F, -4.0F, 0.1919862F, -0.6806784F, 0.0F));

        return LayerDefinition.create(mesh, 64, 32);
    }

    @Override
    public ModelPart root() {
        return this.root;
    }

    @Override
    public void setupAnim(
            AttackSquid entity,
            float limbSwing,
            float limbSwingAmount,
            float ageInTicks,
            float netHeadYaw,
            float headPitch) {
        // gold func_78088_a — f1=limbSwingAmount, f2=ageInTicks, f3=netHeadYaw
        float newangleA;
        float newangleB;
        float newangle1;
        float newangle2;
        float newangle3;
        float newangle4;
        float newangle5;
        float newangle6;
        float newangle7;
        float newangle8;

        if (limbSwingAmount > 0.1F) {
            newangleA = Mth.cos(ageInTicks * 0.25F * this.wingspeed) * (float) Math.PI * 0.04F * limbSwingAmount;
            newangleB = Mth.cos(ageInTicks * 0.39F * this.wingspeed) * (float) Math.PI * 0.04F * limbSwingAmount;
            newangle1 = Mth.cos(ageInTicks * 1.2F * this.wingspeed) * (float) Math.PI * 0.4F * limbSwingAmount;
            newangle2 = Mth.cos(ageInTicks * 1.1F * this.wingspeed) * (float) Math.PI * 0.4F * limbSwingAmount;
            newangle3 = Mth.cos(ageInTicks * 1.0F * this.wingspeed) * (float) Math.PI * 0.4F * limbSwingAmount;
            newangle4 = Mth.cos(ageInTicks * 1.9F * this.wingspeed) * (float) Math.PI * 0.4F * limbSwingAmount;
            newangle5 = Mth.cos(ageInTicks * 1.8F * this.wingspeed) * (float) Math.PI * 0.4F * limbSwingAmount;
            newangle6 = Mth.cos(ageInTicks * 1.7F * this.wingspeed) * (float) Math.PI * 0.4F * limbSwingAmount;
            newangle7 = Mth.cos(ageInTicks * 1.6F * this.wingspeed) * (float) Math.PI * 0.4F * limbSwingAmount;
            newangle8 = Mth.cos(ageInTicks * 1.5F * this.wingspeed) * (float) Math.PI * 0.4F * limbSwingAmount;
        } else {
            newangleA = Mth.cos(ageInTicks * 0.25F * this.wingspeed) * (float) Math.PI * 0.01F;
            newangleB = Mth.cos(ageInTicks * 0.39F * this.wingspeed) * (float) Math.PI * 0.01F;
            newangle1 = Mth.cos(ageInTicks * 1.2F * this.wingspeed) * (float) Math.PI * 0.1F;
            newangle2 = Mth.cos(ageInTicks * 1.1F * this.wingspeed) * (float) Math.PI * 0.1F;
            newangle3 = Mth.cos(ageInTicks * 1.0F * this.wingspeed) * (float) Math.PI * 0.1F;
            newangle4 = Mth.cos(ageInTicks * 1.9F * this.wingspeed) * (float) Math.PI * 0.1F;
            newangle5 = Mth.cos(ageInTicks * 1.8F * this.wingspeed) * (float) Math.PI * 0.1F;
            newangle6 = Mth.cos(ageInTicks * 1.7F * this.wingspeed) * (float) Math.PI * 0.1F;
            newangle7 = Mth.cos(ageInTicks * 1.6F * this.wingspeed) * (float) Math.PI * 0.1F;
            newangle8 = Mth.cos(ageInTicks * 1.5F * this.wingspeed) * (float) Math.PI * 0.1F;
        }

        // gold animates one axis per tentacle; re-apply full absolute pose each frame
        // field_78795_f→xRot, field_78796_g→yRot, field_78808_h→zRot
        this.tent1.xRot = newangle1 - 1.03F;
        this.tent1.yRot = -1.745329F;
        this.tent1.zRot = 0.0F;

        this.tent7.xRot = -0.1919862F;
        this.tent7.yRot = -0.6632251F;
        this.tent7.zRot = newangle2 + 0.37F;

        this.tent5.xRot = newangle3 + 0.6F;
        this.tent5.yRot = 0.2268928F;
        this.tent5.zRot = 0.0F;

        this.tent6.xRot = newangle4 - 0.48F;
        this.tent6.yRot = -0.6806784F;
        this.tent6.zRot = 0.0F;

        this.tent4.xRot = newangle5 + 0.63F;
        this.tent4.yRot = -1.692969F;
        this.tent4.zRot = 0.0F;

        this.tent2.xRot = -0.1745329F;
        this.tent2.yRot = -0.6632251F;
        this.tent2.zRot = newangle6 - 0.26F;

        this.tent3.xRot = newangle7 - 1.03F;
        this.tent3.yRot = 0.3316126F;
        this.tent3.zRot = 0.0F;

        this.tent8.xRot = newangle8 + 0.43F;
        this.tent8.yRot = -0.6806784F;
        this.tent8.zRot = 0.0F;

        // gold overwrites body base rot each frame (bob + head yaw)
        this.body.xRot = newangleA;
        this.body.zRot = newangleB;
        this.body.yRot = (float) Math.toRadians(netHeadYaw) * 0.75F;
    }
}
