package danger.orespawn.client.model;

import danger.orespawn.util.Reference;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

/**
 * Layer definitions for ported gold models.
 */
@EventBusSubscriber(modid = Reference.MOD_ID, value = Dist.CLIENT)
public final class ModModelLayers {
    private ModModelLayers() {}

    public static final ModelLayerLocation TREX = layer("trex");
    public static final ModelLayerLocation ALOSAURUS = layer("alosaurus");
    public static final ModelLayerLocation ALIEN = layer("alien");
    public static final ModelLayerLocation WORM_SMALL = layer("worm_small");
    public static final ModelLayerLocation WORM_MEDIUM = layer("worm_medium");
    public static final ModelLayerLocation WORM_LARGE = layer("worm_large");
    public static final ModelLayerLocation WORM_DOOM = layer("worm_doom");
    public static final ModelLayerLocation BIRD = layer("bird");
    public static final ModelLayerLocation BUTTERFLY = layer("butterfly");

    public static final ModelLayerLocation BARYONYX = layer("baryonyx");
    public static final ModelLayerLocation CAMARASAURUS = layer("camarasaurus");
    public static final ModelLayerLocation POINTYSAURUS = layer("pointysaurus");
    public static final ModelLayerLocation CRYOLOPHOSAURUS = layer("cryolophosaurus");
    public static final ModelLayerLocation NASTYSAURUS = layer("nastysaurus");
    public static final ModelLayerLocation VELOCITYRAPTOR = layer("velocityraptor");
    public static final ModelLayerLocation SPYRO = layer("spyro");
    public static final ModelLayerLocation BRUTALFLY = layer("brutalfly");
    public static final ModelLayerLocation DRAGONFLY = layer("dragonfly");
    public static final ModelLayerLocation BEAVER = layer("beaver");
    public static final ModelLayerLocation CASSOWARY = layer("cassowary");
    public static final ModelLayerLocation STINKBUG = layer("stinkbug");
    public static final ModelLayerLocation CHIPMUNK = layer("chipmunk");
    public static final ModelLayerLocation CRICKET = layer("cricket");
    public static final ModelLayerLocation ANT = layer("ant");
    public static final ModelLayerLocation CAVEFISHER = layer("cavefisher");
    public static final ModelLayerLocation GAMMAMETROID = layer("gammametroid");
    public static final ModelLayerLocation KYUUBI = layer("kyuubi");
    public static final ModelLayerLocation MANTIS = layer("mantis");
    public static final ModelLayerLocation MOSQUITO = layer("mosquito");
    public static final ModelLayerLocation FIREFLY = layer("firefly");
    public static final ModelLayerLocation FAIRY = layer("fairy");
    public static final ModelLayerLocation MOTHRA = layer("mothra");
    public static final ModelLayerLocation MOTH = layer("moth");
    public static final ModelLayerLocation RAT = layer("rat");
    /** 1.7.10 ModelBertha — item in-hand only */
    public static final ModelLayerLocation BERTHA = layer("bertha");

    // classic wave 2026-07-15
    public static final ModelLayerLocation GHOST = layer("ghost");
    public static final ModelLayerLocation GHOST_SKELLY = layer("ghost_skelly");
    public static final ModelLayerLocation CREEPING_HORROR = layer("creeping_horror");
    public static final ModelLayerLocation TERRIBLE_TERROR = layer("terrible_terror");
    public static final ModelLayerLocation FROG = layer("frog");
    public static final ModelLayerLocation BEE = layer("bee");
    public static final ModelLayerLocation RUBBER_DUCKY = layer("rubber_ducky");
    public static final ModelLayerLocation FLOUNDER = layer("flounder");
    public static final ModelLayerLocation GOLD_FISH = layer("gold_fish");
    public static final ModelLayerLocation CLOUD_SHARK = layer("cloud_shark");

    // classic wave B
    public static final ModelLayerLocation LURKING_TERROR = layer("lurking_terror");
    public static final ModelLayerLocation PEACOCK = layer("peacock");
    public static final ModelLayerLocation CLIFF_RACER = layer("cliff_racer");
    public static final ModelLayerLocation LEAF_MONSTER = layer("leaf_monster");
    public static final ModelLayerLocation SCORPION = layer("scorpion");
    public static final ModelLayerLocation ATTACK_SQUID = layer("attack_squid");
    public static final ModelLayerLocation IRUKANDJI = layer("irukandji");
    public static final ModelLayerLocation SKATE = layer("skate");
    public static final ModelLayerLocation ENDER_KNIGHT = layer("ender_knight");
    public static final ModelLayerLocation ENDER_REAPER = layer("ender_reaper");

