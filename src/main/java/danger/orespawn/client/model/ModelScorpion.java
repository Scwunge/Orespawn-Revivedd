package danger.orespawn.client.model;

import danger.orespawn.entity.RenderInfo;
import danger.orespawn.entity.Scorpion;
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
 * Port of gold {@code ModelScorpion} (1.7.10 ModelBase, tex 88×24) → 1.21 HierarchicalModel.
 * Cube sizes/UVs/offsets/base rotations 1:1. Wingspeed default 0.62 matches ClientProxy.
 * Claw/tail state via entity {@link RenderInfo}.
 */
@OnlyIn(Dist.CLIENT)
public class ModelScorpion extends HierarchicalModel<Scorpion> {
    /** Model layer location. */
    public static final ModelLayerLocation LAYER_LOCATION =
            new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "scorpion"), "main");

    private final float wingspeed;
    private final ModelPart root;
    private final ModelPart body;
    private final ModelPart tail1;
    private final ModelPart tail2;
    private final ModelPart tail3;
    private final ModelPart tail4;
    private final ModelPart tail5;
    private final ModelPart tail6;
    private final ModelPart lleg1;
    private final ModelPart rleg1;
    private final ModelPart rleg2;
    private final ModelPart lleg3;
    private final ModelPart rleg4;
    private final ModelPart rleg3;
    private final ModelPart lleg4;
    private final ModelPart lleg2;
    private final ModelPart head;
    private final ModelPart larm2;
    private final ModelPart rarm2;
    private final ModelPart larm1;
    private final ModelPart rarm1;
    private final ModelPart lclaw;
    private final ModelPart rclaw;

    public ModelScorpion(ModelPart root) {
        this(root, 0.62F);
    }

    public ModelScorpion(ModelPart root, float wingspeed) {
        this.root = root;
        this.wingspeed = wingspeed;
        this.body = root.getChild("body");
        this.tail1 = root.getChild("tail1");
        this.tail2 = root.getChild("tail2");
        this.tail3 = root.getChild("tail3");
        this.tail4 = root.getChild("tail4");
        this.tail5 = root.getChild("tail5");
        this.tail6 = root.getChild("tail6");
        this.lleg1 = root.getChild("lleg1");
        this.rleg1 = root.getChild("rleg1");
        this.rleg2 = root.getChild("rleg2");
        this.lleg3 = root.getChild("lleg3");
        this.rleg4 = root.getChild("rleg4");
        this.rleg3 = root.getChild("rleg3");
        this.lleg4 = root.getChild("lleg4");
        this.lleg2 = root.getChild("lleg2");
        this.head = root.getChild("head");
        this.larm2 = root.getChild("larm2");
        this.rarm2 = root.getChild("rarm2");
        this.larm1 = root.getChild("larm1");
        this.rarm1 = root.getChild("rarm1");
        this.lclaw = root.getChild("lclaw");
        this.rclaw = root.getChild("rclaw");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        root.addOrReplaceChild("body",
                CubeListBuilder.create().texOffs(0, 0)
                        .addBox(0.0F, 0.0F, 0.0F, 6.0F, 4.0F, 8.0F),
                PartPose.offset(-3.0F, 17.0F, -4.0F));
        root.addOrReplaceChild("tail1",
                CubeListBuilder.create().texOffs(28, 0)
                        .addBox(0.0F, 0.0F, 0.0F, 4.0F, 4.0F, 5.0F),
                PartPose.offsetAndRotation(-2.0F, 17.0F, 3.0F, (float) (Math.PI / 12), 0.0F, 0.0F));
        root.addOrReplaceChild("tail2",
                CubeListBuilder.create().texOffs(46, 0)
                        .addBox(0.0F, 0.0F, 0.0F, 3.0F, 3.0F, 5.0F),
                PartPose.offsetAndRotation(-1.5F, 16.8F, 6.0F, 1.029744F, 0.0F, 0.0F));
        root.addOrReplaceChild("tail3",
                CubeListBuilder.create().texOffs(62, 0)
                        .addBox(0.0F, 0.0F, 0.0F, 3.0F, 3.0F, 4.0F),
                PartPose.offsetAndRotation(-1.5F, 14.5F, 8.0F, 1.727876F, 0.0F, 0.0F));
        root.addOrReplaceChild("tail4",
                CubeListBuilder.create().texOffs(0, 17)
                        .addBox(0.0F, 0.0F, 0.0F, 2.0F, 2.0F, 5.0F),
                PartPose.offsetAndRotation(-1.0F, 12.0F, 9.0F, 2.513274F, 0.0F, 0.0F));
        root.addOrReplaceChild("tail5",
                CubeListBuilder.create().texOffs(70, 7)
                        .addBox(0.0F, 0.0F, 0.0F, 2.0F, 2.0F, 4.0F),
                PartPose.offsetAndRotation(-1.0F, 9.0F, 6.0F, 3.141593F, 0.0F, 0.0F));
        root.addOrReplaceChild("tail6",
                CubeListBuilder.create().texOffs(62, 7)
                        .addBox(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 3.0F),
                PartPose.offsetAndRotation(-0.5F, 8.0F, 2.0F, 3.141593F, 0.0F, 0.0F));
        root.addOrReplaceChild("lleg1",
                CubeListBuilder.create().texOffs(0, 12)
                        .addBox(0.0F, 0.0F, 0.0F, 11.0F, 2.0F, 2.0F),
                PartPose.offsetAndRotation(2.0F, 18.0F, -3.0F, 0.0F, 0.4886922F, 0.3665191F));
        root.addOrReplaceChild("rleg1",
                CubeListBuilder.create().texOffs(0, 12)
                        .addBox(0.0F, 0.0F, 0.0F, 11.0F, 2.0F, 2.0F),
                PartPose.offsetAndRotation(-2.0F, 18.0F, -1.0F, 0.0F, 2.6529F, -0.3665191F));
        root.addOrReplaceChild("rleg2",
                CubeListBuilder.create().texOffs(0, 12)
                        .addBox(0.0F, 0.0F, 0.0F, 11.0F, 2.0F, 2.0F),
                PartPose.offsetAndRotation(-2.0F, 18.0F, 1.0F, 0.0F, 2.897247F, -0.3665191F));
        root.addOrReplaceChild("lleg3",
                CubeListBuilder.create().texOffs(0, 12)
                        .addBox(0.0F, 0.0F, 0.0F, 11.0F, 2.0F, 2.0F),
                PartPose.offsetAndRotation(2.0F, 18.0F, 1.0F, 0.0F, -0.2443461F, 0.3665191F));
        root.addOrReplaceChild("rleg4",
                CubeListBuilder.create().texOffs(0, 12)
                        .addBox(0.0F, 0.0F, 0.0F, 11.0F, 2.0F, 2.0F),
                PartPose.offsetAndRotation(-2.0F, 18.0F, 5.0F, 0.0F, -2.6529F, -0.3665191F));
        root.addOrReplaceChild("rleg3",
                CubeListBuilder.create().texOffs(0, 12)
                        .addBox(0.0F, 0.0F, 0.0F, 11.0F, 2.0F, 2.0F),
                PartPose.offsetAndRotation(-2.0F, 18.0F, 3.0F, 0.0F, -2.897247F, -0.3665191F));
        root.addOrReplaceChild("lleg4",
                CubeListBuilder.create().texOffs(0, 12)
                        .addBox(0.0F, 0.0F, 0.0F, 11.0F, 2.0F, 2.0F),
                PartPose.offsetAndRotation(2.0F, 18.0F, 3.0F, 0.0F, -0.4886922F, 0.3665191F));
        root.addOrReplaceChild("lleg2",
                CubeListBuilder.create().texOffs(0, 12)
                        .addBox(0.0F, 0.0F, 0.0F, 11.0F, 2.0F, 2.0F),
                PartPose.offsetAndRotation(2.0F, 18.0F, -1.0F, 0.0F, 0.2443461F, 0.3665191F));
        root.addOrReplaceChild("head",
                CubeListBuilder.create().texOffs(28, 9)
                        .addBox(0.0F, 0.0F, 0.0F, 5.0F, 3.0F, 4.0F),
                PartPose.offset(-2.5F, 17.5F, -8.0F));
        root.addOrReplaceChild("larm2",
                CubeListBuilder.create().texOffs(46, 8)
                        .addBox(0.0F, 0.0F, 0.0F, 6.0F, 2.0F, 2.0F),
                PartPose.offsetAndRotation(1.0F, 18.0F, -6.0F, 0.0F, (float) (Math.PI / 6), 0.1745329F));
        root.addOrReplaceChild("rarm2",
                CubeListBuilder.create().texOffs(46, 8)
                        .addBox(0.0F, 0.0F, -2.0F, 6.0F, 2.0F, 2.0F),
                PartPose.offsetAndRotation(-1.0F, 18.0F, -6.0F, 0.0F, 2.617994F, -0.1745329F));
        root.addOrReplaceChild("larm1",
                CubeListBuilder.create().texOffs(70, 13)
                        .addBox(-2.0F, 0.0F, -3.0F, 2.0F, 2.0F, 3.0F),
                PartPose.offsetAndRotation(7.0F, 19.0F, -7.2F, 0.1745329F, 0.1745329F, 0.0F));
        root.addOrReplaceChild("rarm1",
                CubeListBuilder.create().texOffs(70, 13)
                        .addBox(0.0F, 0.0F, -3.0F, 2.0F, 2.0F, 3.0F),
                PartPose.offsetAndRotation(-7.0F, 19.0F, -7.2F, 0.1745329F, -0.1745329F, 0.0F));
        root.addOrReplaceChild("lclaw",
                CubeListBuilder.create().texOffs(46, 12)
                        .addBox(-3.0F, 0.0F, -4.0F, 3.0F, 2.0F, 4.0F),
                PartPose.offsetAndRotation(7.0F, 19.0F, -10.0F, 0.0174533F, 0.3839724F, 0.1396263F));
        root.addOrReplaceChild("rclaw",
                CubeListBuilder.create().texOffs(46, 12)
                        .addBox(0.0F, 0.0F, -4.0F, 3.0F, 2.0F, 4.0F),
                PartPose.offsetAndRotation(-7.0F, 19.0F, -10.0F, 0.0174533F, -0.3839724F, 0.1396263F));

        return LayerDefinition.create(mesh, 88, 24);
    }

    @Override
    public ModelPart root() {
        return this.root;
    }

    @Override
    public void setupAnim(
            Scorpion entity,
            float limbSwing,
            float limbSwingAmount,
            float ageInTicks,
            float netHeadYaw,
            float headPitch) {
        float f1 = limbSwingAmount;
        float f2 = ageInTicks;
        float pi4 = 1.570795F;
        float newangle;
        float nextangle;

        newangle = Mth.cos(f2 * 2.0F * this.wingspeed) * (float) Math.PI * 0.12F * f1;
        this.lleg1.yRot = newangle + 0.49F;
        this.rleg1.yRot = -newangle + 2.65F;
        newangle = Mth.cos(f2 * 2.0F * this.wingspeed - 1.0F * pi4) * (float) Math.PI * 0.12F * f1;
        this.lleg2.yRot = newangle + 0.24F;
        this.rleg2.yRot = -newangle + 2.9F;
        newangle = Mth.cos(f2 * 2.0F * this.wingspeed - 2.0F * pi4) * (float) Math.PI * 0.12F * f1;
        this.lleg3.yRot = newangle - 0.24F;
        this.rleg3.yRot = -newangle - 2.9F;
        newangle = Mth.cos(f2 * 2.0F * this.wingspeed - 3.0F * pi4) * (float) Math.PI * 0.12F * f1;
        this.lleg4.yRot = newangle - 0.49F;
        this.rleg4.yRot = -newangle - 2.65F;

        RenderInfo r = entity.getRenderInfo();
        newangle = Mth.cos(f2 * 3.0F * this.wingspeed) * (float) Math.PI * 0.15F;
        nextangle = Mth.cos((f2 + 0.1F) * 3.0F * this.wingspeed) * (float) Math.PI * 0.15F;
        if (nextangle > 0.0F && newangle < 0.0F) {
            r.ri1 = 0;
            if (entity.getAttacking() == 0) {
                r.ri1 = entity.getRandom().nextInt(20);
                r.ri2 = entity.getRandom().nextInt(25);
            } else {
                r.ri1 = entity.getRandom().nextInt(4);
                r.ri2 = entity.getRandom().nextInt(3);
            }
        }
        if (r.ri1 != 1 && r.ri1 != 3) {
            this.doLeftClaw(0.0F);
        } else {
            this.doLeftClaw(newangle);
        }
        if (r.ri1 != 2 && r.ri1 != 3) {
            this.doRightClaw(0.0F);
        } else {
            this.doRightClaw(newangle);
        }
        if (r.ri2 == 1) {
            this.doTail(newangle);
        } else {
            this.doTail(0.0F);
        }
        entity.setRenderInfo(r);
    }

    private void doLeftClaw(float angle) {
        this.larm2.yRot = 0.52F + angle;
        this.larm1.z = (float) (this.larm2.z - Math.sin(this.larm2.yRot) * 4.5);
        this.lclaw.z = this.larm1.z - 3.0F;
        this.lclaw.yRot = 0.381F - angle;
    }

    private void doRightClaw(float angle) {
        this.rarm2.yRot = 2.61F - angle;
        this.rarm1.z = (float) (this.rarm2.z - Math.sin(this.rarm2.yRot) * 4.5);
        this.rclaw.z = this.rarm1.z - 3.0F;
        this.rclaw.yRot = -0.381F + angle;
    }

    private void doTail(float angle) {
        this.tail1.xRot = 0.26F + angle;
        this.tail2.xRot = this.tail1.xRot + 0.76900005F + angle;
        this.tail2.y = (float) (this.tail1.y - Math.sin(this.tail1.xRot) * 4.0);
        this.tail2.z = (float) (this.tail1.z + Math.cos(this.tail1.xRot) * 4.0);
        this.tail3.xRot = this.tail2.xRot + 0.701F + angle;
        this.tail3.y = (float) (this.tail2.y - Math.sin(this.tail2.xRot) * 4.0);
        this.tail3.z = (float) (this.tail2.z + Math.cos(this.tail2.xRot) * 4.0);
        this.tail4.xRot = this.tail3.xRot + -5.501F - angle * 3.0F / 2.0F - 0.4F;
        this.tail4.y = (float) (this.tail3.y - Math.sin(this.tail3.xRot) * 3.0);
        this.tail4.z = (float) (this.tail3.z + Math.cos(this.tail3.xRot) * 3.0);
        this.tail5.y = (float) (this.tail4.y - Math.sin(this.tail4.xRot) * 4.0);
        this.tail5.z = (float) (this.tail4.z + Math.cos(this.tail4.xRot) * 4.0);
        this.tail6.y = (float) (this.tail5.y - Math.sin(this.tail5.xRot) * 4.0);
        this.tail6.z = (float) (this.tail5.z + Math.cos(this.tail5.xRot) * 4.0);
    }
}
