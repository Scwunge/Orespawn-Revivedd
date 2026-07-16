package danger.orespawn.client.model;

import danger.orespawn.entity.VelocityRaptor;
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
 * Port of gold {@code ModelVelocityRaptor} (1.12 ModelBase) → 1.21 HierarchicalModel.
 */
@OnlyIn(Dist.CLIENT)
public class ModelVelocityRaptor extends HierarchicalModel<VelocityRaptor> {
    private final float wingspeed;
    private final ModelPart root;
    private final ModelPart hf3;
    private final ModelPart hf4;
    private final ModelPart hf2;
    private final ModelPart hf1;
    private final ModelPart lff2;
    private final ModelPart lff1;
    private final ModelPart lff3;
    private final ModelPart rff2;
    private final ModelPart rff3;
    private final ModelPart rff1;
    private final ModelPart tf4;
    private final ModelPart tf1;
    private final ModelPart Shape1;
    private final ModelPart neck;
    private final ModelPart head1;
    private final ModelPart lf1;
    private final ModelPart lf2;
    private final ModelPart head2;
    private final ModelPart tail1;
    private final ModelPart tail2;
    private final ModelPart bl1;
    private final ModelPart br1;
    private final ModelPart bl2;
    private final ModelPart br2;
    private final ModelPart bl3;
    private final ModelPart br3;
    private final ModelPart rf1;
    private final ModelPart rf2;
    private final ModelPart tf2;
    private final ModelPart tf3;
    private final ModelPart bl4;
    private final ModelPart br4;
    private final ModelPart Hat1;
    private final ModelPart Hat2;

    public ModelVelocityRaptor(ModelPart root) {
        this(root, 1F);
    }

