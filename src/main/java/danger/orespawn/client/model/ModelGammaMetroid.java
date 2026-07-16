package danger.orespawn.client.model;

import danger.orespawn.entity.GammaMetroid;
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
 * Port of gold {@code ModelGammaMetroid} (1.12 ModelBase) → 1.21 HierarchicalModel.
 */
@OnlyIn(Dist.CLIENT)
public class ModelGammaMetroid extends HierarchicalModel<GammaMetroid> {
    private final float wingspeed;
    private final ModelPart root;
    private final ModelPart Shell3;
    private final ModelPart Shell4;
    private final ModelPart Head;
    private final ModelPart BeakUpper;
    private final ModelPart BeakLower;
    private final ModelPart LeftTusk;
    private final ModelPart MiddleTusk;
    private final ModelPart RightTusk;
    private final ModelPart LeftFrontUpperLeg;
    private final ModelPart LeftFrontLowerLeg;
    private final ModelPart LeftRearUpperLeg;
    private final ModelPart LeftRearLowerLeg;
    private final ModelPart RightFrontUpperLeg;
    private final ModelPart RightFrontLowerLeg;
    private final ModelPart RightRearUpperLeg;
    private final ModelPart RightRearLowerLeg;
    private final ModelPart Core;
    private final ModelPart Bellyinside;
    private final ModelPart Bellyoutside;
    private final ModelPart Shell1;
    private final ModelPart Shell2;

    public ModelGammaMetroid(ModelPart root) {
        this(root, 1F);
    }

