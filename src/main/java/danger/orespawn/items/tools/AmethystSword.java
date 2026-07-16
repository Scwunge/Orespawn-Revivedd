package danger.orespawn.items.tools;

import net.minecraft.world.item.SwordItem;

/**
 * Gold {@code AmethystSword} — distinct from material-only {@link GenericSword}.
 * <p>
 * Gold facts:
 * <ul>
 *   <li>{@code weaponDamage = 18} flat ({@code getDamageVsEntity})</li>
 *   <li>{@code setMaxDamage(2000)} (overrides AmethystTools durability 1000)</li>
 *   <li>max use duration 3500 (cosmetic in 1.7; not needed for swords in 1.21)</li>
 *   <li>stack size 1, combat tab</li>
 * </ul>
 * 1.21 attributes: total attack = tier.bonus (3) + {@link #SWORD_DAMAGE_BONUS} (15) = <b>18</b>.
 * Register with {@code AmethystSword} instead of {@code GenericSword} for
 * {@code amethyst_sword}.
 */
public class AmethystSword extends SwordItem {
    /** Gold flat damage 18 → bonus so total with AmethystTools.tier (3) is 18. */
    public static final float SWORD_DAMAGE_BONUS = 15.0F;
    public static final float SWORD_ATTACK_SPEED = -2.4F;
    /** Gold {@code setMaxDamage(2000)}. */
    public static final int AMETHYST_SWORD_USES = 2000;

    public AmethystSword(Properties properties) {
        super(OrespawnToolMaterial.AmethystTools.tier, properties);
    }
}
