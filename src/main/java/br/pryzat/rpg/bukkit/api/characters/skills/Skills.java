package br.pryzat.rpg.bukkit.api.characters.skills;

import br.pryzat.rpg.bukkit.builds.skills.swordsman.Perseguir;
import br.pryzat.rpg.bukkit.builds.skills.swordsman.Stomper;
import br.pryzat.rpg.bukkit.main.RpgMain;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Skills {
    private RpgMain main;
    private List<Skill> skills;
    // Variables to frontend

    public Skills(RpgMain main) {
        this.main = main;
        skills = new ArrayList<>();
    }

    public void add(String skilluid, UUID uuid, int level) {
        Skill skill;
        switch (skilluid.toLowerCase()) {
            case "stomper":
                skill = new Stomper(main, uuid, level);
                skills.add(skill);
                break;
            case "perseguir":
                skill = new Perseguir(main, uuid, level);
                skills.add(skill);
                break;
        }

    }


    public Skill get(String skilluid) {
        for (Skill skill : skills) {
            if (skill.getUniqueId().equals(skilluid.toLowerCase())) {
                return skill;
            }
        }
        return null;
    }

    public boolean has(String skilluid) {
        if (skills.size() != 0) {
            for (Skill skill : skills) {
                if (skill.getUniqueId().equals(skilluid)) {
                    return true;
                }
            }
            return false;
        } else {
            return false;
        }
    }


    public List<Skill> toList() {
        return skills;
    }

}
