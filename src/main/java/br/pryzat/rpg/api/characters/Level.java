package br.pryzat.rpg.api.characters;

import java.util.UUID;

public class Level {
    private UUID owner;
    private int level;
    private long experience;
    private long reqExp;

    public Level(UUID uuid) {
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
        this.level = level;
        checkLevelUp();
    }

    public void add(int level) {
        this.level += level;
        checkLevelUp();
    }

    public void rem(int level) {
        this.level -= level;
        checkLevelUp();
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
        if (level < 20) {
            reqExp = (level + 1) * 1000;
            if (experience >= reqExp) {
                add(1);
                setExp(experience - reqExp);
            }
        } else if (level >= 20 && level < 30) {
            reqExp = (level + 1) * 2000;
            if (experience >= reqExp) {
                add(1);
                setExp(experience - reqExp);
            }
        } else if (level >= 30) {
            reqExp = (level + 1) * 3000;
            if (experience >= reqExp) {
                add(1);
                setExp(experience - reqExp);
            }
        }

    }
}
