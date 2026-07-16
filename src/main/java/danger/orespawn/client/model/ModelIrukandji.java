package danger.orespawn.client.model;

import danger.orespawn.entity.Irukandji;
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
 * Port of gold {@code ModelIrukandji} (1.7.10 ModelBase, tex 64×32) → 1.21 HierarchicalModel.
 * Cubes / pivots / UV 1:1. Full gold {@code func_78088_a} four tentacle chains (sin/cos segment offsets).
 * wingspeed default 1.0 from ClientProxy (stored; unused in gold anim).
 */
@OnlyIn(Dist.CLIENT)
public class ModelIrukandji extends HierarchicalModel<Irukandji> {
    /** Model layer location. */
    public static final ModelLayerLocation LAYER_LOCATION =
            new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "irukandji"), "main");

    private final float wingspeed;
    private final ModelPart root;
    private final ModelPart body;
    private final ModelPart t11;
    private final ModelPart t12;
    private final ModelPart t21;
    private final ModelPart t22;
    private final ModelPart t31;
    private final ModelPart t32;
    private final ModelPart t41;
    private final ModelPart t42;

    /** Rest pivots from gold constructor (upper segments never translate; lower repositioned each frame). */
    private static final float T11_X = 1.0F;
    private static final float T11_Y = 10.0F;
    private static final float T11_Z = -2.0F;
    private static final float T21_X = -2.0F;
    private static final float T21_Y = 10.0F;
    private static final float T21_Z = -2.0F;
    private static final float T31_X = 1.0F;
    private static final float T31_Y = 10.0F;
    private static final float T31_Z = 1.0F;
    private static final float T41_X = -2.0F;
    private static final float T41_Y = 10.0F;
    private static final float T41_Z = 1.0F;

    public ModelIrukandji(ModelPart root) {
        this(root, 1.0F);
    }

    public ModelIrukandji(ModelPart root, float wingspeed) {
        this.root = root;
        this.wingspeed = wingspeed;
        this.body = root.getChild("body");
        this.t11 = root.getChild("t11");
        this.t12 = root.getChild("t12");
        this.t21 = root.getChild("t21");
        this.t22 = root.getChild("t22");
        this.t31 = root.getChild("t31");
        this.t32 = root.getChild("t32");
        this.t41 = root.getChild("t41");
        this.t42 = root.getChild("t42");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        // gold tex 64×32; cubes/pivots/UV 1:1
        root.addOrReplaceChild(
                "body",
                CubeListBuilder.create().texOffs(0, 9).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 4.0F, 4.0F),
                PartPose.offset(0.0F, 6.0F, 0.0F));
        root.addOrReplaceChild(
                "t11",
                CubeListBuilder.create().texOffs(25, 0).addBox(0.0F, 0.0F, 0.0F, 1.0F, 7.0F, 1.0F),
                PartPose.offset(T11_X, T11_Y, T11_Z));
        root.addOrReplaceChild(
                "t12",
                CubeListBuilder.create().texOffs(5, 0).addBox(0.0F, 0.0F, 0.0F, 1.0F, 7.0F, 1.0F),
                PartPose.offset(1.0F, 17.0F, -2.0F));
        root.addOrReplaceChild(
                "t21",
                CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, 0.0F, 0.0F, 1.0F, 7.0F, 1.0F),
                PartPose.offset(T21_X, T21_Y, T21_Z));
        root.addOrReplaceChild(
                "t22",
                CubeListBuilder.create().texOffs(20, 0).addBox(0.0F, 0.0F, 0.0F, 1.0F, 7.0F, 1.0F),
                PartPose.offset(-2.0F, 17.0F, -2.0F));
        root.addOrReplaceChild(
                "t31",
                CubeListBuilder.create().texOffs(30, 0).addBox(0.0F, 0.0F, 0.0F, 1.0F, 7.0F, 1.0F),
                PartPose.offset(T31_X, T31_Y, T31_Z));
        root.addOrReplaceChild(
                "t32",
                CubeListBuilder.create().texOffs(10, 0).addBox(0.0F, 0.0F, 0.0F, 1.0F, 7.0F, 1.0F),
                PartPose.offset(1.0F, 17.0F, 1.0F));
        root.addOrReplaceChild(
                "t41",
                CubeListBuilder.create().texOffs(35, 0).addBox(0.0F, 0.0F, 0.0F, 1.0F, 7.0F, 1.0F),
                PartPose.offset(T41_X, T41_Y, T41_Z));
        root.addOrReplaceChild(
                "t42",
                CubeListBuilder.create().texOffs(15, 0).addBox(0.0F, 0.0F, 0.0F, 1.0F, 7.0F, 1.0F),
                PartPose.offset(-2.0F, 17.0F, 1.0F));

        return LayerDefinition.create(mesh, 64, 32);
    }

    @Override
    public ModelPart root() {
        return this.root;
    }

    @Override
    public void setupAnim(
            Irukandji entity,
            float limbSwing,
            float limbSwingAmount,
            float ageInTicks,
            float netHeadYaw,
            float headPitch) {
        // suppress unused field warning (gold stores wingspeed but never multiplies anim by it)
        @SuppressWarnings("unused")
        float ws = this.wingspeed;

        // gold never translates upper segments — pin rest pivots each frame
        this.t11.x = T11_X;
        this.t11.y = T11_Y;
        this.t11.z = T11_Z;
        this.t21.x = T21_X;
        this.t21.y = T21_Y;
        this.t21.z = T21_Z;
        this.t31.x = T31_X;
        this.t31.y = T31_Y;
        this.t31.z = T31_Z;
        this.t41.x = T41_X;
        this.t41.y = T41_Y;
        this.t41.z = T41_Z;

        float newangle;
        float d1;
        float d2;
        float d3;
        float d4;

        // --- tent 1 chain (t11 → t12) ---
        // field_78795_f→xRot, field_78808_h→zRot; field_78800_c/d/e → x/y/z
        newangle = Mth.cos(ageInTicks * 0.55F) * (float) Math.PI * 0.15F;
        this.t11.xRot = newangle;
        d1 = (float) (Math.sin(newangle) * 7.0);
        d2 = (float) (Math.cos(newangle) * 7.0);
        this.t12.z = this.t11.z + d1;
        newangle = Mth.cos(ageInTicks * 0.35F) * (float) Math.PI * 0.1F;
        this.t11.zRot = newangle;
        d3 = (float) (Math.cos(newangle) * d2);
        d4 = (float) (Math.sin(newangle) * d2);
        this.t12.x = this.t11.x - d4;
        this.t12.y = this.t11.y + d3;
        newangle = Mth.cos(ageInTicks * 0.45F) * (float) Math.PI * 0.15F;
        this.t12.xRot = newangle;
        newangle = Mth.cos(ageInTicks * 0.25F) * (float) Math.PI * 0.1F;
        this.t12.zRot = newangle;

        // --- tent 2 chain (t21 → t22) ---
        newangle = Mth.cos(ageInTicks * 0.65F) * (float) Math.PI * 0.15F;
        this.t21.xRot = newangle;
        d1 = (float) (Math.sin(newangle) * 7.0);
        d2 = (float) (Math.cos(newangle) * 7.0);
        this.t22.z = this.t21.z + d1;
        newangle = Mth.cos(ageInTicks * 0.45F) * (float) Math.PI * 0.1F;
        this.t21.zRot = newangle;
        d3 = (float) (Math.cos(newangle) * d2);
        d4 = (float) (Math.sin(newangle) * d2);
        this.t22.x = this.t21.x - d4;
        this.t22.y = this.t21.y + d3;
        newangle = Mth.cos(ageInTicks * 0.55F) * (float) Math.PI * 0.15F;
        this.t22.xRot = newangle;
        newangle = Mth.cos(ageInTicks * 0.35F) * (float) Math.PI * 0.1F;
        this.t22.zRot = newangle;

        // --- tent 3 chain (t31 → t32) ---
        newangle = Mth.cos(ageInTicks * 0.5F) * (float) Math.PI * 0.15F;
        this.t31.xRot = newangle;
        d1 = (float) (Math.sin(newangle) * 7.0);
        d2 = (float) (Math.cos(newangle) * 7.0);
        this.t32.z = this.t31.z + d1;
        newangle = Mth.cos(ageInTicks * 0.3F) * (float) Math.PI * 0.1F;
        this.t31.zRot = newangle;
        d3 = (float) (Math.cos(newangle) * d2);
        d4 = (float) (Math.sin(newangle) * d2);
        this.t32.x = this.t31.x - d4;
        this.t32.y = this.t31.y + d3;
        newangle = Mth.cos(ageInTicks * 0.4F) * (float) Math.PI * 0.15F;
        this.t32.xRot = newangle;
        newangle = Mth.cos(ageInTicks * 0.2F) * (float) Math.PI * 0.1F;
        this.t32.zRot = newangle;

        // --- tent 4 chain (t41 → t42) ---
        newangle = Mth.cos(ageInTicks * 0.57F) * (float) Math.PI * 0.15F;
        this.t41.xRot = newangle;
        d1 = (float) (Math.sin(newangle) * 7.0);
        d2 = (float) (Math.cos(newangle) * 7.0);
        this.t42.z = this.t41.z + d1;
        newangle = Mth.cos(ageInTicks * 0.37F) * (float) Math.PI * 0.1F;
        this.t41.zRot = newangle;
        d3 = (float) (Math.cos(newangle) * d2);
        d4 = (float) (Math.sin(newangle) * d2);
        this.t42.x = this.t41.x - d4;
        this.t42.y = this.t41.y + d3;
        newangle = Mth.cos(ageInTicks * 0.48F) * (float) Math.PI * 0.15F;
        this.t42.xRot = newangle;
        newangle = Mth.cos(ageInTicks * 0.29F) * (float) Math.PI * 0.1F;
        this.t42.zRot = newangle;
    }
}
