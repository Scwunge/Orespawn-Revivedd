package danger.orespawn.client.render;

import danger.orespawn.client.model.ModModelLayers;
import danger.orespawn.client.model.ModelSpyro;
import danger.orespawn.entity.Spyro;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class SpyroRenderer extends MobRenderer<Spyro, ModelSpyro> {
    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath("orespawn", "textures/entity/spyrotexture.png");

    public SpyroRenderer(EntityRendererProvider.Context context) {
        super(context, new ModelSpyro(context.bakeLayer(ModModelLayers.SPYRO), 1F), 0F);
    }

    @Override
    public ResourceLocation getTextureLocation(Spyro entity) {
        return TEXTURE;
    }
}
