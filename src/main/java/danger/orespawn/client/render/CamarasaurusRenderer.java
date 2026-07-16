package danger.orespawn.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import danger.orespawn.client.model.ModModelLayers;
import danger.orespawn.client.model.ModelCamarasaurus;
import danger.orespawn.entity.Camarasaurus;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

/**
 * Gold {@code ModelCamarasaurus}:
 * adult {@code glTranslate(0,0.45,0)} + {@code glScalef(0.7)};
 * child {@code glTranslate(0,0.9,0)} + {@code glScalef(0.4)} (entity is Monster — no baby path).
 */
@OnlyIn(Dist.CLIENT)
public class CamarasaurusRenderer extends MobRenderer<Camarasaurus, ModelCamarasaurus> {
    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath("orespawn", "textures/entity/camarasaurus.png");
    private static final float ADULT_SCALE = 0.7F;

    public CamarasaurusRenderer(EntityRendererProvider.Context context) {
        super(context, new ModelCamarasaurus(context.bakeLayer(ModModelLayers.CAMARASAURUS), 1.5F), 0.5F);
    }

    @Override
    protected void scale(Camarasaurus entity, PoseStack poseStack, float partialTick) {
        // gold adult: translate(0, 0.45, 0) + scale 0.7 (Camarasaurus is not AgeableMob)
        poseStack.translate(0.0F, 0.45F, 0.0F);
        poseStack.scale(ADULT_SCALE, ADULT_SCALE, ADULT_SCALE);
    }

    @Override
    public ResourceLocation getTextureLocation(Camarasaurus entity) {
        return TEXTURE;
    }
}
