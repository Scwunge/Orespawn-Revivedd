package danger.orespawn.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import danger.orespawn.client.model.ModelDungeonBeast;
import danger.orespawn.entity.DungeonBeast;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

/**
 * Gold {@code RenderDungeonBeast}: ModelDungeonBeast(0.62F), shadow 0.25*1.0, scale 1.0.
 * Texture: botwtexture.png (gold Botwtexture.png).
 * Gold model applies {@code GL11.glRotatef(90, 0, 1, 0)} before draw — mirrored in
 * {@link #setupRotations}.
 */
@OnlyIn(Dist.CLIENT)
public class DungeonBeastRenderer extends MobRenderer<DungeonBeast, ModelDungeonBeast> {
    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath("orespawn", "textures/entity/botwtexture.png");
    /** Gold ClientProxy: {@code new RenderDungeonBeast(new ModelDungeonBeast(0.62F), 0.25F, 1.0F)}. */
    private static final float MODEL_SCALE = 1.0F;

    public DungeonBeastRenderer(EntityRendererProvider.Context context) {
        super(
                context,
                new ModelDungeonBeast(context.bakeLayer(ModelDungeonBeast.LAYER_LOCATION), 0.62F),
                0.25F * MODEL_SCALE);
    }

    @Override
    protected void scale(DungeonBeast entity, PoseStack poseStack, float partialTick) {
        poseStack.scale(MODEL_SCALE, MODEL_SCALE, MODEL_SCALE);
    }

    /**
     * Gold {@code ModelDungeonBeast} rotates the mesh 90° about Y before drawing.
     * Without this, the body faces sideways relative to movement.
     */
    @Override
    protected void setupRotations(
            DungeonBeast entity, PoseStack poseStack, float bob, float yBodyRot, float partialTick, float scale) {
        super.setupRotations(entity, poseStack, bob, yBodyRot, partialTick, scale);
        poseStack.mulPose(Axis.YP.rotationDegrees(90.0F));
    }

    @Override
    public ResourceLocation getTextureLocation(DungeonBeast entity) {
        return TEXTURE;
    }
}
