package br.pryzat.rpg.builds.skills.swordsman;

import br.pryzat.rpg.api.characters.Character;
import br.pryzat.rpg.api.characters.skills.Skill;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class Stomper extends Skill {
    public Stomper(Character owner, int level) {
        super(owner, level);
        setUniqueId("stomper");
        setDisplayName("&aStomper!");
        setNeedMana(false);
        setNeedLife(true);
        if (level > 10) setLevel(10);
        setLevel(level);
        if (level < 5) {
            setCooldown(10 * 60000);
            setLifeCoust(6);
        } else if (level >= 5 && level < 10) {
            setCooldown(5 * 60000);
            setLifeCoust(4);
        } else if (level == 10) {
            setCooldown(3 * 60000);
            setLifeCoust(2);
        }
    }

    @Override
    public void execute() {
        Player p = Bukkit.getPlayer(getOwner().getUUID());
        Vector v = new Vector();
        v.setY(p.getLocation().getY() + 15 + getLevel());
        p.setVelocity(v);
        super.execute();
    }

}
