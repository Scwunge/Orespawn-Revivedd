package danger.orespawn.client.model;

import danger.orespawn.entity.EmperorScorpion;
import danger.orespawn.entity.RenderInfo;
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
 * Port of gold {@code ModelEmperorScorpion} (1.7.10 ModelBase, tex 256×128) → 1.21 HierarchicalModel.
 * Cube sizes/UVs/offsets/base rotations 1:1. Wingspeed default 0.22 matches ClientProxy.
 * Claw/tail/leg state via entity {@link RenderInfo} and doLeft/RightLeg/Claw/Tail helpers.
 */
@OnlyIn(Dist.CLIENT)
public class ModelEmperorScorpion extends HierarchicalModel<EmperorScorpion> {
    /** Model layer location. */
    public static final ModelLayerLocation LAYER_LOCATION =
            new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "emperor_scorpion"), "main");

    private final float wingspeed;
    private final ModelPart root;
    private final ModelPart head;
    private final ModelPart seg1;
    private final ModelPart seg2;
    private final ModelPart seg3;
    private final ModelPart seg4;
    private final ModelPart seg5;
    private final ModelPart seg6;
    private final ModelPart seg7;
    private final ModelPart seg8;
    private final ModelPart tailseg1;
    private final ModelPart tailseg2;
    private final ModelPart tailseg3;
    private final ModelPart tailseg4;
    private final ModelPart tailseg5;
    private final ModelPart tailseg6;
    private final ModelPart tailseg7;
    private final ModelPart tailseg8;
    private final ModelPart stinger1;
    private final ModelPart stinger2;
    private final ModelPart stinger3;
    private final ModelPart leftShoulder;
    private final ModelPart leftArmSeg1;
    private final ModelPart leftArmSeg2;
    private final ModelPart leftArmSeg3;
    private final ModelPart leftArmSeg4;
    private final ModelPart rightShoulder;
    private final ModelPart rightArmSeg1;
    private final ModelPart rightArmSeg2;
    private final ModelPart rightArmSeg3;
    private final ModelPart rightArmSeg4;
    private final ModelPart rightPincer;
    private final ModelPart leftPincer;
    private final ModelPart lefteye;
    private final ModelPart righteye;
    private final ModelPart rightMandible;
    private final ModelPart leftMandible;
    private final ModelPart rightManPart2;
    private final ModelPart leftManPart2;
    private final ModelPart leg1Seg1;
    private final ModelPart leg1Seg2;
    private final ModelPart leg1Seg3;
    private final ModelPart leg1Seg4;
    private final ModelPart leg1Seg5;
    private final ModelPart leg2Seg1;
    private final ModelPart leg2Seg2;
    private final ModelPart leg2Seg3;
    private final ModelPart leg2Seg4;
    private final ModelPart leg2Seg5;
    private final ModelPart leg3Seg1;
    private final ModelPart leg3Seg2;
    private final ModelPart leg3Seg3;
    private final ModelPart leg3Seg4;
    private final ModelPart leg3Seg5;
    private final ModelPart leg4Seg1;
    private final ModelPart leg4Seg2;
    private final ModelPart leg4Seg3;
    private final ModelPart leg4Seg4;
    private final ModelPart leg4Seg5;
    private final ModelPart leg5Seg1;
    private final ModelPart leg5Seg2;
    private final ModelPart leg5Seg3;
    private final ModelPart leg5Seg4;
    private final ModelPart leg5Seg5;
    private final ModelPart leg6Seg1;
    private final ModelPart leg6Seg2;
    private final ModelPart leg6Seg3;
    private final ModelPart leg6Seg4;
    private final ModelPart leg6Seg5;
    private final ModelPart leg7Seg1;
    private final ModelPart leg7Seg2;
    private final ModelPart leg7Seg3;
    private final ModelPart leg7Seg4;
    private final ModelPart leg7Seg5;
    private final ModelPart leg8Seg1;
    private final ModelPart leg8Seg2;
    private final ModelPart leg8Seg3;
    private final ModelPart leg8Seg4;
    private final ModelPart leg8Seg5;

    public ModelEmperorScorpion(ModelPart root) {
        this(root, 0.22F);
    }

    public ModelEmperorScorpion(ModelPart root, float wingspeed) {
        this.root = root;
        this.wingspeed = wingspeed;
        this.head = root.getChild("head");
        this.seg1 = root.getChild("seg1");
        this.seg2 = root.getChild("seg2");
        this.seg3 = root.getChild("seg3");
        this.seg4 = root.getChild("seg4");
        this.seg5 = root.getChild("seg5");
        this.seg6 = root.getChild("seg6");
        this.seg7 = root.getChild("seg7");
        this.seg8 = root.getChild("seg8");
        this.tailseg1 = root.getChild("tailseg1");
        this.tailseg2 = root.getChild("tailseg2");
        this.tailseg3 = root.getChild("tailseg3");
        this.tailseg4 = root.getChild("tailseg4");
        this.tailseg5 = root.getChild("tailseg5");
        this.tailseg6 = root.getChild("tailseg6");
        this.tailseg7 = root.getChild("tailseg7");
        this.tailseg8 = root.getChild("tailseg8");
        this.stinger1 = root.getChild("stinger1");
        this.stinger2 = root.getChild("stinger2");
        this.stinger3 = root.getChild("stinger3");
        this.leftShoulder = root.getChild("leftShoulder");
        this.leftArmSeg1 = root.getChild("leftArmSeg1");
        this.leftArmSeg2 = root.getChild("leftArmSeg2");
        this.leftArmSeg3 = root.getChild("leftArmSeg3");
        this.leftArmSeg4 = root.getChild("leftArmSeg4");
        this.rightShoulder = root.getChild("rightShoulder");
        this.rightArmSeg1 = root.getChild("rightArmSeg1");
        this.rightArmSeg2 = root.getChild("rightArmSeg2");
        this.rightArmSeg3 = root.getChild("rightArmSeg3");
        this.rightArmSeg4 = root.getChild("rightArmSeg4");
        this.rightPincer = root.getChild("rightPincer");
        this.leftPincer = root.getChild("leftPincer");
        this.lefteye = root.getChild("lefteye");
        this.righteye = root.getChild("righteye");
        this.rightMandible = root.getChild("rightMandible");
        this.leftMandible = root.getChild("leftMandible");
        this.rightManPart2 = root.getChild("rightManPart2");
        this.leftManPart2 = root.getChild("leftManPart2");
        this.leg1Seg1 = root.getChild("leg1Seg1");
        this.leg1Seg2 = root.getChild("leg1Seg2");
        this.leg1Seg3 = root.getChild("leg1Seg3");
        this.leg1Seg4 = root.getChild("leg1Seg4");
        this.leg1Seg5 = root.getChild("leg1Seg5");
        this.leg2Seg1 = root.getChild("leg2Seg1");
        this.leg2Seg2 = root.getChild("leg2Seg2");
        this.leg2Seg3 = root.getChild("leg2Seg3");
        this.leg2Seg4 = root.getChild("leg2Seg4");
        this.leg2Seg5 = root.getChild("leg2Seg5");
        this.leg3Seg1 = root.getChild("leg3Seg1");
        this.leg3Seg2 = root.getChild("leg3Seg2");
        this.leg3Seg3 = root.getChild("leg3Seg3");
        this.leg3Seg4 = root.getChild("leg3Seg4");
        this.leg3Seg5 = root.getChild("leg3Seg5");
        this.leg4Seg1 = root.getChild("leg4Seg1");
        this.leg4Seg2 = root.getChild("leg4Seg2");
        this.leg4Seg3 = root.getChild("leg4Seg3");
        this.leg4Seg4 = root.getChild("leg4Seg4");
        this.leg4Seg5 = root.getChild("leg4Seg5");
        this.leg5Seg1 = root.getChild("leg5Seg1");
        this.leg5Seg2 = root.getChild("leg5Seg2");
        this.leg5Seg3 = root.getChild("leg5Seg3");
        this.leg5Seg4 = root.getChild("leg5Seg4");
        this.leg5Seg5 = root.getChild("leg5Seg5");
        this.leg6Seg1 = root.getChild("leg6Seg1");
        this.leg6Seg2 = root.getChild("leg6Seg2");
        this.leg6Seg3 = root.getChild("leg6Seg3");
        this.leg6Seg4 = root.getChild("leg6Seg4");
        this.leg6Seg5 = root.getChild("leg6Seg5");
        this.leg7Seg1 = root.getChild("leg7Seg1");
        this.leg7Seg2 = root.getChild("leg7Seg2");
        this.leg7Seg3 = root.getChild("leg7Seg3");
        this.leg7Seg4 = root.getChild("leg7Seg4");
        this.leg7Seg5 = root.getChild("leg7Seg5");
        this.leg8Seg1 = root.getChild("leg8Seg1");
        this.leg8Seg2 = root.getChild("leg8Seg2");
        this.leg8Seg3 = root.getChild("leg8Seg3");
        this.leg8Seg4 = root.getChild("leg8Seg4");
        this.leg8Seg5 = root.getChild("leg8Seg5");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        root.addOrReplaceChild("head",
                CubeListBuilder.create().texOffs(0, 104)
                        .addBox(-9.0F, -4.0F, -16.0F, 18.0F, 8.0F, 16.0F),
                PartPose.offset(0.0F, 13.0F, -8.0F));
        root.addOrReplaceChild("seg1",
                CubeListBuilder.create().texOffs(0, 78)
                        .addBox(-9.0F, -4.0F, 0.0F, 18.0F, 8.0F, 4.0F),
                PartPose.offset(0.0F, 13.0F, -8.0F));
        root.addOrReplaceChild("seg2",
                CubeListBuilder.create().texOffs(0, 65)
                        .addBox(-8.5F, -4.1F, 4.0F, 17.0F, 8.0F, 4.0F),
                PartPose.offset(0.0F, 13.0F, -8.0F));
        root.addOrReplaceChild("seg3",
                CubeListBuilder.create().texOffs(0, 50)
                        .addBox(-9.5F, -4.0F, 8.0F, 19.0F, 8.0F, 5.0F),
                PartPose.offset(0.0F, 13.0F, -8.0F));
        root.addOrReplaceChild("seg4",
                CubeListBuilder.create().texOffs(0, 35)
                        .addBox(-9.0F, -4.1F, 13.0F, 18.0F, 8.0F, 6.0F),
                PartPose.offset(0.0F, 13.0F, -8.0F));
        root.addOrReplaceChild("seg5",
                CubeListBuilder.create().texOffs(45, 91)
                        .addBox(-8.5F, -4.0F, 19.0F, 17.0F, 8.0F, 3.0F),
                PartPose.offset(0.0F, 13.0F, -8.0F));
        root.addOrReplaceChild("seg6",
                CubeListBuilder.create().texOffs(45, 79)
                        .addBox(-8.0F, -4.1F, 22.0F, 16.0F, 8.0F, 3.0F),
                PartPose.offset(0.0F, 13.0F, -8.0F));
        root.addOrReplaceChild("seg7",
                CubeListBuilder.create().texOffs(43, 66)
                        .addBox(-7.0F, -4.0F, 25.0F, 14.0F, 8.0F, 3.0F),
                PartPose.offset(0.0F, 13.0F, -8.0F));
        root.addOrReplaceChild("seg8",
                CubeListBuilder.create().texOffs(49, 53)
                        .addBox(-5.5F, -4.1F, 28.0F, 11.0F, 8.0F, 2.0F),
                PartPose.offset(0.0F, 13.0F, -8.0F));
        root.addOrReplaceChild("tailseg1",
                CubeListBuilder.create().texOffs(92, 0)
                        .addBox(-4.0F, -1.0F, 0.0F, 8.0F, 4.0F, 10.0F),
                PartPose.offsetAndRotation(0.0F, 13.0F, 20.0F, 0.5948578F, 0.0F, 0.0F));
        root.addOrReplaceChild("tailseg2",
                CubeListBuilder.create().texOffs(90, 15)
                        .addBox(-3.5F, -2.0F, 0.0F, 7.0F, 4.0F, 12.0F),
                PartPose.offsetAndRotation(0.0F, 10.0F, 27.0F, 1.07818F, 0.0F, 0.0F));
        root.addOrReplaceChild("tailseg3",
                CubeListBuilder.create().texOffs(96, 32)
                        .addBox(-3.0F, -2.0F, 1.0F, 6.0F, 4.0F, 10.0F),
                PartPose.offsetAndRotation(0.0F, 2.0F, 32.0F, 1.710216F, 0.0F, 0.0F));
        root.addOrReplaceChild("tailseg4",
                CubeListBuilder.create().texOffs(96, 47)
                        .addBox(-2.5F, -2.0F, 0.0F, 5.0F, 4.0F, 11.0F),
                PartPose.offsetAndRotation(0.0F, -7.0F, 31.0F, 2.267895F, 0.0F, 0.0F));
        root.addOrReplaceChild("tailseg5",
                CubeListBuilder.create().texOffs(98, 63)
                        .addBox(-2.0F, -2.0F, 0.0F, 4.0F, 4.0F, 11.0F),
                PartPose.offsetAndRotation(0.0F, -14.0F, 25.0F, 2.899932F, 0.0F, 0.0F));
        root.addOrReplaceChild("tailseg6",
                CubeListBuilder.create().texOffs(98, 79)
                        .addBox(-2.0F, -2.0F, 0.0F, 4.0F, 4.0F, 11.0F),
                PartPose.offsetAndRotation(0.0F, -17.0F, 16.0F, -2.602503F, 0.0F, 0.0F));
        root.addOrReplaceChild("tailseg7",
                CubeListBuilder.create().texOffs(94, 95)
                        .addBox(-3.0F, -2.0F, 0.0F, 6.0F, 4.0F, 11.0F),
                PartPose.offsetAndRotation(0.0F, -12.0F, 8.0F, -0.2230717F, 0.0F, 0.0F));
        root.addOrReplaceChild("tailseg8",
                CubeListBuilder.create().texOffs(102, 111)
                        .addBox(-4.0F, -2.0F, 4.0F, 8.0F, 4.0F, 5.0F),
                PartPose.offsetAndRotation(0.0F, -12.0F, 8.0F, -0.2230717F, 0.0F, 0.0F));
        root.addOrReplaceChild("stinger1",
                CubeListBuilder.create().texOffs(83, 0)
                        .addBox(-0.5F, -0.5F, 0.0F, 1.0F, 1.0F, 3.0F),
                PartPose.offsetAndRotation(0.0F, -10.0F, 18.0F, 0.2230717F, 0.0F, 0.0F));
        root.addOrReplaceChild("stinger2",
                CubeListBuilder.create().texOffs(83, 0)
                        .addBox(-0.5F, -0.5F, 0.0F, 1.0F, 1.0F, 3.0F),
                PartPose.offsetAndRotation(0.0F, -10.5F, 20.5F, -0.2602503F, 0.0F, 0.0F));
        root.addOrReplaceChild("stinger3",
                CubeListBuilder.create().texOffs(79, 5)
                        .addBox(-0.5F, -0.5F, 0.0F, 1.0F, 1.0F, 5.0F),
                PartPose.offsetAndRotation(0.0F, -10.0F, 23.0F, -0.8551081F, 0.0F, 0.0F));
        root.addOrReplaceChild("leftShoulder",
                CubeListBuilder.create().texOffs(69, 103)
                        .addBox(-3.0F, -3.0F, -4.0F, 6.0F, 6.0F, 4.0F),
                PartPose.offsetAndRotation(7.0F, 13.0F, -22.0F, 0.0F, -0.8551081F, 0.0F));
        root.addOrReplaceChild("leftArmSeg1",
                CubeListBuilder.create().texOffs(55, 0)
                        .addBox(-3.0F, -3.0F, -10.0F, 4.0F, 6.0F, 13.0F),
                PartPose.offsetAndRotation(10.0F, 13.0F, -24.0F, 0.0F, -2.044824F, 0.0F));
        root.addOrReplaceChild("leftArmSeg2",
                CubeListBuilder.create().texOffs(130, 0)
                        .addBox(-7.0F, -3.0F, -17.0F, 8.0F, 6.0F, 17.0F),
                PartPose.offsetAndRotation(19.0F, 13.0F, -22.0F, 0.0F, -0.7435722F, 0.0F));
        root.addOrReplaceChild("leftArmSeg3",
                CubeListBuilder.create().texOffs(130, 50)
                        .addBox(-3.0F, -3.0F, -24.0F, 4.0F, 6.0F, 24.0F),
                PartPose.offsetAndRotation(29.0F, 13.0F, -33.0F, 0.0F, 0.3717861F, 0.0F));
        root.addOrReplaceChild("leftArmSeg4",
                CubeListBuilder.create().texOffs(181, 0)
                        .addBox(-3.0F, -3.0F, -14.0F, 8.0F, 6.0F, 12.0F),
                PartPose.offsetAndRotation(29.0F, 13.0F, -33.0F, 0.0F, 1.487144F, 0.0F));
        root.addOrReplaceChild("rightShoulder",
                CubeListBuilder.create().texOffs(69, 103)
                        .addBox(-3.0F, -3.0F, -4.0F, 6.0F, 6.0F, 4.0F),
                PartPose.offsetAndRotation(-7.0F, 13.0F, -22.0F, 0.0F, 0.8551066F, 0.0F));
        root.addOrReplaceChild("rightArmSeg1",
                CubeListBuilder.create().texOffs(55, 0)
                        .addBox(-1.0F, -3.0F, -10.0F, 4.0F, 6.0F, 13.0F),
                PartPose.offsetAndRotation(-10.0F, 13.0F, -24.0F, 0.0F, 2.044828F, 0.0F));
        root.addOrReplaceChild("rightArmSeg2",
                CubeListBuilder.create().texOffs(130, 0)
                        .addBox(-1.0F, -3.0F, -17.0F, 8.0F, 6.0F, 17.0F),
                PartPose.offsetAndRotation(-19.0F, 13.0F, -22.0F, 0.0F, 0.7435801F, 0.0F));
        root.addOrReplaceChild("rightArmSeg3",
                CubeListBuilder.create().texOffs(130, 50)
                        .addBox(-1.0F, -3.0F, -24.0F, 4.0F, 6.0F, 24.0F),
                PartPose.offsetAndRotation(-29.0F, 13.0F, -33.0F, 0.0F, -0.37179F, 0.0F));
        root.addOrReplaceChild("rightArmSeg4",
                CubeListBuilder.create().texOffs(181, 0)
                        .addBox(-5.0F, -3.0F, -14.0F, 8.0F, 6.0F, 12.0F),
                PartPose.offsetAndRotation(-29.0F, 13.0F, -33.0F, 0.0F, -1.487143F, 0.0F));
        root.addOrReplaceChild("rightPincer",
                CubeListBuilder.create().texOffs(130, 24)
                        .addBox(-1.0F, -3.0F, -19.0F, 2.0F, 6.0F, 19.0F),
                PartPose.offsetAndRotation(-17.0F, 13.0F, -33.0F, 0.0F, -0.0743611F, 0.0F));
        root.addOrReplaceChild("leftPincer",
                CubeListBuilder.create().texOffs(130, 24)
                        .addBox(-1.0F, -3.0F, -19.0F, 2.0F, 6.0F, 19.0F),
                PartPose.offsetAndRotation(17.0F, 13.0F, -33.0F, 0.0F, 0.0743685F, 0.0F));
        root.addOrReplaceChild("lefteye",
                CubeListBuilder.create().texOffs(0, 113)
                        .addBox(-0.5F, -5.0F, -7.5F, 3.0F, 2.0F, 3.0F),
                PartPose.offsetAndRotation(0.0F, 13.0F, -8.0F, 0.0F, 0.0F, 0.2974289F));
        root.addOrReplaceChild("righteye",
                CubeListBuilder.create().texOffs(0, 113)
                        .addBox(-2.5F, -5.0F, -7.5F, 3.0F, 2.0F, 3.0F),
                PartPose.offsetAndRotation(0.0F, 13.0F, -8.0F, 0.0F, 0.0F, -0.2974216F));
        root.addOrReplaceChild("rightMandible",
                CubeListBuilder.create().texOffs(76, 55)
                        .addBox(-2.0F, -3.0F, -4.0F, 4.0F, 4.0F, 4.0F),
                PartPose.offsetAndRotation(-2.0F, 13.0F, -23.0F, 0.1115358F, 0.3346075F, 0.0F));
        root.addOrReplaceChild("leftMandible",
                CubeListBuilder.create().texOffs(76, 55)
                        .addBox(-2.0F, -3.0F, -4.0F, 4.0F, 4.0F, 4.0F),
                PartPose.offsetAndRotation(2.0F, 13.0F, -23.0F, 0.111544F, -0.3346145F, 0.0F));
        root.addOrReplaceChild("rightManPart2",
                CubeListBuilder.create().texOffs(82, 64)
                        .addBox(-0.5F, -0.5F, -6.0F, 1.0F, 1.0F, 6.0F),
                PartPose.offsetAndRotation(-3.0F, 11.0F, -26.0F, 1.189716F, 0.0F, 0.0F));
        root.addOrReplaceChild("leftManPart2",
                CubeListBuilder.create().texOffs(82, 64)
                        .addBox(-0.5F, -0.5F, -6.0F, 1.0F, 1.0F, 6.0F),
                PartPose.offsetAndRotation(3.0F, 11.0F, -26.0F, 1.188848F, 0.0F, 0.0F));
        root.addOrReplaceChild("leg1Seg1",
                CubeListBuilder.create().texOffs(20, 20)
                        .addBox(0.0F, -1.5F, -2.0F, 4.0F, 3.0F, 4.0F),
                PartPose.offset(9.0F, 13.0F, -10.0F));
        root.addOrReplaceChild("leg1Seg2",
                CubeListBuilder.create().texOffs(21, 0)
                        .addBox(0.0F, -1.5F, -1.5F, 13.0F, 3.0F, 3.0F),
                PartPose.offsetAndRotation(12.0F, 13.0F, -10.0F, 0.0F, 0.0F, -0.9294576F));
        root.addOrReplaceChild("leg1Seg3",
                CubeListBuilder.create().texOffs(15, 8)
                        .addBox(0.0F, -1.5F, -1.5F, 13.0F, 3.0F, 3.0F),
                PartPose.offsetAndRotation(18.0F, 3.0F, -10.0F, 0.0F, 0.0F, 0.6320361F));
        root.addOrReplaceChild("leg1Seg4",
                CubeListBuilder.create().texOffs(0, 14)
                        .addBox(0.0F, -1.5F, -1.5F, 3.0F, 10.0F, 3.0F),
                PartPose.offset(26.0F, 12.0F, -10.0F));
        root.addOrReplaceChild("leg1Seg5",
                CubeListBuilder.create().texOffs(0, 0)
                        .addBox(0.0F, -1.5F, -1.5F, 7.0F, 3.0F, 3.0F),
                PartPose.offsetAndRotation(27.0F, 19.0F, -10.0F, 0.0F, 0.0F, 0.669215F));
        root.addOrReplaceChild("leg2Seg1",
                CubeListBuilder.create().texOffs(20, 20)
                        .addBox(0.0F, -1.5F, -2.0F, 4.0F, 3.0F, 4.0F),
                PartPose.offset(8.5F, 13.0F, -4.0F));
        root.addOrReplaceChild("leg2Seg2",
                CubeListBuilder.create().texOffs(21, 0)
                        .addBox(0.0F, -1.5F, -1.5F, 13.0F, 3.0F, 3.0F),
                PartPose.offsetAndRotation(12.0F, 13.0F, -4.0F, 0.0F, 0.0F, -0.9294576F));
        root.addOrReplaceChild("leg2Seg3",
                CubeListBuilder.create().texOffs(15, 8)
                        .addBox(0.0F, -1.5F, -1.5F, 13.0F, 3.0F, 3.0F),
                PartPose.offsetAndRotation(18.0F, 3.0F, -4.0F, 0.0F, 0.0F, 0.6320361F));
        root.addOrReplaceChild("leg2Seg4",
                CubeListBuilder.create().texOffs(0, 14)
                        .addBox(0.0F, -1.5F, -1.5F, 3.0F, 10.0F, 3.0F),
                PartPose.offset(26.0F, 12.0F, -4.0F));
        root.addOrReplaceChild("leg2Seg5",
                CubeListBuilder.create().texOffs(0, 0)
                        .addBox(0.0F, -1.5F, -1.5F, 7.0F, 3.0F, 3.0F),
                PartPose.offsetAndRotation(27.0F, 19.0F, -4.0F, 0.0F, 0.0F, 0.669215F));
        root.addOrReplaceChild("leg3Seg1",
                CubeListBuilder.create().texOffs(20, 20)
                        .addBox(0.0F, -1.5F, -2.0F, 4.0F, 3.0F, 4.0F),
                PartPose.offset(9.5F, 13.0F, 2.0F));
        root.addOrReplaceChild("leg3Seg2",
                CubeListBuilder.create().texOffs(21, 0)
                        .addBox(0.0F, -1.5F, -1.5F, 13.0F, 3.0F, 3.0F),
                PartPose.offsetAndRotation(12.0F, 13.0F, 2.0F, 0.0F, 0.0F, -0.9294576F));
        root.addOrReplaceChild("leg3Seg3",
                CubeListBuilder.create().texOffs(15, 8)
                        .addBox(0.0F, -1.5F, -1.5F, 13.0F, 3.0F, 3.0F),
                PartPose.offsetAndRotation(18.0F, 3.0F, 2.0F, 0.0F, 0.0F, 0.6320361F));
        root.addOrReplaceChild("leg3Seg4",
                CubeListBuilder.create().texOffs(0, 14)
                        .addBox(0.0F, -1.5F, -1.5F, 3.0F, 10.0F, 3.0F),
                PartPose.offset(26.0F, 12.0F, 2.0F));
        root.addOrReplaceChild("leg3Seg5",
                CubeListBuilder.create().texOffs(0, 0)
                        .addBox(0.0F, -1.5F, -1.5F, 7.0F, 3.0F, 3.0F),
                PartPose.offsetAndRotation(27.0F, 19.0F, 2.0F, 0.0F, 0.0F, 0.669215F));
        root.addOrReplaceChild("leg4Seg1",
                CubeListBuilder.create().texOffs(20, 20)
                        .addBox(0.0F, -1.5F, -2.0F, 4.0F, 3.0F, 4.0F),
                PartPose.offset(9.0F, 13.0F, 8.0F));
        root.addOrReplaceChild("leg4Seg2",
                CubeListBuilder.create().texOffs(21, 0)
                        .addBox(0.0F, -1.5F, -1.5F, 13.0F, 3.0F, 3.0F),
                PartPose.offsetAndRotation(12.0F, 13.0F, 8.0F, 0.0F, 0.0F, -0.9294576F));
        root.addOrReplaceChild("leg4Seg3",
                CubeListBuilder.create().texOffs(15, 8)
                        .addBox(0.0F, -1.5F, -1.5F, 13.0F, 3.0F, 3.0F),
                PartPose.offsetAndRotation(18.0F, 3.0F, 8.0F, 0.0F, 0.0F, 0.6320361F));
        root.addOrReplaceChild("leg4Seg4",
                CubeListBuilder.create().texOffs(0, 14)
                        .addBox(0.0F, -1.5F, -1.5F, 3.0F, 10.0F, 3.0F),
                PartPose.offset(26.0F, 12.0F, 8.0F));
        root.addOrReplaceChild("leg4Seg5",
                CubeListBuilder.create().texOffs(0, 0)
                        .addBox(0.0F, -1.5F, -1.5F, 7.0F, 3.0F, 3.0F),
                PartPose.offsetAndRotation(27.0F, 19.0F, 8.0F, 0.0F, 0.0F, 0.669215F));
        root.addOrReplaceChild("leg5Seg1",
                CubeListBuilder.create().texOffs(20, 20)
                        .addBox(-4.0F, -1.5F, -2.0F, 4.0F, 3.0F, 4.0F),
                PartPose.offset(-9.0F, 13.0F, -10.0F));
        root.addOrReplaceChild("leg5Seg2",
                CubeListBuilder.create().texOffs(21, 0)
                        .addBox(-13.0F, -1.5F, -1.5F, 13.0F, 3.0F, 3.0F),
                PartPose.offsetAndRotation(-12.0F, 14.0F, -10.0F, 0.0F, 0.0F, 0.9294653F));
        root.addOrReplaceChild("leg5Seg3",
                CubeListBuilder.create().texOffs(15, 8)
                        .addBox(-13.0F, -1.5F, -1.5F, 13.0F, 3.0F, 3.0F),
                PartPose.offsetAndRotation(-18.0F, 4.0F, -10.0F, 0.0F, 0.0F, -0.6320364F));
        root.addOrReplaceChild("leg5Seg4",
                CubeListBuilder.create().texOffs(0, 14)
                        .addBox(-3.0F, -1.5F, -1.5F, 3.0F, 10.0F, 3.0F),
                PartPose.offset(-26.0F, 12.0F, -10.0F));
        root.addOrReplaceChild("leg5Seg5",
                CubeListBuilder.create().texOffs(0, 0)
                        .addBox(0.0F, -1.5F, -1.5F, 7.0F, 3.0F, 3.0F),
                PartPose.offsetAndRotation(-27.0F, 19.0F, -10.0F, 0.0F, 0.0F, 2.240008F));
        root.addOrReplaceChild("leg6Seg1",
                CubeListBuilder.create().texOffs(20, 20)
                        .addBox(-4.0F, -1.5F, -2.0F, 4.0F, 3.0F, 4.0F),
                PartPose.offset(-8.5F, 13.0F, -4.0F));
        root.addOrReplaceChild("leg6Seg2",
                CubeListBuilder.create().texOffs(21, 0)
                        .addBox(-13.0F, -1.5F, -1.5F, 13.0F, 3.0F, 3.0F),
                PartPose.offsetAndRotation(-12.0F, 14.0F, -4.0F, 0.0F, 0.0F, 0.9294576F));
        root.addOrReplaceChild("leg6Seg3",
                CubeListBuilder.create().texOffs(15, 8)
                        .addBox(-13.0F, -1.5F, -1.5F, 13.0F, 3.0F, 3.0F),
                PartPose.offsetAndRotation(-18.0F, 4.0F, -4.0F, 0.0F, 0.0F, -0.6320361F));
        root.addOrReplaceChild("leg6Seg4",
                CubeListBuilder.create().texOffs(0, 14)
                        .addBox(-3.0F, -1.5F, -1.5F, 3.0F, 10.0F, 3.0F),
                PartPose.offset(-26.0F, 12.0F, -4.0F));
        root.addOrReplaceChild("leg6Seg5",
                CubeListBuilder.create().texOffs(0, 0)
                        .addBox(0.0F, -1.5F, -1.5F, 7.0F, 3.0F, 3.0F),
                PartPose.offsetAndRotation(-27.0F, 19.0F, -4.0F, 0.0F, 0.0F, 2.240008F));
        root.addOrReplaceChild("leg7Seg1",
                CubeListBuilder.create().texOffs(20, 20)
                        .addBox(-4.0F, -1.5F, -2.0F, 4.0F, 3.0F, 4.0F),
                PartPose.offset(-9.5F, 13.0F, 2.0F));
        root.addOrReplaceChild("leg7Seg2",
                CubeListBuilder.create().texOffs(21, 0)
                        .addBox(-13.0F, -1.5F, -1.5F, 13.0F, 3.0F, 3.0F),
                PartPose.offsetAndRotation(-12.0F, 14.0F, 2.0F, 0.0F, 0.0F, 0.9294576F));
        root.addOrReplaceChild("leg7Seg3",
                CubeListBuilder.create().texOffs(15, 8)
                        .addBox(-13.0F, -1.5F, -1.5F, 13.0F, 3.0F, 3.0F),
                PartPose.offsetAndRotation(-18.0F, 4.0F, 2.0F, 0.0F, 0.0F, -0.6320361F));
        root.addOrReplaceChild("leg7Seg4",
                CubeListBuilder.create().texOffs(0, 14)
                        .addBox(-3.0F, -1.5F, -1.5F, 3.0F, 10.0F, 3.0F),
                PartPose.offset(-26.0F, 12.0F, 2.0F));
        root.addOrReplaceChild("leg7Seg5",
                CubeListBuilder.create().texOffs(0, 0)
                        .addBox(0.0F, -1.5F, -1.5F, 7.0F, 3.0F, 3.0F),
                PartPose.offsetAndRotation(-27.0F, 19.0F, 2.0F, 0.0F, 0.0F, 2.240008F));
        root.addOrReplaceChild("leg8Seg1",
                CubeListBuilder.create().texOffs(20, 20)
                        .addBox(-4.0F, -1.5F, -2.0F, 4.0F, 3.0F, 4.0F),
                PartPose.offset(-9.0F, 13.0F, 8.0F));
        root.addOrReplaceChild("leg8Seg2",
                CubeListBuilder.create().texOffs(21, 0)
                        .addBox(-12.0F, -1.5F, -1.5F, 13.0F, 3.0F, 3.0F),
                PartPose.offsetAndRotation(-12.0F, 14.0F, 8.0F, 0.0F, 0.0F, 0.9294576F));
        root.addOrReplaceChild("leg8Seg3",
                CubeListBuilder.create().texOffs(15, 8)
                        .addBox(-13.0F, -1.5F, -1.5F, 13.0F, 3.0F, 3.0F),
                PartPose.offsetAndRotation(-18.0F, 4.0F, 8.0F, 0.0F, 0.0F, -0.6320361F));
        root.addOrReplaceChild("leg8Seg4",
                CubeListBuilder.create().texOffs(0, 14)
                        .addBox(-3.0F, -1.5F, -1.5F, 3.0F, 10.0F, 3.0F),
                PartPose.offset(-26.0F, 12.0F, 8.0F));
        root.addOrReplaceChild("leg8Seg5",
                CubeListBuilder.create().texOffs(0, 0)
                        .addBox(0.0F, -1.5F, -1.5F, 7.0F, 3.0F, 3.0F),
                PartPose.offsetAndRotation(-27.0F, 19.0F, 8.0F, 0.0F, 0.0F, 2.240008F));
        return LayerDefinition.create(mesh, 256, 128);
    }

    @Override
    public ModelPart root() {
        return this.root;
    }

    @Override
    public void setupAnim(
            EmperorScorpion entity,
            float limbSwing,
            float limbSwingAmount,
            float ageInTicks,
            float netHeadYaw,
            float headPitch) {
        float f1 = limbSwingAmount;
        float f2 = ageInTicks;
        float pi4 = 1.570795F;
        float newangle;
        float nextangle;
        float upangle;

        newangle = Mth.cos(f2 * 2.0F * this.wingspeed) * (float) Math.PI * 0.12F * f1;
        nextangle = Mth.cos((f2 + 0.1F) * 2.0F * this.wingspeed) * (float) Math.PI * 0.12F * f1;
        upangle = 0.0F;
        if (nextangle > newangle) {
            upangle = 0.47F * f1 - Math.abs(newangle);
        }
        this.doLeftLeg(this.leg1Seg1, this.leg1Seg2, this.leg1Seg3, this.leg1Seg4, this.leg1Seg5, newangle, upangle);
        this.doRightLeg(this.leg5Seg1, this.leg5Seg2, this.leg5Seg3, this.leg5Seg4, this.leg5Seg5, -newangle, upangle);

        newangle = Mth.cos(f2 * 2.0F * this.wingspeed - 1.0F * pi4) * (float) Math.PI * 0.12F * f1;
        nextangle = Mth.cos((f2 + 0.1F) * 2.0F * this.wingspeed - 1.0F * pi4) * (float) Math.PI * 0.12F * f1;
        upangle = 0.0F;
        if (nextangle > newangle) {
            upangle = 0.47F * f1 - Math.abs(newangle);
        }
        this.doLeftLeg(this.leg2Seg1, this.leg2Seg2, this.leg2Seg3, this.leg2Seg4, this.leg2Seg5, newangle, upangle);
        this.doRightLeg(this.leg6Seg1, this.leg6Seg2, this.leg6Seg3, this.leg6Seg4, this.leg6Seg5, -newangle, upangle);

        newangle = Mth.cos(f2 * 2.0F * this.wingspeed - 2.0F * pi4) * (float) Math.PI * 0.12F * f1;
        nextangle = Mth.cos((f2 + 0.1F) * 2.0F * this.wingspeed - 2.0F * pi4) * (float) Math.PI * 0.12F * f1;
        upangle = 0.0F;
        if (nextangle > newangle) {
            upangle = 0.47F * f1 - Math.abs(newangle);
        }
        this.doLeftLeg(this.leg3Seg1, this.leg3Seg2, this.leg3Seg3, this.leg3Seg4, this.leg3Seg5, newangle, upangle);
        this.doRightLeg(this.leg7Seg1, this.leg7Seg2, this.leg7Seg3, this.leg7Seg4, this.leg7Seg5, -newangle, upangle);

        newangle = Mth.cos(f2 * 2.0F * this.wingspeed - 3.0F * pi4) * (float) Math.PI * 0.12F * f1;
        nextangle = Mth.cos((f2 + 0.1F) * 2.0F * this.wingspeed - 3.0F * pi4) * (float) Math.PI * 0.12F * f1;
        upangle = 0.0F;
        if (nextangle > newangle) {
            upangle = 0.47F * f1 - Math.abs(newangle);
        }
        this.doLeftLeg(this.leg4Seg1, this.leg4Seg2, this.leg4Seg3, this.leg4Seg4, this.leg4Seg5, newangle, upangle);
        this.doRightLeg(this.leg8Seg1, this.leg8Seg2, this.leg8Seg3, this.leg8Seg4, this.leg8Seg5, -newangle, upangle);

        if (entity.getAttacking() == 0) {
            newangle = Mth.cos(f2 * 0.5F * this.wingspeed) * (float) Math.PI * 0.05F;
        } else {
            newangle = Mth.cos(f2 * 2.5F * this.wingspeed) * (float) Math.PI * 0.15F;
        }
        this.leftManPart2.zRot = newangle;
        this.rightManPart2.zRot = -newangle;

        RenderInfo r = entity.getRenderInfo();
        newangle = Mth.cos(f2 * 3.0F * this.wingspeed) * (float) Math.PI * 0.15F;
        nextangle = Mth.cos((f2 + 0.1F) * 3.0F * this.wingspeed) * (float) Math.PI * 0.15F;
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
        } else {
            this.doLeftClaw(newangle);
        }
        if (r.ri1 != 2 && r.ri1 != 3) {
            this.doRightClaw(0.0F);
        } else {
            this.doRightClaw(newangle);
        }
        if (r.ri2 == 1) {
            this.doTail(newangle);
        } else {
            this.doTail(0.0F);
        }
        entity.setRenderInfo(r);
    }

    private void doLeftLeg(
            ModelPart seg1,
            ModelPart seg2,
            ModelPart seg3,
            ModelPart seg4,
            ModelPart seg5,
            float angle,
            float upangle) {
        seg2.yRot = angle;
        seg3.yRot = angle;
        seg4.yRot = angle;
        seg5.yRot = angle;
        seg3.z = (float) (seg2.z - Math.sin(angle) * 6.0);
        seg3.x = (float) (seg2.x - Math.abs(Math.sin(angle) * 6.0) + 6.0);
        seg4.z = (float) (seg3.z - Math.sin(angle) * 9.0);
        seg4.x = (float) (seg3.x - Math.abs(Math.sin(angle) * 9.0) + 9.0);
        seg5.z = (float) (seg4.z - Math.sin(angle) * 1.0);
        seg5.x = (float) (seg4.x - Math.abs(Math.sin(angle) * 1.0) + 1.0);
        seg2.zRot = -upangle - 0.929F;
        seg3.zRot = -upangle + 0.632F;
        seg3.y = seg2.y + (float) (11.5 * Math.sin(seg2.zRot));
        seg4.y = seg3.y + (float) (11.5 * Math.sin(seg3.zRot));
        seg5.y = seg4.y + 6.5F;
    }

    private void doRightLeg(
            ModelPart seg1,
            ModelPart seg2,
            ModelPart seg3,
            ModelPart seg4,
            ModelPart seg5,
            float angle,
            float upangle) {
        seg2.yRot = angle;
        seg3.yRot = angle;
        seg4.yRot = angle;
        seg5.yRot = -angle;
        seg3.z = (float) (seg2.z + Math.sin(angle) * 6.0);
        seg3.x = (float) (seg2.x + Math.abs(Math.sin(angle) * 6.0) - 6.0);
        seg4.z = (float) (seg3.z + Math.sin(angle) * 9.0);
        seg4.x = (float) (seg3.x + Math.abs(Math.sin(angle) * 9.0) - 9.0);
        seg5.z = (float) (seg4.z + Math.sin(angle) * 1.0);
        seg5.x = (float) (seg4.x + Math.abs(Math.sin(angle) * 1.0) - 1.0);
        seg2.zRot = upangle + 0.929F;
        seg3.zRot = upangle - 0.632F;
        seg3.y = seg2.y - (float) (11.5 * Math.sin(seg2.zRot));
        seg4.y = seg3.y - (float) (11.5 * Math.sin(seg3.zRot));
        seg5.y = seg4.y + 6.5F;
    }

    private void doLeftClaw(float angle) {
        this.leftArmSeg1.yRot = -1.57F + angle;
        this.leftArmSeg2.z = (float) (-22.0 - Math.cos(this.leftArmSeg1.yRot) * 12.0);
        this.leftArmSeg3.z = this.leftArmSeg2.z - 11.0F;
        this.leftArmSeg4.z = this.leftArmSeg2.z - 11.0F;
        this.leftPincer.z = this.leftArmSeg2.z - 11.0F;
        this.leftArmSeg3.yRot = 0.074F + angle;
        this.leftPincer.yRot = 0.371F - angle;
    }

    private void doRightClaw(float angle) {
        this.rightArmSeg1.yRot = 1.57F - angle;
        this.rightArmSeg2.z = (float) (-22.0 - Math.cos(this.rightArmSeg1.yRot) * 12.0);
        this.rightArmSeg3.z = this.rightArmSeg2.z - 11.0F;
        this.rightArmSeg4.z = this.rightArmSeg2.z - 11.0F;
        this.rightPincer.z = this.rightArmSeg2.z - 11.0F;
        this.rightArmSeg3.yRot = -0.074F - angle;
        this.rightPincer.yRot = -0.371F + angle;
    }

    private void doTail(float angle) {
        this.tailseg1.xRot = 0.594F + angle;
        this.tailseg2.xRot = this.tailseg1.xRot + 0.48399997F + angle;
        this.tailseg2.y = (float) (this.tailseg1.y - Math.sin(this.tailseg1.xRot) * 9.0);
        this.tailseg2.z = (float) (this.tailseg1.z + Math.cos(this.tailseg1.xRot) * 9.0);
        this.tailseg3.xRot = this.tailseg2.xRot + 0.6320001F + angle;
        this.tailseg3.y = (float) (this.tailseg2.y - Math.sin(this.tailseg2.xRot) * 10.0);
        this.tailseg3.z = (float) (this.tailseg2.z + Math.cos(this.tailseg2.xRot) * 10.0);
        this.tailseg4.xRot = this.tailseg3.xRot + 0.5569999F - angle;
        this.tailseg4.y = (float) (this.tailseg3.y - Math.sin(this.tailseg3.xRot) * 10.0);
        this.tailseg4.z = (float) (this.tailseg3.z + Math.cos(this.tailseg3.xRot) * 10.0);
        this.tailseg5.xRot = this.tailseg4.xRot + 0.63199997F - angle;
        this.tailseg5.y = (float) (this.tailseg4.y - Math.sin(this.tailseg4.xRot) * 10.0);
        this.tailseg5.z = (float) (this.tailseg4.z + Math.cos(this.tailseg4.xRot) * 10.0);
        this.tailseg6.xRot = this.tailseg5.xRot + -5.501F - angle * 3.0F / 2.0F - 0.4F;
        this.tailseg6.y = (float) (this.tailseg5.y - Math.sin(this.tailseg5.xRot) * 10.0);
        this.tailseg6.z = (float) (this.tailseg5.z + Math.cos(this.tailseg5.xRot) * 10.0);
        this.tailseg7.xRot = this.tailseg6.xRot + -2.822F - angle * 2.5F - 2.2F;
        this.tailseg7.y = (float) (this.tailseg6.y - Math.sin(this.tailseg6.xRot) * 10.0);
        this.tailseg7.z = (float) (this.tailseg6.z + Math.cos(this.tailseg6.xRot) * 10.0);
        this.tailseg8.xRot = this.tailseg7.xRot;
        this.tailseg8.y = this.tailseg7.y;
        this.tailseg8.z = this.tailseg7.z;
        this.stinger1.xRot = this.tailseg7.xRot + 0.0F + angle * 0.66F;
        this.stinger1.y = (float) (this.tailseg7.y - Math.sin(this.tailseg7.xRot) * 10.0);
        this.stinger1.z = (float) (this.tailseg7.z + Math.cos(this.tailseg7.xRot) * 10.0);
        this.stinger2.xRot = this.stinger1.xRot + -0.48F + angle;
        this.stinger2.y = (float) (this.stinger1.y - Math.sin(this.stinger1.xRot) * 3.0);
        this.stinger2.z = (float) (this.stinger1.z + Math.cos(this.stinger1.xRot) * 3.0);
        this.stinger3.xRot = this.stinger2.xRot + -1.01F + angle * 1.7F;
        this.stinger3.y = (float) (this.stinger2.y - Math.sin(this.stinger2.xRot) * 3.0);
        this.stinger3.z = (float) (this.stinger2.z + Math.cos(this.stinger2.xRot) * 3.0);
    }
}
