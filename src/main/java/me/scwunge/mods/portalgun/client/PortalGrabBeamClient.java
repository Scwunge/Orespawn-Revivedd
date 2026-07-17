package me.scwunge.mods.portalgun.client;

import me.scwunge.mods.portalgun.item.PortalGunItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent.Post;
import org.joml.Vector3f;

@EventBusSubscriber(
   modid = "orespawn",
   value = {Dist.CLIENT}
)
public final class PortalGrabBeamClient {
   private PortalGrabBeamClient() {
   }

   @SubscribeEvent
   public static void onClientTick(Post event) {
      if (ClientGrabStatus.isHolding()) {
         Minecraft mc = Minecraft.getInstance();
         LocalPlayer player = mc.player;
         if (player != null && mc.level != null && !mc.isPaused()) {
            if (player.getMainHandItem().getItem() instanceof PortalGunItem || player.getOffhandItem().getItem() instanceof PortalGunItem) {
               int id = ClientGrabStatus.heldEntityId();
               if (id >= 0) {
                  Entity held = mc.level.getEntity(id);
                  if (held != null && !held.isRemoved()) {
                     Vec3 from = player.getEyePosition(1.0F).add(player.getLookAngle().scale(0.45));
                     Vec3 to = held.getBoundingBox().getCenter();
                     Vector3f colour = Vec3.fromRGB24(361215).toVector3f();
                     int segs = 6;

                     for (int i = 0; i <= segs; i++) {
                        double t = (double)i / (double)segs;
                        double jx = (player.getRandom().nextDouble() - 0.5) * 0.12;
                        double jy = (player.getRandom().nextDouble() - 0.5) * 0.12;
                        double jz = (player.getRandom().nextDouble() - 0.5) * 0.12;
                        if (i == 0 || i == segs) {
                           jz = 0.0;
                           jy = 0.0;
                           jx = 0.0;
                        }

                        double x = from.x + (to.x - from.x) * t + jx;
                        double y = from.y + (to.y - from.y) * t + jy;
                        double z = from.z + (to.z - from.z) * t + jz;
                        mc.level.addParticle(new DustParticleOptions(colour, 0.7F), x, y, z, 0.0, 0.0, 0.0);
                     }
                  }
               }
            }
         }
      }
   }
}
