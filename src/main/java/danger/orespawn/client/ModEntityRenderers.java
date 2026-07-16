package danger.orespawn.client;

import danger.orespawn.client.render.AlienRenderer;
import danger.orespawn.client.render.AlosaurusRenderer;
import danger.orespawn.client.render.AntRenderer;
import danger.orespawn.client.render.AntRobotRenderer;
import danger.orespawn.client.render.AttackSquidRenderer;
import danger.orespawn.client.render.BandPRenderer;
import danger.orespawn.client.render.BaryonyxRenderer;
import danger.orespawn.client.render.BasiliskRenderer;
import danger.orespawn.client.render.BeaverRenderer;
import danger.orespawn.client.render.BeeRenderer;
import danger.orespawn.client.render.BirdRenderer;
import danger.orespawn.client.render.BrutalflyRenderer;
import danger.orespawn.client.render.ButterflyRenderer;
import danger.orespawn.client.render.CamarasaurusRenderer;
import danger.orespawn.client.render.CassowaryRenderer;
import danger.orespawn.client.render.CaterKillerRenderer;
import danger.orespawn.client.render.CaveFisherRenderer;
import danger.orespawn.client.render.CephadromeRenderer;
import danger.orespawn.client.render.ChipmunkRenderer;
import danger.orespawn.client.render.CliffRacerRenderer;
import danger.orespawn.client.render.CloudSharkRenderer;
import danger.orespawn.client.render.CockateilRenderer;
import danger.orespawn.client.render.CrabRenderer;
import danger.orespawn.client.render.CreepingHorrorRenderer;
import danger.orespawn.client.render.CricketRenderer;
import danger.orespawn.client.render.CryolophosaurusRenderer;
import danger.orespawn.client.render.DragonflyRenderer;
import danger.orespawn.client.render.DragonRenderer;
import danger.orespawn.client.render.DungeonBeastRenderer;
import danger.orespawn.client.render.EasterBunnyRenderer;
import danger.orespawn.client.render.ElevatorRenderer;
import danger.orespawn.client.render.EmperorScorpionRenderer;
import danger.orespawn.client.render.EnchantedCowRenderer;
import danger.orespawn.client.render.EnderKnightRenderer;
import danger.orespawn.client.render.EnderReaperRenderer;
import danger.orespawn.client.render.FairyRenderer;
import danger.orespawn.client.render.FireflyRenderer;
import danger.orespawn.client.render.FlounderRenderer;
import danger.orespawn.client.render.FrogRenderer;
import danger.orespawn.client.render.GammaMetroidRenderer;
import danger.orespawn.client.render.GazelleRenderer;
import danger.orespawn.client.render.GhostRenderer;
import danger.orespawn.client.render.GhostSkellyRenderer;
import danger.orespawn.client.render.GiantRobotRenderer;
import danger.orespawn.client.render.GirlfriendRenderer;
import danger.orespawn.client.render.GodzillaHeadRenderer;
import danger.orespawn.client.render.GodzillaRenderer;
import danger.orespawn.client.render.GoldCowRenderer;
import danger.orespawn.client.render.GoldFishRenderer;
import danger.orespawn.client.render.BoyfriendRenderer;
import danger.orespawn.client.render.CoinRenderer;
import danger.orespawn.client.render.CrystalCowRenderer;
import danger.orespawn.client.render.HammerheadRenderer;
import danger.orespawn.client.render.HerculesBeetleRenderer;
import danger.orespawn.client.render.HydroliscRenderer;
import danger.orespawn.client.render.DeadIrukandjiRenderer;
import danger.orespawn.client.render.IceBallRenderer;
import danger.orespawn.client.render.InkSackRenderer;
import danger.orespawn.client.render.IslandRenderer;
import danger.orespawn.client.render.IslandTooRenderer;
import danger.orespawn.client.render.IrukandjiArrowRenderer;
import danger.orespawn.client.render.IrukandjiRenderer;
import danger.orespawn.client.render.LaserBallRenderer;
import danger.orespawn.client.render.SunspotUrchinRenderer;
import danger.orespawn.client.render.KingHeadRenderer;
import danger.orespawn.client.render.KrakenRenderer;
import danger.orespawn.client.render.QueenHeadRenderer;
import danger.orespawn.client.render.SpiderDriverRenderer;
import danger.orespawn.client.render.KyuubiRenderer;
import danger.orespawn.client.render.LeafMonsterRenderer;
import danger.orespawn.client.render.LeonRenderer;
import danger.orespawn.client.render.LizardRenderer;
import danger.orespawn.client.render.LurkingTerrorRenderer;
import danger.orespawn.client.render.MantisRenderer;
import danger.orespawn.client.render.MolenoidRenderer;
import danger.orespawn.client.render.MosquitoRenderer;
import danger.orespawn.client.render.MothRenderer;
import danger.orespawn.client.render.MothraRenderer;
import danger.orespawn.client.render.NastysaurusRenderer;
import danger.orespawn.client.render.OstrichRenderer;
import danger.orespawn.client.render.PeacockRenderer;
import danger.orespawn.client.render.PitchBlackRenderer;
import danger.orespawn.client.render.PointysaurusRenderer;
import danger.orespawn.client.render.PurplePowerRenderer;
import danger.orespawn.client.render.RockBaseRenderer;
import danger.orespawn.client.render.RotatorRenderer;
import danger.orespawn.client.render.RubyBirdRenderer;
import danger.orespawn.client.render.RainbowAntRenderer;
import danger.orespawn.client.render.RatRenderer;
import danger.orespawn.client.render.RedAntRenderer;
import danger.orespawn.client.render.RedCowRenderer;
import danger.orespawn.client.render.Robot1Renderer;
import danger.orespawn.client.render.Robot2Renderer;
import danger.orespawn.client.render.Robot3Renderer;
import danger.orespawn.client.render.Robot4Renderer;
import danger.orespawn.client.render.Robot5Renderer;
import danger.orespawn.client.render.RubberDuckyRenderer;
import danger.orespawn.client.render.ScorpionRenderer;
import danger.orespawn.client.render.SeaMonsterRenderer;
import danger.orespawn.client.render.ShoesRenderer;
import danger.orespawn.client.render.SeaViperRenderer;
import danger.orespawn.client.render.SkateRenderer;
import danger.orespawn.client.render.SpiderRobotRenderer;
import danger.orespawn.client.render.SpitBugRenderer;
import danger.orespawn.client.render.SpyroRenderer;
import danger.orespawn.client.render.TriffidRenderer;
import danger.orespawn.client.render.StinkBugRenderer;
import danger.orespawn.client.render.StinkyRenderer;
import danger.orespawn.client.render.TRexRenderer;
import danger.orespawn.client.render.TrooperBugRenderer;
import danger.orespawn.client.render.TshirtRenderer;
import danger.orespawn.client.render.TermiteRenderer;
import danger.orespawn.client.render.TerribleTerrorRenderer;
import danger.orespawn.client.render.ThunderBoltRenderer;
import danger.orespawn.client.render.ThrownRockRenderer;
import danger.orespawn.client.render.TheKingRenderer;
import danger.orespawn.client.render.ThePrinceAdultRenderer;
import danger.orespawn.client.render.ThePrinceRenderer;
import danger.orespawn.client.render.ThePrinceTeenRenderer;
import danger.orespawn.client.render.ThePrincessRenderer;
import danger.orespawn.client.render.TheQueenRenderer;
import danger.orespawn.client.render.UltimateArrowRenderer;
import danger.orespawn.client.render.UltimateFishHookRenderer;
import danger.orespawn.client.render.UnstableAntRenderer;
import danger.orespawn.client.render.UrchinRenderer;
import danger.orespawn.client.render.WaterBallRenderer;
import danger.orespawn.client.render.VelocityRaptorRenderer;
import danger.orespawn.client.render.VortexRenderer;
import danger.orespawn.client.render.WaterDragonRenderer;
import danger.orespawn.client.render.WhaleRenderer;
import danger.orespawn.client.render.WormDoomRenderer;
import danger.orespawn.client.render.WormLargeRenderer;
import danger.orespawn.client.render.WormMediumRenderer;
import danger.orespawn.client.render.WormSmallRenderer;
import danger.orespawn.init.ModEntities;
import danger.orespawn.util.Reference;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

