package danger.orespawn.client.model;

import danger.orespawn.entity.DungeonBeast;
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
 * Port of gold {@code ModelDungeonBeast} (1.7.10 ModelBase, tex 128×64) → 1.21 HierarchicalModel.
 * Cubes / pivots / UV / base rots 1:1. Full gold {@code func_78088_a} leg/spike/tail/jaw anim.
 * Wingspeed default 0.62 matches ClientProxy {@code new ModelDungeonBeast(0.62F)}.
 * Y-90° mesh rotate is applied in {@code DungeonBeastRenderer.setupRotations}.
 */
@OnlyIn(Dist.CLIENT)
public class ModelDungeonBeast extends HierarchicalModel<DungeonBeast> {
    /** Model layer location. */
    public static final ModelLayerLocation LAYER_LOCATION =
            new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "dungeon_beast"), "main");

    private final float wingspeed;
    private final ModelPart root;
    private final ModelPart tail7;
    private final ModelPart head3;
    private final ModelPart neck;
    private final ModelPart lhornbase;
    private final ModelPart leye;
    private final ModelPart ljaw3;
    private final ModelPart ljaw1;
    private final ModelPart ljaw2;
    private final ModelPart rjaw1;
    private final ModelPart rjaw2;
    private final ModelPart rjaw3;
    private final ModelPart t1s3;
    private final ModelPart rshoulder;
    private final ModelPart rheel;
    private final ModelPart lshoulder;
    private final ModelPart rleg1;
    private final ModelPart rleg2;
    private final ModelPart lleg1;
    private final ModelPart lleg2;
    private final ModelPart rfoot;
    private final ModelPart ltoe3;
    private final ModelPart ltoe2;
    private final ModelPart ltoe1;
    private final ModelPart head1;
    private final ModelPart horn2;
    private final ModelPart rhornbase;
    private final ModelPart rh1;
    private final ModelPart lh1;
    private final ModelPart lh2;
    private final ModelPart rh2;
    private final ModelPart rh3;
    private final ModelPart lh3;
    private final ModelPart lh4;
    private final ModelPart rh4;
    private final ModelPart horn1;
    private final ModelPart t2s3;
    private final ModelPart tail3;
    private final ModelPart t4s1;
    private final ModelPart t6s1;
    private final ModelPart tail6;
    private final ModelPart body;
    private final ModelPart bodys1;
    private final ModelPart bodys2;
    private final ModelPart tail1;
    private final ModelPart bodys3;
    private final ModelPart t1s1;
    private final ModelPart t1s2;
    private final ModelPart tail2;
    private final ModelPart t3s2;
    private final ModelPart t2s2;
    private final ModelPart t2s1;
    private final ModelPart t3s1;
    private final ModelPart tail4;
    private final ModelPart tail5;
    private final ModelPart t5s1;
    private final ModelPart head2;
    private final ModelPart reye;
    private final ModelPart lfoot;
    private final ModelPart rfoot2;
    private final ModelPart lfoot2;
    private final ModelPart lheel;
    private final ModelPart rtoe3;
    private final ModelPart rtoe2;
    private final ModelPart rtoe1;

    public ModelDungeonBeast(ModelPart root) {
        this(root, 0.62F);
    }

    public ModelDungeonBeast(ModelPart root, float wingspeed) {
        this.root = root;
        this.wingspeed = wingspeed;
        this.tail7 = root.getChild("tail7");
        this.head3 = root.getChild("head3");
        this.neck = root.getChild("neck");
        this.lhornbase = root.getChild("lhornbase");
        this.leye = root.getChild("leye");
        this.ljaw3 = root.getChild("ljaw3");
        this.ljaw1 = root.getChild("ljaw1");
        this.ljaw2 = root.getChild("ljaw2");
        this.rjaw1 = root.getChild("rjaw1");
        this.rjaw2 = root.getChild("rjaw2");
        this.rjaw3 = root.getChild("rjaw3");
        this.t1s3 = root.getChild("t1s3");
        this.rshoulder = root.getChild("rshoulder");
        this.rheel = root.getChild("rheel");
        this.lshoulder = root.getChild("lshoulder");
        this.rleg1 = root.getChild("rleg1");
        this.rleg2 = root.getChild("rleg2");
        this.lleg1 = root.getChild("lleg1");
        this.lleg2 = root.getChild("lleg2");
        this.rfoot = root.getChild("rfoot");
        this.ltoe3 = root.getChild("ltoe3");
        this.ltoe2 = root.getChild("ltoe2");
        this.ltoe1 = root.getChild("ltoe1");
        this.head1 = root.getChild("head1");
        this.horn2 = root.getChild("horn2");
        this.rhornbase = root.getChild("rhornbase");
        this.rh1 = root.getChild("rh1");
        this.lh1 = root.getChild("lh1");
        this.lh2 = root.getChild("lh2");
        this.rh2 = root.getChild("rh2");
        this.rh3 = root.getChild("rh3");
        this.lh3 = root.getChild("lh3");
        this.lh4 = root.getChild("lh4");
        this.rh4 = root.getChild("rh4");
        this.horn1 = root.getChild("horn1");
        this.t2s3 = root.getChild("t2s3");
        this.tail3 = root.getChild("tail3");
        this.t4s1 = root.getChild("t4s1");
        this.t6s1 = root.getChild("t6s1");
        this.tail6 = root.getChild("tail6");
        this.body = root.getChild("body");
        this.bodys1 = root.getChild("bodys1");
        this.bodys2 = root.getChild("bodys2");
        this.tail1 = root.getChild("tail1");
        this.bodys3 = root.getChild("bodys3");
        this.t1s1 = root.getChild("t1s1");
        this.t1s2 = root.getChild("t1s2");
        this.tail2 = root.getChild("tail2");
        this.t3s2 = root.getChild("t3s2");
        this.t2s2 = root.getChild("t2s2");
        this.t2s1 = root.getChild("t2s1");
        this.t3s1 = root.getChild("t3s1");
        this.tail4 = root.getChild("tail4");
        this.tail5 = root.getChild("tail5");
        this.t5s1 = root.getChild("t5s1");
        this.head2 = root.getChild("head2");
        this.reye = root.getChild("reye");
        this.lfoot = root.getChild("lfoot");
        this.rfoot2 = root.getChild("rfoot2");
        this.lfoot2 = root.getChild("lfoot2");
        this.lheel = root.getChild("lheel");
        this.rtoe3 = root.getChild("rtoe3");
        this.rtoe2 = root.getChild("rtoe2");
        this.rtoe1 = root.getChild("rtoe1");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        // gold ModelBase tex 128×64; cubes/pivots/base rots 1:1
        root.addOrReplaceChild("tail7",
                CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -0.5F, -0.5333334F, 3.0F, 1.0F, 1.0F),
                PartPose.offsetAndRotation(-24.0F, 23.5F, 0.0F, 0.0F, 0.0F, 3.141593F));
        root.addOrReplaceChild("head3",
                CubeListBuilder.create().texOffs(0, 0).addBox(2.0F, -2.466667F, 4.3F, 4.0F, 4.0F, 2.0F),
                PartPose.offsetAndRotation(5.0F, 15.0F, 0.0F, 0.0F, 0.8028515F, 0.0F));
        root.addOrReplaceChild("neck",
                CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, -1.5F, -2.533333F, 3.0F, 3.0F, 5.0F),
                PartPose.offsetAndRotation(5.0F, 15.0F, 0.0F, 0.0F, 0.0F, -0.1745329F));
        root.addOrReplaceChild("lhornbase",
                CubeListBuilder.create().texOffs(0, 0).addBox(2.5F, -3.0F, 0.5F, 3.0F, 1.0F, 2.0F),
                PartPose.offset(5.0F, 15.0F, 0.0F));
        root.addOrReplaceChild("leye",
                CubeListBuilder.create().texOffs(14, 15).addBox(3.0F, -1.466667F, 3.3F, 2.0F, 1.0F, 2.0F),
                PartPose.offsetAndRotation(5.0F, 15.0F, 0.0F, 0.0F, 0.4363323F, 0.0F));
        root.addOrReplaceChild("ljaw3",
                CubeListBuilder.create().texOffs(10, 28).addBox(3.5F, 0.0F, 1.5F, 1.0F, 1.0F, 1.0F),
                PartPose.offsetAndRotation(10.0F, 16.0F, 2.0F, 0.0F, (float) (Math.PI / 6), 0.0F));
        root.addOrReplaceChild("ljaw1",
                CubeListBuilder.create().texOffs(10, 20).addBox(0.0F, 0.0F, -1.466667F, 3.0F, 1.0F, 2.0F),
                PartPose.offsetAndRotation(10.0F, 16.0F, 2.0F, 0.0F, -0.3490659F, 0.0F));
        root.addOrReplaceChild("ljaw2",
                CubeListBuilder.create().texOffs(10, 24).addBox(2.0F, 0.0F, 0.3F, 2.0F, 1.0F, 2.0F),
                PartPose.offsetAndRotation(10.0F, 16.0F, 2.0F, 0.0F, 0.3490659F, 0.0F));
        root.addOrReplaceChild("rjaw1",
                CubeListBuilder.create().texOffs(10, 20).addBox(0.0F, 0.0F, -0.4666667F, 3.0F, 1.0F, 2.0F),
                PartPose.offsetAndRotation(10.0F, 16.0F, -2.0F, 0.0F, 0.3490659F, 0.0F));
        root.addOrReplaceChild("rjaw2",
                CubeListBuilder.create().texOffs(10, 24).addBox(2.0F, 0.0F, -2.3F, 2.0F, 1.0F, 2.0F),
                PartPose.offsetAndRotation(10.0F, 16.0F, -2.0F, 0.0F, -0.3490659F, 0.0F));
        root.addOrReplaceChild("rjaw3",
                CubeListBuilder.create().texOffs(10, 28).addBox(3.5F, 0.0F, -2.5F, 1.0F, 1.0F, 1.0F),
                PartPose.offsetAndRotation(10.0F, 16.0F, -2.0F, 0.0F, (float) (-Math.PI / 6), 0.0F));
        root.addOrReplaceChild("t1s3",
                CubeListBuilder.create().texOffs(75, 0).addBox(-3.0F, -7.0F, -0.5F, 1.0F, 4.0F, 1.0F),
                PartPose.offsetAndRotation(-1.0F, 15.0F, 0.0F, 0.0F, 0.0F, -0.8726646F));
        root.addOrReplaceChild("rshoulder",
                CubeListBuilder.create().texOffs(0, 0).addBox(2.0F, -2.2F, -5.0F, 4.0F, 4.0F, 2.0F),
                PartPose.offset(-1.0F, 15.0F, 0.0F));
        root.addOrReplaceChild("rheel",
                CubeListBuilder.create().texOffs(0, 0).addBox(-2.3F, 0.3F, 6.0F, 1.0F, 1.0F, 1.0F),
                PartPose.offsetAndRotation(3.0F, 17.0F, -7.0F, -1.570796F, 0.0F, 0.0F));
        root.addOrReplaceChild("lshoulder",
                CubeListBuilder.create().texOffs(0, 0).addBox(2.0F, -2.2F, 3.0F, 4.0F, 4.0F, 2.0F),
                PartPose.offset(-1.0F, 15.0F, 0.0F));
        root.addOrReplaceChild("rleg1",
                CubeListBuilder.create().texOffs(0, 0).addBox(-1.466667F, -2.0F, -5.0F, 3.0F, 3.0F, 6.0F),
                PartPose.offsetAndRotation(3.0F, 15.0F, -4.0F, (float) (Math.PI * 2.0 / 9.0), 0.0F, 0.0F));
        root.addOrReplaceChild("rleg2",
                CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -0.2F, 0.0F, 2.0F, 2.0F, 6.0F),
                PartPose.offsetAndRotation(3.0F, 17.0F, -7.0F, -1.570796F, 0.0F, 0.0F));
        root.addOrReplaceChild("lleg1",
                CubeListBuilder.create().texOffs(0, 0).addBox(-1.466667F, -2.0F, -1.0F, 3.0F, 3.0F, 6.0F),
                PartPose.offsetAndRotation(3.0F, 15.0F, 4.0F, (float) (-Math.PI * 2.0 / 9.0), 0.0F, 0.0F));
        root.addOrReplaceChild("lleg2",
                CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -1.8F, 0.0F, 2.0F, 2.0F, 6.0F),
                PartPose.offsetAndRotation(3.0F, 17.0F, 7.0F, -1.570796F, 0.0F, 0.0F));
        root.addOrReplaceChild("rfoot",
                CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, -0.7F, 5.0F, 3.0F, 3.0F, 2.0F),
                PartPose.offsetAndRotation(3.0F, 17.0F, -7.0F, -1.570796F, 0.0F, 0.0F));
        root.addOrReplaceChild("ltoe3",
                CubeListBuilder.create().texOffs(32, 0).addBox(-3.7F, -1.5F, 4.5F, 1.0F, 1.0F, 2.0F),
                PartPose.offsetAndRotation(3.0F, 17.0F, 7.0F, -1.570796F, (float) (Math.PI / 4), (float) (-Math.PI / 4)));
        root.addOrReplaceChild("ltoe2",
                CubeListBuilder.create().texOffs(32, 0).addBox(-3.0F, -1.3F, 5.2F, 1.0F, 1.0F, 2.0F),
                PartPose.offsetAndRotation(3.0F, 17.0F, 7.0F, -1.570796F, 0.0F, (float) (-Math.PI / 4)));
        root.addOrReplaceChild("ltoe1",
                CubeListBuilder.create().texOffs(32, 0).addBox(-3.0F, -0.6F, 5.2F, 1.0F, 1.0F, 2.0F),
                PartPose.offsetAndRotation(3.0F, 17.0F, 7.0F, -1.570796F, (float) (-Math.PI / 4), (float) (-Math.PI / 4)));
        root.addOrReplaceChild("head1",
                CubeListBuilder.create().texOffs(0, 0).addBox(2.0F, -2.466667F, -3.0F, 4.0F, 4.0F, 6.0F),
                PartPose.offset(5.0F, 15.0F, 0.0F));
        root.addOrReplaceChild("horn2",
                CubeListBuilder.create().texOffs(75, 6).addBox(-7.0F, -4.0F, -0.5F, 1.0F, 2.0F, 1.0F),
                PartPose.offsetAndRotation(5.0F, 15.0F, 0.0F, 0.0F, 0.0F, 2.181662F));
        root.addOrReplaceChild("rhornbase",
                CubeListBuilder.create().texOffs(0, 0).addBox(2.5F, -3.0F, -2.5F, 3.0F, 1.0F, 2.0F),
                PartPose.offset(5.0F, 15.0F, 0.0F));
        root.addOrReplaceChild("rh1",
                CubeListBuilder.create().texOffs(0, 28).addBox(4.0F, -3.0F, -2.5F, 2.0F, 3.0F, 2.0F),
                PartPose.offsetAndRotation(5.0F, 15.0F, 0.0F, 0.0F, 0.0F, (float) (-Math.PI / 6)));
        root.addOrReplaceChild("lh1",
                CubeListBuilder.create().texOffs(0, 28).addBox(4.0F, -3.0F, 0.5F, 2.0F, 3.0F, 2.0F),
                PartPose.offsetAndRotation(5.0F, 15.0F, 0.0F, 0.0F, 0.0F, (float) (-Math.PI / 6)));
        root.addOrReplaceChild("lh2",
                CubeListBuilder.create().texOffs(0, 23).addBox(5.0F, -4.0F, 1.0F, 1.0F, 3.0F, 1.0F),
                PartPose.offsetAndRotation(5.0F, 15.0F, 0.0F, 0.0F, 0.0F, -0.8726646F));
        root.addOrReplaceChild("rh2",
                CubeListBuilder.create().texOffs(0, 23).addBox(5.0F, -4.0F, -2.0F, 1.0F, 3.0F, 1.0F),
                PartPose.offsetAndRotation(5.0F, 15.0F, 0.0F, 0.0F, 0.0F, -0.8726646F));
        root.addOrReplaceChild("rh3",
                CubeListBuilder.create().texOffs(0, 19).addBox(6.1F, -2.4F, -2.0F, 1.0F, 2.0F, 1.0F),
                PartPose.offsetAndRotation(5.0F, 15.0F, 0.0F, 0.0F, 0.0F, -1.396263F));
        root.addOrReplaceChild("lh3",
                CubeListBuilder.create().texOffs(0, 19).addBox(6.1F, -2.4F, 1.0F, 1.0F, 2.0F, 1.0F),
                PartPose.offsetAndRotation(5.0F, 15.0F, 0.0F, 0.0F, 0.0F, -1.396263F));
        root.addOrReplaceChild("lh4",
                CubeListBuilder.create().texOffs(0, 15).addBox(6.5F, -1.8F, 1.0F, 1.0F, 2.0F, 1.0F),
                PartPose.offsetAndRotation(5.0F, 15.0F, 0.0F, 0.0F, 0.0F, -1.745329F));
        root.addOrReplaceChild("rh4",
                CubeListBuilder.create().texOffs(0, 15).addBox(6.5F, -1.8F, -2.0F, 1.0F, 2.0F, 1.0F),
                PartPose.offsetAndRotation(5.0F, 15.0F, 0.0F, 0.0F, 0.0F, -1.745329F));
        root.addOrReplaceChild("horn1",
                CubeListBuilder.create().texOffs(75, 6).addBox(-8.0F, -2.5F, -0.5F, 1.0F, 2.0F, 1.0F),
                PartPose.offsetAndRotation(5.0F, 15.0F, 0.0F, 0.0F, 0.0F, 2.617994F));
        root.addOrReplaceChild("t2s3",
                CubeListBuilder.create().texOffs(75, 0).addBox(3.0F, 3.466667F, -0.5333334F, 1.0F, 3.0F, 1.0F),
                PartPose.offsetAndRotation(-6.0F, 17.0F, 0.0F, 0.0F, 0.0F, 2.007129F));
        root.addOrReplaceChild("tail3",
                CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -1.5F, -2.5F, 6.0F, 3.0F, 5.0F),
                PartPose.offsetAndRotation(-10.0F, 20.0F, 0.0F, 0.0F, 0.0F, 2.530727F));
        root.addOrReplaceChild("t4s1",
                CubeListBuilder.create().texOffs(75, 0).addBox(0.5333334F, 1.533333F, -0.4666667F, 1.0F, 2.0F, 1.0F),
                PartPose.offsetAndRotation(-14.0F, 22.8F, 0.0F, 0.0F, 0.0F, 2.356194F));
        root.addOrReplaceChild("t6s1",
                CubeListBuilder.create().texOffs(75, 0).addBox(0.0F, 0.5F, -0.5F, 1.0F, 1.0F, 1.0F),
                PartPose.offsetAndRotation(-21.0F, 23.5F, 0.0F, 0.0F, 0.0F, 2.356194F));
        root.addOrReplaceChild("tail6",
                CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -0.5F, -1.0F, 4.0F, 1.0F, 2.0F),
                PartPose.offsetAndRotation(-21.0F, 23.5F, 0.0F, 0.0F, 0.0F, 3.141593F));
        root.addOrReplaceChild("body",
                CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, -3.0F, -4.0F, 7.0F, 6.0F, 8.0F),
                PartPose.offset(-1.0F, 15.0F, 0.0F));
        root.addOrReplaceChild("bodys1",
                CubeListBuilder.create().texOffs(75, 0).addBox(6.0F, -3.0F, -0.5F, 1.0F, 4.0F, 1.0F),
                PartPose.offsetAndRotation(-1.0F, 15.0F, 0.0F, 0.0F, 0.0F, (float) (-Math.PI / 6)));
        root.addOrReplaceChild("bodys2",
                CubeListBuilder.create().texOffs(75, 0).addBox(4.0F, -4.0F, -0.5F, 1.0F, 4.0F, 1.0F),
                PartPose.offsetAndRotation(-1.0F, 15.0F, 0.0F, 0.0F, 0.0F, (float) (-Math.PI / 6)));
        root.addOrReplaceChild("tail1",
                CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -2.533333F, -3.5F, 7.0F, 5.0F, 7.0F),
                PartPose.offsetAndRotation(-1.0F, 15.0F, 0.0F, 0.0F, 0.0F, (float) Math.PI * 8.0F / 9.0F));
        root.addOrReplaceChild("bodys3",
                CubeListBuilder.create().texOffs(75, 0).addBox(2.0F, -5.0F, -0.5F, 1.0F, 4.0F, 1.0F),
                PartPose.offsetAndRotation(-1.0F, 15.0F, 0.0F, 0.0F, 0.0F, (float) (-Math.PI / 6)));
        root.addOrReplaceChild("t1s1",
                CubeListBuilder.create().texOffs(75, 0).addBox(1.0F, -5.0F, -0.5F, 1.0F, 4.0F, 1.0F),
                PartPose.offsetAndRotation(-1.0F, 15.0F, 0.0F, 0.0F, 0.0F, -0.8726646F));
        root.addOrReplaceChild("t1s2",
                CubeListBuilder.create().texOffs(75, 0).addBox(-1.0F, -6.0F, -0.5F, 1.0F, 4.0F, 1.0F),
                PartPose.offsetAndRotation(-1.0F, 15.0F, 0.0F, 0.0F, 0.0F, -0.8726646F));
        root.addOrReplaceChild("tail2",
                CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -2.0F, -3.0F, 7.0F, 4.0F, 6.0F),
                PartPose.offsetAndRotation(-6.0F, 17.0F, 0.0F, 0.0F, 0.0F, 2.530727F));
        root.addOrReplaceChild("t3s2",
                CubeListBuilder.create().texOffs(75, 0).addBox(2.5F, 2.466667F, -0.5333334F, 1.0F, 3.0F, 1.0F),
                PartPose.offsetAndRotation(-10.0F, 20.0F, 0.0F, 0.0F, 0.0F, 2.007129F));
        root.addOrReplaceChild("t2s2",
                CubeListBuilder.create().texOffs(75, 0).addBox(1.0F, 2.466667F, -0.5333334F, 1.0F, 3.0F, 1.0F),
                PartPose.offsetAndRotation(-6.0F, 17.0F, 0.0F, 0.0F, 0.0F, 2.007129F));
        root.addOrReplaceChild("t2s1",
                CubeListBuilder.create().texOffs(75, 0).addBox(-1.0F, 1.466667F, -0.5333334F, 1.0F, 3.0F, 1.0F),
                PartPose.offsetAndRotation(-6.0F, 17.0F, 0.0F, 0.0F, 0.0F, 2.007129F));
        root.addOrReplaceChild("t3s1",
                CubeListBuilder.create().texOffs(75, 0).addBox(0.5F, 1.466667F, -0.5333334F, 1.0F, 3.0F, 1.0F),
                PartPose.offsetAndRotation(-10.0F, 20.0F, 0.0F, 0.0F, 0.0F, 2.007129F));
        root.addOrReplaceChild("tail4",
                CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -1.0F, -2.0F, 5.0F, 2.0F, 4.0F),
                PartPose.offsetAndRotation(-14.0F, 22.8F, 0.0F, 0.0F, 0.0F, 3.054326F));
        root.addOrReplaceChild("tail5",
                CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -0.5F, -1.5F, 4.0F, 1.0F, 3.0F),
                PartPose.offsetAndRotation(-18.0F, 23.2F, 0.0F, 0.0F, 0.0F, 3.054326F));
        root.addOrReplaceChild("t5s1",
                CubeListBuilder.create().texOffs(75, 0).addBox(0.0F, 0.5F, -0.5F, 1.0F, 2.0F, 1.0F),
                PartPose.offsetAndRotation(-18.0F, 23.2F, 0.0F, 0.0F, 0.0F, 2.356194F));
        root.addOrReplaceChild("head2",
                CubeListBuilder.create().texOffs(0, 0).addBox(2.0F, -2.466667F, -6.3F, 4.0F, 4.0F, 2.0F),
                PartPose.offsetAndRotation(5.0F, 15.0F, 0.0F, 0.0F, -0.8028515F, 0.0F));
        root.addOrReplaceChild("reye",
                CubeListBuilder.create().texOffs(5, 15).addBox(3.0F, -1.466667F, -5.3F, 2.0F, 1.0F, 2.0F),
                PartPose.offsetAndRotation(5.0F, 15.0F, 0.0F, 0.0F, -0.4363323F, 0.0F));
        root.addOrReplaceChild("lfoot",
                CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, -2.3F, 5.0F, 3.0F, 3.0F, 2.0F),
                PartPose.offsetAndRotation(3.0F, 17.0F, 7.0F, -1.570796F, 0.0F, 0.0F));
        root.addOrReplaceChild("rfoot2",
                CubeListBuilder.create().texOffs(0, 0).addBox(0.6F, -1.5F, 5.0F, 2.0F, 2.0F, 2.0F),
                PartPose.offsetAndRotation(3.0F, 17.0F, -7.0F, -1.570796F, (float) (Math.PI / 4), 0.0F));
        root.addOrReplaceChild("lfoot2",
                CubeListBuilder.create().texOffs(0, 0).addBox(0.7F, -0.5F, 5.0F, 2.0F, 2.0F, 2.0F),
                PartPose.offsetAndRotation(3.0F, 17.0F, 7.0F, -1.570796F, (float) (-Math.PI / 4), 0.0F));
        root.addOrReplaceChild("lheel",
                CubeListBuilder.create().texOffs(0, 0).addBox(-2.3F, -1.3F, 6.0F, 1.0F, 1.0F, 1.0F),
                PartPose.offsetAndRotation(3.0F, 17.0F, 7.0F, -1.570796F, 0.0F, 0.0F));
        root.addOrReplaceChild("rtoe3",
                CubeListBuilder.create().texOffs(32, 0).addBox(-3.7F, 0.6F, 4.5F, 1.0F, 1.0F, 2.0F),
                PartPose.offsetAndRotation(3.0F, 17.0F, -7.0F, -1.570796F, (float) (-Math.PI / 4), (float) (-Math.PI / 4)));
        root.addOrReplaceChild("rtoe2",
                CubeListBuilder.create().texOffs(32, 0).addBox(-3.0F, 0.3F, 5.2F, 1.0F, 1.0F, 2.0F),
                PartPose.offsetAndRotation(3.0F, 17.0F, -7.0F, -1.570796F, 0.0F, (float) (-Math.PI / 4)));
        root.addOrReplaceChild("rtoe1",
                CubeListBuilder.create().texOffs(32, 0).addBox(-3.0F, -0.6F, 5.2F, 1.0F, 1.0F, 2.0F),
                PartPose.offsetAndRotation(3.0F, 17.0F, -7.0F, -1.570796F, (float) (Math.PI / 4), (float) (-Math.PI / 4)));

        return LayerDefinition.create(mesh, 128, 64);
    }

    @Override
    public ModelPart root() {
        return this.root;
    }

    /**
     * Gold {@code func_78088_a} anims (empty {@code func_78087_a}):
     * walk zRot legs/feet/toes; phase-lagged spike xRot; tail yRot chain with cos/sin pivot follow;
     * jaw yRot chomp via RenderInfo.ri1 + getAttacking.
     */
    @Override
    public void setupAnim(
            DungeonBeast entity,
            float limbSwing,
            float limbSwingAmount,
            float ageInTicks,
            float netHeadYaw,
            float headPitch) {
        float f1 = limbSwingAmount;
        float f2 = ageInTicks;
        float newangle;
        float nextangle;
        float tailamp;
        float pi4 = 0.39269876F;

        // --- walk legs (gold zRot shared across right / left chains) ---
        newangle = Mth.cos(f2 * 1.4F * this.wingspeed) * (float) Math.PI * 0.22F * f1;
        this.rleg1.zRot = newangle;
        this.rleg2.zRot = newangle;
        this.rfoot.zRot = newangle;
        this.rfoot2.zRot = newangle;
        this.rheel.zRot = newangle;
        this.rtoe2.zRot = -0.785F + newangle;
        this.lleg1.zRot = -newangle;
        this.lleg2.zRot = -newangle;
        this.lfoot.zRot = -newangle;
        this.lfoot2.zRot = -newangle;
        this.lheel.zRot = -newangle;
        this.ltoe2.zRot = -0.785F - newangle;

        // --- spike / body spine wave (phase-lagged xRot) ---
        this.bodys1.xRot = Mth.cos(f2 * 0.5F * this.wingspeed) * (float) Math.PI * 0.07F;
        this.bodys2.xRot = Mth.cos(f2 * 0.5F * this.wingspeed + pi4) * (float) Math.PI * 0.07F;
        this.bodys3.xRot = Mth.cos(f2 * 0.5F * this.wingspeed + 2.0F * pi4) * (float) Math.PI * 0.07F;
        this.t1s1.xRot = Mth.cos(f2 * 0.5F * this.wingspeed + 3.0F * pi4) * (float) Math.PI * 0.07F;
        this.t1s2.xRot = Mth.cos(f2 * 0.5F * this.wingspeed + 4.0F * pi4) * (float) Math.PI * 0.07F;
        this.t1s3.xRot = Mth.cos(f2 * 0.5F * this.wingspeed + 5.0F * pi4) * (float) Math.PI * 0.07F;
        this.t2s1.xRot = -Mth.cos(f2 * 0.5F * this.wingspeed + 6.0F * pi4) * (float) Math.PI * 0.07F;
        this.t2s2.xRot = -Mth.cos(f2 * 0.5F * this.wingspeed + 7.0F * pi4) * (float) Math.PI * 0.07F;
        this.t2s3.xRot = -Mth.cos(f2 * 0.5F * this.wingspeed + 8.0F * pi4) * (float) Math.PI * 0.07F;
        this.t3s1.xRot = -Mth.cos(f2 * 0.5F * this.wingspeed + 9.0F * pi4) * (float) Math.PI * 0.07F;
        this.t3s2.xRot = -Mth.cos(f2 * 0.5F * this.wingspeed + 10.0F * pi4) * (float) Math.PI * 0.07F;
        this.t4s1.xRot = -Mth.cos(f2 * 0.5F * this.wingspeed + 11.0F * pi4) * (float) Math.PI * 0.07F;
        this.t5s1.xRot = -Mth.cos(f2 * 0.5F * this.wingspeed + 12.0F * pi4) * (float) Math.PI * 0.07F;
        this.t6s1.xRot = -Mth.cos(f2 * 0.5F * this.wingspeed + 13.0F * pi4) * (float) Math.PI * 0.07F;

        // --- tail chain (amp 1.25 when attacking) ---
        if (entity.getAttacking() == 0) {
            tailamp = f1;
        } else {
            tailamp = 1.25F;
        }
        newangle = Mth.cos(f2 * 0.75F * this.wingspeed) * (float) Math.PI * 0.25F * tailamp;
        this.tail1.yRot = newangle * 0.25F;
        this.t1s1.yRot = this.tail1.yRot;
        this.t1s2.yRot = this.tail1.yRot;
        this.t1s3.yRot = this.tail1.yRot;

        this.tail2.yRot = newangle * 0.5F;
        this.tail2.x = this.tail1.x - (float) Math.cos(this.tail1.yRot) * 6.0F;
        this.tail2.z = this.tail1.z - (float) Math.sin(this.tail1.yRot) * 6.0F;
        this.t2s1.yRot = this.tail2.yRot;
        this.t2s2.yRot = this.tail2.yRot;
        this.t2s3.yRot = this.tail2.yRot;
        this.t2s1.z = this.tail2.z;
        this.t2s2.z = this.tail2.z;
        this.t2s3.z = this.tail2.z;
        this.t2s1.x = this.tail2.x;
        this.t2s2.x = this.tail2.x;
        this.t2s3.x = this.tail2.x;

        this.tail3.yRot = newangle * 0.75F;
        this.tail3.x = this.tail2.x - (float) Math.cos(this.tail2.yRot) * 5.0F;
        this.tail3.z = this.tail2.z - (float) Math.sin(this.tail2.yRot) * 5.0F;
        this.t3s1.yRot = this.tail3.yRot;
        this.t3s2.yRot = this.tail3.yRot;
        this.t3s1.z = this.tail3.z;
        this.t3s2.z = this.tail3.z;
        this.t3s1.x = this.tail3.x;
        this.t3s2.x = this.tail3.x;

        this.tail4.yRot = newangle;
        this.tail4.x = this.tail3.x - (float) Math.cos(this.tail3.yRot) * 4.5F;
        this.tail4.z = this.tail3.z - (float) Math.sin(this.tail3.yRot) * 4.5F;
        this.t4s1.yRot = this.tail4.yRot;
        this.t4s1.z = this.tail4.z;
        this.t4s1.x = this.tail4.x;

        this.tail5.yRot = newangle * 1.25F;
        this.tail5.x = this.tail4.x - (float) Math.cos(this.tail4.yRot) * 4.0F;
        this.tail5.z = this.tail4.z - (float) Math.sin(this.tail4.yRot) * 4.0F;
        this.t5s1.yRot = this.tail5.yRot;
        this.t5s1.z = this.tail5.z;
        this.t5s1.x = this.tail5.x;

        this.tail6.yRot = newangle * 1.5F;
        this.tail6.x = this.tail5.x - (float) Math.cos(this.tail5.yRot) * 3.0F;
        this.tail6.z = this.tail5.z - (float) Math.sin(this.tail5.yRot) * 3.0F;
        this.t6s1.yRot = this.tail6.yRot;
        this.t6s1.z = this.tail6.z;
        this.t6s1.x = this.tail6.x;

        this.tail7.yRot = newangle * 1.75F;
        this.tail7.x = this.tail6.x - (float) Math.cos(this.tail6.yRot) * 3.0F;
        this.tail7.z = this.tail6.z - (float) Math.sin(this.tail6.yRot) * 3.0F;

        // --- jaw chomp phase (RenderInfo.ri1) ---
        RenderInfo r = entity.getRenderInfo();
        newangle = Mth.cos(f2 * 2.0F * this.wingspeed) * (float) Math.PI * 0.15F;
        nextangle = Mth.cos((f2 + 0.1F) * 2.0F * this.wingspeed) * (float) Math.PI * 0.15F;
        if (nextangle > 0.0F && newangle < 0.0F) {
            if (entity.getAttacking() == 0) {
                r.ri1 = entity.getRandom().nextInt(15);
                r.ri2 = entity.getRandom().nextInt(15);
            } else {
                r.ri1 = 0;
                r.ri2 = 0;
            }
        }

        if (r.ri1 == 0) {
            this.ljaw1.yRot = -0.349F + newangle;
            this.ljaw2.yRot = 0.349F + newangle;
            this.ljaw3.yRot = 0.523F + newangle;
            this.rjaw1.yRot = 0.349F - newangle;
            this.rjaw2.yRot = -0.349F - newangle;
            this.rjaw3.yRot = -0.523F - newangle;
        } else {
            this.ljaw1.yRot = -0.349F;
            this.ljaw2.yRot = 0.349F;
            this.ljaw3.yRot = 0.523F;
            this.rjaw1.yRot = 0.349F;
            this.rjaw2.yRot = -0.349F;
            this.rjaw3.yRot = -0.523F;
        }

        entity.setRenderInfo(r);
    }
}
