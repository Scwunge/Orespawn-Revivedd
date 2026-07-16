package danger.orespawn.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import danger.orespawn.client.model.ModelPurplePower;
import danger.orespawn.entity.PurplePower;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

/**
 * Gold {@code RenderPurplePower}: ModelPurplePower(1.0F), shadow 0.3*2.75, scale 2.75
 * (type != 0 → local scale 0.55). Multi-texture by purple_type.
 */
@OnlyIn(Dist.CLIENT)
public class PurplePowerRenderer extends MobRenderer<PurplePower, ModelPurplePower> {
    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath("orespawn", "textures/entity/purplepowertexture.png");
    private static final ResourceLocation TEXTURE2 =
            ResourceLocation.fromNamespaceAndPath("orespawn", "textures/entity/purplepowertexture2.png");
    private static final ResourceLocation TEXTURE3 =
            ResourceLocation.fromNamespaceAndPath("orespawn", "textures/entity/purplepowertexture3.png");
    private static final ResourceLocation TEXTURE4 =
            ResourceLocation.fromNamespaceAndPath("orespawn", "textures/entity/purplepowertexture4.png");
    private static final ResourceLocation TEXTURE10 =
            ResourceLocation.fromNamespaceAndPath("orespawn", "textures/entity/purplepowertexture10.png");

    /** Gold ClientProxy: {@code new RenderPurplePower(new ModelPurplePower(1.0F), 0.3F, 2.75F)}. */
    private static final float MODEL_SCALE = 2.75F;
    private static final float ALT_SCALE = 0.55F;

    public PurplePowerRenderer(EntityRendererProvider.Context context) {
        super(
                context,
                new ModelPurplePower(context.bakeLayer(ModelPurplePower.LAYER_LOCATION), 1.0F),
                0.3F * MODEL_SCALE);
    }

    @Override
    protected void scale(PurplePower entity, PoseStack poseStack, float partialTick) {
        float s = entity.getPurpleType() != 0 ? ALT_SCALE : MODEL_SCALE;
        poseStack.scale(s, s, s);
    }

    @Override
    public ResourceLocation getTextureLocation(PurplePower entity) {
        return switch (entity.getPurpleType()) {
            case 1 -> TEXTURE2;
            case 2 -> TEXTURE3;
            case 3 -> TEXTURE4;
            case 10 -> TEXTURE10;
            default -> TEXTURE;
        };
    }
}
