package danger.orespawn.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import danger.orespawn.client.model.ModelCockateil;
import danger.orespawn.entity.Cockateil;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

/**
 * Gold {@code RenderCockateil}: ModelCockateil(1.0F), shadow 0.3, scale 0.75.
 * Textures from gold Cockateil.getTexture(): Bird1.Bird6 → textures/entity/bird1.bird6.png
 * (birdtype 0→bird1 … 5→bird6).
 */
@OnlyIn(Dist.CLIENT)
public class CockateilRenderer extends MobRenderer<Cockateil, ModelCockateil<Cockateil>> {
    private static final ResourceLocation[] TEXTURES = {
            ResourceLocation.fromNamespaceAndPath("orespawn", "textures/entity/bird1.png"),
            ResourceLocation.fromNamespaceAndPath("orespawn", "textures/entity/bird2.png"),
            ResourceLocation.fromNamespaceAndPath("orespawn", "textures/entity/bird3.png"),
            ResourceLocation.fromNamespaceAndPath("orespawn", "textures/entity/bird4.png"),
            ResourceLocation.fromNamespaceAndPath("orespawn", "textures/entity/bird5.png"),
            ResourceLocation.fromNamespaceAndPath("orespawn", "textures/entity/bird6.png"),
    };

    /** Gold ClientProxy par3 scale */
    private static final float ADULT_SCALE = 0.75F;

    public CockateilRenderer(EntityRendererProvider.Context context) {
        super(
                context,
                new ModelCockateil<>(context.bakeLayer(ModelCockateil.LAYER_LOCATION), 1.0F),
                0.3F * ADULT_SCALE);
    }

    @Override
    protected void scale(Cockateil entity, PoseStack poseStack, float partialTick) {
        poseStack.scale(ADULT_SCALE, ADULT_SCALE, ADULT_SCALE);
    }

    @Override
    public ResourceLocation getTextureLocation(Cockateil entity) {
        // gold Cockateil.getTexture: case 0.5 → Bird1.Bird6
        int t = entity.getBirdType();
        if (t >= 0 && t < TEXTURES.length) {
            return TEXTURES[t];
        }
        return TEXTURES[0];
    }
}
