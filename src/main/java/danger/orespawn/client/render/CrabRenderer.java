package danger.orespawn.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import danger.orespawn.client.model.ModelCrab;
import danger.orespawn.entity.Crab;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

/**
 * Gold {@code RenderCrab}: ModelCrab(1.0F), shadow 0.99*1.0, scale = {@link Crab#getCrabScale()}.
 * Texture gold {@code RobotCrabtexture.png} → {@code robotcrabtexture.png}.
 */
@OnlyIn(Dist.CLIENT)
public class CrabRenderer extends MobRenderer<Crab, ModelCrab> {
    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath("orespawn", "textures/entity/robotcrabtexture.png");

    public CrabRenderer(EntityRendererProvider.Context context) {
        super(context, new ModelCrab(context.bakeLayer(ModelCrab.LAYER_LOCATION)), 0.99F);
    }

    @Override
    protected void scale(Crab entity, PoseStack poseStack, float partialTick) {
        float s = entity.getCrabScale();
        poseStack.scale(s, s, s);
    }

    @Override
    public ResourceLocation getTextureLocation(Crab entity) {
        return TEXTURE;
    }
}
