package danger.orespawn.client.render;

import danger.orespawn.entity.RedCow;
import net.minecraft.client.model.CowModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

/** Gold red cow — use cow geometry with orespawn red_cow texture. */
@OnlyIn(Dist.CLIENT)
public class RedCowRenderer extends MobRenderer<RedCow, CowModel<RedCow>> {
    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath("orespawn", "textures/entity/red_cow.png");

    public RedCowRenderer(EntityRendererProvider.Context context) {
        super(context, new CowModel<>(context.bakeLayer(ModelLayers.COW)), 0.7F);
    }

    @Override
    public ResourceLocation getTextureLocation(RedCow entity) {
        return TEXTURE;
    }
}