    private static ModelLayerLocation layer(String name) {
        return new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, name), "main");
    }

    @SubscribeEvent
    public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(TREX, ModelTRex::createBodyLayer);
        event.registerLayerDefinition(ALOSAURUS, ModelAlosaurus::createBodyLayer);
        event.registerLayerDefinition(ALIEN, ModelAlien::createBodyLayer);
        event.registerLayerDefinition(WORM_SMALL, ModelWormSmall::createBodyLayer);
        event.registerLayerDefinition(WORM_MEDIUM, ModelWormMedium::createBodyLayer);
        event.registerLayerDefinition(WORM_LARGE, ModelWormLarge::createBodyLayer);
        event.registerLayerDefinition(WORM_DOOM, ModelWormDoom::createBodyLayer);
        event.registerLayerDefinition(BIRD, ModelBird::createBodyLayer);
        event.registerLayerDefinition(BUTTERFLY, ModelButterfly::createBodyLayer);

        event.registerLayerDefinition(BARYONYX, ModelBaryonyx::createBodyLayer);
        event.registerLayerDefinition(CAMARASAURUS, ModelCamarasaurus::createBodyLayer);
        event.registerLayerDefinition(POINTYSAURUS, ModelPointysaurus::createBodyLayer);
        event.registerLayerDefinition(CRYOLOPHOSAURUS, ModelCryolophosaurus::createBodyLayer);
        event.registerLayerDefinition(NASTYSAURUS, ModelNastysaurus::createBodyLayer);
        event.registerLayerDefinition(VELOCITYRAPTOR, ModelVelocityRaptor::createBodyLayer);
        event.registerLayerDefinition(SPYRO, ModelSpyro::createBodyLayer);
        event.registerLayerDefinition(BRUTALFLY, ModelBrutalfly::createBodyLayer);
        event.registerLayerDefinition(DRAGONFLY, ModelDragonfly::createBodyLayer);
        event.registerLayerDefinition(BEAVER, ModelBeaver::createBodyLayer);
        event.registerLayerDefinition(CASSOWARY, ModelCassowary::createBodyLayer);
        event.registerLayerDefinition(STINKBUG, ModelStinkBug::createBodyLayer);
        event.registerLayerDefinition(CHIPMUNK, ModelChipmunk::createBodyLayer);
        event.registerLayerDefinition(CRICKET, ModelCricket::createBodyLayer);
        event.registerLayerDefinition(ANT, ModelAnt::createBodyLayer);
        event.registerLayerDefinition(CAVEFISHER, ModelCaveFisher::createBodyLayer);
        event.registerLayerDefinition(GAMMAMETROID, ModelGammaMetroid::createBodyLayer);
        event.registerLayerDefinition(KYUUBI, ModelKyuubi::createBodyLayer);
        event.registerLayerDefinition(MANTIS, ModelMantis::createBodyLayer);
        event.registerLayerDefinition(MOSQUITO, ModelMosquito::createBodyLayer);
        event.registerLayerDefinition(FIREFLY, ModelFirefly::createBodyLayer);
        event.registerLayerDefinition(FAIRY, ModelFairy::createBodyLayer);
        event.registerLayerDefinition(MOTHRA, ModelMothra::createBodyLayer);
        event.registerLayerDefinition(MOTH, ModelMoth::createBodyLayer);
        event.registerLayerDefinition(RAT, ModelRat::createBodyLayer);
        event.registerLayerDefinition(BERTHA, ModelBertha::createBodyLayer);

        event.registerLayerDefinition(GHOST, ModelGhost::createBodyLayer);
        event.registerLayerDefinition(GHOST_SKELLY, ModelGhostSkelly::createBodyLayer);
        event.registerLayerDefinition(CREEPING_HORROR, ModelCreepingHorror::createBodyLayer);
        event.registerLayerDefinition(TERRIBLE_TERROR, ModelTerribleTerror::createBodyLayer);
        // Critter models use their own LAYER_LOCATION constants (same resource ids)
        event.registerLayerDefinition(ModelFrog.LAYER_LOCATION, ModelFrog::createBodyLayer);
        event.registerLayerDefinition(ModelBee.LAYER_LOCATION, ModelBee::createBodyLayer);
        event.registerLayerDefinition(ModelRubberDucky.LAYER_LOCATION, ModelRubberDucky::createBodyLayer);
        event.registerLayerDefinition(FLOUNDER, ModelFlounder::createBodyLayer);
        event.registerLayerDefinition(GOLD_FISH, ModelGoldFish::createBodyLayer);
        event.registerLayerDefinition(CLOUD_SHARK, ModelCloudShark::createBodyLayer);

        // wave B — prefer Model*.LAYER_LOCATION when renderers bake that id
        event.registerLayerDefinition(LURKING_TERROR, ModelLurkingTerror::createBodyLayer);
        event.registerLayerDefinition(ModelPeacock.LAYER_LOCATION, ModelPeacock::createBodyLayer);
        event.registerLayerDefinition(ModelCliffRacer.LAYER_LOCATION, ModelCliffRacer::createBodyLayer);
        event.registerLayerDefinition(ModelLeafMonster.LAYER_LOCATION, ModelLeafMonster::createBodyLayer);
        event.registerLayerDefinition(ModelScorpion.LAYER_LOCATION, ModelScorpion::createBodyLayer);
        event.registerLayerDefinition(ModelAttackSquid.LAYER_LOCATION, ModelAttackSquid::createBodyLayer);
        event.registerLayerDefinition(ModelIrukandji.LAYER_LOCATION, ModelIrukandji::createBodyLayer);
        event.registerLayerDefinition(ModelSkate.LAYER_LOCATION, ModelSkate::createBodyLayer);
        event.registerLayerDefinition(ModelEnderKnight.LAYER_LOCATION, ModelEnderKnight::createBodyLayer);
        event.registerLayerDefinition(ModelEnderReaper.LAYER_LOCATION, ModelEnderReaper::createBodyLayer);

        // wave C
        event.registerLayerDefinition(ModelHydrolisc.LAYER_LOCATION, ModelHydrolisc::createBodyLayer);
        event.registerLayerDefinition(ModelBasilisk.LAYER_LOCATION, ModelBasilisk::createBodyLayer);
        event.registerLayerDefinition(ModelGazelle.LAYER_LOCATION, ModelGazelle::createBodyLayer);
        event.registerLayerDefinition(ModelEasterBunny.LAYER_LOCATION, ModelEasterBunny::createBodyLayer);
        event.registerLayerDefinition(ModelBandP.LAYER_LOCATION, ModelBandP::createBodyLayer);
        event.registerLayerDefinition(ModelHammerhead.LAYER_LOCATION, ModelHammerhead::createBodyLayer);
        event.registerLayerDefinition(ModelWhale.LAYER_LOCATION, ModelWhale::createBodyLayer);
        event.registerLayerDefinition(ModelUrchin.LAYER_LOCATION, ModelUrchin::createBodyLayer);
        event.registerLayerDefinition(ModelCrab.LAYER_LOCATION, ModelCrab::createBodyLayer);
        event.registerLayerDefinition(ModelRobot1.LAYER_LOCATION, ModelRobot1::createBodyLayer);
        event.registerLayerDefinition(ModelRobot2.LAYER_LOCATION, ModelRobot2::createBodyLayer);
        event.registerLayerDefinition(ModelMolenoid.LAYER_LOCATION, ModelMolenoid::createBodyLayer);
        event.registerLayerDefinition(ModelVortex.LAYER_LOCATION, ModelVortex::createBodyLayer);

        // SET1
        event.registerLayerDefinition(ModelOstrich.LAYER_LOCATION, ModelOstrich::createBodyLayer);
        event.registerLayerDefinition(ModelLizard.LAYER_LOCATION, ModelLizard::createBodyLayer);
        event.registerLayerDefinition(ModelStinky.LAYER_LOCATION, ModelStinky::createBodyLayer);
        event.registerLayerDefinition(ModelCockateil.LAYER_LOCATION, ModelCockateil::createBodyLayer);
        event.registerLayerDefinition(ModelSeaMonster.LAYER_LOCATION, ModelSeaMonster::createBodyLayer);
        event.registerLayerDefinition(ModelSeaViper.LAYER_LOCATION, ModelSeaViper::createBodyLayer);
        event.registerLayerDefinition(ModelWaterDragon.LAYER_LOCATION, ModelWaterDragon::createBodyLayer);

        // SET2 big bugs
        event.registerLayerDefinition(ModelCaterKiller.LAYER_LOCATION, ModelCaterKiller::createBodyLayer);
        event.registerLayerDefinition(ModelEmperorScorpion.LAYER_LOCATION, ModelEmperorScorpion::createBodyLayer);
        event.registerLayerDefinition(ModelHerculesBeetle.LAYER_LOCATION, ModelHerculesBeetle::createBodyLayer);
        event.registerLayerDefinition(ModelPitchBlack.LAYER_LOCATION, ModelPitchBlack::createBodyLayer);

        // SET3 robots
        event.registerLayerDefinition(ModelRobot3.LAYER_LOCATION, ModelRobot3::createBodyLayer);
        event.registerLayerDefinition(ModelRobot4.LAYER_LOCATION, ModelRobot4::createBodyLayer);
        event.registerLayerDefinition(ModelRobot5.LAYER_LOCATION, ModelRobot5::createBodyLayer);
        event.registerLayerDefinition(ModelAntRobot.LAYER_LOCATION, ModelAntRobot::createBodyLayer);

        // SET4
        event.registerLayerDefinition(ModelTrooperBug.LAYER_LOCATION, ModelTrooperBug::createBodyLayer);
        event.registerLayerDefinition(ModelSpitBug.LAYER_LOCATION, ModelSpitBug::createBodyLayer);
        event.registerLayerDefinition(ModelDungeonBeast.LAYER_LOCATION, ModelDungeonBeast::createBodyLayer);

        // SET5
        event.registerLayerDefinition(ModelCephadrome.LAYER_LOCATION, ModelCephadrome::createBodyLayer);
        event.registerLayerDefinition(ModelTriffid.LAYER_LOCATION, ModelTriffid::createBodyLayer);
        event.registerLayerDefinition(ModelSpiderRobot.LAYER_LOCATION, ModelSpiderRobot::createBodyLayer);
        event.registerLayerDefinition(ModelGiantRobot.LAYER_LOCATION, ModelGiantRobot::createBodyLayer);

        // SET6
        event.registerLayerDefinition(ModelThePrince.LAYER_LOCATION, ModelThePrince::createBodyLayer);
        event.registerLayerDefinition(ModelThePrincess.LAYER_LOCATION, ModelThePrincess::createBodyLayer);
        event.registerLayerDefinition(ModelThePrinceTeen.LAYER_LOCATION, ModelThePrinceTeen::createBodyLayer);
        event.registerLayerDefinition(ModelThePrinceAdult.LAYER_LOCATION, ModelThePrinceAdult::createBodyLayer);
        event.registerLayerDefinition(ModelLeon.LAYER_LOCATION, ModelLeon::createBodyLayer);

        // SET7 bosses — full gold anims, 1.21 HierarchicalModel
        event.registerLayerDefinition(ModelDragon.LAYER_LOCATION, ModelDragon::createBodyLayer);
        event.registerLayerDefinition(ModelTheKing.LAYER_LOCATION, ModelTheKing::createBodyLayer);
        event.registerLayerDefinition(ModelTheQueen.LAYER_LOCATION, ModelTheQueen::createBodyLayer);
        event.registerLayerDefinition(ModelGodzilla.LAYER_LOCATION, ModelGodzilla::createBodyLayer);
        event.registerLayerDefinition(ModelKraken.LAYER_LOCATION, ModelKraken::createBodyLayer);

        // SET9 scenery / vehicles
        event.registerLayerDefinition(ModelIsland.LAYER_LOCATION, ModelIsland::createBodyLayer);
        event.registerLayerDefinition(ModelElevator.LAYER_LOCATION, ModelElevator::createBodyLayer);
        event.registerLayerDefinition(ModelRotator.LAYER_LOCATION, ModelRotator::createBodyLayer);
        event.registerLayerDefinition(ModelCoin.LAYER_LOCATION, ModelCoin::createBodyLayer);
        event.registerLayerDefinition(ModelTshirt.LAYER_LOCATION, ModelTshirt::createBodyLayer);
        event.registerLayerDefinition(ModelPurplePower.LAYER_LOCATION, ModelPurplePower::createBodyLayer);
        event.registerLayerDefinition(ModelRockBase.LAYER_LOCATION, ModelRockBase::createBodyLayer);
    }
}
