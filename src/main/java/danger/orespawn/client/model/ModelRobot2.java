package danger.orespawn.client.model;

import danger.orespawn.entity.RenderInfo;
import danger.orespawn.entity.Robot2;
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
 * Port of gold {@code ModelRobot2} (1.7.10 ModelBase, tex 256×512) → 1.21 HierarchicalModel.
 * Cubes/UVs/offsets 1:1. ClientProxy wingspeed {@code 1.0F}.
 * Full gold anim: walk legs, head yRot, attack arm phase via {@link RenderInfo#ri1}.
 */
@OnlyIn(Dist.CLIENT)
public class ModelRobot2 extends HierarchicalModel<Robot2> {
    public static final ModelLayerLocation LAYER_LOCATION =
            new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "robot2"), "main");

    private final float wingspeed;
    private final ModelPart root;
    private final ModelPart rleg1;
    private final ModelPart rleg2;
    private final ModelPart Shape3;
    private final ModelPart lleg2;
    private final ModelPart lleg1;
    private final ModelPart Shape6;
    private final ModelPart Shape7;
    private final ModelPart Shape8;
    private final ModelPart rarm3;
    private final ModelPart rarm2;
    private final ModelPart rarm1;
    private final ModelPart larm3;
    private final ModelPart larm2;
    private final ModelPart larm1;
    private final ModelPart head;

    public ModelRobot2(ModelPart root) {
        this(root, 1.0F);
    }

    public ModelRobot2(ModelPart root, float wingspeed) {
        this.root = root;
        this.wingspeed = wingspeed;
        this.rleg1 = root.getChild("rleg1");
        this.rleg2 = root.getChild("rleg2");
        this.Shape3 = root.getChild("Shape3");
        this.lleg2 = root.getChild("lleg2");
        this.lleg1 = root.getChild("lleg1");
        this.Shape6 = root.getChild("Shape6");
        this.Shape7 = root.getChild("Shape7");
        this.Shape8 = root.getChild("Shape8");
        this.rarm3 = root.getChild("rarm3");
        this.rarm2 = root.getChild("rarm2");
        this.rarm1 = root.getChild("rarm1");
        this.larm3 = root.getChild("larm3");
        this.larm2 = root.getChild("larm2");
        this.larm1 = root.getChild("larm1");
        this.head = root.getChild("head");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        root.addOrReplaceChild("rleg1",
                CubeListBuilder.create().texOffs(10, 250)
                        .addBox(-14.0F, 24.0F, -7.0F, 16.0F, 24.0F, 16.0F),
                PartPose.offset(-10.0F, -24.0F, 0.0F));
        root.addOrReplaceChild("rleg2",
                CubeListBuilder.create().texOffs(10, 150)
                        .addBox(-12.0F, 0.0F, -6.0F, 12.0F, 24.0F, 12.0F),
                PartPose.offset(-10.0F, -24.0F, 1.0F));
        root.addOrReplaceChild("Shape3",
                CubeListBuilder.create().texOffs(10, 50)
                        .addBox(-4.0F, 0.0F, -2.0F, 26.0F, 8.0F, 12.0F),
                PartPose.offset(-9.0F, -32.0F, -3.0F));
        root.addOrReplaceChild("lleg2",
                CubeListBuilder.create().texOffs(10, 200)
                        .addBox(0.0F, 0.0F, -6.0F, 12.0F, 24.0F, 12.0F),
                PartPose.offset(10.0F, -24.0F, 1.0F));
        root.addOrReplaceChild("lleg1",
                CubeListBuilder.create().texOffs(10, 300)
                        .addBox(-2.0F, 24.0F, -7.0F, 16.0F, 24.0F, 16.0F),
                PartPose.offset(10.0F, -24.0F, 0.0F));
        root.addOrReplaceChild("Shape6",
                CubeListBuilder.create().texOffs(10, 100)
                        .addBox(-4.0F, -8.0F, -3.0F, 8.0F, 8.0F, 8.0F),
                PartPose.offset(0.0F, -32.0F, 0.0F));
        root.addOrReplaceChild("Shape7",
                CubeListBuilder.create().texOffs(10, 350)
                        .addBox(0.0F, 0.0F, 0.0F, 26.0F, 8.0F, 12.0F),
                PartPose.offset(-13.0F, -48.0F, -5.0F));
        root.addOrReplaceChild("Shape8",
                CubeListBuilder.create().texOffs(16, 400)
                        .addBox(0.0F, 0.0F, 0.0F, 44.0F, 18.0F, 14.0F),
                PartPose.offset(-22.0F, -66.0F, -6.0F));
        root.addOrReplaceChild("rarm3",
                CubeListBuilder.create().texOffs(100, 100)
                        .addBox(-16.0F, -16.0F, -7.0F, 16.0F, 24.0F, 17.0F),
                PartPose.offset(-22.0F, -58.0F, 0.0F));
        root.addOrReplaceChild("rarm2",
                CubeListBuilder.create().texOffs(100, 200)
                        .addBox(-14.0F, 8.0F, -5.0F, 12.0F, 24.0F, 12.0F),
                PartPose.offset(-22.0F, -58.0F, 0.0F));
        root.addOrReplaceChild("rarm1",
                CubeListBuilder.create().texOffs(100, 300)
                        .addBox(-14.0F, 32.0F, -5.0F, 12.0F, 24.0F, 12.0F),
                PartPose.offset(-22.0F, -58.0F, 0.0F));
        root.addOrReplaceChild("larm3",
                CubeListBuilder.create().texOffs(100, 50)
                        .addBox(0.0F, -16.0F, -7.0F, 16.0F, 24.0F, 17.0F),
                PartPose.offset(22.0F, -58.0F, 0.0F));
        root.addOrReplaceChild("larm2",
                CubeListBuilder.create().texOffs(100, 150)
                        .addBox(2.0F, 8.0F, -5.0F, 12.0F, 24.0F, 12.0F),
                PartPose.offset(21.0F, -58.0F, 0.0F));
        root.addOrReplaceChild("larm1",
                CubeListBuilder.create().texOffs(100, 250)
                        .addBox(2.0F, 32.0F, -5.0F, 12.0F, 24.0F, 12.0F),
                PartPose.offset(21.0F, -58.0F, 0.0F));
        root.addOrReplaceChild("head",
                CubeListBuilder.create().texOffs(50, 10)
                        .addBox(-7.0F, -12.0F, -5.0F, 15.0F, 12.0F, 10.0F),
                PartPose.offset(0.0F, -66.0F, 1.0F));

        return LayerDefinition.create(mesh, 256, 512);
    }

    @Override
    public ModelPart root() {
        return this.root;
    }

    @Override
    public void setupAnim(
            Robot2 entity,
            float limbSwing,
            float limbSwingAmount,
            float ageInTicks,
            float netHeadYaw,
            float headPitch) {
        float f1 = limbSwingAmount;
        float f2 = ageInTicks;
        float newangle;
        if (f1 > 0.1F) {
            newangle = Mth.cos(f2 * 0.3F * this.wingspeed) * (float) Math.PI * 0.12F * f1;
        } else {
            newangle = 0.0F;
        }
        this.lleg1.xRot = newangle;
        this.lleg2.xRot = newangle;
        this.rleg1.xRot = -newangle;
        this.rleg2.xRot = -newangle;
        this.head.yRot = (float) Math.toRadians(netHeadYaw);

        // gold arm swing phase flip on sin zero-crossing
        newangle = Mth.sin((float) Math.toRadians(f2 * 20.0F * this.wingspeed));
        float nextangle = Mth.sin((float) Math.toRadians(f2 * 20.0F * this.wingspeed + 1.5F));
        RenderInfo r = entity.getRenderInfo();
        if (nextangle > 0.0F && newangle < 0.0F) {
            r.ri1 = 0;
            if (entity.getAttacking() == 0) {
                r.ri1 = 0;
            } else {
                while (r.ri1 == 0) {
                    r.ri1 = entity.getRandom().nextInt(4);
                }
            }
        }
        newangle = (float) Math.toRadians(f2 * 20.0F * this.wingspeed);
        if (r.ri1 != 1 && r.ri1 != 3) {
            this.rarm1.xRot = 0.0F;
            this.rarm2.xRot = 0.0F;
            this.rarm3.xRot = 0.0F;
        } else {
            this.rarm1.xRot = newangle;
            this.rarm2.xRot = newangle;
            this.rarm3.xRot = newangle;
        }
        if (r.ri1 != 2 && r.ri1 != 3) {
            this.larm1.xRot = 0.0F;
            this.larm2.xRot = 0.0F;
            this.larm3.xRot = 0.0F;
        } else {
            this.larm1.xRot = newangle;
            this.larm2.xRot = newangle;
            this.larm3.xRot = newangle;
        }
        entity.setRenderInfo(r);
    }
}
