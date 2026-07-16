package danger.orespawn.client.model;

import danger.orespawn.entity.Ostrich;
import danger.orespawn.entity.RenderInfo;
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
 * Port of gold {@code ModelOstrich} (1.7.10 ModelBase) → 1.21 HierarchicalModel.
 * Cubes/UVs 1:1. Texture 256×128. Wingspeed default 0.65F matches ClientProxy.
 * Full {@code func_78088_a} leg/tail/head/wing/hat animation.
 */
@OnlyIn(Dist.CLIENT)
public class ModelOstrich extends HierarchicalModel<Ostrich> {
    /** Model layer location. */
    public static final ModelLayerLocation LAYER_LOCATION =
            new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "ostrich"), "main");

    private final float wingspeed;
    private final ModelPart root;
    private final ModelPart Body1;
    private final ModelPart body2;
    private final ModelPart LLeg1;
    private final ModelPart Rleg1;
    private final ModelPart LLeg2;
    private final ModelPart Lfoot1;
    private final ModelPart RLeg2;
    private final ModelPart Lfoot2;
    private final ModelPart Lfoot3;
    private final ModelPart LClaw1;
    private final ModelPart LClaw2;
    private final ModelPart LClaw3;
    private final ModelPart Lfoot4;
    private final ModelPart LClaw4;
    private final ModelPart Rfoot1;
    private final ModelPart Rfoot2;
    private final ModelPart Rclaw1;
    private final ModelPart Rfoot3;
    private final ModelPart Rclaw3;
    private final ModelPart Rfoot4;
    private final ModelPart Rclaw2;
    private final ModelPart Rclaw4;
    private final ModelPart Body3;
    private final ModelPart Tail1;
    private final ModelPart Tail2;
    private final ModelPart Tail3;
    private final ModelPart Body4;
    private final ModelPart head;
    private final ModelPart leftleg;
    private final ModelPart Neck1;
    private final ModelPart Head1;
    private final ModelPart mouth1;
    private final ModelPart neck2;
    private final ModelPart rightleg;
    private final ModelPart Lwing;
    private final ModelPart Rwing;
    private final ModelPart Hat1;
    private final ModelPart Hat2;

    public ModelOstrich(ModelPart root) {
        this(root, 0.65F);
    }

    public ModelOstrich(ModelPart root, float wingspeed) {
        this.root = root;
        this.wingspeed = wingspeed;
        this.Body1 = root.getChild("Body1");
        this.body2 = root.getChild("body2");
        this.LLeg1 = root.getChild("LLeg1");
        this.Rleg1 = root.getChild("Rleg1");
        this.LLeg2 = root.getChild("LLeg2");
        this.Lfoot1 = root.getChild("Lfoot1");
        this.RLeg2 = root.getChild("RLeg2");
        this.Lfoot2 = root.getChild("Lfoot2");
        this.Lfoot3 = root.getChild("Lfoot3");
        this.LClaw1 = root.getChild("LClaw1");
        this.LClaw2 = root.getChild("LClaw2");
        this.LClaw3 = root.getChild("LClaw3");
        this.Lfoot4 = root.getChild("Lfoot4");
        this.LClaw4 = root.getChild("LClaw4");
        this.Rfoot1 = root.getChild("Rfoot1");
        this.Rfoot2 = root.getChild("Rfoot2");
        this.Rclaw1 = root.getChild("Rclaw1");
        this.Rfoot3 = root.getChild("Rfoot3");
        this.Rclaw3 = root.getChild("Rclaw3");
        this.Rfoot4 = root.getChild("Rfoot4");
        this.Rclaw2 = root.getChild("Rclaw2");
        this.Rclaw4 = root.getChild("Rclaw4");
        this.Body3 = root.getChild("Body3");
        this.Tail1 = root.getChild("Tail1");
        this.Tail2 = root.getChild("Tail2");
        this.Tail3 = root.getChild("Tail3");
        this.Body4 = root.getChild("Body4");
        this.head = root.getChild("head");
        this.leftleg = root.getChild("leftleg");
        this.Neck1 = root.getChild("Neck1");
        this.Head1 = root.getChild("Head1");
        this.mouth1 = root.getChild("mouth1");
        this.neck2 = root.getChild("neck2");
        this.rightleg = root.getChild("rightleg");
        this.Lwing = root.getChild("Lwing");
        this.Rwing = root.getChild("Rwing");
        this.Hat1 = root.getChild("Hat1");
        this.Hat2 = root.getChild("Hat2");
        this.Hat1.visible = false;
        this.Hat2.visible = false;
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        // gold ModelOstrich cubes/UVs 1:1 (tex 256×128)
        root.addOrReplaceChild(
                "Body1",
                CubeListBuilder.create().texOffs(0, 28).addBox(-4.0F, 0.0F, 0.0F, 8.0F, 9.0F, 8.0F),
                PartPose.offsetAndRotation(0.0F, 0.0F, -6.0F, -0.2230717F, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "body2",
                CubeListBuilder.create().texOffs(25, 111).addBox(-4.0F, 0.0F, 0.0F, 8.0F, 8.0F, 8.0F),
                PartPose.offset(0.0F, 2.0F, -1.0F));
        root.addOrReplaceChild(
                "LLeg1",
                CubeListBuilder.create().texOffs(25, 70).addBox(-1.0F, 3.0F, -5.0F, 2.0F, 7.0F, 3.0F),
                PartPose.offsetAndRotation(3.0F, 8.0F, 1.0F, 0.4833219F, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "Rleg1",
                CubeListBuilder.create().texOffs(25, 70).addBox(-2.0F, 3.0F, -5.0F, 2.0F, 7.0F, 3.0F),
                PartPose.offsetAndRotation(-2.0F, 8.0F, 1.0F, 0.4833219F, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "LLeg2",
                CubeListBuilder.create().texOffs(29, 59).addBox(-1.0F, 7.0F, 4.0F, 2.0F, 7.0F, 3.0F),
                PartPose.offsetAndRotation(3.0F, 8.0F, 1.0F, -0.4370552F, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "Lfoot1",
                CubeListBuilder.create().texOffs(29, 50).addBox(-1.0F, 14.0F, -5.0F, 2.0F, 2.0F, 6.0F),
                PartPose.offset(3.0F, 8.0F, 1.0F));
        root.addOrReplaceChild(
                "RLeg2",
                CubeListBuilder.create().texOffs(29, 59).addBox(-2.0F, 7.0F, 4.0F, 2.0F, 7.0F, 3.0F),
                PartPose.offsetAndRotation(-2.0F, 8.0F, 1.0F, -0.4370552F, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "Lfoot2",
                CubeListBuilder.create().texOffs(0, 9).addBox(-1.0F, 15.0F, -4.0F, 2.0F, 1.0F, 5.0F),
                PartPose.offsetAndRotation(3.0F, 8.0F, 1.0F, 0.0F, 0.2602503F, 0.0F));
        root.addOrReplaceChild(
                "Lfoot3",
                CubeListBuilder.create().texOffs(0, 9).addBox(-1.0F, 15.0F, -4.0F, 2.0F, 1.0F, 5.0F),
                PartPose.offsetAndRotation(3.0F, 8.0F, 1.0F, 0.0F, -0.260246F, 0.0F));
        root.addOrReplaceChild(
                "LClaw1",
                CubeListBuilder.create().texOffs(16, 10).addBox(0.0F, 14.0F, -7.0F, 0.0F, 2.0F, 3.0F),
                PartPose.offset(3.0F, 8.0F, 1.0F));
        root.addOrReplaceChild(
                "LClaw2",
                CubeListBuilder.create().texOffs(19, 16).addBox(-0.5F, 15.0F, -5.0F, 0.0F, 1.0F, 3.0F),
                PartPose.offsetAndRotation(3.0F, 8.0F, 1.0F, 0.0F, 0.260246F, 0.0F));
        root.addOrReplaceChild(
                "LClaw3",
                CubeListBuilder.create().texOffs(19, 16).addBox(0.5F, 15.0F, -5.0F, 0.0F, 1.0F, 3.0F),
                PartPose.offsetAndRotation(3.0F, 8.0F, 1.0F, 0.0F, -0.260246F, 0.0F));
        root.addOrReplaceChild(
                "Lfoot4",
                CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, 14.0F, -1.0F, 2.0F, 2.0F, 4.0F),
                PartPose.offset(3.0F, 8.0F, 1.0F));
        root.addOrReplaceChild(
                "LClaw4",
                CubeListBuilder.create().texOffs(16, 10).addBox(0.0F, 14.0F, 2.0F, 0.0F, 2.0F, 3.0F),
                PartPose.offset(3.0F, 8.0F, 1.0F));
        root.addOrReplaceChild(
                "Rfoot1",
                CubeListBuilder.create().texOffs(29, 50).addBox(-2.0F, 14.0F, -5.0F, 2.0F, 2.0F, 6.0F),
                PartPose.offset(-2.0F, 8.0F, 1.0F));
        root.addOrReplaceChild(
                "Rfoot2",
                CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, 14.0F, -1.0F, 2.0F, 2.0F, 4.0F),
                PartPose.offset(-2.0F, 8.0F, 1.0F));
        root.addOrReplaceChild(
                "Rclaw1",
                CubeListBuilder.create().texOffs(16, 10).addBox(-1.0F, 14.0F, -7.0F, 0.0F, 2.0F, 3.0F),
                PartPose.offset(-2.0F, 8.0F, 1.0F));
        root.addOrReplaceChild(
                "Rfoot3",
                CubeListBuilder.create().texOffs(0, 9).addBox(-2.0F, 15.0F, -4.0F, 2.0F, 1.0F, 5.0F),
                PartPose.offsetAndRotation(-2.0F, 8.0F, 1.0F, 0.0F, -0.260246F, 0.0F));
        root.addOrReplaceChild(
                "Rclaw3",
                CubeListBuilder.create().texOffs(19, 16).addBox(-0.5F, 15.0F, -5.0F, 0.0F, 1.0F, 3.0F),
                PartPose.offsetAndRotation(-2.0F, 8.0F, 1.0F, 0.0F, -0.260246F, 0.0F));
        root.addOrReplaceChild(
                "Rfoot4",
                CubeListBuilder.create().texOffs(0, 9).addBox(-2.0F, 15.0F, -4.0F, 2.0F, 1.0F, 5.0F),
                PartPose.offsetAndRotation(-2.0F, 8.0F, 1.0F, 0.0F, 0.2602503F, 0.0F));
        root.addOrReplaceChild(
                "Rclaw2",
                CubeListBuilder.create().texOffs(19, 16).addBox(-1.5F, 15.0F, -5.0F, 0.0F, 1.0F, 3.0F),
                PartPose.offsetAndRotation(-2.0F, 8.0F, 1.0F, 0.0F, 0.260246F, 0.0F));
        root.addOrReplaceChild(
                "Rclaw4",
                CubeListBuilder.create().texOffs(16, 10).addBox(-1.0F, 14.0F, 2.0F, 0.0F, 2.0F, 3.0F),
                PartPose.offset(-2.0F, 8.0F, 1.0F));
        root.addOrReplaceChild(
                "Body3",
                CubeListBuilder.create().texOffs(17, 96).addBox(-3.0F, 0.0F, 0.0F, 6.0F, 7.0F, 3.0F),
                PartPose.offset(0.0F, 2.0F, 6.0F));
        root.addOrReplaceChild(
                "Tail1",
                CubeListBuilder.create().texOffs(33, 81).addBox(-2.0F, 0.0F, 0.0F, 4.0F, 0.0F, 14.0F),
                PartPose.offsetAndRotation(0.0F, 3.0F, 9.0F, -0.5948578F, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "Tail2",
                CubeListBuilder.create().texOffs(36, 97).addBox(-1.0F, 0.0F, 0.0F, 3.0F, 0.0F, 13.0F),
                PartPose.offsetAndRotation(0.0F, 3.0F, 8.0F, -0.5948578F, 0.3346075F, 0.0F));
        root.addOrReplaceChild(
                "Tail3",
                CubeListBuilder.create().texOffs(36, 97).addBox(-2.0F, 0.0F, 0.0F, 3.0F, 0.0F, 13.0F),
                PartPose.offsetAndRotation(0.0F, 3.0F, 8.0F, -0.5948578F, -0.3346145F, 0.0F));
        root.addOrReplaceChild(
                "Body4",
                CubeListBuilder.create().texOffs(17, 89).addBox(-2.0F, 0.0F, 0.0F, 4.0F, 3.0F, 3.0F),
                PartPose.offsetAndRotation(0.0F, 6.0F, 7.0F, 1.003822F, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "head",
                CubeListBuilder.create().texOffs(74, 48).addBox(-1.0F, -24.0F, -7.0F, 2.0F, 2.0F, 4.0F),
                PartPose.offset(0.0F, 5.0F, -7.0F));
        root.addOrReplaceChild(
                "leftleg",
                CubeListBuilder.create().texOffs(0, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 5.0F),
                PartPose.offsetAndRotation(3.0F, 8.0F, 1.0F, -0.2974289F, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "Neck1",
                CubeListBuilder.create().texOffs(79, 84).addBox(-1.5F, -21.0F, -2.0F, 3.0F, 21.0F, 3.0F),
                PartPose.offsetAndRotation(0.0F, 5.0F, -7.0F, 0.0F, -0.0349066F, 0.0F));
        root.addOrReplaceChild(
                "Head1",
                CubeListBuilder.create().texOffs(0, 70).addBox(-2.0F, -25.0F, -3.0F, 4.0F, 4.0F, 4.0F),
                PartPose.offset(0.0F, 5.0F, -7.0F));
        root.addOrReplaceChild(
                "mouth1",
                CubeListBuilder.create().texOffs(74, 64).addBox(-1.0F, -22.0F, -6.0F, 2.0F, 1.0F, 3.0F),
                PartPose.offset(0.0F, 5.0F, -7.0F));
        root.addOrReplaceChild(
                "neck2",
                CubeListBuilder.create().texOffs(0, 99).addBox(-1.0F, -2.0F, -2.0F, 2.0F, 4.0F, 3.0F),
                PartPose.offset(0.0F, 5.0F, -6.9F));
        root.addOrReplaceChild(
                "rightleg",
                CubeListBuilder.create().texOffs(0, 16).addBox(-3.0F, 0.0F, -2.0F, 4.0F, 6.0F, 5.0F),
                PartPose.offsetAndRotation(-2.0F, 8.0F, 1.0F, -0.2974216F, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "Lwing",
                CubeListBuilder.create().texOffs(0, 107).addBox(0.0F, 0.0F, 0.0F, 1.0F, 7.0F, 11.0F),
                PartPose.offset(4.0F, 1.0F, -5.0F));
        root.addOrReplaceChild(
                "Rwing",
                CubeListBuilder.create().texOffs(0, 107).addBox(0.0F, 0.0F, 0.0F, 1.0F, 7.0F, 11.0F),
                PartPose.offset(-5.0F, 1.0F, -5.0F));
        root.addOrReplaceChild(
                "Hat1",
                CubeListBuilder.create().texOffs(40, 0).addBox(-2.5F, -26.0F, -4.0F, 5.0F, 1.0F, 5.0F),
                PartPose.offset(0.0F, 5.0F, -7.0F));
        root.addOrReplaceChild(
                "Hat2",
                CubeListBuilder.create().texOffs(40, 0).addBox(-2.0F, -28.0F, -3.0F, 4.0F, 2.0F, 4.0F),
                PartPose.offset(0.0F, 5.0F, -7.0F));

        return LayerDefinition.create(mesh, 256, 128);
    }

    @Override
    public ModelPart root() {
        return this.root;
    }

    @Override
    public void setupAnim(
            Ostrich entity,
            float limbSwing,
            float limbSwingAmount,
            float ageInTicks,
            float netHeadYaw,
            float headPitch) {
        // gold func_78088_a — full port
        float f3 = netHeadYaw;
        float newangle;
        float nextangle;
        float lspeed;

        // gold: horizontal speed from previous-frame position delta
        lspeed = (float) ((entity.xo - entity.getX()) * (entity.xo - entity.getX())
                + (entity.zo - entity.getZ()) * (entity.zo - entity.getZ()));
        lspeed = (float) Math.sqrt(lspeed);
        newangle = Mth.cos(ageInTicks * 1.25F * this.wingspeed) * (float) Math.PI * lspeed * 0.4F;
        if (newangle > 0.5F) {
            newangle = 0.75F;
        }
        if (newangle < -0.5F) {
            newangle = -0.75F;
        }

        this.leftleg.xRot = -0.297F + newangle;
        this.LLeg1.xRot = 0.483F + newangle;
        this.LLeg2.xRot = -0.437F + newangle;
        this.Lfoot1.xRot = newangle;
        this.Lfoot2.xRot = newangle;
        this.Lfoot3.xRot = newangle;
        this.Lfoot4.xRot = newangle;
        this.LClaw1.xRot = newangle;
        this.LClaw2.xRot = newangle;
        this.LClaw3.xRot = newangle;
        this.LClaw4.xRot = newangle;
        this.rightleg.xRot = -0.297F - newangle;
        this.Rleg1.xRot = 0.483F - newangle;
        this.RLeg2.xRot = -0.437F - newangle;
        this.Rfoot1.xRot = -newangle;
        this.Rfoot2.xRot = -newangle;
        this.Rfoot3.xRot = -newangle;
        this.Rfoot4.xRot = -newangle;
        this.Rclaw1.xRot = -newangle;
        this.Rclaw2.xRot = -newangle;
        this.Rclaw3.xRot = -newangle;
        this.Rclaw4.xRot = -newangle;

        this.Tail1.xRot = -0.594F + Mth.cos(ageInTicks * 0.05F) * (float) Math.PI * 0.06F;
        this.Tail2.xRot = this.Tail1.xRot;
        this.Tail3.xRot = this.Tail1.xRot;
        this.Tail3.yRot = -0.334F + Mth.cos(ageInTicks * 0.061F) * (float) Math.PI * 0.08F;
        this.Tail2.yRot = 0.334F - Mth.cos(ageInTicks * 0.072F) * (float) Math.PI * 0.08F;

        RenderInfo r = entity.getRenderInfo();
        if (entity.isVehicle()) {
            // gold: rider look → smoothed rf1
            f3 = (entity.yRotO - entity.getYRot()) * 20.0F;
            f3 = -f3;
            r.rf1 = r.rf1 + (f3 - r.rf1) / 60.0F;
            if (r.rf1 > 50.0F) {
                r.rf1 = 50.0F;
            }
            if (r.rf1 < -50.0F) {
                r.rf1 = -50.0F;
            }
            f3 = r.rf1;
        } else {
            f3 /= 2.0F;
        }

        // gold: sitting + not activated → head tucked (xRot = π)
        if (entity.isOreSpawnSitting() && entity.get_is_activated() == 0) {
            f3 = 0.0F;
            this.Head1.xRot = 3.1415F;
            this.head.xRot = this.Head1.xRot;
            this.mouth1.xRot = this.Head1.xRot;
            this.Neck1.xRot = this.Head1.xRot;
            this.Hat1.xRot = this.Head1.xRot;
            this.Hat2.xRot = this.Head1.xRot;
        } else {
            this.Head1.xRot = 0.0F;
            this.head.xRot = this.Head1.xRot;
            this.mouth1.xRot = this.Head1.xRot;
            this.Neck1.xRot = this.Head1.xRot;
            this.Hat1.xRot = this.Head1.xRot;
            this.Hat2.xRot = this.Head1.xRot;
        }

        this.Head1.yRot = (float) Math.toRadians(f3) * 0.65F;
        this.head.yRot = this.Head1.yRot;
        this.mouth1.yRot = this.Head1.yRot;
        this.Hat1.yRot = this.Head1.yRot;
        this.Hat2.yRot = this.Head1.yRot;

        // gold wing flap state machine (ri1)
        newangle = Mth.cos(ageInTicks * 1.0F * this.wingspeed) * (float) Math.PI * 0.15F;
        nextangle = Mth.cos((ageInTicks + 0.3F) * 1.0F * this.wingspeed) * (float) Math.PI * 0.15F;
        if (nextangle > 0.0F && newangle < 0.0F) {
            r.ri1 = 0;
            if (entity.getRandom().nextInt(3) == 1) {
                r.ri1 = 1;
            }
        }
        if (r.ri1 == 0) {
            newangle = 0.0F;
        }
        newangle = Math.abs(newangle);
        this.Lwing.zRot = -newangle;
        this.Lwing.yRot = newangle / 2.0F;
        this.Rwing.zRot = newangle;
        this.Rwing.yRot = -newangle / 2.0F;
        entity.setRenderInfo(r);

        // gold: hats only when cannon-fodder activated
        int act = entity.get_is_activated();
        this.Hat1.visible = act != 0;
        this.Hat2.visible = act > 1;
    }
}
