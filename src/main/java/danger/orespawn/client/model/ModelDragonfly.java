package danger.orespawn.client.model;

import danger.orespawn.entity.Dragonfly;
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
 * Port of gold {@code ModelDragonfly} (1.12 ModelBase) → 1.21 HierarchicalModel.
 */
@OnlyIn(Dist.CLIENT)
public class ModelDragonfly extends HierarchicalModel<Dragonfly> {
    private final float wingspeed;
    private final ModelPart root;
    private final ModelPart Shape1;
    private final ModelPart lfwing;
    private final ModelPart Shape3;
    private final ModelPart Shape4;
    private final ModelPart Shape5;
    private final ModelPart rjaw;
    private final ModelPart ljaw;
    private final ModelPart tail1;
    private final ModelPart tail2;
    private final ModelPart Shape10;
    private final ModelPart Shape11;
    private final ModelPart Shape12;
    private final ModelPart Shape13;
    private final ModelPart Shape14;
    private final ModelPart Shape15;
    private final ModelPart Shape16;
    private final ModelPart Shape17;
    private final ModelPart Shape18;
    private final ModelPart Shape19;
    private final ModelPart Shape20;
    private final ModelPart Shape21;
    private final ModelPart Shape22;
    private final ModelPart Shape23;
    private final ModelPart lrwing;
    private final ModelPart rfwing;
    private final ModelPart rrwing;

    public ModelDragonfly(ModelPart root) {
        this(root, 1.5F);
    }

