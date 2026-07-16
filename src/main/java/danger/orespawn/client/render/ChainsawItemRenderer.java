package danger.orespawn.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import danger.orespawn.client.model.ModelChainsaw;
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
 * Chainsaw 3D hold model (1.7.10 {@code ModelChainsaw} / {@code RenderChainsaw}).
 * <p>
 * Gold has <b>no arm animations</b> — only draw-time transforms. Model itself animates chain
 * teeth + tip blade. Combat is UltimateSword family (AOE hit + wood multi-break).
 * Live item: {@code MyChainsaw = new UltimateSword(., toolCHAINSAW)}.
 * <p>
 * Critical 1.21 detail: {@link ItemRenderer} always does {@code translate(-0.5)} before
 * {@code renderByItem}. Undo with {@code translate(0.5)} so the grip sits on the hold point.
 * <p>
 * Gold {@code RenderChainsaw} scale is <b>0.25</b> with model f5=1.0 → modern GOLD_SCALE =
 * {@code 0.25 * 16 = 4.0} (playtest half → 2.0 for third person).
 * <p>
 * First person + GUI use flat {@code chainsawsmall} handheld icon; third person keeps the 3D mesh.
 * <p>
 * Gold texture bind: {@code Chainsawtexture.png}.
 */
@OnlyIn(Dist.CLIENT)
public class ChainsawItemRenderer extends BlockEntityWithoutLevelRenderer {
    /** Gold RenderChainsaw: {@code new ResourceLocation("orespawn", "Chainsawtexture.png")}. */
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(
            "orespawn", "textures/entity/chainsawtexture.png");

    /**
     * Standalone inventory/ground icon model ({@code item/chainsaw_icon} → chainsawsmall).
     * Register via {@code ModelEvent.RegisterAdditional} and wire
     * {@code IClientItemExtensions}.
     */
    public static final ModelResourceLocation CHAINSAW_ICON = ModelResourceLocation.standalone(
            ResourceLocation.fromNamespaceAndPath("orespawn", "item/chainsaw_icon"));

    /**
     * Gold RenderChainsaw scale 0.25 with model f5=1.0 → each model unit = 0.25 blocks.
     * ModelPart unit = 1/16 block → scale = 0.25 * 16 = 4.0.
     */
    private static final float GOLD_SCALE = 2.0F; // half of prior hold scale (user request)

    private ModelChainsaw model;

    public ChainsawItemRenderer() {
        super(
                Minecraft.getInstance().getBlockEntityRenderDispatcher(),
                Minecraft.getInstance().getEntityModels());
    }

    @Override
    public void onResourceManagerReload(ResourceManager resourceManager) {
        try {
            this.model = new ModelChainsaw(ModelChainsaw.createBodyLayer().bakeRoot());
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

        // First person + GUI/ground: flat chainsawsmall. Third person: 3D mesh.
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

    /**
     * Third person: blade tip −Z out front, red handles +Z behind player, hand-locked.
     */
    private static void applyThirdPerson(PoseStack pose, boolean left) {
        float side = left ? -1.0F : 1.0F;

        pose.scale(1.0F, -1.0F, -1.0F);
        pose.mulPose(Axis.XP.rotationDegrees(-90.0F));
        pose.mulPose(Axis.YP.rotationDegrees(side * 90.0F));
        pose.mulPose(Axis.ZP.rotationDegrees(side * -25.0F));

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
        BakedModel model = mc.getModelManager().getModel(CHAINSAW_ICON);
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
