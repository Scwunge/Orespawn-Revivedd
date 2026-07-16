package danger.orespawn.client.model;

import danger.orespawn.entity.Chipmunk;
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
 * Port of gold {@code ModelChipmunk} (1.7.10 ModelBase) → 1.21 HierarchicalModel.
 * Cubes/UVs 1:1. Hat parts present but never rendered (no cannon-fodder activation).
 */
@OnlyIn(Dist.CLIENT)
public class ModelChipmunk extends HierarchicalModel<Chipmunk> {
    private final float wingspeed;
    private final ModelPart root;
    private final ModelPart Cheek2;
    private final ModelPart Leg1;
    private final ModelPart Leg2;
    private final ModelPart Leg3;
    private final ModelPart Leg4;
    private final ModelPart Tail2;
    private final ModelPart Neck;
    private final ModelPart Head;
    private final ModelPart MouthUnder;
    private final ModelPart Cheek1;
    private final ModelPart Ear2;
    private final ModelPart Nose;
    private final ModelPart Ear1;
    private final ModelPart Body;
    private final ModelPart BodyTail;
    private final ModelPart Tail1;
    private final ModelPart Hat1;
    private final ModelPart Hat2;

    public ModelChipmunk(ModelPart root) {
        this(root, 1.0F);
    }

