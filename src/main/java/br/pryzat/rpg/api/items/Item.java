package br.pryzat.rpg.api.items;

import br.pryzat.rpg.api.characters.stats.Attributes;
import br.pryzat.rpg.utils.PryColor;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nullable;
import java.util.List;
import java.util.ArrayList;

public class Item {
    private final JavaPlugin pl;
    private String iuid;
    private String displayName;
    private Material material;
    private List<Component> lore;
    private Attributes attributes;

    public Item(JavaPlugin pl, String displayName, Material material, @Nullable Attributes attributes) {
        this.pl = pl;
        this.displayName = displayName;
        this.material = material;
        this.attributes = attributes;
        this.lore = new ArrayList<>();
    }

    public ItemStack toItem() {
        ItemStack bis = new ItemStack(material);
        ItemMeta bim = bis.getItemMeta();
        bim.displayName(Component.text(PryColor.color(displayName)));
        if (attributes != null) {
            if (getStats().getStrength() != 0) {
                lore.add(Component.text(PryColor.color("&cForça&f: " + getStats().getStrength())));
            }
            if (getStats().getInteligency() != 0) {
                lore.add(Component.text(PryColor.color("&bInteligência&f: " + getStats().getInteligency())));
            }
            if (getStats().getVelocity() != 0) {
                lore.add(Component.text(PryColor.color("&8Velocidade&f: " + getStats().getVelocity())));
            }
            if (getStats().getResistance() != 0) {
                lore.add(Component.text(PryColor.color("&8&lResistência&f: " + getStats().getResistance())));
            }
        }
        bim.lore(lore);
        bim.setUnbreakable(true);
        bim.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_UNBREAKABLE, ItemFlag.HIDE_ENCHANTS);
        PersistentDataContainer pdc = bim.getPersistentDataContainer();
        pdc.set(new NamespacedKey(pl, "rpg.item.uid"), PersistentDataType.STRING, getUniqueId());
        if (getStats() != null) {
            pdc.set(new NamespacedKey(pl, "rpg.item.strength"), PersistentDataType.STRING, String.valueOf(getStats().getStrength()));
            pdc.set(new NamespacedKey(pl, "rpg.item.inteligency"), PersistentDataType.STRING, String.valueOf(getStats().getInteligency()));
            pdc.set(new NamespacedKey(pl, "rpg.item.velocity"), PersistentDataType.STRING, String.valueOf(getStats().getVelocity()));
            pdc.set(new NamespacedKey(pl, "rpg.item.resistance"), PersistentDataType.STRING, String.valueOf(getStats().getResistance()));
        }
        bis.setItemMeta(bim);
        return bis;
    }

    public String getUniqueId() {
        return iuid;
    }

    public Item setUniqueId(String iuid) {
        this.iuid = iuid;
        return this;
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

    public List<Component> getLore() {
        return lore;
    }

    public void setLore(List<Component> lore) {
        this.lore = lore;
    }

    public Attributes getStats() {
        return attributes;
    }

    public void setStats(Attributes attributes) {
        this.attributes = attributes;
    }
}
