package danger.orespawn.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import danger.orespawn.entity.InkSack;
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

/** Gold RenderItemUrchin-style sprite for InkSack (index 65). */
public class InkSackRenderer extends EntityRenderer<InkSack> {
    private final ItemRenderer itemRenderer;

    public InkSackRenderer(EntityRendererProvider.Context ctx) {
        super(ctx);
        this.itemRenderer = ctx.getItemRenderer();
    }

    @Override
    public void render(InkSack entity, float yaw, float pt, PoseStack pose, MultiBufferSource buf, int light) {
        pose.pushPose();
        pose.scale(0.5F, 0.5F, 0.5F);
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
        Item item = BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath("orespawn", "inksack"));
        if (item != null && item != Items.AIR) {
            return new ItemStack(item);
        }
        return new ItemStack(Items.INK_SAC);
    }

    @Override
    public ResourceLocation getTextureLocation(InkSack entity) {
        return ResourceLocation.fromNamespaceAndPath("orespawn", "textures/item/inksack.png");
    }
}
