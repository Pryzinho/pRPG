package br.pryzat.rpg.itens;

import net.minecraft.nbt.NBTTagCompound;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_17_R1.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Grisaia {
    private ItemStack item;
    private net.minecraft.world.item.ItemStack is;
    public Grisaia() {
        item = new ItemStack(Material.ALLIUM);
        net.minecraft.world.item.ItemStack is = CraftItemStack.asNMSCopy(item);
        NBTTagCompound tc = is.getOrCreateTag();
        tc.setString("rpgdata", "grisaia");
        is.setTag(tc);
        item = CraftItemStack.asBukkitCopy(is);
        this.is = is;
    }
    public boolean equals(ItemStack itemstack){
        net.minecraft.world.item.ItemStack nmitem = CraftItemStack.asNMSCopy(itemstack);
        if (nmitem.getTag() != null){
            NBTTagCompound tagCompound = nmitem.getTag();
            if (tagCompound.getString("rpgdata") == "grisaia"){
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

}