/**
 * Client-only entity renderer registration (mod bus).
 * Ported gold HierarchicalModel renderers (RedCow uses cow model + red_cow texture).
 */
@EventBusSubscriber(modid = Reference.MOD_ID, value = Dist.CLIENT)
public final class ModEntityRenderers {
    private ModEntityRenderers() {}

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        // Dinos / large
        event.registerEntityRenderer(ModEntities.ALOSAURUS.get(), AlosaurusRenderer::new);
        event.registerEntityRenderer(ModEntities.TREX.get(), TRexRenderer::new);
        event.registerEntityRenderer(ModEntities.BARYONYX.get(), BaryonyxRenderer::new);
        event.registerEntityRenderer(ModEntities.CAMARASAURUS.get(), CamarasaurusRenderer::new);
        event.registerEntityRenderer(ModEntities.POINTYSAURUS.get(), PointysaurusRenderer::new);
        event.registerEntityRenderer(ModEntities.CRYOLOPHOSAURUS.get(), CryolophosaurusRenderer::new);
        event.registerEntityRenderer(ModEntities.NASTYSAURUS.get(), NastysaurusRenderer::new);
        event.registerEntityRenderer(ModEntities.VELOCITYRAPTOR.get(), VelocityRaptorRenderer::new);

        // Small / insects / ambient
        event.registerEntityRenderer(ModEntities.ANT.get(), AntRenderer::new);
        event.registerEntityRenderer(ModEntities.RED_ANT.get(), RedAntRenderer::new);
        event.registerEntityRenderer(ModEntities.RAINBOW_ANT.get(), RainbowAntRenderer::new);
        event.registerEntityRenderer(ModEntities.UNSTABLE_ANT.get(), UnstableAntRenderer::new);
        event.registerEntityRenderer(ModEntities.CAVEFISHER.get(), CaveFisherRenderer::new);
        event.registerEntityRenderer(ModEntities.BUTTERFLY.get(), ButterflyRenderer::new);
        event.registerEntityRenderer(ModEntities.BIRD.get(), BirdRenderer::new);
        event.registerEntityRenderer(ModEntities.DRAGONFLY.get(), DragonflyRenderer::new);
        event.registerEntityRenderer(ModEntities.FIREFLY.get(), FireflyRenderer::new);
        event.registerEntityRenderer(ModEntities.FAIRY.get(), FairyRenderer::new);
        event.registerEntityRenderer(ModEntities.MOSQUITO.get(), MosquitoRenderer::new);
        event.registerEntityRenderer(ModEntities.MOTH.get(), MothRenderer::new);
        event.registerEntityRenderer(ModEntities.BEAVER.get(), BeaverRenderer::new);
        event.registerEntityRenderer(ModEntities.TERMITE.get(), TermiteRenderer::new);
        event.registerEntityRenderer(ModEntities.CASSOWARY.get(), CassowaryRenderer::new);
        event.registerEntityRenderer(ModEntities.STINKBUG.get(), StinkBugRenderer::new);
        event.registerEntityRenderer(ModEntities.CHIPMUNK.get(), ChipmunkRenderer::new);
        event.registerEntityRenderer(ModEntities.CRICKET.get(), CricketRenderer::new);
        event.registerEntityRenderer(ModEntities.RAT.get(), RatRenderer::new);

