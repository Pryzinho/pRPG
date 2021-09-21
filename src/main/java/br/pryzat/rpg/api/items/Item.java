package br.pryzat.rpg.api.items;

import br.pryzat.rpg.api.characters.stats.Stats;
import net.minecraft.nbt.NBTTagCompound;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_17_R1.inventory.CraftItemStack;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import br.pryzat.rpg.utils.PryColor;

import java.util.List;

public class Item {
    private String iuid;
    private String displayName;
    private Material material;
    private List<String> lore;
    private Stats stats;

    public Item(String displayName, Material material, Stats stats) {
        this.displayName = displayName;
        this.material = material;
        this.stats = stats;
    }

    public ItemStack toItem() {
        ItemStack bis = new ItemStack(material);
        ItemMeta bim = bis.getItemMeta();
        bim.setDisplayName(PryColor.color(displayName));
		if (getStats().getStrength() !=0){
		this.lore.add(PryColor.color("&cForça&f: " + getStats().getStrength()));	
		}
		if (getStats().getInteligency() !=0){
		this.lore.add(PryColor.color("&bInteligência&f: " + getStats().getInteligency()));		
		}
		if (getStats().getVelocity() !=0){
			this.lore.add(PryColor.color("&8Velocidade&f: " + getStats().getVelocity()));	
		}
		if (getStats().getResistance() !=0){
			this.lore.add(PryColor.color("&8Resistência&f: " + getStats().getResistance()));	
		}
        bim.setLore(lore);
        bim.setUnbreakable(true);
        bim.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_UNBREAKABLE, ItemFlag.HIDE_ENCHANTS);
        bis.setItemMeta(bim);
        net.minecraft.world.item.ItemStack nis = CraftItemStack.asNMSCopy(bis);
        NBTTagCompound nbt = nis.getOrCreateTag();
        nbt.setString("rpg.item.uid", getIUID());
        nbt.setInt("rpg.item.strength", getStats().getStrength());
        nbt.setInt("rpg.item.inteligency", getStats().getInteligency());
        nbt.setInt("rpg.item.velocity", getStats().getVelocity());
        nbt.setInt("rpg.item.resistance", getStats().getResistance());
        nis.setTag(nbt);
        return CraftItemStack.asBukkitCopy(nis);
    }

    public String getIUID() {
        return iuid;
    }

    public void setIUID(String iuid) {
        this.iuid = iuid;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public List<String> getLore() {
        return lore;
    }

    public void setLore(List<String> lore) {
        this.lore = lore;
    }

    public Stats getStats() {
        return stats;
    }

    public void setStats(Stats stats) {
        this.stats = stats;
    }
}
