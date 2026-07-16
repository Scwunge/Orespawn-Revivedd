package danger.orespawn.client.model;

import danger.orespawn.entity.SpitBug;
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
 * Port of gold {@code ModelSpitBug} (1.7.10 ModelBase, tex 512×256) → 1.21 HierarchicalModel.
 * Cube sizes/UVs/offsets 1:1. FULL leg IK + jaw anim from gold {@code func_78088_a}.
 * Wingspeed default 0.55 matches ClientProxy.
 */
@OnlyIn(Dist.CLIENT)
public class ModelSpitBug extends HierarchicalModel<SpitBug> {
    /** Model layer location. */
    public static final ModelLayerLocation LAYER_LOCATION =
            new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "spit_bug"), "main");

    private final float wingspeed;
    private final ModelPart root;
    private final ModelPart legintersection;
    private final ModelPart legintersectionpart2;
    private final ModelPart legintersectionpart3;
    private final ModelPart leg1start;
    private final ModelPart leg1startpart2;
    private final ModelPart leg1startpart3;
    private final ModelPart leg1;
    private final ModelPart leg1part2;
    private final ModelPart leg1part2b;
    private final ModelPart leg1part2c;
    private final ModelPart leg1part2d;
    private final ModelPart leg1part3;
    private final ModelPart leg1part3b;
    private final ModelPart leg1part3c;
    private final ModelPart leg2start;
    private final ModelPart leg2startpart2;
    private final ModelPart leg2startpart3;
    private final ModelPart leg2;
    private final ModelPart leg2part2;
    private final ModelPart leg2part2b;
    private final ModelPart leg2part2c;
    private final ModelPart leg2part2d;
    private final ModelPart leg2part3;
    private final ModelPart leg2part3b;
    private final ModelPart leg2part3c;
    private final ModelPart leg3start;
    private final ModelPart leg3startpart2;
    private final ModelPart leg3startpart3;
    private final ModelPart leg3;
    private final ModelPart leg3part2;
    private final ModelPart leg3part2b;
    private final ModelPart leg3part2c;
    private final ModelPart leg3part2d;
    private final ModelPart leg3part3;
    private final ModelPart leg3part3b;
    private final ModelPart leg3part3c;
    private final ModelPart leg4start;
    private final ModelPart leg4startpart2;
    private final ModelPart leg4startpart3;
    private final ModelPart leg4;
    private final ModelPart leg4part2;
    private final ModelPart leg4part2b;
    private final ModelPart leg4part2c;
    private final ModelPart leg4part2d;
    private final ModelPart leg4part3;
    private final ModelPart leg4part3b;
    private final ModelPart leg4part3c;
    private final ModelPart bodybase;
    private final ModelPart bodybasepart2;
    private final ModelPart bodybasepart3;
    private final ModelPart bodybasepart4;
    private final ModelPart bodybasepart5;
    private final ModelPart bodybasepart6;
    private final ModelPart bodybasepart7;
    private final ModelPart bodybasepart8;
    private final ModelPart bodybasepart9;
    private final ModelPart bodybasepart10;
    private final ModelPart bodybasepart11;
    private final ModelPart bodybasepart12;
    private final ModelPart bodybasepart13;
    private final ModelPart bodybasepart14;
    private final ModelPart bodybasepart15;
    private final ModelPart upperjawbase;
    private final ModelPart upperjawbasepart1;
    private final ModelPart upperjawbasepart2;
    private final ModelPart upperjawbasepart3;
    private final ModelPart tooth1;
    private final ModelPart tooth2;
    private final ModelPart tooth3;
    private final ModelPart tooth4;
    private final ModelPart tooth5;
    private final ModelPart lowerjawbase;
    private final ModelPart lowerjawbasepart1;
    private final ModelPart lowerjawbasepart2;
    private final ModelPart lowerjawbasepart3;
    private final ModelPart lowerjawbasepart4;
    private final ModelPart lowerjawbasepart5;
    private final ModelPart lowerjawbasepart6;
    private final ModelPart lowerjawbasepart7;
    private final ModelPart lowerjawbasepart8;
    private final ModelPart lowerjawbasepart9;
    private final ModelPart lowerjawbasepart10;
    private final ModelPart lowerjawbasepart11;
    private final ModelPart arm1start;
    private final ModelPart arm1;
    private final ModelPart arm1part2;
    private final ModelPart arm1end;
    private final ModelPart arm2start;
    private final ModelPart arm2;
    private final ModelPart arm2part2;
    private final ModelPart arm2end;
    private final ModelPart eye1;
    private final ModelPart eye2;

    public ModelSpitBug(ModelPart root) {
        this(root, 0.55F);
    }

    public ModelSpitBug(ModelPart root, float wingspeed) {
        this.root = root;
        this.wingspeed = wingspeed;
        this.legintersection = root.getChild("legintersection");
        this.legintersectionpart2 = root.getChild("legintersectionpart2");
        this.legintersectionpart3 = root.getChild("legintersectionpart3");
        this.leg1start = root.getChild("leg1start");
        this.leg1startpart2 = root.getChild("leg1startpart2");
        this.leg1startpart3 = root.getChild("leg1startpart3");
        this.leg1 = root.getChild("leg1");
        this.leg1part2 = root.getChild("leg1part2");
        this.leg1part2b = root.getChild("leg1part2b");
        this.leg1part2c = root.getChild("leg1part2c");
        this.leg1part2d = root.getChild("leg1part2d");
        this.leg1part3 = root.getChild("leg1part3");
        this.leg1part3b = root.getChild("leg1part3b");
        this.leg1part3c = root.getChild("leg1part3c");
        this.leg2start = root.getChild("leg2start");
        this.leg2startpart2 = root.getChild("leg2startpart2");
        this.leg2startpart3 = root.getChild("leg2startpart3");
        this.leg2 = root.getChild("leg2");
        this.leg2part2 = root.getChild("leg2part2");
        this.leg2part2b = root.getChild("leg2part2b");
        this.leg2part2c = root.getChild("leg2part2c");
        this.leg2part2d = root.getChild("leg2part2d");
        this.leg2part3 = root.getChild("leg2part3");
        this.leg2part3b = root.getChild("leg2part3b");
        this.leg2part3c = root.getChild("leg2part3c");
        this.leg3start = root.getChild("leg3start");
        this.leg3startpart2 = root.getChild("leg3startpart2");
        this.leg3startpart3 = root.getChild("leg3startpart3");
        this.leg3 = root.getChild("leg3");
        this.leg3part2 = root.getChild("leg3part2");
        this.leg3part2b = root.getChild("leg3part2b");
        this.leg3part2c = root.getChild("leg3part2c");
        this.leg3part2d = root.getChild("leg3part2d");
        this.leg3part3 = root.getChild("leg3part3");
        this.leg3part3b = root.getChild("leg3part3b");
        this.leg3part3c = root.getChild("leg3part3c");
        this.leg4start = root.getChild("leg4start");
        this.leg4startpart2 = root.getChild("leg4startpart2");
        this.leg4startpart3 = root.getChild("leg4startpart3");
        this.leg4 = root.getChild("leg4");
        this.leg4part2 = root.getChild("leg4part2");
        this.leg4part2b = root.getChild("leg4part2b");
        this.leg4part2c = root.getChild("leg4part2c");
        this.leg4part2d = root.getChild("leg4part2d");
        this.leg4part3 = root.getChild("leg4part3");
        this.leg4part3b = root.getChild("leg4part3b");
        this.leg4part3c = root.getChild("leg4part3c");
        this.bodybase = root.getChild("bodybase");
        this.bodybasepart2 = root.getChild("bodybasepart2");
        this.bodybasepart3 = root.getChild("bodybasepart3");
        this.bodybasepart4 = root.getChild("bodybasepart4");
        this.bodybasepart5 = root.getChild("bodybasepart5");
        this.bodybasepart6 = root.getChild("bodybasepart6");
        this.bodybasepart7 = root.getChild("bodybasepart7");
        this.bodybasepart8 = root.getChild("bodybasepart8");
        this.bodybasepart9 = root.getChild("bodybasepart9");
        this.bodybasepart10 = root.getChild("bodybasepart10");
        this.bodybasepart11 = root.getChild("bodybasepart11");
        this.bodybasepart12 = root.getChild("bodybasepart12");
        this.bodybasepart13 = root.getChild("bodybasepart13");
        this.bodybasepart14 = root.getChild("bodybasepart14");
        this.bodybasepart15 = root.getChild("bodybasepart15");
        this.upperjawbase = root.getChild("upperjawbase");
        this.upperjawbasepart1 = root.getChild("upperjawbasepart1");
        this.upperjawbasepart2 = root.getChild("upperjawbasepart2");
        this.upperjawbasepart3 = root.getChild("upperjawbasepart3");
        this.tooth1 = root.getChild("tooth1");
        this.tooth2 = root.getChild("tooth2");
        this.tooth3 = root.getChild("tooth3");
        this.tooth4 = root.getChild("tooth4");
        this.tooth5 = root.getChild("tooth5");
        this.lowerjawbase = root.getChild("lowerjawbase");
        this.lowerjawbasepart1 = root.getChild("lowerjawbasepart1");
        this.lowerjawbasepart2 = root.getChild("lowerjawbasepart2");
        this.lowerjawbasepart3 = root.getChild("lowerjawbasepart3");
        this.lowerjawbasepart4 = root.getChild("lowerjawbasepart4");
        this.lowerjawbasepart5 = root.getChild("lowerjawbasepart5");
        this.lowerjawbasepart6 = root.getChild("lowerjawbasepart6");
        this.lowerjawbasepart7 = root.getChild("lowerjawbasepart7");
        this.lowerjawbasepart8 = root.getChild("lowerjawbasepart8");
        this.lowerjawbasepart9 = root.getChild("lowerjawbasepart9");
        this.lowerjawbasepart10 = root.getChild("lowerjawbasepart10");
        this.lowerjawbasepart11 = root.getChild("lowerjawbasepart11");
        this.arm1start = root.getChild("arm1start");
        this.arm1 = root.getChild("arm1");
        this.arm1part2 = root.getChild("arm1part2");
        this.arm1end = root.getChild("arm1end");
        this.arm2start = root.getChild("arm2start");
        this.arm2 = root.getChild("arm2");
        this.arm2part2 = root.getChild("arm2part2");
        this.arm2end = root.getChild("arm2end");
        this.eye1 = root.getChild("eye1");
        this.eye2 = root.getChild("eye2");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        root.addOrReplaceChild("legintersection",
                CubeListBuilder.create().texOffs(0, 0)
                        .addBox(-6.0F, -2.0F, -7.0F, 12.0F, 6.0F, 14.0F),
                PartPose.offset(0.0F, 3.0F, 1.0F));
        root.addOrReplaceChild("legintersectionpart2",
                CubeListBuilder.create().texOffs(0, 21)
                        .addBox(-8.0F, -2.0F, 0.0F, 8.0F, 6.0F, 8.0F),
                PartPose.offsetAndRotation(0.0F, 3.0F, 2.0F, 0.0F, (float) (Math.PI / 4), 0.0F));
        root.addOrReplaceChild("legintersectionpart3",
                CubeListBuilder.create().texOffs(282, 18)
                        .addBox(-7.0F, 3.0F, -6.0F, 14.0F, 2.0F, 12.0F),
                PartPose.offset(0.0F, 3.0F, 1.0F));
        root.addOrReplaceChild("leg1start",
                CubeListBuilder.create().texOffs(53, 0)
                        .addBox(-3.0F, -3.0F, -3.0F, 6.0F, 6.0F, 4.0F),
                PartPose.offsetAndRotation(4.0F, 3.0F, -4.0F, 0.0F, (float) (-Math.PI / 4), 0.0F));
        root.addOrReplaceChild("leg1startpart2",
                CubeListBuilder.create().texOffs(53, 19)
                        .addBox(-2.5F, -2.0F, -3.0F, 5.0F, 5.0F, 4.0F),
                PartPose.offsetAndRotation(6.0F, 3.0F, -6.0F, 0.3717861F, (float) (-Math.PI / 4), 0.0F));
        root.addOrReplaceChild("leg1startpart3",
                CubeListBuilder.create().texOffs(53, 29)
                        .addBox(-2.0F, -2.0F, -4.0F, 4.0F, 4.0F, 2.0F),
                PartPose.offsetAndRotation(6.0F, 3.0F, -6.0F, 0.669215F, (float) (-Math.PI / 4), 0.0F));
        root.addOrReplaceChild("leg1",
                CubeListBuilder.create().texOffs(45, 36)
                        .addBox(-1.5F, -3.0F, -13.0F, 3.0F, 3.0F, 10.0F),
                PartPose.offsetAndRotation(6.0F, 3.0F, -6.0F, 1.041001F, (float) (-Math.PI / 4), 0.0F));
        root.addOrReplaceChild("leg1part2",
                CubeListBuilder.create().texOffs(33, 50)
                        .addBox(-2.0F, -1.5F, -13.0F, 4.0F, 3.0F, 15.0F),
                PartPose.offsetAndRotation(12.0F, 13.0F, -12.0F, -1.152537F, (float) (-Math.PI / 4), 0.0F));
        root.addOrReplaceChild("leg1part2b",
                CubeListBuilder.create().texOffs(33, 50)
                        .addBox(-2.0F, 0.5F, 1.0F, 4.0F, 1.0F, 3.0F),
                PartPose.offsetAndRotation(12.0F, 13.0F, -12.0F, -0.7435722F, (float) (-Math.PI / 4), 0.0F));
        root.addOrReplaceChild("leg1part2c",
                CubeListBuilder.create().texOffs(33, 50)
                        .addBox(-2.0F, -7.5F, -13.0F, 4.0F, 1.0F, 3.0F),
                PartPose.offsetAndRotation(12.0F, 13.0F, -12.0F, -0.6320451F, (float) (-Math.PI / 4), 0.0F));
        root.addOrReplaceChild("leg1part2d",
                CubeListBuilder.create().texOffs(2, 50)
                        .addBox(-1.5F, -1.5F, -10.0F, 3.0F, 3.0F, 12.0F),
                PartPose.offsetAndRotation(12.0F, 13.0F, -12.0F, -1.041001F, (float) (-Math.PI / 4), 0.0F));
        root.addOrReplaceChild("leg1part3",
                CubeListBuilder.create().texOffs(51, 69)
                        .addBox(-1.5F, -1.5F, -7.0F, 3.0F, 3.0F, 7.0F),
                PartPose.offsetAndRotation(16.0F, 3.0F, -16.0F, 0.669215F, (float) (-Math.PI / 4), 0.0F));
        root.addOrReplaceChild("leg1part3b",
                CubeListBuilder.create().texOffs(55, 80)
                        .addBox(-2.0F, -2.0F, -2.0F, 4.0F, 16.0F, 4.0F),
                PartPose.offsetAndRotation(20.0F, 8.0F, -20.0F, -0.4833219F, (float) (-Math.PI / 4), -0.0349066F));
        root.addOrReplaceChild("leg1part3c",
                CubeListBuilder.create().texOffs(42, 80)
                        .addBox(-2.0F, 14.0F, 0.0F, 4.0F, 3.0F, 2.0F),
                PartPose.offsetAndRotation(20.0F, 8.0F, -20.0F, -0.4833219F, (float) (-Math.PI / 4), -0.0349066F));
        root.addOrReplaceChild("leg2start",
                CubeListBuilder.create().texOffs(52, 0)
                        .addBox(-3.0F, -3.0F, -3.0F, 6.0F, 6.0F, 4.0F),
                PartPose.offsetAndRotation(-4.0F, 3.0F, -4.0F, 0.0F, (float) (Math.PI / 4), 0.0F));
        root.addOrReplaceChild("leg2startpart2",
                CubeListBuilder.create().texOffs(53, 19)
                        .addBox(-2.5F, -2.0F, -3.0F, 5.0F, 5.0F, 4.0F),
                PartPose.offsetAndRotation(-6.0F, 3.0F, -6.0F, 0.3717861F, (float) (Math.PI / 4), 0.0F));
        root.addOrReplaceChild("leg2startpart3",
                CubeListBuilder.create().texOffs(53, 29)
                        .addBox(-2.0F, -2.0F, -4.0F, 4.0F, 4.0F, 2.0F),
                PartPose.offsetAndRotation(-6.0F, 3.0F, -6.0F, 0.669215F, (float) (Math.PI / 4), 0.0F));
        root.addOrReplaceChild("leg2",
                CubeListBuilder.create().texOffs(45, 36)
                        .addBox(-1.5F, -3.0F, -13.0F, 3.0F, 3.0F, 10.0F),
                PartPose.offsetAndRotation(-6.0F, 3.0F, -6.0F, 1.041001F, (float) (Math.PI / 4), 0.0F));
        root.addOrReplaceChild("leg2part2",
                CubeListBuilder.create().texOffs(72, 50)
                        .addBox(-2.0F, -1.5F, -13.0F, 4.0F, 3.0F, 15.0F),
                PartPose.offsetAndRotation(-12.0F, 13.0F, -12.0F, -1.152537F, (float) (Math.PI / 4), 0.0F));
        root.addOrReplaceChild("leg2part2b",
                CubeListBuilder.create().texOffs(33, 50)
                        .addBox(-2.0F, 0.5F, 1.0F, 4.0F, 1.0F, 3.0F),
                PartPose.offsetAndRotation(-12.0F, 13.0F, -12.0F, -0.7435722F, (float) (Math.PI / 4), 0.0F));
        root.addOrReplaceChild("leg2part2c",
                CubeListBuilder.create().texOffs(33, 50)
                        .addBox(-2.0F, -7.5F, -13.0F, 4.0F, 1.0F, 3.0F),
                PartPose.offsetAndRotation(-12.0F, 13.0F, -12.0F, -0.6320451F, (float) (Math.PI / 4), 0.0F));
        root.addOrReplaceChild("leg2part2d",
                CubeListBuilder.create().texOffs(2, 50)
                        .addBox(-1.5F, -1.5F, -10.0F, 3.0F, 3.0F, 12.0F),
                PartPose.offsetAndRotation(-12.0F, 13.0F, -12.0F, -1.041001F, (float) (Math.PI / 4), 0.0F));
        root.addOrReplaceChild("leg2part3",
                CubeListBuilder.create().texOffs(51, 69)
                        .addBox(-1.5F, -1.5F, -7.0F, 3.0F, 3.0F, 7.0F),
                PartPose.offsetAndRotation(-16.0F, 3.0F, -16.0F, 0.669215F, (float) (Math.PI / 4), 0.0F));
        root.addOrReplaceChild("leg2part3b",
                CubeListBuilder.create().texOffs(55, 80)
                        .addBox(-2.0F, -2.0F, -2.0F, 4.0F, 16.0F, 4.0F),
                PartPose.offsetAndRotation(-20.0F, 8.0F, -20.0F, -0.4833219F, (float) (Math.PI / 4), -0.0349066F));
        root.addOrReplaceChild("leg2part3c",
                CubeListBuilder.create().texOffs(42, 80)
                        .addBox(-2.0F, 14.0F, 0.0F, 4.0F, 3.0F, 2.0F),
                PartPose.offsetAndRotation(-20.0F, 8.0F, -20.0F, -0.4833219F, (float) (Math.PI / 4), -0.0349066F));
        root.addOrReplaceChild("leg3start",
                CubeListBuilder.create().texOffs(52, 0)
                        .addBox(-3.0F, -3.0F, -3.0F, 6.0F, 6.0F, 4.0F),
                PartPose.offsetAndRotation(4.0F, 3.0F, 6.0F, 0.0F, -2.356194F, 0.0F));
        root.addOrReplaceChild("leg3startpart2",
                CubeListBuilder.create().texOffs(72, 19)
                        .addBox(-2.5F, -2.0F, -3.0F, 5.0F, 5.0F, 4.0F),
                PartPose.offsetAndRotation(6.0F, 3.0F, 8.0F, 0.3717861F, -2.356194F, 0.0F));
        root.addOrReplaceChild("leg3startpart3",
                CubeListBuilder.create().texOffs(72, 29)
                        .addBox(-2.0F, -2.0F, -4.0F, 4.0F, 4.0F, 2.0F),
                PartPose.offsetAndRotation(6.0F, 3.0F, 8.0F, 0.669215F, -2.356194F, 0.0F));
        root.addOrReplaceChild("leg3",
                CubeListBuilder.create().texOffs(72, 36)
                        .addBox(-1.5F, -3.0F, -13.0F, 3.0F, 3.0F, 10.0F),
                PartPose.offsetAndRotation(6.0F, 3.0F, 8.0F, 1.041001F, -2.356194F, 0.0F));
        root.addOrReplaceChild("leg3part2",
                CubeListBuilder.create().texOffs(33, 50)
                        .addBox(-2.0F, -1.5F, -13.0F, 4.0F, 3.0F, 15.0F),
                PartPose.offsetAndRotation(12.0F, 13.0F, 14.0F, -1.152537F, -2.356194F, 0.0F));
        root.addOrReplaceChild("leg3part2b",
                CubeListBuilder.create().texOffs(33, 50)
                        .addBox(-2.0F, 0.5F, 1.0F, 4.0F, 1.0F, 3.0F),
                PartPose.offsetAndRotation(12.0F, 13.0F, 14.0F, -0.7435722F, -2.356194F, 0.0F));
        root.addOrReplaceChild("leg3part2c",
                CubeListBuilder.create().texOffs(33, 50)
                        .addBox(-2.0F, -7.5F, -13.0F, 4.0F, 1.0F, 3.0F),
                PartPose.offsetAndRotation(12.0F, 13.0F, 14.0F, -0.6320451F, -2.356194F, 0.0F));
        root.addOrReplaceChild("leg3part2d",
                CubeListBuilder.create().texOffs(111, 50)
                        .addBox(-1.5F, -1.5F, -10.0F, 3.0F, 3.0F, 12.0F),
                PartPose.offsetAndRotation(12.0F, 13.0F, 14.0F, -1.041001F, -2.356194F, 0.0F));
        root.addOrReplaceChild("leg3part3",
                CubeListBuilder.create().texOffs(72, 69)
                        .addBox(-1.5F, -1.5F, -7.0F, 3.0F, 3.0F, 7.0F),
                PartPose.offsetAndRotation(16.0F, 3.0F, 18.0F, 0.669215F, -2.356194F, 0.0F));
        root.addOrReplaceChild("leg3part3b",
                CubeListBuilder.create().texOffs(72, 80)
                        .addBox(-2.0F, -2.0F, -2.0F, 4.0F, 16.0F, 4.0F),
                PartPose.offsetAndRotation(20.0F, 8.0F, 22.0F, -0.4833219F, -2.356194F, -0.0349066F));
        root.addOrReplaceChild("leg3part3c",
                CubeListBuilder.create().texOffs(89, 80)
                        .addBox(-2.0F, 14.0F, 0.0F, 4.0F, 3.0F, 2.0F),
                PartPose.offsetAndRotation(20.0F, 8.0F, 22.0F, -0.4833219F, -2.356194F, -0.0349066F));
        root.addOrReplaceChild("leg4start",
                CubeListBuilder.create().texOffs(52, 0)
                        .addBox(-3.0F, -3.0F, -3.0F, 6.0F, 6.0F, 4.0F),
                PartPose.offsetAndRotation(-4.0F, 3.0F, 6.0F, 0.0F, 2.356194F, 0.0F));
        root.addOrReplaceChild("leg4startpart2",
                CubeListBuilder.create().texOffs(72, 19)
                        .addBox(-2.5F, -2.0F, -3.0F, 5.0F, 5.0F, 4.0F),
                PartPose.offsetAndRotation(-6.0F, 3.0F, 8.0F, 0.3717861F, 2.356194F, 0.0F));
        root.addOrReplaceChild("leg4startpart3",
                CubeListBuilder.create().texOffs(72, 29)
                        .addBox(-2.0F, -2.0F, -4.0F, 4.0F, 4.0F, 2.0F),
                PartPose.offsetAndRotation(-6.0F, 3.0F, 8.0F, 0.669215F, 2.356194F, 0.0F));
        root.addOrReplaceChild("leg4",
                CubeListBuilder.create().texOffs(72, 36)
                        .addBox(-1.5F, -3.0F, -13.0F, 3.0F, 3.0F, 10.0F),
                PartPose.offsetAndRotation(-6.0F, 3.0F, 8.0F, 1.041001F, 2.356194F, 0.0F));
        root.addOrReplaceChild("leg4part2",
                CubeListBuilder.create().texOffs(72, 50)
                        .addBox(-2.0F, -1.5F, -13.0F, 4.0F, 3.0F, 15.0F),
                PartPose.offsetAndRotation(-12.0F, 13.0F, 14.0F, -1.152537F, 2.356194F, 0.0F));
        root.addOrReplaceChild("leg4part2b",
                CubeListBuilder.create().texOffs(33, 50)
                        .addBox(-2.0F, 0.5F, 1.0F, 4.0F, 1.0F, 3.0F),
                PartPose.offsetAndRotation(-12.0F, 13.0F, 14.0F, -0.7435722F, 2.363176F, 0.0F));
        root.addOrReplaceChild("leg4part2c",
                CubeListBuilder.create().texOffs(33, 50)
                        .addBox(-2.0F, -7.5F, -13.0F, 4.0F, 1.0F, 3.0F),
                PartPose.offsetAndRotation(-12.0F, 13.0F, 14.0F, -0.6320451F, 2.356194F, 0.0F));
        root.addOrReplaceChild("leg4part2d",
                CubeListBuilder.create().texOffs(111, 50)
                        .addBox(-1.5F, -1.5F, -10.0F, 3.0F, 3.0F, 12.0F),
                PartPose.offsetAndRotation(-12.0F, 13.0F, 14.0F, -1.041001F, 2.356194F, 0.0F));
        root.addOrReplaceChild("leg4part3",
                CubeListBuilder.create().texOffs(72, 69)
                        .addBox(-1.5F, -1.5F, -7.0F, 3.0F, 3.0F, 7.0F),
                PartPose.offsetAndRotation(-16.0F, 3.0F, 18.0F, 0.669215F, 2.356194F, 0.0F));
        root.addOrReplaceChild("leg4part3b",
                CubeListBuilder.create().texOffs(72, 80)
                        .addBox(-2.0F, -2.0F, -2.0F, 4.0F, 16.0F, 4.0F),
                PartPose.offsetAndRotation(-20.0F, 8.0F, 22.0F, -0.4833219F, 2.356194F, -0.0349066F));
        root.addOrReplaceChild("leg4part3c",
                CubeListBuilder.create().texOffs(42, 80)
                        .addBox(-2.0F, 14.0F, 0.0F, 4.0F, 3.0F, 2.0F),
                PartPose.offsetAndRotation(-20.0F, 8.0F, 22.0F, -0.4833219F, 2.356194F, -0.0349066F));
        root.addOrReplaceChild("bodybase",
                CubeListBuilder.create().texOffs(98, 0)
                        .addBox(-5.0F, -20.0F, -6.5F, 10.0F, 20.0F, 13.0F),
                PartPose.offset(0.0F, 1.0F, 1.0F));
        root.addOrReplaceChild("bodybasepart2",
                CubeListBuilder.create().texOffs(146, 0)
                        .addBox(-6.0F, -20.0F, -7.5F, 12.0F, 12.0F, 21.0F),
                PartPose.offset(0.0F, 1.0F, 1.0F));
        root.addOrReplaceChild("bodybasepart3",
                CubeListBuilder.create().texOffs(213, 0)
                        .addBox(-6.5F, -20.0F, -7.5F, 13.0F, 12.0F, 21.0F),
                PartPose.offset(0.0F, 1.0F, 1.0F));
        root.addOrReplaceChild("bodybasepart4",
                CubeListBuilder.create().texOffs(132, 34)
                        .addBox(-5.0F, -18.0F, -16.5F, 10.0F, 8.0F, 9.0F),
                PartPose.offset(0.0F, 1.0F, 1.0F));
        root.addOrReplaceChild("bodybasepart5",
                CubeListBuilder.create().texOffs(172, 36)
                        .addBox(-5.0F, -19.0F, 13.5F, 10.0F, 10.0F, 5.0F),
                PartPose.offset(0.0F, 1.0F, 1.0F));
        root.addOrReplaceChild("bodybasepart6",
                CubeListBuilder.create().texOffs(142, 53)
                        .addBox(-4.5F, -18.0F, 18.5F, 9.0F, 4.0F, 3.0F),
                PartPose.offset(0.0F, 1.0F, 1.0F));
        root.addOrReplaceChild("bodybasepart7",
                CubeListBuilder.create().texOffs(167, 53)
                        .addBox(-2.5F, -26.0F, -10.5F, 5.0F, 2.0F, 9.0F),
                PartPose.offsetAndRotation(0.0F, 1.0F, 1.0F, -1.264073F, 0.0F, 0.0F));
        root.addOrReplaceChild("bodybasepart8",
                CubeListBuilder.create().texOffs(111, 68)
                        .addBox(-6.0F, -13.0F, -18.5F, 12.0F, 10.0F, 11.0F),
                PartPose.offsetAndRotation(0.0F, 1.0F, 1.0F, -0.4089647F, 0.0F, 0.0F));
        root.addOrReplaceChild("bodybasepart9",
                CubeListBuilder.create().texOffs(157, 66)
                        .addBox(-7.5F, -14.0F, -11.5F, 15.0F, 16.0F, 7.0F),
                PartPose.offsetAndRotation(0.0F, 1.0F, 1.0F, -1.412787F, 0.0F, 0.0F));
        root.addOrReplaceChild("bodybasepart10",
                CubeListBuilder.create().texOffs(204, 35)
                        .addBox(-7.5F, -22.0F, -9.5F, 15.0F, 5.0F, 22.0F),
                PartPose.offset(0.0F, 1.0F, 1.0F));
        root.addOrReplaceChild("bodybasepart11",
                CubeListBuilder.create().texOffs(204, 63)
                        .addBox(-6.5F, -21.0F, -14.5F, 13.0F, 4.0F, 5.0F),
                PartPose.offset(0.0F, 1.0F, 1.0F));
        root.addOrReplaceChild("bodybasepart12",
                CubeListBuilder.create().texOffs(282, 0)
                        .addBox(-5.0F, -3.0F, 2.5F, 10.0F, 4.0F, 12.0F),
                PartPose.offsetAndRotation(0.0F, 1.0F, 1.0F, 0.4833219F, 0.0F, 0.0F));
        root.addOrReplaceChild("bodybasepart13",
                CubeListBuilder.create().texOffs(327, 0)
                        .addBox(4.0F, 1.0F, -3.5F, 4.0F, 2.0F, 10.0F),
                PartPose.offsetAndRotation(0.0F, 1.0F, 1.0F, 0.1858931F, 0.0F, -1.003822F));
        root.addOrReplaceChild("bodybasepart14",
                CubeListBuilder.create().texOffs(327, 0)
                        .addBox(-8.0F, 1.0F, -3.5F, 4.0F, 2.0F, 10.0F),
                PartPose.offsetAndRotation(0.0F, 1.0F, 1.0F, 0.185895F, 0.0F, 1.003826F));
        root.addOrReplaceChild("bodybasepart15",
                CubeListBuilder.create().texOffs(144, 91)
                        .addBox(-2.5F, -25.0F, 1.5F, 5.0F, 3.0F, 7.0F),
                PartPose.offsetAndRotation(0.0F, 1.0F, 1.0F, -0.7504916F, 0.0F, 0.0F));
        root.addOrReplaceChild("upperjawbase",
                CubeListBuilder.create().texOffs(0, 37)
                        .addBox(-3.5F, -2.0F, -6.0F, 7.0F, 5.0F, 6.0F),
                PartPose.offset(0.0F, -13.0F, -15.0F));
        root.addOrReplaceChild("upperjawbasepart1",
                CubeListBuilder.create().texOffs(35, 28)
                        .addBox(-4.5F, -1.0F, -7.0F, 3.0F, 3.0F, 2.0F),
                PartPose.offset(0.0F, -13.0F, -15.0F));
        root.addOrReplaceChild("upperjawbasepart2",
                CubeListBuilder.create().texOffs(35, 28)
                        .addBox(1.5F, -1.0F, -7.0F, 3.0F, 3.0F, 2.0F),
                PartPose.offset(0.0F, -13.0F, -15.0F));
        root.addOrReplaceChild("upperjawbasepart3",
                CubeListBuilder.create().texOffs(27, 37)
                        .addBox(-1.0F, -1.0F, -7.0F, 2.0F, 3.0F, 2.0F),
                PartPose.offset(0.0F, -13.0F, -15.0F));
        root.addOrReplaceChild("tooth1",
                CubeListBuilder.create().texOffs(116, 34)
                        .addBox(-1.5F, -2.0F, -14.0F, 2.0F, 2.0F, 7.0F),
                PartPose.offsetAndRotation(0.0F, -13.0F, -15.0F, 0.2602503F, 0.3717861F, 0.0F));
        root.addOrReplaceChild("tooth2",
                CubeListBuilder.create().texOffs(116, 34)
                        .addBox(-0.5F, -2.0F, -14.0F, 2.0F, 2.0F, 7.0F),
                PartPose.offsetAndRotation(0.0F, -13.0F, -15.0F, 0.2602503F, -0.3717861F, 0.0F));
        root.addOrReplaceChild("tooth3",
                CubeListBuilder.create().texOffs(116, 34)
                        .addBox(-1.0F, -2.0F, -14.0F, 2.0F, 2.0F, 7.0F),
                PartPose.offsetAndRotation(0.0F, -13.0F, -15.0F, 0.2602503F, 0.0F, 0.0F));
        root.addOrReplaceChild("tooth4",
                CubeListBuilder.create().texOffs(90, 111)
                        .addBox(-5.5F, 1.5F, -23.5F, 3.0F, 2.0F, 5.0F),
                PartPose.offsetAndRotation(0.0F, 0.0F, -6.0F, -0.2230717F, 0.0F, 0.0F));
        root.addOrReplaceChild("tooth5",
                CubeListBuilder.create().texOffs(90, 111)
                        .addBox(2.5F, 1.5F, -23.5F, 3.0F, 2.0F, 5.0F),
                PartPose.offsetAndRotation(0.0F, 0.0F, -6.0F, -0.2230717F, 0.0F, 0.0F));
        root.addOrReplaceChild("lowerjawbase",
                CubeListBuilder.create().texOffs(90, 91)
                        .addBox(-5.0F, -1.0F, -15.5F, 10.0F, 2.0F, 16.0F),
                PartPose.offset(0.0F, 0.0F, -6.0F));
        root.addOrReplaceChild("lowerjawbasepart1",
                CubeListBuilder.create().texOffs(0, 69)
                        .addBox(-5.0F, -3.0F, -15.5F, 1.0F, 2.0F, 16.0F),
                PartPose.offset(0.0F, 0.0F, -6.0F));
        root.addOrReplaceChild("lowerjawbasepart2",
                CubeListBuilder.create().texOffs(0, 69)
                        .addBox(4.0F, -3.0F, -15.5F, 1.0F, 2.0F, 16.0F),
                PartPose.offset(0.0F, 0.0F, -6.0F));
        root.addOrReplaceChild("lowerjawbasepart3",
                CubeListBuilder.create().texOffs(0, 88)
                        .addBox(-4.0F, -2.0F, -15.5F, 1.0F, 1.0F, 16.0F),
                PartPose.offset(0.0F, 0.0F, -6.0F));
        root.addOrReplaceChild("lowerjawbasepart4",
                CubeListBuilder.create().texOffs(0, 88)
                        .addBox(3.0F, -2.0F, -15.5F, 1.0F, 1.0F, 16.0F),
                PartPose.offset(0.0F, 0.0F, -6.0F));
        root.addOrReplaceChild("lowerjawbasepart5",
                CubeListBuilder.create().texOffs(35, 134)
                        .addBox(5.0F, -5.0F, -16.5F, 2.0F, 4.0F, 20.0F),
                PartPose.offset(0.0F, 0.0F, -6.0F));
        root.addOrReplaceChild("lowerjawbasepart6",
                CubeListBuilder.create().texOffs(35, 109)
                        .addBox(-7.0F, -5.0F, -16.5F, 2.0F, 4.0F, 20.0F),
                PartPose.offset(0.0F, 0.0F, -6.0F));
        root.addOrReplaceChild("lowerjawbasepart7",
                CubeListBuilder.create().texOffs(73, 101)
                        .addBox(-6.0F, -3.0F, -19.5F, 4.0F, 3.0F, 4.0F),
                PartPose.offset(0.0F, 0.0F, -6.0F));
        root.addOrReplaceChild("lowerjawbasepart8",
                CubeListBuilder.create().texOffs(73, 101)
                        .addBox(2.0F, -3.0F, -19.5F, 4.0F, 3.0F, 4.0F),
                PartPose.offset(0.0F, 0.0F, -6.0F));
        root.addOrReplaceChild("lowerjawbasepart9",
                CubeListBuilder.create().texOffs(95, 72)
                        .addBox(-2.0F, -2.0F, -18.5F, 4.0F, 2.0F, 3.0F),
                PartPose.offset(0.0F, 0.0F, -6.0F));
        root.addOrReplaceChild("lowerjawbasepart10",
                CubeListBuilder.create().texOffs(0, 106)
                        .addBox(-8.0F, -3.0F, -12.5F, 1.0F, 2.0F, 16.0F),
                PartPose.offset(0.0F, 0.0F, -6.0F));
        root.addOrReplaceChild("lowerjawbasepart11",
                CubeListBuilder.create().texOffs(0, 106)
                        .addBox(7.0F, -3.0F, -12.5F, 1.0F, 2.0F, 16.0F),
                PartPose.offset(0.0F, 0.0F, -6.0F));
        root.addOrReplaceChild("arm1start",
                CubeListBuilder.create().texOffs(0, 50)
                        .addBox(-0.5F, -1.0F, -1.0F, 3.0F, 2.0F, 2.0F),
                PartPose.offset(5.0F, -11.0F, -14.0F));
        root.addOrReplaceChild("arm1",
                CubeListBuilder.create().texOffs(9, 125)
                        .addBox(-0.5F, -1.0F, -1.0F, 2.0F, 5.0F, 2.0F),
                PartPose.offsetAndRotation(7.0F, -11.0F, -14.0F, -0.8922867F, 0.0F, 0.0F));
        root.addOrReplaceChild("arm1part2",
                CubeListBuilder.create().texOffs(9, 133)
                        .addBox(-0.5F, -1.0F, -1.0F, 2.0F, 5.0F, 2.0F),
                PartPose.offsetAndRotation(7.0F, -9.0F, -16.5F, 0.7435722F, 0.0F, 0.0F));
        root.addOrReplaceChild("arm1end",
                CubeListBuilder.create().texOffs(9, 141)
                        .addBox(1.0F, 3.0F, 1.0F, 1.0F, 1.0F, 2.0F),
                PartPose.offsetAndRotation(6.0F, -9.0F, -16.5F, 0.7435722F, 0.0F, 0.0F));
        root.addOrReplaceChild("arm2start",
                CubeListBuilder.create().texOffs(0, 50)
                        .addBox(-2.5F, -1.0F, -1.0F, 3.0F, 2.0F, 2.0F),
                PartPose.offset(-5.0F, -11.0F, -14.0F));
        root.addOrReplaceChild("arm2",
                CubeListBuilder.create().texOffs(0, 125)
                        .addBox(-1.5F, -1.0F, -1.0F, 2.0F, 5.0F, 2.0F),
                PartPose.offsetAndRotation(-7.0F, -11.0F, -14.0F, -0.8922867F, 0.0F, 0.0F));
        root.addOrReplaceChild("arm2part2",
                CubeListBuilder.create().texOffs(0, 133)
                        .addBox(-1.5F, -1.0F, -1.0F, 2.0F, 5.0F, 2.0F),
                PartPose.offsetAndRotation(-7.0F, -9.0F, -16.5F, 0.7435722F, 0.0F, 0.0F));
        root.addOrReplaceChild("arm2end",
                CubeListBuilder.create().texOffs(0, 141)
                        .addBox(-1.0F, 3.0F, 1.0F, 1.0F, 1.0F, 2.0F),
                PartPose.offsetAndRotation(-7.0F, -9.0F, -16.5F, 0.7435722F, 0.0F, 0.0F));
        root.addOrReplaceChild("eye1",
                CubeListBuilder.create().texOffs(36, 37)
                        .addBox(-0.5F, -1.0F, -1.0F, 1.0F, 2.0F, 2.0F),
                PartPose.offset(6.5F, -10.0F, -11.0F));
        root.addOrReplaceChild("eye2",
                CubeListBuilder.create().texOffs(36, 37)
                        .addBox(-0.5F, -1.0F, -1.0F, 1.0F, 2.0F, 2.0F),
                PartPose.offset(-6.5F, -10.0F, -11.0F));
        return LayerDefinition.create(mesh, 512, 256);
    }

    @Override
    public ModelPart root() {
        return this.root;
    }

    @Override
    public void setupAnim(
            SpitBug entity,
            float limbSwing,
            float limbSwingAmount,
            float ageInTicks,
            float netHeadYaw,
            float headPitch) {
        float newangle = 0.0F;
        float upangle = 0.0F;
        float nextangle = 0.0F;
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
        if (entity.getAttacking() == 0) {
        newangle = Mth.cos(ageInTicks * 0.3F * this.wingspeed) * (float) Math.PI * 0.015F;
        } else {
        newangle = Mth.cos(ageInTicks * 2.6F * this.wingspeed) * (float) Math.PI * 0.1F;
        }
        newangle = Math.abs(newangle);
        this.upperjawbasepart1.xRot = newangle;
        this.upperjawbasepart2.xRot = newangle;
        this.upperjawbasepart3.xRot = newangle;
        this.tooth1.xRot = 0.26F + newangle;
        this.tooth2.xRot = 0.26F + newangle;
        this.tooth3.xRot = 0.26F + newangle;
    }

    private void doRightFrontLeg(float angle, float upangle) {
        this.leg1.yRot = -1.2F + angle;
        this.leg1part2.yRot = this.leg1.yRot;
        this.leg1part2b.yRot = this.leg1.yRot;
        this.leg1part2c.yRot = this.leg1.yRot;
        this.leg1part2d.yRot = this.leg1.yRot;
        this.leg1part3.yRot = this.leg1.yRot;
        this.leg1part3b.yRot = this.leg1.yRot;
        this.leg1part3c.yRot = this.leg1.yRot;
        float dist = 14.0F;
        dist = (float)(dist * Math.cos(this.leg1.xRot));
        this.leg1part2.z = (float)( this.leg1.z - Math.cos(this.leg1.yRot) * dist );
        this.leg1part2b.z = (float)( this.leg1.z - Math.cos(this.leg1.yRot) * dist );
        this.leg1part2c.z = (float)( this.leg1.z - Math.cos(this.leg1.yRot) * dist );
        this.leg1part2d.z = (float)( this.leg1.z - Math.cos(this.leg1.yRot) * dist );
        this.leg1part2.x = (float)( this.leg1.x - Math.sin(this.leg1.yRot) * dist );
        this.leg1part2b.x = (float)( this.leg1.x - Math.sin(this.leg1.yRot) * dist );
        this.leg1part2c.x = (float)( this.leg1.x - Math.sin(this.leg1.yRot) * dist );
        this.leg1part2d.x = (float)( this.leg1.x - Math.sin(this.leg1.yRot) * dist );
        this.leg1part2.xRot = -1.152F + upangle;
        this.leg1part2b.xRot = -0.743F + upangle;
        this.leg1part2c.xRot = -0.632F + upangle;
        this.leg1part2d.xRot = -1.041F + upangle;
        dist = 14.0F;
        dist = (float)(dist * Math.cos(this.leg1part2.xRot));
        this.leg1part3.z = (float)(this.leg1part2.z - Math.cos(this.leg1part2.yRot) * dist);
        this.leg1part3.x = (float)(this.leg1part2.x - Math.sin(this.leg1part2.yRot) * dist);
        this.leg1part3.xRot = 0.669F - upangle;
        dist = 8.0F;
        dist = (float)Math.abs(dist * Math.cos(this.leg1part3.xRot));
        this.leg1part3b.z = (float)(this.leg1part3.z - Math.cos(this.leg1part3.yRot) * dist);
        this.leg1part3c.z = (float)(this.leg1part3.z - Math.cos(this.leg1part3.yRot) * dist);
        this.leg1part3b.x = (float)(this.leg1part3.x - Math.sin(this.leg1part3.yRot) * dist);
        this.leg1part3c.x = (float)(this.leg1part3.x - Math.sin(this.leg1part3.yRot) * dist);
        this.leg1part3b.xRot = -0.48F - upangle;
        this.leg1part3c.xRot = -0.48F - upangle;
    }

    private void doLeftFrontLeg(float angle, float upangle) {
        this.leg2.yRot = 1.2F + angle;
        this.leg2part2.yRot = this.leg2.yRot;
        this.leg2part2b.yRot = this.leg2.yRot;
        this.leg2part2c.yRot = this.leg2.yRot;
        this.leg2part2d.yRot = this.leg2.yRot;
        this.leg2part3.yRot = this.leg2.yRot;
        this.leg2part3b.yRot = this.leg2.yRot;
        this.leg2part3c.yRot = this.leg2.yRot;
        float dist = 14.0F;
        dist = (float)(dist * Math.cos(this.leg2.xRot));
        this.leg2part2.z = (float)( this.leg2.z - Math.cos(this.leg2.yRot) * dist );
        this.leg2part2b.z = (float)( this.leg2.z - Math.cos(this.leg2.yRot) * dist );
        this.leg2part2c.z = (float)( this.leg2.z - Math.cos(this.leg2.yRot) * dist );
        this.leg2part2d.z = (float)( this.leg2.z - Math.cos(this.leg2.yRot) * dist );
        this.leg2part2.x = (float)( this.leg2.x - Math.sin(this.leg2.yRot) * dist );
        this.leg2part2b.x = (float)( this.leg2.x - Math.sin(this.leg2.yRot) * dist );
        this.leg2part2c.x = (float)( this.leg2.x - Math.sin(this.leg2.yRot) * dist );
        this.leg2part2d.x = (float)( this.leg2.x - Math.sin(this.leg2.yRot) * dist );
        this.leg2part2.xRot = -1.152F + upangle;
        this.leg2part2b.xRot = -0.743F + upangle;
        this.leg2part2c.xRot = -0.632F + upangle;
        this.leg2part2d.xRot = -1.041F + upangle;
        dist = 14.0F;
        dist = (float)(dist * Math.cos(this.leg2part2.xRot));
        this.leg2part3.z = (float)(this.leg2part2.z - Math.cos(this.leg2part2.yRot) * dist);
        this.leg2part3.x = (float)(this.leg2part2.x - Math.sin(this.leg2part2.yRot) * dist);
        this.leg2part3.xRot = 0.669F - upangle;
        dist = 8.0F;
        dist = (float)Math.abs(dist * Math.cos(this.leg2part3.xRot));
        this.leg2part3b.z = (float)(this.leg2part3.z - Math.cos(this.leg2part3.yRot) * dist);
        this.leg2part3c.z = (float)(this.leg2part3.z - Math.cos(this.leg2part3.yRot) * dist);
        this.leg2part3b.x = (float)(this.leg2part3.x - Math.sin(this.leg2part3.yRot) * dist);
        this.leg2part3c.x = (float)(this.leg2part3.x - Math.sin(this.leg2part3.yRot) * dist);
        this.leg2part3b.xRot = -0.48F - upangle;
        this.leg2part3c.xRot = -0.48F - upangle;
    }

    private void doRightRearLeg(float angle, float upangle) {
        this.leg4.yRot = 2.1F + angle;
        this.leg4part2.yRot = this.leg4.yRot;
        this.leg4part2b.yRot = this.leg4.yRot;
        this.leg4part2c.yRot = this.leg4.yRot;
        this.leg4part2d.yRot = this.leg4.yRot;
        this.leg4part3.yRot = this.leg4.yRot;
        this.leg4part3b.yRot = this.leg4.yRot;
        this.leg4part3c.yRot = this.leg4.yRot;
        float dist = 14.0F;
        dist = (float)(dist * Math.cos(this.leg4.xRot));
        this.leg4part2.z = (float)( this.leg4.z - Math.cos(this.leg4.yRot) * dist );
        this.leg4part2b.z = (float)( this.leg4.z - Math.cos(this.leg4.yRot) * dist );
        this.leg4part2c.z = (float)( this.leg4.z - Math.cos(this.leg4.yRot) * dist );
        this.leg4part2d.z = (float)( this.leg4.z - Math.cos(this.leg4.yRot) * dist );
        this.leg4part2.x = (float)( this.leg4.x - Math.sin(this.leg4.yRot) * dist );
        this.leg4part2b.x = (float)( this.leg4.x - Math.sin(this.leg4.yRot) * dist );
        this.leg4part2c.x = (float)( this.leg4.x - Math.sin(this.leg4.yRot) * dist );
        this.leg4part2d.x = (float)( this.leg4.x - Math.sin(this.leg4.yRot) * dist );
        this.leg4part2.xRot = -1.152F + upangle;
        this.leg4part2b.xRot = -0.743F + upangle;
        this.leg4part2c.xRot = -0.632F + upangle;
        this.leg4part2d.xRot = -1.041F + upangle;
        dist = 14.0F;
        dist = (float)(dist * Math.cos(this.leg4part2.xRot));
        this.leg4part3.z = (float)(this.leg4part2.z - Math.cos(this.leg4part2.yRot) * dist);
        this.leg4part3.x = (float)(this.leg4part2.x - Math.sin(this.leg4part2.yRot) * dist);
        this.leg4part3.xRot = 0.669F - upangle;
        dist = 8.0F;
        dist = (float)Math.abs(dist * Math.cos(this.leg4part3.xRot));
        this.leg4part3b.z = (float)(this.leg4part3.z - Math.cos(this.leg4part3.yRot) * dist);
        this.leg4part3c.z = (float)(this.leg4part3.z - Math.cos(this.leg4part3.yRot) * dist);
        this.leg4part3b.x = (float)(this.leg4part3.x - Math.sin(this.leg4part3.yRot) * dist);
        this.leg4part3c.x = (float)(this.leg4part3.x - Math.sin(this.leg4part3.yRot) * dist);
        this.leg4part3b.xRot = -0.48F - upangle;
        this.leg4part3c.xRot = -0.48F - upangle;
    }

    private void doLeftRearLeg(float angle, float upangle) {
        this.leg3.yRot = -2.1F + angle;
        this.leg3part2.yRot = this.leg3.yRot;
        this.leg3part2b.yRot = this.leg3.yRot;
        this.leg3part2c.yRot = this.leg3.yRot;
        this.leg3part2d.yRot = this.leg3.yRot;
        this.leg3part3.yRot = this.leg3.yRot;
        this.leg3part3b.yRot = this.leg3.yRot;
        this.leg3part3c.yRot = this.leg3.yRot;
        float dist = 14.0F;
        dist = (float)(dist * Math.cos(this.leg3.xRot));
        this.leg3part2.z = (float)( this.leg3.z - Math.cos(this.leg3.yRot) * dist );
        this.leg3part2b.z = (float)( this.leg3.z - Math.cos(this.leg3.yRot) * dist );
        this.leg3part2c.z = (float)( this.leg3.z - Math.cos(this.leg3.yRot) * dist );
        this.leg3part2d.z = (float)( this.leg3.z - Math.cos(this.leg3.yRot) * dist );
        this.leg3part2.x = (float)( this.leg3.x - Math.sin(this.leg3.yRot) * dist );
        this.leg3part2b.x = (float)( this.leg3.x - Math.sin(this.leg3.yRot) * dist );
        this.leg3part2c.x = (float)( this.leg3.x - Math.sin(this.leg3.yRot) * dist );
        this.leg3part2d.x = (float)( this.leg3.x - Math.sin(this.leg3.yRot) * dist );
        this.leg3part2.xRot = -1.152F + upangle;
        this.leg3part2b.xRot = -0.743F + upangle;
        this.leg3part2c.xRot = -0.632F + upangle;
        this.leg3part2d.xRot = -1.041F + upangle;
        dist = 14.0F;
        dist = (float)(dist * Math.cos(this.leg3part2.xRot));
        this.leg3part3.z = (float)(this.leg3part2.z - Math.cos(this.leg3part2.yRot) * dist);
        this.leg3part3.x = (float)(this.leg3part2.x - Math.sin(this.leg3part2.yRot) * dist);
        this.leg3part3.xRot = 0.669F - upangle;
        dist = 8.0F;
        dist = (float)Math.abs(dist * Math.cos(this.leg3part3.xRot));
        this.leg3part3b.z = (float)(this.leg3part3.z - Math.cos(this.leg3part3.yRot) * dist);
        this.leg3part3c.z = (float)(this.leg3part3.z - Math.cos(this.leg3part3.yRot) * dist);
        this.leg3part3b.x = (float)(this.leg3part3.x - Math.sin(this.leg3part3.yRot) * dist);
        this.leg3part3c.x = (float)(this.leg3part3.x - Math.sin(this.leg3part3.yRot) * dist);
        this.leg3part3b.xRot = -0.48F - upangle;
        this.leg3part3c.xRot = -0.48F - upangle;
    }
}
