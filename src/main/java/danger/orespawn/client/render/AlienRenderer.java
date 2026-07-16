package danger.orespawn.client.render;

import danger.orespawn.client.model.ModModelLayers;
import danger.orespawn.client.model.ModelAlien;
import danger.orespawn.entity.Alien;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class AlienRenderer extends MobRenderer<Alien, ModelAlien> {
    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath("orespawn", "textures/entity/alien.png");

    public AlienRenderer(EntityRendererProvider.Context context) {
        super(context, new ModelAlien(context.bakeLayer(ModModelLayers.ALIEN), 0.1F), 0.0F);
    }

    @Override
    public ResourceLocation getTextureLocation(Alien entity) {
        return TEXTURE;
    }
}
