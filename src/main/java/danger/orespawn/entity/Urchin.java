package danger.orespawn.entity;

import danger.orespawn.OreSpawnMain;
import danger.orespawn.init.ModItems;
import danger.orespawn.util.ai.GoldStyleCombat;
import danger.orespawn.util.ai.WanderALotGoal;
import danger.orespawn.util.handlers.SoundsHandler;
import java.util.Comparator;
import java.util.List;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
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
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import org.jetbrains.annotations.Nullable;

/**
 * Gold {@code Urchin} / Crystal Urchin (EntityMob). Size 1.35×2.1, speed 0.3, XP 20.
 * Stats defaults {@code get_mobstats("Urchin", 25, 10, 4)}.
 * Fire-immune night crystal mob; flame particles; self-hurt in water; setFire on hit.
 * Registry size set in {@code ModEntities} .
 */
public class Urchin extends Monster {
    private static final EntityDataAccessor<Byte> ATTACKING =
            SynchedEntityData.defineId(Urchin.class, EntityDataSerializers.BYTE);

    private final float moveSpeed = 0.3F;
    private RenderInfo renderdata = new RenderInfo();
    /** Gold {@code was_spawnered} — spawned from Crystal Urchin spawner; blocks daytime despawn. */
    private int wasSpawnered = 0;

