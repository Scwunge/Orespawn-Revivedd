package me.scwunge.mods.portalgun.portal.info;

import net.minecraft.nbt.CompoundTag;

public class ChannelInfo {
   public String uuid;
   public String channelName;
   public int colourA;
   public int colourB;

   public ChannelInfo(String uuid, String channelName) {
      this.uuid = uuid;
      this.channelName = channelName;
      this.colourA = -1;
      this.colourB = -1;
   }

   public ChannelInfo setColour(int a, int b) {
      this.colourA = a;
      this.colourB = b;
      return this;
   }

   @Override
   public boolean equals(Object o) {
      if (o instanceof ChannelInfo other && other.uuid.equals(this.uuid) && other.channelName.equals(this.channelName)) {
         return true;
      }

      return false;
   }

   @Override
   public int hashCode() {
      return (this.uuid + "_" + this.channelName).hashCode();
   }

   public ChannelInfo readFromNBT(CompoundTag tag) {
      this.uuid = tag.getString("uuid");
      this.channelName = tag.getString("channelName");
      this.colourA = tag.getInt("colourA");
      this.colourB = tag.getInt("colourB");
      return this;
   }

   public CompoundTag writeToNBT(CompoundTag tag) {
      tag.putString("uuid", this.uuid);
      tag.putString("channelName", this.channelName);
      tag.putInt("colourA", this.colourA);
      tag.putInt("colourB", this.colourB);
      return tag;
   }
}
