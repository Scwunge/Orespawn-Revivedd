package me.scwunge.mods.portalgun.client.render;

import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.pipeline.TextureTarget;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import me.scwunge.mods.portalgun.PortalGunMod;
import me.scwunge.mods.portalgun.block.entity.PortalMasterBlockEntity;
import me.scwunge.mods.portalgun.config.PortalGunConfig;
import me.scwunge.mods.portalgun.entity.PortalCameraEntity;
import me.scwunge.mods.portalgun.init.ModEntities;
import me.scwunge.mods.portalgun.portal.PortalGunHelper;
import me.scwunge.mods.portalgun.portal.worldportal.PortalTransform;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.SectionPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent.LoggingOut;
import net.neoforged.neoforge.client.event.RenderFrameEvent.Pre;
import org.jetbrains.annotations.Nullable;

@EventBusSubscriber(
   modid = "orespawn",
   value = {Dist.CLIENT}
)
public final class PortalSeeThrough {
   /** Only refresh the single nearest portal (pair still shows via mate capture). */
   private static final int MAX_PRIMARY = 1;
   private static final int MAX_NESTED_PER_LEVEL = 0;
   /** ~12 blocks — only capture when you are right next to a portal. */
   private static final double MAX_DIST_SQ = 144.0;
   private static final double NESTED_SCAN = 12.0;
   /** Very small FBO — oval is small; OreSpawn entity counts make full res lethal. */
   private static final int PX_PER_BLOCK = 48;
   private static final int FBO_MAX = 192;
   private static final int FBO_MAX_NESTED = 128;
   /** At most one full world pass per refresh cycle. */
   private static final int MAX_RENDERS_PER_FRAME = 1;
   /** Re-capture every N frames; keep stale image between. */
   private static final int REFRESH_EVERY_FRAMES = 10;
   /** Temporary render-distance while capturing (restored after). */
   private static final int CAPTURE_RENDER_DISTANCE = 4;
   private static final Map<Long, PortalSeeThrough.CaptureRequest> REQUESTS = new HashMap<>();
   private static final Map<Long, TextureTarget> TARGETS = new HashMap<>();
   private static final Map<Long, Boolean> VALID = new HashMap<>();
   private static final Map<Long, Boolean> STALE_OK = new HashMap<>();
   private static final Set<Long> CAPTURED_FRAME = new HashSet<>();
   private static final Set<Long> CAPTURE_STACK = new HashSet<>();
   private static final Map<Long, PortalCameraEntity> CAMERAS = new HashMap<>();
   private static boolean capturing;
   private static int captureDepth;
   private static long capturingOrigin = Long.MIN_VALUE;
   private static int rendersThisFrame;
   private static int frameCounter;

   private PortalSeeThrough() {
   }

   public static boolean isCapturing() {
      return capturing || captureDepth > 0;
   }

   public static boolean allowPortalDisplay() {
      return true;
   }

   public static int maxRecursion() {
      try {
         return Mth.clamp((Integer)PortalGunConfig.PORTAL_RECURSION_DEPTH.get(), 0, 3);
      } catch (Throwable var1) {
         return 2;
      }
   }

   public static void requestCapture(PortalMasterBlockEntity be) {
      if (be != null && be.isOrigin() && be.hasPair()) {
         if (!isCapturing()) {
            if (seeThroughEnabled()) {
               Minecraft mc = Minecraft.getInstance();
               if (mc.player != null) {
                  if (!(mc.player.distanceToSqr(Vec3.atCenterOf(be.getBlockPos())) > MAX_DIST_SQ)) {
                     PortalSeeThrough.CaptureRequest req = fromBe(be);
                     if (req != null) {
                        REQUESTS.put(be.getBlockPos().asLong(), req);
                     }
                  }
               }
            }
         }
      }
   }

   @Nullable
   public static RenderTarget getTarget(BlockPos origin) {
      if (origin == null) {
         return null;
      } else {
         long key = origin.asLong();
         if (!Boolean.TRUE.equals(VALID.get(key)) && !Boolean.TRUE.equals(STALE_OK.get(key))) {
            return null;
         } else {
            return CAPTURE_STACK.contains(key) && !Boolean.TRUE.equals(STALE_OK.get(key)) ? null : (RenderTarget)TARGETS.get(key);
         }
      }
   }

