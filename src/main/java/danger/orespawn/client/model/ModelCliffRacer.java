package danger.orespawn.client.model;

import danger.orespawn.entity.CliffRacer;
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
 * Port of gold {@code ModelCliffRacer} (1.7.10 ModelBase) → 1.21 HierarchicalModel.
 * Cubes/UVs 1:1. Texture 64×64. Wingspeed default 1.0F matches ClientProxy.
 */
@OnlyIn(Dist.CLIENT)
public class ModelCliffRacer extends HierarchicalModel<CliffRacer> {
    /** Model layer location. */
    public static final ModelLayerLocation LAYER_LOCATION =
            new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "cliff_racer"), "main");

    private final float wingspeed;
    private final ModelPart root;
    private final ModelPart Body;
    private final ModelPart Fins;
    private final ModelPart LWing;
    private final ModelPart RWing;
    private final ModelPart Tail;
    private final ModelPart TailEnd;
    private final ModelPart Head;
    private final ModelPart Beak;

    public ModelCliffRacer(ModelPart root) {
        this(root, 1.0F);
    }

    public ModelCliffRacer(ModelPart root, float wingspeed) {
        this.root = root;
        this.wingspeed = wingspeed;
        this.Body = root.getChild("Body");
        this.Fins = root.getChild("Fins");
        this.LWing = root.getChild("LWing");
        this.RWing = root.getChild("RWing");
        this.Tail = root.getChild("Tail");
        this.TailEnd = root.getChild("TailEnd");
        this.Head = root.getChild("Head");
        this.Beak = root.getChild("Beak");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        root.addOrReplaceChild(
                "Body",
                CubeListBuilder.create().texOffs(0, 52).addBox(0.0F, 0.0F, 0.0F, 3.0F, 1.0F, 10.0F),
                PartPose.offset(-1.0F, 15.0F, -4.0F));
        root.addOrReplaceChild(
                "Fins",
                CubeListBuilder.create().texOffs(0, 40).addBox(0.0F, -4.0F, 0.0F, 1.0F, 6.0F, 3.0F),
                PartPose.offset(0.0F, 15.0F, -1.0F));
        root.addOrReplaceChild(
                "LWing",
                CubeListBuilder.create().texOffs(0, 31).addBox(0.0F, 0.0F, 0.0F, 7.0F, 1.0F, 6.0F),
                PartPose.offset(2.0F, 15.0F, -2.0F));
        root.addOrReplaceChild(
                "RWing",
                CubeListBuilder.create().texOffs(39, 0).addBox(-7.0F, 0.0F, 0.0F, 7.0F, 1.0F, 6.0F),
                PartPose.offset(-1.0F, 15.0F, -2.0F));
        root.addOrReplaceChild(
                "Tail",
                CubeListBuilder.create().texOffs(0, 16).addBox(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 9.0F),
                PartPose.offset(0.0F, 15.0F, 6.0F));
        root.addOrReplaceChild(
                "TailEnd",
                CubeListBuilder.create().texOffs(0, 10).addBox(0.0F, -1.0F, 9.0F, 2.0F, 2.0F, 2.0F),
                PartPose.offset(-0.5F, 15.0F, 6.0F));
        root.addOrReplaceChild(
                "Head",
                CubeListBuilder.create().texOffs(28, 21).addBox(0.0F, 0.0F, 0.0F, 2.0F, 2.0F, 2.0F),
                PartPose.offset(-0.5F, 14.0F, -6.0F));
        root.addOrReplaceChild(
                "Beak",
                CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 2.0F),
                PartPose.offset(0.0F, 14.5F, -8.0F));

        return LayerDefinition.create(mesh, 64, 64);
    }

    @Override
    public ModelPart root() {
        return this.root;
    }

    @Override
    public void setupAnim(
            CliffRacer entity,
            float limbSwing,
            float limbSwingAmount,
            float ageInTicks,
            float netHeadYaw,
            float headPitch) {
        // gold: continuous wing flap (no limbSwing gate)
        float newangle = Mth.cos(ageInTicks * 1.3F * this.wingspeed) * (float) Math.PI * 0.25F;
        this.LWing.zRot = newangle;
        this.RWing.zRot = -newangle;
    }
}
