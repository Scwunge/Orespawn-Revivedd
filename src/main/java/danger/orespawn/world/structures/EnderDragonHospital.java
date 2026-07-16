package danger.orespawn.world.structures;

import danger.orespawn.init.ModBlocks;
import danger.orespawn.init.ModEntities;
import danger.orespawn.util.Reference;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.RandomizableContainer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.boss.enderdragon.EndCrystal;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootTable;

/**
 * Gold {@code GenericDungeon.makeEnderDragonHospital}: 10×10 iron-bar cage on end-stone floor,
 * stepped MyEyeOfEnderBlock roof rings, netherrack/end-stone approach ramp, four corner end
 * crystals on bedrock, Ender Reaper roof spawners + Nightmare floor spawners, center chest.
 * <p>
 * Gold {@code DungeonSpawnerBlock} type <b>24</b>. Origin is SW corner of the 10×10 pad.
 */
public final class EnderDragonHospital {
    /** Gold outer shell width/depth (i,k = 0.9). */
    public static final int WIDTH = 10;

    /**
     * Vertical span from floor (j=0) through roof peak / crystals (y+9) and ramp base.
     * Ramp starts at local x=-6,y+1 so total height from pad origin is 10; clear includes ramp.
     */
    public static final int HEIGHT = 10;

    /** Gold ramp extends 6 blocks west of origin. */
    public static final int RAMP_EXTENT = 6;

