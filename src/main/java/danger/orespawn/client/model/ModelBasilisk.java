package danger.orespawn.client.model;

import danger.orespawn.entity.Basilisk;
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
 * Port of gold {@code ModelBasilisk} (1.7.10 ModelBase, tex 256×64) → 1.21 HierarchicalModel.
 * Cube sizes/UVs/pivots/base rots 1:1. Wingspeed default 0.3 matches ClientProxy.
 * Full serpentine body/tail chain + jaw attack anim from gold {@code func_78088_a}.
 */
@OnlyIn(Dist.CLIENT)
public class ModelBasilisk extends HierarchicalModel<Basilisk> {
    /** Model layer location. */
    public static final ModelLayerLocation LAYER_LOCATION =
            new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "basilisk"), "main");

    private final float wingspeed;
    private final ModelPart root;
    private final ModelPart body3;
    private final ModelPart body2;
    private final ModelPart body1;
    private final ModelPart body4;
    private final ModelPart body5;
    private final ModelPart body6;
    private final ModelPart tail1;
    private final ModelPart tail2;
    private final ModelPart tail3;
    private final ModelPart tail4;
    private final ModelPart neck2;
    private final ModelPart neck1;
    private final ModelPart head;
    private final ModelPart rog_1;
    private final ModelPart rog_2;
    private final ModelPart rog_3;
    private final ModelPart rog_4;
    private final ModelPart rog_5;
    private final ModelPart rog_6;
    private final ModelPart snout;
    private final ModelPart jaw;

    public ModelBasilisk(ModelPart root) {
        this(root, 0.3F);
    }

    public ModelBasilisk(ModelPart root, float wingspeed) {
        this.root = root;
        this.wingspeed = wingspeed;
        this.body3 = root.getChild("body3");
        this.body2 = root.getChild("body2");
        this.body1 = root.getChild("body1");
        this.body4 = root.getChild("body4");
        this.body5 = root.getChild("body5");
        this.body6 = root.getChild("body6");
        this.tail1 = root.getChild("tail1");
        this.tail2 = root.getChild("tail2");
        this.tail3 = root.getChild("tail3");
        this.tail4 = root.getChild("tail4");
        this.neck2 = root.getChild("neck2");
        this.neck1 = root.getChild("neck1");
        this.head = root.getChild("head");
        this.rog_1 = root.getChild("rog_1");
        this.rog_2 = root.getChild("rog_2");
        this.rog_3 = root.getChild("rog_3");
        this.rog_4 = root.getChild("rog_4");
        this.rog_5 = root.getChild("rog_5");
        this.rog_6 = root.getChild("rog_6");
        this.snout = root.getChild("snout");
        this.jaw = root.getChild("jaw");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        root.addOrReplaceChild("body3",
                CubeListBuilder.create().texOffs(0, 32).addBox(0.0F, 0.0F, 0.0F, 16.0F, 16.0F, 16.0F),
                PartPose.offset(-8.0F, 8.0F, 0.0F));
        root.addOrReplaceChild("body2",
                CubeListBuilder.create().texOffs(0, 32).addBox(0.0F, 0.0F, 0.0F, 16.0F, 16.0F, 16.0F),
                PartPose.offsetAndRotation(-8.0F, 4.0F, -10.0F, -0.2974289F, 0.0F, 0.0F));
        root.addOrReplaceChild("body1",
                CubeListBuilder.create().texOffs(0, 32).addBox(0.0F, 0.0F, 0.0F, 16.0F, 16.0F, 16.0F),
                PartPose.offsetAndRotation(-8.0F, 2.0F, -25.0F, -0.1487144F, 0.0F, 0.0F));
        root.addOrReplaceChild("body4",
                CubeListBuilder.create().texOffs(0, 32).addBox(0.0F, 0.0F, 0.0F, 16.0F, 16.0F, 16.0F),
                PartPose.offsetAndRotation(-8.0F, 8.0F, 13.0F, 0.1487144F, 0.0F, 0.0F));
        root.addOrReplaceChild("body5",
                CubeListBuilder.create().texOffs(0, 32).addBox(0.0F, 0.0F, 0.0F, 16.0F, 16.0F, 16.0F),
                PartPose.offset(-8.0F, 5.8F, 28.8F));
        root.addOrReplaceChild("body6",
                CubeListBuilder.create().texOffs(148, 4).addBox(0.0F, 0.0F, 0.0F, 15.0F, 15.0F, 17.0F),
                PartPose.offsetAndRotation(-7.5F, 6.166667F, 44.0F, -0.1115358F, 0.0F, 0.0F));
        root.addOrReplaceChild("tail1",
                CubeListBuilder.create().texOffs(140, 36).addBox(0.0F, 0.0F, 0.0F, 13.0F, 13.0F, 15.0F),
                PartPose.offsetAndRotation(-6.5F, 9.0F, 58.0F, 0.1115358F, 0.0F, 0.0F));
        root.addOrReplaceChild("tail2",
                CubeListBuilder.create().texOffs(64, 41).addBox(0.0F, 0.0F, 0.0F, 10.0F, 10.0F, 13.0F),
                PartPose.offsetAndRotation(-5.0F, 10.0F, 70.0F, 0.4089647F, 0.0F, 0.0F));
        root.addOrReplaceChild("tail3",
                CubeListBuilder.create().texOffs(64, 20).addBox(0.0F, 0.0F, 0.0F, 8.0F, 8.0F, 13.0F),
                PartPose.offsetAndRotation(-4.0F, 6.0F, 82.0F, 0.2230717F, 0.0F, 0.0F));
        root.addOrReplaceChild("tail4",
                CubeListBuilder.create().texOffs(64, 1).addBox(0.0F, 0.0F, 0.0F, 6.0F, 6.0F, 13.0F),
                PartPose.offsetAndRotation(-3.0F, 4.0F, 95.0F, -0.0743572F, 0.0F, 0.0F));
        root.addOrReplaceChild("neck2",
                CubeListBuilder.create().texOffs(0, 32).addBox(0.0F, 0.0F, 0.0F, 16.0F, 16.0F, 16.0F),
                PartPose.offsetAndRotation(-8.0F, -4.9F, -26.0F, -0.8464847F, 0.0F, 0.0F));
        root.addOrReplaceChild("neck1",
                CubeListBuilder.create().texOffs(0, 32).addBox(0.0F, 0.0F, 0.0F, 16.0F, 16.0F, 16.0F),
                PartPose.offsetAndRotation(-8.0F, -15.0F, -29.0F, -1.181092F, 0.0F, 0.0F));
        root.addOrReplaceChild("head",
                CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, 0.0F, 0.0F, 16.0F, 18.0F, 10.0F),
                PartPose.offsetAndRotation(-8.0F, -21.0F, -30.0F, -1.404164F, 0.0F, 0.0F));
        root.addOrReplaceChild("rog_1",
                CubeListBuilder.create().texOffs(110, 45).addBox(0.0F, 0.0F, 0.0F, 3.0F, 3.0F, 5.0F),
                PartPose.offsetAndRotation(3.0F, -21.0F, -32.0F, 0.6320364F, 0.2230717F, 0.0F));
        root.addOrReplaceChild("rog_2",
                CubeListBuilder.create().texOffs(110, 45).addBox(0.0F, 0.0F, 0.0F, 3.0F, 3.0F, 5.0F),
                PartPose.offsetAndRotation(-6.0F, -21.0F, -32.8F, 0.6320364F, -0.2230705F, 0.0F));
        root.addOrReplaceChild("rog_3",
                CubeListBuilder.create().texOffs(52, 0).addBox(0.0F, 0.0F, 0.0F, 2.0F, 2.0F, 4.0F),
                PartPose.offsetAndRotation(0.4666667F, -21.0F, -31.0F, 0.6320364F, 0.2230717F, 0.0F));
        root.addOrReplaceChild("rog_4",
                CubeListBuilder.create().texOffs(52, 0).addBox(0.0F, 0.0F, 0.0F, 2.0F, 2.0F, 4.0F),
                PartPose.offsetAndRotation(-2.466667F, -21.0F, -31.46667F, 0.6320364F, -0.2230705F, 0.0F));
        root.addOrReplaceChild("rog_5",
                CubeListBuilder.create().texOffs(52, 0).addBox(0.0F, 0.0F, 0.0F, 2.0F, 2.0F, 4.0F),
                PartPose.offsetAndRotation(-8.0F, -17.0F, -32.0F, 0.6320364F, -0.6692139F, 0.0F));
        root.addOrReplaceChild("rog_6",
                CubeListBuilder.create().texOffs(52, 0).addBox(0.0F, 0.0F, 0.0F, 2.0F, 2.0F, 4.0F),
                PartPose.offsetAndRotation(6.4F, -17.0F, -32.0F, 0.6320364F, 0.6692116F, 0.0F));
        root.addOrReplaceChild("snout",
                CubeListBuilder.create().texOffs(102, 1).addBox(0.0F, 0.0F, 0.0F, 14.0F, 16.0F, 9.0F),
                PartPose.offsetAndRotation(-7.0F, -17.0F, -43.0F, -1.404164F, 0.0F, 0.0F));
        root.addOrReplaceChild("jaw",
                CubeListBuilder.create().texOffs(106, 26).addBox(0.0F, 0.0F, 0.0F, 14.0F, 16.0F, 3.0F),
                PartPose.offsetAndRotation(-7.0F, -11.0F, -39.0F, -0.8836633F, 0.0F, 0.0F));

        return LayerDefinition.create(mesh, 256, 64);
    }

    @Override
    public ModelPart root() {
        return this.root;
    }

    /**
     * Gold {@code func_78088_a} anims (empty {@code func_78087_a}):
     * phase-lagged serpentine yRot chain body1→tail4 with cos/sin pivot follow;
     * jaw opens when attacking.
     */
    @Override
    public void setupAnim(
            Basilisk entity,
            float limbSwing,
            float limbSwingAmount,
            float ageInTicks,
            float netHeadYaw,
            float headPitch) {
        float f1 = limbSwingAmount;
        float f2 = ageInTicks;
        float pi4 = 0.7853975F;

        // unused in gold except as walk-gate — chain always uses f1 scale
        float newangle;
        if (f1 > 0.1F) {
            newangle = Mth.cos(f2 * 1.3F * this.wingspeed) * (float) Math.PI * 0.1F * f1;
        } else {
            newangle = 0.0F;
        }
        // newangle kept for parity with gold local (chain recomputes per segment)

        this.body1.yRot = Mth.cos(f2 * 1.3F * this.wingspeed) * (float) Math.PI * 0.1F * f1;
        this.body2.z = this.body1.z + (float) Math.cos(this.body1.yRot) * 12.0F;
        this.body2.x = this.body1.x + (float) Math.sin(this.body1.yRot) * 12.0F;
        this.body2.yRot = Mth.cos(f2 * 1.3F * this.wingspeed - pi4) * (float) Math.PI * 0.1F * f1;

        this.body3.z = this.body2.z + (float) Math.cos(this.body2.yRot) * 11.0F;
        this.body3.x = this.body2.x + (float) Math.sin(this.body2.yRot) * 11.0F;
        this.body3.yRot = Mth.cos(f2 * 1.3F * this.wingspeed - 2.0F * pi4) * (float) Math.PI * 0.1F * f1;

        this.body4.z = this.body3.z + (float) Math.cos(this.body3.yRot) * 12.0F;
        this.body4.x = this.body3.x + (float) Math.sin(this.body3.yRot) * 12.0F;
        this.body4.yRot = Mth.cos(f2 * 1.3F * this.wingspeed - 3.0F * pi4) * (float) Math.PI * 0.1F * f1;

        this.body5.z = this.body4.z + (float) Math.cos(this.body4.yRot) * 12.0F;
        this.body5.x = this.body4.x + (float) Math.sin(this.body4.yRot) * 12.0F;
        this.body5.yRot = Mth.cos(f2 * 1.3F * this.wingspeed - 4.0F * pi4) * (float) Math.PI * 0.1F * f1;

        this.body6.z = this.body5.z + (float) Math.cos(this.body5.yRot) * 12.0F;
        this.body6.x = this.body5.x + 0.5F + (float) Math.sin(this.body5.yRot) * 12.0F;
        this.body6.yRot = Mth.cos(f2 * 1.3F * this.wingspeed - 5.0F * pi4) * (float) Math.PI * 0.1F * f1;

        this.tail1.z = this.body6.z + (float) Math.cos(this.body6.yRot) * 12.0F;
        this.tail1.x = this.body6.x + 1.0F + (float) Math.sin(this.body6.yRot) * 12.0F;
        this.tail1.yRot = Mth.cos(f2 * 1.3F * this.wingspeed - 6.0F * pi4) * (float) Math.PI * 0.1F * f1;

        this.tail2.z = this.tail1.z + (float) Math.cos(this.tail1.yRot) * 10.0F;
        this.tail2.x = this.tail1.x + 1.5F + (float) Math.sin(this.tail1.yRot) * 10.0F;
        this.tail2.yRot = Mth.cos(f2 * 1.3F * this.wingspeed - 7.0F * pi4) * (float) Math.PI * 0.1F * f1;

        this.tail3.z = this.tail2.z + (float) Math.cos(this.tail2.yRot) * 10.0F;
        this.tail3.x = this.tail2.x + 1.0F + (float) Math.sin(this.tail2.yRot) * 10.0F;
        this.tail3.yRot = Mth.cos(f2 * 1.3F * this.wingspeed - 8.0F * pi4) * (float) Math.PI * 0.1F * f1;

        this.tail4.z = this.tail3.z + (float) Math.cos(this.tail3.yRot) * 10.0F;
        this.tail4.x = this.tail3.x + 1.0F + (float) Math.sin(this.tail3.yRot) * 10.0F;
        this.tail4.yRot = Mth.cos(f2 * 1.3F * this.wingspeed - 9.0F * pi4) * (float) Math.PI * 0.1F * f1;

        // jaw attack chomp
        if (entity.getAttacking() != 0) {
            this.jaw.xRot = -1.0F + Mth.cos(f2 * 0.45F) * (float) Math.PI * 0.18F;
        } else {
            this.jaw.xRot = -1.1F;
        }
    }
}
