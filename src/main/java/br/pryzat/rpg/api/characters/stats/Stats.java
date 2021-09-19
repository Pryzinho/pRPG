package br.pryzat.rpg.api.characters.stats;

public class Stats {
    private int strength;
    private int resistance;

    public Stats(int strength, int resistance) {
        this.strength = strength;
        this.resistance = resistance;
    }

    public int getStrength() {
        return strength;
    }

    public void setStrength(int strength) {
        this.strength = strength;
    }

    public void addStrength(int strength){
        this.strength += strength;
    }

    public void remStrength(int strength){
        this.strength -= strength;
    }

    public int getResistance() {
        return resistance;
    }

    public void setResistance(int resistance) {
        this.resistance = resistance;
    }

    public void addResistance(int resistance){
        this.resistance += resistance;
    }

    public void remResistance(int resistance){
        this.resistance -= resistance;
    }
}
