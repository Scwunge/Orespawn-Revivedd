package danger.orespawn.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import danger.orespawn.client.model.ModModelLayers;
import danger.orespawn.client.model.ModelFairy;
import danger.orespawn.entity.Fairy;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

/**
 * Gold {@code RenderFairy} — scale 0.35 (ClientProxy {@code new RenderFairy(new ModelFairy(1.5F), 0.1F, 0.35F)}).
 * fairy_type 0–8 → fairytexture / fairytexture2–9.
 */
@OnlyIn(Dist.CLIENT)
public class FairyRenderer extends MobRenderer<Fairy, ModelFairy> {
    private static final ResourceLocation[] TEXTURES = {
        ResourceLocation.fromNamespaceAndPath("orespawn", "textures/entity/fairytexture.png"),
        ResourceLocation.fromNamespaceAndPath("orespawn", "textures/entity/fairytexture2.png"),
        ResourceLocation.fromNamespaceAndPath("orespawn", "textures/entity/fairytexture3.png"),
        ResourceLocation.fromNamespaceAndPath("orespawn", "textures/entity/fairytexture4.png"),
        ResourceLocation.fromNamespaceAndPath("orespawn", "textures/entity/fairytexture5.png"),
        ResourceLocation.fromNamespaceAndPath("orespawn", "textures/entity/fairytexture6.png"),
        ResourceLocation.fromNamespaceAndPath("orespawn", "textures/entity/fairytexture7.png"),
        ResourceLocation.fromNamespaceAndPath("orespawn", "textures/entity/fairytexture8.png"),
        ResourceLocation.fromNamespaceAndPath("orespawn", "textures/entity/fairytexture9.png"),
    };

    /** Gold RenderFairy scale (par3). */
    private static final float MODEL_SCALE = 0.35F;

    public FairyRenderer(EntityRendererProvider.Context context) {
        // gold shadow: par2 * par3 = 0.1 * 0.35
        super(context, new ModelFairy(context.bakeLayer(ModModelLayers.FAIRY), 1.5F), 0.1F * MODEL_SCALE);
    }

    @Override
    protected void scale(Fairy entity, PoseStack poseStack, float partialTickTime) {
        poseStack.scale(MODEL_SCALE, MODEL_SCALE, MODEL_SCALE);
    }

    @Override
    protected int getBlockLightLevel(Fairy entity, BlockPos pos) {
        // gold ModelFairy OpenGlHelper blink: 240 brightness half-cycle
        if (entity.getBlink() > 0.0F) {
            return 15;
        }
        return super.getBlockLightLevel(entity, pos);
    }

    @Override
    public ResourceLocation getTextureLocation(Fairy entity) {
        int t = entity.getFairyType();
        if (t < 0 || t >= TEXTURES.length) {
            return TEXTURES[0];
        }
        return TEXTURES[t];
    }
}
