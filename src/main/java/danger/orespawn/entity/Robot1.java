package danger.orespawn.entity;

import danger.orespawn.OreSpawnMain;
import danger.orespawn.util.ai.GoldStyleCombat;
import danger.orespawn.util.ai.WanderALotGoal;
import danger.orespawn.util.handlers.SoundsHandler;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import org.jetbrains.annotations.Nullable;

/**
 * Gold {@code Robot1} (EntityMob) 1:1 for NeoForge 1.21.1.
 * Size 0.5×0.5, speed 0.2, health 5, attack 4, armor 2, XP 5.
 * Suicide bomber: paths to target, explodes at close range (power 2.5).
 * Texture: {@code robot1.png}.
 */
public class Robot1 extends Monster {
    private static final EntityDataAccessor<Byte> ATTACKING =
            SynchedEntityData.defineId(Robot1.class, EntityDataSerializers.BYTE);

    private final float moveSpeed = 0.2F;
    /** Gold client animation state (key wind-up uses entity random; RenderInfo reserved). */
    private RenderInfo renderdata = new RenderInfo();

    public Robot1(EntityType<? extends Robot1> type, Level level) {
        super(type, level);
        this.xpReward = 5;
    }

    public RenderInfo getRenderInfo() {
        if (this.renderdata == null) {
            this.renderdata = new RenderInfo();
        }
        return this.renderdata;
    }

    public void setRenderInfo(RenderInfo r) {
        if (r == null) {
            return;
        }
        if (this.renderdata == null) {
            this.renderdata = new RenderInfo();
        }
        this.renderdata.rf1 = r.rf1;
        this.renderdata.rf2 = r.rf2;
        this.renderdata.rf3 = r.rf3;
        this.renderdata.rf4 = r.rf4;
        this.renderdata.ri1 = r.ri1;
        this.renderdata.ri2 = r.ri2;
        this.renderdata.ri3 = r.ri3;
        this.renderdata.ri4 = r.ri4;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 5.0)
                .add(Attributes.MOVEMENT_SPEED, 0.2)
                .add(Attributes.ATTACK_DAMAGE, 4.0)
                .add(Attributes.ARMOR, 2.0)
                .add(Attributes.FOLLOW_RANGE, 5.0); // gold field_70174_ab = 5
    }

    @Override
    protected void registerGoals() {
        // gold: Swimming, WanderALot(10,1), MoveThroughVillage skipped, WatchClosest(Player,8), LookIdle, HurtByTarget
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new WanderALotGoal(this, 10, 1.0));
        this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(ATTACKING, (byte) 0);
        if (this.renderdata == null) {
            this.renderdata = new RenderInfo();
        }
        this.renderdata.rf1 = 0.0F;
        this.renderdata.rf2 = 0.0F;
        this.renderdata.rf3 = 0.0F;
        this.renderdata.rf4 = 0.0F;
        this.renderdata.ri1 = 0;
        this.renderdata.ri2 = 0;
        this.renderdata.ri3 = 0;
        this.renderdata.ri4 = 0;
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return !this.isPersistenceRequired();
    }

    @Override
    public void tick() {
        if (this.getAttribute(Attributes.MOVEMENT_SPEED) != null) {
            this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(this.moveSpeed);
        }
        super.tick();
    }

    /**
     * Gold {@code onLivingUpdate}: 1/8 scan; if close + server + 1/18 → explode 2.5 + die;
     * smoke/lava particles while chasing; path 1.2.
     */
    @Override
    public void aiStep() {
        super.aiStep();
        if (this.random.nextInt(8) != 0) {
            return;
        }
        LivingEntity e = this.findSomethingToAttack();
        if (e == null) {
            return;
        }
        if (this.distanceToSqr(e) < 5.0 && !this.level().isClientSide && this.random.nextInt(18) == 1) {
            boolean grief = this.level().getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING);
            this.level().explode(
                    this,
                    this.getX(),
                    this.getY(),
                    this.getZ(),
                    2.5F,
                    grief,
                    grief ? Level.ExplosionInteraction.MOB : Level.ExplosionInteraction.NONE);
            this.discard();
            return;
        }
        if (this.level().isClientSide) {
            for (int i = 0; i < 2; i++) {
                this.level().addParticle(
                        ParticleTypes.SMOKE, this.getX(), this.getY() + 1.0, this.getZ(), 0.0, 0.0, 0.0);
                this.level().addParticle(
                        ParticleTypes.LAVA, this.getX(), this.getY() + 1.0, this.getZ(), 0.0, 0.0, 0.0);
            }
        }
        this.getNavigation().moveTo(e, 1.2);
    }

    public int mygetMaxHealth() {
        return 5;
    }

    public int getAttacking() {
        return this.entityData.get(ATTACKING);
    }

    public void setAttacking(int value) {
        this.entityData.set(ATTACKING, (byte) value);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        // gold orespawn:kyuubi_living
        return SoundsHandler.ENTITY_KYUUBI_LIVING.get();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        // gold orespawn:scorpion_hit
        return SoundsHandler.ENTITY_SCORPION_HIT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        //  gold orespawn:robot1_death
        return null;
    }

    @Override
    protected float getSoundVolume() {
        return 1.0F;
    }

    @Override
    public float getVoicePitch() {
        return 1.0F;
    }

    @Override
    protected void dropCustomDeathLoot(ServerLevel level, DamageSource damageSource, boolean recentlyHit) {
        super.dropCustomDeathLoot(level, damageSource, recentlyHit);
        // gold getDropItem: gunpowder
        this.spawnAtLocation(new ItemStack(Items.GUNPOWDER));
    }

    @Override
    public boolean doHurtTarget(Entity target) {
        return GoldStyleCombat.dealAttributeDamage(this, target);
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if ("cactus".equals(source.getMsgId())) {
            return false;
        }
        return super.hurt(source, amount);
    }

    @Override
    protected void customServerAiStep() {
        if (!this.isDeadOrDying()) {
            super.customServerAiStep();
        }
    }

    /** Gold: not EntityMob, not creative player; MyUtils.isIgnoreable deferred. */
    private boolean isSuitableTarget(LivingEntity target) {
        if (target == null || target == this || !target.isAlive()) {
            return false;
        }
        if (!this.hasLineOfSight(target)) {
            return false;
        }
        if (target instanceof Monster) {
            return false;
        }
        if (target instanceof Player player) {
            return !player.isSpectator() && !player.getAbilities().instabuild;
        }
        return true;
    }

    @Nullable
    private LivingEntity findSomethingToAttack() {
        if (OreSpawnMain.PlayNicely != 0) {
            return null;
        }
        // gold box expand(8, 3, 8)
        return GoldStyleCombat.findTarget(this, 8.0, 3.0, this::isSuitableTarget);
    }

    /**
     * Gold getCanSpawnHere: y≥50, not bright (light check), night only.
     */
    @Override
    public boolean checkSpawnRules(LevelAccessor level, MobSpawnType spawnType) {
        if (this.getY() < 50.0) {
            return false;
        }
        if (level instanceof Level lvl && lvl.isDay()) {
            return false;
        }
        return super.checkSpawnRules(level, spawnType);
    }
}
