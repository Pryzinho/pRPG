package br.pryzat.rpg.builds.items;

import br.pryzat.rpg.api.items.types.ConsumableItem;
import br.pryzat.rpg.main.RpgMain;
import org.bukkit.Material;
import org.bukkit.plugin.java.JavaPlugin;

public class Grisaia extends ConsumableItem {

    public Grisaia(RpgMain pl, int amount) {
        super(pl, "grisaia", Material.APPLE);
        displayName("<purple>Fruto da Grisaia");
    }

    @Override
    public void execute(Object obj) {
        
    }

}
