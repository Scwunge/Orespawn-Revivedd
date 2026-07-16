package danger.orespawn.items;

import danger.orespawn.items.tools.ToolEnchantHelper;
import danger.orespawn.world.structures.AlienWTFDungeon;
import danger.orespawn.world.structures.BasiliskMaze;
import danger.orespawn.world.structures.BeeHive;
import danger.orespawn.world.structures.BouncyCastle;
import danger.orespawn.world.structures.CephadromeAltar;
import danger.orespawn.world.structures.CloudSharkDungeon;
import danger.orespawn.world.structures.CrystalBattleTower;
import danger.orespawn.world.structures.CrystalHauntedHouse;
import danger.orespawn.world.structures.CrystalMaze;
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
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Gold {@code ItemRandomDungeon} — registers as {@code randomdungeon}, decorations tab.
 * <p>
 * Gold: stack 1; Silk Touch glint; {@code onItemUse} on stone/cobble/grass/dirt at y≥40
 * placed {@code MyDungeonSpawnerBlock} one block above the click. Port places a random
 * <b>available</b> structure builder immediately (no delayed spawner block):
 * <ul>
 *   <li>0 — {@link GenericDungeon} (gold spawner type 21)</li>
 *   <li>1 — {@link NightmareDungeon} (gold type 38 interim)</li>
 *   <li>2 — {@link RubyBirdDungeon} (gold type 22)</li>
 *   <li>3 — {@link BasiliskMaze} (gold type 23)</li>
 *   <li>4 — {@link CrystalMaze} (no gold spawner id; crystal-dim builder)</li>
 *   <li>5 — {@link CrystalHauntedHouse} (gold type 25)</li>
 *   <li>6 — {@link CrystalBattleTower} (gold type 33)</li>
 *   <li>7 — {@link BeeHive} (gold type 4)</li>
 *   <li>8 — {@link HauntedHouse} (gold type 5)</li>
 *   <li>9 — {@link KingAltar} (gold type 31)</li>
 *   <li>10 — {@link QueenAltar} (gold type 42)</li>
 *   <li>11–16 — MiniDungeon, Igloo, SmallBeeHive, LeonNest, FrogPond, PumpkinHouse</li>
 * </ul>
 * Register; do not edit {@code ModItems} here.
 * <p>
 * Inventory icon: {@code textures/item/randomdungeon.png}.
 */
public class ItemRandomDungeon extends Item {
    /** Gold max stack ({@code field_77777_bU = 1}). */
    public static final int MAX_STACK = 1;

    /** Gold min click Y for placement. */
    public static final int MIN_PLACE_Y = 40;

    /**
     * Count of structure builders available to this item.
     * Expand when more dungeon ports land.
     */
    public static final int STRUCTURE_COUNT = 51;

    /** Horizontal look offset (blocks) for air {@link #use} placement. */
    public static final int LOOK_OFFSET = 6;

    public ItemRandomDungeon(Properties properties) {
        super(properties);
    }

    /** Gold: Silk Touch on craft for inventory glint. */
    @Override
    public void onCraftedBy(ItemStack stack, Level level, Player player) {
        ToolEnchantHelper.applyIfUnenchanted(stack, level, Enchantments.SILK_TOUCH, 1);
        super.onCraftedBy(stack, level, player);
    }

