package br.pryzat.rpg.api.characters.classes;

import br.pryzat.rpg.api.characters.stats.Attributes;
import br.pryzat.rpg.api.items.ItemManager;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public enum ClazzType {
    /*
                    ch.setClazz(new Clazz(main, ClazzType.SWORDSMAN, new Stats(50, 0, 10, 60)));
                    ch.setClazz(new Clazz(main, ClazzType.MAGE, new Stats(10, 60, 0, 55)));
                    ch.setClazz(new Clazz(main, ClazzType.PRIEST, new Stats(10, 30, 0, 100)));
*/
    /*
    void a(){
        Item h = new Item("&9Capacete Inicial", Material.LEATHER_HELMET, new Attributes(0, 0, 0, 5));
        Item c = new Item("&9Peitoral Inicial", Material.LEATHER_CHESTPLATE, new Attributes(0, 0, 0, 10));
        Item l = new Item("&9Calças Inicial", Material.LEATHER_LEGGINGS, new Attributes(0, 0, 0, 5));
        Item b = new Item("&9Botas Inicial", Material.LEATHER_BOOTS, new Attributes(0, 0, 10, 5));
        Item sw = new Item("&9Espada Inicial", Material.WOODEN_SWORD, new Attributes(10, 0, 5, 0));
        Item sd = new Item("&9Escudo Inicial", Material.SHIELD, new Attributes(0, 0, 0, 30));
            case SWORDSMAN:
                h.setIUID("initial.swordsman.helmet");
                c.setIUID("initial.swordsman.chestplate");
                l.setIUID("initial.swordsman.leggins");
                b.setIUID("initial.swordsman.boots");
                sw.setIUID("initial.swordsman.sword");
                sd.setIUID("initial.swordsman.shield");
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
    }
    */

    SWORDSMAN(
            ItemManager.getInitialItems("SWORDSMAN"),
            new Attributes(50, 0, 10, 60)), //Cavaleiro -> Hp+, Ataque Fisico, Defesa++
    MAGE(
            ItemManager.getInitialItems("MAGE"),
            new Attributes(10, 60, 0, 55)), //Mago -> Hp, Mana++, Ataque Magico+, Defesa
    ROGUE(
           ItemManager.getInitialItems("ROGUE"),
            new Attributes(25, 0, 50, 10)), //Ladino -> Hp-, Mana--, Ataque fisico, Esquiva+++ Precisão e critico +++
    ARCHER(
           ItemManager.getInitialItems("ARCHER"),
            new Attributes(0, 50, 25, 5)), //Guarda/Arqueiro -> Hp, Mana+, Ataque magico, Acerto+++, precisao++
    TAMER(
           ItemManager.getInitialItems("TAMER"),
            new Attributes(0, 0, 0, 0)), //Domador -> Hp, Mana+, Ataque magico, Defesa
    PRIEST(
            ItemManager.getInitialItems("PRIEST"),
            new Attributes(0, 0, 0, 0)); //Sacerzin -> Hp+++, Mana++, Ataque magico+, Defesa ;

    // Items iniciais, recebidos após escolher a classe
    List<ItemStack> innitItems;
    private Attributes attributes;


    ClazzType(List<ItemStack> innititems, Attributes attributes) {
        this.attributes = attributes;
        this.innitItems = innititems;
    }

    public Attributes getAttributes() {
        return attributes;
    }

    public void giveInitialItens(Player p) {
        if(p == null)return;
        if (!p.isOnline()) return;
        innitItems.forEach(i -> p.getInventory().addItem(i));
    }
}
