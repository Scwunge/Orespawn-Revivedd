package danger.orespawn.entity;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

/**
 * Hoverboard flight: <b>look = aim</b> in cruise, bird-style aerobatics at speed.
 * <p>
 * Cruise: board yaw/pitch smoothly chase the driver's camera every tick (even when
 * idle / no WASD). W/S thrust along the nose. A/D banks. This keeps you in control
 * of the board — freelook-error banking was stiff and felt like freecam.
 * <p>
 * Aerobatic: high speed + W allows continuous pitch through loops.
 */
public final class HoverBirdFlight {
    private HoverBirdFlight() {}

    public static final double MAX_SPEED = 1.05;
    public static final double ACCEL = 0.052;
    public static final double GLIDE_CAP_MULT = 1.55;
    /** Blend toward desired world vel (lower = more coast). */
    public static final double INERTIA = 0.38;
    public static final double HOVER_VERT_EPS = 0.30;
    public static final double Z_DRAG = 0.0011;
    public static final double DIVE_Z_RATE = 0.028;
    public static final double CLIMB_BLEED = 0.014;

    public static final double AERO_SPEED = 0.42;
    public static final double AERO_SPEED_EXIT = 0.30;
    private static final float AERO_SUSTAIN_PITCH = 3.5f;
    private static final double AERO_LOOK_SCALE = 2.8;

    private static final double HANDLING = 24.0;
    private static final float AD_BANK = 2.0f;
    private static final double DT = 0.05;
    private static final double SPEED_EPS = 0.1;

    /** How hard board chases camera each tick (0–1). Never overshoots (fraction of error). */
    private static final float YAW_CHASE = 0.42f;
    private static final float PITCH_CHASE = 0.40f;
    private static final float MAX_YAW_STEP = 12f;
    private static final float MAX_PITCH_STEP = 10f;
    private static final float ROLL_CHASE = 0.18f;
    /** Ignore sub-degree look error — stops model rocking from float noise. */
    private static final float LOOK_SNAP_EPS = 0.75f;

    private static final float MAX_ROLL_CRUISE = 55f;
    private static final float MAX_PITCH_CRUISE = 80f;
    private static final float MAX_ROLL_AERO = 179f;
    private static final float MAX_PITCH_RATE_AERO = 11f;
    private static final float MAX_ROLL_RATE_AERO = 9f;
    private static final float MAX_YAW_RATE_AERO = 8f;

    public static final class State {
        public Vec3 rideVelocity = Vec3.ZERO;
        public float yaw;
        public float pitch;
        public float roll;
        public boolean seeded;
        public float prevLookYaw;
        public float prevLookPitch;
        public final SmoothD mouseXSmoother = new SmoothD();
        public final SmoothD mouseYSmoother = new SmoothD();
        public boolean aerobatic;

        public void reset() {
            rideVelocity = Vec3.ZERO;
            yaw = pitch = roll = 0f;
            prevLookYaw = prevLookPitch = 0f;
            seeded = false;
            mouseXSmoother.reset();
            mouseYSmoother.reset();
            aerobatic = false;
        }
    }

    public static final class SmoothD {
        private double targetValue;
        private double remaining;
        private double lastAmount;

        public double getNewDeltaValue(double target, double multiplier) {
            this.targetValue += target;
            double d0 = this.targetValue - this.remaining;
            double d1 = Mth.lerp(0.5, this.lastAmount, d0);
            double sign = Math.signum(d0);
            if (sign * d0 > sign * this.lastAmount) {
                d0 = d1;
            }
            this.lastAmount = d1;
            this.remaining += d0 * multiplier;
            return d0 * multiplier;
        }

        public void reset() {
            targetValue = remaining = lastAmount = 0;
        }
    }

