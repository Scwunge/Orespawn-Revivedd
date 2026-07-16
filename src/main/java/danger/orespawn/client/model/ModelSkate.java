package danger.orespawn.client.model;

import danger.orespawn.entity.Skate;
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
 * Port of gold {@code ModelSkate} (1.7.10 ModelBase, tex 64×32) → 1.21 HierarchicalModel.
 * Cubes / pivots / UV 1:1. Full gold {@code func_78088_a} spike fin bob.
 * wingspeed default 1.0 (stored; unused in gold anim).
 */
@OnlyIn(Dist.CLIENT)
public class ModelSkate extends HierarchicalModel<Skate> {
    /** Model layer location. */
    public static final ModelLayerLocation LAYER_LOCATION =
            new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "skate"), "main");

    private final float wingspeed;
    private final ModelPart root;
    private final ModelPart body;
    private final ModelPart tail1;
    private final ModelPart shape1;

    public ModelSkate(ModelPart root) {
        this(root, 1.0F);
    }

    public ModelSkate(ModelPart root, float wingspeed) {
        this.root = root;
        this.wingspeed = wingspeed;
        this.body = root.getChild("body");
        this.tail1 = root.getChild("tail1");
        this.shape1 = root.getChild("shape1");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        // gold tex 64×32; diamond body yRot = π/4; spike base xRot = π/4
        root.addOrReplaceChild(
                "body",
                CubeListBuilder.create().texOffs(0, 13).addBox(-3.0F, 0.0F, -3.0F, 6.0F, 1.0F, 6.0F),
                PartPose.offsetAndRotation(0.0F, 22.0F, 0.0F, 0.0F, (float) (Math.PI / 4), 0.0F));
        root.addOrReplaceChild(
                "tail1",
                CubeListBuilder.create().texOffs(0, 0).addBox(-0.5F, 0.0F, 0.0F, 1.0F, 1.0F, 11.0F),
                PartPose.offset(0.0F, 22.0F, 3.0F));
        root.addOrReplaceChild(
                "shape1",
                CubeListBuilder.create().texOffs(0, 21).addBox(-0.5F, 0.0F, 0.0F, 1.0F, 1.0F, 4.0F),
                PartPose.offsetAndRotation(0.0F, 22.0F, 5.0F, (float) (Math.PI / 4), 0.0F, 0.0F));

        return LayerDefinition.create(mesh, 64, 32);
    }

    @Override
    public ModelPart root() {
        return this.root;
    }

    @Override
    public void setupAnim(
            Skate entity,
            float limbSwing,
            float limbSwingAmount,
            float ageInTicks,
            float netHeadYaw,
            float headPitch) {
        // suppress unused field warning (gold stores wingspeed but never multiplies anim by it)
        @SuppressWarnings("unused")
        float ws = this.wingspeed;

        // gold: f1>0.1 → stronger spike bob; else idle
        float newangle;
        if (limbSwingAmount > 0.1F) {
            newangle = Mth.cos(ageInTicks * 1.2F) * (float) Math.PI * 0.15F * limbSwingAmount;
        } else {
            newangle = Mth.cos(ageInTicks * 0.4F) * (float) Math.PI * 0.05F;
        }

        // diamond body base yRot stays π/4 (static)
        this.body.yRot = (float) (Math.PI / 4);
        this.body.xRot = 0.0F;
        this.body.zRot = 0.0F;

        // gold: Shape1.rotateAngleX = 0.785F + newangle
        this.shape1.xRot = 0.785F + newangle;
        this.shape1.yRot = 0.0F;
        this.shape1.zRot = 0.0F;

        // tail1 is static in gold
        this.tail1.xRot = 0.0F;
        this.tail1.yRot = 0.0F;
        this.tail1.zRot = 0.0F;
    }
}
