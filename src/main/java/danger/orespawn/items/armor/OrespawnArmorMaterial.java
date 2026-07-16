package danger.orespawn.items.armor;

import danger.orespawn.init.ModItems;
import danger.orespawn.util.Reference;
import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.EnumMap;
import java.util.List;

/**
 * Gold {@code OrespawnArmorMaterial} — 1:1 defense / durability factor / toughness / enchantability.
 *
 * <p>Gold protection array order was boots, legs, chest, helmet (1.12 EnumHelper).
 * Ultimate: dur 200, {6,12,10,6}, ench 100, toughness 3
 * Emerald:  dur 100, {3,8,6,3}, ench 12, toughness 3
 */
public final class OrespawnArmorMaterial {
    public static final DeferredRegister<ArmorMaterial> ARMOR_MATERIALS =
            DeferredRegister.create(Registries.ARMOR_MATERIAL, Reference.MOD_ID);

    /** Gold durability factor 200 → piece max damage = type.durability * 200. */
    public static final int ULTIMATE_DURABILITY_FACTOR = 200;
    /** Gold durability factor 100. */
    public static final int EMERALD_DURABILITY_FACTOR = 100;
    /** Gold moth / amethyst durability factor 100. */
    public static final int MOTH_DURABILITY_FACTOR = 100;
    public static final int AMETHYST_DURABILITY_FACTOR = 100;
    // Gold get_armorstats durability factors (ArmorStats.durability)
    public static final int RUBY_DURABILITY_FACTOR = 90;
    public static final int EXPERIENCE_DURABILITY_FACTOR = 70;
    public static final int LAVA_EEL_DURABILITY_FACTOR = 40;
    public static final int PEACOCK_DURABILITY_FACTOR = 40;
    public static final int MOBZILLA_DURABILITY_FACTOR = 1000;
    public static final int LAPIS_DURABILITY_FACTOR = 60;
    public static final int ROYAL_DURABILITY_FACTOR = 2000;
    public static final int QUEEN_DURABILITY_FACTOR = 1500;
    public static final int PINK_DURABILITY_FACTOR = 50;
    public static final int TIGERSEYE_DURABILITY_FACTOR = 80;

    public static final DeferredHolder<ArmorMaterial, ArmorMaterial> ULTIMATE = ARMOR_MATERIALS.register(
            "ultimate",
            () -> material(
                    "ultimate",
                    /* boots, legs, chest, helmet */ 6,
                    12,
                    10,
                    6,
                    100,
                    3.0F,
                    () -> Ingredient.of(ModItems.URANIUM_INGOT.get(), ModItems.TITANIUM_INGOT.get())));

    public static final DeferredHolder<ArmorMaterial, ArmorMaterial> EMERALD = ARMOR_MATERIALS.register(
            "emerald",
            () -> material(
                    "emerald",
                    3,
                    8,
                    6,
                    3,
                    12,
                    3.0F,
                    () -> Ingredient.of(Items.EMERALD)));

    /** Gold MothArmor: dur 100, {2,7,5,2}, ench 12, toughness 3 */
    public static final DeferredHolder<ArmorMaterial, ArmorMaterial> MOTH = ARMOR_MATERIALS.register(
            "moth",
            () -> material(
                    "moth",
                    2,
                    7,
                    5,
                    2,
                    12,
                    3.0F,
                    () -> Ingredient.of(ModItems.MOTH_SCALE.get())));

    /** Gold AmethystArmor: dur 100, {4,8,7,3} boots/legs/chest/helmet, ench 12, toughness 3 */
    public static final DeferredHolder<ArmorMaterial, ArmorMaterial> AMETHYST = ARMOR_MATERIALS.register(
            "amethyst",
            () -> material(
                    "amethyst",
                    4,
                    8,
                    7,
                    3,
                    12,
                    3.0F,
                    () -> Ingredient.of(ModItems.AMETHYST.get())));

