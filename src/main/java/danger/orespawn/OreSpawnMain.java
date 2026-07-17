package danger.orespawn;

import com.mojang.logging.LogUtils;
import danger.orespawn.commands.CommandDimensionTeleport;
import danger.orespawn.init.ModBlockEntities;
import danger.orespawn.init.ModBlocks;
import danger.orespawn.init.ModCreativeTabs;
import danger.orespawn.init.ModEntities;
import danger.orespawn.init.ModFeatures;
import danger.orespawn.init.ModItems;
import danger.orespawn.items.armor.OrespawnArmorMaterial;
import danger.orespawn.util.Reference;
import danger.orespawn.util.handlers.SoundsHandler;
import me.scwunge.mods.portalgun.PortalGunMod;
import me.scwunge.mods.portalgun.client.PortalGunClientMod;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.common.NeoForge;
import org.slf4j.Logger;

/**
 * OreSpawn main entry (includes PortalGun feature module).
 */
@Mod(Reference.MOD_ID)
public class OreSpawnMain {
    public static final Logger LOGGER = LogUtils.getLogger();
    public static OreSpawnMain instance;

    /**
     * Gold {@code OreSpawnMain.PlayNicely}.
     * <ul>
     *   <li>{@code 0} (default) — aggressive: mobs acquire targets / hunt as in vanilla OreSpawn</li>
     *   <li>{@code != 0} — pacified: many hostile mobs skip target scan / player aggro</li>
     * </ul>
     * Set at runtime or from a future config; not currently loaded from NeoForge config.
     */
    public static int PlayNicely = 0;

    /**
     * Gold {@code OreSpawnMain.RatPlayerFriendly}.
     * When non-zero and rat has owner, owned rats skip players as targets.
     */
    public static int RatPlayerFriendly = 0;

    /**
     * Gold {@code OreSpawnMain.RatPetFriendly}.
     * When non-zero and rat has owner, owned rats skip tamed pets as targets.
     */
    public static int RatPetFriendly = 0;

    /**
     * 1.7.10 {@code OreSpawnMain.big_bertha_pvp}.
     * {@code 0} = Bertha cannot hurt players / owned pets (gold default).
     */
    public static int big_bertha_pvp = 0;

    public OreSpawnMain(IEventBus modEventBus, ModContainer container) {
        instance = this;
        ModBlocks.BLOCKS.register(modEventBus);
        ModBlocks.BLOCK_ITEMS.register(modEventBus);
        ModBlockEntities.BLOCK_ENTITIES.register(modEventBus);
        OrespawnArmorMaterial.ARMOR_MATERIALS.register(modEventBus);
        // Entities before items so spawn-egg suppliers resolve cleanly.
        ModEntities.ENTITY_TYPES.register(modEventBus);
        ModItems.ITEMS.register(modEventBus);
        ModCreativeTabs.CREATIVE_MODE_TABS.register(modEventBus);
        SoundsHandler.SOUND_EVENTS.register(modEventBus);
        ModFeatures.FEATURES.register(modEventBus);

        // PortalGun (registry namespace portalgun; events under orespawn)
        PortalGunMod.init(modEventBus, container);
        if (FMLEnvironment.dist == Dist.CLIENT) {
            PortalGunClientMod.register(modEventBus);
        }

        modEventBus.addListener(this::commonSetup);
        // Game bus only — RegisterCommandsEvent is never on the mod bus.
        NeoForge.EVENT_BUS.addListener(CommandDimensionTeleport::onRegisterCommands);
        LOGGER.info("OreSpawn loading.");
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        LOGGER.info("OreSpawn common setup.");
    }
}
