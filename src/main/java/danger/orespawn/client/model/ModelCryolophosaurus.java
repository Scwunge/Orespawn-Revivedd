package danger.orespawn.client.model;

import danger.orespawn.entity.Cryolophosaurus;
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
 * Port of gold {@code ModelCryolophosaurus} (1.12 ModelBase) → 1.21 HierarchicalModel.
 */
@OnlyIn(Dist.CLIENT)
public class ModelCryolophosaurus extends HierarchicalModel<Cryolophosaurus> {
    private final float wingspeed;
    private final ModelPart root;
    private final ModelPart Shape1;
    private final ModelPart Shape2;
    private final ModelPart Shape3;
    private final ModelPart jaw;
    private final ModelPart Shape5;
    private final ModelPart Shape6;
    private final ModelPart Shape7;
    private final ModelPart Shape8;
    private final ModelPart Shape9;
    private final ModelPart rightleg;
    private final ModelPart Shape11;
    private final ModelPart rightleg2;
    private final ModelPart rightleg3;
    private final ModelPart rightleg4;
    private final ModelPart leftleg;
    private final ModelPart Shape16;
    private final ModelPart Shape17;
    private final ModelPart leftleg2;
    private final ModelPart leftleg3;
    private final ModelPart leftleg4;

    public ModelCryolophosaurus(ModelPart root) {
        this(root, 1.5F);
    }

