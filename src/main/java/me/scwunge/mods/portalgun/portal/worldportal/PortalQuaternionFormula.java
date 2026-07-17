package me.scwunge.mods.portalgun.portal.worldportal;

import java.util.HashMap;
import net.minecraft.core.Direction;

public final class PortalQuaternionFormula {
   public static final PortalQuaternion NO_ROTATION_QUAT = new PortalQuaternion(0.0F, 0.0F, 0.0F, 1.0F);
   public static final PortalQuaternionFormula NO_ROTATION = new PortalQuaternionFormula(NO_ROTATION_QUAT, NO_ROTATION_QUAT);
   public static final float[] NO_ROTATION_FLOAT = new float[]{0.0F, 0.0F, 0.0F};
   private static final HashMap<Float, PortalQuaternion> HORIZONTAL_QUATS = new HashMap<>();
   private static final HashMap<Float, PortalQuaternion> VERTICAL_QUATS = new HashMap<>();
   public final PortalQuaternion qHI;
   public final PortalQuaternion conjQHI;
   public final PortalQuaternion qHO;
   public final PortalQuaternion conjQHO;
   public final PortalQuaternion qVI;
   public final PortalQuaternion conjQVI;
   public final PortalQuaternion qVO;
   public final PortalQuaternion conjQVO;
   public final boolean noRotation;

   public PortalQuaternionFormula(PortalQuaternion inH, PortalQuaternion outH) {
      this.qHI = inH;
      this.conjQHI = this.qHI.conjugate();
      this.qHO = outH;
      this.conjQHO = this.qHO.conjugate();
      this.qVI = NO_ROTATION_QUAT;
      this.qVO = NO_ROTATION_QUAT;
      this.conjQVI = NO_ROTATION_QUAT.conjugate();
      this.conjQVO = NO_ROTATION_QUAT.conjugate();
      this.noRotation = quaternionsIdentical(this.qHI, this.qHO);
   }

   public PortalQuaternionFormula(PortalQuaternion inH, PortalQuaternion outH, PortalQuaternion inV, PortalQuaternion outV) {
      this.qHI = inH;
      this.conjQHI = this.qHI.conjugate();
      this.qHO = outH;
      this.conjQHO = this.qHO.conjugate();
      this.qVI = inV;
      this.conjQVI = this.qVI.conjugate();
      this.qVO = outV;
      this.conjQVO = this.qVO.conjugate();
      this.noRotation = quaternionsIdentical(this.qVI, this.qVO) && quaternionsIdentical(this.qHI, this.qHO);
   }

   public static float horizontalAngle(Direction in) {
      return in.getAxis().isVertical() ? 0.0F : in.toYRot() - 0.0125F;
   }

   public static PortalQuaternion getHorizontalQuaternion(Direction in) {
      float horiAngle = horizontalAngle(in);
      PortalQuaternion quat = HORIZONTAL_QUATS.get(horiAngle);
      if (quat == null) {
         quat = createQuaternionFromZXYEuler(0.0F, horiAngle, 0.0F);
         HORIZONTAL_QUATS.put(horiAngle, quat);
      }

      return quat;
   }

   public static PortalQuaternion getVerticalQuaternion(Direction in) {
      float vertAngle = (float)in.getStepY() * 90.0F * 0.99F;
      PortalQuaternion quat = VERTICAL_QUATS.get(vertAngle);
      if (quat == null) {
         quat = createQuaternionFromZXYEuler(vertAngle, 0.0F, 0.0F);
         VERTICAL_QUATS.put(vertAngle, quat);
      }

      return quat;
   }

   public static PortalQuaternionFormula createFromFaces(Direction in, Direction inUp, Direction out, Direction outUp) {
      return !in.getAxis().isVertical() && !out.getAxis().isVertical()
         ? new PortalQuaternionFormula(getHorizontalQuaternion(in), getHorizontalQuaternion(out))
         : new PortalQuaternionFormula(
            getHorizontalQuaternion(in.getAxis().isVertical() ? inUp : in),
            getHorizontalQuaternion(out.getAxis().isVertical() ? outUp : out),
            getVerticalQuaternion(in),
            getVerticalQuaternion(out)
         );
   }

   public static PortalQuaternionFormula createFromPlanes(Direction in, Direction inUp, Direction out, Direction outUp) {
      return createFromFaces(in.getOpposite(), in == Direction.UP ? inUp : inUp.getOpposite(), out, out == Direction.DOWN ? outUp : outUp.getOpposite());
   }

