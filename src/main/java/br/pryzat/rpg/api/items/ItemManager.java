package br.pryzat.rpg.api.items;

import br.pryzat.rpg.api.events.bukkit.CharacterChooseClassEvent;
import br.pryzat.rpg.builds.items.Grisaia;
import br.pryzat.rpg.main.RpgMain;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class ItemManager implements Listener {
    private RpgMain main;
    private HashMap<String, ConsumableItem> items;

    public ItemManager(RpgMain main) {
        this.main = main;
        this.items = new HashMap<>();
        Bukkit.getPluginManager().registerEvents(this, main);
    }

    public void loadAllItems() {
        items.put("grisaia", new Grisaia(1));
        loadAllPackages();
    }


    private void loadAllPackages() {
        // Package: Força aos Novatos (Formally known as 'fn', registered in: rpg.consumableitems.fn).
        PackageItem fn = new PackageItem("fn", "&cForça aos Novatos", Material.ENDER_CHEST, 1);
        List<String> fnl = new ArrayList<>(); //Item lore
        fnl.add("&aPacote especial para novos jogadores&f.");
        fn.setLore(fnl);
        List<ItemStack> fni = new ArrayList<>(); //Pakcage Items
        fni.add(
                new CustomItem(Material.NETHERITE_HOE)
                        .setName("&dFoice do Aprendiz")
                        .setLore(Arrays.asList("&bColeta o dobro de XP em monstros abaixo do nivel 20&f."))
                        .hideEnchants(true)
                        .hideAttributes(true)
                        .toItemStack()
        );
        fni.add(new ItemStack(Material.DIAMOND, 10));
        fn.setItems(fni);
        put(fn);

    }

    public ConsumableItem getItem(String iuid) {
        return items.get(iuid);
    }

    public HashMap<String, ConsumableItem> getItems() {
        return items;
    }

    /**
     * Coloca ou substitui um item na lista de itens registrados.
     *
     * @param i Item
     */
    public void put(ConsumableItem i) {
        if (items.containsKey(i.getIUID())) {
            items.replace(i.getIUID(), i);
        } else {
            items.put(i.getIUID(), i);
        }
    }


    //
    // Listener's
    //

    @EventHandler
    public void onClazzSelect(CharacterChooseClassEvent e){
        if (e.isCancelled())return;
        if (!e.isFirst())return;
        getItem("fn").execute(e.getTrigger());
    }
}
