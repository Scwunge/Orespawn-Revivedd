package danger.orespawn.client.render;

import danger.orespawn.client.model.ModModelLayers;
import danger.orespawn.client.model.ModelNastysaurus;
import danger.orespawn.entity.Nastysaurus;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class NastysaurusRenderer extends MobRenderer<Nastysaurus, ModelNastysaurus> {
    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath("orespawn", "textures/entity/nastysaurus.png");

    public NastysaurusRenderer(EntityRendererProvider.Context context) {
        super(context, new ModelNastysaurus(context.bakeLayer(ModModelLayers.NASTYSAURUS), 1.5F), 0F);
    }

    @Override
    public ResourceLocation getTextureLocation(Nastysaurus entity) {
        return TEXTURE;
    }
}
