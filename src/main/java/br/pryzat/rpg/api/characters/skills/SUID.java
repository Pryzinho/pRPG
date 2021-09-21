package br.pryzat.rpg.api.characters.skills;

import br.pryzat.rpg.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.List;

/**
 * Skill Unique Identifier
 */
public class SUID {
    private String suid; // stomper
    private String displayName; // &aStomper
    private Material material; // Slime block de stomper ha
    private List<String> lore;

    public SUID(String suid, String displayName, Material material, List<String> lore) {
        this.suid = suid;
        this.displayName = displayName;
        this.material = material;
        this.lore = lore;
    }

    public String getSuid() {
        return suid;
    }

    public String getDisplayName() {
        return displayName;
    }

    public Material getMaterial() {
        return material;
    }

    public List<String> getLore(){
        return lore;
    }
    public ItemStack getItem(){
        return ItemBuilder.create(displayName, material, lore);
    }

    @Override
    public String toString() {
        return suid;
    }
}
