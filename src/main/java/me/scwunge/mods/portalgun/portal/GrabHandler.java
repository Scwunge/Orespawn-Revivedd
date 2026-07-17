package me.scwunge.mods.portalgun.portal;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import me.scwunge.mods.portalgun.block.PortalBlock;
import me.scwunge.mods.portalgun.init.ModBlocks;
import me.scwunge.mods.portalgun.init.ModSounds;
import me.scwunge.mods.portalgun.item.PortalGunData;
import me.scwunge.mods.portalgun.item.PortalGunItem;
import me.scwunge.mods.portalgun.network.GrabStatusPayload;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.ClipContext.Block;
import net.minecraft.world.level.ClipContext.Fluid;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.HitResult.Type;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent.PlayerLoggedOutEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent.Post;
import net.neoforged.neoforge.event.tick.EntityTickEvent.Pre;

@EventBusSubscriber(
   modid = "orespawn"
)
public final class GrabHandler {
   public static final double GRAB_REACH = 4.5;
   public static final double HOLD_DIST = 2.0;
   public static final double THROW_SPEED = 1.0;
   private static final double THROW_UPWARD = 0.15;
   private static final Map<UUID, Integer> HELD = new ConcurrentHashMap<>();
   private static final Map<Integer, UUID> HELD_BY_ENTITY = new ConcurrentHashMap<>();

   private GrabHandler() {
   }

   public static boolean isHolding(ServerPlayer player) {
      return HELD.containsKey(player.getUUID());
   }

   public static void toggle(ServerPlayer player) {
      if (player.level() instanceof ServerLevel level) {
         if (holdingPortalGun(player)) {
            Integer id = HELD.remove(player.getUUID());
            if (id != null) {
               HELD_BY_ENTITY.remove(id);
               Entity e = level.getEntity(id);
               if (e != null) {
                  releaseEntity(e, player);
               }

               GrabStatusPayload.syncTo(player, false, -1);
               play(level, player, (SoundEvent)ModSounds.GRAB_STOP.get(), 0.35F);
            } else {
               int strength = PortalGunData.grabStrength(portalGunStack(player));
               Entity target = findGrabTarget(player, level, strength);
               if (target == null) {
                  target = tryGrabBlock(player, level, strength);
               }

               if (target == null) {
                  play(level, player, (SoundEvent)ModSounds.GRAB_FAIL.get(), 0.3F);
               } else if (target instanceof Player) {
                  play(level, player, (SoundEvent)ModSounds.GRAB_FAIL.get(), 0.3F);
               } else {
                  beginHold(player, target);
                  play(level, player, (SoundEvent)ModSounds.GRAB_START.get(), 0.35F);
               }
            }
         }
      }
   }

   public static void release(ServerPlayer player) {
      if (player.level() instanceof ServerLevel level) {
         Integer id = HELD.remove(player.getUUID());
         if (id != null) {
            HELD_BY_ENTITY.remove(id);
            Entity e = level.getEntity(id);
            if (e != null) {
               releaseEntity(e, player);
            }

            GrabStatusPayload.syncTo(player, false, -1);
         }
      }
   }

   private static void beginHold(ServerPlayer player, Entity target) {
      HELD.put(player.getUUID(), target.getId());
      HELD_BY_ENTITY.put(target.getId(), player.getUUID());
      target.setNoGravity(true);
      target.fallDistance = 0.0F;
      target.setDeltaMovement(Vec3.ZERO);
      prepareHeldEntity(target);
      pinToHoldPoint(player, target);
      GrabStatusPayload.syncTo(player, true, target.getId());
   }

   private static void releaseEntity(Entity e, ServerPlayer owner) {
      Vec3 motion = throwMotion(owner);
      e.setDeltaMovement(motion);
      if (e instanceof FallingBlockEntity falling) {
         falling.setOnGround(false);
         falling.time = Math.max(falling.time, 1);
         falling.dropItem = true;
         falling.setDeltaMovement(motion);
      }

      e.setNoGravity(false);
      e.fallDistance = 0.0F;
      e.hasImpulse = true;
      e.hurtMarked = true;
   }

   private static Vec3 throwMotion(ServerPlayer owner) {
      double speed = throwSpeedFor(owner);
      return owner.getLookAngle().scale(speed).add(0.0, 0.15, 0.0);
   }

   private static double throwSpeedFor(ServerPlayer owner) {
      ItemStack gun = portalGunStack(owner);
      if (gun.isEmpty()) {
         return 1.0;
      } else {
         int strength = PortalGunData.grabStrength(gun);
         return 0.55 + 0.25 * (double)strength;
      }
   }

