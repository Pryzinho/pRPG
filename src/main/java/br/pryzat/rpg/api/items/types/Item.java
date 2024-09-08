package br.pryzat.rpg.api.items.types;

import br.pryzat.rpg.api.characters.stats.Attributes;
import br.pryzat.rpg.api.storage.CustomPersistentData;
import br.pryzat.rpg.main.RpgMain;
import br.pryzat.rpg.utils.PryColor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class Item extends ItemStack {
    private final RpgMain instance;
    private String iuid;
    private String ITEM_NAMESPACE;
    private final CustomPersistentData cpd;
    @Nullable
    private Attributes attributes;

    public Item(RpgMain instance, Material material, @Nullable Attributes attributes) {
        this.instance = instance;
        setType(material);
        this.attributes = attributes;
        this.cpd = new CustomPersistentData(this);
        setItemNamespace(instance.getItemHandler().CONSUMABLE_ITEM_NAMESPACE().getKey());

        if (attributes != null) {
            // aprimorar o sistema de lore de atributo depois né pq ta ruim
            if (attributes.getStrength() != 0) {
                getItemMeta().lore().add(Component.text(PryColor.color("&cForça&f: " + attributes.getStrength())));
            }
            if (attributes.getInteligency() != 0) {
                getItemMeta().lore().add(Component.text(PryColor.color("&bInteligência&f: " + attributes.getInteligency())));
            }
            if (attributes.getVelocity() != 0) {
                getItemMeta().lore().add(Component.text(PryColor.color("&8Velocidade&f: " + attributes.getVelocity())));
            }
            if (attributes.getResistance() != 0) {
                getItemMeta().lore().add(Component.text(PryColor.color("&8&lResistência&f: " + attributes.getResistance())));
                getItemMeta().lore().add(Component.text(PryColor.color("&8&lResistência&f: " + attributes.getResistance())));
            }
            this.cpd.setData("rpg.item.strength", PersistentDataType.STRING, String.valueOf(attributes.getStrength()));
            this.cpd.setData("rpg.item.inteligency", PersistentDataType.STRING, String.valueOf(attributes.getInteligency()));
            this.cpd.setData("rpg.item.velocity", PersistentDataType.STRING, String.valueOf(attributes.getVelocity()));
            this.cpd.setData("rpg.item.resistance", PersistentDataType.STRING, String.valueOf(attributes.getResistance()));
        }

    }

    public String getItemNamespace() {
        return ITEM_NAMESPACE;
    }

    public void setItemNamespace(String ITEM_NAMESPACE) {
        this.ITEM_NAMESPACE = ITEM_NAMESPACE;
        this.cpd.setData(ITEM_NAMESPACE, PersistentDataType.BOOLEAN, true);
    }

    public String getUniqueId() {
        return iuid;
    }

    public Item setUniqueId(String iuid) {
        this.iuid = iuid;
        this.cpd.setData(ITEM_NAMESPACE + ".uid", PersistentDataType.STRING, getUniqueId());
        return this;
    }

    public Attributes getStats() {
        return attributes;
    }

    public void setStats(Attributes attributes) {
        this.attributes = attributes;
    }

    /**
     * Define a descrição do item sem precisar usar Component.
     * Aceita formatos do MiniMessage, exemplo: <???>Descrição na cor ???
     *
     * @param lore Descrição do item em uma Lista de Strings.
     */
    public Item stringLore(List<String> lore) {
        List<Component> newlore = new ArrayList<>();
        lore.forEach(l -> newlore.add(MiniMessage.miniMessage().deserialize(l)));
        getItemMeta().lore(newlore);
        return this;
    }

    public CustomPersistentData getCustomPersistentData() {
        return this.cpd;
    }

    public JavaPlugin getPluginInstance() {
        return instance;
    }

    public boolean equals(Item t) {
        if (t == null) return false;
        return getUniqueId().equals(t.getUniqueId());
    }

    public boolean equals(ItemStack t) {
        if (t == null) return false;
        PersistentDataContainer pdc = t.getItemMeta().getPersistentDataContainer();
        NamespacedKey uidkey = new NamespacedKey(instance, ITEM_NAMESPACE + ".uid");
        if (!pdc.has(uidkey, PersistentDataType.STRING))
            return false;
        return getUniqueId().equals(pdc.get(uidkey, PersistentDataType.STRING));
    }


    /*
     *
     * ESPAÇO PARA MÉTODOS HERDADOS DO ItemMeta
     *
     */

    /**
     * Esconde os atributos do item. Ex. Damage 7,06...
     *
     * @param bool
     */
    public Item hideAttributes(boolean bool) {
        if (bool) {
            getItemMeta().addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        } else {
            if (getItemMeta().hasItemFlag(ItemFlag.HIDE_ATTRIBUTES)) {
                getItemMeta().removeItemFlags(ItemFlag.HIDE_ATTRIBUTES);
            }
        }
        return this;
    }

    /**
     * Esconde os encantamentos do item.
     *
     * @param bool
     */
    public Item hideEnchants(boolean bool) {
        if (bool) {
            getItemMeta().addItemFlags(ItemFlag.HIDE_ENCHANTS);
        } else {
            if (getItemMeta().hasItemFlag(ItemFlag.HIDE_ENCHANTS)) {
                getItemMeta().removeItemFlags(ItemFlag.HIDE_ENCHANTS);
            }
        }
        return this;
    }

    /**
     * Define as ItemFlag do item sem apagar as já existentes ou sobrescreve por completo com as novas.
     *
     * @param itemflag Flags a serem setadas
     * @param clear    Se true limpa todas as flags antes de adicionar.
     */
    public Item setItemFlags(boolean clear, ItemFlag... itemflag) {
        if (clear) {
            getItemMeta().removeItemFlags(ItemFlag.values());
        }
        getItemMeta().addItemFlags(itemflag);
        return this;
    }

    public Item setCustomModelData(Integer i) {
        getItemMeta().setCustomModelData(i);
        return this;
    }

    public int getCustomModelData() {
        return getItemMeta().getCustomModelData();
    }

    public Item displayName(String text) {
        getItemMeta().displayName(MiniMessage.miniMessage().deserialize(text));
        return this;
    }
    /*
     *
     * FIM do espaço para métodos herdados do ItemMeta
     *
     */
}
