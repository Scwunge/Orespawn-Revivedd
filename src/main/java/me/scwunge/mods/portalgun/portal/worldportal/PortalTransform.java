package me.scwunge.mods.portalgun.portal.worldportal;

import me.scwunge.mods.portalgun.portal.PortalGunHelper;
import me.scwunge.mods.portalgun.portal.PortalSavedData;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

public final class PortalTransform {
   private PortalTransform() {
   }

   public static Direction defaultUp(Direction face) {
      return PortalGunHelper.heightDirection(face);
   }

   public static Direction upOf(PortalSavedData.PortalEntry entry) {
      return entry.upDir != null ? entry.upDir : defaultUp(entry.face);
   }

   public static PortalQuaternionFormula formula(PortalSavedData.PortalEntry from, PortalSavedData.PortalEntry to) {
      Direction inUp = upOf(from);
      Direction outUp = upOf(to);
      return PortalQuaternionFormula.createFromPlanes(from.face, inUp, to.face, outUp);
   }

   public static Vec3 portalCenter(PortalSavedData.PortalEntry entry) {
      BlockPos origin = entry.info.getPos();
      Direction face = entry.face;
      int w = entry.width;
      int h = entry.height;
      Direction up = upOf(entry);
      Direction widthDir = PortalGunHelper.widthDirection(face, up);
      Direction heightDir = PortalGunHelper.heightDirection(face, up);
      int w0 = PortalGunHelper.widthStart(w);
      int h0 = PortalGunHelper.heightStart(face, h);
      double midW = (double)w0 + (double)(w - 1) * 0.5;
      double midH = (double)h0 + (double)(h - 1) * 0.5;
      double cx = (double)origin.getX() + 0.5 + (double)widthDir.getStepX() * midW + (double)heightDir.getStepX() * midH;
      double cy = (double)origin.getY() + 0.5 + (double)widthDir.getStepY() * midW + (double)heightDir.getStepY() * midH;
      double cz = (double)origin.getZ() + 0.5 + (double)widthDir.getStepZ() * midW + (double)heightDir.getStepZ() * midH;
      double push = 0.05;
      cx += (double)face.getStepX() * push;
      cy += (double)face.getStepY() * push;
      cz += (double)face.getStepZ() * push;
      return new Vec3(cx, cy, cz);
   }

   public static Vec3 exitPosition(Entity entity, PortalSavedData.PortalEntry from, PortalSavedData.PortalEntry to) {
      PortalQuaternionFormula formula = formula(from, to);
      Vec3 fromCenter = portalCenter(from);
      Vec3 toCenter = portalCenter(to);
      double eye = (double)entity.getEyeHeight();
      float[] rel = new float[]{(float)(entity.getX() - fromCenter.x), (float)(entity.getY() + eye - fromCenter.y), (float)(entity.getZ() - fromCenter.z)};
      float[] applied = formula.applyPositionalRotation(rel);
      double x = toCenter.x + (double)applied[0];
      double y = toCenter.y - eye + (double)applied[1];
      double z = toCenter.z + (double)applied[2];
      Direction out = to.face;
      x += (double)out.getStepX() * 0.35;
      y += (double)out.getStepY() * 0.35;
      z += (double)out.getStepZ() * 0.35;
      if (out == Direction.UP) {
         y = Math.max(y, (double)to.info.getPos().getY() + 0.05);
      } else if (out == Direction.DOWN) {
         y = Math.min(y, (double)to.info.getPos().getY() + 1.0 - (double)entity.getBbHeight() - 0.05);
      }

      return new Vec3(x, y, z);
   }

   public static float[] transformLook(float yaw, float pitch, PortalSavedData.PortalEntry from, PortalSavedData.PortalEntry to) {
      PortalQuaternionFormula formula = formula(from, to);
      float[] delta = formula.applyRotationalRotation(new float[]{yaw, pitch, 0.0F});
      float newYaw = Mth.wrapDegrees(yaw + delta[0]);
      float newPitch = Mth.clamp(pitch + delta[1], -90.0F, 90.0F);
      return new float[]{newYaw, newPitch};
   }

   public static Vec3 transformMotion(Vec3 motion, PortalSavedData.PortalEntry from, PortalSavedData.PortalEntry to) {
      PortalQuaternionFormula formula = formula(from, to);
      float[] m = formula.applyPositionalRotation(new float[]{(float)motion.x, (float)motion.y, (float)motion.z});
      Vec3 out = new Vec3((double)m[0], (double)m[1], (double)m[2]);
      return softCapAxis(out);
   }

   private static Vec3 softCapAxis(Vec3 v) {
      double x = v.x;
      double y = v.y;
      double z = v.z;
      if (Math.abs(x) > 0.99) {
         x /= Math.abs(x) + 0.001;
      }

      if (Math.abs(y) > 0.99) {
         y /= Math.abs(y) + 0.001;
      }

      if (Math.abs(z) > 0.99) {
         z /= Math.abs(z) + 0.001;
      }

      return new Vec3(x, y, z);
   }
}