        // Silhouettes / special
        event.registerEntityRenderer(ModEntities.ALIEN.get(), AlienRenderer::new);
        event.registerEntityRenderer(ModEntities.SMALL_WORM.get(), WormSmallRenderer::new);
        event.registerEntityRenderer(ModEntities.MEDIUM_WORM.get(), WormMediumRenderer::new);
        event.registerEntityRenderer(ModEntities.LARGE_WORM.get(), WormLargeRenderer::new);
        event.registerEntityRenderer(ModEntities.DOOM_WORM.get(), WormDoomRenderer::new);
        event.registerEntityRenderer(ModEntities.GAMMAMETROID.get(), GammaMetroidRenderer::new);
        event.registerEntityRenderer(ModEntities.SPYRO.get(), SpyroRenderer::new);
        event.registerEntityRenderer(ModEntities.KYUUBI.get(), KyuubiRenderer::new);
        event.registerEntityRenderer(ModEntities.MANTIS.get(), MantisRenderer::new);
        event.registerEntityRenderer(ModEntities.MOTHRA.get(), MothraRenderer::new);
        event.registerEntityRenderer(ModEntities.BRUTALFLY.get(), BrutalflyRenderer::new);

        event.registerEntityRenderer(ModEntities.REDCOW.get(), RedCowRenderer::new);

