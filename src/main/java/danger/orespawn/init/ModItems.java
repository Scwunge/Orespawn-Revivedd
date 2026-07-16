package danger.orespawn.init;

import danger.orespawn.entity.AntRobot;
import danger.orespawn.entity.SpiderRobot;
import danger.orespawn.items.CritterCage;
import danger.orespawn.items.ItemAcid;
import danger.orespawn.items.ItemCreeperLauncher;
import danger.orespawn.items.ItemDuctTape;
import danger.orespawn.items.ItemElevator;
import danger.orespawn.items.ItemGenericEgg;
import danger.orespawn.items.ItemDeadIrukandji;
import danger.orespawn.items.ItemExperienceCatcher;
import danger.orespawn.items.ItemExperienceTreeSeed;
import danger.orespawn.items.ItemFruitTreeSeed;
import danger.orespawn.items.ItemIceBall;
import danger.orespawn.items.ItemInstantGarden;
import danger.orespawn.items.ItemInstantShelter;
import danger.orespawn.items.ItemIrukandjiArrow;
import danger.orespawn.items.ItemLaserBall;
import danger.orespawn.items.ItemMagicApple;
import danger.orespawn.items.ItemMinersDream;
import danger.orespawn.items.ItemNetherLost;
import danger.orespawn.items.ItemPizza;
import danger.orespawn.items.ItemRandomDungeon;
import danger.orespawn.items.ItemRayGun;
import danger.orespawn.items.ItemRock;
import danger.orespawn.items.ItemShoes;
import danger.orespawn.items.ItemSifter;
import danger.orespawn.items.ItemSkateBow;
import danger.orespawn.items.ItemSpiderRobotKit;
import danger.orespawn.items.ItemStep;
import danger.orespawn.items.ItemWaterBall;
import danger.orespawn.items.ItemWrench;
import danger.orespawn.items.ItemZooCage;
import danger.orespawn.items.ItemZooKeeper;
import danger.orespawn.items.food.ItemBacon;
import danger.orespawn.items.food.ItemButterCandy;
import danger.orespawn.items.food.ItemCrystalApple;
import danger.orespawn.items.food.ItemFireFish;
import danger.orespawn.items.food.ItemGenericFish;
import danger.orespawn.items.food.ItemLavaEel;
import danger.orespawn.items.food.ItemLove;
import danger.orespawn.items.food.ItemSparkFish;
import danger.orespawn.items.food.ItemSunFish;
import danger.orespawn.items.armor.ArmorBase;
import danger.orespawn.items.armor.OrespawnArmorMaterial;
import danger.orespawn.items.tools.EmeraldAxe;
import danger.orespawn.items.tools.EmeraldHoe;
import danger.orespawn.items.tools.EmeraldPickaxe;
import danger.orespawn.items.tools.EmeraldShovel;
import danger.orespawn.items.tools.EmeraldSword;
import danger.orespawn.items.tools.GenericAxe;
import danger.orespawn.items.tools.GenericHoe;
import danger.orespawn.items.tools.GenericPickaxe;
import danger.orespawn.items.tools.GenericShovel;
import danger.orespawn.items.tools.GenericSword;
import danger.orespawn.items.tools.OrespawnToolMaterial;
import danger.orespawn.items.tools.AmethystSword;
import danger.orespawn.items.tools.BattleAxe;
import danger.orespawn.items.tools.QueenBattleAxe;
import danger.orespawn.items.tools.SquidZooka;
import danger.orespawn.items.tools.ThunderStaff;
import danger.orespawn.items.tools.UltimateBow;
import danger.orespawn.items.tools.UltimateFishingRod;
import danger.orespawn.items.tools.Bertha;
import danger.orespawn.items.tools.Chainsaw;
import danger.orespawn.items.tools.Hammy;
import danger.orespawn.items.tools.Royal;
import danger.orespawn.items.tools.Slice;
import danger.orespawn.items.tools.ExperienceSword;
import danger.orespawn.items.tools.FairySword;
import danger.orespawn.items.tools.NightmareSword;
import danger.orespawn.items.tools.PoisonSword;
import danger.orespawn.items.tools.RatSword;
import danger.orespawn.items.tools.UltimateAxe;
import danger.orespawn.items.tools.UltimateHoe;
import danger.orespawn.items.tools.UltimatePickaxe;
import danger.orespawn.items.tools.UltimateShovel;
import danger.orespawn.items.tools.UltimateSword;
import danger.orespawn.util.Reference;
import java.util.function.Supplier;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.SwordItem;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.Nullable;

/**
 * Gold ModItems Ã¢â‚¬â€ core materials + real tools/armor (1:1 registry names & stats).
 */
