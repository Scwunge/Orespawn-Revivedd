package danger.orespawn.items.tools;

import danger.orespawn.entity.Fairy;
import danger.orespawn.init.ModEntities;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.level.Level;

/**
 * 1.7.10 {@code FairySword} — emerald-tier blade.
 * On hit: spawn 1+rand(3) Fairies with {@link Fairy#setOwner(LivingEntity)} = attacker.
 */
public class FairySword extends SwordItem {
    public FairySword(Properties properties) {
        super(OrespawnToolMaterial.EmeraldTools.tier, properties);
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        // gold hitEntity: server-side spawn 1+nextInt(3) Fairy near target, setOwner(attacker)
        if (target != null && !target.level().isClientSide) {
            Level level = target.level();
            int num = 1 + target.getRandom().nextInt(3);
            for (int i = 0; i < num; i++) {
                Fairy fairy = ModEntities.FAIRY.get().create(level);
                if (fairy != null) {
                    double x = target.getX()
                            + (target.getRandom().nextFloat() - target.getRandom().nextFloat()) * 0.5;
                    double y = target.getY() + target.getRandom().nextFloat() + 0.01;
                    double z = target.getZ()
                            + (target.getRandom().nextFloat() - target.getRandom().nextFloat()) * 0.5;
                    fairy.moveTo(x, y, z, target.getRandom().nextFloat() * 360.0F, 0.0F);
                    fairy.setOwner(attacker);
                    level.addFreshEntity(fairy);
                }
            }
        }
        return super.hurtEnemy(stack, target, attacker);
    }
}
