package danger.orespawn.client.model;

import danger.orespawn.entity.StinkBug;
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
 * Port of gold {@code ModelStinkBug} (1.12 ModelBase) → 1.21 HierarchicalModel.
 */
@OnlyIn(Dist.CLIENT)
public class ModelStinkBug extends HierarchicalModel<StinkBug> {
    private final float wingspeed;
    private final ModelPart root;
    private final ModelPart f6;
    private final ModelPart b10;
    private final ModelPart l6;
    private final ModelPart l4;
    private final ModelPart f4;
    private final ModelPart l5;
    private final ModelPart f5;
    private final ModelPart l3;
    private final ModelPart l2;
    private final ModelPart l1;
    private final ModelPart f3;
    private final ModelPart f2;
    private final ModelPart f1;
    private final ModelPart jaw;
    private final ModelPart b9;
    private final ModelPart head;
    private final ModelPart b4;
    private final ModelPart h1;
    private final ModelPart h2;
    private final ModelPart body;
    private final ModelPart t21;
    private final ModelPart tail;
    private final ModelPart t22;
    private final ModelPart t20;
    private final ModelPart t19;
    private final ModelPart t6;
    private final ModelPart t11;
    private final ModelPart t9;
    private final ModelPart t4;
    private final ModelPart t2;
    private final ModelPart t7;
    private final ModelPart t12;
    private final ModelPart t10;
    private final ModelPart t8;
    private final ModelPart t5;
    private final ModelPart t3;
    private final ModelPart t1;
    private final ModelPart t18;
    private final ModelPart t16;
    private final ModelPart t14;
    private final ModelPart t13;
    private final ModelPart t15;
    private final ModelPart t17;
    private final ModelPart b1;
    private final ModelPart b2;
    private final ModelPart b3;
    private final ModelPart b8;
    private final ModelPart b7;
    private final ModelPart b6;
    private final ModelPart b5;

    public ModelStinkBug(ModelPart root) {
        this(root, 0.1F);
    }

