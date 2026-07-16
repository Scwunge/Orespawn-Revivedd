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
 * Gold {@code RubyBirdDungeon}: 10×5×10 room, lapis floor, mixed lapis/cobble/ruby walls,
 * center Ruby Bird spawner, single wall chest.
 * <p>
 * Gold {@code DungeonSpawnerBlock} type <b>22</b> calls this structure.
 */
public final class RubyBirdDungeon {
    public static final int WIDTH = 10;
    public static final int HEIGHT = 5;

    public static final ResourceKey<LootTable> LOOT_TABLE = ResourceKey.create(
            Registries.LOOT_TABLE,
            ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "chests/ruby_bird_dungeon"));

    private RubyBirdDungeon() {}

    /**
     * Places a ruby bird dungeon with corner at {@code (cposx, cposy, cposz)}.
     *
     * @return true if the room was written
     */
    public static boolean makeDungeon(WorldGenLevel level, RandomSource random, int cposx, int cposy, int cposz) {
        int minY = level.getMinBuildHeight();
        int maxY = level.getMaxBuildHeight();
        if (cposy < minY || cposy + HEIGHT > maxY) {
            return false;
        }

        // 1) Carve stone/ore underground first
        StructureHelper.clearBox(level, cposx, cposy, cposz, WIDTH, HEIGHT, WIDTH);

        // Floor: lapis block (gold field_150341_Y)
        BlockState lapis = Blocks.LAPIS_BLOCK.defaultBlockState();
        for (int i = 0; i < WIDTH; i++) {
            for (int k = 0; k < WIDTH; k++) {
                StructureHelper.set(level, cposx + i, cposy, cposz + k, lapis);
            }
        }

        // Ceiling + walls via setThisBlock mix
        int roofY = cposy + HEIGHT - 1;
        for (int i = 0; i < WIDTH; i++) {
            for (int k = 0; k < WIDTH; k++) {
                setThisBlock(level, random, cposx + i, roofY, cposz + k);
            }
        }

        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                setThisBlock(level, random, cposx + i, cposy + j, cposz);
                setThisBlock(level, random, cposx + i, cposy + j, cposz + WIDTH - 1);
            }
        }

        for (int k = 0; k < WIDTH; k++) {
            for (int j = 0; j < HEIGHT; j++) {
                setThisBlock(level, random, cposx, cposy + j, cposz + k);
                setThisBlock(level, random, cposx + WIDTH - 1, cposy + j, cposz + k);
            }
        }

        // 2) Re-hollow interior after shell
        StructureHelper.hollowInterior(level, cposx, cposy, cposz, WIDTH, HEIGHT, WIDTH);

        int midX = cposx + WIDTH / 2;
        int midZ = cposz + WIDTH / 2;
        int interiorY = cposy + 1;

        // Center spawner — gold: "Ruby Bird"
        BlockPos spawnerPos = new BlockPos(midX, interiorY, midZ);
        StructureHelper.set(level, spawnerPos, Blocks.SPAWNER.defaultBlockState());
        if (level.getBlockEntity(spawnerPos) instanceof SpawnerBlockEntity spawner) {
            spawner.setEntityId(ModEntities.RUBY_BIRD.get(), random);
        }

        // Chest against +Z wall edge (gold: z = cposz + 1)
        BlockPos chestPos = new BlockPos(midX, interiorY, cposz + 1);
        BlockState chestState = Blocks.CHEST.defaultBlockState().setValue(ChestBlock.FACING, Direction.SOUTH);
        StructureHelper.set(level, chestPos, chestState);
        RandomizableContainer.setBlockEntityLootTable(level, random, chestPos, LOOT_TABLE);

        return true;
    }

    /**
     * Gold setThisBlock: 1/20 ruby ore, else 50/50 lapis block / cobblestone.
     */
    private static void setThisBlock(WorldGenLevel level, RandomSource random, int x, int y, int z) {
        BlockState state;
        if (random.nextInt(20) == 0) {
            state = ModBlocks.RUBY_ORE.get().defaultBlockState();
        } else if (random.nextBoolean()) {
            state = Blocks.LAPIS_BLOCK.defaultBlockState();
        } else {
            state = Blocks.COBBLESTONE.defaultBlockState();
        }
        StructureHelper.set(level, x, y, z, state);
    }
}
