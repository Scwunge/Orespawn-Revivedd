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
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.SimpleTier;

/**
 * 1.7.10 {@code MyHammy} ("Attitude Adjuster") — gold registers as
 * {@code new Bertha(., toolHAMMY).setUnlocalizedName("hammysmall")} with dedicated
 * {@code ModelHammy}/{@code RenderHammy}.
 * <p>
 * Gold combat differences vs Big Bertha / Slice:
 * <ul>
 *   <li>No auto Sharpness / Knockback / Fire Aspect (Bertha skips them when {@code this == MyHammy})</li>
 *   <li>Swing fires {@link BerthaHit} with {@code setHitType(3)} (hammy damage + explosion arc)</li>
 *   <li>toolHAMMY defaults: harvest 5, uses 2000, eff 15, damage 82, ench 100
 *       ({@code get_weaponstats(., "Attitude", 5, 2000, 15, 82, 100)})</li>
 * </ul>
 * Inventory icon: {@code hammysmall}; in-hand model texture: gold {@code AttitudeAdjustertexture.png}.
 */
public class Hammy extends SwordItem {
    /**
     * Gold {@code toolHAMMY} / {@code hammy_stats}: harvest 5, 2000 uses, eff 15, damage 82, ench 100.
     * Constants for {@code ModItems} / tool material wiring.
     */
    public static final float HAMMY_DAMAGE = 82.0F;
    public static final int HAMMY_USES = 2000;
    public static final float HAMMY_EFFICIENCY = 15.0F;
    public static final int HAMMY_ENCHANTABILITY = 100;

    public static final Tier HAMMY_TIER = new SimpleTier(
            BlockTags.INCORRECT_FOR_DIAMOND_TOOL,
            HAMMY_USES,
            HAMMY_EFFICIENCY,
            HAMMY_DAMAGE,
            HAMMY_ENCHANTABILITY,
            () -> Ingredient.of(ModItems.URANIUM_INGOT.get(), ModItems.TITANIUM_INGOT.get()));

    public Hammy(Properties properties) {
        super(HAMMY_TIER, properties);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        // gold MyHammy: intentionally no auto-enchants (Bertha path skips when this == MyHammy)
        super.inventoryTick(stack, level, entity, slotId, isSelected);
    }

    @Override
    public boolean onLeftClickEntity(ItemStack stack, Player player, Entity entity) {
        // same big_bertha_pvp gate as gold Bertha (MyHammy is a Bertha subclass in 1.7)
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
     * Gold {@code Bertha.onEntitySwing}: fires BerthaHit and {@code setHitType(3)} for MyHammy.
     * BerthaHit hit_type 3 (dmg 82, explosion).
     */
    @Override
    public boolean onEntitySwing(ItemStack stack, LivingEntity entityLiving) {
        if (entityLiving instanceof Player player && !entityLiving.level().isClientSide) {
            BerthaHit hit = new BerthaHit(player.level(), player);
            hit.setHitType(3);
            hit.shootFromPlayer(player);
            player.level().addFreshEntity(hit);
            stack.hurtAndBreak(1, player, LivingEntity.getSlotForHand(player.getUsedItemHand()));
        }
        return false;
    }
}
