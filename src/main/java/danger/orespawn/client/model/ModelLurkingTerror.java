package danger.orespawn.client.model;

import danger.orespawn.entity.LurkingTerror;
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
 * Port of gold {@code ModelLurkingTerror} (1.7.10 ModelBase, tex 256×64) → 1.21 HierarchicalModel.
 * Cube sizes/UVs/offsets 1:1. Animation from gold {@code render()}.
 */
@OnlyIn(Dist.CLIENT)
public class ModelLurkingTerror extends HierarchicalModel<LurkingTerror> {
    private final float wingspeed;
    private final ModelPart root;
    private final ModelPart body;
    private final ModelPart leg1;
    private final ModelPart leg1part2;
    private final ModelPart leg1part3;
    private final ModelPart leg2;
    private final ModelPart leg2part2;
    private final ModelPart leg2part3;
    private final ModelPart leg3;
    private final ModelPart leg3part2;
    private final ModelPart leg3part3;
    private final ModelPart leg4;
    private final ModelPart leg4part2;
    private final ModelPart leg4part3;
    private final ModelPart leg5;
    private final ModelPart leg5part2;
    private final ModelPart leg6;
    private final ModelPart leg6part2;
    private final ModelPart thorax;
    private final ModelPart abdomen;
    private final ModelPart head;
    private final ModelPart jaw1;
    private final ModelPart jaw1part2;
    private final ModelPart jaw1tooth1;
    private final ModelPart jaw1tooth2;
    private final ModelPart jaw1tooth3;
    private final ModelPart jaw1tooth4;
    private final ModelPart jaw1tooth5;
    private final ModelPart jaw1tooth6;
    private final ModelPart jaw2;
    private final ModelPart jaw2part2;
    private final ModelPart jaw2tooth1;
    private final ModelPart jaw2tooth2;
    private final ModelPart jaw2tooth3;
    private final ModelPart jaw2tooth4;
    private final ModelPart jaw2tooth5;
    private final ModelPart jaw2tooth6;
    private final ModelPart jaw3;
    private final ModelPart jaw3part2;
    private final ModelPart jaw3tooth1;
    private final ModelPart jaw3tooth2;
    private final ModelPart jaw3tooth3;
    private final ModelPart jaw3tooth4;
    private final ModelPart jaw3tooth5;
    private final ModelPart jaw3tooth6;
    private final ModelPart jaw4;
    private final ModelPart jaw4part2;
    private final ModelPart jaw4tooth1;
    private final ModelPart jaw4tooth2;
    private final ModelPart jaw4tooth3;
    private final ModelPart jaw4tooth4;
    private final ModelPart jaw4tooth5;
    private final ModelPart jaw4tooth6;
    private final ModelPart tonguepart1;
    private final ModelPart tonguepart2;
    private final ModelPart tonguepart3;
    private final ModelPart wing_1;
    private final ModelPart wing_2;
    private final ModelPart wing_3;
    private final ModelPart wing_4;

    public ModelLurkingTerror(ModelPart root) {
        this(root, 1.0F);
    }