    public ModelStinkBug(ModelPart root, float wingspeed) {
        this.root = root;
        this.wingspeed = wingspeed;
        this.f6 = root.getChild("f6");
        this.b10 = root.getChild("b10");
        this.l6 = root.getChild("l6");
        this.l4 = root.getChild("l4");
        this.f4 = root.getChild("f4");
        this.l5 = root.getChild("l5");
        this.f5 = root.getChild("f5");
        this.l3 = root.getChild("l3");
        this.l2 = root.getChild("l2");
        this.l1 = root.getChild("l1");
        this.f3 = root.getChild("f3");
        this.f2 = root.getChild("f2");
        this.f1 = root.getChild("f1");
        this.jaw = root.getChild("jaw");
        this.b9 = root.getChild("b9");
        this.head = root.getChild("head");
        this.b4 = root.getChild("b4");
        this.h1 = root.getChild("h1");
        this.h2 = root.getChild("h2");
        this.body = root.getChild("body");
        this.t21 = root.getChild("t21");
        this.tail = root.getChild("tail");
        this.t22 = root.getChild("t22");
        this.t20 = root.getChild("t20");
        this.t19 = root.getChild("t19");
        this.t6 = root.getChild("t6");
        this.t11 = root.getChild("t11");
        this.t9 = root.getChild("t9");
        this.t4 = root.getChild("t4");
        this.t2 = root.getChild("t2");
        this.t7 = root.getChild("t7");
        this.t12 = root.getChild("t12");
        this.t10 = root.getChild("t10");
        this.t8 = root.getChild("t8");
        this.t5 = root.getChild("t5");
        this.t3 = root.getChild("t3");
        this.t1 = root.getChild("t1");
        this.t18 = root.getChild("t18");
        this.t16 = root.getChild("t16");
        this.t14 = root.getChild("t14");
        this.t13 = root.getChild("t13");
        this.t15 = root.getChild("t15");
        this.t17 = root.getChild("t17");
        this.b1 = root.getChild("b1");
        this.b2 = root.getChild("b2");
        this.b3 = root.getChild("b3");
        this.b8 = root.getChild("b8");
        this.b7 = root.getChild("b7");
        this.b6 = root.getChild("b6");
        this.b5 = root.getChild("b5");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        root.addOrReplaceChild("f6",
                CubeListBuilder.create().texOffs(20, 16)
                        .addBox(-2.0F, 0.0F, -1.0F, 2.0F, 2.0F, 2.0F),
                PartPose.offset(-3.5F, 16.0F, 3.0F));
        root.addOrReplaceChild("b10",
                CubeListBuilder.create().texOffs(0, 2)
                        .addBox(-0.5F, -1.5F, -0.5F, 1.0F, 2.0F, 1.0F),
                PartPose.offsetAndRotation(0.0F, 11.0F, 1.0F, (float) (-Math.PI / 6), 0.0F, 0.0F));
        root.addOrReplaceChild("l6",
                CubeListBuilder.create().texOffs(20, 13)
                        .addBox(-2.0F, 0.0F, -1.0F, 2.0F, 1.0F, 2.0F),
                PartPose.offset(-3.0F, 15.0F, 3.0F));
        root.addOrReplaceChild("l4",
                CubeListBuilder.create().texOffs(20, 13)
                        .addBox(-2.0F, 0.0F, -1.0F, 2.0F, 1.0F, 2.0F),
                PartPose.offset(-3.0F, 15.0F, -3.0F));
        root.addOrReplaceChild("f4",
                CubeListBuilder.create().texOffs(20, 16)
                        .addBox(-2.0F, 0.0F, -1.0F, 2.0F, 2.0F, 2.0F),
                PartPose.offset(-3.5F, 16.0F, -3.0F));
        root.addOrReplaceChild("l5",
                CubeListBuilder.create().texOffs(20, 13)
                        .addBox(-2.0F, 0.0F, -1.0F, 2.0F, 1.0F, 2.0F),
                PartPose.offset(-3.0F, 15.0F, 0.0F));
        root.addOrReplaceChild("f5",
                CubeListBuilder.create().texOffs(20, 16)
                        .addBox(-2.0F, 0.0F, -1.0F, 2.0F, 2.0F, 2.0F),
                PartPose.offset(-3.5F, 16.0F, 0.0F));
        root.addOrReplaceChild("l3",
                CubeListBuilder.create().texOffs(20, 13)
                        .addBox(0.0F, 0.0F, -1.0F, 2.0F, 1.0F, 2.0F),
                PartPose.offset(3.0F, 15.0F, 3.0F));
        root.addOrReplaceChild("l2",
                CubeListBuilder.create().texOffs(20, 13)
                        .addBox(0.0F, 0.0F, -1.0F, 2.0F, 1.0F, 2.0F),
                PartPose.offset(3.0F, 15.0F, 0.0F));
        root.addOrReplaceChild("l1",
                CubeListBuilder.create().texOffs(20, 13)
                        .addBox(0.0F, 0.0F, -1.0F, 2.0F, 1.0F, 2.0F),
                PartPose.offset(3.0F, 15.0F, -3.0F));
        root.addOrReplaceChild("f3",
                CubeListBuilder.create().texOffs(20, 16)
                        .addBox(0.0F, 0.0F, -1.0F, 2.0F, 2.0F, 2.0F),
                PartPose.offset(3.5F, 16.0F, 3.0F));
        root.addOrReplaceChild("f2",
                CubeListBuilder.create().texOffs(20, 16)
                        .addBox(0.0F, 0.0F, -1.0F, 2.0F, 2.0F, 2.0F),
                PartPose.offset(3.5F, 16.0F, 0.0F));
        root.addOrReplaceChild("f1",
                CubeListBuilder.create().texOffs(20, 16)
                        .addBox(0.0F, 0.0F, -1.0F, 2.0F, 2.0F, 2.0F),
                PartPose.offset(3.5F, 16.0F, -3.0F));
        root.addOrReplaceChild("jaw",
                CubeListBuilder.create().texOffs(28, 8)
                        .addBox(-3.5F, 0.0F, -8.0F, 5.0F, 1.0F, 4.0F),
                PartPose.offsetAndRotation(1.0F, 15.0F, 0.0F, 0.122173F, 0.0F, 0.0F));
        root.addOrReplaceChild("b9",
                CubeListBuilder.create().texOffs(0, 2)
                        .addBox(-0.5F, -1.5F, -0.5F, 1.0F, 2.0F, 1.0F),
                PartPose.offsetAndRotation(0.0F, 11.0F, -1.0F, (float) (Math.PI / 6), 0.0F, 0.0F));
        root.addOrReplaceChild("head",
                CubeListBuilder.create().texOffs(28, 0)
                        .addBox(-3.5F, -3.5F, -8.0F, 5.0F, 4.0F, 4.0F),
                PartPose.offset(1.0F, 15.0F, 0.0F));
        root.addOrReplaceChild("b4",
                CubeListBuilder.create().texOffs(0, 0)
                        .addBox(1.0F, -0.5F, 2.5F, 1.0F, 1.0F, 1.0F),
                PartPose.offset(0.0F, 11.0F, 0.0F));
        root.addOrReplaceChild("h1",
                CubeListBuilder.create().texOffs(0, 2)
                        .addBox(-0.5F, -2.0F, -0.5F, 1.0F, 2.0F, 1.0F),
                PartPose.offsetAndRotation(-1.5F, 12.0F, -7.0F, (float) (Math.PI / 6), 0.3490659F, 0.0F));
        root.addOrReplaceChild("h2",
                CubeListBuilder.create().texOffs(0, 2)
                        .addBox(-0.5F, -2.0F, -0.5F, 1.0F, 2.0F, 1.0F),
                PartPose.offsetAndRotation(1.5F, 12.0F, -7.0F, (float) (Math.PI / 6), -0.3490659F, 0.0F));
        root.addOrReplaceChild("body",
                CubeListBuilder.create().texOffs(0, 0)
                        .addBox(-4.0F, -4.0F, -4.0F, 6.0F, 5.0F, 8.0F),
                PartPose.offset(1.0F, 15.0F, 0.0F));
        root.addOrReplaceChild("t21",
                CubeListBuilder.create().texOffs(0, 0)
                        .addBox(0.5F, 3.5F, 4.0F, 1.0F, 1.0F, 1.0F),
                PartPose.offsetAndRotation(0.0F, 11.5F, 4.0F, -0.3316126F, 0.0F, 0.0F));
        root.addOrReplaceChild("tail",
                CubeListBuilder.create().texOffs(0, 13)
                        .addBox(-2.0F, 0.0F, 0.0F, 4.0F, 4.0F, 6.0F),
                PartPose.offsetAndRotation(0.0F, 11.5F, 4.0F, -0.3316126F, 0.0F, 0.0F));
        root.addOrReplaceChild("t22",
                CubeListBuilder.create().texOffs(0, 0)
                        .addBox(-1.5F, 3.5F, 4.0F, 1.0F, 1.0F, 1.0F),
                PartPose.offsetAndRotation(0.0F, 11.5F, 4.0F, -0.3316126F, 0.0F, 0.0F));
        root.addOrReplaceChild("t20",
                CubeListBuilder.create().texOffs(0, 0)
                        .addBox(-1.5F, 3.5F, 2.0F, 1.0F, 1.0F, 1.0F),
                PartPose.offsetAndRotation(0.0F, 11.5F, 4.0F, -0.3316126F, 0.0F, 0.0F));
        root.addOrReplaceChild("t19",
                CubeListBuilder.create().texOffs(0, 0)
                        .addBox(0.5F, 3.5F, 2.0F, 1.0F, 1.0F, 1.0F),
                PartPose.offsetAndRotation(0.0F, 11.5F, 4.0F, -0.3316126F, 0.0F, 0.0F));
        root.addOrReplaceChild("t6",
                CubeListBuilder.create().texOffs(0, 0)
                        .addBox(1.5F, 2.5F, 4.0F, 1.0F, 1.0F, 1.0F),
                PartPose.offsetAndRotation(0.0F, 11.5F, 4.0F, -0.3316126F, 0.0F, 0.0F));
        root.addOrReplaceChild("t11",
                CubeListBuilder.create().texOffs(0, 0)
                        .addBox(0.5F, -0.5F, 4.0F, 1.0F, 1.0F, 1.0F),
                PartPose.offsetAndRotation(0.0F, 11.5F, 4.0F, -0.3316126F, 0.0F, 0.0F));
        root.addOrReplaceChild("t9",
                CubeListBuilder.create().texOffs(0, 0)
                        .addBox(0.5F, -0.5F, 2.0F, 1.0F, 1.0F, 1.0F),
                PartPose.offsetAndRotation(0.0F, 11.5F, 4.0F, -0.3316126F, 0.0F, 0.0F));
        root.addOrReplaceChild("t4",
                CubeListBuilder.create().texOffs(0, 0)
                        .addBox(1.5F, 2.5F, 2.0F, 1.0F, 1.0F, 1.0F),
                PartPose.offsetAndRotation(0.0F, 11.5F, 4.0F, -0.3316126F, 0.0F, 0.0F));
        root.addOrReplaceChild("t2",
                CubeListBuilder.create().texOffs(0, 0)
                        .addBox(1.5F, 2.5F, 0.0F, 1.0F, 1.0F, 1.0F),
                PartPose.offsetAndRotation(0.0F, 11.5F, 4.0F, -0.3316126F, 0.0F, 0.0F));
        root.addOrReplaceChild("t7",
                CubeListBuilder.create().texOffs(0, 0)
                        .addBox(0.5F, -0.5F, 0.0F, 1.0F, 1.0F, 1.0F),
                PartPose.offsetAndRotation(0.0F, 11.5F, 4.0F, -0.3316126F, 0.0F, 0.0F));
        root.addOrReplaceChild("t12",
                CubeListBuilder.create().texOffs(0, 0)
                        .addBox(-1.5F, -0.5F, 4.0F, 1.0F, 1.0F, 1.0F),
                PartPose.offsetAndRotation(0.0F, 11.5F, 4.0F, -0.3316126F, 0.0F, 0.0F));
        root.addOrReplaceChild("t10",
                CubeListBuilder.create().texOffs(0, 0)
                        .addBox(-1.5F, -0.5F, 2.0F, 1.0F, 1.0F, 1.0F),
                PartPose.offsetAndRotation(0.0F, 11.5F, 4.0F, -0.3316126F, 0.0F, 0.0F));
        root.addOrReplaceChild("t8",
                CubeListBuilder.create().texOffs(0, 0)
                        .addBox(-1.5F, -0.5F, 0.0F, 1.0F, 1.0F, 1.0F),
                PartPose.offsetAndRotation(0.0F, 11.5F, 4.0F, -0.3316126F, 0.0F, 0.0F));
        root.addOrReplaceChild("t5",
                CubeListBuilder.create().texOffs(0, 0)
                        .addBox(1.5F, 0.5F, 4.0F, 1.0F, 1.0F, 1.0F),
                PartPose.offsetAndRotation(0.0F, 11.5F, 4.0F, -0.3316126F, 0.0F, 0.0F));
        root.addOrReplaceChild("t3",
                CubeListBuilder.create().texOffs(0, 0)
                        .addBox(1.5F, 0.5F, 2.0F, 1.0F, 1.0F, 1.0F),
                PartPose.offsetAndRotation(0.0F, 11.5F, 4.0F, -0.3316126F, 0.0F, 0.0F));
        root.addOrReplaceChild("t1",
                CubeListBuilder.create().texOffs(0, 0)
                        .addBox(1.5F, 0.5F, 0.0F, 1.0F, 1.0F, 1.0F),
                PartPose.offsetAndRotation(0.0F, 11.5F, 4.0F, -0.3316126F, 0.0F, 0.0F));
        root.addOrReplaceChild("t18",
                CubeListBuilder.create().texOffs(0, 0)
                        .addBox(-2.5F, 2.5F, 4.0F, 1.0F, 1.0F, 1.0F),
                PartPose.offsetAndRotation(0.0F, 11.5F, 4.0F, -0.3316126F, 0.0F, 0.0F));
        root.addOrReplaceChild("t16",
                CubeListBuilder.create().texOffs(0, 0)
                        .addBox(-2.5F, 2.5F, 2.0F, 1.0F, 1.0F, 1.0F),
                PartPose.offsetAndRotation(0.0F, 11.5F, 4.0F, -0.3316126F, 0.0F, 0.0F));
        root.addOrReplaceChild("t14",
                CubeListBuilder.create().texOffs(0, 0)
                        .addBox(-2.5F, 2.5F, 0.0F, 1.0F, 1.0F, 1.0F),
                PartPose.offsetAndRotation(0.0F, 11.5F, 4.0F, -0.3316126F, 0.0F, 0.0F));
        root.addOrReplaceChild("t13",
                CubeListBuilder.create().texOffs(0, 0)
                        .addBox(-2.5F, 0.5F, 0.0F, 1.0F, 1.0F, 1.0F),
                PartPose.offsetAndRotation(0.0F, 11.5F, 4.0F, -0.3316126F, 0.0F, 0.0F));
        root.addOrReplaceChild("t15",
                CubeListBuilder.create().texOffs(0, 0)
                        .addBox(-2.5F, 0.5F, 2.0F, 1.0F, 1.0F, 1.0F),
                PartPose.offsetAndRotation(0.0F, 11.5F, 4.0F, -0.3316126F, 0.0F, 0.0F));
        root.addOrReplaceChild("t17",
                CubeListBuilder.create().texOffs(0, 0)
                        .addBox(-2.5F, 0.5F, 4.0F, 1.0F, 1.0F, 1.0F),
                PartPose.offsetAndRotation(0.0F, 11.5F, 4.0F, -0.3316126F, 0.0F, 0.0F));
        root.addOrReplaceChild("b1",
                CubeListBuilder.create().texOffs(0, 0)
                        .addBox(1.0F, -0.5F, -3.5F, 1.0F, 1.0F, 1.0F),
                PartPose.offset(0.0F, 11.0F, 0.0F));
        root.addOrReplaceChild("b2",
                CubeListBuilder.create().texOffs(0, 0)
                        .addBox(1.5F, -0.5F, -1.5F, 1.0F, 1.0F, 1.0F),
                PartPose.offset(0.0F, 11.0F, 0.0F));
        root.addOrReplaceChild("b3",
                CubeListBuilder.create().texOffs(0, 0)
                        .addBox(1.5F, -0.5F, 0.5F, 1.0F, 1.0F, 1.0F),
                PartPose.offset(0.0F, 11.0F, 0.0F));
        root.addOrReplaceChild("b8",
                CubeListBuilder.create().texOffs(0, 0)
                        .addBox(-2.0F, -0.5F, 2.5F, 1.0F, 1.0F, 1.0F),
                PartPose.offset(0.0F, 11.0F, 0.0F));
        root.addOrReplaceChild("b7",
                CubeListBuilder.create().texOffs(0, 0)
                        .addBox(-2.5F, -0.5F, 0.5F, 1.0F, 1.0F, 1.0F),
                PartPose.offset(0.0F, 11.0F, 0.0F));
        root.addOrReplaceChild("b6",
                CubeListBuilder.create().texOffs(0, 0)
                        .addBox(-2.5F, -0.5F, -1.5F, 1.0F, 1.0F, 1.0F),
                PartPose.offset(0.0F, 11.0F, 0.0F));
        root.addOrReplaceChild("b5",
                CubeListBuilder.create().texOffs(0, 0)
                        .addBox(-1.966667F, -0.5F, -3.5F, 1.0F, 1.0F, 1.0F),
                PartPose.offset(0.0F, 11.0F, 0.0F));
        return LayerDefinition.create(mesh, 64, 32);
    }