   public static int textureId(BlockPos origin) {
      RenderTarget t = getTarget(origin);
      return t != null ? t.getColorTextureId() : 0;
   }

   public static boolean hasView(BlockPos origin) {
      return textureId(origin) != 0;
   }

   @SubscribeEvent
   public static void onRenderFramePre(Pre event) {
      if (!isCapturing()) {
         Minecraft mc = Minecraft.getInstance();
         if (mc.level == null || mc.player == null || mc.gameRenderer == null) {
            REQUESTS.clear();
         } else if (!seeThroughEnabled()) {
            REQUESTS.clear();
            VALID.clear();
         } else {
            frameCounter++;
            // Keep last good FBO most frames — full renderLevel is the lag source
            boolean doCapture = frameCounter % REFRESH_EVERY_FRAMES == 0;

            // Non-capture frames: no chunk scans, no world re-render
            STALE_OK.putAll(VALID);
            if (!doCapture) {
               REQUESTS.clear();
               return;
            }

            DeltaTracker delta = event.getPartialTick();
            float pt = partial(delta);
            Entity viewer = mc.player;
            if (mc.getCameraEntity() instanceof PortalCameraEntity) {
               mc.setCameraEntity(mc.player);
            }

            Vec3 viewerEye = viewer.getEyePosition(pt);
            float viewerYaw = viewYaw(viewer, pt);
            float viewerPitch = viewPitch(viewer, pt);
            // Prefer BER-requested portals only (no full chunk BE scan every refresh)
            Map<Long, PortalSeeThrough.CaptureRequest> frameReqs = new HashMap<>(REQUESTS);
            REQUESTS.clear();
            // Light fallback scan only if nothing was requested this frame
            if (frameReqs.isEmpty()) {
               scanNearbyOrigins(mc.level, viewerEye, frameReqs);
            }
            ensurePairMates(mc.level, frameReqs);
            List<PortalSeeThrough.CaptureRequest> primaries = new ArrayList<>(frameReqs.values());
            // Nearest portal first
            primaries.sort(Comparator.comparingDouble(r ->
                  viewerEye.distanceToSqr(Vec3.atCenterOf(r.entryOrigin))));
            if (primaries.size() > MAX_PRIMARY) {
               primaries = primaries.subList(0, MAX_PRIMARY);
            }

            CAPTURED_FRAME.clear();
            CAPTURE_STACK.clear();
            rendersThisFrame = 0;
            Map<Long, Boolean> newValid = new HashMap<>();
            int maxDepth = 0;

            for (PortalSeeThrough.CaptureRequest req : primaries) {
               if (rendersThisFrame >= MAX_RENDERS_PER_FRAME) {
                  break;
               }
               try {
                  if (captureRecursive(mc, req, delta, 0, maxDepth, viewerEye, viewerYaw, viewerPitch)) {
                     newValid.put(req.entryOrigin.asLong(), true);
                  }
               } catch (Throwable var18) {
                  PortalGunMod.LOGGER.warn("Portal see-through capture failed: {}", var18.toString());
               } finally {
                  capturing = false;
                  captureDepth = 0;
                  capturingOrigin = Long.MIN_VALUE;
                  CAPTURE_STACK.clear();
                  if (mc.getCameraEntity() instanceof PortalCameraEntity) {
                     mc.setCameraEntity(mc.player);
                  }
               }
            }

            // Keep previous VALID entries that we didn't refresh this cycle (still on-screen)
            for (PortalSeeThrough.CaptureRequest req : primaries) {
               long k = req.entryOrigin.asLong();
               if (!newValid.containsKey(k) && Boolean.TRUE.equals(STALE_OK.get(k))) {
                  newValid.put(k, true);
               }
            }
            VALID.clear();
            VALID.putAll(newValid);

            for (Long key : CAPTURED_FRAME) {
               STALE_OK.put(key, true);
               VALID.put(key, true);
            }

            if (TARGETS.size() > 8) {
               Iterator<Entry<Long, TextureTarget>> it = TARGETS.entrySet().iterator();

               while (it.hasNext()) {
                  Entry<Long, TextureTarget> e = it.next();
                  if (!VALID.containsKey(e.getKey()) && !STALE_OK.containsKey(e.getKey())) {
                     e.getValue().destroyBuffers();
                     it.remove();
                     CAMERAS.remove(e.getKey());
                  }
               }
            }
         }
      }
   }

