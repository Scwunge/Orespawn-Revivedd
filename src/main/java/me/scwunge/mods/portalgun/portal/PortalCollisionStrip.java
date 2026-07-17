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
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;

public final class PortalCollisionStrip {
   private static final double HOLE_DEPTH = 1.15;
   private static final double FRONT_PAD = 0.55;
   private static final double CORE_INSET = 0.08;
   private static final int CLIENT_SCAN = 4;

   private PortalCollisionStrip() {
   }

   public static List<VoxelShape> filterColliders(Entity entity, List<VoxelShape> shapes) {
      if (entity != null && shapes != null && !shapes.isEmpty()) {
         Level level = entity.level();
         if (level == null) {
            return shapes;
         } else {
            return level instanceof ServerLevel server
               ? filterWithHoles(entity, shapes, collectServerHoles(entity, server))
               : filterWithHoles(entity, shapes, collectClientHoles(entity, level));
         }
      } else {
         return shapes;
      }
   }

   private static List<AABB> collectServerHoles(Entity entity, ServerLevel level) {
      PortalSavedData data = PortalSavedData.get(level);
      if (data.getPortals().isEmpty()) {
         return List.of();
      } else {
         Vec3 center = entity.getBoundingBox().getCenter();
         List<AABB> holes = new ArrayList<>(2);

         for (PortalSavedData.PortalEntry entry : data.getPortals().values()) {
            PortalSavedData.PortalEntry pair = data.getEntry(entry.info.uuid, entry.info.channelName, !entry.info.isTypeA);
            if (pair != null) {
               Vec3 pc = PortalTransform.portalCenter(entry);
               if (!(center.distanceToSqr(pc) > 36.0) && (nearCoreForStrip(center, entry) || nearCoreForStrip(entity.position(), entry))) {
                  holes.add(removalAabb(entry));
               }
            }
         }

         return holes;
      }
   }

