package me.scwunge.mods.portalgun.portal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import me.scwunge.mods.portalgun.PortalGunMod;
import me.scwunge.mods.portalgun.entity.PortalProjectile;
import me.scwunge.mods.portalgun.init.ModSounds;
import me.scwunge.mods.portalgun.portal.worldportal.PortalQuaternionFormula;
import me.scwunge.mods.portalgun.portal.worldportal.PortalTransform;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent.Post;
import net.neoforged.neoforge.event.tick.EntityTickEvent.Pre;

@EventBusSubscriber(
   modid = "orespawn"
)
public final class PortalTeleport {
   public static final int COOLDOWN_TICKS = 5;
   private static final String COOLDOWN_KEY = "orespawn_portal_cd";
   private static final double SCAN_DIST = 2.25;
   private static final double INSIDE_DEPTH = 1.4;
   private static final double TRANSIT_INSET = 0.12;
   private static final double HOLE_INSET = 0.06;
   private static final double MIN_PLANE_STEP = 0.004;
   private static final double PLANE_ARRIVE = 0.22;
   private static final double PLANE_START = 0.01;
   private static final Map<UUID, Boolean> NO_PHYSICS_RESTORE = new HashMap<>();
   private static final Map<UUID, Vec3> PREV_FEET = new HashMap<>();
   private static final List<PortalTeleport.PendingTransit> PENDING = new ArrayList<>();
   private static final Map<UUID, Boolean> PENDING_IDS = new HashMap<>();

   private PortalTeleport() {
   }

   @SubscribeEvent
   public static void onEntityTickPre(Pre event) {
      Entity entity = event.getEntity();
      if (!entity.level().isClientSide() && !(entity instanceof PortalProjectile)) {
         if (entity.level() instanceof ServerLevel level) {
            if (!(entity instanceof Projectile) || entity instanceof FallingBlockEntity) {
               if (isTransitCandidate(entity)) {
                  PortalSavedData.PortalEntry entry = findPortalInFront(entity, level);
                  if (entry == null) {
                     restoreNoPhysics(entity);
                  } else {
                     PortalSavedData.PortalEntry pair = PortalSavedData.get(level).getEntry(entry.info.uuid, entry.info.channelName, !entry.info.isTypeA);
                     if (pair == null) {
                        restoreNoPhysics(entity);
                     } else {
                        if (needsDoorHole(entity, entry)) {
                           Vec3 sample = samplePoint(entity, entry.face);
                           double dist = signedDistanceToPlane(sample, entry);
                           boolean nearCross = dist < 0.55 && dist > -1.4;
                           if (nearCross) {
                              if (!entity.noPhysics) {
                                 NO_PHYSICS_RESTORE.put(entity.getUUID(), true);
                                 entity.noPhysics = true;
                              }

                              Vec3 m = entity.getDeltaMovement();
                              if (entry.face == Direction.UP) {
                                 double vy = Math.min(m.y - 0.1, -0.12);
                                 entity.setDeltaMovement(m.x, vy, m.z);
                              } else if (entry.face == Direction.DOWN) {
                                 entity.setDeltaMovement(m.x, m.y + 0.04, m.z);
                              } else if (m.y > -0.4 && !entity.onGround()) {
                                 entity.setDeltaMovement(m.x, m.y - 0.04, m.z);
                              }
                           } else {
                              restoreNoPhysics(entity);
                           }
                        } else {
                           restoreNoPhysics(entity);
                        }
                     }
                  }
               }
            }
         }
      }
   }

   private static boolean needsDoorHole(Entity entity, PortalSavedData.PortalEntry entry) {
      if (!inApertureCorridor(entity, entry)) {
         return false;
      } else if (!isInPortalCore(entity, entry, 0.06)) {
         return false;
      } else {
         Vec3 sample = samplePoint(entity, entry.face);
         double dist = signedDistanceToPlane(sample, entry);
         return entry.face.getAxis().isHorizontal() ? dist < 0.85 && dist > -1.4 : dist < 0.55 && dist > -1.4;
      }
   }

   public static boolean isInPortalCore(Entity entity, PortalSavedData.PortalEntry entry) {
      return isInPortalCore(entity, entry, 0.12);
   }

