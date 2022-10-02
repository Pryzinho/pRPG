package br.pryzat.rpg.api.items;

import br.pryzat.rpg.api.characters.stats.Attributes;
import br.pryzat.rpg.api.events.bukkit.character.CharacterChooseClassEvent;
import br.pryzat.rpg.builds.items.Grisaia;
import br.pryzat.rpg.main.RpgMain;
import br.pryzat.rpg.utils.PryConfig;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class ItemManager implements Listener {
    private static RpgMain main;
    private HashMap<String, ConsumableItem> items;

    public ItemManager(RpgMain pl) {
        main = pl;
        this.items = new HashMap<>();
        Bukkit.getPluginManager().registerEvents(this, main);
    }


    public void loadAllItems() {
        items.put("grisaia", new Grisaia(main, 1));
        loadAllPackages();
    }


    private void loadAllPackages() {
        // Package: Força aos Novatos (Formally known as 'fn', registered in: rpg.consumableitems.fn).
        PackageItem fn = new PackageItem(main, "fn", "&cForça aos Novatos", Material.ENDER_CHEST, 1);
        List<String> fnl = new ArrayList<>(); //Item lore
        fnl.add("&aPacote especial para novos jogadores&f.");
        fn.setLore(fnl);
        List<ItemStack> fni = new ArrayList<>(); //Pakcage Items
        fni.add(
                new CustomItem(main, Material.NETHERITE_HOE)
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
    public void onPackageOpen(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        if (!e.getAction().isRightClick()) return;
        ItemStack is = p.getInventory().getItemInMainHand();
        if (is == null) return;
        if (!is.getItemMeta().getPersistentDataContainer().has(new NamespacedKey(main, "rpg.consumableitems.uid")))
            return;
        items.keySet().forEach(key -> {
            if (is.getItemMeta().getPersistentDataContainer().get(new NamespacedKey(main, "rpg.consumableitems.uid"), PersistentDataType.STRING).equals(key)) {
                p.getInventory().setItemInMainHand(new ItemStack(Material.AIR));
                items.get(key).execute(main.getCharacterManager().getCharacter(p.getUniqueId()));
            }
        });

    }

    @EventHandler
    public void onClazzSelect(CharacterChooseClassEvent e) {
        if (e.isCancelled()) return;
        if (!e.isFirst()) return;
        Player p = e.getTrigger().getPlayer();
        if (p != null) {
            p.getInventory().addItem(getItem("fn").toItem());
        }
    }

    public static List<ItemStack> getInitialItems(String clazztype) {
        return Arrays.asList(
                new Item(main, "&9Capacete Inicial", Material.LEATHER_HELMET, new Attributes(0, 0, 0, 5)).setIUID("thel").toItem(),
                new Item(main, "&9Peitoral Inicial", Material.LEATHER_CHESTPLATE, new Attributes(0, 0, 0, 10)).setIUID("tche").toItem(),
                new Item(main, "&9Calças Inicial", Material.LEATHER_LEGGINGS, new Attributes(0, 0, 0, 5)).setIUID("tleg").toItem(),
                new Item(main, "&9Botas Inicial", Material.LEATHER_BOOTS, new Attributes(0, 0, 10, 5)).setIUID("tboo").toItem(),
                new Item(main, "&9Espada Inicial", Material.WOODEN_SWORD, new Attributes(10, 0, 5, 0)).setIUID("tswo").toItem(),
                new Item(main, "&9Escudo Inicial", Material.SHIELD, new Attributes(0, 0, 0, 30)).setIUID("tshi").toItem()
        );
    }

    public static CustomItem getItemFromPath(PryConfig config, String path) {
        return new CustomItem(main, Material.valueOf(config.getString(path)));
    }
}
