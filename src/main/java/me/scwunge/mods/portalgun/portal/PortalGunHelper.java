package me.scwunge.mods.portalgun.portal;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Predicate;
import me.scwunge.mods.portalgun.block.PortalBlock;
import me.scwunge.mods.portalgun.block.entity.PortalMasterBlockEntity;
import me.scwunge.mods.portalgun.config.PortalGunConfig;
import me.scwunge.mods.portalgun.init.ModBlocks;
import me.scwunge.mods.portalgun.init.ModSounds;
import me.scwunge.mods.portalgun.item.PortalGunData;
import me.scwunge.mods.portalgun.item.PortalGunItem;
import me.scwunge.mods.portalgun.network.PortalStatusPayload;
import me.scwunge.mods.portalgun.portal.info.PortalInfo;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

public final class PortalGunHelper {
   public static final int COLOUR_BLUE = 361215;
   public static final int COLOUR_ORANGE = 16756742;
   public static final int COLOUR_ATLAS_A = 5482192;
   public static final int COLOUR_ATLAS_B = 4064209;
   public static final int COLOUR_PBODY_A = 16373344;
   public static final int COLOUR_PBODY_B = 8394260;
   public static final int MIN_SIZE = 1;
   public static final int MAX_SIZE = 5;
   private static final ThreadLocal<Boolean> FIZZLING = ThreadLocal.withInitial(() -> false);

   private PortalGunHelper() {
   }

   public static int[] coloursForChannel(String uuid, String channelName) {
      String ch = channelName == null ? "" : channelName;
      if (ch.equalsIgnoreCase("Chell")) {
         return new int[]{361215, 16756742};
      } else if (ch.equalsIgnoreCase("Atlas")) {
         return new int[]{5482192, 4064209};
      } else {
         return !ch.equalsIgnoreCase("P-body") && !ch.equalsIgnoreCase("Pbody")
            ? generateChannelColour(uuid == null ? "" : uuid, ch)
            : new int[]{16373344, 8394260};
      }
   }

   public static int[] generateChannelColour(String uuid, String channelName) {
      String generator = uuid + "_" + channelName;
      Random rand = new Random();
      long seed = (long)generator.hashCode() * (long)Math.max(1, uuid.hashCode());
      rand.setSeed(Math.abs(seed));
      int colourA = Math.round(1.6777215E7F * rand.nextFloat());
      float[] hsb = rgbToHsb(colourA >> 16 & 0xFF, colourA >> 8 & 0xFF, colourA & 0xFF);
      hsb[2] = 0.65F + 0.25F * hsb[2];
      colourA = hsbToRgb(hsb[0], hsb[1], hsb[2]);
      hsb[0] += 0.5F;
      if (hsb[0] > 1.0F) {
         hsb[0]--;
      }

      int colourB = hsbToRgb(hsb[0], hsb[1], hsb[2]);
      return new int[]{colourA, colourB};
   }

   private static float[] rgbToHsb(int r, int g, int b) {
      float rf = (float)r / 255.0F;
      float gf = (float)g / 255.0F;
      float bf = (float)b / 255.0F;
      float max = Math.max(rf, Math.max(gf, bf));
      float min = Math.min(rf, Math.min(gf, bf));
      float delta = max - min;
      float h = 0.0F;
      if (delta > 1.0E-6F) {
         if (max == rf) {
            h = (gf - bf) / delta % 6.0F;
         } else if (max == gf) {
            h = (bf - rf) / delta + 2.0F;
         } else {
            h = (rf - gf) / delta + 4.0F;
         }

         h /= 6.0F;
         if (h < 0.0F) {
            h++;
         }
      }

      float s = max <= 1.0E-6F ? 0.0F : delta / max;
      return new float[]{h, s, max};
   }

   private static int hsbToRgb(float h, float s, float v) {
      float hh = (h % 1.0F + 1.0F) % 1.0F * 6.0F;
      int i = (int)Math.floor((double)hh);
      float f = hh - (float)i;
      float p = v * (1.0F - s);
      float q = v * (1.0F - s * f);
      float t = v * (1.0F - s * (1.0F - f));
      float r;
      float g;
      float b;
      switch (i) {
         case 0:
            r = v;
            g = t;
            b = p;
            break;
         case 1:
            r = q;
            g = v;
            b = p;
            break;
         case 2:
            r = p;
            g = v;
            b = t;
            break;
         case 3:
            r = p;
            g = q;
            b = v;
            break;
         case 4:
            r = t;
            g = p;
            b = v;
            break;
         default:
            r = v;
            g = p;
            b = q;
      }

      return Math.round(r * 255.0F) << 16 | Math.round(g * 255.0F) << 8 | Math.round(b * 255.0F);
   }

