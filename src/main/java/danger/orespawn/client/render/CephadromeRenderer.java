package danger.orespawn.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import danger.orespawn.client.model.ModelCephadrome;
import danger.orespawn.entity.Cephadrome;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

/**
 * Gold {@code RenderCephadrome}: ModelCephadrome(0.55F), shadow 1.25*1.0, scale 1.0.
 * Texture: {@code textures/entity/cephadrome.png}.
 */
@OnlyIn(Dist.CLIENT)
public class CephadromeRenderer extends MobRenderer<Cephadrome, ModelCephadrome> {
    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath("orespawn", "textures/entity/cephadrome.png");

    public CephadromeRenderer(EntityRendererProvider.Context context) {
        // gold ClientProxy: new RenderCephadrome(new ModelCephadrome(0.55F), 1.25F, 1.0F)
        super(context, new ModelCephadrome(context.bakeLayer(ModelCephadrome.LAYER_LOCATION), 0.55F), 1.25F);
    }

    @Override
    protected void scale(Cephadrome entity, PoseStack poseStack, float partialTick) {
        // gold scale 1.0F
        poseStack.scale(1.0F, 1.0F, 1.0F);
    }

    @Override
    public ResourceLocation getTextureLocation(Cephadrome entity) {
        return TEXTURE;
    }
}
