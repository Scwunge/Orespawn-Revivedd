package danger.orespawn.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import danger.orespawn.entity.KingHead;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

/**
 * Gold {@code RenderKingHead} — intentionally draws nothing (invisible hitbox).
 * ClientProxy: {@code new RenderKingHead(null, 0.0F, 0.0F)}.
 */
@OnlyIn(Dist.CLIENT)
public class KingHeadRenderer extends EntityRenderer<KingHead> {
    /** Unused — gold returns null; dummy path so API is non-null. */
    private static final ResourceLocation DUMMY =
            ResourceLocation.fromNamespaceAndPath("orespawn", "textures/entity/thekingtexture.png");

    public KingHeadRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.shadowRadius = 0.0F;
    }

    @Override
    public void render(
            KingHead entity,
            float entityYaw,
            float partialTick,
            PoseStack poseStack,
            MultiBufferSource buffer,
            int packedLight) {
        // gold: empty render methods — no model, no texture
    }

    @Override
    public ResourceLocation getTextureLocation(KingHead entity) {
        return DUMMY;
    }
}
