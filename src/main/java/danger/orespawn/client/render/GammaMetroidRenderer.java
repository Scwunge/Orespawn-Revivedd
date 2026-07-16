package danger.orespawn.client.render;

import danger.orespawn.client.model.ModModelLayers;
import danger.orespawn.client.model.ModelGammaMetroid;
import danger.orespawn.entity.GammaMetroid;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class GammaMetroidRenderer extends MobRenderer<GammaMetroid, ModelGammaMetroid> {
    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath("orespawn", "textures/entity/gammametroid.png");

    public GammaMetroidRenderer(EntityRendererProvider.Context context) {
        super(context, new ModelGammaMetroid(context.bakeLayer(ModModelLayers.GAMMAMETROID), 1F), 0F);
    }

    @Override
    public ResourceLocation getTextureLocation(GammaMetroid entity) {
        return TEXTURE;
    }
}
