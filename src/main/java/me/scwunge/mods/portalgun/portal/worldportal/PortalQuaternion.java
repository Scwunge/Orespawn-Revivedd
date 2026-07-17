package me.scwunge.mods.portalgun.portal.worldportal;

public final class PortalQuaternion {
   public float x;
   public float y;
   public float z;
   public float w;

   public PortalQuaternion() {
   }

   public PortalQuaternion(float x, float y, float z, float w) {
      this.x = x;
      this.y = y;
      this.z = z;
      this.w = w;
   }

   public PortalQuaternion set(float x, float y, float z, float w) {
      this.x = x;
      this.y = y;
      this.z = z;
      this.w = w;
      return this;
   }

   public PortalQuaternion conjugate() {
      PortalQuaternion q = new PortalQuaternion();
      q.x = -this.x;
      q.y = -this.y;
      q.z = -this.z;
      q.w = this.w;
      return q;
   }

   public PortalQuaternion mul(PortalQuaternion right) {
      PortalQuaternion q = new PortalQuaternion();
      q.set(
         this.x * right.w + this.w * right.x + this.y * right.z - this.z * right.y,
         this.y * right.w + this.w * right.y + this.z * right.x - this.x * right.z,
         this.z * right.w + this.w * right.z + this.x * right.y - this.y * right.x,
         this.w * right.w - this.x * right.x - this.y * right.y - this.z * right.z
      );
      return q;
   }

   public PortalQuaternion setFromMatrix(float[] m) {
      float m00 = m[0];
      float m01 = m[1];
      float m02 = m[2];
      float m10 = m[3];
      float m11 = m[4];
      float m12 = m[5];
      float m20 = m[6];
      float m21 = m[7];
      float m22 = m[8];
      float tr = m00 + m11 + m22;
      if ((double)tr >= 0.0) {
         float mag = (float)Math.sqrt((double)tr + 1.0);
         this.w = mag * 0.5F;
         mag = 0.5F / mag;
         this.x = (m21 - m12) * mag;
         this.y = (m02 - m20) * mag;
         this.z = (m10 - m01) * mag;
      } else {
         float max = Math.max(Math.max(m00, m11), m22);
         if (max == m00) {
            float mag = (float)Math.sqrt((double)(m00 - (m11 + m22)) + 1.0);
            this.x = mag * 0.5F;
            mag = 0.5F / mag;
            this.y = (m01 + m10) * mag;
            this.z = (m20 + m02) * mag;
            this.w = (m21 - m12) * mag;
         } else if (max == m11) {
            float mag = (float)Math.sqrt((double)(m11 - (m22 + m00)) + 1.0);
            this.y = mag * 0.5F;
            mag = 0.5F / mag;
            this.z = (m12 + m21) * mag;
            this.x = (m01 + m10) * mag;
            this.w = (m02 - m20) * mag;
         } else {
            float mag = (float)Math.sqrt((double)(m22 - (m00 + m11)) + 1.0);
            this.z = mag * 0.5F;
            mag = 0.5F / mag;
            this.x = (m20 + m02) * mag;
            this.y = (m12 + m21) * mag;
            this.w = (m10 - m01) * mag;
         }
      }

      return this;
   }
}
