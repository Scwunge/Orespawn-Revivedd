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
 * 1.7.10 {@code ModelQueenBattleAxe} — cube sizes/offsets/rotations 1:1 (tex 128×64).
 * Gold {@code RenderQueenBattleAxe} applies scale <b>0.35</b> when equipped (do not bake scale into model).
 * Gold {@code render()} uses {@code f5 = 1.0F}.
 * <p>
 * Geometry: handle pivot (−0.5,−12,0) zRot π/2; four fan blades around (−0.5,−14.5,0);
 * top cap at (−1.5,−21,−0.5). Distinct from {@link ModelBattleAxe} (simpler head/blades).
 */
@OnlyIn(Dist.CLIENT)
public class ModelQueenBattleAxe extends Model {
    private final ModelPart root;

    public ModelQueenBattleAxe(ModelPart root) {
        super(RenderType::entityCutoutNoCull);
        this.root = root;
    }

    /**
     * Gold ModelQueenBattleAxe constructor boxes (addBox / setRotationPoint / setRotation) — full 1:1.
     * Rotations: π/2 = 1.570796F; blade zRots −0.5934119 / −0.1919862 / 0.2094395 / 0.5934119.
     */
    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        final float halfPi = 1.570796F;

        root.addOrReplaceChild(
                "handle1",
                CubeListBuilder.create().texOffs(0, 0).addBox(-7.0F, -0.5F, 0.0F, 31, 1, 1),
                PartPose.offsetAndRotation(-0.5F, -12.0F, 0.0F, 0.0F, 0.0F, halfPi));
        root.addOrReplaceChild(
                "head1",
                CubeListBuilder.create().texOffs(29, 18).addBox(-2.0F, -4.5F, -0.5F, 3, 4, 2),
                PartPose.offset(0.0F, -12.0F, 0.0F));
        root.addOrReplaceChild(
                "grip",
                CubeListBuilder.create().texOffs(0, 7).addBox(-1.92F, 13.0F, -0.5F, 2, 11, 2),
                PartPose.offset(0.5F, -12.0F, 0.0F));
        root.addOrReplaceChild(
                "pin",
                CubeListBuilder.create().texOffs(38, 11).addBox(-1.0F, -3.0F, -1.0F, 1, 1, 3),
                PartPose.offset(0.0F, -12.0F, 0.0F));
        root.addOrReplaceChild(
                "blade1",
                CubeListBuilder.create().texOffs(70, 0).addBox(-10.0F, -2.0F, 0.0F, 20, 4, 1),
                PartPose.offsetAndRotation(-0.5F, -14.5F, 0.0F, 0.0F, 0.0F, -0.5934119F));
        root.addOrReplaceChild(
                "blade2",
                CubeListBuilder.create().texOffs(70, 0).addBox(-10.0F, -2.0F, 0.0F, 20, 4, 1),
                PartPose.offsetAndRotation(-0.5F, -14.5F, 0.0F, 0.0F, 0.0F, -0.1919862F));
        root.addOrReplaceChild(
                "blade3",
                CubeListBuilder.create().texOffs(70, 0).addBox(-10.0F, -2.0F, 0.0F, 20, 4, 1),
                PartPose.offsetAndRotation(-0.5F, -14.5F, 0.0F, 0.0F, 0.0F, 0.2094395F));
        root.addOrReplaceChild(
                "blade4",
                CubeListBuilder.create().texOffs(70, 0).addBox(-10.0F, -2.0F, 0.0F, 20, 4, 1),
                PartPose.offsetAndRotation(-0.5F, -14.5F, 0.0F, 0.0F, 0.0F, 0.5934119F));
        root.addOrReplaceChild(
                "top",
                CubeListBuilder.create().texOffs(13, 4).addBox(0.0F, 0.0F, 0.0F, 2, 2, 2),
                PartPose.offset(-1.5F, -21.0F, -0.5F));

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
