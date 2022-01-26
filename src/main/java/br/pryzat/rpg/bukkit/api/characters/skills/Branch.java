package br.pryzat.rpg.bukkit.api.characters.skills;

import br.pryzat.rpg.bukkit.utils.ItemBuilder;
import br.pryzat.rpg.bukkit.utils.PryColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class Branch {
    private String displayName;
    private Material material;
    private int location;
    private List<SUID> suids;
    private Skills skills;

    public Branch(String displayName, Material material, int loc, List<SUID> suids, Skills skills){
        this.displayName = displayName;
        this.material = material;
        this.location = loc;
        this.suids = suids;
        this.skills = skills;
    }

    /**
     *
     * @return Retorna o nome de uma ramificação, ex. Ataque, Defesa, Magia Ofensiva, Cura...
     */
    public String getDisplayName(){
        return displayName;
    }
    public Material getMaterial(){
        return material;
    }
    public ItemStack getItem(){
        return ItemBuilder.create(displayName, material);
    }

    /**
     *
     * @return Retorna o slot que o item vai ficar, sim ele pode escolher 'O'
     */
    public int getLoc(){
        return location;
    }

    public void openBranch(Player p){
        Inventory inv = Bukkit.createInventory(null, 9 * 3, PryColor.color(displayName));
        for (int i = 0; i < suids.size(); i++){
            SUID suid = suids.get(i);
            ItemStack item = suid.getItem();
            if (skills.has(suid.getSuid())) {
                item.getItemMeta().getLore().add(PryColor.color("&eLevel&f: " + skills.get(suid.getSuid())));
            } else {
                item.getItemMeta().getLore().add(PryColor.color("&cVocê ainda não aprendeu está habilidade!"));
            }
            item.getItemMeta().getLore().add(PryColor.color("&bClique com botão esquerdo para adicionar um nivel."));
            inv.setItem(i, item);
        }
        p.openInventory(inv);
    }
}
