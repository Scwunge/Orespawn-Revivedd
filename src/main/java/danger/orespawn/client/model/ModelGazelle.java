package danger.orespawn.client.model;

import danger.orespawn.entity.Gazelle;
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
 * Port of gold {@code ModelGazelle} (1.7.10 ModelBase) → 1.21 HierarchicalModel.
 * Cubes/UVs 1:1. Texture 64×64. Wingspeed default 0.65F matches ClientProxy.
 * Full {@code func_78088_a} leg/head/tail animation.
 */
@OnlyIn(Dist.CLIENT)
public class ModelGazelle extends HierarchicalModel<Gazelle> {
    /** Model layer location. */
    public static final ModelLayerLocation LAYER_LOCATION =
            new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "gazelle"), "main");

    private final float wingspeed;
    private final ModelPart root;
    private final ModelPart Chest;
    private final ModelPart lfleg1;
    private final ModelPart lrleg2;
    private final ModelPart lrleg1;
    private final ModelPart rfleg3;
    private final ModelPart rrleg2;
    private final ModelPart rrleg3;
    private final ModelPart rfleg2;
    private final ModelPart lrleg4;
    private final ModelPart tail;
    private final ModelPart lear;
    private final ModelPart rrleg1;
    private final ModelPart rfleg1;
    private final ModelPart lrleg3;
    private final ModelPart lfleg2;
    private final ModelPart rrleg5;
    private final ModelPart rrleg4;
    private final ModelPart lfleg3;
    private final ModelPart rfleg4;
    private final ModelPart lfleg4;
    private final ModelPart lrleg5;
    private final ModelPart Body;
    private final ModelPart neck;
    private final ModelPart la3;
    private final ModelPart throatfluff;
    private final ModelPart rear;
    private final ModelPart head;
    private final ModelPart ra1;
    private final ModelPart la1;
    private final ModelPart la2;
    private final ModelPart ra2;
    private final ModelPart ra3;
    private final ModelPart nose;
    private final ModelPart mouth;

    public ModelGazelle(ModelPart root) {
        this(root, 0.65F);
    }

    public ModelGazelle(ModelPart root, float wingspeed) {
        this.root = root;
        this.wingspeed = wingspeed;
        this.Chest = root.getChild("Chest");
        this.lfleg1 = root.getChild("lfleg1");
        this.lrleg2 = root.getChild("lrleg2");
        this.lrleg1 = root.getChild("lrleg1");
        this.rfleg3 = root.getChild("rfleg3");
        this.rrleg2 = root.getChild("rrleg2");
        this.rrleg3 = root.getChild("rrleg3");
        this.rfleg2 = root.getChild("rfleg2");
        this.lrleg4 = root.getChild("lrleg4");
        this.tail = root.getChild("tail");
        this.lear = root.getChild("lear");
        this.rrleg1 = root.getChild("rrleg1");
        this.rfleg1 = root.getChild("rfleg1");
        this.lrleg3 = root.getChild("lrleg3");
        this.lfleg2 = root.getChild("lfleg2");
        this.rrleg5 = root.getChild("rrleg5");
        this.rrleg4 = root.getChild("rrleg4");
        this.lfleg3 = root.getChild("lfleg3");
        this.rfleg4 = root.getChild("rfleg4");
        this.lfleg4 = root.getChild("lfleg4");
        this.lrleg5 = root.getChild("lrleg5");
        this.Body = root.getChild("Body");
        this.neck = root.getChild("neck");
        this.la3 = root.getChild("la3");
        this.throatfluff = root.getChild("throatfluff");
        this.rear = root.getChild("rear");
        this.head = root.getChild("head");
        this.ra1 = root.getChild("ra1");
        this.la1 = root.getChild("la1");
        this.la2 = root.getChild("la2");
        this.ra2 = root.getChild("ra2");
        this.ra3 = root.getChild("ra3");
        this.nose = root.getChild("nose");
        this.mouth = root.getChild("mouth");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        root.addOrReplaceChild(
                "Chest",
                CubeListBuilder.create().texOffs(12, 57).addBox(0.0F, 0.0F, 0.0F, 5.0F, 2.0F, 3.0F),
                PartPose.offsetAndRotation(-2.5F, 8.0F, -6.0F, 2.342252F, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "lfleg1",
                CubeListBuilder.create().texOffs(0, 31).addBox(0.0F, 0.0F, 0.0F, 2.0F, 6.0F, 3.0F),
                PartPose.offsetAndRotation(2.0F, 6.0F, -6.0F, 0.2974289F, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "lrleg2",
                CubeListBuilder.create().texOffs(16, 49).addBox(0.0F, 5.0F, -1.0F, 2.0F, 2.0F, 6.0F),
                PartPose.offsetAndRotation(2.0F, 4.0F, 3.0F, 0.1858931F, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "lrleg1",
                CubeListBuilder.create().texOffs(23, 31).addBox(0.0F, 0.0F, 0.0F, 2.0F, 6.0F, 3.0F),
                PartPose.offset(2.0F, 4.0F, 3.0F));
        root.addOrReplaceChild(
                "rfleg3",
                CubeListBuilder.create().texOffs(40, 49).addBox(0.0F, 10.0F, 6.0F, 2.0F, 6.0F, 2.0F),
                PartPose.offsetAndRotation(-4.0F, 5.966667F, -6.0F, -0.4089647F, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "rrleg2",
                CubeListBuilder.create().texOffs(16, 49).addBox(0.0F, 5.0F, -1.0F, 2.0F, 2.0F, 6.0F),
                PartPose.offsetAndRotation(-4.0F, 4.0F, 3.0F, 0.1858931F, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "rrleg3",
                CubeListBuilder.create().texOffs(32, 11).addBox(0.0F, 4.0F, 5.0F, 2.0F, 12.0F, 2.0F),
                PartPose.offsetAndRotation(-4.0F, 3.966667F, 3.0F, -0.0743572F, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "rfleg2",
                CubeListBuilder.create().texOffs(24, 11).addBox(0.0F, 2.0F, 2.0F, 2.0F, 12.0F, 2.0F),
                PartPose.offsetAndRotation(-4.0F, 5.966667F, -6.0F, -0.0743572F, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "lrleg4",
                CubeListBuilder.create().texOffs(32, 49).addBox(0.0F, 11.0F, 9.5F, 2.0F, 6.0F, 2.0F),
                PartPose.offsetAndRotation(2.0F, 4.0F, 3.0F, -0.4089647F, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "tail",
                CubeListBuilder.create().texOffs(0, 49).addBox(0.0F, 0.0F, 0.0F, 4.0F, 4.0F, 4.0F),
                PartPose.offsetAndRotation(-2.0F, 0.0F, 4.0F, 0.9666439F, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "lear",
                CubeListBuilder.create().texOffs(18, 0).addBox(-5.0F, -3.0F, 2.0F, 3.0F, 2.0F, 1.0F),
                PartPose.offsetAndRotation(0.0F, -9.0F, -6.0F, -0.1047198F, 1.570796F, 0.0F));
        root.addOrReplaceChild(
                "rrleg1",
                CubeListBuilder.create().texOffs(23, 31).addBox(0.0F, 0.0F, 0.0F, 2.0F, 6.0F, 3.0F),
                PartPose.offset(-4.0F, 4.0F, 3.0F));
        root.addOrReplaceChild(
                "rfleg1",
                CubeListBuilder.create().texOffs(0, 31).addBox(0.0F, 0.0F, 0.0F, 2.0F, 6.0F, 3.0F),
                PartPose.offsetAndRotation(-4.0F, 6.0F, -6.0F, 0.2974289F, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "lrleg3",
                CubeListBuilder.create().texOffs(32, 11).addBox(0.0F, 4.0F, 5.0F, 2.0F, 12.0F, 2.0F),
                PartPose.offsetAndRotation(2.0F, 3.966667F, 3.0F, -0.0743572F, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "lfleg2",
                CubeListBuilder.create().texOffs(24, 11).addBox(0.0F, 2.0F, 2.0F, 2.0F, 12.0F, 2.0F),
                PartPose.offsetAndRotation(2.0F, 5.966667F, -6.0F, -0.0743572F, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "rrleg5",
                CubeListBuilder.create().texOffs(0, 58).addBox(-0.5F, 17.0F, 2.0F, 3.0F, 3.0F, 3.0F),
                PartPose.offset(-4.0F, 4.0F, 3.0F));
        root.addOrReplaceChild(
                "rrleg4",
                CubeListBuilder.create().texOffs(32, 49).addBox(0.0F, 11.0F, 9.5F, 2.0F, 6.0F, 2.0F),
                PartPose.offsetAndRotation(-4.0F, 3.966667F, 3.0F, -0.4089647F, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "lfleg3",
                CubeListBuilder.create().texOffs(40, 49).addBox(0.0F, 10.0F, 6.0F, 2.0F, 6.0F, 2.0F),
                PartPose.offsetAndRotation(2.0F, 5.966667F, -6.0F, -0.4089647F, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "rfleg4",
                CubeListBuilder.create().texOffs(0, 58).addBox(-0.5F, 15.0F, -1.0F, 3.0F, 3.0F, 3.0F),
                PartPose.offset(-4.0F, 6.0F, -6.0F));
        root.addOrReplaceChild(
                "lfleg4",
                CubeListBuilder.create().texOffs(0, 58).addBox(-0.5F, 15.0F, -1.0F, 3.0F, 3.0F, 3.0F),
                PartPose.offset(2.0F, 6.0F, -6.0F));
        root.addOrReplaceChild(
                "lrleg5",
                CubeListBuilder.create().texOffs(0, 58).addBox(-0.5F, 17.0F, 2.0F, 3.0F, 3.0F, 3.0F),
                PartPose.offset(2.0F, 4.0F, 3.0F));
        root.addOrReplaceChild(
                "Body",
                CubeListBuilder.create().texOffs(0, 12).addBox(0.0F, 0.0F, 0.0F, 6.0F, 6.0F, 13.0F),
                PartPose.offsetAndRotation(-3.0F, 2.0F, -7.0F, 0.2230717F, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "neck",
                CubeListBuilder.create().texOffs(0, 31).addBox(0.0F, 0.0F, 0.0F, 5.0F, 5.0F, 13.0F),
                PartPose.offsetAndRotation(-2.5F, 6.0F, -8.0F, 1.524323F, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "la3",
                CubeListBuilder.create().texOffs(4, 12).addBox(0.5F, -12.5F, 3.0F, 1.0F, 5.0F, 1.0F),
                PartPose.offsetAndRotation(0.0F, -9.0F, -6.0F, -0.3346075F, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "throatfluff",
                CubeListBuilder.create().texOffs(36, 41).addBox(0.0F, -2.0F, 0.0F, 4.0F, 3.0F, 5.0F),
                PartPose.offsetAndRotation(-2.0F, 0.0F, -8.0F, 1.07818F, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "rear",
                CubeListBuilder.create().texOffs(18, 0).addBox(-5.0F, -3.0F, -3.0F, 3.0F, 2.0F, 1.0F),
                PartPose.offsetAndRotation(0.0F, -9.0F, -6.0F, 0.1047198F, 1.570796F, 0.0F));
        root.addOrReplaceChild(
                "head",
                CubeListBuilder.create().texOffs(0, 0).addBox(-3.0F, -3.0F, -3.0F, 6.0F, 6.0F, 6.0F),
                PartPose.offset(0.0F, -9.0F, -6.0F));
        root.addOrReplaceChild(
                "ra1",
                CubeListBuilder.create().texOffs(0, 12).addBox(-1.5F, -5.0F, 0.0F, 1.0F, 4.0F, 1.0F),
                PartPose.offsetAndRotation(0.0F, -9.0F, -6.0F, -0.3717861F, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "la1",
                CubeListBuilder.create().texOffs(0, 12).addBox(0.5F, -5.0F, 0.0F, 1.0F, 4.0F, 1.0F),
                PartPose.offsetAndRotation(0.0F, -9.0F, -6.0F, -0.3717861F, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "la2",
                CubeListBuilder.create().texOffs(0, 17).addBox(0.5F, -8.5F, -3.0F, 1.0F, 5.0F, 1.0F),
                PartPose.offsetAndRotation(0.0F, -9.0F, -6.0F, -1.041001F, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "ra2",
                CubeListBuilder.create().texOffs(0, 17).addBox(-1.5F, -8.5F, -3.0F, 1.0F, 5.0F, 1.0F),
                PartPose.offsetAndRotation(0.0F, -9.0F, -6.0F, -1.041001F, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "ra3",
                CubeListBuilder.create().texOffs(4, 12).addBox(-1.5F, -12.5F, 3.0F, 1.0F, 5.0F, 1.0F),
                PartPose.offsetAndRotation(0.0F, -9.0F, -6.0F, -0.3346075F, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "nose",
                CubeListBuilder.create().texOffs(24, 0).addBox(-2.5F, 0.0F, -7.0F, 5.0F, 3.0F, 5.0F),
                PartPose.offset(0.0F, -9.0F, -6.0F));
        root.addOrReplaceChild(
                "mouth",
                CubeListBuilder.create().texOffs(28, 57).addBox(-2.0F, 2.0F, -6.0F, 4.0F, 2.0F, 5.0F),
                PartPose.offset(0.0F, -9.0F, -6.0F));

        return LayerDefinition.create(mesh, 64, 64);
    }

    @Override
    public ModelPart root() {
        return this.root;
    }

    @Override
    public void setupAnim(
            Gazelle entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        // gold func_78088_a
        float newangle;
        if (limbSwingAmount > 0.1F) {
            newangle = Mth.cos(ageInTicks * 1.1F * this.wingspeed) * (float) Math.PI * 0.12F * limbSwingAmount;
        } else {
            newangle = 0.0F;
        }

        this.lfleg1.xRot = 0.297F + newangle;
        this.lfleg2.xRot = -0.074F + newangle;
        this.lfleg3.xRot = -0.409F + newangle;
        this.lfleg4.xRot = newangle;
        this.rfleg1.xRot = 0.297F - newangle;
        this.rfleg2.xRot = -0.074F - newangle;
        this.rfleg3.xRot = -0.409F - newangle;
        this.rfleg4.xRot = -newangle;
        this.lrleg1.xRot = -newangle;
        this.lrleg2.xRot = 0.185F - newangle;
        this.lrleg3.xRot = -0.074F - newangle;
        this.lrleg4.xRot = -0.409F - newangle;
        this.lrleg5.xRot = -newangle;
        this.rrleg1.xRot = newangle;
        this.rrleg2.xRot = 0.185F + newangle;
        this.rrleg3.xRot = -0.074F + newangle;
        this.rrleg4.xRot = -0.409F + newangle;
        this.rrleg5.xRot = newangle;

        newangle = Mth.cos(ageInTicks * 0.5F) * (float) Math.PI * 0.02F;
        this.head.yRot = (float) Math.toRadians(netHeadYaw) * 0.45F;
        this.nose.yRot = this.head.yRot;
        this.mouth.yRot = this.head.yRot;
        this.lear.yRot = 1.57F + this.head.yRot + newangle;
        this.rear.yRot = 1.57F + this.head.yRot + newangle;
        this.la1.yRot = this.head.yRot;
        this.la2.yRot = this.head.yRot;
        this.la3.yRot = this.head.yRot;
        this.ra1.yRot = this.head.yRot;
        this.ra2.yRot = this.head.yRot;
        this.ra3.yRot = this.head.yRot;

        // gold: if not sitting, wag tail
        if (!entity.isOreSpawnSitting()) {
            this.tail.xRot = 1.0F + Mth.cos(ageInTicks * 0.1F) * (float) Math.PI * 0.06F;
        }
    }
}
