package danger.orespawn.client.model;

import danger.orespawn.entity.Mantis;
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
 * Port of gold {@code ModelMantis} (1.12 ModelBase) → 1.21 HierarchicalModel.
 */
@OnlyIn(Dist.CLIENT)
public class ModelMantis extends HierarchicalModel<Mantis> {
    private final float wingspeed;
    private final ModelPart root;
    private final ModelPart lfleg1;
    private final ModelPart lfleg2;
    private final ModelPart lfleg3;
    private final ModelPart lfleg4;
    private final ModelPart lrleg1;
    private final ModelPart lrleg2;
    private final ModelPart lrleg3;
    private final ModelPart lrleg4;
    private final ModelPart abdomen;
    private final ModelPart thorax;
    private final ModelPart neck1;
    private final ModelPart neck2;
    private final ModelPart head1;
    private final ModelPart head2;
    private final ModelPart leye;
    private final ModelPart reye;
    private final ModelPart lantenna;
    private final ModelPart rantenna;
    private final ModelPart larm1;
    private final ModelPart larm2;
    private final ModelPart larm3;
    private final ModelPart lfwing;
    private final ModelPart rfwing;
    private final ModelPart lrwing;
    private final ModelPart rrwing;
    private final ModelPart rarm1;
    private final ModelPart rarm2;
    private final ModelPart rarm3;
    private final ModelPart rlfleg3;
    private final ModelPart rfleg4;
    private final ModelPart rfleg2;
    private final ModelPart rfleg1;
    private final ModelPart rrleg3;
    private final ModelPart rrleg4;
    private final ModelPart rrleg2;
    private final ModelPart rrleg1;

    public ModelMantis(ModelPart root) {
        this(root, 1F);
    }

