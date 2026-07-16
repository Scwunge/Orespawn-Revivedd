package danger.orespawn.client.model;

import danger.orespawn.entity.SeaMonster;
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
 * Port of gold {@code ModelSeaMonster} (1.7.10 ModelBase, tex 256×128) → 1.21 HierarchicalModel.
 * Cubes / pivots / UV 1:1. Full gold {@code func_78088_a} tail chain + fin paddle + neck chain + jaw.
 * wingspeed default 0.5 from ClientProxy registration.
 */
@OnlyIn(Dist.CLIENT)
public class ModelSeaMonster extends HierarchicalModel<SeaMonster> {
    /** Model layer location. */
    public static final ModelLayerLocation LAYER_LOCATION =
            new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "sea_monster"), "main");

    private final float wingspeed;
    private final ModelPart root;
    private final ModelPart tailTip;
    private final ModelPart tailBase;
    private final ModelPart tail2;
    private final ModelPart tail3;
    private final ModelPart bodyBack;
    private final ModelPart neck6;
    private final ModelPart bodyFront;
    private final ModelPart neckBase;
    private final ModelPart neck2;
    private final ModelPart neck3;
    private final ModelPart neck4;
    private final ModelPart neck5;
    private final ModelPart bottomJaw;
    private final ModelPart finBackRight;
    private final ModelPart finBackLeft;
    private final ModelPart finFrontLeft;
    private final ModelPart finFrontRight;
    private final ModelPart tail4;
    private final ModelPart tail5;
    private final ModelPart tail6;
    private final ModelPart topJaw;
    private final ModelPart rightEye;
    private final ModelPart leftEye;

    // gold base pivots for animated chains
    private static final float TAIL_BASE_X = 0.0F;
    private static final float TAIL_BASE_Y = 16.0F;
    private static final float TAIL_BASE_Z = 26.0F;
    private static final float NECK_BASE_X = 0.0F;
    private static final float NECK_BASE_Y = 12.0F;
    private static final float NECK_BASE_Z = -2.0F;

    public ModelSeaMonster(ModelPart root) {
        this(root, 0.5F);
    }

    public ModelSeaMonster(ModelPart root, float wingspeed) {
        this.root = root;
        this.wingspeed = wingspeed;
        this.tailTip = root.getChild("tail_tip");
        this.tailBase = root.getChild("tail_base");
        this.tail2 = root.getChild("tail2");
        this.tail3 = root.getChild("tail3");
        this.bodyBack = root.getChild("body_back");
        this.neck6 = root.getChild("neck6");
        this.bodyFront = root.getChild("body_front");
        this.neckBase = root.getChild("neck_base");
        this.neck2 = root.getChild("neck2");
        this.neck3 = root.getChild("neck3");
        this.neck4 = root.getChild("neck4");
        this.neck5 = root.getChild("neck5");
        this.bottomJaw = root.getChild("bottom_jaw");
        this.finBackRight = root.getChild("fin_back_right");
        this.finBackLeft = root.getChild("fin_back_left");
        this.finFrontLeft = root.getChild("fin_front_left");
        this.finFrontRight = root.getChild("fin_front_right");
        this.tail4 = root.getChild("tail4");
        this.tail5 = root.getChild("tail5");
        this.tail6 = root.getChild("tail6");
        this.topJaw = root.getChild("top_jaw");
        this.rightEye = root.getChild("right_eye");
        this.leftEye = root.getChild("left_eye");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        // gold tex 256×128; cubes/pivots/base rotations 1:1
        root.addOrReplaceChild(
                "tail_tip",
                CubeListBuilder.create().texOffs(158, 36).addBox(-1.0F, -1.0F, 0.0F, 2.0F, 2.0F, 6.0F),
                PartPose.offset(0.0F, 16.0F, 70.0F));
        root.addOrReplaceChild(
                "tail_base",
                CubeListBuilder.create().texOffs(68, 64).addBox(-7.0F, -7.0F, 0.0F, 14.0F, 14.0F, 12.0F),
                PartPose.offset(TAIL_BASE_X, TAIL_BASE_Y, TAIL_BASE_Z));
        root.addOrReplaceChild(
                "tail2",
                CubeListBuilder.create().texOffs(74, 90).addBox(-6.0F, -6.0F, 0.0F, 12.0F, 12.0F, 8.0F),
                PartPose.offset(0.0F, 16.0F, 38.0F));
        root.addOrReplaceChild(
                "tail3",
                CubeListBuilder.create().texOffs(78, 110).addBox(-5.0F, -5.0F, 0.0F, 10.0F, 10.0F, 6.0F),
                PartPose.offset(0.0F, 16.0F, 46.0F));
        root.addOrReplaceChild(
                "body_back",
                CubeListBuilder.create().texOffs(62, 32).addBox(-8.0F, -8.0F, 0.0F, 16.0F, 16.0F, 16.0F),
                PartPose.offset(0.0F, 16.0F, 10.0F));
        root.addOrReplaceChild(
                "neck6",
                CubeListBuilder.create().texOffs(20, 28).addBox(-2.0F, -6.0F, -2.0F, 4.0F, 6.0F, 4.0F),
                PartPose.offsetAndRotation(0.0F, -21.0F, -25.0F, 1.22173F, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "body_front",
                CubeListBuilder.create().texOffs(62, 0).addBox(-8.0F, -8.0F, -16.0F, 16.0F, 16.0F, 16.0F),
                PartPose.offset(0.0F, 16.0F, 10.0F));
        root.addOrReplaceChild(
                "neck_base",
                CubeListBuilder.create().texOffs(8, 96).addBox(-5.0F, -10.0F, -5.0F, 10.0F, 10.0F, 10.0F),
                PartPose.offsetAndRotation(NECK_BASE_X, NECK_BASE_Y, NECK_BASE_Z, (float) (Math.PI / 4), 0.0F, 0.0F));
        root.addOrReplaceChild(
                "neck2",
                CubeListBuilder.create().texOffs(14, 78).addBox(-3.0F, -10.0F, -4.0F, 6.0F, 10.0F, 8.0F),
                PartPose.offsetAndRotation(0.0F, 6.0F, -9.0F, (float) (Math.PI * 2.0 / 9.0), 0.0F, 0.0F));
        root.addOrReplaceChild(
                "neck3",
                CubeListBuilder.create().texOffs(16, 62).addBox(-3.0F, -10.0F, -3.0F, 6.0F, 10.0F, 6.0F),
                PartPose.offsetAndRotation(0.0F, -1.0F, -15.0F, (float) (Math.PI / 6), 0.0F, 0.0F));
        root.addOrReplaceChild(
                "neck4",
                CubeListBuilder.create().texOffs(20, 48).addBox(-2.0F, -10.0F, -2.0F, 4.0F, 10.0F, 4.0F),
                PartPose.offsetAndRotation(0.0F, -9.0F, -20.0F, (float) (Math.PI / 12), 0.0F, 0.0F));
        root.addOrReplaceChild(
                "neck5",
                CubeListBuilder.create().texOffs(20, 38).addBox(-2.0F, -6.0F, -2.0F, 4.0F, 6.0F, 4.0F),
                PartPose.offsetAndRotation(0.0F, -17.6F, -22.0F, (float) (Math.PI / 4), 0.0F, 0.0F));
        root.addOrReplaceChild(
                "bottom_jaw",
                CubeListBuilder.create().texOffs(10, 0).addBox(-4.0F, 0.0F, -10.0F, 8.0F, 3.0F, 10.0F),
                PartPose.offsetAndRotation(0.0F, -23.0F, -29.0F, 0.1745329F, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "fin_back_right",
                CubeListBuilder.create().texOffs(132, 95).addBox(-8.0F, 0.0F, 0.0F, 8.0F, 1.0F, 16.0F),
                PartPose.offsetAndRotation(
                        -7.0F, 16.0F, 16.0F, (float) (-Math.PI / 6), (float) (-Math.PI * 2.0 / 9.0), 0.0F));
        root.addOrReplaceChild(
                "fin_back_left",
                CubeListBuilder.create().texOffs(132, 61).addBox(0.0F, 0.0F, 0.0F, 8.0F, 1.0F, 16.0F),
                PartPose.offsetAndRotation(
                        7.0F, 16.0F, 16.0F, (float) (-Math.PI / 6), (float) (Math.PI * 2.0 / 9.0), 0.0F));
        root.addOrReplaceChild(
                "fin_front_left",
                CubeListBuilder.create().texOffs(132, 44).addBox(0.0F, 0.0F, 0.0F, 8.0F, 1.0F, 16.0F),
                PartPose.offsetAndRotation(
                        7.0F, 16.0F, -1.0F, (float) (-Math.PI / 6), (float) (Math.PI * 2.0 / 9.0), 0.0F));
        root.addOrReplaceChild(
                "fin_front_right",
                CubeListBuilder.create().texOffs(132, 78).addBox(-8.0F, 0.0F, 0.0F, 8.0F, 1.0F, 16.0F),
                PartPose.offsetAndRotation(
                        -7.0F, 16.0F, -1.0F, (float) (-Math.PI / 6), (float) (-Math.PI * 2.0 / 9.0), 0.0F));
        root.addOrReplaceChild(
                "tail4",
                CubeListBuilder.create().texOffs(152, 0).addBox(-4.0F, -4.0F, 0.0F, 8.0F, 8.0F, 6.0F),
                PartPose.offset(0.0F, 16.0F, 52.0F));
        root.addOrReplaceChild(
                "tail5",
                CubeListBuilder.create().texOffs(154, 14).addBox(-3.0F, -3.0F, 0.0F, 6.0F, 6.0F, 6.0F),
                PartPose.offset(0.0F, 16.0F, 58.0F));
        root.addOrReplaceChild(
                "tail6",
                CubeListBuilder.create().texOffs(156, 26).addBox(-2.0F, -2.0F, 0.0F, 4.0F, 4.0F, 6.0F),
                PartPose.offset(0.0F, 16.0F, 64.0F));
        root.addOrReplaceChild(
                "top_jaw",
                CubeListBuilder.create().texOffs(10, 13).addBox(-4.0F, -4.0F, -10.0F, 8.0F, 4.0F, 10.0F),
                PartPose.offset(0.0F, -23.0F, -29.0F));
        root.addOrReplaceChild(
                "right_eye",
                CubeListBuilder.create().texOffs(46, 16).addBox(-3.0F, -6.0F, -5.0F, 2.0F, 2.0F, 1.0F),
                PartPose.offset(0.0F, -23.0F, -29.0F));
        root.addOrReplaceChild(
                "left_eye",
                CubeListBuilder.create().texOffs(4, 16).addBox(1.0F, -6.0F, -5.0F, 2.0F, 2.0F, 1.0F),
                PartPose.offset(0.0F, -23.0F, -29.0F));

        return LayerDefinition.create(mesh, 256, 128);
    }

    @Override
    public ModelPart root() {
        return this.root;
    }

    @Override
    public void setupAnim(
            SeaMonster entity,
            float limbSwing,
            float limbSwingAmount,
            float ageInTicks,
            float netHeadYaw,
            float headPitch) {
        // gold func_78088_a — f1=limbSwingAmount, f2=ageInTicks, f3=netHeadYaw
        float f1 = limbSwingAmount;
        float f2 = ageInTicks;
        float newangle;

        // --- tail yaw chain (field_78796_g → yRot; field_78798_e/z, field_78800_c/x) ---
        if (!(f1 > 0.1F) && entity.getAttacking() == 0) {
            newangle = 0.0F;
        } else {
            newangle = Mth.cos(f2 * 1.3F * this.wingspeed) * (float) Math.PI * 0.2F * f1;
        }

        this.tailBase.x = TAIL_BASE_X;
        this.tailBase.y = TAIL_BASE_Y;
        this.tailBase.z = TAIL_BASE_Z;
        this.tailBase.yRot = newangle / 7.0F;
        this.tail2.z = this.tailBase.z + (float) Math.cos(this.tailBase.yRot) * 10.0F;
        this.tail2.x = this.tailBase.x + (float) Math.sin(this.tailBase.yRot) * 10.0F;
        this.tail2.y = TAIL_BASE_Y;
        this.tail2.yRot = newangle / 6.0F;
        this.tail3.z = this.tail2.z + (float) Math.cos(this.tail2.yRot) * 7.0F;
        this.tail3.x = this.tail2.x + (float) Math.sin(this.tail2.yRot) * 7.0F;
        this.tail3.y = TAIL_BASE_Y;
        this.tail3.yRot = newangle / 5.0F;
        this.tail4.z = this.tail3.z + (float) Math.cos(this.tail3.yRot) * 5.0F;
        this.tail4.x = this.tail3.x + (float) Math.sin(this.tail3.yRot) * 5.0F;
        this.tail4.y = TAIL_BASE_Y;
        this.tail4.yRot = newangle / 4.0F;
        this.tail5.z = this.tail4.z + (float) Math.cos(this.tail4.yRot) * 5.0F;
        this.tail5.x = this.tail4.x + (float) Math.sin(this.tail4.yRot) * 5.0F;
        this.tail5.y = TAIL_BASE_Y;
        this.tail5.yRot = newangle / 3.0F;
        this.tail6.z = this.tail5.z + (float) Math.cos(this.tail5.yRot) * 5.0F;
        this.tail6.x = this.tail5.x + (float) Math.sin(this.tail5.yRot) * 5.0F;
        this.tail6.y = TAIL_BASE_Y;
        this.tail6.yRot = newangle / 2.0F;
        this.tailTip.z = this.tail6.z + (float) Math.cos(this.tail6.yRot) * 5.0F;
        this.tailTip.x = this.tail6.x + (float) Math.sin(this.tail6.yRot) * 5.0F;
        this.tailTip.y = TAIL_BASE_Y;
        this.tailTip.yRot = newangle;

        // --- fins (field_78795_f → xRot, field_78796_g → yRot) ---
        if (!(f1 > 0.1F) && entity.getAttacking() == 0) {
            newangle = Mth.cos(f2 * 1.2F * this.wingspeed) * (float) Math.PI * 0.02F;
        } else {
            newangle = Mth.cos(f2 * 1.2F * this.wingspeed) * (float) Math.PI * 0.2F * f1;
        }

        this.finFrontLeft.xRot = newangle - 0.523F;
        this.finFrontLeft.yRot = newangle + 0.698F;
        this.finBackLeft.xRot = -newangle - 0.523F;
        this.finBackLeft.yRot = -newangle + 0.698F;
        this.finFrontRight.xRot = newangle - 0.523F;
        this.finFrontRight.yRot = newangle - 0.698F;
        this.finBackRight.xRot = -newangle - 0.523F;
        this.finBackRight.yRot = -newangle - 0.698F;

        // --- neck pitch chain (field_78795_f → xRot; z/y from sin/cos) ---
        if (!(f1 > 0.1F) && entity.getAttacking() == 0) {
            newangle = Mth.cos(f2 * 0.3F * this.wingspeed) * (float) Math.PI * 0.02F;
        } else {
            newangle =
                    0.455F * f1
                            + Mth.cos(f2 * 0.9F * this.wingspeed) * (float) Math.PI * 0.25F * f1;
        }

        this.neckBase.x = NECK_BASE_X;
        this.neckBase.y = NECK_BASE_Y;
        this.neckBase.z = NECK_BASE_Z;
        this.neckBase.xRot = 0.455F + newangle / 5.0F;
        this.neck2.z = this.neckBase.z - (float) Math.sin(this.neckBase.xRot) * 9.0F;
        this.neck2.y = this.neckBase.y - (float) Math.cos(this.neckBase.xRot) * 9.0F;
        this.neck2.x = NECK_BASE_X;
        this.neck2.xRot = this.neckBase.xRot + newangle / 4.0F;
        this.neck3.z = this.neck2.z - (float) Math.sin(this.neck2.xRot) * 9.0F;
        this.neck3.y = this.neck2.y - (float) Math.cos(this.neck2.xRot) * 9.0F;
        this.neck3.x = NECK_BASE_X;
        this.neck3.xRot = this.neck2.xRot + newangle / 3.0F;
        this.neck4.z = this.neck3.z - (float) Math.sin(this.neck3.xRot) * 9.0F;
        this.neck4.y = this.neck3.y - (float) Math.cos(this.neck3.xRot) * 9.0F;
        this.neck4.x = NECK_BASE_X;
        this.neck4.xRot = this.neck3.xRot + newangle / 2.0F;
        this.neck5.z = this.neck4.z - (float) Math.sin(this.neck4.xRot) * 9.0F;
        this.neck5.y = this.neck4.y - (float) Math.cos(this.neck4.xRot) * 9.0F;
        this.neck5.x = NECK_BASE_X;
        this.neck5.xRot = this.neck4.xRot - newangle / 2.0F;
        this.neck6.z = this.neck5.z - (float) Math.sin(this.neck5.xRot) * 5.0F;
        this.neck6.y = this.neck5.y - (float) Math.cos(this.neck5.xRot) * 5.0F;
        this.neck6.x = NECK_BASE_X;
        this.neck6.xRot = this.neck5.xRot - newangle / 3.0F;

        float headZ = this.neck6.z - (float) Math.sin(this.neck6.xRot) * 5.0F;
        float headY = this.neck6.y - (float) Math.cos(this.neck6.xRot) * 5.0F;
        this.bottomJaw.z = this.leftEye.z = this.rightEye.z = this.topJaw.z = headZ;
        this.bottomJaw.y = this.leftEye.y = this.rightEye.y = this.topJaw.y = headY;
        this.bottomJaw.x = this.leftEye.x = this.rightEye.x = this.topJaw.x = NECK_BASE_X;

        // head yaw from netHeadYaw
        newangle = (float) Math.toRadians(netHeadYaw) * 0.5F;
        this.bottomJaw.yRot = this.leftEye.yRot = this.rightEye.yRot = this.topJaw.yRot = newangle;

        // jaw attack / idle bob
        if (entity.getAttacking() != 0) {
            newangle = Mth.cos(f2 * 1.7F * this.wingspeed) * (float) Math.PI * 0.17F;
            this.bottomJaw.xRot = 0.45F + newangle;
        } else {
            newangle = Mth.cos(f2 * 0.2F * this.wingspeed) * (float) Math.PI * 0.05F;
            this.bottomJaw.xRot = 0.17F + newangle;
        }

        // static body
        this.bodyBack.xRot = 0.0F;
        this.bodyFront.xRot = 0.0F;
        this.topJaw.xRot = 0.0F;
        this.rightEye.xRot = 0.0F;
        this.leftEye.xRot = 0.0F;
    }
}
