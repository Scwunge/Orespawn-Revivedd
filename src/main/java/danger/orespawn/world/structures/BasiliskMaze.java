package danger.orespawn.world.structures;

import danger.orespawn.entity.Basilisk;
import danger.orespawn.init.ModBlocks;
import danger.orespawn.init.ModEntities;
import danger.orespawn.util.Reference;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.RandomizableContainer;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootTable;

/**
 * Gold {@code BasiliskMaze}: digs a deep shaft entrance, builds a 10×10-cell maze (obsidian
 * walls), castle floor with lava / teleport pads / bedrock shell, 2–4 rich chests, and three
 * persistent Basilisks.
 * <p>
 * Gold {@code DungeonSpawnerBlock} type <b>23</b> calls {@code BMaze.buildBasiliskMaze}.
 */
public final class BasiliskMaze {
    public static final int WTOP = 1;
    public static final int WRGT = 2;
    public static final int WBOT = 4;
    public static final int WLFT = 8;

    /** Maze grid cells (gold makeMaze 10×10). */
    public static final int GRID = 10;
    /** Cell size in blocks (gold csz=3). */
    public static final int CELL_SIZE = 3;
    /** Maze / castle Z footprint. */
    public static final int CASTLE_Z = 30;
    /** Castle X footprint (maze + boss chamber). */
    public static final int CASTLE_X = 60;
    /** Clear/ceiling height over first half of castle. */
    public static final int INNER_HEIGHT = 5;
    /** Clear/ceiling height over boss half. */
    public static final int BOSS_HEIGHT = 7;
    /** Sandstone pyramid entrance width (gold width=8). */
    public static final int ENTRANCE_WIDTH = 8;
    /** Minimum dig depth (gold 20 + nextInt(10)). */
    public static final int MIN_DEPTH = 20;
    public static final int DEPTH_RANGE = 10;

    /**
     * Approximate vertical span from entrance top (y+8) down to floor (y-depth-4),
     * for worldgen height clamping. Worst-case depth = 29.
     */
    public static final int MAX_VERTICAL_SPAN = ENTRANCE_WIDTH + MIN_DEPTH + DEPTH_RANGE - 1 + 4 + BOSS_HEIGHT;

