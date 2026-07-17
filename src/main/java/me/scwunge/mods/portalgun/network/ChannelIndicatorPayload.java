package me.scwunge.mods.portalgun.network;

import me.scwunge.mods.portalgun.client.ClientChannelIndicators;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload.Type;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record ChannelIndicatorPayload(String ownerUuid, String channel, String dimension, int colourA, boolean portalA, int colourB, boolean portalB)
   implements CustomPacketPayload {
   public static final Type<ChannelIndicatorPayload> TYPE = new Type(ResourceLocation.fromNamespaceAndPath("orespawn", "channel_indicator"));
   public static final StreamCodec<RegistryFriendlyByteBuf, ChannelIndicatorPayload> STREAM_CODEC = StreamCodec.of(
      (buf, p) -> {
         ByteBufCodecs.STRING_UTF8.encode(buf, p.ownerUuid());
         ByteBufCodecs.STRING_UTF8.encode(buf, p.channel());
         ByteBufCodecs.STRING_UTF8.encode(buf, p.dimension());
         ByteBufCodecs.VAR_INT.encode(buf, p.colourA());
         ByteBufCodecs.BOOL.encode(buf, p.portalA());
         ByteBufCodecs.VAR_INT.encode(buf, p.colourB());
         ByteBufCodecs.BOOL.encode(buf, p.portalB());
      },
      buf -> new ChannelIndicatorPayload(
            (String)ByteBufCodecs.STRING_UTF8.decode(buf),
            (String)ByteBufCodecs.STRING_UTF8.decode(buf),
            (String)ByteBufCodecs.STRING_UTF8.decode(buf),
            (Integer)ByteBufCodecs.VAR_INT.decode(buf),
            (Boolean)ByteBufCodecs.BOOL.decode(buf),
            (Integer)ByteBufCodecs.VAR_INT.decode(buf),
            (Boolean)ByteBufCodecs.BOOL.decode(buf)
         )
   );

   public Type<? extends CustomPacketPayload> type() {
      return TYPE;
   }

   public static void handle(ChannelIndicatorPayload payload, IPayloadContext context) {
      context.enqueueWork(
         () -> ClientChannelIndicators.apply(
               payload.ownerUuid(), payload.channel(), payload.dimension(), payload.colourA(), payload.portalA(), payload.colourB(), payload.portalB()
            )
      );
   }
}
