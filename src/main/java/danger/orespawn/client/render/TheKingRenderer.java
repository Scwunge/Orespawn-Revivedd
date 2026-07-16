package danger.orespawn.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import danger.orespawn.client.model.ModelTheKing;
import danger.orespawn.entity.TheKing;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

/**
 * Gold {@code RenderTheKing}: ModelTheKing(0.65F), shadow 1.9*2.1, scale 2.1
 * (scale/4 when {@link TheKing#getPlayNicely()} != 0).
 * Texture gold {@code TheKingtexture.png} → {@code textures/entity/thekingtexture.png}.
 */
@OnlyIn(Dist.CLIENT)
public class TheKingRenderer extends MobRenderer<TheKing, ModelTheKing> {
    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath("orespawn", "textures/entity/thekingtexture.png");

    /** Gold RenderTheKing scale (par3). */
    private static final float MODEL_SCALE = 2.1F;

    public TheKingRenderer(EntityRendererProvider.Context context) {
        super(
                context,
                new ModelTheKing(context.bakeLayer(ModelTheKing.LAYER_LOCATION), 0.65F),
                1.9F * MODEL_SCALE);
    }

    /**
     * Gold preRenderScale + LER body-offset cancel so huge scale 2.1 doesn't
     * float feet above ground (vanilla always translates −1.501 after scale).
     */
    @Override
    protected void scale(TheKing entity, PoseStack poseStack, float partialTick) {
        float s = entity.getPlayNicely() != 0 ? MODEL_SCALE / 4.0F : MODEL_SCALE;
        final float ler = 1.501F;
        poseStack.translate(0.0F, -ler * (s - 1.0F), 0.0F);
        poseStack.scale(s, s, s);
    }

    @Override
    public ResourceLocation getTextureLocation(TheKing entity) {
        return TEXTURE;
    }
}
