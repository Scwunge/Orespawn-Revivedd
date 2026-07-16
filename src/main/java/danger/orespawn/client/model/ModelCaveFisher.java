package danger.orespawn.client.model;

import danger.orespawn.entity.CaveFisher;
import danger.orespawn.entity.RenderInfo;
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
 * Port of gold {@code ModelCaveFisher} (1.12 ModelBase) → 1.21 HierarchicalModel.
 */
@OnlyIn(Dist.CLIENT)
public class ModelCaveFisher extends HierarchicalModel<CaveFisher> {
    private final float wingspeed;
    private final ModelPart root;
    private final ModelPart Nose;
    private final ModelPart EyeLeft;
    private final ModelPart HeadMid;
    private final ModelPart HeadEnd;
    private final ModelPart TailTuft;
    private final ModelPart EyeRight;
    private final ModelPart BodyTopLeft4;
    private final ModelPart BodyTopRight4;
    private final ModelPart BodyTopLeft1;
    private final ModelPart BodyTopRight1;
    private final ModelPart BodyTopRight2;
    private final ModelPart BodyTopLeft2;
    private final ModelPart BodyTopRight3;
    private final ModelPart BodyTopLeft3;
    private final ModelPart HeadBase;
    private final ModelPart TailBase;
    private final ModelPart BodyLow2;
    private final ModelPart BodyLow1;
    private final ModelPart Spine5;
    private final ModelPart Spine1;
    private final ModelPart Spine2;
    private final ModelPart Spine3;
    private final ModelPart Spine4;
    private final ModelPart RightArmSeg4;
    private final ModelPart LeftArmSeg1;
    private final ModelPart LeftArmSeg3;
    private final ModelPart RightArmSeg2;
    private final ModelPart RightArmSeg1;
    private final ModelPart LeftArmSeg5;
    private final ModelPart LeftArmSeg2;
    private final ModelPart LeftClawTop;
    private final ModelPart RightArmSeg3;
    private final ModelPart RightArmSeg5;
    private final ModelPart LeftArmSeg4;
    private final ModelPart LeftClawBase;
    private final ModelPart RightClawBase;
    private final ModelPart LeftClawLow;
    private final ModelPart RightClawTop;
    private final ModelPart RightClawLow;
    private final ModelPart LBLeg1;
    private final ModelPart LBLeg3;
    private final ModelPart RBLeg1;
    private final ModelPart RBLeg3;
    private final ModelPart LBLeg2;
    private final ModelPart RBLeg2;
    private final ModelPart LBLeg4;
    private final ModelPart RBLeg4;
    private final ModelPart RBLeg5;
    private final ModelPart LBLeg6;
    private final ModelPart RBLeg6;
    private final ModelPart LBLeg5;
    private final ModelPart RFLeg1;
    private final ModelPart RFLeg2;
    private final ModelPart RFLeg3;
    private final ModelPart RFLeg4;
    private final ModelPart RFLeg5;
    private final ModelPart RFLeg6;
    private final ModelPart RMLeg1;
    private final ModelPart RMLeg2;
    private final ModelPart RMLeg3;
    private final ModelPart RMLeg4;
    private final ModelPart RMLeg5;
    private final ModelPart RMLeg6;
    private final ModelPart LFLeg1;
    private final ModelPart LFLeg2;
    private final ModelPart LFLeg3;
    private final ModelPart LFLeg4;
    private final ModelPart LFLeg5;
    private final ModelPart LFLeg6;
    private final ModelPart LMLeg1;
    private final ModelPart LMLeg2;
    private final ModelPart LMLeg4;
    private final ModelPart LMLeg3;
    private final ModelPart LMLeg5;
    private final ModelPart LMLeg6;

    public ModelCaveFisher(ModelPart root) {
        this(root, 1F);
    }

