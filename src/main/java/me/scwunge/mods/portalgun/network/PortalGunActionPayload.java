package me.scwunge.mods.portalgun.network;

import me.scwunge.mods.portalgun.item.PortalGunData;
import me.scwunge.mods.portalgun.item.PortalGunItem;
import me.scwunge.mods.portalgun.portal.GrabHandler;
import me.scwunge.mods.portalgun.portal.PortalGunHelper;
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

public record PortalGunActionPayload(int action) implements CustomPacketPayload {
   public static final Type<PortalGunActionPayload> TYPE = new Type(ResourceLocation.fromNamespaceAndPath("orespawn", "gun_action"));
   public static final StreamCodec<RegistryFriendlyByteBuf, PortalGunActionPayload> STREAM_CODEC = StreamCodec.composite(
      ByteBufCodecs.VAR_INT, PortalGunActionPayload::action, PortalGunActionPayload::new
   );
   public static final int ACTION_GRAB = 0;
   public static final int ACTION_RESET = 1;
   public static final int ACTION_SIZE_UP = 2;
   public static final int ACTION_SIZE_DOWN = 3;
   public static final int ACTION_CHANNEL_CYCLE = 4;
   public static final int ACTION_GRAB_STRENGTH_UP = 5;
   public static final int ACTION_GRAB_STRENGTH_DOWN = 6;
   public static final int ACTION_FIRE_SWAP = 7;
   public static final int ACTION_WIDTH_UP = 8;
   public static final int ACTION_WIDTH_DOWN = 9;
   public static final int ACTION_REQUEST_STATUS = 10;
   public static final int ACTION_FIZZLE_BLUE = 11;
   public static final int ACTION_FIZZLE_ORANGE = 12;

   public Type<? extends CustomPacketPayload> type() {
      return TYPE;
   }

   public static void handle(PortalGunActionPayload payload, IPayloadContext context) {
      context.enqueueWork(() -> {
         if (context.player() instanceof ServerPlayer player) {
            ItemStack gun = findGun(player);
            if (!gun.isEmpty()) {
               PortalGunData.ensureDefaults(gun);
               switch (payload.action()) {
                  case 0:
                     GrabHandler.toggle(player);
                     break;
                  case 1:
                     if (player.level() instanceof ServerLevel levelxx) {
                        int n = PortalGunHelper.resetPlayerPortals(levelxx, player, PortalGunData.channel(gun));
                        player.sendSystemMessage(Component.translatable("orespawn.cmd.reset", new Object[]{n}));
                     }
                     break;
                  case 2:
                     PortalGunData.cycleHeight(gun, 1);
                     player.sendSystemMessage(Component.translatable("orespawn.info.size", new Object[]{PortalGunData.width(gun), PortalGunData.height(gun)}));
                     break;
                  case 3:
                     PortalGunData.cycleHeight(gun, -1);
                     player.sendSystemMessage(Component.translatable("orespawn.info.size", new Object[]{PortalGunData.width(gun), PortalGunData.height(gun)}));
                     break;
                  case 4:
                     String ch = PortalGunData.cycleChannel(gun);
                     player.sendSystemMessage(Component.translatable("orespawn.info.channel", new Object[]{ch}));
                     if (player.level() instanceof ServerLevel level) {
                        PortalStatusPayload.syncTo(player, level, ch);
                     }
                     break;
                  case 5: {
                     int s = PortalGunData.cycleGrabStrength(gun, 1);
                     player.sendSystemMessage(Component.translatable("orespawn.info.grab_strength", new Object[]{s}));
                     break;
                  }
                  case 6: {
                     int s = PortalGunData.cycleGrabStrength(gun, -1);
                     player.sendSystemMessage(Component.translatable("orespawn.info.grab_strength", new Object[]{s}));
                     break;
                  }
                  case 7:
                     PortalGunItem.tryFireSwap(player, gun);
                     break;
                  case 8:
                     PortalGunData.cycleWidth(gun, 1);
                     player.sendSystemMessage(Component.translatable("orespawn.info.size", new Object[]{PortalGunData.width(gun), PortalGunData.height(gun)}));
                     break;
                  case 9:
                     PortalGunData.cycleWidth(gun, -1);
                     player.sendSystemMessage(Component.translatable("orespawn.info.size", new Object[]{PortalGunData.width(gun), PortalGunData.height(gun)}));
                     break;
                  case 10:
                     if (player.level() instanceof ServerLevel level) {
                        PortalStatusPayload.syncTo(player, level, PortalGunData.channel(gun));
                     }
                     break;
                  case 11:
                     if (player.level() instanceof ServerLevel levelx) {
                        int n = PortalGunHelper.fizzlePlayerColour(levelx, player, PortalGunData.channel(gun), true);
                        if (n > 0) {
                           player.sendSystemMessage(Component.translatable("orespawn.cmd.reset", new Object[]{n}));
                        }
                     }
                     break;
                  case 12:
                     if (player.level() instanceof ServerLevel level) {
                        int n = PortalGunHelper.fizzlePlayerColour(level, player, PortalGunData.channel(gun), false);
                        if (n > 0) {
                           player.sendSystemMessage(Component.translatable("orespawn.cmd.reset", new Object[]{n}));
                        }
                     }
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
