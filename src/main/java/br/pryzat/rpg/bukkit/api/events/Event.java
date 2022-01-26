package br.pryzat.rpg.bukkit.api.events;

import br.pryzat.rpg.bukkit.main.RpgMain;
import org.bukkit.entity.Player;
import br.pryzat.rpg.bukkit.api.RPG;

public abstract class Event {
    private RpgMain plugin;
    private String euid;
    private boolean enabled, ready, started, finished;

    public Event(RpgMain plugin, String euid){
        this.plugin = plugin;
        this.euid = euid;
        RPG.registerEUID(euid);
    }
    public abstract void finish(Player p);
    public abstract void start(Player p);
    public abstract void ready(Player p);

    public String getEuid() {
        return euid;
    }

    public boolean isReady() {
        return ready;
    }

    public void setReady(boolean ready) {
        this.ready = ready;
    }

    public boolean isStarted() {
        return started;
    }

    public void setStarted(boolean started) {
        this.started = started;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public void setEnabled(boolean enabled){ this.enabled = enabled;}

    public boolean isEnabled(){
        return this.enabled;
    }
    public RpgMain getPlugin(){
        return plugin;
    }
}
