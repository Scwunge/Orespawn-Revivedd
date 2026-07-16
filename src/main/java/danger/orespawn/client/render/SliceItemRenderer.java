package danger.orespawn.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import danger.orespawn.client.model.ModelSlice;
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
 * Slice 3D hold model (1.7.10 {@code ModelSlice} / {@code RenderSlice}).
 * <p>
 * Gold has <b>no arm animations</b> — only draw-time transforms. Swing VFX is {@code BerthaHit}
 * (MySlice is registered as Bertha combat class).
 * <p>
 * Critical 1.21 detail: {@link ItemRenderer} always does {@code translate(-0.5)} before
 * {@code renderByItem}. Undo with {@code translate(0.5)} so the grip sits on the hold point.
 * <p>
 * Gold {@code RenderSlice} scale is <b>0.3</b> (Bertha is 0.25) with model f5=1.0 →
 * modern GOLD_SCALE = {@code 0.3 * 16 = 4.8}. Hold-space rotations mirror
 * {@link BerthaItemRenderer} (1.7 hand matrix does not drop into 1.21 ItemInHandLayer space).
 * <p>
 * Model origin ≈ grip center Y=0; handguard Y≈-7.-9; blade tip Y≈-41; pommel Y≈+6.
 */
@OnlyIn(Dist.CLIENT)
public class SliceItemRenderer extends BlockEntityWithoutLevelRenderer {
    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath("orespawn", "textures/entity/slicetexture.png");

    /**
     * Standalone inventory/ground icon model ({@code item/slice_icon} → slicesmall).
     * Register via {@code ModelEvent.RegisterAdditional} and wire
     * {@code IClientItemExtensions}.
     */
    public static final ModelResourceLocation SLICE_ICON = ModelResourceLocation.standalone(
            ResourceLocation.fromNamespaceAndPath("orespawn", "item/slice_icon"));

    /**
     * Gold RenderSlice scale 0.3 with model f5=1.0 → each model unit = 0.3 blocks.
     * ModelPart unit = 1/16 block → scale = 0.3 * 16 = 4.8.
     */
    /** Third-person only — gold huge scale. First person uses flat slicesmall icon. */
    private static final float GOLD_SCALE = 4.8F;

    private ModelSlice model;

    public SliceItemRenderer() {
        super(
                Minecraft.getInstance().getBlockEntityRenderDispatcher(),
                Minecraft.getInstance().getEntityModels());
    }

    @Override
    public void onResourceManagerReload(ResourceManager resourceManager) {
        try {
            // Bake mesh directly so this file does not depend on ModModelLayers edits.
            // Optionally register ModModelLayers.SLICE for consistency .
            this.model = new ModelSlice(ModelSlice.createBodyLayer().bakeRoot());
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

        // First person + GUI/ground: flat slicesmall. Third person: huge 3D mesh.
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
        BakedModel model = mc.getModelManager().getModel(SLICE_ICON);
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
