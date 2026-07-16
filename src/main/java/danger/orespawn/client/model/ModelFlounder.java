package danger.orespawn.client.model;

import danger.orespawn.entity.Flounder;
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
 * Port of gold {@code ModelFlounder} (1.7.10 ModelBase, tex 64×32) → 1.21 HierarchicalModel.
 * Cubes / pivots / UV 1:1.
 */
@OnlyIn(Dist.CLIENT)
public class ModelFlounder extends HierarchicalModel<Flounder> {
    private final ModelPart root;
    private final ModelPart body;
    private final ModelPart head;
    private final ModelPart tail1;
    private final ModelPart tail2;
    private final ModelPart rfin;
    private final ModelPart lfin;

    public ModelFlounder(ModelPart root) {
        this.root = root;
        this.body = root.getChild("body");
        this.head = root.getChild("head");
        this.tail1 = root.getChild("tail1");
        this.tail2 = root.getChild("tail2");
        this.rfin = root.getChild("rfin");
        this.lfin = root.getChild("lfin");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        root.addOrReplaceChild(
                "body",
                CubeListBuilder.create().texOffs(0, 16).addBox(-4.0F, 0.0F, -5.0F, 8.0F, 1.0F, 12.0F),
                PartPose.offset(0.0F, 22.0F, 0.0F));
        root.addOrReplaceChild(
                "head",
                CubeListBuilder.create().texOffs(0, 5).addBox(-2.0F, 0.0F, 0.0F, 4.0F, 1.0F, 2.0F),
                PartPose.offset(0.0F, 22.0F, -7.0F));
        root.addOrReplaceChild(
                "tail1",
                CubeListBuilder.create().texOffs(30, 0).addBox(-2.0F, 0.0F, 0.0F, 4.0F, 1.0F, 2.0F),
                PartPose.offset(0.0F, 22.0F, 7.0F));
        root.addOrReplaceChild(
                "tail2",
                CubeListBuilder.create().texOffs(30, 4).addBox(-3.0F, 0.0F, 2.0F, 6.0F, 1.0F, 3.0F),
                PartPose.offset(0.0F, 22.0F, 7.0F));
        root.addOrReplaceChild(
                "rfin",
                CubeListBuilder.create().texOffs(12, 0).addBox(-3.0F, 0.0F, 0.0F, 3.0F, 1.0F, 2.0F),
                PartPose.offset(-4.0F, 22.0F, -2.0F));
        root.addOrReplaceChild(
                "lfin",
                CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, 0.0F, 0.0F, 3.0F, 1.0F, 2.0F),
                PartPose.offset(4.0F, 22.0F, -2.0F));

        return LayerDefinition.create(mesh, 64, 32);
    }

    @Override
    public ModelPart root() {
        return this.root;
    }

    @Override
    public void setupAnim(
            Flounder entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        float newangle;
        float newangle2;
        if (limbSwingAmount > 0.1F) {
            newangle = Mth.cos(ageInTicks * 1.3F) * (float) Math.PI * 0.25F * limbSwingAmount;
            newangle2 = Mth.cos(ageInTicks * 1.7F) * (float) Math.PI * 0.25F * limbSwingAmount;
        } else {
            newangle = 0.0F;
            newangle2 = 0.0F;
        }
        this.lfin.zRot = newangle;
        this.rfin.zRot = newangle2;

        if (limbSwingAmount > 0.1F) {
            newangle = Mth.cos(ageInTicks * 1.2F) * (float) Math.PI * 0.25F * limbSwingAmount;
        } else {
            newangle = Mth.cos(ageInTicks * 0.7F) * (float) Math.PI * 0.05F;
        }
        this.tail1.xRot = newangle;
        this.tail2.xRot = newangle;
    }
}
