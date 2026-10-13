package danger.orespawn.world.gen;

import danger.orespawn.world.structures.BouncyCastle;
import danger.orespawn.world.structures.FrogPond;
import danger.orespawn.world.structures.GirlfriendIsland;
import danger.orespawn.world.structures.GoldFishBowl;
import danger.orespawn.world.structures.HauntedHouse;
import danger.orespawn.world.structures.Igloo;
import danger.orespawn.world.structures.LeafMonsterDungeon;
import danger.orespawn.world.structures.MantisHive;
import danger.orespawn.world.structures.MonsterIsland;
import danger.orespawn.world.structures.PlayPool;
import danger.orespawn.world.structures.RubberDuckyPond;
import danger.orespawn.world.structures.SmallBeeHive;
import danger.orespawn.world.structures.SpitBugLair;
import danger.orespawn.world.structures.WaterDragonLair;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

/**
 * Gold {@code OreSpawnWorld.generateSurface} small Overworld structures, run once per chunk.
 * <p>
 * While no structure was placed in the last 50 chunks ({@code recently_placed}), one of six water /
 * pond structures is rolled (Play Pool, Water Dragon Lair, Gold Fish Bowl, Girlfriend Island,
 * Monster Island, Frog Pond), then the first success of: bee/mantis hive, Haunted House,
 * Leaf Monster tower, Spit Bug lair, Igloo, Bouncy Castle, Rubber Ducky pond. Each has gold's own
 * 1-in-N chance, biome, surface block and placement offsets.
 */
public class OverworldDungeonsFeature extends Feature<NoneFeatureConfiguration> {
    /** Gold {@code OreSpawnWorld.recently_placed}: chunks left before another structure may spawn. */
    private static final AtomicInteger RECENTLY_PLACED = new AtomicInteger();
    private static final int COOLDOWN = 50;

    /** Gold "Ocean" (not "Deep Ocean"). */
    private static final Predicate<Holder<Biome>> OCEAN = b -> b.is(Biomes.OCEAN) || b.is(Biomes.LUKEWARM_OCEAN)
            || b.is(Biomes.WARM_OCEAN) || b.is(Biomes.COLD_OCEAN);
    private static final Predicate<Holder<Biome>> PLAINS = b -> b.is(Biomes.PLAINS) || b.is(Biomes.SUNFLOWER_PLAINS);
    private static final Predicate<Holder<Biome>> SWAMP = b -> b.is(Biomes.SWAMP) || b.is(Biomes.MANGROVE_SWAMP);
    /** Gold Forest/ForestHills/Jungle/JungleHills/Birch Forest (Hills). */
    private static final Predicate<Holder<Biome>> NEST_BIOMES = b -> b.is(Biomes.FOREST) || b.is(Biomes.FLOWER_FOREST)
            || b.is(Biomes.JUNGLE) || b.is(Biomes.BAMBOO_JUNGLE)
            || b.is(Biomes.BIRCH_FOREST) || b.is(Biomes.OLD_GROWTH_BIRCH_FOREST);

    private interface Builder {
        boolean build(WorldGenLevel level, RandomSource random, int x, int y, int z);
    }

    public OverworldDungeonsFeature() {
        super(NoneFeatureConfiguration.CODEC);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        if (RECENTLY_PLACED.get() > 0) {
            RECENTLY_PLACED.decrementAndGet();
            return false;
        }
        WorldGenLevel level = context.level();
        RandomSource random = context.random();
        int cx = context.origin().getX();
        int cz = context.origin().getZ();
        // gold reads the biome at the chunk corner
        Holder<Biome> biome = level.getBiome(new BlockPos(cx, level.getSeaLevel(), cz));

        boolean placed = switch (random.nextInt(6)) {
            case 0 -> this.water(level, random, cx, cz, biome, 350, 0, PlayPool::makePlayPool);
            case 1 -> this.water(level, random, cx, cz, biome, 350, -1, WaterDragonLair::makeWaterDragonLair);
            case 2 -> this.water(level, random, cx, cz, biome, 350, -1, GoldFishBowl::makeGoldFishBowl);
            case 3 -> this.water(level, random, cx, cz, biome, 300, -1, GirlfriendIsland::makeGirlfriendIsland);
            case 4 -> this.water(level, random, cx, cz, biome, 300, -1, MonsterIsland::makeMonsterIsland);
            default -> this.frogPond(level, random, cx, cz, biome);
        };
        placed |= this.nest(level, random, cx, cz, biome)
                || this.landScan(level, random, cx, cz, 285, biome, b -> PLAINS.test(b) || b.is(Biomes.TAIGA) || SWAMP.test(b),
                        5, 100, Blocks.GRASS_BLOCK.defaultBlockState(), 0, HauntedHouse::makeHauntedHouse)
                || this.single(level, random, cx, cz, 275, biome, PLAINS, s -> s.is(Blocks.GRASS_BLOCK), 0,
                        LeafMonsterDungeon::makeLeafMonsterDungeon)
                || this.single(level, random, cx, cz, 190, biome, SWAMP, s -> s.is(Blocks.GRASS_BLOCK), 0,
                        SpitBugLair::makeSpitBugLair)
                // gold checked for a snow block; snowy plains are grass + snow layer now, so accept either
                || this.single(level, random, cx, cz, 220, biome, b -> b.is(Biomes.SNOWY_PLAINS),
                        s -> s.is(Blocks.SNOW) || s.is(Blocks.SNOW_BLOCK), -2, Igloo::makeIgloo)
                || this.single(level, random, cx, cz, 230, biome, b -> b.is(Biomes.DESERT), s -> s.is(Blocks.SAND), -1,
                        BouncyCastle::makeBouncyCastle)
                || this.single(level, random, cx, cz, 275, biome, PLAINS, s -> s.is(Blocks.GRASS_BLOCK), 0,
                        RubberDuckyPond::makeRubberDuckyPond);
        return placed;
    }

