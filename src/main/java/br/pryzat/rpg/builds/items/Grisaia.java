package br.pryzat.rpg.builds.items;

import br.pryzat.rpg.api.items.ConsumableItem;
import org.bukkit.Material;

public class Grisaia extends ConsumableItem {

    public Grisaia(int amount) {
        super("grisaia", "&dFruto da Grisaia", Material.APPLE, amount, null);
    }

    @Override
    public void execute(Object obj) {
        
    }

}
