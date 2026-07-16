package danger.orespawn.init;

import danger.orespawn.util.Reference;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;

/**
 * Dimensions for OreSpawn 1.21.1.
 * <p>
 * <b>Classic 1.7.10</b> (primary gold): Utopia, Extreme, VillageMania, Islands, Crystal, Chaos —
 * datapack ids {@code orespawn:utopia} … {@code orespawn:chaos}.
 * <p>
 * <b>ConquerantFix Mining</b> kept as {@link #MINING} for reuse / command access.
 */
public final class ModDimensions {
    // --- Classic 1.7.10 (WorldProviderOreSpawn … OreSpawn6) ---

    /** 1.7.10 DimensionID — brown ant — Dimension-Utopia */
    public static final ResourceKey<Level> UTOPIA = dim("utopia");
    public static final ResourceKey<DimensionType> UTOPIA_TYPE = dimType("utopia");

    /** 1.7.10 DimensionID2 — red ant — Dimension-Extreme */
    public static final ResourceKey<Level> EXTREME = dim("extreme");
    public static final ResourceKey<DimensionType> EXTREME_TYPE = dimType("extreme");

    /** 1.7.10 DimensionID3 — rainbow ant — Dimension-VillageMania */
    public static final ResourceKey<Level> VILLAGE_MANIA = dim("village_mania");
    public static final ResourceKey<DimensionType> VILLAGE_MANIA_TYPE = dimType("village_mania");

    /** 1.7.10 DimensionID4 — unstable ant — Dimension-Islands */
    public static final ResourceKey<Level> ISLANDS = dim("islands");
    public static final ResourceKey<DimensionType> ISLANDS_TYPE = dimType("islands");

    /** 1.7.10 DimensionID5 — termite — Dimension-Crystal */
    public static final ResourceKey<Level> CRYSTAL = dim("crystal");
    public static final ResourceKey<DimensionType> CRYSTAL_TYPE = dimType("crystal");

    /** 1.7.10 DimensionID6 — butterfly — Dimension-Chaos */
    public static final ResourceKey<Level> CHAOS = dim("chaos");
    public static final ResourceKey<DimensionType> CHAOS_TYPE = dimType("chaos");

    // --- ConquerantFix (reuse) ---

    /** CF Mining Dimension — not a 1.7.10 provider; kept for existing content/commands. */
    public static final ResourceKey<Level> MINING = dim("mining");
    public static final ResourceKey<DimensionType> MINING_TYPE = dimType("mining");

    private static ResourceKey<Level> dim(String path) {
        return ResourceKey.create(
                Registries.DIMENSION,
                ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, path));
    }

    private static ResourceKey<DimensionType> dimType(String path) {
        return ResourceKey.create(
                Registries.DIMENSION_TYPE,
                ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, path));
    }

    private ModDimensions() {}
}
