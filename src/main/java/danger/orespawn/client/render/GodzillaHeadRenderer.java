package danger.orespawn.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import danger.orespawn.entity.GodzillaHead;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

/**
 * Gold {@code RenderGodzillaHead} — intentionally draws nothing (invisible hitbox).
 * ClientProxy: {@code new RenderGodzillaHead(null, 0.0F, 0.0F)}.
 */
@OnlyIn(Dist.CLIENT)
public class GodzillaHeadRenderer extends EntityRenderer<GodzillaHead> {
    /** Unused — gold returns null; dummy path so API is non-null. */
    private static final ResourceLocation DUMMY =
            ResourceLocation.fromNamespaceAndPath("orespawn", "textures/entity/godzillatexture.png");

    public GodzillaHeadRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.shadowRadius = 0.0F;
    }

    @Override
    public void render(
            GodzillaHead entity,
            float entityYaw,
            float partialTick,
            PoseStack poseStack,
            MultiBufferSource buffer,
            int packedLight) {
        // gold: empty render methods — no model, no texture
    }

    @Override
    public ResourceLocation getTextureLocation(GodzillaHead entity) {
        return DUMMY;
    }
}
