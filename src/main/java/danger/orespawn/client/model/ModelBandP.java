package danger.orespawn.client.model;

import danger.orespawn.entity.BandP;
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
 * Port of gold {@code ModelBandP} (1.7.10 ModelBase) → 1.21 HierarchicalModel.
 * Cubes/UVs 1:1. Texture 64×128. Wingspeed default 0.4F matches ClientProxy.
 * Full {@code func_78088_a} walk/belly/arm/head animation.
 */
@OnlyIn(Dist.CLIENT)
public class ModelBandP extends HierarchicalModel<BandP> {
    /** Model layer location. */
    public static final ModelLayerLocation LAYER_LOCATION =
            new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "band_p"), "main");

    private final float wingspeed;
    private final ModelPart root;
    private final ModelPart belly;
    private final ModelPart chest;
    private final ModelPart head;
    private final ModelPart lleg;
    private final ModelPart rleg;
    private final ModelPart larm;
    private final ModelPart rarm;

    public ModelBandP(ModelPart root) {
        this(root, 0.4F);
    }

    public ModelBandP(ModelPart root, float wingspeed) {
        this.root = root;
        this.wingspeed = wingspeed;
        this.belly = root.getChild("belly");
        this.chest = root.getChild("chest");
        this.head = root.getChild("head");
        this.lleg = root.getChild("lleg");
        this.rleg = root.getChild("rleg");
        this.larm = root.getChild("larm");
        this.rarm = root.getChild("rarm");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        root.addOrReplaceChild(
                "belly",
                CubeListBuilder.create().texOffs(0, 61).addBox(-8.0F, -5.0F, -7.0F, 16.0F, 10.0F, 16.0F),
                PartPose.offsetAndRotation(0.0F, 12.0F, 0.0F, 0.0698132F, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "chest",
                CubeListBuilder.create().texOffs(0, 42).addBox(-5.0F, -3.0F, -5.0F, 10.0F, 6.0F, 10.0F),
                PartPose.offset(0.0F, 5.0F, 2.0F));
        root.addOrReplaceChild(
                "head",
                CubeListBuilder.create().texOffs(0, 11).addBox(-3.0F, -5.0F, -3.0F, 6.0F, 6.0F, 6.0F),
                PartPose.offset(0.0F, 1.0F, 3.0F));
        root.addOrReplaceChild(
                "lleg",
                CubeListBuilder.create().texOffs(25, 90).addBox(-2.0F, 0.0F, -3.0F, 6.0F, 8.0F, 6.0F),
                PartPose.offset(2.0F, 16.0F, 2.0F));
        root.addOrReplaceChild(
                "rleg",
                CubeListBuilder.create().texOffs(0, 90).addBox(-4.0F, 0.0F, -3.0F, 6.0F, 8.0F, 6.0F),
                PartPose.offset(-2.0F, 16.0F, 2.0F));
        root.addOrReplaceChild(
                "larm",
                CubeListBuilder.create().texOffs(0, 25).addBox(-1.0F, -1.0F, -2.0F, 4.0F, 10.0F, 4.0F),
                PartPose.offsetAndRotation(6.0F, 4.0F, 3.0F, 0.0F, 0.0F, -0.4886922F));
        root.addOrReplaceChild(
                "rarm",
                CubeListBuilder.create().texOffs(18, 25).addBox(-3.0F, -1.0F, -2.0F, 4.0F, 10.0F, 4.0F),
                PartPose.offsetAndRotation(-6.0F, 4.0F, 3.0F, 0.0F, 0.0F, 0.4886922F));

        return LayerDefinition.create(mesh, 64, 128);
    }

    @Override
    public ModelPart root() {
        return this.root;
    }

    @Override
    public void setupAnim(
            BandP entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        // gold func_78088_a
        float newangle;
        float newangle2;
        float newangle3;
        if (limbSwingAmount > 0.1F) {
            newangle = Mth.cos(ageInTicks * 1.3F * this.wingspeed) * (float) Math.PI * 0.25F * limbSwingAmount;
            newangle2 = Mth.cos(ageInTicks * 2.6F * this.wingspeed) * (float) Math.PI * 0.025F * limbSwingAmount;
            newangle3 = newangle;
        } else {
            newangle = 0.0F;
            newangle2 = Mth.cos(ageInTicks * 0.6F * this.wingspeed) * (float) Math.PI * 0.005F;
            newangle3 = Mth.cos(ageInTicks * 0.3F * this.wingspeed) * (float) Math.PI * 0.02F;
        }

        this.lleg.xRot = newangle;
        this.rleg.xRot = -newangle;
        this.belly.xRot = 0.07F + newangle2;
        this.larm.xRot = -newangle3;
        this.rarm.xRot = newangle3;
        this.belly.yRot = -newangle / 2.0F;
        this.head.yRot = (float) Math.toRadians(netHeadYaw);
        this.head.xRot = (float) Math.toRadians(headPitch);
    }
}
