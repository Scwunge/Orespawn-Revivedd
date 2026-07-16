package danger.orespawn.items.tools;

import danger.orespawn.init.ModBlocks;
import danger.orespawn.init.ModItems;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.SimpleTier;

import java.util.function.Supplier;

/**
 * Gold {@code OrespawnToolMaterial} — 1:1 stats for NeoForge 1.21.1 {@link Tier}.
 *
 * <pre>
 * Ultimate: harvest 10, dur 3000, eff 15, dmg 36, ench 100
 * Emerald:  2, 1000, 6.5, 3, 12
 * Amethyst: same as emerald in gold (bug — keep 1:1)
 * </pre>
 */
public final class OrespawnToolMaterial {
    public final String name;
    public final int harvestLevel;
    public final int durability;
    public final float efficiency;
    /** Gold material attack-damage bonus (EnumHelper damage). */
    public final float damage;
    public final int enchantability;
    public final Tier tier;

    /**
     * Gold: Ultimate harvest 10 / 3000 / 15 / 36 / 100.
     * incorrectBlocks: netherite (least restrictive vanilla tag; harvest 10 ≥ netherite).
     */
    public static final OrespawnToolMaterial UltimateTools = new OrespawnToolMaterial(
            "ultimate",
            10,
            3000,
            15.0F,
            36.0F,
            100,
            BlockTags.INCORRECT_FOR_NETHERITE_TOOL,
            () -> Ingredient.of(ModItems.URANIUM_INGOT.get(), ModItems.TITANIUM_INGOT.get()));

    /**
     * Gold: Emerald harvest 2 / 1000 / 6.5 / 3 / 12 (iron-equivalent harvest).
     */
    public static final OrespawnToolMaterial EmeraldTools = new OrespawnToolMaterial(
            "emerald",
            2,
            1000,
            6.5F,
            3.0F,
            12,
            BlockTags.INCORRECT_FOR_IRON_TOOL,
            () -> Ingredient.of(Items.EMERALD));

    /**
     * Gold copies emerald stats (name even registered as "emerald") — keep 1:1.
     * Repair with orespawn amethyst gem.
     */
    public static final OrespawnToolMaterial AmethystTools = new OrespawnToolMaterial(
            "amethyst",
            2,
            1000,
            6.5F,
            3.0F,
            12,
            BlockTags.INCORRECT_FOR_IRON_TOOL,
            () -> Ingredient.of(ModItems.AMETHYST.get()));

    /**
     * 1.7.10 {@code toolBERTHA} defaults: harvest 3, uses 9000, eff 15, damage 496, ench 100.
     * ({@code get_weaponstats(., "Bertha", 3, 9000, 15, 496, 100)}).
     */
    public static final OrespawnToolMaterial BerthaTools = new OrespawnToolMaterial(
            "bertha",
            3,
            9000,
            15.0F,
            496.0F,
            100,
            BlockTags.INCORRECT_FOR_NETHERITE_TOOL,
            () -> Ingredient.of(ModItems.URANIUM_INGOT.get(), ModItems.TITANIUM_INGOT.get()));

    /**
     * 1.7.10 Nightmare defaults: harvest 3, 1800 uses, eff 12, damage 26, ench 60.
     */
    public static final OrespawnToolMaterial NightmareTools = new OrespawnToolMaterial(
            "nightmare",
            3,
            1800,
            12.0F,
            26.0F,
            60,
            BlockTags.INCORRECT_FOR_DIAMOND_TOOL,
            () -> Ingredient.of(ModItems.URANIUM_INGOT.get()));

    /** 1.7.10 CrystalWood: 2, 300, 3, 2, 15 — repair with crystal planks. */
    public static final OrespawnToolMaterial CrystalWoodTools = new OrespawnToolMaterial(
            "crystalwood",
            2,
            300,
            3.0F,
            2.0F,
            15,
            BlockTags.INCORRECT_FOR_WOODEN_TOOL,
            () -> Ingredient.of(ModBlocks.CRYSTAL_PLANKS.get()));

