package danger.orespawn.client.model;

import danger.orespawn.entity.Bee;
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
 * Port of gold {@code ModelBee} (1.7.10 ModelBase) → 1.21 HierarchicalModel.
 * Cubes/UVs 1:1. Texture 256×256. Wingspeed default 2.0F matches ClientProxy.
 */
@OnlyIn(Dist.CLIENT)
public class ModelBee extends HierarchicalModel<Bee> {
    /** Model layer location. */
    public static final ModelLayerLocation LAYER_LOCATION =
            new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "bee"), "main");

    private static final float AB5_BASE_Y = -6.0F;
    private static final float AB5_BASE_Z = -15.0F;

    private final float wingspeed;
    private final ModelPart root;
    private final ModelPart Sting;
    private final ModelPart Abdomnem1;
    private final ModelPart Abdomnem2;
    private final ModelPart Abdomnem3;
    private final ModelPart Abdomnem4;
    private final ModelPart Abdomnem5;
    private final ModelPart MainBody;
    private final ModelPart Neck;
    private final ModelPart Head;
    private final ModelPart WingRight;
    private final ModelPart WingLeft;
    private final ModelPart RA1;
    private final ModelPart LA1;
    private final ModelPart LA2;
    private final ModelPart RA2;
    private final ModelPart RA3;
    private final ModelPart LA3;
    private final ModelPart LeftPom;
    private final ModelPart RightPom;
    private final ModelPart LeftPincerExtra;
    private final ModelPart LeftPincerMain;
    private final ModelPart RightPincerMain;
    private final ModelPart RightPincerExtra;

    public ModelBee(ModelPart root) {
        this(root, 2.0F);
    }

    public ModelBee(ModelPart root, float wingspeed) {
        this.root = root;
        this.wingspeed = wingspeed;
        this.Sting = root.getChild("Sting");
        this.Abdomnem1 = root.getChild("Abdomnem1");
        this.Abdomnem2 = root.getChild("Abdomnem2");
        this.Abdomnem3 = root.getChild("Abdomnem3");
        this.Abdomnem4 = root.getChild("Abdomnem4");
        this.Abdomnem5 = root.getChild("Abdomnem5");
        this.MainBody = root.getChild("MainBody");
        this.Neck = root.getChild("Neck");
        this.Head = root.getChild("Head");
        this.WingRight = root.getChild("WingRight");
        this.WingLeft = root.getChild("WingLeft");
        this.RA1 = root.getChild("RA1");
        this.LA1 = root.getChild("LA1");
        this.LA2 = root.getChild("LA2");
        this.RA2 = root.getChild("RA2");
        this.RA3 = root.getChild("RA3");
        this.LA3 = root.getChild("LA3");
        this.LeftPom = root.getChild("LeftPom");
        this.RightPom = root.getChild("RightPom");
        this.LeftPincerExtra = root.getChild("LeftPincerExtra");
        this.LeftPincerMain = root.getChild("LeftPincerMain");
        this.RightPincerMain = root.getChild("RightPincerMain");
        this.RightPincerExtra = root.getChild("RightPincerExtra");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        root.addOrReplaceChild(
                "Sting",
                CubeListBuilder.create().texOffs(68, 0).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 10.0F, 2.0F),
                PartPose.offsetAndRotation(0.0F, 16.0F, 1.0F, (float) (-Math.PI / 4), 0.0F, 0.0F));
        root.addOrReplaceChild(
                "Abdomnem1",
                CubeListBuilder.create().texOffs(64, 12).addBox(-2.0F, 0.0F, 0.0F, 4.0F, 8.0F, 4.0F),
                PartPose.offsetAndRotation(0.0F, 9.0F, 2.0F, (float) (-Math.PI / 6), 0.0F, 0.0F));
        root.addOrReplaceChild(
                "Abdomnem2",
                CubeListBuilder.create().texOffs(60, 24).addBox(-3.0F, 0.0F, 0.0F, 6.0F, 6.0F, 6.0F),
                PartPose.offset(0.0F, 5.0F, 0.0F));
        root.addOrReplaceChild(
                "Abdomnem3",
                CubeListBuilder.create().texOffs(56, 36).addBox(-4.0F, 0.0F, 0.0F, 8.0F, 7.0F, 8.0F),
                PartPose.offsetAndRotation(0.0F, 1.0F, -2.0F, (float) (Math.PI / 12), 0.0F, 0.0F));
        root.addOrReplaceChild(
                "Abdomnem4",
                CubeListBuilder.create().texOffs(53, 51).addBox(-5.0F, 0.0F, 0.0F, 10.0F, 12.0F, 10.0F),
                PartPose.offsetAndRotation(0.0F, -6.0F, -8.0F, 0.5934119F, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "Abdomnem5",
                CubeListBuilder.create().texOffs(48, 73).addBox(-6.0F, 0.0F, 0.0F, 12.0F, 12.0F, 12.0F),
                PartPose.offsetAndRotation(0.0F, -6.0F, -15.0F, 1.099557F, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "MainBody",
                CubeListBuilder.create().texOffs(48, 97).addBox(-6.0F, 0.0F, -6.0F, 12.0F, 14.0F, 12.0F),
                PartPose.offsetAndRotation(0.0F, -12.0F, -24.0F, 1.48353F, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "Neck",
                CubeListBuilder.create().texOffs(55, 123).addBox(-4.0F, -4.0F, -8.0F, 8.0F, 8.0F, 8.0F),
                PartPose.offset(0.0F, -12.0F, -23.0F));
        root.addOrReplaceChild(
                "Head",
                CubeListBuilder.create().texOffs(51, 139).addBox(-5.0F, -5.0F, -10.0F, 10.0F, 10.0F, 10.0F),
                PartPose.offsetAndRotation(0.0F, -13.0F, -28.0F, (float) (Math.PI / 12), 0.0F, 0.0F));
        root.addOrReplaceChild(
                "WingRight",
                CubeListBuilder.create().texOffs(0, 91).addBox(0.0F, 0.0F, 0.0F, 0.0F, 8.0F, 24.0F),
                PartPose.offsetAndRotation(
                        -4.0F, -14.0F, -15.0F, (float) (-Math.PI / 4), (float) (-Math.PI / 6), 2.617994F));
        root.addOrReplaceChild(
                "WingLeft",
                CubeListBuilder.create().texOffs(96, 91).addBox(0.0F, 0.0F, 0.0F, 0.0F, 8.0F, 24.0F),
                PartPose.offsetAndRotation(
                        3.0F, -14.0F, -15.0F, (float) (-Math.PI / 4), (float) (Math.PI / 6), -2.617994F));
        root.addOrReplaceChild(
                "RA1",
                CubeListBuilder.create().texOffs(47, 152).addBox(0.0F, -6.0F, -1.0F, 1.0F, 6.0F, 1.0F),
                PartPose.offsetAndRotation(
                        -3.0F, -17.0F, -31.0F, (float) (Math.PI / 12), (float) (Math.PI / 6), 0.0F));
        root.addOrReplaceChild(
                "LA1",
                CubeListBuilder.create().texOffs(91, 152).addBox(0.0F, -6.0F, -1.0F, 1.0F, 6.0F, 1.0F),
                PartPose.offsetAndRotation(
                        2.0F, -17.0F, -32.0F, (float) (Math.PI / 12), (float) (-Math.PI / 6), 0.0F));
        root.addOrReplaceChild(
                "LA2",
                CubeListBuilder.create().texOffs(91, 145).addBox(0.0F, -11.0F, 0.0F, 1.0F, 6.0F, 1.0F),
                PartPose.offsetAndRotation(2.0F, -17.0F, -32.0F, 0.4363323F, -0.6108652F, 0.0F));
        root.addOrReplaceChild(
                "RA2",
                CubeListBuilder.create().texOffs(47, 145).addBox(0.0F, -11.0F, 0.0F, 1.0F, 6.0F, 1.0F),
                PartPose.offsetAndRotation(-3.0F, -17.0F, -31.0F, 0.4363323F, 0.6108652F, 0.0F));
        root.addOrReplaceChild(
                "RA3",
                CubeListBuilder.create().texOffs(47, 138).addBox(0.0F, -16.0F, 2.0F, 1.0F, 6.0F, 1.0F),
                PartPose.offsetAndRotation(
                        -3.0F, -17.0F, -31.0F, 0.6108652F, (float) (Math.PI * 2.0 / 9.0), 0.0F));
        root.addOrReplaceChild(
                "LA3",
                CubeListBuilder.create().texOffs(91, 138).addBox(0.0F, -16.0F, 2.0F, 1.0F, 6.0F, 1.0F),
                PartPose.offsetAndRotation(
                        2.0F, -17.0F, -32.0F, 0.6108652F, (float) (-Math.PI * 2.0 / 9.0), 0.0F));
        root.addOrReplaceChild(
                "LeftPom",
                CubeListBuilder.create().texOffs(89, 134).addBox(4.0F, -16.0F, -6.0F, 2.0F, 2.0F, 2.0F),
                PartPose.offset(2.0F, -17.0F, -32.0F));
        root.addOrReplaceChild(
                "RightPom",
                CubeListBuilder.create().texOffs(45, 134).addBox(-5.0F, -16.0F, -7.0F, 2.0F, 2.0F, 2.0F),
                PartPose.offset(-3.0F, -17.0F, -31.0F));
        root.addOrReplaceChild(
                "LeftPincerExtra",
                CubeListBuilder.create().texOffs(71, 166).addBox(-2.0F, 0.0F, -6.0F, 2.0F, 1.0F, 2.0F),
                PartPose.offsetAndRotation(2.0F, -8.0F, -36.0F, 0.1745329F, -0.1745329F, 0.0F));
        root.addOrReplaceChild(
                "LeftPincerMain",
                CubeListBuilder.create().texOffs(71, 159).addBox(0.0F, 0.0F, -6.0F, 2.0F, 1.0F, 6.0F),
                PartPose.offsetAndRotation(2.0F, -8.0F, -36.0F, 0.1745329F, -0.1745329F, 0.0F));
        root.addOrReplaceChild(
                "RightPincerMain",
                CubeListBuilder.create().texOffs(55, 159).addBox(0.0F, 0.0F, -6.0F, 2.0F, 1.0F, 6.0F),
                PartPose.offsetAndRotation(-4.0F, -8.0F, -36.0F, 0.1745329F, 0.1745329F, 0.0F));
        root.addOrReplaceChild(
                "RightPincerExtra",
                CubeListBuilder.create().texOffs(63, 166).addBox(2.0F, 0.0F, -6.0F, 2.0F, 1.0F, 2.0F),
                PartPose.offsetAndRotation(-4.0F, -8.0F, -36.0F, 0.1745329F, 0.1745329F, 0.0F));

        return LayerDefinition.create(mesh, 256, 256);
    }

    @Override
    public ModelPart root() {
        return this.root;
    }

    @Override
    public void setupAnim(
            Bee entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        animate(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
    }

    private void animate(Bee entity, float f, float f1, float f2, float f3, float f4) {
        float newangle = Mth.cos(f2 * 1.1F * this.wingspeed) * (float) Math.PI * 0.3F;
        this.WingLeft.zRot = -1.745F - newangle;
        this.WingRight.zRot = 1.754F + newangle;

        newangle = Mth.cos(f2 * 0.3F * this.wingspeed) * (float) Math.PI * 0.1F;
        this.LeftPincerMain.yRot = -0.274F + newangle;
        this.LeftPincerExtra.yRot = -0.274F + newangle;
        this.RightPincerMain.yRot = 0.274F - newangle;
        this.RightPincerExtra.yRot = 0.274F - newangle;

        newangle = Mth.cos(f2 * 0.21F * this.wingspeed) * (float) Math.PI * 0.06F;
        this.LA1.xRot = 0.261F + newangle;
        this.LA2.xRot = 0.436F + newangle;
        this.LA3.xRot = 0.611F + newangle;
        this.LeftPom.xRot = newangle;

        newangle = Mth.cos(f2 * 0.27F * this.wingspeed) * (float) Math.PI * 0.06F;
        this.RA1.xRot = 0.261F + newangle;
        this.RA2.xRot = 0.436F + newangle;
        this.RA3.xRot = 0.611F + newangle;
        this.RightPom.xRot = newangle;

        newangle = Mth.cos(f2 * 0.31F * this.wingspeed) * (float) Math.PI * 0.06F;
        this.LA1.zRot = newangle;
        this.LA2.zRot = newangle;
        this.LA3.zRot = newangle;
        this.LeftPom.zRot = newangle;

        newangle = Mth.cos(f2 * 0.37F * this.wingspeed) * (float) Math.PI * 0.06F;
        this.RA1.zRot = newangle;
        this.RA2.zRot = newangle;
        this.RA3.zRot = newangle;
        this.RightPom.zRot = newangle;

        if (entity.getAttacking() == 0) {
            newangle = Mth.cos(f2 * 0.021F * this.wingspeed) * (float) Math.PI * 0.023F;
        } else {
            newangle = Mth.cos(f2 * 0.11F * this.wingspeed) * (float) Math.PI * 0.055F;
        }

        // gold abdomen chain from Abdomnem5 base pose
        this.Abdomnem5.y = AB5_BASE_Y;
        this.Abdomnem5.z = AB5_BASE_Z;
        this.Abdomnem5.xRot = 1.099F + newangle;
        this.Abdomnem4.xRot = this.Abdomnem5.xRot + newangle - 0.35F;
        this.Abdomnem4.y = (float) (this.Abdomnem5.y + Math.cos(this.Abdomnem5.xRot) * 10.0);
        this.Abdomnem4.z = (float) (this.Abdomnem5.z + Math.sin(this.Abdomnem5.xRot) * 10.0);
        this.Abdomnem3.xRot = this.Abdomnem4.xRot + newangle - 0.35F;
        this.Abdomnem3.y = (float) (this.Abdomnem4.y + Math.cos(this.Abdomnem4.xRot) * 10.0);
        this.Abdomnem3.z = (float) (this.Abdomnem4.z + Math.sin(this.Abdomnem4.xRot) * 10.0);
        this.Abdomnem2.xRot = this.Abdomnem3.xRot + newangle - 0.35F;
        this.Abdomnem2.y = (float) (this.Abdomnem3.y + Math.cos(this.Abdomnem3.xRot) * 6.0);
        this.Abdomnem2.z = (float) (this.Abdomnem3.z + Math.sin(this.Abdomnem3.xRot) * 6.0);
        this.Abdomnem1.xRot = this.Abdomnem2.xRot + newangle - 0.35F;
        this.Abdomnem1.y = (float) (this.Abdomnem2.y + Math.cos(this.Abdomnem2.xRot) * 5.0);
        this.Abdomnem1.z = (float) (this.Abdomnem2.z + Math.sin(this.Abdomnem2.xRot) * 5.0);
        this.Sting.xRot = this.Abdomnem1.xRot + newangle - 0.35F;
        this.Sting.y = (float) (this.Abdomnem1.y + Math.cos(this.Abdomnem1.xRot) * 7.0);
        this.Sting.z = 1.0F + (float) (this.Abdomnem1.z + Math.sin(this.Abdomnem1.xRot) * 7.0);
    }
}
