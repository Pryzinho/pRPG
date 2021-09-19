package br.pryzat.rpg.commands;

import br.pryzat.rpg.api.characters.Character;
import br.pryzat.rpg.api.characters.CharacterManager;
import br.pryzat.rpg.api.RPG;
import br.pryzat.rpg.api.characters.skills.Branch;
import br.pryzat.rpg.main.RpgMain;
import br.pryzat.rpg.utils.Logger;
import br.pryzat.rpg.utils.PryColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class SkillsCommand implements CommandExecutor {
    private RpgMain main;
    private CharacterManager cm;

    public SkillsCommand(RpgMain main) {
        this.main = main;
        this.cm = main.getCharacterManager();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            Logger.log(sender.getServer().getConsoleSender(), "&cSomente jogadores podem utilizar este comando.");
            return true;
        }
        Player p = (Player) sender;
        Character ch = cm.getCharacter(p.getUniqueId());
        if (ch.getClazz() == null) {
            ch.selectClazz();
            return true;
        }
        Inventory inv = Bukkit.createInventory(null, 9 * 3, PryColor.color("&eRamificações de Habilidades"));
        //11, 13
        for (Branch branch : RPG.getBranches(ch.getClazz().getClassType()).toList()) {
            ItemStack item = branch.getItem();
            inv.setItem(branch.getLoc(), item);
        }
        p.openInventory(inv);
        return true;
    }
}
