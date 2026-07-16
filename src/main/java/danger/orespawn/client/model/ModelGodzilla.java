package danger.orespawn.client.model;

import danger.orespawn.entity.Godzilla;
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
 * Port of gold {@code ModelGodzilla} (1.7.10 ModelBase, tex 1024x1024) -> 1.21 HierarchicalModel.
 * Cube sizes/UVs/offsets/base rotations 1:1. Wingspeed default 0.2 matches ClientProxy.
 * Full setupAnim: dual-leg walk IK, tail chain, jaw / arm / finger attack via {@link RenderInfo}.
 */
@OnlyIn(Dist.CLIENT)
public class ModelGodzilla extends HierarchicalModel<Godzilla> {
    /** Model layer location. */
    public static final ModelLayerLocation LAYER_LOCATION =
            new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "godzilla"), "main");

    private final float wingspeed;
    private final ModelPart root;
    private final ModelPart LToe1;
    private final ModelPart LToe3;
    private final ModelPart LToe2;
    private final ModelPart LToe9;
    private final ModelPart LToe8;
    private final ModelPart LToe7;
    private final ModelPart LToe6;
    private final ModelPart LToe5;
    private final ModelPart LToe4;
    private final ModelPart RToe9;
    private final ModelPart RToe6;
    private final ModelPart RToe5;
    private final ModelPart RToe2;
    private final ModelPart RToe1;
    private final ModelPart RToe4;
    private final ModelPart RToe7;
    private final ModelPart RToe8;
    private final ModelPart RToe3;
    private final ModelPart LThigh;
    private final ModelPart LLowerLeg;
    private final ModelPart LUpperLeg;
    private final ModelPart TailTip;
    private final ModelPart RLegLower;
    private final ModelPart RLegUpper;
    private final ModelPart RThigh;
    private final ModelPart LowerJaw;
    private final ModelPart TailBase;
    private final ModelPart Tail2;
    private final ModelPart Tail3;
    private final ModelPart Tail4;
    private final ModelPart Tail5;
    private final ModelPart Tail6;
    private final ModelPart Tail7;
    private final ModelPart BodyBottom;
    private final ModelPart RLowerArm;
    private final ModelPart BodyCenter;
    private final ModelPart Neck;
    private final ModelPart TopJaw;
    private final ModelPart Head;
    private final ModelPart BodyTop;
    private final ModelPart RShoulder;
    private final ModelPart RThumbTip;
    private final ModelPart RUpperArm;
    private final ModelPart RHand;
    private final ModelPart RThumbBase;
    private final ModelPart R3rdFingerTip;
    private final ModelPart R3rdFingerBase;
    private final ModelPart RIndexTip;
    private final ModelPart RIndexBase;
    private final ModelPart LShoulder;
    private final ModelPart LUpperArm;
    private final ModelPart LLowerArm;
    private final ModelPart LIndexBase;
    private final ModelPart LIndexTip;
    private final ModelPart LHand;
    private final ModelPart LThumbBase;
    private final ModelPart LThumbTip;
    private final ModelPart L3rdFingerTip;
    private final ModelPart L3rdFingerBase;
    private final ModelPart Lspikes1;
    private final ModelPart Rspikes1;
    private final ModelPart Lspike2;
    private final ModelPart Rspike2;
    private final ModelPart Lspike3;
    private final ModelPart Rspike3;
    private final ModelPart Lspike4;
    private final ModelPart Rspike4;
    private final ModelPart Lspike5;
    private final ModelPart Rspike5;
    private final ModelPart Spike6;
    private final ModelPart Spikes7;

    public ModelGodzilla(ModelPart root) {
        this(root, 0.2F);
    }

    public ModelGodzilla(ModelPart root, float wingspeed) {
        this.root = root;
        this.wingspeed = wingspeed;
        this.LToe1 = root.getChild("LToe1");
        this.LToe3 = root.getChild("LToe3");
        this.LToe2 = root.getChild("LToe2");
        this.LToe9 = root.getChild("LToe9");
        this.LToe8 = root.getChild("LToe8");
        this.LToe7 = root.getChild("LToe7");
        this.LToe6 = root.getChild("LToe6");
        this.LToe5 = root.getChild("LToe5");
        this.LToe4 = root.getChild("LToe4");
        this.RToe9 = root.getChild("RToe9");
        this.RToe6 = root.getChild("RToe6");
        this.RToe5 = root.getChild("RToe5");
        this.RToe2 = root.getChild("RToe2");
        this.RToe1 = root.getChild("RToe1");
        this.RToe4 = root.getChild("RToe4");
        this.RToe7 = root.getChild("RToe7");
        this.RToe8 = root.getChild("RToe8");
        this.RToe3 = root.getChild("RToe3");
        this.LThigh = root.getChild("LThigh");
        this.LLowerLeg = root.getChild("LLowerLeg");
        this.LUpperLeg = root.getChild("LUpperLeg");
        this.TailTip = root.getChild("TailTip");
        this.RLegLower = root.getChild("RLegLower");
        this.RLegUpper = root.getChild("RLegUpper");
        this.RThigh = root.getChild("RThigh");
        this.LowerJaw = root.getChild("LowerJaw");
        this.TailBase = root.getChild("TailBase");
        this.Tail2 = root.getChild("Tail2");
        this.Tail3 = root.getChild("Tail3");
        this.Tail4 = root.getChild("Tail4");
        this.Tail5 = root.getChild("Tail5");
        this.Tail6 = root.getChild("Tail6");
        this.Tail7 = root.getChild("Tail7");
        this.BodyBottom = root.getChild("BodyBottom");
        this.RLowerArm = root.getChild("RLowerArm");
        this.BodyCenter = root.getChild("BodyCenter");
        this.Neck = root.getChild("Neck");
        this.TopJaw = root.getChild("TopJaw");
        this.Head = root.getChild("Head");
        this.BodyTop = root.getChild("BodyTop");
        this.RShoulder = root.getChild("RShoulder");
        this.RThumbTip = root.getChild("RThumbTip");
        this.RUpperArm = root.getChild("RUpperArm");
        this.RHand = root.getChild("RHand");
        this.RThumbBase = root.getChild("RThumbBase");
        this.R3rdFingerTip = root.getChild("R3rdFingerTip");
        this.R3rdFingerBase = root.getChild("R3rdFingerBase");
        this.RIndexTip = root.getChild("RIndexTip");
        this.RIndexBase = root.getChild("RIndexBase");
        this.LShoulder = root.getChild("LShoulder");
        this.LUpperArm = root.getChild("LUpperArm");
        this.LLowerArm = root.getChild("LLowerArm");
        this.LIndexBase = root.getChild("LIndexBase");
        this.LIndexTip = root.getChild("LIndexTip");
        this.LHand = root.getChild("LHand");
        this.LThumbBase = root.getChild("LThumbBase");
        this.LThumbTip = root.getChild("LThumbTip");
        this.L3rdFingerTip = root.getChild("L3rdFingerTip");
        this.L3rdFingerBase = root.getChild("L3rdFingerBase");
        this.Lspikes1 = root.getChild("Lspikes1");
        this.Rspikes1 = root.getChild("Rspikes1");
        this.Lspike2 = root.getChild("Lspike2");
        this.Rspike2 = root.getChild("Rspike2");
        this.Lspike3 = root.getChild("Lspike3");
        this.Rspike3 = root.getChild("Rspike3");
        this.Lspike4 = root.getChild("Lspike4");
        this.Rspike4 = root.getChild("Rspike4");
        this.Lspike5 = root.getChild("Lspike5");
        this.Rspike5 = root.getChild("Rspike5");
        this.Spike6 = root.getChild("Spike6");
        this.Spikes7 = root.getChild("Spikes7");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        root.addOrReplaceChild("LToe1",
                CubeListBuilder.create().texOffs(45, 1002)
                        .addBox(-5.0F, -2.0F, -40.0F, 10.0F, 10.0F, 6.0F),
                PartPose.offsetAndRotation(54.0F, 16.0F, 6.0F, 0.0F, (float) (Math.PI / 4), 0.0F));
        root.addOrReplaceChild("LToe3",
                CubeListBuilder.create().texOffs(0, 955)
                        .addBox(-8.0F, -8.0F, -26.0F, 16.0F, 16.0F, 30.0F),
                PartPose.offsetAndRotation(54.0F, 16.0F, 6.0F, 0.0F, (float) (Math.PI / 4), 0.0F));
        root.addOrReplaceChild("LToe2",
                CubeListBuilder.create().texOffs(0, 1002)
                        .addBox(-7.0F, -6.0F, -34.0F, 14.0F, 14.0F, 8.0F),
                PartPose.offsetAndRotation(54.0F, 16.0F, 6.0F, 0.0F, (float) (Math.PI / 4), 0.0F));
        root.addOrReplaceChild("LToe9",
                CubeListBuilder.create().texOffs(0, 955)
                        .addBox(-8.0F, -8.0F, -26.0F, 16.0F, 16.0F, 30.0F),
                PartPose.offsetAndRotation(54.0F, 16.0F, 6.0F, 0.0F, (float) (-Math.PI / 4), 0.0F));
        root.addOrReplaceChild("LToe8",
                CubeListBuilder.create().texOffs(0, 1002)
                        .addBox(-7.0F, -6.0F, -34.0F, 14.0F, 14.0F, 8.0F),
                PartPose.offsetAndRotation(54.0F, 16.0F, 6.0F, 0.0F, (float) (-Math.PI / 4), 0.0F));
        root.addOrReplaceChild("LToe7",
                CubeListBuilder.create().texOffs(45, 1002)
                        .addBox(-5.0F, -2.0F, -40.0F, 10.0F, 10.0F, 6.0F),
                PartPose.offsetAndRotation(54.0F, 16.0F, 6.0F, 0.0F, (float) (-Math.PI / 4), 0.0F));
        root.addOrReplaceChild("LToe6",
                CubeListBuilder.create().texOffs(92, 955)
                        .addBox(-8.0F, -8.0F, -26.0F, 16.0F, 16.0F, 36.0F),
                PartPose.offsetAndRotation(54.0F, 16.0F, 6.0F, 0.0F, 0.0F, 0.0F));
        root.addOrReplaceChild("LToe5",
                CubeListBuilder.create().texOffs(0, 1002)
                        .addBox(-7.0F, -6.0F, -34.0F, 14.0F, 14.0F, 8.0F),
                PartPose.offsetAndRotation(54.0F, 16.0F, 6.0F, 0.0F, 0.0F, 0.0F));
        root.addOrReplaceChild("LToe4",
                CubeListBuilder.create().texOffs(45, 1002)
                        .addBox(-5.0F, -2.0F, -40.0F, 10.0F, 10.0F, 6.0F),
                PartPose.offsetAndRotation(54.0F, 16.0F, 6.0F, 0.0F, 0.0F, 0.0F));
        root.addOrReplaceChild("RToe9",
                CubeListBuilder.create().texOffs(0, 955)
                        .addBox(-8.0F, -8.0F, -26.0F, 16.0F, 16.0F, 30.0F),
                PartPose.offsetAndRotation(-54.0F, 16.0F, 6.0F, 0.0F, (float) (Math.PI / 4), 0.0F));
        root.addOrReplaceChild("RToe6",
                CubeListBuilder.create().texOffs(92, 955)
                        .addBox(-8.0F, -8.0F, -26.0F, 16.0F, 16.0F, 36.0F),
                PartPose.offsetAndRotation(-54.0F, 16.0F, 6.0F, 0.0F, 0.0F, 0.0F));
        root.addOrReplaceChild("RToe5",
                CubeListBuilder.create().texOffs(0, 1002)
                        .addBox(-7.0F, -6.0F, -34.0F, 14.0F, 14.0F, 8.0F),
                PartPose.offsetAndRotation(-54.0F, 16.0F, 6.0F, 0.0F, 0.0F, 0.0F));
        root.addOrReplaceChild("RToe2",
                CubeListBuilder.create().texOffs(0, 1002)
                        .addBox(-7.0F, -6.0F, -34.0F, 14.0F, 14.0F, 8.0F),
                PartPose.offsetAndRotation(-54.0F, 16.0F, 6.0F, 0.0F, (float) (-Math.PI / 4), 0.0F));
        root.addOrReplaceChild("RToe1",
                CubeListBuilder.create().texOffs(45, 1002)
                        .addBox(-5.0F, -2.0F, -40.0F, 10.0F, 10.0F, 6.0F),
                PartPose.offsetAndRotation(-54.0F, 16.0F, 6.0F, 0.0F, (float) (-Math.PI / 4), 0.0F));
        root.addOrReplaceChild("RToe4",
                CubeListBuilder.create().texOffs(45, 1002)
                        .addBox(-5.0F, -2.0F, -40.0F, 10.0F, 10.0F, 6.0F),
                PartPose.offsetAndRotation(-54.0F, 16.0F, 6.0F, 0.0F, 0.0F, 0.0F));
        root.addOrReplaceChild("RToe7",
                CubeListBuilder.create().texOffs(45, 1002)
                        .addBox(-5.0F, -2.0F, -40.0F, 10.0F, 10.0F, 6.0F),
                PartPose.offsetAndRotation(-54.0F, 16.0F, 6.0F, 0.0F, (float) (Math.PI / 4), 0.0F));
        root.addOrReplaceChild("RToe8",
                CubeListBuilder.create().texOffs(0, 1002)
                        .addBox(-7.0F, -6.0F, -34.0F, 14.0F, 14.0F, 8.0F),
                PartPose.offsetAndRotation(-54.0F, 16.0F, 6.0F, 0.0F, (float) (Math.PI / 4), 0.0F));
        root.addOrReplaceChild("RToe3",
                CubeListBuilder.create().texOffs(0, 955)
                        .addBox(-8.0F, -8.0F, -26.0F, 16.0F, 16.0F, 30.0F),
                PartPose.offsetAndRotation(-54.0F, 16.0F, 6.0F, 0.0F, (float) (-Math.PI / 4), 0.0F));
        root.addOrReplaceChild("LThigh",
                CubeListBuilder.create().texOffs(192, 350)
                        .addBox(0.0F, -14.0F, -21.0F, 28.0F, 28.0F, 42.0F),
                PartPose.offsetAndRotation(40.0F, -91.0F, 2.0F, -0.5585054F, 0.0F, 0.0F));
        root.addOrReplaceChild("LLowerLeg",
                CubeListBuilder.create().texOffs(202, 556)
                        .addBox(-15.0F, -62.0F, -15.0F, 30.0F, 62.0F, 30.0F),
                PartPose.offsetAndRotation(54.0F, 14.0F, 6.0F, 0.1745329F, -0.1308997F, 0.0F));
        root.addOrReplaceChild("LUpperLeg",
                CubeListBuilder.create().texOffs(152, 420)
                        .addBox(-16.0F, -52.0F, -16.0F, 32.0F, 52.0F, 32.0F),
                PartPose.offsetAndRotation(56.0F, -36.0F, -5.0F, -0.1745329F, (float) (-Math.PI / 8), -0.0872665F));
        root.addOrReplaceChild("TailTip",
                CubeListBuilder.create().texOffs(0, 694)
                        .addBox(-6.0F, 0.0F, -5.0F, 12.0F, 21.0F, 10.0F),
                PartPose.offsetAndRotation(0.0F, 18.0F, 203.0F, 1.53589F, 0.0F, 0.0F));
        root.addOrReplaceChild("RLegLower",
                CubeListBuilder.create().texOffs(200, 646)
                        .addBox(-15.0F, -62.0F, -15.0F, 30.0F, 62.0F, 30.0F),
                PartPose.offsetAndRotation(-54.0F, 16.0F, 6.0F, 0.1745329F, 0.1308997F, 0.0F));
        root.addOrReplaceChild("RLegUpper",
                CubeListBuilder.create().texOffs(152, 420)
                        .addBox(-16.0F, -52.0F, -16.0F, 32.0F, 52.0F, 32.0F),
                PartPose.offsetAndRotation(-56.0F, -36.0F, -5.0F, -0.1745329F, (float) (Math.PI / 8), 0.0872665F));
        root.addOrReplaceChild("RThigh",
                CubeListBuilder.create().texOffs(192, 350)
                        .addBox(-28.0F, -14.0F, -21.0F, 28.0F, 28.0F, 42.0F),
                PartPose.offsetAndRotation(-40.0F, -91.0F, 2.0F, -0.5585054F, 0.0F, 0.0F));
        root.addOrReplaceChild("LowerJaw",
                CubeListBuilder.create().texOffs(272, 0)
                        .addBox(-13.0F, -5.0F, -50.0F, 26.0F, 11.0F, 50.0F),
                PartPose.offsetAndRotation(0.0F, -142.0F, -109.0F, (float) (Math.PI / 6), 0.0F, 0.0F));
        root.addOrReplaceChild("TailBase",
                CubeListBuilder.create().texOffs(0, 240)
                        .addBox(-32.0F, 0.0F, -29.0F, 64.0F, 40.0F, 58.0F),
                PartPose.offsetAndRotation(0.0F, -73.0F, 26.0F, (float) (Math.PI / 4), 0.0F, 0.0F));
        root.addOrReplaceChild("Tail2",
                CubeListBuilder.create().texOffs(0, 338)
                        .addBox(-25.0F, 0.0F, -23.0F, 50.0F, 36.0F, 46.0F),
                PartPose.offsetAndRotation(0.0F, -48.0F, 48.0F, (float) (Math.PI * 2.0 / 9.0), 0.0F, 0.0F));
        root.addOrReplaceChild("Tail3",
                CubeListBuilder.create().texOffs(0, 420)
                        .addBox(-20.0F, 0.0F, -18.0F, 40.0F, 36.0F, 36.0F),
                PartPose.offsetAndRotation(0.0F, -24.0F, 66.0F, 0.8726646F, 0.0F, 0.0F));
        root.addOrReplaceChild("Tail4",
                CubeListBuilder.create().texOffs(0, 492)
                        .addBox(-16.0F, 0.0F, -14.0F, 32.0F, 42.0F, 28.0F),
                PartPose.offsetAndRotation(0.0F, -3.0F, 87.0F, 1.134464F, 0.0F, 0.0F));
        root.addOrReplaceChild("Tail5",
                CubeListBuilder.create().texOffs(0, 556)
                        .addBox(-13.0F, 0.0F, -11.0F, 26.0F, 42.0F, 22.0F),
                PartPose.offsetAndRotation(0.0F, 12.0F, 116.0F, 1.53589F, 0.0F, 0.0F));
        root.addOrReplaceChild("Tail6",
                CubeListBuilder.create().texOffs(0, 614)
                        .addBox(-10.0F, 0.0F, -9.0F, 20.0F, 32.0F, 18.0F),
                PartPose.offsetAndRotation(0.0F, 14.0F, 154.0F, 1.53589F, 0.0F, 0.0F));
        root.addOrReplaceChild("Tail7",
                CubeListBuilder.create().texOffs(0, 658)
                        .addBox(-8.0F, 0.0F, -7.0F, 16.0F, 22.0F, 14.0F),
                PartPose.offsetAndRotation(0.0F, 16.0F, 185.0F, 1.53589F, 0.0F, 0.0F));
        root.addOrReplaceChild("BodyBottom",
                CubeListBuilder.create().texOffs(0, 104)
                        .addBox(-40.0F, 0.0F, -36.0F, 80.0F, 64.0F, 72.0F),
                PartPose.offsetAndRotation(0.0F, -112.0F, -20.0F, 0.8726646F, 0.0F, 0.0F));
        root.addOrReplaceChild("RLowerArm",
                CubeListBuilder.create().texOffs(245, 240)
                        .addBox(-48.0F, -11.0F, -11.0F, 48.0F, 22.0F, 22.0F),
                PartPose.offsetAndRotation(-80.0F, -115.0F, -61.0F, 0.0F, (float) (-Math.PI / 4), (float) (-Math.PI / 12)));
        root.addOrReplaceChild("BodyCenter",
                CubeListBuilder.create().texOffs(0, 0)
                        .addBox(-36.0F, -32.0F, -32.0F, 72.0F, 40.0F, 64.0F),
                PartPose.offsetAndRotation(0.0F, -112.0F, -20.0F, 1.134464F, 0.0F, 0.0F));
        root.addOrReplaceChild("Neck",
                CubeListBuilder.create().texOffs(0, 720)
                        .addBox(-23.0F, -23.0F, -32.0F, 46.0F, 46.0F, 32.0F),
                PartPose.offsetAndRotation(0.0F, -144.0F, -71.0F, -0.0698132F, 0.0F, 0.0F));
        root.addOrReplaceChild("TopJaw",
                CubeListBuilder.create().texOffs(0, 892)
                        .addBox(-14.0F, -8.0F, -73.0F, 28.0F, 26.0F, 33.0F),
                PartPose.offsetAndRotation(0.0F, -156.0F, -98.0F, 0.0872665F, 0.0F, 0.0F));
        root.addOrReplaceChild("Head",
                CubeListBuilder.create().texOffs(0, 808)
                        .addBox(-17.0F, -18.0F, -40.0F, 34.0F, 36.0F, 40.0F),
                PartPose.offsetAndRotation(0.0F, -156.0F, -98.0F, 0.0872665F, 0.0F, 0.0F));
        root.addOrReplaceChild("BodyTop",
                CubeListBuilder.create().texOffs(0, 0)
                        .addBox(-36.0F, -32.0F, -32.0F, 72.0F, 40.0F, 64.0F),
                PartPose.offsetAndRotation(0.0F, -126.0F, -50.0F, 1.308997F, 0.0F, 0.0F));
        root.addOrReplaceChild("RShoulder",
                CubeListBuilder.create().texOffs(304, 96)
                        .addBox(-16.0F, -32.0F, -32.0F, 16.0F, 42.0F, 46.0F),
                PartPose.offsetAndRotation(-36.0F, -130.0F, -42.0F, 1.308997F, 0.0F, 0.0F));
        root.addOrReplaceChild("RThumbTip",
                CubeListBuilder.create().texOffs(422, 18)
                        .addBox(5.0F, 1.0F, -43.0F, 8.0F, 8.0F, 12.0F),
                PartPose.offsetAndRotation(-115.0F, -100.0F, -99.0F, 0.0F, 0.0F, 0.0F));
        root.addOrReplaceChild("RUpperArm",
                CubeListBuilder.create().texOffs(304, 184)
                        .addBox(-54.0F, -13.0F, -13.0F, 54.0F, 26.0F, 26.0F),
                PartPose.offsetAndRotation(-38.0F, -130.0F, -52.0F, 0.0F, (float) (-Math.PI / 12), -0.3490659F));
        root.addOrReplaceChild("RHand",
                CubeListBuilder.create().texOffs(245, 292)
                        .addBox(-13.0F, -13.0F, -13.0F, 26.0F, 26.0F, 26.0F),
                PartPose.offsetAndRotation(-115.0F, -100.0F, -99.0F, -1.071467F, 2.007129F, 0.1745329F));
        root.addOrReplaceChild("RThumbBase",
                CubeListBuilder.create().texOffs(424, 57)
                        .addBox(2.0F, 1.0F, -32.0F, 8.0F, 8.0F, 20.0F),
                PartPose.offsetAndRotation(-115.0F, -100.0F, -99.0F, 0.0F, -0.1047198F, 0.0F));
        root.addOrReplaceChild("R3rdFingerTip",
                CubeListBuilder.create().texOffs(422, 18)
                        .addBox(-10.0F, 0.0F, -41.0F, 8.0F, 8.0F, 12.0F),
                PartPose.offsetAndRotation(-115.0F, -100.0F, -99.0F, 0.0F, 0.6806784F, 0.0F));
        root.addOrReplaceChild("R3rdFingerBase",
                CubeListBuilder.create().texOffs(424, 57)
                        .addBox(-11.0F, -3.0F, -30.0F, 8.0F, 8.0F, 20.0F),
                PartPose.offsetAndRotation(-115.0F, -100.0F, -99.0F, 0.122173F, 0.6457718F, 0.0F));
        root.addOrReplaceChild("RIndexTip",
                CubeListBuilder.create().texOffs(422, 18)
                        .addBox(-4.0F, -12.0F, -43.0F, 8.0F, 8.0F, 12.0F),
                PartPose.offsetAndRotation(-115.0F, -100.0F, -99.0F, -0.2094395F, 0.1745329F, 0.0F));
        root.addOrReplaceChild("RIndexBase",
                CubeListBuilder.create().texOffs(424, 57)
                        .addBox(-4.0F, -9.0F, -34.0F, 8.0F, 8.0F, 20.0F),
                PartPose.offsetAndRotation(-115.0F, -100.0F, -99.0F, -0.2792527F, 0.1570796F, 0.0F));
        root.addOrReplaceChild("LShoulder",
                CubeListBuilder.create().texOffs(304, 96)
                        .addBox(0.0F, -32.0F, -32.0F, 16.0F, 42.0F, 46.0F),
                PartPose.offsetAndRotation(36.0F, -130.0F, -42.0F, 1.308997F, 0.0F, 0.0F));
        root.addOrReplaceChild("LUpperArm",
                CubeListBuilder.create().texOffs(304, 184)
                        .addBox(0.0F, -13.0F, -13.0F, 54.0F, 26.0F, 26.0F),
                PartPose.offsetAndRotation(38.0F, -130.0F, -52.0F, 0.0F, 0.296706F, 0.3490659F));
        root.addOrReplaceChild("LLowerArm",
                CubeListBuilder.create().texOffs(245, 240)
                        .addBox(0.0F, -11.0F, -11.0F, 48.0F, 22.0F, 22.0F),
                PartPose.offsetAndRotation(80.0F, -115.0F, -61.0F, 0.0F, (float) (Math.PI / 4), (float) (Math.PI / 12)));
        root.addOrReplaceChild("LIndexBase",
                CubeListBuilder.create().texOffs(424, 57)
                        .addBox(-4.0F, -13.0F, -32.0F, 8.0F, 8.0F, 20.0F),
                PartPose.offsetAndRotation(115.0F, -100.0F, -99.0F, -0.1570796F, -0.1396263F, 0.0F));
        root.addOrReplaceChild("LIndexTip",
                CubeListBuilder.create().texOffs(422, 18)
                        .addBox(-1.0F, -18.0F, -41.0F, 8.0F, 8.0F, 12.0F),
                PartPose.offsetAndRotation(115.0F, -100.0F, -99.0F, 0.0F, -0.0349066F, 0.0F));
        root.addOrReplaceChild("LHand",
                CubeListBuilder.create().texOffs(245, 292)
                        .addBox(-13.0F, -13.0F, -13.0F, 26.0F, 26.0F, 26.0F),
                PartPose.offsetAndRotation(115.0F, -100.0F, -99.0F, 0.9599311F, 1.308997F, 0.1745329F));
        root.addOrReplaceChild("LThumbBase",
                CubeListBuilder.create().texOffs(424, 57)
                        .addBox(-8.0F, -2.0F, -32.0F, 8.0F, 8.0F, 20.0F),
                PartPose.offsetAndRotation(115.0F, -100.0F, -98.0F, 0.1396263F, (float) (Math.PI / 12), 0.0F));
        root.addOrReplaceChild("LThumbTip",
                CubeListBuilder.create().texOffs(422, 18)
                        .addBox(-12.0F, 2.0F, -40.0F, 8.0F, 8.0F, 12.0F),
                PartPose.offsetAndRotation(115.0F, -100.0F, -99.0F, 0.0F, 0.1396263F, 0.0F));
        root.addOrReplaceChild("L3rdFingerTip",
                CubeListBuilder.create().texOffs(422, 18)
                        .addBox(9.0F, 2.0F, -42.0F, 8.0F, 8.0F, 12.0F),
                PartPose.offsetAndRotation(115.0F, -100.0F, -99.0F, 0.0349066F, -0.3316126F, 0.0F));
        root.addOrReplaceChild("L3rdFingerBase",
                CubeListBuilder.create().texOffs(424, 57)
                        .addBox(4.0F, -5.0F, -33.0F, 8.0F, 8.0F, 20.0F),
                PartPose.offsetAndRotation(115.0F, -100.0F, -99.0F, (float) (Math.PI / 12), -0.4712389F, 0.0F));
        root.addOrReplaceChild("Lspikes1",
                CubeListBuilder.create().texOffs(500, 0)
                        .addBox(0.0F, -10.0F, 0.0F, 0.0F, 10.0F, 11.0F),
                PartPose.offsetAndRotation(5.0F, -168.0F, -86.0F, -0.0872665F, 0.0F, -0.0174533F));
        root.addOrReplaceChild("Rspikes1",
                CubeListBuilder.create().texOffs(500, 0)
                        .addBox(0.0F, -10.0F, 0.0F, 0.0F, 10.0F, 11.0F),
                PartPose.offsetAndRotation(-5.0F, -168.0F, -86.0F, -0.0872665F, 0.0F, -0.0174533F));
        root.addOrReplaceChild("Lspike2",
                CubeListBuilder.create().texOffs(500, 30)
                        .addBox(0.0F, -25.0F, 0.0F, 0.0F, 25.0F, 21.0F),
                PartPose.offsetAndRotation(10.0F, -162.0F, -63.0F, (float) (-Math.PI / 12), 0.0F, -0.0174533F));
        root.addOrReplaceChild("Rspike2",
                CubeListBuilder.create().texOffs(500, 30)
                        .addBox(0.0F, -25.0F, 0.0F, 0.0F, 25.0F, 21.0F),
                PartPose.offsetAndRotation(-10.0F, -162.0F, -63.0F, (float) (-Math.PI / 12), 0.0F, -0.0174533F));
        root.addOrReplaceChild("Lspike3",
                CubeListBuilder.create().texOffs(500, 80)
                        .addBox(0.0F, -45.0F, 0.0F, 0.0F, 45.0F, 34.0F),
                PartPose.offsetAndRotation(14.0F, -153.0F, -32.0F, -0.4363323F, 0.0F, -0.0174533F));
        root.addOrReplaceChild("Rspike3",
                CubeListBuilder.create().texOffs(500, 80)
                        .addBox(0.0F, -45.0F, 0.0F, 0.0F, 45.0F, 34.0F),
                PartPose.offsetAndRotation(-14.0F, -153.0F, -32.0F, -0.4363323F, 0.0F, -0.0174533F));
        root.addOrReplaceChild("Lspike4",
                CubeListBuilder.create().texOffs(500, 165)
                        .addBox(0.0F, -50.0F, 0.0F, 0.0F, 50.0F, 36.0F),
                PartPose.offsetAndRotation(18.0F, -131.0F, 13.0F, -0.715585F, 0.0F, -0.0174533F));
        root.addOrReplaceChild("Rspike4",
                CubeListBuilder.create().texOffs(500, 165)
                        .addBox(0.0F, -50.0F, 0.0F, 0.0F, 50.0F, 36.0F),
                PartPose.offsetAndRotation(-18.0F, -131.0F, 13.0F, -0.715585F, 0.0F, -0.0174533F));
        root.addOrReplaceChild("Lspike5",
                CubeListBuilder.create().texOffs(500, 255)
                        .addBox(12.0F, -67.0F, 5.0F, 0.0F, 39.0F, 27.0F),
                PartPose.offsetAndRotation(0.0F, -73.0F, 26.0F, (float) (-Math.PI / 4), 0.0F, -0.0174533F));
        root.addOrReplaceChild("Rspike5",
                CubeListBuilder.create().texOffs(500, 255)
                        .addBox(-12.0F, -67.0F, 5.0F, 0.0F, 39.0F, 27.0F),
                PartPose.offsetAndRotation(0.0F, -73.0F, 26.0F, (float) (-Math.PI / 4), 0.0F, -0.0174533F));
        root.addOrReplaceChild("Spike6",
                CubeListBuilder.create().texOffs(500, 325)
                        .addBox(0.0F, -48.0F, 11.0F, 0.0F, 25.0F, 21.0F),
                PartPose.offsetAndRotation(0.0F, -48.0F, 48.0F, -0.8901179F, 0.0F, -0.0174533F));
        root.addOrReplaceChild("Spikes7",
                CubeListBuilder.create().texOffs(500, 376)
                        .addBox(0.0F, -29.0F, 20.0F, 0.0F, 10.0F, 11.0F),
                PartPose.offsetAndRotation(0.0F, -24.0F, 66.0F, -0.7504916F, 0.0F, -0.0174533F));
        return LayerDefinition.create(mesh, 1024, 1024);
    }

    @Override
    public ModelPart root() {
        return this.root;
    }

    @Override
    public void setupAnim(
            Godzilla e,
            float f,
            float f1,
            float f2,
            float f3,
            float f4) {
        RenderInfo r = e.getRenderInfo();
        float newangle = 0.0F;
        float newangle2 = 0.0F;
        float pscale = 1.0F;
        float pi4 = (float) (Math.PI / 4);
        float clawZ = 6.0F;
        float clawY = 16.0F;
        float clawZamp = 35.0F * pscale;
        float clawYamp = 18.0F * pscale;
        float t1 = 0.0F;
        float t2 = 0.0F;
        if (f1 > 0.001F) {
            newangle = Mth.cos(f2 * 0.75F * this.wingspeed / pscale);
            newangle2 = Mth.cos(f2 * 0.75F * this.wingspeed / pscale + pi4);
            t1 = Mth.sin(f2 * 0.75F * this.wingspeed / pscale);
        } else {
            newangle2 = 0.0F;
            newangle = 0.0F;
            t1 = 0.0F;
            t2 = 0.0F;
        }

        if (t1 > 0.0F) {
            t2 = t1 * clawYamp * f1;
            this.LToe1.y = clawY - t2;
        } else {
            this.LToe1.y = clawY;
        }

        this.LToe1.z = clawZ + clawZamp * newangle * f1;
        this.LToe2.z = this.LToe3.z = this.LToe4.z = this.LToe5.z = this.LToe6.z = this.LToe7.z = this.LToe8.z = this.LToe9.z = this.LToe1.z;
        this.LToe2.y = this.LToe3.y = this.LToe4.y = this.LToe5.y = this.LToe6.y = this.LToe7.y = this.LToe8.y = this.LToe9.y = this.LToe1.y;
        this.LLowerLeg.z = this.LToe1.z;
        this.LLowerLeg.y = this.LToe1.y;
        this.LLowerLeg.xRot = 0.22F + newangle * (float) Math.PI * 0.09F * f1;
        this.LUpperLeg.xRot = -0.17F + newangle2 * (float) Math.PI * 0.15F * f1;
        this.LUpperLeg.y = this.LLowerLeg.y - (float) Math.cos(this.LLowerLeg.xRot) * 55.0F;
        this.LUpperLeg.z = this.LLowerLeg.z - (float) Math.sin(this.LLowerLeg.xRot) * 55.0F;
        this.LThigh.xRot = -0.558F + newangle2 * (float) Math.PI * 0.1F * f1;
        this.LThigh.z = 2.0F + clawZamp * newangle * f1 / 4.0F;

        t1 = 0.0F;
        t2 = 0.0F;
        if (f1 > 0.001F) {
            newangle = Mth.cos(f2 * 0.75F * this.wingspeed / pscale + pi4 * 4.0F);
            newangle2 = Mth.cos(f2 * 0.75F * this.wingspeed / pscale + pi4 * 5.0F);
            t1 = Mth.sin(f2 * 0.75F * this.wingspeed / pscale + pi4 * 4.0F);
        } else {
            newangle = 0.0F;
            t1 = 0.0F;
            t2 = 0.0F;
        }

        if (t1 > 0.0F) {
            t2 = t1 * clawYamp * f1;
            this.RToe1.y = clawY - t2;
        } else {
            this.RToe1.y = clawY;
        }

        this.RToe1.z = clawZ + clawZamp * newangle * f1;
        this.RToe2.z = this.RToe3.z = this.RToe4.z = this.RToe5.z = this.RToe6.z = this.RToe7.z = this.RToe8.z = this.RToe9.z = this.RToe1.z;
        this.RToe2.y = this.RToe3.y = this.RToe4.y = this.RToe5.y = this.RToe6.y = this.RToe7.y = this.RToe8.y = this.RToe9.y = this.RToe1.y;
        this.RLegLower.z = this.RToe1.z;
        this.RLegLower.y = this.RToe1.y;
        this.RLegLower.xRot = 0.22F + newangle * (float) Math.PI * 0.09F * f1;
        this.RLegUpper.xRot = -0.17F + newangle2 * (float) Math.PI * 0.15F * f1;
        this.RLegUpper.y = this.RLegLower.y - (float) Math.cos(this.RLegLower.xRot) * 55.0F;
        this.RLegUpper.z = this.RLegLower.z - (float) Math.sin(this.RLegLower.xRot) * 55.0F;
        this.RThigh.xRot = -0.558F + newangle2 * (float) Math.PI * 0.1F * f1;
        this.RThigh.z = 2.0F + clawZamp * newangle * f1 / 4.0F;

        this.LToe1.xRot = this.LToe2.xRot = this.LToe3.xRot = this.LToe4.xRot = this.LToe5.xRot = this.LToe6.xRot = this.LToe7.xRot = this.LToe8.xRot = this.LToe9.xRot = 0.0F;
        this.RToe1.xRot = this.RToe2.xRot = this.RToe3.xRot = this.RToe4.xRot = this.RToe5.xRot = this.RToe6.xRot = this.RToe7.xRot = this.RToe8.xRot = this.RToe9.xRot = 0.0F;

        if (e.getAttacking() != 0) {
            newangle = Mth.cos(f2 * this.wingspeed * 1.75F) * (float) Math.PI * 0.2F;
        } else {
            newangle = Mth.cos(f2 * this.wingspeed * 0.75F) * (float) Math.PI * 0.05F;
        }

        this.doTail(newangle);
        newangle = (float) Math.toRadians(f3) * 0.55F;
        this.Head.yRot = newangle;
        this.TopJaw.yRot = newangle;
        this.LowerJaw.yRot = newangle;
        this.LowerJaw.z = this.Head.z - (float) Math.cos(this.Head.yRot) * 11.0F;
        this.LowerJaw.x = this.Head.x - (float) Math.sin(this.Head.yRot) * 11.0F;
        this.TopJaw.xRot = this.Head.xRot = (float) Math.toRadians(f4);
        newangle = Mth.cos(f2 * this.wingspeed * 1.5F) * (float) Math.PI * 0.12F;
        float newrf1 = f2 * 1.5F * this.wingspeed % (float) (Math.PI * 2);
        newrf1 = Math.abs(newrf1);
        if (newrf1 < r.rf2) {
            r.ri2 = 0;
            if (e.getAttacking() == 0) {
                if (e.level().random.nextInt(20) == 1) {
                    r.ri2 |= 1;
                }
            } else if (e.level().random.nextInt(2) == 1) {
                r.ri2 |= 1;
            }
        }

        r.rf2 = newrf1;
        if ((r.ri2 & 1) == 0) {
            newangle = 0.0F;
        }

        this.LowerJaw.xRot = 0.52F + newangle + this.TopJaw.xRot;
        newangle = newangle2 = Mth.sin(f2 * this.wingspeed * 1.75F) * (float) Math.PI * 0.16F;
        newrf1 = f2 * 1.75F * this.wingspeed % (float) (Math.PI * 2);
        newrf1 = Math.abs(newrf1);
        if (newrf1 < r.rf1) {
            r.ri1 = 0;
            if (e.getAttacking() == 0) {
                if (e.level().random.nextInt(20) == 1) {
                    r.ri1 |= 1;
                }
                if (e.level().random.nextInt(20) == 1) {
                    r.ri1 |= 2;
                }
            } else {
                if (e.level().random.nextInt(2) == 1) {
                    r.ri1 |= 1;
                }
                if (e.level().random.nextInt(2) == 1) {
                    r.ri1 |= 2;
                }
            }
        }

        r.rf1 = newrf1;
        if ((r.ri1 & 1) == 0) {
            newangle = 0.0F;
        }
        if ((r.ri1 & 2) == 0) {
            newangle2 = 0.0F;
        }

        this.LUpperArm.yRot = 0.65F + newangle;
        this.LLowerArm.yRot = 0.78F + newangle * 3.0F / 2.0F;
        this.LLowerArm.z = this.LUpperArm.z - (float) Math.sin(this.LUpperArm.yRot) * 50.0F;
        this.LLowerArm.x = this.LUpperArm.x + (float) Math.cos(this.LUpperArm.yRot) * 50.0F;
        this.LLowerArm.y = this.LUpperArm.y - (float) Math.sin(this.LUpperArm.yRot) * 10.0F + 18.0F;
        this.LHand.z = this.LLowerArm.z - (float) Math.sin(this.LLowerArm.yRot) * 45.0F;
        this.LHand.x = this.LLowerArm.x + (float) Math.cos(this.LLowerArm.yRot) * 45.0F;
        this.LHand.y = this.LLowerArm.y - (float) Math.sin(this.LLowerArm.yRot) * 10.0F + 15.0F;
        this.LIndexBase.z = this.LThumbBase.z = this.L3rdFingerBase.z = this.LHand.z;
        this.LIndexTip.z = this.LThumbTip.z = this.L3rdFingerTip.z = this.LHand.z;
        this.LIndexBase.y = this.LThumbBase.y = this.L3rdFingerBase.y = this.LHand.y;
        this.LIndexTip.y = this.LThumbTip.y = this.L3rdFingerTip.y = this.LHand.y;
        this.LIndexBase.x = this.LThumbBase.x = this.L3rdFingerBase.x = this.LHand.x;
        this.LIndexTip.x = this.LThumbTip.x = this.L3rdFingerTip.x = this.LHand.x;
        this.LHand.yRot = 1.308F + newangle * 2.0F;
        this.LIndexBase.yRot = -0.139F + newangle * 2.0F;
        this.LIndexTip.yRot = -0.034F + newangle * 2.0F;
        this.LThumbBase.yRot = 0.261F + newangle;
        this.LThumbTip.yRot = 0.139F + newangle;
        this.L3rdFingerBase.yRot = -0.471F + newangle * 3.0F;
        this.L3rdFingerTip.yRot = -0.331F + newangle * 3.0F;

        this.RUpperArm.yRot = -0.65F - newangle2;
        this.RLowerArm.yRot = -0.78F - newangle2 * 3.0F / 2.0F;
        this.RLowerArm.z = this.RUpperArm.z + (float) Math.sin(this.RUpperArm.yRot) * 50.0F;
        this.RLowerArm.x = this.RUpperArm.x - (float) Math.cos(this.RUpperArm.yRot) * 50.0F;
        this.RLowerArm.y = this.RUpperArm.y + (float) Math.sin(this.RUpperArm.yRot) * 10.0F + 18.0F;
        this.RHand.z = this.RLowerArm.z + (float) Math.sin(this.RLowerArm.yRot) * 45.0F;
        this.RHand.x = this.RLowerArm.x - (float) Math.cos(this.RLowerArm.yRot) * 45.0F;
        this.RHand.y = this.RLowerArm.y + (float) Math.sin(this.RLowerArm.yRot) * 10.0F + 15.0F;
        this.RIndexBase.z = this.RThumbBase.z = this.R3rdFingerBase.z = this.RHand.z;
        this.RIndexTip.z = this.RThumbTip.z = this.R3rdFingerTip.z = this.RHand.z;
        this.RIndexBase.y = this.RThumbBase.y = this.R3rdFingerBase.y = this.RHand.y;
        this.RIndexTip.y = this.RThumbTip.y = this.R3rdFingerTip.y = this.RHand.y;
        this.RIndexBase.x = this.RThumbBase.x = this.R3rdFingerBase.x = this.RHand.x;
        this.RIndexTip.x = this.RThumbTip.x = this.R3rdFingerTip.x = this.RHand.x;
        this.RHand.yRot = -2.0F - newangle2 * 2.0F;
        this.RIndexBase.yRot = 0.157F - newangle2 * 2.0F;
        this.RIndexTip.yRot = 0.174F - newangle2 * 2.0F;
        this.RThumbBase.yRot = -0.104F - newangle2;
        this.RThumbTip.yRot = 0.001F - newangle2;
        this.R3rdFingerTip.yRot = 0.68F - newangle2 * 3.0F;
        this.R3rdFingerBase.yRot = 0.645F - newangle2 * 3.0F;
        e.setRenderInfo(r);
    }

    private void doTail(float angle) {
        this.TailBase.yRot = angle * 0.25F;
        this.Lspike5.yRot = this.Rspike5.yRot = this.TailBase.yRot;
        this.Tail2.yRot = angle * 0.5F;
        this.Tail2.z = this.TailBase.z + (float) Math.cos(this.TailBase.yRot) * 25.0F;
        this.Tail2.x = this.TailBase.x + (float) Math.sin(this.TailBase.yRot) * 25.0F;
        this.Spike6.yRot = this.Tail2.yRot;
        this.Spike6.z = this.Tail2.z;
        this.Spike6.x = this.Tail2.x;
        this.Tail3.yRot = angle * 0.75F;
        this.Tail3.z = this.Tail2.z + (float) Math.cos(this.Tail2.yRot) * 20.0F;
        this.Tail3.x = this.Tail2.x + (float) Math.sin(this.Tail2.yRot) * 20.0F;
        this.Spikes7.yRot = this.Tail3.yRot;
        this.Spikes7.z = this.Tail3.z;
        this.Spikes7.x = this.Tail3.x;
        this.Tail4.yRot = angle * 1.25F;
        this.Tail4.z = this.Tail3.z + (float) Math.cos(this.Tail3.yRot) * 20.0F;
        this.Tail4.x = this.Tail3.x + (float) Math.sin(this.Tail3.yRot) * 20.0F;
        this.Tail5.yRot = angle * 1.5F;
        this.Tail5.z = this.Tail4.z + (float) Math.cos(this.Tail4.yRot) * 25.0F;
        this.Tail5.x = this.Tail4.x + (float) Math.sin(this.Tail4.yRot) * 25.0F;
        this.Tail6.yRot = angle * 1.75F;
        this.Tail6.z = this.Tail5.z + (float) Math.cos(this.Tail5.yRot) * 27.0F;
        this.Tail6.x = this.Tail5.x + (float) Math.sin(this.Tail5.yRot) * 27.0F;
        this.Tail7.yRot = angle * 2.0F;
        this.Tail7.z = this.Tail6.z + (float) Math.cos(this.Tail6.yRot) * 28.0F;
        this.Tail7.x = this.Tail6.x + (float) Math.sin(this.Tail6.yRot) * 28.0F;
        this.TailTip.yRot = angle * 2.25F;
        this.TailTip.z = this.Tail7.z + (float) Math.cos(this.Tail7.yRot) * 18.0F;
        this.TailTip.x = this.Tail7.x + (float) Math.sin(this.Tail7.yRot) * 18.0F;
    }
}
