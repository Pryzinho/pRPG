package br.pryzat.rpg.api.characters.stats;

import br.pryzat.rpg.api.events.bukkit.character.CharacterLevelChangeEvent;
import br.pryzat.rpg.main.RpgMain;
import br.pryzat.rpg.utils.PryColor;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

public class Level {
    private RpgMain plugin;
    private UUID owner;
    private int level;
    private long experience;
    private long reqExp;
    private int acontinuos = 0;
    private BukkitRunnable arun;
    private int rcontinuos = 0;
    private BukkitRunnable rrun;

    // Definido de acordo com o plano do usuario (mortal, espirito e etc...)
    // restrictions[0] = maxLevel
    // restrictions[1] = Ao primeiro termo da PA que aumenta o exp necessario pra upar
    // restrictions[2] = Razão da pa de exp pra upar
    private int[] restrictions;

    /**
     * @param plugin
     * @param uuid
     * @param restrictions Primeiro argumento equivale a maxLevel e o segundo ao primeiro termo da pa de exp
     */
    public Level(RpgMain plugin, UUID uuid, int[] restrictions) {
        this.plugin = plugin;
        owner = uuid;
        this.arun = new BukkitRunnable() {
            Player p = Bukkit.getPlayer(owner);

            @Override
            public void run() {
                p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 30f, 30f);
                p.sendMessage(PryColor.color("&aVocê subiu " + acontinuos + " nivel(is)&f."));
                acontinuos = 0;
            }
        };
        rrun = new BukkitRunnable() {
            Player p = Bukkit.getPlayer(owner);

            @Override
            public void run() {
                p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 30f, 30f);
                p.sendMessage(PryColor.color("&cVocê perdeu " + rcontinuos + " nivel(is)&f."));
                rcontinuos = 0;
            }
        };
        level = 1;
        experience = 0;
        this.restrictions = restrictions;
        reqExp = restrictions[1];
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
        reqExp = getRequiredExp();
        checkLevelUp();
    }

    public void syncSet(int level) {
        this.level = level;
        reqExp = getRequiredExp();
    }

    public void add(int level) {
        CharacterLevelChangeEvent clce = new CharacterLevelChangeEvent(owner, CharacterLevelChangeEvent.Cause.ADD, get() + level);
        Bukkit.getPluginManager().callEvent(clce);
        if (clce.isCancelled()) {
            return;
        }
        this.level += level;
        if (this.level > restrictions[0]) {
            this.level = restrictions[0];
        }
        reqExp = getRequiredExp();
    }

    public void rem(int level) {
        CharacterLevelChangeEvent clce = new CharacterLevelChangeEvent(owner, CharacterLevelChangeEvent.Cause.REMOVE, get() - 1);
        Bukkit.getPluginManager().callEvent(clce);
        if (clce.isCancelled()) {
            return;
        }
        this.level -= level;
        if (this.level <= 0) {
            this.level = 1;
        }
        reqExp = getRequiredExp();
    }

    public long getExp() {
        checkLevelUp();
        return experience;
    }

    public void setExp(long exp) {
        if (exp < 0) {
            return;
        }
        experience = exp;
        checkLevelUp();
    }

    public void addExp(long exp) {
        if (exp <= 0) {
            return;
        }
        experience += exp;
        checkLevelUp();
    }

    public void remExp(long exp) {
        if (exp <= 0) {
            return;
        }
        experience -= exp;
    }

    public long getRequiredExp() {
        return restrictions[1] + (long) (level - 1) * restrictions[2];
    }

    public void checkLevelUp() {
        if (level >= restrictions[0]) {
            this.experience = Math.min(this.experience, this.reqExp);
            return;
        }
        while (experience >= reqExp) {
            experience -= reqExp;
            add(1);
        }
    }

}
