package br.pryzat.rpg.api.characters.classes;

import br.pryzat.rpg.api.RPG;
import br.pryzat.rpg.api.characters.skills.Branch;
import br.pryzat.rpg.api.characters.skills.SUID;
import br.pryzat.rpg.api.characters.skills.Skills;
import br.pryzat.rpg.api.characters.stats.Attributes;
import br.pryzat.rpg.api.items.Item;
import br.pryzat.rpg.main.RpgMain;
import br.pryzat.rpg.utils.PryColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class Clazz {
    private RpgMain main;
    private Attributes attributes;
    private ClazzType classtype;
    private Skills skills;
    private List<Branch> branches;

    public Clazz(RpgMain main, ClazzType classtype) {
        this.classtype = classtype;
        this.attributes = classtype.getAttributes();
        this.main = main;
        skills = new Skills(main);
    }

    public Inventory getTrees() {
        Branch a = null, b = null;
        switch (classtype) {
            case SWORDSMAN:
                List<SUID> swasuids = new ArrayList<>();
                swasuids.add(RPG.getSUID("perseguir"));
                List<SUID> swdsuids = new ArrayList<>();
                swdsuids.add(RPG.getSUID("stomper"));
                a = new Branch("&cAtaque", Material.IRON_SWORD, 11, swasuids, skills);
                b = new Branch("&bDefesa", Material.SHIELD, 15, swdsuids, skills);
                break;
            case MAGE:
                List<SUID> mgosuids = new ArrayList<>();
                mgosuids.add(RPG.getSUID("fireball"));
                List<SUID> mgdsuids = new ArrayList<>();
                mgdsuids.add(RPG.getSUID("stomper"));
                a = new Branch("&bOffensive", Material.FIRE_CHARGE, 11, mgosuids, skills);
                b = new Branch("&eDefensive", Material.SNOWBALL, 15, mgdsuids, skills);
                break;
            case ROGUE:
                break;
            case ARCHER:
                break;
            case TAMER:
                break;
            case PRIEST:
                break;
        }
        branches.add(a);
        branches.add(b);
        Inventory inv = Bukkit.createInventory(null, 9 * 3, PryColor.color("&eRamificações de Habilidades"));
        for (Branch branch : branches) {
            ItemStack item = branch.getItem();
            inv.setItem(branch.getLoc(), item);
        }
        return inv;
    }
    public void setStats(Attributes attributes){
        this.attributes = attributes;
    }
    public void giveInitialItens(Player p) {
		if (p == null)return;
        if (!p.isOnline()) return;
        Item h = new Item("&9Capacete Inicial", Material.LEATHER_HELMET, new Attributes(0, 0, 0, 5));
        Item c = new Item("&9Peitoral Inicial", Material.LEATHER_CHESTPLATE, new Attributes(0, 0, 0, 10));
        Item l = new Item("&9Calças Inicial", Material.LEATHER_LEGGINGS, new Attributes(0, 0, 0, 5));
        Item b = new Item("&9Botas Inicial", Material.LEATHER_BOOTS, new Attributes(0, 0, 10, 5));
        Item sw = new Item("&9Espada Inicial", Material.WOODEN_SWORD, new Attributes(10, 0, 5, 0));
        Item sd = new Item("&9Escudo Inicial", Material.SHIELD, new Attributes(0, 0, 0, 30));
        switch (classtype) {
            case SWORDSMAN:
                h.setIUID("initial.swordsman.helmet");
                c.setIUID("initial.swordsman.chestplate");
                l.setIUID("initial.swordsman.leggins");
                b.setIUID("initial.swordsman.boots");
                sw.setIUID("initial.swordsman.sword");
                sd.setIUID("initial.swordsman.shield");
                p.getEquipment().setHelmet(h.toItem());
                p.getEquipment().setChestplate(c.toItem());
                p.getEquipment().setLeggings(l.toItem());
                p.getEquipment().setBoots(b.toItem());
                p.getInventory().addItem(sw.toItem());
                p.getInventory().addItem(sd.toItem());
                p.updateInventory();
                break;
        case ROGUE:
		        h.setIUID("initial.rogue.helmet");
                c.setIUID("initial.rogue.chestplate");
                l.setIUID("initial.rogue.leggins");
                b.setIUID("initial.rogue.boots");
                sw.setIUID("initial.rogue.sword");
                sd.setIUID("initial.rogue.shield");
				h.setStats(new Attributes(0, 0, 0, 5));
				c.setStats(new Attributes(0, 0, 0, 5));
				l.setStats(new Attributes(0, 0, 10, 3));
				b.setStats(new Attributes(0, 0, 15, 2));
                p.getEquipment().setHelmet(h.toItem());
                p.getEquipment().setChestplate(c.toItem());
                p.getEquipment().setLeggings(l.toItem());
                p.getEquipment().setBoots(b.toItem());
                p.getInventory().addItem(sw.toItem());
                p.updateInventory();
		break;
		}
    }
    public List<Branch> getBranches(){
        return this.branches;
    }

    public Attributes getStats() {
        return attributes;
    }

    public Skills getSkills() {
        return skills;
    }

    public ClazzType getClassType() {
        return classtype;
    }

}
