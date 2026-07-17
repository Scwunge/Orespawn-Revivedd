package me.scwunge.mods.portalgun.portal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import me.scwunge.mods.portalgun.portal.info.PortalInfo;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedData.Factory;

public class PortalSavedData extends SavedData {
   public static final String ID = "orespawn_portals";
   private final Map<String, PortalSavedData.PortalEntry> portals = new HashMap<>();

   public static Factory<PortalSavedData> factory() {
      return new Factory<>(PortalSavedData::new, PortalSavedData::load);
   }

   public static PortalSavedData get(ServerLevel level) {
      return (PortalSavedData)level.getDataStorage().computeIfAbsent(factory(), "orespawn_portals");
   }

   public static String makeKey(String uuid, String channel, boolean isTypeA) {
      return uuid + "_" + channel + "_" + (isTypeA ? "A" : "B");
   }

   public static String makeKey(PortalInfo info) {
      return makeKey(info.uuid, info.channelName, info.isTypeA);
   }

   public Map<String, PortalSavedData.PortalEntry> getPortals() {
      return this.portals;
   }

   public void registerPortal(PortalInfo info, Direction face, int width, int height) {
      this.registerPortal(info, face, PortalGunHelper.heightDirection(face), width, height);
   }

   public void registerPortal(PortalInfo info, Direction face, Direction upDir, int width, int height) {
      String key = makeKey(info);
      this.portals.put(key, new PortalSavedData.PortalEntry(info, face, upDir, width, height));
      PortalInfo pair = this.findPair(info);
      if (pair != null) {
         info.setPair(pair);
         pair.setPair(info);
      } else {
         info.setPair(null);
      }

      this.setDirty();
   }

   public PortalSavedData.PortalEntry removeSameType(String uuid, String channel, boolean isTypeA) {
      PortalSavedData.PortalEntry removed = this.portals.remove(makeKey(uuid, channel, isTypeA));
      if (removed != null) {
         PortalInfo pair = removed.info.getPair();
         if (pair != null) {
            pair.setPair(null);
            removed.info.setPair(null);
         }

         this.setDirty();
      }

      return removed;
   }

   public PortalSavedData.PortalEntry removeSameType(PortalInfo info) {
      return this.removeSameType(info.uuid, info.channelName, info.isTypeA);
   }

   public PortalInfo findPair(PortalInfo info) {
      PortalSavedData.PortalEntry entry = this.portals.get(makeKey(info.uuid, info.channelName, !info.isTypeA));
      return entry != null ? entry.info : null;
   }

   public PortalSavedData.PortalEntry getEntry(String uuid, String channel, boolean isTypeA) {
      return this.portals.get(makeKey(uuid, channel, isTypeA));
   }

   public List<PortalSavedData.PortalEntry> listForChannel(String uuid, String channel) {
      List<PortalSavedData.PortalEntry> list = new ArrayList<>(2);
      PortalSavedData.PortalEntry a = this.portals.get(makeKey(uuid, channel, true));
      PortalSavedData.PortalEntry b = this.portals.get(makeKey(uuid, channel, false));
      if (a != null) {
         list.add(a);
      }

      if (b != null) {
         list.add(b);
      }

      return list;
   }

   public static PortalSavedData load(CompoundTag tag, Provider registries) {
      PortalSavedData data = new PortalSavedData();
      ListTag list = tag.getList("portals", 10);

      for (int i = 0; i < list.size(); i++) {
         CompoundTag entryTag = list.getCompound(i);
         PortalInfo info = new PortalInfo().readFromNBT(entryTag.getCompound("info"));
         Direction face = Direction.byName(entryTag.getString("face"));
         if (face == null) {
            face = Direction.NORTH;
         }

         Direction upDir = Direction.byName(entryTag.getString("upDir"));
         if (upDir == null) {
            upDir = PortalGunHelper.heightDirection(face);
         }

         int width = entryTag.contains("width") ? Math.max(1, entryTag.getInt("width")) : 1;
         int height = entryTag.contains("height") ? Math.max(1, entryTag.getInt("height")) : (face.getAxis().isHorizontal() ? 2 : 1);
         data.portals.put(makeKey(info), new PortalSavedData.PortalEntry(info, face, upDir, width, height));
      }

      for (PortalSavedData.PortalEntry entry : data.portals.values()) {
         PortalInfo pair = data.findPair(entry.info);
         if (pair != null) {
            entry.info.setPair(pair);
         }
      }

      return data;
   }

   public CompoundTag save(CompoundTag tag, Provider registries) {
      ListTag list = new ListTag();

      for (PortalSavedData.PortalEntry entry : this.portals.values()) {
         CompoundTag entryTag = new CompoundTag();
         entryTag.put("info", entry.info.writeToNBT(new CompoundTag()));
         entryTag.putString("face", entry.face.getSerializedName());
         entryTag.putString("upDir", (entry.upDir != null ? entry.upDir : PortalGunHelper.heightDirection(entry.face)).getSerializedName());
         entryTag.putInt("width", entry.width);
         entryTag.putInt("height", entry.height);
         list.add(entryTag);
      }

      tag.put("portals", list);
      return tag;
   }

   public static final class PortalEntry {
      public final PortalInfo info;
      public Direction face;
      public Direction upDir;
      public int width;
      public int height;

      public PortalEntry(PortalInfo info, Direction face) {
         this(info, face, PortalGunHelper.heightDirection(face), 1, face != null && face.getAxis().isHorizontal() ? 2 : 1);
      }

      public PortalEntry(PortalInfo info, Direction face, int width, int height) {
         this(info, face, PortalGunHelper.heightDirection(face), width, height);
      }

      public PortalEntry(PortalInfo info, Direction face, Direction upDir, int width, int height) {
         this.info = info;
         this.face = face;
         this.upDir = upDir != null ? upDir : PortalGunHelper.heightDirection(face);
         this.width = Math.max(1, width);
         this.height = Math.max(1, height);
      }

      public boolean containsCell(BlockPos cell) {
         Direction up = this.upDir != null ? this.upDir : PortalGunHelper.heightDirection(this.face);
         return PortalGunHelper.forEachPortalCell(this.info.getPos(), this.face, up, this.width, this.height, cell::equals);
      }
   }
}
