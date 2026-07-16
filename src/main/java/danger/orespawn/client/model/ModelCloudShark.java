package danger.orespawn.client.model;

import danger.orespawn.entity.CloudShark;
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
 * Port of gold {@code ModelCloudShark} (1.7.10 ModelBase, tex 64×64) → 1.21 HierarchicalModel.
 * Cubes / pivots / UV 1:1. wingspeed default 1.0 from ClientProxy registration.
 */
@OnlyIn(Dist.CLIENT)
public class ModelCloudShark extends HierarchicalModel<CloudShark> {
    private final float wingspeed;
    private final ModelPart root;
    private final ModelPart body;
    private final ModelPart head;
    private final ModelPart jaw;
    private final ModelPart topfin;
    private final ModelPart bbody;
    private final ModelPart fins;
    private final ModelPart leftfin;
    private final ModelPart rightfin;

    public ModelCloudShark(ModelPart root) {
        this(root, 1.0F);
    }

    public ModelCloudShark(ModelPart root, float wingspeed) {
        this.root = root;
        this.wingspeed = wingspeed;
        this.body = root.getChild("body");
        this.head = root.getChild("head");
        this.jaw = root.getChild("jaw");
        this.topfin = root.getChild("topfin");
        this.bbody = root.getChild("bbody");
        this.fins = root.getChild("fins");
        this.leftfin = root.getChild("leftfin");
        this.rightfin = root.getChild("rightfin");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        root.addOrReplaceChild(
                "body",
                CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, 0.0F, 0.0F, 6.0F, 8.0F, 15.0F),
                PartPose.offset(-4.0F, 11.0F, 0.0F));
        root.addOrReplaceChild(
                "head",
                CubeListBuilder.create().texOffs(0, 51).addBox(-2.5F, 0.0F, -8.0F, 5.0F, 5.0F, 8.0F),
                PartPose.offset(-1.0F, 11.0F, 0.0F));
        root.addOrReplaceChild(
                "jaw",
                CubeListBuilder.create().texOffs(42, 0).addBox(-2.5F, 0.0F, -6.0F, 5.0F, 2.0F, 6.0F),
                PartPose.offsetAndRotation(-1.0F, 15.0F, 0.0F, 0.5056291F, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "topfin",
                CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, 0.0F, 0.0F, 1.0F, 3.0F, 6.0F),
                PartPose.offsetAndRotation(-1.5F, 11.0F, 5.0F, 0.935765F, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "bbody",
                CubeListBuilder.create().texOffs(0, 9).addBox(-2.0F, 0.0F, 0.0F, 4.0F, 8.0F, 6.0F),
                PartPose.offset(-1.0F, 11.0F, 15.0F));
        root.addOrReplaceChild(
                "fins",
                CubeListBuilder.create().texOffs(0, 24).addBox(0.0F, 0.0F, 0.0F, 0.0F, 10.0F, 10.0F),
                PartPose.offsetAndRotation(-1.0F, 16.0F, 16.0F, 0.9220296F, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "leftfin",
                CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, 0.0F, 0.0F, 0.0F, 3.0F, 7.0F),
                PartPose.offsetAndRotation(2.0F, 16.0F, 6.0F, -0.6108652F, 1.134464F, -0.6108652F));
        root.addOrReplaceChild(
                "rightfin",
                CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, 0.0F, 0.0F, 0.0F, 3.0F, 7.0F),
                PartPose.offsetAndRotation(-4.0F, 16.0F, 6.0F, -0.6283185F, -1.134464F, 0.6108652F));

        return LayerDefinition.create(mesh, 64, 64);
    }

    @Override
    public ModelPart root() {
        return this.root;
    }

    @Override
    public void setupAnim(
            CloudShark entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        float newangle = Mth.cos(ageInTicks * 0.7F * this.wingspeed) * (float) Math.PI * 0.15F;
        this.leftfin.yRot = 1.15F + newangle;
        newangle = Mth.cos(ageInTicks * 1.5F * this.wingspeed) * (float) Math.PI * 0.15F;
        this.rightfin.yRot = -0.9F + newangle;
        newangle = Mth.cos(ageInTicks * 1.5F * this.wingspeed) * (float) Math.PI * 0.25F;
        this.fins.yRot = newangle;
        newangle = Mth.cos(ageInTicks * 0.5F * this.wingspeed) * (float) Math.PI * 0.1F;
        this.jaw.xRot = 0.5F + newangle;
    }
}
