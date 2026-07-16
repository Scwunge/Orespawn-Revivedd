package danger.orespawn.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import danger.orespawn.client.model.ModModelLayers;
import danger.orespawn.client.model.ModelChipmunk;
import danger.orespawn.entity.Chipmunk;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

/**
 * Gold {@code RenderChipmunk}: ModelChipmunk(1.0F), shadow 0.15, scale 0.9 (baby half).
 * Texture: chipmunktexture.png (hat variants deferred with cannon fodder).
 */
@OnlyIn(Dist.CLIENT)
public class ChipmunkRenderer extends MobRenderer<Chipmunk, ModelChipmunk> {
    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath("orespawn", "textures/entity/chipmunktexture.png");
    /** Gold ClientProxy par3 scale */
    private static final float ADULT_SCALE = 0.9F;

    public ChipmunkRenderer(EntityRendererProvider.Context context) {
        super(context, new ModelChipmunk(context.bakeLayer(ModModelLayers.CHIPMUNK), 1.0F), 0.15F * ADULT_SCALE);
    }

    @Override
    protected void scale(Chipmunk entity, PoseStack poseStack, float partialTick) {
        float s = entity.isBaby() ? ADULT_SCALE / 2.0F : ADULT_SCALE;
        poseStack.scale(s, s, s);
    }

    @Override
    public ResourceLocation getTextureLocation(Chipmunk entity) {
        return TEXTURE;
    }
}
