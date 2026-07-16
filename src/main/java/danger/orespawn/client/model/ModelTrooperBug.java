package danger.orespawn.client.model;

import danger.orespawn.entity.TrooperBug;
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
 * Port of gold {@code ModelTrooperBug} (1.7.10 ModelBase, tex 512×256) → 1.21 HierarchicalModel.
 * Cube sizes/UVs/offsets/base rotations 1:1. Wingspeed default 0.22 matches ClientProxy.
 * Full leg walk + jaw/arm/antenna attack anim from gold {@code func_78088_a}.
 */
@OnlyIn(Dist.CLIENT)
public class ModelTrooperBug extends HierarchicalModel<TrooperBug> {
    /** Model layer location. */
    public static final ModelLayerLocation LAYER_LOCATION =
            new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "trooper_bug"), "main");

    private final float wingspeed;
    private final ModelPart root;
    private final ModelPart legintersection;
    private final ModelPart legintersectionb;
    private final ModelPart leg1start;
    private final ModelPart leg1shoulder;
    private final ModelPart leg1shoulderpart2;
    private final ModelPart leg1part1;
    private final ModelPart leg1part1b;
    private final ModelPart leg1elbow;
    private final ModelPart leg1part2;
    private final ModelPart leg1part2b;
    private final ModelPart leg1part2c;
    private final ModelPart leg1part3;
    private final ModelPart leg1part3b;
    private final ModelPart leg1part3c;
    private final ModelPart leg1part3d;
    private final ModelPart leg2start;
    private final ModelPart leg2shoulder;
    private final ModelPart leg2shoulderpart2;
    private final ModelPart leg2part1;
    private final ModelPart leg2part1b;
    private final ModelPart leg2elbow;
    private final ModelPart leg2part2;
    private final ModelPart leg2part2b;
    private final ModelPart leg2part2c;
    private final ModelPart leg2part3;
    private final ModelPart leg2part3b;
    private final ModelPart leg2part3c;
    private final ModelPart leg2part3d;
    private final ModelPart leg3start;
    private final ModelPart leg3shoulder;
    private final ModelPart leg3shoulderpart2;
    private final ModelPart leg3part1;
    private final ModelPart leg3part1b;
    private final ModelPart leg3elbow;
    private final ModelPart leg3part2;
    private final ModelPart leg3part2b;
    private final ModelPart leg3part2c;
    private final ModelPart leg3part3;
    private final ModelPart leg3part3b;
    private final ModelPart leg3part3c;
    private final ModelPart leg3part3d;
    private final ModelPart leg4start;
    private final ModelPart leg4shoulder;
    private final ModelPart leg4shoulderpart2;
    private final ModelPart leg4part1;
    private final ModelPart leg4part1b;
    private final ModelPart leg4elbow;
    private final ModelPart leg4part2;
    private final ModelPart leg4part2b;
    private final ModelPart leg4part2c;
    private final ModelPart leg4part3;
    private final ModelPart leg4part3b;
    private final ModelPart leg4part3c;
    private final ModelPart leg4part3d;
    private final ModelPart jawbase;
    private final ModelPart jawbase2;
    private final ModelPart jawbase3;
    private final ModelPart jawbase4;
    private final ModelPart jawbase5;
    private final ModelPart jawbase6;
    private final ModelPart jawbase7;
    private final ModelPart jawbase8;
    private final ModelPart jawbase9;
    private final ModelPart jawleft;
    private final ModelPart jawright;
    private final ModelPart jawend;
    private final ModelPart headstart;
    private final ModelPart headbase;
    private final ModelPart headbase2;
    private final ModelPart headbase3;
    private final ModelPart headbase4;
    private final ModelPart headbase5;
    private final ModelPart headbase6;
    private final ModelPart headbase7;
    private final ModelPart headbase8;
    private final ModelPart headbase9;
    private final ModelPart headbase10;
    private final ModelPart headleftridge;
    private final ModelPart headrightridge;
    private final ModelPart antenna1;
    private final ModelPart antenna1part2;
    private final ModelPart antenna2;
    private final ModelPart antenna2part2;
    private final ModelPart eyebase1;
    private final ModelPart eye1;
    private final ModelPart eyebase2;
    private final ModelPart eye2;
    private final ModelPart arm1start;
    private final ModelPart arm1part1;
    private final ModelPart arm1part2;
    private final ModelPart arm1part2b;
    private final ModelPart arm1part2c;
    private final ModelPart arm1part3;
    private final ModelPart arm1part3b;
    private final ModelPart arm2start;
    private final ModelPart arm2part1;
    private final ModelPart arm2part2;
    private final ModelPart arm2part2b;
    private final ModelPart arm2part2c;
    private final ModelPart arm2part3;
    private final ModelPart arm2part3b;
    private final ModelPart innermouth;
    private final ModelPart innermouth2;
    private final ModelPart tooth1;
    private final ModelPart tooth2;
    private final ModelPart tooth3;
    private final ModelPart tooth4;
    private final ModelPart tooth5;
    private final ModelPart tooth6;
    private final ModelPart mouthedge1;
    private final ModelPart mouthedge2;
    private final ModelPart mouthedge3;
    private final ModelPart mouthedge4;
    private final ModelPart arm3start;
    private final ModelPart arm3part1;
    private final ModelPart arm3part1b;
    private final ModelPart arm3part1c;
    private final ModelPart arm4start;
    private final ModelPart arm4part1;
    private final ModelPart arm4part1b;
    private final ModelPart arm4part1c;
    private final ModelPart tongue;
    private final ModelPart tonguepart2;
    private final ModelPart upperjawridgeleft;
    private final ModelPart upperjawridgeright;
    private final ModelPart upperjawbaseleft;
    private final ModelPart upperjawbaseright;
    private final ModelPart upperjawbase;
    private final ModelPart upperjawbase2;
    private final ModelPart upperjawbase3;
    private final ModelPart upperjawbase4;
    private final ModelPart upperjawend;
    private final ModelPart upperjawleftend;
    private final ModelPart upperjawrightend;

    public ModelTrooperBug(ModelPart root) {
        this(root, 0.22F);
    }

    public ModelTrooperBug(ModelPart root, float wingspeed) {
        this.root = root;
        this.wingspeed = wingspeed;
        this.legintersection = root.getChild("legintersection");
        this.legintersectionb = root.getChild("legintersectionb");
        this.leg1start = root.getChild("leg1start");
        this.leg1shoulder = root.getChild("leg1shoulder");
        this.leg1shoulderpart2 = root.getChild("leg1shoulderpart2");
        this.leg1part1 = root.getChild("leg1part1");
        this.leg1part1b = root.getChild("leg1part1b");
        this.leg1elbow = root.getChild("leg1elbow");
        this.leg1part2 = root.getChild("leg1part2");
        this.leg1part2b = root.getChild("leg1part2b");
        this.leg1part2c = root.getChild("leg1part2c");
        this.leg1part3 = root.getChild("leg1part3");
        this.leg1part3b = root.getChild("leg1part3b");
        this.leg1part3c = root.getChild("leg1part3c");
        this.leg1part3d = root.getChild("leg1part3d");
        this.leg2start = root.getChild("leg2start");
        this.leg2shoulder = root.getChild("leg2shoulder");
        this.leg2shoulderpart2 = root.getChild("leg2shoulderpart2");
        this.leg2part1 = root.getChild("leg2part1");
        this.leg2part1b = root.getChild("leg2part1b");
        this.leg2elbow = root.getChild("leg2elbow");
        this.leg2part2 = root.getChild("leg2part2");
        this.leg2part2b = root.getChild("leg2part2b");
        this.leg2part2c = root.getChild("leg2part2c");
        this.leg2part3 = root.getChild("leg2part3");
        this.leg2part3b = root.getChild("leg2part3b");
        this.leg2part3c = root.getChild("leg2part3c");
        this.leg2part3d = root.getChild("leg2part3d");
        this.leg3start = root.getChild("leg3start");
        this.leg3shoulder = root.getChild("leg3shoulder");
        this.leg3shoulderpart2 = root.getChild("leg3shoulderpart2");
        this.leg3part1 = root.getChild("leg3part1");
        this.leg3part1b = root.getChild("leg3part1b");
        this.leg3elbow = root.getChild("leg3elbow");
        this.leg3part2 = root.getChild("leg3part2");
        this.leg3part2b = root.getChild("leg3part2b");
        this.leg3part2c = root.getChild("leg3part2c");
        this.leg3part3 = root.getChild("leg3part3");
        this.leg3part3b = root.getChild("leg3part3b");
        this.leg3part3c = root.getChild("leg3part3c");
        this.leg3part3d = root.getChild("leg3part3d");
        this.leg4start = root.getChild("leg4start");
        this.leg4shoulder = root.getChild("leg4shoulder");
        this.leg4shoulderpart2 = root.getChild("leg4shoulderpart2");
        this.leg4part1 = root.getChild("leg4part1");
        this.leg4part1b = root.getChild("leg4part1b");
        this.leg4elbow = root.getChild("leg4elbow");
        this.leg4part2 = root.getChild("leg4part2");
        this.leg4part2b = root.getChild("leg4part2b");
        this.leg4part2c = root.getChild("leg4part2c");
        this.leg4part3 = root.getChild("leg4part3");
        this.leg4part3b = root.getChild("leg4part3b");
        this.leg4part3c = root.getChild("leg4part3c");
        this.leg4part3d = root.getChild("leg4part3d");
        this.jawbase = root.getChild("jawbase");
        this.jawbase2 = root.getChild("jawbase2");
        this.jawbase3 = root.getChild("jawbase3");
        this.jawbase4 = root.getChild("jawbase4");
        this.jawbase5 = root.getChild("jawbase5");
        this.jawbase6 = root.getChild("jawbase6");
        this.jawbase7 = root.getChild("jawbase7");
        this.jawbase8 = root.getChild("jawbase8");
        this.jawbase9 = root.getChild("jawbase9");
        this.jawleft = root.getChild("jawleft");
        this.jawright = root.getChild("jawright");
        this.jawend = root.getChild("jawend");
        this.headstart = root.getChild("headstart");
        this.headbase = root.getChild("headbase");
        this.headbase2 = root.getChild("headbase2");
        this.headbase3 = root.getChild("headbase3");
        this.headbase4 = root.getChild("headbase4");
        this.headbase5 = root.getChild("headbase5");
        this.headbase6 = root.getChild("headbase6");
        this.headbase7 = root.getChild("headbase7");
        this.headbase8 = root.getChild("headbase8");
        this.headbase9 = root.getChild("headbase9");
        this.headbase10 = root.getChild("headbase10");
        this.headleftridge = root.getChild("headleftridge");
        this.headrightridge = root.getChild("headrightridge");
        this.antenna1 = root.getChild("antenna1");
        this.antenna1part2 = root.getChild("antenna1part2");
        this.antenna2 = root.getChild("antenna2");
        this.antenna2part2 = root.getChild("antenna2part2");
        this.eyebase1 = root.getChild("eyebase1");
        this.eye1 = root.getChild("eye1");
        this.eyebase2 = root.getChild("eyebase2");
        this.eye2 = root.getChild("eye2");
        this.arm1start = root.getChild("arm1start");
        this.arm1part1 = root.getChild("arm1part1");
        this.arm1part2 = root.getChild("arm1part2");
        this.arm1part2b = root.getChild("arm1part2b");
        this.arm1part2c = root.getChild("arm1part2c");
        this.arm1part3 = root.getChild("arm1part3");
        this.arm1part3b = root.getChild("arm1part3b");
        this.arm2start = root.getChild("arm2start");
        this.arm2part1 = root.getChild("arm2part1");
        this.arm2part2 = root.getChild("arm2part2");
        this.arm2part2b = root.getChild("arm2part2b");
        this.arm2part2c = root.getChild("arm2part2c");
        this.arm2part3 = root.getChild("arm2part3");
        this.arm2part3b = root.getChild("arm2part3b");
        this.innermouth = root.getChild("innermouth");
        this.innermouth2 = root.getChild("innermouth2");
        this.tooth1 = root.getChild("tooth1");
        this.tooth2 = root.getChild("tooth2");
        this.tooth3 = root.getChild("tooth3");
        this.tooth4 = root.getChild("tooth4");
        this.tooth5 = root.getChild("tooth5");
        this.tooth6 = root.getChild("tooth6");
        this.mouthedge1 = root.getChild("mouthedge1");
        this.mouthedge2 = root.getChild("mouthedge2");
        this.mouthedge3 = root.getChild("mouthedge3");
        this.mouthedge4 = root.getChild("mouthedge4");
        this.arm3start = root.getChild("arm3start");
        this.arm3part1 = root.getChild("arm3part1");
        this.arm3part1b = root.getChild("arm3part1b");
        this.arm3part1c = root.getChild("arm3part1c");
        this.arm4start = root.getChild("arm4start");
        this.arm4part1 = root.getChild("arm4part1");
        this.arm4part1b = root.getChild("arm4part1b");
        this.arm4part1c = root.getChild("arm4part1c");
        this.tongue = root.getChild("tongue");
        this.tonguepart2 = root.getChild("tonguepart2");
        this.upperjawridgeleft = root.getChild("upperjawridgeleft");
        this.upperjawridgeright = root.getChild("upperjawridgeright");
        this.upperjawbaseleft = root.getChild("upperjawbaseleft");
        this.upperjawbaseright = root.getChild("upperjawbaseright");
        this.upperjawbase = root.getChild("upperjawbase");
        this.upperjawbase2 = root.getChild("upperjawbase2");
        this.upperjawbase3 = root.getChild("upperjawbase3");
        this.upperjawbase4 = root.getChild("upperjawbase4");
        this.upperjawend = root.getChild("upperjawend");
        this.upperjawleftend = root.getChild("upperjawleftend");
        this.upperjawrightend = root.getChild("upperjawrightend");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        root.addOrReplaceChild("legintersection",
                CubeListBuilder.create().texOffs(0, 0).mirror()
                        .addBox(-9.0F, -2.0F, -9.0F, 18.0F, 4.0F, 18.0F),
                PartPose.offset(0.0F, 0.0F, 0.0F));
        root.addOrReplaceChild("legintersectionb",
                CubeListBuilder.create().texOffs(0, 156).mirror()
                        .addBox(-9.0F, -2.0F, -16.0F, 18.0F, 4.0F, 18.0F),
                PartPose.offsetAndRotation(0.0F, 0.0F, 7.0F, 0.2230717F, 0.0F, 0.0F));
        root.addOrReplaceChild("leg1start",
                CubeListBuilder.create().texOffs(55, 0).mirror()
                        .addBox(-3.0F, -2.5F, -4.0F, 6.0F, 5.0F, 5.0F),
                PartPose.offsetAndRotation(-7.0F, 0.0F, -7.0F, 0.0F, (float) (Math.PI / 4), 0.0F));
        root.addOrReplaceChild("leg1shoulder",
                CubeListBuilder.create().texOffs(77, 0).mirror()
                        .addBox(-2.5F, -4.5F, -6.0F, 5.0F, 6.0F, 10.0F),
                PartPose.offsetAndRotation(-10.0F, 0.0F, -10.0F, -0.5576792F, (float) (Math.PI / 4), 0.0F));
        root.addOrReplaceChild("leg1shoulderpart2",
                CubeListBuilder.create().texOffs(70, 18).mirror()
                        .addBox(-1.5F, -2.5F, -11.0F, 3.0F, 3.0F, 6.0F),
                PartPose.offsetAndRotation(-10.0F, 0.0F, -10.0F, -0.4089647F, (float) (Math.PI / 4), 0.0F));
        root.addOrReplaceChild("leg1part1",
                CubeListBuilder.create().texOffs(68, 17).mirror()
                        .addBox(-2.0F, -2.5F, -23.0F, 4.0F, 3.0F, 26.0F),
                PartPose.offsetAndRotation(-17.0F, -5.0F, -17.0F, 1.115358F, (float) (Math.PI / 4), 0.0F));
        root.addOrReplaceChild("leg1part1b",
                CubeListBuilder.create().texOffs(19, 23).mirror()
                        .addBox(-1.5F, 0.5F, -23.0F, 3.0F, 3.0F, 21.0F),
                PartPose.offsetAndRotation(-17.0F, -5.0F, -17.0F, 1.07818F, (float) (Math.PI / 4), 0.0F));
        root.addOrReplaceChild("leg1elbow",
                CubeListBuilder.create().texOffs(68, 28).mirror()
                        .addBox(-1.5F, -5.5F, -22.0F, 3.0F, 6.0F, 3.0F),
                PartPose.offsetAndRotation(-17.0F, -5.0F, -17.0F, 1.115358F, (float) (Math.PI / 4), 0.0F));
        root.addOrReplaceChild("leg1part2",
                CubeListBuilder.create().texOffs(48, 47).mirror()
                        .addBox(-3.0F, -2.5F, -2.0F, 6.0F, 4.0F, 34.0F),
                PartPose.offsetAndRotation(-27.0F, 12.0F, -27.0F, 1.784573F, (float) (Math.PI / 4), 0.0F));
        root.addOrReplaceChild("leg1part2b",
                CubeListBuilder.create().texOffs(3, 53).mirror()
                        .addBox(-2.5F, -2.5F, -2.0F, 5.0F, 4.0F, 33.0F),
                PartPose.offsetAndRotation(-27.0F, 12.0F, -27.0F, 1.747395F, (float) (Math.PI / 4), 0.0F));
        root.addOrReplaceChild("leg1part2c",
                CubeListBuilder.create().texOffs(48, 86).mirror()
                        .addBox(-2.0F, -2.5F, -2.0F, 4.0F, 4.0F, 32.0F),
                PartPose.offsetAndRotation(-27.0F, 12.0F, -27.0F, 1.710216F, (float) (Math.PI / 4), 0.0F));
        root.addOrReplaceChild("leg1part3",
                CubeListBuilder.create().texOffs(0, 91).mirror()
                        .addBox(-1.5F, -2.5F, -17.0F, 3.0F, 4.0F, 18.0F),
                PartPose.offsetAndRotation(-32.0F, -17.0F, -32.0F, 1.189716F, (float) (Math.PI / 4), 0.0F));
        root.addOrReplaceChild("leg1part3b",
                CubeListBuilder.create().texOffs(15, 30).mirror()
                        .addBox(-1.0F, -1.5F, -27.0F, 2.0F, 3.0F, 10.0F),
                PartPose.offsetAndRotation(-32.0F, -17.0F, -32.0F, 1.189716F, (float) (Math.PI / 4), 0.0F));
        root.addOrReplaceChild("leg1part3c",
                CubeListBuilder.create().texOffs(47, 23).mirror()
                        .addBox(-0.5F, -0.5F, -34.0F, 1.0F, 2.0F, 7.0F),
                PartPose.offsetAndRotation(-32.0F, -17.0F, -32.0F, 1.189716F, (float) (Math.PI / 4), 0.0F));
        root.addOrReplaceChild("leg1part3d",
                CubeListBuilder.create().texOffs(0, 23).mirror()
                        .addBox(-0.5F, 0.5F, -44.0F, 1.0F, 1.0F, 10.0F),
                PartPose.offsetAndRotation(-32.0F, -17.0F, -32.0F, 1.189716F, (float) (Math.PI / 4), 0.0F));
        root.addOrReplaceChild("leg2start",
                CubeListBuilder.create().texOffs(55, 0).mirror()
                        .addBox(-3.0F, -2.5F, -4.0F, 6.0F, 5.0F, 5.0F),
                PartPose.offsetAndRotation(7.0F, 0.0F, -7.0F, 0.0F, (float) (-Math.PI / 4), 0.0F));
        root.addOrReplaceChild("leg2shoulder",
                CubeListBuilder.create().texOffs(77, 0).mirror()
                        .addBox(-2.5F, -4.5F, -6.0F, 5.0F, 6.0F, 10.0F),
                PartPose.offsetAndRotation(10.0F, 0.0F, -10.0F, -0.5576792F, (float) (-Math.PI / 4), 0.0F));
        root.addOrReplaceChild("leg2shoulderpart2",
                CubeListBuilder.create().texOffs(70, 18).mirror()
                        .addBox(-1.5F, -2.5F, -11.0F, 3.0F, 3.0F, 6.0F),
                PartPose.offsetAndRotation(10.0F, 0.0F, -10.0F, -0.4089647F, (float) (-Math.PI / 4), 0.0F));
        root.addOrReplaceChild("leg2part1",
                CubeListBuilder.create().texOffs(68, 17).mirror()
                        .addBox(-2.0F, -2.5F, -23.0F, 4.0F, 3.0F, 26.0F),
                PartPose.offsetAndRotation(17.0F, -5.0F, -17.0F, 1.115358F, (float) (-Math.PI / 4), 0.0F));
        root.addOrReplaceChild("leg2part1b",
                CubeListBuilder.create().texOffs(19, 23).mirror()
                        .addBox(-1.5F, 0.5F, -23.0F, 3.0F, 3.0F, 21.0F),
                PartPose.offsetAndRotation(17.0F, -5.0F, -17.0F, 1.07818F, (float) (-Math.PI / 4), 0.0F));
        root.addOrReplaceChild("leg2elbow",
                CubeListBuilder.create().texOffs(68, 28).mirror()
                        .addBox(-1.5F, -5.5F, -22.0F, 3.0F, 6.0F, 3.0F),
                PartPose.offsetAndRotation(17.0F, -5.0F, -17.0F, 1.115358F, (float) (-Math.PI / 4), 0.0F));
        root.addOrReplaceChild("leg2part2",
                CubeListBuilder.create().texOffs(48, 47).mirror()
                        .addBox(-3.0F, -2.5F, -2.0F, 6.0F, 4.0F, 34.0F),
                PartPose.offsetAndRotation(27.0F, 12.0F, -27.0F, 1.784573F, (float) (-Math.PI / 4), 0.0F));
        root.addOrReplaceChild("leg2part2b",
                CubeListBuilder.create().texOffs(3, 53).mirror()
                        .addBox(-2.5F, -2.5F, -2.0F, 5.0F, 4.0F, 33.0F),
                PartPose.offsetAndRotation(27.0F, 12.0F, -27.0F, 1.747395F, (float) (-Math.PI / 4), 0.0F));
        root.addOrReplaceChild("leg2part2c",
                CubeListBuilder.create().texOffs(48, 86).mirror()
                        .addBox(-2.0F, -2.5F, -2.0F, 4.0F, 4.0F, 32.0F),
                PartPose.offsetAndRotation(27.0F, 12.0F, -27.0F, 1.710216F, (float) (-Math.PI / 4), 0.0F));
        root.addOrReplaceChild("leg2part3",
                CubeListBuilder.create().texOffs(0, 91).mirror()
                        .addBox(-1.5F, -2.5F, -17.0F, 3.0F, 4.0F, 18.0F),
                PartPose.offsetAndRotation(32.0F, -17.0F, -32.0F, 1.189716F, (float) (-Math.PI / 4), 0.0F));
        root.addOrReplaceChild("leg2part3b",
                CubeListBuilder.create().texOffs(15, 30).mirror()
                        .addBox(-1.0F, -1.5F, -27.0F, 2.0F, 3.0F, 10.0F),
                PartPose.offsetAndRotation(32.0F, -17.0F, -32.0F, 1.189716F, (float) (-Math.PI / 4), 0.0F));
        root.addOrReplaceChild("leg2part3c",
                CubeListBuilder.create().texOffs(47, 23).mirror()
                        .addBox(-0.5F, -0.5F, -34.0F, 1.0F, 2.0F, 7.0F),
                PartPose.offsetAndRotation(32.0F, -17.0F, -32.0F, 1.189716F, (float) (-Math.PI / 4), 0.0F));
        root.addOrReplaceChild("leg2part3d",
                CubeListBuilder.create().texOffs(0, 23).mirror()
                        .addBox(-0.5F, 0.5F, -44.0F, 1.0F, 1.0F, 10.0F),
                PartPose.offsetAndRotation(32.0F, -17.0F, -32.0F, 1.189716F, (float) (-Math.PI / 4), 0.0F));
        root.addOrReplaceChild("leg3start",
                CubeListBuilder.create().texOffs(55, 0).mirror()
                        .addBox(-3.0F, -2.5F, -1.0F, 6.0F, 5.0F, 5.0F),
                PartPose.offsetAndRotation(7.0F, 0.0F, 7.0F, 0.0F, (float) (Math.PI / 4), 0.0F));
        root.addOrReplaceChild("leg3shoulder",
                CubeListBuilder.create().texOffs(114, 0).mirror()
                        .addBox(-2.5F, -4.5F, -4.0F, 5.0F, 6.0F, 10.0F),
                PartPose.offsetAndRotation(10.0F, 0.0F, 10.0F, 0.5576851F, (float) (Math.PI / 4), 0.0F));
        root.addOrReplaceChild("leg3shoulderpart2",
                CubeListBuilder.create().texOffs(70, 18).mirror()
                        .addBox(-1.5F, -2.5F, 5.0F, 3.0F, 3.0F, 6.0F),
                PartPose.offsetAndRotation(10.0F, 0.0F, 10.0F, 0.4089656F, (float) (Math.PI / 4), 0.0F));
        root.addOrReplaceChild("leg3part1",
                CubeListBuilder.create().texOffs(68, 17).mirror()
                        .addBox(-2.0F, -2.5F, -3.0F, 4.0F, 3.0F, 26.0F),
                PartPose.offsetAndRotation(17.0F, -5.0F, 17.0F, -1.115353F, (float) (Math.PI / 4), 0.0F));
        root.addOrReplaceChild("leg3part1b",
                CubeListBuilder.create().texOffs(117, 55).mirror()
                        .addBox(-1.5F, 0.5F, 2.0F, 3.0F, 3.0F, 21.0F),
                PartPose.offsetAndRotation(17.0F, -5.0F, 17.0F, -1.078177F, (float) (Math.PI / 4), 0.0F));
        root.addOrReplaceChild("leg3elbow",
                CubeListBuilder.create().texOffs(68, 28).mirror()
                        .addBox(-1.5F, -5.5F, 19.0F, 3.0F, 6.0F, 3.0F),
                PartPose.offsetAndRotation(17.0F, -5.0F, 17.0F, -1.115353F, (float) (Math.PI / 4), 0.0F));
        root.addOrReplaceChild("leg3part2",
                CubeListBuilder.create().texOffs(0, 206).mirror()
                        .addBox(-3.0F, -2.5F, -32.0F, 6.0F, 4.0F, 34.0F),
                PartPose.offsetAndRotation(27.0F, 12.0F, 27.0F, -1.784582F, (float) (Math.PI / 4), 0.0F));
        root.addOrReplaceChild("leg3part2b",
                CubeListBuilder.create().texOffs(3, 53).mirror()
                        .addBox(-2.5F, -2.5F, -31.0F, 5.0F, 4.0F, 33.0F),
                PartPose.offsetAndRotation(27.0F, 12.0F, 27.0F, -1.747389F, (float) (Math.PI / 4), 0.0F));
        root.addOrReplaceChild("leg3part2c",
                CubeListBuilder.create().texOffs(48, 86).mirror()
                        .addBox(-2.0F, -2.5F, -30.0F, 4.0F, 4.0F, 32.0F),
                PartPose.offsetAndRotation(27.0F, 12.0F, 27.0F, -1.710213F, (float) (Math.PI / 4), 0.0F));
        root.addOrReplaceChild("leg3part3",
                CubeListBuilder.create().texOffs(0, 178).mirror()
                        .addBox(-1.5F, -2.5F, -1.0F, 3.0F, 4.0F, 18.0F),
                PartPose.offsetAndRotation(32.0F, -17.0F, 32.0F, -1.189721F, (float) (Math.PI / 4), 0.0F));
        root.addOrReplaceChild("leg3part3b",
                CubeListBuilder.create().texOffs(12, 62).mirror()
                        .addBox(-1.0F, -1.5F, 17.0F, 2.0F, 3.0F, 10.0F),
                PartPose.offsetAndRotation(32.0F, -17.0F, 32.0F, -1.189721F, (float) (Math.PI / 4), 0.0F));
        root.addOrReplaceChild("leg3part3c",
                CubeListBuilder.create().texOffs(47, 32).mirror()
                        .addBox(-0.5F, -0.5F, 27.0F, 1.0F, 2.0F, 7.0F),
                PartPose.offsetAndRotation(32.0F, -17.0F, 32.0F, -1.189721F, (float) (Math.PI / 4), 0.0F));
        root.addOrReplaceChild("leg3part3d",
                CubeListBuilder.create().texOffs(0, 23).mirror()
                        .addBox(-0.5F, 0.5F, 34.0F, 1.0F, 1.0F, 10.0F),
                PartPose.offsetAndRotation(32.0F, -17.0F, 32.0F, -1.189721F, (float) (Math.PI / 4), 0.0F));
        root.addOrReplaceChild("leg4start",
                CubeListBuilder.create().texOffs(55, 0).mirror()
                        .addBox(-3.0F, -2.5F, -1.0F, 6.0F, 5.0F, 5.0F),
                PartPose.offsetAndRotation(-7.0F, 0.0F, 7.0F, 0.0F, (float) (-Math.PI / 4), 0.0F));
        root.addOrReplaceChild("leg4shoulder",
                CubeListBuilder.create().texOffs(114, 0).mirror()
                        .addBox(-2.5F, -4.5F, -4.0F, 5.0F, 6.0F, 10.0F),
                PartPose.offsetAndRotation(-10.0F, 0.0F, 10.0F, 0.5576851F, (float) (-Math.PI / 4), 0.0F));
        root.addOrReplaceChild("leg4shoulderpart2",
                CubeListBuilder.create().texOffs(70, 18).mirror()
                        .addBox(-1.5F, -2.5F, 5.0F, 3.0F, 3.0F, 6.0F),
                PartPose.offsetAndRotation(-10.0F, 0.0F, 10.0F, 0.4089656F, (float) (-Math.PI / 4), 0.0F));
        root.addOrReplaceChild("leg4part1",
                CubeListBuilder.create().texOffs(68, 17).mirror()
                        .addBox(-2.0F, -2.5F, -3.0F, 4.0F, 3.0F, 26.0F),
                PartPose.offsetAndRotation(-17.0F, -5.0F, 17.0F, -1.115353F, (float) (-Math.PI / 4), 0.0F));
        root.addOrReplaceChild("leg4part1b",
                CubeListBuilder.create().texOffs(117, 55).mirror()
                        .addBox(-1.5F, 0.5F, 2.0F, 3.0F, 3.0F, 21.0F),
                PartPose.offsetAndRotation(-17.0F, -5.0F, 17.0F, -1.078177F, (float) (-Math.PI / 4), 0.0F));
        root.addOrReplaceChild("leg4elbow",
                CubeListBuilder.create().texOffs(68, 28).mirror()
                        .addBox(-1.5F, -5.5F, 19.0F, 3.0F, 6.0F, 3.0F),
                PartPose.offsetAndRotation(-17.0F, -5.0F, 17.0F, -1.115353F, (float) (-Math.PI / 4), 0.0F));
        root.addOrReplaceChild("leg4part2",
                CubeListBuilder.create().texOffs(0, 206).mirror()
                        .addBox(-3.0F, -2.5F, -32.0F, 6.0F, 4.0F, 34.0F),
                PartPose.offsetAndRotation(-27.0F, 12.0F, 27.0F, -1.784582F, (float) (-Math.PI / 4), 0.0F));
        root.addOrReplaceChild("leg4part2b",
                CubeListBuilder.create().texOffs(3, 53).mirror()
                        .addBox(-2.5F, -2.5F, -31.0F, 5.0F, 4.0F, 33.0F),
                PartPose.offsetAndRotation(-27.0F, 12.0F, 27.0F, -1.747389F, (float) (-Math.PI / 4), 0.0F));
        root.addOrReplaceChild("leg4part2c",
                CubeListBuilder.create().texOffs(48, 86).mirror()
                        .addBox(-2.0F, -2.5F, -30.0F, 4.0F, 4.0F, 32.0F),
                PartPose.offsetAndRotation(-27.0F, 12.0F, 27.0F, -1.710213F, (float) (-Math.PI / 4), 0.0F));
        root.addOrReplaceChild("leg4part3",
                CubeListBuilder.create().texOffs(0, 178).mirror()
                        .addBox(-1.5F, -2.5F, -1.0F, 3.0F, 4.0F, 18.0F),
                PartPose.offsetAndRotation(-32.0F, -17.0F, 32.0F, -1.189721F, (float) (-Math.PI / 4), 0.0F));
        root.addOrReplaceChild("leg4part3b",
                CubeListBuilder.create().texOffs(12, 62).mirror()
                        .addBox(-1.0F, -1.5F, 17.0F, 2.0F, 3.0F, 10.0F),
                PartPose.offsetAndRotation(-32.0F, -17.0F, 32.0F, -1.189721F, (float) (-Math.PI / 4), 0.0F));
        root.addOrReplaceChild("leg4part3c",
                CubeListBuilder.create().texOffs(47, 32).mirror()
                        .addBox(-0.5F, -0.5F, 27.0F, 1.0F, 2.0F, 7.0F),
                PartPose.offsetAndRotation(-32.0F, -17.0F, 32.0F, -1.189721F, (float) (-Math.PI / 4), 0.0F));
        root.addOrReplaceChild("leg4part3d",
                CubeListBuilder.create().texOffs(0, 23).mirror()
                        .addBox(-0.5F, 0.5F, 34.0F, 1.0F, 1.0F, 10.0F),
                PartPose.offsetAndRotation(-32.0F, -17.0F, 32.0F, -1.189721F, (float) (-Math.PI / 4), 0.0F));
        root.addOrReplaceChild("jawbase",
                CubeListBuilder.create().texOffs(104, 0).mirror()
                        .addBox(-2.0F, -2.0F, -40.0F, 4.0F, 3.0F, 40.0F),
                PartPose.offsetAndRotation(0.0F, 0.0F, -8.0F, 0.2230717F, 0.0F, 0.0F));
        root.addOrReplaceChild("jawbase2",
                CubeListBuilder.create().texOffs(133, 44).mirror()
                        .addBox(-6.0F, -2.0F, -8.0F, 12.0F, 3.0F, 8.0F),
                PartPose.offsetAndRotation(0.0F, 0.0F, -8.0F, 0.2230705F, 0.0F, 0.0F));
        root.addOrReplaceChild("jawbase3",
                CubeListBuilder.create().texOffs(96, 55).mirror()
                        .addBox(-7.0F, -2.0F, -15.0F, 14.0F, 3.0F, 7.0F),
                PartPose.offsetAndRotation(0.0F, 0.0F, -8.0F, 0.2230705F, 0.0F, 0.0F));
        root.addOrReplaceChild("jawbase4",
                CubeListBuilder.create().texOffs(96, 47).mirror()
                        .addBox(-6.0F, -2.0F, -19.0F, 12.0F, 3.0F, 4.0F),
                PartPose.offsetAndRotation(0.0F, 0.0F, -8.0F, 0.2230705F, 0.0F, 0.0F));
        root.addOrReplaceChild("jawbase5",
                CubeListBuilder.create().texOffs(104, 32).mirror()
                        .addBox(-5.0F, -2.0F, -23.0F, 10.0F, 3.0F, 4.0F),
                PartPose.offsetAndRotation(0.0F, 0.0F, -8.0F, 0.2230705F, 0.0F, 0.0F));
        root.addOrReplaceChild("jawbase6",
                CubeListBuilder.create().texOffs(104, 25).mirror()
                        .addBox(-4.0F, -2.0F, -26.0F, 8.0F, 3.0F, 3.0F),
                PartPose.offsetAndRotation(0.0F, 0.0F, -8.0F, 0.2230705F, 0.0F, 0.0F));
        root.addOrReplaceChild("jawbase7",
                CubeListBuilder.create().texOffs(104, 19).mirror()
                        .addBox(-3.0F, -2.0F, -28.0F, 6.0F, 3.0F, 2.0F),
                PartPose.offsetAndRotation(0.0F, 0.0F, -8.0F, 0.2230705F, 0.0F, 0.0F));
        root.addOrReplaceChild("jawbase8",
                CubeListBuilder.create().texOffs(104, 150).mirror()
                        .addBox(-2.0F, -2.0F, -40.0F, 4.0F, 4.0F, 40.0F),
                PartPose.offsetAndRotation(0.0F, 0.0F, -8.0F, 0.2146755F, 0.1487144F, -0.7679449F));
        root.addOrReplaceChild("jawbase9",
                CubeListBuilder.create().texOffs(104, 112).mirror()
                        .addBox(-3.0F, -3.0F, -21.0F, 6.0F, 6.0F, 29.0F),
                PartPose.offsetAndRotation(0.0F, 0.0F, -8.0F, 0.1031397F, 0.0743623F, -0.837758F));
        root.addOrReplaceChild("jawleft",
                CubeListBuilder.create().texOffs(76, 61).mirror()
                        .addBox(-14.0F, -10.0F, -37.0F, 1.0F, 11.0F, 1.0F),
                PartPose.offsetAndRotation(0.0F, 0.0F, -8.0F, 0.2230717F, -0.3346075F, 0.0F));
        root.addOrReplaceChild("jawright",
                CubeListBuilder.create().texOffs(71, 61).mirror()
                        .addBox(13.0F, -10.0F, -37.0F, 1.0F, 11.0F, 1.0F),
                PartPose.offsetAndRotation(0.0F, 0.0F, -8.0F, 0.2230717F, 0.3346145F, 0.0F));
        root.addOrReplaceChild("jawend",
                CubeListBuilder.create().texOffs(76, 47).mirror()
                        .addBox(-0.5F, -11.0F, -39.5F, 1.0F, 12.0F, 1.0F),
                PartPose.offsetAndRotation(0.0F, 0.0F, -8.0F, 0.2230717F, 0.0F, 0.0F));
        root.addOrReplaceChild("headstart",
                CubeListBuilder.create().texOffs(92, 87).mirror()
                        .addBox(-6.5F, -2.0F, -7.0F, 13.0F, 9.0F, 13.0F),
                PartPose.offset(0.0F, -9.0F, 2.0F));
        root.addOrReplaceChild("headbase",
                CubeListBuilder.create().texOffs(206, 0).mirror()
                        .addBox(-8.0F, -2.0F, -6.0F, 16.0F, 13.0F, 24.0F),
                PartPose.offset(0.0F, -17.0F, -2.0F));
        root.addOrReplaceChild("headbase2",
                CubeListBuilder.create().texOffs(300, 136).mirror()
                        .addBox(-8.5F, -2.0F, -6.0F, 17.0F, 1.0F, 34.0F),
                PartPose.offset(0.0F, -17.0F, -3.0F));
        root.addOrReplaceChild("headbase3",
                CubeListBuilder.create().texOffs(300, 129).mirror()
                        .addBox(-9.0F, -2.0F, -6.0F, 18.0F, 1.0F, 39.0F),
                PartPose.offset(0.0F, -18.0F, -4.0F));
        root.addOrReplaceChild("headbase4",
                CubeListBuilder.create().texOffs(300, 121).mirror()
                        .addBox(-9.5F, -2.0F, -6.0F, 19.0F, 1.0F, 45.0F),
                PartPose.offset(0.0F, -19.0F, -5.0F));
        root.addOrReplaceChild("headbase5",
                CubeListBuilder.create().texOffs(300, 117).mirror()
                        .addBox(-10.0F, -2.0F, -6.0F, 20.0F, 1.0F, 47.0F),
                PartPose.offset(0.0F, -20.0F, -6.0F));
        root.addOrReplaceChild("headbase6",
                CubeListBuilder.create().texOffs(300, 112).mirror()
                        .addBox(-11.0F, -2.0F, -6.0F, 22.0F, 1.0F, 50.0F),
                PartPose.offset(0.0F, -21.0F, -9.0F));
        root.addOrReplaceChild("headbase7",
                CubeListBuilder.create().texOffs(0, 123).mirror()
                        .addBox(-8.0F, -7.0F, 0.0F, 16.0F, 8.0F, 24.0F),
                PartPose.offsetAndRotation(0.0F, -8.0F, 13.0F, 0.5576792F, 0.0F, 0.0F));
        root.addOrReplaceChild("headbase8",
                CubeListBuilder.create().texOffs(300, 109).mirror()
                        .addBox(-11.0F, -1.0F, -6.0F, 22.0F, 1.0F, 51.0F),
                PartPose.offset(0.0F, -23.0F, -9.0F));
        root.addOrReplaceChild("headbase9",
                CubeListBuilder.create().texOffs(300, 50).mirror()
                        .addBox(-11.0F, -1.0F, -6.0F, 22.0F, 1.0F, 51.0F),
                PartPose.offset(0.0F, -24.0F, -9.0F));
        root.addOrReplaceChild("headbase10",
                CubeListBuilder.create().texOffs(185, 107).mirror()
                        .addBox(-7.0F, -1.5F, -20.0F, 14.0F, 1.0F, 42.0F),
                PartPose.offsetAndRotation(0.0F, -24.0F, 7.0F, 0.0261799F, 0.0F, 0.0F));
        root.addOrReplaceChild("headleftridge",
                CubeListBuilder.create().texOffs(180, 153).mirror()
                        .addBox(-12.0F, -1.0F, -1.0F, 13.0F, 1.0F, 46.0F),
                PartPose.offsetAndRotation(0.0F, -22.0F, -9.0F, 0.0743572F, -0.2230717F, 0.1487144F));
        root.addOrReplaceChild("headrightridge",
                CubeListBuilder.create().texOffs(300, 174).mirror()
                        .addBox(-1.0F, -1.0F, -1.0F, 13.0F, 1.0F, 46.0F),
                PartPose.offsetAndRotation(0.0F, -22.0F, -9.0F, 0.0743572F, 0.2230705F, -0.1487195F));
        root.addOrReplaceChild("antenna1",
                CubeListBuilder.create().texOffs(332, 72).mirror()
                        .addBox(-3.0F, -1.0F, -1.0F, 5.0F, 2.0F, 2.0F),
                PartPose.offset(-10.0F, -20.0F, 19.0F));
        root.addOrReplaceChild("antenna1part2",
                CubeListBuilder.create().texOffs(300, 81).mirror()
                        .addBox(-3.0F, -1.0F, 1.0F, 2.0F, 2.0F, 9.0F),
                PartPose.offsetAndRotation(-10.0F, -20.0F, 19.0F, 0.0F, -0.7807508F, 0.0F));
        root.addOrReplaceChild("antenna2",
                CubeListBuilder.create().texOffs(325, 79).mirror()
                        .addBox(-2.0F, -1.0F, -1.0F, 5.0F, 2.0F, 2.0F),
                PartPose.offset(10.0F, -20.0F, 19.0F));
        root.addOrReplaceChild("antenna2part2",
                CubeListBuilder.create().texOffs(300, 81).mirror()
                        .addBox(1.0F, -1.0F, 1.0F, 2.0F, 2.0F, 9.0F),
                PartPose.offsetAndRotation(10.0F, -20.0F, 19.0F, 0.0F, 0.7807556F, 0.0F));
        root.addOrReplaceChild("eyebase1",
                CubeListBuilder.create().texOffs(300, 121).mirror()
                        .addBox(-2.5F, -2.5F, -1.5F, 5.0F, 5.0F, 3.0F),
                PartPose.offsetAndRotation(-6.0F, -14.0F, -8.0F, 0.0F, 0.7807556F, -0.37179F));
        root.addOrReplaceChild("eye1",
                CubeListBuilder.create().texOffs(0, 0).mirror()
                        .addBox(-1.0F, -1.0F, -2.5F, 2.0F, 2.0F, 2.0F),
                PartPose.offsetAndRotation(-6.0F, -14.0F, -8.0F, 0.0F, 0.7807556F, -0.37179F));
        root.addOrReplaceChild("eyebase2",
                CubeListBuilder.create().texOffs(300, 121).mirror()
                        .addBox(-2.5F, -2.5F, -1.5F, 5.0F, 5.0F, 3.0F),
                PartPose.offsetAndRotation(6.0F, -14.0F, -8.0F, 0.0F, -0.7807508F, 0.3717861F));
        root.addOrReplaceChild("eye2",
                CubeListBuilder.create().texOffs(0, 0).mirror()
                        .addBox(-1.0F, -1.0F, -2.5F, 2.0F, 2.0F, 2.0F),
                PartPose.offsetAndRotation(6.0F, -14.0F, -8.0F, 0.0F, -0.7807508F, 0.3717861F));
        root.addOrReplaceChild("arm1start",
                CubeListBuilder.create().texOffs(300, 93).mirror()
                        .addBox(-3.0F, -1.5F, -1.5F, 4.0F, 3.0F, 3.0F),
                PartPose.offset(-9.0F, -20.0F, -3.0F));
        root.addOrReplaceChild("arm1part1",
                CubeListBuilder.create().texOffs(250, 83).mirror()
                        .addBox(-2.0F, -1.5F, -16.5F, 3.0F, 3.0F, 18.0F),
                PartPose.offsetAndRotation(-11.0F, -20.0F, -4.0F, 0.0F, 0.2777036F, 0.0F));
        root.addOrReplaceChild("arm1part2",
                CubeListBuilder.create().texOffs(250, 83).mirror()
                        .addBox(-2.0F, -9.5F, -1.5F, 3.0F, 11.0F, 3.0F),
                PartPose.offsetAndRotation(-15.3F, -20.0F, -20.0F, -0.7807508F, 0.0F, 0.0F));
        root.addOrReplaceChild("arm1part2b",
                CubeListBuilder.create().texOffs(250, 69).mirror()
                        .addBox(-2.0F, -15.5F, -3.5F, 3.0F, 8.0F, 3.0F),
                PartPose.offsetAndRotation(-15.3F, -20.0F, -20.0F, -1.041001F, 0.0F, 0.0F));
        root.addOrReplaceChild("arm1part2c",
                CubeListBuilder.create().texOffs(300, 66).mirror()
                        .addBox(-2.0F, -17.5F, 8.5F, 3.0F, 8.0F, 3.0F),
                PartPose.offsetAndRotation(-15.3F, -20.0F, -19.0F, -0.0743572F, 0.0F, 0.0F));
        root.addOrReplaceChild("arm1part3",
                CubeListBuilder.create().texOffs(278, 60).mirror()
                        .addBox(-1.0F, -19.5F, -1.0F, 2.0F, 21.0F, 2.0F),
                PartPose.offsetAndRotation(-15.8F, -36.0F, -8.0F, 1.561502F, 0.0F, 0.0F));
        root.addOrReplaceChild("arm1part3b",
                CubeListBuilder.create().texOffs(279, 84).mirror()
                        .addBox(-0.5F, -10.5F, -1.0F, 1.0F, 12.0F, 2.0F),
                PartPose.offsetAndRotation(-15.8F, -36.0F, -8.0F, 1.710216F, 0.0F, 0.0F));
        root.addOrReplaceChild("arm2start",
                CubeListBuilder.create().texOffs(300, 105).mirror()
                        .addBox(-1.0F, -1.5F, -1.5F, 4.0F, 3.0F, 3.0F),
                PartPose.offset(9.0F, -20.0F, -3.0F));
        root.addOrReplaceChild("arm2part1",
                CubeListBuilder.create().texOffs(250, 83).mirror()
                        .addBox(-2.0F, -1.5F, -16.5F, 3.0F, 3.0F, 18.0F),
                PartPose.offsetAndRotation(11.0F, -20.0F, -4.0F, 0.0F, -0.2776993F, 0.0F));
        root.addOrReplaceChild("arm2part2",
                CubeListBuilder.create().texOffs(250, 83).mirror()
                        .addBox(-2.0F, -9.5F, -1.5F, 3.0F, 11.0F, 3.0F),
                PartPose.offsetAndRotation(15.3F, -20.0F, -20.0F, -0.7807508F, 0.0F, 0.0F));
        root.addOrReplaceChild("arm2part2b",
                CubeListBuilder.create().texOffs(250, 69).mirror()
                        .addBox(-2.0F, -15.5F, -3.5F, 3.0F, 8.0F, 3.0F),
                PartPose.offsetAndRotation(15.3F, -20.0F, -20.0F, -1.041001F, 0.0F, 0.0F));
        root.addOrReplaceChild("arm2part2c",
                CubeListBuilder.create().texOffs(300, 66).mirror()
                        .addBox(-2.0F, -17.5F, 8.5F, 3.0F, 8.0F, 3.0F),
                PartPose.offsetAndRotation(15.3F, -20.0F, -19.0F, -0.0743572F, 0.0F, 0.0F));
        root.addOrReplaceChild("arm2part3",
                CubeListBuilder.create().texOffs(287, 60).mirror()
                        .addBox(-1.0F, -19.5F, -1.0F, 2.0F, 21.0F, 2.0F),
                PartPose.offsetAndRotation(14.8F, -36.0F, -8.0F, 1.561502F, 0.0F, 0.0F));
        root.addOrReplaceChild("arm2part3b",
                CubeListBuilder.create().texOffs(279, 84).mirror()
                        .addBox(-0.5F, -10.5F, -1.0F, 1.0F, 12.0F, 2.0F),
                PartPose.offsetAndRotation(14.8F, -36.0F, -8.0F, 1.710216F, 0.0F, 0.0F));
        root.addOrReplaceChild("innermouth",
                CubeListBuilder.create().texOffs(176, 44).mirror()
                        .addBox(-6.0F, -2.0F, -8.0F, 12.0F, 3.0F, 8.0F),
                PartPose.offsetAndRotation(0.0F, 0.0F, -12.0F, -1.933289F, 0.0F, 0.0F));
        root.addOrReplaceChild("innermouth2",
                CubeListBuilder.create().texOffs(159, 4).mirror()
                        .addBox(-6.0F, -2.0F, -8.0F, 12.0F, 3.0F, 8.0F),
                PartPose.offsetAndRotation(0.0F, -24.0F, -12.0F, 1.933284F, 0.0F, 0.0F));
        root.addOrReplaceChild("tooth1",
                CubeListBuilder.create().texOffs(0, 6).mirror()
                        .addBox(-5.0F, 0.0F, -10.0F, 2.0F, 1.0F, 2.0F),
                PartPose.offsetAndRotation(1.0F, 0.0F, -12.0F, -1.933289F, 0.0F, 0.0F));
        root.addOrReplaceChild("tooth2",
                CubeListBuilder.create().texOffs(0, 6).mirror()
                        .addBox(1.0F, 0.0F, -10.0F, 2.0F, 1.0F, 2.0F),
                PartPose.offsetAndRotation(1.0F, 0.0F, -12.0F, -1.933289F, 0.0F, 0.0F));
        root.addOrReplaceChild("tooth3",
                CubeListBuilder.create().texOffs(0, 6).mirror()
                        .addBox(1.0F, 0.0F, -10.0F, 2.0F, 1.0F, 2.0F),
                PartPose.offsetAndRotation(-2.0F, 0.0F, -12.0F, -1.933289F, 0.0F, 0.0F));
        root.addOrReplaceChild("tooth4",
                CubeListBuilder.create().texOffs(0, 6).mirror()
                        .addBox(-4.0F, -2.0F, -10.0F, 2.0F, 1.0F, 2.0F),
                PartPose.offsetAndRotation(0.0F, -24.0F, -12.0F, 1.933284F, 0.0F, 0.0F));
        root.addOrReplaceChild("tooth5",
                CubeListBuilder.create().texOffs(0, 6).mirror()
                        .addBox(2.0F, -2.0F, -10.0F, 2.0F, 1.0F, 2.0F),
                PartPose.offsetAndRotation(0.0F, -24.0F, -12.0F, 1.933284F, 0.0F, 0.0F));
        root.addOrReplaceChild("tooth6",
                CubeListBuilder.create().texOffs(0, 6).mirror()
                        .addBox(-1.0F, -2.0F, -10.0F, 2.0F, 1.0F, 2.0F),
                PartPose.offsetAndRotation(0.0F, -24.0F, -12.0F, 1.933284F, 0.0F, 0.0F));
        root.addOrReplaceChild("mouthedge1",
                CubeListBuilder.create().texOffs(400, 77).mirror()
                        .addBox(-1.0F, -4.5F, -1.5F, 3.0F, 9.0F, 14.0F),
                PartPose.offsetAndRotation(9.0F, -12.0F, -6.0F, 0.0F, -0.2230717F, 0.0F));
        root.addOrReplaceChild("mouthedge2",
                CubeListBuilder.create().texOffs(400, 52).mirror()
                        .addBox(-2.0F, -4.5F, -1.5F, 3.0F, 9.0F, 14.0F),
                PartPose.offsetAndRotation(-9.0F, -12.0F, -6.0F, 0.0F, 0.2230717F, 0.0F));
        root.addOrReplaceChild("mouthedge3",
                CubeListBuilder.create().texOffs(450, 69).mirror()
                        .addBox(-1.0F, -4.5F, -1.5F, 3.0F, 9.0F, 14.0F),
                PartPose.offsetAndRotation(8.0F, -6.0F, -7.0F, 0.5576792F, -0.2230717F, 0.0F));
        root.addOrReplaceChild("mouthedge4",
                CubeListBuilder.create().texOffs(450, 93).mirror()
                        .addBox(-2.0F, -4.5F, -1.5F, 3.0F, 9.0F, 14.0F),
                PartPose.offsetAndRotation(-8.0F, -6.0F, -7.0F, 0.5576792F, 0.2230705F, 0.0F));
        root.addOrReplaceChild("arm3start",
                CubeListBuilder.create().texOffs(335, 85).mirror()
                        .addBox(-1.0F, -1.5F, -1.5F, 4.0F, 3.0F, 3.0F),
                PartPose.offset(10.0F, -9.0F, -3.0F));
        root.addOrReplaceChild("arm3part1",
                CubeListBuilder.create().texOffs(264, 108).mirror()
                        .addBox(-1.0F, -1.5F, -9.5F, 3.0F, 3.0F, 11.0F),
                PartPose.offsetAndRotation(13.0F, -9.0F, -3.0F, -0.2602503F, 0.0F, 0.0F));
        root.addOrReplaceChild("arm3part1b",
                CubeListBuilder.create().texOffs(317, 67).mirror()
                        .addBox(0.0F, -2.5F, -12.5F, 3.0F, 5.0F, 4.0F),
                PartPose.offsetAndRotation(13.0F, -9.0F, -3.0F, -0.2602503F, 0.0F, 0.0F));
        root.addOrReplaceChild("arm3part1c",
                CubeListBuilder.create().texOffs(300, 81).mirror()
                        .addBox(-2.0F, -2.5F, -12.51F, 3.0F, 5.0F, 0.0F),
                PartPose.offsetAndRotation(13.0F, -9.0F, -3.0F, -0.2602503F, 0.0F, 0.0F));
        root.addOrReplaceChild("arm4start",
                CubeListBuilder.create().texOffs(335, 93).mirror()
                        .addBox(-3.0F, -1.5F, -1.5F, 4.0F, 3.0F, 3.0F),
                PartPose.offset(-10.0F, -9.0F, -3.0F));
        root.addOrReplaceChild("arm4part1",
                CubeListBuilder.create().texOffs(300, 51).mirror()
                        .addBox(-2.0F, -1.5F, -9.5F, 3.0F, 3.0F, 11.0F),
                PartPose.offsetAndRotation(-13.0F, -9.0F, -3.0F, -0.2602503F, 0.0F, 0.0F));
        root.addOrReplaceChild("arm4part1b",
                CubeListBuilder.create().texOffs(322, 106).mirror()
                        .addBox(-3.0F, -2.5F, -12.5F, 3.0F, 5.0F, 4.0F),
                PartPose.offsetAndRotation(-13.0F, -9.0F, -3.0F, -0.2602503F, 0.0F, 0.0F));
        root.addOrReplaceChild("arm4part1c",
                CubeListBuilder.create().texOffs(316, 81).mirror()
                        .addBox(-1.0F, -2.5F, -12.51F, 3.0F, 5.0F, 0.0F),
                PartPose.offsetAndRotation(-13.0F, -9.0F, -3.0F, -0.2602503F, 0.0F, 0.0F));
        root.addOrReplaceChild("tongue",
                CubeListBuilder.create().texOffs(0, 49).mirror()
                        .addBox(-1.0F, -1.0F, -10.0F, 2.0F, 2.0F, 11.0F),
                PartPose.offset(0.0F, -12.0F, -8.0F));
        root.addOrReplaceChild("tonguepart2",
                CubeListBuilder.create().texOffs(0, 12).mirror()
                        .addBox(-1.0F, -1.5F, -8.0F, 2.0F, 3.0F, 1.0F),
                PartPose.offset(0.0F, -12.0F, -8.0F));
        root.addOrReplaceChild("upperjawridgeleft",
                CubeListBuilder.create().texOffs(350, 27).mirror()
                        .addBox(-3.0F, -4.0F, -2.0F, 5.0F, 1.0F, 18.0F),
                PartPose.offsetAndRotation(0.0F, -22.0F, -33.0F, 0.2602503F, -0.3717861F, 0.0F));
        root.addOrReplaceChild("upperjawridgeright",
                CubeListBuilder.create().texOffs(350, 27).mirror()
                        .addBox(-2.0F, -4.0F, -2.0F, 5.0F, 1.0F, 18.0F),
                PartPose.offsetAndRotation(0.0F, -22.0F, -33.0F, 0.260246F, 0.37179F, 0.0F));
        root.addOrReplaceChild("upperjawbaseleft",
                CubeListBuilder.create().texOffs(235, 44).mirror()
                        .addBox(-5.0F, -3.0F, -16.0F, 3.0F, 4.0F, 14.0F),
                PartPose.offsetAndRotation(0.0F, -22.0F, -35.0F, 0.0F, -0.2230717F, 0.0F));
        root.addOrReplaceChild("upperjawbaseright",
                CubeListBuilder.create().texOffs(210, 68).mirror()
                        .addBox(2.0F, -3.0F, -16.0F, 3.0F, 4.0F, 14.0F),
                PartPose.offsetAndRotation(0.0F, -22.0F, -35.0F, 0.0F, 0.2230767F, 0.0F));
        root.addOrReplaceChild("upperjawbase",
                CubeListBuilder.create().texOffs(146, 76).mirror()
                        .addBox(-5.0F, -3.0F, -29.0F, 10.0F, 4.0F, 29.0F),
                PartPose.offset(0.0F, -22.0F, -10.0F));
        root.addOrReplaceChild("upperjawbase2",
                CubeListBuilder.create().texOffs(168, 111).mirror()
                        .addBox(-5.0F, -4.0F, -2.0F, 10.0F, 5.0F, 18.0F),
                PartPose.offsetAndRotation(0.0F, -22.0F, -33.0F, 0.2602503F, 0.0F, 0.0F));
        root.addOrReplaceChild("upperjawbase3",
                CubeListBuilder.create().texOffs(300, 0).mirror()
                        .addBox(-1.5F, -3.01F, -16.0F, 3.0F, 3.0F, 14.0F),
                PartPose.offsetAndRotation(0.0F, -22.0F, -35.0F, 0.0F, 5.1E-6F, 0.0F));
        root.addOrReplaceChild("upperjawbase4",
                CubeListBuilder.create().texOffs(45, 161).mirror()
                        .addBox(-2.0F, -2.0F, -29.0F, 4.0F, 4.0F, 41.0F),
                PartPose.offsetAndRotation(0.0F, -22.0F, -22.0F, 0.0698132F, -0.0488692F, 0.7807508F));
        root.addOrReplaceChild("upperjawend",
                CubeListBuilder.create().texOffs(160, 57).mirror()
                        .addBox(-0.5F, 0.0F, -0.5F, 1.0F, 10.0F, 1.0F),
                PartPose.offsetAndRotation(0.0F, -20.0F, -50.0F, 0.0F, 5.1E-6F, 0.0F));
        root.addOrReplaceChild("upperjawleftend",
                CubeListBuilder.create().texOffs(171, 58).mirror()
                        .addBox(-0.5F, 0.0F, -0.5F, 1.0F, 9.0F, 1.0F),
                PartPose.offsetAndRotation(-1.0F, -20.0F, -50.0F, -0.1115358F, -1.52433F, 0.0F));
        root.addOrReplaceChild("upperjawrightend",
                CubeListBuilder.create().texOffs(165, 58).mirror()
                        .addBox(-0.5F, 0.0F, -0.5F, 1.0F, 9.0F, 1.0F),
                PartPose.offsetAndRotation(1.0F, -20.0F, -50.0F, -0.1115358F, 1.52433F, 0.0F));

        return LayerDefinition.create(mesh, 512, 256);
    }

    @Override
    public ModelPart root() {
        return this.root;
    }

    @Override
    public void setupAnim(
            TrooperBug entity,
            float limbSwing,
            float limbSwingAmount,
            float ageInTicks,
            float netHeadYaw,
            float headPitch) {
        float newangle = 0.0F;
        float upangle = 0.0F;
        float nextangle = 0.0F;
        if (entity.getAttacking() == 0) {
        newangle = Mth.cos(ageInTicks * 0.4F * this.wingspeed) * (float) Math.PI * 0.05F;
        } else {
        newangle = Mth.cos(ageInTicks * 1.4F * this.wingspeed) * (float) Math.PI * 0.1F;
        }
        this.antenna2part2.yRot = 0.78F + newangle;
        this.antenna1part2.yRot = -0.78F - newangle;
        if (entity.getAttacking() == 0) {
        newangle = Mth.cos(ageInTicks * 0.5F * this.wingspeed) * (float) Math.PI * 0.05F;
        } else {
        newangle = Mth.cos(ageInTicks * 2.5F * this.wingspeed) * (float) Math.PI * 0.15F;
        }
        this.arm4part1.yRot = newangle;
        this.arm4part1b.yRot = newangle;
        this.arm4part1c.yRot = newangle;
        this.arm3part1.yRot = -newangle;
        this.arm3part1b.yRot = -newangle;
        this.arm3part1c.yRot = -newangle;
        if (entity.getAttacking() == 0) {
        newangle = Mth.cos(ageInTicks * 0.3F * this.wingspeed) * (float) Math.PI * 0.05F;
        } else {
        newangle = Mth.cos(ageInTicks * 2.6F * this.wingspeed) * (float) Math.PI * 0.2F;
        }
        this.arm1part3.xRot = 1.56F + newangle;
        this.arm1part3b.xRot = 1.56F + newangle;
        this.arm2part3.xRot = 1.56F - newangle;
        this.arm2part3b.xRot = 1.56F - newangle;
        if (entity.getAttacking() == 0) {
        newangle = Mth.cos(ageInTicks * 0.1F * this.wingspeed) * (float) Math.PI * 0.02F;
        } else {
        newangle = Mth.cos(ageInTicks * 1.0F * this.wingspeed) * (float) Math.PI * 0.1F;
        }
        this.headleftridge.yRot = -0.25F + newangle;
        this.headrightridge.yRot = 0.25F - newangle;
        this.upperjawridgeleft.yRot = -0.372F + newangle;
        this.upperjawridgeright.yRot = 0.372F - newangle;
        if (entity.getAttacking() == 0) {
        newangle = Mth.cos(ageInTicks * 0.3F * this.wingspeed) * (float) Math.PI * 0.015F;
        } else {
        newangle = Mth.cos(ageInTicks * 2.6F * this.wingspeed) * (float) Math.PI * 0.1F;
        }
        this.jawbase.xRot = 0.22F + newangle;
        this.jawbase2.xRot = 0.22F + newangle;
        this.jawbase3.xRot = 0.22F + newangle;
        this.jawbase4.xRot = 0.22F + newangle;
        this.jawbase5.xRot = 0.22F + newangle;
        this.jawbase6.xRot = 0.22F + newangle;
        this.jawbase7.xRot = 0.22F + newangle;
        this.jawbase8.xRot = 0.2146F + newangle;
        this.jawbase8.yRot = 0.1487F + newangle;
        this.jawbase9.xRot = 0.1F + newangle;
        this.jawbase9.yRot = 0.07F + newangle;
        this.jawend.xRot = 0.22F + newangle;
        this.jawleft.xRot = 0.22F + newangle;
        this.jawright.xRot = 0.22F + newangle;
        newangle = Mth.sin(ageInTicks * 2.0F * this.wingspeed) * (float) Math.PI * 0.12F * limbSwingAmount;
        nextangle = Mth.sin((ageInTicks + 0.1F) * 2.0F * this.wingspeed) * (float) Math.PI * 0.12F * limbSwingAmount;
        upangle = 0.0F;
        if (nextangle > newangle) {
        upangle = Math.abs(Mth.cos(ageInTicks * 2.0F * this.wingspeed) * (float) Math.PI * 0.12F * limbSwingAmount);
        }
        this.doLeftFrontLeg(newangle, upangle);
        this.doLeftRearLeg(-newangle, upangle);
        newangle = Mth.sin((float)(ageInTicks * 2.0F * this.wingspeed + Math.PI)) * (float) Math.PI * 0.12F * limbSwingAmount;
        nextangle = Mth.sin((float)((ageInTicks + 0.1F) * 2.0F * this.wingspeed + Math.PI)) * (float) Math.PI * 0.12F * limbSwingAmount;
        upangle = 0.0F;
        if (nextangle > newangle) {
        upangle = Math.abs(Mth.cos((float)(ageInTicks * 2.0F * this.wingspeed + Math.PI)) * (float) Math.PI * 0.12F * limbSwingAmount);
        }
        this.doRightFrontLeg(-newangle, upangle);
        this.doRightRearLeg(newangle, upangle);
    }

    private void doRightFrontLeg(float angle, float upangle) {
        this.leg1part1.yRot = 1.2F + angle;
        this.leg1part1b.yRot = this.leg1part1.yRot;
        this.leg1elbow.yRot = this.leg1part1.yRot;
        this.leg1part2.yRot = this.leg1part1.yRot;
        this.leg1part2b.yRot = this.leg1part1.yRot;
        this.leg1part2c.yRot = this.leg1part1.yRot;
        this.leg1part3.yRot = this.leg1part1.yRot;
        this.leg1part3b.yRot = this.leg1part1.yRot;
        this.leg1part3c.yRot = this.leg1part1.yRot;
        this.leg1part3d.yRot = this.leg1part1.yRot;
        this.leg1part1.xRot = 1.115F + upangle;
        this.leg1part1b.xRot = 1.078F + upangle;
        this.leg1elbow.xRot = this.leg1part1.xRot;
        float dist = 26.0F;
        dist = (float)(dist * Math.cos(this.leg1part1.xRot));
        this.leg1part2.z = this.leg1part2b.z = this.leg1part2c.z = (float)(
        this.leg1part1.z - Math.cos(this.leg1part1.yRot) * dist
        );
        this.leg1part2.x = this.leg1part2b.x = this.leg1part2c.x = (float)(
        this.leg1part1.x - Math.sin(this.leg1part1.yRot) * dist
        );
        this.leg1part2.xRot = 1.871F - upangle;
        this.leg1part2b.xRot = 1.817F - upangle;
        this.leg1part2c.xRot = 1.762F - upangle;
        dist = 32.0F;
        dist = (float)Math.abs(dist * Math.cos(this.leg1part2.xRot));
        this.leg1part3.z = this.leg1part3b.z = this.leg1part3c.z = this.leg1part3d.z = (float)(
        this.leg1part2.z - Math.cos(this.leg1part2.yRot) * dist
        );
        this.leg1part3.x = this.leg1part3b.x = this.leg1part3c.x = this.leg1part3d.x = (float)(
        this.leg1part2.x - Math.sin(this.leg1part2.yRot) * dist
        );
        this.leg1part3.xRot = 1.08F + upangle;
        this.leg1part3b.xRot = 1.08F + upangle;
        this.leg1part3c.xRot = 1.08F + upangle;
        this.leg1part3d.xRot = 1.08F + upangle;
    }

    private void doLeftFrontLeg(float angle, float upangle) {
        this.leg2part1.yRot = -1.2F + angle;
        this.leg2part1b.yRot = this.leg2part1.yRot;
        this.leg2elbow.yRot = this.leg2part1.yRot;
        this.leg2part2.yRot = this.leg2part1.yRot;
        this.leg2part2b.yRot = this.leg2part1.yRot;
        this.leg2part2c.yRot = this.leg2part1.yRot;
        this.leg2part3.yRot = this.leg2part1.yRot;
        this.leg2part3b.yRot = this.leg2part1.yRot;
        this.leg2part3c.yRot = this.leg2part1.yRot;
        this.leg2part3d.yRot = this.leg2part1.yRot;
        this.leg2part1.xRot = 1.115F + upangle;
        this.leg2part1b.xRot = 1.078F + upangle;
        this.leg2elbow.xRot = this.leg2part1.xRot;
        float dist = 26.0F;
        dist = (float)(dist * Math.cos(this.leg2part1.xRot));
        this.leg2part2.z = this.leg2part2b.z = this.leg2part2c.z = (float)(
        this.leg2part1.z - Math.cos(this.leg2part1.yRot) * dist
        );
        this.leg2part2.x = this.leg2part2b.x = this.leg2part2c.x = (float)(
        this.leg2part1.x - Math.sin(this.leg2part1.yRot) * dist
        );
        this.leg2part2.xRot = 1.871F - upangle;
        this.leg2part2b.xRot = 1.817F - upangle;
        this.leg2part2c.xRot = 1.762F - upangle;
        dist = 32.0F;
        dist = (float)Math.abs(dist * Math.cos(this.leg2part2.xRot));
        this.leg2part3.z = this.leg2part3b.z = this.leg2part3c.z = this.leg2part3d.z = (float)(
        this.leg2part2.z - Math.cos(this.leg2part2.yRot) * dist
        );
        this.leg2part3.x = this.leg2part3b.x = this.leg2part3c.x = this.leg2part3d.x = (float)(
        this.leg2part2.x - Math.sin(this.leg2part2.yRot) * dist
        );
        this.leg2part3.xRot = 1.08F + upangle;
        this.leg2part3b.xRot = 1.08F + upangle;
        this.leg2part3c.xRot = 1.08F + upangle;
        this.leg2part3d.xRot = 1.08F + upangle;
    }

    private void doRightRearLeg(float angle, float upangle) {
        this.leg4part1.yRot = -1.2F + angle;
        this.leg4part1b.yRot = this.leg4part1.yRot;
        this.leg4elbow.yRot = this.leg4part1.yRot;
        this.leg4part2.yRot = this.leg4part1.yRot;
        this.leg4part2b.yRot = this.leg4part1.yRot;
        this.leg4part2c.yRot = this.leg4part1.yRot;
        this.leg4part3.yRot = this.leg4part1.yRot;
        this.leg4part3b.yRot = this.leg4part1.yRot;
        this.leg4part3c.yRot = this.leg4part1.yRot;
        this.leg4part3d.yRot = this.leg4part1.yRot;
        this.leg4part1.xRot = -1.115F + upangle;
        this.leg4part1b.xRot = -1.078F + upangle;
        this.leg4elbow.xRot = this.leg4part1.xRot;
        float dist = 26.0F;
        dist = (float)(dist * Math.cos(this.leg4part1.xRot));
        this.leg4part2.z = this.leg4part2b.z = this.leg4part2c.z = (float)(
        this.leg4part1.z + Math.cos(this.leg4part1.yRot) * dist
        );
        this.leg4part2.x = this.leg4part2b.x = this.leg4part2c.x = (float)(
        this.leg4part1.x + Math.sin(this.leg4part1.yRot) * dist
        );
        this.leg4part2.xRot = -1.871F - upangle;
        this.leg4part2b.xRot = -1.817F - upangle;
        this.leg4part2c.xRot = -1.762F - upangle;
        dist = 32.0F;
        dist = (float)Math.abs(dist * Math.cos(this.leg4part2.xRot));
        this.leg4part3.z = this.leg4part3b.z = this.leg4part3c.z = this.leg4part3d.z = (float)(
        this.leg4part2.z + Math.cos(this.leg4part2.yRot) * dist
        );
        this.leg4part3.x = this.leg4part3b.x = this.leg4part3c.x = this.leg4part3d.x = (float)(
        this.leg4part2.x + Math.sin(this.leg4part2.yRot) * dist
        );
        this.leg4part3.xRot = -1.08F + upangle;
        this.leg4part3b.xRot = -1.08F + upangle;
        this.leg4part3c.xRot = -1.08F + upangle;
        this.leg4part3d.xRot = -1.08F + upangle;
    }

    private void doLeftRearLeg(float angle, float upangle) {
        this.leg3part1.yRot = 1.2F + angle;
        this.leg3part1b.yRot = this.leg3part1.yRot;
        this.leg3elbow.yRot = this.leg3part1.yRot;
        this.leg3part2.yRot = this.leg3part1.yRot;
        this.leg3part2b.yRot = this.leg3part1.yRot;
        this.leg3part2c.yRot = this.leg3part1.yRot;
        this.leg3part3.yRot = this.leg3part1.yRot;
        this.leg3part3b.yRot = this.leg3part1.yRot;
        this.leg3part3c.yRot = this.leg3part1.yRot;
        this.leg3part3d.yRot = this.leg3part1.yRot;
        this.leg3part1.xRot = -1.115F + upangle;
        this.leg3part1b.xRot = -1.078F + upangle;
        this.leg3elbow.xRot = this.leg3part1.xRot;
        float dist = 26.0F;
        dist = (float)(dist * Math.cos(this.leg3part1.xRot));
        this.leg3part2.z = this.leg3part2b.z = this.leg3part2c.z = (float)(
        this.leg3part1.z + Math.cos(this.leg3part1.yRot) * dist
        );
        this.leg3part2.x = this.leg3part2b.x = this.leg3part2c.x = (float)(
        this.leg3part1.x + Math.sin(this.leg3part1.yRot) * dist
        );
        this.leg3part2.xRot = -1.871F - upangle;
        this.leg3part2b.xRot = -1.817F - upangle;
        this.leg3part2c.xRot = -1.762F - upangle;
        dist = 32.0F;
        dist = (float)Math.abs(dist * Math.cos(this.leg3part2.xRot));
        this.leg3part3.z = this.leg3part3b.z = this.leg3part3c.z = this.leg3part3d.z = (float)(
        this.leg3part2.z + Math.cos(this.leg3part2.yRot) * dist
        );
        this.leg3part3.x = this.leg3part3b.x = this.leg3part3c.x = this.leg3part3d.x = (float)(
        this.leg3part2.x + Math.sin(this.leg3part2.yRot) * dist
        );
        this.leg3part3.xRot = -1.08F + upangle;
        this.leg3part3b.xRot = -1.08F + upangle;
        this.leg3part3c.xRot = -1.08F + upangle;
        this.leg3part3d.xRot = -1.08F + upangle;
    }

}
