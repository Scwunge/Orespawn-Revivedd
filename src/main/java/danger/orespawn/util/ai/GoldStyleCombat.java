package danger.orespawn.util.ai;

import java.util.List;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Nullable;

/**
 * Shared helpers for gold OreSpawn combat: most hostiles do <em>not</em> use
 * {@code MeleeAttackGoal} + continuous chase. They scan nearby living entities and
 * either path a short distance or melee when close (see gold {@code updateAITasks} /
 * {@code findSomethingToAttack}).
 */
public final class GoldStyleCombat {
    private GoldStyleCombat() {}

    /** Prefer nearest non-creative player, else first suitable living entity in box. */
    @Nullable
    public static LivingEntity findTarget(
            Mob self,
            double xzRange,
            double yRange,
            java.util.function.Predicate<LivingEntity> suitable) {
        if (self.level().getDifficulty() == Difficulty.PEACEFUL) {
            return null;
        }
        AABB box = self.getBoundingBox().inflate(xzRange, yRange, xzRange);
        Player nearestPlayer = null;
        double bestPlayer = Double.MAX_VALUE;
        LivingEntity other = null;
        List<LivingEntity> list = self.level().getEntitiesOfClass(LivingEntity.class, box);
        for (LivingEntity living : list) {
            if (!suitable.test(living)) {
                continue;
            }
            if (living instanceof Player player) {
                if (player.isSpectator() || player.getAbilities().instabuild) {
                    continue;
                }
                double d = self.distanceToSqr(player);
                if (d < bestPlayer) {
                    bestPlayer = d;
                    nearestPlayer = player;
                }
            } else if (other == null) {
                other = living;
            }
        }
        return nearestPlayer != null ? nearestPlayer : other;
    }

    /**
     * Gold reach: {@code (base + targetWidth/2)^2} vs distanceSq.
     * Returns true when in melee range.
     */
    public static boolean inMeleeRange(Mob self, LivingEntity target, double baseReach) {
        double reach = baseReach + target.getBbWidth() / 2.0;
        return self.distanceToSqr(target) < reach * reach;
    }

    /** Deal attribute attack damage (gold {@code attackEntityAsMob} without MeleeAttackGoal). */
    public static boolean dealAttributeDamage(Mob self, net.minecraft.world.entity.Entity target) {
        float dmg = (float) self.getAttributeValue(Attributes.ATTACK_DAMAGE);
        if (dmg <= 0.0F) {
            dmg = 1.0F;
        }
        return target.hurt(self.damageSources().mobAttack(self), dmg);
    }

    /**
     * Gold pattern: occasionally look for target; if close, random chance to hit;
     * if farther, path toward them (short-range “aggro”, not vanilla endless chase).
     *
     * @param checkChance gold {@code nextInt(n) == 0} → pass n (e.g. 5 for 1/5)
     * @param hitChanceA  gold first random (e.g. 4 for 1/4)
     * @param hitChanceB  gold second random (e.g. 5 for 1/5) — hit if either succeeds
     * @param baseReach   gold often 4.0
     * @param pathSpeed   gold often 1.2–1.25
     */
    public static void tickProximityCombat(
            Mob self,
            LivingEntity target,
            int checkChance,
            int hitChanceA,
            int hitChanceB,
            double baseReach,
            double pathSpeed,
            Runnable onInRange,
            Runnable onOutOfRange) {
        if (target == null || !target.isAlive()) {
            return;
        }
        if (self.getRandom().nextInt(Math.max(1, checkChance)) != 0) {
            return;
        }
        self.getLookControl().setLookAt(target, 10.0F, 10.0F);
        if (inMeleeRange(self, target, baseReach)) {
            if (onInRange != null) {
                onInRange.run();
            }
            if (self.getRandom().nextInt(Math.max(1, hitChanceA)) == 0
                    || self.getRandom().nextInt(Math.max(1, hitChanceB)) == 1) {
                dealAttributeDamage(self, target);
            }
        } else {
            if (onOutOfRange != null) {
                onOutOfRange.run();
            }
            self.getNavigation().moveTo(target, pathSpeed);
        }
    }
}
