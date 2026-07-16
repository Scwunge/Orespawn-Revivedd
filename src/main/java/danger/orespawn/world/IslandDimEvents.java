package danger.orespawn.world;

/**
 * Gold {@code OreSpawnWorld.addIslands} floating-island seed blocks.
 * <p>
 * Disabled while Islands terrain is driven by the Aetherial Islands noise pack
 * ({@code orespawn:islands}). Not an event subscriber — leaving
 * {@code @EventBusSubscriber} with no {@code @SubscribeEvent} methods crashes NeoForge.
 * <p>
 * To restore gold island-entity seeds, re-add a {@code ChunkEvent.Load} handler here.
 */
public final class IslandDimEvents {
    private IslandDimEvents() {}
}
