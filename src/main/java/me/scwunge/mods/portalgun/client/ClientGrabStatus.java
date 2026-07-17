package me.scwunge.mods.portalgun.client;

public final class ClientGrabStatus {
   private static boolean holding;
   private static int heldEntityId = -1;
   private static float clawOpen;
   private static float fireKick;

   private ClientGrabStatus() {
   }

   public static void setHolding(boolean value) {
      setHolding(value, -1);
   }

   public static void setHolding(boolean value, int entityId) {
      holding = value;
      heldEntityId = value ? entityId : -1;
   }

   public static boolean isHolding() {
      return holding;
   }

   public static int heldEntityId() {
      return heldEntityId;
   }

   public static void pulseFire() {
      fireKick = 1.0F;
   }

   public static float getFireKick() {
      return fireKick;
   }

   public static void tick() {
      float target = holding ? 1.0F : 0.0F;
      clawOpen = clawOpen + (target - clawOpen) * 0.35F;
      if (Math.abs(clawOpen - target) < 0.01F) {
         clawOpen = target;
      }

      if (fireKick > 0.0F) {
         fireKick = Math.max(0.0F, fireKick - 0.12F);
      }
   }

   public static float clawOpen() {
      return clawOpen;
   }

   public static void clear() {
      holding = false;
      heldEntityId = -1;
      clawOpen = 0.0F;
      fireKick = 0.0F;
   }
}
