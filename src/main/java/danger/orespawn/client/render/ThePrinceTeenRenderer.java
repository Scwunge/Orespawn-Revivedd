package danger.orespawn.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import danger.orespawn.client.model.ModelThePrinceTeen;
import danger.orespawn.entity.ThePrinceTeen;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

/**
 * Gold {@code RenderThePrinceTeen}: ModelThePrinceTeen(0.65F), shadow 1.0*1.25, scale 1.25.
 * Texture: {@code textures/entity/princeteentexture.png}.
 */
@OnlyIn(Dist.CLIENT)
public class ThePrinceTeenRenderer extends MobRenderer<ThePrinceTeen, ModelThePrinceTeen> {
    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath("orespawn", "textures/entity/princeteentexture.png");

    public ThePrinceTeenRenderer(EntityRendererProvider.Context context) {
        // gold ClientProxy: new RenderThePrinceTeen(new ModelThePrinceTeen(0.65F), 1.0F, 1.25F)
        super(context, new ModelThePrinceTeen(context.bakeLayer(ModelThePrinceTeen.LAYER_LOCATION), 0.65F), 1.25F);
    }

    @Override
    protected void scale(ThePrinceTeen entity, PoseStack poseStack, float partialTick) {
        // gold preRenderScale glScalef(1.25)
        poseStack.scale(1.25F, 1.25F, 1.25F);
    }

    @Override
    public ResourceLocation getTextureLocation(ThePrinceTeen entity) {
        return TEXTURE;
    }
}
