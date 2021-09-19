package br.pryzat.rpg.api.events;

import br.pryzat.rpg.builds.events.ColheitaMaldita;
import br.pryzat.rpg.main.RpgMain;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;

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
        plugin.getLocationManager().getYml().saveConfig();
    }

    public void checkProgramedEvents() {
        SimpleDateFormat sdf = new SimpleDateFormat("hh");
        sdf.setCalendar(new GregorianCalendar());
        new BukkitRunnable() {
            String date = sdf.format(new Date(System.currentTimeMillis()));

            @Override
            public void run() {
                while (true) {
                    if (ecm.isEnabled()) {
                        switch (date) {
                            case "12":
                                ecm.ready(null);
                            case "16":
                                ecm.ready(null);
                            case "20":
                                ecm.ready(null);
                        }
                    }
                }
            }
        }.runTaskTimerAsynchronously(plugin, 20, 20);
    }

    public ColheitaMaldita getColheitaMaldita() {
        return ecm;
    }
}
