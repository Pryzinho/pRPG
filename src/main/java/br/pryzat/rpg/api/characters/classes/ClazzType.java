package br.pryzat.rpg.api.characters.classes;

import br.pryzat.rpg.api.characters.stats.Attributes;
import br.pryzat.rpg.api.items.CustomItem;
import br.pryzat.rpg.api.items.Item;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

public enum ClazzType {
    /*
                    ch.setClazz(new Clazz(main, ClazzType.SWORDSMAN, new Stats(50, 0, 10, 60)));
                    ch.setClazz(new Clazz(main, ClazzType.MAGE, new Stats(10, 60, 0, 55)));
                    ch.setClazz(new Clazz(main, ClazzType.PRIEST, new Stats(10, 30, 0, 100)));
*/
    SWORDSMAN(
            (List<ItemStack>) Arrays.asList(new Item("&9Capacete Inicial", Material.LEATHER_HELMET, new Attributes(0, 0, 0, 5)).toItem(),
                    new Item("&9Peitoral Inicial", Material.LEATHER_CHESTPLATE, new Attributes(0, 0, 0, 10)).toItem(),
                    new Item("&9Calças Inicial", Material.LEATHER_LEGGINGS, new Attributes(0, 0, 0, 5)).toItem(),
                    new Item("&9Botas Inicial", Material.LEATHER_BOOTS, new Attributes(0, 0, 10, 5)).toItem(),
                    new Item("&9Espada Inicial", Material.WOODEN_SWORD, new Attributes(10, 0, 5, 0)).toItem(),
                    new Item("&9Escudo Inicial", Material.SHIELD, new Attributes(0, 0, 0, 30)).toItem()
            ),
            new Attributes(50, 0, 10, 60)), //Cavaleiro -> Hp+, Ataque Fisico, Defesa++
    MAGE(
            (List<ItemStack>) Arrays.asList(new CustomItem(Material.WOODEN_SWORD).toItemStack()),
            new Attributes(10, 60, 0, 55)), //Mago -> Hp, Mana++, Ataque Magico+, Defesa
    ROGUE(
            (List<ItemStack>) Arrays.asList(new CustomItem(Material.WOODEN_SWORD).toItemStack()),
            new Attributes(25, 0, 50, 10)), //Ladino -> Hp-, Mana--, Ataque fisico, Esquiva+++ Precisão e critico +++
    ARCHER(
            (List<ItemStack>) Arrays.asList(new CustomItem(Material.WOODEN_SWORD).toItemStack()),
            new Attributes(0, 50, 25, 5)), //Guarda/Arqueiro -> Hp, Mana+, Ataque magico, Acerto+++, precisao++
    TAMER(
            (List<ItemStack>) Arrays.asList(new CustomItem(Material.WOODEN_SWORD).toItemStack()),
            new Attributes(0, 0, 0, 0)), //Domador -> Hp, Mana+, Ataque magico, Defesa
    PRIEST(
            Arrays.asList(new CustomItem(Material.WOODEN_SWORD).toItemStack()),
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
        if (!p.isOnline()) return;
        innitItems.forEach(i -> p.getInventory().addItem(i));
    }
}
