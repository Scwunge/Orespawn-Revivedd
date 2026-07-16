package danger.orespawn.init;

import danger.orespawn.items.CritterCage;
import danger.orespawn.items.ItemGenericEgg;
import danger.orespawn.util.Reference;
import java.util.Comparator;
import java.util.function.Supplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * OreSpawn creative inventory — logical tabs + a dedicated Crystal Dimension tab.
 * Crystal-related blocks/items appear only under Crystal (not also in Blocks/Equipment/etc.).
 * Eggs only under OreSpawn Eggs / Crystal (not vanilla Spawn Eggs).
 * <p>
 * All OreSpawn tabs use {@link CreativeModeTab.Row#BOTTOM} so they sit on the <b>second
 * page/row</b> of the creative inventory (with Ingredients / Spawn Eggs), not the top row.
 */
public final class ModCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Reference.MOD_ID);

    /**
     * Column index among BOTTOM-row tabs (vanilla already uses several columns).
     * NeoForge re-sorts with {@code tabsBefore}/{@code tabsAfter}; column is a stable seed.
     */
    private static int nextBottomColumn = 0;

    /** Crystal dim blocks, tools, tigerseye, pink crystal, fairy-related. */
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> CRYSTAL = register(
            "crystal",
            "itemGroup.orespawn.crystal",
            () -> new ItemStack(ModBlocks.CRYSTAL_GRASS.get()),
            CreativeModeTabs.INGREDIENTS,
            TabKind.CRYSTAL);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> BLOCKS = register(
            "blocks",
            "itemGroup.orespawn.blocks",
            () -> new ItemStack(ModBlocks.URANIUM_ORE.get()),
            CreativeModeTabs.INGREDIENTS,
            TabKind.BLOCKS);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> EQUIPMENT = register(
            "equipment",
            "itemGroup.orespawn.equipment",
            () -> itemIcon(ModItems.ULTIMATE_SWORD),
            CreativeModeTabs.INGREDIENTS,
            TabKind.EQUIPMENT);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> MATERIALS = register(
            "materials",
            "itemGroup.orespawn.materials",
            () -> itemIcon(ModItems.URANIUM_INGOT),
            CreativeModeTabs.INGREDIENTS,
            TabKind.MATERIALS);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> FOOD = register(
            "food",
            "itemGroup.orespawn.food",
            () -> itemIcon(ModItems.MAGIC_APPLE),
            CreativeModeTabs.INGREDIENTS,
            TabKind.FOOD);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> EGGS = register(
            "eggs",
            "itemGroup.orespawn.eggs",
            () -> itemIcon(ModItems.TREX_EGG),
            CreativeModeTabs.SPAWN_EGGS,
            TabKind.EGGS);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> CAGES = register(
            "cages",
            "itemGroup.orespawn.cages",
            () -> itemIcon(ModItems.EMPTY_CAGE),
            CreativeModeTabs.SPAWN_EGGS,
            TabKind.CAGES);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> UTILITY = register(
            "utility",
            "itemGroup.orespawn.utility",
            () -> itemIcon(ModItems.RANDOM_DUNGEON),
            CreativeModeTabs.SPAWN_EGGS,
            TabKind.UTILITY);

    private enum TabKind {
        CRYSTAL,
        BLOCKS,
        EQUIPMENT,
        MATERIALS,
        FOOD,
        EGGS,
        CAGES,
        UTILITY
    }

    private ModCreativeTabs() {}

    private static DeferredHolder<CreativeModeTab, CreativeModeTab> register(
            String name,
            String titleKey,
            Supplier<ItemStack> icon,
            ResourceKey<CreativeModeTab> before,
            TabKind kind) {
        // BOTTOM = second creative tab row/page (vanilla Ingredients / Spawn Eggs row)
        final int column = nextBottomColumn++;
        return CREATIVE_MODE_TABS.register(name, () -> CreativeModeTab.builder(
                        CreativeModeTab.Row.BOTTOM, column)
                .title(Component.translatable(titleKey))
                .withTabsBefore(before)
                .icon(icon)
                .displayItems((params, out) -> fill(out, kind))
                .build());
    }

    private static void fill(CreativeModeTab.Output out, TabKind kind) {
        if (kind == TabKind.CRYSTAL) {
            // Crystal blocks first, then crystal items
            ModBlocks.BLOCK_ITEMS.getEntries().stream()
                    .filter(h -> isCrystalPath(h.getId().getPath()))
                    .sorted(Comparator.comparing(h -> h.getId().getPath()))
                    .forEach(h -> out.accept(h.get()));
            ModItems.ITEMS.getEntries().stream()
                    .filter(h -> !(h.get() instanceof net.minecraft.world.item.BlockItem))
                    .filter(h -> isCrystalPath(h.getId().getPath()))
                    .sorted(Comparator.comparing(h -> h.getId().getPath()))
                    .forEach(h -> out.accept(h.get()));
            return;
        }

        if (kind == TabKind.BLOCKS) {
            ModBlocks.BLOCK_ITEMS.getEntries().stream()
                    .filter(h -> !isCrystalPath(h.getId().getPath()))
                    .sorted(Comparator.comparing(h -> h.getId().getPath()))
                    .forEach(h -> out.accept(h.get()));
            return;
        }

        ModItems.ITEMS.getEntries().stream()
                .sorted(Comparator.comparing(h -> h.getId().getPath()))
                .forEach(h -> {
                    Item item = h.get();
                    if (item instanceof net.minecraft.world.item.BlockItem) {
                        return;
                    }
                    String path = h.getId().getPath();
                    // Crystal set lives only in the Crystal tab
                    if (isCrystalPath(path)) {
                        return;
                    }
                    if (classify(path, item) == kind) {
                        out.accept(item);
                    }
                });
    }

    /**
     * Crystal dimension family: crystal blocks/tools, tigerseye gear, pink crystal,
     * crystal cow, fairy gear/eggs (heavy crystal-dim identity).
     */
    static boolean isCrystalPath(String p) {
        if (p.contains("crystal")) {
            return true;
        }
        if (p.startsWith("tigerseye") || p.equals("tigerseye")) {
            return true;
        }
        // thrown crystal rocks
        if (p.startsWith("rockcrystal")) {
            return true;
        }
        // Fairy is a signature crystal-dim ambient
        if (p.equals("fairy_egg") || p.equals("fairy_sword") || p.equals("fairy_cage")) {
            return true;
        }
        return false;
    }

    private static TabKind classify(String p, Item item) {
        if (item instanceof ItemGenericEgg || isEggPath(p)) {
            return TabKind.EGGS;
        }
        if (item instanceof CritterCage || isCagePath(p)) {
            return TabKind.CAGES;
        }
        if (isEquipmentPath(p)) {
            return TabKind.EQUIPMENT;
        }
        if (isFoodPath(p) || item.getDefaultInstance().getFoodProperties(null) != null) {
            return TabKind.FOOD;
        }
        if (isUtilityPath(p)) {
            return TabKind.UTILITY;
        }
        if (isMaterialPath(p)) {
            return TabKind.MATERIALS;
        }
        return TabKind.MATERIALS;
    }

    private static boolean isEggPath(String p) {
        return p.endsWith("_egg") || p.endsWith("egg");
    }

    private static boolean isCagePath(String p) {
        return p.endsWith("_cage") || p.equals("empty_cage");
    }

    private static boolean isEquipmentPath(String p) {
        if (p.contains("helmet")
                || p.contains("chestplate")
                || p.contains("leggings")
                || p.contains("boots")
                || p.contains("heels")
                || p.equals("slippers")) {
            return true;
        }
        if (p.contains("sword")
                || p.contains("pickaxe")
                || p.contains("shovel")
                || p.contains("hoe")
                || p.endsWith("axe")
                || p.contains("battle")
                || p.contains("bertha")
                || p.contains("hammy")
                || p.contains("chainsaw")
                || p.contains("bow")
                || p.contains("staff")
                || p.contains("zooka")
                || p.contains("slice")
                || p.equals("royal")
                || p.startsWith("bb")
                || p.contains("nightmare_sword")
                || p.contains("poison_sword")
                || p.contains("experience_sword")
                || p.contains("rat_sword")
                || p.equals("ultimate_fishing_rod")) {
            return true;
        }
        return false;
    }

    private static boolean isFoodPath(String p) {
        return p.contains("bacon")
                || p.contains("fish")
                || p.equals("magic_apple")
                || p.equals("heart")
                || p.equals("pizza")
                || p.contains("corn")
                || p.contains("popcorn")
                || p.contains("cheese")
                || p.contains("butter")
                || p.contains("salad")
                || p.contains("strawberry")
                || p.contains("rice")
                || p.contains("quinoa")
                || p.contains("tomato")
                || p.contains("lettuce")
                || p.contains("radish")
                || p.contains("peacock")
                || p.contains("crab")
                || p.equals("blt_sandwich")
                || p.equals("worm_food")
                || p.equals("deadstinkbug")
                || p.equals("lavaeel")
                || p.equals("greengoo");
    }

    private static boolean isMaterialPath(String p) {
        return p.contains("ingot")
                || p.contains("nugget")
                || p.contains("scale")
                || p.contains("tooth")
                || p.contains("tongue")
                || p.contains("feather")
                || p.contains("claw")
                || p.contains("jaw")
                || p.equals("ruby")
                || p.equals("amethyst")
                || p.equals("salt")
                || p.equals("vortexeye")
                || p.equals("molenoidnose")
                || p.equals("peacockfeather")
                || p.equals("mantis_claw")
                || p.equals("caterkillerjaw");
    }

    private static boolean isUtilityPath(String p) {
        return p.equals("randomdungeon")
                || p.equals("miners_dream")
                || p.equals("sifter")
                || p.equals("zoo_keeper")
                || p.equals("wrench")
                || p.equals("gamecontroller")
                || p.equals("elevator")
                || p.equals("instantshelter")
                || p.equals("instantgarden")
                || p.equals("creeperlauncher")
                || p.equals("raygun")
                || p.equals("acid")
                || p.equals("ducttape")
                || p.equals("rock")
                || (p.startsWith("rock") && !p.startsWith("rockcrystal"))
                || p.equals("waterball")
                || p.equals("iceball")
                || p.equals("laserball")
                || p.contains("robot_kit")
                || p.contains("seed")
                || p.equals("boots_throw")
                || p.equals("thunder_staff")
                || p.equals("squid_zooka")
                || p.equals("deadirukandji")
                || p.equals("irukandjiarrow")
                || p.equals("skatebow")
                || p.equals("experiencecatcher")
                || p.equals("step_up")
                || p.equals("step_down")
                || p.equals("step_accross")
                || p.equals("netherlost")
                || p.startsWith("zoo")
                || p.equals("crystal_coal")
                || p.equals("ruby_block")
                || p.equals("mobzilla_scale_block")
                || p.equals("ender_pearl_block")
                || p.equals("eye_of_ender_block");
    }

    private static ItemStack itemIcon(DeferredItem<? extends Item> item) {
        return item.get().getDefaultInstance();
    }
}
