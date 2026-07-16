package danger.orespawn.client.render;

import danger.orespawn.client.model.ModModelLayers;
import danger.orespawn.client.model.ModelDragonfly;
import danger.orespawn.entity.Dragonfly;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class DragonflyRenderer extends MobRenderer<Dragonfly, ModelDragonfly> {
    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath("orespawn", "textures/entity/dragonfly.png");

    public DragonflyRenderer(EntityRendererProvider.Context context) {
        super(context, new ModelDragonfly(context.bakeLayer(ModModelLayers.DRAGONFLY), 1.5F), 0F);
    }

    @Override
    public ResourceLocation getTextureLocation(Dragonfly entity) {
        return TEXTURE;
    }
}
