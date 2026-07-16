package danger.orespawn.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import danger.orespawn.client.model.ModModelLayers;
import danger.orespawn.client.model.ModelKyuubi;
import danger.orespawn.entity.Kyuubi;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class KyuubiRenderer extends MobRenderer<Kyuubi, ModelKyuubi> {
    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath("orespawn", "textures/entity/kyuubi.png");

    public KyuubiRenderer(EntityRendererProvider.Context context) {
        super(context, new ModelKyuubi(context.bakeLayer(ModModelLayers.KYUUBI), 1F), 0F);
    }

    /**
     * Gold {@code ModelKyuubi} rotates the entire mesh 180° around Y before drawing
     * ({@code GL11.glRotatef(180, 0, 1, 0)}). Without that, the body faces opposite
     * movement and the legs look like moonwalking.
     */
    @Override
    protected void setupRotations(Kyuubi entity, PoseStack poseStack, float bob, float yBodyRot, float partialTick, float scale) {
        super.setupRotations(entity, poseStack, bob, yBodyRot, partialTick, scale);
        poseStack.mulPose(Axis.YP.rotationDegrees(180.0F));
    }

    @Override
    public ResourceLocation getTextureLocation(Kyuubi entity) {
        return TEXTURE;
    }
}