    public ModelVelocityRaptor(ModelPart root, float wingspeed) {
        this.root = root;
        this.wingspeed = wingspeed;
        this.hf3 = root.getChild("hf3");
        this.hf4 = root.getChild("hf4");
        this.hf2 = root.getChild("hf2");
        this.hf1 = root.getChild("hf1");
        this.lff2 = root.getChild("lff2");
        this.lff1 = root.getChild("lff1");
        this.lff3 = root.getChild("lff3");
        this.rff2 = root.getChild("rff2");
        this.rff3 = root.getChild("rff3");
        this.rff1 = root.getChild("rff1");
        this.tf4 = root.getChild("tf4");
        this.tf1 = root.getChild("tf1");
        this.Shape1 = root.getChild("Shape1");
        this.neck = root.getChild("neck");
        this.head1 = root.getChild("head1");
        this.lf1 = root.getChild("lf1");
        this.lf2 = root.getChild("lf2");
        this.head2 = root.getChild("head2");
        this.tail1 = root.getChild("tail1");
        this.tail2 = root.getChild("tail2");
        this.bl1 = root.getChild("bl1");
        this.br1 = root.getChild("br1");
        this.bl2 = root.getChild("bl2");
        this.br2 = root.getChild("br2");
        this.bl3 = root.getChild("bl3");
        this.br3 = root.getChild("br3");
        this.rf1 = root.getChild("rf1");
        this.rf2 = root.getChild("rf2");
        this.tf2 = root.getChild("tf2");
        this.tf3 = root.getChild("tf3");
        this.bl4 = root.getChild("bl4");
        this.br4 = root.getChild("br4");
        this.Hat1 = root.getChild("Hat1");
        this.Hat2 = root.getChild("Hat2");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        root.addOrReplaceChild("hf3",
                CubeListBuilder.create().texOffs(0, 0)
                        .addBox(0.0F, 0.0F, 0.0F, 0.0F, 1.0F, 3.0F),
                PartPose.offsetAndRotation(0.0F, 7.0F, -2.0F, 0.4537856F, 0.0F, 0.0F));
        root.addOrReplaceChild("hf4",
                CubeListBuilder.create().texOffs(0, 0)
                        .addBox(0.0F, -0.2F, 0.0F, 0.0F, 1.0F, 3.0F),
                PartPose.offsetAndRotation(0.0F, 8.0F, -1.5F, 0.2443461F, 0.0F, 0.0F));
        root.addOrReplaceChild("hf2",
                CubeListBuilder.create().texOffs(0, 0)
                        .addBox(0.0F, 0.0F, 0.0F, 0.0F, 1.0F, 3.0F),
                PartPose.offsetAndRotation(0.0F, 7.0F, -3.5F, 0.6632251F, 0.0F, 0.0F));
        root.addOrReplaceChild("hf1",
                CubeListBuilder.create().texOffs(0, 1)
                        .addBox(0.0F, 0.0F, 0.0F, 0.0F, 1.0F, 2.0F),
                PartPose.offsetAndRotation(0.0F, 7.0F, -4.5F, (float) (Math.PI * 3.0 / 10.0), 0.0F, 0.0F));
        root.addOrReplaceChild("lff2",
                CubeListBuilder.create().texOffs(0, 6)
                        .addBox(0.5F, 2.5F, 3.0F, 0.0F, 1.0F, 3.0F),
                PartPose.offsetAndRotation(2.0F, 14.0F, 1.0F, -0.4537856F, 0.0F, 0.0F));
        root.addOrReplaceChild("lff1",
                CubeListBuilder.create().texOffs(0, 6)
                        .addBox(0.5F, 2.0F, 2.0F, 0.0F, 1.0F, 3.0F),
                PartPose.offsetAndRotation(2.0F, 14.0F, 1.0F, -0.2792527F, 0.0F, 0.0F));
        root.addOrReplaceChild("lff3",
                CubeListBuilder.create().texOffs(0, 6)
                        .addBox(0.5F, 1.0F, 4.0F, 0.0F, 1.0F, 3.0F),
                PartPose.offsetAndRotation(2.0F, 14.0F, 1.0F, -1.047198F, 0.0F, 0.0F));
        root.addOrReplaceChild("rff2",
                CubeListBuilder.create().texOffs(0, 6)
                        .addBox(-0.5F, 2.5F, 3.0F, 0.0F, 1.0F, 3.0F),
                PartPose.offsetAndRotation(-2.0F, 14.0F, 1.0F, -0.4537856F, 0.0F, 0.0F));
        root.addOrReplaceChild("rff3",
                CubeListBuilder.create().texOffs(0, 6)
                        .addBox(-0.5F, 1.0F, 4.0F, 0.0F, 1.0F, 3.0F),
                PartPose.offsetAndRotation(-2.0F, 14.0F, 1.0F, -1.047198F, 0.0F, 0.0F));
        root.addOrReplaceChild("rff1",
                CubeListBuilder.create().texOffs(0, 6)
                        .addBox(-0.5F, 2.0F, 2.0F, 0.0F, 1.0F, 3.0F),
                PartPose.offsetAndRotation(-2.0F, 14.0F, 1.0F, -0.2792527F, 0.0F, 0.0F));
        root.addOrReplaceChild("tf4",
                CubeListBuilder.create().texOffs(0, 3)
                        .addBox(0.0F, 0.0F, 0.0F, 0.0F, 1.0F, 3.0F),
                PartPose.offsetAndRotation(0.0F, 11.0F, 25.0F, -0.5410521F, 0.0F, 0.0F));
        root.addOrReplaceChild("tf1",
                CubeListBuilder.create().texOffs(0, 3)
                        .addBox(0.0F, 0.0F, 0.0F, 0.0F, 1.0F, 3.0F),
                PartPose.offsetAndRotation(0.0F, 11.0F, 19.0F, -0.5410521F, 0.0F, 0.0F));
        root.addOrReplaceChild("Shape1",
                CubeListBuilder.create().texOffs(0, 0)
                        .addBox(-2.0F, 0.0F, 0.0F, 4.0F, 7.0F, 11.0F),
                PartPose.offset(0.0F, 10.0F, 0.0F));
        root.addOrReplaceChild("neck",
                CubeListBuilder.create().texOffs(0, 19)
                        .addBox(-1.0F, -7.0F, -2.0F, 2.0F, 8.0F, 3.0F),
                PartPose.offsetAndRotation(0.0F, 12.0F, 2.0F, 1.082104F, 0.0F, 0.0F));
        root.addOrReplaceChild("head1",
                CubeListBuilder.create().texOffs(0, 49)
                        .addBox(-2.0F, 0.0F, -7.0F, 3.0F, 4.0F, 7.0F),
                PartPose.offset(0.5F, 7.0F, -1.0F));
        root.addOrReplaceChild("lf1",
                CubeListBuilder.create().texOffs(0, 31)
                        .addBox(0.0F, 0.0F, 0.0F, 1.0F, 3.0F, 2.0F),
                PartPose.offsetAndRotation(2.0F, 14.0F, 1.0F, 0.2792527F, 0.0F, 0.0F));
        root.addOrReplaceChild("lf2",
                CubeListBuilder.create().texOffs(16, 19)
                        .addBox(0.0F, 1.0F, 2.0F, 1.0F, 4.0F, 1.0F),
                PartPose.offsetAndRotation(2.0F, 14.0F, 1.0F, -0.4363323F, 0.0F, 0.0F));
        root.addOrReplaceChild("head2",
                CubeListBuilder.create().texOffs(20, 0)
                        .addBox(-1.0F, 0.0F, -10.0F, 2.0F, 4.0F, 4.0F),
                PartPose.offset(0.0F, 7.0F, -1.0F));
        root.addOrReplaceChild("tail1",
                CubeListBuilder.create().texOffs(0, 38)
                        .addBox(-1.0F, 0.0F, 0.0F, 2.0F, 5.0F, 4.0F),
                PartPose.offset(0.0F, 10.0F, 11.0F));
        root.addOrReplaceChild("tail2",
                CubeListBuilder.create().texOffs(26, 11)
                        .addBox(0.0F, 0.0F, 0.0F, 1.0F, 2.0F, 10.0F),
                PartPose.offset(-0.5F, 10.0F, 15.0F));
        root.addOrReplaceChild("bl1",
                CubeListBuilder.create().texOffs(22, 24)
                        .addBox(-1.0F, 0.0F, 0.0F, 2.0F, 6.0F, 4.0F),
                PartPose.offset(2.0F, 13.0F, 6.0F));
        root.addOrReplaceChild("br1",
                CubeListBuilder.create().texOffs(36, 0)
                        .addBox(-1.0F, 0.0F, 0.0F, 2.0F, 6.0F, 4.0F),
                PartPose.offset(-2.0F, 13.0F, 6.0F));
        root.addOrReplaceChild("bl2",
                CubeListBuilder.create().texOffs(12, 26)
                        .addBox(-1.0F, 5.0F, -3.0F, 2.0F, 5.0F, 2.0F),
                PartPose.offsetAndRotation(2.0F, 13.0F, 6.0F, 0.4886922F, 0.0F, 0.0F));
        root.addOrReplaceChild("br2",
                CubeListBuilder.create().texOffs(13, 36)
                        .addBox(-1.0F, 5.0F, -3.0F, 2.0F, 5.0F, 2.0F),
                PartPose.offsetAndRotation(-2.0F, 13.0F, 6.0F, 0.4886922F, 0.0F, 0.0F));
        root.addOrReplaceChild("bl3",
                CubeListBuilder.create().texOffs(28, 39)
                        .addBox(-1.0F, 9.0F, -1.0F, 2.0F, 2.0F, 4.0F),
                PartPose.offset(2.0F, 13.0F, 6.0F));
        root.addOrReplaceChild("br3",
                CubeListBuilder.create().texOffs(18, 45)
                        .addBox(-1.0F, 9.0F, -1.0F, 2.0F, 2.0F, 4.0F),
                PartPose.offset(-2.0F, 13.0F, 6.0F));
        root.addOrReplaceChild("rf1",
                CubeListBuilder.create().texOffs(35, 31)
                        .addBox(-1.0F, 0.0F, 0.0F, 1.0F, 3.0F, 2.0F),
                PartPose.offsetAndRotation(-2.0F, 14.0F, 1.0F, 0.2792527F, 0.0F, 0.0F));
        root.addOrReplaceChild("rf2",
                CubeListBuilder.create().texOffs(11, 19)
                        .addBox(-1.0F, 1.0F, 2.0F, 1.0F, 4.0F, 1.0F),
                PartPose.offsetAndRotation(-2.0F, 14.0F, 1.0F, -0.4363323F, 0.0F, 0.0F));
        root.addOrReplaceChild("tf2",
                CubeListBuilder.create().texOffs(0, 3)
                        .addBox(0.0F, 0.0F, 0.0F, 0.0F, 1.0F, 3.0F),
                PartPose.offsetAndRotation(0.0F, 11.0F, 21.0F, -0.5410521F, 0.0F, 0.0F));
        root.addOrReplaceChild("tf3",
                CubeListBuilder.create().texOffs(0, 3)
                        .addBox(0.0F, 0.0F, 0.0F, 0.0F, 1.0F, 3.0F),
                PartPose.offsetAndRotation(0.0F, 11.0F, 23.0F, -0.5410521F, 0.0F, 0.0F));
        root.addOrReplaceChild("bl4",
                CubeListBuilder.create().texOffs(31, 10)
                        .addBox(-1.0F, 6.0F, -5.0F, 1.0F, 3.0F, 1.0F),
                PartPose.offsetAndRotation(2.0F, 13.0F, 6.0F, 0.6283185F, 0.0F, 0.0F));
        root.addOrReplaceChild("br4",
                CubeListBuilder.create().texOffs(31, 15)
                        .addBox(0.0F, 6.0F, -5.0F, 1.0F, 3.0F, 1.0F),
                PartPose.offsetAndRotation(-2.0F, 13.0F, 6.0F, 0.6283185F, 0.0F, 0.0F));
        root.addOrReplaceChild("Hat1",
                CubeListBuilder.create().texOffs(50, 0)
                        .addBox(0.0F, 0.0F, 0.0F, 4.0F, 1.0F, 5.0F),
                PartPose.offset(-2.0F, 6.0F, -6.0F));
        root.addOrReplaceChild("Hat2",
                CubeListBuilder.create().texOffs(50, 0)
                        .addBox(0.0F, 0.0F, 0.0F, 3.0F, 2.0F, 3.0F),
                PartPose.offset(-1.5F, 4.0F, -4.0F));
        return LayerDefinition.create(mesh, 128, 128);
    }

