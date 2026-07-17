package me.scwunge.mods.portalgun.network;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.EventBusSubscriber.Bus;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@EventBusSubscriber(
   modid = "orespawn",
   bus = Bus.MOD
)
public final class ModNetwork {
   private ModNetwork() {
   }

   @SubscribeEvent
   public static void register(RegisterPayloadHandlersEvent event) {
      PayloadRegistrar registrar = event.registrar("1");
      registrar.playToServer(PortalGunActionPayload.TYPE, PortalGunActionPayload.STREAM_CODEC, PortalGunActionPayload::handle);
      registrar.playToServer(SetChannelPayload.TYPE, SetChannelPayload.STREAM_CODEC, SetChannelPayload::handle);
      registrar.playToClient(PortalStatusPayload.TYPE, PortalStatusPayload.STREAM_CODEC, PortalStatusPayload::handle);
      registrar.playToClient(GrabStatusPayload.TYPE, GrabStatusPayload.STREAM_CODEC, GrabStatusPayload::handle);
      registrar.playToServer(RequestChannelIndicatorPayload.TYPE, RequestChannelIndicatorPayload.STREAM_CODEC, RequestChannelIndicatorPayload::handle);
      registrar.playToClient(ChannelIndicatorPayload.TYPE, ChannelIndicatorPayload.STREAM_CODEC, ChannelIndicatorPayload::handle);
   }
}
