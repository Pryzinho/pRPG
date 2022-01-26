package br.pryzat.rpg.bukkit.main;

import br.pryzat.rpg.bot.BotMain;
import br.pryzat.rpg.bukkit.api.RPG;
import br.pryzat.rpg.bukkit.api.characters.CharacterManager;
import br.pryzat.rpg.bukkit.api.events.EventManager;
import br.pryzat.rpg.bukkit.builds.npcs.TestTrait;
import br.pryzat.rpg.bukkit.commands.RpgCommand;
import br.pryzat.rpg.bukkit.commands.SkillsCommand;
import br.pryzat.rpg.bukkit.commands.TestCommand;
import br.pryzat.rpg.bukkit.events.PlayerEvent;
import br.pryzat.rpg.bukkit.utils.ConfigManager;
import br.pryzat.rpg.bukkit.utils.LocationsManager;
import br.pryzat.rpg.bukkit.utils.Logger;
import com.comphenix.packetwrapper.WrapperPlayServerPlayerInfo;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.PlayerInfoData;
import com.comphenix.protocol.wrappers.WrappedGameProfile;
import com.google.common.collect.Lists;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.trait.TraitInfo;
import net.luckperms.api.LuckPerms;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class RpgMain extends JavaPlugin {
    private ConfigManager conm;
    private LocationsManager lm;
    private CharacterManager cm;
    private EventManager em;
    // Depends
    private LuckPerms lp;
    private ProtocolManager protocolmanager;

    //    CitizensAPI.getNPCRegistry();
    // Discord Integration
    //private BotMain bm;

    @Override
    public void onEnable() {
        ConsoleCommandSender ccs = Bukkit.getConsoleSender();
        String[] dependencies = {"ProtocolLib", "PacketWrapper", "LuckPerms", "Citizens"};
        int d = 0;
        for (Plugin pl : getServer().getPluginManager().getPlugins()) {
            for (String dependencie : dependencies) {
                if (pl.getName().equalsIgnoreCase(dependencie)) {
                    Logger.logInfo(ccs, "Dependência &e" + dependencie + "&a encontrada&f.");
                    d++;
                }
            }
        }
        if (d != 4) {
            Logger.logError(ccs, "As dependências não foram encontradas.");
            getServer().getPluginManager().disablePlugin(this);
        } else {
            RegisteredServiceProvider<LuckPerms> lp_provider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
            lp = lp_provider.getProvider();
            protocolmanager = ProtocolLibrary.getProtocolManager();
        }
       // CitizensAPI.getTraitFactory().registerTrait(TraitInfo.create(TestTrait.class).withName("teleportador"));

        conm = new ConfigManager(this);
        conm.getYml().saveDefaultConfig();
     //   bm = new BotMain(this);
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
        getCommand("test").setExecutor(new TestCommand(this));
        getServer().getPluginManager().registerEvents(new PlayerEvent(this), this);
        em.checkProgramedEvents();
    }

    @Override
    public void onDisable() {
        cm.saveCharacters();
        em.registerAllEvents();
        HandlerList.unregisterAll();
    }

    public LuckPerms getLuckPerms() {
        return lp;
    }

    public ConfigManager getConfigManager() {
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
/*
    public JDA getBot() {
        return bm.getJDA();
    }
*/
 /*
 public BotMain getBotMain() {
     return bm;
    }
	*/
	public void changePlayerNameAboveHead(Player player, String name) {
	// do nothing	
	}
/*
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
	*/
}
