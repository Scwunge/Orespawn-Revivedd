package danger.orespawn.entity;

import danger.orespawn.OreSpawnMain;
import danger.orespawn.init.ModItems;
import danger.orespawn.util.ai.WanderALotGoal;
import danger.orespawn.util.handlers.SoundsHandler;
import java.util.Comparator;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.Difficulty;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.entity.monster.Spider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

/**
 * Gold {@code Lizard} (EntityCannonFodder / EntityTameable) 1:1 for NeoForge 1.21.1.
 * Size 1.5×1.25, speed 0.3, health 30, attack 6, armor 5, XP 15, follow 3.
 * Water-seek when dry; hunts AttackSquid / spiders / chickens; ink-sac buddy follow;
 * breed item gold CrystalApple → golden apple stand-in. Cannon-fodder hat activation deferred.
 */
public class Lizard extends Animal {
    private static final EntityDataAccessor<Byte> ATTACKING =
            SynchedEntityData.defineId(Lizard.class, EntityDataSerializers.BYTE);

    private final float moveSpeed = 0.3F;
    /** Gold {@code should_despawn} — false while follow_time &gt; 0. */
    private boolean shouldDespawn = true;
    @Nullable
    private LivingEntity buddy;
    private int followTime;
    private int closest = 99999;
    private int tx;
    private int ty;
    private int tz;