public final class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Reference.MOD_ID);

    public static final DeferredItem<Item> URANIUM_NUGGET = basic("uranium_nugget");
    public static final DeferredItem<Item> TITANIUM_NUGGET = basic("titanium_nugget");
    public static final DeferredItem<Item> URANIUM_INGOT = basic("uranium_ingot");
    public static final DeferredItem<Item> TITANIUM_INGOT = basic("titanium_ingot");
    public static final DeferredItem<Item> TREX_TOOTH = basic("trextooth");
    public static final DeferredItem<Item> AMETHYST = basic("amethyst");
    public static final DeferredItem<Item> WORM_TOOTH = basic("worm_tooth");
    public static final DeferredItem<Item> WORM_FOOD = basic("worm_food");
    public static final DeferredItem<Item> MOTH_SCALE = basic("moth_scale");
    public static final DeferredItem<Item> MANTIS_CLAW = basic("mantis_claw");

    // Ã¢â‚¬â€Ã¢â‚¬â€Ã¢â‚¬â€ 1.7.10 crystal / ruby materials Ã¢â‚¬â€Ã¢â‚¬â€Ã¢â‚¬â€
    public static final DeferredItem<Item> CRYSTAL_PINK_INGOT = basic("crystal_pink_ingot");
    public static final DeferredItem<Item> CRYSTAL_STICKS = basic("crystal_sticks");
    public static final DeferredItem<Item> RUBY = basic("ruby");
    public static final DeferredItem<Item> NIGHTMARE_SCALE = basic("nightmare_scale");
    public static final DeferredItem<Item> TIGERSEYE_INGOT = basic("tigerseye_ingot");

    // Ã¢â‚¬â€Ã¢â‚¬â€Ã¢â‚¬â€ SET11 boss materials / drops (gold ItemSalt unlocalized names) Ã¢â‚¬â€Ã¢â‚¬â€Ã¢â‚¬â€
    public static final DeferredItem<Item> QUEEN_SCALE = basic("queenscale");
    public static final DeferredItem<Item> EMPEROR_SCORPION_SCALE = basic("emperorscorpionscale");
    public static final DeferredItem<Item> BASILISK_SCALE = basic("basiliskscale");
    public static final DeferredItem<Item> WATER_DRAGON_SCALE = basic("waterdragonscale");
    public static final DeferredItem<Item> PEACOCK_FEATHER = basic("peacockfeather");
    public static final DeferredItem<Item> JUMPY_BUG_SCALE = basic("jumpybugscale");
    public static final DeferredItem<Item> KRAKEN_TOOTH = basic("krakentooth");
    public static final DeferredItem<Item> GODZILLA_SCALE = basic("godzillascale");
    public static final DeferredItem<Item> GREEN_GOO = basic("greengoo");
    public static final DeferredItem<Item> BERTHA_HANDLE = basic("bbhandle");
    public static final DeferredItem<Item> BERTHA_GUARD = basic("bbguard");
    public static final DeferredItem<Item> BERTHA_BLADE = basic("bbblade");
    /** 1.7.10 MyBigHammer — craft ingredient for Hammy / Bertha handle; Hercules drop. */
    public static final DeferredItem<Item> BIG_HAMMER = basic("bighammer");
    public static final DeferredItem<Item> MOLENOID_NOSE = basic("molenoidnose");
    public static final DeferredItem<Item> SEA_MONSTER_SCALE = basic("seamonsterscale");
    public static final DeferredItem<Item> CATERKILLER_JAW = basic("caterkillerjaw");
    public static final DeferredItem<Item> SEA_VIPER_TONGUE = basic("seavipertongue");
    public static final DeferredItem<Item> VORTEX_EYE = basic("vortexeye");
    public static final DeferredItem<Item> DEAD_STINKBUG = basic("deadstinkbug");
    public static final DeferredItem<Item> SALT = basic("salt");

    // Ã¢â‚¬â€Ã¢â‚¬â€Ã¢â‚¬â€ Ultimate tools (gold damage / speed + auto-enchants) Ã¢â‚¬â€Ã¢â‚¬â€Ã¢â‚¬â€
    public static final DeferredItem<Item> ULTIMATE_SWORD = ITEMS.register(
            "ultimate_sword",
            () -> new UltimateSword(ultimateToolProps(
                    SwordItem.createAttributes(
                            OrespawnToolMaterial.UltimateTools.tier,
                            OrespawnToolMaterial.UltimateTools.swordDamageBonus(),
                            OrespawnToolMaterial.UltimateTools.swordAttackSpeed()))));
    public static final DeferredItem<Item> ULTIMATE_PICKAXE = ITEMS.register(
            "ultimate_pickaxe",
            () -> new UltimatePickaxe(ultimateToolProps(
                    PickaxeItem.createAttributes(
                            OrespawnToolMaterial.UltimateTools.tier,
                            OrespawnToolMaterial.UltimateTools.pickaxeDamageBonus(),
                            OrespawnToolMaterial.UltimateTools.pickaxeAttackSpeed()))));
    public static final DeferredItem<Item> ULTIMATE_AXE = ITEMS.register(
            "ultimate_axe",
            () -> new UltimateAxe(ultimateToolProps(
                    AxeItem.createAttributes(
                            OrespawnToolMaterial.UltimateTools.tier,
                            OrespawnToolMaterial.UltimateTools.axeDamageBonus(),
                            OrespawnToolMaterial.UltimateTools.axeAttackSpeed()))));
    public static final DeferredItem<Item> ULTIMATE_SHOVEL = ITEMS.register(
            "ultimate_shovel",
            () -> new UltimateShovel(ultimateToolProps(
                    ShovelItem.createAttributes(
                            OrespawnToolMaterial.UltimateTools.tier,
                            OrespawnToolMaterial.UltimateTools.shovelDamageBonus(),
                            OrespawnToolMaterial.UltimateTools.shovelAttackSpeed()))));
    public static final DeferredItem<Item> ULTIMATE_HOE = ITEMS.register(
            "ultimate_hoe",
            () -> new UltimateHoe(ultimateToolProps(
                    HoeItem.createAttributes(
                            OrespawnToolMaterial.UltimateTools.tier,
                            OrespawnToolMaterial.UltimateTools.hoeDamageBonus(),
                            OrespawnToolMaterial.UltimateTools.hoeAttackSpeed()))));

    // Ã¢â‚¬â€Ã¢â‚¬â€Ã¢â‚¬â€ 1.7.10 signature / special swords Ã¢â‚¬â€Ã¢â‚¬â€Ã¢â‚¬â€
    public static final DeferredItem<Item> BERTHA = ITEMS.register(
            "bertha",
            () -> new Bertha(ultimateToolProps(
                    SwordItem.createAttributes(
                            OrespawnToolMaterial.BerthaTools.tier,
                            OrespawnToolMaterial.BerthaTools.swordDamageBonus(),
                            OrespawnToolMaterial.BerthaTools.swordAttackSpeed()))));
    /** 1.7.10 MySlice Ã¢â‚¬â€ Bertha combat + ModelSlice / RenderSlice presentation */
    public static final DeferredItem<Item> SLICE = ITEMS.register(
            "slice",
            () -> new Slice(ultimateToolProps(
                    SwordItem.createAttributes(
                            OrespawnToolMaterial.BerthaTools.tier,
                            OrespawnToolMaterial.BerthaTools.swordDamageBonus(),
                            OrespawnToolMaterial.BerthaTools.swordAttackSpeed()))));
    /** 1.7.10 MyRoyal Ã¢â‚¬â€ Unbreaking 5 + hit_type 2; ModelRoyal GOLD_SCALE 5.6 */
    public static final DeferredItem<Item> ROYAL = ITEMS.register(
            "royal",
            () -> new Royal(new Item.Properties()
                    .rarity(Rarity.EPIC)
                    .durability(Royal.ROYAL_USES)
                    .attributes(SwordItem.createAttributes(Royal.ROYAL_TIER, 3.0F, -2.4F))));
    /** 1.7.10 MyHammy / Attitude Adjuster Ã¢â‚¬â€ hit_type 3; ModelHammy GOLD_SCALE 2.4 */
    public static final DeferredItem<Item> HAMMY = ITEMS.register(
            "hammy",
            () -> new Hammy(new Item.Properties()
                    .rarity(Rarity.EPIC)
                    .durability(Hammy.HAMMY_USES)
                    .attributes(SwordItem.createAttributes(Hammy.HAMMY_TIER, 3.0F, -2.4F))));
    /** 1.7.10 MyBattleAxe Ã¢â‚¬â€ UltimateSword family; GOLD_SCALE 5.6 */
    public static final DeferredItem<Item> BATTLE_AXE = ITEMS.register(
            "battle_axe",
            () -> new BattleAxe(new Item.Properties()
                    .rarity(Rarity.RARE)
                    .durability(BattleAxe.BATTLE_AXE_USES)
                    .attributes(SwordItem.createAttributes(BattleAxe.BATTLE_AXE_TIER, 3.0F, -2.4F))));
    /** 1.7.10 MyChainsaw Ã¢â‚¬â€ UltimateSword family; GOLD_SCALE 4.0 */
    public static final DeferredItem<Item> CHAINSAW = ITEMS.register(
            "chainsaw",
            () -> new Chainsaw(new Item.Properties()
                    .rarity(Rarity.RARE)
                    .durability(Chainsaw.CHAINSAW_USES)
                    .attributes(SwordItem.createAttributes(Chainsaw.CHAINSAW_TIER, 3.0F, -2.4F))));
    /** 1.7.10 MyQueenBattleAxe Ã¢â‚¬â€ UltimateSword family; GOLD_SCALE 5.6 */
    public static final DeferredItem<Item> QUEEN_BATTLE_AXE = ITEMS.register(
            "queen_battle_axe",
            () -> new QueenBattleAxe(new Item.Properties()
                    .rarity(Rarity.EPIC)
                    .fireResistant()
                    .durability(QueenBattleAxe.QUEEN_BATTLE_AXE_USES)
                    .attributes(SwordItem.createAttributes(QueenBattleAxe.QUEEN_BATTLE_AXE_TIER, 3.0F, -2.4F))));
    /** 1.7.10 MyUltimateBow Ã¢â‚¬â€ fires UltimateArrow velocity 3.0 */
    public static final DeferredItem<Item> ULTIMATE_BOW = ITEMS.register(
            "ultimate_bow",
            () -> new UltimateBow(new Item.Properties()
                    .rarity(Rarity.EPIC)
                    .stacksTo(1)
                    .durability(UltimateBow.ULTIMATE_BOW_USES)));
    /** 1.7.10 UltimateFishingRod Ã¢â‚¬â€ cast UltimateFishHook */
    public static final DeferredItem<Item> ULTIMATE_FISHING_ROD = ITEMS.register(
            "ultimate_fishing_rod",
            () -> new UltimateFishingRod(new Item.Properties()
                    .rarity(Rarity.EPIC)
                    .stacksTo(1)
                    .durability(UltimateFishingRod.ULTIMATE_FISHING_ROD_USES)));
    /** 1.7.10 MyThunderStaff Ã¢â‚¬â€ fires ThunderBolt */
    public static final DeferredItem<Item> THUNDER_STAFF = ITEMS.register(
            "thunder_staff",
            () -> new ThunderStaff(new Item.Properties()
                    .rarity(Rarity.RARE)
                    .stacksTo(1)
                    .durability(ThunderStaff.THUNDER_STAFF_USES)));
    /** 1.7.10 MySquidZooka Ã¢â‚¬â€ spawns AttackSquid; BEWLR GOLD_SCALE 5.6 */
    public static final DeferredItem<Item> SQUID_ZOOKA = ITEMS.register(
            "squid_zooka",
            () -> new SquidZooka(new Item.Properties()
                    .rarity(Rarity.RARE)
                    .stacksTo(1)
                    .durability(SquidZooka.SQUID_ZOOKA_USES)));

    // Ã¢â‚¬â€Ã¢â‚¬â€Ã¢â‚¬â€ SET10 throwables / utility (gold ItemRock family, balls, elevator, shoes) Ã¢â‚¬â€Ã¢â‚¬â€Ã¢â‚¬â€
    public static final DeferredItem<Item> ROCK_SMALL =
            ITEMS.register("rocksmall", () -> new ItemRock(new Item.Properties().stacksTo(64), 1));
    public static final DeferredItem<Item> ROCK =
            ITEMS.register("rock", () -> new ItemRock(new Item.Properties().stacksTo(64), 2));
    public static final DeferredItem<Item> ROCK_RED =
            ITEMS.register("rockred", () -> new ItemRock(new Item.Properties().stacksTo(64), 3));
    public static final DeferredItem<Item> ROCK_GREEN =
            ITEMS.register("rockgreen", () -> new ItemRock(new Item.Properties().stacksTo(64), 4));
    public static final DeferredItem<Item> ROCK_BLUE =
            ITEMS.register("rockblue", () -> new ItemRock(new Item.Properties().stacksTo(64), 5));
    public static final DeferredItem<Item> ROCK_PURPLE =
            ITEMS.register("rockpurple", () -> new ItemRock(new Item.Properties().stacksTo(64), 6));
    public static final DeferredItem<Item> ROCK_SPIKEY =
            ITEMS.register("rockspikey", () -> new ItemRock(new Item.Properties().stacksTo(64), 7));
    public static final DeferredItem<Item> ROCK_TNT =
            ITEMS.register("rocktnt", () -> new ItemRock(new Item.Properties().stacksTo(64), 8));
    public static final DeferredItem<Item> ROCK_CRYSTAL_RED =
            ITEMS.register("rockcrystalred", () -> new ItemRock(new Item.Properties().stacksTo(64), 9));
    public static final DeferredItem<Item> ROCK_CRYSTAL_GREEN =
            ITEMS.register("rockcrystalgreen", () -> new ItemRock(new Item.Properties().stacksTo(64), 10));
    public static final DeferredItem<Item> ROCK_CRYSTAL_BLUE =
            ITEMS.register("rockcrystalblue", () -> new ItemRock(new Item.Properties().stacksTo(64), 11));
    public static final DeferredItem<Item> ROCK_CRYSTAL_TNT =
            ITEMS.register("rockcrystaltnt", () -> new ItemRock(new Item.Properties().stacksTo(64), 12));
    public static final DeferredItem<Item> LASER_BALL =
            ITEMS.register("laserball", () -> new ItemLaserBall(new Item.Properties().stacksTo(64)));
    public static final DeferredItem<Item> WATER_BALL =
            ITEMS.register("waterball", () -> new ItemWaterBall(new Item.Properties().stacksTo(64)));
    public static final DeferredItem<Item> ICE_BALL =
            ITEMS.register("iceball", () -> new ItemIceBall(new Item.Properties().stacksTo(64)));
    /** Gold MyElevator Ã¢â‚¬â€ places hoverboard entity */
    public static final DeferredItem<Item> ELEVATOR =
            ITEMS.register("elevator", () -> new ItemElevator(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> REDHEELS =
            ITEMS.register("redheels", () -> new ItemShoes(new Item.Properties().stacksTo(64), 2));
    public static final DeferredItem<Item> BLACKHEELS =
            ITEMS.register("blackheels", () -> new ItemShoes(new Item.Properties().stacksTo(64), 3));
    public static final DeferredItem<Item> SLIPPERS =
            ITEMS.register("slippers", () -> new ItemShoes(new Item.Properties().stacksTo(64), 4));
    public static final DeferredItem<Item> BOOTS_THROW =
            ITEMS.register("boots_throw", () -> new ItemShoes(new Item.Properties().stacksTo(64), 5));
    public static final DeferredItem<Item> GAMECONTROLLER =
            ITEMS.register("gamecontroller", () -> new ItemShoes(new Item.Properties().stacksTo(64), 6));

    // Ã¢â‚¬â€Ã¢â‚¬â€Ã¢â‚¬â€ SET10 QoL Ã¢â‚¬â€Ã¢â‚¬â€Ã¢â‚¬â€
    public static final DeferredItem<Item> MINERS_DREAM =
            ITEMS.register("miners_dream", () -> new ItemMinersDream(new Item.Properties().stacksTo(16)));
    public static final DeferredItem<Item> SIFTER =
            ITEMS.register("sifter", () -> new ItemSifter(new Item.Properties().stacksTo(1).durability(ItemSifter.SIFTER_USES)));
    public static final DeferredItem<Item> SPIDER_ROBOT_KIT = ITEMS.register(
            "spider_robot_kit",
            () -> new ItemSpiderRobotKit(
                    new Item.Properties().stacksTo(1).durability((int) SpiderRobot.GOLD_HEALTH),
                    ItemSpiderRobotKit.KitKind.SPIDER));
    public static final DeferredItem<Item> ANT_ROBOT_KIT = ITEMS.register(
            "ant_robot_kit",
            () -> new ItemSpiderRobotKit(
                    new Item.Properties().stacksTo(1).durability((int) AntRobot.GOLD_HEALTH),
                    ItemSpiderRobotKit.KitKind.ANT));
    public static final DeferredItem<Item> WRENCH =
            ITEMS.register("wrench", () -> new ItemWrench(new Item.Properties().stacksTo(1).durability(ItemWrench.WRENCH_USES)));
    public static final DeferredItem<Item> ZOO_KEEPER =
            ITEMS.register("zoo_keeper", () -> new ItemZooKeeper(new Item.Properties().stacksTo(1).durability(ItemZooKeeper.ZOOKEEPER_USES)));

    // Ã¢â‚¬â€Ã¢â‚¬â€Ã¢â‚¬â€ SET11 utility / combat items Ã¢â‚¬â€Ã¢â‚¬â€Ã¢â‚¬â€
    public static final DeferredItem<Item> RAY_GUN = ITEMS.register(
            "raygun",
            () -> new ItemRayGun(new Item.Properties().stacksTo(1).durability(ItemRayGun.RAYGUN_USES)));
    /** Gold {@code MyIrukandji} / deadirukandji — throwable 100-dmg projectile. */
    public static final DeferredItem<Item> DEAD_IRUKANDJI = ITEMS.register(
            "deadirukandji",
            () -> new ItemDeadIrukandji(new Item.Properties().stacksTo(64)));
    /** Gold {@code MyIrukandjiArrow} — SkateBow ammo. */
    public static final DeferredItem<Item> IRUKANDJI_ARROW = ITEMS.register(
            "irukandjiarrow",
            () -> new ItemIrukandjiArrow(new Item.Properties().stacksTo(64)));
    /** Gold {@code MySkateBow} — fires IrukandjiArrow (100 dmg). */
    public static final DeferredItem<Item> SKATE_BOW = ITEMS.register(
            "skatebow",
            () -> new ItemSkateBow(new Item.Properties().stacksTo(1).durability(ItemSkateBow.SKATE_BOW_USES)));
    /** Gold {@code MyExperienceCatcher} — converts nearby XP orbs into bottles. */
    public static final DeferredItem<Item> EXPERIENCE_CATCHER = ITEMS.register(
            "experiencecatcher",
            () -> new ItemExperienceCatcher(new Item.Properties().stacksTo(ItemExperienceCatcher.MAX_STACK)));
    /** Gold {@code MyAppleSeed} — grows apple-leaf tree on grass/dirt. */
    public static final DeferredItem<Item> APPLE_TREE_SEED = ITEMS.register(
            "appletree_seed",
            () -> new ItemFruitTreeSeed(
                    new Item.Properties().stacksTo(ItemFruitTreeSeed.MAX_STACK), ItemFruitTreeSeed.Kind.APPLE));
    /** Gold {@code MyExperienceTreeSeed} — grows experience tree on grass/dirt. */
    public static final DeferredItem<Item> EXPERIENCE_TREE_SEED = ITEMS.register(
            "experiencetree_seed",
            () -> new ItemExperienceTreeSeed(
                    new Item.Properties().stacksTo(ItemExperienceTreeSeed.MAX_STACK)));
    /** Gold {@code MyCherry} unlocalized cherries. */
    public static final DeferredItem<Item> CHERRIES = foodItem("cherries", 3, 0.45F);
    /** Gold {@code MyPeach}. */
    public static final DeferredItem<Item> PEACH = foodItem("peach", 4, 0.55F);
    public static final DeferredItem<Item> CHERRY_TREE_SEED = ITEMS.register(
            "cherrytree_seed",
            () -> new ItemFruitTreeSeed(
                    new Item.Properties().stacksTo(ItemFruitTreeSeed.MAX_STACK), ItemFruitTreeSeed.Kind.CHERRY));
    public static final DeferredItem<Item> PEACH_TREE_SEED = ITEMS.register(
            "peachtree_seed",
            () -> new ItemFruitTreeSeed(
                    new Item.Properties().stacksTo(ItemFruitTreeSeed.MAX_STACK), ItemFruitTreeSeed.Kind.PEACH));
    /** Gold {@code MyStepUp} / StepDown / StepAccross — cobble path builders. */
    public static final DeferredItem<Item> STEP_UP = ITEMS.register(
            "step_up",
            () -> new ItemStep(new Item.Properties().stacksTo(ItemStep.MAX_STACK), ItemStep.Kind.UP));
    public static final DeferredItem<Item> STEP_DOWN = ITEMS.register(
            "step_down",
            () -> new ItemStep(new Item.Properties().stacksTo(ItemStep.MAX_STACK), ItemStep.Kind.DOWN));
    public static final DeferredItem<Item> STEP_ACCROSS = ITEMS.register(
            "step_accross",
            () -> new ItemStep(new Item.Properties().stacksTo(ItemStep.MAX_STACK), ItemStep.Kind.ACROSS));
    /**
     * Gold {@code NetherLost} — display name "Nether Tracker".
     * Held in Nether converts end portal frame underfoot → iron block.
     */
    public static final DeferredItem<Item> NETHER_LOST = ITEMS.register(
            "netherlost",
            () -> new ItemNetherLost(new Item.Properties().stacksTo(1).durability(ItemNetherLost.NETHER_LOST_USES)));
    /** Gold {@code ZooCage2/4/6/8/10} — iron floor/roof + glass walls (sizes 3/5/9/13/17). */
    public static final DeferredItem<Item> ZOO2 = ITEMS.register(
            "zoo2", () -> new ItemZooCage(new Item.Properties().stacksTo(ItemZooCage.MAX_STACK), 3));
    public static final DeferredItem<Item> ZOO4 = ITEMS.register(
            "zoo4", () -> new ItemZooCage(new Item.Properties().stacksTo(ItemZooCage.MAX_STACK), 5));
    public static final DeferredItem<Item> ZOO6 = ITEMS.register(
            "zoo6", () -> new ItemZooCage(new Item.Properties().stacksTo(ItemZooCage.MAX_STACK), 9));
    public static final DeferredItem<Item> ZOO8 = ITEMS.register(
            "zoo8", () -> new ItemZooCage(new Item.Properties().stacksTo(ItemZooCage.MAX_STACK), 13));
    public static final DeferredItem<Item> ZOO10 = ITEMS.register(
            "zoo10", () -> new ItemZooCage(new Item.Properties().stacksTo(ItemZooCage.MAX_STACK), 17));
    public static final DeferredItem<Item> ACID = ITEMS.register(
            "acid",
            () -> new ItemAcid(new Item.Properties().stacksTo(ItemAcid.MAX_STACK)));
    public static final DeferredItem<Item> CREEPER_LAUNCHER = ITEMS.register(
            "creeperlauncher",
            () -> new ItemCreeperLauncher(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> INSTANT_SHELTER = ITEMS.register(
            "instantshelter",
            () -> new ItemInstantShelter(new Item.Properties().stacksTo(ItemInstantShelter.MAX_STACK)));
    public static final DeferredItem<Item> INSTANT_GARDEN = ITEMS.register(
            "instantgarden",
            () -> new ItemInstantGarden(new Item.Properties().stacksTo(ItemInstantGarden.MAX_STACK)));
    /** Gold ItemRandomDungeon Ã¢â‚¬â€ places random ported structure. */
    public static final DeferredItem<Item> RANDOM_DUNGEON = ITEMS.register(
            "randomdungeon",
            () -> new ItemRandomDungeon(new Item.Properties().stacksTo(ItemRandomDungeon.MAX_STACK)));

    public static final DeferredItem<Item> NIGHTMARE_SWORD = ITEMS.register(
            "nightmare_sword",
            () -> new NightmareSword(new Item.Properties().rarity(Rarity.RARE).attributes(
                    SwordItem.createAttributes(
                            OrespawnToolMaterial.NightmareTools.tier,
                            OrespawnToolMaterial.NightmareTools.swordDamageBonus(),
                            OrespawnToolMaterial.NightmareTools.swordAttackSpeed()))));
    public static final DeferredItem<Item> POISON_SWORD = ITEMS.register(
            "poison_sword",
            () -> new PoisonSword(toolProps(
                    OrespawnToolMaterial.EmeraldTools,
                    SwordItem.createAttributes(
                            OrespawnToolMaterial.EmeraldTools.tier,
                            OrespawnToolMaterial.EmeraldTools.swordDamageBonus(),
                            OrespawnToolMaterial.EmeraldTools.swordAttackSpeed()))));
    public static final DeferredItem<Item> EXPERIENCE_SWORD = ITEMS.register(
            "experience_sword",
            () -> new ExperienceSword(toolProps(
                    OrespawnToolMaterial.EmeraldTools,
                    SwordItem.createAttributes(
                            OrespawnToolMaterial.EmeraldTools.tier,
                            OrespawnToolMaterial.EmeraldTools.swordDamageBonus(),
                            OrespawnToolMaterial.EmeraldTools.swordAttackSpeed()))));
    public static final DeferredItem<Item> RAT_SWORD = ITEMS.register(
            "rat_sword",
            () -> new RatSword(toolProps(
                    OrespawnToolMaterial.EmeraldTools,
                    SwordItem.createAttributes(
                            OrespawnToolMaterial.EmeraldTools.tier,
                            OrespawnToolMaterial.EmeraldTools.swordDamageBonus(),
                            OrespawnToolMaterial.EmeraldTools.swordAttackSpeed()))));
    public static final DeferredItem<Item> FAIRY_SWORD = ITEMS.register(
            "fairy_sword",
            () -> new FairySword(toolProps(
                    OrespawnToolMaterial.EmeraldTools,
                    SwordItem.createAttributes(
                            OrespawnToolMaterial.EmeraldTools.tier,
                            OrespawnToolMaterial.EmeraldTools.swordDamageBonus(),
                            OrespawnToolMaterial.EmeraldTools.swordAttackSpeed()))));

    // Ã¢â‚¬â€Ã¢â‚¬â€Ã¢â‚¬â€ Crystal pink tools (1.7.10 Pink set) Ã¢â‚¬â€Ã¢â‚¬â€Ã¢â‚¬â€
    public static final DeferredItem<Item> CRYSTAL_PINK_SWORD = ITEMS.register(
            "crystal_pink_sword",
            () -> new GenericSword(
                    OrespawnToolMaterial.CrystalPinkTools,
                    toolProps(
                            OrespawnToolMaterial.CrystalPinkTools,
                            SwordItem.createAttributes(
                                    OrespawnToolMaterial.CrystalPinkTools.tier,
                                    OrespawnToolMaterial.CrystalPinkTools.swordDamageBonus(),
                                    OrespawnToolMaterial.CrystalPinkTools.swordAttackSpeed()))));
    public static final DeferredItem<Item> CRYSTAL_PINK_PICKAXE = ITEMS.register(
            "crystal_pink_pickaxe",
            () -> new GenericPickaxe(
                    OrespawnToolMaterial.CrystalPinkTools,
                    toolProps(
                            OrespawnToolMaterial.CrystalPinkTools,
                            PickaxeItem.createAttributes(
                                    OrespawnToolMaterial.CrystalPinkTools.tier,
                                    OrespawnToolMaterial.CrystalPinkTools.pickaxeDamageBonus(),
                                    OrespawnToolMaterial.CrystalPinkTools.pickaxeAttackSpeed()))));
    public static final DeferredItem<Item> CRYSTAL_PINK_AXE = ITEMS.register(
            "crystal_pink_axe",
            () -> new GenericAxe(
                    OrespawnToolMaterial.CrystalPinkTools,
                    toolProps(
                            OrespawnToolMaterial.CrystalPinkTools,
                            AxeItem.createAttributes(
                                    OrespawnToolMaterial.CrystalPinkTools.tier,
                                    OrespawnToolMaterial.CrystalPinkTools.axeDamageBonus(),
                                    OrespawnToolMaterial.CrystalPinkTools.axeAttackSpeed()))));
    public static final DeferredItem<Item> CRYSTAL_PINK_SHOVEL = ITEMS.register(
            "crystal_pink_shovel",
            () -> new GenericShovel(
                    OrespawnToolMaterial.CrystalPinkTools,
                    toolProps(
                            OrespawnToolMaterial.CrystalPinkTools,
                            ShovelItem.createAttributes(
                                    OrespawnToolMaterial.CrystalPinkTools.tier,
                                    OrespawnToolMaterial.CrystalPinkTools.shovelDamageBonus(),
                                    OrespawnToolMaterial.CrystalPinkTools.shovelAttackSpeed()))));
    public static final DeferredItem<Item> CRYSTAL_PINK_HOE = ITEMS.register(
            "crystal_pink_hoe",
            () -> new GenericHoe(
                    OrespawnToolMaterial.CrystalPinkTools,
                    toolProps(
                            OrespawnToolMaterial.CrystalPinkTools,
                            HoeItem.createAttributes(
                                    OrespawnToolMaterial.CrystalPinkTools.tier,
                                    OrespawnToolMaterial.CrystalPinkTools.hoeDamageBonus(),
                                    OrespawnToolMaterial.CrystalPinkTools.hoeAttackSpeed()))));

    // Ã¢â‚¬â€Ã¢â‚¬â€Ã¢â‚¬â€ Ruby tools (1.7.10) Ã¢â‚¬â€Ã¢â‚¬â€Ã¢â‚¬â€
    public static final DeferredItem<Item> RUBY_SWORD = ITEMS.register(
            "ruby_sword",
            () -> new GenericSword(
                    OrespawnToolMaterial.RubyTools,
                    toolProps(
                            OrespawnToolMaterial.RubyTools,
                            SwordItem.createAttributes(
                                    OrespawnToolMaterial.RubyTools.tier,
                                    OrespawnToolMaterial.RubyTools.swordDamageBonus(),
                                    OrespawnToolMaterial.RubyTools.swordAttackSpeed()))));
    public static final DeferredItem<Item> RUBY_PICKAXE = ITEMS.register(
            "ruby_pickaxe",
            () -> new GenericPickaxe(
                    OrespawnToolMaterial.RubyTools,
                    toolProps(
                            OrespawnToolMaterial.RubyTools,
                            PickaxeItem.createAttributes(
                                    OrespawnToolMaterial.RubyTools.tier,
                                    OrespawnToolMaterial.RubyTools.pickaxeDamageBonus(),
                                    OrespawnToolMaterial.RubyTools.pickaxeAttackSpeed()))));
    public static final DeferredItem<Item> RUBY_AXE = ITEMS.register(
            "ruby_axe",
            () -> new GenericAxe(
                    OrespawnToolMaterial.RubyTools,
                    toolProps(
                            OrespawnToolMaterial.RubyTools,
                            AxeItem.createAttributes(
                                    OrespawnToolMaterial.RubyTools.tier,
                                    OrespawnToolMaterial.RubyTools.axeDamageBonus(),
                                    OrespawnToolMaterial.RubyTools.axeAttackSpeed()))));
    // Ã¢â‚¬â€Ã¢â‚¬â€Ã¢â‚¬â€ Tiger's Eye tools (1.7.10 TigersEye: 4 / 1600 / 12 / 8 / 75) Ã¢â‚¬â€Ã¢â‚¬â€Ã¢â‚¬â€
    public static final DeferredItem<Item> TIGERSEYE_SWORD = ITEMS.register(
            "tigerseye_sword",
            () -> new GenericSword(
                    OrespawnToolMaterial.TigersEyeTools,
                    toolProps(
                            OrespawnToolMaterial.TigersEyeTools,
                            SwordItem.createAttributes(
                                    OrespawnToolMaterial.TigersEyeTools.tier,
                                    OrespawnToolMaterial.TigersEyeTools.swordDamageBonus(),
                                    OrespawnToolMaterial.TigersEyeTools.swordAttackSpeed()))));
    public static final DeferredItem<Item> TIGERSEYE_PICKAXE = ITEMS.register(
            "tigerseye_pickaxe",
            () -> new GenericPickaxe(
                    OrespawnToolMaterial.TigersEyeTools,
                    toolProps(
                            OrespawnToolMaterial.TigersEyeTools,
                            PickaxeItem.createAttributes(
                                    OrespawnToolMaterial.TigersEyeTools.tier,
                                    OrespawnToolMaterial.TigersEyeTools.pickaxeDamageBonus(),
                                    OrespawnToolMaterial.TigersEyeTools.pickaxeAttackSpeed()))));
    public static final DeferredItem<Item> TIGERSEYE_AXE = ITEMS.register(
            "tigerseye_axe",
            () -> new GenericAxe(
                    OrespawnToolMaterial.TigersEyeTools,
                    toolProps(
                            OrespawnToolMaterial.TigersEyeTools,
                            AxeItem.createAttributes(
                                    OrespawnToolMaterial.TigersEyeTools.tier,
                                    OrespawnToolMaterial.TigersEyeTools.axeDamageBonus(),
                                    OrespawnToolMaterial.TigersEyeTools.axeAttackSpeed()))));
    public static final DeferredItem<Item> TIGERSEYE_SHOVEL = ITEMS.register(
            "tigerseye_shovel",
            () -> new GenericShovel(
                    OrespawnToolMaterial.TigersEyeTools,
                    toolProps(
                            OrespawnToolMaterial.TigersEyeTools,
                            ShovelItem.createAttributes(
                                    OrespawnToolMaterial.TigersEyeTools.tier,
                                    OrespawnToolMaterial.TigersEyeTools.shovelDamageBonus(),
                                    OrespawnToolMaterial.TigersEyeTools.shovelAttackSpeed()))));
    public static final DeferredItem<Item> TIGERSEYE_HOE = ITEMS.register(
            "tigerseye_hoe",
            () -> new GenericHoe(
                    OrespawnToolMaterial.TigersEyeTools,
                    toolProps(
                            OrespawnToolMaterial.TigersEyeTools,
                            HoeItem.createAttributes(
                                    OrespawnToolMaterial.TigersEyeTools.tier,
                                    OrespawnToolMaterial.TigersEyeTools.hoeDamageBonus(),
                                    OrespawnToolMaterial.TigersEyeTools.hoeAttackSpeed()))));

    // Ã¢â‚¬â€Ã¢â‚¬â€Ã¢â‚¬â€ Crystal wood tools (gold crystalwood*, sticks = crystal_sticks) Ã¢â‚¬â€Ã¢â‚¬â€Ã¢â‚¬â€
    public static final DeferredItem<Item> CRYSTALWOOD_SWORD = ITEMS.register(
            "crystalwoodsword",
            () -> new GenericSword(
                    OrespawnToolMaterial.CrystalWoodTools,
                    toolProps(
                            OrespawnToolMaterial.CrystalWoodTools,
                            SwordItem.createAttributes(
                                    OrespawnToolMaterial.CrystalWoodTools.tier,
                                    OrespawnToolMaterial.CrystalWoodTools.swordDamageBonus(),
                                    OrespawnToolMaterial.CrystalWoodTools.swordAttackSpeed()))));
    public static final DeferredItem<Item> CRYSTALWOOD_PICKAXE = ITEMS.register(
            "crystalwoodpickaxe",
            () -> new GenericPickaxe(
                    OrespawnToolMaterial.CrystalWoodTools,
                    toolProps(
                            OrespawnToolMaterial.CrystalWoodTools,
                            PickaxeItem.createAttributes(
                                    OrespawnToolMaterial.CrystalWoodTools.tier,
                                    OrespawnToolMaterial.CrystalWoodTools.pickaxeDamageBonus(),
                                    OrespawnToolMaterial.CrystalWoodTools.pickaxeAttackSpeed()))));
    public static final DeferredItem<Item> CRYSTALWOOD_AXE = ITEMS.register(
            "crystalwoodaxe",
            () -> new GenericAxe(
                    OrespawnToolMaterial.CrystalWoodTools,
                    toolProps(
                            OrespawnToolMaterial.CrystalWoodTools,
                            AxeItem.createAttributes(
                                    OrespawnToolMaterial.CrystalWoodTools.tier,
                                    OrespawnToolMaterial.CrystalWoodTools.axeDamageBonus(),
                                    OrespawnToolMaterial.CrystalWoodTools.axeAttackSpeed()))));
    public static final DeferredItem<Item> CRYSTALWOOD_SHOVEL = ITEMS.register(
            "crystalwoodshovel",
            () -> new GenericShovel(
                    OrespawnToolMaterial.CrystalWoodTools,
                    toolProps(
                            OrespawnToolMaterial.CrystalWoodTools,
                            ShovelItem.createAttributes(
                                    OrespawnToolMaterial.CrystalWoodTools.tier,
                                    OrespawnToolMaterial.CrystalWoodTools.shovelDamageBonus(),
                                    OrespawnToolMaterial.CrystalWoodTools.shovelAttackSpeed()))));
    public static final DeferredItem<Item> CRYSTALWOOD_HOE = ITEMS.register(
            "crystalwoodhoe",
            () -> new GenericHoe(
                    OrespawnToolMaterial.CrystalWoodTools,
                    toolProps(
                            OrespawnToolMaterial.CrystalWoodTools,
                            HoeItem.createAttributes(
                                    OrespawnToolMaterial.CrystalWoodTools.tier,
                                    OrespawnToolMaterial.CrystalWoodTools.hoeDamageBonus(),
                                    OrespawnToolMaterial.CrystalWoodTools.hoeAttackSpeed()))));

    // Ã¢â‚¬â€Ã¢â‚¬â€Ã¢â‚¬â€ Crystal stone tools (gold crystalstone*) Ã¢â‚¬â€Ã¢â‚¬â€Ã¢â‚¬â€
    public static final DeferredItem<Item> CRYSTALSTONE_SWORD = ITEMS.register(
            "crystalstonesword",
            () -> new GenericSword(
                    OrespawnToolMaterial.CrystalStoneTools,
                    toolProps(
                            OrespawnToolMaterial.CrystalStoneTools,
                            SwordItem.createAttributes(
                                    OrespawnToolMaterial.CrystalStoneTools.tier,
                                    OrespawnToolMaterial.CrystalStoneTools.swordDamageBonus(),
                                    OrespawnToolMaterial.CrystalStoneTools.swordAttackSpeed()))));
    public static final DeferredItem<Item> CRYSTALSTONE_PICKAXE = ITEMS.register(
            "crystalstonepickaxe",
            () -> new GenericPickaxe(
                    OrespawnToolMaterial.CrystalStoneTools,
                    toolProps(
                            OrespawnToolMaterial.CrystalStoneTools,
                            PickaxeItem.createAttributes(
                                    OrespawnToolMaterial.CrystalStoneTools.tier,
                                    OrespawnToolMaterial.CrystalStoneTools.pickaxeDamageBonus(),
                                    OrespawnToolMaterial.CrystalStoneTools.pickaxeAttackSpeed()))));
    public static final DeferredItem<Item> CRYSTALSTONE_AXE = ITEMS.register(
            "crystalstoneaxe",
            () -> new GenericAxe(
                    OrespawnToolMaterial.CrystalStoneTools,
                    toolProps(
                            OrespawnToolMaterial.CrystalStoneTools,
                            AxeItem.createAttributes(
                                    OrespawnToolMaterial.CrystalStoneTools.tier,
                                    OrespawnToolMaterial.CrystalStoneTools.axeDamageBonus(),
                                    OrespawnToolMaterial.CrystalStoneTools.axeAttackSpeed()))));
    public static final DeferredItem<Item> CRYSTALSTONE_SHOVEL = ITEMS.register(
            "crystalstoneshovel",
            () -> new GenericShovel(
                    OrespawnToolMaterial.CrystalStoneTools,
                    toolProps(
                            OrespawnToolMaterial.CrystalStoneTools,
                            ShovelItem.createAttributes(
                                    OrespawnToolMaterial.CrystalStoneTools.tier,
                                    OrespawnToolMaterial.CrystalStoneTools.shovelDamageBonus(),
                                    OrespawnToolMaterial.CrystalStoneTools.shovelAttackSpeed()))));
    public static final DeferredItem<Item> CRYSTALSTONE_HOE = ITEMS.register(
            "crystalstonehoe",
            () -> new GenericHoe(
                    OrespawnToolMaterial.CrystalStoneTools,
                    toolProps(
                            OrespawnToolMaterial.CrystalStoneTools,
                            HoeItem.createAttributes(
                                    OrespawnToolMaterial.CrystalStoneTools.tier,
                                    OrespawnToolMaterial.CrystalStoneTools.hoeDamageBonus(),
                                    OrespawnToolMaterial.CrystalStoneTools.hoeAttackSpeed()))));

    public static final DeferredItem<Item> RUBY_SHOVEL = ITEMS.register(
            "ruby_shovel",
            () -> new GenericShovel(
                    OrespawnToolMaterial.RubyTools,
                    toolProps(
                            OrespawnToolMaterial.RubyTools,
                            ShovelItem.createAttributes(
                                    OrespawnToolMaterial.RubyTools.tier,
                                    OrespawnToolMaterial.RubyTools.shovelDamageBonus(),
                                    OrespawnToolMaterial.RubyTools.shovelAttackSpeed()))));
    public static final DeferredItem<Item> RUBY_HOE = ITEMS.register(
            "ruby_hoe",
            () -> new GenericHoe(
                    OrespawnToolMaterial.RubyTools,
                    toolProps(
                            OrespawnToolMaterial.RubyTools,
                            HoeItem.createAttributes(
                                    OrespawnToolMaterial.RubyTools.tier,
                                    OrespawnToolMaterial.RubyTools.hoeDamageBonus(),
                                    OrespawnToolMaterial.RubyTools.hoeAttackSpeed()))));

    // Ã¢â‚¬â€Ã¢â‚¬â€Ã¢â‚¬â€ Emerald tools Ã¢â‚¬â€Ã¢â‚¬â€Ã¢â‚¬â€
    public static final DeferredItem<Item> EMERALD_SWORD = ITEMS.register(
            "emerald_sword",
            () -> new EmeraldSword(toolProps(
                    OrespawnToolMaterial.EmeraldTools,
                    SwordItem.createAttributes(
                            OrespawnToolMaterial.EmeraldTools.tier,
                            OrespawnToolMaterial.EmeraldTools.swordDamageBonus(),
                            OrespawnToolMaterial.EmeraldTools.swordAttackSpeed()))));
    public static final DeferredItem<Item> EMERALD_PICKAXE = ITEMS.register(
            "emerald_pickaxe",
            () -> new EmeraldPickaxe(toolProps(
                    OrespawnToolMaterial.EmeraldTools,
                    PickaxeItem.createAttributes(
                            OrespawnToolMaterial.EmeraldTools.tier,
                            OrespawnToolMaterial.EmeraldTools.pickaxeDamageBonus(),
                            OrespawnToolMaterial.EmeraldTools.pickaxeAttackSpeed()))));
    public static final DeferredItem<Item> EMERALD_AXE = ITEMS.register(
            "emerald_axe",
            () -> new EmeraldAxe(toolProps(
                    OrespawnToolMaterial.EmeraldTools,
                    AxeItem.createAttributes(
                            OrespawnToolMaterial.EmeraldTools.tier,
                            OrespawnToolMaterial.EmeraldTools.axeDamageBonus(),
                            OrespawnToolMaterial.EmeraldTools.axeAttackSpeed()))));
    public static final DeferredItem<Item> EMERALD_SHOVEL = ITEMS.register(
            "emerald_shovel",
            () -> new EmeraldShovel(toolProps(
                    OrespawnToolMaterial.EmeraldTools,
                    ShovelItem.createAttributes(
                            OrespawnToolMaterial.EmeraldTools.tier,
                            OrespawnToolMaterial.EmeraldTools.shovelDamageBonus(),
                            OrespawnToolMaterial.EmeraldTools.shovelAttackSpeed()))));
    public static final DeferredItem<Item> EMERALD_HOE = ITEMS.register(
            "emerald_hoe",
            () -> new EmeraldHoe(toolProps(
                    OrespawnToolMaterial.EmeraldTools,
                    HoeItem.createAttributes(
                            OrespawnToolMaterial.EmeraldTools.tier,
                            OrespawnToolMaterial.EmeraldTools.hoeDamageBonus(),
                            OrespawnToolMaterial.EmeraldTools.hoeAttackSpeed()))));

    // Ã¢â‚¬â€Ã¢â‚¬â€Ã¢â‚¬â€ Amethyst tools (gold Generic* with AmethystTools == emerald stats) Ã¢â‚¬â€Ã¢â‚¬â€Ã¢â‚¬â€
    /** Gold AmethystSword Ã¢â‚¬â€ flat dmg 18, durability 2000 (not GenericSword material defaults). */
    public static final DeferredItem<Item> AMETHYST_SWORD = ITEMS.register(
            "amethyst_sword",
            () -> new AmethystSword(new Item.Properties()
                    .stacksTo(1)
                    .durability(AmethystSword.AMETHYST_SWORD_USES)
                    .attributes(SwordItem.createAttributes(
                            OrespawnToolMaterial.AmethystTools.tier,
                            AmethystSword.SWORD_DAMAGE_BONUS,
                            AmethystSword.SWORD_ATTACK_SPEED))));
    public static final DeferredItem<Item> AMETHYST_PICKAXE = ITEMS.register(
            "amethyst_pickaxe",
            () -> new GenericPickaxe(
                    OrespawnToolMaterial.AmethystTools,
                    toolProps(
                            OrespawnToolMaterial.AmethystTools,
                            PickaxeItem.createAttributes(
                                    OrespawnToolMaterial.AmethystTools.tier,
                                    OrespawnToolMaterial.AmethystTools.pickaxeDamageBonus(),
                                    OrespawnToolMaterial.AmethystTools.pickaxeAttackSpeed()))));
    public static final DeferredItem<Item> AMETHYST_AXE = ITEMS.register(
            "amethyst_axe",
            () -> new GenericAxe(
                    OrespawnToolMaterial.AmethystTools,
                    toolProps(
                            OrespawnToolMaterial.AmethystTools,
                            AxeItem.createAttributes(
                                    OrespawnToolMaterial.AmethystTools.tier,
                                    OrespawnToolMaterial.AmethystTools.axeDamageBonus(),
                                    OrespawnToolMaterial.AmethystTools.axeAttackSpeed()))));
    public static final DeferredItem<Item> AMETHYST_SHOVEL = ITEMS.register(
            "amethyst_shovel",
            () -> new GenericShovel(
                    OrespawnToolMaterial.AmethystTools,
                    toolProps(
                            OrespawnToolMaterial.AmethystTools,
                            ShovelItem.createAttributes(
                                    OrespawnToolMaterial.AmethystTools.tier,
                                    OrespawnToolMaterial.AmethystTools.shovelDamageBonus(),
                                    OrespawnToolMaterial.AmethystTools.shovelAttackSpeed()))));
    public static final DeferredItem<Item> AMETHYST_HOE = ITEMS.register(
            "amethyst_hoe",
            () -> new GenericHoe(
                    OrespawnToolMaterial.AmethystTools,
                    toolProps(
                            OrespawnToolMaterial.AmethystTools,
                            HoeItem.createAttributes(
                                    OrespawnToolMaterial.AmethystTools.tier,
                                    OrespawnToolMaterial.AmethystTools.hoeDamageBonus(),
                                    OrespawnToolMaterial.AmethystTools.hoeAttackSpeed()))));

    // Ã¢â‚¬â€Ã¢â‚¬â€Ã¢â‚¬â€ Ultimate armor Ã¢â‚¬â€Ã¢â‚¬â€Ã¢â‚¬â€
    public static final DeferredItem<Item> ULTIMATE_HELMET = ITEMS.register(
            "ultimate_helmet",
            () -> new ArmorBase(
                    OrespawnArmorMaterial.ultimate(),
                    ArmorItem.Type.HELMET,
                    ultimateArmorProps(ArmorItem.Type.HELMET)));
    public static final DeferredItem<Item> ULTIMATE_CHESTPLATE = ITEMS.register(
            "ultimate_chestplate",
            () -> new ArmorBase(
                    OrespawnArmorMaterial.ultimate(),
                    ArmorItem.Type.CHESTPLATE,
                    ultimateArmorProps(ArmorItem.Type.CHESTPLATE)));
    public static final DeferredItem<Item> ULTIMATE_LEGGINGS = ITEMS.register(
            "ultimate_leggings",
            () -> new ArmorBase(
                    OrespawnArmorMaterial.ultimate(),
                    ArmorItem.Type.LEGGINGS,
                    ultimateArmorProps(ArmorItem.Type.LEGGINGS)));
    public static final DeferredItem<Item> ULTIMATE_BOOTS = ITEMS.register(
            "ultimate_boots",
            () -> new ArmorBase(
                    OrespawnArmorMaterial.ultimate(),
                    ArmorItem.Type.BOOTS,
                    ultimateArmorProps(ArmorItem.Type.BOOTS)));

    // Ã¢â‚¬â€Ã¢â‚¬â€Ã¢â‚¬â€ Emerald armor Ã¢â‚¬â€Ã¢â‚¬â€Ã¢â‚¬â€
    public static final DeferredItem<Item> EMERALD_HELMET = ITEMS.register(
            "emerald_helmet",
            () -> new ArmorBase(
                    OrespawnArmorMaterial.emerald(),
                    ArmorItem.Type.HELMET,
                    emeraldArmorProps(ArmorItem.Type.HELMET)));
    public static final DeferredItem<Item> EMERALD_CHESTPLATE = ITEMS.register(
            "emerald_chestplate",
            () -> new ArmorBase(
                    OrespawnArmorMaterial.emerald(),
                    ArmorItem.Type.CHESTPLATE,
                    emeraldArmorProps(ArmorItem.Type.CHESTPLATE)));
    public static final DeferredItem<Item> EMERALD_LEGGINGS = ITEMS.register(
            "emerald_leggings",
            () -> new ArmorBase(
                    OrespawnArmorMaterial.emerald(),
                    ArmorItem.Type.LEGGINGS,
                    emeraldArmorProps(ArmorItem.Type.LEGGINGS)));
    public static final DeferredItem<Item> EMERALD_BOOTS = ITEMS.register(
            "emerald_boots",
            () -> new ArmorBase(
                    OrespawnArmorMaterial.emerald(),
                    ArmorItem.Type.BOOTS,
                    emeraldArmorProps(ArmorItem.Type.BOOTS)));

    // Ã¢â‚¬â€Ã¢â‚¬â€Ã¢â‚¬â€ Moth armor (gold MothArmor) Ã¢â‚¬â€Ã¢â‚¬â€Ã¢â‚¬â€
    public static final DeferredItem<Item> MOTH_HELMET = ITEMS.register(
            "moth_helmet",
            () -> new ArmorBase(
                    OrespawnArmorMaterial.moth(),
                    ArmorItem.Type.HELMET,
                    mothArmorProps(ArmorItem.Type.HELMET)));
    public static final DeferredItem<Item> MOTH_CHESTPLATE = ITEMS.register(
            "moth_chestplate",
            () -> new ArmorBase(
                    OrespawnArmorMaterial.moth(),
                    ArmorItem.Type.CHESTPLATE,
                    mothArmorProps(ArmorItem.Type.CHESTPLATE)));
    public static final DeferredItem<Item> MOTH_LEGGINGS = ITEMS.register(
            "moth_leggings",
            () -> new ArmorBase(
                    OrespawnArmorMaterial.moth(),
                    ArmorItem.Type.LEGGINGS,
                    mothArmorProps(ArmorItem.Type.LEGGINGS)));
    public static final DeferredItem<Item> MOTH_BOOTS = ITEMS.register(
            "moth_boots",
            () -> new ArmorBase(
                    OrespawnArmorMaterial.moth(),
                    ArmorItem.Type.BOOTS,
                    mothArmorProps(ArmorItem.Type.BOOTS)));

    // Ã¢â‚¬â€Ã¢â‚¬â€Ã¢â‚¬â€ Amethyst armor (gold AmethystArmor) Ã¢â‚¬â€Ã¢â‚¬â€Ã¢â‚¬â€
    public static final DeferredItem<Item> AMETHYST_HELMET = ITEMS.register(
            "amethyst_helmet",
            () -> new ArmorBase(
                    OrespawnArmorMaterial.amethyst(),
                    ArmorItem.Type.HELMET,
                    amethystArmorProps(ArmorItem.Type.HELMET)));
    public static final DeferredItem<Item> AMETHYST_CHESTPLATE = ITEMS.register(
            "amethyst_chestplate",
            () -> new ArmorBase(
                    OrespawnArmorMaterial.amethyst(),
                    ArmorItem.Type.CHESTPLATE,
                    amethystArmorProps(ArmorItem.Type.CHESTPLATE)));
    public static final DeferredItem<Item> AMETHYST_LEGGINGS = ITEMS.register(
            "amethyst_leggings",
            () -> new ArmorBase(
                    OrespawnArmorMaterial.amethyst(),
                    ArmorItem.Type.LEGGINGS,
                    amethystArmorProps(ArmorItem.Type.LEGGINGS)));
    public static final DeferredItem<Item> AMETHYST_BOOTS = ITEMS.register(
            "amethyst_boots",
            () -> new ArmorBase(
                    OrespawnArmorMaterial.amethyst(),
                    ArmorItem.Type.BOOTS,
                    amethystArmorProps(ArmorItem.Type.BOOTS)));

    // Ã¢â‚¬â€Ã¢â‚¬â€Ã¢â‚¬â€ SET13 remaining gold armor sets Ã¢â‚¬â€Ã¢â‚¬â€Ã¢â‚¬â€
    public static final DeferredItem<Item> RUBY_HELMET = armor("ruby_helmet", OrespawnArmorMaterial::ruby, ArmorItem.Type.HELMET, OrespawnArmorMaterial.RUBY_DURABILITY_FACTOR);
    public static final DeferredItem<Item> RUBY_CHESTPLATE = armor("ruby_chestplate", OrespawnArmorMaterial::ruby, ArmorItem.Type.CHESTPLATE, OrespawnArmorMaterial.RUBY_DURABILITY_FACTOR);
    public static final DeferredItem<Item> RUBY_LEGGINGS = armor("ruby_leggings", OrespawnArmorMaterial::ruby, ArmorItem.Type.LEGGINGS, OrespawnArmorMaterial.RUBY_DURABILITY_FACTOR);
    public static final DeferredItem<Item> RUBY_BOOTS = armor("ruby_boots", OrespawnArmorMaterial::ruby, ArmorItem.Type.BOOTS, OrespawnArmorMaterial.RUBY_DURABILITY_FACTOR);

    public static final DeferredItem<Item> EXPERIENCE_HELMET = armor("experience_helmet", OrespawnArmorMaterial::experience, ArmorItem.Type.HELMET, OrespawnArmorMaterial.EXPERIENCE_DURABILITY_FACTOR);
    public static final DeferredItem<Item> EXPERIENCE_CHESTPLATE = armor("experience_chestplate", OrespawnArmorMaterial::experience, ArmorItem.Type.CHESTPLATE, OrespawnArmorMaterial.EXPERIENCE_DURABILITY_FACTOR);
    public static final DeferredItem<Item> EXPERIENCE_LEGGINGS = armor("experience_leggings", OrespawnArmorMaterial::experience, ArmorItem.Type.LEGGINGS, OrespawnArmorMaterial.EXPERIENCE_DURABILITY_FACTOR);
    public static final DeferredItem<Item> EXPERIENCE_BOOTS = armor("experience_boots", OrespawnArmorMaterial::experience, ArmorItem.Type.BOOTS, OrespawnArmorMaterial.EXPERIENCE_DURABILITY_FACTOR);

    public static final DeferredItem<Item> LAVA_EEL_HELMET = armor("lavaeel_helmet", OrespawnArmorMaterial::lavaEel, ArmorItem.Type.HELMET, OrespawnArmorMaterial.LAVA_EEL_DURABILITY_FACTOR);
    public static final DeferredItem<Item> LAVA_EEL_CHESTPLATE = armor("lavaeel_chestplate", OrespawnArmorMaterial::lavaEel, ArmorItem.Type.CHESTPLATE, OrespawnArmorMaterial.LAVA_EEL_DURABILITY_FACTOR);
    public static final DeferredItem<Item> LAVA_EEL_LEGGINGS = armor("lavaeel_leggings", OrespawnArmorMaterial::lavaEel, ArmorItem.Type.LEGGINGS, OrespawnArmorMaterial.LAVA_EEL_DURABILITY_FACTOR);
    public static final DeferredItem<Item> LAVA_EEL_BOOTS = armor("lavaeel_boots", OrespawnArmorMaterial::lavaEel, ArmorItem.Type.BOOTS, OrespawnArmorMaterial.LAVA_EEL_DURABILITY_FACTOR);

    public static final DeferredItem<Item> PEACOCK_HELMET = armor("peacock_helmet", OrespawnArmorMaterial::peacock, ArmorItem.Type.HELMET, OrespawnArmorMaterial.PEACOCK_DURABILITY_FACTOR);
    public static final DeferredItem<Item> PEACOCK_CHESTPLATE = armor("peacock_chestplate", OrespawnArmorMaterial::peacock, ArmorItem.Type.CHESTPLATE, OrespawnArmorMaterial.PEACOCK_DURABILITY_FACTOR);
    public static final DeferredItem<Item> PEACOCK_LEGGINGS = armor("peacock_leggings", OrespawnArmorMaterial::peacock, ArmorItem.Type.LEGGINGS, OrespawnArmorMaterial.PEACOCK_DURABILITY_FACTOR);
    public static final DeferredItem<Item> PEACOCK_BOOTS = armor("peacock_boots", OrespawnArmorMaterial::peacock, ArmorItem.Type.BOOTS, OrespawnArmorMaterial.PEACOCK_DURABILITY_FACTOR);

    public static final DeferredItem<Item> MOBZILLA_HELMET = armor("mobzilla_helmet", OrespawnArmorMaterial::mobzilla, ArmorItem.Type.HELMET, OrespawnArmorMaterial.MOBZILLA_DURABILITY_FACTOR, Rarity.EPIC);
    public static final DeferredItem<Item> MOBZILLA_CHESTPLATE = armor("mobzilla_chestplate", OrespawnArmorMaterial::mobzilla, ArmorItem.Type.CHESTPLATE, OrespawnArmorMaterial.MOBZILLA_DURABILITY_FACTOR, Rarity.EPIC);
    public static final DeferredItem<Item> MOBZILLA_LEGGINGS = armor("mobzilla_leggings", OrespawnArmorMaterial::mobzilla, ArmorItem.Type.LEGGINGS, OrespawnArmorMaterial.MOBZILLA_DURABILITY_FACTOR, Rarity.EPIC);
    public static final DeferredItem<Item> MOBZILLA_BOOTS = armor("mobzilla_boots", OrespawnArmorMaterial::mobzilla, ArmorItem.Type.BOOTS, OrespawnArmorMaterial.MOBZILLA_DURABILITY_FACTOR, Rarity.EPIC);

    public static final DeferredItem<Item> LAPIS_HELMET = armor("lapis_helmet", OrespawnArmorMaterial::lapis, ArmorItem.Type.HELMET, OrespawnArmorMaterial.LAPIS_DURABILITY_FACTOR);
    public static final DeferredItem<Item> LAPIS_CHESTPLATE = armor("lapis_chestplate", OrespawnArmorMaterial::lapis, ArmorItem.Type.CHESTPLATE, OrespawnArmorMaterial.LAPIS_DURABILITY_FACTOR);
    public static final DeferredItem<Item> LAPIS_LEGGINGS = armor("lapis_leggings", OrespawnArmorMaterial::lapis, ArmorItem.Type.LEGGINGS, OrespawnArmorMaterial.LAPIS_DURABILITY_FACTOR);
    public static final DeferredItem<Item> LAPIS_BOOTS = armor("lapis_boots", OrespawnArmorMaterial::lapis, ArmorItem.Type.BOOTS, OrespawnArmorMaterial.LAPIS_DURABILITY_FACTOR);

    public static final DeferredItem<Item> ROYAL_HELMET = armor("royal_helmet", OrespawnArmorMaterial::royal, ArmorItem.Type.HELMET, OrespawnArmorMaterial.ROYAL_DURABILITY_FACTOR, Rarity.EPIC);
    public static final DeferredItem<Item> ROYAL_CHESTPLATE = armor("royal_chestplate", OrespawnArmorMaterial::royal, ArmorItem.Type.CHESTPLATE, OrespawnArmorMaterial.ROYAL_DURABILITY_FACTOR, Rarity.EPIC);
    public static final DeferredItem<Item> ROYAL_LEGGINGS = armor("royal_leggings", OrespawnArmorMaterial::royal, ArmorItem.Type.LEGGINGS, OrespawnArmorMaterial.ROYAL_DURABILITY_FACTOR, Rarity.EPIC);
    public static final DeferredItem<Item> ROYAL_BOOTS = armor("royal_boots", OrespawnArmorMaterial::royal, ArmorItem.Type.BOOTS, OrespawnArmorMaterial.ROYAL_DURABILITY_FACTOR, Rarity.EPIC);

    public static final DeferredItem<Item> QUEEN_HELMET = armor("queen_helmet", OrespawnArmorMaterial::queen, ArmorItem.Type.HELMET, OrespawnArmorMaterial.QUEEN_DURABILITY_FACTOR, Rarity.EPIC);
    public static final DeferredItem<Item> QUEEN_CHESTPLATE = armor("queen_chestplate", OrespawnArmorMaterial::queen, ArmorItem.Type.CHESTPLATE, OrespawnArmorMaterial.QUEEN_DURABILITY_FACTOR, Rarity.EPIC);
    public static final DeferredItem<Item> QUEEN_LEGGINGS = armor("queen_leggings", OrespawnArmorMaterial::queen, ArmorItem.Type.LEGGINGS, OrespawnArmorMaterial.QUEEN_DURABILITY_FACTOR, Rarity.EPIC);
    public static final DeferredItem<Item> QUEEN_BOOTS = armor("queen_boots", OrespawnArmorMaterial::queen, ArmorItem.Type.BOOTS, OrespawnArmorMaterial.QUEEN_DURABILITY_FACTOR, Rarity.EPIC);

    public static final DeferredItem<Item> PINK_HELMET = armor("pink_helmet", OrespawnArmorMaterial::pink, ArmorItem.Type.HELMET, OrespawnArmorMaterial.PINK_DURABILITY_FACTOR);
    public static final DeferredItem<Item> PINK_CHESTPLATE = armor("pink_chestplate", OrespawnArmorMaterial::pink, ArmorItem.Type.CHESTPLATE, OrespawnArmorMaterial.PINK_DURABILITY_FACTOR);
    public static final DeferredItem<Item> PINK_LEGGINGS = armor("pink_leggings", OrespawnArmorMaterial::pink, ArmorItem.Type.LEGGINGS, OrespawnArmorMaterial.PINK_DURABILITY_FACTOR);
    public static final DeferredItem<Item> PINK_BOOTS = armor("pink_boots", OrespawnArmorMaterial::pink, ArmorItem.Type.BOOTS, OrespawnArmorMaterial.PINK_DURABILITY_FACTOR);

    public static final DeferredItem<Item> TIGERSEYE_HELMET = armor("tigerseye_helmet", OrespawnArmorMaterial::tigersEye, ArmorItem.Type.HELMET, OrespawnArmorMaterial.TIGERSEYE_DURABILITY_FACTOR);
    public static final DeferredItem<Item> TIGERSEYE_CHESTPLATE = armor("tigerseye_chestplate", OrespawnArmorMaterial::tigersEye, ArmorItem.Type.CHESTPLATE, OrespawnArmorMaterial.TIGERSEYE_DURABILITY_FACTOR);
    public static final DeferredItem<Item> TIGERSEYE_LEGGINGS = armor("tigerseye_leggings", OrespawnArmorMaterial::tigersEye, ArmorItem.Type.LEGGINGS, OrespawnArmorMaterial.TIGERSEYE_DURABILITY_FACTOR);
    public static final DeferredItem<Item> TIGERSEYE_BOOTS = armor("tigerseye_boots", OrespawnArmorMaterial::tigersEye, ArmorItem.Type.BOOTS, OrespawnArmorMaterial.TIGERSEYE_DURABILITY_FACTOR);

    // Ã¢â‚¬â€Ã¢â‚¬â€Ã¢â‚¬â€ Critter cages (gold CritterCage; stacksTo 16; chance 0.4 for filled) Ã¢â‚¬â€Ã¢â‚¬â€Ã¢â‚¬â€
    public static final DeferredItem<Item> EMPTY_CAGE = cage("empty_cage", null, 1.0F);
    // Gold cages + mining-dim spawns that gold never registered (baryonyx/camara/pointy/cryo/bird/butterfly)
    public static final DeferredItem<Item> ALOSAURUS_CAGE = cage("alosaurus_cage", ModEntities.ALOSAURUS, 0.4F);
    public static final DeferredItem<Item> TREX_CAGE = cage("trex_cage", ModEntities.TREX, 0.4F);
    public static final DeferredItem<Item> BARYONYX_CAGE = cage("baryonyx_cage", ModEntities.BARYONYX, 0.4F);
    public static final DeferredItem<Item> CAMARASAURUS_CAGE = cage("camarasaurus_cage", ModEntities.CAMARASAURUS, 0.4F);
    public static final DeferredItem<Item> POINTYSAURUS_CAGE = cage("pointysaurus_cage", ModEntities.POINTYSAURUS, 0.4F);
    public static final DeferredItem<Item> CRYOLOPHOSAURUS_CAGE =
            cage("cryolophosaurus_cage", ModEntities.CRYOLOPHOSAURUS, 0.4F);
    public static final DeferredItem<Item> COW_CAGE = cage("cow_cage", () -> EntityType.COW, 0.4F);
    public static final DeferredItem<Item> CREEPER_CAGE = cage("creeper_cage", () -> EntityType.CREEPER, 0.4F);
    public static final DeferredItem<Item> GHAST_CAGE = cage("ghast_cage", () -> EntityType.GHAST, 0.4F);
    public static final DeferredItem<Item> HORSE_CAGE = cage("horse_cage", () -> EntityType.HORSE, 0.4F);
    public static final DeferredItem<Item> PIG_CAGE = cage("pig_cage", () -> EntityType.PIG, 0.4F);
    public static final DeferredItem<Item> ZOMBIE_CAGE = cage("zombie_cage", () -> EntityType.ZOMBIE, 0.4F);
    public static final DeferredItem<Item> GAMMAMETROID_CAGE = cage("gammametroid_cage", ModEntities.GAMMAMETROID, 0.4F);
    public static final DeferredItem<Item> SPYRO_CAGE = cage("spyro_cage", ModEntities.SPYRO, 0.4F);
    public static final DeferredItem<Item> DRAGONFLY_CAGE = cage("dragonfly_cage", ModEntities.DRAGONFLY, 0.4F);
    public static final DeferredItem<Item> FIREFLY_CAGE = cage("firefly_cage", ModEntities.FIREFLY, 0.4F);
    public static final DeferredItem<Item> BIRD_CAGE = cage("bird_cage", ModEntities.BIRD, 0.4F);
    public static final DeferredItem<Item> BUTTERFLY_CAGE = cage("butterfly_cage", ModEntities.BUTTERFLY, 0.4F);
    public static final DeferredItem<Item> MOSQUITO_CAGE = cage("mosquito_cage", ModEntities.MOSQUITO, 0.4F);
    public static final DeferredItem<Item> MOTH_CAGE = cage("moth_cage", ModEntities.MOTH, 0.4F);
    public static final DeferredItem<Item> RED_ANT_CAGE = cage("red_ant_cage", ModEntities.RED_ANT, 0.4F);
    public static final DeferredItem<Item> TERMITE_CAGE = cage("termite_cage", ModEntities.TERMITE, 0.4F);
    public static final DeferredItem<Item> CAVEFISHER_CAGE = cage("cavefisher_cage", ModEntities.CAVEFISHER, 0.4F);
    public static final DeferredItem<Item> DOOMWORM_CAGE = cage("doomworm_cage", ModEntities.DOOM_WORM, 0.4F);
    public static final DeferredItem<Item> NASTYSAURUS_CAGE = cage("nastysaurus_cage", ModEntities.NASTYSAURUS, 0.4F);
    public static final DeferredItem<Item> ALIEN_CAGE = cage("alien_cage", ModEntities.ALIEN, 0.4F);
    public static final DeferredItem<Item> VELOCITYRAPTOR_CAGE = cage("velocityraptor_cage", ModEntities.VELOCITYRAPTOR, 0.4F);
    public static final DeferredItem<Item> SMALLWORM_CAGE = cage("smallworm_cage", ModEntities.SMALL_WORM, 0.4F);
    public static final DeferredItem<Item> MEDIUMWORM_CAGE = cage("mediumworm_cage", ModEntities.MEDIUM_WORM, 0.4F);
    public static final DeferredItem<Item> LARGEWORM_CAGE = cage("largeworm_cage", ModEntities.LARGE_WORM, 0.4F);
    public static final DeferredItem<Item> KYUUBI_CAGE = cage("kyuubi_cage", ModEntities.KYUUBI, 0.4F);
    public static final DeferredItem<Item> MANTIS_CAGE = cage("mantis_cage", ModEntities.MANTIS, 0.4F);
    public static final DeferredItem<Item> MOTHRA_CAGE = cage("mothra_cage", ModEntities.MOTHRA, 0.4F);
    public static final DeferredItem<Item> BRUTALFLY_CAGE = cage("brutalfly_cage", ModEntities.BRUTALFLY, 0.4F);
    public static final DeferredItem<Item> BEAVER_CAGE = cage("beaver_cage", ModEntities.BEAVER, 0.4F);
    public static final DeferredItem<Item> REDCOW_CAGE = cage("redcow_cage", ModEntities.REDCOW, 0.4F);
    public static final DeferredItem<Item> STINKBUG_CAGE = cage("stinkbug_cage", ModEntities.STINKBUG, 0.4F);
    public static final DeferredItem<Item> CASSOWARY_CAGE = cage("cassowary_cage", ModEntities.CASSOWARY, 0.4F);

    // Ã¢â‚¬â€Ã¢â‚¬â€Ã¢â‚¬â€ Plant seeds / food (gold ItemButterflySeed, ItemCorn, Ã¢â‚¬Â¦) Ã¢â‚¬â€Ã¢â‚¬â€Ã¢â‚¬â€
    /** Gold ItemCorn extends ItemSeedFood(1, 1.0F) Ã¢â‚¬â€ places corn_plant. */
    public static final DeferredItem<Item> CORN = ITEMS.register(
            "corn",
            () -> new ItemNameBlockItem(
                    ModBlocks.CORN_PLANT.get(),
                    new Item.Properties().food(new FoodProperties.Builder()
                            .nutrition(1)
                            .saturationModifier(1.0F)
                            .build())));

    public static final DeferredItem<Item> BUTTERFLY_SEED = ITEMS.register(
            "butterfly_seed",
            () -> new ItemNameBlockItem(ModBlocks.BUTTERFLY_PLANT.get(), new Item.Properties()));

    public static final DeferredItem<Item> MOSQUITO_SEED = ITEMS.register(
            "mosquito_seed",
            () -> new ItemNameBlockItem(ModBlocks.MOSQUITO_PLANT.get(), new Item.Properties()));

    public static final DeferredItem<Item> FIREFLY_SEED = ITEMS.register(
            "firefly_seed",
            () -> new ItemNameBlockItem(ModBlocks.FIREFLY_PLANT.get(), new Item.Properties()));

    public static final DeferredItem<Item> MOTH_SEED = ITEMS.register(
            "moth_seed",
            () -> new ItemNameBlockItem(ModBlocks.MOTH_PLANT.get(), new Item.Properties()));

    // Ã¢â‚¬â€Ã¢â‚¬â€Ã¢â‚¬â€ SET12 crops / seeds (gold nutrition) Ã¢â‚¬â€Ã¢â‚¬â€Ã¢â‚¬â€
    public static final DeferredItem<Item> STRAWBERRY = foodItem("strawberry", 2, 0.65F);
    public static final DeferredItem<Item> STRAWBERRY_SEED = ITEMS.register(
            "strawberry_seed",
            () -> new ItemNameBlockItem(ModBlocks.STRAWBERRY_PLANT.get(), new Item.Properties()));
    public static final DeferredItem<Item> RADISH = ITEMS.register(
            "radish",
            () -> new ItemNameBlockItem(
                    ModBlocks.RADISH_PLANT.get(),
                    new Item.Properties().food(food(2, 0.45F, false))));
    public static final DeferredItem<Item> RICE = ITEMS.register(
            "rice",
            () -> new ItemNameBlockItem(
                    ModBlocks.RICE_PLANT.get(),
                    new Item.Properties().food(food(5, 0.65F, false))));
    public static final DeferredItem<Item> QUINOA = ITEMS.register(
            "quinoa",
            () -> new ItemNameBlockItem(
                    ModBlocks.QUINOA_PLANT.get(),
                    new Item.Properties().food(food(7, 0.85F, false))));
    public static final DeferredItem<Item> TOMATO_SEED = ITEMS.register(
            "tomato_seed",
            () -> new ItemNameBlockItem(
                    ModBlocks.TOMATO_PLANT.get(),
                    new Item.Properties().food(food(4, 0.55F, false))));
    public static final DeferredItem<Item> LETTUCE_SEED = ITEMS.register(
            "lettuce_seed",
            () -> new ItemNameBlockItem(
                    ModBlocks.LETTUCE_PLANT.get(),
                    new Item.Properties().food(food(3, 0.45F, false))));

    public static final DeferredItem<Item> PIZZA = ITEMS.register(
            "pizza",
            () -> new ItemPizza(ModBlocks.PIZZA.get(), new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> DUCTTAPE = ITEMS.register(
            "ducttape",
            () -> new ItemDuctTape(ModBlocks.DUCTTAPE.get(), new Item.Properties().stacksTo(1)));

    // Ã¢â‚¬â€Ã¢â‚¬â€Ã¢â‚¬â€ SET11 fish (gold unlocalized names Ã¢â‚¬â€ UltimateFishHook loot keys) Ã¢â‚¬â€Ã¢â‚¬â€Ã¢â‚¬â€
    public static final DeferredItem<Item> FIRE_FISH = ITEMS.register(
            "firefish",
            () -> new ItemFireFish(new Item.Properties().food(food(4, 0.6F, true))));
    public static final DeferredItem<Item> SUN_FISH = ITEMS.register(
            "sunfish",
            () -> new ItemSunFish(new Item.Properties().food(food(6, 0.6F, true))));
    public static final DeferredItem<Item> LAVA_EEL = ITEMS.register(
            "lavaeel",
            () -> new ItemLavaEel(new Item.Properties().food(food(2, 0.6F, true))));
    public static final DeferredItem<Item> SPARK_FISH = ITEMS.register(
            "sparkfish",
            () -> new ItemSparkFish(new Item.Properties().food(food(1, 0.2F, true))));
    public static final DeferredItem<Item> GREEN_FISH = ITEMS.register(
            "greenfish",
            () -> new ItemGenericFish(new Item.Properties().food(food(3, 0.5F, false))));
    public static final DeferredItem<Item> BLUE_FISH = ITEMS.register(
            "bluefish",
            () -> new ItemGenericFish(new Item.Properties().food(food(4, 0.4F, false))));
    public static final DeferredItem<Item> PINK_FISH = ITEMS.register(
            "pinkfish",
            () -> new ItemGenericFish(new Item.Properties().food(food(4, 0.6F, false))));
    public static final DeferredItem<Item> ROCK_FISH = ITEMS.register(
            "rockfish",
            () -> new ItemGenericFish(new Item.Properties().food(food(3, 0.7F, false))));
    public static final DeferredItem<Item> WOOD_FISH = ITEMS.register(
            "woodfish",
            () -> new ItemGenericFish(new Item.Properties().food(food(5, 0.7F, false))));
    public static final DeferredItem<Item> GREY_FISH = ITEMS.register(
            "greyfish",
            () -> new ItemGenericFish(new Item.Properties().food(food(5, 0.5F, false))));

    // Ã¢â‚¬â€Ã¢â‚¬â€Ã¢â‚¬â€ SET11 cooked/special foods Ã¢â‚¬â€Ã¢â‚¬â€Ã¢â‚¬â€
    public static final DeferredItem<Item> COOKED_BACON = ITEMS.register(
            "cookedbacon",
            () -> new ItemBacon(new Item.Properties().food(food(14, 1.5F, true))));
    public static final DeferredItem<Item> BUTTER_CANDY = ITEMS.register(
            "buttercandy",
            () -> new ItemButterCandy(new Item.Properties().food(food(4, 0.5F, true))));

    // Ã¢â‚¬â€Ã¢â‚¬â€Ã¢â‚¬â€ SET11 simple foods (gold ItemPopcorn / plain food) Ã¢â‚¬â€Ã¢â‚¬â€Ã¢â‚¬â€
    public static final DeferredItem<Item> POPCORN = foodItem("popcorn", 1, 0.5F);
    public static final DeferredItem<Item> POPCORN_BUTTERED = foodItem("popcorn_buttered", 2, 0.6F);
    public static final DeferredItem<Item> POPCORN_BUTTERED_SALTED = foodItem("popcorn_buttered_salted", 3, 0.75F);
    public static final DeferredItem<Item> POPCORN_BAG = foodItem("popcorn_bag", 10, 1.25F);
    public static final DeferredItem<Item> BUTTER = foodItem("butter", 1, 0.5F);
    public static final DeferredItem<Item> CORNDOG_COOKED = foodItem("corndog_cooked", 16, 2.5F);
    public static final DeferredItem<Item> CORNDOG_RAW = foodItem("corndog_raw", 4, 0.6F);
    public static final DeferredItem<Item> BACON = foodItem("bacon", 8, 1.0F);
    public static final DeferredItem<Item> CRABMEAT = foodItem("crabmeat", 4, 0.25F);
    public static final DeferredItem<Item> COOKED_CRABMEAT = foodItem("cookedcrabmeat", 6, 0.75F);
    public static final DeferredItem<Item> CHEESE = foodItem("cheese", 4, 0.5F);
    public static final DeferredItem<Item> SALAD = foodItem("salad", 10, 0.95F);
    public static final DeferredItem<Item> BLT_SANDWICH = foodItem("blt_sandwich", 12, 0.95F);
    public static final DeferredItem<Item> CRABBY_PATTY = foodItem("crabbypatty", 16, 2.35F);
    public static final DeferredItem<Item> COOKED_PEACOCK = foodItem("cookedpeacock", 12, 1.4F);
    public static final DeferredItem<Item> RAW_PEACOCK = foodItem("rawpeacock", 6, 0.7F);

    // Ã¢â‚¬â€Ã¢â‚¬â€Ã¢â‚¬â€ SET18 special foods / magic Ã¢â‚¬â€Ã¢â‚¬â€Ã¢â‚¬â€
    /** Gold MyLove (unlocalized heart) Ã¢â‚¬â€ nutrition 8 / 0.95. */
    public static final DeferredItem<Item> HEART = ITEMS.register(
            "heart",
            () -> new ItemLove(new Item.Properties().rarity(Rarity.RARE).food(food(8, 0.95F, true))));
    /** Gold MagicApple Ã¢â‚¬â€ grows a large square tree on grass/dirt. */
    public static final DeferredItem<Item> MAGIC_APPLE = ITEMS.register(
            "magic_apple",
            () -> new ItemMagicApple(new Item.Properties().rarity(Rarity.EPIC).stacksTo(ItemMagicApple.MAX_STACK)));
    /** Gold MyCrystalApple Ã¢â‚¬â€ regen + resistance; breeding food for several mobs. */
    public static final DeferredItem<Item> CRYSTAL_APPLE = ITEMS.register(
            "crystalapple",
            () -> new ItemCrystalApple(new Item.Properties().rarity(Rarity.UNCOMMON).food(food(6, 0.8F, true))));

    // Ã¢â‚¬â€Ã¢â‚¬â€Ã¢â‚¬â€ Gold ItemGenericEgg (right-click block to spawn) Ã¢â‚¬â€Ã¢â‚¬â€Ã¢â‚¬â€
    public static final DeferredItem<Item> ALOSAURUS_EGG = genericEgg("alosaurus_egg", ModEntities.ALOSAURUS);
    public static final DeferredItem<Item> BARYONYX_EGG = genericEgg("baryonyx_egg", ModEntities.BARYONYX);
    public static final DeferredItem<Item> CAMARASAURUS_EGG = genericEgg("camarasaurus_egg", ModEntities.CAMARASAURUS);
    public static final DeferredItem<Item> CRYOLOPHOSAURUS_EGG = genericEgg("cryolophosaurus_egg", ModEntities.CRYOLOPHOSAURUS);
    public static final DeferredItem<Item> POINTYSAURUS_EGG = genericEgg("pointysaurus_egg", ModEntities.POINTYSAURUS);
    public static final DeferredItem<Item> TREX_EGG = genericEgg("trex_egg", ModEntities.TREX);
    public static final DeferredItem<Item> CAVEFISHER_EGG = genericEgg("cavefisher_egg", ModEntities.CAVEFISHER);
    public static final DeferredItem<Item> BUTTERFLY_EGG = genericEgg("butterfly_egg", ModEntities.BUTTERFLY);
    public static final DeferredItem<Item> BIRD_EGG = genericEgg("bird_egg", ModEntities.BIRD);
    public static final DeferredItem<Item> ANT_EGG = genericEgg("ant_egg", ModEntities.ANT);
    public static final DeferredItem<Item> RED_ANT_EGG = genericEgg("red_ant_egg", ModEntities.RED_ANT);
    public static final DeferredItem<Item> RAINBOW_ANT_EGG = genericEgg("rainbow_ant_egg", ModEntities.RAINBOW_ANT);
    public static final DeferredItem<Item> UNSTABLE_ANT_EGG = genericEgg("unstable_ant_egg", ModEntities.UNSTABLE_ANT);
    public static final DeferredItem<Item> GAMMAMETROID_EGG = genericEgg("gammametroid_egg", ModEntities.GAMMAMETROID);
    public static final DeferredItem<Item> SPYRO_EGG = genericEgg("spyro_egg", ModEntities.SPYRO);
    public static final DeferredItem<Item> DRAGONFLY_EGG = genericEgg("dragonfly_egg", ModEntities.DRAGONFLY);
    public static final DeferredItem<Item> FIREFLY_EGG = genericEgg("firefly_egg", ModEntities.FIREFLY);
    public static final DeferredItem<Item> FAIRY_EGG = genericEgg("fairy_egg", ModEntities.FAIRY);
    public static final DeferredItem<Item> MOSQUITO_EGG = genericEgg("mosquito_egg", ModEntities.MOSQUITO);
    public static final DeferredItem<Item> NASTYSAURUS_EGG = genericEgg("nastysaurus_egg", ModEntities.NASTYSAURUS);
    public static final DeferredItem<Item> ALIEN_EGG = genericEgg("alien_egg", ModEntities.ALIEN);
    public static final DeferredItem<Item> VELOCITYRAPTOR_EGG = genericEgg("velocityraptor_egg", ModEntities.VELOCITYRAPTOR);
    public static final DeferredItem<Item> SMALLWORM_EGG = genericEgg("smallworm_egg", ModEntities.SMALL_WORM);
    public static final DeferredItem<Item> MEDIUMWORM_EGG = genericEgg("mediumworm_egg", ModEntities.MEDIUM_WORM);
    public static final DeferredItem<Item> LARGEWORM_EGG = genericEgg("largeworm_egg", ModEntities.LARGE_WORM);
    public static final DeferredItem<Item> DOOMWORM_EGG = genericEgg("doomworm_egg", ModEntities.DOOM_WORM);
    public static final DeferredItem<Item> MOTH_EGG = genericEgg("moth_egg", ModEntities.MOTH);
    public static final DeferredItem<Item> KYUUBI_EGG = genericEgg("kyuubi_egg", ModEntities.KYUUBI);
    public static final DeferredItem<Item> MANTIS_EGG = genericEgg("mantis_egg", ModEntities.MANTIS);
    public static final DeferredItem<Item> MOTHRA_EGG = genericEgg("mothra_egg", ModEntities.MOTHRA);
    public static final DeferredItem<Item> BRUTALFLY_EGG = genericEgg("brutalfly_egg", ModEntities.BRUTALFLY);
    public static final DeferredItem<Item> BEAVER_EGG = genericEgg("beaver_egg", ModEntities.BEAVER);
    public static final DeferredItem<Item> TERMITE_EGG = genericEgg("termite_egg", ModEntities.TERMITE);
    public static final DeferredItem<Item> CASSOWARY_EGG = genericEgg("cassowary_egg", ModEntities.CASSOWARY);
    public static final DeferredItem<Item> REDCOW_EGG = genericEgg("redcow_egg", ModEntities.REDCOW);
    public static final DeferredItem<Item> STINKBUG_EGG = genericEgg("stinkbug_egg", ModEntities.STINKBUG);
    public static final DeferredItem<Item> RAT_EGG = genericEgg("rat_egg", ModEntities.RAT);
    public static final DeferredItem<Item> CHIPMUNK_EGG = genericEgg("chipmunk_egg", ModEntities.CHIPMUNK);
    public static final DeferredItem<Item> CRICKET_EGG = genericEgg("cricket_egg", ModEntities.CRICKET);
    public static final DeferredItem<Item> BEE_EGG = genericEgg("bee_egg", ModEntities.BEE);
    public static final DeferredItem<Item> FROG_EGG = genericEgg("frog_egg", ModEntities.FROG);
    public static final DeferredItem<Item> RUBBER_DUCKY_EGG = genericEgg("rubber_ducky_egg", ModEntities.RUBBER_DUCKY);
    public static final DeferredItem<Item> FLOUNDER_EGG = genericEgg("flounder_egg", ModEntities.FLOUNDER);
    public static final DeferredItem<Item> GOLD_FISH_EGG = genericEgg("gold_fish_egg", ModEntities.GOLD_FISH);
    public static final DeferredItem<Item> CLOUD_SHARK_EGG = genericEgg("cloud_shark_egg", ModEntities.CLOUD_SHARK);
    public static final DeferredItem<Item> LURKING_TERROR_EGG = genericEgg("lurking_terror_egg", ModEntities.LURKING_TERROR);
    public static final DeferredItem<Item> CREEPING_HORROR_EGG = genericEgg("creeping_horror_egg", ModEntities.CREEPING_HORROR);
    public static final DeferredItem<Item> TERRIBLE_TERROR_EGG = genericEgg("terrible_terror_egg", ModEntities.TERRIBLE_TERROR);
    public static final DeferredItem<Item> PEACOCK_EGG = genericEgg("peacock_egg", ModEntities.PEACOCK);
    public static final DeferredItem<Item> CLIFF_RACER_EGG = genericEgg("cliff_racer_egg", ModEntities.CLIFF_RACER);
    public static final DeferredItem<Item> LEAF_MONSTER_EGG = genericEgg("leaf_monster_egg", ModEntities.LEAF_MONSTER);
    public static final DeferredItem<Item> SCORPION_EGG = genericEgg("scorpion_egg", ModEntities.SCORPION);
    public static final DeferredItem<Item> ATTACK_SQUID_EGG = genericEgg("attack_squid_egg", ModEntities.ATTACK_SQUID);
    public static final DeferredItem<Item> IRUKANDJI_EGG = genericEgg("irukandji_egg", ModEntities.IRUKANDJI);
    public static final DeferredItem<Item> SKATE_EGG = genericEgg("skate_egg", ModEntities.SKATE);
    public static final DeferredItem<Item> ENDER_KNIGHT_EGG = genericEgg("ender_knight_egg", ModEntities.ENDER_KNIGHT);
    public static final DeferredItem<Item> ENDER_REAPER_EGG = genericEgg("ender_reaper_egg", ModEntities.ENDER_REAPER);
    public static final DeferredItem<Item> HYDROLISC_EGG = genericEgg("hydrolisc_egg", ModEntities.HYDROLISC);
    public static final DeferredItem<Item> BASILISK_EGG = genericEgg("basilisk_egg", ModEntities.BASILISK);
    public static final DeferredItem<Item> GAZELLE_EGG = genericEgg("gazelle_egg", ModEntities.GAZELLE);
    public static final DeferredItem<Item> EASTER_BUNNY_EGG = genericEgg("easter_bunny_egg", ModEntities.EASTER_BUNNY);
    public static final DeferredItem<Item> BANDP_EGG = genericEgg("bandp_egg", ModEntities.BANDP);
    public static final DeferredItem<Item> HAMMERHEAD_EGG = genericEgg("hammerhead_egg", ModEntities.HAMMERHEAD);
    public static final DeferredItem<Item> WHALE_EGG = genericEgg("whale_egg", ModEntities.WHALE);
    public static final DeferredItem<Item> URCHIN_EGG = genericEgg("urchin_egg", ModEntities.URCHIN);
    public static final DeferredItem<Item> CRAB_EGG = genericEgg("crab_egg", ModEntities.CRAB);
    public static final DeferredItem<Item> ROBOT1_EGG = genericEgg("robot1_egg", ModEntities.ROBOT1);
    public static final DeferredItem<Item> ROBOT2_EGG = genericEgg("robot2_egg", ModEntities.ROBOT2);
    public static final DeferredItem<Item> ROBOT3_EGG = genericEgg("robot3_egg", ModEntities.ROBOT3);
    public static final DeferredItem<Item> ROBOT4_EGG = genericEgg("robot4_egg", ModEntities.ROBOT4);
    public static final DeferredItem<Item> ROBOT5_EGG = genericEgg("robot5_egg", ModEntities.ROBOT5);
    public static final DeferredItem<Item> MOLENOID_EGG = genericEgg("molenoid_egg", ModEntities.MOLENOID);
    public static final DeferredItem<Item> VORTEX_EGG = genericEgg("vortex_egg", ModEntities.VORTEX);
    public static final DeferredItem<Item> OSTRICH_EGG = genericEgg("ostrich_egg", ModEntities.OSTRICH);
    public static final DeferredItem<Item> LIZARD_EGG = genericEgg("lizard_egg", ModEntities.LIZARD);
    public static final DeferredItem<Item> STINKY_EGG = genericEgg("stinky_egg", ModEntities.STINKY);
    public static final DeferredItem<Item> COCKATEIL_EGG = genericEgg("cockateil_egg", ModEntities.COCKATEIL);
    public static final DeferredItem<Item> SEA_MONSTER_EGG = genericEgg("sea_monster_egg", ModEntities.SEA_MONSTER);
    public static final DeferredItem<Item> SEA_VIPER_EGG = genericEgg("sea_viper_egg", ModEntities.SEA_VIPER);
    public static final DeferredItem<Item> WATER_DRAGON_EGG = genericEgg("water_dragon_egg", ModEntities.WATER_DRAGON);
    public static final DeferredItem<Item> CATERKILLER_EGG = genericEgg("caterkiller_egg", ModEntities.CATERKILLER);
    public static final DeferredItem<Item> EMPEROR_SCORPION_EGG = genericEgg("emperor_scorpion_egg", ModEntities.EMPEROR_SCORPION);
    public static final DeferredItem<Item> HERCULES_BEETLE_EGG = genericEgg("hercules_beetle_egg", ModEntities.HERCULES_BEETLE);
    public static final DeferredItem<Item> PITCH_BLACK_EGG = genericEgg("pitch_black_egg", ModEntities.PITCH_BLACK);
    public static final DeferredItem<Item> ANT_ROBOT_EGG = genericEgg("ant_robot_egg", ModEntities.ANT_ROBOT);
    public static final DeferredItem<Item> TROOPER_BUG_EGG = genericEgg("trooper_bug_egg", ModEntities.TROOPER_BUG);
    public static final DeferredItem<Item> SPIT_BUG_EGG = genericEgg("spit_bug_egg", ModEntities.SPIT_BUG);
    public static final DeferredItem<Item> DUNGEON_BEAST_EGG = genericEgg("dungeon_beast_egg", ModEntities.DUNGEON_BEAST);
    public static final DeferredItem<Item> CEPHADROME_EGG = genericEgg("cephadrome_egg", ModEntities.CEPHADROME);
    public static final DeferredItem<Item> TRIFFID_EGG = genericEgg("triffid_egg", ModEntities.TRIFFID);
    public static final DeferredItem<Item> SPIDER_ROBOT_EGG = genericEgg("spider_robot_egg", ModEntities.SPIDER_ROBOT);
    public static final DeferredItem<Item> THE_PRINCE_EGG = genericEgg("the_prince_egg", ModEntities.THE_PRINCE);
    public static final DeferredItem<Item> THE_PRINCESS_EGG = genericEgg("the_princess_egg", ModEntities.THE_PRINCESS);
    public static final DeferredItem<Item> LEON_EGG = genericEgg("leon_egg", ModEntities.LEON);
    public static final DeferredItem<Item> DRAGON_EGG = genericEgg("dragon_egg", ModEntities.DRAGON);
    public static final DeferredItem<Item> GODZILLA_EGG = genericEgg("godzilla_egg", ModEntities.GODZILLA);
    public static final DeferredItem<Item> KRAKEN_EGG = genericEgg("kraken_egg", ModEntities.KRAKEN);
    public static final DeferredItem<Item> GIRLFRIEND_EGG = genericEgg("girlfriend_egg", ModEntities.GIRLFRIEND);
    public static final DeferredItem<Item> BOYFRIEND_EGG = genericEgg("boyfriend_egg", ModEntities.BOYFRIEND);
    public static final DeferredItem<Item> SPIDER_DRIVER_EGG = genericEgg("spider_driver_egg", ModEntities.SPIDER_DRIVER);
    public static final DeferredItem<Item> ROTATOR_EGG = genericEgg("rotator_egg", ModEntities.ROTATOR);
    public static final DeferredItem<Item> CRYSTAL_COW_EGG = genericEgg("crystal_cow_egg", ModEntities.CRYSTAL_COW);
    public static final DeferredItem<Item> GOLD_COW_EGG = genericEgg("gold_cow_egg", ModEntities.GOLD_COW);
    public static final DeferredItem<Item> ENCHANTED_COW_EGG = genericEgg("enchanted_cow_egg", ModEntities.ENCHANTED_COW);
    public static final DeferredItem<Item> RUBY_BIRD_EGG = genericEgg("ruby_bird_egg", ModEntities.RUBY_BIRD);
    public static final DeferredItem<Item> GHOST_EGG = genericEgg("ghost_egg", ModEntities.GHOST);
    public static final DeferredItem<Item> GHOST_SKELLY_EGG = genericEgg("ghost_skelly_egg", ModEntities.GHOST_SKELLY);
    public static final DeferredItem<Item> THE_PRINCE_TEEN_EGG = genericEgg("the_prince_teen_egg", ModEntities.THE_PRINCE_TEEN);
    public static final DeferredItem<Item> THE_PRINCE_ADULT_EGG = genericEgg("the_prince_adult_egg", ModEntities.THE_PRINCE_ADULT);
    public static final DeferredItem<Item> GIANT_ROBOT_EGG = genericEgg("giant_robot_egg", ModEntities.GIANT_ROBOT);
    public static final DeferredItem<Item> THE_KING_EGG = genericEgg("the_king_egg", ModEntities.THE_KING);
    public static final DeferredItem<Item> THE_QUEEN_EGG = genericEgg("the_queen_egg", ModEntities.THE_QUEEN);



    // Gold only had ItemGenericEgg (OG textures). No modern DeferredSpawnEggItem duplicates.

    private static DeferredItem<Item> basic(String name) {
        return ITEMS.registerSimpleItem(name);
    }

    /** Gold ItemPopcorn / plain food registration helper. */
    private static DeferredItem<Item> foodItem(String name, int nutrition, float saturation) {
        return ITEMS.register(name, () -> new Item(new Item.Properties().food(food(nutrition, saturation, false))));
    }

    private static FoodProperties food(int nutrition, float saturation, boolean alwaysEdible) {
        FoodProperties.Builder b = new FoodProperties.Builder()
                .nutrition(nutrition)
                .saturationModifier(saturation);
        if (alwaysEdible) {
            b.alwaysEdible();
        }
        return b.build();
    }

    private static DeferredItem<Item> genericEgg(String name, Supplier<? extends EntityType<?>> type) {
        return ITEMS.register(name, () -> new ItemGenericEgg(new Item.Properties().stacksTo(16), type));
    }

    /**
     * Gold CritterCage registration Ã¢â‚¬â€ stacksTo(16); entityType null = empty capture cage.
     * Pass the Supplier through (lazy resolve) so natural + egg spawns always match.
     */
    private static DeferredItem<Item> cage(
            String name, @Nullable Supplier<? extends EntityType<?>> type, float chance) {
        return ITEMS.register(
                name, () -> new CritterCage(new Item.Properties().stacksTo(16), type, chance));
    }

    private static Item.Properties ultimateToolProps(net.minecraft.world.item.component.ItemAttributeModifiers attrs) {
        return new Item.Properties().rarity(Rarity.EPIC).fireResistant().attributes(attrs);
    }

    private static Item.Properties toolProps(
            OrespawnToolMaterial material, net.minecraft.world.item.component.ItemAttributeModifiers attrs) {
        return new Item.Properties().rarity(Rarity.UNCOMMON).attributes(attrs);
    }

    private static Item.Properties ultimateArmorProps(ArmorItem.Type type) {
        return new Item.Properties()
                .rarity(Rarity.EPIC)
                .fireResistant()
                .durability(type.getDurability(OrespawnArmorMaterial.ULTIMATE_DURABILITY_FACTOR));
    }

    private static Item.Properties emeraldArmorProps(ArmorItem.Type type) {
        return new Item.Properties()
                .rarity(Rarity.UNCOMMON)
                .durability(type.getDurability(OrespawnArmorMaterial.EMERALD_DURABILITY_FACTOR));
    }

    private static Item.Properties mothArmorProps(ArmorItem.Type type) {
        return new Item.Properties()
                .rarity(Rarity.UNCOMMON)
                .durability(type.getDurability(OrespawnArmorMaterial.MOTH_DURABILITY_FACTOR));
    }

    private static Item.Properties amethystArmorProps(ArmorItem.Type type) {
        return new Item.Properties()
                .rarity(Rarity.UNCOMMON)
                .durability(type.getDurability(OrespawnArmorMaterial.AMETHYST_DURABILITY_FACTOR));
    }

    private static DeferredItem<Item> armor(
            String name,
            java.util.function.Supplier<net.minecraft.core.Holder<net.minecraft.world.item.ArmorMaterial>> material,
            ArmorItem.Type type,
            int durabilityFactor) {
        return armor(name, material, type, durabilityFactor, Rarity.UNCOMMON);
    }

    private static DeferredItem<Item> armor(
            String name,
            java.util.function.Supplier<net.minecraft.core.Holder<net.minecraft.world.item.ArmorMaterial>> material,
            ArmorItem.Type type,
            int durabilityFactor,
            Rarity rarity) {
        return ITEMS.register(
                name,
                () -> new ArmorBase(
                        material.get(),
                        type,
                        new Item.Properties().rarity(rarity).durability(type.getDurability(durabilityFactor))));
    }

    private ModItems() {}
}
