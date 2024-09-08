package br.pryzat.rpg.api.items.types;

import br.pryzat.rpg.api.characters.stats.Attributes;
import br.pryzat.rpg.main.RpgMain;
import br.pryzat.rpg.utils.PryColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract class ConsumableItem  extends Item{
    /**
     * Create a new Item
     *
     * @param pl          Instance of plugin to register the namespacedkey
     * @param iuid        Item Unique Identifier
     * @param material    Bukkit Material
     */
    public ConsumableItem(RpgMain pl, String iuid, Material material) {
        super(pl, material, null);
        setItemNamespace("rpg.consumableitem");
        setUniqueId(iuid);
        getItemMeta().setUnbreakable(true);
        setItemFlags(true, ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_UNBREAKABLE, ItemFlag.HIDE_ENCHANTS);
    }

    public abstract void execute(Object obj);

}
