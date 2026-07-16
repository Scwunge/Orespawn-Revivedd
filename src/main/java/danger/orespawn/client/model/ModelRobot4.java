package danger.orespawn.client.model;

import danger.orespawn.entity.Robot4;
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
 * Port of gold {@code ModelRobot4} (1.7.10 ModelBase, tex 512×512) → 1.21 HierarchicalModel.
 * Cubes/UVs/offsets/base rotations 1:1. ClientProxy wingspeed {@code 1.0F}.
 * Full gold anim: walk legs, head yRot, shield-arm swing + setShielding, cannon arm raise + y/z follow.
 */
@OnlyIn(Dist.CLIENT)
public class ModelRobot4 extends HierarchicalModel<Robot4> {
    /** Model layer location. */
    public static final ModelLayerLocation LAYER_LOCATION =
            new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "robot4"), "main");

    private final float wingspeed;
    private final ModelPart root;

    private final ModelPart leftfootfront;
    private final ModelPart leftfootbase;
    private final ModelPart leftfootback;
    private final ModelPart leftfoottip;
    private final ModelPart leftshin;
    private final ModelPart leftcalf;
    private final ModelPart leftkneegaurd;
    private final ModelPart leftthigh;
    private final ModelPart rightfootfront;
    private final ModelPart rightfoottip;
    private final ModelPart rightfootbase;
    private final ModelPart rightfootback;
    private final ModelPart rightshin;
    private final ModelPart rightcalf;
    private final ModelPart rightkneegaurd;
    private final ModelPart rightthigh;
    private final ModelPart hips;
    private final ModelPart stomach;
    private final ModelPart chest;
    private final ModelPart neck;
    private final ModelPart head;
    private final ModelPart righttopspinebase;
    private final ModelPart lefttopspinebase;
    private final ModelPart righttopspinetip;
    private final ModelPart lefttopspinetip;
    private final ModelPart middlerightspinebase;
    private final ModelPart middleleftspinebase;
    private final ModelPart middleleftspinetip;
    private final ModelPart middlerightspinetip;
    private final ModelPart torso;
    private final ModelPart rightsholder;
    private final ModelPart leftsholder;
    private final ModelPart rightsholdergaurd;
    private final ModelPart sheildbase;
    private final ModelPart sheildtip;
    private final ModelPart rightupperarm;
    private final ModelPart rightlowerarm;
    private final ModelPart sheildend;
    private final ModelPart leftupperarm;
    private final ModelPart sholdergaurdtip;
    private final ModelPart cannonbase;
    private final ModelPart cannonend;
    private final ModelPart leftcannonpiece;
    private final ModelPart topcannonpiece;
    private final ModelPart rightcannonpiece;
    private final ModelPart bottomcannonpiece;
    private final ModelPart glowycannonbit1;
    private final ModelPart glowycannonbit2;
    private final ModelPart glowycannonbit3;
    private final ModelPart glowycannonbit4;
    private final ModelPart glowycannonbit5;
    private final ModelPart cannonammo;
    private final ModelPart lowerrightspinebase;
    private final ModelPart lowerleftspinebase;
    private final ModelPart lowerrightspinetip;
    private final ModelPart lowerleftspinetip;

    public ModelRobot4(ModelPart root) {
        this(root, 1.0F);
    }

    public ModelRobot4(ModelPart root, float wingspeed) {
        this.root = root;
        this.wingspeed = wingspeed;
        this.leftfootfront = root.getChild("leftfootfront");
        this.leftfootbase = root.getChild("leftfootbase");
        this.leftfootback = root.getChild("leftfootback");
        this.leftfoottip = root.getChild("leftfoottip");
        this.leftshin = root.getChild("leftshin");
        this.leftcalf = root.getChild("leftcalf");
        this.leftkneegaurd = root.getChild("leftkneegaurd");
        this.leftthigh = root.getChild("leftthigh");
        this.rightfootfront = root.getChild("rightfootfront");
        this.rightfoottip = root.getChild("rightfoottip");
        this.rightfootbase = root.getChild("rightfootbase");
        this.rightfootback = root.getChild("rightfootback");
        this.rightshin = root.getChild("rightshin");
        this.rightcalf = root.getChild("rightcalf");
        this.rightkneegaurd = root.getChild("rightkneegaurd");
        this.rightthigh = root.getChild("rightthigh");
        this.hips = root.getChild("hips");
        this.stomach = root.getChild("stomach");
        this.chest = root.getChild("chest");
        this.neck = root.getChild("neck");
        this.head = root.getChild("head");
        this.righttopspinebase = root.getChild("righttopspinebase");
        this.lefttopspinebase = root.getChild("lefttopspinebase");
        this.righttopspinetip = root.getChild("righttopspinetip");
        this.lefttopspinetip = root.getChild("lefttopspinetip");
        this.middlerightspinebase = root.getChild("middlerightspinebase");
        this.middleleftspinebase = root.getChild("middleleftspinebase");
        this.middleleftspinetip = root.getChild("middleleftspinetip");
        this.middlerightspinetip = root.getChild("middlerightspinetip");
        this.torso = root.getChild("torso");
        this.rightsholder = root.getChild("rightsholder");
        this.leftsholder = root.getChild("leftsholder");
        this.rightsholdergaurd = root.getChild("rightsholdergaurd");
        this.sheildbase = root.getChild("sheildbase");
        this.sheildtip = root.getChild("sheildtip");
        this.rightupperarm = root.getChild("rightupperarm");
        this.rightlowerarm = root.getChild("rightlowerarm");
        this.sheildend = root.getChild("sheildend");
        this.leftupperarm = root.getChild("leftupperarm");
        this.sholdergaurdtip = root.getChild("sholdergaurdtip");
        this.cannonbase = root.getChild("cannonbase");
        this.cannonend = root.getChild("cannonend");
        this.leftcannonpiece = root.getChild("leftcannonpiece");
        this.topcannonpiece = root.getChild("topcannonpiece");
        this.rightcannonpiece = root.getChild("rightcannonpiece");
        this.bottomcannonpiece = root.getChild("bottomcannonpiece");
        this.glowycannonbit1 = root.getChild("glowycannonbit1");
        this.glowycannonbit2 = root.getChild("glowycannonbit2");
        this.glowycannonbit3 = root.getChild("glowycannonbit3");
        this.glowycannonbit4 = root.getChild("glowycannonbit4");
        this.glowycannonbit5 = root.getChild("glowycannonbit5");
        this.cannonammo = root.getChild("cannonammo");
        this.lowerrightspinebase = root.getChild("lowerrightspinebase");
        this.lowerleftspinebase = root.getChild("lowerleftspinebase");
        this.lowerrightspinetip = root.getChild("lowerrightspinetip");
        this.lowerleftspinetip = root.getChild("lowerleftspinetip");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        // left leg chain — pivot (-8, -3, 6)
        root.addOrReplaceChild("leftfootfront",
                CubeListBuilder.create().texOffs(20, 50)
                        .addBox(-6.0F, 22.0F, -9.0F, 8.0F, 5.0F, 7.0F),
                PartPose.offset(-8.0F, -3.0F, 6.0F));
        root.addOrReplaceChild("leftfootbase",
                CubeListBuilder.create().texOffs(20, 100)
                        .addBox(-4.0F, 22.0F, -4.0F, 4.0F, 5.0F, 5.0F),
                PartPose.offset(-8.0F, -3.0F, 6.0F));
        root.addOrReplaceChild("leftfootback",
                CubeListBuilder.create().texOffs(20, 150)
                        .addBox(-4.5F, 22.0F, 1.0F, 5.0F, 5.0F, 4.0F),
                PartPose.offset(-8.0F, -3.0F, 6.0F));
        root.addOrReplaceChild("leftfoottip",
                CubeListBuilder.create().texOffs(20, 200)
                        .addBox(-4.5F, 23.0F, -12.0F, 5.0F, 4.0F, 3.0F),
                PartPose.offset(-8.0F, -3.0F, 6.0F));
        root.addOrReplaceChild("leftshin",
                CubeListBuilder.create().texOffs(20, 250)
                        .addBox(-5.0F, 10.0F, -9.0F, 6.0F, 13.0F, 6.0F),
                PartPose.offsetAndRotation(-8.0F, -3.0F, 6.0F, 0.1745329F, 0.0F, 0.0F));
        root.addOrReplaceChild("leftcalf",
                CubeListBuilder.create().texOffs(20, 300)
                        .addBox(-6.0F, 10.0F, -9.0F, 8.0F, 8.0F, 9.0F),
                PartPose.offsetAndRotation(-8.0F, -3.0F, 6.0F, 0.1745329F, 0.0F, 0.0F));
        root.addOrReplaceChild("leftkneegaurd",
                CubeListBuilder.create().texOffs(20, 350)
                        .addBox(-5.5F, 4.0F, -14.0F, 7.0F, 7.0F, 1.0F),
                PartPose.offsetAndRotation(-8.0F, -3.0F, 6.0F, 0.6283185F, 0.0F, 0.0F));
        root.addOrReplaceChild("leftthigh",
                CubeListBuilder.create().texOffs(20, 400)
                        .addBox(-5.0F, 0.0F, -4.0F, 6.0F, 13.0F, 8.0F),
                PartPose.offsetAndRotation(-8.0F, -3.0F, 6.0F, -0.1745329F, 0.1745329F, 0.0F));

        // right leg chain — pivot (5, -3, 6)
        root.addOrReplaceChild("rightfootfront",
                CubeListBuilder.create().texOffs(20, 450)
                        .addBox(0.0F, 22.0F, -9.0F, 8.0F, 5.0F, 7.0F),
                PartPose.offset(5.0F, -3.0F, 6.0F));
        root.addOrReplaceChild("rightfoottip",
                CubeListBuilder.create().texOffs(100, 50)
                        .addBox(1.5F, 23.0F, -12.0F, 5.0F, 4.0F, 3.0F),
                PartPose.offset(5.0F, -3.0F, 6.0F));
        root.addOrReplaceChild("rightfootbase",
                CubeListBuilder.create().texOffs(100, 150)
                        .addBox(2.0F, 22.0F, -4.0F, 4.0F, 5.0F, 5.0F),
                PartPose.offset(5.0F, -3.0F, 6.0F));
        root.addOrReplaceChild("rightfootback",
                CubeListBuilder.create().texOffs(100, 100)
                        .addBox(1.5F, 22.0F, 1.0F, 5.0F, 5.0F, 4.0F),
                PartPose.offset(5.0F, -3.0F, 6.0F));
        root.addOrReplaceChild("rightshin",
                CubeListBuilder.create().texOffs(100, 200)
                        .addBox(1.0F, 10.0F, -9.0F, 6.0F, 13.0F, 6.0F),
                PartPose.offsetAndRotation(5.0F, -3.0F, 6.0F, 0.1745329F, 0.0F, 0.0F));
        root.addOrReplaceChild("rightcalf",
                CubeListBuilder.create().texOffs(100, 250)
                        .addBox(0.0F, 10.0F, -10.0F, 8.0F, 8.0F, 9.0F),
                PartPose.offsetAndRotation(5.0F, -3.0F, 6.0F, 0.1745329F, 0.0F, 0.0F));
        root.addOrReplaceChild("rightkneegaurd",
                CubeListBuilder.create().texOffs(100, 300)
                        .addBox(0.5F, 4.0F, -15.0F, 7.0F, 7.0F, 1.0F),
                PartPose.offsetAndRotation(5.0F, -3.0F, 6.0F, 0.6283185F, 0.0F, 0.0F));
        root.addOrReplaceChild("rightthigh",
                CubeListBuilder.create().texOffs(100, 400)
                        .addBox(0.0F, 0.0F, -5.0F, 6.0F, 13.0F, 8.0F),
                PartPose.offsetAndRotation(5.0F, -3.0F, 6.0F, -0.1745329F, -0.1745329F, 0.0F));

        // torso / head / spines
        root.addOrReplaceChild("hips",
                CubeListBuilder.create().texOffs(100, 350)
                        .addBox(0.0F, 0.0F, 0.0F, 14.0F, 7.0F, 8.0F),
                PartPose.offsetAndRotation(-8.0F, -3.0F, 2.0F, 0.1396263F, 0.0F, 0.0F));
        root.addOrReplaceChild("stomach",
                CubeListBuilder.create().texOffs(100, 450)
                        .addBox(0.0F, 0.0F, 0.0F, 12.0F, 6.0F, 7.0F),
                PartPose.offsetAndRotation(-7.0F, -9.0F, 2.0F, 0.1396263F, 0.0F, 0.0F));
        root.addOrReplaceChild("chest",
                CubeListBuilder.create().texOffs(200, 50)
                        .addBox(0.0F, 0.0F, 0.0F, 18.0F, 12.0F, 13.0F),
                PartPose.offsetAndRotation(-10.0F, -23.0F, -4.0F, 0.2443461F, 0.0F, 0.0F));
        root.addOrReplaceChild("neck",
                CubeListBuilder.create().texOffs(200, 100)
                        .addBox(0.0F, 0.0F, 0.0F, 6.0F, 7.0F, 6.0F),
                PartPose.offsetAndRotation(-4.0F, -22.0F, -7.0F, 0.8726646F, 0.0F, 0.0F));
        root.addOrReplaceChild("head",
                CubeListBuilder.create().texOffs(200, 150)
                        .addBox(-3.0F, -3.0F, -5.0F, 6.0F, 6.0F, 8.0F),
                PartPose.offsetAndRotation(-1.0F, -26.0F, -5.0F, (float) (Math.PI / 6), 0.0F, 0.0F));
        root.addOrReplaceChild("righttopspinebase",
                CubeListBuilder.create().texOffs(200, 200)
                        .addBox(0.0F, 0.0F, 0.0F, 2.0F, 8.0F, 2.0F),
                PartPose.offsetAndRotation(3.0F, -29.0F, 5.0F, -0.1396263F, 0.0F, 0.0F));
        root.addOrReplaceChild("lefttopspinebase",
                CubeListBuilder.create().texOffs(200, 250)
                        .addBox(0.0F, 0.0F, 0.0F, 2.0F, 8.0F, 2.0F),
                PartPose.offsetAndRotation(-7.0F, -29.0F, 5.0F, -0.1396263F, 0.0F, 0.0F));
        root.addOrReplaceChild("righttopspinetip",
                CubeListBuilder.create().texOffs(200, 300)
                        .addBox(0.0F, 0.0F, 0.0F, 1.0F, 8.0F, 1.0F),
                PartPose.offsetAndRotation(3.5F, -35.0F, 8.0F, -0.3316126F, 0.0F, 0.0F));
        root.addOrReplaceChild("lefttopspinetip",
                CubeListBuilder.create().texOffs(200, 350)
                        .addBox(0.0F, 0.0F, 0.0F, 1.0F, 8.0F, 1.0F),
                PartPose.offsetAndRotation(-6.5F, -35.0F, 8.0F, -0.3316126F, 0.0F, 0.0F));
        root.addOrReplaceChild("middlerightspinebase",
                CubeListBuilder.create().texOffs(200, 400)
                        .addBox(0.0F, 0.0F, 0.0F, 2.0F, 8.0F, 2.0F),
                PartPose.offsetAndRotation(-6.0F, -25.0F, 14.0F, (float) (-Math.PI * 2.0 / 9.0), 0.0F, 0.0F));
        root.addOrReplaceChild("middleleftspinebase",
                CubeListBuilder.create().texOffs(200, 450)
                        .addBox(0.0F, 0.0F, 0.0F, 2.0F, 8.0F, 2.0F),
                PartPose.offsetAndRotation(2.0F, -25.0F, 14.0F, (float) (-Math.PI * 2.0 / 9.0), 0.0F, 0.0F));
        root.addOrReplaceChild("middleleftspinetip",
                CubeListBuilder.create().texOffs(300, 50)
                        .addBox(0.0F, 0.0F, 0.0F, 1.0F, 7.0F, 1.0F),
                PartPose.offsetAndRotation(2.5F, -28.0F, 18.0F, (float) (-Math.PI / 4), 0.0F, 0.0F));
        root.addOrReplaceChild("middlerightspinetip",
                CubeListBuilder.create().texOffs(300, 100)
                        .addBox(0.0F, 0.0F, 0.0F, 1.0F, 7.0F, 1.0F),
                PartPose.offsetAndRotation(-5.5F, -28.0F, 18.0F, (float) (-Math.PI / 4), 0.0F, 0.0F));
        root.addOrReplaceChild("torso",
                CubeListBuilder.create().texOffs(300, 150)
                        .addBox(0.0F, 0.0F, 0.0F, 14.0F, 6.0F, 10.0F),
                PartPose.offsetAndRotation(-8.0F, -13.0F, 0.0F, 0.1396263F, 0.0F, 0.0F));

        // right shield arm — pivot (7, -18, 4); sheildtip pivot (6, -18, 4) gold 1:1
        root.addOrReplaceChild("rightsholder",
                CubeListBuilder.create().texOffs(300, 200)
                        .addBox(0.0F, -3.0F, -3.0F, 6.0F, 6.0F, 6.0F),
                PartPose.offset(7.0F, -18.0F, 4.0F));
        root.addOrReplaceChild("leftsholder",
                CubeListBuilder.create().texOffs(300, 250)
                        .addBox(-6.0F, -3.0F, -3.0F, 6.0F, 6.0F, 6.0F),
                PartPose.offset(-9.0F, -18.0F, 4.0F));
        root.addOrReplaceChild("rightsholdergaurd",
                CubeListBuilder.create().texOffs(300, 300)
                        .addBox(8.0F, -4.0F, -4.0F, 4.0F, 12.0F, 9.0F),
                PartPose.offsetAndRotation(7.0F, -18.0F, 4.0F, -0.2094395F, 0.0F, 0.0F));
        root.addOrReplaceChild("sheildbase",
                CubeListBuilder.create().texOffs(300, 350)
                        .addBox(8.0F, -4.0F, -30.0F, 3.0F, 12.0F, 19.0F),
                PartPose.offsetAndRotation(7.0F, -18.0F, 4.0F, 1.047198F, 0.0F, 0.0F));
        root.addOrReplaceChild("sheildtip",
                CubeListBuilder.create().texOffs(300, 400)
                        .addBox(9.0F, -2.0F, -34.0F, 3.0F, 8.0F, 4.0F),
                PartPose.offsetAndRotation(6.0F, -18.0F, 4.0F, 1.047198F, 0.0F, 0.0F));
        root.addOrReplaceChild("rightupperarm",
                CubeListBuilder.create().texOffs(300, 450)
                        .addBox(3.0F, -1.0F, -4.0F, 6.0F, 13.0F, 6.0F),
                PartPose.offsetAndRotation(7.0F, -18.0F, 4.0F, -0.2094395F, 0.0F, 0.0F));
        root.addOrReplaceChild("rightlowerarm",
                CubeListBuilder.create().texOffs(350, 50)
                        .addBox(3.0F, 0.0F, -25.0F, 6.0F, 6.0F, 14.0F),
                PartPose.offsetAndRotation(7.0F, -18.0F, 4.0F, 1.047198F, 0.0F, 0.0F));
        root.addOrReplaceChild("sheildend",
                CubeListBuilder.create().texOffs(350, 100)
                        .addBox(8.0F, -1.0F, -11.0F, 3.0F, 8.0F, 4.0F),
                PartPose.offsetAndRotation(7.0F, -18.0F, 4.0F, 1.047198F, 0.0F, 0.0F));
        root.addOrReplaceChild("leftupperarm",
                CubeListBuilder.create().texOffs(350, 200)
                        .addBox(-9.0F, -1.0F, -4.0F, 6.0F, 15.0F, 6.0F),
                PartPose.offsetAndRotation(-9.0F, -18.0F, 4.0F, -0.2094395F, 0.0F, 0.0F));
        root.addOrReplaceChild("sholdergaurdtip",
                CubeListBuilder.create().texOffs(350, 250)
                        .addBox(10.0F, -3.0F, -7.0F, 2.0F, 5.0F, 3.0F),
                PartPose.offsetAndRotation(7.0F, -18.0F, 4.0F, -0.2094395F, 0.0F, 0.0F));

        // cannon chain — rest pivot (-15, -5, 1); y/z follow left arm each frame
        float cannonBaseX = (float) (-Math.PI * 2.0 / 9.0);
        root.addOrReplaceChild("cannonbase",
                CubeListBuilder.create().texOffs(350, 300)
                        .addBox(-4.0F, 0.0F, -4.0F, 8.0F, 12.0F, 8.0F),
                PartPose.offsetAndRotation(-15.0F, -5.0F, 1.0F, cannonBaseX, 0.0F, 0.0F));
        root.addOrReplaceChild("cannonend",
                CubeListBuilder.create().texOffs(350, 400)
                        .addBox(-3.0F, 11.0F, -3.0F, 6.0F, 4.0F, 6.0F),
                PartPose.offsetAndRotation(-15.0F, -5.0F, 1.0F, cannonBaseX, 0.0F, 0.0F));
        root.addOrReplaceChild("leftcannonpiece",
                CubeListBuilder.create().texOffs(20, 20)
                        .addBox(-5.0F, 11.0F, -1.5F, 3.0F, 6.0F, 3.0F),
                PartPose.offsetAndRotation(-15.0F, -5.0F, 1.0F, cannonBaseX, 0.0F, 0.0F));
        root.addOrReplaceChild("topcannonpiece",
                CubeListBuilder.create().texOffs(40, 20)
                        .addBox(-1.5F, 11.0F, -5.0F, 3.0F, 6.0F, 3.0F),
                PartPose.offsetAndRotation(-15.0F, -5.0F, 1.0F, cannonBaseX, 0.0F, 0.0F));
        root.addOrReplaceChild("rightcannonpiece",
                CubeListBuilder.create().texOffs(80, 20)
                        .addBox(2.0F, 11.0F, -1.5F, 3.0F, 6.0F, 3.0F),
                PartPose.offsetAndRotation(-15.0F, -5.0F, 1.0F, cannonBaseX, 0.0F, 0.0F));
        root.addOrReplaceChild("bottomcannonpiece",
                CubeListBuilder.create().texOffs(100, 20)
                        .addBox(-1.5F, 11.0F, 2.0F, 3.0F, 6.0F, 3.0F),
                PartPose.offsetAndRotation(-15.0F, -5.0F, 1.0F, cannonBaseX, 0.0F, 0.0F));
        root.addOrReplaceChild("glowycannonbit1",
                CubeListBuilder.create().texOffs(150, 20)
                        .addBox(-3.5F, 0.0F, -11.0F, 2.0F, 5.0F, 2.0F),
                PartPose.offsetAndRotation(-15.0F, -5.0F, 1.0F, 0.1745329F, 0.0F, 0.0F));
        root.addOrReplaceChild("glowycannonbit2",
                CubeListBuilder.create().texOffs(200, 20)
                        .addBox(1.5F, 0.0F, -11.0F, 2.0F, 5.0F, 2.0F),
                PartPose.offsetAndRotation(-15.0F, -5.0F, 1.0F, 0.1745329F, 0.0F, 0.0F));
        root.addOrReplaceChild("glowycannonbit3",
                CubeListBuilder.create().texOffs(250, 20)
                        .addBox(-3.0F, -2.0F, -8.0F, 2.0F, 5.0F, 2.0F),
                PartPose.offsetAndRotation(-15.0F, -5.0F, 1.0F, 0.0872665F, 0.0F, 0.0F));
        root.addOrReplaceChild("glowycannonbit4",
                CubeListBuilder.create().texOffs(300, 20)
                        .addBox(1.0F, -2.0F, -8.0F, 2.0F, 5.0F, 2.0F),
                PartPose.offsetAndRotation(-15.0F, -5.0F, 1.0F, 0.0872665F, 0.0F, 0.0F));
        root.addOrReplaceChild("glowycannonbit5",
                CubeListBuilder.create().texOffs(350, 20)
                        .addBox(-1.0F, -5.0F, -5.0F, 2.0F, 5.0F, 2.0F),
                PartPose.offset(-15.0F, -5.0F, 1.0F));
        root.addOrReplaceChild("cannonammo",
                CubeListBuilder.create().texOffs(400, 400)
                        .addBox(-6.0F, 3.0F, 0.0F, 5.0F, 5.0F, 5.0F),
                PartPose.offsetAndRotation(-15.0F, -5.0F, 1.0F, cannonBaseX, 0.0F, 0.0F));

        root.addOrReplaceChild("lowerrightspinebase",
                CubeListBuilder.create().texOffs(400, 450)
                        .addBox(0.0F, 0.0F, 0.0F, 2.0F, 8.0F, 2.0F),
                PartPose.offsetAndRotation(4.0F, -19.0F, 15.0F, -1.047198F, 0.0F, 0.0F));
        root.addOrReplaceChild("lowerleftspinebase",
                CubeListBuilder.create().texOffs(360, 450)
                        .addBox(0.0F, 0.0F, 0.0F, 2.0F, 8.0F, 2.0F),
                PartPose.offsetAndRotation(-8.0F, -19.0F, 15.0F, -1.047198F, 0.0F, 0.0F));
        root.addOrReplaceChild("lowerrightspinetip",
                CubeListBuilder.create().texOffs(250, 100)
                        .addBox(0.0F, 0.0F, 0.0F, 1.0F, 7.0F, 1.0F),
                PartPose.offsetAndRotation(4.5F, -21.0F, 20.0F, -1.134464F, 0.0F, 0.0F));
        root.addOrReplaceChild("lowerleftspinetip",
                CubeListBuilder.create().texOffs(150, 100)
                        .addBox(0.0F, 0.0F, 0.0F, 1.0F, 7.0F, 1.0F),
                PartPose.offsetAndRotation(-7.5F, -21.0F, 20.0F, -1.134464F, 0.0F, 0.0F));

        return LayerDefinition.create(mesh, 512, 512);
    }

    @Override
    public ModelPart root() {
        return this.root;
    }

    @Override
    public void setupAnim(
            Robot4 entity,
            float limbSwing,
            float limbSwingAmount,
            float ageInTicks,
            float netHeadYaw,
            float headPitch) {
        float f1 = limbSwingAmount;
        float f2 = ageInTicks;
        float newangle;

        // gold walk: cos(f2 * 0.5 * wingspeed) * PI * 0.15 * f1
        if (f1 > 0.1F) {
            newangle = Mth.cos(f2 * 0.5F * this.wingspeed) * (float) Math.PI * 0.15F * f1;
        } else {
            newangle = 0.0F;
        }

        this.leftfootfront.xRot = newangle;
        this.leftfootbase.xRot = newangle;
        this.leftfootback.xRot = newangle;
        this.leftfoottip.xRot = newangle;
        this.leftshin.xRot = newangle;
        this.leftcalf.xRot = newangle + 0.175F;
        this.leftkneegaurd.xRot = newangle + 0.63F;
        this.leftthigh.xRot = newangle - 0.175F;

        this.rightfootfront.xRot = -newangle;
        this.rightfoottip.xRot = -newangle;
        this.rightfootbase.xRot = -newangle;
        this.rightfootback.xRot = -newangle;
        this.rightshin.xRot = -newangle;
        this.rightcalf.xRot = -newangle + 0.175F;
        this.rightkneegaurd.xRot = -newangle + 0.63F;
        this.rightthigh.xRot = -newangle - 0.175F;

        // gold head yRot only (base xRot PI/6 preserved via PartPose; overwrite if needed)
        this.head.xRot = (float) (Math.PI / 6);
        this.head.yRot = (float) Math.toRadians(netHeadYaw / 1.5F);

        // gold shield arm: cos(toRadians(f2%360)*ws*6) * amp, abs, +0.75 when attacking
        float amp = (float) (Math.PI / 4);
        if (entity.getAttacking() != 0) {
            newangle = Mth.cos((float) Math.toRadians(f2 % 360.0F) * this.wingspeed * 6.0F) * amp;
            newangle = Math.abs(newangle);
            newangle += 0.75F;
        } else {
            newangle = 0.0F;
        }

        // gold setShielding side-effect from model (client entity data)
        if (newangle > amp / 3.0F) {
            entity.setShielding(1);
        } else {
            entity.setShielding(0);
        }

        this.rightsholder.xRot = -newangle;
        this.rightsholdergaurd.xRot = -newangle - 0.21F;
        this.sheildbase.xRot = -newangle + 1.047F;
        this.sheildtip.xRot = -newangle + 1.047F;
        this.rightupperarm.xRot = -newangle - 0.21F;
        this.rightlowerarm.xRot = -newangle + 1.047F;
        this.sheildend.xRot = -newangle + 1.04F;
        this.sholdergaurdtip.xRot = -newangle - 0.21F;

        // gold cannon arm raise when attacking
        if (entity.getAttacking() != 0) {
            newangle = 0.85F;
        } else {
            newangle = 0.0F;
        }

        this.leftsholder.xRot = -newangle;
        this.leftupperarm.xRot = -newangle - 0.21F;
        this.cannonbase.xRot = -newangle - 0.7F;
        this.cannonend.xRot = -newangle - 0.7F;
        this.leftcannonpiece.xRot = -newangle - 0.7F;
        this.topcannonpiece.xRot = -newangle - 0.7F;
        this.rightcannonpiece.xRot = -newangle - 0.7F;
        this.bottomcannonpiece.xRot = -newangle - 0.7F;
        this.glowycannonbit1.xRot = -newangle + 0.17F;
        this.glowycannonbit2.xRot = -newangle + 0.17F;
        this.glowycannonbit3.xRot = -newangle + 0.08F;
        this.glowycannonbit4.xRot = -newangle + 0.08F;
        this.glowycannonbit5.xRot = -newangle;
        this.cannonammo.xRot = -newangle - 0.7F;

        // gold: cannon y/z follow leftupperarm rotation around leftsholder
        float newposy = (float) (this.leftsholder.y + Math.cos(this.leftupperarm.xRot) * 14.0);
        float newposz = (float) (this.leftsholder.z + Math.sin(this.leftupperarm.xRot) * 14.0);
        this.cannonbase.y = newposy;
        this.cannonbase.z = newposz;
        this.cannonend.y = newposy;
        this.cannonend.z = newposz;
        this.leftcannonpiece.y = newposy;
        this.leftcannonpiece.z = newposz;
        this.topcannonpiece.y = newposy;
        this.topcannonpiece.z = newposz;
        this.rightcannonpiece.y = newposy;
        this.rightcannonpiece.z = newposz;
        this.bottomcannonpiece.y = newposy;
        this.bottomcannonpiece.z = newposz;
        this.glowycannonbit1.y = newposy;
        this.glowycannonbit1.z = newposz;
        this.glowycannonbit2.y = newposy;
        this.glowycannonbit2.z = newposz;
        this.glowycannonbit3.y = newposy;
        this.glowycannonbit3.z = newposz;
        this.glowycannonbit4.y = newposy;
        this.glowycannonbit4.z = newposz;
        this.glowycannonbit5.y = newposy;
        this.glowycannonbit5.z = newposz;
        this.cannonammo.y = newposy;
        this.cannonammo.z = newposz;
    }
}
