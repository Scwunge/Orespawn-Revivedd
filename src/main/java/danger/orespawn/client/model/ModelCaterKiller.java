package danger.orespawn.client.model;

import danger.orespawn.entity.CaterKiller;
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
 * Port of gold {@code ModelCaterKiller} (1.7.10 ModelBase, tex 256×512) → 1.21 HierarchicalModel.
 * Cubes/UVs/offsets/base rotations 1:1. Gold reuses seg1×3 and seg2×6 during render; here each
 * instance is a separate part so setupAnim matches without multi-pass render.
 * Wingspeed default 0.22 matches ClientProxy {@code new ModelCaterKiller(0.22F)}.
 */
@OnlyIn(Dist.CLIENT)
public class ModelCaterKiller extends HierarchicalModel<CaterKiller> {
    /** Model layer location. */
    public static final ModelLayerLocation LAYER_LOCATION =
            new ModelLayerLocation(
                    ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "caterkiller"), "main");

    private final float wingspeed;
    private final ModelPart root;

    private final ModelPart Head;
    private final ModelPart falsehead;
    private final ModelPart ltusk1;
    private final ModelPart ltusk2;
    private final ModelPart rtusk1;
    private final ModelPart rtusk2;
    private final ModelPart ljaw;
    private final ModelPart rjaw;

    /** Gold reuses seg1 family 3× (i=0.2). */
    private final ModelPart[] seg1 = new ModelPart[3];
    private final ModelPart[] seg1lspike = new ModelPart[3];
    private final ModelPart[] seg1rspike = new ModelPart[3];
    private final ModelPart[] seg1ltopspike = new ModelPart[3];
    private final ModelPart[] seg1rtopspike = new ModelPart[3];
    private final ModelPart[] seg1lleg = new ModelPart[3];
    private final ModelPart[] seg1rleg = new ModelPart[3];

    /** Gold reuses seg2 family 6× (var27=0.5). */
    private final ModelPart[] seg2 = new ModelPart[6];
    private final ModelPart[] seg2lfoot = new ModelPart[6];
    private final ModelPart[] seg2rfoot = new ModelPart[6];
    private final ModelPart[] seg2ltopspike = new ModelPart[6];
    private final ModelPart[] seg2rtopspike = new ModelPart[6];
    private final ModelPart[] seg2lspike = new ModelPart[6];
    private final ModelPart[] seg2rspike = new ModelPart[6];

    private final ModelPart seg3;
    private final ModelPart seg3lfoot;
    private final ModelPart seg3rfoot;
    private final ModelPart seg3lspike;
    private final ModelPart seg3rspike;
    private final ModelPart seg3ltopspike;
    private final ModelPart seg3rtopspike;
    private final ModelPart seg3lbackspike;
    private final ModelPart seg3rbackspike;

    public ModelCaterKiller(ModelPart root) {
        this(root, 0.22F);
    }

    public ModelCaterKiller(ModelPart root, float wingspeed) {
        this.root = root;
        this.wingspeed = wingspeed;

        this.Head = root.getChild("Head");
        this.falsehead = root.getChild("falsehead");
        this.ltusk1 = root.getChild("ltusk1");
        this.ltusk2 = root.getChild("ltusk2");
        this.rtusk1 = root.getChild("rtusk1");
        this.rtusk2 = root.getChild("rtusk2");
        this.ljaw = root.getChild("ljaw");
        this.rjaw = root.getChild("rjaw");

        for (int i = 0; i < 3; i++) {
            this.seg1[i] = root.getChild("seg1_" + i);
            this.seg1lspike[i] = root.getChild("seg1lspike_" + i);
            this.seg1rspike[i] = root.getChild("seg1rspike_" + i);
            this.seg1ltopspike[i] = root.getChild("seg1ltopspike_" + i);
            this.seg1rtopspike[i] = root.getChild("seg1rtopspike_" + i);
            this.seg1lleg[i] = root.getChild("seg1lleg_" + i);
            this.seg1rleg[i] = root.getChild("seg1rleg_" + i);
        }

        for (int i = 0; i < 6; i++) {
            this.seg2[i] = root.getChild("seg2_" + i);
            this.seg2lfoot[i] = root.getChild("seg2lfoot_" + i);
            this.seg2rfoot[i] = root.getChild("seg2rfoot_" + i);
            this.seg2ltopspike[i] = root.getChild("seg2ltopspike_" + i);
            this.seg2rtopspike[i] = root.getChild("seg2rtopspike_" + i);
            this.seg2lspike[i] = root.getChild("seg2lspike_" + i);
            this.seg2rspike[i] = root.getChild("seg2rspike_" + i);
        }

        this.seg3 = root.getChild("seg3");
        this.seg3lfoot = root.getChild("seg3lfoot");
        this.seg3rfoot = root.getChild("seg3rfoot");
        this.seg3lspike = root.getChild("seg3lspike");
        this.seg3rspike = root.getChild("seg3rspike");
        this.seg3ltopspike = root.getChild("seg3ltopspike");
        this.seg3rtopspike = root.getChild("seg3rtopspike");
        this.seg3lbackspike = root.getChild("seg3lbackspike");
        this.seg3rbackspike = root.getChild("seg3rbackspike");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        // Gold tex 256×512 (field_78090_t/u); part setTextureSize(64,32) is after addBox → ignored
        root.addOrReplaceChild(
                "Head",
                CubeListBuilder.create().texOffs(0, 50).addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 8.0F),
                PartPose.offset(0.0F, -8.0F, -12.0F));
        root.addOrReplaceChild(
                "falsehead",
                CubeListBuilder.create().texOffs(0, 100).addBox(-10.0F, -27.0F, -11.0F, 20.0F, 20.0F, 10.0F),
                PartPose.offsetAndRotation(0.0F, -8.0F, -12.0F, -0.1570796F, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "ltusk1",
                CubeListBuilder.create().texOffs(0, 140).addBox(-1.0F, -1.0F, -1.0F, 33.0F, 3.0F, 3.0F),
                PartPose.offsetAndRotation(9.0F, -25.0F, -19.0F, 0.0F, 0.5585054F, 0.0F));
        root.addOrReplaceChild(
                "ltusk2",
                CubeListBuilder.create().texOffs(0, 160).addBox(0.0F, 0.0F, 0.0F, 20.0F, 1.0F, 1.0F),
                PartPose.offsetAndRotation(36.0F, -25.0F, -36.0F, 0.0F, 0.8028515F, 0.0F));
        root.addOrReplaceChild(
                "rtusk1",
                CubeListBuilder.create().texOffs(0, 150).addBox(-33.0F, 0.0F, 0.0F, 33.0F, 3.0F, 3.0F),
                PartPose.offsetAndRotation(-8.0F, -25.0F, -17.0F, 0.0F, -0.5585054F, 0.0F));
        root.addOrReplaceChild(
                "rtusk2",
                CubeListBuilder.create().texOffs(0, 170).addBox(-20.0F, 0.0F, 0.0F, 20.0F, 1.0F, 1.0F),
                PartPose.offsetAndRotation(-36.0F, -24.0F, -34.0F, 0.0F, -0.8028515F, 0.0F));
        root.addOrReplaceChild(
                "ljaw",
                CubeListBuilder.create().texOffs(100, 50).addBox(0.0F, 0.0F, 0.0F, 1.0F, 7.0F, 4.0F),
                PartPose.offsetAndRotation(4.0F, -1.0F, -18.0F, 0.0F, 0.0F, 0.1396263F));
        root.addOrReplaceChild(
                "rjaw",
                CubeListBuilder.create().texOffs(125, 50).addBox(0.0F, 0.0F, 0.0F, 1.0F, 7.0F, 4.0F),
                PartPose.offsetAndRotation(-5.0F, -1.0F, -18.0F, 0.0F, 0.0F, -0.1396263F));

        // seg1 base offsets (i=0); setupAnim repositions each instance
        for (int i = 0; i < 3; i++) {
            root.addOrReplaceChild(
                    "seg1_" + i,
                    CubeListBuilder.create().texOffs(0, 200).addBox(-14.0F, -31.0F, 0.0F, 28.0F, 32.0F, 14.0F),
                    PartPose.offset(0.0F, -8.0F, -12.0F));
            root.addOrReplaceChild(
                    "seg1lspike_" + i,
                    CubeListBuilder.create().texOffs(0, 260).addBox(-1.0F, -1.0F, -1.0F, 33.0F, 2.0F, 2.0F),
                    PartPose.offsetAndRotation(14.0F, -32.0F, -6.0F, 0.0F, 0.3316126F, -0.122173F));
            root.addOrReplaceChild(
                    "seg1rspike_" + i,
                    CubeListBuilder.create().texOffs(0, 270).addBox(-33.0F, -1.0F, -1.0F, 33.0F, 2.0F, 2.0F),
                    PartPose.offsetAndRotation(-13.0F, -32.0F, -6.0F, 0.0F, -0.3316126F, 0.122173F));
            root.addOrReplaceChild(
                    "seg1ltopspike_" + i,
                    CubeListBuilder.create().texOffs(125, 260).addBox(-2.0F, -8.0F, -2.0F, 4.0F, 9.0F, 4.0F),
                    PartPose.offsetAndRotation(8.0F, -39.0F, -6.0F, 0.0F, 0.0F, 0.1396263F));
            root.addOrReplaceChild(
                    "seg1rtopspike_" + i,
                    CubeListBuilder.create().texOffs(150, 260).addBox(-2.0F, -8.0F, -2.0F, 4.0F, 9.0F, 4.0F),
                    PartPose.offsetAndRotation(-10.0F, -39.0F, -6.0F, 0.0F, 0.0F, -0.1396263F));
            root.addOrReplaceChild(
                    "seg1lleg_" + i,
                    CubeListBuilder.create().texOffs(125, 200).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 16.0F, 2.0F),
                    PartPose.offsetAndRotation(8.0F, -8.0F, -5.0F, 0.0F, 0.0F, 0.1570796F));
            root.addOrReplaceChild(
                    "seg1rleg_" + i,
                    CubeListBuilder.create().texOffs(150, 200).addBox(0.0F, 0.0F, 0.0F, 2.0F, 16.0F, 2.0F),
                    PartPose.offsetAndRotation(-9.0F, -8.0F, -5.0F, 0.0F, 0.0F, -0.1570796F));
        }

        // seg2 base offsets (var27=0 at z=32 gold); setupAnim drives z along body
        for (int i = 0; i < 6; i++) {
            root.addOrReplaceChild(
                    "seg2_" + i,
                    CubeListBuilder.create().texOffs(0, 300).addBox(-20.0F, -17.0F, -9.0F, 40.0F, 34.0F, 18.0F),
                    PartPose.offset(0.0F, -2.0F, 32.0F));
            root.addOrReplaceChild(
                    "seg2lfoot_" + i,
                    CubeListBuilder.create().texOffs(125, 300).addBox(-5.0F, 0.0F, -5.0F, 10.0F, 10.0F, 10.0F),
                    PartPose.offset(13.0F, 14.0F, 32.0F));
            root.addOrReplaceChild(
                    "seg2rfoot_" + i,
                    CubeListBuilder.create().texOffs(175, 300).addBox(-5.0F, 0.0F, -5.0F, 10.0F, 10.0F, 10.0F),
                    PartPose.offset(-13.0F, 14.0F, 32.0F));
            root.addOrReplaceChild(
                    "seg2ltopspike_" + i,
                    CubeListBuilder.create().texOffs(100, 360).addBox(-2.0F, -9.0F, -2.0F, 4.0F, 9.0F, 4.0F),
                    PartPose.offsetAndRotation(14.0F, -18.0F, 32.0F, 0.0F, 0.0F, 0.1396263F));
            root.addOrReplaceChild(
                    "seg2rtopspike_" + i,
                    CubeListBuilder.create().texOffs(125, 360).addBox(-2.0F, -9.0F, -2.0F, 4.0F, 9.0F, 4.0F),
                    PartPose.offsetAndRotation(-14.0F, -18.0F, 32.0F, 0.0F, 0.0F, -0.1396263F));
            root.addOrReplaceChild(
                    "seg2lspike_" + i,
                    CubeListBuilder.create().texOffs(0, 360).addBox(0.0F, -1.0F, -1.0F, 20.0F, 2.0F, 2.0F),
                    PartPose.offsetAndRotation(18.0F, -9.0F, 32.0F, 0.0F, 0.0F, -0.0698132F));
            root.addOrReplaceChild(
                    "seg2rspike_" + i,
                    CubeListBuilder.create().texOffs(0, 370).addBox(-20.0F, -1.0F, -1.0F, 20.0F, 2.0F, 2.0F),
                    PartPose.offsetAndRotation(-18.0F, -9.0F, 32.0F, 0.0F, 0.0F, 0.0698132F));
        }

        root.addOrReplaceChild(
                "seg3",
                CubeListBuilder.create().texOffs(0, 400).addBox(-15.0F, -14.0F, -7.0F, 30.0F, 28.0F, 14.0F),
                PartPose.offset(0.0F, 3.0F, 48.0F));
        root.addOrReplaceChild(
                "seg3lfoot",
                CubeListBuilder.create().texOffs(100, 400).addBox(-4.0F, 0.0F, -6.0F, 8.0F, 8.0F, 12.0F),
                PartPose.offset(10.0F, 16.0F, 48.0F));
        root.addOrReplaceChild(
                "seg3rfoot",
                CubeListBuilder.create().texOffs(150, 400).addBox(-4.0F, 0.0F, -6.0F, 8.0F, 8.0F, 12.0F),
                PartPose.offset(-10.0F, 16.0F, 48.0F));
        root.addOrReplaceChild(
                "seg3lspike",
                CubeListBuilder.create().texOffs(0, 450).addBox(0.0F, -1.0F, -1.0F, 14.0F, 2.0F, 2.0F),
                PartPose.offsetAndRotation(14.0F, -4.0F, 48.0F, 0.0F, 0.0F, -0.0698132F));
        root.addOrReplaceChild(
                "seg3rspike",
                CubeListBuilder.create().texOffs(0, 460).addBox(-14.0F, -1.0F, -1.0F, 14.0F, 2.0F, 2.0F),
                PartPose.offsetAndRotation(-14.0F, -4.0F, 48.0F, 0.0F, 0.0F, 0.0698132F));
        root.addOrReplaceChild(
                "seg3ltopspike",
                CubeListBuilder.create().texOffs(100, 450).addBox(-2.0F, -13.0F, -2.0F, 3.0F, 13.0F, 3.0F),
                PartPose.offsetAndRotation(10.0F, -10.0F, 48.0F, 0.0F, 0.0F, 0.1396263F));
        root.addOrReplaceChild(
                "seg3rtopspike",
                CubeListBuilder.create().texOffs(120, 450).addBox(-2.0F, -13.0F, -2.0F, 3.0F, 13.0F, 3.0F),
                PartPose.offsetAndRotation(-10.0F, -10.0F, 48.0F, 0.0F, 0.0F, -0.1396263F));
        root.addOrReplaceChild(
                "seg3lbackspike",
                CubeListBuilder.create().texOffs(50, 450).addBox(-2.0F, -20.0F, -2.0F, 4.0F, 20.0F, 4.0F),
                PartPose.offsetAndRotation(13.0F, -8.0F, 54.0F, -0.9773844F, 0.2792527F, 0.1396263F));
        root.addOrReplaceChild(
                "seg3rbackspike",
                CubeListBuilder.create().texOffs(75, 450).addBox(-2.0F, -20.0F, -2.0F, 4.0F, 20.0F, 4.0F),
                PartPose.offsetAndRotation(-13.0F, -8.0F, 54.0F, -0.9773844F, -0.3490659F, 0.1396263F));

        return LayerDefinition.create(mesh, 256, 512);
    }

    @Override
    public ModelPart root() {
        return this.root;
    }

    /**
     * Full gold {@code func_78088_a} animation (jaws, head bob, tusks, seg1×3, seg2×6 undulation, seg3).
     * f1 = limbSwingAmount, f2 = ageInTicks.
     */
    @Override
    public void setupAnim(
            CaterKiller entity,
            float limbSwing,
            float limbSwingAmount,
            float ageInTicks,
            float netHeadYaw,
            float headPitch) {
        float f1 = limbSwingAmount;
        float f2 = ageInTicks;
        float newangle;
        float headoff;
        float zpi = 0.0F;
        float zdist;

        if (entity.getAttacking() != 0) {
            newangle = Mth.cos(f2 * 1.7F * this.wingspeed) * (float) Math.PI * 0.07F;
        } else {
            newangle = Mth.cos(f2 * 1.3F * this.wingspeed) * (float) Math.PI * 0.025F;
        }

        this.ljaw.zRot = 0.139F + newangle;
        this.rjaw.zRot = -0.139F - newangle;

        if (entity.getAttacking() != 0) {
            headoff = Mth.cos(f2 * 1.7F * this.wingspeed) * 8.0F;
        } else {
            headoff = Mth.cos(f2 * 0.3F * this.wingspeed) * 2.0F;
        }

        this.Head.y = -8.0F + headoff;
        this.falsehead.y = -8.0F + headoff;
        this.ltusk1.y = -25.0F + headoff;
        this.ltusk2.y = -25.0F + headoff;
        this.rtusk1.y = -25.0F + headoff;
        this.rtusk2.y = -25.0F + headoff;
        this.ljaw.y = -1.0F + headoff;
        this.rjaw.y = -1.0F + headoff;

        newangle = Mth.cos(f2 * 2.11F * this.wingspeed) * (float) Math.PI * 0.08F;
        this.ltusk2.yRot = 0.802F + newangle;
        newangle = Mth.cos(f2 * 2.3F * this.wingspeed) * (float) Math.PI * 0.08F;
        this.rtusk2.yRot = -0.802F + newangle;

        for (int i = 0; i < 3; i++) {
            this.seg1[i].y = -8.0F + headoff / (i + 1) + 8 * i;
            this.seg1lspike[i].y = -32.0F + headoff / (i + 1) + 8 * i;
            this.seg1rspike[i].y = -32.0F + headoff / (i + 1) + 8 * i;
            this.seg1ltopspike[i].y = -39.0F + headoff / (i + 1) + 8 * i;
            this.seg1rtopspike[i].y = -39.0F + headoff / (i + 1) + 8 * i;
            this.seg1lleg[i].y = -8.0F + headoff / (i + 1) + 8 * i;
            this.seg1rleg[i].y = -8.0F + headoff / (i + 1) + 8 * i;

            this.seg1[i].z = -12 + 14 * i;
            this.seg1lspike[i].z = -6 + 14 * i;
            this.seg1rspike[i].z = -6 + 14 * i;
            this.seg1ltopspike[i].z = -6 + 14 * i;
            this.seg1rtopspike[i].z = -6 + 14 * i;
            this.seg1lleg[i].z = -5 + 14 * i;
            this.seg1rleg[i].z = -5 + 14 * i;

            newangle = Mth.cos((float) (f2 * 0.91F * this.wingspeed + (Math.PI / 8) * i))
                    * (float) Math.PI
                    * 0.08F;
            this.seg1lspike[i].zRot = newangle;
            this.seg1rspike[i].zRot = -newangle;

            if (entity.getAttacking() != 0) {
                newangle = Mth.cos((float) (f2 * 2.91F * this.wingspeed + (Math.PI / 8) * i))
                        * (float) Math.PI
                        * 0.15F;
            } else {
                newangle = Mth.cos((float) (f2 * 0.35F * this.wingspeed + (Math.PI / 8) * i))
                        * (float) Math.PI
                        * 0.04F;
            }
            this.seg1lleg[i].xRot = newangle;
            this.seg1rleg[i].xRot = -newangle;
        }

        for (int var27 = 0; var27 < 6; var27++) {
            zdist = Mth.cos(f2 * 1.7F * this.wingspeed + zpi) * 1.5F * f1;
            float z = 39.0F + (16.0F + zdist) * var27;
            this.seg2[var27].z = z;
            this.seg2lfoot[var27].z = z;
            this.seg2rfoot[var27].z = z;
            this.seg2ltopspike[var27].z = z;
            this.seg2rtopspike[var27].z = z;
            this.seg2lspike[var27].z = z;
            this.seg2rspike[var27].z = z;

            newangle = Mth.cos((float) (f2 * 0.4F * this.wingspeed - (Math.PI / 8) * var27))
                    * (float) Math.PI
                    * 0.07F;
            this.seg2lspike[var27].zRot = newangle;
            this.seg2rspike[var27].zRot = -newangle;

            zpi += (float) (Math.PI / 4);
        }

        this.seg3.z = this.seg2rspike[5].z + 16.0F;
        this.seg3lfoot.z = this.seg3.z;
        this.seg3rfoot.z = this.seg3.z;
        this.seg3lspike.z = this.seg3.z;
        this.seg3rspike.z = this.seg3.z;
        this.seg3ltopspike.z = this.seg3.z;
        this.seg3rtopspike.z = this.seg3.z;
        this.seg3lbackspike.z = this.seg3.z + 6.0F;
        this.seg3rbackspike.z = this.seg3.z + 6.0F;

        int var28 = 6;
        newangle = Mth.cos((float) (f2 * 0.4F * this.wingspeed - (Math.PI / 8) * var28))
                * (float) Math.PI
                * 0.07F;
        this.seg3lspike.zRot = newangle;
        this.seg3rspike.zRot = -newangle;

        newangle = Mth.cos(f2 * 0.81F * this.wingspeed) * (float) Math.PI * 0.04F;
        this.seg3lbackspike.xRot = -0.977F + newangle;
        newangle = Mth.cos(f2 * 0.87F * this.wingspeed) * (float) Math.PI * 0.04F;
        this.seg3rbackspike.xRot = -0.977F + newangle;
        newangle = Mth.cos(f2 * 1.11F * this.wingspeed) * (float) Math.PI * 0.04F;
        this.seg3lbackspike.yRot = 0.28F + newangle;
        newangle = Mth.cos(f2 * 1.3F * this.wingspeed) * (float) Math.PI * 0.04F;
        this.seg3rbackspike.yRot = -0.28F + newangle;
    }
}
