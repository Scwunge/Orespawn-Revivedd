package danger.orespawn.entity;

import danger.orespawn.init.ModItems;

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
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
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
 * Gold {@code ThePrinceAdult} (EntityTameable) ported for NeoForge 1.21.1.
 * Size 6.25x10.25, speed 0.36, health 3000, attack 100, armor 20, XP 3000.
 * Three-headed flyer: activity 0 ground / 1 fly, rideable, BetterFireball + deferred Ice/Thunder.
 * diamond-block instant tame, growth to Young Prince deferred.
 * Texture: {@code textures/entity/thekingtexture.png}.
 * Registry size/attrs set in {@code ModEntities}.
 */
public class ThePrinceAdult extends Monster {
    private static final EntityDataAccessor<Byte> ACTIVITY =
            SynchedEntityData.defineId(ThePrinceAdult.class, EntityDataSerializers.BYTE);
    private static final EntityDataAccessor<Byte> FIRE =
            SynchedEntityData.defineId(ThePrinceAdult.class, EntityDataSerializers.BYTE);
    private static final EntityDataAccessor<Byte> ATTACKING =
            SynchedEntityData.defineId(ThePrinceAdult.class, EntityDataSerializers.BYTE);
    private static final EntityDataAccessor<Byte> PET_FLAGS =
            SynchedEntityData.defineId(ThePrinceAdult.class, EntityDataSerializers.BYTE);
    private static final EntityDataAccessor<Optional<UUID>> OWNER =
            SynchedEntityData.defineId(ThePrinceAdult.class, EntityDataSerializers.OPTIONAL_UUID);

    public static final float GOLD_WIDTH = 6.25F;
    public static final float GOLD_HEIGHT = 10.25F;
    public static final double GOLD_HEALTH = 3000.0;
    public static final double GOLD_SPEED = 0.36;
    public static final double GOLD_ATTACK = 100.0;
    public static final double GOLD_ARMOR = 20.0;
    public static final int GOLD_XP = 3000;
    public static final double GOLD_FOLLOW = 1000.0;

    private final float moveSpeed = 0.36F;

    @Nullable
    private BlockPos currentFlightTarget;
    /** Gold public activity mirror (0=ground, 1=fly). */
    public int activity = 0;
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
    private int growcounter = 0;
    private int hurt_timer = 0;
    private int flyaway = 0;
    private boolean target_in_sight = false;
    private RenderInfo renderdata = new RenderInfo();

