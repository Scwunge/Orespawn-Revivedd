package danger.orespawn.client.model;

import danger.orespawn.entity.Pointysaurus;
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
 * Port of gold {@code ModelPointysaurus} (1.12 ModelBase) → 1.21 HierarchicalModel.
 */
@OnlyIn(Dist.CLIENT)
public class ModelPointysaurus extends HierarchicalModel<Pointysaurus> {
    private final float wingspeed;
    private final ModelPart root;
    private final ModelPart lfleg;
    private final ModelPart rfleg;
    private final ModelPart lrleg;
    private final ModelPart rrleg;
    private final ModelPart body1;
    private final ModelPart head;
    private final ModelPart body2;
    private final ModelPart body3;
    private final ModelPart guard;
    private final ModelPart nose;
    private final ModelPart lhorn;
    private final ModelPart rhorn;
    private final ModelPart chorn;
    private final ModelPart tail;
    private final ModelPart bump1;
    private final ModelPart bump2;
    private final ModelPart bump3;
    private final ModelPart bump4;
    private final ModelPart bump5;
    private final ModelPart bump6;
    private final ModelPart bump7;
    private final ModelPart bump8;
    private final ModelPart bump9;
    private final ModelPart bump10;
    private final ModelPart bump11;
    private final ModelPart bump12;
    private final ModelPart bump13;
    private final ModelPart bump14;
    private final ModelPart bump15;
    private final ModelPart bump16;

    public ModelPointysaurus(ModelPart root) {
        this(root, 1.5F);
    }

