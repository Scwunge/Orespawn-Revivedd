package danger.orespawn.client.model;

import danger.orespawn.entity.GiantRobot;
import danger.orespawn.entity.GiantRobot.RenderGiantRobotInfo;
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
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

/**
 * Port of gold {@code ModelGiantRobot} (1.7.10 ModelBase, tex 256×512) → 1.21 HierarchicalModel.
 * Cubes/UVs/offsets 1:1. Gold reuses Thigh/Shin/Foot/Arm parts ×2 in render; here each side is a
 * separate instance so setupAnim matches without multi-pass render.
 * ClientProxy wingspeed {@code 0.25F}. Full gold anim: hip bob/sway, dual-leg IK, dual-arm
 * attack swing, head look, shoulder twist.
 */
@OnlyIn(Dist.CLIENT)
public class ModelGiantRobot extends HierarchicalModel<GiantRobot> {
    /** Model layer location. */
    public static final ModelLayerLocation LAYER_LOCATION =
            new ModelLayerLocation(
                    ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "giant_robot"), "main");

    private static final float HIP_Y_BASE = -60.0F;

    private final float wingspeed;
    private final ModelPart root;

    private final ModelPart Hip;
    private final ModelPart[] Thigh = new ModelPart[2];
    private final ModelPart[] Shin = new ModelPart[2];
    private final ModelPart[] Foot1 = new ModelPart[2];
    private final ModelPart[] Foot2 = new ModelPart[2];
    private final ModelPart[] Foot3 = new ModelPart[2];
    private final ModelPart[] Thigh2 = new ModelPart[2];
    private final ModelPart[] Thigh3 = new ModelPart[2];
    private final ModelPart Back1;
    private final ModelPart Back2;
    private final ModelPart Back3;
    private final ModelPart Shoulders;
    private final ModelPart Neck;
    private final ModelPart Head;
    private final ModelPart[] Arm1 = new ModelPart[2];
    private final ModelPart[] Arm2 = new ModelPart[2];
    private final ModelPart[] Arm3 = new ModelPart[2];
    private final ModelPart[] Knuckles = new ModelPart[2];

    public ModelGiantRobot(ModelPart root) {
        this(root, 0.25F);
    }

    public ModelGiantRobot(ModelPart root, float wingspeed) {
        this.root = root;
        this.wingspeed = wingspeed;
        this.Hip = root.getChild("Hip");
        for (int i = 0; i < 2; i++) {
            this.Thigh[i] = root.getChild("Thigh_" + i);
            this.Shin[i] = root.getChild("Shin_" + i);
            this.Foot1[i] = root.getChild("Foot1_" + i);
            this.Foot2[i] = root.getChild("Foot2_" + i);
            this.Foot3[i] = root.getChild("Foot3_" + i);
            this.Thigh2[i] = root.getChild("Thigh2_" + i);
            this.Thigh3[i] = root.getChild("Thigh3_" + i);
            this.Arm1[i] = root.getChild("Arm1_" + i);
            this.Arm2[i] = root.getChild("Arm2_" + i);
            this.Arm3[i] = root.getChild("Arm3_" + i);
            this.Knuckles[i] = root.getChild("Knuckles_" + i);
        }
        this.Back1 = root.getChild("Back1");
        this.Back2 = root.getChild("Back2");
        this.Back3 = root.getChild("Back3");
        this.Shoulders = root.getChild("Shoulders");
        this.Neck = root.getChild("Neck");
        this.Head = root.getChild("Head");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        root.addOrReplaceChild(
                "Hip",
                CubeListBuilder.create().texOffs(0, 0)
                        .addBox(-4.0F, -4.0F, -15.0F, 8.0F, 8.0F, 30.0F),
                PartPose.offset(0.0F, HIP_Y_BASE, 0.0F));

        // Gold reuses Thigh/Shin/Foot ×2 (left/right); expand to _0/_1.
        for (int i = 0; i < 2; i++) {
            root.addOrReplaceChild(
                    "Thigh_" + i,
                    CubeListBuilder.create().texOffs(0, 115)
                            .addBox(-3.0F, -3.0F, -3.0F, 6.0F, 43.0F, 6.0F),
                    PartPose.offset(0.0F, -58.0F, 0.0F));
            root.addOrReplaceChild(
                    "Shin_" + i,
                    CubeListBuilder.create().texOffs(0, 167)
                            .addBox(-3.0F, -3.0F, -3.0F, 6.0F, 43.0F, 6.0F),
                    PartPose.offset(0.0F, -18.0F, 0.0F));
            root.addOrReplaceChild(
                    "Foot1_" + i,
                    CubeListBuilder.create().texOffs(0, 282)
                            .addBox(-7.0F, 38.0F, -11.0F, 14.0F, 4.0F, 17.0F),
                    PartPose.offset(0.0F, -18.0F, 0.0F));
            root.addOrReplaceChild(
                    "Foot2_" + i,
                    CubeListBuilder.create().texOffs(0, 246)
                            .addBox(-6.0F, 19.0F, -8.0F, 12.0F, 19.0F, 13.0F),
                    PartPose.offset(0.0F, -18.0F, 0.0F));
            root.addOrReplaceChild(
                    "Foot3_" + i,
                    CubeListBuilder.create().texOffs(0, 219)
                            .addBox(-5.0F, 5.0F, -5.0F, 10.0F, 14.0F, 9.0F),
                    PartPose.offset(0.0F, -18.0F, 0.0F));
            root.addOrReplaceChild(
                    "Thigh2_" + i,
                    CubeListBuilder.create().texOffs(0, 43)
                            .addBox(-7.0F, -8.0F, -7.0F, 14.0F, 24.0F, 14.0F),
                    PartPose.offset(0.0F, -58.0F, 0.0F));
            root.addOrReplaceChild(
                    "Thigh3_" + i,
                    CubeListBuilder.create().texOffs(0, 84)
                            .addBox(-5.0F, 16.0F, -5.0F, 10.0F, 17.0F, 10.0F),
                    PartPose.offset(0.0F, -58.0F, 0.0F));
        }

        root.addOrReplaceChild(
                "Back1",
                CubeListBuilder.create().texOffs(125, 138)
                        .addBox(-4.0F, -20.0F, -4.0F, 8.0F, 24.0F, 8.0F),
                PartPose.offset(0.0F, HIP_Y_BASE, 0.0F));
        root.addOrReplaceChild(
                "Back2",
                CubeListBuilder.create().texOffs(125, 95)
                        .addBox(-13.0F, -42.0F, -10.0F, 26.0F, 24.0F, 16.0F),
                PartPose.offset(0.0F, HIP_Y_BASE, 0.0F));
        root.addOrReplaceChild(
                "Back3",
                CubeListBuilder.create().texOffs(125, 43)
                        .addBox(-17.0F, -68.0F, -13.0F, 34.0F, 26.0F, 20.0F),
                PartPose.offset(0.0F, HIP_Y_BASE, 0.0F));
        root.addOrReplaceChild(
                "Shoulders",
                CubeListBuilder.create().texOffs(60, 200)
                        .addBox(-22.0F, -64.0F, -4.0F, 44.0F, 8.0F, 8.0F),
                PartPose.offset(0.0F, HIP_Y_BASE, 0.0F));
        root.addOrReplaceChild(
                "Neck",
                CubeListBuilder.create().texOffs(125, 29)
                        .addBox(-4.0F, -70.0F, -4.0F, 8.0F, 2.0F, 8.0F),
                PartPose.offset(0.0F, HIP_Y_BASE, 0.0F));
        root.addOrReplaceChild(
                "Head",
                CubeListBuilder.create().texOffs(127, 0)
                        .addBox(-7.0F, -82.0F, -7.0F, 14.0F, 12.0F, 14.0F),
                PartPose.offset(0.0F, HIP_Y_BASE, 0.0F));

        // Gold reuses Arm/Knuckles ×2; expand to _0 (right/+x) and _1 (left/-x).
        for (int i = 0; i < 2; i++) {
            float armX = i == 0 ? 28.0F : -28.0F;
            root.addOrReplaceChild(
                    "Arm1_" + i,
                    CubeListBuilder.create().texOffs(77, 250)
                            .addBox(-6.0F, -6.0F, -6.0F, 12.0F, 21.0F, 12.0F),
                    PartPose.offset(armX, -120.0F, 0.0F));
            root.addOrReplaceChild(
                    "Arm2_" + i,
                    CubeListBuilder.create().texOffs(73, 300)
                            .addBox(-4.0F, 15.0F, -4.0F, 8.0F, 24.0F, 8.0F),
                    PartPose.offset(armX, -120.0F, 0.0F));
            root.addOrReplaceChild(
                    "Arm3_" + i,
                    CubeListBuilder.create().texOffs(61, 350)
                            .addBox(-3.0F, -3.0F, -3.0F, 6.0F, 33.0F, 6.0F),
                    PartPose.offset(armX, -81.0F, 0.0F));
            root.addOrReplaceChild(
                    "Knuckles_" + i,
                    CubeListBuilder.create().texOffs(56, 400)
                            .addBox(-7.0F, 30.0F, -5.0F, 14.0F, 12.0F, 10.0F),
                    PartPose.offset(armX, -81.0F, 0.0F));
        }

        return LayerDefinition.create(mesh, 256, 512);
    }

    @Override
    public ModelPart root() {
        return this.root;
    }

    @Override
    public void setupAnim(
            GiantRobot entity,
            float limbSwing,
            float limbSwingAmount,
            float ageInTicks,
            float netHeadYaw,
            float headPitch) {
        // gold: f=limbSwing unused for walk phase, f1=limbSwingAmount, f2=ageInTicks, f3=yaw, f4=pitch
        float f1 = limbSwingAmount;
        float f2 = ageInTicks;
        float f3 = netHeadYaw;
        float f4 = headPitch;

        RenderGiantRobotInfo r = entity.getRenderGiantRobotInfo();

        float movescale = f1 * 0.65F;
        if (movescale > 1.0F) {
            movescale = 1.0F;
        }

        r.hipxdisplayangle = (float) (Math.cos(-f2 * this.wingspeed) * Math.PI * 0.1F * movescale);
        r.hipydisplayangle = (float) (Math.sin(-f2 * this.wingspeed) * Math.PI * 0.1F * movescale);
        r.thighdisplayangle[0] =
                (float) (Math.cos(-f2 * this.wingspeed + (Math.PI / 2)) * Math.PI * 0.15F * movescale)
                        - (float) ((Math.PI / 16) * movescale);
        r.thighdisplayangle[1] =
                (float) (Math.cos(-f2 * this.wingspeed + Math.PI + (Math.PI / 2)) * Math.PI * 0.15F * movescale)
                        - (float) ((Math.PI / 16) * movescale);
        r.shindisplayangle[0] =
                (float) (Math.cos(-f2 * this.wingspeed + Math.PI) * Math.PI * 0.2F * movescale)
                        + (float) (0.6283185400806344 * movescale);
        r.shindisplayangle[1] =
                (float) (Math.cos(-f2 * this.wingspeed) * Math.PI * 0.2F * movescale)
                        + (float) (0.6283185400806344 * movescale);

        float newangle = (float) (Math.cos(-f2 * this.wingspeed * 2.0F) * movescale);
        this.Hip.y = HIP_Y_BASE + newangle * 4.0F;
        this.Hip.xRot = r.hipxdisplayangle;
        this.Hip.yRot = (float) (r.hipydisplayangle + (Math.PI / 2));
        this.Hip.zRot = 0.0F;
        this.Hip.x = 0.0F;
        this.Hip.z = 0.0F;

        // Leg side 0 — gold first thigh/shin pass
        placeLeg(0, r.thighdisplayangle[0], r.shindisplayangle[0], /*sideSign*/ -1.0F);
        // Leg side 1 — gold second thigh/shin pass
        placeLeg(1, r.thighdisplayangle[1], r.shindisplayangle[1], /*sideSign*/ 1.0F);

        float shoulderangle = -r.hipydisplayangle;
        float a2angle;
        float a1angle = a2angle = r.thighdisplayangle[1];
        float b2angle;
        float b1angle = b2angle = r.thighdisplayangle[0];
        if (entity.getAttacking() != 0) {
            shoulderangle = (float) (-(Math.sin(f2 * this.wingspeed * 2.0F) * Math.PI * 0.2F));
            a1angle = (float) ((float) (Math.sin(f2 * this.wingspeed * 2.0F) * Math.PI / 5.0) - (Math.PI / 4));
            a2angle = (float) (-a1angle + Math.PI);
            a1angle = (float) (a1angle + (Math.PI / 5));
            a2angle = (float) (a2angle + (Math.PI / 5));
            b1angle = (float) ((float) (-(Math.sin(f2 * this.wingspeed * 2.0F) * Math.PI / 5.0)) - (Math.PI / 4));
            b2angle = (float) (-b1angle + Math.PI);
            b1angle = (float) (b1angle + (Math.PI / 5));
            b2angle = (float) (b2angle + (Math.PI / 5));
        }

        this.Back3.yRot = shoulderangle / 2.0F;
        this.Shoulders.yRot = shoulderangle;
        this.Shoulders.xRot = 0.0F;
        this.Shoulders.zRot = 0.0F;
        this.Shoulders.x = 0.0F;
        this.Shoulders.z = 0.0F;

        // Arm side 0 (+x / gold first arm at +26)
        placeArm(0, 26.0F, -1.0F, a1angle, a2angle);
        // Arm side 1 (-x / gold second arm at -26)
        placeArm(1, -26.0F, 1.0F, b1angle, b2angle);

        this.Back1.y = this.Back2.y = this.Back3.y = this.Hip.y;
        this.Shoulders.y = this.Neck.y = this.Head.y = this.Hip.y;
        this.Back1.x = this.Back2.x = this.Back3.x = 0.0F;
        this.Back1.z = this.Back2.z = this.Back3.z = 0.0F;
        this.Neck.x = this.Head.x = 0.0F;
        this.Neck.z = this.Head.z = 0.0F;
        this.Back1.xRot = this.Back2.xRot = this.Back3.xRot = 0.0F;
        this.Back1.yRot = 0.0F;
        this.Back2.yRot = 0.0F;
        this.Back1.zRot = this.Back2.zRot = this.Back3.zRot = 0.0F;
        this.Neck.xRot = this.Neck.yRot = this.Neck.zRot = 0.0F;
        this.Head.yRot = (float) Math.toRadians(f3);
        this.Head.xRot = (float) Math.toRadians(f4) / 3.0F;
        this.Head.zRot = 0.0F;
    }

    /**
     * Gold leg IK for one side.
     * sideSign -1 → gold first pass (Hip + sin/cos as written for side 0),
     * sideSign +1 → gold second pass with inverted hip offsets.
     */
    private void placeLeg(int side, float thighAngle, float shinAngle, float sideSign) {
        float hipXRot = this.Hip.xRot;
        float hipYRot = this.Hip.yRot;
        float thighY = this.Hip.y + sideSign * (float) Math.sin(hipXRot) * 13.0F;
        // gold side0: Hip.z + cos*cos*13 ; side1: Hip.z - cos*cos*13 → sideSign maps via:
        // sideSign -1: y uses -sin, z uses +cos*cos, x uses +cos*sin
        // sideSign +1: y uses +sin, z uses -cos*cos, x uses -cos*sin
        float thighZ =
                this.Hip.z - sideSign * (float) Math.cos(hipXRot) * (float) Math.cos(hipYRot) * 13.0F;
        float thighX =
                this.Hip.x - sideSign * (float) Math.cos(hipXRot) * (float) Math.sin(hipYRot) * 13.0F;
        // Fix side0 (sideSign=-1): gold uses Hip.y - sin, Hip.z + cos*cos, Hip.x + cos*sin
        // With sideSign=-1: y = Hip.y + (-1)*sin = Hip.y - sin ✓
        // z = Hip.z - (-1)*cos*cos = Hip.z + cos*cos ✓
        // x = Hip.x - (-1)*cos*sin = Hip.x + cos*sin ✓
        // side1 (sideSign=+1): y = Hip.y + sin ✓, z = Hip.z - cos*cos ✓, x = Hip.x - cos*sin ✓

        this.Thigh[side].xRot = thighAngle;
        this.Thigh[side].yRot = 0.0F;
        this.Thigh[side].zRot = 0.0F;
        this.Thigh[side].y = thighY;
        this.Thigh[side].z = thighZ;
        this.Thigh[side].x = thighX;
        this.Thigh2[side].xRot = thighAngle;
        this.Thigh2[side].yRot = 0.0F;
        this.Thigh2[side].zRot = 0.0F;
        this.Thigh2[side].y = thighY;
        this.Thigh2[side].z = thighZ;
        this.Thigh2[side].x = thighX;
        this.Thigh3[side].xRot = thighAngle;
        this.Thigh3[side].yRot = 0.0F;
        this.Thigh3[side].zRot = 0.0F;
        this.Thigh3[side].y = thighY;
        this.Thigh3[side].z = thighZ;
        this.Thigh3[side].x = thighX;

        float shinY = thighY + (float) Math.cos(thighAngle) * 40.0F;
        float shinZ = thighZ + (float) Math.sin(thighAngle) * 40.0F;
        float shinX = thighX;

        this.Shin[side].xRot = shinAngle;
        this.Shin[side].yRot = 0.0F;
        this.Shin[side].zRot = 0.0F;
        this.Shin[side].y = shinY;
        this.Shin[side].z = shinZ;
        this.Shin[side].x = shinX;

        this.Foot1[side].xRot = shinAngle;
        this.Foot1[side].yRot = 0.0F;
        this.Foot1[side].zRot = 0.0F;
        this.Foot1[side].y = shinY;
        this.Foot1[side].z = shinZ;
        this.Foot1[side].x = shinX;
        this.Foot2[side].xRot = shinAngle;
        this.Foot2[side].yRot = 0.0F;
        this.Foot2[side].zRot = 0.0F;
        this.Foot2[side].y = shinY;
        this.Foot2[side].z = shinZ;
        this.Foot2[side].x = shinX;
        this.Foot3[side].xRot = shinAngle;
        this.Foot3[side].yRot = 0.0F;
        this.Foot3[side].zRot = 0.0F;
        this.Foot3[side].y = shinY;
        this.Foot3[side].z = shinZ;
        this.Foot3[side].x = shinX;
    }

    /**
     * Gold arm IK for one side.
     * xOffset ±26; zSign -1 for +x arm (Shoulders.z - sin*26), +1 for -x arm (Shoulders.z + sin*26).
     */
    private void placeArm(int side, float xOffset, float zSign, float a1angle, float a2angle) {
        float armY = this.Hip.y - 60.0F;
        float armX = this.Hip.x + xOffset;
        float armZ = this.Shoulders.z + zSign * (float) Math.sin(this.Shoulders.yRot) * 26.0F;
        // gold +x: Shoulders.z - sin * 26 → zSign = -1, armZ = Shoulders.z + (-1)*sin*26 ✓
        // gold -x: Shoulders.z + sin * 26 → zSign = +1 ✓

        this.Arm1[side].xRot = a1angle;
        this.Arm1[side].yRot = 0.0F;
        this.Arm1[side].zRot = 0.0F;
        this.Arm1[side].y = armY;
        this.Arm1[side].x = armX;
        this.Arm1[side].z = armZ;
        this.Arm2[side].xRot = a1angle;
        this.Arm2[side].yRot = 0.0F;
        this.Arm2[side].zRot = 0.0F;
        this.Arm2[side].y = armY;
        this.Arm2[side].x = armX;
        this.Arm2[side].z = armZ;

        float forearmRot = (float) (a2angle - (Math.PI / 16));
        float forearmY = armY + (float) Math.cos(a1angle) * 41.0F;
        float forearmZ = armZ + (float) Math.sin(a1angle) * 41.0F;

        this.Arm3[side].xRot = forearmRot;
        this.Arm3[side].yRot = 0.0F;
        this.Arm3[side].zRot = 0.0F;
        this.Arm3[side].y = forearmY;
        this.Arm3[side].z = forearmZ;
        this.Arm3[side].x = armX;
        this.Knuckles[side].xRot = forearmRot;
        this.Knuckles[side].yRot = 0.0F;
        this.Knuckles[side].zRot = 0.0F;
        this.Knuckles[side].y = forearmY;
        this.Knuckles[side].z = forearmZ;
        this.Knuckles[side].x = armX;
    }
}
