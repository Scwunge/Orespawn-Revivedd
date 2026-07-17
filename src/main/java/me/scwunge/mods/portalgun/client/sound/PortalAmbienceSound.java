package me.scwunge.mods.portalgun.client.sound;

import me.scwunge.mods.portalgun.block.PortalBlock;
import me.scwunge.mods.portalgun.init.ModSounds;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance.Attenuation;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;

public class PortalAmbienceSound extends AbstractTickableSoundInstance {
   private final Level level;
   private final BlockPos portalPos;

   public PortalAmbienceSound(Level level, BlockPos portalPos, float volume, float pitch) {
      super((SoundEvent)ModSounds.PORTAL_AMBIENT.get(), SoundSource.AMBIENT, RandomSource.create());
      this.level = level;
      this.portalPos = portalPos.immutable();
      this.looping = true;
      this.delay = 0;
      this.volume = volume;
      this.pitch = pitch;
      this.x = (double)portalPos.getX() + 0.5;
      this.y = (double)portalPos.getY() + 0.5;
      this.z = (double)portalPos.getZ() + 0.5;
      this.attenuation = Attenuation.LINEAR;
      this.relative = false;
   }

   public void tick() {
      if (this.level != null && this.level.getBlockState(this.portalPos).getBlock() instanceof PortalBlock) {
         this.x = (double)this.portalPos.getX() + 0.5;
         this.y = (double)this.portalPos.getY() + 0.5;
         this.z = (double)this.portalPos.getZ() + 0.5;
      } else {
         this.stop();
      }
   }

   public BlockPos getPortalPos() {
      return this.portalPos;
   }
}
