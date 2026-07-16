package danger.orespawn.items;

import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

/**
 * Gold {@code ItemCreeperLauncher} — registers as {@code creeperlauncher}, tools tab.
 * <p>
 * Gold: {@code setMaxDamage(1)}; {@code onLeftClickEntity} on a creeper:
 * smoke / explode / reddust FX, fireworks.launch SFX, vertical push 4.5, shrink stack
 * unless creative. Return true cancels default attack.
 * <p>
 * Inventory icon: {@code textures/item/creeperlauncher.png}. Register in {@code ModItems}.
 */
public class ItemCreeperLauncher extends Item {
    /** Gold vertical knock/launch amount. */
    public static final double LAUNCH_Y = 4.5;
    /** Gold particle burst count per type group. */
    public static final int FX_BURSTS = 6;

    public ItemCreeperLauncher(Properties properties) {
        super(properties);
    }

    /**
     * NeoForge hook matching gold {@code onLeftClickEntity}.
     * Return true to cancel the default attack.
     */
    @Override
    public boolean onLeftClickEntity(ItemStack stack, Player player, Entity entity) {
        if (!(entity instanceof Creeper)) {
            return false;
        }

        Level level = player.level();
        launchFx(level, entity);

        // gold: "fireworks.launch" 2.0F, 1.2F
        level.playSound(
                null,
                player.getX(),
                player.getY(),
                player.getZ(),
                SoundEvents.FIREWORK_ROCKET_LAUNCH,
                SoundSource.PLAYERS,
                2.0F,
                1.2F);

        // gold: EntityLiving.addVelocity(0, 4.5, 0)
        entity.push(0.0, LAUNCH_Y, 0.0);
        entity.hasImpulse = true;

        if (!player.getAbilities().instabuild) {
            stack.shrink(1);
        }

        return true;
    }

    private static void launchFx(Level level, Entity entity) {
        for (int i = 0; i < FX_BURSTS; i++) {
            float f1 = level.random.nextFloat() - level.random.nextFloat();
            float f2 = 0.25F + level.random.nextFloat() * 6.0F;
            float f3 = level.random.nextFloat() - level.random.nextFloat();
            double px = entity.getX() + f1;
            double py = entity.getY() + f2;
            double pz = entity.getZ() + f3;
            double vy = f2 / 4.0F;

            if (level instanceof ServerLevel server) {
                // gold: smoke, explode, reddust with upward-biased motion
                server.sendParticles(ParticleTypes.SMOKE, px, py, pz, 1, 0.0, vy, 0.0, 0.0);

                f1 = level.random.nextFloat() - level.random.nextFloat();
                f2 = 0.25F + level.random.nextFloat() * 6.0F;
                f3 = level.random.nextFloat() - level.random.nextFloat();
                px = entity.getX() + f1;
                py = entity.getY() + f2;
                pz = entity.getZ() + f3;
                vy = f2 / 4.0F;
                server.sendParticles(ParticleTypes.EXPLOSION, px, py, pz, 1, 0.0, vy, 0.0, 0.0);

                f1 = level.random.nextFloat() - level.random.nextFloat();
                f2 = 0.25F + level.random.nextFloat() * 6.0F;
                f3 = level.random.nextFloat() - level.random.nextFloat();
                px = entity.getX() + f1;
                py = entity.getY() + f2;
                pz = entity.getZ() + f3;
                vy = f2 / 4.0F;
                server.sendParticles(DustParticleOptions.REDSTONE, px, py, pz, 1, 0.0, vy, 0.0, 0.0);
            }
        }
    }
}
