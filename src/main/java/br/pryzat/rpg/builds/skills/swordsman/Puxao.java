package br.pryzat.rpg.builds.skills.swordsman;

import br.pryzat.rpg.api.characters.Character;
import br.pryzat.rpg.api.characters.skills.Skill;
import br.pryzat.rpg.api.events.bukkit.CharacterTargettedBySkillEvent;
import br.pryzat.rpg.main.RpgMain;
import br.pryzat.rpg.utils.PryColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.UUID;

public class Puxao extends Skill {

    public Puxao(Character owner, int level) {
        super(owner, level);
        setUniqueId("puxao");
        setDisplayName("&aPuxão");
        setNeedMana(false);
        setNeedLife(false);
        if (level > 10) setLevel(10);
        setLevel(level);
        if (level < 5) {
            setCooldown(7 * 60000);
            setLifeCoust(6);
        } else if (level >= 5 && level < 10) {
            setCooldown(5 * 60000);
            setLifeCoust(4);
        } else if (level == 10) {
            //setCooldown(3 * 60000);
            setCooldown(1 * 6000); //test skill temp
            setLifeCoust(2);
        }
    }

    @Override
    public void execute() {
        Player p = Bukkit.getPlayer(getOwner().getUUID());
        Location loc = p.getLocation();
        if (p.getNearbyEntities(3, 2, 3) != null) {

            for (Entity entity : p.getNearbyEntities(3, 2, 3)) {
                if (entity instanceof Player) {
                    Player t = (Player) entity;
                    CharacterTargettedBySkillEvent ctbse = new CharacterTargettedBySkillEvent(t.getUniqueId());
                    Bukkit.getPluginManager().callEvent(ctbse);
                    if (!ctbse.isCancelled()) {
                        t.teleport(p.getLocation(), PlayerTeleportEvent.TeleportCause.PLUGIN);
                    }

                }
            }
        } else {
            p.sendMessage(PryColor.color("&cNenhum alvo disponivel&f."));
        }
        super.execute();
    }
}