    public ModelLurkingTerror(ModelPart root, float wingspeed) {
        this.root = root;
        this.wingspeed = wingspeed;
        this.body = root.getChild("body");
        this.leg1 = root.getChild("leg1");
        this.leg1part2 = root.getChild("leg1part2");
        this.leg1part3 = root.getChild("leg1part3");
        this.leg2 = root.getChild("leg2");
        this.leg2part2 = root.getChild("leg2part2");
        this.leg2part3 = root.getChild("leg2part3");
        this.leg3 = root.getChild("leg3");
        this.leg3part2 = root.getChild("leg3part2");
        this.leg3part3 = root.getChild("leg3part3");
        this.leg4 = root.getChild("leg4");
        this.leg4part2 = root.getChild("leg4part2");
        this.leg4part3 = root.getChild("leg4part3");
        this.leg5 = root.getChild("leg5");
        this.leg5part2 = root.getChild("leg5part2");
        this.leg6 = root.getChild("leg6");
        this.leg6part2 = root.getChild("leg6part2");
        this.thorax = root.getChild("thorax");
        this.abdomen = root.getChild("abdomen");
        this.head = root.getChild("head");
        this.jaw1 = root.getChild("jaw1");
        this.jaw1part2 = root.getChild("jaw1part2");
        this.jaw1tooth1 = root.getChild("jaw1tooth1");
        this.jaw1tooth2 = root.getChild("jaw1tooth2");
        this.jaw1tooth3 = root.getChild("jaw1tooth3");
        this.jaw1tooth4 = root.getChild("jaw1tooth4");
        this.jaw1tooth5 = root.getChild("jaw1tooth5");
        this.jaw1tooth6 = root.getChild("jaw1tooth6");
        this.jaw2 = root.getChild("jaw2");
        this.jaw2part2 = root.getChild("jaw2part2");
        this.jaw2tooth1 = root.getChild("jaw2tooth1");
        this.jaw2tooth2 = root.getChild("jaw2tooth2");
        this.jaw2tooth3 = root.getChild("jaw2tooth3");
        this.jaw2tooth4 = root.getChild("jaw2tooth4");
        this.jaw2tooth5 = root.getChild("jaw2tooth5");
        this.jaw2tooth6 = root.getChild("jaw2tooth6");
        this.jaw3 = root.getChild("jaw3");
        this.jaw3part2 = root.getChild("jaw3part2");
        this.jaw3tooth1 = root.getChild("jaw3tooth1");
        this.jaw3tooth2 = root.getChild("jaw3tooth2");
        this.jaw3tooth3 = root.getChild("jaw3tooth3");
        this.jaw3tooth4 = root.getChild("jaw3tooth4");
        this.jaw3tooth5 = root.getChild("jaw3tooth5");
        this.jaw3tooth6 = root.getChild("jaw3tooth6");
        this.jaw4 = root.getChild("jaw4");
        this.jaw4part2 = root.getChild("jaw4part2");
        this.jaw4tooth1 = root.getChild("jaw4tooth1");
        this.jaw4tooth2 = root.getChild("jaw4tooth2");
        this.jaw4tooth3 = root.getChild("jaw4tooth3");
        this.jaw4tooth4 = root.getChild("jaw4tooth4");
        this.jaw4tooth5 = root.getChild("jaw4tooth5");
        this.jaw4tooth6 = root.getChild("jaw4tooth6");
        this.tonguepart1 = root.getChild("tonguepart1");
        this.tonguepart2 = root.getChild("tonguepart2");
        this.tonguepart3 = root.getChild("tonguepart3");
        this.wing_1 = root.getChild("wing_1");
        this.wing_2 = root.getChild("wing_2");
        this.wing_3 = root.getChild("wing_3");
        this.wing_4 = root.getChild("wing_4");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        root.addOrReplaceChild("body",
                CubeListBuilder.create().texOffs(39, 27)
                        .addBox(-4.0F, -4.0F, -8.0F, 8.0F, 8.0F, 12.0F),
                PartPose.offset(0.0F, 10.0F, 0.0F));
        root.addOrReplaceChild("leg1",
                CubeListBuilder.create().texOffs(18, 0)
                        .addBox(-15.0F, -1.5F, -1.5F, 16.0F, 3.0F, 3.0F),
                PartPose.offsetAndRotation(-4.0F, 10.0F, -1.0F, 0.0F, -0.5759587F, -0.1919862F));
        root.addOrReplaceChild("leg1part2",
                CubeListBuilder.create().texOffs(58, 0)
                        .addBox(-15.0F, -1.5F, -1.5F, 3.0F, 8.0F, 3.0F),
                PartPose.offsetAndRotation(-4.0F, 10.0F, -1.0F, 0.0F, -0.5759587F, -0.1919862F));
        root.addOrReplaceChild("leg1part3",
                CubeListBuilder.create().texOffs(0, 0)
                        .addBox(-15.0F, -1.0F, -1.0F, 2.0F, 8.0F, 2.0F),
                PartPose.offsetAndRotation(-4.0F, 10.0F, -1.0F, 0.0F, -0.5759587F, -0.6753082F));
        root.addOrReplaceChild("leg2",
                CubeListBuilder.create().texOffs(18, 0)
                        .addBox(-1.0F, -1.5F, -1.5F, 16.0F, 3.0F, 3.0F),
                PartPose.offsetAndRotation(4.0F, 10.0F, -1.0F, 0.0F, 0.5759587F, 0.1919862F));
        root.addOrReplaceChild("leg2part2",
                CubeListBuilder.create().texOffs(58, 0)
                        .addBox(12.0F, -1.5F, -1.5F, 3.0F, 8.0F, 3.0F),
                PartPose.offsetAndRotation(4.0F, 10.0F, -1.0F, 0.0F, 0.5759587F, 0.1919862F));
        root.addOrReplaceChild("leg2part3",
                CubeListBuilder.create().texOffs(0, 0)
                        .addBox(13.0F, -1.0F, -1.0F, 2.0F, 8.0F, 2.0F),
                PartPose.offsetAndRotation(4.0F, 10.0F, -1.0F, 0.0F, 0.5759587F, 0.6753028F));
        root.addOrReplaceChild("leg3",
                CubeListBuilder.create().texOffs(18, 0)
                        .addBox(-15.0F, -1.5F, -1.5F, 16.0F, 3.0F, 3.0F),
                PartPose.offsetAndRotation(-4.0F, 10.0F, 1.0F, 0.0F, 0.2792527F, -0.1919862F));
        root.addOrReplaceChild("leg3part2",
                CubeListBuilder.create().texOffs(58, 0)
                        .addBox(-15.0F, -1.5F, -1.5F, 3.0F, 8.0F, 3.0F),
                PartPose.offsetAndRotation(-4.0F, 10.0F, 1.0F, 0.0F, 0.2792527F, -0.1919862F));
        root.addOrReplaceChild("leg3part3",
                CubeListBuilder.create().texOffs(0, 0)
                        .addBox(-15.0F, -1.0F, -1.0F, 2.0F, 8.0F, 2.0F),
                PartPose.offsetAndRotation(-4.0F, 10.0F, 1.0F, 0.0F, 0.2792527F, -0.6753028F));
        root.addOrReplaceChild("leg4",
                CubeListBuilder.create().texOffs(18, 0)
                        .addBox(-1.0F, -1.5F, -1.5F, 16.0F, 3.0F, 3.0F),
                PartPose.offsetAndRotation(4.0F, 10.0F, 1.0F, 0.0F, -0.2792527F, 0.1919862F));
        root.addOrReplaceChild("leg4part2",
                CubeListBuilder.create().texOffs(58, 0)
                        .addBox(12.0F, -1.5F, -1.5F, 3.0F, 8.0F, 3.0F),
                PartPose.offsetAndRotation(4.0F, 10.0F, 1.0F, 0.0F, -0.2792527F, 0.1919862F));
        root.addOrReplaceChild("leg4part3",
                CubeListBuilder.create().texOffs(0, 0)
                        .addBox(13.0F, -1.0F, -1.0F, 2.0F, 8.0F, 2.0F),
                PartPose.offsetAndRotation(4.0F, 10.0F, 1.0F, 0.0F, -0.2792527F, 0.6753028F));
        root.addOrReplaceChild("leg5",
                CubeListBuilder.create().texOffs(119, 0)
                        .addBox(-4.0F, -1.5F, -1.5F, 25.0F, 3.0F, 3.0F),
                PartPose.offsetAndRotation(4.0F, 10.0F, 4.0F, 0.0F, -1.134359F, 0.3407057F));
        root.addOrReplaceChild("leg5part2",
                CubeListBuilder.create().texOffs(18, 9)
                        .addBox(18.0F, -1.5F, -1.5F, 3.0F, 10.0F, 3.0F),
                PartPose.offsetAndRotation(4.0F, 10.0F, 4.0F, 0.0F, -1.134359F, 0.3407057F));
        root.addOrReplaceChild("leg6",
                CubeListBuilder.create().texOffs(119, 0)
                        .addBox(-21.0F, -1.5F, -1.5F, 25.0F, 3.0F, 3.0F),
                PartPose.offsetAndRotation(-4.0F, 10.0F, 4.0F, 0.0F, 1.134359F, -0.3407057F));
        root.addOrReplaceChild("leg6part2",
                CubeListBuilder.create().texOffs(18, 9)
                        .addBox(-21.0F, -1.5F, -1.5F, 3.0F, 10.0F, 3.0F),
                PartPose.offsetAndRotation(-4.0F, 10.0F, 4.0F, 0.0F, 1.134359F, -0.3407057F));
        root.addOrReplaceChild("thorax",
                CubeListBuilder.create().texOffs(0, 42)
                        .addBox(-2.0F, -2.0F, -6.0F, 4.0F, 4.0F, 18.0F),
                PartPose.offsetAndRotation(0.0F, 10.0F, 9.0F, -0.2602503F, 0.0F, 0.0F));
        root.addOrReplaceChild("abdomen",
                CubeListBuilder.create().texOffs(118, 18)
                        .addBox(-3.0F, -3.0F, 0.0F, 6.0F, 6.0F, 16.0F),
                PartPose.offset(0.0F, 13.0F, 20.0F));
        root.addOrReplaceChild("head",
                CubeListBuilder.create().texOffs(27, 48)
                        .addBox(-3.0F, -3.0F, -3.0F, 6.0F, 6.0F, 5.0F),
                PartPose.offset(0.0F, 10.0F, -8.0F));
        root.addOrReplaceChild("jaw1",
                CubeListBuilder.create().texOffs(96, 31)
                        .addBox(-1.0F, -1.0F, -13.0F, 1.0F, 2.0F, 14.0F),
                PartPose.offsetAndRotation(-2.0F, 10.0F, -8.0F, 0.0F, 0.4089647F, 0.0F));
        root.addOrReplaceChild("jaw1part2",
                CubeListBuilder.create().texOffs(39, 17)
                        .addBox(-1.1F, -2.0F, -5.0F, 1.0F, 4.0F, 5.0F),
                PartPose.offsetAndRotation(-2.0F, 10.0F, -8.0F, 0.0F, 0.4089647F, 0.0F));
        root.addOrReplaceChild("jaw1tooth1",
                CubeListBuilder.create().texOffs(39, 27)
                        .addBox(0.0F, -0.5F, -13.0F, 1.0F, 1.0F, 1.0F),
                PartPose.offsetAndRotation(-2.0F, 10.0F, -8.0F, 0.0F, 0.4089647F, 0.0F));
        root.addOrReplaceChild("jaw1tooth2",
                CubeListBuilder.create().texOffs(39, 27)
                        .addBox(0.0F, -0.5F, -11.0F, 1.0F, 1.0F, 1.0F),
                PartPose.offsetAndRotation(-2.0F, 10.0F, -8.0F, 0.0F, 0.4089647F, 0.0F));
        root.addOrReplaceChild("jaw1tooth3",
                CubeListBuilder.create().texOffs(39, 27)
                        .addBox(0.0F, -0.5F, -9.0F, 1.0F, 1.0F, 1.0F),
                PartPose.offsetAndRotation(-2.0F, 10.0F, -8.0F, 0.0F, 0.4089647F, 0.0F));
        root.addOrReplaceChild("jaw1tooth4",
                CubeListBuilder.create().texOffs(39, 27)
                        .addBox(0.0F, -0.5F, -7.0F, 1.0F, 1.0F, 1.0F),
                PartPose.offsetAndRotation(-2.0F, 10.0F, -8.0F, 0.0F, 0.4089647F, 0.0F));
        root.addOrReplaceChild("jaw1tooth5",
                CubeListBuilder.create().texOffs(39, 27)
                        .addBox(0.0F, -1.5F, -4.5F, 1.0F, 1.0F, 1.0F),
                PartPose.offsetAndRotation(-2.0F, 10.0F, -8.0F, 0.0F, 0.4089647F, 0.0F));
        root.addOrReplaceChild("jaw1tooth6",
                CubeListBuilder.create().texOffs(39, 27)
                        .addBox(0.0F, 0.5F, -4.5F, 1.0F, 1.0F, 1.0F),
                PartPose.offsetAndRotation(-2.0F, 10.0F, -8.0F, 0.0F, 0.4089647F, 0.0F));
        root.addOrReplaceChild("jaw2",
                CubeListBuilder.create().texOffs(96, 48)
                        .addBox(0.0F, -1.0F, -13.0F, 1.0F, 2.0F, 14.0F),
                PartPose.offsetAndRotation(2.0F, 10.0F, -8.0F, 0.0F, -0.4089656F, 0.0F));
        root.addOrReplaceChild("jaw2part2",
                CubeListBuilder.create().texOffs(39, 7)
                        .addBox(0.1F, -2.0F, -5.0F, 1.0F, 4.0F, 5.0F),
                PartPose.offsetAndRotation(2.0F, 10.0F, -8.0F, 0.0F, -0.4089656F, 0.0F));
        root.addOrReplaceChild("jaw2tooth1",
                CubeListBuilder.create().texOffs(96, 48)
                        .addBox(-1.0F, -0.5F, -13.0F, 1.0F, 1.0F, 1.0F),
                PartPose.offsetAndRotation(2.0F, 10.0F, -8.0F, 0.0F, -0.4089656F, 0.0F));
        root.addOrReplaceChild("jaw2tooth2",
                CubeListBuilder.create().texOffs(96, 48)
                        .addBox(-1.0F, -0.5F, -11.0F, 1.0F, 1.0F, 1.0F),
                PartPose.offsetAndRotation(2.0F, 10.0F, -8.0F, 0.0F, -0.4089656F, 0.0F));
        root.addOrReplaceChild("jaw2tooth3",
                CubeListBuilder.create().texOffs(96, 48)
                        .addBox(-1.0F, -0.5F, -9.0F, 1.0F, 1.0F, 1.0F),
                PartPose.offsetAndRotation(2.0F, 10.0F, -8.0F, 0.0F, -0.4089656F, 0.0F));
        root.addOrReplaceChild("jaw2tooth4",
                CubeListBuilder.create().texOffs(96, 48)
                        .addBox(-1.0F, -0.5F, -7.0F, 1.0F, 1.0F, 1.0F),
                PartPose.offsetAndRotation(2.0F, 10.0F, -8.0F, 0.0F, -0.4089656F, 0.0F));
        root.addOrReplaceChild("jaw2tooth5",
                CubeListBuilder.create().texOffs(96, 48)
                        .addBox(-1.0F, -1.5F, -4.5F, 1.0F, 1.0F, 1.0F),
                PartPose.offsetAndRotation(2.0F, 10.0F, -8.0F, 0.0F, -0.4089656F, 0.0F));
        root.addOrReplaceChild("jaw2tooth6",
                CubeListBuilder.create().texOffs(96, 48)
                        .addBox(-1.0F, 0.5F, -4.5F, 1.0F, 1.0F, 1.0F),
                PartPose.offsetAndRotation(2.0F, 10.0F, -8.0F, 0.0F, -0.4089656F, 0.0F));
        root.addOrReplaceChild("jaw3",
                CubeListBuilder.create().texOffs(95, 16)
                        .addBox(-1.0F, -1.0F, -13.0F, 2.0F, 1.0F, 14.0F),
                PartPose.offsetAndRotation(0.0F, 8.0F, -8.0F, -0.4089647F, 0.0F, 0.0F));
        root.addOrReplaceChild("jaw3part2",
                CubeListBuilder.create().texOffs(0, 27)
                        .addBox(-2.0F, -1.0F, -5.0F, 4.0F, 1.0F, 5.0F),
                PartPose.offsetAndRotation(0.0F, 7.9F, -8.0F, -0.4089647F, 0.0F, 0.0F));
        root.addOrReplaceChild("jaw3tooth1",
                CubeListBuilder.create().texOffs(95, 16)
                        .addBox(-0.5F, 0.0F, -13.0F, 1.0F, 1.0F, 1.0F),
                PartPose.offsetAndRotation(0.0F, 8.0F, -8.0F, -0.4089647F, 0.0F, 0.0F));
        root.addOrReplaceChild("jaw3tooth2",
                CubeListBuilder.create().texOffs(95, 16)
                        .addBox(-0.5F, 0.0F, -11.0F, 1.0F, 1.0F, 1.0F),
                PartPose.offsetAndRotation(0.0F, 8.0F, -8.0F, -0.4089647F, 0.0F, 0.0F));
        root.addOrReplaceChild("jaw3tooth3",
                CubeListBuilder.create().texOffs(95, 16)
                        .addBox(-0.5F, 0.0F, -9.0F, 1.0F, 1.0F, 1.0F),
                PartPose.offsetAndRotation(0.0F, 8.0F, -8.0F, -0.4089647F, 0.0F, 0.0F));
        root.addOrReplaceChild("jaw3tooth4",
                CubeListBuilder.create().texOffs(95, 16)
                        .addBox(-0.5F, 0.0F, -7.0F, 1.0F, 1.0F, 1.0F),
                PartPose.offsetAndRotation(0.0F, 8.0F, -8.0F, -0.4089647F, 0.0F, 0.0F));
        root.addOrReplaceChild("jaw3tooth5",
                CubeListBuilder.create().texOffs(95, 16)
                        .addBox(-1.5F, 0.0F, -4.5F, 1.0F, 1.0F, 1.0F),
                PartPose.offsetAndRotation(0.0F, 8.0F, -8.0F, -0.4089647F, 0.0F, 0.0F));
        root.addOrReplaceChild("jaw3tooth6",
                CubeListBuilder.create().texOffs(95, 16)
                        .addBox(0.5F, 0.0F, -4.5F, 1.0F, 1.0F, 1.0F),
                PartPose.offsetAndRotation(0.0F, 8.0F, -8.0F, -0.4089647F, 0.0F, 0.0F));
        root.addOrReplaceChild("jaw4",
                CubeListBuilder.create().texOffs(95, 0)
                        .addBox(-1.0F, 0.0F, -13.0F, 2.0F, 1.0F, 14.0F),
                PartPose.offsetAndRotation(0.0F, 12.0F, -8.0F, 0.4089656F, 0.0F, 0.0F));
        root.addOrReplaceChild("jaw4part2",
                CubeListBuilder.create().texOffs(0, 20)
                        .addBox(-2.0F, 0.0F, -5.0F, 4.0F, 1.0F, 5.0F),
                PartPose.offsetAndRotation(0.0F, 12.1F, -8.0F, 0.4089656F, 0.0F, 0.0F));
        root.addOrReplaceChild("jaw4tooth1",
                CubeListBuilder.create().texOffs(95, 0)
                        .addBox(-0.5F, -1.0F, -13.0F, 1.0F, 1.0F, 1.0F),
                PartPose.offsetAndRotation(0.0F, 12.0F, -8.0F, 0.4089656F, 0.0F, 0.0F));
        root.addOrReplaceChild("jaw4tooth2",
                CubeListBuilder.create().texOffs(95, 0)
                        .addBox(-0.5F, -1.0F, -11.0F, 1.0F, 1.0F, 1.0F),
                PartPose.offsetAndRotation(0.0F, 12.0F, -8.0F, 0.4089656F, 0.0F, 0.0F));
        root.addOrReplaceChild("jaw4tooth3",
                CubeListBuilder.create().texOffs(95, 0)
                        .addBox(-0.5F, -1.0F, -9.0F, 1.0F, 1.0F, 1.0F),
                PartPose.offsetAndRotation(0.0F, 12.0F, -8.0F, 0.4089656F, 0.0F, 0.0F));
        root.addOrReplaceChild("jaw4tooth4",
                CubeListBuilder.create().texOffs(95, 0)
                        .addBox(-0.5F, -1.0F, -7.0F, 1.0F, 1.0F, 1.0F),
                PartPose.offsetAndRotation(0.0F, 12.0F, -8.0F, 0.4089656F, 0.0F, 0.0F));
        root.addOrReplaceChild("jaw4tooth5",
                CubeListBuilder.create().texOffs(95, 0)
                        .addBox(-1.5F, -1.0F, -4.5F, 1.0F, 1.0F, 1.0F),
                PartPose.offsetAndRotation(0.0F, 12.0F, -8.0F, 0.4089656F, 0.0F, 0.0F));
        root.addOrReplaceChild("jaw4tooth6",
                CubeListBuilder.create().texOffs(95, 0)
                        .addBox(0.5F, -1.0F, -4.5F, 1.0F, 1.0F, 1.0F),
                PartPose.offsetAndRotation(0.0F, 12.0F, -8.0F, 0.4089656F, 0.0F, 0.0F));
        root.addOrReplaceChild("tonguepart1",
                CubeListBuilder.create().texOffs(24, 34)
                        .addBox(-0.5F, -0.5F, -5.0F, 1.0F, 1.0F, 5.0F),
                PartPose.offsetAndRotation(1.6F, 9.3F, -15.0F, 1.041001F, 1.264073F, -1.07818F));
        root.addOrReplaceChild("tonguepart2",
                CubeListBuilder.create().texOffs(0, 46)
                        .addBox(-0.5F, -0.5F, -5.0F, 1.0F, 1.0F, 5.0F),
                PartPose.offsetAndRotation(0.0F, 10.0F, -11.0F, -0.1858931F, -0.2230717F, 0.669215F));
        root.addOrReplaceChild("tonguepart3",
                CubeListBuilder.create().texOffs(24, 27)
                        .addBox(-0.5F, -0.5F, -5.0F, 1.0F, 1.0F, 5.0F),
                PartPose.offsetAndRotation(0.2F, 11.3F, -19.0F, -0.2602503F, 0.3717861F, -1.07818F));
        root.addOrReplaceChild("wing_1",
                CubeListBuilder.create().texOffs(108, 42)
                        .addBox(-4.0F, 0.0F, 0.0F, 8.0F, 0.0F, 22.0F),
                PartPose.offsetAndRotation(-2.0F, 6.0F, -5.0F, 0.5948578F, -0.9294653F, 0.0F));
        root.addOrReplaceChild("wing_2",
                CubeListBuilder.create().texOffs(141, 42)
                        .addBox(-4.0F, 0.0F, 0.0F, 8.0F, 0.0F, 22.0F),
                PartPose.offsetAndRotation(2.0F, 6.0F, -5.0F, 0.5948606F, 0.9294576F, 0.0F));
        root.addOrReplaceChild("wing_3",
                CubeListBuilder.create().texOffs(64, 27)
                        .addBox(-2.0F, 0.0F, 0.0F, 4.0F, 0.0F, 18.0F),
                PartPose.offsetAndRotation(-2.0F, 6.0F, -1.0F, 0.3346075F, -0.4089647F, 0.0F));
        root.addOrReplaceChild("wing_4",
                CubeListBuilder.create().texOffs(153, 17)
                        .addBox(-2.0F, 0.0F, 0.0F, 4.0F, 0.0F, 18.0F),
                PartPose.offsetAndRotation(2.0F, 6.0F, -1.0F, 0.3346075F, 0.4089656F, 0.0F));

        return LayerDefinition.create(mesh, 256, 64);
    }

