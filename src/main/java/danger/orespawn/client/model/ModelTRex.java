package danger.orespawn.client.model;

import danger.orespawn.entity.TRex;
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
 * Port of gold {@code ModelTRex} (1.12 ModelBase) → 1.21 HierarchicalModel.
 */
@OnlyIn(Dist.CLIENT)
public class ModelTRex extends HierarchicalModel<TRex> {
    private final float wingspeed;
    private final ModelPart root;
    private final ModelPart Shape18;
    private final ModelPart Shape19;
    private final ModelPart Shape20;
    private final ModelPart Shape21;
    private final ModelPart Shape1;
    private final ModelPart Shape2;
    private final ModelPart Shape3;
    private final ModelPart Shape4;
    private final ModelPart Shape5;
    private final ModelPart Shape6;
    private final ModelPart jaw;
    private final ModelPart leftleg;
    private final ModelPart leftleg2;
    private final ModelPart leftleg3;
    private final ModelPart Shape11;
    private final ModelPart rightleg;
    private final ModelPart rightleg2;
    private final ModelPart rightleg3;
    private final ModelPart leftleg4;
    private final ModelPart rightleg4;
    private final ModelPart Shape17;
    private final ModelPart TailExtension;
    private final ModelPart Spine1;
    private final ModelPart Spine2;
    private final ModelPart Spine3;
    private final ModelPart Spine4;
    private final ModelPart Spine5;

    public ModelTRex(ModelPart root) {
        this(root, 1.5F);
    }