   public static boolean isInPortalCore(Entity entity, PortalSavedData.PortalEntry entry, double inset) {
      Vec3 body = entity.position().add(0.0, (double)entity.getBbHeight() * 0.5, 0.0);
      Vec3 sample = samplePoint(entity, entry.face);
      return withinApertureDisk(sample, entry, -inset) || withinApertureDisk(body, entry, -inset);
   }

   @SubscribeEvent
   public static void onEntityTickPost(Post event) {
      Entity entity = event.getEntity();
      if (!entity.level().isClientSide() && !(entity instanceof PortalProjectile) && !entity.isRemoved()) {
         if (isTransitCandidate(entity)) {
            if (entity.level() instanceof ServerLevel level) {
               if (PENDING_IDS.containsKey(entity.getUUID())) {
                  rememberFeet(entity);
               } else {
                  int cd = entity.getPersistentData().getInt("orespawn_portal_cd");
                  if (cd > 0) {
                     entity.getPersistentData().putInt("orespawn_portal_cd", cd - 1);
                     rememberFeet(entity);
                  } else {
                     PortalSavedData data = PortalSavedData.get(level);
                     PortalSavedData.PortalEntry from = findPortalCrossing(entity, level, data);
                     rememberFeet(entity);
                     if (from != null) {
                        PortalSavedData.PortalEntry to = data.getEntry(from.info.uuid, from.info.channelName, !from.info.isTypeA);
                        if (to != null && to.info.getPos().getY() >= level.getMinBuildHeight()) {
                           PENDING_IDS.put(entity.getUUID(), Boolean.TRUE);
                           PENDING.add(new PortalTeleport.PendingTransit(entity.getUUID(), level.dimension(), from, to));
                        }
                     }
                  }
               }
            }
         }
      }
   }

   @SubscribeEvent
   public static void onServerTickEnd(net.neoforged.neoforge.event.tick.ServerTickEvent.Post event) {
      if (!PENDING.isEmpty()) {
         List<PortalTeleport.PendingTransit> batch = new ArrayList<>(PENDING);
         PENDING.clear();
         PENDING_IDS.clear();

         for (PortalTeleport.PendingTransit p : batch) {
            try {
               ServerLevel level = event.getServer().getLevel(p.dimension);
               if (level != null) {
                  Entity entity = findByUuid(level, p.entityId);
                  if (entity != null && !entity.isRemoved()) {
                     PortalSavedData data = PortalSavedData.get(level);
                     PortalSavedData.PortalEntry from = data.getEntry(p.from.info.uuid, p.from.info.channelName, p.from.info.isTypeA);
                     PortalSavedData.PortalEntry to = data.getEntry(p.to.info.uuid, p.to.info.channelName, p.to.info.isTypeA);
                     if (from != null && to != null) {
                        walkThrough(entity, level, from, to);
                     }
                  }
               }
            } catch (Throwable var9) {
               PortalGunMod.LOGGER.error("Portal transit failed for {}: {}", p.entityId, var9.toString());
            }
         }
      }
   }

   private static Entity findByUuid(ServerLevel level, UUID id) {
      ServerPlayer player = level.getServer().getPlayerList().getPlayer(id);
      if (player != null && player.level() == level) {
         return player;
      } else {
         for (Entity e : level.getAllEntities()) {
            if (id.equals(e.getUUID())) {
               return e;
            }
         }

         return null;
      }
   }

   @SubscribeEvent
   public static void onDamage(LivingIncomingDamageEvent event) {
      if (event.getSource().is(DamageTypes.IN_WALL) || event.getSource().is(DamageTypes.CRAMMING)) {
         Entity entity = event.getEntity();
         if (!NO_PHYSICS_RESTORE.containsKey(entity.getUUID()) && !entity.noPhysics) {
            if (entity.level() instanceof ServerLevel level) {
               PortalSavedData.PortalEntry front = findPortalInFront(entity, level);
               if (front != null && inApertureCorridor(entity, front)) {
                  event.setCanceled(true);
               }
            }
         } else {
            event.setCanceled(true);
         }
      }
   }

