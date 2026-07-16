package danger.orespawn.client.model;

import danger.orespawn.entity.Fairy;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

/**
 * Port of gold {@code ModelFairy} (1.7.10 ModelBase) → 1.21 HierarchicalModel.
 * Cubes / pivots / UV 1:1; wingspeed default 1.5 from ClientProxy registration.
 */
@OnlyIn(Dist.CLIENT)
public class ModelFairy extends HierarchicalModel<Fairy> {
    private final float wingspeed;
    private final ModelPart root;
    private final ModelPart head;
    private final ModelPart chest;
    private final ModelPart waist;
    private final ModelPart hips;
    private final ModelPart lleg1;
    private final ModelPart lleg2;
    private final ModelPart rleg;
    private final ModelPart b1;
    private final ModelPart b2;
    private final ModelPart larm;
    private final ModelPart rarm;
    private final ModelPart lwing2;
    private final ModelPart lwing1;
    private final ModelPart rwing2;
    private final ModelPart rwing1;

    public ModelFairy(ModelPart root) {
        this(root, 1.5F);
    }

    public ModelFairy(ModelPart root, float wingspeed) {
        this.root = root;
        this.wingspeed = wingspeed;
        this.head = root.getChild("head");
        this.chest = root.getChild("chest");
        this.waist = root.getChild("waist");
        this.hips = root.getChild("hips");
        this.lleg1 = root.getChild("lleg1");
        this.lleg2 = root.getChild("lleg2");
        this.rleg = root.getChild("rleg");
        this.b1 = root.getChild("b1");
        this.b2 = root.getChild("b2");
        this.larm = root.getChild("larm");
        this.rarm = root.getChild("rarm");
        this.lwing2 = root.getChild("lwing2");
        this.lwing1 = root.getChild("lwing1");
        this.rwing2 = root.getChild("rwing2");
        this.rwing1 = root.getChild("rwing1");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        root.addOrReplaceChild(
                "head",
                CubeListBuilder.create().texOffs(0, 0).addBox(-2.5F, -5.0F, -2.5F, 5.0F, 5.0F, 5.0F),
                PartPose.offset(0.0F, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "chest",
                CubeListBuilder.create().texOffs(31, 5).addBox(-3.5F, 0.0F, -1.0F, 7.0F, 4.0F, 3.0F),
                PartPose.offset(0.0F, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "waist",
                CubeListBuilder.create().texOffs(33, 13).addBox(-2.5F, 4.0F, -1.0F, 5.0F, 3.0F, 3.0F),
                PartPose.offset(0.0F, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "hips",
                CubeListBuilder.create().texOffs(31, 20).addBox(-3.0F, 7.0F, -1.0F, 6.0F, 4.0F, 4.0F),
                PartPose.offset(0.0F, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "lleg1",
                CubeListBuilder.create().texOffs(53, 8).addBox(0.0F, 0.0F, 0.0F, 2.0F, 7.0F, 2.0F),
                PartPose.offsetAndRotation(1.0F, 10.0F, 0.0F, (float) (-Math.PI / 4), 0.0F, 0.0F));
        root.addOrReplaceChild(
                "lleg2",
                CubeListBuilder.create().texOffs(53, 18).addBox(0.0F, 0.0F, 0.0F, 2.0F, 8.0F, 2.0F),
                PartPose.offsetAndRotation(1.0F, 15.0F, -5.0F, 0.7679449F, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "rleg",
                CubeListBuilder.create().texOffs(51, 30).addBox(-3.0F, 0.0F, 0.0F, 2.0F, 13.0F, 2.0F),
                PartPose.offset(0.0F, 11.0F, 0.0F));
        root.addOrReplaceChild(
                "b1",
                CubeListBuilder.create().texOffs(42, 1).addBox(1.0F, 1.0F, -2.0F, 2.0F, 2.0F, 1.0F),
                PartPose.offset(0.0F, 1.0F, 0.0F));
        root.addOrReplaceChild(
                "b2",
                CubeListBuilder.create().texOffs(32, 1).addBox(-3.0F, 2.0F, -2.0F, 2.0F, 2.0F, 1.0F),
                PartPose.offset(0.0F, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "larm",
                CubeListBuilder.create().texOffs(7, 14).addBox(0.0F, 0.0F, 0.0F, 1.0F, 10.0F, 1.0F),
                PartPose.offsetAndRotation(3.0F, 0.0F, 0.0F, -0.0174533F, 0.0F, -0.122173F));
        root.addOrReplaceChild(
                "rarm",
                CubeListBuilder.create().texOffs(2, 14).addBox(-1.0F, 0.0F, 0.0F, 1.0F, 10.0F, 1.0F),
                PartPose.offsetAndRotation(-3.0F, 0.0F, 0.0F, -0.0174533F, 0.0F, 0.122173F));
        root.addOrReplaceChild(
                "lwing2",
                CubeListBuilder.create().texOffs(0, 47).addBox(0.0F, -9.0F, 0.0F, 26.0F, 16.0F, 0.0F),
                PartPose.offsetAndRotation(2.0F, 0.0F, 2.0F, 0.0F, -0.5934119F, 0.0F));
        root.addOrReplaceChild(
                "lwing1",
                CubeListBuilder.create().texOffs(0, 30).addBox(0.0F, -7.0F, 0.0F, 24.0F, 16.0F, 0.0F),
                PartPose.offsetAndRotation(2.0F, 3.0F, 2.0F, 0.0F, -0.8203047F, 0.0F));
        root.addOrReplaceChild(
                "rwing2",
                CubeListBuilder.create().texOffs(0, 30).addBox(0.0F, -7.0F, 0.0F, 24.0F, 16.0F, 0.0F),
                PartPose.offsetAndRotation(-2.0F, 3.0F, 2.0F, 0.0F, -2.356194F, 0.0F));
        root.addOrReplaceChild(
                "rwing1",
                CubeListBuilder.create().texOffs(0, 47).addBox(0.0F, -9.0F, 0.0F, 26.0F, 16.0F, 0.0F),
                PartPose.offsetAndRotation(-2.0F, 0.0F, 2.0F, 0.0F, -2.548181F, 0.0F));

        return LayerDefinition.create(mesh, 64, 64);
    }

    @Override
    public ModelPart root() {
        return this.root;
    }

    @Override
    public void setupAnim(
            Fairy entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.lwing1.yRot = -0.6F + Mth.cos(ageInTicks * this.wingspeed) * (float) Math.PI * 0.35F;
        this.rwing1.yRot = -2.55F - Mth.cos(ageInTicks * this.wingspeed) * (float) Math.PI * 0.35F;
        this.lwing2.yRot = -0.6F + Mth.cos(ageInTicks * this.wingspeed * 0.85F) * (float) Math.PI * 0.25F;
        this.rwing2.yRot = -2.55F - Mth.cos(ageInTicks * this.wingspeed * 0.85F) * (float) Math.PI * 0.25F;

        this.head.yRot = (float) Math.toRadians(netHeadYaw) * 0.45F;
        if (this.head.yRot > 0.45F) {
            this.head.yRot = 0.45F;
        }
        if (this.head.yRot < -0.45F) {
            this.head.yRot = -0.45F;
        }
        this.head.xRot = (float) Math.toRadians(headPitch);

        this.larm.xRot = -0.2F + Mth.cos(ageInTicks * this.wingspeed * 0.15F) * (float) Math.PI * 0.05F;
        this.rarm.xRot = -0.2F + Mth.cos(ageInTicks * this.wingspeed * 0.12F) * (float) Math.PI * 0.05F;
        this.larm.zRot = -0.15F + Mth.cos(ageInTicks * this.wingspeed * 0.1F) * (float) Math.PI * 0.03F;
        this.rarm.zRot = 0.15F + Mth.cos(ageInTicks * this.wingspeed * 0.11F) * (float) Math.PI * 0.03F;
        // gold OpenGlHelper blink fullbright on body — handled by FairyRenderer.getBlockLightLevel
    }
}
