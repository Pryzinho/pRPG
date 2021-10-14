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
import com.comphenix.packetwrapper.WrapperPlayServerPlayerInfo;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.PlayerInfoData;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.comphenix.protocol.wrappers.WrappedGameProfile;
import com.google.common.collect.Lists;
import net.luckperms.api.LuckPerms;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Collections;
import java.util.List;

public class RpgMain extends JavaPlugin {
    private ConfigManager conm;
    private LocationsManager lm;
    private CharacterManager cm;
    private EventManager em;
    // Depends
    private LuckPerms lp;
	private ProtocolManager protocolmanager;

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
		protocolmanager = ProtocolLibrary.getProtocolManager();
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

    public void changePlayerNameAboveHead(Player player, String name) {
        protocolmanager.addPacketListener(new PacketAdapter(this, PacketType.Play.Server.PLAYER_INFO) {
            @Override
            public void onPacketSending(PacketEvent event) {
                WrapperPlayServerPlayerInfo wrapper = new WrapperPlayServerPlayerInfo(event.getPacket());
                Player target = event.getPlayer();

                List<PlayerInfoData> playerInfoDataList = wrapper.getData();
                if (wrapper.getAction() != EnumWrappers.PlayerInfoAction.ADD_PLAYER) {
                    return;
                }
                List<PlayerInfoData> newPlayerInfoDataList = Lists.newArrayList();

                for (PlayerInfoData playerInfoData : playerInfoDataList) {
                    Player p2;

                    if (playerInfoData == null || playerInfoData.getProfile() == null || (p2 = Bukkit.getPlayer(playerInfoData.getProfile().getUUID())) == null || !p2.isOnline()) {
                        newPlayerInfoDataList.add(playerInfoData);
                        continue;
                    }

                    WrappedGameProfile profile = playerInfoData.getProfile();

                    String newNick = new StringBuilder(name).toString();

                    WrappedGameProfile newProfile = profile.withName(newNick);
                    newProfile.getProperties().putAll(profile.getProperties());

                    PlayerInfoData newPlayerInfoData = new PlayerInfoData(newProfile, playerInfoData.getLatency(), playerInfoData.getGameMode(), playerInfoData.getDisplayName());
                    newPlayerInfoDataList.add(newPlayerInfoData);
                }

                wrapper.setData(newPlayerInfoDataList);
            }
        });
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (p.equals(player)) {
                continue;
            }
            p.hidePlayer(this, player);
            p.showPlayer(this, player);
        }
    }
}
