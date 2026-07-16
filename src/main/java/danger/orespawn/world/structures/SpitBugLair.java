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
 * Gold {@code GenericDungeon.makeSpitBugLair}: lime/green terracotta diamond pad,
 * lapis A-frame ridge with emerald-ore spire, triple Spit Bug spawners, center chest.
 * <p>
 * Gold {@code DungeonSpawnerBlock} type <b>19</b>. Origin is the center of the pad.
 */
public final class SpitBugLair {
    /** Gold {@code width = 9}. */
    public static final int WIDTH = 9;

    /** Spire top at {@code y + width + 3}. */
    public static final int HEIGHT = WIDTH + 4;

    public static final ResourceKey<LootTable> LOOT_TABLE = ResourceKey.create(
            Registries.LOOT_TABLE,
            ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "chests/spit_bug_lair"));

    private SpitBugLair() {}

    /**
     * Places the spit bug lair centered on {@code (cposx, cposy, cposz)}.
     *
     * @return true if written
     */
    public static boolean makeSpitBugLair(
            WorldGenLevel level, RandomSource random, int cposx, int cposy, int cposz) {
        int minY = level.getMinBuildHeight();
        int maxY = level.getMaxBuildHeight();
        if (cposy < minY || cposy + HEIGHT >= maxY) {
            return false;
        }

        int size = WIDTH * 2 + 1;
        // Clear full AABB (diamond pad + ridge + spire) so underground stone is gone
        StructureHelper.clearBox(
                level, cposx - WIDTH, cposy, cposz - WIDTH, size, HEIGHT, size);

        // gold stained clay meta 5 = lime, meta 13 = green
        BlockState lime = Blocks.LIME_TERRACOTTA.defaultBlockState();
        BlockState green = Blocks.GREEN_TERRACOTTA.defaultBlockState();
        BlockState lapis = Blocks.LAPIS_BLOCK.defaultBlockState();
        // gold field_150412_bA = emerald_ore (used as decorative spire)
        BlockState emeraldOre = Blocks.EMERALD_ORE.defaultBlockState();
        BlockState wall = Blocks.COBBLESTONE_WALL.defaultBlockState();
        BlockState air = Blocks.AIR.defaultBlockState();

        // Gold: A-frame ridge along X at z = origin (i = 0.width)
        for (int i = 0; i < WIDTH; i++) {
            int ridgeY = cposy + WIDTH - i;
            StructureHelper.set(level, cposx + i, ridgeY + 2, cposz, green);
            StructureHelper.set(level, cposx + i, ridgeY + 1, cposz, green);
            StructureHelper.set(level, cposx + i, ridgeY, cposz, lapis);
            StructureHelper.set(level, cposx - i, ridgeY + 2, cposz, green);
            StructureHelper.set(level, cposx - i, ridgeY + 1, cposz, green);
            StructureHelper.set(level, cposx - i, ridgeY, cposz, lapis);
        }

        // Gold: emerald-ore spire
        StructureHelper.set(level, cposx, cposy + WIDTH + 3, cposz, emeraldOre);
        StructureHelper.set(level, cposx, cposy + WIDTH + 2, cposz, emeraldOre);
        StructureHelper.set(level, cposx, cposy + WIDTH + 1, cposz, emeraldOre);

        // Gold: triple Spit Bug spawners under the spire
        placeSpawner(level, random, cposx, cposy + WIDTH, cposz);
        placeSpawner(level, random, cposx, cposy + WIDTH - 1, cposz);
        placeSpawner(level, random, cposx, cposy + WIDTH - 2, cposz);

        // Gold: diamond-shaped pad (var15 = 0.width-1)
        for (int var15 = 0; var15 < WIDTH; var15++) {
            for (int j = -var15; j <= var15; j++) {
                int westX = cposx - WIDTH + var15 + 1;
                int eastX = cposx + WIDTH - var15 - 1;
                StructureHelper.set(level, westX, cposy, cposz + j, lime);
                StructureHelper.set(level, eastX, cposy, cposz + j, lime);
                if (j != -var15 && j != var15) {
                    StructureHelper.set(level, westX, cposy + 1, cposz + j, air);
                    StructureHelper.set(level, eastX, cposy + 1, cposz + j, air);
                    StructureHelper.set(level, westX, cposy + 2, cposz + j, air);
                    StructureHelper.set(level, eastX, cposy + 2, cposz + j, air);
                } else {
                    StructureHelper.set(level, westX, cposy + 1, cposz + j, green);
                    StructureHelper.set(level, eastX, cposy + 1, cposz + j, green);
                    StructureHelper.set(level, westX, cposy + 2, cposz + j, wall);
                    StructureHelper.set(level, eastX, cposy + 2, cposz + j, wall);
                }
            }
        }

        // Gold: center chest at y+1
        BlockPos chestPos = new BlockPos(cposx, cposy + 1, cposz);
        StructureHelper.set(
                level,
                chestPos,
                Blocks.CHEST.defaultBlockState().setValue(ChestBlock.FACING, Direction.SOUTH));
        RandomizableContainer.setBlockEntityLootTable(level, random, chestPos, LOOT_TABLE);

        return true;
    }

    private static void placeSpawner(WorldGenLevel level, RandomSource random, int x, int y, int z) {
        BlockPos pos = new BlockPos(x, y, z);
        StructureHelper.set(level, pos, Blocks.SPAWNER.defaultBlockState());
        if (level.getBlockEntity(pos) instanceof SpawnerBlockEntity spawner) {
            EntityType<?> type =
                    ModEntities.SPIT_BUG != null ? ModEntities.SPIT_BUG.get() : EntityType.SPIDER;
            spawner.setEntityId(type, random);
        }
    }
}
