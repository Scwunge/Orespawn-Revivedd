package danger.orespawn.client.model;

import danger.orespawn.entity.GoldFish;
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
 * Port of gold {@code ModelGoldFish} (1.7.10 ModelBase, tex 64×64) → 1.21 HierarchicalModel.
 * Cubes / pivots / UV 1:1. wingspeed default 0.7 from ClientProxy registration.
 */
@OnlyIn(Dist.CLIENT)
public class ModelGoldFish extends HierarchicalModel<GoldFish> {
    private final float wingspeed;
    private final ModelPart root;
    private final ModelPart Body;
    private final ModelPart Head;
    private final ModelPart Dorsalfin;
    private final ModelPart Mouth;
    private final ModelPart Jaw;
    private final ModelPart Pectoralfin1;
    private final ModelPart Pectoralfin2;
    private final ModelPart Pectoralfin3;
    private final ModelPart Pectoralfin4;
    private final ModelPart Bottomfin;
    private final ModelPart Tail1;
    private final ModelPart Tail2;
    private final ModelPart Caudalfin1;
    private final ModelPart Caudalfin2;
    private final ModelPart Bottomfin1;
    private final ModelPart Bottomfin2;

    public ModelGoldFish(ModelPart root) {
        this(root, 0.7F);
    }