   public static boolean tryPlaceFromHit(
      ServerLevel level, Player owner, BlockHitResult hit, boolean orange, int width, int height, String uuid, String channel
   ) {
      return tryPlaceFromHitLiving(level, owner, hit, orange, width, height, uuid, channel);
   }

   public static boolean tryPlaceFromHitMob(
      ServerLevel level, LivingEntity owner, BlockHitResult hit, boolean orange, int width, int height, String uuid, String channel
   ) {
      return tryPlaceFromHitLiving(level, owner, hit, orange, width, height, uuid, channel);
   }

   private static boolean tryPlaceFromHitLiving(
      ServerLevel level, LivingEntity owner, BlockHitResult hit, boolean orange, int width, int height, String uuid, String channel
   ) {
      int reqW = clampSize(width);
      int reqH = clampSize(height);
      Direction face = hit.getDirection();
      BlockPos supportPos = hit.getBlockPos();
      BlockPos hitOrigin = supportPos.relative(face);
      Direction upDir = placementUpDir(face, owner);
      PortalSavedData data = PortalSavedData.get(level);
      boolean isTypeA = !orange;
      PortalSavedData.PortalEntry pendingClear = data.getEntry(uuid, channel, isTypeA);
      boolean canResize = true;

      try {
         canResize = (Boolean)PortalGunConfig.CAN_PORTALS_RESIZE.get();
      } catch (Throwable var24) {
      }

      int minH = 1;
      int minW = 1;

      for (int h = reqH; h >= minH; h--) {
         for (int w = reqW; w >= minW; w--) {
            PortalGunHelper.PlacementFit fit = findPlacement(level, hitOrigin, face, upDir, w, h, pendingClear);
            if (fit != null) {
               return placeGrid(
                  level, owner instanceof Player p ? p : null, fit.origin, face, fit.upDir, fit.width, fit.height, orange, uuid, channel, pendingClear, data
               );
            }

            if (!canResize) {
               playInvalid(level, owner instanceof Player p ? p : null, hitOrigin);
               return false;
            }
         }
      }

      playInvalid(level, owner instanceof Player p ? p : null, hitOrigin);
      return false;
   }

   private static PortalGunHelper.PlacementFit findPlacement(
      ServerLevel level, BlockPos hitOrigin, Direction face, Direction preferUp, int width, int height, PortalSavedData.PortalEntry pendingClear
   ) {
      List<Direction> ups = new ArrayList<>(2);
      ups.add(preferUp != null ? preferUp : heightDirection(face));
      if (face.getAxis().isVertical()) {
         Direction rot = ups.get(0).getClockWise();
         if (!ups.contains(rot)) {
            ups.add(rot);
         }
      }

      int[][] sizes = width == height ? new int[][]{{width, height}} : new int[][]{{width, height}, {height, width}};
      int maxOff = Math.max(width, height);

      for (int[] wh : sizes) {
         int w = wh[0];
         int h = wh[1];

         for (Direction up : ups) {
            Direction wDir = widthDirection(face, up);
            Direction hDir = heightDirection(face, up);

            for (int radius = 0; radius <= maxOff; radius++) {
               for (int ow = -radius; ow <= radius; ow++) {
                  for (int oh = -radius; oh <= radius; oh++) {
                     if (radius <= 0 || Math.max(Math.abs(ow), Math.abs(oh)) == radius) {
                        BlockPos origin = hitOrigin.relative(wDir, ow).relative(hDir, oh);
                        if (canPlaceGrid(level, origin, face, up, w, h, pendingClear)) {
                           return new PortalGunHelper.PlacementFit(origin, up, w, h);
                        }
                     }
                  }
               }
            }
         }
      }

      return null;
   }

