package danger.orespawn.init;

import danger.orespawn.blocks.BlockAnt;
import danger.orespawn.blocks.BlockButterflyPlant;
import danger.orespawn.blocks.BlockCreeperRepellent;
import danger.orespawn.blocks.BlockCrystal;
import danger.orespawn.blocks.BlockCrystalFurnace;
import danger.orespawn.blocks.BlockCrystalGrass;
import danger.orespawn.blocks.BlockCrystalOre;
import danger.orespawn.blocks.BlockCrystalPlant;
import danger.orespawn.blocks.BlockCrystalPlanks;
import danger.orespawn.blocks.BlockCrystalWorkbench;
import danger.orespawn.blocks.BlockCornPlant;
import danger.orespawn.blocks.BlockDuctTape;
import danger.orespawn.blocks.BlockEggOre;
import danger.orespawn.blocks.BlockExtremeTorch;
import danger.orespawn.blocks.BlockFireflyPlant;
import danger.orespawn.blocks.BlockAppleLeaves;
import danger.orespawn.blocks.BlockCrystalLeaves;
import danger.orespawn.blocks.BlockCrystalTorch;
import danger.orespawn.blocks.BlockCrystalTreeLog;
import danger.orespawn.blocks.BlockDungeonSpawner;
import danger.orespawn.blocks.BlockDuplicatorLog;
import danger.orespawn.blocks.BlockExperienceLeaves;
import danger.orespawn.blocks.BlockIsland;
import danger.orespawn.blocks.BlockKingSpawner;
import danger.orespawn.blocks.BlockKrakenRepellent;
import danger.orespawn.blocks.BlockLavafoam;
import danger.orespawn.blocks.BlockLettucePlant;
import danger.orespawn.blocks.BlockMoleDirt;
import danger.orespawn.blocks.BlockMosquitoPlant;
import danger.orespawn.blocks.BlockMothPlant;
import danger.orespawn.blocks.BlockOreRuby;
import danger.orespawn.blocks.BlockOreSalt;
import danger.orespawn.blocks.BlockOreSpawnFlower;
import danger.orespawn.blocks.BlockPizza;
import danger.orespawn.blocks.BlockPortalOreSpawn;
import danger.orespawn.blocks.BlockQueenSpawner;
import danger.orespawn.blocks.BlockQuinoaPlant;
import danger.orespawn.blocks.BlockRadishPlant;
import danger.orespawn.blocks.BlockRedAntTroll;
import danger.orespawn.blocks.BlockRicePlant;
import danger.orespawn.blocks.BlockRock;
import danger.orespawn.blocks.BlockRTP;
import danger.orespawn.blocks.BlockScaryLeaves;
import danger.orespawn.blocks.BlockSkyTreeLog;
import danger.orespawn.blocks.BlockStrawberryPlant;
import danger.orespawn.blocks.BlockTermiteTroll;
import danger.orespawn.blocks.BlockTitaniumMetal;
import danger.orespawn.blocks.BlockTitaniumOre;
import danger.orespawn.blocks.BlockTomatoPlant;
import danger.orespawn.blocks.BlockUraniumMetal;
import danger.orespawn.blocks.BlockUraniumOre;
import danger.orespawn.util.Reference;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * Gold ModBlocks — registry names 1:1. Stats refined as agents port Block* classes.
 * Wave 0: core ores/blocks so items/tools can reference materials.
 * Plants / ant_block / extreme_torch ported from gold blocks/*.
 */
