package me.scwunge.mods.portalgun.portal;

import java.util.ArrayList;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.ServerTickEvent.Post;

@EventBusSubscriber(
   modid = "orespawn"
)
public final class PortalIntegrity {
   private static final int INTERVAL = 40;

   private PortalIntegrity() {
   }

   @SubscribeEvent
   public static void onServerTick(Post event) {
      if (event.getServer().getTickCount() % 40 == 0) {
         for (ServerLevel level : event.getServer().getAllLevels()) {
            validateLevel(level);
         }
      }
   }

   private static void validateLevel(ServerLevel level) {
      PortalSavedData data = PortalSavedData.get(level);
      if (!data.getPortals().isEmpty()) {
         for (PortalSavedData.PortalEntry entry : new ArrayList<>(data.getPortals().values())) {
            if (entry != null && entry.info != null && !hasSturdySupport(level, entry)) {
               PortalGunHelper.fizzleEntry(level, entry);
            }
         }
      }
   }

   public static boolean hasSturdySupport(ServerLevel level, PortalSavedData.PortalEntry entry) {
      Direction face = entry.face;
      if (face == null) {
         return false;
      } else {
         Direction up = entry.upDir != null ? entry.upDir : PortalGunHelper.heightDirection(face);
         BlockPos origin = entry.info.getPos();
         boolean[] ok = new boolean[]{true};
         PortalGunHelper.forEachPortalCell(origin, face, up, Math.max(1, entry.width), Math.max(1, entry.height), cell -> {
            BlockPos support = cell.relative(face.getOpposite());
            BlockState state = level.getBlockState(support);
            if (!state.isFaceSturdy(level, support, face)) {
               ok[0] = false;
               return true;
            } else {
               return false;
            }
         });
         return ok[0];
      }
   }
}
