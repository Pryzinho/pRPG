package br.pryzat.rpg.commands;

import br.pryzat.rpg.main.RpgMain;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TestCommand implements CommandExecutor {
    private RpgMain main;

    public TestCommand(RpgMain main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("So player");
            return true;
        }
        Player p = (Player) sender;
        if (args.length != 0) {
            p.sendMessage("Args erradinha");
            return true;
        }
        main.getCharacterManager().getCharacter(p.getUniqueId()).getLevelManager().setExp(10000000);
      //  main.changePlayerNameAboveHead(p, "Teste de nome");
        return true;
    }
}
