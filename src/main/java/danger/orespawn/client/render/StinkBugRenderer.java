package danger.orespawn.client.render;

import danger.orespawn.client.model.ModModelLayers;
import danger.orespawn.client.model.ModelStinkBug;
import danger.orespawn.entity.StinkBug;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class StinkBugRenderer extends MobRenderer<StinkBug, ModelStinkBug> {
    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath("orespawn", "textures/entity/stinkbug.png");

    public StinkBugRenderer(EntityRendererProvider.Context context) {
        super(context, new ModelStinkBug(context.bakeLayer(ModModelLayers.STINKBUG), 0.1F), 0F);
    }

    @Override
    public ResourceLocation getTextureLocation(StinkBug entity) {
        return TEXTURE;
    }
}
