package br.pryzat.rpg.bukkit.api.characters.classes;

import br.pryzat.rpg.bukkit.api.characters.stats.Stats;

public enum ClazzType {
    /*
                    ch.setClazz(new Clazz(main, ClazzType.SWORDSMAN, new Stats(50, 0, 10, 60)));
                    ch.setClazz(new Clazz(main, ClazzType.MAGE, new Stats(10, 60, 0, 55)));
                    ch.setClazz(new Clazz(main, ClazzType.PRIEST, new Stats(10, 30, 0, 100)));
*/
    SWORDSMAN(new Stats(50, 0, 10, 60)), //Cavaleiro -> Hp+, Ataque Fisico, Defesa++
    MAGE(new Stats(10, 60, 0, 55)), //Mago -> Hp, Mana++, Ataque Magico+, Defesa
    ROGUE(new Stats(25, 0, 50, 10)), //Ladino -> Hp-, Mana--, Ataque fisico, Esquiva+++ Precisão e critico +++
    ARCHER(new Stats(0, 50, 25, 5)), //Guarda/Arqueiro -> Hp, Mana+, Ataque magico, Acerto+++, precisao++
    TAMER(new Stats(0, 0, 0, 0)), //Domador -> Hp, Mana+, Ataque magico, Defesa
    PRIEST(new Stats(0, 0, 0, 0)); //Sacerzin -> Hp+++, Mana++, Ataque magico+, Defesa ;
    private Stats stats;

    ClazzType(Stats stats) {
        this.stats = stats;
    }

    public Stats getStats() {
        return stats;
    }
}
