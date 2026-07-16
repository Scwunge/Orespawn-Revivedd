package danger.orespawn.entity;

import danger.orespawn.init.ModEntities;
import danger.orespawn.util.Reference;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.registries.DeferredHolder;

@EventBusSubscriber(modid = Reference.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public final class ModEntityAttributes {
    private ModEntityAttributes() {}

    @SubscribeEvent
    public static void onAttributes(EntityAttributeCreationEvent event) {
        // dinos
        event.put(ModEntities.TREX.get(), TRex.createAttributes().build());
        event.put(ModEntities.ALOSAURUS.get(), Alosaurus.createAttributes().build());
        event.put(ModEntities.BARYONYX.get(), Baryonyx.createAttributes().build());
        event.put(ModEntities.CAMARASAURUS.get(), Camarasaurus.createAttributes().build());
        event.put(ModEntities.POINTYSAURUS.get(), Pointysaurus.createAttributes().build());
        event.put(ModEntities.CRYOLOPHOSAURUS.get(), Cryolophosaurus.createAttributes().build());
        event.put(ModEntities.NASTYSAURUS.get(), Nastysaurus.createAttributes().build());
        event.put(ModEntities.VELOCITYRAPTOR.get(), VelocityRaptor.createAttributes().build());

        // small creatures
        event.put(ModEntities.BIRD.get(), Bird.createAttributes().build());
        event.put(ModEntities.BUTTERFLY.get(), Butterfly.createAttributes().build());
        event.put(ModEntities.DRAGONFLY.get(), Dragonfly.createAttributes().build());
        event.put(ModEntities.FIREFLY.get(), Firefly.createAttributes().build());
        event.put(ModEntities.FAIRY.get(), Fairy.createAttributes().build());
        event.put(ModEntities.MOSQUITO.get(), Mosquito.createAttributes().build());
        event.put(ModEntities.MOTH.get(), Moth.createAttributes().build());
        event.put(ModEntities.ANT.get(), Ant.createAttributes().build());
        event.put(ModEntities.RED_ANT.get(), RedAnt.createAttributes().build());
        event.put(ModEntities.RAINBOW_ANT.get(), RainbowAnt.createAttributes().build());
        event.put(ModEntities.UNSTABLE_ANT.get(), UnstableAnt.createAttributes().build());
        event.put(ModEntities.TERMITE.get(), Termite.createAttributes().build());
        event.put(ModEntities.STINKBUG.get(), StinkBug.createAttributes().build());
        event.put(ModEntities.BEAVER.get(), Beaver.createAttributes().build());
        event.put(ModEntities.CASSOWARY.get(), Cassowary.createAttributes().build());
        event.put(ModEntities.CAVEFISHER.get(), CaveFisher.createAttributes().build());
        event.put(ModEntities.CHIPMUNK.get(), Chipmunk.createAttributes().build());
        event.put(ModEntities.CRICKET.get(), Cricket.createAttributes().build());
        event.put(ModEntities.RAT.get(), Rat.createAttributes().build());

        // worms
        event.put(ModEntities.SMALL_WORM.get(), WormSmall.createAttributes().build());
        event.put(ModEntities.MEDIUM_WORM.get(), WormMedium.createAttributes().build());
        event.put(ModEntities.LARGE_WORM.get(), WormLarge.createAttributes().build());
        event.put(ModEntities.DOOM_WORM.get(), WormDoom.createAttributes().build());

        // bosses / misc (other agents)
        event.put(ModEntities.GAMMAMETROID.get(), GammaMetroid.createAttributes().build());
        event.put(ModEntities.SPYRO.get(), Spyro.createAttributes().build());
        event.put(ModEntities.ALIEN.get(), Alien.createAttributes().build());
        event.put(ModEntities.KYUUBI.get(), Kyuubi.createAttributes().build());
        event.put(ModEntities.MANTIS.get(), Mantis.createAttributes().build());
        event.put(ModEntities.MOTHRA.get(), Mothra.createAttributes().build());
        event.put(ModEntities.BRUTALFLY.get(), Brutalfly.createAttributes().build());
        event.put(ModEntities.REDCOW.get(), RedCow.createAttributes().build());

        // classic wave 2026-07-15
        event.put(ModEntities.GHOST.get(), Ghost.createAttributes().build());
        event.put(ModEntities.GHOST_SKELLY.get(), GhostSkelly.createAttributes().build());
        event.put(ModEntities.CREEPING_HORROR.get(), CreepingHorror.createAttributes().build());
        event.put(ModEntities.TERRIBLE_TERROR.get(), TerribleTerror.createAttributes().build());
        event.put(ModEntities.FROG.get(), Frog.createAttributes().build());
        event.put(ModEntities.BEE.get(), Bee.createAttributes().build());
        event.put(ModEntities.RUBBER_DUCKY.get(), RubberDucky.createAttributes().build());
        event.put(ModEntities.FLOUNDER.get(), Flounder.createAttributes().build());
        event.put(ModEntities.GOLD_FISH.get(), GoldFish.createAttributes().build());
        event.put(ModEntities.CLOUD_SHARK.get(), CloudShark.createAttributes().build());

        // classic wave B (full model anims)
        event.put(ModEntities.LURKING_TERROR.get(), LurkingTerror.createAttributes().build());
        event.put(ModEntities.PEACOCK.get(), Peacock.createAttributes().build());
        event.put(ModEntities.CLIFF_RACER.get(), CliffRacer.createAttributes().build());
        event.put(ModEntities.LEAF_MONSTER.get(), LeafMonster.createAttributes().build());
        event.put(ModEntities.SCORPION.get(), Scorpion.createAttributes().build());
        event.put(ModEntities.ATTACK_SQUID.get(), AttackSquid.createAttributes().build());
        event.put(ModEntities.IRUKANDJI.get(), Irukandji.createAttributes().build());
        event.put(ModEntities.SKATE.get(), Skate.createAttributes().build());
        event.put(ModEntities.ENDER_KNIGHT.get(), EnderKnight.createAttributes().build());
        event.put(ModEntities.ENDER_REAPER.get(), EnderReaper.createAttributes().build());

        // classic wave C
        event.put(ModEntities.HYDROLISC.get(), Hydrolisc.createAttributes().build());
        event.put(ModEntities.BASILISK.get(), Basilisk.createAttributes().build());
        event.put(ModEntities.GAZELLE.get(), Gazelle.createAttributes().build());
        event.put(ModEntities.EASTER_BUNNY.get(), EasterBunny.createAttributes().build());
        event.put(ModEntities.BANDP.get(), BandP.createAttributes().build());
        event.put(ModEntities.HAMMERHEAD.get(), Hammerhead.createAttributes().build());
        event.put(ModEntities.WHALE.get(), Whale.createAttributes().build());
        event.put(ModEntities.URCHIN.get(), Urchin.createAttributes().build());
        event.put(ModEntities.CRAB.get(), Crab.createAttributes().build());
        event.put(ModEntities.ROBOT1.get(), Robot1.createAttributes().build());
        event.put(ModEntities.ROBOT2.get(), Robot2.createAttributes().build());
        event.put(ModEntities.MOLENOID.get(), Molenoid.createAttributes().build());
        event.put(ModEntities.VORTEX.get(), Vortex.createAttributes().build());

        // SET1 land/sea
        event.put(ModEntities.OSTRICH.get(), Ostrich.createAttributes().build());
        event.put(ModEntities.LIZARD.get(), Lizard.createAttributes().build());
        event.put(ModEntities.STINKY.get(), Stinky.createAttributes().build());
        event.put(ModEntities.COCKATEIL.get(), Cockateil.createAttributes().build());
        event.put(ModEntities.SEA_MONSTER.get(), SeaMonster.createAttributes().build());
        event.put(ModEntities.SEA_VIPER.get(), SeaViper.createAttributes().build());
        event.put(ModEntities.WATER_DRAGON.get(), WaterDragon.createAttributes().build());

        // SET2 big bugs
        event.put(ModEntities.CATERKILLER.get(), CaterKiller.createAttributes().build());
        event.put(ModEntities.EMPEROR_SCORPION.get(), EmperorScorpion.createAttributes().build());
        event.put(ModEntities.HERCULES_BEETLE.get(), HerculesBeetle.createAttributes().build());
        event.put(ModEntities.PITCH_BLACK.get(), PitchBlack.createAttributes().build());

        // SET3 robots
        event.put(ModEntities.ROBOT3.get(), Robot3.createAttributes().build());
        event.put(ModEntities.ROBOT4.get(), Robot4.createAttributes().build());
        event.put(ModEntities.ROBOT5.get(), Robot5.createAttributes().build());
        event.put(ModEntities.ANT_ROBOT.get(), AntRobot.createAttributes().build());

        // SET4
        event.put(ModEntities.TROOPER_BUG.get(), TrooperBug.createAttributes().build());
        event.put(ModEntities.SPIT_BUG.get(), SpitBug.createAttributes().build());
        event.put(ModEntities.DUNGEON_BEAST.get(), DungeonBeast.createAttributes().build());

        // SET5
        event.put(ModEntities.CEPHADROME.get(), Cephadrome.createAttributes().build());
        event.put(ModEntities.TRIFFID.get(), Triffid.createAttributes().build());
        event.put(ModEntities.SPIDER_ROBOT.get(), SpiderRobot.createAttributes().build());
        event.put(ModEntities.GIANT_ROBOT.get(), GiantRobot.createAttributes().build());

        // SET6
        event.put(ModEntities.THE_PRINCE.get(), ThePrince.createAttributes().build());
        event.put(ModEntities.THE_PRINCESS.get(), ThePrincess.createAttributes().build());
        event.put(ModEntities.THE_PRINCE_TEEN.get(), ThePrinceTeen.createAttributes().build());
        event.put(ModEntities.THE_PRINCE_ADULT.get(), ThePrinceAdult.createAttributes().build());
        event.put(ModEntities.LEON.get(), Leon.createAttributes().build());

        // SET7 Dragon + mega bosses
        event.put(ModEntities.DRAGON.get(), Dragon.createAttributes().build());
        event.put(ModEntities.THE_KING.get(), TheKing.createAttributes().build());
        event.put(ModEntities.KING_HEAD.get(), KingHead.createAttributes().build());
        event.put(ModEntities.THE_QUEEN.get(), TheQueen.createAttributes().build());
        event.put(ModEntities.GODZILLA.get(), Godzilla.createAttributes().build());
        event.put(ModEntities.GODZILLA_HEAD.get(), GodzillaHead.createAttributes().build());
        event.put(ModEntities.KRAKEN.get(), Kraken.createAttributes().build());

        // SET8 social / companions
        event.put(ModEntities.GIRLFRIEND.get(), Girlfriend.createAttributes().build());
        event.put(ModEntities.BOYFRIEND.get(), Boyfriend.createAttributes().build());
        event.put(ModEntities.SPIDER_DRIVER.get(), SpiderDriver.createAttributes().build());
        event.put(ModEntities.QUEEN_HEAD.get(), QueenHead.createAttributes().build());

        // SET9 scenery / vehicles / cows / birds
        event.put(ModEntities.ISLAND.get(), Island.createAttributes().build());
        event.put(ModEntities.ISLAND_TOO.get(), IslandToo.createAttributes().build());
        event.put(ModEntities.ELEVATOR.get(), Elevator.createAttributes().build());
        event.put(ModEntities.ROTATOR.get(), Rotator.createAttributes().build());
        event.put(ModEntities.COIN.get(), Coin.createAttributes().build());
        event.put(ModEntities.TSHIRT.get(), Tshirt.createAttributes().build());
        event.put(ModEntities.PURPLE_POWER.get(), PurplePower.createAttributes().build());
        event.put(ModEntities.ROCK_BASE.get(), RockBase.createAttributes().build());
        event.put(ModEntities.CRYSTAL_COW.get(), CrystalCow.createAttributes().build());
        event.put(ModEntities.GOLD_COW.get(), GoldCow.createAttributes().build());
        event.put(ModEntities.ENCHANTED_COW.get(), EnchantedCow.createAttributes().build());
        event.put(ModEntities.RUBY_BIRD.get(), RubyBird.createAttributes().build());

        // remaining dummies only — skip non-living projectiles and everything registered above
        for (DeferredHolder<EntityType<?>, ? extends EntityType<?>> holder : ModEntities.ENTITY_TYPES.getEntries()) {
            if (holder == ModEntities.TREX
                    || holder == ModEntities.ALOSAURUS
                    || holder == ModEntities.BARYONYX
                    || holder == ModEntities.CAMARASAURUS
                    || holder == ModEntities.POINTYSAURUS
                    || holder == ModEntities.CRYOLOPHOSAURUS
                    || holder == ModEntities.NASTYSAURUS
                    || holder == ModEntities.VELOCITYRAPTOR
                    || holder == ModEntities.BIRD
                    || holder == ModEntities.BUTTERFLY
                    || holder == ModEntities.DRAGONFLY
                    || holder == ModEntities.FIREFLY
                    || holder == ModEntities.FAIRY
                    || holder == ModEntities.MOSQUITO
                    || holder == ModEntities.MOTH
                    || holder == ModEntities.ANT
                    || holder == ModEntities.RED_ANT
                    || holder == ModEntities.RAINBOW_ANT
                    || holder == ModEntities.UNSTABLE_ANT
                    || holder == ModEntities.TERMITE
                    || holder == ModEntities.STINKBUG
                    || holder == ModEntities.BEAVER
                    || holder == ModEntities.CASSOWARY
                    || holder == ModEntities.CAVEFISHER
                    || holder == ModEntities.CHIPMUNK
                    || holder == ModEntities.CRICKET
                    || holder == ModEntities.RAT
                    || holder == ModEntities.SMALL_WORM
                    || holder == ModEntities.MEDIUM_WORM
                    || holder == ModEntities.LARGE_WORM
                    || holder == ModEntities.DOOM_WORM
                    || holder == ModEntities.GAMMAMETROID
                    || holder == ModEntities.SPYRO
                    || holder == ModEntities.ALIEN
                    || holder == ModEntities.KYUUBI
                    || holder == ModEntities.MANTIS
                    || holder == ModEntities.MOTHRA
                    || holder == ModEntities.BRUTALFLY
                    || holder == ModEntities.REDCOW
                    || holder == ModEntities.GHOST
                    || holder == ModEntities.GHOST_SKELLY
                    || holder == ModEntities.CREEPING_HORROR
                    || holder == ModEntities.TERRIBLE_TERROR
                    || holder == ModEntities.FROG
                    || holder == ModEntities.BEE
                    || holder == ModEntities.RUBBER_DUCKY
                    || holder == ModEntities.FLOUNDER
                    || holder == ModEntities.GOLD_FISH
                    || holder == ModEntities.CLOUD_SHARK
                    || holder == ModEntities.LURKING_TERROR
                    || holder == ModEntities.PEACOCK
                    || holder == ModEntities.CLIFF_RACER
                    || holder == ModEntities.LEAF_MONSTER
                    || holder == ModEntities.SCORPION
                    || holder == ModEntities.ATTACK_SQUID
                    || holder == ModEntities.IRUKANDJI
                    || holder == ModEntities.SKATE
                    || holder == ModEntities.ENDER_KNIGHT
                    || holder == ModEntities.ENDER_REAPER
                    || holder == ModEntities.HYDROLISC
                    || holder == ModEntities.BASILISK
                    || holder == ModEntities.GAZELLE
                    || holder == ModEntities.EASTER_BUNNY
                    || holder == ModEntities.BANDP
                    || holder == ModEntities.HAMMERHEAD
                    || holder == ModEntities.WHALE
                    || holder == ModEntities.URCHIN
                    || holder == ModEntities.CRAB
                    || holder == ModEntities.ROBOT1
                    || holder == ModEntities.ROBOT2
                    || holder == ModEntities.MOLENOID
                    || holder == ModEntities.VORTEX
                    || holder == ModEntities.OSTRICH
                    || holder == ModEntities.LIZARD
                    || holder == ModEntities.STINKY
                    || holder == ModEntities.COCKATEIL
                    || holder == ModEntities.SEA_MONSTER
                    || holder == ModEntities.SEA_VIPER
                    || holder == ModEntities.WATER_DRAGON
                    || holder == ModEntities.CATERKILLER
                    || holder == ModEntities.EMPEROR_SCORPION
                    || holder == ModEntities.HERCULES_BEETLE
                    || holder == ModEntities.PITCH_BLACK
                    || holder == ModEntities.ROBOT3
                    || holder == ModEntities.ROBOT4
                    || holder == ModEntities.ROBOT5
                    || holder == ModEntities.ANT_ROBOT
                    || holder == ModEntities.TROOPER_BUG
                    || holder == ModEntities.SPIT_BUG
                    || holder == ModEntities.DUNGEON_BEAST
                    || holder == ModEntities.CEPHADROME
                    || holder == ModEntities.TRIFFID
                    || holder == ModEntities.SPIDER_ROBOT
                    || holder == ModEntities.GIANT_ROBOT
                    || holder == ModEntities.THE_PRINCE
                    || holder == ModEntities.THE_PRINCESS
                    || holder == ModEntities.THE_PRINCE_TEEN
                    || holder == ModEntities.THE_PRINCE_ADULT
                    || holder == ModEntities.LEON
                    || holder == ModEntities.DRAGON
                    || holder == ModEntities.THE_KING
                    || holder == ModEntities.KING_HEAD
                    || holder == ModEntities.THE_QUEEN
                    || holder == ModEntities.GODZILLA
                    || holder == ModEntities.GODZILLA_HEAD
                    || holder == ModEntities.KRAKEN
                    || holder == ModEntities.GIRLFRIEND
                    || holder == ModEntities.BOYFRIEND
                    || holder == ModEntities.SPIDER_DRIVER
                    || holder == ModEntities.QUEEN_HEAD
                    || holder == ModEntities.THROWN_CRITTER_CAGE
                    || holder == ModEntities.BETTER_FIREBALL
                    || holder == ModEntities.BERTHA_HIT
                    || holder == ModEntities.THROWN_ROCK
                    || holder == ModEntities.ULTIMATE_ARROW
                    || holder == ModEntities.THUNDER_BOLT
                    || holder == ModEntities.LASER_BALL
                    || holder == ModEntities.WATER_BALL
                    || holder == ModEntities.ICE_BALL
                    || holder == ModEntities.ISLAND
                    || holder == ModEntities.ISLAND_TOO
                    || holder == ModEntities.ELEVATOR
                    || holder == ModEntities.ROTATOR
                    || holder == ModEntities.COIN
                    || holder == ModEntities.TSHIRT
                    || holder == ModEntities.PURPLE_POWER
                    || holder == ModEntities.ROCK_BASE
                    || holder == ModEntities.SHOES
                    || holder == ModEntities.CRYSTAL_COW
                    || holder == ModEntities.GOLD_COW
                    || holder == ModEntities.ENCHANTED_COW
                    || holder == ModEntities.RUBY_BIRD
                    || holder == ModEntities.INK_SACK
                    || holder == ModEntities.IRUKANDJI_ARROW
                    || holder == ModEntities.SUNSPOT_URCHIN
                    || holder == ModEntities.DEAD_IRUKANDJI
                    || holder == ModEntities.ULTIMATE_FISH_HOOK) {
                continue;
            }
            // LivingEntity dummies only
            @SuppressWarnings("unchecked")
            EntityType<? extends LivingEntity> type = (EntityType<? extends LivingEntity>) (Object) holder.get();
            event.put(type, OreSpawnDummyMob.createAttributes().build());
        }
    }
}
