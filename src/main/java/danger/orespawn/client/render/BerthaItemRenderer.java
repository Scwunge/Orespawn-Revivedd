package danger.orespawn.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import danger.orespawn.client.model.ModelBertha;
import danger.orespawn.client.model.ModModelLayers;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.EntityModelSet;
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
 * Big Bertha 3D hold model (1.7.10 {@code ModelBertha} / {@code RenderBertha}).
 * <p>
 * Gold has <b>no arm animations</b> — only draw-time transforms. Swing VFX is {@code BerthaHit}.
 * <p>
 * Critical 1.21 detail: {@link ItemRenderer} always does {@code translate(-0.5)} before
 * {@code renderByItem} (baked-model cube centering). Origin-centered models like Bertha must
 * undo that or the sword sits half a block off the palm (screenshots: black shaft through chest).
 * <p>
 * Gold {@code RenderBertha} numbers (scale 0.25 + f5=1.0 + large model-space translates) are
 * for the 1.7 hand matrix + Forge helpers — they do <b>not</b> drop into 1.21
 * {@code ItemInHandLayer} space. We keep gold visual scale ({@code 0.25 * 16 = 4}) and put the
 * grip (model Y≈0) at the modern hold point.
 * <p>
 * Model: black grip Y=-6.+6, handguard Y≈-7, blade tip Y≈-43, pommel Y≈+6.
 */
@OnlyIn(Dist.CLIENT)
public class BerthaItemRenderer extends BlockEntityWithoutLevelRenderer {
    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath("orespawn", "textures/entity/bertha.png");

    /** Flat *small icon for first person + GUI (handheld parent = normal sword size). */
    public static final ModelResourceLocation BERTHA_ICON = ModelResourceLocation.standalone(
            ResourceLocation.fromNamespaceAndPath("orespawn", "item/bertha_icon"));

    /**
     * Third-person only — gold huge scale (0.25 * 16 = 4). First person uses flat icon.
     */
    private static final float GOLD_SCALE = 4.0F;

    private ModelBertha model;

    public BerthaItemRenderer() {
        super(
                Minecraft.getInstance().getBlockEntityRenderDispatcher(),
                Minecraft.getInstance().getEntityModels());
    }

    @Override
    public void onResourceManagerReload(ResourceManager resourceManager) {
        try {
            EntityModelSet models = Minecraft.getInstance().getEntityModels();
            this.model = new ModelBertha(models.bakeLayer(ModModelLayers.BERTHA));
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

        // First person + GUI/ground: flat berthasmall (normal sword size). Third person: huge 3D.
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
            // ItemRenderer always: pose.translate(-0.5F, -0.5F, -0.5F) before BEWLR.
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

    /** Third person only — huge gold-scale mesh, locked to hand. */
    private static void applyThirdPerson(PoseStack pose, boolean left) {
        float side = left ? -1.0F : 1.0F;

        pose.scale(1.0F, -1.0F, -1.0F);
        pose.mulPose(Axis.YP.rotationDegrees(side * 90.0F));
        pose.mulPose(Axis.ZP.rotationDegrees(side * -35.0F));

        pose.scale(GOLD_SCALE, GOLD_SCALE, GOLD_SCALE);
        pose.translate(-0.5F / 16.0F, 0.0F, -0.5F / 16.0F);
    }

    /**
     * Flat {@code berthasmall} handheld icon. GUI uses NONE (slot-centered); first person
     * reuses the FP display context so vanilla sword hold transforms apply.
     */
    private void renderFlatIcon(
            ItemStack stack,
            PoseStack poseStack,
            MultiBufferSource buffer,
            int light,
            int overlay,
            ItemDisplayContext displayContext) {
        Minecraft mc = Minecraft.getInstance();
        ItemRenderer ir = mc.getItemRenderer();
        BakedModel model = mc.getModelManager().getModel(BERTHA_ICON);
        if (model == null || model == mc.getModelManager().getMissingModel()) {
            return;
        }
        boolean left = displayContext == ItemDisplayContext.FIRST_PERSON_LEFT_HAND
                || displayContext == ItemDisplayContext.THIRD_PERSON_LEFT_HAND;
        // For GUI/ground, NONE + center undo; for FP, pass through the hand context.
        ItemDisplayContext ctx = displayContext.firstPerson() ? displayContext : ItemDisplayContext.NONE;
        poseStack.pushPose();
        poseStack.translate(0.5F, 0.5F, 0.5F);
        ir.render(stack, ctx, left, poseStack, buffer, light, overlay, model);
        poseStack.popPose();
    }
}