    /**
     * Gold armor protection order in port materials: boots, legs, chest, helmet
     * (from ArmorStats boot/leg/chest/head fields).
     */
    public static final DeferredHolder<ArmorMaterial, ArmorMaterial> RUBY = ARMOR_MATERIALS.register(
            "ruby",
            () -> material("ruby", 4, 8, 9, 4, 40, 2.0F, () -> Ingredient.of(ModItems.RUBY.get())));
    public static final DeferredHolder<ArmorMaterial, ArmorMaterial> EXPERIENCE = ARMOR_MATERIALS.register(
            "experience",
            () -> material("experience", 4, 7, 9, 5, 50, 2.0F, () -> Ingredient.of(Items.EXPERIENCE_BOTTLE)));
    public static final DeferredHolder<ArmorMaterial, ArmorMaterial> LAVA_EEL = ARMOR_MATERIALS.register(
            "lavaeel",
            () -> material("lavaeel", 2, 5, 7, 2, 35, 1.0F, () -> Ingredient.of(ModItems.LAVA_EEL.get())));
    public static final DeferredHolder<ArmorMaterial, ArmorMaterial> PEACOCK = ARMOR_MATERIALS.register(
            "peacock",
            () -> material("peacock", 2, 4, 5, 2, 30, 0.0F, () -> Ingredient.of(ModItems.PEACOCK_FEATHER.get())));
    public static final DeferredHolder<ArmorMaterial, ArmorMaterial> MOBZILLA = ARMOR_MATERIALS.register(
            "mobzilla",
            () -> material("mobzilla", 7, 11, 13, 7, 150, 4.0F, () -> Ingredient.of(ModItems.GODZILLA_SCALE.get())));
    public static final DeferredHolder<ArmorMaterial, ArmorMaterial> LAPIS = ARMOR_MATERIALS.register(
            "lapis",
            () -> material("lapis", 2, 5, 7, 2, 60, 0.0F, () -> Ingredient.of(Items.LAPIS_LAZULI)));
    public static final DeferredHolder<ArmorMaterial, ArmorMaterial> ROYAL = ARMOR_MATERIALS.register(
            "royal",
            () -> material("royal", 8, 12, 14, 8, 200, 5.0F, () -> Ingredient.of(ModItems.QUEEN_SCALE.get())));
    public static final DeferredHolder<ArmorMaterial, ArmorMaterial> QUEEN = ARMOR_MATERIALS.register(
            "queen",
            () -> material("queen", 9, 14, 16, 9, 150, 5.0F, () -> Ingredient.of(ModItems.QUEEN_SCALE.get())));
    public static final DeferredHolder<ArmorMaterial, ArmorMaterial> PINK = ARMOR_MATERIALS.register(
            "pink",
            () -> material("pink", 2, 5, 7, 3, 40, 1.0F, () -> Ingredient.of(ModItems.CRYSTAL_PINK_INGOT.get())));
    public static final DeferredHolder<ArmorMaterial, ArmorMaterial> TIGERSEYE = ARMOR_MATERIALS.register(
            "tigerseye",
            () -> material("tigerseye", 4, 7, 8, 4, 55, 2.0F, () -> Ingredient.of(ModItems.TIGERSEYE_INGOT.get())));

    private static ArmorMaterial material(
            String name,
            int boots,
            int legs,
            int chest,
            int helmet,
            int enchantability,
            float toughness,
            java.util.function.Supplier<Ingredient> repair) {
        EnumMap<ArmorItem.Type, Integer> defense = Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
            map.put(ArmorItem.Type.BOOTS, boots);
            map.put(ArmorItem.Type.LEGGINGS, legs);
            map.put(ArmorItem.Type.CHESTPLATE, chest);
            map.put(ArmorItem.Type.HELMET, helmet);
            map.put(ArmorItem.Type.BODY, chest);
        });
        List<ArmorMaterial.Layer> layers =
                List.of(new ArmorMaterial.Layer(ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, name)));
        return new ArmorMaterial(
                defense,
                enchantability,
                SoundEvents.ARMOR_EQUIP_DIAMOND,
                repair,
                layers,
                toughness,
                0.0F);
    }

    public static Holder<ArmorMaterial> ultimate() {
        return ULTIMATE;
    }

    public static Holder<ArmorMaterial> emerald() {
        return EMERALD;
    }

    public static Holder<ArmorMaterial> moth() {
        return MOTH;
    }

    public static Holder<ArmorMaterial> amethyst() {
        return AMETHYST;
    }

    public static Holder<ArmorMaterial> ruby() {
        return RUBY;
    }

    public static Holder<ArmorMaterial> experience() {
        return EXPERIENCE;
    }

    public static Holder<ArmorMaterial> lavaEel() {
        return LAVA_EEL;
    }

    public static Holder<ArmorMaterial> peacock() {
        return PEACOCK;
    }

    public static Holder<ArmorMaterial> mobzilla() {
        return MOBZILLA;
    }

    public static Holder<ArmorMaterial> lapis() {
        return LAPIS;
    }

    public static Holder<ArmorMaterial> royal() {
        return ROYAL;
    }

    public static Holder<ArmorMaterial> queen() {
        return QUEEN;
    }

    public static Holder<ArmorMaterial> pink() {
        return PINK;
    }

    public static Holder<ArmorMaterial> tigersEye() {
        return TIGERSEYE;
    }

    private OrespawnArmorMaterial() {}
}
