package danger.orespawn.client.model;

import danger.orespawn.entity.Cricket;
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
 * Port of gold {@code ModelCricket} (1.7.10 ModelBase) → 1.21 HierarchicalModel.
 * Cubes/UVs 1:1. Wingspeed default 2.5F matches ClientProxy registration.
 */
@OnlyIn(Dist.CLIENT)
public class ModelCricket extends HierarchicalModel<Cricket> {
    private final float wingspeed;
    private final ModelPart root;
    private final ModelPart body;
    private final ModelPart head;
    private final ModelPart abdomen;
    private final ModelPart lfleg;
    private final ModelPart lrleg;
    private final ModelPart rfleg;
    private final ModelPart rrleg;
    private final ModelPart lleg1;
    private final ModelPart rleg1;
    private final ModelPart lleg2;
    private final ModelPart rleg2;

    public ModelCricket(ModelPart root) {
        this(root, 2.5F);
    }

    public ModelCricket(ModelPart root, float wingspeed) {
        this.root = root;
        this.wingspeed = wingspeed;
        this.body = root.getChild("body");
        this.head = root.getChild("head");
        this.abdomen = root.getChild("abdomen");
        this.lfleg = root.getChild("lfleg");
        this.lrleg = root.getChild("lrleg");
        this.rfleg = root.getChild("rfleg");
        this.rrleg = root.getChild("rrleg");
        this.lleg1 = root.getChild("lleg1");
        this.rleg1 = root.getChild("rleg1");
        this.lleg2 = root.getChild("lleg2");
        this.rleg2 = root.getChild("rleg2");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        root.addOrReplaceChild(
                "body",
                CubeListBuilder.create().texOffs(0, 25).addBox(-1.0F, -1.0F, -3.0F, 3.0F, 3.0F, 6.0F),
                PartPose.offset(0.0F, 21.0F, 0.0F));
        root.addOrReplaceChild(
                "head",
                CubeListBuilder.create().texOffs(0, 17).addBox(-1.0F, -2.0F, -1.0F, 3.0F, 4.0F, 3.0F),
                PartPose.offsetAndRotation(0.0F, 21.0F, -5.0F, -0.1745329F, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "abdomen",
                CubeListBuilder.create().texOffs(0, 36).addBox(-0.5F, -1.0F, 3.0F, 2.0F, 2.0F, 3.0F),
                PartPose.offset(0.0F, 21.0F, 0.0F));
        root.addOrReplaceChild(
                "lfleg",
                CubeListBuilder.create().texOffs(25, 0).addBox(2.0F, 0.0F, 0.0F, 5.0F, 1.0F, 1.0F),
                PartPose.offsetAndRotation(0.0F, 21.0F, -2.0F, 0.0F, 0.4712389F, 0.418879F));
        root.addOrReplaceChild(
                "lrleg",
                CubeListBuilder.create().texOffs(23, 4).addBox(1.0F, 0.0F, -2.0F, 6.0F, 1.0F, 1.0F),
                PartPose.offsetAndRotation(0.0F, 21.0F, 0.0F, 0.0F, -0.296706F, 0.418879F));
        root.addOrReplaceChild(
                "rfleg",
                CubeListBuilder.create().texOffs(25, 8).addBox(-7.0F, 0.0F, 0.0F, 5.0F, 1.0F, 1.0F),
                PartPose.offsetAndRotation(1.0F, 21.0F, -2.0F, 0.0F, -0.5410521F, -0.4363323F));
        root.addOrReplaceChild(
                "rrleg",
                CubeListBuilder.create().texOffs(25, 12).addBox(-7.0F, -1.0F, 0.0F, 5.0F, 1.0F, 1.0F),
                PartPose.offsetAndRotation(1.0F, 22.0F, -2.0F, 0.0F, 0.3839724F, -0.418879F));
        root.addOrReplaceChild(
                "lleg1",
                CubeListBuilder.create().texOffs(40, 0).addBox(-1.0F, -1.0F, 0.0F, 1.0F, 2.0F, 8.0F),
                PartPose.offsetAndRotation(2.0F, 22.0F, 0.0F, 0.5585054F, 0.4363323F, 0.0F));
        root.addOrReplaceChild(
                "rleg1",
                CubeListBuilder.create().texOffs(40, 11).addBox(0.0F, -1.0F, 0.0F, 1.0F, 2.0F, 8.0F),
                PartPose.offsetAndRotation(-1.0F, 22.0F, 0.0F, 0.5585054F, -0.4363323F, 0.0F));
        root.addOrReplaceChild(
                "lleg2",
                CubeListBuilder.create().texOffs(21, 23).addBox(-0.5F, -6.5F, 4.5F, 1.0F, 1.0F, 8.0F),
                PartPose.offsetAndRotation(2.0F, 22.0F, 0.0F, -0.3665191F, 0.3490659F, 0.0F));
        root.addOrReplaceChild(
                "rleg2",
                CubeListBuilder.create().texOffs(21, 34).addBox(-0.5F, -6.5F, 4.0F, 1.0F, 1.0F, 8.0F),
                PartPose.offsetAndRotation(-1.0F, 22.0F, 0.0F, -0.3665191F, -0.3490659F, 0.0F));

        return LayerDefinition.create(mesh, 64, 64);
    }

    @Override
    public ModelPart root() {
        return this.root;
    }

    @Override
    public void setupAnim(
            Cricket entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        animate(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
    }

    private void animate(Cricket entity, float f, float f1, float f2, float f3, float f4) {
        float newangle;
        if (f1 > 0.1F) {
            newangle = Mth.cos(f2 * this.wingspeed) * (float) Math.PI * 0.25F * f1;
        } else {
            newangle = 0.0F;
        }

        this.lfleg.yRot = 0.47F + newangle;
        this.rfleg.yRot = -0.54F + newangle;
        this.lrleg.yRot = -0.296F - newangle;
        this.rrleg.yRot = 0.384F - newangle;

        if (entity.getSinging() != 0) {
            newangle = Mth.cos(f2 * 3.0F * this.wingspeed) * (float) Math.PI * 0.25F;
            this.lleg1.yRot = -0.035F;
            this.lleg2.yRot = -0.105F;
            this.rleg1.yRot = 0.035F;
            this.rleg2.yRot = 0.105F;
        } else {
            newangle = 0.0F;
            this.lleg1.yRot = 0.436F;
            this.lleg2.yRot = 0.349F;
            this.rleg1.yRot = -0.436F;
            this.rleg2.yRot = -0.349F;
        }

        this.lleg1.xRot = newangle + 0.558F;
        this.lleg2.xRot = newangle - 0.366F;
        this.rleg1.xRot = -newangle + 0.558F;
        this.rleg2.xRot = -newangle - 0.366F;
    }
}
