package danger.orespawn.client.model;

import danger.orespawn.entity.Brutalfly;
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
 * Port of gold {@code ModelBrutalfly} (1.12 ModelBase) → 1.21 HierarchicalModel.
 */
@OnlyIn(Dist.CLIENT)
public class ModelBrutalfly extends HierarchicalModel<Brutalfly> {
    private final float wingspeed;
    private final ModelPart root;
    private final ModelPart body;
    private final ModelPart leftwing;
    private final ModelPart rightwing;
    private final ModelPart leftwing2;
    private final ModelPart rightwing2;
    private final ModelPart leftwing3;
    private final ModelPart rightwing3;
    private final ModelPart head;
    private final ModelPart leftwing4;
    private final ModelPart rightwing4;
    private final ModelPart leftwing5;
    private final ModelPart leftwing6;
    private final ModelPart rightwing5;
    private final ModelPart rightwing6;

    public ModelBrutalfly(ModelPart root) {
        this(root, 0.1F);
    }

    public ModelBrutalfly(ModelPart root, float wingspeed) {
        this.root = root;
        this.wingspeed = wingspeed;
        this.body = root.getChild("body");
        this.leftwing = root.getChild("leftwing");
        this.rightwing = root.getChild("rightwing");
        this.leftwing2 = root.getChild("leftwing2");
        this.rightwing2 = root.getChild("rightwing2");
        this.leftwing3 = root.getChild("leftwing3");
        this.rightwing3 = root.getChild("rightwing3");
        this.head = root.getChild("head");
        this.leftwing4 = root.getChild("leftwing4");
        this.rightwing4 = root.getChild("rightwing4");
        this.leftwing5 = root.getChild("leftwing5");
        this.leftwing6 = root.getChild("leftwing6");
        this.rightwing5 = root.getChild("rightwing5");
        this.rightwing6 = root.getChild("rightwing6");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        root.addOrReplaceChild("body",
                CubeListBuilder.create().texOffs(21, 19)
                        .addBox(0.0F, 0.0F, -4.0F, 1.0F, 1.0F, 8.0F),
                PartPose.offset(0.0F, 17.0F, 0.0F));
        root.addOrReplaceChild("leftwing",
                CubeListBuilder.create().texOffs(43, 24)
                        .addBox(0.0F, 0.0F, -4.0F, 1.0F, 1.0F, 5.0F),
                PartPose.offset(1.0F, 17.0F, 0.0F));
        root.addOrReplaceChild("rightwing",
                CubeListBuilder.create().texOffs(43, 17)
                        .addBox(-1.0F, 0.0F, -4.0F, 1.0F, 1.0F, 5.0F),
                PartPose.offset(0.0F, 17.0F, 0.0F));
        root.addOrReplaceChild("leftwing2",
                CubeListBuilder.create().texOffs(0, 0)
                        .addBox(1.0F, 0.0F, -6.0F, 6.0F, 1.0F, 7.0F),
                PartPose.offset(1.0F, 17.0F, 0.0F));
        root.addOrReplaceChild("rightwing2",
                CubeListBuilder.create().texOffs(29, 0)
                        .addBox(-7.0F, 0.0F, -6.0F, 6.0F, 1.0F, 7.0F),
                PartPose.offset(0.0F, 17.0F, 0.0F));
        root.addOrReplaceChild("leftwing3",
                CubeListBuilder.create().texOffs(0, 9)
                        .addBox(0.0F, 0.0F, 1.0F, 5.0F, 1.0F, 5.0F),
                PartPose.offset(1.0F, 17.0F, 0.0F));
        root.addOrReplaceChild("rightwing3",
                CubeListBuilder.create().texOffs(27, 9)
                        .addBox(-5.0F, 0.0F, 1.0F, 5.0F, 1.0F, 5.0F),
                PartPose.offset(0.0F, 17.0F, 0.0F));
        root.addOrReplaceChild("head",
                CubeListBuilder.create().texOffs(21, 11)
                        .addBox(0.0F, 0.0F, -6.0F, 1.0F, 1.0F, 1.0F),
                PartPose.offset(0.0F, 17.0F, 1.0F));
        root.addOrReplaceChild("leftwing4",
                CubeListBuilder.create().texOffs(2, 24)
                        .addBox(0.0F, 0.0F, 6.0F, 2.0F, 1.0F, 7.0F),
                PartPose.offset(1.0F, 17.0F, 0.0F));
        root.addOrReplaceChild("rightwing4",
                CubeListBuilder.create().texOffs(2, 16)
                        .addBox(-2.0F, 0.0F, 6.0F, 2.0F, 1.0F, 7.0F),
                PartPose.offset(0.0F, 17.0F, 0.0F));
        root.addOrReplaceChild("leftwing5",
                CubeListBuilder.create().texOffs(21, 16)
                        .addBox(1.0F, 0.0F, -7.0F, 1.0F, 1.0F, 1.0F),
                PartPose.offset(1.0F, 17.0F, 0.0F));
        root.addOrReplaceChild("leftwing6",
                CubeListBuilder.create().texOffs(50, 10)
                        .addBox(7.0F, 0.0F, -6.0F, 2.0F, 1.0F, 1.0F),
                PartPose.offset(1.0F, 17.0F, 0.0F));
        root.addOrReplaceChild("rightwing5",
                CubeListBuilder.create().texOffs(27, 16)
                        .addBox(-2.0F, 0.0F, -7.0F, 1.0F, 1.0F, 1.0F),
                PartPose.offset(0.0F, 17.0F, 0.0F));
        root.addOrReplaceChild("rightwing6",
                CubeListBuilder.create().texOffs(50, 13)
                        .addBox(-9.0F, 0.0F, -6.0F, 2.0F, 1.0F, 1.0F),
                PartPose.offset(0.0F, 17.0F, 0.0F));
        return LayerDefinition.create(mesh, 64, 32);
    }

    @Override
    public ModelPart root() {
        return this.root;
    }

    @Override
    public void setupAnim(Brutalfly entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        animate(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
    }

    private void animate(Brutalfly entity, float f, float f1, float f2, float f3, float f4) {
        this.rightwing.zRot = Mth.cos(f2 * 1.3F * this.wingspeed) * (float) Math.PI * 0.25F;
        this.rightwing2.zRot = this.rightwing.zRot;
        this.rightwing3.zRot = this.rightwing.zRot;
        this.rightwing4.zRot = this.rightwing.zRot;
        this.rightwing5.zRot = this.rightwing.zRot;
        this.rightwing6.zRot = this.rightwing.zRot;
        this.leftwing.zRot = -this.rightwing.zRot;
        this.leftwing2.zRot = -this.rightwing.zRot;
        this.leftwing3.zRot = -this.rightwing.zRot;
        this.leftwing4.zRot = -this.rightwing.zRot;
        this.leftwing5.zRot = -this.rightwing.zRot;
        this.leftwing6.zRot = -this.rightwing.zRot;
    }
}