    public ModelCaveFisher(ModelPart root, float wingspeed) {
        this.root = root;
        this.wingspeed = wingspeed;
        this.Nose = root.getChild("Nose");
        this.EyeLeft = root.getChild("EyeLeft");
        this.HeadMid = root.getChild("HeadMid");
        this.HeadEnd = root.getChild("HeadEnd");
        this.TailTuft = root.getChild("TailTuft");
        this.EyeRight = root.getChild("EyeRight");
        this.BodyTopLeft4 = root.getChild("BodyTopLeft4");
        this.BodyTopRight4 = root.getChild("BodyTopRight4");
        this.BodyTopLeft1 = root.getChild("BodyTopLeft1");
        this.BodyTopRight1 = root.getChild("BodyTopRight1");
        this.BodyTopRight2 = root.getChild("BodyTopRight2");
        this.BodyTopLeft2 = root.getChild("BodyTopLeft2");
        this.BodyTopRight3 = root.getChild("BodyTopRight3");
        this.BodyTopLeft3 = root.getChild("BodyTopLeft3");
        this.HeadBase = root.getChild("HeadBase");
        this.TailBase = root.getChild("TailBase");
        this.BodyLow2 = root.getChild("BodyLow2");
        this.BodyLow1 = root.getChild("BodyLow1");
        this.Spine5 = root.getChild("Spine5");
        this.Spine1 = root.getChild("Spine1");
        this.Spine2 = root.getChild("Spine2");
        this.Spine3 = root.getChild("Spine3");
        this.Spine4 = root.getChild("Spine4");
        this.RightArmSeg4 = root.getChild("RightArmSeg4");
        this.LeftArmSeg1 = root.getChild("LeftArmSeg1");
        this.LeftArmSeg3 = root.getChild("LeftArmSeg3");
        this.RightArmSeg2 = root.getChild("RightArmSeg2");
        this.RightArmSeg1 = root.getChild("RightArmSeg1");
        this.LeftArmSeg5 = root.getChild("LeftArmSeg5");
        this.LeftArmSeg2 = root.getChild("LeftArmSeg2");
        this.LeftClawTop = root.getChild("LeftClawTop");
        this.RightArmSeg3 = root.getChild("RightArmSeg3");
        this.RightArmSeg5 = root.getChild("RightArmSeg5");
        this.LeftArmSeg4 = root.getChild("LeftArmSeg4");
        this.LeftClawBase = root.getChild("LeftClawBase");
        this.RightClawBase = root.getChild("RightClawBase");
        this.LeftClawLow = root.getChild("LeftClawLow");
        this.RightClawTop = root.getChild("RightClawTop");
        this.RightClawLow = root.getChild("RightClawLow");
        this.LBLeg1 = root.getChild("LBLeg1");
        this.LBLeg3 = root.getChild("LBLeg3");
        this.RBLeg1 = root.getChild("RBLeg1");
        this.RBLeg3 = root.getChild("RBLeg3");
        this.LBLeg2 = root.getChild("LBLeg2");
        this.RBLeg2 = root.getChild("RBLeg2");
        this.LBLeg4 = root.getChild("LBLeg4");
        this.RBLeg4 = root.getChild("RBLeg4");
        this.RBLeg5 = root.getChild("RBLeg5");
        this.LBLeg6 = root.getChild("LBLeg6");
        this.RBLeg6 = root.getChild("RBLeg6");
        this.LBLeg5 = root.getChild("LBLeg5");
        this.RFLeg1 = root.getChild("RFLeg1");
        this.RFLeg2 = root.getChild("RFLeg2");
        this.RFLeg3 = root.getChild("RFLeg3");
        this.RFLeg4 = root.getChild("RFLeg4");
        this.RFLeg5 = root.getChild("RFLeg5");
        this.RFLeg6 = root.getChild("RFLeg6");
        this.RMLeg1 = root.getChild("RMLeg1");
        this.RMLeg2 = root.getChild("RMLeg2");
        this.RMLeg3 = root.getChild("RMLeg3");
        this.RMLeg4 = root.getChild("RMLeg4");
        this.RMLeg5 = root.getChild("RMLeg5");
        this.RMLeg6 = root.getChild("RMLeg6");
        this.LFLeg1 = root.getChild("LFLeg1");
        this.LFLeg2 = root.getChild("LFLeg2");
        this.LFLeg3 = root.getChild("LFLeg3");
        this.LFLeg4 = root.getChild("LFLeg4");
        this.LFLeg5 = root.getChild("LFLeg5");
        this.LFLeg6 = root.getChild("LFLeg6");
        this.LMLeg1 = root.getChild("LMLeg1");
        this.LMLeg2 = root.getChild("LMLeg2");
        this.LMLeg4 = root.getChild("LMLeg4");
        this.LMLeg3 = root.getChild("LMLeg3");
        this.LMLeg5 = root.getChild("LMLeg5");
        this.LMLeg6 = root.getChild("LMLeg6");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        root.addOrReplaceChild("Nose",
                CubeListBuilder.create().texOffs(0, 0)
                        .addBox(-0.5F, -0.5F, -12.0F, 1.0F, 1.0F, 6.0F),
                PartPose.offset(0.0F, 19.0F, -4.0F));
        root.addOrReplaceChild("EyeLeft",
                CubeListBuilder.create().texOffs(0, 28)
                        .addBox(0.5F, -2.5F, -2.5F, 3.0F, 2.0F, 2.0F),
                PartPose.offset(0.0F, 19.0F, -4.0F));
        root.addOrReplaceChild("HeadMid",
                CubeListBuilder.create().texOffs(0, 0)
                        .addBox(-2.5F, -1.5F, -5.0F, 5.0F, 3.0F, 2.0F),
                PartPose.offset(0.0F, 19.0F, -4.0F));
        root.addOrReplaceChild("HeadEnd",
                CubeListBuilder.create().texOffs(0, 0)
                        .addBox(-2.0F, -1.0F, -6.0F, 4.0F, 2.0F, 1.0F),
                PartPose.offset(0.0F, 19.0F, -4.0F));
        root.addOrReplaceChild("TailTuft",
                CubeListBuilder.create().texOffs(0, 23)
                        .addBox(-2.0F, -1.0F, 3.0F, 4.0F, 1.0F, 2.0F),
                PartPose.offset(0.0F, 19.0F, 10.0F));
        root.addOrReplaceChild("EyeRight",
                CubeListBuilder.create().texOffs(0, 28)
                        .addBox(-3.5F, -2.5F, -2.5F, 3.0F, 2.0F, 2.0F),
                PartPose.offset(0.0F, 19.0F, -4.0F));
        root.addOrReplaceChild("BodyTopLeft4",
                CubeListBuilder.create().texOffs(0, 0)
                        .addBox(0.0F, 0.0F, 0.0F, 6.0F, 3.0F, 4.0F),
                PartPose.offsetAndRotation(0.0F, 16.2F, 7.0F, 0.1047198F, 0.1047198F, 0.1047198F));
        root.addOrReplaceChild("BodyTopRight4",
                CubeListBuilder.create().texOffs(0, 0)
                        .addBox(-5.0F, 0.0F, 0.0F, 6.0F, 3.0F, 4.0F),
                PartPose.offsetAndRotation(-1.0F, 16.2F, 7.0F, 0.1047198F, -0.1047198F, -0.1047198F));
        root.addOrReplaceChild("BodyTopLeft1",
                CubeListBuilder.create().texOffs(0, 0)
                        .addBox(0.0F, 0.0F, 0.0F, 5.0F, 3.0F, 4.0F),
                PartPose.offsetAndRotation(0.0F, 16.0F, -4.0F, 0.1745329F, 0.1745329F, 0.1047198F));
        root.addOrReplaceChild("BodyTopRight1",
                CubeListBuilder.create().texOffs(0, 0)
                        .addBox(-5.0F, 0.0F, 0.0F, 5.0F, 3.0F, 4.0F),
                PartPose.offsetAndRotation(0.0F, 16.0F, -4.0F, 0.1745329F, -0.1745329F, -0.1047198F));
        root.addOrReplaceChild("BodyTopRight2",
                CubeListBuilder.create().texOffs(0, 0)
                        .addBox(-5.0F, 0.0F, 0.0F, 7.0F, 3.0F, 4.0F),
                PartPose.offsetAndRotation(-1.0F, 16.0F, -1.0F, 0.2094395F, -0.1745329F, -0.1047198F));
        root.addOrReplaceChild("BodyTopLeft2",
                CubeListBuilder.create().texOffs(0, 0)
                        .addBox(-1.0F, 0.0F, 0.0F, 7.0F, 3.0F, 4.0F),
                PartPose.offsetAndRotation(0.0F, 16.0F, -1.0F, 0.2094395F, 0.1745329F, 0.1047198F));
        root.addOrReplaceChild("BodyTopRight3",
                CubeListBuilder.create().texOffs(0, 0)
                        .addBox(-5.0F, 0.0F, 0.0F, 6.0F, 3.0F, 4.0F),
                PartPose.offsetAndRotation(-1.0F, 16.0F, 3.0F, 0.1396263F, -0.1396263F, -0.1047198F));
        root.addOrReplaceChild("BodyTopLeft3",
                CubeListBuilder.create().texOffs(0, 0)
                        .addBox(0.0F, 0.0F, 0.0F, 6.0F, 3.0F, 4.0F),
                PartPose.offsetAndRotation(0.0F, 16.0F, 3.0F, 0.1396263F, 0.1396263F, 0.1047198F));
        root.addOrReplaceChild("HeadBase",
                CubeListBuilder.create().texOffs(0, 0)
                        .addBox(-3.0F, -2.0F, -3.0F, 6.0F, 4.0F, 3.0F),
                PartPose.offset(0.0F, 19.0F, -4.0F));
        root.addOrReplaceChild("TailBase",
                CubeListBuilder.create().texOffs(0, 0)
                        .addBox(-3.0F, -2.0F, 0.0F, 6.0F, 3.0F, 3.0F),
                PartPose.offset(0.0F, 19.0F, 10.0F));
        root.addOrReplaceChild("BodyLow2",
                CubeListBuilder.create().texOffs(34, 0)
                        .addBox(0.0F, 0.0F, 0.0F, 8.0F, 2.0F, 7.0F),
                PartPose.offset(-4.0F, 18.3F, 3.0F));
        root.addOrReplaceChild("BodyLow1",
                CubeListBuilder.create().texOffs(34, 0)
                        .addBox(0.0F, 0.0F, 0.0F, 8.0F, 2.0F, 7.0F),
                PartPose.offset(-4.0F, 18.7F, -4.0F));
        root.addOrReplaceChild("Spine5",
                CubeListBuilder.create().texOffs(0, 0)
                        .addBox(-0.5F, 0.0F, 0.0F, 1.0F, 1.0F, 4.0F),
                PartPose.offset(0.0F, 16.0F, 8.6F));
        root.addOrReplaceChild("Spine1",
                CubeListBuilder.create().texOffs(0, 0)
                        .addBox(-0.5F, 0.0F, 0.0F, 1.0F, 1.0F, 4.0F),
                PartPose.offsetAndRotation(0.0F, 16.0F, -4.2F, 0.2443461F, 0.0F, 0.0F));
        root.addOrReplaceChild("Spine2",
                CubeListBuilder.create().texOffs(0, 0)
                        .addBox(-0.5F, 0.0F, 0.0F, 1.0F, 1.0F, 5.0F),
                PartPose.offsetAndRotation(0.0F, 16.0F, -1.2F, 0.3141593F, 0.0F, 0.0F));
        root.addOrReplaceChild("Spine3",
                CubeListBuilder.create().texOffs(0, 0)
                        .addBox(-0.5F, 0.0F, 0.0F, 1.0F, 1.0F, 6.0F),
                PartPose.offsetAndRotation(0.0F, 16.0F, 1.8F, 0.2792527F, 0.0F, 0.0F));
        root.addOrReplaceChild("Spine4",
                CubeListBuilder.create().texOffs(0, 0)
                        .addBox(-0.5F, 0.0F, 0.0F, 1.0F, 1.0F, 8.0F),
                PartPose.offsetAndRotation(0.0F, 16.0F, 3.8F, 0.1745329F, 0.0F, 0.0F));
        root.addOrReplaceChild("RightArmSeg4",
                CubeListBuilder.create().texOffs(0, 0)
                        .addBox(-3.2F, -1.0F, -10.5F, 2.0F, 2.0F, 4.0F),
                PartPose.offsetAndRotation(-4.7F, 17.5F, -3.0F, 0.0F, 0.0872665F, 0.0F));
        root.addOrReplaceChild("LeftArmSeg1",
                CubeListBuilder.create().texOffs(0, 13)
                        .addBox(-0.5F, -0.5F, -4.0F, 1.0F, 1.0F, 4.0F),
                PartPose.offsetAndRotation(4.7F, 17.5F, -3.0F, 0.0F, (float) (-Math.PI / 6), 0.0F));
        root.addOrReplaceChild("LeftArmSeg3",
                CubeListBuilder.create().texOffs(0, 13)
                        .addBox(1.0F, -0.5F, -8.0F, 1.0F, 1.0F, 3.0F),
                PartPose.offsetAndRotation(4.7F, 17.5F, -3.0F, 0.0F, -0.1745329F, 0.0F));
        root.addOrReplaceChild("RightArmSeg2",
                CubeListBuilder.create().texOffs(0, 0)
                        .addBox(-1.5F, -1.0F, -6.0F, 2.0F, 2.0F, 4.0F),
                PartPose.offsetAndRotation(-4.7F, 17.5F, -3.0F, 0.0F, 0.3490659F, 0.0F));
        root.addOrReplaceChild("RightArmSeg1",
                CubeListBuilder.create().texOffs(0, 13)
                        .addBox(-0.5F, -0.5F, -4.0F, 1.0F, 1.0F, 4.0F),
                PartPose.offsetAndRotation(-4.7F, 17.5F, -3.0F, 0.0F, (float) (Math.PI / 6), 0.0F));
        root.addOrReplaceChild("LeftArmSeg5",
                CubeListBuilder.create().texOffs(0, 13)
                        .addBox(2.4F, -0.5F, -12.0F, 1.0F, 1.0F, 3.0F),
                PartPose.offset(4.7F, 17.5F, -3.0F));
        root.addOrReplaceChild("LeftArmSeg2",
                CubeListBuilder.create().texOffs(0, 0)
                        .addBox(-0.5F, -1.0F, -6.0F, 2.0F, 2.0F, 4.0F),
                PartPose.offsetAndRotation(4.7F, 17.5F, -3.0F, 0.0F, -0.3490659F, 0.0F));
        root.addOrReplaceChild("LeftClawTop",
                CubeListBuilder.create().texOffs(15, 15)
                        .addBox(1.8F, 4.7F, -15.0F, 2.0F, 2.0F, 5.0F),
                PartPose.offsetAndRotation(4.7F, 17.5F, -3.0F, -0.5410521F, 0.0F, 0.0F));
        root.addOrReplaceChild("RightArmSeg3",
                CubeListBuilder.create().texOffs(0, 13)
                        .addBox(-2.0F, -0.5F, -8.0F, 1.0F, 1.0F, 3.0F),
                PartPose.offsetAndRotation(-4.7F, 17.5F, -3.0F, 0.0F, 0.1745329F, 0.0F));
        root.addOrReplaceChild("RightArmSeg5",
                CubeListBuilder.create().texOffs(0, 13)
                        .addBox(-3.6F, -0.5F, -12.0F, 1.0F, 1.0F, 3.0F),
                PartPose.offset(-4.7F, 17.5F, -3.0F));
        root.addOrReplaceChild("LeftArmSeg4",
                CubeListBuilder.create().texOffs(0, 0)
                        .addBox(1.1F, -1.0F, -10.5F, 2.0F, 2.0F, 4.0F),
                PartPose.offsetAndRotation(4.7F, 17.5F, -3.0F, 0.0F, -0.0872665F, 0.0F));
        root.addOrReplaceChild("LeftClawBase",
                CubeListBuilder.create().texOffs(0, 0)
                        .addBox(1.8F, -1.0F, -13.0F, 2.0F, 2.0F, 2.0F),
                PartPose.offset(4.7F, 17.5F, -3.0F));
        root.addOrReplaceChild("RightClawBase",
                CubeListBuilder.create().texOffs(0, 0)
                        .addBox(-4.2F, -1.0F, -13.0F, 2.0F, 2.0F, 2.0F),
                PartPose.offset(-4.7F, 17.5F, -3.0F));
        root.addOrReplaceChild("LeftClawLow",
                CubeListBuilder.create().texOffs(25, 25)
                        .addBox(1.8F, -4.3F, -15.0F, 2.0F, 1.0F, 4.0F),
                PartPose.offsetAndRotation(4.7F, 17.5F, -3.0F, 0.3490659F, 0.0F, 0.0F));
        root.addOrReplaceChild("RightClawTop",
                CubeListBuilder.create().texOffs(15, 15)
                        .addBox(-4.2F, 4.7F, -15.0F, 2.0F, 2.0F, 5.0F),
                PartPose.offsetAndRotation(-4.7F, 17.5F, -3.0F, -0.5410521F, 0.0F, 0.0F));
        root.addOrReplaceChild("RightClawLow",
                CubeListBuilder.create().texOffs(25, 25)
                        .addBox(-4.2F, -4.3F, -15.0F, 2.0F, 1.0F, 4.0F),
                PartPose.offsetAndRotation(-4.7F, 17.5F, -3.0F, 0.3490659F, 0.0F, 0.0F));
        root.addOrReplaceChild("LBLeg1",
                CubeListBuilder.create().texOffs(0, 13)
                        .addBox(0.5F, -0.5F, -0.5F, 3.0F, 1.0F, 1.0F),
                PartPose.offsetAndRotation(5.0F, 18.0F, 8.5F, 0.0F, 0.0F, -0.4363323F));
        root.addOrReplaceChild("LBLeg3",
                CubeListBuilder.create().texOffs(2, 0)
                        .addBox(5.1F, -1.5F, -1.0F, 3.0F, 1.0F, 2.0F),
                PartPose.offsetAndRotation(5.0F, 18.0F, 8.5F, 0.0F, 0.0F, -0.5759587F));
        root.addOrReplaceChild("RBLeg1",
                CubeListBuilder.create().texOffs(0, 13)
                        .addBox(-3.5F, -0.5F, -0.5F, 3.0F, 1.0F, 1.0F),
                PartPose.offsetAndRotation(-5.0F, 18.0F, 8.5F, 0.0F, 0.0F, 0.4363323F));
        root.addOrReplaceChild("RBLeg3",
                CubeListBuilder.create().texOffs(2, 0)
                        .addBox(-8.1F, -1.5F, -1.0F, 3.0F, 1.0F, 2.0F),
                PartPose.offsetAndRotation(-5.0F, 18.0F, 8.5F, 0.0F, 0.0F, 0.5759587F));
        root.addOrReplaceChild("LBLeg2",
                CubeListBuilder.create().texOffs(0, 0)
                        .addBox(2.5F, 0.5F, -1.0F, 3.0F, 2.0F, 2.0F),
                PartPose.offsetAndRotation(5.0F, 18.0F, 8.5F, 0.0F, 0.0F, -0.9599311F));
        root.addOrReplaceChild("RBLeg2",
                CubeListBuilder.create().texOffs(0, 0)
                        .addBox(-5.5F, 0.5F, -1.0F, 3.0F, 2.0F, 2.0F),
                PartPose.offsetAndRotation(-5.0F, 18.0F, 8.5F, 0.0F, 0.0F, 0.9599311F));
        root.addOrReplaceChild("LBLeg4",
                CubeListBuilder.create().texOffs(0, 13)
                        .addBox(5.0F, -3.0F, -0.5F, 1.0F, 3.0F, 1.0F),
                PartPose.offsetAndRotation(5.0F, 18.0F, 8.5F, 0.0F, 0.0F, -0.2094395F));
        root.addOrReplaceChild("RBLeg4",
                CubeListBuilder.create().texOffs(0, 13)
                        .addBox(-6.0F, -3.0F, -0.5F, 1.0F, 3.0F, 1.0F),
                PartPose.offsetAndRotation(-5.0F, 18.0F, 8.5F, 0.0F, 0.0F, 0.2094395F));
        root.addOrReplaceChild("RBLeg5",
                CubeListBuilder.create().texOffs(0, 0)
                        .addBox(-6.4F, -1.0F, -1.0F, 2.0F, 6.0F, 2.0F),
                PartPose.offsetAndRotation(-5.0F, 18.0F, 8.5F, 0.0F, 0.0F, 0.1047198F));
        root.addOrReplaceChild("LBLeg6",
                CubeListBuilder.create().texOffs(0, 13)
                        .addBox(5.5F, 3.0F, -0.5F, 1.0F, 3.0F, 1.0F),
                PartPose.offset(5.0F, 18.0F, 8.5F));
        root.addOrReplaceChild("RBLeg6",
                CubeListBuilder.create().texOffs(0, 13)
                        .addBox(-6.5F, 3.0F, -0.5F, 1.0F, 3.0F, 1.0F),
                PartPose.offset(-5.0F, 18.0F, 8.5F));
        root.addOrReplaceChild("LBLeg5",
                CubeListBuilder.create().texOffs(0, 0)
                        .addBox(4.6F, -1.0F, -1.0F, 2.0F, 6.0F, 2.0F),
                PartPose.offsetAndRotation(5.0F, 18.0F, 8.5F, 0.0F, 0.0F, -0.1047198F));
        root.addOrReplaceChild("RFLeg1",
                CubeListBuilder.create().texOffs(0, 13)
                        .addBox(-3.5F, -0.5F, -0.5F, 3.0F, 1.0F, 1.0F),
                PartPose.offsetAndRotation(-5.0F, 18.0F, 0.5F, 0.0F, 0.0F, 0.4363323F));
        root.addOrReplaceChild("RFLeg2",
                CubeListBuilder.create().texOffs(0, 0)
                        .addBox(-5.5F, 0.5F, -1.0F, 3.0F, 2.0F, 2.0F),
                PartPose.offsetAndRotation(-5.0F, 18.0F, 0.5F, 0.0F, 0.0F, 0.9599311F));
        root.addOrReplaceChild("RFLeg3",
                CubeListBuilder.create().texOffs(2, 0)
                        .addBox(-8.1F, -1.5F, -1.0F, 3.0F, 1.0F, 2.0F),
                PartPose.offsetAndRotation(-5.0F, 18.0F, 0.5F, 0.0F, 0.0F, 0.5759587F));
        root.addOrReplaceChild("RFLeg4",
                CubeListBuilder.create().texOffs(0, 13)
                        .addBox(-6.0F, -3.0F, -0.5F, 1.0F, 3.0F, 1.0F),
                PartPose.offsetAndRotation(-5.0F, 18.0F, 0.5F, 0.0F, 0.0F, 0.2094395F));
        root.addOrReplaceChild("RFLeg5",
                CubeListBuilder.create().texOffs(0, 0)
                        .addBox(-6.4F, -1.0F, -1.0F, 2.0F, 6.0F, 2.0F),
                PartPose.offsetAndRotation(-5.0F, 18.0F, 0.5F, 0.0F, 0.0F, 0.1047198F));
        root.addOrReplaceChild("RFLeg6",
                CubeListBuilder.create().texOffs(0, 13)
                        .addBox(-6.5F, 3.0F, -0.5F, 1.0F, 3.0F, 1.0F),
                PartPose.offset(-5.0F, 18.0F, 0.5F));
        root.addOrReplaceChild("RMLeg1",
                CubeListBuilder.create().texOffs(0, 13)
                        .addBox(-3.5F, -0.5F, -0.5F, 3.0F, 1.0F, 1.0F),
                PartPose.offsetAndRotation(-5.0F, 18.0F, 4.5F, 0.0F, 0.0F, 0.4363323F));
        root.addOrReplaceChild("RMLeg2",
                CubeListBuilder.create().texOffs(0, 0)
                        .addBox(-5.5F, 0.5F, -1.0F, 3.0F, 2.0F, 2.0F),
                PartPose.offsetAndRotation(-5.0F, 18.0F, 4.5F, 0.0F, 0.0F, 0.9599311F));
        root.addOrReplaceChild("RMLeg3",
                CubeListBuilder.create().texOffs(2, 0)
                        .addBox(-8.1F, -1.5F, -1.0F, 3.0F, 1.0F, 2.0F),
                PartPose.offsetAndRotation(-5.0F, 18.0F, 4.5F, 0.0F, 0.0F, 0.5759587F));
        root.addOrReplaceChild("RMLeg4",
                CubeListBuilder.create().texOffs(0, 13)
                        .addBox(-6.0F, -3.0F, -0.5F, 1.0F, 3.0F, 1.0F),
                PartPose.offsetAndRotation(-5.0F, 18.0F, 4.5F, 0.0F, 0.0F, 0.2094395F));
        root.addOrReplaceChild("RMLeg5",
                CubeListBuilder.create().texOffs(0, 0)
                        .addBox(-6.4F, -1.0F, -1.0F, 2.0F, 6.0F, 2.0F),
                PartPose.offsetAndRotation(-5.0F, 18.0F, 4.5F, 0.0F, 0.0F, 0.1047198F));
        root.addOrReplaceChild("RMLeg6",
                CubeListBuilder.create().texOffs(0, 13)
                        .addBox(-6.5F, 3.0F, -0.5F, 1.0F, 3.0F, 1.0F),
                PartPose.offset(-5.0F, 18.0F, 4.5F));
        root.addOrReplaceChild("LFLeg1",
                CubeListBuilder.create().texOffs(0, 13)
                        .addBox(0.5F, -0.5F, -0.5F, 3.0F, 1.0F, 1.0F),
                PartPose.offsetAndRotation(5.0F, 18.0F, 0.5F, 0.0F, 0.0F, -0.4363323F));
        root.addOrReplaceChild("LFLeg2",
                CubeListBuilder.create().texOffs(0, 0)
                        .addBox(2.5F, 0.5F, -1.0F, 3.0F, 2.0F, 2.0F),
                PartPose.offsetAndRotation(5.0F, 18.0F, 0.5F, 0.0F, 0.0F, -0.9599311F));
        root.addOrReplaceChild("LFLeg3",
                CubeListBuilder.create().texOffs(2, 0)
                        .addBox(5.1F, -1.5F, -1.0F, 3.0F, 1.0F, 2.0F),
                PartPose.offsetAndRotation(5.0F, 18.0F, 0.5F, 0.0F, 0.0F, -0.5759587F));
        root.addOrReplaceChild("LFLeg4",
                CubeListBuilder.create().texOffs(0, 13)
                        .addBox(5.0F, -3.0F, -0.5F, 1.0F, 3.0F, 1.0F),
                PartPose.offsetAndRotation(5.0F, 18.0F, 0.5F, 0.0F, 0.0F, -0.2094395F));
        root.addOrReplaceChild("LFLeg5",
                CubeListBuilder.create().texOffs(0, 0)
                        .addBox(4.6F, -1.0F, -1.0F, 2.0F, 6.0F, 2.0F),
                PartPose.offsetAndRotation(5.0F, 18.0F, 0.5F, 0.0F, 0.0F, -0.1047198F));
        root.addOrReplaceChild("LFLeg6",
                CubeListBuilder.create().texOffs(0, 13)
                        .addBox(5.5F, 3.0F, -0.5F, 1.0F, 3.0F, 1.0F),
                PartPose.offset(5.0F, 18.0F, 0.5F));
        root.addOrReplaceChild("LMLeg1",
                CubeListBuilder.create().texOffs(0, 13)
                        .addBox(0.5F, -0.5F, -0.5F, 3.0F, 1.0F, 1.0F),
                PartPose.offsetAndRotation(5.0F, 18.0F, 4.5F, 0.0F, 0.0F, -0.4363323F));
        root.addOrReplaceChild("LMLeg2",
                CubeListBuilder.create().texOffs(0, 0)
                        .addBox(2.5F, 0.5F, -1.0F, 3.0F, 2.0F, 2.0F),
                PartPose.offsetAndRotation(5.0F, 18.0F, 4.5F, 0.0F, 0.0F, -0.9599311F));
        root.addOrReplaceChild("LMLeg4",
                CubeListBuilder.create().texOffs(0, 13)
                        .addBox(5.0F, -3.0F, -0.5F, 1.0F, 3.0F, 1.0F),
                PartPose.offsetAndRotation(5.0F, 18.0F, 4.5F, 0.0F, 0.0F, -0.2094395F));
        root.addOrReplaceChild("LMLeg3",
                CubeListBuilder.create().texOffs(2, 0)
                        .addBox(5.1F, -1.5F, -1.0F, 3.0F, 1.0F, 2.0F),
                PartPose.offsetAndRotation(5.0F, 18.0F, 4.5F, 0.0F, 0.0F, -0.5759587F));
        root.addOrReplaceChild("LMLeg5",
                CubeListBuilder.create().texOffs(0, 0)
                        .addBox(4.6F, -1.0F, -1.0F, 2.0F, 6.0F, 2.0F),
                PartPose.offsetAndRotation(5.0F, 18.0F, 4.5F, 0.0F, 0.0F, -0.1047198F));
        root.addOrReplaceChild("LMLeg6",
                CubeListBuilder.create().texOffs(0, 13)
                        .addBox(5.5F, 3.0F, -0.5F, 1.0F, 3.0F, 1.0F),
                PartPose.offset(5.0F, 18.0F, 4.5F));
        return LayerDefinition.create(mesh, 64, 32);
    }

