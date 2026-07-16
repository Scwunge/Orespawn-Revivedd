package danger.orespawn.client.model;

import danger.orespawn.entity.Coin;
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
 * Port of gold {@code ModelCoin} (1.7.10 ModelBase, tex 512×512) → HierarchicalModel.
 * Single flat coin Shape1 (256×256×1) at (0,-109,0). Cubes/UVs 1:1.
 * Gold render: yRot = cos(age * 0.05 * wingspeed) * π.
 * Wingspeed default 0.22 matches ClientProxy {@code new ModelCoin(0.22F)}.
 */
@OnlyIn(Dist.CLIENT)
public class ModelCoin extends HierarchicalModel<Coin> {
    public static final ModelLayerLocation LAYER_LOCATION =
            new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "coin"), "main");

    private final float wingspeed;
    private final ModelPart root;
    private final ModelPart shape1;

    public ModelCoin(ModelPart root) {
        this(root, 0.22F);
    }

    public ModelCoin(ModelPart root, float wingspeed) {
        this.root = root;
        this.wingspeed = wingspeed;
        this.shape1 = root.getChild("shape1");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        // gold: addBox(-128, -128, 0, 256, 256, 1) UV 0,0 offset(0, -109, 0) tex 512×512
        root.addOrReplaceChild(
                "shape1",
                CubeListBuilder.create().texOffs(0, 0)
                        .addBox(-128.0F, -128.0F, 0.0F, 256.0F, 256.0F, 1.0F),
                PartPose.offset(0.0F, -109.0F, 0.0F));

        return LayerDefinition.create(mesh, 512, 512);
    }

    @Override
    public ModelPart root() {
        return this.root;
    }

    @Override
    public void setupAnim(
            Coin entity,
            float limbSwing,
            float limbSwingAmount,
            float ageInTicks,
            float netHeadYaw,
            float headPitch) {
        // gold: newangle = cos(f2 * 0.05 * wingspeed) * PI; Shape1.rotateAngleY = newangle
        this.shape1.yRot = Mth.cos(ageInTicks * 0.05F * this.wingspeed) * (float) Math.PI;
    }
}
