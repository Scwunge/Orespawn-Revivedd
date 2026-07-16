package danger.orespawn.client.model;

import danger.orespawn.entity.CreepingHorror;
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
 * Port of gold {@code ModelCreepingHorror} (1.7.10 ModelBase, tex 128×128) → 1.21 HierarchicalModel.
 * Cube sizes/UVs/offsets 1:1. Animation from gold {@code render()}.
 */
@OnlyIn(Dist.CLIENT)
public class ModelCreepingHorror extends HierarchicalModel<CreepingHorror> {
    private final ModelPart root;
    private final ModelPart body;
    private final ModelPart leg1;
    private final ModelPart leg1part2;
    private final ModelPart leg2;
    private final ModelPart leg2part2;
    private final ModelPart leg3;
    private final ModelPart leg3part2;
    private final ModelPart leg4;
    private final ModelPart leg4part2;
    private final ModelPart tailseg1;
    private final ModelPart tailseg2;
    private final ModelPart tailseg3;
    private final ModelPart pincer1;
    private final ModelPart pincer1part2;
    private final ModelPart pincer2;
    private final ModelPart pincer2part2;
    private final ModelPart spike1;
    private final ModelPart spike2;
    private final ModelPart spike3;
    private final ModelPart spike4;
    private final ModelPart spike5;
    private final ModelPart insides1;
    private final ModelPart insides2;
    private final ModelPart insides3;
    private final ModelPart insides4;
    private final ModelPart insides5;

