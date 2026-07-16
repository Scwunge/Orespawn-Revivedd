package danger.orespawn.client.model;

import danger.orespawn.entity.LeafMonster;
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
 * Port of gold {@code ModelLeafMonster} (1.7.10 ModelBase, tex 128×128) → 1.21 HierarchicalModel.
 * Cube sizes/UVs/offsets 1:1. Idle crouch vs attacking limb swing from gold {@code render()}.
 */
@OnlyIn(Dist.CLIENT)
public class ModelLeafMonster extends HierarchicalModel<LeafMonster> {
    /** Model layer location. */
    public static final ModelLayerLocation LAYER_LOCATION =
            new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "leaf_monster"), "main");

    private final ModelPart root;
    private final ModelPart body;
    private final ModelPart larm;
    private final ModelPart rarm;
    private final ModelPart lleg;
    private final ModelPart rleg;

    public ModelLeafMonster(ModelPart root) {
        this.root = root;
        this.body = root.getChild("body");
        this.larm = root.getChild("larm");
        this.rarm = root.getChild("rarm");
        this.lleg = root.getChild("lleg");
        this.rleg = root.getChild("rleg");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        // gold: texOffs then addBox; setTextureSize(64,32) per-part ignored — sheet is 128×128
        root.addOrReplaceChild("body",
                CubeListBuilder.create().texOffs(32, 32)
                        .addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F),
                PartPose.offset(0.0F, 0.0F, 0.0F));
        root.addOrReplaceChild("larm",
                CubeListBuilder.create().texOffs(64, 0)
                        .addBox(0.0F, -16.0F, -8.0F, 16.0F, 16.0F, 16.0F),
                PartPose.offset(8.0F, -8.0F, 0.0F));
        root.addOrReplaceChild("rarm",
                CubeListBuilder.create().texOffs(0, 0)
                        .addBox(-16.0F, -16.0F, -8.0F, 16.0F, 16.0F, 16.0F),
                PartPose.offset(-8.0F, -8.0F, 0.0F));
        root.addOrReplaceChild("lleg",
                CubeListBuilder.create().texOffs(64, 64)
                        .addBox(0.0F, 0.0F, -8.0F, 16.0F, 16.0F, 16.0F),
                PartPose.offset(8.0F, 8.0F, 0.0F));
        root.addOrReplaceChild("rleg",
                CubeListBuilder.create().texOffs(0, 64)
                        .addBox(-16.0F, 0.0F, -8.0F, 16.0F, 16.0F, 16.0F),
                PartPose.offset(-8.0F, 8.0F, 0.0F));

        return LayerDefinition.create(mesh, 128, 128);
    }

    @Override
    public ModelPart root() {
        return this.root;
    }

    @Override
    public void setupAnim(
            LeafMonster entity,
            float limbSwing,
            float limbSwingAmount,
            float ageInTicks,
            float netHeadYaw,
            float headPitch) {
        // gold render(): idle = body y=16 arms y=8 limbs zero; attack = body y=0 arms y=-8 walk+swing
        if (entity.getAttacking() == 0) {
            this.body.y = 16.0F;
            this.rarm.y = 8.0F;
            this.larm.y = 8.0F;
            this.rarm.yRot = 0.0F;
            this.larm.yRot = 0.0F;
            this.rarm.xRot = 0.0F;
            this.larm.xRot = 0.0F;
            this.lleg.xRot = 0.0F;
            this.rleg.xRot = 0.0F;
        } else {
            this.body.y = 0.0F;
            this.rarm.y = -8.0F;
            this.larm.y = -8.0F;
            float newangle;
            if (limbSwingAmount > 0.1F) {
                newangle = Mth.cos(ageInTicks * 0.95F) * (float) Math.PI * 0.25F * limbSwingAmount;
            } else {
                newangle = 0.0F;
            }
            this.lleg.xRot = newangle;
            this.rleg.xRot = -newangle;
            newangle = Mth.cos(ageInTicks * 0.7F) * (float) Math.PI * 0.55F;
            this.rarm.yRot = -Math.abs(newangle);
            this.larm.yRot = Math.abs(newangle);
            this.rarm.xRot = -Math.abs(newangle);
            this.larm.xRot = -Math.abs(newangle);
        }
    }
}
