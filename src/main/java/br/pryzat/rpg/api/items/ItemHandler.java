package br.pryzat.rpg.api.items;

import br.pryzat.rpg.api.characters.classes.BaseClass;
import br.pryzat.rpg.api.characters.stats.AttributeHandler;
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
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ItemHandler implements Listener {
    private static RpgMain main;
    private HashMap<String, ConsumableItem> items; // ItemUniqueId, ConsumableItem ? Troca isso pra items

    public ItemHandler(RpgMain pl) {
        main = pl;
        this.items = new HashMap<>();
        Bukkit.getPluginManager().registerEvents(this, main);
    }


    public void loadAllItems() {
        items.put("grisaia", new Grisaia(main, 1));
        loadAllPackages();
    }


    private void loadAllPackages() {
        // Package: Força aos Novatos (ItemUniqueId: 'fn', PDC: rpg.consumableitems.fn).
        PackageItem fn = new PackageItem(main, "fn", "&cForça aos Novatos", Material.ENDER_CHEST, 1);
        List<String> fnl = new ArrayList<>(); //Item lore
        fnl.add("&aPacote especial para novos jogadores&f.");
        fn.setLore(fnl);
        List<ItemStack> fni = new ArrayList<>(); //Pakcage Items
        fni.add(
                new CustomItem(main, Material.NETHERITE_HOE)
                        .setName("&dFoice do Aprendiz")
                        .setLore(List.of("&bColeta o dobro de XP em monstros abaixo do nivel 20&f."))
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
        if (!is.getItemMeta().getPersistentDataContainer().has(new NamespacedKey(main, "rpg.consumableitems.uid")))
            return;
        items.keySet().forEach(key -> {
            if (key == null) return;
            if (is.getItemMeta().getPersistentDataContainer().get(new NamespacedKey(main, "rpg.consumableitems.uid"), PersistentDataType.STRING).equals(key)) {
                p.getInventory().setItemInMainHand(new ItemStack(Material.AIR));
                items.get(key).execute(main.getCharacterManager().getCharacter(p.getUniqueId()));
            }
        });
        e.setCancelled(true);
    }

    @EventHandler
    public void onClazzSelect(CharacterChooseClassEvent e) {
        if (e.isCancelled()) return;
        if (!e.isFirst()) return;
        Player p = e.getTrigger().getPlayer();
        if (p != null && !e.isCancelled()) {
            p.getInventory().addItem(getItem("fn").toItem());
        }
    }

    /**
     * @param namespace Espaço reservado para guardar informações sobre o item.
     * @param bukkitItem ItemStack que vai ser analisado.
     * @param IUID Identificador único de um item do RPG.
     * @return Retorna true(verdadeiro) caso o item tenha o IUID especifico ou false(falso) caso o item não tenha o IUID ou não esteja no namespace correto.
     */
    public boolean matchInNamespace(NamespacedKey namespace, ItemStack bukkitItem, String IUID) {
        return bukkitItem.getItemMeta().getPersistentDataContainer().has(namespace, PersistentDataType.STRING) &&
                IUID.equalsIgnoreCase(bukkitItem.getItemMeta().getPersistentDataContainer().get(main.REPRESENTATIVE_ITEM_NAMESPACE, PersistentDataType.STRING));
    }


    public static List<ItemStack> getInitialItems(List<String> items) {
        List<ItemStack> itemStacks = new ArrayList<>();
        items.forEach(item -> {
            if (item.startsWith("RPGITEM")) {
                String iuid;
                String displayName;
                Material material;
                Attributes attributes;

                Matcher matcher = Pattern.compile("\\((.*?)\\)").matcher(item);
                // Verifica se o padrão foi encontrado e extrai o conteúdo entre parênteses
                if (matcher.find()) {
                    String[] deserializedItem = matcher.group(1).split(";");
                    if (deserializedItem.length != 4) {
                        Bukkit.getLogger().warning("[pRPG] Um item RPG precisa ter quatro argumentos. Argumentos encontrado: " + matcher.group(1));
                        return;
                    }
                    iuid = deserializedItem[0];
                    displayName = deserializedItem[1];
                    material = Material.matchMaterial(deserializedItem[2]);
                    if (material == null) {
                        Bukkit.getLogger().warning("[pRPG] Material invalido em um item, IUID: " + iuid);
                        return;
                    }
                    attributes = AttributeHandler.byStringConfig(deserializedItem[3]);
                } else {
                    Bukkit.getLogger().warning("[pRPG] Não foi possível encontrar nenhuma especificação entre parenteses de um item.");
                    return;
                }
                itemStacks.add(new Item(main, displayName, material, attributes).setUniqueId(iuid).toItem());
            }
        });
        return itemStacks;
    }


    public static CustomItem getItemFromPath(PryConfig config, String path) {
        return new CustomItem(main, Material.valueOf(config.getString(path)));
    }
}
