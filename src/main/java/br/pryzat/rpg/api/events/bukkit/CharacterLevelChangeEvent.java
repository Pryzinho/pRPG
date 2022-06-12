package br.pryzat.rpg.api.events.bukkit;


import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.UUID;

public class CharacterLevelChangeEvent extends Event implements Cancellable {
    private UUID trigger;
    private Cause cause;
    private int newLevel;
    private static final HandlerList handlerlist = new HandlerList();
    private boolean isCancelled;

    public CharacterLevelChangeEvent(UUID trigger, Cause cause, int newLevel) {
        this.trigger = trigger;
        this.cause = cause;
        this.newLevel = newLevel;
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

    public UUID getTrigger() {
        return trigger;
    }

    public Cause getCause() {
        return cause;
    }

    public int getNewLevel() {
        return newLevel;
    }

    public enum Cause {
        ADD,
        REMOVE,
        SET
    }
}
