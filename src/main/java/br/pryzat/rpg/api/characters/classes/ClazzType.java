package br.pryzat.rpg.api.characters.classes;

import br.pryzat.rpg.api.characters.stats.Stats;

public enum ClazzType {
    SWORDSMAN(new Stats(0, 0, 0, 0)), //Cavaleiro -> Hp+, Ataque Fisico, Defesa++
    MAGE(new Stats(0, 0, 0, 0)), //Mago -> Hp, Mana++, Ataque Magico+, Defesa
    ROGUE(new Stats(0, 0, 0, 0)), //Ladino -> Hp-, Mana--, Ataque fisico, Esquiva+++ Precisão e critico +++
    RANGER(new Stats(0, 0, 0, 0)), //Guarda/Arqueiro -> Hp, Mana+, Ataque magico, Acerto+++, precisao++
    ARCHER(new Stats(0, 0, 0, 0)), //Domador -> Hp, Mana+, Ataque magico, Defesa
    PRIEST(new Stats(0, 0, 0, 0)); //Sacerzin -> Hp+++, Mana++, Ataque magico+, Defesa ;
    private Stats stats;

    ClazzType(Stats stats) {
        this.stats = stats;
    }

    public Stats getStats() {
        return stats;
    }
}
