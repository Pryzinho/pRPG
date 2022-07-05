package br.pryzat.rpg.api.items;

import br.pryzat.rpg.utils.PryColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public abstract class ConsumableItem {
    private JavaPlugin pl;
    private String iuid;
    private String path = "rpg.consumableitems.";
    private String displayName;
    private Material material;
    private int amount;
    private List<String> lore;

    /**
     * Create a new Item
     *
     * @param pl          Instance of plugin to register the namespacedkey
     * @param iuid        Item Unique Identifier
     * @param displayName Display Name
     * @param material    Bukkit Material
     * @param amount      Amount of the item
     * @param lore        Lore of the item
     */
    public ConsumableItem(JavaPlugin pl, String iuid, String displayName, Material material, int amount, List<String> lore) {
        this.pl = pl;
        this.iuid = iuid;
        this.displayName = displayName;
        this.material = material;
        this.amount = amount;
        this.lore = lore;
    }

    public ItemStack toItem() {
        ItemStack bis = new ItemStack(material, amount);
        ItemMeta bim = bis.getItemMeta();
        bim.setDisplayName(PryColor.color(displayName));
        if (lore != null) {
            this.lore = PryColor.color(lore);
            bim.setLore(lore);
        }
        bim.setUnbreakable(true);
        bim.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_UNBREAKABLE, ItemFlag.HIDE_ENCHANTS);
        PersistentDataContainer pdc = bim.getPersistentDataContainer();
        pdc.set(new NamespacedKey(pl, path + "uid"), PersistentDataType.STRING, getIUID());
        bis.setItemMeta(bim);
        return bis;
    }

    public boolean equals(Item t) {
        if (t == null) return false;
        ItemStack is = t.toItem();
        if (!is.getItemMeta().getPersistentDataContainer().has(new NamespacedKey(pl, "rpg.item.uid"), PersistentDataType.STRING))
            return false;
        if (is.getItemMeta().getPersistentDataContainer().get(new NamespacedKey(pl, "rpg.item.uid"), PersistentDataType.STRING) != getIUID())
            return false;
        return true;
    }

    public boolean equals(ItemStack t) {
        if (t == null) return false;
        if (!t.getItemMeta().getPersistentDataContainer().has(new NamespacedKey(pl, "rpg.item.uid"), PersistentDataType.STRING))
            return false;
        if (t.getItemMeta().getPersistentDataContainer().get(new NamespacedKey(pl, "rpg.item.uid"), PersistentDataType.STRING) != getIUID())
            return false;
        return true;
    }

    public String getIUID() {
        return iuid;
    }

    public JavaPlugin getPlugin() {
        return pl;
    }

    public void setIUID(String iuid) {
        this.iuid = iuid;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public List<String> getLore() {
        return lore;
    }

    public void setLore(List<String> lore) {
        this.lore = lore;
    }

    protected String getUIDPath() {
        return path;
    }

    public abstract void execute(Object obj);

}
