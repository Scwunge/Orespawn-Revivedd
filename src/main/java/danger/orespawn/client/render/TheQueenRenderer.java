package danger.orespawn.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import danger.orespawn.client.model.ModelTheQueen;
import danger.orespawn.entity.TheQueen;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

/**
 * Gold {@code RenderTheQueen}: ModelTheQueen(0.65F), shadow 1.9*2.0, scale 2.0.
 * Textures: mad {@code thequeentexture.png}, happy {@code thequeentexture2.png}.
 * ClientProxy: {@code new RenderTheQueen(new ModelTheQueen(0.65F), 1.9F, 2.0F)}.
 * PlayNicely != 0 → scale / 4.
 */
@OnlyIn(Dist.CLIENT)
public class TheQueenRenderer extends MobRenderer<TheQueen, ModelTheQueen> {
    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath("orespawn", "textures/entity/thequeentexture.png");
    private static final ResourceLocation TEXTURE2 =
            ResourceLocation.fromNamespaceAndPath("orespawn", "textures/entity/thequeentexture2.png");

    /** Gold RenderTheQueen scale (par3). */
    private static final float MODEL_SCALE = 2.0F;

    public TheQueenRenderer(EntityRendererProvider.Context context) {
        super(
                context,
                new ModelTheQueen(context.bakeLayer(ModelTheQueen.LAYER_LOCATION), 0.65F),
                1.9F * MODEL_SCALE);
    }

    @Override
    protected void scale(TheQueen entity, PoseStack poseStack, float partialTick) {
        float s = MODEL_SCALE;
        if (entity.getPlayNicely() != 0) {
            s = MODEL_SCALE / 4.0F;
        }
        poseStack.scale(s, s, s);
    }

    @Override
    public ResourceLocation getTextureLocation(TheQueen entity) {
        // gold: isHappy() → texture2, else texture
        return entity.isHappy() ? TEXTURE2 : TEXTURE;
    }
}
