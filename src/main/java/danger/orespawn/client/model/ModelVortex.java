package danger.orespawn.client.model;

import danger.orespawn.entity.Vortex;
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
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

/**
 * Port of gold {@code ModelVortex} (1.7.10 ModelBase, tex 256×128) → 1.21 HierarchicalModel.
 * Single flat plane Shape1 (128×64×0) at (0,22,0). Cubes/UVs 1:1.
 * Gold {@code setRotationAngles} empty — simple continuous Y-spin using wingspeed
 * (ClientProxy {@code new ModelVortex(0.25F)}).
 */
@OnlyIn(Dist.CLIENT)
public class ModelVortex extends HierarchicalModel<Vortex> {
    /** Model layer location. */
    public static final ModelLayerLocation LAYER_LOCATION =
            new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "vortex"), "main");

    private final float wingspeed;
    private final ModelPart root;
    private final ModelPart shape1;

    public ModelVortex(ModelPart root) {
        this(root, 0.25F);
    }

    public ModelVortex(ModelPart root, float wingspeed) {
        this.root = root;
        this.wingspeed = wingspeed;
        this.shape1 = root.getChild("shape1");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        // gold: addBox(-64, -64, 0, 128, 64, 0) — zero-thickness plane
        root.addOrReplaceChild("shape1",
                CubeListBuilder.create().texOffs(0, 0)
                        .addBox(-64.0F, -64.0F, 0.0F, 128.0F, 64.0F, 0.0F),
                PartPose.offset(0.0F, 22.0F, 0.0F));

        return LayerDefinition.create(mesh, 256, 128);
    }

    @Override
    public ModelPart root() {
        return this.root;
    }

    @Override
    public void setupAnim(
            Vortex entity,
            float limbSwing,
            float limbSwingAmount,
            float ageInTicks,
            float netHeadYaw,
            float headPitch) {
        // gold setRotationAngles empty — simple spin so the plane reads as a vortex
        this.shape1.yRot = ageInTicks * 0.35F * this.wingspeed;
    }
}
