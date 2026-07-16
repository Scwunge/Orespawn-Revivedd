package danger.orespawn.client.render;

import danger.orespawn.client.model.ModModelLayers;
import danger.orespawn.client.model.ModelMosquito;
import danger.orespawn.entity.Mosquito;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class MosquitoRenderer extends MobRenderer<Mosquito, ModelMosquito> {
    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath("orespawn", "textures/entity/mosquito.png");

    public MosquitoRenderer(EntityRendererProvider.Context context) {
        super(context, new ModelMosquito(context.bakeLayer(ModModelLayers.MOSQUITO)), 0F);
    }

    @Override
    public ResourceLocation getTextureLocation(Mosquito entity) {
        return TEXTURE;
    }
}
