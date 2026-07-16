package danger.orespawn.client.model;

import danger.orespawn.entity.Beaver;
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
 * Port of gold {@code ModelBeaver} (1.12 ModelBase) → 1.21 HierarchicalModel.
 */
@OnlyIn(Dist.CLIENT)
public class ModelBeaver extends HierarchicalModel<Beaver> {
    private final float wingspeed;
    private final ModelPart root;
    private final ModelPart head;
    private final ModelPart nose;
    private final ModelPart teeth;
    private final ModelPart body;
    private final ModelPart tail;
    private final ModelPart rff;
    private final ModelPart lff;
    private final ModelPart rrf;
    private final ModelPart lrf;

    public ModelBeaver(ModelPart root) {
        this(root, 1F);
    }

    public ModelBeaver(ModelPart root, float wingspeed) {
        this.root = root;
        this.wingspeed = wingspeed;
        this.head = root.getChild("head");
        this.nose = root.getChild("nose");
        this.teeth = root.getChild("teeth");
        this.body = root.getChild("body");
        this.tail = root.getChild("tail");
        this.rff = root.getChild("rff");
        this.lff = root.getChild("lff");
        this.rrf = root.getChild("rrf");
        this.lrf = root.getChild("lrf");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        root.addOrReplaceChild("head",
                CubeListBuilder.create().texOffs(0, 3)
                        .addBox(0.0F, 0.0F, 0.0F, 6.0F, 5.0F, 5.0F),
                PartPose.offset(0.0F, 15.0F, -8.0F));
        root.addOrReplaceChild("nose",
                CubeListBuilder.create().texOffs(6, 0)
                        .addBox(0.0F, 0.0F, 0.0F, 2.0F, 1.0F, 1.0F),
                PartPose.offset(2.0F, 18.0F, -8.5F));
        root.addOrReplaceChild("teeth",
                CubeListBuilder.create().texOffs(0, 0)
                        .addBox(0.0F, 0.0F, 0.0F, 2.0F, 2.0F, 1.0F),
                PartPose.offset(2.0F, 19.0F, -8.2F));
        root.addOrReplaceChild("body",
                CubeListBuilder.create().texOffs(0, 13)
                        .addBox(0.0F, 0.0F, 0.0F, 8.0F, 8.0F, 10.0F),
                PartPose.offset(-1.0F, 14.0F, -3.0F));
        root.addOrReplaceChild("tail",
                CubeListBuilder.create().texOffs(22, 0)
                        .addBox(0.0F, -1.0F, 0.0F, 5.0F, 1.0F, 8.0F),
                PartPose.offset(0.5F, 21.0F, 7.0F));
        root.addOrReplaceChild("rff",
                CubeListBuilder.create().texOffs(22, 9)
                        .addBox(0.0F, 0.0F, 0.0F, 2.0F, 2.0F, 2.0F),
                PartPose.offset(-0.5F, 22.0F, -2.5F));
        root.addOrReplaceChild("lff",
                CubeListBuilder.create().texOffs(22, 9)
                        .addBox(0.0F, 0.0F, 0.0F, 2.0F, 2.0F, 2.0F),
                PartPose.offset(4.5F, 22.0F, -2.5F));
        root.addOrReplaceChild("rrf",
                CubeListBuilder.create().texOffs(22, 9)
                        .addBox(0.0F, 0.0F, 0.0F, 2.0F, 2.0F, 2.0F),
                PartPose.offset(-0.5F, 22.0F, 4.5F));
        root.addOrReplaceChild("lrf",
                CubeListBuilder.create().texOffs(22, 9)
                        .addBox(0.0F, 0.0F, 0.0F, 2.0F, 2.0F, 2.0F),
                PartPose.offset(4.5F, 22.0F, 4.5F));
        return LayerDefinition.create(mesh, 64, 32);
    }

    @Override
    public ModelPart root() {
        return this.root;
    }

    @Override
    public void setupAnim(Beaver entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        animate(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
    }

    private void animate(Beaver entity, float f, float f1, float f2, float f3, float f4) {
        float newangle = 0.0F;
        newangle = Mth.cos(f2 * 3.7F * this.wingspeed) * (float) Math.PI * 0.45F * f1;
        this.rff.xRot = newangle;
        this.lrf.xRot = newangle;
        this.lff.xRot = -newangle;
        this.rrf.xRot = -newangle;
        newangle = Mth.cos(f2 * 2.7F * this.wingspeed) * (float) Math.PI * 0.25F;
        this.teeth.xRot = newangle;
        newangle = Mth.cos(f2 * 0.5F * this.wingspeed) * (float) Math.PI * 0.05F;
        this.tail.xRot = newangle;
    }
}
