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
 * 1.7.10 {@code MyBattleAxe} — gold registers as
 * {@code new UltimateSword(BaseItemID+422, toolBATTLE).setUnlocalizedName("battleaxesmall")}
 * with dedicated {@code ModelBattleAxe}/{@code RenderBattleAxe} (NOT Bertha).
 * <p>
 * Gold combat (UltimateSword special-cases MyBattleAxe):
 * <ul>
 *   <li>Auto-enchants: Fire Aspect + Looting only (no full UltimateSword kit)</li>
 *   <li>Levels: {@code 1 + UltimateSwordMagic/2} (default magic 5 → level 3 each)</li>
 *   <li>toolBATTLE: harvest 3, uses 1500, eff 15, damage 46, ench 75
 *       ({@code get_weaponstats(., "BattleAxe", 3, 1500, 15, 46, 75)})</li>
 *   <li>UltimateSword constructor also {@code setMaxDamage(3000)} — live durability 3000</li>
 *   <li>PVP gate: {@code ultimate_sword_pvp} (not big_bertha_pvp)</li>
 * </ul>
 * Inventory icon: {@code battleaxesmall}; in-hand texture: {@code BattleAxetexture.png}.
 */
public class BattleAxe extends SwordItem {
    /** Gold config default {@code UltimateSwordMagic}. */
    public static final int ULTIMATE_SWORD_MAGIC = 5;

    /** Gold material max uses (toolBATTLE); live UltimateSword forces 3000. */
    public static final int BATTLE_AXE_MATERIAL_USES = 1500;
    /** Live durability from UltimateSword constructor {@code setMaxDamage(3000)}. */
    public static final int BATTLE_AXE_USES = 3000;
    public static final float BATTLE_AXE_DAMAGE = 46.0F;
    public static final float BATTLE_AXE_EFFICIENCY = 15.0F;
    public static final int BATTLE_AXE_ENCHANTABILITY = 75;

    /**
     * Gold {@code OreSpawnMain.ultimate_sword_pvp} (default 0 = no player/pet damage).
     * May be rebound from config; not yet on OreSpawnMain.
     */
    public static int ultimate_sword_pvp = 0;

    public static final Tier BATTLE_AXE_TIER = new SimpleTier(
            BlockTags.INCORRECT_FOR_DIAMOND_TOOL,
            BATTLE_AXE_USES,
            BATTLE_AXE_EFFICIENCY,
            BATTLE_AXE_DAMAGE,
            BATTLE_AXE_ENCHANTABILITY,
            () -> Ingredient.of(ModItems.URANIUM_INGOT.get(), ModItems.TITANIUM_INGOT.get()));

    public BattleAxe(Properties properties) {
        super(BATTLE_AXE_TIER, properties);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        // gold MyBattleAxe branch: Fire Aspect + Looting only (field_77335_o / field_77347_r)
        int mid = 1 + ULTIMATE_SWORD_MAGIC / 2;
        ToolEnchantHelper.applyAllIfUnenchanted(
                stack,
                level,
                new ToolEnchantHelper.EnchantSpec(Enchantments.FIRE_ASPECT, mid),
                new ToolEnchantHelper.EnchantSpec(Enchantments.LOOTING, mid));
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
