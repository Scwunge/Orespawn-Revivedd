package danger.orespawn.client.model;

import danger.orespawn.entity.Alien;
import danger.orespawn.entity.RenderInfo;
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
 * Port of gold {@code ModelAlien} (1.12 ModelBase) → 1.21 HierarchicalModel.
 */
@OnlyIn(Dist.CLIENT)
public class ModelAlien extends HierarchicalModel<Alien> {
    private final float wingspeed;
    private final ModelPart root;
    private final ModelPart torso;
    private final ModelPart stomach;
    private final ModelPart rThigh;
    private final ModelPart lThigh;
    private final ModelPart lShin;
    private final ModelPart rShin;
    private final ModelPart lShin1;
    private final ModelPart rShin1;
    private final ModelPart lFoot;
    private final ModelPart rFoot;
    private final ModelPart neck;
    private final ModelPart fan;
    private final ModelPart tail2;
    private final ModelPart tail3;
    private final ModelPart tail4;
    private final ModelPart tail5;
    private final ModelPart tail1;
    private final ModelPart fanl1;
    private final ModelPart fanr1;
    private final ModelPart fanl2;
    private final ModelPart fanr2;
    private final ModelPart fanl3;
    private final ModelPart fanr3;
    private final ModelPart fanl4;
    private final ModelPart fanr4;
    private final ModelPart fanl5;
    private final ModelPart fanr5;
    private final ModelPart fanl6;
    private final ModelPart fanr6;
    private final ModelPart spike4;
    private final ModelPart spike5;
    private final ModelPart spike3;
    private final ModelPart fanl7;
    private final ModelPart fanr7;
    private final ModelPart head;
    private final ModelPart head1;
    private final ModelPart jaw1;
    private final ModelPart head2;
    private final ModelPart jaw2;
    private final ModelPart fang1;
    private final ModelPart fang2;
    private final ModelPart fang3;
    private final ModelPart fang4;
    private final ModelPart spike2;
    private final ModelPart spike1;
    private final ModelPart arml1;
    private final ModelPart armr1;
    private final ModelPart arml2;
    private final ModelPart armr2;
    private final ModelPart clawr1;
    private final ModelPart clawr2;
    private final ModelPart clawr3;
    private final ModelPart clawl2;
    private final ModelPart clawl3;
    private final ModelPart clawl1;

    public ModelAlien(ModelPart root) {
        this(root, 0.1F);
    }

