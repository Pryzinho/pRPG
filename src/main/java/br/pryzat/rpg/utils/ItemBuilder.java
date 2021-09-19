package br.pryzat.rpg.utils;


import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemBuilder {
    public static ItemStack create(String name, Material material) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(color(name));
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack create(String name, Material material, int quantia) {
        ItemStack item = new ItemStack(material, quantia);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(color(name));
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack create(String name, Material material, List<String> lore) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(color(name));
        meta.setLore(colorL(lore));
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        item.setItemMeta(meta);
        return item;
    }
    private static String color(String txt){
        return ChatColor.translateAlternateColorCodes('&', txt);
    }
    private static List<String> colorL (List<String> l){
        List<String> nl = new ArrayList<>();
        for (int i = 0; i < l.size(); i++){
            nl.add(color(l.get(i)));
        }
        return nl;
    }
}

