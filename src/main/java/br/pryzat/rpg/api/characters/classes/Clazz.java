package br.pryzat.rpg.api.characters.classes;

import br.pryzat.rpg.api.characters.skills.Skill;
import br.pryzat.rpg.api.characters.skills.SkillType;
import br.pryzat.rpg.api.characters.skills.Skills;
import br.pryzat.rpg.api.characters.stats.Stats;
import br.pryzat.rpg.main.RpgMain;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class Clazz {
    private RpgMain main;
    private Stats stats;
    private ClazzType classtype;
    private Skills skills;
    private ItemStack[] initialitems;
	
    public Clazz(RpgMain main, ClazzType classtype, Stats stats){
        this.classtype = classtype;
        this.stats = stats;
        this.main = main;
      skills = new Skills(main);
    }

    public Stats getStats(){
        return stats;
    }
    public Skills getSkills() {
        return skills;
    }
    public ClazzType getClassType(){
        return classtype;
    }

}
