package danger.orespawn.items;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

/**
 * Gold {@code ItemPizza} — places {@code pizza} block. Stack size 1
 * ({@code setMaxStackSize(1)}), creative food tab in gold.
 * <p>
 * Registry name gold: {@code pizza}. Register as {@code ModItems.PIZZA}
 * with {@code stacksTo(1)} and the pizza block.
 */
public class ItemPizza extends BlockItem {
    public ItemPizza(Block block, Item.Properties properties) {
        super(block, properties);
    }
}
