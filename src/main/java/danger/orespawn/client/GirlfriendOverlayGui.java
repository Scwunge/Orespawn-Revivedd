package danger.orespawn.client;

import danger.orespawn.entity.Alien;
import danger.orespawn.entity.Alosaurus;
import danger.orespawn.entity.Boyfriend;
import danger.orespawn.entity.EmperorScorpion;
import danger.orespawn.entity.Girlfriend;
import danger.orespawn.entity.Godzilla;
import danger.orespawn.entity.Kraken;
import danger.orespawn.entity.Kyuubi;
import danger.orespawn.entity.Mantis;
import danger.orespawn.entity.Mothra;
import danger.orespawn.entity.Nastysaurus;
import danger.orespawn.entity.Spyro;
import danger.orespawn.entity.TRex;
import danger.orespawn.entity.TheKing;
import danger.orespawn.entity.TheQueen;
import danger.orespawn.entity.WormLarge;
import danger.orespawn.util.Reference;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderGuiLayerEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;

/**
 * Gold {@code GirlfriendOverlayGui} — name + health bar when crosshair targets
 * selected OreSpawn bosses / pets. Texture: {@code textures/gui/girlfriendgui.png}.
 * <p>
 * SET19 expands gold list with King/Queen/Godzilla/Kraken/Emperor + GF/BF custom names.
 */
@EventBusSubscriber(modid = Reference.MOD_ID, value = Dist.CLIENT)
public final class GirlfriendOverlayGui {
    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "textures/gui/girlfriendgui.png");

    private static final int NAME_COLOR = 16725044;
    private static final int BAR_WIDTH = 182;
    private static final int BAR_HEIGHT = 5;
    private static final int TEX_SIZE = 256;

    private GirlfriendOverlayGui() {}

    @SubscribeEvent
    public static void onRenderGuiLayer(RenderGuiLayerEvent.Post event) {
        if (!VanillaGuiLayers.HOTBAR.equals(event.getName())) {
            return;
        }

        Minecraft mc = Minecraft.getInstance();
        if (mc.options.hideGui || mc.screen != null) {
            return;
        }

        LocalPlayer player = mc.player;
        if (player == null) {
            return;
        }

        Entity entity = mc.crosshairPickEntity;
        if (entity == null) {
            return;
        }

        String outstring = null;
        float gfHealth = 0.0F;

        if (entity instanceof Mothra living) {
            outstring = "Mothra!";
            gfHealth = healthRatio(living);
        }
        if (entity instanceof Spyro living) {
            outstring = namedOr(living, "Baby Dragon");
            gfHealth = healthRatio(living);
        }
        if (entity instanceof WormLarge living) {
            if (!living.noPhysics) {
                outstring = "Worm";
                gfHealth = healthRatio(living);
            }
        }
        if (entity instanceof Alien living) {
            outstring = "Alien!";
            gfHealth = healthRatio(living);
        }
        if (entity instanceof Alosaurus living) {
            outstring = "Alosaurus";
            gfHealth = healthRatio(living);
        }
        if (entity instanceof Nastysaurus living) {
            outstring = "Nastysaurus";
            gfHealth = healthRatio(living);
        }
        if (entity instanceof TRex living) {
            outstring = "T. Rex";
            gfHealth = healthRatio(living);
        }
        if (entity instanceof Kyuubi living) {
            outstring = "Kyuubi";
            gfHealth = healthRatio(living);
        }
        if (entity instanceof Mantis living) {
            outstring = "Mantis";
            gfHealth = healthRatio(living);
        }
        // SET19 boss / companion expansions
        if (entity instanceof Godzilla living) {
            outstring = "Mobzilla";
            gfHealth = healthRatio(living);
        }
        if (entity instanceof TheKing living) {
            outstring = "The King";
            gfHealth = healthRatio(living);
        }
        if (entity instanceof TheQueen living) {
            outstring = "The Queen";
            gfHealth = healthRatio(living);
        }
        if (entity instanceof EmperorScorpion living) {
            outstring = "Emperor Scorpion";
            gfHealth = healthRatio(living);
        }
        if (entity instanceof Kraken living) {
            outstring = "Kraken";
            gfHealth = healthRatio(living);
        }
        if (entity instanceof Girlfriend living) {
            outstring = namedOr(living, "Girlfriend");
            gfHealth = healthRatio(living);
        }
        if (entity instanceof Boyfriend living) {
            outstring = namedOr(living, "Boyfriend");
            gfHealth = healthRatio(living);
        }

        if (outstring == null) {
            return;
        }

        GuiGraphics guiGraphics = event.getGuiGraphics();
        Font font = mc.font;
        int width = guiGraphics.guiWidth();
        int barWidthFilled = (int) (gfHealth * (BAR_WIDTH + 1));
        int x = width / 2 - BAR_WIDTH / 2;
        int y = 25;
        if (player.isInWater() || player.getArmorValue() > 0) {
            y -= 10;
        }

        guiGraphics.drawString(font, outstring, width / 2 - font.width(outstring) / 2, y - 10, NAME_COLOR, true);
        guiGraphics.blit(TEXTURE, x, y, 0.0F, 0.0F, BAR_WIDTH, BAR_HEIGHT, TEX_SIZE, TEX_SIZE);
        if (barWidthFilled > 0) {
            guiGraphics.blit(TEXTURE, x, y, 0.0F, (float) BAR_HEIGHT, barWidthFilled, BAR_HEIGHT, TEX_SIZE, TEX_SIZE);
        }
    }

    private static String namedOr(LivingEntity living, String fallback) {
        if (living.hasCustomName() && living.getCustomName() != null) {
            String custom = living.getCustomName().getString();
            if (!custom.isEmpty()) {
                return custom;
            }
        }
        return fallback;
    }

    private static float healthRatio(LivingEntity living) {
        float max = living.getMaxHealth();
        if (max <= 0.0F) {
            return 0.0F;
        }
        float ratio = living.getHealth() / max;
        if (ratio < 0.0F) {
            return 0.0F;
        }
        if (ratio > 1.0F) {
            return 1.0F;
        }
        return ratio;
    }
}
