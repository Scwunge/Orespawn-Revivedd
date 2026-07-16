package danger.orespawn.items;

import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

/**
 * Gold {@code ItemZooKeeper} ("ZooKeeper Shard") — marks living mobs as persistent (no despawn).
 * <p>
 * Gold: creative tab materials, durability 1; {@code onLeftClickEntity} calls
 * {@code EntityLiving.func_110163_bv()} ({@link Mob#setPersistenceRequired()}) and
 * damages the shard by 2 (one-shot).
 */
public class ItemZooKeeper extends Item {
    /** Gold {@code setMaxDamage(1)}. */
    public static final int ZOOKEEPER_USES = 1;

    public ItemZooKeeper(Properties properties) {
        super(properties);
    }

    /**
     * NeoForge hook matching gold {@code onLeftClickEntity}.
     * Return true to cancel the default attack.
     */
    @Override
    public boolean onLeftClickEntity(ItemStack stack, Player player, Entity entity) {
        Level level = player.level();
        zooFx(level, entity);

        if (entity instanceof LivingEntity living) {
            if (living instanceof Mob mob) {
                // gold EntityLiving.func_110163_bv → setPersistenceRequired
                mob.setPersistenceRequired();
            } else {
                // non-Mob LivingEntity: no despawn API; still consume shard for FX parity
            }

            InteractionHand hand =
                    player.getMainHandItem() == stack ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND;
            stack.hurtAndBreak(2, player, LivingEntity.getSlotForHand(hand));
            return true;
        }
        return false;
    }

    private void zooFx(Level level, Entity entity) {
        for (int i = 0; i < 8; i++) {
            float f1 = level.random.nextFloat() * 3.0F - level.random.nextFloat() * 3.0F;
            float f2 = 0.25F + level.random.nextFloat() * 2.0F;
            float f3 = level.random.nextFloat() * 3.0F - level.random.nextFloat() * 3.0F;
            double px = entity.getX() + f1;
            double py = entity.getY() + f2;
            double pz = entity.getZ() + f3;
            if (level instanceof ServerLevel server) {
                server.sendParticles(ParticleTypes.SMOKE, px, py, pz, 1, 0.0, 0.0, 0.0, 0.0);
                server.sendParticles(ParticleTypes.EXPLOSION, px, py, pz, 1, 0.0, 0.0, 0.0, 0.0);
                server.sendParticles(DustParticleOptions.REDSTONE, px, py, pz, 1, 0.0, 0.0, 0.0, 0.0);
            }
        }
        level.playSound(
                null,
                entity.getX(),
                entity.getY(),
                entity.getZ(),
                SoundEvents.GENERIC_EXPLODE.value(),
                SoundSource.PLAYERS,
                0.5F,
                1.5F);
    }
}