   private static void scanNearbyOrigins(Level level, Vec3 eye, Map<Long, PortalSeeThrough.CaptureRequest> out) {
      BlockPos base = BlockPos.containing(eye);
      int r = 12;
      int minCx = SectionPos.blockToSectionCoord(base.getX() - r);
      int maxCx = SectionPos.blockToSectionCoord(base.getX() + r);
      int minCz = SectionPos.blockToSectionCoord(base.getZ() - r);
      int maxCz = SectionPos.blockToSectionCoord(base.getZ() + r);
      double maxSq = MAX_DIST_SQ;

      for (int cx = minCx; cx <= maxCx; cx++) {
         for (int cz = minCz; cz <= maxCz; cz++) {
            if (level.hasChunk(cx, cz)) {
               LevelChunk chunk = level.getChunk(cx, cz);

               for (BlockEntity be : chunk.getBlockEntities().values()) {
                  if (be instanceof PortalMasterBlockEntity) {
                     PortalMasterBlockEntity master = (PortalMasterBlockEntity)be;
                     if (master.isOrigin() && master.hasPair()) {
                        double dSelf = eye.distanceToSqr(Vec3.atCenterOf(master.getBlockPos()));
                        double dPair = eye.distanceToSqr(Vec3.atCenterOf(master.getPairPos()));
                        if (!(dSelf > maxSq) || !(dPair > maxSq)) {
                           PortalSeeThrough.CaptureRequest req = fromBe(master);
                           if (req != null) {
                              out.put(master.getBlockPos().asLong(), req);
                           }
                        }
                     }
                  }
               }
            }
         }
      }
   }

   private static void ensurePairMates(Level level, Map<Long, PortalSeeThrough.CaptureRequest> out) {
      for (PortalSeeThrough.CaptureRequest req : new ArrayList<>(out.values())) {
         long pairKey = req.pairOrigin.asLong();
         if (!out.containsKey(pairKey)) {
            BlockEntity mate = level.getBlockEntity(req.pairOrigin);
            if (mate instanceof PortalMasterBlockEntity) {
               PortalMasterBlockEntity master = (PortalMasterBlockEntity)mate;
               if (master.isOrigin() && master.hasPair()) {
                  PortalSeeThrough.CaptureRequest matex = fromBe(master);
                  if (matex != null) {
                     out.put(pairKey, matex);
                     continue;
                  }
               }
            }

            out.put(
               pairKey,
               new PortalSeeThrough.CaptureRequest(
                  req.pairOrigin, req.pairFacing, req.pairUp, req.pairW, req.pairH, req.entryOrigin, req.entryFacing, req.entryUp, req.entryW, req.entryH
               )
            );
         }
      }
   }

   private static boolean captureRecursive(
      Minecraft mc, PortalSeeThrough.CaptureRequest req, DeltaTracker delta, int depth, int maxDepth, Vec3 viewerEye, float viewerYaw, float viewerPitch
   ) {
      long key = req.entryOrigin.asLong();
      if (CAPTURE_STACK.contains(key) || CAPTURED_FRAME.contains(key)) {
         return CAPTURED_FRAME.contains(key);
      } else if (depth <= maxDepth && rendersThisFrame < MAX_RENDERS_PER_FRAME) {
         PortalSeeThrough.CamPose cam = computeExitCamera(req, viewerEye, viewerYaw, viewerPitch);
         if (cam == null) {
            return false;
         } else {
            if (depth < maxDepth && rendersThisFrame < MAX_RENDERS_PER_FRAME) {
               for (PortalSeeThrough.CaptureRequest child : findNearbyPortals(mc.level, cam.eye, key, MAX_NESTED_PER_LEVEL)) {
                  if (rendersThisFrame >= MAX_RENDERS_PER_FRAME) {
                     break;
                  }

                  long ck = child.entryOrigin.asLong();
                  if (!CAPTURE_STACK.contains(ck) && !CAPTURED_FRAME.contains(ck)) {
                     try {
                        captureRecursive(mc, child, delta, depth + 1, maxDepth, cam.eye, cam.yaw, cam.pitch);
                     } catch (Throwable var17) {
                        PortalGunMod.LOGGER.debug("Nested portal capture skip: {}", var17.toString());
                     }
                  }
               }
            }

            return captureFromExit(mc, req, delta, depth, cam);
         }
      } else {
         return Boolean.TRUE.equals(STALE_OK.get(key));
      }
   }

