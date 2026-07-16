package danger.orespawn.client.render;

import danger.orespawn.client.model.ModModelLayers;
import danger.orespawn.client.model.ModelButterfly;
import danger.orespawn.entity.Butterfly;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ButterflyRenderer extends MobRenderer<Butterfly, ModelButterfly> {
    private static final ResourceLocation TEXTURE1 =
            ResourceLocation.fromNamespaceAndPath("orespawn", "textures/entity/butterfly.png");
    private static final ResourceLocation TEXTURE2 =
            ResourceLocation.fromNamespaceAndPath("orespawn", "textures/entity/butterfly2.png");
    private static final ResourceLocation TEXTURE3 =
            ResourceLocation.fromNamespaceAndPath("orespawn", "textures/entity/butterfly3.png");
    private static final ResourceLocation TEXTURE4 =
            ResourceLocation.fromNamespaceAndPath("orespawn", "textures/entity/butterfly4.png");

    public ButterflyRenderer(EntityRendererProvider.Context context) {
        super(context, new ModelButterfly(context.bakeLayer(ModModelLayers.BUTTERFLY), 0.6F), 0.0F);
    }

    @Override
    public ResourceLocation getTextureLocation(Butterfly entity) {
        return switch (entity.getButterflyType()) {
            case 1 -> TEXTURE1;
            case 2 -> TEXTURE2;
            case 3 -> TEXTURE3;
            default -> TEXTURE4;
        };
    }
}
