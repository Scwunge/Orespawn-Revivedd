package danger.orespawn.world.gen;

import com.mojang.serialization.MapCodec;
import danger.orespawn.blocks.BlockCrystalPlant;
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
import net.minecraft.world.level.levelgen.Heightmap;

/**
 * Crystal-dim surface décor: mixed full crystal trees (tall / scraggly / blue),
 * flowers, and occasional saplings. Trees are placed fully grown so the dim
 * looks forested without waiting on random ticks.
 * <p>
 * Density kept sparse (playtest 2026-07-15: previous 10×12@50% was a solid canopy).
 */
public class CrystalDecorFeature extends Feature<NoneFeatureConfiguration> {
    public static final MapCodec<CrystalDecorFeature> CODEC = MapCodec.unit(CrystalDecorFeature::new);

    /** Min horizontal gap between crystal tree trunks. */
    private static final int TREE_SPACING = 6;

    public CrystalDecorFeature() {
        super(NoneFeatureConfiguration.CODEC);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        WorldGenLevel level = context.level();
        RandomSource random = context.random();
        BlockPos origin = context.origin();

        int placed = 0;
        // Few tries, wide scatter — open crystal glades not a solid forest
        for (int t = 0; t < 4; t++) {
            int dx = random.nextInt(15) - 7;
            int dz = random.nextInt(15) - 7;
            int x = origin.getX() + dx;
            int z = origin.getZ() + dz;
            int y = level.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, x, z);
            BlockPos ground = new BlockPos(x, y - 1, z);
            BlockPos above = new BlockPos(x, y, z);
            if (!level.isEmptyBlock(above)) {
                continue;
            }
            BlockState floor = level.getBlockState(ground);
            if (!isCrystalSoil(floor)) {
                continue;
            }

            int roll = random.nextInt(10);
            if (roll < 2) {
                // ~20% trees — all three styles; skip if another trunk is nearby
                if (hasNearbyTree(level, above)) {
                    continue;
                }
                BlockCrystalPlant.TreeStyle style = BlockCrystalPlant.randomStyle(random);
                BlockCrystalPlant.growTree(level, above, random, style);
                placed++;
            } else if (roll < 3) {
                // Occasional saplings for slow extra growth
                Block sapling = switch (random.nextInt(3)) {
                    case 1 -> ModBlocks.CRYSTAL_SAPLING_2.get();
                    case 2 -> ModBlocks.CRYSTAL_SAPLING_3.get();
                    default -> ModBlocks.CRYSTAL_SAPLING.get();
                };
                level.setBlock(above, sapling.defaultBlockState(), 2);
                placed++;
            } else if (roll < 7) {
                Block flower = switch (random.nextInt(4)) {
                    case 1 -> ModBlocks.CRYSTALFLOWER_GREEN.get();
                    case 2 -> ModBlocks.CRYSTALFLOWER_BLUE.get();
                    case 3 -> ModBlocks.CRYSTALFLOWER_YELLOW.get();
                    default -> ModBlocks.CRYSTALFLOWER_RED.get();
                };
                level.setBlock(above, flower.defaultBlockState(), 2);
                placed++;
            }
            // else empty — leave open ground
        }
        return placed > 0;
    }

    private static boolean hasNearbyTree(WorldGenLevel level, BlockPos pos) {
        Block log = ModBlocks.CRYSTAL_TREE_LOG.get();
        int r = TREE_SPACING;
        for (int dx = -r; dx <= r; dx++) {
            for (int dz = -r; dz <= r; dz++) {
                if (dx * dx + dz * dz > r * r) {
                    continue;
                }
                for (int dy = -1; dy <= 4; dy++) {
                    if (level.getBlockState(pos.offset(dx, dy, dz)).is(log)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private static boolean isCrystalSoil(BlockState state) {
        return state.is(ModBlocks.CRYSTAL_GRASS.get())
                || state.is(ModBlocks.CRYSTAL_STONE.get())
                || state.is(Blocks.GRASS_BLOCK)
                || state.is(Blocks.DIRT);
    }
}