   private static void restoreNoPhysics(Entity entity) {
      if (NO_PHYSICS_RESTORE.remove(entity.getUUID()) != null) {
         entity.noPhysics = false;
      }
   }

   private static boolean isTransitCandidate(Entity entity) {
      return entity instanceof LivingEntity living
         ? living.isAlive()
         : entity instanceof ItemEntity || entity instanceof Projectile || entity instanceof ExperienceOrb || entity instanceof FallingBlockEntity;
   }

   private static Vec3 samplePoint(Entity entity, Direction face) {
      return sampleFromFeet(entity.position(), entity, face);
   }

   private static Vec3 sampleFromFeet(Vec3 feet, Entity entity, Direction face) {
      if (face == Direction.UP) {
         return feet.add(0.0, 0.05, 0.0);
      } else {
         return face == Direction.DOWN
            ? feet.add(0.0, Math.max(0.1, (double)entity.getBbHeight() - 0.05), 0.0)
            : feet.add(0.0, (double)entity.getEyeHeight(), 0.0);
      }
   }

   private static void rememberFeet(Entity entity) {
      if (PREV_FEET.size() > 512) {
         PREV_FEET.clear();
      }

      PREV_FEET.put(entity.getUUID(), entity.position());
   }

   private static PortalSavedData.PortalEntry findPortalCrossing(Entity entity, ServerLevel level, PortalSavedData data) {
      Vec3 motion = entity.getDeltaMovement();
      Vec3 feetNow = entity.position();
      Vec3 feetPrev = PREV_FEET.getOrDefault(entity.getUUID(), feetNow.subtract(motion.lengthSqr() > 1.0E-8 ? motion : new Vec3(0.0, 0.05, 0.0)));

      for (PortalSavedData.PortalEntry entry : data.getPortals().values()) {
         if (sameDimensionRough(entry, level)) {
            Vec3 sampleNow = sampleFromFeet(feetNow, entity, entry.face);
            Vec3 samplePrev = sampleFromFeet(feetPrev, entity, entry.face);
            Vec3 body = feetNow.add(0.0, (double)entity.getBbHeight() * 0.5, 0.0);
            boolean coreNow = withinApertureDisk(sampleNow, entry, -0.12) || withinApertureDisk(body, entry, -0.12);
            boolean corePrev = withinApertureDisk(samplePrev, entry, -0.12);
            if (coreNow || corePrev) {
               double dPrev = signedDistanceToPlane(samplePrev, entry);
               double dNow = signedDistanceToPlane(sampleNow, entry);
               double closed = dPrev - dNow;
               boolean projectile = entity instanceof Projectile && !(entity instanceof FallingBlockEntity);
               boolean inCore = coreNow || corePrev;
               if (inCore && dPrev > 0.01 && dNow <= 0.22 && closed >= 0.004) {
                  if (!projectile) {
                     return entry;
                  }

                  double projInset = 0.05;
                  if (withinApertureDisk(sampleNow, entry, -projInset) || withinApertureDisk(samplePrev, entry, -projInset)) {
                     return entry;
                  }
               }

               if (!projectile && coreNow && dPrev > 0.01 && dNow <= 0.05) {
                  Vec3 n = faceNormal(entry.face);
                  double intoDoor = -motion.dot(n);
                  if (intoDoor > 0.01 || closed >= 0.004) {
                     return entry;
                  }
               }

               if (projectile
                  || entry.face != Direction.UP
                  || !inCore
                  || !(dNow <= 0.22)
                  || !(dPrev > -0.35)
                  || !(motion.y <= 0.08)
                  || !(closed >= 0.004) && !(dNow <= 0.02) && !(motion.y < -0.05)) {
                  if (projectile || entry.face != Direction.DOWN || !inCore || !(dNow <= 0.22) || !(dPrev > -0.35) || !(closed >= 0.004) && !(motion.y > 0.05)) {
                     continue;
                  }

                  return entry;
               }

               return entry;
            }
         }
      }

      return null;
   }

