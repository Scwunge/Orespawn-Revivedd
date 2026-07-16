package danger.orespawn.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import danger.orespawn.client.model.ModelRoyal;
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
 * Royal 3D hold model (1.7.10 {@code RenderRoyal} + {@code ModelSlice} mesh).
 * <p>
 * Gold has <b>no arm animations</b> — only draw-time transforms. Swing VFX is {@code BerthaHit}
 * hit_type 2 (MyRoyal is registered as Bertha combat class with toolROYAL).
 * <p>
 * Critical 1.21 detail: {@link ItemRenderer} always does {@code translate(-0.5)} before
 * {@code renderByItem}. Undo with {@code translate(0.5)} so the grip sits on the hold point.
 * <p>
 * Gold {@code RenderRoyal} scale is <b>0.35</b> (Slice is 0.3, Bertha is 0.25) with model
 * f5=1.0 → modern GOLD_SCALE = {@code 0.35 * 16 = 5.6}. Hold-space rotations mirror
 * {@link SliceItemRenderer} / {@link BerthaItemRenderer} (1.7 hand matrix does not drop into
 * 1.21 ItemInHandLayer space).
 * <p>
 * Model origin ≈ grip center Y=0; handguard Y≈-7.-9; blade tip Y≈-41; pommel Y≈+6.
 * Texture: gold {@code Royaltexture.png} → {@code textures/entity/royaltexture.png}.
 */
@OnlyIn(Dist.CLIENT)
public class RoyalItemRenderer extends BlockEntityWithoutLevelRenderer {
    /** Gold RenderRoyal: {@code new ResourceLocation("orespawn", "Royaltexture.png")}. */
    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath("orespawn", "textures/entity/royaltexture.png");

    /**
     * Standalone inventory/ground icon model ({@code item/royal_icon} → royalsmall).
     * Register via {@code ModelEvent.RegisterAdditional} and wire
     * {@code IClientItemExtensions}.
     */
    public static final ModelResourceLocation ROYAL_ICON = ModelResourceLocation.standalone(
            ResourceLocation.fromNamespaceAndPath("orespawn", "item/royal_icon"));

    /**
     * Gold RenderRoyal scale 0.35 with model f5=1.0 → each model unit = 0.35 blocks.
     * ModelPart unit = 1/16 block → scale = 0.35 * 16 = 5.6.
     */
    /** Third-person only — gold huge scale. First person uses flat royalsmall icon. */
    private static final float GOLD_SCALE = 5.6F;

    private ModelRoyal model;

    public RoyalItemRenderer() {
        super(
                Minecraft.getInstance().getBlockEntityRenderDispatcher(),
                Minecraft.getInstance().getEntityModels());
    }

    @Override
    public void onResourceManagerReload(ResourceManager resourceManager) {
        try {
            // Bake mesh directly so this file does not depend on ModModelLayers edits.
            // Optionally register ModModelLayers.ROYAL for consistency .
            this.model = new ModelRoyal(ModelRoyal.createBodyLayer().bakeRoot());
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

        // First person + GUI/ground: flat royalsmall. Third person: huge 3D mesh.
        if (displayContext.firstPerson()
                || displayContext == ItemDisplayContext.GUI
                || displayContext == ItemDisplayContext.GROUND
                || displayContext == ItemDisplayContext.FIXED
                || displayContext == ItemDisplayContext.NONE
                || displayContext == ItemDisplayContext.HEAD) {
            renderFlatIcon(stack, poseStack, buffer, packedLight, packedOverlay, displayContext);
            return;
        }

        if (this.model == null) {
            renderFlatIcon(stack, poseStack, buffer, packedLight, packedOverlay, displayContext);
            return;
        }

        poseStack.pushPose();
        try {
            poseStack.translate(0.5F, 0.5F, 0.5F);
            boolean left = displayContext == ItemDisplayContext.THIRD_PERSON_LEFT_HAND;
            applyThirdPerson(poseStack, left);

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

    private void renderFlatIcon(
            ItemStack stack,
            PoseStack poseStack,
            MultiBufferSource buffer,
            int light,
            int overlay,
            ItemDisplayContext displayContext) {
        Minecraft mc = Minecraft.getInstance();
        ItemRenderer ir = mc.getItemRenderer();
        BakedModel model = mc.getModelManager().getModel(ROYAL_ICON);
        if (model == null || model == mc.getModelManager().getMissingModel()) {
            return;
        }
        boolean left = displayContext == ItemDisplayContext.FIRST_PERSON_LEFT_HAND
                || displayContext == ItemDisplayContext.THIRD_PERSON_LEFT_HAND;
        ItemDisplayContext ctx = displayContext.firstPerson() ? displayContext : ItemDisplayContext.NONE;
        poseStack.pushPose();
        poseStack.translate(0.5F, 0.5F, 0.5F);
        ir.render(stack, ctx, left, poseStack, buffer, light, overlay, model);
        poseStack.popPose();
    }
}
