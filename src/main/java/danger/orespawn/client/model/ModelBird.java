package danger.orespawn.client.model;

import danger.orespawn.entity.Bird;
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
 * Port of gold {@code ModelBird} (1.12 ModelBase) → 1.21 HierarchicalModel.
 */
@OnlyIn(Dist.CLIENT)
public class ModelBird extends HierarchicalModel<Bird> {
    private final float wingspeed;
    private final ModelPart root;
    private final ModelPart Body;
    private final ModelPart Head;
    private final ModelPart Beak;
    private final ModelPart LowerBeak;
    private final ModelPart feather2;
    private final ModelPart feather1;
    private final ModelPart feather3;
    private final ModelPart tailfeather1;
    private final ModelPart rwing1;
    private final ModelPart lwing1;
    private final ModelPart leg;
    private final ModelPart otherleg;
    private final ModelPart lwing2;
    private final ModelPart rwing2;
    private final ModelPart tailfeather2;
    private final ModelPart tailfeather3;

    public ModelBird(ModelPart root) {
        this(root, 0.6F);
    }

    public ModelBird(ModelPart root, float wingspeed) {
        this.root = root;
        this.wingspeed = wingspeed;
        this.Body = root.getChild("Body");
        this.Head = root.getChild("Head");
        this.Beak = root.getChild("Beak");
        this.LowerBeak = root.getChild("LowerBeak");
        this.feather2 = root.getChild("feather2");
        this.feather1 = root.getChild("feather1");
        this.feather3 = root.getChild("feather3");
        this.tailfeather1 = root.getChild("tailfeather1");
        this.rwing1 = root.getChild("rwing1");
        this.lwing1 = root.getChild("lwing1");
        this.leg = root.getChild("leg");
        this.otherleg = root.getChild("otherleg");
        this.lwing2 = root.getChild("lwing2");
        this.rwing2 = root.getChild("rwing2");
        this.tailfeather2 = root.getChild("tailfeather2");
        this.tailfeather3 = root.getChild("tailfeather3");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        root.addOrReplaceChild("Body",
                CubeListBuilder.create().texOffs(0, 0)
                        .addBox(0.0F, 0.0F, 0.0F, 5.0F, 3.0F, 6.0F),
                PartPose.offset(-1.0F, 18.0F, 0.0F));
        root.addOrReplaceChild("Head",
                CubeListBuilder.create().texOffs(22, 0)
                        .addBox(0.0F, 0.0F, 0.0F, 3.0F, 3.0F, 4.0F),
                PartPose.offset(0.0F, 16.0F, -3.0F));
        root.addOrReplaceChild("Beak",
                CubeListBuilder.create().texOffs(0, 21)
                        .addBox(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 3.0F),
                PartPose.offset(1.0F, 17.0F, -6.0F));
        root.addOrReplaceChild("LowerBeak",
                CubeListBuilder.create().texOffs(1, 17)
                        .addBox(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F),
                PartPose.offset(1.0F, 18.0F, -4.0F));
        root.addOrReplaceChild("feather2",
                CubeListBuilder.create().texOffs(15, 9)
                        .addBox(0.0F, -2.5F, -0.75F, 1.0F, 3.0F, 1.0F),
                PartPose.offsetAndRotation(1.0F, 16.0F, 0.0F, -0.6426736F, 0.0F, 0.0F));
        root.addOrReplaceChild("feather1",
                CubeListBuilder.create().texOffs(11, 9)
                        .addBox(0.0F, -2.5F, -0.5F, 1.0F, 3.0F, 1.0F),
                PartPose.offsetAndRotation(1.0F, 16.0F, -2.0F, -0.2230717F, 0.0F, 0.0F));
        root.addOrReplaceChild("feather3",
                CubeListBuilder.create().texOffs(19, 9)
                        .addBox(0.0F, -3.0F, 0.5F, 1.0F, 4.0F, 1.0F),
                PartPose.offsetAndRotation(1.0F, 16.0F, 1.0F, -1.276259F, 0.0F, 0.0F));
        root.addOrReplaceChild("tailfeather1",
                CubeListBuilder.create().texOffs(46, 15)
                        .addBox(0.0F, 0.0F, 0.0F, 3.0F, 2.0F, 3.0F),
                PartPose.offset(0.0F, 18.0F, 6.0F));
        root.addOrReplaceChild("rwing1",
                CubeListBuilder.create().texOffs(23, 9)
                        .addBox(0.0F, 0.0F, 0.0F, 1.0F, 4.0F, 4.0F),
                PartPose.offsetAndRotation(-1.0F, 18.0F, 1.0F, 0.0F, 0.0F, 1.595066F));
        root.addOrReplaceChild("lwing1",
                CubeListBuilder.create().texOffs(33, 9)
                        .addBox(-1.0F, 0.0F, 0.0F, 1.0F, 4.0F, 4.0F),
                PartPose.offsetAndRotation(4.0F, 18.0F, 1.0F, 0.0F, 0.0F, -1.561488F));
        root.addOrReplaceChild("leg",
                CubeListBuilder.create().texOffs(4, 12)
                        .addBox(0.0F, 0.0F, 0.0F, 1.0F, 3.0F, 1.0F),
                PartPose.offsetAndRotation(2.0F, 21.0F, 3.0F, 0.8726646F, 0.0F, 0.0F));
        root.addOrReplaceChild("otherleg",
                CubeListBuilder.create().texOffs(0, 12)
                        .addBox(0.0F, 0.0F, 0.0F, 1.0F, 3.0F, 1.0F),
                PartPose.offsetAndRotation(0.0F, 21.0F, 3.0F, 0.6108652F, 0.0F, 0.0F));
        root.addOrReplaceChild("lwing2",
                CubeListBuilder.create().texOffs(10, 14)
                        .addBox(4.0F, 0.0F, 0.0F, 3.0F, 1.0F, 3.0F),
                PartPose.offset(4.0F, 18.0F, 1.0F));
        root.addOrReplaceChild("rwing2",
                CubeListBuilder.create().texOffs(10, 19)
                        .addBox(-7.0F, 0.0F, 0.0F, 3.0F, 1.0F, 3.0F),
                PartPose.offset(-1.0F, 18.0F, 1.0F));
        root.addOrReplaceChild("tailfeather2",
                CubeListBuilder.create().texOffs(44, 20)
                        .addBox(-0.5F, 0.0F, 3.0F, 4.0F, 1.0F, 4.0F),
                PartPose.offset(0.0F, 18.0F, 6.0F));
        root.addOrReplaceChild("tailfeather3",
                CubeListBuilder.create().texOffs(36, 26)
                        .addBox(-1.0F, 0.0F, 7.0F, 5.0F, 1.0F, 4.0F),
                PartPose.offset(0.0F, 18.0F, 6.0F));
        return LayerDefinition.create(mesh, 64, 32);
    }

