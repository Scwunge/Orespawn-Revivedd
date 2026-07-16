package danger.orespawn.client.model;

import danger.orespawn.entity.Frog;
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
 * Port of gold {@code ModelFrog} (1.7.10 ModelBase) → 1.21 HierarchicalModel.
 * Cubes/UVs 1:1. Texture 64×64. Wingspeed default 1.0F matches ClientProxy.
 */
@OnlyIn(Dist.CLIENT)
public class ModelFrog extends HierarchicalModel<Frog> {
    /** Model layer location. */
    public static final ModelLayerLocation LAYER_LOCATION =
            new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "frog"), "main");

    private static final float LLEG1_BASE_X = 3.0F;
    private static final float LLEG1_BASE_Y = 24.0F;
    private static final float RLEG1_BASE_X = -3.0F;
    private static final float RLEG1_BASE_Y = 24.0F;

    private final float wingspeed;
    private final ModelPart root;
    private final ModelPart body;
    private final ModelPart jaw;
    private final ModelPart lfleg;
    private final ModelPart rfleg;
    private final ModelPart lleg1;
    private final ModelPart rleg1;
    private final ModelPart lleg2;
    private final ModelPart rleg2;
    private final ModelPart leye;
    private final ModelPart reye;

    public ModelFrog(ModelPart root) {
        this(root, 1.0F);
    }

    public ModelFrog(ModelPart root, float wingspeed) {
        this.root = root;
        this.wingspeed = wingspeed;
        this.body = root.getChild("body");
        this.jaw = root.getChild("jaw");
        this.lfleg = root.getChild("lfleg");
        this.rfleg = root.getChild("rfleg");
        this.lleg1 = root.getChild("lleg1");
        this.rleg1 = root.getChild("rleg1");
        this.lleg2 = root.getChild("lleg2");
        this.rleg2 = root.getChild("rleg2");
        this.leye = root.getChild("leye");
        this.reye = root.getChild("reye");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        root.addOrReplaceChild(
                "body",
                CubeListBuilder.create().texOffs(41, 0).addBox(-4.0F, -10.0F, 0.0F, 8.0F, 11.0F, 2.0F),
                PartPose.offsetAndRotation(0.0F, 24.0F, 2.0F, 0.7330383F, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "jaw",
                CubeListBuilder.create().texOffs(42, 15).addBox(-4.0F, -8.0F, 0.0F, 8.0F, 8.0F, 1.0F),
                PartPose.offsetAndRotation(0.0F, 24.0F, 2.0F, 1.22173F, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "lfleg",
                CubeListBuilder.create().texOffs(14, 0).addBox(0.0F, 0.0F, 0.0F, 1.0F, 5.0F, 1.0F),
                PartPose.offsetAndRotation(3.0F, 20.0F, 0.0F, (float) (-Math.PI / 6), 0.0F, -0.4712389F));
        root.addOrReplaceChild(
                "rfleg",
                CubeListBuilder.create().texOffs(20, 0).addBox(-1.0F, 0.0F, 0.0F, 1.0F, 5.0F, 1.0F),
                PartPose.offsetAndRotation(-3.0F, 20.0F, 0.0F, (float) (-Math.PI / 6), 0.0F, 0.4712389F));
        root.addOrReplaceChild(
                "lleg1",
                CubeListBuilder.create().texOffs(10, 8).addBox(0.0F, -9.0F, -1.0F, 1.0F, 9.0F, 2.0F),
                PartPose.offsetAndRotation(3.0F, 24.0F, 3.0F, 0.0F, 0.0F, 0.2268928F));
        root.addOrReplaceChild(
                "rleg1",
                CubeListBuilder.create().texOffs(18, 8).addBox(-1.0F, -9.0F, -1.0F, 1.0F, 9.0F, 2.0F),
                PartPose.offsetAndRotation(-3.0F, 24.0F, 3.0F, 0.0F, 0.0F, -0.2268928F));
        root.addOrReplaceChild(
                "lleg2",
                CubeListBuilder.create().texOffs(11, 20).addBox(0.0F, 0.0F, 0.0F, 1.0F, 10.0F, 1.0F),
                PartPose.offsetAndRotation(5.0F, 15.0F, 3.0F, 0.0F, 0.0F, -0.3839724F));
        root.addOrReplaceChild(
                "rleg2",
                CubeListBuilder.create().texOffs(19, 20).addBox(-1.0F, 0.0F, 0.0F, 1.0F, 10.0F, 1.0F),
                PartPose.offsetAndRotation(-5.0F, 15.0F, 3.0F, 0.0F, 0.0F, 0.3839724F));
        root.addOrReplaceChild(
                "leye",
                CubeListBuilder.create().texOffs(0, 8).addBox(0.0F, 0.0F, 0.0F, 1.0F, 2.0F, 1.0F),
                PartPose.offsetAndRotation(2.0F, 17.0F, -2.0F, 0.7330383F, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "reye",
                CubeListBuilder.create().texOffs(0, 4).addBox(0.0F, 0.0F, 0.0F, 1.0F, 2.0F, 1.0F),
                PartPose.offsetAndRotation(-3.0F, 17.0F, -2.0F, 0.7330383F, 0.0F, 0.0F));

        return LayerDefinition.create(mesh, 64, 64);
    }

    @Override
    public ModelPart root() {
        return this.root;
    }

    @Override
    public void setupAnim(
            Frog entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        animate(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
    }

    private void animate(Frog entity, float f, float f1, float f2, float f3, float f4) {
        float newangle;
        if (f1 > 0.1F) {
            newangle = Mth.cos(f2 * this.wingspeed * 1.4F) * (float) Math.PI * 0.55F * f1;
        } else {
            newangle = 0.0F;
        }

        this.lfleg.yRot = newangle;
        this.rfleg.yRot = -newangle;
        this.lleg2.yRot = -newangle / 2.0F;
        this.rleg2.yRot = newangle / 2.0F;

        if (entity.getSinging() != 0) {
            newangle = Mth.cos(f2 * 0.85F * this.wingspeed) * (float) Math.PI * 0.15F;
        } else {
            newangle = 0.0F;
        }
        this.jaw.xRot = newangle + 1.22F;

        // gold: if motionY outside ±0.1, flip back legs out
        double dy = entity.getDeltaMovement().y;
        if (!(dy > 0.1) && !(dy < -0.1)) {
            this.lleg1.zRot = 0.227F;
            this.rleg1.zRot = -0.227F;
        } else {
            this.lleg1.zRot = 2.44F;
            this.rleg1.zRot = -2.44F;
        }

        // gold: hinge lower legs from upper leg tips
        this.lleg1.x = LLEG1_BASE_X;
        this.lleg1.y = LLEG1_BASE_Y;
        this.rleg1.x = RLEG1_BASE_X;
        this.rleg1.y = RLEG1_BASE_Y;
        this.lleg2.y = this.lleg1.y - (float) Math.cos(this.lleg1.zRot) * 9.0F;
        this.lleg2.x = this.lleg1.x + (float) Math.sin(this.lleg1.zRot) * 9.0F;
        this.rleg2.y = this.rleg1.y - (float) Math.cos(this.rleg1.zRot) * 9.0F;
        this.rleg2.x = this.rleg1.x + (float) Math.sin(this.rleg1.zRot) * 9.0F;
    }
}
