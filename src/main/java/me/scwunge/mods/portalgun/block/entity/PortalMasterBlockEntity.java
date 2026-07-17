package me.scwunge.mods.portalgun.block.entity;

import me.scwunge.mods.portalgun.init.ModBlockEntities;
import me.scwunge.mods.portalgun.portal.PortalGunHelper;
import me.scwunge.mods.portalgun.portal.info.PortalInfo;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class PortalMasterBlockEntity extends BlockEntity {
   private String uuid = "";
   private String channelName = "Chell";
   private boolean isTypeA = true;
   private int colour = 16777215;
   private Direction facing = Direction.NORTH;
   private Direction upDir = Direction.SOUTH;
   private boolean origin = true;
   private int width = 1;
   private int height = 1;
   private long placedGameTime;
   private boolean hasPair;
   private BlockPos pairPos = BlockPos.ZERO;
   private Direction pairFacing = Direction.NORTH;
   private Direction pairUpDir = Direction.UP;
   private int pairWidth = 1;
   private int pairHeight = 1;

   public PortalMasterBlockEntity(BlockPos pos, BlockState state) {
      super((BlockEntityType)ModBlockEntities.PORTAL_MASTER.get(), pos, state);
   }

   public void setPortalData(String uuid, String channelName, boolean isTypeA, Direction facing, boolean origin, int width, int height) {
      this.setPortalData(uuid, channelName, isTypeA, -1, facing, defaultUpForFace(facing), origin, width, height);
   }

   public void setPortalData(String uuid, String channelName, boolean isTypeA, Direction facing, Direction upDir, boolean origin, int width, int height) {
      this.setPortalData(uuid, channelName, isTypeA, -1, facing, upDir, origin, width, height);
   }

   public void setPortalData(
      String uuid, String channelName, boolean isTypeA, int colour, Direction facing, Direction upDir, boolean origin, int width, int height
   ) {
      this.uuid = uuid != null ? uuid : "";
      this.channelName = channelName != null ? channelName : "Chell";
      this.isTypeA = isTypeA;
      if (colour >= 0) {
         this.colour = colour & 16777215;
      } else {
         int[] pair = PortalGunHelper.coloursForChannel(this.uuid, this.channelName);
         this.colour = (isTypeA ? pair[0] : pair[1]) & 16777215;
      }

      this.facing = facing != null ? facing : Direction.NORTH;
      this.upDir = sanitizeUpDir(this.facing, upDir);
      this.origin = origin;
      this.width = Math.max(1, width);
      this.height = Math.max(1, height);
      if (this.level != null) {
         this.placedGameTime = this.level.getGameTime();
      }

      this.markAndSync();
   }

   public void setPortalData(String uuid, String channelName, boolean isTypeA, Direction facing) {
      this.setPortalData(uuid, channelName, isTypeA, facing, true, 1, 1);
   }

   public void setPortalData(PortalInfo info, Direction facing) {
      this.setPortalData(info.uuid, info.channelName, info.isTypeA, info.colour, facing, defaultUpForFace(facing), true, 1, 1);
   }

   private static Direction defaultUpForFace(Direction face) {
      return face != null && face.getAxis().isHorizontal() ? Direction.UP : Direction.SOUTH;
   }

   private static Direction sanitizeUpDir(Direction face, Direction upDir) {
      if (face != null && face.getAxis().isHorizontal()) {
         return Direction.UP;
      } else {
         return upDir != null && upDir.getAxis().isHorizontal() ? upDir : Direction.SOUTH;
      }
   }

   public void setPairData(@Nullable BlockPos pairOrigin, @Nullable Direction pairFacing, int pairWidth, int pairHeight) {
      this.setPairData(pairOrigin, pairFacing, null, pairWidth, pairHeight);
   }

   public void setPairData(@Nullable BlockPos pairOrigin, @Nullable Direction pairFacing, @Nullable Direction pairUp, int pairWidth, int pairHeight) {
      if (pairOrigin != null && pairFacing != null) {
         this.hasPair = true;
         this.pairPos = pairOrigin.immutable();
         this.pairFacing = pairFacing;
         this.pairUpDir = pairUp != null ? pairUp : defaultUpForFace(pairFacing);
         this.pairWidth = Math.max(1, pairWidth);
         this.pairHeight = Math.max(1, pairHeight);
      } else {
         this.hasPair = false;
         this.pairPos = BlockPos.ZERO;
         this.pairFacing = Direction.NORTH;
         this.pairUpDir = Direction.UP;
         this.pairWidth = 1;
         this.pairHeight = 1;
      }

      this.markAndSync();
   }

   public void clearPairData() {
      this.setPairData(null, null, 1, 1);
   }

   private void markAndSync() {
      this.setChanged();
      if (this.level != null && !this.level.isClientSide) {
         BlockState state = this.getBlockState();
         this.level.sendBlockUpdated(this.worldPosition, state, state, 3);
      }
   }

   public String getUuid() {
      return this.uuid;
   }

   public String getChannelName() {
      return this.channelName;
   }

   public boolean isTypeA() {
      return this.isTypeA;
   }

   public int getColour() {
      return this.colour & 16777215;
   }

   public Direction getFacing() {
      return this.facing;
   }

   public Direction getUpDir() {
      return this.upDir;
   }

   public boolean isOrigin() {
      return this.origin;
   }

   public int getPortalWidth() {
      return this.width;
   }

   public int getPortalHeight() {
      return this.height;
   }

   public long getPlacedGameTime() {
      return this.placedGameTime;
   }

   public float getOpenProgress(float partialTick) {
      if (this.level != null && this.placedGameTime > 0L) {
         float age = (float)(this.level.getGameTime() - this.placedGameTime) + partialTick;
         if (age >= 10.0F) {
            return 1.0F;
         } else if (age <= 0.0F) {
            return 0.05F;
         } else {
            float t = age / 10.0F;
            return 0.05F + 0.95F * (1.0F - (1.0F - t) * (1.0F - t));
         }
      } else {
         return 1.0F;
      }
   }

   public boolean hasPair() {
      return this.hasPair;
   }

   public BlockPos getPairPos() {
      return this.pairPos;
   }

   public Direction getPairFacing() {
      return this.pairFacing;
   }

   public Direction getPairUpDir() {
      return this.pairUpDir;
   }

   public int getPairWidth() {
      return this.pairWidth;
   }

   public int getPairHeight() {
      return this.pairHeight;
   }

   protected void saveAdditional(CompoundTag tag, Provider registries) {
      super.saveAdditional(tag, registries);
      tag.putString("uuid", this.uuid);
      tag.putString("channelName", this.channelName);
      tag.putBoolean("isTypeA", this.isTypeA);
      tag.putInt("colour", this.colour & 16777215);
      tag.putString("facing", this.facing.getSerializedName());
      tag.putString("upDir", this.upDir.getSerializedName());
      tag.putBoolean("origin", this.origin);
      tag.putInt("width", this.width);
      tag.putInt("height", this.height);
      tag.putLong("placedGameTime", this.placedGameTime);
      tag.putBoolean("hasPair", this.hasPair);
      if (this.hasPair) {
         tag.putInt("pairX", this.pairPos.getX());
         tag.putInt("pairY", this.pairPos.getY());
         tag.putInt("pairZ", this.pairPos.getZ());
         tag.putString("pairFacing", this.pairFacing.getSerializedName());
         tag.putString("pairUpDir", this.pairUpDir.getSerializedName());
         tag.putInt("pairW", this.pairWidth);
         tag.putInt("pairH", this.pairHeight);
      }
   }

   protected void loadAdditional(CompoundTag tag, Provider registries) {
      super.loadAdditional(tag, registries);
      this.uuid = tag.getString("uuid");
      this.channelName = tag.contains("channelName") ? tag.getString("channelName") : "Chell";
      this.isTypeA = !tag.contains("isTypeA") || tag.getBoolean("isTypeA");
      if (tag.contains("colour")) {
         this.colour = tag.getInt("colour") & 16777215;
      } else {
         int[] pair = PortalGunHelper.coloursForChannel(this.uuid, this.channelName);
         this.colour = (this.isTypeA ? pair[0] : pair[1]) & 16777215;
      }

      Direction face = Direction.byName(tag.getString("facing"));
      this.facing = face != null ? face : Direction.NORTH;
      Direction up = Direction.byName(tag.getString("upDir"));
      this.upDir = sanitizeUpDir(this.facing, up);
      this.origin = !tag.contains("origin") || tag.getBoolean("origin");
      this.width = tag.contains("width") ? Math.max(1, tag.getInt("width")) : 1;
      this.height = tag.contains("height") ? Math.max(1, tag.getInt("height")) : 1;
      this.placedGameTime = tag.contains("placedGameTime") ? tag.getLong("placedGameTime") : 0L;
      this.hasPair = tag.getBoolean("hasPair");
      if (this.hasPair) {
         this.pairPos = new BlockPos(tag.getInt("pairX"), tag.getInt("pairY"), tag.getInt("pairZ"));
         Direction pf = Direction.byName(tag.getString("pairFacing"));
         this.pairFacing = pf != null ? pf : Direction.NORTH;
         Direction pu = Direction.byName(tag.getString("pairUpDir"));
         this.pairUpDir = pu != null ? pu : defaultUpForFace(this.pairFacing);
         this.pairWidth = Math.max(1, tag.getInt("pairW"));
         this.pairHeight = Math.max(1, tag.getInt("pairH"));
      } else {
         this.pairPos = BlockPos.ZERO;
         this.pairFacing = Direction.NORTH;
         this.pairUpDir = Direction.UP;
         this.pairWidth = 1;
         this.pairHeight = 1;
      }
   }

   public CompoundTag getUpdateTag(Provider registries) {
      return this.saveWithoutMetadata(registries);
   }

   @Nullable
   public Packet<ClientGamePacketListener> getUpdatePacket() {
      return ClientboundBlockEntityDataPacket.create(this);
   }
}