    /** Gold: re-apply Silk Touch if stripped (inventory / selected tick). */
    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        ToolEnchantHelper.applyIfUnenchanted(stack, level, Enchantments.SILK_TOUCH, 1);
        super.inventoryTick(stack, level, entity, slotId, isSelected);
    }

    /**
     * Gold {@code onItemUse}: surface must be stone/cobble/grass/dirt and y≥40;
     * structure origin is one block above the clicked face (gold spawner Y+1).
     */
    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        Player player = context.getPlayer();
        if (player == null) {
            return InteractionResult.PASS;
        }

        BlockPos clicked = context.getClickedPos();
        BlockState state = level.getBlockState(clicked);
        if (!isGoldSurface(state.getBlock())) {
            return InteractionResult.FAIL;
        }
        if (clicked.getY() < MIN_PLACE_Y) {
            return InteractionResult.FAIL;
        }

        // gold: place at clickedY + 1
        BlockPos origin = clicked.above();

        playPlaceSound(level, player);

        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        }

        if (!(level instanceof ServerLevel server)) {
            return InteractionResult.SUCCESS;
        }

        placeRandomStructure(server, server.getRandom(), origin);

        ItemStack stack = context.getItemInHand();
        if (!player.getAbilities().instabuild) {
            stack.shrink(1);
        }
        return InteractionResult.SUCCESS;
    }

    /**
     * Air right-click: place at feet + look-horizontal offset (server only).
     * No gold surface / Y gate — creative convenience for floating placement.
     */
    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        playPlaceSound(level, player);

        if (level.isClientSide) {
            return InteractionResultHolder.success(stack);
        }

        if (!(level instanceof ServerLevel server)) {
            return InteractionResultHolder.success(stack);
        }

        BlockPos origin = originFromLook(player);
        placeRandomStructure(server, server.getRandom(), origin);

        if (!player.getAbilities().instabuild) {
            stack.shrink(1);
        }
        return InteractionResultHolder.sidedSuccess(stack, false);
    }

    /** Gold surface set for onItemUse. */
    private static boolean isGoldSurface(Block block) {
        return block == Blocks.STONE
                || block == Blocks.COBBLESTONE
                || block == Blocks.GRASS_BLOCK
                || block == Blocks.DIRT
                || block == Blocks.DEEPSLATE
                || block == Blocks.ANDESITE
                || block == Blocks.DIORITE
                || block == Blocks.GRANITE;
    }

    private static void playPlaceSound(Level level, Player player) {
        // match InstantShelter / MinersDream structure-place SFX
        level.playSound(
                null,
                player.getX(),
                player.getY(),
                player.getZ(),
                SoundEvents.GENERIC_EXPLODE.value(),
                SoundSource.PLAYERS,
                1.0F,
                1.5F);
    }

    /**
     * Feet block + horizontal facing × {@link #LOOK_OFFSET}. Y stays at player feet
     * block so room floor sits near the player.
     */
    private static BlockPos originFromLook(Player player) {
        Direction horiz = player.getDirection();
        int bx = player.getBlockX() + horiz.getStepX() * LOOK_OFFSET;
        int by = player.getBlockY();
        int bz = player.getBlockZ() + horiz.getStepZ() * LOOK_OFFSET;
        return new BlockPos(bx, by, bz);
    }

    /**
     * Picks {@code random.nextInt(STRUCTURE_COUNT)} and dispatches a builder.
     * Room types are centered on {@code origin}; BasiliskMaze uses origin as
     * entrance surface point (gold / BasiliskMazeFeature).
     *
     * @return true if a builder accepted the Y range and wrote blocks
     */
    public static boolean placeRandomStructure(ServerLevel level, RandomSource random, BlockPos origin) {
        int pick = random.nextInt(STRUCTURE_COUNT);
        return placeStructure(level, random, origin, pick);
    }

    /**
     * Explicit structure index (0.{@link #STRUCTURE_COUNT}-1). Used by random pick
     * and available for debug hooks.
     */
    public static boolean placeStructure(ServerLevel level, RandomSource random, BlockPos origin, int pick) {
        int x = origin.getX();
        int y = origin.getY();
        int z = origin.getZ();

        return switch (pick) {
            case 0 -> GenericDungeon.makeDungeon(
                    level, random, x - GenericDungeon.WIDTH / 2, y, z - GenericDungeon.WIDTH / 2);
            case 1 -> NightmareDungeon.makeDungeon(
                    level, random, x - NightmareDungeon.WIDTH / 2, y, z - NightmareDungeon.WIDTH / 2);
            case 2 -> RubyBirdDungeon.makeDungeon(
                    level, random, x - RubyBirdDungeon.WIDTH / 2, y, z - RubyBirdDungeon.WIDTH / 2);
            case 3 -> BasiliskMaze.buildBasiliskMaze(level, random, x, y, z);
            case 4 -> CrystalMaze.buildCrystalMaze(
                    level, random, x - CrystalMaze.FOOTPRINT / 2, y, z - CrystalMaze.FOOTPRINT / 2);
            case 5 -> CrystalHauntedHouse.makeCrystalHauntedHouse(level, random, x, y, z);
            case 6 -> CrystalBattleTower.makeCrystalBattleTower(level, random, x, y, z);
            case 7 -> BeeHive.makeBeeHive(level, random, x - BeeHive.WIDTH / 2, y, z - BeeHive.WIDTH / 2);
            case 8 -> HauntedHouse.makeHauntedHouse(level, random, x, y, z);
            case 9 -> KingAltar.makeKingAltar(
                    level, random, x - KingAltar.WIDTH / 2, y, z - KingAltar.WIDTH / 2);
            case 10 -> QueenAltar.makeQueenAltar(
                    level, random, x - QueenAltar.WIDTH / 2, y, z - QueenAltar.WIDTH / 2);
            case 11 -> MiniDungeon.makeDungeon(
                    level, random, x - MiniDungeon.WIDTH / 2, y, z - MiniDungeon.WIDTH / 2);
            case 12 -> Igloo.makeIgloo(level, random, x, y, z);
            case 13 -> SmallBeeHive.makeSmallBeeHive(
                    level, random, x - SmallBeeHive.WIDTH / 2, y, z - SmallBeeHive.WIDTH / 2);
            case 14 -> LeonNest.makeLeonNest(level, random, x, y, z);
            case 15 -> FrogPond.makeFrogPond(level, random, x, y, z);
            case 16 -> PumpkinHouse.makePumpkin(
                    level, random, x - PumpkinHouse.WIDTH / 2, y, z - PumpkinHouse.DEPTH / 2);
            case 17 -> PlayPool.makePlayPool(level, random, x, y, z);
            case 18 -> GoldFishBowl.makeGoldFishBowl(level, random, x, y, z);
            case 19 -> SpitBugLair.makeSpitBugLair(level, random, x, y, z);
            case 20 -> LeafMonsterDungeon.makeLeafMonsterDungeon(level, random, x, y, z);
            case 21 -> RotatorStation.makeRotatorStation(level, random, x, y, z);
            case 22 -> ShadowDungeon.makeShadowDungeon(
                    level, random, x - ShadowDungeon.WIDTH / 2, y, z - ShadowDungeon.WIDTH / 2);
            case 23 -> SpiderHangout.makeSpiderHangout(
                    level, random, x - SpiderHangout.WIDTH / 2, y, z - SpiderHangout.WIDTH / 2);
            case 24 -> RedAntHangout.makeRedAntHangout(
                    level, random, x - RedAntHangout.WIDTH / 2, y, z - RedAntHangout.WIDTH / 2);
            case 25 -> RoundRotator.makeRoundRotator(level, random, x, y, z);
            case 26 -> RubberDuckyPond.makeRubberDuckyPond(
                    level, random, x - RubberDuckyPond.WIDTH / 2, y, z - RubberDuckyPond.WIDTH / 2);
            case 27 -> WaterDragonLair.makeWaterDragonLair(level, random, x, y, z);
            case 28 -> MantisHive.makeMantisHive(
                    level, random, x - MantisHive.WIDTH / 2, y, z - MantisHive.WIDTH / 2);
            case 29 -> KyuubiDungeon.makeKyuubiDungeon(
                    level, random, x - KyuubiDungeon.WIDTH / 2, y, z - KyuubiDungeon.WIDTH / 2);
            case 30 -> AlienWTFDungeon.makeAlienWTFDungeon(level, random, x, y, z);
            case 31 -> EnderKnightDungeon.makeEnderKnightDungeon(
                    level, random, x, y, z - EnderKnightDungeon.WIDTH_Z / 2);
            case 32 -> CloudSharkDungeon.makeCloudSharkDungeon(level, random, x, y, z);
            case 33 -> EnderReaperGraveyard.makeEnderReaperGraveyard(
                    level,
                    random,
                    x - EnderReaperGraveyard.WIDTH / 2,
                    y,
                    z - EnderReaperGraveyard.LENGTH / 2);
            case 34 -> BouncyCastle.makeBouncyCastle(level, random, x, y, z);
            case 35 -> DamselInDistress.makeDamselInDistress(level, random, x, y, z);
            case 36 -> CephadromeAltar.makeCephadromeAltar(level, random, x, y, z);
            case 37 -> GirlfriendIsland.makeGirlfriendIsland(level, random, x, y, z);
            case 38 -> GreenhouseDungeon.makeGreenhouseDungeon(
                    level,
                    random,
                    x - GreenhouseDungeon.WIDTH / 2,
                    y,
                    z - GreenhouseDungeon.DEPTH / 2);
            case 39 -> StinkyHouse.makeStinkyHouse(
                    level,
                    random,
                    x - StinkyHouse.HOUSE_LENGTH / 2,
                    y,
                    z - StinkyHouse.HOUSE_WIDTH / 2);
            case 40 -> RainbowDungeon.makeRainbow(level, random, x, y, z);
            case 41 -> FairyCastleTree.makeFairyCastleTree(level, random, x, y, z);
            case 42 -> RobotLab.makeRobotLab(
                    level, random, x - RobotLab.WIDTH / 2, y, z - RobotLab.LENGTH / 2);
            case 43 -> MonsterIsland.makeMonsterIsland(level, random, x, y, z);
            case 44 -> EnderDragonHospital.makeEnderDragonHospital(
                    level,
                    random,
                    x - EnderDragonHospital.WIDTH / 2,
                    y,
                    z - EnderDragonHospital.WIDTH / 2);
            case 45 -> IncaPyramid.makeIncaPyramid(
                    level, random, x - IncaPyramid.BASE_WIDTH / 2, y, z - IncaPyramid.BASE_DEPTH / 2);
            case 46 -> EnderCastle.makeEnderCastle(
                    level, random, x - EnderCastle.WIDTH / 2, y, z - EnderCastle.WIDTH / 2);
            case 47 -> WhiteHouse.makeWhiteHouse(level, random, x, y, z);
            case 48 -> EnormousCastle.makeEnormousCastle(
                    level, random, x - EnormousCastle.WIDTH / 2, y, z - EnormousCastle.WIDTH / 2);
            case 49 -> FairyTree.makeFairyTree(level, random, x, y, z);
            case 50 -> EnormousCastleQ.makeEnormousCastleQ(
                    level, random, x - EnormousCastleQ.WIDTH / 2, y, z - EnormousCastleQ.WIDTH / 2);
            default -> GenericDungeon.makeDungeon(
                    level, random, x - GenericDungeon.WIDTH / 2, y, z - GenericDungeon.WIDTH / 2);
        };
    }

    /** Human-readable name for structure index (docs / debug). */
    public static String structureName(int pick) {
        return switch (pick) {
            case 0 -> "GenericDungeon";
            case 1 -> "NightmareDungeon";
            case 2 -> "RubyBirdDungeon";
            case 3 -> "BasiliskMaze";
            case 4 -> "CrystalMaze";
            case 5 -> "CrystalHauntedHouse";
            case 6 -> "CrystalBattleTower";
            case 7 -> "BeeHive";
            case 8 -> "HauntedHouse";
            case 9 -> "KingAltar";
            case 10 -> "QueenAltar";
            case 11 -> "MiniDungeon";
            case 12 -> "Igloo";
            case 13 -> "SmallBeeHive";
            case 14 -> "LeonNest";
            case 15 -> "FrogPond";
            case 16 -> "PumpkinHouse";
            case 17 -> "PlayPool";
            case 18 -> "GoldFishBowl";
            case 19 -> "SpitBugLair";
            case 20 -> "LeafMonsterDungeon";
            case 21 -> "RotatorStation";
            case 22 -> "ShadowDungeon";
            case 23 -> "SpiderHangout";
            case 24 -> "RedAntHangout";
            case 25 -> "RoundRotator";
            case 26 -> "RubberDuckyPond";
            case 27 -> "WaterDragonLair";
            case 28 -> "MantisHive";
            case 29 -> "KyuubiDungeon";
            case 30 -> "AlienWTFDungeon";
            case 31 -> "EnderKnightDungeon";
            case 32 -> "CloudSharkDungeon";
            case 33 -> "EnderReaperGraveyard";
            case 34 -> "BouncyCastle";
            case 35 -> "DamselInDistress";
            case 36 -> "CephadromeAltar";
            case 37 -> "GirlfriendIsland";
            case 38 -> "GreenhouseDungeon";
            case 39 -> "StinkyHouse";
            case 40 -> "RainbowDungeon";
            case 41 -> "FairyCastleTree";
            case 42 -> "RobotLab";
            case 43 -> "MonsterIsland";
            case 44 -> "EnderDragonHospital";
            case 45 -> "IncaPyramid";
            case 46 -> "EnderCastle";
            case 47 -> "WhiteHouse";
            case 48 -> "EnormousCastle";
            case 49 -> "FairyTree";
            case 50 -> "EnormousCastleQ";
            default -> "Unknown(" + pick + ")";
        };
    }
}
