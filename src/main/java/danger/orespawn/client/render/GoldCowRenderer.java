package danger.orespawn.client.render;

import danger.orespawn.entity.GoldCow;
import net.minecraft.client.model.CowModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

/**
 * Gold {@code RenderEnchantedCow} branch for GoldCow: ModelCow, shadow 0.7,
 * texture {@code gold_cow.png}.
 */
@OnlyIn(Dist.CLIENT)
public class GoldCowRenderer extends MobRenderer<GoldCow, CowModel<GoldCow>> {
    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath("orespawn", "textures/entity/gold_cow.png");

    public GoldCowRenderer(EntityRendererProvider.Context context) {
        super(context, new CowModel<>(context.bakeLayer(ModelLayers.COW)), 0.7F);
    }

    @Override
    public ResourceLocation getTextureLocation(GoldCow entity) {
        return TEXTURE;
    }
}
