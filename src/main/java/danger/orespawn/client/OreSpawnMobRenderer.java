package danger.orespawn.client;

import java.util.HashMap;
import java.util.Map;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

/**
 * Placeholder humanoid model with <b>gold entity textures</b> mapped by registry id.
 * Full ModelTRex-style geometry is still TODO (gold entity/model/*).
 */
@OnlyIn(Dist.CLIENT)
public class OreSpawnMobRenderer<T extends Mob> extends MobRenderer<T, HumanoidModel<T>> {
    private static final ResourceLocation FALLBACK =
            ResourceLocation.withDefaultNamespace("textures/entity/zombie/zombie.png");

    /** Gold texture file under assets/orespawn/textures/entity/ */
    private static final Map<String, ResourceLocation> TEXTURES = new HashMap<>();

    static {
        map("alosaurus", "alosaurus.png");
        map("trex", "trex.png");
        map("baryonyx", "baryonyx.png");
        map("camarasaurus", "camarasaurus.png");
        map("pointysaurus", "pointysaurus.png");
        map("cryolophosaurus", "cryolophosaurus.png");
        map("red_ant", "red_ant.png");
        map("cavefisher", "cavefisher.png");
        map("butterfly", "butterfly.png");
        map("bird", "bird1.png");
        map("gammametroid", "gammametroid.png");
        map("spyro", "spyrotexture.png");
        map("dragonfly", "dragonfly.png");
        map("firefly", "firefly.png");
        map("mosquito", "mosquito.png");
        map("nastysaurus", "nastysaurus.png");
        map("alien", "alien.png");
        map("velocityraptor", "velocityraptor.png");
        map("small_worm", "wormsmalltexture.png");
        map("medium_worm", "wormmediumtexture.png");
        map("large_worm", "wormlargetexture.png");
        map("doom_worm", "wormdoomtexture.png");
        map("moth", "darkmoth.png");
        map("kyuubi", "kyuubi.png");
        map("mantis", "mantis.png");
        map("mothra", "eyemoth.png");
        map("brutalfly", "brutalfly.png");
        map("beaver", "beaver.png");
        map("termite", "termite.png");
        map("cassowary", "cassowary.png");
        map("redcow", "red_cow.png");
        map("stinkbug", "stinkbug.png");
    }

    private static void map(String entityId, String file) {
        TEXTURES.put(entityId, ResourceLocation.fromNamespaceAndPath("orespawn", "textures/entity/" + file));
    }

    public OreSpawnMobRenderer(EntityRendererProvider.Context context) {
        this(context, 0.5F);
    }

    public OreSpawnMobRenderer(EntityRendererProvider.Context context, float shadowRadius) {
        super(context, new HumanoidModel<>(context.bakeLayer(ModelLayers.ZOMBIE)), shadowRadius);
    }

    @Override
    public ResourceLocation getTextureLocation(T entity) {
        EntityType<?> type = entity.getType();
        ResourceLocation key = EntityType.getKey(type);
        if (key != null && "orespawn".equals(key.getNamespace())) {
            ResourceLocation tex = TEXTURES.get(key.getPath());
            if (tex != null) {
                return tex;
            }
        }
        return FALLBACK;
    }
}
