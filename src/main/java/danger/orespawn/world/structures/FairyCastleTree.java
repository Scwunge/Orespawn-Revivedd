package danger.orespawn.world.structures;

import danger.orespawn.init.ModBlocks;
import danger.orespawn.init.ModEntities;
import danger.orespawn.util.Reference;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.RandomizableContainer;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootTable;

/**
 * Gold {@code Trees.FairyCastleTree}: multi-layer crystal "castle tree" of solid
 * log platforms with perimeter castle leaves, corner crystal torches, and
 * random Fairy spawners / crystal loot chests on platform centers.
 * <p>
 * Gold algorithm (CFR-reconstructed from {@code Trees.class}; Vineflower OOM'd):
 * <ul>
 *   <li>{@code nc = 6} layers (LessLag shrink omitted — lag mode 0 default)</li>
 *   <li>Height {@code j = 3+rand(3)}; each layer {@code grow = 4+rand(3)}</li>
 *   <li>Layer 0: one +X-offset platform at {@code spread=0} (no center decor)</li>
 *   <li>Layers ≥1: four cardinal platforms at ±spread on X/Z</li>
 *   <li>Layers ≥2: four diagonal platforms at (±spread, ±spread)</li>
 *   <li>After each layer: {@code j += grow}; first layer sets {@code spread=3}
 *       then {@code spread += grow}</li>
 * </ul>
 * Gold {@code DungeonSpawnerBlock} type <b>1</b> calls this
 * ({@code OreSpawnMain.OreSpawnTrees.FairyCastleTree}).
 * <p>
 * <b>Size (finite, lag-0):</b> layers fixed at 6. Max Y span from origin ≈
 * {@code 5 + 6×6 + torch/leaf pad ≈ 43} → {@link #MAX_HEIGHT}{@code =48}.
 * Max horizontal half-extent ≈ {@code 3 + 6×6 + width≤9 + leaf pad ≈50}
 * → {@link #MAX_RADIUS}{@code =52}. All loops are gold-finite (no recursion).
 */
public final class FairyCastleTree {
    /** Gold layer count at LessLag==0. */
    public static final int LAYERS = 6;

    /**
     * Conservative max Y span above origin for bounds checks.
     * Gold: j starts ≤5, each of 6 layers adds grow≤6 → j≤41; torch/leaves +2.
     */
    public static final int MAX_HEIGHT = 48;

    /**
     * Approx half-radius: final spread ≤3+6×6=39, platform width ≤1+rand(3+5)=9,
     * castle leaves ±1.
     */
    public static final int MAX_RADIUS = 52;

