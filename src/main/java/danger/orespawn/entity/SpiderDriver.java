package danger.orespawn.entity;

import danger.orespawn.OreSpawnMain;
import java.util.Comparator;
import java.util.List;
import net.minecraft.world.Difficulty;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.monster.CaveSpider;
import net.minecraft.world.entity.monster.Spider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Nullable;

/**
 * Gold {@code SpiderDriver} (EntitySpider) 1:1 best-effort for NeoForge 1.21.1.
 * Size vanilla spider 1.4×0.9; mounts {@link SpiderRobot} and steers via {@code goThisWay}.
 * Texture: {@code textures/entity/spiderdriver.png}.
 * Registry size/attrs set in {@code ModEntities}.
 */
public class SpiderDriver extends Spider {

    public SpiderDriver(EntityType<? extends SpiderDriver> type, Level level) {
        super(type, level);
    }

    /**
     * Vanilla spider attrs + gold dynamic armor baseline (20 when free; 8 while riding — applied in {@link #aiStep()}).
     */
    public static AttributeSupplier.Builder createAttributes() {
        return Spider.createAttributes()
                .add(Attributes.ARMOR, 20.0); // gold func_70658_aO when not riding
    }

    @Override
    protected void registerGoals() {
        // gold EntitySpider super goals (leap/melee/target) + explicit gold list:
        // Swimming, Panic(1.5), MyEntityAIWander(0.65), LookIdle, HurtByTarget(false)
        super.registerGoals();
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(2, new PanicGoal(this, 1.5));
        // gold MyEntityAIWander — WaterAvoidingRandomStrollGoal is closest vanilla (no MyEntityAIWander port)
        this.goalSelector.addGoal(3, new WaterAvoidingRandomStrollGoal(this, 0.65));
        this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));
        // gold EntityAIHurtByTarget(this, false) — no alert others (default)
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
    }

    /**
     * Gold {@code func_70692_ba} / canDespawn:
     * {@code isNoDespawnRequired ? false : ridingEntity == null}
     * → despawn only when not riding (and not persistence-required).
     */
    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        if (this.isPersistenceRequired()) {
            return false;
        }
        return !this.isPassenger();
    }

    @Override
    public void aiStep() {
        // gold getTotalArmorValue: 8 while riding, 20 free
        if (this.getAttribute(Attributes.ARMOR) != null) {
            this.getAttribute(Attributes.ARMOR).setBaseValue(this.isPassenger() ? 8.0 : 20.0);
        }
        super.aiStep();
    }

    /**
     * Gold {@code updateAITasks} / {@code func_70619_bc}:
     * mount empty SpiderRobot; while riding, steer robot toward attack targets.
     */
    @Override
    protected void customServerAiStep() {
        if (this.isDeadOrDying()) {
            return;
        }
        super.customServerAiStep();

        if (this.level().getDifficulty() == Difficulty.PEACEFUL) {
            return;
        }

        // gold: 1/5 when not riding → find SpiderRobot, look, mount if close else path 0.55
        if (this.random.nextInt(5) == 0 && !this.isPassenger()) {
            LivingEntity e = this.findSpiderRobot();
            if (e != null) {
                this.getLookControl().setLookAt(e, 10.0F, 10.0F);
                float reach = 4.0F + e.getBbWidth() / 2.0F;
                if (this.distanceToSqr(e) < (double) (reach * reach)) {
                    this.startRiding(e);
                } else {
                    this.getNavigation().moveTo(e, 0.55);
                }
            }
        }

        // gold: 1/4 when riding → find attack target; if not in close band, steer SpiderRobot
        if (this.random.nextInt(4) == 0 && this.isPassenger()) {
            LivingEntity e = this.findSomethingToAttack();
            if (e != null) {
                this.getLookControl().setLookAt(e, 10.0F, 10.0F);
                float close = 11.0F + e.getBbWidth() / 2.0F;
                if (!(this.distanceToSqr(e) < (double) (close * close))
                        && this.getVehicle() instanceof SpiderRobot sp) {
                    double d1 = e.getZ() - this.getZ();
                    double d2 = e.getX() - this.getX();
                    double dd = Math.atan2(d1, d2);
                    sp.goThisWay(0.35 * Math.cos(dd), 0.35 * Math.sin(dd));
                }
            }
        }
    }

    /**
     * Gold {@code attackEntityAsMob} path via spider melee + 50% poison 60 ticks amp 0.
     */
    @Override
    public boolean doHurtTarget(Entity target) {
        boolean hit = super.doHurtTarget(target);
        if (hit && target instanceof LivingEntity living && this.random.nextInt(2) == 0) {
            living.addEffect(new MobEffectInstance(MobEffects.POISON, 60, 0));
        }
        return hit;
    }

    /** Gold {@code findSpiderRobot}: nearest empty SpiderRobot in 25×15×25 (GenericTargetSorter). */
    @Nullable
    private LivingEntity findSpiderRobot() {
        if (OreSpawnMain.PlayNicely != 0) {
            return null;
        }
        AABB box = this.getBoundingBox().inflate(25.0, 15.0, 25.0);
        List<SpiderRobot> list = this.level().getEntitiesOfClass(SpiderRobot.class, box);
        list.sort(Comparator.comparingDouble(this::distanceToSqr));
        for (SpiderRobot robot : list) {
            // gold: riddenByEntity == null
            if (!robot.isVehicle()) {
                return robot;
            }
        }
        return null;
    }

    /**
     * Gold {@code isSuitableTarget}: not peaceful/self/dead; not ignoreable (deferred);
     * not SpiderRobot / SpiderDriver / Spider / CaveSpider; needs LOS;
     * players not creative; non-players only if dist² ≥ 36.
     */
    private boolean isSuitableTarget(LivingEntity target) {
        if (this.level().getDifficulty() == Difficulty.PEACEFUL) {
            return false;
        }
        if (target == null || target == this || !target.isAlive()) {
            return false;
        }
        // gold MyUtils.isIgnoreable deferred
        if (target instanceof SpiderRobot) {
            return false;
        }
        if (target instanceof SpiderDriver) {
            return false;
        }
        if (target instanceof Spider) {
            return false;
        }
        if (target instanceof CaveSpider) {
            return false;
        }
        if (!this.hasLineOfSight(target)) {
            return false;
        }
        if (target instanceof Player player) {
            return !player.getAbilities().instabuild && !player.isSpectator();
        }
        // gold: return !(distSq < 36.0) → only far non-players
        return !(this.distanceToSqr(target) < 36.0);
    }

    /** Gold {@code findSomethingToAttack}: LivingEntity box 35×15×35, nearest suitable. */
    @Nullable
    private LivingEntity findSomethingToAttack() {
        if (OreSpawnMain.PlayNicely != 0) {
            return null;
        }
        AABB box = this.getBoundingBox().inflate(35.0, 15.0, 35.0);
        List<LivingEntity> list = this.level().getEntitiesOfClass(LivingEntity.class, box);
        list.sort(Comparator.comparingDouble(this::distanceToSqr));
        for (LivingEntity living : list) {
            if (this.isSuitableTarget(living)) {
                return living;
            }
        }
        return null;
    }

    /**
     * Gold {@code getCanSpawnHere}: true if SpiderRobot within 24×12×24, else super.
     */
    @Override
    public boolean checkSpawnRules(LevelAccessor level, MobSpawnType spawnType) {
        AABB box = this.getBoundingBox().inflate(24.0, 12.0, 24.0);
        List<SpiderRobot> nearby = level.getEntitiesOfClass(SpiderRobot.class, box);
        if (!nearby.isEmpty()) {
            return true;
        }
        return super.checkSpawnRules(level, spawnType);
    }
}