   private static boolean placeGrid(
      ServerLevel level,
      Player owner,
      BlockPos origin,
      Direction face,
      Direction upDir,
      int placeW,
      int placeH,
      boolean orange,
      String uuid,
      String channel,
      PortalSavedData.PortalEntry pendingClear,
      PortalSavedData data
   ) {
      boolean isTypeA = !orange;
      int[] channelCols = coloursForChannel(uuid, channel);
      int colour = (isTypeA ? channelCols[0] : channelCols[1]) & 16777215;
      PortalSavedData.PortalEntry old = data.removeSameType(uuid, channel, isTypeA);
      if (old != null) {
         clearPortalBlocks(level, old);
      }

      BlockState portalState = (BlockState)((BlockState)((PortalBlock)ModBlocks.PORTAL.get()).defaultBlockState().setValue(PortalBlock.FACING, face))
         .setValue(PortalBlock.TYPE_A, isTypeA);
      forEachPortalCell(origin, face, upDir, placeW, placeH, cell -> {
         level.setBlock(cell, portalState, 3);
         boolean isOrigin = cell.equals(origin);
         writePortalMaster(level, cell, uuid, channel, isTypeA, colour, face, upDir, isOrigin, placeW, placeH);
         return false;
      });
      PortalInfo info = new PortalInfo().setInfo(uuid, channel, isTypeA).setColour(colour).setPos(origin);
      data.registerPortal(info, face, upDir, placeW, placeH);
      syncPairDataOnMasters(level, uuid, channel);
      level.playSound(
         null,
         (double)origin.getX() + 0.5,
         (double)origin.getY() + 0.5,
         (double)origin.getZ() + 0.5,
         isTypeA ? (SoundEvent)ModSounds.PORTAL_OPEN_BLUE.get() : (SoundEvent)ModSounds.PORTAL_OPEN_RED.get(),
         SoundSource.BLOCKS,
         0.6F,
         1.0F + (level.random.nextFloat() - level.random.nextFloat()) * 0.1F
      );
      PortalStatusPayload.broadcastChannel(level, uuid, channel);
      ChannelIndicatorServer.notifyListeners(level, uuid, channel);
      return true;
   }

   public static void syncPairDataOnMasters(ServerLevel level, String uuid, String channel) {
      PortalSavedData data = PortalSavedData.get(level);
      PortalSavedData.PortalEntry a = data.getEntry(uuid, channel, true);
      PortalSavedData.PortalEntry b = data.getEntry(uuid, channel, false);
      if (a != null && b != null) {
         writePairOntoOrigin(level, a, b);
         writePairOntoOrigin(level, b, a);
      } else {
         if (a != null) {
            clearPairOnOrigin(level, a);
         }

         if (b != null) {
            clearPairOnOrigin(level, b);
         }
      }
   }

   private static void writePairOntoOrigin(ServerLevel level, PortalSavedData.PortalEntry self, PortalSavedData.PortalEntry other) {
      if (level.getBlockEntity(self.info.getPos()) instanceof PortalMasterBlockEntity be) {
         Direction otherUp = other.upDir != null ? other.upDir : heightDirection(other.face);
         be.setPairData(other.info.getPos(), other.face, otherUp, other.width, other.height);
      }
   }

   private static void clearPairOnOrigin(ServerLevel level, PortalSavedData.PortalEntry self) {
      if (level.getBlockEntity(self.info.getPos()) instanceof PortalMasterBlockEntity be) {
         be.clearPairData();
      }
   }

   public static boolean canPlaceGrid(ServerLevel level, BlockPos origin, Direction face, int width, int height, PortalSavedData.PortalEntry replaceableOld) {
      return canPlaceGrid(level, origin, face, heightDirection(face), width, height, replaceableOld);
   }

   public static boolean canPlaceGrid(
      ServerLevel level, BlockPos origin, Direction face, Direction upDir, int width, int height, PortalSavedData.PortalEntry replaceableOld
   ) {
      return !forEachPortalCell(origin, face, upDir, width, height, cell -> {
         if (!isReplaceableForPlace(level, cell, replaceableOld)) {
            return true;
         } else {
            BlockPos support = cell.relative(face.getOpposite());
            BlockState supportState = level.getBlockState(support);
            return !supportState.isFaceSturdy(level, support, face);
         }
      });
   }

   public static boolean forEachPortalCell(BlockPos origin, Direction face, int width, int height, Predicate<BlockPos> visitor) {
      return forEachPortalCell(origin, face, heightDirection(face), width, height, visitor);
   }

