package me.scwunge.mods.portalgun.client;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import me.scwunge.mods.portalgun.item.PortalGunData;
import me.scwunge.mods.portalgun.item.PortalGunItem;
import me.scwunge.mods.portalgun.network.RequestChannelIndicatorPayload;
import me.scwunge.mods.portalgun.portal.PortalGunHelper;
import me.scwunge.mods.portalgun.portal.info.ChannelIndicator;
import me.scwunge.mods.portalgun.portal.info.ChannelInfo;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent.LoggingOut;
import net.neoforged.neoforge.client.event.ClientTickEvent.Post;
import net.neoforged.neoforge.network.PacketDistributor;

@EventBusSubscriber(
   modid = "orespawn",
   value = {Dist.CLIENT}
)
public final class ClientChannelIndicators {
   private static final Map<String, Map<String, ChannelIndicator>> CACHE = new HashMap<>();
   private static final Set<String> PENDING = new HashSet<>();
   private static int tickCounter;

   private ClientChannelIndicators() {
   }

   public static ChannelIndicator get(String ownerUuid, String channel) {
      Minecraft mc = Minecraft.getInstance();
      String dim = mc.level != null ? mc.level.dimension().location().toString() : "minecraft:overworld";
      return get(ownerUuid, channel, dim);
   }

   public static ChannelIndicator get(String ownerUuid, String channel, String dimension) {
      String uuid = ownerUuid == null ? "" : ownerUuid;
      String ch = channel != null && !channel.isBlank() ? channel : "Chell";
      String dim = dimension == null ? "minecraft:overworld" : dimension;
      Map<String, ChannelIndicator> byChannel = CACHE.computeIfAbsent(uuid, k -> new HashMap<>());
      ChannelIndicator ind = byChannel.get(ch.toLowerCase());
      boolean needRequest = ind == null;
      if (ind == null) {
         int[] colours = PortalGunHelper.coloursForChannel(uuid, ch);
         ind = new ChannelIndicator(new ChannelInfo(uuid, ch).setColour(colours[0], colours[1]), dim);
         byChannel.put(ch.toLowerCase(), ind);
      }

      String pendingKey = uuid + "|" + ch.toLowerCase() + "|" + dim;
      if (needRequest && !PENDING.contains(pendingKey)) {
         PENDING.add(pendingKey);
         PacketDistributor.sendToServer(new RequestChannelIndicatorPayload(uuid, ch, dim), new CustomPacketPayload[0]);
      }

      return ind;
   }

   public static void apply(String ownerUuid, String channel, String dimension, int colourA, boolean portalA, int colourB, boolean portalB) {
      String uuid = ownerUuid == null ? "" : ownerUuid;
      String ch = channel != null && !channel.isBlank() ? channel : "Chell";
      String dim = dimension == null ? "minecraft:overworld" : dimension;
      Map<String, ChannelIndicator> byChannel = CACHE.computeIfAbsent(uuid, k -> new HashMap<>());
      ChannelIndicator ind = byChannel.get(ch.toLowerCase());
      if (ind == null) {
         ind = new ChannelIndicator(new ChannelInfo(uuid, ch), dim);
         byChannel.put(ch.toLowerCase(), ind);
      }

      ind.info.setColour(colourA, colourB);
      ind.setPortalAStatus(portalA);
      ind.setPortalBStatus(portalB);
      PENDING.remove(uuid + "|" + ch.toLowerCase() + "|" + dim);
      Minecraft mc = Minecraft.getInstance();
      if (mc.player != null && uuid.equals(mc.player.getUUID().toString())) {
         ClientPortalStatus.set(portalA, portalB);
      }
   }

   public static void clear() {
      CACHE.clear();
      PENDING.clear();
   }

   public static ChannelIndicator localHeld() {
      Minecraft mc = Minecraft.getInstance();
      LocalPlayer player = mc.player;
      if (player == null) {
         return new ChannelIndicator(new ChannelInfo("", "Chell").setColour(361215, 16756742), "minecraft:overworld");
      } else {
         ItemStack gun = heldGun(player);
         String ch = gun.isEmpty() ? "Chell" : PortalGunData.channel(gun);
         return get(player.getUUID().toString(), ch);
      }
   }

   @SubscribeEvent
   public static void onTick(Post event) {
      Minecraft mc = Minecraft.getInstance();
      if (mc.player != null && mc.level != null && !mc.isPaused()) {
         tickCounter++;
         if (tickCounter % 20 == 0) {
            ItemStack localGun = heldGun(mc.player);
            if (!localGun.isEmpty()) {
               get(mc.player.getUUID().toString(), PortalGunData.channel(localGun));
            }

            for (AbstractClientPlayer other : mc.level.players()) {
               if (other != mc.player && !(other.distanceToSqr(mc.player) > 4096.0)) {
                  ItemStack gun = heldGun(other);
                  if (!gun.isEmpty()) {
                     get(other.getUUID().toString(), PortalGunData.channel(gun));
                  }
               }
            }

            if (tickCounter % 100 == 0) {
               PENDING.clear();
            }
         }
      }
   }

   @SubscribeEvent
   public static void onLogout(LoggingOut event) {
      clear();
   }

   private static ItemStack heldGun(Player player) {
      ItemStack main = player.getMainHandItem();
      if (main.getItem() instanceof PortalGunItem) {
         return main;
      } else {
         ItemStack off = player.getOffhandItem();
         return off.getItem() instanceof PortalGunItem ? off : ItemStack.EMPTY;
      }
   }
}
