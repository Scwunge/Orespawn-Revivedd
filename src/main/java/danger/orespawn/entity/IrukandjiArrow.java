package danger.orespawn.entity;

import danger.orespawn.init.ModEntities;
import danger.orespawn.items.tools.BattleAxe;
import javax.annotation.Nullable;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

/**
 * Gold {@code IrukandjiArrow} (EntityArrow). Fixed hit damage <b>100</b> (crit adds random).
 * Ground life 50 ticks then drop {@code irukandjiarrow}. PVP gate when
 * {@link BattleAxe#ultimate_sword_pvp} == 0: players / GF / BF / tamed pets → sound + discard, no dmg.
 * Skips Elevator / mounted Cephadrome / Dragon / Horse.
 * <p>
 * Registry: {@code irukandji_arrow}.
 */
public class IrukandjiArrow extends AbstractArrow {
    /** Gold fixed damage (func_70242_d / onImpact). */
    public static final float IRUKANDJI_DAMAGE = 100.0F;
    /** Gold ticksInGround before drop+kill. */
    public static final int GROUND_LIFE_TICKS = 50;

    private int goldGroundLife = 0;
    private int knockbackStrength = 0;

    public IrukandjiArrow(EntityType<? extends IrukandjiArrow> type, Level level) {
        super(type, level);
        this.setBaseDamage(IRUKANDJI_DAMAGE);
    }

    public IrukandjiArrow(Level level, LivingEntity owner, ItemStack pickup, @Nullable ItemStack weapon) {
        super(ModEntities.IRUKANDJI_ARROW.get(), owner, level, pickup, weapon);
        this.setBaseDamage(IRUKANDJI_DAMAGE);
    }

    public IrukandjiArrow(Level level, LivingEntity owner) {
        this(level, owner, irukandjiPickupStack(), null);
    }

    public IrukandjiArrow(Level level, double x, double y, double z) {
        super(ModEntities.IRUKANDJI_ARROW.get(), x, y, z, level, irukandjiPickupStack(), null);
        this.setBaseDamage(IRUKANDJI_DAMAGE);
    }

    private static ItemStack irukandjiPickupStack() {
        Item item = BuiltInRegistries.ITEM.get(
                ResourceLocation.fromNamespaceAndPath("orespawn", "irukandjiarrow"));
        if (item != null && item != Items.AIR) {
            return new ItemStack(item);
        }
        return new ItemStack(Items.ARROW);
    }

    @Override
    protected ItemStack getDefaultPickupItem() {
        return irukandjiPickupStack();
    }

    /** Gold {@code setKnockbackStrength}. */
    public void setKnockbackStrength(int strength) {
        this.knockbackStrength = strength;
    }

    @Override
    public void tick() {
        this.setBaseDamage(IRUKANDJI_DAMAGE);
        super.tick();
        if (this.inGround) {
            this.goldGroundLife++;
            if (this.goldGroundLife >= GROUND_LIFE_TICKS && !this.level().isClientSide) {
                // gold: drop MyIrukandjiArrow then die
                this.spawnAtLocation(this.getPickupItemStackOrigin().copy(), 0.1F);
                this.discard();
            }
        } else {
            this.goldGroundLife = 0;
        }
    }

    @Override
    protected boolean canHitEntity(Entity target) {
        if (target instanceof Elevator) {
            return false;
        }
        if (target instanceof Cephadrome ceph && ceph.getFirstPassenger() != null) {
            return false;
        }
        if (target instanceof Dragon dragon && dragon.getFirstPassenger() != null) {
            return false;
        }
        if (target instanceof AbstractHorse horse && horse.getFirstPassenger() != null) {
            return false;
        }
        return super.canHitEntity(target);
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        Entity hit = result.getEntity();

        // gold ultimate_sword_pvp == 0: no dmg to player / GF / BF / tamed; sound + discard
        if (BattleAxe.ultimate_sword_pvp == 0) {
            if (hit instanceof Player
                    || hit instanceof Girlfriend
                    || hit instanceof Boyfriend
                    || isTamedPet(hit)) {
                this.playSound(
                        SoundEvents.ARROW_HIT,
                        1.0F,
                        1.2F / (this.random.nextFloat() * 0.2F + 0.9F));
                this.discard();
                return;
            }
        }

        float dmg = IRUKANDJI_DAMAGE;
        if (this.isCritArrow()) {
            dmg += this.random.nextInt((int) (dmg / 2.0F) + 2);
        }

        Entity owner = this.getOwner();
        if (this.isOnFire()) {
            hit.igniteForSeconds(5);
        }

        boolean damaged = hit.hurt(this.damageSources().arrow(this, owner), dmg);
        if (damaged) {
            if (hit instanceof LivingEntity living) {
                if (!this.level().isClientSide) {
                    living.setArrowCount(living.getArrowCount() + 1);
                }
                if (this.knockbackStrength > 0) {
                    Vec3 motion = this.getDeltaMovement();
                    double h = Math.sqrt(motion.x * motion.x + motion.z * motion.z);
                    if (h > 0.0) {
                        hit.push(
                                motion.x * this.knockbackStrength * 0.6F / h,
                                0.1,
                                motion.z * this.knockbackStrength * 0.6F / h);
                    }
                }
                if (owner instanceof ServerPlayer
                        && hit instanceof Player
                        && hit != owner) {
                    // gold S2BPacketChangeGameState(6) — shield disable feedback
                }
            }
            this.playSound(
                    SoundEvents.ARROW_HIT,
                    1.0F,
                    1.2F / (this.random.nextFloat() * 0.2F + 0.9F));
            this.discard();
        } else {
            // gold: bounce
            this.setDeltaMovement(this.getDeltaMovement().scale(-0.1));
            this.setYRot(this.getYRot() + 180.0F);
            this.yRotO += 180.0F;
        }
    }

    private static boolean isTamedPet(Entity hit) {
        return hit instanceof TamableAnimal tame && tame.isTame();
    }

    /** Gold damage report. */
    public double getGoldDamage() {
        return IRUKANDJI_DAMAGE;
    }
}
