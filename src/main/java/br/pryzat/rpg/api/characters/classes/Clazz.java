package br.pryzat.rpg.api.characters.classes;

import br.pryzat.rpg.api.RPG;
import br.pryzat.rpg.api.characters.skills.Branch;
import br.pryzat.rpg.api.characters.skills.SUID;
import br.pryzat.rpg.api.characters.skills.Skills;
import br.pryzat.rpg.api.characters.stats.Stats;
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
    private Stats stats;
    private ClazzType classtype;
    private Skills skills;
    private List<Branch> branches;
    private ItemStack[] initialitems;

    public Clazz(RpgMain main, ClazzType classtype, Stats stats) {
        this.classtype = classtype;
        this.stats = stats;
        this.main = main;
        skills = new Skills(main);
    }

    public Inventory getTrees() {
        Branch a = null, b = null;
        switch (classtype) {
            case SWORDSMAN:
                List<SUID> suids = new ArrayList<>();
                suids.add(RPG.getSUID("perseguir"));
                a = new Branch("&cAtaque", Material.IRON_SWORD, 11, suids, skills);
                b = new Branch("&bDefesa", Material.SHIELD, 15, suids, skills);
                /*
          b =15
          0, 0, 0, 0, 0, 0, 0, 0, 0, X
          0, 0, a, 0, 0, 0, b, 0, 0,
          0, 0, 0, 0, 0, 0, 0, 0, 0, X
           /skills
           verifica a classe do player
           e abre as ramificações
           Abre um iventario de ramificações
           se for um cavaleiro vai aparecer as branch de cavaleiro que no caso é
           espada de ferro e escudo e o nome dos item customizados
           verificar a ramificação selecionada
           se for "a" abrir a ramificação
           Abre um inventario
           mostrando nesse invetario todas a habilidades disponiveis para aquisaição do
           player, alem disso todas as habilidades que ele tem e seu respectivel level
           Branch -> DisplayName do item, materia, locs, todas as habilidades adquiriveis e
           todas as habilidades que o player ja tem e seus respectivos niveis
           */

            case MAGE:
            case ROGUE:
            case TAMER:
            case RANGER:
            case SUPPORT:
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

    public void giveInitialItens(Player p) {
        if (!p.isOnline()) return;
        Item h = new Item("&9Capacete Inicial", Material.LEATHER_HELMET, new Stats(0, 0, 0, 5));
        Item c = new Item("&9Peitoral Inicial", Material.LEATHER_CHESTPLATE, new Stats(0, 0, 0, 10));
        Item l = new Item("&9Calças Inicial", Material.LEATHER_LEGGINGS, new Stats(0, 0, 0, 5));
        Item b = new Item("&9Botas Inicial", Material.LEATHER_BOOTS, new Stats(0, 0, 10, 5));
        Item sw = new Item("&9Espada Inicial", Material.WOODEN_SWORD, new Stats(10, 0, 10, 0));
        Item sd = new Item("&9Escudo Inicial", Material.SHIELD, new Stats(0, 0, 0, 30));
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
        }
    }
    public List<Branch> getBranches(){
        return this.branches;
    }

    public Stats getStats() {
        return stats;
    }

    public Skills getSkills() {
        return skills;
    }

    public ClazzType getClassType() {
        return classtype;
    }

}
