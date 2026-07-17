package me.scwunge.mods.portalgun.portal;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
import java.util.Map.Entry;
import me.scwunge.mods.portalgun.config.PortalGunConfig;
import me.scwunge.mods.portalgun.entity.PortalProjectile;
import net.minecraft.core.SectionPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ChunkPos;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.EventBusSubscriber.Bus;
import net.neoforged.neoforge.common.world.chunk.RegisterTicketControllersEvent;
import net.neoforged.neoforge.common.world.chunk.TicketController;
import net.neoforged.neoforge.common.world.chunk.TicketHelper;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.EntityEvent.EnteringSection;
import net.neoforged.neoforge.event.server.ServerStoppingEvent;

@EventBusSubscriber(
   modid = "orespawn"
)
public final class ChunkLoadHandler {
   private static final int MAX_TRACKED = 20;
   public static final TicketController CONTROLLER = new TicketController(
      ResourceLocation.fromNamespaceAndPath("orespawn", "portal_projectile"), ChunkLoadHandler::validateTickets
   );
   private static final Map<UUID, ChunkPos> FORCED_CHUNKS = Collections.synchronizedMap(new LinkedHashMap<>());

   private ChunkLoadHandler() {
   }

   private static void validateTickets(ServerLevel level, TicketHelper helper) {
      for (UUID owner : helper.getEntityTickets().keySet()) {
         helper.removeAllTickets(owner);
      }
   }

   private static boolean chunkloadEnabled() {
      try {
         return (Boolean)PortalGunConfig.PROJECTILES_CHUNKLOAD.get();
      } catch (Exception var1) {
         return true;
      }
   }

   public static void forceAt(PortalProjectile projectile) {
      if (chunkloadEnabled() && !projectile.level().isClientSide && !projectile.isRemoved()) {
         if (projectile.level() instanceof ServerLevel level) {
            ChunkPos var3 = projectile.chunkPosition();
            forceChunk(level, projectile.getUUID(), var3);
         }
      }
   }

   private static void forceChunk(ServerLevel level, UUID owner, ChunkPos newChunk) {
      ChunkPos prev;
      synchronized (FORCED_CHUNKS) {
         prev = FORCED_CHUNKS.get(owner);
      }

      if (prev != null && !prev.equals(newChunk)) {
         CONTROLLER.forceChunk(level, owner, prev.x, prev.z, false, true);
      }

      CONTROLLER.forceChunk(level, owner, newChunk.x, newChunk.z, true, true);
      synchronized (FORCED_CHUNKS) {
         FORCED_CHUNKS.remove(owner);
         FORCED_CHUNKS.put(owner, newChunk);

         while (FORCED_CHUNKS.size() > 20) {
            Iterator<Entry<UUID, ChunkPos>> it = FORCED_CHUNKS.entrySet().iterator();
            if (!it.hasNext()) {
               break;
            }

            Entry<UUID, ChunkPos> oldest = it.next();
            CONTROLLER.forceChunk(level, oldest.getKey(), oldest.getValue().x, oldest.getValue().z, false, true);
            it.remove();
         }
      }
   }

   public static void release(PortalProjectile projectile) {
      if (!projectile.level().isClientSide) {
         if (projectile.level() instanceof ServerLevel level) {
            release(level, projectile.getUUID());
         }
      }
   }

   public static void release(ServerLevel level, UUID owner) {
      ChunkPos prev;
      synchronized (FORCED_CHUNKS) {
         prev = FORCED_CHUNKS.remove(owner);
      }

      if (prev != null) {
         CONTROLLER.forceChunk(level, owner, prev.x, prev.z, false, true);
      }
   }

   @SubscribeEvent
   public static void onJoin(EntityJoinLevelEvent event) {
      if (!event.getLevel().isClientSide() && chunkloadEnabled()) {
         if (event.getEntity() instanceof PortalProjectile projectile) {
            forceAt(projectile);
         }
      }
   }

   @SubscribeEvent
   public static void onEnteringSection(EnteringSection event) {
      if (chunkloadEnabled() && event.didChunkChange()) {
         Entity entity = event.getEntity();
         if (!entity.level().isClientSide && !entity.isRemoved() && entity instanceof PortalProjectile projectile) {
            if (entity.level() instanceof ServerLevel level) {
               SectionPos var5 = event.getNewPos();
               forceChunk(level, projectile.getUUID(), new ChunkPos(var5.x(), var5.z()));
            }
         }
      }
   }

   @SubscribeEvent
   public static void onServerStopping(ServerStoppingEvent event) {
      FORCED_CHUNKS.clear();
   }

   @EventBusSubscriber(
      modid = "orespawn",
      bus = Bus.MOD
   )
   public static final class ModBus {
      private ModBus() {
      }

      @SubscribeEvent
      public static void registerTicketControllers(RegisterTicketControllersEvent event) {
         event.register(ChunkLoadHandler.CONTROLLER);
      }
   }
}