    public ModelPointysaurus(ModelPart root, float wingspeed) {
        this.root = root;
        this.wingspeed = wingspeed;
        this.lfleg = root.getChild("lfleg");
        this.rfleg = root.getChild("rfleg");
        this.lrleg = root.getChild("lrleg");
        this.rrleg = root.getChild("rrleg");
        this.body1 = root.getChild("body1");
        this.head = root.getChild("head");
        this.body2 = root.getChild("body2");
        this.body3 = root.getChild("body3");
        this.guard = root.getChild("guard");
        this.nose = root.getChild("nose");
        this.lhorn = root.getChild("lhorn");
        this.rhorn = root.getChild("rhorn");
        this.chorn = root.getChild("chorn");
        this.tail = root.getChild("tail");
        this.bump1 = root.getChild("bump1");
        this.bump2 = root.getChild("bump2");
        this.bump3 = root.getChild("bump3");
        this.bump4 = root.getChild("bump4");
        this.bump5 = root.getChild("bump5");
        this.bump6 = root.getChild("bump6");
        this.bump7 = root.getChild("bump7");
        this.bump8 = root.getChild("bump8");
        this.bump9 = root.getChild("bump9");
        this.bump10 = root.getChild("bump10");
        this.bump11 = root.getChild("bump11");
        this.bump12 = root.getChild("bump12");
        this.bump13 = root.getChild("bump13");
        this.bump14 = root.getChild("bump14");
        this.bump15 = root.getChild("bump15");
        this.bump16 = root.getChild("bump16");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        root.addOrReplaceChild("lfleg",
                CubeListBuilder.create().texOffs(102, 66)
                        .addBox(-3.0F, 0.0F, -3.0F, 6.0F, 8.0F, 6.0F),
                PartPose.offset(9.0F, 16.0F, -8.0F));
        root.addOrReplaceChild("rfleg",
                CubeListBuilder.create().texOffs(102, 66)
                        .addBox(-3.0F, 0.0F, -3.0F, 6.0F, 8.0F, 6.0F),
                PartPose.offset(-9.0F, 16.0F, -8.0F));
        root.addOrReplaceChild("lrleg",
                CubeListBuilder.create().texOffs(0, 0)
                        .addBox(-4.0F, 0.0F, -4.0F, 8.0F, 8.0F, 8.0F),
                PartPose.offset(9.0F, 16.0F, 12.0F));
        root.addOrReplaceChild("rrleg",
                CubeListBuilder.create().texOffs(0, 0)
                        .addBox(-4.0F, 0.0F, -4.0F, 8.0F, 8.0F, 8.0F),
                PartPose.offset(-9.0F, 16.0F, 12.0F));
        root.addOrReplaceChild("body1",
                CubeListBuilder.create().texOffs(0, 87)
                        .addBox(-4.0F, 0.0F, 0.0F, 22.0F, 9.0F, 30.0F),
                PartPose.offset(-7.0F, 9.0F, -12.0F));
        root.addOrReplaceChild("head",
                CubeListBuilder.create().texOffs(70, 0)
                        .addBox(-6.0F, -10.0F, -12.0F, 12.0F, 10.0F, 12.0F),
                PartPose.offsetAndRotation(0.0F, 11.0F, -7.0F, -0.1919862F, 0.0F, 0.0F));
        root.addOrReplaceChild("body2",
                CubeListBuilder.create().texOffs(0, 63)
                        .addBox(-9.0F, 0.0F, 0.0F, 18.0F, 7.0F, 15.0F),
                PartPose.offset(0.0F, 2.0F, -9.0F));
        root.addOrReplaceChild("body3",
                CubeListBuilder.create().texOffs(0, 44)
                        .addBox(-8.0F, 0.0F, 0.0F, 16.0F, 6.0F, 11.0F),
                PartPose.offset(0.0F, 3.0F, 6.0F));
        root.addOrReplaceChild("guard",
                CubeListBuilder.create().texOffs(60, 34)
                        .addBox(-14.0F, -20.0F, -8.0F, 28.0F, 23.0F, 3.0F),
                PartPose.offsetAndRotation(0.0F, 11.0F, -7.0F, (float) (-Math.PI / 12), 0.0F, 0.0F));
        root.addOrReplaceChild("nose",
                CubeListBuilder.create().texOffs(39, 0)
                        .addBox(-5.0F, -9.0F, -15.0F, 10.0F, 6.0F, 5.0F),
                PartPose.offset(0.0F, 11.0F, -7.0F));
        root.addOrReplaceChild("lhorn",
                CubeListBuilder.create().texOffs(0, 18)
                        .addBox(8.0F, -16.0F, -29.0F, 2.0F, 2.0F, 23.0F),
                PartPose.offsetAndRotation(0.0F, 11.0F, -7.0F, -0.1570796F, -0.1396263F, 0.0F));
        root.addOrReplaceChild("rhorn",
                CubeListBuilder.create().texOffs(0, 18)
                        .addBox(-9.0F, -16.0F, -29.0F, 2.0F, 2.0F, 23.0F),
                PartPose.offsetAndRotation(0.0F, 11.0F, -7.0F, -0.1570796F, 0.1396263F, 0.0F));
        root.addOrReplaceChild("chorn",
                CubeListBuilder.create().texOffs(52, 13)
                        .addBox(-1.5F, -9.0F, -20.0F, 3.0F, 3.0F, 5.0F),
                PartPose.offset(0.0F, 11.0F, -7.0F));
        root.addOrReplaceChild("tail",
                CubeListBuilder.create().texOffs(68, 70)
                        .addBox(-3.0F, -3.0F, 0.0F, 6.0F, 6.0F, 9.0F),
                PartPose.offsetAndRotation(0.0F, 7.0F, 15.0F, 0.2792527F, 0.0F, 0.0F));
        root.addOrReplaceChild("bump1",
                CubeListBuilder.create().texOffs(57, 17)
                        .addBox(14.0F, -20.0F, -8.0F, 2.0F, 2.0F, 2.0F),
                PartPose.offsetAndRotation(0.0F, 11.0F, -7.0F, (float) (-Math.PI / 12), 0.0F, 0.0F));
        root.addOrReplaceChild("bump2",
                CubeListBuilder.create().texOffs(57, 17)
                        .addBox(14.0F, -15.0F, -8.0F, 2.0F, 2.0F, 2.0F),
                PartPose.offsetAndRotation(0.0F, 11.0F, -7.0F, (float) (-Math.PI / 12), 0.0F, 0.0F));
        root.addOrReplaceChild("bump3",
                CubeListBuilder.create().texOffs(57, 17)
                        .addBox(14.0F, -10.0F, -8.0F, 2.0F, 2.0F, 2.0F),
                PartPose.offsetAndRotation(0.0F, 11.0F, -7.0F, (float) (-Math.PI / 12), 0.0F, 0.0F));
        root.addOrReplaceChild("bump4",
                CubeListBuilder.create().texOffs(57, 17)
                        .addBox(14.0F, -5.0F, -8.0F, 2.0F, 2.0F, 2.0F),
                PartPose.offsetAndRotation(0.0F, 11.0F, -7.0F, (float) (-Math.PI / 12), 0.0F, 0.0F));
        root.addOrReplaceChild("bump5",
                CubeListBuilder.create().texOffs(57, 17)
                        .addBox(14.0F, 0.0F, -8.0F, 2.0F, 2.0F, 2.0F),
                PartPose.offsetAndRotation(0.0F, 11.0F, -7.0F, (float) (-Math.PI / 12), 0.0F, 0.0F));
        root.addOrReplaceChild("bump6",
                CubeListBuilder.create().texOffs(57, 17)
                        .addBox(-16.0F, -20.0F, -8.0F, 2.0F, 2.0F, 2.0F),
                PartPose.offsetAndRotation(0.0F, 11.0F, -7.0F, (float) (-Math.PI / 12), 0.0F, 0.0F));
        root.addOrReplaceChild("bump7",
                CubeListBuilder.create().texOffs(57, 17)
                        .addBox(-16.0F, -15.0F, -8.0F, 2.0F, 2.0F, 2.0F),
                PartPose.offsetAndRotation(0.0F, 11.0F, -7.0F, (float) (-Math.PI / 12), 0.0F, 0.0F));
        root.addOrReplaceChild("bump8",
                CubeListBuilder.create().texOffs(57, 17)
                        .addBox(-16.0F, -10.0F, -8.0F, 2.0F, 2.0F, 2.0F),
                PartPose.offsetAndRotation(0.0F, 11.0F, -7.0F, (float) (-Math.PI / 12), 0.0F, 0.0F));
        root.addOrReplaceChild("bump9",
                CubeListBuilder.create().texOffs(57, 17)
                        .addBox(-16.0F, -5.0F, -8.0F, 2.0F, 2.0F, 2.0F),
                PartPose.offsetAndRotation(0.0F, 11.0F, -7.0F, (float) (-Math.PI / 12), 0.0F, 0.0F));
        root.addOrReplaceChild("bump10",
                CubeListBuilder.create().texOffs(57, 17)
                        .addBox(-16.0F, 0.0F, -8.0F, 2.0F, 2.0F, 2.0F),
                PartPose.offsetAndRotation(0.0F, 11.0F, -7.0F, (float) (-Math.PI / 12), 0.0F, 0.0F));
        root.addOrReplaceChild("bump11",
                CubeListBuilder.create().texOffs(57, 17)
                        .addBox(12.0F, -22.0F, -8.0F, 2.0F, 2.0F, 2.0F),
                PartPose.offsetAndRotation(0.0F, 11.0F, -7.0F, (float) (-Math.PI / 12), 0.0F, 0.0F));
        root.addOrReplaceChild("bump12",
                CubeListBuilder.create().texOffs(57, 17)
                        .addBox(7.0F, -22.0F, -8.0F, 2.0F, 2.0F, 2.0F),
                PartPose.offsetAndRotation(0.0F, 11.0F, -7.0F, (float) (-Math.PI / 12), 0.0F, 0.0F));
        root.addOrReplaceChild("bump13",
                CubeListBuilder.create().texOffs(57, 17)
                        .addBox(2.0F, -22.0F, -8.0F, 2.0F, 2.0F, 2.0F),
                PartPose.offsetAndRotation(0.0F, 11.0F, -7.0F, (float) (-Math.PI / 12), 0.0F, 0.0F));
        root.addOrReplaceChild("bump14",
                CubeListBuilder.create().texOffs(57, 17)
                        .addBox(-4.0F, -22.0F, -8.0F, 2.0F, 2.0F, 2.0F),
                PartPose.offsetAndRotation(0.0F, 11.0F, -7.0F, (float) (-Math.PI / 12), 0.0F, 0.0F));
        root.addOrReplaceChild("bump15",
                CubeListBuilder.create().texOffs(57, 17)
                        .addBox(-9.0F, -22.0F, -8.0F, 2.0F, 2.0F, 2.0F),
                PartPose.offsetAndRotation(0.0F, 11.0F, -7.0F, (float) (-Math.PI / 12), 0.0F, 0.0F));
        root.addOrReplaceChild("bump16",
                CubeListBuilder.create().texOffs(57, 17)
                        .addBox(-14.0F, -22.0F, -8.0F, 2.0F, 2.0F, 2.0F),
                PartPose.offsetAndRotation(0.0F, 11.0F, -7.0F, (float) (-Math.PI / 12), 0.0F, 0.0F));
        return LayerDefinition.create(mesh, 128, 128);
    }