    public ModelMantis(ModelPart root, float wingspeed) {
        this.root = root;
        this.wingspeed = wingspeed;
        this.lfleg1 = root.getChild("lfleg1");
        this.lfleg2 = root.getChild("lfleg2");
        this.lfleg3 = root.getChild("lfleg3");
        this.lfleg4 = root.getChild("lfleg4");
        this.lrleg1 = root.getChild("lrleg1");
        this.lrleg2 = root.getChild("lrleg2");
        this.lrleg3 = root.getChild("lrleg3");
        this.lrleg4 = root.getChild("lrleg4");
        this.abdomen = root.getChild("abdomen");
        this.thorax = root.getChild("thorax");
        this.neck1 = root.getChild("neck1");
        this.neck2 = root.getChild("neck2");
        this.head1 = root.getChild("head1");
        this.head2 = root.getChild("head2");
        this.leye = root.getChild("leye");
        this.reye = root.getChild("reye");
        this.lantenna = root.getChild("lantenna");
        this.rantenna = root.getChild("rantenna");
        this.larm1 = root.getChild("larm1");
        this.larm2 = root.getChild("larm2");
        this.larm3 = root.getChild("larm3");
        this.lfwing = root.getChild("lfwing");
        this.rfwing = root.getChild("rfwing");
        this.lrwing = root.getChild("lrwing");
        this.rrwing = root.getChild("rrwing");
        this.rarm1 = root.getChild("rarm1");
        this.rarm2 = root.getChild("rarm2");
        this.rarm3 = root.getChild("rarm3");
        this.rlfleg3 = root.getChild("rlfleg3");
        this.rfleg4 = root.getChild("rfleg4");
        this.rfleg2 = root.getChild("rfleg2");
        this.rfleg1 = root.getChild("rfleg1");
        this.rrleg3 = root.getChild("rrleg3");
        this.rrleg4 = root.getChild("rrleg4");
        this.rrleg2 = root.getChild("rrleg2");
        this.rrleg1 = root.getChild("rrleg1");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        root.addOrReplaceChild("lfleg1",
                CubeListBuilder.create().texOffs(28, 35)
                        .addBox(0.0F, 0.0F, 0.0F, 1.0F, 10.0F, 1.0F),
                PartPose.offsetAndRotation(27.0F, 16.0F, -3.0F, 0.0F, 0.0F, -0.6283185F));
        root.addOrReplaceChild("lfleg2",
                CubeListBuilder.create().texOffs(0, 32)
                        .addBox(0.0F, 0.0F, 0.0F, 1.0F, 22.0F, 1.0F),
                PartPose.offsetAndRotation(21.0F, -5.0F, -3.0F, 0.0F, 0.0F, -0.2792527F));
        root.addOrReplaceChild("lfleg3",
                CubeListBuilder.create().texOffs(64, 2)
                        .addBox(0.0F, 0.0F, 0.0F, 20.0F, 1.0F, 1.0F),
                PartPose.offsetAndRotation(2.0F, -5.0F, 0.0F, 0.0F, 0.1570796F, 0.0F));
        root.addOrReplaceChild("lfleg4",
                CubeListBuilder.create().texOffs(64, 20)
                        .addBox(15.0F, 0.0F, -2.0F, 4.0F, 1.0F, 5.0F),
                PartPose.offsetAndRotation(2.0F, -5.0F, 0.0F, 0.0F, 0.1570796F, 0.0F));
        root.addOrReplaceChild("lrleg1",
                CubeListBuilder.create().texOffs(35, 35)
                        .addBox(0.0F, 0.0F, 0.0F, 1.0F, 10.0F, 1.0F),
                PartPose.offsetAndRotation(32.0F, 18.0F, 11.0F, 0.0F, 0.0F, -0.8726646F));
        root.addOrReplaceChild("lrleg2",
                CubeListBuilder.create().texOffs(14, 32)
                        .addBox(0.0F, 0.0F, 0.0F, 1.0F, 22.0F, 1.0F),
                PartPose.offsetAndRotation(21.0F, 0.0F, 11.0F, 0.0F, 0.0F, -0.5410521F));
        root.addOrReplaceChild("lrleg3",
                CubeListBuilder.create().texOffs(64, 11)
                        .addBox(0.0F, 0.0F, 0.0F, 20.0F, 1.0F, 1.0F),
                PartPose.offsetAndRotation(2.0F, 0.0F, 8.0F, 0.0F, -0.1570796F, 0.0F));
        root.addOrReplaceChild("lrleg4",
                CubeListBuilder.create().texOffs(64, 36)
                        .addBox(15.0F, 0.0F, -2.0F, 4.0F, 1.0F, 5.0F),
                PartPose.offsetAndRotation(2.0F, 0.0F, 8.0F, 0.0F, -0.1570796F, 0.0F));
        root.addOrReplaceChild("abdomen",
                CubeListBuilder.create().texOffs(118, 0)
                        .addBox(0.0F, 0.0F, 0.0F, 9.0F, 5.0F, 53.0F),
                PartPose.offsetAndRotation(-4.0F, -11.0F, 0.0F, -0.5061455F, 0.0F, 0.0F));
        root.addOrReplaceChild("thorax",
                CubeListBuilder.create().texOffs(145, 62)
                        .addBox(0.0F, 0.0F, 0.0F, 15.0F, 3.0F, 13.0F),
                PartPose.offsetAndRotation(-7.0F, -14.0F, -12.0F, -0.2443461F, 0.0F, 0.0F));
        root.addOrReplaceChild("neck1",
                CubeListBuilder.create().texOffs(145, 82)
                        .addBox(0.0F, 0.0F, 0.0F, 9.0F, 1.0F, 15.0F),
                PartPose.offsetAndRotation(-4.0F, -15.0F, -27.0F, -0.0698132F, 0.0F, 0.0F));
        root.addOrReplaceChild("neck2",
                CubeListBuilder.create().texOffs(40, 150)
                        .addBox(0.0F, 0.0F, 0.0F, 3.0F, 1.0F, 2.0F),
                PartPose.offset(-1.0F, -15.0F, -29.0F));
        root.addOrReplaceChild("head1",
                CubeListBuilder.create().texOffs(0, 150)
                        .addBox(0.0F, 0.0F, 0.0F, 2.0F, 6.0F, 1.0F),
                PartPose.offsetAndRotation(0.0F, -16.0F, -30.0F, 0.0F, 0.0F, 0.1396263F));
        root.addOrReplaceChild("head2",
                CubeListBuilder.create().texOffs(10, 150)
                        .addBox(-2.0F, 0.0F, 0.0F, 2.0F, 6.0F, 1.0F),
                PartPose.offsetAndRotation(0.0F, -16.0F, -30.0F, 0.0F, 0.0F, -0.1745329F));
        root.addOrReplaceChild("leye",
                CubeListBuilder.create().texOffs(20, 150)
                        .addBox(1.0F, 0.0F, -0.5F, 2.0F, 2.0F, 1.0F),
                PartPose.offsetAndRotation(0.0F, -16.0F, -30.0F, 0.0F, 0.0F, 0.1396263F));
        root.addOrReplaceChild("reye",
                CubeListBuilder.create().texOffs(30, 150)
                        .addBox(-3.0F, 0.0F, -0.5F, 2.0F, 2.0F, 1.0F),
                PartPose.offsetAndRotation(0.0F, -16.0F, -30.0F, 0.0F, 0.0F, -0.1745329F));
        root.addOrReplaceChild("lantenna",
                CubeListBuilder.create().texOffs(53, 150)
                        .addBox(0.0F, -20.0F, 0.0F, 1.0F, 20.0F, 1.0F),
                PartPose.offsetAndRotation(0.0F, -16.0F, -30.0F, 0.0F, 0.0F, 0.2792527F));
        root.addOrReplaceChild("rantenna",
                CubeListBuilder.create().texOffs(60, 150)
                        .addBox(-1.0F, -20.0F, 0.0F, 1.0F, 20.0F, 1.0F),
                PartPose.offsetAndRotation(0.0F, -16.0F, -30.0F, 0.0F, 0.0F, -0.2792527F));
        root.addOrReplaceChild("larm1",
                CubeListBuilder.create().texOffs(51, 0)
                        .addBox(0.0F, 0.0F, -1.0F, 1.0F, 23.0F, 4.0F),
                PartPose.offsetAndRotation(2.0F, -14.0F, -23.0F, 0.0349066F, 0.0F, 0.0F));
        root.addOrReplaceChild("larm2",
                CubeListBuilder.create().texOffs(30, 0)
                        .addBox(0.0F, -18.0F, -2.0F, 1.0F, 18.0F, 2.0F),
                PartPose.offsetAndRotation(2.0F, 8.0F, -22.0F, 0.5585054F, 0.0F, 0.0F));
        root.addOrReplaceChild("larm3",
                CubeListBuilder.create().texOffs(16, 0)
                        .addBox(0.0F, 0.0F, 0.0F, 1.0F, 21.0F, 1.0F),
                PartPose.offset(2.0F, -7.0F, -33.0F));
        root.addOrReplaceChild("lfwing",
                CubeListBuilder.create().texOffs(0, 67)
                        .addBox(0.0F, 0.0F, 0.0F, 48.0F, 1.0F, 12.0F),
                PartPose.offsetAndRotation(2.0F, -11.0F, 0.0F, -0.2268928F, 0.0F, (float) (-Math.PI * 2.0 / 9.0)));
        root.addOrReplaceChild("rfwing",
                CubeListBuilder.create().texOffs(0, 83)
                        .addBox(-48.0F, 0.0F, 0.0F, 48.0F, 1.0F, 12.0F),
                PartPose.offsetAndRotation(-1.0F, -11.0F, 0.0F, -0.2268928F, 0.0F, (float) (Math.PI * 2.0 / 9.0)));
        root.addOrReplaceChild("lrwing",
                CubeListBuilder.create().texOffs(0, 100)
                        .addBox(0.0F, 0.0F, 0.0F, 42.0F, 1.0F, 17.0F),
                PartPose.offsetAndRotation(2.0F, -6.0F, 10.0F, -0.2268928F, 0.0F, -0.3490659F));
        root.addOrReplaceChild("rrwing",
                CubeListBuilder.create().texOffs(0, 122)
                        .addBox(-42.0F, 0.0F, 0.0F, 42.0F, 1.0F, 17.0F),
                PartPose.offsetAndRotation(-1.0F, -6.0F, 10.0F, -0.2268928F, 0.0F, 0.3490659F));
        root.addOrReplaceChild("rarm1",
                CubeListBuilder.create().texOffs(38, 0)
                        .addBox(0.0F, 0.0F, -1.0F, 1.0F, 23.0F, 4.0F),
                PartPose.offsetAndRotation(-1.0F, -14.0F, -23.0F, 0.0349066F, 0.0F, 0.0F));
        root.addOrReplaceChild("rarm2",
                CubeListBuilder.create().texOffs(22, 0)
                        .addBox(0.0F, -18.0F, -2.0F, 1.0F, 18.0F, 2.0F),
                PartPose.offsetAndRotation(-1.0F, 8.0F, -22.0F, 0.5585054F, 0.0F, 0.0F));
        root.addOrReplaceChild("rarm3",
                CubeListBuilder.create().texOffs(10, 0)
                        .addBox(0.0F, 0.0F, 0.0F, 1.0F, 21.0F, 1.0F),
                PartPose.offset(-1.0F, -7.0F, -33.0F));
        root.addOrReplaceChild("rlfleg3",
                CubeListBuilder.create().texOffs(64, 6)
                        .addBox(-20.0F, 0.0F, 0.0F, 20.0F, 1.0F, 1.0F),
                PartPose.offsetAndRotation(-1.0F, -5.0F, 0.0F, 0.0F, -0.1570796F, 0.0F));
        root.addOrReplaceChild("rfleg4",
                CubeListBuilder.create().texOffs(64, 28)
                        .addBox(-19.0F, 0.0F, -2.0F, 4.0F, 1.0F, 5.0F),
                PartPose.offsetAndRotation(-1.0F, -5.0F, 0.0F, 0.0F, -0.1570796F, 0.0F));
        root.addOrReplaceChild("rfleg2",
                CubeListBuilder.create().texOffs(7, 32)
                        .addBox(0.0F, 0.0F, 0.0F, 1.0F, 22.0F, 1.0F),
                PartPose.offsetAndRotation(-21.0F, -5.0F, -3.0F, 0.0F, 0.0F, 0.2792527F));
        root.addOrReplaceChild("rfleg1",
                CubeListBuilder.create().texOffs(42, 35)
                        .addBox(0.0F, 0.0F, 0.0F, 1.0F, 10.0F, 1.0F),
                PartPose.offsetAndRotation(-27.0F, 16.0F, -3.0F, 0.0F, 0.0F, 0.6283185F));
        root.addOrReplaceChild("rrleg3",
                CubeListBuilder.create().texOffs(64, 16)
                        .addBox(-20.0F, 0.0F, 0.0F, 20.0F, 1.0F, 1.0F),
                PartPose.offsetAndRotation(-1.0F, 0.0F, 8.0F, 0.0F, 0.1570796F, 0.0F));
        root.addOrReplaceChild("rrleg4",
                CubeListBuilder.create().texOffs(64, 44)
                        .addBox(-19.0F, 0.0F, -2.0F, 4.0F, 1.0F, 5.0F),
                PartPose.offsetAndRotation(-1.0F, 0.0F, 8.0F, 0.0F, 0.1570796F, 0.0F));
        root.addOrReplaceChild("rrleg2",
                CubeListBuilder.create().texOffs(21, 32)
                        .addBox(0.0F, 0.0F, 0.0F, 1.0F, 22.0F, 1.0F),
                PartPose.offsetAndRotation(-21.0F, 0.0F, 11.0F, 0.0F, 0.0F, 0.5410521F));
        root.addOrReplaceChild("rrleg1",
                CubeListBuilder.create().texOffs(49, 35)
                        .addBox(0.0F, 0.0F, 0.0F, 1.0F, 10.0F, 1.0F),
                PartPose.offsetAndRotation(-32.0F, 18.0F, 11.0F, 0.0F, 0.0F, 0.8726646F));
        return LayerDefinition.create(mesh, 256, 256);
    }

