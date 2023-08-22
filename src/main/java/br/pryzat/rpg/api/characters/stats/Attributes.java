package br.pryzat.rpg.api.characters.stats;

public class Attributes {
    /* Atributos Naturais */
    private double MAX_HP, MAX_MANA;
    private double HEALTH, MANA;
    /* Atributos Primarios */
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
    public Attributes(int strength, int inteligency, int velocity, int resistance) {
        this.strength = strength;
        this.inteligency = inteligency;
        this.velocity = velocity;
        this.resistance = resistance;
    }

    public Attributes() {

    }

    public Attributes(Attributes att) {
        setInteligency(att.getInteligency());
        setResistance(att.getResistance());
        setStrength(att.getStrength());
        setVelocity(att.getVelocity());
    }

    /* Atributos Naturais */
    public double getMaxHealth() {
        return MAX_HP;
    }

    public double getMaxMana() {
        return MAX_MANA;
    }

    public double getHealth() {
        return HEALTH;
    }

    public double getMana() {
        return MANA;
    }

    public Attributes setMaxHealth(double MAX_HP) {
        this.MAX_HP = MAX_HP;
        return this;
    }

    public Attributes setMaxMana(double MAX_MANA) {
        this.MAX_MANA = MAX_MANA;
        return this;
    }

    public Attributes setHealth(double HEALTH) {
        this.HEALTH = HEALTH;
        return this;
    }

    public Attributes setMana(double MANA) {
        this.MANA = MANA;
        return this;
    }

    public void addHealth(double health){
        this.HEALTH += health;
    }
    public void addMana(double mana){
        this.MANA += mana;
    }
    /* Atributos Primarios */
    public int getStrength() {
        return strength;
    }

    public int getInteligency() {
        return inteligency;
    }

    public int getVelocity() {
        return strength;
    }

    public int getResistance() {
        return resistance;
    }


    public Attributes setStrength(int strength) {
        this.strength = strength;
        return this;
    }

    public Attributes setInteligency(int inteligency) {
        this.inteligency = inteligency;
        return this;
    }

    public Attributes setVelocity(int velocity) {
        this.velocity = velocity;
        return this;
    }

    public Attributes setResistance(int resistance) {
        this.resistance = resistance;
        return this;
    }

    public void addStrength(int strength) {
        this.strength += strength;
    }
    public void addInteligency(int inteligency) {
        this.inteligency += inteligency;
    }
    public void addVelocity(int velocity) {
        this.velocity += velocity;
    }
    public void addResistance(int resistance) {
        this.resistance += resistance;
    }
}
