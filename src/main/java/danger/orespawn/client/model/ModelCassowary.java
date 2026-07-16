package danger.orespawn.client.model;

import danger.orespawn.entity.Cassowary;
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
 * Port of gold {@code ModelCassowary} (1.12 ModelBase) → 1.21 HierarchicalModel.
 */
@OnlyIn(Dist.CLIENT)
public class ModelCassowary extends HierarchicalModel<Cassowary> {
    private final float wingspeed;
    private final ModelPart root;
    private final ModelPart tail;
    private final ModelPart body;
    private final ModelPart neck1;
    private final ModelPart neck;
    private final ModelPart head;
    private final ModelPart beak;
    private final ModelPart leg1;
    private final ModelPart leg2;
    private final ModelPart crest;
    private final ModelPart foot1;
    private final ModelPart foot2;
    private final ModelPart gobbler;

    public ModelCassowary(ModelPart root) {
        this(root, 1F);
    }

    public ModelCassowary(ModelPart root, float wingspeed) {
        this.root = root;
        this.wingspeed = wingspeed;
        this.tail = root.getChild("tail");
        this.body = root.getChild("body");
        this.neck1 = root.getChild("neck1");
        this.neck = root.getChild("neck");
        this.head = root.getChild("head");
        this.beak = root.getChild("beak");
        this.leg1 = root.getChild("leg1");
        this.leg2 = root.getChild("leg2");
        this.crest = root.getChild("crest");
        this.foot1 = root.getChild("foot1");
        this.foot2 = root.getChild("foot2");
        this.gobbler = root.getChild("gobbler");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        root.addOrReplaceChild("tail",
                CubeListBuilder.create().texOffs(38, 16)
                        .addBox(-3.0F, 0.0F, 0.0F, 6.0F, 9.0F, 7.0F),
                PartPose.offsetAndRotation(0.0F, 8.0F, 1.0F, 0.8922867F, 0.0F, 0.0F));
        root.addOrReplaceChild("body",
                CubeListBuilder.create().texOffs(0, 13)
                        .addBox(-4.0F, 0.0F, 0.0F, 8.0F, 10.0F, 9.0F),
                PartPose.offsetAndRotation(0.0F, 5.0F, -3.0F, 0.3346075F, 0.0F, 0.0F));
        root.addOrReplaceChild("neck1",
                CubeListBuilder.create().texOffs(48, 0)
                        .addBox(-2.0F, 0.0F, 0.0F, 4.0F, 5.0F, 4.0F),
                PartPose.offsetAndRotation(0.0F, 4.0F, -1.0F, -1.189716F, 0.0F, 0.0F));
        root.addOrReplaceChild("neck",
                CubeListBuilder.create().texOffs(38, 0)
                        .addBox(-1.0F, 0.0F, 0.0F, 2.0F, 7.0F, 2.0F),
                PartPose.offsetAndRotation(0.0F, 8.0F, -3.0F, -2.806985F, 0.0F, 0.0F));
        root.addOrReplaceChild("head",
                CubeListBuilder.create().texOffs(24, 0)
                        .addBox(-1.0F, -2.0F, -3.0F, 2.0F, 2.0F, 4.0F),
                PartPose.offsetAndRotation(0.0F, 2.0F, -6.0F, 0.0371786F, 0.0F, 0.0F));
        root.addOrReplaceChild("beak",
                CubeListBuilder.create().texOffs(28, 7)
                        .addBox(-0.5F, 0.0F, 3.0F, 1.0F, 1.0F, 3.0F),
                PartPose.offsetAndRotation(0.0F, 2.0F, -6.0F, -3.104414F, 0.0F, 0.0F));
        root.addOrReplaceChild("leg1",
                CubeListBuilder.create().texOffs(0, 0)
                        .addBox(-0.5F, 0.0F, -1.0F, 1.0F, 11.0F, 2.0F),
                PartPose.offset(3.0F, 12.0F, 3.0F));
        root.addOrReplaceChild("leg2",
                CubeListBuilder.create().texOffs(0, 0)
                        .addBox(-0.5F, 0.0F, -1.0F, 1.0F, 11.0F, 2.0F),
                PartPose.offset(-3.0F, 12.0F, 3.0F));
        root.addOrReplaceChild("crest",
                CubeListBuilder.create().texOffs(10, 0)
                        .addBox(-0.5F, -4.0F, 1.0F, 1.0F, 4.0F, 5.0F),
                PartPose.offsetAndRotation(0.0F, 2.0F, -6.0F, 1.710216F, 0.0F, 0.0F));
        root.addOrReplaceChild("foot1",
                CubeListBuilder.create().texOffs(47, 10)
                        .addBox(-1.033333F, 11.0F, -2.0F, 2.0F, 1.0F, 3.0F),
                PartPose.offset(-3.0F, 12.0F, 3.0F));
        root.addOrReplaceChild("foot2",
                CubeListBuilder.create().texOffs(47, 10)
                        .addBox(-1.0F, 11.0F, -2.0F, 2.0F, 1.0F, 3.0F),
                PartPose.offset(3.0F, 12.0F, 3.0F));
        root.addOrReplaceChild("gobbler",
                CubeListBuilder.create().texOffs(38, 10)
                        .addBox(-0.5F, -1.0F, -2.5F, 1.0F, 5.0F, 1.0F),
                PartPose.offset(0.0F, 8.0F, -3.0F));
        return LayerDefinition.create(mesh, 64, 32);
    }

    @Override
    public ModelPart root() {
        return this.root;
    }

    @Override
    public void setupAnim(Cassowary entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        animate(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
    }

    private void animate(Cassowary entity, float f, float f1, float f2, float f3, float f4) {

        float newangle = 0.0F;
        float newangle2 = 0.0F;
        if (f1 > 0.1) {
        newangle = Mth.cos(f2 * 1.3F * this.wingspeed) * (float) Math.PI * 0.15F * f1;
        newangle2 = Mth.cos(f2 * 2.6F * this.wingspeed) * (float) Math.PI * 0.1F * f1;
        } else {
        newangle2 = 0.0F;
        newangle = 0.0F;
        }
        this.leg1.xRot = newangle;
        this.foot2.xRot = newangle;
        this.leg2.xRot = -newangle;
        this.foot1.xRot = -newangle;
        this.neck.xRot = -2.827F + newangle2;
        this.gobbler.xRot = newangle2;
        this.head.z = this.neck.z + Mth.sin(this.neck.xRot) * 7.0F;
        this.crest.z = this.neck.z + Mth.sin(this.neck.xRot) * 7.0F;
        this.beak.z = this.neck.z + Mth.sin(this.neck.xRot) * 7.0F;
        this.head.y = this.neck.y + Mth.cos(this.neck.xRot) * 7.0F;
        this.crest.y = this.neck.y + Mth.cos(this.neck.xRot) * 7.0F;
        this.beak.y = this.neck.y + Mth.cos(this.neck.xRot) * 7.0F;
    }
}
