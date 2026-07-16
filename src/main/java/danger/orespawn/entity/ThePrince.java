package danger.orespawn.entity;

import danger.orespawn.OreSpawnMain;
import danger.orespawn.entity.tame.OreSpawnPet;
import danger.orespawn.util.ai.FollowOwnerGoal;
import danger.orespawn.util.ai.WanderALotGoal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.Difficulty;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
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
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.SmallFireball;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

/**
 * Gold {@code ThePrince} (EntityTameable) ported for NeoForge 1.21.1.
 * Size 0.75×1.25, speed 0.32, health 500, attack 10, armor 16, XP 50.
 * Three-headed flyer: activity 1 ground / 2 fly, fire/lightning/ice canons,
 * diamond-block instant tame, growth to Young Prince deferred.
 * Texture: {@code textures/entity/theprincetexture.png}.
 * Registry size/attrs set in {@code ModEntities}.
 */
public class ThePrince extends Monster {
    private static final EntityDataAccessor<Byte> ACTIVITY =
            SynchedEntityData.defineId(ThePrince.class, EntityDataSerializers.BYTE);
    private static final EntityDataAccessor<Byte> FIRE =
            SynchedEntityData.defineId(ThePrince.class, EntityDataSerializers.BYTE);
    private static final EntityDataAccessor<Byte> ATTACKING =
            SynchedEntityData.defineId(ThePrince.class, EntityDataSerializers.BYTE);
    private static final EntityDataAccessor<Byte> PET_FLAGS =
            SynchedEntityData.defineId(ThePrince.class, EntityDataSerializers.BYTE);
    private static final EntityDataAccessor<Optional<UUID>> OWNER =
            SynchedEntityData.defineId(ThePrince.class, EntityDataSerializers.OPTIONAL_UUID);

    public static final float GOLD_WIDTH = 0.75F;
    public static final float GOLD_HEIGHT = 1.25F;
    public static final double GOLD_HEALTH = 500.0;
    public static final double GOLD_SPEED = 0.32;
    public static final double GOLD_ATTACK = 10.0;
    public static final double GOLD_ARMOR = 16.0;
    public static final int GOLD_XP = 50;
    public static final double GOLD_FOLLOW = 16.0;

    private final float moveSpeed = 0.32F;

    @Nullable
    private BlockPos currentFlightTarget;
    /** Gold public activity mirror (1=ground, 2=fly). */
    public int activity = 1;
    private int owner_flying = 0;
    private int syncit = 0;

    private int head1ext = 0;
    private int head2ext = 0;
    private int head3ext = 0;
    private int head1dir = 1;
    private int head2dir = 1;
    private int head3dir = 1;

    private int ok_to_grow = 0;
    private int kill_count = 0;
    private int fed_count = 0;
    private int day_count = 0;
    private int is_day = 0;

