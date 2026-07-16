package danger.orespawn.client.model;

import danger.orespawn.entity.Ant;
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
 * Port of gold {@code ModelAnt} (1.12 ModelBase) → 1.21 HierarchicalModel.
 */
@OnlyIn(Dist.CLIENT)
public class ModelAnt<T extends Ant> extends HierarchicalModel<T> {
    private final ModelPart root;
    private final ModelPart thorax;
    private final ModelPart thorax1;
    private final ModelPart thorax3;
    private final ModelPart abdomen;
    private final ModelPart abdomen1;
    private final ModelPart head;
    private final ModelPart jawsr;
    private final ModelPart jawsl;
    private final ModelPart llegtop1;
    private final ModelPart llegbot1;
    private final ModelPart llegtop2;
    private final ModelPart llegbot2;
    private final ModelPart llegtop3;
    private final ModelPart llegbot3;
    private final ModelPart rlegtop1;
    private final ModelPart rlegbot1;
    private final ModelPart rlegtop2;
    private final ModelPart rlegbot2;
    private final ModelPart rlegtop3;
    private final ModelPart rlegbot3;

    public ModelAnt(ModelPart root) {
        this.root = root;
        this.thorax = root.getChild("thorax");
        this.thorax1 = root.getChild("thorax1");
        this.thorax3 = root.getChild("thorax3");
        this.abdomen = root.getChild("abdomen");
        this.abdomen1 = root.getChild("abdomen1");
        this.head = root.getChild("head");
        this.jawsr = root.getChild("jawsr");
        this.jawsl = root.getChild("jawsl");
        this.llegtop1 = root.getChild("llegtop1");
        this.llegbot1 = root.getChild("llegbot1");
        this.llegtop2 = root.getChild("llegtop2");
        this.llegbot2 = root.getChild("llegbot2");
        this.llegtop3 = root.getChild("llegtop3");
        this.llegbot3 = root.getChild("llegbot3");
        this.rlegtop1 = root.getChild("rlegtop1");
        this.rlegbot1 = root.getChild("rlegbot1");
        this.rlegtop2 = root.getChild("rlegtop2");
        this.rlegbot2 = root.getChild("rlegbot2");
        this.rlegtop3 = root.getChild("rlegtop3");
        this.rlegbot3 = root.getChild("rlegbot3");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        root.addOrReplaceChild("thorax",
                CubeListBuilder.create().texOffs(22, 0)
                        .addBox(0.0F, 0.0F, 0.0F, 3.0F, 3.0F, 3.0F),
                PartPose.offset(0.0F, 17.0F, 0.0F));
        root.addOrReplaceChild("thorax1",
                CubeListBuilder.create().texOffs(18, 0)
                        .addBox(1.0F, 1.0F, -1.0F, 1.0F, 1.0F, 1.0F),
                PartPose.offset(0.0F, 17.0F, 0.0F));
        root.addOrReplaceChild("thorax3",
                CubeListBuilder.create().texOffs(34, 0)
                        .addBox(1.0F, 1.0F, 3.0F, 1.0F, 1.0F, 1.0F),
                PartPose.offset(0.0F, 17.0F, 0.0F));
        root.addOrReplaceChild("abdomen",
                CubeListBuilder.create().texOffs(38, 0)
                        .addBox(0.0F, 0.0F, 4.0F, 3.0F, 3.0F, 5.0F),
                PartPose.offset(0.0F, 17.0F, 0.0F));
        root.addOrReplaceChild("abdomen1",
                CubeListBuilder.create().texOffs(54, 0)
                        .addBox(1.0F, 1.0F, 9.0F, 1.0F, 1.0F, 1.0F),
                PartPose.offset(0.0F, 17.0F, 0.0F));
        root.addOrReplaceChild("head",
                CubeListBuilder.create().texOffs(6, 0)
                        .addBox(0.0F, -1.0F, -4.0F, 3.0F, 3.0F, 3.0F),
                PartPose.offset(0.0F, 17.0F, 0.0F));
        root.addOrReplaceChild("jawsr",
                CubeListBuilder.create().texOffs(0, 9)
                        .addBox(-1.0F, 0.0F, -6.0F, 1.0F, 1.0F, 3.0F),
                PartPose.offset(0.0F, 17.0F, 0.0F));
        root.addOrReplaceChild("jawsl",
                CubeListBuilder.create().texOffs(0, 14)
                        .addBox(3.0F, 0.0F, -6.0F, 1.0F, 1.0F, 3.0F),
                PartPose.offset(0.0F, 17.0F, 0.0F));
        root.addOrReplaceChild("llegtop1",
                CubeListBuilder.create().texOffs(15, 10)
                        .addBox(3.0F, 1.0F, 1.0F, 3.0F, 1.0F, 1.0F),
                PartPose.offsetAndRotation(0.0F, 17.0F, 0.0F, 0.0F, 0.0F, 0.3839724F));
        root.addOrReplaceChild("llegbot1",
                CubeListBuilder.create().texOffs(15, 19)
                        .addBox(5.0F, -3.0F, 1.0F, 3.0F, 1.0F, 1.0F),
                PartPose.offsetAndRotation(0.0F, 17.0F, 0.0F, 0.0F, 0.0F, 1.064651F));
        root.addOrReplaceChild("llegtop2",
                CubeListBuilder.create().texOffs(15, 13)
                        .addBox(3.0F, 1.0F, 2.0F, 3.0F, 1.0F, 1.0F),
                PartPose.offsetAndRotation(0.0F, 17.0F, 0.0F, 0.0F, -0.2094395F, 0.3839724F));
        root.addOrReplaceChild("llegbot2",
                CubeListBuilder.create().texOffs(15, 22)
                        .addBox(5.0F, -3.0F, 2.0F, 3.0F, 1.0F, 1.0F),
                PartPose.offsetAndRotation(0.0F, 17.0F, 0.0F, 0.0F, -0.2268928F, 1.064651F));
        root.addOrReplaceChild("llegtop3",
                CubeListBuilder.create().texOffs(15, 16)
                        .addBox(3.0F, 1.0F, 0.0F, 3.0F, 1.0F, 1.0F),
                PartPose.offsetAndRotation(0.0F, 17.0F, 0.0F, 0.0F, 0.3490659F, 0.3839724F));
        root.addOrReplaceChild("llegbot3",
                CubeListBuilder.create().texOffs(15, 25)
                        .addBox(5.0F, -3.0F, 0.0F, 3.0F, 1.0F, 1.0F),
                PartPose.offsetAndRotation(0.0F, 17.0F, 0.0F, 0.0F, 0.3490659F, 1.064651F));
        root.addOrReplaceChild("rlegtop1",
                CubeListBuilder.create().texOffs(25, 10)
                        .addBox(-4.0F, 2.0F, 1.0F, 3.0F, 1.0F, 1.0F),
                PartPose.offsetAndRotation(0.0F, 17.0F, 0.0F, 0.0F, 0.0F, -0.4712389F));
        root.addOrReplaceChild("rlegbot1",
                CubeListBuilder.create().texOffs(25, 19)
                        .addBox(-7.0F, 0.0F, 1.0F, 3.0F, 1.0F, 1.0F),
                PartPose.offsetAndRotation(0.0F, 17.0F, 0.0F, 0.0F, 0.0F, -0.9773844F));
        root.addOrReplaceChild("rlegtop2",
                CubeListBuilder.create().texOffs(25, 13)
                        .addBox(-4.0F, 2.0F, 0.0F, 3.0F, 1.0F, 1.0F),
                PartPose.offsetAndRotation(0.0F, 17.0F, 0.0F, 0.0F, -0.5934119F, -0.4712389F));
        root.addOrReplaceChild("rlegbot2",
                CubeListBuilder.create().texOffs(25, 22)
                        .addBox(-7.0F, 0.0F, 0.0F, 3.0F, 1.0F, 1.0F),
                PartPose.offsetAndRotation(0.0F, 17.0F, 0.0F, 0.0F, -0.5934119F, -0.9773844F));
        root.addOrReplaceChild("rlegtop3",
                CubeListBuilder.create().texOffs(25, 16)
                        .addBox(-4.0F, 2.0F, 2.0F, 3.0F, 1.0F, 1.0F),
                PartPose.offsetAndRotation(0.0F, 17.0F, 0.0F, 0.0F, 0.418879F, -0.4712389F));
        root.addOrReplaceChild("rlegbot3",
                CubeListBuilder.create().texOffs(25, 25)
                        .addBox(-7.0F, 0.0F, 2.0F, 3.0F, 1.0F, 1.0F),
                PartPose.offsetAndRotation(0.0F, 17.0F, 0.0F, 0.0F, 0.418879F, -0.9773844F));
        return LayerDefinition.create(mesh, 64, 32);
    }

    @Override
    public ModelPart root() {
        return this.root;
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        animate(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
    }

    private void animate(T entity, float f, float f1, float f2, float f3, float f4) {
        this.llegtop1.xRot = Mth.cos(f2 * 2.7F) * (float) Math.PI * 0.45F * f1;
        this.llegbot1.xRot = this.llegtop1.xRot;
        this.rlegtop2.xRot = this.llegtop1.xRot;
        this.rlegbot2.xRot = this.llegtop1.xRot;
        this.rlegtop3.xRot = this.llegtop1.xRot;
        this.rlegbot3.xRot = this.llegtop1.xRot;
        this.rlegtop1.xRot = -this.llegtop1.xRot;
        this.rlegbot1.xRot = -this.llegtop1.xRot;
        this.llegtop2.xRot = -this.llegtop1.xRot;
        this.llegbot2.xRot = -this.llegtop1.xRot;
        this.llegtop3.xRot = -this.llegtop1.xRot;
        this.llegbot3.xRot = -this.llegtop1.xRot;
        this.jawsl.yRot = Mth.cos(f2 * 0.4F) * (float) Math.PI * 0.05F;
        this.jawsr.yRot = -this.jawsl.yRot;
    }
}
