package me.scwunge.mods.portalgun.client;

import me.scwunge.mods.portalgun.PortalGunMod;
import me.scwunge.mods.portalgun.block.PortalBlock;
import me.scwunge.mods.portalgun.block.entity.PortalMasterBlockEntity;
import me.scwunge.mods.portalgun.client.render.PortalMasterRenderer;
import me.scwunge.mods.portalgun.client.render.PortalProjectileRenderer;
import me.scwunge.mods.portalgun.client.render.PortalStencil;
import me.scwunge.mods.portalgun.entity.PortalCameraEntity;
import me.scwunge.mods.portalgun.init.ModBlockEntities;
import me.scwunge.mods.portalgun.init.ModBlocks;
import me.scwunge.mods.portalgun.init.ModEntities;
import me.scwunge.mods.portalgun.init.ModItems;
import me.scwunge.mods.portalgun.portal.PortalGunHelper;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent.RegisterRenderers;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent.Block;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;

public final class ClientSetup {
   private ClientSetup() {
   }

   public static void register(IEventBus modBus) {
      modBus.addListener(ClientSetup::registerRenderers);
      modBus.addListener(ClientSetup::registerClientExtensions);
      modBus.addListener(ClientSetup::registerBlockColors);
      modBus.addListener(ClientSetup::registerItemColors);
      modBus.addListener(ClientSetup::onClientSetup);
   }

   private static void onClientSetup(FMLClientSetupEvent event) {
      event.enqueueWork(() -> {
         try {
            PortalStencil.ensureEnabled();
         } catch (Throwable var1) {
            PortalGunMod.LOGGER.debug("Stencil enable deferred: {}", var1.toString());
         }
      });
   }

   private static void registerRenderers(RegisterRenderers event) {
      event.registerEntityRenderer((EntityType)ModEntities.PORTAL_PROJECTILE.get(), PortalProjectileRenderer::new);
      event.registerEntityRenderer((EntityType)ModEntities.PORTAL_CAMERA.get(), ClientSetup.PortalCameraRenderer::new);
      event.registerBlockEntityRenderer((BlockEntityType)ModBlockEntities.PORTAL_MASTER.get(), PortalMasterRenderer::new);
   }

   private static void registerClientExtensions(RegisterClientExtensionsEvent event) {
      event.registerItem(new PortalGunClientItemExtensions(), new Item[]{(Item)ModItems.PORTAL_GUN.get()});
      PortalGunMod.LOGGER.info("Registered portal gun 3D item renderer (BEWLR)");
   }

   private static void registerBlockColors(Block event) {
      event.register((state, level, pos, tintIndex) -> {
         if (tintIndex != 0) {
            return 16777215;
         } else if (level != null && pos != null && level.getBlockEntity(pos) instanceof PortalMasterBlockEntity be) {
            int c = be.getColour() & 16777215;
            if (c != 0 && c != 16777215) {
               return c;
            } else {
               int[] pair = PortalGunHelper.coloursForChannel(be.getUuid(), be.getChannelName());
               return (be.isTypeA() ? pair[0] : pair[1]) & 16777215;
            }
         } else {
            return state.getValue(PortalBlock.TYPE_A) ? 361215 : 16756742;
         }
      }, new net.minecraft.world.level.block.Block[]{(net.minecraft.world.level.block.Block)ModBlocks.PORTAL.get()});
   }

   private static void registerItemColors(net.neoforged.neoforge.client.event.RegisterColorHandlersEvent.Item event) {
      event.register((stack, tintIndex) -> tintIndex == 0 ? 361215 : 16777215, new ItemLike[]{(ItemLike)ModBlocks.PORTAL_ITEM.get()});
   }

   private static final class PortalCameraRenderer extends EntityRenderer<PortalCameraEntity> {
      private PortalCameraRenderer(Context ctx) {
         super(ctx);
      }

      public ResourceLocation getTextureLocation(PortalCameraEntity entity) {
         return ResourceLocation.withDefaultNamespace("textures/misc/white.png");
      }

      public boolean shouldRender(PortalCameraEntity entity, Frustum frustum, double x, double y, double z) {
         return false;
      }
   }
}