    public ModelGammaMetroid(ModelPart root, float wingspeed) {
        this.root = root;
        this.wingspeed = wingspeed;
        this.Shell3 = root.getChild("Shell3");
        this.Shell4 = root.getChild("Shell4");
        this.Head = root.getChild("Head");
        this.BeakUpper = root.getChild("BeakUpper");
        this.BeakLower = root.getChild("BeakLower");
        this.LeftTusk = root.getChild("LeftTusk");
        this.MiddleTusk = root.getChild("MiddleTusk");
        this.RightTusk = root.getChild("RightTusk");
        this.LeftFrontUpperLeg = root.getChild("LeftFrontUpperLeg");
        this.LeftFrontLowerLeg = root.getChild("LeftFrontLowerLeg");
        this.LeftRearUpperLeg = root.getChild("LeftRearUpperLeg");
        this.LeftRearLowerLeg = root.getChild("LeftRearLowerLeg");
        this.RightFrontUpperLeg = root.getChild("RightFrontUpperLeg");
        this.RightFrontLowerLeg = root.getChild("RightFrontLowerLeg");
        this.RightRearUpperLeg = root.getChild("RightRearUpperLeg");
        this.RightRearLowerLeg = root.getChild("RightRearLowerLeg");
        this.Core = root.getChild("Core");
        this.Bellyinside = root.getChild("Bellyinside");
        this.Bellyoutside = root.getChild("Bellyoutside");
        this.Shell1 = root.getChild("Shell1");
        this.Shell2 = root.getChild("Shell2");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        root.addOrReplaceChild("Shell3",
                CubeListBuilder.create().texOffs(128, 0)
                        .addBox(-6.0F, -6.0F, 0.0F, 12.0F, 12.0F, 7.0F),
                PartPose.offsetAndRotation(0.0F, 7.0F, 10.0F, -0.9599311F, 0.6283185F, (float) (Math.PI / 6)));
        root.addOrReplaceChild("Shell4",
                CubeListBuilder.create().texOffs(48, 34)
                        .addBox(0.0F, 0.0F, 0.0F, 6.0F, 6.0F, 8.0F),
                PartPose.offsetAndRotation(-3.0F, 9.0F, 13.0F, -0.2792527F, 0.0F, 0.0F));
        root.addOrReplaceChild("Head",
                CubeListBuilder.create().texOffs(48, 48)
                        .addBox(0.0F, 0.0F, 0.0F, 16.0F, 8.0F, 6.0F),
                PartPose.offset(-8.0F, -1.0F, -11.0F));
        root.addOrReplaceChild("BeakUpper",
                CubeListBuilder.create().texOffs(114, 44)
                        .addBox(-3.0F, 0.0F, -3.0F, 6.0F, 4.0F, 6.0F),
                PartPose.offsetAndRotation(0.0F, 5.0F, -11.0F, 0.1047198F, (float) (Math.PI / 4), 0.1047198F));
        root.addOrReplaceChild("BeakLower",
                CubeListBuilder.create().texOffs(120, 54)
                        .addBox(-1.5F, 0.0F, -1.5F, 3.0F, 6.0F, 3.0F),
                PartPose.offsetAndRotation(0.0F, 9.0F, -12.0F, 0.1396263F, (float) (Math.PI / 4), 0.1396263F));
        root.addOrReplaceChild("LeftTusk",
                CubeListBuilder.create().texOffs(76, 50)
                        .addBox(0.0F, 0.0F, -12.0F, 2.0F, 2.0F, 12.0F),
                PartPose.offsetAndRotation(5.0F, 6.0F, -10.0F, 0.1047198F, 0.0872665F, 0.0F));
        root.addOrReplaceChild("MiddleTusk",
                CubeListBuilder.create().texOffs(76, 50)
                        .addBox(-1.0F, 0.0F, -12.0F, 2.0F, 2.0F, 12.0F),
                PartPose.offsetAndRotation(0.0F, -2.0F, -10.0F, 0.122173F, 0.0F, 0.0F));
        root.addOrReplaceChild("RightTusk",
                CubeListBuilder.create().texOffs(76, 50)
                        .addBox(-2.0F, 0.0F, -12.0F, 2.0F, 2.0F, 12.0F),
                PartPose.offsetAndRotation(-5.0F, 6.0F, -10.0F, 0.1047198F, -0.0872665F, 0.0F));
        root.addOrReplaceChild("LeftFrontUpperLeg",
                CubeListBuilder.create().texOffs(64, 0)
                        .addBox(0.0F, 0.0F, -1.5F, 3.0F, 8.0F, 3.0F),
                PartPose.offsetAndRotation(8.0F, 8.0F, -2.0F, -0.1745329F, 0.0F, -0.6632251F));
        root.addOrReplaceChild("LeftFrontLowerLeg",
                CubeListBuilder.create().texOffs(48, 0)
                        .addBox(-1.0F, 0.0F, -1.0F, 2.0F, 11.0F, 2.0F),
                PartPose.offsetAndRotation(14.0F, 13.0F, -3.5F, (float) (-Math.PI / 12), 0.1396263F, 0.0F));
        root.addOrReplaceChild("LeftRearUpperLeg",
                CubeListBuilder.create().texOffs(64, 0)
                        .addBox(-1.0F, 0.0F, -1.5F, 3.0F, 8.0F, 3.0F),
                PartPose.offsetAndRotation(8.0F, 9.0F, 7.0F, 0.1745329F, 0.0F, -0.8203047F));
        root.addOrReplaceChild("LeftRearLowerLeg",
                CubeListBuilder.create().texOffs(48, 0)
                        .addBox(-1.0F, 0.0F, -1.0F, 2.0F, 11.0F, 2.0F),
                PartPose.offsetAndRotation(14.0F, 14.0F, 8.5F, 0.3141593F, -0.1570796F, -0.2792527F));
        root.addOrReplaceChild("RightFrontUpperLeg",
                CubeListBuilder.create().texOffs(64, 0)
                        .addBox(-3.0F, 0.0F, -1.5F, 3.0F, 8.0F, 3.0F),
                PartPose.offsetAndRotation(-8.0F, 8.0F, -2.0F, -0.1745329F, 0.0F, 0.6632251F));
        root.addOrReplaceChild("RightFrontLowerLeg",
                CubeListBuilder.create().texOffs(48, 0)
                        .addBox(-1.0F, 0.0F, -1.0F, 2.0F, 11.0F, 2.0F),
                PartPose.offsetAndRotation(-14.0F, 13.0F, -3.5F, (float) (-Math.PI / 12), -0.1396263F, 0.0F));
        root.addOrReplaceChild("RightRearUpperLeg",
                CubeListBuilder.create().texOffs(64, 0)
                        .addBox(-2.0F, 0.0F, -1.5F, 3.0F, 8.0F, 3.0F),
                PartPose.offsetAndRotation(-8.0F, 9.0F, 7.0F, 0.1745329F, 0.0F, 0.8203047F));
        root.addOrReplaceChild("RightRearLowerLeg",
                CubeListBuilder.create().texOffs(48, 0)
                        .addBox(-1.0F, 0.0F, -1.0F, 2.0F, 11.0F, 2.0F),
                PartPose.offsetAndRotation(-14.0F, 14.0F, 8.5F, 0.3141593F, 0.1570796F, 0.2792527F));
        root.addOrReplaceChild("Core",
                CubeListBuilder.create().texOffs(82, 33)
                        .addBox(-3.0F, 0.0F, -3.0F, 6.0F, 6.0F, 6.0F),
                PartPose.offsetAndRotation(0.0F, 8.0F, 3.0F, -0.122173F, 0.0F, 0.0F));
        root.addOrReplaceChild("Bellyinside",
                CubeListBuilder.create().texOffs(150, 3)
                        .addBox(-8.0F, -1.0F, -8.0F, 16.0F, 1.0F, 16.0F),
                PartPose.offsetAndRotation(0.0F, 8.0F, 2.0F, -0.122173F, 0.0F, 0.0F));
        root.addOrReplaceChild("Bellyoutside",
                CubeListBuilder.create().texOffs(0, 0)
                        .addBox(-8.0F, -6.0F, -8.0F, 16.0F, 14.0F, 16.0F),
                PartPose.offsetAndRotation(0.0F, 8.0F, 2.0F, -0.122173F, 0.0F, 0.0F));
        root.addOrReplaceChild("Shell1",
                CubeListBuilder.create().texOffs(64, 0)
                        .addBox(-10.0F, -10.0F, 2.0F, 19.0F, 19.0F, 12.0F),
                PartPose.offsetAndRotation(0.0F, 4.0F, -7.0F, 0.0F, 0.0F, (float) (Math.PI / 4)));
        root.addOrReplaceChild("Shell2",
                CubeListBuilder.create().texOffs(0, 30)
                        .addBox(-9.0F, -9.0F, 0.0F, 16.0F, 16.0F, 8.0F),
                PartPose.offsetAndRotation(0.0F, 4.5F, 5.0F, (float) (-Math.PI / 6), 0.3665191F, 0.715585F));
        return LayerDefinition.create(mesh, 256, 64);
    }

