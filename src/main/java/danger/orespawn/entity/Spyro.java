package danger.orespawn.entity;

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
import danger.orespawn.OreSpawnMain;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.PanicGoal;
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
 * Gold {@code Spyro} (EntityTameable) ported for NeoForge 1.21.1.
 * Size 0.5×0.5, speed 0.3, health 200, attack 5, armor 5, XP 35.
 * Activity 0/1/2 flight system, lava seeking, owner-flying chase, fireball combat.
 * Taming via raw/cooked beef ({@link OreSpawnPet} 50%), FollowOwner 1.15/12/2, sit toggle.
 */
public class Spyro extends Monster {
    private static final EntityDataAccessor<Byte> ACTIVITY =
            SynchedEntityData.defineId(Spyro.class, EntityDataSerializers.BYTE);
    private static final EntityDataAccessor<Byte> FIRE =
            SynchedEntityData.defineId(Spyro.class, EntityDataSerializers.BYTE);
    private static final EntityDataAccessor<Byte> PET_FLAGS =
            SynchedEntityData.defineId(Spyro.class, EntityDataSerializers.BYTE);
    private static final EntityDataAccessor<Optional<UUID>> OWNER =
            SynchedEntityData.defineId(Spyro.class, EntityDataSerializers.OPTIONAL_UUID);

    private final float moveSpeed = 0.3F;

    /** Gold: currentFlightTarget for activity-2 flight steering. */
    @Nullable
    private BlockPos currentFlightTarget;
    /** Gold public activity mirror (1=ground, 2=fly). Synced via ACTIVITY data. */
    public int activity = 1;
    private int owner_flying = 0;
    private boolean target_in_sight = false;
    private int closest = 99999;
    private int tx = 0;
    private int ty = 0;
    private int tz = 0;

