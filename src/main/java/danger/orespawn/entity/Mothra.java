package danger.orespawn.entity;

import danger.orespawn.OreSpawnMain;
import danger.orespawn.init.ModEntities;
import danger.orespawn.init.ModItems;
import danger.orespawn.util.handlers.SoundsHandler;
import java.util.Comparator;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.SmallFireball;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

/**
 * Gold {@code Mothra} (extends Butterfly implements IMob). Size 5.0×2.0, speed 0.35,
 * health 150, attack 12, armor 8, XP 100. Giant flying fireball attacker.
 * Render mesh is ModelButterfly ×8 — cull box must cover wings or she pops invisible.
 */
public class Mothra extends Monster {
    /** Matches gold ModelButterfly second ctor arg / MothraRenderer.MODEL_SCALE. */
    public static final float RENDER_SCALE = 8.0F;

    private final float moveSpeed = 0.35F;
    @Nullable
    private BlockPos currentFlightTarget;
    private int lastX;
    private int lastY;
    private int lastZ;
    private int stuckCount;
    private int wingSound;
    private int healthTicker = 100;

    public Mothra(EntityType<? extends Mothra> type, Level level) {
        super(type, level);
        this.xpReward = 100;
        // Hitbox is 5×2 but mesh is ~8× butterfly — skip frustum cull on tiny BB
        this.noCulling = true;
    }