    @Override
    public ModelPart root() {
        return this.root;
    }

    @Override
    public void setupAnim(VelocityRaptor entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        animate(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
    }

    private void animate(VelocityRaptor entity, float f, float f1, float f2, float f3, float f4) {

        float hf = 0.0F;
        float newangle = 0.0F;
        if (f1 > 0.1) {
        newangle = Mth.cos(f2 * 1.3F * this.wingspeed) * (float) Math.PI * 0.25F * f1;
        } else {
        newangle = 0.0F;
        }
        this.bl1.xRot = newangle;
        this.bl2.xRot = newangle + 0.488F;
        this.bl3.xRot = newangle;
        this.bl4.xRot = newangle + 0.628F;
        this.br1.xRot = -newangle;
        this.br2.xRot = -newangle + 0.488F;
        this.br3.xRot = -newangle;
        this.br4.xRot = -newangle + 0.628F;
        hf = entity.getHealth() / entity.getMaxHealth();
        newangle = Mth.cos(f2 * 1.25F * this.wingspeed * hf) * (float) Math.PI * 0.1F * hf;
        this.hf1.yRot = newangle;
        this.hf2.yRot = -newangle;
        this.hf3.yRot = newangle;
        this.hf4.yRot = -newangle;
        newangle = Mth.cos(f2 * 0.3F) * (float) Math.PI * 0.05F;
        this.lf1.xRot = newangle + 0.279F;
        this.lf2.xRot = newangle - 0.436F;
        this.lff1.xRot = newangle - 0.279F;
        this.lff2.xRot = newangle - 0.453F;
        this.lff3.xRot = newangle - 1.047F;
        this.rf1.xRot = -newangle + 0.279F;
        this.rf2.xRot = -newangle - 0.436F;
        this.rff1.xRot = -newangle - 0.279F;
        this.rff2.xRot = -newangle - 0.453F;
        this.rff3.xRot = -newangle - 1.047F;
        newangle = Mth.cos(f2 * 1.3F * this.wingspeed) * (float) Math.PI * 0.1F;
        this.lff1.yRot = newangle;
        this.lff2.yRot = -newangle;
        this.lff3.yRot = newangle;
        this.rff1.yRot = -newangle;
        this.rff2.yRot = newangle;
        this.rff3.yRot = -newangle;
        if (false /* sitting deferred */) {
        newangle = 0.0F;
        } else {
        newangle = Mth.cos(f2 * 1.4F * this.wingspeed * hf) * (float) Math.PI * 0.25F * hf;
        }
        this.tf1.zRot = newangle;
        this.tf2.zRot = -newangle;
        this.tf3.zRot = newangle;
        this.tf4.zRot = -newangle;
    }
}
