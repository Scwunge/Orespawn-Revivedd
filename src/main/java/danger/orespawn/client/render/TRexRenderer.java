package danger.orespawn.client.render;

import danger.orespawn.client.model.ModModelLayers;
import danger.orespawn.client.model.ModelTRex;
import danger.orespawn.entity.TRex;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class TRexRenderer extends MobRenderer<TRex, ModelTRex> {
    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath("orespawn", "textures/entity/trex.png");

    public TRexRenderer(EntityRendererProvider.Context context) {
        super(context, new ModelTRex(context.bakeLayer(ModModelLayers.TREX), 1.5F), 0.0F);
    }

    @Override
    public ResourceLocation getTextureLocation(TRex entity) {
        return TEXTURE;
    }
}
