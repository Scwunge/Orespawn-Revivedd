package danger.orespawn.items;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

/**
 * Gold {@code ItemDuctTape} — places {@code ducttape} block. Stack size 1
 * ({@code setMaxStackSize(1)}), creative tools tab in gold.
 * <p>
 * Registry name gold: {@code ducttape}. Register as {@code ModItems.DUCTTAPE}
 * with {@code stacksTo(1)} and the duct tape block.
 */
public class ItemDuctTape extends BlockItem {
    public ItemDuctTape(Block block, Item.Properties properties) {
        super(block, properties);
    }
}
