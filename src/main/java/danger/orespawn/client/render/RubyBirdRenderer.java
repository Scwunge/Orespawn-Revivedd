package danger.orespawn.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import danger.orespawn.client.model.ModelCockateil;
import danger.orespawn.entity.RubyBird;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

/**
 * Gold ClientProxy: {@code RenderCockateil(new ModelCockateil(1.0F), 0.3F, 0.75F)} for RubyBird.
 * Always birdtype 5 → {@code bird6.png} (gold Cockateil texture map case 5).
 * Model layer: {@link ModelCockateil#LAYER_LOCATION} (registered with entity renderer).
 */
@OnlyIn(Dist.CLIENT)
public class RubyBirdRenderer extends MobRenderer<RubyBird, ModelCockateil<RubyBird>> {
    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath("orespawn", "textures/entity/bird6.png");

    /** Gold ClientProxy par3 scale */
    private static final float ADULT_SCALE = 0.75F;

    public RubyBirdRenderer(EntityRendererProvider.Context context) {
        super(
                context,
                new ModelCockateil<>(context.bakeLayer(ModelCockateil.LAYER_LOCATION), 1.0F),
                0.3F * ADULT_SCALE);
    }

    @Override
    protected void scale(RubyBird entity, PoseStack poseStack, float partialTick) {
        poseStack.scale(ADULT_SCALE, ADULT_SCALE, ADULT_SCALE);
    }

    @Override
    public ResourceLocation getTextureLocation(RubyBird entity) {
        // gold birdtype 5 → Bird6; also present: textures/entity/ruby_1.png, ruby_2.png (unused by gold)
        return TEXTURE;
    }
}
