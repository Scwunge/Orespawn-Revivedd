package danger.orespawn.client.model;

import danger.orespawn.entity.Firefly;
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
 * Port of gold {@code ModelFirefly} (1.12 ModelBase) → 1.21 HierarchicalModel.
 */
@OnlyIn(Dist.CLIENT)
public class ModelFirefly extends HierarchicalModel<Firefly> {
    private final float wingspeed;
    private final ModelPart root;
    private final ModelPart body;
    private final ModelPart wing_left;
    private final ModelPart wing_right;
    private final ModelPart head;
    private final ModelPart mouth;
    private final ModelPart eye_left;
    private final ModelPart eye_right;
    private final ModelPart front_leg_left_;
    private final ModelPart front_leg_right;
    private final ModelPart back_leg_left;
    private final ModelPart back_leg_right;
    private final ModelPart TailLight;

    public ModelFirefly(ModelPart root) {
        this(root, 1.5F);
    }

    public ModelFirefly(ModelPart root, float wingspeed) {
        this.root = root;
        this.wingspeed = wingspeed;
        this.body = root.getChild("body");
        this.wing_left = root.getChild("wing_left");
        this.wing_right = root.getChild("wing_right");
        this.head = root.getChild("head");
        this.mouth = root.getChild("mouth");
        this.eye_left = root.getChild("eye_left");
        this.eye_right = root.getChild("eye_right");
        this.front_leg_left_ = root.getChild("front_leg_left_");
        this.front_leg_right = root.getChild("front_leg_right");
        this.back_leg_left = root.getChild("back_leg_left");
        this.back_leg_right = root.getChild("back_leg_right");
        this.TailLight = root.getChild("TailLight");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        root.addOrReplaceChild("body",
                CubeListBuilder.create().texOffs(38, 12)
                        .addBox(-3.0F, -3.0F, -3.0F, 5.0F, 5.0F, 5.0F),
                PartPose.offset(-1.0F, 9.0F, -1.0F));
        root.addOrReplaceChild("wing_left",
                CubeListBuilder.create().texOffs(46, 0)
                        .addBox(0.0F, -6.0F, 0.0F, 0.0F, 6.0F, 2.0F),
                PartPose.offsetAndRotation(1.0F, 6.0F, -2.0F, 0.0F, 0.0174533F, (float) (Math.PI * 2.0 / 9.0)));
        root.addOrReplaceChild("wing_right",
                CubeListBuilder.create().texOffs(53, 0)
                        .addBox(0.0F, -6.0F, 0.0F, 0.0F, 6.0F, 2.0F),
                PartPose.offsetAndRotation(-4.0F, 6.0F, -2.0F, 0.0F, 0.0F, (float) (-Math.PI * 2.0 / 9.0)));
        root.addOrReplaceChild("head",
                CubeListBuilder.create().texOffs(3, 14)
                        .addBox(0.0F, 0.0F, 0.0F, 3.0F, 3.0F, 3.0F),
                PartPose.offsetAndRotation(-3.0F, 7.0F, -7.0F, 0.2230717F, 0.0F, 0.0F));
        root.addOrReplaceChild("mouth",
                CubeListBuilder.create().texOffs(26, 15)
                        .addBox(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 3.0F),
                PartPose.offsetAndRotation(-2.0F, 9.0F, -8.0F, 0.2117115F, 0.0F, 0.0F));
        root.addOrReplaceChild("eye_left",
                CubeListBuilder.create().texOffs(18, 12)
                        .addBox(0.0F, 0.0F, 0.0F, 1.0F, 2.0F, 2.0F),
                PartPose.offsetAndRotation(-1.0F, 6.5F, -6.0F, 0.0174533F, 0.2602503F, -0.2230717F));
        root.addOrReplaceChild("eye_right",
                CubeListBuilder.create().texOffs(18, 18)
                        .addBox(1.0F, -0.6F, -0.6F, 1.0F, 2.0F, 2.0F),
                PartPose.offsetAndRotation(-4.0F, 6.5F, -6.0F, 0.0F, -0.2602503F, 0.2230717F));
        root.addOrReplaceChild("front_leg_left_",
                CubeListBuilder.create().texOffs(32, 0)
                        .addBox(0.0F, 0.0F, 0.0F, 1.0F, 5.0F, 1.0F),
                PartPose.offsetAndRotation(-1.0F, 10.0F, -3.0F, -0.2792527F, 0.0F, -0.2792527F));
        root.addOrReplaceChild("front_leg_right",
                CubeListBuilder.create().texOffs(22, 0)
                        .addBox(0.0F, 0.0F, 0.0F, 1.0F, 5.0F, 1.0F),
                PartPose.offsetAndRotation(-3.0F, 10.0F, -3.0F, -0.2792527F, 0.0F, 0.2792527F));
        root.addOrReplaceChild("back_leg_left",
                CubeListBuilder.create().texOffs(11, 0)
                        .addBox(0.0F, 0.0F, 0.0F, 1.0F, 5.0F, 1.0F),
                PartPose.offsetAndRotation(-1.0F, 10.0F, -1.0F, 0.2792527F, 0.0F, -0.2792527F));
        root.addOrReplaceChild("back_leg_right",
                CubeListBuilder.create().texOffs(2, 0)
                        .addBox(0.0F, 0.0F, 0.0F, 1.0F, 5.0F, 1.0F),
                PartPose.offsetAndRotation(-3.0F, 10.0F, -1.0F, 0.2792527F, 0.0F, 0.2792527F));
        root.addOrReplaceChild("TailLight",
                CubeListBuilder.create().texOffs(10, 27)
                        .addBox(0.0F, 0.0F, 0.0F, 3.0F, 3.0F, 4.0F),
                PartPose.offset(-3.0F, 6.0F, 1.0F));
        return LayerDefinition.create(mesh, 64, 128);
    }

    @Override
    public ModelPart root() {
        return this.root;
    }

    @Override
    public void setupAnim(Firefly entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        animate(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
    }

    private void animate(Firefly entity, float f, float f1, float f2, float f3, float f4) {

        float onoff = 0.0F;
        this.wing_left.zRot = 1.11F + Mth.cos(f2 * this.wingspeed) * (float) Math.PI * 0.35F;
        this.wing_right.zRot = -1.11F - Mth.cos(f2 * this.wingspeed) * (float) Math.PI * 0.35F;
        onoff = entity.getBlink();
        // OpenGlHelper glow deferred
    }
}
