package danger.orespawn.client.model;

import danger.orespawn.entity.Whale;
import danger.orespawn.util.Reference;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

/**
 * Port of gold {@code ModelWhale} (1.7.10 ModelBase, tex 256×256) → 1.21 HierarchicalModel.
 * Cubes / pivots / UV 1:1. Full gold {@code func_78088_a} fin roll + jaw bob + chained tail.
 * No wingspeed in gold constructor.
 */
@OnlyIn(Dist.CLIENT)
public class ModelWhale extends HierarchicalModel<Whale> {
    /** Model layer location. */
    public static final ModelLayerLocation LAYER_LOCATION =
            new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "whale"), "main");

    private final ModelPart root;
    private final ModelPart belly;
    private final ModelPart body;
    private final ModelPart back;
    private final ModelPart tail1;
    private final ModelPart tail2;
    private final ModelPart tailfin1;
    private final ModelPart tailfin2;
    private final ModelPart backfin;
    private final ModelPart head;
    private final ModelPart jaw;
    private final ModelPart lfin1;
    private final ModelPart lfin2;
    private final ModelPart rfin1;
    private final ModelPart rfin2;

    /** Gold base pivots for animated tail chain. */
    private static final float TAIL1_Y = 11.0F;
    private static final float TAIL1_Z = 52.0F;
    private static final float TAIL2_Y = 12.0F;
    private static final float TAIL2_Z = 66.0F;
    private static final float TAILFIN_Y = 13.0F;
    private static final float TAILFIN_Z = 74.0F;

    public ModelWhale(ModelPart root) {
        this.root = root;
        this.belly = root.getChild("belly");
        this.body = root.getChild("body");
        this.back = root.getChild("back");
        this.tail1 = root.getChild("tail1");
        this.tail2 = root.getChild("tail2");
        this.tailfin1 = root.getChild("tailfin1");
        this.tailfin2 = root.getChild("tailfin2");
        this.backfin = root.getChild("backfin");
        this.head = root.getChild("head");
        this.jaw = root.getChild("jaw");
        this.lfin1 = root.getChild("lfin1");
        this.lfin2 = root.getChild("lfin2");
        this.rfin1 = root.getChild("rfin1");
        this.rfin2 = root.getChild("rfin2");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        // gold tex 256×256; cubes/pivots/base rotations 1:1
        root.addOrReplaceChild(
                "belly",
                CubeListBuilder.create().texOffs(0, 92).addBox(-6.0F, 0.0F, 0.0F, 12.0F, 2.0F, 32.0F),
                PartPose.offset(0.0F, 22.0F, 6.0F));
        root.addOrReplaceChild(
                "body",
                CubeListBuilder.create().texOffs(0, 188).addBox(-10.0F, 0.0F, 0.0F, 20.0F, 12.0F, 52.0F),
                PartPose.offset(0.0F, 10.0F, 0.0F));
        root.addOrReplaceChild(
                "back",
                CubeListBuilder.create().texOffs(0, 45).addBox(-4.0F, 0.0F, 0.0F, 8.0F, 2.0F, 40.0F),
                PartPose.offset(0.0F, 8.0F, 3.0F));
        root.addOrReplaceChild(
                "tail1",
                CubeListBuilder.create().texOffs(186, 0).addBox(-6.0F, 0.0F, 0.0F, 12.0F, 7.0F, 14.0F),
                PartPose.offset(0.0F, TAIL1_Y, TAIL1_Z));
        root.addOrReplaceChild(
                "tail2",
                CubeListBuilder.create().texOffs(186, 24).addBox(-4.0F, 0.0F, 0.0F, 8.0F, 5.0F, 10.0F),
                PartPose.offset(0.0F, TAIL2_Y, TAIL2_Z));
        root.addOrReplaceChild(
                "tailfin1",
                CubeListBuilder.create().texOffs(186, 43).addBox(0.0F, 0.0F, 0.0F, 17.0F, 2.0F, 11.0F),
                PartPose.offsetAndRotation(2.0F, TAILFIN_Y, TAILFIN_Z, 0.0872665F, -0.0872665F, 0.0F));
        root.addOrReplaceChild(
                "tailfin2",
                CubeListBuilder.create().texOffs(186, 59).addBox(-17.0F, 0.0F, 0.0F, 17.0F, 2.0F, 11.0F),
                PartPose.offsetAndRotation(-2.0F, TAILFIN_Y, TAILFIN_Z, 0.0872665F, 0.0872665F, 0.0F));
        root.addOrReplaceChild(
                "backfin",
                CubeListBuilder.create().texOffs(0, 15).addBox(-0.5F, 0.0F, 0.0F, 1.0F, 4.0F, 8.0F),
                PartPose.offsetAndRotation(0.0F, 8.0F, 11.0F, 0.3665191F, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "head",
                CubeListBuilder.create().texOffs(0, 155).addBox(-8.0F, 0.0F, -16.0F, 16.0F, 8.0F, 22.0F),
                PartPose.offset(0.0F, 11.0F, -6.0F));
        root.addOrReplaceChild(
                "jaw",
                CubeListBuilder.create().texOffs(0, 130).addBox(-7.0F, -1.0F, -20.0F, 14.0F, 2.0F, 20.0F),
                PartPose.offsetAndRotation(0.0F, 20.0F, 0.0F, 0.0698132F, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "lfin1",
                CubeListBuilder.create().texOffs(96, 0).addBox(0.0F, -1.0F, -3.0F, 4.0F, 3.0F, 6.0F),
                PartPose.offsetAndRotation(10.0F, 18.0F, 8.0F, 0.0F, -0.0872665F, 0.0F));
        root.addOrReplaceChild(
                "lfin2",
                CubeListBuilder.create().texOffs(120, 0).addBox(2.0F, -0.5F, -3.0F, 22.0F, 2.0F, 8.0F),
                PartPose.offsetAndRotation(10.0F, 18.0F, 8.0F, 0.0F, -0.0872665F, 0.0F));
        root.addOrReplaceChild(
                "rfin1",
                CubeListBuilder.create().texOffs(96, 12).addBox(-4.0F, -1.0F, -3.0F, 4.0F, 3.0F, 6.0F),
                PartPose.offsetAndRotation(-10.0F, 18.0F, 8.0F, 0.0F, 0.0872665F, 0.0F));
        root.addOrReplaceChild(
                "rfin2",
                CubeListBuilder.create().texOffs(120, 13).addBox(-24.0F, -0.5F, -3.0F, 22.0F, 2.0F, 8.0F),
                PartPose.offsetAndRotation(-10.0F, 18.0F, 8.0F, 0.0F, 0.0872665F, 0.0F));

        return LayerDefinition.create(mesh, 256, 256);
    }

    @Override
    public ModelPart root() {
        return this.root;
    }

    @Override
    public void setupAnim(
            Whale entity,
            float limbSwing,
            float limbSwingAmount,
            float ageInTicks,
            float netHeadYaw,
            float headPitch) {
        // gold func_78088_a — f1=limbSwingAmount, f2=ageInTicks
        float newangle = Mth.cos(ageInTicks * 0.55F) * (float) Math.PI * 0.15F;
        if (limbSwingAmount > 0.1F) {
            newangle = Mth.cos(ageInTicks * 0.3F) * (float) Math.PI * 0.2F * limbSwingAmount;
        } else {
            newangle = Mth.cos(ageInTicks * 0.08F) * (float) Math.PI * 0.05F;
        }

        // side fins roll (field_78808_h → zRot)
        this.lfin2.zRot = 0.436F + newangle;
        this.lfin1.zRot = this.lfin2.zRot / 2.0F;
        this.rfin2.zRot = -0.436F - newangle;
        this.rfin1.zRot = this.rfin2.zRot / 2.0F;
        this.lfin1.yRot = -0.0872665F;
        this.lfin2.yRot = -0.0872665F;
        this.rfin1.yRot = 0.0872665F;
        this.rfin2.yRot = 0.0872665F;

        // jaw idle bob
        newangle = Mth.cos(ageInTicks * 0.03F) * (float) Math.PI * 0.02F;
        this.jaw.xRot = 0.087F + newangle;

        // tail chain
        if (limbSwingAmount > 0.1F) {
            newangle = Mth.cos(ageInTicks * 0.4F) * (float) Math.PI * 0.16F * limbSwingAmount;
        } else {
            newangle = Mth.cos(ageInTicks * 0.05F) * (float) Math.PI * 0.03F;
        }

        this.tail1.xRot = newangle * 0.5F;
        this.tail2.xRot = newangle * 1.25F;
        this.tailfin1.xRot = this.tailfin2.xRot = newangle * 2.25F;
        // keep gold yaw offsets on fins
        this.tailfin1.yRot = -0.0872665F;
        this.tailfin2.yRot = 0.0872665F;

        // gold: field_78798_e=z, field_78797_d=y — chain from tail1 base
        this.tail1.y = TAIL1_Y;
        this.tail1.z = TAIL1_Z;
        this.tail2.z = this.tail1.z + (float) Math.cos(this.tail1.xRot) * 14.0F;
        this.tail2.y = this.tail1.y - (float) Math.sin(this.tail1.xRot) * 14.0F;
        this.tailfin1.z = this.tailfin2.z = this.tail2.z + (float) Math.cos(this.tail2.xRot) * 8.0F;
        this.tailfin1.y = this.tailfin2.y = this.tail2.y - (float) Math.sin(this.tail2.xRot) * 8.0F;

        // static body parts
        this.backfin.xRot = 0.3665191F;
        this.belly.xRot = 0.0F;
        this.body.xRot = 0.0F;
        this.back.xRot = 0.0F;
        this.head.xRot = 0.0F;
    }
}