    public ThePrinceAdult(EntityType<? extends ThePrinceAdult> type, Level level) {
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

    /** Gold was EntityTameable â€” never wipe on Peaceful. */
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
                        1.1,
                        12.0F,
                        2.0F));
        this.goalSelector.addGoal(3, new TemptGoal(this, 1.25, Ingredient.of(Items.BEEF), false));
        this.goalSelector.addGoal(4, new LookAtPlayerGoal(this, LivingEntity.class, 20.0F));
        this.goalSelector.addGoal(5, new WanderALotGoal(this, 10, 0.75));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
        if (OreSpawnMain.PlayNicely == 0) {
            this.targetSelector.addGoal(
                    1, new NearestAttackableTargetGoal<>(this, Monster.class, true, false));
        }
        this.targetSelector.addGoal(2, new HurtByTargetGoal(this));
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(ACTIVITY, (byte) 0);
        builder.define(FIRE, (byte) 1); // gold data 20 default 1
        builder.define(ATTACKING, (byte) 0);
        builder.define(PET_FLAGS, (byte) 0);
        builder.define(OWNER, Optional.empty());
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("ThePrinceAdultActivity", this.getActivity());
        tag.putInt("ThePrinceAdultFire", this.getThePrinceAdultFire());
        tag.putInt("ThePrinceAdultGrow", this.growcounter);
        tag.putInt("ThePrinceAdultKill", this.kill_count);
        tag.putInt("ThePrinceAdultFed", this.fed_count);
        tag.putInt("ThePrinceAdultDay", this.day_count);
        OreSpawnPet.addAdditionalSaveData(this, PET_FLAGS, OWNER, tag);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.contains("ThePrinceAdultActivity")) {
            this.setActivity(tag.getInt("ThePrinceAdultActivity"));
        }
        if (tag.contains("ThePrinceAdultFire")) {
            this.setThePrinceAdultFire(tag.getInt("ThePrinceAdultFire"));
        }
        this.growcounter = tag.getInt("ThePrinceAdultGrow");
        this.kill_count = tag.getInt("ThePrinceAdultKill");
        this.fed_count = tag.getInt("ThePrinceAdultFed");
        this.day_count = tag.getInt("ThePrinceAdultDay");
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

    public int getThePrinceAdultFire() {
        return this.entityData.get(FIRE) & 0xFF;
    }

    public void setThePrinceAdultFire(int par1) {
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

    /** Gold isSitting for model anim. */
    public boolean isSitting() {
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
        // gold: any ItemFood â€” use edible flag
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

        if (this.hurt_timer > 0) {
            this.hurt_timer--;
        }

        // gold: activity != 0 â†’ noClip
        this.noPhysics = this.getActivity() == 1;

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

        if (this.getActivity() == 1) {
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

        if (this.random.nextInt(250) == 1 && this.getHealth() < this.mygetMaxHealth()) {
            this.heal(5.0F);
        }

        if (!OreSpawnPet.isSitting(this, PET_FLAGS)) {
            // gold always_do-ish: random activity; owner flying forces fly(1)
            this.owner_flying = 0;
            if (OreSpawnPet.isTame(this, PET_FLAGS) && OreSpawnPet.getOwner(this, OWNER) != null) {
                LivingEntity owner = OreSpawnPet.getOwner(this, OWNER);
                if (owner instanceof Player player && player.getAbilities().flying) {
                    this.owner_flying = 1;
                    this.setActivity(1);
                }
            }

            if (this.random.nextInt(50) == 1 && !this.isVehicle()) {
                if (this.random.nextInt(15) == 1) {
                    this.setActivity(1);
                } else {
                    this.setActivity(0);
                }
            }

            if (OreSpawnPet.isTame(this, PET_FLAGS)
                    && OreSpawnPet.getOwner(this, OWNER) != null) {
                LivingEntity e = OreSpawnPet.getOwner(this, OWNER);
                if (e != null && this.distanceToSqr(e) > 900.0) {
                    this.setActivity(1);
                }
            }

            if (this.getActivity() == 1) {
                this.do_movement();
            }
        } else if (OreSpawnPet.isTame(this, PET_FLAGS) && OreSpawnPet.getOwner(this, OWNER) != null) {
            LivingEntity e = OreSpawnPet.getOwner(this, OWNER);
            if (e != null && this.distanceToSqr(e) > 900.0) {
                OreSpawnPet.setSitting(this, PET_FLAGS, false);
                this.setActivity(1);
            }
        }

        // growth to The Young Prince deferred (ThePrinceAdultTeen not in this wave)
        if (this.kill_count > 25 && this.fed_count > 10 && this.day_count > 10) {
            // deferred: spawn ThePrinceAdultTeen + discard self
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

        if (this.getActivity() == 1 && this.random.nextInt(300) == 0) {
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
                    this.setActivity(1);
                    this.setAttacking(0);
                    do_new = 0;
                    this.currentFlightTarget = BlockPos.containing(
                            this.getX() + (this.getX() - e.getX()),
                            this.getY() + 1.0,
                            this.getZ() + (this.getZ() - e.getZ()));
                } else {
                    this.setActivity(1);
                    this.setAttacking(1);
                    this.currentFlightTarget =
                            BlockPos.containing(e.getX(), e.getY() + 1.0, e.getZ());
                    do_new = 0;
                    double reach = 10.0F + e.getBbWidth() / 2.0F;
                    if (this.distanceToSqr(e) < reach * reach) {
                        this.doHurtTarget(e);
                    } else if (this.distanceToSqr(e) < 600.0
                            && !this.isInWater()
                            && this.getThePrinceAdultFire() != 0
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

        if (this.getActivity() == 1) {
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
        // gold MyUtils.isRoyalty
        return e instanceof ThePrince
                || e instanceof ThePrincess
                || e instanceof ThePrinceAdult
                || e instanceof Spyro;
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

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    public void jumpFromGround() {
        super.jumpFromGround();
        this.setDeltaMovement(this.getDeltaMovement().add(0.0, 0.35, 0.0));
    }

    @Override
    protected boolean canAddPassenger(Entity passenger) {
        return this.getPassengers().isEmpty();
    }

    @Nullable
    @Override
    public LivingEntity getControllingPassenger() {
        Entity p = this.getFirstPassenger();
        return p instanceof LivingEntity living ? living : null;
    }

    @Override
    protected Vec3 getPassengerAttachmentPoint(
            Entity entity, net.minecraft.world.entity.EntityDimensions dimensions, float partialTick) {
        // gold mountedYOffset 9.25; lateral f=4.65
        float f = 4.65F;
        double yaw = Math.toRadians(this.getYRot());
        return new Vec3(-f * Math.sin(yaw), 9.25, f * Math.cos(yaw));
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
        if (par1EntityLiving instanceof WaterDragon) {
            return true;
        }
        if (par1EntityLiving instanceof GammaMetroid) {
            return true;
        }
        return false;
    }

    @Nullable
    private LivingEntity findSomethingToAttack() {
        if (OreSpawnMain.PlayNicely != 0) {
            return null;
        }
        List<LivingEntity> list = this.level().getEntitiesOfClass(
                LivingEntity.class, this.getBoundingBox().inflate(32.0, 20.0, 32.0));
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

    /** Gold firecanon â†’ BetterFireball (big or small 50%). */
    private void firecanon(LivingEntity e) {
        double yoff = 3.5;
        double xzoff = 6.0;
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
     * Gold firecanonl â†’ ThunderBolt projectile (deferred). Stand-in: SmallFireball.
     */
    private void firecanonl(LivingEntity e) {
        double yoff = 3.5;
        double xzoff = 6.0;
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
     * Gold firecanoni â†’ IceBall + setIceMaker(1) (deferred). Stand-in: SmallFireball.
     */
    private void firecanoni(LivingEntity e) {
        double yoff = 3.5;
        double xzoff = 6.0;
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
        if (player.distanceToSqr(this) > 36.0) {
            return InteractionResult.PASS;
        }

        // gold: diamond block â†’ full heal + growcounter=288000 (not tame)
        if (stack.is(Blocks.DIAMOND_BLOCK.asItem())) {
            if (!this.level().isClientSide) {
                this.heal(this.mygetMaxHealth() - this.getHealth());
                this.growcounter = 288000;
            }
            if (!player.getAbilities().instabuild) {
                stack.shrink(1);
            }
            return InteractionResult.SUCCESS;
        }

        // gold: tamed + owner + food â†’ heal * 10 nutrition, fed_count++
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
                    this.setThePrinceAdultFire(0);
                    player.displayClientMessage(
                            Component.literal("Fireballs extinguished."), true);
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
                    this.setThePrinceAdultFire(1);
                    player.displayClientMessage(Component.literal("Fireballs lit!"), true);
                }
                if (!player.getAbilities().instabuild) {
                    stack.shrink(1);
                }
                return InteractionResult.SUCCESS;
            }
            // diamond grow â†’ Young Prince (deferred if Teen missing)
            if (stack.is(Items.DIAMOND) && this.ok_to_grow != 0) {
                if (!this.level().isClientSide) {
                    // deferred: spawn ThePrinceAdultTeen, transfer ownership, discard
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
            // empty hand: mount (gold)
            if (stack.isEmpty()) {
                if (!this.level().isClientSide) {
                    player.startRiding(this);
                    this.setActivity(1);
                    OreSpawnPet.setSitting(this, PET_FLAGS, false);
                }
                return InteractionResult.SUCCESS;
            }
            // emerald Ã¢â€ â€™ Young Prince deferred
            if (stack.is(Items.EMERALD)) {
                if (!this.level().isClientSide) {
                    // deferred: spawn ThePrinceTeen + discard
                }
                if (!player.getAbilities().instabuild) {
                    stack.shrink(1);
                }
                return InteractionResult.SUCCESS;
            }
            // any other item: sit toggle Ã¢â€ â€™ activity 0
            if (!this.level().isClientSide) {
                if (!OreSpawnPet.isSitting(this, PET_FLAGS)) {
                    OreSpawnPet.setSitting(this, PET_FLAGS, true);
                    this.setActivity(0);
                } else {
                    OreSpawnPet.setSitting(this, PET_FLAGS, false);
                    this.setActivity(0);
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
        // gold: null if sitting; king_living if activity==1 and no rider
        if (OreSpawnPet.isSitting(this, PET_FLAGS)) {
            return null;
        }
        if (this.getActivity() == 1 && !this.isVehicle()) {
            return null; // TODO: orespawn:king_living
        }
        return null;
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
        // gold: ThePrinceEgg Ã—1
        this.spawnAtLocation(new ItemStack(ModItems.THE_PRINCE_EGG.get()));
    }

    @Override
    public boolean doHurtTarget(Entity target) {
        if (OreSpawnPet.isOwnedBy(this, OWNER, target)) {
            return false;
        }
        // gold: fixed 100 dmg + knockback ks=2 inair=0.2
        if (target instanceof LivingEntity living) {
            boolean hit = living.hurt(this.damageSources().mobAttack(this), 100.0F);
            float f3 = (float) Math.atan2(living.getZ() - this.getZ(), living.getX() - this.getX());
            double ks = 2.0;
            double inair = 0.2;
            if (!living.isAlive() || living instanceof Player) {
                inair *= 2.0;
            }
            living.push(Math.cos(f3) * ks, inair, Math.sin(f3) * ks);
            if (hit && living.getHealth() <= 0.0F) {
                this.kill_count++;
            }
            return hit;
        }
        return target.hurt(this.damageSources().mobAttack(this), 100.0F);
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (this.hurt_timer > 0) {
            return false;
        }
        String msg = source.getMsgId();
        if ("cactus".equals(msg) || "inFire".equals(msg) || "onFire".equals(msg) || "lava".equals(msg)) {
            return false;
        }
        if ("inWall".equals(msg)) {
            OreSpawnPet.setSitting(this, PET_FLAGS, false);
            this.setActivity(1);
            return false;
        }
        OreSpawnPet.setSitting(this, PET_FLAGS, false);
        this.setActivity(1);
        Entity e = source.getEntity();
        if (e instanceof BetterFireball || e instanceof SmallFireball) {
            e.discard();
            return false;
        }
        if (e instanceof ThePrinceAdult || e instanceof Spyro) {
            return false;
        }
        boolean ret = super.hurt(source, amount);
        this.hurt_timer = 20;
        if (e instanceof LivingEntity living) {
            if (OreSpawnPet.isTame(this, PET_FLAGS) && living instanceof Player) {
                return false;
            }
            this.setTarget(living);
            this.setLastHurtByMob(living);
            this.getNavigation().moveTo(living, 1.2);
            ret = true;
        }
        return ret;
    }

    @Override
    public boolean checkSpawnRules(LevelAccessor level, MobSpawnType spawnType) {
        // gold getCanSpawnHere: false (growth / egg / creative only)
        return false;
    }
}
