package danger.orespawn.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import danger.orespawn.client.model.ModelCoin;
import danger.orespawn.entity.Coin;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

/**
 * Gold {@code RenderCoin}: ModelCoin(0.22F), shadow 0.75*0.125, scale 0.125.
 * Texture: cointexture.png
 */
@OnlyIn(Dist.CLIENT)
public class CoinRenderer extends MobRenderer<Coin, ModelCoin> {
    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath("orespawn", "textures/entity/cointexture.png");
    /** Gold ClientProxy: {@code new RenderCoin(new ModelCoin(0.22F), 0.75F, 0.125F)}. */
    private static final float MODEL_SCALE = 0.125F;

    public CoinRenderer(EntityRendererProvider.Context context) {
        super(
                context,
                new ModelCoin(context.bakeLayer(ModelCoin.LAYER_LOCATION), 0.22F),
                0.75F * MODEL_SCALE);
    }

    @Override
    protected void scale(Coin entity, PoseStack poseStack, float partialTick) {
        poseStack.scale(MODEL_SCALE, MODEL_SCALE, MODEL_SCALE);
    }

    @Override
    public ResourceLocation getTextureLocation(Coin entity) {
        return TEXTURE;
    }
}
