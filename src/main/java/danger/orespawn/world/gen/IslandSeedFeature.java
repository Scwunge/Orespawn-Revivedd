package danger.orespawn.world.gen;

import com.mojang.serialization.MapCodec;
import danger.orespawn.init.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

/**
 * Gold {@code OreSpawnWorld.addIslands} for Dimension-Islands:
 * ~1/10 of chunks place {@link danger.orespawn.blocks.BlockIsland} on grass surface.
 * The block then ticks → spawns floating {@code Island}/{@code IslandToo} entities.
 */
public class IslandSeedFeature extends Feature<NoneFeatureConfiguration> {
    public static final MapCodec<IslandSeedFeature> CODEC = MapCodec.unit(IslandSeedFeature::new);

    public IslandSeedFeature() {
        super(NoneFeatureConfiguration.CODEC);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        WorldGenLevel level = context.level();
        BlockPos origin = context.origin();
        RandomSource random = context.random();

        // gold: random.nextInt(10 + LessLag*2) == 1 → LessLag 0 → 1/10
        if (random.nextInt(10) != 1) {
            return false;
        }

        int chunkBaseX = (origin.getX() >> 4) << 4;
        int chunkBaseZ = (origin.getZ() >> 4) << 4;
        // gold: posX = 2 + chunkX + r12
        int posX = 2 + chunkBaseX + random.nextInt(12);
        int posZ = 2 + chunkBaseZ + random.nextInt(12);

        // gold flat islands surface is y≈7–8; scan from 20 down like gold
        for (int posY = 20; posY > 2; posY--) {
            BlockPos above = new BlockPos(posX, posY, posZ);
            BlockPos below = above.below();
            if (!level.isEmptyBlock(above)) {
                continue;
            }
            if (level.getBlockState(below).is(Blocks.GRASS_BLOCK)) {
                level.setBlock(above, ModBlocks.ISLAND_BLOCK.get().defaultBlockState(), 2);
                return true;
            }
        }
        return false;
    }
}
