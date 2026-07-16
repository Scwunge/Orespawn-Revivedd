package danger.orespawn.client.model;

import danger.orespawn.entity.Kraken;
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
 * Port of gold {@code ModelKraken} (1.7.10 ModelBase, tex 512×512, 111 cubes) → HierarchicalModel.
 * Full gold {@code func_78088_a} animation (fins, 6 tentacle chains via dangle_tentacle, mouth/teeth, suction cups).
 * Gold ctor baked: all pivots z+=90, y+=30. Gold render GL11 rotate 90° about X is applied in KrakenRenderer.
 * Local LAYER_LOCATION; wingspeed default 1.0F matches ClientProxy.
 */
@OnlyIn(Dist.CLIENT)
public class ModelKraken extends HierarchicalModel<Kraken> {
    /** Model layer location. */
    public static final ModelLayerLocation LAYER_LOCATION =
            new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "kraken"), "main");

    private final float wingspeed;
    private final ModelPart root;
    private final ModelPart Lefteye;
    private final ModelPart Backbody;
    private final ModelPart Centerbody;
    private final ModelPart Head;
    private final ModelPart Sucktioncupleft;
    private final ModelPart Finright;
    private final ModelPart Tailbase1;
    private final ModelPart Tail2;
    private final ModelPart Tailtip;
    private final ModelPart Finleft;
    private final ModelPart Frontbody;
    private final ModelPart Mouth1;
    private final ModelPart Tent54;
    private final ModelPart Tent62;
    private final ModelPart Tent63;
    private final ModelPart Tent64;
    private final ModelPart Tent58;
    private final ModelPart Tent66;
    private final ModelPart Tent67;
    private final ModelPart Tent68;
    private final ModelPart Tent28;
    private final ModelPart Tent51;
    private final ModelPart Tent52;
    private final ModelPart Tent53;
    private final ModelPart Tent65;
    private final ModelPart Tent55;
    private final ModelPart Tent56;
    private final ModelPart Tent57;
    private final ModelPart Sucktioncupright;
    private final ModelPart Righteye;
    private final ModelPart Mouth2;
    private final ModelPart Mouth3;
    private final ModelPart Mouth4;
    private final ModelPart Mouth5;
    private final ModelPart Mouth6;
    private final ModelPart Mouth7;
    private final ModelPart Mouth8;
    private final ModelPart Tent61;
    private final ModelPart Tent38;
    private final ModelPart Tent22;
    private final ModelPart Tent23;
    private final ModelPart Tent24;
    private final ModelPart Tent25;
    private final ModelPart Tent26;
    private final ModelPart Tent27;
    private final ModelPart Tooth1;
    private final ModelPart Tent48;
    private final ModelPart Tent32;
    private final ModelPart Tent33;
    private final ModelPart Tent34;
    private final ModelPart Tent35;
    private final ModelPart Tent36;
    private final ModelPart Tent37;
    private final ModelPart Jet;
    private final ModelPart Tent41;
    private final ModelPart Tent42;
    private final ModelPart Tent43;
    private final ModelPart Tent44;
    private final ModelPart Tent45;
    private final ModelPart Tent46;
    private final ModelPart Tent47;
    private final ModelPart Tent21;
    private final ModelPart Tent11;
    private final ModelPart Tent12;
    private final ModelPart Tent13;
    private final ModelPart Tent14;
    private final ModelPart Tent15;
    private final ModelPart Tent16;
    private final ModelPart Tent31;
    private final ModelPart Tent18;
    private final ModelPart Tooth2;
    private final ModelPart Tooth3;
    private final ModelPart Tooth4;
    private final ModelPart Tooth5;
    private final ModelPart Tooth6;
    private final ModelPart Tooth7;
    private final ModelPart Tooth8;
    private final ModelPart Tooth9;
    private final ModelPart Tooth10;
    private final ModelPart Tooth11;
    private final ModelPart Tooth12;
    private final ModelPart Tooth13;
    private final ModelPart Tooth14;
    private final ModelPart Tooth15;
    private final ModelPart Tooth16;
    private final ModelPart Tooth17;
    private final ModelPart Tooth18;
    private final ModelPart Tooth19;
    private final ModelPart Tooth20;
    private final ModelPart Tooth21;
    private final ModelPart Tooth22;
    private final ModelPart Tooth23;
    private final ModelPart Tooth24;
    private final ModelPart Tooth25;
    private final ModelPart Tooth26;
    private final ModelPart Tooth27;
    private final ModelPart Tooth28;
    private final ModelPart Tooth29;
    private final ModelPart Tooth30;
    private final ModelPart Tooth31;
    private final ModelPart Tooth32;
    private final ModelPart Tooth33;
    private final ModelPart Tooth34;
    private final ModelPart Tooth35;
    private final ModelPart Tooth36;
    private final ModelPart Tooth37;
    private final ModelPart Tooth38;
    private final ModelPart Tooth39;
    private final ModelPart Tooth40;
    private final ModelPart Tooth41;
    private final ModelPart Tent17;

    public ModelKraken(ModelPart root) {
        this(root, 1.0F);
    }

    public ModelKraken(ModelPart root, float wingspeed) {
        this.root = root;
        this.wingspeed = wingspeed;
        this.Lefteye = root.getChild("Lefteye");
        this.Backbody = root.getChild("Backbody");
        this.Centerbody = root.getChild("Centerbody");
        this.Head = root.getChild("Head");
        this.Sucktioncupleft = root.getChild("Sucktioncupleft");
        this.Finright = root.getChild("Finright");
        this.Tailbase1 = root.getChild("Tailbase1");
        this.Tail2 = root.getChild("Tail2");
        this.Tailtip = root.getChild("Tailtip");
        this.Finleft = root.getChild("Finleft");
        this.Frontbody = root.getChild("Frontbody");
        this.Mouth1 = root.getChild("Mouth1");
        this.Tent54 = root.getChild("Tent54");
        this.Tent62 = root.getChild("Tent62");
        this.Tent63 = root.getChild("Tent63");
        this.Tent64 = root.getChild("Tent64");
        this.Tent58 = root.getChild("Tent58");
        this.Tent66 = root.getChild("Tent66");
        this.Tent67 = root.getChild("Tent67");
        this.Tent68 = root.getChild("Tent68");
        this.Tent28 = root.getChild("Tent28");
        this.Tent51 = root.getChild("Tent51");
        this.Tent52 = root.getChild("Tent52");
        this.Tent53 = root.getChild("Tent53");
        this.Tent65 = root.getChild("Tent65");
        this.Tent55 = root.getChild("Tent55");
        this.Tent56 = root.getChild("Tent56");
        this.Tent57 = root.getChild("Tent57");
        this.Sucktioncupright = root.getChild("Sucktioncupright");
        this.Righteye = root.getChild("Righteye");
        this.Mouth2 = root.getChild("Mouth2");
        this.Mouth3 = root.getChild("Mouth3");
        this.Mouth4 = root.getChild("Mouth4");
        this.Mouth5 = root.getChild("Mouth5");
        this.Mouth6 = root.getChild("Mouth6");
        this.Mouth7 = root.getChild("Mouth7");
        this.Mouth8 = root.getChild("Mouth8");
        this.Tent61 = root.getChild("Tent61");
        this.Tent38 = root.getChild("Tent38");
        this.Tent22 = root.getChild("Tent22");
        this.Tent23 = root.getChild("Tent23");
        this.Tent24 = root.getChild("Tent24");
        this.Tent25 = root.getChild("Tent25");
        this.Tent26 = root.getChild("Tent26");
        this.Tent27 = root.getChild("Tent27");
        this.Tooth1 = root.getChild("Tooth1");
        this.Tent48 = root.getChild("Tent48");
        this.Tent32 = root.getChild("Tent32");
        this.Tent33 = root.getChild("Tent33");
        this.Tent34 = root.getChild("Tent34");
        this.Tent35 = root.getChild("Tent35");
        this.Tent36 = root.getChild("Tent36");
        this.Tent37 = root.getChild("Tent37");
        this.Jet = root.getChild("Jet");
        this.Tent41 = root.getChild("Tent41");
        this.Tent42 = root.getChild("Tent42");
        this.Tent43 = root.getChild("Tent43");
        this.Tent44 = root.getChild("Tent44");
        this.Tent45 = root.getChild("Tent45");
        this.Tent46 = root.getChild("Tent46");
        this.Tent47 = root.getChild("Tent47");
        this.Tent21 = root.getChild("Tent21");
        this.Tent11 = root.getChild("Tent11");
        this.Tent12 = root.getChild("Tent12");
        this.Tent13 = root.getChild("Tent13");
        this.Tent14 = root.getChild("Tent14");
        this.Tent15 = root.getChild("Tent15");
        this.Tent16 = root.getChild("Tent16");
        this.Tent31 = root.getChild("Tent31");
        this.Tent18 = root.getChild("Tent18");
        this.Tooth2 = root.getChild("Tooth2");
        this.Tooth3 = root.getChild("Tooth3");
        this.Tooth4 = root.getChild("Tooth4");
        this.Tooth5 = root.getChild("Tooth5");
        this.Tooth6 = root.getChild("Tooth6");
        this.Tooth7 = root.getChild("Tooth7");
        this.Tooth8 = root.getChild("Tooth8");
        this.Tooth9 = root.getChild("Tooth9");
        this.Tooth10 = root.getChild("Tooth10");
        this.Tooth11 = root.getChild("Tooth11");
        this.Tooth12 = root.getChild("Tooth12");
        this.Tooth13 = root.getChild("Tooth13");
        this.Tooth14 = root.getChild("Tooth14");
        this.Tooth15 = root.getChild("Tooth15");
        this.Tooth16 = root.getChild("Tooth16");
        this.Tooth17 = root.getChild("Tooth17");
        this.Tooth18 = root.getChild("Tooth18");
        this.Tooth19 = root.getChild("Tooth19");
        this.Tooth20 = root.getChild("Tooth20");
        this.Tooth21 = root.getChild("Tooth21");
        this.Tooth22 = root.getChild("Tooth22");
        this.Tooth23 = root.getChild("Tooth23");
        this.Tooth24 = root.getChild("Tooth24");
        this.Tooth25 = root.getChild("Tooth25");
        this.Tooth26 = root.getChild("Tooth26");
        this.Tooth27 = root.getChild("Tooth27");
        this.Tooth28 = root.getChild("Tooth28");
        this.Tooth29 = root.getChild("Tooth29");
        this.Tooth30 = root.getChild("Tooth30");
        this.Tooth31 = root.getChild("Tooth31");
        this.Tooth32 = root.getChild("Tooth32");
        this.Tooth33 = root.getChild("Tooth33");
        this.Tooth34 = root.getChild("Tooth34");
        this.Tooth35 = root.getChild("Tooth35");
        this.Tooth36 = root.getChild("Tooth36");
        this.Tooth37 = root.getChild("Tooth37");
        this.Tooth38 = root.getChild("Tooth38");
        this.Tooth39 = root.getChild("Tooth39");
        this.Tooth40 = root.getChild("Tooth40");
        this.Tooth41 = root.getChild("Tooth41");
        this.Tent17 = root.getChild("Tent17");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        root.addOrReplaceChild(
                "Lefteye",
                CubeListBuilder.create().texOffs(56, 458).addBox(0.0F, -8.0F, -12.0F, 4.0F, 16.0F, 24.0F),
                PartPose.offset(20.0F, 6.0F, 0.0F));
        root.addOrReplaceChild(
                "Backbody",
                CubeListBuilder.create().texOffs(320, 272).addBox(-24.0F, -24.0F, 0.0F, 48.0F, 48.0F, 48.0F),
                PartPose.offset(0.0F, 6.0F, 118.0F));
        root.addOrReplaceChild(
                "Centerbody",
                CubeListBuilder.create().texOffs(320, 176).addBox(-24.0F, -24.0F, -24.0F, 48.0F, 48.0F, 48.0F),
                PartPose.offset(0.0F, 6.0F, 94.0F));
        root.addOrReplaceChild(
                "Head",
                CubeListBuilder.create().texOffs(336, 0).addBox(-20.0F, -20.0F, -40.0F, 40.0F, 40.0F, 40.0F),
                PartPose.offset(0.0F, 6.0F, 22.0F));
        root.addOrReplaceChild(
                "Sucktioncupleft",
                CubeListBuilder.create().texOffs(80, 84).addBox(-8.0F, -4.0F, -32.0F, 16.0F, 8.0F, 32.0F),
                PartPose.offsetAndRotation(32.0F, 4.0F, -246.0F, 0.3490659F, (float) (Math.PI / 12), 0.0F));
        root.addOrReplaceChild(
                "Finright",
                CubeListBuilder.create().texOffs(0, 329).addBox(-40.0F, -8.0F, -32.0F, 40.0F, 12.0F, 104.0F),
                PartPose.offset(-12.0F, 9.0F, 173.0F));
        root.addOrReplaceChild(
                "Tailbase1",
                CubeListBuilder.create().texOffs(368, 368).addBox(-20.0F, -20.0F, 0.0F, 40.0F, 40.0F, 32.0F),
                PartPose.offset(0.0F, 6.0F, 165.0F));
        root.addOrReplaceChild(
                "Tail2",
                CubeListBuilder.create().texOffs(384, 440).addBox(-16.0F, -16.0F, 0.0F, 32.0F, 32.0F, 32.0F),
                PartPose.offset(0.0F, 6.0F, 197.0F));
        root.addOrReplaceChild(
                "Tailtip",
                CubeListBuilder.create().texOffs(272, 457).addBox(-12.0F, -12.0F, 0.0F, 24.0F, 24.0F, 32.0F),
                PartPose.offset(0.0F, 6.0F, 229.0F));
        root.addOrReplaceChild(
                "Finleft",
                CubeListBuilder.create().texOffs(0, 201).addBox(0.0F, -8.0F, -32.0F, 40.0F, 12.0F, 104.0F),
                PartPose.offset(12.0F, 9.0F, 173.0F));
        root.addOrReplaceChild(
                "Frontbody",
                CubeListBuilder.create().texOffs(320, 80).addBox(-20.0F, -20.0F, -47.0F, 48.0F, 48.0F, 48.0F),
                PartPose.offset(-4.0F, 2.0F, 69.0F));
        root.addOrReplaceChild(
                "Mouth1",
                CubeListBuilder.create().texOffs(232, 160).addBox(-2.0F, -8.0F, -24.0F, 4.0F, 16.0F, 24.0F),
                PartPose.offsetAndRotation(11.0F, 2.0F, -14.0F, -0.3839724F, -0.3839724F, (float) (-Math.PI / 4)));
        root.addOrReplaceChild(
                "Tent54",
                CubeListBuilder.create().texOffs(80, 161).addBox(-4.0F, -4.0F, -32.0F, 8.0F, 8.0F, 32.0F),
                PartPose.offsetAndRotation(37.0F, -21.0F, -101.0F, 0.0872665F, -0.0872665F, 0.0F));
        root.addOrReplaceChild(
                "Tent62",
                CubeListBuilder.create().texOffs(80, 161).addBox(-4.0F, -4.0F, -32.0F, 8.0F, 8.0F, 32.0F),
                PartPose.offsetAndRotation(-21.0F, -16.0F, -43.0F, (float) (-Math.PI / 12), 0.3490659F, 0.0F));
        root.addOrReplaceChild(
                "Tent63",
                CubeListBuilder.create().texOffs(80, 161).addBox(-4.0F, -4.0F, -32.0F, 8.0F, 8.0F, 32.0F),
                PartPose.offsetAndRotation(-31.0F, -24.0F, -69.5333F, -0.0872665F, 0.1745329F, 0.0F));
        root.addOrReplaceChild(
                "Tent64",
                CubeListBuilder.create().texOffs(80, 161).addBox(-4.0F, -4.0F, -32.0F, 8.0F, 8.0F, 32.0F),
                PartPose.offsetAndRotation(-36.0F, -27.0F, -98.0F, 0.0872665F, -0.0872665F, 0.0F));
        root.addOrReplaceChild(
                "Tent58",
                CubeListBuilder.create().texOffs(80, 124).addBox(-2.0F, -2.0F, -32.0F, 4.0F, 4.0F, 32.0F),
                PartPose.offsetAndRotation(40.0F, -6.0F, -218.0F, 0.3490659F, (float) (Math.PI / 12), 0.0F));
        root.addOrReplaceChild(
                "Tent66",
                CubeListBuilder.create().texOffs(80, 124).addBox(-2.0F, -2.0F, -32.0F, 4.0F, 4.0F, 32.0F),
                PartPose.offsetAndRotation(-31.0F, -21.0F, -160.0F, 0.0872665F, -0.0872665F, 0.0F));
        root.addOrReplaceChild(
                "Tent67",
                CubeListBuilder.create().texOffs(80, 124).addBox(-2.0F, -2.0F, -32.0F, 4.0F, 4.0F, 32.0F),
                PartPose.offsetAndRotation(-28.0F, -18.0F, -191.0F, (float) (Math.PI / 12), -0.1745329F, 0.0F));
        root.addOrReplaceChild(
                "Tent68",
                CubeListBuilder.create().texOffs(80, 124).addBox(-2.0F, -2.0F, -32.0F, 4.0F, 4.0F, 32.0F),
                PartPose.offsetAndRotation(-23.0F, -10.0F, -219.0F, 0.3490659F, (float) (-Math.PI / 12), 0.0F));
        root.addOrReplaceChild(
                "Tent28",
                CubeListBuilder.create().texOffs(0, 57).addBox(-1.0F, -1.0F, -32.0F, 1.0F, 1.0F, 32.0F),
                PartPose.offsetAndRotation(-17.0F, 80.0F, -217.0F, 0.0872665F, -0.1745329F, 0.0F));
        root.addOrReplaceChild(
                "Tent51",
                CubeListBuilder.create().texOffs(80, 161).addBox(-4.0F, -4.0F, -32.0F, 8.0F, 8.0F, 32.0F),
                PartPose.offsetAndRotation(14.0F, -8.0F, -15.0F, (float) (-Math.PI / 12), (float) (-Math.PI / 12), 0.0F));
        root.addOrReplaceChild(
                "Tent52",
                CubeListBuilder.create().texOffs(80, 161).addBox(-4.0F, -4.0F, -32.0F, 8.0F, 8.0F, 32.0F),
                PartPose.offsetAndRotation(22.0F, -16.0F, -44.0F, (float) (-Math.PI / 12), -0.3490659F, -0.0523599F));
        root.addOrReplaceChild(
                "Tent53",
                CubeListBuilder.create().texOffs(80, 161).addBox(-4.0F, -4.0F, -32.0F, 8.0F, 8.0F, 32.0F),
                PartPose.offsetAndRotation(32.0F, -24.0F, -71.0F, 0.0872665F, -0.1745329F, 0.0F));
        root.addOrReplaceChild(
                "Tent65",
                CubeListBuilder.create().texOffs(80, 124).addBox(-2.0F, -2.0F, -32.0F, 4.0F, 4.0F, 32.0F),
                PartPose.offsetAndRotation(-34.0F, -24.0F, -129.0F, 0.0872665F, -0.0872665F, 0.0F));
        root.addOrReplaceChild(
                "Tent55",
                CubeListBuilder.create().texOffs(80, 124).addBox(-2.0F, -2.0F, -32.0F, 4.0F, 4.0F, 32.0F),
                PartPose.offsetAndRotation(40.0F, -19.0F, -129.0F, 0.0872665F, -0.0872665F, 0.0F));
        root.addOrReplaceChild(
                "Tent56",
                CubeListBuilder.create().texOffs(80, 124).addBox(-2.0F, -2.0F, -32.0F, 4.0F, 4.0F, 32.0F),
                PartPose.offsetAndRotation(42.0F, -16.0F, -159.0F, 0.0872665F, -0.0872665F, 0.0F));
        root.addOrReplaceChild(
                "Tent57",
                CubeListBuilder.create().texOffs(80, 124).addBox(-2.0F, -2.0F, -32.0F, 4.0F, 4.0F, 32.0F),
                PartPose.offsetAndRotation(45.0F, -14.0F, -189.0F, (float) (Math.PI / 12), 0.1745329F, 0.0F));
        root.addOrReplaceChild(
                "Sucktioncupright",
                CubeListBuilder.create().texOffs(80, 84).addBox(-8.0F, -4.0F, -32.0F, 16.0F, 8.0F, 32.0F),
                PartPose.offsetAndRotation(-14.0F, 0.0F, -246.0F, 0.3490659F, (float) (-Math.PI / 12), 0.0F));
        root.addOrReplaceChild(
                "Righteye",
                CubeListBuilder.create().texOffs(0, 458).addBox(-4.0F, -8.0F, -12.0F, 4.0F, 16.0F, 24.0F),
                PartPose.offset(-20.0F, 6.0F, 0.0F));
        root.addOrReplaceChild(
                "Mouth2",
                CubeListBuilder.create().texOffs(232, 92).addBox(-2.0F, -8.0F, -24.0F, 4.0F, 16.0F, 24.0F),
                PartPose.offsetAndRotation(-12.0F, 2.0F, -14.0F, -0.3839724F, 0.3839724F, (float) (Math.PI / 4)));
        root.addOrReplaceChild(
                "Mouth3",
                CubeListBuilder.create().texOffs(232, 12).addBox(-2.0F, -8.0F, -24.0F, 4.0F, 16.0F, 24.0F),
                PartPose.offsetAndRotation(-10.0F, 18.0F, -14.0F, 0.3839724F, 0.3839724F, -0.8004762F));
        root.addOrReplaceChild(
                "Mouth4",
                CubeListBuilder.create().texOffs(288, 427).addBox(-8.0F, -2.0F, -24.0F, 16.0F, 4.0F, 24.0F),
                PartPose.offsetAndRotation(0.0F, 21.0F, -14.0F, 0.3839724F, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "Mouth5",
                CubeListBuilder.create().texOffs(288, 387).addBox(-2.0F, -8.0F, -24.0F, 4.0F, 16.0F, 24.0F),
                PartPose.offsetAndRotation(11.0F, 18.0F, -14.0F, 0.3839724F, -0.3839724F, (float) (Math.PI / 4)));
        root.addOrReplaceChild(
                "Mouth6",
                CubeListBuilder.create().texOffs(175, 160).addBox(-2.0F, -8.0F, -24.0F, 4.0F, 16.0F, 24.0F),
                PartPose.offsetAndRotation(10.0F, 9.0F, -14.0F, 0.0F, -0.3839724F, 0.0F));
        root.addOrReplaceChild(
                "Mouth7",
                CubeListBuilder.create().texOffs(232, 52).addBox(-2.0F, -8.0F, -24.0F, 4.0F, 16.0F, 24.0F),
                PartPose.offsetAndRotation(-10.0F, 9.0F, -14.0F, 0.0F, 0.3839724F, 0.0F));
        root.addOrReplaceChild(
                "Mouth8",
                CubeListBuilder.create().texOffs(232, 132).addBox(-8.0F, -2.0F, -24.0F, 16.0F, 4.0F, 24.0F),
                PartPose.offsetAndRotation(0.0F, -1.0F, -14.0F, -0.3839724F, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "Tent61",
                CubeListBuilder.create().texOffs(80, 161).addBox(-4.0F, -4.0F, -32.0F, 8.0F, 8.0F, 32.0F),
                PartPose.offsetAndRotation(-14.0F, -8.0F, -15.0F, (float) (-Math.PI / 12), (float) (Math.PI / 12), 0.0F));
        root.addOrReplaceChild(
                "Tent38",
                CubeListBuilder.create().texOffs(0, 57).addBox(0.0F, 0.0F, -32.0F, 1.0F, 1.0F, 32.0F),
                PartPose.offset(57.0F, 69.0F, -217.0F));
        root.addOrReplaceChild(
                "Tent22",
                CubeListBuilder.create().texOffs(0, 162).addBox(-4.0F, -4.0F, -32.0F, 7.0F, 7.0F, 32.0F),
                PartPose.offsetAndRotation(-21.0F, 30.0F, -43.0F, (float) (Math.PI / 12), 0.1745329F, 0.0F));
        root.addOrReplaceChild(
                "Tent23",
                CubeListBuilder.create().texOffs(0, 162).addBox(-4.0F, -4.0F, -32.0F, 7.0F, 7.0F, 32.0F),
                PartPose.offsetAndRotation(-26.0F, 38.0F, -71.0F, 0.3490659F, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "Tent24",
                CubeListBuilder.create().texOffs(0, 125).addBox(-3.0F, -3.0F, -32.0F, 5.0F, 5.0F, 32.0F),
                PartPose.offsetAndRotation(-26.0F, 49.0F, -101.0F, 0.3490659F, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "Tent25",
                CubeListBuilder.create().texOffs(0, 125).addBox(-3.0F, -3.0F, -32.0F, 5.0F, 5.0F, 32.0F),
                PartPose.offsetAndRotation(-26.0F, 59.0F, -129.0F, (float) (Math.PI / 12), -0.0872665F, 0.0F));
        root.addOrReplaceChild(
                "Tent26",
                CubeListBuilder.create().texOffs(0, 90).addBox(-2.0F, -2.0F, -32.0F, 3.0F, 3.0F, 32.0F),
                PartPose.offsetAndRotation(-24.0F, 67.0F, -158.0F, (float) (Math.PI / 12), -0.0872665F, 0.0F));
        root.addOrReplaceChild(
                "Tent27",
                CubeListBuilder.create().texOffs(0, 90).addBox(-2.0F, -2.0F, -32.0F, 3.0F, 3.0F, 32.0F),
                PartPose.offsetAndRotation(-22.0F, 75.0F, -187.0F, 0.1745329F, -0.1745329F, 0.0F));
        root.addOrReplaceChild(
                "Tooth1",
                CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, 0.0F, -8.0F, 1.0F, 1.0F, 8.0F),
                PartPose.offsetAndRotation(-7.0F, 23.0F, -29.0F, -0.1745329F, -0.3490659F, 0.0F));
        root.addOrReplaceChild(
                "Tent48",
                CubeListBuilder.create().texOffs(0, 57).addBox(0.0F, 0.0F, -32.0F, 1.0F, 1.0F, 32.0F),
                PartPose.offsetAndRotation(82.0F, 25.0F, -212.0F, 0.0872665F, (float) (Math.PI / 12), 0.0F));
        root.addOrReplaceChild(
                "Tent32",
                CubeListBuilder.create().texOffs(0, 162).addBox(-4.0F, -4.0F, -32.0F, 7.0F, 7.0F, 32.0F),
                PartPose.offsetAndRotation(24.0F, 30.0F, -44.0F, (float) (Math.PI / 12), -0.3490659F, 0.0F));
        root.addOrReplaceChild(
                "Tent33",
                CubeListBuilder.create().texOffs(0, 161).addBox(-4.0F, -4.0F, -32.0F, 7.0F, 7.0F, 32.0F),
                PartPose.offsetAndRotation(34.0F, 38.0F, -72.0F, 0.3490659F, (float) (-Math.PI / 12), 0.0F));
        root.addOrReplaceChild(
                "Tent34",
                CubeListBuilder.create().texOffs(0, 125).addBox(-2.0F, -2.0F, -32.0F, 5.0F, 5.0F, 32.0F),
                PartPose.offsetAndRotation(41.0F, 48.0F, -100.0F, (float) (Math.PI / 12), (float) (-Math.PI / 12), 0.0F));
        root.addOrReplaceChild(
                "Tent35",
                CubeListBuilder.create().texOffs(0, 125).addBox(-2.0F, -2.0F, -32.0F, 5.0F, 5.0F, 32.0F),
                PartPose.offsetAndRotation(49.0F, 56.0F, -128.0F, 0.1745329F, -0.1745329F, 0.0F));
        root.addOrReplaceChild(
                "Tent36",
                CubeListBuilder.create().texOffs(0, 90).addBox(-1.0F, -1.0F, -32.0F, 3.0F, 3.0F, 32.0F),
                PartPose.offsetAndRotation(54.0F, 61.0F, -157.0F, 0.1745329F, -0.0872665F, 0.0F));
        root.addOrReplaceChild(
                "Tent37",
                CubeListBuilder.create().texOffs(0, 90).addBox(-1.0F, -1.0F, -32.0F, 3.0F, 3.0F, 32.0F),
                PartPose.offsetAndRotation(57.0F, 66.0F, -186.0F, 0.0872665F, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "Jet",
                CubeListBuilder.create().texOffs(80, 42).addBox(-5.0F, -5.0F, -32.0F, 10.0F, 10.0F, 32.0F),
                PartPose.offsetAndRotation(0.0F, 23.0F, 26.0F, 0.3490659F, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "Tent41",
                CubeListBuilder.create().texOffs(0, 162).addBox(-4.0F, -4.0F, -32.0F, 7.0F, 7.0F, 32.0F),
                PartPose.offsetAndRotation(16.0F, 7.0F, -15.0F, 0.0872665F, -0.4363323F, 0.0F));
        root.addOrReplaceChild(
                "Tent42",
                CubeListBuilder.create().texOffs(0, 162).addBox(-4.0F, -4.0F, -32.0F, 7.0F, 7.0F, 32.0F),
                PartPose.offsetAndRotation(28.0F, 9.0F, -42.0F, 0.0872665F, (float) (-Math.PI / 6), 0.0F));
        root.addOrReplaceChild(
                "Tent43",
                CubeListBuilder.create().texOffs(0, 162).addBox(-4.0F, -4.0F, -32.0F, 7.0F, 7.0F, 32.0F),
                PartPose.offsetAndRotation(43.0F, 12.0F, -69.0F, 0.0872665F, -0.6108652F, 0.0F));
        root.addOrReplaceChild(
                "Tent44",
                CubeListBuilder.create().texOffs(0, 125).addBox(-2.0F, -2.0F, -32.0F, 5.0F, 5.0F, 32.0F),
                PartPose.offsetAndRotation(60.0F, 14.0F, -95.0F, 0.0872665F, -0.4363323F, 0.0F));
        root.addOrReplaceChild(
                "Tent45",
                CubeListBuilder.create().texOffs(0, 125).addBox(-2.0F, -2.0F, -32.0F, 5.0F, 5.0F, 32.0F),
                PartPose.offsetAndRotation(73.0F, 17.0F, -123.0F, 0.0872665F, (float) (-Math.PI / 12), 0.0F));
        root.addOrReplaceChild(
                "Tent46",
                CubeListBuilder.create().texOffs(0, 90).addBox(-1.0F, -1.0F, -32.0F, 3.0F, 3.0F, 32.0F),
                PartPose.offsetAndRotation(81.0F, 20.0F, -152.0F, 0.0872665F, -0.0872665F, 0.0F));
        root.addOrReplaceChild(
                "Tent47",
                CubeListBuilder.create().texOffs(0, 90).addBox(-1.0F, -1.0F, -32.0F, 3.0F, 3.0F, 32.0F),
                PartPose.offsetAndRotation(84.0F, 23.0F, -182.0F, 0.0872665F, 0.0872665F, 0.0F));
        root.addOrReplaceChild(
                "Tent21",
                CubeListBuilder.create().texOffs(0, 162).addBox(-4.0F, -4.0F, -32.0F, 7.0F, 7.0F, 32.0F),
                PartPose.offsetAndRotation(-14.0F, 22.0F, -15.0F, (float) (Math.PI / 12), (float) (Math.PI / 12), 0.0F));
        root.addOrReplaceChild(
                "Tent11",
                CubeListBuilder.create().texOffs(0, 162).addBox(-4.0F, -4.0F, -32.0F, 7.0F, 7.0F, 32.0F),
                PartPose.offsetAndRotation(-14.0F, 7.0F, -15.0F, 0.0872665F, 0.4363323F, 0.0F));
        root.addOrReplaceChild(
                "Tent12",
                CubeListBuilder.create().texOffs(0, 162).addBox(-4.0F, -4.0F, -32.0F, 7.0F, 7.0F, 32.0F),
                PartPose.offsetAndRotation(-26.0F, 10.0F, -42.0F, 0.0872665F, 0.3490659F, 0.0F));
        root.addOrReplaceChild(
                "Tent13",
                CubeListBuilder.create().texOffs(0, 162).addBox(-4.0F, -4.0F, -32.0F, 7.0F, 7.0F, 32.0F),
                PartPose.offsetAndRotation(-36.0F, 13.0F, -70.0F, 0.0872665F, (float) (Math.PI / 12), 0.0F));
        root.addOrReplaceChild(
                "Tent14",
                CubeListBuilder.create().texOffs(0, 125).addBox(-2.0F, -2.0F, -32.0F, 5.0F, 5.0F, 32.0F),
                PartPose.offsetAndRotation(-44.0F, 15.0F, -98.0F, 0.0872665F, 0.4363323F, 0.0F));
        root.addOrReplaceChild(
                "Tent15",
                CubeListBuilder.create().texOffs(0, 125).addBox(-2.0F, -2.0F, -32.0F, 5.0F, 5.0F, 32.0F),
                PartPose.offsetAndRotation(-57.0F, 18.0F, -127.0F, 0.0872665F, (float) (Math.PI / 12), 0.0F));
        root.addOrReplaceChild(
                "Tent16",
                CubeListBuilder.create().texOffs(0, 90).addBox(-1.0F, -1.0F, -32.0F, 3.0F, 3.0F, 32.0F),
                PartPose.offsetAndRotation(-65.0F, 21.0F, -156.0F, 0.0872665F, 0.0872665F, 0.0F));
        root.addOrReplaceChild(
                "Tent31",
                CubeListBuilder.create().texOffs(0, 162).addBox(-4.0F, -4.0F, -32.0F, 7.0F, 7.0F, 32.0F),
                PartPose.offsetAndRotation(16.0F, 22.0F, -15.0F, (float) (Math.PI / 12), (float) (-Math.PI / 12), 0.0F));
        root.addOrReplaceChild(
                "Tent18",
                CubeListBuilder.create().texOffs(0, 57).addBox(-1.0F, -1.0F, -32.0F, 1.0F, 1.0F, 32.0F),
                PartPose.offsetAndRotation(-62.0F, 27.0F, -217.0F, 0.0872665F, -0.1745329F, 0.0F));
        root.addOrReplaceChild(
                "Tooth2",
                CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, 0.0F, -8.0F, 1.0F, 1.0F, 8.0F),
                PartPose.offsetAndRotation(-3.0F, 24.0F, -27.0F, -0.3490659F, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "Tooth3",
                CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, 0.0F, -8.0F, 1.0F, 1.0F, 8.0F),
                PartPose.offsetAndRotation(-1.0F, 24.0F, -27.0F, -0.3490659F, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "Tooth4",
                CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, 0.0F, -8.0F, 1.0F, 1.0F, 8.0F),
                PartPose.offsetAndRotation(1.0F, 24.0F, -27.0F, -0.3490659F, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "Tooth5",
                CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, 0.0F, -8.0F, 1.0F, 1.0F, 8.0F),
                PartPose.offsetAndRotation(3.0F, 24.0F, -27.0F, -0.3490659F, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "Tooth6",
                CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, 0.0F, -8.0F, 1.0F, 1.0F, 8.0F),
                PartPose.offsetAndRotation(5.0F, 24.0F, -27.0F, -0.3490659F, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "Tooth7",
                CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, 0.0F, -8.0F, 1.0F, 1.0F, 8.0F),
                PartPose.offsetAndRotation(9.0F, 23.0F, -28.0F, -0.3490659F, 0.1745329F, 0.0F));
        root.addOrReplaceChild(
                "Tooth8",
                CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, 0.0F, -8.0F, 1.0F, 1.0F, 8.0F),
                PartPose.offsetAndRotation(10.0F, 21.0F, -29.0F, -0.3490659F, 0.1745329F, 0.0F));
        root.addOrReplaceChild(
                "Tooth9",
                CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, 0.0F, -8.0F, 1.0F, 1.0F, 8.0F),
                PartPose.offsetAndRotation(12.0F, 20.0F, -28.0F, -0.3490659F, 0.1745329F, 0.0F));
        root.addOrReplaceChild(
                "Tooth10",
                CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, 0.0F, -8.0F, 1.0F, 1.0F, 8.0F),
                PartPose.offsetAndRotation(14.0F, 19.0F, -28.0F, -0.3490659F, 0.1745329F, 0.0F));
        root.addOrReplaceChild(
                "Tooth11",
                CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, 0.0F, -8.0F, 1.0F, 1.0F, 8.0F),
                PartPose.offsetAndRotation(15.0F, 14.0F, -30.0F, 0.0F, 0.3490659F, 0.0F));
        root.addOrReplaceChild(
                "Tooth12",
                CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, 0.0F, -8.0F, 1.0F, 1.0F, 8.0F),
                PartPose.offsetAndRotation(15.0F, 12.0F, -30.0F, 0.0F, 0.3490659F, 0.0F));
        root.addOrReplaceChild(
                "Tooth13",
                CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, 0.0F, -8.0F, 1.0F, 1.0F, 8.0F),
                PartPose.offsetAndRotation(15.0F, 10.0F, -30.0F, 0.0F, 0.3490659F, 0.0F));
        root.addOrReplaceChild(
                "Tooth14",
                CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, 0.0F, -8.0F, 1.0F, 1.0F, 8.0F),
                PartPose.offsetAndRotation(15.0F, 8.0F, -30.0F, 0.0F, 0.3490659F, 0.0F));
        root.addOrReplaceChild(
                "Tooth15",
                CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, 0.0F, -8.0F, 1.0F, 1.0F, 8.0F),
                PartPose.offsetAndRotation(15.0F, 6.0F, -30.0F, 0.0F, 0.3490659F, 0.0F));
        root.addOrReplaceChild(
                "Tooth16",
                CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, 0.0F, -8.0F, 1.0F, 1.0F, 8.0F),
                PartPose.offsetAndRotation(15.0F, 4.0F, -30.0F, 0.0F, 0.3490659F, 0.0F));
        root.addOrReplaceChild(
                "Tooth17",
                CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, 0.0F, -8.0F, 1.0F, 1.0F, 8.0F),
                PartPose.offsetAndRotation(15.0F, 2.0F, -30.0F, 0.0F, 0.3490659F, 0.0F));
        root.addOrReplaceChild(
                "Tooth18",
                CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, 0.0F, -8.0F, 1.0F, 1.0F, 8.0F),
                PartPose.offsetAndRotation(13.0F, -1.0F, -32.0F, 0.1745329F, 0.3490659F, 0.0F));
        root.addOrReplaceChild(
                "Tooth19",
                CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, 0.0F, -8.0F, 1.0F, 1.0F, 8.0F),
                PartPose.offsetAndRotation(11.0F, -3.0F, -32.0F, 0.1745329F, 0.3490659F, 0.0F));
        root.addOrReplaceChild(
                "Tooth20",
                CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, 0.0F, -8.0F, 1.0F, 1.0F, 8.0F),
                PartPose.offsetAndRotation(9.0F, -5.0F, -32.0F, 0.1745329F, 0.3490659F, 0.0F));
        root.addOrReplaceChild(
                "Tooth21",
                CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, 0.0F, -8.0F, 1.0F, 1.0F, 8.0F),
                PartPose.offsetAndRotation(9.0F, -5.0F, -32.0F, 0.1745329F, 0.3490659F, 0.0F));
        root.addOrReplaceChild(
                "Tooth22",
                CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, 0.0F, -8.0F, 1.0F, 1.0F, 8.0F),
                PartPose.offsetAndRotation(5.0F, -7.0F, -32.0F, 0.3490659F, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "Tooth23",
                CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, 0.0F, -8.0F, 1.0F, 1.0F, 8.0F),
                PartPose.offsetAndRotation(3.0F, -7.0F, -32.0F, 0.3490659F, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "Tooth24",
                CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, 0.0F, -8.0F, 1.0F, 1.0F, 8.0F),
                PartPose.offsetAndRotation(1.0F, -7.0F, -32.0F, 0.3490659F, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "Tooth25",
                CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, 0.0F, -8.0F, 1.0F, 1.0F, 8.0F),
                PartPose.offsetAndRotation(-1.0F, -7.0F, -32.0F, 0.3490659F, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "Tooth26",
                CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, 0.0F, -8.0F, 1.0F, 1.0F, 8.0F),
                PartPose.offsetAndRotation(-3.0F, -7.0F, -32.0F, 0.3490659F, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "Tooth27",
                CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, 0.0F, -8.0F, 1.0F, 1.0F, 8.0F),
                PartPose.offsetAndRotation(-5.0F, -7.0F, -32.0F, 0.3490659F, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "Tooth28",
                CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, 0.0F, -8.0F, 1.0F, 1.0F, 8.0F),
                PartPose.offsetAndRotation(-8.0F, -6.0F, -32.0F, 0.3490659F, -0.1745329F, 0.0F));
        root.addOrReplaceChild(
                "Tooth29",
                CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, 0.0F, -8.0F, 1.0F, 1.0F, 8.0F),
                PartPose.offsetAndRotation(-11.0F, -4.0F, -32.0F, 0.3490659F, -0.1745329F, 0.0F));
        root.addOrReplaceChild(
                "Tooth30",
                CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, 0.0F, -8.0F, 1.0F, 1.0F, 8.0F),
                PartPose.offsetAndRotation(-13.0F, -2.0F, -32.0F, 0.3490659F, -0.1745329F, 0.0F));
        root.addOrReplaceChild(
                "Tooth31",
                CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, 0.0F, -8.0F, 1.0F, 1.0F, 8.0F),
                PartPose.offsetAndRotation(-14.0F, 0.0F, -32.0F, 0.3490659F, -0.1745329F, 0.0F));
        root.addOrReplaceChild(
                "Tooth32",
                CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, 0.0F, -8.0F, 1.0F, 1.0F, 8.0F),
                PartPose.offsetAndRotation(-16.0F, 4.0F, -32.0F, 0.0F, -0.3490659F, 0.0F));
        root.addOrReplaceChild(
                "Tooth33",
                CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, 0.0F, -8.0F, 1.0F, 1.0F, 8.0F),
                PartPose.offsetAndRotation(-16.0F, 6.0F, -32.0F, 0.0F, -0.3490659F, 0.0F));
        root.addOrReplaceChild(
                "Tooth34",
                CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, 0.0F, -8.0F, 1.0F, 1.0F, 8.0F),
                PartPose.offsetAndRotation(-16.0F, 8.0F, -32.0F, 0.0F, -0.3490659F, 0.0F));
        root.addOrReplaceChild(
                "Tooth35",
                CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, 0.0F, -8.0F, 1.0F, 1.0F, 8.0F),
                PartPose.offsetAndRotation(-16.0F, 10.0F, -32.0F, 0.0F, -0.3490659F, 0.0F));
        root.addOrReplaceChild(
                "Tooth36",
                CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, 0.0F, -8.0F, 1.0F, 1.0F, 8.0F),
                PartPose.offsetAndRotation(-16.0F, 12.0F, -32.0F, 0.0F, -0.3490659F, 0.0F));
        root.addOrReplaceChild(
                "Tooth37",
                CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, 0.0F, -8.0F, 1.0F, 1.0F, 8.0F),
                PartPose.offsetAndRotation(-16.0F, 14.0F, -32.0F, 0.0F, -0.3490659F, 0.0F));
        root.addOrReplaceChild(
                "Tooth38",
                CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, 0.0F, -8.0F, 1.0F, 1.0F, 8.0F),
                PartPose.offsetAndRotation(-15.0F, 18.0F, -30.0F, -0.1745329F, -0.3490659F, 0.0F));
        root.addOrReplaceChild(
                "Tooth39",
                CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, 0.0F, -8.0F, 1.0F, 1.0F, 8.0F),
                PartPose.offsetAndRotation(-13.0F, 20.0F, -30.0F, -0.1745329F, -0.3490659F, 0.0F));
        root.addOrReplaceChild(
                "Tooth40",
                CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, 0.0F, -8.0F, 1.0F, 1.0F, 8.0F),
                PartPose.offsetAndRotation(-11.0F, 22.0F, -30.0F, -0.1745329F, -0.3490659F, 0.0F));
        root.addOrReplaceChild(
                "Tooth41",
                CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, 0.0F, -8.0F, 1.0F, 1.0F, 8.0F),
                PartPose.offsetAndRotation(-9.0F, 23.0F, -29.0F, -0.1745329F, -0.3490659F, 0.0F));
        root.addOrReplaceChild(
                "Tent17",
                CubeListBuilder.create().texOffs(0, 90).addBox(-1.0F, -1.0F, -32.0F, 3.0F, 3.0F, 32.0F),
                PartPose.offsetAndRotation(-68.0F, 24.0F, -187.0F, 0.0872665F, -0.1745329F, 0.0F));
        return LayerDefinition.create(mesh, 512, 512);
    }

    @Override
    public ModelPart root() {
        return this.root;
    }

    @Override
    public void setupAnim(
            Kraken entity,
            float limbSwing,
            float limbSwingAmount,
            float ageInTicks,
            float netHeadYaw,
            float headPitch) {
        // gold func_78088_a — FULL animation
        float f2 = ageInTicks;
        float newangle = 0.0F;
        float nextangle = 0.0F;

        this.Finright.zRot = Mth.cos(f2 * 0.43F * this.wingspeed) * (float) Math.PI * 0.15F;
        this.Finleft.zRot = Mth.cos(f2 * 0.32F * this.wingspeed) * (float) Math.PI * 0.14F;

        this.dangle_tentacle(
                f2,
                5,
                entity.getAttacking(),
                this.Tent51,
                this.Tent52,
                this.Tent53,
                this.Tent54,
                this.Tent55,
                this.Tent56,
                this.Tent57,
                this.Tent58);
        this.dangle_tentacle(
                f2,
                6,
                entity.getAttacking(),
                this.Tent61,
                this.Tent62,
                this.Tent63,
                this.Tent64,
                this.Tent65,
                this.Tent66,
                this.Tent67,
                this.Tent68);

        // suction cups follow tentacle tips (dist 30)
        this.Sucktioncupleft.y =
                this.Tent58.y + (float) Math.sin(this.Tent58.xRot) * 30.0F * (float) Math.cos(this.Tent58.yRot);
        this.Sucktioncupleft.z =
                this.Tent58.z - (float) Math.cos(this.Tent58.xRot) * 30.0F * (float) Math.cos(this.Tent58.yRot);
        this.Sucktioncupleft.x =
                this.Tent58.x - (float) Math.sin(this.Tent58.yRot) * 30.0F * (float) Math.cos(this.Tent58.xRot);
        this.Sucktioncupleft.xRot = this.Tent58.xRot;
        this.Sucktioncupleft.yRot = this.Tent58.yRot;

        this.Sucktioncupright.y =
                this.Tent68.y + (float) Math.sin(this.Tent68.xRot) * 30.0F * (float) Math.cos(this.Tent68.yRot);
        this.Sucktioncupright.z =
                this.Tent68.z - (float) Math.cos(this.Tent68.xRot) * 30.0F * (float) Math.cos(this.Tent68.yRot);
        this.Sucktioncupright.x =
                this.Tent68.x - (float) Math.sin(this.Tent68.yRot) * 30.0F * (float) Math.cos(this.Tent68.xRot);
        this.Sucktioncupright.xRot = this.Tent68.xRot;
        this.Sucktioncupright.yRot = this.Tent68.yRot;

        RenderInfo r = entity.getRenderInfo();
        newangle = Mth.cos(f2 * 0.66F) * (float) Math.PI * 0.15F;
        nextangle = Mth.cos((f2 + 0.1F) * 0.66F) * (float) Math.PI * 0.15F;
        if (nextangle > 0.0F && newangle < 0.0F) {
            r.ri1 = 0;
            if (entity.getAttacking() == 0) {
                r.ri1 = entity.getRandom().nextInt(10);
                r.ri2 = entity.getRandom().nextInt(15);
            } else {
                r.ri1 = entity.getRandom().nextInt(4);
                r.ri2 = entity.getRandom().nextInt(3);
            }
        }

        if (r.ri1 != 1 && r.ri1 != 3) {
            newangle = 0.0F;
        } else {
            newangle = Mth.cos(f2 * 0.5F * this.wingspeed) * (float) Math.PI * 0.015F;
        }

        this.Mouth1.xRot = -0.38F + newangle;
        this.Mouth1.yRot = -0.38F + newangle;
        this.Mouth2.xRot = -0.38F + newangle;
        this.Mouth2.yRot = 0.38F - newangle;
        this.Mouth3.xRot = 0.38F - newangle;
        this.Mouth3.yRot = 0.38F - newangle;
        this.Mouth5.xRot = 0.38F - newangle;
        this.Mouth5.yRot = -0.38F + newangle;
        this.Mouth4.xRot = 0.38F - newangle;
        this.Mouth6.yRot = -0.38F + newangle;
        this.Mouth7.yRot = 0.38F - newangle;
        this.Mouth8.xRot = -0.38F + newangle;
        newangle *= 7.0F;
        this.Tooth2.xRot = -0.35F - newangle;
        this.Tooth3.xRot = -0.34F - newangle;
        this.Tooth4.xRot = -0.33F - newangle;
        this.Tooth5.xRot = -0.36F - newangle;
        this.Tooth6.xRot = -0.32F - newangle;
        this.Tooth11.yRot = 0.35F + newangle;
        this.Tooth12.yRot = 0.37F + newangle;
        this.Tooth13.yRot = 0.33F + newangle;
        this.Tooth14.yRot = 0.34F + newangle;
        this.Tooth15.yRot = 0.36F + newangle;
        this.Tooth16.yRot = 0.35F + newangle;
        this.Tooth17.yRot = 0.32F + newangle;
        this.Tooth22.xRot = 0.31F + newangle;
        this.Tooth23.xRot = 0.37F + newangle;
        this.Tooth24.xRot = 0.33F + newangle;
        this.Tooth25.xRot = 0.34F + newangle;
        this.Tooth26.xRot = 0.36F + newangle;
        this.Tooth27.xRot = 0.35F + newangle;
        this.Tooth31.yRot = -0.35F - newangle;
        this.Tooth32.yRot = -0.37F - newangle;
        this.Tooth33.yRot = -0.33F - newangle;
        this.Tooth34.yRot = -0.34F - newangle;
        this.Tooth35.yRot = -0.36F - newangle;
        this.Tooth36.yRot = -0.35F - newangle;
        this.Tooth37.yRot = -0.32F - newangle;
        this.Tooth7.xRot = -0.35F - newangle;
        this.Tooth7.yRot = 0.33F + newangle;
        this.Tooth8.xRot = -0.31F - newangle;
        this.Tooth8.yRot = 0.37F + newangle;
        this.Tooth9.xRot = -0.32F - newangle;
        this.Tooth9.yRot = 0.3F + newangle;
        this.Tooth10.xRot = -0.33F - newangle;
        this.Tooth10.yRot = 0.33F + newangle;
        this.Tooth18.xRot = 0.35F + newangle;
        this.Tooth18.yRot = 0.33F + newangle;
        this.Tooth19.xRot = 0.31F + newangle;
        this.Tooth19.yRot = 0.37F + newangle;
        this.Tooth20.xRot = 0.37F + newangle;
        this.Tooth20.yRot = 0.37F + newangle;
        this.Tooth21.xRot = 0.3F + newangle;
        this.Tooth21.yRot = 0.3F + newangle;
        this.Tooth28.xRot = 0.37F + newangle;
        this.Tooth28.yRot = -0.3F - newangle;
        this.Tooth29.xRot = 0.33F + newangle;
        this.Tooth29.yRot = -0.32F - newangle;
        this.Tooth30.xRot = 0.3F + newangle;
        this.Tooth30.yRot = -0.37F - newangle;
        this.Tooth31.xRot = 0.37F + newangle;
        this.Tooth31.yRot = -0.3F - newangle;
        this.Tooth38.xRot = -0.34F - newangle;
        this.Tooth38.yRot = -0.33F - newangle;
        this.Tooth39.xRot = -0.35F - newangle;
        this.Tooth39.yRot = -0.37F - newangle;
        this.Tooth40.xRot = -0.39F - newangle;
        this.Tooth40.yRot = -0.33F - newangle;
        this.Tooth41.xRot = -0.34F - newangle;
        this.Tooth41.yRot = -0.36F - newangle;
        this.Tooth1.xRot = -0.35F - newangle;
        this.Tooth1.yRot = -0.32F - newangle;
        entity.setRenderInfo(r);

        this.dangle_tentacle(
                f2, 1, 0, this.Tent11, this.Tent12, this.Tent13, this.Tent14, this.Tent15, this.Tent16, this.Tent17, this.Tent18);
        this.dangle_tentacle(
                f2, 2, 0, this.Tent21, this.Tent22, this.Tent23, this.Tent24, this.Tent25, this.Tent26, this.Tent27, this.Tent28);
        this.dangle_tentacle(
                f2, 3, 0, this.Tent31, this.Tent32, this.Tent33, this.Tent34, this.Tent35, this.Tent36, this.Tent37, this.Tent38);
        this.dangle_tentacle(
                f2, 4, 0, this.Tent41, this.Tent42, this.Tent43, this.Tent44, this.Tent45, this.Tent46, this.Tent47, this.Tent48);
        // gold GL11.glRotatef(90, 1, 0, 0) applied in KrakenRenderer.setupRotations
    }

    /**
     * Gold {@code dangle_tentacle} — 8-segment chain; positions follow prior segment (dist 30).
     * field_78795_f→xRot, field_78796_g→yRot, field_78797_d→y, field_78798_e→z, field_78800_c→x.
     */
    private void dangle_tentacle(
            float f2,
            int dir,
            int att,
            ModelPart p1,
            ModelPart p2,
            ModelPart p3,
            ModelPart p4,
            ModelPart p5,
            ModelPart p6,
            ModelPart p7,
            ModelPart p8) {
        float pi4 = 0.314159F;
        int dist = 30;
        float differ = 0.1F;
        float xoff = 0.0F;
        float ydiffer = 0.1F;
        float yoff = 0.0F;
        float s = -1.0F;
        float amp = 0.1F;
        if (dir == 1) {
            differ = 0.101F;
        }
        if (dir == 2) {
            differ = 0.097F;
        }
        if (dir == 3) {
            differ = 0.093F;
        }
        if (dir == 4) {
            differ = 0.087F;
        }
        if (dir == 1) {
            ydiffer = 0.102F;
        }
        if (dir == 2) {
            ydiffer = 0.098F;
        }
        if (dir == 3) {
            ydiffer = 0.092F;
        }
        if (dir == 4) {
            ydiffer = 0.088F;
        }
        if (dir == 2) {
            xoff = 0.26F;
        }
        if (dir == 3) {
            xoff = 0.26F;
        }
        if (dir == 1) {
            yoff = 0.44F;
        }
        if (dir == 4) {
            yoff = -0.44F;
        }
        if (dir == 5) {
            differ = 0.2F;
        }
        if (dir == 6) {
            differ = 0.2F;
        }
        if (dir == 5) {
            xoff = -0.25F;
        }
        if (dir == 6) {
            xoff = -0.25F;
        }
        if (dir == 6) {
            s = 1.0F;
        }
        if (att != 0) {
            if (dir == 5) {
                differ = 0.5F;
                amp = 0.03F;
                xoff = 0.0F;
            }
            if (dir == 6) {
                differ = 0.5F;
                amp = 0.03F;
                xoff = 0.0F;
            }
        }

        p1.xRot = xoff + s * Mth.cos(f2 * differ * this.wingspeed) * (float) Math.PI * amp;
        p1.yRot = yoff - Mth.cos(f2 * ydiffer * this.wingspeed) * (float) Math.PI * amp;
        p2.y = p1.y + (float) Math.sin(p1.xRot) * dist * (float) Math.cos(p1.yRot);
        p2.z = p1.z - (float) Math.cos(p1.xRot) * dist * (float) Math.cos(p1.yRot);
        p2.x = p1.x - (float) Math.sin(p1.yRot) * dist * (float) Math.cos(p1.xRot);
        p2.xRot = xoff / 2.0F + s * Mth.cos(f2 * differ * this.wingspeed - pi4) * (float) Math.PI * amp;
        p2.yRot = yoff / 2.0F - Mth.cos(f2 * ydiffer * this.wingspeed - pi4) * (float) Math.PI * amp;
        p3.y = p2.y + (float) Math.sin(p2.xRot) * dist * (float) Math.cos(p2.yRot);
        p3.z = p2.z - (float) Math.cos(p2.xRot) * dist * (float) Math.cos(p2.yRot);
        p3.x = p2.x - (float) Math.sin(p2.yRot) * dist * (float) Math.cos(p2.xRot);
        p3.xRot = s * Mth.cos(f2 * differ * this.wingspeed - 2.0F * pi4) * (float) Math.PI * amp;
        p3.yRot = -Mth.cos(f2 * ydiffer * this.wingspeed - 2.0F * pi4) * (float) Math.PI * amp;
        p4.y = p3.y + (float) Math.sin(p3.xRot) * dist * (float) Math.cos(p3.yRot);
        p4.z = p3.z - (float) Math.cos(p3.xRot) * dist * (float) Math.cos(p3.yRot);
        p4.x = p3.x - (float) Math.sin(p3.yRot) * dist * (float) Math.cos(p3.xRot);
        p4.xRot = s * Mth.cos(f2 * differ * this.wingspeed - 3.0F * pi4) * (float) Math.PI * amp;
        p4.yRot = -Mth.cos(f2 * ydiffer * this.wingspeed - 3.0F * pi4) * (float) Math.PI * amp;
        p5.y = p4.y + (float) Math.sin(p4.xRot) * dist * (float) Math.cos(p4.yRot);
        p5.z = p4.z - (float) Math.cos(p4.xRot) * dist * (float) Math.cos(p4.yRot);
        p5.x = p4.x - (float) Math.sin(p4.yRot) * dist * (float) Math.cos(p4.xRot);
        p5.xRot = s * Mth.cos(f2 * differ * this.wingspeed - 4.0F * pi4) * (float) Math.PI * amp;
        p5.yRot = -Mth.cos(f2 * ydiffer * this.wingspeed - 4.0F * pi4) * (float) Math.PI * amp;
        p6.y = p5.y + (float) Math.sin(p5.xRot) * dist * (float) Math.cos(p5.yRot);
        p6.z = p5.z - (float) Math.cos(p5.xRot) * dist * (float) Math.cos(p5.yRot);
        p6.x = p5.x - (float) Math.sin(p5.yRot) * dist * (float) Math.cos(p5.xRot);
        p6.xRot = s * Mth.cos(f2 * differ * this.wingspeed - 5.0F * pi4) * (float) Math.PI * amp;
        p6.yRot = -Mth.cos(f2 * ydiffer * this.wingspeed - 5.0F * pi4) * (float) Math.PI * amp;
        p7.y = p6.y + (float) Math.sin(p6.xRot) * dist * (float) Math.cos(p6.yRot);
        p7.z = p6.z - (float) Math.cos(p6.xRot) * dist * (float) Math.cos(p6.yRot);
        p7.x = p6.x - (float) Math.sin(p6.yRot) * dist * (float) Math.cos(p6.xRot);
        p7.xRot = s * Mth.cos(f2 * differ * this.wingspeed - 6.0F * pi4) * (float) Math.PI * amp;
        p7.yRot = -Mth.cos(f2 * ydiffer * this.wingspeed - 6.0F * pi4) * (float) Math.PI * amp;
        p8.y = p7.y + (float) Math.sin(p7.xRot) * dist * (float) Math.cos(p7.yRot);
        p8.z = p7.z - (float) Math.cos(p7.xRot) * dist * (float) Math.cos(p7.yRot);
        p8.x = p7.x - (float) Math.sin(p7.yRot) * dist * (float) Math.cos(p7.xRot);
        p8.xRot = s * Mth.cos(f2 * differ * this.wingspeed - 7.0F * pi4) * (float) Math.PI * amp;
        p8.yRot = -Mth.cos(f2 * ydiffer * this.wingspeed - 7.0F * pi4) * (float) Math.PI * amp;
    }
}
