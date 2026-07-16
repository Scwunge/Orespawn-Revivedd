package danger.orespawn.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import danger.orespawn.entity.RockBase;
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
 * Port of gold {@code ModelRockBase} (1.7.10 ModelBase, tex 64×64) → HierarchicalModel.
 * Cubes/UVs/pivots 1:1. Parts selected by rock_type in setupAnim (visibility).
 * Crystal types (9–12) use translucent tint + fullbright like gold.
 * Wingspeed default 1.0 matches ClientProxy {@code new ModelRockBase(1.0F)}.
 */
@OnlyIn(Dist.CLIENT)
public class ModelRockBase extends HierarchicalModel<RockBase> {
    public static final ModelLayerLocation LAYER_LOCATION =
            new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "rock_base"), "main");

    /** Gold GL11.glColor4f(0.75, 0.75, 0.75, 0.55) as ARGB for crystals. */
    private static final int CRYSTAL_TINT = 0x8CBFBFBF;

    private final float wingspeed;
    private final ModelPart root;

    private final ModelPart rockShape1;
    private final ModelPart rockShape2;
    private final ModelPart rockShape3;
    private final ModelPart rockSmallShape1;
    private final ModelPart rockSmallShape2;
    private final ModelPart rockTntShape1;
    private final ModelPart rockTntShape2;
    private final ModelPart rockTntShape3;
    private final ModelPart rockTntShape4;
    private final ModelPart rockSpikeyShape1;
    private final ModelPart rockSpikeyShape2;
    private final ModelPart rockSpikeyShape3;
    private final ModelPart crystalShape1;
    private final ModelPart crystalShape2;
    private final ModelPart crystalShape3a;
    private final ModelPart crystalShape3b;
    private final ModelPart crystalShape3c;
    private final ModelPart crystalShape3d;
    private final ModelPart crystalShape4a;
    private final ModelPart crystalShape4b;
    private final ModelPart crystalShape4c;
    private final ModelPart crystalShape4d;

    private boolean crystalMode;

    public ModelRockBase(ModelPart root) {
        this(root, 1.0F);
    }

    public ModelRockBase(ModelPart root, float wingspeed) {
        super(RenderType::entityTranslucent);
        this.root = root;
        this.wingspeed = wingspeed;
        this.rockShape1 = root.getChild("rockShape1");
        this.rockShape2 = root.getChild("rockShape2");
        this.rockShape3 = root.getChild("rockShape3");
        this.rockSmallShape1 = root.getChild("rockSmallShape1");
        this.rockSmallShape2 = root.getChild("rockSmallShape2");
        this.rockTntShape1 = root.getChild("rockTntShape1");
        this.rockTntShape2 = root.getChild("rockTntShape2");
        this.rockTntShape3 = root.getChild("rockTntShape3");
        this.rockTntShape4 = root.getChild("rockTntShape4");
        this.rockSpikeyShape1 = root.getChild("rockSpikeyShape1");
        this.rockSpikeyShape2 = root.getChild("rockSpikeyShape2");
        this.rockSpikeyShape3 = root.getChild("rockSpikeyShape3");
        this.crystalShape1 = root.getChild("crystalShape1");
        this.crystalShape2 = root.getChild("crystalShape2");
        this.crystalShape3a = root.getChild("crystalShape3a");
        this.crystalShape3b = root.getChild("crystalShape3b");
        this.crystalShape3c = root.getChild("crystalShape3c");
        this.crystalShape3d = root.getChild("crystalShape3d");
        this.crystalShape4a = root.getChild("crystalShape4a");
        this.crystalShape4b = root.getChild("crystalShape4b");
        this.crystalShape4c = root.getChild("crystalShape4c");
        this.crystalShape4d = root.getChild("crystalShape4d");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        // gold RockShape1: box(-3,0,-1, 6,1,2) UV 0,0 offset(0,23,0)
        root.addOrReplaceChild(
                "rockShape1",
                CubeListBuilder.create().texOffs(0, 0).addBox(-3.0F, 0.0F, -1.0F, 6.0F, 1.0F, 2.0F),
                PartPose.offset(0.0F, 23.0F, 0.0F));
        // gold RockShape2: box(-3,0,1, 3,1,1) UV 0,4
        root.addOrReplaceChild(
                "rockShape2",
                CubeListBuilder.create().texOffs(0, 4).addBox(-3.0F, 0.0F, 1.0F, 3.0F, 1.0F, 1.0F),
                PartPose.offset(0.0F, 23.0F, 0.0F));
        // gold RockShape3: box(0,0,-2, 2,1,1) UV 0,7
        root.addOrReplaceChild(
                "rockShape3",
                CubeListBuilder.create().texOffs(0, 7).addBox(0.0F, 0.0F, -2.0F, 2.0F, 1.0F, 1.0F),
                PartPose.offset(0.0F, 23.0F, 0.0F));

        // gold RockSmallShape2: box(-2,0,0, 3,1,1) UV 0,4
        root.addOrReplaceChild(
                "rockSmallShape2",
                CubeListBuilder.create().texOffs(0, 4).addBox(-2.0F, 0.0F, 0.0F, 3.0F, 1.0F, 1.0F),
                PartPose.offset(0.0F, 23.0F, 0.0F));
        // gold RockSmallShape1: box(0,0,-1, 2,1,1) UV 0,7
        root.addOrReplaceChild(
                "rockSmallShape1",
                CubeListBuilder.create().texOffs(0, 7).addBox(0.0F, 0.0F, -1.0F, 2.0F, 1.0F, 1.0F),
                PartPose.offset(0.0F, 23.0F, 0.0F));

        // gold TNT shapes
        root.addOrReplaceChild(
                "rockTntShape1",
                CubeListBuilder.create().texOffs(0, 0).addBox(-3.0F, 0.0F, -1.0F, 6.0F, 1.0F, 2.0F),
                PartPose.offset(0.0F, 23.0F, 0.0F));
        root.addOrReplaceChild(
                "rockTntShape2",
                CubeListBuilder.create().texOffs(0, 4).addBox(-3.0F, 0.0F, 1.0F, 3.0F, 1.0F, 1.0F),
                PartPose.offset(0.0F, 23.0F, 0.0F));
        root.addOrReplaceChild(
                "rockTntShape3",
                CubeListBuilder.create().texOffs(0, 7).addBox(0.0F, 0.0F, -2.0F, 2.0F, 1.0F, 1.0F),
                PartPose.offset(0.0F, 23.0F, 0.0F));
        root.addOrReplaceChild(
                "rockTntShape4",
                CubeListBuilder.create().texOffs(0, 10).addBox(-4.0F, 0.0F, -2.0F, 3.0F, 1.0F, 3.0F),
                PartPose.offset(0.0F, 22.0F, 0.0F));

        // gold Spikey: Shape2/3 yRot 1.570796
        root.addOrReplaceChild(
                "rockSpikeyShape1",
                CubeListBuilder.create().texOffs(0, 0).addBox(-3.0F, 0.0F, -1.0F, 6.0F, 1.0F, 2.0F),
                PartPose.offset(0.0F, 23.0F, 0.0F));
        root.addOrReplaceChild(
                "rockSpikeyShape2",
                CubeListBuilder.create().texOffs(0, 4).addBox(-4.0F, 0.0F, -1.0F, 3.0F, 1.0F, 1.0F),
                PartPose.offsetAndRotation(0.0F, 23.0F, 0.0F, 0.0F, 1.570796F, 0.0F));
        root.addOrReplaceChild(
                "rockSpikeyShape3",
                CubeListBuilder.create().texOffs(0, 7).addBox(1.0F, 0.0F, 1.0F, 2.0F, 1.0F, 1.0F),
                PartPose.offsetAndRotation(0.0F, 23.0F, 0.0F, 0.0F, 1.570796F, 0.0F));

        // gold Crystal shapes
        root.addOrReplaceChild(
                "crystalShape1",
                CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -4.0F, -1.0F, 2.0F, 5.0F, 2.0F),
                PartPose.offset(0.0F, 23.0F, 0.0F));
        root.addOrReplaceChild(
                "crystalShape2",
                CubeListBuilder.create().texOffs(10, 0).addBox(-0.5F, -7.0F, -0.5F, 1.0F, 3.0F, 1.0F),
                PartPose.offset(0.0F, 23.0F, 0.0F));
        root.addOrReplaceChild(
                "crystalShape3a",
                CubeListBuilder.create().texOffs(0, 8).addBox(-1.0F, -5.0F, -1.0F, 1.0F, 5.0F, 1.0F),
                PartPose.offsetAndRotation(0.0F, 23.0F, 0.0F, 0.5410521F, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "crystalShape3b",
                CubeListBuilder.create().texOffs(0, 8).addBox(0.0F, -5.0F, 0.0F, 1.0F, 5.0F, 1.0F),
                PartPose.offsetAndRotation(0.0F, 23.0F, 0.0F, -0.5410521F, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "crystalShape3c",
                CubeListBuilder.create().texOffs(0, 8).addBox(0.0F, -5.0F, -1.0F, 1.0F, 5.0F, 1.0F),
                PartPose.offsetAndRotation(0.0F, 23.0F, 0.0F, 0.0F, 0.0F, 0.5410521F));
        root.addOrReplaceChild(
                "crystalShape3d",
                CubeListBuilder.create().texOffs(0, 8).addBox(-1.0F, -5.0F, 0.0F, 1.0F, 5.0F, 1.0F),
                PartPose.offsetAndRotation(0.0F, 23.0F, 0.0F, 0.0F, 0.0F, -0.5410521F));
        root.addOrReplaceChild(
                "crystalShape4a",
                CubeListBuilder.create().texOffs(0, 16).addBox(0.0F, -3.0F, -1.0F, 1.0F, 3.0F, 1.0F),
                PartPose.offsetAndRotation(0.0F, 23.0F, 0.0F, 1.308997F, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "crystalShape4b",
                CubeListBuilder.create().texOffs(0, 16).addBox(-1.0F, -3.0F, 0.0F, 1.0F, 3.0F, 1.0F),
                PartPose.offsetAndRotation(0.0F, 23.0F, 0.0F, -1.308997F, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "crystalShape4c",
                CubeListBuilder.create().texOffs(0, 16).addBox(0.0F, -3.0F, 0.0F, 1.0F, 3.0F, 1.0F),
                PartPose.offsetAndRotation(0.0F, 23.0F, 0.0F, 0.0F, 0.0F, 1.308997F));
        root.addOrReplaceChild(
                "crystalShape4d",
                CubeListBuilder.create().texOffs(0, 16).addBox(-1.0F, -3.0F, -1.0F, 1.0F, 3.0F, 1.0F),
                PartPose.offsetAndRotation(0.0F, 23.0F, 0.0F, 0.0F, 0.0F, -1.308997F));

        return LayerDefinition.create(mesh, 64, 64);
    }

    @Override
    public ModelPart root() {
        return this.root;
    }

    @Override
    public void setupAnim(
            RockBase entity,
            float limbSwing,
            float limbSwingAmount,
            float ageInTicks,
            float netHeadYaw,
            float headPitch) {
        // gold setRotationAngles empty — visibility by rock_type only
        int rt = entity.getRockType();
        hideAll();
        this.crystalMode = false;
        if (rt < 1 || rt > 12) {
            return;
        }
        if (rt == 1) {
            this.rockSmallShape1.visible = true;
            this.rockSmallShape2.visible = true;
        } else if (rt == 7) {
            this.rockSpikeyShape1.visible = true;
            this.rockSpikeyShape2.visible = true;
            this.rockSpikeyShape3.visible = true;
        } else if (rt == 8) {
            this.rockTntShape1.visible = true;
            this.rockTntShape2.visible = true;
            this.rockTntShape3.visible = true;
            this.rockTntShape4.visible = true;
        } else if (rt >= 9 && rt <= 12) {
            this.crystalMode = true;
            this.crystalShape1.visible = true;
            this.crystalShape2.visible = true;
            this.crystalShape3a.visible = true;
            this.crystalShape3b.visible = true;
            this.crystalShape3c.visible = true;
            this.crystalShape3d.visible = true;
            this.crystalShape4a.visible = true;
            this.crystalShape4b.visible = true;
            this.crystalShape4c.visible = true;
            this.crystalShape4d.visible = true;
        } else {
            // types 2–6 default rock
            this.rockShape1.visible = true;
            this.rockShape2.visible = true;
            this.rockShape3.visible = true;
        }
    }

    private void hideAll() {
        this.rockShape1.visible = false;
        this.rockShape2.visible = false;
        this.rockShape3.visible = false;
        this.rockSmallShape1.visible = false;
        this.rockSmallShape2.visible = false;
        this.rockTntShape1.visible = false;
        this.rockTntShape2.visible = false;
        this.rockTntShape3.visible = false;
        this.rockTntShape4.visible = false;
        this.rockSpikeyShape1.visible = false;
        this.rockSpikeyShape2.visible = false;
        this.rockSpikeyShape3.visible = false;
        this.crystalShape1.visible = false;
        this.crystalShape2.visible = false;
        this.crystalShape3a.visible = false;
        this.crystalShape3b.visible = false;
        this.crystalShape3c.visible = false;
        this.crystalShape3d.visible = false;
        this.crystalShape4a.visible = false;
        this.crystalShape4b.visible = false;
        this.crystalShape4c.visible = false;
        this.crystalShape4d.visible = false;
    }

    @Override
    public void renderToBuffer(
            PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, int color) {
        if (this.crystalMode) {
            // gold translucent crystal + OpenGlHelper fullbright
            super.renderToBuffer(poseStack, buffer, 0xF000F0, packedOverlay, CRYSTAL_TINT);
        } else {
            super.renderToBuffer(poseStack, buffer, packedLight, packedOverlay, color);
        }
    }
}
