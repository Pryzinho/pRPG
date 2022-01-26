package br.pryzat.rpg.bukkit.builds.skills.swordsman;

import br.pryzat.rpg.bukkit.api.characters.skills.Skill;
import br.pryzat.rpg.bukkit.main.RpgMain;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;
import br.pryzat.rpg.bukkit.api.characters.Character;
import java.util.UUID;

public class Perseguir extends Skill {

    public Perseguir(RpgMain main, UUID uuid, int level) {
        super(main, uuid, level);
        setUniqueId("perseguir");
        setDisplayName("&aPerseguição !");
        setNeedMana(false);
        setNeedLife(true);
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
        Player p = Bukkit.getPlayer(getOwner());
       // Vector lsd = p.getEyeLocation().getDirection().multiply(5);
        // Vector ls = p.getEyeLocation().toVector().add(lsd);
         //lsd.multiply(-1);
		// Location v1 = p.getEyeLocation().toVector().add(p.getEyeLocation().getDirection().multiply(5)).toLocation(p.getWorld());
        RayTraceResult rtr = p.getWorld().rayTraceEntities(p.getEyeLocation(), p.getEyeLocation().getDirection().multiply(5), 5);
        if (rtr == null){
            p.sendMessage("RayTraceResult null");
            return;
        }
        Entity e = rtr.getHitEntity();
        if (e == null) {
            p.sendMessage("RTR.HitEntity null");
            return;
        }
        if (!(e instanceof Player)) {
            p.sendMessage("Não é um player");
            return;
        }
        Player target = (Player) e;
		if (p.getUniqueId().equals(target.getUniqueId())){
			p.sendMessage("Mesmo player");
			return;
		}
        Location o = p.getLocation();
        Location d = target.getLocation();
        Vector v2 = new Vector();
        v2.setX(d.getX());
        v2.setY(((d.getY() - o.getY()) + d.getY() + 3) / 7);
        v2.setZ(d.getZ());
        p.setVelocity(v2);
		p.sendMessage(target.getName()); //Debug 
        Character ch = getPlugin().getCharacterManager().getCharacter(getOwner());
        if (ch.getSkills().has("stomper")) {
            if (p.getNearbyEntities(3, 3, 3) != null) {
                for (Entity entity : p.getNearbyEntities(3, 3, 3)) {
                    if (entity instanceof Player) {
                        p.damage(10, entity);
                    }
                }
            }
        }
        super.execute();
    }
}