    @Override
    public ModelPart root() {
        return this.root;
    }

    @Override
    public void setupAnim(Mantis entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        animate(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
    }

    private void animate(Mantis entity, float f, float f1, float f2, float f3, float f4) {
        float newangle = 0.0F;

        newangle = Mth.cos(f2 * 0.9F * this.wingspeed) * (float) Math.PI * 0.25F;
        this.lfwing.zRot = -0.698F - newangle;
        this.rfwing.zRot = 0.698F + newangle;
        newangle = Mth.cos(f2 * 0.9F * this.wingspeed) * (float) Math.PI * 0.35F;
        this.lrwing.zRot = -0.349F + newangle;
        this.rrwing.zRot = 0.349F - newangle;
        float a1;
        if (entity.getAttacking() == 0) {
        newangle = Mth.cos(f2 * 0.051F * this.wingspeed) * (float) Math.PI * 0.013F;
        a1 = -0.2F;
        } else {
        newangle = Mth.cos(f2 * 0.51F * this.wingspeed) * (float) Math.PI * 0.25F;
        a1 = -0.698F;
        }
        this.larm1.xRot = a1 + newangle;
        this.larm2.z = (float)(this.larm1.z + 1.0F + Math.sin(this.larm1.xRot) * 22.0);
        this.larm2.y = (float)(this.larm1.y + Math.cos(this.larm1.xRot) * 22.0);
        this.larm2.xRot = -a1 - newangle;
        this.larm3.z = (float)(this.larm2.z + 1.0F - Math.sin(this.larm2.xRot) * 17.0);
        this.larm3.y = (float)(this.larm2.y - Math.cos(this.larm2.xRot) * 17.0);
        this.larm3.xRot = a1 + newangle;
        this.rarm1.xRot = a1 - newangle;
        this.rarm2.z = (float)(this.rarm1.z + 1.0F + Math.sin(this.rarm1.xRot) * 22.0);
        this.rarm2.y = (float)(this.rarm1.y + Math.cos(this.rarm1.xRot) * 22.0);
        this.rarm2.xRot = -a1 + newangle;
        this.rarm3.z = (float)(this.rarm2.z + 1.0F - Math.sin(this.rarm2.xRot) * 17.0);
        this.rarm3.y = (float)(this.rarm2.y - Math.cos(this.rarm2.xRot) * 17.0);
        this.rarm3.xRot = a1 - newangle;
    }
}
