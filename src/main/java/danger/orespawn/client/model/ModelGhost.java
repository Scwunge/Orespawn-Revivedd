package danger.orespawn.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import danger.orespawn.entity.Ghost;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.util.Mth;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

/**
 * Port of gold {@code ModelGhost} (1.7.10 ModelBase, tex 64×64) → HierarchicalModel.
 * Cubes/UVs/pivots 1:1. Gold render tint Color4f(0.75,0.75,0.75,0.25) applied in renderToBuffer.
 */
@OnlyIn(Dist.CLIENT)
public class ModelGhost extends HierarchicalModel<Ghost> {
    /** Gold GL11.glColor4f(0.75, 0.75, 0.75, 0.25) as ARGB. */
    private static final int GHOST_TINT = 0x40BFBFBF;

    private final ModelPart root;
    private final ModelPart HeadAndBody;
    private final ModelPart LArm;
    private final ModelPart RArm;

    public ModelGhost(ModelPart root) {
        super(RenderType::entityTranslucent);
        this.root = root;
        this.HeadAndBody = root.getChild("HeadAndBody");
        this.LArm = root.getChild("LArm");
        this.RArm = root.getChild("RArm");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        // gold: HeadAndBody box(-3,0,-3, 6,21,6) UV 0,0 offset(0,0,0)
        root.addOrReplaceChild(
                "HeadAndBody",
                CubeListBuilder.create().texOffs(0, 0).addBox(-3.0F, 0.0F, -3.0F, 6.0F, 21.0F, 6.0F),
                PartPose.offset(0.0F, 0.0F, 0.0F));
        // gold: LArm box(-1,-1,-1, 2,11,2) UV 34,0 offset(3,6,0) zRot -0.3316126
        root.addOrReplaceChild(
                "LArm",
                CubeListBuilder.create().texOffs(34, 0).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 11.0F, 2.0F),
                PartPose.offsetAndRotation(3.0F, 6.0F, 0.0F, 0.0F, 0.0F, -0.3316126F));
        // gold: RArm box(-1,-1,-1, 2,11,2) UV 25,0 offset(-3,6,0) zRot 0.3316126
        root.addOrReplaceChild(
                "RArm",
                CubeListBuilder.create().texOffs(25, 0).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 11.0F, 2.0F),
                PartPose.offsetAndRotation(-3.0F, 6.0F, 0.0F, 0.0F, 0.0F, 0.3316126F));

        return LayerDefinition.create(mesh, 64, 64);
    }

    @Override
    public ModelPart root() {
        return this.root;
    }

    @Override
    public void setupAnim(
            Ghost entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        // gold ModelGhost.render arm sway (f2 = ageInTicks)
        this.LArm.zRot = -0.33F + Mth.cos(ageInTicks * 0.3F) * (float) Math.PI * 0.05F;
        this.RArm.zRot = 0.33F + Mth.cos(ageInTicks * 0.32F) * (float) Math.PI * 0.05F;
        this.LArm.xRot = -0.33F + Mth.cos(ageInTicks * 0.34F) * (float) Math.PI * 0.05F;
        this.RArm.xRot = 0.33F + Mth.cos(ageInTicks * 0.36F) * (float) Math.PI * 0.05F;
    }

    @Override
    public void renderToBuffer(
            PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, int color) {
        // gold translucent ghost tint (ignore incoming color for 1:1 alpha)
        super.renderToBuffer(poseStack, buffer, packedLight, packedOverlay, GHOST_TINT);
    }
}
