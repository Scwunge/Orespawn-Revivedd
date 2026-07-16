package danger.orespawn.items.tools;

import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.Tier;

/** Gold GenericPickaxe (amethyst etc.). */
public class GenericPickaxe extends PickaxeItem {
    public GenericPickaxe(Tier tier, Properties properties) {
        super(tier, properties);
    }

    public GenericPickaxe(OrespawnToolMaterial material, Properties properties) {
        this(material.tier, properties);
    }
}
