package me.scwunge.mods.portalgun.portal;

import me.scwunge.mods.portalgun.block.PortalBlock;
import me.scwunge.mods.portalgun.item.PortalGunData;
import me.scwunge.mods.portalgun.item.PortalGunItem;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent.ItemCraftedEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent.LeftClickBlock;
import net.neoforged.neoforge.event.level.BlockEvent.BreakEvent;

@EventBusSubscriber(
   modid = "orespawn"
)
public final class PortalGunServerEvents {
   private PortalGunServerEvents() {
   }

   @SubscribeEvent
   public static void onBlockBreak(BreakEvent event) {
      Player player = event.getPlayer();
      BlockState state = event.getState();
      if (!(state.getBlock() instanceof PortalBlock)) {
         if (player != null && holdingPortalGun(player)) {
            event.setCanceled(true);
         }
      }
   }

   @SubscribeEvent(
      priority = EventPriority.LOWEST
   )
   public static void onBlockBreakPost(BreakEvent event) {
      if (!event.isCanceled()) {
         if (event.getLevel() instanceof ServerLevel level) {
            BlockPos var4 = event.getPos();
            BlockState state = event.getState();
            if (!(state.getBlock() instanceof PortalBlock)) {
               PortalGunHelper.fizzlePortalsSupportedBy(level, var4);
            }
         }
      }
   }

   @SubscribeEvent
   public static void onLeftClickBlock(LeftClickBlock event) {
      if (!(event.getLevel().getBlockState(event.getPos()).getBlock() instanceof PortalBlock) && holdingPortalGun(event.getEntity())) {
         event.setCanceled(true);
      }
   }

   private static boolean holdingPortalGun(Player player) {
      ItemStack main = player.getMainHandItem();
      return main.getItem() instanceof PortalGunItem;
   }

   @SubscribeEvent
   public static void onItemCrafted(ItemCraftedEvent event) {
      ItemStack crafted = event.getCrafting();
      if (crafted.getItem() instanceof PortalGunItem && event.getEntity() != null) {
         PortalGunData.applyCraftedTags(crafted, event.getEntity());
      }
   }
}
