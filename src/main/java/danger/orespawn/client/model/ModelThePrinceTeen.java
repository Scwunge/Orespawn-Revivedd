package danger.orespawn.client.model;

import danger.orespawn.entity.RenderInfo;
import danger.orespawn.entity.ThePrinceTeen;
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
 * Port of gold {@code ModelThePrinceTeen} (1.7.10 ModelBase, tex 512×256, 71 cubes) → HierarchicalModel.
 * Full gold {@code func_78088_a} animation (wings/legs/claws/tail/3-heads/necks/jaws) in setupAnim.
 * Local LAYER_LOCATION; wingspeed default 0.65F matches ClientProxy.
 */
@OnlyIn(Dist.CLIENT)
public class ModelThePrinceTeen extends HierarchicalModel<ThePrinceTeen> {
    /** Model layer location. */
    public static final ModelLayerLocation LAYER_LOCATION =
            new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "the_prince_teen"), "main");

    private final float wingspeed;
    private final ModelPart root;
    private final ModelPart body;
    private final ModelPart leftleg1;
    private final ModelPart tail1;
    private final ModelPart leftleg2;
    private final ModelPart body2;
    private final ModelPart leftleg3;
    private final ModelPart tail2;
    private final ModelPart tail3;
    private final ModelPart lclaw2;
    private final ModelPart lclaw4;
    private final ModelPart lclaw5;
    private final ModelPart lclaw6;
    private final ModelPart lclaw7;
    private final ModelPart tail4;
    private final ModelPart tail5;
    private final ModelPart neck1;
    private final ModelPart neck3;
    private final ModelPart head3;
    private final ModelPart jaw1;
    private final ModelPart jaw5;
    private final ModelPart head7;
    private final ModelPart rightleg1;
    private final ModelPart rightleg2;
    private final ModelPart rightleg3;
    private final ModelPart rclaw2;
    private final ModelPart rclaw4;
    private final ModelPart rclaw5;
    private final ModelPart rclaw7;
    private final ModelPart rclaw6;
    private final ModelPart wing1;
    private final ModelPart wing2;
    private final ModelPart mem1;
    private final ModelPart mem2;
    private final ModelPart lshoulder;
    private final ModelPart rshoulder;
    private final ModelPart rwing1;
    private final ModelPart rmem1;
    private final ModelPart rwing2;
    private final ModelPart rmem2;
    private final ModelPart neck4;
    private final ModelPart neck5;
    private final ModelPart wing3;
    private final ModelPart mem3;
    private final ModelPart rwing3;
    private final ModelPart rmem3;
    private final ModelPart wing4;
    private final ModelPart mem4;
    private final ModelPart rwing4;
    private final ModelPart rmem4;
    private final ModelPart Tailspike1;
    private final ModelPart Tailspike2;
    private final ModelPart Tailspike3;
    private final ModelPart headfin;
    private final ModelPart backfin1;
    private final ModelPart backfin2;
    private final ModelPart neck3L;
    private final ModelPart neck4L;
    private final ModelPart neck3R;
    private final ModelPart neck4R;
    private final ModelPart neck5L;
    private final ModelPart neck5R;
    private final ModelPart jaw5L;
    private final ModelPart jaw5R;
    private final ModelPart head7L;
    private final ModelPart headfinL;
    private final ModelPart headfinR;
    private final ModelPart head7R;
    private final ModelPart jaw1L;
    private final ModelPart jaw1R;
    private final ModelPart head3L;
    private final ModelPart head3R;

    public ModelThePrinceTeen(ModelPart root) {
        this(root, 0.65F);
    }

    public ModelThePrinceTeen(ModelPart root, float wingspeed) {
        this.root = root;
        this.wingspeed = wingspeed;
        this.body = root.getChild("body");
        this.leftleg1 = root.getChild("leftleg1");
        this.tail1 = root.getChild("tail1");
        this.leftleg2 = root.getChild("leftleg2");
        this.body2 = root.getChild("body2");
        this.leftleg3 = root.getChild("leftleg3");
        this.tail2 = root.getChild("tail2");
        this.tail3 = root.getChild("tail3");
        this.lclaw2 = root.getChild("lclaw2");
        this.lclaw4 = root.getChild("lclaw4");
        this.lclaw5 = root.getChild("lclaw5");
        this.lclaw6 = root.getChild("lclaw6");
        this.lclaw7 = root.getChild("lclaw7");
        this.tail4 = root.getChild("tail4");
        this.tail5 = root.getChild("tail5");
        this.neck1 = root.getChild("neck1");
        this.neck3 = root.getChild("neck3");
        this.head3 = root.getChild("head3");
        this.jaw1 = root.getChild("jaw1");
        this.jaw5 = root.getChild("jaw5");
        this.head7 = root.getChild("head7");
        this.rightleg1 = root.getChild("rightleg1");
        this.rightleg2 = root.getChild("rightleg2");
        this.rightleg3 = root.getChild("rightleg3");
        this.rclaw2 = root.getChild("rclaw2");
        this.rclaw4 = root.getChild("rclaw4");
        this.rclaw5 = root.getChild("rclaw5");
        this.rclaw7 = root.getChild("rclaw7");
        this.rclaw6 = root.getChild("rclaw6");
        this.wing1 = root.getChild("wing1");
        this.wing2 = root.getChild("wing2");
        this.mem1 = root.getChild("mem1");
        this.mem2 = root.getChild("mem2");
        this.lshoulder = root.getChild("lshoulder");
        this.rshoulder = root.getChild("rshoulder");
        this.rwing1 = root.getChild("rwing1");
        this.rmem1 = root.getChild("rmem1");
        this.rwing2 = root.getChild("rwing2");
        this.rmem2 = root.getChild("rmem2");
        this.neck4 = root.getChild("neck4");
        this.neck5 = root.getChild("neck5");
        this.wing3 = root.getChild("wing3");
        this.mem3 = root.getChild("mem3");
        this.rwing3 = root.getChild("rwing3");
        this.rmem3 = root.getChild("rmem3");
        this.wing4 = root.getChild("wing4");
        this.mem4 = root.getChild("mem4");
        this.rwing4 = root.getChild("rwing4");
        this.rmem4 = root.getChild("rmem4");
        this.Tailspike1 = root.getChild("Tailspike1");
        this.Tailspike2 = root.getChild("Tailspike2");
        this.Tailspike3 = root.getChild("Tailspike3");
        this.headfin = root.getChild("headfin");
        this.backfin1 = root.getChild("backfin1");
        this.backfin2 = root.getChild("backfin2");
        this.neck3L = root.getChild("neck3L");
        this.neck4L = root.getChild("neck4L");
        this.neck3R = root.getChild("neck3R");
        this.neck4R = root.getChild("neck4R");
        this.neck5L = root.getChild("neck5L");
        this.neck5R = root.getChild("neck5R");
        this.jaw5L = root.getChild("jaw5L");
        this.jaw5R = root.getChild("jaw5R");
        this.head7L = root.getChild("head7L");
        this.headfinL = root.getChild("headfinL");
        this.headfinR = root.getChild("headfinR");
        this.head7R = root.getChild("head7R");
        this.jaw1L = root.getChild("jaw1L");
        this.jaw1R = root.getChild("jaw1R");
        this.head3L = root.getChild("head3L");
        this.head3R = root.getChild("head3R");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        root.addOrReplaceChild(
                "body",
                CubeListBuilder.create().texOffs(400, 26).addBox(-12.5F, -12.0F, -9.0F, 25.0F, 12.0F, 9.0F),
                PartPose.offsetAndRotation(0.0F, 0.0F, 9.0F, 0.0698132F, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "leftleg1",
                CubeListBuilder.create().texOffs(300, 10).addBox(-1.0F, -1.0F, -3.0F, 5.0F, 9.0F, 9.0F),
                PartPose.offsetAndRotation(14.0F, -8.0F, 13.0F, -0.5759587F, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "tail1",
                CubeListBuilder.create().texOffs(400, 82).addBox(-9.0F, -6.0F, 0.0F, 18.0F, 10.0F, 12.0F),
                PartPose.offsetAndRotation(0.0F, -3.0F, 22.0F, -0.1745329F, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "leftleg2",
                CubeListBuilder.create().texOffs(300, 31).addBox(-1.0F, 6.0F, -7.0F, 4.0F, 12.0F, 5.0F),
                PartPose.offsetAndRotation(14.0F, -8.0F, 13.0F, 0.9773844F, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "body2",
                CubeListBuilder.create().texOffs(400, 50).addBox(0.0F, -3.0F, -3.0F, 26.0F, 14.0F, 16.0F),
                PartPose.offsetAndRotation(-13.0F, -9.0F, 10.0F, -0.1047198F, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "leftleg3",
                CubeListBuilder.create().texOffs(300, 51).addBox(-1.0F, -1.0F, -2.0F, 3.0F, 19.0F, 4.0F),
                PartPose.offsetAndRotation(14.0F, 7.0F, 22.0F, (float) (-Math.PI / 6), 0.0F, 0.0F));
        root.addOrReplaceChild(
                "tail2",
                CubeListBuilder.create().texOffs(400, 106).addBox(-5.0F, -4.0F, 0.0F, 10.0F, 7.0F, 10.0F),
                PartPose.offsetAndRotation(0.0F, -2.0F, 33.0F, -0.1396263F, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "tail3",
                CubeListBuilder.create().texOffs(400, 126).addBox(-3.0F, -2.0F, 0.0F, 6.0F, 5.0F, 10.0F),
                PartPose.offsetAndRotation(0.0F, -2.0F, 42.0F, -0.1396263F, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "lclaw2",
                CubeListBuilder.create().texOffs(300, 76).addBox(-3.0F, 0.0F, -3.0F, 7.0F, 3.0F, 8.0F),
                PartPose.offset(14.0F, 21.0F, 13.0F));
        root.addOrReplaceChild(
                "lclaw4",
                CubeListBuilder.create().texOffs(310, 123).addBox(0.0F, 1.0F, -7.0F, 1.0F, 2.0F, 4.0F),
                PartPose.offset(14.0F, 21.0F, 13.0F));
        root.addOrReplaceChild(
                "lclaw5",
                CubeListBuilder.create().texOffs(297, 123).addBox(-2.5F, 1.0F, -7.0F, 1.0F, 2.0F, 4.0F),
                PartPose.offset(14.0F, 21.0F, 13.0F));
        root.addOrReplaceChild(
                "lclaw6",
                CubeListBuilder.create().texOffs(322, 123).addBox(2.5F, 1.0F, -7.0F, 1.0F, 2.0F, 4.0F),
                PartPose.offset(14.0F, 21.0F, 13.0F));
        root.addOrReplaceChild(
                "lclaw7",
                CubeListBuilder.create().texOffs(334, 123).addBox(0.0F, 1.0F, 5.0F, 1.0F, 2.0F, 3.0F),
                PartPose.offset(14.0F, 21.0F, 13.0F));
        root.addOrReplaceChild(
                "tail4",
                CubeListBuilder.create().texOffs(400, 143).addBox(-2.0F, -2.0F, 0.0F, 4.0F, 4.0F, 10.0F),
                PartPose.offsetAndRotation(0.0F, 0.0F, 51.0F, -0.1396263F, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "tail5",
                CubeListBuilder.create().texOffs(400, 159).addBox(-1.5F, -2.0F, 0.0F, 3.0F, 3.0F, 10.0F),
                PartPose.offsetAndRotation(0.0F, 2.0F, 59.0F, -0.1396263F, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "neck1",
                CubeListBuilder.create().texOffs(400, 7).addBox(-12.0F, -4.0F, 0.0F, 24.0F, 8.0F, 8.0F),
                PartPose.offsetAndRotation(0.0F, -7.0F, -5.0F, 0.0872665F, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "neck3",
                CubeListBuilder.create().texOffs(365, 5).addBox(-3.0F, -3.0F, -9.0F, 6.0F, 6.0F, 10.0F),
                PartPose.offsetAndRotation(0.0F, -8.0F, -5.0F, 0.0174533F, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "head3",
                CubeListBuilder.create().texOffs(143, 149).addBox(-2.0F, -3.0F, -15.0F, 4.0F, 4.0F, 17.0F),
                PartPose.offsetAndRotation(0.0F, -6.0F, -34.0F, -0.2443461F, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "jaw1",
                CubeListBuilder.create().texOffs(143, 173).addBox(-1.5F, 1.0F, -14.0F, 3.0F, 2.0F, 12.0F),
                PartPose.offsetAndRotation(0.0F, -6.0F, -34.0F, 0.1919862F, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "jaw5",
                CubeListBuilder.create().texOffs(144, 206).addBox(-2.5F, 1.0F, -3.0F, 5.0F, 2.0F, 6.0F),
                PartPose.offsetAndRotation(0.0F, -6.0F, -34.0F, 0.1919862F, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "head7",
                CubeListBuilder.create().texOffs(144, 192).addBox(-3.0F, -4.0F, -3.0F, 6.0F, 5.0F, 7.0F),
                PartPose.offsetAndRotation(0.0F, -6.0F, -34.0F, -0.2443461F, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "rightleg1",
                CubeListBuilder.create().texOffs(250, 10).addBox(-1.0F, -1.0F, -3.0F, 5.0F, 9.0F, 9.0F),
                PartPose.offsetAndRotation(-17.0F, -8.0F, 13.0F, -0.5934119F, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "rightleg2",
                CubeListBuilder.create().texOffs(250, 32).addBox(0.0F, 6.0F, -7.0F, 4.0F, 12.0F, 5.0F),
                PartPose.offsetAndRotation(-17.0F, -8.0F, 13.0F, 0.9773844F, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "rightleg3",
                CubeListBuilder.create().texOffs(250, 52).addBox(1.0F, 1.0F, -2.0F, 3.0F, 19.0F, 4.0F),
                PartPose.offsetAndRotation(-17.0F, 5.0F, 23.0F, (float) (-Math.PI / 6), 0.0F, 0.0F));
        root.addOrReplaceChild(
                "rclaw2",
                CubeListBuilder.create().texOffs(250, 76).addBox(-1.0F, 0.0F, -3.0F, 7.0F, 3.0F, 8.0F),
                PartPose.offset(-17.0F, 21.0F, 13.0F));
        root.addOrReplaceChild(
                "rclaw4",
                CubeListBuilder.create().texOffs(247, 123).addBox(2.0F, 1.0F, -7.0F, 1.0F, 2.0F, 4.0F),
                PartPose.offset(-17.0F, 21.0F, 13.0F));
        root.addOrReplaceChild(
                "rclaw5",
                CubeListBuilder.create().texOffs(258, 123).addBox(-0.5F, 1.0F, -7.0F, 1.0F, 2.0F, 4.0F),
                PartPose.offset(-17.0F, 21.0F, 13.0F));
        root.addOrReplaceChild(
                "rclaw7",
                CubeListBuilder.create().texOffs(283, 123).addBox(2.0F, 1.0F, 5.0F, 1.0F, 2.0F, 3.0F),
                PartPose.offset(-17.0F, 21.0F, 13.0F));
        root.addOrReplaceChild(
                "rclaw6",
                CubeListBuilder.create().texOffs(270, 123).addBox(4.5F, 1.0F, -7.0F, 1.0F, 2.0F, 4.0F),
                PartPose.offset(-17.0F, 21.0F, 13.0F));
        root.addOrReplaceChild(
                "wing1",
                CubeListBuilder.create().texOffs(10, 30).addBox(-1.0F, -1.0F, -1.0F, 23.0F, 2.0F, 2.0F),
                PartPose.offsetAndRotation(13.0F, -12.0F, 3.0F, 0.0F, 0.0872665F, 0.0F));
        root.addOrReplaceChild(
                "wing2",
                CubeListBuilder.create().texOffs(10, 40).addBox(-1.0F, -1.0F, -1.0F, 44.0F, 2.0F, 2.0F),
                PartPose.offset(34.0F, -12.0F, 1.0F));
        root.addOrReplaceChild(
                "mem1",
                CubeListBuilder.create().texOffs(10, 60).addBox(-2.0F, 0.0F, 0.0F, 24.0F, 1.0F, 21.0F),
                PartPose.offsetAndRotation(13.0F, -12.0F, 3.0F, 0.0F, 0.0872665F, 0.0F));
        root.addOrReplaceChild(
                "mem2",
                CubeListBuilder.create().texOffs(10, 85).addBox(0.0F, 0.0F, 0.0F, 43.0F, 1.0F, 21.0F),
                PartPose.offset(34.0F, -12.0F, 1.0F));
        root.addOrReplaceChild(
                "lshoulder",
                CubeListBuilder.create().texOffs(370, 78).addBox(0.0F, 0.0F, 0.0F, 5.0F, 2.0F, 8.0F),
                PartPose.offsetAndRotation(8.0F, -13.0F, 1.0F, 0.0698132F, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "rshoulder",
                CubeListBuilder.create().texOffs(370, 66).addBox(0.0F, 0.0F, 0.0F, 5.0F, 2.0F, 8.0F),
                PartPose.offsetAndRotation(-13.0F, -13.0F, 1.0F, 0.0698132F, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "rwing1",
                CubeListBuilder.create().texOffs(10, 140).addBox(-22.0F, -1.0F, -1.0F, 23.0F, 2.0F, 2.0F),
                PartPose.offsetAndRotation(-13.0F, -12.0F, 3.0F, 0.0F, -0.0872665F, 0.0F));
        root.addOrReplaceChild(
                "rmem1",
                CubeListBuilder.create().texOffs(10, 170).addBox(-22.0F, 0.0F, 0.0F, 24.0F, 1.0F, 21.0F),
                PartPose.offsetAndRotation(-13.0F, -12.0F, 3.0F, 0.0F, -0.0872665F, 0.0F));
        root.addOrReplaceChild(
                "rwing2",
                CubeListBuilder.create().texOffs(10, 150).addBox(-43.0F, -1.0F, -1.0F, 44.0F, 2.0F, 2.0F),
                PartPose.offset(-34.0F, -12.0F, 1.0F));
        root.addOrReplaceChild(
                "rmem2",
                CubeListBuilder.create().texOffs(10, 195).addBox(-43.0F, 0.0F, 0.0F, 43.0F, 1.0F, 21.0F),
                PartPose.offset(-34.0F, -12.0F, 1.0F));
        root.addOrReplaceChild(
                "neck4",
                CubeListBuilder.create().texOffs(366, 23).addBox(-2.5F, -2.5F, -9.0F, 5.0F, 5.0F, 10.0F),
                PartPose.offsetAndRotation(0.0F, -8.0F, -14.0F, 0.1396263F, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "neck5",
                CubeListBuilder.create().texOffs(369, 41).addBox(-2.0F, -2.0F, -9.0F, 4.0F, 4.0F, 10.0F),
                PartPose.offsetAndRotation(0.0F, -7.0F, -22.0F, 0.1396263F, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "wing3",
                CubeListBuilder.create().texOffs(10, 46).addBox(0.0F, 0.0F, 0.0F, 44.0F, 2.0F, 2.0F),
                PartPose.offsetAndRotation(13.0F, -13.0F, 3.0F, 0.0F, 0.0F, -0.3490659F));
        root.addOrReplaceChild(
                "mem3",
                CubeListBuilder.create().texOffs(10, 110).addBox(0.0F, 0.0F, 0.0F, 43.0F, 1.0F, 21.0F),
                PartPose.offsetAndRotation(13.0F, -12.5F, 5.0F, 0.0F, 0.0F, -0.3490659F));
        root.addOrReplaceChild(
                "rwing3",
                CubeListBuilder.create().texOffs(10, 156).addBox(-43.0F, 0.0F, 0.0F, 44.0F, 2.0F, 2.0F),
                PartPose.offsetAndRotation(-13.0F, -13.0F, 3.0F, 0.0F, 0.0F, 0.3490659F));
        root.addOrReplaceChild(
                "rmem3",
                CubeListBuilder.create().texOffs(10, 221).addBox(-42.0F, 0.0F, 0.0F, 43.0F, 1.0F, 21.0F),
                PartPose.offsetAndRotation(-13.0F, -12.5F, 5.0F, 0.0F, 0.0F, 0.3490659F));
        root.addOrReplaceChild(
                "wing4",
                CubeListBuilder.create().texOffs(10, 46).addBox(0.0F, 0.0F, 0.0F, 44.0F, 2.0F, 2.0F),
                PartPose.offsetAndRotation(13.0F, -12.0F, 3.0F, 0.0F, 0.0F, 0.3490659F));
        root.addOrReplaceChild(
                "mem4",
                CubeListBuilder.create().texOffs(10, 110).addBox(0.0F, 0.0F, 0.0F, 43.0F, 1.0F, 21.0F),
                PartPose.offsetAndRotation(13.0F, -11.5F, 5.0F, 0.0F, 0.0F, 0.3490659F));
        root.addOrReplaceChild(
                "rwing4",
                CubeListBuilder.create().texOffs(10, 156).addBox(-43.0F, 0.0F, 0.0F, 44.0F, 2.0F, 2.0F),
                PartPose.offsetAndRotation(-13.0F, -12.0F, 3.0F, 0.0F, 0.0F, -0.3490659F));
        root.addOrReplaceChild(
                "rmem4",
                CubeListBuilder.create().texOffs(10, 221).addBox(-42.0F, 0.0F, 0.0F, 43.0F, 1.0F, 21.0F),
                PartPose.offsetAndRotation(-13.0F, -11.5F, 5.0F, 0.0F, 0.0F, -0.3490659F));
        root.addOrReplaceChild(
                "Tailspike1",
                CubeListBuilder.create().texOffs(150, 0).addBox(-7.0F, 0.0F, 0.0F, 14.0F, 2.0F, 6.0F),
                PartPose.offset(0.0F, 2.0F, 69.0F));
        root.addOrReplaceChild(
                "Tailspike2",
                CubeListBuilder.create().texOffs(150, 11).addBox(-5.0F, 0.0F, 0.0F, 10.0F, 2.0F, 6.0F),
                PartPose.offset(0.0F, 2.0F, 75.0F));
        root.addOrReplaceChild(
                "Tailspike3",
                CubeListBuilder.create().texOffs(150, 23).addBox(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 15.0F),
                PartPose.offset(0.0F, 2.0F, 80.0F));
        root.addOrReplaceChild(
                "headfin",
                CubeListBuilder.create().texOffs(150, 216).addBox(-0.5F, -3.0F, 3.0F, 1.0F, 4.0F, 4.0F),
                PartPose.offsetAndRotation(0.0F, -6.0F, -34.0F, 0.0872665F, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "backfin1",
                CubeListBuilder.create().texOffs(69, 0).addBox(-0.5F, 0.0F, 0.0F, 1.0F, 6.0F, 6.0F),
                PartPose.offsetAndRotation(0.0F, -11.0F, 0.0F, (float) (Math.PI / 4), 0.0F, 0.0F));
        root.addOrReplaceChild(
                "backfin2",
                CubeListBuilder.create().texOffs(85, 0).addBox(-0.5F, 0.0F, 0.0F, 1.0F, 4.0F, 4.0F),
                PartPose.offsetAndRotation(0.0F, -11.0F, 10.0F, (float) (Math.PI / 4), 0.0F, 0.0F));
        root.addOrReplaceChild(
                "neck3L",
                CubeListBuilder.create().texOffs(365, 100).addBox(-3.0F, -3.0F, -9.0F, 6.0F, 6.0F, 10.0F),
                PartPose.offsetAndRotation(8.0F, -8.0F, -5.0F, 0.0174533F, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "neck4L",
                CubeListBuilder.create().texOffs(366, 119).addBox(-2.5F, -2.5F, -9.0F, 5.0F, 5.0F, 10.0F),
                PartPose.offsetAndRotation(8.0F, -8.0F, -14.0F, 0.1396263F, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "neck3R",
                CubeListBuilder.create().texOffs(365, 175).addBox(-3.0F, -3.0F, -9.0F, 6.0F, 6.0F, 10.0F),
                PartPose.offsetAndRotation(-8.0F, -8.0F, -5.0F, 0.0174533F, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "neck4R",
                CubeListBuilder.create().texOffs(366, 194).addBox(-2.5F, -2.5F, -9.0F, 5.0F, 5.0F, 10.0F),
                PartPose.offsetAndRotation(-8.0F, -8.0F, -14.0F, 0.1396263F, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "neck5L",
                CubeListBuilder.create().texOffs(369, 137).addBox(-2.0F, -2.0F, -9.0F, 4.0F, 4.0F, 10.0F),
                PartPose.offsetAndRotation(8.0F, -7.0F, -23.0F, 0.1396263F, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "neck5R",
                CubeListBuilder.create().texOffs(369, 212).addBox(-2.0F, -2.0F, -9.0F, 4.0F, 4.0F, 10.0F),
                PartPose.offsetAndRotation(-8.0F, -7.0F, -23.0F, 0.1396263F, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "jaw5L",
                CubeListBuilder.create().texOffs(200, 206).addBox(-2.5F, 1.0F, -3.0F, 5.0F, 2.0F, 6.0F),
                PartPose.offsetAndRotation(8.0F, -6.0F, -34.0F, 0.1919862F, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "jaw5R",
                CubeListBuilder.create().texOffs(250, 206).addBox(-2.5F, 1.0F, -3.0F, 5.0F, 2.0F, 6.0F),
                PartPose.offsetAndRotation(-8.0F, -6.0F, -34.0F, 0.1919862F, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "head7L",
                CubeListBuilder.create().texOffs(200, 192).addBox(-3.0F, -4.0F, -3.0F, 6.0F, 5.0F, 7.0F),
                PartPose.offsetAndRotation(8.0F, -6.0F, -34.0F, -0.2443461F, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "headfinL",
                CubeListBuilder.create().texOffs(200, 216).addBox(-0.5F, -3.0F, 3.0F, 1.0F, 4.0F, 4.0F),
                PartPose.offsetAndRotation(8.0F, -6.0F, -34.0F, 0.0872665F, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "headfinR",
                CubeListBuilder.create().texOffs(250, 216).addBox(-0.5F, -3.0F, 3.0F, 1.0F, 4.0F, 4.0F),
                PartPose.offsetAndRotation(-8.0F, -6.0F, -34.0F, 0.0872665F, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "head7R",
                CubeListBuilder.create().texOffs(250, 192).addBox(-3.0F, -4.0F, -3.0F, 6.0F, 5.0F, 7.0F),
                PartPose.offsetAndRotation(-8.0F, -6.0F, -34.0F, -0.2443461F, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "jaw1L",
                CubeListBuilder.create().texOffs(200, 173).addBox(-1.5F, 1.0F, -14.0F, 3.0F, 2.0F, 12.0F),
                PartPose.offsetAndRotation(8.0F, -6.0F, -34.0F, 0.1919862F, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "jaw1R",
                CubeListBuilder.create().texOffs(250, 173).addBox(-1.5F, 1.0F, -14.0F, 3.0F, 2.0F, 12.0F),
                PartPose.offsetAndRotation(-8.0F, -6.0F, -34.0F, 0.1919862F, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "head3L",
                CubeListBuilder.create().texOffs(200, 149).addBox(-2.0F, -3.0F, -15.0F, 4.0F, 4.0F, 17.0F),
                PartPose.offsetAndRotation(8.0F, -6.0F, -34.0F, -0.2443461F, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "head3R",
                CubeListBuilder.create().texOffs(250, 149).addBox(-2.0F, -3.0F, -15.0F, 4.0F, 4.0F, 17.0F),
                PartPose.offsetAndRotation(-8.0F, -6.0F, -34.0F, -0.2443461F, 0.0F, 0.0F));

        return LayerDefinition.create(mesh, 512, 256);
    }

    @Override
    public ModelPart root() {
        return this.root;
    }

    @Override
    public void setupAnim(
            ThePrinceTeen entity,
            float limbSwing,
            float limbSwingAmount,
            float ageInTicks,
            float netHeadYaw,
            float headPitch) {
        // gold func_78088_a FULL (f,f1,f2,f3,f4)
        float f1 = limbSwingAmount;
        float f2 = ageInTicks;
        float f3 = netHeadYaw;
        float f4 = headPitch;
        ThePrinceTeen c = entity;
        RenderInfo r = c.getRenderInfo();
        float newangle = 0.0F;
        float newangle2 = 0.0F;
        float rnewangle = 0.0F;
        float rnewangle2 = 0.0F;
        float clawangle = 0.0F;
        float tailspeed = 0.26F;
        float tailamp = 0.08F;
        float pi4 = (float) (Math.PI / 4);
        int current_activity = c.getActivity();

        if (f1 > 0.1 && current_activity == 0) {
            newangle = Mth.cos(f2 * 1.3F * this.wingspeed) * (float) Math.PI * 0.2F * f1;
        } else {
            newangle = Mth.cos(f2 * 0.3F * this.wingspeed) * (float) Math.PI * 0.04F;
        }
        if (current_activity == 1) {
            newangle = Mth.cos(f2 * 1.4F * this.wingspeed) * (float) Math.PI * 0.4F;
        }
        if (c.getAttacking() != 0) {
            newangle = Mth.cos(f2 * 1.7F * this.wingspeed) * (float) Math.PI * 0.4F;
        }

        this.wing1.zRot = newangle - 0.4F;
        this.wing2.zRot = newangle * 1.25F - 0.4F;
        this.wing3.zRot = newangle - 0.6F;
        this.wing4.zRot = newangle - 0.2F;
        this.wing2.y = this.wing1.y + (float) Math.sin(this.wing1.zRot) * 22.0F;
        this.wing2.x = this.wing1.x + (float) Math.cos(this.wing1.zRot) * 22.0F;
        this.mem1.zRot = this.wing1.zRot;
        this.mem2.zRot = this.wing2.zRot;
        this.mem3.zRot = this.wing3.zRot;
        this.mem4.zRot = this.wing4.zRot;
        this.mem2.y = this.wing2.y;
        this.mem2.x = this.wing2.x;
        this.rwing1.zRot = -newangle + 0.4F;
        this.rwing2.zRot = -newangle * 1.25F + 0.4F;
        this.rwing3.zRot = -newangle + 0.6F;
        this.rwing4.zRot = -newangle + 0.2F;
        this.rwing2.y = this.rwing1.y - (float) Math.sin(this.rwing1.zRot) * 22.0F;
        this.rwing2.x = this.rwing1.x - (float) Math.cos(this.rwing1.zRot) * 22.0F;
        this.rmem1.zRot = this.rwing1.zRot;
        this.rmem2.zRot = this.rwing2.zRot;
        this.rmem3.zRot = this.rwing3.zRot;
        this.rmem4.zRot = this.rwing4.zRot;
        this.rmem2.y = this.rwing2.y;
        this.rmem2.x = this.rwing2.x;

        if (f1 > 0.1) {
            newangle = Mth.cos(f2 * 0.55F * this.wingspeed) * (float) Math.PI * 0.25F * f1;
            newangle2 = Mth.cos((float) (f2 * 0.55F * this.wingspeed + (Math.PI / 2))) * (float) Math.PI * 0.25F * f1;
            rnewangle = newangle;
            rnewangle2 = newangle2;
            clawangle = 0.0F;
        } else {
            newangle = 0.0F;
            newangle2 = 0.0F;
            rnewangle = 0.0F;
            rnewangle2 = 0.0F;
            clawangle = 0.0F;
        }
        if (c.getAttacking() != 0) {
            newangle = Mth.cos(f2 * this.wingspeed) * (float) Math.PI * 0.25F;
            newangle2 = Mth.cos((float) (f2 * this.wingspeed + (Math.PI / 2))) * (float) Math.PI * 0.25F;
            rnewangle = newangle;
            rnewangle2 = newangle2;
            clawangle = 0.0F;
        }
        if (current_activity == 1 && c.getAttacking() == 0) {
            newangle = -0.5F;
            newangle2 = -1.25F;
            rnewangle = 0.5F;
            rnewangle2 = 1.25F;
        }
        if (current_activity == 1) {
            clawangle = -0.685F;
        }

        this.leftleg1.xRot = newangle - 0.575F;
        this.leftleg2.xRot = newangle + 0.977F;
        this.leftleg3.xRot = newangle2 - 0.523F;
        this.leftleg3.y = this.leftleg2.y + (float) Math.cos(this.leftleg2.xRot) * 14.0F + 6.0F;
        this.leftleg3.z = this.leftleg2.z + (float) Math.sin(this.leftleg2.xRot) * 14.0F;
        this.lclaw2.y = this.leftleg3.y + (float) Math.cos(this.leftleg3.xRot) * 17.0F;
        this.lclaw2.z = this.leftleg3.z + (float) Math.sin(this.leftleg3.xRot) * 17.0F - 1.0F;
        this.lclaw4.y = this.lclaw2.y;
        this.lclaw4.z = this.lclaw2.z;
        this.lclaw5.y = this.lclaw2.y;
        this.lclaw5.z = this.lclaw2.z;
        this.lclaw6.y = this.lclaw2.y;
        this.lclaw6.z = this.lclaw2.z;
        this.lclaw7.y = this.lclaw2.y;
        this.lclaw7.z = this.lclaw2.z;
        this.lclaw2.xRot = clawangle;
        this.lclaw4.xRot = clawangle;
        this.lclaw5.xRot = clawangle;
        this.lclaw6.xRot = clawangle;
        this.lclaw7.xRot = clawangle;

        this.rightleg1.xRot = -rnewangle - 0.575F;
        this.rightleg2.xRot = -rnewangle + 0.977F;
        this.rightleg3.xRot = -rnewangle2 - 0.523F;
        this.rightleg3.y = this.rightleg2.y + (float) Math.cos(this.rightleg2.xRot) * 14.0F + 5.0F;
        this.rightleg3.z = this.rightleg2.z + (float) Math.sin(this.rightleg2.xRot) * 14.0F;
        this.rclaw2.y = this.rightleg3.y + (float) Math.cos(this.rightleg3.xRot) * 17.0F;
        this.rclaw2.z = this.rightleg3.z + (float) Math.sin(this.rightleg3.xRot) * 17.0F - 1.0F;
        this.rclaw4.y = this.rclaw2.y;
        this.rclaw4.z = this.rclaw2.z;
        this.rclaw5.y = this.rclaw2.y;
        this.rclaw5.z = this.rclaw2.z;
        this.rclaw6.y = this.rclaw2.y;
        this.rclaw6.z = this.rclaw2.z;
        this.rclaw7.y = this.rclaw2.y;
        this.rclaw7.z = this.rclaw2.z;
        this.rclaw2.xRot = clawangle;
        this.rclaw4.xRot = clawangle;
        this.rclaw5.xRot = clawangle;
        this.rclaw6.xRot = clawangle;
        this.rclaw7.xRot = clawangle;

        if (c.getAttacking() != 0) {
            tailspeed = 0.56F;
            tailamp = 0.19F;
        }
        if (c.isOreSpawnSitting()) {
            tailamp = 0.0F;
        }

        this.tail1.yRot = Mth.cos(f2 * tailspeed * this.wingspeed) * (float) Math.PI * tailamp / 4.0F;
        this.tail2.z = this.tail1.z + (float) Math.cos(this.tail1.yRot) * 11.0F;
        this.tail2.x = this.tail1.x + (float) Math.sin(this.tail1.yRot) * 11.0F;
        this.tail2.yRot = Mth.cos(f2 * tailspeed * this.wingspeed - pi4) * (float) Math.PI * tailamp;
        this.tail3.z = this.tail2.z + (float) Math.cos(this.tail2.yRot) * 9.0F;
        this.tail3.x = this.tail2.x + (float) Math.sin(this.tail2.yRot) * 9.0F;
        this.tail3.yRot = Mth.cos(f2 * tailspeed * this.wingspeed - 2.0F * pi4) * (float) Math.PI * tailamp;
        this.tail4.z = this.tail3.z + (float) Math.cos(this.tail3.yRot) * 9.0F;
        this.tail4.x = this.tail3.x + (float) Math.sin(this.tail3.yRot) * 9.0F;
        this.tail4.yRot = Mth.cos(f2 * tailspeed * this.wingspeed - 3.0F * pi4) * (float) Math.PI * tailamp;
        newangle = Mth.cos(f2 * tailspeed * this.wingspeed - 3.0F * pi4) * (float) Math.PI * tailamp;
        newangle /= 2.0F;
        this.tail5.z = this.tail4.z + (float) Math.cos(this.tail4.yRot) * 9.0F;
        this.tail5.x = this.tail4.x + (float) Math.sin(this.tail4.yRot) * 9.0F;
        this.tail5.yRot = this.tail4.yRot + newangle;
        this.Tailspike1.z = this.tail5.z + (float) Math.cos(this.tail5.yRot) * 9.0F;
        this.Tailspike1.x = this.tail5.x + (float) Math.sin(this.tail5.yRot) * 9.0F;
        this.Tailspike2.z = this.tail5.z + (float) Math.cos(this.tail5.yRot) * 15.0F;
        this.Tailspike2.x = this.tail5.x + (float) Math.sin(this.tail5.yRot) * 15.0F;
        this.Tailspike1.yRot = this.Tailspike2.yRot = this.tail5.yRot + newangle * 2.0F / 3.0F;
        this.Tailspike3.z = this.Tailspike1.z + (float) Math.cos(this.Tailspike1.yRot) * 11.0F;
        this.Tailspike3.x = this.Tailspike1.x + (float) Math.sin(this.Tailspike1.yRot) * 11.0F;
        this.Tailspike3.yRot = this.Tailspike1.yRot + newangle * 3.0F / 2.0F;

        if (c.getActivity() == 1) {
            // gold: (prevYaw - yaw) * 10, smoothed into RenderInfo.rf1
            f3 = (c.yRotO - c.getYRot()) * 10.0F;
            f3 = -f3;
            r.rf1 = r.rf1 + (f3 - r.rf1) / 50.0F;
            if (r.rf1 > 50.0F) {
                r.rf1 = 50.0F;
            }
            if (r.rf1 < -50.0F) {
                r.rf1 = -50.0F;
            }
            f3 = r.rf1;
        }

        float h3;
        float h1 = h3 = f3 * 2.0F / 3.0F;
        float d3;
        float d1 = d3 = f4 * 2.0F / 3.0F;
        float h2;
        float d2;
        if (h1 < 0.0F) {
            h2 = h3 = h1 / 2.0F;
            d2 = d3 = d1 / 2.0F;
        } else {
            h2 = h1 = h3 / 2.0F;
            d2 = d1 = d3 / 2.0F;
        }

        this.head7.yRot = (float) Math.toRadians(h2);
        this.head3.yRot = (float) Math.toRadians(h2);
        this.headfin.yRot = (float) Math.toRadians(h2);
        this.jaw5.yRot = (float) Math.toRadians(h2);
        this.jaw1.yRot = (float) Math.toRadians(h2);
        this.neck3.yRot = (float) Math.toRadians(h2) / 8.0F;
        this.neck4.yRot = (float) Math.toRadians(h2) / 4.0F;
        this.neck5.yRot = (float) Math.toRadians(h2) / 2.0F;
        this.head7L.yRot = (float) Math.toRadians(h1);
        this.head3L.yRot = (float) Math.toRadians(h1);
        this.headfinL.yRot = (float) Math.toRadians(h1);
        this.jaw5L.yRot = (float) Math.toRadians(h1);
        this.jaw1L.yRot = (float) Math.toRadians(h1);
        this.neck3L.yRot = (float) Math.toRadians(h1) / 8.0F;
        this.neck4L.yRot = (float) Math.toRadians(h1) / 4.0F;
        this.neck5L.yRot = (float) Math.toRadians(h1) / 2.0F;
        this.head7R.yRot = (float) Math.toRadians(h3);
        this.head3R.yRot = (float) Math.toRadians(h3);
        this.headfinR.yRot = (float) Math.toRadians(h3);
        this.jaw5R.yRot = (float) Math.toRadians(h3);
        this.jaw1R.yRot = (float) Math.toRadians(h3);
        this.neck3R.yRot = (float) Math.toRadians(h3) / 8.0F;
        this.neck4R.yRot = (float) Math.toRadians(h3) / 4.0F;
        this.neck5R.yRot = (float) Math.toRadians(h3) / 2.0F;

        float Rjx = 0.0F;
        float jx = 0.0F;
        float Ljx = 0.0F;
        if (c.getAttacking() != 0) {
            newangle = Mth.cos(f2 * 0.9F * this.wingspeed) * (float) Math.PI * 0.1F;
            Ljx = 0.25F + newangle;
            newangle = Mth.cos(f2 * 1.1F * this.wingspeed) * (float) Math.PI * 0.1F;
            Rjx = 0.25F + newangle;
            newangle = Mth.cos(f2 * 1.3F * this.wingspeed) * (float) Math.PI * 0.1F;
            jx = 0.25F + newangle;
        } else {
            newangle = Mth.cos(f2 * 0.25F * this.wingspeed) * (float) Math.PI * 0.02F;
            Ljx = 0.1F + newangle;
            newangle = Mth.cos(f2 * 0.3F * this.wingspeed) * (float) Math.PI * 0.02F;
            Rjx = 0.1F + newangle;
            newangle = Mth.cos(f2 * 0.35F * this.wingspeed) * (float) Math.PI * 0.02F;
            jx = 0.1F + newangle;
        }

        this.head7.xRot = (float) Math.toRadians(d2);
        this.head3.xRot = (float) Math.toRadians(d2);
        this.headfin.xRot = (float) Math.toRadians(d2) + 0.5F;
        this.jaw5.xRot = (float) Math.toRadians(d2) + jx;
        this.jaw1.xRot = (float) Math.toRadians(d2) + jx;
        this.head7L.xRot = (float) Math.toRadians(d1);
        this.head3L.xRot = (float) Math.toRadians(d1);
        this.headfinL.xRot = (float) Math.toRadians(d1) + 0.5F;
        this.jaw5L.xRot = (float) Math.toRadians(d1) + Ljx;
        this.jaw1L.xRot = (float) Math.toRadians(d1) + Ljx;
        this.head7R.xRot = (float) Math.toRadians(d3);
        this.head3R.xRot = (float) Math.toRadians(d3);
        this.headfinR.xRot = (float) Math.toRadians(d3) + 0.5F;
        this.jaw5R.xRot = (float) Math.toRadians(d3) + Rjx;
        this.jaw1R.xRot = (float) Math.toRadians(d3) + Rjx;

        d1 = c.getHead1Ext();
        d2 = c.getHead2Ext();
        d3 = c.getHead3Ext();
        this.neck3L.xRot = -((float) Math.toRadians(d1 / 3.0));
        this.neck4L.xRot = -((float) Math.toRadians(d1 * 2.0 / 3.0));
        this.neck5L.xRot = -((float) Math.toRadians(d1));
        this.neck3.xRot = -((float) Math.toRadians(d2 / 3.0));
        this.neck4.xRot = -((float) Math.toRadians(d2 * 2.0 / 3.0));
        this.neck5.xRot = -((float) Math.toRadians(d2));
        this.neck3R.xRot = -((float) Math.toRadians(d3 / 3.0));
        this.neck4R.xRot = -((float) Math.toRadians(d3 * 2.0 / 3.0));
        this.neck5R.xRot = -((float) Math.toRadians(d3));

        this.neck4.y = this.neck3.y + (float) Math.sin(this.neck3.xRot) * 9.0F;
        this.neck4.z = this.neck3.z - (float) Math.cos(this.neck3.xRot) * 9.0F;
        this.neck4.x = this.neck3.x - (float) Math.sin(this.neck3.yRot) * 9.0F * (float) Math.cos(this.neck3.xRot);
        this.neck5.y = this.neck4.y + (float) Math.sin(this.neck4.xRot) * 9.0F;
        this.neck5.z = this.neck4.z - (float) Math.cos(this.neck4.xRot) * 9.0F;
        this.neck5.x = this.neck4.x - (float) Math.sin(this.neck4.yRot) * 9.0F * (float) Math.cos(this.neck4.xRot);
        this.head7.y = this.neck5.y + (float) Math.sin(this.neck5.xRot) * 9.0F;
        this.headfin.y = this.jaw5.y = this.jaw1.y = this.head3.y = this.head7.y;
        this.head7.z = this.neck5.z - (float) Math.cos(this.neck5.xRot) * 9.0F;
        this.headfin.z = this.jaw5.z = this.jaw1.z = this.head3.z = this.head7.z;
        this.head7.x = this.neck5.x - (float) Math.sin(this.neck5.yRot) * 9.0F * (float) Math.cos(this.neck5.xRot);
        this.headfin.x = this.jaw5.x = this.jaw1.x = this.head3.x = this.head7.x;

        this.neck4L.y = this.neck3L.y + (float) Math.sin(this.neck3L.xRot) * 9.0F;
        this.neck4L.z = this.neck3L.z - (float) Math.cos(this.neck3L.xRot) * 9.0F;
        this.neck4L.x = this.neck3L.x - (float) Math.sin(this.neck3L.yRot) * 9.0F * (float) Math.cos(this.neck3L.xRot);
        this.neck5L.y = this.neck4L.y + (float) Math.sin(this.neck4L.xRot) * 9.0F;
        this.neck5L.z = this.neck4L.z - (float) Math.cos(this.neck4L.xRot) * 9.0F;
        this.neck5L.x = this.neck4L.x - (float) Math.sin(this.neck4L.yRot) * 9.0F * (float) Math.cos(this.neck4L.xRot);
        this.head7L.y = this.neck5L.y + (float) Math.sin(this.neck5L.xRot) * 9.0F;
        this.headfinL.y = this.jaw5L.y = this.jaw1L.y = this.head3L.y = this.head7L.y;
        this.head7L.z = this.neck5L.z - (float) Math.cos(this.neck5L.xRot) * 9.0F;
        this.headfinL.z = this.jaw5L.z = this.jaw1L.z = this.head3L.z = this.head7L.z;
        this.head7L.x = this.neck5L.x - (float) Math.sin(this.neck5L.yRot) * 9.0F * (float) Math.cos(this.neck5L.xRot);
        this.headfinL.x = this.jaw5L.x = this.jaw1L.x = this.head3L.x = this.head7L.x;

        this.neck4R.y = this.neck3R.y + (float) Math.sin(this.neck3R.xRot) * 9.0F;
        this.neck4R.z = this.neck3R.z - (float) Math.cos(this.neck3R.xRot) * 9.0F;
        this.neck4R.x = this.neck3R.x - (float) Math.sin(this.neck3R.yRot) * 9.0F * (float) Math.cos(this.neck3R.xRot);
        this.neck5R.y = this.neck4R.y + (float) Math.sin(this.neck4R.xRot) * 9.0F;
        this.neck5R.z = this.neck4R.z - (float) Math.cos(this.neck4R.xRot) * 9.0F;
        this.neck5R.x = this.neck4R.x - (float) Math.sin(this.neck4R.yRot) * 9.0F * (float) Math.cos(this.neck4R.xRot);
        this.head7R.y = this.neck5R.y + (float) Math.sin(this.neck5R.xRot) * 9.0F;
        this.headfinR.y = this.jaw5R.y = this.jaw1R.y = this.head3R.y = this.head7R.y;
        this.head7R.z = this.neck5R.z - (float) Math.cos(this.neck5R.xRot) * 9.0F;
        this.headfinR.z = this.jaw5R.z = this.jaw1R.z = this.head3R.z = this.head7R.z;
        this.head7R.x = this.neck5R.x - (float) Math.sin(this.neck5R.yRot) * 9.0F * (float) Math.cos(this.neck5R.xRot);
        this.headfinR.x = this.jaw5R.x = this.jaw1R.x = this.head3R.x = this.head7R.x;

        c.setRenderInfo(r);
        // membranes rendered opaque (gold GL blend alpha skipped in HierarchicalModel)
    }

}
