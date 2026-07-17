package me.scwunge.mods.portalgun;

import com.mojang.logging.LogUtils;
import me.scwunge.mods.portalgun.config.PortalGunConfig;
import me.scwunge.mods.portalgun.init.ModBlockEntities;
import me.scwunge.mods.portalgun.init.ModBlocks;
import me.scwunge.mods.portalgun.init.ModCreativeTabs;
import me.scwunge.mods.portalgun.init.ModDataComponents;
import me.scwunge.mods.portalgun.init.ModEntities;
import me.scwunge.mods.portalgun.init.ModItems;
import me.scwunge.mods.portalgun.init.ModSounds;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import org.slf4j.Logger;

/**
 * Portal feature module for OreSpawn (same mod id / namespace as OreSpawn).
 */
public final class PortalGunMod {
    /** Same as OreSpawn - all registries and assets use this namespace. */
    public static final String MOD_ID = "orespawn";
    public static final String MOD_NAME = "OreSpawn";
    public static final String MOD_AUTHOR = "Scwunge";
    public static final Logger LOGGER = LogUtils.getLogger();

    private PortalGunMod() {}

    public static void init(IEventBus modBus, ModContainer container) {
        container.registerConfig(ModConfig.Type.COMMON, PortalGunConfig.SPEC, "orespawn-portal.toml");
        ModItems.ITEMS.register(modBus);
        ModBlocks.BLOCKS.register(modBus);
        ModBlocks.BLOCK_ITEMS.register(modBus);
        ModBlockEntities.BLOCK_ENTITY_TYPES.register(modBus);
        ModEntities.ENTITY_TYPES.register(modBus);
        ModSounds.SOUNDS.register(modBus);
        ModCreativeTabs.TABS.register(modBus);
        ModDataComponents.COMPONENTS.register(modBus);
        modBus.addListener(PortalGunMod::commonSetup);
        LOGGER.info("OreSpawn portals ready (by {})", MOD_AUTHOR);
    }

    private static void commonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {});
    }
}
