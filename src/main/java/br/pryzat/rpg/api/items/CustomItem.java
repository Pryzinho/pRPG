package br.pryzat.rpg.api.items;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class CustomItem {
	private ItemStack is;
	private ItemMeta im;
	private PersistentDataContainer pdc;

	public CustomItem(ItemStack is) {
		this.is = is;
		this.im = is.getItemMeta();
		this.pdc = im.getPersistentDataContainer();
	}

	public CustomItem(Material material) {
		this.is = new ItemStack(material);
		this.im = is.getItemMeta();
		this.pdc = im.getPersistentDataContainer();
	}

	/**
	 * Aceita coloração (&)
	 * 
	 * @param name
	 */
	public CustomItem setName(String name) {
		im.setDisplayName(color(name));

		return this;
	}

	/**
	 * Aceita coloração (&)
	 * 
	 * @param lore
	 */
	public CustomItem setLore(List<String> lore) {
		List<String> newlore = new ArrayList<>();
		lore.forEach(l -> newlore.add(color(l)));
		im.setLore(lore);
return this;
	}

	/**
	 * Esconde os atributos do item. Ex. Damage 7,06...
	 * 
	 * @param bool
	 */
	public CustomItem hideAttributes(boolean bool) {
		if (bool) {
			im.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		} else {
			if (im.hasItemFlag(ItemFlag.HIDE_ATTRIBUTES)) {
				im.removeItemFlags(ItemFlag.HIDE_ATTRIBUTES);
			}
		}
		return this;
	}

	/**
	 * Esconde os encantamentos do item.
	 * 
	 * @param bool
	 */
	public CustomItem hideEnchants(boolean bool) {
		if (bool) {
			im.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		} else {
			if (im.hasItemFlag(ItemFlag.HIDE_ENCHANTS)) {
				im.removeItemFlags(ItemFlag.HIDE_ENCHANTS);
			}
		}
		return this;
	}

	/**
	 * Remove os itemflags do item se seta um novo.
	 * 
	 * @param itemflag
	 */
	public CustomItem setItemFlags(ItemFlag... itemflag) {
		im.removeItemFlags(ItemFlag.values());
		im.addItemFlags(itemflag);
		return this;
	}

	/**
	 * Adiciona um novo dado dentro do PersistentDataContainer do item
	 * 
	 * @param key                Nome do local que vai está armazenado. Ex:
	 *                           item_customizados; meu.plugin.dados.items.vip;
	 * @param persistentdatatype Tipo de objeto que está sendo armazenado (Enum)
	 * @param value              Objeto a ser armazenado
	 */
	public CustomItem addData(String key, PersistentDataType persistentdatatype, Object value) {
		pdc.set(NamespacedKey.fromString(key), persistentdatatype, value);
		return this;
	}

	public PersistentDataContainer getDataManager() {
		return pdc;
	}

	/**
	 * 
	 * @param key  Nome do local que está armazenado
	 * @param type Tipo de objeto que está armazenado
	 * @return Verifica se o item tem o local de armazenamento especifico e retorna
	 *         verdadeiro ou falso.
	 */
	public boolean hasKey(String key, PersistentDataType type) {
		return pdc.has(NamespacedKey.fromString(key), type);
	}

	/**
	 * 
	 * @param key   Nome do local que está armazenado
	 * @param type  Tipo de objeto que está armazenado
	 * @param value Objeto a ser verificado
	 * @return Verficia se o item contem o objeto armazenado no mesmo local e
	 *         retorna verdadeiro ou falso
	 */
	public boolean contains(String key, PersistentDataType type, Object value) {
		if (!hasKey(key, type))
			return false;
		if (!pdc.get(NamespacedKey.fromString(key), type).equals(value))
			return false;

		return true;

	}

	/**
	 * Apaga o local de armazenamento juntamente com o objeto armazenado
	 * 
	 * @param key  Nome do local que está armazenado
	 * @param type Tipo de objeto que está armazenado
	 */
	public void removeKey(String key, PersistentDataType type) {
		if (hasKey(key, type))
			pdc.remove(NamespacedKey.fromString(key));
	}

	public ItemStack toItemStack() {
		is.setItemMeta(im);
		return is;
	}

	private String color(String txt) {
		return ChatColor.translateAlternateColorCodes('&', txt);
	}
}
