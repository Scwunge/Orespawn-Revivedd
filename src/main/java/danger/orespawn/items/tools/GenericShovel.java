package danger.orespawn.items.tools;

import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.Tier;

/** Gold GenericShovel (amethyst etc.). */
public class GenericShovel extends ShovelItem {
    public GenericShovel(Tier tier, Properties properties) {
        super(tier, properties);
    }

    public GenericShovel(OrespawnToolMaterial material, Properties properties) {
        this(material.tier, properties);
    }
}