    public ModelChipmunk(ModelPart root, float wingspeed) {
        this.root = root;
        this.wingspeed = wingspeed;
        this.Cheek2 = root.getChild("Cheek2");
        this.Leg1 = root.getChild("Leg1");
        this.Leg2 = root.getChild("Leg2");
        this.Leg3 = root.getChild("Leg3");
        this.Leg4 = root.getChild("Leg4");
        this.Tail2 = root.getChild("Tail2");
        this.Neck = root.getChild("Neck");
        this.Head = root.getChild("Head");
        this.MouthUnder = root.getChild("MouthUnder");
        this.Cheek1 = root.getChild("Cheek1");
        this.Ear2 = root.getChild("Ear2");
        this.Nose = root.getChild("Nose");
        this.Ear1 = root.getChild("Ear1");
        this.Body = root.getChild("Body");
        this.BodyTail = root.getChild("BodyTail");
        this.Tail1 = root.getChild("Tail1");
        this.Hat1 = root.getChild("Hat1");
        this.Hat2 = root.getChild("Hat2");
        // hats only for activated cannon fodder in gold
        this.Hat1.visible = false;
        this.Hat2.visible = false;
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        root.addOrReplaceChild(
                "Cheek2",
                CubeListBuilder.create().texOffs(14, 0).addBox(0.5F, -1.5F, -3.5F, 2.0F, 2.0F, 2.0F),
                PartPose.offset(0.0F, 20.0F, -3.0F));
        root.addOrReplaceChild(
                "Leg1",
                CubeListBuilder.create().texOffs(22, 7).addBox(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F),
                PartPose.offset(-2.0F, 23.0F, -4.0F));
        root.addOrReplaceChild(
                "Leg2",
                CubeListBuilder.create().texOffs(22, 9).addBox(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F),
                PartPose.offset(1.0F, 23.0F, -4.0F));
        root.addOrReplaceChild(
                "Leg3",
                CubeListBuilder.create().texOffs(22, 11).addBox(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F),
                PartPose.offset(1.0F, 23.0F, 0.0F));
        root.addOrReplaceChild(
                "Leg4",
                CubeListBuilder.create().texOffs(22, 13).addBox(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F),
                PartPose.offset(-2.0F, 23.0F, 0.0F));
        root.addOrReplaceChild(
                "Tail2",
                CubeListBuilder.create().texOffs(28, 15).addBox(-0.5F, 1.0F, 2.5F, 3.0F, 3.0F, 4.0F),
                PartPose.offsetAndRotation(-1.0F, 20.0F, 1.0F, 0.7662421F, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "Neck",
                CubeListBuilder.create().texOffs(26, 9).addBox(0.0F, 0.0F, 0.0F, 3.0F, 2.0F, 4.0F),
                PartPose.offsetAndRotation(-1.5F, 22.0F, -5.0F, 1.570796F, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "Head",
                CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -3.0F, 0.0F, 4.0F, 4.0F, 3.0F),
                PartPose.offsetAndRotation(0.0F, 20.0F, -3.0F, 1.570796F, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "MouthUnder",
                CubeListBuilder.create().texOffs(20, 4).addBox(-1.0F, -1.9F, -3.8F, 2.0F, 2.0F, 1.0F),
                PartPose.offset(0.0F, 20.0F, -3.0F));
        root.addOrReplaceChild(
                "Cheek1",
                CubeListBuilder.create().texOffs(22, 0).addBox(-2.5F, -1.5F, -3.5F, 2.0F, 2.0F, 2.0F),
                PartPose.offset(0.0F, 20.0F, -3.0F));
        root.addOrReplaceChild(
                "Ear2",
                CubeListBuilder.create().texOffs(18, 11).addBox(1.0F, 0.0F, 3.0F, 1.0F, 1.0F, 1.0F),
                PartPose.offsetAndRotation(0.0F, 20.0F, -3.0F, 1.570796F, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "Nose",
                CubeListBuilder.create().texOffs(18, 7).addBox(-0.5F, -2.0F, -4.2F, 1.0F, 1.0F, 1.0F),
                PartPose.offset(0.0F, 20.0F, -3.0F));
        root.addOrReplaceChild(
                "Ear1",
                CubeListBuilder.create().texOffs(18, 9).addBox(-2.0F, 0.0F, 3.0F, 1.0F, 1.0F, 1.0F),
                PartPose.offsetAndRotation(0.0F, 20.0F, -3.0F, 1.570796F, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "Body",
                CubeListBuilder.create().texOffs(0, 7).addBox(0.0F, 0.0F, 0.0F, 4.0F, 3.0F, 5.0F),
                PartPose.offset(-2.0F, 20.0F, -4.0F));
        root.addOrReplaceChild(
                "BodyTail",
                CubeListBuilder.create().texOffs(0, 15).addBox(0.0F, 0.0F, 0.0F, 5.0F, 4.0F, 3.0F),
                PartPose.offset(-2.5F, 19.0F, -1.0F));
        root.addOrReplaceChild(
                "Tail1",
                CubeListBuilder.create().texOffs(16, 15).addBox(0.0F, 0.0F, 0.0F, 2.0F, 2.0F, 4.0F),
                PartPose.offsetAndRotation(-1.0F, 20.0F, 1.0F, 0.3064968F, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "Hat1",
                CubeListBuilder.create().texOffs(40, 0).addBox(-2.5F, -4.0F, -4.0F, 5.0F, 1.0F, 5.0F),
                PartPose.offset(0.0F, 20.0F, -3.0F));
        root.addOrReplaceChild(
                "Hat2",
                CubeListBuilder.create().texOffs(40, 0).addBox(-2.0F, -6.0F, -3.0F, 4.0F, 2.0F, 4.0F),
                PartPose.offset(0.0F, 20.0F, -3.0F));

        return LayerDefinition.create(mesh, 64, 32);
    }

    @Override
    public ModelPart root() {
        return this.root;
    }

    @Override
    public void setupAnim(
            Chipmunk entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        animate(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
    }

    private void animate(Chipmunk entity, float f, float f1, float f2, float f3, float f4) {
        float newangle;
        if (f1 > 0.1F) {
            newangle = Mth.cos(f2 * 2.3F * this.wingspeed) * (float) Math.PI * 0.25F * f1;
        } else {
            newangle = 0.0F;
        }

        this.Leg1.xRot = newangle;
        this.Leg3.xRot = newangle;
        this.Leg2.xRot = -newangle;
        this.Leg4.xRot = -newangle;

        float headY = (float) Math.toRadians(f3) * 0.45F;
        this.Head.yRot = headY;
        this.Nose.yRot = headY;
        this.Ear1.yRot = headY;
        this.Ear2.yRot = headY;
        this.MouthUnder.yRot = headY;
        this.Cheek1.yRot = headY;
        this.Cheek2.yRot = headY;
        this.Hat1.yRot = headY;
        this.Hat2.yRot = headY;

        // gold: if not sitting, wag tail — simple port never sits
        this.Tail1.xRot = 0.306F + Mth.cos(f2 * 0.25F) * (float) Math.PI * 0.06F;
        newangle = Mth.cos(f2 * 1.3F * this.wingspeed) * (float) Math.PI * 0.25F * f1;
        this.Tail1.xRot += newangle;
        this.Tail2.xRot = 0.306F + this.Tail1.xRot;
    }
}
