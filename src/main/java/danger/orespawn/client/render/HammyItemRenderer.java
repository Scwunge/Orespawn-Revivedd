package danger.orespawn.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import danger.orespawn.client.model.ModelHammy;
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
 * Hammy 3D hold model (1.7.10 {@code ModelHammy} / {@code RenderHammy}).
 * <p>
 * Gold has <b>no arm animations</b> — only draw-time transforms. Swing VFX is {@code BerthaHit}
 * hit_type 3 (MyHammy is registered as Bertha combat class with toolHAMMY).
 * <p>
 * Critical 1.21 detail: {@link ItemRenderer} always does {@code translate(-0.5)} before
 * {@code renderByItem}. Undo with {@code translate(0.5)} so the grip sits on the hold point.
 * <p>
 * Gold {@code RenderHammy} scale is <b>0.15</b> with model f5=1.0 → modern GOLD_SCALE =
 * {@code 0.15 * 16 = 2.4}. Hold-space rotations mirror {@link SliceItemRenderer} /
 * {@link BerthaItemRenderer} (1.7 hand matrix does not drop into 1.21 ItemInHandLayer space).
 * <p>
 * Model origin ≈ grip center Y=0; handle Y=-12.+24; head mass around Y≈-12.-29.
 * <p>
 * Gold texture bind: {@code AttitudeAdjustertexture.png} (also shipped as
 * {@code Hammytexture.png} — renderer follows gold ResourceLocation).
 */
@OnlyIn(Dist.CLIENT)
public class HammyItemRenderer extends BlockEntityWithoutLevelRenderer {
    /** Gold RenderHammy: {@code new ResourceLocation("orespawn", "AttitudeAdjustertexture.png")}. */
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(
            "orespawn", "textures/entity/attitudeadjustertexture.png");

    /**
     * Standalone inventory/ground icon model ({@code item/hammy_icon} → hammysmall).
     * Register via {@code ModelEvent.RegisterAdditional} and wire
     * {@code IClientItemExtensions}.
     */
    public static final ModelResourceLocation HAMMY_ICON = ModelResourceLocation.standalone(
            ResourceLocation.fromNamespaceAndPath("orespawn", "item/hammy_icon"));

    /**
     * Gold RenderHammy scale 0.15 with model f5=1.0 → each model unit = 0.15 blocks.
     * ModelPart unit = 1/16 block → scale = 0.15 * 16 = 2.4.
     */
    private static final float GOLD_SCALE = 1.2F; // half of prior hold scale (user request)

    private ModelHammy model;

    public HammyItemRenderer() {
        super(
                Minecraft.getInstance().getBlockEntityRenderDispatcher(),
                Minecraft.getInstance().getEntityModels());
    }

    @Override
    public void onResourceManagerReload(ResourceManager resourceManager) {
        try {
            // Bake mesh directly so this file does not depend on ModModelLayers edits.
            // Optionally register ModModelLayers.HAMMY for consistency .
            this.model = new ModelHammy(ModelHammy.createBodyLayer().bakeRoot());
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

        // Gold only drew EQUIPPED + first-person; GUI/ground/fixed use flat hammysmall icon.
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
            // ItemRenderer always: pose.translate(-0.5F, -0.5F, -0.5F) before BEWLR.
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
        BakedModel model = mc.getModelManager().getModel(HAMMY_ICON);
        if (model == null || model == mc.getModelManager().getMissingModel()) {
            return;
        }
        // Use the incoming stack so we do not hard-depend on ModItems.HAMMY before full item registration.
        // Icon model is item/generated (not builtin/entity) → no BEWLR recursion.
        poseStack.pushPose();
        // ItemRenderer.translate(-0.5) before BEWLR; undo so item/generated icon is slot-centered.
        poseStack.translate(0.5F, 0.5F, 0.5F);
        ir.render(stack, ItemDisplayContext.NONE, false, poseStack, buffer, light, overlay, model);
        poseStack.popPose();
    }
}
