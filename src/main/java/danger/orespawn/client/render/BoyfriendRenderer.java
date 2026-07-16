package danger.orespawn.client.render;

import danger.orespawn.entity.Boyfriend;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

/**
 * Gold {@code RenderBoyfriend}: ModelBiped, shadow {@code 0.55F}.
 * Texture from {@link Boyfriend#getTexture()}.
 * No custom model layer — vanilla {@link ModelLayers#PLAYER}.
 */
@OnlyIn(Dist.CLIENT)
public class BoyfriendRenderer extends MobRenderer<Boyfriend, HumanoidModel<Boyfriend>> {
    public BoyfriendRenderer(EntityRendererProvider.Context context) {
        // gold ClientProxy: new RenderBoyfriend(new ModelBiped(), 0.55F)
        super(context, new HumanoidModel<>(context.bakeLayer(ModelLayers.PLAYER)), 0.55F);
    }

    @Override
    public ResourceLocation getTextureLocation(Boyfriend entity) {
        return entity.getTexture();
    }
}
