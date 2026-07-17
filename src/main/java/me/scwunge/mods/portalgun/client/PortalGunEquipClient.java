package me.scwunge.mods.portalgun.client;

import me.scwunge.mods.portalgun.config.PortalGunConfig;
import me.scwunge.mods.portalgun.init.ModSounds;
import me.scwunge.mods.portalgun.item.PortalGunItem;
import net.minecraft.client.Minecraft;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent.Post;

@EventBusSubscriber(
   modid = "orespawn",
   value = {Dist.CLIENT}
)
public final class PortalGunEquipClient {
   private static boolean wasHoldingGun;

   private PortalGunEquipClient() {
   }

   @SubscribeEvent
   public static void onClientTick(Post event) {
      Minecraft mc = Minecraft.getInstance();
      if (mc.player != null && mc.level != null && !mc.isPaused()) {
         boolean holding = isHoldingGun(mc);
         if (holding && !wasHoldingGun && equipSoundEnabled()) {
            mc.level
               .playLocalSound(
                  mc.player.getX(), mc.player.getY(), mc.player.getZ(), (SoundEvent)ModSounds.GUN_EQUIP.get(), SoundSource.PLAYERS, 1.0F, 1.0F, false
               );
         }

         wasHoldingGun = holding;
      } else {
         wasHoldingGun = false;
      }
   }

   private static boolean isHoldingGun(Minecraft mc) {
      ItemStack main = mc.player.getMainHandItem();
      ItemStack off = mc.player.getOffhandItem();
      return main.getItem() instanceof PortalGunItem || off.getItem() instanceof PortalGunItem;
   }

   private static boolean equipSoundEnabled() {
      try {
         return (Boolean)PortalGunConfig.EQUIP_SOUND.get();
      } catch (Exception var1) {
         return true;
      }
   }
}
