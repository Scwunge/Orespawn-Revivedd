package danger.orespawn.items.tools;

import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.Tier;

/** Gold GenericHoe (amethyst etc.). */
public class GenericHoe extends HoeItem {
    public GenericHoe(Tier tier, Properties properties) {
        super(tier, properties);
    }

    public GenericHoe(OrespawnToolMaterial material, Properties properties) {
        this(material.tier, properties);
    }
}
