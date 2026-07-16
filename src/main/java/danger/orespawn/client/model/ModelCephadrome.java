package danger.orespawn.client.model;

import danger.orespawn.entity.Cephadrome;
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
 * Port of gold {@code ModelCephadrome} (1.7.10 ModelBase, tex 512×256, 50 cubes) → HierarchicalModel.
 * Full gold {@code func_78088_a} animation (legs/wings/topfins/tail/neck/jaw) in setupAnim.
 * Local LAYER_LOCATION; wingspeed default 0.55F matches ClientProxy.
 */
@OnlyIn(Dist.CLIENT)
public class ModelCephadrome extends HierarchicalModel<Cephadrome> {
    /** Model layer location. */
    public static final ModelLayerLocation LAYER_LOCATION =
            new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "cephadrome"), "main");

    private final float wingspeed;
    private final ModelPart root;
    private final ModelPart leftfoot;
    private final ModelPart butt;
    private final ModelPart rightfoot;
    private final ModelPart topfin1;
    private final ModelPart topfin2;
    private final ModelPart topfin3;
    private final ModelPart topfin4;
    private final ModelPart leftshoulder;
    private final ModelPart lefwingfin1;
    private final ModelPart tailfin1;
    private final ModelPart tailmembrane2;
    private final ModelPart tailfin2;
    private final ModelPart tailfin4;
    private final ModelPart tailfin3;
    private final ModelPart tailmembrane1;
    private final ModelPart topmem1;
    private final ModelPart topmem2;
    private final ModelPart topmem3;
    private final ModelPart topmem4;
    private final ModelPart neck1;
    private final ModelPart body;
    private final ModelPart chest1;
    private final ModelPart leftleg1;
    private final ModelPart mouth;
    private final ModelPart neck2;
    private final ModelPart head;
    private final ModelPart hammerhead;
    private final ModelPart chest;
    private final ModelPart neck3;
    private final ModelPart tail1;
    private final ModelPart rightleg1;
    private final ModelPart leftleg2;
    private final ModelPart rightleg2;
    private final ModelPart body2;
    private final ModelPart leftleg3;
    private final ModelPart rightleg3;
    private final ModelPart tail2;
    private final ModelPart tail3;
    private final ModelPart tailmembrane3;
    private final ModelPart leftwingfin2;
    private final ModelPart leftwingfin3;
    private final ModelPart leftwingfin4;
    private final ModelPart leftwingmembrane;
    private final ModelPart rightshoulder;
    private final ModelPart rightwingfin1;
    private final ModelPart rightwingfin2;
    private final ModelPart rightwingfin3;
    private final ModelPart rightwingfin4;
    private final ModelPart rightwingmembrane;
    private final ModelPart hammerhead2;

    public ModelCephadrome(ModelPart root) {
        this(root, 0.55F);
    }

    public ModelCephadrome(ModelPart root, float wingspeed) {
        this.root = root;
        this.wingspeed = wingspeed;
        this.leftfoot = root.getChild("leftfoot");
        this.butt = root.getChild("butt");
        this.rightfoot = root.getChild("rightfoot");
        this.topfin1 = root.getChild("topfin1");
        this.topfin2 = root.getChild("topfin2");
        this.topfin3 = root.getChild("topfin3");
        this.topfin4 = root.getChild("topfin4");
        this.leftshoulder = root.getChild("leftshoulder");
        this.lefwingfin1 = root.getChild("lefwingfin1");
        this.tailfin1 = root.getChild("tailfin1");
        this.tailmembrane2 = root.getChild("tailmembrane2");
        this.tailfin2 = root.getChild("tailfin2");
        this.tailfin4 = root.getChild("tailfin4");
        this.tailfin3 = root.getChild("tailfin3");
        this.tailmembrane1 = root.getChild("tailmembrane1");
        this.topmem1 = root.getChild("topmem1");
        this.topmem2 = root.getChild("topmem2");
        this.topmem3 = root.getChild("topmem3");
        this.topmem4 = root.getChild("topmem4");
        this.neck1 = root.getChild("neck1");
        this.body = root.getChild("body");
        this.chest1 = root.getChild("chest1");
        this.leftleg1 = root.getChild("leftleg1");
        this.mouth = root.getChild("mouth");
        this.neck2 = root.getChild("neck2");
        this.head = root.getChild("head");
        this.hammerhead = root.getChild("hammerhead");
        this.chest = root.getChild("chest");
        this.neck3 = root.getChild("neck3");
        this.tail1 = root.getChild("tail1");
        this.rightleg1 = root.getChild("rightleg1");
        this.leftleg2 = root.getChild("leftleg2");
        this.rightleg2 = root.getChild("rightleg2");
        this.body2 = root.getChild("body2");
        this.leftleg3 = root.getChild("leftleg3");
        this.rightleg3 = root.getChild("rightleg3");
        this.tail2 = root.getChild("tail2");
        this.tail3 = root.getChild("tail3");
        this.tailmembrane3 = root.getChild("tailmembrane3");
        this.leftwingfin2 = root.getChild("leftwingfin2");
        this.leftwingfin3 = root.getChild("leftwingfin3");
        this.leftwingfin4 = root.getChild("leftwingfin4");
        this.leftwingmembrane = root.getChild("leftwingmembrane");
        this.rightshoulder = root.getChild("rightshoulder");
        this.rightwingfin1 = root.getChild("rightwingfin1");
        this.rightwingfin2 = root.getChild("rightwingfin2");
        this.rightwingfin3 = root.getChild("rightwingfin3");
        this.rightwingfin4 = root.getChild("rightwingfin4");
        this.rightwingmembrane = root.getChild("rightwingmembrane");
        this.hammerhead2 = root.getChild("hammerhead2");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        root.addOrReplaceChild(
                "leftfoot",
                CubeListBuilder.create().texOffs(41, 194).addBox(-2.0F, 34.0F, -12.0F, 9.0F, 4.0F, 10.0F),
                PartPose.offset(7.0F, -14.0F, 17.0F));
        root.addOrReplaceChild(
                "butt",
                CubeListBuilder.create().texOffs(367, 235).addBox(0.0F, 0.0F, -2.0F, 9.0F, 14.0F, 6.0F),
                PartPose.offsetAndRotation(-4.5F, -8.0F, 29.0F, -0.8726646F, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "rightfoot",
                CubeListBuilder.create().texOffs(41, 170).addBox(-7.0F, 34.0F, -12.0F, 9.0F, 4.0F, 10.0F),
                PartPose.offset(-7.0F, -14.0F, 17.0F));
        root.addOrReplaceChild(
                "topfin1",
                CubeListBuilder.create().texOffs(64, 112).addBox(-3.0F, -2.0F, -30.0F, 6.0F, 3.0F, 30.0F),
                PartPose.offsetAndRotation(0.0F, -15.0F, -7.0F, -1.850049F, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "topfin2",
                CubeListBuilder.create().texOffs(69, 81).addBox(-3.0F, -2.0F, -25.0F, 6.0F, 3.0F, 25.0F),
                PartPose.offsetAndRotation(0.0F, -15.0F, -2.0F, -2.076942F, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "topfin3",
                CubeListBuilder.create().texOffs(-1, 140).addBox(-3.0F, -2.0F, -20.0F, 6.0F, 3.0F, 20.0F),
                PartPose.offsetAndRotation(0.0F, -16.0F, 3.0F, -2.426008F, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "topfin4",
                CubeListBuilder.create().texOffs(148, 148).addBox(-3.0F, -2.0F, -10.0F, 6.0F, 3.0F, 10.0F),
                PartPose.offsetAndRotation(0.0F, -17.0F, 13.0F, -2.635447F, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "leftshoulder",
                CubeListBuilder.create().texOffs(144, 236).addBox(0.0F, 0.0F, 1.0F, 6.0F, 8.0F, 11.0F),
                PartPose.offsetAndRotation(6.0F, -16.0F, -14.0F, -0.1745329F, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "lefwingfin1",
                CubeListBuilder.create().texOffs(147, 96).addBox(0.0F, -2.0F, -2.0F, 70.0F, 5.0F, 3.0F),
                PartPose.offsetAndRotation(9.0F, -12.0F, -11.0F, (float) (-Math.PI / 12), -0.1745329F, 0.0F));
        root.addOrReplaceChild(
                "tailfin1",
                CubeListBuilder.create().texOffs(168, 0).addBox(-6.0F, -1.0F, 0.0F, 12.0F, 3.0F, 30.0F),
                PartPose.offsetAndRotation(0.0F, -9.0F, 56.0F, 0.1396263F, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "tailmembrane2",
                CubeListBuilder.create().texOffs(201, 38).addBox(0.0F, -8.0F, 3.0F, 0.0F, 10.0F, 19.0F),
                PartPose.offsetAndRotation(0.0F, 0.0F, 56.0F, -0.296706F, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "tailfin2",
                CubeListBuilder.create().texOffs(186, 184).addBox(-4.0F, 0.0F, 0.0F, 8.0F, 2.0F, 27.0F),
                PartPose.offsetAndRotation(0.0F, -7.0F, 56.0F, -0.1919862F, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "tailfin4",
                CubeListBuilder.create().texOffs(186, 137).addBox(-4.0F, 1.0F, 1.0F, 8.0F, 3.0F, 22.0F),
                PartPose.offsetAndRotation(0.0F, -3.0F, 56.0F, -0.837758F, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "tailfin3",
                CubeListBuilder.create().texOffs(185, 216).addBox(-4.0F, 0.0F, 1.0F, 8.0F, 2.0F, 23.0F),
                PartPose.offsetAndRotation(0.0F, -5.0F, 57.0F, -0.5759587F, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "tailmembrane1",
                CubeListBuilder.create().texOffs(245, 38).addBox(0.0F, 0.0F, 4.0F, 0.0F, 11.0F, 21.0F),
                PartPose.offsetAndRotation(0.0F, -9.0F, 56.0F, 0.1396263F, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "topmem1",
                CubeListBuilder.create().texOffs(25, 0).addBox(0.0F, -25.0F, 0.0F, 0.0F, 24.0F, 10.0F),
                PartPose.offsetAndRotation(0.0F, -15.0F, -6.0F, (float) (-Math.PI / 12), 0.0F, 0.0F));
        root.addOrReplaceChild(
                "topmem2",
                CubeListBuilder.create().texOffs(135, 0).addBox(1.0F, -22.0F, 0.0F, 0.0F, 20.0F, 10.0F),
                PartPose.offsetAndRotation(-1.0F, -15.0F, -2.0F, (float) (-Math.PI / 6), 0.0F, 0.0F));
        root.addOrReplaceChild(
                "topmem3",
                CubeListBuilder.create().texOffs(258, 0).addBox(0.0F, -18.0F, 0.0F, 0.0F, 18.0F, 8.0F),
                PartPose.offsetAndRotation(0.0F, -16.0F, 3.0F, -0.8901179F, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "topmem4",
                CubeListBuilder.create().texOffs(282, 0).addBox(0.0F, -9.0F, 0.0F, 0.0F, 9.0F, 10.0F),
                PartPose.offsetAndRotation(0.0F, -17.0F, 13.0F, -1.117011F, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "neck1",
                CubeListBuilder.create().texOffs(404, 235).addBox(-6.0F, -5.0F, -10.0F, 10.0F, 9.0F, 10.0F),
                PartPose.offsetAndRotation(1.0F, -6.0F, -33.0F, 0.3665191F, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "body",
                CubeListBuilder.create().texOffs(28, 220).addBox(-6.0F, -11.0F, -10.0F, 12.0F, 15.0F, 19.0F),
                PartPose.offsetAndRotation(0.0F, -7.0F, 3.0F, 0.1745329F, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "chest1",
                CubeListBuilder.create().texOffs(98, 210).addBox(-3.0F, -4.0F, -2.0F, 10.0F, 11.0F, 5.0F),
                PartPose.offsetAndRotation(-2.0F, -6.0F, -13.0F, 1.029744F, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "leftleg1",
                CubeListBuilder.create().texOffs(135, 183).addBox(-1.0F, 0.0F, -4.0F, 7.0F, 18.0F, 10.0F),
                PartPose.offsetAndRotation(7.0F, -14.0F, 17.0F, -0.5759587F, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "mouth",
                CubeListBuilder.create().texOffs(92, 150).addBox(-7.0F, 1.0F, 3.0F, 14.0F, 15.0F, 4.0F),
                PartPose.offsetAndRotation(0.0F, -6.0F, -43.0F, -0.8726646F, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "neck2",
                CubeListBuilder.create().texOffs(152, 110).addBox(-6.0F, -5.0F, -17.0F, 11.0F, 10.0F, 17.0F),
                PartPose.offsetAndRotation(0.5F, -10.0F, -19.0F, (float) (Math.PI / 12), 0.0F, 0.0F));
        root.addOrReplaceChild(
                "head",
                CubeListBuilder.create().texOffs(275, 219).addBox(-10.0F, -3.0F, -16.0F, 20.0F, 7.0F, 16.0F),
                PartPose.offsetAndRotation(0.0F, -6.0F, -43.0F, 0.5061455F, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "hammerhead",
                CubeListBuilder.create().texOffs(258, 134).addBox(-18.0F, -2.0F, -15.0F, 36.0F, 6.0F, 14.0F),
                PartPose.offsetAndRotation(0.0F, -6.0F, -43.0F, 0.4537856F, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "chest",
                CubeListBuilder.create().texOffs(100, 15).addBox(-3.0F, -3.0F, 0.0F, 9.0F, 29.0F, 7.0F),
                PartPose.offsetAndRotation(-1.5F, 0.0F, -5.0F, 1.413717F, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "neck3",
                CubeListBuilder.create().texOffs(264, 173).addBox(-6.0F, -5.0F, -16.0F, 12.0F, 11.0F, 16.0F),
                PartPose.offsetAndRotation(0.0F, -11.0F, -6.0F, 0.0872665F, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "tail1",
                CubeListBuilder.create().texOffs(51, 5).addBox(-5.0F, -6.0F, 0.0F, 10.0F, 13.0F, 14.0F),
                PartPose.offsetAndRotation(0.0F, -10.0F, 22.0F, -0.1745329F, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "rightleg1",
                CubeListBuilder.create().texOffs(94, 175).addBox(-6.0F, 0.0F, -4.0F, 7.0F, 18.0F, 10.0F),
                PartPose.offsetAndRotation(-7.0F, -14.0F, 17.0F, -0.5759587F, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "leftleg2",
                CubeListBuilder.create().texOffs(28, 112).addBox(-1.0F, 6.0F, -17.0F, 7.0F, 12.0F, 7.0F),
                PartPose.offsetAndRotation(7.0F, -14.0F, 17.0F, 0.9773844F, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "rightleg2",
                CubeListBuilder.create().texOffs(32, 90).addBox(-6.0F, 6.0F, -17.0F, 7.0F, 12.0F, 7.0F),
                PartPose.offsetAndRotation(-7.0F, -14.0F, 17.0F, 0.9773844F, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "body2",
                CubeListBuilder.create().texOffs(400, 179).addBox(0.0F, 3.0F, 3.0F, 12.0F, 16.0F, 16.0F),
                PartPose.offsetAndRotation(-6.0F, -23.0F, 6.0F, -0.1919862F, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "leftleg3",
                CubeListBuilder.create().texOffs(351, 192).addBox(-1.0F, 17.0F, 10.0F, 7.0F, 17.0F, 6.0F),
                PartPose.offsetAndRotation(7.0F, -14.0F, 17.0F, (float) (-Math.PI / 6), 0.0F, 0.0F));
        root.addOrReplaceChild(
                "rightleg3",
                CubeListBuilder.create().texOffs(323, 192).addBox(-6.0F, 17.0F, 10.0F, 7.0F, 17.0F, 6.0F),
                PartPose.offsetAndRotation(-7.0F, -14.0F, 17.0F, (float) (-Math.PI / 6), 0.0F, 0.0F));
        root.addOrReplaceChild(
                "tail2",
                CubeListBuilder.create().texOffs(51, 55).addBox(-6.0F, -6.0F, 0.0F, 9.0F, 12.0F, 14.0F),
                PartPose.offsetAndRotation(1.5F, -7.0F, 35.0F, -0.1396263F, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "tail3",
                CubeListBuilder.create().texOffs(105, 52).addBox(-5.0F, -6.0F, 0.0F, 8.0F, 11.0F, 14.0F),
                PartPose.offsetAndRotation(1.0F, -5.0F, 48.0F, -0.1396263F, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "tailmembrane3",
                CubeListBuilder.create().texOffs(155, 38).addBox(0.0F, -10.0F, 0.0F, 0.0F, 10.0F, 18.0F),
                PartPose.offsetAndRotation(0.0F, 2.0F, 56.0F, -0.837758F, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "leftwingfin2",
                CubeListBuilder.create().texOffs(160, 83).addBox(0.0F, -2.0F, 0.0F, 64.0F, 4.0F, 2.0F),
                PartPose.offsetAndRotation(9.0F, -12.0F, -11.0F, (float) (-Math.PI / 12), -0.4363323F, 0.0F));
        root.addOrReplaceChild(
                "leftwingfin3",
                CubeListBuilder.create().texOffs(209, 106).addBox(0.0F, -2.0F, 0.0F, 48.0F, 4.0F, 2.0F),
                PartPose.offsetAndRotation(9.0F, -11.0F, -10.0F, (float) (-Math.PI / 12), (float) (-Math.PI / 4), 0.0F));
        root.addOrReplaceChild(
                "leftwingfin4",
                CubeListBuilder.create().texOffs(233, 120).addBox(0.0F, 0.0F, 0.0F, 37.0F, 4.0F, 2.0F),
                PartPose.offsetAndRotation(9.0F, -13.0F, -6.0F, (float) (-Math.PI / 12), -1.186824F, 0.0F));
        root.addOrReplaceChild(
                "leftwingmembrane",
                CubeListBuilder.create().texOffs(300, 27).addBox(3.0F, 0.0F, 0.0F, 64.0F, 0.0F, 34.0F),
                PartPose.offsetAndRotation(9.0F, -13.0F, -10.0F, -0.0872665F, -0.1745329F, 0.0F));
        root.addOrReplaceChild(
                "rightshoulder",
                CubeListBuilder.create().texOffs(0, 193).addBox(0.0F, 0.0F, 0.0F, 6.0F, 8.0F, 11.0F),
                PartPose.offsetAndRotation(-12.0F, -16.0F, -13.0F, -0.1745329F, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "rightwingfin1",
                CubeListBuilder.create().texOffs(344, 109).addBox(-69.0F, -2.0F, 0.0F, 69.0F, 5.0F, 3.0F),
                PartPose.offsetAndRotation(-10.0F, -12.0F, -13.0F, (float) (-Math.PI / 12), 0.1745329F, 0.0F));
        root.addOrReplaceChild(
                "rightwingfin2",
                CubeListBuilder.create().texOffs(349, 119).addBox(-63.0F, -2.0F, 0.0F, 64.0F, 4.0F, 2.0F),
                PartPose.offsetAndRotation(-9.0F, -12.0F, -11.0F, (float) (-Math.PI / 12), 0.4363323F, 0.0F));
        root.addOrReplaceChild(
                "rightwingfin3",
                CubeListBuilder.create().texOffs(368, 128).addBox(-49.0F, 0.0F, 0.0F, 48.0F, 4.0F, 2.0F),
                PartPose.offsetAndRotation(-9.0F, -13.0F, -9.0F, (float) (-Math.PI / 12), 0.7679449F, 0.0F));
        root.addOrReplaceChild(
                "rightwingfin4",
                CubeListBuilder.create().texOffs(379, 137).addBox(-35.0F, 0.0F, 0.0F, 37.0F, 4.0F, 2.0F),
                PartPose.offsetAndRotation(-9.0F, -13.0F, -6.0F, (float) (-Math.PI / 12), 1.186824F, 0.0F));
        root.addOrReplaceChild(
                "rightwingmembrane",
                CubeListBuilder.create().texOffs(300, 67).addBox(-67.0F, -1.0F, 0.0F, 64.0F, 0.0F, 34.0F),
                PartPose.offsetAndRotation(-9.0F, -12.0F, -12.0F, -0.0872665F, 0.1745329F, 0.0F));
        root.addOrReplaceChild(
                "hammerhead2",
                CubeListBuilder.create().texOffs(258, 157).addBox(-25.0F, 0.0F, -14.0F, 50.0F, 4.0F, 7.0F),
                PartPose.offsetAndRotation(0.0F, -7.0F, -43.0F, 0.4537856F, 0.0F, 0.0F));

        return LayerDefinition.create(mesh, 512, 256);
    }

    @Override
    public ModelPart root() {
        return this.root;
    }

    @Override
    public void setupAnim(
            Cephadrome entity,
            float limbSwing,
            float limbSwingAmount,
            float ageInTicks,
            float netHeadYaw,
            float headPitch) {
        // gold func_78088_a FULL (empty func_78087_a)
        float f1 = limbSwingAmount;
        float f2 = ageInTicks;
        float f3 = netHeadYaw;
        RenderInfo r = entity.getRenderInfo();
        float newangle = 0.0F;
        float lspeed = 0.0F;
        float pi4 = (float) (Math.PI / 4);
        float tailspeed = 0.76F;
        float tailamp = 0.1F;

        if (f1 > 0.001F) {
            lspeed = (float) (
                    (entity.xo - entity.getX()) * (entity.xo - entity.getX())
                            + (entity.zo - entity.getZ()) * (entity.zo - entity.getZ()));
            lspeed = (float) Math.sqrt(lspeed);
            newangle = Mth.cos(f2 * 0.75F * this.wingspeed) * (float) Math.PI * lspeed * 0.4F;
            if (newangle > 0.5F) {
                newangle = 0.75F;
            }
            if (newangle < -0.5F) {
                newangle = -0.75F;
            }
        } else {
            newangle = 0.0F;
        }

        if (entity.getActivity() != 0) {
            newangle = 1.0F;
            this.rightleg1.xRot = -0.58F + newangle;
            this.rightleg2.xRot = 0.98F + newangle;
            this.rightleg3.xRot = -0.52F + newangle;
            this.rightfoot.xRot = newangle;
            this.leftleg1.xRot = -0.58F + newangle;
            this.leftleg2.xRot = 0.98F + newangle;
            this.leftleg3.xRot = -0.52F + newangle;
            this.leftfoot.xRot = newangle;
        } else {
            this.rightleg1.xRot = -0.58F + newangle;
            this.rightleg2.xRot = 0.98F + newangle;
            this.rightleg3.xRot = -0.52F + newangle;
            this.rightfoot.xRot = newangle;
            this.leftleg1.xRot = -0.58F - newangle;
            this.leftleg2.xRot = 0.98F - newangle;
            this.leftleg3.xRot = -0.52F - newangle;
            this.leftfoot.xRot = -newangle;
        }

        if (entity.getActivity() != 0) {
            newangle = Mth.cos(f2 * 0.55F * this.wingspeed) * (float) Math.PI * 0.28F;
        } else if (entity.getAttacking() == 0) {
            newangle = -0.85F + Mth.cos(f2 * 0.2F * this.wingspeed) * (float) Math.PI * 0.028F;
        } else {
            newangle = -0.65F + Mth.cos(f2 * 0.9F * this.wingspeed) * (float) Math.PI * 0.068F;
        }

        this.lefwingfin1.zRot = newangle;
        this.leftwingfin2.zRot = newangle;
        this.leftwingfin3.zRot = newangle;
        this.leftwingfin4.zRot = newangle;
        this.leftwingmembrane.zRot = newangle;
        this.rightwingfin1.zRot = -newangle;
        this.rightwingfin2.zRot = -newangle;
        this.rightwingfin3.zRot = -newangle;
        this.rightwingfin4.zRot = -newangle;
        this.rightwingmembrane.zRot = -newangle;

        newangle = Mth.cos(f2 * 0.15F * this.wingspeed) * (float) Math.PI * 0.05F;
        this.topfin1.xRot = -1.85F - Math.abs(newangle);
        this.topmem1.xRot = -0.26F - Math.abs(newangle);
        this.topfin2.xRot = -2.07F - Math.abs(newangle / 2.0F);
        this.topmem2.xRot = -0.52F - Math.abs(newangle / 2.0F);
        this.topfin3.xRot = -2.42F - Math.abs(newangle / 4.0F);
        this.topmem3.xRot = -0.89F - Math.abs(newangle / 4.0F);
        this.topfin4.xRot = -2.63F - Math.abs(newangle / 8.0F);
        this.topmem4.xRot = -1.11F - Math.abs(newangle / 8.0F);

        if (entity.getActivity() == 0 && entity.getAttacking() == 0) {
            tailspeed = 0.22F;
            tailamp = 0.03F;
        }

        this.tail1.yRot = Mth.cos(f2 * tailspeed * this.wingspeed) * (float) Math.PI * 0.04F;
        this.tail2.z = this.tail1.z + (float) Math.cos(this.tail1.yRot) * 13.0F;
        this.tail2.x = this.tail1.x + 1.5F + (float) Math.sin(this.tail1.yRot) * 13.0F;
        this.tail2.yRot = Mth.cos(f2 * tailspeed * this.wingspeed - pi4) * (float) Math.PI * tailamp;
        this.tail3.z = this.tail2.z + (float) Math.cos(this.tail2.yRot) * 13.0F;
        this.tail3.x = this.tail2.x - 0.5F + (float) Math.sin(this.tail2.yRot) * 13.0F;
        this.tail3.yRot = Mth.cos(f2 * tailspeed * this.wingspeed - 2.0F * pi4) * (float) Math.PI * tailamp;
        this.tailfin1.z = this.tail3.z + (float) Math.cos(this.tail3.yRot) * 10.0F;
        this.tailfin1.x = this.tail3.x - 1.0F + (float) Math.sin(this.tail3.yRot) * 10.0F;
        this.tailfin1.yRot = Mth.cos(f2 * tailspeed * this.wingspeed - 3.0F * pi4) * (float) Math.PI * tailamp;
        this.tailfin2.z = this.tailfin1.z;
        this.tailfin2.x = this.tailfin1.x;
        this.tailfin2.yRot = this.tailfin1.yRot;
        this.tailfin3.z = this.tailfin1.z;
        this.tailfin3.x = this.tailfin1.x;
        this.tailfin3.yRot = this.tailfin1.yRot;
        this.tailfin4.z = this.tailfin1.z;
        this.tailfin4.x = this.tailfin1.x;
        this.tailfin4.yRot = this.tailfin1.yRot;
        this.tailmembrane1.z = this.tailfin1.z;
        this.tailmembrane1.x = this.tailfin1.x;
        this.tailmembrane1.yRot = this.tailfin1.yRot;
        this.tailmembrane2.z = this.tailfin1.z;
        this.tailmembrane2.x = this.tailfin1.x;
        this.tailmembrane2.yRot = this.tailfin1.yRot;
        this.tailmembrane3.z = this.tailfin1.z;
        this.tailmembrane3.x = this.tailfin1.x;
        this.tailmembrane3.yRot = this.tailfin1.yRot;

        if (entity.getActivity() == 1) {
            f3 = (entity.yRotO - entity.getYRot()) * 10.0F;
            f3 = -f3;
            r.rf1 = r.rf1 + (f3 - r.rf1) / 50.0F;
            if (r.rf1 > 50.0F) {
                r.rf1 = 50.0F;
            }
            if (r.rf1 < -50.0F) {
                r.rf1 = -50.0F;
            }
            f3 = r.rf1;
        } else {
            f3 /= 2.0F;
        }

        this.neck3.yRot = (float) Math.toRadians(f3) * 0.125F;
        this.neck2.z = this.neck3.z - (float) Math.cos(this.neck3.yRot) * 14.0F;
        this.neck2.x = this.neck3.x + 0.5F - (float) Math.sin(this.neck3.yRot) * 14.0F;
        this.neck2.yRot = (float) Math.toRadians(f3) * 0.25F;
        this.neck1.z = this.neck2.z - (float) Math.cos(this.neck2.yRot) * 14.0F;
        this.neck1.x = this.neck2.x + 0.5F - (float) Math.sin(this.neck2.yRot) * 14.0F;
        this.neck1.yRot = (float) Math.toRadians(f3) * 0.5F;
        this.head.z = this.neck1.z - (float) Math.cos(this.neck1.yRot) * 8.0F;
        this.head.x = this.neck1.x - (float) Math.sin(this.neck1.yRot) * 8.0F;
        this.head.yRot = (float) Math.toRadians(f3) * 0.75F;
        this.hammerhead.z = this.head.z;
        this.hammerhead.x = this.head.x;
        this.hammerhead.yRot = this.head.yRot;
        this.hammerhead2.z = this.head.z;
        this.hammerhead2.x = this.head.x;
        this.hammerhead2.yRot = this.head.yRot;
        this.mouth.z = this.head.z;
        this.mouth.x = this.head.x;
        this.mouth.yRot = this.head.yRot;

        newangle = Mth.cos(f2 * 0.5F * this.wingspeed) * (float) Math.PI * 0.14F;
        if (entity.getAttacking() != 0) {
            this.mouth.xRot = -0.61F + newangle;
        } else {
            this.mouth.xRot = -0.87F;
        }

        entity.setRenderInfo(r);
    }

}
