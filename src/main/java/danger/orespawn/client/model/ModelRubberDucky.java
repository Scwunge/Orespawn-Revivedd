package danger.orespawn.client.model;

import danger.orespawn.entity.RenderInfo;
import danger.orespawn.entity.RubberDucky;
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
 * Port of gold {@code ModelRubberDucky} (1.7.10 ModelBase) → 1.21 HierarchicalModel.
 * Cubes/UVs 1:1. Texture 64×64. Wingspeed default 1.0F matches ClientProxy.
 */
@OnlyIn(Dist.CLIENT)
public class ModelRubberDucky extends HierarchicalModel<RubberDucky> {
    /** Model layer location. */
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
            ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "rubber_ducky"), "main");

    private final float wingspeed;
    private final ModelPart root;
    private final ModelPart bottom;
    private final ModelPart body;
    private final ModelPart back;
    private final ModelPart neck;
    private final ModelPart head;
    private final ModelPart beak;
    private final ModelPart Lwing;
    private final ModelPart Rwing;

    public ModelRubberDucky(ModelPart root) {
        this(root, 1.0F);
    }

    public ModelRubberDucky(ModelPart root, float wingspeed) {
        this.root = root;
        this.wingspeed = wingspeed;
        this.bottom = root.getChild("bottom");
        this.body = root.getChild("body");
        this.back = root.getChild("back");
        this.neck = root.getChild("neck");
        this.head = root.getChild("head");
        this.beak = root.getChild("beak");
        this.Lwing = root.getChild("Lwing");
        this.Rwing = root.getChild("Rwing");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        root.addOrReplaceChild(
                "bottom",
                CubeListBuilder.create().texOffs(0, 56).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 1.0F, 4.0F),
                PartPose.offset(0.0F, 23.0F, 0.0F));
        root.addOrReplaceChild(
                "body",
                CubeListBuilder.create().texOffs(0, 45).addBox(-3.0F, 0.0F, -3.0F, 6.0F, 2.0F, 8.0F),
                PartPose.offset(0.0F, 21.0F, 0.0F));
        root.addOrReplaceChild(
                "back",
                CubeListBuilder.create().texOffs(0, 33).addBox(-3.0F, 0.0F, -3.0F, 6.0F, 1.0F, 10.0F),
                PartPose.offset(0.0F, 20.0F, 0.0F));
        root.addOrReplaceChild(
                "neck",
                CubeListBuilder.create().texOffs(17, 27).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 1.0F, 2.0F),
                PartPose.offset(0.0F, 19.0F, -1.0F));
        root.addOrReplaceChild(
                "head",
                CubeListBuilder.create().texOffs(13, 18).addBox(-2.0F, -4.0F, -2.0F, 4.0F, 4.0F, 4.0F),
                PartPose.offset(0.0F, 19.0F, -1.0F));
        root.addOrReplaceChild(
                "beak",
                CubeListBuilder.create().texOffs(0, 21).addBox(-1.5F, -1.0F, -5.0F, 3.0F, 1.0F, 3.0F),
                PartPose.offset(0.0F, 19.0F, -1.0F));
        root.addOrReplaceChild(
                "Lwing",
                CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, -0.5F, 0.0F, 2.0F, 1.0F, 5.0F),
                PartPose.offset(3.0F, 21.0F, -2.0F));
        root.addOrReplaceChild(
                "Rwing",
                CubeListBuilder.create().texOffs(17, 0).addBox(-2.0F, -0.5F, 0.0F, 2.0F, 1.0F, 5.0F),
                PartPose.offset(-3.0F, 21.0F, -2.0F));

        return LayerDefinition.create(mesh, 64, 64);
    }

    @Override
    public ModelPart root() {
        return this.root;
    }

    @Override
    public void setupAnim(
            RubberDucky entity,
            float limbSwing,
            float limbSwingAmount,
            float ageInTicks,
            float netHeadYaw,
            float headPitch) {
        animate(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
    }

    private void animate(RubberDucky entity, float f, float f1, float f2, float f3, float f4) {
        this.head.yRot = (float) Math.toRadians(f3) * 0.45F;
        this.beak.yRot = this.head.yRot;
        this.head.xRot = (float) Math.toRadians(f4) * 0.65F;
        this.beak.xRot = this.head.xRot;

        RenderInfo r = entity.getRenderInfo();
        float newangle = Mth.cos(f2 * 1.0F * this.wingspeed) * (float) Math.PI * 0.15F;
        float nextangle = Mth.cos((f2 + 0.3F) * 1.0F * this.wingspeed) * (float) Math.PI * 0.15F;

        // gold: zero-crossing random flap enable; evil (killcount≥5) flaps harder
        if (nextangle > 0.0F && newangle < 0.0F) {
            r.ri1 = 0;
            if (entity.level().random.nextInt(3) == 1) {
                r.ri1 = 1;
            }
            if (entity.getKillCount() >= 5) {
                if (entity.level().random.nextInt(2) == 1) {
                    r.ri1 = 1;
                }
                newangle *= 4.0F;
            }
        }

        if (r.ri1 == 0) {
            newangle = 0.0F;
        }
        // gold: sitting freezes wings — no sit in simple port

        newangle = Math.abs(newangle);
        this.Lwing.zRot = -newangle;
        this.Lwing.yRot = newangle / 2.0F;
        this.Rwing.zRot = newangle;
        this.Rwing.yRot = -newangle / 2.0F;
        entity.setRenderInfo(r);
    }
}
