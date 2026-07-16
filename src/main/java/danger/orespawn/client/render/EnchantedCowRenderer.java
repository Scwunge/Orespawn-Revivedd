package danger.orespawn.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import danger.orespawn.entity.EnchantedCow;
import net.minecraft.client.model.CowModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

/**
 * Gold {@code RenderEnchantedCow} for EnchantedCow: ModelCow, shadow 0.7,
 * texture {@code gold_cow.png} (same as GoldCow) + enchant glint pass
 * (gold {@code shouldRenderPass} when instanceof EnchantedCow & pass==3 → return 31).
 */
@OnlyIn(Dist.CLIENT)
public class EnchantedCowRenderer extends MobRenderer<EnchantedCow, CowModel<EnchantedCow>> {
    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath("orespawn", "textures/entity/gold_cow.png");

    public EnchantedCowRenderer(EntityRendererProvider.Context context) {
        super(context, new CowModel<>(context.bakeLayer(ModelLayers.COW)), 0.7F);
        this.addLayer(new EnchantedCowGlintLayer(this));
    }

    @Override
    public ResourceLocation getTextureLocation(EnchantedCow entity) {
        return TEXTURE;
    }

    /** Gold multi-pass enchant glint overlay on the cow model. */
    @OnlyIn(Dist.CLIENT)
    private static class EnchantedCowGlintLayer extends RenderLayer<EnchantedCow, CowModel<EnchantedCow>> {
        EnchantedCowGlintLayer(RenderLayerParent<EnchantedCow, CowModel<EnchantedCow>> parent) {
            super(parent);
        }

        @Override
        public void render(
                PoseStack poseStack,
                MultiBufferSource buffer,
                int packedLight,
                EnchantedCow entity,
                float limbSwing,
                float limbSwingAmount,
                float partialTick,
                float ageInTicks,
                float netHeadYaw,
                float headPitch) {
            // gold return 31 → re-render living with enchantment glint
            VertexConsumer consumer = buffer.getBuffer(RenderType.entityGlint());
            this.getParentModel()
                    .renderToBuffer(poseStack, consumer, packedLight, OverlayTexture.NO_OVERLAY);
        }
    }
}
