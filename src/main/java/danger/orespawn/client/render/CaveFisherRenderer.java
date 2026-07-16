package danger.orespawn.client.render;

import danger.orespawn.client.model.ModModelLayers;
import danger.orespawn.client.model.ModelCaveFisher;
import danger.orespawn.entity.CaveFisher;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class CaveFisherRenderer extends MobRenderer<CaveFisher, ModelCaveFisher> {
    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath("orespawn", "textures/entity/cavefisher.png");

    public CaveFisherRenderer(EntityRendererProvider.Context context) {
        super(context, new ModelCaveFisher(context.bakeLayer(ModModelLayers.CAVEFISHER), 1F), 0F);
    }

    @Override
    public ResourceLocation getTextureLocation(CaveFisher entity) {
        return TEXTURE;
    }
}
