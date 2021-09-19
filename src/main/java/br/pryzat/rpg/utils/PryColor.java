package br.pryzat.rpg.utils;

import org.bukkit.ChatColor;

public class PryColor {
    public static String color(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }
    public static String decolor(String message){
        return ChatColor.stripColor(message);
    }
}
