package br.pryzat.rpg.api.items.types;

import br.pryzat.rpg.api.characters.Character;
import br.pryzat.rpg.main.RpgMain;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class PackageItem extends ConsumableItem {
    private List<ItemStack> items = new ArrayList<>();

    /**
     * Cria um novo Pacote (Presente/Crate) - Aberto ao clicar com botao direito.
     *
     * @param iuid        Item Unique Identifier
     * @param displayName Display Name
     * @param material    Bukkit Material
     */
    public PackageItem(RpgMain pl, String iuid, Material material) {
        super(pl, iuid, material);
        setItemNamespace("rpg.packageitem");
        setUniqueId(iuid);
    }

    /**
     * Abre o pacote, e dá os items que estão dentro, futuramente talvez dê um efeito ou condição.
     *
     * @param obj No caso do PackageItem o obj sempre vai ser uma instância de Character (alvo).
     */
    @Override
    public void execute(Object obj) {
        Character ch = (Character) obj;
        Player t = Bukkit.getPlayer(ch.getUUID());
        if (t != null && t.isOnline()) {
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
