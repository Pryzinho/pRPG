package br.pryzat.rpg.builds.skills.swordsman;

import br.pryzat.rpg.api.characters.skills.Skill;
import br.pryzat.rpg.main.RpgMain;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.UUID;

public class Stomper extends Skill {
    public Stomper(RpgMain main, UUID uuid, int level) {
        super(main, uuid, level);
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
        Player p = Bukkit.getPlayer(getOwner());
        Vector v = new Vector();
        v.setX(p.getLocation().getX());
        v.setY(p.getLocation().getY() + 15 + getLevel());
        v.setZ(p.getLocation().getZ());
        p.setVelocity(v);
        super.execute();
    }
}
