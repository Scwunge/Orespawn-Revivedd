package danger.orespawn.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import danger.orespawn.client.model.ModModelLayers;
import danger.orespawn.client.model.ModelCryolophosaurus;
import danger.orespawn.entity.Cryolophosaurus;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

/**
 * Gold {@code ModelCryolophosaurus} renders with {@code glTranslate(0,0.675,0)} + {@code glScalef(0.55)}.
 */
@OnlyIn(Dist.CLIENT)
public class CryolophosaurusRenderer extends MobRenderer<Cryolophosaurus, ModelCryolophosaurus> {
    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath("orespawn", "textures/entity/cryolophosaurus.png");
    private static final float MODEL_SCALE = 0.55F;

    public CryolophosaurusRenderer(EntityRendererProvider.Context context) {
        super(context, new ModelCryolophosaurus(context.bakeLayer(ModModelLayers.CRYOLOPHOSAURUS), 1.5F), 0.3F);
    }

    @Override
    protected void scale(Cryolophosaurus entity, PoseStack poseStack, float partialTick) {
        poseStack.translate(0.0F, 0.675F, 0.0F);
        poseStack.scale(MODEL_SCALE, MODEL_SCALE, MODEL_SCALE);
    }

    @Override
    public ResourceLocation getTextureLocation(Cryolophosaurus entity) {
        return TEXTURE;
    }
}
