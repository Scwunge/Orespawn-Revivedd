package danger.orespawn.world;

import danger.orespawn.entity.Girlfriend;
import danger.orespawn.init.ModEntities;
import danger.orespawn.util.Reference;
import java.time.LocalDate;
import java.time.Month;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.level.LevelEvent;

/**
 * Gold {@code OreSpawnMain} date checks (read once at startup, like 1.7.10):
 * <ul>
 *   <li>October 31 — Ghosts and Ghost Skellies spawn across most Overworld biomes
 *       (weight 15, groups of 3–6) on top of their year-round spawns.</li>
 *   <li>April 20 — the Easter Bunny spawns in plains, forests and taigas.</li>
 *   <li>February 14 — Girlfriend Valentine's Day mode.</li>
 * </ul>
 */
@EventBusSubscriber(modid = Reference.MOD_ID)
public final class SeasonalSpawnEvents {
    private static final TagKey<Biome> HALLOWEEN_BIOMES = TagKey.create(
            Registries.BIOME, ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "halloween_ghost_biomes"));
    private static final TagKey<Biome> EASTER_BIOMES = TagKey.create(
            Registries.BIOME, ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "easter_bunny_biomes"));

    private static boolean halloween;
    private static boolean easter;

    /*
     * Cached so NaturalSpawner's list.contains(...) check (reference equality) sees the same
     * instances on both PotentialSpawns calls of a spawn attempt.
     */
    private static MobSpawnSettings.SpawnerData ghost;
    private static MobSpawnSettings.SpawnerData ghostSkelly;
    private static MobSpawnSettings.SpawnerData easterBunny10;
    private static MobSpawnSettings.SpawnerData easterBunny8;
    private static MobSpawnSettings.SpawnerData easterBunny5;

    private SeasonalSpawnEvents() {}

    /** Called from common setup (both sides, so the client renderer sees Valentine's Day too). */
    public static void readDate() {
        LocalDate today = LocalDate.now();
        halloween = today.getMonth() == Month.OCTOBER && today.getDayOfMonth() == 31;
        easter = today.getMonth() == Month.APRIL && today.getDayOfMonth() == 20;
        Girlfriend.VALENTINES_DAY = today.getMonth() == Month.FEBRUARY && today.getDayOfMonth() == 14 ? 1 : 0;
    }

    @SubscribeEvent
    public static void onPotentialSpawns(LevelEvent.PotentialSpawns event) {
        if (!halloween && !easter) {
            return;
        }
        if (!(event.getLevel() instanceof ServerLevel level) || level.dimension() != Level.OVERWORLD) {
            return;
        }
        Holder<Biome> biome = level.getBiome(event.getPos());
        if (halloween && event.getMobCategory() == MobCategory.AMBIENT && biome.is(HALLOWEEN_BIOMES)) {
            if (ghost == null) {
                ghost = new MobSpawnSettings.SpawnerData(ModEntities.GHOST.get(), 15, 3, 6);
                ghostSkelly = new MobSpawnSettings.SpawnerData(ModEntities.GHOST_SKELLY.get(), 15, 3, 6);
            }
            event.addSpawnerData(ghost);
            event.addSpawnerData(ghostSkelly);
        }
        if (easter && event.getMobCategory() == MobCategory.CREATURE && biome.is(EASTER_BIOMES)) {
            if (easterBunny10 == null) {
                easterBunny10 = new MobSpawnSettings.SpawnerData(ModEntities.EASTER_BUNNY.get(), 10, 1, 2);
                easterBunny8 = new MobSpawnSettings.SpawnerData(ModEntities.EASTER_BUNNY.get(), 8, 1, 2);
                easterBunny5 = new MobSpawnSettings.SpawnerData(ModEntities.EASTER_BUNNY.get(), 5, 1, 2);
            }
            event.addSpawnerData(easterWeight(biome));
        }
    }

    /** Gold weights: 10 plains/forest, 8 taiga, 5 birch forests and old-growth taigas. */
    private static MobSpawnSettings.SpawnerData easterWeight(Holder<Biome> biome) {
        if (biome.is(Biomes.PLAINS) || biome.is(Biomes.SUNFLOWER_PLAINS)
                || biome.is(Biomes.FOREST) || biome.is(Biomes.FLOWER_FOREST)) {
            return easterBunny10;
        }
        if (biome.is(Biomes.TAIGA)) {
            return easterBunny8;
        }
        return easterBunny5;
    }
}
