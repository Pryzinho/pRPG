package br.pryzat.rpg.api.events.bukkit.character;


import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.UUID;

public class CharacterLevelChangeEvent extends Event implements Cancellable {
    private final UUID trigger;
    private final Cause cause;
    private final int newLevel;
    private final int oldLevel;
    private static final HandlerList handlerlist = new HandlerList();
    private boolean isCancelled;

    public CharacterLevelChangeEvent(UUID trigger, Cause cause, int oldLevel, int newLevel) {
        this.trigger = trigger;
        this.cause = cause;
        this.oldLevel = oldLevel;
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

    public int getOldLevel() {
        return oldLevel;
    }

    public enum Cause {
        ADD,
        REMOVE,
        SET
    }
}