    public Lizard(EntityType<? extends Lizard> type, Level level) {
        super(type, level);
        this.xpReward = 15; // gold field_70728_aV
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Animal.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 30.0) // mygetMaxHealth()
                .add(Attributes.MOVEMENT_SPEED, 0.3)
                .add(Attributes.ATTACK_DAMAGE, 6.0) // gold attr + attackEntityAsMob
                .add(Attributes.ARMOR, 5.0) // gold getTotalArmorValue 5
                .add(Attributes.FOLLOW_RANGE, 3.0); // gold field_70174_ab = 3
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(ATTACKING, (byte) 0);
    }

    @Override
    protected void registerGoals() {
        // gold: Swimming, FollowOwner (cannon-fodder tame deferred), Mate,
        // Tempt ink_sac 1.25, WanderALot(16,1), WatchClosest, LookIdle, HurtByTarget
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(2, new BreedGoal(this, 1.0));
        this.goalSelector.addGoal(3, new TemptGoal(this, 1.25, Ingredient.of(Items.INK_SAC), false));
        this.goalSelector.addGoal(4, new WanderALotGoal(this, 16, 1.0));
        this.goalSelector.addGoal(5, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(5, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
    }

    @Override
    public void tick() {
        if (this.getAttribute(Attributes.MOVEMENT_SPEED) != null) {
            this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(this.moveSpeed);
        }
        super.tick();
    }

    public int mygetMaxHealth() {
        return 30;
    }

    public final int getAttacking() {
        return this.entityData.get(ATTACKING);
    }

    public final void setAttacking(int value) {
        this.entityData.set(ATTACKING, (byte) value);
    }

    /**
     * Gold EntityCannonFodder {@code get_is_activated} — hat mesh gate.
     * Full hat/activation system deferred; always 0 (no hat).
     */
    public int get_is_activated() {
        return 0;
    }

    /**
     * Gold EntityCannonFodder {@code getHatColor} — texture2/3 when activated.
     */
    public int getHatColor() {
        return 0;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        //  gold null
        return null;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        // gold orespawn:alo_hurt
        return SoundsHandler.ENTITY_ALOSAURUS_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        // gold orespawn:alo_death
        return SoundsHandler.ENTITY_ALOSAURUS_DEATH.get();
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
    public boolean doHurtTarget(Entity target) {
        // gold attackEntityAsMob: fixed 6.0
        return target.hurt(this.damageSources().mobAttack(this), 6.0F);
    }

    /** Gold: ignore cactus; on living source set attack target; clear follow_time. */
    @Override
    public boolean hurt(DamageSource source, float amount) {
        if ("cactus".equals(source.getMsgId())) {
            return false;
        }
        boolean ret = super.hurt(source, amount);
        Entity e = source.getEntity();
        if (e instanceof LivingEntity living) {
            this.setTarget(living);
            this.setLastHurtByMob(living);
        }
        this.followTime = 0;
        return ret;
    }

    /**
     * Gold interact: super first (cannon-fodder hat deferred → pass).
     * Ink sac within 16: buddy=player, follow_time 3000+rand(2000), hearts.
     * Else clear buddy/follow and smoke particles.
     */
    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (stack.isEmpty()) {
            // gold empty-hand clears buddy
            if (!this.level().isClientSide) {
                this.buddy = null;
                this.followTime = 0;
            }
            this.playTameEffect(false);
            return InteractionResult.sidedSuccess(this.level().isClientSide);
        }

        if (stack.is(Items.INK_SAC) && this.distanceToSqr(player) < 16.0) {
            if (!this.level().isClientSide) {
                this.buddy = player;
                this.followTime = 3000 + this.random.nextInt(2000);
            }
            this.playTameEffect(true);
            if (!player.getAbilities().instabuild) {
                stack.shrink(1);
            }
            return InteractionResult.sidedSuccess(this.level().isClientSide);
        }

        // breeding food handled by Animal super when in love
        InteractionResult breed = super.mobInteract(player, hand);
        if (breed.consumesAction()) {
            return breed;
        }

        if (!this.level().isClientSide) {
            this.buddy = null;
            this.followTime = 0;
        }
        this.playTameEffect(false);
        return InteractionResult.sidedSuccess(this.level().isClientSide);
    }

    /** Gold {@code playTameEffect} hearts vs smoke. */
    private void playTameEffect(boolean success) {
        if (this.level().isClientSide) {
            for (int i = 0; i < 7; i++) {
                double dx = this.random.nextGaussian() * 0.02;
                double dy = this.random.nextGaussian() * 0.02;
                double dz = this.random.nextGaussian() * 0.02;
                this.level()
                        .addParticle(
                                success ? ParticleTypes.HEART : ParticleTypes.SMOKE,
                                this.getX()
                                        + (double) (this.random.nextFloat() * this.getBbWidth() * 2.0F)
                                        - (double) this.getBbWidth(),
                                this.getY() + 0.5 + (double) (this.random.nextFloat() * this.getBbHeight()),
                                this.getZ()
                                        + (double) (this.random.nextFloat() * this.getBbWidth() * 2.0F)
                                        - (double) this.getBbWidth(),
                                dx,
                                dy,
                                dz);
            }
        }
    }

    private boolean isWater(BlockState state) {
        return state.is(Blocks.WATER);
    }

    /** Gold {@code scan_it} — expanding shell search for water / flowing water. */
    private boolean scanIt(int x, int y, int z, int dx, int dy, int dz) {
        int found = 0;

        for (int i = -dy; i <= dy; i++) {
            for (int j = -dz; j <= dz; j++) {
                BlockState bid = this.level().getBlockState(new BlockPos(x + dx, y + i, z + j));
                if (this.isWater(bid)) {
                    int d = dx * dx + j * j + i * i;
                    if (d < this.closest) {
                        this.closest = d;
                        this.tx = x + dx;
                        this.ty = y + i;
                        this.tz = z + j;
                        found++;
                    }
                }
                bid = this.level().getBlockState(new BlockPos(x - dx, y + i, z + j));
                if (this.isWater(bid)) {
                    int d = dx * dx + j * j + i * i;
                    if (d < this.closest) {
                        this.closest = d;
                        this.tx = x - dx;
                        this.ty = y + i;
                        this.tz = z + j;
                        found++;
                    }
                }
            }
        }

        for (int xi = -dx; xi <= dx; xi++) {
            for (int j = -dz; j <= dz; j++) {
                BlockState bid = this.level().getBlockState(new BlockPos(x + xi, y + dy, z + j));
                if (this.isWater(bid)) {
                    int d = dy * dy + j * j + xi * xi;
                    if (d < this.closest) {
                        this.closest = d;
                        this.tx = x + xi;
                        this.ty = y + dy;
                        this.tz = z + j;
                        found++;
                    }
                }
                bid = this.level().getBlockState(new BlockPos(x + xi, y - dy, z + j));
                if (this.isWater(bid)) {
                    int d = dy * dy + j * j + xi * xi;
                    if (d < this.closest) {
                        this.closest = d;
                        this.tx = x + xi;
                        this.ty = y - dy;
                        this.tz = z + j;
                        found++;
                    }
                }
            }
        }

        for (int xi = -dx; xi <= dx; xi++) {
            for (int j = -dy; j <= dy; j++) {
                BlockState bid = this.level().getBlockState(new BlockPos(x + xi, y + j, z + dz));
                if (this.isWater(bid)) {
                    int d = dz * dz + j * j + xi * xi;
                    if (d < this.closest) {
                        this.closest = d;
                        this.tx = x + xi;
                        this.ty = y + j;
                        this.tz = z + dz;
                        found++;
                    }
                }
                bid = this.level().getBlockState(new BlockPos(x + xi, y + j, z - dz));
                if (this.isWater(bid)) {
                    int d = dz * dz + j * j + xi * xi;
                    if (d < this.closest) {
                        this.closest = d;
                        this.tx = x + xi;
                        this.ty = y + j;
                        this.tz = z - dz;
                        found++;
                    }
                }
            }
        }

        return found != 0;
    }

    /**
     * Gold {@code updateAITasks}: follow_time countdown; dry water-seek 1/100;
     * heal 1/300 when hurt; combat 1/10 non-peaceful; buddy path while following.
     */
    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
        if (this.isDeadOrDying()) {
            return;
        }

        if (this.followTime > 0) {
            this.followTime--;
            this.shouldDespawn = false;
        } else {
            this.shouldDespawn = true;
        }

        // gold: not in water, 1/100 seek water shells 1.13
        if (!this.isInWater() && this.random.nextInt(100) == 0) {
            this.closest = 99999;
            this.tx = this.ty = this.tz = 0;

            for (int i = 1; i < 14; i++) {
                int j = i;
                if (j > 5) {
                    j = 5;
                }
                if (this.scanIt((int) this.getX(), (int) this.getY() - 1, (int) this.getZ(), i, j, i)) {
                    break;
                }
                if (i >= 5) {
                    i++;
                }
            }

            if (this.closest < 99999) {
                this.getNavigation().moveTo(this.tx, this.ty - 1, this.tz, 1.33);
            }
        }

        if (this.getHealth() < this.mygetMaxHealth() && this.random.nextInt(300) == 1) {
            this.heal(1.0F);
        }

        if (this.level().getDifficulty() != Difficulty.PEACEFUL && this.random.nextInt(10) == 1) {
            LivingEntity e = this.findSomethingToAttack();
            if (e != null) {
                this.followTime = 0;
                // gold distSq < 12.0
                if (this.distanceToSqr(e) < 12.0) {
                    this.setAttacking(1);
                    if (this.random.nextInt(4) == 0 || this.random.nextInt(5) == 1) {
                        this.doHurtTarget(e);
                    }
                } else {
                    this.getNavigation().moveTo(e, 1.2);
                }
            } else {
                if (this.buddy != null && this.buddy.isAlive() && this.random.nextInt(15) == 1) {
                    this.getNavigation().moveTo(this.buddy, 1.0);
                }
                this.setAttacking(0);
            }
        }

        if (this.buddy != null
                && this.buddy.isAlive()
                && this.followTime > 0
                && this.random.nextInt(20) == 1) {
            this.getNavigation().moveTo(this.buddy, 1.0);
        }
    }

    private boolean isSuitableTarget(LivingEntity target) {
        if (this.level().getDifficulty() == Difficulty.PEACEFUL) {
            return false;
        }
        if (target == null || target == this || !target.isAlive()) {
            return false;
        }
        if (!this.hasLineOfSight(target)) {
            return false;
        }
        // gold AttackSquid
        if (target instanceof AttackSquid) {
            return true;
        }
        // gold EntitySpider + EntityCaveSpider (CaveSpider extends Spider)
        if (target instanceof Spider) {
            return true;
        }
        if (target instanceof Chicken) {
            return true;
        }
        // gold: other Lizard 1/10 while not following → buddy (not attack)
        if (target instanceof Lizard && this.random.nextInt(10) == 1 && this.followTime <= 0) {
            this.buddy = target;
        }
        return false;
    }

    @Nullable
    private LivingEntity findSomethingToAttack() {
        if (OreSpawnMain.PlayNicely != 0) {
            return null;
        }
        // gold: 1/100 clear attack target
        if (this.random.nextInt(100) == 0) {
            this.setTarget(null);
        }
        LivingEntity current = this.getTarget();
        if (current != null && current.isAlive()) {
            return current;
        }
        this.setTarget(null);

        // gold box expand(12, 4, 12), GenericTargetSorter → nearest first
        List<LivingEntity> list = this.level()
                .getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(12.0, 4.0, 12.0));
        list.sort(Comparator.comparingDouble(this::distanceToSqr));
        for (LivingEntity living : list) {
            if (this.isSuitableTarget(living)) {
                return living;
            }
        }
        return null;
    }

    /** Gold {@code getCanSpawnHere}: y ≥ 50. */
    @Override
    public boolean checkSpawnRules(LevelAccessor level, MobSpawnType spawnType) {
        return !(this.getY() < 50.0);
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        // gold canDespawn: baby → persist; noDespawnRequired false; tamed false; else should_despawn
        if (this.isBaby()) {
            this.setPersistenceRequired();
            return false;
        }
        if (this.isPersistenceRequired()) {
            return false;
        }
        return this.shouldDespawn;
    }

    @Override
    public boolean isFood(ItemStack stack) {
        // gold isBreedingItem: MyCrystalApple
        return stack.is(ModItems.CRYSTAL_APPLE.get());
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob other) {
        // avoid ModEntities dependency until LIZARD
        return (AgeableMob) this.getType().create(level);
    }
}
