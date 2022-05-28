package br.pryzat.rpg.commands;

import br.pryzat.rpg.api.characters.Character;
import br.pryzat.rpg.api.characters.CharacterManager;
import br.pryzat.rpg.api.characters.classes.Clazz;
import br.pryzat.rpg.api.characters.classes.ClazzType;
import br.pryzat.rpg.utils.PryColor;
import br.pryzat.rpg.main.RpgMain;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ClassCommand implements CommandExecutor {
    private RpgMain main;
    private CharacterManager cm;

    public ClassCommand(RpgMain main) {
        this.main = main;
        this.cm = main.getCharacterManager();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(PryColor.color("&eSistema &f> &cApenas jogadores podem utilizar este comando."));
            return true;
        }
        Player p = (Player) sender;
        Character ch = cm.getCharacter(p.getUniqueId());
        ClazzType cz = ch.getClazz();
        if (args.length != 0){
            p.sendMessage(PryColor.color("&eSistema &f> &cEsse comando não existe&f."));
            return true;
        }
        if (cz == null){
            ch.selectClazz();
            p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_YES, 50f, 50f);
            return true;
        }
        p.sendMessage(PryColor.color("&eSistema &f> &cEsse comando não existe."));
        return true;
    }
}
