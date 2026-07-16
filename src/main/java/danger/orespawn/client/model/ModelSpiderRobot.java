package danger.orespawn.client.model;

import danger.orespawn.entity.SpiderRobot;
import danger.orespawn.entity.SpiderRobot.SpiderLegInfo;
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
 * Port of gold {@code ModelSpiderRobot} (1.7.10 ModelBase, tex 256×512) → 1.21 HierarchicalModel.
 * Cubes/UVs/offsets/base rotations 1:1. Gold reuses one leg chain ×8 in render; here each of 8
 * legs is a separate instance so setupAnim matches without multi-pass render.
 * ClientProxy wingspeed {@code 1.0F}. Full gold anim: IK legs + jaw attack/idle.
 */
@OnlyIn(Dist.CLIENT)
public class ModelSpiderRobot extends HierarchicalModel<SpiderRobot> {
    /** Model layer location. */
    public static final ModelLayerLocation LAYER_LOCATION =
            new ModelLayerLocation(
                    ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "spider_robot"), "main");

    private final float wingspeed;
    private final ModelPart root;

    private final ModelPart[] Leg1p1 = new ModelPart[8];
    private final ModelPart[] Leg1p2 = new ModelPart[8];
    private final ModelPart[] Leg1p3 = new ModelPart[8];
    private final ModelPart[] Foot = new ModelPart[8];
    private final ModelPart[] FootSpike1 = new ModelPart[8];
    private final ModelPart[] FootSpike2 = new ModelPart[8];
    private final ModelPart[] FootSpike3 = new ModelPart[8];
    private final ModelPart[] FootSpike4 = new ModelPart[8];
    private final ModelPart[] AnkleSpike1 = new ModelPart[8];
    private final ModelPart[] AnkleSpike2 = new ModelPart[8];
    private final ModelPart[] AnkleSpike3 = new ModelPart[8];
    private final ModelPart[] AnkleSpike4 = new ModelPart[8];
    private final ModelPart[] LowerKnee = new ModelPart[8];
    private final ModelPart[] UpperKnee = new ModelPart[8];
    private final ModelPart[] LegBump1 = new ModelPart[8];
    private final ModelPart[] LegBump2 = new ModelPart[8];
    private final ModelPart[] LowerKnee2 = new ModelPart[8];
    private final ModelPart[] UpperKnee2 = new ModelPart[8];
    private final ModelPart[] HipJoint = new ModelPart[8];

    private final ModelPart BodyCenter;
    private final ModelPart Abdomen;
    private final ModelPart Head;
    private final ModelPart Ljaw1;
    private final ModelPart Rjaw1;
    private final ModelPart Ljaw2;
    private final ModelPart Rjaw2;
    private final ModelPart Ljaw3;
    private final ModelPart Rjaw3;
    private final ModelPart Tail;
    private final ModelPart HeadSpike1;
    private final ModelPart HeadSpike2;
    private final ModelPart Hip1;
    private final ModelPart Hip2;
    private final ModelPart Hip3;
    private final ModelPart Hip4;
    private final ModelPart Hip5;
    private final ModelPart Hip6;
    private final ModelPart Hip7;
    private final ModelPart Hip8;

    public ModelSpiderRobot(ModelPart root) {
        this(root, 1.0F);
    }

    public ModelSpiderRobot(ModelPart root, float wingspeed) {
        this.root = root;
        this.wingspeed = wingspeed;

        for (int i = 0; i < 8; i++) {
            this.Leg1p1[i] = root.getChild("Leg1p1_" + i);
            this.Leg1p2[i] = root.getChild("Leg1p2_" + i);
            this.Leg1p3[i] = root.getChild("Leg1p3_" + i);
            this.Foot[i] = root.getChild("Foot_" + i);
            this.FootSpike1[i] = root.getChild("FootSpike1_" + i);
            this.FootSpike2[i] = root.getChild("FootSpike2_" + i);
            this.FootSpike3[i] = root.getChild("FootSpike3_" + i);
            this.FootSpike4[i] = root.getChild("FootSpike4_" + i);
            this.AnkleSpike1[i] = root.getChild("AnkleSpike1_" + i);
            this.AnkleSpike2[i] = root.getChild("AnkleSpike2_" + i);
            this.AnkleSpike3[i] = root.getChild("AnkleSpike3_" + i);
            this.AnkleSpike4[i] = root.getChild("AnkleSpike4_" + i);
            this.LowerKnee[i] = root.getChild("LowerKnee_" + i);
            this.UpperKnee[i] = root.getChild("UpperKnee_" + i);
            this.LegBump1[i] = root.getChild("LegBump1_" + i);
            this.LegBump2[i] = root.getChild("LegBump2_" + i);
            this.LowerKnee2[i] = root.getChild("LowerKnee2_" + i);
            this.UpperKnee2[i] = root.getChild("UpperKnee2_" + i);
            this.HipJoint[i] = root.getChild("HipJoint_" + i);
        }

        this.BodyCenter = root.getChild("BodyCenter");
        this.Abdomen = root.getChild("Abdomen");
        this.Head = root.getChild("Head");
        this.Ljaw1 = root.getChild("Ljaw1");
        this.Rjaw1 = root.getChild("Rjaw1");
        this.Ljaw2 = root.getChild("Ljaw2");
        this.Rjaw2 = root.getChild("Rjaw2");
        this.Ljaw3 = root.getChild("Ljaw3");
        this.Rjaw3 = root.getChild("Rjaw3");
        this.Tail = root.getChild("Tail");
        this.HeadSpike1 = root.getChild("HeadSpike1");
        this.HeadSpike2 = root.getChild("HeadSpike2");
        this.Hip1 = root.getChild("Hip1");
        this.Hip2 = root.getChild("Hip2");
        this.Hip3 = root.getChild("Hip3");
        this.Hip4 = root.getChild("Hip4");
        this.Hip5 = root.getChild("Hip5");
        this.Hip6 = root.getChild("Hip6");
        this.Hip7 = root.getChild("Hip7");
        this.Hip8 = root.getChild("Hip8");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        // Gold reuses one leg chain 8×; expand to *_i for HierarchicalModel.
        for (int i = 0; i < 8; i++) {
            root.addOrReplaceChild(
                    "Leg1p1_" + i,
                    CubeListBuilder.create().texOffs(0, 149).mirror()
                            .addBox(-2.0F, -2.0F, 0.0F, 4.0F, 4.0F, 100.0F),
                    PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, (float) (Math.PI / 4), 0.0F, 0.0F));
            root.addOrReplaceChild(
                    "Leg1p2_" + i,
                    CubeListBuilder.create().texOffs(0, 149).mirror()
                            .addBox(-1.5F, -1.5F, 0.0F, 3.0F, 3.0F, 100.0F),
                    PartPose.offset(0.0F, -70.0F, 70.0F));
            root.addOrReplaceChild(
                    "Leg1p3_" + i,
                    CubeListBuilder.create().texOffs(0, 149).mirror()
                            .addBox(-1.0F, -1.0F, 0.0F, 2.0F, 2.0F, 100.0F),
                    PartPose.offsetAndRotation(0.0F, -70.0F, 169.0F, (float) (-Math.PI / 4), 0.0F, 0.0F));
            root.addOrReplaceChild(
                    "Foot_" + i,
                    CubeListBuilder.create().texOffs(0, 28).mirror()
                            .addBox(-3.0F, -3.0F, 93.0F, 6.0F, 6.0F, 6.0F),
                    PartPose.offsetAndRotation(0.0F, -70.0F, 169.0F, (float) (-Math.PI / 4), 0.0F, 0.0F));
            root.addOrReplaceChild(
                    "FootSpike1_" + i,
                    CubeListBuilder.create().texOffs(29, 27).mirror()
                            .addBox(2.0F, 2.0F, 99.0F, 1.0F, 1.0F, 5.0F),
                    PartPose.offsetAndRotation(0.0F, -70.0F, 169.0F, (float) (-Math.PI / 4), 0.0F, 0.0F));
            root.addOrReplaceChild(
                    "FootSpike2_" + i,
                    CubeListBuilder.create().texOffs(29, 34).mirror()
                            .addBox(-3.0F, 2.0F, 99.0F, 1.0F, 1.0F, 5.0F),
                    PartPose.offsetAndRotation(0.0F, -70.0F, 169.0F, (float) (-Math.PI / 4), 0.0F, 0.0F));
            root.addOrReplaceChild(
                    "FootSpike3_" + i,
                    CubeListBuilder.create().texOffs(43, 27).mirror()
                            .addBox(2.0F, -3.0F, 99.0F, 1.0F, 1.0F, 5.0F),
                    PartPose.offsetAndRotation(0.0F, -70.0F, 169.0F, (float) (-Math.PI / 4), 0.0F, 0.0F));
            root.addOrReplaceChild(
                    "FootSpike4_" + i,
                    CubeListBuilder.create().texOffs(43, 34).mirror()
                            .addBox(-3.0F, -3.0F, 99.0F, 1.0F, 1.0F, 5.0F),
                    PartPose.offsetAndRotation(0.0F, -70.0F, 169.0F, (float) (-Math.PI / 4), 0.0F, 0.0F));
            root.addOrReplaceChild(
                    "AnkleSpike1_" + i,
                    CubeListBuilder.create().texOffs(1, 42).mirror()
                            .addBox(3.0F, -10.0F, 92.0F, 1.0F, 20.0F, 1.0F),
                    PartPose.offsetAndRotation(0.0F, -70.0F, 169.0F, (float) (-Math.PI / 4), 0.0F, 0.0F));
            root.addOrReplaceChild(
                    "AnkleSpike2_" + i,
                    CubeListBuilder.create().texOffs(7, 42).mirror()
                            .addBox(-4.0F, -10.0F, 92.0F, 1.0F, 20.0F, 1.0F),
                    PartPose.offsetAndRotation(0.0F, -70.0F, 169.0F, (float) (-Math.PI / 4), 0.0F, 0.0F));
            root.addOrReplaceChild(
                    "AnkleSpike3_" + i,
                    CubeListBuilder.create().texOffs(14, 42).mirror()
                            .addBox(-10.0F, 3.0F, 92.0F, 20.0F, 1.0F, 1.0F),
                    PartPose.offsetAndRotation(0.0F, -70.0F, 169.0F, (float) (-Math.PI / 4), 0.0F, 0.0F));
            root.addOrReplaceChild(
                    "AnkleSpike4_" + i,
                    CubeListBuilder.create().texOffs(14, 46).mirror()
                            .addBox(-10.0F, -4.0F, 92.0F, 20.0F, 1.0F, 1.0F),
                    PartPose.offsetAndRotation(0.0F, -70.0F, 169.0F, (float) (-Math.PI / 4), 0.0F, 0.0F));
            root.addOrReplaceChild(
                    "LowerKnee_" + i,
                    CubeListBuilder.create().texOffs(14, 49).mirror()
                            .addBox(-1.5F, -1.5F, -1.0F, 3.0F, 3.0F, 15.0F),
                    PartPose.offsetAndRotation(0.0F, -70.0F, 169.0F, (float) (-Math.PI / 4), 0.0F, 0.0F));
            root.addOrReplaceChild(
                    "UpperKnee_" + i,
                    CubeListBuilder.create().texOffs(0, 69).mirror()
                            .addBox(-2.5F, -2.5F, 81.0F, 5.0F, 5.0F, 20.0F),
                    PartPose.offset(0.0F, -70.0F, 70.0F));
            root.addOrReplaceChild(
                    "LegBump1_" + i,
                    CubeListBuilder.create().texOffs(52, 50).mirror()
                            .addBox(-0.5F, -2.0F, 80.0F, 1.0F, 1.0F, 1.0F),
                    PartPose.offsetAndRotation(0.0F, -70.0F, 169.0F, (float) (-Math.PI / 4), 0.0F, 0.0F));
            root.addOrReplaceChild(
                    "LegBump2_" + i,
                    CubeListBuilder.create().texOffs(52, 54).mirror()
                            .addBox(-0.5F, -2.0F, 70.0F, 1.0F, 1.0F, 1.0F),
                    PartPose.offsetAndRotation(0.0F, -70.0F, 169.0F, (float) (-Math.PI / 4), 0.0F, 0.0F));
            root.addOrReplaceChild(
                    "LowerKnee2_" + i,
                    CubeListBuilder.create().texOffs(0, 96).mirror()
                            .addBox(-2.5F, -2.5F, -1.0F, 5.0F, 5.0F, 15.0F),
                    PartPose.offset(0.0F, -70.0F, 70.0F));
            root.addOrReplaceChild(
                    "UpperKnee2_" + i,
                    CubeListBuilder.create().texOffs(0, 119).mirror()
                            .addBox(-3.0F, -3.0F, 81.0F, 6.0F, 6.0F, 20.0F),
                    PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, (float) (Math.PI / 4), 0.0F, 0.0F));
            root.addOrReplaceChild(
                    "HipJoint_" + i,
                    CubeListBuilder.create().texOffs(0, 149).mirror()
                            .addBox(-4.0F, -4.0F, 0.0F, 8.0F, 8.0F, 16.0F),
                    PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, (float) (Math.PI / 4), 0.0F, 0.0F));
        }

        root.addOrReplaceChild(
                "BodyCenter",
                CubeListBuilder.create().texOffs(0, 321).mirror()
                        .addBox(-18.0F, -12.0F, -21.0F, 36.0F, 24.0F, 51.0F),
                PartPose.offset(0.0F, -4.0F, 0.0F));
        root.addOrReplaceChild(
                "Abdomen",
                CubeListBuilder.create().texOffs(0, 398).mirror()
                        .addBox(-24.0F, -30.0F, 29.0F, 48.0F, 40.0F, 73.0F),
                PartPose.offset(0.0F, -4.0F, 0.0F));
        root.addOrReplaceChild(
                "Head",
                CubeListBuilder.create().texOffs(0, 256).mirror()
                        .addBox(-15.0F, -16.0F, -57.0F, 30.0F, 26.0F, 36.0F),
                PartPose.offset(0.0F, -4.0F, 0.0F));
        root.addOrReplaceChild(
                "Ljaw1",
                CubeListBuilder.create().texOffs(75, 26).mirror()
                        .addBox(-4.0F, 0.0F, -4.0F, 8.0F, 3.0F, 8.0F),
                PartPose.offset(14.0F, -3.0F, -56.0F));
        root.addOrReplaceChild(
                "Rjaw1",
                CubeListBuilder.create().texOffs(75, 26).mirror()
                        .addBox(-4.0F, 0.0F, -4.0F, 8.0F, 3.0F, 8.0F),
                PartPose.offset(-14.0F, -3.0F, -56.0F));
        root.addOrReplaceChild(
                "Ljaw2",
                CubeListBuilder.create().texOffs(63, 40).mirror()
                        .addBox(0.0F, 1.0F, -3.0F, 21.0F, 2.0F, 6.0F),
                PartPose.offsetAndRotation(14.0F, -3.0F, -56.0F, 0.0F, 0.7504916F, 0.0F));
        root.addOrReplaceChild(
                "Rjaw2",
                CubeListBuilder.create().texOffs(63, 40).mirror()
                        .addBox(0.0F, 1.0F, -3.0F, 21.0F, 2.0F, 6.0F),
                PartPose.offsetAndRotation(-14.0F, -3.0F, -56.0F, 0.0F, 2.303835F, 0.0F));
        root.addOrReplaceChild(
                "Ljaw3",
                CubeListBuilder.create().texOffs(0, 18).mirror()
                        .addBox(11.0F, 2.0F, 14.0F, 23.0F, 1.0F, 4.0F),
                PartPose.offsetAndRotation(14.0F, -3.0F, -56.0F, 0.0F, 1.710423F, 0.0F));
        root.addOrReplaceChild(
                "Rjaw3",
                CubeListBuilder.create().texOffs(0, 18).mirror()
                        .addBox(11.0F, 2.0F, -17.0F, 23.0F, 1.0F, 4.0F),
                PartPose.offsetAndRotation(-14.0F, -3.0F, -56.0F, 0.0F, 1.413717F, 0.0F));
        root.addOrReplaceChild(
                "Tail",
                CubeListBuilder.create().texOffs(130, 0).mirror()
                        .addBox(-5.0F, -5.0F, -5.0F, 10.0F, 10.0F, 49.0F),
                PartPose.offset(0.0F, -32.0F, 69.0F));
        root.addOrReplaceChild(
                "HeadSpike1",
                CubeListBuilder.create().texOffs(74, 0).mirror()
                        .addBox(-1.0F, -1.0F, -10.0F, 2.0F, 2.0F, 21.0F),
                PartPose.offset(6.0F, -20.0F, -60.0F));
        root.addOrReplaceChild(
                "HeadSpike2",
                CubeListBuilder.create().texOffs(74, 0).mirror()
                        .addBox(-1.0F, -1.0F, -10.0F, 2.0F, 2.0F, 21.0F),
                PartPose.offset(-6.0F, -20.0F, -60.0F));
        root.addOrReplaceChild(
                "Hip1",
                CubeListBuilder.create().texOffs(70, 60).mirror()
                        .addBox(0.0F, 0.0F, 0.0F, 10.0F, 10.0F, 10.0F),
                PartPose.offset(22.0F, -3.0F, 44.0F));
        root.addOrReplaceChild(
                "Hip2",
                CubeListBuilder.create().texOffs(70, 60).mirror()
                        .addBox(0.0F, 0.0F, 0.0F, 10.0F, 10.0F, 10.0F),
                PartPose.offset(-32.0F, -3.0F, 44.0F));
        root.addOrReplaceChild(
                "Hip3",
                CubeListBuilder.create().texOffs(70, 60).mirror()
                        .addBox(0.0F, 0.0F, 0.0F, 10.0F, 10.0F, 10.0F),
                PartPose.offset(16.0F, -1.0F, 12.0F));
        root.addOrReplaceChild(
                "Hip4",
                CubeListBuilder.create().texOffs(70, 60).mirror()
                        .addBox(0.0F, 0.0F, 0.0F, 10.0F, 10.0F, 10.0F),
                PartPose.offset(-26.0F, -1.0F, 12.0F));
        root.addOrReplaceChild(
                "Hip5",
                CubeListBuilder.create().texOffs(70, 60).mirror()
                        .addBox(0.0F, 0.0F, 0.0F, 10.0F, 10.0F, 10.0F),
                PartPose.offset(16.0F, -1.0F, -11.0F));
        root.addOrReplaceChild(
                "Hip6",
                CubeListBuilder.create().texOffs(70, 60).mirror()
                        .addBox(0.0F, 0.0F, 0.0F, 10.0F, 10.0F, 10.0F),
                PartPose.offset(-26.0F, -1.0F, -11.0F));
        root.addOrReplaceChild(
                "Hip7",
                CubeListBuilder.create().texOffs(70, 60).mirror()
                        .addBox(0.0F, 0.0F, 0.0F, 10.0F, 10.0F, 10.0F),
                PartPose.offset(13.0F, -3.0F, -33.0F));
        root.addOrReplaceChild(
                "Hip8",
                CubeListBuilder.create().texOffs(70, 60).mirror()
                        .addBox(0.0F, 0.0F, 0.0F, 10.0F, 10.0F, 10.0F),
                PartPose.offset(-23.0F, -3.0F, -33.0F));

        return LayerDefinition.create(mesh, 256, 512);
    }

    @Override
    public ModelPart root() {
        return this.root;
    }

    /**
     * Full gold {@code func_78088_a} animation (8-leg IK + jaws).
     * Segment length 99 (gold). {@code wingspeed} reserved.
     */
    @Override
    public void setupAnim(
            SpiderRobot entity,
            float limbSwing,
            float limbSwingAmount,
            float ageInTicks,
            float netHeadYaw,
            float headPitch) {
        SpiderLegInfo r = entity.getRenderSpiderRobotInfo();
        if (r == null) {
            return;
        }

        // Ensure client IK has run at least once (entity.aiStep also updates).
        if (r.gpcounter == 0) {
            entity.updateLegs();
            r = entity.getRenderSpiderRobotInfo();
        }

        for (int i = 0; i < 8; i++) {
            float yDisp = r.ydisplayangle[i];
            float p1 = (float) r.p1xangle[i] + r.uddisplayangle[i];
            float p2 = (float) r.p2xangle[i] + r.uddisplayangle[i];
            float p3 = (float) r.p3xangle[i] + r.uddisplayangle[i];

            this.Leg1p1[i].yRot = yDisp;
            this.Leg1p2[i].yRot = yDisp;
            this.Leg1p3[i].yRot = yDisp;
            this.Foot[i].yRot = yDisp;
            this.FootSpike1[i].yRot = yDisp;
            this.FootSpike2[i].yRot = yDisp;
            this.FootSpike3[i].yRot = yDisp;
            this.FootSpike4[i].yRot = yDisp;
            this.AnkleSpike1[i].yRot = yDisp;
            this.AnkleSpike2[i].yRot = yDisp;
            this.AnkleSpike3[i].yRot = yDisp;
            this.AnkleSpike4[i].yRot = yDisp;
            this.LowerKnee[i].yRot = yDisp;
            this.UpperKnee[i].yRot = yDisp;
            this.LegBump1[i].yRot = yDisp;
            this.LegBump2[i].yRot = yDisp;
            this.LowerKnee2[i].yRot = yDisp;
            this.UpperKnee2[i].yRot = yDisp;
            this.HipJoint[i].yRot = yDisp;

            this.Leg1p1[i].xRot = p1;
            this.UpperKnee2[i].xRot = p1;
            this.HipJoint[i].xRot = p1;
            this.Leg1p2[i].xRot = p2;
            this.UpperKnee[i].xRot = p2;
            this.LowerKnee2[i].xRot = p2;
            this.Leg1p3[i].xRot = p3;
            this.Foot[i].xRot = p3;
            this.FootSpike1[i].xRot = p3;
            this.FootSpike2[i].xRot = p3;
            this.FootSpike3[i].xRot = p3;
            this.FootSpike4[i].xRot = p3;
            this.AnkleSpike1[i].xRot = p3;
            this.AnkleSpike2[i].xRot = p3;
            this.AnkleSpike3[i].xRot = p3;
            this.AnkleSpike4[i].xRot = p3;
            this.LegBump1[i].xRot = p3;
            this.LegBump2[i].xRot = p3;
            this.LowerKnee[i].xRot = p3;

            // gold dynamic pivots (segment 99)
            this.Leg1p1[i].x = -((float) Math.cos(r.ymid[i])) * r.legoff[i] * 16.0F;
            this.Leg1p1[i].z = (float) Math.sin(r.ymid[i]) * r.legoff[i] * 16.0F;
            this.Leg1p1[i].y = r.yoff[i] * -16.0F;

            this.UpperKnee2[i].x = this.Leg1p1[i].x;
            this.UpperKnee2[i].y = this.Leg1p1[i].y;
            this.UpperKnee2[i].z = this.Leg1p1[i].z;
            this.HipJoint[i].x = this.Leg1p1[i].x;
            this.HipJoint[i].y = this.Leg1p1[i].y;
            this.HipJoint[i].z = this.Leg1p1[i].z;

            this.Leg1p2[i].y = this.Leg1p1[i].y - (float) Math.sin(this.Leg1p1[i].xRot) * 99.0F;
            this.Leg1p2[i].z = this.Leg1p1[i].z
                    + (float) Math.cos(this.Leg1p1[i].xRot)
                    * (float) Math.cos(this.Leg1p1[i].yRot)
                    * 99.0F;
            this.Leg1p2[i].x = this.Leg1p1[i].x
                    + (float) Math.cos(this.Leg1p1[i].xRot)
                    * (float) Math.sin(this.Leg1p1[i].yRot)
                    * 99.0F;

            this.UpperKnee[i].x = this.Leg1p2[i].x;
            this.UpperKnee[i].y = this.Leg1p2[i].y;
            this.UpperKnee[i].z = this.Leg1p2[i].z;
            this.LowerKnee2[i].x = this.Leg1p2[i].x;
            this.LowerKnee2[i].y = this.Leg1p2[i].y;
            this.LowerKnee2[i].z = this.Leg1p2[i].z;

            this.Leg1p3[i].y = this.Leg1p2[i].y - (float) Math.sin(this.Leg1p2[i].xRot) * 99.0F;
            this.Leg1p3[i].z = this.Leg1p2[i].z
                    + (float) Math.cos(this.Leg1p2[i].xRot)
                    * (float) Math.cos(this.Leg1p2[i].yRot)
                    * 99.0F;
            this.Leg1p3[i].x = this.Leg1p2[i].x
                    + (float) Math.cos(this.Leg1p2[i].xRot)
                    * (float) Math.sin(this.Leg1p2[i].yRot)
                    * 99.0F;

            this.Foot[i].x = this.Leg1p3[i].x;
            this.Foot[i].y = this.Leg1p3[i].y;
            this.Foot[i].z = this.Leg1p3[i].z;
            this.FootSpike1[i].x = this.Leg1p3[i].x;
            this.FootSpike1[i].y = this.Leg1p3[i].y;
            this.FootSpike1[i].z = this.Leg1p3[i].z;
            this.FootSpike2[i].x = this.Leg1p3[i].x;
            this.FootSpike2[i].y = this.Leg1p3[i].y;
            this.FootSpike2[i].z = this.Leg1p3[i].z;
            this.FootSpike3[i].x = this.Leg1p3[i].x;
            this.FootSpike3[i].y = this.Leg1p3[i].y;
            this.FootSpike3[i].z = this.Leg1p3[i].z;
            this.FootSpike4[i].x = this.Leg1p3[i].x;
            this.FootSpike4[i].y = this.Leg1p3[i].y;
            this.FootSpike4[i].z = this.Leg1p3[i].z;
            this.AnkleSpike1[i].x = this.Leg1p3[i].x;
            this.AnkleSpike1[i].y = this.Leg1p3[i].y;
            this.AnkleSpike1[i].z = this.Leg1p3[i].z;
            this.AnkleSpike2[i].x = this.Leg1p3[i].x;
            this.AnkleSpike2[i].y = this.Leg1p3[i].y;
            this.AnkleSpike2[i].z = this.Leg1p3[i].z;
            this.AnkleSpike3[i].x = this.Leg1p3[i].x;
            this.AnkleSpike3[i].y = this.Leg1p3[i].y;
            this.AnkleSpike3[i].z = this.Leg1p3[i].z;
            this.AnkleSpike4[i].x = this.Leg1p3[i].x;
            this.AnkleSpike4[i].y = this.Leg1p3[i].y;
            this.AnkleSpike4[i].z = this.Leg1p3[i].z;
            this.LegBump1[i].x = this.Leg1p3[i].x;
            this.LegBump1[i].y = this.Leg1p3[i].y;
            this.LegBump1[i].z = this.Leg1p3[i].z;
            this.LegBump2[i].x = this.Leg1p3[i].x;
            this.LegBump2[i].y = this.Leg1p3[i].y;
            this.LegBump2[i].z = this.Leg1p3[i].z;
            this.LowerKnee[i].x = this.Leg1p3[i].x;
            this.LowerKnee[i].y = this.Leg1p3[i].y;
            this.LowerKnee[i].z = this.Leg1p3[i].z;
        }

        if (entity.getAttacking() == 0) {
            this.Ljaw1.yRot = 0.0F;
            this.Ljaw2.yRot = 0.75F;
            this.Ljaw3.yRot = 1.71F;
            this.Rjaw1.yRot = 0.0F;
            this.Rjaw2.yRot = 2.3F;
            this.Rjaw3.yRot = 1.41F;
        } else {
            // gold: MathHelper.cos(gpcounter * 0.25) * PI * 0.22 — wingspeed stored unused
            float newangle = Mth.cos(r.gpcounter * 0.25F * this.wingspeed) * (float) Math.PI * 0.22F;
            this.Ljaw1.yRot = newangle;
            this.Ljaw2.yRot = newangle + 0.75F;
            this.Ljaw3.yRot = newangle + 1.71F;
            this.Rjaw1.yRot = -newangle;
            this.Rjaw2.yRot = 2.3F - newangle;
            this.Rjaw3.yRot = 1.41F - newangle;
        }
    }
}
