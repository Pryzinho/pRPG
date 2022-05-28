package br.pryzat.rpg.utils;

import java.util.HashMap;
import java.util.Map;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class ActionBar {
  private static Map<Player, BukkitTask> PENDING_MESSAGES = new HashMap<>();

  public static void broadcastMessage(String message, int duration, Plugin plugin) {
    Bukkit.getOnlinePlayers().forEach(p -> sendMessage(p, message, duration, plugin));
  }

  public static void sendMessage(String message, String permission, int duration, Plugin plugin) {
    Bukkit.getOnlinePlayers().forEach(p -> {
          if (p.hasPermission(permission))
            sendMessage(p, message, duration, plugin);
        });
  }
  public static void sendMessage(String message, Permission permission, int duration, Plugin plugin) {
    Bukkit.getOnlinePlayers().forEach(p -> {
          if (p.hasPermission(permission))
            sendMessage(p, message, duration, plugin);
        });
  }

    /**
     *
     * @param p Player
     * @param message Message
     * @param infinity true if never ends
     * @param pl JavaPlugin
     *      */
    public static void send(Player p, String message, boolean infinity, Plugin pl) {
        if (infinity) {
            sendMessage(p, message, Integer.MAX_VALUE, pl);
        } else {
            sendMessage(p, message, 0, pl);
        }
    }

    public static void sendMessage(final Player bukkitPlayer, final String message, int duration, Plugin plugin) {
        cancelPendingMessages(bukkitPlayer);
        BukkitTask messageTask = (new BukkitRunnable() {
            private int count = 0;
            public void run() {
                if (this.count >= duration - 3)
                    cancel();
                bukkitPlayer.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(PryColor.color(message)));
                this.count++;
            }
        }).runTaskTimer(plugin, 0L, 20L);
        PENDING_MESSAGES.put(bukkitPlayer, messageTask);
    }
    private static void cancelPendingMessages(Player bukkitPlayer) {
        if (PENDING_MESSAGES.containsKey(bukkitPlayer))
            (PENDING_MESSAGES.get(bukkitPlayer)).cancel();
    }
}
