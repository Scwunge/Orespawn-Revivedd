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
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import org.slf4j.Logger;

/**
 * 1:1 port entry — gold OreSpawnMain (1.12 FML lifecycle → NeoForge mod bus).
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

    public OreSpawnMain(IEventBus modEventBus) {
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
        modEventBus.addListener(this::commonSetup);
        // Game bus only — RegisterCommandsEvent is never on the mod bus.
        NeoForge.EVENT_BUS.addListener(CommandDimensionTeleport::onRegisterCommands);
        LOGGER.info("OreSpawn 1.21.1 port loading (primary gold: 1.7.10 classic + CF reuse).");
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        LOGGER.info("OreSpawn common setup.");
    }
}
