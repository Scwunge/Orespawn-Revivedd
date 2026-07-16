package danger.orespawn.world.structures;

import danger.orespawn.init.ModBlocks;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Gold {@code CrystalMaze}: 4×4-cell maze (cell size 4 → 16×16 footprint), bedrock walls
 * (gold {@code makeMaze(., b=1)}), air corridors 3 high, bedrock floor/ceiling with a few
 * {@link ModBlocks#CRYSTAL_STONE} accents.
 * <p>
 * Gold only invokes this from {@code ChunkProviderOreSpawn5} at {@code (chunkX*16, 25, chunkZ*16)}
 * in the Crystal dimension — <b>no</b> {@code DungeonSpawnerBlock} type id.
 * <p>
 * Footprint is deliberately chunk-sized and O(grid²) so generation cannot hang.
 */
public final class CrystalMaze {
    public static final int WTOP = 1;
    public static final int WRGT = 2;
    public static final int WBOT = 4;
    public static final int WLFT = 8;

    /** Maze grid cells (gold makeMaze 4×4). */
    public static final int GRID = 4;
    /** Cell size in blocks (gold csz=4). */
    public static final int CELL_SIZE = 4;
    /** 4×4 cells × size 4 = one chunk. */
    public static final int FOOTPRINT = GRID * CELL_SIZE;
    /** Gold ChunkProviderOreSpawn5 places the maze at this Y. */
    public static final int DEFAULT_Y = 25;
    /** Wall corridor height (y.y+2); floor y-1; ceiling y+3. */
    public static final int WALL_HEIGHT = 3;
    public static final int TOTAL_HEIGHT = 5; // floor through ceiling

    private record MazePoint(int x, int y) {}

    private CrystalMaze() {}

    /**
     * Gold {@code buildCrystalMaze(world, x, y, z, chunk)} — origin is typically chunk origin
     * {@code (chunkX*16, 25, chunkZ*16)}.
     *
     * @return true if the structure was written (Y within build limits)
     */
    public static boolean buildCrystalMaze(WorldGenLevel level, RandomSource random, int x, int y, int z) {
        int minY = level.getMinBuildHeight();
        int maxY = level.getMaxBuildHeight();
        // Needs y-1 (floor) through y+3 (ceiling)
        if (y - 1 < minY || y + 3 >= maxY) {
            return false;
        }

        clearVolume(level, x, y, z);
        makeMaze(level, random, x, y, z, GRID, GRID, CELL_SIZE, 1);
        openCrystalMaze(level, random, x, y, z, GRID, GRID, CELL_SIZE);
        return true;
    }

    /** Convenience overload matching gold's fixed crystal-dim Y. */
    public static boolean buildCrystalMaze(WorldGenLevel level, RandomSource random, int x, int z) {
        return buildCrystalMaze(level, random, x, DEFAULT_Y, z);
    }

    // ——— Clear / open (gold openCrystalMaze) ———

    /** Gold: air over 16×16 for k=0.2 at base y. */
    private static void clearVolume(WorldGenLevel level, int x, int y, int z) {
        BlockState air = Blocks.AIR.defaultBlockState();
        for (int i = 0; i < FOOTPRINT; i++) {
            for (int j = 0; j < FOOTPRINT; j++) {
                for (int k = 0; k < WALL_HEIGHT; k++) {
                    setFast(level, x + j, y + k, z + i, air);
                }
            }
        }
    }

    /**
     * Gold {@code openCrystalMaze}: strip all four outer walls to air (3 high), solid bedrock
     * floor at y-1 and ceiling at y+3, then 4 ceiling CrystalStone + 1 floor CrystalStone.
     */
    private static void openCrystalMaze(
            WorldGenLevel level, RandomSource random, int xx, int yy, int zz, int xw, int zw, int csz) {
        BlockState air = Blocks.AIR.defaultBlockState();
        BlockState bedrock = Blocks.BEDROCK.defaultBlockState();
        BlockState crystal = ModBlocks.CRYSTAL_STONE.get().defaultBlockState();

        int span = zw * csz; // gold uses zw*csz for all four edges (xw==zw==4, csz==4 → 16)
        for (int i = 0; i < span; i++) {
            // -X edge
            setFast(level, xx, yy, zz + i, air);
            setFast(level, xx, yy + 1, zz + i, air);
            setFast(level, xx, yy + 2, zz + i, air);
            // -Z edge
            setFast(level, xx + i, yy, zz, air);
            setFast(level, xx + i, yy + 1, zz, air);
            setFast(level, xx + i, yy + 2, zz, air);
            // +X edge
            setFast(level, xx + span - 1, yy, zz + i, air);
            setFast(level, xx + span - 1, yy + 1, zz + i, air);
            setFast(level, xx + span - 1, yy + 2, zz + i, air);
            // +Z edge
            setFast(level, xx + i, yy, zz + span - 1, air);
            setFast(level, xx + i, yy + 1, zz + span - 1, air);
            setFast(level, xx + i, yy + 2, zz + span - 1, air);
        }

        for (int zi = 0; zi < span; zi++) {
            for (int xi = 0; xi < span; xi++) {
                setFast(level, xx + xi, yy - 1, zz + zi, bedrock);
                setFast(level, xx + xi, yy + 3, zz + zi, bedrock);
            }
        }

        for (int k = 0; k < 4; k++) {
            int rx = random.nextInt(span);
            int rz = random.nextInt(span);
            setFast(level, xx + rx, yy + 3, zz + rz, crystal);
        }
        int fx = random.nextInt(span);
        int fz = random.nextInt(span);
        setFast(level, xx + fx, yy - 1, zz + fz, crystal);
    }

    // ——— Maze generation (gold Prim-style frontier; shared algorithm with BasiliskMaze) ———

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

        // Bounded: frontlist only receives each of the gridw*gridh cells once.
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
        // gold: field_150343_Z = obsidian; bb!=0 → bedrock (field_150357_h). CrystalMaze uses bb=1.
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

    private static void setFast(WorldGenLevel level, int x, int y, int z, BlockState state) {
        int minY = level.getMinBuildHeight();
        int maxY = level.getMaxBuildHeight();
        if (y < minY || y >= maxY) {
            return;
        }
        StructureHelper.set(level, x, y, z, state);
    }
}