    public ModelDragonfly(ModelPart root, float wingspeed) {
        this.root = root;
        this.wingspeed = wingspeed;
        this.Shape1 = root.getChild("Shape1");
        this.lfwing = root.getChild("lfwing");
        this.Shape3 = root.getChild("Shape3");
        this.Shape4 = root.getChild("Shape4");
        this.Shape5 = root.getChild("Shape5");
        this.rjaw = root.getChild("rjaw");
        this.ljaw = root.getChild("ljaw");
        this.tail1 = root.getChild("tail1");
        this.tail2 = root.getChild("tail2");
        this.Shape10 = root.getChild("Shape10");
        this.Shape11 = root.getChild("Shape11");
        this.Shape12 = root.getChild("Shape12");
        this.Shape13 = root.getChild("Shape13");
        this.Shape14 = root.getChild("Shape14");
        this.Shape15 = root.getChild("Shape15");
        this.Shape16 = root.getChild("Shape16");
        this.Shape17 = root.getChild("Shape17");
        this.Shape18 = root.getChild("Shape18");
        this.Shape19 = root.getChild("Shape19");
        this.Shape20 = root.getChild("Shape20");
        this.Shape21 = root.getChild("Shape21");
        this.Shape22 = root.getChild("Shape22");
        this.Shape23 = root.getChild("Shape23");
        this.lrwing = root.getChild("lrwing");
        this.rfwing = root.getChild("rfwing");
        this.rrwing = root.getChild("rrwing");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        root.addOrReplaceChild("Shape1",
                CubeListBuilder.create().texOffs(0, 0)
                        .addBox(0.0F, 0.0F, 0.0F, 5.0F, 4.0F, 7.0F),
                PartPose.offset(0.0F, 16.0F, 0.0F));
        root.addOrReplaceChild("lfwing",
                CubeListBuilder.create().texOffs(0, 33)
                        .addBox(0.0F, 0.0F, 0.0F, 10.0F, 1.0F, 3.0F),
                PartPose.offsetAndRotation(5.0F, 16.0F, 1.0F, 0.0F, 0.4886922F, 0.0F));
        root.addOrReplaceChild("Shape3",
                CubeListBuilder.create().texOffs(0, 13)
                        .addBox(-2.0F, 0.0F, -4.0F, 4.0F, 3.0F, 4.0F),
                PartPose.offsetAndRotation(2.5F, 16.0F, -1.0F, 0.4886922F, 0.0F, 0.0F));
        root.addOrReplaceChild("Shape4",
                CubeListBuilder.create().texOffs(9, 21)
                        .addBox(0.0F, 0.0F, 0.0F, 1.0F, 2.0F, 3.0F),
                PartPose.offsetAndRotation(1.0F, 18.0F, -6.0F, 0.4886922F, 0.1745329F, 0.0F));
        root.addOrReplaceChild("Shape5",
                CubeListBuilder.create().texOffs(0, 21)
                        .addBox(0.0F, 0.0F, 0.0F, 1.0F, 2.0F, 3.0F),
                PartPose.offsetAndRotation(3.0F, 18.0F, -6.0F, 0.4886922F, -0.1745329F, 0.0F));
        root.addOrReplaceChild("rjaw",
                CubeListBuilder.create().texOffs(0, 27)
                        .addBox(-1.0F, 0.0F, 0.0F, 1.0F, 3.0F, 1.0F),
                PartPose.offsetAndRotation(2.0F, 19.0F, -5.0F, 0.4363323F, 0.1745329F, 0.0F));
        root.addOrReplaceChild("ljaw",
                CubeListBuilder.create().texOffs(5, 27)
                        .addBox(0.0F, 0.0F, 0.0F, 1.0F, 3.0F, 1.0F),
                PartPose.offsetAndRotation(3.0F, 19.0F, -5.0F, 0.4363323F, -0.1745329F, 0.0F));
        root.addOrReplaceChild("tail1",
                CubeListBuilder.create().texOffs(25, 0)
                        .addBox(-1.0F, 0.0F, 0.0F, 3.0F, 3.0F, 7.0F),
                PartPose.offset(2.0F, 16.0F, 7.0F));
        root.addOrReplaceChild("tail2",
                CubeListBuilder.create().texOffs(25, 11)
                        .addBox(0.0F, 0.0F, 0.0F, 1.0F, 2.0F, 9.0F),
                PartPose.offset(2.0F, 16.0F, 14.0F));
        root.addOrReplaceChild("Shape10",
                CubeListBuilder.create().texOffs(23, 0)
                        .addBox(-1.0F, 0.0F, 0.0F, 1.0F, 4.0F, 1.0F),
                PartPose.offsetAndRotation(1.0F, 18.0F, 0.0F, -0.2792527F, 0.0F, 0.3490659F));
        root.addOrReplaceChild("Shape11",
                CubeListBuilder.create().texOffs(40, 0)
                        .addBox(0.0F, 0.0F, -4.0F, 1.0F, 1.0F, 4.0F),
                PartPose.offset(-1.0F, 21.0F, 0.0F));
        root.addOrReplaceChild("Shape12",
                CubeListBuilder.create().texOffs(18, 12)
                        .addBox(-1.0F, 0.0F, 0.0F, 1.0F, 3.0F, 1.0F),
                PartPose.offsetAndRotation(0.0F, 21.0F, -4.0F, 0.0F, 0.0F, -0.1919862F));
        root.addOrReplaceChild("Shape13",
                CubeListBuilder.create().texOffs(18, 0)
                        .addBox(0.0F, 0.0F, 0.0F, 1.0F, 4.0F, 1.0F),
                PartPose.offsetAndRotation(4.0F, 18.0F, 0.0F, -0.2792527F, 0.0F, -0.3490659F));
        root.addOrReplaceChild("Shape14",
                CubeListBuilder.create().texOffs(51, 0)
                        .addBox(0.0F, 0.0F, -4.0F, 1.0F, 1.0F, 4.0F),
                PartPose.offset(5.0F, 21.0F, 0.0F));
        root.addOrReplaceChild("Shape15",
                CubeListBuilder.create().texOffs(13, 12)
                        .addBox(0.0F, 0.0F, 0.0F, 1.0F, 3.0F, 1.0F),
                PartPose.offsetAndRotation(5.0F, 21.0F, -4.0F, 0.0F, 0.0F, 0.1919862F));
        root.addOrReplaceChild("Shape16",
                CubeListBuilder.create().texOffs(9, 53)
                        .addBox(0.0F, 0.0F, 0.0F, 3.0F, 1.0F, 1.0F),
                PartPose.offsetAndRotation(5.0F, 19.5F, 3.0F, 0.0F, 0.0F, 0.6457718F));
        root.addOrReplaceChild("Shape17",
                CubeListBuilder.create().texOffs(0, 56)
                        .addBox(0.0F, 0.0F, 0.0F, 1.0F, 3.0F, 1.0F),
                PartPose.offset(6.0F, 21.0F, 3.0F));
        root.addOrReplaceChild("Shape18",
                CubeListBuilder.create().texOffs(0, 53)
                        .addBox(-3.0F, 0.0F, 0.0F, 3.0F, 1.0F, 1.0F),
                PartPose.offsetAndRotation(0.0F, 19.5F, 3.0F, 0.0F, 0.0F, -0.6457718F));
        root.addOrReplaceChild("Shape19",
                CubeListBuilder.create().texOffs(5, 56)
                        .addBox(-1.0F, 0.0F, 0.0F, 1.0F, 3.0F, 1.0F),
                PartPose.offset(-1.0F, 21.0F, 3.0F));
        root.addOrReplaceChild("Shape20",
                CubeListBuilder.create().texOffs(9, 61)
                        .addBox(0.0F, 0.0F, 0.0F, 3.0F, 1.0F, 1.0F),
                PartPose.offsetAndRotation(4.0F, 19.5F, 6.0F, 0.0F, -0.6457718F, 0.5061455F));
        root.addOrReplaceChild("Shape21",
                CubeListBuilder.create().texOffs(0, 61)
                        .addBox(0.0F, 0.0F, 0.0F, 3.0F, 1.0F, 1.0F),
                PartPose.offsetAndRotation(1.5F, 19.5F, 7.0F, 0.0F, -2.391101F, 0.5061455F));
        root.addOrReplaceChild("Shape22",
                CubeListBuilder.create().texOffs(0, 0)
                        .addBox(0.0F, 0.0F, 0.0F, 1.0F, 3.0F, 1.0F),
                PartPose.offset(-1.0F, 21.0F, 7.5F));
        root.addOrReplaceChild("Shape23",
                CubeListBuilder.create().texOffs(0, 13)
                        .addBox(0.0F, 0.0F, 0.0F, 1.0F, 3.0F, 1.0F),
                PartPose.offset(5.0F, 21.0F, 7.5F));
        root.addOrReplaceChild("lrwing",
                CubeListBuilder.create().texOffs(0, 38)
                        .addBox(0.0F, 0.0F, -3.0F, 10.0F, 1.0F, 3.0F),
                PartPose.offsetAndRotation(5.0F, 16.0F, 6.0F, 0.0F, -0.3839724F, 0.0F));
        root.addOrReplaceChild("rfwing",
                CubeListBuilder.create().texOffs(0, 48)
                        .addBox(-10.0F, 0.0F, 0.0F, 10.0F, 1.0F, 3.0F),
                PartPose.offsetAndRotation(0.0F, 16.0F, 1.0F, 0.0F, -0.4886922F, 0.0F));
        root.addOrReplaceChild("rrwing",
                CubeListBuilder.create().texOffs(0, 43)
                        .addBox(-10.0F, 0.0F, -3.0F, 10.0F, 1.0F, 3.0F),
                PartPose.offsetAndRotation(0.0F, 16.0F, 6.0F, 0.0F, 0.3839724F, 0.0F));
        return LayerDefinition.create(mesh, 64, 64);
    }

    @Override
    public ModelPart root() {
        return this.root;
    }

    @Override
    public void setupAnim(Dragonfly entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        animate(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
    }

    private void animate(Dragonfly entity, float f, float f1, float f2, float f3, float f4) {
        float newangle = 0.0F;
        newangle = Mth.cos(f2 * 1.3F * this.wingspeed) * (float) Math.PI * 0.25F;
        this.lfwing.zRot = newangle;
        this.rfwing.zRot = -newangle;
        this.lrwing.zRot = newangle + 3.14F;
        this.rrwing.zRot = -newangle + 3.14F;
        newangle = Mth.cos(f2 * 0.3F * this.wingspeed) * (float) Math.PI * 0.1F;
        this.ljaw.xRot = newangle;
        this.rjaw.xRot = -newangle;
    }
}