        // classic wave 2026-07-15
        event.registerEntityRenderer(ModEntities.GHOST.get(), GhostRenderer::new);
        event.registerEntityRenderer(ModEntities.GHOST_SKELLY.get(), GhostSkellyRenderer::new);
        event.registerEntityRenderer(ModEntities.CREEPING_HORROR.get(), CreepingHorrorRenderer::new);
        event.registerEntityRenderer(ModEntities.TERRIBLE_TERROR.get(), TerribleTerrorRenderer::new);
        event.registerEntityRenderer(ModEntities.FROG.get(), FrogRenderer::new);
        event.registerEntityRenderer(ModEntities.BEE.get(), BeeRenderer::new);
        event.registerEntityRenderer(ModEntities.RUBBER_DUCKY.get(), RubberDuckyRenderer::new);
        event.registerEntityRenderer(ModEntities.FLOUNDER.get(), FlounderRenderer::new);
        event.registerEntityRenderer(ModEntities.GOLD_FISH.get(), GoldFishRenderer::new);
        event.registerEntityRenderer(ModEntities.CLOUD_SHARK.get(), CloudSharkRenderer::new);

        // classic wave B (full gold model anims)
        event.registerEntityRenderer(ModEntities.LURKING_TERROR.get(), LurkingTerrorRenderer::new);
        event.registerEntityRenderer(ModEntities.PEACOCK.get(), PeacockRenderer::new);
        event.registerEntityRenderer(ModEntities.CLIFF_RACER.get(), CliffRacerRenderer::new);
        event.registerEntityRenderer(ModEntities.LEAF_MONSTER.get(), LeafMonsterRenderer::new);
        event.registerEntityRenderer(ModEntities.SCORPION.get(), ScorpionRenderer::new);
        event.registerEntityRenderer(ModEntities.ATTACK_SQUID.get(), AttackSquidRenderer::new);
        event.registerEntityRenderer(ModEntities.IRUKANDJI.get(), IrukandjiRenderer::new);
        event.registerEntityRenderer(ModEntities.SKATE.get(), SkateRenderer::new);
        event.registerEntityRenderer(ModEntities.ENDER_KNIGHT.get(), EnderKnightRenderer::new);
        event.registerEntityRenderer(ModEntities.ENDER_REAPER.get(), EnderReaperRenderer::new);

        // classic wave C (full gold model anims)
        event.registerEntityRenderer(ModEntities.HYDROLISC.get(), HydroliscRenderer::new);
        event.registerEntityRenderer(ModEntities.BASILISK.get(), BasiliskRenderer::new);
        event.registerEntityRenderer(ModEntities.GAZELLE.get(), GazelleRenderer::new);
        event.registerEntityRenderer(ModEntities.EASTER_BUNNY.get(), EasterBunnyRenderer::new);
        event.registerEntityRenderer(ModEntities.BANDP.get(), BandPRenderer::new);
        event.registerEntityRenderer(ModEntities.HAMMERHEAD.get(), HammerheadRenderer::new);
        event.registerEntityRenderer(ModEntities.WHALE.get(), WhaleRenderer::new);
        event.registerEntityRenderer(ModEntities.URCHIN.get(), UrchinRenderer::new);
        event.registerEntityRenderer(ModEntities.CRAB.get(), CrabRenderer::new);
        event.registerEntityRenderer(ModEntities.ROBOT1.get(), Robot1Renderer::new);
        event.registerEntityRenderer(ModEntities.ROBOT2.get(), Robot2Renderer::new);
        event.registerEntityRenderer(ModEntities.MOLENOID.get(), MolenoidRenderer::new);
        event.registerEntityRenderer(ModEntities.VORTEX.get(), VortexRenderer::new);

