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
 * Royal hold mesh — gold {@code RenderRoyal} reuses {@code ModelSlice} (no separate ModelRoyal
 * in 1.7.10). Geometry/UVs/rotations are 1:1 with {@link ModelSlice} (tex 64×128).
 * <p>
 * Gold {@code RenderRoyal} applies scale <b>0.35</b> when equipped (do not bake scale into model).
 * Gold model {@code render()} uses {@code f5 = 1.0F}.
 */
@OnlyIn(Dist.CLIENT)
public class ModelRoyal extends Model {
    private final ModelPart root;

    public ModelRoyal(ModelPart root) {
        super(RenderType::entityCutoutNoCull);
        this.root = root;
    }

    /**
     * Same boxes as gold {@code ModelSlice} (used by {@code RenderRoyal}):
     * Grip 0,-6,0 1×12×1;
     * Blade1 @ (0.5,0,-2.3) yRot 0.3490659  box 0,-41,0 1×34×3;
     * Handguard2 0,-7,-4 1×1×9; Handguard1 -3,-7,0 7×1×1;
     * hg2 @ (0.5,0,0) 0,-9,-7 1×3×3; hg4 @ (0.5,0,0) 0,-9,5 1×3×3;
     * hg3 @ (-2,0,0.5) -4,-9,0 3×3×1; hg1 @ (0,0,0.5) 4,-9,0 3×3×1;
     * BaseGrip -1,5,-1 3×1×3; Bottom 0,6,0 1×1×1;
     * Blade2 @ (0.5,0,-2.3) yRot -0.3490659 box -1,-41,0 1×34×3;
     * Blade3 @ (1.5,0,0.4) yRot -0.3490659 box 0,-41,0 1×34×3;
     * Blade4 @ (-1.5,0,0.7) yRot 0.3490659 box 0,-41,0 1×34×3;
     * Shape1 @ (0.5,-40,-1) 0,-6,0 1×6×3.
     */
    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        root.addOrReplaceChild(
                "grip",
                CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, -6.0F, 0.0F, 1, 12, 1),
                PartPose.ZERO);
        root.addOrReplaceChild(
                "blade1",
                CubeListBuilder.create().texOffs(6, 49).addBox(0.0F, -41.0F, 0.0F, 1, 34, 3),
                PartPose.offsetAndRotation(0.5F, 0.0F, -2.3F, 0.0F, 0.3490659F, 0.0F));
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
                CubeListBuilder.create().texOffs(0, 15).addBox(0.0F, -9.0F, -7.0F, 1, 3, 3),
                PartPose.offset(0.5F, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "hg4",
                CubeListBuilder.create().texOffs(0, 22).addBox(0.0F, -9.0F, 5.0F, 1, 3, 3),
                PartPose.offset(0.5F, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "hg3",
                CubeListBuilder.create().texOffs(0, 29).addBox(-4.0F, -9.0F, 0.0F, 3, 3, 1),
                PartPose.offset(-2.0F, 0.0F, 0.5F));
        root.addOrReplaceChild(
                "hg1",
                CubeListBuilder.create().texOffs(0, 34).addBox(4.0F, -9.0F, 0.0F, 3, 3, 1),
                PartPose.offset(0.0F, 0.0F, 0.5F));
        root.addOrReplaceChild(
                "base_grip",
                CubeListBuilder.create().texOffs(0, 39).addBox(-1.0F, 5.0F, -1.0F, 3, 1, 3),
                PartPose.ZERO);
        root.addOrReplaceChild(
                "bottom",
                CubeListBuilder.create().texOffs(0, 45).addBox(0.0F, 6.0F, 0.0F, 1, 1, 1),
                PartPose.ZERO);
        root.addOrReplaceChild(
                "blade2",
                CubeListBuilder.create().texOffs(24, 49).addBox(-1.0F, -41.0F, 0.0F, 1, 34, 3),
                PartPose.offsetAndRotation(0.5F, 0.0F, -2.3F, 0.0F, -0.3490659F, 0.0F));
        root.addOrReplaceChild(
                "blade3",
                CubeListBuilder.create().texOffs(15, 49).addBox(0.0F, -41.0F, 0.0F, 1, 34, 3),
                PartPose.offsetAndRotation(1.5F, 0.0F, 0.4F, 0.0F, -0.3490659F, 0.0F));
        root.addOrReplaceChild(
                "blade4",
                CubeListBuilder.create().texOffs(33, 49).addBox(0.0F, -41.0F, 0.0F, 1, 34, 3),
                PartPose.offsetAndRotation(-1.5F, 0.0F, 0.7F, 0.0F, 0.3490659F, 0.0F));
        root.addOrReplaceChild(
                "shape1",
                CubeListBuilder.create().texOffs(6, 0).addBox(0.0F, -6.0F, 0.0F, 1, 6, 3),
                PartPose.offset(0.5F, -40.0F, -1.0F));

        // Gold ModelSlice tex size 64×128 (RenderRoyal reuses ModelSlice)
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
