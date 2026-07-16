package danger.orespawn.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import danger.orespawn.entity.WormDoom;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

/**
 * Gold {@code ModelWormDoom} (CF 1.12) → HierarchicalModel.
 * Cubes/UVs 1:1 (tex 64×256). Gold draws head once + <b>100-pass body trail</b> in
 * {@code render()}; body is not drawn by default HierarchicalModel walk — use
 * {@link #renderHead} / {@link #renderBodySegment} from {@code WormDoomRenderer}.
 */
@OnlyIn(Dist.CLIENT)
public class ModelWormDoom extends HierarchicalModel<WormDoom> {
    public static final float WORM_SCALE = 2.0F;

    private final ModelPart root;
    private final ModelPart head1;
    private final ModelPart tooth1;
    private final ModelPart tooth2;
    private final ModelPart tooth3;
    private final ModelPart tooth4;
    private final ModelPart tooth5;
    private final ModelPart tooth6;
    private final ModelPart tooth7;
    private final ModelPart tooth8;
    private final ModelPart head2;
    private final ModelPart body1;
    private final ModelPart body2;

    public ModelWormDoom(ModelPart root) {
        this.root = root;
        this.head1 = root.getChild("head1");
        this.tooth1 = root.getChild("tooth1");
        this.tooth2 = root.getChild("tooth2");
        this.tooth3 = root.getChild("tooth3");
        this.tooth4 = root.getChild("tooth4");
        this.tooth5 = root.getChild("tooth5");
        this.tooth6 = root.getChild("tooth6");
        this.tooth7 = root.getChild("tooth7");
        this.tooth8 = root.getChild("tooth8");
        this.head2 = root.getChild("head2");
        this.body1 = root.getChild("body1");
        this.body2 = root.getChild("body2");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        root.addOrReplaceChild(
                "head1",
                CubeListBuilder.create().texOffs(0, 0).addBox(-8.0F, -8.0F, -6.0F, 16.0F, 16.0F, 12.0F),
                PartPose.offset(0.0F, 12.0F, 0.0F));
        root.addOrReplaceChild(
                "tooth1",
                CubeListBuilder.create().texOffs(0, 220).addBox(-0.5F, -0.5F, -7.0F, 1.0F, 1.0F, 7.0F),
                PartPose.offset(0.0F, 21.0F, -6.0F));
        root.addOrReplaceChild(
                "tooth2",
                CubeListBuilder.create().texOffs(0, 210).addBox(-0.5F, -0.5F, -7.0F, 1.0F, 1.0F, 7.0F),
                PartPose.offset(0.0F, 3.0F, -6.0F));
        root.addOrReplaceChild(
                "tooth3",
                CubeListBuilder.create().texOffs(0, 200).addBox(-0.5F, -0.5F, -7.0F, 1.0F, 1.0F, 7.0F),
                PartPose.offset(9.0F, 12.0F, -6.0F));
        root.addOrReplaceChild(
                "tooth4",
                CubeListBuilder.create().texOffs(0, 190).addBox(-0.5F, -0.5F, -7.0F, 1.0F, 1.0F, 7.0F),
                PartPose.offset(-9.0F, 12.0F, -6.0F));
        root.addOrReplaceChild(
                "tooth5",
                CubeListBuilder.create().texOffs(0, 180).addBox(-0.5F, -0.5F, -7.0F, 1.0F, 1.0F, 7.0F),
                PartPose.offset(-6.0F, 6.0F, -6.0F));
        root.addOrReplaceChild(
                "tooth6",
                CubeListBuilder.create().texOffs(0, 170).addBox(-0.5F, -0.5F, -7.0F, 1.0F, 1.0F, 7.0F),
                PartPose.offset(6.0F, 18.0F, -6.0F));
        root.addOrReplaceChild(
                "tooth7",
                CubeListBuilder.create().texOffs(0, 160).addBox(-0.5F, -0.5F, -7.0F, 1.0F, 1.0F, 7.0F),
                PartPose.offset(6.0F, 6.0F, -6.0F));
        root.addOrReplaceChild(
                "tooth8",
                CubeListBuilder.create().texOffs(0, 150).addBox(-0.5F, -0.5F, -7.0F, 1.0F, 1.0F, 7.0F),
                PartPose.offset(-6.0F, 18.0F, -6.0F));
        // gold head2 zRot = PI/4
        root.addOrReplaceChild(
                "head2",
                CubeListBuilder.create().texOffs(0, 31).addBox(-8.0F, -8.0F, -6.0F, 16.0F, 16.0F, 12.0F),
                PartPose.offsetAndRotation(0.0F, 12.0F, 0.0F, 0.0F, 0.0F, (float) (Math.PI / 4)));
        root.addOrReplaceChild(
                "body1",
                CubeListBuilder.create().texOffs(0, 82).addBox(-8.0F, -8.0F, -6.0F, 16.0F, 16.0F, 12.0F),
                PartPose.offset(0.0F, 12.0F, 4.0F));
        // gold body2 zRot = PI/4
        root.addOrReplaceChild(
                "body2",
                CubeListBuilder.create().texOffs(0, 114).addBox(-8.0F, -8.0F, -6.0F, 16.0F, 16.0F, 12.0F),
                PartPose.offsetAndRotation(0.0F, 12.0F, 4.0F, 0.0F, 0.0F, (float) (Math.PI / 4)));
        return LayerDefinition.create(mesh, 64, 256);
    }

