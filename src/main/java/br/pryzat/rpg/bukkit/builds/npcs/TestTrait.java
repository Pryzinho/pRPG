package br.pryzat.rpg.bukkit.builds.npcs;

import br.pryzat.rpg.bukkit.main.RpgMain;
import br.pryzat.rpg.bukkit.utils.ItemBuilder;
import br.pryzat.rpg.bukkit.utils.PryColor;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import net.citizensnpcs.api.trait.Trait;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import java.util.ArrayList;
import java.util.Arrays;

public class TestTrait extends Trait {
    private RpgMain main;

    public TestTrait(RpgMain main) {
        super("teleportador");
        this.main = main;
    }

    @EventHandler
    public void click(NPCRightClickEvent e) {
        Inventory inv;
        switch (e.getNPC().getOwningRegistry().getName()) {
            case "clareira_guardacdp":
                inv = Bukkit.createInventory(null, 9 * 3, PryColor.color("&dTeleporte da Escuridão"));
                inv.setItem(11, ItemBuilder.create("&8Entrar nas Cavernas", Material.IRON_BARS, Arrays.asList("&bLhe teleporta para as Cavernas do Crepusculo.")));
                inv.setItem(13, ItemBuilder.create("&bGuia para Iniciantes", Material.BOOK));
                e.getClicker().openInventory(inv);
                break;
            case "clareira_deusalunar":
                inv = Bukkit.createInventory(null, 9 * 3, PryColor.color("&2Deusa Lunar"));
                inv.setItem(11, ItemBuilder.create("&bEntrar na Paisagem Lunar", Material.IRON_BARS, Arrays.asList("&bLhe teleporta para as Cavernas do Crepusculo.")));
                inv.setItem(13, ItemBuilder.create("&bGuia para Iniciantes", Material.BOOK));
                e.getClicker().openInventory(inv);
                break;
        }

    }

    @EventHandler
    public void clickTp(InventoryClickEvent e) {
        if (!(e.getWhoClicked() instanceof Player)) return;
        Player p = (Player) e.getWhoClicked();
        if (e.getView().getTitle().equals(PryColor.color("&dTeleporte da Escuridão"))) {
            if (e.getCurrentItem() == null) return;
            switch (e.getCurrentItem().getType()) {
                case IRON_BARS:
                    p.teleport(new Location(p.getWorld(),0, 6, 0), PlayerTeleportEvent.TeleportCause.PLUGIN);
                    p.playSound(p.getLocation(), Sound.ENTITY_ENDER_DRAGON_DEATH, SoundCategory.HOSTILE, 30f, 30f);
                    p.sendMessage(PryColor.color("&dVocê entrou nas &2&nCavernas do Crepusculo 1"));
                    break;
                case BOOK:
                    ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
                    BookMeta bookMeta = (BookMeta) book.getItemMeta();
                    ArrayList<String> texto = new ArrayList<String>();
                    texto.add(PryColor.color(" &b&lGuia para Iniciantes\n&dCavernas do Crepusculo&f."));
                    bookMeta.setTitle(PryColor.color("&aAs cavernas se dividem em 4 mapas, cada mapa possui seus propios monstros."));
                    bookMeta.setAuthor(PryColor.color("&aConforme avançar pelas canvernas, isso é indo de 1 a 4, os niveis dos monstros aumentarão."));
                    bookMeta.setAuthor(PryColor.color("&aOs monstros são hostis e atacarão o personagem imediatamente, alguns deles podem ignorar o efeito de furtividade."));
                    bookMeta.setPages(texto);
                    book.setItemMeta(bookMeta);
                    p.openBook(book);
                    break;
                default:
                    p.sendMessage(PryColor.color("&cItem não registrado no evento."));
                    break;

            }
        }

    }





}
