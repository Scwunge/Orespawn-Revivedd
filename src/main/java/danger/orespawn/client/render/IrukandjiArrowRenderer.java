package danger.orespawn.client.render;

import danger.orespawn.entity.IrukandjiArrow;
import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.TippableArrowRenderer;
import net.minecraft.resources.ResourceLocation;

/** Gold ClientProxy {@code new RenderArrow()} for IrukandjiArrow. */
public class IrukandjiArrowRenderer extends ArrowRenderer<IrukandjiArrow> {
    public IrukandjiArrowRenderer(EntityRendererProvider.Context ctx) {
        super(ctx);
    }

    @Override
    public ResourceLocation getTextureLocation(IrukandjiArrow entity) {
        // gold used arrow renderer; item icon is irukandjiarrow.png for the item
        return TippableArrowRenderer.NORMAL_ARROW_LOCATION;
    }
}
