package danger.orespawn.init;

import danger.orespawn.util.Reference;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.levelgen.Heightmap;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;

/**
 * Gold {@code EntitySpawns} — natural spawn <em>placement</em> rules for 1.21.1.
 * <p>
 * Biome weights live in datapack biome JSON / {@code neoforge:add_spawns} modifiers.
 * This class registers {@link RegisterSpawnPlacementsEvent} so dim and overworld lists
 * can actually place entities.
 * <p>
 * Coverage: all living OreSpawn mobs that appear on gold dim/overworld spawn lists
 * (skips projectiles, heads, island entities, elevators, coins/tshirts).
 */
@EventBusSubscriber(modid = Reference.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public final class EntitySpawns {
    private EntitySpawns() {}

    @SubscribeEvent
    public static void registerSpawnPlacements(RegisterSpawnPlacementsEvent event) {
        // ——— Ambient / small flying ———
        groundMob(event, ModEntities.BIRD.get());
        groundMob(event, ModEntities.BUTTERFLY.get());
        groundMob(event, ModEntities.FIREFLY.get());
        groundMob(event, ModEntities.FAIRY.get());
        groundMob(event, ModEntities.MOSQUITO.get());
        groundMob(event, ModEntities.MOTH.get());
        groundMob(event, ModEntities.GHOST.get());
        groundMob(event, ModEntities.GHOST_SKELLY.get());
        groundMob(event, ModEntities.DRAGONFLY.get());
        groundMob(event, ModEntities.CLIFF_RACER.get());
        groundMob(event, ModEntities.CLOUD_SHARK.get());
        groundMob(event, ModEntities.GOLD_FISH.get());
        groundMob(event, ModEntities.PEACOCK.get());
        groundMob(event, ModEntities.COCKATEIL.get());
        groundMob(event, ModEntities.RUBY_BIRD.get());
        groundMob(event, ModEntities.STINKY.get());
        groundMob(event, ModEntities.DRAGON.get());
        groundMob(event, ModEntities.BEE.get());

        // ——— Creatures / pets / dinos that use Mob spawn rules ———
        groundMob(event, ModEntities.BEAVER.get());
        groundMob(event, ModEntities.CASSOWARY.get());
        groundMob(event, ModEntities.REDCOW.get());
        groundMob(event, ModEntities.GOLD_COW.get());
        groundMob(event, ModEntities.ENCHANTED_COW.get());
        groundMob(event, ModEntities.CRYSTAL_COW.get());
        groundMob(event, ModEntities.STINKBUG.get());
        groundMob(event, ModEntities.CHIPMUNK.get());
        groundMob(event, ModEntities.CRICKET.get());
        groundMob(event, ModEntities.BARYONYX.get());
        groundMob(event, ModEntities.CAMARASAURUS.get());
        groundMob(event, ModEntities.VELOCITYRAPTOR.get());
        groundMob(event, ModEntities.GAMMAMETROID.get());
        groundMob(event, ModEntities.SPYRO.get());
        groundMob(event, ModEntities.ANT.get());
        groundMob(event, ModEntities.RED_ANT.get());
        groundMob(event, ModEntities.RAINBOW_ANT.get());
        groundMob(event, ModEntities.UNSTABLE_ANT.get());
        groundMob(event, ModEntities.TERMITE.get());
        groundMob(event, ModEntities.GAZELLE.get());
        groundMob(event, ModEntities.GIRLFRIEND.get());
        groundMob(event, ModEntities.BOYFRIEND.get());
        groundMob(event, ModEntities.EASTER_BUNNY.get());
        groundMob(event, ModEntities.BANDP.get());
        groundMob(event, ModEntities.OSTRICH.get());
        groundMob(event, ModEntities.LIZARD.get());
        groundMob(event, ModEntities.HYDROLISC.get());
        groundMob(event, ModEntities.RUBBER_DUCKY.get());
        groundMob(event, ModEntities.THE_PRINCE.get());
        groundMob(event, ModEntities.THE_PRINCESS.get());
        groundMob(event, ModEntities.THE_PRINCE_TEEN.get());
        groundMob(event, ModEntities.THE_PRINCE_ADULT.get());
        groundMob(event, ModEntities.LEON.get());
        groundMob(event, ModEntities.CEPHADROME.get());
        groundMob(event, ModEntities.WATER_DRAGON.get());
        groundMob(event, ModEntities.MOTHRA.get());
        groundMob(event, ModEntities.DOOM_WORM.get());

        // ——— Monsters (Monster spawn rules) ———
        groundMonster(event, ModEntities.CAVEFISHER.get());
        groundMonster(event, ModEntities.MANTIS.get());
        groundMonster(event, ModEntities.RAT.get());
        groundMonster(event, ModEntities.BRUTALFLY.get());
        groundMonster(event, ModEntities.KYUUBI.get());
        groundMonster(event, ModEntities.SMALL_WORM.get());
        groundMonster(event, ModEntities.MEDIUM_WORM.get());
        groundMonster(event, ModEntities.LARGE_WORM.get());
        groundMonster(event, ModEntities.SPIDER_DRIVER.get());
        groundMonster(event, ModEntities.LEAF_MONSTER.get());
        groundMonster(event, ModEntities.CREEPING_HORROR.get());
        groundMonster(event, ModEntities.TERRIBLE_TERROR.get());
        groundMonster(event, ModEntities.LURKING_TERROR.get());
        groundMonster(event, ModEntities.ENDER_REAPER.get());
        groundMonster(event, ModEntities.ENDER_KNIGHT.get());
        groundMonster(event, ModEntities.BASILISK.get());
        groundMonster(event, ModEntities.ALOSAURUS.get());
        groundMonster(event, ModEntities.TREX.get());
        groundMonster(event, ModEntities.POINTYSAURUS.get());
        groundMonster(event, ModEntities.CRYOLOPHOSAURUS.get());
        groundMonster(event, ModEntities.ALIEN.get());
        groundMonster(event, ModEntities.NASTYSAURUS.get());
        groundMonster(event, ModEntities.SCORPION.get());
        groundMonster(event, ModEntities.URCHIN.get());
        groundMonster(event, ModEntities.CRAB.get());
        groundMonster(event, ModEntities.ROBOT1.get());
        groundMonster(event, ModEntities.ROBOT2.get());
        groundMonster(event, ModEntities.ROBOT3.get());
        groundMonster(event, ModEntities.ROBOT4.get());
        groundMonster(event, ModEntities.ROBOT5.get());
        // PathfinderMob robots (not Monster) — Mob spawn rules
        groundMob(event, ModEntities.ANT_ROBOT.get());
        groundMob(event, ModEntities.SPIDER_ROBOT.get());
        groundMonster(event, ModEntities.GIANT_ROBOT.get());
        groundMonster(event, ModEntities.MOLENOID.get());
        groundMonster(event, ModEntities.VORTEX.get());
        groundMonster(event, ModEntities.ROTATOR.get());
        groundMonster(event, ModEntities.CATERKILLER.get());
        groundMonster(event, ModEntities.EMPEROR_SCORPION.get());
        groundMonster(event, ModEntities.HERCULES_BEETLE.get());
        groundMonster(event, ModEntities.PITCH_BLACK.get());
        groundMonster(event, ModEntities.TROOPER_BUG.get());
        groundMonster(event, ModEntities.SPIT_BUG.get());
        groundMonster(event, ModEntities.DUNGEON_BEAST.get());
        groundMonster(event, ModEntities.TRIFFID.get());
        groundMonster(event, ModEntities.THE_KING.get());
        groundMonster(event, ModEntities.THE_QUEEN.get());
        groundMonster(event, ModEntities.GODZILLA.get());
        groundMonster(event, ModEntities.KRAKEN.get());
        groundMonster(event, ModEntities.SEA_MONSTER.get());
        groundMonster(event, ModEntities.SEA_VIPER.get());
        groundMonster(event, ModEntities.ATTACK_SQUID.get());
        groundMonster(event, ModEntities.HAMMERHEAD.get());

        // ——— Water (IN_WATER placement) ———
        waterMob(event, ModEntities.IRUKANDJI.get());
        waterMob(event, ModEntities.SKATE.get());
        waterMob(event, ModEntities.WHALE.get());
        waterMob(event, ModEntities.FLOUNDER.get());
        waterMob(event, ModEntities.FROG.get());
    }

    private static <T extends Mob> void groundMob(
            RegisterSpawnPlacementsEvent event, EntityType<T> type) {
        event.register(
                type,
                SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                Mob::checkMobSpawnRules,
                RegisterSpawnPlacementsEvent.Operation.OR);
    }

    private static <T extends Monster> void groundMonster(
            RegisterSpawnPlacementsEvent event, EntityType<T> type) {
        event.register(
                type,
                SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                Monster::checkMonsterSpawnRules,
                RegisterSpawnPlacementsEvent.Operation.OR);
    }

    private static <T extends Mob> void waterMob(
            RegisterSpawnPlacementsEvent event, EntityType<T> type) {
        event.register(
                type,
                SpawnPlacementTypes.IN_WATER,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                Mob::checkMobSpawnRules,
                RegisterSpawnPlacementsEvent.Operation.OR);
    }
}
