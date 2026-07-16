package danger.orespawn.client.render;

import danger.orespawn.client.model.ModModelLayers;
import danger.orespawn.client.model.ModelPointysaurus;
import danger.orespawn.entity.Pointysaurus;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class PointysaurusRenderer extends MobRenderer<Pointysaurus, ModelPointysaurus> {
    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath("orespawn", "textures/entity/pointysaurus.png");

    public PointysaurusRenderer(EntityRendererProvider.Context context) {
        super(context, new ModelPointysaurus(context.bakeLayer(ModModelLayers.POINTYSAURUS), 1.5F), 0.7F);
    }

    @Override
    public ResourceLocation getTextureLocation(Pointysaurus entity) {
        return TEXTURE;
    }
}
