package danger.orespawn.entity;

import danger.orespawn.OreSpawnMain;
import danger.orespawn.init.ModItems;
import danger.orespawn.util.ai.GoldStyleCombat;
import danger.orespawn.util.ai.WanderALotGoal;
import danger.orespawn.util.handlers.SoundsHandler;
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
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Spider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import org.jetbrains.annotations.Nullable;

/**
 * Gold {@code Scorpion} (EntityMob) 1:1 for NeoForge 1.21.1.
 * Size 0.85×0.55, speed 0.2, health 15, attack 4, armor 10, XP 10.
 * Proximity combat; hunts spiders/creepers/VelocityRaptor/animals/players.
 * Model uses {@link RenderInfo} for claw/tail animation state.
 */
public class Scorpion extends Monster {
    private static final EntityDataAccessor<Byte> ATTACKING =
            SynchedEntityData.defineId(Scorpion.class, EntityDataSerializers.BYTE);

    private final float moveSpeed = 0.2F;
    /** Gold client animation state for {@code ModelScorpion}. */
    private RenderInfo renderdata = new RenderInfo();

    public Scorpion(EntityType<? extends Scorpion> type, Level level) {
        super(type, level);
        this.xpReward = 10;
    }

    public RenderInfo getRenderInfo() {
        if (this.renderdata == null) {
            this.renderdata = new RenderInfo();
        }
        return this.renderdata;
    }

    /** Gold {@code setRenderInfo} — copy fields (model mutates then writes back). */
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
                .add(Attributes.MAX_HEALTH, 15.0) // Scorpion_stats.health
                .add(Attributes.MOVEMENT_SPEED, 0.2)
                .add(Attributes.ATTACK_DAMAGE, 4.0) // Scorpion_stats.attack
                .add(Attributes.ARMOR, 10.0) // Scorpion_stats.defense
                .add(Attributes.FOLLOW_RANGE, 16.0); // gold field_70174_ab = 100 (tracking); scan box 8
    }

    @Override
    protected void registerGoals() {
        // gold: Swimming, MoveThroughVillage (skipped), WanderALot(14,1), WatchClosest(Player,8),
        // LookIdle, HurtByTarget
        this.goalSelector.addGoal(0, new FloatGoal(this));
        // gold EntityAIMoveThroughVillage skipped
        this.goalSelector.addGoal(2, new WanderALotGoal(this, 14, 1.0));
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
        // gold canDespawn: !isNoDespawnRequired
        return !this.isPersistenceRequired();
    }

    @Override
    public void tick() {
        if (this.getAttribute(Attributes.MOVEMENT_SPEED) != null) {
            this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(this.moveSpeed);
        }
        super.tick();
    }

    public int mygetMaxHealth() {
        return 15;
    }

    public int getAttacking() {
        return this.entityData.get(ATTACKING);
    }

    public void setAttacking(int value) {
        this.entityData.set(ATTACKING, (byte) value);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return null;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        // gold orespawn:scorpion_hit (wired)
        return SoundsHandler.ENTITY_SCORPION_HIT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        // gold orespawn:cryo_death (wired)
        return SoundsHandler.ENTITY_CRYO_DEATH.get();
    }

    @Override
    protected float getSoundVolume() {
        return 1.5F;
    }

    @Override
    public float getVoicePitch() {
        return 1.0F;
    }

    @Override
    protected void dropCustomDeathLoot(ServerLevel level, DamageSource damageSource, boolean recentlyHit) {
        super.dropCustomDeathLoot(level, damageSource, recentlyHit);
        // gold getDropItem: nextInt(10) → iron_ingot / uranium_nugget / titanium_nugget / null
        int i = this.random.nextInt(10);
        if (i == 0) {
            this.spawnAtLocation(new ItemStack(Items.IRON_INGOT));
        } else if (i == 1) {
            this.spawnAtLocation(new ItemStack(ModItems.URANIUM_NUGGET.get()));
        } else if (i == 2) {
            this.spawnAtLocation(new ItemStack(ModItems.TITANIUM_NUGGET.get()));
        }
    }

    @Override
    public boolean doHurtTarget(Entity target) {
        return GoldStyleCombat.dealAttributeDamage(this, target);
    }

    /** Gold: ignore cactus damage. */
    @Override
    public boolean hurt(DamageSource source, float amount) {
        if ("cactus".equals(source.getMsgId())) {
            return false;
        }
        return super.hurt(source, amount);
    }

    /**
     * Gold {@code updateAITasks}: every 1/6 tick scan; if distSq &lt; 9 set attacking + random hit
     * (nextInt(5)==0 || nextInt(6)==1) + 1/3 scorpion_attack sound; else path 1.2.
     */
    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
        if (this.isDeadOrDying()) {
            return;
        }
        if (this.random.nextInt(6) != 0) {
            return;
        }
        LivingEntity e = this.findSomethingToAttack();
        if (e != null) {
            if (this.distanceToSqr(e) < 9.0) {
                this.setAttacking(1);
                if (this.random.nextInt(5) == 0 || this.random.nextInt(6) == 1) {
                    this.doHurtTarget(e);
                    // gold scorpion_attack 0.75 / 1.5 on 1/3 — deferred until SoundsHandler wires attack
                }
            } else {
                this.getNavigation().moveTo(e, 1.2);
            }
        } else {
            this.setAttacking(0);
        }
    }

    /**
     * Gold {@code isSuitableTarget}: VelocityRaptor/Spider/CaveSpider/Creeper yes;
     * Scorpion/EmperorScorpion/Ghost/GhostSkelly no; other EntityMob no; animals + players yes.
     * MyUtils.isIgnoreable deferred (no port helper).
     */
    private boolean isSuitableTarget(LivingEntity target) {
        if (target == null || target == this || !target.isAlive()) {
            return false;
        }
        if (!this.hasLineOfSight(target)) {
            return false;
        }
        if (target instanceof Ghost || target instanceof GhostSkelly) {
            return false;
        }
        if (target instanceof VelocityRaptor) {
            return true;
        }
        // gold EntitySpider + EntityCaveSpider (CaveSpider extends Spider in 1.21)
        if (target instanceof Spider) {
            return true;
        }
        if (target instanceof Scorpion) {
            return false;
        }
        // gold EmperorScorpion — not ported; skip if ever present
        if (target instanceof Creeper) {
            return true;
        }
        if (target instanceof Monster) {
            return false;
        }
        if (target instanceof Player player) {
            if (player.isSpectator() || player.getAbilities().instabuild) {
                return false;
            }
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
     * Gold {@code getCanSpawnHere}: valid light; night always OK; day only if y ≤ 50.
     */
    @Override
    public boolean checkSpawnRules(LevelAccessor level, MobSpawnType spawnType) {
        if (!super.checkSpawnRules(level, spawnType)) {
            return false;
        }
        if (level instanceof Level lvl && lvl.isDay()) {
            return !(this.getY() > 50.0);
        }
        return true;
    }
}
