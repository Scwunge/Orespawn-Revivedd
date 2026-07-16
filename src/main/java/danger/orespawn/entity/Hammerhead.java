package danger.orespawn.entity;

import danger.orespawn.OreSpawnMain;
import danger.orespawn.util.ai.GoldStyleCombat;
import danger.orespawn.util.ai.WanderALotGoal;
import danger.orespawn.util.handlers.SoundsHandler;
import java.util.Comparator;
import java.util.List;
import net.minecraft.core.BlockPos;
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
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import org.jetbrains.annotations.Nullable;

/**
 * Gold {@code Hammerhead} (EntityMob). Size 3.0×5.0, speed 0.35, XP 350.
 * Stats defaults {@code get_mobstats("Hammerhead", 240, 75, 20)}.
 * Night ground boss; revenge-target + proximity combat; knockback on hit.
 * Registry size set in {@code ModEntities} .
 */
public class Hammerhead extends Monster {
    private static final EntityDataAccessor<Byte> ATTACKING =
            SynchedEntityData.defineId(Hammerhead.class, EntityDataSerializers.BYTE);

    private final float moveSpeed = 0.35F;
    /** Last living entity that damaged us (gold {@code rt}). */
    @Nullable
    private LivingEntity revengeTargetLiving = null;

    public Hammerhead(EntityType<? extends Hammerhead> type, Level level) {
        super(type, level);
        this.xpReward = 350; // gold field_70728_aV
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 240.0) // Hammerhead_stats.health default
                .add(Attributes.MOVEMENT_SPEED, 0.35)
                .add(Attributes.ATTACK_DAMAGE, 75.0) // Hammerhead_stats.attack default
                .add(Attributes.ARMOR, 20.0) // Hammerhead_stats.defense default
                .add(Attributes.FOLLOW_RANGE, 100.0); // gold field_70174_ab = 100
    }

    @Override
    protected void registerGoals() {
        // gold: Swimming, MoveThroughVillage(skipped), WanderALot(16,1.0), WatchClosest(Living,8), LookIdle, HurtByTarget
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(2, new WanderALotGoal(this, 16, 1.0));
        this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(ATTACKING, (byte) 0);
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
        return 240;
    }

    public int getAttacking() {
        return this.entityData.get(ATTACKING);
    }

    public void setAttacking(int value) {
        this.entityData.set(ATTACKING, (byte) value);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        // gold: orespawn:hammerhead_living (ogg present; SoundsHandler wiring deferred)
        return this.random.nextInt(3) == 0 ? SoundsHandler.ENTITY_ALOSAURUS_LIVING.get() : null;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        // gold: orespawn:alo_hurt
        return SoundsHandler.ENTITY_ALOSAURUS_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        // gold: orespawn:hammerhead_death (ogg present; stand-in until SoundsHandler wire)
        return SoundsHandler.ENTITY_ALOSAURUS_DEATH.get();
    }

    @Override
    protected float getSoundVolume() {
        return 1.2F;
    }

    @Override
    public float getVoicePitch() {
        return 0.9F;
    }

    @Override
    protected void dropCustomDeathLoot(ServerLevel level, DamageSource damageSource, boolean recentlyHit) {
        super.dropCustomDeathLoot(level, damageSource, recentlyHit);
        // gold: 8 bone meal, 10 experience catcher, 16 creeper launcher, 4 creeper repellent,
        // 6 steak, 2 experience tree seed, 1/3 hammy — port stand-ins for unported items
        for (int i = 0; i < 8; i++) {
            this.spawnAtLocation(new ItemStack(Items.BONE_MEAL));
        }
        for (int i = 0; i < 10; i++) {
            this.spawnAtLocation(new ItemStack(Items.EXPERIENCE_BOTTLE));
        }
        for (int i = 0; i < 16; i++) {
            this.spawnAtLocation(new ItemStack(Items.GUNPOWDER));
        }
        for (int i = 0; i < 4; i++) {
            this.spawnAtLocation(new ItemStack(Items.SLIME_BALL));
        }
        for (int i = 0; i < 6; i++) {
            this.spawnAtLocation(new ItemStack(Items.COOKED_BEEF));
        }
        for (int i = 0; i < 2; i++) {
            this.spawnAtLocation(new ItemStack(Items.OAK_SAPLING));
        }
        if (this.random.nextInt(3) == 1) {
            this.spawnAtLocation(new ItemStack(Items.IRON_AXE)); // MyHammy stand-in
        }
    }

    @Override
    public boolean doHurtTarget(Entity target) {
        // gold attackEntityAsMob: super then knockback ks=1.1 inair=0.85 (*2 if dead/player)
        if (!GoldStyleCombat.dealAttributeDamage(this, target)) {
            return false;
        }
        if (target instanceof LivingEntity) {
            double ks = 1.1;
            double inair = 0.85;
            float f3 = (float) Math.atan2(target.getZ() - this.getZ(), target.getX() - this.getX());
            if (!target.isAlive() || target instanceof Player) {
                inair *= 2.0;
            }
            target.push(Math.cos(f3) * ks, inair, Math.sin(f3) * ks);
        }
        return true;
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        // gold: ignore cactus damage string
        if ("cactus".equals(source.getMsgId())) {
            return false;
        }
        boolean ret = super.hurt(source, amount);
        Entity e = source.getEntity();
        if (e instanceof LivingEntity living) {
            this.revengeTargetLiving = living;
        }
        return ret;
    }

    /**
     * Gold {@code updateAITasks}: nextInt(3)==1; prefer rt (PlayNicely=0); clear dead/1/250/no LOS;
     * scan 18×9×18; reach 7+w/2; hit nextInt(3)==1||nextInt(4)==1; path 1.25.
     */
    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
        if (this.isDeadOrDying()) {
            return;
        }
        if (this.random.nextInt(3) != 1) {
            return;
        }

        LivingEntity e = this.revengeTargetLiving;
        if (OreSpawnMain.PlayNicely != 0) {
            e = null;
        }
        if (e != null) {
            if (!e.isAlive() || this.random.nextInt(250) == 1) {
                e = null;
                this.revengeTargetLiving = null;
            }
            if (e != null && !this.hasLineOfSight(e)) {
                e = null;
            }
        }
        if (e == null) {
            e = this.findSomethingToAttack();
        }
        if (e != null) {
            this.getLookControl().setLookAt(e, 10.0F, 10.0F);
            // gold: distanceSq < (7.0F + e.width/2)^2
            if (GoldStyleCombat.inMeleeRange(this, e, 7.0)) {
                this.setAttacking(1);
                if (this.random.nextInt(3) == 1 || this.random.nextInt(4) == 1) {
                    this.doHurtTarget(e);
                }
            } else {
                this.getNavigation().moveTo(e, 1.25);
            }
        } else {
            this.setAttacking(0);
        }
    }

    private boolean isSuitableTarget(LivingEntity target) {
        if (target == null || target == this || !target.isAlive()) {
            return false;
        }
        if (!this.hasLineOfSight(target)) {
            return false;
        }
        if (target instanceof Hammerhead) {
            return false;
        }
        if (target instanceof Player p) {
            return !p.getAbilities().instabuild && !p.isSpectator();
        }
        // gold: EntityMob → true; else MyUtils.isAttackableNonMob (animals/villagers etc.)
        if (target instanceof Monster) {
            return true;
        }
        if (target instanceof Animal || target instanceof Villager) {
            return true;
        }
        return false;
    }

    @Nullable
    private LivingEntity findSomethingToAttack() {
        if (OreSpawnMain.PlayNicely != 0) {
            return null;
        }
        List<LivingEntity> list = this.level()
                .getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(18.0, 9.0, 18.0));
        list.sort(Comparator.comparingDouble(this::distanceToSqr));
        for (LivingEntity living : list) {
            if (this.isSuitableTarget(living)) {
                return living;
            }
        }
        return null;
    }

    /**
     * Gold {@code getCanSpawnHere}: spawner name "Hammerhead" bypass;
     * light (super), y≥50, night, air column y+1.y+5 at offsets (-1.0), no buddy in 16×8×16.
     */
    @Override
    public boolean checkSpawnRules(LevelAccessor level, MobSpawnType spawnType) {
        if (!super.checkSpawnRules(level, spawnType)) {
            return false;
        }
        if (this.getY() < 50.0) {
            return false;
        }
        if (level instanceof Level lvl && lvl.isDay()) {
            return false;
        }
        int baseX = (int) this.getX();
        int baseY = (int) this.getY();
        int baseZ = (int) this.getZ();
        BlockPos.MutableBlockPos cursor = new BlockPos.MutableBlockPos();
        for (int k = -1; k < 1; k++) {
            for (int j = -1; j < 1; j++) {
                for (int i = 1; i < 6; i++) {
                    cursor.set(baseX + j, baseY + i, baseZ + k);
                    if (!level.getBlockState(cursor).isAir()) {
                        return false;
                    }
                }
            }
        }
        return this.level()
                .getEntitiesOfClass(Hammerhead.class, this.getBoundingBox().inflate(16.0, 8.0, 16.0), e -> e != this)
                .isEmpty();
    }
}