    @Override
    public ModelPart root() {
        return this.root;
    }

    @Override
    public void setupAnim(Pointysaurus entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        animate(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
    }

    private void animate(Pointysaurus entity, float f, float f1, float f2, float f3, float f4) {

        float newangle = 0.0F;
        if (f1 > 0.1) {
        newangle = Mth.cos(f2 * 0.4F * this.wingspeed) * (float) Math.PI * 0.25F * f1;
        } else {
        newangle = 0.0F;
        }
        this.lfleg.xRot = newangle;
        this.rrleg.xRot = newangle;
        this.rfleg.xRot = -newangle;
        this.lrleg.xRot = -newangle;
        this.head.yRot = (float)Math.toRadians(f3) * 0.45F;
        this.nose.yRot = this.head.yRot;
        this.chorn.yRot = this.head.yRot;
        this.lhorn.yRot = this.head.yRot - 0.14F;
        this.rhorn.yRot = this.head.yRot + 0.14F;
        this.guard.yRot = this.head.yRot;
        this.bump1.yRot = this.head.yRot;
        this.bump2.yRot = this.head.yRot;
        this.bump3.yRot = this.head.yRot;
        this.bump4.yRot = this.head.yRot;
        this.bump5.yRot = this.head.yRot;
        this.bump6.yRot = this.head.yRot;
        this.bump7.yRot = this.head.yRot;
        this.bump8.yRot = this.head.yRot;
        this.bump9.yRot = this.head.yRot;
        this.bump10.yRot = this.head.yRot;
        this.bump11.yRot = this.head.yRot;
        this.bump12.yRot = this.head.yRot;
        this.bump13.yRot = this.head.yRot;
        this.bump14.yRot = this.head.yRot;
        this.bump15.yRot = this.head.yRot;
        this.bump16.yRot = this.head.yRot;
        this.head.xRot = (float)Math.toRadians(f4) * 0.45F;
        this.nose.xRot = this.head.xRot;
        this.chorn.xRot = this.head.xRot;
        this.lhorn.xRot = this.head.xRot - 0.16F;
        this.rhorn.xRot = this.head.xRot - 0.16F;
        this.guard.xRot = this.head.xRot - 0.262F;
        this.bump1.xRot = this.guard.xRot;
        this.bump2.xRot = this.guard.xRot;
        this.bump3.xRot = this.guard.xRot;
        this.bump4.xRot = this.guard.xRot;
        this.bump5.xRot = this.guard.xRot;
        this.bump6.xRot = this.guard.xRot;
        this.bump7.xRot = this.guard.xRot;
        this.bump8.xRot = this.guard.xRot;
        this.bump9.xRot = this.guard.xRot;
        this.bump10.xRot = this.guard.xRot;
        this.bump11.xRot = this.guard.xRot;
        this.bump12.xRot = this.guard.xRot;
        this.bump13.xRot = this.guard.xRot;
        this.bump14.xRot = this.guard.xRot;
        this.bump15.xRot = this.guard.xRot;
        this.bump16.xRot = this.guard.xRot;
        if (entity.getAttacking() != 0) {
        newangle = Mth.cos(f2 * 0.4F * this.wingspeed) * (float) Math.PI * 0.25F;
        } else {
        newangle = Mth.cos(f2 * 0.3F * this.wingspeed) * (float) Math.PI * 0.05F;
        }
        this.tail.yRot = newangle;
        newangle = Mth.cos(f2 * 0.02F * this.wingspeed) * (float) Math.PI * 0.15F;
        this.tail.xRot = newangle + 0.28F;
    }
}
