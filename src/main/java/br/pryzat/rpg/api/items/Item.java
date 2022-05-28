package br.pryzat.rpg.api.items;

import br.pryzat.rpg.api.characters.stats.Attributes;
import br.pryzat.rpg.utils.PryColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import javax.annotation.Nullable;
import java.util.List;
import java.util.ArrayList;

public class Item {
    private String iuid;
    private String displayName;
    private Material material;
    private List<String> lore;
    private Attributes attributes;

    public Item(String displayName, Material material, @Nullable Attributes attributes) {
        this.displayName = displayName;
        this.material = material;
        this.attributes = attributes;
		this.lore = new ArrayList<String>();
    }

    public ItemStack toItem() {
        ItemStack bis = new ItemStack(material);
        ItemMeta bim = bis.getItemMeta();
        bim.setDisplayName(PryColor.color(displayName));
        if (attributes != null) {
            if (getStats().getStrength() != 0) {
                lore.add(PryColor.color("&cForça&f: " + getStats().getStrength()));
            }
            if (getStats().getInteligency() != 0) {
                lore.add(PryColor.color("&bInteligência&f: " + getStats().getInteligency()));
            }
            if (getStats().getVelocity() != 0) {
                lore.add(PryColor.color("&8Velocidade&f: " + getStats().getVelocity()));
            }
            if (getStats().getResistance() != 0) {
                lore.add(PryColor.color("&8&lResistência&f: " + getStats().getResistance()));
            }
        }
        bim.setLore(lore);
        bim.setUnbreakable(true);
        bim.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_UNBREAKABLE, ItemFlag.HIDE_ENCHANTS);
        PersistentDataContainer pdc = bim.getPersistentDataContainer();
pdc.set(NamespacedKey.fromString("rpg.item.uid"), PersistentDataType.STRING, getIUID());
        if (getStats() != null) {
            pdc.set(NamespacedKey.fromString("rpg.item.strength"), PersistentDataType.STRING, String.valueOf(getStats().getStrength()));
            pdc.set(NamespacedKey.fromString("rpg.item.inteligency"), PersistentDataType.STRING, String.valueOf(getStats().getInteligency()));
            pdc.set(NamespacedKey.fromString("rpg.item.velocity"), PersistentDataType.STRING, String.valueOf(getStats().getVelocity()));
            pdc.set(NamespacedKey.fromString("rpg.item.resistance"), PersistentDataType.STRING, String.valueOf(getStats().getResistance()));
        }
        bis.setItemMeta(bim);
        return bis;
    }

    public String getIUID() {
        return iuid;
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

    public Attributes getStats() {
        return attributes;
    }

    public void setStats(Attributes attributes) {
        this.attributes = attributes;
    }
}
