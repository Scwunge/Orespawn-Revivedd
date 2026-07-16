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
 * Gold {@code GenericDungeon}: ~12x6x12 cobble/mossy room, air interior, center spawner + wall chest.
 */
public final class GenericDungeon {
    public static final int WIDTH = 12;
    public static final int HEIGHT = 6;

    public static final ResourceKey<LootTable> LOOT_TABLE = ResourceKey.create(
            Registries.LOOT_TABLE,
            ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "chests/generic_dungeon"));

    private GenericDungeon() {}

    /**
     * Places a dungeon with corner at {@code (cposx, cposy, cposz)}.
     *
     * @return true if the room was written
     */
    public static boolean makeDungeon(WorldGenLevel level, RandomSource random, int cposx, int cposy, int cposz) {
        int minY = level.getMinBuildHeight();
        int maxY = level.getMaxBuildHeight();
        if (cposy < minY || cposy + HEIGHT > maxY) {
            return false;
        }

        // 1) Carve full volume (underground stone/ore must go before walls)
        StructureHelper.clearBox(level, cposx, cposy, cposz, WIDTH, HEIGHT, WIDTH);

        // Floor: always mossy cobble (gold)
        for (int i = 0; i < WIDTH; i++) {
            for (int k = 0; k < WIDTH; k++) {
                StructureHelper.set(
                        level, cposx + i, cposy, cposz + k, Blocks.MOSSY_COBBLESTONE.defaultBlockState());
            }
        }

        // Ceiling: random cobble / mossy
        int roofY = cposy + HEIGHT - 1;
        for (int i = 0; i < WIDTH; i++) {
            for (int k = 0; k < WIDTH; k++) {
                setWallBlock(level, random, cposx + i, roofY, cposz + k);
            }
        }

        // Walls along Z edges (X walls)
        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                setWallBlock(level, random, cposx + i, cposy + j, cposz);
                setWallBlock(level, random, cposx + i, cposy + j, cposz + WIDTH - 1);
            }
        }

        // Walls along X edges (Z walls)
        for (int k = 0; k < WIDTH; k++) {
            for (int j = 0; j < HEIGHT; j++) {
                setWallBlock(level, random, cposx, cposy + j, cposz + k);
                setWallBlock(level, random, cposx + WIDTH - 1, cposy + j, cposz + k);
            }
        }

        // 2) Re-hollow interior after shell (belt-and-suspenders for underground)
        StructureHelper.hollowInterior(level, cposx, cposy, cposz, WIDTH, HEIGHT, WIDTH);

        int midX = cposx + WIDTH / 2;
        int midZ = cposz + WIDTH / 2;
        int interiorY = cposy + 1;

        // Spawner (center) — gold rotated Alien / GammaMetroid / Cryolophosaurus
        BlockPos spawnerPos = new BlockPos(midX, interiorY, midZ);
        StructureHelper.set(level, spawnerPos, Blocks.SPAWNER.defaultBlockState());
        if (level.getBlockEntity(spawnerPos) instanceof SpawnerBlockEntity spawner) {
            EntityType<?> type = switch (random.nextInt(3)) {
                case 0 -> ModEntities.ALIEN.get();
                case 1 -> ModEntities.GAMMAMETROID.get();
                default -> ModEntities.CRYOLOPHOSAURUS.get();
            };
            spawner.setEntityId(type, random);
        }

        // Chest against the south-facing wall edge (gold: z = cposz + 1, facing SOUTH)
        BlockPos chestPos = new BlockPos(midX, interiorY, cposz + 1);
        BlockState chestState = Blocks.CHEST.defaultBlockState().setValue(ChestBlock.FACING, Direction.SOUTH);
        StructureHelper.set(level, chestPos, chestState);
        RandomizableContainer.setBlockEntityLootTable(level, random, chestPos, LOOT_TABLE);

        return true;
    }

    private static void setWallBlock(WorldGenLevel level, RandomSource random, int x, int y, int z) {
        BlockState state = random.nextBoolean()
                ? Blocks.MOSSY_COBBLESTONE.defaultBlockState()
                : Blocks.COBBLESTONE.defaultBlockState();
        StructureHelper.set(level, x, y, z, state);
    }
}