    /**
     * Gold {@code Trees.CrystalChestContentsList} — same pool as FairyTree /
     * {@code addSomething} chests. Reuses existing JSON (no separate castle table).
     */
    public static final ResourceKey<LootTable> LOOT_TABLE = ResourceKey.create(
            Registries.LOOT_TABLE,
            ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "chests/fairy_tree"));

    private FairyCastleTree() {}

    /**
     * Places a Fairy Castle Tree centered at {@code (x, y, z)} (gold origin).
     *
     * @return true if the structure was written
     */
    public static boolean makeFairyCastleTree(WorldGenLevel level, RandomSource random, int x, int y, int z) {
        int minY = level.getMinBuildHeight();
        int maxY = level.getMaxBuildHeight();
        if (y < minY || y + MAX_HEIGHT >= maxY) {
            return false;
        }

        // Gold: nc = 6; LessLag shrinks omitted (default lag mode 0)
        int nc = LAYERS;
        int j = 3 + random.nextInt(3);
        int spread = 0;

        for (int iter = 0; iter < nc; iter++) {
            int grow = 4 + random.nextInt(3);
            int width = 1 + random.nextInt(3);
            int randy = random.nextInt(3) - 1;

            // Gold arm 1: +X offset (spread). Center decor only when iter != 0.
            placePlatform(level, random, x + spread, y + j + randy, z, width, iter != 0);

            if (iter != 0) {
                // Gold arm 2: -X
                width = 1 + random.nextInt(3 + iter);
                randy = random.nextInt(3) - 1;
                placePlatform(level, random, x - spread, y + j + randy, z, width, true);

                // Gold arm 3: +Z
                width = 1 + random.nextInt(3 + iter);
                randy = random.nextInt(3) - 1;
                placePlatform(level, random, x, y + j + randy, z + spread, width, true);

                // Gold arm 4: -Z
                width = 1 + random.nextInt(3 + iter);
                randy = random.nextInt(3) - 1;
                placePlatform(level, random, x, y + j + randy, z - spread, width, true);
            }

            // Gold diagonals when iter >= 2 (bytecode places this outside the iter!=0 block)
            if (iter >= 2) {
                // +X +Z
                width = 1 + random.nextInt(3 + iter);
                randy = random.nextInt(3) - 1;
                placePlatform(level, random, x + spread, y + j + randy, z + spread, width, true);

                // -X -Z
                width = 1 + random.nextInt(3 + iter);
                randy = random.nextInt(3) - 1;
                placePlatform(level, random, x - spread, y + j + randy, z - spread, width, true);

                // -X +Z
                width = 1 + random.nextInt(3 + iter);
                randy = random.nextInt(3) - 1;
                placePlatform(level, random, x - spread, y + j + randy, z + spread, width, true);

                // +X -Z
                width = 1 + random.nextInt(3 + iter);
                randy = random.nextInt(3) - 1;
                placePlatform(level, random, x + spread, y + j + randy, z - spread, width, true);
            }

            j += grow;
            if (iter == 0) {
                spread = 3;
            }
            spread += grow;
        }

        return true;
    }

    /**
     * Gold solid square of {@code MyCrystalTreeLog} at {@code (cx,cy,cz)} with
     * inclusive half-width {@code width}: perimeter castle leaves, corner torches
     * at cy+1, optional center {@link #addSomething}.
     */
    private static void placePlatform(
            WorldGenLevel level,
            RandomSource random,
            int cx,
            int cy,
            int cz,
            int width,
            boolean centerDecor) {
        BlockState log = ModBlocks.CRYSTAL_TREE_LOG.get().defaultBlockState();
        BlockState torch = ModBlocks.CRYSTAL_TORCH.get().defaultBlockState();

        for (int i = -width; i <= width; i++) {
            for (int k = -width; k <= width; k++) {
                int px = cx + i;
                int pz = cz + k;
                StructureHelper.set(level, px, cy, pz, log);

                // Gold: perimeter → make_crystal_castle_leaves
                if (i == -width || i == width || k == -width || k == width) {
                    makeCrystalCastleLeaves(level, px, cy, pz);
                }

                // Gold: center addSomething (Fairy spawner / chest at y+1)
                if (centerDecor && i == 0 && k == 0) {
                    addSomething(level, random, px, cy, pz);
                }

                // Gold: corner CrystalTorch at y+1
                if (i == -width && (k == -width || k == width)) {
                    StructureHelper.set(level, px, cy + 1, pz, torch);
                }
                if (i == width && (k == -width || k == width)) {
                    StructureHelper.set(level, px, cy + 1, pz, torch);
                }
            }
        }
    }

    /**
     * Gold {@code Trees.make_crystal_castle_leaves}: ±1 X/Z, Y and Y+1, air only.
     * Y+0 → {@code MyCrystalLeaves2}; Y+1 → {@code MyCrystalLeaves3}.
     */
    private static void makeCrystalCastleLeaves(WorldGenLevel level, int x, int y, int z) {
        BlockState leaves2 = leafState(ModBlocks.CRYSTAL_TREE_LEAVES_2.get().defaultBlockState());
        BlockState leaves3 = leafState(ModBlocks.CRYSTAL_TREE_LEAVES_3.get().defaultBlockState());
        for (int l1 = -1; l1 <= 1; l1++) {
            for (int l2 = -1; l2 <= 1; l2++) {
                for (int l3 = 0; l3 <= 1; l3++) {
                    BlockPos pos = new BlockPos(x + l1, y + l3, z + l2);
                    if (level.getBlockState(pos).isAir()) {
                        StructureHelper.set(level, pos, l3 != 0 ? leaves3 : leaves2);
                    }
                }
            }
        }
    }

    /** Start leaves at distance 1 so they don't decay before log-distance refresh. */
    private static BlockState leafState(BlockState leaves) {
        if (leaves.hasProperty(net.minecraft.world.level.block.LeavesBlock.DISTANCE)) {
            leaves = leaves.setValue(net.minecraft.world.level.block.LeavesBlock.DISTANCE, 1);
        }
        if (leaves.hasProperty(net.minecraft.world.level.block.LeavesBlock.PERSISTENT)) {
            leaves = leaves.setValue(net.minecraft.world.level.block.LeavesBlock.PERSISTENT, false);
        }
        return leaves;
    }

    /**
     * Gold {@code Trees.addSomething}: {@code nextInt(3)} —
     * 1 → Fairy spawner at (x, y+1, z); 2 → crystal-loot chest; 0 → nothing.
     */
    private static void addSomething(WorldGenLevel level, RandomSource random, int x, int y, int z) {
        int i = random.nextInt(3);
        if (i == 1) {
            placeSpawner(level, random, new BlockPos(x, y + 1, z), ModEntities.FAIRY.get());
        }
        if (i == 2) {
            BlockPos chestPos = new BlockPos(x, y + 1, z);
            // Gold chest meta 0 → facing west (matches FairyTree)
            BlockState chestState = Blocks.CHEST.defaultBlockState().setValue(ChestBlock.FACING, Direction.WEST);
            StructureHelper.set(level, chestPos, chestState);
            RandomizableContainer.setBlockEntityLootTable(level, random, chestPos, LOOT_TABLE);
        }
    }

    private static void placeSpawner(
            WorldGenLevel level, RandomSource random, BlockPos pos, net.minecraft.world.entity.EntityType<?> type) {
        StructureHelper.set(level, pos, Blocks.SPAWNER.defaultBlockState());
        if (level.getBlockEntity(pos) instanceof SpawnerBlockEntity spawner) {
            spawner.setEntityId(type, random);
        }
    }
}
