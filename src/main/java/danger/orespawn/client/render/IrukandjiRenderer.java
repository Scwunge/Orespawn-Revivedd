package danger.orespawn.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import danger.orespawn.client.model.ModelIrukandji;
import danger.orespawn.entity.Irukandji;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

/**
 * Gold {@code RenderIrukandji}: ModelIrukandji(1.0F), shadow 0.1, scale 0.25.
 * Texture: irukandjitexture.png (gold Irukandjitexture.png).
 */
@OnlyIn(Dist.CLIENT)
public class IrukandjiRenderer extends MobRenderer<Irukandji, ModelIrukandji> {
    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath("orespawn", "textures/entity/irukandjitexture.png");
    /** Gold ClientProxy par3 scale */
    private static final float MODEL_SCALE = 0.25F;

    public IrukandjiRenderer(EntityRendererProvider.Context context) {
        // gold: shadow par2 * par3 = 0.1 * 0.25; wingspeed 1.0
        super(
                context,
                new ModelIrukandji(context.bakeLayer(ModelIrukandji.LAYER_LOCATION), 1.0F),
                0.1F * MODEL_SCALE);
    }

    @Override
    protected void scale(Irukandji entity, PoseStack poseStack, float partialTick) {
        poseStack.scale(MODEL_SCALE, MODEL_SCALE, MODEL_SCALE);
    }

    @Override
    public ResourceLocation getTextureLocation(Irukandji entity) {
        return TEXTURE;
    }
}
