package danger.orespawn.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import danger.orespawn.client.model.ModelStinky;
import danger.orespawn.entity.Stinky;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

/**
 * Gold {@code RenderStinky}: ModelStinky(0.65F), shadow 0.75, scale 1.0.
 * Texture: stinkytexture{1.19}.png by {@link Stinky#getSkin()} 0.18.
 */
@OnlyIn(Dist.CLIENT)
public class StinkyRenderer extends MobRenderer<Stinky, ModelStinky> {
    private static final ResourceLocation[] TEXTURES = {
            ResourceLocation.fromNamespaceAndPath("orespawn", "textures/entity/stinkytexture1.png"),
            ResourceLocation.fromNamespaceAndPath("orespawn", "textures/entity/stinkytexture2.png"),
            ResourceLocation.fromNamespaceAndPath("orespawn", "textures/entity/stinkytexture3.png"),
            ResourceLocation.fromNamespaceAndPath("orespawn", "textures/entity/stinkytexture4.png"),
            ResourceLocation.fromNamespaceAndPath("orespawn", "textures/entity/stinkytexture5.png"),
            ResourceLocation.fromNamespaceAndPath("orespawn", "textures/entity/stinkytexture6.png"),
            ResourceLocation.fromNamespaceAndPath("orespawn", "textures/entity/stinkytexture7.png"),
            ResourceLocation.fromNamespaceAndPath("orespawn", "textures/entity/stinkytexture8.png"),
            ResourceLocation.fromNamespaceAndPath("orespawn", "textures/entity/stinkytexture9.png"),
            ResourceLocation.fromNamespaceAndPath("orespawn", "textures/entity/stinkytexture10.png"),
            ResourceLocation.fromNamespaceAndPath("orespawn", "textures/entity/stinkytexture11.png"),
            ResourceLocation.fromNamespaceAndPath("orespawn", "textures/entity/stinkytexture12.png"),
            ResourceLocation.fromNamespaceAndPath("orespawn", "textures/entity/stinkytexture13.png"),
            ResourceLocation.fromNamespaceAndPath("orespawn", "textures/entity/stinkytexture14.png"),
            ResourceLocation.fromNamespaceAndPath("orespawn", "textures/entity/stinkytexture15.png"),
            ResourceLocation.fromNamespaceAndPath("orespawn", "textures/entity/stinkytexture16.png"),
            ResourceLocation.fromNamespaceAndPath("orespawn", "textures/entity/stinkytexture17.png"),
            ResourceLocation.fromNamespaceAndPath("orespawn", "textures/entity/stinkytexture18.png"),
            ResourceLocation.fromNamespaceAndPath("orespawn", "textures/entity/stinkytexture19.png"),
    };

    /** Gold ClientProxy par3 scale */
    private static final float ADULT_SCALE = 1.0F;

    public StinkyRenderer(EntityRendererProvider.Context context) {
        super(
                context,
                new ModelStinky(context.bakeLayer(ModelStinky.LAYER_LOCATION), 0.65F),
                0.75F * ADULT_SCALE);
    }

    @Override
    protected void scale(Stinky entity, PoseStack poseStack, float partialTick) {
        poseStack.scale(ADULT_SCALE, ADULT_SCALE, ADULT_SCALE);
    }

    @Override
    public ResourceLocation getTextureLocation(Stinky entity) {
        // gold RenderStinky: skin i → texture(i+1); default texture1
        int i = entity.getSkin();
        if (i >= 0 && i < TEXTURES.length) {
            return TEXTURES[i];
        }
        return TEXTURES[0];
    }
}
