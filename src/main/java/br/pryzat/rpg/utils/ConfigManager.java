package br.pryzat.rpg.utils;

import br.pryzat.rpg.main.RpgMain;

public class ConfigManager {
    private RpgMain main;
    private PryConfig config;

    public ConfigManager(RpgMain main){
        this.main = main;
        config = new PryConfig(main, "config.yml");
    }

    public PryConfig getYml(){
        return config;
    }
}
