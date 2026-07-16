package danger.orespawn.entity;

import danger.orespawn.init.ModEntities;
import danger.orespawn.items.tools.BattleAxe;
import javax.annotation.Nullable;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;

/**
 * Gold {@code UltimateArrow} (EntityArrow). Damage = ceil(speed × {@link #UltimateBowDamage})
 * via {@link #setBaseDamage}; crit adds random; PVP gate when
 * {@link BattleAxe#ultimate_sword_pvp} == 0 heals players/tamed pets by 1 instead of damaging.
 * Ground despawn 500 ticks (gold ticksInGround).
 * <p>
 * Registry: {@code ultimate_arrow}.
 */
public class UltimateArrow extends AbstractArrow {
    /**
     * Gold {@code OreSpawnMain.UltimateBowDamage} (config default 10, clamped 2–20 in gold).
     * May sync from config later.
     */
    public static int UltimateBowDamage = 10;

    /** Gold ticksInGround counter (vanilla life is private 1200). */
    private int goldGroundLife = 0;

    public UltimateArrow(EntityType<? extends UltimateArrow> type, Level level) {
        super(type, level);
        this.setBaseDamage(UltimateBowDamage);
    }

    public UltimateArrow(Level level, LivingEntity owner, ItemStack pickup, @Nullable ItemStack weapon) {
        super(ModEntities.ULTIMATE_ARROW.get(), owner, level, pickup, weapon);
        this.setBaseDamage(UltimateBowDamage);
    }

    public UltimateArrow(Level level, LivingEntity owner) {
        this(level, owner, new ItemStack(Items.ARROW), null);
    }

    public UltimateArrow(Level level, double x, double y, double z) {
        super(ModEntities.ULTIMATE_ARROW.get(), x, y, z, level, new ItemStack(Items.ARROW), null);
        this.setBaseDamage(UltimateBowDamage);
    }

    @Override
    protected ItemStack getDefaultPickupItem() {
        return new ItemStack(Items.ARROW);
    }

    @Override
    public void tick() {
        if (this.getBaseDamage() != UltimateBowDamage) {
            this.setBaseDamage(UltimateBowDamage);
        }
        super.tick();
        if (this.inGround) {
            this.goldGroundLife++;
            if (this.goldGroundLife >= 500) {
                this.discard();
            }
        } else {
            this.goldGroundLife = 0;
        }
    }

    @Override
    protected boolean canHitEntity(Entity target) {
        if (target instanceof Cephadrome ceph && ceph.getFirstPassenger() != null) {
            return false;
        }
        if (target instanceof Dragon dragon && dragon.getFirstPassenger() != null) {
            return false;
        }
        if (target instanceof AbstractHorse horse && horse.getFirstPassenger() != null) {
            return false;
        }
        // gold Elevator skip — Elevator entity not ported
        return super.canHitEntity(target);
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        Entity hit = result.getEntity();

        // gold ultimate_sword_pvp == 0: heal players / tamed instead of damage
        if (BattleAxe.ultimate_sword_pvp == 0) {
            if (hit instanceof Player || isFriendlyHealTarget(hit)) {
                this.playSound(SoundEvents.ARROW_HIT, 1.0F, 1.2F / (this.random.nextFloat() * 0.2F + 0.9F));
                if (hit instanceof LivingEntity living) {
                    living.heal(1.0F);
                }
                this.discard();
                return;
            }
        }

        this.setBaseDamage(UltimateBowDamage);
        super.onHitEntity(result);
    }

    private static boolean isFriendlyHealTarget(Entity hit) {
        if (hit instanceof TamableAnimal tame && tame.isTame()) {
            return true;
        }
        // Girlfriend / Boyfriend not ported yet
        return false;
    }

    /** Gold damage report for UI / bow. */
    public double getGoldDamage() {
        return UltimateBowDamage;
    }
}
