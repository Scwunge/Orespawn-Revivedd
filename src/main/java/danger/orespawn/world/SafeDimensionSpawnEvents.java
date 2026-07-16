package danger.orespawn.world;

import danger.orespawn.init.ModDimensions;
import danger.orespawn.util.Reference;
import danger.orespawn.util.Teleport;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.level.LevelEvent;

/**
 * Prevents players spawning / respawning in void (Aetherial Islands terrain).
 * Fixes shared spawn when Islands first loads and rescues players who join mid-air.
 */
@EventBusSubscriber(modid = Reference.MOD_ID)
public final class SafeDimensionSpawnEvents {
    private SafeDimensionSpawnEvents() {}

    @SubscribeEvent
    public static void onLevelLoad(LevelEvent.Load event) {
        if (!(event.getLevel() instanceof ServerLevel level)) {
            return;
        }
        if (!level.dimension().equals(ModDimensions.ISLANDS)) {
            return;
        }
        BlockPos current = level.getSharedSpawnPos();
        BlockPos safe = Teleport.findSafeLanding(level, current.getX(), current.getZ());
        if (!safe.equals(current)) {
            level.setDefaultSpawnPos(safe, 0.0F);
        }
    }

    @SubscribeEvent
    public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) {
            return;
        }
        rescueIfVoid(player);
    }

    @SubscribeEvent
    public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) {
            return;
        }
        rescueIfVoid(player);
    }

    @SubscribeEvent
    public static void onDimChange(PlayerEvent.PlayerChangedDimensionEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) {
            return;
        }
        if (event.getTo().equals(ModDimensions.ISLANDS)
                || event.getTo().equals(ModDimensions.UTOPIA)
                || event.getTo().equals(ModDimensions.CRYSTAL)
                || event.getTo().equals(ModDimensions.CHAOS)
                || event.getTo().equals(ModDimensions.EXTREME)) {
            rescueIfVoid(player);
        }
    }

    private static void rescueIfVoid(ServerPlayer player) {
        Level level = player.level();
        if (!(level instanceof ServerLevel server) || level.isClientSide) {
            return;
        }
        BlockPos feet = player.blockPosition();
        // Falling into void or standing on air with no floor nearby
        boolean noFloor = server.getBlockState(feet.below()).isAir()
                && server.getBlockState(feet.below(2)).isAir()
                && server.getBlockState(feet.below(3)).isAir();
        boolean lowVoid = player.getY() < server.getMinBuildHeight() + 5;
        if (!noFloor && !lowVoid) {
            return;
        }
        // Only auto-rescue in OreSpawn dims (and islands especially)
        if (!server.dimension().equals(ModDimensions.ISLANDS)
                && !server.dimension().equals(ModDimensions.UTOPIA)
                && !server.dimension().equals(ModDimensions.CRYSTAL)
                && !server.dimension().equals(ModDimensions.CHAOS)
                && !server.dimension().equals(ModDimensions.EXTREME)
                && !server.dimension().equals(ModDimensions.VILLAGE_MANIA)
                && !server.dimension().equals(ModDimensions.MINING)) {
            return;
        }

        BlockPos safe = Teleport.findSafeLanding(server, feet.getX(), feet.getZ());
        player.teleportTo(
                server,
                safe.getX() + 0.5,
                safe.getY(),
                safe.getZ() + 0.5,
                player.getYRot(),
                player.getXRot());
        player.resetFallDistance();
        player.setDeltaMovement(0.0, 0.0, 0.0);
    }
}