    public ModelTRex(ModelPart root, float wingspeed) {
        this.root = root;
        this.wingspeed = wingspeed;
        this.Shape18 = root.getChild("Shape18");
        this.Shape19 = root.getChild("Shape19");
        this.Shape20 = root.getChild("Shape20");
        this.Shape21 = root.getChild("Shape21");
        this.Shape1 = root.getChild("Shape1");
        this.Shape2 = root.getChild("Shape2");
        this.Shape3 = root.getChild("Shape3");
        this.Shape4 = root.getChild("Shape4");
        this.Shape5 = root.getChild("Shape5");
        this.Shape6 = root.getChild("Shape6");
        this.jaw = root.getChild("jaw");
        this.leftleg = root.getChild("leftleg");
        this.leftleg2 = root.getChild("leftleg2");
        this.leftleg3 = root.getChild("leftleg3");
        this.Shape11 = root.getChild("Shape11");
        this.rightleg = root.getChild("rightleg");
        this.rightleg2 = root.getChild("rightleg2");
        this.rightleg3 = root.getChild("rightleg3");
        this.leftleg4 = root.getChild("leftleg4");
        this.rightleg4 = root.getChild("rightleg4");
        this.Shape17 = root.getChild("Shape17");
        this.TailExtension = root.getChild("TailExtension");
        this.Spine1 = root.getChild("Spine1");
        this.Spine2 = root.getChild("Spine2");
        this.Spine3 = root.getChild("Spine3");
        this.Spine4 = root.getChild("Spine4");
        this.Spine5 = root.getChild("Spine5");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        root.addOrReplaceChild("Shape18",
                CubeListBuilder.create().texOffs(91, 114)
                        .addBox(0.0F, 0.0F, 0.0F, 2.0F, 4.0F, 5.0F),
                PartPose.offsetAndRotation(3.3F, -25.0F, -23.0F, 0.5759587F, 0.0F, 0.5585054F));
        root.addOrReplaceChild("Shape19",
                CubeListBuilder.create().texOffs(71, 114)
                        .addBox(0.0F, 0.0F, 0.0F, 2.0F, 4.0F, 5.0F),
                PartPose.offsetAndRotation(-4.0F, -24.0F, -23.0F, 0.5759587F, 0.0F, -0.5585054F));
        root.addOrReplaceChild("Shape20",
                CubeListBuilder.create().texOffs(91, 30)
                        .addBox(0.0F, 0.0F, 0.0F, 2.0F, 7.0F, 5.0F),
                PartPose.offsetAndRotation(5.0F, -8.0F, -6.0F, 0.3839724F, 0.0F, 0.0F));
        root.addOrReplaceChild("Shape21",
                CubeListBuilder.create().texOffs(93, 46)
                        .addBox(-2.0F, 0.0F, 0.0F, 2.0F, 7.0F, 5.0F),
                PartPose.offsetAndRotation(-4.0F, -8.0F, -6.0F, 0.3839724F, 0.0F, 0.0F));
        root.addOrReplaceChild("Shape1",
                CubeListBuilder.create().texOffs(0, 0)
                        .addBox(-7.0F, 0.0F, 0.0F, 10.0F, 18.0F, 31.0F),
                PartPose.offset(2.5F, -19.0F, -8.0F));
        root.addOrReplaceChild("Shape2",
                CubeListBuilder.create().texOffs(62, 0)
                        .addBox(-5.0F, 0.0F, 0.0F, 10.0F, 11.0F, 11.0F),
                PartPose.offset(0.5F, -19.0F, 23.0F));
        root.addOrReplaceChild("Shape3",
                CubeListBuilder.create().texOffs(10, 54)
                        .addBox(-3.0F, 0.0F, 0.0F, 7.0F, 7.0F, 25.0F),
                PartPose.offset(0.0F, -19.0F, 34.0F));
        root.addOrReplaceChild("Shape4",
                CubeListBuilder.create().texOffs(68, 88)
                        .addBox(-5.0F, 0.0F, 0.0F, 8.0F, 9.0F, 16.0F),
                PartPose.offsetAndRotation(1.5F, -25.0F, -16.0F, -0.4014257F, 0.0F, 0.0F));
        root.addOrReplaceChild("Shape5",
                CubeListBuilder.create().texOffs(75, 65)
                        .addBox(0.0F, 0.0F, 0.0F, 9.0F, 9.0F, 12.0F),
                PartPose.offset(-4.0F, -25.0F, -27.0F));
        root.addOrReplaceChild("Shape6",
                CubeListBuilder.create().texOffs(0, 50)
                        .addBox(0.0F, 0.0F, 0.0F, 7.0F, 9.0F, 9.0F),
                PartPose.offset(-3.0F, -25.0F, -36.0F));
        root.addOrReplaceChild("jaw",
                CubeListBuilder.create().texOffs(0, 86)
                        .addBox(-5.0F, 0.0F, -10.0F, 7.0F, 1.0F, 13.0F),
                PartPose.offsetAndRotation(2.0F, -15.0F, -24.0F, 0.5201081F, 0.0F, 0.0F));
        root.addOrReplaceChild("leftleg",
                CubeListBuilder.create().texOffs(0, 0)
                        .addBox(-1.0F, 0.0F, 0.0F, 3.0F, 16.0F, 10.0F),
                PartPose.offsetAndRotation(6.0F, -10.0F, 11.0F, -0.1745329F, 0.0F, 0.0F));
        root.addOrReplaceChild("leftleg2",
                CubeListBuilder.create().texOffs(0, 106)
                        .addBox(-1.0F, 12.0F, -8.0F, 3.0F, 15.0F, 5.0F),
                PartPose.offsetAndRotation(6.0F, -10.0F, 11.0F, 0.5061455F, 0.0F, 0.0F));
        root.addOrReplaceChild("leftleg3",
                CubeListBuilder.create().texOffs(112, 89)
                        .addBox(-1.0F, 19.0F, 16.0F, 3.0F, 9.0F, 3.0F),
                PartPose.offsetAndRotation(6.0F, -10.0F, 11.0F, -0.4014257F, 0.0F, 0.0F));
        root.addOrReplaceChild("Shape11",
                CubeListBuilder.create().texOffs(0, 72)
                        .addBox(0.0F, 0.0F, 0.0F, 2.0F, 10.0F, 2.0F),
                PartPose.offsetAndRotation(5.0F, -5.0F, -3.0F, (float) (-Math.PI / 6), 0.0F, 0.0F));
        root.addOrReplaceChild("rightleg",
                CubeListBuilder.create().texOffs(54, 51)
                        .addBox(0.0F, 0.0F, 0.0F, 3.0F, 16.0F, 10.0F),
                PartPose.offsetAndRotation(-7.0F, -10.0F, 11.0F, -0.1745329F, 0.0F, 0.0F));
        root.addOrReplaceChild("rightleg2",
                CubeListBuilder.create().texOffs(23, 106)
                        .addBox(0.0F, 12.0F, -8.0F, 3.0F, 15.0F, 5.0F),
                PartPose.offsetAndRotation(-7.0F, -10.0F, 11.0F, 0.5061455F, 0.0F, 0.0F));
        root.addOrReplaceChild("rightleg3",
                CubeListBuilder.create().texOffs(70, 90)
                        .addBox(0.0F, 19.0F, 16.0F, 3.0F, 9.0F, 3.0F),
                PartPose.offsetAndRotation(-7.0F, -10.0F, 11.0F, -0.4014257F, 0.0F, 0.0F));
        root.addOrReplaceChild("leftleg4",
                CubeListBuilder.create().texOffs(42, 113)
                        .addBox(-1.0F, 31.0F, -1.0F, 3.0F, 3.0F, 8.0F),
                PartPose.offset(6.0F, -10.0F, 11.0F));
        root.addOrReplaceChild("rightleg4",
                CubeListBuilder.create().texOffs(44, 93)
                        .addBox(0.0F, 31.0F, -1.0F, 3.0F, 3.0F, 8.0F),
                PartPose.offset(-7.0F, -10.0F, 11.0F));
        root.addOrReplaceChild("Shape17",
                CubeListBuilder.create().texOffs(112, 60)
                        .addBox(-2.0F, 0.0F, 0.0F, 2.0F, 10.0F, 2.0F),
                PartPose.offsetAndRotation(-4.0F, -3.533333F, -3.0F, (float) (-Math.PI / 6), 0.0F, 0.0F));
        root.addOrReplaceChild("TailExtension",
                CubeListBuilder.create().texOffs(0, 10)
                        .addBox(0.0F, 0.0F, 0.0F, 3.0F, 3.0F, 10.0F),
                PartPose.offset(-1.0F, -19.0F, 59.0F));
        root.addOrReplaceChild("Spine1",
                CubeListBuilder.create().texOffs(73, 0)
                        .addBox(0.0F, 0.0F, 0.0F, 2.0F, 2.0F, 3.0F),
                PartPose.offset(-1.0F, -21.0F, 0.0F));
        root.addOrReplaceChild("Spine2",
                CubeListBuilder.create().texOffs(73, 0)
                        .addBox(0.0F, 0.0F, 0.0F, 2.0F, 2.0F, 3.0F),
                PartPose.offset(-0.5F, -21.0F, 6.0F));
        root.addOrReplaceChild("Spine3",
                CubeListBuilder.create().texOffs(73, 0)
                        .addBox(0.0F, 0.0F, 0.0F, 2.0F, 2.0F, 3.0F),
                PartPose.offset(-0.5F, -21.0F, 12.0F));
        root.addOrReplaceChild("Spine4",
                CubeListBuilder.create().texOffs(73, 0)
                        .addBox(0.0F, 0.0F, 0.0F, 2.0F, 2.0F, 3.0F),
                PartPose.offsetAndRotation(-0.5F, -24.0F, -9.0F, -0.4014257F, 0.0F, 0.0F));
        root.addOrReplaceChild("Spine5",
                CubeListBuilder.create().texOffs(73, 0)
                        .addBox(0.0F, 0.0F, 0.0F, 2.0F, 2.0F, 3.0F),
                PartPose.offsetAndRotation(-0.5F, -26.0F, -14.0F, -0.4014257F, 0.0F, 0.0F));
        return LayerDefinition.create(mesh, 128, 128);
    }