public final class ModBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(Reference.MOD_ID);
    public static final DeferredRegister.Items BLOCK_ITEMS = DeferredRegister.createItems(Reference.MOD_ID);

    /** Gold OreUranium: hardness 5, resistance 5, lightLevel 0.2F, glow/sparkle on interact. */
    public static final DeferredBlock<Block> URANIUM_ORE = register(
            "uranium_ore",
            () -> new BlockUraniumOre(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_GREEN)
                    .strength(5.0f, 5.0f)
                    .requiresCorrectToolForDrops()
                    .lightLevel(s -> 3) // gold setLightLevel(0.2F) ≈ 3/15
                    .sound(SoundType.STONE)));
    /** Gold OreTitanium: hardness 5, resistance 5, lightLevel 0.2F, glow/sparkle on interact. */
    public static final DeferredBlock<Block> TITANIUM_ORE = register(
            "titanium_ore",
            () -> new BlockTitaniumOre(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.METAL)
                    .strength(5.0f, 5.0f)
                    .requiresCorrectToolForDrops()
                    .lightLevel(s -> 3)
                    .sound(SoundType.STONE)));
    public static final DeferredBlock<Block> AMETHYST_ORE = register("amethyst_ore", ore(MapColor.COLOR_PURPLE, 3.0f));
    /** Gold BlockUranium — metal block, light ~0.2, occasional green sparkle. */
    public static final DeferredBlock<Block> URANIUM_BLOCK = register(
            "uranium_block",
            () -> new BlockUraniumMetal(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_GREEN)
                    .strength(5.0f, 5.0f)
                    .requiresCorrectToolForDrops()
                    .lightLevel(s -> 3)
                    .sound(SoundType.METAL)));
    /** Gold BlockTitanium — metal block, light ~0.2, occasional silver sparkle. */
    public static final DeferredBlock<Block> TITANIUM_BLOCK = register(
            "titanium_block",
            () -> new BlockTitaniumMetal(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.METAL)
                    .strength(5.0f, 5.0f)
                    .requiresCorrectToolForDrops()
                    .lightLevel(s -> 3)
                    .sound(SoundType.METAL)));
    public static final DeferredBlock<Block> AMETHYST_BLOCK = register("amethyst_block", metal(MapColor.COLOR_PURPLE));

    // ——— 1.7.10 Crystal dimension terrain (non-opaque / translucent like gold) ———
    /** Shared props: gold isOpaqueCube=false, renderAsNormalBlock=false → see-through crystal. */
    private static BlockBehaviour.Properties crystalTranslucent(MapColor color, float hard, float res) {
        return BlockBehaviour.Properties.of()
                .mapColor(color)
                .strength(hard, res)
                .noOcclusion()
                .isViewBlocking((s, g, p) -> false)
                .isSuffocating((s, g, p) -> false)
                .sound(SoundType.GLASS);
    }

    /** Crystal stone (BlockCrystal). Hardness 4, light ~0.4. */
    public static final DeferredBlock<Block> CRYSTAL_STONE = register(
            "crystal_stone",
            () -> new BlockCrystal(crystalTranslucent(MapColor.COLOR_PURPLE, 4.0f, 4.0f)
                    .requiresCorrectToolForDrops()
                    .lightLevel(s -> 6)));
    /**
     * Crystal grass — gold {@code CrystalGrass}. Walkable solid; gold textures use alpha
     * flecks (must render translucent). See {@link danger.orespawn.blocks.BlockCrystalGrass}.
     */
    public static final DeferredBlock<Block> CRYSTAL_GRASS = register(
            "crystal_grass",
            () -> new BlockCrystalGrass(BlockCrystalGrass.defaultProps()));
    /** Crystal log — gold non-opaque. */
    public static final DeferredBlock<Block> CRYSTAL_LOG = register(
            "crystal_log",
            () -> new BlockCrystal(crystalTranslucent(MapColor.COLOR_PURPLE, 2.0f, 2.0f)
                    .sound(SoundType.GLASS)));
    public static final DeferredBlock<Block> CRYSTAL_LEAVES = register(
            "crystal_leaves",
            () -> new BlockCrystal(crystalTranslucent(MapColor.COLOR_PURPLE, 0.2f, 0.2f)
                    .sound(SoundType.GLASS)
                    .isRedstoneConductor((s, g, p) -> false)));
    /** Crystal planks (CrystalWood) — gold 1.5 / 4.0 non-opaque. */
    public static final DeferredBlock<Block> CRYSTAL_PLANKS = register(
            "crystal_planks",
            () -> new BlockCrystalPlanks(BlockCrystalPlanks.defaultProps()));
    /** Gold CrystalWorkbench — opens vanilla 3×3 crafting. */
    public static final DeferredBlock<Block> CRYSTAL_WORKBENCH = register(
            "crystal_workbench",
            () -> new BlockCrystalWorkbench(BlockCrystalWorkbench.defaultProps()));
    /** Gold CrystalFurnace — LIT light ~9, smelting BE. */
    public static final DeferredBlock<Block> CRYSTAL_FURNACE = register(
            "crystal_furnace",
            () -> new BlockCrystalFurnace(BlockCrystalFurnace.defaultProps()));

    // Gold MyBlockFlower day/night morph pairs (partners via registry id to avoid static forward refs)
    public static final DeferredBlock<Block> FLOWER_PINK = register(
            "flower_pink",
            () -> new BlockOreSpawnFlower(
                    BlockOreSpawnFlower.defaultProps(), flowerPartner("flower_black"), true));
    public static final DeferredBlock<Block> FLOWER_BLACK = register(
            "flower_black",
            () -> new BlockOreSpawnFlower(
                    BlockOreSpawnFlower.defaultProps(), flowerPartner("flower_pink"), false));
    public static final DeferredBlock<Block> FLOWER_BLUE = register(
            "flower_blue",
            () -> new BlockOreSpawnFlower(
                    BlockOreSpawnFlower.defaultProps(), flowerPartner("flower_scary"), true));
    public static final DeferredBlock<Block> FLOWER_SCARY = register(
            "flower_scary",
            () -> new BlockOreSpawnFlower(
                    BlockOreSpawnFlower.defaultProps(), flowerPartner("flower_blue"), false));

    // Gold crystal-dimension MyBlockFlower variants (no day/night morph)
    public static final DeferredBlock<Block> CRYSTALFLOWER_RED = register(
            "crystalflower_red",
            () -> new BlockOreSpawnFlower(BlockOreSpawnFlower.defaultProps(), null, false));
    public static final DeferredBlock<Block> CRYSTALFLOWER_GREEN = register(
            "crystalflower_green",
            () -> new BlockOreSpawnFlower(BlockOreSpawnFlower.defaultProps(), null, false));
    public static final DeferredBlock<Block> CRYSTALFLOWER_BLUE = register(
            "crystalflower_blue",
            () -> new BlockOreSpawnFlower(BlockOreSpawnFlower.defaultProps(), null, false));
    public static final DeferredBlock<Block> CRYSTALFLOWER_YELLOW = register(
            "crystalflower_yellow",
            () -> new BlockOreSpawnFlower(BlockOreSpawnFlower.defaultProps(), null, false));

    // Gold BlockCrystalPlant saplings
    public static final DeferredBlock<Block> CRYSTAL_SAPLING = register(
            "crystalsapling",
            () -> new BlockCrystalPlant(BlockCrystalPlant.defaultProps(), BlockCrystalPlant.TreeStyle.TALL));
    public static final DeferredBlock<Block> CRYSTAL_SAPLING_2 = register(
            "crystalsapling2",
            () -> new BlockCrystalPlant(BlockCrystalPlant.defaultProps(), BlockCrystalPlant.TreeStyle.SCRAGGLY));
    public static final DeferredBlock<Block> CRYSTAL_SAPLING_3 = register(
            "crystalsapling3",
            () -> new BlockCrystalPlant(BlockCrystalPlant.defaultProps(), BlockCrystalPlant.TreeStyle.TALL_BLUE));

    /** OreCrystal — translucent sparkle ore (render pass 1). */
    public static final DeferredBlock<Block> CRYSTAL_ORE = register(
            "crystal_ore",
            () -> new BlockCrystalOre(crystalTranslucent(MapColor.COLOR_PURPLE, 3.0f, 5.0f)
                    .requiresCorrectToolForDrops()
                    .lightLevel(s -> 8)));
    public static final DeferredBlock<Block> CRYSTAL_PINK_BLOCK = register(
            "crystal_pink_block",
            () -> new BlockCrystal(crystalTranslucent(MapColor.COLOR_PINK, 5.0f, 6.0f)
                    .requiresCorrectToolForDrops()
                    .lightLevel(s -> 5)));

    /**
     * Gold {@code TigersEye} OreCrystalCrystal — crystal-dim ore (0.5 hard, light, translucent).
     * Registry: {@code tigerseye}. Smelts to tigerseye_ingot.
     */
    public static final DeferredBlock<Block> TIGERSEYE = register(
            "tigerseye",
            () -> new BlockCrystalOre(crystalTranslucent(MapColor.COLOR_ORANGE, 0.5f, 15.0f)
                    .requiresCorrectToolForDrops()
                    .lightLevel(s -> 9)));
    /** Gold {@code MyTigersEyeBlock} storage block. */
    public static final DeferredBlock<Block> TIGERSEYE_BLOCK = register(
            "tigerseye_block",
            () -> new BlockCrystal(crystalTranslucent(MapColor.COLOR_ORANGE, 5.0f, 6.0f)
                    .requiresCorrectToolForDrops()
                    .lightLevel(s -> 5)));

    // Egg ores (gold OreGenericEgg) — BlockEggOre: 50% XP 5+rand3+rand3 on break
    public static final DeferredBlock<Block> ALOSAURUS_ORE = registerEgg("alosaurus_ore");
    public static final DeferredBlock<Block> BARYONYX_ORE = registerEgg("baryonyx_ore");
    public static final DeferredBlock<Block> CAMARASAURUS_ORE = registerEgg("camarasaurus_ore");
    public static final DeferredBlock<Block> CRYOLOPHOSAURUS_ORE = registerEgg("cryolophosaurus_ore");
    public static final DeferredBlock<Block> POINTYSAURUS_ORE = registerEgg("pointysaurus_ore");
    public static final DeferredBlock<Block> TREX_ORE = registerEgg("trex_ore");
    public static final DeferredBlock<Block> COW_ORE = registerEgg("cow_ore");
    public static final DeferredBlock<Block> CREEPER_ORE = registerEgg("creeper_ore");
    public static final DeferredBlock<Block> GHAST_ORE = registerEgg("ghast_ore");
    public static final DeferredBlock<Block> HORSE_ORE = registerEgg("horse_ore");
    public static final DeferredBlock<Block> PIG_ORE = registerEgg("pig_ore");
    public static final DeferredBlock<Block> ZOMBIE_ORE = registerEgg("zombie_ore");
    public static final DeferredBlock<Block> BIRD_ORE = registerEgg("bird_ore");
    public static final DeferredBlock<Block> ALIEN_ORE = registerEgg("alien_ore");
    public static final DeferredBlock<Block> CAVEFISHER_ORE = registerEgg("cavefisher_ore");
    public static final DeferredBlock<Block> NASTYSAURUS_ORE = registerEgg("nastysaurus_ore");
    public static final DeferredBlock<Block> VELOCITYRAPTOR_ORE = registerEgg("velocityraptor_ore");
    public static final DeferredBlock<Block> GAMMAMETROID_ORE = registerEgg("gammametroid_ore");
    public static final DeferredBlock<Block> SPYRO_ORE = registerEgg("spyro_ore");
    public static final DeferredBlock<Block> DRAGONFLY_ORE = registerEgg("dragonfly_ore");
    public static final DeferredBlock<Block> SMALLWORM_ORE = registerEgg("smallworm_ore");
    public static final DeferredBlock<Block> MEDIUMWORM_ORE = registerEgg("mediumworm_ore");
    public static final DeferredBlock<Block> LARGEWORM_ORE = registerEgg("largeworm_ore");
    public static final DeferredBlock<Block> DOOMWORM_ORE = registerEgg("doomworm_ore");
    public static final DeferredBlock<Block> MANTIS_ORE = registerEgg("mantis_ore");
    public static final DeferredBlock<Block> BEAVER_ORE = registerEgg("beaver_ore");
    public static final DeferredBlock<Block> BRUTALFLY_ORE = registerEgg("brutalfly_ore");
    public static final DeferredBlock<Block> KYUUBI_ORE = registerEgg("kyuubi_ore");
    public static final DeferredBlock<Block> MOTHRA_ORE = registerEgg("mothra_ore");
    public static final DeferredBlock<Block> CASSOWARY_ORE = registerEgg("cassowary_ore");
    public static final DeferredBlock<Block> REDCOW_ORE = registerEgg("redcow_ore");
    public static final DeferredBlock<Block> STINKBUG_ORE = registerEgg("stinkbug_ore");

    // Remaining gold 1.7.10 egg ores (OreSpawnWorld.generateOres pool) + boss egg parts
    public static final DeferredBlock<Block> SPIDER_ORE = registerEgg("spider_ore");
    public static final DeferredBlock<Block> BAT_ORE = registerEgg("bat_ore");
    public static final DeferredBlock<Block> SQUID_ORE = registerEgg("squid_ore");
    public static final DeferredBlock<Block> CHICKEN_ORE = registerEgg("chicken_ore");
    public static final DeferredBlock<Block> SKELETON_ORE = registerEgg("skeleton_ore");
    public static final DeferredBlock<Block> SLIME_ORE = registerEgg("slime_ore");
    public static final DeferredBlock<Block> ZOMBIE_PIGMAN_ORE = registerEgg("zombie_pigman_ore");
    public static final DeferredBlock<Block> ENDERMAN_ORE = registerEgg("enderman_ore");
    public static final DeferredBlock<Block> CAVE_SPIDER_ORE = registerEgg("cave_spider_ore");
    public static final DeferredBlock<Block> SILVERFISH_ORE = registerEgg("silverfish_ore");
    public static final DeferredBlock<Block> MAGMA_CUBE_ORE = registerEgg("magma_cube_ore");
    public static final DeferredBlock<Block> WITCH_ORE = registerEgg("witch_ore");
    public static final DeferredBlock<Block> SHEEP_ORE = registerEgg("sheep_ore");
    public static final DeferredBlock<Block> WOLF_ORE = registerEgg("wolf_ore");
    public static final DeferredBlock<Block> MOOSHROOM_ORE = registerEgg("mooshroom_ore");
    public static final DeferredBlock<Block> OCELOT_ORE = registerEgg("ocelot_ore");
    public static final DeferredBlock<Block> BLAZE_ORE = registerEgg("blaze_ore");
    public static final DeferredBlock<Block> WITHER_SKELETON_ORE = registerEgg("wither_skeleton_ore");
    public static final DeferredBlock<Block> ENDER_DRAGON_ORE = registerEgg("ender_dragon_ore");
    public static final DeferredBlock<Block> SNOW_GOLEM_ORE = registerEgg("snow_golem_ore");
    public static final DeferredBlock<Block> IRON_GOLEM_ORE = registerEgg("iron_golem_ore");
    public static final DeferredBlock<Block> WITHER_ORE = registerEgg("wither_ore");
    public static final DeferredBlock<Block> VILLAGER_ORE = registerEgg("villager_ore");
    public static final DeferredBlock<Block> GIRLFRIEND_ORE = registerEgg("girlfriend_ore");
    public static final DeferredBlock<Block> BOYFRIEND_ORE = registerEgg("boyfriend_ore");
    public static final DeferredBlock<Block> GOLD_COW_ORE = registerEgg("gold_cow_ore");
    public static final DeferredBlock<Block> ENCHANTED_COW_ORE = registerEgg("enchanted_cow_ore");
    public static final DeferredBlock<Block> HYDROLISC_ORE = registerEgg("hydrolisc_ore");
    public static final DeferredBlock<Block> BASILISK_ORE = registerEgg("basilisk_ore");
    public static final DeferredBlock<Block> EMPEROR_SCORPION_ORE = registerEgg("emperor_scorpion_ore");
    public static final DeferredBlock<Block> SCORPION_ORE = registerEgg("scorpion_ore");
    public static final DeferredBlock<Block> COCKATEIL_ORE = registerEgg("cockateil_ore");
    public static final DeferredBlock<Block> ATTACK_SQUID_ORE = registerEgg("attack_squid_ore");
    public static final DeferredBlock<Block> WATER_DRAGON_ORE = registerEgg("water_dragon_ore");
    public static final DeferredBlock<Block> KRAKEN_ORE = registerEgg("kraken_ore");
    public static final DeferredBlock<Block> LIZARD_ORE = registerEgg("lizard_ore");
    public static final DeferredBlock<Block> CEPHADROME_ORE = registerEgg("cephadrome_ore");
    public static final DeferredBlock<Block> DRAGON_ORE = registerEgg("dragon_ore");
    public static final DeferredBlock<Block> BEE_ORE = registerEgg("bee_ore");
    public static final DeferredBlock<Block> TROOPER_BUG_ORE = registerEgg("trooper_bug_ore");
    public static final DeferredBlock<Block> SPIT_BUG_ORE = registerEgg("spit_bug_ore");
    public static final DeferredBlock<Block> OSTRICH_ORE = registerEgg("ostrich_ore");
    public static final DeferredBlock<Block> GAZELLE_ORE = registerEgg("gazelle_ore");
    public static final DeferredBlock<Block> CHIPMUNK_ORE = registerEgg("chipmunk_ore");
    public static final DeferredBlock<Block> CREEPING_HORROR_ORE = registerEgg("creeping_horror_ore");
    public static final DeferredBlock<Block> TERRIBLE_TERROR_ORE = registerEgg("terrible_terror_ore");
    public static final DeferredBlock<Block> CLIFF_RACER_ORE = registerEgg("cliff_racer_ore");
    public static final DeferredBlock<Block> TRIFFID_ORE = registerEgg("triffid_ore");
    public static final DeferredBlock<Block> PITCH_BLACK_ORE = registerEgg("pitch_black_ore");
    public static final DeferredBlock<Block> LURKING_TERROR_ORE = registerEgg("lurking_terror_ore");
    public static final DeferredBlock<Block> GODZILLA_ORE = registerEgg("godzilla_ore");
    public static final DeferredBlock<Block> CLOUD_SHARK_ORE = registerEgg("cloud_shark_ore");
    public static final DeferredBlock<Block> GOLD_FISH_ORE = registerEgg("gold_fish_ore");
    public static final DeferredBlock<Block> LEAF_MONSTER_ORE = registerEgg("leaf_monster_ore");
    public static final DeferredBlock<Block> TSHIRT_ORE = registerEgg("tshirt_ore");
    public static final DeferredBlock<Block> ENDER_KNIGHT_ORE = registerEgg("ender_knight_ore");
    public static final DeferredBlock<Block> ENDER_REAPER_ORE = registerEgg("ender_reaper_ore");
    public static final DeferredBlock<Block> HERCULES_BEETLE_ORE = registerEgg("hercules_beetle_ore");
    public static final DeferredBlock<Block> STINKY_ORE = registerEgg("stinky_ore");
    public static final DeferredBlock<Block> EASTER_BUNNY_ORE = registerEgg("easter_bunny_ore");
    public static final DeferredBlock<Block> CATERKILLER_ORE = registerEgg("caterkiller_ore");
    public static final DeferredBlock<Block> MOLENOID_ORE = registerEgg("molenoid_ore");
    public static final DeferredBlock<Block> SEA_MONSTER_ORE = registerEgg("sea_monster_ore");
    public static final DeferredBlock<Block> SEA_VIPER_ORE = registerEgg("sea_viper_ore");
    public static final DeferredBlock<Block> LEON_ORE = registerEgg("leon_ore");
    public static final DeferredBlock<Block> HAMMERHEAD_ORE = registerEgg("hammerhead_ore");
    public static final DeferredBlock<Block> RUBBER_DUCKY_ORE = registerEgg("rubber_ducky_ore");
    public static final DeferredBlock<Block> CRICKET_ORE = registerEgg("cricket_ore");
    public static final DeferredBlock<Block> FROG_ORE = registerEgg("frog_ore");
    public static final DeferredBlock<Block> SPIDER_DRIVER_ORE = registerEgg("spider_driver_ore");
    public static final DeferredBlock<Block> CRAB_ORE = registerEgg("crab_ore");
    public static final DeferredBlock<Block> THE_KING_ORE = registerEgg("the_king_ore");
    public static final DeferredBlock<Block> THE_QUEEN_ORE = registerEgg("the_queen_ore");
    public static final DeferredBlock<Block> GODZILLA_PART_ORE = registerEgg("godzilla_part_ore");
    public static final DeferredBlock<Block> THE_KING_PART_ORE = registerEgg("the_king_part_ore");
    public static final DeferredBlock<Block> THE_QUEEN_PART_ORE = registerEgg("the_queen_part_ore");

    // ——— Plants / nests / torch (gold registry names 1:1) ———
    // Crops have no block-item; seeds (ModItems) place them.

    public static final DeferredBlock<Block> CORN_PLANT = registerNoItem(
            "corn_plant",
            () -> new BlockCornPlant(cropProps()));

    public static final DeferredBlock<Block> BUTTERFLY_PLANT = registerNoItem(
            "butterfly_plant",
            () -> new BlockButterflyPlant(cropProps()));

    public static final DeferredBlock<Block> MOSQUITO_PLANT = registerNoItem(
            "mosquito_plant",
            () -> new BlockMosquitoPlant(cropProps()));

    public static final DeferredBlock<Block> FIREFLY_PLANT = registerNoItem(
            "firefly_plant",
            () -> new BlockFireflyPlant(cropProps()));

    public static final DeferredBlock<Block> MOTH_PLANT = registerNoItem(
            "moth_plant",
            () -> new BlockMothPlant(cropProps()));

    // ——— SET12 food crops (gold BlockStrawberry / tomato / lettuce / …) ———
    public static final DeferredBlock<Block> STRAWBERRY_PLANT = registerNoItem(
            "strawberry_plant",
            () -> new BlockStrawberryPlant(cropProps()));
    public static final DeferredBlock<Block> RADISH_PLANT = registerNoItem(
            "radish_plant",
            () -> new BlockRadishPlant(cropProps()));
    public static final DeferredBlock<Block> RICE_PLANT = registerNoItem(
            "rice_plant",
            () -> new BlockRicePlant(cropProps()));
    public static final DeferredBlock<Block> QUINOA_PLANT = registerNoItem(
            "quinoa_plant",
            () -> new BlockQuinoaPlant(cropProps()));
    public static final DeferredBlock<Block> TOMATO_PLANT = registerNoItem(
            "tomato_plant",
            () -> new BlockTomatoPlant(cropProps()));
    public static final DeferredBlock<Block> LETTUCE_PLANT = registerNoItem(
            "lettuce_plant",
            () -> new BlockLettucePlant(cropProps()));

    /** Gold MyPizzaBlock — placed by ItemPizza (no auto BlockItem). */
    public static final DeferredBlock<Block> PIZZA = registerNoItem(
            "pizza",
            () -> new BlockPizza(BlockPizza.defaultProps()));
    /** Gold MyDuctTapeBlock — placed by ItemDuctTape. */
    public static final DeferredBlock<Block> DUCTTAPE = registerNoItem(
            "ducttape",
            () -> new BlockDuctTape(BlockDuctTape.defaultProps()));

    /** Gold MyRedAntBlock "Red Ant Nest" (id kept as ant_block so existing worlds keep their nests). */
    public static final DeferredBlock<Block> ANT_BLOCK = register(
            "ant_block", () -> new BlockAnt(antNestProps(), ModEntities.RED_ANT));
    /** Gold MyAntBlock "Ant Nest" — brown ants (Utopia portal). */
    public static final DeferredBlock<Block> BLACK_ANT_NEST = register(
            "black_ant_nest", () -> new BlockAnt(antNestProps(), ModEntities.ANT));
    public static final DeferredBlock<Block> RAINBOW_ANT_NEST = register(
            "rainbow_ant_nest", () -> new BlockAnt(antNestProps(), ModEntities.RAINBOW_ANT));
    public static final DeferredBlock<Block> UNSTABLE_ANT_NEST = register(
            "unstable_ant_nest", () -> new BlockAnt(antNestProps(), ModEntities.UNSTABLE_ANT));
    public static final DeferredBlock<Block> TERMITE_NEST = register(
            "termite_nest", () -> new BlockAnt(antNestProps(), ModEntities.TERMITE));
    /** Gold CrystalTermiteBlock — on crystal grass in the Crystal dimension. */
    public static final DeferredBlock<Block> CRYSTAL_TERMITE_NEST = register(
            "crystal_termite_nest", () -> new BlockAnt(antNestProps(), ModEntities.TERMITE));

    public static final DeferredBlock<Block> EXTREME_TORCH = register(
            "extreme_torch",
            () -> new BlockExtremeTorch(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_YELLOW)
                    .noCollission()
                    .instabreak()
                    .lightLevel(s -> 15)
                    .sound(SoundType.WOOD)
                    .pushReaction(PushReaction.DESTROY)));

    /** Gold BlockRedAntTroll — break spawns 20 red ants. */
    public static final DeferredBlock<Block> RED_ANT_TROLL_BLOCK = register(
            "red_ant_troll_block",
            () -> new BlockRedAntTroll(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.STONE)
                    .strength(1.5f, 6.0f)
                    .requiresCorrectToolForDrops()
                    .sound(SoundType.STONE)));

    /** Gold BlockTermiteTroll — break spawns 20 termites. */
    public static final DeferredBlock<Block> TERMITE_TROLL_BLOCK = register(
            "termite_troll_block",
            () -> new BlockTermiteTroll(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.STONE)
                    .strength(1.5f, 6.0f)
                    .requiresCorrectToolForDrops()
                    .sound(SoundType.STONE)));

    /**
     * Gold {@code MyIslandBlock} / IslandBlock — reed-like seed placed by Islands dim worldgen.
     * Random/scheduled tick spawns floating Island / IslandToo entities.
     */
    public static final DeferredBlock<Block> ISLAND_BLOCK = register(
            "island_block",
            () -> new BlockIsland(BlockIsland.defaultProps()));

    // ——— SET10 spawners / portal / RTP ———
    public static final DeferredBlock<Block> KING_SPAWNER =
            register("king_spawner", () -> new BlockKingSpawner(BlockKingSpawner.defaultProps()));
    public static final DeferredBlock<Block> QUEEN_SPAWNER =
            register("queen_spawner", () -> new BlockQueenSpawner(BlockQueenSpawner.defaultProps()));
    public static final DeferredBlock<Block> DUNGEON_SPAWNER =
            register("dungeon_spawner", () -> new BlockDungeonSpawner(BlockDungeonSpawner.defaultProps()));
    public static final DeferredBlock<Block> PORTAL_BLOCK =
            register("portal_block", () -> new BlockPortalOreSpawn(BlockPortalOreSpawn.defaultProps()));
    public static final DeferredBlock<Block> BLOCK_TELEPORT =
            register("block_teleport", () -> new BlockRTP(BlockRTP.defaultProps()));

    // ——— SET10 crystal / dim terrain ———
    public static final DeferredBlock<Block> CRYSTAL_TORCH =
            register("crystal_torch", () -> new BlockCrystalTorch(BlockCrystalTorch.defaultProps()));
    public static final DeferredBlock<Block> CRYSTAL_TREE_LOG =
            register("crystal_tree_log", () -> new BlockCrystalTreeLog(BlockCrystalTreeLog.defaultProps()));
    public static final DeferredBlock<Block> SKY_TREE_LOG =
            register("sky_tree_log", () -> new BlockSkyTreeLog(BlockSkyTreeLog.defaultProps()));
    public static final DeferredBlock<Block> DUPLICATOR_TREE_LOG =
            register("duplicator_tree_log", () -> new BlockDuplicatorLog(BlockDuplicatorLog.defaultProps()));
    public static final DeferredBlock<Block> CRYSTAL_TREE_LEAVES =
            register("crystal_tree_leaves", () -> new BlockCrystalLeaves(BlockCrystalLeaves.defaultProps()));
    public static final DeferredBlock<Block> CRYSTAL_TREE_LEAVES_2 =
            register("crystal_tree_leaves_2", () -> new BlockCrystalLeaves(BlockCrystalLeaves.defaultPropsHarder()));
    public static final DeferredBlock<Block> CRYSTAL_TREE_LEAVES_3 =
            register("crystal_tree_leaves_3", () -> new BlockCrystalLeaves(BlockCrystalLeaves.defaultPropsHarder()));
    public static final DeferredBlock<Block> APPLE_LEAVES =
            register("apple_leaves", () -> new BlockAppleLeaves(BlockAppleLeaves.defaultProps()));
    public static final DeferredBlock<Block> SCARY_LEAVES =
            register("scary_leaves", () -> new BlockScaryLeaves(BlockScaryLeaves.defaultProps()));
    public static final DeferredBlock<Block> EXPERIENCE_LEAVES =
            register("experience_leaves", () -> new BlockExperienceLeaves(BlockExperienceLeaves.defaultProps()));
    public static final DeferredBlock<Block> SALT_ORE =
            register("salt_ore", () -> new BlockOreSalt(BlockOreSalt.defaultProps()));
    public static final DeferredBlock<Block> RUBY_ORE =
            register("ruby_ore", () -> new BlockOreRuby(BlockOreRuby.defaultProps()));
    /** Gold {@code MyBlockRubyBlock} — 9× ruby storage. */
    public static final DeferredBlock<Block> RUBY_BLOCK =
            register("ruby_block", metal(MapColor.COLOR_RED));
    /** Gold {@code MyBlockMobzillaScaleBlock} — 9× godzilla scale storage. */
    public static final DeferredBlock<Block> MOBZILLA_SCALE_BLOCK =
            register("mobzilla_scale_block", metal(MapColor.COLOR_GREEN));
    /** Gold {@code MyEnderPearlBlock} — 9× ender pearl storage. */
    public static final DeferredBlock<Block> ENDER_PEARL_BLOCK =
            register("ender_pearl_block", metal(MapColor.COLOR_BLACK));
    /** Gold {@code MyEyeOfEnderBlock} — 9× eye of ender storage. */
    public static final DeferredBlock<Block> EYE_OF_ENDER_BLOCK =
            register("eye_of_ender_block", metal(MapColor.COLOR_GREEN));
    /**
     * Gold {@code CrystalCoal} — crystal-dim fuel/ore block; crafts CrystalTorch with crystal sticks.
     * Texture: {@code block/crystalcoal}.
     */
    public static final DeferredBlock<Block> CRYSTAL_COAL =
            register("crystal_coal", () -> new BlockCrystalOre(crystalTranslucent(MapColor.COLOR_BLACK, 0.6f, 6.0f)
                    .requiresCorrectToolForDrops()
                    .lightLevel(s -> 3)));
    /** Gold {@code MyCherryLeaves} — drops cherries. */
    public static final DeferredBlock<Block> CHERRY_LEAVES =
            register("cherry_leaves", () -> new BlockScaryLeaves(BlockScaryLeaves.defaultProps()));
    /** Gold {@code MyPeachLeaves} — drops peach. */
    public static final DeferredBlock<Block> PEACH_LEAVES =
            register("peach_leaves", () -> new BlockScaryLeaves(BlockScaryLeaves.defaultProps()));
    public static final DeferredBlock<Block> LAVAFOAM =
            register("lavafoam", () -> new BlockLavafoam(BlockLavafoam.defaultProps()));
    public static final DeferredBlock<Block> MOLE_DIRT =
            register("mole_dirt", () -> new BlockMoleDirt(BlockMoleDirt.defaultProps()));
    public static final DeferredBlock<Block> ROCK_BLOCK =
            register("rock_block", () -> new BlockRock(BlockRock.defaultProps()));

    // ——— SET11 repellents (gold CreeperRepellent / KrakenRepellent torch blocks) ———
    public static final DeferredBlock<Block> CREEPER_REPELLENT =
            register("creeperrepellent", () -> new BlockCreeperRepellent(BlockCreeperRepellent.defaultProps()));
    public static final DeferredBlock<Block> KRAKEN_REPELLENT =
            register("krakenrepellent", () -> new BlockKrakenRepellent(BlockKrakenRepellent.defaultProps()));

    private static BlockBehaviour.Properties cropProps() {
        return BlockBehaviour.Properties.of()
                .mapColor(MapColor.PLANT)
                .noCollission()
                .randomTicks()
                .instabreak()
                .sound(SoundType.CROP)
                .pushReaction(PushReaction.DESTROY);
    }

    /** Lazy partner block for day/night flower morph (resolved after full registry). */
    private static java.util.function.Supplier<Block> flowerPartner(String name) {
        return () -> net.minecraft.core.registries.BuiltInRegistries.BLOCK.get(
                net.minecraft.resources.ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, name));
    }

    private static BlockBehaviour.Properties antNestProps() {
        return BlockBehaviour.Properties.of()
                .mapColor(MapColor.DIRT)
                .strength(0.5f)
                .randomTicks()
                .sound(SoundType.GRAVEL);
    }

    private static DeferredBlock<Block> registerEgg(String name) {
        // Gold OreGenericEgg: hardness 0.5F, resistance 1.0F, Material.ground — soft, no tool req
        return register(name, () -> new BlockEggOre(BlockBehaviour.Properties.of()
                .mapColor(MapColor.SAND)
                .strength(0.5f, 1.0f)
                .sound(SoundType.STONE)));
    }

    private static BlockBehaviour.Properties ore(MapColor color, float hardness) {
        return BlockBehaviour.Properties.of()
                .mapColor(color)
                .strength(hardness, 3.0f)
                .requiresCorrectToolForDrops()
                .sound(SoundType.STONE);
    }

    private static BlockBehaviour.Properties metal(MapColor color) {
        return BlockBehaviour.Properties.of()
                .mapColor(color)
                .strength(5.0f, 6.0f)
                .requiresCorrectToolForDrops()
                .sound(SoundType.METAL);
    }

    private static DeferredBlock<Block> register(String name, BlockBehaviour.Properties props) {
        return register(name, () -> new Block(props));
    }

    private static DeferredBlock<Block> register(String name, java.util.function.Supplier<Block> block) {
        DeferredBlock<Block> def = BLOCKS.register(name, block);
        BLOCK_ITEMS.register(name, () -> new BlockItem(def.get(), new Item.Properties()));
        return def;
    }

    /** Block only — no BlockItem (seeds / food place the plant). */
    private static DeferredBlock<Block> registerNoItem(String name, java.util.function.Supplier<Block> block) {
        return BLOCKS.register(name, block);
    }

    private ModBlocks() {}
}
