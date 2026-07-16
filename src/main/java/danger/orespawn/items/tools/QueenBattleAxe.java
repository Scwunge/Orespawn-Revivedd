package danger.orespawn.items.tools;

import danger.orespawn.init.ModItems;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.SimpleTier;

/**
 * 1.7.10 {@code MyQueenBattleAxe} — gold registers as
 * {@code new UltimateSword(BaseItemID+470, toolQUEENBATTLE).setUnlocalizedName("queenbattleaxesmall")}
 * with dedicated {@code ModelQueenBattleAxe}/{@code RenderQueenBattleAxe} (NOT Bertha).
 * <p>
 * Gold combat (UltimateSword — full kit, not BattleAxe special-case):
 * <ul>
 *   <li>Auto-enchants: Sharpness / Smite / Bane / Knockback / Fire Aspect / Looting / Unbreaking</li>
 *   <li>Levels from {@code UltimateSwordMagic} (default 5): Sharpness/Smite/Bane 5;
 *       Knockback/Fire/Looting {@code 1+magic/2}=3; Unbreaking {@code 1+magic/3}=2</li>
 *   <li>toolQUEENBATTLE: harvest 3, uses 2200, eff 15, damage 662, ench 100
 *       ({@code get_weaponstats(., "QueenBattleAxe", 3, 2200, 15, 662, 100)})</li>
 *   <li>UltimateSword constructor also {@code setMaxDamage(3000)} — live durability 3000</li>
 *   <li>PVP gate: {@code ultimate_sword_pvp} (not big_bertha_pvp)</li>
 * </ul>
 * Inventory icon: {@code queenbattleaxesmall}; in-hand texture: {@code QueenBattleAxetexture.png}.
 */
public class QueenBattleAxe extends SwordItem {
    /** Gold config default {@code UltimateSwordMagic}. */
    public static final int ULTIMATE_SWORD_MAGIC = 5;

    /** Gold material max uses (toolQUEENBATTLE / queenbattleaxe_stats). */
    public static final int QUEEN_BATTLE_AXE_MATERIAL_USES = 2200;
    /** Live durability from UltimateSword constructor {@code setMaxDamage(3000)}. */
    public static final int QUEEN_BATTLE_AXE_USES = 3000;
    public static final float QUEEN_BATTLE_AXE_DAMAGE = 662.0F;
    public static final float QUEEN_BATTLE_AXE_EFFICIENCY = 15.0F;
    public static final int QUEEN_BATTLE_AXE_ENCHANTABILITY = 100;

    /**
     * Gold {@code OreSpawnMain.ultimate_sword_pvp} (default 0 = no player/pet damage).
     * May be rebound from config; not yet on OreSpawnMain.
     * Shared default with {@link BattleAxe#ultimate_sword_pvp}.
     */
    public static int ultimate_sword_pvp = 0;

    public static final Tier QUEEN_BATTLE_AXE_TIER = new SimpleTier(
            BlockTags.INCORRECT_FOR_DIAMOND_TOOL,
            QUEEN_BATTLE_AXE_USES,
            QUEEN_BATTLE_AXE_EFFICIENCY,
            QUEEN_BATTLE_AXE_DAMAGE,
            QUEEN_BATTLE_AXE_ENCHANTABILITY,
            () -> Ingredient.of(ModItems.URANIUM_INGOT.get(), ModItems.TITANIUM_INGOT.get()));

    public QueenBattleAxe(Properties properties) {
        super(QUEEN_BATTLE_AXE_TIER, properties);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        // gold UltimateSword full kit (MyQueenBattleAxe is not MyBattleAxe / MyChainsaw special-case)
        int mid = 1 + ULTIMATE_SWORD_MAGIC / 2;
        int low = 1 + ULTIMATE_SWORD_MAGIC / 3;
        ToolEnchantHelper.applyAllIfUnenchanted(
                stack,
                level,
                new ToolEnchantHelper.EnchantSpec(Enchantments.SHARPNESS, ULTIMATE_SWORD_MAGIC),
                new ToolEnchantHelper.EnchantSpec(Enchantments.SMITE, ULTIMATE_SWORD_MAGIC),
                new ToolEnchantHelper.EnchantSpec(Enchantments.BANE_OF_ARTHROPODS, ULTIMATE_SWORD_MAGIC),
                new ToolEnchantHelper.EnchantSpec(Enchantments.KNOCKBACK, mid),
                new ToolEnchantHelper.EnchantSpec(Enchantments.FIRE_ASPECT, mid),
                new ToolEnchantHelper.EnchantSpec(Enchantments.LOOTING, mid),
                new ToolEnchantHelper.EnchantSpec(Enchantments.UNBREAKING, low));
        super.inventoryTick(stack, level, entity, slotId, isSelected);
    }

    @Override
    public boolean onLeftClickEntity(ItemStack stack, Player player, Entity entity) {
        // gold UltimateSword: ultimate_sword_pvp gate (not big_bertha_pvp)
        if (entity != null && ultimate_sword_pvp == 0) {
            if (entity instanceof Player) {
                return true;
            }
            if (entity instanceof OwnableEntity ownable && ownable.getOwner() != null) {
                return true;
            }
        }
        return false;
    }
}
