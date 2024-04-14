package br.pryzat.rpg.main;

import br.pryzat.rpg.api.RPG;
import br.pryzat.rpg.api.characters.CharacterManager;
import br.pryzat.rpg.api.characters.classes.ClassesManager;
import br.pryzat.rpg.api.events.EventManager;
import br.pryzat.rpg.api.items.ItemHandler;
import br.pryzat.rpg.commands.ClassCommand;
import br.pryzat.rpg.commands.RpgCommand;
import br.pryzat.rpg.commands.SkillsCommand;
import br.pryzat.rpg.commands.TestCommand;
import br.pryzat.rpg.listeners.PlayerEvent;
import br.pryzat.rpg.utils.*;
import br.pryzat.rpg.utils.packetwrapper.WrapperPlayServerPlayerInfo;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.PlayerInfoData;
import com.comphenix.protocol.wrappers.WrappedGameProfile;
import com.google.common.collect.Lists;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class RpgMain extends JavaPlugin {
    private final String[] DEPENDENCIES = {"ProtocolLib", "Vault", "Citizens", "nLogin"};
    public NamespacedKey REPRESENTATIVE_ITEM_NAMESPACE;
    private ConfigManager conm;
    private LocationsManager lm;
    private ClassesManager classesManager;
    private CharacterManager cm;
    private EventManager em;
    private ItemHandler im;
    // Depends
    private Permission permissionsManager;
    private ProtocolManager protocolmanager;

    //    CitizensAPI.getNPCRegistry();

    // comecei a fazer isso aproximadamente no dia 19/09/2021
    @Override
    public void onEnable() {
        //ArmorEquipEvent.registerListener(this);
        ConsoleCommandSender ccs = Bukkit.getConsoleSender();
        Logger.logInfo(ccs, "&aIniciando &epRPG&f.");
        for (String dependencie : DEPENDENCIES) {
            if (!getServer().getPluginManager().isPluginEnabled(dependencie)) {
                Logger.logError(ccs, "As dependências não foram encontradas.");
                getServer().getPluginManager().disablePlugin(this);
            } else {
                Logger.logInfo(ccs, "Dependência &e" + dependencie + "&a encontrada&f.");
            }
        }
        this.REPRESENTATIVE_ITEM_NAMESPACE = new NamespacedKey(this, "rpg.representative.item");
        RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
        assert rsp != null;
        permissionsManager = rsp.getProvider();
        protocolmanager = ProtocolLibrary.getProtocolManager();
        // CitizensAPI.getTraitFactory().registerTrait(TraitInfo.create(TestTrait.class).withName("teleportador"));

        conm = new ConfigManager(this);
        conm.getYml().saveDefaultConfig();
        lm = new LocationsManager(this);
        lm.getYml().saveDefaultConfig();
        im = new ItemHandler(this);
        im.loadAllItems();
        classesManager = new ClassesManager(this);
        cm = new CharacterManager(this);
        cm.getCharactersYml().saveDefaultConfig();
        cm.loadCharacters();
        em = new EventManager(this);
        em.loadAllEvents();
        if (!permissionsManager.hasGroupSupport()) {
            Logger.logError(ccs, "&cNenhum plugin de permissão com suporte a Grupos foi identificado&f.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        String NEWBIE_GROUP = (getConfigManager().getYml().getString("NEWBIE_GROUP") == null) ? "iniciante" : getConfigManager().getYml().getString("NEWBIE_GROUP");
        if (!Arrays.asList(permissionsManager.getGroups()).contains(NEWBIE_GROUP)) {
            Logger.logError(ccs, "&cNão foi possivel reconheçer o grupo '" + NEWBIE_GROUP + "'&f.&c Crie o grupo em seu plugin de permissão! (" + permissionsManager.getName() + ")");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        RPG.loadStaticAcces();
        Logger.logInfo(ccs, "&aCarregando jogadores&f...");
        Objects.requireNonNull(getCommand("rpg")).setExecutor(new RpgCommand(this));
        Objects.requireNonNull(getCommand("skills")).setExecutor(new SkillsCommand(this));
        Objects.requireNonNull(getCommand("class")).setExecutor(new ClassCommand(this));
        Objects.requireNonNull(getCommand("test")).setExecutor(new TestCommand(this));
        getServer().getPluginManager().registerEvents(new PlayerEvent(this), this);
        em.checkProgramedEvents();
        Logger.logInfo(ccs, "&aInicializado com sucesso!");
    }

    @Override
    public void onDisable() {
        cm.saveCharacters();
        em.saveAllEvents();
        HandlerList.unregisterAll();
    }

    public Permission getPermissionsManager() {
        return permissionsManager;
    }

    public ConfigManager getConfigManager() {
        return conm;
    }

    public LocationsManager getLocationManager() {
        return lm;
    }

    public ItemHandler getItemHandler(){
        return im;
    }
    public ClassesManager getClassesManager() {
        return classesManager;
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
