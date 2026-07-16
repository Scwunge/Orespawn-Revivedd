package danger.orespawn.items.tools;

import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.Tier;

/** Gold GenericAxe (amethyst etc.). */
public class GenericAxe extends AxeItem {
    public GenericAxe(Tier tier, Properties properties) {
        super(tier, properties);
    }

    public GenericAxe(OrespawnToolMaterial material, Properties properties) {
        this(material.tier, properties);
    }
}