    public ThePrince(EntityType<? extends ThePrince> type, Level level) {
        super(type, level);
        this.xpReward = GOLD_XP;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, GOLD_HEALTH)
                .add(Attributes.MOVEMENT_SPEED, GOLD_SPEED)
                .add(Attributes.ATTACK_DAMAGE, GOLD_ATTACK)
                .add(Attributes.ARMOR, GOLD_ARMOR)
                .add(Attributes.FOLLOW_RANGE, GOLD_FOLLOW);
    }

    /** Gold was EntityTameable — never wipe on Peaceful. */
    @Override
    protected boolean shouldDespawnInPeaceful() {
        return false;
    }

    @Override
    protected void registerGoals() {
        // gold: Swimming, FollowOwner 1.15/12/2, Tempt beef 1.25, WatchClosest Living 6,
        // Wander 0.75, LookIdle, MoveIndoors (skipped)
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(
                2,
                new FollowOwnerGoal(
                        this,
                        () -> OreSpawnPet.getOwnerUUID(this, OWNER),
                        () -> OreSpawnPet.isSitting(this, PET_FLAGS),
                        1.15,
                        12.0F,
                        2.0F));
        this.goalSelector.addGoal(
                3, new TemptGoal(this, 1.25, Ingredient.of(Items.BEEF, Items.COOKED_BEEF), false));
        this.goalSelector.addGoal(4, new LookAtPlayerGoal(this, LivingEntity.class, 6.0F));
        this.goalSelector.addGoal(5, new WanderALotGoal(this, 10, 0.75));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(ACTIVITY, (byte) 1);
        builder.define(FIRE, (byte) 1); // gold data 20 default 1
        builder.define(ATTACKING, (byte) 0);
        builder.define(PET_FLAGS, (byte) 0);
        builder.define(OWNER, Optional.empty());
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("SpyroActivity", this.getActivity());
        tag.putInt("SpyroFire", this.getSpyroFire());
        tag.putInt("SpyroGrow", this.ok_to_grow);
        tag.putInt("SpyroKill", this.kill_count);
        tag.putInt("SpyroFed", this.fed_count);
        tag.putInt("SpyroDay", this.day_count);
        OreSpawnPet.addAdditionalSaveData(this, PET_FLAGS, OWNER, tag);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.contains("SpyroActivity")) {
            this.setActivity(tag.getInt("SpyroActivity"));
        }
        if (tag.contains("SpyroFire")) {
            this.setSpyroFire(tag.getInt("SpyroFire"));
        }
        this.ok_to_grow = tag.getInt("SpyroGrow");
        this.kill_count = tag.getInt("SpyroKill");
        this.fed_count = tag.getInt("SpyroFed");
        this.day_count = tag.getInt("SpyroDay");
        OreSpawnPet.readAdditionalSaveData(this, PET_FLAGS, OWNER, tag);
        if (OreSpawnPet.isTame(this, PET_FLAGS)) {
            this.setPersistenceRequired();
        }
    }

    @Override
    public boolean fireImmune() {
        return true; // gold field_70178_ae
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        // gold canDespawn: false always
        return false;
    }

    public int mygetMaxHealth() {
        return (int) GOLD_HEALTH;
    }

    public int getActivity() {
        int i = this.entityData.get(ACTIVITY) & 0xFF;
        this.activity = i;
        return i;
    }

    public void setActivity(int par1) {
        this.activity = par1;
        this.entityData.set(ACTIVITY, (byte) par1);
    }

    public int getSpyroFire() {
        return this.entityData.get(FIRE) & 0xFF;
    }

    public void setSpyroFire(int par1) {
        this.entityData.set(FIRE, (byte) par1);
    }

    public int getAttacking() {
        return this.entityData.get(ATTACKING) & 0xFF;
    }

    public void setAttacking(int par1) {
        this.entityData.set(ATTACKING, (byte) par1);
    }

    public int getHead1Ext() {
        return this.head1ext;
    }

    public int getHead2Ext() {
        return this.head2ext;
    }

    public int getHead3Ext() {
        return this.head3ext;
    }

    public boolean isOreSpawnTame() {
        return OreSpawnPet.isTame(this, PET_FLAGS);
    }

    public boolean isOreSpawnSitting() {
        return OreSpawnPet.isSitting(this, PET_FLAGS);
    }

    public void set_ok_to_grow() {
        this.ok_to_grow = 1;
        this.kill_count = 0;
        this.fed_count = 0;
        this.day_count = 0;
    }

    private static boolean isBeef(ItemStack stack) {
        return !stack.isEmpty() && (stack.is(Items.BEEF) || stack.is(Items.COOKED_BEEF));
    }

    private static boolean isFood(ItemStack stack) {
        // gold: any ItemFood — use edible flag
        return !stack.isEmpty() && stack.getFoodProperties(null) != null;
    }

    public boolean canSeeTarget(double pX, double pY, double pZ) {
        return this.level()
                        .clip(new ClipContext(
                                new Vec3(this.getX(), this.getY() + 0.75, this.getZ()),
                                new Vec3(pX, pY, pZ),
                                ClipContext.Block.COLLIDER,
                                ClipContext.Fluid.NONE,
                                this))
                        .getType()
                == HitResult.Type.MISS;
    }

    @Override
    public void tick() {
        if (this.getAttribute(Attributes.MOVEMENT_SPEED) != null) {
            this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(this.moveSpeed);
        }
        if (OreSpawnPet.isSitting(this, PET_FLAGS)) {
            this.getNavigation().stop();
        }
        super.tick();

        // gold: activity 2 → noClip
        this.noPhysics = this.getActivity() == 2;

        // head extension wander (client + server; model reads on client)
        if (this.random.nextInt(10) == 1) {
            int i = this.random.nextInt(3);
            if (i == 0) {
                this.head1dir = 2;
            } else if (i == 1) {
                this.head1dir = -2;
            } else {
                this.head1dir = 0;
            }
        }
        if (this.random.nextInt(10) == 1) {
            int i = this.random.nextInt(3);
            if (i == 0) {
                this.head2dir = 2;
            } else if (i == 1) {
                this.head2dir = -2;
            } else {
                this.head2dir = 0;
            }
        }
        if (this.random.nextInt(10) == 1) {
            int i = this.random.nextInt(3);
            if (i == 0) {
                this.head3dir = 2;
            } else if (i == 1) {
                this.head3dir = -2;
            } else {
                this.head3dir = 0;
            }
        }

        this.head1ext += this.head1dir;
        if (this.head1ext < 0) {
            this.head1ext = 0;
        }
        if (this.head1ext > 60) {
            this.head1ext = 60;
        }
        this.head2ext += this.head2dir;
        if (this.head2ext < 0) {
            this.head2ext = 0;
        }
        if (this.head2ext > 60) {
            this.head2ext = 60;
        }
        this.head3ext += this.head3dir;
        if (this.head3ext < 0) {
            this.head3ext = 0;
        }
        if (this.head3ext > 60) {
            this.head3ext = 60;
        }

        if (this.isInWater()) {
            this.setDeltaMovement(this.getDeltaMovement().add(0.0, 0.07, 0.0));
        }

        if (this.currentFlightTarget == null) {
            this.currentFlightTarget = BlockPos.containing(this.getX(), this.getY(), this.getZ());
        }

        this.syncit++;
        if (this.syncit > 20) {
            this.syncit = 0;
            if (this.level().isClientSide) {
                this.getActivity();
            } else {
                int j = this.activity;
                this.setActivity(j);
            }
        }

        if (this.activity == 2) {
            Vec3 m = this.getDeltaMovement();
            this.setDeltaMovement(m.x, m.y * 0.6, m.z);
        }
    }

    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
        if (this.isDeadOrDying()) {
            return;
        }

        if (this.random.nextInt(200) == 1) {
            this.setTarget(null);
        }

        // gold: only run super AI goals when not flying
        // (goals always registered; flight overrides motion in do_movement)

        if (this.random.nextInt(200) == 1 && this.getHealth() < this.mygetMaxHealth()) {
            this.heal(1.0F);
        }

        // gold: auto-tame nearest player within 10 if untamed
        if (!OreSpawnPet.isTame(this, PET_FLAGS)) {
            Player p = this.level().getNearestPlayer(this, 10.0);
            if (p != null) {
                OreSpawnPet.tame(this, PET_FLAGS, OWNER, p);
                this.heal(this.mygetMaxHealth() - this.getHealth());
            }
        }

        if (!OreSpawnPet.isSitting(this, PET_FLAGS)) {
            if (this.activity == 0) {
                this.setActivity(1);
            }

            if (this.random.nextInt(100) == 1) {
                if (this.random.nextInt(20) == 1) {
                    this.setActivity(2);
                } else {
                    this.setActivity(1);
                }
            }

            this.owner_flying = 0;
            if (OreSpawnPet.isTame(this, PET_FLAGS) && OreSpawnPet.getOwner(this, OWNER) != null) {
                LivingEntity owner = OreSpawnPet.getOwner(this, OWNER);
                if (owner instanceof Player player && player.getAbilities().flying) {
                    this.owner_flying = 1;
                    this.setActivity(2);
                }
            }

            if (this.activity == 1
                    && OreSpawnPet.isTame(this, PET_FLAGS)
                    && OreSpawnPet.getOwner(this, OWNER) != null) {
                LivingEntity e = OreSpawnPet.getOwner(this, OWNER);
                if (e != null && this.distanceToSqr(e) > 256.0) {
                    this.setActivity(2);
                }
            }

            this.do_movement();
        } else if (OreSpawnPet.isTame(this, PET_FLAGS) && OreSpawnPet.getOwner(this, OWNER) != null) {
            LivingEntity e = OreSpawnPet.getOwner(this, OWNER);
            if (e != null && this.distanceToSqr(e) > 256.0) {
                OreSpawnPet.setSitting(this, PET_FLAGS, false);
                this.setActivity(2);
            }
        }

        // growth to The Young Prince deferred (ThePrinceTeen not in this wave)
        if (this.kill_count > 25 && this.fed_count > 10 && this.day_count > 10) {
            // deferred: spawn ThePrinceTeen + discard self
        }

        if (this.is_day == 0) {
            this.is_day = 1;
            if (!this.level().isDay()) {
                this.is_day = -1;
            }
        } else {
            if (this.is_day == -1 && this.level().isDay()) {
                this.day_count++;
            }
            this.is_day = 1;
            if (!this.level().isDay()) {
                this.is_day = -1;
            }
        }
    }

    private void do_movement() {
        int xdir = 1;
        int zdir = 1;
        int keep_trying = 10;
        int do_new = 0;
        double ox = 0.0;
        double oy = 0.0;
        double oz = 0.0;
        int has_owner = 0;
        double rr;
        double rhdir;
        double rdd;
        double pi = 3.1415926545;
        LivingEntity e = null;

        if (this.currentFlightTarget == null) {
            do_new = 1;
            this.currentFlightTarget = BlockPos.containing(this.getX(), this.getY(), this.getZ());
        }

        if (this.activity == 2 && this.random.nextInt(300) == 0) {
            do_new = 1;
        }

        if (OreSpawnPet.isTame(this, PET_FLAGS) && OreSpawnPet.getOwner(this, OWNER) != null) {
            e = OreSpawnPet.getOwner(this, OWNER);
            if (e != null) {
                has_owner = 1;
                ox = e.getX();
                oy = e.getY() + 1.0;
                oz = e.getZ();
                if (this.distanceToSqr(e) > 100.0) {
                    do_new = 1;
                }
                if (this.owner_flying != 0 && this.distanceToSqr(e) > 36.0) {
                    do_new = 1;
                }
            }
        }

        if (this.random.nextInt(7) == 1 && this.level().getDifficulty() != Difficulty.PEACEFUL) {
            e = this.findSomethingToAttack();
            if (e != null) {
                if (OreSpawnPet.isTame(this, PET_FLAGS)
                        && this.getHealth() / this.mygetMaxHealth() < 0.25F) {
                    this.setActivity(2);
                    this.setAttacking(0);
                    do_new = 0;
                    this.currentFlightTarget = BlockPos.containing(
                            this.getX() + (this.getX() - e.getX()),
                            this.getY() + 1.0,
                            this.getZ() + (this.getZ() - e.getZ()));
                } else {
                    this.setActivity(2);
                    this.setAttacking(1);
                    this.currentFlightTarget =
                            BlockPos.containing(e.getX(), e.getY() + 1.0, e.getZ());
                    do_new = 0;
                    double reach = 3.0F + e.getBbWidth() / 2.0F;
                    if (this.distanceToSqr(e) < reach * reach) {
                        this.doHurtTarget(e);
                    } else if (this.distanceToSqr(e) > 25.0
                            && this.distanceToSqr(e) < 144.0
                            && !this.isInWater()
                            && this.getSpyroFire() != 0
                            && (this.random.nextInt(3) == 0 || this.random.nextInt(4) == 1)) {
                        int which = this.random.nextInt(3);
                        if (which == 0) {
                            rr = Math.atan2(e.getZ() - this.getZ(), e.getX() - this.getX());
                            rhdir = Math.toRadians((this.getYRot() + 90.0F) % 360.0F);
                            rdd = Math.abs(rr - rhdir) % (pi * 2.0);
                            if (rdd > pi) {
                                rdd -= pi * 2.0;
                            }
                            rdd = Math.abs(rdd);
                            if (rdd < 0.5) {
                                this.firecanon(e);
                            }
                        } else if (which == 1) {
                            rr = Math.atan2(e.getZ() - this.getZ(), e.getX() - this.getX());
                            rhdir = Math.toRadians((this.getYRot() + 90.0F) % 360.0F);
                            rdd = Math.abs(rr - rhdir) % (pi * 2.0);
                            if (rdd > pi) {
                                rdd -= pi * 2.0;
                            }
                            rdd = Math.abs(rdd);
                            if (rdd < 0.5) {
                                this.firecanonl(e);
                            }
                        } else {
                            rr = Math.atan2(e.getZ() - this.getZ(), e.getX() - this.getX());
                            rhdir = Math.toRadians((this.getYRot() + 90.0F) % 360.0F);
                            rdd = Math.abs(rr - rhdir) % (pi * 2.0);
                            if (rdd > pi) {
                                rdd -= pi * 2.0;
                            }
                            rdd = Math.abs(rdd);
                            if (rdd < 0.5) {
                                this.firecanoni(e);
                            }
                        }
                    }
                }
            } else {
                this.setAttacking(0);
            }
        }

        if (this.activity != 1) {
            if (this.currentFlightTarget.distSqr(
                            BlockPos.containing(this.getX(), this.getY(), this.getZ()))
                    < 2.1F) {
                do_new = 1;
            }

            if (do_new != 0) {
                BlockState bid = Blocks.STONE.defaultBlockState();
                while (!bid.isAir() && keep_trying != 0) {
                    keep_trying--;
                    int gox = (int) this.getX();
                    int goy = (int) this.getY();
                    int goz = (int) this.getZ();
                    if (has_owner == 1) {
                        gox = (int) ox;
                        goy = (int) oy;
                        goz = (int) oz;
                        if (this.owner_flying == 0) {
                            zdir = this.random.nextInt(4) + 6;
                            xdir = this.random.nextInt(4) + 6;
                        } else {
                            zdir = this.random.nextInt(8);
                            xdir = this.random.nextInt(8);
                        }
                    } else {
                        zdir = this.random.nextInt(5) + 6;
                        xdir = this.random.nextInt(5) + 6;
                    }
                    if (this.random.nextInt(2) == 0) {
                        zdir = -zdir;
                    }
                    if (this.random.nextInt(2) == 0) {
                        xdir = -xdir;
                    }
                    this.currentFlightTarget = new BlockPos(
                            gox + xdir,
                            goy + (this.random.nextInt(6 + this.owner_flying * 2) - 2),
                            goz + zdir);
                    bid = this.level().getBlockState(this.currentFlightTarget);
                }
            }

            double speed_factor = 1.0;
            double var1 = this.currentFlightTarget.getX() + 0.5 - this.getX();
            double var3 = this.currentFlightTarget.getY() + 0.1 - this.getY();
            double var5 = this.currentFlightTarget.getZ() + 0.5 - this.getZ();
            if (this.owner_flying != 0) {
                speed_factor = 1.75;
                if (OreSpawnPet.isTame(this, PET_FLAGS) && OreSpawnPet.getOwner(this, OWNER) != null) {
                    e = OreSpawnPet.getOwner(this, OWNER);
                    if (e != null && this.distanceToSqr(e) > 49.0) {
                        speed_factor = 3.5;
                    }
                }
            }

            Vec3 m = this.getDeltaMovement();
            this.setDeltaMovement(
                    m.x + (Math.signum(var1) * 0.5 - m.x) * 0.15 * speed_factor,
                    m.y + (Math.signum(var3) * 0.7 - m.y) * 0.21 * speed_factor,
                    m.z + (Math.signum(var5) * 0.5 - m.z) * 0.15 * speed_factor);
            float var7 =
                    (float) (Math.atan2(this.getDeltaMovement().z, this.getDeltaMovement().x) * 180.0
                                    / Math.PI)
                            - 90.0F;
            float var8 = Mth.wrapDegrees(var7 - this.getYRot());
            this.setZza((float) (0.75 * speed_factor));
            this.setYRot(this.getYRot() + var8 / 3.0F);
        }
    }

    private boolean isRoyalty(LivingEntity e) {
        // gold MyUtils.isRoyalty — only ThePrince family ported in this wave
        return e instanceof ThePrince;
    }

    private boolean isSuitableTarget(LivingEntity par1EntityLiving) {
        if (this.level().getDifficulty() == Difficulty.PEACEFUL) {
            return false;
        }
        if (par1EntityLiving == null || par1EntityLiving == this || !par1EntityLiving.isAlive()) {
            return false;
        }
        if (OreSpawnPet.isOwnedBy(this, OWNER, par1EntityLiving)) {
            return false;
        }
        if (!this.hasLineOfSight(par1EntityLiving)) {
            return false;
        }
        if (this.isRoyalty(par1EntityLiving)) {
            return false;
        }
        if (par1EntityLiving instanceof Monster) {
            return true;
        }
        if (par1EntityLiving instanceof Mothra) {
            return true;
        }
        if (par1EntityLiving instanceof Butterfly) {
            return true;
        }
        if (par1EntityLiving instanceof Cockateil) {
            return true;
        }
        if (par1EntityLiving instanceof Dragonfly) {
            return true;
        }
        return par1EntityLiving instanceof Mosquito;
    }

    @Nullable
    private LivingEntity findSomethingToAttack() {
        if (OreSpawnMain.PlayNicely != 0) {
            return null;
        }
        List<LivingEntity> list = this.level().getEntitiesOfClass(
                LivingEntity.class, this.getBoundingBox().inflate(12.0, 6.0, 12.0));
        LivingEntity best = null;
        double bestDist = Double.MAX_VALUE;
        for (LivingEntity living : list) {
            if (this.isSuitableTarget(living)
                    && this.canSeeTarget(living.getX(), living.getY(), living.getZ())) {
                double d = this.distanceToSqr(living);
                if (d < bestDist) {
                    bestDist = d;
                    best = living;
                }
            }
        }
        return best;
    }

    /** Gold firecanon → BetterFireball (big or small 50%). */
    private void firecanon(LivingEntity e) {
        double yoff = 1.0;
        double xzoff = 3.0;
        double cx = this.getX() - xzoff * Math.sin(Math.toRadians(this.getYRot()));
        double cz = this.getZ() + xzoff * Math.cos(Math.toRadians(this.getYRot()));
        float r1 = 5.0F * (this.random.nextFloat() - this.random.nextFloat());
        float r2 = 3.0F * (this.random.nextFloat() - this.random.nextFloat());
        float r3 = 5.0F * (this.random.nextFloat() - this.random.nextFloat());
        Vec3 dir = new Vec3(
                e.getX() - cx + r1,
                e.getY() + e.getBbHeight() / 2.0F - (this.getY() + yoff) + r2,
                e.getZ() - cz + r3);
        BetterFireball bf = new BetterFireball(this.level(), this, dir);
        bf.setPos(cx, this.getY() + yoff, cz);
        bf.setBig();
        if (this.random.nextInt(2) == 1) {
            bf.setSmall();
        }
        this.playSound(
                SoundEvents.ARROW_SHOOT,
                1.0F,
                1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
        this.level().addFreshEntity(bf);
    }

    /**
     * Gold firecanonl → ThunderBolt projectile (deferred). Stand-in: SmallFireball.
     */
    private void firecanonl(LivingEntity e) {
        double yoff = 1.0;
        double xzoff = 3.0;
        double cx = this.getX() - xzoff * Math.sin(Math.toRadians(this.getYRot()));
        double cz = this.getZ() + xzoff * Math.cos(Math.toRadians(this.getYRot()));
        this.playSound(
                SoundEvents.ARROW_SHOOT,
                1.0F,
                1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
        float r1 = 5.0F * (this.random.nextFloat() - this.random.nextFloat());
        float r2 = 3.0F * (this.random.nextFloat() - this.random.nextFloat());
        float r3 = 5.0F * (this.random.nextFloat() - this.random.nextFloat());
        Vec3 dir = new Vec3(
                e.getX() - cx + r1,
                e.getY() + 0.25 - (this.getY() + yoff) + r2,
                e.getZ() - cz + r3);
        SmallFireball lb = new SmallFireball(this.level(), this, dir.normalize().scale(3.0));
        lb.setPos(cx, this.getY() + yoff, cz);
        this.level().addFreshEntity(lb);
    }

    /**
     * Gold firecanoni → IceBall + setIceMaker(1) (deferred). Stand-in: SmallFireball.
     */
    private void firecanoni(LivingEntity e) {
        double yoff = 1.0;
        double xzoff = 3.0;
        double cx = this.getX() - xzoff * Math.sin(Math.toRadians(this.getYRot()));
        double cz = this.getZ() + xzoff * Math.cos(Math.toRadians(this.getYRot()));
        this.playSound(
                SoundEvents.ARROW_SHOOT,
                1.0F,
                1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
        float r1 = 5.0F * (this.random.nextFloat() - this.random.nextFloat());
        float r2 = 3.0F * (this.random.nextFloat() - this.random.nextFloat());
        float r3 = 5.0F * (this.random.nextFloat() - this.random.nextFloat());
        Vec3 dir = new Vec3(
                e.getX() - cx + r1,
                e.getY() + 0.25 - (this.getY() + yoff) + r2,
                e.getZ() - cz + r3);
        SmallFireball lb = new SmallFireball(this.level(), this, dir.normalize().scale(3.0));
        lb.setPos(cx, this.getY() + yoff, cz);
        this.level().addFreshEntity(lb);
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (player.distanceToSqr(this) > 16.0) {
            return InteractionResult.PASS;
        }

        // gold: diamond block → instant tame + full grow flags
        if (stack.is(Blocks.DIAMOND_BLOCK.asItem())) {
            if (!this.level().isClientSide) {
                OreSpawnPet.tame(this, PET_FLAGS, OWNER, player);
                this.heal(this.mygetMaxHealth() - this.getHealth());
                this.ok_to_grow = 1;
                this.kill_count = 1000;
                this.fed_count = 1000;
                this.day_count = 1000;
            }
            if (!player.getAbilities().instabuild) {
                stack.shrink(1);
            }
            return InteractionResult.SUCCESS;
        }

        // gold: tamed + owner + food → heal * 10 nutrition, fed_count++
        if (OreSpawnPet.isTame(this, PET_FLAGS)
                && OreSpawnPet.isOwnedBy(this, OWNER, player)
                && isFood(stack)) {
            if (!this.level().isClientSide) {
                var food = stack.getFoodProperties(this);
                if (food != null && this.getHealth() < this.mygetMaxHealth()) {
                    this.heal(food.nutrition() * 10.0F);
                }
                this.level().broadcastEntityEvent(this, (byte) 7);
                this.fed_count++;
            }
            if (!player.getAbilities().instabuild) {
                stack.shrink(1);
            }
            return InteractionResult.SUCCESS;
        }

        if (OreSpawnPet.isTame(this, PET_FLAGS) && OreSpawnPet.isOwnedBy(this, OWNER, player)) {
            // ice extinguish
            if (stack.is(Items.ICE)) {
                if (!this.level().isClientSide) {
                    this.level().broadcastEntityEvent(this, (byte) 6);
                    this.setSpyroFire(0);
                    player.displayClientMessage(
                            Component.literal("Prince fireballs extinguished."), true);
                }
                if (!player.getAbilities().instabuild) {
                    stack.shrink(1);
                }
                return InteractionResult.SUCCESS;
            }
            // flint and steel light
            if (stack.is(Items.FLINT_AND_STEEL)) {
                if (!this.level().isClientSide) {
                    this.level().broadcastEntityEvent(this, (byte) 6);
                    this.setSpyroFire(1);
                    player.displayClientMessage(Component.literal("Prince fireballs lit!"), true);
                }
                if (!player.getAbilities().instabuild) {
                    stack.shrink(1);
                }
                return InteractionResult.SUCCESS;
            }
            // diamond grow → Young Prince (deferred if Teen missing)
            if (stack.is(Items.DIAMOND) && this.ok_to_grow != 0) {
                if (!this.level().isClientSide) {
                    // deferred: spawn ThePrinceTeen, transfer ownership, discard
                }
                if (!player.getAbilities().instabuild) {
                    stack.shrink(1);
                }
                return InteractionResult.SUCCESS;
            }
            // name tag
            if (stack.is(Items.NAME_TAG)) {
                if (!this.level().isClientSide) {
                    this.setCustomName(stack.getHoverName());
                }
                if (!player.getAbilities().instabuild) {
                    stack.shrink(1);
                }
                return InteractionResult.SUCCESS;
            }
            // empty hand: sit toggle
            if (!this.level().isClientSide) {
                if (!OreSpawnPet.isSitting(this, PET_FLAGS)) {
                    OreSpawnPet.setSitting(this, PET_FLAGS, true);
                    this.setActivity(1);
                } else {
                    OreSpawnPet.setSitting(this, PET_FLAGS, false);
                }
            }
            return InteractionResult.SUCCESS;
        }

        return InteractionResult.PASS;
    }

    @Override
    public void handleEntityEvent(byte id) {
        if (id == 7) {
            for (int i = 0; i < 7; i++) {
                double dx = this.random.nextGaussian() * 0.02;
                double dy = this.random.nextGaussian() * 0.02;
                double dz = this.random.nextGaussian() * 0.02;
                this.level()
                        .addParticle(
                                ParticleTypes.HEART,
                                this.getRandomX(1.0),
                                this.getRandomY() + 0.5,
                                this.getRandomZ(1.0),
                                dx,
                                dy,
                                dz);
            }
        } else if (id == 6) {
            for (int i = 0; i < 7; i++) {
                double dx = this.random.nextGaussian() * 0.02;
                double dy = this.random.nextGaussian() * 0.02;
                double dz = this.random.nextGaussian() * 0.02;
                this.level()
                        .addParticle(
                                ParticleTypes.SMOKE,
                                this.getRandomX(1.0),
                                this.getRandomY() + 0.5,
                                this.getRandomZ(1.0),
                                dx,
                                dy,
                                dz);
            }
        } else {
            super.handleEntityEvent(id);
        }
    }

    @Override
    protected SoundEvent getAmbientSound() {
        // gold: null if sitting or not attacking; else orespawn:roar
        if (OreSpawnPet.isSitting(this, PET_FLAGS) || this.getAttacking() == 0) {
            return null;
        }
        return null; // roar deferred
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return danger.orespawn.util.handlers.SoundsHandler.ENTITY_DUCK_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return danger.orespawn.util.handlers.SoundsHandler.ENTITY_CRYO_DEATH.get();
    }

    @Override
    protected float getSoundVolume() {
        return 0.6F;
    }

    @Override
    public float getVoicePitch() {
        // gold: (rand - rand) * 0.2 + 1.3
        return (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.3F;
    }

    @Override
    public boolean causeFallDamage(float fallDistance, float multiplier, DamageSource source) {
        return false;
    }

    @Override
    protected void checkFallDamage(double y, boolean onGround, BlockState state, BlockPos pos) {
        // gold empty updateFallState
    }

    @Override
    protected void dropCustomDeathLoot(ServerLevel level, DamageSource damageSource, boolean recentlyHit) {
        super.dropCustomDeathLoot(level, damageSource, recentlyHit);
        // gold: 1+rand(4) beef always
        int n = 1 + this.random.nextInt(4);
        for (int i = 0; i < n; i++) {
            this.spawnAtLocation(new ItemStack(Items.BEEF));
        }
    }

    @Override
    public boolean doHurtTarget(Entity target) {
        if (OreSpawnPet.isOwnedBy(this, OWNER, target)) {
            return false;
        }
        boolean hit = target.hurt(this.damageSources().mobAttack(this), (float) GOLD_ATTACK);
        if (hit && target instanceof LivingEntity living && living.getHealth() <= 0.0F) {
            this.kill_count++;
        }
        return hit;
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if ("inWall".equals(source.getMsgId())) {
            return false;
        }
        if ("cactus".equals(source.getMsgId())) {
            return false;
        }
        boolean ret = super.hurt(source, amount);
        if (ret) {
            OreSpawnPet.setSitting(this, PET_FLAGS, false);
            this.setActivity(2);
        }
        return ret;
    }

    @Override
    public boolean checkSpawnRules(LevelAccessor level, MobSpawnType spawnType) {
        // gold getCanSpawnHere: true
        return true;
    }
}
