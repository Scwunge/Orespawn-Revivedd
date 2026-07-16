package danger.orespawn.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.RenderType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

/**
 * 1.7.10 {@code ModelBertha} — cube sizes/offsets 1:1 (tex 64×128).
 * Gold {@code RenderBertha} applies scale 0.25 when equipped (do not bake scale into model).
 */
@OnlyIn(Dist.CLIENT)
public class ModelBertha extends Model {
    private final ModelPart root;

    public ModelBertha(ModelPart root) {
        super(RenderType::entityCutoutNoCull);
        this.root = root;
    }

    /**
     * Gold ModelBertha constructor boxes (addBox / setRotationPoint):
     * Grip 0,-6,0 1×12×1; Blade 0,-41,-1 1×34×3; Handguard2 0,-7,-4 1×1×9;
     * Handguard1 -3,-7,0 7×1×1; hg2 0,-8,-5 1×1×1; hg4 0,-8,5 1×1×1;
     * hg3 -4,-8,0 1×1×1; hg1 4,-8,0 1×1×1; BaseGrip -1,5,-1 3×1×3;
     * Tip1 0,-42,-0.5 1×1×2; Tip2 0,-43,0 1×1×1; Bottom 0,6,0 1×1×1.
     */
    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        root.addOrReplaceChild(
                "grip",
                CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, -6.0F, 0.0F, 1, 12, 1),
                PartPose.ZERO);
        root.addOrReplaceChild(
                "blade",
                CubeListBuilder.create().texOffs(6, 0).addBox(0.0F, -41.0F, -1.0F, 1, 34, 3),
                PartPose.ZERO);
        root.addOrReplaceChild(
                "handguard2",
                CubeListBuilder.create().texOffs(16, 0).addBox(0.0F, -7.0F, -4.0F, 1, 1, 9),
                PartPose.ZERO);
        root.addOrReplaceChild(
                "handguard1",
                CubeListBuilder.create().texOffs(18, 12).addBox(-3.0F, -7.0F, 0.0F, 7, 1, 1),
                PartPose.ZERO);
        root.addOrReplaceChild(
                "hg2",
                CubeListBuilder.create().texOffs(0, 15).addBox(0.0F, -8.0F, -5.0F, 1, 1, 1),
                PartPose.ZERO);
        root.addOrReplaceChild(
                "hg4",
                CubeListBuilder.create().texOffs(0, 18).addBox(0.0F, -8.0F, 5.0F, 1, 1, 1),
                PartPose.ZERO);
        root.addOrReplaceChild(
                "hg3",
                CubeListBuilder.create().texOffs(0, 21).addBox(-4.0F, -8.0F, 0.0F, 1, 1, 1),
                PartPose.ZERO);
        root.addOrReplaceChild(
                "hg1",
                CubeListBuilder.create().texOffs(0, 24).addBox(4.0F, -8.0F, 0.0F, 1, 1, 1),
                PartPose.ZERO);
        root.addOrReplaceChild(
                "base_grip",
                CubeListBuilder.create().texOffs(0, 39).addBox(-1.0F, 5.0F, -1.0F, 3, 1, 3),
                PartPose.ZERO);
        root.addOrReplaceChild(
                "tip1",
                CubeListBuilder.create().texOffs(21, 16).addBox(0.0F, -42.0F, -0.5F, 1, 1, 2),
                PartPose.ZERO);
        root.addOrReplaceChild(
                "tip2",
                CubeListBuilder.create().texOffs(22, 20).addBox(0.0F, -43.0F, 0.0F, 1, 1, 1),
                PartPose.ZERO);
        root.addOrReplaceChild(
                "bottom",
                CubeListBuilder.create().texOffs(0, 45).addBox(0.0F, 6.0F, 0.0F, 1, 1, 1),
                PartPose.ZERO);

        // Gold tex size 64×128
        return LayerDefinition.create(mesh, 64, 128);
    }

    @Override
    public void renderToBuffer(
            PoseStack poseStack,
            VertexConsumer buffer,
            int packedLight,
            int packedOverlay,
            int color) {
        this.root.render(poseStack, buffer, packedLight, packedOverlay, color);
    }
}
