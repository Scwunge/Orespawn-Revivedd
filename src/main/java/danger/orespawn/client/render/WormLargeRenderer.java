package danger.orespawn.client.render;

import danger.orespawn.client.model.ModModelLayers;
import danger.orespawn.client.model.ModelWormLarge;
import danger.orespawn.entity.WormLarge;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class WormLargeRenderer extends MobRenderer<WormLarge, ModelWormLarge> {
    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath("orespawn", "textures/entity/wormlargetexture.png");

    public WormLargeRenderer(EntityRendererProvider.Context context) {
        super(context, new ModelWormLarge(context.bakeLayer(ModModelLayers.WORM_LARGE)), 0.0F);
    }

    @Override
    public ResourceLocation getTextureLocation(WormLarge entity) {
        return TEXTURE;
    }
}
