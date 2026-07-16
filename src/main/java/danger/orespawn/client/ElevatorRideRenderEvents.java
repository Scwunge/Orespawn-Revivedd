package danger.orespawn.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import danger.orespawn.entity.Elevator;
import danger.orespawn.util.Reference;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.neoforged.neoforge.client.event.RenderLivingEvent;
import net.neoforged.neoforge.client.event.ViewportEvent;

/**
 * Rider body tilts with the board. Camera stays full freelook — only a light
 * bank roll is added (never hijacks yaw/pitch / freecam snap).
 */
@EventBusSubscriber(modid = Reference.MOD_ID, value = Dist.CLIENT)
public final class ElevatorRideRenderEvents {
    private ElevatorRideRenderEvents() {}

    private static float smoothBankRoll;
    private static boolean rollSeeded;
    private static int lastBoardId = -1;

    /** Light horizon bank only — keep freelook fully free. */
    private static final float BANK_ROLL_SCALE = 0.45F;
    private static final float ROLL_SMOOTH = 12.0F;

    @SubscribeEvent
    public static void onLogout(ClientPlayerNetworkEvent.LoggingOut event) {
        rollSeeded = false;
        lastBoardId = -1;
    }

    @SubscribeEvent
    public static void onCameraAngles(ViewportEvent.ComputeCameraAngles event) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null || event.getCamera().getEntity() != player) {
            rollSeeded = false;
            return;
        }
        if (!(player.getVehicle() instanceof Elevator board)) {
            rollSeeded = false;
            lastBoardId = -1;
            return;
        }

        if (board.getId() != lastBoardId) {
            rollSeeded = false;
            lastBoardId = board.getId();
        }

        float pt = (float) event.getPartialTick();
        float boardRoll = board.getFlightRoll(pt);

        if (!rollSeeded) {
            smoothBankRoll = boardRoll;
            rollSeeded = true;
        } else {
            float dt = frameDt();
            float a = 1.0F - (float) Math.exp(-ROLL_SMOOTH * dt);
            a = Mth.clamp(a, 0.08F, 0.55F);
            smoothBankRoll = Mth.rotLerp(a, smoothBankRoll, boardRoll);
        }

        // NEVER set yaw/pitch — that caused freecam snap / loss of board control feel
        float add = smoothBankRoll * BANK_ROLL_SCALE;
        if (Math.abs(add) > 0.05F) {
            event.setRoll(event.getRoll() + add);
        }
    }

    private static float frameDt() {
        float dt = 1.0F / 60.0F;
        try {
            dt = Minecraft.getInstance().getTimer().getGameTimeDeltaPartialTick(false) / 20.0F;
        } catch (Throwable ignored) {
            // default
        }
        if (dt <= 0f || dt > 0.25f) {
            return 1.0F / 60.0F;
        }
        return dt;
    }

    @SubscribeEvent
    public static void onRenderLivingPre(RenderLivingEvent.Pre<?, ?> event) {
        LivingEntity living = event.getEntity();
        if (!(living.getVehicle() instanceof Elevator board)) {
            return;
        }
        float pt = event.getPartialTick();
        float pitch = board.getFlightPitch(pt);
        float roll = board.getFlightRoll(pt);

        PoseStack pose = event.getPoseStack();
        pose.pushPose();

        if (Math.abs(pitch) > 0.05F || Math.abs(roll) > 0.05F) {
            double bx = Mth.lerp(pt, board.xo, board.getX());
            double by = Mth.lerp(pt, board.yo, board.getY());
            double bz = Mth.lerp(pt, board.zo, board.getZ());
            double px = Mth.lerp(pt, living.xo, living.getX());
            double py = Mth.lerp(pt, living.yo, living.getY());
            double pz = Mth.lerp(pt, living.zo, living.getZ());

            pose.translate(bx - px, by - py, bz - pz);
            pose.mulPose(Axis.XP.rotationDegrees(pitch));
            pose.mulPose(Axis.ZP.rotationDegrees(roll));
            pose.translate(px - bx, py - by, pz - bz);
        }
    }

    @SubscribeEvent
    public static void onRenderLivingPost(RenderLivingEvent.Post<?, ?> event) {
        if (event.getEntity().getVehicle() instanceof Elevator) {
            event.getPoseStack().popPose();
        }
    }
}
