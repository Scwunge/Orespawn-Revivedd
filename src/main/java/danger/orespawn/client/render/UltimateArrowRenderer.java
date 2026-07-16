package danger.orespawn.client.render;

import danger.orespawn.entity.UltimateArrow;
import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.TippableArrowRenderer;
import net.minecraft.resources.ResourceLocation;

/** Gold ClientProxy {@code new RenderArrow()} for UltimateArrow. */
public class UltimateArrowRenderer extends ArrowRenderer<UltimateArrow> {
    public UltimateArrowRenderer(EntityRendererProvider.Context ctx) {
        super(ctx);
    }

    @Override
    public ResourceLocation getTextureLocation(UltimateArrow entity) {
        return TippableArrowRenderer.NORMAL_ARROW_LOCATION;
    }
}
