package danger.orespawn.world;

/**
 * Intentionally not an event subscriber.
 * <p>
 * A bulk terrain rewrite on {@code ChunkEvent.Load} froze the server (void + dead chat).
 * Crystal look is datapack features only ({@code crystal_surface_patch}, ores, maze).
 * <p>
 * Do not add {@code @EventBusSubscriber} here unless there is at least one
 * {@code @SubscribeEvent} method — empty subscribers fail mod load.
 */
public final class CrystalDimEvents {
    private CrystalDimEvents() {}
}