    public Urchin(EntityType<? extends Urchin> type, Level level) {
        super(type, level);
        this.xpReward = 20; // gold field_70728_aV
        this.renderdata = new RenderInfo();
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 25.0) // Urchin_stats.health default
                .add(Attributes.MOVEMENT_SPEED, 0.3)
                .add(Attributes.ATTACK_DAMAGE, 10.0) // Urchin_stats.attack default
                .add(Attributes.ARMOR, 4.0) // Urchin_stats.defense default
                .add(Attributes.FOLLOW_RANGE, 1000.0); // gold field_70174_ab = 1000
    }

    @Override
    protected void registerGoals() {
        // gold: Swimming, WanderALot(14,1.0), WatchClosest(Player,8), LookIdle, HurtByTarget
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new WanderALotGoal(this, 14, 1.0));
        this.goalSelector.addGoal(2, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(3, new RandomLookAroundGoal(this));
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

    public RenderInfo getRenderInfo() {
        return this.renderdata;
    }

    public void setRenderInfo(RenderInfo r) {
        this.renderdata.rf1 = r.rf1;
        this.renderdata.rf2 = r.rf2;
        this.renderdata.rf3 = r.rf3;
        this.renderdata.rf4 = r.rf4;
        this.renderdata.ri1 = r.ri1;
        this.renderdata.ri2 = r.ri2;
        this.renderdata.ri3 = r.ri3;
        this.renderdata.ri4 = r.ri4;
    }

    @Override
    public boolean fireImmune() {
        // gold field_70178_ae = true (immune to fire)
        return true;
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        // gold: if noDespawnRequired → false; else was_spawnered==0
        if (this.isPersistenceRequired()) {
            return false;
        }
        return this.wasSpawnered == 0;
    }

    @Override
    public void tick() {
        if (this.getAttribute(Attributes.MOVEMENT_SPEED) != null) {
            this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(this.moveSpeed);
        }
        super.tick();

        // gold: daytime despawn if not persistent and not spawnered
        if (!this.isPersistenceRequired() && this.wasSpawnered == 0) {
            long t = this.level().getDayTime() % 24000L;
            if (t < 12000L && this.random.nextInt(400) == 1) {
                this.discard();
            }
        }
    }

    /**
     * Gold {@code onLivingUpdate}: 1/3 flame particles; if water + 1/5 self-attack + smoke.
     */
    @Override
    public void aiStep() {
        super.aiStep();
        if (this.random.nextInt(3) != 1) {
            return;
        }
        if (this.level().isClientSide) {
            this.level()
                    .addParticle(
                            ParticleTypes.FLAME,
                            this.getX(),
                            this.getY() + 0.75,
                            this.getZ(),
                            0.0,
                            this.random.nextFloat() / 10.0F,
                            0.0);
            if (this.isInWater() && this.random.nextInt(5) == 1) {
                this.level()
                        .addParticle(
                                ParticleTypes.SMOKE,
                                this.getX(),
                                this.getY() + 1.75,
                                this.getZ(),
                                0.0,
                                this.random.nextFloat() / 10.0F,
                                0.0);
                this.level()
                        .addParticle(
                                ParticleTypes.LARGE_SMOKE,
                                this.getX(),
                                this.getY() + 1.75,
                                this.getZ(),
                                0.0,
                                this.random.nextFloat() / 10.0F,
                                0.0);
                this.level()
                        .addParticle(
                                ParticleTypes.SMOKE,
                                this.getX(),
                                this.getY() + 2.0,
                                this.getZ(),
                                0.0,
                                this.random.nextFloat() / 10.0F,
                                0.0);
                this.level()
                        .addParticle(
                                ParticleTypes.LARGE_SMOKE,
                                this.getX(),
                                this.getY() + 2.0,
                                this.getZ(),
                                0.0,
                                this.random.nextFloat() / 10.0F,
                                0.0);
            }
        } else if (this.isInWater() && this.random.nextInt(5) == 1) {
            // gold: attackEntityAsMob(self) when wet
            this.doHurtTarget(this);
        }
    }

    public int mygetMaxHealth() {
        return 25;
    }

    public int getAttacking() {
        return this.entityData.get(ATTACKING);
    }

    public void setAttacking(int value) {
        this.entityData.set(ATTACKING, (byte) value);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        // gold: orespawn:kyuubi_living
        return SoundsHandler.ENTITY_KYUUBI_LIVING.get();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        // gold: orespawn:glasshit (not wired) — glass break stand-in
        return SoundEvents.GLASS_BREAK;
    }

    @Override
    protected SoundEvent getDeathSound() {
        // gold: orespawn:glassdead — glass break stand-in
        return SoundEvents.GLASS_BREAK;
    }

    @Override
    protected float getSoundVolume() {
        return 1.1F;
    }

    @Override
    public float getVoicePitch() {
        return 1.25F;
    }

    @Override
    protected void dropCustomDeathLoot(ServerLevel level, DamageSource damageSource, boolean recentlyHit) {
        super.dropCustomDeathLoot(level, damageSource, recentlyHit);
        // gold getDropItem: nextInt(3) → pink ingot / crystal apple / null
        int i = this.random.nextInt(3);
        if (i == 1) {
            this.spawnAtLocation(new ItemStack(ModItems.CRYSTAL_PINK_INGOT.get()));
        } else if (i == 2) {
            this.spawnAtLocation(new ItemStack(ModItems.CRYSTAL_APPLE.get()));
        }
    }

    @Override
    public boolean doHurtTarget(Entity target) {
        // gold: setFire(5) then super
        target.igniteForSeconds(5);
        return GoldStyleCombat.dealAttributeDamage(this, target);
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        // gold: ignore cactus
        if ("cactus".equals(source.getMsgId())) {
            return false;
        }
        return super.hurt(source, amount);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("WasSpawnered", this.wasSpawnered);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.wasSpawnered = tag.getInt("WasSpawnered");
    }

    /**
     * Gold {@code updateAITasks}: nextInt(8)==0; scan 16×3×16; melee distSq&lt;8;
     * hit nextInt(7)==0||nextInt(8)==1; path 1.2.
     */
    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
        if (this.isDeadOrDying()) {
            return;
        }
        if (this.random.nextInt(8) != 0) {
            return;
        }
        LivingEntity e = this.findSomethingToAttack();
        if (e != null) {
            if (this.distanceToSqr(e) < 8.0) {
                this.setAttacking(1);
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

    private boolean isSuitableTarget(LivingEntity target) {
        if (target == null || target == this || !target.isAlive()) {
            return false;
        }
        // gold MyUtils.isIgnoreable deferred
        if (!this.hasLineOfSight(target)) {
            return false;
        }
        // gold exclusions: Vortex, Rotator (not ported), Peacock, CrystalCow (not ported),
        // Irukandji, Skate, Whale, Flounder, Urchin
        if (target instanceof Peacock) {
            return false;
        }
        if (target instanceof Irukandji) {
            return false;
        }
        if (target instanceof Skate) {
            return false;
        }
        if (target instanceof Whale) {
            return false;
        }
        if (target instanceof Flounder) {
            return false;
        }
        if (target instanceof Urchin) {
            return false;
        }
        if (target instanceof Player p) {
            return !p.getAbilities().instabuild && !p.isSpectator();
        }
        return true;
    }

    @Nullable
    private LivingEntity findSomethingToAttack() {
        if (OreSpawnMain.PlayNicely != 0) {
            return null;
        }
        List<LivingEntity> list = this.level()
                .getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(16.0, 3.0, 16.0));
        list.sort(Comparator.comparingDouble(this::distanceToSqr));
        for (LivingEntity living : list) {
            if (this.isSuitableTarget(living)) {
                return living;
            }
        }
        return null;
    }

    /**
     * Gold {@code getCanSpawnHere}: Crystal Urchin spawner sets was_spawnered;
     * need ≥6 air at y+1 around, light, night (daytime ≥ 13000).
     */
    @Override
    public boolean checkSpawnRules(LevelAccessor level, MobSpawnType spawnType) {
        // natural spawn: air clearance + light + night
        int sc = 0;
        int baseX = (int) this.getX();
        int baseY = (int) this.getY();
        int baseZ = (int) this.getZ();
        for (int k = -1; k <= 1; k++) {
            for (int j = -1; j <= 1; j++) {
                if (level.getBlockState(new net.minecraft.core.BlockPos(baseX + j, baseY + 1, baseZ + k)).isAir()) {
                    sc++;
                }
            }
        }
        if (sc < 6) {
            return false;
        }
        if (!super.checkSpawnRules(level, spawnType)) {
            return false;
        }
        long t = level instanceof Level lvl ? lvl.getDayTime() % 24000L : 0L;
        return t >= 13000L;
    }

    /** Called when spawner egg / Crystal Urchin spawner (gold was_spawnered=1). */
    public void setWasSpawnered(int value) {
        this.wasSpawnered = value;
    }

    public int getWasSpawnered() {
        return this.wasSpawnered;
    }
}