    @Override
    public ModelPart root() {
        return this.root;
    }

    @Override
    public void setupAnim(Bird entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        animate(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
    }

    
    private void animate(Bird entity, float f, float f1, float f2, float f3, float f4) {
        float newangle = Mth.cos(f2 * 1.5F * this.wingspeed) * (float) Math.PI * 0.35F;
        this.lwing1.zRot = -1.5F + newangle;
        this.lwing2.zRot = newangle;
        this.rwing1.zRot = 1.5F - newangle;
        this.rwing2.zRot = -newangle;
        newangle = Mth.cos(f2 * 0.3F * this.wingspeed) * (float) Math.PI * 0.1F;
        this.tailfeather1.xRot = newangle;
        this.tailfeather2.xRot = newangle;
        this.tailfeather3.xRot = newangle;
        newangle = Mth.cos(f2 * 1.1F * this.wingspeed) * (float) Math.PI * 0.08F;
        this.feather1.zRot = newangle;
        newangle = Mth.cos(f2 * 1.2F * this.wingspeed) * (float) Math.PI * 0.08F;
        this.feather2.zRot = newangle;
        newangle = Mth.cos(f2 * 1.3F * this.wingspeed) * (float) Math.PI * 0.08F;
        this.feather3.zRot = newangle;
    }
}

