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
 * 1.7.10 {@code ModelChainsaw} — cube sizes/offsets/rotations 1:1 (tex 64×64).
 * Gold {@code RenderChainsaw} applies scale <b>0.25</b> when equipped (do not bake scale into model).
 * Gold {@code render()} uses {@code f5 = 1.0F} and animates chain teeth + tip blade rotation.
 * <p>
 * Geometry: engine at origin; bar extends -Z; teeth shuttle along bar; blade2 spins at tip.
 */
@OnlyIn(Dist.CLIENT)
public class ModelChainsaw extends Model {
    private final ModelPart root;
    private final ModelPart blade2;
    private final ModelPart tooth;

    // Gold tooth shuttle state (single ModelRenderer drawn 6 times with different pos)
    private float toothpos = 0.0F;
    private int toothdir = 0;
    private float toothpos1 = 7.0F;
    private int toothdir1 = 0;
    private float toothpos2 = 14.0F;
    private int toothdir2 = 0;
    private float toothpos3 = 20.0F;
    private int toothdir3 = 1;
    private float toothpos4 = 13.0F;
    private int toothdir4 = 1;
    private float toothpos5 = 6.0F;
    private int toothdir5 = 1;

    public ModelChainsaw(ModelPart root) {
        super(RenderType::entityCutoutNoCull);
        this.root = root;
        this.blade2 = root.getChild("blade2");
        this.tooth = root.getChild("tooth");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        root.addOrReplaceChild(
                "engine",
                CubeListBuilder.create().texOffs(0, 19).addBox(-2.0F, -4.0F, -4.0F, 4, 7, 8),
                PartPose.ZERO);
        root.addOrReplaceChild(
                "handle1",
                CubeListBuilder.create().texOffs(49, 0).addBox(0.0F, -3.0F, 3.0F, 1, 1, 5),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.1919862F, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "handle2",
                CubeListBuilder.create().texOffs(50, 13).addBox(0.0F, 2.0F, 4.0F, 1, 1, 4),
                PartPose.ZERO);
        root.addOrReplaceChild(
                "handle3",
                CubeListBuilder.create().texOffs(52, 7).addBox(0.0F, -2.0F, 7.0F, 1, 4, 1),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.0872665F, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "muffler",
                CubeListBuilder.create().texOffs(14, 0).addBox(-3.0F, 0.0F, 1.0F, 1, 3, 3),
                PartPose.ZERO);
        root.addOrReplaceChild(
                "blade1",
                CubeListBuilder.create().texOffs(0, 35).addBox(0.0F, -2.0F, -28.0F, 1, 4, 24),
                PartPose.ZERO);
        root.addOrReplaceChild(
                "blade2",
                CubeListBuilder.create().texOffs(0, 8).addBox(0.0F, -2.5F, -2.5F, 1, 5, 5),
                PartPose.offset(0.0F, 0.0F, -28.0F));
        root.addOrReplaceChild(
                "tooth",
                CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, -1.0F, -0.5F, 1, 1, 1),
                PartPose.offset(0.0F, -2.0F, -5.0F));

        return LayerDefinition.create(mesh, 64, 64);
    }

    @Override
    public void renderToBuffer(
            PoseStack poseStack,
            VertexConsumer buffer,
            int packedLight,
            int packedOverlay,
            int color) {
        // Gold order: teeth → spin blade2 → body parts.
        // Draw teeth first (must be visible), then hide tooth so root pass doesn't double-draw.
        this.tooth.visible = true;
        renderTooth(poseStack, buffer, packedLight, packedOverlay, color, 0);
        renderTooth(poseStack, buffer, packedLight, packedOverlay, color, 1);
        renderTooth(poseStack, buffer, packedLight, packedOverlay, color, 2);
        renderTooth(poseStack, buffer, packedLight, packedOverlay, color, 3);
        renderTooth(poseStack, buffer, packedLight, packedOverlay, color, 4);
        renderTooth(poseStack, buffer, packedLight, packedOverlay, color, 5);

        // gold: blade2.rotateAngleX += 0.104719755. wrap 2π (before body render)
        this.blade2.xRot += 0.10471975511965977F;
        if (this.blade2.xRot > (float) (Math.PI * 2)) {
            this.blade2.xRot = 0.0F;
        }

        this.tooth.visible = false;
        this.root.render(poseStack, buffer, packedLight, packedOverlay, color);
        this.tooth.visible = true;
    }

    private void renderTooth(
            PoseStack poseStack,
            VertexConsumer buffer,
            int packedLight,
            int packedOverlay,
            int color,
            int idx) {
        float pos;
        int dir;
        switch (idx) {
            case 0 -> {
                pos = this.toothpos;
                dir = this.toothdir;
            }
            case 1 -> {
                pos = this.toothpos1;
                dir = this.toothdir1;
            }
            case 2 -> {
                pos = this.toothpos2;
                dir = this.toothdir2;
            }
            case 3 -> {
                pos = this.toothpos3;
                dir = this.toothdir3;
            }
            case 4 -> {
                pos = this.toothpos4;
                dir = this.toothdir4;
            }
            default -> {
                pos = this.toothpos5;
                dir = this.toothdir5;
            }
        }

        if (dir == 0) {
            this.tooth.y = -2.0F;
            this.tooth.z = -5.0F - pos;
            pos += 0.5F;
            if (pos > 21.0F) {
                pos = 21.0F;
                dir = 1;
            }
        } else {
            this.tooth.y = 3.0F;
            this.tooth.z = -5.0F - pos;
            pos -= 0.5F;
            if (pos < 0.0F) {
                pos = 0.0F;
                dir = 0;
            }
        }

        switch (idx) {
            case 0 -> {
                this.toothpos = pos;
                this.toothdir = dir;
            }
            case 1 -> {
                this.toothpos1 = pos;
                this.toothdir1 = dir;
            }
            case 2 -> {
                this.toothpos2 = pos;
                this.toothdir2 = dir;
            }
            case 3 -> {
                this.toothpos3 = pos;
                this.toothdir3 = dir;
            }
            case 4 -> {
                this.toothpos4 = pos;
                this.toothdir4 = dir;
            }
            default -> {
                this.toothpos5 = pos;
                this.toothdir5 = dir;
            }
        }

        this.tooth.render(poseStack, buffer, packedLight, packedOverlay, color);
    }
}
