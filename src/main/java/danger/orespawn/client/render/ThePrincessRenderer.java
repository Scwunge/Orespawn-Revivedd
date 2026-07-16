package danger.orespawn.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import danger.orespawn.client.model.ModelThePrincess;
import danger.orespawn.entity.ThePrincess;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

/**
 * Gold {@code RenderThePrincess}: ModelThePrincess(0.65F), shadow 0.7*0.7, scale 0.7.
 * Textures: theprincesstexture.png (idle) / theprincesstexture2.png (attacking).
 * ClientProxy: {@code new RenderThePrincess(new ModelThePrincess(0.65F), 0.7F, 0.7F)}.
 */
@OnlyIn(Dist.CLIENT)
public class ThePrincessRenderer extends MobRenderer<ThePrincess, ModelThePrincess> {
    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath("orespawn", "textures/entity/theprincesstexture.png");
    private static final ResourceLocation TEXTURE2 =
            ResourceLocation.fromNamespaceAndPath("orespawn", "textures/entity/theprincesstexture2.png");

    /** Gold RenderThePrincess scale (par3). */
    private static final float MODEL_SCALE = 0.7F;

    public ThePrincessRenderer(EntityRendererProvider.Context context) {
        super(
                context,
                new ModelThePrincess(context.bakeLayer(ModelThePrincess.LAYER_LOCATION), 0.65F),
                0.7F * MODEL_SCALE);
    }

    @Override
    protected void scale(ThePrincess entity, PoseStack poseStack, float partialTick) {
        poseStack.scale(MODEL_SCALE, MODEL_SCALE, MODEL_SCALE);
    }

    @Override
    public ResourceLocation getTextureLocation(ThePrincess entity) {
        return entity.getAttacking() != 0 ? TEXTURE2 : TEXTURE;
    }
}
