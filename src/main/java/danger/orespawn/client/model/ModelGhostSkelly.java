package danger.orespawn.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import danger.orespawn.entity.GhostSkelly;
import danger.orespawn.entity.RenderInfo;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.util.Mth;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

/**
 * Port of gold {@code ModelGhostSkelly} (1.7.10 ModelBase, tex 128×64) → HierarchicalModel.
 * Cubes/UVs/pivots 1:1. Gold translucent tint + head spin via {@link RenderInfo}.
 */
@OnlyIn(Dist.CLIENT)
public class ModelGhostSkelly extends HierarchicalModel<GhostSkelly> {
    /** Gold GL11.glColor4f(0.75, 0.75, 0.75, 0.25) as ARGB. */
    private static final int GHOST_TINT = 0x40BFBFBF;

    private final ModelPart root;
    private final ModelPart body;
    private final ModelPart shirt;
    private final ModelPart head;
    private final ModelPart stem;
    private final ModelPart rarm;
    private final ModelPart larm;
    private final ModelPart rsleeve;
    private final ModelPart lsleeve;
    private final ModelPart lchains;
    private final ModelPart rchains;

    public ModelGhostSkelly(ModelPart root) {
        super(RenderType::entityTranslucent);
        this.root = root;
        this.body = root.getChild("body");
        this.shirt = root.getChild("shirt");
        this.head = root.getChild("head");
        this.stem = root.getChild("stem");
        this.rarm = root.getChild("rarm");
        this.larm = root.getChild("larm");
        this.rsleeve = root.getChild("rsleeve");
        this.lsleeve = root.getChild("lsleeve");
        this.lchains = root.getChild("lchains");
        this.rchains = root.getChild("rchains");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        // gold tex 128×64 — all cubes 1:1
        root.addOrReplaceChild(
                "body",
                CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, 0.0F, 0.0F, 1.0F, 21.0F, 1.0F),
                PartPose.offset(0.0F, -1.0F, 0.0F));
        root.addOrReplaceChild(
                "shirt",
                CubeListBuilder.create().texOffs(42, 43).addBox(-2.0F, 0.0F, -2.0F, 5.0F, 12.0F, 5.0F),
                PartPose.offset(0.0F, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "head",
                CubeListBuilder.create().texOffs(40, 29).addBox(-3.0F, 0.0F, -3.0F, 7.0F, 5.0F, 7.0F),
                PartPose.offset(0.0F, -6.0F, 0.0F));
        root.addOrReplaceChild(
                "stem",
                CubeListBuilder.create().texOffs(49, 23).addBox(0.0F, 0.0F, 0.0F, 1.0F, 2.0F, 1.0F),
                PartPose.offsetAndRotation(0.0F, -8.0F, 0.0F, 0.1745329F, 0.0F, 0.1745329F));
        root.addOrReplaceChild(
                "rarm",
                CubeListBuilder.create().texOffs(26, 0).addBox(-14.0F, 0.0F, 0.0F, 15.0F, 1.0F, 1.0F),
                PartPose.offset(0.0F, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "larm",
                CubeListBuilder.create().texOffs(63, 0).addBox(0.0F, 0.0F, 0.0F, 15.0F, 1.0F, 1.0F),
                PartPose.offset(0.0F, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "rsleeve",
                CubeListBuilder.create().texOffs(31, 7).addBox(-11.0F, 0.0F, -1.0F, 9.0F, 8.0F, 3.0F),
                PartPose.offset(0.0F, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "lsleeve",
                CubeListBuilder.create().texOffs(71, 7).addBox(3.0F, 0.0F, -1.0F, 9.0F, 8.0F, 3.0F),
                PartPose.offset(0.0F, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "lchains",
                CubeListBuilder.create().texOffs(98, 0).addBox(11.0F, -1.0F, 0.0F, 3.0F, 16.0F, 1.0F),
                PartPose.offset(0.0F, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "rchains",
                CubeListBuilder.create().texOffs(12, 0).addBox(-13.0F, -1.0F, 0.0F, 3.0F, 10.0F, 1.0F),
                PartPose.offset(0.0F, 0.0F, 0.0F));

        return LayerDefinition.create(mesh, 128, 64);
    }

    @Override
    public ModelPart root() {
        return this.root;
    }

    @Override
    public void setupAnim(
            GhostSkelly entity,
            float limbSwing,
            float limbSwingAmount,
            float ageInTicks,
            float netHeadYaw,
            float headPitch) {
        RenderInfo r = entity.getRenderInfo();
        float newangle;
        float newrf1;

        // gold: arm/sleeve/chain sway
        this.larm.zRot =
                this.lsleeve.zRot =
                        this.lchains.zRot = Mth.cos(ageInTicks * 0.2F) * (float) Math.PI * 0.05F;
        this.rarm.zRot =
                this.rsleeve.zRot =
                        this.rchains.zRot = Mth.cos(ageInTicks * 0.22F) * (float) Math.PI * 0.05F;
        this.larm.yRot =
                this.lsleeve.yRot =
                        this.lchains.yRot = Mth.cos(ageInTicks * 0.24F) * (float) Math.PI * 0.05F;
        this.rarm.yRot =
                this.rsleeve.yRot =
                        this.rchains.yRot = Mth.cos(ageInTicks * 0.26F) * (float) Math.PI * 0.05F;

        // gold: occasional full head spin (RenderInfo.ri2 bit 0)
        newangle = Mth.cos(ageInTicks * 0.05F) * (float) Math.PI * 2.0F;
        newrf1 = ageInTicks * 0.05F % (float) (Math.PI * 2);
        newrf1 = Math.abs(newrf1);
        if (newrf1 < r.rf2) {
            r.ri2 = 0;
            if (entity.level().random.nextInt(3) == 1) {
                r.ri2 |= 1;
            }
        }
        r.rf2 = newrf1;
        if ((r.ri2 & 1) == 0) {
            newangle = 0.0F;
        }
        this.head.yRot = newangle;
        entity.setRenderInfo(r);
    }

    @Override
    public void renderToBuffer(
            PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, int color) {
        super.renderToBuffer(poseStack, buffer, packedLight, packedOverlay, GHOST_TINT);
    }
}
