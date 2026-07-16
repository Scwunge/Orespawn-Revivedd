package danger.orespawn.client.render;

import danger.orespawn.client.model.ModModelLayers;
import danger.orespawn.client.model.ModelVelocityRaptor;
import danger.orespawn.entity.VelocityRaptor;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class VelocityRaptorRenderer extends MobRenderer<VelocityRaptor, ModelVelocityRaptor> {
    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath("orespawn", "textures/entity/velocityraptor.png");

    public VelocityRaptorRenderer(EntityRendererProvider.Context context) {
        super(context, new ModelVelocityRaptor(context.bakeLayer(ModModelLayers.VELOCITYRAPTOR), 1F), 0F);
    }

    @Override
    public ResourceLocation getTextureLocation(VelocityRaptor entity) {
        return TEXTURE;
    }
}