    public ModelCryolophosaurus(ModelPart root, float wingspeed) {
        this.root = root;
        this.wingspeed = wingspeed;
        this.Shape1 = root.getChild("Shape1");
        this.Shape2 = root.getChild("Shape2");
        this.Shape3 = root.getChild("Shape3");
        this.jaw = root.getChild("jaw");
        this.Shape5 = root.getChild("Shape5");
        this.Shape6 = root.getChild("Shape6");
        this.Shape7 = root.getChild("Shape7");
        this.Shape8 = root.getChild("Shape8");
        this.Shape9 = root.getChild("Shape9");
        this.rightleg = root.getChild("rightleg");
        this.Shape11 = root.getChild("Shape11");
        this.rightleg2 = root.getChild("rightleg2");
        this.rightleg3 = root.getChild("rightleg3");
        this.rightleg4 = root.getChild("rightleg4");
        this.leftleg = root.getChild("leftleg");
        this.Shape16 = root.getChild("Shape16");
        this.Shape17 = root.getChild("Shape17");
        this.leftleg2 = root.getChild("leftleg2");
        this.leftleg3 = root.getChild("leftleg3");
        this.leftleg4 = root.getChild("leftleg4");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        root.addOrReplaceChild("Shape1",
                CubeListBuilder.create().texOffs(0, 0)
                        .addBox(0.0F, 0.0F, 0.0F, 8.0F, 9.0F, 18.0F),
                PartPose.offset(0.0F, 0.0F, 0.0F));
        root.addOrReplaceChild("Shape2",
                CubeListBuilder.create().texOffs(53, 0)
                        .addBox(0.0F, 0.0F, 0.0F, 6.0F, 4.0F, 11.0F),
                PartPose.offsetAndRotation(1.0F, -2.0F, -7.0F, -0.2268928F, 0.0F, 0.0F));
        root.addOrReplaceChild("Shape3",
                CubeListBuilder.create().texOffs(0, 41)
                        .addBox(0.0F, 0.0F, 0.0F, 6.0F, 4.0F, 10.0F),
                PartPose.offset(1.0F, -2.0F, -15.0F));
        root.addOrReplaceChild("jaw",
                CubeListBuilder.create().texOffs(0, 30)
                        .addBox(0.0F, 0.0F, 0.0F, 4.0F, 9.0F, 1.0F),
                PartPose.offsetAndRotation(2.0F, 1.0F, -8.0F, -1.256637F, 0.0F, 0.0F));
        root.addOrReplaceChild("Shape5",
                CubeListBuilder.create().texOffs(91, 0)
                        .addBox(0.0F, 0.0F, 0.0F, 6.0F, 6.0F, 7.0F),
                PartPose.offset(1.0F, 0.0F, 18.0F));
        root.addOrReplaceChild("Shape6",
                CubeListBuilder.create().texOffs(36, 31)
                        .addBox(0.0F, 0.0F, 0.0F, 4.0F, 4.0F, 14.0F),
                PartPose.offset(2.0F, 0.0F, 25.0F));
        root.addOrReplaceChild("Shape7",
                CubeListBuilder.create().texOffs(43, 8)
                        .addBox(0.0F, 0.0F, 0.0F, 1.0F, 4.0F, 2.0F),
                PartPose.offsetAndRotation(-1.0F, 8.0F, 0.0F, 0.1919862F, 0.0F, 0.0F));
        root.addOrReplaceChild("Shape8",
                CubeListBuilder.create().texOffs(9, 0)
                        .addBox(0.0F, 0.0F, 0.0F, 1.0F, 3.0F, 1.0F),
                PartPose.offsetAndRotation(-1.0F, 11.0F, 1.0F, (float) (-Math.PI / 12), 0.0F, 0.0F));
        root.addOrReplaceChild("Shape9",
                CubeListBuilder.create().texOffs(0, 0)
                        .addBox(0.0F, 0.0F, 0.0F, 2.0F, 4.0F, 1.0F),
                PartPose.offsetAndRotation(3.0F, -4.0F, -9.0F, (float) (-Math.PI * 3.0 / 10.0), 0.0F, 0.0F));
        root.addOrReplaceChild("rightleg",
                CubeListBuilder.create().texOffs(0, 58)
                        .addBox(0.0F, 0.0F, 0.0F, 2.0F, 10.0F, 6.0F),
                PartPose.offsetAndRotation(-1.0F, 2.0F, 12.0F, -0.2792527F, 0.0F, 0.0F));
        root.addOrReplaceChild("Shape11",
                CubeListBuilder.create().texOffs(39, 0)
                        .addBox(0.0F, 0.0F, 0.0F, 4.0F, 3.0F, 3.0F),
                PartPose.offset(2.0F, -1.0F, -18.0F));
        root.addOrReplaceChild("rightleg2",
                CubeListBuilder.create().texOffs(0, 77)
                        .addBox(0.0F, 7.0F, -5.0F, 2.0F, 10.0F, 3.0F),
                PartPose.offsetAndRotation(-1.0F, 2.0F, 12.0F, 0.3839724F, 0.0F, 0.0F));
        root.addOrReplaceChild("rightleg3",
                CubeListBuilder.create().texOffs(35, 31)
                        .addBox(0.0F, 10.0F, 12.0F, 2.0F, 7.0F, 2.0F),
                PartPose.offsetAndRotation(-1.0F, 2.0F, 12.0F, -0.6806784F, 0.0F, 0.0F));
        root.addOrReplaceChild("rightleg4",
                CubeListBuilder.create().texOffs(68, 55)
                        .addBox(0.0F, 20.0F, -5.0F, 2.0F, 2.0F, 6.0F),
                PartPose.offset(-1.0F, 2.0F, 12.0F));
        root.addOrReplaceChild("leftleg",
                CubeListBuilder.create().texOffs(22, 58)
                        .addBox(0.0F, 0.0F, 0.0F, 2.0F, 10.0F, 6.0F),
                PartPose.offsetAndRotation(7.0F, 2.0F, 12.0F, -0.2792527F, 0.0F, 0.0F));
        root.addOrReplaceChild("Shape16",
                CubeListBuilder.create().texOffs(0, 8)
                        .addBox(0.0F, 0.0F, 0.0F, 1.0F, 4.0F, 2.0F),
                PartPose.offsetAndRotation(8.0F, 8.0F, 0.0F, 0.1919862F, 0.0F, 0.0F));
        root.addOrReplaceChild("Shape17",
                CubeListBuilder.create().texOffs(9, 9)
                        .addBox(0.0F, 0.0F, 0.0F, 1.0F, 3.0F, 1.0F),
                PartPose.offsetAndRotation(8.0F, 11.0F, 1.0F, (float) (-Math.PI / 12), 0.0F, 0.0F));
        root.addOrReplaceChild("leftleg2",
                CubeListBuilder.create().texOffs(16, 77)
                        .addBox(0.0F, 7.0F, -5.0F, 2.0F, 10.0F, 3.0F),
                PartPose.offsetAndRotation(7.0F, 2.0F, 12.0F, 0.3839724F, 0.0F, 0.0F));
        root.addOrReplaceChild("leftleg3",
                CubeListBuilder.create().texOffs(67, 31)
                        .addBox(0.0F, 10.0F, 12.0F, 2.0F, 7.0F, 2.0F),
                PartPose.offsetAndRotation(7.0F, 2.0F, 12.0F, -0.6806784F, 0.0F, 0.0F));
        root.addOrReplaceChild("leftleg4",
                CubeListBuilder.create().texOffs(47, 56)
                        .addBox(0.0F, 20.0F, -5.0F, 2.0F, 2.0F, 6.0F),
                PartPose.offset(7.0F, 2.0F, 12.0F));
        return LayerDefinition.create(mesh, 128, 128);
    }

    @Override
    public ModelPart root() {
        return this.root;
    }

    @Override
    public void setupAnim(Cryolophosaurus entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        animate(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
    }

    private void animate(Cryolophosaurus entity, float f, float f1, float f2, float f3, float f4) {
        float newangle = 0.0F;
        if (f1 > 0.1) {
        newangle = Mth.cos(f2 * 0.4F * this.wingspeed) * (float) Math.PI * 0.25F * f1;
        } else {
        newangle = 0.0F;
        }
        this.rightleg.xRot = -0.2792527F + newangle;
        this.rightleg2.xRot = 0.384F + newangle;
        this.rightleg3.xRot = -0.68F + newangle;
        this.rightleg4.xRot = newangle;
        this.leftleg.xRot = -0.2792527F - newangle;
        this.leftleg2.xRot = 0.384F - newangle;
        this.leftleg3.xRot = -0.68F - newangle;
        this.leftleg4.xRot = -newangle;
        this.jaw.xRot = -1.15F + Mth.cos(f2 * 0.28F) * (float) Math.PI * 0.1F;
    }
}
