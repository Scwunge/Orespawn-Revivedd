package danger.orespawn.client.model;

import danger.orespawn.entity.Mosquito;
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
 * Port of gold {@code ModelMosquito} (1.12 ModelBase) → 1.21 HierarchicalModel.
 */
@OnlyIn(Dist.CLIENT)
public class ModelMosquito extends HierarchicalModel<Mosquito> {
    private final ModelPart root;
    private final ModelPart body;
    private final ModelPart leftwing1;
    private final ModelPart rightwing1;
    private final ModelPart leftwing2;
    private final ModelPart rightwing2;

    public ModelMosquito(ModelPart root) {
        this.root = root;
        this.body = root.getChild("body");
        this.leftwing1 = root.getChild("leftwing1");
        this.rightwing1 = root.getChild("rightwing1");
        this.leftwing2 = root.getChild("leftwing2");
        this.rightwing2 = root.getChild("rightwing2");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        root.addOrReplaceChild("body",
                CubeListBuilder.create().texOffs(8, 18)
                        .addBox(0.0F, 0.0F, -2.0F, 1.0F, 1.0F, 8.0F),
                PartPose.offset(0.0F, 17.0F, 0.0F));
        root.addOrReplaceChild("leftwing1",
                CubeListBuilder.create().texOffs(16, 13)
                        .addBox(1.0F, 0.0F, -1.0F, 3.0F, 1.0F, 3.0F),
                PartPose.offset(1.0F, 17.0F, 0.0F));
        root.addOrReplaceChild("rightwing1",
                CubeListBuilder.create().texOffs(2, 13)
                        .addBox(-4.0F, 0.0F, -1.0F, 3.0F, 1.0F, 3.0F),
                PartPose.offset(0.0F, 17.0F, 0.0F));
        root.addOrReplaceChild("leftwing2",
                CubeListBuilder.create().texOffs(15, 8)
                        .addBox(0.0F, 0.0F, 0.0F, 5.0F, 1.0F, 1.0F),
                PartPose.offset(1.0F, 17.0F, 0.0F));
        root.addOrReplaceChild("rightwing2",
                CubeListBuilder.create().texOffs(2, 8)
                        .addBox(-5.0F, 0.0F, 0.0F, 5.0F, 1.0F, 1.0F),
                PartPose.offset(0.0F, 17.0F, 0.0F));
        return LayerDefinition.create(mesh, 32, 32);
    }

    @Override
    public ModelPart root() {
        return this.root;
    }

    @Override
    public void setupAnim(Mosquito entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        animate(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
    }

    private void animate(Mosquito entity, float f, float f1, float f2, float f3, float f4) {
        this.rightwing1.zRot = Mth.cos(f2 * 3.0F) * (float) Math.PI * 0.25F;
        this.rightwing2.zRot = this.rightwing1.zRot;
        this.leftwing1.zRot = -this.rightwing1.zRot;
        this.leftwing2.zRot = -this.rightwing1.zRot;
    }
}