   public static boolean forEachPortalCell(BlockPos origin, Direction face, Direction upDir, int width, int height, Predicate<BlockPos> visitor) {
      if (origin != null && face != null && width >= 1 && height >= 1) {
         Direction widthDir = widthDirection(face, upDir);
         Direction heightDir = heightDirection(face, upDir);
         int w0 = widthStart(width);
         int h0 = heightStart(face, height);

         for (int dw = 0; dw < width; dw++) {
            for (int dh = 0; dh < height; dh++) {
               BlockPos cell = origin.relative(widthDir, w0 + dw).relative(heightDir, h0 + dh);
               if (visitor.test(cell)) {
                  return true;
               }
            }
         }

         return false;
      } else {
         return false;
      }
   }

   public static Direction placementUpDir(Direction face, LivingEntity owner) {
      if (face == null || face.getAxis().isHorizontal()) {
         return Direction.UP;
      } else if (owner == null) {
         return Direction.SOUTH;
      } else {
         Vec3 look = owner.getLookAngle();
         double lx = look.x;
         double lz = look.z;
         if (lx * lx + lz * lz < 1.0E-6) {
            Direction body = owner.getDirection();
            return body.getAxis().isHorizontal() ? body : Direction.SOUTH;
         } else {
            Direction fromLook = Direction.getNearest(lx, 0.0, lz);
            return fromLook.getAxis().isHorizontal() ? fromLook : Direction.SOUTH;
         }
      }
   }

   public static Direction widthDirection(Direction face) {
      return widthDirection(face, null);
   }

   public static Direction widthDirection(Direction face, Direction upDir) {
      if (face != null && face.getAxis().isHorizontal()) {
         return face.getClockWise();
      } else {
         Direction h = heightDirection(face, upDir);
         return h.getClockWise();
      }
   }

   public static Direction heightDirection(Direction face) {
      return heightDirection(face, null);
   }

   public static Direction heightDirection(Direction face, Direction upDir) {
      if (face != null && face.getAxis().isHorizontal()) {
         return Direction.UP;
      } else {
         return upDir != null && upDir.getAxis().isHorizontal() ? upDir : Direction.SOUTH;
      }
   }

   public static int widthStart(int width) {
      return -(width - 1) / 2;
   }

   public static int heightStart(Direction face, int height) {
      return face.getAxis().isHorizontal() ? 0 : -(height - 1) / 2;
   }

   public static double axisMid(int size, int start) {
      return (double)start + (double)(size - 1) * 0.5;
   }

   public static int clampSize(int v) {
      return Mth.clamp(v, 1, 5);
   }

   public static void writePortalMaster(
      ServerLevel level, BlockPos pos, String uuid, String channel, boolean isTypeA, Direction face, boolean isOrigin, int width, int height
   ) {
      writePortalMaster(level, pos, uuid, channel, isTypeA, -1, face, heightDirection(face), isOrigin, width, height);
   }

   public static void writePortalMaster(
      ServerLevel level, BlockPos pos, String uuid, String channel, boolean isTypeA, Direction face, Direction upDir, boolean isOrigin, int width, int height
   ) {
      writePortalMaster(level, pos, uuid, channel, isTypeA, -1, face, upDir, isOrigin, width, height);
   }

   public static void writePortalMaster(
      ServerLevel level,
      BlockPos pos,
      String uuid,
      String channel,
      boolean isTypeA,
      int colour,
      Direction face,
      Direction upDir,
      boolean isOrigin,
      int width,
      int height
   ) {
      if (level.getBlockEntity(pos) instanceof PortalMasterBlockEntity be) {
         be.setPortalData(uuid, channel, isTypeA, colour, face, upDir, isOrigin, width, height);
      }
   }

   private static boolean isReplaceable(ServerLevel level, BlockPos pos) {
      BlockState state = level.getBlockState(pos);
      return state.isAir() || state.canBeReplaced();
   }

   private static boolean isReplaceableForPlace(ServerLevel level, BlockPos pos, PortalSavedData.PortalEntry replaceableOld) {
      return isReplaceable(level, pos)
         ? true
         : replaceableOld != null && level.getBlockState(pos).is((Block)ModBlocks.PORTAL.get()) && replaceableOld.containsCell(pos);
   }

   public static void clearPortalBlocks(ServerLevel level, PortalSavedData.PortalEntry entry) {
      clearPortalBlocksWithUp(level, entry);
   }

