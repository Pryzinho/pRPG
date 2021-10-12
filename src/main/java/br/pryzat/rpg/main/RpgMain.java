package br.pryzat.rpg.main;

import br.pryzat.rpg.api.RPG;
import br.pryzat.rpg.api.characters.CharacterManager;
import br.pryzat.rpg.api.events.EventManager;
import br.pryzat.rpg.commands.RpgCommand;
import br.pryzat.rpg.commands.SkillsCommand;
import br.pryzat.rpg.events.PlayerEvent;
import br.pryzat.rpg.utils.ConfigManager;
import br.pryzat.rpg.utils.LocationsManager;
import br.pryzat.rpg.utils.Logger;
import net.luckperms.api.LuckPerms;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class RpgMain extends JavaPlugin {
    private ConfigManager conm;
    private LocationsManager lm;
    private CharacterManager cm;
    private EventManager em;
    // Depends
    private LuckPerms lp;

    @Override
    public void onEnable() {
        ConsoleCommandSender ccs = Bukkit.getConsoleSender();
        RegisteredServiceProvider<LuckPerms> provider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
        if (provider == null) {
            Logger.logError(ccs, "Dependência &eLuckPerms&c não foi encontrada.");
            getServer().getPluginManager().disablePlugin(this);
        } else {
            lp = provider.getProvider();
            Logger.logInfo(ccs, "Dependência &eLuckPerms&a encontrada.");
        }
        conm = new ConfigManager(this);
        conm.getYml().saveDefaultConfig();
        lm = new LocationsManager(this);
        lm.getYml().saveDefaultConfig();
        RPG.registerAllSkills();
        cm = new CharacterManager(this);
        cm.getCharactersYml().saveDefaultConfig();
        cm.loadCharacters();
        em = new EventManager(this);
        em.loadAllEvents();
        RPG.registerAllItems();

        Logger.logInfo(ccs, "Carregando jogadores...");
        getCommand("rpg").setExecutor(new RpgCommand(this));
        getCommand("skills").setExecutor(new SkillsCommand(this));
        getCommand("class").setExecutor(new SkillsCommand(this));
        getServer().getPluginManager().registerEvents(new PlayerEvent(this), this);
        em.checkProgramedEvents();
    }

    @Override
    public void onDisable() {
        cm.saveCharacters();
        em.registerAllEvents();
        HandlerList.unregisterAll();
    }

    public LuckPerms getLuckPerms(){
        return lp;
    }
    public ConfigManager getConfigManager(){
        return conm;
    }
    public LocationsManager getLocationManager() {
        return lm;
    }

    public CharacterManager getCharacterManager() {
        return cm;
    }

    public EventManager getEventManager() {
        return em;
    }
}