        // SET1 land/sea
        event.registerEntityRenderer(ModEntities.OSTRICH.get(), OstrichRenderer::new);
        event.registerEntityRenderer(ModEntities.LIZARD.get(), LizardRenderer::new);
        event.registerEntityRenderer(ModEntities.STINKY.get(), StinkyRenderer::new);
        event.registerEntityRenderer(ModEntities.COCKATEIL.get(), CockateilRenderer::new);
        event.registerEntityRenderer(ModEntities.SEA_MONSTER.get(), SeaMonsterRenderer::new);
        event.registerEntityRenderer(ModEntities.SEA_VIPER.get(), SeaViperRenderer::new);
        event.registerEntityRenderer(ModEntities.WATER_DRAGON.get(), WaterDragonRenderer::new);

        // SET2 big bugs
        event.registerEntityRenderer(ModEntities.CATERKILLER.get(), CaterKillerRenderer::new);
        event.registerEntityRenderer(ModEntities.EMPEROR_SCORPION.get(), EmperorScorpionRenderer::new);
        event.registerEntityRenderer(ModEntities.HERCULES_BEETLE.get(), HerculesBeetleRenderer::new);
        event.registerEntityRenderer(ModEntities.PITCH_BLACK.get(), PitchBlackRenderer::new);

        // SET3 robots
        event.registerEntityRenderer(ModEntities.ROBOT3.get(), Robot3Renderer::new);
        event.registerEntityRenderer(ModEntities.ROBOT4.get(), Robot4Renderer::new);
        event.registerEntityRenderer(ModEntities.ROBOT5.get(), Robot5Renderer::new);
        event.registerEntityRenderer(ModEntities.ANT_ROBOT.get(), AntRobotRenderer::new);

        // SET4 chaos + dungeon
        event.registerEntityRenderer(ModEntities.TROOPER_BUG.get(), TrooperBugRenderer::new);
        event.registerEntityRenderer(ModEntities.SPIT_BUG.get(), SpitBugRenderer::new);
        event.registerEntityRenderer(ModEntities.DUNGEON_BEAST.get(), DungeonBeastRenderer::new);

        // SET5 heavies
        event.registerEntityRenderer(ModEntities.CEPHADROME.get(), CephadromeRenderer::new);
        event.registerEntityRenderer(ModEntities.TRIFFID.get(), TriffidRenderer::new);
        event.registerEntityRenderer(ModEntities.SPIDER_ROBOT.get(), SpiderRobotRenderer::new);
        event.registerEntityRenderer(ModEntities.GIANT_ROBOT.get(), GiantRobotRenderer::new);

        // SET6 prince tree + Leon
        event.registerEntityRenderer(ModEntities.THE_PRINCE.get(), ThePrinceRenderer::new);
        event.registerEntityRenderer(ModEntities.THE_PRINCESS.get(), ThePrincessRenderer::new);
        event.registerEntityRenderer(ModEntities.THE_PRINCE_TEEN.get(), ThePrinceTeenRenderer::new);
        event.registerEntityRenderer(ModEntities.THE_PRINCE_ADULT.get(), ThePrinceAdultRenderer::new);
        event.registerEntityRenderer(ModEntities.LEON.get(), LeonRenderer::new);

        // SET7 Dragon + mega bosses (gold 1:1 on 1.21.1)
        event.registerEntityRenderer(ModEntities.DRAGON.get(), DragonRenderer::new);
        event.registerEntityRenderer(ModEntities.THE_KING.get(), TheKingRenderer::new);
        event.registerEntityRenderer(ModEntities.KING_HEAD.get(), KingHeadRenderer::new);
        event.registerEntityRenderer(ModEntities.THE_QUEEN.get(), TheQueenRenderer::new);
        event.registerEntityRenderer(ModEntities.GODZILLA.get(), GodzillaRenderer::new);
        event.registerEntityRenderer(ModEntities.GODZILLA_HEAD.get(), GodzillaHeadRenderer::new);
        event.registerEntityRenderer(ModEntities.KRAKEN.get(), KrakenRenderer::new);