    @Override
    public ModelPart root() {
        return this.root;
    }

    @Override
    public void setupAnim(GammaMetroid entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        animate(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
    }

    private void animate(GammaMetroid entity, float f, float f1, float f2, float f3, float f4) {

        float newangle = 0.0F;
        newangle = Mth.cos(f2 * 0.81F * this.wingspeed) * (float) Math.PI * 0.08F;
        this.LeftTusk.xRot = newangle;
        newangle = Mth.cos(f2 * 0.87F * this.wingspeed) * (float) Math.PI * 0.08F;
        this.RightTusk.xRot = newangle;
        newangle = Mth.cos(f2 * 0.99F * this.wingspeed) * (float) Math.PI * 0.08F;
        this.MiddleTusk.xRot = newangle;
        newangle = Mth.cos(f2 * 1.11F * this.wingspeed) * (float) Math.PI * 0.08F;
        this.LeftTusk.yRot = newangle;
        newangle = Mth.cos(f2 * 1.17F * this.wingspeed) * (float) Math.PI * 0.08F;
        this.RightTusk.yRot = newangle;
        newangle = Mth.cos(f2 * 1.25F * this.wingspeed) * (float) Math.PI * 0.08F;
        this.MiddleTusk.yRot = newangle;
        float nextangle = 0.0F;
        float upangle = 0.0F;
        newangle = Mth.cos(f2 * 2.0F * this.wingspeed) * (float) Math.PI * 0.12F * f1;
        nextangle = Mth.cos((f2 + 0.1F) * 2.0F * this.wingspeed) * (float) Math.PI * 0.12F * f1;
        upangle = 0.0F;
        if (nextangle > newangle) {
        upangle = 0.47F * f1 - Math.abs(newangle);
        }
        this.doLeftFLeg(this.LeftFrontUpperLeg, this.LeftFrontLowerLeg, newangle, upangle);
        this.doRightFLeg(this.RightFrontUpperLeg, this.RightFrontLowerLeg, -newangle, upangle);
        this.doLeftRLeg(this.LeftRearUpperLeg, this.LeftRearLowerLeg, -newangle, upangle);
        this.doRightRLeg(this.RightRearUpperLeg, this.RightRearLowerLeg, newangle, upangle);
        newangle = Mth.cos(f2 * 0.4F * this.wingspeed) * (float) Math.PI * 0.05F;
        if (false /* sitting deferred */) {
        newangle = 0.0F;
        }
        this.Shell1.xRot = newangle / 4.0F;
        this.Shell1.yRot = -(newangle / 4.0F);
        this.Shell2.xRot = newangle - 0.49F;
        this.Shell2.yRot = -newangle + 0.33F;
        this.Shell3.xRot = newangle - 0.96F;
        this.Shell3.yRot = -newangle + 0.63F;
        this.Shell4.xRot = newangle - 0.28F;
        newangle = Mth.cos(f2 * 0.75F * this.wingspeed) * (float) Math.PI * 0.1F;
        newangle = Math.abs(newangle);
        this.BeakLower.xRot = newangle + 0.14F;
        this.BeakLower.zRot = newangle + 0.14F;
    }


    private void doLeftFLeg(ModelPart seg2, ModelPart seg3, float angle, float upangle) {
        
        seg2.xRot = angle - 0.17F;
        seg3.xRot = angle - 0.26F;
        seg3.z = (float)(seg2.z + Math.sin(seg2.xRot) * 7.0) - 0.5F;
        seg2.zRot = -upangle - 0.66F;
        seg3.zRot = -upangle;
        seg3.y = seg2.y + (float)(5.0 * Math.cos(seg2.xRot));
        seg3.x = (float)(seg2.x + Math.abs(Math.sin(seg2.zRot) * 7.0) + 1.0);
        
    }

    private void doLeftRLeg(ModelPart seg2, ModelPart seg3, float angle, float upangle) {
        
        seg2.xRot = angle + 0.17F;
        seg3.xRot = angle + 0.31F;
        seg3.z = (float)(seg2.z + Math.sin(seg2.xRot) * 7.0) - 0.5F;
        seg2.zRot = -upangle - 0.82F;
        seg3.zRot = -upangle;
        seg3.y = seg2.y + (float)(5.0 * Math.cos(seg2.xRot));
        seg3.x = (float)(seg2.x + Math.abs(Math.sin(seg2.zRot) * 7.0) + 1.5);
        
    }

    private void doRightFLeg(ModelPart seg2, ModelPart seg3, float angle, float upangle) {
        
        seg2.xRot = angle - 0.17F;
        seg3.xRot = angle - 0.26F;
        seg3.z = (float)(seg2.z + Math.sin(seg2.xRot) * 7.0) - 0.5F;
        seg2.zRot = -upangle + 0.34F;
        seg3.zRot = -upangle;
        seg3.y = seg2.y + (float)(5.0 * Math.cos(seg2.xRot));
        seg3.x = (float)(seg2.x - Math.abs(Math.sin(seg2.zRot) * 7.0) - 1.0);
        
    }

    private void doRightRLeg(ModelPart seg2, ModelPart seg3, float angle, float upangle) {
        
        seg2.xRot = angle + 0.17F;
        seg3.xRot = angle + 0.31F;
        seg3.z = (float)(seg2.z + Math.sin(seg2.xRot) * 7.0) - 0.5F;
        seg2.zRot = -upangle + 0.82F;
        seg3.zRot = -upangle;
        seg3.y = seg2.y + (float)(5.0 * Math.cos(seg2.xRot));
        seg3.x = (float)(seg2.x - Math.abs(Math.sin(seg2.zRot) * 7.0) - 1.5);
        
    }
}