    public ModelCreepingHorror(ModelPart root) {
        this.root = root;
        this.body = root.getChild("body");
        this.leg1 = root.getChild("leg1");
        this.leg1part2 = root.getChild("leg1part2");
        this.leg2 = root.getChild("leg2");
        this.leg2part2 = root.getChild("leg2part2");
        this.leg3 = root.getChild("leg3");
        this.leg3part2 = root.getChild("leg3part2");
        this.leg4 = root.getChild("leg4");
        this.leg4part2 = root.getChild("leg4part2");
        this.tailseg1 = root.getChild("tailseg1");
        this.tailseg2 = root.getChild("tailseg2");
        this.tailseg3 = root.getChild("tailseg3");
        this.pincer1 = root.getChild("pincer1");
        this.pincer1part2 = root.getChild("pincer1part2");
        this.pincer2 = root.getChild("pincer2");
        this.pincer2part2 = root.getChild("pincer2part2");
        this.spike1 = root.getChild("spike1");
        this.spike2 = root.getChild("spike2");
        this.spike3 = root.getChild("spike3");
        this.spike4 = root.getChild("spike4");
        this.spike5 = root.getChild("spike5");
        this.insides1 = root.getChild("insides1");
        this.insides2 = root.getChild("insides2");
        this.insides3 = root.getChild("insides3");
        this.insides4 = root.getChild("insides4");
        this.insides5 = root.getChild("insides5");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        root.addOrReplaceChild("body",
                CubeListBuilder.create().texOffs(0, 30)
                        .addBox(-4.0F, -5.0F, -4.0F, 8.0F, 8.0F, 8.0F),
                PartPose.offset(0.0F, 20.0F, 0.0F));
        root.addOrReplaceChild("leg1",
                CubeListBuilder.create().texOffs(65, 0)
                        .addBox(-1.0F, -1.0F, -1.0F, 16.0F, 2.0F, 2.0F),
                PartPose.offsetAndRotation(4.0F, 18.0F, -2.0F, 0.0F, 0.5759587F, 0.1919862F));
        root.addOrReplaceChild("leg1part2",
                CubeListBuilder.create().texOffs(37, 5)
                        .addBox(13.01F, -1.01F, -1.0F, 2.0F, 5.0F, 2.0F),
                PartPose.offsetAndRotation(4.0F, 18.0F, -2.0F, 0.0F, 0.5759587F, 0.1919862F));
        root.addOrReplaceChild("leg2",
                CubeListBuilder.create().texOffs(65, 0)
                        .addBox(-1.0F, -1.0F, -1.0F, 16.0F, 2.0F, 2.0F),
                PartPose.offsetAndRotation(4.0F, 18.0F, 2.0F, 0.0F, -0.5759587F, 0.1919862F));
        root.addOrReplaceChild("leg2part2",
                CubeListBuilder.create().texOffs(37, 5)
                        .addBox(13.01F, -1.01F, -1.0F, 2.0F, 5.0F, 2.0F),
                PartPose.offsetAndRotation(4.0F, 18.0F, 2.0F, 0.0F, -0.5759587F, 0.1919862F));
        root.addOrReplaceChild("leg3",
                CubeListBuilder.create().texOffs(28, 0)
                        .addBox(-15.0F, -1.0F, -1.0F, 16.0F, 2.0F, 2.0F),
                PartPose.offsetAndRotation(-4.0F, 18.0F, -2.0F, 0.0F, -0.5759587F, -0.1919862F));
        root.addOrReplaceChild("leg3part2",
                CubeListBuilder.create().texOffs(28, 5)
                        .addBox(-15.01F, -1.01F, -1.0F, 2.0F, 5.0F, 2.0F),
                PartPose.offsetAndRotation(-4.0F, 18.0F, -2.0F, 0.0F, -0.5759587F, -0.1919862F));
        root.addOrReplaceChild("leg4",
                CubeListBuilder.create().texOffs(28, 0)
                        .addBox(-15.0F, -1.0F, -1.0F, 16.0F, 2.0F, 2.0F),
                PartPose.offsetAndRotation(-4.0F, 18.0F, 2.0F, 0.0F, 0.5759587F, -0.1919862F));
        root.addOrReplaceChild("leg4part2",
                CubeListBuilder.create().texOffs(28, 5)
                        .addBox(-15.01F, -1.01F, -1.0F, 2.0F, 5.0F, 2.0F),
                PartPose.offsetAndRotation(-4.0F, 18.0F, 2.0F, 0.0F, 0.5759587F, -0.1919862F));
        root.addOrReplaceChild("tailseg1",
                CubeListBuilder.create().texOffs(0, 13)
                        .addBox(-2.0F, -1.0F, 0.0F, 4.0F, 2.0F, 7.0F),
                PartPose.offsetAndRotation(0.0F, 17.0F, 3.0F, -0.5576792F, 0.0F, 0.0F));
        root.addOrReplaceChild("tailseg2",
                CubeListBuilder.create().texOffs(0, 0)
                        .addBox(-1.0F, 3.0F, 7.0F, 2.0F, 1.0F, 11.0F),
                PartPose.offsetAndRotation(0.0F, 17.0F, 3.0F, -0.0349066F, 0.0F, 0.0F));
        root.addOrReplaceChild("tailseg3",
                CubeListBuilder.create().texOffs(0, 24)
                        .addBox(-1.5F, 1.0F, 6.0F, 3.0F, 2.0F, 2.0F),
                PartPose.offsetAndRotation(0.0F, 17.0F, 3.0F, -0.2230717F, 0.0F, 0.0F));
        root.addOrReplaceChild("pincer1",
                CubeListBuilder.create().texOffs(26, 30)
                        .addBox(-0.5F, -0.5F, -5.0F, 1.0F, 1.0F, 5.0F),
                PartPose.offsetAndRotation(-3.0F, 19.0F, -3.0F, 0.0F, -0.2230717F, 0.0F));
        root.addOrReplaceChild("pincer1part2",
                CubeListBuilder.create().texOffs(26, 30)
                        .addBox(-0.5F, -0.5F, -5.01F, 2.0F, 1.0F, 0.0F),
                PartPose.offsetAndRotation(-3.0F, 19.0F, -3.0F, 0.0F, -0.2230717F, 0.0F));
        root.addOrReplaceChild("pincer2",
                CubeListBuilder.create().texOffs(26, 30)
                        .addBox(-0.5F, -0.5F, -5.0F, 1.0F, 1.0F, 5.0F),
                PartPose.offsetAndRotation(3.0F, 19.0F, -3.0F, 0.0F, 0.2230705F, 0.0F));
        root.addOrReplaceChild("pincer2part2",
                CubeListBuilder.create().texOffs(26, 28)
                        .addBox(-1.5F, -0.5F, -5.01F, 2.0F, 1.0F, 0.0F),
                PartPose.offsetAndRotation(3.0F, 19.0F, -3.0F, 0.0F, 0.2230705F, 0.0F));
        root.addOrReplaceChild("spike1",
                CubeListBuilder.create().texOffs(26, 13)
                        .addBox(-0.5F, -0.5F, 0.0F, 1.0F, 1.0F, 6.0F),
                PartPose.offsetAndRotation(-3.0F, 16.0F, -2.0F, 0.7063936F, -0.2602503F, 0.0F));
        root.addOrReplaceChild("spike2",
                CubeListBuilder.create().texOffs(26, 13)
                        .addBox(-0.5F, -0.5F, 0.0F, 1.0F, 1.0F, 6.0F),
                PartPose.offsetAndRotation(-1.0F, 16.0F, 1.0F, 0.7063936F, -0.111544F, 0.0F));
        root.addOrReplaceChild("spike3",
                CubeListBuilder.create().texOffs(26, 13)
                        .addBox(-0.5F, -0.5F, 0.0F, 1.0F, 1.0F, 6.0F),
                PartPose.offsetAndRotation(1.0F, 16.0F, 1.0F, 0.7063936F, 0.1115358F, 0.0F));
        root.addOrReplaceChild("spike4",
                CubeListBuilder.create().texOffs(26, 13)
                        .addBox(-0.5F, -0.5F, 0.0F, 1.0F, 1.0F, 6.0F),
                PartPose.offsetAndRotation(3.0F, 16.0F, -2.0F, 0.7063936F, 0.260246F, 0.0F));
        root.addOrReplaceChild("spike5",
                CubeListBuilder.create().texOffs(26, 13)
                        .addBox(-0.5F, -0.5F, 0.0F, 1.0F, 1.0F, 6.0F),
                PartPose.offsetAndRotation(0.0F, 16.0F, -3.0F, 0.7063936F, 0.0F, 0.0F));
        root.addOrReplaceChild("insides1",
                CubeListBuilder.create().texOffs(0, 30)
                        .addBox(-2.0F, -3.0F, -3.0F, 4.0F, 4.0F, 0.0F),
                PartPose.offset(0.0F, 20.0F, 0.0F));
        root.addOrReplaceChild("insides2",
                CubeListBuilder.create().texOffs(-1, 29)
                        .addBox(-2.0F, -3.0F, -4.0F, 4.0F, 0.0F, 1.0F),
                PartPose.offset(0.0F, 20.0F, 0.0F));
        root.addOrReplaceChild("insides3",
                CubeListBuilder.create().texOffs(-1, 29)
                        .addBox(-2.0F, 1.0F, -4.0F, 4.0F, 0.0F, 1.0F),
                PartPose.offset(0.0F, 20.0F, 0.0F));
        root.addOrReplaceChild("insides4",
                CubeListBuilder.create().texOffs(0, 29)
                        .addBox(-2.0F, -3.0F, -4.0F, 0.0F, 4.0F, 1.0F),
                PartPose.offset(0.0F, 20.0F, 0.0F));
        root.addOrReplaceChild("insides5",
                CubeListBuilder.create().texOffs(0, 29)
                        .addBox(2.0F, -3.0F, -4.0F, 0.0F, 4.0F, 1.0F),
                PartPose.offset(0.0F, 20.0F, 0.0F));

        return LayerDefinition.create(mesh, 128, 128);
    }

