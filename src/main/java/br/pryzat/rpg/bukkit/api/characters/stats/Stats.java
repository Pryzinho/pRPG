package br.pryzat.rpg.bukkit.api.characters.stats;

public class Stats {
    private int strength; // Força > Ataque fisisco, critico
    private int inteligency; // Iteligencia > Ataque magico, mana, acerto
    private int velocity; // Esquiva > Esquiva, precisão
    private int resistance; // Resistencia > Defesa fisica e magica, hp

    /**
     * @param strength    Pontos de força
     * @param inteligency Pontos de inteligência
     * @param velocity    Pontos de velocidade
     * @param resistance  Pontos de resistancia
     */
    public Stats(int strength, int inteligency, int velocity, int resistance) {
        this.strength = strength;
        this.inteligency = inteligency;
        this.velocity = velocity;
        this.resistance = resistance;
    }

    public int getStrength() {
        return strength;
    }

    public void setStrength(int strength) {
        this.strength = strength;
    }

    public void addStrength(int strength) {
        this.strength += strength;
    }

    public void remStrength(int strength) {
        this.strength -= strength;
    }

    public int getInteligency() {
        return inteligency;
    }

    public void setInteligency(int inteligency) {
        this.inteligency = inteligency;
    }

    public void addInteligency(int inteligency) {
        this.inteligency += inteligency;
    }

    public void remInteligency(int inteligency) {
        this.inteligency -= inteligency;
    }

    public int getVelocity() {
        return strength;
    }

    public void setVelocity(int velocity) {
        this.velocity = velocity;
    }

    public void addVelocity(int velocity) {
        this.velocity += velocity;
    }

    public void remVelocity(int velocity) {
        this.velocity = velocity;
    }

    public int getResistance() {
        return resistance;
    }

    public void setResistance(int resistance) {
        this.resistance = resistance;
    }

    public void addResistance(int resistance) {
        this.resistance += resistance;
    }

    public void remResistance(int resistance) {
        this.resistance -= resistance;
    }
}
