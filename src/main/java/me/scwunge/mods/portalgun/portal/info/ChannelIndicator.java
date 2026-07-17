package me.scwunge.mods.portalgun.portal.info;

public final class ChannelIndicator {
   public final ChannelInfo info;
   public final String dimension;
   public boolean portalAAvailable;
   public boolean portalBAvailable;

   public ChannelIndicator(ChannelInfo info, String dimension) {
      this.info = info;
      this.dimension = dimension == null ? "minecraft:overworld" : dimension;
   }

   public ChannelIndicator setPortalAStatus(boolean available) {
      this.portalAAvailable = available;
      return this;
   }

   public ChannelIndicator setPortalBStatus(boolean available) {
      this.portalBAvailable = available;
      return this;
   }

   @Override
   public boolean equals(Object o) {
      if (o instanceof ChannelIndicator other && other.info.equals(this.info) && other.dimension.equals(this.dimension)) {
         return true;
      }

      return false;
   }

   @Override
   public int hashCode() {
      return this.info.hashCode() * 31 + this.dimension.hashCode();
   }
}