    @Override
    public ModelPart root() {
        return this.root;
    }

    /**
     * Hide body from default HierarchicalModel walk — multi-pass trail is drawn by the renderer.
     * Head/teeth stay visible if super.render is used as fallback.
     */
    @Override
    public void renderToBuffer(
            PoseStack poseStack,
            VertexConsumer buffer,
            int packedLight,
            int packedOverlay,
            int color) {
        // no-op: WormDoomRenderer drives multi-pass exclusively
    }

    @Override
    public void setupAnim(
            WormDoom entity,
            float limbSwing,
            float limbSwingAmount,
            float ageInTicks,
            float netHeadYaw,
            float headPitch) {
        // gold tooth anim uses limbSwing as f (walk swing param from render)
        float newangle = (float) Math.cos(Math.toRadians(limbSwing * 5.7F)) * (float) Math.PI * 0.35F;

        // gold repositions teeth relative to head each frame (simplified to base offsets + bite)
        this.tooth1.x = 0.0F;
        this.tooth1.y = 21.0F;
        this.tooth1.z = -6.0F;
        this.tooth2.x = 0.0F;
        this.tooth2.y = 3.0F;
        this.tooth2.z = -6.0F;
        this.tooth3.x = 9.0F;
        this.tooth3.y = 12.0F;
        this.tooth3.z = -6.0F;
        this.tooth4.x = -9.0F;
        this.tooth4.y = 12.0F;
        this.tooth4.z = -6.0F;
        this.tooth5.x = -6.0F;
        this.tooth5.y = 6.0F;
        this.tooth5.z = -6.0F;
        this.tooth6.x = 6.0F;
        this.tooth6.y = 18.0F;
        this.tooth6.z = -6.0F;
        this.tooth7.x = 6.0F;
        this.tooth7.y = 6.0F;
        this.tooth7.z = -6.0F;
        this.tooth8.x = -6.0F;
        this.tooth8.y = 18.0F;
        this.tooth8.z = -6.0F;

        this.tooth1.xRot = this.head1.xRot + newangle;
        this.tooth2.xRot = this.head1.xRot - newangle;
        this.tooth3.yRot = this.head1.yRot + newangle;
        this.tooth4.yRot = this.head1.yRot - newangle;
        this.tooth5.xRot = this.head1.xRot + newangle;
        this.tooth7.xRot = this.head1.xRot + newangle;
        this.tooth6.xRot = this.head1.xRot - newangle;
        this.tooth8.xRot = this.head1.xRot - newangle;
        this.tooth6.yRot = this.head1.yRot + newangle;
        this.tooth7.yRot = this.head1.yRot + newangle;
        this.tooth5.yRot = this.head1.yRot - newangle;
        this.tooth8.yRot = this.head1.yRot - newangle;
    }

    /** Gold: head1 + head2 + 8 teeth at worm_scale with entity yaw/pitch applied by caller. */
    public void renderHead(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay) {
        this.head1.render(poseStack, buffer, packedLight, packedOverlay);
        this.head2.render(poseStack, buffer, packedLight, packedOverlay);
        this.tooth1.render(poseStack, buffer, packedLight, packedOverlay);
        this.tooth2.render(poseStack, buffer, packedLight, packedOverlay);
        this.tooth3.render(poseStack, buffer, packedLight, packedOverlay);
        this.tooth4.render(poseStack, buffer, packedLight, packedOverlay);
        this.tooth5.render(poseStack, buffer, packedLight, packedOverlay);
        this.tooth6.render(poseStack, buffer, packedLight, packedOverlay);
        this.tooth7.render(poseStack, buffer, packedLight, packedOverlay);
        this.tooth8.render(poseStack, buffer, packedLight, packedOverlay);
    }

    /** Gold body1 + body2 segment (diamond-ish pair via body2 zRot). */
    public void renderBodySegment(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay) {
        this.body1.render(poseStack, buffer, packedLight, packedOverlay);
        this.body2.render(poseStack, buffer, packedLight, packedOverlay);
    }
}
