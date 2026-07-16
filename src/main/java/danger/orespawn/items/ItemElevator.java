package danger.orespawn.items;

import danger.orespawn.entity.Elevator;
import danger.orespawn.init.ModEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

/**
 * Gold {@code ItemElevator} — registers as {@code elevator} (MyElevator), stack 1,
 * transport tab. Places hoverboard {@link Elevator} entity at clicked block center
 * + 1.2 Y (gold {@code EntityList} name {@code "Hoverboard"}).
 * <p>
 * Inventory icon: {@code textures/item/elevator.png}.
 */
public class ItemElevator extends Item {
    /** Gold vertical offset above clicked block. */
    public static final double PLACE_Y_OFFSET = 1.2;

    public ItemElevator(Properties properties) {
        super(properties);
    }

    /**
     * Gold {@code onItemUse}: server-only spawn Elevator at
     * {@code (x+0.5, y+1.2, z+0.5)}, random yaw; consume unless creative.
     */
    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        }

        Player player = context.getPlayer();
        BlockPos pos = context.getClickedPos();
        ItemStack stack = context.getItemInHand();

        Elevator elevator = ModEntities.ELEVATOR.get().create(level);
        if (elevator != null) {
            double x = pos.getX() + 0.5;
            double y = pos.getY() + PLACE_Y_OFFSET;
            double z = pos.getZ() + 0.5;
            elevator.moveTo(x, y, z, level.getRandom().nextFloat() * 360.0F, 0.0F);
            level.addFreshEntity(elevator);
        }

        if (player != null && !player.getAbilities().instabuild) {
            stack.shrink(1);
        }

        return InteractionResult.SUCCESS;
    }
}