    public static final ResourceKey<LootTable> LOOT_TABLE = ResourceKey.create(
            Registries.LOOT_TABLE,
            ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "chests/basilisk_maze"));

    private record MazePoint(int x, int y) {}

    private BasiliskMaze() {}

    /**
     * Gold {@code buildBasiliskMaze(world, x, y, z)} — entrance origin at surface
     * {@code (x,y,z)}; maze/castle carved below.
     *
     * @return true if the structure was written (Y within build limits)
     */
    public static boolean buildBasiliskMaze(WorldGenLevel level, RandomSource random, int x, int y, int z) {
        int depth = MIN_DEPTH + random.nextInt(DEPTH_RANGE);
        int floorY = y - depth - 4;
        int mazeY = y - depth - 3;
        int minY = level.getMinBuildHeight();
        int maxY = level.getMaxBuildHeight();

        // Floor / maze / entrance top must fit
        if (floorY < minY + 1) {
            return false;
        }
        if (y + ENTRANCE_WIDTH >= maxY) {
            return false;
        }
        if (floorY + BOSS_HEIGHT >= maxY) {
            return false;
        }

        int mazeX = x + 3;
        int mazeZ = z - 20;

        clearArea(level, mazeX, floorY, mazeZ);
        makeMaze(level, random, mazeX, mazeY, mazeZ, GRID, GRID, CELL_SIZE, 0);
        openMaze(level, mazeX, mazeY, mazeZ, GRID, GRID, CELL_SIZE);
        buildCastle(level, random, mazeX, floorY, mazeZ);
        makeEntrance(level, x, y, z, depth);
        return true;
    }

    // ——— Maze generation (gold Prim-style frontier) ———

    private static void makeMaze(
            WorldGenLevel level,
            RandomSource random,
            int xx,
            int yy,
            int zz,
            int xw,
            int zw,
            int csz,
            int b) {
        int gridw = xw;
        int gridh = zw;
        int cellsize = Math.max(3, csz);

        int[][] cells = new int[gridw][gridh];
        int full = 15;

        for (int cx = 0; cx < gridw; cx++) {
            for (int cy = 0; cy < gridh; cy++) {
                cells[cx][cy] = full;
            }
        }

        int left = 128;
        int right = 32;
        for (int cy = 0; cy < gridh; cy++) {
            cells[0][cy] |= left;
            cells[gridw - 1][cy] |= right;
        }

        int top = 16;
        int bottom = 64;
        for (int cx = 0; cx < gridw; cx++) {
            cells[cx][0] |= top;
            cells[cx][gridh - 1] |= bottom;
        }

        List<MazePoint> outlist = new ArrayList<>(gridw * gridh);
        List<MazePoint> inlist = new ArrayList<>();
        List<MazePoint> frontlist = new ArrayList<>();

        for (int cx = 0; cx < gridw; cx++) {
            for (int cy = 0; cy < gridh; cy++) {
                outlist.add(new MazePoint(cx, cy));
            }
        }

        MazePoint current = rndElement(random, outlist);
        inlist.add(current);
        moveNbrs(current, cells, outlist, frontlist);

        while (!frontlist.isEmpty()) {
            current = rndElement(random, frontlist);
            inlist.add(current);
            moveNbrs(current, cells, outlist, frontlist);
            int dir = findInNbr(random, current, cells, inlist);
            removeWall(current, dir, cells);
        }

        for (int cx = 0; cx < gridw; cx++) {
            for (int cy = 0; cy < gridh; cy++) {
                int val = cells[cx][cy];
                if ((val & 1) != 0) {
                    drawSide(
                            level,
                            cx * cellsize,
                            cy * cellsize,
                            (cx + 1) * cellsize,
                            cy * cellsize,
                            xx,
                            yy,
                            zz,
                            cellsize,
                            gridh,
                            gridw,
                            b);
                }
                if ((val & 2) != 0) {
                    drawSide(
                            level,
                            (cx + 1) * cellsize - 1,
                            cy * cellsize,
                            (cx + 1) * cellsize - 1,
                            (cy + 1) * cellsize,
                            xx,
                            yy,
                            zz,
                            cellsize,
                            gridh,
                            gridw,
                            b);
                }
                if ((val & 4) != 0) {
                    drawSide(
                            level,
                            cx * cellsize,
                            (cy + 1) * cellsize - 1,
                            (cx + 1) * cellsize,
                            (cy + 1) * cellsize - 1,
                            xx,
                            yy,
                            zz,
                            cellsize,
                            gridh,
                            gridw,
                            b);
                }
                if ((val & 8) != 0) {
                    drawSide(
                            level,
                            cx * cellsize,
                            cy * cellsize,
                            cx * cellsize,
                            (cy + 1) * cellsize,
                            xx,
                            yy,
                            zz,
                            cellsize,
                            gridh,
                            gridw,
                            b);
                }
            }
        }
    }

    private static void drawSide(
            WorldGenLevel level,
            int fromx,
            int fromz,
            int tox,
            int toz,
            int x,
            int y,
            int z,
            int cellsize,
            int gridh,
            int gridw,
            int bb) {
        // gold field_150343_Z = obsidian; bb!=0 → bedrock (field_150357_h)
        BlockState blk = bb != 0 ? Blocks.BEDROCK.defaultBlockState() : Blocks.OBSIDIAN.defaultBlockState();

        if (fromx > tox) {
            int t = fromx;
            fromx = tox;
            tox = t;
        }
        if (fromz > toz) {
            int t = fromz;
            fromz = toz;
            toz = t;
        }

        if (fromx == tox) {
            int i = fromx;
            for (int j = fromz; j <= toz; j++) {
                if (j < cellsize * gridh) {
                    setFast(level, i + x, y, j + z, blk);
                    setFast(level, i + x, y + 1, j + z, blk);
                    setFast(level, i + x, y + 2, j + z, blk);
                }
            }
        } else {
            int j = fromz;
            for (int i = fromx; i <= tox; i++) {
                if (i < cellsize * gridw) {
                    setFast(level, i + x, y, j + z, blk);
                    setFast(level, i + x, y + 1, j + z, blk);
                    setFast(level, i + x, y + 2, j + z, blk);
                }
            }
        }
    }

    private static int findInNbr(RandomSource random, MazePoint p, int[][] cells, List<MazePoint> inlist) {
        int d = rnd(random, 4) - 1;
        for (int k = 0; k < 4; k++) {
            switch (d) {
                case 0 -> {
                    if ((cells[p.x][p.y] & 16) == 0 && inlist.indexOf(new MazePoint(p.x, p.y - 1)) >= 0) {
                        return 1;
                    }
                }
                case 1 -> {
                    if ((cells[p.x][p.y] & 32) == 0 && inlist.indexOf(new MazePoint(p.x + 1, p.y)) >= 0) {
                        return 2;
                    }
                }
                case 2 -> {
                    if ((cells[p.x][p.y] & 64) == 0 && inlist.indexOf(new MazePoint(p.x, p.y + 1)) >= 0) {
                        return 4;
                    }
                }
                case 3 -> {
                    if ((cells[p.x][p.y] & 128) == 0 && inlist.indexOf(new MazePoint(p.x - 1, p.y)) >= 0) {
                        return 8;
                    }
                }
                default -> {}
            }
            d = (d + 1) % 4;
        }
        return 0;
    }

    private static void moveNbrs(MazePoint p, int[][] cells, List<MazePoint> outlist, List<MazePoint> frontlist) {
        if ((cells[p.x][p.y] & 16) == 0) {
            movePoint(new MazePoint(p.x, p.y - 1), outlist, frontlist);
        }
        if ((cells[p.x][p.y] & 32) == 0) {
            movePoint(new MazePoint(p.x + 1, p.y), outlist, frontlist);
        }
        if ((cells[p.x][p.y] & 64) == 0) {
            movePoint(new MazePoint(p.x, p.y + 1), outlist, frontlist);
        }
        if ((cells[p.x][p.y] & 128) == 0) {
            movePoint(new MazePoint(p.x - 1, p.y), outlist, frontlist);
        }
    }

    private static void movePoint(MazePoint p, List<MazePoint> v, List<MazePoint> w) {
        int i = v.indexOf(p);
        if (i >= 0) {
            v.remove(i);
            w.add(p);
        }
    }

    private static void removeWall(MazePoint p, int d, int[][] cells) {
        cells[p.x][p.y] = cells[p.x][p.y] ^ d;
        switch (d) {
            case 1 -> cells[p.x][p.y - 1] = cells[p.x][p.y - 1] ^ 4;
            case 2 -> cells[p.x + 1][p.y] = cells[p.x + 1][p.y] ^ 8;
            case 4 -> cells[p.x][p.y + 1] = cells[p.x][p.y + 1] ^ 1;
            case 8 -> cells[p.x - 1][p.y] = cells[p.x - 1][p.y] ^ 2;
            default -> {}
        }
    }

    /** Gold {@code rnd}: 1.n inclusive. */
    private static int rnd(RandomSource random, int n) {
        return random.nextInt(n) + 1;
    }

    private static MazePoint rndElement(RandomSource random, List<MazePoint> v) {
        int i = rnd(random, v.size()) - 1;
        return v.remove(i);
    }

    // ——— Area carve / openings / castle ———

    private static void clearArea(WorldGenLevel level, int x, int y, int z) {
        BlockState air = Blocks.AIR.defaultBlockState();
        for (int i = 0; i < 60; i++) {
            int hi = i >= 30 ? 7 : 5;
            for (int j = 0; j < hi; j++) {
                for (int k = 0; k < 30; k++) {
                    setFast(level, x + i, y + j, z + k, air);
                }
            }
        }
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 6; j++) {
                for (int k = 0; k < 30; k++) {
                    setFast(level, x - i, y + j, z + k, air);
                }
            }
        }
    }

    private static void openMaze(WorldGenLevel level, int xx, int yy, int zz, int xw, int zw, int csz) {
        BlockState air = Blocks.AIR.defaultBlockState();
        for (int i = 0; i < zw * csz; i++) {
            if (level.getBlockState(new BlockPos(xx + 1, yy, zz + i)).isAir()) {
                setFast(level, xx, yy, zz + i, air);
                setFast(level, xx, yy + 1, zz + i, air);
                setFast(level, xx, yy + 2, zz + i, air);
                break;
            }
        }
        for (int i = zw * csz - 1; i >= 0; i--) {
            if (level.getBlockState(new BlockPos(xx + xw * csz - 2, yy, zz + i)).isAir()) {
                setFast(level, xx + xw * csz - 1, yy, zz + i, air);
                setFast(level, xx + xw * csz - 1, yy + 1, zz + i, air);
                setFast(level, xx + xw * csz - 1, yy + 2, zz + i, air);
                break;
            }
        }
    }

    private static void buildCastle(WorldGenLevel level, RandomSource random, int x, int y, int z) {
        BlockState obsidian = Blocks.OBSIDIAN.defaultBlockState();
        BlockState bedrock = Blocks.BEDROCK.defaultBlockState();
        BlockState lava = Blocks.LAVA.defaultBlockState();
        BlockState rtp = ModBlocks.BLOCK_TELEPORT.get().defaultBlockState();
        BlockState sandstone = Blocks.SANDSTONE.defaultBlockState();
        BlockState ironOre = Blocks.IRON_ORE.defaultBlockState();
        BlockState torch = ModBlocks.EXTREME_TORCH.get().defaultBlockState();
        BlockState glowstone = Blocks.GLOWSTONE.defaultBlockState();
        BlockState vanillaTorch = Blocks.TORCH.defaultBlockState();

        // Floor: solid obsidian 60×30
        for (int i = 0; i < 60; i++) {
            for (int k = 0; k < 30; k++) {
                setFast(level, x + i, y, z + k, obsidian);
            }
        }

        // 80 random lava floor patches
        for (int i = 0; i < 80; i++) {
            setFast(level, x + random.nextInt(28) + 1, y, z + random.nextInt(28) + 1, lava);
        }

        // 20 RTP pads on boss half (gold MyRTPBlock)
        for (int i = 0; i < 20; i++) {
            setFast(level, x + 30 + random.nextInt(28) + 1, y, z + random.nextInt(28) + 1, rtp);
        }

        // Bedrock ceiling over maze half (y+4)
        for (int i = 0; i < 30; i++) {
            for (int k = 0; k < 30; k++) {
                setFast(level, x + i, y + 4, z + k, bedrock);
            }
        }

        // Bedrock ceiling over boss half (y+6)
        for (int i = 0; i < 30; i++) {
            for (int k = 0; k < 30; k++) {
                setFast(level, x + i + 30, y + 6, z + k, bedrock);
            }
        }

        // +X end wall: obsidian + double bedrock
        for (int i = 0; i < 30; i++) {
            for (int k = 0; k < 5; k++) {
                setFast(level, x + 59, y + k + 1, z + i, obsidian);
                setFast(level, x + 60, y + k + 1, z + i, bedrock);
                setFast(level, x + 61, y + k + 1, z + i, bedrock);
            }
        }

        // Boss -Z wall
        for (int i = 0; i < 30; i++) {
            for (int k = 0; k < 5; k++) {
                setFast(level, x + 30 + i, y + k + 1, z, obsidian);
                setFast(level, x + 30 + i, y + k + 1, z - 1, bedrock);
                setFast(level, x + 30 + i, y + k + 1, z - 2, bedrock);
            }
        }

        // Boss +Z wall
        for (int i = 0; i < 30; i++) {
            for (int k = 0; k < 5; k++) {
                setFast(level, x + 30 + i, y + k + 1, z + 29, obsidian);
                setFast(level, x + 30 + i, y + k + 1, z + 30, bedrock);
                setFast(level, x + 30 + i, y + k + 1, z + 31, bedrock);
            }
        }

        // Divider ledge at x+30, y+5
        for (int i = 0; i < 30; i++) {
            setFast(level, x + 30, y + 5, z + i, obsidian);
        }

        // Sandstone foyer floor strip
        for (int i = 0; i < 30; i++) {
            for (int k = 0; k < 4; k++) {
                setFast(level, x - 4 + k, y, z + i, sandstone);
            }
        }

        // Sandstone foyer ceiling
        for (int i = 0; i < 30; i++) {
            for (int k = 0; k < 4; k++) {
                setFast(level, x - 4 + k, y + 5, z + i, obsidian);
            }
        }

        // Iron-ore foyer shell
        for (int i = 0; i < 30; i++) {
            for (int k = 1; k < 5; k++) {
                setFast(level, x - 5, y + k, z + i, ironOre);
            }
        }
        for (int i = 0; i < 5; i++) {
            for (int k = 1; k < 5; k++) {
                setFast(level, x - 4 + i, y + k, z - 1, ironOre);
                setFast(level, x - 4 + i, y + k, z + 30, ironOre);
            }
        }

        // Sandstone pillars
        for (int k = 0; k < 4; k++) {
            setFast(level, x - 4, y + 1 + k, z, sandstone);
            setFast(level, x - 4, y + 1 + k, z + 15, sandstone);
            setFast(level, x - 4, y + 1 + k, z + 29, sandstone);
        }

        // Extreme torches
        setFast(level, x - 3, y + 3, z, torch);
        setFast(level, x - 3, y + 3, z + 15, torch);
        setFast(level, x - 3, y + 3, z + 29, torch);

        // Glowstone markers at boss divider
        setFast(level, x + 30, y + 4, z + 2, glowstone);
        setFast(level, x + 30, y + 4, z + 15, glowstone);
        setFast(level, x + 30, y + 4, z + 27, glowstone);

        // 2–4 chests with torches (gold WeightedRandomChestContent → loot table)
        int chestCount = 2 + random.nextInt(3);
        for (int i = 0; i < chestCount; i++) {
            int cz = z + 2 + i * 2;
            setFast(level, x + 58, y + 4, cz, vanillaTorch);
            BlockPos chestPos = new BlockPos(x + 58, y + 1, cz);
            BlockState chestState = Blocks.CHEST.defaultBlockState().setValue(ChestBlock.FACING, Direction.WEST);
            StructureHelper.set(level, chestPos, chestState);
            RandomizableContainer.setBlockEntityLootTable(level, random, chestPos, LOOT_TABLE);
        }

        // Three persistent Basilisks in boss chamber (gold spawnCreature + setPersistenceRequired)
        spawnBasilisk(level, random, x + 45.0, y + 1.01, z + 15.0);
        spawnBasilisk(level, random, x + 46.0, y + 1.01, z + 15.0);
        spawnBasilisk(level, random, x + 47.0, y + 1.01, z + 15.0);
    }

    public static void makeEntrance(WorldGenLevel level, int x, int y, int z, int depth) {
        int width = ENTRANCE_WIDTH;
        BlockState sandstone = Blocks.SANDSTONE.defaultBlockState();
        BlockState bedrock = Blocks.BEDROCK.defaultBlockState();
        BlockState air = Blocks.AIR.defaultBlockState();
        BlockState obsidian = Blocks.OBSIDIAN.defaultBlockState();

        // Sandstone pyramid collar
        for (int j = width; j >= 0; j--) {
            for (int i = 0; i < j * 2 + 4; i++) {
                setFast(level, x + i - j, y + width - j, z - j, sandstone);
                setFast(level, x + i - j, y + width - j, z + j + 3, sandstone);
                setFast(level, x - j, y + width - j, z + i - j, sandstone);
                setFast(level, x + j + 3, y + width - j, z + i - j, sandstone);
            }
        }

        // Vertical bedrock shaft with spiral obsidian steps
        int k = 0;
        for (int yy = width; yy > -depth; yy--) {
            for (int i = 0; i < 4; i++) {
                setFast(level, x + i, y + yy, z, bedrock);
                setFast(level, x + i, y + yy, z + 3, bedrock);
                setFast(level, x, y + yy, z + i, bedrock);
                setFast(level, x + 3, y + yy, z + i, bedrock);
            }
            for (int l = 0; l < 2; l++) {
                for (int m = 0; m < 2; m++) {
                    setFast(level, x + 1 + l, y + yy, z + 1 + m, air);
                }
            }
            switch (k) {
                case 0 -> setFast(level, x + 1, y + yy, z + 1, obsidian);
                case 1 -> setFast(level, x + 2, y + yy, z + 1, obsidian);
                case 2 -> setFast(level, x + 2, y + yy, z + 2, obsidian);
                default -> setFast(level, x + 1, y + yy, z + 2, obsidian);
            }
            if (++k > 3) {
                k = 0;
            }
        }
    }

    private static void spawnBasilisk(WorldGenLevel level, RandomSource random, double x, double y, double z) {
        ServerLevel server = level.getLevel();
        Basilisk basilisk = ModEntities.BASILISK.get().create(server);
        if (basilisk == null) {
            return;
        }
        basilisk.moveTo(x, y, z, random.nextFloat() * 360.0F, 0.0F);
        basilisk.setPersistenceRequired();
        basilisk.finalizeSpawn(
                server, server.getCurrentDifficultyAt(basilisk.blockPosition()), MobSpawnType.STRUCTURE, null);
        level.addFreshEntity(basilisk);
    }

    private static void setFast(WorldGenLevel level, int x, int y, int z, BlockState state) {
        StructureHelper.set(level, x, y, z, state);
    }
}
