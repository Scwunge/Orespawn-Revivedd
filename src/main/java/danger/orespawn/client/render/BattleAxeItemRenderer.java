package danger.orespawn.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import danger.orespawn.client.model.ModelBattleAxe;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

/**
 * BattleAxe 3D hold model (1.7.10 {@code ModelBattleAxe} / {@code RenderBattleAxe}).
 * <p>
 * Gold has <b>no arm animations</b> — only draw-time transforms. Combat is UltimateSword family
 * (not BerthaHit). Live item: {@code MyBattleAxe = new UltimateSword(., toolBATTLE)}.
 * <p>
 * Critical 1.21 detail: {@link ItemRenderer} always does {@code translate(-0.5)} before
 * {@code renderByItem}. Undo with {@code translate(0.5)} so the grip sits on the hold point.
 * <p>
 * Gold {@code RenderBattleAxe} scale is <b>0.35</b> with model f5=1.0 → modern GOLD_SCALE =
 * {@code 0.35 * 16 = 5.6}. Hold-space rotations mirror {@link SliceItemRenderer} /
 * {@link HammyItemRenderer} (1.7 hand matrix does not drop into 1.21 ItemInHandLayer space).
 * <p>
 * Model pivot ≈ (0,-12,0) on parts; handle along Y via zRot π/2 box; grip at Y≈+1.+12 world.
 * <p>
 * Gold texture bind: {@code BattleAxetexture.png}.
 */
@OnlyIn(Dist.CLIENT)
public class BattleAxeItemRenderer extends BlockEntityWithoutLevelRenderer {
    /** Gold RenderBattleAxe: {@code new ResourceLocation("orespawn", "BattleAxetexture.png")}. */
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(
            "orespawn", "textures/entity/battleaxetexture.png");

    /**
     * Standalone inventory/ground icon model ({@code item/battle_axe_icon} → battleaxesmall).
     * Register via {@code ModelEvent.RegisterAdditional} and wire
     * {@code IClientItemExtensions}.
     */
    public static final ModelResourceLocation BATTLE_AXE_ICON = ModelResourceLocation.standalone(
            ResourceLocation.fromNamespaceAndPath("orespawn", "item/battle_axe_icon"));

    /**
     * Gold RenderBattleAxe scale 0.35 with model f5=1.0 → each model unit = 0.35 blocks.
     * ModelPart unit = 1/16 block → scale = 0.35 * 16 = 5.6.
     */
    private static final float GOLD_SCALE = 2.8F; // half of prior hold scale (user request)

    private ModelBattleAxe model;

    public BattleAxeItemRenderer() {
        super(
                Minecraft.getInstance().getBlockEntityRenderDispatcher(),
                Minecraft.getInstance().getEntityModels());
    }

    @Override
    public void onResourceManagerReload(ResourceManager resourceManager) {
        try {
            this.model = new ModelBattleAxe(ModelBattleAxe.createBodyLayer().bakeRoot());
        } catch (Exception e) {
            this.model = null;
        }
    }

    @Override
    public void renderByItem(
            ItemStack stack,
            ItemDisplayContext displayContext,
            PoseStack poseStack,
            MultiBufferSource buffer,
            int packedLight,
            int packedOverlay) {
        if (this.model == null) {
            this.onResourceManagerReload(Minecraft.getInstance().getResourceManager());
        }

        if (displayContext == ItemDisplayContext.GUI
                || displayContext == ItemDisplayContext.GROUND
                || displayContext == ItemDisplayContext.FIXED
                || displayContext == ItemDisplayContext.NONE
                || displayContext == ItemDisplayContext.HEAD) {
            renderFlatIcon(stack, poseStack, buffer, packedLight, packedOverlay);
            return;
        }

        if (this.model == null) {
            renderFlatIcon(stack, poseStack, buffer, packedLight, packedOverlay);
            return;
        }

        poseStack.pushPose();
        try {
            poseStack.translate(0.5F, 0.5F, 0.5F);

            boolean left = displayContext == ItemDisplayContext.FIRST_PERSON_LEFT_HAND
                    || displayContext == ItemDisplayContext.THIRD_PERSON_LEFT_HAND;

            if (displayContext.firstPerson()) {
                applyFirstPerson(poseStack, left);
            } else {
                applyThirdPerson(poseStack, left);
            }

            VertexConsumer consumer = ItemRenderer.getFoilBufferDirect(
                    buffer, RenderType.entityCutoutNoCull(TEXTURE), false, stack.hasFoil());
            this.model.renderToBuffer(poseStack, consumer, packedLight, packedOverlay, 0xFFFFFFFF);
        } finally {
            poseStack.popPose();
        }
    }

    private static void applyThirdPerson(PoseStack pose, boolean left) {
        float side = left ? -1.0F : 1.0F;

        pose.scale(1.0F, -1.0F, -1.0F);

        pose.mulPose(Axis.YP.rotationDegrees(side * 90.0F));
        pose.mulPose(Axis.ZP.rotationDegrees(side * -35.0F));

        pose.scale(GOLD_SCALE, GOLD_SCALE, GOLD_SCALE);
        pose.translate(-0.5F / 16.0F, 0.0F, -0.5F / 16.0F);
    }

    private static void applyFirstPerson(PoseStack pose, boolean left) {
        float side = left ? -1.0F : 1.0F;

        pose.scale(1.0F, -1.0F, -1.0F);

        pose.mulPose(Axis.YP.rotationDegrees(side * 90.0F));
        pose.mulPose(Axis.ZP.rotationDegrees(side * -25.0F));
        pose.mulPose(Axis.XP.rotationDegrees(40.0F));

        pose.scale(GOLD_SCALE, GOLD_SCALE, GOLD_SCALE);
        pose.translate(-0.5F / 16.0F, 0.0F, -0.5F / 16.0F);

        pose.translate(side * 0.05F, -0.55F, 0.08F);
    }

    private void renderFlatIcon(
            ItemStack stack,
            PoseStack poseStack,
            MultiBufferSource buffer,
            int light,
            int overlay) {
        Minecraft mc = Minecraft.getInstance();
        ItemRenderer ir = mc.getItemRenderer();
        BakedModel model = mc.getModelManager().getModel(BATTLE_AXE_ICON);
        if (model == null || model == mc.getModelManager().getMissingModel()) {
            return;
        }
        poseStack.pushPose();
        // ItemRenderer.translate(-0.5) before BEWLR; undo so item/generated icon is slot-centered.
        poseStack.translate(0.5F, 0.5F, 0.5F);
        ir.render(stack, ItemDisplayContext.NONE, false, poseStack, buffer, light, overlay, model);
        poseStack.popPose();
    }
}