    /** 1.7.10 CrystalStone: 3, 800, 6, 5, 45 — repair with crystal stone. */
    public static final OrespawnToolMaterial CrystalStoneTools = new OrespawnToolMaterial(
            "crystalstone",
            3,
            800,
            6.0F,
            5.0F,
            45,
            BlockTags.INCORRECT_FOR_STONE_TOOL,
            () -> Ingredient.of(ModBlocks.CRYSTAL_STONE.get()));

    /** 1.7.10 Pink (crystal pink): 4, 1100, 10, 7, 65 */
    public static final OrespawnToolMaterial CrystalPinkTools = new OrespawnToolMaterial(
            "crystalpink",
            4,
            1100,
            10.0F,
            7.0F,
            65,
            BlockTags.INCORRECT_FOR_DIAMOND_TOOL,
            () -> Ingredient.of(ModItems.CRYSTAL_PINK_INGOT.get()));

    /** 1.7.10 Ruby: 5, 1500, 11, 16, 85 */
    public static final OrespawnToolMaterial RubyTools = new OrespawnToolMaterial(
            "ruby",
            5,
            1500,
            11.0F,
            16.0F,
            85,
            BlockTags.INCORRECT_FOR_DIAMOND_TOOL,
            () -> Ingredient.of(ModItems.RUBY.get()));

    /** 1.7.10 TigersEye: 4, 1600, 12, 8, 75 */
    public static final OrespawnToolMaterial TigersEyeTools = new OrespawnToolMaterial(
            "tigerseye",
            4,
            1600,
            12.0F,
            8.0F,
            75,
            BlockTags.INCORRECT_FOR_DIAMOND_TOOL,
            () -> Ingredient.of(ModItems.TIGERSEYE_INGOT.get()));

    private OrespawnToolMaterial(
            String name,
            int harvestLevel,
            int durability,
            float efficiency,
            float damage,
            int enchantability,
            TagKey<Block> incorrectBlocksForDrops,
            Supplier<Ingredient> repair) {
        this.name = name;
        this.harvestLevel = harvestLevel;
        this.durability = durability;
        this.efficiency = efficiency;
        this.damage = damage;
        this.enchantability = enchantability;
        this.tier = new SimpleTier(
                incorrectBlocksForDrops,
                durability,
                efficiency,
                damage,
                enchantability,
                repair);
    }

    /**
     * Gold {@code ItemSword}: base 3 + material damage, attack speed -2.4.
     * 1.21: {@code createAttributes(tier, 3, -2.4F)}.
     */
    public float swordDamageBonus() {
        return 3.0F;
    }

    public float swordAttackSpeed() {
        return -2.4F;
    }

    /**
     * Gold {@code ItemPickaxe}: 1.0 + material, speed -2.8.
     */
    public float pickaxeDamageBonus() {
        return 1.0F;
    }

    public float pickaxeAttackSpeed() {
        return -2.8F;
    }

    /**
     * Gold axes: {@code super(material, material.Damage, -3.0F)} → total dmg = Damage + material dmg = 2×.
     * 1.21: pass {@link #damage} so total = damage + tier.bonus.
     */
    public float axeDamageBonus() {
        return this.damage;
    }

    public float axeAttackSpeed() {
        return -3.0F;
    }

    /**
     * Gold {@code ItemSpade}: 1.5 + material, speed -3.0.
     */
    public float shovelDamageBonus() {
        return 1.5F;
    }

    public float shovelAttackSpeed() {
        return -3.0F;
    }

    /**
     * Gold {@code ItemHoe}: attack damage attribute 0; speed = (material.dmg + 1) - 4.
     * 1.21: pass {@code -damage} so total with tier bonus is 0.
     */
    public float hoeDamageBonus() {
        return -this.damage;
    }

    public float hoeAttackSpeed() {
        return (this.damage + 1.0F) - 4.0F;
    }

    private OrespawnToolMaterial() {
        throw new AssertionError();
    }
}
