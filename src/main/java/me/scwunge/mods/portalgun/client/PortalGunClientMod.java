package me.scwunge.mods.portalgun.client;

import me.scwunge.mods.portalgun.PortalGunMod;
import net.neoforged.bus.api.IEventBus;

/**
 * Client bootstrap for PortalGun when hosted by OreSpawn.
 */
public final class PortalGunClientMod {
    private static boolean registered;

    private PortalGunClientMod() {}

    /** Call from OreSpawn main on the client dist only. */
    public static void register(IEventBus modBus) {
        if (registered) {
            return;
        }
        registered = true;
        ClientSetup.register(modBus);
        PortalGunMod.LOGGER.info("OreSpawn portal client: 3D gun BEWLR + renderers registered");
    }
}

