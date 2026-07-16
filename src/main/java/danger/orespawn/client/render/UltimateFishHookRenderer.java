package danger.orespawn.client.render;

import danger.orespawn.entity.UltimateFishHook;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.FishingHookRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.projectile.FishingHook;

/**
 * Gold used default fish-hook rendering. Thin typed wrapper around vanilla
 * {@link FishingHookRenderer} for {@link UltimateFishHook}.
 */
public class UltimateFishHookRenderer extends FishingHookRenderer {
    private static final ResourceLocation TEXTURE_LOCATION =
            ResourceLocation.withDefaultNamespace("textures/entity/fishing_hook.png");

    public UltimateFishHookRenderer(EntityRendererProvider.Context ctx) {
        super(ctx);
    }

    @Override
    public ResourceLocation getTextureLocation(FishingHook entity) {
        return TEXTURE_LOCATION;
    }
}
