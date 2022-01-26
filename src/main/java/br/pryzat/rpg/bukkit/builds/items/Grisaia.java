package br.pryzat.rpg.bukkit.builds.items;

import br.pryzat.rpg.bukkit.api.items.ConsumableItem;
import org.bukkit.Material;

import java.util.List;

public class Grisaia extends ConsumableItem {

    public Grisaia(int amount) {
        super("grisaia", "&dFruto da Grisaia", Material.APPLE, amount, null);
    }

    @Override
    public void execute(Object obj) {
        
    }
}
