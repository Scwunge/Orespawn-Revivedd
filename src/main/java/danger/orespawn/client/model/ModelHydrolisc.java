package danger.orespawn.client.model;

import danger.orespawn.entity.Hydrolisc;
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
 * Port of gold {@code ModelHydrolisc} (1.7.10 ModelBase, tex 64×128) → 1.21 HierarchicalModel.
 * Cube sizes/UVs/pivots/base rots 1:1. Wingspeed default 0.65 matches ClientProxy.
 * Full walk / tail / feather anims from gold {@code func_78088_a}.
 */
@OnlyIn(Dist.CLIENT)
public class ModelHydrolisc extends HierarchicalModel<Hydrolisc> {
    /** Model layer location. */
    public static final ModelLayerLocation LAYER_LOCATION =
            new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "hydrolisc"), "main");

    private final float wingspeed;
    private final ModelPart root;
    private final ModelPart tail2;
    private final ModelPart tail3;
    private final ModelPart body2;
    private final ModelPart lb2;
    private final ModelPart lb1;
    private final ModelPart spine3;
    private final ModelPart spine4;
    private final ModelPart rb1;
    private final ModelPart rb2;
    private final ModelPart spine1;
    private final ModelPart spine2;
    private final ModelPart lb3;
    private final ModelPart rb3;
    private final ModelPart body1;
    private final ModelPart body0;
    private final ModelPart lf1;
    private final ModelPart rf1;
    private final ModelPart rb6;
    private final ModelPart rb4;
    private final ModelPart rb5;
    private final ModelPart lb6;
    private final ModelPart lb5;
    private final ModelPart lb4;
    private final ModelPart head3;
    private final ModelPart feather3;
    private final ModelPart feather1;
    private final ModelPart feather2;
    private final ModelPart head1;
    private final ModelPart rf2;
    private final ModelPart rf3;
    private final ModelPart rf4;
    private final ModelPart rf5;
    private final ModelPart rf6;
    private final ModelPart lf2;
    private final ModelPart lf3;
    private final ModelPart lf4;
    private final ModelPart lf5;
    private final ModelPart lf6;
    private final ModelPart head2;
    private final ModelPart tail1;

    public ModelHydrolisc(ModelPart root) {
        this(root, 0.65F);
    }

    public ModelHydrolisc(ModelPart root, float wingspeed) {
        this.root = root;
        this.wingspeed = wingspeed;
        this.tail2 = root.getChild("tail2");
        this.tail3 = root.getChild("tail3");
        this.body2 = root.getChild("body2");
        this.lb2 = root.getChild("lb2");
        this.lb1 = root.getChild("lb1");
        this.spine3 = root.getChild("spine3");
        this.spine4 = root.getChild("spine4");
        this.rb1 = root.getChild("rb1");
        this.rb2 = root.getChild("rb2");
        this.spine1 = root.getChild("spine1");
        this.spine2 = root.getChild("spine2");
        this.lb3 = root.getChild("lb3");
        this.rb3 = root.getChild("rb3");
        this.body1 = root.getChild("body1");
        this.body0 = root.getChild("body0");
        this.lf1 = root.getChild("lf1");
        this.rf1 = root.getChild("rf1");
        this.rb6 = root.getChild("rb6");
        this.rb4 = root.getChild("rb4");
        this.rb5 = root.getChild("rb5");
        this.lb6 = root.getChild("lb6");
        this.lb5 = root.getChild("lb5");
        this.lb4 = root.getChild("lb4");
        this.head3 = root.getChild("head3");
        this.feather3 = root.getChild("feather3");
        this.feather1 = root.getChild("feather1");
        this.feather2 = root.getChild("feather2");
        this.head1 = root.getChild("head1");
        this.rf2 = root.getChild("rf2");
        this.rf3 = root.getChild("rf3");
        this.rf4 = root.getChild("rf4");
        this.rf5 = root.getChild("rf5");
        this.rf6 = root.getChild("rf6");
        this.lf2 = root.getChild("lf2");
        this.lf3 = root.getChild("lf3");
        this.lf4 = root.getChild("lf4");
        this.lf5 = root.getChild("lf5");
        this.lf6 = root.getChild("lf6");
        this.head2 = root.getChild("head2");
        this.tail1 = root.getChild("tail1");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        root.addOrReplaceChild("tail2",
                CubeListBuilder.create().texOffs(29, 3).addBox(-1.0F, 0.0F, -0.8F, 2.0F, 8.0F, 2.0F),
                PartPose.offsetAndRotation(1.0F, 20.0F, 13.53333F, 1.392442F, 0.0F, 0.0F));
        root.addOrReplaceChild("tail3",
                CubeListBuilder.create().texOffs(39, 0).addBox(-1.0F, -1.0F, -2.0F, 2.0F, 8.0F, 2.0F),
                PartPose.offsetAndRotation(1.0F, 20.0F, 21.0F, 1.72705F, 0.0F, 0.0F));
        root.addOrReplaceChild("body2",
                CubeListBuilder.create().texOffs(0, 99).addBox(-2.0F, 14.0F, 0.0F, 6.0F, 4.0F, 10.0F),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.0523599F, 0.0F, 0.0F));
        root.addOrReplaceChild("lb2",
                CubeListBuilder.create().texOffs(45, 13).addBox(0.0F, 0.0F, 3.0F, 3.0F, 2.0F, 5.0F),
                PartPose.offsetAndRotation(5.0F, 15.0F, 0.0F, -0.4886922F, 0.0F, 0.0F));
        root.addOrReplaceChild("lb1",
                CubeListBuilder.create().texOffs(46, 22).addBox(-1.0F, 0.0F, 0.0F, 4.0F, 3.0F, 3.0F),
                PartPose.offset(5.0F, 15.0F, 0.0F));
        root.addOrReplaceChild("spine3",
                CubeListBuilder.create().texOffs(11, 31).addBox(-1.0F, -5.0F, 0.0F, 2.0F, 6.0F, 2.0F),
                PartPose.offsetAndRotation(1.0F, 14.0F, 6.0F, -1.117011F, 0.0F, 0.0F));
        root.addOrReplaceChild("spine4",
                CubeListBuilder.create().texOffs(0, 30).addBox(-1.0F, -10.5F, -1.0F, 2.0F, 6.0F, 2.0F),
                PartPose.offsetAndRotation(1.0F, 14.0F, 6.0F, -1.343904F, 0.0F, 0.0F));
        root.addOrReplaceChild("rb1",
                CubeListBuilder.create().texOffs(46, 22).addBox(-4.0F, 0.0F, 0.0F, 4.0F, 3.0F, 3.0F),
                PartPose.offset(-2.0F, 15.0F, 0.0F));
        root.addOrReplaceChild("rb2",
                CubeListBuilder.create().texOffs(45, 13).addBox(-4.0F, 0.0F, 2.0F, 3.0F, 2.0F, 5.0F),
                PartPose.offsetAndRotation(-2.0F, 15.0F, 0.0F, -0.4886922F, 0.0F, 0.0F));
        root.addOrReplaceChild("spine1",
                CubeListBuilder.create().texOffs(33, 19).addBox(-1.0F, -5.0F, 0.0F, 2.0F, 6.0F, 2.0F),
                PartPose.offsetAndRotation(1.0F, 14.0F, 0.0F, -0.8552113F, 0.0F, 0.0F));
        root.addOrReplaceChild("spine2",
                CubeListBuilder.create().texOffs(21, 19).addBox(-1.0F, -10.5F, -1.5F, 2.0F, 6.0F, 2.0F),
                PartPose.offsetAndRotation(1.0F, 14.0F, 0.0F, -1.169371F, 0.0F, 0.0F));
        root.addOrReplaceChild("lb3",
                CubeListBuilder.create().texOffs(0, 58).addBox(0.0F, -8.0F, -2.0F, 3.0F, 2.0F, 6.0F),
                PartPose.offsetAndRotation(5.0F, 15.0F, 0.0F, -2.347623F, 0.0F, 0.0F));
        root.addOrReplaceChild("rb3",
                CubeListBuilder.create().texOffs(0, 58).addBox(-4.0F, -8.0F, -2.0F, 3.0F, 2.0F, 6.0F),
                PartPose.offsetAndRotation(-2.0F, 15.0F, 0.0F, -2.347623F, 0.0F, 0.0F));
        root.addOrReplaceChild("body1",
                CubeListBuilder.create().texOffs(0, 79).addBox(-2.0F, 16.0F, -7.0F, 4.0F, 2.0F, 5.0F),
                PartPose.offset(1.0F, -1.0F, 2.0F));
        root.addOrReplaceChild("body0",
                CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, 14.0F, -13.0F, 4.0F, 3.0F, 10.0F),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0523599F, 0.0F, 0.0F));
        root.addOrReplaceChild("lf1",
                CubeListBuilder.create().texOffs(45, 32).addBox(-1.0F, 0.0F, -2.0F, 4.0F, 3.0F, 3.0F),
                PartPose.offset(4.0F, 14.0F, -7.0F));
        root.addOrReplaceChild("rf1",
                CubeListBuilder.create().texOffs(45, 32).addBox(-3.0F, 0.0F, -2.0F, 4.0F, 3.0F, 3.0F),
                PartPose.offset(-2.0F, 14.0F, -7.0F));
        root.addOrReplaceChild("rb6",
                CubeListBuilder.create().texOffs(30, 39).addBox(-3.5F, 7.0F, 2.0F, 2.0F, 3.0F, 1.0F),
                PartPose.offsetAndRotation(-2.0F, 15.0F, 0.0F, 0.1745329F, 0.0F, 0.0F));
        root.addOrReplaceChild("rb4",
                CubeListBuilder.create().texOffs(20, 39).addBox(-2.0F, 3.0F, 6.0F, 1.0F, 4.0F, 1.0F),
                PartPose.offsetAndRotation(-2.0F, 15.0F, 0.0F, -0.6283185F, 0.0F, 0.0F));
        root.addOrReplaceChild("rb5",
                CubeListBuilder.create().texOffs(20, 39).addBox(-4.0F, 3.0F, 6.0F, 1.0F, 4.0F, 1.0F),
                PartPose.offsetAndRotation(-2.0F, 15.0F, 0.0F, -0.6283185F, 0.0F, 0.0F));
        root.addOrReplaceChild("lb6",
                CubeListBuilder.create().texOffs(30, 39).addBox(0.5F, 7.0F, 2.0F, 2.0F, 3.0F, 1.0F),
                PartPose.offsetAndRotation(5.0F, 15.0F, 0.0F, 0.1745329F, 0.0F, 0.0F));
        root.addOrReplaceChild("lb5",
                CubeListBuilder.create().texOffs(20, 39).addBox(2.0F, 3.0F, 6.0F, 1.0F, 4.0F, 1.0F),
                PartPose.offsetAndRotation(5.0F, 15.0F, 0.0F, -0.6283185F, 0.0F, 0.0F));
        root.addOrReplaceChild("lb4",
                CubeListBuilder.create().texOffs(20, 39).addBox(0.0F, 3.0F, 6.0F, 1.0F, 4.0F, 1.0F),
                PartPose.offsetAndRotation(5.0F, 15.0F, 0.0F, -0.6283185F, 0.0F, 0.0F));
        root.addOrReplaceChild("head3",
                CubeListBuilder.create().texOffs(38, 50).addBox(0.0F, 0.0F, 0.0F, 4.0F, 2.0F, 8.0F),
                PartPose.offsetAndRotation(-1.0F, 15.0F, -13.0F, (float) (Math.PI / 6), 0.0F, 0.0F));
        root.addOrReplaceChild("feather3",
                CubeListBuilder.create().texOffs(25, 117).addBox(0.0F, 0.0F, 1.0F, 1.0F, 2.0F, 9.0F),
                PartPose.offsetAndRotation(1.0F, 12.0F, -8.0F, 0.3490659F, (float) (Math.PI / 12), 0.0F));
        root.addOrReplaceChild("feather1",
                CubeListBuilder.create().texOffs(34, 100).addBox(0.0F, 0.0F, 1.0F, 1.0F, 2.0F, 9.0F),
                PartPose.offsetAndRotation(0.0F, 12.0F, -8.0F, 0.3490659F, (float) (-Math.PI / 12), 0.0F));
        root.addOrReplaceChild("feather2",
                CubeListBuilder.create().texOffs(0, 116).addBox(0.0F, 0.0F, 0.0F, 1.0F, 2.0F, 10.0F),
                PartPose.offsetAndRotation(0.5F, 11.0F, -6.0F, 0.3490659F, 0.0F, 0.0F));
        root.addOrReplaceChild("head1",
                CubeListBuilder.create().texOffs(38, 41).addBox(0.0F, 0.0F, 0.0F, 4.0F, 3.0F, 4.0F),
                PartPose.offsetAndRotation(-1.0F, 15.0F, -15.0F, 0.1396263F, 0.0F, 0.0F));
        root.addOrReplaceChild("rf2",
                CubeListBuilder.create().texOffs(19, 58).addBox(-3.0F, 0.0F, 0.0F, 3.0F, 3.0F, 6.0F),
                PartPose.offsetAndRotation(-2.0F, 14.0F, -7.0F, -0.4886922F, 0.0F, 0.0F));
        root.addOrReplaceChild("rf3",
                CubeListBuilder.create().texOffs(19, 47).addBox(-3.0F, -7.0F, 0.0F, 3.0F, 3.0F, 6.0F),
                PartPose.offsetAndRotation(-2.0F, 14.0F, -7.0F, -2.347623F, 0.0F, 0.0F));
        root.addOrReplaceChild("rf4",
                CubeListBuilder.create().texOffs(20, 39).addBox(0.0F, 6.0F, 4.0F, 1.0F, 4.0F, 1.0F),
                PartPose.offsetAndRotation(-3.0F, 14.0F, -7.0F, -0.6283185F, 0.0F, 0.0F));
        root.addOrReplaceChild("rf5",
                CubeListBuilder.create().texOffs(20, 39).addBox(-2.0F, 6.0F, 4.0F, 1.0F, 4.0F, 1.0F),
                PartPose.offsetAndRotation(-3.0F, 14.0F, -7.0F, -0.6283185F, 0.0F, 0.0F));
        root.addOrReplaceChild("rf6",
                CubeListBuilder.create().texOffs(30, 39).addBox(-2.5F, 6.0F, 0.0F, 2.0F, 5.0F, 1.0F),
                PartPose.offsetAndRotation(-2.0F, 14.0F, -7.0F, 0.1745329F, 0.0F, 0.0F));
        root.addOrReplaceChild("lf2",
                CubeListBuilder.create().texOffs(19, 58).addBox(0.0F, 0.0F, 0.0F, 3.0F, 3.0F, 6.0F),
                PartPose.offsetAndRotation(4.0F, 14.0F, -7.0F, -0.4886922F, 0.0F, 0.0F));
        root.addOrReplaceChild("lf3",
                CubeListBuilder.create().texOffs(19, 47).addBox(0.0F, -7.0F, 0.0F, 3.0F, 3.0F, 6.0F),
                PartPose.offsetAndRotation(4.0F, 14.0F, -7.0F, -2.347623F, 0.0F, 0.0F));
        root.addOrReplaceChild("lf4",
                CubeListBuilder.create().texOffs(20, 39).addBox(0.0F, 6.0F, 4.0F, 1.0F, 4.0F, 1.0F),
                PartPose.offsetAndRotation(4.0F, 14.0F, -7.0F, -0.6283185F, 0.0F, 0.0F));
        root.addOrReplaceChild("lf5",
                CubeListBuilder.create().texOffs(20, 39).addBox(2.0F, 6.0F, 4.0F, 1.0F, 4.0F, 1.0F),
                PartPose.offsetAndRotation(4.0F, 14.0F, -7.0F, -0.6283185F, 0.0F, 0.0F));
        root.addOrReplaceChild("lf6",
                CubeListBuilder.create().texOffs(30, 39).addBox(0.5F, 6.0F, -2.0F, 2.0F, 5.0F, 1.0F),
                PartPose.offsetAndRotation(4.0F, 14.0F, -5.0F, 0.1745329F, 0.0F, 0.0F));
        root.addOrReplaceChild("head2",
                CubeListBuilder.create().texOffs(19, 80).addBox(-1.0F, 16.0F, -16.0F, 4.0F, 1.0F, 5.0F),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.1047198F, 0.0F, 0.0F));
        root.addOrReplaceChild("tail1",
                CubeListBuilder.create().texOffs(9, 18).addBox(-1.0F, -1.0F, -3.0F, 2.0F, 8.0F, 2.0F),
                PartPose.offsetAndRotation(1.0F, 15.0F, 9.0F, 1.095163F, 0.0F, 0.0F));

        return LayerDefinition.create(mesh, 64, 128);
    }

    @Override
    public ModelPart root() {
        return this.root;
    }

    /**
     * Gold {@code func_78088_a} anims (empty {@code func_78087_a}):
     * walk legs (lf/rf/lb/rb segments), tail sway when not sitting, health-scaled feathers.
     */
    @Override
    public void setupAnim(
            Hydrolisc entity,
            float limbSwing,
            float limbSwingAmount,
            float ageInTicks,
            float netHeadYaw,
            float headPitch) {
        float f1 = limbSwingAmount;
        float f2 = ageInTicks;
        float newangle;

        // --- walk cycle: all four legs (6 segments each) ---
        if (f1 > 0.1F) {
            newangle = Mth.cos(f2 * 1.3F * this.wingspeed) * (float) Math.PI * 0.25F * f1;
        } else {
            newangle = 0.0F;
        }

        this.lf1.xRot = newangle;
        this.lf2.xRot = newangle - 0.488F;
        this.lf3.xRot = newangle - 2.347F;
        this.lf4.xRot = newangle - 0.628F;
        this.lf5.xRot = newangle - 0.628F;
        this.lf6.xRot = newangle + 0.174F;

        this.rf1.xRot = -newangle;
        this.rf2.xRot = -newangle - 0.488F;
        this.rf3.xRot = -newangle - 2.347F;
        this.rf4.xRot = -newangle - 0.628F;
        this.rf5.xRot = -newangle - 0.628F;
        this.rf6.xRot = -newangle + 0.174F;

        this.lb1.xRot = -newangle;
        this.lb2.xRot = -newangle - 0.488F;
        this.lb3.xRot = -newangle - 2.347F;
        this.lb4.xRot = -newangle - 0.628F;
        this.lb5.xRot = -newangle - 0.628F;
        this.lb6.xRot = -newangle + 0.174F;

        this.rb1.xRot = newangle;
        this.rb2.xRot = newangle - 0.488F;
        this.rb3.xRot = newangle - 2.347F;
        this.rb4.xRot = newangle - 0.628F;
        this.rb5.xRot = newangle - 0.628F;
        this.rb6.xRot = newangle + 0.174F;

        // --- tail sway (disabled when sitting) ---
        newangle = Mth.cos(f2 * 1.0F * this.wingspeed) * (float) Math.PI * 0.15F;
        if (entity.isSitting()) {
            newangle = 0.0F;
        }
        this.tail1.yRot = newangle * 0.25F;
        this.tail2.z = this.tail1.z + (float) Math.cos(this.tail1.yRot) * 5.0F;
        this.tail2.x = this.tail1.x + (float) Math.sin(this.tail1.yRot) * 5.0F;
        this.tail2.yRot = newangle * 0.5F;
        this.tail3.z = this.tail2.z + (float) Math.cos(this.tail2.yRot) * 8.0F;
        this.tail3.x = this.tail2.x + (float) Math.sin(this.tail2.yRot) * 8.0F;
        this.tail3.yRot = newangle * 0.75F;

        // --- feathers: speed scales with health fraction ---
        float hf = entity.getHydroHealth() / (float) entity.mygetMaxHealth();
        newangle = Mth.cos(f2 * 1.25F * this.wingspeed * hf) * (float) Math.PI * 0.2F * hf;
        this.feather2.yRot = newangle;
        newangle = Mth.cos(f2 * 0.75F * this.wingspeed * hf) * (float) Math.PI * 0.2F * hf;
        this.feather1.yRot = newangle - 0.9F;
        this.feather3.yRot = -newangle + 0.9F;
    }
}