    public ModelGoldFish(ModelPart root, float wingspeed) {
        this.root = root;
        this.wingspeed = wingspeed;
        this.Body = root.getChild("Body");
        this.Head = root.getChild("Head");
        this.Dorsalfin = root.getChild("Dorsalfin");
        this.Mouth = root.getChild("Mouth");
        this.Jaw = root.getChild("Jaw");
        this.Pectoralfin1 = root.getChild("Pectoralfin1");
        this.Pectoralfin2 = root.getChild("Pectoralfin2");
        this.Pectoralfin3 = root.getChild("Pectoralfin3");
        this.Pectoralfin4 = root.getChild("Pectoralfin4");
        this.Bottomfin = root.getChild("Bottomfin");
        this.Tail1 = root.getChild("Tail1");
        this.Tail2 = root.getChild("Tail2");
        this.Caudalfin1 = root.getChild("Caudalfin1");
        this.Caudalfin2 = root.getChild("Caudalfin2");
        this.Bottomfin1 = root.getChild("Bottomfin1");
        this.Bottomfin2 = root.getChild("Bottomfin2");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        root.addOrReplaceChild(
                "Body",
                CubeListBuilder.create().texOffs(0, 15).addBox(-2.0F, -2.0F, 0.0F, 4.0F, 4.0F, 10.0F),
                PartPose.offset(0.0F, 14.0F, -5.0F));
        root.addOrReplaceChild(
                "Head",
                CubeListBuilder.create().texOffs(0, 30).addBox(-1.5F, -2.0F, -3.0F, 3.0F, 4.0F, 3.0F),
                PartPose.offset(0.0F, 14.0F, -5.0F));
        root.addOrReplaceChild(
                "Dorsalfin",
                CubeListBuilder.create().texOffs(29, 0).addBox(0.0F, -6.0F, 0.0F, 0.0F, 4.0F, 10.0F),
                PartPose.offset(0.0F, 14.0F, -5.0F));
        root.addOrReplaceChild(
                "Mouth",
                CubeListBuilder.create().texOffs(0, 38).addBox(-1.5F, 0.6F, -3.5F, 3.0F, 3.0F, 3.0F),
                PartPose.offsetAndRotation(0.0F, 14.0F, -5.0F, (float) (-Math.PI / 4), 0.0F, 0.0F));
        root.addOrReplaceChild(
                "Jaw",
                CubeListBuilder.create().texOffs(13, 30).addBox(-1.0F, 0.0F, -3.0F, 3.0F, 1.0F, 3.0F),
                PartPose.offsetAndRotation(-0.5F, 15.6F, -7.4F, -0.2284419F, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "Pectoralfin1",
                CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, -1.5F, 0.0F, 0.0F, 3.0F, 5.0F),
                PartPose.offsetAndRotation(-2.0F, 14.0F, -3.0F, -0.2974289F, -0.3346075F, 0.0F));
        root.addOrReplaceChild(
                "Pectoralfin2",
                CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, -1.5F, 0.0F, 0.0F, 3.0F, 5.0F),
                PartPose.offsetAndRotation(2.0F, 14.0F, -3.0F, -0.2974216F, 0.3346145F, 0.0F));
        root.addOrReplaceChild(
                "Pectoralfin3",
                CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, -1.5F, 0.0F, 0.0F, 3.0F, 5.0F),
                PartPose.offsetAndRotation(-2.0F, 14.0F, 1.0F, -0.2974289F, -0.3346075F, 0.0F));
        root.addOrReplaceChild(
                "Pectoralfin4",
                CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, -1.5F, 0.0F, 0.0F, 3.0F, 5.0F),
                PartPose.offsetAndRotation(2.0F, 14.0F, 1.0F, -0.2974289F, 0.3346145F, 0.0F));
        root.addOrReplaceChild(
                "Bottomfin",
                CubeListBuilder.create().texOffs(20, 8).addBox(0.0F, 2.0F, 6.0F, 0.0F, 3.0F, 4.0F),
                PartPose.offset(0.0F, 14.0F, -5.0F));
        root.addOrReplaceChild(
                "Tail1",
                CubeListBuilder.create().texOffs(29, 15).addBox(-1.5F, -2.0F, 0.0F, 3.0F, 4.0F, 6.0F),
                PartPose.offset(0.0F, 14.0F, 5.0F));
        root.addOrReplaceChild(
                "Tail2",
                CubeListBuilder.create().texOffs(0, 8).addBox(-1.0F, -1.5F, 6.0F, 2.0F, 3.0F, 4.0F),
                PartPose.offset(0.0F, 14.0F, 5.0F));
        root.addOrReplaceChild(
                "Caudalfin1",
                CubeListBuilder.create().texOffs(13, 35).addBox(-0.5F, 5.5F, 6.0F, 1.0F, 3.0F, 4.0F),
                PartPose.offsetAndRotation(0.0F, 14.0F, 5.0F, 0.8179294F, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "Caudalfin2",
                CubeListBuilder.create().texOffs(15, 35).addBox(-0.5F, 5.5F, 6.0F, 1.0F, 4.0F, 3.0F),
                PartPose.offsetAndRotation(0.0F, 14.0F, 5.0F, 0.8179294F, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "Bottomfin1",
                CubeListBuilder.create().texOffs(20, 0).addBox(-1.0F, 2.0F, 1.0F, 0.0F, 5.0F, 2.0F),
                PartPose.offsetAndRotation(0.0F, 14.0F, -5.0F, 0.2974289F, 0.0F, 0.3346145F));
        root.addOrReplaceChild(
                "Bottomfin2",
                CubeListBuilder.create().texOffs(20, 0).addBox(1.0F, 2.0F, 1.0F, 0.0F, 5.0F, 2.0F),
                PartPose.offsetAndRotation(0.0F, 14.0F, -5.0F, 0.2974289F, 0.0F, -0.3346075F));

        return LayerDefinition.create(mesh, 64, 64);
    }

    @Override
    public ModelPart root() {
        return this.root;
    }

    @Override
    public void setupAnim(
            GoldFish entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        float newangle = Mth.cos(ageInTicks * 1.3F * this.wingspeed) * (float) Math.PI * 0.15F;
        this.Pectoralfin1.yRot = 0.4F + newangle;
        newangle = Mth.cos(ageInTicks * 1.2F * this.wingspeed) * (float) Math.PI * 0.15F;
        this.Pectoralfin2.yRot = -0.4F + newangle;
        newangle = Mth.cos(ageInTicks * 1.1F * this.wingspeed) * (float) Math.PI * 0.15F;
        this.Pectoralfin3.yRot = 0.4F + newangle;
        newangle = Mth.cos(ageInTicks * 1.0F * this.wingspeed) * (float) Math.PI * 0.15F;
        this.Pectoralfin4.yRot = -0.4F + newangle;
        newangle = Mth.cos(ageInTicks * 1.7F * this.wingspeed) * (float) Math.PI * 0.25F;
        this.Bottomfin1.yRot = newangle;
        this.Bottomfin2.yRot = -newangle;
        newangle = Mth.cos(ageInTicks * 0.7F * this.wingspeed) * (float) Math.PI * 0.1F;
        this.Jaw.xRot = -0.25F + newangle;
    }
}
