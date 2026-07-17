package me.scwunge.mods.portalgun.portal;

import java.util.ArrayList;
import java.util.List;
import me.scwunge.mods.portalgun.block.entity.PortalMasterBlockEntity;
import me.scwunge.mods.portalgun.portal.worldportal.PortalTransform;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.SectionPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.EntityTickEvent.Post;

@EventBusSubscriber(
   modid = "orespawn"
)
public final class PortalRimCollision {
   private static final double RIM = 0.16;
   private static final double DEPTH = 0.14;
   private static final int CLIENT_SCAN = 4;

   private PortalRimCollision() {
   }

   @SubscribeEvent(
      priority = EventPriority.HIGH
   )
   public static void onEntityTickPost(Post event) {
      Entity entity = event.getEntity();
      if (entity.isRemoved()) {
         return;
      }
      // Cheap filter: only players every tick; other living every 4 ticks; skip items
      if (entity instanceof ItemEntity) {
         return;
      }
      if (!(entity instanceof Player) && !(entity instanceof LivingEntity)) {
         return;
      }
      if (!(entity instanceof Player) && (entity.tickCount & 3) != 0) {
         return;
      }
      Level level = entity.level();
      if (level instanceof ServerLevel server) {
         applyServer(entity, server);
      } else if (level.isClientSide() && entity instanceof Player) {
         applyClient(entity, level);
      }
   }

   private static void applyServer(Entity entity, ServerLevel level) {
      PortalSavedData data = PortalSavedData.get(level);
      if (!data.getPortals().isEmpty()) {
         AABB box = entity.getBoundingBox();
         Vec3 center = box.getCenter();

         for (PortalSavedData.PortalEntry entry : data.getPortals().values()) {
            if (!entry.face.getAxis().isVertical()) {
               Vec3 pc = PortalTransform.portalCenter(entry);
               if (!(center.distanceToSqr(pc) > 64.0)
                  && !PortalTeleport.isInPortalCore(entity, entry)
                  && !PortalCollisionStrip.isInStrippableHole(entity)
                  && nearRimBand(center, entry.info.getPos(), entry.face, PortalTransform.upOf(entry), entry.width, entry.height)) {
                  Direction face = entry.face;
                  Direction up = PortalTransform.upOf(entry);

                  for (AABB rim : rimBoxes(entry.info.getPos(), face, up, entry.width, entry.height)) {
                     if (box.intersects(rim)) {
                        pushOut(entity, rim, face, up, entry.info.getPos(), entry.width, entry.height);
                        box = entity.getBoundingBox();
                     }
                  }
               }
            }
         }
      }
   }

   private static void applyClient(Entity entity, Level level) {
      BlockPos base = entity.blockPosition();
      AABB box = entity.getBoundingBox();
      Vec3 center = box.getCenter();
      int minCx = SectionPos.blockToSectionCoord(base.getX() - 4);
      int maxCx = SectionPos.blockToSectionCoord(base.getX() + 4);
      int minCz = SectionPos.blockToSectionCoord(base.getZ() - 4);
      int maxCz = SectionPos.blockToSectionCoord(base.getZ() + 4);

      for (int cx = minCx; cx <= maxCx; cx++) {
         for (int cz = minCz; cz <= maxCz; cz++) {
            if (level.hasChunk(cx, cz)) {
               LevelChunk chunk = level.getChunk(cx, cz);

               for (BlockEntity be : chunk.getBlockEntities().values()) {
                  if (be instanceof PortalMasterBlockEntity) {
                     PortalMasterBlockEntity master = (PortalMasterBlockEntity)be;
                     if (master.isOrigin()) {
                        Direction face = master.getFacing();
                        if (!face.getAxis().isVertical()) {
                           Direction up = master.getUpDir();
                           int w = master.getPortalWidth();
                           int h = master.getPortalHeight();
                           BlockPos origin = master.getBlockPos();
                           Vec3 pc = PortalCollisionStrip.removalAabb(origin, face, up, w, h).getCenter();
                           if (!(center.distanceToSqr(pc) > 64.0)
                              && !PortalCollisionStrip.isInStrippableHole(entity)
                              && !nearCoreLoose(center, origin, face, up, w, h)
                              && nearRimBand(center, origin, face, up, w, h)) {
                              for (AABB rim : rimBoxes(origin, face, up, w, h)) {
                                 if (box.intersects(rim)) {
                                    pushOut(entity, rim, face, up, origin, w, h);
                                    box = entity.getBoundingBox();
                                 }
                              }
                           }
                        }
                     }
                  }
               }
            }
         }
      }
   }

