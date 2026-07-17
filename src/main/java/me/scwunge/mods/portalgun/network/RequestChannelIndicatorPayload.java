package me.scwunge.mods.portalgun.network;

import me.scwunge.mods.portalgun.item.PortalGunData;
import me.scwunge.mods.portalgun.portal.ChannelIndicatorServer;
import me.scwunge.mods.portalgun.portal.info.ChannelIndicator;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload.Type;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record RequestChannelIndicatorPayload(String ownerUuid, String channel, String dimension) implements CustomPacketPayload {
   public static final Type<RequestChannelIndicatorPayload> TYPE = new Type(ResourceLocation.fromNamespaceAndPath("orespawn", "request_channel_indicator"));
   public static final StreamCodec<RegistryFriendlyByteBuf, RequestChannelIndicatorPayload> STREAM_CODEC = StreamCodec.composite(
      ByteBufCodecs.STRING_UTF8,
      RequestChannelIndicatorPayload::ownerUuid,
      ByteBufCodecs.STRING_UTF8,
      RequestChannelIndicatorPayload::channel,
      ByteBufCodecs.STRING_UTF8,
      RequestChannelIndicatorPayload::dimension,
      RequestChannelIndicatorPayload::new
   );

   public Type<? extends CustomPacketPayload> type() {
      return TYPE;
   }

   public static void handle(RequestChannelIndicatorPayload payload, IPayloadContext context) {
      context.enqueueWork(() -> {
         if (context.player() instanceof ServerPlayer player) {
            String var7 = PortalGunData.sanitizeChannel(payload.channel());
            String uuid = payload.ownerUuid() == null ? "" : payload.ownerUuid();
            if (uuid.length() > 64) {
               uuid = uuid.substring(0, 64);
            }

            String dim = payload.dimension() == null ? "" : payload.dimension();
            if (dim.length() > 128) {
               dim = dim.substring(0, 128);
            }

            ChannelIndicator indicator = ChannelIndicatorServer.getAndListen(player, uuid, var7, dim);
            ChannelIndicatorServer.send(player, indicator);
         }
      });
   }
}