    /**
     * Gold mesh is far larger than entity dimensions; inflate so client never drops the model
     * when the player looks slightly off the hitbox.
     */
    @Override
    public AABB getBoundingBoxForCulling() {
        return this.getBoundingBox().inflate(RENDER_SCALE * 1.5, RENDER_SCALE, RENDER_SCALE * 1.5);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 150.0) // mygetMaxHealth()
                .add(Attributes.MOVEMENT_SPEED, 0.35)
                .add(Attributes.ATTACK_DAMAGE, 12.0)
                .add(Attributes.ARMOR, 8.0) // func_70658_aO
                .add(Attributes.FOLLOW_RANGE, 32.0);
    }

    @Override
    protected void registerGoals() {
        // gold: no AI goals — pure updateAITasks flight/combat
    }

    @Override
    public boolean fireImmune() {
        return true;
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return !this.isPersistenceRequired();
    }

    public int mygetMaxHealth() {
        return 150;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return null;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return null;
    }

    @Override
    protected SoundEvent getDeathSound() {
        // gold onDeath: SoundEvents.field_187539_bB (same as cage success → PLAYER_LEVELUP)
        return SoundEvents.PLAYER_LEVELUP;
    }

    @Override
    protected float getSoundVolume() {
        return 1.5F;
    }

    @Override
    public float getVoicePitch() {
        return 1.0F;
    }

    /** Gold {@code collideWithEntity} empty — no shove on contact. */
    @Override
    protected void doPush(Entity entity) {}

    /** Gold {@code canTriggerWalking} false. */
    @Override
    public boolean isIgnoringBlockTriggers() {
        return true;
    }

    @Override
    public void tick() {
        super.tick();
        Vec3 m = this.getDeltaMovement();
        this.setDeltaMovement(m.x, m.y * 0.6, m.z);
        this.wingSound++;
        if (this.wingSound > 30) {
            if (!this.level().isClientSide) {
                this.playSound(SoundsHandler.ENTITY_MOTHRA_WINGS.get(), 1.0F, 1.0F);
            }
            this.wingSound = 0;
        }
        this.healthTicker--;
        if (this.healthTicker <= 0) {
            if (this.getHealth() < this.mygetMaxHealth()) {
                this.heal(1.0F);
            }
            this.healthTicker = 200;
        }
    }

    @Override
    public boolean causeFallDamage(float fallDistance, float multiplier, DamageSource source) {
        return false;
    }

    @Override
    protected void checkFallDamage(double y, boolean onGround, BlockState state, BlockPos pos) {}

    /**
     * Gold {@code getCanSpawnHere}: y ≥ 70, night only, clear air volume above,
     * no other Mothra within inflate 64×32×64. (Gold Butterfly path — no light check.)
     */
    @Override
    public boolean checkSpawnRules(LevelAccessor level, MobSpawnType spawnType) {
        if (this.getY() < 70.0) {
            return false;
        }
        if (level instanceof Level lvl && lvl.isDay()) {
            return false;
        }
        // gold: k -4.3, j -3.2, i 1.9 must be air above spawn
        int bx = Mth.floor(this.getX());
        int by = Mth.floor(this.getY());
        int bz = Mth.floor(this.getZ());
        for (int k = -4; k < 4; k++) {
            for (int j = -3; j < 3; j++) {
                for (int i = 1; i < 10; i++) {
                    if (!level.getBlockState(new BlockPos(bx + j, by + i, bz + k)).isAir()) {
                        return false;
                    }
                }
            }
        }
        // gold: getClosestEntity(Mothra, expand 64,32,64) == null
        List<Mothra> nearby = level.getEntitiesOfClass(
                Mothra.class, this.getBoundingBox().inflate(64.0, 32.0, 64.0));
        for (Mothra other : nearby) {
            if (other != this) {
                return false;
            }
        }
        return true;
    }

    /** Gold {@code dropItemRand}: scatter x/z ±(0.7)-(0.7), y+1. */
    private void dropItemRand(Item index, int count) {
        ItemEntity item = new ItemEntity(
                this.level(),
                this.getX() + this.random.nextInt(8) - this.random.nextInt(8),
                this.getY() + 1.0,
                this.getZ() + this.random.nextInt(8) - this.random.nextInt(8),
                new ItemStack(index, count));
        this.level().addFreshEntity(item);
    }

    /** Gold {@code spawnCreature}: create, place, add, play living sound / finalize. */
    public static Entity spawnCreature(
            Level level, EntityType<? extends Mob> type, double x, double y, double z) {
        if (!(level instanceof ServerLevel server)) {
            return null;
        }
        Entity e = type.create(server);
        if (e != null) {
            e.moveTo(x, y, z, level.random.nextFloat() * 360.0F, 0.0F);
            if (e instanceof Mob mob) {
                mob.finalizeSpawn(
                        server,
                        server.getCurrentDifficultyAt(mob.blockPosition()),
                        MobSpawnType.MOB_SUMMONED,
                        null);
            }
            level.addFreshEntity(e);
        }
        return e;
    }

    @Override
    protected void dropCustomDeathLoot(ServerLevel level, DamageSource damageSource, boolean recentlyHit) {
        super.dropCustomDeathLoot(level, damageSource, recentlyHit);
        // gold: item_frame, EXPLOSION_LARGE×20, 53 gold_nugget, 3 blaze_rod, nether_star, 20 Moth
        this.dropItemRand(Items.ITEM_FRAME, 1);
        for (int i = 0; i < 20; i++) {
            double px = this.getX() + (this.random.nextFloat() - 0.5F) * 8.0F;
            double py = this.getY() + 2.0 + (this.random.nextFloat() - 0.5F) * 4.0F;
            double pz = this.getZ() + (this.random.nextFloat() - 0.5F) * 8.0F;
            // gold EXPLOSION_LARGE — must send from server so clients see it
            level.sendParticles(ParticleTypes.EXPLOSION, px, py, pz, 1, 0.0, 0.0, 0.0, 0.0);
        }
        for (int i = 0; i < 53; i++) {
            this.dropItemRand(Items.GOLD_NUGGET, 1);
        }
        for (int i = 0; i < 3; i++) {
            this.dropItemRand(Items.BLAZE_ROD, 1);
        }
        this.dropItemRand(Items.NETHER_STAR, 1);
        // port extra: moth scales (crafting; not in gold drop list)
        for (int i = 0; i < 8; i++) {
            this.dropItemRand(ModItems.MOTH_SCALE.get(), 1);
        }
        for (int i = 0; i < 20; i++) {
            spawnCreature(
                    level,
                    ModEntities.MOTH.get(),
                    this.getX() + 0.5,
                    this.getY() + 1.0,
                    this.getZ() + 0.5);
        }
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        Entity e = source.getEntity();
        if (e instanceof Mothra) {
            return false;
        }
        boolean ret = super.hurt(source, amount);
        if (e != null && this.currentFlightTarget != null) {
            this.currentFlightTarget = BlockPos.containing(e.getX(), e.getY() + 2.0, e.getZ());
        }
        return ret;
    }

    /** Gold canSeeTarget — ray from eye-ish height to waypoint; MISS means clear. */
    private boolean canSeeTarget(double pX, double pY, double pZ) {
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
    protected void customServerAiStep() {
        super.customServerAiStep();
        if (this.isDeadOrDying()) {
            return;
        }

        // gold: shoot = 3, HARD → 2
        int shoot = this.level().getDifficulty() == Difficulty.HARD ? 2 : 3;

        if ((int) this.getX() == this.lastX && (int) this.getY() == this.lastY && (int) this.getZ() == this.lastZ) {
            this.stuckCount++;
        } else {
            this.stuckCount = 0;
            this.lastX = (int) this.getX();
            this.lastY = (int) this.getY();
            this.lastZ = (int) this.getZ();
        }

        if (this.currentFlightTarget == null) {
            this.currentFlightTarget = BlockPos.containing(this.getX(), this.getY(), this.getZ());
        }

        // gold: repath if stuck > 50, random 1/300, or within 9 of target
        if (this.stuckCount > 50
                || this.random.nextInt(300) == 0
                || this.currentFlightTarget.distToCenterSqr(this.getX(), this.getY(), this.getZ()) < 9.0) {
            int down = 0;
            int dist = 20;
            // gold: scan ground distance under a 3×3 pattern of columns
            for (int i = -5; i <= 5; i += 5) {
                for (int j = -5; j <= 5; j += 5) {
                    for (int k = 1; k < 20; k++) {
                        BlockPos probe = BlockPos.containing(this.getX() + j, this.getY() - k, this.getZ() + i);
                        if (!this.level().getBlockState(probe).isAir()) {
                            if (k < dist) {
                                dist = k;
                            }
                            break;
                        }
                    }
                }
            }
            if (dist > 10) {
                down = dist - 10 + 1;
            }

            int keepTrying = 50;
            BlockState bid = net.minecraft.world.level.block.Blocks.STONE.defaultBlockState();
            while (!bid.isAir() && keepTrying != 0) {
                keepTrying--;
                int xdir = this.random.nextInt(2) == 0 ? -1 : 1;
                int zdir = this.random.nextInt(2) == 0 ? -1 : 1;
                int newx = (this.random.nextInt(20) + 8) * xdir;
                int newz = (this.random.nextInt(20) + 8) * zdir;
                this.currentFlightTarget = BlockPos.containing(
                        this.getX() + newx,
                        this.getY() + this.random.nextInt(7) - 1 - down,
                        this.getZ() + newz);
                bid = this.level().getBlockState(this.currentFlightTarget);
                // gold: if air but cannot see, treat as solid and retry
                if (bid.isAir()
                        && !this.canSeeTarget(
                                this.currentFlightTarget.getX(),
                                this.currentFlightTarget.getY(),
                                this.currentFlightTarget.getZ())) {
                    bid = net.minecraft.world.level.block.Blocks.STONE.defaultBlockState();
                }
            }
            this.stuckCount = 0;
        } else if (this.random.nextInt(10) == 0 && this.level().getDifficulty() != Difficulty.PEACEFUL) {
            // gold: getClosestEntity(Player, expand 25,20,25)
            Player target = this.findNearestPlayer(25.0, 20.0, 25.0);
            if (target != null) {
                if (!target.getAbilities().instabuild) {
                    if (this.hasLineOfSight(target)) {
                        this.currentFlightTarget =
                                BlockPos.containing(target.getX(), target.getY() + 4.0, target.getZ());
                        if (this.random.nextInt(shoot) == 0) {
                            this.attackWithSomething(target);
                        }
                    }
                } else {
                    target = null;
                }
            }
            // gold: only hunt other living if no player target, 1/3
            if (target == null && this.random.nextInt(3) == 0) {
                LivingEntity e = this.findSomethingToAttack();
                if (e != null) {
                    this.currentFlightTarget = BlockPos.containing(e.getX(), e.getY() + 5.0, e.getZ());
                    if (this.random.nextInt(shoot) == 0) {
                        this.attackWithSomething(e);
                    }
                }
            }
        }

        double var1 = this.currentFlightTarget.getX() + 0.5 - this.getX();
        double var3 = this.currentFlightTarget.getY() + 0.1 - this.getY();
        double var5 = this.currentFlightTarget.getZ() + 0.5 - this.getZ();
        Vec3 m = this.getDeltaMovement();
        // gold flight lerp formulas
        this.setDeltaMovement(
                m.x + (Math.signum(var1) * 0.5 - m.x) * 0.30001,
                m.y + (Math.signum(var3) * 0.7 - m.y) * 0.20001,
                m.z + (Math.signum(var5) * 0.5 - m.z) * 0.30001);
        float var7 = (float) (Math.atan2(this.getDeltaMovement().z, this.getDeltaMovement().x) * (180.0 / Math.PI))
                - 90.0F;
        float var8 = Mth.wrapDegrees(var7 - this.getYRot());
        this.setZza(1.0F);
        this.setYRot(this.getYRot() + var8 / 4.0F);
    }

    /**
     * Gold attackWithSomething — difficulty-scaled fireballs from mouth offset.
     * EASY: SmallFireball; NORMAL: 50/50 Small / BetterFireball+setNotMe; HARD: BetterFireball+setNotMe.
     */
    private void attackWithSomething(LivingEntity par1) {
        if (this.level().getDifficulty() == Difficulty.PEACEFUL) {
            return;
        }
        double xzoff = 2.25;
        double yoff = 0.0;
        double cx = this.getX() - xzoff * Math.sin(Math.toRadians(this.getYRot()));
        double cz = this.getZ() + xzoff * Math.cos(Math.toRadians(this.getYRot()));
        // gold: target.x - cx, target.y + 0.55 - (this.y + yoff), target.z - cz
        double dx = par1.getX() - cx;
        double dy = par1.getY() + 0.55 - (this.getY() + yoff);
        double dz = par1.getZ() - cz;
        Vec3 dir = new Vec3(dx, dy, dz);

        Difficulty diff = this.level().getDifficulty();
        if (diff == Difficulty.EASY) {
            SmallFireball sf = new SmallFireball(this.level(), this, dir.normalize());
            sf.setPos(cx, this.getY() + yoff, cz);
            this.playSound(SoundEvents.BLAZE_SHOOT, 0.75F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
            this.level().addFreshEntity(sf);
        } else if (diff == Difficulty.NORMAL) {
            if (this.random.nextInt(2) == 0) {
                SmallFireball sf = new SmallFireball(this.level(), this, dir.normalize());
                sf.setPos(cx, this.getY() + yoff, cz);
                this.playSound(SoundEvents.BLAZE_SHOOT, 0.75F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
                this.level().addFreshEntity(sf);
            } else {
                BetterFireball bf = new BetterFireball(this.level(), this, dir.normalize());
                bf.setPos(cx, this.getY() + yoff, cz);
                bf.setNotMe();
                this.playSound(SoundEvents.GHAST_SHOOT, 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
                this.level().addFreshEntity(bf);
            }
        } else {
            // HARD (and anything else non-peaceful)
            BetterFireball bf = new BetterFireball(this.level(), this, dir.normalize());
            bf.setPos(cx, this.getY() + yoff, cz);
            bf.setNotMe();
            this.playSound(SoundEvents.GHAST_SHOOT, 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
            this.level().addFreshEntity(bf);
        }

        if (this.getHealth() < this.mygetMaxHealth()) {
            this.heal(1.0F);
        }
    }

    private boolean isSuitableTarget(LivingEntity par1EntityLiving) {
        if (this.level().getDifficulty() == Difficulty.PEACEFUL) {
            return false;
        }
        if (par1EntityLiving == null || par1EntityLiving == this || !par1EntityLiving.isAlive()) {
            return false;
        }
        if (!this.hasLineOfSight(par1EntityLiving)) {
            return false;
        }
        // gold: skip Mothra, Brutalfly, VelocityRaptor, Cryolophosaurus, Mantis
        if (par1EntityLiving instanceof Mothra
                || par1EntityLiving instanceof Brutalfly
                || par1EntityLiving instanceof VelocityRaptor
                || par1EntityLiving instanceof Cryolophosaurus
                || par1EntityLiving instanceof Mantis) {
            return false;
        }
        if (par1EntityLiving instanceof Player p && p.getAbilities().instabuild) {
            return false;
        }
        return true;
    }

    /** Gold {@code getClosestEntity(Player, expand dx,dy,dz)} — nearest non-self player in box. */
    @Nullable
    private Player findNearestPlayer(double dx, double dy, double dz) {
        List<Player> list =
                this.level().getEntitiesOfClass(Player.class, this.getBoundingBox().inflate(dx, dy, dz));
        Player nearest = null;
        double best = Double.MAX_VALUE;
        for (Player p : list) {
            if (!p.isAlive()) {
                continue;
            }
            double d = this.distanceToSqr(p);
            if (d < best) {
                best = d;
                nearest = p;
            }
        }
        return nearest;
    }

    @Nullable
    private LivingEntity findSomethingToAttack() {
        // gold: PlayNicely != 0 → null
        if (OreSpawnMain.PlayNicely != 0) {
            return null;
        }
        List<LivingEntity> list = this.level()
                .getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(15.0, 20.0, 15.0));
        // gold GenericTargetSorter — nearest first
        list.sort(Comparator.comparingDouble(this::distanceToSqr));
        for (LivingEntity living : list) {
            if (this.isSuitableTarget(living)) {
                return living;
            }
        }
        return null;
    }
}