    public static Vec3 tick(
            Elevator board,
            Player driver,
            State st,
            boolean jumpThisTick,
            boolean damaged
    ) {
        double maxSpeed = MAX_SPEED;
        double accel = ACCEL;
        if (jumpThisTick && st.rideVelocity.length() >= HOVER_VERT_EPS) {
            maxSpeed += 0.40;
            accel += 0.10;
        }
        if (damaged) {
            maxSpeed *= 0.55;
            accel *= 0.55;
        }
        double glideCap = maxSpeed * GLIDE_CAP_MULT;

        if (!st.seeded) {
            st.yaw = driver.getYRot();
            st.pitch = Mth.clamp(driver.getXRot(), -MAX_PITCH_CRUISE, MAX_PITCH_CRUISE);
            st.roll = 0f;
            st.prevLookYaw = driver.getYRot();
            st.prevLookPitch = driver.getXRot();
            st.seeded = true;
        }

        double speed = st.rideVelocity.length();
        boolean inverted = isInverted(st.pitch, st.roll);
        boolean wantAero = speed >= AERO_SPEED
                && (driver.zza > 0.2f && Math.abs(driver.getXRot()) > 40f
                || inverted
                || Math.abs(st.pitch) > 70f);
        boolean aero;
        if (st.aerobatic) {
            aero = inverted || Math.abs(st.pitch) > 55f
                    || (speed >= AERO_SPEED_EXIT && driver.zza > 0.05f && Math.abs(st.pitch) > 40f);
            if (wantAero) {
                aero = true;
            }
            if (speed < AERO_SPEED_EXIT && Math.abs(st.pitch) < 35f && !inverted) {
                aero = false;
            }
        } else {
            aero = wantAero;
        }
        st.aerobatic = aero;

        float dLookYaw = Mth.wrapDegrees(driver.getYRot() - st.prevLookYaw);
        float dLookPitch = Mth.wrapDegrees(driver.getXRot() - st.prevLookPitch);
        st.prevLookYaw = driver.getYRot();
        st.prevLookPitch = driver.getXRot();

        if (aero) {
            tickAerobatic(st, driver, dLookYaw, dLookPitch, speed);
        } else {
            tickCruiseLookFollow(st, driver, speed);
        }

        Basis basis = basisFromYPR(st.yaw, st.pitch, st.roll);
        calculateRideSpaceVel(board, driver, st, maxSpeed, accel, glideCap, jumpThisTick, aero);

        Vec3 local = st.rideVelocity;
        Vec3 world = basis.forward.scale(local.z)
                .add(basis.up.scale(local.y))
                .add(basis.right.scale(local.x));

        board.setYRot(st.yaw);
        board.setXRot(Mth.clamp(st.pitch, -90.0F, 90.0F) * 0.35F);
        board.yBodyRot = st.yaw;
        board.yHeadRot = st.yaw;
        board.setFlightRoll(st.roll);
        board.setFlightPitch(st.pitch);
        board.setAerobatic(aero);

        Vec3 cur = board.getDeltaMovement();
        return cur.scale(1.0 - INERTIA).add(world.scale(INERTIA));
    }

    /**
     * Cruise: board aims where you look. Works at idle (no WASD).
     * Uses fractional error chase (no overshoot) — turn-bank from yaw error was
     * rocking the model left/right and is intentionally omitted.
     */
    private static void tickCruiseLookFollow(State st, Player driver, double speed) {
        float targetYaw = driver.getYRot();
        float targetPitch = Mth.clamp(driver.getXRot(), -MAX_PITCH_CRUISE, MAX_PITCH_CRUISE);

        float yawErr = Mth.wrapDegrees(targetYaw - st.yaw);
        float pitchErr = targetPitch - st.pitch;

        // Snap when very close — kills endless micro-corrections
        if (Math.abs(yawErr) < LOOK_SNAP_EPS) {
            st.yaw = targetYaw;
        } else {
            float yawStep = Mth.clamp(yawErr * YAW_CHASE, -MAX_YAW_STEP, MAX_YAW_STEP);
            st.yaw = Mth.wrapDegrees(st.yaw + yawStep);
        }
        if (Math.abs(pitchErr) < LOOK_SNAP_EPS) {
            st.pitch = targetPitch;
        } else {
            float pitchStep = Mth.clamp(pitchErr * PITCH_CHASE, -MAX_PITCH_STEP, MAX_PITCH_STEP);
            st.pitch = Mth.clamp(st.pitch + pitchStep, -MAX_PITCH_CRUISE, MAX_PITCH_CRUISE);
        }

        // Roll: A/D only (no look-error bank — that jittered the mesh)
        float xxa = driver.xxa;
        float rollTarget = Math.abs(xxa) > 0.05f
                ? Mth.clamp(-xxa * 40f, -MAX_ROLL_CRUISE, MAX_ROLL_CRUISE)
                : 0f;
        st.roll = Mth.lerp(ROLL_CHASE, st.roll, rollTarget);
    }

