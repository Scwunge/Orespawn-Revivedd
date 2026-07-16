package danger.orespawn.client.model;

import danger.orespawn.entity.Leon;
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
 * Port of gold ModelLeon (1.7.10 ModelBase, tex 256x256, 98 cubes, dual ground/flight meshes).
 * Full gold func_78088_a animation in setupAnim.
 * Local LAYER_LOCATION; wingspeed default 0.22F matches ClientProxy.
 */
@OnlyIn(Dist.CLIENT)
public class ModelLeon extends HierarchicalModel<Leon> {
    /** Model layer location. */
    public static final ModelLayerLocation LAYER_LOCATION =
            new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "leon"), "main");

    private final float wingspeed;
    private final ModelPart root;
    private final ModelPart chest;
    private final ModelPart neck_1;
    private final ModelPart neck_2;
    private final ModelPart neck_3;
    private final ModelPart abdomen;
    private final ModelPart head;
    private final ModelPart upper_jaw;
    private final ModelPart bottom_jaw;
    private final ModelPart chest_ridge;
    private final ModelPart upper_sail_1;
    private final ModelPart upper_sail2_;
    private final ModelPart upper_sail3;
    private final ModelPart lower_sail1;
    private final ModelPart lower_sail2;
    private final ModelPart lower_sail_3;
    private final ModelPart eye_ridge_L;
    private final ModelPart eye_ridge_R;
    private final ModelPart anntena_1_L;
    private final ModelPart anntena_1_R;
    private final ModelPart anntena_2_L;
    private final ModelPart anntena_2_R;
    private final ModelPart arm_1_L;
    private final ModelPart arm_2_L;
    private final ModelPart wing_1_L;
    private final ModelPart wing_2_L;
    private final ModelPart arm_1_R;
    private final ModelPart arm_2_R;
    private final ModelPart wing_1_R;
    private final ModelPart wing_2_R;
    private final ModelPart leg_1_L;
    private final ModelPart leg_1_R;
    private final ModelPart leg_2_L;
    private final ModelPart leg_2_R;
    private final ModelPart footL;
    private final ModelPart footR;
    private final ModelPart wing_3_L;
    private final ModelPart wing_3_R;
    private final ModelPart wing_4_L;
    private final ModelPart wing_4_R;
    private final ModelPart claw_L;
    private final ModelPart claw_R;
    private final ModelPart claw_L2;
    private final ModelPart claw_R_2;
    private final ModelPart wing_5_L;
    private final ModelPart wing_6_L;
    private final ModelPart wing_7_L;
    private final ModelPart wing_5_R;
    private final ModelPart wing_6_R;
    private final ModelPart wing_7_R;
    private final ModelPart fchest;
    private final ModelPart fneck_1;
    private final ModelPart fneck_2;
    private final ModelPart fneck_3;
    private final ModelPart fabdomen;
    private final ModelPart fhead;
    private final ModelPart fupper_jaw;
    private final ModelPart fbottom_jaw;
    private final ModelPart fchest_ridge;
    private final ModelPart fupper_sail_1;
    private final ModelPart fupper_sail2_;
    private final ModelPart fupper_sail3;
    private final ModelPart flower_sail1;
    private final ModelPart flower_sail2;
    private final ModelPart flower_sail_3;
    private final ModelPart feye_ridge_L;
    private final ModelPart feye_ridge_R;
    private final ModelPart fanntena_1_L;
    private final ModelPart fanntena_1_R;
    private final ModelPart fanntena_2_L;
    private final ModelPart fanntena_2_R;
    private final ModelPart farm_1_L;
    private final ModelPart farm_2_L;
    private final ModelPart fwing_1_L;
    private final ModelPart fwing_2_L;
    private final ModelPart farm_1_R;
    private final ModelPart farm_2_R;
    private final ModelPart fwing_1_R;
    private final ModelPart fwing_2_R;
    private final ModelPart fleg_1_L;
    private final ModelPart fleg_1_R;
    private final ModelPart fleg_2_L;
    private final ModelPart fleg_2_R;
    private final ModelPart ffootL;
    private final ModelPart ffootR;
    private final ModelPart fwing_3_L;
    private final ModelPart fwing_3_R;
    private final ModelPart fwing_4_L;
    private final ModelPart fwing_4_R;
    private final ModelPart fclaw_L;
    private final ModelPart fclaw_R;
    private final ModelPart fclaw_L2;
    private final ModelPart fclaw_R_2;
    private final ModelPart fwing_5_L;
    private final ModelPart fwing_6_L;
    private final ModelPart fwing_7_L;
    private final ModelPart fwing_5_R;
    private final ModelPart fwing_6_R;
    private final ModelPart fwing_7_R;

    public ModelLeon(ModelPart root) {
        this(root, 0.22F);
    }

    public ModelLeon(ModelPart root, float wingspeed) {
        this.root = root;
        this.wingspeed = wingspeed;
        this.chest = root.getChild("chest");
        this.neck_1 = root.getChild("neck_1");
        this.neck_2 = root.getChild("neck_2");
        this.neck_3 = root.getChild("neck_3");
        this.abdomen = root.getChild("abdomen");
        this.head = root.getChild("head");
        this.upper_jaw = root.getChild("upper_jaw");
        this.bottom_jaw = root.getChild("bottom_jaw");
        this.chest_ridge = root.getChild("chest_ridge");
        this.upper_sail_1 = root.getChild("upper_sail_1");
        this.upper_sail2_ = root.getChild("upper_sail2_");
        this.upper_sail3 = root.getChild("upper_sail3");
        this.lower_sail1 = root.getChild("lower_sail1");
        this.lower_sail2 = root.getChild("lower_sail2");
        this.lower_sail_3 = root.getChild("lower_sail_3");
        this.eye_ridge_L = root.getChild("eye_ridge_L");
        this.eye_ridge_R = root.getChild("eye_ridge_R");
        this.anntena_1_L = root.getChild("anntena_1_L");
        this.anntena_1_R = root.getChild("anntena_1_R");
        this.anntena_2_L = root.getChild("anntena_2_L");
        this.anntena_2_R = root.getChild("anntena_2_R");
        this.arm_1_L = root.getChild("arm_1_L");
        this.arm_2_L = root.getChild("arm_2_L");
        this.wing_1_L = root.getChild("wing_1_L");
        this.wing_2_L = root.getChild("wing_2_L");
        this.arm_1_R = root.getChild("arm_1_R");
        this.arm_2_R = root.getChild("arm_2_R");
        this.wing_1_R = root.getChild("wing_1_R");
        this.wing_2_R = root.getChild("wing_2_R");
        this.leg_1_L = root.getChild("leg_1_L");
        this.leg_1_R = root.getChild("leg_1_R");
        this.leg_2_L = root.getChild("leg_2_L");
        this.leg_2_R = root.getChild("leg_2_R");
        this.footL = root.getChild("footL");
        this.footR = root.getChild("footR");
        this.wing_3_L = root.getChild("wing_3_L");
        this.wing_3_R = root.getChild("wing_3_R");
        this.wing_4_L = root.getChild("wing_4_L");
        this.wing_4_R = root.getChild("wing_4_R");
        this.claw_L = root.getChild("claw_L");
        this.claw_R = root.getChild("claw_R");
        this.claw_L2 = root.getChild("claw_L2");
        this.claw_R_2 = root.getChild("claw_R_2");
        this.wing_5_L = root.getChild("wing_5_L");
        this.wing_6_L = root.getChild("wing_6_L");
        this.wing_7_L = root.getChild("wing_7_L");
        this.wing_5_R = root.getChild("wing_5_R");
        this.wing_6_R = root.getChild("wing_6_R");
        this.wing_7_R = root.getChild("wing_7_R");
        this.fchest = root.getChild("fchest");
        this.fneck_1 = root.getChild("fneck_1");
        this.fneck_2 = root.getChild("fneck_2");
        this.fneck_3 = root.getChild("fneck_3");
        this.fabdomen = root.getChild("fabdomen");
        this.fhead = root.getChild("fhead");
        this.fupper_jaw = root.getChild("fupper_jaw");
        this.fbottom_jaw = root.getChild("fbottom_jaw");
        this.fchest_ridge = root.getChild("fchest_ridge");
        this.fupper_sail_1 = root.getChild("fupper_sail_1");
        this.fupper_sail2_ = root.getChild("fupper_sail2_");
        this.fupper_sail3 = root.getChild("fupper_sail3");
        this.flower_sail1 = root.getChild("flower_sail1");
        this.flower_sail2 = root.getChild("flower_sail2");
        this.flower_sail_3 = root.getChild("flower_sail_3");
        this.feye_ridge_L = root.getChild("feye_ridge_L");
        this.feye_ridge_R = root.getChild("feye_ridge_R");
        this.fanntena_1_L = root.getChild("fanntena_1_L");
        this.fanntena_1_R = root.getChild("fanntena_1_R");
        this.fanntena_2_L = root.getChild("fanntena_2_L");
        this.fanntena_2_R = root.getChild("fanntena_2_R");
        this.farm_1_L = root.getChild("farm_1_L");
        this.farm_2_L = root.getChild("farm_2_L");
        this.fwing_1_L = root.getChild("fwing_1_L");
        this.fwing_2_L = root.getChild("fwing_2_L");
        this.farm_1_R = root.getChild("farm_1_R");
        this.farm_2_R = root.getChild("farm_2_R");
        this.fwing_1_R = root.getChild("fwing_1_R");
        this.fwing_2_R = root.getChild("fwing_2_R");
        this.fleg_1_L = root.getChild("fleg_1_L");
        this.fleg_1_R = root.getChild("fleg_1_R");
        this.fleg_2_L = root.getChild("fleg_2_L");
        this.fleg_2_R = root.getChild("fleg_2_R");
        this.ffootL = root.getChild("ffootL");
        this.ffootR = root.getChild("ffootR");
        this.fwing_3_L = root.getChild("fwing_3_L");
        this.fwing_3_R = root.getChild("fwing_3_R");
        this.fwing_4_L = root.getChild("fwing_4_L");
        this.fwing_4_R = root.getChild("fwing_4_R");
        this.fclaw_L = root.getChild("fclaw_L");
        this.fclaw_R = root.getChild("fclaw_R");
        this.fclaw_L2 = root.getChild("fclaw_L2");
        this.fclaw_R_2 = root.getChild("fclaw_R_2");
        this.fwing_5_L = root.getChild("fwing_5_L");
        this.fwing_6_L = root.getChild("fwing_6_L");
        this.fwing_7_L = root.getChild("fwing_7_L");
        this.fwing_5_R = root.getChild("fwing_5_R");
        this.fwing_6_R = root.getChild("fwing_6_R");
        this.fwing_7_R = root.getChild("fwing_7_R");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        root.addOrReplaceChild(
                "chest",
                CubeListBuilder.create().texOffs(80, 0).addBox(-8.0F, -9.5F, -9.5F, 16.0F, 19.0F, 19.0F),
                PartPose.offsetAndRotation(0.0F, -2.0F, -7.0F, -0.4363323F, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "neck_1",
                CubeListBuilder.create().texOffs(106, 68).addBox(-5.5F, -7.0F, -9.0F, 11.0F, 14.0F, 11.0F),
                PartPose.offsetAndRotation(0.0F, -6.0F, -13.0F, -0.8726646F, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "neck_2",
                CubeListBuilder.create().texOffs(71, 69).addBox(-4.0F, -5.0F, -8.0F, 8.0F, 10.0F, 9.0F),
                PartPose.offsetAndRotation(0.0F, -12.0F, -17.0F, -1.064651F, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "neck_3",
                CubeListBuilder.create().texOffs(102, 94).addBox(-3.0F, -4.0F, -17.0F, 6.0F, 8.0F, 18.0F),
                PartPose.offsetAndRotation(0.0F, -19.0F, -21.0F, -1.029744F, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "abdomen",
                CubeListBuilder.create().texOffs(96, 39).addBox(-5.0F, -2.0F, 1.0F, 10.0F, 11.0F, 17.0F),
                PartPose.offsetAndRotation(0.0F, -5.0F, 4.0F, -0.6457718F, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "head",
                CubeListBuilder.create().texOffs(61, 49).addBox(-4.0F, -2.0F, -4.0F, 8.0F, 8.0F, 9.0F),
                PartPose.offsetAndRotation(0.0F, -32.0F, -29.0F, -1.413717F, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "upper_jaw",
                CubeListBuilder.create().texOffs(83, 89).addBox(-3.0F, 4.0F, -4.0F, 6.0F, 13.0F, 5.0F),
                PartPose.offsetAndRotation(0.0F, -32.0F, -29.0F, -1.37881F, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "bottom_jaw",
                CubeListBuilder.create().texOffs(85, 108).addBox(-2.5F, -1.0F, -1.5F, 5.0F, 12.0F, 3.0F),
                PartPose.offsetAndRotation(0.0F, -28.0F, -34.0F, -1.413717F, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "chest_ridge",
                CubeListBuilder.create().texOffs(113, 129).addBox(-2.0F, 7.0F, -3.0F, 4.0F, 3.0F, 17.0F),
                PartPose.offsetAndRotation(0.0F, -2.0F, -7.0F, -0.6283185F, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "upper_sail_1",
                CubeListBuilder.create().texOffs(76, 110).addBox(-1.0F, -17.0F, -16.0F, 2.0F, 14.0F, 2.0F),
                PartPose.offsetAndRotation(0.0F, -32.0F, -29.0F, 0.2443461F, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "upper_sail2_",
                CubeListBuilder.create().texOffs(63, 110).addBox(-0.5F, -15.0F, -16.0F, 1.0F, 12.0F, 5.0F),
                PartPose.offsetAndRotation(0.0F, -32.0F, -29.0F, 0.1396263F, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "upper_sail3",
                CubeListBuilder.create().texOffs(0, 82).addBox(0.0F, -1.5F, -18.0F, 0.0F, 9.0F, 13.0F),
                PartPose.offsetAndRotation(0.0F, -32.0F, -29.0F, -0.7504916F, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "lower_sail1",
                CubeListBuilder.create().texOffs(0, 2).addBox(-1.0F, 0.0F, -10.0F, 2.0F, 11.0F, 2.0F),
                PartPose.offsetAndRotation(0.0F, -28.0F, -34.0F, 0.1919862F, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "lower_sail2",
                CubeListBuilder.create().texOffs(52, 94).addBox(-0.5F, 0.5F, -9.0F, 1.0F, 9.0F, 4.0F),
                PartPose.offsetAndRotation(0.0F, -28.0F, -34.0F, 0.296706F, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "lower_sail_3",
                CubeListBuilder.create().texOffs(66, 90).addBox(0.0F, 1.5F, -4.0F, 0.0F, 9.0F, 7.0F),
                PartPose.offsetAndRotation(0.0F, -28.0F, -34.0F, -0.4886922F, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "eye_ridge_L",
                CubeListBuilder.create().texOffs(0, 68).addBox(0.0F, -4.0F, -5.0F, 5.0F, 2.0F, 11.0F),
                PartPose.offsetAndRotation(0.0F, -32.0F, -29.0F, 0.2094395F, 0.5585054F, 0.2268928F));
        root.addOrReplaceChild(
                "eye_ridge_R",
                CubeListBuilder.create().texOffs(0, 68).addBox(-5.0F, -4.0F, -5.0F, 5.0F, 2.0F, 11.0F),
                PartPose.offsetAndRotation(0.0F, -32.0F, -29.0F, 0.2094395F, -0.5585054F, -0.2268928F));
        root.addOrReplaceChild(
                "anntena_1_L",
                CubeListBuilder.create().texOffs(0, 40).addBox(3.0F, -4.2F, 5.0F, 2.0F, 2.0F, 5.0F),
                PartPose.offsetAndRotation(0.0F, -32.0F, -29.0F, 0.2443461F, 0.3665191F, 0.2268928F));
        root.addOrReplaceChild(
                "anntena_1_R",
                CubeListBuilder.create().texOffs(0, 40).addBox(-5.0F, -4.2F, 5.0F, 2.0F, 2.0F, 5.0F),
                PartPose.offsetAndRotation(0.0F, -32.0F, -29.0F, 0.2443461F, -0.3665191F, -0.2268928F));
        root.addOrReplaceChild(
                "anntena_2_L",
                CubeListBuilder.create().texOffs(46, 91).addBox(5.0F, -6.0F, 7.0F, 1.0F, 1.0F, 17.0F),
                PartPose.offsetAndRotation(0.0F, -32.0F, -29.0F, 0.0698132F, 0.1396263F, 0.2268928F));
        root.addOrReplaceChild(
                "anntena_2_R",
                CubeListBuilder.create().texOffs(46, 91).addBox(-6.0F, -6.0F, 7.0F, 1.0F, 1.0F, 17.0F),
                PartPose.offsetAndRotation(0.0F, -32.0F, -29.0F, 0.0698132F, -0.1396263F, -0.2268928F));
        root.addOrReplaceChild(
                "arm_1_L",
                CubeListBuilder.create().texOffs(77, 150).addBox(0.0F, -1.0F, -1.0F, 3.0F, 18.0F, 5.0F),
                PartPose.offsetAndRotation(8.0F, -8.0F, -10.0F, -0.0698132F, 0.0F, -0.7679449F));
        root.addOrReplaceChild(
                "arm_2_L",
                CubeListBuilder.create().texOffs(102, 150).addBox(-0.5F, 0.0F, -1.0F, 2.0F, 24.0F, 3.0F),
                PartPose.offsetAndRotation(20.0F, 3.0F, -10.0F, -0.4712389F, 0.0F, -0.4886922F));
        root.addOrReplaceChild(
                "wing_1_L",
                CubeListBuilder.create().texOffs(0, 33).addBox(1.5F, -1.0F, 3.0F, 1.0F, 19.0F, 15.0F),
                PartPose.offsetAndRotation(8.0F, -8.0F, -10.0F, -0.1745329F, -0.1919862F, -0.7504916F));
        root.addOrReplaceChild(
                "wing_2_L",
                CubeListBuilder.create().texOffs(33, 50).addBox(0.0F, -1.0F, 1.0F, 1.0F, 23.0F, 17.0F),
                PartPose.offsetAndRotation(20.0F, 3.0F, -10.0F, (float) (-Math.PI / 6), -0.0349066F, -0.4712389F));
        root.addOrReplaceChild(
                "arm_1_R",
                CubeListBuilder.create().texOffs(77, 127).addBox(-3.0F, -1.0F, -1.0F, 3.0F, 18.0F, 5.0F),
                PartPose.offsetAndRotation(-8.0F, -8.0F, -10.0F, -0.0698132F, 0.0F, 0.7679449F));
        root.addOrReplaceChild(
                "arm_2_R",
                CubeListBuilder.create().texOffs(102, 123).addBox(-1.5F, 0.0F, -1.0F, 2.0F, 24.0F, 3.0F),
                PartPose.offsetAndRotation(-20.0F, 3.0F, -10.0F, -0.4712389F, 0.0F, 0.4886922F));
        root.addOrReplaceChild(
                "wing_1_R",
                CubeListBuilder.create().texOffs(24, 150).addBox(-2.5F, -1.0F, 3.0F, 1.0F, 19.0F, 15.0F),
                PartPose.offsetAndRotation(-8.0F, -8.0F, -10.0F, -0.1745329F, 0.1919862F, 0.7504916F));
        root.addOrReplaceChild(
                "wing_2_R",
                CubeListBuilder.create().texOffs(150, 50).addBox(-1.0F, -1.0F, 0.0F, 1.0F, 23.0F, 17.0F),
                PartPose.offsetAndRotation(-20.0F, 3.0F, -10.0F, (float) (-Math.PI / 6), 0.0349066F, 0.4712389F));
        root.addOrReplaceChild(
                "leg_1_L",
                CubeListBuilder.create().texOffs(0, 104).addBox(0.0F, -3.0F, -4.0F, 3.0F, 15.0F, 7.0F),
                PartPose.offsetAndRotation(5.0F, 5.0F, 10.0F, -0.6108652F, 0.0F, -0.3316126F));
        root.addOrReplaceChild(
                "leg_1_R",
                CubeListBuilder.create().texOffs(0, 149).addBox(-3.0F, -3.0F, -4.0F, 3.0F, 15.0F, 7.0F),
                PartPose.offsetAndRotation(-6.0F, 5.0F, 10.0F, -0.6108652F, 0.0F, 0.3316126F));
        root.addOrReplaceChild(
                "leg_2_L",
                CubeListBuilder.create().texOffs(21, 108).addBox(1.0F, 0.0F, -3.0F, 2.0F, 14.0F, 4.0F),
                PartPose.offsetAndRotation(8.0F, 13.0F, 6.0F, 0.6108652F, 0.0F, -0.1745329F));
        root.addOrReplaceChild(
                "leg_2_R",
                CubeListBuilder.create().texOffs(21, 108).addBox(-2.0F, 0.0F, -3.0F, 2.0F, 14.0F, 4.0F),
                PartPose.offsetAndRotation(-10.0F, 13.0F, 6.0F, 0.6108652F, 0.0F, 0.1745329F));
        root.addOrReplaceChild(
                "footL",
                CubeListBuilder.create().texOffs(50, 29).addBox(-2.0F, -1.0F, -8.0F, 4.0F, 2.0F, 9.0F),
                PartPose.offset(12.0F, 24.0F, 11.0F));
        root.addOrReplaceChild(
                "footR",
                CubeListBuilder.create().texOffs(50, 29).addBox(-1.0F, 1.0F, -8.0F, 4.0F, 2.0F, 9.0F),
                PartPose.offset(-14.0F, 22.0F, 11.0F));
        root.addOrReplaceChild(
                "wing_3_L",
                CubeListBuilder.create().texOffs(0, 0).addBox(-7.5F, 0.0F, -5.0F, 16.0F, 1.0F, 26.0F),
                PartPose.offsetAndRotation(-5.0F, 0.0F, 12.0F, -0.4886922F, (float) (-Math.PI / 6), 0.4014257F));
        root.addOrReplaceChild(
                "wing_3_R",
                CubeListBuilder.create().texOffs(150, 0).addBox(-8.5F, 0.0F, -5.0F, 16.0F, 1.0F, 26.0F),
                PartPose.offsetAndRotation(4.0F, 0.0F, 12.0F, -0.4886922F, (float) (Math.PI / 6), -0.4014257F));
        root.addOrReplaceChild(
                "wing_4_L",
                CubeListBuilder.create().texOffs(8, 117).addBox(-1.5F, -0.5F, -2.0F, 3.0F, 1.0F, 31.0F),
                PartPose.offsetAndRotation(6.0F, 6.0F, 24.0F, -0.6283185F, -0.0174533F, 0.0F));
        root.addOrReplaceChild(
                "wing_4_R",
                CubeListBuilder.create().texOffs(8, 117).addBox(-1.5F, -0.5F, -2.0F, 3.0F, 1.0F, 31.0F),
                PartPose.offsetAndRotation(-7.0F, 6.0F, 24.0F, -0.6283185F, 0.0174533F, 0.0F));
        root.addOrReplaceChild(
                "claw_L",
                CubeListBuilder.create().texOffs(0, 129).addBox(0.0F, -1.0F, -9.0F, 1.0F, 2.0F, 10.0F),
                PartPose.offsetAndRotation(30.0F, 23.0F, -20.0F, 0.0F, 0.1570796F, 0.0F));
        root.addOrReplaceChild(
                "claw_R",
                CubeListBuilder.create().texOffs(0, 129).addBox(0.0F, -1.0F, -9.0F, 1.0F, 2.0F, 10.0F),
                PartPose.offsetAndRotation(-31.0F, 23.0F, -20.0F, 0.0F, -0.1570796F, 0.0F));
        root.addOrReplaceChild(
                "claw_L2",
                CubeListBuilder.create().texOffs(18, 38).addBox(0.0F, -2.5F, -6.0F, 1.0F, 2.0F, 7.0F),
                PartPose.offsetAndRotation(-30.0F, 23.0F, -28.0F, 0.5061455F, -0.2792527F, 0.0F));
        root.addOrReplaceChild(
                "claw_R_2",
                CubeListBuilder.create().texOffs(18, 38).addBox(-1.0F, -2.5F, -6.0F, 1.0F, 2.0F, 7.0F),
                PartPose.offsetAndRotation(30.0F, 23.0F, -28.0F, 0.5061455F, 0.2792527F, 0.0F));
        root.addOrReplaceChild(
                "wing_5_L",
                CubeListBuilder.create().texOffs(46, 10).addBox(-1.0F, -3.0F, -1.0F, 1.0F, 8.0F, 31.0F),
                PartPose.offsetAndRotation(31.0F, 21.0F, -19.0F, 0.6806784F, 0.0523599F, -0.2792527F));
        root.addOrReplaceChild(
                "wing_6_L",
                CubeListBuilder.create().texOffs(46, 10).addBox(-1.0F, -3.0F, -1.0F, 1.0F, 8.0F, 31.0F),
                PartPose.offsetAndRotation(31.0F, 21.0F, -19.0F, 0.4537856F, 0.2443461F, -0.3665191F));
        root.addOrReplaceChild(
                "wing_7_L",
                CubeListBuilder.create().texOffs(46, 10).addBox(-1.0F, -3.0F, -1.0F, 1.0F, 8.0F, 31.0F),
                PartPose.offsetAndRotation(-30.0F, 21.0F, -19.0F, 0.1396263F, -0.3316126F, 0.4014257F));
        root.addOrReplaceChild(
                "wing_5_R",
                CubeListBuilder.create().texOffs(46, 10).addBox(-1.0F, -3.0F, -1.0F, 1.0F, 8.0F, 31.0F),
                PartPose.offsetAndRotation(-30.0F, 21.0F, -19.0F, 0.6806784F, -0.0523599F, 0.2792527F));
        root.addOrReplaceChild(
                "wing_6_R",
                CubeListBuilder.create().texOffs(46, 10).addBox(-1.0F, -3.0F, -1.0F, 1.0F, 8.0F, 31.0F),
                PartPose.offsetAndRotation(-30.0F, 21.0F, -19.0F, 0.4537856F, -0.2443461F, 0.3665191F));
        root.addOrReplaceChild(
                "wing_7_R",
                CubeListBuilder.create().texOffs(46, 10).addBox(-1.0F, -3.0F, -1.0F, 1.0F, 8.0F, 31.0F),
                PartPose.offsetAndRotation(31.0F, 21.0F, -19.0F, 0.1396263F, 0.3316126F, -0.4014257F));
        root.addOrReplaceChild(
                "fchest",
                CubeListBuilder.create().texOffs(80, 0).addBox(-8.0F, -9.5F, -9.5F, 16.0F, 19.0F, 19.0F),
                PartPose.offsetAndRotation(0.0F, -2.0F, -7.0F, -0.4363323F, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "fneck_1",
                CubeListBuilder.create().texOffs(106, 68).addBox(-5.5F, -7.0F, -9.0F, 11.0F, 14.0F, 11.0F),
                PartPose.offsetAndRotation(0.0F, -6.0F, -13.0F, -0.8726646F, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "fneck_2",
                CubeListBuilder.create().texOffs(71, 69).addBox(-4.0F, -5.0F, -8.0F, 8.0F, 10.0F, 9.0F),
                PartPose.offsetAndRotation(0.0F, -12.0F, -17.0F, -1.064651F, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "fneck_3",
                CubeListBuilder.create().texOffs(102, 94).addBox(-3.0F, -4.0F, -17.0F, 6.0F, 8.0F, 18.0F),
                PartPose.offsetAndRotation(0.0F, -19.0F, -21.0F, -1.029744F, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "fabdomen",
                CubeListBuilder.create().texOffs(96, 39).addBox(-5.0F, -2.0F, 1.0F, 10.0F, 11.0F, 17.0F),
                PartPose.offsetAndRotation(0.0F, -5.0F, 4.0F, -0.6457718F, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "fhead",
                CubeListBuilder.create().texOffs(61, 49).addBox(-4.0F, -2.0F, -4.0F, 8.0F, 8.0F, 9.0F),
                PartPose.offsetAndRotation(0.0F, -32.0F, -29.0F, -1.413717F, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "fupper_jaw",
                CubeListBuilder.create().texOffs(83, 89).addBox(-3.0F, 4.0F, -4.0F, 6.0F, 13.0F, 5.0F),
                PartPose.offsetAndRotation(0.0F, -32.0F, -29.0F, -1.37881F, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "fbottom_jaw",
                CubeListBuilder.create().texOffs(85, 108).addBox(-2.5F, -1.0F, -1.5F, 5.0F, 12.0F, 3.0F),
                PartPose.offsetAndRotation(0.0F, -28.0F, -34.0F, -1.413717F, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "fchest_ridge",
                CubeListBuilder.create().texOffs(113, 129).addBox(-2.0F, 7.0F, -3.0F, 4.0F, 3.0F, 17.0F),
                PartPose.offsetAndRotation(0.0F, -2.0F, -7.0F, -0.6283185F, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "fupper_sail_1",
                CubeListBuilder.create().texOffs(76, 110).addBox(-1.0F, -17.0F, -16.0F, 2.0F, 14.0F, 2.0F),
                PartPose.offsetAndRotation(0.0F, -32.0F, -29.0F, 0.2443461F, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "fupper_sail2_",
                CubeListBuilder.create().texOffs(63, 110).addBox(-0.5F, -15.0F, -16.0F, 1.0F, 12.0F, 5.0F),
                PartPose.offsetAndRotation(0.0F, -32.0F, -29.0F, 0.1396263F, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "fupper_sail3",
                CubeListBuilder.create().texOffs(0, 82).addBox(0.0F, -1.5F, -18.0F, 0.0F, 9.0F, 13.0F),
                PartPose.offsetAndRotation(0.0F, -32.0F, -29.0F, -0.7504916F, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "flower_sail1",
                CubeListBuilder.create().texOffs(0, 2).addBox(-1.0F, 0.0F, -10.0F, 2.0F, 11.0F, 2.0F),
                PartPose.offsetAndRotation(0.0F, -28.0F, -34.0F, 0.1919862F, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "flower_sail2",
                CubeListBuilder.create().texOffs(52, 94).addBox(-0.5F, 0.5F, -9.0F, 1.0F, 9.0F, 4.0F),
                PartPose.offsetAndRotation(0.0F, -28.0F, -34.0F, 0.296706F, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "flower_sail_3",
                CubeListBuilder.create().texOffs(66, 90).addBox(0.0F, 1.5F, -4.0F, 0.0F, 9.0F, 7.0F),
                PartPose.offsetAndRotation(0.0F, -28.0F, -34.0F, -0.4886922F, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "feye_ridge_L",
                CubeListBuilder.create().texOffs(0, 68).addBox(0.0F, -4.0F, -5.0F, 5.0F, 2.0F, 11.0F),
                PartPose.offsetAndRotation(0.0F, -32.0F, -29.0F, 0.2094395F, 0.5585054F, 0.2268928F));
        root.addOrReplaceChild(
                "feye_ridge_R",
                CubeListBuilder.create().texOffs(0, 68).addBox(-5.0F, -4.0F, -5.0F, 5.0F, 2.0F, 11.0F),
                PartPose.offsetAndRotation(0.0F, -32.0F, -29.0F, 0.2094395F, -0.5585054F, -0.2268928F));
        root.addOrReplaceChild(
                "fanntena_1_L",
                CubeListBuilder.create().texOffs(0, 40).addBox(3.0F, -4.2F, 5.0F, 2.0F, 2.0F, 5.0F),
                PartPose.offsetAndRotation(0.0F, -32.0F, -29.0F, 0.2443461F, 0.3665191F, 0.2268928F));
        root.addOrReplaceChild(
                "fanntena_1_R",
                CubeListBuilder.create().texOffs(0, 40).addBox(-5.0F, -4.2F, 5.0F, 2.0F, 2.0F, 5.0F),
                PartPose.offsetAndRotation(0.0F, -32.0F, -29.0F, 0.2443461F, -0.3665191F, -0.2268928F));
        root.addOrReplaceChild(
                "fanntena_2_L",
                CubeListBuilder.create().texOffs(46, 91).addBox(5.0F, -6.0F, 7.0F, 1.0F, 1.0F, 17.0F),
                PartPose.offsetAndRotation(0.0F, -32.0F, -29.0F, 0.0698132F, 0.1396263F, 0.2268928F));
        root.addOrReplaceChild(
                "fanntena_2_R",
                CubeListBuilder.create().texOffs(46, 91).addBox(-6.0F, -6.0F, 7.0F, 1.0F, 1.0F, 17.0F),
                PartPose.offsetAndRotation(0.0F, -32.0F, -29.0F, 0.0698132F, -0.1396263F, -0.2268928F));
        root.addOrReplaceChild(
                "farm_1_L",
                CubeListBuilder.create().texOffs(77, 150).addBox(0.0F, -1.0F, -1.0F, 3.0F, 18.0F, 5.0F),
                PartPose.offsetAndRotation(8.0F, -8.0F, -10.0F, -0.0698132F, 0.0F, -0.7679449F));
        root.addOrReplaceChild(
                "farm_2_L",
                CubeListBuilder.create().texOffs(102, 150).addBox(-0.5F, 0.0F, -1.0F, 2.0F, 24.0F, 3.0F),
                PartPose.offsetAndRotation(20.0F, 3.0F, -10.0F, -0.4712389F, 0.0F, -0.4886922F));
        root.addOrReplaceChild(
                "fwing_1_L",
                CubeListBuilder.create().texOffs(0, 33).addBox(1.5F, -1.0F, 3.0F, 1.0F, 19.0F, 15.0F),
                PartPose.offsetAndRotation(8.0F, -8.0F, -10.0F, -0.1745329F, -0.1919862F, -0.7504916F));
        root.addOrReplaceChild(
                "fwing_2_L",
                CubeListBuilder.create().texOffs(33, 50).addBox(0.0F, -1.0F, 1.0F, 1.0F, 23.0F, 17.0F),
                PartPose.offsetAndRotation(20.0F, 3.0F, -10.0F, (float) (-Math.PI / 6), -0.0349066F, -0.4712389F));
        root.addOrReplaceChild(
                "farm_1_R",
                CubeListBuilder.create().texOffs(77, 127).addBox(-3.0F, -1.0F, -1.0F, 3.0F, 18.0F, 5.0F),
                PartPose.offsetAndRotation(-8.0F, -8.0F, -10.0F, -0.0698132F, 0.0F, 0.7679449F));
        root.addOrReplaceChild(
                "farm_2_R",
                CubeListBuilder.create().texOffs(102, 123).addBox(-1.5F, 0.0F, -1.0F, 2.0F, 24.0F, 3.0F),
                PartPose.offsetAndRotation(-20.0F, 3.0F, -10.0F, -0.4712389F, 0.0F, 0.4886922F));
        root.addOrReplaceChild(
                "fwing_1_R",
                CubeListBuilder.create().texOffs(24, 150).addBox(-2.5F, -1.0F, 3.0F, 1.0F, 19.0F, 15.0F),
                PartPose.offsetAndRotation(-8.0F, -8.0F, -10.0F, -0.1745329F, 0.1919862F, 0.7504916F));
        root.addOrReplaceChild(
                "fwing_2_R",
                CubeListBuilder.create().texOffs(150, 50).addBox(-1.0F, -1.0F, 0.0F, 1.0F, 23.0F, 17.0F),
                PartPose.offsetAndRotation(-20.0F, 3.0F, -10.0F, (float) (-Math.PI / 6), 0.0349066F, 0.4712389F));
        root.addOrReplaceChild(
                "fleg_1_L",
                CubeListBuilder.create().texOffs(0, 104).addBox(0.0F, -3.0F, -4.0F, 3.0F, 15.0F, 7.0F),
                PartPose.offsetAndRotation(5.0F, 5.0F, 10.0F, -0.6108652F, 0.0F, -0.3316126F));
        root.addOrReplaceChild(
                "fleg_1_R",
                CubeListBuilder.create().texOffs(0, 149).addBox(-3.0F, -3.0F, -4.0F, 3.0F, 15.0F, 7.0F),
                PartPose.offsetAndRotation(-6.0F, 5.0F, 10.0F, -0.6108652F, 0.0F, 0.3316126F));
        root.addOrReplaceChild(
                "fleg_2_L",
                CubeListBuilder.create().texOffs(21, 108).addBox(1.0F, 0.0F, -3.0F, 2.0F, 14.0F, 4.0F),
                PartPose.offsetAndRotation(8.0F, 13.0F, 6.0F, 0.6108652F, 0.0F, -0.1745329F));
        root.addOrReplaceChild(
                "fleg_2_R",
                CubeListBuilder.create().texOffs(21, 108).addBox(-2.0F, 0.0F, -3.0F, 2.0F, 14.0F, 4.0F),
                PartPose.offsetAndRotation(-10.0F, 13.0F, 6.0F, 0.6108652F, 0.0F, 0.1745329F));
        root.addOrReplaceChild(
                "ffootL",
                CubeListBuilder.create().texOffs(50, 29).addBox(-2.0F, -1.0F, -8.0F, 4.0F, 2.0F, 9.0F),
                PartPose.offset(12.0F, 24.0F, 11.0F));
        root.addOrReplaceChild(
                "ffootR",
                CubeListBuilder.create().texOffs(50, 29).addBox(-1.0F, 1.0F, -8.0F, 4.0F, 2.0F, 9.0F),
                PartPose.offset(-14.0F, 22.0F, 11.0F));
        root.addOrReplaceChild(
                "fwing_3_L",
                CubeListBuilder.create().texOffs(0, 0).addBox(-7.5F, 0.0F, -5.0F, 16.0F, 1.0F, 26.0F),
                PartPose.offsetAndRotation(-5.0F, 0.0F, 12.0F, -0.4886922F, (float) (-Math.PI / 6), 0.4014257F));
        root.addOrReplaceChild(
                "fwing_3_R",
                CubeListBuilder.create().texOffs(150, 0).addBox(-8.5F, 0.0F, -5.0F, 16.0F, 1.0F, 26.0F),
                PartPose.offsetAndRotation(4.0F, 0.0F, 12.0F, -0.4886922F, (float) (Math.PI / 6), -0.4014257F));
        root.addOrReplaceChild(
                "fwing_4_L",
                CubeListBuilder.create().texOffs(8, 117).addBox(-1.5F, -0.5F, -2.0F, 3.0F, 1.0F, 31.0F),
                PartPose.offsetAndRotation(6.0F, 6.0F, 24.0F, -0.6283185F, -0.0174533F, 0.0F));
        root.addOrReplaceChild(
                "fwing_4_R",
                CubeListBuilder.create().texOffs(8, 117).addBox(-1.5F, -0.5F, -2.0F, 3.0F, 1.0F, 31.0F),
                PartPose.offsetAndRotation(-7.0F, 6.0F, 24.0F, -0.6283185F, 0.0174533F, 0.0F));
        root.addOrReplaceChild(
                "fclaw_L",
                CubeListBuilder.create().texOffs(0, 129).addBox(0.0F, -1.0F, -9.0F, 1.0F, 2.0F, 10.0F),
                PartPose.offsetAndRotation(30.0F, 23.0F, -20.0F, 0.0F, 0.1570796F, 0.0F));
        root.addOrReplaceChild(
                "fclaw_R",
                CubeListBuilder.create().texOffs(0, 129).addBox(0.0F, -1.0F, -9.0F, 1.0F, 2.0F, 10.0F),
                PartPose.offsetAndRotation(-31.0F, 23.0F, -20.0F, 0.0F, -0.1570796F, 0.0F));
        root.addOrReplaceChild(
                "fclaw_L2",
                CubeListBuilder.create().texOffs(18, 38).addBox(0.0F, -2.5F, -6.0F, 1.0F, 2.0F, 7.0F),
                PartPose.offsetAndRotation(-30.0F, 23.0F, -28.0F, 0.5061455F, -0.2792527F, 0.0F));
        root.addOrReplaceChild(
                "fclaw_R_2",
                CubeListBuilder.create().texOffs(18, 38).addBox(-1.0F, -2.5F, -6.0F, 1.0F, 2.0F, 7.0F),
                PartPose.offsetAndRotation(30.0F, 23.0F, -28.0F, 0.5061455F, 0.2792527F, 0.0F));
        root.addOrReplaceChild(
                "fwing_5_L",
                CubeListBuilder.create().texOffs(46, 10).addBox(-1.0F, -3.0F, -1.0F, 1.0F, 8.0F, 31.0F),
                PartPose.offsetAndRotation(31.0F, 21.0F, -19.0F, 0.6806784F, 0.0523599F, -0.2792527F));
        root.addOrReplaceChild(
                "fwing_6_L",
                CubeListBuilder.create().texOffs(46, 10).addBox(-1.0F, -3.0F, -1.0F, 1.0F, 8.0F, 31.0F),
                PartPose.offsetAndRotation(31.0F, 21.0F, -19.0F, 0.4537856F, 0.2443461F, -0.3665191F));
        root.addOrReplaceChild(
                "fwing_7_L",
                CubeListBuilder.create().texOffs(46, 10).addBox(-1.0F, -3.0F, -1.0F, 1.0F, 8.0F, 31.0F),
                PartPose.offsetAndRotation(-30.0F, 21.0F, -19.0F, 0.1396263F, -0.3316126F, 0.4014257F));
        root.addOrReplaceChild(
                "fwing_5_R",
                CubeListBuilder.create().texOffs(46, 10).addBox(-1.0F, -3.0F, -1.0F, 1.0F, 8.0F, 31.0F),
                PartPose.offsetAndRotation(-30.0F, 21.0F, -19.0F, 0.6806784F, -0.0523599F, 0.2792527F));
        root.addOrReplaceChild(
                "fwing_6_R",
                CubeListBuilder.create().texOffs(46, 10).addBox(-1.0F, -3.0F, -1.0F, 1.0F, 8.0F, 31.0F),
                PartPose.offsetAndRotation(-30.0F, 21.0F, -19.0F, 0.4537856F, -0.2443461F, 0.3665191F));
        root.addOrReplaceChild(
                "fwing_7_R",
                CubeListBuilder.create().texOffs(46, 10).addBox(-1.0F, -3.0F, -1.0F, 1.0F, 8.0F, 31.0F),
                PartPose.offsetAndRotation(31.0F, 21.0F, -19.0F, 0.1396263F, 0.3316126F, -0.4014257F));

        return LayerDefinition.create(mesh, 256, 256);
    }

    @Override
    public ModelPart root() {
        return this.root;
    }

    private void setPoseVisible(boolean flying) {
        this.chest.visible = !flying;
        this.neck_1.visible = !flying;
        this.neck_2.visible = !flying;
        this.neck_3.visible = !flying;
        this.abdomen.visible = !flying;
        this.head.visible = !flying;
        this.upper_jaw.visible = !flying;
        this.bottom_jaw.visible = !flying;
        this.chest_ridge.visible = !flying;
        this.upper_sail_1.visible = !flying;
        this.upper_sail2_.visible = !flying;
        this.upper_sail3.visible = !flying;
        this.lower_sail1.visible = !flying;
        this.lower_sail2.visible = !flying;
        this.lower_sail_3.visible = !flying;
        this.eye_ridge_L.visible = !flying;
        this.eye_ridge_R.visible = !flying;
        this.anntena_1_L.visible = !flying;
        this.anntena_1_R.visible = !flying;
        this.anntena_2_L.visible = !flying;
        this.anntena_2_R.visible = !flying;
        this.arm_1_L.visible = !flying;
        this.arm_2_L.visible = !flying;
        this.wing_1_L.visible = !flying;
        this.wing_2_L.visible = !flying;
        this.arm_1_R.visible = !flying;
        this.arm_2_R.visible = !flying;
        this.wing_1_R.visible = !flying;
        this.wing_2_R.visible = !flying;
        this.leg_1_L.visible = !flying;
        this.leg_1_R.visible = !flying;
        this.leg_2_L.visible = !flying;
        this.leg_2_R.visible = !flying;
        this.footL.visible = !flying;
        this.footR.visible = !flying;
        this.wing_3_L.visible = !flying;
        this.wing_3_R.visible = !flying;
        this.wing_4_L.visible = !flying;
        this.wing_4_R.visible = !flying;
        this.claw_L.visible = !flying;
        this.claw_R.visible = !flying;
        this.claw_L2.visible = !flying;
        this.claw_R_2.visible = !flying;
        this.wing_5_L.visible = !flying;
        this.wing_6_L.visible = !flying;
        this.wing_7_L.visible = !flying;
        this.wing_5_R.visible = !flying;
        this.wing_6_R.visible = !flying;
        this.wing_7_R.visible = !flying;
        this.fchest.visible = flying;
        this.fneck_1.visible = flying;
        this.fneck_2.visible = flying;
        this.fneck_3.visible = flying;
        this.fabdomen.visible = flying;
        this.fhead.visible = flying;
        this.fupper_jaw.visible = flying;
        this.fbottom_jaw.visible = flying;
        this.fchest_ridge.visible = flying;
        this.fupper_sail_1.visible = flying;
        this.fupper_sail2_.visible = flying;
        this.fupper_sail3.visible = flying;
        this.flower_sail1.visible = flying;
        this.flower_sail2.visible = flying;
        this.flower_sail_3.visible = flying;
        this.feye_ridge_L.visible = flying;
        this.feye_ridge_R.visible = flying;
        this.fanntena_1_L.visible = flying;
        this.fanntena_1_R.visible = flying;
        this.fanntena_2_L.visible = flying;
        this.fanntena_2_R.visible = flying;
        this.farm_1_L.visible = flying;
        this.farm_2_L.visible = flying;
        this.fwing_1_L.visible = flying;
        this.fwing_2_L.visible = flying;
        this.farm_1_R.visible = flying;
        this.farm_2_R.visible = flying;
        this.fwing_1_R.visible = flying;
        this.fwing_2_R.visible = flying;
        this.fleg_1_L.visible = flying;
        this.fleg_1_R.visible = flying;
        this.fleg_2_L.visible = flying;
        this.fleg_2_R.visible = flying;
        this.ffootL.visible = flying;
        this.ffootR.visible = flying;
        this.fwing_3_L.visible = flying;
        this.fwing_3_R.visible = flying;
        this.fwing_4_L.visible = flying;
        this.fwing_4_R.visible = flying;
        this.fclaw_L.visible = flying;
        this.fclaw_R.visible = flying;
        this.fclaw_L2.visible = flying;
        this.fclaw_R_2.visible = flying;
        this.fwing_5_L.visible = flying;
        this.fwing_6_L.visible = flying;
        this.fwing_7_L.visible = flying;
        this.fwing_5_R.visible = flying;
        this.fwing_6_R.visible = flying;
        this.fwing_7_R.visible = flying;
    }

    @Override
    public void setupAnim(Leon e, float f, float f1, float f2, float f3, float f4) {
        // gold: f=limbSwing, f1=limbSwingAmount, f2=ageInTicks, f3=netHeadYaw, f4=headPitch
           RenderInfo r = null;
           float newangle = 0.0F;
           float newangle2 = 0.0F;
           float newangle3 = 0.0F;
           float spd = 1.0F;
           float amp = 1.0F;
           if (f1 > 0.1) {
              newangle = Mth.cos(f2 * 1.8F * this.wingspeed) * (float) Math.PI * 0.25F * f1;
              newangle2 = Mth.cos(f2 * 0.9F * this.wingspeed) * (float) Math.PI * 0.25F * f1;
           } else {
              newangle = 0.0F;
              newangle2 = Mth.cos(f2 * 0.9F * this.wingspeed) * (float) Math.PI * 0.02F;
              if (e.isOreSpawnSitting()) {
                 newangle2 = 0.0F;
              }
           }
           if (e.getActivity() == 0) {
              this.leg_1_L.xRot = -0.611F + newangle;
              this.leg_1_R.xRot = -0.611F - newangle;
              this.leg_2_L.xRot = 0.611F + newangle;
              this.leg_2_L.z = (float)(this.leg_1_L.z + Math.sin(this.leg_1_L.xRot) * 9.0);
              this.leg_2_L.y = (float)(this.leg_1_L.y + Math.cos(this.leg_1_L.xRot) * 9.0);
              this.leg_2_R.xRot = 0.611F - newangle;
              this.leg_2_R.z = (float)(this.leg_1_R.z + Math.sin(this.leg_1_R.xRot) * 9.0);
              this.leg_2_R.y = (float)(this.leg_1_R.y + Math.cos(this.leg_1_R.xRot) * 9.0);
              this.footL.z = (float)(this.leg_2_L.z + Math.sin(this.leg_2_L.xRot) * 13.0);
              this.footL.y = (float)(this.leg_2_L.y + Math.cos(this.leg_2_L.xRot) * 13.0);
              this.footR.z = (float)(this.leg_2_R.z + Math.sin(this.leg_2_R.xRot) * 11.0);
              this.footR.y = (float)(this.leg_2_R.y + Math.cos(this.leg_2_R.xRot) * 11.0);
              this.wing_3_R.yRot = 0.523F - newangle / 10.0F;
              this.wing_3_L.yRot = -0.523F - newangle / 10.0F;
              newangle /= 2.0F;
              this.arm_1_L.xRot = -0.07F - newangle;
              this.arm_1_R.xRot = -0.07F + newangle;
              this.wing_1_L.xRot = -0.17F - newangle;
              this.wing_1_R.xRot = -0.17F + newangle;
              this.arm_2_L.xRot = -0.471F - newangle;
              this.wing_2_L.xRot = -0.523F - newangle;
              this.arm_2_L.z = this.wing_2_L.z = (float)(this.arm_1_L.z + Math.sin(this.arm_1_L.xRot) * 11.0);
              this.arm_2_L.y = this.wing_2_L.y = (float)(this.arm_1_L.y + Math.cos(this.arm_1_L.xRot) * 11.0);
              this.wing_5_L.xRot = 0.68F + newangle2 / 2.0F;
              this.wing_6_L.xRot = 0.453F + newangle2 / 4.0F;
              this.wing_7_R.xRot = 0.119F + newangle2 / 8.0F;
              this.wing_5_L.z = (float)(this.arm_2_L.z + Math.sin(this.arm_2_L.xRot) * 20.0);
              this.wing_5_L.y = (float)(this.arm_2_L.y + Math.cos(this.arm_2_L.xRot) * 20.0);
              this.wing_6_L.z = this.wing_5_L.z;
              this.wing_6_L.y = this.wing_5_L.y;
              this.wing_7_R.z = this.wing_5_L.z;
              this.wing_7_R.y = this.wing_5_L.y;
              this.claw_L.z = this.wing_5_L.z - 1.0F;
              this.claw_R_2.z = this.wing_5_L.z - 9.0F;
              this.claw_L.y = this.wing_5_L.y + 2.0F;
              this.claw_R_2.y = this.wing_5_L.y + 2.0F;
              this.arm_2_R.xRot = -0.471F + newangle;
              this.wing_2_R.xRot = -0.523F + newangle;
              this.arm_2_R.z = this.wing_2_R.z = (float)(this.arm_1_R.z + Math.sin(this.arm_1_R.xRot) * 11.0);
              this.arm_2_R.y = this.wing_2_R.y = (float)(this.arm_1_R.y + Math.cos(this.arm_1_R.xRot) * 11.0);
              this.wing_5_R.xRot = 0.68F + newangle2 / 2.0F;
              this.wing_6_R.xRot = 0.453F + newangle2 / 4.0F;
              this.wing_7_L.xRot = 0.119F + newangle2 / 8.0F;
              this.wing_5_R.z = (float)(this.arm_2_R.z + Math.sin(this.arm_2_R.xRot) * 20.0);
              this.wing_5_R.y = (float)(this.arm_2_R.y + Math.cos(this.arm_2_R.xRot) * 20.0);
              this.wing_6_R.z = this.wing_5_R.z;
              this.wing_6_R.y = this.wing_5_R.y;
              this.wing_7_L.z = this.wing_5_R.z;
              this.wing_7_L.y = this.wing_5_R.y;
              this.claw_R.z = this.wing_5_R.z - 1.0F;
              this.claw_L2.z = this.wing_5_R.z - 9.0F;
              this.claw_R.y = this.wing_5_R.y + 2.0F;
              this.claw_L2.y = this.wing_5_R.y + 2.0F;
              newangle2 = Mth.cos(f2 * 0.6F * this.wingspeed) * (float) Math.PI * 0.02F;
              this.chest.xRot = -0.436F + newangle2 / 8.0F;
              this.chest_ridge.xRot = this.chest.xRot;
              this.bottom_jaw.xRot = -1.308F + newangle2 / 2.0F;
              this.lower_sail1.xRot = 0.297F + newangle2 / 2.0F;
              this.lower_sail2.xRot = 0.384F + newangle2 / 2.0F;
              this.lower_sail_3.xRot = -0.384F + newangle2 / 2.0F;
              newangle = (float)Math.toRadians(f3) * 0.5F;
              this.head.yRot = this.upper_jaw.yRot = this.upper_sail_1.yRot = this.upper_sail2_.yRot = this.upper_sail3.yRot = newangle;
              this.eye_ridge_L.yRot = 0.558F + newangle;
              this.anntena_1_L.yRot = 0.366F + newangle;
              this.anntena_2_L.yRot = 0.139F + newangle;
              this.eye_ridge_R.yRot = -0.558F + newangle;
              this.anntena_1_R.yRot = -0.366F + newangle;
              this.anntena_2_R.yRot = -0.139F + newangle;
              this.bottom_jaw.yRot = this.lower_sail1.yRot = this.lower_sail2.yRot = this.lower_sail_3.yRot = newangle;
              this.bottom_jaw.z = (float)(this.head.z - Math.cos(newangle) * 5.0);
              this.bottom_jaw.x = (float)(this.head.x - Math.sin(newangle) * 5.0);
           } else {
              if (e.getAttacking() != 0) {
                 spd = 1.7F;
                 amp = 1.4F;
              }
              newangle2 = Mth.cos(f2 * 1.6F * this.wingspeed * spd) * (float) Math.PI * 0.06F;
              this.fchest.xRot = newangle2 / 8.0F;
              this.fchest_ridge.xRot = -0.18F + this.fchest.xRot;
              if (e.getBeingRidden() == 0) {
                 this.fchest.y = (float)(-2.0 + Math.sin(newangle2) * 10.0 * amp);
              } else {
                 this.fchest.y = -2.0F;
              }
              this.fchest_ridge.y = this.fchest.y;
              this.fabdomen.xRot = 0.0F;
              this.fabdomen.z = (float)(this.fchest.z + Math.cos(this.fchest.xRot) * 8.0);
              this.fabdomen.y = (float)(this.fchest.y - Math.sin(this.fchest.xRot) * 8.0 - 6.0);
              this.fwing_3_R.y = this.fabdomen.y;
              this.fwing_3_L.y = this.fabdomen.y;
              this.fwing_3_R.xRot = this.fwing_3_R.zRot = 0.0F;
              this.fwing_3_L.xRot = this.fwing_3_L.zRot = 0.0F;
              this.fwing_3_R.yRot = 0.785F;
              this.fwing_3_L.yRot = -0.785F;
              this.fwing_4_R.y = this.fabdomen.y + 0.55F;
              this.fwing_4_L.y = this.fabdomen.y + 0.55F;
              this.fwing_4_R.z = this.fabdomen.z + 26.0F;
              this.fwing_4_L.z = this.fabdomen.z + 26.0F;
              this.fwing_4_R.x = this.fabdomen.z + 8.0F;
              this.fwing_4_L.x = this.fabdomen.z - 9.0F;
              this.fwing_4_R.xRot = newangle2 / 10.0F;
              this.fwing_4_L.xRot = -newangle2 / 10.0F;
              if (e.getAttacking() == 0) {
                 newangle = (float) (Math.PI / 2);
                 this.fleg_1_L.y = this.fabdomen.y + 5.0F;
                 this.fleg_1_R.y = this.fabdomen.y + 5.0F;
                 this.fleg_1_L.xRot = -0.1F + newangle;
                 this.fleg_1_R.xRot = -0.1F + newangle;
                 this.fleg_2_L.xRot = 0.1F + newangle;
                 this.fleg_2_L.z = (float)(this.fleg_1_L.z + Math.sin(this.fleg_1_L.xRot) * 9.0);
                 this.fleg_2_L.y = (float)(this.fleg_1_L.y + Math.cos(this.fleg_1_L.xRot) * 9.0);
                 this.fleg_2_R.xRot = 0.1F + newangle;
                 this.fleg_2_R.z = (float)(this.fleg_1_R.z + Math.sin(this.fleg_1_R.xRot) * 9.0);
                 this.fleg_2_R.y = (float)(this.fleg_1_R.y + Math.cos(this.fleg_1_R.xRot) * 9.0);
                 this.ffootL.z = (float)(this.fleg_2_L.z + Math.sin(this.fleg_2_L.xRot) * 13.0);
                 this.ffootL.y = (float)(this.fleg_2_L.y + Math.cos(this.fleg_2_L.xRot) * 13.0);
                 this.ffootR.z = (float)(this.fleg_2_R.z + Math.sin(this.fleg_2_R.xRot) * 11.0);
                 this.ffootR.y = (float)(this.fleg_2_R.y + Math.cos(this.fleg_2_R.xRot) * 11.0);
                 this.ffootL.xRot = (float) Math.PI;
                 this.ffootR.xRot = (float) Math.PI;
                 this.fleg_2_L.x = this.fleg_1_L.x;
                 this.ffootL.x = this.fleg_1_L.x;
                 this.fleg_2_R.x = this.fleg_1_R.x;
                 this.ffootR.x = this.fleg_1_R.x;
              } else {
                 newangle = (float) (-Math.PI / 4);
                 newangle3 = Mth.cos(f2 * 3.6F * this.wingspeed) * (float) Math.PI * 0.1F;
                 this.fleg_1_L.y = this.fabdomen.y + 5.0F;
                 this.fleg_1_R.y = this.fabdomen.y + 5.0F;
                 this.fleg_1_L.xRot = -0.1F + newangle + newangle3;
                 this.fleg_1_R.xRot = -0.1F + newangle - newangle3;
                 this.fleg_2_L.xRot = 0.2F + newangle + newangle3 * 3.0F / 2.0F;
                 this.fleg_2_L.z = (float)(this.fleg_1_L.z + Math.sin(this.fleg_1_L.xRot) * 9.0);
                 this.fleg_2_L.y = (float)(this.fleg_1_L.y + Math.cos(this.fleg_1_L.xRot) * 9.0);
                 this.fleg_2_R.xRot = 0.2F + newangle - newangle3 * 3.0F / 2.0F;
                 this.fleg_2_R.z = (float)(this.fleg_1_R.z + Math.sin(this.fleg_1_R.xRot) * 9.0);
                 this.fleg_2_R.y = (float)(this.fleg_1_R.y + Math.cos(this.fleg_1_R.xRot) * 9.0);
                 this.ffootL.z = (float)(this.fleg_2_L.z + Math.sin(this.fleg_2_L.xRot) * 13.0);
                 this.ffootL.y = (float)(this.fleg_2_L.y + Math.cos(this.fleg_2_L.xRot) * 13.0);
                 this.ffootR.z = (float)(this.fleg_2_R.z + Math.sin(this.fleg_2_R.xRot) * 11.0);
                 this.ffootR.y = (float)(this.fleg_2_R.y + Math.cos(this.fleg_2_R.xRot) * 11.0);
                 this.ffootL.xRot = (float) (-Math.PI / 4) + newangle3 * 2.0F;
                 this.ffootR.xRot = (float) (-Math.PI / 4) - newangle3 * 2.0F;
                 this.fleg_2_L.x = 7.0F;
                 this.ffootL.x = 11.0F;
                 this.fleg_2_R.x = -9.0F;
                 this.ffootR.x = -13.0F;
              }
              newangle = Mth.cos(f2 * 1.6F * this.wingspeed * spd) * (float) Math.PI * 0.26F * amp;
              this.farm_1_L.zRot = (float)((-Math.PI / 2) - newangle);
              this.farm_1_R.zRot = (float)((Math.PI / 2) + newangle);
              this.fwing_1_L.zRot = (float)((-Math.PI / 2) - newangle);
              this.fwing_1_R.zRot = (float)((Math.PI / 2) + newangle);
              this.farm_2_L.zRot = (float)((-Math.PI / 2) - newangle * 1.3F);
              this.fwing_2_L.zRot = (float)((-Math.PI / 2) - newangle * 1.3F);
              this.farm_2_L.x = this.fwing_2_L.x = (float)(this.farm_1_L.x + Math.cos(newangle) * 14.0);
              this.farm_2_L.y = this.fwing_2_L.y = (float)(this.farm_1_L.y - Math.sin(newangle) * 14.0);
              this.fwing_5_L.x = (float)(this.farm_2_L.x + Math.cos(newangle * 1.3F) * 20.0);
              this.fwing_5_L.y = (float)(this.farm_2_L.y - Math.sin(newangle * 1.3F) * 20.0);
              this.fwing_6_L.x = this.fwing_5_L.x;
              this.fwing_6_L.y = this.fwing_5_L.y;
              this.fwing_7_R.x = this.fwing_5_L.x;
              this.fwing_7_R.y = this.fwing_5_L.y;
              this.fclaw_L.x = this.fwing_5_L.x;
              this.fclaw_R_2.x = this.fwing_5_L.x;
              this.fclaw_L.y = this.fwing_5_L.y;
              this.fclaw_R_2.y = this.fwing_5_L.y;
              this.fwing_5_L.zRot = (float)((-Math.PI / 2) - newangle * 1.65F);
              this.fwing_6_L.zRot = (float)((-Math.PI / 2) - newangle * 1.65F);
              this.fwing_7_R.zRot = (float)((-Math.PI / 2) - newangle * 1.65F);
              this.fwing_7_R.xRot = (float) (-Math.PI / 2);
              this.fwing_6_L.xRot = (float) (-Math.PI * 3.0 / 8.0);
              this.fwing_5_L.xRot = (float) (-Math.PI / 4);
              this.farm_2_R.zRot = (float)((Math.PI / 2) + newangle * 1.3F);
              this.fwing_2_R.zRot = (float)((Math.PI / 2) + newangle * 1.3F);
              this.farm_2_R.x = this.fwing_2_R.x = (float)(this.farm_1_R.x - Math.cos(newangle) * 14.0);
              this.farm_2_R.y = this.fwing_2_R.y = (float)(this.farm_1_R.y - Math.sin(newangle) * 14.0);
              this.fwing_5_R.x = (float)(this.farm_2_R.x - Math.cos(newangle * 1.3F) * 20.0);
              this.fwing_5_R.y = (float)(this.farm_2_R.y - Math.sin(newangle * 1.3F) * 20.0);
              this.fwing_6_R.x = this.fwing_5_R.x;
              this.fwing_6_R.y = this.fwing_5_R.y;
              this.fwing_7_L.x = this.fwing_5_R.x;
              this.fwing_7_L.y = this.fwing_5_R.y;
              this.fclaw_R.x = this.fwing_5_R.x;
              this.fclaw_L2.x = this.fwing_5_R.x;
              this.fclaw_R.y = this.fwing_5_R.y;
              this.fclaw_L2.y = this.fwing_5_R.y;
              this.fwing_5_R.zRot = (float)((Math.PI / 2) + newangle * 1.65F);
              this.fwing_6_R.zRot = (float)((Math.PI / 2) + newangle * 1.65F);
              this.fwing_7_L.zRot = (float)((Math.PI / 2) + newangle * 1.65F);
              this.fwing_7_L.xRot = (float) (-Math.PI / 2);
              this.fwing_6_R.xRot = (float) (-Math.PI * 3.0 / 8.0);
              this.fwing_5_R.xRot = (float) (-Math.PI / 4);
              this.fneck_1.xRot = -newangle / 12.0F;
              this.fneck_1.z = (float)(this.fchest.z - Math.cos(this.fchest.xRot) * 10.0);
              this.fneck_1.y = (float)(this.fchest.y + Math.sin(this.fchest.xRot) * 8.0 - 1.0);
              this.fneck_2.xRot = -newangle / 10.0F;
              this.fneck_2.z = (float)(this.fneck_1.z - Math.cos(this.fneck_1.xRot) * 7.0);
              this.fneck_2.y = (float)(this.fneck_1.y + Math.sin(this.fneck_1.xRot) * 6.0 - 1.0);
              this.fneck_3.xRot = -newangle / 8.0F;
              this.fneck_3.z = (float)(this.fneck_2.z - Math.cos(this.fneck_2.xRot) * 7.0);
              this.fneck_3.y = (float)(this.fneck_2.y + Math.sin(this.fneck_2.xRot) * 5.0);
              this.fhead.z = (float)(this.fneck_3.z - Math.cos(this.fneck_3.xRot) * 16.0);
              this.fhead.y = (float)(this.fneck_3.y + Math.sin(this.fneck_3.xRot) * 15.0);
              this.fupper_jaw.z = this.fhead.z;
              this.fupper_sail_1.z = this.fhead.z;
              this.fupper_sail2_.z = this.fhead.z;
              this.fupper_sail3.z = this.fhead.z;
              this.feye_ridge_L.z = this.fhead.z;
              this.fanntena_1_L.z = this.fhead.z;
              this.fanntena_2_L.z = this.fhead.z;
              this.feye_ridge_R.z = this.fhead.z;
              this.fanntena_1_R.z = this.fhead.z;
              this.fanntena_2_R.z = this.fhead.z;
              this.fbottom_jaw.z = this.fhead.z - 5.0F;
              this.flower_sail1.z = this.fhead.z - 5.0F;
              this.flower_sail2.z = this.fhead.z - 5.0F;
              this.flower_sail_3.z = this.fhead.z - 5.0F;
              this.fupper_jaw.y = this.fhead.y;
              this.fupper_sail_1.y = this.fhead.y;
              this.fupper_sail2_.y = this.fhead.y;
              this.fupper_sail3.y = this.fhead.y;
              this.feye_ridge_L.y = this.fhead.y;
              this.fanntena_1_L.y = this.fhead.y;
              this.fanntena_2_L.y = this.fhead.y;
              this.feye_ridge_R.y = this.fhead.y;
              this.fanntena_1_R.y = this.fhead.y;
              this.fanntena_2_R.y = this.fhead.y;
              this.fbottom_jaw.y = this.fhead.y + 4.0F;
              this.flower_sail1.y = this.fhead.y + 4.0F;
              this.flower_sail2.y = this.fhead.y + 4.0F;
              this.flower_sail_3.y = this.fhead.y + 4.0F;
              if (e.getBeingRidden() == 0) {
                 newangle = (float)Math.toRadians(f3) * 0.5F;
              } else {
                 r = e.getRenderInfo();
                 f3 = (e.yRotO - e.getYRot()) * 8.0F;
                 f3 = -f3;
                 r.rf1 = r.rf1 + (f3 - r.rf1) / 60.0F;
                 if (r.rf1 > 50.0F) {
                    r.rf1 = 50.0F;
                 }
                 if (r.rf1 < -50.0F) {
                    r.rf1 = -50.0F;
                 }
                 f3 = r.rf1;
                 e.setRenderInfo(r);
                 newangle = (float)Math.toRadians(f3) * 0.5F;
              }
              this.fhead.yRot = this.fupper_jaw.yRot = this.fupper_sail_1.yRot = this.fupper_sail2_.yRot = this.fupper_sail3.yRot = newangle;
              this.feye_ridge_L.yRot = 0.558F + newangle;
              this.fanntena_1_L.yRot = 0.366F + newangle;
              this.fanntena_2_L.yRot = 0.139F + newangle;
              this.feye_ridge_R.yRot = -0.558F + newangle;
              this.fanntena_1_R.yRot = -0.366F + newangle;
              this.fanntena_2_R.yRot = -0.139F + newangle;
              this.fbottom_jaw.yRot = this.flower_sail1.yRot = this.flower_sail2.yRot = this.flower_sail_3.yRot = newangle;
              this.fbottom_jaw.z = (float)(this.fhead.z - Math.cos(newangle) * 5.0);
              this.fbottom_jaw.x = (float)(this.fhead.x - Math.sin(newangle) * 5.0);
              float tf1 = 1.605F;
              float tf2 = 1.6919999F;
              float tf3 = 0.92399997F;
              if (e.getAttacking() == 0) {
                 this.fbottom_jaw.xRot = -1.308F + newangle2 / 2.0F;
              } else {
                 newangle2 = Mth.cos(f2 * 2.6F * this.wingspeed) * (float) Math.PI * 0.16F;
                 this.fbottom_jaw.xRot = -0.9F + newangle2;
              }
              this.flower_sail1.xRot = this.fbottom_jaw.xRot + tf1;
              this.flower_sail2.xRot = this.fbottom_jaw.xRot + tf2;
              this.flower_sail_3.xRot = this.fbottom_jaw.xRot + tf3;
           }
        this.setPoseVisible(e.getActivity() != 0);
    }
}
