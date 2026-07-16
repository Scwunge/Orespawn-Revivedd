package danger.orespawn.client.render;

import danger.orespawn.entity.SpiderDriver;
import net.minecraft.client.model.SpiderModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

/**
 * Gold {@code RenderSpiderDriver}: ModelSpider, shadow 0.5F (ClientProxy).
 * Texture: {@code textures/entity/spiderdriver.png}.
 * Uses vanilla {@link ModelLayers#SPIDER} (no custom layer / no eyes layer).
 */
@OnlyIn(Dist.CLIENT)
public class SpiderDriverRenderer extends MobRenderer<SpiderDriver, SpiderModel<SpiderDriver>> {
    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath("orespawn", "textures/entity/spiderdriver.png");

    public SpiderDriverRenderer(EntityRendererProvider.Context context) {
        super(context, new SpiderModel<>(context.bakeLayer(ModelLayers.SPIDER)), 0.5F);
    }

    @Override
    public ResourceLocation getTextureLocation(SpiderDriver entity) {
        return TEXTURE;
    }
}