    @Override
    public ModelPart root() {
        return this.root;
    }

    @Override
    public void setupAnim(CaveFisher entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        animate(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
    }

    private void animate(CaveFisher entity, float f, float f1, float f2, float f3, float f4) {

        float newangle = 0.0F;
        float upangle = 0.0F;
        float nextangle = 0.0F;
        float pi4 = 1.570795F;
        newangle = Mth.cos(f2 * 2.0F * this.wingspeed) * (float) Math.PI * 0.12F * f1;
        this.LFLeg1.yRot = newangle;
        this.LFLeg2.yRot = newangle;
        this.LFLeg3.yRot = newangle;
        this.LFLeg4.yRot = newangle;
        this.LFLeg5.yRot = newangle;
        this.LFLeg6.yRot = newangle;
        this.RFLeg1.yRot = -newangle;
        this.RFLeg2.yRot = -newangle;
        this.RFLeg3.yRot = -newangle;
        this.RFLeg4.yRot = -newangle;
        this.RFLeg5.yRot = -newangle;
        this.RFLeg6.yRot = -newangle;
        newangle = Mth.cos(f2 * 2.0F * this.wingspeed - 1.0F * pi4) * (float) Math.PI * 0.12F * f1;
        this.LMLeg1.yRot = newangle;
        this.LMLeg2.yRot = newangle;
        this.LMLeg3.yRot = newangle;
        this.LMLeg4.yRot = newangle;
        this.LMLeg5.yRot = newangle;
        this.LMLeg6.yRot = newangle;
        this.RMLeg1.yRot = -newangle;
        this.RMLeg2.yRot = -newangle;
        this.RMLeg3.yRot = -newangle;
        this.RMLeg4.yRot = -newangle;
        this.RMLeg5.yRot = -newangle;
        this.RMLeg6.yRot = -newangle;
        newangle = Mth.cos(f2 * 2.0F * this.wingspeed - 2.0F * pi4) * (float) Math.PI * 0.12F * f1;
        this.LBLeg1.yRot = newangle;
        this.LBLeg2.yRot = newangle;
        this.LBLeg3.yRot = newangle;
        this.LBLeg4.yRot = newangle;
        this.LBLeg5.yRot = newangle;
        this.LBLeg6.yRot = newangle;
        this.RBLeg1.yRot = -newangle;
        this.RBLeg2.yRot = -newangle;
        this.RBLeg3.yRot = -newangle;
        this.RBLeg4.yRot = -newangle;
        this.RBLeg5.yRot = -newangle;
        this.RBLeg6.yRot = -newangle;
        RenderInfo r = entity.getRenderInfo();
        newangle = Mth.cos(f2 * 3.0F * this.wingspeed) * (float) Math.PI * 0.15F;
        nextangle = Mth.cos((f2 + 0.1F) * 3.0F * this.wingspeed) * (float) Math.PI * 0.15F;
        if (entity.getAttacking() == 0) {
        newangle = 0.0F;
        }
        if (nextangle > 0.0F && newangle < 0.0F) {
        r.ri1 = 0;
        if (entity.getAttacking() == 0) {
        r.ri1 = entity.getRandom().nextInt(20);
        r.ri2 = entity.getRandom().nextInt(25);
        } else {
        r.ri1 = entity.getRandom().nextInt(4);
        r.ri2 = entity.getRandom().nextInt(3);
        }
        }
        if (r.ri1 != 1 && r.ri1 != 3) {
        this.doLeftClaw(0.0F);
        this.doRightClaw(0.0F);
        } else {
        this.doLeftClaw(newangle);
        this.doRightClaw(newangle);
        }
        entity.setRenderInfo(r);
    }


    private void doLeftClaw(float angle) {
        
        this.LeftArmSeg1.xRot = Math.abs(angle);
        this.LeftArmSeg2.xRot = Math.abs(angle);
        this.LeftArmSeg3.xRot = Math.abs(angle);
        this.LeftArmSeg4.xRot = Math.abs(angle);
        this.LeftArmSeg5.xRot = Math.abs(angle);
        this.LeftClawBase.xRot = Math.abs(angle);
        this.LeftClawTop.xRot = Math.abs(angle) - 0.54F;
        this.LeftClawLow.xRot = Math.abs(angle) + 0.35F;
        
    }

    private void doRightClaw(float angle) {
        
        this.RightArmSeg1.xRot = Math.abs(angle);
        this.RightArmSeg2.xRot = Math.abs(angle);
        this.RightArmSeg3.xRot = Math.abs(angle);
        this.RightArmSeg4.xRot = Math.abs(angle);
        this.RightArmSeg5.xRot = Math.abs(angle);
        this.RightClawBase.xRot = Math.abs(angle);
        this.RightClawTop.xRot = Math.abs(angle) - 0.54F;
        this.RightClawLow.xRot = Math.abs(angle) + 0.35F;
        
    }
}
