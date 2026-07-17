package me.scwunge.mods.portalgun.client;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import me.scwunge.mods.portalgun.block.PortalBlock;
import me.scwunge.mods.portalgun.block.entity.PortalMasterBlockEntity;
import me.scwunge.mods.portalgun.client.sound.PortalAmbienceSound;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent.Post;

@EventBusSubscriber(
   modid = "orespawn",
   value = {Dist.CLIENT}
)
public final class PortalAmbientClient {
   private static final int SCAN_INTERVAL = 20;
   private static final int RANGE = 5;
   private static final float VOLUME = 0.12F;
   private static final float PITCH = 1.0F;
   private static final Map<Long, PortalAmbienceSound> ACTIVE = new HashMap<>();
   private static int tickCounter;

   private PortalAmbientClient() {
   }

   @SubscribeEvent
   public static void onClientTick(Post event) {
      Minecraft mc = Minecraft.getInstance();
      if (mc.player != null && mc.level != null && !mc.isPaused()) {
         tickCounter++;
         if (tickCounter >= 20) {
            tickCounter = 0;
            Iterator<Entry<Long, PortalAmbienceSound>> it = ACTIVE.entrySet().iterator();

            while (it.hasNext()) {
               Entry<Long, PortalAmbienceSound> e = it.next();
               PortalAmbienceSound snd = e.getValue();
               if (snd.isStopped() || mc.player.blockPosition().distSqr(snd.getPortalPos()) > 25.0) {
                  mc.getSoundManager().stop(snd);
                  it.remove();
               }
            }

            BlockPos origin = mc.player.blockPosition();
            int r = 5;

            for (int dx = -r; dx <= r; dx++) {
               for (int dy = -2; dy <= 4; dy++) {
                  for (int dz = -r; dz <= r; dz++) {
                     if (dx * dx + dy * dy + dz * dz <= r * r) {
                        BlockPos pos = origin.offset(dx, dy, dz);
                        BlockState state = mc.level.getBlockState(pos);
                        if (state.getBlock() instanceof PortalBlock) {
                           BlockEntity be = mc.level.getBlockEntity(pos);
                           if (be instanceof PortalMasterBlockEntity) {
                              PortalMasterBlockEntity master = (PortalMasterBlockEntity)be;
                              if (master.isOrigin()) {
                                 long key = pos.asLong();
                                 if (!ACTIVE.containsKey(key)) {
                                    PortalAmbienceSound sound = new PortalAmbienceSound(mc.level, pos, 0.12F, 1.0F);
                                    ACTIVE.put(key, sound);
                                    mc.getSoundManager().play(sound);
                                 }
                              }
                           }
                        }
                     }
                  }
               }
            }
         }
      } else {
         stopAll(mc);
      }
   }

   private static void stopAll(Minecraft mc) {
      if (!ACTIVE.isEmpty()) {
         for (PortalAmbienceSound snd : ACTIVE.values()) {
            mc.getSoundManager().stop(snd);
         }

         ACTIVE.clear();
      }
   }
}
