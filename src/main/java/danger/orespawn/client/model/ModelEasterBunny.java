package danger.orespawn.client.model;

import danger.orespawn.entity.EasterBunny;
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
 * Port of gold {@code ModelEasterBunny} (1.7.10 ModelBase) → 1.21 HierarchicalModel.
 * Cubes/UVs 1:1. Texture 64×128. Wingspeed default 0.55F matches ClientProxy.
 * Full {@code func_78088_a} hop/ear animation.
 */
@OnlyIn(Dist.CLIENT)
public class ModelEasterBunny extends HierarchicalModel<EasterBunny> {
    /** Model layer location. */
    public static final ModelLayerLocation LAYER_LOCATION =
            new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "easter_bunny"), "main");

    private final float wingspeed;
    private final ModelPart root;
    private final ModelPart body;
    private final ModelPart tail;
    private final ModelPart lfoot;
    private final ModelPart lleg;
    private final ModelPart upperbody;
    private final ModelPart head;
    private final ModelPart nose;
    private final ModelPart lear;
    private final ModelPart lpaw;
    private final ModelPart rleg;
    private final ModelPart rfoot;
    private final ModelPart rear;
    private final ModelPart rpaw;

    public ModelEasterBunny(ModelPart root) {
        this(root, 0.55F);
    }

    public ModelEasterBunny(ModelPart root, float wingspeed) {
        this.root = root;
        this.wingspeed = wingspeed;
        this.body = root.getChild("body");
        this.tail = root.getChild("tail");
        this.lfoot = root.getChild("lfoot");
        this.lleg = root.getChild("lleg");
        this.upperbody = root.getChild("upperbody");
        this.head = root.getChild("head");
        this.nose = root.getChild("nose");
        this.lear = root.getChild("lear");
        this.lpaw = root.getChild("lpaw");
        this.rleg = root.getChild("rleg");
        this.rfoot = root.getChild("rfoot");
        this.rear = root.getChild("rear");
        this.rpaw = root.getChild("rpaw");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        root.addOrReplaceChild(
                "body",
                CubeListBuilder.create().texOffs(0, 44).addBox(-3.0F, 0.0F, -3.0F, 6.0F, 6.0F, 7.0F),
                PartPose.offset(0.0F, 17.0F, 0.0F));
        root.addOrReplaceChild(
                "tail",
                CubeListBuilder.create().texOffs(0, 58).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 4.0F, 4.0F),
                PartPose.offset(0.0F, 19.0F, 6.0F));
        root.addOrReplaceChild(
                "lfoot",
                CubeListBuilder.create().texOffs(0, 30).addBox(-1.0F, 2.0F, -5.0F, 3.0F, 1.0F, 7.0F),
                PartPose.offset(3.0F, 21.0F, 1.0F));
        root.addOrReplaceChild(
                "lleg",
                CubeListBuilder.create().texOffs(0, 20).addBox(0.0F, -2.0F, -2.0F, 1.0F, 4.0F, 5.0F),
                PartPose.offset(3.0F, 21.0F, 1.0F));
        root.addOrReplaceChild(
                "upperbody",
                CubeListBuilder.create().texOffs(42, 27).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 1.0F, 5.0F),
                PartPose.offset(0.0F, 16.0F, -1.0F));
        root.addOrReplaceChild(
                "head",
                CubeListBuilder.create().texOffs(40, 17).addBox(-2.5F, 0.0F, -2.0F, 5.0F, 4.0F, 5.0F),
                PartPose.offset(0.0F, 12.0F, -2.0F));
        root.addOrReplaceChild(
                "nose",
                CubeListBuilder.create().texOffs(44, 9).addBox(-1.0F, -1.0F, 0.0F, 2.0F, 2.0F, 1.0F),
                PartPose.offset(0.0F, 15.0F, -5.0F));
        root.addOrReplaceChild(
                "lear",
                CubeListBuilder.create().texOffs(54, 0).addBox(0.0F, -10.0F, -1.0F, 1.0F, 10.0F, 3.0F),
                PartPose.offsetAndRotation(2.0F, 13.0F, -1.0F, -0.2268928F, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "lpaw",
                CubeListBuilder.create().texOffs(6, 7).addBox(0.0F, 0.0F, 0.0F, 1.0F, 3.0F, 1.0F),
                PartPose.offset(0.5F, 19.0F, -4.0F));
        root.addOrReplaceChild(
                "rleg",
                CubeListBuilder.create().texOffs(21, 20).addBox(0.0F, -2.0F, -2.0F, 1.0F, 4.0F, 5.0F),
                PartPose.offset(-4.0F, 21.0F, 1.0F));
        root.addOrReplaceChild(
                "rfoot",
                CubeListBuilder.create().texOffs(21, 30).addBox(-1.0F, 2.0F, -5.0F, 3.0F, 1.0F, 7.0F),
                PartPose.offset(-4.0F, 21.0F, 1.0F));
        root.addOrReplaceChild(
                "rear",
                CubeListBuilder.create().texOffs(32, 0).addBox(0.0F, -10.0F, -1.0F, 1.0F, 10.0F, 3.0F),
                PartPose.offsetAndRotation(-3.0F, 13.0F, -1.0F, -0.418879F, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "rpaw",
                CubeListBuilder.create().texOffs(0, 7).addBox(0.0F, 0.0F, 0.0F, 1.0F, 3.0F, 1.0F),
                PartPose.offset(-1.5F, 19.0F, -4.0F));

        return LayerDefinition.create(mesh, 64, 128);
    }

    @Override
    public ModelPart root() {
        return this.root;
    }

    @Override
    public void setupAnim(
            EasterBunny entity,
            float limbSwing,
            float limbSwingAmount,
            float ageInTicks,
            float netHeadYaw,
            float headPitch) {
        // gold func_78088_a
        float newangle;
        float newangle2;
        if (limbSwingAmount > 0.1F) {
            newangle = Mth.cos(ageInTicks * 2.6F * this.wingspeed) * (float) Math.PI * 0.15F * limbSwingAmount;
            newangle2 = Mth.cos(ageInTicks * 1.3F * this.wingspeed) * (float) Math.PI * 0.1F * limbSwingAmount;
        } else {
            newangle = 0.0F;
            newangle2 = Mth.cos(ageInTicks * 1.3F * this.wingspeed) * (float) Math.PI * 0.01F;
        }

        this.lleg.xRot = newangle;
        this.lfoot.xRot = newangle;
        this.rleg.xRot = -newangle;
        this.rfoot.xRot = -newangle;
        this.lear.xRot = -0.226F + newangle2;
        this.rear.xRot = -0.418F - newangle2;
    }
}
