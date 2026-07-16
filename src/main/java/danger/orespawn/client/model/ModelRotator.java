package danger.orespawn.client.model;

import danger.orespawn.entity.RenderInfo;
import danger.orespawn.entity.Rotator;
import danger.orespawn.util.Reference;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

/**
 * Port of gold {@code ModelRotator} (1.7.10 ModelBase, tex 64×32) → HierarchicalModel.
 * Gold draws Shape1/2/3 eight times at zRot = i·π/4 with group GL rotates on X/Y/Z from RenderInfo.rf1.
 * Cubes/UVs/pivots 1:1; eight instances per ring replace multi-draw.
 * Wingspeed default 0.25 matches ClientProxy {@code new ModelRotator(0.25F)}.
 */
@OnlyIn(Dist.CLIENT)
public class ModelRotator extends HierarchicalModel<Rotator> {
    /** Model layer location. */
    public static final ModelLayerLocation LAYER_LOCATION =
            new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "rotator"), "main");

    private final float wingspeed;
    private final ModelPart root;
    private final ModelPart group1;
    private final ModelPart group2;
    private final ModelPart group3;

    public ModelRotator(ModelPart root) {
        this(root, 0.25F);
    }

    public ModelRotator(ModelPart root, float wingspeed) {
        this.root = root;
        this.wingspeed = wingspeed;
        this.group1 = root.getChild("group1");
        this.group2 = root.getChild("group2");
        this.group3 = root.getChild("group3");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        PartDefinition group1 = root.addOrReplaceChild("group1", CubeListBuilder.create(), PartPose.ZERO);
        PartDefinition group2 = root.addOrReplaceChild("group2", CubeListBuilder.create(), PartPose.ZERO);
        PartDefinition group3 = root.addOrReplaceChild("group3", CubeListBuilder.create(), PartPose.ZERO);

        // gold Shape1: addBox(-2, 3.9, 0, 4, 1, 1) UV 0,12 offset(0,0,0) — 8 rings zRot = i*PI/4
        for (int i = 0; i < 8; i++) {
            float z = (float) (i * Math.PI / 4.0);
            group1.addOrReplaceChild(
                    "shape1_" + i,
                    CubeListBuilder.create().texOffs(0, 12).addBox(-2.0F, 3.9F, 0.0F, 4.0F, 1.0F, 1.0F),
                    PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, z));
        }

        // gold Shape2: addBox(-4, 7.6, 0, 8, 2, 2) UV 0,7 offset(0,0,-0.5)
        for (int i = 0; i < 8; i++) {
            float z = (float) (i * Math.PI / 4.0);
            group2.addOrReplaceChild(
                    "shape2_" + i,
                    CubeListBuilder.create().texOffs(0, 7).addBox(-4.0F, 7.6F, 0.0F, 8.0F, 2.0F, 2.0F),
                    PartPose.offsetAndRotation(0.0F, 0.0F, -0.5F, 0.0F, 0.0F, z));
        }

        // gold Shape3: addBox(-7, 13.7, 0, 14, 3, 3) UV 0,0 offset(0,0,-1)
        for (int i = 0; i < 8; i++) {
            float z = (float) (i * Math.PI / 4.0);
            group3.addOrReplaceChild(
                    "shape3_" + i,
                    CubeListBuilder.create().texOffs(0, 0).addBox(-7.0F, 13.7F, 0.0F, 14.0F, 3.0F, 3.0F),
                    PartPose.offsetAndRotation(0.0F, 0.0F, -1.0F, 0.0F, 0.0F, z));
        }

        return LayerDefinition.create(mesh, 64, 32);
    }

    @Override
    public ModelPart root() {
        return this.root;
    }

    @Override
    public void setupAnim(
            Rotator entity,
            float limbSwing,
            float limbSwingAmount,
            float ageInTicks,
            float netHeadYaw,
            float headPitch) {
        RenderInfo ri = entity.getRenderInfo();
        // gold: GL11.glRotatef(ri.rf1, axis) on each ring group
        float rad = (float) Math.toRadians(ri.rf1);
        this.group1.xRot = rad;
        this.group1.yRot = 0.0F;
        this.group1.zRot = 0.0F;
        this.group2.xRot = 0.0F;
        this.group2.yRot = rad;
        this.group2.zRot = 0.0F;
        this.group3.xRot = 0.0F;
        this.group3.yRot = 0.0F;
        this.group3.zRot = rad;

        // gold: ri.rf1 += 2 each frame; wrap 0.359
        float step = 2.0F * this.wingspeed / 0.25F;
        ri.rf1 += step;
        if (ri.rf1 > 359.0F) {
            ri.rf1 = 0.0F;
        }
        entity.setRenderInfo(ri);
    }
}
