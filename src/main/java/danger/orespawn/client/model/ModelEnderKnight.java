package danger.orespawn.client.model;

import danger.orespawn.entity.EnderKnight;
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
 * Port of gold {@code ModelEnderKnight} (1.7.10 ModelBase, tex 512×512) → 1.21 HierarchicalModel.
 * Cube sizes/UVs/offsets 1:1. Animation from gold {@code render()} (wingspeed 0.21).
 */
@OnlyIn(Dist.CLIENT)
public class ModelEnderKnight extends HierarchicalModel<EnderKnight> {
    /** Model layer location. */
    public static final ModelLayerLocation LAYER_LOCATION =
            new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "ender_knight"), "main");

    private final float wingspeed;
    private final ModelPart root;
    private final ModelPart rleg1;
    private final ModelPart rleg3;
    private final ModelPart pelvis;
    private final ModelPart spine1;
    private final ModelPart spine2;
    private final ModelPart spine3;
    private final ModelPart neck;
    private final ModelPart rleg2;
    private final ModelPart rhip;
    private final ModelPart rib4;
    private final ModelPart rib3;
    private final ModelPart rib2;
    private final ModelPart rib1;
    private final ModelPart rfoot1;
    private final ModelPart rfoot3;
    private final ModelPart rcollar;
    private final ModelPart lcollar;
    private final ModelPart lleg3;
    private final ModelPart lleg2;
    private final ModelPart lhip;
    private final ModelPart lleg1;
    private final ModelPart rfoot4;
    private final ModelPart rfoot2;
    private final ModelPart cape2;
    private final ModelPart cape1;
    private final ModelPart lfoot1;
    private final ModelPart lfoot3;
    private final ModelPart lfoot2;
    private final ModelPart lfoot4;
    private final ModelPart head;
    private final ModelPart lshoulder;
    private final ModelPart rshoulder;
    private final ModelPart rarm3;
    private final ModelPart rarm2;
    private final ModelPart rarm1;
    private final ModelPart larm3;
    private final ModelPart larm2;
    private final ModelPart larm1;
    private final ModelPart blade;
    private final ModelPart handle;

    public ModelEnderKnight(ModelPart root) {
        this(root, 0.21F);
    }

    public ModelEnderKnight(ModelPart root, float wingspeed) {
        this.root = root;
        this.wingspeed = wingspeed;
        this.rleg1 = root.getChild("rleg1");
        this.rleg3 = root.getChild("rleg3");
        this.pelvis = root.getChild("pelvis");
        this.spine1 = root.getChild("spine1");
        this.spine2 = root.getChild("spine2");
        this.spine3 = root.getChild("spine3");
        this.neck = root.getChild("neck");
        this.rleg2 = root.getChild("rleg2");
        this.rhip = root.getChild("rhip");
        this.rib4 = root.getChild("rib4");
        this.rib3 = root.getChild("rib3");
        this.rib2 = root.getChild("rib2");
        this.rib1 = root.getChild("rib1");
        this.rfoot1 = root.getChild("rfoot1");
        this.rfoot3 = root.getChild("rfoot3");
        this.rcollar = root.getChild("rcollar");
        this.lcollar = root.getChild("lcollar");
        this.lleg3 = root.getChild("lleg3");
        this.lleg2 = root.getChild("lleg2");
        this.lhip = root.getChild("lhip");
        this.lleg1 = root.getChild("lleg1");
        this.rfoot4 = root.getChild("rfoot4");
        this.rfoot2 = root.getChild("rfoot2");
        this.cape2 = root.getChild("cape2");
        this.cape1 = root.getChild("cape1");
        this.lfoot1 = root.getChild("lfoot1");
        this.lfoot3 = root.getChild("lfoot3");
        this.lfoot2 = root.getChild("lfoot2");
        this.lfoot4 = root.getChild("lfoot4");
        this.head = root.getChild("head");
        this.lshoulder = root.getChild("lshoulder");
        this.rshoulder = root.getChild("rshoulder");
        this.rarm3 = root.getChild("rarm3");
        this.rarm2 = root.getChild("rarm2");
        this.rarm1 = root.getChild("rarm1");
        this.larm3 = root.getChild("larm3");
        this.larm2 = root.getChild("larm2");
        this.larm1 = root.getChild("larm1");
        this.blade = root.getChild("blade");
        this.handle = root.getChild("handle");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        root.addOrReplaceChild("rleg1",
                CubeListBuilder.create().texOffs(20, 50).addBox(0.0F, 12.0F, -1.0F, 1.0F, 15.0F, 1.0F),
                PartPose.offset(-7.0F, -5.0F, -2.0F));
        root.addOrReplaceChild("rleg3",
                CubeListBuilder.create().texOffs(20, 100).addBox(0.0F, 0.0F, 0.0F, 1.0F, 14.0F, 2.0F),
                PartPose.offsetAndRotation(-6.0F, -5.0F, -2.0F, -0.1F, 0.0F, 0.0F));
        root.addOrReplaceChild("pelvis",
                CubeListBuilder.create().texOffs(20, 150).addBox(0.0F, 0.0F, 0.0F, 3.0F, 3.0F, 3.0F),
                PartPose.offset(-5.0F, -6.0F, -2.0F));
        root.addOrReplaceChild("spine1",
                CubeListBuilder.create().texOffs(20, 200).addBox(0.0F, 0.0F, 0.0F, 1.0F, 5.0F, 1.0F),
                PartPose.offsetAndRotation(-4.0F, -9.0F, 1.0F, -0.3F, 0.0F, 0.0F));
        root.addOrReplaceChild("spine2",
                CubeListBuilder.create().texOffs(20, 250).addBox(0.0F, 0.0F, 0.0F, 1.0F, 5.0F, 1.0F),
                PartPose.offset(-4.0F, -13.0F, 1.0F));
        root.addOrReplaceChild("spine3",
                CubeListBuilder.create().texOffs(20, 300).addBox(0.0F, 0.0F, 0.0F, 1.0F, 5.0F, 1.0F),
                PartPose.offsetAndRotation(-4.0F, -17.0F, 0.0F, 0.2F, 0.0F, 0.0F));
        root.addOrReplaceChild("neck",
                CubeListBuilder.create().texOffs(20, 11).addBox(0.0F, 0.0F, 0.0F, 5.0F, 3.0F, 3.0F),
                PartPose.offset(-6.0F, -20.0F, 0.0F));
        root.addOrReplaceChild("rleg2",
                CubeListBuilder.create().texOffs(20, 400).addBox(0.0F, 0.0F, 0.0F, 1.0F, 14.0F, 2.0F),
                PartPose.offsetAndRotation(-8.0F, -5.0F, -2.0F, -0.1F, 0.0F, 0.0F));
        root.addOrReplaceChild("rhip",
                CubeListBuilder.create().texOffs(20, 450).addBox(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F),
                PartPose.offset(-7.0F, -4.0F, -2.0F));
        root.addOrReplaceChild("rib4",
                CubeListBuilder.create().texOffs(20, 79).addBox(0.0F, 0.0F, 0.0F, 3.0F, 1.0F, 1.0F),
                PartPose.offset(-5.0F, -9.0F, 1.0F));
        root.addOrReplaceChild("rib3",
                CubeListBuilder.create().texOffs(20, 86).addBox(0.0F, 0.0F, 0.0F, 3.0F, 1.0F, 1.0F),
                PartPose.offset(-5.0F, -11.0F, 1.0F));
        root.addOrReplaceChild("rib2",
                CubeListBuilder.create().texOffs(20, 94).addBox(0.0F, 0.0F, 0.0F, 5.0F, 1.0F, 1.0F),
                PartPose.offset(-6.0F, -13.0F, 1.0F));
        root.addOrReplaceChild("rib1",
                CubeListBuilder.create().texOffs(20, 122).addBox(0.0F, 0.0F, 0.0F, 5.0F, 1.0F, 1.0F),
                PartPose.offset(-6.0F, -16.0F, 0.0F));
        root.addOrReplaceChild("rfoot1",
                CubeListBuilder.create().texOffs(20, 131).addBox(0.0F, 21.0F, -2.0F, 3.0F, 8.0F, 3.0F),
                PartPose.offset(-8.0F, -5.0F, -2.0F));
        root.addOrReplaceChild("rfoot3",
                CubeListBuilder.create().texOffs(20, 162).addBox(0.0F, 27.0F, -5.0F, 3.0F, 2.0F, 6.0F),
                PartPose.offset(-8.0F, -5.0F, -2.0F));
        root.addOrReplaceChild("rcollar",
                CubeListBuilder.create().texOffs(20, 243).addBox(0.0F, 0.0F, 0.0F, 5.0F, 1.0F, 1.0F),
                PartPose.offset(-11.0F, -19.0F, 1.0F));
        root.addOrReplaceChild("lcollar",
                CubeListBuilder.create().texOffs(20, 286).addBox(0.0F, 0.0F, 0.0F, 5.0F, 1.0F, 1.0F),
                PartPose.offset(-1.0F, -19.0F, 1.0F));
        root.addOrReplaceChild("lleg3",
                CubeListBuilder.create().texOffs(48, 159).addBox(0.0F, 0.0F, 0.0F, 1.0F, 14.0F, 2.0F),
                PartPose.offsetAndRotation(-2.0F, -5.0F, -2.0F, -0.1F, 0.0F, 0.0F));
        root.addOrReplaceChild("lleg2",
                CubeListBuilder.create().texOffs(28, 187).addBox(0.0F, 0.0F, 0.0F, 1.0F, 14.0F, 2.0F),
                PartPose.offsetAndRotation(0.0F, -5.0F, -2.0F, -0.1F, 0.0F, 0.0F));
        root.addOrReplaceChild("lhip",
                CubeListBuilder.create().texOffs(32, 219).addBox(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F),
                PartPose.offset(-1.0F, -4.0F, -2.0F));
        root.addOrReplaceChild("lleg1",
                CubeListBuilder.create().texOffs(36, 224).addBox(0.0F, 12.0F, -1.0F, 1.0F, 15.0F, 1.0F),
                PartPose.offset(-1.0F, -5.0F, -2.0F));
        root.addOrReplaceChild("rfoot4",
                CubeListBuilder.create().texOffs(33, 254).addBox(0.0F, 26.0F, -3.0F, 3.0F, 1.0F, 1.0F),
                PartPose.offset(-8.0F, -5.0F, -2.0F));
        root.addOrReplaceChild("rfoot2",
                CubeListBuilder.create().texOffs(32, 36).addBox(0.0F, 19.5F, -19.0F, 3.0F, 1.0F, 5.0F),
                PartPose.offsetAndRotation(-8.0F, -5.0F, -2.0F, 0.6F, 0.0F, 0.0F));
        root.addOrReplaceChild("cape2",
                CubeListBuilder.create().texOffs(51, 276).addBox(-4.0F, 0.0F, 0.0F, 9.0F, 24.0F, 0.0F),
                PartPose.offset(-4.0F, -20.0F, 4.0F));
        root.addOrReplaceChild("cape1",
                CubeListBuilder.create().texOffs(51, 264).addBox(0.0F, 0.0F, 0.0F, 9.0F, 1.0F, 1.0F),
                PartPose.offset(-8.0F, -20.0F, 3.0F));
        root.addOrReplaceChild("lfoot1",
                CubeListBuilder.create().texOffs(44, 182).addBox(0.0F, 21.0F, -2.0F, 3.0F, 8.0F, 3.0F),
                PartPose.offset(-2.0F, -5.0F, -2.0F));
        root.addOrReplaceChild("lfoot3",
                CubeListBuilder.create().texOffs(52, 200).addBox(0.0F, 27.0F, -5.0F, 3.0F, 2.0F, 6.0F),
                PartPose.offset(-2.0F, -5.0F, -2.0F));
        root.addOrReplaceChild("lfoot2",
                CubeListBuilder.create().texOffs(52, 218).addBox(0.0F, 19.5F, -19.0F, 3.0F, 1.0F, 5.0F),
                PartPose.offsetAndRotation(-2.0F, -5.0F, -2.0F, 0.6F, 0.0F, 0.0F));
        root.addOrReplaceChild("lfoot4",
                CubeListBuilder.create().texOffs(48, 235).addBox(0.0F, 26.0F, -3.0F, 3.0F, 1.0F, 1.0F),
                PartPose.offset(-2.0F, -5.0F, -2.0F));
        root.addOrReplaceChild("head",
                CubeListBuilder.create().texOffs(34, 106).addBox(-4.0F, -8.0F, -4.0F, 7.0F, 6.0F, 6.0F),
                PartPose.offset(-3.0F, -18.0F, 3.0F));
        root.addOrReplaceChild("lshoulder",
                CubeListBuilder.create().texOffs(48, 16).addBox(0.0F, 0.0F, 0.0F, 5.0F, 4.0F, 4.0F),
                PartPose.offset(2.0F, -21.0F, 0.0F));
        root.addOrReplaceChild("rshoulder",
                CubeListBuilder.create().texOffs(48, 16).addBox(0.0F, 0.0F, 0.0F, 5.0F, 4.0F, 4.0F),
                PartPose.offset(-14.0F, -21.0F, 0.0F));
        root.addOrReplaceChild("rarm3",
                CubeListBuilder.create().texOffs(39, 64).addBox(0.0F, 0.0F, 0.0F, 1.0F, 12.0F, 1.0F),
                PartPose.offsetAndRotation(-11.0F, -18.0F, 1.0F, -0.5F, 0.0F, 0.0F));
        root.addOrReplaceChild("rarm2",
                CubeListBuilder.create().texOffs(57, 62).addBox(0.0F, 0.0F, 0.0F, 1.0F, 12.0F, 1.0F),
                PartPose.offsetAndRotation(-13.0F, -18.0F, 1.0F, -0.5F, 0.0F, 0.0F));
        root.addOrReplaceChild("rarm1",
                CubeListBuilder.create().texOffs(49, 81).addBox(0.0F, 0.0F, 0.0F, 1.0F, 11.0F, 1.0F),
                PartPose.offsetAndRotation(-12.0F, -18.0F, 2.0F, -1.0F, -1.0F, 0.0F));
        root.addOrReplaceChild("larm3",
                CubeListBuilder.create().texOffs(49, 129).addBox(0.0F, 0.0F, 0.0F, 1.0F, 12.0F, 1.0F),
                PartPose.offsetAndRotation(3.0F, -18.0F, 1.0F, -0.5F, 0.0F, 0.0F));
        root.addOrReplaceChild("larm2",
                CubeListBuilder.create().texOffs(64, 133).addBox(0.0F, 0.0F, 0.0F, 1.0F, 12.0F, 1.0F),
                PartPose.offsetAndRotation(5.0F, -18.0F, 1.0F, -0.5F, 0.0F, 0.0F));
        root.addOrReplaceChild("larm1",
                CubeListBuilder.create().texOffs(22, 316).addBox(0.0F, 0.0F, 0.0F, 1.0F, 11.0F, 1.0F),
                PartPose.offsetAndRotation(4.0F, -18.0F, 1.0F, -1.0F, 1.0F, 0.0F));
        root.addOrReplaceChild("blade",
                CubeListBuilder.create().texOffs(36, 304).addBox(0.0F, -34.0F, -2.0F, 1.0F, 32.0F, 6.0F),
                PartPose.offsetAndRotation(-4.0F, -2.0F, -8.0F, 0.35F, 0.0F, 0.0F));
        root.addOrReplaceChild("handle",
                CubeListBuilder.create().texOffs(18, 26).addBox(0.0F, -2.0F, 0.0F, 1.0F, 4.0F, 1.0F),
                PartPose.offsetAndRotation(-4.0F, -2.0F, -8.0F, 0.35F, 0.0F, 0.0F));

        return LayerDefinition.create(mesh, 512, 512);
    }

    @Override
    public ModelPart root() {
        return this.root;
    }

    @Override
    public void setupAnim(
            EnderKnight entity,
            float limbSwing,
            float limbSwingAmount,
            float ageInTicks,
            float netHeadYaw,
            float headPitch) {
        float newangle;
        if (limbSwingAmount > 0.1F) {
            newangle = Mth.cos(ageInTicks * 1.3F * this.wingspeed) * (float) Math.PI * 0.25F * limbSwingAmount;
        } else {
            newangle = 0.0F;
        }

        this.lfoot1.xRot = newangle;
        this.lfoot2.xRot = 0.6F + newangle;
        this.lfoot3.xRot = newangle;
        this.lfoot4.xRot = newangle;
        this.lleg1.xRot = newangle;
        this.lleg2.xRot = -0.1F + newangle;
        this.lleg3.xRot = -0.1F + newangle;
        this.rfoot1.xRot = -newangle;
        this.rfoot2.xRot = 0.6F - newangle;
        this.rfoot3.xRot = -newangle;
        this.rfoot4.xRot = -newangle;
        this.rleg1.xRot = -newangle;
        this.rleg2.xRot = -0.1F - newangle;
        this.rleg3.xRot = -0.1F - newangle;
        this.cape2.zRot = newangle / 4.0F;

        newangle = Mth.cos(ageInTicks * 0.7F * this.wingspeed) * (float) Math.PI * 0.02F;
        this.cape2.xRot = newangle;

        this.head.yRot = (float) Math.toRadians(netHeadYaw) * 0.45F;
        if (this.head.yRot > 0.45F) {
            this.head.yRot = 0.45F;
        }
        if (this.head.yRot < -0.45F) {
            this.head.yRot = -0.45F;
        }

        newangle = Mth.cos(ageInTicks * 2.7F * this.wingspeed) * (float) Math.PI * 0.3F;
        if (entity.isScreaming()) {
            this.larm2.xRot = -1.2F + newangle;
            this.larm3.xRot = -1.2F + newangle;
            this.rarm2.xRot = -1.2F + newangle;
            this.rarm3.xRot = -1.2F + newangle;
            this.larm1.xRot = -1.8F + newangle;
            this.rarm1.xRot = -1.8F + newangle;
            this.blade.xRot = this.handle.xRot = 0.5F + newangle * 3.0F / 2.0F;
        } else {
            this.larm2.xRot = -0.5F;
            this.larm3.xRot = -0.5F;
            this.larm1.zRot = 0.0F;
            this.larm1.yRot = 1.0F;
            this.larm1.xRot = -1.0F;
            this.rarm2.xRot = -0.5F;
            this.rarm3.xRot = -0.5F;
            this.rarm1.zRot = 0.0F;
            this.rarm1.yRot = -1.0F;
            this.rarm1.xRot = -1.0F;
            this.blade.xRot = this.handle.xRot = 0.35F;
        }

        // gold dynamic forearm / blade positions from upper-arm angles
        this.larm1.y = (float) (this.larm2.y + Math.cos(this.larm2.xRot) * 10.0);
        this.larm1.z = (float) (this.larm2.z + Math.sin(this.larm2.xRot) * 10.0);
        this.rarm1.y = (float) (this.rarm2.y + Math.cos(this.rarm2.xRot) * 10.0);
        this.rarm1.z = (float) (this.rarm2.z + Math.sin(this.rarm2.xRot) * 10.0);
        this.blade.y = this.handle.y = (float) (this.rarm1.y + Math.cos(this.rarm1.xRot) * 7.0) + 1.0F;
        this.blade.z = this.handle.z = (float) (this.rarm1.z + Math.sin(this.rarm1.xRot) * 7.0);
    }
}