   private static boolean nearCoreLoose(Vec3 body, BlockPos origin, Direction face, Direction up, int width, int height) {
      Vec3 c = PortalCollisionStrip.removalAabb(origin, face, up, width, height).getCenter();
      Direction wDir = PortalGunHelper.widthDirection(face, up);
      Direction hDir = PortalGunHelper.heightDirection(face, up);
      Vec3 n = new Vec3((double)face.getStepX(), (double)face.getStepY(), (double)face.getStepZ());
      double alongN = body.subtract(c).dot(n);
      if (Math.abs(alongN) > 0.85) {
         return false;
      } else {
         Vec3 d = body.subtract(c).subtract(n.scale(alongN));
         double alongW = Math.abs(d.dot(new Vec3((double)wDir.getStepX(), (double)wDir.getStepY(), (double)wDir.getStepZ())));
         double alongH = Math.abs(d.dot(new Vec3((double)hDir.getStepX(), (double)hDir.getStepY(), (double)hDir.getStepZ())));
         return alongW <= (double)width * 0.5 - 0.08 && alongH <= (double)height * 0.5 - 0.08;
      }
   }

   private static boolean nearRimBand(Vec3 body, BlockPos origin, Direction face, Direction up, int width, int height) {
      Direction wDir = PortalGunHelper.widthDirection(face, up);
      Direction hDir = PortalGunHelper.heightDirection(face, up);
      Vec3 c = apertureCenter(origin, face, up, width, height);
      Vec3 n = new Vec3((double)face.getStepX(), (double)face.getStepY(), (double)face.getStepZ());
      double alongN = body.subtract(c).dot(n);
      if (!(alongN < -0.3) && !(alongN > 0.35)) {
         Vec3 d = body.subtract(c).subtract(n.scale(alongN));
         double alongW = Math.abs(d.dot(new Vec3((double)wDir.getStepX(), (double)wDir.getStepY(), (double)wDir.getStepZ())));
         double alongH = Math.abs(d.dot(new Vec3((double)hDir.getStepX(), (double)hDir.getStepY(), (double)hDir.getStepZ())));
         double halfW = (double)width * 0.5;
         double halfH = (double)height * 0.5;
         boolean inOuter = alongW <= halfW + 0.16 && alongH <= halfH + 0.16;
         boolean inInner = alongW <= Math.max(0.05, halfW - 0.06) && alongH <= Math.max(0.05, halfH - 0.06);
         return inOuter && !inInner;
      } else {
         return false;
      }
   }

   static List<AABB> rimBoxes(BlockPos origin, Direction face, Direction up, int width, int height) {
      List<AABB> list = new ArrayList<>(4);
      Direction wDir = PortalGunHelper.widthDirection(face, up);
      Direction hDir = PortalGunHelper.heightDirection(face, up);
      Vec3 c = apertureCenter(origin, face, up, width, height);
      double halfW = (double)width * 0.5;
      double halfH = (double)height * 0.5;
      Vec3 base = c.add((double)face.getStepX() * -0.021, (double)face.getStepY() * -0.021, (double)face.getStepZ() * -0.021);
      double halfDepth = 0.07;
      list.add(strip(base, wDir, hDir, face, halfW + 0.08, 0.0, 0.08, halfH + 0.16, halfDepth));
      list.add(strip(base, wDir, hDir, face, -(halfW + 0.08), 0.0, 0.08, halfH + 0.16, halfDepth));
      list.add(strip(base, wDir, hDir, face, 0.0, halfH + 0.08, halfW + 0.16, 0.08, halfDepth));
      list.add(strip(base, wDir, hDir, face, 0.0, -(halfH + 0.08), halfW + 0.16, 0.08, halfDepth));
      return list;
   }

   private static Vec3 apertureCenter(BlockPos origin, Direction face, Direction up, int width, int height) {
      Direction wDir = PortalGunHelper.widthDirection(face, up);
      Direction hDir = PortalGunHelper.heightDirection(face, up);
      int w0 = PortalGunHelper.widthStart(width);
      int h0 = PortalGunHelper.heightStart(face, height);
      double midW = (double)w0 + (double)(width - 1) * 0.5;
      double midH = (double)h0 + (double)(height - 1) * 0.5;
      double cx = (double)origin.getX() + 0.5 + (double)wDir.getStepX() * midW + (double)hDir.getStepX() * midH + (double)face.getStepX() * 0.05;
      double cy = (double)origin.getY() + 0.5 + (double)wDir.getStepY() * midW + (double)hDir.getStepY() * midH + (double)face.getStepY() * 0.05;
      double cz = (double)origin.getZ() + 0.5 + (double)wDir.getStepZ() * midW + (double)hDir.getStepZ() * midH + (double)face.getStepZ() * 0.05;
      return new Vec3(cx, cy, cz);
   }

