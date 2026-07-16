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
 * 1.7.10 {@code Bertha} (Big Bertha) — OP sword, auto Sharpness/Knockback/Fire Aspect,
 * swing fires {@link BerthaHit}. Texture: {@code berthasmall}.
 */
public class Bertha extends SwordItem {
    public Bertha(Properties properties) {
        super(OrespawnToolMaterial.BerthaTools.tier, properties);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        // gold: Sharpness 5, Knockback 1, Fire Aspect 1 (field_77337_m / field_77336_l / field_77334_n)
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

    /** NeoForge / Item swing hook — fires BerthaHit arc. */
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
