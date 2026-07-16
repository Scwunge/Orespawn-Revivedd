package danger.orespawn.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.logging.LogUtils;
import danger.orespawn.init.ModDimensions;
import danger.orespawn.util.Teleport;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import org.slf4j.Logger;

/**
 * Dimension teleports: classic 1.7.10 dims + CF mining + overworld.
 * <pre>
 * /orespawn &lt;dim&gt;
 * /dimensiontp|tpdim|dimtp &lt;dim&gt;
 * </pre>
 * Permission level <b>0</b> so singleplayer without cheats still works for testing.
 * Registered from {@link danger.orespawn.OreSpawnMain} on {@code NeoForge.EVENT_BUS}.
 */
public final class CommandDimensionTeleport {
    private static final Logger LOGGER = LogUtils.getLogger();

    private CommandDimensionTeleport() {}

    public static void onRegisterCommands(RegisterCommandsEvent event) {
        register(event.getDispatcher());
        LOGGER.info("OreSpawn dimension commands registered (/orespawn, /dimensiontp, /tpdim, /dimtp)");
    }

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        // permission 0 = anyone (SP without "Allow Cheats" still works)
        LiteralArgumentBuilder<CommandSourceStack> orespawn = Commands.literal("orespawn")
                .requires(source -> source.hasPermission(0))
                .executes(ctx -> {
                    ctx.getSource()
                            .sendSuccess(
                                    () -> Component.literal(
                                            "Usage: /orespawn <overworld|utopia|extreme|village|islands|crystal|chaos|mining>"),
                                    false);
                    return 1;
                });
        addDestinations(orespawn);
        dispatcher.register(orespawn);

        for (String name : new String[] {"dimensiontp", "tpdim", "dimtp"}) {
            LiteralArgumentBuilder<CommandSourceStack> root = Commands.literal(name)
                    .requires(source -> source.hasPermission(0))
                    .executes(ctx -> {
                        ctx.getSource()
                                .sendSuccess(
                                        () -> Component.literal(
                                                "Usage: /" + name + " <overworld|utopia|extreme|village|islands|crystal|chaos|mining>"),
                                        false);
                        return 1;
                    });
            addDestinations(root);
            dispatcher.register(root);
        }
    }

    private static void addDestinations(LiteralArgumentBuilder<CommandSourceStack> root) {
        root.then(Commands.literal("overworld")
                .executes(ctx -> teleport(ctx, Level.OVERWORLD, "minecraft:overworld")));
        root.then(Commands.literal("utopia")
                .executes(ctx -> teleport(ctx, ModDimensions.UTOPIA, "orespawn:utopia")));
        root.then(Commands.literal("extreme")
                .executes(ctx -> teleport(ctx, ModDimensions.EXTREME, "orespawn:extreme")));
        root.then(Commands.literal("village")
                .executes(ctx -> teleport(ctx, ModDimensions.VILLAGE_MANIA, "orespawn:village_mania")));
        root.then(Commands.literal("village_mania")
                .executes(ctx -> teleport(ctx, ModDimensions.VILLAGE_MANIA, "orespawn:village_mania")));
        root.then(Commands.literal("islands")
                .executes(ctx -> teleport(ctx, ModDimensions.ISLANDS, "orespawn:islands")));
        root.then(Commands.literal("crystal")
                .executes(ctx -> teleport(ctx, ModDimensions.CRYSTAL, "orespawn:crystal")));
        root.then(Commands.literal("chaos")
                .executes(ctx -> teleport(ctx, ModDimensions.CHAOS, "orespawn:chaos")));
        root.then(Commands.literal("mining")
                .executes(ctx -> teleport(ctx, ModDimensions.MINING, "orespawn:mining")));
    }

    private static int teleport(
            CommandContext<CommandSourceStack> ctx, ResourceKey<Level> dimension, String displayName) {
        CommandSourceStack source = ctx.getSource();
        ServerPlayer player;
        try {
            player = source.getPlayerOrException();
        } catch (Exception e) {
            source.sendFailure(Component.literal("Player only — type this in chat while in a world."));
            return 0;
        }

        String err = Teleport.teleportToDimension(
                player, dimension, player.getX(), player.getY(), player.getZ());
        if (err != null) {
            source.sendFailure(Component.literal("Teleport failed (" + displayName + "): " + err));
            return 0;
        }
        source.sendSuccess(() -> Component.literal("Teleported to " + displayName), true);
        return 1;
    }
}