   @Nullable
   private static PortalSeeThrough.CamPose computeExitCamera(PortalSeeThrough.CaptureRequest req, Vec3 viewerEye, float viewerYaw, float viewerPitch) {
      Direction pairUp = req.pairUp != null ? req.pairUp : PortalTransform.defaultUp(req.pairFacing);
      Vec3 pairCenter = apertureCenter(req.pairOrigin, req.pairFacing, pairUp, req.pairW, req.pairH);
      Vec3 pN = dirVec(req.pairFacing);
      double OUT = 0.35;
      double DROP = 0.38;
      float yaw;
      float pitch;
      Vec3 camEye;
      if (req.pairFacing == Direction.UP) {
         Direction h = PortalGunHelper.heightDirection(req.pairFacing, pairUp);
         yaw = h.toYRot();
         pitch = -90.0F;
         camEye = pairCenter.add(pN.scale(0.35));
      } else if (req.pairFacing == Direction.DOWN) {
         Direction h = PortalGunHelper.heightDirection(req.pairFacing, pairUp);
         yaw = h.toYRot();
         pitch = 90.0F;
         camEye = pairCenter.add(pN.scale(0.35));
      } else {
         yaw = req.pairFacing.toYRot();
         pitch = 0.0F;
         camEye = pairCenter.add(pN.scale(0.35));
         camEye = new Vec3(camEye.x, camEye.y - 0.38, camEye.z);
      }

      return new PortalSeeThrough.CamPose(camEye, yaw, pitch);
   }

   private static Vec3 dirVec(Direction d) {
      return new Vec3((double)d.getStepX(), (double)d.getStepY(), (double)d.getStepZ());
   }

   private static float partial(DeltaTracker delta) {
      try {
         return delta.getGameTimeDeltaPartialTick(false);
      } catch (Throwable var2) {
         return 1.0F;
      }
   }

   private static float viewYaw(Entity entity, float partialTick) {
      return entity instanceof LivingEntity living ? living.getViewYRot(partialTick) : entity.getYRot();
   }

   private static float viewPitch(Entity entity, float partialTick) {
      return entity instanceof LivingEntity living ? living.getViewXRot(partialTick) : entity.getXRot();
   }

   private static boolean captureFromExit(
      Minecraft mc, PortalSeeThrough.CaptureRequest req, DeltaTracker delta, int recursionLevel, PortalSeeThrough.CamPose cam
   ) {
      if (cam == null || mc.level == null) {
         return false;
      } else if (rendersThisFrame >= MAX_RENDERS_PER_FRAME) {
         return false;
      } else {
         long key = req.entryOrigin.asLong();
         PortalCameraEntity portalCam = obtainCamera(mc.level, key);
         if (portalCam == null) {
            return false;
         } else {
            int pxCap = recursionLevel > 0 ? FBO_MAX_NESTED : FBO_MAX;
            int pxPer = recursionLevel > 0 ? Math.max(48, PX_PER_BLOCK / (1 + recursionLevel)) : PX_PER_BLOCK;
            int fboW = Mth.clamp(req.entryW * pxPer, 64, pxCap);
            int fboH = Mth.clamp(req.entryH * pxPer, 64, pxCap);
            TextureTarget target = TARGETS.get(key);
            if (target == null) {
               target = new TextureTarget(fboW, fboH, true, Minecraft.ON_OSX);
               target.setClearColor(0.12F, 0.16F, 0.28F, 1.0F);
               TARGETS.put(key, target);
            } else if (target.width != fboW || target.height != fboH) {
               target.resize(fboW, fboH, Minecraft.ON_OSX);
            }

            portalCam.place(cam.eye.x, cam.eye.y, cam.eye.z, cam.yaw, cam.pitch);
            Entity previousCam = mc.getCameraEntity();
            long prevOrigin = capturingOrigin;
            capturing = true;
            captureDepth++;
            capturingOrigin = key;
            CAPTURE_STACK.add(key);
            rendersThisFrame++;
            RenderTarget main = mc.getMainRenderTarget();

            // Drop render distance for the portal pass (OreSpawn mobs explode cost otherwise)
            int oldDist = 12;
            try {
               oldDist = mc.options.renderDistance().get();
               if (oldDist > CAPTURE_RENDER_DISTANCE) {
                  mc.options.renderDistance().set(CAPTURE_RENDER_DISTANCE);
               }
            } catch (Throwable ignored) {
            }

            boolean var17;
            try {
               mc.setCameraEntity(portalCam);
               mc.gameRenderer.setRenderHand(false);
               mc.gameRenderer.setRenderBlockOutline(false);
               mc.gameRenderer.renderLevel(delta);
               mc.gameRenderer.setRenderHand(true);
               mc.gameRenderer.setRenderBlockOutline(true);
               blitCropped(main, target);
               main.bindWrite(true);
               CAPTURED_FRAME.add(key);
               var17 = true;
            } finally {
               try {
                  mc.options.renderDistance().set(oldDist);
               } catch (Throwable ignored) {
               }
               CAPTURE_STACK.remove(key);
               captureDepth--;
               capturing = captureDepth > 0;
               capturingOrigin = prevOrigin;
               if (previousCam != null) {
                  mc.setCameraEntity(previousCam);
               } else if (mc.player != null) {
                  mc.setCameraEntity(mc.player);
               }

               main.bindWrite(true);
            }

            return var17;
         }
      }
   }