    public ModelAlien(ModelPart root, float wingspeed) {
        this.root = root;
        this.wingspeed = wingspeed;
        this.torso = root.getChild("torso");
        this.stomach = root.getChild("stomach");
        this.rThigh = root.getChild("rThigh");
        this.lThigh = root.getChild("lThigh");
        this.lShin = root.getChild("lShin");
        this.rShin = root.getChild("rShin");
        this.lShin1 = root.getChild("lShin1");
        this.rShin1 = root.getChild("rShin1");
        this.lFoot = root.getChild("lFoot");
        this.rFoot = root.getChild("rFoot");
        this.neck = root.getChild("neck");
        this.fan = root.getChild("fan");
        this.tail2 = root.getChild("tail2");
        this.tail3 = root.getChild("tail3");
        this.tail4 = root.getChild("tail4");
        this.tail5 = root.getChild("tail5");
        this.tail1 = root.getChild("tail1");
        this.fanl1 = root.getChild("fanl1");
        this.fanr1 = root.getChild("fanr1");
        this.fanl2 = root.getChild("fanl2");
        this.fanr2 = root.getChild("fanr2");
        this.fanl3 = root.getChild("fanl3");
        this.fanr3 = root.getChild("fanr3");
        this.fanl4 = root.getChild("fanl4");
        this.fanr4 = root.getChild("fanr4");
        this.fanl5 = root.getChild("fanl5");
        this.fanr5 = root.getChild("fanr5");
        this.fanl6 = root.getChild("fanl6");
        this.fanr6 = root.getChild("fanr6");
        this.spike4 = root.getChild("spike4");
        this.spike5 = root.getChild("spike5");
        this.spike3 = root.getChild("spike3");
        this.fanl7 = root.getChild("fanl7");
        this.fanr7 = root.getChild("fanr7");
        this.head = root.getChild("head");
        this.head1 = root.getChild("head1");
        this.jaw1 = root.getChild("jaw1");
        this.head2 = root.getChild("head2");
        this.jaw2 = root.getChild("jaw2");
        this.fang1 = root.getChild("fang1");
        this.fang2 = root.getChild("fang2");
        this.fang3 = root.getChild("fang3");
        this.fang4 = root.getChild("fang4");
        this.spike2 = root.getChild("spike2");
        this.spike1 = root.getChild("spike1");
        this.arml1 = root.getChild("arml1");
        this.armr1 = root.getChild("armr1");
        this.arml2 = root.getChild("arml2");
        this.armr2 = root.getChild("armr2");
        this.clawr1 = root.getChild("clawr1");
        this.clawr2 = root.getChild("clawr2");
        this.clawr3 = root.getChild("clawr3");
        this.clawl2 = root.getChild("clawl2");
        this.clawl3 = root.getChild("clawl3");
        this.clawl1 = root.getChild("clawl1");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        root.addOrReplaceChild("torso",
                CubeListBuilder.create().texOffs(0, 46)
                        .addBox(-4.5F, -2.0F, 0.0F, 9.0F, 8.0F, 10.0F),
                PartPose.offsetAndRotation(0.0F, -2.5F, -8.0F, -0.1919862F, 0.0F, 0.0F));
        root.addOrReplaceChild("stomach",
                CubeListBuilder.create().texOffs(0, 27)
                        .addBox(-3.5F, -5.0F, 8.0F, 7.0F, 6.0F, 12.0F),
                PartPose.offsetAndRotation(0.0F, -2.5F, -8.0F, -0.5585054F, 0.0F, 0.0F));
        root.addOrReplaceChild("rThigh",
                CubeListBuilder.create().texOffs(59, 45)
                        .addBox(-1.5F, -4.0F, -2.5F, 4.0F, 14.0F, 5.0F),
                PartPose.offsetAndRotation(-4.5F, 7.0F, 8.0F, -0.8028515F, 0.2443461F, 0.418879F));
        root.addOrReplaceChild("lThigh",
                CubeListBuilder.create().texOffs(40, 45)
                        .addBox(-2.5F, -4.0F, -2.5F, 4.0F, 14.0F, 5.0F),
                PartPose.offsetAndRotation(4.5F, 7.0F, 8.0F, -0.8028515F, -0.2443461F, -0.418879F));
        root.addOrReplaceChild("lShin",
                CubeListBuilder.create().texOffs(79, 49)
                        .addBox(-2.0F, 8.0F, -5.5F, 3.0F, 3.0F, 12.0F),
                PartPose.offsetAndRotation(4.5F, 7.0F, 8.0F, -0.4014257F, -0.2443461F, -0.418879F));
        root.addOrReplaceChild("rShin",
                CubeListBuilder.create().texOffs(79, 33)
                        .addBox(-1.0F, 8.0F, -5.5F, 3.0F, 3.0F, 12.0F),
                PartPose.offsetAndRotation(-4.5F, 7.0F, 8.0F, -0.4014257F, 0.2443461F, 0.418879F));
        root.addOrReplaceChild("lShin1",
                CubeListBuilder.create().texOffs(113, 40)
                        .addBox(-1.5F, 5.5F, 9.0F, 2.0F, 9.0F, 2.0F),
                PartPose.offsetAndRotation(4.5F, 7.0F, 8.0F, -0.8028515F, -0.2443461F, -0.418879F));
        root.addOrReplaceChild("rShin1",
                CubeListBuilder.create().texOffs(113, 53)
                        .addBox(-0.5F, 5.5F, 9.0F, 2.0F, 9.0F, 2.0F),
                PartPose.offsetAndRotation(-4.5F, 7.0F, 8.0F, -0.8028515F, 0.2443461F, 0.418879F));
        root.addOrReplaceChild("lFoot",
                CubeListBuilder.create().texOffs(110, 24)
                        .addBox(5.0F, 15.0F, -8.0F, 2.0F, 2.0F, 6.0F),
                PartPose.offsetAndRotation(4.5F, 7.0F, 8.0F, 0.0F, -0.2443461F, 0.0F));
        root.addOrReplaceChild("rFoot",
                CubeListBuilder.create().texOffs(95, 24)
                        .addBox(-7.0F, 15.0F, -8.0F, 2.0F, 2.0F, 6.0F),
                PartPose.offsetAndRotation(-4.5F, 7.0F, 8.0F, 0.0F, 0.2443461F, 0.0F));
        root.addOrReplaceChild("neck",
                CubeListBuilder.create().texOffs(23, 86)
                        .addBox(-2.0F, -2.0F, -4.0F, 4.0F, 6.0F, 5.0F),
                PartPose.offsetAndRotation(0.0F, -2.5F, -8.0F, -0.1919862F, 0.0F, 0.0F));
        root.addOrReplaceChild("fan",
                CubeListBuilder.create().texOffs(149, 10)
                        .addBox(-3.0F, -24.0F, 0.0F, 6.0F, 24.0F, 1.0F),
                PartPose.offset(0.0F, -7.0F, -10.0F));
        root.addOrReplaceChild("tail2",
                CubeListBuilder.create().texOffs(85, 66)
                        .addBox(-2.0F, -1.5F, 0.0F, 4.0F, 4.0F, 11.0F),
                PartPose.offsetAndRotation(0.0F, 9.5F, 20.5F, -0.3141593F, 0.0F, 0.0F));
        root.addOrReplaceChild("tail3",
                CubeListBuilder.create().texOffs(118, 66)
                        .addBox(-1.5F, -1.5F, 0.0F, 3.0F, 3.0F, 11.0F),
                PartPose.offsetAndRotation(0.0F, 13.5F, 30.5F, -0.2094395F, 0.0F, 0.0F));
        root.addOrReplaceChild("tail4",
                CubeListBuilder.create().texOffs(149, 66)
                        .addBox(-1.0F, -1.0F, 0.0F, 2.0F, 2.0F, 11.0F),
                PartPose.offsetAndRotation(0.0F, 15.5F, 40.5F, -0.1396263F, 0.0F, 0.0F));
        root.addOrReplaceChild("tail5",
                CubeListBuilder.create().texOffs(178, 66)
                        .addBox(-0.5F, -0.5F, 0.0F, 1.0F, 1.0F, 11.0F),
                PartPose.offsetAndRotation(0.0F, 17.5F, 50.5F, -0.0523599F, 0.0F, 0.0F));
        root.addOrReplaceChild("tail1",
                CubeListBuilder.create().texOffs(50, 66)
                        .addBox(-2.0F, -2.5F, 0.0F, 4.0F, 4.0F, 11.0F),
                PartPose.offsetAndRotation(0.0F, 6.5F, 10.5F, -0.4014257F, 0.0F, 0.0F));
        root.addOrReplaceChild("fanl1",
                CubeListBuilder.create().texOffs(130, 10)
                        .addBox(-3.0F, -24.0F, 0.0F, 6.0F, 24.0F, 1.0F),
                PartPose.offset(0.0F, -7.0F, -10.0F));
        root.addOrReplaceChild("fanr1",
                CubeListBuilder.create().texOffs(130, 10)
                        .addBox(-3.0F, -24.0F, 0.0F, 6.0F, 24.0F, 1.0F),
                PartPose.offset(0.0F, -7.0F, -10.0F));
        root.addOrReplaceChild("fanl2",
                CubeListBuilder.create().texOffs(130, 10)
                        .addBox(-3.0F, -24.0F, 0.0F, 6.0F, 24.0F, 1.0F),
                PartPose.offset(0.0F, -7.0F, -10.0F));
        root.addOrReplaceChild("fanr2",
                CubeListBuilder.create().texOffs(130, 10)
                        .addBox(-3.0F, -24.0F, 0.0F, 6.0F, 24.0F, 1.0F),
                PartPose.offset(0.0F, -7.0F, -10.0F));
        root.addOrReplaceChild("fanl3",
                CubeListBuilder.create().texOffs(130, 10)
                        .addBox(-3.0F, -24.0F, 0.0F, 6.0F, 24.0F, 1.0F),
                PartPose.offset(0.0F, -7.0F, -10.0F));
        root.addOrReplaceChild("fanr3",
                CubeListBuilder.create().texOffs(130, 10)
                        .addBox(-3.0F, -24.0F, 0.0F, 6.0F, 24.0F, 1.0F),
                PartPose.offset(0.0F, -7.0F, -10.0F));
        root.addOrReplaceChild("fanl4",
                CubeListBuilder.create().texOffs(130, 10)
                        .addBox(-3.0F, -24.0F, 0.0F, 6.0F, 24.0F, 1.0F),
                PartPose.offsetAndRotation(0.0F, -7.0F, -10.0F, 0.0F, 0.0F, 1.047198F));
        root.addOrReplaceChild("fanr4",
                CubeListBuilder.create().texOffs(130, 10)
                        .addBox(-3.0F, -24.0F, 0.0F, 6.0F, 24.0F, 1.0F),
                PartPose.offsetAndRotation(0.0F, -7.0F, -10.0F, 0.0F, 0.0F, -1.047198F));
        root.addOrReplaceChild("fanl5",
                CubeListBuilder.create().texOffs(130, 10)
                        .addBox(-3.0F, -24.0F, 0.0F, 6.0F, 24.0F, 1.0F),
                PartPose.offsetAndRotation(0.0F, -7.0F, -10.0F, 0.0F, 0.0F, 1.308997F));
        root.addOrReplaceChild("fanr5",
                CubeListBuilder.create().texOffs(130, 10)
                        .addBox(-3.0F, -24.0F, 0.0F, 6.0F, 24.0F, 1.0F),
                PartPose.offsetAndRotation(0.0F, -7.0F, -10.0F, 0.0F, 0.0F, -1.308997F));
        root.addOrReplaceChild("fanl6",
                CubeListBuilder.create().texOffs(130, 10)
                        .addBox(-3.0F, -24.0F, 0.0F, 6.0F, 24.0F, 1.0F),
                PartPose.offsetAndRotation(0.0F, -7.0F, -10.0F, 0.0F, 0.0F, 1.570796F));
        root.addOrReplaceChild("fanr6",
                CubeListBuilder.create().texOffs(130, 10)
                        .addBox(-3.0F, -24.0F, 0.0F, 6.0F, 24.0F, 1.0F),
                PartPose.offsetAndRotation(0.0F, -7.0F, -10.0F, 0.0F, 0.0F, -1.570796F));
        root.addOrReplaceChild("spike4",
                CubeListBuilder.create().texOffs(178, 66)
                        .addBox(-0.5F, -0.5F, 0.0F, 1.0F, 1.0F, 11.0F),
                PartPose.offsetAndRotation(0.0F, 16.0F, 41.0F, -0.0523599F, (float) (Math.PI / 6), 0.0F));
        root.addOrReplaceChild("spike5",
                CubeListBuilder.create().texOffs(178, 66)
                        .addBox(-0.5F, -0.5F, 0.0F, 1.0F, 1.0F, 11.0F),
                PartPose.offsetAndRotation(0.0F, 16.0F, 41.0F, -0.0523599F, -0.5759587F, 0.0F));
        root.addOrReplaceChild("spike3",
                CubeListBuilder.create().texOffs(178, 66)
                        .addBox(-0.5F, -0.5F, 0.0F, 1.0F, 1.0F, 11.0F),
                PartPose.offsetAndRotation(0.0F, 13.5F, 30.5F, 0.3141593F, 0.0F, 0.0F));
        root.addOrReplaceChild("fanl7",
                CubeListBuilder.create().texOffs(130, 10)
                        .addBox(-3.0F, -24.0F, 0.0F, 6.0F, 24.0F, 1.0F),
                PartPose.offsetAndRotation(0.0F, -7.0F, -10.0F, 0.0F, 0.0F, 1.832596F));
        root.addOrReplaceChild("fanr7",
                CubeListBuilder.create().texOffs(130, 10)
                        .addBox(-3.0F, -24.0F, 0.0F, 6.0F, 24.0F, 1.0F),
                PartPose.offsetAndRotation(0.0F, -7.0F, -10.0F, 0.0F, 0.0F, -1.832596F));
        root.addOrReplaceChild("head",
                CubeListBuilder.create().texOffs(200, 0)
                        .addBox(-3.0F, -4.0F, -7.0F, 6.0F, 7.0F, 8.0F),
                PartPose.offset(0.0F, -3.0F, -11.0F));
        root.addOrReplaceChild("head1",
                CubeListBuilder.create().texOffs(200, 18)
                        .addBox(-2.5F, -2.0F, -15.0F, 5.0F, 2.0F, 8.0F),
                PartPose.offset(0.0F, -3.0F, -11.0F));
        root.addOrReplaceChild("jaw1",
                CubeListBuilder.create().texOffs(200, 43)
                        .addBox(-2.0F, -1.0F, -7.0F, 4.0F, 2.0F, 8.0F),
                PartPose.offset(0.0F, -2.0F, -19.0F));
        root.addOrReplaceChild("head2",
                CubeListBuilder.create().texOffs(200, 31)
                        .addBox(-2.0F, -2.0F, -22.0F, 4.0F, 2.0F, 7.0F),
                PartPose.offset(0.0F, -3.0F, -11.0F));
        root.addOrReplaceChild("jaw2",
                CubeListBuilder.create().texOffs(200, 56)
                        .addBox(-1.5F, -1.0F, -13.0F, 3.0F, 2.0F, 6.0F),
                PartPose.offset(0.0F, -2.0F, -19.0F));
        root.addOrReplaceChild("fang1",
                CubeListBuilder.create().texOffs(42, 0)
                        .addBox(1.0F, 0.0F, -20.0F, 1.0F, 5.0F, 1.0F),
                PartPose.offset(0.0F, -3.0F, -11.0F));
        root.addOrReplaceChild("fang2",
                CubeListBuilder.create().texOffs(50, 0)
                        .addBox(-2.0F, 0.0F, -20.0F, 1.0F, 5.0F, 1.0F),
                PartPose.offset(0.0F, -3.0F, -11.0F));
        root.addOrReplaceChild("fang3",
                CubeListBuilder.create().texOffs(60, 0)
                        .addBox(1.0F, 0.0F, -14.0F, 1.0F, 3.0F, 1.0F),
                PartPose.offset(0.0F, -3.0F, -11.0F));
        root.addOrReplaceChild("fang4",
                CubeListBuilder.create().texOffs(69, 0)
                        .addBox(-2.0F, 0.0F, -14.0F, 1.0F, 3.0F, 1.0F),
                PartPose.offset(0.0F, -3.0F, -11.0F));
        root.addOrReplaceChild("spike2",
                CubeListBuilder.create().texOffs(178, 66)
                        .addBox(-0.5F, -0.5F, 0.0F, 1.0F, 1.0F, 11.0F),
                PartPose.offsetAndRotation(0.0F, 9.5F, 20.5F, 0.3141593F, 0.0F, 0.0F));
        root.addOrReplaceChild("spike1",
                CubeListBuilder.create().texOffs(178, 66)
                        .addBox(-0.5F, -1.5F, 0.0F, 1.0F, 1.0F, 11.0F),
                PartPose.offsetAndRotation(0.0F, 6.5F, 10.5F, 0.3141593F, 0.0F, 0.0F));
        root.addOrReplaceChild("arml1",
                CubeListBuilder.create().texOffs(50, 98)
                        .addBox(0.0F, 0.0F, -2.0F, 11.0F, 3.0F, 4.0F),
                PartPose.offsetAndRotation(2.0F, -1.0F, -6.0F, 0.0F, (float) (-Math.PI / 6), 0.1745329F));
        root.addOrReplaceChild("armr1",
                CubeListBuilder.create().texOffs(49, 88)
                        .addBox(0.0F, 0.0F, -2.0F, 11.0F, 3.0F, 4.0F),
                PartPose.offsetAndRotation(-3.0F, -1.0F, -6.0F, 0.0F, -2.617994F, -0.1745329F));
        root.addOrReplaceChild("arml2",
                CubeListBuilder.create().texOffs(41, 107)
                        .addBox(0.0F, -1.0F, -1.0F, 15.0F, 3.0F, 3.0F),
                PartPose.offsetAndRotation(11.0F, 2.0F, -1.0F, 0.0F, 0.8552113F, 0.0F));
        root.addOrReplaceChild("armr2",
                CubeListBuilder.create().texOffs(42, 115)
                        .addBox(0.0F, -1.0F, -2.0F, 15.0F, 3.0F, 3.0F),
                PartPose.offsetAndRotation(-11.0F, 2.0F, -1.0F, 0.0F, 2.268928F, 0.0F));
        root.addOrReplaceChild("clawr1",
                CubeListBuilder.create().texOffs(100, 85)
                        .addBox(-0.5F, -1.0F, -6.0F, 1.0F, 1.0F, 6.0F),
                PartPose.offsetAndRotation(-21.0F, 2.0F, -12.0F, -0.1745329F, 0.4363323F, 0.0F));
        root.addOrReplaceChild("clawr2",
                CubeListBuilder.create().texOffs(100, 94)
                        .addBox(0.0F, 0.0F, -10.0F, 1.0F, 1.0F, 10.0F),
                PartPose.offsetAndRotation(-21.0F, 2.0F, -12.0F, 0.0F, 0.8726646F, 0.0F));
        root.addOrReplaceChild("clawr3",
                CubeListBuilder.create().texOffs(100, 107)
                        .addBox(0.0F, 1.0F, -6.0F, 1.0F, 1.0F, 6.0F),
                PartPose.offsetAndRotation(-21.0F, 2.0F, -12.0F, 0.1745329F, 0.4363323F, 0.0F));
        root.addOrReplaceChild("clawl2",
                CubeListBuilder.create().texOffs(130, 94)
                        .addBox(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 10.0F),
                PartPose.offsetAndRotation(21.0F, 2.0F, -12.0F, 0.0F, 2.268928F, 0.0F));
        root.addOrReplaceChild("clawl3",
                CubeListBuilder.create().texOffs(130, 109)
                        .addBox(0.0F, 1.0F, 0.0F, 1.0F, 1.0F, 6.0F),
                PartPose.offsetAndRotation(21.0F, 2.0F, -12.0F, -0.1745329F, 2.70526F, 0.0F));
        root.addOrReplaceChild("clawl1",
                CubeListBuilder.create().texOffs(130, 83)
                        .addBox(0.0F, -1.0F, 0.0F, 1.0F, 1.0F, 6.0F),
                PartPose.offsetAndRotation(21.0F, 2.0F, -12.0F, 0.1745329F, 2.70526F, 0.0F));
        return LayerDefinition.create(mesh, 256, 128);
    }