   private static PortalSavedData.PortalEntry findPortalInFront(Entity entity, ServerLevel level) {
      PortalSavedData data = PortalSavedData.get(level);
      AABB box = entity.getBoundingBox().inflate(0.4);
      PortalSavedData.PortalEntry best = null;
      double bestDist = Double.MAX_VALUE;
      Vec3 body = entity.position().add(0.0, (double)entity.getBbHeight() * 0.5, 0.0);

      for (PortalSavedData.PortalEntry entry : data.getPortals().values()) {
         if (scanBox(entry).intersects(box) || insidesBox(entry).intersects(box)) {
            Vec3 probe = samplePoint(entity, entry.face);
            if (withinApertureDisk(probe, entry, 0.55) || withinApertureDisk(body, entry, 0.55)) {
               double d = Math.abs(signedDistanceToPlane(probe, entry));
               if (d < bestDist) {
                  bestDist = d;
                  best = entry;
               }
            }
         }
      }

      return best;
   }

   private static boolean sameDimensionRough(PortalSavedData.PortalEntry entry, ServerLevel level) {
      return entry.info.getPos().getY() >= level.getMinBuildHeight();
   }

   private static Vec3 faceNormal(Direction face) {
      return new Vec3((double)face.getStepX(), (double)face.getStepY(), (double)face.getStepZ());
   }

   private static double signedDistanceToPlane(Vec3 point, PortalSavedData.PortalEntry entry) {
      Direction face = entry.face;
      if (face == Direction.UP) {
         double surfaceY = (double)entry.info.getPos().getY() + 0.01;
         return point.y - surfaceY;
      } else if (face == Direction.DOWN) {
         double surfaceY = (double)entry.info.getPos().getY() + 1.0 - 0.01;
         return surfaceY - point.y;
      } else {
         Vec3 c = PortalTransform.portalCenter(entry);
         return point.subtract(c).dot(faceNormal(face));
      }
   }

   private static boolean withinApertureDisk(Vec3 point, PortalSavedData.PortalEntry entry, double pad) {
      Vec3 c = PortalTransform.portalCenter(entry);
      Direction face = entry.face;
      Direction up = PortalTransform.upOf(entry);
      Direction wDir = PortalGunHelper.widthDirection(face, up);
      Direction hDir = PortalGunHelper.heightDirection(face, up);
      Vec3 d = point.subtract(c);
      Vec3 n = faceNormal(face);
      d = d.subtract(n.scale(d.dot(n)));
      double alongW = d.dot(new Vec3((double)wDir.getStepX(), (double)wDir.getStepY(), (double)wDir.getStepZ()));
      double alongH = d.dot(new Vec3((double)hDir.getStepX(), (double)hDir.getStepY(), (double)hDir.getStepZ()));
      double halfW = (double)entry.width * 0.5 + pad;
      double halfH = (double)entry.height * 0.5 + pad;
      return Math.abs(alongW) <= halfW && Math.abs(alongH) <= halfH;
   }

   private static boolean inApertureCorridor(Entity entity, PortalSavedData.PortalEntry entry) {
      AABB box = entity.getBoundingBox();
      if (!scanBox(entry).intersects(box) && !insidesBox(entry).intersects(box)) {
         return false;
      } else {
         Vec3 probe = samplePoint(entity, entry.face);
         Vec3 body = entity.position().add(0.0, (double)entity.getBbHeight() * 0.5, 0.0);
         return withinApertureDisk(probe, entry, 0.15) || withinApertureDisk(body, entry, 0.15);
      }
   }

   public static double apertureHalfW(PortalSavedData.PortalEntry entry) {
      return (double)entry.width * 0.5;
   }

   public static double apertureHalfH(PortalSavedData.PortalEntry entry) {
      return (double)entry.height * 0.5;
   }

   private static AABB scanBox(PortalSavedData.PortalEntry entry) {
      return planeSlab(entry, 0.0, 2.25);
   }

   private static AABB insidesBox(PortalSavedData.PortalEntry entry) {
      return planeSlab(entry, -1.4, 0.15);
   }

