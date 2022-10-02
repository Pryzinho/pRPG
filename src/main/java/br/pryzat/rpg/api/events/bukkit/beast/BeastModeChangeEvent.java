package br.pryzat.rpg.api.events.bukkit.beast;


import br.pryzat.rpg.api.characters.Character;
import br.pryzat.rpg.api.characters.classes.Beast;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class BeastModeChangeEvent extends Event implements Cancellable {
    private Character owner;
    private Beast beast;
    private Beast.Mode newMode;
    private static final HandlerList handlerlist = new HandlerList();
    private boolean isCancelled;

    public BeastModeChangeEvent(Character owner, Beast beast, Beast.Mode newMode) {
        this.owner = owner;
        this.beast = beast;
        this.newMode = newMode;
    }


    public static HandlerList getHandlerList() {
        return handlerlist;
    }

    @Override
    public HandlerList getHandlers() {
        return handlerlist;
    }

    @Override
    public boolean isCancelled() {
        return isCancelled;
    }

    @Override
    public void setCancelled(boolean b) {
        this.isCancelled = b;
    }

    public Character getOwner() {
        return owner;
    }

    public Beast getBeast() {
        return beast;
    }

    public Beast.Mode getNewMode() {
        return newMode;
    }
}
