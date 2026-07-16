package danger.orespawn.util;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

/**
 * 1.7.10 ant / termite / butterfly empty-hand dimension toggle.
 * <p>
 * Gold rule: if player is <b>not</b> in {@code targetDim} → go there; if already there → Overworld.
 * Any source dimension is allowed (not overworld-only).
 */
public final class AntDimensionPortal {
    private AntDimensionPortal() {}

    /**
     * @return true if a teleport was attempted (hand was empty on server player)
     */
    public static boolean tryToggle(
            Player player, ItemStack held, ResourceKey<Level> targetDim, String enterMessage) {
        if (player == null || player.level().isClientSide) {
            return false;
        }
        if (!(player instanceof ServerPlayer serverPlayer)) {
            return false;
        }
        if (!held.isEmpty()) {
            return false;
        }

        ResourceKey<Level> current = serverPlayer.level().dimension();
        boolean enter = current != targetDim;
        ResourceKey<Level> dest = enter ? targetDim : Level.OVERWORLD;
        String msg = enter ? enterMessage : "Warped back to the Overworld.";

        String err = Teleport.teleportToDimension(
                serverPlayer, dest, serverPlayer.getX(), serverPlayer.getY(), serverPlayer.getZ());
        if (err == null) {
            serverPlayer.displayClientMessage(Component.literal(msg), true);
        } else {
            serverPlayer.displayClientMessage(Component.literal("Teleport failed: " + err), true);
        }
        return true;
    }

    /** 1.7.10 Termite: enter Crystal only with empty inventory and no armor. */
    public static boolean inventoryEmptyForCrystal(ServerPlayer player) {
        for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
            if (!player.getInventory().getItem(i).isEmpty()) {
                return false;
            }
        }
        return player.getInventory().armor.stream().allMatch(ItemStack::isEmpty)
                && player.getOffhandItem().isEmpty();
    }
}
