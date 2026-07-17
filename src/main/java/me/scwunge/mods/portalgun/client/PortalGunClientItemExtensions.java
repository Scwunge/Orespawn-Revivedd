package me.scwunge.mods.portalgun.client;

import me.scwunge.mods.portalgun.client.render.PortalGunItemRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;

public final class PortalGunClientItemExtensions implements IClientItemExtensions {
   private BlockEntityWithoutLevelRenderer renderer;

   public BlockEntityWithoutLevelRenderer getCustomRenderer() {
      if (this.renderer == null) {
         this.renderer = new PortalGunItemRenderer(Minecraft.getInstance().getEntityModels());
      }

      return this.renderer;
   }
}
