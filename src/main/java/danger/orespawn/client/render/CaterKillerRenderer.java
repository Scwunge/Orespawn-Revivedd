package danger.orespawn.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import danger.orespawn.client.model.ModelCaterKiller;
import danger.orespawn.entity.CaterKiller;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

/**
 * Gold {@code RenderCaterKiller}: ModelCaterKiller(0.22F), shadow 1.0*1.25, scale 1.25
 * (half when {@link CaterKiller#getPlayNicely()} != 0).
 * Texture gold {@code CaterKillertexture.png} → {@code caterkillertexture.png}.
 */
@OnlyIn(Dist.CLIENT)
public class CaterKillerRenderer extends MobRenderer<CaterKiller, ModelCaterKiller> {
    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath("orespawn", "textures/entity/caterkillertexture.png");
    /** Gold ClientProxy: {@code new RenderCaterKiller(new ModelCaterKiller(0.22F), 1.0F, 1.25F)}. */
    private static final float MODEL_SCALE = 1.25F;

    public CaterKillerRenderer(EntityRendererProvider.Context context) {
        super(
                context,
                new ModelCaterKiller(context.bakeLayer(ModelCaterKiller.LAYER_LOCATION), 0.22F),
                1.0F * MODEL_SCALE);
    }

    @Override
    protected void scale(CaterKiller entity, PoseStack poseStack, float partialTick) {
        // gold preRenderScale: PlayNicely != 0 → scale/2
        float s = entity.getPlayNicely() != 0 ? MODEL_SCALE / 2.0F : MODEL_SCALE;
        poseStack.scale(s, s, s);
    }

    @Override
    public ResourceLocation getTextureLocation(CaterKiller entity) {
        return TEXTURE;
    }
}
