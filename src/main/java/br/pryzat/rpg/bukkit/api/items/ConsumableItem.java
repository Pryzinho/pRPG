package br.pryzat.rpg.bukkit.api.items;

import br.pryzat.rpg.bukkit.utils.PryColor;
import net.minecraft.nbt.NBTTagCompound;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_17_R1.inventory.CraftItemStack;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public abstract class ConsumableItem {
    private String iuid;
    private String displayName;
    private Material material;
    private int amount;
    private List<String> lore;

    /**
     * Create a new Item
     * @param iuid Item Unique Identifier
     * @param displayName Display Name
     * @param material Bukkit Material
     * @param amount Amount of the item
     * @param lore Lore of the item
     */
    public ConsumableItem(String iuid, String displayName, Material material, int amount, List<String> lore) {
        this.iuid = iuid;
        this.displayName = displayName;
        this.material = material;
        this.amount = amount;
        this.lore = lore;
    }

    public ItemStack toItem() {
        ItemStack bis = new ItemStack(material, amount);
        ItemMeta bim = bis.getItemMeta();
        bim.setDisplayName(PryColor.color(displayName));
        if (lore != null) {
            this.lore = PryColor.color(lore);
            bim.setLore(lore);
        }
        bim.setUnbreakable(true);
        bim.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_UNBREAKABLE, ItemFlag.HIDE_ENCHANTS);
        bis.setItemMeta(bim);
        net.minecraft.world.item.ItemStack nis = CraftItemStack.asNMSCopy(bis);
        NBTTagCompound nbt = nis.getOrCreateTag();
        nbt.setString("rpg.item.uid", getIUID());
        nis.setTag(nbt);
        return CraftItemStack.asBukkitCopy(nis);
    }

    public boolean equals(Item t) {
        if (t == null) return false;
        ItemStack is = t.toItem();
        net.minecraft.world.item.ItemStack nis = CraftItemStack.asNMSCopy(is);
        if (!nis.hasTag()) return false;
        if (nis.getTag().getString("rpg.item.uid") != getIUID()) return false;
        return true;
    }
    public boolean equals(ItemStack t) {
        if (t == null) return false;
        net.minecraft.world.item.ItemStack nis = CraftItemStack.asNMSCopy(t);
        if (!nis.hasTag()) return false;
        if (nis.getTag().getString("pitems.iuid") != getIUID()) return false;
        return true;
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

    public abstract void execute(Object obj);

}
