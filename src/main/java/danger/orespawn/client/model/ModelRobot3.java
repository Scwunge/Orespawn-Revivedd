package danger.orespawn.client.model;

import danger.orespawn.entity.RenderInfo;
import danger.orespawn.entity.Robot3;
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
 * Port of gold {@code ModelRobot3} (1.7.10 ModelBase, tex 512×512) → 1.21 HierarchicalModel.
 * Cubes/UVs/offsets 1:1. ClientProxy wingspeed {@code 1.0F}.
 * Full gold anim: walk legs, lazer yRot from head yaw, arm phase via {@link RenderInfo#ri1}.
 */
@OnlyIn(Dist.CLIENT)
public class ModelRobot3 extends HierarchicalModel<Robot3> {
    public static final ModelLayerLocation LAYER_LOCATION =
            new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "robot3"), "main");

    private final float wingspeed;
    private final ModelPart root;
    private final ModelPart rleg1;
    private final ModelPart lleg1;
    private final ModelPart rleg2;
    private final ModelPart lleg2;
    private final ModelPart hips;
    private final ModelPart waist1;
    private final ModelPart waist2;
    private final ModelPart body3;
    private final ModelPart lazer;
    private final ModelPart body2;
    private final ModelPart body1;
    private final ModelPart body4;
    private final ModelPart waist3;
    private final ModelPart larm3;
    private final ModelPart rarm3;
    private final ModelPart larm2;
    private final ModelPart rarm2;
    private final ModelPart larm1;
    private final ModelPart rarm1;

    public ModelRobot3(ModelPart root) {
        this(root, 1.0F);
    }

    public ModelRobot3(ModelPart root, float wingspeed) {
        this.root = root;
        this.wingspeed = wingspeed;
        this.rleg1 = root.getChild("rleg1");
        this.lleg1 = root.getChild("lleg1");
        this.rleg2 = root.getChild("rleg2");
        this.lleg2 = root.getChild("lleg2");
        this.hips = root.getChild("hips");
        this.waist1 = root.getChild("waist1");
        this.waist2 = root.getChild("waist2");
        this.body3 = root.getChild("body3");
        this.lazer = root.getChild("lazer");
        this.body2 = root.getChild("body2");
        this.body1 = root.getChild("body1");
        this.body4 = root.getChild("body4");
        this.waist3 = root.getChild("waist3");
        this.larm3 = root.getChild("larm3");
        this.rarm3 = root.getChild("rarm3");
        this.larm2 = root.getChild("larm2");
        this.rarm2 = root.getChild("rarm2");
        this.larm1 = root.getChild("larm1");
        this.rarm1 = root.getChild("rarm1");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        root.addOrReplaceChild("rleg1",
                CubeListBuilder.create().texOffs(20, 100)
                        .addBox(-23.0F, 26.0F, -8.0F, 16.0F, 29.0F, 16.0F),
                PartPose.offset(-9.0F, -31.0F, 0.0F));
        root.addOrReplaceChild("lleg1",
                CubeListBuilder.create().texOffs(20, 159)
                        .addBox(7.0F, 25.0F, -8.0F, 16.0F, 29.0F, 16.0F),
                PartPose.offset(9.0F, -30.0F, 0.0F));
        root.addOrReplaceChild("rleg2",
                CubeListBuilder.create().texOffs(20, 212)
                        .addBox(-14.0F, 0.0F, -7.0F, 14.0F, 29.0F, 14.0F),
                PartPose.offsetAndRotation(-9.0F, -31.0F, 0.0F, 0.0F, 0.0F, 0.2792527F));
        root.addOrReplaceChild("lleg2",
                CubeListBuilder.create().texOffs(20, 265)
                        .addBox(0.0F, 0.0F, -7.0F, 13.0F, 29.0F, 14.0F),
                PartPose.offsetAndRotation(9.0F, -31.0F, 0.0F, 0.0F, 0.0F, -0.2792527F));
        root.addOrReplaceChild("hips",
                CubeListBuilder.create().texOffs(20, 316)
                        .addBox(0.0F, 0.0F, 0.0F, 18.0F, 16.0F, 16.0F),
                PartPose.offset(-9.0F, -43.0F, -8.0F));
        root.addOrReplaceChild("waist1",
                CubeListBuilder.create().texOffs(20, 359)
                        .addBox(0.0F, 0.0F, 0.0F, 12.0F, 12.0F, 12.0F),
                PartPose.offsetAndRotation(-6.0F, -55.0F, -4.0F, -0.1F, 0.0F, 0.0F));
        root.addOrReplaceChild("waist2",
                CubeListBuilder.create().texOffs(20, 391)
                        .addBox(0.0F, 0.0F, 0.0F, 12.0F, 12.0F, 12.0F),
                PartPose.offset(-6.0F, -67.0F, -4.0F));
        root.addOrReplaceChild("body3",
                CubeListBuilder.create().texOffs(20, 426)
                        .addBox(-23.0F, -25.0F, 10.0F, 47.0F, 47.0F, 25.0F),
                PartPose.offsetAndRotation(0.0F, -88.0F, -10.0F, 0.2F, 0.0F, 0.0F));
        root.addOrReplaceChild("lazer",
                CubeListBuilder.create().texOffs(20, 50)
                        .addBox(-8.0F, -8.0F, -22.0F, 17.0F, 16.0F, 22.0F),
                PartPose.offsetAndRotation(0.0F, -88.0F, -11.0F, 0.4F, 0.0F, 0.0F));
        root.addOrReplaceChild("body2",
                CubeListBuilder.create().texOffs(101, 103)
                        .addBox(9.0F, -24.0F, -12.0F, 15.0F, 47.0F, 47.0F),
                PartPose.offsetAndRotation(0.0F, -88.0F, -11.0F, 0.2F, 0.0F, 0.0F));
        root.addOrReplaceChild("body1",
                CubeListBuilder.create().texOffs(101, 210)
                        .addBox(-23.0F, -24.0F, -12.0F, 15.0F, 47.0F, 47.0F),
                PartPose.offsetAndRotation(0.0F, -88.0F, -11.0F, 0.2F, 0.0F, 0.0F));
        root.addOrReplaceChild("body4",
                CubeListBuilder.create().texOffs(101, 321)
                        .addBox(-8.0F, -24.0F, -12.0F, 18.0F, 16.0F, 22.0F),
                PartPose.offsetAndRotation(0.0F, -88.0F, -11.0F, 0.2F, 0.0F, 0.0F));
        root.addOrReplaceChild("waist3",
                CubeListBuilder.create().texOffs(99, 375)
                        .addBox(0.0F, 0.0F, -1.0F, 12.0F, 17.0F, 12.0F),
                PartPose.offsetAndRotation(-6.0F, -83.0F, -6.0F, 0.2F, 0.0F, 0.0F));
        // arm base xRot set fully in setupAnim (gold ±1.0 offsets)
        root.addOrReplaceChild("larm3",
                CubeListBuilder.create().texOffs(121, 54)
                        .addBox(0.0F, -10.0F, -9.0F, 20.0F, 18.0F, 18.0F),
                PartPose.offset(24.0F, -92.0F, 2.0F));
        root.addOrReplaceChild("rarm3",
                CubeListBuilder.create().texOffs(26, 8)
                        .addBox(-20.0F, -9.0F, -9.0F, 20.0F, 18.0F, 18.0F),
                PartPose.offset(-23.0F, -92.0F, 2.0F));
        root.addOrReplaceChild("larm2",
                CubeListBuilder.create().texOffs(207, 47)
                        .addBox(3.0F, 8.0F, -7.0F, 14.0F, 29.0F, 14.0F),
                PartPose.offset(24.0F, -92.0F, 2.0F));
        root.addOrReplaceChild("rarm2",
                CubeListBuilder.create().texOffs(161, 372)
                        .addBox(-17.0F, 9.0F, -7.0F, 14.0F, 29.0F, 14.0F),
                PartPose.offset(-23.0F, -92.0F, 2.0F));
        root.addOrReplaceChild("larm1",
                CubeListBuilder.create().texOffs(185, 433)
                        .addBox(0.0F, -12.0F, 30.0F, 14.0F, 37.0F, 14.0F),
                PartPose.offset(27.0F, -92.0F, 2.0F));
        root.addOrReplaceChild("rarm1",
                CubeListBuilder.create().texOffs(239, 105)
                        .addBox(-17.0F, -12.0F, 30.0F, 14.0F, 37.0F, 14.0F),
                PartPose.offset(-23.0F, -92.0F, 2.0F));

        return LayerDefinition.create(mesh, 512, 512);
    }

    @Override
    public ModelPart root() {
        return this.root;
    }

    @Override
    public void setupAnim(
            Robot3 entity,
            float limbSwing,
            float limbSwingAmount,
            float ageInTicks,
            float netHeadYaw,
            float headPitch) {
        float f1 = limbSwingAmount;
        float f2 = ageInTicks;
        float newangle;
        float nextangle;

        // gold walk: cos(f2 * 0.55 * wingspeed) * PI * 0.12 * f1 when f1 > 0.1
        if (f1 > 0.1F) {
            newangle = Mth.cos(f2 * 0.55F * this.wingspeed) * (float) Math.PI * 0.12F * f1;
        } else {
            newangle = 0.0F;
        }
        this.lleg1.xRot = newangle;
        this.lleg2.xRot = newangle;
        this.rleg1.xRot = -newangle;
        this.rleg2.xRot = -newangle;

        // gold lazer.yRot = toRadians(f3 / 2) — f3 is netHeadYaw
        this.lazer.yRot = (float) Math.toRadians(netHeadYaw / 2.0F);

        // gold arm phase: cos zero-crossing; ri1 set when attacking
        RenderInfo r = entity.getRenderInfo();
        newangle = Mth.cos(f2 * 1.0F * this.wingspeed) * (float) Math.PI * 0.15F;
        nextangle = Mth.cos((f2 + 0.3F) * 1.0F * this.wingspeed) * (float) Math.PI * 0.15F;
        if (nextangle > 0.0F && newangle < 0.0F) {
            r.ri1 = 0;
            if (entity.getAttacking() != 0) {
                r.ri1 = 1;
            }
        }
        if (r.ri1 == 0) {
            newangle = 0.0F;
        }
        this.rarm1.xRot = newangle - 1.0F;
        this.rarm2.xRot = newangle + 1.0F;
        this.rarm3.xRot = newangle + 1.0F;
        this.larm1.xRot = newangle - 1.0F;
        this.larm2.xRot = newangle + 1.0F;
        this.larm3.xRot = newangle + 1.0F;
        entity.setRenderInfo(r);
    }
}
