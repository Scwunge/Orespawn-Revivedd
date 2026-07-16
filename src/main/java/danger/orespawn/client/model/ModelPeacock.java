package danger.orespawn.client.model;

import danger.orespawn.entity.Peacock;
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
 * Port of gold {@code ModelPeacock} (1.7.10 ModelBase) → 1.21 HierarchicalModel.
 * Cubes/UVs 1:1. Texture 128×128. Wingspeed default 0.75F matches ClientProxy.
 */
@OnlyIn(Dist.CLIENT)
public class ModelPeacock extends HierarchicalModel<Peacock> {
    /** Model layer location. */
    public static final ModelLayerLocation LAYER_LOCATION =
            new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "peacock"), "main");

    private final float wingspeed;
    private final ModelPart root;
    private final ModelPart lleg;
    private final ModelPart rleg;
    private final ModelPart body;
    private final ModelPart neck;
    private final ModelPart head1;
    private final ModelPart head2;
    private final ModelPart hf1;
    private final ModelPart hf2;
    private final ModelPart hf3;
    private final ModelPart tailf1;
    private final ModelPart tailf2;
    private final ModelPart tailf3;
    private final ModelPart tailf4;
    private final ModelPart tailf5;
    private final ModelPart tailf6;
    private final ModelPart tailf7;

    public ModelPeacock(ModelPart root) {
        this(root, 0.75F);
    }

    public ModelPeacock(ModelPart root, float wingspeed) {
        this.root = root;
        this.wingspeed = wingspeed;
        this.lleg = root.getChild("lleg");
        this.rleg = root.getChild("rleg");
        this.body = root.getChild("body");
        this.neck = root.getChild("neck");
        this.head1 = root.getChild("head1");
        this.head2 = root.getChild("head2");
        this.hf1 = root.getChild("hf1");
        this.hf2 = root.getChild("hf2");
        this.hf3 = root.getChild("hf3");
        this.tailf1 = root.getChild("tailf1");
        this.tailf2 = root.getChild("tailf2");
        this.tailf3 = root.getChild("tailf3");
        this.tailf4 = root.getChild("tailf4");
        this.tailf5 = root.getChild("tailf5");
        this.tailf6 = root.getChild("tailf6");
        this.tailf7 = root.getChild("tailf7");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        root.addOrReplaceChild(
                "lleg",
                CubeListBuilder.create().texOffs(0, 20).addBox(0.0F, 0.0F, 0.0F, 1.0F, 7.0F, 1.0F),
                PartPose.offset(1.0F, 17.0F, 0.0F));
        root.addOrReplaceChild(
                "rleg",
                CubeListBuilder.create().texOffs(5, 20).addBox(0.0F, 0.0F, 0.0F, 1.0F, 7.0F, 1.0F),
                PartPose.offset(-1.0F, 17.0F, 0.0F));
        root.addOrReplaceChild(
                "body",
                CubeListBuilder.create().texOffs(88, 0).addBox(-2.0F, -2.0F, -5.0F, 5.0F, 4.0F, 11.0F),
                PartPose.offsetAndRotation(0.0F, 15.0F, 1.0F, -0.1396263F, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "neck",
                CubeListBuilder.create().texOffs(70, 0).addBox(-0.5F, -1.0F, -6.0F, 2.0F, 2.0F, 6.0F),
                PartPose.offsetAndRotation(0.0F, 14.0F, -3.0F, -0.5585054F, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "head1",
                CubeListBuilder.create().texOffs(56, 0).addBox(-0.5F, -2.0F, -2.0F, 2.0F, 2.0F, 4.0F),
                PartPose.offset(0.0F, 12.0F, -8.0F));
        root.addOrReplaceChild(
                "head2",
                CubeListBuilder.create().texOffs(48, 0).addBox(0.0F, -1.0F, -4.0F, 1.0F, 1.0F, 2.0F),
                PartPose.offset(0.0F, 12.0F, -8.0F));
        root.addOrReplaceChild(
                "hf1",
                CubeListBuilder.create().texOffs(8, 0).addBox(0.5F, -9.0F, -1.5F, 0.0F, 7.0F, 3.0F),
                PartPose.offsetAndRotation(0.0F, 12.0F, -8.0F, 0.4014257F, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "hf2",
                CubeListBuilder.create().texOffs(8, 0).addBox(0.5F, -9.0F, -1.5F, 0.0F, 7.0F, 3.0F),
                PartPose.offsetAndRotation(0.0F, 12.0F, -8.0F, -0.1745329F, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "hf3",
                CubeListBuilder.create().texOffs(8, 0).addBox(0.5F, -9.0F, -1.5F, 0.0F, 7.0F, 3.0F),
                PartPose.offsetAndRotation(0.0F, 12.0F, -8.0F, (float) (-Math.PI * 2.0 / 9.0), 0.0F, 0.0F));
        root.addOrReplaceChild(
                "tailf1",
                CubeListBuilder.create().texOffs(0, 50).addBox(-4.0F, 0.0F, 0.0F, 8.0F, 0.0F, 30.0F),
                PartPose.offset(0.5F, 14.0F, 7.0F));
        root.addOrReplaceChild(
                "tailf2",
                CubeListBuilder.create().texOffs(0, 50).addBox(-4.0F, 0.0F, 0.0F, 8.0F, 0.0F, 30.0F),
                PartPose.offset(0.5F, 14.0F, 7.0F));
        root.addOrReplaceChild(
                "tailf3",
                CubeListBuilder.create().texOffs(0, 50).addBox(-4.0F, 0.0F, 0.0F, 8.0F, 0.0F, 30.0F),
                PartPose.offset(0.5F, 14.0F, 7.0F));
        root.addOrReplaceChild(
                "tailf4",
                CubeListBuilder.create().texOffs(0, 50).addBox(-4.0F, 0.0F, 0.0F, 8.0F, 0.0F, 30.0F),
                PartPose.offset(0.5F, 14.0F, 7.0F));
        root.addOrReplaceChild(
                "tailf5",
                CubeListBuilder.create().texOffs(0, 50).addBox(-4.0F, 0.0F, 0.0F, 8.0F, 0.0F, 30.0F),
                PartPose.offset(0.5F, 14.0F, 7.0F));
        root.addOrReplaceChild(
                "tailf6",
                CubeListBuilder.create().texOffs(0, 50).addBox(-4.0F, 0.0F, 0.0F, 8.0F, 0.0F, 30.0F),
                PartPose.offset(0.5F, 14.0F, 7.0F));
        root.addOrReplaceChild(
                "tailf7",
                CubeListBuilder.create().texOffs(0, 50).addBox(-4.0F, 0.0F, 0.0F, 8.0F, 0.0F, 30.0F),
                PartPose.offset(0.514F, 14.0F, 7.0F));

        return LayerDefinition.create(mesh, 128, 128);
    }

    @Override
    public ModelPart root() {
        return this.root;
    }

    @Override
    public void setupAnim(
            Peacock entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        float newangle;
        if (limbSwingAmount > 0.1F) {
            newangle = Mth.cos(ageInTicks * 1.3F * this.wingspeed) * (float) Math.PI * 0.15F * limbSwingAmount;
        } else {
            newangle = 0.0F;
        }
        this.lleg.xRot = newangle;
        this.rleg.xRot = -newangle;

        if (entity.getBlink() > 0) {
            // gold open display
            this.hf1.xRot = 0.401F;
            this.hf2.xRot = -0.174F;
            this.hf3.xRot = -0.698F;
            this.tailf1.xRot = 1.047F;
            this.tailf2.xRot = 1.047F;
            this.tailf3.xRot = 1.047F;
            this.tailf4.xRot = 1.047F;
            this.tailf5.xRot = 1.047F;
            this.tailf6.xRot = 1.047F;
            this.tailf7.xRot = 1.047F;
            this.tailf1.zRot = -0.4F;
            this.tailf2.zRot = -0.8F;
            this.tailf3.zRot = -1.2F;
            this.tailf4.zRot = 0.4F;
            this.tailf5.zRot = 0.8F;
            this.tailf6.zRot = 1.2F;
        } else {
            // gold closed
            this.hf1.xRot = -1.06F;
            this.hf2.xRot = -1.06F;
            this.hf3.xRot = -1.06F;
            this.tailf1.xRot = 0.0F;
            this.tailf2.xRot = 0.0F;
            this.tailf3.xRot = 0.0F;
            this.tailf4.xRot = 0.0F;
            this.tailf5.xRot = 0.0F;
            this.tailf6.xRot = 0.0F;
            this.tailf7.xRot = 0.0F;
            this.tailf1.zRot = 0.0F;
            this.tailf2.zRot = 0.0F;
            this.tailf3.zRot = 0.0F;
            this.tailf4.zRot = 0.0F;
            this.tailf5.zRot = 0.0F;
            this.tailf6.zRot = 0.0F;
        }
    }
}
