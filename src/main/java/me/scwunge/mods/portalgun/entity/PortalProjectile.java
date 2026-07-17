package me.scwunge.mods.portalgun.entity;

import me.scwunge.mods.portalgun.block.PortalBlock;
import me.scwunge.mods.portalgun.config.PortalGunConfig;
import me.scwunge.mods.portalgun.init.ModEntities;
import me.scwunge.mods.portalgun.portal.ChunkLoadHandler;
import me.scwunge.mods.portalgun.portal.PortalGunHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.network.syncher.SynchedEntityData.Builder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity.RemovalReason;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ClipContext.Block;
import net.minecraft.world.level.ClipContext.Fluid;
import net.minecraft.world.level.block.IronBarsBlock;
import net.minecraft.world.level.block.StainedGlassBlock;
import net.minecraft.world.level.block.StainedGlassPaneBlock;
import net.minecraft.world.level.block.TintedGlassBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.HitResult.Type;
import net.neoforged.neoforge.common.Tags.Blocks;
import org.joml.Vector3f;

public class PortalProjectile extends ThrowableItemProjectile {
   private static final EntityDataAccessor<Boolean> ORANGE = SynchedEntityData.defineId(PortalProjectile.class, EntityDataSerializers.BOOLEAN);
   private static final double DEFAULT_MAX_DISTANCE = 256.0;
   private static final int MAX_LIFETIME_TICKS = 200;
   private static final int MAX_PASS_PER_TICK = 12;
   private int portalWidth = 1;
   private int portalHeight = 2;
   private String channel = "Chell";
   private String portalOwnerKey = "";
   private Vec3 spawnPos;

   public PortalProjectile(EntityType<? extends PortalProjectile> type, Level level) {
      super(type, level);
      this.noPhysics = false;
   }

   public PortalProjectile(Level level, LivingEntity shooter) {
      super((EntityType)ModEntities.PORTAL_PROJECTILE.get(), shooter, level);
      this.spawnPos = shooter.getEyePosition();
   }

   protected void defineSynchedData(Builder builder) {
      super.defineSynchedData(builder);
      builder.define(ORANGE, false);
   }

   public void setOrange(boolean orange) {
      this.entityData.set(ORANGE, orange);
   }

   public boolean isOrange() {
      return (Boolean)this.entityData.get(ORANGE);
   }

   public void setPortalSize(int width, int height) {
      this.portalWidth = Math.max(1, width);
      this.portalHeight = Math.max(1, height);
   }

   public void setChannel(String channel) {
      this.channel = channel != null && !channel.isBlank() ? channel : "Chell";
   }

   public String getChannel() {
      return this.channel != null && !this.channel.isBlank() ? this.channel : "Chell";
   }

   public void setPortalOwnerKey(String key) {
      this.portalOwnerKey = key == null ? "" : key;
   }

   public String getPortalOwnerKey() {
      return this.portalOwnerKey;
   }

   public int getTrailColour() {
      String owner = this.portalOwnerKey != null && !this.portalOwnerKey.isBlank()
         ? this.portalOwnerKey
         : (this.getOwner() != null ? this.getOwner().getUUID().toString() : "");
      int[] cols = PortalGunHelper.coloursForChannel(owner, this.getChannel());
      return (this.isOrange() ? cols[1] : cols[0]) & 16777215;
   }

   protected Item getDefaultItem() {
      return Items.ENDER_PEARL;
   }

   public boolean isPushedByFluid() {
      return !canFireThroughLiquid() && super.isPushedByFluid();
   }

