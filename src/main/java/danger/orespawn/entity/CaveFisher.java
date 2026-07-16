package danger.orespawn.entity;

import danger.orespawn.OreSpawnMain;
import danger.orespawn.init.ModItems;
import danger.orespawn.util.ai.GoldStyleCombat;
import danger.orespawn.util.ai.WanderALotGoal;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
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
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import org.jetbrains.annotations.Nullable;

/**
 * Gold {@code CaveFisher} (EntityMob). Size 1.35×0.75, speed 0.2, health 20, attack 4, armor 10, XP 10.
 * Combat is proximity-scan (no {@code MeleeAttackGoal}): every 1/8 tick scan box 10×3×10,
 * melee reach {@code (4 + targetWidth/2)^2}, hit if {@code nextInt(7)==0 || nextInt(8)==1}, path 1.2.
 */
public class CaveFisher extends Monster {
    private static final EntityDataAccessor<Byte> ATTACKING =
            SynchedEntityData.defineId(CaveFisher.class, EntityDataSerializers.BYTE);

    private final float moveSpeed = 0.2F;
    /** Gold client animation state for {@code ModelCaveFisher}. */
    private RenderInfo renderdata = new RenderInfo();

    public CaveFisher(EntityType<? extends CaveFisher> type, Level level) {
        super(type, level);
        this.xpReward = 10;
    }

    public RenderInfo getRenderInfo() {
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
                .add(Attributes.MAX_HEALTH, 20.0) // mygetMaxHealth()
                .add(Attributes.MOVEMENT_SPEED, 0.2)
                .add(Attributes.ATTACK_DAMAGE, 4.0)
                .add(Attributes.ARMOR, 10.0) // func_70658_aO
                .add(Attributes.FOLLOW_RANGE, 16.0);
    }

    @Override
    protected void registerGoals() {
        // gold: Swimming, WanderALot(14,1.0), WatchClosest(Player,8), LookIdle, HurtByTarget
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new WanderALotGoal(this, 14, 1.0));
        this.goalSelector.addGoal(2, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(3, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        // no MeleeAttackGoal — gold uses updateAITasks proximity combat only
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(ATTACKING, (byte) 0);
        // gold entityInit zeros RenderInfo
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
        // gold onUpdate: reset move speed every tick
        if (this.getAttribute(Attributes.MOVEMENT_SPEED) != null) {
            this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(this.moveSpeed);
        }
        super.tick();
    }

    public int mygetMaxHealth() {
        return 20;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return null;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return danger.orespawn.util.handlers.SoundsHandler.ENTITY_CRYO_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return danger.orespawn.util.handlers.SoundsHandler.ENTITY_ALOSAURUS_DEATH.get();
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
        // gold getDropItem: nextInt(6) → glowstone / uranium / titanium / null
        int i = this.random.nextInt(6);
        if (i == 0) {
            this.spawnAtLocation(new ItemStack(Items.GLOWSTONE_DUST));
        } else if (i == 1) {
            this.spawnAtLocation(new ItemStack(ModItems.URANIUM_NUGGET.get()));
        } else if (i == 2) {
            this.spawnAtLocation(new ItemStack(ModItems.TITANIUM_NUGGET.get()));
        }
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        // gold: ignore cactus damage by msg id
        if ("cactus".equals(source.getMsgId())) {
            return false;
        }
        return super.hurt(source, amount);
    }

    /**
     * Gold {@code updateAITasks} / {@code func_70619_bc}:
     * every 1/8 tick, find target; if in reach set attacking + random hit; else path 1.2.
     */
    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
        if (this.isDeadOrDying()) {
            return;
        }
        // gold: nextInt(8) == 0
        if (this.random.nextInt(8) != 0) {
            return;
        }
        LivingEntity e = this.findSomethingToAttack();
        if (e != null) {
            // gold: distanceSq < (4.0F + e.width/2)^2
            if (GoldStyleCombat.inMeleeRange(this, e, 4.0)) {
                this.setAttacking(1);
                // gold: nextInt(7)==0 || nextInt(8)==1
                if (this.random.nextInt(7) == 0 || this.random.nextInt(8) == 1) {
                    this.doHurtTarget(e);
                }
            } else {
                this.getNavigation().moveTo(e, 1.2);
            }
        } else {
            this.setAttacking(0);
        }
    }

    /**
     * Gold {@code isSuitableTarget}: alive, always canSee, not CaveFisher, not EntityMob,
     * not creative player. Attacks animals / players / other non-monsters.
     */
    private boolean isSuitableTarget(LivingEntity target) {
        if (target == null || target == this || !target.isAlive()) {
            return false;
        }
        // gold: EntitySenses.canSee — required always (not only when far)
        if (!this.hasLineOfSight(target)) {
            return false;
        }
        if (target instanceof CaveFisher) {
            return false;
        }
        // gold: instanceof EntityMob
        if (target instanceof Monster) {
            return false;
        }
        if (target instanceof Player player) {
            // gold: skip creative; spectator is modern equivalent skip
            if (player.isSpectator() || player.getAbilities().instabuild) {
                return false;
            }
        }
        return true;
    }

    @Nullable
    private LivingEntity findSomethingToAttack() {
        // gold: OreSpawnMain.PlayNicely != 0 → null
        if (OreSpawnMain.PlayNicely != 0) {
            return null;
        }
        // gold box expand(10, 3, 10); first suitable (GoldStyleCombat prefers nearest player)
        return GoldStyleCombat.findTarget(this, 10.0, 3.0, this::isSuitableTarget);
    }

    public int getAttacking() {
        return this.entityData.get(ATTACKING);
    }

    public void setAttacking(int value) {
        this.entityData.set(ATTACKING, (byte) value);
    }

    /**
     * Gold {@code getCanSpawnHere}: valid light (via super) and y ≤ 50.
     */
    @Override
    public boolean checkSpawnRules(LevelAccessor level, MobSpawnType spawnType) {
        if (!super.checkSpawnRules(level, spawnType)) {
            return false;
        }
        return !(this.getY() > 50.0);
    }
}
