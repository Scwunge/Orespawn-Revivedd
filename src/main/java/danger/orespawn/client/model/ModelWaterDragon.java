package danger.orespawn.client.model;

import danger.orespawn.entity.WaterDragon;
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
 * Port of gold {@code ModelWaterDragon} (1.7.10 ModelBase, tex 128×128) → 1.21 HierarchicalModel.
 * Cube sizes/UVs/pivots/base rots 1:1. Wingspeed default 0.5 matches ClientProxy.
 * Full walk / tail chain / fin / jaw / head-look anims from gold {@code func_78088_a}.
 */
@OnlyIn(Dist.CLIENT)
public class ModelWaterDragon extends HierarchicalModel<WaterDragon> {
    /** Model layer location. */
    public static final ModelLayerLocation LAYER_LOCATION =
            new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "water_dragon"), "main");

    private final float wingspeed;
    private final ModelPart root;
    private final ModelPart Head;
    private final ModelPart neck1;
    private final ModelPart body1;
    private final ModelPart Leg8;
    private final ModelPart Leg2;
    private final ModelPart Leg7;
    private final ModelPart Leg1;
    private final ModelPart neck2;
    private final ModelPart neck3;
    private final ModelPart neck4;
    private final ModelPart body2;
    private final ModelPart body3;
    private final ModelPart body4;
    private final ModelPart tail1;
    private final ModelPart tailmiddle;
    private final ModelPart tailtop;
    private final ModelPart tailbottom;
    private final ModelPart nose;
    private final ModelPart headfin;
    private final ModelPart rightear;
    private final ModelPart leftear;
    private final ModelPart neackfin;
    private final ModelPart Bodyfin;
    private final ModelPart jaw;

    public ModelWaterDragon(ModelPart root) {
        this(root, 0.5F);
    }

    public ModelWaterDragon(ModelPart root, float wingspeed) {
        this.root = root;
        this.wingspeed = wingspeed;
        this.Head = root.getChild("Head");
        this.neck1 = root.getChild("neck1");
        this.body1 = root.getChild("body1");
        this.Leg8 = root.getChild("Leg8");
        this.Leg2 = root.getChild("Leg2");
        this.Leg7 = root.getChild("Leg7");
        this.Leg1 = root.getChild("Leg1");
        this.neck2 = root.getChild("neck2");
        this.neck3 = root.getChild("neck3");
        this.neck4 = root.getChild("neck4");
        this.body2 = root.getChild("body2");
        this.body3 = root.getChild("body3");
        this.body4 = root.getChild("body4");
        this.tail1 = root.getChild("tail1");
        this.tailmiddle = root.getChild("tailmiddle");
        this.tailtop = root.getChild("tailtop");
        this.tailbottom = root.getChild("tailbottom");
        this.nose = root.getChild("nose");
        this.headfin = root.getChild("headfin");
        this.rightear = root.getChild("rightear");
        this.leftear = root.getChild("leftear");
        this.neackfin = root.getChild("neackfin");
        this.Bodyfin = root.getChild("Bodyfin");
        this.jaw = root.getChild("jaw");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        root.addOrReplaceChild("Head",
                CubeListBuilder.create().texOffs(79, 64).addBox(-4.0F, -4.0F, -8.0F, 7.0F, 8.0F, 8.0F),
                PartPose.offset(0.0F, 0.0F, -3.0F));
        root.addOrReplaceChild("neck1",
                CubeListBuilder.create().texOffs(29, 70).addBox(-2.0F, 0.0F, -3.0F, 5.0F, 5.0F, 5.0F),
                PartPose.offsetAndRotation(-1.0F, 4.0F, -5.0F, -0.1858931F, 0.0F, 0.0F));
        root.addOrReplaceChild("body1",
                CubeListBuilder.create().texOffs(0, 33).addBox(-5.0F, -4.0F, -6.0F, 9.0F, 9.0F, 9.0F),
                PartPose.offset(0.0F, 19.0F, 2.0F));
        root.addOrReplaceChild("Leg8",
                CubeListBuilder.create().texOffs(23, 25).addBox(0.0F, -1.0F, -1.0F, 9.0F, 2.0F, 3.0F),
                PartPose.offsetAndRotation(3.0F, 22.0F, -2.0F, 0.0F, 0.5759587F, 0.1919862F));
        root.addOrReplaceChild("Leg2",
                CubeListBuilder.create().texOffs(80, 18).addBox(0.0F, -1.0F, -1.0F, 9.0F, 2.0F, 3.0F),
                PartPose.offsetAndRotation(2.0F, 22.0F, 13.0F, 0.0F, -0.5759587F, 0.1919862F));
        root.addOrReplaceChild("Leg7",
                CubeListBuilder.create().texOffs(23, 18).addBox(-9.0F, -1.0F, -1.0F, 9.0F, 2.0F, 3.0F),
                PartPose.offsetAndRotation(-4.0F, 22.0F, -1.0F, 0.0F, -0.5759587F, -0.1919862F));
        root.addOrReplaceChild("Leg1",
                CubeListBuilder.create().texOffs(80, 25).addBox(-9.0F, -1.0F, -2.0F, 9.0F, 2.0F, 3.0F),
                PartPose.offsetAndRotation(-3.0F, 22.0F, 14.0F, 0.0F, 0.5759587F, -0.1919862F));
        root.addOrReplaceChild("neck2",
                CubeListBuilder.create().texOffs(0, 11).addBox(-2.0F, 0.0F, -2.0F, 5.0F, 5.0F, 5.0F),
                PartPose.offsetAndRotation(-1.0F, 9.0F, -7.0F, 0.1115358F, 0.0F, 0.0F));
        root.addOrReplaceChild("neck3",
                CubeListBuilder.create().texOffs(0, 22).addBox(-2.0F, 0.0F, -2.0F, 5.0F, 5.0F, 5.0F),
                PartPose.offsetAndRotation(-1.0F, 14.0F, -6.0F, 0.4461433F, 0.0F, 0.0F));
        root.addOrReplaceChild("neck4",
                CubeListBuilder.create().texOffs(26, 12).addBox(-3.0F, 0.0F, -2.0F, 5.0F, 3.0F, 3.0F),
                PartPose.offsetAndRotation(0.0F, 18.0F, -4.0F, 1.226894F, 0.0F, 0.0F));
        root.addOrReplaceChild("body2",
                CubeListBuilder.create().texOffs(0, 52).addBox(-5.0F, -5.0F, 0.0F, 7.0F, 7.0F, 9.0F),
                PartPose.offset(1.0F, 21.0F, 5.0F));
        root.addOrReplaceChild("body3",
                CubeListBuilder.create().texOffs(0, 69).addBox(-3.0F, -3.0F, 0.0F, 5.0F, 5.0F, 7.0F),
                PartPose.offset(0.0F, 20.0F, 14.0F));
        root.addOrReplaceChild("body4",
                CubeListBuilder.create().texOffs(0, 89).addBox(-1.0F, -1.0F, 0.0F, 3.0F, 3.0F, 5.0F),
                PartPose.offset(-1.0F, 19.0F, 21.0F));
        root.addOrReplaceChild("tail1",
                CubeListBuilder.create().texOffs(0, 82).addBox(0.0F, 0.0F, 0.0F, 1.0F, 2.0F, 3.0F),
                PartPose.offset(-1.0F, 19.0F, 25.0F));
        root.addOrReplaceChild("tailmiddle",
                CubeListBuilder.create().texOffs(55, 37).addBox(-1.0F, -6.0F, 0.0F, 2.0F, 11.0F, 9.0F),
                PartPose.offset(0.0F, 19.0F, 28.0F));
        root.addOrReplaceChild("tailtop",
                CubeListBuilder.create().texOffs(82, 36).addBox(-1.0F, -11.0F, 0.0F, 2.0F, 11.0F, 9.0F),
                PartPose.offsetAndRotation(0.0F, 14.0F, 28.0F, -0.6320364F, 0.0F, 0.0F));
        root.addOrReplaceChild("tailbottom",
                CubeListBuilder.create().texOffs(56, 60).addBox(0.0F, 0.0F, 0.0F, 2.0F, 11.0F, 9.0F),
                PartPose.offsetAndRotation(-1.0F, 23.0F, 28.0F, 0.6320361F, 0.0F, -0.0174533F));
        root.addOrReplaceChild("nose",
                CubeListBuilder.create().texOffs(54, 19).addBox(-3.0F, -2.0F, -5.0F, 5.0F, 5.0F, 5.0F),
                PartPose.offset(0.0F, -2.0F, -11.0F));
        root.addOrReplaceChild("headfin",
                CubeListBuilder.create().texOffs(0, 99).addBox(0.0F, -5.0F, 0.0F, 0.0F, 10.0F, 9.0F),
                PartPose.offsetAndRotation(0.0F, -4.0F, -6.0F, 0.1396263F, 0.0F, 0.0F));
        root.addOrReplaceChild("rightear",
                CubeListBuilder.create().texOffs(38, 32).addBox(0.0F, 0.0F, 0.0F, 0.0F, 5.0F, 5.0F),
                PartPose.offsetAndRotation(-4.0F, -2.0F, -5.0F, 0.0698132F, -0.418879F, 0.0F));
        root.addOrReplaceChild("leftear",
                CubeListBuilder.create().texOffs(38, 32).addBox(0.0F, 0.0F, 0.0F, 0.0F, 5.0F, 5.0F),
                PartPose.offsetAndRotation(3.0F, -2.0F, -5.0F, 0.0698132F, 0.418879F, 0.0F));
        root.addOrReplaceChild("neackfin",
                CubeListBuilder.create().texOffs(42, 47).addBox(0.0F, -1.0F, 0.0F, 0.0F, 5.0F, 5.0F),
                PartPose.offsetAndRotation(0.0F, 3.0F, -3.0F, -0.185895F, 0.0F, 0.0F));
        root.addOrReplaceChild("Bodyfin",
                CubeListBuilder.create().texOffs(21, 91).addBox(0.0F, -6.0F, -3.0F, 0.0F, 10.0F, 9.0F),
                PartPose.offsetAndRotation(0.0F, 15.0F, 2.0F, -0.0698132F, 0.0F, 0.0F));
        root.addOrReplaceChild("jaw",
                CubeListBuilder.create().texOffs(76, 8).addBox(-2.0F, 0.0F, -5.0F, 5.0F, 1.0F, 5.0F),
                PartPose.offset(-1.0F, 3.0F, -10.0F));

        return LayerDefinition.create(mesh, 128, 128);
    }

    @Override
    public ModelPart root() {
        return this.root;
    }

    /**
     * Gold {@code func_78088_a} anims (empty {@code func_78087_a}):
     * body3→tail chain yRot phase lag with cos/sin pivot follow;
     * 4-leg walk yRot; ear flutter; Bodyfin zRot / neckfin yRot sit-gated;
     * jaw by attacking 0/1/2; head look + nose/jaw/fin/ear follow.
     */
    @Override
    public void setupAnim(
            WaterDragon entity,
            float limbSwing,
            float limbSwingAmount,
            float ageInTicks,
            float netHeadYaw,
            float headPitch) {
        float f1 = limbSwingAmount;
        float f2 = ageInTicks;
        float newangle;
        float pi4 = (float) (Math.PI / 4);
        float root13 = (float) Math.sqrt(13.0);
        float root20 = (float) Math.sqrt(20.0);

        if (f1 > 0.1F) {
            newangle = Mth.cos(f2 * 1.3F * this.wingspeed) * (float) Math.PI * 0.2F * f1;
        } else {
            newangle = 0.0F;
        }

        // --- serpentine tail chain (body3 pivot fixed; body4→tail follow) ---
        this.body3.yRot = Mth.cos(f2 * 1.3F * this.wingspeed) * (float) Math.PI * 0.4F * f1;
        this.body4.z = this.body3.z + (float) Math.cos(this.body3.yRot) * 7.0F;
        this.body4.x = this.body3.x - 1.0F + (float) Math.sin(this.body3.yRot) * 7.0F;
        this.body4.yRot = Mth.cos(f2 * 1.3F * this.wingspeed - pi4) * (float) Math.PI * 0.4F * f1;
        this.tail1.z = this.body4.z + (float) Math.cos(this.body4.yRot) * 5.0F;
        this.tail1.x = this.body4.x + (float) Math.sin(this.body4.yRot) * 5.0F;
        this.tail1.yRot = Mth.cos(f2 * 1.3F * this.wingspeed - 2.0F * pi4) * (float) Math.PI * 0.4F * f1;
        this.tailmiddle.z = this.tail1.z + (float) Math.cos(this.tail1.yRot) * 3.0F;
        this.tailmiddle.x = this.tail1.x + (float) Math.sin(this.tail1.yRot) * 3.0F;
        this.tailmiddle.yRot = Mth.cos(f2 * 1.3F * this.wingspeed - 3.0F * pi4) * (float) Math.PI * 0.4F * f1;
        this.tailtop.yRot = this.tailmiddle.yRot;
        this.tailtop.z = this.tailmiddle.z;
        this.tailtop.x = this.tailmiddle.x;
        this.tailbottom.yRot = this.tailmiddle.yRot;
        this.tailbottom.z = this.tailmiddle.z;
        this.tailbottom.x = this.tailmiddle.x;

        // --- legs walk (yRot) ---
        this.Leg8.yRot = 0.58F + newangle;
        this.Leg2.yRot = -0.58F + newangle;
        this.Leg7.yRot = -0.58F - newangle;
        this.Leg1.yRot = 0.58F - newangle;

        // --- ear flutter ---
        newangle = Mth.cos(f2 * 0.8F * this.wingspeed) * (float) Math.PI * 0.1F;
        this.leftear.yRot = 0.62F + newangle;
        this.rightear.yRot = -0.62F - newangle;

        // --- Bodyfin zRot sit-gated ---
        newangle = Mth.cos(f2 * 0.7F * this.wingspeed) * (float) Math.PI * 0.02F;
        if (entity.isOreSpawnSitting()) {
            newangle = 0.0F;
        }
        this.Bodyfin.zRot = newangle;

        // --- neck fin yRot sit-gated ---
        newangle = Mth.cos(f2 * 0.6F * this.wingspeed) * (float) Math.PI * 0.1F;
        if (entity.isOreSpawnSitting()) {
            newangle = 0.0F;
        }
        this.neackfin.yRot = newangle;

        // --- headfin flutter (overwritten by head look below, gold order preserved) ---
        newangle = Mth.cos(f2 * 0.5F * this.wingspeed) * (float) Math.PI * 0.05F;
        if (entity.isOreSpawnSitting()) {
            newangle = 0.0F;
        }
        this.headfin.yRot = newangle;

        // --- jaw by attacking: 1 chomp, 2 open stream, else closed ---
        if (entity.getAttacking() == 1) {
            newangle = Mth.cos(f2 * 1.2F * this.wingspeed) * (float) Math.PI * 0.25F;
            this.jaw.xRot = newangle;
        } else if (entity.getAttacking() == 2) {
            this.jaw.xRot = 0.45F;
        } else {
            this.jaw.xRot = -0.25F;
        }

        // --- head look + attached parts follow ---
        newangle = (float) Math.toRadians(netHeadYaw) * 0.75F;
        this.Head.yRot = newangle;
        this.nose.yRot = newangle;
        this.nose.z = this.Head.z - (float) Math.cos(this.Head.yRot) * 8.0F;
        this.nose.x = this.Head.x - (float) Math.sin(this.Head.yRot) * 8.0F;
        this.jaw.yRot = newangle;
        this.jaw.z = this.Head.z - (float) Math.cos(this.Head.yRot) * 7.0F;
        this.jaw.x = this.Head.x - (float) Math.sin(this.Head.yRot) * 7.0F - 1.0F;
        this.headfin.yRot = newangle;
        this.headfin.z = this.Head.z - (float) Math.cos(this.Head.yRot) * 3.0F;
        this.headfin.x = this.Head.x - (float) Math.sin(this.Head.yRot) * 3.0F;
        this.leftear.yRot += newangle;
        this.leftear.z = this.Head.z - (float) Math.cos(this.Head.yRot - pi4) * root13;
        this.leftear.x = this.Head.x - (float) Math.sin(this.Head.yRot - pi4) * root13;
        this.rightear.yRot += newangle;
        this.rightear.z = this.Head.z - (float) Math.cos(this.Head.yRot + pi4) * root20;
        this.rightear.x = this.Head.x - (float) Math.sin(this.Head.yRot + pi4) * root20;
    }
}
