package danger.orespawn.client.model;

import danger.orespawn.entity.Stinky;
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
 * Port of gold {@code ModelStinky} (1.7.10 ModelBase) → 1.21 HierarchicalModel.
 * Cubes/UVs 1:1. Texture 128×64. Wingspeed default 0.65F matches ClientProxy.
 */
@OnlyIn(Dist.CLIENT)
public class ModelStinky extends HierarchicalModel<Stinky> {
    /** Model layer location. */
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
            ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "stinky"), "main");

    private final float wingspeed;
    private final ModelPart root;
    private final ModelPart body;
    private final ModelPart neck1;
    private final ModelPart neck;
    private final ModelPart neckbase;
    private final ModelPart head;
    private final ModelPart Rleg1;
    private final ModelPart Lleg1;
    private final ModelPart Lhorn1;
    private final ModelPart Rhorn1;
    private final ModelPart snout;
    private final ModelPart Lhorn2;
    private final ModelPart Rhorn2;
    private final ModelPart tail1;
    private final ModelPart Rleg2;
    private final ModelPart Lleg2;
    private final ModelPart tail2;
    private final ModelPart tail3;
    private final ModelPart tail4;
    private final ModelPart Lwing;
    private final ModelPart Rwing;

    public ModelStinky(ModelPart root) {
        this(root, 0.65F);
    }

    public ModelStinky(ModelPart root, float wingspeed) {
        this.root = root;
        this.wingspeed = wingspeed;
        this.body = root.getChild("body");
        this.neck1 = root.getChild("neck1");
        this.neck = root.getChild("neck");
        this.neckbase = root.getChild("neckbase");
        this.head = root.getChild("head");
        this.Rleg1 = root.getChild("Rleg1");
        this.Lleg1 = root.getChild("Lleg1");
        this.Lhorn1 = root.getChild("Lhorn1");
        this.Rhorn1 = root.getChild("Rhorn1");
        this.snout = root.getChild("snout");
        this.Lhorn2 = root.getChild("Lhorn2");
        this.Rhorn2 = root.getChild("Rhorn2");
        this.tail1 = root.getChild("tail1");
        this.Rleg2 = root.getChild("Rleg2");
        this.Lleg2 = root.getChild("Lleg2");
        this.tail2 = root.getChild("tail2");
        this.tail3 = root.getChild("tail3");
        this.tail4 = root.getChild("tail4");
        this.Lwing = root.getChild("Lwing");
        this.Rwing = root.getChild("Rwing");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        root.addOrReplaceChild(
                "body",
                CubeListBuilder.create().texOffs(0, 12).addBox(-4.5F, -3.0F, -5.0F, 8.0F, 8.0F, 10.0F),
                PartPose.offset(0.5F, 15.0F, 1.0F));
        root.addOrReplaceChild(
                "neck1",
                CubeListBuilder.create().texOffs(0, 31).addBox(-2.0F, -3.0F, -2.0F, 4.0F, 5.0F, 5.0F),
                PartPose.offsetAndRotation(0.0F, 16.0F, -5.0F, 0.715585F, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "neck",
                CubeListBuilder.create().texOffs(0, 42).addBox(-2.0F, -8.0F, -3.0F, 4.0F, 8.0F, 4.0F),
                PartPose.offset(0.0F, 15.0F, -5.5F));
        root.addOrReplaceChild(
                "neckbase",
                CubeListBuilder.create().texOffs(0, 55).addBox(-3.0F, -4.0F, 0.0F, 6.0F, 6.0F, 3.0F),
                PartPose.offset(0.0F, 17.0F, 5.0F));
        root.addOrReplaceChild(
                "head",
                CubeListBuilder.create().texOffs(0, 0).addBox(-2.5F, -10.0F, -3.5F, 5.0F, 5.0F, 5.0F),
                PartPose.offset(0.0F, 15.0F, -5.5F));
        root.addOrReplaceChild(
                "Rleg1",
                CubeListBuilder.create().texOffs(19, 53).addBox(-1.5F, 0.0F, -1.0F, 3.0F, 8.0F, 3.0F),
                PartPose.offset(2.0F, 16.0F, 5.5F));
        root.addOrReplaceChild(
                "Lleg1",
                CubeListBuilder.create().texOffs(19, 53).addBox(-1.5F, 0.0F, -0.5F, 3.0F, 8.0F, 3.0F),
                PartPose.offset(-2.0F, 16.0F, 5.0F));
        root.addOrReplaceChild(
                "Lhorn1",
                CubeListBuilder.create().texOffs(19, 47).addBox(-3.0F, -10.5F, -1.0F, 2.0F, 2.0F, 3.0F),
                PartPose.offset(0.0F, 15.0F, -5.5F));
        root.addOrReplaceChild(
                "Rhorn1",
                CubeListBuilder.create().texOffs(19, 47).addBox(1.0F, -10.5F, -1.0F, 2.0F, 2.0F, 3.0F),
                PartPose.offset(0.0F, 15.0F, -5.5F));
        root.addOrReplaceChild(
                "snout",
                CubeListBuilder.create().texOffs(32, 57).addBox(-1.5F, -8.0F, -6.5F, 3.0F, 3.0F, 4.0F),
                PartPose.offset(0.0F, 15.0F, -5.5F));
        root.addOrReplaceChild(
                "Lhorn2",
                CubeListBuilder.create().texOffs(19, 42).addBox(-2.5F, -10.0F, 1.0F, 1.0F, 1.0F, 3.0F),
                PartPose.offset(0.0F, 15.0F, -5.5F));
        root.addOrReplaceChild(
                "Rhorn2",
                CubeListBuilder.create().texOffs(19, 42).addBox(1.5F, -10.0F, 1.0F, 1.0F, 1.0F, 3.0F),
                PartPose.offset(0.0F, 15.0F, -5.5F));
        root.addOrReplaceChild(
                "tail1",
                CubeListBuilder.create().texOffs(47, 55).addBox(-3.0F, -3.0F, -3.0F, 6.0F, 6.0F, 3.0F),
                PartPose.offset(0.0F, 16.5F, -2.0F));
        root.addOrReplaceChild(
                "Rleg2",
                CubeListBuilder.create().texOffs(19, 53).addBox(-1.5F, 0.0F, -1.5F, 3.0F, 8.0F, 3.0F),
                PartPose.offset(2.0F, 16.0F, -3.0F));
        root.addOrReplaceChild(
                "Lleg2",
                CubeListBuilder.create().texOffs(19, 53).addBox(-1.5F, 0.0F, -1.5F, 3.0F, 8.0F, 3.0F),
                PartPose.offset(-2.0F, 16.0F, -3.0F));
        // gold rest pose: tail2 xRot -0.3839724, tail3 -0.2094395, tail4 -0.0698132
        root.addOrReplaceChild(
                "tail2",
                CubeListBuilder.create().texOffs(19, 31).addBox(-2.5F, -2.5F, 0.0F, 5.0F, 5.0F, 5.0F),
                PartPose.offsetAndRotation(0.0F, 16.0F, 7.0F, -0.3839724F, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "tail3",
                CubeListBuilder.create().texOffs(32, 46).addBox(-2.0F, -2.0F, 0.0F, 4.0F, 4.0F, 4.0F),
                PartPose.offsetAndRotation(0.0F, 17.2F, 11.0F, -0.2094395F, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "tail4",
                CubeListBuilder.create().texOffs(37, 13).addBox(-1.5F, -1.5F, 0.0F, 3.0F, 3.0F, 5.0F),
                PartPose.offsetAndRotation(0.0F, 17.5F, 14.0F, -0.0698132F, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "Lwing",
                CubeListBuilder.create().texOffs(59, 0).addBox(-18.0F, 0.0F, -5.0F, 18.0F, 0.0F, 10.0F),
                PartPose.offsetAndRotation(-2.0F, 12.6F, 0.0F, 0.0F, 0.0F, 0.4014257F));
        root.addOrReplaceChild(
                "Rwing",
                CubeListBuilder.create().texOffs(59, 11).addBox(0.0F, 0.0F, -5.0F, 18.0F, 0.0F, 10.0F),
                PartPose.offsetAndRotation(2.0F, 12.6F, 0.0F, 0.0F, 0.0F, -0.4014257F));

        return LayerDefinition.create(mesh, 128, 64);
    }

    @Override
    public ModelPart root() {
        return this.root;
    }

    @Override
    public void setupAnim(
            Stinky entity,
            float limbSwing,
            float limbSwingAmount,
            float ageInTicks,
            float netHeadYaw,
            float headPitch) {
        animate(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
    }

    /**
     * Gold ModelStinky.render anim block — wings, legs by activity, chain tail, head look.
     */
    private void animate(Stinky entity, float f, float f1, float f2, float f3, float f4) {
        float newangle;
        int current_activity = entity.getActivity();

        // wings always flap with limb amount (gold cos 2.3 * wingspeed * PI * 0.4 * f1)
        if (f1 > 0.1F) {
            newangle = Mth.cos(f2 * 2.3F * this.wingspeed) * (float) Math.PI * 0.4F * f1;
        } else {
            newangle = 0.0F;
        }
        this.Rwing.zRot = newangle - 0.4F;
        this.Lwing.zRot = -newangle + 0.4F;

        // legs: walk cycle on ground; tucked when flying (activity == 2)
        if (f1 > 0.1F) {
            newangle = Mth.cos(f2 * 2.0F * this.wingspeed) * (float) Math.PI * 0.25F * f1;
        } else {
            newangle = 0.0F;
        }
        if (current_activity != 2) {
            this.Rleg1.xRot = newangle;
            this.Lleg1.xRot = -newangle;
            this.Rleg2.xRot = -newangle;
            this.Lleg2.xRot = newangle;
        } else {
            // gold fly pose: front legs (leg2) tuck -1, rear (leg1) stretch +1
            this.Rleg2.xRot = -1.0F;
            this.Lleg2.xRot = -1.0F;
            this.Rleg1.xRot = 1.0F;
            this.Lleg1.xRot = 1.0F;
        }

        // chain tail yaw wag; freeze when sitting
        newangle = Mth.cos(f2 * 1.0F * this.wingspeed) * (float) Math.PI * 0.2F;
        if (entity.isOreSpawnSitting()) {
            newangle = 0.0F;
        }
        // rest x offsets from gold setRotation
        float tail2Z = 7.0F;
        float tail2X = 0.0F;
        this.tail2.yRot = newangle;
        this.tail3.z = tail2Z + (float) Math.cos(this.tail2.yRot) * 4.0F;
        this.tail3.x = tail2X + (float) Math.sin(this.tail2.yRot) * 4.0F - 0.5F;
        this.tail3.yRot = newangle * 1.6F;
        this.tail4.z = this.tail3.z + (float) Math.cos(this.tail3.yRot) * 3.0F;
        this.tail4.x = this.tail3.x + (float) Math.sin(this.tail3.yRot) * 3.0F - 0.5F;
        this.tail4.yRot = newangle * 2.6F;

        // head look (gold full yaw for head/snout/horns; neck half; pitch /3)
        float headY = (float) Math.toRadians(f3);
        float headX = (float) Math.toRadians(f4) / 3.0F;
        this.head.yRot = headY;
        this.snout.yRot = headY;
        this.neck.yRot = headY / 2.0F;
        this.Rhorn1.yRot = headY;
        this.Rhorn2.yRot = headY;
        this.Lhorn1.yRot = headY;
        this.Lhorn2.yRot = headY;
        this.head.xRot = headX;
        this.snout.xRot = headX;
        this.neck.xRot = headX;
        this.Rhorn1.xRot = headX;
        this.Rhorn2.xRot = headX;
        this.Lhorn1.xRot = headX;
        this.Lhorn2.xRot = headX;
    }
}