   private static ItemStack portalGunStack(Player player) {
      ItemStack main = player.getMainHandItem();
      if (main.getItem() instanceof PortalGunItem) {
         return main;
      } else {
         ItemStack off = player.getOffhandItem();
         return off.getItem() instanceof PortalGunItem ? off : ItemStack.EMPTY;
      }
   }

   private static void prepareHeldEntity(Entity held) {
      if (held instanceof FallingBlockEntity falling) {
         falling.time = 1;
         falling.setOnGround(false);
         falling.dropItem = true;

         try {
            falling.setHurtsEntities(0.0F, 0);
         } catch (Throwable var3) {
         }
      }

      if (held instanceof ItemEntity item) {
         item.setPickUpDelay(60);
      }
   }

   @SubscribeEvent(
      priority = EventPriority.HIGHEST
   )
   public static void onEntityTickPre(Pre event) {
      Entity held = event.getEntity();
      if (!held.level().isClientSide() && HELD_BY_ENTITY.containsKey(held.getId())) {
         held.setNoGravity(true);
         held.setDeltaMovement(Vec3.ZERO);
         held.fallDistance = 0.0F;
         prepareHeldEntity(held);
      }
   }

   @SubscribeEvent(
      priority = EventPriority.LOWEST
   )
   public static void onEntityTickPost(Post event) {
      Entity held = event.getEntity();
      if (!held.level().isClientSide()) {
         UUID ownerId = HELD_BY_ENTITY.get(held.getId());
         if (ownerId != null) {
            if (held.level() instanceof ServerLevel level) {
               ServerPlayer player = level.getServer().getPlayerList().getPlayer(ownerId);
               if (player != null && holdingPortalGun(player)) {
                  Integer expect = HELD.get(ownerId);
                  if (expect == null || expect != held.getId()) {
                     HELD_BY_ENTITY.remove(held.getId());
                  } else if (!held.isRemoved() && held.isAlive()) {
                     prepareHeldEntity(held);
                     pinToHoldPoint(player, held);
                     if ((double)held.distanceTo(player) > 7.5) {
                        release(player);
                     }
                  } else {
                     HELD.remove(ownerId);
                     HELD_BY_ENTITY.remove(held.getId());
                     GrabStatusPayload.syncTo(player, false, -1);
                  }
               } else {
                  HELD.remove(ownerId);
                  HELD_BY_ENTITY.remove(held.getId());
                  if (player != null) {
                     releaseEntity(held, player);
                     GrabStatusPayload.syncTo(player, false, -1);
                  } else {
                     held.setNoGravity(false);
                     held.fallDistance = 0.0F;
                     held.hasImpulse = true;
                     held.hurtMarked = true;
                  }
               }
            }
         }
      }
   }

   @SubscribeEvent
   public static void onPlayerTick(net.neoforged.neoforge.event.tick.PlayerTickEvent.Post event) {
      if (event.getEntity() instanceof ServerPlayer player) {
         Integer id = HELD.get(player.getUUID());
         if (id != null) {
            if (!holdingPortalGun(player)) {
               release(player);
            }
         }
      }
   }

   @SubscribeEvent
   public static void onLogout(PlayerLoggedOutEvent event) {
      if (event.getEntity() instanceof ServerPlayer sp) {
         release(sp);
      }
   }

   private static void pinToHoldPoint(ServerPlayer player, Entity held) {
      Vec3 dest = holdPoint(player, held);
      held.setPos(dest.x, dest.y, dest.z);
      held.setDeltaMovement(Vec3.ZERO);
      held.setNoGravity(true);
      held.fallDistance = 0.0F;
      held.hasImpulse = true;
   }

   private static Vec3 holdPoint(ServerPlayer player, Entity held) {
      Vec3 look = player.getLookAngle();
      double eyeY = player.getY() + (double)player.getEyeHeight();
      Vec3 eye = new Vec3(player.getX(), eyeY, player.getZ());
      Vec3 dest = eye.add(look.scale(2.0));
      double yOff = (double)held.getBbHeight() * 0.5;
      return new Vec3(dest.x, dest.y - yOff, dest.z);
   }

   private static void play(ServerLevel level, Player player, SoundEvent sound, float vol) {
      level.playSound(null, player.getX(), player.getY(), player.getZ(), sound, SoundSource.PLAYERS, vol, 1.0F);
   }

   private static boolean holdingPortalGun(Player player) {
      return player.getMainHandItem().getItem() instanceof PortalGunItem || player.getOffhandItem().getItem() instanceof PortalGunItem;
   }