    /** High-speed continuous pitch for loops. */
    private static void tickAerobatic(
            State st, Player driver, float dLookYaw, float dLookPitch, double speed
    ) {
        double mouseX = Mth.clamp(-dLookYaw * AERO_LOOK_SCALE, -45.0, 45.0);
        double mouseY = Mth.clamp(dLookPitch * AERO_LOOK_SCALE, -45.0, 45.0);
        if (driver.getXRot() <= -85.0F) {
            mouseY = Math.min(mouseY, -AERO_SUSTAIN_PITCH);
        } else if (driver.getXRot() >= 85.0F) {
            mouseY = Math.max(mouseY, AERO_SUSTAIN_PITCH);
        }
        float xxa = driver.xxa;
        if (Math.abs(xxa) > 0.05f) {
            mouseX = Mth.clamp(mouseX + xxa * 14.0, -45.0, 45.0);
        }

        double rollDelta = st.mouseXSmoother.getNewDeltaValue(mouseX * 0.12, DT * 1.5);
        double pitchDelta = st.mouseYSmoother.getNewDeltaValue(mouseY * 0.13, DT * 1.5);

        double pitchOut = Mth.clamp(pitchDelta, -MAX_PITCH_RATE_AERO, MAX_PITCH_RATE_AERO);
        double rollOut = Mth.clamp(rollDelta + xxa * AD_BANK, -MAX_ROLL_RATE_AERO, MAX_ROLL_RATE_AERO);
        double yawOut = Mth.clamp(rollDelta * 0.25, -MAX_YAW_RATE_AERO, MAX_YAW_RATE_AERO);

        st.yaw = Mth.wrapDegrees(st.yaw + (float) yawOut);
        st.pitch = wrap180(st.pitch + (float) pitchOut);
        st.roll = wrap180(st.roll + (float) rollOut);
        if (Math.abs(st.roll) > MAX_ROLL_AERO) {
            st.roll = Math.signum(st.roll) * MAX_ROLL_AERO;
        }

        double yawRate = DT * HANDLING * Math.sin(st.roll * Mth.DEG_TO_RAD);
        yawRate *= Mth.clamp(Math.abs(Math.cos(st.pitch * Mth.DEG_TO_RAD)), 0.0, 1.0);
        if (Math.abs(st.pitch) > 100f) {
            yawRate *= 0.25;
        }
        st.yaw = Mth.wrapDegrees(st.yaw + (float) Mth.clamp(yawRate, -MAX_YAW_RATE_AERO, MAX_YAW_RATE_AERO));
    }

    public static boolean isInverted(float pitchDeg, float rollDeg) {
        return Math.abs(pitchDeg) > 90f || Math.abs(rollDeg) > 100f;
    }

