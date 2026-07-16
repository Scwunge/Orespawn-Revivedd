package danger.orespawn.client.model;

import danger.orespawn.entity.EnderReaper;
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
 * Port of gold {@code ModelEnderReaper} (1.7.10 ModelBase, tex 512×512) → 1.21 HierarchicalModel.
 * Cube sizes/UVs/offsets 1:1. Animation from gold {@code render()} (wingspeed 0.23).
 */
@OnlyIn(Dist.CLIENT)
public class ModelEnderReaper extends HierarchicalModel<EnderReaper> {
    public static final ModelLayerLocation LAYER_LOCATION =
            new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "ender_reaper"), "main");

    private final float wingspeed;
    private final ModelPart root;
    private final ModelPart rwing1;
    private final ModelPart lwing1;
    private final ModelPart Shape3;
    private final ModelPart Shape4;
    private final ModelPart Shape5;
    private final ModelPart Shape6;
    private final ModelPart Shape7;
    private final ModelPart Shape8;
    private final ModelPart Shape9;
    private final ModelPart Shape10;
    private final ModelPart Shape11;
    private final ModelPart Shape12;
    private final ModelPart Shape13;
    private final ModelPart Shape14;
    private final ModelPart Shape15;
    private final ModelPart Shape16;
    private final ModelPart Shape17;
    private final ModelPart Shape18;
    private final ModelPart Shape19;
    private final ModelPart Shape20;
    private final ModelPart Shape21;
    private final ModelPart Shape22;
    private final ModelPart Shape23;
    private final ModelPart Shape24;
    private final ModelPart Shape25;
    private final ModelPart Shape26;
    private final ModelPart Shape27;
    private final ModelPart Shape28;
    private final ModelPart Shape29;
    private final ModelPart Shape30;
    private final ModelPart Shape31;
    private final ModelPart Shape32;
    private final ModelPart Shape33;
    private final ModelPart Shape34;
    private final ModelPart Shape35;
    private final ModelPart Shape36;
    private final ModelPart Shape37;
    private final ModelPart Shape38;
    private final ModelPart Shape39;
    private final ModelPart Shape40;
    private final ModelPart Shape41;
    private final ModelPart Shape42;
    private final ModelPart Shape43;
    private final ModelPart Shape44;
    private final ModelPart Shape45;
    private final ModelPart Shape46;
    private final ModelPart Shape47;
    private final ModelPart Shape48;
    private final ModelPart Shape49;
    private final ModelPart rarm2;
    private final ModelPart rarm3;
    private final ModelPart relbow;
    private final ModelPart rarm1;
    private final ModelPart Shape54;
    private final ModelPart larm3;
    private final ModelPart larm2;
    private final ModelPart lelbow;
    private final ModelPart larm1;
    private final ModelPart scythe1;
    private final ModelPart scythe2;
    private final ModelPart scythe3;
    private final ModelPart head;
    private final ModelPart lwing3;
    private final ModelPart lwing2;
    private final ModelPart rwing3;
    private final ModelPart rwing2;

    public ModelEnderReaper(ModelPart root) {
        this(root, 0.23F);
    }

    public ModelEnderReaper(ModelPart root, float wingspeed) {
        this.root = root;
        this.wingspeed = wingspeed;
        this.rwing1 = root.getChild("rwing1");
        this.lwing1 = root.getChild("lwing1");
        this.Shape3 = root.getChild("Shape3");
        this.Shape4 = root.getChild("Shape4");
        this.Shape5 = root.getChild("Shape5");
        this.Shape6 = root.getChild("Shape6");
        this.Shape7 = root.getChild("Shape7");
        this.Shape8 = root.getChild("Shape8");
        this.Shape9 = root.getChild("Shape9");
        this.Shape10 = root.getChild("Shape10");
        this.Shape11 = root.getChild("Shape11");
        this.Shape12 = root.getChild("Shape12");
        this.Shape13 = root.getChild("Shape13");
        this.Shape14 = root.getChild("Shape14");
        this.Shape15 = root.getChild("Shape15");
        this.Shape16 = root.getChild("Shape16");
        this.Shape17 = root.getChild("Shape17");
        this.Shape18 = root.getChild("Shape18");
        this.Shape19 = root.getChild("Shape19");
        this.Shape20 = root.getChild("Shape20");
        this.Shape21 = root.getChild("Shape21");
        this.Shape22 = root.getChild("Shape22");
        this.Shape23 = root.getChild("Shape23");
        this.Shape24 = root.getChild("Shape24");
        this.Shape25 = root.getChild("Shape25");
        this.Shape26 = root.getChild("Shape26");
        this.Shape27 = root.getChild("Shape27");
        this.Shape28 = root.getChild("Shape28");
        this.Shape29 = root.getChild("Shape29");
        this.Shape30 = root.getChild("Shape30");
        this.Shape31 = root.getChild("Shape31");
        this.Shape32 = root.getChild("Shape32");
        this.Shape33 = root.getChild("Shape33");
        this.Shape34 = root.getChild("Shape34");
        this.Shape35 = root.getChild("Shape35");
        this.Shape36 = root.getChild("Shape36");
        this.Shape37 = root.getChild("Shape37");
        this.Shape38 = root.getChild("Shape38");
        this.Shape39 = root.getChild("Shape39");
        this.Shape40 = root.getChild("Shape40");
        this.Shape41 = root.getChild("Shape41");
        this.Shape42 = root.getChild("Shape42");
        this.Shape43 = root.getChild("Shape43");
        this.Shape44 = root.getChild("Shape44");
        this.Shape45 = root.getChild("Shape45");
        this.Shape46 = root.getChild("Shape46");
        this.Shape47 = root.getChild("Shape47");
        this.Shape48 = root.getChild("Shape48");
        this.Shape49 = root.getChild("Shape49");
        this.rarm2 = root.getChild("rarm2");
        this.rarm3 = root.getChild("rarm3");
        this.relbow = root.getChild("relbow");
        this.rarm1 = root.getChild("rarm1");
        this.Shape54 = root.getChild("Shape54");
        this.larm3 = root.getChild("larm3");
        this.larm2 = root.getChild("larm2");
        this.lelbow = root.getChild("lelbow");
        this.larm1 = root.getChild("larm1");
        this.scythe1 = root.getChild("scythe1");
        this.scythe2 = root.getChild("scythe2");
        this.scythe3 = root.getChild("scythe3");
        this.head = root.getChild("head");
        this.lwing3 = root.getChild("lwing3");
        this.lwing2 = root.getChild("lwing2");
        this.rwing3 = root.getChild("rwing3");
        this.rwing2 = root.getChild("rwing2");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        root.addOrReplaceChild("rwing1",
                CubeListBuilder.create().texOffs(20, 430).addBox(0.0F, 0.0F, 0.0F, 0.0F, 50.0F, 17.0F),
                PartPose.offsetAndRotation(-4.0F, -6.9F, 8.5F, 1.745F, -0.785F, 0.0F));
        root.addOrReplaceChild("lwing1",
                CubeListBuilder.create().texOffs(20, 350).addBox(0.0F, 0.0F, 0.0F, 0.0F, 50.0F, 17.0F),
                PartPose.offsetAndRotation(4.0F, -6.9F, 8.5F, 1.745F, 0.785F, 0.0F));
        root.addOrReplaceChild("Shape3",
                CubeListBuilder.create().texOffs(20, 320).addBox(-4.0F, 0.0F, -2.0F, 2.0F, 12.0F, 1.0F),
                PartPose.offset(3.0F, -14.0F, 10.0F));
        root.addOrReplaceChild("Shape4",
                CubeListBuilder.create().texOffs(40, 320).addBox(-4.0F, 0.0F, -2.0F, 2.0F, 6.0F, 1.0F),
                PartPose.offsetAndRotation(3.0F, -2.0F, 10.0F, -0.247F, 0.0F, 0.0F));
        root.addOrReplaceChild("Shape5",
                CubeListBuilder.create().texOffs(20, 310).addBox(-4.0F, 0.0F, -2.0F, 1.0F, 3.0F, 1.0F),
                PartPose.offsetAndRotation(3.5F, 4.0F, 8.0F, -0.768F, 0.0F, 0.0F));
        root.addOrReplaceChild("Shape6",
                CubeListBuilder.create().texOffs(20, 292).addBox(-4.0F, 0.0F, -2.0F, 2.0F, 6.0F, 2.0F),
                PartPose.offsetAndRotation(3.0F, -12.0F, 7.5F, -2.356F, 0.0F, 0.0F));
        root.addOrReplaceChild("Shape7",
                CubeListBuilder.create().texOffs(20, 280).addBox(-4.0F, 0.0F, -2.0F, 4.0F, 1.0F, 1.0F),
                PartPose.offset(5.0F, -14.0F, 10.0F));
        root.addOrReplaceChild("Shape8",
                CubeListBuilder.create().texOffs(20, 269).addBox(-4.0F, 0.0F, -2.0F, 4.0F, 1.0F, 1.0F),
                PartPose.offset(-1.0F, -14.0F, 10.0F));
        root.addOrReplaceChild("Shape9",
                CubeListBuilder.create().texOffs(20, 257).addBox(-4.0F, 0.0F, -2.0F, 4.0F, 1.0F, 1.0F),
                PartPose.offset(-1.0F, -12.0F, 10.0F));
        root.addOrReplaceChild("Shape10",
                CubeListBuilder.create().texOffs(20, 246).addBox(-4.0F, 0.0F, -2.0F, 4.0F, 1.0F, 1.0F),
                PartPose.offset(-1.0F, -10.0F, 10.0F));
        root.addOrReplaceChild("Shape11",
                CubeListBuilder.create().texOffs(20, 237).addBox(-4.0F, 0.0F, -2.0F, 4.0F, 1.0F, 1.0F),
                PartPose.offset(-1.0F, -8.0F, 10.0F));
        root.addOrReplaceChild("Shape12",
                CubeListBuilder.create().texOffs(20, 228).addBox(-4.0F, 0.0F, -2.0F, 2.0F, 1.0F, 1.0F),
                PartPose.offset(1.0F, -6.0F, 10.0F));
        root.addOrReplaceChild("Shape13",
                CubeListBuilder.create().texOffs(20, 219).addBox(-4.0F, 0.0F, -2.0F, 3.0F, 1.0F, 1.0F),
                PartPose.offset(1.0F, -4.0F, 10.0F));
        root.addOrReplaceChild("Shape14",
                CubeListBuilder.create().texOffs(20, 209).addBox(-4.0F, 0.0F, -2.0F, 1.0F, 1.0F, 1.0F),
                PartPose.offset(3.5F, -14.0F, 11.0F));
        root.addOrReplaceChild("Shape15",
                CubeListBuilder.create().texOffs(20, 201).addBox(-4.0F, 0.0F, -2.0F, 1.0F, 1.0F, 1.0F),
                PartPose.offset(3.5F, -12.0F, 11.0F));
        root.addOrReplaceChild("Shape16",
                CubeListBuilder.create().texOffs(20, 194).addBox(-4.0F, 0.0F, -2.0F, 1.0F, 1.0F, 1.0F),
                PartPose.offset(3.5F, -10.0F, 11.0F));
        root.addOrReplaceChild("Shape17",
                CubeListBuilder.create().texOffs(20, 185).addBox(-4.0F, 0.0F, -2.0F, 1.0F, 1.0F, 1.0F),
                PartPose.offset(3.5F, -8.0F, 11.0F));
        root.addOrReplaceChild("Shape18",
                CubeListBuilder.create().texOffs(20, 175).addBox(-4.0F, 0.0F, -2.0F, 1.0F, 1.0F, 1.0F),
                PartPose.offset(3.5F, -6.0F, 11.0F));
        root.addOrReplaceChild("Shape19",
                CubeListBuilder.create().texOffs(20, 165).addBox(-4.0F, 0.0F, -2.0F, 1.0F, 1.0F, 1.0F),
                PartPose.offset(3.5F, -4.0F, 11.0F));
        root.addOrReplaceChild("Shape20",
                CubeListBuilder.create().texOffs(20, 155).addBox(-4.0F, 0.0F, -2.0F, 4.0F, 1.0F, 1.0F),
                PartPose.offset(5.0F, -12.0F, 10.0F));
        root.addOrReplaceChild("Shape21",
                CubeListBuilder.create().texOffs(20, 146).addBox(-4.0F, 0.0F, -2.0F, 4.0F, 1.0F, 1.0F),
                PartPose.offset(5.0F, -10.0F, 10.0F));
        root.addOrReplaceChild("Shape22",
                CubeListBuilder.create().texOffs(20, 139).addBox(-4.0F, 0.0F, -2.0F, 4.0F, 1.0F, 1.0F),
                PartPose.offset(5.0F, -8.0F, 10.0F));
        root.addOrReplaceChild("Shape23",
                CubeListBuilder.create().texOffs(20, 132).addBox(-4.0F, 0.0F, -2.0F, 3.0F, 1.0F, 1.0F),
                PartPose.offset(5.0F, -6.0F, 10.0F));
        root.addOrReplaceChild("Shape24",
                CubeListBuilder.create().texOffs(20, 124).addBox(-4.0F, 0.0F, -2.0F, 2.0F, 1.0F, 1.0F),
                PartPose.offset(5.0F, -4.0F, 10.0F));
        root.addOrReplaceChild("Shape25",
                CubeListBuilder.create().texOffs(20, 114).addBox(-4.0F, 0.0F, -2.0F, 1.0F, 1.0F, 3.0F),
                PartPose.offset(6.0F, -4.0F, 8.0F));
        root.addOrReplaceChild("Shape26",
                CubeListBuilder.create().texOffs(20, 106).addBox(-4.0F, 0.0F, -2.0F, 2.0F, 1.0F, 1.0F),
                PartPose.offset(5.0F, -4.0F, 8.0F));
        root.addOrReplaceChild("Shape27",
                CubeListBuilder.create().texOffs(20, 94).addBox(-4.0F, 0.0F, -2.0F, 1.0F, 1.0F, 5.0F),
                PartPose.offset(7.0F, -6.0F, 6.0F));
        root.addOrReplaceChild("Shape28",
                CubeListBuilder.create().texOffs(20, 83).addBox(-4.0F, 0.0F, -2.0F, 1.0F, 1.0F, 5.0F),
                PartPose.offset(8.0F, -8.0F, 5.0F));
        root.addOrReplaceChild("Shape29",
                CubeListBuilder.create().texOffs(20, 70).addBox(-4.0F, 0.0F, -2.0F, 1.0F, 1.0F, 6.0F),
                PartPose.offset(8.0F, -10.0F, 4.0F));
        root.addOrReplaceChild("Shape30",
                CubeListBuilder.create().texOffs(20, 59).addBox(-4.0F, 0.0F, -2.0F, 1.0F, 1.0F, 6.0F),
                PartPose.offset(8.0F, -12.0F, 4.0F));
        root.addOrReplaceChild("Shape31",
                CubeListBuilder.create().texOffs(20, 47).addBox(-4.0F, 0.0F, -2.0F, 1.0F, 1.0F, 6.0F),
                PartPose.offset(8.0F, -14.0F, 4.0F));
        root.addOrReplaceChild("Shape32",
                CubeListBuilder.create().texOffs(20, 37).addBox(-4.0F, 0.0F, -2.0F, 2.0F, 1.0F, 1.0F),
                PartPose.offset(6.0F, -6.0F, 6.0F));
        root.addOrReplaceChild("Shape33",
                CubeListBuilder.create().texOffs(20, 29).addBox(-4.0F, 0.0F, -2.0F, 4.0F, 1.0F, 1.0F),
                PartPose.offset(5.0F, -8.0F, 5.0F));
        root.addOrReplaceChild("Shape34",
                CubeListBuilder.create().texOffs(40, 312).addBox(-4.0F, 0.0F, -2.0F, 4.0F, 1.0F, 1.0F),
                PartPose.offset(5.0F, -10.0F, 4.0F));
        root.addOrReplaceChild("Shape35",
                CubeListBuilder.create().texOffs(40, 301).addBox(-4.0F, 0.0F, -2.0F, 4.0F, 1.0F, 1.0F),
                PartPose.offset(5.0F, -12.0F, 4.0F));
        root.addOrReplaceChild("Shape36",
                CubeListBuilder.create().texOffs(40, 291).addBox(-4.0F, 0.0F, -2.0F, 4.0F, 1.0F, 1.0F),
                PartPose.offset(5.0F, -14.0F, 4.0F));
        root.addOrReplaceChild("Shape37",
                CubeListBuilder.create().texOffs(40, 278).addBox(-4.0F, 0.0F, -2.0F, 1.0F, 1.0F, 3.0F),
                PartPose.offset(1.0F, -4.0F, 8.0F));
        root.addOrReplaceChild("Shape38",
                CubeListBuilder.create().texOffs(40, 265).addBox(-4.0F, 0.0F, -2.0F, 1.0F, 1.0F, 5.0F),
                PartPose.offset(0.0F, -6.0F, 6.0F));
        root.addOrReplaceChild("Shape39",
                CubeListBuilder.create().texOffs(40, 251).addBox(-4.0F, 0.0F, -2.0F, 1.0F, 1.0F, 6.0F),
                PartPose.offset(-1.0F, -8.0F, 5.0F));
        root.addOrReplaceChild("Shape40",
                CubeListBuilder.create().texOffs(40, 235).addBox(-4.0F, 0.0F, -2.0F, 1.0F, 1.0F, 6.0F),
                PartPose.offset(-1.0F, -10.0F, 4.0F));
        root.addOrReplaceChild("Shape41",
                CubeListBuilder.create().texOffs(40, 222).addBox(-4.0F, 0.0F, -2.0F, 1.0F, 1.0F, 6.0F),
                PartPose.offset(-1.0F, -12.0F, 4.0F));
        root.addOrReplaceChild("Shape42",
                CubeListBuilder.create().texOffs(40, 209).addBox(-4.0F, 0.0F, -2.0F, 1.0F, 1.0F, 6.0F),
                PartPose.offset(-1.0F, -14.0F, 4.0F));
        root.addOrReplaceChild("Shape43",
                CubeListBuilder.create().texOffs(40, 200).addBox(-4.0F, 0.0F, -2.0F, 2.0F, 1.0F, 1.0F),
                PartPose.offset(1.0F, -4.0F, 8.0F));
        root.addOrReplaceChild("Shape44",
                CubeListBuilder.create().texOffs(40, 189).addBox(-4.0F, 0.0F, -2.0F, 2.0F, 1.0F, 1.0F),
                PartPose.offset(0.0F, -6.0F, 6.0F));
        root.addOrReplaceChild("Shape45",
                CubeListBuilder.create().texOffs(40, 180).addBox(-4.0F, 0.0F, -2.0F, 4.0F, 1.0F, 1.0F),
                PartPose.offset(-1.0F, -8.0F, 5.0F));
        root.addOrReplaceChild("Shape46",
                CubeListBuilder.create().texOffs(40, 170).addBox(-4.0F, 0.0F, -2.0F, 4.0F, 1.0F, 1.0F),
                PartPose.offset(-1.0F, -10.0F, 4.0F));
        root.addOrReplaceChild("Shape47",
                CubeListBuilder.create().texOffs(40, 161).addBox(-4.0F, 0.0F, -2.0F, 4.0F, 1.0F, 1.0F),
                PartPose.offset(-1.0F, -12.0F, 4.0F));
        root.addOrReplaceChild("Shape48",
                CubeListBuilder.create().texOffs(40, 151).addBox(-4.0F, 0.0F, -2.0F, 4.0F, 1.0F, 1.0F),
                PartPose.offset(-1.0F, -14.0F, 4.0F));
        root.addOrReplaceChild("Shape49",
                CubeListBuilder.create().texOffs(40, 140).addBox(0.0F, 0.0F, 0.0F, 3.0F, 2.0F, 3.0F),
                PartPose.offsetAndRotation(-7.5F, -15.5F, 3.0F, 0.0F, 0.0F, 0.524F));
        root.addOrReplaceChild("rarm2",
                CubeListBuilder.create().texOffs(40, 122).addBox(-4.0F, 0.0F, -2.0F, 1.0F, 12.0F, 1.0F),
                PartPose.offsetAndRotation(-5.0F, -11.5F, 8.0F, 0.0F, -0.5F, 0.524F));
        root.addOrReplaceChild("rarm3",
                CubeListBuilder.create().texOffs(49, 122).addBox(-4.0F, 0.0F, -2.0F, 1.0F, 12.0F, 1.0F),
                PartPose.offsetAndRotation(-4.0F, -11.5F, 6.0F, 0.0F, -0.5F, 0.524F));
        root.addOrReplaceChild("relbow",
                CubeListBuilder.create().texOffs(40, 111).addBox(0.0F, 0.0F, 0.0F, 2.0F, 2.0F, 3.0F),
                PartPose.offsetAndRotation(-11.0F, -3.5F, 3.0F, 0.0F, -0.5F, 0.524F));
        root.addOrReplaceChild("rarm1",
                CubeListBuilder.create().texOffs(40, 91).addBox(-2.0F, -1.0F, -1.0F, 1.0F, 11.0F, 2.0F),
                PartPose.offsetAndRotation(-10.5F, -2.0F, 2.5F, -0.76F, 0.0F, 0.3F));
        root.addOrReplaceChild("Shape54",
                CubeListBuilder.create().texOffs(40, 78).addBox(0.0F, 0.0F, 0.0F, 3.0F, 2.0F, 3.0F),
                PartPose.offsetAndRotation(5.0F, -14.0F, 3.0F, 0.0F, 0.0F, -0.524F));
        root.addOrReplaceChild("larm3",
                CubeListBuilder.create().texOffs(40, 58).addBox(-4.0F, 0.0F, -2.0F, 1.0F, 12.0F, 1.0F),
                PartPose.offsetAndRotation(9.5F, -15.0F, 3.0F, 0.0F, 0.5F, -0.524F));
        root.addOrReplaceChild("larm2",
                CubeListBuilder.create().texOffs(40, 35).addBox(-4.0F, 0.0F, -2.0F, 1.0F, 12.0F, 1.0F),
                PartPose.offsetAndRotation(10.5F, -15.0F, 5.0F, 0.0F, 0.5F, -0.524F));
        root.addOrReplaceChild("lelbow",
                CubeListBuilder.create().texOffs(55, 38).addBox(0.0F, 0.0F, 0.0F, 2.0F, 2.0F, 4.0F),
                PartPose.offsetAndRotation(10.0F, -3.0F, 3.0F, 0.0F, 0.5F, -0.524F));
        root.addOrReplaceChild("larm1",
                CubeListBuilder.create().texOffs(56, 53).addBox(0.0F, 0.0F, -1.0F, 1.0F, 9.0F, 2.0F),
                PartPose.offsetAndRotation(12.0F, -3.0F, 2.5F, 0.0F, -0.6F, -0.3F));
        root.addOrReplaceChild("scythe1",
                CubeListBuilder.create().texOffs(57, 70).addBox(0.0F, -39.0F, 1.0F, 1.0F, 39.0F, 1.0F),
                PartPose.offsetAndRotation(-17.0F, 6.0F, -2.0F, 0.0F, 0.0F, 1.0F));
        root.addOrReplaceChild("scythe2",
                CubeListBuilder.create().texOffs(58, 118).addBox(0.0F, -39.0F, 1.0F, 16.0F, 6.0F, 0.0F),
                PartPose.offsetAndRotation(-17.0F, 6.0F, -2.0F, 0.0F, 0.0F, 1.0F));
        root.addOrReplaceChild("scythe3",
                CubeListBuilder.create().texOffs(61, 133).addBox(9.0F, -34.0F, 1.0F, 7.0F, 5.0F, 0.0F),
                PartPose.offsetAndRotation(-17.0F, 6.0F, -2.0F, 0.0F, 0.0F, 1.0F));
        root.addOrReplaceChild("head",
                CubeListBuilder.create().texOffs(58, 145).addBox(-3.0F, -6.0F, -3.0F, 6.0F, 6.0F, 5.0F),
                PartPose.offset(0.0F, -16.0F, 4.0F));
        root.addOrReplaceChild("lwing3",
                CubeListBuilder.create().texOffs(71, 58).addBox(-0.5F, 0.0F, 0.0F, 1.0F, 19.0F, 3.0F),
                PartPose.offsetAndRotation(4.0F, -11.7F, 8.5F, 2.356F, 0.785F, 0.0F));
        root.addOrReplaceChild("lwing2",
                CubeListBuilder.create().texOffs(58, 168).addBox(-0.5F, 11.0F, -2.0F, 1.0F, 19.0F, 3.0F),
                PartPose.offsetAndRotation(4.0F, -23.9F, 8.5F, 1.745F, 0.785F, 0.0F));
        root.addOrReplaceChild("rwing3",
                CubeListBuilder.create().texOffs(71, 88).addBox(-0.5F, 0.0F, 0.0F, 1.0F, 19.0F, 3.0F),
                PartPose.offsetAndRotation(-4.0F, -11.7F, 8.5F, 2.356F, -0.785F, 0.0F));
        root.addOrReplaceChild("rwing2",
                CubeListBuilder.create().texOffs(73, 168).addBox(-0.5F, 12.0F, -2.0F, 1.0F, 19.0F, 3.0F),
                PartPose.offsetAndRotation(-4.0F, -23.9F, 8.5F, 1.745F, -0.785F, 0.0F));

        return LayerDefinition.create(mesh, 512, 512);
    }

    @Override
    public ModelPart root() {
        return this.root;
    }

    @Override
    public void setupAnim(
            EnderReaper entity,
            float limbSwing,
            float limbSwingAmount,
            float ageInTicks,
            float netHeadYaw,
            float headPitch) {
        float newangle;
        if (limbSwingAmount > 0.1F) {
            newangle = Mth.cos(ageInTicks * 1.3F * this.wingspeed) * (float) Math.PI * 0.25F * limbSwingAmount;
        } else {
            newangle = 0.0F;
        }

        // gold: scythe zRot = 1.0 - abs(walkAngle); overwritten when screaming
        this.scythe3.zRot = this.scythe2.zRot = this.scythe1.zRot = 1.0F - Math.abs(newangle);

        if (entity.isScreaming()) {
            newangle = Mth.cos(ageInTicks * 1.9F * this.wingspeed) * (float) Math.PI * 0.25F;
            this.scythe3.zRot = this.scythe2.zRot = this.scythe1.zRot = 1.0F + newangle;
            this.larm1.xRot = -0.436F;
            this.larm1.yRot = -0.488F;
            // wing flap rate while screaming
            newangle = Mth.cos(ageInTicks * 2.7F * this.wingspeed) * (float) Math.PI * 0.3F;
        } else {
            this.larm1.xRot = -2.436F;
            this.larm1.yRot = 1.0F;
            // idle wing sway
            newangle = Mth.cos(ageInTicks * 0.7F * this.wingspeed) * (float) Math.PI * 0.06F;
        }

        this.lwing1.yRot = this.lwing2.yRot = this.lwing3.yRot = 0.785F + newangle;
        this.rwing1.yRot = this.rwing2.yRot = this.rwing3.yRot = -0.785F - newangle;

        this.head.yRot = (float) Math.toRadians(netHeadYaw) * 0.45F;
        if (this.head.yRot > 0.45F) {
            this.head.yRot = 0.45F;
        }
        if (this.head.yRot < -0.45F) {
            this.head.yRot = -0.45F;
        }
    }
}
