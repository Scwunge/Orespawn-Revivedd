package danger.orespawn.client;

import com.mojang.blaze3d.vertex.PoseStack;
import danger.orespawn.entity.BerthaHit;
import danger.orespawn.entity.BetterFireball;
import danger.orespawn.entity.EntityCage;
import danger.orespawn.init.ModItems;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

/** Simple item-as-sprite projectile renderers (1.21.1). */
public final class OreSpawnProjectileRenderers {
    private OreSpawnProjectileRenderers() {}

    public static class CageRenderer extends EntityRenderer<EntityCage> {
        private final ItemRenderer itemRenderer;

        public CageRenderer(EntityRendererProvider.Context ctx) {
            super(ctx);
            this.itemRenderer = ctx.getItemRenderer();
        }

        @Override
        public void render(EntityCage entity, float yaw, float pt, PoseStack pose, MultiBufferSource buf, int light) {
            pose.pushPose();
            pose.mulPose(this.entityRenderDispatcher.cameraOrientation());
            this.itemRenderer.renderStatic(
                    new ItemStack(ModItems.EMPTY_CAGE.get()),
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

        @Override
        public ResourceLocation getTextureLocation(EntityCage entity) {
            return ResourceLocation.withDefaultNamespace("textures/misc/white.png");
        }
    }

    public static class FireballRenderer extends EntityRenderer<BetterFireball> {
        private final ItemRenderer itemRenderer;

        public FireballRenderer(EntityRendererProvider.Context ctx) {
            super(ctx);
            this.itemRenderer = ctx.getItemRenderer();
        }

        @Override
        public void render(BetterFireball entity, float yaw, float pt, PoseStack pose, MultiBufferSource buf, int light) {
            pose.pushPose();
            pose.scale(2.0f, 2.0f, 2.0f);
            pose.mulPose(this.entityRenderDispatcher.cameraOrientation());
            this.itemRenderer.renderStatic(
                    new ItemStack(Items.FIRE_CHARGE),
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

        @Override
        public ResourceLocation getTextureLocation(BetterFireball entity) {
            return ResourceLocation.withDefaultNamespace("textures/item/fire_charge.png");
        }
    }

    /** 1.7.10 RenderItemUrchin-style sprite for BerthaHit. */
    public static class BerthaHitRenderer extends EntityRenderer<BerthaHit> {
        private final ItemRenderer itemRenderer;

        public BerthaHitRenderer(EntityRendererProvider.Context ctx) {
            super(ctx);
            this.itemRenderer = ctx.getItemRenderer();
        }

        @Override
        public void render(BerthaHit entity, float yaw, float pt, PoseStack pose, MultiBufferSource buf, int light) {
            pose.pushPose();
            pose.scale(0.5f, 0.5f, 0.5f);
            pose.mulPose(this.entityRenderDispatcher.cameraOrientation());
            this.itemRenderer.renderStatic(
                    new ItemStack(ModItems.BERTHA.get()),
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

        @Override
        public ResourceLocation getTextureLocation(BerthaHit entity) {
            return ResourceLocation.fromNamespaceAndPath("orespawn", "textures/item/berthasmall.png");
        }
    }
}
