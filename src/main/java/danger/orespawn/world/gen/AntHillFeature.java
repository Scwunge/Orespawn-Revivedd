package danger.orespawn.world.gen;

import danger.orespawn.init.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

/**
 * Gold {@code OreSpawnWorld} ant nests. The chunk roll (1 in 30 / 25 / 40) is the placed
 * feature's rarity filter; this places the nests for one chunk from its corner.
 * <ul>
 *   <li>{@link Kind#SURFACE} ({@code addAnts}): 4 tries, top block must be grass with air
 *       above, scanning down from y 100 to 40. 1 in {@code specialChance} nests is a Red,
 *       Rainbow, Unstable or Termite nest (equal odds); the rest are (black) Ant Nests.
 *       Gold used 4 for the Overworld and Village dimension, 2 for Mining and Chaos.</li>
 *   <li>{@link Kind#NETHER} ({@code addNetherAnts}): 3 tries, Red Ant Nest on netherrack, y 90..21.</li>
 *   <li>{@link Kind#CRYSTAL} ({@code addCrystalTermites}): 3 tries, Crystal Termite Nest on
 *       crystal grass, y 100..51.</li>
 * </ul>
 */
public class AntHillFeature extends Feature<NoneFeatureConfiguration> {
    public enum Kind { SURFACE, NETHER, CRYSTAL }

    private final Kind kind;
    private final int specialChance;

    public AntHillFeature(Kind kind, int specialChance) {
        super(NoneFeatureConfiguration.CODEC);
        this.kind = kind;
        this.specialChance = specialChance;
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        WorldGenLevel level = context.level();
        RandomSource random = context.random();
        BlockPos corner = context.origin();
        int tries = this.kind == Kind.SURFACE ? 4 : 3;
        boolean placed = false;
        for (int i = 0; i < tries; i++) {
            int x = corner.getX() + random.nextInt(16);
            int z = corner.getZ() + random.nextInt(16);
            BlockPos ground = this.findGround(level, x, z);
            if (ground != null) {
                level.setBlock(ground, this.pickNest(random).defaultBlockState(), 2);
                placed = true;
            }
        }
        return placed;
    }

    private BlockPos findGround(WorldGenLevel level, int x, int z) {
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos(x, 0, z);
        switch (this.kind) {
            case SURFACE -> {
                // gold: while (y > 40 && air) { if (below is grass) place; else y-- }
                for (int y = 100; y > 40 && level.isEmptyBlock(pos.setY(y)); y--) {
                    if (level.getBlockState(pos.setY(y - 1)).is(Blocks.GRASS_BLOCK)) {
                        return pos.immutable();
                    }
                }
            }
            case NETHER -> {
                for (int y = 90; y > 20; y--) {
                    if (level.isEmptyBlock(pos.setY(y)) && level.getBlockState(pos.setY(y - 1)).is(Blocks.NETHERRACK)) {
                        return pos.immutable();
                    }
                }
            }
            case CRYSTAL -> {
                for (int y = 100; y > 50; y--) {
                    if (level.isEmptyBlock(pos.setY(y))
                            && level.getBlockState(pos.setY(y - 1)).is(ModBlocks.CRYSTAL_GRASS.get())) {
                        return pos.immutable();
                    }
                }
            }
        }
        return null;
    }

    private Block pickNest(RandomSource random) {
        return switch (this.kind) {
            case NETHER -> ModBlocks.ANT_BLOCK.get();
            case CRYSTAL -> ModBlocks.CRYSTAL_TERMITE_NEST.get();
            case SURFACE -> {
                if (random.nextInt(this.specialChance) != 0) {
                    yield ModBlocks.BLACK_ANT_NEST.get();
                }
                yield switch (random.nextInt(4)) {
                    case 0 -> ModBlocks.ANT_BLOCK.get();
                    case 1 -> ModBlocks.RAINBOW_ANT_NEST.get();
                    case 2 -> ModBlocks.UNSTABLE_ANT_NEST.get();
                    default -> ModBlocks.TERMITE_NEST.get();
                };
            }
        };
    }
}