    public static final ResourceKey<LootTable> LOOT_TABLE = ResourceKey.create(
            Registries.LOOT_TABLE,
            ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "chests/ender_dragon_hospital"));

    private EnderDragonHospital() {}

    /**
     * Places the hospital with SW corner at {@code (cposx, cposy, cposz)}.
     *
     * @return true if written
     */
    public static boolean makeEnderDragonHospital(
            WorldGenLevel level, RandomSource random, int cposx, int cposy, int cposz) {
        int minY = level.getMinBuildHeight();
        int maxY = level.getMaxBuildHeight();
        if (cposy < minY || cposy + HEIGHT >= maxY) {
            return false;
        }

        // Clear pad + west ramp + roof stack
        StructureHelper.clearBox(
                level,
                cposx - RAMP_EXTENT,
                cposy,
                cposz,
                WIDTH + RAMP_EXTENT,
                HEIGHT + 1,
                WIDTH);

        BlockState air = Blocks.AIR.defaultBlockState();
        BlockState bars = Blocks.IRON_BARS.defaultBlockState();
        BlockState obsidian = Blocks.OBSIDIAN.defaultBlockState();
        BlockState endStone = Blocks.END_STONE.defaultBlockState();
        BlockState netherrack = Blocks.NETHERRACK.defaultBlockState();
        BlockState bedrock = Blocks.BEDROCK.defaultBlockState();
        // Gold MyEyeOfEnderBlock
        BlockState eye = ModBlocks.EYE_OF_ENDER_BLOCK.get().defaultBlockState();

        // Gold main shell j=0.6, i/k=0.9
        for (int i = 0; i < WIDTH; i++) {
            for (int k = 0; k < WIDTH; k++) {
                for (int j = 0; j < 7; j++) {
                    BlockState blk = air;
                    if (i == 0 || k == 0 || i == 9 || k == 9) {
                        blk = bars;
                    }
                    if ((i == 0 || i == 9) && (k == 0 || k == 9)) {
                        blk = obsidian;
                    }
                    if (j == 0) {
                        blk = endStone;
                    }
                    if (j == 6 && (i == 0 || k == 0 || i == 9 || k == 9)) {
                        blk = endStone;
                    }
                    StructureHelper.set(level, cposx + i, cposy + j, cposz + k, blk);
                }
            }
        }

        // Gold roof rings of MyEyeOfEnderBlock at j=7,8,9 (stepping inward)
        for (int i = 1; i < 9; i++) {
            for (int k = 1; k < 9; k++) {
                BlockState blk = air;
                if (i == 1 || i == 8 || k == 1 || k == 8) {
                    blk = eye;
                }
                StructureHelper.set(level, cposx + i, cposy + 7, cposz + k, blk);
            }
        }
        for (int i = 2; i < 8; i++) {
            for (int k = 2; k < 8; k++) {
                BlockState blk = air;
                if (i == 2 || i == 7 || k == 2 || k == 7) {
                    blk = eye;
                }
                StructureHelper.set(level, cposx + i, cposy + 8, cposz + k, blk);
            }
        }
        for (int i = 3; i < 7; i++) {
            for (int k = 3; k < 7; k++) {
                BlockState blk = air;
                if (i == 3 || i == 6 || k == 3 || k == 6) {
                    blk = eye;
                }
                StructureHelper.set(level, cposx + i, cposy + 9, cposz + k, blk);
            }
        }

        // Gold west approach ramp: x from -6, rising each step, 4-wide end-stone base + bars/netherrack
        int rx = -6;
        int ry = 1;
        int rz = 3;
        for (int m = 0; m < 6; m++) {
            StructureHelper.set(level, cposx + rx, cposy + ry, cposz + rz, endStone);
            StructureHelper.set(level, cposx + rx, cposy + ry, cposz + rz + 1, endStone);
            StructureHelper.set(level, cposx + rx, cposy + ry, cposz + rz + 2, endStone);
            StructureHelper.set(level, cposx + rx, cposy + ry, cposz + rz + 3, endStone);
            StructureHelper.set(level, cposx + rx, cposy + ry + 1, cposz + rz, bars);
            StructureHelper.set(level, cposx + rx, cposy + ry + 1, cposz + rz + 3, bars);
            StructureHelper.set(level, cposx + rx, cposy + ry + 2, cposz + rz, netherrack);
            StructureHelper.set(level, cposx + rx, cposy + ry + 2, cposz + rz + 3, netherrack);
            rx++;
            ry++;
        }

        // Gold corner obsidian posts at y+7 / y+8
        StructureHelper.set(level, cposx + 0, cposy + 7, cposz + 0, obsidian);
        StructureHelper.set(level, cposx + 0, cposy + 7, cposz + 9, obsidian);
        StructureHelper.set(level, cposx + 9, cposy + 7, cposz + 0, obsidian);
        StructureHelper.set(level, cposx + 9, cposy + 7, cposz + 9, obsidian);
        StructureHelper.set(level, cposx + 0, cposy + 8, cposz + 0, obsidian);
        StructureHelper.set(level, cposx + 0, cposy + 8, cposz + 9, obsidian);
        StructureHelper.set(level, cposx + 9, cposy + 8, cposz + 0, obsidian);
        StructureHelper.set(level, cposx + 9, cposy + 8, cposz + 9, obsidian);

        // Gold EntityEnderCrystal at corners + bedrock bases
        spawnEndCrystal(level, random, cposx + 0.5, cposy + 9, cposz + 0.5);
        StructureHelper.set(level, cposx, cposy + 9, cposz, bedrock);
        spawnEndCrystal(level, random, cposx + 0.5, cposy + 9, cposz + 9.5);
        StructureHelper.set(level, cposx, cposy + 9, cposz + 9, bedrock);
        spawnEndCrystal(level, random, cposx + 9.5, cposy + 9, cposz + 0.5);
        StructureHelper.set(level, cposx + 9, cposy + 9, cposz, bedrock);
        spawnEndCrystal(level, random, cposx + 9.5, cposy + 9, cposz + 9.5);
        StructureHelper.set(level, cposx + 9, cposy + 9, cposz + 9, bedrock);

        // Gold roof Ender Reaper spawners (y+9, positions 3/6 × 3/6)
        placeSpawner(level, random, cposx + 3, cposy + 9, cposz + 3, enderReaper());
        placeSpawner(level, random, cposx + 3, cposy + 9, cposz + 6, enderReaper());
        placeSpawner(level, random, cposx + 6, cposy + 9, cposz + 3, enderReaper());
        placeSpawner(level, random, cposx + 6, cposy + 9, cposz + 6, enderReaper());

        // Gold floor Nightmare spawners (corners of interior, y+1)
        placeSpawner(level, random, cposx + 1, cposy + 1, cposz + 1, nightmare());
        placeSpawner(level, random, cposx + 1, cposy + 1, cposz + 8, nightmare());
        placeSpawner(level, random, cposx + 8, cposy + 1, cposz + 1, nightmare());
        placeSpawner(level, random, cposx + 8, cposy + 1, cposz + 8, nightmare());

        placeChest(level, random, cposx + 4, cposy + 1, cposz + 4, Direction.SOUTH);

        return true;
    }

    /** Gold "Ender Reaper". */
    private static EntityType<?> enderReaper() {
        return ModEntities.ENDER_REAPER != null
                ? ModEntities.ENDER_REAPER.get()
                : EntityType.ENDERMAN;
    }

    /** Gold "Nightmare" → {@link ModEntities#PITCH_BLACK} (project convention). */
    private static EntityType<?> nightmare() {
        return ModEntities.PITCH_BLACK != null
                ? ModEntities.PITCH_BLACK.get()
                : EntityType.ENDERMAN;
    }

    private static void spawnEndCrystal(
            WorldGenLevel level, RandomSource random, double x, double y, double z) {
        ServerLevel server = level.getLevel();
        EndCrystal crystal = EntityType.END_CRYSTAL.create(server);
        if (crystal == null) {
            return;
        }
        crystal.moveTo(x, y, z, random.nextFloat() * 360.0F, 0.0F);
        crystal.setShowBottom(false);
        level.addFreshEntity(crystal);
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
