package danger.orespawn.client.model;

import danger.orespawn.entity.Lizard;
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
 * Port of gold {@code ModelLizard} (1.7.10 ModelBase, tex 128×128) → 1.21 HierarchicalModel.
 * Cube sizes/UVs/pivots/base rots 1:1. Wingspeed default 0.65 matches ClientProxy.
 * Full walk / jaw / serpentine tail / head-yaw chain from gold {@code func_78088_a}.
 */
@OnlyIn(Dist.CLIENT)
public class ModelLizard extends HierarchicalModel<Lizard> {
    /** Model layer location. */
    public static final ModelLayerLocation LAYER_LOCATION =
            new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "lizard"), "main");

    private final float wingspeed;
    private final ModelPart root;
    private final ModelPart BodyBack;
    private final ModelPart TopBackLeftLeg;
    private final ModelPart TailTip;
    private final ModelPart BodyFront;
    private final ModelPart TailBase1;
    private final ModelPart Tail2;
    private final ModelPart Tail3;
    private final ModelPart Tail4;
    private final ModelPart Neck;
    private final ModelPart TopFrontLeftLeg;
    private final ModelPart TopBackRightLeg;
    private final ModelPart BottomBackRightLeg;
    private final ModelPart TopFrontRightLeg;
    private final ModelPart BottomBackLeftLeg;
    private final ModelPart BottomFrontRightLeg;
    private final ModelPart BottomFrontLeftLeg;
    private final ModelPart BodyCenter;
    private final ModelPart Toe7;
    private final ModelPart Toe6;
    private final ModelPart BackLeftFoot;
    private final ModelPart Toe4;
    private final ModelPart Toe5;
    private final ModelPart BackRightFoot;
    private final ModelPart Toe8;
    private final ModelPart Toe1;
    private final ModelPart FrontLeftFoot;
    private final ModelPart Toe3;
    private final ModelPart Toe2;
    private final ModelPart FrontRightFoot;
    private final ModelPart FinRidge7;
    private final ModelPart FinRidge6;
    private final ModelPart FinRidge5;
    private final ModelPart FinRidge4;
    private final ModelPart FinRidge3;
    private final ModelPart FinRidge2;
    private final ModelPart FinRidge1;
    private final ModelPart Fin10;
    private final ModelPart Fin9;
    private final ModelPart Fin8;
    private final ModelPart Fin7;
    private final ModelPart Fin6;
    private final ModelPart Fin5;
    private final ModelPart Fin3;
    private final ModelPart Fin2;
    private final ModelPart Tooth11;
    private final ModelPart Tooth10;
    private final ModelPart Tooth8;
    private final ModelPart Tooth7;
    private final ModelPart Tooth6;
    private final ModelPart Tooth5;
    private final ModelPart Tooth4;
    private final ModelPart Tooth3;
    private final ModelPart Tooth2;
    private final ModelPart CenterRightNose;
    private final ModelPart CenterLeftNose;
    private final ModelPart Tooth1;
    private final ModelPart BottomNose;
    private final ModelPart TopNose;
    private final ModelPart JawTop;
    private final ModelPart CenterMiddleNose;
    private final ModelPart RightEye;
    private final ModelPart LeftEye;
    private final ModelPart Tooth16;
    private final ModelPart Tooth15;
    private final ModelPart Tooth14;
    private final ModelPart Tooth13;
    private final ModelPart Tooth12;
    private final ModelPart Tooth9;
    private final ModelPart BottomJaw;
    private final ModelPart Hat1;
    private final ModelPart Hat2;

    public ModelLizard(ModelPart root) {
        this(root, 0.65F);
    }

    public ModelLizard(ModelPart root, float wingspeed) {
        this.root = root;
        this.wingspeed = wingspeed;
        this.BodyBack = root.getChild("BodyBack");
        this.TopBackLeftLeg = root.getChild("TopBackLeftLeg");
        this.TailTip = root.getChild("TailTip");
        this.BodyFront = root.getChild("BodyFront");
        this.TailBase1 = root.getChild("TailBase1");
        this.Tail2 = root.getChild("Tail2");
        this.Tail3 = root.getChild("Tail3");
        this.Tail4 = root.getChild("Tail4");
        this.Neck = root.getChild("Neck");
        this.TopFrontLeftLeg = root.getChild("TopFrontLeftLeg");
        this.TopBackRightLeg = root.getChild("TopBackRightLeg");
        this.BottomBackRightLeg = root.getChild("BottomBackRightLeg");
        this.TopFrontRightLeg = root.getChild("TopFrontRightLeg");
        this.BottomBackLeftLeg = root.getChild("BottomBackLeftLeg");
        this.BottomFrontRightLeg = root.getChild("BottomFrontRightLeg");
        this.BottomFrontLeftLeg = root.getChild("BottomFrontLeftLeg");
        this.BodyCenter = root.getChild("BodyCenter");
        this.Toe7 = root.getChild("Toe7");
        this.Toe6 = root.getChild("Toe6");
        this.BackLeftFoot = root.getChild("BackLeftFoot");
        this.Toe4 = root.getChild("Toe4");
        this.Toe5 = root.getChild("Toe5");
        this.BackRightFoot = root.getChild("BackRightFoot");
        this.Toe8 = root.getChild("Toe8");
        this.Toe1 = root.getChild("Toe1");
        this.FrontLeftFoot = root.getChild("FrontLeftFoot");
        this.Toe3 = root.getChild("Toe3");
        this.Toe2 = root.getChild("Toe2");
        this.FrontRightFoot = root.getChild("FrontRightFoot");
        this.FinRidge7 = root.getChild("FinRidge7");
        this.FinRidge6 = root.getChild("FinRidge6");
        this.FinRidge5 = root.getChild("FinRidge5");
        this.FinRidge4 = root.getChild("FinRidge4");
        this.FinRidge3 = root.getChild("FinRidge3");
        this.FinRidge2 = root.getChild("FinRidge2");
        this.FinRidge1 = root.getChild("FinRidge1");
        this.Fin10 = root.getChild("Fin10");
        this.Fin9 = root.getChild("Fin9");
        this.Fin8 = root.getChild("Fin8");
        this.Fin7 = root.getChild("Fin7");
        this.Fin6 = root.getChild("Fin6");
        this.Fin5 = root.getChild("Fin5");
        this.Fin3 = root.getChild("Fin3");
        this.Fin2 = root.getChild("Fin2");
        this.Tooth11 = root.getChild("Tooth11");
        this.Tooth10 = root.getChild("Tooth10");
        this.Tooth8 = root.getChild("Tooth8");
        this.Tooth7 = root.getChild("Tooth7");
        this.Tooth6 = root.getChild("Tooth6");
        this.Tooth5 = root.getChild("Tooth5");
        this.Tooth4 = root.getChild("Tooth4");
        this.Tooth3 = root.getChild("Tooth3");
        this.Tooth2 = root.getChild("Tooth2");
        this.CenterRightNose = root.getChild("CenterRightNose");
        this.CenterLeftNose = root.getChild("CenterLeftNose");
        this.Tooth1 = root.getChild("Tooth1");
        this.BottomNose = root.getChild("BottomNose");
        this.TopNose = root.getChild("TopNose");
        this.JawTop = root.getChild("JawTop");
        this.CenterMiddleNose = root.getChild("CenterMiddleNose");
        this.RightEye = root.getChild("RightEye");
        this.LeftEye = root.getChild("LeftEye");
        this.Tooth16 = root.getChild("Tooth16");
        this.Tooth15 = root.getChild("Tooth15");
        this.Tooth14 = root.getChild("Tooth14");
        this.Tooth13 = root.getChild("Tooth13");
        this.Tooth12 = root.getChild("Tooth12");
        this.Tooth9 = root.getChild("Tooth9");
        this.BottomJaw = root.getChild("BottomJaw");
        this.Hat1 = root.getChild("Hat1");
        this.Hat2 = root.getChild("Hat2");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        root.addOrReplaceChild("BodyBack",
                CubeListBuilder.create().texOffs(92, 48).addBox(-4.0F, -4.0F, 0.0F, 8.0F, 8.0F, 8.0F),
                PartPose.offset(0.0F, 14.0F, 0.0F));
        root.addOrReplaceChild("TopBackLeftLeg",
                CubeListBuilder.create().texOffs(54, 32).addBox(0.0F, -2.0F, -2.0F, 8.0F, 3.0F, 3.0F),
                PartPose.offsetAndRotation(3.0F, 13.0F, 2.0F, 0.0F, 0.0F, (float) (Math.PI / 12)));
        root.addOrReplaceChild("TailTip",
                CubeListBuilder.create().texOffs(100, 118).addBox(-1.0F, -1.0F, 0.0F, 2.0F, 2.0F, 8.0F),
                PartPose.offset(0.0F, 23.0F, 41.0F));
        root.addOrReplaceChild("BodyFront",
                CubeListBuilder.create().texOffs(92, 16).addBox(-4.0F, -4.0F, -8.0F, 8.0F, 8.0F, 8.0F),
                PartPose.offset(0.0F, 14.0F, -8.0F));
        root.addOrReplaceChild("TailBase1",
                CubeListBuilder.create().texOffs(88, 64).addBox(-3.0F, -3.0F, 0.0F, 6.0F, 6.0F, 14.0F),
                PartPose.offsetAndRotation(0.0F, 14.0F, 7.0F, (float) (-Math.PI / 12), 0.0F, 0.0F));
        root.addOrReplaceChild("Tail2",
                CubeListBuilder.create().texOffs(95, 84).addBox(-2.0F, -2.0F, 0.0F, 4.0F, 4.0F, 10.0F),
                PartPose.offsetAndRotation(0.0F, 17.0F, 19.0F, (float) (-Math.PI / 6), 0.0F, 0.0F));
        root.addOrReplaceChild("Tail3",
                CubeListBuilder.create().texOffs(100, 98).addBox(-1.0F, -1.0F, 0.0F, 2.0F, 2.0F, 8.0F),
                PartPose.offsetAndRotation(0.0F, 21.0F, 26.0F, (float) (-Math.PI / 12), 0.0F, 0.0F));
        root.addOrReplaceChild("Tail4",
                CubeListBuilder.create().texOffs(100, 108).addBox(-1.0F, -1.0F, 0.0F, 2.0F, 2.0F, 8.0F),
                PartPose.offset(0.0F, 23.0F, 33.0F));
        root.addOrReplaceChild("Neck",
                CubeListBuilder.create().texOffs(100, 9).addBox(-3.0F, -2.0F, -2.0F, 6.0F, 5.0F, 2.0F),
                PartPose.offset(0.0F, 12.0F, -16.0F));
        root.addOrReplaceChild("TopFrontLeftLeg",
                CubeListBuilder.create().texOffs(26, 12).addBox(0.0F, -2.0F, -2.0F, 8.0F, 3.0F, 3.0F),
                PartPose.offsetAndRotation(3.0F, 13.0F, -12.0F, 0.0F, 0.0F, (float) (Math.PI / 12)));
        root.addOrReplaceChild("TopBackRightLeg",
                CubeListBuilder.create().texOffs(26, 32).addBox(-8.0F, -2.0F, -2.0F, 8.0F, 3.0F, 3.0F),
                PartPose.offsetAndRotation(-3.0F, 13.0F, 2.0F, 0.0F, 0.0F, (float) (-Math.PI / 12)));
        root.addOrReplaceChild("BottomBackRightLeg",
                CubeListBuilder.create().texOffs(25, 26).addBox(-12.0F, -8.0F, -2.0F, 9.0F, 3.0F, 3.0F),
                PartPose.offsetAndRotation(-3.0F, 13.0F, 2.0F, 0.0F, 0.0F, -1.308997F));
        root.addOrReplaceChild("TopFrontRightLeg",
                CubeListBuilder.create().texOffs(54, 12).addBox(-8.0F, -2.0F, -2.0F, 8.0F, 3.0F, 3.0F),
                PartPose.offsetAndRotation(-3.0F, 13.0F, -12.0F, 0.0F, 0.0F, (float) (-Math.PI / 12)));
        root.addOrReplaceChild("BottomBackLeftLeg",
                CubeListBuilder.create().texOffs(53, 26).addBox(3.0F, -8.0F, -2.0F, 9.0F, 3.0F, 3.0F),
                PartPose.offsetAndRotation(3.0F, 13.0F, 2.0F, 0.0F, 0.0F, 1.308997F));
        root.addOrReplaceChild("BottomFrontRightLeg",
                CubeListBuilder.create().texOffs(53, 18).addBox(-12.0F, -8.0F, -2.0F, 9.0F, 3.0F, 3.0F),
                PartPose.offsetAndRotation(-3.0F, 13.0F, -12.0F, 0.0F, 0.0F, -1.308997F));
        root.addOrReplaceChild("BottomFrontLeftLeg",
                CubeListBuilder.create().texOffs(25, 18).addBox(3.0F, -8.0F, -2.0F, 9.0F, 3.0F, 3.0F),
                PartPose.offsetAndRotation(3.0F, 13.0F, -12.0F, 0.0F, 0.0F, 1.308997F));
        root.addOrReplaceChild("BodyCenter",
                CubeListBuilder.create().texOffs(92, 32).addBox(-4.0F, -4.0F, -4.0F, 8.0F, 8.0F, 8.0F),
                PartPose.offset(0.0F, 14.0F, -4.0F));
        root.addOrReplaceChild("Toe7",
                CubeListBuilder.create().texOffs(104, 0).addBox(10.0F, 10.0F, -5.0F, 1.0F, 1.0F, 1.0F),
                PartPose.offset(3.0F, 13.0F, 2.0F));
        root.addOrReplaceChild("Toe6",
                CubeListBuilder.create().texOffs(108, 0).addBox(8.0F, 10.0F, -5.0F, 1.0F, 1.0F, 1.0F),
                PartPose.offset(3.0F, 13.0F, 2.0F));
        root.addOrReplaceChild("BackLeftFoot",
                CubeListBuilder.create().texOffs(20, 0).addBox(7.0F, 9.0F, -4.0F, 4.0F, 2.0F, 6.0F),
                PartPose.offset(3.0F, 13.0F, 2.0F));
        root.addOrReplaceChild("Toe4",
                CubeListBuilder.create().texOffs(80, 0).addBox(-11.0F, 10.0F, -5.0F, 1.0F, 1.0F, 1.0F),
                PartPose.offset(-3.0F, 13.0F, 2.0F));
        root.addOrReplaceChild("Toe5",
                CubeListBuilder.create().texOffs(84, 0).addBox(-9.0F, 10.0F, -5.0F, 1.0F, 1.0F, 1.0F),
                PartPose.offset(-3.0F, 13.0F, 2.0F));
        root.addOrReplaceChild("BackRightFoot",
                CubeListBuilder.create().texOffs(60, 0).addBox(-11.0F, 9.0F, -4.0F, 4.0F, 2.0F, 6.0F),
                PartPose.offset(-3.0F, 13.0F, 2.0F));
        root.addOrReplaceChild("Toe8",
                CubeListBuilder.create().texOffs(100, 0).addBox(10.0F, 10.0F, -5.0F, 1.0F, 1.0F, 1.0F),
                PartPose.offset(3.0F, 13.0F, -12.0F));
        root.addOrReplaceChild("Toe1",
                CubeListBuilder.create().texOffs(96, 0).addBox(8.0F, 10.0F, -5.0F, 1.0F, 1.0F, 1.0F),
                PartPose.offset(3.0F, 13.0F, -12.0F));
        root.addOrReplaceChild("FrontLeftFoot",
                CubeListBuilder.create().texOffs(40, 0).addBox(7.0F, 9.0F, -4.0F, 4.0F, 2.0F, 6.0F),
                PartPose.offset(3.0F, 13.0F, -12.0F));
        root.addOrReplaceChild("Toe3",
                CubeListBuilder.create().texOffs(88, 0).addBox(-11.0F, 10.0F, -5.0F, 1.0F, 1.0F, 1.0F),
                PartPose.offset(-3.0F, 13.0F, -12.0F));
        root.addOrReplaceChild("Toe2",
                CubeListBuilder.create().texOffs(92, 0).addBox(-9.0F, 10.0F, -5.0F, 1.0F, 1.0F, 1.0F),
                PartPose.offset(-3.0F, 13.0F, -12.0F));
        root.addOrReplaceChild("FrontRightFoot",
                CubeListBuilder.create().texOffs(0, 0).addBox(-11.0F, 9.0F, -4.0F, 4.0F, 2.0F, 6.0F),
                PartPose.offset(-3.0F, 13.0F, -12.0F));
        root.addOrReplaceChild("FinRidge7",
                CubeListBuilder.create().texOffs(0, 99).addBox(0.0F, -13.0F, 0.0F, 2.0F, 13.0F, 1.0F),
                PartPose.offsetAndRotation(-1.0F, 10.0F, -4.5F, -0.9666439F, 0.0F, 0.0F));
        root.addOrReplaceChild("FinRidge6",
                CubeListBuilder.create().texOffs(6, 98).addBox(0.0F, -13.0F, 0.0F, 2.0F, 13.0F, 1.0F),
                PartPose.offsetAndRotation(-1.0F, 10.0F, -4.0F, -0.5205006F, 0.0F, 0.0F));
        root.addOrReplaceChild("FinRidge5",
                CubeListBuilder.create().texOffs(12, 99).addBox(0.0F, -13.0F, 0.0F, 2.0F, 13.0F, 1.0F),
                PartPose.offset(-1.0F, 10.0F, -4.0F));
        root.addOrReplaceChild("FinRidge4",
                CubeListBuilder.create().texOffs(6, 114).addBox(0.0F, -13.0F, 0.0F, 2.0F, 13.0F, 1.0F),
                PartPose.offsetAndRotation(-1.0F, 10.0F, -3.5F, 0.9666439F, 0.0F, 0.0F));
        root.addOrReplaceChild("FinRidge3",
                CubeListBuilder.create().texOffs(12, 115).addBox(0.0F, -13.0F, 0.0F, 2.0F, 13.0F, 1.0F),
                PartPose.offsetAndRotation(-1.0F, 10.0F, -4.0F, 0.5205006F, 0.0F, 0.0F));
        root.addOrReplaceChild("FinRidge2",
                CubeListBuilder.create().texOffs(0, 84).addBox(0.0F, -13.0F, 0.0F, 2.0F, 13.0F, 1.0F),
                PartPose.offsetAndRotation(-1.0F, 10.0F, -4.5F, -1.375609F, 0.0F, 0.0F));
        root.addOrReplaceChild("FinRidge1",
                CubeListBuilder.create().texOffs(0, 114).addBox(0.0F, -13.0F, 0.0F, 2.0F, 13.0F, 1.0F),
                PartPose.offsetAndRotation(-1.0F, 10.0F, -3.5F, 1.412787F, 0.0F, 0.0F));
        root.addOrReplaceChild("Fin10",
                CubeListBuilder.create().texOffs(0, 58).addBox(0.0F, -13.0F, -2.0F, 0.0F, 11.0F, 6.0F),
                PartPose.offsetAndRotation(0.0F, 10.5F, -5.0F, 0.2094395F, 0.0F, 0.0F));
        root.addOrReplaceChild("Fin9",
                CubeListBuilder.create().texOffs(7, 84).addBox(0.0F, -11.0F, 0.0F, 0.0F, 11.0F, 3.0F),
                PartPose.offsetAndRotation(0.0F, 10.0F, -5.0F, 1.570796F, 0.0F, 0.0F));
        root.addOrReplaceChild("Fin8",
                CubeListBuilder.create().texOffs(12, 34).addBox(0.0F, -7.0F, -4.0F, 0.0F, 7.0F, 4.0F),
                PartPose.offsetAndRotation(0.0F, 10.0F, 1.0F, -1.570796F, 0.0F, 0.0F));
        root.addOrReplaceChild("Fin7",
                CubeListBuilder.create().texOffs(12, 46).addBox(0.0F, -8.0F, -4.0F, 0.0F, 8.0F, 4.0F),
                PartPose.offsetAndRotation(0.0F, 10.0F, 1.0F, -1.033256F, 0.0F, 0.0F));
        root.addOrReplaceChild("Fin6",
                CubeListBuilder.create().texOffs(0, 31).addBox(0.0F, -10.0F, -4.0F, 0.0F, 10.0F, 4.0F),
                PartPose.offsetAndRotation(0.0F, 10.0F, -1.0F, -0.7267386F, 0.0F, 0.0F));
        root.addOrReplaceChild("Fin5",
                CubeListBuilder.create().texOffs(30, 59).addBox(0.0F, -12.0F, -5.0F, 0.0F, 11.0F, 6.0F),
                PartPose.offsetAndRotation(0.0F, 10.0F, -2.0F, -0.3003206F, 0.0F, 0.0F));
        root.addOrReplaceChild("Fin3",
                CubeListBuilder.create().texOffs(14, 60).addBox(0.0F, -12.0F, -3.0F, 0.0F, 12.0F, 6.0F),
                PartPose.offsetAndRotation(0.0F, 10.0F, -4.0F, 0.7073231F, 0.0F, 0.0F));
        root.addOrReplaceChild("Fin2",
                CubeListBuilder.create().texOffs(14, 79).addBox(0.0F, -12.0F, -4.0F, 0.0F, 11.0F, 6.0F),
                PartPose.offsetAndRotation(0.0F, 10.0F, -4.0F, 1.048747F, 0.0F, 0.0F));
        root.addOrReplaceChild("Tooth11",
                CubeListBuilder.create().texOffs(24, 110).addBox(3.0F, 3.0F, -8.0F, 1.0F, 1.0F, 1.0F),
                PartPose.offset(0.0F, 12.0F, -18.0F));
        root.addOrReplaceChild("Tooth10",
                CubeListBuilder.create().texOffs(24, 106).addBox(3.0F, 3.0F, -10.0F, 1.0F, 1.0F, 1.0F),
                PartPose.offset(0.0F, 12.0F, -18.0F));
        root.addOrReplaceChild("Tooth8",
                CubeListBuilder.create().texOffs(28, 95).addBox(3.0F, 3.0F, -14.0F, 1.0F, 1.0F, 1.0F),
                PartPose.offset(0.0F, 12.0F, -18.0F));
        root.addOrReplaceChild("Tooth7",
                CubeListBuilder.create().texOffs(70, 106).addBox(-4.0F, 3.0F, -10.0F, 1.0F, 1.0F, 1.0F),
                PartPose.offset(0.0F, 12.0F, -18.0F));
        root.addOrReplaceChild("Tooth6",
                CubeListBuilder.create().texOffs(70, 102).addBox(-4.0F, 3.0F, -12.0F, 1.0F, 1.0F, 1.0F),
                PartPose.offset(0.0F, 12.0F, -18.0F));
        root.addOrReplaceChild("Tooth5",
                CubeListBuilder.create().texOffs(66, 95).addBox(-4.0F, 3.0F, -14.0F, 1.0F, 1.0F, 1.0F),
                PartPose.offset(0.0F, 12.0F, -18.0F));
        root.addOrReplaceChild("Tooth4",
                CubeListBuilder.create().texOffs(60, 95).addBox(1.0F, 3.0F, -14.0F, 1.0F, 1.0F, 1.0F),
                PartPose.offset(0.0F, 12.0F, -18.0F));
        root.addOrReplaceChild("Tooth3",
                CubeListBuilder.create().texOffs(34, 95).addBox(-2.0F, 3.0F, -14.0F, 1.0F, 1.0F, 1.0F),
                PartPose.offset(0.0F, 12.0F, -18.0F));
        root.addOrReplaceChild("Tooth2",
                CubeListBuilder.create().texOffs(70, 110).addBox(-4.0F, 3.0F, -8.0F, 1.0F, 1.0F, 1.0F),
                PartPose.offset(0.0F, 12.0F, -18.0F));
        root.addOrReplaceChild("CenterRightNose",
                CubeListBuilder.create().texOffs(40, 88).addBox(-4.0F, 0.0F, -14.0F, 1.0F, 1.0F, 1.0F),
                PartPose.offset(0.0F, 12.0F, -18.0F));
        root.addOrReplaceChild("CenterLeftNose",
                CubeListBuilder.create().texOffs(54, 88).addBox(3.0F, 0.0F, -14.0F, 1.0F, 1.0F, 1.0F),
                PartPose.offset(0.0F, 12.0F, -18.0F));
        root.addOrReplaceChild("Tooth1",
                CubeListBuilder.create().texOffs(24, 102).addBox(3.0F, 3.0F, -12.0F, 1.0F, 1.0F, 1.0F),
                PartPose.offset(0.0F, 12.0F, -18.0F));
        root.addOrReplaceChild("BottomNose",
                CubeListBuilder.create().texOffs(40, 90).addBox(-4.0F, 1.0F, -14.0F, 8.0F, 2.0F, 1.0F),
                PartPose.offset(0.0F, 12.0F, -18.0F));
        root.addOrReplaceChild("TopNose",
                CubeListBuilder.create().texOffs(40, 84).addBox(-4.0F, -3.0F, -14.0F, 8.0F, 3.0F, 1.0F),
                PartPose.offset(0.0F, 12.0F, -18.0F));
        root.addOrReplaceChild("JawTop",
                CubeListBuilder.create().texOffs(28, 97).addBox(-4.0F, -3.0F, -13.0F, 8.0F, 6.0F, 13.0F),
                PartPose.offset(0.0F, 12.0F, -18.0F));
        root.addOrReplaceChild("CenterMiddleNose",
                CubeListBuilder.create().texOffs(46, 88).addBox(-1.0F, 0.0F, -14.0F, 2.0F, 1.0F, 1.0F),
                PartPose.offset(0.0F, 12.0F, -18.0F));
        root.addOrReplaceChild("RightEye",
                CubeListBuilder.create().texOffs(116, 10).addBox(-2.0F, -4.0F, -4.0F, 2.0F, 2.0F, 1.0F),
                PartPose.offsetAndRotation(0.0F, 12.0F, -18.0F, 0.0F, (float) (Math.PI / 4), 0.3490659F));
        root.addOrReplaceChild("LeftEye",
                CubeListBuilder.create().texOffs(94, 10).addBox(0.0F, -4.0F, -4.0F, 2.0F, 2.0F, 1.0F),
                PartPose.offsetAndRotation(0.0F, 12.0F, -18.0F, 0.0F, (float) (-Math.PI / 4), -0.3490659F));
        root.addOrReplaceChild("Tooth16",
                CubeListBuilder.create().texOffs(24, 97).addBox(3.0F, -1.0F, -10.0F, 1.0F, 1.0F, 1.0F),
                PartPose.offsetAndRotation(0.0F, 14.0F, -19.0F, (float) (Math.PI / 6), 0.0F, 0.0F));
        root.addOrReplaceChild("Tooth15",
                CubeListBuilder.create().texOffs(70, 97).addBox(-4.0F, -1.0F, -10.0F, 1.0F, 1.0F, 1.0F),
                PartPose.offsetAndRotation(0.0F, 14.0F, -19.0F, (float) (Math.PI / 6), 0.0F, 0.0F));
        root.addOrReplaceChild("Tooth14",
                CubeListBuilder.create().texOffs(42, 95).addBox(-2.0F, -1.0F, -10.0F, 1.0F, 1.0F, 1.0F),
                PartPose.offsetAndRotation(0.0F, 14.0F, -19.0F, (float) (Math.PI / 6), 0.0F, 0.0F));
        root.addOrReplaceChild("Tooth13",
                CubeListBuilder.create().texOffs(52, 95).addBox(1.0F, -1.0F, -10.0F, 1.0F, 1.0F, 1.0F),
                PartPose.offsetAndRotation(0.0F, 14.0F, -19.0F, (float) (Math.PI / 6), 0.0F, 0.0F));
        root.addOrReplaceChild("Tooth12",
                CubeListBuilder.create().texOffs(24, 114).addBox(3.0F, -1.0F, -7.0F, 1.0F, 1.0F, 1.0F),
                PartPose.offsetAndRotation(0.0F, 14.0F, -19.0F, (float) (Math.PI / 6), 0.0F, 0.0F));
        root.addOrReplaceChild("Tooth9",
                CubeListBuilder.create().texOffs(70, 114).addBox(-4.0F, -1.0F, -7.0F, 1.0F, 1.0F, 1.0F),
                PartPose.offsetAndRotation(0.0F, 14.0F, -19.0F, (float) (Math.PI / 6), 0.0F, 0.0F));
        root.addOrReplaceChild("BottomJaw",
                CubeListBuilder.create().texOffs(31, 116).addBox(-4.0F, 0.0F, -10.0F, 8.0F, 2.0F, 10.0F),
                PartPose.offsetAndRotation(0.0F, 14.0F, -19.0F, (float) (Math.PI / 6), 0.0F, 0.0F));
        root.addOrReplaceChild("Hat1",
                CubeListBuilder.create().texOffs(30, 40).addBox(-2.0F, -4.0F, -6.0F, 4.0F, 1.0F, 6.0F),
                PartPose.offset(0.0F, 12.0F, -18.0F));
        root.addOrReplaceChild("Hat2",
                CubeListBuilder.create().texOffs(30, 40).addBox(-1.5F, -6.0F, -4.0F, 3.0F, 2.0F, 4.0F),
                PartPose.offset(0.0F, 12.0F, -18.0F));

        return LayerDefinition.create(mesh, 128, 128);
    }

    @Override
    public ModelPart root() {
        return this.root;
    }

    /**
     * Gold {@code func_78088_a} full anim (empty {@code func_78087_a}):
     * walk legs + toes; jaw chomp when attacking; serpentine tail chain with cos/sin pivot follow;
     * neck/head yaw from netHeadYaw; hat visibility from cannon-fodder activation.
     */
    @Override
    public void setupAnim(
            Lizard entity,
            float limbSwing,
            float limbSwingAmount,
            float ageInTicks,
            float netHeadYaw,
            float headPitch) {
        float f1 = limbSwingAmount;
        float f2 = ageInTicks;
        float f3 = netHeadYaw;
        float newangle;

        // --- walk cycle (gold f1 > 0.1) ---
        if (f1 > 0.1F) {
            newangle = Mth.cos(f2 * 1.0F * this.wingspeed) * (float) Math.PI * 0.25F * f1;
        } else {
            newangle = 0.0F;
        }

        this.TopFrontLeftLeg.yRot = newangle;
        this.BottomFrontLeftLeg.xRot = newangle;
        this.FrontLeftFoot.yRot = newangle;
        this.Toe8.yRot = newangle;
        this.Toe1.yRot = newangle;

        this.TopFrontRightLeg.yRot = newangle;
        this.BottomFrontRightLeg.xRot = -newangle;
        this.FrontRightFoot.yRot = newangle;
        this.Toe3.yRot = newangle;
        this.Toe2.yRot = newangle;

        this.TopBackLeftLeg.yRot = -newangle;
        this.BottomBackLeftLeg.xRot = -newangle;
        this.BackLeftFoot.yRot = -newangle;
        this.Toe7.yRot = -newangle;
        this.Toe6.yRot = -newangle;

        this.TopBackRightLeg.yRot = -newangle;
        this.BottomBackRightLeg.xRot = newangle;
        this.BackRightFoot.yRot = -newangle;
        this.Toe4.yRot = -newangle;
        this.Toe5.yRot = -newangle;

        // --- jaw chomp ---
        if (entity.getAttacking() != 0) {
            this.BottomJaw.xRot = 0.52F + Mth.cos(f2 * 0.45F) * 0.35F;
        } else {
            this.BottomJaw.xRot = 0.25F;
        }
        this.Tooth9.xRot = this.BottomJaw.xRot;
        this.Tooth15.xRot = this.BottomJaw.xRot;
        this.Tooth14.xRot = this.BottomJaw.xRot;
        this.Tooth13.xRot = this.BottomJaw.xRot;
        this.Tooth16.xRot = this.BottomJaw.xRot;
        this.Tooth12.xRot = this.BottomJaw.xRot;

        // --- serpentine tail ---
        newangle = Mth.cos(f2 * 0.25F * this.wingspeed) * (float) Math.PI * 0.05F;
        if (entity.getAttacking() != 0) {
            newangle = Mth.cos(f2 * 1.25F * this.wingspeed) * (float) Math.PI * 0.35F;
        }

        this.TailBase1.yRot = newangle * 0.25F;
        this.Tail2.z = this.TailBase1.z + (float) Math.cos(this.TailBase1.yRot) * 12.0F;
        this.Tail2.x = this.TailBase1.x + (float) Math.sin(this.TailBase1.yRot) * 12.0F;
        this.Tail2.yRot = newangle * 0.5F;
        this.Tail3.z = this.Tail2.z + (float) Math.cos(this.Tail2.yRot) * 9.0F;
        this.Tail3.x = this.Tail2.x + (float) Math.sin(this.Tail2.yRot) * 9.0F;
        this.Tail3.yRot = newangle * 0.75F;
        this.Tail4.z = this.Tail3.z + (float) Math.cos(this.Tail3.yRot) * 7.0F;
        this.Tail4.x = this.Tail3.x + (float) Math.sin(this.Tail3.yRot) * 7.0F;
        this.Tail4.yRot = newangle * 1.0F;
        this.TailTip.z = this.Tail4.z + (float) Math.cos(this.Tail4.yRot) * 7.0F;
        this.TailTip.x = this.Tail4.x + (float) Math.sin(this.Tail4.yRot) * 7.0F;
        this.TailTip.yRot = newangle * 1.25F;

        // --- head / neck follow look yaw (gold f3 degrees → radians) ---
        this.Neck.yRot = (float) Math.toRadians(f3) * 0.25F;
        this.JawTop.z = this.Neck.z - (float) Math.cos(this.Neck.yRot) * 2.0F;
        this.JawTop.x = this.Neck.x - (float) Math.sin(this.Neck.yRot) * 2.0F;
        this.JawTop.yRot = (float) Math.toRadians(f3) * 0.5F;

        this.TopNose.z = this.JawTop.z;
        this.TopNose.x = this.JawTop.x;
        this.TopNose.yRot = this.JawTop.yRot;
        this.BottomNose.z = this.JawTop.z;
        this.BottomNose.x = this.JawTop.x;
        this.BottomNose.yRot = this.JawTop.yRot;
        this.CenterRightNose.z = this.JawTop.z;
        this.CenterRightNose.x = this.JawTop.x;
        this.CenterRightNose.yRot = this.JawTop.yRot;
        this.CenterMiddleNose.z = this.JawTop.z;
        this.CenterMiddleNose.x = this.JawTop.x;
        this.CenterMiddleNose.yRot = this.JawTop.yRot;
        this.CenterLeftNose.z = this.JawTop.z;
        this.CenterLeftNose.x = this.JawTop.x;
        this.CenterLeftNose.yRot = this.JawTop.yRot;
        this.RightEye.z = this.JawTop.z;
        this.RightEye.x = this.JawTop.x;
        this.RightEye.yRot = this.JawTop.yRot + 0.78F;
        this.LeftEye.z = this.JawTop.z;
        this.LeftEye.x = this.JawTop.x;
        this.LeftEye.yRot = this.JawTop.yRot - 0.78F;

        this.Tooth11.z = this.JawTop.z;
        this.Tooth11.x = this.JawTop.x;
        this.Tooth11.yRot = this.JawTop.yRot;
        this.Tooth10.z = this.JawTop.z;
        this.Tooth10.x = this.JawTop.x;
        this.Tooth10.yRot = this.JawTop.yRot;
        this.Tooth1.z = this.JawTop.z;
        this.Tooth1.x = this.JawTop.x;
        this.Tooth1.yRot = this.JawTop.yRot;
        this.Tooth8.z = this.JawTop.z;
        this.Tooth8.x = this.JawTop.x;
        this.Tooth8.yRot = this.JawTop.yRot;
        this.Tooth4.z = this.JawTop.z;
        this.Tooth4.x = this.JawTop.x;
        this.Tooth4.yRot = this.JawTop.yRot;
        this.Tooth3.z = this.JawTop.z;
        this.Tooth3.x = this.JawTop.x;
        this.Tooth3.yRot = this.JawTop.yRot;
        this.Tooth5.z = this.JawTop.z;
        this.Tooth5.x = this.JawTop.x;
        this.Tooth5.yRot = this.JawTop.yRot;
        this.Tooth6.z = this.JawTop.z;
        this.Tooth6.x = this.JawTop.x;
        this.Tooth6.yRot = this.JawTop.yRot;
        this.Tooth7.z = this.JawTop.z;
        this.Tooth7.x = this.JawTop.x;
        this.Tooth7.yRot = this.JawTop.yRot;
        this.Tooth2.z = this.JawTop.z;
        this.Tooth2.x = this.JawTop.x;
        this.Tooth2.yRot = this.JawTop.yRot;

        this.Hat1.z = this.JawTop.z;
        this.Hat1.x = this.JawTop.x;
        this.Hat1.yRot = this.JawTop.yRot;
        this.Hat2.z = this.JawTop.z;
        this.Hat2.x = this.JawTop.x;
        this.Hat2.yRot = this.JawTop.yRot;

        this.BottomJaw.z = this.Neck.z - (float) Math.cos(this.Neck.yRot) * 3.0F;
        this.BottomJaw.x = this.Neck.x - (float) Math.sin(this.Neck.yRot) * 3.0F;
        this.BottomJaw.yRot = (float) Math.toRadians(f3) * 0.5F;
        this.Tooth9.z = this.BottomJaw.z;
        this.Tooth9.x = this.BottomJaw.x;
        this.Tooth9.yRot = this.BottomJaw.yRot;
        this.Tooth16.z = this.BottomJaw.z;
        this.Tooth16.x = this.BottomJaw.x;
        this.Tooth16.yRot = this.BottomJaw.yRot;
        this.Tooth15.z = this.BottomJaw.z;
        this.Tooth15.x = this.BottomJaw.x;
        this.Tooth15.yRot = this.BottomJaw.yRot;
        this.Tooth14.z = this.BottomJaw.z;
        this.Tooth14.x = this.BottomJaw.x;
        this.Tooth14.yRot = this.BottomJaw.yRot;
        this.Tooth13.z = this.BottomJaw.z;
        this.Tooth13.x = this.BottomJaw.x;
        this.Tooth13.yRot = this.BottomJaw.yRot;
        this.Tooth12.z = this.BottomJaw.z;
        this.Tooth12.x = this.BottomJaw.x;
        this.Tooth12.yRot = this.BottomJaw.yRot;

        // --- hat mesh (gold EntityCannonFodder activation; deferred → usually hidden) ---
        int activated = entity.get_is_activated();
        this.Hat1.visible = activated != 0;
        this.Hat2.visible = activated > 1;

        // keep unused static parts referenced so IDE/tree is complete (fins/body static)
        // BodyBack / BodyFront / BodyCenter / Fin* hold base PartPose only
    }
}
