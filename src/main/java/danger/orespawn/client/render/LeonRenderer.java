package danger.orespawn.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import danger.orespawn.client.model.ModelLeon;
import danger.orespawn.entity.Leon;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

/**
 * Gold {@code RenderLeon}: ModelLeon(0.22F), shadow 1.0*1.75, scale 1.75.
 * Texture: {@code textures/entity/leon.png}.
 */
@OnlyIn(Dist.CLIENT)
public class LeonRenderer extends MobRenderer<Leon, ModelLeon> {
    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath("orespawn", "textures/entity/leon.png");

    public LeonRenderer(EntityRendererProvider.Context context) {
        // gold ClientProxy: new RenderLeon(new ModelLeon(0.22F), 1.0F, 1.75F)
        super(context, new ModelLeon(context.bakeLayer(ModelLeon.LAYER_LOCATION), 0.22F), 1.0F * 1.75F);
    }

    @Override
    protected void scale(Leon entity, PoseStack poseStack, float partialTick) {
        // gold scale 1.75F
        poseStack.scale(1.75F, 1.75F, 1.75F);
    }

    @Override
    public ResourceLocation getTextureLocation(Leon entity) {
        return TEXTURE;
    }
}
