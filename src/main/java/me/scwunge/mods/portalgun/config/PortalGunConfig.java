package me.scwunge.mods.portalgun.config;

import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.common.ModConfigSpec.BooleanValue;
import net.neoforged.neoforge.common.ModConfigSpec.Builder;
import net.neoforged.neoforge.common.ModConfigSpec.IntValue;

public final class PortalGunConfig {
   public static final ModConfigSpec SPEC;
   public static final IntValue ENTITY_GRAB_WEIGHT_BASE;
   public static final IntValue MAX_SHOOT_DISTANCE;
   public static final BooleanValue CAN_FIRE_THROUGH_GLASS;
   public static final BooleanValue CAN_FIRE_THROUGH_LIQUID;
   public static final BooleanValue CAN_PORTALS_RESIZE;
   public static final BooleanValue PROJECTILES_CHUNKLOAD;
   public static final BooleanValue EQUIP_SOUND;
   public static final IntValue INDICATOR_SIZE;
   public static final BooleanValue SEE_THROUGH_PORTALS;
   public static final BooleanValue FANCY_PORTALS;
   public static final BooleanValue USE_STENCIL_APERTURE;
   public static final IntValue PORTAL_RECURSION_DEPTH;

   private PortalGunConfig() {
   }

   static {
      Builder b = new Builder();
      b.push("portal");
      ENTITY_GRAB_WEIGHT_BASE = b.comment("Base grab weight % (0 disables non-mod grabs). Default 100.")
         .defineInRange("entityGrabWeightBase", 100, 0, 10000);
      MAX_SHOOT_DISTANCE = b.comment("Max portal projectile distance.").defineInRange("maxShootDistance", 10000, 1, 100000);
      CAN_FIRE_THROUGH_GLASS = b.comment("Portal bolts pass through glass / panes.").define("canFireThroughGlass", true);
      CAN_FIRE_THROUGH_LIQUID = b.comment("Portal bolts ignore water/lava collision and fluid push.").define("canFireThroughLiquid", false);
      CAN_PORTALS_RESIZE = b.define("canPortalsResizeWhenCreated", true);
      PROJECTILES_CHUNKLOAD = b.define("canPortalProjectilesChunkload", true);
      b.pop();
      b.push("client");
      EQUIP_SOUND = b.define("equipItemSound", true);
      INDICATOR_SIZE = b.defineInRange("portalGunIndicatorSize", 30, 0, 100);
      SEE_THROUGH_PORTALS = b.comment(
            "Live linked view inside the portal oval. Costs a full world re-render (heavy with OreSpawn mobs). Default off for FPS; enable if you want the window."
         ).define("seeThroughPortals", false);
      FANCY_PORTALS = b.comment("Extra spinning swirl / glow layers on portals. Disable for more FPS.")
         .define("fancyPortals", false);
      USE_STENCIL_APERTURE = b.comment(
            "GL stencil oval mask for portal see-through (cleaner aperture clip). Requires stencil bits on the main framebuffer (NeoForge enableStencil). If unavailable, falls back to ellipse mesh automatically."
         )
         .define("useStencilAperture", true);
      PORTAL_RECURSION_DEPTH = b.comment(
            "Portal-through-portal FBO depth. 0=primary only (recommended). 1â€“3=nested re-renders â€” heavy with OreSpawn."
         )
         .defineInRange("portalRecursionDepth", 0, 0, 3);
      b.pop();
      SPEC = b.build();
   }
}