   private static Entity findGrabTarget(ServerPlayer player, ServerLevel level, int strength) {
      Vec3 eye = player.getEyePosition();
      Vec3 look = player.getLookAngle();
      AABB box = player.getBoundingBox().expandTowards(look.scale(4.5)).inflate(0.6);
      Entity best = null;
      double bestDot = 0.75;

      for (Entity e : level.getEntities(player, box, ex -> canGrab(ex, strength))) {
         Vec3 to = e.getBoundingBox().getCenter().subtract(eye);
         double dist = to.length();
         if (!(dist < 0.4) && !(dist > 4.5)) {
            double dot = to.normalize().dot(look);
            if (dot > bestDot) {
               bestDot = dot;
               best = e;
            }
         }
      }

      if (best == null) {
         double bestD = 20.25;

         for (Entity ex : level.getEntities(player, box, exx -> canGrab(exx, strength))) {
            double d = ex.distanceToSqr(player);
            if (d < bestD) {
               bestD = d;
               best = ex;
            }
         }
      }

      return best;
   }

   private static Entity tryGrabBlock(ServerPlayer player, ServerLevel level, int strength) {
      if (strength < 2) {
         return null;
      } else {
         Vec3 eye = player.getEyePosition();
         Vec3 end = eye.add(player.getLookAngle().scale(4.5));
         BlockHitResult hit = level.clip(new ClipContext(eye, end, Block.OUTLINE, Fluid.NONE, player));
         if (hit.getType() != Type.BLOCK) {
            return null;
         } else {
            BlockPos pos = hit.getBlockPos();
            BlockState state = level.getBlockState(pos);
            if (!canGrabBlock(level, pos, state, strength)) {
               return null;
            } else {
               FallingBlockEntity falling = FallingBlockEntity.fall(level, pos, state);
               falling.setNoGravity(true);
               falling.time = 1;
               falling.setOnGround(false);
               falling.dropItem = true;
               falling.setDeltaMovement(Vec3.ZERO);

               try {
                  falling.setHurtsEntities(0.0F, 0);
               } catch (Throwable var10) {
               }

               return falling;
            }
         }
      }
   }

   private static boolean canGrabBlock(ServerLevel level, BlockPos pos, BlockState state, int strength) {
      if (state.isAir()) {
         return false;
      } else if (state.getBlock() instanceof PortalBlock || state.is((net.minecraft.world.level.block.Block)ModBlocks.PORTAL.get())) {
         return false;
      } else if (!state.is(Blocks.NETHER_PORTAL)
         && !state.is(Blocks.END_PORTAL)
         && !state.is(Blocks.END_GATEWAY)
         && !state.is(Blocks.BEDROCK)
         && !state.is(Blocks.BARRIER)
         && !state.is(Blocks.COMMAND_BLOCK)
         && !state.is(Blocks.CHAIN_COMMAND_BLOCK)
         && !state.is(Blocks.REPEATING_COMMAND_BLOCK)
         && !state.is(Blocks.STRUCTURE_BLOCK)
         && !state.is(Blocks.JIGSAW)
         && !state.is(Blocks.MOVING_PISTON)) {
         float hardness = state.getDestroySpeed(level, pos);
         if (hardness < 0.0F) {
            return false;
         } else {
            float maxHard = switch (Math.max(1, Math.min(5, strength))) {
               case 1 -> -1.0F;
               case 2 -> 1.5F;
               case 3 -> 3.0F;
               case 4 -> 5.0F;
               default -> 50.0F;
            };
            if (hardness > maxHard) {
               return false;
            } else if (state.canBeReplaced()) {
               return false;
            } else if (!state.isFaceSturdy(level, pos, Direction.UP)) {
               return false;
            } else {
               BlockEntity be = level.getBlockEntity(pos);
               return !(be instanceof BaseContainerBlockEntity);
            }
         }
      } else {
         return false;
      }
   }

   private static boolean canGrab(Entity e, int strength) {
      if (e == null || e.isRemoved() || !e.isAlive()) {
         return false;
      } else if (e instanceof Player) {
         return false;
      } else if (e instanceof ItemEntity) {
         return true;
      } else if (e instanceof FallingBlockEntity) {
         return strength >= 2;
      } else if (!(e instanceof LivingEntity)) {
         return false;
      } else {
         double vol = (double)(e.getBbWidth() * e.getBbWidth() * e.getBbHeight());

         double maxVol = switch (Math.max(1, Math.min(5, strength))) {
            case 1 -> 0.6;
            case 2 -> 2.5;
            case 3 -> 6.0;
            case 4 -> 16.0;
            default -> 64.0;
         };
         return vol <= maxVol;
      }
   }
}
