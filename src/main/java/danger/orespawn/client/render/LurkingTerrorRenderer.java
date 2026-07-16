package danger.orespawn.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import danger.orespawn.client.model.ModModelLayers;
import danger.orespawn.client.model.ModelLurkingTerror;
import danger.orespawn.entity.LurkingTerror;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

/**
 * Gold {@code RenderLurkingTerror}: ModelLurkingTerror, shadow 0.45*0.85, scale 0.85.
 * Texture: lurkingterror.png (gold LurkingTerror.png).
 */
@OnlyIn(Dist.CLIENT)
public class LurkingTerrorRenderer extends MobRenderer<LurkingTerror, ModelLurkingTerror> {
    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath("orespawn", "textures/entity/lurkingterror.png");
    /** Gold ClientProxy: {@code new RenderLurkingTerror(new ModelLurkingTerror(), 0.45F, 0.85F)}. */
    private static final float MODEL_SCALE = 0.85F;

    public LurkingTerrorRenderer(EntityRendererProvider.Context context) {
        super(
                context,
                new ModelLurkingTerror(context.bakeLayer(ModModelLayers.LURKING_TERROR), 1.0F),
                0.45F * MODEL_SCALE);
    }

    @Override
    protected void scale(LurkingTerror entity, PoseStack poseStack, float partialTick) {
        poseStack.scale(MODEL_SCALE, MODEL_SCALE, MODEL_SCALE);
    }

    @Override
    public ResourceLocation getTextureLocation(LurkingTerror entity) {
        return TEXTURE;
    }
}
