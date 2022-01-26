package br.pryzat.rpg.bukkit.api.events;

import br.pryzat.rpg.bukkit.api.RPG;
import br.pryzat.rpg.bukkit.builds.events.ColheitaMaldita;
import br.pryzat.rpg.bukkit.main.RpgMain;
import org.bukkit.Location;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;

public class EventManager {
    private final RpgMain plugin;
    // Custom Events
    private ColheitaMaldita ecm;

    public EventManager(RpgMain main) {
        this.plugin = main;
    }

    public void loadAllEvents() {
        HashMap<Integer, Location> flowers = new HashMap<>();
        for (String key : plugin.getLocationManager().getYml().getSection("flowers")) {
            if (key != null) {
                flowers.put(Integer.parseInt(key), plugin.getLocationManager().getYml().getLocation("flowers." + key));
            }
        }
        ecm = new ColheitaMaldita(plugin, "colheitamaldita", flowers);
        ecm.setEnabled(plugin.getConfigManager().getYml().getBoolean("events.colheitamaldita"));
    }

    public void registerAllEvents() {
        if (ecm.isEnabled()) {
            if (ecm.getFlowers() == null) {
                ecm.initFlowers();
            }
            for (int i = 0; i < ecm.getFlowers().size(); i++) {
                if (ecm.getFlowers().get(i) != null) {
                    plugin.getLocationManager().getYml().setLocation("flowers." + (i + 1), ecm.getFlowers().get(i));
                }
            }
        }
        plugin.getConfigManager().getYml().set("events." + ecm.getEuid(), RPG.getEvent(ecm.getEuid()).isEnabled());
        plugin.getLocationManager().getYml().saveConfig();
    }

    public void checkProgramedEvents() {
        SimpleDateFormat sdf = new SimpleDateFormat("hh");
        sdf.setCalendar(new GregorianCalendar());
        String date = sdf.format(new Date(System.currentTimeMillis()));
        switch (date) {
            case "12":
                if (!ecm.isEnabled()) ecm.ready(null);
            case "16":
                if (!ecm.isEnabled()) ecm.ready(null);
            case "20":
                if (!ecm.isEnabled()) ecm.ready(null);
        }
    }

    public ColheitaMaldita getColheitaMaldita() {
        return ecm;
    }
}
