package danger.orespawn.client.render;

import danger.orespawn.client.model.ModModelLayers;
import danger.orespawn.client.model.ModelMoth;
import danger.orespawn.entity.Moth;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class MothRenderer extends MobRenderer<Moth, ModelMoth> {
    // gold RenderMoth: lunamoth / eyemoth / firemoth / darkmoth by moth_type
    private static final ResourceLocation TEXTURE1 =
            ResourceLocation.fromNamespaceAndPath("orespawn", "textures/entity/lunamoth.png");
    private static final ResourceLocation TEXTURE2 =
            ResourceLocation.fromNamespaceAndPath("orespawn", "textures/entity/eyemoth.png");
    private static final ResourceLocation TEXTURE3 =
            ResourceLocation.fromNamespaceAndPath("orespawn", "textures/entity/firemoth.png");
    private static final ResourceLocation TEXTURE4 =
            ResourceLocation.fromNamespaceAndPath("orespawn", "textures/entity/darkmoth.png");

    public MothRenderer(EntityRendererProvider.Context context) {
        super(context, new ModelMoth(context.bakeLayer(ModModelLayers.MOTH), 0.6F), 0.0F);
    }

    @Override
    public ResourceLocation getTextureLocation(Moth entity) {
        return switch (entity.getMothType()) {
            case 0 -> TEXTURE1;
            case 1 -> TEXTURE2;
            case 2 -> TEXTURE3;
            default -> TEXTURE4;
        };
    }
}
