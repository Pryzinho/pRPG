package br.pryzat.rpg.api.items;

import br.pryzat.rpg.builds.items.Grisaia;
import br.pryzat.rpg.main.RpgMain;

import java.util.HashMap;

public class ItemManager {
    private RpgMain main;
    private HashMap<String, ConsumableItem> items;

    public ItemManager(RpgMain main) {
        this.main = main;
        this.items = new HashMap<>();
    }

    public void registerAllItems() {
        items.put("grisaia", new Grisaia(1));
    }

   public ConsumableItem getItem(String iuid){
        return items.get(iuid);
   }
   public HashMap<String, ConsumableItem> getItems(){
        return items;
   }

}
