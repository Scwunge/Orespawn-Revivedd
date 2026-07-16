package danger.orespawn.client.model;

import danger.orespawn.entity.Elevator;
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
 * Port of gold {@code ModelElevator} (1.7.10 ModelBase, tex 64×64) → HierarchicalModel.
 * Five platform cubes 1:1 (Shape1–5). Gold setRotationAngles empty.
 * ClientProxy: {@code new ModelElevator()} (no wingspeed).
 */
@OnlyIn(Dist.CLIENT)
public class ModelElevator extends HierarchicalModel<Elevator> {
    /** Model layer location. */
    public static final ModelLayerLocation LAYER_LOCATION =
            new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "elevator"), "main");

    private final ModelPart root;
    private final ModelPart shape1;
    private final ModelPart shape2;
    private final ModelPart shape3;
    private final ModelPart shape4;
    private final ModelPart shape5;

    public ModelElevator(ModelPart root) {
        this.root = root;
        this.shape1 = root.getChild("shape1");
        this.shape2 = root.getChild("shape2");
        this.shape3 = root.getChild("shape3");
        this.shape4 = root.getChild("shape4");
        this.shape5 = root.getChild("shape5");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        // gold Shape2: addBox(-3,0,-9, 6,1,1) UV 0,18
        root.addOrReplaceChild(
                "shape2",
                CubeListBuilder.create().texOffs(0, 18).addBox(-3.0F, 0.0F, -9.0F, 6.0F, 1.0F, 1.0F),
                PartPose.offset(0.0F, 0.0F, 0.0F));

        // gold Shape3: addBox(-1,0,-10, 2,1,1) UV 0,21
        root.addOrReplaceChild(
                "shape3",
                CubeListBuilder.create().texOffs(0, 21).addBox(-1.0F, 0.0F, -10.0F, 2.0F, 1.0F, 1.0F),
                PartPose.offset(0.0F, 0.0F, 0.0F));

        // gold Shape4: addBox(-3,0,8, 6,1,1) UV 17,18
        root.addOrReplaceChild(
                "shape4",
                CubeListBuilder.create().texOffs(17, 18).addBox(-3.0F, 0.0F, 8.0F, 6.0F, 1.0F, 1.0F),
                PartPose.offset(0.0F, 0.0F, 0.0F));

        // gold Shape5: addBox(-1,0,9, 2,1,1) UV 17,21
        root.addOrReplaceChild(
                "shape5",
                CubeListBuilder.create().texOffs(17, 21).addBox(-1.0F, 0.0F, 9.0F, 2.0F, 1.0F, 1.0F),
                PartPose.offset(0.0F, 0.0F, 0.0F));

        // gold Shape1: addBox(-4,0,-8, 8,1,16) UV 0,0
        root.addOrReplaceChild(
                "shape1",
                CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, 0.0F, -8.0F, 8.0F, 1.0F, 16.0F),
                PartPose.offset(0.0F, 0.0F, 0.0F));

        return LayerDefinition.create(mesh, 64, 64);
    }

    @Override
    public ModelPart root() {
        return this.root;
    }

    @Override
    public void setupAnim(
            Elevator entity,
            float limbSwing,
            float limbSwingAmount,
            float ageInTicks,
            float netHeadYaw,
            float headPitch) {
        // gold setRotationAngles empty — static platform
        this.shape1.xRot = 0.0F;
        this.shape1.yRot = 0.0F;
        this.shape1.zRot = 0.0F;
        this.shape2.xRot = 0.0F;
        this.shape2.yRot = 0.0F;
        this.shape2.zRot = 0.0F;
        this.shape3.xRot = 0.0F;
        this.shape3.yRot = 0.0F;
        this.shape3.zRot = 0.0F;
        this.shape4.xRot = 0.0F;
        this.shape4.yRot = 0.0F;
        this.shape4.zRot = 0.0F;
        this.shape5.xRot = 0.0F;
        this.shape5.yRot = 0.0F;
        this.shape5.zRot = 0.0F;
    }
}