    @Override
    public ModelPart root() {
        return this.root;
    }

    @Override
    public void setupAnim(Alien entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        animate(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
    }

    
    private void animate(Alien entity, float f, float f1, float f2, float f3, float f4) {
        float newangle = Mth.cos(f2 * 4.0F * this.wingspeed) * (float) Math.PI * 0.5F * f1;
        doLeftLeg(newangle);
        doRightLeg(-newangle);

        if (entity.getAttacking() == 0) {
            this.fan.zRot = this.fanl1.zRot = this.fanl2.zRot = this.fanl3.zRot = this.fanl4.zRot =
                    this.fanl5.zRot = this.fanl6.zRot = this.fanl7.zRot = 0.0F;
            this.fanr1.zRot = this.fanr2.zRot = this.fanr3.zRot = this.fanr4.zRot =
                    this.fanr5.zRot = this.fanr6.zRot = this.fanr7.zRot = 0.0F;
            this.fan.xRot = this.fanl1.xRot = this.fanl2.xRot = this.fanl3.xRot = this.fanl4.xRot =
                    this.fanl5.xRot = this.fanl6.xRot = this.fanl7.xRot = -1.85F;
            this.fanr1.xRot = this.fanr2.xRot = this.fanr3.xRot = this.fanr4.xRot =
                    this.fanr5.xRot = this.fanr6.xRot = this.fanr7.xRot = -1.85F;
        } else {
            float pi6 = (float) (Math.PI / 6);
            float fanspeed = 1.22F;
            float fanamp = 0.1F;
            this.fan.xRot = Mth.cos(f2 * fanspeed * this.wingspeed) * (float) Math.PI * fanamp;
            this.fanl1.xRot = Mth.cos(f2 * fanspeed * this.wingspeed - 1.0F * pi6) * (float) Math.PI * fanamp;
            this.fanl2.xRot = Mth.cos(f2 * fanspeed * this.wingspeed - 2.0F * pi6) * (float) Math.PI * fanamp;
            this.fanl3.xRot = Mth.cos(f2 * fanspeed * this.wingspeed - 3.0F * pi6) * (float) Math.PI * fanamp;
            this.fanl4.xRot = Mth.cos(f2 * fanspeed * this.wingspeed - 4.0F * pi6) * (float) Math.PI * fanamp;
            this.fanl5.xRot = Mth.cos(f2 * fanspeed * this.wingspeed - 5.0F * pi6) * (float) Math.PI * fanamp;
            this.fanl6.xRot = Mth.cos(f2 * fanspeed * this.wingspeed - 6.0F * pi6) * (float) Math.PI * fanamp;
            this.fanl7.xRot = Mth.cos(f2 * fanspeed * this.wingspeed - 7.0F * pi6) * (float) Math.PI * fanamp;
            this.fanr1.xRot = Mth.cos(f2 * fanspeed * this.wingspeed - 1.0F * pi6) * (float) Math.PI * fanamp;
            this.fanr2.xRot = Mth.cos(f2 * fanspeed * this.wingspeed - 2.0F * pi6) * (float) Math.PI * fanamp;
            this.fanr3.xRot = Mth.cos(f2 * fanspeed * this.wingspeed - 3.0F * pi6) * (float) Math.PI * fanamp;
            this.fanr4.xRot = Mth.cos(f2 * fanspeed * this.wingspeed - 4.0F * pi6) * (float) Math.PI * fanamp;
            this.fanr5.xRot = Mth.cos(f2 * fanspeed * this.wingspeed - 5.0F * pi6) * (float) Math.PI * fanamp;
            this.fanr6.xRot = Mth.cos(f2 * fanspeed * this.wingspeed - 6.0F * pi6) * (float) Math.PI * fanamp;
            this.fanr7.xRot = Mth.cos(f2 * fanspeed * this.wingspeed - 7.0F * pi6) * (float) Math.PI * fanamp;
            this.fan.zRot = 0.0F;
            this.fanl1.zRot = 0.261F; this.fanl2.zRot = 0.523F; this.fanl3.zRot = 0.785F;
            this.fanl4.zRot = 1.047F; this.fanl5.zRot = 1.309F; this.fanl6.zRot = 1.571F; this.fanl7.zRot = 1.832F;
            this.fanr1.zRot = -0.261F; this.fanr2.zRot = -0.523F; this.fanr3.zRot = -0.785F;
            this.fanr4.zRot = -1.047F; this.fanr5.zRot = -1.309F; this.fanr6.zRot = -1.571F; this.fanr7.zRot = -1.832F;
        }

        // reset head/neck base pivots
        this.neck.x = 0.0F; this.neck.y = -2.5F; this.neck.z = -8.0F;
        this.head.x = 0.0F; this.head.y = -3.0F; this.head.z = -11.0F;
        this.tail1.x = 0.0F; this.tail1.y = 6.5F; this.tail1.z = 10.5F;
        this.arml1.x = 2.0F; this.arml1.y = -1.0F; this.arml1.z = -6.0F;
        this.armr1.x = -3.0F; this.armr1.y = -1.0F; this.armr1.z = -6.0F;

        this.neck.yRot = (float) Math.toRadians(f3) * 0.35F;
        this.head.yRot = (float) Math.toRadians(f3) * 0.75F;
        this.head.z = this.neck.z - (float) Math.cos(this.neck.yRot) * 3.0F;
        this.head.x = this.neck.x + (float) Math.sin(this.neck.yRot) * 3.0F;
        this.head1.yRot = this.head.yRot; this.head1.z = this.head.z; this.head1.x = this.head.x; this.head1.y = this.head.y;
        this.head2.yRot = this.head.yRot; this.head2.z = this.head.z; this.head2.x = this.head.x; this.head2.y = this.head.y;
        this.fang1.yRot = this.head.yRot; this.fang1.z = this.head.z; this.fang1.x = this.head.x; this.fang1.y = this.head.y;
        this.fang2.yRot = this.head.yRot; this.fang2.z = this.head.z; this.fang2.x = this.head.x; this.fang2.y = this.head.y;
        this.fang3.yRot = this.head.yRot; this.fang3.z = this.head.z; this.fang3.x = this.head.x; this.fang3.y = this.head.y;
        this.fang4.yRot = this.head.yRot; this.fang4.z = this.head.z; this.fang4.x = this.head.x; this.fang4.y = this.head.y;
        this.jaw1.yRot = this.head.yRot;
        this.jaw1.z = this.head.z - (float) Math.cos(this.head.yRot) * 8.0F;
        this.jaw1.x = this.head.x - (float) Math.sin(this.head.yRot) * 8.0F;
        this.jaw1.y = -2.0F;
        this.jaw2.yRot = this.jaw1.yRot; this.jaw2.z = this.jaw1.z; this.jaw2.x = this.jaw1.x; this.jaw2.y = this.jaw1.y;

        RenderInfo r = entity.getRenderInfo();
        newangle = Mth.cos(f2 * 3.5F * this.wingspeed) * (float) Math.PI * 0.5F;
        float nextangle = Mth.cos((f2 + 0.2F) * 3.5F * this.wingspeed) * (float) Math.PI * 0.5F;
        if (nextangle > 0.0F && newangle < 0.0F) {
            if (entity.getAttacking() == 0) {
                r.ri1 = entity.getRandom().nextInt(15);
                r.ri2 = entity.getRandom().nextInt(15);
                r.ri3 = entity.getRandom().nextInt(15);
            } else {
                r.ri1 = entity.getRandom().nextInt(4);
                r.ri2 = entity.getRandom().nextInt(2);
                r.ri3 = 1;
            }
        }
        if (r.ri2 == 1) {
            doTail(newangle);
        } else {
            newangle = Mth.cos(f2 * this.wingspeed) * (float) Math.PI * 0.05F;
            doTail(newangle);
        }
        if (r.ri3 == 1) {
            newangle = Mth.cos(f2 * 3.5F * this.wingspeed) * (float) Math.PI * 0.35F;
            doJaw(newangle);
        } else {
            newangle = Mth.cos(f2 * this.wingspeed) * (float) Math.PI * 0.02F;
            doJaw(newangle);
        }
        newangle = Mth.cos(f2 * this.wingspeed * 3.5F) * (float) Math.PI * 0.2F;
        if (r.ri1 != 1 && r.ri1 != 3) {
            float slow = Mth.cos(f2 * this.wingspeed) * (float) Math.PI * 0.03F;
            doLeftClaw(slow);
        } else {
            doLeftClaw(newangle);
        }
        if (r.ri1 != 2 && r.ri1 != 3) {
            float slow = Mth.cos(f2 * this.wingspeed) * (float) Math.PI * 0.03F;
            doRightClaw(-slow);
        } else {
            doRightClaw(-newangle);
        }
        entity.setRenderInfo(r);
    }

    private void doLeftLeg(float angle) {
        this.lFoot.xRot = angle;
        this.lShin.xRot = angle - 0.4F;
        this.lShin1.xRot = angle - 0.8F;
        this.lThigh.xRot = angle - 0.8F;
    }

    private void doRightLeg(float angle) {
        this.rFoot.xRot = angle;
        this.rShin.xRot = angle - 0.4F;
        this.rShin1.xRot = angle - 0.8F;
        this.rThigh.xRot = angle - 0.8F;
    }

    private void doJaw(float angle) {
        this.jaw1.xRot = Math.abs(angle);
        this.jaw2.xRot = this.jaw1.xRot;
    }

    private void doTail(float angle) {
        this.tail1.yRot = angle * 0.25F;
        this.spike1.yRot = this.tail1.yRot;
        this.spike1.x = this.tail1.x; this.spike1.y = this.tail1.y; this.spike1.z = this.tail1.z;
        this.tail2.yRot = angle * 0.5F;
        this.tail2.z = this.tail1.z + (float) Math.cos(this.tail1.yRot) * 10.0F;
        this.tail2.x = this.tail1.x + (float) Math.sin(this.tail1.yRot) * 10.0F;
        this.tail2.y = 9.5F;
        this.spike2.yRot = this.tail2.yRot;
        this.spike2.z = this.tail2.z; this.spike2.x = this.tail2.x; this.spike2.y = this.tail2.y;
        this.tail3.yRot = angle * 0.8F;
        this.tail3.z = this.tail2.z + (float) Math.cos(this.tail2.yRot) * 10.0F;
        this.tail3.x = this.tail2.x + (float) Math.sin(this.tail2.yRot) * 10.0F;
        this.tail3.y = 13.5F;
        this.spike3.yRot = this.tail3.yRot;
        this.spike3.z = this.tail3.z; this.spike3.x = this.tail3.x; this.spike3.y = this.tail3.y;
        this.tail4.yRot = angle * 1.25F;
        this.tail4.z = this.tail3.z + (float) Math.cos(this.tail3.yRot) * 10.0F;
        this.tail4.x = this.tail3.x + (float) Math.sin(this.tail3.yRot) * 10.0F;
        this.tail4.y = 15.5F;
        this.spike4.yRot = this.tail4.yRot + 0.52F;
        this.spike4.z = this.tail4.z; this.spike4.x = this.tail4.x; this.spike4.y = 16.0F;
        this.spike5.yRot = this.tail4.yRot - 0.52F;
        this.spike5.z = this.tail4.z; this.spike5.x = this.tail4.x; this.spike5.y = 16.0F;
        this.tail5.yRot = angle * 1.5F;
        this.tail5.z = this.tail4.z + (float) Math.cos(this.tail4.yRot) * 10.0F;
        this.tail5.x = this.tail4.x + (float) Math.sin(this.tail4.yRot) * 10.0F;
        this.tail5.y = 17.5F;
    }

    private void doLeftClaw(float angle) {
        this.arml1.yRot = -0.52F + Math.abs(angle * 2.0F);
        this.arml2.z = this.arml1.z - (float) Math.sin(this.arml1.yRot) * 9.0F;
        this.arml2.x = this.arml1.x + (float) Math.cos(this.arml1.yRot) * 9.0F;
        this.arml2.y = 2.0F;
        this.arml2.yRot = 0.855F + Math.abs(angle);
        this.clawl1.z = this.arml2.z - (float) Math.sin(this.arml2.yRot) * 14.0F;
        this.clawl1.x = this.arml2.x + (float) Math.cos(this.arml2.yRot) * 14.0F;
        this.clawl1.y = 2.0F;
        this.clawl1.yRot = 2.7F + Math.abs(angle * 4.0F);
        this.clawl2.z = this.clawl1.z; this.clawl2.x = this.clawl1.x; this.clawl2.y = this.clawl1.y;
        this.clawl2.yRot = 2.27F + Math.abs(angle * 4.0F);
        this.clawl3.z = this.clawl1.z; this.clawl3.x = this.clawl1.x; this.clawl3.y = this.clawl1.y;
        this.clawl3.yRot = 2.7F + Math.abs(angle * 4.0F);
    }

    private void doRightClaw(float angle) {
        this.armr1.yRot = -2.61F - Math.abs(angle * 2.0F);
        this.armr2.z = this.armr1.z - (float) Math.sin(this.armr1.yRot) * 9.0F;
        this.armr2.x = this.armr1.x + (float) Math.cos(this.armr1.yRot) * 9.0F;
        this.armr2.y = 2.0F;
        this.armr2.yRot = 2.27F - Math.abs(angle);
        this.clawr1.z = this.armr2.z - (float) Math.sin(this.armr2.yRot) * 14.0F;
        this.clawr1.x = this.armr2.x + (float) Math.cos(this.armr2.yRot) * 14.0F;
        this.clawr1.y = 2.0F;
        this.clawr1.yRot = 0.436F - Math.abs(angle * 4.0F);
        this.clawr2.z = this.clawr1.z; this.clawr2.x = this.clawr1.x; this.clawr2.y = this.clawr1.y;
        this.clawr2.yRot = 0.87F - Math.abs(angle * 4.0F);
        this.clawr3.z = this.clawr1.z; this.clawr3.x = this.clawr1.x; this.clawr3.y = this.clawr1.y;
        this.clawr3.yRot = 0.436F - Math.abs(angle * 4.0F);
    }
}