    @Override
    public ModelPart root() {
        return this.root;
    }

    @Override
    public void setupAnim(
            LurkingTerror entity,
            float limbSwing,
            float limbSwingAmount,
            float ageInTicks,
            float netHeadYaw,
            float headPitch) {
        // gold render(): leg/mouth phase via RenderInfo; f2 = ageInTicks
        float f2 = ageInTicks;
        float newangle = 0.0F;
        float legspeed = 0.7F;
        float mouthspeed = 0.9F;
        RenderInfo r = entity.getRenderInfo();

        newangle = f2 * legspeed * this.wingspeed % (float) (Math.PI * 2);
        newangle = Math.abs(newangle);
        if (newangle < r.rf1) {
            r.ri1 = 0;
            if (entity.getRandom().nextInt(3) == 1) {
                r.ri1 |= 1;
            }
            if (entity.getRandom().nextInt(3) == 1) {
                r.ri1 |= 2;
            }
            if (entity.getRandom().nextInt(4) == 1) {
                r.ri1 |= 4;
            }
            if (entity.getRandom().nextInt(4) == 1) {
                r.ri1 |= 8;
            }
            if (entity.getRandom().nextInt(6) == 1) {
                r.ri1 |= 16;
            }
            if (entity.getRandom().nextInt(6) == 1) {
                r.ri1 |= 32;
            }
        }
        r.rf1 = newangle;

        newangle = f2 * mouthspeed * this.wingspeed % (float) (Math.PI * 2);
        newangle = Math.abs(newangle);
        if (newangle < r.rf2) {
            r.ri2 = 0;
            if (entity.getRandom().nextInt(20) == 1) {
                r.ri2 |= 1;
            }
            if (entity.getAttacking() != 0) {
                r.ri2 = 1;
            }
        }
        r.rf2 = newangle;

        newangle = 0.0F;
        if ((r.ri1 & 1) != 0) {
            newangle = Mth.sin(f2 * legspeed * this.wingspeed) * (float) Math.PI * 0.25F;
        }
        this.leg2.zRot = this.leg2part2.zRot = 0.191F + newangle;
        this.leg2part3.zRot = 0.675F + newangle;

        newangle = 0.0F;
        if ((r.ri1 & 2) != 0) {
            newangle = Mth.sin(f2 * legspeed * this.wingspeed) * (float) Math.PI * 0.25F;
        }
        this.leg1.zRot = this.leg1part2.zRot = -0.191F + newangle;
        this.leg1part3.zRot = -0.675F + newangle;

        newangle = 0.0F;
        if ((r.ri1 & 4) != 0) {
            newangle = Mth.sin(f2 * legspeed * this.wingspeed) * (float) Math.PI * 0.15F;
        }
        this.leg4.zRot = this.leg4part2.zRot = 0.191F + newangle;
        this.leg4part3.zRot = 0.675F + newangle;

        newangle = 0.0F;
        if ((r.ri1 & 8) != 0) {
            newangle = Mth.sin(f2 * legspeed * this.wingspeed) * (float) Math.PI * 0.15F;
        }
        this.leg3.zRot = this.leg3part2.zRot = -0.191F + newangle;
        this.leg3part3.zRot = -0.675F + newangle;

        newangle = 0.0F;
        if ((r.ri1 & 16) != 0) {
            newangle = Mth.sin(f2 * legspeed * this.wingspeed) * (float) Math.PI * 0.1F;
        }
        this.leg6.zRot = this.leg6part2.zRot = -0.34F + newangle;

        newangle = 0.0F;
        if ((r.ri1 & 32) != 0) {
            newangle = Mth.sin(f2 * legspeed * this.wingspeed) * (float) Math.PI * 0.1F;
        }
        this.leg5.zRot = this.leg5part2.zRot = 0.34F + newangle;

        newangle = 0.0F;
        if ((r.ri2 & 1) != 0) {
            newangle = Mth.sin(f2 * mouthspeed * this.wingspeed) * (float) Math.PI * 0.35F;
            newangle = Math.abs(newangle);
        }

        // gold replaces jaw base rotations with absolute newangle (not offset)
        this.jaw1.yRot = newangle;
        this.jaw1part2.yRot = newangle;
        this.jaw1tooth1.yRot = this.jaw1tooth3.yRot = this.jaw1tooth5.yRot = newangle;
        this.jaw1tooth2.yRot = this.jaw1tooth4.yRot = this.jaw1tooth6.yRot = newangle;
        this.jaw2.yRot = -newangle;
        this.jaw2part2.yRot = -newangle;
        this.jaw2tooth1.yRot = this.jaw2tooth3.yRot = this.jaw2tooth5.yRot = -newangle;
        this.jaw2tooth2.yRot = this.jaw2tooth4.yRot = this.jaw2tooth6.yRot = -newangle;
        this.jaw3.xRot = -newangle;
        this.jaw3part2.xRot = -newangle;
        this.jaw3tooth1.xRot = this.jaw3tooth3.xRot = this.jaw3tooth5.xRot = -newangle;
        this.jaw3tooth2.xRot = this.jaw3tooth4.xRot = this.jaw3tooth6.xRot = -newangle;
        this.jaw4.xRot = newangle;
        this.jaw4part2.xRot = newangle;
        this.jaw4tooth1.xRot = this.jaw4tooth3.xRot = this.jaw4tooth5.xRot = newangle;
        this.jaw4tooth2.xRot = this.jaw4tooth4.xRot = this.jaw4tooth6.xRot = newangle;

        this.tonguepart1.xRot = this.tonguepart2.xRot = this.tonguepart3.xRot = 0.0F;
        this.tonguepart1.yRot = this.tonguepart2.yRot = this.tonguepart3.yRot = 0.0F;
        this.tonguepart1.zRot = this.tonguepart2.zRot = this.tonguepart3.zRot = 0.0F;
        this.tonguepart1.x = this.tonguepart3.x = this.tonguepart2.x;
        this.tonguepart1.y = this.tonguepart3.y = this.tonguepart2.y;
        this.tonguepart1.z = this.tonguepart2.z - newangle * 5.0F;
        this.tonguepart3.z = this.tonguepart2.z - newangle * 10.0F;

        newangle = Mth.sin(f2 * 0.1F * this.wingspeed) * (float) Math.PI * 0.06F;
        this.thorax.xRot = newangle;
        this.abdomen.y = (float) (this.thorax.y - Math.sin(newangle) * 14.0);

        newangle = Mth.cos(f2 * 1.4F * this.wingspeed) * (float) Math.PI * 0.2F;
        this.wing_1.xRot = 0.455F + newangle;
        this.wing_2.xRot = 0.455F + newangle;
        this.wing_3.xRot = 0.455F - newangle;
        this.wing_4.xRot = 0.455F - newangle;

        entity.setRenderInfo(r);
    }
}
