package br.pryzat.rpg.api.characters.skills;

import br.pryzat.rpg.api.characters.Character;
import br.pryzat.rpg.utils.PryColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class Skill {

    private String displayName;
    private String description;

    private String suid;
    private Material representative_item;

    private int level;
    private int maxLevel;
    private Character owner;

    //private RpgMain main;
    private long cooldown; // tempo em ms que devem ser esperados para executar a skill novamente
    private HashMap<UUID, Long> incooldowns = new HashMap<UUID, Long>();
    private boolean needMana;
    private boolean needLife;
    private int manaCoust;
    private int lifeCoust;
    private boolean inuse;

    public Skill(Character owner, int level) {
       // this.main = main;
        this.owner = owner;

    }
    public Skill(UUID uuid){

    }

    public void execute() {

    }

    public void preExecute() {
        Player p = Bukkit.getPlayer(owner.getUUID());
        if (incooldowns.containsKey(owner.getUUID())) {
            if (incooldowns.get(owner.getUUID()) <= System.currentTimeMillis()) {
                incooldowns.remove(p.getUniqueId());
                execute();
                incooldowns.put(owner.getUUID(), getCooldown() + System.currentTimeMillis());
            } else {
                p.sendMessage(PryColor.color("&cEssa habilidade está recarregando!"));
                p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 50f, 50f);
                return;
            }
        } else {
            execute();
            incooldowns.put(owner.getUUID(), getCooldown() + System.currentTimeMillis());
        }


    }

    public boolean isInUse() {
        return inuse;
    }

    public void setInUse(boolean inuse) {
        this.inuse = inuse;
    }

    public void setUniqueId(String suid) {
        this.suid = suid;
    }

    public String getUniqueId() {
        return suid;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = PryColor.color(displayName);
    }

    public void addLevel(int level) {
        this.level += level;
        if (this.level > this.maxLevel) {
            this.level = this.maxLevel;
        }
    }

    public void remLevel(int level) {
        this.level -= level;
        if (this.level <= 0) {
            this.level = 1;
            owner.removeSkill(null, getUniqueId(), false);
        }
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
        if (this.level <= 0) {
            this.level = 1;
            owner.removeSkill(null, getUniqueId(), false);
        } else if (this.level > this.maxLevel) {
            this.level = this.maxLevel;
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

    public Character getOwner() {
        return owner;
    }
    public void setOwner(Character ch){
        this.owner = ch;
    }

    /*
    public RpgMain getPlugin() {
        return main;
    }
    */

}
