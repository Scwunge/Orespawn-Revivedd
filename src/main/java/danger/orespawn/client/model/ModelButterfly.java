package danger.orespawn.client.model;

import danger.orespawn.entity.Butterfly;
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
 * Port of gold {@code ModelButterfly} (1.12 ModelBase) → 1.21 HierarchicalModel.
 */
@OnlyIn(Dist.CLIENT)
public class ModelButterfly extends HierarchicalModel<Butterfly> {
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

    public ModelButterfly(ModelPart root) {
        this(root, 0.6F);
    }

    public ModelButterfly(ModelPart root, float wingspeed) {
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
                        .addBox(0.0F, 0.0F, -4.0F, 5.0F, 1.0F, 5.0F),
                PartPose.offset(1.0F, 17.0F, 0.0F));
        root.addOrReplaceChild("rightwing",
                CubeListBuilder.create().texOffs(43, 17)
                        .addBox(-5.0F, 0.0F, -4.0F, 5.0F, 1.0F, 5.0F),
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
                        .addBox(0.0F, 0.0F, 6.0F, 1.0F, 1.0F, 7.0F),
                PartPose.offset(1.0F, 17.0F, 0.0F));
        root.addOrReplaceChild("rightwing4",
                CubeListBuilder.create().texOffs(2, 16)
                        .addBox(-1.0F, 0.0F, 6.0F, 1.0F, 1.0F, 7.0F),
                PartPose.offset(0.0F, 17.0F, 0.0F));
        return LayerDefinition.create(mesh, 64, 32);
    }

    @Override
    public ModelPart root() {
        return this.root;
    }

    @Override
    public void setupAnim(Butterfly entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        animate(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
    }

    
    private void animate(Butterfly entity, float f, float f1, float f2, float f3, float f4) {
        float wing = Mth.cos(f2 * 1.3F * this.wingspeed) * (float) Math.PI * 0.25F;
        this.rightwing.zRot = wing;
        this.rightwing2.zRot = wing;
        this.rightwing3.zRot = wing;
        this.rightwing4.zRot = wing;
        this.leftwing.zRot = -wing;
        this.leftwing2.zRot = -wing;
        this.leftwing3.zRot = -wing;
        this.leftwing4.zRot = -wing;
    }
}

