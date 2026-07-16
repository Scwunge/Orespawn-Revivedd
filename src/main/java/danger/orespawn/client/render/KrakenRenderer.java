package danger.orespawn.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import danger.orespawn.client.model.ModelKraken;
import danger.orespawn.entity.Kraken;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

/**
 * Gold {@code RenderKraken}: ModelKraken(1.0F), shadow 1.0*1.0, scale 1.0
 * (PlayNicely → scale/3). Texture: {@code textures/entity/kraken.png}.
 * Gold model applies {@code GL11.glRotatef(90, 1, 0, 0)} before draw — mirrored in
 * {@link #setupRotations}.
 */
@OnlyIn(Dist.CLIENT)
public class KrakenRenderer extends MobRenderer<Kraken, ModelKraken> {
    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath("orespawn", "textures/entity/kraken.png");
    /** Gold ClientProxy: {@code new RenderKraken(new ModelKraken(1.0F), 1.0F, 1.0F)}. */
    private static final float MODEL_SCALE = 1.0F;

    public KrakenRenderer(EntityRendererProvider.Context context) {
        // gold: shadow par2 * par3 = 1.0 * 1.0; wingspeed 1.0
        super(
                context,
                new ModelKraken(context.bakeLayer(ModelKraken.LAYER_LOCATION), 1.0F),
                1.0F * MODEL_SCALE);
    }

    @Override
    protected void scale(Kraken entity, PoseStack poseStack, float partialTick) {
        // gold preRenderScale: PlayNicely != 0 → scale/3 else scale
        float s = MODEL_SCALE;
        if (entity.getPlayNicely() != 0) {
            s = MODEL_SCALE / 3.0F;
        }
        poseStack.scale(s, s, s);
    }

    /**
     * Gold {@code ModelKraken} uses {@code GL11.glRotatef(90, 1, 0, 0)} so the long
     * body hangs vertical. Mojang {@link Axis#XP} is opposite-handed to that GL call —
     * use <b>-90</b> or the mesh appears upside-down.
     */
    @Override
    protected void setupRotations(
            Kraken entity, PoseStack poseStack, float bob, float yBodyRot, float partialTick, float scale) {
        super.setupRotations(entity, poseStack, bob, yBodyRot, partialTick, scale);
        poseStack.mulPose(Axis.XP.rotationDegrees(-90.0F));
    }

    @Override
    public ResourceLocation getTextureLocation(Kraken entity) {
        return TEXTURE;
    }
}
