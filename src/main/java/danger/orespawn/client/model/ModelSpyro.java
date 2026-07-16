package danger.orespawn.client.model;

import danger.orespawn.entity.Spyro;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

/**
 * Port of gold {@code ModelSpyro} (1.12 ModelBase) → 1.21 HierarchicalModel.
 */
@OnlyIn(Dist.CLIENT)
public class ModelSpyro extends HierarchicalModel<Spyro> {
    private final float wingspeed;
    private final ModelPart root;
    private final ModelPart RightFrontPaw;
    private final ModelPart WingLeft;
    private final ModelPart LegRightFrontTop;
    private final ModelPart LegRightFrontBottom;
    private final ModelPart LegRightBackTop;
    private final ModelPart LegRightBackBottom;
    private final ModelPart RightBackPaw;
    private final ModelPart LegLeftFrontTop;
    private final ModelPart SnoutRight;
    private final ModelPart LeftFrontPaw;
    private final ModelPart LegLeftBackTop;
    private final ModelPart LegLeftBackBottom;
    private final ModelPart LeftBackPaw;
    private final ModelPart LegLeftFrontBottom;
    private final ModelPart TailPieceSmall;
    private final ModelPart JawPiece;
    private final ModelPart HeadPieceBottom;
    private final ModelPart HeadPieceTop;
    private final ModelPart HornRightBottom;
    private final ModelPart HornLeftBottom;
    private final ModelPart HornRightTop;
    private final ModelPart HornLeftTop;
    private final ModelPart Torso;
    private final ModelPart SnoutLeft;
    private final ModelPart WingPieceLeft;
    private final ModelPart WingRight;
    private final ModelPart WingPieceRight;
    private final ModelPart Neck;
    private final ModelPart TailBack;
    private final ModelPart TailFront;
    private final ModelPart ScaleBackHead;
    private final ModelPart TailPieceLarge;
    private final ModelPart ScaleTailPiece;
    private final ModelPart ScaleHead;
    private final ModelPart ScaleTop1;
    private final ModelPart ScaleBackPiece1;
    private final ModelPart ScaleBackPiece2;

    public ModelSpyro(ModelPart root) {
        this(root, 1F);
    }

