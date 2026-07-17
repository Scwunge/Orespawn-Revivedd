package me.scwunge.mods.portalgun.network;

import me.scwunge.mods.portalgun.item.PortalGunData;
import me.scwunge.mods.portalgun.item.PortalGunItem;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload.Type;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record SetChannelPayload(String channel) implements CustomPacketPayload {
   public static final Type<SetChannelPayload> TYPE = new Type(ResourceLocation.fromNamespaceAndPath("orespawn", "set_channel"));
   public static final StreamCodec<RegistryFriendlyByteBuf, SetChannelPayload> STREAM_CODEC = StreamCodec.composite(
      ByteBufCodecs.STRING_UTF8, SetChannelPayload::channel, SetChannelPayload::new
   );

   public Type<? extends CustomPacketPayload> type() {
      return TYPE;
   }

   public static void handle(SetChannelPayload payload, IPayloadContext context) {
      context.enqueueWork(() -> {
         if (context.player() instanceof ServerPlayer player) {
            ItemStack gun = findGun(player);
            if (!gun.isEmpty()) {
               PortalGunData.ensureDefaults(gun);
               String ch = PortalGunData.sanitizeChannel(payload.channel());
               PortalGunData.setChannel(gun, ch);
               player.sendSystemMessage(Component.translatable("orespawn.info.channel", new Object[]{ch}));
               if (player.level() instanceof ServerLevel level) {
                  PortalStatusPayload.syncTo(player, level, ch);
               }
            }
         }
      });
   }

   private static ItemStack findGun(ServerPlayer player) {
      if (player.getMainHandItem().getItem() instanceof PortalGunItem) {
         return player.getMainHandItem();
      } else {
         return player.getOffhandItem().getItem() instanceof PortalGunItem ? player.getOffhandItem() : ItemStack.EMPTY;
      }
   }
}
