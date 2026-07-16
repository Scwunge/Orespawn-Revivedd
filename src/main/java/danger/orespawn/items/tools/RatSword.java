package danger.orespawn.items.tools;

import danger.orespawn.entity.Rat;
import danger.orespawn.init.ModEntities;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.level.Level;

/**
 * 1.7.10 {@code RatSword} — emerald-tier blade.
 * Gold hit spawns 1+rand(6) owned Rats around the target.
 */
public class RatSword extends SwordItem {
    public RatSword(Properties properties) {
        super(OrespawnToolMaterial.EmeraldTools.tier, properties);
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (target != null && !target.level().isClientSide) {
            Level level = target.level();
            int num = 1 + level.getRandom().nextInt(6);
            for (int i = 0; i < num; i++) {
                Rat r = ModEntities.RAT.get().create(level);
                if (r != null) {
                    double x = target.getX()
                            + (level.getRandom().nextFloat() - level.getRandom().nextFloat()) * 0.5;
                    double y = target.getY() + level.getRandom().nextFloat() + 0.01;
                    double z = target.getZ()
                            + (level.getRandom().nextFloat() - level.getRandom().nextFloat()) * 0.5;
                    r.moveTo(x, y, z, level.getRandom().nextFloat() * 360.0F, 0.0F);
                    r.setOwner(attacker);
                    if (level instanceof ServerLevel server) {
                        server.addFreshEntityWithPassengers(r);
                    } else {
                        level.addFreshEntity(r);
                    }
                }
            }
        }
        return super.hurtEnemy(stack, target, attacker);
    }
}
