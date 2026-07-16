package danger.orespawn.client.render;

import danger.orespawn.client.model.ModModelLayers;
import danger.orespawn.client.model.ModelFirefly;
import danger.orespawn.entity.Firefly;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

/**
 * Gold {@code RenderFirefly}. Uses {@link Firefly#getBlink()} so the bug glows when the blinker is on
 * (gold returned 240 brightness for half the blink cycle).
 */
@OnlyIn(Dist.CLIENT)
public class FireflyRenderer extends MobRenderer<Firefly, ModelFirefly> {
    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath("orespawn", "textures/entity/firefly.png");

    public FireflyRenderer(EntityRendererProvider.Context context) {
        super(context, new ModelFirefly(context.bakeLayer(ModModelLayers.FIREFLY), 1.5F), 0F);
    }

    @Override
    protected int getBlockLightLevel(Firefly entity, BlockPos pos) {
        // gold getBlink: half-cycle full bright
        if (entity.getBlink() > 0.0F) {
            return 15;
        }
        return super.getBlockLightLevel(entity, pos);
    }

    @Override
    public ResourceLocation getTextureLocation(Firefly entity) {
        return TEXTURE;
    }
}
