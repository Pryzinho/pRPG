package br.pryzat.rpg.commands;

import br.pryzat.rpg.api.characters.Character;
import br.pryzat.rpg.api.characters.CharacterManager;
import br.pryzat.rpg.main.RpgMain;
import br.pryzat.rpg.utils.Logger;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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
        if (args.length != 0){
            p.sendMessage("args length errdinha");
            return true;
        }
       // p.openInventory(ch.getClazz().getTrees());
        main.changePlayerNameAboveHead(p, "Teste de nome");
        return true;
    }
}
