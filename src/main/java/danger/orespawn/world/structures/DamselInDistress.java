package danger.orespawn.world.structures;

import danger.orespawn.init.ModEntities;
import danger.orespawn.util.Reference;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.RandomizableContainer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootTable;

/**
 * Gold {@code GenericDungeon.makeDamselInDistress}: cobble/mossy tower shell with
 * stepped front roof, iron-bar cage wall, dual Scorpion spawners, chest, and
 * Girlfriend spawner (gold spawned a live Girlfriend entity).
 * <p>
 * Gold {@code DungeonSpawnerBlock} type <b>28</b>. Origin is tower center at floor Y.
 */
public final class DamselInDistress {
    /** Gold {@code width = 4} (half-extent X). */
    public static final int HALF_W = 4;

    /** Gold {@code length = 4} (half-extent Z). */
    public static final int HALF_L = 4;

    /** Full X footprint. */
    public static final int WIDTH = HALF_W * 2 + 1;

    /** Full Z footprint. */
    public static final int DEPTH = HALF_L * 2 + 1;

    /** Gold wall loop height {@code height = 5}; stepped roof climbs to ~y+9. */
    public static final int WALL_HEIGHT = 5;

    /** Max Y above origin including stepped front ramp (gold k starts at height, m 4.0). */
    public static final int HEIGHT = 10;

    public static final ResourceKey<LootTable> LOOT_TABLE = ResourceKey.create(
            Registries.LOOT_TABLE,
            ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "chests/damsel_in_distress"));

    private DamselInDistress() {}

    /**
     * Places the damsel tower centered on {@code (cposx, cposy, cposz)}.
     *
     * @return true if written
     */
    public static boolean makeDamselInDistress(
            WorldGenLevel level, RandomSource random, int cposx, int cposy, int cposz) {
        int minY = level.getMinBuildHeight();
        int maxY = level.getMaxBuildHeight();
        if (cposy < minY || cposy + HEIGHT >= maxY) {
            return false;
        }

        StructureHelper.clearBox(
                level, cposx - HALF_W, cposy, cposz - HALF_L, WIDTH, HEIGHT, DEPTH);

        BlockState cobble = Blocks.COBBLESTONE.defaultBlockState();
        BlockState mossy = Blocks.MOSSY_COBBLESTONE.defaultBlockState();
        BlockState air = Blocks.AIR.defaultBlockState();
        BlockState bars = Blocks.IRON_BARS.defaultBlockState();

        // Gold main shell: floor + outer walls (mossy 1/8)
        for (int i = -HALF_W; i <= HALF_W; i++) {
            for (int j = -HALF_L; j <= HALF_L; j++) {
                for (int k = 0; k < WALL_HEIGHT; k++) {
                    BlockState bid = air;
                    if (k == 0) {
                        bid = cobble;
                    }
                    if (i == -HALF_W || i == HALF_W) {
                        bid = cobble;
                    }
                    if (j == -HALF_L || j == HALF_L) {
                        bid = cobble;
                    }
                    if (bid.getBlock() == Blocks.COBBLESTONE && random.nextInt(8) == 1) {
                        bid = mossy;
                    }
                    // Front doorway gap on −Z wall (k 1.3, i −1.1)
                    if ((k == 1 || k == 2 || k == 3) && (i == 0 || i == -1 || i == 1) && j == -HALF_L) {
                        bid = air;
                    }
                    StructureHelper.set(level, cposx + i, cposy + k, cposz + j, bid);
                }
            }
        }

        // Gold roof layer at height (inset X; Z −length . length-1)
        for (int i = -HALF_W + 1; i <= HALF_W - 1; i++) {
            for (int j = -HALF_L; j <= HALF_L - 1; j++) {
                BlockState bid = random.nextInt(8) == 1 ? mossy : cobble;
                StructureHelper.set(level, cposx + i, cposy + WALL_HEIGHT, cposz + j, bid);
            }
        }

        // Gold second roof at height+1 (further inset)
        for (int i = -HALF_W + 2; i <= HALF_W - 2; i++) {
            for (int j = -HALF_L; j <= HALF_L - 2; j++) {
                BlockState bid = random.nextInt(8) == 1 ? mossy : cobble;
                StructureHelper.set(level, cposx + i, cposy + WALL_HEIGHT + 1, cposz + j, bid);
            }
        }

        // Gold stepped front ramp on −Z face (m = width.0, k climbing)
        int k = WALL_HEIGHT;
        int jFront = -HALF_L;
        for (int m = HALF_W; m >= 0; m--) {
            for (int i = m; i >= 0; i--) {
                BlockState bid = random.nextInt(8) == 1 ? mossy : cobble;
                StructureHelper.set(level, cposx + i, cposy + k, cposz + jFront, bid);
                bid = random.nextInt(8) == 1 ? mossy : cobble;
                StructureHelper.set(level, cposx - i, cposy + k, cposz + jFront, bid);
            }
            k++;
        }

        // Gold iron-bar cage wall near +Z (length-3) interior
        int cageZ = HALF_L - 3;
        for (int i = -HALF_W + 1; i < HALF_W; i++) {
            for (int y = 1; y < WALL_HEIGHT; y++) {
                // gold: cposx - var22 with var22 from -width+1 . width-1 → places at +var22 effectively? 
                // gold: OreSpawnMain.setBlockFast(world, cposx - var22, .) where var22 runs -width+1.width-1
                // so worldX = cposx - var22 covers full interior range
                StructureHelper.set(level, cposx - i, cposy + y, cposz + cageZ, bars);
            }
        }

        // Gold Scorpion spawners at front corners
        placeSpawner(
                level,
                random,
                cposx - HALF_W + 1,
                cposy + 1,
                cposz - HALF_L + 1,
                scorpion());
        placeSpawner(
                level,
                random,
                cposx + HALF_W - 1,
                cposy + 1,
                cposz - HALF_L + 1,
                scorpion());

        // Gold chest rear corner (meta 2 ≈ north)
        placeChest(
                level,
                random,
                cposx + HALF_W - 1,
                cposy + 1,
                cposz + HALF_L - 1,
                Direction.NORTH);

        // Gold spawned live Girlfriend; port uses spawner (stable worldgen)
        placeSpawner(
                level,
                random,
                cposx - HALF_W + 2,
                cposy + 1,
                cposz + HALF_L - 1,
                girlfriend());

        return true;
    }

    private static EntityType<?> scorpion() {
        return ModEntities.SCORPION != null ? ModEntities.SCORPION.get() : EntityType.CAVE_SPIDER;
    }

    private static EntityType<?> girlfriend() {
        return ModEntities.GIRLFRIEND != null ? ModEntities.GIRLFRIEND.get() : EntityType.VILLAGER;
    }

    private static void placeSpawner(
            WorldGenLevel level, RandomSource random, int x, int y, int z, EntityType<?> mob) {
        BlockPos pos = new BlockPos(x, y, z);
        StructureHelper.set(level, pos, Blocks.SPAWNER.defaultBlockState());
        if (level.getBlockEntity(pos) instanceof SpawnerBlockEntity spawner) {
            spawner.setEntityId(mob, random);
        }
    }

    private static void placeChest(
            WorldGenLevel level, RandomSource random, int x, int y, int z, Direction facing) {
        BlockPos pos = new BlockPos(x, y, z);
        StructureHelper.set(
                level, pos, Blocks.CHEST.defaultBlockState().setValue(ChestBlock.FACING, facing));
        RandomizableContainer.setBlockEntityLootTable(level, random, pos, LOOT_TABLE);
    }
}
