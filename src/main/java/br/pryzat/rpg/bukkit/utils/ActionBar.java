package br.pryzat.rpg.bukkit.utils;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.network.chat.ChatMessageType;
import net.minecraft.network.chat.IChatBaseComponent;
import net.minecraft.network.chat.IChatMutableComponent;
import net.minecraft.network.protocol.game.PacketPlayOutChat;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class ActionBar {
  private static Map<Player, BukkitTask> PENDING_MESSAGES = new HashMap<>();
  
  public static void sendMessage(Player bukkitPlayer, String message) {
    sendRawMessage(bukkitPlayer, "{\"text\": \"" + PryColor.color(message) + "\"}");
  }
  
  private static void sendRawMessage(Player bukkitPlayer, String rawMessage) {
    CraftPlayer player = (CraftPlayer)bukkitPlayer;
    IChatMutableComponent iChatMutableComponent = IChatBaseComponent.ChatSerializer.a(rawMessage);
    PacketPlayOutChat packetPlayOutChat = new PacketPlayOutChat(iChatMutableComponent, ChatMessageType.c, player.getUniqueId());
    (player.getHandle()).b.sendPacket(packetPlayOutChat);
  }
  

  public static void broadcastMessage(String message) {
    Bukkit.getOnlinePlayers().forEach(p -> sendMessage(p, message));
  }
  
  public static void broadcastMessage(String message, int duration, Plugin plugin) {
    Bukkit.getOnlinePlayers().forEach(p -> sendMessage(p, message, duration, plugin));
  }
  
  public static void sendMessage(String message, String permission) {
    Bukkit.getOnlinePlayers().forEach(p -> {
          if (p.hasPermission(permission))
            sendMessage(p, message);
        });
  }
  
  public static void sendMessage(String message, String permission, int duration, Plugin plugin) {
    Bukkit.getOnlinePlayers().forEach(p -> {
          if (p.hasPermission(permission))
            sendMessage(p, message, duration, plugin);
        });
  }
  
  public static void sendMessage(String message, Permission permission) {
    Bukkit.getOnlinePlayers().forEach(p -> {
          if (p.hasPermission(permission))
            sendMessage(p, message);
        });
  }
  
  public static void sendMessage(String message, Permission permission, int duration, Plugin plugin) {
    Bukkit.getOnlinePlayers().forEach(p -> {
          if (p.hasPermission(permission))
            sendMessage(p, message, duration, plugin);
        });
  }
  
  public static void sendMessage(final Player bukkitPlayer, final String message, final int duration, Plugin plugin) {
    cancelPendingMessages(bukkitPlayer);
    BukkitTask messageTask = (new BukkitRunnable() {
        private int count = 0;
        
        public void run() {
          if (this.count >= duration - 3)
            cancel(); 
          ActionBar.sendMessage(bukkitPlayer, message);
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
