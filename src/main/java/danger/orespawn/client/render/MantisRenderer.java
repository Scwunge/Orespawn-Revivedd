package danger.orespawn.client.render;

import danger.orespawn.client.model.ModModelLayers;
import danger.orespawn.client.model.ModelMantis;
import danger.orespawn.entity.Mantis;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class MantisRenderer extends MobRenderer<Mantis, ModelMantis> {
    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath("orespawn", "textures/entity/mantis.png");

    public MantisRenderer(EntityRendererProvider.Context context) {
        super(context, new ModelMantis(context.bakeLayer(ModModelLayers.MANTIS), 1F), 0F);
    }

    @Override
    public ResourceLocation getTextureLocation(Mantis entity) {
        return TEXTURE;
    }
}
