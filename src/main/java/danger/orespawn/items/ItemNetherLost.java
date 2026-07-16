package danger.orespawn.items;

import danger.orespawn.items.tools.ToolEnchantHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;

/**
 * Gold {@code ItemNetherLost} — display name <b>Nether Tracker</b> (unlocalized {@code netherlost}).
 * Durability 3000, stack 1; auto Unbreaking II.
 * While held in the Nether, if the block under the player is an end portal frame,
 * replace it with an iron block (gold quirky interaction).
 */
public class ItemNetherLost extends Item {
    public static final int NETHER_LOST_USES = 3000;

    public ItemNetherLost(Properties properties) {
        super(properties);
    }

    @Override
    public void onCraftedBy(ItemStack stack, Level level, Player player) {
        ToolEnchantHelper.applyAllIfUnenchanted(
                stack, level, new ToolEnchantHelper.EnchantSpec(Enchantments.UNBREAKING, 2));
        super.onCraftedBy(stack, level, player);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        ToolEnchantHelper.applyAllIfUnenchanted(
                stack, level, new ToolEnchantHelper.EnchantSpec(Enchantments.UNBREAKING, 2));
        super.inventoryTick(stack, level, entity, slotId, isSelected);

        if (level.isClientSide || !isSelected || !(entity instanceof Player player)) {
            return;
        }
        if (level.dimension() != Level.NETHER) {
            return;
        }
        if (player.getMainHandItem().getItem() != this && player.getOffhandItem().getItem() != this) {
            return;
        }

        BlockPos under = BlockPos.containing(player.getX(), player.getY() - 1.0, player.getZ());
        if (level.getBlockState(under).is(Blocks.END_PORTAL_FRAME)) {
            level.setBlock(under, Blocks.IRON_BLOCK.defaultBlockState(), 3);
        }
    }
}
