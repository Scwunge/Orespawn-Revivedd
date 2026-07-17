package me.scwunge.mods.portalgun.mixin;

import java.util.List;
import me.scwunge.mods.portalgun.portal.PortalCollisionStrip;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({Entity.class})
public abstract class EntityCollectCollidersMixin {
   @Inject(
      method = {"collectColliders"},
      at = {@At("RETURN")},
      cancellable = true
   )
   private static void portalgun$stripPortalHole(
      Entity entity, Level level, List<VoxelShape> collisions, AABB boundingBox, CallbackInfoReturnable<List<VoxelShape>> cir
   ) {
      if (entity != null) {
         List<VoxelShape> original = (List<VoxelShape>)cir.getReturnValue();
         if (original != null && !original.isEmpty()) {
            List<VoxelShape> filtered = PortalCollisionStrip.filterColliders(entity, original);
            if (filtered != original) {
               cir.setReturnValue(filtered);
            }
         }
      }
   }
}
