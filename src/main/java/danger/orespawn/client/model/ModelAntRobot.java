package danger.orespawn.client.model;

import danger.orespawn.entity.AntRobot;
import danger.orespawn.entity.AntRobot.SpiderLegInfo;
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
 * Port of gold {@code ModelAntRobot} (1.7.10 ModelBase, tex 128×256) → 1.21 HierarchicalModel.
 * Cubes/UVs/offsets/base rotations 1:1. Gold reuses Leg/Foot parts ×6 in render; here each of 6
 * legs is a separate instance so setupAnim matches without multi-pass render.
 * ClientProxy wingspeed {@code 1.0F}. Full gold anim: IK legs + jaw/antenna attack/idle.
 */
@OnlyIn(Dist.CLIENT)
public class ModelAntRobot extends HierarchicalModel<AntRobot> {
    /** Model layer location. */
    public static final ModelLayerLocation LAYER_LOCATION =
            new ModelLayerLocation(
                    ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "ant_robot"), "main");

    private final float wingspeed;
    private final ModelPart root;

    private final ModelPart[] Leg1 = new ModelPart[6];
    private final ModelPart[] Leg2 = new ModelPart[6];
    private final ModelPart[] Leg3 = new ModelPart[6];
    private final ModelPart[] Foot1 = new ModelPart[6];
    private final ModelPart[] Foot2 = new ModelPart[6];
    private final ModelPart[] Foot3 = new ModelPart[6];
    private final ModelPart[] Foot4 = new ModelPart[6];
    private final ModelPart[] Foot5 = new ModelPart[6];
    private final ModelPart[] Foot6 = new ModelPart[6];
    private final ModelPart[] Foot7 = new ModelPart[6];

    private final ModelPart Body;
    private final ModelPart Abdomen;
    private final ModelPart Head;
    private final ModelPart Jet1;
    private final ModelPart Jet2;
    private final ModelPart Hip1;
    private final ModelPart Hip2;
    private final ModelPart Hip3;
    private final ModelPart Hip4;
    private final ModelPart Hip5;
    private final ModelPart Hip6;
    private final ModelPart LJaw1;
    private final ModelPart RJaw1;
    private final ModelPart LJaw2;
    private final ModelPart RJaw2;
    private final ModelPart LAntenna;
    private final ModelPart RAntenna;

    public ModelAntRobot(ModelPart root) {
        this(root, 1.0F);
    }

    public ModelAntRobot(ModelPart root, float wingspeed) {
        this.root = root;
        this.wingspeed = wingspeed;

        for (int i = 0; i < 6; i++) {
            this.Leg1[i] = root.getChild("Leg1_" + i);
            this.Leg2[i] = root.getChild("Leg2_" + i);
            this.Leg3[i] = root.getChild("Leg3_" + i);
            this.Foot1[i] = root.getChild("Foot1_" + i);
            this.Foot2[i] = root.getChild("Foot2_" + i);
            this.Foot3[i] = root.getChild("Foot3_" + i);
            this.Foot4[i] = root.getChild("Foot4_" + i);
            this.Foot5[i] = root.getChild("Foot5_" + i);
            this.Foot6[i] = root.getChild("Foot6_" + i);
            this.Foot7[i] = root.getChild("Foot7_" + i);
        }

        this.Body = root.getChild("Body");
        this.Abdomen = root.getChild("Abdomen");
        this.Head = root.getChild("Head");
        this.Jet1 = root.getChild("Jet1");
        this.Jet2 = root.getChild("Jet2");
        this.Hip1 = root.getChild("Hip1");
        this.Hip2 = root.getChild("Hip2");
        this.Hip3 = root.getChild("Hip3");
        this.Hip4 = root.getChild("Hip4");
        this.Hip5 = root.getChild("Hip5");
        this.Hip6 = root.getChild("Hip6");
        this.LJaw1 = root.getChild("LJaw1");
        this.RJaw1 = root.getChild("RJaw1");
        this.LJaw2 = root.getChild("LJaw2");
        this.RJaw2 = root.getChild("RJaw2");
        this.LAntenna = root.getChild("LAntenna");
        this.RAntenna = root.getChild("RAntenna");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        // Gold reuses one Leg/Foot chain 6×; expand to Leg*_i for HierarchicalModel.
        for (int i = 0; i < 6; i++) {
            root.addOrReplaceChild(
                    "Leg1_" + i,
                    CubeListBuilder.create().texOffs(19, 40).mirror()
                            .addBox(-1.5F, -1.5F, 0.0F, 3.0F, 3.0F, 50.0F),
                    PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, (float) (Math.PI / 4), 0.0F, 0.0F));
            root.addOrReplaceChild(
                    "Leg2_" + i,
                    CubeListBuilder.create().texOffs(19, 41).mirror()
                            .addBox(-1.0F, -1.0F, 0.0F, 2.0F, 2.0F, 50.0F),
                    PartPose.offset(0.0F, -35.0F, 35.0F));
            root.addOrReplaceChild(
                    "Leg3_" + i,
                    CubeListBuilder.create().texOffs(20, 42).mirror()
                            .addBox(-0.5F, -0.5F, 0.0F, 1.0F, 1.0F, 50.0F),
                    PartPose.offsetAndRotation(0.0F, -35.0F, 85.0F, (float) (-Math.PI / 4), 0.0F, 0.0F));
            root.addOrReplaceChild(
                    "Foot1_" + i,
                    CubeListBuilder.create().texOffs(28, 0).mirror()
                            .addBox(-2.5F, -0.5F, 50.0F, 5.0F, 1.0F, 2.0F),
                    PartPose.offsetAndRotation(0.0F, -35.0F, 85.0F, (float) (-Math.PI / 4), 0.0F, 0.0F));
            root.addOrReplaceChild(
                    "Foot2_" + i,
                    CubeListBuilder.create().texOffs(30, 4).mirror()
                            .addBox(1.5F, -0.5F, 52.0F, 1.0F, 1.0F, 3.0F),
                    PartPose.offsetAndRotation(0.0F, -35.0F, 85.0F, (float) (-Math.PI / 4), 0.0F, 0.0F));
            root.addOrReplaceChild(
                    "Foot3_" + i,
                    CubeListBuilder.create().texOffs(44, 0).mirror()
                            .addBox(-0.5F, -0.5F, 52.0F, 1.0F, 1.0F, 5.0F),
                    PartPose.offsetAndRotation(0.0F, -35.0F, 85.0F, (float) (-Math.PI / 4), 0.0F, 0.0F));
            root.addOrReplaceChild(
                    "Foot4_" + i,
                    CubeListBuilder.create().texOffs(30, 9).mirror()
                            .addBox(-2.5F, -0.5F, 52.0F, 1.0F, 1.0F, 3.0F),
                    PartPose.offsetAndRotation(0.0F, -35.0F, 85.0F, (float) (-Math.PI / 4), 0.0F, 0.0F));
            root.addOrReplaceChild(
                    "Foot5_" + i,
                    CubeListBuilder.create().texOffs(40, 8).mirror()
                            .addBox(-0.5F, -2.5F, 50.0F, 1.0F, 5.0F, 2.0F),
                    PartPose.offsetAndRotation(0.0F, -35.0F, 85.0F, (float) (-Math.PI / 4), 0.0F, 0.0F));
            root.addOrReplaceChild(
                    "Foot6_" + i,
                    CubeListBuilder.create().texOffs(48, 9).mirror()
                            .addBox(-0.5F, -2.5F, 52.0F, 1.0F, 1.0F, 2.0F),
                    PartPose.offsetAndRotation(0.0F, -35.0F, 85.0F, (float) (-Math.PI / 4), 0.0F, 0.0F));
            root.addOrReplaceChild(
                    "Foot7_" + i,
                    CubeListBuilder.create().texOffs(48, 14).mirror()
                            .addBox(-0.5F, 1.5F, 52.0F, 1.0F, 1.0F, 2.0F),
                    PartPose.offsetAndRotation(0.0F, -35.0F, 85.0F, (float) (-Math.PI / 4), 0.0F, 0.0F));
        }

        root.addOrReplaceChild(
                "Body",
                CubeListBuilder.create().texOffs(0, 151).mirror()
                        .addBox(-11.0F, 0.0F, -16.0F, 22.0F, 14.0F, 32.0F),
                PartPose.offset(0.0F, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "Abdomen",
                CubeListBuilder.create().texOffs(0, 199).mirror()
                        .addBox(-15.0F, -10.0F, 16.0F, 30.0F, 22.0F, 34.0F),
                PartPose.offset(0.0F, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "Head",
                CubeListBuilder.create().texOffs(0, 120).mirror()
                        .addBox(-7.0F, 4.0F, -34.0F, 14.0F, 11.0F, 18.0F),
                PartPose.offset(0.0F, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "Jet1",
                CubeListBuilder.create().texOffs(78, 0).mirror()
                        .addBox(0.0F, 0.0F, 0.0F, 6.0F, 6.0F, 18.0F),
                PartPose.offset(8.0F, -12.0F, 35.0F));
        root.addOrReplaceChild(
                "Jet2",
                CubeListBuilder.create().texOffs(78, 0).mirror()
                        .addBox(0.0F, 0.0F, 0.0F, 6.0F, 6.0F, 18.0F),
                PartPose.offset(-14.0F, -12.0F, 35.0F));
        root.addOrReplaceChild(
                "Hip1",
                CubeListBuilder.create().texOffs(0, 96).mirror()
                        .addBox(0.0F, 0.0F, 0.0F, 6.0F, 6.0F, 6.0F),
                PartPose.offset(11.0F, 9.0F, -3.0F));
        root.addOrReplaceChild(
                "Hip2",
                CubeListBuilder.create().texOffs(0, 96).mirror()
                        .addBox(0.0F, 0.0F, 0.0F, 6.0F, 6.0F, 6.0F),
                PartPose.offset(-17.0F, 9.0F, -3.0F));
        root.addOrReplaceChild(
                "Hip3",
                CubeListBuilder.create().texOffs(0, 96).mirror()
                        .addBox(0.0F, 0.0F, 0.0F, 6.0F, 6.0F, 6.0F),
                PartPose.offset(-17.0F, 9.0F, 10.0F));
        root.addOrReplaceChild(
                "Hip4",
                CubeListBuilder.create().texOffs(0, 96).mirror()
                        .addBox(0.0F, 0.0F, 0.0F, 6.0F, 6.0F, 6.0F),
                PartPose.offset(11.0F, 9.0F, 10.0F));
        root.addOrReplaceChild(
                "Hip5",
                CubeListBuilder.create().texOffs(0, 96).mirror()
                        .addBox(0.0F, 0.0F, 0.0F, 6.0F, 6.0F, 6.0F),
                PartPose.offset(11.0F, 9.0F, -16.0F));
        root.addOrReplaceChild(
                "Hip6",
                CubeListBuilder.create().texOffs(0, 96).mirror()
                        .addBox(0.0F, 0.0F, 0.0F, 6.0F, 6.0F, 6.0F),
                PartPose.offset(-17.0F, 9.0F, -16.0F));
        root.addOrReplaceChild(
                "LJaw1",
                CubeListBuilder.create().texOffs(0, 33).mirror()
                        .addBox(-2.0F, 0.0F, -2.0F, 17.0F, 1.0F, 4.0F),
                PartPose.offsetAndRotation(5.0F, 13.0F, -33.0F, 0.0F, 0.8901179F, 0.0F));
        root.addOrReplaceChild(
                "RJaw1",
                CubeListBuilder.create().texOffs(0, 33).mirror()
                        .addBox(-2.0F, 0.0F, -2.0F, 17.0F, 1.0F, 4.0F),
                PartPose.offsetAndRotation(-5.0F, 13.0F, -33.0F, 0.0F, 2.216568F, 0.0F));
        root.addOrReplaceChild(
                "LJaw2",
                CubeListBuilder.create().texOffs(0, 27).mirror()
                        .addBox(12.0F, 0.0F, 5.0F, 17.0F, 1.0F, 3.0F),
                PartPose.offsetAndRotation(5.0F, 13.0F, -33.0F, 0.0F, 1.37881F, 0.0F));
        root.addOrReplaceChild(
                "RJaw2",
                CubeListBuilder.create().texOffs(0, 27).mirror()
                        .addBox(12.0F, 0.0F, -8.0F, 17.0F, 1.0F, 3.0F),
                PartPose.offsetAndRotation(-5.0F, 13.0F, -33.0F, 0.0F, 1.745329F, 0.0F));
        root.addOrReplaceChild(
                "LAntenna",
                CubeListBuilder.create().texOffs(70, 0).mirror()
                        .addBox(-0.5F, -12.0F, -0.5F, 1.0F, 12.0F, 1.0F),
                PartPose.offsetAndRotation(0.0F, 4.0F, -32.0F, 0.0F, 0.0F, 0.5410521F));
        root.addOrReplaceChild(
                "RAntenna",
                CubeListBuilder.create().texOffs(70, 0).mirror()
                        .addBox(-0.5F, -12.0F, -0.5F, 1.0F, 12.0F, 1.0F),
                PartPose.offsetAndRotation(0.0F, 4.0F, -32.0F, 0.0F, 0.0F, -0.5410521F));

        return LayerDefinition.create(mesh, 128, 256);
    }

    @Override
    public ModelPart root() {
        return this.root;
    }

    /**
     * Full gold {@code func_78088_a} animation (leg IK + jaws/antennae).
     * {@code wingspeed} reserved (gold constructor stores it; anim uses gpcounter).
     */
    @Override
    public void setupAnim(
            AntRobot entity,
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

        for (int i = 0; i < 6; i++) {
            float yDisp = r.ydisplayangle[i];
            float p1 = (float) r.p1xangle[i] + r.uddisplayangle[i];
            float p2 = (float) r.p2xangle[i] + r.uddisplayangle[i];
            float p3 = (float) r.p3xangle[i] + r.uddisplayangle[i];

            this.Leg1[i].yRot = yDisp;
            this.Leg2[i].yRot = yDisp;
            this.Leg3[i].yRot = yDisp;
            this.Foot1[i].yRot = yDisp;
            this.Foot2[i].yRot = yDisp;
            this.Foot3[i].yRot = yDisp;
            this.Foot4[i].yRot = yDisp;
            this.Foot5[i].yRot = yDisp;
            this.Foot6[i].yRot = yDisp;
            this.Foot7[i].yRot = yDisp;

            this.Leg1[i].xRot = p1;
            this.Leg2[i].xRot = p2;
            this.Leg3[i].xRot = p3;
            this.Foot1[i].xRot = p3;
            this.Foot2[i].xRot = p3;
            this.Foot3[i].xRot = p3;
            this.Foot4[i].xRot = p3;
            this.Foot5[i].xRot = p3;
            this.Foot6[i].xRot = p3;
            this.Foot7[i].xRot = p3;

            // gold dynamic pivots
            this.Leg1[i].x = -((float) Math.cos(r.ymid[i])) * r.legoff[i] * 16.0F;
            this.Leg1[i].z = (float) Math.sin(r.ymid[i]) * r.legoff[i] * 16.0F;
            this.Leg1[i].y = r.yoff[i] * -16.0F;

            this.Leg2[i].y = this.Leg1[i].y - (float) Math.sin(this.Leg1[i].xRot) * 49.0F;
            this.Leg2[i].z = this.Leg1[i].z
                    + (float) Math.cos(this.Leg1[i].xRot)
                    * (float) Math.cos(this.Leg1[i].yRot)
                    * 49.0F;
            this.Leg2[i].x = this.Leg1[i].x
                    + (float) Math.cos(this.Leg1[i].xRot)
                    * (float) Math.sin(this.Leg1[i].yRot)
                    * 49.0F;

            this.Leg3[i].y = this.Leg2[i].y - (float) Math.sin(this.Leg2[i].xRot) * 49.0F;
            this.Leg3[i].z = this.Leg2[i].z
                    + (float) Math.cos(this.Leg2[i].xRot)
                    * (float) Math.cos(this.Leg2[i].yRot)
                    * 49.0F;
            this.Leg3[i].x = this.Leg2[i].x
                    + (float) Math.cos(this.Leg2[i].xRot)
                    * (float) Math.sin(this.Leg2[i].yRot)
                    * 49.0F;

            this.Foot1[i].x = this.Leg3[i].x;
            this.Foot1[i].y = this.Leg3[i].y;
            this.Foot1[i].z = this.Leg3[i].z;
            this.Foot2[i].x = this.Leg3[i].x;
            this.Foot2[i].y = this.Leg3[i].y;
            this.Foot2[i].z = this.Leg3[i].z;
            this.Foot3[i].x = this.Leg3[i].x;
            this.Foot3[i].y = this.Leg3[i].y;
            this.Foot3[i].z = this.Leg3[i].z;
            this.Foot4[i].x = this.Leg3[i].x;
            this.Foot4[i].y = this.Leg3[i].y;
            this.Foot4[i].z = this.Leg3[i].z;
            this.Foot5[i].x = this.Leg3[i].x;
            this.Foot5[i].y = this.Leg3[i].y;
            this.Foot5[i].z = this.Leg3[i].z;
            this.Foot6[i].x = this.Leg3[i].x;
            this.Foot6[i].y = this.Leg3[i].y;
            this.Foot6[i].z = this.Leg3[i].z;
            this.Foot7[i].x = this.Leg3[i].x;
            this.Foot7[i].y = this.Leg3[i].y;
            this.Foot7[i].z = this.Leg3[i].z;
        }

        float gp = r.gpcounter * this.wingspeed;
        if (entity.getAttacking() == 0) {
            this.LJaw1.yRot = 0.89F;
            this.LJaw2.yRot = 1.378F;
            this.RJaw1.yRot = 2.216F;
            this.RJaw2.yRot = 1.745F;
            this.LAntenna.xRot = Mth.cos(gp * 0.35F) * (float) Math.PI * 0.05F;
            this.LAntenna.zRot = 0.54F + Mth.cos(gp * 0.25F) * (float) Math.PI * 0.05F;
            this.RAntenna.xRot = Mth.cos(gp * 0.3F) * (float) Math.PI * 0.05F;
            this.RAntenna.zRot = -0.54F + Mth.cos(gp * 0.45F) * (float) Math.PI * 0.05F;
        } else {
            float newangle = Mth.cos(gp * 0.25F) * (float) Math.PI * 0.22F;
            this.LJaw1.yRot = newangle + 0.89F;
            this.LJaw2.yRot = newangle + 1.378F;
            this.RJaw1.yRot = -newangle + 2.216F;
            this.RJaw2.yRot = 1.745F - newangle;
            this.LAntenna.xRot = Mth.cos(gp * 0.45F) * (float) Math.PI * 0.1F;
            this.LAntenna.zRot = 0.54F + Mth.cos(gp * 0.35F) * (float) Math.PI * 0.1F;
            this.RAntenna.xRot = Mth.cos(gp * 0.4F) * (float) Math.PI * 0.1F;
            this.RAntenna.zRot = -0.54F + Mth.cos(gp * 0.55F) * (float) Math.PI * 0.1F;
        }
    }
}