    /** Gold addPlayPool & co: 1-in-N, Ocean, 4 tries at the first air-over-water from y 100 down. */
    private boolean water(WorldGenLevel level, RandomSource random, int cx, int cz, Holder<Biome> biome,
            int chance, int yOffset, Builder builder) {
        if (random.nextInt(chance) != 0 || !OCEAN.test(biome)) {
            return false;
        }
        for (int i = 0; i < 4; i++) {
            int x = cx + random.nextInt(16);
            int z = cz + random.nextInt(16);
            int y = firstAirAbove(level, x, z, s -> s.is(Blocks.WATER));
            if (y != Integer.MIN_VALUE) {
                return this.build(level, random, x, y + yOffset, z, builder);
            }
        }
        return false;
    }

    /** Gold addFrogPond: 1 in 350, Plains, 4 tries, pond at grass level. */
    private boolean frogPond(WorldGenLevel level, RandomSource random, int cx, int cz, Holder<Biome> biome) {
        if (random.nextInt(350) != 0 || !PLAINS.test(biome)) {
            return false;
        }
        for (int i = 0; i < 4; i++) {
            int x = cx + random.nextInt(16);
            int z = cz + random.nextInt(16);
            int y = firstAirAbove(level, x, z, s -> s.is(Blocks.GRASS_BLOCK));
            if (y != Integer.MIN_VALUE) {
                return this.build(level, random, x, y - 1, z, FrogPond::makeFrogPond);
            }
        }
        return false;
    }

    /** Gold addANest: 1 in 230, forests/jungles, 5 tries from y 128; bee hive or mantis hive. */
    private boolean nest(WorldGenLevel level, RandomSource random, int cx, int cz, Holder<Biome> biome) {
        if (random.nextInt(230) != 0 || !NEST_BIOMES.test(biome)) {
            return false;
        }
        for (int i = 0; i < 5; i++) {
            int x = cx + random.nextInt(16);
            int z = cz + random.nextInt(16);
            int y = airColumnGround(level, x, z, 128, Blocks.GRASS_BLOCK.defaultBlockState());
            if (y != Integer.MIN_VALUE) {
                Builder hive = random.nextInt(2) == 0 ? SmallBeeHive::makeSmallBeeHive : MantisHive::makeMantisHive;
                return this.build(level, random, x, y, z, hive);
            }
        }
        return false;
    }

    /** Gold addHauntedHouse: scan down through air only (stops at the first solid block). */
    private boolean landScan(WorldGenLevel level, RandomSource random, int cx, int cz, int chance,
            Holder<Biome> biome, Predicate<Holder<Biome>> biomes, int tries, int top, BlockState ground,
            int yOffset, Builder builder) {
        if (random.nextInt(chance) != 0 || !biomes.test(biome)) {
            return false;
        }
        for (int i = 0; i < tries; i++) {
            int x = cx + random.nextInt(16);
            int z = cz + random.nextInt(16);
            int y = airColumnGround(level, x, z, top, ground);
            if (y != Integer.MIN_VALUE) {
                return this.build(level, random, x, y + yOffset, z, builder);
            }
        }
        return false;
    }

    /** Gold addLeafMonster / addSpitBug / addIgloo / addBouncyCastle / addRubberDuckyPond: one try. */
    private boolean single(WorldGenLevel level, RandomSource random, int cx, int cz, int chance,
            Holder<Biome> biome, Predicate<Holder<Biome>> biomes, Predicate<BlockState> ground, int yOffset,
            Builder builder) {
        if (random.nextInt(chance) != 0 || !biomes.test(biome)) {
            return false;
        }
        int x = cx + random.nextInt(16);
        int z = cz + random.nextInt(16);
        int y = firstAirAbove(level, x, z, ground);
        return y != Integer.MIN_VALUE && this.build(level, random, x, y + yOffset, z, builder);
    }

    private boolean build(WorldGenLevel level, RandomSource random, int x, int y, int z, Builder builder) {
        if (builder.build(level, random, x, y, z)) {
            RECENTLY_PLACED.set(COOLDOWN);
            return true;
        }
        return false;
    }

    /** Gold "for y = 100..41: air at y and ground below" — returns y (the air block) or MIN_VALUE. */
    private static int firstAirAbove(WorldGenLevel level, int x, int z, Predicate<BlockState> ground) {
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos(x, 0, z);
        for (int y = 100; y > 40; y--) {
            if (level.getBlockState(pos.setY(y)).isAir() && ground.test(level.getBlockState(pos.setY(y - 1)))) {
                return y;
            }
        }
        return Integer.MIN_VALUE;
    }

    /** Gold "while (y > 40 && air) { if (below == ground) ...; y-- }". */
    private static int airColumnGround(WorldGenLevel level, int x, int z, int top, BlockState ground) {
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos(x, 0, z);
        for (int y = top; y > 40 && level.getBlockState(pos.setY(y)).isAir(); y--) {
            if (level.getBlockState(pos.setY(y - 1)).is(ground.getBlock())) {
                return y;
            }
        }
        return Integer.MIN_VALUE;
    }
}
