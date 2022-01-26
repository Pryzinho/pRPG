package br.pryzat.rpg.bukkit.utils;

import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;

public class PryColor {
    public static String color(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }
    public static List<String> color(List<String> messages){
        List<String> coloredmsgs = new ArrayList<>();
        messages.forEach(msg -> coloredmsgs.add(color(msg)));
    return coloredmsgs;
    }
    public static String decolor(String message){
        return ChatColor.stripColor(message);
    }
}
