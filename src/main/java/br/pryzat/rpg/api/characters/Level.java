package br.pryzat.rpg.api.characters;

import br.pryzat.rpg.api.events.bukkit.CharacterLevelChangeEvent;
import br.pryzat.rpg.main.RpgMain;
import br.pryzat.rpg.utils.PryColor;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.types.InheritanceNode;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class Level {
    private RpgMain plugin;
    private UUID owner;
    private int level;
    private long experience;
    private long reqExp;
    private int acontinuos = 0;
    private BukkitRunnable arun = new BukkitRunnable() {
        Player p = Bukkit.getPlayer(owner);

        @Override
        public void run() {
            p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 30f, 30f);
            p.sendMessage(PryColor.color("&aVocê subiu " + acontinuos + " nivel(is)&f."));
            acontinuos = 0;
        }
    };
    private int rcontinuos = 0;
    private BukkitRunnable rrun = new BukkitRunnable() {
        Player p = Bukkit.getPlayer(owner);

        @Override
        public void run() {
            p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 30f, 30f);
            p.sendMessage(PryColor.color("&cVocê perdeu " + rcontinuos + " nivel(is)&f."));
            rcontinuos = 0;
        }
    };

    public Level(RpgMain plugin, UUID uuid) {
        this.plugin = plugin;
        owner = uuid;
        level = 1;
        experience = 0;
        checkLevelUp();
    }

    public int get() {
        checkLevelUp();
        return level;
    }

    public void set(int level) {
        CharacterLevelChangeEvent clce = new CharacterLevelChangeEvent(owner, CharacterLevelChangeEvent.Cause.SET, level);
        Bukkit.getPluginManager().callEvent(clce);
        if (clce.isCancelled()) {
            return;
        }
        this.level = level;
        Player p = Bukkit.getPlayer(owner);
        if (p != null && p.isOnline()) {
            p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 30f, 30f);
            p.sendMessage(PryColor.color("&aSeu nivel foi definido para " + this.level));
        }
        checkLevelUp();
    }

    public void add(int level) {
        CharacterLevelChangeEvent clce = new CharacterLevelChangeEvent(owner, CharacterLevelChangeEvent.Cause.ADD, get() + 1);
        Bukkit.getPluginManager().callEvent(clce);
        if (clce.isCancelled()) {
            return;
        }
        this.level += level;
        /*
        acontinuos++;
        Player p = Bukkit.getPlayer(owner);
        if (p != null && p.isOnline()) {
            if (!arun.isCancelled()) {
                arun.cancel();
            }
            arun.runTaskLater(plugin, 3 * 20);
        }
         */
    }

    public void rem(int level) {
        CharacterLevelChangeEvent clce = new CharacterLevelChangeEvent(owner, CharacterLevelChangeEvent.Cause.REMOVE, get() - 1);
        Bukkit.getPluginManager().callEvent(clce);
        if (clce.isCancelled()) {
            return;
        }
        this.level -= level;

        /*
        rcontinuos++;
        Player p = Bukkit.getPlayer(owner);
        if (p != null && p.isOnline()) {
            if (!rrun.isCancelled()) {
                rrun.cancel();
            }
            rrun.runTaskLater(plugin, 3 * 20);
        }
        */
    }

    public long getExp() {
        checkLevelUp();
        return experience;
    }

    public void setExp(long exp) {
        experience = exp;
        if (experience < 0) experience = 0;
        checkLevelUp();
    }

    public void addExp(long exp) {
        experience += exp;
        checkLevelUp();
    }

    public void remExp(long exp) {
        experience -= exp;
        if (experience < 0) experience = 0;
    }

    public long getRequiredExp() {
        checkLevelUp();
        return reqExp;
    }

    public void checkLevelUp() {
      /*  if (level >= 20) {
            Player player = Bukkit.getPlayer(owner);
            LuckPerms lp = LuckPermsProvider.get();
            User user;
            if (player == null) {
                CompletableFuture<User> userFuture = lp.getUserManager().loadUser(owner);
                try {
                    user = userFuture.get();
                } catch (InterruptedException | ExecutionException e) {
                    user = null;
                }
                if (user != null) {
                    InheritanceNode node = InheritanceNode.builder("iniciante").value(true).build();
                    user.data().remove(node);
                    lp.getUserManager().saveUser(user);
                }
            } else {
                if (player.isOnline()) {
                    user = lp.getUserManager().getUser(owner);
                } else {
                    CompletableFuture<User> userFuture = lp.getUserManager().loadUser(owner);
                    try {
                        user = userFuture.get();
                    } catch (InterruptedException | ExecutionException e) {
                        user = null;
                    }
                }
                if (player.hasPermission("group.iniciante") && user != null) {
                    assert user != null;
                    InheritanceNode node = InheritanceNode.builder("iniciante").value(true).build();
                    user.data().remove(node);
                    lp.getUserManager().saveUser(user);
                }
            }
        }*/
        if (level < 20) {
            reqExp = (level + 1) * 1000;
            if (experience >= reqExp) {
                add(1);
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        setExp(experience - reqExp);
                    }
                }.runTaskLaterAsynchronously(plugin, 20);
            }
        } else if (level >= 20 && level < 30) {
            reqExp = (level + 1) * 2000;
            if (experience >= reqExp) {
                add(1);
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        setExp(experience - reqExp);
                    }
                }.runTaskLaterAsynchronously(plugin, 20);
            }
        } else if (level >= 30) {
            reqExp = (level + 1) * 3000;
            if (experience >= reqExp) {
                add(1);
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        setExp(experience - reqExp);
                    }
                }.runTaskLaterAsynchronously(plugin, 20);
            }
        }

    }
}
