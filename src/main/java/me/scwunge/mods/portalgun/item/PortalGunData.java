package me.scwunge.mods.portalgun.item;

import me.scwunge.mods.portalgun.init.ModDataComponents;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public final class PortalGunData {
   public static final String DEFAULT_CHANNEL = "Chell";
   public static final String GLOBAL_UUID = "Global";
   public static final String GLOBAL_NAME = "Global";
   public static final String[] CHANNELS = new String[]{"Chell", "Atlas", "P-body", "Mel", "Custom"};
   public static final int DEFAULT_WIDTH = 1;
   public static final int DEFAULT_HEIGHT = 2;
   public static final int MIN_SIZE = 1;
   public static final int MAX_SIZE = 5;
   public static final int DEFAULT_GRAB_STRENGTH = 4;
   public static final int MIN_GRAB_STRENGTH = 1;
   public static final int MAX_GRAB_STRENGTH = 5;

   private PortalGunData() {
   }

   public static String channel(ItemStack stack) {
      String c = (String)stack.get(ModDataComponents.CHANNEL);
      return c != null && !c.isBlank() ? c : "Chell";
   }

   public static int width(ItemStack stack) {
      Integer w = (Integer)stack.get(ModDataComponents.PORTAL_WIDTH);
      return clamp(w == null ? 1 : w);
   }

   public static int height(ItemStack stack) {
      Integer h = (Integer)stack.get(ModDataComponents.PORTAL_HEIGHT);
      return clamp(h == null ? 2 : h);
   }

   public static int grabStrength(ItemStack stack) {
      Integer s = (Integer)stack.get(ModDataComponents.GRAB_STRENGTH);
      return clampGrabStrength(s == null ? 4 : s);
   }

   public static void setGrabStrength(ItemStack stack, int strength) {
      stack.set(ModDataComponents.GRAB_STRENGTH, clampGrabStrength(strength));
   }

   public static int cycleGrabStrength(ItemStack stack, int delta) {
      int next = clampGrabStrength(grabStrength(stack) + delta);
      setGrabStrength(stack, next);
      return next;
   }

   public static boolean lastOrange(ItemStack stack) {
      Boolean v = (Boolean)stack.get(ModDataComponents.LAST_ORANGE);
      return v != null && v;
   }

   public static void setLastOrange(ItemStack stack, boolean orange) {
      stack.set(ModDataComponents.LAST_ORANGE, orange);
   }

   public static String ownerUuid(ItemStack stack) {
      String u = (String)stack.get(ModDataComponents.OWNER_UUID);
      return u == null ? "" : u;
   }

   public static void setOwnerUuid(ItemStack stack, String uuid) {
      if (uuid != null && !uuid.isBlank()) {
         if (uuid.length() > 64) {
            uuid = uuid.substring(0, 64);
         }

         if (!uuid.equals(ownerUuid(stack))) {
            stack.set(ModDataComponents.OWNER_UUID, uuid);
         }
      }
   }

   public static String ownerName(ItemStack stack) {
      String n = (String)stack.get(ModDataComponents.OWNER_NAME);
      return n == null ? "" : n;
   }

   public static void setOwnerName(ItemStack stack, String name) {
      if (name == null) {
         name = "";
      }

      if (name.length() > 32) {
         name = name.substring(0, 32);
      }

      if (!name.equals(ownerName(stack))) {
         stack.set(ModDataComponents.OWNER_NAME, name);
      }
   }

   public static boolean isGlobal(ItemStack stack) {
      return "Global".equalsIgnoreCase(ownerUuid(stack)) || "Global".equalsIgnoreCase(ownerName(stack));
   }

   public static String portalOwnerKey(ItemStack stack, Player player) {
      if (isGlobal(stack)) {
         return "Global";
      } else {
         String u = ownerUuid(stack);
         if (!u.isEmpty() && !u.startsWith("mob-")) {
            return u;
         } else {
            return player != null ? player.getUUID().toString() : u;
         }
      }
   }

   public static void applyCraftedTags(ItemStack stack, Player player) {
      ensureDefaults(stack);
      if (player != null) {
         setOwnerUuid(stack, player.getUUID().toString());
         setOwnerName(stack, player.getGameProfile().getName());
      }

      setChannel(stack, "Random Channel #" + (Math.floorMod(stack.hashCode(), 9000) + 1000));
      setGrabStrength(stack, 4);
      setSize(stack, 1, 2);
      setLastOrange(stack, true);
   }

   public static void stampPersonalOwner(ItemStack stack, Player player) {
      if (player != null && !isGlobal(stack)) {
         setOwnerUuid(stack, player.getUUID().toString());
         setOwnerName(stack, player.getGameProfile().getName());
      }
   }

   public static void makeGlobal(ItemStack stack, String channel) {
      ensureDefaults(stack);
      setOwnerUuid(stack, "Global");
      setOwnerName(stack, "Global");
      setChannel(stack, channel);
      setGrabStrength(stack, 4);
      setSize(stack, 1, 2);
   }

   public static void makePersonalCreative(ItemStack stack, Player player, String channel) {
      ensureDefaults(stack);
      if (player != null) {
         setOwnerUuid(stack, player.getUUID().toString());
         setOwnerName(stack, player.getGameProfile().getName());
      }

      setChannel(stack, channel);
      setGrabStrength(stack, 4);
      setSize(stack, 1, 2);
   }

   public static String sanitizeChannel(String raw) {
      if (raw == null) {
         return "Chell";
      } else {
         StringBuilder sb = new StringBuilder(raw.length());

         for (int i = 0; i < raw.length(); i++) {
            char c = raw.charAt(i);
            if (c >= ' ' && c != 127) {
               sb.append(c);
            }
         }

         String s = sb.toString().trim();
         if (s.isEmpty()) {
            return "Chell";
         } else {
            if (s.length() > 24) {
               s = s.substring(0, 24);
            }

            return s;
         }
      }
   }

   public static void ensureDefaults(ItemStack stack) {
      if (!stack.has(ModDataComponents.CHANNEL)) {
         stack.set(ModDataComponents.CHANNEL, "Chell");
      }

      if (!stack.has(ModDataComponents.PORTAL_WIDTH)) {
         stack.set(ModDataComponents.PORTAL_WIDTH, 1);
      }

      if (!stack.has(ModDataComponents.PORTAL_HEIGHT)) {
         stack.set(ModDataComponents.PORTAL_HEIGHT, 2);
      }

      if (!stack.has(ModDataComponents.GRAB_STRENGTH)) {
         stack.set(ModDataComponents.GRAB_STRENGTH, 4);
      }

      if (!stack.has(ModDataComponents.LAST_ORANGE)) {
         stack.set(ModDataComponents.LAST_ORANGE, false);
      }
   }

   public static void setChannel(ItemStack stack, String channel) {
      stack.set(ModDataComponents.CHANNEL, sanitizeChannel(channel));
   }

   public static void setSize(ItemStack stack, int width, int height) {
      stack.set(ModDataComponents.PORTAL_WIDTH, clamp(width));
      stack.set(ModDataComponents.PORTAL_HEIGHT, clamp(height));
   }

   public static String cycleChannel(ItemStack stack) {
      String current = channel(stack);
      int idx = 0;

      for (int i = 0; i < CHANNELS.length; i++) {
         if (CHANNELS[i].equalsIgnoreCase(current)) {
            idx = i;
            break;
         }
      }

      String next = CHANNELS[(idx + 1) % CHANNELS.length];
      setChannel(stack, next);
      return next;
   }

   public static void cycleSize(ItemStack stack, int delta) {
      cycleHeight(stack, delta);
   }

   public static int cycleHeight(ItemStack stack, int delta) {
      int next = clamp(height(stack) + delta);
      stack.set(ModDataComponents.PORTAL_HEIGHT, next);
      return next;
   }

   public static int cycleWidth(ItemStack stack, int delta) {
      int next = clamp(width(stack) + delta);
      stack.set(ModDataComponents.PORTAL_WIDTH, next);
      return next;
   }

   private static int clamp(int v) {
      return Math.max(1, Math.min(5, v));
   }

   private static int clampGrabStrength(int v) {
      return Math.max(1, Math.min(5, v));
   }
}
