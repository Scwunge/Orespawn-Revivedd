package danger.orespawn.items.tools;

import danger.orespawn.OreSpawnMain;
import danger.orespawn.entity.BerthaHit;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;

/**
 * 1.7.10 {@code MySlice} — gold registers as {@code new Bertha(., toolBERTHA)} named
 * {@code slicesmall}, with dedicated {@code ModelSlice}/{@code RenderSlice}.
 * <p>
 * Combat/swing family matches Bertha: auto Sharpness 5 / Knockback 1 / Fire Aspect 1,
 * swing fires {@link BerthaHit}. Inventory icon: {@code slicesmall}; in-hand model texture:
 * {@code slicetexture}.
 * <p>
 * Note: gold also ships an unused {@code Slice extends ItemSword} with slightly different
 * enchants/PVP; live item is the Bertha registration — this class follows live gold.
 */
public class Slice extends SwordItem {
    public Slice(Properties properties) {
        super(OrespawnToolMaterial.BerthaTools.tier, properties);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        // gold Bertha path used by MySlice: Sharpness 5, Knockback 1, Fire Aspect 1
        ToolEnchantHelper.applyAllIfUnenchanted(
                stack,
                level,
                new ToolEnchantHelper.EnchantSpec(Enchantments.SHARPNESS, 5),
                new ToolEnchantHelper.EnchantSpec(Enchantments.KNOCKBACK, 1),
                new ToolEnchantHelper.EnchantSpec(Enchantments.FIRE_ASPECT, 1));
        super.inventoryTick(stack, level, entity, slotId, isSelected);
    }

    @Override
    public boolean onLeftClickEntity(ItemStack stack, Player player, Entity entity) {
        if (entity != null && OreSpawnMain.big_bertha_pvp == 0) {
            if (entity instanceof Player) {
                return true; // cancel hit
            }
            if (entity instanceof OwnableEntity ownable && ownable.getOwner() != null) {
                return true;
            }
        }
        return false;
    }

    /** NeoForge / Item swing hook — fires BerthaHit arc (same as gold MySlice / Bertha). */
    @Override
    public boolean onEntitySwing(ItemStack stack, LivingEntity entityLiving) {
        if (entityLiving instanceof Player player && !entityLiving.level().isClientSide) {
            BerthaHit hit = new BerthaHit(player.level(), player);
            hit.shootFromPlayer(player);
            player.level().addFreshEntity(hit);
            stack.hurtAndBreak(1, player, LivingEntity.getSlotForHand(player.getUsedItemHand()));
        }
        return false;
    }
}
