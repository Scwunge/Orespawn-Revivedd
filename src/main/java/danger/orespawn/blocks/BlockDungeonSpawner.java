package danger.orespawn.blocks;

import danger.orespawn.world.structures.AlienWTFDungeon;
import danger.orespawn.world.structures.BasiliskMaze;
import danger.orespawn.world.structures.BeeHive;
import danger.orespawn.world.structures.BouncyCastle;
import danger.orespawn.world.structures.CephadromeAltar;
import danger.orespawn.world.structures.CloudSharkDungeon;
import danger.orespawn.world.structures.CrystalBattleTower;
import danger.orespawn.world.structures.CrystalHauntedHouse;
import danger.orespawn.world.structures.DamselInDistress;
import danger.orespawn.world.structures.EnderCastle;
import danger.orespawn.world.structures.EnderDragonHospital;
import danger.orespawn.world.structures.EnderKnightDungeon;
import danger.orespawn.world.structures.EnderReaperGraveyard;
import danger.orespawn.world.structures.EnormousCastle;
import danger.orespawn.world.structures.EnormousCastleQ;
import danger.orespawn.world.structures.FairyCastleTree;
import danger.orespawn.world.structures.FairyTree;
import danger.orespawn.world.structures.FrogPond;
import danger.orespawn.world.structures.GenericDungeon;
import danger.orespawn.world.structures.GirlfriendIsland;
import danger.orespawn.world.structures.GoldFishBowl;
import danger.orespawn.world.structures.GreenhouseDungeon;
import danger.orespawn.world.structures.HauntedHouse;
import danger.orespawn.world.structures.Igloo;
import danger.orespawn.world.structures.IncaPyramid;
import danger.orespawn.world.structures.KingAltar;
import danger.orespawn.world.structures.KyuubiDungeon;
import danger.orespawn.world.structures.LeafMonsterDungeon;
import danger.orespawn.world.structures.LeonNest;
import danger.orespawn.world.structures.MantisHive;
import danger.orespawn.world.structures.MiniDungeon;
import danger.orespawn.world.structures.MonsterIsland;
import danger.orespawn.world.structures.NightmareDungeon;
import danger.orespawn.world.structures.PlayPool;
import danger.orespawn.world.structures.PumpkinHouse;
import danger.orespawn.world.structures.QueenAltar;
import danger.orespawn.world.structures.RainbowDungeon;
import danger.orespawn.world.structures.RedAntHangout;
import danger.orespawn.world.structures.RobotLab;
import danger.orespawn.world.structures.RotatorStation;
import danger.orespawn.world.structures.RoundRotator;
import danger.orespawn.world.structures.RubberDuckyPond;
import danger.orespawn.world.structures.RubyBirdDungeon;
import danger.orespawn.world.structures.ShadowDungeon;
import danger.orespawn.world.structures.SmallBeeHive;
import danger.orespawn.world.structures.SpiderHangout;
import danger.orespawn.world.structures.SpitBugLair;
import danger.orespawn.world.structures.StinkyHouse;
import danger.orespawn.world.structures.WaterDragonLair;
import danger.orespawn.world.structures.WhiteHouse;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.TickTask;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Gold {@code DungeonSpawnerBlock} (extends BlockReed, unlocalized {@code dungeonspawner}).
 * <p>
 * Not a mob spawner: after a short delay it picks {@code type = rand(50)} and runs one of
 * gold's 50 structure generators (FairyTree, GenericDungeon, KingAltar, …), then removes self.
 * <p>
 * Activation is multi-path so placement never silently sits forever:
 * <ul>
 *   <li>scheduled block tick</li>
 *   <li>server {@link TickTask} backup (survives lost schedule entries)</li>
 *   <li>random tick backup</li>
 *   <li>right-click force-generate</li>
 * </ul>
 * Ported types: 0 {@link FairyTree}, 21 {@link GenericDungeon}, 22 {@link RubyBirdDungeon},
 * 23 {@link BasiliskMaze}, 25 {@link CrystalHauntedHouse}, 33 {@link CrystalBattleTower},
 * 38 {@link NightmareDungeon}.
 */
public class BlockDungeonSpawner extends Block {
    private static final Logger LOGGER = LoggerFactory.getLogger(BlockDungeonSpawner.class);

    private static final VoxelShape SHAPE = Block.box(5.0, 0.0, 5.0, 11.0, 16.0, 11.0);