   public static void clearPortalBlocks(ServerLevel level, BlockPos origin, Direction face, int width, int height) {
      clearPortalBlocks(level, origin, face, heightDirection(face), width, height);
   }

   public static void clearPortalBlocks(ServerLevel level, BlockPos origin, Direction face, Direction upDir, int width, int height) {
      if (origin != null && origin.getY() >= level.getMinBuildHeight()) {
         width = Math.max(1, width);
         height = Math.max(1, height);
         forEachPortalCell(origin, face, upDir, width, height, cell -> {
            clearIfPortal(level, cell);
            return false;
         });
      }
   }

   public static void clearPortalBlocksWithUp(ServerLevel level, PortalSavedData.PortalEntry entry) {
      if (entry != null && entry.info != null) {
         clearPortalBlocks(level, entry.info.getPos(), entry.face, entry.upDir != null ? entry.upDir : heightDirection(entry.face), entry.width, entry.height);
      }
   }

   public static void clearPortalBlocks(ServerLevel level, BlockPos origin, Direction face) {
      int h = face != null && face.getAxis().isHorizontal() ? 2 : 1;
      clearPortalBlocks(level, origin, face, 1, h);
   }

   private static void clearIfPortal(ServerLevel level, BlockPos pos) {
      if (level.getBlockState(pos).is((Block)ModBlocks.PORTAL.get())) {
         level.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
      }
   }

   public static boolean isFizzlingPortals() {
      return Boolean.TRUE.equals(FIZZLING.get());
   }

   public static boolean fizzlePortalContaining(ServerLevel level, BlockPos cell) {
      if (level != null && cell != null && !isFizzlingPortals()) {
         PortalSavedData data = PortalSavedData.get(level);
         PortalSavedData.PortalEntry match = null;

         for (PortalSavedData.PortalEntry entry : data.getPortals().values()) {
            if (entry != null && entry.containsCell(cell)) {
               match = entry;
               break;
            }
         }

         if (match == null) {
            if (level.getBlockState(cell).is((Block)ModBlocks.PORTAL.get())) {
               FIZZLING.set(true);

               try {
                  clearIfPortal(level, cell);
               } finally {
                  FIZZLING.set(false);
               }

               return true;
            } else {
               return false;
            }
         } else {
            return fizzleEntry(level, match);
         }
      } else {
         return false;
      }
   }

   public static boolean fizzleEntry(ServerLevel level, PortalSavedData.PortalEntry entry) {
      if (level != null && entry != null && entry.info != null && !isFizzlingPortals()) {
         String uuid = entry.info.uuid;
         String channel = entry.info.channelName;
         boolean typeA = entry.info.isTypeA;
         FIZZLING.set(true);

         try {
            PortalSavedData data = PortalSavedData.get(level);
            PortalSavedData.PortalEntry removed = data.removeSameType(uuid, channel, typeA);
            if (removed != null) {
               clearPortalBlocks(level, removed);
            } else {
               clearPortalBlocks(level, entry);
            }

            syncPairDataOnMasters(level, uuid, channel);
            level.playSound(
               null,
               (double)entry.info.getPos().getX() + 0.5,
               (double)entry.info.getPos().getY() + 0.5,
               (double)entry.info.getPos().getZ() + 0.5,
               (SoundEvent)ModSounds.PORTAL_FIZZLE.get(),
               SoundSource.BLOCKS,
               0.45F,
               1.0F + (level.random.nextFloat() - level.random.nextFloat()) * 0.1F
            );
            PortalStatusPayload.broadcastChannel(level, uuid, channel);
            ChannelIndicatorServer.notifyListeners(level, uuid, channel);
         } finally {
            FIZZLING.set(false);
         }

         return true;
      } else {
         return false;
      }
   }

   public static void fizzlePortalsSupportedBy(ServerLevel level, BlockPos supportPos) {
      if (level != null && supportPos != null && !isFizzlingPortals()) {
         for (Direction face : Direction.values()) {
            BlockPos portalCell = supportPos.relative(face);
            if (level.getBlockState(portalCell).is((Block)ModBlocks.PORTAL.get())) {
               BlockEntity var8 = level.getBlockEntity(portalCell);
               if (var8 instanceof PortalMasterBlockEntity) {
                  PortalMasterBlockEntity be = (PortalMasterBlockEntity)var8;
                  if (be.getFacing() == face) {
                     fizzlePortalContaining(level, portalCell);
                  }
               } else {
                  fizzlePortalContaining(level, portalCell);
               }
            }
         }
      }
   }