   private static PortalCameraEntity obtainCamera(Level level, long originKey) {
      try {
         PortalCameraEntity cam = CAMERAS.get(originKey);
         if (cam == null || cam.level() != level) {
            cam = (PortalCameraEntity)((EntityType)ModEntities.PORTAL_CAMERA.get()).create(level);
            CAMERAS.put(originKey, cam);
         }

         return cam;
      } catch (Throwable var4) {
         PortalGunMod.LOGGER.warn("Portal camera entity create failed: {}", var4.toString());
         return null;
      }
   }

   private static void destroyCameras() {
      CAMERAS.clear();
   }

   private static List<PortalSeeThrough.CaptureRequest> findNearbyPortals(Level level, Vec3 eye, long excludeKey, int max) {
      List<PortalSeeThrough.CaptureRequest> found = new ArrayList<>();
      if (level == null) {
         return found;
      } else {
         BlockPos base = BlockPos.containing(eye);
         int r = (int)Math.ceil(24.0);
         int minCx = SectionPos.blockToSectionCoord(base.getX() - r);
         int maxCx = SectionPos.blockToSectionCoord(base.getX() + r);
         int minCz = SectionPos.blockToSectionCoord(base.getZ() - r);
         int maxCz = SectionPos.blockToSectionCoord(base.getZ() + r);
         double maxSq = 576.0;

         for (int cx = minCx; cx <= maxCx; cx++) {
            for (int cz = minCz; cz <= maxCz; cz++) {
               if (level.hasChunk(cx, cz)) {
                  LevelChunk chunk = level.getChunk(cx, cz);

                  for (BlockEntity be : chunk.getBlockEntities().values()) {
                     if (be instanceof PortalMasterBlockEntity) {
                        PortalMasterBlockEntity master = (PortalMasterBlockEntity)be;
                        if (master.isOrigin() && master.hasPair()) {
                           long k = master.getBlockPos().asLong();
                           if (k != excludeKey && !CAPTURE_STACK.contains(k)) {
                              double d = eye.distanceToSqr(Vec3.atCenterOf(master.getBlockPos()));
                              if (!(d > maxSq)) {
                                 PortalSeeThrough.CaptureRequest req = fromBe(master);
                                 if (req != null) {
                                    found.add(req);
                                 }
                              }
                           }
                        }
                     }
                  }
               }
            }
         }

         found.sort(Comparator.comparingDouble(r0 -> eye.distanceToSqr(Vec3.atCenterOf(r0.entryOrigin))));
         return (List<PortalSeeThrough.CaptureRequest>)(found.size() > max ? new ArrayList<>(found.subList(0, max)) : found);
      }
   }