    /**
     * Gold schedule delay was 400 ticks (~20s). Port uses ~1s so placement feels responsive;
     * randomTick + TickTask + right-click still back it up.
     */
    private static final int PLACE_DELAY = 20;

    /** Gold type range: nextInt(50) → 0.49. */
    public static final int DUNGEON_TYPE_COUNT = 50;

    public BlockDungeonSpawner(BlockBehaviour.Properties properties) {
        super(properties);
    }

    public static BlockBehaviour.Properties defaultProps() {
        return BlockBehaviour.Properties.of()
                .mapColor(MapColor.COLOR_PURPLE)
                .noCollission()
                .randomTicks()
                .instabreak()
                .sound(SoundType.GRASS)
                .lightLevel(s -> 13) // gold setLightLevel(0.9F) ≈ 13/15
                .pushReaction(PushReaction.DESTROY);
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    protected boolean isRandomlyTicking(BlockState state) {
        return true;
    }

    /** Gold canPlace: solid below. */
    @Override
    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        BlockPos below = pos.below();
        return level.getBlockState(below).isFaceSturdy(level, below, Direction.UP);
    }

    @Override
    protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
        super.onPlace(state, level, pos, oldState, movedByPiston);
        if (level.isClientSide || !(level instanceof ServerLevel server)) {
            return;
        }
        // Primary: vanilla scheduled block tick (same pattern as KingSpawner)
        server.scheduleTick(pos, this, PLACE_DELAY);
        // Backup: server TickTask — scheduled block ticks can be dropped on chunk edge cases
        scheduleServerBackup(server, pos.immutable(), PLACE_DELAY);
    }

    /**
     * Runs generate after {@code delayTicks} game ticks even if the block schedule is lost.
     */
    private static void scheduleServerBackup(ServerLevel server, BlockPos pos, int delayTicks) {
        int when = server.getServer().getTickCount() + Math.max(1, delayTicks);
        server.getServer().tell(new TickTask(when, () -> {
            if (!server.hasChunkAt(pos)) {
                return;
            }
            if (!(server.getBlockState(pos).getBlock() instanceof BlockDungeonSpawner)) {
                return;
            }
            generateRandomDungeon(server, pos, server.getRandom());
        }));
    }

    @Override
    protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        generateRandomDungeon(level, pos, random);
    }

    /**
     * Backup if the scheduled tick never fires (chunk unload, edge cases).
     * Safe: generateRandomDungeon no-ops if this block is already gone.
     */
    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        generateRandomDungeon(level, pos, random);
    }

    /**
     * Right-click (empty hand or any item fallthrough): force generate immediately
     * so creative testing never depends only on scheduled ticks.
     */
    @Override
    protected InteractionResult useWithoutItem(
            BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        }
        if (level instanceof ServerLevel server) {
            generateRandomDungeon(server, pos, server.getRandom());
        }
        return InteractionResult.CONSUME;
    }

    /**
     * Gold {@code updateTick}: clear self + above, then type 0–49 structure dispatch.
     */
    public static void generateRandomDungeon(ServerLevel level, BlockPos pos, RandomSource random) {
        if (level.isClientSide) {
            return;
        }
        if (!(level.getBlockState(pos).getBlock() instanceof BlockDungeonSpawner)) {
            return;
        }

        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();

        // Remove seed (UPDATE_ALL so client sees it vanish)
        level.setBlock(pos, Blocks.AIR.defaultBlockState(), Block.UPDATE_ALL);
        level.setBlock(pos.above(), Blocks.AIR.defaultBlockState(), Block.UPDATE_ALL);

        int type = random.nextInt(DUNGEON_TYPE_COUNT);
        LOGGER.info("DungeonSpawner activating type {} ({}) at {},{},{}", type, goldTypeName(type), x, y, z);
        runDungeonType(level, random, x, y, z, type);
    }

    /**
     * Gold dispatch table (DungeonSpawnerBlock 0–49). Implemented where ported;
     * otherwise falls back to GenericDungeon so creative tests always get a room.
     * Room builders that use a corner origin are centered on the seed so the shell
     * appears around the player instead of only +X/+Z.
     */
    public static void runDungeonType(ServerLevel level, RandomSource random, int x, int y, int z, int type) {
        switch (type) {
            // 0 gold OreSpawnMain.OreSpawnTrees.FairyTree
            case 0 -> FairyTree.makeFairyTree(level, random, x, y, z);
            // 1 gold OreSpawnTrees.FairyCastleTree
            case 1 -> FairyCastleTree.makeFairyCastleTree(level, random, x, y, z);
            // 2 gold MyDungeon.makeEnormousCastle — shell min-corner
            case 2 -> EnormousCastle.makeEnormousCastle(
                    level, random, x - EnormousCastle.WIDTH / 2, y, z - EnormousCastle.WIDTH / 2);
            // 3 gold makeRotatorStation
            case 3 -> RotatorStation.makeRotatorStation(level, random, x, y, z);
            // 4 gold MyDungeon.makeBeeHive
            case 4 -> BeeHive.makeBeeHive(
                    level, random, x - BeeHive.WIDTH / 2, y, z - BeeHive.WIDTH / 2);
            // 5 gold MyDungeon.makeHauntedHouse
            case 5 -> HauntedHouse.makeHauntedHouse(level, random, x, y, z);
            // 6 gold MyDungeon.makeMantisHive — top-layer SW corner
            case 6 -> MantisHive.makeMantisHive(
                    level, random, x - MantisHive.WIDTH / 2, y, z - MantisHive.WIDTH / 2);
            // 7 gold MyDungeon.makeKyuubiDungeon — shaft SW corner
            case 7 -> KyuubiDungeon.makeKyuubiDungeon(
                    level, random, x - KyuubiDungeon.WIDTH / 2, y, z - KyuubiDungeon.WIDTH / 2);
            // 8 gold MyDungeon.makeSmallBeeHive
            case 8 -> SmallBeeHive.makeSmallBeeHive(
                    level, random, x - SmallBeeHive.WIDTH / 2, y, z - SmallBeeHive.WIDTH / 2);
            // 9 gold makeShadowDungeon
            case 9 -> ShadowDungeon.makeShadowDungeon(
                    level, random, x - ShadowDungeon.WIDTH / 2, y, z - ShadowDungeon.WIDTH / 2);
            // 10 gold MyDungeon.makeAlienWTFDungeon — surface seed (builder drops Y)
            case 10 -> AlienWTFDungeon.makeAlienWTFDungeon(level, random, x, y, z);
            // 11 gold MyDungeon.makeEnderKnightDungeon — west pad SW corner
            case 11 -> EnderKnightDungeon.makeEnderKnightDungeon(
                    level, random, x, y, z - EnderKnightDungeon.WIDTH_Z / 2);
            // 12 gold makePlayPool
            case 12 -> PlayPool.makePlayPool(level, random, x, y, z);
            // 13 gold makeWaterDragonLair
            case 13 -> WaterDragonLair.makeWaterDragonLair(level, random, x, y, z);
            // 14 gold MyDungeon.makeCloudSharkDungeon — pad center
            case 14 -> CloudSharkDungeon.makeCloudSharkDungeon(level, random, x, y, z);
            // 15 gold makeLeafMonsterDungeon
            case 15 -> LeafMonsterDungeon.makeLeafMonsterDungeon(level, random, x, y, z);
            // 18 gold MyDungeon.makeEnderReaperGraveyard — SW corner
            case 18 -> EnderReaperGraveyard.makeEnderReaperGraveyard(
                    level,
                    random,
                    x - EnderReaperGraveyard.WIDTH / 2,
                    y,
                    z - EnderReaperGraveyard.LENGTH / 2);
            // 16 gold MyDungeon.makeMiniDungeon
            case 16 -> MiniDungeon.makeDungeon(
                    level, random, x - MiniDungeon.WIDTH / 2, y, z - MiniDungeon.WIDTH / 2);
            // 17 gold makeGoldFishBowl
            case 17 -> GoldFishBowl.makeGoldFishBowl(level, random, x, y, z);
            // 19 gold makeSpitBugLair
            case 19 -> SpitBugLair.makeSpitBugLair(level, random, x, y, z);
            // 20 gold MyDungeon.makeIgloo (centered)
            case 20 -> Igloo.makeIgloo(level, random, x, y, z);
            // 21 gold MyDungeon.makeDungeon → GenericDungeon
            case 21 -> GenericDungeon.makeDungeon(
                    level, random, x - GenericDungeon.WIDTH / 2, y, z - GenericDungeon.WIDTH / 2);
            // 22 gold RubyDungeon.makeDungeon → RubyBirdDungeon
            case 22 -> RubyBirdDungeon.makeDungeon(
                    level, random, x - RubyBirdDungeon.WIDTH / 2, y, z - RubyBirdDungeon.WIDTH / 2);
            // 23 gold OreSpawnMain.BMaze.buildBasiliskMaze
            case 23 -> BasiliskMaze.buildBasiliskMaze(level, random, x, y, z);
            // 24 gold MyDungeon.makeEnderDragonHospital — pad SW corner
            case 24 -> EnderDragonHospital.makeEnderDragonHospital(
                    level,
                    random,
                    x - EnderDragonHospital.WIDTH / 2,
                    y,
                    z - EnderDragonHospital.WIDTH / 2);
            // 25 gold GenericDungeon.makeCrystalHauntedHouse
            case 25 -> CrystalHauntedHouse.makeCrystalHauntedHouse(level, random, x, y, z);
            // 26 gold MyDungeon.makeBouncyCastle — center
            case 26 -> BouncyCastle.makeBouncyCastle(level, random, x, y, z);
            // 27 gold MyDungeon.makeEnderCastle — shell min-corner
            case 27 -> EnderCastle.makeEnderCastle(
                    level, random, x - EnderCastle.WIDTH / 2, y, z - EnderCastle.WIDTH / 2);
            // 28 gold MyDungeon.makeDamselInDistress — center
            case 28 -> DamselInDistress.makeDamselInDistress(level, random, x, y, z);
            // 29 gold MyDungeon.makeIncaPyramid — base pad SW corner
            case 29 -> IncaPyramid.makeIncaPyramid(
                    level, random, x - IncaPyramid.BASE_WIDTH / 2, y, z - IncaPyramid.BASE_DEPTH / 2);
            // 30 gold MyDungeon.makeRobotLab — entry hall SW corner
            case 30 -> RobotLab.makeRobotLab(
                    level, random, x - RobotLab.WIDTH / 2, y, z - RobotLab.LENGTH / 2);
            // 31 gold MyDungeon.makeKingAltar
            case 31 -> KingAltar.makeKingAltar(
                    level, random, x - KingAltar.WIDTH / 2, y, z - KingAltar.WIDTH / 2);
            // 32 gold MyDungeon.makeLeonNest
            case 32 -> LeonNest.makeLeonNest(level, random, x, y, z);
            // 33 gold GenericDungeon.makeCrystalBattleTower
            case 33 -> CrystalBattleTower.makeCrystalBattleTower(level, random, x, y, z);
            // 34 gold MyDungeon.makeCephadromeAltar — center
            case 34 -> CephadromeAltar.makeCephadromeAltar(level, random, x, y, z);
            // 35 gold MyDungeon.makeGirlfriendIsland — center
            case 35 -> GirlfriendIsland.makeGirlfriendIsland(level, random, x, y, z);
            // 36 gold MyDungeon.makeGreenhouseDungeon — min-corner
            case 36 -> GreenhouseDungeon.makeGreenhouseDungeon(
                    level,
                    random,
                    x - GreenhouseDungeon.WIDTH / 2,
                    y,
                    z - GreenhouseDungeon.DEPTH / 2);
            // 37 gold MyDungeon.makeMonsterIsland — center
            case 37 -> MonsterIsland.makeMonsterIsland(level, random, x, y, z);
            // 38 gold makeNightmareRookery — interim standalone NightmareDungeon
            case 38 -> NightmareDungeon.makeDungeon(
                    level, random, x - NightmareDungeon.WIDTH / 2, y, z - NightmareDungeon.WIDTH / 2);
            // 39 gold MyDungeon.makeStinkyHouse — house min-corner
            case 39 -> StinkyHouse.makeStinkyHouse(
                    level,
                    random,
                    x - StinkyHouse.HOUSE_LENGTH / 2,
                    y,
                    z - StinkyHouse.HOUSE_WIDTH / 2);
            // 40 gold makeRubberDuckyPond
            case 40 -> RubberDuckyPond.makeRubberDuckyPond(
                    level, random, x - RubberDuckyPond.WIDTH / 2, y, z - RubberDuckyPond.WIDTH / 2);
            // 41 gold MyDungeon.makeWhiteHouse — gold seed origin
            case 41 -> WhiteHouse.makeWhiteHouse(level, random, x, y, z);
            // 42 gold MyDungeon.makeQueenAltar
            case 42 -> QueenAltar.makeQueenAltar(
                    level, random, x - QueenAltar.WIDTH / 2, y, z - QueenAltar.WIDTH / 2);
            // 43 gold makeFrogPond (gold Y+1)
            case 43 -> FrogPond.makeFrogPond(level, random, x, y + 1, z);
            // 44 gold makePumpkin (gold Y+1)
            case 44 -> PumpkinHouse.makePumpkin(
                    level, random, x - PumpkinHouse.WIDTH / 2, y + 1, z - PumpkinHouse.DEPTH / 2);
            // 45 gold makeRoundRotator (gold Y+1)
            case 45 -> RoundRotator.makeRoundRotator(level, random, x, y + 1, z);
            // 46 gold MyDungeon.makeRainbow — center (elevated content)
            case 46 -> RainbowDungeon.makeRainbow(level, random, x, y, z);
            // 47 gold MyDungeon.makeEnormousCastleQ — min-corner of 28×28 shell
            case 47 -> EnormousCastleQ.makeEnormousCastleQ(
                    level, random, x - EnormousCastleQ.WIDTH / 2, y, z - EnormousCastleQ.WIDTH / 2);
            // 48 gold makeSpiderHangout
            case 48 -> SpiderHangout.makeSpiderHangout(
                    level, random, x - SpiderHangout.WIDTH / 2, y, z - SpiderHangout.WIDTH / 2);
            // 49 gold makeRedAntHangout
            case 49 -> RedAntHangout.makeRedAntHangout(
                    level, random, x - RedAntHangout.WIDTH / 2, y, z - RedAntHangout.WIDTH / 2);
            default -> {
                GenericDungeon.makeDungeon(
                        level, random, x - GenericDungeon.WIDTH / 2, y, z - GenericDungeon.WIDTH / 2);
                LOGGER.debug(
                        "DungeonSpawner type {} — full gold structure not ported; used GenericDungeon at {},{},{}",
                        type,
                        x,
                        y,
                        z);
            }
        }
    }

    /** Gold type name table for docs / debug (indices match gold if-chain). */
    public static String goldTypeName(int type) {
        return switch (type) {
            case 0 -> "FairyTree";
            case 1 -> "FairyCastleTree";
            case 2 -> "EnormousCastle";
            case 3 -> "RotatorStation";
            case 4 -> "BeeHive";
            case 5 -> "HauntedHouse";
            case 6 -> "MantisHive";
            case 7 -> "KyuubiDungeon";
            case 8 -> "SmallBeeHive";
            case 9 -> "ShadowDungeon";
            case 10 -> "AlienWTFDungeon";
            case 11 -> "EnderKnightDungeon";
            case 12 -> "PlayPool";
            case 13 -> "WaterDragonLair";
            case 14 -> "CloudSharkDungeon";
            case 15 -> "LeafMonsterDungeon";
            case 16 -> "MiniDungeon";
            case 17 -> "GoldFishBowl";
            case 18 -> "EnderReaperGraveyard";
            case 19 -> "SpitBugLair";
            case 20 -> "Igloo";
            case 21 -> "GenericDungeon";
            case 22 -> "RubyBirdDungeon";
            case 23 -> "BasiliskMaze";
            case 24 -> "EnderDragonHospital";
            case 25 -> "CrystalHauntedHouse";
            case 26 -> "BouncyCastle";
            case 27 -> "EnderCastle";
            case 28 -> "DamselInDistress";
            case 29 -> "IncaPyramid";
            case 30 -> "RobotLab";
            case 31 -> "KingAltar";
            case 32 -> "LeonNest";
            case 33 -> "CrystalBattleTower";
            case 34 -> "CephadromeAltar";
            case 35 -> "GirlfriendIsland";
            case 36 -> "GreenhouseDungeon";
            case 37 -> "MonsterIsland";
            case 38 -> "NightmareDungeon"; // gold name NightmareRookery; interim NightmareDungeon structure
            case 39 -> "StinkyHouse";
            case 40 -> "RubberDuckyPond";
            case 41 -> "WhiteHouse";
            case 42 -> "QueenAltar";
            case 43 -> "FrogPond";
            case 44 -> "Pumpkin";
            case 45 -> "RoundRotator";
            case 46 -> "Rainbow";
            case 47 -> "EnormousCastleQ";
            case 48 -> "SpiderHangout";
            case 49 -> "RedAntHangout";
            default -> "Unknown(" + type + ")";
        };
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        // gold: always 5 fireworksSpark per display tick
        for (int j1 = 0; j1 < 5; j1++) {
            level.addParticle(
                    ParticleTypes.FIREWORK,
                    pos.getX() + random.nextFloat(),
                    pos.getY() + random.nextFloat(),
                    pos.getZ() + random.nextFloat(),
                    (random.nextFloat() - random.nextFloat()) / 4.0,
                    random.nextFloat() / 2.0,
                    (random.nextFloat() - random.nextFloat()) / 4.0);
        }
    }
}