    @Override
    public ModelPart root() {
        return this.root;
    }

    @Override
    public void setupAnim(TRex entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        animate(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
    }

    
    private void animate(TRex entity, float f, float f1, float f2, float f3, float f4) {
        float newangle = 0.0F;
        if (f1 > 0.1F) {
            newangle = Mth.cos(f2 * 0.4F * this.wingspeed) * (float) Math.PI * 0.25F * f1;
        }
        this.rightleg.xRot = -0.174F + newangle;
        this.rightleg2.xRot = 0.506F + newangle;
        this.rightleg3.xRot = -0.401F + newangle;
        this.rightleg4.xRot = newangle;
        this.leftleg.xRot = -0.174F - newangle;
        this.leftleg2.xRot = 0.506F - newangle;
        this.leftleg3.xRot = -0.401F - newangle;
        this.leftleg4.xRot = -newangle;
        if (entity.getAttacking() != 0) {
            this.jaw.xRot = 0.52F + Mth.cos(f2 * 0.45F) * (float) Math.PI * 0.18F;
        } else {
            this.jaw.xRot = 0.1F;
        }
        this.Shape17.xRot = -0.523F + Mth.cos(f2 * 0.1F) * (float) Math.PI * 0.05F;
        this.Shape11.xRot = -0.523F + Mth.cos(f2 * 0.1F) * (float) Math.PI * 0.05F;
    }
}

