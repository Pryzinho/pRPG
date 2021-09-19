package br.pryzat.rpg.api.characters.skills;

import br.pryzat.rpg.utils.ItemBuilder;
import br.pryzat.rpg.utils.PryColor;
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

    public Branch(String displayName, Material material, int loc, List<SUID> suids){
        this.displayName = displayName;
        this.material = material;
        this.location = loc;
        this.suids = suids;
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

    /**
     *
     * @return Retorna a lista de skill (em suid) de uma ramificação
     */
    public List<SUID> getSUIDS(){
        return suids;
    }

    public void openBranch(Player p){
        Inventory inv = Bukkit.createInventory(null, 9 * 3, PryColor.color(displayName));
        for (int i = 0; i < suids.size(); i++){
            SUID suid = suids.get(i);
            ItemStack item = suid.getItem();
            item.getItemMeta().getLore().add(PryColor.color("&eLevel&f: "));
            item.getItemMeta().getLore().add(PryColor.color("&bClique com botão esquerdo para adicionar um nivel."));
            item.getItemMeta().getLore().add(PryColor.color("&cClique com o botão direito para remover um nivel"));
            inv.setItem(i, item);
        }
        p.openInventory(inv);
    }
}
