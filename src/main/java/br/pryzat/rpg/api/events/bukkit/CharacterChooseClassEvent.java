package br.pryzat.rpg.api.events.bukkit;


import br.pryzat.rpg.api.characters.Character;
import br.pryzat.rpg.api.characters.classes.ClazzType;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.UUID;

/**
 * Esse evento é chamado ANTES da classe do player ser alterado ou selecionada.
 * O metodo isFirst() retorna verdadeiro se é a primeira vez do personagem escolhendo uma classe.
 * Se o evento for cancelado a classe nao será selecionada ou alterada.
 */
public class CharacterChooseClassEvent extends Event implements Cancellable {
    private Character trigger;
    private ClazzType clazz;
    private boolean isFirst = false;
    private static final HandlerList handlerlist = new HandlerList();
    private boolean isCancelled;

    public CharacterChooseClassEvent(Character trigger, ClazzType clazz) {
        this.trigger = trigger;
        this.clazz = clazz;
        if (trigger.getClazz() == null) {
            this.isFirst = true;
        }
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

    public Character getTrigger() {
        return trigger;
    }

    public ClazzType getClazz() {
        return clazz;
    }

    public boolean isFirst(){
        return this.isFirst;
    }
}
