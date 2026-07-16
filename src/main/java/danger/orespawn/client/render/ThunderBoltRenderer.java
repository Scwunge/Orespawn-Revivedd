package danger.orespawn.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import danger.orespawn.entity.ThunderBolt;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

/**
 * Gold has no dedicated ThunderBolt renderer (particle-only trail).
 * Sprite stand-in using thunderstaff / lightning rod for visibility.
 */
public class ThunderBoltRenderer extends EntityRenderer<ThunderBolt> {
    private final ItemRenderer itemRenderer;

    public ThunderBoltRenderer(EntityRendererProvider.Context ctx) {
        super(ctx);
        this.itemRenderer = ctx.getItemRenderer();
    }

    @Override
    public void render(ThunderBolt entity, float yaw, float pt, PoseStack pose, MultiBufferSource buf, int light) {
        pose.pushPose();
        pose.scale(0.4F, 0.4F, 0.4F);
        pose.mulPose(this.entityRenderDispatcher.cameraOrientation());
        this.itemRenderer.renderStatic(
                itemStack(),
                ItemDisplayContext.GROUND,
                light,
                OverlayTexture.NO_OVERLAY,
                pose,
                buf,
                entity.level(),
                entity.getId());
        pose.popPose();
        super.render(entity, yaw, pt, pose, buf, light);
    }

    private static ItemStack itemStack() {
        Item item = BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath("orespawn", "thunderstaff"));
        if (item != null && item != Items.AIR) {
            return new ItemStack(item);
        }
        return new ItemStack(Items.LIGHTNING_ROD);
    }

    @Override
    public ResourceLocation getTextureLocation(ThunderBolt entity) {
        return ResourceLocation.fromNamespaceAndPath("orespawn", "textures/item/thunderstaff.png");
    }
}
