package danger.orespawn.entity;

import danger.orespawn.OreSpawnMain;
import danger.orespawn.init.ModDimensions;
import danger.orespawn.init.ModItems;
import danger.orespawn.util.ai.GoldStyleCombat;
import danger.orespawn.util.ai.WanderALotGoal;
import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
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
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.Nullable;

/**
 * Gold {@code DungeonBeast} (EntityMob) 1:1 for NeoForge 1.21.1.
 * Size 1.15×1.1, speed 0.29, health 65, attack 6, armor 12, XP 60.
 * Crystal-dungeon hunter; proximity combat in {@link #customServerAiStep()}.
 */
public class DungeonBeast extends Monster {
    private static final EntityDataAccessor<Byte> ATTACKING =
            SynchedEntityData.defineId(DungeonBeast.class, EntityDataSerializers.BYTE);

    private final float moveSpeed = 0.29F;
    /** Gold client animation state for {@code ModelDungeonBeast} jaw phase. */
    private RenderInfo renderdata = new RenderInfo();

    public DungeonBeast(EntityType<? extends DungeonBeast> type, Level level) {
        super(type, level);
        this.xpReward = 60; // gold field_70728_aV
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

    public static AttributeSupplier.Builder createAttributes() {
        // gold DungeonBeast_stats default: health 65, defense 12, attack 6
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 65.0)
                .add(Attributes.MOVEMENT_SPEED, 0.29)
                .add(Attributes.ATTACK_DAMAGE, 6.0)
                .add(Attributes.ARMOR, 12.0)
                .add(Attributes.FOLLOW_RANGE, 16.0); // gold field_70174_ab = 10; combat scan 16
    }

    @Override
    protected void registerGoals() {
        // gold: Swimming, WanderALot(14,1), WatchClosest(Player,8), LookIdle, HurtByTarget
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
        return 65;
    }

    public int getAttacking() {
        return this.entityData.get(ATTACKING);
    }

    public void setAttacking(int value) {
        this.entityData.set(ATTACKING, (byte) value);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        //  gold: null
        return null;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        //  gold orespawn:dbhit
        return null;
    }

    @Override
    protected SoundEvent getDeathSound() {
        //  gold orespawn:dbdead
        return null;
    }

    @Override
    protected float getSoundVolume() {
        return 0.8F;
    }

    @Override
    public float getVoicePitch() {
        return 1.0F;
    }

    @Override
    protected void dropCustomDeathLoot(ServerLevel level, DamageSource damageSource, boolean recentlyHit) {
        super.dropCustomDeathLoot(level, damageSource, recentlyHit);
        // gold getDropItem: nextInt(4) → 1 pink ingot, 2 crystal apple, 3 oak log, else null
        int i = this.random.nextInt(4);
        if (i == 1) {
            this.spawnAtLocation(new ItemStack(ModItems.CRYSTAL_PINK_INGOT.get()));
        } else if (i == 2) {
            // gold MyCrystalApple
            this.spawnAtLocation(new ItemStack(ModItems.CRYSTAL_APPLE.get()));
        } else if (i == 3) {
            this.spawnAtLocation(new ItemStack(Blocks.OAK_LOG));
        }
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        // gold interact returns false always
        return InteractionResult.PASS;
    }

    @Override
    public boolean doHurtTarget(Entity target) {
        return GoldStyleCombat.dealAttributeDamage(this, target);
    }

    /**
     * Gold {@code updateAITasks}: combat 1/8; setAttacking; melee distSq &lt; 8;
     * hit nextInt(7)==0 || nextInt(8)==1; path 1.2 when farther.
     */
    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
        if (this.isDeadOrDying()) {
            return;
        }
        if (this.random.nextInt(8) == 0) {
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
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        // gold: ignore inWall entirely; ignore cactus; else super
        if (source.is(DamageTypes.IN_WALL) || source.is(DamageTypes.CACTUS)) {
            return false;
        }
        return super.hurt(source, amount);
    }

    private boolean isSuitableTarget(LivingEntity target) {
        if (target == null || target == this || !target.isAlive()) {
            return false;
        }
        // gold MyUtils.isIgnoreable deferred
        if (!this.hasLineOfSight(target)) {
            return false;
        }
        // gold skip: Rat, DungeonBeast, Rotator (unported), Peacock, Irukandji, Skate, Whale, Flounder
        if (target instanceof Rat) {
            return false;
        }
        if (target instanceof DungeonBeast) {
            return false;
        }
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
        // gold box expand(16, 3, 16)
        return GoldStyleCombat.findTarget(this, 16.0, 3.0, this::isSuitableTarget);
    }

    /**
     * Gold {@code getCanSpawnHere}: named "Dungeon Beast" spawner OK; else valid light;
     * in DimensionID5 (crystal): y in [25,28] and ≥6 air in 3×3 at feet+1.
     */
    @Override
    public boolean checkSpawnRules(LevelAccessor level, MobSpawnType spawnType) {
        // gold spawner name "Dungeon Beast" early-return true
        if (spawnType == MobSpawnType.SPAWNER) {
            return true;
        }
        if (!super.checkSpawnRules(level, spawnType)) {
            return false;
        }
        if (level instanceof Level lvl && lvl.dimension() == ModDimensions.CRYSTAL) {
            double y = this.getY();
            if (y > 28.0 || y < 25.0) {
                return false;
            }
            int sc = 0;
            BlockPos base = this.blockPosition();
            for (int k = -1; k <= 1; k++) {
                for (int j = -1; j <= 1; j++) {
                    if (level.getBlockState(base.offset(j, 1, k)).isAir()) {
                        sc++;
                    }
                }
            }
            if (sc < 6) {
                return false;
            }
        }
        return true;
    }
}