   private static List<AABB> collectClientHoles(Entity entity, Level level) {
      BlockPos base = entity.blockPosition();
      Vec3 center = entity.getBoundingBox().getCenter();
      List<AABB> holes = new ArrayList<>(2);
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
                     if (master.isOrigin() && master.hasPair()) {
                        Direction face = master.getFacing();
                        Direction up = master.getUpDir();
                        int w = master.getPortalWidth();
                        int h = master.getPortalHeight();
                        BlockPos origin = master.getBlockPos();
                        Vec3 pc = apertureCenter(origin, face, up, w, h);
                        if (!(center.distanceToSqr(pc) > 36.0)
                           && (nearCoreForStrip(center, origin, face, up, w, h) || nearCoreForStrip(entity.position(), origin, face, up, w, h))) {
                           holes.add(removalAabb(origin, face, up, w, h));
                        }
                     }
                  }
               }
            }
         }
      }

      return holes;
   }

   private static List<VoxelShape> filterWithHoles(Entity entity, List<VoxelShape> shapes, List<AABB> holes) {
      if (holes != null && !holes.isEmpty()) {
         List<VoxelShape> out = new ArrayList<>(shapes.size());

         for (VoxelShape shape : shapes) {
            if (!shape.isEmpty()) {
               AABB shapeBox = shape.bounds();
               boolean remove = false;

               for (AABB hole : holes) {
                  if (shapeBox.intersects(hole)) {
                     remove = true;
                     break;
                  }
               }

               if (!remove) {
                  out.add(shape);
               }
            }
         }

         return out;
      } else {
         return shapes;
      }
   }

   public static AABB removalAabb(PortalSavedData.PortalEntry entry) {
      return removalAabb(entry.info.getPos(), entry.face, PortalTransform.upOf(entry), entry.width, entry.height);
   }

   public static AABB removalAabb(BlockPos origin, Direction face, Direction upDir, int width, int height) {
      Vec3 c = apertureCenter(origin, face, upDir, width, height);
      Direction wDir = PortalGunHelper.widthDirection(face, upDir);
      Direction hDir = PortalGunHelper.heightDirection(face, upDir);
      double halfW = Math.max(0.15, (double)width * 0.5 - 0.08);
      double halfH = Math.max(0.15, (double)height * 0.5 - 0.08);
      Vec3 n = new Vec3((double)face.getStepX(), (double)face.getStepY(), (double)face.getStepZ());
      Vec3 w = new Vec3((double)wDir.getStepX(), (double)wDir.getStepY(), (double)wDir.getStepZ()).scale(halfW);
      Vec3 h = new Vec3((double)hDir.getStepX(), (double)hDir.getStepY(), (double)hDir.getStepZ()).scale(halfH);
      Vec3 back = c.subtract(n.scale(1.15));
      Vec3 front = c.add(n.scale(0.55));
      double minX = Math.min(back.x, front.x) - Math.abs(w.x) - Math.abs(h.x);
      double maxX = Math.max(back.x, front.x) + Math.abs(w.x) + Math.abs(h.x);
      double minY = Math.min(back.y, front.y) - Math.abs(w.y) - Math.abs(h.y);
      double maxY = Math.max(back.y, front.y) + Math.abs(w.y) + Math.abs(h.y);
      double minZ = Math.min(back.z, front.z) - Math.abs(w.z) - Math.abs(h.z);
      double maxZ = Math.max(back.z, front.z) + Math.abs(w.z) + Math.abs(h.z);
      return new AABB(minX, minY, minZ, maxX, maxY, maxZ);
   }

   private static Vec3 apertureCenter(BlockPos origin, Direction face, Direction upDir, int width, int height) {
      Direction widthDir = PortalGunHelper.widthDirection(face, upDir);
      Direction heightDir = PortalGunHelper.heightDirection(face, upDir);
      int w0 = PortalGunHelper.widthStart(width);
      int h0 = PortalGunHelper.heightStart(face, height);
      double midW = (double)w0 + (double)(width - 1) * 0.5;
      double midH = (double)h0 + (double)(height - 1) * 0.5;
      double cx = (double)origin.getX() + 0.5 + (double)widthDir.getStepX() * midW + (double)heightDir.getStepX() * midH;
      double cy = (double)origin.getY() + 0.5 + (double)widthDir.getStepY() * midW + (double)heightDir.getStepY() * midH;
      double cz = (double)origin.getZ() + 0.5 + (double)widthDir.getStepZ() * midW + (double)heightDir.getStepZ() * midH;
      cx += (double)face.getStepX() * 0.05;
      cy += (double)face.getStepY() * 0.05;
      cz += (double)face.getStepZ() * 0.05;
      return new Vec3(cx, cy, cz);
   }

   private static boolean nearCoreForStrip(Vec3 body, PortalSavedData.PortalEntry entry) {
      return nearCoreForStrip(body, entry.info.getPos(), entry.face, PortalTransform.upOf(entry), entry.width, entry.height);
   }

   private static boolean nearCoreForStrip(Vec3 body, BlockPos origin, Direction face, Direction up, int width, int height) {
      Vec3 c = apertureCenter(origin, face, up, width, height);
      Vec3 n = new Vec3((double)face.getStepX(), (double)face.getStepY(), (double)face.getStepZ());
      double alongN = body.subtract(c).dot(n);
      if (!(alongN < -1.4) && !(alongN > 1.55)) {
         Direction wDir = PortalGunHelper.widthDirection(face, up);
         Direction hDir = PortalGunHelper.heightDirection(face, up);
         Vec3 d = body.subtract(c).subtract(n.scale(alongN));
         double alongW = Math.abs(d.dot(new Vec3((double)wDir.getStepX(), (double)wDir.getStepY(), (double)wDir.getStepZ())));
         double alongH = Math.abs(d.dot(new Vec3((double)hDir.getStepX(), (double)hDir.getStepY(), (double)hDir.getStepZ())));
         double halfW = (double)width * 0.5 - 0.08;
         double halfH = (double)height * 0.5 - 0.08;
         return alongW <= halfW + 0.28 && alongH <= halfH + 0.28;
      } else {
         return false;
      }
   }

   public static boolean isInStrippableHole(Entity entity) {
      if (entity != null && entity.level() != null) {
         Level level = entity.level();
         List<AABB> holes;
         if (level instanceof ServerLevel server) {
            holes = collectServerHoles(entity, server);
         } else {
            holes = collectClientHoles(entity, level);
         }

         if (holes.isEmpty()) {
            return false;
         } else {
            AABB box = entity.getBoundingBox().inflate(0.05);

            for (AABB hole : holes) {
               if (hole.intersects(box)) {
                  return true;
               }
            }

            return false;
         }
      } else {
         return false;
      }
   }
}