    @Override
    public ModelPart root() {
        return this.root;
    }

    @Override
    public void setupAnim(StinkBug entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        animate(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
    }

    private void animate(StinkBug entity, float f, float f1, float f2, float f3, float f4) {
        float newangle = 0.0F;
        newangle = Mth.sin(f2 * 3.1F * this.wingspeed) * (float) Math.PI * 0.3F * f1;
        this.f1.xRot = newangle;
        this.f3.xRot = newangle;
        this.f3.xRot = newangle;
        this.f2.xRot = -newangle;
        this.f4.xRot = -newangle;
        this.f6.xRot = -newangle;
        newangle = Mth.sin(f2 * 0.4F * this.wingspeed) * (float) Math.PI * 0.2F;
        this.b9.zRot = newangle;
        this.b10.zRot = -newangle;
        newangle = Mth.sin(f2 * 0.2F * this.wingspeed) * (float) Math.PI * 0.04F;
        this.jaw.xRot = 0.18F + newangle;
        this.h1.xRot = 0.52F + Mth.sin(f2 * 0.4F * this.wingspeed) * (float) Math.PI * 0.15F;
        this.h1.yRot = -0.3F + Mth.sin(f2 * 0.43F * this.wingspeed) * (float) Math.PI * 0.15F;
        this.h2.xRot = 0.52F + Mth.sin(f2 * 0.46F * this.wingspeed) * (float) Math.PI * 0.15F;
        this.h2.yRot = 0.3F + Mth.sin(f2 * 0.49F * this.wingspeed) * (float) Math.PI * 0.15F;
        this.tail.xRot = -0.2F + Mth.sin(f2 * 0.1F * this.wingspeed) * (float) Math.PI * 0.1F;
        this.t1.xRot = this.tail.xRot;
        this.t2.xRot = this.tail.xRot;
        this.t3.xRot = this.tail.xRot;
        this.t4.xRot = this.tail.xRot;
        this.t5.xRot = this.tail.xRot;
        this.t6.xRot = this.tail.xRot;
        this.t7.xRot = this.tail.xRot;
        this.t8.xRot = this.tail.xRot;
        this.t9.xRot = this.tail.xRot;
        this.t10.xRot = this.tail.xRot;
        this.t11.xRot = this.tail.xRot;
        this.t12.xRot = this.tail.xRot;
        this.t13.xRot = this.tail.xRot;
        this.t14.xRot = this.tail.xRot;
        this.t15.xRot = this.tail.xRot;
        this.t16.xRot = this.tail.xRot;
        this.t17.xRot = this.tail.xRot;
        this.t18.xRot = this.tail.xRot;
        this.t19.xRot = this.tail.xRot;
        this.t20.xRot = this.tail.xRot;
        this.t21.xRot = this.tail.xRot;
        this.t22.xRot = this.tail.xRot;
    }
}
