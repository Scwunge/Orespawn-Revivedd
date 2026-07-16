package danger.orespawn.client.model;

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
import net.minecraft.world.entity.Entity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

/**
 * Port of gold {@code ModelIsland} (1.7.10 ModelBase, tex 64×32) → HierarchicalModel.
 * Cubes/UVs/pivots 1:1. Shared by Island + IslandToo (generic T).
 * Wingspeed default 1.0F matches ClientProxy {@code new ModelIsland(1.0F)}.
 * Gold render anims (Shape1–3 full-sphere spin) live in {@link #setupAnim}.
 */
@OnlyIn(Dist.CLIENT)
public class ModelIsland<T extends Entity> extends HierarchicalModel<T> {
    /** Model layer location. */
    public static final ModelLayerLocation LAYER_LOCATION =
            new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "island"), "main");

    private final float wingspeed;
    private final ModelPart root;
    private final ModelPart Shape1;
    private final ModelPart Shape2;
    private final ModelPart Shape3;

    public ModelIsland(ModelPart root) {
        this(root, 1.0F);
    }

    public ModelIsland(ModelPart root, float wingspeed) {
        this.root = root;
        this.wingspeed = wingspeed;
        this.Shape1 = root.getChild("Shape1");
        this.Shape2 = root.getChild("Shape2");
        this.Shape3 = root.getChild("Shape3");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        // gold Shape1: box(-4,-4,-4, 8,8,8) UV 0,0 pivot(0,16,0) rot 0,0,0
        root.addOrReplaceChild(
                "Shape1",
                CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -4.0F, -4.0F, 8.0F, 8.0F, 8.0F),
                PartPose.offset(0.0F, 16.0F, 0.0F));
        // gold Shape2: UV 32,0 pivot(0,16,0) rot (π/4, π/4, π/4)
        root.addOrReplaceChild(
                "Shape2",
                CubeListBuilder.create().texOffs(32, 0).addBox(-4.0F, -4.0F, -4.0F, 8.0F, 8.0F, 8.0F),
                PartPose.offsetAndRotation(
                        0.0F,
                        16.0F,
                        0.0F,
                        (float) (Math.PI / 4),
                        (float) (Math.PI / 4),
                        (float) (Math.PI / 4)));
        // gold Shape3: UV 32,16 pivot(0,16,0) rot (π/4, π/4, π/4)
        root.addOrReplaceChild(
                "Shape3",
                CubeListBuilder.create().texOffs(32, 16).addBox(-4.0F, -4.0F, -4.0F, 8.0F, 8.0F, 8.0F),
                PartPose.offsetAndRotation(
                        0.0F,
                        16.0F,
                        0.0F,
                        (float) (Math.PI / 4),
                        (float) (Math.PI / 4),
                        (float) (Math.PI / 4)));

        return LayerDefinition.create(mesh, 64, 32);
    }

    @Override
    public ModelPart root() {
        return this.root;
    }

    @Override
    public void setupAnim(
            T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        // gold ModelIsland.render (f2 = ageInTicks, wingspeed default 1.0)
        float newangle = Mth.cos(ageInTicks * 0.05F * this.wingspeed) * (float) Math.PI;
        this.Shape1.xRot = newangle;
        newangle = Mth.cos(ageInTicks * 0.051F * this.wingspeed) * (float) Math.PI;
        this.Shape1.yRot = newangle;
        newangle = Mth.cos(ageInTicks * 0.052F * this.wingspeed) * (float) Math.PI;
        this.Shape1.zRot = newangle;

        newangle = Mth.cos(ageInTicks * 0.053F * this.wingspeed) * (float) Math.PI;
        this.Shape2.xRot = newangle;
        newangle = Mth.cos(ageInTicks * 0.054F * this.wingspeed) * (float) Math.PI;
        this.Shape2.yRot = newangle;
        newangle = Mth.cos(ageInTicks * 0.055F * this.wingspeed) * (float) Math.PI;
        this.Shape2.zRot = newangle;

        newangle = Mth.cos(ageInTicks * 0.056F * this.wingspeed) * (float) Math.PI;
        this.Shape3.xRot = newangle;
        newangle = Mth.cos(ageInTicks * 0.057F * this.wingspeed) * (float) Math.PI;
        this.Shape3.yRot = newangle;
        newangle = Mth.cos(ageInTicks * 0.058F * this.wingspeed) * (float) Math.PI;
        this.Shape3.zRot = newangle;
    }
}
