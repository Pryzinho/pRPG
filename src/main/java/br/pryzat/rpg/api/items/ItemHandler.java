package br.pryzat.rpg.api.items;

import br.pryzat.rpg.api.characters.stats.AttributeHandler;
import br.pryzat.rpg.api.characters.stats.Attributes;
import br.pryzat.rpg.api.events.bukkit.character.CharacterChooseClassEvent;
import br.pryzat.rpg.api.items.types.*;
import br.pryzat.rpg.builds.items.Grisaia;
import br.pryzat.rpg.main.RpgMain;
import br.pryzat.rpg.utils.PryConfig;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * TO-DO
 * criar uma clase CustomPersistentData que vai ter metodos como addString
 * addKey, hasKey, removeKey, remString, setString, getBoolean
 */
public class ItemHandler implements Listener {
    private static RpgMain main;
    private HashMap<String, Item> items; // ItemUniqueId, ConsumableItem ? Troca isso pra items

    private NamespacedKey REPRESENTATIVE_ITEM_NAMESPACE;
    private NamespacedKey ITEM_NAMESPACE;
    private NamespacedKey CONSUMABLE_ITEM_NAMESPACE;


    public ItemHandler(RpgMain pl) {
        main = pl;
        this.items = new HashMap<>();
        this.registerAllNamespacedKeys(pl);
        Bukkit.getPluginManager().registerEvents(this, main);
    }


    public void loadAllItems() {
        items.put("grisaia", new Grisaia(main, 1));

        loadAllPackages();
    }


    private void loadAllPackages() {
        // Package: Força aos Novatos (ItemUniqueId: 'fn', PDC: rpg.packageitem.fn).
        PackageItem fn = new PackageItem(main, "fn", Material.ENDER_CHEST);
        fn.displayName("<gold>Força aos Novatos");
        fn.lore(Arrays.asList(MiniMessage.miniMessage().deserialize("<red>Pacote especial para novos jogadores<white>.")));
        List<ItemStack> fni = new ArrayList<>(); //Pakcage Items
        fni.add(
                new Item(main, , Material.NETHERITE_HOE, null)
                        .displayName("<purple>Foice do Aprendiz")
                        .stringLore(List.of("<aqua>Coleta o dobro de XP em monstros abaixo do nivel 20<white>."))
                        .hideEnchants(true)
                        .hideAttributes(true)
                        .setUniqueId("fa1")
        );
        fni.add(new ItemStack(Material.DIAMOND, 10));
        fn.setItems(fni);
        put(fn);

    }

    public Item getItem(String iuid) {
        return items.get(iuid);
    }

    public HashMap<String, Item> getItems() {
        return items;
    }

    /**
     * Coloca ou substitui um item na lista de itens registrados.
     *
     * @param i Item
     */
    public void put(Item i) {
        items.put(i.getUniqueId(), i);
    }

    /*
     *
     * ÁREA RESERVADA PARA LISTENERS
     *
     */

    @EventHandler
    public void onPackageOpen(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        if (!e.getAction().isRightClick()) return;
        ItemStack is = p.getInventory().getItemInMainHand();
        if (!is.getItemMeta().getPersistentDataContainer().has(CONSUMABLE_ITEM_NAMESPACE))
            return;
        items.keySet().forEach(key -> {
            if (key == null) return;
            if (key.equals(is.getItemMeta().getPersistentDataContainer().get(CONSUMABLE_ITEM_NAMESPACE, PersistentDataType.STRING))) {
                is.subtract();
                ConsumableItem ci = (ConsumableItem) items.get(key);
                ci.execute(main.getCharacterManager().getCharacter(p.getUniqueId()));
            }
        });
        e.setCancelled(true);
    }

    //  TEMPORARIO, DEPOIS UM NPC DA O PACOTE OU COISA DO TIPO - 08/09/24
    @EventHandler
    public void onClazzSelect(CharacterChooseClassEvent e) {
        if (e.isCancelled()) return;
        if (!e.isFirst()) return; //NÃO ACREDITO QUE CRIEI ESSE METODO SO PRA SETAR ESSE PACOTE CARA.
        Player p = e.getTrigger().getPlayer();
        if (p != null && !e.isCancelled()) {
            p.getInventory().addItem(getItem("fn"));
        }
    }

    /*
     *
     * FIM da área reservada para listeners.
     *
     */

    /**
     * @param namespace  Espaço reservado para guardar informações sobre o item.
     * @param bukkitItem ItemStack que vai ser analisado.
     * @param IUID       Identificador único de um item do RPG.
     * @return Retorna true(verdadeiro) caso o item tenha o IUID especifico ou false(falso) caso o item não tenha o IUID ou não esteja no namespace correto.
     */
    public boolean matchInNamespace(NamespacedKey namespace, ItemStack bukkitItem, String IUID) {
        return bukkitItem.getItemMeta().getPersistentDataContainer().has(namespace, PersistentDataType.STRING) &&
                IUID.equalsIgnoreCase(bukkitItem.getItemMeta().getPersistentDataContainer().get(namespace, PersistentDataType.STRING));
    }


    // muito trabalho a ser feito em serializar um item em yml viu kkkk. 08/09/24
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
                itemStacks.add(new Item(main, material, attributes).displayName(displayName).setUniqueId(iuid));
            }
        });
        return itemStacks;
    }


    public static Item getItemFromPath(PryConfig config, String path) {
        return new Item(main, Material.valueOf(config.getString(path)), null);
    }


    /*
     *
     * ÁREA RESERVADA PARA MÉTODOS DE NAMESPACE
     *
     */
    private void registerAllNamespacedKeys(JavaPlugin instance) {
        this.REPRESENTATIVE_ITEM_NAMESPACE = new NamespacedKey(instance, "rpg.representative.item");
        this.ITEM_NAMESPACE = new NamespacedKey(instance, "rpg.item");
        this.CONSUMABLE_ITEM_NAMESPACE = new NamespacedKey(instance, "rpg.consumableitems");
    }

    public NamespacedKey REPRESENTATIVE_ITEM_NAMESPACE() {
        return this.REPRESENTATIVE_ITEM_NAMESPACE;
    }

    public NamespacedKey CONSUMABLE_ITEM_NAMESPACE() {
        return this.CONSUMABLE_ITEM_NAMESPACE;
    }

    public NamespacedKey ITEM_NAMESPACE() {
        return this.ITEM_NAMESPACE;
    }
    /*
     *
     * FIM da área reservada para métodos namespace.
     *
     */
}
