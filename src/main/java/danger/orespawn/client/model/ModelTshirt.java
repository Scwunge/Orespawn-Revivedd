package danger.orespawn.client.model;

import danger.orespawn.entity.Tshirt;
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
 * Port of gold {@code ModelTshirt} (1.7.10 ModelBase, tex 512×256) → HierarchicalModel.
 * Shape1 (256×64×1) UV 0,0 + Shape2 (128×128×1) UV 0,64 at (0,-128,0). Cubes/UVs 1:1.
 * Gold render: both yRot = cos(age * 0.05 * wingspeed) * π.
 * Wingspeed default 0.22 matches ClientProxy {@code new ModelTshirt(0.22F)}.
 */
@OnlyIn(Dist.CLIENT)
public class ModelTshirt extends HierarchicalModel<Tshirt> {
    public static final ModelLayerLocation LAYER_LOCATION =
            new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "tshirt"), "main");

    private final float wingspeed;
    private final ModelPart root;
    private final ModelPart shape1;
    private final ModelPart shape2;

    public ModelTshirt(ModelPart root) {
        this(root, 0.22F);
    }

    public ModelTshirt(ModelPart root, float wingspeed) {
        this.root = root;
        this.wingspeed = wingspeed;
        this.shape1 = root.getChild("shape1");
        this.shape2 = root.getChild("shape2");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        // gold: Shape1 addBox(-128, -64, 0, 256, 64, 1) UV 0,0 offset(0, -128, 0)
        root.addOrReplaceChild(
                "shape1",
                CubeListBuilder.create().texOffs(0, 0)
                        .addBox(-128.0F, -64.0F, 0.0F, 256.0F, 64.0F, 1.0F),
                PartPose.offset(0.0F, -128.0F, 0.0F));

        // gold: Shape2 addBox(-64, 0, 0, 128, 128, 1) UV 0,64 offset(0, -128, 0)
        root.addOrReplaceChild(
                "shape2",
                CubeListBuilder.create().texOffs(0, 64)
                        .addBox(-64.0F, 0.0F, 0.0F, 128.0F, 128.0F, 1.0F),
                PartPose.offset(0.0F, -128.0F, 0.0F));

        return LayerDefinition.create(mesh, 512, 256);
    }

    @Override
    public ModelPart root() {
        return this.root;
    }

    @Override
    public void setupAnim(
            Tshirt entity,
            float limbSwing,
            float limbSwingAmount,
            float ageInTicks,
            float netHeadYaw,
            float headPitch) {
        float newangle = Mth.cos(ageInTicks * 0.05F * this.wingspeed) * (float) Math.PI;
        this.shape1.yRot = newangle;
        this.shape2.yRot = newangle;
    }
}
