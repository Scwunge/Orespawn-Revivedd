package danger.orespawn.items.tools;

import danger.orespawn.OreSpawnMain;
import danger.orespawn.entity.BerthaHit;
import danger.orespawn.init.ModItems;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
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
 * 1.7.10 {@code MyRoyal} ("Royal Guardian Sword") — gold registers as
 * {@code new Bertha(., toolROYAL).setUnlocalizedName("royalsmall")} with dedicated
 * {@code RenderRoyal} (reuses {@code ModelSlice} mesh) + {@code Royaltexture.png}.
 * <p>
 * Gold combat differences vs Big Bertha / Slice:
 * <ul>
 *   <li>Auto-enchant is <b>Unbreaking 5 only</b> ({@code Enchantment.field_77347_r}),
 *       not Sharpness / Knockback / Fire Aspect</li>
 *   <li>Swing fires {@link BerthaHit} with {@code setHitType(2)} (royal damage, no fire)</li>
 *   <li>toolROYAL defaults: harvest 3, uses 10000, eff 15, damage 746, ench 150
 *       ({@code get_weaponstats(., "Royal", 3, 10000, 15, 746, 150)})</li>
 * </ul>
 * Inventory icon: {@code royalsmall}; in-hand model texture: gold {@code Royaltexture.png}.
 */
public class Royal extends SwordItem {
    /**
     * Gold {@code toolROYAL} / {@code royal_stats}: harvest 3, 10000 uses, eff 15, damage 746, ench 150.
     * Constants for {@code ModItems} / tool material wiring.
     */
    public static final float ROYAL_DAMAGE = 746.0F;
    public static final int ROYAL_USES = 10000;
    public static final float ROYAL_EFFICIENCY = 15.0F;
    public static final int ROYAL_ENCHANTABILITY = 150;

    public static final Tier ROYAL_TIER = new SimpleTier(
            BlockTags.INCORRECT_FOR_NETHERITE_TOOL,
            ROYAL_USES,
            ROYAL_EFFICIENCY,
            ROYAL_DAMAGE,
            ROYAL_ENCHANTABILITY,
            () -> Ingredient.of(ModItems.URANIUM_INGOT.get(), ModItems.TITANIUM_INGOT.get()));

    public Royal(Properties properties) {
        super(ROYAL_TIER, properties);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        // gold MyRoyal: Unbreaking 5 only (field_77347_r) — not Sharpness/Knockback/Fire Aspect
        ToolEnchantHelper.applyAllIfUnenchanted(
                stack,
                level,
                new ToolEnchantHelper.EnchantSpec(Enchantments.UNBREAKING, 5));
        super.inventoryTick(stack, level, entity, slotId, isSelected);
    }

    @Override
    public boolean onLeftClickEntity(ItemStack stack, Player player, Entity entity) {
        // same big_bertha_pvp gate as gold Bertha (MyRoyal is a Bertha subclass in 1.7)
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

    /**
     * Gold {@code Bertha.onEntitySwing}: fires BerthaHit and {@code setHitType(2)} for MyRoyal.
     * BerthaHit hit_type 2 (dmg 746, longer range, no fire).
     */
    @Override
    public boolean onEntitySwing(ItemStack stack, LivingEntity entityLiving) {
        if (entityLiving instanceof Player player && !entityLiving.level().isClientSide) {
            BerthaHit hit = new BerthaHit(player.level(), player);
            hit.setHitType(2);
            hit.shootFromPlayer(player);
            player.level().addFreshEntity(hit);
            stack.hurtAndBreak(1, player, LivingEntity.getSlotForHand(player.getUsedItemHand()));
        }
        return false;
    }
}
