package me.scwunge.mods.portalgun.portal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.Map.Entry;
import me.scwunge.mods.portalgun.network.ChannelIndicatorPayload;
import me.scwunge.mods.portalgun.portal.info.ChannelIndicator;
import me.scwunge.mods.portalgun.portal.info.ChannelInfo;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.PacketDistributor;

public final class ChannelIndicatorServer {
   private static final Map<String, List<ChannelIndicator>> LISTENERS = new HashMap<>();

   private ChannelIndicatorServer() {
   }

   public static ChannelIndicator getAndListen(ServerPlayer requester, String ownerUuid, String channel, String dimensionId) {
      String ch = channel != null && !channel.isBlank() ? channel : "Chell";
      String uuid = ownerUuid == null ? "" : ownerUuid;
      String dim = dimensionId != null && !dimensionId.isBlank() ? dimensionId : dimId(requester.level());
      String playerKey = requester.getUUID().toString();
      List<ChannelIndicator> list = LISTENERS.computeIfAbsent(playerKey, k -> new ArrayList<>());
      ChannelIndicator existing = null;

      for (ChannelIndicator ind : list) {
         if (ind.info.uuid.equals(uuid) && ind.info.channelName.equalsIgnoreCase(ch) && ind.dimension.equals(dim)) {
            existing = ind;
            break;
         }
      }

      ChannelIndicator indicator = buildIndicator(requester.serverLevel(), uuid, ch, dim);
      if (existing == null) {
         list.add(indicator);
      } else {
         existing.info.setColour(indicator.info.colourA, indicator.info.colourB);
         existing.portalAAvailable = indicator.portalAAvailable;
         existing.portalBAvailable = indicator.portalBAvailable;
         indicator = existing;
      }

      return indicator;
   }

   public static void notifyListeners(ServerLevel level, String ownerUuid, String channel) {
      if (level != null && ownerUuid != null && level.getServer() != null) {
         String ch = channel != null && !channel.isBlank() ? channel : "Chell";
         String dim = dimId(level);
         ChannelIndicator fresh = buildIndicator(level, ownerUuid, ch, dim);

         for (Entry<String, List<ChannelIndicator>> e : LISTENERS.entrySet()) {
            boolean cares = false;

            for (ChannelIndicator ind : e.getValue()) {
               if (ind.info.uuid.equals(ownerUuid) && ind.info.channelName.equalsIgnoreCase(ch) && ind.dimension.equals(dim)) {
                  ind.info.setColour(fresh.info.colourA, fresh.info.colourB);
                  ind.portalAAvailable = fresh.portalAAvailable;
                  ind.portalBAvailable = fresh.portalBAvailable;
                  cares = true;
               }
            }

            if (cares) {
               try {
                  UUID id = UUID.fromString(e.getKey());
                  ServerPlayer player = level.getServer().getPlayerList().getPlayer(id);
                  if (player != null) {
                     send(player, fresh);
                  }
               } catch (IllegalArgumentException var11) {
               }
            }
         }
      }
   }

   public static void clearPlayer(ServerPlayer player) {
      if (player != null) {
         LISTENERS.remove(player.getUUID().toString());
      }
   }

   public static void send(ServerPlayer player, ChannelIndicator ind) {
      PacketDistributor.sendToPlayer(
         player,
         new ChannelIndicatorPayload(
            ind.info.uuid, ind.info.channelName, ind.dimension, ind.info.colourA, ind.portalAAvailable, ind.info.colourB, ind.portalBAvailable
         ),
         new CustomPacketPayload[0]
      );
   }

   public static ChannelIndicator buildIndicator(ServerLevel level, String ownerUuid, String channel, String dimensionId) {
      int[] colours = PortalGunHelper.coloursForChannel(ownerUuid, channel);
      ChannelInfo info = new ChannelInfo(ownerUuid, channel).setColour(colours[0], colours[1]);
      ChannelIndicator indicator = new ChannelIndicator(info, dimensionId);
      ServerLevel target = level;
      if (level.getServer() != null) {
         for (ServerLevel sl : level.getServer().getAllLevels()) {
            if (dimId(sl).equals(dimensionId)) {
               target = sl;
               break;
            }
         }
      }

      PortalSavedData data = PortalSavedData.get(target);
      PortalSavedData.PortalEntry a = data.getEntry(ownerUuid, channel, true);
      PortalSavedData.PortalEntry b = data.getEntry(ownerUuid, channel, false);
      indicator.setPortalAStatus(a != null);
      indicator.setPortalBStatus(b != null);
      return indicator;
   }

   public static String dimId(Level level) {
      ResourceKey<Level> key = level.dimension();
      return key.location().toString();
   }
}
