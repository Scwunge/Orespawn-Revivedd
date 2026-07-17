package me.scwunge.mods.portalgun.portal;

import me.scwunge.mods.portalgun.entity.PortalProjectile;
import me.scwunge.mods.portalgun.init.ModSounds;
import me.scwunge.mods.portalgun.item.PortalGunData;
import me.scwunge.mods.portalgun.item.PortalGunItem;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.EntityTickEvent.Post;

@EventBusSubscriber(
   modid = "orespawn"
)
public final class PortalGunZombieAI {
   private PortalGunZombieAI() {
   }

   @SubscribeEvent
   public static void onEntityTick(Post event) {
      if (event.getEntity() instanceof Zombie zombie) {
         if (!zombie.level().isClientSide() && zombie.level() instanceof ServerLevel level) {
            ItemStack main = zombie.getMainHandItem();
            if (main.getItem() instanceof PortalGunItem) {
               if (!(zombie.getRandom().nextFloat() >= 0.008F)) {
                  PortalGunData.ensureDefaults(main);
                  boolean orange = zombie.getRandom().nextBoolean();
                  PortalGunData.setLastOrange(main, orange);
                  PortalProjectile proj = new PortalProjectile(level, zombie);
                  proj.setOrange(orange);
                  proj.setPortalSize(PortalGunData.width(main), PortalGunData.height(main));
                  proj.setChannel(PortalGunData.channel(main));
                  proj.shootFromRotation(zombie, zombie.getXRot(), zombie.getYRot(), 0.0F, 2.2F, 1.0F);
                  level.addFreshEntity(proj);
                  level.playSound(
                     null,
                     zombie.getX(),
                     zombie.getY(),
                     zombie.getZ(),
                     orange ? (SoundEvent)ModSounds.GUN_FIRE_RED.get() : (SoundEvent)ModSounds.GUN_FIRE_BLUE.get(),
                     SoundSource.HOSTILE,
                     0.35F,
                     1.0F + (zombie.getRandom().nextFloat() - zombie.getRandom().nextFloat()) * 0.1F
                  );
               }
            }
         }
      }
   }
}
