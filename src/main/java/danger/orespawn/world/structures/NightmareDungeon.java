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
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootTable;

/**
 * Gold {@code NightmareDungeon}: 25×12×25 room, RTP floor, bedrock ceiling,
 * bedrock/end-stone walls, center spawner (Emperor Scorpion / Nightmare), two rich chests.
 * <p>
 * Note: gold never dispatches this class from {@code DungeonSpawnerBlock} (type 38 is
 * {@code makeNightmareRookery} on GenericDungeon). Port wires type 38 here until a full
 * NightmareRookery builder exists, and exposes {@link #makeDungeon} for worldgen features.
 */
public final class NightmareDungeon {
    public static final int WIDTH = 25;
    public static final int HEIGHT = 12;

    public static final ResourceKey<LootTable> LOOT_TABLE = ResourceKey.create(
            Registries.LOOT_TABLE,
            ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "chests/nightmare_dungeon"));

    private NightmareDungeon() {}

    /**
     * Places a nightmare dungeon with corner at {@code (cposx, cposy, cposz)}.
     *
     * @return true if the room was written
     */
    public static boolean makeDungeon(WorldGenLevel level, RandomSource random, int cposx, int cposy, int cposz) {
        int minY = level.getMinBuildHeight();
        int maxY = level.getMaxBuildHeight();
        if (cposy < minY || cposy + HEIGHT > maxY) {
            return false;
        }

        // 1) Carve full volume (gold: Blocks.air) — wipe stone/ore underground
        StructureHelper.clearBox(level, cposx, cposy, cposz, WIDTH, HEIGHT, WIDTH);

        // Floor: MyRTPBlock (block_teleport)
        BlockState rtp = ModBlocks.BLOCK_TELEPORT.get().defaultBlockState();
        for (int i = 0; i < WIDTH; i++) {
            for (int k = 0; k < WIDTH; k++) {
                StructureHelper.set(level, cposx + i, cposy, cposz + k, rtp);
            }
        }

        // Ceiling: solid bedrock (gold field_150357_h)
        int roofY = cposy + HEIGHT - 1;
        BlockState bedrock = Blocks.BEDROCK.defaultBlockState();
        for (int i = 0; i < WIDTH; i++) {
            for (int k = 0; k < WIDTH; k++) {
                StructureHelper.set(level, cposx + i, roofY, cposz + k, bedrock);
            }
        }

        // Walls along Z edges
        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                setWallBlock(level, random, cposx + i, cposy + j, cposz);
                setWallBlock(level, random, cposx + i, cposy + j, cposz + WIDTH - 1);
            }
        }

        // Walls along X edges
        for (int k = 0; k < WIDTH; k++) {
            for (int j = 0; j < HEIGHT; j++) {
                setWallBlock(level, random, cposx, cposy + j, cposz + k);
                setWallBlock(level, random, cposx + WIDTH - 1, cposy + j, cposz + k);
            }
        }

        // 2) Re-hollow interior after shell
        StructureHelper.hollowInterior(level, cposx, cposy, cposz, WIDTH, HEIGHT, WIDTH);

        int midX = cposx + WIDTH / 2;
        int midZ = cposz + WIDTH / 2;
        int interiorY = cposy + 1;

        // Center spawner — gold: Emperor Scorpion or Nightmare (PitchBlack)
        BlockPos spawnerPos = new BlockPos(midX, interiorY, midZ);
        StructureHelper.set(level, spawnerPos, Blocks.SPAWNER.defaultBlockState());
        if (level.getBlockEntity(spawnerPos) instanceof SpawnerBlockEntity spawner) {
            EntityType<?> type = random.nextBoolean()
                    ? ModEntities.EMPEROR_SCORPION.get()
                    : ModEntities.PITCH_BLACK.get();
            spawner.setEntityId(type, random);
        }

        // Two diagonal chests with rich loot (gold: mid±1, mid±1)
        placeLootChest(level, random, midX + 1, interiorY, midZ + 1, Direction.SOUTH);
        placeLootChest(level, random, midX - 1, interiorY, midZ - 1, Direction.NORTH);

        return true;
    }

    /** Gold setThisBlock: 50/50 bedrock / end stone. */
    private static void setWallBlock(WorldGenLevel level, RandomSource random, int x, int y, int z) {
        BlockState state = random.nextBoolean()
                ? Blocks.BEDROCK.defaultBlockState()
                : Blocks.END_STONE.defaultBlockState();
        StructureHelper.set(level, x, y, z, state);
    }

    private static void placeLootChest(
            WorldGenLevel level, RandomSource random, int x, int y, int z, Direction facing) {
        BlockPos chestPos = new BlockPos(x, y, z);
        BlockState chestState = Blocks.CHEST.defaultBlockState().setValue(ChestBlock.FACING, facing);
        StructureHelper.set(level, chestPos, chestState);
        RandomizableContainer.setBlockEntityLootTable(level, random, chestPos, LOOT_TABLE);
    }
}
