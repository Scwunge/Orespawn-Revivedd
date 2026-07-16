package danger.orespawn.client.model;

import danger.orespawn.entity.Robot1;
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
import net.minecraft.util.Mth;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

/**
 * Port of gold {@code ModelRobot1} (1.7.10 ModelBase, tex 64×32) → 1.21 HierarchicalModel.
 * Cubes/UVs/offsets 1:1. ClientProxy wingspeed {@code 2.0F}.
 * Full gold anim: foot walk + continuous key wind-up (zRot).
 */
@OnlyIn(Dist.CLIENT)
public class ModelRobot1 extends HierarchicalModel<Robot1> {
    public static final ModelLayerLocation LAYER_LOCATION =
            new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "robot1"), "main");

    private final float wingspeed;
    private final ModelPart root;
    private final ModelPart Shape1;
    private final ModelPart Shape2;
    private final ModelPart Shape2a;
    private final ModelPart Shape3;
    private final ModelPart Shape4;
    private final ModelPart Shape5;
    private final ModelPart Shape6;
    private final ModelPart Shape7;
    private final ModelPart Shape8;
    private final ModelPart Shape9;
    private final ModelPart Shape10;
    private final ModelPart Shape11;
    private final ModelPart Shape12;
    private final ModelPart Shape13;
    private final ModelPart Shape14;
    private final ModelPart Shape15;
    private final ModelPart Shape15a;
    private final ModelPart Shape16;
    private final ModelPart Shape17;
    private final ModelPart Shape18;
    private final ModelPart rfoot;
    private final ModelPart lfoot;
    private final ModelPart key1;
    private final ModelPart key2;
    private final ModelPart key3;
    private final ModelPart key4;
    private final ModelPart key5;

    public ModelRobot1(ModelPart root) {
        this(root, 2.0F);
    }

    public ModelRobot1(ModelPart root, float wingspeed) {
        this.root = root;
        this.wingspeed = wingspeed;
        this.Shape1 = root.getChild("Shape1");
        this.Shape2 = root.getChild("Shape2");
        this.Shape2a = root.getChild("Shape2a");
        this.Shape3 = root.getChild("Shape3");
        this.Shape4 = root.getChild("Shape4");
        this.Shape5 = root.getChild("Shape5");
        this.Shape6 = root.getChild("Shape6");
        this.Shape7 = root.getChild("Shape7");
        this.Shape8 = root.getChild("Shape8");
        this.Shape9 = root.getChild("Shape9");
        this.Shape10 = root.getChild("Shape10");
        this.Shape11 = root.getChild("Shape11");
        this.Shape12 = root.getChild("Shape12");
        this.Shape13 = root.getChild("Shape13");
        this.Shape14 = root.getChild("Shape14");
        this.Shape15 = root.getChild("Shape15");
        this.Shape15a = root.getChild("Shape15a");
        this.Shape16 = root.getChild("Shape16");
        this.Shape17 = root.getChild("Shape17");
        this.Shape18 = root.getChild("Shape18");
        this.rfoot = root.getChild("rfoot");
        this.lfoot = root.getChild("lfoot");
        this.key1 = root.getChild("key1");
        this.key2 = root.getChild("key2");
        this.key3 = root.getChild("key3");
        this.key4 = root.getChild("key4");
        this.key5 = root.getChild("key5");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        root.addOrReplaceChild("Shape1",
                CubeListBuilder.create().texOffs(0, 0)
                        .addBox(0.0F, 0.0F, 0.0F, 3.0F, 9.0F, 3.0F),
                PartPose.offset(-1.0F, 13.0F, -1.0F));
        root.addOrReplaceChild("Shape2",
                CubeListBuilder.create().texOffs(0, 0)
                        .addBox(0.0F, 0.0F, 0.0F, 1.0F, 9.0F, 5.0F),
                PartPose.offset(0.0F, 13.0F, -2.0F));
        root.addOrReplaceChild("Shape2a",
                CubeListBuilder.create().texOffs(0, 0)
                        .addBox(0.0F, 0.0F, 0.0F, 5.0F, 9.0F, 1.0F),
                PartPose.offset(-2.0F, 13.0F, 0.0F));
        root.addOrReplaceChild("Shape3",
                CubeListBuilder.create().texOffs(0, 0)
                        .addBox(0.0F, 0.0F, 0.0F, 7.0F, 7.0F, 3.0F),
                PartPose.offset(-3.0F, 14.0F, -1.0F));
        root.addOrReplaceChild("Shape4",
                CubeListBuilder.create().texOffs(0, 0)
                        .addBox(0.0F, 0.0F, 0.0F, 3.0F, 7.0F, 7.0F),
                PartPose.offset(-1.0F, 14.0F, -3.0F));
        root.addOrReplaceChild("Shape5",
                CubeListBuilder.create().texOffs(0, 0)
                        .addBox(0.0F, 0.0F, 0.0F, 5.0F, 7.0F, 5.0F),
                PartPose.offset(-2.0F, 14.0F, -2.0F));
        root.addOrReplaceChild("Shape6",
                CubeListBuilder.create().texOffs(0, 0)
                        .addBox(0.0F, 0.0F, 0.0F, 5.0F, 5.0F, 7.0F),
                PartPose.offset(-2.0F, 15.0F, -3.0F));
        root.addOrReplaceChild("Shape7",
                CubeListBuilder.create().texOffs(0, 0)
                        .addBox(0.0F, 0.0F, 0.0F, 1.0F, 5.0F, 1.0F),
                PartPose.offset(0.0F, 15.0F, 4.0F));
        root.addOrReplaceChild("Shape8",
                CubeListBuilder.create().texOffs(0, 0)
                        .addBox(0.0F, 0.0F, 0.0F, 7.0F, 5.0F, 5.0F),
                PartPose.offset(-3.0F, 15.0F, -2.0F));
        root.addOrReplaceChild("Shape9",
                CubeListBuilder.create().texOffs(0, 0)
                        .addBox(0.0F, 0.0F, 0.0F, 9.0F, 5.0F, 1.0F),
                PartPose.offset(-4.0F, 15.0F, 0.0F));
        root.addOrReplaceChild("Shape10",
                CubeListBuilder.create().texOffs(0, 0)
                        .addBox(0.0F, 0.0F, 1.0F, 3.0F, 3.0F, 8.0F),
                PartPose.offset(-1.0F, 16.0F, -4.0F));
        root.addOrReplaceChild("Shape11",
                CubeListBuilder.create().texOffs(0, 0)
                        .addBox(0.0F, 0.0F, 0.0F, 9.0F, 3.0F, 3.0F),
                PartPose.offset(-4.0F, 16.0F, -1.0F));
        root.addOrReplaceChild("Shape12",
                CubeListBuilder.create().texOffs(0, 0)
                        .addBox(0.0F, 0.0F, 0.0F, 7.0F, 3.0F, 7.0F),
                PartPose.offset(-3.0F, 16.0F, -3.0F));
        root.addOrReplaceChild("Shape13",
                CubeListBuilder.create().texOffs(0, 0)
                        .addBox(0.0F, 0.0F, 0.0F, 9.0F, 1.0F, 5.0F),
                PartPose.offset(-4.0F, 17.0F, -2.0F));
        root.addOrReplaceChild("Shape14",
                CubeListBuilder.create().texOffs(0, 0)
                        .addBox(0.0F, 0.0F, 0.0F, 5.0F, 1.0F, 1.0F),
                PartPose.offset(-2.0F, 17.0F, 4.0F));
        root.addOrReplaceChild("Shape15",
                CubeListBuilder.create().texOffs(32, 0)
                        .addBox(0.0F, 0.0F, 0.0F, 2.0F, 3.0F, 1.0F),
                PartPose.offset(-2.0F, 15.0F, -4.0F));
        root.addOrReplaceChild("Shape15a",
                CubeListBuilder.create().texOffs(32, 0)
                        .addBox(0.0F, 0.0F, 0.0F, 2.0F, 3.0F, 1.0F),
                PartPose.offset(1.0F, 15.0F, -4.0F));
        root.addOrReplaceChild("Shape16",
                CubeListBuilder.create().texOffs(45, 0)
                        .addBox(0.0F, 0.0F, 0.0F, 3.0F, 1.0F, 3.0F),
                PartPose.offset(-1.0F, 12.0F, -1.0F));
        root.addOrReplaceChild("Shape17",
                CubeListBuilder.create().texOffs(33, 7)
                        .addBox(0.0F, 0.0F, 0.0F, 1.0F, 2.0F, 1.0F),
                PartPose.offset(0.0F, 10.0F, 0.0F));
        root.addOrReplaceChild("Shape18",
                CubeListBuilder.create().texOffs(33, 7)
                        .addBox(0.0F, 0.0F, 0.0F, 1.0F, 2.0F, 1.0F),
                PartPose.offsetAndRotation(1.7F, 8.733334F, 0.0F, 0.0F, 0.0F, 0.9667472F));
        root.addOrReplaceChild("rfoot",
                CubeListBuilder.create().texOffs(46, 8)
                        .addBox(0.0F, 3.0F, -2.0F, 2.0F, 2.0F, 4.0F),
                PartPose.offset(-3.0F, 19.0F, 0.0F));
        root.addOrReplaceChild("lfoot",
                CubeListBuilder.create().texOffs(46, 8)
                        .addBox(0.0F, 3.0F, -2.0F, 2.0F, 2.0F, 4.0F),
                PartPose.offset(2.0F, 19.0F, 0.0F));
        root.addOrReplaceChild("key2",
                CubeListBuilder.create().texOffs(46, 8)
                        .addBox(-0.5F, -1.5F, 1.0F, 1.0F, 3.0F, 1.0F),
                PartPose.offset(0.5F, 17.5F, 5.0F));
        root.addOrReplaceChild("key1",
                CubeListBuilder.create().texOffs(46, 8)
                        .addBox(-0.5F, -0.5F, 0.0F, 1.0F, 1.0F, 3.0F),
                PartPose.offset(0.5F, 17.5F, 5.0F));
        root.addOrReplaceChild("key3",
                CubeListBuilder.create().texOffs(46, 8)
                        .addBox(-0.5F, -2.5F, 1.0F, 1.0F, 1.0F, 2.0F),
                PartPose.offset(0.5F, 17.5F, 5.0F));
        root.addOrReplaceChild("key4",
                CubeListBuilder.create().texOffs(46, 8)
                        .addBox(-0.5F, 1.5F, 1.0F, 1.0F, 1.0F, 2.0F),
                PartPose.offset(0.5F, 17.5F, 5.0F));
        root.addOrReplaceChild("key5",
                CubeListBuilder.create().texOffs(46, 8)
                        .addBox(-0.5F, -1.5F, 3.0F, 1.0F, 3.0F, 1.0F),
                PartPose.offset(0.5F, 17.5F, 5.0F));

        return LayerDefinition.create(mesh, 64, 32);
    }

    @Override
    public ModelPart root() {
        return this.root;
    }

    @Override
    public void setupAnim(
            Robot1 entity,
            float limbSwing,
            float limbSwingAmount,
            float ageInTicks,
            float netHeadYaw,
            float headPitch) {
        float f1 = limbSwingAmount;
        float f2 = ageInTicks;
        float newangle;
        if (f1 > 0.1F) {
            newangle = Mth.cos(f2 * 1.5F * this.wingspeed) * (float) Math.PI * 0.75F * f1;
        } else {
            newangle = 0.0F;
        }
        this.lfoot.xRot = newangle;
        this.rfoot.xRot = -newangle;
        // gold continuous key wind: toRadians(f2 * 0.75 * wingspeed) on zRot for all key parts
        newangle = (float) Math.toRadians(f2 * 0.75F * this.wingspeed);
        this.key1.zRot = newangle;
        this.key2.zRot = newangle;
        this.key3.zRot = newangle;
        this.key4.zRot = newangle;
        this.key5.zRot = newangle;
    }
}
