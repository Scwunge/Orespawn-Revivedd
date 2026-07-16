package danger.orespawn.world;

import danger.orespawn.init.ModDimensions;
import danger.orespawn.init.ModEntities;
import danger.orespawn.util.Reference;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.level.LevelEvent;

/**
 * Gold {@code BiomeGenUtopianPlains.setIslandCreatures()} for Islands dim.
 * <p>
 * Replaces natural spawn lists in {@code orespawn:islands} with gold island tables
 * (belt-and-suspenders with the fixed {@code orespawn:islands} biome).
 * <p>
 * <b>Critical:</b> {@link net.minecraft.world.level.NaturalSpawner} fires
 * {@code PotentialSpawns} twice per attempt and checks
 * {@code list.contains(spawnerData)} with <em>reference</em> equality
 * ({@link MobSpawnSettings.SpawnerData} has no {@code equals}). Entries must be
 * the same cached instances on every event fire or gold mobs never place.
 */
@EventBusSubscriber(modid = Reference.MOD_ID)
public final class IslandGoldSpawnEvents {
    /** Lazy-built so DeferredHolders are resolved after entity registration. */
    private static Map<MobCategory, List<MobSpawnSettings.SpawnerData>> goldTables;

    private IslandGoldSpawnEvents() {}

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onPotentialSpawns(LevelEvent.PotentialSpawns event) {
        if (!(event.getLevel() instanceof ServerLevel level)) {
            return;
        }
        if (!level.dimension().equals(ModDimensions.ISLANDS)) {
            return;
        }

        List<MobSpawnSettings.SpawnerData> gold =
                goldTables().getOrDefault(event.getMobCategory(), List.of());
        replaceAll(event, gold);
    }

    private static Map<MobCategory, List<MobSpawnSettings.SpawnerData>> goldTables() {
        if (goldTables == null) {
            goldTables = buildGoldTables();
        }
        return goldTables;
    }

    private static Map<MobCategory, List<MobSpawnSettings.SpawnerData>> buildGoldTables() {
        Map<MobCategory, List<MobSpawnSettings.SpawnerData>> map =
                new EnumMap<>(MobCategory.class);

        // Categories match entity registration (avoids NeoForge mob-cap warnings
        // and ensures NaturalSpawner category cycles can pick them).
        map.put(MobCategory.MONSTER, List.of(
                entry(ModEntities.CREEPING_HORROR.get(), 60, 4, 8),
                entry(ModEntities.TERRIBLE_TERROR.get(), 25, 3, 6),
                entry(ModEntities.LURKING_TERROR.get(), 1, 1, 1),
                entry(ModEntities.PITCH_BLACK.get(), 15, 3, 6),
                entry(ModEntities.LEAF_MONSTER.get(), 35, 2, 4),
                entry(ModEntities.ENDER_REAPER.get(), 25, 2, 4),
                entry(ModEntities.HERCULES_BEETLE.get(), 5, 1, 2),
                entry(ModEntities.CLOUD_SHARK.get(), 1, 1, 1)));

        map.put(MobCategory.CREATURE, List.of(
                entry(ModEntities.COCKATEIL.get(), 4, 1, 2),
                entry(ModEntities.DRAGON.get(), 1, 1, 2),
                entry(ModEntities.STINKY.get(), 2, 1, 2),
                entry(ModEntities.CLIFF_RACER.get(), 20, 3, 6),
                entry(ModEntities.GOLD_FISH.get(), 5, 2, 4)));

        map.put(MobCategory.AMBIENT, List.of(
                entry(ModEntities.BUTTERFLY.get(), 5, 2, 6),
                entry(ModEntities.MOTH.get(), 5, 2, 4),
                entry(ModEntities.FIREFLY.get(), 10, 4, 8)));

        for (MobCategory cat : MobCategory.values()) {
            map.putIfAbsent(cat, List.of());
        }
        return map;
    }

    /**
     * Wipe biome/multi-noise entries and install cached gold list.
     * Always mutates when there is anything to clear or add so EventHooks
     * rebuilds a WeightedRandomList (it returns oldList if view is unchanged).
     */
    private static void replaceAll(
            LevelEvent.PotentialSpawns event,
            List<MobSpawnSettings.SpawnerData> gold) {
        List<MobSpawnSettings.SpawnerData> existing =
                new ArrayList<>(event.getSpawnerDataList());
        for (MobSpawnSettings.SpawnerData data : existing) {
            event.removeSpawnerData(data);
        }
        for (MobSpawnSettings.SpawnerData data : gold) {
            event.addSpawnerData(data);
        }
    }

    private static MobSpawnSettings.SpawnerData entry(
            EntityType<?> type, int weight, int min, int max) {
        return new MobSpawnSettings.SpawnerData(type, weight, min, max);
    }
}