    private static void calculateRideSpaceVel(
            Entity board,
            Player driver,
            State st,
            double maxSpeed,
            double accel,
            double glideCap,
            boolean jumpThisTick,
            boolean aero
    ) {
        Vec3 v = st.rideVelocity;
        float zza = driver.zza;

        if (board.verticalCollision || board.horizontalCollision) {
            double len = board.getDeltaMovement().length();
            if (v.lengthSqr() > 1e-8) {
                v = v.normalize().scale(len);
            } else {
                v = Vec3.ZERO;
            }
        }

        double cruise = aero ? maxSpeed * 1.08 : maxSpeed;
        if (zza != 0f && v.length() < glideCap) {
            double thrustSign;
            if (zza > 0f && v.z > cruise) {
                thrustSign = 0;
            } else if (zza < 0f && v.z < -maxSpeed / 3.0) {
                thrustSign = 0;
            } else {
                thrustSign = Math.signum(zza);
            }
            double a = aero && zza > 0f ? accel * 1.12 : accel;
            v = new Vec3(v.x, v.y, v.z + a * thrustSign);
        }

        v = new Vec3(lerp(v.x, 0.0, 0.15), v.y, v.z);

        double pitchSin = Math.sin(st.pitch * Mth.DEG_TO_RAD) * 0.25;
        if (pitchSin <= 0.0) {
            if (zza <= 0f && !aero) {
                double t = pitchSin * -CLIMB_BLEED * 2.0;
                v = new Vec3(v.x, v.y, lerp(v.z, 0.0, t));
            }
        } else {
            v = new Vec3(v.x, v.y, Math.min(v.z + DIVE_Z_RATE * pitchSin, glideCap));
        }

        double maxVert = maxSpeed / 2.0;
        double preLen = st.rideVelocity.length();
        double vertIn = 0.0;
        if (!aero && preLen < HOVER_VERT_EPS) {
            if (jumpThisTick) {
                vertIn = 1.0;
            } else if (driver.isShiftKeyDown()) {
                vertIn = -1.0;
            }
        }
        if (vertIn != 0.0) {
            v = new Vec3(v.x, Mth.clamp(v.y + accel * vertIn, -maxVert, maxVert), v.z);
        } else {
            v = new Vec3(v.x, lerp(v.y, 0.0, maxVert / 20.0), v.z);
        }

        double zDrag = aero ? Z_DRAG * 0.35 : Z_DRAG;
        if (Math.abs(v.z) > 0.0) {
            v = v.subtract(0.0, 0.0, Math.min(zDrag * Math.signum(v.z), v.z));
        }

        double len = v.length();
        if (len > glideCap) {
            v = v.scale(glideCap / len);
        }
        st.rideVelocity = v;
    }

    private static Basis basisFromYPR(float yawDeg, float pitchDeg, float rollDeg) {
        float f = pitchDeg * Mth.DEG_TO_RAD;
        float f1 = -yawDeg * Mth.DEG_TO_RAD;
        float f2 = Mth.cos(f1);
        float f3 = Mth.sin(f1);
        float f4 = Mth.cos(f);
        float f5 = Mth.sin(f);
        Vec3 forward = new Vec3(f3 * f4, -f5, f2 * f4);
        if (forward.lengthSqr() < 1e-12) {
            forward = new Vec3(0.0, 0.0, 1.0);
        } else {
            forward = forward.normalize();
        }

        Vec3 worldUp = new Vec3(0.0, 1.0, 0.0);
        Vec3 right = forward.cross(worldUp);
        if (right.lengthSqr() < 1e-6) {
            float yr = yawDeg * Mth.DEG_TO_RAD;
            right = new Vec3(Mth.cos(yr), 0.0, Mth.sin(yr));
        }
        right = right.normalize();
        Vec3 up = right.cross(forward).normalize();

        if (Math.cos(pitchDeg * Mth.DEG_TO_RAD) < 0.0) {
            right = worldUp.cross(forward);
            if (right.lengthSqr() < 1e-6) {
                float yr = yawDeg * Mth.DEG_TO_RAD;
                right = new Vec3(Mth.cos(yr), 0.0, Mth.sin(yr));
            }
            right = right.normalize();
            up = right.cross(forward).normalize();
        }

        float roll = rollDeg * Mth.DEG_TO_RAD;
        double cr = Math.cos(roll);
        double sr = Math.sin(roll);
        Vec3 rightR = right.scale(cr).add(up.scale(sr));
        Vec3 upR = up.scale(cr).subtract(right.scale(sr));
        return new Basis(forward, rightR.normalize(), upR.normalize());
    }

    private static float wrap180(float deg) {
        return Mth.wrapDegrees(deg);
    }

    private static double lerp(double a, double b, double t) {
        return a + (b - a) * t;
    }

    private record Basis(Vec3 forward, Vec3 right, Vec3 up) {}
}
