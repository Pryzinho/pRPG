package br.pryzat.rpg.builds.skills.mage;

import br.pryzat.rpg.api.characters.skills.Skill;
import br.pryzat.rpg.main.RpgMain;
import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.UUID;

public class Fireball extends Skill {

    public Fireball(RpgMain main, UUID uuid, int level) {
        super(main, uuid, level);
        setUniqueId("fireball");
        setDisplayName("&cSou eu bola de fogo");
        setNeedMana(true);
        setNeedLife(false);
        if (level > 10) setLevel(10);
        setLevel(level);
        if (level < 5) {
            setCooldown(10 * 6000);
            setManaCoust(30);
        } else if (level >= 5 && level < 10) {
            setCooldown(5 * 6000);
            setManaCoust(20);
        } else if (level == 10) {
            setCooldown(3 * 6000);
            setManaCoust(10);
        }
    }

    @Override
    public void execute() {
        Player p = Bukkit.getPlayer(getOwner());
        org.bukkit.entity.Fireball fire = (org.bukkit.entity.Fireball)p.getWorld().spawnEntity(p.getEyeLocation(), EntityType.FIREBALL);
        fire.setDirection(p.getEyeLocation().getDirection());
        fire.setVisualFire(true);
        super.execute();
    }
}
