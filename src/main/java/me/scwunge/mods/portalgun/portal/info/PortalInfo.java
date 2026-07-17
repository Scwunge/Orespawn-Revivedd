package me.scwunge.mods.portalgun.portal.info;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;

public class PortalInfo {
   public String uuid;
   public String channelName;
   public boolean isTypeA;
   public int colour = 16777215;
   private BlockPos pos = new BlockPos(-1, -1, -1);
   private PortalInfo pair;

   public PortalInfo setInfo(String uuid, String channelName, boolean isTypeA) {
      this.uuid = uuid;
      this.channelName = channelName;
      this.isTypeA = isTypeA;
      return this;
   }

   public PortalInfo setColour(int colour) {
      this.colour = colour;
      return this;
   }

   public PortalInfo setPos(int x, int y, int z) {
      this.pos = new BlockPos(x, y, z);
      return this;
   }

   public PortalInfo setPos(BlockPos pos) {
      this.pos = pos.immutable();
      return this;
   }

   public BlockPos getPos() {
      return this.pos;
   }

   public void setPair(PortalInfo pair) {
      this.pair = pair;
   }

   public PortalInfo getPair() {
      return this.pair;
   }

   public PortalInfo readFromNBT(CompoundTag tag) {
      this.uuid = tag.getString("uuid");
      this.channelName = tag.getString("channelName");
      this.isTypeA = tag.getBoolean("isTypeA");
      this.colour = tag.getInt("colour");
      this.setPos(tag.getInt("x"), tag.getInt("y"), tag.getInt("z"));
      return this;
   }

   public CompoundTag writeToNBT(CompoundTag tag) {
      tag.putString("uuid", this.uuid);
      tag.putString("channelName", this.channelName);
      tag.putBoolean("isTypeA", this.isTypeA);
      tag.putInt("colour", this.colour);
      tag.putInt("x", this.pos.getX());
      tag.putInt("y", this.pos.getY());
      tag.putInt("z", this.pos.getZ());
      return tag;
   }

   public boolean isPair(PortalInfo info) {
      return info.uuid.equals(this.uuid) && info.channelName.equals(this.channelName) && info.isTypeA != this.isTypeA;
   }

   public boolean isSameType(PortalInfo info) {
      return info.uuid.equals(this.uuid) && info.channelName.equals(this.channelName) && info.isTypeA == this.isTypeA;
   }

   @Override
   public boolean equals(Object o) {
      return !(o instanceof PortalInfo info)
         ? false
         : info.uuid.equals(this.uuid) && info.channelName.equals(this.channelName) && info.isTypeA == this.isTypeA && info.pos.equals(this.pos);
   }

   @Override
   public int hashCode() {
      return (this.uuid + "_" + this.channelName + "_" + (this.isTypeA ? "A" : "B")).hashCode();
   }
}
