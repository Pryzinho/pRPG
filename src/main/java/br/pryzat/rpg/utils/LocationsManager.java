package br.pryzat.rpg.utils;

import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;

public class LocationsManager {
    private JavaPlugin plugin;
    private PryConfig locationsyml;

    public LocationsManager(JavaPlugin plugin){
        this.plugin = plugin;
        locationsyml = new PryConfig(plugin, "locations.yml");
        locationsyml.saveDefaultConfig();
    }

    public Location getLocation(String path){
        return locationsyml.getLocation(path);
    }
    public void setLocation(String path, Location loc){
        locationsyml.setLocation(path, loc);
        locationsyml.saveConfig();
    }

    public PryConfig getYml(){
        return locationsyml;
    }
}
