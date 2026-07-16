package danger.orespawn.items.tools;

import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;

/** Gold GenericSword (amethyst etc.). */
public class GenericSword extends SwordItem {
    public GenericSword(Tier tier, Properties properties) {
        super(tier, properties);
    }

    public GenericSword(OrespawnToolMaterial material, Properties properties) {
        this(material.tier, properties);
    }
}