   public void tick() {
      this.baseTick();
      if (this.spawnPos == null) {
         this.spawnPos = this.position();
      }

      if (this.level().isClientSide) {
         this.spawnTrailParticles();
      }

      if (!this.level().isClientSide) {
         if (this.tickCount > 200) {
            this.discard();
            return;
         }

         double maxDist = maxShootDistance();
         if (this.position().distanceToSqr(this.spawnPos) > maxDist * maxDist) {
            this.discard();
            return;
         }
      }

      Vec3 motion = this.getDeltaMovement();
      if (!(motion.lengthSqr() < 1.0E-10)) {
         Vec3 start = this.position();
         Vec3 end = start.add(motion);
         Fluid fluidMode = canFireThroughLiquid() ? Fluid.NONE : Fluid.ANY;
         Vec3 cursor = start;
         boolean placedOrStopped = false;

         for (int pass = 0; pass < 12; pass++) {
            BlockHitResult hit = this.level().clip(new ClipContext(cursor, end, Block.COLLIDER, fluidMode, this));
            if (hit.getType() == Type.MISS) {
               cursor = end;
               break;
            }

            BlockPos hitPos = hit.getBlockPos();
            BlockState state = this.level().getBlockState(hitPos);
            if (!this.shouldPassThrough(state)) {
               if (!this.level().isClientSide && this.level() instanceof ServerLevel serverLevel && this.getOwner() instanceof LivingEntity living) {
                  String ownerKey = this.portalOwnerKey;
                  if (ownerKey == null || ownerKey.isBlank()) {
                     ownerKey = living instanceof Player ? living.getUUID().toString() : "mob-" + living.getUUID();
                  }

                  if (living instanceof Player player) {
                     PortalGunHelper.tryPlaceFromHit(serverLevel, player, hit, this.isOrange(), this.portalWidth, this.portalHeight, ownerKey, this.channel);
                  } else {
                     PortalGunHelper.tryPlaceFromHitMob(serverLevel, living, hit, this.isOrange(), this.portalWidth, this.portalHeight, ownerKey, this.channel);
                  }
               }

               this.discard();
               placedOrStopped = true;
               break;
            }

            Vec3 dir = end.subtract(cursor);
            double dlen = dir.length();
            Vec3 nudge = dlen > 1.0E-6 ? dir.scale(0.05 / dlen) : motion.normalize().scale(0.05);
            cursor = hit.getLocation().add(nudge);
            if (cursor.subtract(start).lengthSqr() >= motion.lengthSqr()) {
               cursor = end;
               break;
            }
         }

         if (!placedOrStopped && !this.isRemoved()) {
            this.setPos(cursor.x, cursor.y, cursor.z);
            this.updateRotation();
            float drag = this.isInWater() && !canFireThroughLiquid() ? 0.8F : 0.99F;
            Vec3 newMotion = motion.scale((double)drag);
            newMotion = newMotion.add(0.0, -this.getDefaultGravity(), 0.0);
            this.setDeltaMovement(newMotion);
            this.hasImpulse = true;
         }
      }
   }

   protected void onHit(HitResult result) {
   }

   private void spawnTrailParticles() {
      Vector3f rgb = Vec3.fromRGB24(this.getTrailColour()).toVector3f();
      Vec3 p = this.position();
      this.level().addParticle(new DustParticleOptions(rgb, 0.85F), p.x, p.y, p.z, 0.0, 0.0, 0.0);
      if (this.random.nextInt(3) == 0) {
         this.level().addParticle(ParticleTypes.CRIT, p.x, p.y, p.z, 0.0, 0.0, 0.0);
      }
   }

   private boolean shouldPassThrough(BlockState state) {
      if (state.isAir()) {
         return true;
      } else if (state.getBlock() instanceof PortalBlock) {
         return true;
      } else if (canFireThroughGlass() && isGlassLike(state)) {
         return true;
      } else {
         return canFireThroughLiquid() && isLiquidLike(state) ? true : !state.canOcclude() && state.getCollisionShape(this.level(), BlockPos.ZERO).isEmpty();
      }
   }

   private static boolean isGlassLike(BlockState state) {
      try {
         if (state.is(Blocks.GLASS_BLOCKS) || state.is(Blocks.GLASS_PANES)) {
            return true;
         }
      } catch (Throwable var2) {
      }

      return !state.is(net.minecraft.world.level.block.Blocks.GLASS)
            && !state.is(net.minecraft.world.level.block.Blocks.GLASS_PANE)
            && !state.is(net.minecraft.world.level.block.Blocks.TINTED_GLASS)
            && !(state.getBlock() instanceof TintedGlassBlock)
            && !(state.getBlock() instanceof StainedGlassBlock)
            && !(state.getBlock() instanceof StainedGlassPaneBlock)
         ? state.getBlock() instanceof IronBarsBlock && state.getBlock().builtInRegistryHolder().key().location().getPath().contains("glass")
         : true;
   }

   private static boolean isLiquidLike(BlockState state) {
      return !state.getFluidState().isEmpty()
         || state.getFluidState().is(FluidTags.WATER)
         || state.getFluidState().is(FluidTags.LAVA)
         || state.is(net.minecraft.world.level.block.Blocks.WATER)
         || state.is(net.minecraft.world.level.block.Blocks.LAVA)
         || state.is(net.minecraft.world.level.block.Blocks.BUBBLE_COLUMN);
   }

   private static boolean canFireThroughGlass() {
      try {
         return (Boolean)PortalGunConfig.CAN_FIRE_THROUGH_GLASS.get();
      } catch (Throwable var1) {
         return true;
      }
   }

   private static boolean canFireThroughLiquid() {
      try {
         return (Boolean)PortalGunConfig.CAN_FIRE_THROUGH_LIQUID.get();
      } catch (Throwable var1) {
         return false;
      }
   }

   public void remove(RemovalReason reason) {
      if (!this.level().isClientSide) {
         ChunkLoadHandler.release(this);
      }

      super.remove(reason);
   }

   private static double maxShootDistance() {
      try {
         return (double)((Integer)PortalGunConfig.MAX_SHOOT_DISTANCE.get()).intValue();
      } catch (Exception var1) {
         return 256.0;
      }
   }
}
