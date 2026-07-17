package me.scwunge.mods.portalgun.network;

import me.scwunge.mods.portalgun.client.ClientGrabStatus;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload.Type;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record GrabStatusPayload(boolean holding, int entityId) implements CustomPacketPayload {
   public static final Type<GrabStatusPayload> TYPE = new Type(ResourceLocation.fromNamespaceAndPath("orespawn", "grab_status"));
   public static final StreamCodec<RegistryFriendlyByteBuf, GrabStatusPayload> STREAM_CODEC = StreamCodec.composite(
      ByteBufCodecs.BOOL, GrabStatusPayload::holding, ByteBufCodecs.VAR_INT, GrabStatusPayload::entityId, GrabStatusPayload::new
   );

   public Type<? extends CustomPacketPayload> type() {
      return TYPE;
   }

   public static void handle(GrabStatusPayload payload, IPayloadContext context) {
      context.enqueueWork(() -> ClientGrabStatus.setHolding(payload.holding(), payload.entityId()));
   }

   public static void syncTo(ServerPlayer player, boolean holding) {
      syncTo(player, holding, -1);
   }

   public static void syncTo(ServerPlayer player, boolean holding, int entityId) {
      PacketDistributor.sendToPlayer(player, new GrabStatusPayload(holding, holding ? entityId : -1), new CustomPacketPayload[0]);
   }
}
