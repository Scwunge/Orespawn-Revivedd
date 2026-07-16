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
 * Gold {@code GenericDungeon.makePumpkin}: 14×14×12 orange shell (stained clay →
 * orange wool), jack-o face air gaps on front, green stem, hollow interior with
 * wood platform, jack o'lanterns, fire, dual Ghost Pumpkin Skelly spawners.
 * <p>
 * Gold {@code DungeonSpawnerBlock} type <b>44</b> (calls with {@code y+1}).
 * Origin is the min-corner of the shell AABB (gold corner placement).
 */
public final class PumpkinHouse {
    /** Gold {@code width = 14}. */
    public static final int WIDTH = 14;

    /** Gold {@code height = 14}. */
    public static final int HEIGHT = 14;

    /** Gold {@code depth = 12}. */
    public static final int DEPTH = 12;

    /** Stem climbs {@code height + 0.3} → pad for bounds. */
    public static final int STEM_EXTRA = 4;

    public static final ResourceKey<LootTable> LOOT_TABLE = ResourceKey.create(
            Registries.LOOT_TABLE,
            ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "chests/pumpkin_house"));

    private PumpkinHouse() {}

    /**
     * Places a pumpkin house with min-corner at {@code (cposx, cposy, cposz)}.
     *
     * @return true if written
     */
    public static boolean makePumpkin(WorldGenLevel level, RandomSource random, int cposx, int cposy, int cposz) {
        int minY = level.getMinBuildHeight();
        int maxY = level.getMaxBuildHeight();
        if (cposy < minY || cposy + HEIGHT + STEM_EXTRA >= maxY) {
            return false;
        }

        BlockState orange = Blocks.ORANGE_WOOL.defaultBlockState();
        BlockState green = Blocks.GREEN_WOOL.defaultBlockState();
        BlockState air = Blocks.AIR.defaultBlockState();
        BlockState planks = Blocks.OAK_PLANKS.defaultBlockState();
        BlockState jack = Blocks.JACK_O_LANTERN.defaultBlockState();
        BlockState fire = Blocks.FIRE.defaultBlockState();

        // Clear full shell + stem volume so underground stone never remains inside
        StructureHelper.clearBox(level, cposx, cposy, cposz, WIDTH, HEIGHT + STEM_EXTRA, DEPTH);

        // Gold: shell on floor/ceiling/X walls/Z walls; interior stays air
        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                for (int k = 0; k < DEPTH; k++) {
                    boolean shell = j == 0
                            || j == HEIGHT - 1
                            || i == 0
                            || i == WIDTH - 1
                            || k == 0
                            || k == DEPTH - 1;
                    StructureHelper.set(
                            level, cposx + i, cposy + j, cposz + k, shell ? orange : air);
                }
            }
        }

        // Re-hollow interior (belt-and-suspenders for underground)
        StructureHelper.hollowInterior(level, cposx, cposy, cposz, WIDTH, HEIGHT, DEPTH);

        // Gold: jack-o face air gaps on front face (k = 0)
        carveFace(level, cposx, cposy, cposz);

        // Gold: green stem on top (dark_green stained clay → green wool)
        int stemZ = DEPTH / 2 - 1;
        for (int up = 0; up < 4; up++) {
            for (int len = 0; len < 3; len++) {
                StructureHelper.set(
                        level,
                        cposx + WIDTH / 2 - len - up,
                        cposy + HEIGHT + up,
                        cposz + stemZ,
                        green);
            }
        }

        // Gold: 2×2 oak plank platform / column inside (y+1.y+5)
        for (int j = 0; j < 5; j++) {
            for (int dx = 0; dx < 2; dx++) {
                for (int dz = 0; dz < 2; dz++) {
                    StructureHelper.set(
                            level,
                            cposx + WIDTH / 2 + dx - 1,
                            cposy + j + 1,
                            cposz + DEPTH / 2 + dz - 1,
                            planks);
                }
            }
        }

        // Gold: 2×2 jack o'lanterns at y+6
        for (int dx = 0; dx < 2; dx++) {
            for (int dz = 0; dz < 2; dz++) {
                StructureHelper.set(
                        level,
                        cposx + WIDTH / 2 + dx - 1,
                        cposy + 6,
                        cposz + DEPTH / 2 + dz - 1,
                        jack);
            }
        }

        // Gold: fire on front half of platform top (y+7, z = depth/2 - 1)
        for (int dx = 0; dx < 2; dx++) {
            StructureHelper.set(
                    level,
                    cposx + WIDTH / 2 + dx - 1,
                    cposy + 7,
                    cposz + DEPTH / 2 - 1,
                    fire);
        }

        // Gold: dual "Ghost Pumpkin Skelly" spawners → GHOST_SKELLY
        placeSpawner(level, random, cposx + WIDTH / 2 - 1, cposy + 7, cposz + DEPTH / 2);
        placeSpawner(level, random, cposx + WIDTH / 2, cposy + 7, cposz + DEPTH / 2);

        // Port loot chest (gold had none) — floor near rear wall
        BlockPos chestPos = new BlockPos(cposx + WIDTH / 2, cposy + 1, cposz + DEPTH - 2);
        StructureHelper.set(
                level,
                chestPos,
                Blocks.CHEST.defaultBlockState().setValue(ChestBlock.FACING, Direction.NORTH));
        RandomizableContainer.setBlockEntityLootTable(level, random, chestPos, LOOT_TABLE);

        return true;
    }

    /**
     * Gold face cutouts on {@code k == 0} (eyes + mouth / door gap).
     * Uses {@code width/2 - 1} and {@code width/2} as the two eye columns.
     */
    private static void carveFace(WorldGenLevel level, int cposx, int cposy, int cposz) {
        BlockState air = Blocks.AIR.defaultBlockState();
        int k = 0;

        // Right eye column (var16 = width/2 - 1)
        int right = WIDTH / 2 - 1;
        for (int j : new int[] {11, 10, 9}) {
            StructureHelper.set(level, cposx + right + 3, cposy + j, cposz + k, air);
            StructureHelper.set(level, cposx + right + 4, cposy + j, cposz + k, air);
            StructureHelper.set(level, cposx + right + 5, cposy + j, cposz + k, air);
        }
        for (int j : new int[] {8, 7}) {
            StructureHelper.set(level, cposx + right + 2, cposy + j, cposz + k, air);
            StructureHelper.set(level, cposx + right + 3, cposy + j, cposz + k, air);
        }
        StructureHelper.set(level, cposx + right + 1, cposy + 4, cposz + k, air);
        StructureHelper.set(level, cposx + right + 4, cposy + 4, cposz + k, air);
        for (int j : new int[] {3, 2}) {
            StructureHelper.set(level, cposx + right + 1, cposy + j, cposz + k, air);
            StructureHelper.set(level, cposx + right + 2, cposy + j, cposz + k, air);
            StructureHelper.set(level, cposx + right + 3, cposy + j, cposz + k, air);
            StructureHelper.set(level, cposx + right + 4, cposy + j, cposz + k, air);
        }
        StructureHelper.set(level, cposx + right + 2, cposy + 1, cposz + k, air);

        // Left eye column (var16 = width/2)
        int left = WIDTH / 2;
        for (int j : new int[] {11, 10, 9}) {
            StructureHelper.set(level, cposx + left - 3, cposy + j, cposz + k, air);
            StructureHelper.set(level, cposx + left - 4, cposy + j, cposz + k, air);
            StructureHelper.set(level, cposx + left - 5, cposy + j, cposz + k, air);
        }
        for (int j : new int[] {8, 7}) {
            StructureHelper.set(level, cposx + left - 2, cposy + j, cposz + k, air);
            StructureHelper.set(level, cposx + left - 3, cposy + j, cposz + k, air);
        }
        StructureHelper.set(level, cposx + left - 1, cposy + 4, cposz + k, air);
        StructureHelper.set(level, cposx + left - 4, cposy + 4, cposz + k, air);
        for (int j : new int[] {3, 2}) {
            StructureHelper.set(level, cposx + left - 1, cposy + j, cposz + k, air);
            StructureHelper.set(level, cposx + left - 2, cposy + j, cposz + k, air);
            StructureHelper.set(level, cposx + left - 3, cposy + j, cposz + k, air);
            StructureHelper.set(level, cposx + left - 4, cposy + j, cposz + k, air);
        }
        StructureHelper.set(level, cposx + left - 2, cposy + 1, cposz + k, air);
    }

    private static void placeSpawner(WorldGenLevel level, RandomSource random, int x, int y, int z) {
        BlockPos pos = new BlockPos(x, y, z);
        StructureHelper.set(level, pos, Blocks.SPAWNER.defaultBlockState());
        if (level.getBlockEntity(pos) instanceof SpawnerBlockEntity spawner) {
            // Gold "Ghost Pumpkin Skelly" → closest port entity
            EntityType<?> type =
                    ModEntities.GHOST_SKELLY != null ? ModEntities.GHOST_SKELLY.get() : EntityType.SKELETON;
            spawner.setEntityId(type, random);
        }
    }
}