    public ModelSpyro(ModelPart root, float wingspeed) {
        this.root = root;
        this.wingspeed = wingspeed;
        this.RightFrontPaw = root.getChild("RightFrontPaw");
        this.WingLeft = root.getChild("WingLeft");
        this.LegRightFrontTop = root.getChild("LegRightFrontTop");
        this.LegRightFrontBottom = root.getChild("LegRightFrontBottom");
        this.LegRightBackTop = root.getChild("LegRightBackTop");
        this.LegRightBackBottom = root.getChild("LegRightBackBottom");
        this.RightBackPaw = root.getChild("RightBackPaw");
        this.LegLeftFrontTop = root.getChild("LegLeftFrontTop");
        this.SnoutRight = root.getChild("SnoutRight");
        this.LeftFrontPaw = root.getChild("LeftFrontPaw");
        this.LegLeftBackTop = root.getChild("LegLeftBackTop");
        this.LegLeftBackBottom = root.getChild("LegLeftBackBottom");
        this.LeftBackPaw = root.getChild("LeftBackPaw");
        this.LegLeftFrontBottom = root.getChild("LegLeftFrontBottom");
        this.TailPieceSmall = root.getChild("TailPieceSmall");
        this.JawPiece = root.getChild("JawPiece");
        this.HeadPieceBottom = root.getChild("HeadPieceBottom");
        this.HeadPieceTop = root.getChild("HeadPieceTop");
        this.HornRightBottom = root.getChild("HornRightBottom");
        this.HornLeftBottom = root.getChild("HornLeftBottom");
        this.HornRightTop = root.getChild("HornRightTop");
        this.HornLeftTop = root.getChild("HornLeftTop");
        this.Torso = root.getChild("Torso");
        this.SnoutLeft = root.getChild("SnoutLeft");
        this.WingPieceLeft = root.getChild("WingPieceLeft");
        this.WingRight = root.getChild("WingRight");
        this.WingPieceRight = root.getChild("WingPieceRight");
        this.Neck = root.getChild("Neck");
        this.TailBack = root.getChild("TailBack");
        this.TailFront = root.getChild("TailFront");
        this.ScaleBackHead = root.getChild("ScaleBackHead");
        this.TailPieceLarge = root.getChild("TailPieceLarge");
        this.ScaleTailPiece = root.getChild("ScaleTailPiece");
        this.ScaleHead = root.getChild("ScaleHead");
        this.ScaleTop1 = root.getChild("ScaleTop1");
        this.ScaleBackPiece1 = root.getChild("ScaleBackPiece1");
        this.ScaleBackPiece2 = root.getChild("ScaleBackPiece2");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        root.addOrReplaceChild("RightFrontPaw",
                CubeListBuilder.create().texOffs(12, 31)
                        .addBox(0.0F, 5.0F, -4.0F, 2.0F, 1.0F, 4.0F),
                PartPose.offset(3.0F, 18.0F, -2.0F));
        root.addOrReplaceChild("WingLeft",
                CubeListBuilder.create().texOffs(2, 51)
                        .addBox(-10.0F, -1.0F, -2.0F, 10.0F, 0.0F, 4.0F),
                PartPose.offsetAndRotation(-1.0F, 16.0F, 0.0F, 0.1745329F, 0.0F, -0.1745329F));
        root.addOrReplaceChild("LegRightFrontTop",
                CubeListBuilder.create().texOffs(20, 19)
                        .addBox(0.0F, 0.0F, -2.0F, 2.0F, 3.0F, 3.0F),
                PartPose.offsetAndRotation(3.0F, 18.0F, -2.0F, -0.0872665F, 0.0F, 0.0F));
        root.addOrReplaceChild("LegRightFrontBottom",
                CubeListBuilder.create().texOffs(0, 25)
                        .addBox(0.0F, 2.0F, -1.5F, 2.0F, 4.0F, 2.0F),
                PartPose.offsetAndRotation(3.0F, 18.0F, -2.0F, -0.1745329F, 0.0F, 0.0F));
        root.addOrReplaceChild("LegRightBackTop",
                CubeListBuilder.create().texOffs(30, 19)
                        .addBox(0.0F, 0.0F, -2.0F, 2.0F, 3.0F, 3.0F),
                PartPose.offsetAndRotation(3.0F, 18.0F, 3.0F, 0.1396263F, 0.0F, 0.0F));
        root.addOrReplaceChild("LegRightBackBottom",
                CubeListBuilder.create().texOffs(16, 25)
                        .addBox(0.0F, 2.0F, -1.0F, 2.0F, 4.0F, 2.0F),
                PartPose.offsetAndRotation(3.0F, 18.0F, 3.0F, -0.1745329F, 0.0F, 0.0F));
        root.addOrReplaceChild("RightBackPaw",
                CubeListBuilder.create().texOffs(36, 31)
                        .addBox(0.0F, 5.0F, -3.0F, 2.0F, 1.0F, 4.0F),
                PartPose.offset(3.0F, 18.0F, 3.0F));
        root.addOrReplaceChild("LegLeftFrontTop",
                CubeListBuilder.create().texOffs(0, 19)
                        .addBox(-2.0F, 0.0F, -1.0F, 2.0F, 3.0F, 3.0F),
                PartPose.offsetAndRotation(-2.0F, 18.0F, -3.0F, -0.0872665F, 0.0F, 0.0F));
        root.addOrReplaceChild("SnoutRight",
                CubeListBuilder.create().texOffs(48, 2)
                        .addBox(1.0F, -3.0F, -5.0F, 1.0F, 1.0F, 1.0F),
                PartPose.offset(1.0F, 16.0F, -3.0F));
        root.addOrReplaceChild("LeftFrontPaw",
                CubeListBuilder.create().texOffs(0, 31)
                        .addBox(-2.0F, 5.0F, -3.0F, 2.0F, 1.0F, 4.0F),
                PartPose.offset(-2.0F, 18.0F, -3.0F));
        root.addOrReplaceChild("LegLeftBackTop",
                CubeListBuilder.create().texOffs(10, 19)
                        .addBox(-2.0F, 0.0F, -2.0F, 2.0F, 3.0F, 3.0F),
                PartPose.offsetAndRotation(-2.0F, 18.0F, 3.0F, 0.1396263F, 0.0F, 0.0F));
        root.addOrReplaceChild("LegLeftBackBottom",
                CubeListBuilder.create().texOffs(24, 25)
                        .addBox(-2.0F, 2.0F, -1.0F, 2.0F, 4.0F, 2.0F),
                PartPose.offsetAndRotation(-2.0F, 18.0F, 3.0F, -0.1745329F, 0.0F, 0.0F));
        root.addOrReplaceChild("LeftBackPaw",
                CubeListBuilder.create().texOffs(24, 31)
                        .addBox(-2.0F, 5.0F, -3.0F, 2.0F, 1.0F, 4.0F),
                PartPose.offset(-2.0F, 18.0F, 3.0F));
        root.addOrReplaceChild("LegLeftFrontBottom",
                CubeListBuilder.create().texOffs(8, 25)
                        .addBox(-2.0F, 2.0F, -0.5F, 2.0F, 4.0F, 2.0F),
                PartPose.offsetAndRotation(-2.0F, 18.0F, -3.0F, -0.1745329F, 0.0F, 0.0F));
        root.addOrReplaceChild("TailPieceSmall",
                CubeListBuilder.create().texOffs(28, 36)
                        .addBox(0.0F, -0.5F, 4.0F, 1.0F, 1.0F, 1.0F),
                PartPose.offsetAndRotation(0.0F, 16.0F, 7.0F, 0.1745329F, 0.0F, 0.0F));
        root.addOrReplaceChild("JawPiece",
                CubeListBuilder.create().texOffs(52, 0)
                        .addBox(-2.0F, -1.0F, -4.0F, 3.0F, 1.0F, 3.0F),
                PartPose.offsetAndRotation(1.0F, 16.0F, -3.0F, 0.1745329F, 0.0F, 0.0F));
        root.addOrReplaceChild("HeadPieceBottom",
                CubeListBuilder.create().texOffs(30, 7)
                        .addBox(-3.0F, -2.0F, -5.0F, 5.0F, 2.0F, 6.0F),
                PartPose.offset(1.0F, 16.0F, -3.0F));
        root.addOrReplaceChild("HeadPieceTop",
                CubeListBuilder.create().texOffs(30, 0)
                        .addBox(-3.0F, -5.0F, -3.0F, 5.0F, 3.0F, 4.0F),
                PartPose.offset(1.0F, 16.0F, -3.0F));
        root.addOrReplaceChild("HornRightBottom",
                CubeListBuilder.create().texOffs(8, 14)
                        .addBox(0.0F, -6.0F, -3.5F, 2.0F, 3.0F, 2.0F),
                PartPose.offsetAndRotation(1.0F, 16.0F, -3.0F, (float) (-Math.PI / 4), (float) (Math.PI / 4), 0.0F));
        root.addOrReplaceChild("HornLeftBottom",
                CubeListBuilder.create().texOffs(0, 14)
                        .addBox(-2.75F, -6.5F, -3.0F, 2.0F, 3.0F, 2.0F),
                PartPose.offsetAndRotation(1.0F, 16.0F, -3.0F, (float) (-Math.PI / 4), (float) (-Math.PI / 4), 0.0F));
        root.addOrReplaceChild("HornRightTop",
                CubeListBuilder.create().texOffs(20, 14)
                        .addBox(0.5F, -9.0F, -3.0F, 1.0F, 3.0F, 1.0F),
                PartPose.offsetAndRotation(1.0F, 16.0F, -3.0F, (float) (-Math.PI / 4), (float) (Math.PI / 4), 0.0F));
        root.addOrReplaceChild("HornLeftTop",
                CubeListBuilder.create().texOffs(16, 14)
                        .addBox(-2.2F, -9.5F, -2.5F, 1.0F, 3.0F, 1.0F),
                PartPose.offsetAndRotation(1.0F, 16.0F, -3.0F, (float) (-Math.PI / 4), (float) (-Math.PI / 4), 0.0F));
        root.addOrReplaceChild("Torso",
                CubeListBuilder.create().texOffs(0, 0)
                        .addBox(-2.0F, -2.0F, -5.0F, 5.0F, 4.0F, 10.0F),
                PartPose.offset(0.0F, 19.0F, 0.0F));
        root.addOrReplaceChild("SnoutLeft",
                CubeListBuilder.create().texOffs(48, 0)
                        .addBox(-3.0F, -3.0F, -5.0F, 1.0F, 1.0F, 1.0F),
                PartPose.offset(1.0F, 16.0F, -3.0F));
        root.addOrReplaceChild("WingPieceLeft",
                CubeListBuilder.create().texOffs(4, 42)
                        .addBox(-1.0F, -2.0F, -1.0F, 1.0F, 2.0F, 1.0F),
                PartPose.offsetAndRotation(0.0F, 17.2F, 0.0F, 0.1745329F, 0.0F, -0.1745329F));
        root.addOrReplaceChild("WingRight",
                CubeListBuilder.create().texOffs(2, 45)
                        .addBox(0.0F, -1.0F, -2.0F, 10.0F, 0.0F, 4.0F),
                PartPose.offsetAndRotation(2.0F, 16.0F, 0.0F, 0.1745329F, 0.0F, 0.1745329F));
        root.addOrReplaceChild("WingPieceRight",
                CubeListBuilder.create().texOffs(0, 42)
                        .addBox(-1.0F, -2.0F, 0.0F, 1.0F, 2.0F, 1.0F),
                PartPose.offsetAndRotation(2.0F, 17.5F, -1.0F, 0.1745329F, 0.0F, 0.1745329F));
        root.addOrReplaceChild("Neck",
                CubeListBuilder.create().texOffs(52, 7)
                        .addBox(-1.0F, -2.0F, -1.0F, 3.0F, 3.0F, 3.0F),
                PartPose.offsetAndRotation(0.0F, 17.0F, -4.0F, 0.4537856F, 0.0F, 0.0F));
        root.addOrReplaceChild("TailBack",
                CubeListBuilder.create().texOffs(0, 36)
                        .addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 4.0F),
                PartPose.offsetAndRotation(0.5F, 17.5F, 5.0F, 0.4537856F, 0.0F, 0.0F));
        root.addOrReplaceChild("TailFront",
                CubeListBuilder.create().texOffs(12, 36)
                        .addBox(0.0F, 0.0F, -1.0F, 1.0F, 1.0F, 4.0F),
                PartPose.offsetAndRotation(0.0F, 16.0F, 7.0F, (float) (Math.PI / 12), 0.0F, 0.0F));
        root.addOrReplaceChild("ScaleBackHead",
                CubeListBuilder.create().texOffs(38, 36)
                        .addBox(-1.0F, -3.0F, 2.0F, 1.0F, 2.0F, 1.0F),
                PartPose.offset(1.0F, 16.0F, -4.0F));
        root.addOrReplaceChild("TailPieceLarge",
                CubeListBuilder.create().texOffs(22, 36)
                        .addBox(0.0F, -1.0F, 2.0F, 1.0F, 2.0F, 2.0F),
                PartPose.offsetAndRotation(0.0F, 16.0F, 7.0F, 0.1745329F, 0.0F, 0.0F));
        root.addOrReplaceChild("ScaleTailPiece",
                CubeListBuilder.create().texOffs(48, 36)
                        .addBox(-0.5F, -2.0F, 0.2F, 1.0F, 1.0F, 2.0F),
                PartPose.offsetAndRotation(0.5F, 17.5F, 5.0F, 0.4537856F, 0.0F, 0.0F));
        root.addOrReplaceChild("ScaleHead",
                CubeListBuilder.create().texOffs(42, 36)
                        .addBox(-1.0F, -6.0F, 0.0F, 1.0F, 2.0F, 2.0F),
                PartPose.offset(1.0F, 16.0F, -3.0F));
        root.addOrReplaceChild("ScaleTop1",
                CubeListBuilder.create().texOffs(48, 36)
                        .addBox(-1.0F, -6.0F, -4.0F, 1.0F, 1.0F, 2.0F),
                PartPose.offset(1.0F, 16.0F, -2.0F));
        root.addOrReplaceChild("ScaleBackPiece1",
                CubeListBuilder.create().texOffs(48, 36)
                        .addBox(0.0F, -1.0F, -1.0F, 1.0F, 1.0F, 2.0F),
                PartPose.offset(0.0F, 17.0F, 0.0F));
        root.addOrReplaceChild("ScaleBackPiece2",
                CubeListBuilder.create().texOffs(48, 36)
                        .addBox(0.0F, -1.0F, -1.0F, 1.0F, 1.0F, 2.0F),
                PartPose.offset(0.0F, 17.0F, 3.0F));
        return LayerDefinition.create(mesh, 64, 64);
    }

    @Override
    public ModelPart root() {
        return this.root;
    }

    @Override
    public void setupAnim(Spyro entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        animate(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
    }

    private void animate(Spyro entity, float f, float f1, float f2, float f3, float f4) {

        float hf = 0.0F;
        float newangle = 0.0F;
        int current_activity = entity.getActivity();
        if (f1 > 0.1) {
        newangle = Mth.cos(f2 * 2.3F * this.wingspeed) * (float) Math.PI * 0.4F * f1;
        } else {
        newangle = 0.0F;
        }
        if (current_activity == 3) {
        newangle *= 0.5F;
        }
        this.WingLeft.zRot = newangle;
        this.WingRight.zRot = -newangle;
        if (f1 > 0.1) {
        newangle = Mth.cos(f2 * 2.0F * this.wingspeed) * (float) Math.PI * 0.25F * f1;
        } else {
        newangle = 0.0F;
        }
        if (current_activity == 3) {
        newangle = 0.0F;
        }
        if (current_activity != 2) {
        this.LegRightFrontTop.xRot = newangle - 0.087F;
        this.LegRightFrontBottom.xRot = newangle - 0.17F;
        this.RightFrontPaw.xRot = newangle;
        this.LegLeftFrontTop.xRot = -newangle - 0.087F;
        this.LegLeftFrontBottom.xRot = -newangle - 0.17F;
        this.LeftFrontPaw.xRot = -newangle;
        this.LegRightBackBottom.xRot = -newangle + 0.139F;
        this.LegRightBackTop.xRot = -newangle - 0.174F;
        this.RightBackPaw.xRot = -newangle;
        this.LegLeftBackBottom.xRot = newangle + 0.139F;
        this.LegLeftBackTop.xRot = newangle - 0.174F;
        this.LeftBackPaw.xRot = newangle;
        } else {
        newangle = -1.0F;
        this.LegRightFrontTop.xRot = newangle - 0.087F;
        this.LegRightFrontBottom.xRot = newangle - 0.17F;
        this.RightFrontPaw.xRot = newangle;
        this.LegLeftFrontTop.xRot = newangle - 0.087F;
        this.LegLeftFrontBottom.xRot = newangle - 0.17F;
        this.LeftFrontPaw.xRot = newangle;
        newangle = 1.0F;
        this.LegRightBackBottom.xRot = newangle + 0.139F;
        this.LegRightBackTop.xRot = newangle - 0.174F;
        this.RightBackPaw.xRot = newangle;
        this.LegLeftBackBottom.xRot = newangle + 0.139F;
        this.LegLeftBackTop.xRot = newangle - 0.174F;
        this.LeftBackPaw.xRot = newangle;
        }
        newangle = Mth.cos(f2 * 1.2F * this.wingspeed) * (float) Math.PI * 0.25F;
        if (false /* sitting deferred */ || current_activity == 3) {
        newangle = 0.0F;
        }
        this.TailBack.yRot = newangle;
        this.ScaleTailPiece.yRot = newangle;
        this.TailFront.z = this.TailBack.z + (float)Math.cos(this.TailBack.yRot) * 3.0F;
        this.TailFront.x = this.TailBack.x + (float)Math.sin(this.TailBack.yRot) * 3.0F - 0.5F;
        this.TailFront.yRot = newangle * 1.6F;
        this.TailPieceLarge.z = this.TailFront.z;
        this.TailPieceLarge.x = this.TailFront.x;
        this.TailPieceLarge.yRot = this.TailFront.yRot;
        this.TailPieceSmall.z = this.TailFront.z;
        this.TailPieceSmall.x = this.TailFront.x;
        this.TailPieceSmall.yRot = this.TailFront.yRot;
        this.HeadPieceTop.yRot = (float)Math.toRadians(f3);
        this.HeadPieceBottom.yRot = (float)Math.toRadians(f3);
        this.JawPiece.yRot = (float)Math.toRadians(f3);
        this.SnoutRight.yRot = (float)Math.toRadians(f3);
        this.SnoutLeft.yRot = (float)Math.toRadians(f3);
        this.ScaleTop1.yRot = (float)Math.toRadians(f3);
        this.ScaleHead.yRot = (float)Math.toRadians(f3);
        this.ScaleBackHead.yRot = (float)Math.toRadians(f3);
        this.HornRightBottom.yRot = (float)Math.toRadians(f3) + 0.785F;
        this.HornRightTop.yRot = (float)Math.toRadians(f3) + 0.785F;
        this.HornLeftBottom.yRot = (float)Math.toRadians(f3) - 0.785F;
        this.HornLeftTop.yRot = (float)Math.toRadians(f3) - 0.785F;
        this.HeadPieceTop.xRot = (float)Math.toRadians(f4);
        this.HeadPieceBottom.xRot = (float)Math.toRadians(f4);
        this.JawPiece.xRot = (float)Math.toRadians(f4);
        this.SnoutRight.xRot = (float)Math.toRadians(f4);
        this.SnoutLeft.xRot = (float)Math.toRadians(f4);
        this.ScaleTop1.xRot = (float)Math.toRadians(f4);
        this.ScaleHead.xRot = (float)Math.toRadians(f4);
        this.ScaleBackHead.xRot = (float)Math.toRadians(f4);
        this.HornRightBottom.xRot = (float)Math.toRadians(f4) - 0.785F;
        this.HornRightTop.xRot = (float)Math.toRadians(f4) - 0.785F;
        this.HornLeftBottom.xRot = (float)Math.toRadians(f4) - 0.785F;
        this.HornLeftTop.xRot = (float)Math.toRadians(f4) - 0.785F;
    }
}