   private static void playInvalid(ServerLevel level, Player owner, BlockPos pos) {
      float pitch = 1.0F + (level.random.nextFloat() - level.random.nextFloat()) * 0.1F;
      level.playSound(
         null,
         (double)pos.getX() + 0.5,
         (double)pos.getY() + 0.5,
         (double)pos.getZ() + 0.5,
         (SoundEvent)ModSounds.PORTAL_INVALID.get(),
         SoundSource.BLOCKS,
         0.5F,
         pitch
      );
      if (owner != null) {
         level.playSound(null, owner.getX(), owner.getY(), owner.getZ(), (SoundEvent)ModSounds.PORTAL_INVALID.get(), SoundSource.PLAYERS, 0.25F, pitch);
         playInvalidSurfaceSwt(level, owner);
      }
   }

   public static int resetPlayerPortals(ServerLevel level, Player owner, String channel) {
      ItemStack gun = heldGun(owner);
      String uuid = gun.isEmpty() ? owner.getUUID().toString() : PortalGunData.portalOwnerKey(gun, owner);
      if (!gun.isEmpty()) {
         channel = PortalGunData.channel(gun);
      }

      return resetPortalsFor(level, owner, uuid, channel, null);
   }

   public static int fizzlePlayerColour(ServerLevel level, Player owner, String channel, boolean typeA) {
      ItemStack gun = heldGun(owner);
      String uuid = gun.isEmpty() ? owner.getUUID().toString() : PortalGunData.portalOwnerKey(gun, owner);
      if (!gun.isEmpty()) {
         channel = PortalGunData.channel(gun);
      }

      return resetPortalsFor(level, owner, uuid, channel, typeA);
   }

   public static int resetPortalsFor(ServerLevel level, Player owner, String uuid, String channel, Boolean onlyTypeA) {
      PortalSavedData data = PortalSavedData.get(level);
      int removed = 0;
      boolean[] types = onlyTypeA == null ? new boolean[]{true, false} : new boolean[]{onlyTypeA};
      FIZZLING.set(true);

      try {
         for (boolean typeA : types) {
            PortalSavedData.PortalEntry old = data.removeSameType(uuid, channel, typeA);
            if (old != null) {
               clearPortalBlocks(level, old);
               removed++;
            }
         }

         if (removed > 0) {
            syncPairDataOnMasters(level, uuid, channel);
            level.playSound(null, owner.getX(), owner.getY(), owner.getZ(), (SoundEvent)ModSounds.PORTAL_FIZZLE.get(), SoundSource.PLAYERS, 0.55F, 1.0F);
            level.playSound(
               null,
               owner.getX(),
               owner.getY(),
               owner.getZ(),
               (SoundEvent)ModSounds.FIZZLER_SHIMMY.get(),
               SoundSource.PLAYERS,
               0.4F,
               1.0F + (level.random.nextFloat() - level.random.nextFloat()) * 0.08F
            );
         }
      } finally {
         FIZZLING.set(false);
      }

      PortalStatusPayload.broadcastChannel(level, uuid, channel);
      ChannelIndicatorServer.notifyListeners(level, uuid, channel);
      return removed;
   }

   private static ItemStack heldGun(Player owner) {
      if (owner == null) {
         return ItemStack.EMPTY;
      } else {
         ItemStack main = owner.getMainHandItem();
         if (main.getItem() instanceof PortalGunItem) {
            return main;
         } else {
            ItemStack off = owner.getOffhandItem();
            return off.getItem() instanceof PortalGunItem ? off : ItemStack.EMPTY;
         }
      }
   }

   public static void playInvalidSurfaceSwt(ServerLevel level, Player owner) {
      if (level != null && owner != null) {
         level.playSound(
            null,
            owner.getX(),
            owner.getY(),
            owner.getZ(),
            (SoundEvent)ModSounds.PORTAL_INVALID_SWT.get(),
            SoundSource.PLAYERS,
            0.35F,
            1.0F + (level.random.nextFloat() - level.random.nextFloat()) * 0.1F
         );
      }
   }

   private static record PlacementFit(BlockPos origin, Direction upDir, int width, int height) {
   }
}
