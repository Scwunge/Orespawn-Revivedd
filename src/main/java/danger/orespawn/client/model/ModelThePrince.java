package danger.orespawn.client.model;

import danger.orespawn.entity.ThePrince;
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
 * Port of gold {@code ModelThePrince} (1.7.10 ModelBase, tex 128×128, 35 cubes) → HierarchicalModel.
 * Cube sizes/UVs/pivots/base rots 1:1. Wingspeed default 0.65F matches ClientProxy.
 * Full wing / leg / tail chain / multi-head look / jaw / neck-ext anims from gold {@code func_78088_a}.
 */
@OnlyIn(Dist.CLIENT)
public class ModelThePrince extends HierarchicalModel<ThePrince> {
    /** Model layer location. */
    public static final ModelLayerLocation LAYER_LOCATION =
            new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "the_prince"), "main");

    private final float wingspeed;
    private final ModelPart root;
    private final ModelPart body;
    private final ModelPart neck1;
    private final ModelPart neck;
    private final ModelPart neckbase;
    private final ModelPart head;
    private final ModelPart Rleg1;
    private final ModelPart Lleg1;
    private final ModelPart snout;
    private final ModelPart tail1;
    private final ModelPart tail2;
    private final ModelPart tail3;
    private final ModelPart tail4;
    private final ModelPart Lwing;
    private final ModelPart Rwing;
    private final ModelPart Tail5;
    private final ModelPart Tail6;
    private final ModelPart Lneck1;
    private final ModelPart Lneck;
    private final ModelPart Lhead;
    private final ModelPart Lsnout;
    private final ModelPart Rneck1;
    private final ModelPart Rneck;
    private final ModelPart Rhead;
    private final ModelPart Rsnout;
    private final ModelPart headfin;
    private final ModelPart Lheadfin;
    private final ModelPart Rheadfin;
    private final ModelPart Backfin;
    private final ModelPart Rwing2;
    private final ModelPart Rwing3;
    private final ModelPart Lwing2;
    private final ModelPart Lwing3;
    private final ModelPart Ljaw;
    private final ModelPart jaw;
    private final ModelPart Rjaw;

    // gold base pivots used when positions are rewritten each frame
    private static final float TAIL2_X = 0.0F;
    private static final float TAIL2_Z = 7.0F;
    private static final float LNECK_X = 4.5F;
    private static final float LNECK_Y = 15.0F;
    private static final float LNECK_Z = -6.0F;
    private static final float NECK_X = 0.0F;
    private static final float NECK_Y = 15.0F;
    private static final float NECK_Z = -6.0F;
    private static final float RNECK_X = -4.5F;
    private static final float RNECK_Y = 15.0F;
    private static final float RNECK_Z = -6.0F;
    private static final float SNOUT_Z = -6.0F;
    private static final float SNOUT_X = 0.0F;
    private static final float LSNOUT_X = 4.5F;
    private static final float RSNOUT_X = -4.5F;
    private static final float JAW_BASE_Z = -7.0F;

    public ModelThePrince(ModelPart root) {
        this(root, 0.65F);
    }

    public ModelThePrince(ModelPart root, float wingspeed) {
        this.root = root;
        this.wingspeed = wingspeed;
        this.body = root.getChild("body");
        this.neck1 = root.getChild("neck1");
        this.neck = root.getChild("neck");
        this.neckbase = root.getChild("neckbase");
        this.head = root.getChild("head");
        this.Rleg1 = root.getChild("Rleg1");
        this.Lleg1 = root.getChild("Lleg1");
        this.snout = root.getChild("snout");
        this.tail1 = root.getChild("tail1");
        this.tail2 = root.getChild("tail2");
        this.tail3 = root.getChild("tail3");
        this.tail4 = root.getChild("tail4");
        this.Lwing = root.getChild("Lwing");
        this.Rwing = root.getChild("Rwing");
        this.Tail5 = root.getChild("Tail5");
        this.Tail6 = root.getChild("Tail6");
        this.Lneck1 = root.getChild("Lneck1");
        this.Lneck = root.getChild("Lneck");
        this.Lhead = root.getChild("Lhead");
        this.Lsnout = root.getChild("Lsnout");
        this.Rneck1 = root.getChild("Rneck1");
        this.Rneck = root.getChild("Rneck");
        this.Rhead = root.getChild("Rhead");
        this.Rsnout = root.getChild("Rsnout");
        this.headfin = root.getChild("headfin");
        this.Lheadfin = root.getChild("Lheadfin");
        this.Rheadfin = root.getChild("Rheadfin");
        this.Backfin = root.getChild("Backfin");
        this.Rwing2 = root.getChild("Rwing2");
        this.Rwing3 = root.getChild("Rwing3");
        this.Lwing2 = root.getChild("Lwing2");
        this.Lwing3 = root.getChild("Lwing3");
        this.Ljaw = root.getChild("Ljaw");
        this.jaw = root.getChild("jaw");
        this.Rjaw = root.getChild("Rjaw");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        // gold ModelThePrince cubes 1:1 (tex 128×128)
        root.addOrReplaceChild("body",
                CubeListBuilder.create().texOffs(59, 34).addBox(-7.0F, -3.0F, -5.0F, 13.0F, 8.0F, 10.0F),
                PartPose.offset(0.5F, 15.0F, 1.0F));
        root.addOrReplaceChild("neck1",
                CubeListBuilder.create().texOffs(20, 45).addBox(-1.5F, -2.0F, -1.0F, 3.0F, 4.0F, 4.0F),
                PartPose.offsetAndRotation(0.0F, 16.0F, -5.0F, 0.715585F, 0.0F, 0.0F));
        root.addOrReplaceChild("neck",
                CubeListBuilder.create().texOffs(20, 31).addBox(-1.5F, -8.0F, -1.0F, 3.0F, 8.0F, 3.0F),
                PartPose.offset(0.0F, 15.0F, -6.0F));
        root.addOrReplaceChild("neckbase",
                CubeListBuilder.create().texOffs(0, 76).addBox(-4.5F, -4.0F, 0.0F, 9.0F, 6.0F, 3.0F),
                PartPose.offset(0.0F, 17.0F, 5.0F));
        root.addOrReplaceChild("head",
                CubeListBuilder.create().texOffs(20, 20).addBox(-2.0F, -3.0F, -3.5F, 4.0F, 4.0F, 5.0F),
                PartPose.offset(0.0F, 8.0F, -6.0F));
        root.addOrReplaceChild("Rleg1",
                CubeListBuilder.create().texOffs(0, 58).addBox(-1.5F, 0.0F, -2.0F, 3.0F, 8.0F, 4.0F),
                PartPose.offset(6.0F, 16.0F, 1.0F));
        root.addOrReplaceChild("Lleg1",
                CubeListBuilder.create().texOffs(15, 58).addBox(-1.5F, 0.0F, -2.0F, 3.0F, 8.0F, 4.0F),
                PartPose.offset(-6.0F, 16.0F, 1.0F));
        root.addOrReplaceChild("snout",
                CubeListBuilder.create().texOffs(20, 11).addBox(-1.5F, -2.0F, -8.5F, 3.0F, 3.0F, 5.0F),
                PartPose.offset(0.0F, 8.0F, -6.0F));
        root.addOrReplaceChild("tail1",
                CubeListBuilder.create().texOffs(59, 55).addBox(-6.0F, -3.0F, -3.0F, 12.0F, 5.0F, 3.0F),
                PartPose.offset(0.0F, 16.5F, -2.0F));
        root.addOrReplaceChild("tail2",
                CubeListBuilder.create().texOffs(0, 86).addBox(-3.0F, -2.5F, 0.0F, 6.0F, 4.0F, 7.0F),
                PartPose.offsetAndRotation(0.0F, 16.0F, 7.0F, -0.3839724F, 0.0F, 0.0F));
        root.addOrReplaceChild("tail3",
                CubeListBuilder.create().texOffs(0, 98).addBox(-2.0F, -2.0F, 0.0F, 4.0F, 3.0F, 6.0F),
                PartPose.offsetAndRotation(0.0F, 18.2F, 13.0F, -0.2094395F, 0.0F, 0.0F));
        root.addOrReplaceChild("tail4",
                CubeListBuilder.create().texOffs(0, 108).addBox(-1.5F, -1.5F, 0.0F, 3.0F, 2.0F, 5.0F),
                PartPose.offsetAndRotation(0.0F, 19.5F, 18.0F, -0.0698132F, 0.0F, 0.0F));
        root.addOrReplaceChild("Lwing",
                CubeListBuilder.create().texOffs(59, 0).addBox(-22.0F, 0.0F, -3.0F, 22.0F, 0.0F, 10.0F),
                PartPose.offsetAndRotation(-6.0F, 12.6F, 0.0F, 0.0F, 0.0F, 0.4014257F));
        root.addOrReplaceChild("Rwing",
                CubeListBuilder.create().texOffs(59, 66).addBox(0.0F, 0.0F, -3.0F, 22.0F, 0.0F, 10.0F),
                PartPose.offsetAndRotation(6.0F, 12.6F, 0.0F, 0.0F, 0.0F, -0.4014257F));
        root.addOrReplaceChild("Tail5",
                CubeListBuilder.create().texOffs(0, 116).addBox(-3.0F, 0.0F, 0.0F, 6.0F, 2.0F, 4.0F),
                PartPose.offset(0.0F, 18.0F, 22.0F));
        root.addOrReplaceChild("Tail6",
                CubeListBuilder.create().texOffs(0, 123).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 2.0F, 2.0F),
                PartPose.offset(0.0F, 18.0F, 26.0F));
        root.addOrReplaceChild("Lneck1",
                CubeListBuilder.create().texOffs(0, 45).addBox(-1.5F, -2.0F, -1.0F, 3.0F, 4.0F, 4.0F),
                PartPose.offsetAndRotation(4.5F, 16.0F, -5.0F, 0.715585F, 0.0F, 0.0F));
        root.addOrReplaceChild("Lneck",
                CubeListBuilder.create().texOffs(0, 30).addBox(-1.5F, -8.0F, -1.0F, 3.0F, 8.0F, 3.0F),
                PartPose.offset(4.5F, 15.0F, -6.0F));
        root.addOrReplaceChild("Lhead",
                CubeListBuilder.create().texOffs(0, 20).addBox(-2.0F, -3.0F, -3.5F, 4.0F, 4.0F, 5.0F),
                PartPose.offsetAndRotation(4.5F, 8.0F, -6.0F, -0.0174533F, 0.0F, 0.0F));
        root.addOrReplaceChild("Lsnout",
                CubeListBuilder.create().texOffs(0, 11).addBox(-1.5F, -2.0F, -8.5F, 3.0F, 3.0F, 5.0F),
                PartPose.offset(4.5F, 8.0F, -6.0F));
        root.addOrReplaceChild("Rneck1",
                CubeListBuilder.create().texOffs(40, 45).addBox(-1.5F, -2.0F, -1.0F, 3.0F, 4.0F, 4.0F),
                PartPose.offsetAndRotation(-4.5F, 16.0F, -5.0F, 0.715585F, 0.0F, 0.0F));
        root.addOrReplaceChild("Rneck",
                CubeListBuilder.create().texOffs(40, 31).addBox(-1.5F, -8.0F, -1.0F, 3.0F, 8.0F, 3.0F),
                PartPose.offset(-4.5F, 15.0F, -6.0F));
        root.addOrReplaceChild("Rhead",
                CubeListBuilder.create().texOffs(40, 20).addBox(-2.0F, -3.0F, -3.5F, 4.0F, 4.0F, 5.0F),
                PartPose.offset(-4.5F, 8.0F, -6.0F));
        root.addOrReplaceChild("Rsnout",
                CubeListBuilder.create().texOffs(40, 11).addBox(-1.5F, -2.0F, -8.5F, 3.0F, 3.0F, 5.0F),
                PartPose.offset(-4.5F, 8.0F, -6.0F));
        root.addOrReplaceChild("headfin",
                CubeListBuilder.create().texOffs(20, 0).addBox(-0.5F, -3.0F, 1.0F, 1.0F, 4.0F, 3.0F),
                PartPose.offsetAndRotation(0.0F, 8.0F, -6.0F, -0.122173F, 0.0F, 0.0F));
        root.addOrReplaceChild("Lheadfin",
                CubeListBuilder.create().texOffs(0, 0).addBox(-0.5F, -3.0F, 1.0F, 1.0F, 4.0F, 3.0F),
                PartPose.offsetAndRotation(4.5F, 8.0F, -6.0F, -0.122173F, 0.0F, 0.0F));
        root.addOrReplaceChild("Rheadfin",
                CubeListBuilder.create().texOffs(40, 0).addBox(-0.5F, -3.0F, 1.0F, 1.0F, 4.0F, 3.0F),
                PartPose.offsetAndRotation(-4.5F, 8.0F, -6.0F, -0.122173F, 0.0F, 0.0F));
        root.addOrReplaceChild("Backfin",
                CubeListBuilder.create().texOffs(35, 57).addBox(-0.5F, 0.0F, 0.0F, 1.0F, 3.0F, 5.0F),
                PartPose.offsetAndRotation(0.0F, 12.0F, -1.0F, 0.5061455F, 0.0F, 0.0F));
        root.addOrReplaceChild("Rwing2",
                CubeListBuilder.create().texOffs(59, 77).addBox(0.0F, 0.0F, -3.0F, 12.0F, 0.0F, 10.0F),
                PartPose.offsetAndRotation(6.0F, 12.6F, 0.0F, 0.0F, 0.0F, (float) (-Math.PI * 2.0 / 9.0)));
        root.addOrReplaceChild("Rwing3",
                CubeListBuilder.create().texOffs(59, 88).addBox(0.0F, 0.0F, -3.0F, 10.0F, 0.0F, 10.0F),
                PartPose.offsetAndRotation(6.0F, 12.6F, 0.0F, 0.0F, 0.0F, -0.0698132F));
        root.addOrReplaceChild("Lwing2",
                CubeListBuilder.create().texOffs(59, 11).addBox(-12.0F, 0.0F, -3.0F, 12.0F, 0.0F, 10.0F),
                PartPose.offsetAndRotation(-6.0F, 12.6F, 0.0F, 0.0F, 0.0F, (float) (Math.PI * 2.0 / 9.0)));
        root.addOrReplaceChild("Lwing3",
                CubeListBuilder.create().texOffs(59, 22).addBox(-10.0F, 0.0F, -3.0F, 10.0F, 0.0F, 10.0F),
                PartPose.offsetAndRotation(-6.0F, 12.6F, 0.0F, 0.0F, 0.0F, 0.0698132F));
        root.addOrReplaceChild("Ljaw",
                CubeListBuilder.create().texOffs(30, 70).addBox(-1.5F, 1.0F, -5.0F, 3.0F, 1.0F, 5.0F),
                PartPose.offsetAndRotation(4.5F, 8.0F, -7.0F, 0.2443461F, 0.0F, 0.0F));
        root.addOrReplaceChild("jaw",
                CubeListBuilder.create().texOffs(30, 80).addBox(-1.5F, 1.0F, -5.0F, 3.0F, 1.0F, 5.0F),
                PartPose.offsetAndRotation(0.0F, 8.0F, -7.0F, 0.2443461F, 0.0F, 0.0F));
        root.addOrReplaceChild("Rjaw",
                CubeListBuilder.create().texOffs(30, 90).addBox(-1.5F, 1.0F, -5.0F, 3.0F, 1.0F, 5.0F),
                PartPose.offsetAndRotation(-4.5F, 8.0F, -7.0F, 0.2443461F, 0.0F, 0.0F));

        return LayerDefinition.create(mesh, 128, 128);
    }

    @Override
    public ModelPart root() {
        return this.root;
    }

    /**
     * Gold {@code func_78088_a} (empty {@code func_78087_a}):
     * wings zRot idle/move; legs xRot walk or fly-tuck; serpentine tail chain;
     * 3-head look split; jaw attack chomp; neck extension from head1/2/3ext.
     * Mapping: field_78795_f→xRot, field_78796_g→yRot, field_78808_h→zRot,
     * field_78800_c→x, field_78797_d→y, field_78798_e→z.
     */
    @Override
    public void setupAnim(
            ThePrince entity,
            float limbSwing,
            float limbSwingAmount,
            float ageInTicks,
            float netHeadYaw,
            float headPitch) {
        float f1 = limbSwingAmount;
        float f2 = ageInTicks;
        float f3 = netHeadYaw;
        float f4 = headPitch;
        float newangle;
        int current_activity = entity.getActivity();

        // --- wings ---
        if (!(f1 > 0.1F) && entity.getAttacking() == 0) {
            newangle = Mth.cos(f2 * 0.3F * this.wingspeed) * (float) Math.PI * 0.04F;
        } else {
            newangle = Mth.cos(f2 * 2.3F * this.wingspeed) * (float) Math.PI * 0.4F * f1;
        }
        this.Rwing.zRot = newangle - 0.4F;
        this.Rwing2.zRot = newangle - 0.6F;
        this.Rwing3.zRot = newangle - 0.2F;
        this.Lwing.zRot = -newangle + 0.4F;
        this.Lwing2.zRot = -newangle + 0.6F;
        this.Lwing3.zRot = -newangle + 0.2F;

        // --- legs ---
        if (f1 > 0.1F) {
            newangle = Mth.cos(f2 * 2.0F * this.wingspeed) * (float) Math.PI * 0.25F * f1;
        } else {
            newangle = 0.0F;
        }
        if (current_activity == 2 && entity.getAttacking() == 0) {
            newangle = -1.0F;
            this.Rleg1.xRot = newangle;
            this.Lleg1.xRot = newangle;
        } else {
            this.Rleg1.xRot = newangle;
            this.Lleg1.xRot = -newangle;
        }

        // --- tail sway ---
        newangle = Mth.cos(f2 * 0.9F * this.wingspeed) * (float) Math.PI * 0.06F;
        if (entity.isOreSpawnSitting()) {
            newangle = 0.0F;
        }
        if (entity.getAttacking() != 0) {
            newangle = Mth.cos(f2 * 1.3F * this.wingspeed) * (float) Math.PI * 0.12F;
        }

        // reset tail2 base xz each frame (gold only rewrites yRot on tail2)
        this.tail2.x = TAIL2_X;
        this.tail2.z = TAIL2_Z;
        this.tail2.yRot = newangle;
        this.tail3.z = this.tail2.z + (float) Math.cos(this.tail2.yRot) * 6.0F;
        this.tail3.x = this.tail2.x + (float) Math.sin(this.tail2.yRot) * 6.0F;
        this.tail3.yRot = newangle * 1.6F;
        this.tail4.z = this.tail3.z + (float) Math.cos(this.tail3.yRot) * 5.0F;
        this.tail4.x = this.tail3.x + (float) Math.sin(this.tail3.yRot) * 5.0F;
        this.tail4.yRot = newangle * 2.6F;
        this.Tail5.z = this.tail4.z + (float) Math.cos(this.tail4.yRot) * 4.0F;
        this.Tail5.x = this.tail4.x + (float) Math.sin(this.tail4.yRot) * 4.0F;
        this.Tail5.yRot = newangle * 3.6F;
        this.Tail6.z = this.Tail5.z + (float) Math.cos(this.Tail5.yRot) * 4.0F;
        this.Tail6.x = this.Tail5.x + (float) Math.sin(this.Tail5.yRot) * 4.0F;
        this.Tail6.yRot = newangle * 4.6F;

        // --- multi-head look split (gold h1/h2/h3, d1/d2/d3) ---
        float h3;
        float h1 = h3 = f3 * 2.0F / 3.0F;
        float d3;
        float d1 = d3 = f4 * 2.0F / 3.0F;
        float h2;
        float d2;
        if (h1 < 0.0F) {
            h2 = h3 = h1 / 2.0F;
            d2 = d3 = d1 / 2.0F;
        } else {
            h2 = h1 = h3 / 2.0F;
            d2 = d1 = d3 / 2.0F;
        }

        // reset snout base x/z for jaw follow
        this.snout.x = SNOUT_X;
        this.snout.z = SNOUT_Z;
        this.Lsnout.x = LSNOUT_X;
        this.Lsnout.z = SNOUT_Z;
        this.Rsnout.x = RSNOUT_X;
        this.Rsnout.z = SNOUT_Z;

        this.head.yRot = (float) Math.toRadians(h2);
        this.snout.yRot = (float) Math.toRadians(h2);
        this.headfin.yRot = (float) Math.toRadians(h2);
        this.jaw.yRot = (float) Math.toRadians(h2);
        this.jaw.z = this.snout.z - (float) Math.cos(this.snout.yRot);
        this.jaw.x = this.snout.x - (float) Math.sin(this.snout.yRot);
        this.neck.yRot = (float) Math.toRadians(h2) / 2.0F;

        this.Lhead.yRot = (float) Math.toRadians(h1);
        this.Lsnout.yRot = (float) Math.toRadians(h1);
        this.Lheadfin.yRot = (float) Math.toRadians(h1);
        this.Ljaw.yRot = (float) Math.toRadians(h1);
        this.Ljaw.z = this.Lsnout.z - (float) Math.cos(this.Lsnout.yRot);
        this.Ljaw.x = this.Lsnout.x - (float) Math.sin(this.Lsnout.yRot);
        this.Lneck.yRot = (float) Math.toRadians(h1) / 2.0F;

        this.Rhead.yRot = (float) Math.toRadians(h3);
        this.Rsnout.yRot = (float) Math.toRadians(h3);
        this.Rheadfin.yRot = (float) Math.toRadians(h3);
        this.Rjaw.yRot = (float) Math.toRadians(h3);
        this.Rjaw.z = this.Rsnout.z - (float) Math.cos(this.Rsnout.yRot);
        this.Rjaw.x = this.Rsnout.x - (float) Math.sin(this.Rsnout.yRot);
        this.Rneck.yRot = (float) Math.toRadians(h3) / 2.0F;

        float Rjx = 0.0F;
        float jx = 0.0F;
        float Ljx = 0.0F;
        if (entity.getAttacking() != 0) {
            newangle = Mth.cos(f2 * 1.9F * this.wingspeed) * (float) Math.PI * 0.2F;
            Ljx = 0.2F + newangle;
            newangle = Mth.cos(f2 * 2.1F * this.wingspeed) * (float) Math.PI * 0.2F;
            Rjx = 0.2F + newangle;
            newangle = Mth.cos(f2 * 2.3F * this.wingspeed) * (float) Math.PI * 0.2F;
            jx = 0.2F + newangle;
        }

        this.head.xRot = (float) Math.toRadians(d2);
        this.snout.xRot = (float) Math.toRadians(d2);
        this.headfin.xRot = (float) Math.toRadians(d2);
        this.jaw.xRot = (float) Math.toRadians(d2) + jx;
        this.Lhead.xRot = (float) Math.toRadians(d1);
        this.Lsnout.xRot = (float) Math.toRadians(d1);
        this.Lheadfin.xRot = (float) Math.toRadians(d1);
        this.Ljaw.xRot = (float) Math.toRadians(d1) + Ljx;
        this.Rhead.xRot = (float) Math.toRadians(d3);
        this.Rsnout.xRot = (float) Math.toRadians(d3);
        this.Rheadfin.xRot = (float) Math.toRadians(d3);
        this.Rjaw.xRot = (float) Math.toRadians(d3) + Rjx;

        // neck extension from entity head1/2/3ext (degrees)
        d1 = entity.getHead1Ext();
        d2 = entity.getHead2Ext();
        d3 = entity.getHead3Ext();
        this.Lneck.x = LNECK_X;
        this.Lneck.y = LNECK_Y;
        this.Lneck.z = LNECK_Z;
        this.neck.x = NECK_X;
        this.neck.y = NECK_Y;
        this.neck.z = NECK_Z;
        this.Rneck.x = RNECK_X;
        this.Rneck.y = RNECK_Y;
        this.Rneck.z = RNECK_Z;

        this.Lneck.xRot = (float) Math.toRadians(d1);
        this.neck.xRot = (float) Math.toRadians(d2);
        this.Rneck.xRot = (float) Math.toRadians(d3);

        this.Lhead.y = this.Lneck.y - (float) Math.cos(this.Lneck.xRot) * 7.0F;
        this.Lheadfin.y = this.Lsnout.y = this.Ljaw.y = this.Lhead.y;
        this.Lhead.z = this.Lneck.z - (float) Math.sin(this.Lneck.xRot) * 7.0F;
        this.Lheadfin.z = this.Lsnout.z = this.Ljaw.z = this.Lhead.z;
        this.Lhead.x = this.Lneck.x
                - (float) Math.sin(this.Lneck.yRot) * 7.0F * (float) Math.sin(this.Lneck.xRot);
        this.Lheadfin.x = this.Lsnout.x = this.Ljaw.x = this.Lhead.x;

        this.Rhead.y = this.Rneck.y - (float) Math.cos(this.Rneck.xRot) * 7.0F;
        this.Rheadfin.y = this.Rsnout.y = this.Rjaw.y = this.Rhead.y;
        this.Rhead.z = this.Rneck.z - (float) Math.sin(this.Rneck.xRot) * 7.0F;
        this.Rheadfin.z = this.Rsnout.z = this.Rjaw.z = this.Rhead.z;
        this.Rhead.x = this.Rneck.x
                - (float) Math.sin(this.Rneck.yRot) * 7.0F * (float) Math.sin(this.Rneck.xRot);
        this.Rheadfin.x = this.Rsnout.x = this.Rjaw.x = this.Rhead.x;

        this.head.y = this.neck.y - (float) Math.cos(this.neck.xRot) * 7.0F;
        this.headfin.y = this.snout.y = this.jaw.y = this.head.y;
        this.head.z = this.neck.z - (float) Math.sin(this.neck.xRot) * 7.0F;
        this.headfin.z = this.snout.z = this.jaw.z = this.head.z;
        this.head.x = this.neck.x
                - (float) Math.sin(this.neck.yRot) * 7.0F * (float) Math.sin(this.neck.xRot);
        this.headfin.x = this.snout.x = this.jaw.x = this.head.x;

        // gold jaw z/x follow after head placement: re-apply snout-relative jaw offset
        this.jaw.z = this.snout.z - (float) Math.cos(this.snout.yRot);
        this.jaw.x = this.snout.x - (float) Math.sin(this.snout.yRot);
        this.Ljaw.z = this.Lsnout.z - (float) Math.cos(this.Lsnout.yRot);
        this.Ljaw.x = this.Lsnout.x - (float) Math.sin(this.Lsnout.yRot);
        this.Rjaw.z = this.Rsnout.z - (float) Math.cos(this.Rsnout.yRot);
        this.Rjaw.x = this.Rsnout.x - (float) Math.sin(this.Rsnout.yRot);
    }
}
