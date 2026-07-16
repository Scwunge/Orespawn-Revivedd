package danger.orespawn.client.render;

import danger.orespawn.entity.CrystalCow;
import net.minecraft.client.model.CowModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

/**
 * Gold {@code RenderEnchantedCow} branch for CrystalCow: ModelCow, shadow 0.7,
 * texture {@code crystal_cow.png}.
 */
@OnlyIn(Dist.CLIENT)
public class CrystalCowRenderer extends MobRenderer<CrystalCow, CowModel<CrystalCow>> {
    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath("orespawn", "textures/entity/crystal_cow.png");

    public CrystalCowRenderer(EntityRendererProvider.Context context) {
        super(context, new CowModel<>(context.bakeLayer(ModelLayers.COW)), 0.7F);
    }

    @Override
    public ResourceLocation getTextureLocation(CrystalCow entity) {
        return TEXTURE;
    }
}