        // SET8 social / companions
        event.registerEntityRenderer(ModEntities.GIRLFRIEND.get(), GirlfriendRenderer::new);
        event.registerEntityRenderer(ModEntities.BOYFRIEND.get(), BoyfriendRenderer::new);
        event.registerEntityRenderer(ModEntities.SPIDER_DRIVER.get(), SpiderDriverRenderer::new);
        event.registerEntityRenderer(ModEntities.QUEEN_HEAD.get(), QueenHeadRenderer::new);

        // Projectiles
        event.registerEntityRenderer(ModEntities.THROWN_CRITTER_CAGE.get(), OreSpawnProjectileRenderers.CageRenderer::new);
        event.registerEntityRenderer(ModEntities.BETTER_FIREBALL.get(), OreSpawnProjectileRenderers.FireballRenderer::new);
        event.registerEntityRenderer(ModEntities.BERTHA_HIT.get(), OreSpawnProjectileRenderers.BerthaHitRenderer::new);
        event.registerEntityRenderer(ModEntities.THROWN_ROCK.get(), ThrownRockRenderer::new);
        event.registerEntityRenderer(ModEntities.LASER_BALL.get(), LaserBallRenderer::new);
        event.registerEntityRenderer(ModEntities.WATER_BALL.get(), WaterBallRenderer::new);
        event.registerEntityRenderer(ModEntities.ICE_BALL.get(), IceBallRenderer::new);
        event.registerEntityRenderer(ModEntities.THUNDER_BOLT.get(), ThunderBoltRenderer::new);
        event.registerEntityRenderer(ModEntities.ULTIMATE_ARROW.get(), UltimateArrowRenderer::new);
        event.registerEntityRenderer(ModEntities.INK_SACK.get(), InkSackRenderer::new);
        event.registerEntityRenderer(ModEntities.IRUKANDJI_ARROW.get(), IrukandjiArrowRenderer::new);
        event.registerEntityRenderer(ModEntities.SUNSPOT_URCHIN.get(), SunspotUrchinRenderer::new);
        event.registerEntityRenderer(ModEntities.DEAD_IRUKANDJI.get(), DeadIrukandjiRenderer::new);
        event.registerEntityRenderer(ModEntities.ULTIMATE_FISH_HOOK.get(), UltimateFishHookRenderer::new);

        // SET9 scenery / vehicles / cows / birds
        event.registerEntityRenderer(ModEntities.ISLAND.get(), IslandRenderer::new);
        event.registerEntityRenderer(ModEntities.ISLAND_TOO.get(), IslandTooRenderer::new);
        event.registerEntityRenderer(ModEntities.ELEVATOR.get(), ElevatorRenderer::new);
        event.registerEntityRenderer(ModEntities.ROTATOR.get(), RotatorRenderer::new);
        event.registerEntityRenderer(ModEntities.COIN.get(), CoinRenderer::new);
        event.registerEntityRenderer(ModEntities.TSHIRT.get(), TshirtRenderer::new);
        event.registerEntityRenderer(ModEntities.PURPLE_POWER.get(), PurplePowerRenderer::new);
        event.registerEntityRenderer(ModEntities.ROCK_BASE.get(), RockBaseRenderer::new);
        event.registerEntityRenderer(ModEntities.SHOES.get(), ShoesRenderer::new);
        event.registerEntityRenderer(ModEntities.CRYSTAL_COW.get(), CrystalCowRenderer::new);
        event.registerEntityRenderer(ModEntities.GOLD_COW.get(), GoldCowRenderer::new);
        event.registerEntityRenderer(ModEntities.ENCHANTED_COW.get(), EnchantedCowRenderer::new);
        event.registerEntityRenderer(ModEntities.RUBY_BIRD.get(), RubyBirdRenderer::new);
    }
}
