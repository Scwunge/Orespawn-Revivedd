package danger.orespawn.client.render;

import danger.orespawn.client.model.ModModelLayers;
import danger.orespawn.client.model.ModelAlosaurus;
import danger.orespawn.entity.Alosaurus;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class AlosaurusRenderer extends MobRenderer<Alosaurus, ModelAlosaurus> {
    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath("orespawn", "textures/entity/alosaurus.png");

    public AlosaurusRenderer(EntityRendererProvider.Context context) {
        super(context, new ModelAlosaurus(context.bakeLayer(ModModelLayers.ALOSAURUS), 1.5F), 0.0F);
    }

    @Override
    public ResourceLocation getTextureLocation(Alosaurus entity) {
        return TEXTURE;
    }
}
