package danger.orespawn.client.render;

import danger.orespawn.client.model.ModModelLayers;
import danger.orespawn.client.model.ModelCassowary;
import danger.orespawn.entity.Cassowary;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class CassowaryRenderer extends MobRenderer<Cassowary, ModelCassowary> {
    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath("orespawn", "textures/entity/cassowary.png");

    public CassowaryRenderer(EntityRendererProvider.Context context) {
        super(context, new ModelCassowary(context.bakeLayer(ModModelLayers.CASSOWARY), 1F), 0F);
    }

    @Override
    public ResourceLocation getTextureLocation(Cassowary entity) {
        return TEXTURE;
    }
}
