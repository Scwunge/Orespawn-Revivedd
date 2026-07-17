package me.scwunge.mods.portalgun.client;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent.LoggingOut;

@EventBusSubscriber(
   modid = "orespawn",
   value = {Dist.CLIENT}
)
public final class PortalProximityClient {
   private PortalProximityClient() {
   }

   @SubscribeEvent
   public static void onLogout(LoggingOut event) {
   }
}