   @Nullable
   private static PortalSeeThrough.CaptureRequest fromBe(PortalMasterBlockEntity be) {
      return be != null && be.isOrigin() && be.hasPair()
         ? new PortalSeeThrough.CaptureRequest(
            be.getBlockPos().immutable(),
            be.getFacing(),
            be.getUpDir(),
            Math.max(1, be.getPortalWidth()),
            Math.max(1, be.getPortalHeight()),
            be.getPairPos().immutable(),
            be.getPairFacing(),
            be.getPairUpDir(),
            Math.max(1, be.getPairWidth()),
            Math.max(1, be.getPairHeight())
         )
         : null;
   }

   private static boolean seeThroughEnabled() {
      try {
         return (Boolean)PortalGunConfig.SEE_THROUGH_PORTALS.get();
      } catch (Throwable var1) {
         return true;
      }
   }

   private static void blitCropped(RenderTarget src, RenderTarget dst) {
      RenderSystem.assertOnRenderThread();
      float srcAspect = (float)src.width / (float)src.height;
      float dstAspect = (float)dst.width / (float)dst.height;
      int srcX = 0;
      int srcY = 0;
      int srcW = src.width;
      int srcH = src.height;
      if (srcAspect > dstAspect) {
         srcW = Math.max(1, Math.round((float)src.height * dstAspect));
         srcX = (src.width - srcW) / 2;
      } else if (srcAspect < dstAspect) {
         srcH = Math.max(1, Math.round((float)src.width / dstAspect));
         srcY = (src.height - srcH) / 2;
      }

      GlStateManager._glBindFramebuffer(36008, src.frameBufferId);
      GlStateManager._glBindFramebuffer(36009, dst.frameBufferId);
      GlStateManager._glBlitFrameBuffer(srcX, srcY, srcX + srcW, srcY + srcH, 0, 0, dst.width, dst.height, 16384, 9729);
      GlStateManager._glBindFramebuffer(36160, 0);
   }

   public static Vec3 apertureCenter(BlockPos origin, Direction face, int width, int height) {
      return apertureCenter(origin, face, PortalTransform.defaultUp(face), width, height);
   }

   public static Vec3 apertureCenter(BlockPos origin, Direction face, Direction upDir, int width, int height) {
      Direction widthDir = PortalGunHelper.widthDirection(face, upDir);
      Direction heightDir = PortalGunHelper.heightDirection(face, upDir);
      int w0 = PortalGunHelper.widthStart(width);
      int h0 = PortalGunHelper.heightStart(face, height);
      double midW = (double)w0 + (double)(width - 1) * 0.5;
      double midH = (double)h0 + (double)(height - 1) * 0.5;
      double cx = (double)origin.getX() + 0.5 + (double)widthDir.getStepX() * midW + (double)heightDir.getStepX() * midH;
      double cy = (double)origin.getY() + 0.5 + (double)widthDir.getStepY() * midW + (double)heightDir.getStepY() * midH;
      double cz = (double)origin.getZ() + 0.5 + (double)widthDir.getStepZ() * midW + (double)heightDir.getStepZ() * midH;
      cx += (double)face.getStepX() * 0.05;
      cy += (double)face.getStepY() * 0.05;
      cz += (double)face.getStepZ() * 0.05;
      return new Vec3(cx, cy, cz);
   }

   @SubscribeEvent
   public static void onLogout(LoggingOut event) {
      for (TextureTarget t : TARGETS.values()) {
         t.destroyBuffers();
      }

      TARGETS.clear();
      VALID.clear();
      STALE_OK.clear();
      REQUESTS.clear();
      CAPTURED_FRAME.clear();
      CAPTURE_STACK.clear();
      capturing = false;
      captureDepth = 0;
      capturingOrigin = Long.MIN_VALUE;
      rendersThisFrame = 0;
      destroyCameras();
   }

   private static record CamPose(Vec3 eye, float yaw, float pitch) {
   }

   private static record CaptureRequest(
      BlockPos entryOrigin,
      Direction entryFacing,
      Direction entryUp,
      int entryW,
      int entryH,
      BlockPos pairOrigin,
      Direction pairFacing,
      Direction pairUp,
      int pairW,
      int pairH
   ) {
   }
}
