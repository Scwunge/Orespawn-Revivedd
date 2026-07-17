package me.scwunge.mods.portalgun.client;

public final class ClientPortalStatus {
   private static boolean hasBlue;
   private static boolean hasOrange;

   private ClientPortalStatus() {
   }

   public static void set(boolean blue, boolean orange) {
      hasBlue = blue;
      hasOrange = orange;
   }

   public static void clear() {
      hasBlue = false;
      hasOrange = false;
   }

   public static boolean hasBlue() {
      return hasBlue;
   }

   public static boolean hasOrange() {
      return hasOrange;
   }
}
