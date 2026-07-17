package me.scwunge.mods.portalgun.network;

import me.scwunge.mods.portalgun.client.ClientPortalStatus;
import me.scwunge.mods.portalgun.item.PortalGunData;
import me.scwunge.mods.portalgun.item.PortalGunItem;
import me.scwunge.mods.portalgun.portal.ChannelIndicatorServer;
import me.scwunge.mods.portalgun.portal.PortalSavedData;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload.Type;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent.PlayerChangedDimensionEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent.PlayerLoggedOutEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent.PlayerRespawnEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record PortalStatusPayload(boolean hasBlue, boolean hasOrange) implements CustomPacketPayload {
   public static final Type<PortalStatusPayload> TYPE = new Type(ResourceLocation.fromNamespaceAndPath("orespawn", "portal_status"));
   public static final StreamCodec<RegistryFriendlyByteBuf, PortalStatusPayload> STREAM_CODEC = StreamCodec.composite(
      ByteBufCodecs.BOOL, PortalStatusPayload::hasBlue, ByteBufCodecs.BOOL, PortalStatusPayload::hasOrange, PortalStatusPayload::new
   );

   public Type<? extends CustomPacketPayload> type() {
      return TYPE;
   }

   public static void handle(PortalStatusPayload payload, IPayloadContext context) {
      context.enqueueWork(() -> ClientPortalStatus.set(payload.hasBlue(), payload.hasOrange()));
   }

   public static void syncTo(ServerPlayer player, ServerLevel level, String channel) {
      syncTo(player, level, player.getUUID().toString(), channel);
   }

   public static void syncTo(ServerPlayer player, ServerLevel level, String ownerUuid, String channel) {
      String ch = channel != null && !channel.isBlank() ? channel : "Chell";
      String uuid = ownerUuid != null && !ownerUuid.isBlank() ? ownerUuid : player.getUUID().toString();
      if (uuid.equals(player.getUUID().toString())) {
         PortalSavedData data = PortalSavedData.get(level);
         boolean blue = data.getEntry(uuid, ch, true) != null;
         boolean orange = data.getEntry(uuid, ch, false) != null;
         PacketDistributor.sendToPlayer(player, new PortalStatusPayload(blue, orange), new CustomPacketPayload[0]);
      }
   }

   public static void broadcastChannel(ServerLevel level, String ownerUuid, String channel) {
      if (level != null && ownerUuid != null && level.getServer() != null) {
         for (ServerPlayer player : level.getServer().getPlayerList().getPlayers()) {
            if (player.getUUID().toString().equals(ownerUuid)) {
               Level held = player.level();
               if (held instanceof ServerLevel) {
                  ServerLevel pl = (ServerLevel)held;
                  if (pl.dimension().equals(level.dimension())) {
                     ItemStack heldx = heldGun(player);
                     String heldCh = !heldx.isEmpty() ? PortalGunData.channel(heldx) : channelForPlayer(player);
                     String ch = channel != null && !channel.isBlank() ? channel : heldCh;
                     if (heldCh.equalsIgnoreCase(ch) || playerUsesChannel(player, ch)) {
                        syncTo(player, level, ownerUuid, ch);
                     }
                  }
               }
            }
         }
      }
   }

   private static boolean playerUsesChannel(ServerPlayer player, String channel) {
      ItemStack held = heldGun(player);
      if (!held.isEmpty() && PortalGunData.channel(held).equalsIgnoreCase(channel)) {
         return true;
      } else {
         for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
            ItemStack stack = player.getInventory().getItem(i);
            if (stack.getItem() instanceof PortalGunItem && PortalGunData.channel(stack).equalsIgnoreCase(channel)) {
               return true;
            }
         }

         return false;
      }
   }

   private static ItemStack heldGun(ServerPlayer player) {
      ItemStack main = player.getMainHandItem();
      if (main.getItem() instanceof PortalGunItem) {
         return main;
      } else {
         ItemStack off = player.getOffhandItem();
         return off.getItem() instanceof PortalGunItem ? off : ItemStack.EMPTY;
      }
   }

   public static String channelForPlayer(ServerPlayer player) {
      ItemStack held = heldGun(player);
      if (!held.isEmpty()) {
         return PortalGunData.channel(held);
      } else {
         for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
            ItemStack stack = player.getInventory().getItem(i);
            if (stack.getItem() instanceof PortalGunItem) {
               return PortalGunData.channel(stack);
            }
         }

         return "Chell";
      }
   }

   @EventBusSubscriber(
      modid = "orespawn"
   )
   public static final class LoginSync {
      private LoginSync() {
      }

      @SubscribeEvent
      public static void onPlayerLoggedIn(PlayerLoggedInEvent event) {
         if (event.getEntity() instanceof ServerPlayer player && player.level() instanceof ServerLevel level) {
            PortalStatusPayload.syncTo(player, level, PortalStatusPayload.channelForPlayer(player));
         }
      }

      @SubscribeEvent
      public static void onPlayerLoggedOut(PlayerLoggedOutEvent event) {
         if (event.getEntity() instanceof ServerPlayer player) {
            ChannelIndicatorServer.clearPlayer(player);
         }
      }

      @SubscribeEvent
      public static void onDimChange(PlayerChangedDimensionEvent event) {
         if (event.getEntity() instanceof ServerPlayer player && player.level() instanceof ServerLevel level) {
            PortalStatusPayload.syncTo(player, level, PortalStatusPayload.channelForPlayer(player));
         }
      }

      @SubscribeEvent
      public static void onRespawn(PlayerRespawnEvent event) {
         if (event.getEntity() instanceof ServerPlayer player && player.level() instanceof ServerLevel level) {
            PortalStatusPayload.syncTo(player, level, PortalStatusPayload.channelForPlayer(player));
         }
      }
   }
}
