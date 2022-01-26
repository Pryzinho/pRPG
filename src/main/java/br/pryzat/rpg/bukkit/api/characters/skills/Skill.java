package br.pryzat.rpg.bukkit.api.characters.skills;

import br.pryzat.rpg.bukkit.utils.PryColor;
import br.pryzat.rpg.bukkit.main.RpgMain;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class Skill {
    private RpgMain main;
    private UUID owner;
    private String suid;
    private String displayName;
    private int level;
    private long cooldown;
    private HashMap<UUID, Long> incooldowns = new HashMap<UUID, Long>();
    private boolean needMana;
    private boolean needLife;
    private int manaCoust;
    private int lifeCoust;
    private boolean inuse;

    public Skill(RpgMain main, UUID uuid, int level) {
        this.main = main;
        this.owner = uuid;

    }

    public void execute() {

    }

    public void preExecute() {
        Player p = Bukkit.getPlayer(owner);
        if (incooldowns.containsKey(owner)) {
            if (incooldowns.get(owner) <= System.currentTimeMillis()) {
                incooldowns.remove(p.getUniqueId());
                execute();
                incooldowns.put(owner, getCooldown() + System.currentTimeMillis());
            } else {
                p.sendMessage(PryColor.color("&cEssa habilidade está recarregando!"));
                p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 50f, 50f);
                return;
            }
        } else {
            execute();
            incooldowns.put(owner, getCooldown() + System.currentTimeMillis());
        }


    }

    public boolean isInUse() {
        return inuse;
    }

    public void setInUse(boolean inuse) {
        this.inuse = inuse;
    }

    public void setUniqueId(String suid){
        this.suid = suid;
    }
    public String getUniqueId(){ return suid;}

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = PryColor.color(displayName);
    }

    public void addLevel(int level) {
        this.level += level;
        if (this.level > 10) {
            this.level = 10;
        }
    }

    public void decLevel(int level) {
        this.level -= level;
        if (this.level <= 0) {
            this.level = 1;
        }
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
        if (this.level <= 0) {
            this.level = 1;
        } else if (this.level > 10) {
            this.level = 10;
        }
    }

    public long getCooldown() {
        return cooldown;
    }

    public void setCooldown(long cooldown) {
        this.cooldown = cooldown;
    }

    public boolean isManaNeeded() {
        return needMana;
    }

    public void setNeedMana(boolean needMana) {
        this.needMana = needMana;
    }

    public boolean isLifeNeeded() {
        return needLife;
    }

    public void setNeedLife(boolean needLife) {
        this.needLife = needLife;
    }

    public int getManaCoust() {
        return manaCoust;
    }

    public void setManaCoust(int manaCoust) {
        this.manaCoust = manaCoust;
    }

    public int getLifeCoust() {
        return lifeCoust;
    }

    public void setLifeCoust(int lifeCoust) {
        this.lifeCoust = lifeCoust;
    }

    public UUID getOwner(){
        return owner;
    }

    public RpgMain getPlugin(){
        return main;
    }
}
