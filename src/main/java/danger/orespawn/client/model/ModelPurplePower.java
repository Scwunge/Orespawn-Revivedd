package danger.orespawn.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import danger.orespawn.entity.PurplePower;
import danger.orespawn.util.Reference;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

/**
 * Port of gold {@code ModelPurplePower} (1.7.10 ModelBase, tex 64×32) → HierarchicalModel.
 * Three bar lengths drawn 6× each (π/3 zRot steps) on rotating rings — gold GL multi-draw.
 * Wingspeed default 1.0 matches ClientProxy {@code new ModelPurplePower(1.0F)}.
 * Gold Color4f(0.75,0.75,0.75,0.55) applied in renderToBuffer.
 */
@OnlyIn(Dist.CLIENT)
public class ModelPurplePower extends HierarchicalModel<PurplePower> {
    public static final ModelLayerLocation LAYER_LOCATION =
            new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "purple_power"), "main");

    /** Gold GL11.glColor4f(0.75, 0.75, 0.75, 0.55) as ARGB. */
    private static final int PURPLE_TINT = 0x8CBFBFBF;

    private final float wingspeed;
    private final ModelPart root;
    private final ModelPart ring1;
    private final ModelPart ring2;
    private final ModelPart ring3;

    public ModelPurplePower(ModelPart root) {
        this(root, 1.0F);
    }

    public ModelPurplePower(ModelPart root, float wingspeed) {
        super(RenderType::entityTranslucent);
        this.root = root;
        this.wingspeed = wingspeed;
        this.ring1 = root.getChild("ring1");
        this.ring2 = root.getChild("ring2");
        this.ring3 = root.getChild("ring3");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        // gold Shape1: addBox(-2, -0.5, -0.5, 4, 1, 1) UV 0,12 — 6 copies at zRot i*π/3
        PartDefinition ring1 = root.addOrReplaceChild("ring1", CubeListBuilder.create(), PartPose.ZERO);
        for (int i = 0; i < 6; i++) {
            float z = i * ((float) Math.PI / 3.0F);
            ring1.addOrReplaceChild(
                    "s1_" + i,
                    CubeListBuilder.create().texOffs(0, 12)
                            .addBox(-2.0F, -0.5F, -0.5F, 4.0F, 1.0F, 1.0F),
                    PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, z));
        }

        // gold Shape2: addBox(-4, -0.5, -0.5, 8, 1, 1) UV 0,7
        PartDefinition ring2 = root.addOrReplaceChild("ring2", CubeListBuilder.create(), PartPose.ZERO);
        for (int i = 0; i < 6; i++) {
            float z = i * ((float) Math.PI / 3.0F);
            ring2.addOrReplaceChild(
                    "s2_" + i,
                    CubeListBuilder.create().texOffs(0, 7)
                            .addBox(-4.0F, -0.5F, -0.5F, 8.0F, 1.0F, 1.0F),
                    PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, z));
        }

        // gold Shape3: addBox(-7, -0.5, -0.5, 14, 1, 1) UV 0,0
        PartDefinition ring3 = root.addOrReplaceChild("ring3", CubeListBuilder.create(), PartPose.ZERO);
        for (int i = 0; i < 6; i++) {
            float z = i * ((float) Math.PI / 3.0F);
            ring3.addOrReplaceChild(
                    "s3_" + i,
                    CubeListBuilder.create().texOffs(0, 0)
                            .addBox(-7.0F, -0.5F, -0.5F, 14.0F, 1.0F, 1.0F),
                    PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, z));
        }

        return LayerDefinition.create(mesh, 64, 32);
    }

    @Override
    public ModelPart root() {
        return this.root;
    }

    @Override
    public void setupAnim(
            PurplePower entity,
            float limbSwing,
            float limbSwingAmount,
            float ageInTicks,
            float netHeadYaw,
            float headPitch) {
        // gold: random rotate rings each frame; approximate with age + entity id hash
        int seed = entity.getId() * 31 + (int) ageInTicks;
        float r1 = ((seed * 1103515245 + 12345) & 0x7FFF) / 32767.0F * 360.0F * ((float) Math.PI / 180.0F);
        float r2 = (((seed + 7) * 1103515245 + 12345) & 0x7FFF) / 32767.0F * 360.0F * ((float) Math.PI / 180.0F);
        float r3 = (((seed + 13) * 1103515245 + 12345) & 0x7FFF) / 32767.0F * 360.0F * ((float) Math.PI / 180.0F);

        // gold: ring1 rotated about X, ring2 about Y, ring3 about Z
        this.ring1.xRot = r1 * this.wingspeed;
        this.ring1.yRot = 0.0F;
        this.ring1.zRot = 0.0F;

        this.ring2.xRot = r1 * this.wingspeed;
        this.ring2.yRot = r2 * this.wingspeed;
        this.ring2.zRot = 0.0F;

        this.ring3.xRot = r1 * this.wingspeed;
        this.ring3.yRot = r2 * this.wingspeed;
        this.ring3.zRot = r3 * this.wingspeed;
    }

    @Override
    public void renderToBuffer(
            PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, int color) {
        // gold translucent tint + fullbright-ish (OpenGlHelper 240,240 approximated by full light)
        super.renderToBuffer(poseStack, buffer, 0xF000F0, packedOverlay, PURPLE_TINT);
    }
}
