package br.pryzat.rpg.builds.events;

import br.pryzat.rpg.api.events.Event;
import br.pryzat.rpg.main.RpgMain;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DraculaExpedition extends Event {
    public DraculaExpedition(RpgMain plugin, String euid) {
        super(plugin, euid);
        setReady(false);
        setStarted(false);
        setFinished(false);
    }

    @Override
    public void finish(Player p) {

    }

    @Override
    public void start(Player p) {

    }

    @Override
    public void ready(Player p) {

    }
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        return false;
    }
    @Override
    public void saveConfiguration() {

    }

}
