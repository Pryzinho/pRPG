package br.pryzat.rpg.api.items;

import br.pryzat.rpg.api.characters.Character;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class PackageItem extends ConsumableItem {
    private List<ItemStack> items = new ArrayList<>();

    /**
     * Create a new Item
     *
     * @param iuid        Item Unique Identifier
     * @param displayName Display Name
     * @param material    Bukkit Material
     * @param amount      Amount of the item
     */
    public PackageItem(String iuid, String displayName, Material material, int amount) {
        super(iuid, displayName, material, amount, null);
        setIUID(iuid);
    }

    /**
     * @param obj No caso do PackageItem o obj sempre vai ser uma instancia do character (alvo).
     */
    @Override
    public void execute(Object obj) {
        Character ch = (Character) obj;
        Player t = Bukkit.getPlayer(ch.getUUID());
        if (t != null && t.isOnline()) {
        // check player inventory...
            give(t);
        }
    }

    public void setItems(List<ItemStack> items) {
        this.items = items;
    }

    private void give(Player p) {
        items.forEach(p.getInventory()::addItem);
    }
}
