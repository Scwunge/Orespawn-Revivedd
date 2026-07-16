package danger.orespawn.client.model;

import danger.orespawn.entity.HerculesBeetle;
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
 * Port of gold {@code ModelHerculesBeetle} (1.7.10 ModelBase, tex 256×256) → 1.21 HierarchicalModel.
 * Cube sizes/UVs/offsets/base rotations 1:1. Wingspeed default 1.0 matches ClientProxy.
 * Full leg walk + jaw attack anim from gold {@code func_78088_a}.
 */
@OnlyIn(Dist.CLIENT)
public class ModelHerculesBeetle extends HierarchicalModel<HerculesBeetle> {
    /** Model layer location. */
    public static final ModelLayerLocation LAYER_LOCATION =
            new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "hercules_beetle"), "main");

    private final float wingspeed;
    private final ModelPart root;
    private final ModelPart body1;
    private final ModelPart body2;
    private final ModelPart head1;
    private final ModelPart head2;
    private final ModelPart head3;
    private final ModelPart head4;
    private final ModelPart head5;
    private final ModelPart head6;
    private final ModelPart head8;
    private final ModelPart jaw1;
    private final ModelPart jaw2;
    private final ModelPart jaw3;
    private final ModelPart jaw4;
    private final ModelPart head7;
    private final ModelPart lfleg1;
    private final ModelPart lfleg2;
    private final ModelPart lfleg3;
    private final ModelPart lmleg1;
    private final ModelPart lmleg2;
    private final ModelPart lmleg3;
    private final ModelPart lrleg1;
    private final ModelPart lrleg2;
    private final ModelPart lrleg3;
    private final ModelPart jaw5;
    private final ModelPart jaw6;
    private final ModelPart jaw7;
    private final ModelPart jaw8;
    private final ModelPart rfleg1;
    private final ModelPart rfleg2;
    private final ModelPart rfleg3;
    private final ModelPart rmleg1;
    private final ModelPart rmleg2;
    private final ModelPart rmleg3;
    private final ModelPart rrleg1;
    private final ModelPart rrleg2;
    private final ModelPart rrleg3;
    private final ModelPart jaw9;

    public ModelHerculesBeetle(ModelPart root) {
        this(root, 1.0F);
    }

    public ModelHerculesBeetle(ModelPart root, float wingspeed) {
        this.root = root;
        this.wingspeed = wingspeed;
        this.body1 = root.getChild("body1");
        this.body2 = root.getChild("body2");
        this.head1 = root.getChild("head1");
        this.head2 = root.getChild("head2");
        this.head3 = root.getChild("head3");
        this.head4 = root.getChild("head4");
        this.head5 = root.getChild("head5");
        this.head6 = root.getChild("head6");
        this.head8 = root.getChild("head8");
        this.jaw1 = root.getChild("jaw1");
        this.jaw2 = root.getChild("jaw2");
        this.jaw3 = root.getChild("jaw3");
        this.jaw4 = root.getChild("jaw4");
        this.head7 = root.getChild("head7");
        this.lfleg1 = root.getChild("lfleg1");
        this.lfleg2 = root.getChild("lfleg2");
        this.lfleg3 = root.getChild("lfleg3");
        this.lmleg1 = root.getChild("lmleg1");
        this.lmleg2 = root.getChild("lmleg2");
        this.lmleg3 = root.getChild("lmleg3");
        this.lrleg1 = root.getChild("lrleg1");
        this.lrleg2 = root.getChild("lrleg2");
        this.lrleg3 = root.getChild("lrleg3");
        this.jaw5 = root.getChild("jaw5");
        this.jaw6 = root.getChild("jaw6");
        this.jaw7 = root.getChild("jaw7");
        this.jaw8 = root.getChild("jaw8");
        this.rfleg1 = root.getChild("rfleg1");
        this.rfleg2 = root.getChild("rfleg2");
        this.rfleg3 = root.getChild("rfleg3");
        this.rmleg1 = root.getChild("rmleg1");
        this.rmleg2 = root.getChild("rmleg2");
        this.rmleg3 = root.getChild("rmleg3");
        this.rrleg1 = root.getChild("rrleg1");
        this.rrleg2 = root.getChild("rrleg2");
        this.rrleg3 = root.getChild("rrleg3");
        this.jaw9 = root.getChild("jaw9");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        root.addOrReplaceChild("body1",
                CubeListBuilder.create().texOffs(0, 30)
                        .addBox(-8.0F, 0.0F, 0.0F, 16.0F, 16.0F, 23.0F),
                PartPose.offset(0.0F, 0.0F, 0.0F));
        root.addOrReplaceChild("body2",
                CubeListBuilder.create().texOffs(80, 41)
                        .addBox(-6.0F, 0.0F, 0.0F, 12.0F, 12.0F, 4.0F),
                PartPose.offset(0.0F, 3.0F, 23.0F));
        root.addOrReplaceChild("head1",
                CubeListBuilder.create().texOffs(0, 71)
                        .addBox(-9.0F, 0.0F, 0.0F, 18.0F, 16.0F, 12.0F),
                PartPose.offsetAndRotation(0.0F, -1.0F, -10.0F, -0.122173F, 0.0F, 0.0F));
        root.addOrReplaceChild("head2",
                CubeListBuilder.create().texOffs(0, 100)
                        .addBox(-7.0F, 0.0F, 0.0F, 14.0F, 10.0F, 6.0F),
                PartPose.offsetAndRotation(0.0F, -2.0F, -16.0F, -0.122173F, 0.0F, 0.0F));
        root.addOrReplaceChild("head3",
                CubeListBuilder.create().texOffs(0, 117)
                        .addBox(-5.0F, 0.0F, 0.0F, 10.0F, 6.0F, 9.0F),
                PartPose.offsetAndRotation(0.0F, -3.0F, -25.0F, -0.122173F, 0.0F, 0.0F));
        root.addOrReplaceChild("head4",
                CubeListBuilder.create().texOffs(0, 133)
                        .addBox(-4.0F, 0.0F, 0.0F, 8.0F, 4.0F, 12.0F),
                PartPose.offsetAndRotation(0.0F, -4.0F, -37.0F, -0.122173F, 0.0F, 0.0F));
        root.addOrReplaceChild("head5",
                CubeListBuilder.create().texOffs(0, 150)
                        .addBox(-3.0F, 0.0F, 0.0F, 6.0F, 3.0F, 21.0F),
                PartPose.offset(0.0F, -4.0F, -58.0F));
        root.addOrReplaceChild("head6",
                CubeListBuilder.create().texOffs(0, 175)
                        .addBox(-2.0F, 0.0F, 0.0F, 4.0F, 2.0F, 14.0F),
                PartPose.offsetAndRotation(0.0F, -2.0F, -72.0F, 0.122173F, 0.0F, 0.0F));
        root.addOrReplaceChild("head8",
                CubeListBuilder.create().texOffs(6, 193)
                        .addBox(0.0F, 0.0F, 0.0F, 1.0F, 3.0F, 1.0F),
                PartPose.offsetAndRotation(0.0F, -2.0F, -46.0F, -0.2094395F, 0.0F, 0.0F));
        root.addOrReplaceChild("jaw1",
                CubeListBuilder.create().texOffs(114, 0)
                        .addBox(-3.0F, -3.0F, -4.0F, 6.0F, 7.0F, 5.0F),
                PartPose.offsetAndRotation(0.0F, 12.0F, -12.0F, 0.122173F, 0.0F, 0.0F));
        root.addOrReplaceChild("jaw2",
                CubeListBuilder.create().texOffs(115, 14)
                        .addBox(-2.5F, -3.0F, -27.0F, 5.0F, 5.0F, 23.0F),
                PartPose.offsetAndRotation(0.0F, 12.0F, -12.0F, 0.122173F, 0.0F, 0.0F));
        root.addOrReplaceChild("jaw3",
                CubeListBuilder.create().texOffs(115, 43)
                        .addBox(-1.5F, 0.0F, -44.0F, 3.0F, 5.0F, 18.0F),
                PartPose.offset(0.0F, 12.0F, -12.0F));
        root.addOrReplaceChild("jaw4",
                CubeListBuilder.create().texOffs(115, 70)
                        .addBox(-0.5F, -2.0F, -45.0F, 1.0F, 5.0F, 1.0F),
                PartPose.offset(0.0F, 12.0F, -12.0F));
        root.addOrReplaceChild("head7",
                CubeListBuilder.create().texOffs(0, 193)
                        .addBox(-0.5F, 0.0F, 0.0F, 1.0F, 4.0F, 1.0F),
                PartPose.offsetAndRotation(0.0F, -2.0F, -73.0F, 0.122173F, 0.0F, 0.0F));

        // left front / mid / rear legs
        root.addOrReplaceChild("lfleg1",
                CubeListBuilder.create().texOffs(60, 0)
                        .addBox(0.0F, 0.0F, -0.5F, 10.0F, 3.0F, 3.0F),
                PartPose.offsetAndRotation(6.0F, 15.0F, -5.0F, 0.0F, 0.3490659F, 0.0872665F));
        root.addOrReplaceChild("lfleg2",
                CubeListBuilder.create().texOffs(60, 8)
                        .addBox(10.0F, -1.0F, 0.0F, 11.0F, 2.0F, 2.0F),
                PartPose.offsetAndRotation(6.0F, 15.0F, -5.0F, 0.0F, 0.3490659F, (float) (Math.PI / 12)));
        root.addOrReplaceChild("lfleg3",
                CubeListBuilder.create().texOffs(60, 14)
                        .addBox(21.0F, -2.0F, 0.5F, 10.0F, 1.0F, 1.0F),
                PartPose.offsetAndRotation(6.0F, 15.0F, -5.0F, 0.0F, 0.3490659F, 0.3490659F));
        root.addOrReplaceChild("lmleg1",
                CubeListBuilder.create().texOffs(60, 0)
                        .addBox(0.0F, 0.0F, -0.5F, 10.0F, 3.0F, 3.0F),
                PartPose.offsetAndRotation(6.0F, 15.0F, 0.0F, 0.0F, 0.0F, 0.0872665F));
        root.addOrReplaceChild("lmleg2",
                CubeListBuilder.create().texOffs(60, 8)
                        .addBox(10.0F, -1.0F, 0.0F, 11.0F, 2.0F, 2.0F),
                PartPose.offsetAndRotation(6.0F, 15.0F, 0.0F, 0.0F, 0.0F, (float) (Math.PI / 12)));
        root.addOrReplaceChild("lmleg3",
                CubeListBuilder.create().texOffs(60, 14)
                        .addBox(21.0F, -2.0F, 0.5F, 10.0F, 1.0F, 1.0F),
                PartPose.offsetAndRotation(6.0F, 15.0F, 0.0F, 0.0F, 0.0F, 0.3490659F));
        root.addOrReplaceChild("lrleg1",
                CubeListBuilder.create().texOffs(60, 0)
                        .addBox(0.0F, 0.0F, -0.5F, 10.0F, 3.0F, 3.0F),
                PartPose.offsetAndRotation(6.0F, 15.0F, 5.0F, 0.0F, -0.3490659F, 0.0872665F));
        root.addOrReplaceChild("lrleg2",
                CubeListBuilder.create().texOffs(60, 8)
                        .addBox(10.0F, -1.0F, 0.0F, 11.0F, 2.0F, 2.0F),
                PartPose.offsetAndRotation(6.0F, 15.0F, 5.0F, 0.0F, -0.3490659F, (float) (Math.PI / 12)));
        root.addOrReplaceChild("lrleg3",
                CubeListBuilder.create().texOffs(60, 14)
                        .addBox(21.0F, -2.0F, 0.5F, 10.0F, 1.0F, 1.0F),
                PartPose.offsetAndRotation(6.0F, 15.0F, 5.0F, 0.0F, -0.3490659F, 0.3490659F));

        root.addOrReplaceChild("jaw5",
                CubeListBuilder.create().texOffs(115, 78)
                        .addBox(2.0F, -2.0F, -9.0F, 2.0F, 3.0F, 3.0F),
                PartPose.offsetAndRotation(0.0F, 12.0F, -12.0F, 0.122173F, 0.0F, 0.0F));
        root.addOrReplaceChild("jaw6",
                CubeListBuilder.create().texOffs(127, 78)
                        .addBox(-4.0F, -2.0F, -9.0F, 2.0F, 3.0F, 3.0F),
                PartPose.offsetAndRotation(0.0F, 12.0F, -12.0F, 0.122173F, 0.0F, 0.0F));
        root.addOrReplaceChild("jaw7",
                CubeListBuilder.create().texOffs(115, 86)
                        .addBox(5.0F, 1.0F, -6.0F, 9.0F, 1.0F, 1.0F),
                PartPose.offsetAndRotation(0.0F, 12.0F, -12.0F, 0.0F, 0.5585054F, 0.2268928F));
        root.addOrReplaceChild("jaw8",
                CubeListBuilder.create().texOffs(115, 89)
                        .addBox(-14.0F, 1.0F, -6.0F, 9.0F, 1.0F, 1.0F),
                PartPose.offsetAndRotation(0.0F, 12.0F, -12.0F, 0.0F, -0.5585054F, -0.2268928F));

        // right front / mid / rear legs
        root.addOrReplaceChild("rfleg1",
                CubeListBuilder.create().texOffs(30, 0)
                        .addBox(-10.0F, 0.0F, -0.5F, 10.0F, 3.0F, 3.0F),
                PartPose.offsetAndRotation(-6.0F, 15.0F, -5.0F, 0.0F, -0.3490659F, -0.0872665F));
        root.addOrReplaceChild("rfleg2",
                CubeListBuilder.create().texOffs(30, 8)
                        .addBox(-21.0F, -1.0F, 0.0F, 11.0F, 2.0F, 2.0F),
                PartPose.offsetAndRotation(-6.0F, 15.0F, -5.0F, 0.0F, -0.3490659F, (float) (-Math.PI / 12)));
        root.addOrReplaceChild("rfleg3",
                CubeListBuilder.create().texOffs(30, 14)
                        .addBox(-31.0F, -2.0F, 0.5F, 10.0F, 1.0F, 1.0F),
                PartPose.offsetAndRotation(-6.0F, 15.0F, -5.0F, 0.0F, -0.3490659F, -0.3490659F));
        root.addOrReplaceChild("rmleg1",
                CubeListBuilder.create().texOffs(30, 0)
                        .addBox(-10.0F, 0.0F, -0.5F, 10.0F, 3.0F, 3.0F),
                PartPose.offsetAndRotation(-6.0F, 15.0F, 0.0F, 0.0F, 0.0F, -0.0872665F));
        root.addOrReplaceChild("rmleg2",
                CubeListBuilder.create().texOffs(30, 8)
                        .addBox(-21.0F, -1.0F, 0.0F, 11.0F, 2.0F, 2.0F),
                PartPose.offsetAndRotation(-6.0F, 15.0F, 0.0F, 0.0F, 0.0F, (float) (-Math.PI / 12)));
        root.addOrReplaceChild("rmleg3",
                CubeListBuilder.create().texOffs(30, 14)
                        .addBox(-31.0F, -2.0F, 0.5F, 10.0F, 1.0F, 1.0F),
                PartPose.offsetAndRotation(-6.0F, 15.0F, 0.0F, 0.0F, 0.0F, -0.3490659F));
        root.addOrReplaceChild("rrleg1",
                CubeListBuilder.create().texOffs(30, 0)
                        .addBox(-10.0F, 0.0F, -0.5F, 10.0F, 3.0F, 3.0F),
                PartPose.offsetAndRotation(-6.0F, 15.0F, 5.0F, 0.0F, 0.3490659F, -0.0872665F));
        root.addOrReplaceChild("rrleg2",
                CubeListBuilder.create().texOffs(30, 8)
                        .addBox(-21.0F, -1.0F, 0.0F, 11.0F, 2.0F, 2.0F),
                PartPose.offsetAndRotation(-6.0F, 15.0F, 5.0F, 0.0F, 0.3490659F, (float) (-Math.PI / 12)));
        root.addOrReplaceChild("rrleg3",
                CubeListBuilder.create().texOffs(30, 14)
                        .addBox(-31.0F, -2.0F, 0.5F, 10.0F, 1.0F, 1.0F),
                PartPose.offsetAndRotation(-6.0F, 15.0F, 5.0F, 0.0F, 0.3490659F, -0.3490659F));

        root.addOrReplaceChild("jaw9",
                CubeListBuilder.create().texOffs(121, 70)
                        .addBox(-0.5F, -12.0F, -25.0F, 1.0F, 5.0F, 1.0F),
                PartPose.offsetAndRotation(0.0F, 12.0F, -12.0F, 0.3141593F, 0.0F, 0.0F));

        return LayerDefinition.create(mesh, 256, 256);
    }

    @Override
    public ModelPart root() {
        return this.root;
    }

    @Override
    public void setupAnim(
            HerculesBeetle entity,
            float limbSwing,
            float limbSwingAmount,
            float ageInTicks,
            float netHeadYaw,
            float headPitch) {
        float f1 = limbSwingAmount;
        float f2 = ageInTicks;
        float newangle;

        // gold: walk legs — cos(f2 * wingspeed * 0.45) * PI * 0.12 * f1
        newangle = Mth.cos(f2 * this.wingspeed * 0.45F) * (float) Math.PI * 0.12F * f1;
        this.lfleg1.yRot = 0.349F + newangle;
        this.lfleg2.yRot = this.lfleg1.yRot;
        this.lfleg3.yRot = this.lfleg1.yRot;
        this.lmleg1.yRot = -newangle;
        this.lmleg2.yRot = this.lmleg1.yRot;
        this.lmleg3.yRot = this.lmleg1.yRot;
        this.lrleg1.yRot = -0.349F + newangle;
        this.lrleg2.yRot = this.lrleg1.yRot;
        this.lrleg3.yRot = this.lrleg1.yRot;
        this.rfleg1.yRot = -0.349F + newangle;
        this.rfleg2.yRot = this.rfleg1.yRot;
        this.rfleg3.yRot = this.rfleg1.yRot;
        this.rmleg1.yRot = -newangle;
        this.rmleg2.yRot = this.rmleg1.yRot;
        this.rmleg3.yRot = this.rmleg1.yRot;
        this.rrleg1.yRot = 0.349F + newangle;
        this.rrleg2.yRot = this.rrleg1.yRot;
        this.rrleg3.yRot = this.rrleg1.yRot;

        // gold: jaw idle vs attack
        if (entity.getAttacking() == 0) {
            newangle = Mth.cos(f2 * 0.051F * this.wingspeed) * (float) Math.PI * 0.01F;
        } else {
            newangle = Mth.cos(f2 * 0.51F * this.wingspeed) * (float) Math.PI * 0.07F;
        }
        this.jaw1.xRot = 0.122F + newangle;
        this.jaw2.xRot = 0.122F + newangle;
        this.jaw3.xRot = 0.0F + newangle;
        this.jaw4.xRot = 0.0F + newangle;
        this.jaw5.xRot = 0.122F + newangle;
        this.jaw6.xRot = 0.122F + newangle;
        this.jaw7.xRot = 0.0F + newangle;
        this.jaw8.xRot = 0.0F + newangle;
        this.jaw9.xRot = 0.314F + newangle;
    }
}
