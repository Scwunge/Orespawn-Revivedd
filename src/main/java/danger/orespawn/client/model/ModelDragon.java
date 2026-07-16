package danger.orespawn.client.model;

import danger.orespawn.entity.Dragon;
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
 * Port of gold {@code ModelDragon} (1.7.10 ModelBase, tex 256x128, 55 cubes).
 * Cubes/UVs/pivots/base rots 1:1. FULL setupAnim from gold func_78088_a.
 * Local LAYER_LOCATION; wingspeed default 0.65F matches ClientProxy.
 */
@OnlyIn(Dist.CLIENT)
public class ModelDragon extends HierarchicalModel<Dragon> {
    /** Model layer location. */
    public static final ModelLayerLocation LAYER_LOCATION =
            new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "dragon"), "main");

    private final float wingspeed;
    private final ModelPart root;
    private final ModelPart horn1;
    private final ModelPart horn2;
    private final ModelPart tail6;
    private final ModelPart wing15;
    private final ModelPart spike1;
    private final ModelPart spike2;
    private final ModelPart spike3;
    private final ModelPart spike4;
    private final ModelPart wing14;
    private final ModelPart spike5;
    private final ModelPart spike6;
    private final ModelPart spike7;
    private final ModelPart spike8;
    private final ModelPart spike9;
    private final ModelPart spike10;
    private final ModelPart head;
    private final ModelPart body;
    private final ModelPart leg1;
    private final ModelPart leg2;
    private final ModelPart leg3;
    private final ModelPart leg4;
    private final ModelPart body2;
    private final ModelPart neck1;
    private final ModelPart body3;
    private final ModelPart neck2;
    private final ModelPart neck3;
    private final ModelPart leg5;
    private final ModelPart leg6;
    private final ModelPart leg7;
    private final ModelPart leg9;
    private final ModelPart foot1;
    private final ModelPart foot2;
    private final ModelPart leg10;
    private final ModelPart leg11;
    private final ModelPart foot3;
    private final ModelPart foot4;
    private final ModelPart tail1;
    private final ModelPart tail2;
    private final ModelPart tail3;
    private final ModelPart mouth1;
    private final ModelPart mouth2;
    private final ModelPart tail5;
    private final ModelPart wing1;
    private final ModelPart wing2;
    private final ModelPart wing3;
    private final ModelPart wing4;
    private final ModelPart wing5;
    private final ModelPart wing6;
    private final ModelPart wing7;
    private final ModelPart wing8;
    private final ModelPart wing9;
    private final ModelPart wing10;
    private final ModelPart wing11;
    private final ModelPart wing12;
    private final ModelPart tail4;

    public ModelDragon(ModelPart root) {
        this(root, 0.65F);
    }

    public ModelDragon(ModelPart root, float wingspeed) {
        this.root = root;
        this.wingspeed = wingspeed;
        this.horn1 = root.getChild("horn1");
        this.horn2 = root.getChild("horn2");
        this.tail6 = root.getChild("tail6");
        this.wing15 = root.getChild("wing15");
        this.spike1 = root.getChild("spike1");
        this.spike2 = root.getChild("spike2");
        this.spike3 = root.getChild("spike3");
        this.spike4 = root.getChild("spike4");
        this.wing14 = root.getChild("wing14");
        this.spike5 = root.getChild("spike5");
        this.spike6 = root.getChild("spike6");
        this.spike7 = root.getChild("spike7");
        this.spike8 = root.getChild("spike8");
        this.spike9 = root.getChild("spike9");
        this.spike10 = root.getChild("spike10");
        this.head = root.getChild("head");
        this.body = root.getChild("body");
        this.leg1 = root.getChild("leg1");
        this.leg2 = root.getChild("leg2");
        this.leg3 = root.getChild("leg3");
        this.leg4 = root.getChild("leg4");
        this.body2 = root.getChild("body2");
        this.neck1 = root.getChild("neck1");
        this.body3 = root.getChild("body3");
        this.neck2 = root.getChild("neck2");
        this.neck3 = root.getChild("neck3");
        this.leg5 = root.getChild("leg5");
        this.leg6 = root.getChild("leg6");
        this.leg7 = root.getChild("leg7");
        this.leg9 = root.getChild("leg9");
        this.foot1 = root.getChild("foot1");
        this.foot2 = root.getChild("foot2");
        this.leg10 = root.getChild("leg10");
        this.leg11 = root.getChild("leg11");
        this.foot3 = root.getChild("foot3");
        this.foot4 = root.getChild("foot4");
        this.tail1 = root.getChild("tail1");
        this.tail2 = root.getChild("tail2");
        this.tail3 = root.getChild("tail3");
        this.mouth1 = root.getChild("mouth1");
        this.mouth2 = root.getChild("mouth2");
        this.tail5 = root.getChild("tail5");
        this.wing1 = root.getChild("wing1");
        this.wing2 = root.getChild("wing2");
        this.wing3 = root.getChild("wing3");
        this.wing4 = root.getChild("wing4");
        this.wing5 = root.getChild("wing5");
        this.wing6 = root.getChild("wing6");
        this.wing7 = root.getChild("wing7");
        this.wing8 = root.getChild("wing8");
        this.wing9 = root.getChild("wing9");
        this.wing10 = root.getChild("wing10");
        this.wing11 = root.getChild("wing11");
        this.wing12 = root.getChild("wing12");
        this.tail4 = root.getChild("tail4");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        root.addOrReplaceChild("horn1",
                CubeListBuilder.create().texOffs(0, 39).mirror()
                        .addBox(2.0F, -4.0F, 1.0F, 2.0F, 2.0F, 6.0F),
                PartPose.offsetAndRotation(0.0F, 6.0F, -23.0F, 0.4089647F, 0.2602503F, 0.0F));
        root.addOrReplaceChild("horn2",
                CubeListBuilder.create().texOffs(0, 39).mirror()
                        .addBox(-4.0F, -4.0F, 1.0F, 2.0F, 2.0F, 6.0F),
                PartPose.offsetAndRotation(0.0F, 6.0F, -23.0F, 0.4089647F, -0.2602503F, 0.0F));
        root.addOrReplaceChild("tail6",
                CubeListBuilder.create().texOffs(0, 49).mirror()
                        .addBox(-1.0F, 0.0F, -2.0F, 2.0F, 6.0F, 4.0F),
                PartPose.offsetAndRotation(0.0F, 7.0F, 43.0F, 1.570796F, 0.0F, 0.0F));
        root.addOrReplaceChild("wing15",
                CubeListBuilder.create().texOffs(0, 62).mirror()
                        .addBox(1.0F, -1.0F, 1.0F, 12.0F, 1.0F, 8.0F),
                PartPose.offsetAndRotation(4.0F, 3.0F, -5.0F, -0.0743572F, -0.4089594F, 0.0F));
        root.addOrReplaceChild("spike1",
                CubeListBuilder.create().texOffs(0, 73).mirror()
                        .addBox(-1.0F, -3.0F, -5.0F, 2.0F, 2.0F, 4.0F),
                PartPose.offset(0.0F, 6.0F, -17.0F));
        root.addOrReplaceChild("spike2",
                CubeListBuilder.create().texOffs(0, 73).mirror()
                        .addBox(-1.0F, -3.0F, -5.0F, 2.0F, 2.0F, 4.0F),
                PartPose.offset(0.0F, 6.0F, -11.0F));
        root.addOrReplaceChild("spike3",
                CubeListBuilder.create().texOffs(0, 73).mirror()
                        .addBox(-1.0F, -4.0F, 1.0F, 2.0F, 2.0F, 4.0F),
                PartPose.offset(0.0F, 7.0F, 25.0F));
        root.addOrReplaceChild("spike4",
                CubeListBuilder.create().texOffs(0, 73).mirror()
                        .addBox(-1.0F, -2.0F, 0.0F, 2.0F, 2.0F, 4.0F),
                PartPose.offset(0.0F, 3.0F, -7.0F));
        root.addOrReplaceChild("wing14",
                CubeListBuilder.create().texOffs(0, 62).mirror()
                        .addBox(-13.0F, -1.0F, 0.0F, 12.0F, 1.0F, 8.0F),
                PartPose.offsetAndRotation(-4.0F, 3.0F, -5.0F, -0.0698132F, 0.4089656F, 0.0F));
        root.addOrReplaceChild("spike5",
                CubeListBuilder.create().texOffs(0, 73).mirror()
                        .addBox(-1.0F, -2.0F, 0.0F, 2.0F, 2.0F, 4.0F),
                PartPose.offset(0.0F, 3.0F, -1.0F));
        root.addOrReplaceChild("spike6",
                CubeListBuilder.create().texOffs(0, 73).mirror()
                        .addBox(-1.0F, -2.0F, 0.0F, 2.0F, 2.0F, 4.0F),
                PartPose.offset(0.0F, 3.0F, 5.0F));
        root.addOrReplaceChild("spike7",
                CubeListBuilder.create().texOffs(0, 73).mirror()
                        .addBox(-1.0F, -4.0F, 1.0F, 2.0F, 2.0F, 4.0F),
                PartPose.offset(0.0F, 7.0F, 13.0F));
        root.addOrReplaceChild("spike8",
                CubeListBuilder.create().texOffs(0, 73).mirror()
                        .addBox(-1.0F, -2.0F, 1.0F, 2.0F, 2.0F, 4.0F),
                PartPose.offset(0.0F, 5.0F, 19.0F));
        root.addOrReplaceChild("spike9",
                CubeListBuilder.create().texOffs(0, 73).mirror()
                        .addBox(-1.0F, -4.0F, 1.0F, 2.0F, 2.0F, 4.0F),
                PartPose.offset(0.0F, 7.0F, 31.0F));
        root.addOrReplaceChild("spike10",
                CubeListBuilder.create().texOffs(0, 73).mirror()
                        .addBox(-1.0F, -4.0F, 2.0F, 2.0F, 2.0F, 4.0F),
                PartPose.offset(0.0F, 7.0F, 36.0F));
        root.addOrReplaceChild("head",
                CubeListBuilder.create().texOffs(0, 81).mirror()
                        .addBox(-4.0F, -4.0F, -8.0F, 8.0F, 8.0F, 8.0F),
                PartPose.offset(0.0F, 6.0F, -23.0F));
        root.addOrReplaceChild("body",
                CubeListBuilder.create().texOffs(1, 99).mirror()
                        .addBox(-6.0F, -10.0F, -7.0F, 12.0F, 18.0F, 10.0F),
                PartPose.offsetAndRotation(0.0F, 5.0F, 2.0F, 1.570796F, 0.0F, 0.0F));
        root.addOrReplaceChild("leg1",
                CubeListBuilder.create().texOffs(47, 112).mirror()
                        .addBox(-4.0F, -1.0F, -2.0F, 4.0F, 9.0F, 5.0F),
                PartPose.offsetAndRotation(-4.0F, 11.0F, 8.0F, -0.6320364F, 0.0F, 0.0F));
        root.addOrReplaceChild("leg2",
                CubeListBuilder.create().texOffs(47, 112).mirror()
                        .addBox(1.0F, -1.0F, -2.0F, 4.0F, 9.0F, 5.0F),
                PartPose.offsetAndRotation(3.0F, 11.0F, 8.0F, -0.6320364F, 0.0F, 0.0F));
        root.addOrReplaceChild("leg3",
                CubeListBuilder.create().texOffs(18, 47).mirror()
                        .addBox(-3.0F, -2.0F, -2.0F, 4.0F, 9.0F, 4.0F),
                PartPose.offsetAndRotation(-4.0F, 11.0F, -5.0F, 0.5576792F, 0.0F, 0.0F));
        root.addOrReplaceChild("leg4",
                CubeListBuilder.create().texOffs(18, 47).mirror()
                        .addBox(0.0F, -2.0F, -2.0F, 4.0F, 9.0F, 4.0F),
                PartPose.offsetAndRotation(3.0F, 11.0F, -5.0F, 0.5576792F, 0.0F, 0.0F));
        root.addOrReplaceChild("body2",
                CubeListBuilder.create().texOffs(68, 94).mirror()
                        .addBox(-5.0F, 0.0F, 0.0F, 10.0F, 22.0F, 10.0F),
                PartPose.offsetAndRotation(0.0F, 12.0F, -10.0F, 1.570796F, 0.0F, 0.0F));
        root.addOrReplaceChild("neck1",
                CubeListBuilder.create().texOffs(43, 85).mirror()
                        .addBox(-3.0F, -3.0F, 0.0F, 6.0F, 6.0F, 6.0F),
                PartPose.offset(0.0F, 7.0F, 25.0F));
        root.addOrReplaceChild("body3",
                CubeListBuilder.create().texOffs(70, 59).mirror()
                        .addBox(-4.0F, 0.0F, 0.0F, 8.0F, 24.0F, 8.0F),
                PartPose.offsetAndRotation(0.0F, 11.0F, -11.0F, 1.570796F, 0.0F, 0.0F));
        root.addOrReplaceChild("neck2",
                CubeListBuilder.create().texOffs(43, 85).mirror()
                        .addBox(-3.0F, -2.0F, -6.0F, 6.0F, 6.0F, 6.0F),
                PartPose.offset(0.0F, 6.0F, -11.0F));
        root.addOrReplaceChild("neck3",
                CubeListBuilder.create().texOffs(43, 85).mirror()
                        .addBox(-3.0F, -2.0F, -6.0F, 6.0F, 6.0F, 6.0F),
                PartPose.offset(0.0F, 6.0F, -17.0F));
        root.addOrReplaceChild("leg5",
                CubeListBuilder.create().texOffs(47, 99).mirror()
                        .addBox(0.0F, 3.0F, 3.0F, 4.0F, 8.0F, 4.0F),
                PartPose.offsetAndRotation(3.0F, 11.0F, -5.0F, -0.5576792F, 0.0F, 0.0F));
        root.addOrReplaceChild("leg6",
                CubeListBuilder.create().texOffs(47, 99).mirror()
                        .addBox(-3.0F, 3.0F, 3.0F, 4.0F, 8.0F, 4.0F),
                PartPose.offsetAndRotation(-4.0F, 11.0F, -5.0F, -0.5576792F, 0.0F, 0.0F));
        root.addOrReplaceChild("leg7",
                CubeListBuilder.create().texOffs(38, 73).mirror()
                        .addBox(1.0F, 2.0F, -8.0F, 4.0F, 5.0F, 4.0F),
                PartPose.offsetAndRotation(3.0F, 11.0F, 8.0F, 0.8922867F, 0.0F, 0.0F));
        root.addOrReplaceChild("leg9",
                CubeListBuilder.create().texOffs(38, 73).mirror()
                        .addBox(-4.0F, 2.0F, -8.0F, 4.0F, 5.0F, 4.0F),
                PartPose.offsetAndRotation(-4.0F, 11.0F, 8.0F, 0.8922867F, 0.0F, 0.0F));
        root.addOrReplaceChild("foot1",
                CubeListBuilder.create().texOffs(43, 63).mirror()
                        .addBox(-3.0F, 11.0F, -5.0F, 4.0F, 2.0F, 6.0F),
                PartPose.offset(-4.0F, 11.0F, -5.0F));
        root.addOrReplaceChild("foot2",
                CubeListBuilder.create().texOffs(43, 63).mirror()
                        .addBox(0.0F, 11.0F, -5.0F, 4.0F, 2.0F, 6.0F),
                PartPose.offset(3.0F, 11.0F, -5.0F));
        root.addOrReplaceChild("leg10",
                CubeListBuilder.create().texOffs(39, 52).mirror()
                        .addBox(1.0F, 6.0F, 2.0F, 4.0F, 5.0F, 4.0F),
                PartPose.offsetAndRotation(3.0F, 11.0F, 8.0F, -0.5576792F, 0.0F, 0.0F));
        root.addOrReplaceChild("leg11",
                CubeListBuilder.create().texOffs(39, 52).mirror()
                        .addBox(-4.0F, 6.0F, 2.0F, 4.0F, 5.0F, 4.0F),
                PartPose.offsetAndRotation(-4.0F, 11.0F, 8.0F, -0.5576792F, 0.0F, 0.0F));
        root.addOrReplaceChild("foot3",
                CubeListBuilder.create().texOffs(43, 63).mirror()
                        .addBox(1.0F, 11.0F, -7.0F, 4.0F, 2.0F, 6.0F),
                PartPose.offset(3.0F, 11.0F, 8.0F));
        root.addOrReplaceChild("foot4",
                CubeListBuilder.create().texOffs(43, 63).mirror()
                        .addBox(-4.0F, 11.0F, -7.0F, 4.0F, 2.0F, 6.0F),
                PartPose.offset(-4.0F, 11.0F, 8.0F));
        root.addOrReplaceChild("tail1",
                CubeListBuilder.create().texOffs(43, 85).mirror()
                        .addBox(-3.0F, -3.0F, 0.0F, 6.0F, 6.0F, 6.0F),
                PartPose.offset(0.0F, 7.0F, 13.0F));
        root.addOrReplaceChild("tail2",
                CubeListBuilder.create().texOffs(43, 85).mirror()
                        .addBox(-3.0F, -3.0F, 0.0F, 6.0F, 6.0F, 6.0F),
                PartPose.offset(0.0F, 7.0F, 19.0F));
        root.addOrReplaceChild("tail3",
                CubeListBuilder.create().texOffs(56, 45).mirror()
                        .addBox(-3.0F, -3.0F, 0.0F, 4.0F, 6.0F, 6.0F),
                PartPose.offset(1.0F, 7.0F, 31.0F));
        root.addOrReplaceChild("mouth1",
                CubeListBuilder.create().texOffs(90, 22).mirror()
                        .addBox(-3.0F, -1.0F, -15.0F, 6.0F, 3.0F, 8.0F),
                PartPose.offset(0.0F, 6.0F, -23.0F));
        root.addOrReplaceChild("mouth2",
                CubeListBuilder.create().texOffs(90, 6).mirror()
                        .addBox(-2.0F, 1.0F, -5.0F, 4.0F, 2.0F, 8.0F),
                PartPose.offsetAndRotation(0.0F, 7.0F, -32.0F, 0.0698132F, 0.0F, 0.0F));
        root.addOrReplaceChild("tail5",
                CubeListBuilder.create().texOffs(87, 36).mirror()
                        .addBox(0.0F, 0.0F, -5.0F, 0.0F, 11.0F, 10.0F),
                PartPose.offsetAndRotation(0.0F, 7.0F, 49.0F, 1.570796F, 0.0F, 0.0F));
        root.addOrReplaceChild("wing1",
                CubeListBuilder.create().texOffs(26, 40).mirror()
                        .addBox(0.0F, -1.0F, -1.0F, 11.0F, 2.0F, 2.0F),
                PartPose.offsetAndRotation(4.0F, 3.0F, -5.0F, 0.0F, -0.4833219F, 0.0F));
        root.addOrReplaceChild("wing2",
                CubeListBuilder.create().texOffs(110, 88).mirror()
                        .addBox(-1.0F, -1.0F, 0.0F, 2.0F, 2.0F, 36.0F),
                PartPose.offsetAndRotation(19.0F, 3.0F, -23.0F, 0.0F, 1.041001F, 0.0F));
        root.addOrReplaceChild("wing3",
                CubeListBuilder.create().texOffs(109, 60).mirror()
                        .addBox(-1.0F, -1.0F, -24.0F, 2.0F, 2.0F, 24.0F),
                PartPose.offsetAndRotation(12.0F, 3.0F, -1.0F, -0.0090881F, -0.3497888F, 0.0F));
        root.addOrReplaceChild("wing4",
                CubeListBuilder.create().texOffs(26, 40).mirror()
                        .addBox(-11.0F, -1.0F, -1.0F, 11.0F, 2.0F, 2.0F),
                PartPose.offsetAndRotation(-4.0F, 3.0F, -5.0F, 0.0F, 0.4833166F, 0.0F));
        root.addOrReplaceChild("wing5",
                CubeListBuilder.create().texOffs(109, 60).mirror()
                        .addBox(-1.0F, -1.0F, -24.0F, 2.0F, 2.0F, 24.0F),
                PartPose.offsetAndRotation(-12.0F, 3.0F, -1.0F, -0.0090932F, 0.3323281F, 0.0F));
        root.addOrReplaceChild("wing6",
                CubeListBuilder.create().texOffs(110, 88).mirror()
                        .addBox(-1.0F, -1.0F, 0.0F, 2.0F, 2.0F, 36.0F),
                PartPose.offsetAndRotation(-20.0F, 3.0F, -23.0F, 0.0F, -1.041002F, 0.0F));
        root.addOrReplaceChild("wing7",
                CubeListBuilder.create().texOffs(124, 21).mirror()
                        .addBox(-8.0F, 0.0F, 1.0F, 8.0F, 1.0F, 36.0F),
                PartPose.offsetAndRotation(19.0F, 2.0F, -23.0F, 0.0F, 1.041001F, 0.0F));
        root.addOrReplaceChild("wing8",
                CubeListBuilder.create().texOffs(122, 10).mirror()
                        .addBox(-11.0F, -1.0F, 0.0F, 28.0F, 1.0F, 8.0F),
                PartPose.offsetAndRotation(12.0F, 3.0F, -1.0F, 0.002272F, 1.264073F, -0.0174533F));
        root.addOrReplaceChild("wing9",
                CubeListBuilder.create().texOffs(0, 10).mirror()
                        .addBox(-25.0F, -1.0F, 7.0F, 18.0F, 1.0F, 26.0F),
                PartPose.offsetAndRotation(19.0F, 3.0F, -23.0F, 0.002272F, 1.264073F, 0.0F));
        root.addOrReplaceChild("wing10",
                CubeListBuilder.create().texOffs(122, 10).mirror()
                        .addBox(-23.0F, -1.0F, 0.0F, 33.0F, 1.0F, 8.0F),
                PartPose.offsetAndRotation(-12.0F, 3.0F, -1.0F, -0.0022689F, -1.226894F, 0.0F));
        root.addOrReplaceChild("wing11",
                CubeListBuilder.create().texOffs(124, 21).mirror()
                        .addBox(0.0F, -1.0F, 1.0F, 8.0F, 1.0F, 36.0F),
                PartPose.offsetAndRotation(-20.0F, 3.0F, -23.0F, 0.0F, -1.041002F, 0.0F));
        root.addOrReplaceChild("wing12",
                CubeListBuilder.create().texOffs(0, 10).mirror()
                        .addBox(7.0F, -1.0F, 7.0F, 18.0F, 1.0F, 26.0F),
                PartPose.offsetAndRotation(-20.0F, 3.0F, -23.0F, 0.002272F, -1.264072F, 0.0F));
        root.addOrReplaceChild("tail4",
                CubeListBuilder.create().texOffs(56, 45).mirror()
                        .addBox(-3.0F, -3.0F, 0.0F, 4.0F, 6.0F, 6.0F),
                PartPose.offset(1.0F, 7.0F, 37.0F));
        return LayerDefinition.create(mesh, 256, 128);
    }

    @Override
    public ModelPart root() {
        return this.root;
    }

    @Override
    public void setupAnim(Dragon entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.animate(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
    }

    /** Full gold func_78088_a (empty func_78087_a). */
    private void animate(Dragon e, float f, float f1, float f2, float f3, float f4) {
        float newangle = 0.0F;
        float lspeed = 0.0F;
        RenderInfo r = e.getRenderInfo();
        float tailspeed = 0.76F;
        float tailamp = 0.45F;

        if (f1 > 0.001F) {
            lspeed = (float) ((e.xo - e.getX()) * (e.xo - e.getX()) + (e.zo - e.getZ()) * (e.zo - e.getZ()));
            lspeed = (float) Math.sqrt(lspeed);
            newangle = Mth.cos(f2 * 1.25F * this.wingspeed) * (float) Math.PI * lspeed * 0.6F;
        } else {
            newangle = 0.0F;
        }

        if (e.getActivity() != 0) {
            newangle = 1.0F;
            this.leg4.xRot = 0.557F - newangle;
            this.leg5.xRot = -0.557F - newangle;
            this.foot2.xRot = -newangle;
            this.leg3.xRot = 0.557F - newangle;
            this.leg6.xRot = -0.557F - newangle;
            this.foot1.xRot = -newangle;
            this.leg2.xRot = -0.632F + newangle;
            this.leg7.xRot = 0.89F + newangle;
            this.leg10.xRot = -0.557F + newangle;
            this.foot3.xRot = newangle;
            this.leg1.xRot = -0.632F + newangle;
            this.leg9.xRot = 0.89F + newangle;
            this.leg11.xRot = -0.557F + newangle;
            this.foot4.xRot = newangle;
        } else {
            this.leg4.xRot = 0.557F + newangle;
            this.leg5.xRot = -0.557F + newangle;
            this.foot2.xRot = newangle;
            this.leg3.xRot = 0.557F - newangle;
            this.leg6.xRot = -0.557F - newangle;
            this.foot1.xRot = -newangle;
            this.leg2.xRot = -0.632F - newangle;
            this.leg7.xRot = 0.89F - newangle;
            this.leg10.xRot = -0.557F - newangle;
            this.foot3.xRot = -newangle;
            this.leg1.xRot = -0.632F + newangle;
            this.leg9.xRot = 0.89F + newangle;
            this.leg11.xRot = -0.557F + newangle;
            this.foot4.xRot = newangle;
        }

        if (e.getAttacking() != 0) {
            if (e.getActivity() != 0) {
                newangle = Mth.cos(f2 * 0.75F * this.wingspeed) * (float) Math.PI * 0.28F;
            } else {
                newangle = -0.45F + Mth.cos(f2 * 0.85F * this.wingspeed) * (float) Math.PI * 0.2F;
            }
        } else if (e.getActivity() != 0) {
            newangle = Mth.cos(f2 * 0.75F * this.wingspeed) * (float) Math.PI * 0.28F;
        } else {
            newangle = -0.85F + Mth.cos(f2 * 0.2F * this.wingspeed) * (float) Math.PI * 0.028F;
        }

        this.wing1.zRot = newangle;
        this.wing15.zRot = newangle;
        this.wing3.zRot = newangle * 4.0F / 3.0F;
        this.wing3.y = this.wing1.y + (float) Math.sin(this.wing1.zRot) * 7.0F;
        this.wing3.x = this.wing1.x + (float) Math.cos(this.wing1.zRot) * 7.0F;
        this.wing8.zRot = newangle * 4.0F / 3.0F;
        this.wing8.y = this.wing3.y;
        this.wing8.x = this.wing3.x;
        this.wing2.zRot = newangle * 3.0F / 2.0F;
        this.wing2.y = this.wing3.y + (float) Math.sin(this.wing3.zRot) * 6.0F;
        this.wing2.x = this.wing3.x + (float) Math.cos(this.wing3.zRot) * 6.0F;
        this.wing7.zRot = newangle * 3.0F / 2.0F;
        this.wing7.y = this.wing2.y;
        this.wing7.x = this.wing2.x;
        this.wing9.zRot = newangle * 3.0F / 2.0F;
        this.wing9.y = this.wing2.y;
        this.wing9.x = this.wing2.x;
        this.wing4.zRot = -newangle;
        this.wing14.zRot = -newangle;
        this.wing5.zRot = -newangle * 4.0F / 3.0F;
        this.wing5.y = this.wing4.y - (float) Math.sin(this.wing4.zRot) * 7.0F;
        this.wing5.x = this.wing4.x - (float) Math.cos(this.wing4.zRot) * 7.0F;
        this.wing10.zRot = -newangle * 4.0F / 3.0F;
        this.wing10.y = this.wing5.y;
        this.wing10.x = this.wing5.x;
        this.wing6.zRot = -newangle * 3.0F / 2.0F;
        this.wing6.y = this.wing5.y - (float) Math.sin(this.wing5.zRot) * 6.0F;
        this.wing6.x = this.wing5.x - (float) Math.cos(this.wing5.zRot) * 6.0F;
        this.wing11.zRot = -newangle * 3.0F / 2.0F;
        this.wing11.y = this.wing6.y;
        this.wing11.x = this.wing6.x;
        this.wing12.zRot = -newangle * 3.0F / 2.0F;
        this.wing12.y = this.wing6.y;
        this.wing12.x = this.wing6.x;

        if (e.getAttacking() != 0) {
            tailspeed = 0.96F;
            tailamp = 0.75F;
        }
        if (e.getActivity() == 0 && e.getAttacking() == 0) {
            tailspeed = 0.22F;
            tailamp = 0.22F;
        }
        if (e.isOreSpawnSitting()) {
            tailspeed = 0.0F;
            tailamp = 0.0F;
        }

        this.tail1.yRot = Mth.cos(f2 * tailspeed * this.wingspeed) * (float) Math.PI * 0.04F;
        this.spike10.z = this.tail1.z;
        this.spike10.x = this.tail1.x;
        this.spike10.yRot = this.tail1.yRot;
        this.tail2.z = this.tail1.z + (float) Math.cos(this.tail1.yRot) * 6.0F;
        this.tail2.x = this.tail1.x + (float) Math.sin(this.tail1.yRot) * 6.0F;
        this.tail2.yRot = Mth.cos(f2 * tailspeed * this.wingspeed) * (float) Math.PI * tailamp * 0.125F;
        this.spike7.z = this.tail2.z;
        this.spike7.x = this.tail2.x;
        this.spike7.yRot = this.tail2.yRot;
        this.neck1.z = this.tail2.z + (float) Math.cos(this.tail2.yRot) * 6.0F;
        this.neck1.x = this.tail2.x + (float) Math.sin(this.tail2.yRot) * 6.0F;
        this.neck1.yRot = Mth.cos(f2 * tailspeed * this.wingspeed) * (float) Math.PI * tailamp * 0.25F;
        this.spike8.z = this.neck1.z;
        this.spike8.x = this.neck1.x;
        this.spike8.yRot = this.neck1.yRot;
        this.tail3.z = this.neck1.z + (float) Math.cos(this.neck1.yRot) * 6.0F;
        this.tail3.x = this.neck1.x + 1.0F + (float) Math.sin(this.neck1.yRot) * 6.0F;
        this.tail3.yRot = Mth.cos(f2 * tailspeed * this.wingspeed) * (float) Math.PI * tailamp * 0.375F;
        this.spike3.z = this.tail3.z;
        this.spike3.x = this.tail3.x - 1.0F;
        this.spike3.yRot = this.tail3.yRot;
        this.tail4.z = this.tail3.z + (float) Math.cos(this.tail3.yRot) * 6.0F;
        this.tail4.x = this.tail3.x + (float) Math.sin(this.tail3.yRot) * 6.0F;
        this.tail4.yRot = Mth.cos(f2 * tailspeed * this.wingspeed) * (float) Math.PI * tailamp * 0.5F;
        this.spike9.z = this.tail4.z;
        this.spike9.x = this.tail4.x - 1.0F;
        this.spike9.yRot = this.tail4.yRot;
        this.tail6.z = this.tail4.z + (float) Math.cos(this.tail4.yRot) * 6.0F;
        this.tail6.x = this.tail4.x - 1.0F + (float) Math.sin(this.tail4.yRot) * 6.0F;
        this.tail6.yRot = Mth.cos(f2 * tailspeed * this.wingspeed) * (float) Math.PI * tailamp * 0.625F;
        this.tail5.z = this.tail6.z + (float) Math.cos(this.tail6.yRot) * 6.0F;
        this.tail5.x = this.tail6.x - 0.5F + (float) Math.sin(this.tail6.yRot) * 6.0F;
        this.tail5.yRot = Mth.cos(f2 * tailspeed * this.wingspeed) * (float) Math.PI * tailamp * 0.75F;

        if (e.getActivity() == 1) {
            f3 = (e.yRotO - e.getYRot()) * 8.0F;
            f3 = -f3;
            r.rf1 = r.rf1 + (f3 - r.rf1) / 60.0F;
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

        this.neck2.yRot = (float) Math.toRadians(f3) * 0.25F;
        this.spike2.yRot = this.neck2.yRot;
        this.neck3.z = this.neck2.z - (float) Math.cos(this.neck2.yRot) * 6.0F;
        this.neck3.x = this.neck2.x - (float) Math.sin(this.neck2.yRot) * 6.0F;
        this.neck3.yRot = (float) Math.toRadians(f3) * 0.5F;
        this.spike1.z = this.neck3.z;
        this.spike1.x = this.neck3.x;
        this.spike1.yRot = this.neck3.yRot;
        this.head.z = this.neck3.z - (float) Math.cos(this.neck3.yRot) * 6.0F;
        this.head.x = this.neck3.x - (float) Math.sin(this.neck3.yRot) * 6.0F;
        this.head.yRot = (float) Math.toRadians(f3) * 0.75F;
        this.mouth1.z = this.head.z;
        this.mouth1.x = this.head.x;
        this.mouth1.yRot = this.head.yRot;
        this.horn1.z = this.head.z;
        this.horn1.x = this.head.x;
        this.horn1.yRot = this.head.yRot + 0.26F;
        this.horn2.z = this.head.z;
        this.horn2.x = this.head.x;
        this.horn2.yRot = this.head.yRot - 0.26F;
        this.mouth2.z = this.head.z - (float) Math.cos(this.head.yRot) * 9.0F;
        this.mouth2.x = this.head.x - (float) Math.sin(this.head.yRot) * 9.0F;
        this.mouth2.yRot = this.head.yRot;
        newangle = Mth.cos(f2 * 1.5F * this.wingspeed) * (float) Math.PI * 0.14F;
        if (e.getAttacking() != 0) {
            this.mouth2.xRot = 0.4F + newangle;
        } else {
            this.mouth2.xRot = 0.07F;
        }

        e.setRenderInfo(r);
    }
}
