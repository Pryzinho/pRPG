package br.pryzat.rpg.bukkit.utils;

import org.bukkit.command.ConsoleCommandSender;

public class Logger {

    public static void log(ConsoleCommandSender sender, String text){
        sender.sendMessage(PryColor.color(text));
    }
    public static void logInfo(ConsoleCommandSender sender, String text){
        sender.sendMessage(PryColor.color("&f[&b&lINFO&f] &b" + text));
    }
    public static void logWarn(ConsoleCommandSender sender, String text){
        sender.sendMessage(PryColor.color("&f[&e&lWARNING&f] &e" + text));
    }
    public static void logError(ConsoleCommandSender sender, String text){
        sender.sendMessage(PryColor.color("&c[&c&lERROR&f] &c" + text));
    }
}
