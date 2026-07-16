package danger.orespawn.client.render;

import danger.orespawn.client.model.ModModelLayers;
import danger.orespawn.client.model.ModelWormSmall;
import danger.orespawn.entity.WormSmall;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class WormSmallRenderer extends MobRenderer<WormSmall, ModelWormSmall> {
    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath("orespawn", "textures/entity/wormsmalltexture.png");

    public WormSmallRenderer(EntityRendererProvider.Context context) {
        super(context, new ModelWormSmall(context.bakeLayer(ModModelLayers.WORM_SMALL)), 0.0F);
    }

    @Override
    public ResourceLocation getTextureLocation(WormSmall entity) {
        return TEXTURE;
    }
}
