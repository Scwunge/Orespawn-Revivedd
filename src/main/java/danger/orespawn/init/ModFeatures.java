package danger.orespawn.init;

import danger.orespawn.util.Reference;
import danger.orespawn.world.gen.AntHillFeature;
import danger.orespawn.world.gen.BasiliskMazeFeature;
import danger.orespawn.world.gen.CephadromeAltarFeature;
import danger.orespawn.world.gen.CornPlantFeature;
import danger.orespawn.world.gen.CrystalDecorFeature;
import danger.orespawn.world.gen.CrystalMazeFeature;
import danger.orespawn.world.gen.DungeonFeature;
import danger.orespawn.world.gen.InsectPlantFeature;
import danger.orespawn.world.gen.IslandSeedFeature;
import danger.orespawn.world.gen.LiquidSurfaceFeature;
import danger.orespawn.world.gen.NightmareDungeonFeature;
import danger.orespawn.world.gen.OverworldDungeonsFeature;
import danger.orespawn.world.gen.RubyUnderLavaFeature;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

/** Custom worldgen features (gold AntHillGenerator / StructureGenerator paths). */
public final class ModFeatures {
    public static final DeferredRegister<Feature<?>> FEATURES =
            DeferredRegister.create(Registries.FEATURE, Reference.MOD_ID);

    /** Gold addAnts(..., 4): Overworld and Village Mania. */
    public static final DeferredHolder<Feature<?>, Feature<NoneFeatureConfiguration>> ANT_HILL =
            FEATURES.register("ant_hill", () -> new AntHillFeature(AntHillFeature.Kind.SURFACE, 4));

    /** Gold addAnts(..., 2): Mining and Chaos dimensions (more special nests). */
    public static final DeferredHolder<Feature<?>, Feature<NoneFeatureConfiguration>> ANT_HILL_RICH =
            FEATURES.register("ant_hill_rich", () -> new AntHillFeature(AntHillFeature.Kind.SURFACE, 2));

    /** Gold addNetherAnts: Red Ant Nests on netherrack. */
    public static final DeferredHolder<Feature<?>, Feature<NoneFeatureConfiguration>> NETHER_ANT_HILL =
            FEATURES.register("nether_ant_hill", () -> new AntHillFeature(AntHillFeature.Kind.NETHER, 1));

    /** Gold addCrystalTermites: Crystal Termite Nests on crystal grass. */
    public static final DeferredHolder<Feature<?>, Feature<NoneFeatureConfiguration>> CRYSTAL_TERMITE_HILL =
            FEATURES.register("crystal_termite_hill", () -> new AntHillFeature(AntHillFeature.Kind.CRYSTAL, 1));

    public static final DeferredHolder<Feature<?>, Feature<NoneFeatureConfiguration>> GENERIC_DUNGEON =
            FEATURES.register("generic_dungeon", DungeonFeature::new);

    /** Gold NightmareDungeon layout (25×12); also dungeon spawner type 38 interim. */
    public static final DeferredHolder<Feature<?>, Feature<NoneFeatureConfiguration>> NIGHTMARE_DUNGEON =
            FEATURES.register("nightmare_dungeon", NightmareDungeonFeature::new);

    /** Gold BasiliskMaze — dungeon spawner type 23. */
    public static final DeferredHolder<Feature<?>, Feature<NoneFeatureConfiguration>> BASILISK_MAZE =
            FEATURES.register("basilisk_maze", BasiliskMazeFeature::new);

    /** Gold CephadromeAltar — dungeon spawner type 34, overworld surface. */
    public static final DeferredHolder<Feature<?>, Feature<NoneFeatureConfiguration>> CEPHADROME_ALTAR =
            FEATURES.register("cephadrome_altar", CephadromeAltarFeature::new);

    /** Gold generateRuby — one ruby ore in the stone directly under lava. */
    public static final DeferredHolder<Feature<?>, Feature<NoneFeatureConfiguration>> RUBY_UNDER_LAVA =
            FEATURES.register("ruby_under_lava", RubyUnderLavaFeature::new);

    /** Gold generateSurface small Overworld structures (pools, islands, hives, igloo, ...). */
    public static final DeferredHolder<Feature<?>, Feature<NoneFeatureConfiguration>> OVERWORLD_DUNGEONS =
            FEATURES.register("overworld_dungeons", OverworldDungeonsFeature::new);

    /** Gold CrystalMaze — crystal dim chunk maze at y=25 (no dungeon spawner type). */
    public static final DeferredHolder<Feature<?>, Feature<NoneFeatureConfiguration>> CRYSTAL_MAZE =
            FEATURES.register("crystal_maze", CrystalMazeFeature::new);

    /** SET17: crystal flowers + saplings (count-limited, no full-chunk paint). */
    public static final DeferredHolder<Feature<?>, Feature<NoneFeatureConfiguration>> CRYSTAL_DECOR =
            FEATURES.register("crystal_decor", CrystalDecorFeature::new);

    /** Gold PlantGenerator — butterfly/mosquito/firefly patches. */
    public static final DeferredHolder<Feature<?>, Feature<NoneFeatureConfiguration>> INSECT_PLANT =
            FEATURES.register("insect_plant", InsectPlantFeature::new);

    /** Gold CornPlantGenerator. */
    public static final DeferredHolder<Feature<?>, Feature<NoneFeatureConfiguration>> CORN_PLANT =
            FEATURES.register("corn_plant", CornPlantFeature::new);

    /** Gold LiquidGenerator — surface water/lava in mining biome. */
    public static final DeferredHolder<Feature<?>, Feature<NoneFeatureConfiguration>> LIQUID_SURFACE =
            FEATURES.register("liquid_surface", LiquidSurfaceFeature::new);

    /** Gold OreSpawnWorld.addIslands — IslandBlock seeds in Islands dim. */
    public static final DeferredHolder<Feature<?>, Feature<NoneFeatureConfiguration>> ISLAND_SEED =
            FEATURES.register("island_seed", IslandSeedFeature::new);

    private ModFeatures() {}
}
