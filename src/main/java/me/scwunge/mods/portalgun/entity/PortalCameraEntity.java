package me.scwunge.mods.portalgun.entity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData.Builder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class PortalCameraEntity extends Entity {
   public PortalCameraEntity(EntityType<? extends PortalCameraEntity> type, Level level) {
      super(type, level);
      this.noPhysics = true;
      this.setInvisible(true);
      this.setNoGravity(true);
      this.setSilent(true);
   }

   public void place(double eyeX, double eyeY, double eyeZ, float yaw, float pitch) {
      this.setPos(eyeX, eyeY, eyeZ);
      this.xo = eyeX;
      this.yo = eyeY;
      this.zo = eyeZ;
      this.xOld = eyeX;
      this.yOld = eyeY;
      this.zOld = eyeZ;
      this.setYRot(yaw);
      this.setXRot(pitch);
      this.yRotO = yaw;
      this.xRotO = pitch;
      this.setDeltaMovement(Vec3.ZERO);
   }

   public boolean shouldRenderAtSqrDistance(double distance) {
      return false;
   }

   public boolean isPickable() {
      return false;
   }

   public boolean isAttackable() {
      return false;
   }

   protected void defineSynchedData(Builder builder) {
   }

   protected void readAdditionalSaveData(CompoundTag tag) {
   }

   protected void addAdditionalSaveData(CompoundTag tag) {
   }
}
