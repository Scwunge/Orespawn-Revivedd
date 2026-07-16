package danger.orespawn.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import danger.orespawn.client.model.ModelSquidZooka;
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
 * SquidZooka 3D hold model (1.7.10 {@code ModelSquidZooka} / {@code RenderSquidZooka}).
 * <p>
 * Gold has <b>no arm animations</b> — only draw-time transforms. Combat is right-click
 * AttackSquid launch (see {@link danger.orespawn.items.tools.SquidZooka}).
 * <p>
 * Critical 1.21 detail: {@link ItemRenderer} always does {@code translate(-0.5)} before
 * {@code renderByItem}. Undo with {@code translate(0.5)} so the grip sits on the hold point.
 * <p>
 * Gold {@code RenderSquidZooka} scale is <b>0.35</b> with model f5=1.0 → modern GOLD_SCALE =
 * {@code 0.35 * 16 = 5.6}. Hold-space rotations mirror {@link BattleAxeItemRenderer} /
 * {@link SliceItemRenderer} (1.7 hand matrix does not drop into 1.21 ItemInHandLayer space).
 * <p>
 * Gold texture bind: {@code SquidZookatexture.png}.
 */
@OnlyIn(Dist.CLIENT)
public class SquidZookaItemRenderer extends BlockEntityWithoutLevelRenderer {
    /** Gold RenderSquidZooka: {@code new ResourceLocation("orespawn", "SquidZookatexture.png")}. */
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(
            "orespawn", "textures/entity/squidzookatexture.png");

    /**
     * Standalone inventory/ground icon model ({@code item/squid_zooka_icon} → squidzookasmall).
     * Register via {@code ModelEvent.RegisterAdditional} and wire
     * {@code IClientItemExtensions}.
     */
    public static final ModelResourceLocation SQUID_ZOOKA_ICON = ModelResourceLocation.standalone(
            ResourceLocation.fromNamespaceAndPath("orespawn", "item/squid_zooka_icon"));

    /**
     * Gold RenderSquidZooka scale 0.35 with model f5=1.0 → each model unit = 0.35 blocks.
     * ModelPart unit = 1/16 block → scale = 0.35 * 16 = 5.6. Playtest half → 2.8.
     * <p>
     * Model barrel is along <b>Z</b> (muzzle −Z, blue tail +Z). Third-person and first-person
     * use <b>separate</b> hold stacks (ItemInHandLayer matrices differ).
     */
    private static final float GOLD_SCALE = 2.8F; // half of prior hold scale (user request)

    private ModelSquidZooka model;

    public SquidZookaItemRenderer() {
        super(
                Minecraft.getInstance().getBlockEntityRenderDispatcher(),
                Minecraft.getInstance().getEntityModels());
    }

    @Override
    public void onResourceManagerReload(ResourceManager resourceManager) {
        try {
            this.model = new ModelSquidZooka(ModelSquidZooka.createBodyLayer().bakeRoot());
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

        // Gold only drew EQUIPPED + first-person; GUI/ground/fixed use flat squidzookasmall icon.
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

    /**
     * Third person: blue flare behind player, barrel pointed forward, still hand-locked.
     * <p>
     * Model muzzle −Z / blue +Z; model also Z-180s in render. Hold-space scale flips Z.
     * X−90 (TP hand matrix — not the same as first person) puts muzzle on −Y out from fist
     * and blue on +Y toward the body.
     */
    private static void applyThirdPerson(PoseStack pose, boolean left) {
        float side = left ? -1.0F : 1.0F;

        // Locked to ItemInHandLayer third-person hand hold point
        pose.scale(1.0F, -1.0F, -1.0F);

        // TP-only barrel map (do not copy into first person)
        pose.mulPose(Axis.XP.rotationDegrees(-90.0F));

        pose.mulPose(Axis.YP.rotationDegrees(side * 90.0F));
        pose.mulPose(Axis.ZP.rotationDegrees(side * -25.0F));

        pose.scale(GOLD_SCALE, GOLD_SCALE, GOLD_SCALE);
        pose.translate(-0.5F / 16.0F, 0.0F, -0.5F / 16.0F);
    }

    /**
     * First person: prior gun-in-hand pose. FP matrix ≠ TP — keep this stack independent.
     */
    private static void applyFirstPerson(PoseStack pose, boolean left) {
        float side = left ? -1.0F : 1.0F;

        pose.scale(1.0F, -1.0F, -1.0F);

        pose.mulPose(Axis.YP.rotationDegrees(side * 90.0F));
        pose.mulPose(Axis.ZP.rotationDegrees(side * -25.0F));
        pose.mulPose(Axis.XP.rotationDegrees(30.0F));

        pose.scale(GOLD_SCALE, GOLD_SCALE, GOLD_SCALE);
        pose.translate(-0.5F / 16.0F, 0.0F, -0.5F / 16.0F);

        pose.translate(side * 0.05F, -0.35F, 0.12F);
    }

    private void renderFlatIcon(
            ItemStack stack,
            PoseStack poseStack,
            MultiBufferSource buffer,
            int light,
            int overlay) {
        Minecraft mc = Minecraft.getInstance();
        ItemRenderer ir = mc.getItemRenderer();
        BakedModel model = mc.getModelManager().getModel(SQUID_ZOOKA_ICON);
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
