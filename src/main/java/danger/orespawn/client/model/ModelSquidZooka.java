package danger.orespawn.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
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
 * 1.7.10 {@code ModelSquidZooka} — cube sizes/offsets/rotations 1:1 (tex 128×128).
 * Gold {@code RenderSquidZooka} applies scale <b>0.35</b> when equipped (do not bake scale into model).
 * Gold {@code render()} uses {@code f5 = 1.0F} and {@code glRotatef(180, 0, 0, 1)} before parts.
 * <p>
 * Geometry: barrel along Z (-19.+15); flared tail +Z; sights +X; handle +Y.
 */
@OnlyIn(Dist.CLIENT)
public class ModelSquidZooka extends Model {
    private final ModelPart root;

    public ModelSquidZooka(ModelPart root) {
        super(RenderType::entityCutoutNoCull);
        this.root = root;
    }

    /**
     * Gold ModelSquidZooka constructor boxes (addBox / setRotationPoint / setRotation) — full 1:1.
     * All parts pivot (0,0,0), zero rotation.
     */
    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        root.addOrReplaceChild(
                "barrel",
                CubeListBuilder.create().texOffs(29, 19).addBox(-1.0F, -1.0F, -19.0F, 2, 2, 34),
                PartPose.ZERO);
        root.addOrReplaceChild(
                "tail1",
                CubeListBuilder.create().texOffs(0, 53).addBox(-1.5F, -1.5F, 15.0F, 3, 3, 1),
                PartPose.ZERO);
        root.addOrReplaceChild(
                "tail2",
                CubeListBuilder.create().texOffs(0, 58).addBox(-2.0F, -2.0F, 16.0F, 4, 4, 1),
                PartPose.ZERO);
        root.addOrReplaceChild(
                "tail3",
                CubeListBuilder.create().texOffs(0, 64).addBox(-2.5F, -2.5F, 17.0F, 5, 5, 1),
                PartPose.ZERO);
        root.addOrReplaceChild(
                "tail4",
                CubeListBuilder.create().texOffs(0, 71).addBox(-3.0F, -3.0F, 18.0F, 6, 6, 6),
                PartPose.ZERO);
        root.addOrReplaceChild(
                "tail5",
                CubeListBuilder.create().texOffs(0, 84).addBox(-2.5F, -2.5F, 24.0F, 5, 5, 1),
                PartPose.ZERO);
        root.addOrReplaceChild(
                "tail6",
                CubeListBuilder.create().texOffs(0, 91).addBox(-2.0F, -2.0F, 25.0F, 4, 4, 1),
                PartPose.ZERO);
        root.addOrReplaceChild(
                "tail7",
                CubeListBuilder.create().texOffs(0, 97).addBox(-1.5F, -1.5F, 26.0F, 3, 3, 1),
                PartPose.ZERO);
        root.addOrReplaceChild(
                "sight3",
                CubeListBuilder.create().texOffs(25, 0).addBox(1.0F, -2.0F, -10.0F, 1, 1, 2),
                PartPose.ZERO);
        root.addOrReplaceChild(
                "sight2",
                CubeListBuilder.create().texOffs(32, 0).addBox(0.5F, -4.0F, -12.0F, 2, 2, 6),
                PartPose.ZERO);
        root.addOrReplaceChild(
                "sight1",
                CubeListBuilder.create().texOffs(18, 0).addBox(1.0F, -1.0F, -10.0F, 1, 1, 2),
                PartPose.ZERO);
        root.addOrReplaceChild(
                "handle1",
                CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, 1.0F, 0.0F, 1, 7, 1),
                PartPose.ZERO);

        // Gold tex size 128×128
        return LayerDefinition.create(mesh, 128, 128);
    }

    @Override
    public void renderToBuffer(
            PoseStack poseStack,
            VertexConsumer buffer,
            int packedLight,
            int packedOverlay,
            int color) {
        // gold render(): GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F) then all parts at f5=1.0
        poseStack.pushPose();
        poseStack.mulPose(Axis.ZP.rotationDegrees(180.0F));
        this.root.render(poseStack, buffer, packedLight, packedOverlay, color);
        poseStack.popPose();
    }
}
