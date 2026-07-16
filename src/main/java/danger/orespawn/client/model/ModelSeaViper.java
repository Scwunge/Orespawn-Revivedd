package danger.orespawn.client.model;

import danger.orespawn.entity.SeaViper;
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
 * Port of gold {@code ModelSeaViper} (1.7.10 ModelBase, tex 128×128) → 1.21 HierarchicalModel.
 * Cubes / pivots / UV 1:1. Gold ctor shifts all Z by +32 — baked into PartPose here.
 * Full gold {@code func_78088_a} doseg tail chain + jaw/tongue attack + head yaw.
 * wingspeed default 0.5 from ClientProxy registration.
 */
@OnlyIn(Dist.CLIENT)
public class ModelSeaViper extends HierarchicalModel<SeaViper> {
    /** Model layer location. */
    public static final ModelLayerLocation LAYER_LOCATION =
            new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "sea_viper"), "main");

    private final float wingspeed;
    private final ModelPart root;
    private final ModelPart tailTip;
    private final ModelPart neck;
    private final ModelPart tBase;
    private final ModelPart t2;
    private final ModelPart t3;
    private final ModelPart t4;
    private final ModelPart t5;
    private final ModelPart t6;
    private final ModelPart t7;
    private final ModelPart t8;
    private final ModelPart t9;
    private final ModelPart t10;
    private final ModelPart t11;
    private final ModelPart t12;
    private final ModelPart t13;
    private final ModelPart t14;
    private final ModelPart t15;
    private final ModelPart t16;
    private final ModelPart t17;
    private final ModelPart t18;
    private final ModelPart t19;
    private final ModelPart t20;
    private final ModelPart t21;
    private final ModelPart mouthBottom;
    private final ModelPart toungBase;
    private final ModelPart middleTounge;
    private final ModelPart eyeRight;
    private final ModelPart eyeLeft;
    private final ModelPart mouthTop;
    private final ModelPart head;
    private final ModelPart fangRight;
    private final ModelPart fangLeft;
    private final ModelPart forkRight;
    private final ModelPart forkLeft;

    // gold base after +32 Z shift
    private static final float TBASE_X = 0.0F;
    private static final float TBASE_Y = 4.0F;
    private static final float TBASE_Z = -2.0F; // -34 + 32
    private static final float HEAD_X = 0.0F;
    private static final float HEAD_Y = 6.0F;
    private static final float HEAD_Z = -8.0F; // -40 + 32
    private static final float MOUTH_BOT_Y = 4.0F;
    private static final float MOUTH_BOT_Z = -10.0F; // -42 + 32
    private static final float TONGUE_BASE_Z = -8.0F;

    public ModelSeaViper(ModelPart root) {
        this(root, 0.5F);
    }

    public ModelSeaViper(ModelPart root, float wingspeed) {
        this.root = root;
        this.wingspeed = wingspeed;
        this.tailTip = root.getChild("tail_tip");
        this.neck = root.getChild("neck");
        this.tBase = root.getChild("t_base");
        this.t2 = root.getChild("t2");
        this.t3 = root.getChild("t3");
        this.t4 = root.getChild("t4");
        this.t5 = root.getChild("t5");
        this.t6 = root.getChild("t6");
        this.t7 = root.getChild("t7");
        this.t8 = root.getChild("t8");
        this.t9 = root.getChild("t9");
        this.t10 = root.getChild("t10");
        this.t11 = root.getChild("t11");
        this.t12 = root.getChild("t12");
        this.t13 = root.getChild("t13");
        this.t14 = root.getChild("t14");
        this.t15 = root.getChild("t15");
        this.t16 = root.getChild("t16");
        this.t17 = root.getChild("t17");
        this.t18 = root.getChild("t18");
        this.t19 = root.getChild("t19");
        this.t20 = root.getChild("t20");
        this.t21 = root.getChild("t21");
        this.mouthBottom = root.getChild("mouth_bottom");
        this.toungBase = root.getChild("toung_base");
        this.middleTounge = root.getChild("middle_tounge");
        this.eyeRight = root.getChild("eye_right");
        this.eyeLeft = root.getChild("eye_left");
        this.mouthTop = root.getChild("mouth_top");
        this.head = root.getChild("head");
        this.fangRight = root.getChild("fang_right");
        this.fangLeft = root.getChild("fang_left");
        this.forkRight = root.getChild("fork_right");
        this.forkLeft = root.getChild("fork_left");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        // gold tex 128×128; pivots include gold constructor Z+=32
        root.addOrReplaceChild(
                "tail_tip",
                CubeListBuilder.create().texOffs(0, 90).addBox(-1.0F, -1.0F, 0.0F, 2.0F, 5.0F, 10.0F),
                PartPose.offsetAndRotation(1.0F, 20.0F, 152.0F, 0.0F, (float) (-Math.PI * 2.0 / 9.0), 0.0F));
        root.addOrReplaceChild(
                "neck",
                CubeListBuilder.create().texOffs(60, 60).addBox(-4.0F, -4.0F, -10.0F, 8.0F, 8.0F, 10.0F),
                PartPose.offsetAndRotation(0.0F, 4.5F, -1.0F, (float) (-Math.PI / 12), 0.0F, 0.0F));
        root.addOrReplaceChild(
                "t_base",
                CubeListBuilder.create().texOffs(0, 31).addBox(-4.0F, -4.0F, 0.0F, 8.0F, 8.0F, 10.0F),
                PartPose.offsetAndRotation(TBASE_X, TBASE_Y, TBASE_Z, (float) (-Math.PI / 6), 0.0F, 0.0F));
        root.addOrReplaceChild(
                "t2",
                CubeListBuilder.create().texOffs(0, 31).addBox(-4.0F, -4.0F, 0.0F, 8.0F, 8.0F, 10.0F),
                PartPose.offsetAndRotation(0.0F, 7.0F, 5.0F, -1.047198F, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "t3",
                CubeListBuilder.create().texOffs(0, 31).addBox(-4.0F, -4.0F, 0.0F, 8.0F, 8.0F, 10.0F),
                PartPose.offsetAndRotation(0.0F, 14.0F, 8.0F, (float) (-Math.PI / 6), 0.0F, 0.0F));
        root.addOrReplaceChild(
                "t4",
                CubeListBuilder.create().texOffs(0, 31).addBox(-4.0F, -4.0F, 0.0F, 8.0F, 8.0F, 10.0F),
                PartPose.offsetAndRotation(0.0F, 19.0F, 15.0F, -0.0872665F, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "t5",
                CubeListBuilder.create().texOffs(0, 31).addBox(-4.0F, -4.0F, 0.0F, 8.0F, 8.0F, 10.0F),
                PartPose.offset(0.0F, 20.0F, 23.0F));
        root.addOrReplaceChild(
                "t6",
                CubeListBuilder.create().texOffs(0, 31).addBox(-4.0F, -4.0F, 0.0F, 8.0F, 8.0F, 10.0F),
                PartPose.offsetAndRotation(0.0F, 20.0F, 31.0F, 0.0F, 0.3490659F, 0.0F));
        root.addOrReplaceChild(
                "t7",
                CubeListBuilder.create().texOffs(0, 31).addBox(-4.0F, -4.0F, 0.0F, 8.0F, 8.0F, 10.0F),
                PartPose.offsetAndRotation(2.0F, 20.0F, 38.0F, 0.0F, (float) (Math.PI * 2.0 / 9.0), 0.0F));
        root.addOrReplaceChild(
                "t8",
                CubeListBuilder.create().texOffs(0, 31).addBox(-4.0F, -4.0F, 0.0F, 8.0F, 8.0F, 10.0F),
                PartPose.offsetAndRotation(7.0F, 20.0F, 44.0F, 0.0F, 0.3490659F, 0.0F));
        root.addOrReplaceChild(
                "t9",
                CubeListBuilder.create().texOffs(0, 31).addBox(-4.0F, -4.0F, 0.0F, 8.0F, 8.0F, 10.0F),
                PartPose.offset(10.0F, 20.0F, 52.0F));
        root.addOrReplaceChild(
                "t10",
                CubeListBuilder.create().texOffs(0, 31).addBox(-4.0F, -4.0F, 0.0F, 8.0F, 8.0F, 10.0F),
                PartPose.offsetAndRotation(10.0F, 20.0F, 60.0F, 0.0F, -0.3490659F, 0.0F));
        root.addOrReplaceChild(
                "t11",
                CubeListBuilder.create().texOffs(0, 31).addBox(-4.0F, -4.0F, 0.0F, 8.0F, 8.0F, 10.0F),
                PartPose.offsetAndRotation(8.0F, 20.0F, 67.0F, 0.0F, (float) (-Math.PI * 2.0 / 9.0), 0.0F));
        root.addOrReplaceChild(
                "t12",
                CubeListBuilder.create().texOffs(0, 31).addBox(-4.0F, -4.0F, 0.0F, 8.0F, 8.0F, 10.0F),
                PartPose.offsetAndRotation(2.0F, 20.0F, 74.0F, 0.0F, (float) (-Math.PI * 2.0 / 9.0), 0.0F));
        root.addOrReplaceChild(
                "t13",
                CubeListBuilder.create().texOffs(0, 31).addBox(-4.0F, -4.0F, 0.0F, 8.0F, 8.0F, 10.0F),
                PartPose.offsetAndRotation(-4.0F, 20.0F, 80.0F, 0.0F, -0.3490659F, 0.0F));
        root.addOrReplaceChild(
                "t14",
                CubeListBuilder.create().texOffs(0, 51).addBox(-3.0F, -3.0F, 0.0F, 6.0F, 7.0F, 10.0F),
                PartPose.offset(-8.0F, 20.0F, 88.0F));
        root.addOrReplaceChild(
                "t15",
                CubeListBuilder.create().texOffs(0, 51).addBox(-3.0F, -3.0F, 0.0F, 6.0F, 7.0F, 10.0F),
                PartPose.offsetAndRotation(-8.0F, 20.0F, 97.0F, 0.0F, 0.3490659F, 0.0F));
        root.addOrReplaceChild(
                "t16",
                CubeListBuilder.create().texOffs(0, 51).addBox(-3.0F, -3.0F, 0.0F, 6.0F, 7.0F, 10.0F),
                PartPose.offsetAndRotation(-5.0F, 20.0F, 105.0F, 0.0F, (float) (Math.PI * 2.0 / 9.0), 0.0F));
        root.addOrReplaceChild(
                "t17",
                CubeListBuilder.create().texOffs(0, 70).addBox(-2.0F, -2.0F, 0.0F, 4.0F, 6.0F, 10.0F),
                PartPose.offsetAndRotation(1.0F, 20.0F, 112.0F, 0.0F, (float) (Math.PI * 2.0 / 9.0), 0.0F));
        root.addOrReplaceChild(
                "t18",
                CubeListBuilder.create().texOffs(0, 70).addBox(-2.0F, -2.0F, 0.0F, 4.0F, 6.0F, 10.0F),
                PartPose.offsetAndRotation(7.0F, 20.0F, 119.0F, 0.0F, 0.3490659F, 0.0F));
        root.addOrReplaceChild(
                "t19",
                CubeListBuilder.create().texOffs(0, 70).addBox(-2.0F, -2.0F, 0.0F, 4.0F, 6.0F, 10.0F),
                PartPose.offset(10.0F, 20.0F, 127.0F));
        root.addOrReplaceChild(
                "t20",
                CubeListBuilder.create().texOffs(0, 90).addBox(-1.0F, -1.0F, 0.0F, 2.0F, 5.0F, 10.0F),
                PartPose.offsetAndRotation(10.0F, 20.0F, 136.0F, 0.0F, -0.3490659F, 0.0F));
        root.addOrReplaceChild(
                "t21",
                CubeListBuilder.create().texOffs(0, 90).addBox(-1.0F, -1.0F, 0.0F, 2.0F, 5.0F, 10.0F),
                PartPose.offsetAndRotation(7.0F, 20.0F, 145.0F, 0.0F, (float) (-Math.PI * 2.0 / 9.0), 0.0F));
        root.addOrReplaceChild(
                "mouth_bottom",
                CubeListBuilder.create().texOffs(58, 78).addBox(-4.0F, 0.0F, -12.0F, 8.0F, 2.0F, 12.0F),
                PartPose.offsetAndRotation(0.0F, MOUTH_BOT_Y, MOUTH_BOT_Z, (float) (Math.PI / 6), 0.0F, 0.0F));
        root.addOrReplaceChild(
                "toung_base",
                CubeListBuilder.create().texOffs(70, 17).addBox(-1.0F, -2.0F, -11.0F, 2.0F, 1.0F, 6.0F),
                PartPose.offsetAndRotation(0.0F, HEAD_Y, HEAD_Z, (float) (Math.PI / 12), 0.0F, 0.0F));
        root.addOrReplaceChild(
                "middle_tounge",
                CubeListBuilder.create().texOffs(70, 10).addBox(-1.0F, -1.0F, -17.0F, 2.0F, 1.0F, 6.0F),
                PartPose.offsetAndRotation(0.0F, HEAD_Y, HEAD_Z, 0.1745329F, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "eye_right",
                CubeListBuilder.create().texOffs(96, 60).addBox(-7.0F, -7.0F, -3.0F, 1.0F, 3.0F, 4.0F),
                PartPose.offsetAndRotation(0.0F, HEAD_Y, HEAD_Z, 0.3490659F, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "eye_left",
                CubeListBuilder.create().texOffs(50, 60).addBox(6.0F, -7.0F, -3.0F, 1.0F, 3.0F, 4.0F),
                PartPose.offsetAndRotation(0.0F, HEAD_Y, HEAD_Z, 0.3490659F, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "mouth_top",
                CubeListBuilder.create().texOffs(52, 24).addBox(-5.0F, -6.0F, -16.0F, 10.0F, 6.0F, 16.0F),
                PartPose.offset(0.0F, HEAD_Y, HEAD_Z));
        root.addOrReplaceChild(
                "head",
                CubeListBuilder.create().texOffs(60, 46).addBox(-6.0F, -8.0F, -6.0F, 12.0F, 8.0F, 6.0F),
                PartPose.offset(0.0F, HEAD_Y, HEAD_Z));
        root.addOrReplaceChild(
                "fang_right",
                CubeListBuilder.create().texOffs(92, 18).addBox(-4.0F, -3.0F, -15.0F, 1.0F, 5.0F, 1.0F),
                PartPose.offsetAndRotation(0.0F, HEAD_Y, HEAD_Z, 0.1745329F, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "fang_left",
                CubeListBuilder.create().texOffs(60, 18).addBox(3.0F, -3.0F, -15.0F, 1.0F, 5.0F, 1.0F),
                PartPose.offsetAndRotation(0.0F, HEAD_Y, HEAD_Z, 0.1745329F, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "fork_right",
                CubeListBuilder.create().texOffs(60, 3).addBox(6.0F, 0.6F, -21.0F, 2.0F, 1.0F, 6.0F),
                PartPose.offsetAndRotation(0.0F, HEAD_Y, HEAD_Z, 0.0872665F, 0.4363323F, 0.0F));
        root.addOrReplaceChild(
                "fork_left",
                CubeListBuilder.create().texOffs(80, 3).addBox(-8.0F, 0.6F, -21.0F, 2.0F, 1.0F, 6.0F),
                PartPose.offsetAndRotation(0.0F, HEAD_Y, HEAD_Z, 0.0872665F, -0.4363323F, 0.0F));

        return LayerDefinition.create(mesh, 128, 128);
    }

    @Override
    public ModelPart root() {
        return this.root;
    }

    @Override
    public void setupAnim(
            SeaViper entity,
            float limbSwing,
            float limbSwingAmount,
            float ageInTicks,
            float netHeadYaw,
            float headPitch) {
        // gold func_78088_a — f1=limbSwingAmount, f2=ageInTicks, f3=netHeadYaw
        float f1 = limbSwingAmount;
        float f2 = ageInTicks;
        if (f1 < 0.0F) {
            f1 = 0.0F;
        }

        float newangle = Mth.cos(f2 * 1.3F * this.wingspeed) * (float) Math.PI * 0.1F * f1;
        this.tBase.x = TBASE_X;
        this.tBase.y = TBASE_Y;
        this.tBase.z = TBASE_Z;
        this.tBase.xRot = (float) (-Math.PI / 6);
        this.tBase.yRot = newangle;

        // restore static xRot on segments (doseg only rewrites yRot + x/z)
        this.t2.xRot = -1.047198F;
        this.t3.xRot = (float) (-Math.PI / 6);
        this.t4.xRot = -0.0872665F;
        this.t5.xRot = 0.0F;
        this.t6.xRot = 0.0F;
        this.t7.xRot = 0.0F;
        this.t8.xRot = 0.0F;
        this.t9.xRot = 0.0F;
        this.t10.xRot = 0.0F;
        this.t11.xRot = 0.0F;
        this.t12.xRot = 0.0F;
        this.t13.xRot = 0.0F;
        this.t14.xRot = 0.0F;
        this.t15.xRot = 0.0F;
        this.t16.xRot = 0.0F;
        this.t17.xRot = 0.0F;
        this.t18.xRot = 0.0F;
        this.t19.xRot = 0.0F;
        this.t20.xRot = 0.0F;
        this.t21.xRot = 0.0F;
        this.tailTip.xRot = 0.0F;

        // gold phase indices: tBase→t2 and t2→t3 both use 2.0F, then 3.21 to TailTip
        this.doseg(this.tBase, this.t2, 2.0F, f1, f2);
        this.doseg(this.t2, this.t3, 2.0F, f1, f2);
        this.doseg(this.t3, this.t4, 3.0F, f1, f2);
        this.doseg(this.t4, this.t5, 4.0F, f1, f2);
        this.doseg(this.t5, this.t6, 5.0F, f1, f2);
        this.doseg(this.t6, this.t7, 6.0F, f1, f2);
        this.doseg(this.t7, this.t8, 7.0F, f1, f2);
        this.doseg(this.t8, this.t9, 8.0F, f1, f2);
        this.doseg(this.t9, this.t10, 9.0F, f1, f2);
        this.doseg(this.t10, this.t11, 10.0F, f1, f2);
        this.doseg(this.t11, this.t12, 11.0F, f1, f2);
        this.doseg(this.t12, this.t13, 12.0F, f1, f2);
        this.doseg(this.t13, this.t14, 13.0F, f1, f2);
        this.doseg(this.t14, this.t15, 14.0F, f1, f2);
        this.doseg(this.t15, this.t16, 15.0F, f1, f2);
        this.doseg(this.t16, this.t17, 16.0F, f1, f2);
        this.doseg(this.t17, this.t18, 17.0F, f1, f2);
        this.doseg(this.t18, this.t19, 18.0F, f1, f2);
        this.doseg(this.t19, this.t20, 19.0F, f1, f2);
        this.doseg(this.t20, this.t21, 20.0F, f1, f2);
        this.doseg(this.t21, this.tailTip, 21.0F, f1, f2);

        // jaw / tongue attack vs idle
        float tongueZOffset;
        if (entity.getAttacking() != 0) {
            newangle = Mth.cos(f2 * 1.7F * this.wingspeed) * (float) Math.PI * 0.17F;
            this.mouthBottom.xRot = 0.65F + newangle;
            newangle = Mth.cos(f2 * 4.7F * this.wingspeed) * (float) Math.PI * 0.07F;
            this.toungBase.xRot = 0.261F + newangle;
            this.middleTounge.xRot = 0.174F + newangle;
            this.forkLeft.xRot = 0.087F + newangle;
            this.forkRight.xRot = 0.087F + newangle;
            // gold field_82907_q = offsetZ
            tongueZOffset = Mth.cos(f2 * 1.5F * this.wingspeed) * (float) Math.PI * 0.05F;
        } else {
            newangle = Mth.cos(f2 * 0.2F * this.wingspeed) * (float) Math.PI * 0.02F;
            this.mouthBottom.xRot = 0.45F + newangle;
            newangle = Mth.cos(f2 * 1.7F * this.wingspeed) * (float) Math.PI * 0.03F;
            this.toungBase.xRot = 0.261F + newangle;
            this.middleTounge.xRot = 0.174F + newangle;
            this.forkLeft.xRot = 0.087F + newangle;
            this.forkRight.xRot = 0.087F + newangle;
            tongueZOffset = Mth.cos(f2 * 0.5F * this.wingspeed) * (float) Math.PI * 0.05F;
        }

        // head group base pivots first (gold Head at 0,6,-8 after Z+32)
        this.head.x = this.mouthTop.x = this.eyeLeft.x = this.eyeRight.x = HEAD_X;
        this.head.y = this.mouthTop.y = this.eyeLeft.y = this.eyeRight.y = HEAD_Y;
        this.head.z = this.mouthTop.z = this.eyeLeft.z = this.eyeRight.z = HEAD_Z;
        this.fangLeft.x = this.fangRight.x = HEAD_X;
        this.fangLeft.y = this.fangRight.y = HEAD_Y;
        this.fangLeft.z = this.fangRight.z = HEAD_Z;
        this.fangLeft.xRot = 0.1745329F;
        this.fangRight.xRot = 0.1745329F;
        this.eyeLeft.xRot = 0.3490659F;
        this.eyeRight.xRot = 0.3490659F;
        this.mouthTop.xRot = 0.0F;
        this.head.xRot = 0.0F;

        // head yaw (f3)
        newangle = (float) Math.toRadians(netHeadYaw) * 0.5F;
        this.head.yRot = this.mouthTop.yRot = this.eyeLeft.yRot = this.eyeRight.yRot = newangle;
        this.fangLeft.yRot = this.fangRight.yRot = newangle;
        this.mouthBottom.yRot = newangle;
        this.mouthBottom.z = this.head.z - (float) Math.cos(this.head.yRot) * 2.0F;
        this.mouthBottom.x = this.head.x - (float) Math.sin(this.head.yRot) * 2.0F;
        this.mouthBottom.y = MOUTH_BOT_Y;

        this.toungBase.yRot = newangle;
        this.middleTounge.yRot = newangle;
        this.forkLeft.yRot = newangle - 0.436F;
        this.forkRight.yRot = newangle + 0.436F;

        // tongue z gets gold field_82907_q offsetZ
        this.toungBase.x = this.middleTounge.x = this.forkLeft.x = this.forkRight.x = HEAD_X;
        this.toungBase.y = this.middleTounge.y = this.forkLeft.y = this.forkRight.y = HEAD_Y;
        this.toungBase.z = this.middleTounge.z = this.forkLeft.z = this.forkRight.z = TONGUE_BASE_Z + tongueZOffset;

        // neck static pitch
        this.neck.xRot = (float) (-Math.PI / 12);
        this.neck.yRot = 0.0F;
    }

    /**
     * Gold {@code doseg}: place {@code notinn} off {@code inn} by 9 along yaw, with pitch-scaled length;
     * overwrite notinn.yRot with delayed wave (index f).
     */
    private void doseg(ModelPart inn, ModelPart notinn, float f, float f1, float f2) {
        float pi4 = (float) (Math.PI / 4);
        notinn.z = (float) (inn.z + (float) Math.cos(inn.yRot) * (9.0 * Math.abs(Math.cos(inn.xRot))));
        notinn.x = (float) (inn.x + (float) Math.sin(inn.yRot) * 9.0F * Math.abs(Math.cos(inn.xRot)));
        // gold does not rewrite y — keep layer y; horizontal body segments stay at y=20 after t5
        float newangle = Mth.cos(f2 * 1.3F * this.wingspeed - pi4 * f) * (float) Math.PI * 0.2F * f1;
        float a = Mth.cos(-(pi4 * f));
        notinn.yRot = newangle + a - a * f1;
    }
}
