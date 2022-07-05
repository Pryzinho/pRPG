package br.pryzat.rpg.builds.items;

import br.pryzat.rpg.api.items.ConsumableItem;
import org.bukkit.Material;
import org.bukkit.plugin.java.JavaPlugin;

public class Grisaia extends ConsumableItem {

    public Grisaia(JavaPlugin pl, int amount) {
        super(pl, "grisaia", "&dFruto da Grisaia", Material.APPLE, amount, null);
    }

    @Override
    public void execute(Object obj) {
        
    }

}
