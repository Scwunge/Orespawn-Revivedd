package danger.orespawn.client.render;

import danger.orespawn.client.model.ModModelLayers;
import danger.orespawn.client.model.ModelBeaver;
import danger.orespawn.entity.Beaver;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class BeaverRenderer extends MobRenderer<Beaver, ModelBeaver> {
    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath("orespawn", "textures/entity/beaver.png");

    public BeaverRenderer(EntityRendererProvider.Context context) {
        super(context, new ModelBeaver(context.bakeLayer(ModModelLayers.BEAVER), 1F), 0F);
    }

    @Override
    public ResourceLocation getTextureLocation(Beaver entity) {
        return TEXTURE;
    }
}