   public float[] applyPositionalRotation(float[] ori) {
      if (this.noRotation) {
         return ori;
      } else {
         float[] applied = new float[3];
         PortalQuaternion qPos = new PortalQuaternion(ori[0], ori[1], ori[2], 0.0F);
         qPos = this.qHI.mul(qPos).mul(this.conjQHI);
         qPos = this.qVI.mul(qPos).mul(this.conjQVI);
         qPos = this.conjQVO.mul(qPos).mul(this.qVO);
         qPos = this.conjQHO.mul(qPos).mul(this.qHO);
         applied[0] = qPos.x;
         applied[1] = qPos.y;
         applied[2] = qPos.z;
         return applied;
      }
   }

   public float[] applyRotationalRotation(float[] ori) {
      if (this.noRotation) {
         return (float[])NO_ROTATION_FLOAT.clone();
      } else {
         PortalQuaternion qRot = createQuaternionFromZXYEuler(ori[1], ori[0], ori[2]);
         qRot = qRot.mul(this.conjQHI);
         if (!quaternionsIdentical(this.qVI, NO_ROTATION_QUAT)) {
            qRot = qRot.mul(this.qVI);
         }

         if (!quaternionsIdentical(this.qVO, NO_ROTATION_QUAT)) {
            qRot = qRot.mul(this.conjQVO);
         }

         qRot = qRot.mul(this.qHO);
         float[] angles = createZXYEulerFromQuaternion(qRot);
         return new float[]{angles[1] - ori[0], angles[0] - ori[1], angles[2] - ori[2]};
      }
   }

   public static PortalQuaternion createQuaternionFromZXYEuler(float x, float y, float z) {
      double radX = Math.toRadians((double)x);
      double radY = Math.toRadians((double)y);
      double radZ = Math.toRadians((double)z);
      float cx = (float)Math.cos(radX);
      float sx = (float)Math.sin(radX);
      float cy = (float)Math.cos(radY);
      float sy = (float)Math.sin(radY);
      float cz = (float)Math.cos(radZ);
      float sz = (float)Math.sin(radZ);
      float[] m = new float[]{
         cy * cz - sx * sy * sz, -cx * sz, cz * sy + cy * sx * sz, cz * sx * sy + cy * sz, cx * cz, -cy * cz * sx + sy * sz, -cx * sy, sx, cx * cy
      };
      return new PortalQuaternion().setFromMatrix(m);
   }

   public static float[] createZXYEulerFromQuaternion(PortalQuaternion q) {
      float xx = q.x * q.x;
      float xy = q.x * q.y;
      float xz = q.x * q.z;
      float xw = q.x * q.w;
      float yy = q.y * q.y;
      float yz = q.y * q.z;
      float yw = q.y * q.w;
      float zz = q.z * q.z;
      float zw = q.z * q.w;
      float m00 = 1.0F - 2.0F * (yy + zz);
      float m01 = 2.0F * (xy - zw);
      float m02 = 2.0F * (xz + yw);
      float m10 = 2.0F * (xy + zw);
      float m11 = 1.0F - 2.0F * (xx + zz);
      float m12 = 2.0F * (yz - xw);
      float m20 = 2.0F * (xz - yw);
      float m21 = 2.0F * (yz + xw);
      float m22 = 1.0F - 2.0F * (xx + yy);
      double thetaY;
      double thetaZ;
      double thetaX;
      if (m21 < 1.0F) {
         if (m21 > -1.0F) {
            thetaX = Math.asin((double)m21);
            thetaZ = Math.atan2((double)(-m01), (double)m11);
            thetaY = Math.atan2((double)(-m20), (double)m22);
         } else {
            thetaX = -Math.PI / 2;
            thetaZ = -Math.atan2((double)m02, (double)m00);
            thetaY = 0.0;
         }
      } else {
         thetaX = Math.PI / 2;
         thetaZ = Math.atan2((double)m02, (double)m00);
         thetaY = 0.0;
      }

      return new float[]{(float)Math.toDegrees(thetaX), (float)Math.toDegrees(thetaY), (float)Math.toDegrees(thetaZ)};
   }

   public static boolean quaternionsIdentical(PortalQuaternion a, PortalQuaternion b) {
      return a.x == b.x && a.y == b.y && a.z == b.z && a.w == b.w;
   }
}