    @Override
    public ModelPart root() {
        return this.root;
    }

    @Override
    public void setupAnim(
            CreepingHorror entity,
            float limbSwing,
            float limbSwingAmount,
            float ageInTicks,
            float netHeadYaw,
            float headPitch) {
        // gold render(): leg walk from age * 1.25 * PI * 0.35 * limbSwingAmount
        float newangle = Mth.cos(ageInTicks * 1.25F) * (float) Math.PI * 0.35F * limbSwingAmount;
        this.leg1.yRot = 0.576F + newangle;
        this.leg1part2.yRot = this.leg1.yRot;
        this.leg2.yRot = -0.576F - newangle;
        this.leg2part2.yRot = this.leg2.yRot;
        this.leg3.yRot = -0.576F - newangle;
        this.leg3part2.yRot = this.leg3.yRot;
        this.leg4.yRot = 0.576F + newangle;
        this.leg4part2.yRot = this.leg4.yRot;

        newangle = Mth.cos(ageInTicks * 0.48F) * (float) Math.PI * 0.15F;
        this.pincer1.yRot = newangle;
        this.pincer1part2.yRot = newangle;
        this.pincer2.yRot = -newangle;
        this.pincer2part2.yRot = -newangle;

        newangle = Mth.cos(ageInTicks * 0.11F) * (float) Math.PI * 0.25F;
        newangle = Math.abs(newangle);
        this.tailseg1.xRot = -0.55F + newangle;
        this.tailseg3.xRot = -0.22F + newangle;
        this.tailseg2.xRot = newangle;

        newangle = Mth.cos(ageInTicks * 0.81F) * (float) Math.PI * 0.08F;
        this.spike1.xRot = 0.7F + newangle;
        newangle = Mth.cos(ageInTicks * 0.87F) * (float) Math.PI * 0.08F;
        this.spike2.xRot = 0.7F + newangle;
        newangle = Mth.cos(ageInTicks * 0.99F) * (float) Math.PI * 0.08F;
        this.spike3.xRot = 0.7F + newangle;
        newangle = Mth.cos(ageInTicks * 0.103F) * (float) Math.PI * 0.08F;
        this.spike4.xRot = 0.7F + newangle;
        newangle = Mth.cos(ageInTicks * 0.107F) * (float) Math.PI * 0.08F;
        this.spike5.xRot = 0.7F + newangle;

        newangle = Mth.cos(ageInTicks * 1.11F) * (float) Math.PI * 0.08F;
        this.spike1.yRot = newangle;
        newangle = Mth.cos(ageInTicks * 1.17F) * (float) Math.PI * 0.08F;
        this.spike2.yRot = newangle;
        newangle = Mth.cos(ageInTicks * 1.25F) * (float) Math.PI * 0.08F;
        this.spike3.yRot = newangle;
        newangle = Mth.cos(ageInTicks * 1.28F) * (float) Math.PI * 0.08F;
        this.spike4.yRot = newangle;
        newangle = Mth.cos(ageInTicks * 1.31F) * (float) Math.PI * 0.08F;
        this.spike5.yRot = newangle;

        newangle = Mth.cos(ageInTicks * 1.41F) * (float) Math.PI * 0.08F;
        this.spike1.zRot = newangle;
        newangle = Mth.cos(ageInTicks * 1.47F) * (float) Math.PI * 0.08F;
        this.spike2.zRot = newangle;
        newangle = Mth.cos(ageInTicks * 1.55F) * (float) Math.PI * 0.08F;
        this.spike3.zRot = newangle;
        newangle = Mth.cos(ageInTicks * 1.58F) * (float) Math.PI * 0.08F;
        this.spike4.zRot = newangle;
        newangle = Mth.cos(ageInTicks * 1.61F) * (float) Math.PI * 0.08F;
        this.spike5.zRot = newangle;
    }
}
