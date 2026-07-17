package me.scwunge.mods.portalgun.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import me.scwunge.mods.portalgun.item.PortalGunData;
import me.scwunge.mods.portalgun.item.PortalGunItem;
import me.scwunge.mods.portalgun.network.PortalStatusPayload;
import me.scwunge.mods.portalgun.portal.PortalGunHelper;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

@EventBusSubscriber(
   modid = "orespawn"
)
public final class PortalGunCommands {
   private PortalGunCommands() {
   }

   @SubscribeEvent
   public static void onRegisterCommands(RegisterCommandsEvent event) {
      CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();
      dispatcher.register(buildRoot("portalgun"));
      dispatcher.register(buildRoot("pg"));
   }

   private static LiteralArgumentBuilder<CommandSourceStack> buildRoot(String name) {
      return (LiteralArgumentBuilder<CommandSourceStack>)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal(
                           name
                        )
                        .requires(src -> src.getEntity() instanceof ServerPlayer))
                     .then(
                        Commands.literal("channel")
                           .then(
                              Commands.argument("name", StringArgumentType.greedyString())
                                 .executes(ctx -> setChannel((CommandSourceStack)ctx.getSource(), StringArgumentType.getString(ctx, "name")))
                           )
                     ))
                  .then(
                     Commands.literal("size")
                        .then(
                           Commands.argument("w", IntegerArgumentType.integer(1, 5))
                              .then(
                                 Commands.argument("h", IntegerArgumentType.integer(1, 5))
                                    .executes(
                                       ctx -> setSize(
                                             (CommandSourceStack)ctx.getSource(),
                                             IntegerArgumentType.getInteger(ctx, "w"),
                                             IntegerArgumentType.getInteger(ctx, "h")
                                          )
                                    )
                              )
                        )
                  ))
               .then(
                  Commands.literal("strength")
                     .then(
                        Commands.argument("level", IntegerArgumentType.integer(1, 5))
                           .executes(ctx -> setStrength((CommandSourceStack)ctx.getSource(), IntegerArgumentType.getInteger(ctx, "level")))
                     )
               ))
            .then(Commands.literal("reset").executes(ctx -> reset((CommandSourceStack)ctx.getSource()))))
         .then(Commands.literal("info").executes(ctx -> info((CommandSourceStack)ctx.getSource())));
   }

   private static ItemStack requireGun(CommandSourceStack source) {
      ServerPlayer player = source.getPlayer();
      if (player == null) {
         return ItemStack.EMPTY;
      } else {
         ItemStack main = player.getMainHandItem();
         if (main.getItem() instanceof PortalGunItem) {
            return main;
         } else {
            ItemStack off = player.getOffhandItem();
            return off.getItem() instanceof PortalGunItem ? off : ItemStack.EMPTY;
         }
      }
   }

   private static int failNoGun(CommandSourceStack source) {
      source.sendFailure(Component.translatable("orespawn.cmd.need_gun"));
      return 0;
   }

   private static int setChannel(CommandSourceStack source, String name) {
      ItemStack gun = requireGun(source);
      if (gun.isEmpty()) {
         return failNoGun(source);
      } else {
         PortalGunData.setChannel(gun, name);
         String ch = PortalGunData.channel(gun);
         source.sendSuccess(() -> Component.translatable("orespawn.info.channel", new Object[]{ch}), false);
         ServerPlayer player = source.getPlayer();
         if (player != null && player.level() instanceof ServerLevel level) {
            PortalStatusPayload.syncTo(player, level, ch);
         }

         return 1;
      }
   }

   private static int setSize(CommandSourceStack source, int w, int h) {
      ItemStack gun = requireGun(source);
      if (gun.isEmpty()) {
         return failNoGun(source);
      } else {
         PortalGunData.setSize(gun, w, h);
         source.sendSuccess(() -> Component.translatable("orespawn.info.size", new Object[]{PortalGunData.width(gun), PortalGunData.height(gun)}), false);
         return 1;
      }
   }

   private static int setStrength(CommandSourceStack source, int level) {
      ItemStack gun = requireGun(source);
      if (gun.isEmpty()) {
         return failNoGun(source);
      } else {
         PortalGunData.setGrabStrength(gun, level);
         source.sendSuccess(() -> Component.translatable("orespawn.info.grab_strength", new Object[]{PortalGunData.grabStrength(gun)}), false);
         return 1;
      }
   }

   private static int reset(CommandSourceStack source) {
      ItemStack gun = requireGun(source);
      if (gun.isEmpty()) {
         return failNoGun(source);
      } else {
         ServerPlayer player = source.getPlayer();
         if (player != null && player.level() instanceof ServerLevel level) {
            PortalGunData.ensureDefaults(gun);
            int var5 = PortalGunHelper.resetPlayerPortals(level, player, PortalGunData.channel(gun));
            source.sendSuccess(() -> Component.translatable("orespawn.cmd.reset", new Object[]{var5}), false);
            return 1;
         } else {
            return 0;
         }
      }
   }

   private static int info(CommandSourceStack source) {
      ItemStack gun = requireGun(source);
      if (gun.isEmpty()) {
         return failNoGun(source);
      } else {
         PortalGunData.ensureDefaults(gun);
         String last = PortalGunData.lastOrange(gun) ? "Orange" : "Blue";
         source.sendSuccess(
            () -> Component.translatable(
                  "orespawn.cmd.info",
                  new Object[]{PortalGunData.channel(gun), PortalGunData.width(gun), PortalGunData.height(gun), PortalGunData.grabStrength(gun), last}
               ),
            false
         );
         return 1;
      }
   }
}
