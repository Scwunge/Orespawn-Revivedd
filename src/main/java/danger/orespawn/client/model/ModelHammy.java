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
 * 1.7.10 {@code ModelHammy} — cube sizes/offsets/rotations 1:1 (tex 128×256).
 * Gold {@code RenderHammy} applies scale <b>0.15</b> when equipped (do not bake scale into model).
 * Gold {@code render()} uses {@code f5 = 1.0F}.
 * <p>
 * Geometry: tri-handle shaft Y=-12.+24; head bands/spikes around Y≈-12.-29, X≈±20.
 */
@OnlyIn(Dist.CLIENT)
public class ModelHammy extends Model {
    private final ModelPart root;

    public ModelHammy(ModelPart root) {
        super(RenderType::entityCutoutNoCull);
        this.root = root;
    }

    /**
     * Gold ModelHammy constructor boxes (addBox / setRotationPoint / setRotation) — full 1:1.
     * Rotations: π/4 = 0.7853982F, π/3 ≈ 1.047198F.
     */
    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        final float pi4 = (float) (Math.PI / 4);
        final float pi3 = 1.047198F;

        // ——— Handle (3 rotated blades) ———
        root.addOrReplaceChild(
                "handle1",
                CubeListBuilder.create().texOffs(0, 0).addBox(-0.5F, -12.0F, -1.0F, 1, 36, 2),
                PartPose.ZERO);
        root.addOrReplaceChild(
                "handle2",
                CubeListBuilder.create().texOffs(7, 0).addBox(-0.5F, -12.0F, -1.0F, 1, 36, 2),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, pi3, 0.0F));
        root.addOrReplaceChild(
                "handle3",
                CubeListBuilder.create().texOffs(14, 0).addBox(-0.5F, -12.0F, -1.0F, 1, 36, 2),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -pi3, 0.0F));

        // ——— Head core ———
        root.addOrReplaceChild(
                "head1",
                CubeListBuilder.create().texOffs(0, 230).addBox(-20.0F, -22.0F, -7.0F, 40, 6, 14),
                PartPose.ZERO);
        root.addOrReplaceChild(
                "head2",
                CubeListBuilder.create().texOffs(0, 184).addBox(-20.0F, -26.0F, -3.0F, 40, 14, 6),
                PartPose.ZERO);
        root.addOrReplaceChild(
                "head3",
                CubeListBuilder.create().texOffs(0, 161).addBox(-20.0F, -16.5F, 6.4F, 40, 6, 14),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, pi4, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "head4",
                CubeListBuilder.create().texOffs(0, 207).addBox(-20.0F, -16.5F, -20.4F, 40, 6, 14),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -pi4, 0.0F, 0.0F));

        // ——— Right-side bands (positive X) ———
        root.addOrReplaceChild(
                "band1",
                CubeListBuilder.create().texOffs(0, 88).addBox(12.0F, -22.5F, -8.0F, 5, 7, 1),
                PartPose.ZERO);
        root.addOrReplaceChild(
                "band2",
                CubeListBuilder.create().texOffs(0, 128).addBox(12.0F, -22.5F, 7.0F, 5, 7, 1),
                PartPose.ZERO);
        root.addOrReplaceChild(
                "band3",
                CubeListBuilder.create().texOffs(0, 98).addBox(12.0F, -17.0F, 5.4F, 5, 7, 1),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, pi4, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "band4",
                CubeListBuilder.create().texOffs(0, 118).addBox(12.0F, -16.9F, -6.4F, 5, 7, 1),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -pi4, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "band5",
                CubeListBuilder.create().texOffs(0, 108).addBox(12.0F, -12.0F, -3.5F, 5, 1, 7),
                PartPose.ZERO);
        root.addOrReplaceChild(
                "band6",
                CubeListBuilder.create().texOffs(0, 79).addBox(12.0F, -16.5F, -21.4F, 5, 6, 1),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -pi4, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "band7",
                CubeListBuilder.create().texOffs(0, 138).addBox(12.0F, -17.0F, 20.4F, 5, 7, 1),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, pi4, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "band8",
                CubeListBuilder.create().texOffs(0, 148).addBox(12.0F, -27.0F, -3.5F, 5, 1, 7),
                PartPose.ZERO);

        root.addOrReplaceChild(
                "point1",
                CubeListBuilder.create().texOffs(28, 130).addBox(-2.5F, -29.5F, -0.5F, 5, 5, 1),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, pi4));

        // ——— Right spikes ———
        root.addOrReplaceChild(
                "spike1",
                CubeListBuilder.create().texOffs(67, 0).addBox(14.0F, -20.0F, -10.0F, 1, 1, 20),
                PartPose.ZERO);
        root.addOrReplaceChild(
                "spike2",
                CubeListBuilder.create().texOffs(49, 0).addBox(14.0F, -29.0F, 0.0F, 1, 20, 1),
                PartPose.ZERO);
        root.addOrReplaceChild(
                "spike3",
                CubeListBuilder.create().texOffs(55, 0).addBox(14.0F, -23.5F, 13.0F, 1, 20, 1),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, pi4, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "spike4",
                CubeListBuilder.create().texOffs(61, 0).addBox(-15.0F, -23.5F, -14.0F, 1, 20, 1),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -pi4, 0.0F, 0.0F));

        // ——— Left-side bands (negative X, "b" parts) ———
        root.addOrReplaceChild(
                "band1b",
                CubeListBuilder.create().texOffs(0, 88).addBox(-17.0F, -22.5F, -8.0F, 5, 7, 1),
                PartPose.ZERO);
        root.addOrReplaceChild(
                "band2b",
                CubeListBuilder.create().texOffs(0, 128).addBox(-17.0F, -22.5F, 7.0F, 5, 7, 1),
                PartPose.ZERO);
        root.addOrReplaceChild(
                "band3b",
                CubeListBuilder.create().texOffs(0, 98).addBox(-17.0F, -17.0F, 5.4F, 5, 7, 1),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, pi4, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "band4b",
                CubeListBuilder.create().texOffs(0, 118).addBox(-17.0F, -16.9F, -6.4F, 5, 7, 1),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -pi4, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "band5b",
                CubeListBuilder.create().texOffs(0, 108).addBox(-17.0F, -12.0F, -3.5F, 5, 1, 7),
                PartPose.ZERO);
        root.addOrReplaceChild(
                "band6b",
                CubeListBuilder.create().texOffs(0, 79).addBox(-17.0F, -16.5F, -21.4F, 5, 6, 1),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -pi4, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "band7b",
                CubeListBuilder.create().texOffs(0, 138).addBox(-17.0F, -17.0F, 20.4F, 5, 7, 1),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, pi4, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "band8b",
                CubeListBuilder.create().texOffs(0, 148).addBox(-17.0F, -27.0F, -3.5F, 5, 1, 7),
                PartPose.ZERO);

        root.addOrReplaceChild(
                "point1b",
                CubeListBuilder.create().texOffs(28, 130).addBox(-29.5F, -2.5F, -0.5F, 5, 5, 1),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, pi4));

        // ——— Left spikes ———
        root.addOrReplaceChild(
                "spike2b",
                CubeListBuilder.create().texOffs(49, 0).addBox(-15.0F, -29.0F, 0.0F, 1, 20, 1),
                PartPose.ZERO);
        root.addOrReplaceChild(
                "spike1b",
                CubeListBuilder.create().texOffs(67, 0).addBox(-15.0F, -20.0F, -10.0F, 1, 1, 20),
                PartPose.ZERO);
        root.addOrReplaceChild(
                "spike3b",
                CubeListBuilder.create().texOffs(55, 0).addBox(-15.0F, -23.5F, 13.0F, 1, 20, 1),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, pi4, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "spike4b",
                CubeListBuilder.create().texOffs(61, 0).addBox(14.0F, -23.5F, -14.0F, 1, 20, 1),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -pi4, 0.0F, 0.0F));

        // Gold tex size 128×256
        return LayerDefinition.create(mesh, 128, 256);
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
