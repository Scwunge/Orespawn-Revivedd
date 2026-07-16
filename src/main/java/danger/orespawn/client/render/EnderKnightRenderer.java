package danger.orespawn.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import danger.orespawn.client.model.ModelEnderKnight;
import danger.orespawn.entity.EnderKnight;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

/**
 * Gold {@code RenderEnderKnight}: ModelEnderKnight(0.21), shadow 0.3*1.0, scale 1.0.
 * Texture: enderknighttexture.png (gold EnderKnighttexture.png).
 */
@OnlyIn(Dist.CLIENT)
public class EnderKnightRenderer extends MobRenderer<EnderKnight, ModelEnderKnight> {
    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath("orespawn", "textures/entity/enderknighttexture.png");
    /** Gold ClientProxy: {@code new RenderEnderKnight(new ModelEnderKnight(0.21F), 0.3F, 1.0F)}. */
    private static final float MODEL_SCALE = 1.0F;

    public EnderKnightRenderer(EntityRendererProvider.Context context) {
        super(context, new ModelEnderKnight(context.bakeLayer(ModelEnderKnight.LAYER_LOCATION), 0.21F), 0.3F * MODEL_SCALE);
    }

    @Override
    protected void scale(EnderKnight entity, PoseStack poseStack, float partialTick) {
        poseStack.scale(MODEL_SCALE, MODEL_SCALE, MODEL_SCALE);
    }

    @Override
    public ResourceLocation getTextureLocation(EnderKnight entity) {
        return TEXTURE;
    }
}
