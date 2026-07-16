package danger.orespawn.world.structures;

import danger.orespawn.init.ModEntities;
import danger.orespawn.util.Reference;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootTable;

/**
 * Gold {@code GenericDungeon.makeSpiderHangout}: 20×20 stone foundation + gravel pad,
 * air cleared above, corner Spider Driver spawners on j=1.3, Robot Spider at center.
 * <p>
 * Port adds cobweb clutter in the air volume (user request: cobweb-heavy hangout).
 * Robot Spider is a center spawner (not a live entity) for finite structure placement.
 * <p>
 * Gold {@code DungeonSpawnerBlock} type <b>48</b>. Origin is the SW corner of the pad.
 */
public final class SpiderHangout {
    /** Gold loops {@code i/k = 0.19}. */
    public static final int WIDTH = 20;

    /**
     * Gold {@code j = -1 . 19}: foundation at -1, pad at 0, air 1.19.
     * Clear height from foundation through top air = 21.
     */
    public static final int HEIGHT = 21;

    /** Gold air column height above the pad. */
    public static final int AIR_HEIGHT = 19;

    public static final ResourceKey<LootTable> LOOT_TABLE = ResourceKey.create(
            Registries.LOOT_TABLE,
            ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "chests/spider_hangout"));

    private SpiderHangout() {}

    /**
     * Places a spider hangout with corner at {@code (cposx, cposy, cposz)} (pad Y).
     * Structure extends one block below origin for the stone foundation.
     *
     * @return true if written
     */
    public static boolean makeSpiderHangout(
            WorldGenLevel level, RandomSource random, int cposx, int cposy, int cposz) {
        int minY = level.getMinBuildHeight();
        int maxY = level.getMaxBuildHeight();
        int foundationY = cposy - 1;
        if (foundationY < minY || cposy + AIR_HEIGHT >= maxY) {
            return false;
        }

        // Clear full volume first (foundation through air column)
        StructureHelper.clearBox(level, cposx, foundationY, cposz, WIDTH, HEIGHT, WIDTH);

        BlockState stone = Blocks.STONE.defaultBlockState();
        BlockState gravel = Blocks.GRAVEL.defaultBlockState();
        BlockState cobweb = Blocks.COBWEB.defaultBlockState();
        BlockState air = Blocks.AIR.defaultBlockState();

        // Gold: j=-1 stone, j=0 gravel, j>0 air (then we add cobwebs)
        for (int i = 0; i < WIDTH; i++) {
            for (int k = 0; k < WIDTH; k++) {
                StructureHelper.set(level, cposx + i, foundationY, cposz + k, stone);
                StructureHelper.set(level, cposx + i, cposy, cposz + k, gravel);
            }
        }

        // Cobweb-heavy volume above the pad (finite density; leave walk corridor chance)
        for (int j = 1; j <= AIR_HEIGHT; j++) {
            for (int i = 0; i < WIDTH; i++) {
                for (int k = 0; k < WIDTH; k++) {
                    // denser near top / edges like a hangout web
                    boolean edge = i == 0 || k == 0 || i == WIDTH - 1 || k == WIDTH - 1;
                    int chance = edge ? 2 : (j >= 8 ? 3 : 6);
                    if (random.nextInt(chance) == 0) {
                        StructureHelper.set(level, cposx + i, cposy + j, cposz + k, cobweb);
                    } else {
                        StructureHelper.set(level, cposx + i, cposy + j, cposz + k, air);
                    }
                }
            }
        }

        // Gold: Spider Driver spawners at four corners, j = 1.3
        for (int j = 1; j < 4; j++) {
            placeSpawner(level, random, cposx, cposy + j, cposz, spiderDriver());
            placeSpawner(level, random, cposx + WIDTH - 1, cposy + j, cposz + WIDTH - 1, spiderDriver());
            placeSpawner(level, random, cposx + WIDTH - 1, cposy + j, cposz, spiderDriver());
            placeSpawner(level, random, cposx, cposy + j, cposz + WIDTH - 1, spiderDriver());
        }

        // Gold: live "Robot Spider" at center — port as spawner (finite, no entity spawn)
        placeSpawner(
                level,
                random,
                cposx + WIDTH / 2,
                cposy + 1,
                cposz + WIDTH / 2,
                spiderRobot());

        // Vanilla cave spiders sprinkled under webs
        placeSpawner(level, random, cposx + 5, cposy + 1, cposz + 5, EntityType.CAVE_SPIDER);
        placeSpawner(level, random, cposx + 14, cposy + 1, cposz + 14, EntityType.SPIDER);

        return true;
    }

    private static EntityType<?> spiderDriver() {
        return ModEntities.SPIDER_DRIVER != null
                ? ModEntities.SPIDER_DRIVER.get()
                : EntityType.SPIDER;
    }

    private static EntityType<?> spiderRobot() {
        return ModEntities.SPIDER_ROBOT != null
                ? ModEntities.SPIDER_ROBOT.get()
                : EntityType.CAVE_SPIDER;
    }

    private static void placeSpawner(
            WorldGenLevel level, RandomSource random, int x, int y, int z, EntityType<?> type) {
        BlockPos pos = new BlockPos(x, y, z);
        StructureHelper.set(level, pos, Blocks.SPAWNER.defaultBlockState());
        if (level.getBlockEntity(pos) instanceof SpawnerBlockEntity spawner) {
            spawner.setEntityId(type, random);
        }
    }
}