   private static AABB planeSlab(PortalSavedData.PortalEntry entry, double back, double front) {
      Vec3 c = PortalTransform.portalCenter(entry);
      Direction face = entry.face;
      Direction up = PortalTransform.upOf(entry);
      Direction wDir = PortalGunHelper.widthDirection(face, up);
      Direction hDir = PortalGunHelper.heightDirection(face, up);
      double halfW = (double)entry.width * 0.5 + 0.15;
      double halfH = (double)entry.height * 0.5 + 0.15;
      Vec3 w = new Vec3((double)wDir.getStepX(), (double)wDir.getStepY(), (double)wDir.getStepZ()).scale(halfW);
      Vec3 h = new Vec3((double)hDir.getStepX(), (double)hDir.getStepY(), (double)hDir.getStepZ()).scale(halfH);
      Vec3 n = faceNormal(face);
      Vec3 p0 = c.add(n.scale(back)).subtract(w).subtract(h);
      Vec3 p1 = c.add(n.scale(front)).add(w).add(h);
      return new AABB(p0, p1);
   }

   private static void walkThrough(Entity entity, ServerLevel level, PortalSavedData.PortalEntry from, PortalSavedData.PortalEntry to) {
      PortalQuaternionFormula formula = PortalTransform.formula(from, to);
      Vec3 fromCenter = PortalTransform.portalCenter(from);
      Vec3 toCenter = PortalTransform.portalCenter(to);
      double eyeH = Math.max(0.0, (double)entity.getEyeHeight());
      Vec3 sample = samplePoint(entity, from.face);
      Vec3 motion = entity.getDeltaMovement();
      float[] rel = new float[]{(float)(sample.x - fromCenter.x), (float)(sample.y - fromCenter.y), (float)(sample.z - fromCenter.z)};
      float[] appliedOffset = formula.applyPositionalRotation(rel);
      float[] appliedMotion = formula.applyPositionalRotation(new float[]{(float)motion.x, (float)motion.y, (float)motion.z});
      float[] lookDelta = formula.applyRotationalRotation(new float[]{entity.getYRot(), entity.getXRot(), 0.0F});
      if (finite3(appliedOffset) && finite3(appliedMotion)) {
         Vec3 exitN = faceNormal(to.face);
         double alongOut = new Vec3((double)appliedOffset[0], (double)appliedOffset[1], (double)appliedOffset[2]).dot(exitN);
         double outStep = Math.max(0.35, -alongOut + 0.25);
         if (to.face.getAxis().isVertical()) {
            outStep = Math.max(outStep, 0.55);
         }

         if (entity instanceof Projectile) {
            outStep = Math.max(outStep, 0.75);
         }

         double eyeX = toCenter.x + (double)appliedOffset[0] + exitN.x * outStep;
         double eyeY = toCenter.y + (double)appliedOffset[1] + exitN.y * outStep;
         double eyeZ = toCenter.z + (double)appliedOffset[2] + exitN.z * outStep;
         double feetY = eyeY - eyeH;
         if (to.face == Direction.UP) {
            double standY = (double)to.info.getPos().getY() + 0.01;
            feetY = Math.max(feetY, standY);
            Vec3 probeFeet = new Vec3(eyeX, feetY + 0.05, eyeZ);
            if (signedDistanceToPlane(probeFeet, to) < 0.15) {
               feetY = toCenter.y + 0.05;
            }

            eyeY = feetY + eyeH;
         }

         if (to.face == Direction.DOWN) {
            double maxTop = (double)to.info.getPos().getY() + 1.0 - 0.05;
            double headY = feetY + (double)entity.getBbHeight();
            if (headY > maxTop) {
               feetY = maxTop - (double)entity.getBbHeight();
            }

            Vec3 probeHead = new Vec3(eyeX, feetY + (double)entity.getBbHeight() - 0.05, eyeZ);
            if (signedDistanceToPlane(probeHead, to) < 0.1) {
               feetY = toCenter.y - (double)entity.getBbHeight() - 0.15;
            }

            eyeY = feetY + eyeH;
         }

         if (entity instanceof Projectile) {
            feetY = eyeY;
         }

         if (Double.isFinite(eyeX) && Double.isFinite(feetY) && Double.isFinite(eyeZ)) {
            float yaw = Mth.wrapDegrees(entity.getYRot() + (Float.isFinite(lookDelta[0]) ? lookDelta[0] : 0.0F));
            float pitch = Mth.clamp(entity.getXRot() + (Float.isFinite(lookDelta[1]) ? lookDelta[1] : 0.0F), -90.0F, 90.0F);
            Vec3 newMotion = softCap(new Vec3((double)appliedMotion[0], (double)appliedMotion[1], (double)appliedMotion[2]));
            double exitBoost = entity instanceof Projectile ? 0.25 : 0.05;
            newMotion = newMotion.add(exitN.scale(exitBoost));
            if (!Double.isFinite(newMotion.x) || !Double.isFinite(newMotion.y) || !Double.isFinite(newMotion.z)) {
               newMotion = exitN.scale(0.25);
            }

            level.playSound(
               null,
               entity.getX(),
               entity.getY(),
               entity.getZ(),
               (SoundEvent)ModSounds.PORTAL_ENTER.get(),
               SoundSource.PLAYERS,
               0.15F,
               1.0F + (level.random.nextFloat() - level.random.nextFloat()) * 0.05F
            );
            entity.noPhysics = false;
            NO_PHYSICS_RESTORE.remove(entity.getUUID());
            if (entity instanceof ServerPlayer player) {
               player.connection.teleport(eyeX, feetY, eyeZ, yaw, pitch);
            } else if (entity instanceof Projectile proj) {
               proj.setPos(eyeX, feetY, eyeZ);
               proj.setDeltaMovement(newMotion);
               proj.hasImpulse = true;
               proj.setYRot(yaw);
               proj.setXRot(pitch);
               if (proj instanceof AbstractArrow arrow) {
                  arrow.setNoGravity(false);
                  double len = newMotion.length();
                  if (len > 1.0E-4) {
                     float yRot = (float)(Mth.atan2(newMotion.x, newMotion.z) * (180.0 / Math.PI));
                     float xRot = (float)(Mth.atan2(newMotion.y, Math.sqrt(newMotion.x * newMotion.x + newMotion.z * newMotion.z)) * (180.0 / Math.PI));
                     arrow.setYRot(yRot);
                     arrow.setXRot(xRot);
                     arrow.yRotO = yRot;
                     arrow.xRotO = xRot;
                  }
               }
            } else {
               entity.teleportTo(eyeX, feetY, eyeZ);
               entity.setYRot(yaw);
               entity.setXRot(pitch);
               if (entity instanceof LivingEntity living) {
                  living.setYHeadRot(yaw);
                  living.yBodyRot = yaw;
               }
            }

            if (!(entity instanceof Projectile)) {
               entity.setDeltaMovement(newMotion);
               entity.hasImpulse = true;
            }

            entity.fallDistance = 0.0F;
            PREV_FEET.put(entity.getUUID(), new Vec3(eyeX, feetY, eyeZ).add(exitN.scale(0.4)));
            entity.getPersistentData().putInt("orespawn_portal_cd", 5);
            level.playSound(
               null,
               eyeX,
               feetY,
               eyeZ,
               (SoundEvent)ModSounds.PORTAL_EXIT.get(),
               SoundSource.PLAYERS,
               0.15F,
               1.0F + (level.random.nextFloat() - level.random.nextFloat()) * 0.05F
            );
         } else {
            PortalGunMod.LOGGER.warn("Portal transit aborted: non-finite exit pos");
         }
      } else {
         PortalGunMod.LOGGER.warn("Portal transit aborted: non-finite transform for {}", entity.getUUID());
      }
   }

   private static boolean finite3(float[] v) {
      return v != null && v.length >= 3 && Float.isFinite(v[0]) && Float.isFinite(v[1]) && Float.isFinite(v[2]);
   }

   private static Vec3 softCap(Vec3 v) {
      double x = v.x;
      double y = v.y;
      double z = v.z;
      if (Math.abs(x) > 0.99) {
         x /= Math.abs(x) + 0.001;
      }

      if (Math.abs(y) > 0.99) {
         y /= Math.abs(y) + 0.001;
      }

      if (Math.abs(z) > 0.99) {
         z /= Math.abs(z) + 0.001;
      }

      return new Vec3(x, y, z);
   }

   private static record PendingTransit(UUID entityId, ResourceKey<Level> dimension, PortalSavedData.PortalEntry from, PortalSavedData.PortalEntry to) {
   }
}
