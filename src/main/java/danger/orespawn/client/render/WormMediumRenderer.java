package danger.orespawn.client.render;

import danger.orespawn.client.model.ModModelLayers;
import danger.orespawn.client.model.ModelWormMedium;
import danger.orespawn.entity.WormMedium;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class WormMediumRenderer extends MobRenderer<WormMedium, ModelWormMedium> {
    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath("orespawn", "textures/entity/wormmediumtexture.png");

    public WormMediumRenderer(EntityRendererProvider.Context context) {
        super(context, new ModelWormMedium(context.bakeLayer(ModModelLayers.WORM_MEDIUM)), 0.0F);
    }

    @Override
    public ResourceLocation getTextureLocation(WormMedium entity) {
        return TEXTURE;
    }
}
