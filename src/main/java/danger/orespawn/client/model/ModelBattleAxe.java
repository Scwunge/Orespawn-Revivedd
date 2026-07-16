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
 * 1.7.10 {@code ModelBattleAxe} — cube sizes/offsets/rotations 1:1 (tex 128×64).
 * Gold {@code RenderBattleAxe} applies scale <b>0.35</b> when equipped (do not bake scale into model).
 * Gold {@code render()} uses {@code f5 = 1.0F}.
 * <p>
 * Geometry: handle pivot (0,-12,0); grip down +Y; dual axe heads ±X around head.
 */
@OnlyIn(Dist.CLIENT)
public class ModelBattleAxe extends Model {
    private final ModelPart root;

    public ModelBattleAxe(ModelPart root) {
        super(RenderType::entityCutoutNoCull);
        this.root = root;
    }

    /**
     * Gold ModelBattleAxe constructor boxes (addBox / setRotationPoint / setRotation) — full 1:1.
     * Rotations: π/2 = 1.570796F, ±0.5061455F on blades.
     */
    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        final float halfPi = 1.570796F;
        final float bladeTilt = 0.5061455F;

        root.addOrReplaceChild(
                "handle1",
                CubeListBuilder.create().texOffs(0, 0).addBox(-7.0F, -0.5F, 0.0F, 31, 2, 1),
                PartPose.offsetAndRotation(0.0F, -12.0F, 0.0F, 0.0F, 0.0F, halfPi));
        root.addOrReplaceChild(
                "head1",
                CubeListBuilder.create().texOffs(29, 18).addBox(-2.0F, -4.5F, -0.5F, 3, 4, 2),
                PartPose.offset(0.0F, -12.0F, 0.0F));
        root.addOrReplaceChild(
                "grip",
                CubeListBuilder.create().texOffs(0, 7).addBox(-1.92F, 13.0F, -0.5F, 3, 11, 2),
                PartPose.offset(0.0F, -12.0F, 0.0F));
        root.addOrReplaceChild(
                "pin",
                CubeListBuilder.create().texOffs(38, 11).addBox(-1.0F, -3.0F, -1.0F, 1, 1, 3),
                PartPose.offset(0.0F, -12.0F, 0.0F));
        root.addOrReplaceChild(
                "top",
                CubeListBuilder.create().texOffs(24, 11).addBox(-2.0F, -8.0F, -0.5F, 3, 2, 2),
                PartPose.offset(0.0F, -12.0F, 0.0F));
        root.addOrReplaceChild(
                "blade1",
                CubeListBuilder.create().texOffs(70, 0).addBox(6.0F, -8.0F, 0.0F, 3, 10, 1),
                PartPose.offsetAndRotation(0.0F, -12.0F, 0.0F, 0.0F, 0.0F, bladeTilt));
        root.addOrReplaceChild(
                "blade2",
                CubeListBuilder.create().texOffs(70, 0).addBox(8.5F, -6.9F, 0.0F, 3, 10, 1),
                PartPose.offsetAndRotation(0.0F, -12.0F, 0.0F, 0.0F, 0.0F, -bladeTilt));
        root.addOrReplaceChild(
                "blade3",
                CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, -3.0F, 0.0F, 10, 1, 1),
                PartPose.offset(0.0F, -12.0F, 0.0F));
        root.addOrReplaceChild(
                "blade4",
                CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -2.0F, 0.0F, 7, 1, 1),
                PartPose.offsetAndRotation(0.0F, -12.0F, 0.0F, 0.0F, 0.0F, bladeTilt));
        root.addOrReplaceChild(
                "blade5",
                CubeListBuilder.create().texOffs(0, 0).addBox(0.5F, -3.5F, 0.0F, 8, 1, 1),
                PartPose.offsetAndRotation(0.0F, -12.0F, 0.0F, 0.0F, 0.0F, -bladeTilt));
        root.addOrReplaceChild(
                "blade6",
                CubeListBuilder.create().texOffs(70, 0).addBox(-12.2F, -5.2F, 0.0F, 3, 10, 1),
                PartPose.offsetAndRotation(0.0F, -13.0F, 0.0F, 0.0F, 0.0F, bladeTilt));
        root.addOrReplaceChild(
                "blade7",
                CubeListBuilder.create().texOffs(0, 0).addBox(-9.9F, -3.0F, 0.0F, 8, 1, 1),
                PartPose.offsetAndRotation(0.0F, -12.0F, 0.0F, 0.0F, 0.0F, bladeTilt));
        root.addOrReplaceChild(
                "blade8",
                CubeListBuilder.create().texOffs(0, 0).addBox(-10.0F, -3.0F, 0.0F, 10, 1, 1),
                PartPose.offset(0.0F, -12.0F, 0.0F));
        root.addOrReplaceChild(
                "blade9",
                CubeListBuilder.create().texOffs(70, 0).addBox(-10.0F, -8.5F, 0.0F, 3, 10, 1),
                PartPose.offsetAndRotation(0.0F, -12.0F, 0.0F, 0.0F, 0.0F, -bladeTilt));
        root.addOrReplaceChild(
                "blade10",
                CubeListBuilder.create().texOffs(0, 0).addBox(-7.0F, -2.5F, 0.0F, 7, 1, 1),
                PartPose.offsetAndRotation(0.0F, -12.0F, 0.0F, 0.0F, 0.0F, -bladeTilt));

        return LayerDefinition.create(mesh, 128, 64);
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
