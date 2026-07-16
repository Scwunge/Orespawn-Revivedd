package danger.orespawn.client.model;

import danger.orespawn.entity.ThePrinceAdult;
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
 * Port of gold {@code ModelThePrinceAdult} (1.7.10 ModelBase, tex 2048Ã—2048) â†’ 1.21 HierarchicalModel.
 * Cube sizes/UVs/pivots/base rotations 1:1. FULL anims from gold {@code func_78088_a} + move*Head.
 * Wingspeed default 0.65 matches ClientProxy. Local LAYER_LOCATION.
 */
@OnlyIn(Dist.CLIENT)
public class ModelThePrinceAdult extends HierarchicalModel<ThePrinceAdult> {
    /** Model layer location. */
    public static final ModelLayerLocation LAYER_LOCATION =
            new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "the_prince_adult"), "main");

    private final float wingspeed;
    private final ModelPart root;
    private final ModelPart LCClaw1;
    private final ModelPart LThigh;
    private final ModelPart LUpperLeg;
    private final ModelPart TailTip;
    private final ModelPart Tail1;
    private final ModelPart Tail2;
    private final ModelPart Tail3;
    private final ModelPart Tail4;
    private final ModelPart Tail5;
    private final ModelPart Tail6;
    private final ModelPart Tail7;
    private final ModelPart Body1;
    private final ModelPart Chest;
    private final ModelPart NeckC1;
    private final ModelPart LLowerLeg;
    private final ModelPart LFoot;
    private final ModelPart LLClaw1;
    private final ModelPart LRClaw1;
    private final ModelPart LCClaw2;
    private final ModelPart LLClaw2;
    private final ModelPart TailSpike;
    private final ModelPart LRClaw2;
    private final ModelPart LClawRear;
    private final ModelPart NeckL1;
    private final ModelPart NeckR1;
    private final ModelPart RThigh;
    private final ModelPart RUpperLeg;
    private final ModelPart RLowerLeg;
    private final ModelPart RFoot;
    private final ModelPart RClawRear;
    private final ModelPart RLClaw1;
    private final ModelPart RCClaw1;
    private final ModelPart RRClaw1;
    private final ModelPart RLClaw2;
    private final ModelPart RCClaw2;
    private final ModelPart RRClaw2;
    private final ModelPart NeckL2;
    private final ModelPart NeckC2;
    private final ModelPart NeckR2;
    private final ModelPart NeckL3;
    private final ModelPart NeckC3;
    private final ModelPart NeckR3;
    private final ModelPart NeckL4;
    private final ModelPart LHead1;
    private final ModelPart LHead2;
    private final ModelPart LHead3;
    private final ModelPart LJaw1;
    private final ModelPart LJaw2;
    private final ModelPart LJaw3;
    private final ModelPart LTooth1;
    private final ModelPart LTooth2;
    private final ModelPart LTooth3;
    private final ModelPart LTooth4;
    private final ModelPart NeckC4;
    private final ModelPart NeckR4;
    private final ModelPart CHead1;
    private final ModelPart RHead1;
    private final ModelPart CHead2;
    private final ModelPart RHead2;
    private final ModelPart CHead3;
    private final ModelPart RHead3;
    private final ModelPart CJaw1;
    private final ModelPart CJaw2;
    private final ModelPart CJaw3;
    private final ModelPart RJaw1;
    private final ModelPart RJaw2;
    private final ModelPart RJaw3;
    private final ModelPart CTooth3;
    private final ModelPart CTooth4;
    private final ModelPart CTooth1;
    private final ModelPart CTooth2;
    private final ModelPart RTooth3;
    private final ModelPart RTooth4;
    private final ModelPart RTooth1;
    private final ModelPart RTooth2;
    private final ModelPart LLEye;
    private final ModelPart LREye;
    private final ModelPart CLEye;
    private final ModelPart CREye;
    private final ModelPart RLEye;
    private final ModelPart RREye;
    private final ModelPart LHeadMane;
    private final ModelPart CHeadMane;
    private final ModelPart RHeadMane;
    private final ModelPart LLNoseSpike;
    private final ModelPart LRNoseSpike;
    private final ModelPart CLNoseSpike;
    private final ModelPart CRNoseSpike;
    private final ModelPart RLNoseSpike;
    private final ModelPart RRNoseSpike;
    private final ModelPart Back1;
    private final ModelPart Back2;
    private final ModelPart Lwing1;
    private final ModelPart Lwing2;
    private final ModelPart Lwing3;
    private final ModelPart Lwing4;
    private final ModelPart Lwing5;
    private final ModelPart Lwing6;
    private final ModelPart Lwing7;
    private final ModelPart Lwing9;
    private final ModelPart Lwing8;
    private final ModelPart Lwing10;
    private final ModelPart Rwing1;
    private final ModelPart Rwing2;
    private final ModelPart Rwing3;
    private final ModelPart Rwing4;
    private final ModelPart Rwing5;
    private final ModelPart Rwing6;
    private final ModelPart Rwing7;
    private final ModelPart Rwing8;
    private final ModelPart Rwing9;
    private final ModelPart Rwing10;
    private final ModelPart TailTip2;
    private final ModelPart Ridge1;
    private final ModelPart Ridge2;
    private final ModelPart Ridge3;
    private final ModelPart Ridge4;
    private final ModelPart Ridge5;
    private final ModelPart Ridge6;

    public ModelThePrinceAdult(ModelPart root) {
        this(root, 0.65F);
    }

    public ModelThePrinceAdult(ModelPart root, float wingspeed) {
        this.root = root;
        this.wingspeed = wingspeed;
        this.LCClaw1 = root.getChild("LCClaw1");
        this.LThigh = root.getChild("LThigh");
        this.LUpperLeg = root.getChild("LUpperLeg");
        this.TailTip = root.getChild("TailTip");
        this.Tail1 = root.getChild("Tail1");
        this.Tail2 = root.getChild("Tail2");
        this.Tail3 = root.getChild("Tail3");
        this.Tail4 = root.getChild("Tail4");
        this.Tail5 = root.getChild("Tail5");
        this.Tail6 = root.getChild("Tail6");
        this.Tail7 = root.getChild("Tail7");
        this.Body1 = root.getChild("Body1");
        this.Chest = root.getChild("Chest");
        this.NeckC1 = root.getChild("NeckC1");
        this.LLowerLeg = root.getChild("LLowerLeg");
        this.LFoot = root.getChild("LFoot");
        this.LLClaw1 = root.getChild("LLClaw1");
        this.LRClaw1 = root.getChild("LRClaw1");
        this.LCClaw2 = root.getChild("LCClaw2");
        this.LLClaw2 = root.getChild("LLClaw2");
        this.TailSpike = root.getChild("TailSpike");
        this.LRClaw2 = root.getChild("LRClaw2");
        this.LClawRear = root.getChild("LClawRear");
        this.NeckL1 = root.getChild("NeckL1");
        this.NeckR1 = root.getChild("NeckR1");
        this.RThigh = root.getChild("RThigh");
        this.RUpperLeg = root.getChild("RUpperLeg");
        this.RLowerLeg = root.getChild("RLowerLeg");
        this.RFoot = root.getChild("RFoot");
        this.RClawRear = root.getChild("RClawRear");
        this.RLClaw1 = root.getChild("RLClaw1");
        this.RCClaw1 = root.getChild("RCClaw1");
        this.RRClaw1 = root.getChild("RRClaw1");
        this.RLClaw2 = root.getChild("RLClaw2");
        this.RCClaw2 = root.getChild("RCClaw2");
        this.RRClaw2 = root.getChild("RRClaw2");
        this.NeckL2 = root.getChild("NeckL2");
        this.NeckC2 = root.getChild("NeckC2");
        this.NeckR2 = root.getChild("NeckR2");
        this.NeckL3 = root.getChild("NeckL3");
        this.NeckC3 = root.getChild("NeckC3");
        this.NeckR3 = root.getChild("NeckR3");
        this.NeckL4 = root.getChild("NeckL4");
        this.LHead1 = root.getChild("LHead1");
        this.LHead2 = root.getChild("LHead2");
        this.LHead3 = root.getChild("LHead3");
        this.LJaw1 = root.getChild("LJaw1");
        this.LJaw2 = root.getChild("LJaw2");
        this.LJaw3 = root.getChild("LJaw3");
        this.LTooth1 = root.getChild("LTooth1");
        this.LTooth2 = root.getChild("LTooth2");
        this.LTooth3 = root.getChild("LTooth3");
        this.LTooth4 = root.getChild("LTooth4");
        this.NeckC4 = root.getChild("NeckC4");
        this.NeckR4 = root.getChild("NeckR4");
        this.CHead1 = root.getChild("CHead1");
        this.RHead1 = root.getChild("RHead1");
        this.CHead2 = root.getChild("CHead2");
        this.RHead2 = root.getChild("RHead2");
        this.CHead3 = root.getChild("CHead3");
        this.RHead3 = root.getChild("RHead3");
        this.CJaw1 = root.getChild("CJaw1");
        this.CJaw2 = root.getChild("CJaw2");
        this.CJaw3 = root.getChild("CJaw3");
        this.RJaw1 = root.getChild("RJaw1");
        this.RJaw2 = root.getChild("RJaw2");
        this.RJaw3 = root.getChild("RJaw3");
        this.CTooth3 = root.getChild("CTooth3");
        this.CTooth4 = root.getChild("CTooth4");
        this.CTooth1 = root.getChild("CTooth1");
        this.CTooth2 = root.getChild("CTooth2");
        this.RTooth3 = root.getChild("RTooth3");
        this.RTooth4 = root.getChild("RTooth4");
        this.RTooth1 = root.getChild("RTooth1");
        this.RTooth2 = root.getChild("RTooth2");
        this.LLEye = root.getChild("LLEye");
        this.LREye = root.getChild("LREye");
        this.CLEye = root.getChild("CLEye");
        this.CREye = root.getChild("CREye");
        this.RLEye = root.getChild("RLEye");
        this.RREye = root.getChild("RREye");
        this.LHeadMane = root.getChild("LHeadMane");
        this.CHeadMane = root.getChild("CHeadMane");
        this.RHeadMane = root.getChild("RHeadMane");
        this.LLNoseSpike = root.getChild("LLNoseSpike");
        this.LRNoseSpike = root.getChild("LRNoseSpike");
        this.CLNoseSpike = root.getChild("CLNoseSpike");
        this.CRNoseSpike = root.getChild("CRNoseSpike");
        this.RLNoseSpike = root.getChild("RLNoseSpike");
        this.RRNoseSpike = root.getChild("RRNoseSpike");
        this.Back1 = root.getChild("Back1");
        this.Back2 = root.getChild("Back2");
        this.Lwing1 = root.getChild("Lwing1");
        this.Lwing2 = root.getChild("Lwing2");
        this.Lwing3 = root.getChild("Lwing3");
        this.Lwing4 = root.getChild("Lwing4");
        this.Lwing5 = root.getChild("Lwing5");
        this.Lwing6 = root.getChild("Lwing6");
        this.Lwing7 = root.getChild("Lwing7");
        this.Lwing9 = root.getChild("Lwing9");
        this.Lwing8 = root.getChild("Lwing8");
        this.Lwing10 = root.getChild("Lwing10");
        this.Rwing1 = root.getChild("Rwing1");
        this.Rwing2 = root.getChild("Rwing2");
        this.Rwing3 = root.getChild("Rwing3");
        this.Rwing4 = root.getChild("Rwing4");
        this.Rwing5 = root.getChild("Rwing5");
        this.Rwing6 = root.getChild("Rwing6");
        this.Rwing7 = root.getChild("Rwing7");
        this.Rwing8 = root.getChild("Rwing8");
        this.Rwing9 = root.getChild("Rwing9");
        this.Rwing10 = root.getChild("Rwing10");
        this.TailTip2 = root.getChild("TailTip2");
        this.Ridge1 = root.getChild("Ridge1");
        this.Ridge2 = root.getChild("Ridge2");
        this.Ridge3 = root.getChild("Ridge3");
        this.Ridge4 = root.getChild("Ridge4");
        this.Ridge5 = root.getChild("Ridge5");
        this.Ridge6 = root.getChild("Ridge6");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        root.addOrReplaceChild("LCClaw1",
                CubeListBuilder.create().texOffs(0, 1450)
                        .addBox(-5.0F, -5.0F, -20.0F, 10.0F, 10.0F, 20.0F),
                PartPose.offsetAndRotation(52.0F, -2.0F, -7.0F, 0.3839724F, 0.0F, 0.0F));
        root.addOrReplaceChild("LThigh",
                CubeListBuilder.create().texOffs(0, 1050)
                        .addBox(0.0F, -14.0F, -21.0F, 25.0F, 34.0F, 42.0F),
                PartPose.offsetAndRotation(40.0F, -91.0F, 2.0F, (float) (Math.PI / 4.0), 0.0F, 0.0F));
        root.addOrReplaceChild("LUpperLeg",
                CubeListBuilder.create().texOffs(0, 1137)
                        .addBox(0.0F, 12.0F, -16.0F, 24.0F, 52.0F, 24.0F),
                PartPose.offsetAndRotation(40.0F, -91.0F, 2.0F, (float) (Math.PI / 4.0), 0.0F, 0.0F));
        root.addOrReplaceChild("TailTip",
                CubeListBuilder.create().texOffs(500, 102)
                        .addBox(-51.0F, -3.0F, 0.0F, 100.0F, 6.0F, 36.0F),
                PartPose.offset(0.0F, -86.0F, 308.0F));
        root.addOrReplaceChild("Tail1",
                CubeListBuilder.create().texOffs(500, 614)
                        .addBox(-32.0F, 0.0F, 0.0F, 64.0F, 52.0F, 58.0F),
                PartPose.offset(0.0F, -114.0F, 29.0F));
        root.addOrReplaceChild("Tail2",
                CubeListBuilder.create().texOffs(500, 520)
                        .addBox(-25.0F, -19.0F, -3.0F, 50.0F, 42.0F, 46.0F),
                PartPose.offset(0.0F, -90.0F, 79.0F));
        root.addOrReplaceChild("Tail3",
                CubeListBuilder.create().texOffs(500, 432)
                        .addBox(-20.0F, -18.0F, 0.0F, 40.0F, 36.0F, 45.0F),
                PartPose.offset(0.0F, -88.0F, 117.0F));
        root.addOrReplaceChild("Tail4",
                CubeListBuilder.create().texOffs(500, 355)
                        .addBox(-16.0F, -15.0F, 0.0F, 32.0F, 30.0F, 38.0F),
                PartPose.offset(0.0F, -87.0F, 156.0F));
        root.addOrReplaceChild("Tail5",
                CubeListBuilder.create().texOffs(500, 286)
                        .addBox(-13.0F, -12.0F, 0.0F, 26.0F, 24.0F, 38.0F),
                PartPose.offset(0.0F, -87.0F, 189.0F));
        root.addOrReplaceChild("Tail6",
                CubeListBuilder.create().texOffs(500, 218)
                        .addBox(-10.0F, -9.0F, 0.0F, 20.0F, 18.0F, 44.0F),
                PartPose.offset(0.0F, -87.0F, 222.0F));
        root.addOrReplaceChild("Tail7",
                CubeListBuilder.create().texOffs(500, 150)
                        .addBox(-8.0F, -6.0F, -7.0F, 16.0F, 12.0F, 49.0F),
                PartPose.offset(0.0F, -87.0F, 268.0F));
        root.addOrReplaceChild("Body1",
                CubeListBuilder.create().texOffs(500, 732)
                        .addBox(-40.0F, -32.0F, -36.0F, 80.0F, 64.0F, 72.0F),
                PartPose.offset(0.0F, -89.0F, 1.0F));
        root.addOrReplaceChild("Chest",
                CubeListBuilder.create().texOffs(0, 73)
                        .addBox(-41.0F, -32.0F, -60.0F, 82.0F, 44.0F, 60.0F),
                PartPose.offset(0.0F, -91.0F, -22.0F));
        root.addOrReplaceChild("NeckC1",
                CubeListBuilder.create().texOffs(700, 1100)
                        .addBox(-12.0F, -12.0F, -24.0F, 24.0F, 24.0F, 24.0F),
                PartPose.offsetAndRotation(0.0F, -113.0F, -76.0F, -0.1047198F, 0.0F, 0.0F));
        root.addOrReplaceChild("LLowerLeg",
                CubeListBuilder.create().texOffs(0, 1220)
                        .addBox(-11.0F, 0.0F, -10.0F, 22.0F, 59.0F, 20.0F),
                PartPose.offsetAndRotation(52.0F, -52.0F, 36.0F, -0.6283185F, 0.0F, 0.0F));
        root.addOrReplaceChild("LFoot",
                CubeListBuilder.create().texOffs(0, 1307)
                        .addBox(-18.0F, 59.0F, -10.0F, 36.0F, 12.0F, 20.0F),
                PartPose.offsetAndRotation(52.0F, -52.0F, 36.0F, -0.6283185F, 0.0F, 0.0F));
        root.addOrReplaceChild("LLClaw1",
                CubeListBuilder.create().texOffs(0, 1400)
                        .addBox(-5.0F, -5.0F, -20.0F, 10.0F, 10.0F, 20.0F),
                PartPose.offsetAndRotation(67.0F, -2.0F, -7.0F, 0.3839724F, 0.0F, 0.0F));
        root.addOrReplaceChild("LRClaw1",
                CubeListBuilder.create().texOffs(0, 1500)
                        .addBox(-5.0F, -5.0F, -20.0F, 10.0F, 10.0F, 20.0F),
                PartPose.offsetAndRotation(37.0F, -2.0F, -7.0F, 0.3839724F, 0.0F, 0.0F));
        root.addOrReplaceChild("LCClaw2",
                CubeListBuilder.create().texOffs(0, 1600)
                        .addBox(-2.0F, -7.0F, -46.0F, 4.0F, 7.0F, 28.0F),
                PartPose.offsetAndRotation(52.0F, -2.0F, -7.0F, 0.6457718F, 0.0F, 0.0F));
        root.addOrReplaceChild("LLClaw2",
                CubeListBuilder.create().texOffs(0, 1550)
                        .addBox(-2.0F, -7.0F, -46.0F, 4.0F, 7.0F, 28.0F),
                PartPose.offsetAndRotation(67.0F, -2.0F, -7.0F, 0.6457718F, 0.0F, 0.0F));
        root.addOrReplaceChild("TailSpike",
                CubeListBuilder.create().texOffs(500, 0)
                        .addBox(-2.0F, -1.0F, -7.0F, 4.0F, 4.0F, 53.0F),
                PartPose.offset(0.0F, -87.0F, 375.0F));
        root.addOrReplaceChild("LRClaw2",
                CubeListBuilder.create().texOffs(0, 1650)
                        .addBox(-2.0F, -7.0F, -46.0F, 4.0F, 7.0F, 28.0F),
                PartPose.offsetAndRotation(37.0F, -2.0F, -7.0F, 0.6457718F, 0.0F, 0.0F));
        root.addOrReplaceChild("LClawRear",
                CubeListBuilder.create().texOffs(0, 1350)
                        .addBox(-2.0F, -3.0F, 0.0F, 4.0F, 9.0F, 28.0F),
                PartPose.offsetAndRotation(52.0F, 4.0F, 4.0F, -0.9250245F, 0.0F, 0.0F));
        root.addOrReplaceChild("NeckL1",
                CubeListBuilder.create().texOffs(500, 1100)
                        .addBox(-12.0F, -12.0F, -24.0F, 24.0F, 24.0F, 24.0F),
                PartPose.offsetAndRotation(30.0F, -113.0F, -76.0F, -0.0523599F, -0.1047198F, 0.0F));
        root.addOrReplaceChild("NeckR1",
                CubeListBuilder.create().texOffs(900, 1100)
                        .addBox(-12.0F, -12.0F, -24.0F, 24.0F, 24.0F, 24.0F),
                PartPose.offsetAndRotation(-30.0F, -113.0F, -76.0F, -0.0523599F, 0.1047198F, 0.0F));
        root.addOrReplaceChild("RThigh",
                CubeListBuilder.create().texOffs(200, 1050)
                        .addBox(0.0F, -14.0F, -21.0F, 25.0F, 34.0F, 42.0F),
                PartPose.offsetAndRotation(-65.0F, -91.0F, 2.0F, (float) (Math.PI / 4.0), 0.0F, 0.0F));
        root.addOrReplaceChild("RUpperLeg",
                CubeListBuilder.create().texOffs(200, 1137)
                        .addBox(0.0F, 12.0F, -16.0F, 24.0F, 52.0F, 24.0F),
                PartPose.offsetAndRotation(-64.0F, -91.0F, 2.0F, (float) (Math.PI / 4.0), 0.0F, 0.0F));
        root.addOrReplaceChild("RLowerLeg",
                CubeListBuilder.create().texOffs(200, 1220)
                        .addBox(-4.0F, 0.0F, -10.0F, 22.0F, 59.0F, 20.0F),
                PartPose.offsetAndRotation(-59.0F, -52.0F, 36.0F, -0.6283185F, 0.0F, 0.0F));
        root.addOrReplaceChild("RFoot",
                CubeListBuilder.create().texOffs(200, 1307)
                        .addBox(-11.0F, 59.0F, -10.0F, 36.0F, 12.0F, 20.0F),
                PartPose.offsetAndRotation(-59.0F, -52.0F, 36.0F, -0.6283185F, 0.0F, 0.0F));
        root.addOrReplaceChild("RClawRear",
                CubeListBuilder.create().texOffs(200, 1350)
                        .addBox(-2.0F, -3.0F, 0.0F, 4.0F, 9.0F, 28.0F),
                PartPose.offsetAndRotation(-52.0F, 4.0F, 4.0F, -0.9250245F, 0.0F, 0.0F));
        root.addOrReplaceChild("RLClaw1",
                CubeListBuilder.create().texOffs(200, 1400)
                        .addBox(-5.0F, -5.0F, -20.0F, 10.0F, 10.0F, 20.0F),
                PartPose.offsetAndRotation(-37.0F, -2.0F, -7.0F, 0.3839724F, 0.0F, 0.0F));
        root.addOrReplaceChild("RCClaw1",
                CubeListBuilder.create().texOffs(200, 1450)
                        .addBox(-5.0F, -5.0F, -20.0F, 10.0F, 10.0F, 20.0F),
                PartPose.offsetAndRotation(-52.0F, -2.0F, -7.0F, 0.3839724F, 0.0F, 0.0F));
        root.addOrReplaceChild("RRClaw1",
                CubeListBuilder.create().texOffs(200, 1500)
                        .addBox(-5.0F, -5.0F, -20.0F, 10.0F, 10.0F, 20.0F),
                PartPose.offsetAndRotation(-67.0F, -2.0F, -7.0F, 0.3839724F, 0.0F, 0.0F));
        root.addOrReplaceChild("RLClaw2",
                CubeListBuilder.create().texOffs(200, 1550)
                        .addBox(-2.0F, -7.0F, -46.0F, 4.0F, 7.0F, 28.0F),
                PartPose.offsetAndRotation(-37.0F, -2.0F, -7.0F, 0.6457718F, 0.0F, 0.0F));
        root.addOrReplaceChild("RCClaw2",
                CubeListBuilder.create().texOffs(200, 1600)
                        .addBox(-2.0F, -7.0F, -46.0F, 4.0F, 7.0F, 28.0F),
                PartPose.offsetAndRotation(-52.0F, -2.0F, -7.0F, 0.6457718F, 0.0F, 0.0F));
        root.addOrReplaceChild("RRClaw2",
                CubeListBuilder.create().texOffs(200, 1650)
                        .addBox(-2.0F, -7.0F, -46.0F, 4.0F, 7.0F, 28.0F),
                PartPose.offsetAndRotation(-67.0F, -2.0F, -7.0F, 0.6457718F, 0.0F, 0.0F));
        root.addOrReplaceChild("NeckL2",
                CubeListBuilder.create().texOffs(500, 1154)
                        .addBox(-11.0F, -11.0F, -40.0F, 22.0F, 22.0F, 40.0F),
                PartPose.offsetAndRotation(32.0F, -114.0F, -95.0F, 0.0F, -0.1745329F, 0.0F));
        root.addOrReplaceChild("NeckC2",
                CubeListBuilder.create().texOffs(700, 1154)
                        .addBox(-11.0F, -11.0F, -40.0F, 22.0F, 22.0F, 40.0F),
                PartPose.offsetAndRotation(0.0F, -115.0F, -95.0F, -0.1570796F, 0.0F, 0.0F));
        root.addOrReplaceChild("NeckR2",
                CubeListBuilder.create().texOffs(900, 1154)
                        .addBox(-11.0F, -11.0F, -40.0F, 22.0F, 22.0F, 40.0F),
                PartPose.offsetAndRotation(-32.0F, -114.0F, -95.0F, -0.0523599F, 0.1745329F, 0.0F));
        root.addOrReplaceChild("NeckL3",
                CubeListBuilder.create().texOffs(500, 1222)
                        .addBox(-10.0F, -10.0F, -40.0F, 20.0F, 20.0F, 40.0F),
                PartPose.offsetAndRotation(38.0F, -114.0F, -131.0F, 0.0698132F, (float) (-Math.PI / 12.0), 0.0F));
        root.addOrReplaceChild("NeckC3",
                CubeListBuilder.create().texOffs(700, 1222)
                        .addBox(-10.0F, -10.0F, -40.0F, 20.0F, 20.0F, 40.0F),
                PartPose.offsetAndRotation(0.0F, -120.0F, -131.0F, -0.2094395F, 0.0F, 0.0F));
        root.addOrReplaceChild("NeckR3",
                CubeListBuilder.create().texOffs(900, 1222)
                        .addBox(-10.0F, -10.0F, -40.0F, 20.0F, 20.0F, 40.0F),
                PartPose.offsetAndRotation(-37.0F, -116.0F, -128.0F, -0.0523599F, 0.3141593F, 0.0F));
        root.addOrReplaceChild("NeckL4",
                CubeListBuilder.create().texOffs(500, 1300)
                        .addBox(-9.0F, -9.0F, -40.0F, 18.0F, 18.0F, 40.0F),
                PartPose.offsetAndRotation(46.0F, -112.0F, -163.0F, 0.1396263F, -0.3839724F, 0.0F));
        root.addOrReplaceChild("LHead1",
                CubeListBuilder.create().texOffs(500, 1425)
                        .addBox(-13.0F, -13.0F, -30.0F, 26.0F, 26.0F, 30.0F),
                PartPose.offsetAndRotation(59.0F, -114.0F, -195.0F, 0.1396263F, -0.3839724F, 0.0F));
        root.addOrReplaceChild("LHead2",
                CubeListBuilder.create().texOffs(500, 1550)
                        .addBox(-10.0F, -7.0F, -58.0F, 20.0F, 20.0F, 28.0F),
                PartPose.offsetAndRotation(59.0F, -114.0F, -195.0F, 0.1396263F, -0.3839724F, 0.0F));
        root.addOrReplaceChild("LHead3",
                CubeListBuilder.create().texOffs(500, 1605)
                        .addBox(-8.0F, -3.0F, -83.0F, 16.0F, 16.0F, 26.0F),
                PartPose.offsetAndRotation(59.0F, -114.0F, -195.0F, 0.1396263F, -0.3839724F, 0.0F));
        root.addOrReplaceChild("LJaw1",
                CubeListBuilder.create().texOffs(500, 1660)
                        .addBox(-14.0F, -2.0F, -14.0F, 28.0F, 7.0F, 16.0F),
                PartPose.offsetAndRotation(59.0F, -100.0F, -196.0F, 0.2443461F, -0.3839724F, 0.0F));
        root.addOrReplaceChild("LJaw2",
                CubeListBuilder.create().texOffs(500, 1700)
                        .addBox(-12.0F, -2.0F, -55.0F, 24.0F, 6.0F, 46.0F),
                PartPose.offsetAndRotation(59.0F, -100.0F, -196.0F, 0.2443461F, -0.3839724F, 0.0F));
        root.addOrReplaceChild("LJaw3",
                CubeListBuilder.create().texOffs(500, 1765)
                        .addBox(-10.0F, -2.0F, -77.0F, 20.0F, 6.0F, 22.0F),
                PartPose.offsetAndRotation(59.0F, -100.0F, -196.0F, 0.2443461F, -0.3839724F, 0.0F));
        root.addOrReplaceChild("LTooth1",
                CubeListBuilder.create().texOffs(10, 0)
                        .addBox(-10.0F, -10.0F, -77.0F, 2.0F, 8.0F, 2.0F),
                PartPose.offsetAndRotation(59.0F, -100.0F, -196.0F, 0.2443461F, -0.3839724F, 0.0F));
        root.addOrReplaceChild("LTooth2",
                CubeListBuilder.create().texOffs(20, 0)
                        .addBox(8.0F, -10.0F, -77.0F, 2.0F, 8.0F, 2.0F),
                PartPose.offsetAndRotation(59.0F, -100.0F, -196.0F, 0.2443461F, -0.3839724F, 0.0F));
        root.addOrReplaceChild("LTooth3",
                CubeListBuilder.create().texOffs(30, 0)
                        .addBox(-12.0F, -8.0F, -55.0F, 2.0F, 6.0F, 2.0F),
                PartPose.offsetAndRotation(59.0F, -100.0F, -196.0F, 0.2443461F, -0.3839724F, 0.0F));
        root.addOrReplaceChild("LTooth4",
                CubeListBuilder.create().texOffs(40, 0)
                        .addBox(10.0F, -8.0F, -55.0F, 2.0F, 6.0F, 2.0F),
                PartPose.offsetAndRotation(59.0F, -100.0F, -196.0F, 0.2443461F, -0.3839724F, 0.0F));
        root.addOrReplaceChild("NeckC4",
                CubeListBuilder.create().texOffs(700, 1300)
                        .addBox(-9.0F, -9.0F, -40.0F, 18.0F, 18.0F, 40.0F),
                PartPose.offsetAndRotation(0.0F, -126.0F, -162.0F, (float) (-Math.PI / 12.0), 0.0F, 0.0F));
        root.addOrReplaceChild("NeckR4",
                CubeListBuilder.create().texOffs(900, 1300)
                        .addBox(-9.0F, -9.0F, -40.0F, 18.0F, 18.0F, 40.0F),
                PartPose.offsetAndRotation(-47.0F, -118.0F, -161.0F, -0.1047198F, 0.3665191F, 0.0F));
        root.addOrReplaceChild("CHead1",
                CubeListBuilder.create().texOffs(700, 1425)
                        .addBox(-13.0F, -13.0F, -30.0F, 26.0F, 26.0F, 30.0F),
                PartPose.offsetAndRotation(0.0F, -141.0F, -195.0F, -0.296706F, 0.0F, 0.0F));
        root.addOrReplaceChild("RHead1",
                CubeListBuilder.create().texOffs(900, 1425)
                        .addBox(-13.0F, -13.0F, -30.0F, 26.0F, 26.0F, 30.0F),
                PartPose.offsetAndRotation(-60.0F, -128.0F, -195.0F, -0.122173F, 0.418879F, 0.0F));
        root.addOrReplaceChild("CHead2",
                CubeListBuilder.create().texOffs(700, 1550)
                        .addBox(-10.0F, -7.0F, -58.0F, 20.0F, 20.0F, 28.0F),
                PartPose.offsetAndRotation(0.0F, -141.0F, -195.0F, -0.296706F, 0.0F, 0.0F));
        root.addOrReplaceChild("RHead2",
                CubeListBuilder.create().texOffs(900, 1550)
                        .addBox(-10.0F, -7.0F, -58.0F, 20.0F, 20.0F, 28.0F),
                PartPose.offsetAndRotation(-60.0F, -128.0F, -195.0F, -0.122173F, 0.418879F, 0.0F));
        root.addOrReplaceChild("CHead3",
                CubeListBuilder.create().texOffs(700, 1605)
                        .addBox(-8.0F, -3.0F, -83.0F, 16.0F, 16.0F, 26.0F),
                PartPose.offsetAndRotation(0.0F, -141.0F, -195.0F, -0.296706F, 0.0F, 0.0F));
        root.addOrReplaceChild("RHead3",
                CubeListBuilder.create().texOffs(900, 1605)
                        .addBox(-8.0F, -3.0F, -83.0F, 16.0F, 16.0F, 26.0F),
                PartPose.offsetAndRotation(-60.0F, -128.0F, -195.0F, -0.122173F, 0.418879F, 0.0F));
        root.addOrReplaceChild("CJaw1",
                CubeListBuilder.create().texOffs(700, 1660)
                        .addBox(-14.0F, -2.0F, -14.0F, 28.0F, 7.0F, 16.0F),
                PartPose.offsetAndRotation(0.0F, -130.0F, -201.0F, -0.1047198F, 0.0F, 0.0F));
        root.addOrReplaceChild("CJaw2",
                CubeListBuilder.create().texOffs(700, 1700)
                        .addBox(-12.0F, -2.0F, -55.0F, 24.0F, 6.0F, 46.0F),
                PartPose.offsetAndRotation(0.0F, -130.0F, -201.0F, -0.1047198F, 0.0F, 0.0F));
        root.addOrReplaceChild("CJaw3",
                CubeListBuilder.create().texOffs(700, 1765)
                        .addBox(-10.0F, -2.0F, -77.0F, 20.0F, 6.0F, 22.0F),
                PartPose.offsetAndRotation(0.0F, -130.0F, -201.0F, -0.1047198F, 0.0F, 0.0F));
        root.addOrReplaceChild("RJaw1",
                CubeListBuilder.create().texOffs(900, 1660)
                        .addBox(-14.0F, -2.0F, -14.0F, 28.0F, 7.0F, 16.0F),
                PartPose.offsetAndRotation(-62.0F, -115.0F, -200.0F, 0.1570796F, 0.418879F, 0.0F));
        root.addOrReplaceChild("RJaw2",
                CubeListBuilder.create().texOffs(900, 1700)
                        .addBox(-12.0F, -2.0F, -55.0F, 24.0F, 6.0F, 46.0F),
                PartPose.offsetAndRotation(-62.0F, -115.0F, -200.0F, 0.1570796F, 0.418879F, 0.0F));
        root.addOrReplaceChild("RJaw3",
                CubeListBuilder.create().texOffs(900, 1765)
                        .addBox(-10.0F, -2.0F, -77.0F, 20.0F, 6.0F, 22.0F),
                PartPose.offsetAndRotation(-62.0F, -115.0F, -200.0F, 0.1570796F, 0.418879F, 0.0F));
        root.addOrReplaceChild("CTooth3",
                CubeListBuilder.create().texOffs(70, 0)
                        .addBox(-12.0F, -8.0F, -55.0F, 2.0F, 6.0F, 2.0F),
                PartPose.offsetAndRotation(0.0F, -130.0F, -201.0F, -0.1047198F, 0.0F, 0.0F));
        root.addOrReplaceChild("CTooth4",
                CubeListBuilder.create().texOffs(80, 0)
                        .addBox(10.0F, -8.0F, -55.0F, 2.0F, 6.0F, 2.0F),
                PartPose.offsetAndRotation(0.0F, -130.0F, -201.0F, -0.1047198F, 0.0F, 0.0F));
        root.addOrReplaceChild("CTooth1",
                CubeListBuilder.create().texOffs(50, 0)
                        .addBox(-10.0F, -10.0F, -77.0F, 2.0F, 8.0F, 2.0F),
                PartPose.offsetAndRotation(0.0F, -130.0F, -201.0F, -0.1047198F, 0.0F, 0.0F));
        root.addOrReplaceChild("CTooth2",
                CubeListBuilder.create().texOffs(60, 0)
                        .addBox(8.0F, -10.0F, -77.0F, 2.0F, 8.0F, 2.0F),
                PartPose.offsetAndRotation(0.0F, -130.0F, -201.0F, -0.1047198F, 0.0F, 0.0F));
        root.addOrReplaceChild("RTooth3",
                CubeListBuilder.create().texOffs(110, 0)
                        .addBox(-12.0F, -8.0F, -55.0F, 2.0F, 6.0F, 2.0F),
                PartPose.offsetAndRotation(-62.0F, -115.0F, -200.0F, 0.1570796F, 0.418879F, 0.0F));
        root.addOrReplaceChild("RTooth4",
                CubeListBuilder.create().texOffs(120, 0)
                        .addBox(10.0F, -8.0F, -55.0F, 2.0F, 6.0F, 2.0F),
                PartPose.offsetAndRotation(-62.0F, -115.0F, -200.0F, 0.1570796F, 0.418879F, 0.0F));
        root.addOrReplaceChild("RTooth1",
                CubeListBuilder.create().texOffs(90, 0)
                        .addBox(-10.0F, -10.0F, -77.0F, 2.0F, 8.0F, 2.0F),
                PartPose.offsetAndRotation(-62.0F, -115.0F, -200.0F, 0.1570796F, 0.418879F, 0.0F));
        root.addOrReplaceChild("RTooth2",
                CubeListBuilder.create().texOffs(100, 0)
                        .addBox(8.0F, -10.0F, -77.0F, 2.0F, 8.0F, 2.0F),
                PartPose.offsetAndRotation(-62.0F, -115.0F, -200.0F, 0.1570796F, 0.418879F, 0.0F));
        root.addOrReplaceChild("LLEye",
                CubeListBuilder.create().texOffs(500, 1500)
                        .addBox(13.0F, -12.0F, -29.0F, 2.0F, 6.0F, 10.0F),
                PartPose.offsetAndRotation(59.0F, -114.0F, -195.0F, 0.1396263F, -0.3839724F, 0.0F));
        root.addOrReplaceChild("LREye",
                CubeListBuilder.create().texOffs(533, 1500)
                        .addBox(-15.0F, -12.0F, -29.0F, 2.0F, 6.0F, 10.0F),
                PartPose.offsetAndRotation(59.0F, -114.0F, -195.0F, 0.1396263F, -0.3839724F, 0.0F));
        root.addOrReplaceChild("CLEye",
                CubeListBuilder.create().texOffs(700, 1500)
                        .addBox(13.0F, -12.0F, -29.0F, 2.0F, 6.0F, 10.0F),
                PartPose.offsetAndRotation(0.0F, -141.0F, -195.0F, -0.296706F, 0.0F, 0.0F));
        root.addOrReplaceChild("CREye",
                CubeListBuilder.create().texOffs(733, 1500)
                        .addBox(-15.0F, -12.0F, -29.0F, 2.0F, 6.0F, 10.0F),
                PartPose.offsetAndRotation(0.0F, -141.0F, -195.0F, -0.296706F, 0.0F, 0.0F));
        root.addOrReplaceChild("RLEye",
                CubeListBuilder.create().texOffs(900, 1500)
                        .addBox(13.0F, -12.0F, -29.0F, 2.0F, 6.0F, 10.0F),
                PartPose.offsetAndRotation(-60.0F, -128.0F, -195.0F, -0.122173F, 0.418879F, 0.0F));
        root.addOrReplaceChild("RREye",
                CubeListBuilder.create().texOffs(933, 1500)
                        .addBox(-15.0F, -12.0F, -29.0F, 2.0F, 5.0F, 10.0F),
                PartPose.offsetAndRotation(-60.0F, -128.0F, -195.0F, -0.122173F, 0.418879F, 0.0F));
        root.addOrReplaceChild("LHeadMane",
                CubeListBuilder.create().texOffs(500, 1375)
                        .addBox(0.0F, -14.0F, 0.0F, 1.0F, 23.0F, 19.0F),
                PartPose.offsetAndRotation(59.0F, -114.0F, -195.0F, (float) (Math.PI / 6), -0.3839724F, 0.0F));
        root.addOrReplaceChild("CHeadMane",
                CubeListBuilder.create().texOffs(700, 1375)
                        .addBox(0.0F, -14.0F, 0.0F, 1.0F, 23.0F, 19.0F),
                PartPose.offsetAndRotation(0.0F, -141.0F, -195.0F, 0.1047198F, 0.0F, 0.0F));
        root.addOrReplaceChild("RHeadMane",
                CubeListBuilder.create().texOffs(900, 1375)
                        .addBox(0.0F, -14.0F, 0.0F, 1.0F, 23.0F, 19.0F),
                PartPose.offsetAndRotation(-60.0F, -128.0F, -195.0F, 0.3665191F, 0.418879F, 0.0F));
        root.addOrReplaceChild("LLNoseSpike",
                CubeListBuilder.create().texOffs(0, 300)
                        .addBox(24.0F, -23.0F, -76.0F, 1.0F, 1.0F, 17.0F),
                PartPose.offsetAndRotation(59.0F, -114.0F, -195.0F, 0.4014257F, -0.1570796F, 0.0F));
        root.addOrReplaceChild("LRNoseSpike",
                CubeListBuilder.create().texOffs(0, 325)
                        .addBox(-26.0F, -24.0F, -75.0F, 1.0F, 1.0F, 17.0F),
                PartPose.offsetAndRotation(59.0F, -114.0F, -195.0F, 0.418879F, -0.6283185F, 0.0F));
        root.addOrReplaceChild("CLNoseSpike",
                CubeListBuilder.create().texOffs(0, 350)
                        .addBox(24.0F, -23.0F, -76.0F, 1.0F, 1.0F, 17.0F),
                PartPose.offsetAndRotation(0.0F, -141.0F, -195.0F, -0.0523599F, 0.2443461F, 0.0F));
        root.addOrReplaceChild("CRNoseSpike",
                CubeListBuilder.create().texOffs(0, 375)
                        .addBox(-26.0F, -24.0F, -75.0F, 1.0F, 1.0F, 17.0F),
                PartPose.offsetAndRotation(0.0F, -141.0F, -195.0F, -0.0349066F, (float) (-Math.PI / 12.0), 0.0F));
        root.addOrReplaceChild("RLNoseSpike",
                CubeListBuilder.create().texOffs(0, 400)
                        .addBox(24.0F, -23.0F, -76.0F, 1.0F, 1.0F, 17.0F),
                PartPose.offsetAndRotation(-60.0F, -128.0F, -195.0F, 0.1396263F, 0.6457718F, 0.0F));
        root.addOrReplaceChild("RRNoseSpike",
                CubeListBuilder.create().texOffs(0, 425)
                        .addBox(-26.0F, -24.0F, -75.0F, 1.0F, 1.0F, 17.0F),
                PartPose.offsetAndRotation(-60.0F, -128.0F, -195.0F, 0.1570796F, 0.1745329F, 0.0F));
        root.addOrReplaceChild("Back1",
                CubeListBuilder.create().texOffs(167, 186)
                        .addBox(0.0F, 0.0F, 0.0F, 30.0F, 15.0F, 49.0F),
                PartPose.offsetAndRotation(-45.0F, -127.0F, -58.0F, -0.0698132F, 0.0F, 0.0F));
        root.addOrReplaceChild("Back2",
                CubeListBuilder.create().texOffs(0, 186)
                        .addBox(0.0F, 0.0F, 0.0F, 30.0F, 15.0F, 49.0F),
                PartPose.offsetAndRotation(15.0F, -127.0F, -58.0F, -0.0698132F, 0.0F, 0.0F));
        root.addOrReplaceChild("Lwing1",
                CubeListBuilder.create().texOffs(250, 1000)
                        .addBox(0.0F, 0.0F, 0.0F, 87.0F, 5.0F, 5.0F),
                PartPose.offsetAndRotation(40.0F, -121.0F, -50.0F, 0.0F, 0.0349066F, 0.0F));
        root.addOrReplaceChild("Lwing2",
                CubeListBuilder.create().texOffs(1220, 782)
                        .addBox(0.0F, 2.0F, 0.0F, 87.0F, 1.0F, 110.0F),
                PartPose.offsetAndRotation(40.0F, -121.0F, -49.0F, 0.0F, 0.0349066F, 0.0F));
        root.addOrReplaceChild("Lwing3",
                CubeListBuilder.create().texOffs(250, 975)
                        .addBox(0.0F, 0.0F, 0.0F, 188.0F, 5.0F, 5.0F),
                PartPose.offset(124.0F, -121.0F, -53.0F));
        root.addOrReplaceChild("Lwing4",
                CubeListBuilder.create().texOffs(1341, 625)
                        .addBox(0.0F, 2.0F, 0.0F, 188.0F, 1.0F, 147.0F),
                PartPose.offset(124.0F, -121.0F, -50.0F));
        root.addOrReplaceChild("Lwing5",
                CubeListBuilder.create().texOffs(245, 950)
                        .addBox(0.0F, 0.0F, 0.0F, 87.0F, 5.0F, 5.0F),
                PartPose.offsetAndRotation(309.0F, -121.0F, -53.0F, 0.0F, -0.0349066F, 0.0F));
        root.addOrReplaceChild("Lwing6",
                CubeListBuilder.create().texOffs(1300, 1300)
                        .addBox(0.0F, 2.0F, 1.0F, 87.0F, 1.0F, 125.0F),
                PartPose.offset(309.0F, -121.0F, -50.0F));
        root.addOrReplaceChild("Lwing7",
                CubeListBuilder.create().texOffs(250, 900)
                        .addBox(0.0F, 0.0F, 0.0F, 150.0F, 5.0F, 5.0F),
                PartPose.offsetAndRotation(124.0F, -121.0F, -53.0F, 0.0F, 0.0F, (float) (Math.PI / 12.0)));
        root.addOrReplaceChild("Lwing9",
                CubeListBuilder.create().texOffs(250, 925)
                        .addBox(0.0F, 0.0F, 0.0F, 175.0F, 5.0F, 5.0F),
                PartPose.offsetAndRotation(124.0F, -121.0F, -53.0F, 0.0F, 0.0F, (float) (-Math.PI / 12.0)));
        root.addOrReplaceChild("Lwing8",
                CubeListBuilder.create().texOffs(1300, 1156)
                        .addBox(0.0F, 2.0F, 0.0F, 150.0F, 1.0F, 120.0F),
                PartPose.offsetAndRotation(124.0F, -121.0F, -50.0F, 0.0F, 0.0F, (float) (Math.PI / 12.0)));
        root.addOrReplaceChild("Lwing10",
                CubeListBuilder.create().texOffs(1392, 326)
                        .addBox(0.0F, 2.0F, 0.0F, 176.0F, 1.0F, 136.0F),
                PartPose.offsetAndRotation(124.0F, -121.0F, -50.0F, 0.0F, 0.0F, (float) (-Math.PI / 12.0)));
        root.addOrReplaceChild("Rwing1",
                CubeListBuilder.create().texOffs(650, 1000)
                        .addBox(-87.0F, 0.0F, 0.0F, 87.0F, 5.0F, 5.0F),
                PartPose.offsetAndRotation(-40.0F, -121.0F, -50.0F, 0.0F, -0.0349066F, 0.0F));
        root.addOrReplaceChild("Rwing2",
                CubeListBuilder.create().texOffs(1619, 782)
                        .addBox(-87.0F, 2.0F, 0.0F, 87.0F, 1.0F, 110.0F),
                PartPose.offsetAndRotation(-40.0F, -121.0F, -49.0F, 0.0F, -0.0349066F, 0.0F));
        root.addOrReplaceChild("Rwing3",
                CubeListBuilder.create().texOffs(550, 950)
                        .addBox(-188.0F, 0.0F, 0.0F, 188.0F, 5.0F, 5.0F),
                PartPose.offset(-124.0F, -121.0F, -53.0F));
        root.addOrReplaceChild("Rwing4",
                CubeListBuilder.create().texOffs(1341, 470)
                        .addBox(-188.0F, 2.0F, 0.0F, 188.0F, 1.0F, 147.0F),
                PartPose.offset(-124.0F, -121.0F, -53.0F));
        root.addOrReplaceChild("Rwing5",
                CubeListBuilder.create().texOffs(750, 975)
                        .addBox(-87.0F, 0.0F, 0.0F, 87.0F, 5.0F, 5.0F),
                PartPose.offsetAndRotation(-309.0F, -121.0F, -53.0F, 0.0F, 0.0349066F, 0.0F));
        root.addOrReplaceChild("Rwing6",
                CubeListBuilder.create().texOffs(1300, 1436)
                        .addBox(-87.0F, 2.0F, 1.0F, 87.0F, 1.0F, 125.0F),
                PartPose.offset(-309.0F, -121.0F, -50.0F));
        root.addOrReplaceChild("Rwing7",
                CubeListBuilder.create().texOffs(650, 900)
                        .addBox(-150.0F, 0.0F, 0.0F, 150.0F, 5.0F, 5.0F),
                PartPose.offsetAndRotation(-124.0F, -121.0F, -53.0F, 0.0F, 0.0F, (float) (-Math.PI / 12.0)));
        root.addOrReplaceChild("Rwing8",
                CubeListBuilder.create().texOffs(1300, 1024)
                        .addBox(-150.0F, 2.0F, 0.0F, 150.0F, 1.0F, 120.0F),
                PartPose.offsetAndRotation(-124.0F, -121.0F, -53.0F, 0.0F, 0.0F, (float) (-Math.PI / 12.0)));
        root.addOrReplaceChild("Rwing9",
                CubeListBuilder.create().texOffs(621, 925)
                        .addBox(-175.0F, 0.0F, 0.0F, 175.0F, 5.0F, 5.0F),
                PartPose.offsetAndRotation(-124.0F, -121.0F, -53.0F, 0.0F, 0.0F, (float) (Math.PI / 12.0)));
        root.addOrReplaceChild("Rwing10",
                CubeListBuilder.create().texOffs(1391, 184)
                        .addBox(-176.0F, 2.0F, 0.0F, 176.0F, 1.0F, 136.0F),
                PartPose.offsetAndRotation(-124.0F, -121.0F, -53.0F, 0.0F, 0.0F, (float) (Math.PI / 12.0)));
        root.addOrReplaceChild("TailTip2",
                CubeListBuilder.create().texOffs(500, 63)
                        .addBox(-35.0F, -2.0F, 36.0F, 70.0F, 4.0F, 26.0F),
                PartPose.offset(0.0F, -86.0F, 308.0F));
        root.addOrReplaceChild("Ridge1",
                CubeListBuilder.create().texOffs(850, 717)
                        .addBox(-1.0F, 0.0F, 0.0F, 3.0F, 10.0F, 24.0F),
                PartPose.offsetAndRotation(0.0F, -122.0F, -75.0F, 0.3665191F, 0.0F, 0.0F));
        root.addOrReplaceChild("Ridge2",
                CubeListBuilder.create().texOffs(850, 676)
                        .addBox(-1.0F, 0.0F, 0.0F, 3.0F, 10.0F, 24.0F),
                PartPose.offsetAndRotation(0.0F, -122.0F, -50.0F, 0.3665191F, 0.0F, 0.0F));
        root.addOrReplaceChild("Ridge3",
                CubeListBuilder.create().texOffs(800, 600)
                        .addBox(-1.0F, 0.0F, 0.0F, 3.0F, 20.0F, 49.0F),
                PartPose.offsetAndRotation(0.0F, -120.0F, -20.0F, 0.3665191F, 0.0F, 0.0F));
        root.addOrReplaceChild("Ridge4",
                CubeListBuilder.create().texOffs(800, 550)
                        .addBox(-1.0F, 3.0F, 9.0F, 3.0F, 10.0F, 22.0F),
                PartPose.offsetAndRotation(0.0F, -114.0F, 29.0F, 0.3665191F, 0.0F, 0.0F));
        root.addOrReplaceChild("Ridge5",
                CubeListBuilder.create().texOffs(800, 500)
                        .addBox(-1.0F, 13.0F, 33.0F, 3.0F, 10.0F, 20.0F),
                PartPose.offsetAndRotation(0.0F, -114.0F, 29.0F, 0.3665191F, 0.0F, 0.0F));
        root.addOrReplaceChild("Ridge6",
                CubeListBuilder.create().texOffs(638, 165)
                        .addBox(-1.0F, 2.0F, 20.0F, 3.0F, 10.0F, 20.0F),
                PartPose.offsetAndRotation(0.0F, -87.0F, 268.0F, 0.3665191F, 0.0F, 0.0F));

        return LayerDefinition.create(mesh, 2048, 2048);
    }

    @Override
    public ModelPart root() {
        return this.root;
    }

    @Override
    public void setupAnim(ThePrinceAdult k, float f, float f1, float f2, float f3, float f4) {
        float newangle = 0.0F;
        float tailspeed = 0.26F;
        float tailamp = 0.08F;
        float pi4 = (float) (Math.PI / 4.0);
        float Lheadlr = 0.0F;
        float Lheadud = 0.0F;
        float Ljawangle = 0.0F;
        float Cheadlr = 0.0F;
        float Cheadud = 0.0F;
        float Cjawangle = 0.0F;
        float Rheadlr = 0.0F;
        float Rheadud = 0.0F;
        float Rjawangle = 0.0F;
        if (k.getAttacking() != 0) {
           newangle = Mth.cos(f2 * 0.75F * this.wingspeed) * (float) Math.PI * 0.21F;
        } else {
           newangle = Mth.cos(f2 * 0.35F * this.wingspeed) * (float) Math.PI * 0.15F;
           if (k.getActivity() == 0) {
              newangle = Mth.cos(f2 * 0.35F * this.wingspeed) * (float) Math.PI * 0.15F * f1;
           }
        }

        if (k.isSitting()) {
           newangle = 0.0F;
        }

        this.Lwing1.zRot = newangle;
        this.Lwing2.zRot = newangle;
        this.Lwing3.zRot = newangle * 5.0F / 3.0F;
        this.Lwing3.y = this.Lwing1.y + (float)Math.sin(this.Lwing1.zRot) * 84.0F;
        this.Lwing3.x = this.Lwing1.x + (float)Math.cos(this.Lwing1.zRot) * 84.0F;
        this.Lwing4.zRot = this.Lwing3.zRot;
        this.Lwing4.y = this.Lwing3.y;
        this.Lwing4.x = this.Lwing3.x;
        this.Lwing5.zRot = newangle * 7.0F / 3.0F;
        this.Lwing5.y = this.Lwing3.y + (float)Math.sin(this.Lwing3.zRot) * 184.0F;
        this.Lwing5.x = this.Lwing3.x + (float)Math.cos(this.Lwing3.zRot) * 184.0F;
        this.Lwing6.zRot = this.Lwing5.zRot;
        this.Lwing6.y = this.Lwing5.y;
        this.Lwing6.x = this.Lwing5.x;
        this.Lwing7.y = this.Lwing9.y = this.Lwing3.y;
        this.Lwing7.x = this.Lwing9.x = this.Lwing3.x;
        this.Lwing8.y = this.Lwing10.y = this.Lwing3.y;
        this.Lwing8.x = this.Lwing10.x = this.Lwing3.x;
        this.Lwing7.zRot = this.Lwing8.zRot = 0.261F + this.Lwing3.zRot;
        this.Lwing9.zRot = this.Lwing10.zRot = -0.261F + this.Lwing3.zRot;
        this.Rwing1.zRot = -newangle;
        this.Rwing2.zRot = -newangle;
        this.Rwing3.zRot = -newangle * 5.0F / 3.0F;
        this.Rwing3.y = this.Rwing1.y - (float)Math.sin(this.Rwing1.zRot) * 84.0F;
        this.Rwing3.x = this.Rwing1.x - (float)Math.cos(this.Rwing1.zRot) * 84.0F;
        this.Rwing4.zRot = this.Rwing3.zRot;
        this.Rwing4.y = this.Rwing3.y;
        this.Rwing4.x = this.Rwing3.x;
        this.Rwing5.zRot = -newangle * 7.0F / 3.0F;
        this.Rwing5.y = this.Rwing3.y - (float)Math.sin(this.Rwing3.zRot) * 184.0F;
        this.Rwing5.x = this.Rwing3.x - (float)Math.cos(this.Rwing3.zRot) * 184.0F;
        this.Rwing6.zRot = this.Rwing5.zRot;
        this.Rwing6.y = this.Rwing5.y;
        this.Rwing6.x = this.Rwing5.x;
        this.Rwing7.y = this.Rwing9.y = this.Rwing3.y;
        this.Rwing7.x = this.Rwing9.x = this.Rwing3.x;
        this.Rwing8.y = this.Rwing10.y = this.Rwing3.y;
        this.Rwing8.x = this.Rwing10.x = this.Rwing3.x;
        this.Rwing7.zRot = this.Rwing8.zRot = -0.261F + this.Rwing3.zRot;
        this.Rwing9.zRot = this.Rwing10.zRot = 0.261F + this.Rwing3.zRot;
        newangle = 0.0F;
        if (k.getAttacking() != 0) {
           newangle = Mth.cos(f2 * 0.75F * this.wingspeed) * (float) Math.PI * 0.25F;
        }

        this.LClawRear.xRot = this.RClawRear.xRot = -0.925F + newangle;
        this.LLClaw1.xRot = 0.384F - newangle;
        this.LLClaw2.xRot = 0.645F - newangle;
        this.LCClaw1.xRot = 0.384F - newangle;
        this.LCClaw2.xRot = 0.645F - newangle;
        this.LRClaw1.xRot = 0.384F - newangle;
        this.LRClaw2.xRot = 0.645F - newangle;
        this.RLClaw1.xRot = 0.384F - newangle;
        this.RLClaw2.xRot = 0.645F - newangle;
        this.RCClaw1.xRot = 0.384F - newangle;
        this.RCClaw2.xRot = 0.645F - newangle;
        this.RRClaw1.xRot = 0.384F - newangle;
        this.RRClaw2.xRot = 0.645F - newangle;
        newangle = 0.0F;
        if (k.getAttacking() != 0) {
           newangle = Mth.cos(f2 * 0.6F * this.wingspeed) * (float) Math.PI * 0.45F;
        } else if (!k.isSitting() && k.getActivity() == 0) {
           newangle = Mth.cos(f2 * 0.3F * this.wingspeed) * (float) Math.PI * 0.25F * f1;
        }

        this.LThigh.xRot = this.LUpperLeg.xRot = 0.785F + newangle / 4.0F;
        this.LLowerLeg.xRot = this.LFoot.xRot = -0.628F + newangle / 2.0F;
        this.LLowerLeg.y = this.LFoot.y = this.LUpperLeg.y + (float)Math.cos(this.LUpperLeg.xRot) * 50.0F;
        this.LLowerLeg.z = this.LFoot.z = this.LUpperLeg.z + (float)Math.sin(this.LUpperLeg.xRot) * 50.0F;
        this.LLClaw1.y = this.LLClaw2.y = this.LLowerLeg.y + (float)Math.cos(this.LLowerLeg.xRot - 0.1F) * 66.0F;
        this.LLClaw1.z = this.LLClaw2.z = this.LLowerLeg.z + (float)Math.sin(this.LLowerLeg.xRot - 0.1F) * 66.0F;
        this.LCClaw1.y = this.LCClaw2.y = this.LLClaw1.y;
        this.LRClaw1.y = this.LRClaw2.y = this.LLClaw1.y;
        this.LCClaw1.z = this.LCClaw2.z = this.LLClaw1.z;
        this.LRClaw1.z = this.LRClaw2.z = this.LLClaw1.z;
        this.LClawRear.y = this.LLowerLeg.y + (float)Math.cos(this.LLowerLeg.xRot + 0.15F) * 66.0F;
        this.LClawRear.z = this.LLowerLeg.z + (float)Math.sin(this.LLowerLeg.xRot + 0.15F) * 66.0F;
        this.RThigh.xRot = this.RUpperLeg.xRot = 0.785F - newangle / 4.0F;
        this.RLowerLeg.xRot = this.RFoot.xRot = -0.628F - newangle / 2.0F;
        this.RLowerLeg.y = this.RFoot.y = this.RUpperLeg.y + (float)Math.cos(this.RUpperLeg.xRot) * 50.0F;
        this.RLowerLeg.z = this.RFoot.z = this.RUpperLeg.z + (float)Math.sin(this.RUpperLeg.xRot) * 50.0F;
        this.RLClaw1.y = this.RLClaw2.y = this.RLowerLeg.y + (float)Math.cos(this.RLowerLeg.xRot - 0.1F) * 66.0F;
        this.RLClaw1.z = this.RLClaw2.z = this.RLowerLeg.z + (float)Math.sin(this.RLowerLeg.xRot - 0.1F) * 66.0F;
        this.RCClaw1.y = this.RCClaw2.y = this.RLClaw1.y;
        this.RRClaw1.y = this.RRClaw2.y = this.RLClaw1.y;
        this.RCClaw1.z = this.RCClaw2.z = this.RLClaw1.z;
        this.RRClaw1.z = this.RRClaw2.z = this.RLClaw1.z;
        this.RClawRear.y = this.RLowerLeg.y + (float)Math.cos(this.RLowerLeg.xRot + 0.15F) * 66.0F;
        this.RClawRear.z = this.RLowerLeg.z + (float)Math.sin(this.RLowerLeg.xRot + 0.15F) * 66.0F;
        if (k.getAttacking() != 0) {
           tailspeed = 0.56F;
           tailamp = 0.19F;
        }

        if (k.isSitting()) {
           tailspeed = 0.0F;
           tailamp = 0.0F;
        }

        this.Tail1.yRot = Mth.cos(f2 * tailspeed * this.wingspeed) * (float) Math.PI * tailamp / 2.0F;
        this.Ridge4.yRot = this.Ridge5.yRot = this.Tail1.yRot;
        this.Tail2.z = this.Tail1.z + (float)Math.cos(this.Tail1.yRot) * 54.0F;
        this.Tail2.x = this.Tail1.x - 1.0F + (float)Math.sin(this.Tail1.yRot) * 54.0F;
        this.Tail2.yRot = Mth.cos(f2 * tailspeed * this.wingspeed - pi4) * (float) Math.PI * tailamp;
        this.Tail3.z = this.Tail2.z + (float)Math.cos(this.Tail2.yRot) * 42.0F;
        this.Tail3.x = this.Tail2.x + (float)Math.sin(this.Tail2.yRot) * 42.0F;
        this.Tail3.yRot = Mth.cos(f2 * tailspeed * this.wingspeed - 2.0F * pi4) * (float) Math.PI * tailamp;
        this.Tail4.z = this.Tail3.z + (float)Math.cos(this.Tail3.yRot) * 41.0F;
        this.Tail4.x = this.Tail3.x + (float)Math.sin(this.Tail3.yRot) * 41.0F;
        this.Tail4.yRot = Mth.cos(f2 * tailspeed * this.wingspeed - 3.0F * pi4) * (float) Math.PI * tailamp;
        newangle = Mth.cos(f2 * tailspeed * this.wingspeed - 3.0F * pi4) * (float) Math.PI * tailamp;
        newangle /= 2.0F;
        this.Tail5.z = this.Tail4.z + (float)Math.cos(this.Tail4.yRot) * 34.0F;
        this.Tail5.x = this.Tail4.x + (float)Math.sin(this.Tail4.yRot) * 34.0F;
        this.Tail5.yRot = this.Tail4.yRot + newangle;
        this.Tail6.z = this.Tail5.z + (float)Math.cos(this.Tail5.yRot) * 34.0F;
        this.Tail6.x = this.Tail5.x + (float)Math.sin(this.Tail5.yRot) * 34.0F;
        this.Tail6.yRot = this.Tail5.yRot + newangle;
        this.Tail7.z = this.Ridge6.z = this.Tail6.z + (float)Math.cos(this.Tail6.yRot) * 40.0F;
        this.Tail7.x = this.Ridge6.x = this.Tail6.x + (float)Math.sin(this.Tail6.yRot) * 40.0F;
        this.Tail7.yRot = this.Ridge6.yRot = this.Tail6.yRot + newangle;
        this.TailTip.z = this.TailTip2.z = this.Tail7.z + (float)Math.cos(this.Tail7.yRot) * 43.0F;
        this.TailTip.x = this.TailTip2.x = this.Tail7.x + (float)Math.sin(this.Tail7.yRot) * 43.0F;
        this.TailTip.yRot = this.TailTip2.yRot = this.Tail7.yRot + newangle;
        this.TailSpike.z = this.TailTip.z + (float)Math.cos(this.TailTip.yRot) * 58.0F;
        this.TailSpike.x = this.TailTip.x + (float)Math.sin(this.TailTip.yRot) * 58.0F;
        this.TailSpike.yRot = this.TailTip.yRot + newangle;
        if (k.getAttacking() != 0) {
           Lheadlr = Mth.sin(f2 * 0.3F * this.wingspeed) * (float) Math.PI * 0.25F;
           Lheadud = (float)Math.toRadians(k.getHead1Ext() - 30);
           Ljawangle = Mth.sin(f2 * 0.85F * this.wingspeed) * (float) Math.PI * 0.12F;
           Rheadlr = Mth.sin(f2 * 0.32F * this.wingspeed) * (float) Math.PI * 0.25F;
           Rheadud = (float)Math.toRadians(k.getHead3Ext() - 30);
           Rjawangle = Mth.sin(f2 * 0.95F * this.wingspeed) * (float) Math.PI * 0.12F;
           Cheadlr = Mth.sin(f2 * 0.28F * this.wingspeed) * (float) Math.PI * 0.25F;
           Cheadud = (float)Math.toRadians(k.getHead2Ext() - 30);
           Cjawangle = Mth.sin(f2 * 0.75F * this.wingspeed) * (float) Math.PI * 0.12F;
           Ljawangle += 0.5F;
           Ljawangle += Lheadud;
           Cjawangle += 0.5F;
           Cjawangle += Cheadud;
           Rjawangle += 0.5F;
           Rjawangle += Rheadud;
        } else {
           if (k.getActivity() != 0) {
              Lheadlr = Mth.sin(f2 * 0.17F * this.wingspeed) * (float) Math.PI * 0.08F;
              Lheadud = (float)Math.toRadians(k.getHead1Ext() - 30);
              Ljawangle = Mth.sin(f2 * 0.45F * this.wingspeed) * (float) Math.PI * 0.04F;
              Rheadlr = Mth.sin(f2 * 0.19F * this.wingspeed) * (float) Math.PI * 0.08F;
              Rheadud = (float)Math.toRadians(k.getHead3Ext() - 30);
              Rjawangle = Mth.sin(f2 * 0.55F * this.wingspeed) * (float) Math.PI * 0.04F;
              Cheadlr = Mth.sin(f2 * 0.13F * this.wingspeed) * (float) Math.PI * 0.08F;
              Cheadud = (float)Math.toRadians(k.getHead2Ext() - 30);
              Cjawangle = Mth.sin(f2 * 0.65F * this.wingspeed) * (float) Math.PI * 0.04F;
           } else {
              Lheadud = (float)Math.toRadians(k.getHead1Ext() - 30);
              Rheadud = (float)Math.toRadians(k.getHead3Ext() - 30);
              Cheadud = (float)Math.toRadians(k.getHead2Ext() - 30);
              float h3;
              float h1 = h3 = f3 * 2.0F / 3.0F;
              float d3;
              float d1 = d3 = f4 * 2.0F / 3.0F;
              float h2;
              float d2;
              if (h1 < 0.0F) {
                 h2 = h3 = h1 / 2.0F;
                 d2 = d3 = d1 / 2.0F;
              } else {
                 h2 = h1 = h3 / 2.0F;
                 d2 = d1 = d3 / 2.0F;
              }

              Lheadlr = (float)Math.toRadians(h1);
              Cheadlr = (float)Math.toRadians(h2);
              Rheadlr = (float)Math.toRadians(h3);
              Lheadud += (float)Math.toRadians(d1);
              Cheadud += (float)Math.toRadians(d2);
              Rheadud += (float)Math.toRadians(d3);
              Ljawangle = Mth.sin(f2 * 0.25F * this.wingspeed) * (float) Math.PI * 0.03F;
              Rjawangle = Mth.sin(f2 * 0.35F * this.wingspeed) * (float) Math.PI * 0.03F;
              Cjawangle = Mth.sin(f2 * 0.45F * this.wingspeed) * (float) Math.PI * 0.03F;
           }

           Ljawangle += 0.25F;
           Ljawangle += Lheadud;
           Cjawangle += 0.25F;
           Cjawangle += Cheadud;
           Rjawangle += 0.25F;
           Rjawangle += Rheadud;
        }

        if (Lheadlr > Cheadlr) {
           Lheadlr = Cheadlr;
        }

        if (Rheadlr < Cheadlr) {
           Rheadlr = Cheadlr;
        }

        this.moveLeftHead(Lheadlr, Lheadud, Ljawangle);
        this.moveCenterHead(Cheadlr, Cheadud, Cjawangle);
        this.moveRightHead(Rheadlr, Rheadud, Rjawangle);
    }

    private void moveLeftHead(float a, float b, float c) {

        this.LJaw1.xRot = c;
        this.LJaw2.xRot = c;
        this.LJaw3.xRot = c;
        this.LTooth1.xRot = c;
        this.LTooth2.xRot = c;
        this.LTooth3.xRot = c;
        this.LTooth4.xRot = c;
        this.NeckL1.yRot = a * 0.125F;
        this.NeckL2.z = this.NeckL1.z - (float)Math.cos(this.NeckL1.yRot) * 20.0F;
        this.NeckL2.x = this.NeckL1.x - (float)Math.sin(this.NeckL1.yRot) * 20.0F;
        this.NeckL2.yRot = a * 0.25F;
        this.NeckL3.z = this.NeckL2.z - (float)Math.cos(this.NeckL2.yRot) * 36.0F;
        this.NeckL3.x = this.NeckL2.x - (float)Math.sin(this.NeckL2.yRot) * 36.0F;
        this.NeckL3.yRot = a * 0.38F;
        this.NeckL4.z = this.NeckL3.z - (float)Math.cos(this.NeckL3.yRot) * 36.0F;
        this.NeckL4.x = this.NeckL3.x - (float)Math.sin(this.NeckL3.yRot) * 36.0F;
        this.NeckL4.yRot = a * 0.5F;
        this.LHead1.z = this.NeckL4.z - (float)Math.cos(this.NeckL4.yRot) * 36.0F;
        this.LHead1.x = this.NeckL4.x - (float)Math.sin(this.NeckL4.yRot) * 36.0F;
        this.LHead1.yRot = a;
        this.LHead2.yRot = a;
        this.LHead2.z = this.LHead1.z;
        this.LHead2.x = this.LHead1.x;
        this.LHead3.yRot = a;
        this.LHead3.z = this.LHead1.z;
        this.LHead3.x = this.LHead1.x;
        this.LHeadMane.yRot = a;
        this.LHeadMane.z = this.LHead1.z;
        this.LHeadMane.x = this.LHead1.x;
        this.LLEye.yRot = a;
        this.LLEye.z = this.LHead1.z;
        this.LLEye.x = this.LHead1.x;
        this.LREye.yRot = a;
        this.LREye.z = this.LHead1.z;
        this.LREye.x = this.LHead1.x;
        this.LLNoseSpike.yRot = 0.244F + a;
        this.LLNoseSpike.z = this.LHead1.z;
        this.LLNoseSpike.x = this.LHead1.x;
        this.LRNoseSpike.yRot = -0.261F + a;
        this.LRNoseSpike.z = this.LHead1.z;
        this.LRNoseSpike.x = this.LHead1.x;
        this.LJaw1.yRot = a;
        this.LJaw2.yRot = a;
        this.LJaw3.yRot = a;
        this.LTooth1.yRot = a;
        this.LTooth2.yRot = a;
        this.LTooth3.yRot = a;
        this.LTooth4.yRot = a;
        this.NeckL1.xRot = b * 0.125F;
        this.NeckL2.y = this.NeckL1.y + (float)Math.sin(this.NeckL1.xRot) * 20.0F;
        this.NeckL2.z = this.NeckL1.z
           + (this.NeckL2.z - this.NeckL1.z) * (float)Math.cos(this.NeckL1.xRot);
        this.NeckL2.x = this.NeckL1.x
           + (this.NeckL2.x - this.NeckL1.x) * (float)Math.cos(this.NeckL1.xRot);
        this.NeckL2.xRot = b * 0.25F;
        this.NeckL3.y = this.NeckL2.y + (float)Math.sin(this.NeckL2.xRot) * 36.0F;
        this.NeckL3.z = this.NeckL2.z
           + (this.NeckL3.z - this.NeckL2.z) * (float)Math.cos(this.NeckL2.xRot);
        this.NeckL3.x = this.NeckL2.x
           + (this.NeckL3.x - this.NeckL2.x) * (float)Math.cos(this.NeckL2.xRot);
        this.NeckL3.xRot = b * 0.38F;
        this.NeckL4.y = this.NeckL3.y + (float)Math.sin(this.NeckL3.xRot) * 36.0F;
        this.NeckL4.z = this.NeckL3.z
           + (this.NeckL4.z - this.NeckL3.z) * (float)Math.cos(this.NeckL3.xRot);
        this.NeckL4.x = this.NeckL3.x
           + (this.NeckL4.x - this.NeckL3.x) * (float)Math.cos(this.NeckL3.xRot);
        this.NeckL4.xRot = b * 0.5F;
        this.LHead1.y = this.NeckL4.y + (float)Math.sin(this.NeckL4.xRot) * 36.0F;
        this.LHead1.z = this.NeckL4.z
           + (this.LHead1.z - this.NeckL4.z) * (float)Math.cos(this.NeckL4.xRot);
        this.LHead1.x = this.NeckL4.x
           + (this.LHead1.x - this.NeckL4.x) * (float)Math.cos(this.NeckL4.xRot);
        this.LHead1.xRot = b;
        this.LHead2.xRot = b;
        this.LHead2.z = this.LHead1.z;
        this.LHead2.x = this.LHead1.x;
        this.LHead2.y = this.LHead1.y;
        this.LHead3.xRot = b;
        this.LHead3.z = this.LHead1.z;
        this.LHead3.x = this.LHead1.x;
        this.LHead3.y = this.LHead1.y;
        this.LHeadMane.xRot = 0.384F + b;
        this.LHeadMane.z = this.LHead1.z;
        this.LHeadMane.x = this.LHead1.x;
        this.LHeadMane.y = this.LHead1.y;
        this.LLEye.xRot = b;
        this.LLEye.z = this.LHead1.z;
        this.LLEye.x = this.LHead1.x;
        this.LLEye.y = this.LHead1.y;
        this.LREye.xRot = b;
        this.LREye.z = this.LHead1.z;
        this.LREye.x = this.LHead1.x;
        this.LREye.y = this.LHead1.y;
        this.LLNoseSpike.xRot = 0.244F + b;
        this.LLNoseSpike.z = this.LHead1.z;
        this.LLNoseSpike.x = this.LHead1.x;
        this.LLNoseSpike.y = this.LHead1.y;
        this.LRNoseSpike.xRot = 0.261F + b;
        this.LRNoseSpike.z = this.LHead1.z;
        this.LRNoseSpike.x = this.LHead1.x;
        this.LRNoseSpike.y = this.LHead1.y;
        this.LJaw1.y = this.LHead1.y + (float)Math.cos(this.LHead1.xRot) * 14.0F;
        this.LJaw1.z = this.NeckL4.z + -38.0F * (float)Math.cos(this.NeckL4.xRot);
        this.LJaw1.z = this.LJaw1.z + (float)Math.sin(this.LHead1.xRot) * 12.0F;
        this.LJaw1.x = (float)(this.LHead1.x - Math.sin(this.LHead1.yRot) * 7.0);
        this.LJaw2.z = this.LJaw1.z;
        this.LJaw2.x = this.LJaw1.x;
        this.LJaw2.y = this.LJaw1.y;
        this.LJaw3.z = this.LJaw1.z;
        this.LJaw3.x = this.LJaw1.x;
        this.LJaw3.y = this.LJaw1.y;
        this.LTooth1.z = this.LJaw1.z;
        this.LTooth1.x = this.LJaw1.x;
        this.LTooth1.y = this.LJaw1.y;
        this.LTooth2.z = this.LJaw1.z;
        this.LTooth2.x = this.LJaw1.x;
        this.LTooth2.y = this.LJaw1.y;
        this.LTooth3.z = this.LJaw1.z;
        this.LTooth3.x = this.LJaw1.x;
        this.LTooth3.y = this.LJaw1.y;
        this.LTooth4.z = this.LJaw1.z;
        this.LTooth4.x = this.LJaw1.x;
        this.LTooth4.y = this.LJaw1.y;

    }

    private void moveCenterHead(float a, float b, float c) {

        this.CJaw1.xRot = c;
        this.CJaw2.xRot = c;
        this.CJaw3.xRot = c;
        this.CTooth1.xRot = c;
        this.CTooth2.xRot = c;
        this.CTooth3.xRot = c;
        this.CTooth4.xRot = c;
        this.NeckC1.yRot = a * 0.125F;
        this.NeckC2.z = this.NeckC1.z - (float)Math.cos(this.NeckC1.yRot) * 20.0F;
        this.NeckC2.x = this.NeckC1.x - (float)Math.sin(this.NeckC1.yRot) * 20.0F;
        this.NeckC2.yRot = a * 0.25F;
        this.NeckC3.z = this.NeckC2.z - (float)Math.cos(this.NeckC2.yRot) * 36.0F;
        this.NeckC3.x = this.NeckC2.x - (float)Math.sin(this.NeckC2.yRot) * 36.0F;
        this.NeckC3.yRot = a * 0.38F;
        this.NeckC4.z = this.NeckC3.z - (float)Math.cos(this.NeckC3.yRot) * 36.0F;
        this.NeckC4.x = this.NeckC3.x - (float)Math.sin(this.NeckC3.yRot) * 36.0F;
        this.NeckC4.yRot = a * 0.5F;
        this.CHead1.z = this.NeckC4.z - (float)Math.cos(this.NeckC4.yRot) * 36.0F;
        this.CHead1.x = this.NeckC4.x - (float)Math.sin(this.NeckC4.yRot) * 36.0F;
        this.CHead1.yRot = a;
        this.CHead2.yRot = a;
        this.CHead2.z = this.CHead1.z;
        this.CHead2.x = this.CHead1.x;
        this.CHead3.yRot = a;
        this.CHead3.z = this.CHead1.z;
        this.CHead3.x = this.CHead1.x;
        this.CHeadMane.yRot = a;
        this.CHeadMane.z = this.CHead1.z;
        this.CHeadMane.x = this.CHead1.x;
        this.CLEye.yRot = a;
        this.CLEye.z = this.CHead1.z;
        this.CLEye.x = this.CHead1.x;
        this.CREye.yRot = a;
        this.CREye.z = this.CHead1.z;
        this.CREye.x = this.CHead1.x;
        this.CLNoseSpike.yRot = 0.244F + a;
        this.CLNoseSpike.z = this.CHead1.z;
        this.CLNoseSpike.x = this.CHead1.x;
        this.CRNoseSpike.yRot = -0.261F + a;
        this.CRNoseSpike.z = this.CHead1.z;
        this.CRNoseSpike.x = this.CHead1.x;
        this.CJaw1.yRot = a;
        this.CJaw2.yRot = a;
        this.CJaw3.yRot = a;
        this.CTooth1.yRot = a;
        this.CTooth2.yRot = a;
        this.CTooth3.yRot = a;
        this.CTooth4.yRot = a;
        this.NeckC1.xRot = b * 0.125F;
        this.NeckC2.y = this.NeckC1.y + (float)Math.sin(this.NeckC1.xRot) * 20.0F;
        this.NeckC2.z = this.NeckC1.z
           + (this.NeckC2.z - this.NeckC1.z) * (float)Math.cos(this.NeckC1.xRot);
        this.NeckC2.x = this.NeckC1.x
           + (this.NeckC2.x - this.NeckC1.x) * (float)Math.cos(this.NeckC1.xRot);
        this.NeckC2.xRot = b * 0.25F;
        this.NeckC3.y = this.NeckC2.y + (float)Math.sin(this.NeckC2.xRot) * 36.0F;
        this.NeckC3.z = this.NeckC2.z
           + (this.NeckC3.z - this.NeckC2.z) * (float)Math.cos(this.NeckC2.xRot);
        this.NeckC3.x = this.NeckC2.x
           + (this.NeckC3.x - this.NeckC2.x) * (float)Math.cos(this.NeckC2.xRot);
        this.NeckC3.xRot = b * 0.38F;
        this.NeckC4.y = this.NeckC3.y + (float)Math.sin(this.NeckC3.xRot) * 36.0F;
        this.NeckC4.z = this.NeckC3.z
           + (this.NeckC4.z - this.NeckC3.z) * (float)Math.cos(this.NeckC3.xRot);
        this.NeckC4.x = this.NeckC3.x
           + (this.NeckC4.x - this.NeckC3.x) * (float)Math.cos(this.NeckC3.xRot);
        this.NeckC4.xRot = b * 0.5F;
        this.CHead1.y = this.NeckC4.y + (float)Math.sin(this.NeckC4.xRot) * 36.0F;
        this.CHead1.z = this.NeckC4.z
           + (this.CHead1.z - this.NeckC4.z) * (float)Math.cos(this.NeckC4.xRot);
        this.CHead1.x = this.NeckC4.x
           + (this.CHead1.x - this.NeckC4.x) * (float)Math.cos(this.NeckC4.xRot);
        this.CHead1.xRot = b;
        this.CHead2.xRot = b;
        this.CHead2.z = this.CHead1.z;
        this.CHead2.x = this.CHead1.x;
        this.CHead2.y = this.CHead1.y;
        this.CHead3.xRot = b;
        this.CHead3.z = this.CHead1.z;
        this.CHead3.x = this.CHead1.x;
        this.CHead3.y = this.CHead1.y;
        this.CHeadMane.xRot = 0.384F + b;
        this.CHeadMane.z = this.CHead1.z;
        this.CHeadMane.x = this.CHead1.x;
        this.CHeadMane.y = this.CHead1.y;
        this.CLEye.xRot = b;
        this.CLEye.z = this.CHead1.z;
        this.CLEye.x = this.CHead1.x;
        this.CLEye.y = this.CHead1.y;
        this.CREye.xRot = b;
        this.CREye.z = this.CHead1.z;
        this.CREye.x = this.CHead1.x;
        this.CREye.y = this.CHead1.y;
        this.CLNoseSpike.xRot = 0.244F + b;
        this.CLNoseSpike.z = this.CHead1.z;
        this.CLNoseSpike.x = this.CHead1.x;
        this.CLNoseSpike.y = this.CHead1.y;
        this.CRNoseSpike.xRot = 0.261F + b;
        this.CRNoseSpike.z = this.CHead1.z;
        this.CRNoseSpike.x = this.CHead1.x;
        this.CRNoseSpike.y = this.CHead1.y;
        this.CJaw1.y = this.CHead1.y + (float)Math.cos(this.CHead1.xRot) * 14.0F;
        this.CJaw1.z = this.NeckC4.z + -38.0F * (float)Math.cos(this.NeckC4.xRot);
        this.CJaw1.z = this.CJaw1.z + (float)Math.sin(this.CHead1.xRot) * 12.0F;
        this.CJaw1.x = (float)(this.CHead1.x - Math.sin(this.CHead1.yRot) * 7.0);
        this.CJaw2.z = this.CJaw1.z;
        this.CJaw2.x = this.CJaw1.x;
        this.CJaw2.y = this.CJaw1.y;
        this.CJaw3.z = this.CJaw1.z;
        this.CJaw3.x = this.CJaw1.x;
        this.CJaw3.y = this.CJaw1.y;
        this.CTooth1.z = this.CJaw1.z;
        this.CTooth1.x = this.CJaw1.x;
        this.CTooth1.y = this.CJaw1.y;
        this.CTooth2.z = this.CJaw1.z;
        this.CTooth2.x = this.CJaw1.x;
        this.CTooth2.y = this.CJaw1.y;
        this.CTooth3.z = this.CJaw1.z;
        this.CTooth3.x = this.CJaw1.x;
        this.CTooth3.y = this.CJaw1.y;
        this.CTooth4.z = this.CJaw1.z;
        this.CTooth4.x = this.CJaw1.x;
        this.CTooth4.y = this.CJaw1.y;

    }

    private void moveRightHead(float a, float b, float c) {

        this.RJaw1.xRot = c;
        this.RJaw2.xRot = c;
        this.RJaw3.xRot = c;
        this.RTooth1.xRot = c;
        this.RTooth2.xRot = c;
        this.RTooth3.xRot = c;
        this.RTooth4.xRot = c;
        this.NeckR1.yRot = a * 0.125F;
        this.NeckR2.z = this.NeckR1.z - (float)Math.cos(this.NeckR1.yRot) * 20.0F;
        this.NeckR2.x = this.NeckR1.x - (float)Math.sin(this.NeckR1.yRot) * 20.0F;
        this.NeckR2.yRot = a * 0.25F;
        this.NeckR3.z = this.NeckR2.z - (float)Math.cos(this.NeckR2.yRot) * 36.0F;
        this.NeckR3.x = this.NeckR2.x - (float)Math.sin(this.NeckR2.yRot) * 36.0F;
        this.NeckR3.yRot = a * 0.38F;
        this.NeckR4.z = this.NeckR3.z - (float)Math.cos(this.NeckR3.yRot) * 36.0F;
        this.NeckR4.x = this.NeckR3.x - (float)Math.sin(this.NeckR3.yRot) * 36.0F;
        this.NeckR4.yRot = a * 0.5F;
        this.RHead1.z = this.NeckR4.z - (float)Math.cos(this.NeckR4.yRot) * 36.0F;
        this.RHead1.x = this.NeckR4.x - (float)Math.sin(this.NeckR4.yRot) * 36.0F;
        this.RHead1.yRot = a;
        this.RHead2.yRot = a;
        this.RHead2.z = this.RHead1.z;
        this.RHead2.x = this.RHead1.x;
        this.RHead3.yRot = a;
        this.RHead3.z = this.RHead1.z;
        this.RHead3.x = this.RHead1.x;
        this.RHeadMane.yRot = a;
        this.RHeadMane.z = this.RHead1.z;
        this.RHeadMane.x = this.RHead1.x;
        this.RLEye.yRot = a;
        this.RLEye.z = this.RHead1.z;
        this.RLEye.x = this.RHead1.x;
        this.RREye.yRot = a;
        this.RREye.z = this.RHead1.z;
        this.RREye.x = this.RHead1.x;
        this.RLNoseSpike.yRot = 0.244F + a;
        this.RLNoseSpike.z = this.RHead1.z;
        this.RLNoseSpike.x = this.RHead1.x;
        this.RRNoseSpike.yRot = -0.261F + a;
        this.RRNoseSpike.z = this.RHead1.z;
        this.RRNoseSpike.x = this.RHead1.x;
        this.RJaw1.yRot = a;
        this.RJaw2.yRot = a;
        this.RJaw3.yRot = a;
        this.RTooth1.yRot = a;
        this.RTooth2.yRot = a;
        this.RTooth3.yRot = a;
        this.RTooth4.yRot = a;
        this.NeckR1.xRot = b * 0.125F;
        this.NeckR2.y = this.NeckR1.y + (float)Math.sin(this.NeckR1.xRot) * 20.0F;
        this.NeckR2.z = this.NeckR1.z
           + (this.NeckR2.z - this.NeckR1.z) * (float)Math.cos(this.NeckR1.xRot);
        this.NeckR2.x = this.NeckR1.x
           + (this.NeckR2.x - this.NeckR1.x) * (float)Math.cos(this.NeckR1.xRot);
        this.NeckR2.xRot = b * 0.25F;
        this.NeckR3.y = this.NeckR2.y + (float)Math.sin(this.NeckR2.xRot) * 36.0F;
        this.NeckR3.z = this.NeckR2.z
           + (this.NeckR3.z - this.NeckR2.z) * (float)Math.cos(this.NeckR2.xRot);
        this.NeckR3.x = this.NeckR2.x
           + (this.NeckR3.x - this.NeckR2.x) * (float)Math.cos(this.NeckR2.xRot);
        this.NeckR3.xRot = b * 0.38F;
        this.NeckR4.y = this.NeckR3.y + (float)Math.sin(this.NeckR3.xRot) * 36.0F;
        this.NeckR4.z = this.NeckR3.z
           + (this.NeckR4.z - this.NeckR3.z) * (float)Math.cos(this.NeckR3.xRot);
        this.NeckR4.x = this.NeckR3.x
           + (this.NeckR4.x - this.NeckR3.x) * (float)Math.cos(this.NeckR3.xRot);
        this.NeckR4.xRot = b * 0.5F;
        this.RHead1.y = this.NeckR4.y + (float)Math.sin(this.NeckR4.xRot) * 36.0F;
        this.RHead1.z = this.NeckR4.z
           + (this.RHead1.z - this.NeckR4.z) * (float)Math.cos(this.NeckR4.xRot);
        this.RHead1.x = this.NeckR4.x
           + (this.RHead1.x - this.NeckR4.x) * (float)Math.cos(this.NeckR4.xRot);
        this.RHead1.xRot = b;
        this.RHead2.xRot = b;
        this.RHead2.z = this.RHead1.z;
        this.RHead2.x = this.RHead1.x;
        this.RHead2.y = this.RHead1.y;
        this.RHead3.xRot = b;
        this.RHead3.z = this.RHead1.z;
        this.RHead3.x = this.RHead1.x;
        this.RHead3.y = this.RHead1.y;
        this.RHeadMane.xRot = 0.384F + b;
        this.RHeadMane.z = this.RHead1.z;
        this.RHeadMane.x = this.RHead1.x;
        this.RHeadMane.y = this.RHead1.y;
        this.RLEye.xRot = b;
        this.RLEye.z = this.RHead1.z;
        this.RLEye.x = this.RHead1.x;
        this.RLEye.y = this.RHead1.y;
        this.RREye.xRot = b;
        this.RREye.z = this.RHead1.z;
        this.RREye.x = this.RHead1.x;
        this.RREye.y = this.RHead1.y;
        this.RLNoseSpike.xRot = 0.244F + b;
        this.RLNoseSpike.z = this.RHead1.z;
        this.RLNoseSpike.x = this.RHead1.x;
        this.RLNoseSpike.y = this.RHead1.y;
        this.RRNoseSpike.xRot = 0.261F + b;
        this.RRNoseSpike.z = this.RHead1.z;
        this.RRNoseSpike.x = this.RHead1.x;
        this.RRNoseSpike.y = this.RHead1.y;
        this.RJaw1.y = this.RHead1.y + (float)Math.cos(this.RHead1.xRot) * 14.0F;
        this.RJaw1.z = this.NeckR4.z + -38.0F * (float)Math.cos(this.NeckR4.xRot);
        this.RJaw1.z = this.RJaw1.z + (float)Math.sin(this.RHead1.xRot) * 12.0F;
        this.RJaw1.x = (float)(this.RHead1.x - Math.sin(this.RHead1.yRot) * 7.0);
        this.RJaw2.z = this.RJaw1.z;
        this.RJaw2.x = this.RJaw1.x;
        this.RJaw2.y = this.RJaw1.y;
        this.RJaw3.z = this.RJaw1.z;
        this.RJaw3.x = this.RJaw1.x;
        this.RJaw3.y = this.RJaw1.y;
        this.RTooth1.z = this.RJaw1.z;
        this.RTooth1.x = this.RJaw1.x;
        this.RTooth1.y = this.RJaw1.y;
        this.RTooth2.z = this.RJaw1.z;
        this.RTooth2.x = this.RJaw1.x;
        this.RTooth2.y = this.RJaw1.y;
        this.RTooth3.z = this.RJaw1.z;
        this.RTooth3.x = this.RJaw1.x;
        this.RTooth3.y = this.RJaw1.y;
        this.RTooth4.z = this.RJaw1.z;
        this.RTooth4.x = this.RJaw1.x;
        this.RTooth4.y = this.RJaw1.y;

    }

}
