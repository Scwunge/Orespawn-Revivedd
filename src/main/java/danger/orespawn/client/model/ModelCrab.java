package danger.orespawn.client.model;

import danger.orespawn.entity.Crab;
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
 * Port of gold {@code ModelCrab} (1.7.10 ModelBase, tex 256×512) → 1.21 HierarchicalModel.
 * Cubes/UVs/offsets 1:1. Gold reuses one leg triplet 8× during render; here 8 instances
 * (4 right + 4 left × z=0/10/20/30) so setupAnim can match without multi-pass render.
 */
@OnlyIn(Dist.CLIENT)
public class ModelCrab extends HierarchicalModel<Crab> {
    public static final ModelLayerLocation LAYER_LOCATION =
            new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "crab"), "main");

    private static final float[] LEG_Z = {0.0F, 10.0F, 20.0F, 30.0F};

    private final ModelPart root;
    private final ModelPart body1;
    private final ModelPart body2;
    private final ModelPart body3;
    private final ModelPart body4;
    private final ModelPart body5;
    private final ModelPart body6;
    private final ModelPart Leye1;
    private final ModelPart Reye1;
    private final ModelPart Leye2;
    private final ModelPart Reye2;
    private final ModelPart Lclaw1;
    private final ModelPart Lclaw2;
    private final ModelPart Lclaw3;
    private final ModelPart Lclaw4;
    private final ModelPart Lclaw5;
    private final ModelPart Rclaw1;
    private final ModelPart Rclaw2;
    private final ModelPart Rclaw3;
    private final ModelPart Rclaw4;
    private final ModelPart Rclaw5;
    private final ModelPart Rmouth;
    private final ModelPart Lmouth;
    /** [side 0=R 1=L][zIndex 0.3][seg 0=leg1 1=leg2 2=leg3] */
    private final ModelPart[][][] legs = new ModelPart[2][4][3];

    public ModelCrab(ModelPart root) {
        this.root = root;
        this.body1 = root.getChild("body1");
        this.body2 = root.getChild("body2");
        this.body3 = root.getChild("body3");
        this.body4 = root.getChild("body4");
        this.body5 = root.getChild("body5");
        this.body6 = root.getChild("body6");
        this.Leye1 = root.getChild("Leye1");
        this.Reye1 = root.getChild("Reye1");
        this.Leye2 = root.getChild("Leye2");
        this.Reye2 = root.getChild("Reye2");
        this.Lclaw1 = root.getChild("Lclaw1");
        this.Lclaw2 = root.getChild("Lclaw2");
        this.Lclaw3 = root.getChild("Lclaw3");
        this.Lclaw4 = root.getChild("Lclaw4");
        this.Lclaw5 = root.getChild("Lclaw5");
        this.Rclaw1 = root.getChild("Rclaw1");
        this.Rclaw2 = root.getChild("Rclaw2");
        this.Rclaw3 = root.getChild("Rclaw3");
        this.Rclaw4 = root.getChild("Rclaw4");
        this.Rclaw5 = root.getChild("Rclaw5");
        this.Rmouth = root.getChild("Rmouth");
        this.Lmouth = root.getChild("Lmouth");
        for (int side = 0; side < 2; side++) {
            for (int z = 0; z < 4; z++) {
                String prefix = (side == 0 ? "r" : "l") + "leg" + z + "_";
                this.legs[side][z][0] = root.getChild(prefix + "1");
                this.legs[side][z][1] = root.getChild(prefix + "2");
                this.legs[side][z][2] = root.getChild(prefix + "3");
            }
        }
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        root.addOrReplaceChild("body1",
                CubeListBuilder.create().texOffs(0, 450)
                        .addBox(-38.0F, -5.0F, -8.0F, 76.0F, 10.0F, 48.0F),
                PartPose.offset(0.0F, 0.0F, 0.0F));
        root.addOrReplaceChild("body2",
                CubeListBuilder.create().texOffs(0, 406)
                        .addBox(-32.0F, -10.0F, -10.0F, 64.0F, 5.0F, 34.0F),
                PartPose.offset(0.0F, 0.0F, 0.0F));
        root.addOrReplaceChild("body3",
                CubeListBuilder.create().texOffs(0, 357)
                        .addBox(0.0F, 0.0F, 0.0F, 8.0F, 4.0F, 40.0F),
                PartPose.offset(38.0F, -5.0F, -6.0F));
        root.addOrReplaceChild("body4",
                CubeListBuilder.create().texOffs(100, 357)
                        .addBox(0.0F, 0.0F, 0.0F, 8.0F, 4.0F, 40.0F),
                PartPose.offset(-46.0F, -5.0F, -6.0F));
        root.addOrReplaceChild("body5",
                CubeListBuilder.create().texOffs(0, 339)
                        .addBox(-25.0F, 0.0F, 0.0F, 50.0F, 4.0F, 10.0F),
                PartPose.offset(0.0F, -4.0F, 40.0F));
        root.addOrReplaceChild("body6",
                CubeListBuilder.create().texOffs(124, 342)
                        .addBox(-14.0F, 0.0F, 0.0F, 28.0F, 3.0F, 4.0F),
                PartPose.offset(0.0F, -10.0F, -14.0F));

        root.addOrReplaceChild("Leye1",
                CubeListBuilder.create().texOffs(62, 0)
                        .addBox(-0.5F, -12.0F, -0.5F, 1.0F, 12.0F, 1.0F),
                PartPose.offsetAndRotation(9.0F, -9.0F, -11.0F, 0.0F, 0.0F, 0.4886922F));
        root.addOrReplaceChild("Reye1",
                CubeListBuilder.create().texOffs(40, 0)
                        .addBox(-0.5F, -12.0F, -0.5F, 1.0F, 12.0F, 1.0F),
                PartPose.offsetAndRotation(-9.0F, -9.0F, -11.0F, 0.0F, 0.0F, -0.4886922F));
        root.addOrReplaceChild("Leye2",
                CubeListBuilder.create().texOffs(50, 0)
                        .addBox(-1.0F, -14.0F, -1.0F, 2.0F, 2.0F, 2.0F),
                PartPose.offsetAndRotation(9.0F, -9.0F, -11.0F, 0.0F, 0.0F, 0.4886922F));
        root.addOrReplaceChild("Reye2",
                CubeListBuilder.create().texOffs(26, 0)
                        .addBox(-1.0F, -14.0F, -1.0F, 2.0F, 2.0F, 2.0F),
                PartPose.offsetAndRotation(-9.0F, -9.0F, -11.0F, 0.0F, 0.0F, -0.4886922F));

        root.addOrReplaceChild("Lclaw1",
                CubeListBuilder.create().texOffs(0, 80)
                        .addBox(-4.0F, 0.0F, -14.0F, 8.0F, 4.0F, 18.0F),
                PartPose.offsetAndRotation(31.0F, -2.0F, -8.0F, 0.0F, -0.4886922F, 0.0F));
        root.addOrReplaceChild("Lclaw2",
                CubeListBuilder.create().texOffs(0, 105)
                        .addBox(-7.0F, -3.0F, -12.0F, 17.0F, 6.0F, 16.0F),
                PartPose.offsetAndRotation(37.0F, 0.0F, -20.0F, 0.0F, -0.1745329F, 0.0F));
        root.addOrReplaceChild("Lclaw3",
                CubeListBuilder.create().texOffs(0, 131)
                        .addBox(0.0F, -5.0F, -25.0F, 17.0F, 10.0F, 30.0F),
                PartPose.offsetAndRotation(37.0F, 0.0F, -31.0F, 0.0F, -0.4537856F, 0.0F));
        root.addOrReplaceChild("Lclaw4",
                CubeListBuilder.create().texOffs(0, 175)
                        .addBox(2.0F, -3.0F, -32.0F, 11.0F, 5.0F, 12.0F),
                PartPose.offsetAndRotation(37.0F, 0.0F, -31.0F, 0.0F, -0.3490659F, 0.0F));
        root.addOrReplaceChild("Lclaw5",
                CubeListBuilder.create().texOffs(0, 197)
                        .addBox(-4.0F, -3.0F, -27.0F, 7.0F, 5.0F, 32.0F),
                PartPose.offsetAndRotation(36.0F, 0.0F, -31.0F, 0.0F, 0.3839724F, 0.0F));

        root.addOrReplaceChild("Rclaw1",
                CubeListBuilder.create().texOffs(102, 78)
                        .addBox(-4.0F, 0.0F, -14.0F, 8.0F, 4.0F, 18.0F),
                PartPose.offsetAndRotation(-31.0F, -2.0F, -8.0F, 0.0F, 0.4886922F, 0.0F));
        root.addOrReplaceChild("Rclaw2",
                CubeListBuilder.create().texOffs(103, 106)
                        .addBox(-10.0F, -3.0F, -12.0F, 17.0F, 6.0F, 16.0F),
                PartPose.offsetAndRotation(-37.0F, 0.0F, -20.0F, 0.0F, 0.1745329F, 0.0F));
        root.addOrReplaceChild("Rclaw3",
                CubeListBuilder.create().texOffs(100, 131)
                        .addBox(-17.0F, -5.0F, -25.0F, 17.0F, 10.0F, 30.0F),
                PartPose.offsetAndRotation(-37.0F, 0.0F, -31.0F, 0.0F, 0.4537856F, 0.0F));
        root.addOrReplaceChild("Rclaw4",
                CubeListBuilder.create().texOffs(101, 175)
                        .addBox(-13.0F, -3.0F, -32.0F, 11.0F, 5.0F, 12.0F),
                PartPose.offsetAndRotation(-37.0F, 0.0F, -31.0F, 0.0F, 0.3490659F, 0.0F));
        root.addOrReplaceChild("Rclaw5",
                CubeListBuilder.create().texOffs(100, 197)
                        .addBox(-4.0F, -3.0F, -27.0F, 7.0F, 5.0F, 32.0F),
                PartPose.offsetAndRotation(-36.0F, 0.0F, -31.0F, 0.0F, -0.3839724F, 0.0F));

        root.addOrReplaceChild("Rmouth",
                CubeListBuilder.create().texOffs(0, 28)
                        .addBox(0.0F, 0.0F, -0.5F, 6.0F, 3.0F, 1.0F),
                PartPose.offsetAndRotation(-7.0F, 0.0F, -7.5F, 0.0F, 0.3665191F, 0.0F));
        root.addOrReplaceChild("Lmouth",
                CubeListBuilder.create().texOffs(0, 19)
                        .addBox(-6.0F, 0.0F, -0.5F, 6.0F, 3.0F, 1.0F),
                PartPose.offsetAndRotation(7.0F, 0.0F, -7.5F, 0.0F, -0.3665191F, 0.0F));

        // Right legs (x=36) and left (x=-36), z = 0/10/20/30 — gold multi-pass
        for (int z = 0; z < 4; z++) {
            float zz = LEG_Z[z];
            addLegTriplet(root, "rleg" + z + "_", 36.0F, 3.0F, zz, -1.500983F);
            addLegTriplet(root, "lleg" + z + "_", -36.0F, 3.0F, zz, 1.500983F);
        }

        return LayerDefinition.create(mesh, 256, 512);
    }

    private static void addLegTriplet(PartDefinition root, String prefix, float x, float y, float z, float baseYRot) {
        // gold leg1: box(-2,0,-2, 4,12,4) xRot=-1.343904 yRot=-1.500983 (right base)
        root.addOrReplaceChild(prefix + "1",
                CubeListBuilder.create().texOffs(128, 0)
                        .addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F),
                PartPose.offsetAndRotation(x, y, z, -1.343904F, baseYRot, 0.0F));
        root.addOrReplaceChild(prefix + "2",
                CubeListBuilder.create().texOffs(128, 20)
                        .addBox(-1.0F, 10.0F, -6.0F, 3.0F, 16.0F, 3.0F),
                PartPose.offsetAndRotation(x, y, z, -0.9599311F, baseYRot, 0.0F));
        root.addOrReplaceChild(prefix + "3",
                CubeListBuilder.create().texOffs(128, 43)
                        .addBox(0.0F, 21.0F, -15.0F, 2.0F, 16.0F, 2.0F),
                PartPose.offsetAndRotation(x, y, z, -0.5759587F, baseYRot, 0.0F));
    }

    @Override
    public ModelPart root() {
        return this.root;
    }

    @Override
    public void setupAnim(
            Crab entity,
            float limbSwing,
            float limbSwingAmount,
            float ageInTicks,
            float netHeadYaw,
            float headPitch) {
        float f1 = limbSwingAmount;
        float f2 = ageInTicks;

        // Gold: alternating yRot on right legs at z 0/10/20/30, mirrored left
        for (int z = 0; z < 4; z++) {
            float wave = Mth.cos(f2 * 1.7F) * (float) Math.PI * 0.15F * f1;
            // even z: +wave, odd z: -wave (gold pattern)
            float signed = ((z % 2) == 0) ? wave : -wave;
            float rightY = (float) (-Math.PI / 2) + signed;
            float leftY = -((float) (-Math.PI / 2) + signed);
            for (int seg = 0; seg < 3; seg++) {
                this.legs[0][z][seg].yRot = rightY;
                this.legs[1][z][seg].yRot = leftY;
            }
        }

        if (entity.getAttacking() == 0) {
            this.Leye1.xRot = this.Leye2.xRot = Mth.cos(f2 * 0.35F) * (float) Math.PI * 0.05F;
            this.Leye1.zRot = this.Leye2.zRot = 0.54F + Mth.cos(f2 * 0.25F) * (float) Math.PI * 0.05F;
            this.Reye1.xRot = this.Reye2.xRot = Mth.cos(f2 * 0.3F) * (float) Math.PI * 0.05F;
            this.Reye1.zRot = this.Reye2.zRot = -0.54F + Mth.cos(f2 * 0.45F) * (float) Math.PI * 0.05F;
            this.Lmouth.yRot = -0.72F + Mth.cos(f2 * 0.25F) * (float) Math.PI * 0.05F;
            this.Rmouth.yRot = 0.72F - Mth.cos(f2 * 0.25F) * (float) Math.PI * 0.05F;
            float newangle = Mth.cos(f2 * 0.15F) * (float) Math.PI * 0.03F;
            this.Lclaw3.yRot = -0.453F + newangle;
            this.Lclaw4.yRot = -0.349F + newangle;
            this.Lclaw5.yRot = 0.384F - newangle;
            newangle = Mth.cos(f2 * 0.13F) * (float) Math.PI * 0.02F;
            this.Rclaw3.yRot = 0.453F + newangle;
            this.Rclaw4.yRot = 0.349F + newangle;
            this.Rclaw5.yRot = -0.384F - newangle;
        } else {
            this.Leye1.xRot = this.Leye2.xRot = Mth.cos(f2 * 0.45F) * (float) Math.PI * 0.1F;
            this.Leye1.zRot = this.Leye2.zRot = 0.54F + Mth.cos(f2 * 0.35F) * (float) Math.PI * 0.1F;
            this.Reye1.xRot = this.Reye2.xRot = Mth.cos(f2 * 0.4F) * (float) Math.PI * 0.1F;
            this.Reye1.zRot = this.Reye2.zRot = -0.54F + Mth.cos(f2 * 0.55F) * (float) Math.PI * 0.1F;
            this.Lmouth.yRot = -0.72F + Mth.cos(f2 * 0.45F) * (float) Math.PI * 0.15F;
            this.Rmouth.yRot = 0.72F - Mth.cos(f2 * 0.45F) * (float) Math.PI * 0.15F;
            float newangle = Mth.cos(f2 * 0.35F) * (float) Math.PI * 0.13F;
            this.Lclaw3.yRot = -0.453F + newangle;
            this.Lclaw4.yRot = -0.349F + newangle;
            this.Lclaw5.yRot = 0.384F - newangle;
            newangle = Mth.cos(f2 * 0.43F) * (float) Math.PI * 0.12F;
            this.Rclaw3.yRot = 0.453F + newangle;
            this.Rclaw4.yRot = 0.349F + newangle;
            this.Rclaw5.yRot = -0.384F - newangle;
        }
    }
}