   private static AABB strip(Vec3 base, Direction wDir, Direction hDir, Direction face, double offW, double offH, double halfW, double halfH, double halfN) {
      double cx = base.x + (double)wDir.getStepX() * offW + (double)hDir.getStepX() * offH;
      double cy = base.y + (double)wDir.getStepY() * offW + (double)hDir.getStepY() * offH;
      double cz = base.z + (double)wDir.getStepZ() * offW + (double)hDir.getStepZ() * offH;
      double ex = (double)Math.abs(wDir.getStepX()) * halfW + (double)Math.abs(hDir.getStepX()) * halfH + (double)Math.abs(face.getStepX()) * halfN;
      double ey = (double)Math.abs(wDir.getStepY()) * halfW + (double)Math.abs(hDir.getStepY()) * halfH + (double)Math.abs(face.getStepY()) * halfN;
      double ez = (double)Math.abs(wDir.getStepZ()) * halfW + (double)Math.abs(hDir.getStepZ()) * halfH + (double)Math.abs(face.getStepZ()) * halfN;
      ex = Math.max(ex, 0.04);
      ey = Math.max(ey, 0.04);
      ez = Math.max(ez, 0.04);
      return new AABB(cx - ex, cy - ey, cz - ez, cx + ex, cy + ey, cz + ez);
   }

   private static void pushOut(Entity entity, AABB solid, Direction face, Direction up, BlockPos origin, int width, int height) {
      AABB box = entity.getBoundingBox();
      if (box.intersects(solid)) {
         double penXPos = solid.maxX - box.minX;
         double penXNeg = box.maxX - solid.minX;
         double penYPos = solid.maxY - box.minY;
         double penYNeg = box.maxY - solid.minY;
         double penZPos = solid.maxZ - box.minZ;
         double penZNeg = box.maxZ - solid.minZ;
         double best = Double.MAX_VALUE;
         double mx = 0.0;
         double my = 0.0;
         double mz = 0.0;
         if (penXPos > 0.0 && penXPos < best) {
            best = penXPos;
            mx = penXPos;
            my = 0.0;
            mz = 0.0;
         }

         if (penXNeg > 0.0 && penXNeg < best) {
            best = penXNeg;
            mx = -penXNeg;
            my = 0.0;
            mz = 0.0;
         }

         if (penYPos > 0.0 && penYPos < best) {
            best = penYPos;
            mx = 0.0;
            my = penYPos;
            mz = 0.0;
         }

         if (penYNeg > 0.0 && penYNeg < best) {
            best = penYNeg;
            mx = 0.0;
            my = -penYNeg;
            mz = 0.0;
         }

         if (penZPos > 0.0 && penZPos < best) {
            best = penZPos;
            mx = 0.0;
            my = 0.0;
            mz = penZPos;
         }

         if (penZNeg > 0.0 && penZNeg < best) {
            best = penZNeg;
            mx = 0.0;
            my = 0.0;
            mz = -penZNeg;
         }

         if (!(best >= Double.MAX_VALUE) && !(best <= 1.0E-4)) {
            Vec3 c = apertureCenter(origin, face, up, width, height);
            Vec3 body = box.getCenter();
            Direction wDir = PortalGunHelper.widthDirection(face, up);
            Direction hDir = PortalGunHelper.heightDirection(face, up);
            Vec3 n = new Vec3((double)face.getStepX(), (double)face.getStepY(), (double)face.getStepZ());
            Vec3 planar = body.subtract(c).subtract(n.scale(body.subtract(c).dot(n)));
            Vec3 after = body.add(mx, my, mz);
            Vec3 afterPlanar = after.subtract(c).subtract(n.scale(after.subtract(c).dot(n)));
            double halfW = (double)width * 0.5;
            double halfH = (double)height * 0.5;
            double afterW = Math.abs(afterPlanar.dot(new Vec3((double)wDir.getStepX(), (double)wDir.getStepY(), (double)wDir.getStepZ())));
            double afterH = Math.abs(afterPlanar.dot(new Vec3((double)hDir.getStepX(), (double)hDir.getStepY(), (double)hDir.getStepZ())));
            boolean intoCorridor = afterW < halfW - 0.05 && afterH < halfH - 0.05;
            if (intoCorridor && planar.lengthSqr() > 1.0E-6) {
               Vec3 out = planar.normalize().scale(best + 0.02);
               mx = out.x;
               my = out.y;
               mz = out.z;
            }

            entity.setPos(entity.getX() + mx, entity.getY() + my, entity.getZ() + mz);
            Vec3 m = entity.getDeltaMovement();
            double nx = Math.abs(mx) > 1.0E-4 ? 0.0 : m.x;
            double ny = Math.abs(my) > 1.0E-4 ? 0.0 : m.y;
            double nz = Math.abs(mz) > 1.0E-4 ? 0.0 : m.z;
            entity.setDeltaMovement(nx, ny, nz);
            entity.hurtMarked = true;
            entity.hasImpulse = true;
         }
      }
   }
}