    public Spyro(EntityType<? extends Spyro> type, Level level) {
        super(type, level);
        this.xpReward = 35;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 200.0) // mygetMaxHealth()
                .add(Attributes.MOVEMENT_SPEED, 0.3)
                .add(Attributes.ATTACK_DAMAGE, 5.0)
                .add(Attributes.ARMOR, 5.0) // func_70658_aO
                .add(Attributes.FOLLOW_RANGE, 16.0);
    }

    /** Gold was EntityTameable, not EntityMob — never wipe on Peaceful. */
    @Override
    protected boolean shouldDespawnInPeaceful() {
        return false;
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(2, new AvoidEntityGoal<>(this, Monster.class, 8.0F, 0.3, 0.4));
        // gold: MyEntityAIFollowOwner(this, 1.15F, 12.0F, 2.0F) priority 3
        this.goalSelector.addGoal(
                3,
                new FollowOwnerGoal(
                        this,
                        () -> OreSpawnPet.getOwnerUUID(this, OWNER),
                        () -> OreSpawnPet.isSitting(this, PET_FLAGS),
                        1.15,
                        12.0F,
                        2.0F));
        // gold: EntityAITempt(beef) field_151082_bd — also cooked so steak tempts
        this.goalSelector.addGoal(
                4, new TemptGoal(this, 1.25, Ingredient.of(Items.BEEF, Items.COOKED_BEEF), false));
        this.goalSelector.addGoal(5, new PanicGoal(this, 1.5));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 6.0F));
        // gold: MyEntityAIWander 0.75 — WanderALot covers roam
        this.goalSelector.addGoal(7, new WanderALotGoal(this, 10, 0.75));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(ACTIVITY, (byte) 1);
        builder.define(FIRE, (byte) 1);
        builder.define(PET_FLAGS, (byte) 0);
        builder.define(OWNER, Optional.empty());
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putByte("SpyroActivity", (byte) this.getActivity());
        tag.putByte("SpyroFire", (byte) this.getSpyroFire());
        OreSpawnPet.addAdditionalSaveData(this, PET_FLAGS, OWNER, tag);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.contains("SpyroActivity")) {
            this.setActivity(tag.getByte("SpyroActivity"));
        }
        if (tag.contains("SpyroFire")) {
            this.setSpyroFire(tag.getByte("SpyroFire"));
        }
        OreSpawnPet.readAdditionalSaveData(this, PET_FLAGS, OWNER, tag);
        if (OreSpawnPet.isTame(this, PET_FLAGS)) {
            this.setPersistenceRequired();
        }
    }

    @Override
    public boolean fireImmune() {
        return true;
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        // gold: !tamed (and not persistence)
        if (OreSpawnPet.isTame(this, PET_FLAGS)) {
            return false;
        }
        return !this.isPersistenceRequired();
    }

    public int mygetMaxHealth() {
        return 200;
    }

    public int getActivity() {
        return this.entityData.get(ACTIVITY);
    }

    public void setActivity(int value) {
        this.activity = value;
        this.entityData.set(ACTIVITY, (byte) value);
    }

    public int getSpyroFire() {
        return this.entityData.get(FIRE);
    }

    public void setSpyroFire(int value) {
        this.entityData.set(FIRE, (byte) value);
    }

    public boolean isOreSpawnTame() {
        return OreSpawnPet.isTame(this, PET_FLAGS);
    }

    public boolean isOreSpawnSitting() {
        return OreSpawnPet.isSitting(this, PET_FLAGS);
    }

    private static boolean isTameFood(ItemStack stack) {
        // gold: Items.field_151082_bd (raw beef). Cooked accepted so steak works too.
        return !stack.isEmpty() && (stack.is(Items.BEEF) || stack.is(Items.COOKED_BEEF));
    }

    /** Gold scan_it: expand search shells for lava blocks; updates closest/tx/ty/tz. */
    private boolean scan_it(int x, int y, int z, int dx, int dy, int dz) {
        int found = 0;

        for (int i = -dy; i <= dy; i++) {
            for (int j = -dz; j <= dz; j++) {
                BlockState state = this.level().getBlockState(new BlockPos(x + dx, y + i, z + j));
                if (state.is(Blocks.LAVA)) {
                    int d = dx * dx + j * j + i * i;
                    if (d < this.closest) {
                        this.closest = d;
                        this.tx = x + dx;
                        this.ty = y + i;
                        this.tz = z + j;
                        found++;
                    }
                }

                state = this.level().getBlockState(new BlockPos(x - dx, y + i, z + j));
                if (state.is(Blocks.LAVA)) {
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

        for (int i = -dx; i <= dx; i++) {
            for (int j = -dz; j <= dz; j++) {
                BlockState state = this.level().getBlockState(new BlockPos(x + i, y + dy, z + j));
                if (state.is(Blocks.LAVA)) {
                    int d = dy * dy + j * j + i * i;
                    if (d < this.closest) {
                        this.closest = d;
                        this.tx = x + i;
                        this.ty = y + dy;
                        this.tz = z + j;
                        found++;
                    }
                }

                state = this.level().getBlockState(new BlockPos(x + i, y - dy, z + j));
                if (state.is(Blocks.LAVA)) {
                    int d = dy * dy + j * j + i * i;
                    if (d < this.closest) {
                        this.closest = d;
                        this.tx = x + i;
                        this.ty = y - dy;
                        this.tz = z + j;
                        found++;
                    }
                }
            }
        }

        for (int i = -dx; i <= dx; i++) {
            for (int j = -dy; j <= dy; j++) {
                BlockState state = this.level().getBlockState(new BlockPos(x + i, y + j, z + dz));
                if (state.is(Blocks.LAVA)) {
                    int d = dz * dz + j * j + i * i;
                    if (d < this.closest) {
                        this.closest = d;
                        this.tx = x + i;
                        this.ty = y + j;
                        this.tz = z + dz;
                        found++;
                    }
                }

                state = this.level().getBlockState(new BlockPos(x + i, y + j, z - dz));
                if (state.is(Blocks.LAVA)) {
                    int d = dz * dz + j * j + i * i;
                    if (d < this.closest) {
                        this.closest = d;
                        this.tx = x + i;
                        this.ty = y + j;
                        this.tz = z - dz;
                        found++;
                    }
                }
            }
        }

        return found != 0;
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
        if (this.isInWater()) {
            this.setDeltaMovement(this.getDeltaMovement().add(0.0, 0.07, 0.0));
        }

        // gold func_70071_h_ server flight + movement (not client)
        if (!this.level().isClientSide) {
            if (this.currentFlightTarget == null) {
                this.currentFlightTarget = BlockPos.containing(this.getX(), this.getY(), this.getZ());
            }

            // gold: activity 2 damps vertical motion toward flight target height
            if (this.activity == 2) {
                Vec3 m = this.getDeltaMovement();
                double y = m.y;
                if (this.getY() < this.currentFlightTarget.getY() + 2.0) {
                    y *= 0.7;
                } else if (this.getY() > this.currentFlightTarget.getY() - 2.0) {
                    y *= 0.5;
                } else {
                    y *= 0.61;
                }
                this.setDeltaMovement(m.x, y, m.z);
            }

            // gold: tamed + ground + owner far (>256) → fly
            if (this.activity == 1
                    && OreSpawnPet.isTame(this, PET_FLAGS)
                    && OreSpawnPet.getOwner(this, OWNER) != null) {
                LivingEntity e = OreSpawnPet.getOwner(this, OWNER);
                if (e != null && this.distanceToSqr(e) > 256.0) {
                    this.setActivity(2);
                }
            }

            this.do_movement();
        }
    }

    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
        if (this.isDeadOrDying()) {
            return;
        }

        // gold onEntityUpdate: occasionally clear attack target
        if (this.random.nextInt(200) == 1) {
            this.setTarget(null);
        }

        // gold heal 1/100 ticks
        if (this.random.nextInt(100) == 1 && this.getHealth() < this.mygetMaxHealth()) {
            this.heal(1.0F);
        }

        // gold: sitting skips activity/lava/owner-flying (and flight combat via do_movement)
        if (OreSpawnPet.isSitting(this, PET_FLAGS)) {
            return;
        }

        if (this.activity == 0) {
            this.activity = 1;
            this.setActivity(1);
        }

        // gold: every 20 ticks scan for lava and path/heal
        if (this.random.nextInt(20) == 0) {
            this.closest = 99999;
            this.tx = this.ty = this.tz = 0;
            for (int i = 1; i < 11; i++) {
                int j = i;
                if (j > 4) {
                    j = 4;
                }
                if (this.scan_it((int) this.getX(), (int) this.getY() - 1, (int) this.getZ(), i, j, i)) {
                    break;
                }
                if (i >= 6) {
                    i++;
                }
            }
            if (this.closest < 99999) {
                this.setActivity(1);
                this.getNavigation().moveTo(this.tx, this.ty - 1, this.tz, 1.0);
                if (this.isInLava()) {
                    this.heal(1.0F);
                    this.playSound(
                            SoundEvents.LAVA_POP, 1.0F, this.random.nextFloat() * 0.2F + 0.9F);
                }
            }
        }

        // gold: every 100 ticks if no combat target in sight, pick ground or fly (1/8)
        if (this.random.nextInt(100) == 1 && !this.target_in_sight) {
            this.activity = 1;
            if (this.random.nextInt(8) == 1) {
                this.activity = 2;
            }
            this.setActivity(this.activity);
        }

        // gold: owner creative-flying forces fly mode and owner_flying chase
        this.owner_flying = 0;
        if (OreSpawnPet.isTame(this, PET_FLAGS) && OreSpawnPet.getOwner(this, OWNER) != null) {
            LivingEntity owner = OreSpawnPet.getOwner(this, OWNER);
            if (owner instanceof Player player && player.getAbilities().flying) {
                this.owner_flying = 1;
                this.setActivity(2);
            }
        }
    }

    /**
     * Gold do_movement: flight steering, combat, owner chase. No-ops when sitting or activity==1.
     */
    private void do_movement() {
        int xdir = 1;
        int zdir = 1;
        int keep_trying = 50;
        int do_new = 0;
        double ox = 0.0;
        double oy = 0.0;
        double oz = 0.0;
        int has_owner = 0;
        LivingEntity e = null;

        if (this.currentFlightTarget == null) {
            do_new = 1;
            this.currentFlightTarget = BlockPos.containing(this.getX(), this.getY(), this.getZ());
        }

        // gold: sitting cancels all flight movement
        if (OreSpawnPet.isSitting(this, PET_FLAGS)) {
            return;
        }

        // gold: only fly when activity != 1
        if (this.activity == 1) {
            return;
        }

        if (this.getActivity() == 2 && this.random.nextInt(300) == 0) {
            do_new = 1;
        }

        if (OreSpawnPet.isTame(this, PET_FLAGS) && OreSpawnPet.getOwner(this, OWNER) != null) {
            e = OreSpawnPet.getOwner(this, OWNER);
            if (e != null) {
                has_owner = 1;
                ox = e.getX();
                oy = e.getY();
                oz = e.getZ();
                if (this.distanceToSqr(e) > 100.0) {
                    do_new = 1;
                }
                if (this.owner_flying != 0 && this.distanceToSqr(e) > 36.0) {
                    do_new = 1;
                }
            }
        }

        // gold combat while flying (nextInt(6)==1, not peaceful)
        if (this.random.nextInt(6) == 1 && this.level().getDifficulty() != Difficulty.PEACEFUL) {
            e = this.findSomethingToAttack();
            if (e != null) {
                if (OreSpawnPet.isTame(this, PET_FLAGS)
                        && this.getHealth() / this.mygetMaxHealth() < 0.25F) {
                    // flee away from target
                    this.setActivity(2);
                    this.target_in_sight = false;
                    do_new = 0;
                    this.currentFlightTarget = BlockPos.containing(
                            this.getX() + (this.getX() - e.getX()),
                            this.getY() + 1.0,
                            this.getZ() + (this.getZ() - e.getZ()));
                } else {
                    this.setActivity(2);
                    this.target_in_sight = true;
                    this.currentFlightTarget =
                            BlockPos.containing(e.getX(), e.getY() + 1.0, e.getZ());
                    this.getNavigation().moveTo(e, 1.25);
                    do_new = 0;
                    double reach = 3.0F + e.getBbWidth() / 2.0F;
                    if (this.distanceToSqr(e) < reach * reach) {
                        this.doHurtTarget(e);
                    } else if (this.distanceToSqr(e) < 64.0
                            && !this.isInWater()
                            && ((this.getSpyroFire() == 1 && this.random.nextInt(10) == 0)
                                    || this.random.nextInt(15) == 1)) {
                        // gold fireball condition (fire on OR rare 1/15)
                        Vec3 dir = new Vec3(
                                e.getX() - this.getX(),
                                e.getY() + 0.25 - (this.getY() + 1.25),
                                e.getZ() - this.getZ());
                        SmallFireball ball = new SmallFireball(this.level(), this, dir.normalize());
                        ball.setPos(this.getX(), this.getY() + 1.25, this.getZ());
                        this.playSound(
                                SoundEvents.BLAZE_SHOOT,
                                0.75F,
                                1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
                        this.level().addFreshEntity(ball);
                    }
                }
            } else {
                this.target_in_sight = false;
            }
        }

        // gold: near flight target (not activity 3) → pick new
        if (this.currentFlightTarget.distSqr(
                                BlockPos.containing(this.getX(), this.getY(), this.getZ()))
                        < 2.1F
                && this.getActivity() != 3) {
            do_new = 1;
        }

        if (do_new != 0 && !this.target_in_sight) {
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
                        zdir = this.random.nextInt(6);
                        xdir = this.random.nextInt(6);
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
                        goy + this.random.nextInt(9 + this.owner_flying * 2) - 4,
                        goz + zdir);
                bid = this.level().getBlockState(this.currentFlightTarget);
                if (bid.isAir()
                        && !this.canSeeTarget(
                                this.currentFlightTarget.getX(),
                                this.currentFlightTarget.getY(),
                                this.currentFlightTarget.getZ())) {
                    bid = Blocks.STONE.defaultBlockState();
                }
            }
        }

        // gold flight velocity toward currentFlightTarget
        double speed_factor = 0.5;
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

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        // Prefer the hand that actually holds food (main or offhand)
        ItemStack stack = player.getItemInHand(hand);
        if (!isTameFood(stack)) {
            ItemStack other = OreSpawnPet.findFoodInHands(player, Spyro::isTameFood);
            if (!other.isEmpty()) {
                stack = other;
            }
        }

        // gold: distanceSq < 16 (~4 blocks). Relax slightly for tiny 0.5 hitbox.
        if (player.distanceToSqr(this) > 36.0) {
            return InteractionResult.PASS;
        }

        // gold: beef tame — 50% survival; creative always (tryTame)
        if (isTameFood(stack)) {
            if (!OreSpawnPet.isTame(this, PET_FLAGS)) {
                if (!this.level().isClientSide) {
                    if (OreSpawnPet.tryTameWithFeedback(
                            this,
                            PET_FLAGS,
                            OWNER,
                            player,
                            2,
                            "Baby Dragon is now yours!",
                            "Baby Dragon sniffs the beef… try again!")) {
                        this.heal(this.mygetMaxHealth() - this.getHealth());
                    }
                }
            } else if (OreSpawnPet.isOwnedBy(this, OWNER, player)) {
                // already owned: heal a bit like feeding a pet
                if (!this.level().isClientSide && this.getHealth() < this.mygetMaxHealth()) {
                    this.heal(4.0F);
                    this.level().broadcastEntityEvent(this, (byte) 7);
                }
            }
            if (!player.getAbilities().instabuild) {
                stack.shrink(1);
            }
            // SUCCESS on both sides so food never "eats" instead of interact
            return InteractionResult.SUCCESS;
        }

        if (OreSpawnPet.isTame(this, PET_FLAGS)) {
            // gold: cobweb (field_150330_I) releases pet
            if (stack.is(Items.COBWEB)) {
                if (!this.level().isClientSide) {
                    OreSpawnPet.setTame(this, PET_FLAGS, false);
                    OreSpawnPet.setOwnerUUID(this, OWNER, null);
                    OreSpawnPet.setSitting(this, PET_FLAGS, false);
                    this.setHealth(this.mygetMaxHealth());
                    this.level().broadcastEntityEvent(this, (byte) 6);
                    player.displayClientMessage(Component.literal("Baby Dragon was released."), true);
                }
                if (!player.getAbilities().instabuild) {
                    stack.shrink(1);
                }
                return InteractionResult.SUCCESS;
            }
            // gold: ice (field_150432_aD) extinguish fireballs
            if (stack.is(Items.ICE)) {
                if (!this.level().isClientSide) {
                    this.level().broadcastEntityEvent(this, (byte) 6);
                    this.setSpyroFire(0);
                    player.displayClientMessage(Component.literal("Baby Spyro fireballs extinguished."), true);
                }
                if (!player.getAbilities().instabuild) {
                    stack.shrink(1);
                }
                return InteractionResult.SUCCESS;
            }
            // gold: flint and steel (field_151033_d) light fireballs
            if (stack.is(Items.FLINT_AND_STEEL)) {
                if (!this.level().isClientSide) {
                    this.level().broadcastEntityEvent(this, (byte) 6);
                    this.setSpyroFire(1);
                    player.displayClientMessage(Component.literal("Baby Spyro fireballs lit!"), true);
                }
                if (!player.getAbilities().instabuild) {
                    stack.shrink(1);
                }
                return InteractionResult.SUCCESS;
            }
            // gold: name tag sets custom name (before sit toggle)
            if (stack.is(Items.NAME_TAG)) {
                if (!this.level().isClientSide) {
                    this.setCustomName(stack.getHoverName());
                }
                if (!player.getAbilities().instabuild) {
                    stack.shrink(1);
                }
                return InteractionResult.SUCCESS;
            }
            // gold: any other right-click while tamed toggles sit
            if (!this.level().isClientSide) {
                boolean nowSitting = !OreSpawnPet.isSitting(this, PET_FLAGS);
                OreSpawnPet.setSitting(this, PET_FLAGS, nowSitting);
                if (nowSitting) {
                    this.setActivity(1);
                    this.getNavigation().stop();
                }
                player.displayClientMessage(
                        Component.literal(nowSitting ? "Baby Dragon is sitting." : "Baby Dragon is following."),
                        true);
            }
            return InteractionResult.SUCCESS;
        }

        return InteractionResult.PASS;
    }

    @Override
    public void handleEntityEvent(byte id) {
        if (id == 7) {
            // tame success hearts
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
            // tame fail / feedback smoke
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
        //  gold: roar when activity==2 and not sitting; roar sound not registered in port yet
        return null;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return danger.orespawn.util.handlers.SoundsHandler.ENTITY_DUCK_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return danger.orespawn.util.handlers.SoundsHandler.ENTITY_ALOSAURUS_DEATH.get();
    }

    @Override
    protected float getSoundVolume() {
        return 0.4F;
    }

    @Override
    public boolean causeFallDamage(float fallDistance, float multiplier, DamageSource source) {
        // gold: empty fall()
        return false;
    }

    @Override
    protected void checkFallDamage(double y, boolean onGround, BlockState state, BlockPos pos) {
        // gold: empty updateFallState
    }

    @Override
    protected void dropCustomDeathLoot(ServerLevel level, DamageSource damageSource, boolean recentlyHit) {
        super.dropCustomDeathLoot(level, damageSource, recentlyHit);
        // gold: drops beef only when tamed
        if (!OreSpawnPet.isTame(this, PET_FLAGS)) {
            return;
        }
        int n = 1 + this.random.nextInt(4);
        for (int i = 0; i < n; i++) {
            this.spawnAtLocation(new ItemStack(Items.BEEF));
        }
    }

    @Override
    public boolean doHurtTarget(Entity target) {
        // never attack owner
        if (OreSpawnPet.isOwnedBy(this, OWNER, target)) {
            return false;
        }
        // gold getAttackStrength = 4
        return target.hurt(this.damageSources().mobAttack(this), 4.0F);
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if ("cactus".equals(source.getMsgId())) {
            return false;
        }
        return super.hurt(source, amount);
    }

    private boolean isSuitableTarget(LivingEntity target) {
        if (this.level().getDifficulty() == Difficulty.PEACEFUL) {
            return false;
        }
        if (target == null || target == this || !target.isAlive()) {
            return false;
        }
        // never target owner
        if (OreSpawnPet.isOwnedBy(this, OWNER, target)) {
            return false;
        }
        if (!this.hasLineOfSight(target)) {
            return false;
        }
        if (target instanceof Spyro) {
            return false;
        }
        // gold: EntityMob only
        return target instanceof Monster;
    }

    @Nullable
    private LivingEntity findSomethingToAttack() {
        // gold: OreSpawnMain.PlayNicely != 0 → no combat hunt
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

    /**
     * Gold {@code getCanSpawnHere}: !daytime → false; y &lt; 50 → false; else true.
     */
    @Override
    public boolean checkSpawnRules(LevelAccessor level, MobSpawnType spawnType) {
        if (this.getY() < 50.0) {
            return false;
        }
        if (level instanceof Level lvl) {
            return lvl.isDay();
        }
        return true;
    }
}
