package me.scwunge.mods.portalgun.client.sound;

import me.scwunge.mods.portalgun.client.ClientGrabStatus;
import me.scwunge.mods.portalgun.init.ModSounds;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance.Attenuation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;

public class PortalGunGrabSound extends AbstractTickableSoundInstance {
   public PortalGunGrabSound() {
      super((SoundEvent)ModSounds.GRAB_LOOP.get(), SoundSource.PLAYERS, RandomSource.create());
      this.looping = true;
      this.delay = 0;
      this.volume = 0.35F;
      this.pitch = 1.0F;
      this.attenuation = Attenuation.NONE;
      this.relative = true;
      this.x = 0.0;
      this.y = 0.0;
      this.z = 0.0;
   }

   public void tick() {
      if (!ClientGrabStatus.isHolding()) {
         this.stop();
      } else {
         Player p = Minecraft.getInstance().player;
         if (p == null) {
            this.stop();
         } else {
            this.x = p.getX();
            this.y = p.getY();
            this.z = p.getZ();
         }
      }
   }
}
