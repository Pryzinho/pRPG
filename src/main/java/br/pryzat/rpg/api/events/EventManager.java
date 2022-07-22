package br.pryzat.rpg.api.events;

import br.pryzat.rpg.builds.events.ColheitaMaldita;
import br.pryzat.rpg.api.RPG;
import br.pryzat.rpg.main.RpgMain;
import org.bukkit.Location;

import java.text.SimpleDateFormat;
import java.util.*;

public class EventManager {
    private final RpgMain plugin;
    // Custom Events

    private List<String> eventsuids = new ArrayList<>();
    private ColheitaMaldita ecm;

    public EventManager(RpgMain main) {
        this.plugin = main;
    }

    public void loadAllEvents() {
        ecm = new ColheitaMaldita(plugin, "colheitamaldita");
        register(ecm);
    }

    public void saveAllEvents() {
        ecm.saveConfiguration();

        plugin.getLocationManager().getYml().saveConfig();
    }

    public void checkProgramedEvents() {
        SimpleDateFormat sdf = new SimpleDateFormat("hh");
        sdf.setCalendar(new GregorianCalendar());
        String date = sdf.format(new Date(System.currentTimeMillis()));
        switch (date) {
            case "12":
            case "16":
            case "20":
                if (ecm.isEnabled()) ecm.ready(null);
                break;
        }
    }

    public boolean isEvent(String euid){
        return eventsuids.stream().anyMatch(euid::equals);
    }
    public void register(Event e){
        eventsuids.add(e.getEuid());
    }

    public ColheitaMaldita getColheitaMaldita() {
        return ecm;
    }
}
